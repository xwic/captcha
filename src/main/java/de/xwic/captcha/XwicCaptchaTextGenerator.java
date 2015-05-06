package de.xwic.captcha;

import java.util.Random;

/**
 *
 */
public final class XwicCaptchaTextGenerator {

	private char[] possibleCharacters;
	private final Random rand = new Random();

	/**
	 * @param captchaCharacters
	 */
	XwicCaptchaTextGenerator(final String captchaCharacters) {
		this.possibleCharacters = captchaCharacters.toCharArray();
	}

	/**
	 * @return the random text
	 */
	char[] randomCharacters(final int length) {
		final char[] chars = new char[length];

		for (int i = 0; i < length; i++) {
			final int index = rand.nextInt(possibleCharacters.length);
			chars[i] = possibleCharacters[index];
		}
		return chars;
	}


	/**
	 * @return the possibleCharacters
	 */
	public char[] getPossibleCharacters() {
		return possibleCharacters;
	}


	/**
	 * @param possibleCharacters
	 *            the possibleCharacters to set
	 */
	public void setPossibleCharacters(final char[] possibleCharacters) {
		this.possibleCharacters = possibleCharacters;
	}

}
