package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("LastFileHistoryTransaction")
public class LastFileHistoryTransaction {
	
	@XStreamAlias("LastTransactionDate")
	private String lastTransactionDate;
	
	@XStreamAlias("LastTransactionDescription")
	private String lastTransactionDescription;

	public String getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(String lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public String getLastTransactionDescription() {
		return lastTransactionDescription;
	}

	public void setLastTransactionDescription(String lastTransactionDescription) {
		this.lastTransactionDescription = lastTransactionDescription;
	}

}
