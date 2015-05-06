package de.xwic.captcha;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TransformFilter;
import com.jhlabs.image.WaterFilter;

/**
 * based on com.google.code.kaptcha
 */
public final class XwicCaptchaRenderer {

	private final int width;
	private final int height;

	private final XwicCaptchaConfig config;

	/**
	 * @param config
	 */
	public XwicCaptchaRenderer(final XwicCaptchaConfig config) {
		this.config = config;
		this.width = config.getWidth();
		this.height = config.getHeight();
	}

	/**
	 * @param len
	 * @return
	 */
	public XwicCaptcha createCaptchaImage(final int len) {
		final char[] randomCharacters = config.getTextGenerator().randomCharacters(len);

		BufferedImage bi = renderWord(randomCharacters);
		bi = distort(bi);
		bi = addBackground(bi);
		final Graphics2D graphics = bi.createGraphics();

		if (config.isDrawBorder()) {
			drawBorder(graphics);
		}

		return new XwicCaptcha(new String(randomCharacters), bi);
	}

	/**
	 * @param graphics
	 */
	private void drawBorder(final Graphics2D graphics) {
		graphics.setColor(config.getBorderColor());
		graphics.draw(new Line2D.Double(0, 0, 0, width));
		graphics.draw(new Line2D.Double(0, 0, width, 0));
		graphics.draw(new Line2D.Double(0, height - 1, width, height - 1));
		graphics.draw(new Line2D.Double(width - 1, height - 1, width - 1, 0));
	}

	/**
	 * @param from
	 * @return
	 */
	private BufferedImage addBackground(final BufferedImage from) {
		final int width = from.getWidth();
		final int height = from.getHeight();

		final BufferedImage to = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		final Graphics2D g2d = to.createGraphics();
		g2d.setColor(config.getBackgroundColor());
		g2d.fill(new Rectangle2D.Double(0, 0, width, height));
		g2d.drawImage(from, 0, 0, null);
		return to;
	}

	/**
	 * @param image
	 * @param factorOne
	 * @param factorTwo
	 * @param factorThree
	 * @param factorFour
	 * @param color
	 */
	private static void makeNoise(final BufferedImage image, final float factorOne, final float factorTwo, final float factorThree,
			final float factorFour, final Color color) {

		// image size
		final int width = image.getWidth();
		final int height = image.getHeight();

		// the points where the line changes the stroke and direction
		Point2D[] pts = null;
		final Random rand = new Random();

		// the curve from where the points are taken
		final CubicCurve2D cc = new CubicCurve2D.Float(width * factorOne, height * rand.nextFloat(), width * factorTwo, height
				* rand.nextFloat(), width * factorThree, height * rand.nextFloat(), width * factorFour, height * rand.nextFloat());

		// creates an iterator to define the boundary of the flattened curve
		final PathIterator pi = cc.getPathIterator(null, 2);
		final Point2D tmp[] = new Point2D[200];
		int i = 0;

		// while pi is iterating the curve, adds points to tmp array
		while (!pi.isDone()) {
			final float[] coords = new float[6];
			switch (pi.currentSegment(coords)) {
			case PathIterator.SEG_MOVETO:
			case PathIterator.SEG_LINETO:
				tmp[i] = new Point2D.Float(coords[0], coords[1]);
			}
			i++;
			pi.next();
		}

		pts = new Point2D[i];
		System.arraycopy(tmp, 0, pts, 0, i);

		final Graphics2D graph = (Graphics2D) image.getGraphics();
		graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

		graph.setColor(color);

		// for the maximum 3 point change the stroke and direction
		for (i = 0; i < pts.length - 1; i++) {
			if (i < 3) {
				graph.setStroke(new BasicStroke(0.9f * (4 - i)));
			}
			graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(), (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
		}

		graph.dispose();
	}

	/**
	 * @param baseImage
	 * @return
	 */
	private BufferedImage distort(final BufferedImage baseImage) {
		final BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);

		final Graphics2D graphics = (Graphics2D) distortedImage.getGraphics();

		final RippleFilter rippleFilter = new RippleFilter();
		rippleFilter.setWaveType(RippleFilter.SINE);
		rippleFilter.setXAmplitude(2.6f);
		rippleFilter.setYAmplitude(1.7f);
		rippleFilter.setXWavelength(15);
		rippleFilter.setYWavelength(5);
		rippleFilter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);

		final WaterFilter waterFilter = new WaterFilter();
		waterFilter.setAmplitude(1.5f);
		waterFilter.setPhase(10);
		waterFilter.setWavelength(2);

		BufferedImage effectImage = waterFilter.filter(baseImage, null);
		effectImage = rippleFilter.filter(effectImage, null);

		graphics.drawImage(effectImage, 0, 0, null, null);

		graphics.dispose();

		if (config.isAddNoise()) {
			makeNoise(distortedImage, .1f, .1f, .25f, .25f, config.getNoiseColor());
			makeNoise(distortedImage, .1f, .25f, .5f, .9f, config.getNoiseColor());
		}
		return distortedImage;
	}

	/**
	 * @param wordChars
	 * @return
	 */
	private BufferedImage renderWord(final char[] wordChars) {
		final int width = config.getWidth();
		final int height = config.getHeight();

		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2D = image.createGraphics();
		g2D.setColor(config.getTextColor());

		final RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
		g2D.setRenderingHints(hints);

		final FontRenderContext frc = g2D.getFontRenderContext();
		final Random random = new Random();

		final int fontSize = config.getFontSize();
		final int startPosY = (height - fontSize) / 5 + fontSize;

		final Font[] chosenFonts = new Font[wordChars.length];
		final int[] charWidths = new int[wordChars.length];
		int widthNeeded = 0;
		final List<Font> fonts = config.getFonts();
		final int size = fonts.size();
		for (int i = 0; i < wordChars.length; i++) {

			chosenFonts[i] = fonts.get(random.nextInt(size));

			final char[] charToDraw = new char[] { wordChars[i] };
			final GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
			charWidths[i] = (int) gv.getVisualBounds().getWidth();
			if (i > 0) {
				widthNeeded = widthNeeded + 2;
			}
			widthNeeded = widthNeeded + charWidths[i];
		}

		final int charSpace = config.getCharacterSpacing();

		int startPosX = (width - widthNeeded) / 2;
		for (int i = 0; i < wordChars.length; i++) {
			g2D.setFont(chosenFonts[i]);
			final char[] charToDraw = new char[] { wordChars[i] };
			g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
			startPosX = startPosX + charWidths[i] + charSpace;
		}

		return image;
	}

}
