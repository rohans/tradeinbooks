<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_privacy_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_options_privacy_block2}" escapeXml="false" /></h2>
<form method="post" action="options.php">
<c:out value="${__wp_admin_options_privacy_block3}" escapeXml="false" /><table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_privacy_block4}" escapeXml="false" /> </th>
<td>
<p><input id="blog-public" type="radio" name="blog_public" value="1" <c:out value="${__wp_admin_options_privacy_block5}" escapeXml="false" /> />
<label for="blog-public"><c:out value="${__wp_admin_options_privacy_block6}" escapeXml="false" /></label></p>
<p><input id="blog-norobots" type="radio" name="blog_public" value="0" <c:out value="${__wp_admin_options_privacy_block7}" escapeXml="false" /> />
<label for="blog-norobots"><c:out value="${__wp_admin_options_privacy_block8}" escapeXml="false" /></label></p>
<c:out value="${__wp_admin_options_privacy_block9}" escapeXml="false" /></td>
</tr>
</table>

<p class="submit"><input type="submit" name="Submit" value="<c:out value="${__wp_admin_options_privacy_block10}" escapeXml="false" />" />
<input type="hidden" name="action" value="update" />
<input type="hidden" name="page_options" value="blog_public" />
</p>
</form>

</div>

<c:out value="${__wp_admin_options_privacy_block11}" escapeXml="false" />