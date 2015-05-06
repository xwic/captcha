package de.xwic.captcha;

import java.awt.image.BufferedImage;

/**
 *
 */
public final class XwicCaptcha {

	public final String text;
	public final BufferedImage image;

	/**
	 * @param text
	 * @param image
	 */
	public XwicCaptcha(final String text, final BufferedImage image) {
		this.text = text;
		this.image = image;
	}

}
