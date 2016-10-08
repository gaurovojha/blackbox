/**
 *
 */
package com.blackbox.ids.core.model.enums;

/**
 * @author ajay2258
 *
 */
public enum FilePackage {

	CERTIFICATION_STATEMENT(FolderRelativePathEnum.IDS, "certificates"),

	IDS_SB08_DATA(FolderRelativePathEnum.IDS, "sb08"),

	// Named as ids/package/${idsID}/IDS.zip
	IDS_PACKAGE(FolderRelativePathEnum.IDS, "package"),

	// Named as ids/package/${idsID}/IDS.pdf
	IDS_FORM(FolderRelativePathEnum.IDS, "package");

	public final FolderRelativePathEnum rootFolder;
	public final String packageName;

	private FilePackage(final FolderRelativePathEnum rootFolder, final String packageName) {
		this.rootFolder = rootFolder;
		this.packageName = packageName;
	}

}
