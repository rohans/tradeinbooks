<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_discussion_block1}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_options_discussion_block2}" escapeXml="false" /></h2>
<form method="post" action="options.php">
<c:out value="${__wp_admin_options_discussion_block3}" escapeXml="false" /><table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block4}" escapeXml="false" /></th>
<td>
<label for="default_pingback_flag">
<input name="default_pingback_flag" type="checkbox" id="default_pingback_flag" value="1" <c:out value="${__wp_admin_options_discussion_block5}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_discussion_block6}" escapeXml="false" /></label>
<br />
<label for="default_ping_status">
<input name="default_ping_status" type="checkbox" id="default_ping_status" value="open" <c:out value="${__wp_admin_options_discussion_block7}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_discussion_block8}" escapeXml="false" /></label>
<br />
<label for="default_comment_status">
<input name="default_comment_status" type="checkbox" id="default_comment_status" value="open" <c:out value="${__wp_admin_options_discussion_block9}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_discussion_block10}" escapeXml="false" /></label>
<br />
<small><em><c:out value="${__wp_admin_options_discussion_block11}" escapeXml="false" /></em></small>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block12}" escapeXml="false" /></th>
<td>
<label for="comments_notify">
<input name="comments_notify" type="checkbox" id="comments_notify" value="1" <c:out value="${__wp_admin_options_discussion_block13}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_discussion_block14}" escapeXml="false" /> </label>
<br />
<label for="moderation_notify">
<input name="moderation_notify" type="checkbox" id="moderation_notify" value="1" <c:out value="${__wp_admin_options_discussion_block15}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_discussion_block16}" escapeXml="false" /> </label>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block17}" escapeXml="false" /></th>
<td>
<label for="comment_moderation">
<input name="comment_moderation" type="checkbox" id="comment_moderation" value="1" <c:out value="${__wp_admin_options_discussion_block18}" escapeXml="false" /> />
<c:out value="${__wp_admin_options_discussion_block19}" escapeXml="false" /> </label>
<br />
<label for="require_name_email"><input type="checkbox" name="require_name_email" id="require_name_email" value="1" <c:out value="${__wp_admin_options_discussion_block20}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_discussion_block21}" escapeXml="false" /></label>
<br />
<label for="comment_whitelist"><input type="checkbox" name="comment_whitelist" id="comment_whitelist" value="1" <c:out value="${__wp_admin_options_discussion_block22}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_discussion_block23}" escapeXml="false" /></label>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block24}" escapeXml="false" /></th>
<td>
<p><c:out value="${__wp_admin_options_discussion_block25}" escapeXml="false" /></p>

<p><c:out value="${__wp_admin_options_discussion_block26}" escapeXml="false" /></p>
<p>
<textarea name="moderation_keys" cols="60" rows="10" id="moderation_keys" style="width: 98%; font-size: 12px;" class="code"><c:out value="${__wp_admin_options_discussion_block27}" escapeXml="false" /></textarea>
</p>
</td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block28}" escapeXml="false" /></th>
<td>
<p><c:out value="${__wp_admin_options_discussion_block29}" escapeXml="false" /></p>
<p>
<textarea name="blacklist_keys" cols="60" rows="10" id="blacklist_keys" style="width: 98%; font-size: 12px;" class="code"><c:out value="${__wp_admin_options_discussion_block30}" escapeXml="false" /></textarea>
</p>
</td>
</tr>
</table>

<h3><c:out value="${__wp_admin_options_discussion_block31}" escapeXml="false" /></h3>

<p><c:out value="${__wp_admin_options_discussion_block32}" escapeXml="false" /></p>


<table class="form-table">
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block33}" escapeXml="false" /></th>
<td>
<c:out value="${__wp_admin_options_discussion_block34}" escapeXml="false" /></td>
</tr>
<tr valign="top">
<th scope="row"><c:out value="${__wp_admin_options_discussion_block35}" escapeXml="false" /></th>
<td>

<c:out value="${__wp_admin_options_discussion_block36}" escapeXml="false" />
</td>
</tr>

</table>


<p class="submit">
<input type="hidden" name="action" value="update" />
<input type="hidden" name="page_options" value="default_pingback_flag,default_ping_status,default_comment_status,comments_notify,moderation_notify,comment_moderation,require_name_email,comment_whitelist,comment_max_links,moderation_keys,blacklist_keys,show_avatars,avatar_rating" />
<input type="submit" name="Submit" value="<c:out value="${__wp_admin_options_discussion_block37}" escapeXml="false" />" />
</p>
</form>
</div>

<c:out value="${__wp_admin_options_discussion_block38}" escapeXml="false" />