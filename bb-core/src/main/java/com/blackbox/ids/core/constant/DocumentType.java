package com.blackbox.ids.core.constant;

public enum DocumentType {

	N417(1);
	public int code;

	private DocumentType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
