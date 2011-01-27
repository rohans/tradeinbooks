

<%
	String clientIP = request.getRemoteAddr();
	System.out.print("clientIP="+clientIP);
	String clientHostname = request.getRemoteHost();
	System.out.print(", clientHostname="+clientHostname);

	String price = request.getParameter("p");
	System.out.print(", price="+price);
	String title = request.getParameter("t");
	System.out.print(", title="+title);
	String isbn = request.getParameter("i");
	System.out.print(", isbn="+isbn);
	String author = request.getParameter("a");
	System.out.print(", author="+author);
	String publisher = request.getParameter("pb");
	System.out.println(", publisher="+publisher);
	
	out.println("{'result':'success', 'postid':'1234'}");

%>