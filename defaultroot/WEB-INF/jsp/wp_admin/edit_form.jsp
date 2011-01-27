<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>


<div class="wrap">
<h2><c:out value="${__wp_admin_edit_form_block1}" escapeXml="false" /></h2>
<form name="post" action="post.php" method="post" id="simple">

<c:out value="${__wp_admin_edit_form_block2}" escapeXml="false" /><input type="hidden" id="user-id" name="user_ID" value="<c:out value="${__wp_admin_edit_form_block3}" escapeXml="false" />" />
<input type="hidden" name="action" value='post' />

<div id="poststuff">
    <fieldset id="titlediv">
      <legend><a href="http://wordpress.org/docs/reference/post/#title" title="<c:out value="${__wp_admin_edit_form_block4}" escapeXml="false" />"><c:out value="${__wp_admin_edit_form_block5}" escapeXml="false" /></a></legend>
	  <div><input type="text" name="post_title" size="30" tabindex="1" value="<c:out value="${__wp_admin_edit_form_block6}" escapeXml="false" />" id="title" /></div>
    </fieldset>

    <fieldset id="categorydiv">
      <legend><a href="http://wordpress.org/docs/reference/post/#category" title="<c:out value="${__wp_admin_edit_form_block7}" escapeXml="false" />"><c:out value="${__wp_admin_edit_form_block8}" escapeXml="false" /></a></legend>
	  <div><c:out value="${__wp_admin_edit_form_block9}" escapeXml="false" /></div>
    </fieldset>

<br />
<fieldset id="postdiv">
    <legend><a href="http://wordpress.org/docs/reference/post/#post" title="<c:out value="${__wp_admin_edit_form_block10}" escapeXml="false" />"><c:out value="${__wp_admin_edit_form_block11}" escapeXml="false" /></a></legend>
<c:out value="${__wp_admin_edit_form_block12}" escapeXml="false" /><div><textarea rows="<c:out value="${__wp_admin_edit_form_block13}" escapeXml="false" />" cols="40" name="content" tabindex="4" id="content"><c:out value="${__wp_admin_edit_form_block14}" escapeXml="false" /></textarea></div>
<c:out value="${__wp_admin_edit_form_block15}" escapeXml="false" /></fieldset>


<script type="text/javascript">
<!--
edCanvas = document.getElementById('content');
//-->
</script>

<input type="hidden" name="post_pingback" value="<c:out value="${__wp_admin_edit_form_block16}" escapeXml="false" />" id="post_pingback" />

<p><label for="trackback"> <c:out value="${__wp_admin_edit_form_block17}" escapeXml="false" /><br />	<input type="text" name="trackback_url" style="width: 360px" id="trackback" tabindex="7" /></p>

<p class="submit"><input name="saveasdraft" type="submit" id="saveasdraft" tabindex="9" value="<c:out value="${__wp_admin_edit_form_block18}" escapeXml="false" />" />
	<input name="saveasprivate" type="submit" id="saveasprivate" tabindex="10" value="<c:out value="${__wp_admin_edit_form_block19}" escapeXml="false" />" />

	 <c:out value="${__wp_admin_edit_form_block20}" escapeXml="false" />
<c:out value="${__wp_admin_edit_form_block21}" escapeXml="false" />	<input name="referredby" type="hidden" id="referredby" value="<c:out value="${__wp_admin_edit_form_block22}" escapeXml="false" />" />
</p>

<c:out value="${__wp_admin_edit_form_block23}" escapeXml="false" />
</div>
</form>

<script type="text/javascript">
try{document.getElementById('title').focus();}catch(e){}
</script>
</div>
