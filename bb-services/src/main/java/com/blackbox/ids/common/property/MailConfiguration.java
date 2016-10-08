package com.blackbox.ids.common.property;

// This class holds information about 
public class MailConfiguration {

	private String smtpHost;

	private int smtpPort;

	private String smtpUser;

	private String smtpPassword;

	public String getSmtpHost() {
		return smtpHost;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

}
