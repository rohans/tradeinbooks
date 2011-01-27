<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_admin_widgets_block1}" escapeXml="false" />
<div class="wrap">

	<form id="widgets-filter" action="" method="get">

	<h2><c:out value="${__wp_admin_widgets_block2}" escapeXml="false" /></h2>
	<p id="widget-search">
		<input type="text" id="widget-search-input" name="s" value="<c:out value="${__wp_admin_widgets_block3}" escapeXml="false" />" />
		<input type="submit" class="button" value="<c:out value="${__wp_admin_widgets_block4}" escapeXml="false" />" />
	</p>

	<div class="widget-liquid-left-holder">
	<div id="available-widgets-filter" class="widget-liquid-left">
		<h3><c:out value="${__wp_admin_widgets_block5}" escapeXml="false" /></h3>
		<div class="nav">
			<select name="show">
<c:out value="${__wp_admin_widgets_block6}" escapeXml="false" />			</select>
			<input type="submit" value="<c:out value="${__wp_admin_widgets_block7}" escapeXml="false" />" class="button-secondary" />
			<p class="pagenav">
				<c:out value="${__wp_admin_widgets_block8}" escapeXml="false" />			</p>
		</div>
	</div>
	</div>

	<div id="available-sidebars" class="widget-liquid-right">
		<h3><c:out value="${__wp_admin_widgets_block9}" escapeXml="false" /></h3>

		<div class="nav">
			<select id="sidebar-selector" name="sidebar">
<c:out value="${__wp_admin_widgets_block10}" escapeXml="false" />			</select>
			<input type="submit" value="<c:out value="${__wp_admin_widgets_block11}" escapeXml="false" />" class="button-secondary" />
		</div>

	</div>

	</form>

	<div id="widget-content" class="widget-liquid-left-holder">

		<div id="available-widgets" class="widget-liquid-left">

			<c:out value="${__wp_admin_widgets_block12}" escapeXml="false" />
			<div class="nav">
				<p class="pagenav">
					<c:out value="${__wp_admin_widgets_block13}" escapeXml="false" />				</p>
			</div>
		</div>
	</div>

	<form id="widget-controls" action="" method="post">

	<div id="current-widgets-head" class="widget-liquid-right">

		<div id="sidebar-info">
			<p><c:out value="${__wp_admin_widgets_block14}" escapeXml="false" /></p>
			<p><c:out value="${__wp_admin_widgets_block15}" escapeXml="false" /></p>
		</div>

	</div>

	<div id="current-widgets" class="widget-liquid-right">
		<div id="current-sidebar">

			<c:out value="${__wp_admin_widgets_block16}" escapeXml="false" />
		</div>

		<p class="submit">
			<input type="hidden" id='sidebar' name='sidebar' value="<c:out value="${__wp_admin_widgets_block17}" escapeXml="false" />" />
			<input type="hidden" id="generated-time" name="generated-time" value="<c:out value="${__wp_admin_widgets_block18}" escapeXml="false" />" />
			<input type="submit" name="save-widgets" value="<c:out value="${__wp_admin_widgets_block19}" escapeXml="false" />" />
<c:out value="${__wp_admin_widgets_block20}" escapeXml="false" />		</p>
	</div>

	</form>

</div>

<c:out value="${__wp_admin_widgets_block21}" escapeXml="false" />
<br class="clear" />

<c:out value="${__wp_admin_widgets_block22}" escapeXml="false" />
