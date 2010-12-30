
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
 *         &lt;element name="EngineName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EngineId" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
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
    "engineName",
    "engineId"
})
@XmlRootElement(name = "VehicleEngine")
public class VehicleEngine {

    @XmlElement(name = "EngineName")
    protected String engineName;
    @XmlElement(name = "EngineId", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger engineId;

    /**
     * Gets the value of the engineName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngineName() {
        return engineName;
    }

    /**
     * Sets the value of the engineName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngineName(String value) {
        this.engineName = value;
    }

    /**
     * Gets the value of the engineId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEngineId() {
        return engineId;
    }

    /**
     * Sets the value of the engineId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEngineId(BigInteger value) {
        this.engineId = value;
    }

}
