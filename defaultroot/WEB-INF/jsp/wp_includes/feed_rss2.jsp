<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><%@ page contentType="text/xml;charset=UTF-8" %><c:out value="${__wp_includes_feed_rss2_block1}" escapeXml="false" />
<rss version="2.0"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	xmlns:wfw="http://wellformedweb.org/CommentAPI/"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:atom="http://www.w3.org/2005/Atom"
	<c:out value="${__wp_includes_feed_rss2_block2}" escapeXml="false" />>

<channel>
	<title><c:out value="${__wp_includes_feed_rss2_block3}" escapeXml="false" /></title>
	<atom:link href="<c:out value="${__wp_includes_feed_rss2_block4}" escapeXml="false" />" rel="self" type="application/rss+xml" />
	<link><c:out value="${__wp_includes_feed_rss2_block5}" escapeXml="false" /></link>
	<description><c:out value="${__wp_includes_feed_rss2_block6}" escapeXml="false" /></description>
	<pubDate><c:out value="${__wp_includes_feed_rss2_block7}" escapeXml="false" /></pubDate>
	<c:out value="${__wp_includes_feed_rss2_block8}" escapeXml="false" />	<language><c:out value="${__wp_includes_feed_rss2_block9}" escapeXml="false" /></language>
	<c:out value="${__wp_includes_feed_rss2_block10}" escapeXml="false" />	<c:out value="${__wp_includes_feed_rss2_block11}" escapeXml="false" /></channel>
</rss>