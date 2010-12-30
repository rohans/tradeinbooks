
package com.ECS.client.jax;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IsFit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}MissingVehicleAttributes" minOccurs="0"/>
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
    "isFit",
    "missingVehicleAttributes"
})
@XmlRootElement(name = "VehiclePartFit")
public class VehiclePartFit {

    @XmlElement(name = "IsFit", required = true)
    protected String isFit;
    @XmlElement(name = "MissingVehicleAttributes")
    protected MissingVehicleAttributes missingVehicleAttributes;

    /**
     * Gets the value of the isFit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsFit() {
        return isFit;
    }

    /**
     * Sets the value of the isFit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsFit(String value) {
        this.isFit = value;
    }

    /**
     * Gets the value of the missingVehicleAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link MissingVehicleAttributes }
     *     
     */
    public MissingVehicleAttributes getMissingVehicleAttributes() {
        return missingVehicleAttributes;
    }

    /**
     * Sets the value of the missingVehicleAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link MissingVehicleAttributes }
     *     
     */
    public void setMissingVehicleAttributes(MissingVehicleAttributes value) {
        this.missingVehicleAttributes = value;
    }

}
