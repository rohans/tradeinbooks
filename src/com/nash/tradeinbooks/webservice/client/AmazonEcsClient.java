/**
 * 
 */
package com.nash.tradeinbooks.webservice.client;

import java.util.List;

import com.ECS.client.jax.Item;
import com.ECS.client.jax.ItemLookupResponse;
import com.ECS.client.jax.Items;

/**
 * @author rohans
 * 
 */
public class AmazonEcsClient {

    /*
     * Your AWS Access Key ID, as taken from the AWS Your Account page.
     */
    private static final String AWS_ACCESS_KEY_ID = "AKIAJRYJMUHPBRBZDXSA";

    /*
     * Your AWS Secret Key corresponding to the above ID, as taken from the AWS
     * Your Account page.
     */
    private static final String AWS_SECRET_KEY = "aeig5hFXXmFS+sc9lqwAbdnMT4k3nhmSP3U/865O";
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Set the service:
		com.ECS.client.jax.AWSECommerceService service = new com.ECS.client.jax.AWSECommerceService();
		service.setHandlerResolver(new AwsHandlerResolver(AWS_SECRET_KEY));
		// Set the service port:
		com.ECS.client.jax.AWSECommerceServicePortType port = service
				.getAWSECommerceServicePort();

		// Get the operation object:
		com.ECS.client.jax.ItemSearchRequest itemRequest = new com.ECS.client.jax.ItemSearchRequest();

		// Fill in the request object:
		itemRequest.setSearchIndex("Books");
		itemRequest.setKeywords("harry potter");
		com.ECS.client.jax.ItemSearch ItemElement = new com.ECS.client.jax.ItemSearch();
		ItemElement.setAWSAccessKeyId(AWS_ACCESS_KEY_ID);
		ItemElement.getRequest().add(itemRequest);

		// Call the Web service operation and store the response
		// in the response object:
		com.ECS.client.jax.ItemSearchResponse response = port
				.itemSearch(ItemElement);

		// Fetch the response and print out the title
		if (response!=null) {
			List<Items> itemses = response.getItems();
			
			for ( Items items: itemses ) {
				List<Item> item = items.getItem();
				int i=0;
				for ( Item it: item ) {
		            String title = it.getItemAttributes().getTitle();
		            String isbn = it.getItemAttributes().getISBN();
		            System.out.println("Item " + i++ + " is titled '" + title + "', isbn = " + isbn);
				}

			}
		}


	}

}
