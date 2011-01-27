<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_edit_link_form_block1}" escapeXml="false" />
<c:out value="${__wp_admin_edit_link_form_block2}" escapeXml="false" />
<div class="wrap">
<h2><c:out value="${__wp_admin_edit_link_form_block3}" escapeXml="false" /></h2>

<div id="poststuff">

<div class="submitbox" id="submitlink">

<div id="previewview">
<c:out value="${__wp_admin_edit_link_form_block4}" escapeXml="false" /></div>

<div class="inside">
<p><label for="link_private" class="selectit"><input id="link_private" name="link_visible" type="checkbox" value="N" <c:out value="${__wp_admin_edit_link_form_block5}" escapeXml="false" /> /> <c:out value="${__wp_admin_edit_link_form_block6}" escapeXml="false" /></label></p>
</div>

<p class="submit">
<input type="submit" class="button button-highlighted" name="save" value="<c:out value="${__wp_admin_edit_link_form_block7}" escapeXml="false" />" tabindex="4" />
<c:out value="${__wp_admin_edit_link_form_block8}" escapeXml="false" /></p>

<div class="side-info">
<h5><c:out value="${__wp_admin_edit_link_form_block9}" escapeXml="false" /></h5>

<ul>
<li><a href="link-manager.php"><c:out value="${__wp_admin_edit_link_form_block10}" escapeXml="false" /></a></li>
<li><a href="edit-link-categories.php"><c:out value="${__wp_admin_edit_link_form_block11}" escapeXml="false" /></a></li>
<li><a href="link-import.php"><c:out value="${__wp_admin_edit_link_form_block12}" escapeXml="false" /></a></li>
<c:out value="${__wp_admin_edit_link_form_block13}" escapeXml="false" /></ul>
</div>
<c:out value="${__wp_admin_edit_link_form_block14}" escapeXml="false" /></div>

<div id="post-body">
<div id="namediv" class="stuffbox">
<h3><c:out value="${__wp_admin_edit_link_form_block15}" escapeXml="false" /></h3>
<div class="inside">
	<input type="text" name="link_name" size="30" tabindex="1" value="<c:out value="${__wp_admin_edit_link_form_block16}" escapeXml="false" />" id="link_name" /><br />
    <c:out value="${__wp_admin_edit_link_form_block17}" escapeXml="false" /></div>
</div>

<div id="addressdiv" class="stuffbox">
<h3><c:out value="${__wp_admin_edit_link_form_block18}" escapeXml="false" /></h3>
<div class="inside">
	<input type="text" name="link_url" size="30" tabindex="1" value="<c:out value="${__wp_admin_edit_link_form_block19}" escapeXml="false" />" id="link_url" /><br />
    <c:out value="${__wp_admin_edit_link_form_block20}" escapeXml="false" /></div>
</div>

<div id="descriptiondiv" class="stuffbox">
<h3><c:out value="${__wp_admin_edit_link_form_block21}" escapeXml="false" /></h3>
<div class="inside">
	<input type="text" name="link_description" size="30" tabindex="1" value="<c:out value="${__wp_admin_edit_link_form_block22}" escapeXml="false" />" id="link_description" /><br />
    <c:out value="${__wp_admin_edit_link_form_block23}" escapeXml="false" /></div>
</div>

<div id="linkcategorydiv" class="postbox <c:out value="${__wp_admin_edit_link_form_block24}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_link_form_block25}" escapeXml="false" /></h3>
<div class="inside">

<div id="category-adder" class="wp-hidden-children">
	<h4><a id="category-add-toggle" href="#category-add"><c:out value="${__wp_admin_edit_link_form_block26}" escapeXml="false" /></a></h4>
	<p id="link-category-add" class="wp-hidden-child">
		<input type="text" name="newcat" id="newcat" class="form-required form-input-tip" value="<c:out value="${__wp_admin_edit_link_form_block27}" escapeXml="false" />" />
		<input type="button" id="category-add-sumbit" class="add:categorychecklist:linkcategorydiv button" value="<c:out value="${__wp_admin_edit_link_form_block28}" escapeXml="false" />" />
		<c:out value="${__wp_admin_edit_link_form_block29}" escapeXml="false" />		<span id="category-ajax-response"></span>
	</p>
</div>

<ul id="category-tabs">
	<li class="ui-tabs-selected"><a href="#categories-all"><c:out value="${__wp_admin_edit_link_form_block30}" escapeXml="false" /></a></li>
	<li class="wp-no-js-hidden"><a href="#categories-pop"><c:out value="${__wp_admin_edit_link_form_block31}" escapeXml="false" /></a></li>
</ul>

<div id="categories-all" class="ui-tabs-panel">
	<ul id="categorychecklist" class="list:category categorychecklist form-no-clear">
		<c:out value="${__wp_admin_edit_link_form_block32}" escapeXml="false" />	</ul>
</div>

<div id="categories-pop" class="ui-tabs-panel" style="display: none;">
	<ul id="categorychecklist-pop" class="categorychecklist form-no-clear">
		<c:out value="${__wp_admin_edit_link_form_block33}" escapeXml="false" />	</ul>
</div>

</div>
</div>

<c:out value="${__wp_admin_edit_link_form_block34}" escapeXml="false" />
<h2><c:out value="${__wp_admin_edit_link_form_block35}" escapeXml="false" /></h2>

<div id="linktargetdiv" class="postbox <c:out value="${__wp_admin_edit_link_form_block36}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_link_form_block37}" escapeXml="false" /></h3>
<div class="inside">
<label for="link_target_blank" class="selectit">
<input id="link_target_blank" type="radio" name="link_target" value="_blank" <c:out value="${__wp_admin_edit_link_form_block38}" escapeXml="false" /> />
<code>_blank</code></label><br />
<label for="link_target_top" class="selectit">
<input id="link_target_top" type="radio" name="link_target" value="_top" <c:out value="${__wp_admin_edit_link_form_block39}" escapeXml="false" /> />
<code>_top</code></label><br />
<label for="link_target_none" class="selectit">
<input id="link_target_none" type="radio" name="link_target" value="" <c:out value="${__wp_admin_edit_link_form_block40}" escapeXml="false" /> />
<c:out value="${__wp_admin_edit_link_form_block41}" escapeXml="false" /></label>
<p><c:out value="${__wp_admin_edit_link_form_block42}" escapeXml="false" /></p>
</div>
</div>

<div id="linkxfndiv" class="postbox <c:out value="${__wp_admin_edit_link_form_block43}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_link_form_block44}" escapeXml="false" /></h3>
<div class="inside">
<table class="editform" style="width: 100%;" cellspacing="2" cellpadding="5">
	<tr>
		<th style="width: 20%;" scope="row"><c:out value="${__wp_admin_edit_link_form_block45}" escapeXml="false" /></th>
		<td style="width: 80%;"><input type="text" name="link_rel" id="link_rel" size="50" value="<c:out value="${__wp_admin_edit_link_form_block46}" escapeXml="false" />" /></td>
	</tr>
	<tr>
		<td colspan="2">
			<table cellpadding="3" cellspacing="5" class="form-table">
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block47}" escapeXml="false" /> </th>
					<td>
						<label for="me">
						<input type="checkbox" name="identity" value="me" id="me" <c:out value="${__wp_admin_edit_link_form_block48}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block49}" escapeXml="false" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block50}" escapeXml="false" /> </th>
					<td>
						<label for="contact">
						<input class="valinp" type="radio" name="friendship" value="contact" id="contact" <c:out value="${__wp_admin_edit_link_form_block51}" escapeXml="false" /> /> <c:out value="${__wp_admin_edit_link_form_block52}" escapeXml="false" /></label>
						<label for="acquaintance">
						<input class="valinp" type="radio" name="friendship" value="acquaintance" id="acquaintance" <c:out value="${__wp_admin_edit_link_form_block53}" escapeXml="false" /> />  <c:out value="${__wp_admin_edit_link_form_block54}" escapeXml="false" /></label>
						<label for="friend">
						<input class="valinp" type="radio" name="friendship" value="friend" id="friend" <c:out value="${__wp_admin_edit_link_form_block55}" escapeXml="false" /> /> <c:out value="${__wp_admin_edit_link_form_block56}" escapeXml="false" /></label>
						<label for="friendship">
						<input name="friendship" type="radio" class="valinp" value="" id="friendship" <c:out value="${__wp_admin_edit_link_form_block57}" escapeXml="false" /> /> <c:out value="${__wp_admin_edit_link_form_block58}" escapeXml="false" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block59}" escapeXml="false" /> </th>
					<td>
						<label for="met">
						<input class="valinp" type="checkbox" name="physical" value="met" id="met" <c:out value="${__wp_admin_edit_link_form_block60}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block61}" escapeXml="false" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block62}" escapeXml="false" /> </th>
					<td>
						<label for="co-worker">
						<input class="valinp" type="checkbox" name="professional" value="co-worker" id="co-worker" <c:out value="${__wp_admin_edit_link_form_block63}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block64}" escapeXml="false" /></label>
						<label for="colleague">
						<input class="valinp" type="checkbox" name="professional" value="colleague" id="colleague" <c:out value="${__wp_admin_edit_link_form_block65}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block66}" escapeXml="false" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block67}" escapeXml="false" /> </th>
					<td>
						<label for="co-resident">
						<input class="valinp" type="radio" name="geographical" value="co-resident" id="co-resident" <c:out value="${__wp_admin_edit_link_form_block68}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block69}" escapeXml="false" /></label>
						<label for="neighbor">
						<input class="valinp" type="radio" name="geographical" value="neighbor" id="neighbor" <c:out value="${__wp_admin_edit_link_form_block70}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block71}" escapeXml="false" /></label>
						<label for="geographical">
						<input class="valinp" type="radio" name="geographical" value="" id="geographical" <c:out value="${__wp_admin_edit_link_form_block72}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block73}" escapeXml="false" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block74}" escapeXml="false" /> </th>
					<td>
						<label for="child">
						<input class="valinp" type="radio" name="family" value="child" id="child" <c:out value="${__wp_admin_edit_link_form_block75}" escapeXml="false" />  />
						<c:out value="${__wp_admin_edit_link_form_block76}" escapeXml="false" /></label>
						<label for="kin">
						<input class="valinp" type="radio" name="family" value="kin" id="kin" <c:out value="${__wp_admin_edit_link_form_block77}" escapeXml="false" />  />
						<c:out value="${__wp_admin_edit_link_form_block78}" escapeXml="false" /></label>
						<label for="parent">
						<input class="valinp" type="radio" name="family" value="parent" id="parent" <c:out value="${__wp_admin_edit_link_form_block79}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block80}" escapeXml="false" /></label>
						<label for="sibling">
						<input class="valinp" type="radio" name="family" value="sibling" id="sibling" <c:out value="${__wp_admin_edit_link_form_block81}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block82}" escapeXml="false" /></label>
						<label for="spouse">
						<input class="valinp" type="radio" name="family" value="spouse" id="spouse" <c:out value="${__wp_admin_edit_link_form_block83}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block84}" escapeXml="false" /></label>
						<label for="family">
						<input class="valinp" type="radio" name="family" value="" id="family" <c:out value="${__wp_admin_edit_link_form_block85}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block86}" escapeXml="false" /></label>
					</td>
				</tr>
				<tr>
					<th scope="row"> <c:out value="${__wp_admin_edit_link_form_block87}" escapeXml="false" /> </th>
					<td>
						<label for="muse">
						<input class="valinp" type="checkbox" name="romantic" value="muse" id="muse" <c:out value="${__wp_admin_edit_link_form_block88}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block89}" escapeXml="false" /></label>
						<label for="crush">
						<input class="valinp" type="checkbox" name="romantic" value="crush" id="crush" <c:out value="${__wp_admin_edit_link_form_block90}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block91}" escapeXml="false" /></label>
						<label for="date">
						<input class="valinp" type="checkbox" name="romantic" value="date" id="date" <c:out value="${__wp_admin_edit_link_form_block92}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block93}" escapeXml="false" /></label>
						<label for="romantic">
						<input class="valinp" type="checkbox" name="romantic" value="sweetheart" id="romantic" <c:out value="${__wp_admin_edit_link_form_block94}" escapeXml="false" /> />
						<c:out value="${__wp_admin_edit_link_form_block95}" escapeXml="false" /></label>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<p><c:out value="${__wp_admin_edit_link_form_block96}" escapeXml="false" /></p>
</div>
</div>

<div id="linkadvanceddiv" class="postbox <c:out value="${__wp_admin_edit_link_form_block97}" escapeXml="false" />">
<h3><c:out value="${__wp_admin_edit_link_form_block98}" escapeXml="false" /></h3>
<div class="inside">
<table class="form-table" style="width: 100%;" cellspacing="2" cellpadding="5">
	<tr class="form-field">
		<th valign="top"  scope="row"><label for="link_image"><c:out value="${__wp_admin_edit_link_form_block99}" escapeXml="false" /></label></th>
		<td><input type="text" name="link_image" id="link_image" size="50" value="<c:out value="${__wp_admin_edit_link_form_block100}" escapeXml="false" />" style="width: 95%" /></td>
	</tr>
	<tr class="form-field">
		<th valign="top"  scope="row"><label for="rss_uri"><c:out value="${__wp_admin_edit_link_form_block101}" escapeXml="false" /></label></th>
		<td><input name="link_rss" type="text" id="rss_uri" value="<c:out value="${__wp_admin_edit_link_form_block102}" escapeXml="false" />" size="50" style="width: 95%" /></td>
	</tr>
	<tr class="form-field">
		<th valign="top"  scope="row"><label for="link_notes"><c:out value="${__wp_admin_edit_link_form_block103}" escapeXml="false" /></label></th>
		<td><textarea name="link_notes" id="link_notes" cols="50" rows="10" style="width: 95%"><c:out value="${__wp_admin_edit_link_form_block104}" escapeXml="false" /></textarea></td>
	</tr>
	<tr class="form-field">
		<th valign="top"  scope="row"><label for="link_rating"><c:out value="${__wp_admin_edit_link_form_block105}" escapeXml="false" /></label></th>
		<td><select name="link_rating" id="link_rating" size="1">
		<c:out value="${__wp_admin_edit_link_form_block106}" escapeXml="false" /></select>&nbsp;<c:out value="${__wp_admin_edit_link_form_block107}" escapeXml="false" />		</td>
	</tr>
</table>
</div>
</div>

<c:out value="${__wp_admin_edit_link_form_block108}" escapeXml="false" />
<c:out value="${__wp_admin_edit_link_form_block109}" escapeXml="false" />
</div>
</div>

</div>

</form>
