/**
 *
 */
package com.blackbox.ids.core.model.mdm;

import java.util.Arrays;
import java.util.List;

import com.blackbox.ids.core.model.mdm.IncompatibleAttribute.Jurisdiction;

/*-
"Jurisdiction - US, Child Application Type - US National – First US Filing"
"Jurisdiction - WO, Child Application Type - PCT US – First US Filing "
"Jurisdiction - US, Child Application Type – US National – Subsequent US filing / CIP / CA / DIV"
"Jurisdiction - WO, Child Application Type - PCT US - Subsequent Filing "
"Jurisdiction - WO, Child Application Type - Foreign Family Member"
"Jurisdiction - Other Than WO / US, Child Application Type - Foreign Family Member"
 */
/**
 * Enumerates all the possible scenarios, an application can be created manually with.
 * <p/>
 * This also helps manage application types allowed against a particular jurisdiction.
 *
 * @author ajay2258
 */
public enum ApplicationScenario {

	US_FIRST_FILING(Jurisdiction.US, Arrays.asList(ApplicationType.FIRST_FILING)),

	WO_FIRST_FILING(Jurisdiction.WO, Arrays.asList(ApplicationType.PCT_US_FIRST_FILING)),

	US_CHILD_FILING(Jurisdiction.US, Arrays.asList(ApplicationType.US_SUBSEQUENT_FILING,
			ApplicationType.CIP, ApplicationType.CA, ApplicationType.DIV)),

	WO_SUBSEQUENT_FILING(Jurisdiction.WO, Arrays.asList(ApplicationType.PCT_US_SUBSEQUENT_FILING)),

	WO_FOREIGN_FILING(Jurisdiction.WO, Arrays.asList(ApplicationType.FOREIGN_FAMILY_MEMBER)),

	OTHER_FOREIGN_FILING(Jurisdiction.OTHER, Arrays.asList(ApplicationType.FOREIGN_FAMILY_MEMBER));

	public final Jurisdiction jurisdiction;
	public final List<ApplicationType> appTypes;

	private ApplicationScenario(final Jurisdiction jurisdiction, final List<ApplicationType> appTypes) {
		this.jurisdiction = jurisdiction;
		this.appTypes = appTypes;
	}

	public static ApplicationScenario getScenario(final String strJurisdiction, final String strAppType) {
		ApplicationType appType = ApplicationType.fromString(strAppType);
		Jurisdiction jurisdiction = Jurisdiction.fromString(strJurisdiction);
		return getScenario(jurisdiction, appType);
	}
	
	public static ApplicationScenario getScenario(final Jurisdiction jurisdiction, final ApplicationType appType) {
		ApplicationScenario scenario = null;
		if (jurisdiction != null && appType != null) {
			for (final ApplicationScenario e : ApplicationScenario.values()) {
				if (e.jurisdiction.equals(jurisdiction) && e.appTypes.contains(appType)) {
					scenario = e;
					break;
				}
			}
		}
		return scenario;
	}

	public static ApplicationScenario fromString(String strScenario) {
		ApplicationScenario scenario = null;

		for (final ApplicationScenario e : ApplicationScenario.values()) {
			if (e.name().equalsIgnoreCase(strScenario)) {
				scenario = e;
				break;
			}
		}

		return scenario;
	}

}
