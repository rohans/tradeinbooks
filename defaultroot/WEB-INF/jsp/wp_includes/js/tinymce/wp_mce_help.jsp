<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java"%>

<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block1}" escapeXml="false" /><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" <c:out value="${__wp_includes_js_tinymce_wp_mce_help_block2}" escapeXml="false" />>
<head>
<meta http-equiv="Content-Type" content="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block3}" escapeXml="false" />; charset=<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block4}" escapeXml="false" />" />
<title><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block5}" escapeXml="false" /></title>
<script type="text/javascript" src="tiny_mce_popup.js"></script>
<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block6}" escapeXml="false" /><style type="text/css">
	#wphead {
		font-size: 80%;
		border-top: 0;
		color:#555;
		background-color: #e4f2fd;
	}
	#wphead h1 {
		font-size: 32px;
		color: #555;
		margin: 0;
		padding: 10px;
	}
	#adminmenu {
		padding-top: 2px;
		padding-left: 15px;
		background-color: #e4f2fd;
		border-color: #C6D9E9;
	}
	#adminmenu a.current {
		background-color: #fff;
		border-color: #c6d9e9;
		border-bottom-color: #fff;
		color: #d54e21;
	}
	#adminmenu a {
		color: #2583AD;
		padding: 6px;
		border-width: 1px;
		border-style: solid solid none;
		border-color: #E4F2FD;
	}
	#adminmenu a:hover {
		color: #d54e21;
	}
	.wrap h2 {
		border-bottom-color:#DADADA;
		color:#666666;
		margin: 12px 0;
		padding: 0;
	}
	#user_info {
		right: 5%;
		top: 5px;
	}
	h3 {
		font-size: 1.1em;
		margin-top: 20px;
		margin-bottom: 0px;
	}
	#flipper {
		margin: 0;
		padding: 5px 20px 10px;
		background-color: #fff;
		border-left: 1px solid #c6d9e9;
		border-bottom: 1px solid #c6d9e9;
	}
	* html {
        overflow-x: hidden;
        overflow-y: scroll;
    }
	#flipper div p {
		margin-top: 0.4em;
		margin-bottom: 0.8em;
		text-align: justify;
	}
	th {
		text-align: center;
	}
	.top th {
		text-decoration: underline;
	}
	.top .key {
		text-align: center;
		width: 36px;
	}
	.top .action {
		text-align: left;
	}
	.align {
		border-left: 3px double #333;
		border-right: 3px double #333;
	}
	.keys {
		margin-bottom: 15px;
	}
	.keys p {
		display: inline-block;
		margin: 0px;
		padding: 0px;
	}
	.keys .left { text-align: left; }
	.keys .center { text-align: center; }
	.keys .right { text-align: right; }
	td b {
		font-family: "Times New Roman" Times serif;
	}
	#buttoncontainer {
		text-align: center;
		margin-bottom: 20px;
	}
	#buttoncontainer a, #buttoncontainer a:hover {
		border-bottom: 0px;
	}
</style>
<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block7}" escapeXml="false" /><script type="text/javascript">
	function d(id) { return document.getElementById(id); }

	function flipTab(n) {
		for (i=1;i<=4;i++) {
			c = d('content'+i.toString());
			t = d('tab'+i.toString());
			if ( n == i ) {
				c.className = '';
				t.className = 'current';
			} else {
				c.className = 'hidden';
				t.className = '';
			}
		}
	}

    function init() {
        document.getElementById('version').innerHTML = tinymce.majorVersion + "." + tinymce.minorVersion;
        document.getElementById('date').innerHTML = tinymce.releaseDate;
    }
    tinyMCEPopup.onInit.add(init);
</script>
</head>
<body>
<div class="zerosize"></div>
<div id="wphead"><h1><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block8}" escapeXml="false" /></h1></div>

<ul id="adminmenu">
	<li><a id="tab1" href="javascript:flipTab(1)" title="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block9}" escapeXml="false" />" accesskey="1" tabindex="1" class="current"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block10}" escapeXml="false" /></a></li>
	<li><a id="tab2" href="javascript:flipTab(2)" title="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block11}" escapeXml="false" />" accesskey="2" tabindex="2"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block12}" escapeXml="false" /></a></li>
	<li><a id="tab3" href="javascript:flipTab(3)" title="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block13}" escapeXml="false" />" accesskey="3" tabindex="3"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block14}" escapeXml="false" /></a></li>
	<li><a id="tab4" href="javascript:flipTab(4)" title="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block15}" escapeXml="false" />" accesskey="4" tabindex="4"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block16}" escapeXml="false" /></a></li>
</ul>

<div id="flipper" class="wrap">

<div id="content1">
	<h2><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block17}" escapeXml="false" /></h2>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block18}" escapeXml="false" /></p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block19}" escapeXml="false" /></p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block20}" escapeXml="false" /></p>
    <p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block21}" escapeXml="false" /></p>
</div>

<div id="content2" class="hidden">
	<h2><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block22}" escapeXml="false" /></h2>
	<h3><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block23}" escapeXml="false" /></h3>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block24}" escapeXml="false" /></p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block25}" escapeXml="false" /></p>
	<h3><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block26}" escapeXml="false" /></h3>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block27}" escapeXml="false" /></p>
	<h3><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block28}" escapeXml="false" /></h3>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block29}" escapeXml="false" /></p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block30}" escapeXml="false" /></p>
</div>

<div id="content3" class="hidden">
	<h2><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block31}" escapeXml="false" /></h2>
    <p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block32}" escapeXml="false" /></p>
	<table class="keys" width="100%" style="border: 0 none;">
		<tr class="top"><th class="key center"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block33}" escapeXml="false" /></th><th class="left"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block34}" escapeXml="false" /></th><th class="key center"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block35}" escapeXml="false" /></th><th class="left"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block36}" escapeXml="false" /></th></tr>
		<tr><th>c</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block37}" escapeXml="false" /></td><th>v</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block38}" escapeXml="false" /></td></tr>
		<tr><th>a</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block39}" escapeXml="false" /></td><th>x</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block40}" escapeXml="false" /></td></tr>
		<tr><th>z</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block41}" escapeXml="false" /></td><th>y</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block42}" escapeXml="false" /></td></tr>
		<script type="text/javascript">
		if ( ! tinymce.isWebKit )
			document.write("<tr><th>b</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block43}" escapeXml="false" /></td><th>i</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block44}" escapeXml="false" /></td></tr>"+
			"<tr><th>u</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block45}" escapeXml="false" /></td><th>1</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block46}" escapeXml="false" /></td></tr>"+
			"<tr><th>2</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block47}" escapeXml="false" /></td><th>3</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block48}" escapeXml="false" /></td></tr>"+
			"<tr><th>4</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block49}" escapeXml="false" /></td><th>5</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block50}" escapeXml="false" /></td></tr>"+
			"<tr><th>6</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block51}" escapeXml="false" /></td><th>9</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block52}" escapeXml="false" /></td></tr>")
		</script>
	</table>
	
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block53}" escapeXml="false" /></p>
	<table class="keys" width="100%" style="border: 0 none;">
		<tr class="top"><th class="key center"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block54}" escapeXml="false" /></th><th class="left"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block55}" escapeXml="false" /></th><th class="key center"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block56}" escapeXml="false" /></th><th class="left"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block57}" escapeXml="false" /></th></tr>
		<script type="text/javascript">
		if ( tinymce.isWebKit )
			document.write("<tr><th>b</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block58}" escapeXml="false" /></td><th>i</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block59}" escapeXml="false" /></td></tr>")
		</script>
		<tr><th>n</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block60}" escapeXml="false" /></td><th>l</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block61}" escapeXml="false" /></td></tr>
		<tr><th>j</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block62}" escapeXml="false" /></td><th>c</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block63}" escapeXml="false" /></td></tr>
		<tr><th>d</th><td><span style="text-decoration: line-through;"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block64}" escapeXml="false" /></span></td><th>r</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block65}" escapeXml="false" /></td></tr>
		<tr><th>u</th><td><strong>&bull;</strong> <c:out value="${__wp_includes_js_tinymce_wp_mce_help_block66}" escapeXml="false" /></td><th>a</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block67}" escapeXml="false" /></td></tr>
		<tr><th>o</th><td>1. <c:out value="${__wp_includes_js_tinymce_wp_mce_help_block68}" escapeXml="false" /></td><th>s</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block69}" escapeXml="false" /></td></tr>
		<tr><th>q</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block70}" escapeXml="false" /></td><th>m</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block71}" escapeXml="false" /></td></tr>
		<tr><th>g</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block72}" escapeXml="false" /></td><th>t</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block73}" escapeXml="false" /></td></tr>
		<tr><th>p</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block74}" escapeXml="false" /></td><th>h</th><td><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block75}" escapeXml="false" /></td></tr>
		<tr><th>e</th><td colspan="3"><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block76}" escapeXml="false" /></td></tr>
	</table>
</div>

<div id="content4" class="hidden">
	<h2><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block77}" escapeXml="false" /></h2>

    <p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block78}" escapeXml="false" /> <span id="version"></span> (<span id="date"></span>)</p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block79}" escapeXml="false" /></p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block80}" escapeXml="false" /></p>
	<p><c:out value="${__wp_includes_js_tinymce_wp_mce_help_block81}" escapeXml="false" /></p>

	<div id="buttoncontainer">
		<a href="http://www.moxiecode.com" target="_new"><img src="themes/advanced/img/gotmoxie.png" alt="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block82}" escapeXml="false" />" style="border: none;" /></a>
		<a href="http://sourceforge.net/projects/tinymce/" target="_blank"><img src="themes/advanced/img/sflogo.png" alt="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block83}" escapeXml="false" />" style="border: none;" /></a>
		<a href="http://www.freshmeat.net/projects/tinymce" target="_blank"><img src="themes/advanced/img/fm.gif" alt="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block84}" escapeXml="false" />" style="border: none;" /></a>
	</div>

</div>
</div>

<div class="mceActionPanel">
	<div style="margin: 8px auto; text-align: center;padding-bottom: 10px;">
		<input type="button" id="cancel" name="cancel" value="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block85}" escapeXml="false" />" title="<c:out value="${__wp_includes_js_tinymce_wp_mce_help_block86}" escapeXml="false" />" onclick="tinyMCEPopup.close();" />
	</div>
</div>

</body>
</html>
