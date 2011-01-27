<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<form method="get" id="searchform" action="<c:out value="${__wp_content_themes__default_searchform_block1}" escapeXml="false" />/">
<div><input type="text" value="<c:out value="${__wp_content_themes__default_searchform_block2}" escapeXml="false" />" name="s" id="s" />
<input type="submit" id="searchsubmit" value="Search" />
</div>
</form>
