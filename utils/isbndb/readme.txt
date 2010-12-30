

To generate the xsd file
------------------------

1. Download xml to xsd generator tool. Instructions available here at
	http://www.javarants.com/2006/04/30/simple-and-efficient-xml-parsing-using-jaxb-2-0/

2. Goto the folder in command line.

3. to generate the xsd, run the command below:

> java -jar trang.jar -I xml -O xsd response.xml response.xsd


To generate the java files
--------------------------

1. Run the following command next to create the java objects

	xjc -d src -p com.isbndb.beans response.xsd
	
2. Optionally, you can next create the jar file with the following command: 
	
	jar cvf isbndb-beans.jar -C src/ .
	

	