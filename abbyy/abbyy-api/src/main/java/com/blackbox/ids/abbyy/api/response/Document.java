package com.blackbox.ids.abbyy.api.response;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}tables"/&gt;
 *         &lt;element ref="{}fields"/&gt;
 *         &lt;element ref="{}documentConfidenceLevel"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tables",
    "fields",
    "documentConfidenceLevel",
    "classificationType"
})
@XmlRootElement(name = "document")
public class Document {

	/** The classification type. */
	@XmlElement(required = true)
	protected String classificationType;
    
    /** The tables. */
    @XmlElement(required = true)
    protected Tables tables;
    
    /** The fields. */
    @XmlElement(required = true)
    protected Fields fields;
    
    /** The document confidence level. */
    @XmlElement(required = true)
    protected BigInteger documentConfidenceLevel;

    /**
     * Gets the value of the tables property.
     * 
     * @return
     *     possible object is
     *     {@link Tables }
     *     
     */
    public Tables getTables() {
        return tables;
    }

    /**
     * Sets the value of the tables property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tables }
     *     
     */
    public void setTables(Tables value) {
        this.tables = value;
    }

    /**
     * Gets the value of the fields property.
     * 
     * @return
     *     possible object is
     *     {@link Fields }
     *     
     */
    public Fields getFields() {
        return fields;
    }

    /**
     * Sets the value of the fields property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fields }
     *     
     */
    public void setFields(Fields value) {
        this.fields = value;
    }

    /**
     * Gets the value of the documentConfidenceLevel property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDocumentConfidenceLevel() {
        return documentConfidenceLevel;
    }

    /**
     * Sets the value of the documentConfidenceLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDocumentConfidenceLevel(BigInteger value) {
        this.documentConfidenceLevel = value;
    }

	/**
	 * Gets the classification type.
	 *
	 * @return the classification type
	 */
	public String getClassificationType() {
		return classificationType;
	}

	/**
	 * Sets the classification type.
	 *
	 * @param classificationType the new classification type
	 */
	public void setClassificationType(String classificationType) {
		this.classificationType = classificationType;
	}
	
	@Override
	public String toString() {
		return "Document [classificationType=" + classificationType + ", tables=" + tables + ", fields=" + fields
				+ ", documentConfidenceLevel=" + documentConfidenceLevel + "]";
	}

}
