
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
 *         &lt;element name="TrimName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TrimId" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="IsValid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}VehicleOptions" minOccurs="0"/>
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
    "trimName",
    "trimId",
    "isValid",
    "vehicleOptions"
})
@XmlRootElement(name = "VehicleTrim")
public class VehicleTrim {

    @XmlElement(name = "TrimName")
    protected String trimName;
    @XmlElement(name = "TrimId", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger trimId;
    @XmlElement(name = "IsValid")
    protected String isValid;
    @XmlElement(name = "VehicleOptions")
    protected VehicleOptions vehicleOptions;

    /**
     * Gets the value of the trimName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrimName() {
        return trimName;
    }

    /**
     * Sets the value of the trimName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrimName(String value) {
        this.trimName = value;
    }

    /**
     * Gets the value of the trimId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTrimId() {
        return trimId;
    }

    /**
     * Sets the value of the trimId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTrimId(BigInteger value) {
        this.trimId = value;
    }

    /**
     * Gets the value of the isValid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * Sets the value of the isValid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsValid(String value) {
        this.isValid = value;
    }

    /**
     * Gets the value of the vehicleOptions property.
     * 
     * @return
     *     possible object is
     *     {@link VehicleOptions }
     *     
     */
    public VehicleOptions getVehicleOptions() {
        return vehicleOptions;
    }

    /**
     * Sets the value of the vehicleOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleOptions }
     *     
     */
    public void setVehicleOptions(VehicleOptions value) {
        this.vehicleOptions = value;
    }

}
