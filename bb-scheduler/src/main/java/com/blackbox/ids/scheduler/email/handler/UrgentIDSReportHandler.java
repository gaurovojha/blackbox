package com.blackbox.ids.scheduler.email.handler;

import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.util.BlackboxUtils;

public class UrgentIDSReportHandler extends AbstractMessageHandler {
	
	@Value("${root.folder.dir}")
	private String rootDir;
	
	private static final String IDS = "IDS";
	private static final String REPORT = "UrgentRecordReport";
	//private static final String URGENT_IDS= "UrgentIDS_";

	public UrgentIDSReportHandler(final TemplateType templateType)
	{
		super(templateType);
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}



	@Override
	protected void doHandleInternal(Message message, MimeMessageHelper messageHelper, Map<String, String> map)
			throws ApplicationException {
		
		String reportQualifiedName = BlackboxUtils.concat(rootDir, File.separator, IDS, File.separator, REPORT);	
		//String targetFolder = FolderRelativePathEnum.CORRESPONDENCE.getAbsolutePath("temp");
		String fileName = "";
		Set<MessageData> messageDatas = message.getMessageData();
		if (!CollectionUtils.isEmpty(messageDatas)) {
			for (MessageData messageData : messageDatas) {
				if (messageData.getKey().equals(com.blackbox.ids.core.constant.Constant.EMAIL_PLACEHOLDER_URGENT_IDS_REPORT_NAME)) {
					fileName = messageData.getValue();
				}
			}
		}
		File file = new File(BlackboxUtils.concat(reportQualifiedName,File.separator,fileName));
		try {
			messageHelper.addAttachment(file.getName(), file);
		} catch (MessagingException e) {
			throw new ApplicationException(e);
		}
	}

}
