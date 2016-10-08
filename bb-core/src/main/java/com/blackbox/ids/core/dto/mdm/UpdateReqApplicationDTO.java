package com.blackbox.ids.core.dto.mdm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mysema.query.annotations.QueryProjection;

/**
 * The {@link UpdateReqApplicationDTO} holds all the attributes in MDM update request application records list.
 * 
 * @author tusharagarwal
 *
 */

public class UpdateReqApplicationDTO extends ActionsBaseDto {
	
	private Long id;
	
	private String discrepencies;
	
	private String refernceDocument;
	
	
	@QueryProjection
	public UpdateReqApplicationDTO(final Long id, final String jurisdiction, final String applicationNumber, final String discrepencies,
								   final String referenceDocument, final Calendar notifiedOn) {
		super();
		this.id = id;
		this.discrepencies = discrepencies;
		this.refernceDocument = referenceDocument;
	}
	
	public UpdateReqApplicationDTO(){}
	
	/*---getters - setters--*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDiscrepencies() {
		return discrepencies;
	}

	public void setDiscrepencies(String discrepencies) {
		this.discrepencies = discrepencies;
	}

	public String getRefernceDocument() {
		return refernceDocument;
	}

	public void setRefernceDocument(String refernceDocument) {
		this.refernceDocument = refernceDocument;
	}
	
}
