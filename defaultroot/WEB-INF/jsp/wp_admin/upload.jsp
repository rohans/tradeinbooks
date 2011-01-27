<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_upload_block1}" escapeXml="false" />
<div class="wrap">

<form id="posts-filter" action="" method="get">
<h2><c:out value="${__wp_admin_upload_block2}" escapeXml="false" /></h2>

<ul class="subsubsub">
<c:out value="${__wp_admin_upload_block3}" escapeXml="false" /></ul>

<c:out value="${__wp_admin_upload_block4}" escapeXml="false" />
<p id="post-search">
	<input type="text" id="post-search-input" name="s" value="<c:out value="${__wp_admin_upload_block5}" escapeXml="false" />" />
	<input type="submit" value="<c:out value="${__wp_admin_upload_block6}" escapeXml="false" />" class="button" />
</p>

<c:out value="${__wp_admin_upload_block7}" escapeXml="false" />
<div class="tablenav">

<c:out value="${__wp_admin_upload_block8}" escapeXml="false" />
<div class="alignleft">
<input type="submit" value="<c:out value="${__wp_admin_upload_block9}" escapeXml="false" />" name="deleteit" class="button-secondary delete" />
<c:out value="${__wp_admin_upload_block10}" escapeXml="false" />
</div>

<br class="clear" />
</div>

<br class="clear" />

<c:out value="${__wp_admin_upload_block11}" escapeXml="false" />
</form>

<div id="ajax-response"></div>

<div class="tablenav">

<c:out value="${__wp_admin_upload_block12}" escapeXml="false" />
</div>

<br class="clear" />

<c:out value="${__wp_admin_upload_block13}" escapeXml="false" />
</div>

<c:out value="${__wp_admin_upload_block14}" escapeXml="false" />