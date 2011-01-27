<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_reading_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_options_reading_block2}" escapeXml="false" /></h2>
<form name="form1" method="post" action="options.php">
<c:out value="${__wp_admin_options_reading_block3}" escapeXml="false" /><table class="form-table">
<c:out value="${__wp_admin_options_reading_block4}" escapeXml="false" /><tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_reading_block5}" escapeXml="false" /></th>
<td>
<input name="posts_per_page" type="text" id="posts_per_page" value="<c:out value="${__wp_admin_options_reading_block6}" escapeXml="false" />" size="3" /> <c:out value="${__wp_admin_options_reading_block7}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_reading_block8}" escapeXml="false" /></th>
<td><input name="posts_per_rss" type="text" id="posts_per_rss" value="<c:out value="${__wp_admin_options_reading_block9}" escapeXml="false" />" size="3" /> <c:out value="${__wp_admin_options_reading_block10}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_reading_block11}" escapeXml="false" /> </th>
<td>
<p><label><input name="rss_use_excerpt"  type="radio" value="0" <c:out value="${__wp_admin_options_reading_block12}" escapeXml="false" />	/> <c:out value="${__wp_admin_options_reading_block13}" escapeXml="false" /></label><br />
<label><input name="rss_use_excerpt" type="radio" value="1" <c:out value="${__wp_admin_options_reading_block14}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_reading_block15}" escapeXml="false" /></label></p>
</td>
</tr>

<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_reading_block16}" escapeXml="false" /></th>
<td><input name="blog_charset" type="text" id="blog_charset" value="<c:out value="${__wp_admin_options_reading_block17}" escapeXml="false" />" size="20" class="code" /><br />
<c:out value="${__wp_admin_options_reading_block18}" escapeXml="false" /></td>
</tr>
</table>
<p class="submit">
<input type="hidden" name="action" value="update" />
<input type="hidden" name="page_options" value="posts_per_page,posts_per_rss,rss_use_excerpt,blog_charset,gzipcompression,show_on_front,page_on_front,page_for_posts" />
<input type="submit" name="Submit" value="<c:out value="${__wp_admin_options_reading_block19}" escapeXml="false" />" />
</p>
</form>
</div>
<c:out value="${__wp_admin_options_reading_block20}" escapeXml="false" />