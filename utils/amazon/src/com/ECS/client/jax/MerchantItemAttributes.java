
package com.ECS.client.jax;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


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
 *         &lt;element name="Actor" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}Address" minOccurs="0"/>
 *         &lt;element name="AmazonMaximumAge" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="AmazonMinimumAge" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="ApertureModes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Artist" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AspectRatio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AssemblyInstructions" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AssemblyRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AudienceRating" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AudioFormat" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Author" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BackFinding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BandMaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BatteriesIncluded" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BatteriesRequired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Batteries" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="BatteryDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BatteryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BezelMaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Binding" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Brand" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CalendarType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CameraManualFeatures" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CaseDiameter" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="CaseMaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CaseThickness" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="CaseType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CatalogNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CDRWDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ChainType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClaspType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClothingSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Compatibility" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ComputerHardwareType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ComputerPlatform" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Connectivity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ContinuousShootingSpeed" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="Country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountryOfOrigin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CPUManufacturer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CPUSpeed" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="CPUType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Creator" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="Role" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Cuisine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Customizable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DelayBetweenShots" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="DeliveryOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Department" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeweyDecimalNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DialColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DialWindowMaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DigitalZoom" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="Director" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DisplaySize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="DrumSetPieceQuantity" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="DVDLayers" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="DVDRWDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DVDSides" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="DPCI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Edition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ESRBAgeRating" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExternalDisplaySupportDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FabricType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaxNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Feature" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FirstIssueLeadTime" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}StringWithUnits" minOccurs="0"/>
 *         &lt;element name="FloppyDiskDriveDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Format" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FixedShippingCharge" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}Price" minOccurs="0"/>
 *         &lt;element name="GemType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GraphicsCardInterface" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GraphicsDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GraphicsMemorySize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="GuitarAttribute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GuitarBridgeSystem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GuitarPickThickness" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GuitarPickupConfiguration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HardDiskCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="HardDiskSize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="HasAutoFocus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasBurstMode" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasInCameraEditing" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasRedEyeReduction" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasSelfTimer" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasTripodMount" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasVideoOut" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HasViewfinder" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HazardousMaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HoursOfOperation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncludedSoftware" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncludesMp3Player" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Indications" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Ingredients" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IngredientsSetElement" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InstrumentKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsAutographed" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ISBN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsEmailNotifyAvailable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsFragile" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsLabCreated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsMemorabilia" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ISOEquivalent" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="IssuesPerYear" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ItemDimensions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Height" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                   &lt;element name="Length" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                   &lt;element name="Weight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                   &lt;element name="Width" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="KeyboardDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Label" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Languages" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Language" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="AudioFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LegalDisclaimer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LineVoltage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ListPrice" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}Price" minOccurs="0"/>
 *         &lt;element name="MacroFocusRange" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MagazineType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MalletHardness" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Manufacturer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ManufacturerLaborWarrantyDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ManufacturerMaximumAge" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="ManufacturerMinimumAge" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="ManufacturerPartsWarrantyDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MaterialType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MaximumAperture" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumColorDepth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MaximumFocalLength" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumHighResolutionImages" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumHorizontalResolution" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumLowResolutionImages" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MaximumResolution" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumShutterSpeed" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumVerticalResolution" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="MaximumWeightRecommendation" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MemorySlotsAvailable" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="MetalStamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MetalType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MiniMovieDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MinimumAdvertisedPrice" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}Price" minOccurs="0"/>
 *         &lt;element name="MinimumFocalLength" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MinimumShutterSpeed" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="Model" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ModelYear" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="ModemDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MonitorSize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MonitorViewableDiagonalSize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="MouseDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MPN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MusicalStyle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NativeResolution" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Neighborhood" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NetworkInterfaceDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NotebookDisplayTechnology" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NotebookPointingDeviceDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NumberOfDiscs" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfIssues" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfItems" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfKeys" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfPages" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfPearls" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfRapidFireShots" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfStones" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfStrings" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="NumberOfTracks" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="OpticalZoom" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="OriginalReleaseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OutputWattage" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="PackageDimensions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Height" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                   &lt;element name="Length" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                   &lt;element name="Weight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                   &lt;element name="Width" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PearlLustre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PearlMinimumColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PearlShape" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PearlStringingMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PearlSurfaceBlemishes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PearlType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PearlUniformity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PhotoFlashType" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PictureFormat" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Platform" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PriceRating" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="ProcessorCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="ProductGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PromotionalTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="POBoxShippingExcluded" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PublicationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Publisher" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PurchasingChannel" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ReadingLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RecorderTrackCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="RegionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RegionOfOrigin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReleaseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReturnMethod" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RemovableMemory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResolutionModes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReturnPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RingSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SafetyWarning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SalesRestriction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SecondaryCacheSize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="SettingType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Size" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SKU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SoldInStores" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SizePerPearl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SkillLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SoundCardDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SpeakerCount" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="SpeakerDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SpecialFeatures" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="StoneClarity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StoneColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StoneCut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StoneShape" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StoneWeight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="Studio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SubscriptionLength" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}NonNegativeIntegerWithUnits" minOccurs="0"/>
 *         &lt;element name="SupportedImageType" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SystemBusSpeed" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="SystemMemorySizeMax" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="SystemMemorySize" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="SystemMemoryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TellingPageIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TheatricalReleaseDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TotalDiamondWeight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="TotalExternalBaysFree" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalFirewirePorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalGemWeight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="TotalInternalBaysFree" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalMetalWeight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="TotalNTSCPALPorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalParallelPorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalPCCardSlots" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalPCISlotsFree" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalSerialPorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalSVideoOutPorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalUSB2Ports" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalUSBPorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="TotalVGAOutPorts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *         &lt;element name="UPC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VariationDenomination" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VariationDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VendorRebate" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Warranty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WatchMovementType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WebsiteBuyability" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WaterResistanceDepth" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
 *         &lt;element name="WirelessMicrophoneFrequency" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
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
    "actor",
    "address",
    "amazonMaximumAge",
    "amazonMinimumAge",
    "apertureModes",
    "artist",
    "aspectRatio",
    "assemblyInstructions",
    "assemblyRequired",
    "audienceRating",
    "audioFormat",
    "author",
    "backFinding",
    "bandMaterialType",
    "batteriesIncluded",
    "batteriesRequired",
    "batteries",
    "batteryDescription",
    "batteryType",
    "bezelMaterialType",
    "binding",
    "brand",
    "calendarType",
    "cameraManualFeatures",
    "caseDiameter",
    "caseMaterialType",
    "caseThickness",
    "caseType",
    "catalogNumber",
    "cdrwDescription",
    "chainType",
    "claspType",
    "clothingSize",
    "color",
    "compatibility",
    "computerHardwareType",
    "computerPlatform",
    "connectivity",
    "continuousShootingSpeed",
    "country",
    "countryOfOrigin",
    "cpuManufacturer",
    "cpuSpeed",
    "cpuType",
    "creator",
    "cuisine",
    "customizable",
    "delayBetweenShots",
    "deliveryOption",
    "department",
    "description",
    "deweyDecimalNumber",
    "dialColor",
    "dialWindowMaterialType",
    "digitalZoom",
    "director",
    "displaySize",
    "drumSetPieceQuantity",
    "dvdLayers",
    "dvdrwDescription",
    "dvdSides",
    "dpci",
    "ean",
    "edition",
    "esrbAgeRating",
    "externalDisplaySupportDescription",
    "fabricType",
    "faxNumber",
    "feature",
    "firstIssueLeadTime",
    "floppyDiskDriveDescription",
    "format",
    "fixedShippingCharge",
    "gemType",
    "graphicsCardInterface",
    "graphicsDescription",
    "graphicsMemorySize",
    "guitarAttribute",
    "guitarBridgeSystem",
    "guitarPickThickness",
    "guitarPickupConfiguration",
    "hardDiskCount",
    "hardDiskSize",
    "hasAutoFocus",
    "hasBurstMode",
    "hasInCameraEditing",
    "hasRedEyeReduction",
    "hasSelfTimer",
    "hasTripodMount",
    "hasVideoOut",
    "hasViewfinder",
    "hazardousMaterialType",
    "hoursOfOperation",
    "includedSoftware",
    "includesMp3Player",
    "indications",
    "ingredients",
    "ingredientsSetElement",
    "instrumentKey",
    "isAutographed",
    "isbn",
    "isEmailNotifyAvailable",
    "isFragile",
    "isLabCreated",
    "isMemorabilia",
    "isoEquivalent",
    "issuesPerYear",
    "itemDimensions",
    "keyboardDescription",
    "label",
    "languages",
    "legalDisclaimer",
    "lineVoltage",
    "listPrice",
    "macroFocusRange",
    "magazineType",
    "malletHardness",
    "manufacturer",
    "manufacturerLaborWarrantyDescription",
    "manufacturerMaximumAge",
    "manufacturerMinimumAge",
    "manufacturerPartsWarrantyDescription",
    "materialType",
    "maximumAperture",
    "maximumColorDepth",
    "maximumFocalLength",
    "maximumHighResolutionImages",
    "maximumHorizontalResolution",
    "maximumLowResolutionImages",
    "maximumResolution",
    "maximumShutterSpeed",
    "maximumVerticalResolution",
    "maximumWeightRecommendation",
    "memorySlotsAvailable",
    "metalStamp",
    "metalType",
    "miniMovieDescription",
    "minimumAdvertisedPrice",
    "minimumFocalLength",
    "minimumShutterSpeed",
    "model",
    "modelYear",
    "modemDescription",
    "monitorSize",
    "monitorViewableDiagonalSize",
    "mouseDescription",
    "mpn",
    "musicalStyle",
    "nativeResolution",
    "neighborhood",
    "networkInterfaceDescription",
    "notebookDisplayTechnology",
    "notebookPointingDeviceDescription",
    "numberOfDiscs",
    "numberOfIssues",
    "numberOfItems",
    "numberOfKeys",
    "numberOfPages",
    "numberOfPearls",
    "numberOfRapidFireShots",
    "numberOfStones",
    "numberOfStrings",
    "numberOfTracks",
    "opticalZoom",
    "originalReleaseDate",
    "outputWattage",
    "packageDimensions",
    "pearlLustre",
    "pearlMinimumColor",
    "pearlShape",
    "pearlStringingMethod",
    "pearlSurfaceBlemishes",
    "pearlType",
    "pearlUniformity",
    "phoneNumber",
    "photoFlashType",
    "pictureFormat",
    "platform",
    "priceRating",
    "processorCount",
    "productGroup",
    "promotionalTag",
    "poBoxShippingExcluded",
    "publicationDate",
    "publisher",
    "purchasingChannel",
    "readingLevel",
    "recorderTrackCount",
    "regionCode",
    "regionOfOrigin",
    "releaseDate",
    "returnMethod",
    "removableMemory",
    "resolutionModes",
    "returnPolicy",
    "ringSize",
    "safetyWarning",
    "salesRestriction",
    "secondaryCacheSize",
    "settingType",
    "size",
    "sku",
    "soldInStores",
    "sizePerPearl",
    "skillLevel",
    "soundCardDescription",
    "speakerCount",
    "speakerDescription",
    "specialFeatures",
    "stoneClarity",
    "stoneColor",
    "stoneCut",
    "stoneShape",
    "stoneWeight",
    "studio",
    "subscriptionLength",
    "supportedImageType",
    "systemBusSpeed",
    "systemMemorySizeMax",
    "systemMemorySize",
    "systemMemoryType",
    "tellingPageIndicator",
    "theatricalReleaseDate",
    "title",
    "totalDiamondWeight",
    "totalExternalBaysFree",
    "totalFirewirePorts",
    "totalGemWeight",
    "totalInternalBaysFree",
    "totalMetalWeight",
    "totalNTSCPALPorts",
    "totalParallelPorts",
    "totalPCCardSlots",
    "totalPCISlotsFree",
    "totalSerialPorts",
    "totalSVideoOutPorts",
    "totalUSB2Ports",
    "totalUSBPorts",
    "totalVGAOutPorts",
    "upc",
    "variationDenomination",
    "variationDescription",
    "vendorRebate",
    "warranty",
    "watchMovementType",
    "websiteBuyability",
    "waterResistanceDepth",
    "wirelessMicrophoneFrequency"
})
@XmlRootElement(name = "MerchantItemAttributes")
public class MerchantItemAttributes {

    @XmlElement(name = "Actor")
    protected List<String> actor;
    @XmlElement(name = "Address")
    protected Address address;
    @XmlElement(name = "AmazonMaximumAge")
    protected DecimalWithUnits amazonMaximumAge;
    @XmlElement(name = "AmazonMinimumAge")
    protected DecimalWithUnits amazonMinimumAge;
    @XmlElement(name = "ApertureModes")
    protected String apertureModes;
    @XmlElement(name = "Artist")
    protected List<String> artist;
    @XmlElement(name = "AspectRatio")
    protected String aspectRatio;
    @XmlElement(name = "AssemblyInstructions")
    protected String assemblyInstructions;
    @XmlElement(name = "AssemblyRequired")
    protected String assemblyRequired;
    @XmlElement(name = "AudienceRating")
    protected String audienceRating;
    @XmlElement(name = "AudioFormat")
    protected List<String> audioFormat;
    @XmlElement(name = "Author")
    protected List<String> author;
    @XmlElement(name = "BackFinding")
    protected String backFinding;
    @XmlElement(name = "BandMaterialType")
    protected String bandMaterialType;
    @XmlElement(name = "BatteriesIncluded")
    protected String batteriesIncluded;
    @XmlElement(name = "BatteriesRequired")
    protected String batteriesRequired;
    @XmlElement(name = "Batteries")
    protected NonNegativeIntegerWithUnits batteries;
    @XmlElement(name = "BatteryDescription")
    protected String batteryDescription;
    @XmlElement(name = "BatteryType")
    protected String batteryType;
    @XmlElement(name = "BezelMaterialType")
    protected String bezelMaterialType;
    @XmlElement(name = "Binding")
    protected String binding;
    @XmlElement(name = "Brand")
    protected String brand;
    @XmlElement(name = "CalendarType")
    protected String calendarType;
    @XmlElement(name = "CameraManualFeatures")
    protected List<String> cameraManualFeatures;
    @XmlElement(name = "CaseDiameter")
    protected DecimalWithUnits caseDiameter;
    @XmlElement(name = "CaseMaterialType")
    protected String caseMaterialType;
    @XmlElement(name = "CaseThickness")
    protected DecimalWithUnits caseThickness;
    @XmlElement(name = "CaseType")
    protected String caseType;
    @XmlElement(name = "CatalogNumber")
    protected String catalogNumber;
    @XmlElement(name = "CDRWDescription")
    protected String cdrwDescription;
    @XmlElement(name = "ChainType")
    protected String chainType;
    @XmlElement(name = "ClaspType")
    protected String claspType;
    @XmlElement(name = "ClothingSize")
    protected String clothingSize;
    @XmlElement(name = "Color")
    protected String color;
    @XmlElement(name = "Compatibility")
    protected String compatibility;
    @XmlElement(name = "ComputerHardwareType")
    protected String computerHardwareType;
    @XmlElement(name = "ComputerPlatform")
    protected String computerPlatform;
    @XmlElement(name = "Connectivity")
    protected String connectivity;
    @XmlElement(name = "ContinuousShootingSpeed")
    protected DecimalWithUnits continuousShootingSpeed;
    @XmlElement(name = "Country")
    protected String country;
    @XmlElement(name = "CountryOfOrigin")
    protected String countryOfOrigin;
    @XmlElement(name = "CPUManufacturer")
    protected String cpuManufacturer;
    @XmlElement(name = "CPUSpeed")
    protected DecimalWithUnits cpuSpeed;
    @XmlElement(name = "CPUType")
    protected String cpuType;
    @XmlElement(name = "Creator")
    protected List<MerchantItemAttributes.Creator> creator;
    @XmlElement(name = "Cuisine")
    protected String cuisine;
    @XmlElement(name = "Customizable")
    protected String customizable;
    @XmlElement(name = "DelayBetweenShots")
    protected DecimalWithUnits delayBetweenShots;
    @XmlElement(name = "DeliveryOption")
    protected String deliveryOption;
    @XmlElement(name = "Department")
    protected String department;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "DeweyDecimalNumber")
    protected String deweyDecimalNumber;
    @XmlElement(name = "DialColor")
    protected String dialColor;
    @XmlElement(name = "DialWindowMaterialType")
    protected String dialWindowMaterialType;
    @XmlElement(name = "DigitalZoom")
    protected DecimalWithUnits digitalZoom;
    @XmlElement(name = "Director")
    protected List<String> director;
    @XmlElement(name = "DisplaySize")
    protected DecimalWithUnits displaySize;
    @XmlElement(name = "DrumSetPieceQuantity")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger drumSetPieceQuantity;
    @XmlElement(name = "DVDLayers")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger dvdLayers;
    @XmlElement(name = "DVDRWDescription")
    protected String dvdrwDescription;
    @XmlElement(name = "DVDSides")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger dvdSides;
    @XmlElement(name = "DPCI")
    protected String dpci;
    @XmlElement(name = "EAN")
    protected String ean;
    @XmlElement(name = "Edition")
    protected String edition;
    @XmlElement(name = "ESRBAgeRating")
    protected String esrbAgeRating;
    @XmlElement(name = "ExternalDisplaySupportDescription")
    protected String externalDisplaySupportDescription;
    @XmlElement(name = "FabricType")
    protected String fabricType;
    @XmlElement(name = "FaxNumber")
    protected String faxNumber;
    @XmlElement(name = "Feature")
    protected List<String> feature;
    @XmlElement(name = "FirstIssueLeadTime")
    protected StringWithUnits firstIssueLeadTime;
    @XmlElement(name = "FloppyDiskDriveDescription")
    protected String floppyDiskDriveDescription;
    @XmlElement(name = "Format")
    protected List<String> format;
    @XmlElement(name = "FixedShippingCharge")
    protected Price fixedShippingCharge;
    @XmlElement(name = "GemType")
    protected String gemType;
    @XmlElement(name = "GraphicsCardInterface")
    protected String graphicsCardInterface;
    @XmlElement(name = "GraphicsDescription")
    protected String graphicsDescription;
    @XmlElement(name = "GraphicsMemorySize")
    protected DecimalWithUnits graphicsMemorySize;
    @XmlElement(name = "GuitarAttribute")
    protected String guitarAttribute;
    @XmlElement(name = "GuitarBridgeSystem")
    protected String guitarBridgeSystem;
    @XmlElement(name = "GuitarPickThickness")
    protected String guitarPickThickness;
    @XmlElement(name = "GuitarPickupConfiguration")
    protected String guitarPickupConfiguration;
    @XmlElement(name = "HardDiskCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger hardDiskCount;
    @XmlElement(name = "HardDiskSize")
    protected NonNegativeIntegerWithUnits hardDiskSize;
    @XmlElement(name = "HasAutoFocus")
    protected Boolean hasAutoFocus;
    @XmlElement(name = "HasBurstMode")
    protected Boolean hasBurstMode;
    @XmlElement(name = "HasInCameraEditing")
    protected Boolean hasInCameraEditing;
    @XmlElement(name = "HasRedEyeReduction")
    protected Boolean hasRedEyeReduction;
    @XmlElement(name = "HasSelfTimer")
    protected Boolean hasSelfTimer;
    @XmlElement(name = "HasTripodMount")
    protected Boolean hasTripodMount;
    @XmlElement(name = "HasVideoOut")
    protected Boolean hasVideoOut;
    @XmlElement(name = "HasViewfinder")
    protected Boolean hasViewfinder;
    @XmlElement(name = "HazardousMaterialType")
    protected String hazardousMaterialType;
    @XmlElement(name = "HoursOfOperation")
    protected String hoursOfOperation;
    @XmlElement(name = "IncludedSoftware")
    protected String includedSoftware;
    @XmlElement(name = "IncludesMp3Player")
    protected Boolean includesMp3Player;
    @XmlElement(name = "Indications")
    protected String indications;
    @XmlElement(name = "Ingredients")
    protected String ingredients;
    @XmlElement(name = "IngredientsSetElement")
    protected List<String> ingredientsSetElement;
    @XmlElement(name = "InstrumentKey")
    protected String instrumentKey;
    @XmlElement(name = "IsAutographed")
    protected Boolean isAutographed;
    @XmlElement(name = "ISBN")
    protected String isbn;
    @XmlElement(name = "IsEmailNotifyAvailable")
    protected Boolean isEmailNotifyAvailable;
    @XmlElement(name = "IsFragile")
    protected Boolean isFragile;
    @XmlElement(name = "IsLabCreated")
    protected Boolean isLabCreated;
    @XmlElement(name = "IsMemorabilia")
    protected Boolean isMemorabilia;
    @XmlElement(name = "ISOEquivalent")
    protected NonNegativeIntegerWithUnits isoEquivalent;
    @XmlElement(name = "IssuesPerYear")
    protected String issuesPerYear;
    @XmlElement(name = "ItemDimensions")
    protected MerchantItemAttributes.ItemDimensions itemDimensions;
    @XmlElement(name = "KeyboardDescription")
    protected String keyboardDescription;
    @XmlElement(name = "Label")
    protected String label;
    @XmlElement(name = "Languages")
    protected MerchantItemAttributes.Languages languages;
    @XmlElement(name = "LegalDisclaimer")
    protected String legalDisclaimer;
    @XmlElement(name = "LineVoltage")
    protected String lineVoltage;
    @XmlElement(name = "ListPrice")
    protected Price listPrice;
    @XmlElement(name = "MacroFocusRange")
    protected String macroFocusRange;
    @XmlElement(name = "MagazineType")
    protected String magazineType;
    @XmlElement(name = "MalletHardness")
    protected String malletHardness;
    @XmlElement(name = "Manufacturer")
    protected String manufacturer;
    @XmlElement(name = "ManufacturerLaborWarrantyDescription")
    protected String manufacturerLaborWarrantyDescription;
    @XmlElement(name = "ManufacturerMaximumAge")
    protected DecimalWithUnits manufacturerMaximumAge;
    @XmlElement(name = "ManufacturerMinimumAge")
    protected DecimalWithUnits manufacturerMinimumAge;
    @XmlElement(name = "ManufacturerPartsWarrantyDescription")
    protected String manufacturerPartsWarrantyDescription;
    @XmlElement(name = "MaterialType")
    protected String materialType;
    @XmlElement(name = "MaximumAperture")
    protected DecimalWithUnits maximumAperture;
    @XmlElement(name = "MaximumColorDepth")
    protected String maximumColorDepth;
    @XmlElement(name = "MaximumFocalLength")
    protected DecimalWithUnits maximumFocalLength;
    @XmlElement(name = "MaximumHighResolutionImages")
    protected NonNegativeIntegerWithUnits maximumHighResolutionImages;
    @XmlElement(name = "MaximumHorizontalResolution")
    protected NonNegativeIntegerWithUnits maximumHorizontalResolution;
    @XmlElement(name = "MaximumLowResolutionImages")
    protected String maximumLowResolutionImages;
    @XmlElement(name = "MaximumResolution")
    protected DecimalWithUnits maximumResolution;
    @XmlElement(name = "MaximumShutterSpeed")
    protected DecimalWithUnits maximumShutterSpeed;
    @XmlElement(name = "MaximumVerticalResolution")
    protected NonNegativeIntegerWithUnits maximumVerticalResolution;
    @XmlElement(name = "MaximumWeightRecommendation")
    protected DecimalWithUnits maximumWeightRecommendation;
    @XmlElement(name = "MemorySlotsAvailable")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger memorySlotsAvailable;
    @XmlElement(name = "MetalStamp")
    protected String metalStamp;
    @XmlElement(name = "MetalType")
    protected String metalType;
    @XmlElement(name = "MiniMovieDescription")
    protected String miniMovieDescription;
    @XmlElement(name = "MinimumAdvertisedPrice")
    protected Price minimumAdvertisedPrice;
    @XmlElement(name = "MinimumFocalLength")
    protected DecimalWithUnits minimumFocalLength;
    @XmlElement(name = "MinimumShutterSpeed")
    protected DecimalWithUnits minimumShutterSpeed;
    @XmlElement(name = "Model")
    protected String model;
    @XmlElement(name = "ModelYear")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger modelYear;
    @XmlElement(name = "ModemDescription")
    protected String modemDescription;
    @XmlElement(name = "MonitorSize")
    protected DecimalWithUnits monitorSize;
    @XmlElement(name = "MonitorViewableDiagonalSize")
    protected DecimalWithUnits monitorViewableDiagonalSize;
    @XmlElement(name = "MouseDescription")
    protected String mouseDescription;
    @XmlElement(name = "MPN")
    protected String mpn;
    @XmlElement(name = "MusicalStyle")
    protected String musicalStyle;
    @XmlElement(name = "NativeResolution")
    protected String nativeResolution;
    @XmlElement(name = "Neighborhood")
    protected String neighborhood;
    @XmlElement(name = "NetworkInterfaceDescription")
    protected String networkInterfaceDescription;
    @XmlElement(name = "NotebookDisplayTechnology")
    protected String notebookDisplayTechnology;
    @XmlElement(name = "NotebookPointingDeviceDescription")
    protected String notebookPointingDeviceDescription;
    @XmlElement(name = "NumberOfDiscs")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfDiscs;
    @XmlElement(name = "NumberOfIssues")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfIssues;
    @XmlElement(name = "NumberOfItems")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfItems;
    @XmlElement(name = "NumberOfKeys")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfKeys;
    @XmlElement(name = "NumberOfPages")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfPages;
    @XmlElement(name = "NumberOfPearls")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfPearls;
    @XmlElement(name = "NumberOfRapidFireShots")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfRapidFireShots;
    @XmlElement(name = "NumberOfStones")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfStones;
    @XmlElement(name = "NumberOfStrings")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfStrings;
    @XmlElement(name = "NumberOfTracks")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger numberOfTracks;
    @XmlElement(name = "OpticalZoom")
    protected DecimalWithUnits opticalZoom;
    @XmlElement(name = "OriginalReleaseDate")
    protected String originalReleaseDate;
    @XmlElement(name = "OutputWattage")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger outputWattage;
    @XmlElement(name = "PackageDimensions")
    protected MerchantItemAttributes.PackageDimensions packageDimensions;
    @XmlElement(name = "PearlLustre")
    protected String pearlLustre;
    @XmlElement(name = "PearlMinimumColor")
    protected String pearlMinimumColor;
    @XmlElement(name = "PearlShape")
    protected String pearlShape;
    @XmlElement(name = "PearlStringingMethod")
    protected String pearlStringingMethod;
    @XmlElement(name = "PearlSurfaceBlemishes")
    protected String pearlSurfaceBlemishes;
    @XmlElement(name = "PearlType")
    protected String pearlType;
    @XmlElement(name = "PearlUniformity")
    protected String pearlUniformity;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "PhotoFlashType")
    protected List<String> photoFlashType;
    @XmlElement(name = "PictureFormat")
    protected List<String> pictureFormat;
    @XmlElement(name = "Platform")
    protected List<String> platform;
    @XmlElement(name = "PriceRating")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger priceRating;
    @XmlElement(name = "ProcessorCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger processorCount;
    @XmlElement(name = "ProductGroup")
    protected String productGroup;
    @XmlElement(name = "PromotionalTag")
    protected String promotionalTag;
    @XmlElement(name = "POBoxShippingExcluded")
    protected String poBoxShippingExcluded;
    @XmlElement(name = "PublicationDate")
    protected String publicationDate;
    @XmlElement(name = "Publisher")
    protected String publisher;
    @XmlElement(name = "PurchasingChannel")
    protected List<String> purchasingChannel;
    @XmlElement(name = "ReadingLevel")
    protected String readingLevel;
    @XmlElement(name = "RecorderTrackCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger recorderTrackCount;
    @XmlElement(name = "RegionCode")
    protected String regionCode;
    @XmlElement(name = "RegionOfOrigin")
    protected String regionOfOrigin;
    @XmlElement(name = "ReleaseDate")
    protected String releaseDate;
    @XmlElement(name = "ReturnMethod")
    protected List<String> returnMethod;
    @XmlElement(name = "RemovableMemory")
    protected String removableMemory;
    @XmlElement(name = "ResolutionModes")
    protected String resolutionModes;
    @XmlElement(name = "ReturnPolicy")
    protected String returnPolicy;
    @XmlElement(name = "RingSize")
    protected String ringSize;
    @XmlElement(name = "SafetyWarning")
    protected String safetyWarning;
    @XmlElement(name = "SalesRestriction")
    protected String salesRestriction;
    @XmlElement(name = "SecondaryCacheSize")
    protected NonNegativeIntegerWithUnits secondaryCacheSize;
    @XmlElement(name = "SettingType")
    protected String settingType;
    @XmlElement(name = "Size")
    protected String size;
    @XmlElement(name = "SKU")
    protected String sku;
    @XmlElement(name = "SoldInStores")
    protected String soldInStores;
    @XmlElement(name = "SizePerPearl")
    protected String sizePerPearl;
    @XmlElement(name = "SkillLevel")
    protected String skillLevel;
    @XmlElement(name = "SoundCardDescription")
    protected String soundCardDescription;
    @XmlElement(name = "SpeakerCount")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger speakerCount;
    @XmlElement(name = "SpeakerDescription")
    protected String speakerDescription;
    @XmlElement(name = "SpecialFeatures")
    protected List<String> specialFeatures;
    @XmlElement(name = "StoneClarity")
    protected String stoneClarity;
    @XmlElement(name = "StoneColor")
    protected String stoneColor;
    @XmlElement(name = "StoneCut")
    protected String stoneCut;
    @XmlElement(name = "StoneShape")
    protected String stoneShape;
    @XmlElement(name = "StoneWeight")
    protected DecimalWithUnits stoneWeight;
    @XmlElement(name = "Studio")
    protected String studio;
    @XmlElement(name = "SubscriptionLength")
    protected NonNegativeIntegerWithUnits subscriptionLength;
    @XmlElement(name = "SupportedImageType")
    protected List<String> supportedImageType;
    @XmlElement(name = "SystemBusSpeed")
    protected DecimalWithUnits systemBusSpeed;
    @XmlElement(name = "SystemMemorySizeMax")
    protected DecimalWithUnits systemMemorySizeMax;
    @XmlElement(name = "SystemMemorySize")
    protected DecimalWithUnits systemMemorySize;
    @XmlElement(name = "SystemMemoryType")
    protected String systemMemoryType;
    @XmlElement(name = "TellingPageIndicator")
    protected String tellingPageIndicator;
    @XmlElement(name = "TheatricalReleaseDate")
    protected String theatricalReleaseDate;
    @XmlElement(name = "Title")
    protected String title;
    @XmlElement(name = "TotalDiamondWeight")
    protected DecimalWithUnits totalDiamondWeight;
    @XmlElement(name = "TotalExternalBaysFree")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalExternalBaysFree;
    @XmlElement(name = "TotalFirewirePorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalFirewirePorts;
    @XmlElement(name = "TotalGemWeight")
    protected DecimalWithUnits totalGemWeight;
    @XmlElement(name = "TotalInternalBaysFree")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalInternalBaysFree;
    @XmlElement(name = "TotalMetalWeight")
    protected DecimalWithUnits totalMetalWeight;
    @XmlElement(name = "TotalNTSCPALPorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalNTSCPALPorts;
    @XmlElement(name = "TotalParallelPorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalParallelPorts;
    @XmlElement(name = "TotalPCCardSlots")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalPCCardSlots;
    @XmlElement(name = "TotalPCISlotsFree")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalPCISlotsFree;
    @XmlElement(name = "TotalSerialPorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalSerialPorts;
    @XmlElement(name = "TotalSVideoOutPorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalSVideoOutPorts;
    @XmlElement(name = "TotalUSB2Ports")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalUSB2Ports;
    @XmlElement(name = "TotalUSBPorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalUSBPorts;
    @XmlElement(name = "TotalVGAOutPorts")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger totalVGAOutPorts;
    @XmlElement(name = "UPC")
    protected String upc;
    @XmlElement(name = "VariationDenomination")
    protected String variationDenomination;
    @XmlElement(name = "VariationDescription")
    protected String variationDescription;
    @XmlElement(name = "VendorRebate")
    protected MerchantItemAttributes.VendorRebate vendorRebate;
    @XmlElement(name = "Warranty")
    protected String warranty;
    @XmlElement(name = "WatchMovementType")
    protected String watchMovementType;
    @XmlElement(name = "WebsiteBuyability")
    protected String websiteBuyability;
    @XmlElement(name = "WaterResistanceDepth")
    protected DecimalWithUnits waterResistanceDepth;
    @XmlElement(name = "WirelessMicrophoneFrequency")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger wirelessMicrophoneFrequency;

    /**
     * Gets the value of the actor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getActor() {
        if (actor == null) {
            actor = new ArrayList<String>();
        }
        return this.actor;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setAddress(Address value) {
        this.address = value;
    }

    /**
     * Gets the value of the amazonMaximumAge property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getAmazonMaximumAge() {
        return amazonMaximumAge;
    }

    /**
     * Sets the value of the amazonMaximumAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setAmazonMaximumAge(DecimalWithUnits value) {
        this.amazonMaximumAge = value;
    }

    /**
     * Gets the value of the amazonMinimumAge property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getAmazonMinimumAge() {
        return amazonMinimumAge;
    }

    /**
     * Sets the value of the amazonMinimumAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setAmazonMinimumAge(DecimalWithUnits value) {
        this.amazonMinimumAge = value;
    }

    /**
     * Gets the value of the apertureModes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApertureModes() {
        return apertureModes;
    }

    /**
     * Sets the value of the apertureModes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApertureModes(String value) {
        this.apertureModes = value;
    }

    /**
     * Gets the value of the artist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the artist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArtist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getArtist() {
        if (artist == null) {
            artist = new ArrayList<String>();
        }
        return this.artist;
    }

    /**
     * Gets the value of the aspectRatio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAspectRatio() {
        return aspectRatio;
    }

    /**
     * Sets the value of the aspectRatio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAspectRatio(String value) {
        this.aspectRatio = value;
    }

    /**
     * Gets the value of the assemblyInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssemblyInstructions() {
        return assemblyInstructions;
    }

    /**
     * Sets the value of the assemblyInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssemblyInstructions(String value) {
        this.assemblyInstructions = value;
    }

    /**
     * Gets the value of the assemblyRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssemblyRequired() {
        return assemblyRequired;
    }

    /**
     * Sets the value of the assemblyRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssemblyRequired(String value) {
        this.assemblyRequired = value;
    }

    /**
     * Gets the value of the audienceRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAudienceRating() {
        return audienceRating;
    }

    /**
     * Sets the value of the audienceRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAudienceRating(String value) {
        this.audienceRating = value;
    }

    /**
     * Gets the value of the audioFormat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the audioFormat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAudioFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAudioFormat() {
        if (audioFormat == null) {
            audioFormat = new ArrayList<String>();
        }
        return this.audioFormat;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAuthor() {
        if (author == null) {
            author = new ArrayList<String>();
        }
        return this.author;
    }

    /**
     * Gets the value of the backFinding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackFinding() {
        return backFinding;
    }

    /**
     * Sets the value of the backFinding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackFinding(String value) {
        this.backFinding = value;
    }

    /**
     * Gets the value of the bandMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandMaterialType() {
        return bandMaterialType;
    }

    /**
     * Sets the value of the bandMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandMaterialType(String value) {
        this.bandMaterialType = value;
    }

    /**
     * Gets the value of the batteriesIncluded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatteriesIncluded() {
        return batteriesIncluded;
    }

    /**
     * Sets the value of the batteriesIncluded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatteriesIncluded(String value) {
        this.batteriesIncluded = value;
    }

    /**
     * Gets the value of the batteriesRequired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatteriesRequired() {
        return batteriesRequired;
    }

    /**
     * Sets the value of the batteriesRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatteriesRequired(String value) {
        this.batteriesRequired = value;
    }

    /**
     * Gets the value of the batteries property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getBatteries() {
        return batteries;
    }

    /**
     * Sets the value of the batteries property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setBatteries(NonNegativeIntegerWithUnits value) {
        this.batteries = value;
    }

    /**
     * Gets the value of the batteryDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatteryDescription() {
        return batteryDescription;
    }

    /**
     * Sets the value of the batteryDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatteryDescription(String value) {
        this.batteryDescription = value;
    }

    /**
     * Gets the value of the batteryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBatteryType() {
        return batteryType;
    }

    /**
     * Sets the value of the batteryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBatteryType(String value) {
        this.batteryType = value;
    }

    /**
     * Gets the value of the bezelMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBezelMaterialType() {
        return bezelMaterialType;
    }

    /**
     * Sets the value of the bezelMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBezelMaterialType(String value) {
        this.bezelMaterialType = value;
    }

    /**
     * Gets the value of the binding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinding() {
        return binding;
    }

    /**
     * Sets the value of the binding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinding(String value) {
        this.binding = value;
    }

    /**
     * Gets the value of the brand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the value of the brand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBrand(String value) {
        this.brand = value;
    }

    /**
     * Gets the value of the calendarType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalendarType() {
        return calendarType;
    }

    /**
     * Sets the value of the calendarType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalendarType(String value) {
        this.calendarType = value;
    }

    /**
     * Gets the value of the cameraManualFeatures property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cameraManualFeatures property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCameraManualFeatures().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCameraManualFeatures() {
        if (cameraManualFeatures == null) {
            cameraManualFeatures = new ArrayList<String>();
        }
        return this.cameraManualFeatures;
    }

    /**
     * Gets the value of the caseDiameter property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getCaseDiameter() {
        return caseDiameter;
    }

    /**
     * Sets the value of the caseDiameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setCaseDiameter(DecimalWithUnits value) {
        this.caseDiameter = value;
    }

    /**
     * Gets the value of the caseMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseMaterialType() {
        return caseMaterialType;
    }

    /**
     * Sets the value of the caseMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseMaterialType(String value) {
        this.caseMaterialType = value;
    }

    /**
     * Gets the value of the caseThickness property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getCaseThickness() {
        return caseThickness;
    }

    /**
     * Sets the value of the caseThickness property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setCaseThickness(DecimalWithUnits value) {
        this.caseThickness = value;
    }

    /**
     * Gets the value of the caseType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseType() {
        return caseType;
    }

    /**
     * Sets the value of the caseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseType(String value) {
        this.caseType = value;
    }

    /**
     * Gets the value of the catalogNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCatalogNumber() {
        return catalogNumber;
    }

    /**
     * Sets the value of the catalogNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCatalogNumber(String value) {
        this.catalogNumber = value;
    }

    /**
     * Gets the value of the cdrwDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCDRWDescription() {
        return cdrwDescription;
    }

    /**
     * Sets the value of the cdrwDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCDRWDescription(String value) {
        this.cdrwDescription = value;
    }

    /**
     * Gets the value of the chainType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChainType() {
        return chainType;
    }

    /**
     * Sets the value of the chainType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChainType(String value) {
        this.chainType = value;
    }

    /**
     * Gets the value of the claspType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaspType() {
        return claspType;
    }

    /**
     * Sets the value of the claspType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaspType(String value) {
        this.claspType = value;
    }

    /**
     * Gets the value of the clothingSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClothingSize() {
        return clothingSize;
    }

    /**
     * Sets the value of the clothingSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClothingSize(String value) {
        this.clothingSize = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the compatibility property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompatibility() {
        return compatibility;
    }

    /**
     * Sets the value of the compatibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompatibility(String value) {
        this.compatibility = value;
    }

    /**
     * Gets the value of the computerHardwareType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComputerHardwareType() {
        return computerHardwareType;
    }

    /**
     * Sets the value of the computerHardwareType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComputerHardwareType(String value) {
        this.computerHardwareType = value;
    }

    /**
     * Gets the value of the computerPlatform property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComputerPlatform() {
        return computerPlatform;
    }

    /**
     * Sets the value of the computerPlatform property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComputerPlatform(String value) {
        this.computerPlatform = value;
    }

    /**
     * Gets the value of the connectivity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectivity() {
        return connectivity;
    }

    /**
     * Sets the value of the connectivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectivity(String value) {
        this.connectivity = value;
    }

    /**
     * Gets the value of the continuousShootingSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getContinuousShootingSpeed() {
        return continuousShootingSpeed;
    }

    /**
     * Sets the value of the continuousShootingSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setContinuousShootingSpeed(DecimalWithUnits value) {
        this.continuousShootingSpeed = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the countryOfOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    /**
     * Sets the value of the countryOfOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryOfOrigin(String value) {
        this.countryOfOrigin = value;
    }

    /**
     * Gets the value of the cpuManufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPUManufacturer() {
        return cpuManufacturer;
    }

    /**
     * Sets the value of the cpuManufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPUManufacturer(String value) {
        this.cpuManufacturer = value;
    }

    /**
     * Gets the value of the cpuSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getCPUSpeed() {
        return cpuSpeed;
    }

    /**
     * Sets the value of the cpuSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setCPUSpeed(DecimalWithUnits value) {
        this.cpuSpeed = value;
    }

    /**
     * Gets the value of the cpuType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPUType() {
        return cpuType;
    }

    /**
     * Sets the value of the cpuType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPUType(String value) {
        this.cpuType = value;
    }

    /**
     * Gets the value of the creator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCreator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MerchantItemAttributes.Creator }
     * 
     * 
     */
    public List<MerchantItemAttributes.Creator> getCreator() {
        if (creator == null) {
            creator = new ArrayList<MerchantItemAttributes.Creator>();
        }
        return this.creator;
    }

    /**
     * Gets the value of the cuisine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuisine() {
        return cuisine;
    }

    /**
     * Sets the value of the cuisine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuisine(String value) {
        this.cuisine = value;
    }

    /**
     * Gets the value of the customizable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomizable() {
        return customizable;
    }

    /**
     * Sets the value of the customizable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomizable(String value) {
        this.customizable = value;
    }

    /**
     * Gets the value of the delayBetweenShots property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getDelayBetweenShots() {
        return delayBetweenShots;
    }

    /**
     * Sets the value of the delayBetweenShots property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setDelayBetweenShots(DecimalWithUnits value) {
        this.delayBetweenShots = value;
    }

    /**
     * Gets the value of the deliveryOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryOption() {
        return deliveryOption;
    }

    /**
     * Sets the value of the deliveryOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryOption(String value) {
        this.deliveryOption = value;
    }

    /**
     * Gets the value of the department property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the value of the department property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartment(String value) {
        this.department = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the deweyDecimalNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeweyDecimalNumber() {
        return deweyDecimalNumber;
    }

    /**
     * Sets the value of the deweyDecimalNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeweyDecimalNumber(String value) {
        this.deweyDecimalNumber = value;
    }

    /**
     * Gets the value of the dialColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDialColor() {
        return dialColor;
    }

    /**
     * Sets the value of the dialColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDialColor(String value) {
        this.dialColor = value;
    }

    /**
     * Gets the value of the dialWindowMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDialWindowMaterialType() {
        return dialWindowMaterialType;
    }

    /**
     * Sets the value of the dialWindowMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDialWindowMaterialType(String value) {
        this.dialWindowMaterialType = value;
    }

    /**
     * Gets the value of the digitalZoom property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getDigitalZoom() {
        return digitalZoom;
    }

    /**
     * Sets the value of the digitalZoom property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setDigitalZoom(DecimalWithUnits value) {
        this.digitalZoom = value;
    }

    /**
     * Gets the value of the director property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the director property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDirector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDirector() {
        if (director == null) {
            director = new ArrayList<String>();
        }
        return this.director;
    }

    /**
     * Gets the value of the displaySize property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getDisplaySize() {
        return displaySize;
    }

    /**
     * Sets the value of the displaySize property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setDisplaySize(DecimalWithUnits value) {
        this.displaySize = value;
    }

    /**
     * Gets the value of the drumSetPieceQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDrumSetPieceQuantity() {
        return drumSetPieceQuantity;
    }

    /**
     * Sets the value of the drumSetPieceQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDrumSetPieceQuantity(BigInteger value) {
        this.drumSetPieceQuantity = value;
    }

    /**
     * Gets the value of the dvdLayers property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDVDLayers() {
        return dvdLayers;
    }

    /**
     * Sets the value of the dvdLayers property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDVDLayers(BigInteger value) {
        this.dvdLayers = value;
    }

    /**
     * Gets the value of the dvdrwDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDVDRWDescription() {
        return dvdrwDescription;
    }

    /**
     * Sets the value of the dvdrwDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDVDRWDescription(String value) {
        this.dvdrwDescription = value;
    }

    /**
     * Gets the value of the dvdSides property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDVDSides() {
        return dvdSides;
    }

    /**
     * Sets the value of the dvdSides property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDVDSides(BigInteger value) {
        this.dvdSides = value;
    }

    /**
     * Gets the value of the dpci property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDPCI() {
        return dpci;
    }

    /**
     * Sets the value of the dpci property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDPCI(String value) {
        this.dpci = value;
    }

    /**
     * Gets the value of the ean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEAN() {
        return ean;
    }

    /**
     * Sets the value of the ean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEAN(String value) {
        this.ean = value;
    }

    /**
     * Gets the value of the edition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Sets the value of the edition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEdition(String value) {
        this.edition = value;
    }

    /**
     * Gets the value of the esrbAgeRating property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getESRBAgeRating() {
        return esrbAgeRating;
    }

    /**
     * Sets the value of the esrbAgeRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setESRBAgeRating(String value) {
        this.esrbAgeRating = value;
    }

    /**
     * Gets the value of the externalDisplaySupportDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalDisplaySupportDescription() {
        return externalDisplaySupportDescription;
    }

    /**
     * Sets the value of the externalDisplaySupportDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalDisplaySupportDescription(String value) {
        this.externalDisplaySupportDescription = value;
    }

    /**
     * Gets the value of the fabricType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFabricType() {
        return fabricType;
    }

    /**
     * Sets the value of the fabricType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFabricType(String value) {
        this.fabricType = value;
    }

    /**
     * Gets the value of the faxNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Sets the value of the faxNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxNumber(String value) {
        this.faxNumber = value;
    }

    /**
     * Gets the value of the feature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the feature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFeature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFeature() {
        if (feature == null) {
            feature = new ArrayList<String>();
        }
        return this.feature;
    }

    /**
     * Gets the value of the firstIssueLeadTime property.
     * 
     * @return
     *     possible object is
     *     {@link StringWithUnits }
     *     
     */
    public StringWithUnits getFirstIssueLeadTime() {
        return firstIssueLeadTime;
    }

    /**
     * Sets the value of the firstIssueLeadTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringWithUnits }
     *     
     */
    public void setFirstIssueLeadTime(StringWithUnits value) {
        this.firstIssueLeadTime = value;
    }

    /**
     * Gets the value of the floppyDiskDriveDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFloppyDiskDriveDescription() {
        return floppyDiskDriveDescription;
    }

    /**
     * Sets the value of the floppyDiskDriveDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFloppyDiskDriveDescription(String value) {
        this.floppyDiskDriveDescription = value;
    }

    /**
     * Gets the value of the format property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the format property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFormat() {
        if (format == null) {
            format = new ArrayList<String>();
        }
        return this.format;
    }

    /**
     * Gets the value of the fixedShippingCharge property.
     * 
     * @return
     *     possible object is
     *     {@link Price }
     *     
     */
    public Price getFixedShippingCharge() {
        return fixedShippingCharge;
    }

    /**
     * Sets the value of the fixedShippingCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Price }
     *     
     */
    public void setFixedShippingCharge(Price value) {
        this.fixedShippingCharge = value;
    }

    /**
     * Gets the value of the gemType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGemType() {
        return gemType;
    }

    /**
     * Sets the value of the gemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGemType(String value) {
        this.gemType = value;
    }

    /**
     * Gets the value of the graphicsCardInterface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphicsCardInterface() {
        return graphicsCardInterface;
    }

    /**
     * Sets the value of the graphicsCardInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphicsCardInterface(String value) {
        this.graphicsCardInterface = value;
    }

    /**
     * Gets the value of the graphicsDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphicsDescription() {
        return graphicsDescription;
    }

    /**
     * Sets the value of the graphicsDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphicsDescription(String value) {
        this.graphicsDescription = value;
    }

    /**
     * Gets the value of the graphicsMemorySize property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getGraphicsMemorySize() {
        return graphicsMemorySize;
    }

    /**
     * Sets the value of the graphicsMemorySize property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setGraphicsMemorySize(DecimalWithUnits value) {
        this.graphicsMemorySize = value;
    }

    /**
     * Gets the value of the guitarAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuitarAttribute() {
        return guitarAttribute;
    }

    /**
     * Sets the value of the guitarAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuitarAttribute(String value) {
        this.guitarAttribute = value;
    }

    /**
     * Gets the value of the guitarBridgeSystem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuitarBridgeSystem() {
        return guitarBridgeSystem;
    }

    /**
     * Sets the value of the guitarBridgeSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuitarBridgeSystem(String value) {
        this.guitarBridgeSystem = value;
    }

    /**
     * Gets the value of the guitarPickThickness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuitarPickThickness() {
        return guitarPickThickness;
    }

    /**
     * Sets the value of the guitarPickThickness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuitarPickThickness(String value) {
        this.guitarPickThickness = value;
    }

    /**
     * Gets the value of the guitarPickupConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuitarPickupConfiguration() {
        return guitarPickupConfiguration;
    }

    /**
     * Sets the value of the guitarPickupConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuitarPickupConfiguration(String value) {
        this.guitarPickupConfiguration = value;
    }

    /**
     * Gets the value of the hardDiskCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHardDiskCount() {
        return hardDiskCount;
    }

    /**
     * Sets the value of the hardDiskCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHardDiskCount(BigInteger value) {
        this.hardDiskCount = value;
    }

    /**
     * Gets the value of the hardDiskSize property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getHardDiskSize() {
        return hardDiskSize;
    }

    /**
     * Sets the value of the hardDiskSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setHardDiskSize(NonNegativeIntegerWithUnits value) {
        this.hardDiskSize = value;
    }

    /**
     * Gets the value of the hasAutoFocus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasAutoFocus() {
        return hasAutoFocus;
    }

    /**
     * Sets the value of the hasAutoFocus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasAutoFocus(Boolean value) {
        this.hasAutoFocus = value;
    }

    /**
     * Gets the value of the hasBurstMode property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasBurstMode() {
        return hasBurstMode;
    }

    /**
     * Sets the value of the hasBurstMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasBurstMode(Boolean value) {
        this.hasBurstMode = value;
    }

    /**
     * Gets the value of the hasInCameraEditing property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasInCameraEditing() {
        return hasInCameraEditing;
    }

    /**
     * Sets the value of the hasInCameraEditing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasInCameraEditing(Boolean value) {
        this.hasInCameraEditing = value;
    }

    /**
     * Gets the value of the hasRedEyeReduction property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasRedEyeReduction() {
        return hasRedEyeReduction;
    }

    /**
     * Sets the value of the hasRedEyeReduction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasRedEyeReduction(Boolean value) {
        this.hasRedEyeReduction = value;
    }

    /**
     * Gets the value of the hasSelfTimer property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasSelfTimer() {
        return hasSelfTimer;
    }

    /**
     * Sets the value of the hasSelfTimer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasSelfTimer(Boolean value) {
        this.hasSelfTimer = value;
    }

    /**
     * Gets the value of the hasTripodMount property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasTripodMount() {
        return hasTripodMount;
    }

    /**
     * Sets the value of the hasTripodMount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasTripodMount(Boolean value) {
        this.hasTripodMount = value;
    }

    /**
     * Gets the value of the hasVideoOut property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasVideoOut() {
        return hasVideoOut;
    }

    /**
     * Sets the value of the hasVideoOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasVideoOut(Boolean value) {
        this.hasVideoOut = value;
    }

    /**
     * Gets the value of the hasViewfinder property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasViewfinder() {
        return hasViewfinder;
    }

    /**
     * Sets the value of the hasViewfinder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasViewfinder(Boolean value) {
        this.hasViewfinder = value;
    }

    /**
     * Gets the value of the hazardousMaterialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHazardousMaterialType() {
        return hazardousMaterialType;
    }

    /**
     * Sets the value of the hazardousMaterialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHazardousMaterialType(String value) {
        this.hazardousMaterialType = value;
    }

    /**
     * Gets the value of the hoursOfOperation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    /**
     * Sets the value of the hoursOfOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoursOfOperation(String value) {
        this.hoursOfOperation = value;
    }

    /**
     * Gets the value of the includedSoftware property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIncludedSoftware() {
        return includedSoftware;
    }

    /**
     * Sets the value of the includedSoftware property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIncludedSoftware(String value) {
        this.includedSoftware = value;
    }

    /**
     * Gets the value of the includesMp3Player property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludesMp3Player() {
        return includesMp3Player;
    }

    /**
     * Sets the value of the includesMp3Player property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludesMp3Player(Boolean value) {
        this.includesMp3Player = value;
    }

    /**
     * Gets the value of the indications property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndications() {
        return indications;
    }

    /**
     * Sets the value of the indications property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndications(String value) {
        this.indications = value;
    }

    /**
     * Gets the value of the ingredients property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIngredients() {
        return ingredients;
    }

    /**
     * Sets the value of the ingredients property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIngredients(String value) {
        this.ingredients = value;
    }

    /**
     * Gets the value of the ingredientsSetElement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ingredientsSetElement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIngredientsSetElement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIngredientsSetElement() {
        if (ingredientsSetElement == null) {
            ingredientsSetElement = new ArrayList<String>();
        }
        return this.ingredientsSetElement;
    }

    /**
     * Gets the value of the instrumentKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrumentKey() {
        return instrumentKey;
    }

    /**
     * Sets the value of the instrumentKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrumentKey(String value) {
        this.instrumentKey = value;
    }

    /**
     * Gets the value of the isAutographed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAutographed() {
        return isAutographed;
    }

    /**
     * Sets the value of the isAutographed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAutographed(Boolean value) {
        this.isAutographed = value;
    }

    /**
     * Gets the value of the isbn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISBN() {
        return isbn;
    }

    /**
     * Sets the value of the isbn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISBN(String value) {
        this.isbn = value;
    }

    /**
     * Gets the value of the isEmailNotifyAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsEmailNotifyAvailable() {
        return isEmailNotifyAvailable;
    }

    /**
     * Sets the value of the isEmailNotifyAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEmailNotifyAvailable(Boolean value) {
        this.isEmailNotifyAvailable = value;
    }

    /**
     * Gets the value of the isFragile property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFragile() {
        return isFragile;
    }

    /**
     * Sets the value of the isFragile property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFragile(Boolean value) {
        this.isFragile = value;
    }

    /**
     * Gets the value of the isLabCreated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsLabCreated() {
        return isLabCreated;
    }

    /**
     * Sets the value of the isLabCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsLabCreated(Boolean value) {
        this.isLabCreated = value;
    }

    /**
     * Gets the value of the isMemorabilia property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsMemorabilia() {
        return isMemorabilia;
    }

    /**
     * Sets the value of the isMemorabilia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMemorabilia(Boolean value) {
        this.isMemorabilia = value;
    }

    /**
     * Gets the value of the isoEquivalent property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getISOEquivalent() {
        return isoEquivalent;
    }

    /**
     * Sets the value of the isoEquivalent property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setISOEquivalent(NonNegativeIntegerWithUnits value) {
        this.isoEquivalent = value;
    }

    /**
     * Gets the value of the issuesPerYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuesPerYear() {
        return issuesPerYear;
    }

    /**
     * Sets the value of the issuesPerYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuesPerYear(String value) {
        this.issuesPerYear = value;
    }

    /**
     * Gets the value of the itemDimensions property.
     * 
     * @return
     *     possible object is
     *     {@link MerchantItemAttributes.ItemDimensions }
     *     
     */
    public MerchantItemAttributes.ItemDimensions getItemDimensions() {
        return itemDimensions;
    }

    /**
     * Sets the value of the itemDimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link MerchantItemAttributes.ItemDimensions }
     *     
     */
    public void setItemDimensions(MerchantItemAttributes.ItemDimensions value) {
        this.itemDimensions = value;
    }

    /**
     * Gets the value of the keyboardDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyboardDescription() {
        return keyboardDescription;
    }

    /**
     * Sets the value of the keyboardDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyboardDescription(String value) {
        this.keyboardDescription = value;
    }

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the languages property.
     * 
     * @return
     *     possible object is
     *     {@link MerchantItemAttributes.Languages }
     *     
     */
    public MerchantItemAttributes.Languages getLanguages() {
        return languages;
    }

    /**
     * Sets the value of the languages property.
     * 
     * @param value
     *     allowed object is
     *     {@link MerchantItemAttributes.Languages }
     *     
     */
    public void setLanguages(MerchantItemAttributes.Languages value) {
        this.languages = value;
    }

    /**
     * Gets the value of the legalDisclaimer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalDisclaimer() {
        return legalDisclaimer;
    }

    /**
     * Sets the value of the legalDisclaimer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalDisclaimer(String value) {
        this.legalDisclaimer = value;
    }

    /**
     * Gets the value of the lineVoltage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLineVoltage() {
        return lineVoltage;
    }

    /**
     * Sets the value of the lineVoltage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLineVoltage(String value) {
        this.lineVoltage = value;
    }

    /**
     * Gets the value of the listPrice property.
     * 
     * @return
     *     possible object is
     *     {@link Price }
     *     
     */
    public Price getListPrice() {
        return listPrice;
    }

    /**
     * Sets the value of the listPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Price }
     *     
     */
    public void setListPrice(Price value) {
        this.listPrice = value;
    }

    /**
     * Gets the value of the macroFocusRange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMacroFocusRange() {
        return macroFocusRange;
    }

    /**
     * Sets the value of the macroFocusRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMacroFocusRange(String value) {
        this.macroFocusRange = value;
    }

    /**
     * Gets the value of the magazineType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMagazineType() {
        return magazineType;
    }

    /**
     * Sets the value of the magazineType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMagazineType(String value) {
        this.magazineType = value;
    }

    /**
     * Gets the value of the malletHardness property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMalletHardness() {
        return malletHardness;
    }

    /**
     * Sets the value of the malletHardness property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMalletHardness(String value) {
        this.malletHardness = value;
    }

    /**
     * Gets the value of the manufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturer(String value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the manufacturerLaborWarrantyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerLaborWarrantyDescription() {
        return manufacturerLaborWarrantyDescription;
    }

    /**
     * Sets the value of the manufacturerLaborWarrantyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerLaborWarrantyDescription(String value) {
        this.manufacturerLaborWarrantyDescription = value;
    }

    /**
     * Gets the value of the manufacturerMaximumAge property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getManufacturerMaximumAge() {
        return manufacturerMaximumAge;
    }

    /**
     * Sets the value of the manufacturerMaximumAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setManufacturerMaximumAge(DecimalWithUnits value) {
        this.manufacturerMaximumAge = value;
    }

    /**
     * Gets the value of the manufacturerMinimumAge property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getManufacturerMinimumAge() {
        return manufacturerMinimumAge;
    }

    /**
     * Sets the value of the manufacturerMinimumAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setManufacturerMinimumAge(DecimalWithUnits value) {
        this.manufacturerMinimumAge = value;
    }

    /**
     * Gets the value of the manufacturerPartsWarrantyDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerPartsWarrantyDescription() {
        return manufacturerPartsWarrantyDescription;
    }

    /**
     * Sets the value of the manufacturerPartsWarrantyDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerPartsWarrantyDescription(String value) {
        this.manufacturerPartsWarrantyDescription = value;
    }

    /**
     * Gets the value of the materialType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterialType() {
        return materialType;
    }

    /**
     * Sets the value of the materialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterialType(String value) {
        this.materialType = value;
    }

    /**
     * Gets the value of the maximumAperture property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMaximumAperture() {
        return maximumAperture;
    }

    /**
     * Sets the value of the maximumAperture property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMaximumAperture(DecimalWithUnits value) {
        this.maximumAperture = value;
    }

    /**
     * Gets the value of the maximumColorDepth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumColorDepth() {
        return maximumColorDepth;
    }

    /**
     * Sets the value of the maximumColorDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumColorDepth(String value) {
        this.maximumColorDepth = value;
    }

    /**
     * Gets the value of the maximumFocalLength property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMaximumFocalLength() {
        return maximumFocalLength;
    }

    /**
     * Sets the value of the maximumFocalLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMaximumFocalLength(DecimalWithUnits value) {
        this.maximumFocalLength = value;
    }

    /**
     * Gets the value of the maximumHighResolutionImages property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getMaximumHighResolutionImages() {
        return maximumHighResolutionImages;
    }

    /**
     * Sets the value of the maximumHighResolutionImages property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setMaximumHighResolutionImages(NonNegativeIntegerWithUnits value) {
        this.maximumHighResolutionImages = value;
    }

    /**
     * Gets the value of the maximumHorizontalResolution property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getMaximumHorizontalResolution() {
        return maximumHorizontalResolution;
    }

    /**
     * Sets the value of the maximumHorizontalResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setMaximumHorizontalResolution(NonNegativeIntegerWithUnits value) {
        this.maximumHorizontalResolution = value;
    }

    /**
     * Gets the value of the maximumLowResolutionImages property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumLowResolutionImages() {
        return maximumLowResolutionImages;
    }

    /**
     * Sets the value of the maximumLowResolutionImages property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumLowResolutionImages(String value) {
        this.maximumLowResolutionImages = value;
    }

    /**
     * Gets the value of the maximumResolution property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMaximumResolution() {
        return maximumResolution;
    }

    /**
     * Sets the value of the maximumResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMaximumResolution(DecimalWithUnits value) {
        this.maximumResolution = value;
    }

    /**
     * Gets the value of the maximumShutterSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMaximumShutterSpeed() {
        return maximumShutterSpeed;
    }

    /**
     * Sets the value of the maximumShutterSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMaximumShutterSpeed(DecimalWithUnits value) {
        this.maximumShutterSpeed = value;
    }

    /**
     * Gets the value of the maximumVerticalResolution property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getMaximumVerticalResolution() {
        return maximumVerticalResolution;
    }

    /**
     * Sets the value of the maximumVerticalResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setMaximumVerticalResolution(NonNegativeIntegerWithUnits value) {
        this.maximumVerticalResolution = value;
    }

    /**
     * Gets the value of the maximumWeightRecommendation property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMaximumWeightRecommendation() {
        return maximumWeightRecommendation;
    }

    /**
     * Sets the value of the maximumWeightRecommendation property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMaximumWeightRecommendation(DecimalWithUnits value) {
        this.maximumWeightRecommendation = value;
    }

    /**
     * Gets the value of the memorySlotsAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMemorySlotsAvailable() {
        return memorySlotsAvailable;
    }

    /**
     * Sets the value of the memorySlotsAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMemorySlotsAvailable(BigInteger value) {
        this.memorySlotsAvailable = value;
    }

    /**
     * Gets the value of the metalStamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetalStamp() {
        return metalStamp;
    }

    /**
     * Sets the value of the metalStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetalStamp(String value) {
        this.metalStamp = value;
    }

    /**
     * Gets the value of the metalType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetalType() {
        return metalType;
    }

    /**
     * Sets the value of the metalType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetalType(String value) {
        this.metalType = value;
    }

    /**
     * Gets the value of the miniMovieDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiniMovieDescription() {
        return miniMovieDescription;
    }

    /**
     * Sets the value of the miniMovieDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiniMovieDescription(String value) {
        this.miniMovieDescription = value;
    }

    /**
     * Gets the value of the minimumAdvertisedPrice property.
     * 
     * @return
     *     possible object is
     *     {@link Price }
     *     
     */
    public Price getMinimumAdvertisedPrice() {
        return minimumAdvertisedPrice;
    }

    /**
     * Sets the value of the minimumAdvertisedPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Price }
     *     
     */
    public void setMinimumAdvertisedPrice(Price value) {
        this.minimumAdvertisedPrice = value;
    }

    /**
     * Gets the value of the minimumFocalLength property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMinimumFocalLength() {
        return minimumFocalLength;
    }

    /**
     * Sets the value of the minimumFocalLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMinimumFocalLength(DecimalWithUnits value) {
        this.minimumFocalLength = value;
    }

    /**
     * Gets the value of the minimumShutterSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMinimumShutterSpeed() {
        return minimumShutterSpeed;
    }

    /**
     * Sets the value of the minimumShutterSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMinimumShutterSpeed(DecimalWithUnits value) {
        this.minimumShutterSpeed = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the modelYear property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getModelYear() {
        return modelYear;
    }

    /**
     * Sets the value of the modelYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setModelYear(BigInteger value) {
        this.modelYear = value;
    }

    /**
     * Gets the value of the modemDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModemDescription() {
        return modemDescription;
    }

    /**
     * Sets the value of the modemDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModemDescription(String value) {
        this.modemDescription = value;
    }

    /**
     * Gets the value of the monitorSize property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMonitorSize() {
        return monitorSize;
    }

    /**
     * Sets the value of the monitorSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMonitorSize(DecimalWithUnits value) {
        this.monitorSize = value;
    }

    /**
     * Gets the value of the monitorViewableDiagonalSize property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getMonitorViewableDiagonalSize() {
        return monitorViewableDiagonalSize;
    }

    /**
     * Sets the value of the monitorViewableDiagonalSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setMonitorViewableDiagonalSize(DecimalWithUnits value) {
        this.monitorViewableDiagonalSize = value;
    }

    /**
     * Gets the value of the mouseDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMouseDescription() {
        return mouseDescription;
    }

    /**
     * Sets the value of the mouseDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMouseDescription(String value) {
        this.mouseDescription = value;
    }

    /**
     * Gets the value of the mpn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMPN() {
        return mpn;
    }

    /**
     * Sets the value of the mpn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMPN(String value) {
        this.mpn = value;
    }

    /**
     * Gets the value of the musicalStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMusicalStyle() {
        return musicalStyle;
    }

    /**
     * Sets the value of the musicalStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMusicalStyle(String value) {
        this.musicalStyle = value;
    }

    /**
     * Gets the value of the nativeResolution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNativeResolution() {
        return nativeResolution;
    }

    /**
     * Sets the value of the nativeResolution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNativeResolution(String value) {
        this.nativeResolution = value;
    }

    /**
     * Gets the value of the neighborhood property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Sets the value of the neighborhood property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNeighborhood(String value) {
        this.neighborhood = value;
    }

    /**
     * Gets the value of the networkInterfaceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkInterfaceDescription() {
        return networkInterfaceDescription;
    }

    /**
     * Sets the value of the networkInterfaceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkInterfaceDescription(String value) {
        this.networkInterfaceDescription = value;
    }

    /**
     * Gets the value of the notebookDisplayTechnology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotebookDisplayTechnology() {
        return notebookDisplayTechnology;
    }

    /**
     * Sets the value of the notebookDisplayTechnology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotebookDisplayTechnology(String value) {
        this.notebookDisplayTechnology = value;
    }

    /**
     * Gets the value of the notebookPointingDeviceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotebookPointingDeviceDescription() {
        return notebookPointingDeviceDescription;
    }

    /**
     * Sets the value of the notebookPointingDeviceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotebookPointingDeviceDescription(String value) {
        this.notebookPointingDeviceDescription = value;
    }

    /**
     * Gets the value of the numberOfDiscs property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfDiscs() {
        return numberOfDiscs;
    }

    /**
     * Sets the value of the numberOfDiscs property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfDiscs(BigInteger value) {
        this.numberOfDiscs = value;
    }

    /**
     * Gets the value of the numberOfIssues property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfIssues() {
        return numberOfIssues;
    }

    /**
     * Sets the value of the numberOfIssues property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfIssues(BigInteger value) {
        this.numberOfIssues = value;
    }

    /**
     * Gets the value of the numberOfItems property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfItems() {
        return numberOfItems;
    }

    /**
     * Sets the value of the numberOfItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfItems(BigInteger value) {
        this.numberOfItems = value;
    }

    /**
     * Gets the value of the numberOfKeys property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfKeys() {
        return numberOfKeys;
    }

    /**
     * Sets the value of the numberOfKeys property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfKeys(BigInteger value) {
        this.numberOfKeys = value;
    }

    /**
     * Gets the value of the numberOfPages property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the value of the numberOfPages property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfPages(BigInteger value) {
        this.numberOfPages = value;
    }

    /**
     * Gets the value of the numberOfPearls property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfPearls() {
        return numberOfPearls;
    }

    /**
     * Sets the value of the numberOfPearls property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfPearls(BigInteger value) {
        this.numberOfPearls = value;
    }

    /**
     * Gets the value of the numberOfRapidFireShots property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfRapidFireShots() {
        return numberOfRapidFireShots;
    }

    /**
     * Sets the value of the numberOfRapidFireShots property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfRapidFireShots(BigInteger value) {
        this.numberOfRapidFireShots = value;
    }

    /**
     * Gets the value of the numberOfStones property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfStones() {
        return numberOfStones;
    }

    /**
     * Sets the value of the numberOfStones property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfStones(BigInteger value) {
        this.numberOfStones = value;
    }

    /**
     * Gets the value of the numberOfStrings property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfStrings() {
        return numberOfStrings;
    }

    /**
     * Sets the value of the numberOfStrings property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfStrings(BigInteger value) {
        this.numberOfStrings = value;
    }

    /**
     * Gets the value of the numberOfTracks property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumberOfTracks() {
        return numberOfTracks;
    }

    /**
     * Sets the value of the numberOfTracks property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumberOfTracks(BigInteger value) {
        this.numberOfTracks = value;
    }

    /**
     * Gets the value of the opticalZoom property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getOpticalZoom() {
        return opticalZoom;
    }

    /**
     * Sets the value of the opticalZoom property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setOpticalZoom(DecimalWithUnits value) {
        this.opticalZoom = value;
    }

    /**
     * Gets the value of the originalReleaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalReleaseDate() {
        return originalReleaseDate;
    }

    /**
     * Sets the value of the originalReleaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalReleaseDate(String value) {
        this.originalReleaseDate = value;
    }

    /**
     * Gets the value of the outputWattage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getOutputWattage() {
        return outputWattage;
    }

    /**
     * Sets the value of the outputWattage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setOutputWattage(BigInteger value) {
        this.outputWattage = value;
    }

    /**
     * Gets the value of the packageDimensions property.
     * 
     * @return
     *     possible object is
     *     {@link MerchantItemAttributes.PackageDimensions }
     *     
     */
    public MerchantItemAttributes.PackageDimensions getPackageDimensions() {
        return packageDimensions;
    }

    /**
     * Sets the value of the packageDimensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link MerchantItemAttributes.PackageDimensions }
     *     
     */
    public void setPackageDimensions(MerchantItemAttributes.PackageDimensions value) {
        this.packageDimensions = value;
    }

    /**
     * Gets the value of the pearlLustre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlLustre() {
        return pearlLustre;
    }

    /**
     * Sets the value of the pearlLustre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlLustre(String value) {
        this.pearlLustre = value;
    }

    /**
     * Gets the value of the pearlMinimumColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlMinimumColor() {
        return pearlMinimumColor;
    }

    /**
     * Sets the value of the pearlMinimumColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlMinimumColor(String value) {
        this.pearlMinimumColor = value;
    }

    /**
     * Gets the value of the pearlShape property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlShape() {
        return pearlShape;
    }

    /**
     * Sets the value of the pearlShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlShape(String value) {
        this.pearlShape = value;
    }

    /**
     * Gets the value of the pearlStringingMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlStringingMethod() {
        return pearlStringingMethod;
    }

    /**
     * Sets the value of the pearlStringingMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlStringingMethod(String value) {
        this.pearlStringingMethod = value;
    }

    /**
     * Gets the value of the pearlSurfaceBlemishes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlSurfaceBlemishes() {
        return pearlSurfaceBlemishes;
    }

    /**
     * Sets the value of the pearlSurfaceBlemishes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlSurfaceBlemishes(String value) {
        this.pearlSurfaceBlemishes = value;
    }

    /**
     * Gets the value of the pearlType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlType() {
        return pearlType;
    }

    /**
     * Sets the value of the pearlType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlType(String value) {
        this.pearlType = value;
    }

    /**
     * Gets the value of the pearlUniformity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPearlUniformity() {
        return pearlUniformity;
    }

    /**
     * Sets the value of the pearlUniformity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPearlUniformity(String value) {
        this.pearlUniformity = value;
    }

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    /**
     * Gets the value of the photoFlashType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the photoFlashType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhotoFlashType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPhotoFlashType() {
        if (photoFlashType == null) {
            photoFlashType = new ArrayList<String>();
        }
        return this.photoFlashType;
    }

    /**
     * Gets the value of the pictureFormat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pictureFormat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPictureFormat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPictureFormat() {
        if (pictureFormat == null) {
            pictureFormat = new ArrayList<String>();
        }
        return this.pictureFormat;
    }

    /**
     * Gets the value of the platform property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the platform property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlatform().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPlatform() {
        if (platform == null) {
            platform = new ArrayList<String>();
        }
        return this.platform;
    }

    /**
     * Gets the value of the priceRating property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriceRating() {
        return priceRating;
    }

    /**
     * Sets the value of the priceRating property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriceRating(BigInteger value) {
        this.priceRating = value;
    }

    /**
     * Gets the value of the processorCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getProcessorCount() {
        return processorCount;
    }

    /**
     * Sets the value of the processorCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setProcessorCount(BigInteger value) {
        this.processorCount = value;
    }

    /**
     * Gets the value of the productGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductGroup() {
        return productGroup;
    }

    /**
     * Sets the value of the productGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductGroup(String value) {
        this.productGroup = value;
    }

    /**
     * Gets the value of the promotionalTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionalTag() {
        return promotionalTag;
    }

    /**
     * Sets the value of the promotionalTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionalTag(String value) {
        this.promotionalTag = value;
    }

    /**
     * Gets the value of the poBoxShippingExcluded property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPOBoxShippingExcluded() {
        return poBoxShippingExcluded;
    }

    /**
     * Sets the value of the poBoxShippingExcluded property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPOBoxShippingExcluded(String value) {
        this.poBoxShippingExcluded = value;
    }

    /**
     * Gets the value of the publicationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the value of the publicationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicationDate(String value) {
        this.publicationDate = value;
    }

    /**
     * Gets the value of the publisher property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the value of the publisher property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublisher(String value) {
        this.publisher = value;
    }

    /**
     * Gets the value of the purchasingChannel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the purchasingChannel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPurchasingChannel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPurchasingChannel() {
        if (purchasingChannel == null) {
            purchasingChannel = new ArrayList<String>();
        }
        return this.purchasingChannel;
    }

    /**
     * Gets the value of the readingLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReadingLevel() {
        return readingLevel;
    }

    /**
     * Sets the value of the readingLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReadingLevel(String value) {
        this.readingLevel = value;
    }

    /**
     * Gets the value of the recorderTrackCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRecorderTrackCount() {
        return recorderTrackCount;
    }

    /**
     * Sets the value of the recorderTrackCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRecorderTrackCount(BigInteger value) {
        this.recorderTrackCount = value;
    }

    /**
     * Gets the value of the regionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * Sets the value of the regionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionCode(String value) {
        this.regionCode = value;
    }

    /**
     * Gets the value of the regionOfOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionOfOrigin() {
        return regionOfOrigin;
    }

    /**
     * Sets the value of the regionOfOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionOfOrigin(String value) {
        this.regionOfOrigin = value;
    }

    /**
     * Gets the value of the releaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the value of the releaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseDate(String value) {
        this.releaseDate = value;
    }

    /**
     * Gets the value of the returnMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returnMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReturnMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReturnMethod() {
        if (returnMethod == null) {
            returnMethod = new ArrayList<String>();
        }
        return this.returnMethod;
    }

    /**
     * Gets the value of the removableMemory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemovableMemory() {
        return removableMemory;
    }

    /**
     * Sets the value of the removableMemory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemovableMemory(String value) {
        this.removableMemory = value;
    }

    /**
     * Gets the value of the resolutionModes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResolutionModes() {
        return resolutionModes;
    }

    /**
     * Sets the value of the resolutionModes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResolutionModes(String value) {
        this.resolutionModes = value;
    }

    /**
     * Gets the value of the returnPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnPolicy() {
        return returnPolicy;
    }

    /**
     * Sets the value of the returnPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnPolicy(String value) {
        this.returnPolicy = value;
    }

    /**
     * Gets the value of the ringSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRingSize() {
        return ringSize;
    }

    /**
     * Sets the value of the ringSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRingSize(String value) {
        this.ringSize = value;
    }

    /**
     * Gets the value of the safetyWarning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSafetyWarning() {
        return safetyWarning;
    }

    /**
     * Sets the value of the safetyWarning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSafetyWarning(String value) {
        this.safetyWarning = value;
    }

    /**
     * Gets the value of the salesRestriction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesRestriction() {
        return salesRestriction;
    }

    /**
     * Sets the value of the salesRestriction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesRestriction(String value) {
        this.salesRestriction = value;
    }

    /**
     * Gets the value of the secondaryCacheSize property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getSecondaryCacheSize() {
        return secondaryCacheSize;
    }

    /**
     * Sets the value of the secondaryCacheSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setSecondaryCacheSize(NonNegativeIntegerWithUnits value) {
        this.secondaryCacheSize = value;
    }

    /**
     * Gets the value of the settingType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettingType() {
        return settingType;
    }

    /**
     * Sets the value of the settingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettingType(String value) {
        this.settingType = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Gets the value of the sku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSKU() {
        return sku;
    }

    /**
     * Sets the value of the sku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSKU(String value) {
        this.sku = value;
    }

    /**
     * Gets the value of the soldInStores property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoldInStores() {
        return soldInStores;
    }

    /**
     * Sets the value of the soldInStores property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoldInStores(String value) {
        this.soldInStores = value;
    }

    /**
     * Gets the value of the sizePerPearl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSizePerPearl() {
        return sizePerPearl;
    }

    /**
     * Sets the value of the sizePerPearl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSizePerPearl(String value) {
        this.sizePerPearl = value;
    }

    /**
     * Gets the value of the skillLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkillLevel() {
        return skillLevel;
    }

    /**
     * Sets the value of the skillLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkillLevel(String value) {
        this.skillLevel = value;
    }

    /**
     * Gets the value of the soundCardDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSoundCardDescription() {
        return soundCardDescription;
    }

    /**
     * Sets the value of the soundCardDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSoundCardDescription(String value) {
        this.soundCardDescription = value;
    }

    /**
     * Gets the value of the speakerCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSpeakerCount() {
        return speakerCount;
    }

    /**
     * Sets the value of the speakerCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSpeakerCount(BigInteger value) {
        this.speakerCount = value;
    }

    /**
     * Gets the value of the speakerDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeakerDescription() {
        return speakerDescription;
    }

    /**
     * Sets the value of the speakerDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeakerDescription(String value) {
        this.speakerDescription = value;
    }

    /**
     * Gets the value of the specialFeatures property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialFeatures property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialFeatures().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSpecialFeatures() {
        if (specialFeatures == null) {
            specialFeatures = new ArrayList<String>();
        }
        return this.specialFeatures;
    }

    /**
     * Gets the value of the stoneClarity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoneClarity() {
        return stoneClarity;
    }

    /**
     * Sets the value of the stoneClarity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoneClarity(String value) {
        this.stoneClarity = value;
    }

    /**
     * Gets the value of the stoneColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoneColor() {
        return stoneColor;
    }

    /**
     * Sets the value of the stoneColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoneColor(String value) {
        this.stoneColor = value;
    }

    /**
     * Gets the value of the stoneCut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoneCut() {
        return stoneCut;
    }

    /**
     * Sets the value of the stoneCut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoneCut(String value) {
        this.stoneCut = value;
    }

    /**
     * Gets the value of the stoneShape property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoneShape() {
        return stoneShape;
    }

    /**
     * Sets the value of the stoneShape property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoneShape(String value) {
        this.stoneShape = value;
    }

    /**
     * Gets the value of the stoneWeight property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getStoneWeight() {
        return stoneWeight;
    }

    /**
     * Sets the value of the stoneWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setStoneWeight(DecimalWithUnits value) {
        this.stoneWeight = value;
    }

    /**
     * Gets the value of the studio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudio() {
        return studio;
    }

    /**
     * Sets the value of the studio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudio(String value) {
        this.studio = value;
    }

    /**
     * Gets the value of the subscriptionLength property.
     * 
     * @return
     *     possible object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public NonNegativeIntegerWithUnits getSubscriptionLength() {
        return subscriptionLength;
    }

    /**
     * Sets the value of the subscriptionLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonNegativeIntegerWithUnits }
     *     
     */
    public void setSubscriptionLength(NonNegativeIntegerWithUnits value) {
        this.subscriptionLength = value;
    }

    /**
     * Gets the value of the supportedImageType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supportedImageType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupportedImageType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSupportedImageType() {
        if (supportedImageType == null) {
            supportedImageType = new ArrayList<String>();
        }
        return this.supportedImageType;
    }

    /**
     * Gets the value of the systemBusSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getSystemBusSpeed() {
        return systemBusSpeed;
    }

    /**
     * Sets the value of the systemBusSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setSystemBusSpeed(DecimalWithUnits value) {
        this.systemBusSpeed = value;
    }

    /**
     * Gets the value of the systemMemorySizeMax property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getSystemMemorySizeMax() {
        return systemMemorySizeMax;
    }

    /**
     * Sets the value of the systemMemorySizeMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setSystemMemorySizeMax(DecimalWithUnits value) {
        this.systemMemorySizeMax = value;
    }

    /**
     * Gets the value of the systemMemorySize property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getSystemMemorySize() {
        return systemMemorySize;
    }

    /**
     * Sets the value of the systemMemorySize property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setSystemMemorySize(DecimalWithUnits value) {
        this.systemMemorySize = value;
    }

    /**
     * Gets the value of the systemMemoryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemMemoryType() {
        return systemMemoryType;
    }

    /**
     * Sets the value of the systemMemoryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemMemoryType(String value) {
        this.systemMemoryType = value;
    }

    /**
     * Gets the value of the tellingPageIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTellingPageIndicator() {
        return tellingPageIndicator;
    }

    /**
     * Sets the value of the tellingPageIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTellingPageIndicator(String value) {
        this.tellingPageIndicator = value;
    }

    /**
     * Gets the value of the theatricalReleaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTheatricalReleaseDate() {
        return theatricalReleaseDate;
    }

    /**
     * Sets the value of the theatricalReleaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTheatricalReleaseDate(String value) {
        this.theatricalReleaseDate = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the totalDiamondWeight property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getTotalDiamondWeight() {
        return totalDiamondWeight;
    }

    /**
     * Sets the value of the totalDiamondWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setTotalDiamondWeight(DecimalWithUnits value) {
        this.totalDiamondWeight = value;
    }

    /**
     * Gets the value of the totalExternalBaysFree property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalExternalBaysFree() {
        return totalExternalBaysFree;
    }

    /**
     * Sets the value of the totalExternalBaysFree property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalExternalBaysFree(BigInteger value) {
        this.totalExternalBaysFree = value;
    }

    /**
     * Gets the value of the totalFirewirePorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalFirewirePorts() {
        return totalFirewirePorts;
    }

    /**
     * Sets the value of the totalFirewirePorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalFirewirePorts(BigInteger value) {
        this.totalFirewirePorts = value;
    }

    /**
     * Gets the value of the totalGemWeight property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getTotalGemWeight() {
        return totalGemWeight;
    }

    /**
     * Sets the value of the totalGemWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setTotalGemWeight(DecimalWithUnits value) {
        this.totalGemWeight = value;
    }

    /**
     * Gets the value of the totalInternalBaysFree property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalInternalBaysFree() {
        return totalInternalBaysFree;
    }

    /**
     * Sets the value of the totalInternalBaysFree property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalInternalBaysFree(BigInteger value) {
        this.totalInternalBaysFree = value;
    }

    /**
     * Gets the value of the totalMetalWeight property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getTotalMetalWeight() {
        return totalMetalWeight;
    }

    /**
     * Sets the value of the totalMetalWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setTotalMetalWeight(DecimalWithUnits value) {
        this.totalMetalWeight = value;
    }

    /**
     * Gets the value of the totalNTSCPALPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalNTSCPALPorts() {
        return totalNTSCPALPorts;
    }

    /**
     * Sets the value of the totalNTSCPALPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalNTSCPALPorts(BigInteger value) {
        this.totalNTSCPALPorts = value;
    }

    /**
     * Gets the value of the totalParallelPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalParallelPorts() {
        return totalParallelPorts;
    }

    /**
     * Sets the value of the totalParallelPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalParallelPorts(BigInteger value) {
        this.totalParallelPorts = value;
    }

    /**
     * Gets the value of the totalPCCardSlots property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalPCCardSlots() {
        return totalPCCardSlots;
    }

    /**
     * Sets the value of the totalPCCardSlots property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalPCCardSlots(BigInteger value) {
        this.totalPCCardSlots = value;
    }

    /**
     * Gets the value of the totalPCISlotsFree property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalPCISlotsFree() {
        return totalPCISlotsFree;
    }

    /**
     * Sets the value of the totalPCISlotsFree property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalPCISlotsFree(BigInteger value) {
        this.totalPCISlotsFree = value;
    }

    /**
     * Gets the value of the totalSerialPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalSerialPorts() {
        return totalSerialPorts;
    }

    /**
     * Sets the value of the totalSerialPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalSerialPorts(BigInteger value) {
        this.totalSerialPorts = value;
    }

    /**
     * Gets the value of the totalSVideoOutPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalSVideoOutPorts() {
        return totalSVideoOutPorts;
    }

    /**
     * Sets the value of the totalSVideoOutPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalSVideoOutPorts(BigInteger value) {
        this.totalSVideoOutPorts = value;
    }

    /**
     * Gets the value of the totalUSB2Ports property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalUSB2Ports() {
        return totalUSB2Ports;
    }

    /**
     * Sets the value of the totalUSB2Ports property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalUSB2Ports(BigInteger value) {
        this.totalUSB2Ports = value;
    }

    /**
     * Gets the value of the totalUSBPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalUSBPorts() {
        return totalUSBPorts;
    }

    /**
     * Sets the value of the totalUSBPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalUSBPorts(BigInteger value) {
        this.totalUSBPorts = value;
    }

    /**
     * Gets the value of the totalVGAOutPorts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalVGAOutPorts() {
        return totalVGAOutPorts;
    }

    /**
     * Sets the value of the totalVGAOutPorts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalVGAOutPorts(BigInteger value) {
        this.totalVGAOutPorts = value;
    }

    /**
     * Gets the value of the upc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUPC() {
        return upc;
    }

    /**
     * Sets the value of the upc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUPC(String value) {
        this.upc = value;
    }

    /**
     * Gets the value of the variationDenomination property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariationDenomination() {
        return variationDenomination;
    }

    /**
     * Sets the value of the variationDenomination property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariationDenomination(String value) {
        this.variationDenomination = value;
    }

    /**
     * Gets the value of the variationDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariationDescription() {
        return variationDescription;
    }

    /**
     * Sets the value of the variationDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariationDescription(String value) {
        this.variationDescription = value;
    }

    /**
     * Gets the value of the vendorRebate property.
     * 
     * @return
     *     possible object is
     *     {@link MerchantItemAttributes.VendorRebate }
     *     
     */
    public MerchantItemAttributes.VendorRebate getVendorRebate() {
        return vendorRebate;
    }

    /**
     * Sets the value of the vendorRebate property.
     * 
     * @param value
     *     allowed object is
     *     {@link MerchantItemAttributes.VendorRebate }
     *     
     */
    public void setVendorRebate(MerchantItemAttributes.VendorRebate value) {
        this.vendorRebate = value;
    }

    /**
     * Gets the value of the warranty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWarranty() {
        return warranty;
    }

    /**
     * Sets the value of the warranty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWarranty(String value) {
        this.warranty = value;
    }

    /**
     * Gets the value of the watchMovementType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWatchMovementType() {
        return watchMovementType;
    }

    /**
     * Sets the value of the watchMovementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWatchMovementType(String value) {
        this.watchMovementType = value;
    }

    /**
     * Gets the value of the websiteBuyability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsiteBuyability() {
        return websiteBuyability;
    }

    /**
     * Sets the value of the websiteBuyability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsiteBuyability(String value) {
        this.websiteBuyability = value;
    }

    /**
     * Gets the value of the waterResistanceDepth property.
     * 
     * @return
     *     possible object is
     *     {@link DecimalWithUnits }
     *     
     */
    public DecimalWithUnits getWaterResistanceDepth() {
        return waterResistanceDepth;
    }

    /**
     * Sets the value of the waterResistanceDepth property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecimalWithUnits }
     *     
     */
    public void setWaterResistanceDepth(DecimalWithUnits value) {
        this.waterResistanceDepth = value;
    }

    /**
     * Gets the value of the wirelessMicrophoneFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWirelessMicrophoneFrequency() {
        return wirelessMicrophoneFrequency;
    }

    /**
     * Sets the value of the wirelessMicrophoneFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWirelessMicrophoneFrequency(BigInteger value) {
        this.wirelessMicrophoneFrequency = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="Role" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Creator {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "Role", required = true)
        protected String role;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the role property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRole() {
            return role;
        }

        /**
         * Sets the value of the role property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRole(String value) {
            this.role = value;
        }

    }


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
     *         &lt;element name="Height" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
     *         &lt;element name="Length" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
     *         &lt;element name="Weight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
     *         &lt;element name="Width" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
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
        "height",
        "length",
        "weight",
        "width"
    })
    public static class ItemDimensions {

        @XmlElement(name = "Height")
        protected DecimalWithUnits height;
        @XmlElement(name = "Length")
        protected DecimalWithUnits length;
        @XmlElement(name = "Weight")
        protected DecimalWithUnits weight;
        @XmlElement(name = "Width")
        protected DecimalWithUnits width;

        /**
         * Gets the value of the height property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getHeight() {
            return height;
        }

        /**
         * Sets the value of the height property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setHeight(DecimalWithUnits value) {
            this.height = value;
        }

        /**
         * Gets the value of the length property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getLength() {
            return length;
        }

        /**
         * Sets the value of the length property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setLength(DecimalWithUnits value) {
            this.length = value;
        }

        /**
         * Gets the value of the weight property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getWeight() {
            return weight;
        }

        /**
         * Sets the value of the weight property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setWeight(DecimalWithUnits value) {
            this.weight = value;
        }

        /**
         * Gets the value of the width property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getWidth() {
            return width;
        }

        /**
         * Sets the value of the width property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setWidth(DecimalWithUnits value) {
            this.width = value;
        }

    }


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
     *         &lt;element name="Language" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="AudioFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "language"
    })
    public static class Languages {

        @XmlElement(name = "Language")
        protected List<MerchantItemAttributes.Languages.Language> language;

        /**
         * Gets the value of the language property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the language property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLanguage().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MerchantItemAttributes.Languages.Language }
         * 
         * 
         */
        public List<MerchantItemAttributes.Languages.Language> getLanguage() {
            if (language == null) {
                language = new ArrayList<MerchantItemAttributes.Languages.Language>();
            }
            return this.language;
        }


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
         *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="AudioFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "name",
            "type",
            "audioFormat"
        })
        public static class Language {

            @XmlElement(name = "Name", required = true)
            protected String name;
            @XmlElement(name = "Type", required = true)
            protected String type;
            @XmlElement(name = "AudioFormat")
            protected String audioFormat;

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the audioFormat property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAudioFormat() {
                return audioFormat;
            }

            /**
             * Sets the value of the audioFormat property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAudioFormat(String value) {
                this.audioFormat = value;
            }

        }

    }


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
     *         &lt;element name="Height" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
     *         &lt;element name="Length" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
     *         &lt;element name="Weight" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
     *         &lt;element name="Width" type="{http://webservices.amazon.com/AWSECommerceService/2010-11-01}DecimalWithUnits" minOccurs="0"/>
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
        "height",
        "length",
        "weight",
        "width"
    })
    public static class PackageDimensions {

        @XmlElement(name = "Height")
        protected DecimalWithUnits height;
        @XmlElement(name = "Length")
        protected DecimalWithUnits length;
        @XmlElement(name = "Weight")
        protected DecimalWithUnits weight;
        @XmlElement(name = "Width")
        protected DecimalWithUnits width;

        /**
         * Gets the value of the height property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getHeight() {
            return height;
        }

        /**
         * Sets the value of the height property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setHeight(DecimalWithUnits value) {
            this.height = value;
        }

        /**
         * Gets the value of the length property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getLength() {
            return length;
        }

        /**
         * Sets the value of the length property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setLength(DecimalWithUnits value) {
            this.length = value;
        }

        /**
         * Gets the value of the weight property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getWeight() {
            return weight;
        }

        /**
         * Sets the value of the weight property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setWeight(DecimalWithUnits value) {
            this.weight = value;
        }

        /**
         * Gets the value of the width property.
         * 
         * @return
         *     possible object is
         *     {@link DecimalWithUnits }
         *     
         */
        public DecimalWithUnits getWidth() {
            return width;
        }

        /**
         * Sets the value of the width property.
         * 
         * @param value
         *     allowed object is
         *     {@link DecimalWithUnits }
         *     
         */
        public void setWidth(DecimalWithUnits value) {
            this.width = value;
        }

    }


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
     *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="StartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "type",
        "startDate",
        "endDate"
    })
    public static class VendorRebate {

        @XmlElement(name = "Type")
        protected String type;
        @XmlElement(name = "StartDate")
        protected String startDate;
        @XmlElement(name = "EndDate")
        protected String endDate;

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the startDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStartDate() {
            return startDate;
        }

        /**
         * Sets the value of the startDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStartDate(String value) {
            this.startDate = value;
        }

        /**
         * Gets the value of the endDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEndDate() {
            return endDate;
        }

        /**
         * Sets the value of the endDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEndDate(String value) {
            this.endDate = value;
        }

    }

}
