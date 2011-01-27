<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.nash.tradeinbooks.webservice.client.IsbnDbClient"%>
<%@page import="com.isbndb.beans.*"%>
<%@page import="java.io.*"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="org.apache.log4j.Logger"%>

<%

	Logger log = Logger.getLogger("getData.jsp");


	//q is defined in autocomplete.js #receiveData
	String query = request.getParameter("q");

	if ( query == null || query.equals("") ) {
		out.println("{result: 'failure', detail: 'Query not set.'}");
		return;	
	}
	
	log.debug("Getting book info for query = " + query);
	IsbnDbClient fetch = IsbnDbClient.getInstance();
 
    fetch.setSearchString(query);
    ISBNdb result = fetch.invoke();
   
    Gson gson = new Gson();
    String GsonResult;
    
    if(result == null){
    	log.debug("result is null");
    	return;
    }
    BookList bookList = result.getBookList();
    
    if(bookList == null){
    	log.debug("bookList is null");
    	return;
    }
    List<BookData> bookData = bookList.getBookData();
    
    if(bookData == null){
    	log.debug("bookData is null");
    	return;
    }
	
	//gson.toJson automatically reads the collection API so no need to iterate.
    GsonResult = gson.toJson(bookData);
	System.out.println(GsonResult);
	out.println(GsonResult);

 %>