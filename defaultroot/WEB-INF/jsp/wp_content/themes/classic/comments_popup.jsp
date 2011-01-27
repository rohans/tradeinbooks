<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_content_themes_classic_comments_popup_block1}" escapeXml="false" />
<!-- // this is just the end of the motor - don't touch that line either :) -->
<p class="credit"><c:out value="${__wp_content_themes_classic_comments_popup_block2}" escapeXml="false" /> <c:out value="${__wp_content_themes_classic_comments_popup_block3}" escapeXml="false" /></p>
<script type="text/javascript">
<!--
document.onkeypress = function esc(e) {
	if(typeof(e) == "undefined") { e=event; }
	if (e.keyCode == 27) { self.close(); }
}
// -->
</script>
</body>
</html>
