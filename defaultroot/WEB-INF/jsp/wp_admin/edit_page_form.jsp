<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_page_form_block1}" escapeXml="false" />
<form name="post" action="page.php" method="post" id="post">
<div class="wrap">
<h2><c:out value="${__wp_admin_edit_page_form_block2}" escapeXml="false" /></h2>

<c:out value="${__wp_admin_edit_page_form_block3}" escapeXml="false" /><input type="hidden" id="user-id" name="user_ID" value="<c:out value="${__wp_admin_edit_page_form_block4}" escapeXml="false" />" />
<input type="hidden" id="hiddenaction" name="action" value='<c:out value="${__wp_admin_edit_page_form_block5}" escapeXml="false" />' />
<input type="hidden" id="originalaction" name="originalaction" value="<c:out value="${__wp_admin_edit_page_form_block6}" escapeXml="false" />" />
<c:out value="${__wp_admin_edit_page_form_block7}" escapeXml="false" /><input type="hidden" id="post_type" name="post_type" value="<c:out value="${__wp_admin_edit_page_form_block8}" escapeXml="false" />" />
<input type="hidden" id="original_post_status" name="original_post_status" value="<c:out value="${__wp_admin_edit_page_form_block9}" escapeXml="false" />" />
<input name="referredby" type="hidden" id="referredby" value="<c:out value="${__wp_admin_edit_page_form_block10}" escapeXml="false" />" />
<c:out value="${__wp_admin_edit_page_form_block11}" escapeXml="false" />
<div id="poststuff">

<div class="submitbox" id="submitpage">

<div id="previewview">
<c:out value="${__wp_admin_edit_page_form_block12}" escapeXml="false" /></div>

<div class="inside">

<p><strong><c:out value="${__wp_admin_edit_page_form_block13}" escapeXml="false" /></strong></p>
<p>
<select name='post_status' tabindex='4'>
<c:out value="${__wp_admin_edit_page_form_block14}" escapeXml="false" /><option<c:out value="${__wp_admin_edit_page_form_block15}" escapeXml="false" /> value='pending'><c:out value="${__wp_admin_edit_page_form_block16}" escapeXml="false" /></option>
<option<c:out value="${__wp_admin_edit_page_form_block17}" escapeXml="false" /> value='draft'><c:out value="${__wp_admin_edit_page_form_block18}" escapeXml="false" /></option>
</select>
</p>

<p><label for="post_status_private" class="selectit"><input id="post_status_private" name="post_status" type="checkbox" value="private" <c:out value="${__wp_admin_edit_page_form_block19}" escapeXml="false" /> tabindex='4' /> <c:out value="${__wp_admin_edit_page_form_block20}" escapeXml="false" /></label></p>
<c:out value="${__wp_admin_edit_page_form_block21}" escapeXml="false" /><p class="curtime"><c:out value="${__wp_admin_edit_page_form_block22}" escapeXml="false" />&nbsp;<a href="#edit_timestamp" class="edit-timestamp hide-if-no-js" tabindex='4'><c:out value="${__wp_admin_edit_page_form_block23}" escapeXml="false" /></a></p>

<div id='timestampdiv' class='hide-if-js'><c:out value="${__wp_admin_edit_page_form_block24}" escapeXml="false" /></div>

</div>

<p class="submit">
<input type="submit" name="save" class="button button-highlighted" value="<c:out value="${__wp_admin_edit_page_form_block25}" escapeXml="false" />" tabindex="4" />
<c:out value="${__wp_admin_edit_page_form_block26}" escapeXml="false" /><br class="clear" />
<c:out value="${__wp_admin_edit_page_form_block27}" escapeXml="false" /><span id="autosave"></span>
</p>

<div class="side-info">
<h5><c:out value="${__wp_admin_edit_page_form_block28}" escapeXml="false" /></h5>

<ul>
<c:out value="${__wp_admin_edit_page_form_block29}" escapeXml="false" /><li><a href="edit-comments.php"><c:out value="${__wp_admin_edit_page_form_block30}" escapeXml="false" /></a></li>
<li><a href="edit-pages.php"><c:out value="${__wp_admin_edit_page_form_block31}" escapeXml="false" /></a></li>
<c:out value="${__wp_admin_edit_page_form_block32}" escapeXml="false" /></ul>
</div>
<c:out value="${__wp_admin_edit_page_form_block33}" escapeXml="false" /></div>

<div id="post-body">
<div id="titlediv">
<h3><c:out value="${__wp_admin_edit_page_form_block34}" escapeXml="false" /></h3>
<div id="titlewrap">
  <input type="text" name="post_title" size="30" tabindex="1" value="<c:out value="${__wp_admin_edit_page_form_block35}" escapeXml="false" />" id="title" autocomplete="off" />
</div>
<div class="inside">
<c:out value="${__wp_admin_edit_page_form_block36}" escapeXml="false" />	<div id="edit-slug-box">
<c:out value="${__wp_admin_edit_page_form_block37}" escapeXml="false" />	</div>
</div>
</div>

<div id="<c:out value="${__wp_admin_edit_page_form_block38}" escapeXml="false" />" class="postarea">
<h3><c:out value="${__wp_admin_edit_page_form_block39}" escapeXml="false" /></h3>
<c:out value="${__wp_admin_edit_page_form_block40}" escapeXml="false" /></div>

<c:out value="${__wp_admin_edit_page_form_block41}" escapeXml="false" />
<c:out value="${__wp_admin_edit_page_form_block42}" escapeXml="false" />
<h2><c:out value="${__wp_admin_edit_page_form_block43}" escapeXml="false" /></h2>

<div id="pagepostcustom" class="postbox <c:out value="${__wp_admin_edit_page_form_block44}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_page_form_block45}" escapeXml="false" /></h3>
<div class="inside">
<div id="postcustomstuff">
<table cellpadding="3">
<c:out value="${__wp_admin_edit_page_form_block46}" escapeXml="false" />
</table>
<c:out value="${__wp_admin_edit_page_form_block47}" escapeXml="false" /><div id="ajax-response"></div>
</div>
<p><c:out value="${__wp_admin_edit_page_form_block48}" escapeXml="false" /></p>
</div>
</div>

<div id="pagecommentstatusdiv" class="postbox <c:out value="${__wp_admin_edit_page_form_block49}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_page_form_block50}" escapeXml="false" /></h3>
<div class="inside">
<input name="advanced_view" type="hidden" value="1" />
<p><label for="comment_status" class="selectit">
<input name="comment_status" type="checkbox" id="comment_status" value="open" <c:out value="${__wp_admin_edit_page_form_block51}" escapeXml="false" /> />
<c:out value="${__wp_admin_edit_page_form_block52}" escapeXml="false" /></label></p>
<p><label for="ping_status" class="selectit"><input name="ping_status" type="checkbox" id="ping_status" value="open" <c:out value="${__wp_admin_edit_page_form_block53}" escapeXml="false" /> /> <c:out value="${__wp_admin_edit_page_form_block54}" escapeXml="false" /></label></p>
<p><c:out value="${__wp_admin_edit_page_form_block55}" escapeXml="false" /></p>
</div>
</div>

<div id="pagepassworddiv" class="postbox <c:out value="${__wp_admin_edit_page_form_block56}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_page_form_block57}" escapeXml="false" /></h3>
<div class="inside">
<p><input name="post_password" type="text" size="25" id="post_password" value="<c:out value="${__wp_admin_edit_page_form_block58}" escapeXml="false" />" /></p>
<p><c:out value="${__wp_admin_edit_page_form_block59}" escapeXml="false" /></p>
</div>
</div>

<div id="pageslugdiv" class="postbox <c:out value="${__wp_admin_edit_page_form_block60}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_page_form_block61}" escapeXml="false" /></h3>
<div class="inside">
<input name="post_name" type="text" size="13" id="post_name" value="<c:out value="${__wp_admin_edit_page_form_block62}" escapeXml="false" />" />
</div>
</div>

<div id="pageparentdiv" class="postbox <c:out value="${__wp_admin_edit_page_form_block63}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_page_form_block64}" escapeXml="false" /></h3>
<div class="inside">
<select name="parent_id">
<option value='0'><c:out value="${__wp_admin_edit_page_form_block65}" escapeXml="false" /></option>
<c:out value="${__wp_admin_edit_page_form_block66}" escapeXml="false" /></select>
<p><c:out value="${__wp_admin_edit_page_form_block67}" escapeXml="false" /></p>
</div>
</div>

<c:out value="${__wp_admin_edit_page_form_block68}" escapeXml="false" />
<div id="pageorderdiv" class="postbox <c:out value="${__wp_admin_edit_page_form_block69}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_page_form_block70}" escapeXml="false" /></h3>
<div class="inside">
<p><input name="menu_order" type="text" size="4" id="menu_order" value="<c:out value="${__wp_admin_edit_page_form_block71}" escapeXml="false" />" /></p>
<p><c:out value="${__wp_admin_edit_page_form_block72}" escapeXml="false" /></p>
</div>
</div>

<c:out value="${__wp_admin_edit_page_form_block73}" escapeXml="false" />
<c:out value="${__wp_admin_edit_page_form_block74}" escapeXml="false" />
</div>
</div>

</div>

</form>

<script type="text/javascript">
try{document.post.title.focus();}catch(e){}
</script>
