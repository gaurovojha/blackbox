/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.common;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * <pre>
 * &lt;name name-type=""&gt;Ajay Singh Raman&lt;/name&gt;
 * </pre>
 *
 * @author ajay2258
 */
@XmlRootElement(name = "name")
@XmlAccessorType(XmlAccessType.FIELD)
public class Name implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -6810307633573692550L;

	@XmlAttribute(name = "name-type")
	private String type;

	@XmlValue
	private String name;

	/*-
	 @XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement
	public class Sport {

	@XmlAttribute
	protected String type;

	@XmlAttribute
	protected String gender;

	@XmlValue;
	protected String description;

	}
	 */

	/*- --------------------------------- getter-setters -- */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
