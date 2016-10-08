/**
 *
 */
package com.blackbox.ids.core.dto.mdm.family;

/**
 * The {@code FamilySearchFilter} contains the various attributes, a family can be search on.
 *
 * @author ajay2258
 */
public class FamilySearchFilter {

	/** Enumerates the list of linkage types for searching a family Id. */
	public enum FamilyLinkage {
		APPLICATION_NUMBER,
		ATTORNEY_DOCKET_NUMBER,
		FAMILY_ID;

		public static FamilyLinkage fromString(final String val) {
			FamilyLinkage linage = null;
			for (final FamilyLinkage e : FamilyLinkage.values()) {
				if (e.name().equalsIgnoreCase(val)) {
					linage = e;
					break;
				}
			}
			return linage;
		}
	}

	private FamilyLinkage familyLinker;

	protected String jurisdiction;

	protected String applicationNo;

	protected String docketNo;

	protected String familyId;

	protected boolean onlyFirstFiling;

	/*- ---------------------------- Constructor -- */
	public FamilySearchFilter() {
		super();
	}

	public FamilySearchFilter(final String familyId, boolean onlyFirstFiling) {
		super();
		this.familyId = familyId;
		this.onlyFirstFiling = onlyFirstFiling;
		this.familyLinker = FamilyLinkage.FAMILY_ID;
	}

	/*- ---------------------------- getter-setters -- */
	public FamilyLinkage getFamilyLinker() {
		return familyLinker;
	}

	public void setFamilyLinker(FamilyLinkage familyLinker) {
		this.familyLinker = familyLinker;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getDocketNo() {
		return docketNo;
	}

	public void setDocketNo(String docketNo) {
		this.docketNo = docketNo;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public boolean isOnlyFirstFiling() {
		return onlyFirstFiling;
	}

	public void setOnlyFirstFiling(boolean onlyFirstFiling) {
		this.onlyFirstFiling = onlyFirstFiling;
	}

}
