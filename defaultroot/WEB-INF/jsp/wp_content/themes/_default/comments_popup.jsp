<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_content_themes__default_comments_popup_block1}" escapeXml="false" />
<!-- // this is just the end of the motor - don't touch that line either :) -->
<p class="credit"><c:out value="${__wp_content_themes__default_comments_popup_block2}" escapeXml="false" /> <cite>Powered by <a href="http://wordpress.org/" title="Powered by WordPress, state-of-the-art semantic personal publishing platform"><strong>WordPress</strong></a></cite></p>
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
