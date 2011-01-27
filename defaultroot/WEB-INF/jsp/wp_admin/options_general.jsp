<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_general_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_options_general_block2}" escapeXml="false" /></h2>
<form method="post" action="options.php">
<c:out value="${__wp_admin_options_general_block3}" escapeXml="false" /><table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block4}" escapeXml="false" /></th>
<td><input name="blogname" type="text" id="blogname" value="<c:out value="${__wp_admin_options_general_block5}" escapeXml="false" />" size="40" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block6}" escapeXml="false" /></th>
<td><input name="blogdescription" type="text" id="blogdescription" style="width: 95%" value="<c:out value="${__wp_admin_options_general_block7}" escapeXml="false" />" size="45" />
<br />
<c:out value="${__wp_admin_options_general_block8}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block9}" escapeXml="false" /></th>
<td><input name="siteurl" type="text" id="siteurl" value="<c:out value="${__wp_admin_options_general_block10}" escapeXml="false" />" size="40" class="code<c:out value="${__wp_admin_options_general_block11}" escapeXml="false" /> /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block12}" escapeXml="false" /></th>
<td><input name="home" type="text" id="home" value="<c:out value="${__wp_admin_options_general_block13}" escapeXml="false" />" size="40" class="code<c:out value="${__wp_admin_options_general_block14}" escapeXml="false" /> /><br /><c:out value="${__wp_admin_options_general_block15}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block16}" escapeXml="false" /> </th>
<td><input name="admin_email" type="text" id="admin_email" value="<c:out value="${__wp_admin_options_general_block17}" escapeXml="false" />" size="40" class="code" />
<br />
<c:out value="${__wp_admin_options_general_block18}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block19}" escapeXml="false" /></th>
<td> <label for="users_can_register">
<input name="users_can_register" type="checkbox" id="users_can_register" value="1" <c:out value="${__wp_admin_options_general_block20}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_general_block21}" escapeXml="false" /></label><br />
<label for="comment_registration">
<input name="comment_registration" type="checkbox" id="comment_registration" value="1" <c:out value="${__wp_admin_options_general_block22}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_general_block23}" escapeXml="false" /></label>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_general_block24}" escapeXml="false" /></th>
<td><label for="default_role">
<select name="default_role" id="default_role"><c:out value="${__wp_admin_options_general_block25}" escapeXml="false" /></select></label>
</td>
</tr>
<tr>
<th scope="row"><c:out value="${__wp_admin_options_general_block26}" escapeXml="false" /> </th>
<td>
<select name="gmt_offset">
<c:out value="${__wp_admin_options_general_block27}" escapeXml="false" /></select>
<c:out value="${__wp_admin_options_general_block28}" escapeXml="false" /><br />
<c:out value="${__wp_admin_options_general_block29}" escapeXml="false" /><br />
<c:out value="${__wp_admin_options_general_block30}" escapeXml="false" /><br />
<c:out value="${__wp_admin_options_general_block31}" escapeXml="false" /></td>
</tr>
<tr>
<th scope="row"><c:out value="${__wp_admin_options_general_block32}" escapeXml="false" /></th>
<td><input name="date_format" type="text" id="date_format" size="30" value="<c:out value="${__wp_admin_options_general_block33}" escapeXml="false" />" /><br />
<c:out value="${__wp_admin_options_general_block34}" escapeXml="false" /> <strong><c:out value="${__wp_admin_options_general_block35}" escapeXml="false" /></strong></td>
</tr>
<tr>
<th scope="row"><c:out value="${__wp_admin_options_general_block36}" escapeXml="false" /></th>
<td><input name="time_format" type="text" id="time_format" size="30" value="<c:out value="${__wp_admin_options_general_block37}" escapeXml="false" />" /><br />
<c:out value="${__wp_admin_options_general_block38}" escapeXml="false" /> <strong><c:out value="${__wp_admin_options_general_block39}" escapeXml="false" /></strong><br />
<c:out value="${__wp_admin_options_general_block40}" escapeXml="false" /></td>
</tr>
<tr>
<th scope="row"><c:out value="${__wp_admin_options_general_block41}" escapeXml="false" /></th>
<td><select name="start_of_week" id="start_of_week">
<c:out value="${__wp_admin_options_general_block42}" escapeXml="false" /></select></td>
</tr>
</table>

<p class="submit"><input type="submit" name="Submit" value="<c:out value="${__wp_admin_options_general_block43}" escapeXml="false" />" />
<input type="hidden" name="action" value="update" />
<input type="hidden" name="page_options" value="<c:out value="${__wp_admin_options_general_block44}" escapeXml="false" />blogname,blogdescription,admin_email,users_can_register,gmt_offset,date_format,time_format,start_of_week,comment_registration,default_role" />
</p>
</form>

</div>

<c:out value="${__wp_admin_options_general_block45}" escapeXml="false" />