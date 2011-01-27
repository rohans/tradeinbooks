/**
 * 
 */
package com.nash.tradeinbooks.webservice.client;

import java.util.Iterator;
import java.util.List;

import com.isbndb.beans.*;

/**
 * @author Ashu
 *
 */
public class IsbnDbClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IsbnDbClient fetch = IsbnDbClient.getInstance();
	    
	    //q is defined in autocomplete.js #receiveData
	    String query = "index1=full&value1=Robin Hood";
	    fetch.setSearchString(query);
	    ISBNdb result = fetch.invoke();
	    
	    if(result == null){
	    	System.out.println("result is null");
	    	return;
	    }
	    BookList bookList = result.getBookList();
	    if(bookList == null){
	    	System.out.println("bookList is null");
	    	return;
	    }
	    List<BookData> bookData = bookList.getBookData();
	    if(bookData == null){
	    	System.out.println("bookData is null");
	    	return;
	    }
	    Iterator<BookData> bookDataIter = bookData.iterator();
	    if(bookDataIter == null){
	    	System.out.println("bookDataIter is null");
	    	return;
	    }
	    while(bookDataIter.hasNext()){
			BookData iter = bookDataIter.next();
			if(iter !=null){
				System.out.println( "Title: "+iter.getTitle() + " ISBN:" + iter.getIsbn13()
						+ " AUTHOR:" +iter.getAuthorsText());
			}
			else{
				System.out.println("no more bookData present");
			}
		}    					

	}
}