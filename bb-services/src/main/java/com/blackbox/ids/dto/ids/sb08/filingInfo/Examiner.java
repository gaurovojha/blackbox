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
import com.blackbox.ids.dto.ids.sb08.common.Signature;

/**
 * <pre>
 * &lt;primary-examiner&gt;
     &lt;name name-type=""&gt;Mahfuzur Rahman&lt;/name&gt;
     &lt;electronic-signature date="2016-02-12" place-signed=""&gt;
        &lt;basic-signature&gt;
           &lt;text-string&gt;/Mahfuzur&lt;/text-string&gt;
        &lt;/basic-signature&gt;
     &lt;/electronic-signature&gt;
  &lt;/primary-examiner&gt;
 * </pre>
 *
 * @author ajay2258
 */
@XmlRootElement(name = "primary-examiner")
@XmlAccessorType(XmlAccessType.FIELD)
public class Examiner implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -3481301656333291764L;

	@XmlElement
	private Name name;

	@XmlElement
	private Signature signature;

	/*- ----------------------------- getter-setters -- */
	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Signature getSignature() {
		return signature;
	}

	public void setSignature(Signature signature) {
		this.signature = signature;
	}

}
