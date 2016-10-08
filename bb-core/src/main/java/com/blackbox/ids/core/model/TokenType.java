package com.blackbox.ids.core.model;

public enum TokenType {

	FORGET_PASSWORD("FP"), OTP("OTP");

	private String type;

	private TokenType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the token type.
	 *
	 * @param type
	 *            the type
	 * @return the token type
	 */
	public static TokenType getTokenType(String type) {
		TokenType tokenType = null;
		if (type != null) {
			for (TokenType token : TokenType.values()) {
				if (type.equals(token.getType())) {
					tokenType = token;
					break;
				}
			}
		}
		return tokenType;
	}

}
