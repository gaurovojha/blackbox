/**
 *
 */
package com.blackbox.ids.services.ids;

import com.blackbox.ids.workflows.ids.common.ControlFlags.AttorneyResponse;
import com.blackbox.ids.workflows.ids.common.ControlFlags.ParallegalAction;

/**
 * The interface type {@code IDSWorkflowService} exposes APIs to help manage the IDS build workflow.
 *
 * @author ajay2258
 */
public interface IDSWorkflowService {

	/**
	 * The method initiates the IDS build workflow.
	 *
	 * @param targetApplication
	 *            Database ID for application whose IDS is being initiated
	 * @return Id for IDS instance created.
	 */
	public long initiateIDSBuild(final Long application);

	/**
	 * The method does all the processing required to execute given action taken by the paralegal user on an IDS.
	 * Parameter 'attorneyUser' is required only for {@link ParallegalAction#SUBMIT_FOR_APPROVAL}.
	 *
	 * @param idsID
	 *            Database ID for the IDS, that is being processed
	 * @param parallegalAction
	 *            defines the action paralegal action
	 * @param attorneyUser
	 *            Id for attorney user, approval is requested to
	 */
	public void processParalegalAction(final long idsID, final ParallegalAction parallegalAction,
			final Long attorneyUser);

	/**
	 * The method does all the processing required to execute action taken by the attorney user on given IDS approval
	 * request.
	 *
	 * @param idsID
	 *            Database ID for the IDS, that is being processed
	 * @param attorneyResponse
	 *            Action taken by the attorney on (can't be {@code null})
	 */
	public void processAttorneyAction(final long idsID, final AttorneyResponse attorneyResponse);

}
