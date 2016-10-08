/**
 *
 */
package com.blackbox.ids.workflows.ids.common;

/**
 * @author ajay2258
 *
 */
public interface IDSBuildConst {

	/** Name of the IDS build process. */
	String PROCESS_KEY = "idsBuild";

	/**
	 * Value type: <b>long</b> <br/>
	 * Holds the associated application instance Id whose IDS is being prepared.
	 */
	String TARGET_APPLICATION = "targetApplication";

	/**
	 * Value type: <b>String</b> <br/>
	 * Holds the temp/build Id for IDS instance.
	 */
	String BUILD_ID = "idsBuildId";

	/**
	 * Value type: <b>long</b> <br/>
	 * Holds the database Id for IDS.
	 */
	String IDS_dbID = "idsDbId";

	/**
	 * Value type: <b>IDS.Status</b> <br/>
	 * Holds the current status for IDS.
	 */
	String IDS_STATUS = "idsStatus";

	/**
	 * Value type: <b>long</b> <br/>
	 * Holds the attorney user Id who has been requested to approve IDS.
	 */
	String ATTORNEY_USER = "attorneyUser";

	/**
	 * Value type: <b>long</b> <br/>
	 * Holds the paralegal user Id who has requested approval on IDS.
	 */
	String PARALEGAL_USER = "paralegalUser";

	/**
	 * Value type: <b>long</b> <br/>
	 * Holds the Id of user, who has last updated the IDS instance.
	 */
	String LAST_UPDATED_BY = "updatedBy";

	/**
	 * Value type: <b>Long</b> <br/>
	 * Holds approval request notification process Id.
	 */
	String APPROVAL_NOTIFICATION_ID = "approvalNotificationId";

	/**
	 * Value type: <b>Boolean</b> <br/>
	 * Indicates whether or not, the attorney user has requested changes on IDS approval request.
	 */
	String CHANGES_REQUESTED = "changesRequested";

}
