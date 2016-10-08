/**
 *
 */
package com.blackbox.ids.core.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.blackbox.ids.core.model.enums.FilePackage;

/**
 * The utility class {@code FileIOUtil} exposes APIs to perform file IO.
 *
 * @author ajay2258
 */
public class FileIOUtil {

	/** The system-dependent default name-separator character. */
	private static final String SEPARATOR = File.separator;

	public static String getAbsolutePath(final FilePackage filePackage) {
		return filePackage.rootFolder.getAbsolutePath().concat(SEPARATOR).concat(filePackage.packageName);
	}

	/**
	 * Writes bytes to a file under given name. Parameter <tt>filePackage</tt> helps determine the folder in file
	 * system.
	 * <p/>
	 * Please note that CREATE, TRUNCATE_EXISTING, and WRITE options are being applied, i.e. it opens the file for
	 * writing, creating the file if it doesn't exist, or initially truncating an existing regular-file to a size of 0.
	 * All bytes in the byte array are written to the file.
	 *
	 * @param filePackage
	 *            helps find the file path
	 * @param fileName
	 *            name of the file with extension
	 * @param fileData
	 *            the byte array with the bytes to write
	 * @throws IOException
	 *             if an I/O error occurs writing to or creating the file
	 * @throws SecurityException
	 *             In the case of the default provider, and a security manager is installed, the
	 *             {@link SecurityManager#checkWrite(String) checkWrite} method is invoked to check write access to the
	 *             file.
	 */
	public static void saveFile(final FilePackage filePackage, final String fileName, final byte[] fileData)
			throws IOException {

		final String uploadDirectory = getAbsolutePath(filePackage);
		new File(uploadDirectory).mkdirs();
		final String filePath = uploadDirectory.concat(SEPARATOR).concat(fileName);
		Files.write(Paths.get(filePath), fileData);
	}

}
