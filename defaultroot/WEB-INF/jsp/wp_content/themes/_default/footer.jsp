<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>


<hr />
<div id="footer">
<!-- If you'd like to support WordPress, having the "powered by" link somewhere on your blog is the best way; it's our only promotion or advertising. -->
	<p>
		<c:out value="${__wp_content_themes__default_footer_block1}" escapeXml="false" /> is designed by
		<a href="http://www.nashtechnology.com">Nashtechnology, Inc</a>.
		<br /><a href="<c:out value="${__wp_content_themes__default_footer_block2}" escapeXml="false" />">Entries (RSS)</a>
		and <a href="<c:out value="${__wp_content_themes__default_footer_block3}" escapeXml="false" />">Comments (RSS)</a>.
		<!-- <c:out value="${__wp_content_themes__default_footer_block4}" escapeXml="false" /> queries. <c:out value="${__wp_content_themes__default_footer_block5}" escapeXml="false" /> seconds. -->
	</p>
</div>
</div>

<!-- Gorgeous design by Michael Heilemann - http://binarybonsai.com/kubrick/ -->

		<c:out value="${__wp_content_themes__default_footer_block6}" escapeXml="false" /></body>
</html>
