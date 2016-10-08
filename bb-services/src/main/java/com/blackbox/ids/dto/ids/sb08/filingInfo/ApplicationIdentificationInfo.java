/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.filingInfo;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.blackbox.ids.dto.ids.sb08.adapter.DateAdapter;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "us-application-identification-info")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationIdentificationInfo implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = -607936511672426191L;

	@XmlElement(name = "doc-number")
	private String applicationNo;

	@XmlElement(name = "date")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date publishedOn;

	/*- ------------------------------------- getter-setters -- */
	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Date getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}

}
