<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_misc_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_options_misc_block2}" escapeXml="false" /></h2>
<form method="post" action="options.php">
<c:out value="${__wp_admin_options_misc_block3}" escapeXml="false" /><h3><c:out value="${__wp_admin_options_misc_block4}" escapeXml="false" /></h3>
<table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_misc_block5}" escapeXml="false" /></th>
<td><input name="upload_path" type="text" id="upload_path" class="code" value="<c:out value="${__wp_admin_options_misc_block6}" escapeXml="false" />" size="40" />
<br />
<c:out value="${__wp_admin_options_misc_block7}" escapeXml="false" /></td>
</tr>

<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_misc_block8}" escapeXml="false" /></th>
<td><input name="upload_url_path" type="text" id="upload_url_path" class="code" value="<c:out value="${__wp_admin_options_misc_block9}" escapeXml="false" />" size="40" />
</td>
</tr>

<tr>
<th scope="row" colspan="2" class="th-full">
<label for="uploads_use_yearmonth_folders">
<input name="uploads_use_yearmonth_folders" type="checkbox" id="uploads_use_yearmonth_folders" value="1" <c:out value="${__wp_admin_options_misc_block10}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_misc_block11}" escapeXml="false" /></label>
</th>
</tr>
</table>

<h3><c:out value="${__wp_admin_options_misc_block12}" escapeXml="false" /></h3>
<p><c:out value="${__wp_admin_options_misc_block13}" escapeXml="false" /></p>

<table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_misc_block14}" escapeXml="false" /></th>
<td>
<label for="thumbnail_size_w"><c:out value="${__wp_admin_options_misc_block15}" escapeXml="false" /></label>
<input name="thumbnail_size_w" type="text" id="thumbnail_size_w" value="<c:out value="${__wp_admin_options_misc_block16}" escapeXml="false" />" size="6" />
<label for="thumbnail_size_h"><c:out value="${__wp_admin_options_misc_block17}" escapeXml="false" /></label>
<input name="thumbnail_size_h" type="text" id="thumbnail_size_h" value="<c:out value="${__wp_admin_options_misc_block18}" escapeXml="false" />" size="6" /><br />
<input name="thumbnail_crop" type="checkbox" id="thumbnail_crop" value="1" <c:out value="${__wp_admin_options_misc_block19}" escapeXml="false" />/>
<label for="thumbnail_crop"><c:out value="${__wp_admin_options_misc_block20}" escapeXml="false" /></label>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_misc_block21}" escapeXml="false" /></th>
<td>
<label for="medium_size_w"><c:out value="${__wp_admin_options_misc_block22}" escapeXml="false" /></label>
<input name="medium_size_w" type="text" id="medium_size_w" value="<c:out value="${__wp_admin_options_misc_block23}" escapeXml="false" />" size="6" />
<label for="medium_size_h"><c:out value="${__wp_admin_options_misc_block24}" escapeXml="false" /></label>
<input name="medium_size_h" type="text" id="medium_size_h" value="<c:out value="${__wp_admin_options_misc_block25}" escapeXml="false" />" size="6" />
</td>
</tr>
</table>



<table class="form-table">

<tr>
<th scope="row" class="th-full">
<label for="use_linksupdate">
<input name="use_linksupdate" type="checkbox" id="use_linksupdate" value="1" <c:out value="${__wp_admin_options_misc_block26}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_misc_block27}" escapeXml="false" /></label>
</th>
</tr>
<tr>

<th scope="row" class="th-full">
<label for="hack_file">
<input type="checkbox" id="hack_file" name="hack_file" value="1" <c:out value="${__wp_admin_options_misc_block28}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_misc_block29}" escapeXml="false" /></label>
</th>
</tr>

</table>

<p class="submit">
<input type="hidden" name="action" value="update" />
<input type="hidden" name="page_options" value="hack_file,use_linksupdate,uploads_use_yearmonth_folders,upload_path,upload_url_path,thumbnail_size_w,thumbnail_size_h,thumbnail_crop,medium_size_w,medium_size_h" />
<input type="submit" name="Submit" value="<c:out value="${__wp_admin_options_misc_block30}" escapeXml="false" />" class="button" />
</p>
</form>
</div>

<c:out value="${__wp_admin_options_misc_block31}" escapeXml="false" />