Issues and fixes:
----------------

1) 	Error: 
		Issue when accessing when index.php.
		Error displayed is RequestDispatcher in violation of SRV.8.2 and SRV.14.2.5.1
		
	Solution: 
		Add the following vm arguments to startup script
			-Dorg.apache.catalina.STRICT_SERVLET_COMPLIANCE=false
