package com.blackbox.ids.common.constant;

public enum OTPStatus {
	
	SUCCESS(1, "OTP was sent "), FAIL(0, "OTP Failed");
    private int code;
    private String message;
    

    private OTPStatus(int code, String message) {
          this.code = code;
          this.message=message;
    }

    public int getCode() {
          return code;
    }

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
    
}
