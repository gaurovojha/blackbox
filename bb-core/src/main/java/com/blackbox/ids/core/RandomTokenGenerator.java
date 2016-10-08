package com.blackbox.ids.core;

import java.util.Random;

public class RandomTokenGenerator {
	private static final String	LOWER_CASE_CHARS	= "abcdefghijklmnopqrstuvwxyz";
	private static final String	UPPER_CASE_CHARS	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String	SPECIAL_CHARS		= "!@#$%^&*";
	private static final String	INTEGER_CHARS		= "0123456789";
	private static Random		random				= new Random();

	private RandomTokenGenerator()
	{}

	public static String generateRandomPassword() {
		StringBuilder pass = new StringBuilder();
		int spot = random.nextInt(25);
		pass.append(UPPER_CASE_CHARS.charAt(spot));
		spot = random.nextInt(9);
		pass.append(INTEGER_CHARS.charAt(spot));
		spot = random.nextInt(7);
		pass.append(SPECIAL_CHARS.charAt(spot));
		while (pass.length() != 8) {
			int rPick = random.nextInt(4);
			if (rPick == 0) {
				spot = random.nextInt(25);
				pass.append(LOWER_CASE_CHARS.charAt(spot));
			} else if (rPick == 1) {
				spot = random.nextInt(25);
				pass.append(UPPER_CASE_CHARS.charAt(spot));
			} else if (rPick == 2) {
				spot = random.nextInt(7);
				pass.append(SPECIAL_CHARS.charAt(spot));
			} else if (rPick == 3) {
				spot = random.nextInt(9);
				pass.append(INTEGER_CHARS.charAt(spot));
			}
		}
		return pass.toString();
	}

	public static String generateRandomOTP() {
		StringBuffer pass = new StringBuffer();
		while (pass.length() != 6) {
			pass.append(INTEGER_CHARS.charAt(random.nextInt(9)));
		}
		return pass.toString();
	}
}