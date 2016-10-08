/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author ajay2258
 *
 */
public class InitiateIdsUpload implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		/*-
		 * Update IDS Upload Queue for this application IDS.
		 */
	}

}
