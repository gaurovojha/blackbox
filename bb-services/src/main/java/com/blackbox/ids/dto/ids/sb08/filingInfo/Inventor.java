/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.filingInfo;

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
@XmlRootElement(name = "us-first-named-inventor")
@XmlAccessorType(XmlAccessType.FIELD)
public class Inventor implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 8219838514343275567L;

	@XmlElement
	private Name name;

	/*- ----------------------------- getter-setters -- */
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

}
