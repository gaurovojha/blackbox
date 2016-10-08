/**
 *
 */
package com.blackbox.ids.ui.form.mdm;

import com.blackbox.ids.core.dto.mdm.family.FamilySearchFilter;

/**
 * Form {@code FamilySearchForm} outlines the filter attributes, user can choose to search for a family Id in application.
 *
 * @author ajay2258
 */
public class FamilySearchForm extends FamilySearchFilter {

	public static final String KEY_FORM = "familySearchForm";

	public static final String KEY_FAMILY_LINKAGES = "familyLinkageTypes";

	private String linkage;

	public FamilySearchFilter toFilter() {
		FamilySearchFilter filter = new FamilySearchFilter();
		filter.setJurisdiction(jurisdiction);
		filter.setApplicationNo(applicationNo);
		filter.setDocketNo(docketNo);
		filter.setFamilyId(familyId);
		filter.setFamilyLinker(FamilyLinkage.fromString(linkage));
		//setting flag to get Family Records with application having Application Type as First Filing.
		filter.setOnlyFirstFiling(true);
		return filter;
	}

	/*- ---------------------------- getter-setters -- */
	public String getLinkage() {
		return linkage;
	}

	public void setLinkage(String linkage) {
		this.linkage = linkage;
	}

}
