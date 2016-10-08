/**
 *
 */
package com.blackbox.ids.dto.mdm;

import java.util.List;

/**
 * @author ajay2258
 *
 */
public class MdmValidationData {

	private List<String> customerNumbers;

	private List<String> assignees;

	private List<String> entityTypes;

	public MdmValidationData(final List<String> customerNumbers, final List<String> assignees,
			final List<String> entityTypes) {
		super();
		this.customerNumbers = customerNumbers;
		this.assignees = assignees;
		this.entityTypes = entityTypes;
	}

	/*- ------------------------------------------ getter-setters -- */
	public List<String> getCustomerNumbers() {
		return customerNumbers;
	}

	public void setCustomerNumbers(List<String> customerNumbers) {
		this.customerNumbers = customerNumbers;
	}

	public List<String> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<String> assignees) {
		this.assignees = assignees;
	}

	public List<String> getEntityTypes() {
		return entityTypes;
	}

	public void setEntityTypes(List<String> entityTypes) {
		this.entityTypes = entityTypes;
	}

}
