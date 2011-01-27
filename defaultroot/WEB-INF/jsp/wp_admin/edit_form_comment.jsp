<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_form_comment_block1}" escapeXml="false" />
<form name="post" action="comment.php" method="post" id="post">
<c:out value="${__wp_admin_edit_form_comment_block2}" escapeXml="false" /><div class="wrap">
<h2><c:out value="${__wp_admin_edit_form_comment_block3}" escapeXml="false" /></h2>
<input type="hidden" name="user_ID" value="<c:out value="${__wp_admin_edit_form_comment_block4}" escapeXml="false" />" />
<input type="hidden" name="action" value='<c:out value="${__wp_admin_edit_form_comment_block5}" escapeXml="false" />' />

<div id="poststuff">

<div class="submitbox" id="submitcomment">

<div id="previewview">
<a href="<c:out value="${__wp_admin_edit_form_comment_block6}" escapeXml="false" />" target="_blank"><c:out value="${__wp_admin_edit_form_comment_block7}" escapeXml="false" /></a>
</div>

<div class="inside">

<p><strong><c:out value="${__wp_admin_edit_form_comment_block8}" escapeXml="false" /></strong></p>
<p>
<select name='comment_status'>
<option<c:out value="${__wp_admin_edit_form_comment_block9}" escapeXml="false" /> value='1'><c:out value="${__wp_admin_edit_form_comment_block10}" escapeXml="false" /></option>
<option<c:out value="${__wp_admin_edit_form_comment_block11}" escapeXml="false" /> value='0'><c:out value="${__wp_admin_edit_form_comment_block12}" escapeXml="false" /></option>
<option<c:out value="${__wp_admin_edit_form_comment_block13}" escapeXml="false" /> value='spam'><c:out value="${__wp_admin_edit_form_comment_block14}" escapeXml="false" /></option>
</select>
</p>

<c:out value="${__wp_admin_edit_form_comment_block15}" escapeXml="false" /><p class="curtime"><c:out value="${__wp_admin_edit_form_comment_block16}" escapeXml="false" />&nbsp;<a href="#edit_timestamp" class="edit-timestamp hide-if-no-js"><c:out value="${__wp_admin_edit_form_comment_block17}" escapeXml="false" /></a></p>

<div id='timestampdiv' class='hide-if-js'><c:out value="${__wp_admin_edit_form_comment_block18}" escapeXml="false" /></div>

</div>

<p class="submit">
<input type="submit" name="save" value="<c:out value="${__wp_admin_edit_form_comment_block19}" escapeXml="false" />" tabindex="4" class="button button-highlighted" />
<c:out value="${__wp_admin_edit_form_comment_block20}" escapeXml="false" /></p>

<div class="side-info">
<h5><c:out value="${__wp_admin_edit_form_comment_block21}" escapeXml="false" /></h5>

<ul>
<li><a href="edit-comments.php"><c:out value="${__wp_admin_edit_form_comment_block22}" escapeXml="false" /></a></li>
<li><a href="edit-comments.php?comment_status=moderated"><c:out value="${__wp_admin_edit_form_comment_block23}" escapeXml="false" /></a></li>
<c:out value="${__wp_admin_edit_form_comment_block24}" escapeXml="false" /></ul>
</div>
<c:out value="${__wp_admin_edit_form_comment_block25}" escapeXml="false" /></div>

<div id="post-body">
<div id="namediv" class="stuffbox">
<h3><c:out value="${__wp_admin_edit_form_comment_block26}" escapeXml="false" /></h3>
<div class="inside">
<input type="text" name="newcomment_author" size="30" value="<c:out value="${__wp_admin_edit_form_comment_block27}" escapeXml="false" />" tabindex="1" id="name" />
</div>
</div>

<div id="emaildiv" class="stuffbox">
<h3><c:out value="${__wp_admin_edit_form_comment_block28}" escapeXml="false" /></h3>
<div class="inside">
<input type="text" name="newcomment_author_email" size="30" value="<c:out value="${__wp_admin_edit_form_comment_block29}" escapeXml="false" />" tabindex="2" id="email" />
</div>
</div>

<div id="uridiv" class="stuffbox">
<h3><c:out value="${__wp_admin_edit_form_comment_block30}" escapeXml="false" /></h3>
<div class="inside">
<input type="text" id="newcomment_author_url" name="newcomment_author_url" size="30" value="<c:out value="${__wp_admin_edit_form_comment_block31}" escapeXml="false" />" tabindex="3" />
</div>
</div>

<div id="postdiv" class="postarea">
<h3><c:out value="${__wp_admin_edit_form_comment_block32}" escapeXml="false" /></h3>
<c:out value="${__wp_admin_edit_form_comment_block33}" escapeXml="false" /></div>

<c:out value="${__wp_admin_edit_form_comment_block34}" escapeXml="false" />
<input type="hidden" name="c" value="<c:out value="${__wp_admin_edit_form_comment_block35}" escapeXml="false" />" />
<input type="hidden" name="p" value="<c:out value="${__wp_admin_edit_form_comment_block36}" escapeXml="false" />" />
<input name="referredby" type="hidden" id="referredby" value="<c:out value="${__wp_admin_edit_form_comment_block37}" escapeXml="false" />" />
<c:out value="${__wp_admin_edit_form_comment_block38}" escapeXml="false" /><input type="hidden" name="noredir" value="1" />
</div>
</div>
</div>

</form>

<script type="text/javascript">
try{document.post.name.focus();}catch(e){}
</script>
