package com.nash.tradeinbooks.webservice.util;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.isbndb.beans.ISBNdb;

public class XMLUnmarshaller {
	public XMLUnmarshaller(){
		
	}
	public ISBNdb unmarshall(String xmlString){
		ISBNdb jaxbObject = null;//i added
		 try {
	           ByteArrayInputStream input = new ByteArrayInputStream (xmlString.getBytes()); 
	           JAXBContext jc = JAXBContext.newInstance("com.isbndb.beans"); 
	           Unmarshaller u = jc.createUnmarshaller(); 
	           jaxbObject = (ISBNdb)u.unmarshal(input); 
	           
	       } catch (JAXBException e) {
	           e.printStackTrace ();
	       }
	       return jaxbObject;//i added
	}
}
