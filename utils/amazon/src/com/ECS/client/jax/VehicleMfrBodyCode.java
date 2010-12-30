
package com.ECS.client.jax;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MfrBodyCodeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MfrBodyCodeId" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mfrBodyCodeName",
    "mfrBodyCodeId"
})
@XmlRootElement(name = "VehicleMfrBodyCode")
public class VehicleMfrBodyCode {

    @XmlElement(name = "MfrBodyCodeName", required = true)
    protected String mfrBodyCodeName;
    @XmlElement(name = "MfrBodyCodeId", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger mfrBodyCodeId;

    /**
     * Gets the value of the mfrBodyCodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMfrBodyCodeName() {
        return mfrBodyCodeName;
    }

    /**
     * Sets the value of the mfrBodyCodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMfrBodyCodeName(String value) {
        this.mfrBodyCodeName = value;
    }

    /**
     * Gets the value of the mfrBodyCodeId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMfrBodyCodeId() {
        return mfrBodyCodeId;
    }

    /**
     * Sets the value of the mfrBodyCodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMfrBodyCodeId(BigInteger value) {
        this.mfrBodyCodeId = value;
    }

}
