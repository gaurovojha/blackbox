/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.common;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.blackbox.ids.dto.ids.sb08.adapter.DateAdapter;

/**
 * <pre>
 * &lt;electronic-signature date="2016-02-12" place-signed=""&gt;
        &lt;basic-signature&gt;
           &lt;text-string&gt;/Mahfuzur&lt;/text-string&gt;
        &lt;/basic-signature&gt;
   &lt;/electronic-signature&gt;
 * </pre>
 *
 * @author ajay2258
 */
@XmlRootElement(name = "electronic-signature")
@XmlAccessorType(XmlAccessType.FIELD)
public class Signature implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 2456044530022698945L;

	@XmlRootElement(name = "basic-signature")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class BasicSignature {

		@XmlElement(name = "text-string")
		private String signature;

		public String getSignature() {
			return signature;
		}

		public void setSignature(String signature) {
			this.signature = signature;
		}

	}

	@XmlAttribute(name = "date")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date date;

	@XmlElement
	private BasicSignature basicSignature;

	/*- ---------------------------------- getter-setters -- */
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BasicSignature getBasicSignature() {
		return basicSignature;
	}

	public void setBasicSignature(BasicSignature basicSignature) {
		this.basicSignature = basicSignature;
	}

}
