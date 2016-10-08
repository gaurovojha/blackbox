/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enumerates the list of applications types.
 *
 * @author ajay2258
 */
public enum ApplicationType {

	ALL,
	FIRST_FILING,
	PCT_US_FIRST_FILING,
	PCT_US_SUBSEQUENT_FILING,
	US_SUBSEQUENT_FILING,
	FOREIGN_FAMILY_MEMBER,
	CIP,
	CA,
	DIV;

	public static List<ApplicationType> childAppTypes() {
		List<ApplicationType> types = new ArrayList<>();
		for (ApplicationType e : ApplicationType.values()) {
			if (!e.equals(ALL)) {
				types.add(e);
			}
		}
		return types;
	}

	public static List<ApplicationType> usAppTypes() {
		return Arrays.asList(FIRST_FILING, US_SUBSEQUENT_FILING, CIP, CA, DIV);
	}

	public static List<ApplicationType> woAppTypes() {
		return Arrays.asList(PCT_US_FIRST_FILING, PCT_US_SUBSEQUENT_FILING, FOREIGN_FAMILY_MEMBER);
	}

	public static List<ApplicationType> otherAppTypes() {
		return Arrays.asList(FOREIGN_FAMILY_MEMBER);
	}

	public static List<ApplicationType> firstFilings() {
		return Arrays.asList(FIRST_FILING, PCT_US_FIRST_FILING);
	}

	public static boolean isFirstFiling(final String applicationType) {
		return applicationType == null ? false : firstFilings().contains(fromString(applicationType));
	}

	public static boolean isFirstFiling(final ApplicationType applicationType) {
		return applicationType == null ? false : firstFilings().contains(applicationType);
	}

	public static ApplicationType fromString(String appType) {
		ApplicationType applicationType =  null;
		for (final ApplicationType e : ApplicationType.values()) {
			if (e.name().equalsIgnoreCase(appType)) {
				applicationType = e;
				break;
			}
		}
		return applicationType;
	}

	public static List<ApplicationType> updateFamilyLinkageAppTypes() {
		return Arrays.asList(US_SUBSEQUENT_FILING, CIP, CA, DIV);
	}

}
