<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_link_categories_block1}" escapeXml="false" />
<div class="wrap">

<form id="posts-filter" action="" method="get">
<c:out value="${__wp_admin_edit_link_categories_block2}" escapeXml="false" />
<p id="post-search">
	<input type="text" id="post-search-input" name="s" value="<c:out value="${__wp_admin_edit_link_categories_block3}" escapeXml="false" />" />
	<input type="submit" value="<c:out value="${__wp_admin_edit_link_categories_block4}" escapeXml="false" />" class="button" />
</p>

<br class="clear" />

<div class="tablenav">

<c:out value="${__wp_admin_edit_link_categories_block5}" escapeXml="false" />
<div class="alignleft">
<input type="submit" value="<c:out value="${__wp_admin_edit_link_categories_block6}" escapeXml="false" />" name="deleteit" class="button-secondary delete" />
<c:out value="${__wp_admin_edit_link_categories_block7}" escapeXml="false" /></div>

<br class="clear" />
</div>

<br class="clear" />

<table class="widefat">
	<thead>
	<tr>
        <th scope="col" class="check-column"><input type="checkbox" onclick="checkAll(document.getElementById('posts-filter'));" /></th>
        <th scope="col"><c:out value="${__wp_admin_edit_link_categories_block8}" escapeXml="false" /></th>
        <th scope="col"><c:out value="${__wp_admin_edit_link_categories_block9}" escapeXml="false" /></th>
        <th scope="col" class="num" style="width: 90px;"><c:out value="${__wp_admin_edit_link_categories_block10}" escapeXml="false" /></th>
	</tr>
	</thead>
	<tbody id="the-list" class="list:link-cat">
<c:out value="${__wp_admin_edit_link_categories_block11}" escapeXml="false" />	</tbody>
</table>
</form>

<div class="tablenav">

<c:out value="${__wp_admin_edit_link_categories_block12}" escapeXml="false" /><br class="clear" />
</div>
<br class="clear" />

</div>

<c:out value="${__wp_admin_edit_link_categories_block13}" escapeXml="false" />
<c:out value="${__wp_admin_edit_link_categories_block14}" escapeXml="false" />