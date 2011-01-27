<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_options_permalink_block1}" escapeXml="false" />
<c:out value="${__wp_admin_options_permalink_block2}" escapeXml="false" />
<div class="wrap">
  <h2><c:out value="${__wp_admin_options_permalink_block3}" escapeXml="false" /></h2>
<form name="form" action="options-permalink.php" method="post">
<c:out value="${__wp_admin_options_permalink_block4}" escapeXml="false" />  <p><c:out value="${__wp_admin_options_permalink_block5}" escapeXml="false" /></p>

<c:out value="${__wp_admin_options_permalink_block6}" escapeXml="false" /><h3><c:out value="${__wp_admin_options_permalink_block7}" escapeXml="false" /></h3>
<table class="form-table">
	<tr>
		<th><label><input name="selection" type="radio" value="" class="tog" <c:out value="${__wp_admin_options_permalink_block8}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_permalink_block9}" escapeXml="false" /></label></th>
		<td><code><c:out value="${__wp_admin_options_permalink_block10}" escapeXml="false" />/?p=123</code></td>
	</tr>
	<tr>
		<th><label><input name="selection" type="radio" value="<c:out value="${__wp_admin_options_permalink_block11}" escapeXml="false" />" class="tog" <c:out value="${__wp_admin_options_permalink_block12}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_permalink_block13}" escapeXml="false" /></label></th>
		<td><code><c:out value="${__wp_admin_options_permalink_block14}" escapeXml="false" /></code></td>
	</tr>
	<tr>
		<th><label><input name="selection" type="radio" value="<c:out value="${__wp_admin_options_permalink_block15}" escapeXml="false" />" class="tog" <c:out value="${__wp_admin_options_permalink_block16}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_permalink_block17}" escapeXml="false" /></label></th>
		<td><code><c:out value="${__wp_admin_options_permalink_block18}" escapeXml="false" /></code></td>
	</tr>
	<tr>
		<th><label><input name="selection" type="radio" value="<c:out value="${__wp_admin_options_permalink_block19}" escapeXml="false" />" class="tog" <c:out value="${__wp_admin_options_permalink_block20}" escapeXml="false" /> /> <c:out value="${__wp_admin_options_permalink_block21}" escapeXml="false" /></label></th>
		<td><code><c:out value="${__wp_admin_options_permalink_block22}" escapeXml="false" />/archives/123</code></td>
	</tr>
	<tr>
		<th>
			<label><input name="selection" id="custom_selection" type="radio" value="custom" class="tog"
			<c:out value="${__wp_admin_options_permalink_block23}" escapeXml="false" />			 />
			<c:out value="${__wp_admin_options_permalink_block24}" escapeXml="false" />			</label>
		</th>
		<td>
			<input name="permalink_structure" id="permalink_structure" type="text" class="code" style="width: 60%;" value="<c:out value="${__wp_admin_options_permalink_block25}" escapeXml="false" />" size="50" />
		</td>
	</tr>
</table>

<h3><c:out value="${__wp_admin_options_permalink_block26}" escapeXml="false" /></h3>
<c:out value="${__wp_admin_options_permalink_block27}" escapeXml="false" />
<table class="form-table">
	<tr>
		<th><c:out value="${__wp_admin_options_permalink_block28}" escapeXml="false" /></th>
		<td><input name="category_base" id="category_base" type="text" class="code"  value="<c:out value="${__wp_admin_options_permalink_block29}" escapeXml="false" />" size="30" /></td>
	</tr>
	<tr>
		<th><c:out value="${__wp_admin_options_permalink_block30}" escapeXml="false" /></th>
		<td><input name="tag_base" id="tag_base" type="text" class="code"  value="<c:out value="${__wp_admin_options_permalink_block31}" escapeXml="false" />" size="30" /></td>
	</tr>
</table>
<p class="submit"><input type="submit" name="submit" class="button" value="<c:out value="${__wp_admin_options_permalink_block32}" escapeXml="false" />" /></p>
  </form>
<c:out value="${__wp_admin_options_permalink_block33}" escapeXml="false" />
</div>

<c:out value="${__wp_admin_options_permalink_block34}" escapeXml="false" />