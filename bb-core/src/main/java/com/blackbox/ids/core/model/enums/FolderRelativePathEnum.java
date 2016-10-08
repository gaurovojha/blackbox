package com.blackbox.ids.core.model.enums;

import java.io.File;

import com.blackbox.ids.core.util.BlackboxPropertyUtil;
import com.blackbox.ids.core.util.BlackboxUtils;

/**
 * The Enum RelativeFolderEnum.
 */
public enum FolderRelativePathEnum {

	/** The correspondence staging. */
	CORRESPONDENCE("correspondence"),

	/** The crawler default download directory. */
	CRAWLER("crawler"),

	/** The reference default upload directory */
	REFERENCE("reference"),

	/** Root file package for IDS. */
	IDS("ids");

	private String moduleFolderName;

	/**
	 * Instantiates a new relative folder enum.
	 *
	 * @param moduleBasePath the relative directory location
	 */
	private FolderRelativePathEnum(String moduleFolderName) {
		this.moduleFolderName = moduleFolderName;
	}

	/**
	 * Gets the relative directory location.
	 *
	 * @return the relative directory location
	 */
	public String getModuleFolderName() {
		return this.moduleFolderName;
	}

	public String getAbsolutePath() {
		return BlackboxUtils.concat(BlackboxPropertyUtil.getProperty("root.folder.dir"), File.separator,
				this.moduleFolderName, File.separator);
	}

	public String getAbsolutePath(String identifier) {
		return BlackboxUtils.concat(BlackboxPropertyUtil.getProperty("root.folder.dir"),File.separator,
				this.moduleFolderName,File.separator,identifier,File.separator);
	}

}
