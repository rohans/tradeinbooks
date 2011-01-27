<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_includes_feed_rdf_block1}" escapeXml="false" /><rdf:RDF
	xmlns="http://purl.org/rss/1.0/"
	xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
	xmlns:dc="http://purl.org/dc/elements/1.1/"
	xmlns:sy="http://purl.org/rss/1.0/modules/syndication/"
	xmlns:admin="http://webns.net/mvcb/"
	xmlns:content="http://purl.org/rss/1.0/modules/content/"
	<c:out value="${__wp_includes_feed_rdf_block2}" escapeXml="false" />>
<channel rdf:about="<c:out value="${__wp_includes_feed_rdf_block3}" escapeXml="false" />">
	<title><c:out value="${__wp_includes_feed_rdf_block4}" escapeXml="false" /></title>
	<link><c:out value="${__wp_includes_feed_rdf_block5}" escapeXml="false" /></link>
	<description><c:out value="${__wp_includes_feed_rdf_block6}" escapeXml="false" /></description>
	<dc:date><c:out value="${__wp_includes_feed_rdf_block7}" escapeXml="false" /></dc:date>
	<c:out value="${__wp_includes_feed_rdf_block8}" escapeXml="false" />	<sy:updatePeriod>hourly</sy:updatePeriod>
	<sy:updateFrequency>1</sy:updateFrequency>
	<sy:updateBase>2000-01-01T12:00+00:00</sy:updateBase>
	<c:out value="${__wp_includes_feed_rdf_block9}" escapeXml="false" />	<items>
		<rdf:Seq>
		<c:out value="${__wp_includes_feed_rdf_block10}" escapeXml="false" />		</rdf:Seq>
	</items>
</channel>
<c:out value="${__wp_includes_feed_rdf_block11}" escapeXml="false" /></rdf:RDF>
