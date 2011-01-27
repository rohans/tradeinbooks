<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_export_block1}" escapeXml="false" />
<c:if test="${EXIT_INVOKED!='true'}">
	<div class="wrap">
	<h2><c:out value="${__wp_admin_export_block2}" escapeXml="false" /></h2>
	<p><c:out value="${__wp_admin_export_block3}" escapeXml="false" /></p>
	<p><c:out value="${__wp_admin_export_block4}" escapeXml="false" /></p>
	<p><c:out value="${__wp_admin_export_block5}" escapeXml="false" /></p>
	<form action="" method="get">
	<h3><c:out value="${__wp_admin_export_block6}" escapeXml="false" /></h3>
	
	<table class="form-table">
	<tr>
	<th><c:out value="${__wp_admin_export_block7}" escapeXml="false" /></th>
	<td>
	<select name="author">
	<option value="all" selected="selected"><c:out value="${__wp_admin_export_block8}" escapeXml="false" /></option>
	<c:out value="${__wp_admin_export_block9}" escapeXml="false" /></select>
	</td>
	</tr>
	</table>
	<p class="submit"><input type="submit" name="submit" value="<c:out value="${__wp_admin_export_block10}" escapeXml="false" />" />
	<input type="hidden" name="download" value="true" />
	</p>
	</form>
	</div>
	
	<c:out value="${__wp_admin_export_block11}" escapeXml="false" />
</c:if>