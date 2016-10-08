package com.blackbox.ids.core.dto.correspondence.xmlmapping;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author rajatjain01
 *
 */
@XStreamAlias("DocumentList")
public class DocumentList {

	@XStreamAlias("DocumentData")
	@XStreamImplicit
	private List<DocumentData> documentData;

	public List<DocumentData> getDocumentData() {
		return documentData;
	}

	public void setDocumentData(final List<DocumentData> documentData) {
		this.documentData = documentData;
	}



}