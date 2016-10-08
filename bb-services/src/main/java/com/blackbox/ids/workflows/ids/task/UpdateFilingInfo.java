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
public class UpdateFilingInfo implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		/*-
		 * - Update the internal filing info table on IDS upload status SUCCESS.
		 */
	}

}
