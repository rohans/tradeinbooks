<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_includes_feed_rss_block1}" escapeXml="false" /><rss version="0.92">
<channel>
	<title><c:out value="${__wp_includes_feed_rss_block2}" escapeXml="false" /></title>
	<link><c:out value="${__wp_includes_feed_rss_block3}" escapeXml="false" /></link>
	<description><c:out value="${__wp_includes_feed_rss_block4}" escapeXml="false" /></description>
	<lastBuildDate><c:out value="${__wp_includes_feed_rss_block5}" escapeXml="false" /></lastBuildDate>
	<docs>http://backend.userland.com/rss092</docs>
	<language><c:out value="${__wp_includes_feed_rss_block6}" escapeXml="false" /></language>
	<c:out value="${__wp_includes_feed_rss_block7}" escapeXml="false" />
<c:out value="${__wp_includes_feed_rss_block8}" escapeXml="false" /></channel>
</rss>
