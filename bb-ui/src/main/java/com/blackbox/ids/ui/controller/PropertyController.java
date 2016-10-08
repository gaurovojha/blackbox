/**
 *
 */
package com.blackbox.ids.ui.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ajay2258
 *
 */
@Controller
@RequestMapping(value = "/i18n/")
public class PropertyController {

	public static final String FILE_FORMAT = "UTF-8";

	private Logger log = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/loadProp/{file}", method = RequestMethod.GET)
	@ResponseBody
	public void loadProperties(@PathVariable("file") final String fileName,
			final HttpServletResponse response) throws UnsupportedEncodingException {

		String propName = URLDecoder.decode(fileName.split("_")[0], FILE_FORMAT) + ".properties";
		readProp(propName, response);
	}

	@RequestMapping(value = "/loadProp/{path}/{file}", method = RequestMethod.GET)
	@ResponseBody
	public void loadProperties2(@PathVariable("path") final String path, @PathVariable("file") final String fileName,
			final HttpServletResponse response) throws UnsupportedEncodingException {

		String propName = path + "/" +  URLDecoder.decode((fileName.split("_")[0] + ".properties"), FILE_FORMAT);
		readProp(propName, response);
	}

	private void readProp(final String propName, final HttpServletResponse response) throws UnsupportedEncodingException {
		InputStream inStream = this.getClass().getClassLoader().getResourceAsStream(propName);
		String line = null;
		StringBuffer stringBuffer = new StringBuffer();

		if (inStream != null) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream, FILE_FORMAT));
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; file=" + propName);
			try {
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line).append("\n");
				}
				response.getOutputStream().write(stringBuffer.toString().getBytes(FILE_FORMAT));
			} catch (IOException e) {
				log.error(MessageFormat.format("IOException occurred while writing resource file {0}.", propName), e);
			}
		}
	}

}
