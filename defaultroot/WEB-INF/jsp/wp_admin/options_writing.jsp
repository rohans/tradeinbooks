<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_writing_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_options_writing_block2}" escapeXml="false" /></h2>
<form method="post" action="options.php">
<c:out value="${__wp_admin_options_writing_block3}" escapeXml="false" />
<table class="form-table">
<tr valign="top">
<th scope="row"> <c:out value="${__wp_admin_options_writing_block4}" escapeXml="false" /></th>
<td><input name="default_post_edit_rows" type="text" id="default_post_edit_rows" value="<c:out value="${__wp_admin_options_writing_block5}" escapeXml="false" />" size="2" style="width: 1.5em;" />
<c:out value="${__wp_admin_options_writing_block6}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block7}" escapeXml="false" /></th>
<td>
<label for="use_smilies">
<input name="use_smilies" type="checkbox" id="use_smilies" value="1" <c:out value="${__wp_admin_options_writing_block8}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_writing_block9}" escapeXml="false" /></label><br />
<label for="use_balanceTags"><input name="use_balanceTags" type="checkbox" id="use_balanceTags" value="1" <c:out value="${__wp_admin_options_writing_block10}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_writing_block11}" escapeXml="false" /></label>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block12}" escapeXml="false" /></th>
<td><select name="default_category" id="default_category">
<c:out value="${__wp_admin_options_writing_block13}" escapeXml="false" /></select></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block14}" escapeXml="false" /></th>
<td><select name="default_link_category" id="default_link_category">
<c:out value="${__wp_admin_options_writing_block15}" escapeXml="false" /></select></td>
</tr>
</table>

<h3><c:out value="${__wp_admin_options_writing_block16}" escapeXml="false" /></h3>
<p><c:out value="${__wp_admin_options_writing_block17}" escapeXml="false" /></p>

<table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block18}" escapeXml="false" /></th>
<td><input name="mailserver_url" type="text" id="mailserver_url" value="<c:out value="${__wp_admin_options_writing_block19}" escapeXml="false" />" size="40" />
<label for="mailserver_port"><c:out value="${__wp_admin_options_writing_block20}" escapeXml="false" /></label>
<input name="mailserver_port" type="text" id="mailserver_port" value="<c:out value="${__wp_admin_options_writing_block21}" escapeXml="false" />" size="6" />
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block22}" escapeXml="false" /></th>
<td><input name="mailserver_login" type="text" id="mailserver_login" value="<c:out value="${__wp_admin_options_writing_block23}" escapeXml="false" />" size="40" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block24}" escapeXml="false" /></th>
<td>
<input name="mailserver_pass" type="text" id="mailserver_pass" value="<c:out value="${__wp_admin_options_writing_block25}" escapeXml="false" />" size="40" />
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_writing_block26}" escapeXml="false" /></th>
<td><select name="default_email_category" id="default_email_category">
<c:out value="${__wp_admin_options_writing_block27}" escapeXml="false" /></select></td>
</tr>
</table>

<h3><c:out value="${__wp_admin_options_writing_block28}" escapeXml="false" /></h3>

<c:out value="${__wp_admin_options_writing_block29}" escapeXml="false" />
<p class="submit">
<input type="hidden" name="action" value="update" />
<input type="hidden" name="page_options" value="default_post_edit_rows,use_smilies,ping_sites,mailserver_url,mailserver_port,mailserver_login,mailserver_pass,default_category,default_email_category,use_balanceTags,default_link_category" />
<input type="submit" name="Submit" value="<c:out value="${__wp_admin_options_writing_block30}" escapeXml="false" />" />
</p>
</form>
</div>

<c:out value="${__wp_admin_options_writing_block31}" escapeXml="false" />