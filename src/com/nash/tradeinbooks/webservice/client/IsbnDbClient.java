package com.nash.tradeinbooks.webservice.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.isbndb.beans.ISBNdb;
import com.nash.tradeinbooks.webservice.util.XMLUnmarshaller;

public class IsbnDbClient {
	private static Logger log = Logger.getLogger(IsbnDbClient.class);

	private String searchString;
	private String accessKey = "ZFRWD3NS";//ZFRWD3NS, WDURB9OK, 32JWQAD5, P9WLCLCZ
	
	public String queryURL = "http://isbndb.com/api/books.xml?access_key="+accessKey + "&results=texts";

	private static IsbnDbClient instance;

	public static IsbnDbClient getInstance() {
		if (instance == null) {
			instance = new IsbnDbClient();
		}
		return instance;

	}

	private IsbnDbClient() {// making the class constructor private
	}

	public String parseSearchString(String query, String character) {
		String queryResult = this.queryURL + "&";
		StringTokenizer st = new StringTokenizer(query, character);

		List<String> keys = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			keys.add(st.nextToken().trim());
		}
		
		for (String key : keys) {
			queryResult += key + "+";
		}
		
		queryResult += " ";
		
		log.debug("query url : " + queryResult);
		
		return queryResult;
	}

	public ISBNdb invoke() {

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();

		// Create a method instance.
		GetMethod method = new GetMethod(parseSearchString(this.searchString, " "));

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));
		ISBNdb result = null;

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			byte[] responseBody = method.getResponseBody();

			// Deal with the response.
			// Use caution: ensure correct character encoding and is not binary
			// data
			String xmlData = new String(responseBody);

			log.debug(xmlData);
			
		    XMLUnmarshaller xu = new XMLUnmarshaller();  
		    //the root node is ISBNdb so the unmarshaller creates an object named ISBNdb with all the sub
		    //nodes as member variable(or objects)
		    result = xu.unmarshall(xmlData);

		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		
		return result;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}
