package com.blackbox.ids.scheduler.email.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.blackbox.ids.core.ApplicationException;
import com.blackbox.ids.core.TemplateType;
import com.blackbox.ids.core.constant.Constant;
import com.blackbox.ids.core.model.email.Message;
import com.blackbox.ids.core.model.email.MessageData;
import com.blackbox.ids.core.repository.email.MessageDataRepository;
import com.blackbox.ids.core.repository.email.MessageRepository;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * The Class AbstractMessageHandler.
 * @author Nagarro
 */
public abstract class AbstractMessageHandler implements MessageHandler {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMessageHandler.class);

	/** The template type. */
	protected TemplateType templateType;

	/** The email sender. */
	protected JavaMailSender emailSender;

	/** The subject template. */
	protected String subjectTemplate;

	/** The content template. */
	protected String contentTemplate;

	/** The mail sender. */
	protected String mailSender;

	/** The props. */
	protected Properties props;

	/** The is html. */
	protected boolean isHTML;

	/** The sender name. */
	protected String senderName;

	/** The message repository. */
	@Autowired
	public MessageRepository messageRepository;

	/** The message data repository. */
	@Autowired
	public MessageDataRepository messageDataRepository;

	/** The cfg. */
	private Configuration cfg;

	/**
	 * Instantiates a new abstract message handler.
	 * @param templateType
	 *            the template type
	 */
	public AbstractMessageHandler(final TemplateType templateType)
	{
		this.templateType = templateType;
		init();
	}

	/**
	 * This method initialize and load email configuration related resources.
	 */
	private void init() {
		try {
			final Resource res = new ClassPathResource(Constant.EMAIL_TEMPLATE_FOLDER_PATH
					+ templateType.getTemplateName() + Constant.FORWARDSLASH + Constant.EMAIL_TEMPLATE_PROPERTY_FILE);
			props = new Properties();
			props.load(res.getInputStream());
			cfg = new Configuration(Configuration.VERSION_2_3_22);
			cfg.setClassForTemplateLoading(this.getClass(),
					Constant.EMAIL_TEMPLATE_FOLDER_PATH + templateType.getTemplateName() + Constant.FORWARDSLASH);
			cfg.setDefaultEncoding("UTF-8");
			contentTemplate = Constant.EMAIL_PROPERTY_TEMPLATE_BODY;
			subjectTemplate = props.getProperty(Constant.EMAIL_PROPERTY_TEMPLATE_SUBJECT);
			mailSender = props.getProperty(Constant.EMAIL_PROPERTY_TEMPLATE_FROM);
			isHTML = isHTMLTemplate();
			senderName = props.getProperty(Constant.EMAIL_PROPERTY_TEMPLATE_SENDER_NAME);
		} catch (final Exception e) {
			throw new ApplicationException(e);
		}

	}

	@Override
	@Transactional
	public final void handle(final Message message) throws Exception {
		process(message);
	}

	/**
	 * This API actually process the email.
	 * @param message
	 *            the message
	 */
	private void process(final Message message) {
		try {
			final MimeMessage mimeMessage = emailSender.createMimeMessage();
			final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			messageHelper.setFrom(mailSender);
			messageHelper.setTo(message.getReceiverList().split(Constant.SEMI_COLON));
			final Map<String, String> map = new HashMap<>();
			doHandleInternal(message, messageHelper, map);
			messageHelper.setSubject(processSubject(message.getId()));
			final String actualMessage = processContent(message.getId(), map);
			messageHelper.setText(actualMessage, isHTML);
			emailSender.send(mimeMessage);
		} catch (final Exception e) {
			LOGGER.error("Exception during email processing: ", e);
			throw new ApplicationException(e);
		}
	}

	/**
	 * Do handle internal.
	 * @param message
	 *            the message
	 * @param messageHelper
	 *            the message helper
	 * @param map
	 *            the map
	 * @throws ApplicationException
	 *             the application exception
	 */
	protected abstract void doHandleInternal(Message message, MimeMessageHelper messageHelper, Map<String, String> map);

	@Override
	public Collection<Message> queryMessages() {
		return messageRepository.findByTemplateTypeAndStatus(templateType, Constant.MESSAGE_STATUS_NOT_SENT);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int lock(final Message message) {
		return messageRepository.lockMessage(message.getId(), message.getVersion(),
				Constant.MESSAGE_STATUS_IN_PROGRESS);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void markAsComplete(final Message message) {
		messageRepository.markStatus(message.getId(), Constant.MESSAGE_STATUS_SENT);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void markAsError(final Message message) {
		messageRepository.markStatus(message.getId(), Constant.MESSAGE_STATUS_FAIL);
	}

	/**
	 * Process content.
	 * @param messageId
	 *            the message id
	 * @param map
	 *            the map
	 * @return the string
	 * @throws TemplateException
	 *             the template exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String processContent(final long messageId, final Map<String, String> map)
			throws TemplateException, IOException {
		OutputStream ops = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(ops);
		String content = Constant.EMPTY_STRING;
		try {
			final Collection<MessageData> messageDatas = messageDataRepository.findByMessageId(messageId);
			final Map<String, String> messagePair = new LinkedHashMap<String, String>();
			for (final MessageData messageData : messageDatas) {
				messagePair.put(messageData.getKey(), messageData.getValue());
			}
			if (messagePair.get(Constant.EMAIL_PROPERTY_TEMPLATE_SENDER_NAME) == null) {
				messagePair.put(Constant.EMAIL_PLACEHOLDER_SENDER_NAME, senderName);
			}
			if (!CollectionUtils.isEmpty(map)) {
				messagePair.putAll(map);
			}
			cfg.getTemplate(contentTemplate).process(messagePair, out);
		} finally {
			content = ops.toString();
			ops.close();
			out.close();
		}
		return content;
	}

	/**
	 * Process subject.
	 * @param messageId
	 *            the message id
	 * @return the string
	 * @throws TemplateException
	 *             the template exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String processSubject(final long messageId) throws TemplateException, IOException {
		final Collection<MessageData> messageDatas = messageDataRepository.findByMessageId(messageId);
		final Map<String, String> messagePair = new LinkedHashMap<String, String>();
		for (final MessageData messageData : messageDatas) {
			messagePair.put(messageData.getKey(), messageData.getValue());
		}
		final StrSubstitutor sub = new StrSubstitutor(messagePair);
		return sub.replace(subjectTemplate);
	}

	/**
	 * Gets the email sender.
	 * @return the email sender
	 */
	public JavaMailSender getEmailSender() {
		return emailSender;
	}

	/**
	 * Sets the email sender.
	 * @param emailSender
	 *            the new email sender
	 */
	public void setEmailSender(final JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	/**
	 * Gets the subject template.
	 * @return the subject template
	 */
	public String getSubjectTemplate() {
		return subjectTemplate;
	}

	/**
	 * Sets the subject template.
	 * @param subjectTemplate
	 *            the new subject template
	 */
	public void setSubjectTemplate(final String subjectTemplate) {
		this.subjectTemplate = subjectTemplate;
	}

	/**
	 * Gets the content template.
	 * @return the content template
	 */
	public String getContentTemplate() {
		return contentTemplate;
	}

	/**
	 * Sets the content template.
	 * @param contentTemplate
	 *            the new content template
	 */
	public void setContentTemplate(final String contentTemplate) {
		this.contentTemplate = contentTemplate;
	}

	/**
	 * Gets the cfg.
	 * @return the cfg
	 */
	public Configuration getCfg() {
		return cfg;
	}

	/**
	 * Gets the template type.
	 * @return the template type
	 */
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * Sets the template type.
	 * @param templateType
	 *            the new template type
	 */
	public void setTemplateType(final TemplateType templateType) {
		this.templateType = templateType;
	}

	/**
	 * Gets the message repository.
	 * @return the message repository
	 */
	public MessageRepository getMessageRepository() {
		return messageRepository;
	}

	/**
	 * Sets the message repository.
	 * @param messageRepository
	 *            the new message repository
	 */
	public void setMessageRepository(final MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	/**
	 * Gets the message data repository.
	 * @return the message data repository
	 */
	public MessageDataRepository getMessageDataRepository() {
		return messageDataRepository;
	}

	/**
	 * Sets the message data repository.
	 * @param messageDataRepository
	 *            the new message data repository
	 */
	public void setMessageDataRepository(final MessageDataRepository messageDataRepository) {
		this.messageDataRepository = messageDataRepository;
	}

	/**
	 * Gets the props.
	 * @return the props
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * Gets the mail sender.
	 * @return the mail sender
	 */
	public String getMailSender() {
		return mailSender;
	}

	/**
	 * Checks if is HTML template.
	 * @return true, if is HTML template
	 */
	private boolean isHTMLTemplate() {
		final String htmlString = props.getProperty(Constant.EMAIL_PROPERTY_TEMPLATE_IS_HTML);
		return htmlString.isEmpty() ? false
				: (htmlString.equalsIgnoreCase(Constant.EMAIL_PROPERTY_TEMPLATE_HTML_CONTENT) ? true : false);
	}

	/**
	 * Checks if is html.
	 * @return true, if is html
	 */
	public boolean isHTML() {
		return isHTML;
	}
}
