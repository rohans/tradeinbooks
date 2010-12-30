
package com.ECS.client.jax;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}VehicleMfrBodyCode" maxOccurs="unbounded" minOccurs="0"/>
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
    "vehicleMfrBodyCode"
})
@XmlRootElement(name = "VehicleMfrBodyCodeOptions")
public class VehicleMfrBodyCodeOptions {

    @XmlElement(name = "VehicleMfrBodyCode")
    protected List<VehicleMfrBodyCode> vehicleMfrBodyCode;

    /**
     * Gets the value of the vehicleMfrBodyCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vehicleMfrBodyCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVehicleMfrBodyCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VehicleMfrBodyCode }
     * 
     * 
     */
    public List<VehicleMfrBodyCode> getVehicleMfrBodyCode() {
        if (vehicleMfrBodyCode == null) {
            vehicleMfrBodyCode = new ArrayList<VehicleMfrBodyCode>();
        }
        return this.vehicleMfrBodyCode;
    }

}
