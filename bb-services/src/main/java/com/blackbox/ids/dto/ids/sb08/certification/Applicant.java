/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.certification;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.blackbox.ids.dto.ids.sb08.common.Name;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "applicant-name")
@XmlAccessorType(XmlAccessType.FIELD)
public class Applicant implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 6027184041830879450L;

	@XmlElement
	private Name name;

	@XmlElement(name = "registered-number")
	private String registrationNo;

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

}

