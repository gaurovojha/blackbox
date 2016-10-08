/**
 *
 */
package com.blackbox.ids.workflows.ids.task;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author ajay2258
 *
 */
public abstract class IDSTask implements JavaDelegate {

	protected Expression idsBuildService;

	public void setIdsBuildService(Expression idsBuildService) {
		this.idsBuildService = idsBuildService;
	}

}
