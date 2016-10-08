/**
 *
 */
package com.blackbox.ids.workflows.ids.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author ajay2258
 *
 */
public class IdsUploadListener implements TaskListener {

	/** The serial version UID. */
	private static final long serialVersionUID = 8554684895394486362L;

	@Override
	public void notify(DelegateTask delegateTask) {
		/*-
		 * - Set the 'uploadStatus' based on the upload IDS attempt status.
		 */
	}

}
