package com.blackbox.ids.common.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.blackbox.ids.common.constant.Constant;

@Configuration
@PropertySource(value = { "classpath:/META-INF/application.properties" })
public class PropertyConfig {

	@Autowired
	Environment env;

	@Bean
	public MailConfiguration getMailConfiguration() {
		MailConfiguration configuration = new MailConfiguration();
		configuration.setSmtpHost(env.getProperty(Constant.SMTP_HOST));
		configuration.setSmtpPort(Integer.valueOf(env.getProperty(Constant.SMTP_PORT)));
		configuration.setSmtpUser(Constant.SMTP_USER);
		configuration.setSmtpPassword(Constant.SMTP_Password);
		return configuration;
	}
	

}