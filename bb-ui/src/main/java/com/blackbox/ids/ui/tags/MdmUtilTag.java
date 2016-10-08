package com.blackbox.ids.ui.tags;

import javax.servlet.jsp.tagext.TagSupport;

import com.blackbox.ids.core.model.mdm.IncompatibleAttribute;

public class MdmUtilTag extends TagSupport {

	/** The serial version UID. */
	private static final long serialVersionUID = -3684093700061528761L;

	public static boolean isValidCombination(final String field, final String jurisdiction, final String applicationType) {
		return !IncompatibleAttribute.violoates(field, jurisdiction, applicationType);
	}

	/**
	 * The method confirms whether or not, the given attribute is open for modifications on <tt>Edit Application</tt>
	 * screen; provided the given application sub-source.
	 * <p/>
	 * Please note that this API should be called from within edit application only.
	 *
	 * @param field
	 *            Field to see access for.
	 * @param subSource
	 *            Sub-source for application.
	 * @return <tt>true</tt> if given field value is allowed to be updated; <tt>false</tt> otherwise.
	 */
	public static boolean canUpdateAttribute(final String field, final String subSource) {
		return IncompatibleAttribute.canUpdateAttribute(field, subSource);
	}

}
