/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.reference;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.blackbox.ids.dto.ids.sb08.common.DocReference;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "us-foreign-document-cite")
@XmlAccessorType(XmlAccessType.FIELD)
public class Foreign implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -4423316576466272963L;

	@XmlElement(name = "us-doc-reference")
	private List<DocReference> references;

	/*- --------------------------------------- getter-setters -- */
	public List<DocReference> getReferences() {
		return references;
	}

	public void setReferences(List<DocReference> references) {
		this.references = references;
	}

}
