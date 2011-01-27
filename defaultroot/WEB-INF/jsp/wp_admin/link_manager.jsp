<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_link_manager_block1}" escapeXml="false" />
<div class="wrap">

<form id="posts-filter" action="" method="get">
<h2><c:out value="${__wp_admin_link_manager_block2}" escapeXml="false" /></h2>

<p id="post-search">
	<input type="text" id="post-search-input" name="s" value="<c:out value="${__wp_admin_link_manager_block3}" escapeXml="false" />" />
	<input type="submit" value="<c:out value="${__wp_admin_link_manager_block4}" escapeXml="false" />" class="button" />
</p>

<br class="clear" />

<div class="tablenav">

<div class="alignleft">
<input type="submit" value="<c:out value="${__wp_admin_link_manager_block5}" escapeXml="false" />" name="deleteit" class="button-secondary delete" />
<c:out value="${__wp_admin_link_manager_block6}" escapeXml="false" /><input type="submit" id="post-query-submit" value="<c:out value="${__wp_admin_link_manager_block7}" escapeXml="false" />" class="button-secondary" />

</div>

<br class="clear" />
</div>

<br class="clear" />

<c:out value="${__wp_admin_link_manager_block8}" escapeXml="false" />
<c:out value="${__wp_admin_link_manager_block9}" escapeXml="false" /></form>

<div id="ajax-response"></div>

<div class="tablenav">
<br class="clear" />
</div>


</div>

<c:out value="${__wp_admin_link_manager_block10}" escapeXml="false" />