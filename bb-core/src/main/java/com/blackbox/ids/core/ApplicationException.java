package com.blackbox.ids.core;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public final int code;
	public final String message;
	private Throwable linkedException;

	public ApplicationException(final String message) {
		super(message);
		code = 500;
		this.message = message;
	}

	public ApplicationException(final int code, final String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public ApplicationException(final int code, final String message, final Throwable linkedException) {
		super(message);
		this.code = code;
		this.message = message;
		this.linkedException = linkedException;
	}

	public ApplicationException(final Throwable cause, final String message, final Object... params) {
		super(String.format(message, params), cause);
		this.code = 500;
		this.message = super.getMessage();
		this.linkedException = cause;
	}

	public ApplicationException(final Throwable linkedException) {
		super(linkedException.getMessage());
		this.code = 500;
		this.message = linkedException.getMessage();
		this.linkedException = linkedException;
	}

	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public Throwable getLinkedException() {
		return linkedException;
	}
}
