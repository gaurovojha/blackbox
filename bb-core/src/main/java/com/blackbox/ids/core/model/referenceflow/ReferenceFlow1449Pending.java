/**
 * 
 */
package com.blackbox.ids.core.model.referenceflow;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blackbox.ids.core.model.reference.ReferenceBaseData;

/**
 * Entity bean for storing reference flow that results in duplicate with status PENDING_1449
 * 
 * @author nagarro
 *
 */
@Entity
@Table(name = "BB_REFERENCE_FLOW_1449_PENDING")
public class ReferenceFlow1449Pending implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4971131408913217413L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BB_REFERENCE_FLOW_1449_PENDING_ID")
	private Long ID;

	@OneToOne
	@JoinColumn(name = "BB_IDS_REFERENCE_FLOW_ID", referencedColumnName = "REFERENCE_FLOW_ID", nullable = false)
	private IDSReferenceFlow idsReferenceFlow;

	@OneToOne
	@JoinColumn(name = "BB_REFERENCE_BASE_DATA_ID", referencedColumnName = "BB_REFERENCE_BASE_DATA_ID", nullable = false)
	private ReferenceBaseData referenceBaseData;

	/**
	 * @return the iD
	 */
	public Long getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(Long iD) {
		ID = iD;
	}

	/**
	 * @return the idsReferenceFlow
	 */
	public IDSReferenceFlow getIdsReferenceFlow() {
		return idsReferenceFlow;
	}

	/**
	 * @param idsReferenceFlow
	 *            the idsReferenceFlow to set
	 */
	public void setIdsReferenceFlow(IDSReferenceFlow idsReferenceFlow) {
		this.idsReferenceFlow = idsReferenceFlow;
	}

	/**
	 * @return the referenceBaseData
	 */
	public ReferenceBaseData getReferenceBaseData() {
		return referenceBaseData;
	}

	/**
	 * @param referenceBaseData
	 *            the referenceBaseData to set
	 */
	public void setReferenceBaseData(ReferenceBaseData referenceBaseData) {
		this.referenceBaseData = referenceBaseData;
	}

}
