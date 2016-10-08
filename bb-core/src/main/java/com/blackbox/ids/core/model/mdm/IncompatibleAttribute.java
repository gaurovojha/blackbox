package com.blackbox.ids.core.model.mdm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import com.blackbox.ids.core.model.mdm.Application.SubSource;

public class IncompatibleAttribute {

	public enum Jurisdiction {
		US,
		WO,
		OTHER;

		public static Jurisdiction fromString(final String strJurisdiction) {
			Jurisdiction jurisdiction = null;
			for (final Jurisdiction e : Jurisdiction.values()) {
				if (e.name().equalsIgnoreCase(strJurisdiction)) {
					jurisdiction = e;
					break;
				}
			}
			return jurisdiction == null && !StringUtils.isEmpty(strJurisdiction) ? OTHER : jurisdiction;
		}
	}

	public enum Field {
		FAMILY,
		ENTITY,
		ISSUE_DATE,
		PATENT_NO,
		CUSTOMER_NO,
		CONFIRMATION_NO,
		DOCKET_NO,
		FILING_DATE,
		TITLE;

		public static Field fromString(final String strField) {
			Field field = null;
			for (final Field e : Field.values()) {
				if (e.name().contentEquals(strField)) {
					field = e;
					break;
				}
			}
			return field;
		}

	}

	public enum NonEditableAttribute {
		MANUAL(Application.SubSource.MANUAL),
		THIRD_PARTY(Application.SubSource.THIRD_PARTY),
		USPTO(Application.SubSource.USPTO,
				Arrays.asList(Field.DOCKET_NO, Field.FILING_DATE, Field.CONFIRMATION_NO, Field.TITLE));

		private final SubSource subSource;
		public final List<Field> nonEditableFields;

		private NonEditableAttribute(final SubSource subSource) {
			this.subSource = subSource;
			this.nonEditableFields = Collections.emptyList();
		}

		private NonEditableAttribute(final SubSource subSource, final List<Field> nonEditableFields) {
			this.subSource = subSource;
			this.nonEditableFields = nonEditableFields;
		}

		public static NonEditableAttribute fromString(final String strSource) {
			NonEditableAttribute source = null;
			for (final NonEditableAttribute e : NonEditableAttribute.values()) {
				if (e.subSource.name().equalsIgnoreCase(strSource)) {
					source = e;
					break;
				}
			}
			return source;
		}
	}

	/*- ------------------------------------ Attributes -- */
	public final Field field;
	public final Jurisdiction jurisdiction;
	public final ApplicationType applicationType;

	/*- ------------------------------------ Constructors -- */
	private IncompatibleAttribute(final Field field, final Jurisdiction jurisdiction) {
		super();
		this.field = field;
		this.jurisdiction = jurisdiction;
		this.applicationType = null;
	}

	private IncompatibleAttribute(final Field field, final ApplicationType applicationType) {
		super();
		this.field = field;
		this.jurisdiction = null;
		this.applicationType = applicationType;
	}

	private IncompatibleAttribute(final Field field, final Jurisdiction jurisdiction, final ApplicationType applicationType) {
		super();
		this.field = field;
		this.jurisdiction = jurisdiction;
		this.applicationType = applicationType;
	}

	/*- ------------------------------------ equals 'n' hashCode -- */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (applicationType == null ? 0 : applicationType.hashCode());
		result = prime * result + (field == null ? 0 : field.hashCode());
		result = prime * result + (jurisdiction == null ? 0 : jurisdiction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		IncompatibleAttribute other = (IncompatibleAttribute) obj;

		return this.field == other.field &&
				(this.jurisdiction == other.jurisdiction || this.applicationType == other.applicationType);
	}

	/*- ------------------------------------ Checking Violations -- */
	public static boolean violoates(final Field field, final Jurisdiction jurisdiction, final ApplicationType applicationType) {
		return CONDITIONAL_ATTRIBUTES.contains(new IncompatibleAttribute(field, jurisdiction, applicationType));
	}

	public static boolean violoates(final String field, final String jurisdiction, final String applicationType) {
		return violoates(Field.fromString(field), Jurisdiction.fromString(jurisdiction), ApplicationType.fromString(applicationType));
	}

	public static boolean isMandatory(final String field, final String jurisdiction, final String applicationType) {
		return !violoates(Field.fromString(field), Jurisdiction.fromString(jurisdiction),
				ApplicationType.fromString(applicationType));
	}

	public static boolean isMandatory(final Field field, final Jurisdiction jurisdiction,
			final ApplicationType applicationType) {
		return !violoates(field, jurisdiction, applicationType);
	}

	/*- ------------------------------------ Edit Application :: Read Only Attributes -- */
	/**
	 * The method confirms whether or not, the given attribute is open for modifications on <tt>Edit Application</tt>
	 * screen; provided the given application sub-source.
	 * <p/>
	 * Please note that this API should be called from within edit application only.
	 *
	 * @param strField
	 *            Field to see access for.
	 * @param strSubSource
	 *            Sub-source for application.
	 * @return <tt>true</tt> if given field value is allowed to be updated; <tt>false</tt> otherwise.
	 */
	public static boolean canUpdateAttribute(final String strField, final String strSubSource) {
		return canUpdateAttribute(Field.fromString(strField), strSubSource);
	}

	public static boolean canUpdateAttribute(final Field field, final String strSubSource) {
		NonEditableAttribute subSource = NonEditableAttribute.fromString(strSubSource);
		return subSource == null || field == null || !subSource.nonEditableFields.contains(field);
	}

	/*- ------------------------------------ Defined Constraints -- */
	private static final List<IncompatibleAttribute> CONDITIONAL_ATTRIBUTES = Arrays.asList(
			new IncompatibleAttribute(Field.FAMILY, ApplicationType.FIRST_FILING),
			new IncompatibleAttribute(Field.FAMILY, ApplicationType.PCT_US_FIRST_FILING),
			new IncompatibleAttribute(Field.ENTITY, Jurisdiction.WO),
			new IncompatibleAttribute(Field.ENTITY, Jurisdiction.OTHER),
			new IncompatibleAttribute(Field.ISSUE_DATE, Jurisdiction.WO),
			new IncompatibleAttribute(Field.PATENT_NO, Jurisdiction.WO),
			new IncompatibleAttribute(Field.CUSTOMER_NO, ApplicationType.FOREIGN_FAMILY_MEMBER),
			new IncompatibleAttribute(Field.CONFIRMATION_NO, ApplicationType.FOREIGN_FAMILY_MEMBER)
			);

}
