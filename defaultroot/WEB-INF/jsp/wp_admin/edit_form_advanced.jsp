<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

 <c:out value="${__wp_admin_edit_form_advanced_block1}" escapeXml="false" />
<form name="post" action="post.php" method="post" id="post">
<c:out value="${__wp_admin_edit_form_advanced_block2}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_edit_form_advanced_block3}" escapeXml="false" /></h2>
<c:out value="${__wp_admin_edit_form_advanced_block4}" escapeXml="false" />
<input type="hidden" id="user-id" name="user_ID" value="<c:out value="${__wp_admin_edit_form_advanced_block5}" escapeXml="false" />" />
<input type="hidden" id="hiddenaction" name="action" value="<c:out value="${__wp_admin_edit_form_advanced_block6}" escapeXml="false" />" />
<input type="hidden" id="originalaction" name="originalaction" value="<c:out value="${__wp_admin_edit_form_advanced_block7}" escapeXml="false" />" />
<input type="hidden" id="post_author" name="post_author" value="<c:out value="${__wp_admin_edit_form_advanced_block8}" escapeXml="false" />" />
<input type="hidden" id="post_type" name="post_type" value="<c:out value="${__wp_admin_edit_form_advanced_block9}" escapeXml="false" />" />
<input type="hidden" id="original_post_status" name="original_post_status" value="<c:out value="${__wp_admin_edit_form_advanced_block10}" escapeXml="false" />" />
<input name="referredby" type="hidden" id="referredby" value="<c:out value="${__wp_admin_edit_form_advanced_block11}" escapeXml="false" />" />
<c:out value="${__wp_admin_edit_form_advanced_block12}" escapeXml="false" />
<c:out value="${__wp_admin_edit_form_advanced_block13}" escapeXml="false" />
<div id="poststuff">

<div class="submitbox" id="submitpost">

<div id="previewview">
<c:out value="${__wp_admin_edit_form_advanced_block14}" escapeXml="false" /></div>

<div class="inside">

<p><strong><c:out value="${__wp_admin_edit_form_advanced_block15}" escapeXml="false" /></strong></p>
<p>
<select name='post_status' tabindex='4'>
<c:out value="${__wp_admin_edit_form_advanced_block16}" escapeXml="false" /><option<c:out value="${__wp_admin_edit_form_advanced_block17}" escapeXml="false" /> value='pending'><c:out value="${__wp_admin_edit_form_advanced_block18}" escapeXml="false" /></option>
<option<c:out value="${__wp_admin_edit_form_advanced_block19}" escapeXml="false" /> value='draft'><c:out value="${__wp_admin_edit_form_advanced_block20}" escapeXml="false" /></option>
</select>
</p>

<c:out value="${__wp_admin_edit_form_advanced_block21}" escapeXml="false" />
</div>

<p class="submit">
<input type="submit" name="save" id="save-post" value="<c:out value="${__wp_admin_edit_form_advanced_block22}" escapeXml="false" />" tabindex="4" class="button button-highlighted" />
<c:out value="${__wp_admin_edit_form_advanced_block23}" escapeXml="false" /><br class="clear" />
<c:out value="${__wp_admin_edit_form_advanced_block24}" escapeXml="false" /><span id="autosave"></span>
</p>

<div class="side-info">
<h5><c:out value="${__wp_admin_edit_form_advanced_block25}" escapeXml="false" /></h5>

<ul>
<c:out value="${__wp_admin_edit_form_advanced_block26}" escapeXml="false" /><li><a href="edit-comments.php"><c:out value="${__wp_admin_edit_form_advanced_block27}" escapeXml="false" /></a></li>
<li><a href="edit.php"><c:out value="${__wp_admin_edit_form_advanced_block28}" escapeXml="false" /></a></li>
<li><a href="categories.php"><c:out value="${__wp_admin_edit_form_advanced_block29}" escapeXml="false" /></a></li>
<li><a href="edit-tags.php"><c:out value="${__wp_admin_edit_form_advanced_block30}" escapeXml="false" /></a></li>
<li><a href="edit.php?post_status=draft"><c:out value="${__wp_admin_edit_form_advanced_block31}" escapeXml="false" /></a></li>
<c:out value="${__wp_admin_edit_form_advanced_block32}" escapeXml="false" /></ul>
</div>

<c:out value="${__wp_admin_edit_form_advanced_block33}" escapeXml="false" /></div>

<div id="post-body">
<div id="titlediv">
<h3><c:out value="${__wp_admin_edit_form_advanced_block34}" escapeXml="false" /></h3>
<div id="titlewrap">
	<input type="text" name="post_title" size="30" tabindex="1" value="<c:out value="${__wp_admin_edit_form_advanced_block35}" escapeXml="false" />" id="title" autocomplete="off" />
</div>
<div class="inside">
<c:out value="${__wp_admin_edit_form_advanced_block36}" escapeXml="false" />	<div id="edit-slug-box">
<c:out value="${__wp_admin_edit_form_advanced_block37}" escapeXml="false" />	</div>
</div>
</div>

<div id="<c:out value="${__wp_admin_edit_form_advanced_block38}" escapeXml="false" />" class="postarea">
<h3><c:out value="${__wp_admin_edit_form_advanced_block39}" escapeXml="false" /></h3>
<c:out value="${__wp_admin_edit_form_advanced_block40}" escapeXml="false" /></div>

<c:out value="${__wp_admin_edit_form_advanced_block41}" escapeXml="false" />
<div id="tagsdiv" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block42}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block43}" escapeXml="false" /></h3>
<div class="inside">
<p id="jaxtag"><input type="text" name="tags_input" class="tags-input" id="tags-input" size="40" tabindex="3" value="<c:out value="${__wp_admin_edit_form_advanced_block44}" escapeXml="false" />" /></p>
<div id="tagchecklist"></div>
</div>
</div>

<div id="categorydiv" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block45}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block46}" escapeXml="false" /></h3>
<div class="inside">

<div id="category-adder" class="wp-hidden-children">
	<h4><a id="category-add-toggle" href="#category-add" class="hide-if-no-js" tabindex="3"><c:out value="${__wp_admin_edit_form_advanced_block47}" escapeXml="false" /></a></h4>
	<p id="category-add" class="wp-hidden-child">
		<input type="text" name="newcat" id="newcat" class="form-required form-input-tip" value="<c:out value="${__wp_admin_edit_form_advanced_block48}" escapeXml="false" />" tabindex="3" />
		<c:out value="${__wp_admin_edit_form_advanced_block49}" escapeXml="false" />		<input type="button" id="category-add-sumbit" class="add:categorychecklist:category-add button" value="<c:out value="${__wp_admin_edit_form_advanced_block50}" escapeXml="false" />" tabindex="3" />
		<c:out value="${__wp_admin_edit_form_advanced_block51}" escapeXml="false" />		<span id="category-ajax-response"></span>
	</p>
</div>

<ul id="category-tabs">
	<li class="ui-tabs-selected"><a href="#categories-all" tabindex="3"><c:out value="${__wp_admin_edit_form_advanced_block52}" escapeXml="false" /></a></li>
	<li class="wp-no-js-hidden"><a href="#categories-pop" tabindex="3"><c:out value="${__wp_admin_edit_form_advanced_block53}" escapeXml="false" /></a></li>
</ul>

<div id="categories-pop" class="ui-tabs-panel" style="display: none;">
	<ul id="categorychecklist-pop" class="categorychecklist form-no-clear" >
		<c:out value="${__wp_admin_edit_form_advanced_block54}" escapeXml="false" />	</ul>
</div>

<div id="categories-all" class="ui-tabs-panel">
	<ul id="categorychecklist" class="list:category categorychecklist form-no-clear">
		<c:out value="${__wp_admin_edit_form_advanced_block55}" escapeXml="false" />	</ul>
</div>

</div>
</div>

<c:out value="${__wp_admin_edit_form_advanced_block56}" escapeXml="false" />
<c:out value="${__wp_admin_edit_form_advanced_block57}" escapeXml="false" />
<h2><c:out value="${__wp_admin_edit_form_advanced_block58}" escapeXml="false" /></h2>

<div id="postexcerpt" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block59}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block60}" escapeXml="false" /></h3>
<div class="inside"><textarea rows="1" cols="40" name="excerpt" tabindex="6" id="excerpt"><c:out value="${__wp_admin_edit_form_advanced_block61}" escapeXml="false" /></textarea>
<p><c:out value="${__wp_admin_edit_form_advanced_block62}" escapeXml="false" /></p>
</div>
</div>

<div id="trackbacksdiv" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block63}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block64}" escapeXml="false" /></h3>
<div class="inside">
<p><c:out value="${__wp_admin_edit_form_advanced_block65}" escapeXml="false" /> <c:out value="${__wp_admin_edit_form_advanced_block66}" escapeXml="false" /><br /> (<c:out value="${__wp_admin_edit_form_advanced_block67}" escapeXml="false" />)</p>
<p><c:out value="${__wp_admin_edit_form_advanced_block68}" escapeXml="false" /></p>
<c:out value="${__wp_admin_edit_form_advanced_block69}" escapeXml="false" /></div>
</div>

<div id="postcustom" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block70}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block71}" escapeXml="false" /></h3>
<div class="inside">
<div id="postcustomstuff">
<table cellpadding="3">
<c:out value="${__wp_admin_edit_form_advanced_block72}" escapeXml="false" />
</table>
<c:out value="${__wp_admin_edit_form_advanced_block73}" escapeXml="false" /><div id="ajax-response"></div>
</div>
<p><c:out value="${__wp_admin_edit_form_advanced_block74}" escapeXml="false" /></p>
</div>
</div>

<c:out value="${__wp_admin_edit_form_advanced_block75}" escapeXml="false" />
<div id="commentstatusdiv" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block76}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block77}" escapeXml="false" /></h3>
<div class="inside">
<input name="advanced_view" type="hidden" value="1" />
<p><label for="comment_status" class="selectit">
<input name="comment_status" type="checkbox" id="comment_status" value="open" <c:out value="${__wp_admin_edit_form_advanced_block78}" escapeXml="false" /> />
<c:out value="${__wp_admin_edit_form_advanced_block79}" escapeXml="false" /></label></p>
<p><label for="ping_status" class="selectit"><input name="ping_status" type="checkbox" id="ping_status" value="open" <c:out value="${__wp_admin_edit_form_advanced_block80}" escapeXml="false" /> /> <c:out value="${__wp_admin_edit_form_advanced_block81}" escapeXml="false" /></label></p>
<p><c:out value="${__wp_admin_edit_form_advanced_block82}" escapeXml="false" /></p>
</div>
</div>

<div id="passworddiv" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block83}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block84}" escapeXml="false" /></h3>
<div class="inside">
<p><input name="post_password" type="text" size="25" id="post_password" value="<c:out value="${__wp_admin_edit_form_advanced_block85}" escapeXml="false" />" /></p>
<p><c:out value="${__wp_admin_edit_form_advanced_block86}" escapeXml="false" /></p>
</div>
</div>

<div id="slugdiv" class="postbox <c:out value="${__wp_admin_edit_form_advanced_block87}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_form_advanced_block88}" escapeXml="false" /></h3>
<div class="inside">
<input name="post_name" type="text" size="13" id="post_name" value="<c:out value="${__wp_admin_edit_form_advanced_block89}" escapeXml="false" />" />
</div>
</div>

<c:out value="${__wp_admin_edit_form_advanced_block90}" escapeXml="false" />
<c:out value="${__wp_admin_edit_form_advanced_block91}" escapeXml="false" />
<c:out value="${__wp_admin_edit_form_advanced_block92}" escapeXml="false" /></div>
</div>

</div>

</form>

<c:out value="${__wp_admin_edit_form_advanced_block93}" escapeXml="false" />