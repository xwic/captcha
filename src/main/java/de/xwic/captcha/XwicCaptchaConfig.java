package de.xwic.captcha;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public final class XwicCaptchaConfig {

	private int fontSize = 40;
	private List<Font> fonts = createFonts(fontSize);

	private int characterSpacing = 3;

	private static final Color DEFAULT_COLOR = BLACK;

	private Color borderColor = DEFAULT_COLOR;
	private Color backgroundColor = WHITE;
	private Color noiseColor = DEFAULT_COLOR;
	private Color textColor = DEFAULT_COLOR;

	private boolean addNoise = true;
	private boolean drawBorder = true;

	private int width = 200;
	private int height = 50;

	private final XwicCaptchaTextGenerator textGenerator = new XwicCaptchaTextGenerator("abcde2345678gfynmnpwx");

	/**
	 * @return
	 */
	public boolean isDrawBorder() {
		return drawBorder;
	}

	/**
	 * @param drawBorder
	 */
	public void setDrawBorder(final boolean drawBorder) {
		this.drawBorder = drawBorder;
	}

	/**
	 * @return
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param borderColor
	 */
	public void setBorderColor(final Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 */
	public void setWidth(final int width) {
		this.width = width;
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 */
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * @param backgroundColor
	 */
	public void setBackgroundColor(final Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * @return
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @return
	 */
	public Color getNoiseColor() {
		return noiseColor;
	}

	/**
	 * @param noiseColor
	 */
	public void setNoiseColor(final Color noiseColor) {
		this.noiseColor = noiseColor;
	}

	/**
	 * @return
	 */
	public int getCharacterSpacing() {
		return characterSpacing;
	}

	/**
	 * @param fontSize
	 */
	public void setFontSize(final int fontSize) {
		this.fontSize = fontSize;
		fonts = createFonts(fontSize);
	}

	/**
	 * @return
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * @return
	 */
	public List<Font> getFonts() {
		return fonts;
	}

	/**
	 * @param textColor
	 */
	public void setTextColor(final Color textColor) {
		this.textColor = textColor;
	}

	/**
	 * @return
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * @param characterSpacing
	 */
	public void setCharacterSpacing(final int characterSpacing) {
		this.characterSpacing = characterSpacing;
	}

	/**
	 * @param addNoise
	 */
	public void setAddNoise(final boolean addNoise) {
		this.addNoise = addNoise;
	}

	/**
	 * @return
	 */
	public boolean isAddNoise() {
		return addNoise;
	}

	/**
	 * @param fontSize
	 * @return
	 */
	private static List<Font> createFonts(final int fontSize) {
		return Arrays.asList(new Font("Arial", Font.BOLD, fontSize), new Font(
				"Courier", Font.BOLD, fontSize));
	}

	/**
	 * @return the textGenerator
	 */
	public XwicCaptchaTextGenerator getTextGenerator() {
		return textGenerator;
	}

}
