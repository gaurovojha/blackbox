package com.blackbox.ids.ui.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.blackbox.ids.core.util.BlackboxUtils;

@Component
public class BlackboxFileHelper {
	
	private static final Logger log = Logger.getLogger(BlackboxFileHelper.class);
	
	@Value("${root.folder.dir}")
	private String rootDir;
	
	public enum Source {
		CORRESPONDENCE ("correspondence"),
		BULK_UPLOAD("bulk_upload"),
		NPL_REFERENCE("npl_reference"),
		FP_REFERENCE("fp_reference"),
		OCRED_DOCUMENT("ocred_document"),
		CRAWLER("crawler");;
		
		private String dir;
		
		private Source(String dir) {
			this.dir = dir;
		}
		
		public String getDir() {
			return dir;
		}
	}
	
	public void viewFile(HttpServletResponse response, Source source, String id, String fileName) throws IOException {
		String filePath = BlackboxUtils.concat(rootDir, File.separator, source.getDir(), File.separator, id, File.separator, fileName);
		viewFile(response, new File(filePath));
	}

	public void viewFile(HttpServletResponse response, File file) throws IOException {
		if (!file.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		
		if (mimeType == null) {
			log.warn("mimetype is not detectable, will take default");
			mimeType = "application/pdf";
		}
		
		response.setContentType(mimeType);


		/*
		 * "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser]
		 * right on browser while others(zip e.g) will be directly downloaded [may provide save as popup, based on your
		 * browser setting.]
		 */

		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

		/*
		 * "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your
		 * browser setting
		 */
		// response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		// Copy bytes from source to destination(outputstream in this example), closes both streams.
		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}
}