/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.reference;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "us-nplcit")
@XmlAccessorType(XmlAccessType.FIELD)
public class NPL implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 4462312947646113701L;

	@XmlElement(name = "text")
	private String text;

	/*- ------------------------------ getter-setters -- */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
