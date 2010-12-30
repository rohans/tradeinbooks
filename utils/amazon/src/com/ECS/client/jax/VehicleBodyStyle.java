
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
 *         &lt;element name="BodyStyleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BodyStyleId" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
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
    "bodyStyleName",
    "bodyStyleId"
})
@XmlRootElement(name = "VehicleBodyStyle")
public class VehicleBodyStyle {

    @XmlElement(name = "BodyStyleName")
    protected String bodyStyleName;
    @XmlElement(name = "BodyStyleId", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger bodyStyleId;

    /**
     * Gets the value of the bodyStyleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBodyStyleName() {
        return bodyStyleName;
    }

    /**
     * Sets the value of the bodyStyleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBodyStyleName(String value) {
        this.bodyStyleName = value;
    }

    /**
     * Gets the value of the bodyStyleId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBodyStyleId() {
        return bodyStyleId;
    }

    /**
     * Sets the value of the bodyStyleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBodyStyleId(BigInteger value) {
        this.bodyStyleId = value;
    }

}
