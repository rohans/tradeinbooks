<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_category_form_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_edit_category_form_block2}" escapeXml="false" /></h2>
<div id="ajax-response"></div>
<c:out value="${__wp_admin_edit_category_form_block3}" escapeXml="false" /><input type="hidden" name="action" value="<c:out value="${__wp_admin_edit_category_form_block4}" escapeXml="false" />" />
<input type="hidden" name="cat_ID" value="<c:out value="${__wp_admin_edit_category_form_block5}" escapeXml="false" />" />
<c:out value="${__wp_admin_edit_category_form_block6}" escapeXml="false" />	<table class="form-table">
		<tr class="form-field form-required">
			<th scope="row" valign="top"><label for="cat_name"><c:out value="${__wp_admin_edit_category_form_block7}" escapeXml="false" /></label></th>
			<td><input name="cat_name" id="cat_name" type="text" value="<c:out value="${__wp_admin_edit_category_form_block8}" escapeXml="false" />" size="40" /><br />
            <c:out value="${__wp_admin_edit_category_form_block9}" escapeXml="false" /></td>
		</tr>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="category_nicename"><c:out value="${__wp_admin_edit_category_form_block10}" escapeXml="false" /></label></th>
			<td><input name="category_nicename" id="category_nicename" type="text" value="<c:out value="${__wp_admin_edit_category_form_block11}" escapeXml="false" />" size="40" /><br />
            <c:out value="${__wp_admin_edit_category_form_block12}" escapeXml="false" /></td>
		</tr>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="category_parent"><c:out value="${__wp_admin_edit_category_form_block13}" escapeXml="false" /></label></th>
			<td>
	  			<c:out value="${__wp_admin_edit_category_form_block14}" escapeXml="false" /><br />
                <c:out value="${__wp_admin_edit_category_form_block15}" escapeXml="false" />	  		</td>
		</tr>
		<tr class="form-field">
			<th scope="row" valign="top"><label for="category_description"><c:out value="${__wp_admin_edit_category_form_block16}" escapeXml="false" /></label></th>
			<td><textarea name="category_description" id="category_description" rows="5" cols="50" style="width: 97%;"><c:out value="${__wp_admin_edit_category_form_block17}" escapeXml="false" /></textarea><br />
            <c:out value="${__wp_admin_edit_category_form_block18}" escapeXml="false" /></td>
		</tr>
	</table>
<p class="submit"><input type="submit" class="button" name="submit" value="<c:out value="${__wp_admin_edit_category_form_block19}" escapeXml="false" />" /></p>
<c:out value="${__wp_admin_edit_category_form_block20}" escapeXml="false" /></form>
</div>
