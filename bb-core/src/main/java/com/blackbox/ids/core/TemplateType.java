package com.blackbox.ids.core;

import org.springframework.util.StringUtils;

public enum TemplateType {
	OTP("otp"), 
	FORGOT_PASSWORD("forgot_password"), 
	ACCOUNT_LOCKED("account_locked"), 
	PAIR_AUDIT_REPORT("pair_audit_report"), 
	CORRUPTED_AUDIT_FILE("corrupted_audit_file"), 
	BULK_FILE_ERROR("bulk_file_error"), 
	CRAWLER_NOTIFICATION("crawler_notification"), 
	CRAWLER_AUTHENTICATION("crawler_authentication"), 
	URGENT_IDS_REPORT("urgent_ids_report"),
	IDS_TRACKING_FAILED("ids_tracking_failed"),
	NEW_REF_UPDATE("new_ref_update"),
	NOTIFICATION_EMAIL("notification_email");

	private String	templateName;

	private TemplateType(final String templateName)
	{
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public static TemplateType getTemplateTypeByName(String templateName) {
		TemplateType templateType = null;
		if (!StringUtils.isEmpty(templateName)) {
			TemplateType[] templateTypes = TemplateType.values();
			for (TemplateType key : templateTypes) {
				if (templateName.equalsIgnoreCase(key.getTemplateName())) {
					templateType = key;
					break;
				}
			}
		}
		return templateType;
	}
}
