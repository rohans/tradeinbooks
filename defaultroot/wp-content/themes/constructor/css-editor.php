<?php
/**
 * CSS Generator for WYSIWYG editor, please never change this is file, if your not sure what are you doing!
 *
 * @package WordPress
 * @subpackage Constructor
 */
session_start();
header('Content-type: text/css');

// debug
//error_reporting(E_ALL);

// config is null
$constructor = null;

// load custom theme (using theme switcher)
if (isset($_GET['theme'])) {
    $theme = $_GET['theme'];
    $theme = preg_replace('/[^a-z0-9\-\_]+/i', '', $theme);
    if (file_exists(dirname(__FILE__) . '/themes/'.$theme.'/config.php')) {
       $constructor = include dirname(__FILE__) . '/themes/'.$theme.'/config.php';
    }
}

if (!$constructor) {
    $constructor = include dirname(__FILE__) . '/themes/default/config.php';
}

if (isset($_SESSION['constructor_width'])) {
    $constructor['layout']['width'] = $_SESSION['constructor_width'];
}
if (isset($_SESSION['constructor_color'])) {
    $constructor['color'] = $_SESSION['constructor_color'];
}
if (isset($_SESSION['constructor_fonts'])) {
    $constructor['fonts'] = $_SESSION['constructor_fonts'];
}


$color1   = $constructor['color']['header1'];
$color2   = $constructor['color']['header2'];
$color3   = $constructor['color']['header3'];

$color_bg      = $constructor['color']['bg'];
$color_bg2     = $constructor['color']['bg2'];
$color_form    = $constructor['color']['form'];
$color_text    = $constructor['color']['text'];
$color_text2   = $constructor['color']['text2'];
$color_border  = $constructor['color']['border'];
$color_border2 = $constructor['color']['border2'];
$color_opacity = isset($constructor['color']['opacity'])?$constructor['color']['opacity']:'#ffffff';

/*Fonts*/

// detect font-face
$font_face = require dirname(__FILE__) .'/admin/font-face.php';
$include_fonts = array();
if (array_search($constructor['fonts']['title']['family'], $font_face) !== false) {
    $font = preg_split('/[,]+/', $constructor['fonts']['title']['family']);
    $font = urlencode(trim($font[0],'"'));
    array_push($include_fonts, $font);
}
if (array_search($constructor['fonts']['description']['family'], $font_face) !== false) {
    $font = preg_split('/[,]+/', $constructor['fonts']['description']['family']);
    $font = urlencode(trim($font[0],'"'));
    if (array_search($font, $include_fonts) === false) {
        array_push($include_fonts, $font);
    }
}
if (array_search($constructor['fonts']['header']['family'], $font_face) !== false) {
    $font = preg_split('/[,]+/', $constructor['fonts']['header']['family']);
    $font = urlencode(trim($font[0],'"'));
    if (array_search($font, $include_fonts) === false) {
        array_push($include_fonts, $font);
    }
}
if (array_search($constructor['fonts']['content']['family'], $font_face) !== false) {
    $font = preg_split('/[,]+/', $constructor['fonts']['content']['family']);
    $font = urlencode(trim($font[0],'"'));
    if (array_search($font, $include_fonts) === false) {
        array_push($include_fonts, $font);
    }
}
if (!empty($include_fonts)) {
    $font_face = '@import url(http://fonts.googleapis.com/css?family='.join('|',$include_fonts).');'."\n";
} else {
    $font_face = '';
}

$title_font = <<<CSS
    font-family:{$constructor['fonts']['title']['family']};
    font-size:{$constructor['fonts']['title']['size']}px;
    line-height:{$constructor['fonts']['title']['size']}px;
    font-weight:{$constructor['fonts']['title']['weight']};
    color:{$constructor['fonts']['title']['color']};
    text-transform:{$constructor['fonts']['title']['transform']};
CSS;

$description_font = <<<CSS
    font-family:{$constructor['fonts']['description']['family']};
    font-size:{$constructor['fonts']['description']['size']}px;
    line-height:{$constructor['fonts']['description']['size']}px;
    font-weight:{$constructor['fonts']['description']['weight']};
    color:{$constructor['fonts']['description']['color']};
    text-transform:{$constructor['fonts']['description']['transform']};
CSS;

$body_font = <<<CSS
    font-family:{$constructor['fonts']['content']['family']};
CSS;

$header_font = <<<CSS
    font-family:{$constructor['fonts']['header']['family']};
CSS;

$content_font = <<<CSS
    font-family:{$constructor['fonts']['content']['family']};
CSS;

/*/Fonts*/
/* Output CSS */
echo <<<CSS
{$font_face}
/*MCE*/
html .mceContentBody {
	max-width:{$constructor['layout']['width']}px;
}
body, .mceWPmore {
    background-color:{$color_bg};
}
/*Content*/
* {
	font-family:{$constructor['fonts']['content']['family']};
	color:{$color_text};
    background-color:{$color_bg};
	line-height: 1.5;
}
p,dl,td,th,ul,ol,blockquote {
	font-size: 16px;
}
body, input, textarea {
	font-size: 12px;
	line-height: 18px;
}
hr {
	background-color: {$color1};
	border:0;
	height: 1px;
	margin-bottom: 1em;
	clear:both;
}


h1,h2,h3,h4,h5,h6 {{$header_font}}

h1,
h2 { color:{$color1} }
h3,
h4 { color:{$color2} }
h5,
h6 { color:{$color3} }

pre { font-family:{$constructor['fonts']['content']['family']}; }

/*Form*/
input, select, textarea {
    font-size:1.4em;
    padding: 4px;
    border: {$color_border} 1px solid;
    color:{$color_text};
    background-color:{$color_form}
}
input:active, select:active, textarea:active {
    border-color:{$color3};
    background-color:{$color_bg2}
}

input:focus, select:focus, textarea:focus {
    border-color:{$color3};
    background-color:{$color_bg2}
}
fieldset{
    border-color: {$color_border} 1px solid;
    padding: 8px
}
textarea {width: 98%}


/*/Form*/
/*Table*/
table {
    border-collapse:collapse
}

table caption {
    color:{$color2};
}
th {
    font-size:1.2em;
    padding:4px 6px;
    color:{$color_text};
    background-color:{$color3};
    border:{$color_border} 1px solid
}
td {
    padding:4px;
    border:{$color_border} 1px solid
}
/*/Table*/
/*Images*/
.wp-caption {
    text-align: center;
    padding-top: 4px;
    margin: 10px;
    color:{$color_text};
    border: 1px solid {$color_border};
    background-color: {$color_bg2};
}
.wp-caption a {
    border: 0 none !important;
}
.wp-caption img {
    margin: 0 !important;
    padding: 0 !important;
    border: 0 none !important;
}
.wp-caption p.wp-caption-text {
    font-size: 1em;
    line-height: 17px;
    padding: 4px 0;
    text-indent:0;
    margin: 0
    color:{$color_text};
}
.gallery-caption {
   color:{$color_text};
}
.wp-smiley {
	margin:0;
}
/*/Images*/
/*Post*/
p {
    text-indent:12px;
    margin-bottom:4px
}
h1, h2, h3, h4, h5, h6,
ul, ol {
    margin-left:12px;
}
ol, ul {
    padding-left:20px
}
li ol, li ul {
    padding-left:6px
}
ul {
    list-style:circle
}
ol {
    list-style: decimal
}
li {
    padding:2px;
}

a {
    outline:none;
    text-decoration:none;
    color:{$color_text};
    border-bottom:1px dotted {$color_text}
}
a:hover {
    color:{$color1};
    border-bottom:1px solid {$color1}
}

h2 a{
   color: {$color_bg};
}
h2 a:hover{
   color: {$color_bg2};
}
img {
    border:1px solid {$color_border};
    padding:4px;
}
img.alignleft {
    margin: 0 4px 4px 0
}
img.alignright {
    margin: 0 4px 0 4px
}
/*/Post*/
CSS;
?>
