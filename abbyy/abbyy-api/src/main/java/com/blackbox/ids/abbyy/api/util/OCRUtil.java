package com.blackbox.ids.abbyy.api.util;

public class OCRUtil {

    /**
     * This method will concatenate all the string passed in parameters and crate a single string and send back as
     * response.
     *
     * @param strings
     *            the strings
     * @return the string
     */
    public static String concat(final String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
        }
        return sb.toString();
    }
}
