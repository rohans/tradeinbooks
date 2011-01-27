<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_tag_form_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_edit_tag_form_block2}" escapeXml="false" /></h2>
<div id="ajax-response"></div>
<c:out value="${__wp_admin_edit_tag_form_block3}" escapeXml="false" /><input type="hidden" name="action" value="<c:out value="${__wp_admin_edit_tag_form_block4}" escapeXml="false" />" />
<input type="hidden" name="tag_ID" value="<c:out value="${__wp_admin_edit_tag_form_block5}" escapeXml="false" />" />
<c:out value="${__wp_admin_edit_tag_form_block6}" escapeXml="false" />	<table class="form-table">
		<tr class="form-field form-required">
			<th scope="row" valign="top"><label for="name"><c:out value="${__wp_admin_edit_tag_form_block7}" escapeXml="false" /></label></th>
			<td><input name="name" id="name" type="text" value="<c:out value="${__wp_admin_edit_tag_form_block8}" escapeXml="false" />" size="40" />
            <p><c:out value="${__wp_admin_edit_tag_form_block9}" escapeXml="false" /></p></td>
		</tr>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="slug"><c:out value="${__wp_admin_edit_tag_form_block10}" escapeXml="false" /></label></th>
			<td><input name="slug" id="slug" type="text" value="<c:out value="${__wp_admin_edit_tag_form_block11}" escapeXml="false" />" size="40" />
            <p><c:out value="${__wp_admin_edit_tag_form_block12}" escapeXml="false" /></p></td>
		</tr>
	</table>
<p class="submit"><input type="submit" class="button" name="submit" value="<c:out value="${__wp_admin_edit_tag_form_block13}" escapeXml="false" />" /></p>
<c:out value="${__wp_admin_edit_tag_form_block14}" escapeXml="false" /></form>
</div>
