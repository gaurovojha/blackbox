package com.blackbox.ids.core.dto.reference;

import java.io.Serializable;

public class SourceReferenceDTO implements Serializable {

	private static final long serialVersionUID = 790369089004364271L;

	private Long id;

	public SourceReferenceDTO(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
