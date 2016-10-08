/**
 *
 */
package com.blackbox.ids.dto.ids.sb08.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.blackbox.ids.dto.ids.sb08.adapter.BooleanAdapter;
import com.blackbox.ids.dto.ids.sb08.adapter.DateAdapter;

/**
 * @author ajay2258
 *
 */
@XmlRootElement(name = "us-doc-reference")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocReference implements Serializable {

	/** The serial version UID. */
	private static final long serialVersionUID = 8508134820626174388L;

	// FIXME: Remove it.
	public DocReference() {
		this.id = "dd:id_2";
		this.num = 1;
		this.referenceNo = "22582258";
		this.kindCode = "B2";
		this.date = new Date();
		this.description = "Pages,Columns,Lines where Relevant Passages or Relevant Figures Appear If you wish to add additional U.S. Patent citation information please click the";
	}

	/*- ------------------------------- XML Attributes -- */
	@XmlAttribute(name = "id")
	private String id;

	@XmlAttribute(name = "num")
	private int num;

	@XmlAttribute(name = "sequence")
	private int sequence;

	@XmlAttribute(name = "translation-attached")
	@XmlJavaTypeAdapter(BooleanAdapter.class)
	private Boolean translationAttached;

	@Column(name = "medium")
	private String medium;

	@Column(name = "type")
	private String type;

	@Column(name = "url")
	private String url;

	/*- ------------------------------- XML Elements -- */
	@XmlElement(name = "doc-number")
	private String referenceNo;

	@XmlElement
	private Name name;

	@XmlElement(name = "kind")
	private String kindCode;

	@XmlElement(name = "date")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date date;

	@XmlElement(name = "class", required = false)
	private String clazz;

	@XmlElement(name = "subclass", required = false)
	private String subClazz;

	@XmlElement(name = "relevant-portion")
	private String description;

	/*- --------------------------------- getter-setters -- */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Boolean getTranslationAttached() {
		return translationAttached;
	}

	public void setTranslationAttached(Boolean translationAttached) {
		this.translationAttached = translationAttached;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getSubClazz() {
		return subClazz;
	}

	public void setSubClazz(String subClazz) {
		this.subClazz = subClazz;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
