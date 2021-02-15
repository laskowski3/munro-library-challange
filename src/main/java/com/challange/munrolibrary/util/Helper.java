package com.challange.munrolibrary.util;

import com.challange.munrolibrary.constants.AppConstants;

public class Helper {

	public static boolean isNumericDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isNumericInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isAscOrDesc(String value) {
		if (value.equalsIgnoreCase(AppConstants.ASC) || value.equalsIgnoreCase(AppConstants.DESC)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMinSmallerThanMax(String min, String max) {
		if (Double.parseDouble(min) <= Double.parseDouble(max)) {
			return true;
		} else {
			return false;
		}
	}
}
