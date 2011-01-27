<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_comments_block1}" escapeXml="false" /><div class="wrap">
<form id="posts-filter" action="" method="get">
<h2><c:out value="${__wp_admin_edit_comments_block2}" escapeXml="false" /></h2>

<ul class="subsubsub">
<c:out value="${__wp_admin_edit_comments_block3}" escapeXml="false" /></ul>

<p id="post-search">
	<input type="text" id="post-search-input" name="s" value="<c:out value="${__wp_admin_edit_comments_block4}" escapeXml="false" />" />
	<input type="submit" value="<c:out value="${__wp_admin_edit_comments_block5}" escapeXml="false" />" class="button" />
</p>

<input type="hidden" name="mode" value="<c:out value="${__wp_admin_edit_comments_block6}" escapeXml="false" />" />
<input type="hidden" name="comment_status" value="<c:out value="${__wp_admin_edit_comments_block7}" escapeXml="false" />" />
</form>

<ul class="view-switch">
	<li <c:out value="${__wp_admin_edit_comments_block8}" escapeXml="false" />><a href="<c:out value="${__wp_admin_edit_comments_block9}" escapeXml="false" />"><c:out value="${__wp_admin_edit_comments_block10}" escapeXml="false" /></a></li>
	<li <c:out value="${__wp_admin_edit_comments_block11}" escapeXml="false" />><a href="<c:out value="${__wp_admin_edit_comments_block12}" escapeXml="false" />"><c:out value="${__wp_admin_edit_comments_block13}" escapeXml="false" /></a></li>
</ul>

<c:out value="${__wp_admin_edit_comments_block14}" escapeXml="false" />
<form id="comments-form" action="" method="post">

<div class="tablenav">

<c:out value="${__wp_admin_edit_comments_block15}" escapeXml="false" />
<div class="alignleft">
<c:out value="${__wp_admin_edit_comments_block16}" escapeXml="false" /><input type="submit" value="<c:out value="${__wp_admin_edit_comments_block17}" escapeXml="false" />" name="spamit" class="button-secondary" />
<c:out value="${__wp_admin_edit_comments_block18}" escapeXml="false" /><input type="submit" value="<c:out value="${__wp_admin_edit_comments_block19}" escapeXml="false" />" name="deleteit" class="button-secondary delete" />
<c:out value="${__wp_admin_edit_comments_block20}" escapeXml="false" /></div>

<br class="clear" />

</div>

<br class="clear" />
<c:out value="${__wp_admin_edit_comments_block21}" escapeXml="false" /><div class="tablenav">
<c:out value="${__wp_admin_edit_comments_block22}" escapeXml="false" /><br class="clear" />
</div>

</div>

<c:out value="${__wp_admin_edit_comments_block23}" escapeXml="false" />