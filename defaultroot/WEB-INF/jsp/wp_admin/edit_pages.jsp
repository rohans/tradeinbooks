<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_pages_block1}" escapeXml="false" /><div class="wrap">
<form id="posts-filter" action="" method="get">
<h2><c:out value="${__wp_admin_edit_pages_block2}" escapeXml="false" /></h2>

<ul class="subsubsub">
<c:out value="${__wp_admin_edit_pages_block3}" escapeXml="false" /></ul>

<c:out value="${__wp_admin_edit_pages_block4}" escapeXml="false" />
<p id="post-search">
	<input type="text" id="post-search-input" name="s" value="<c:out value="${__wp_admin_edit_pages_block5}" escapeXml="false" />" />
	<input type="submit" value="<c:out value="${__wp_admin_edit_pages_block6}" escapeXml="false" />" class="button" />
</p>

<div class="tablenav">

<div class="alignleft">
<input type="submit" value="<c:out value="${__wp_admin_edit_pages_block7}" escapeXml="false" />" name="deleteit" class="button-secondary delete" />
<c:out value="${__wp_admin_edit_pages_block8}" escapeXml="false" /></div>

<br class="clear" />
</div>

<br class="clear" />

<c:out value="${__wp_admin_edit_pages_block9}" escapeXml="false" />
<div class="tablenav">
<br class="clear" />
</div>

<c:out value="${__wp_admin_edit_pages_block10}" escapeXml="false" />
</div>

<c:out value="${__wp_admin_edit_pages_block11}" escapeXml="false" />