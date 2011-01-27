<?php
/*
	Creates Global Meta Options for 'page' Post Types
*/
$meta_array = get_edit_page_post_array();

$page_settings = array(
		'id' => 'page-meta',
		'name' => THEMENAME." Page Template Options",
		'posttype' => 'page'
	);

$page_options =  new PageLinesMetaOptions($meta_array, $page_settings);

/*
	Creates Global Meta Options for 'post' Post Types
*/
$post_settings = array(
		'id' => 'post-meta',
		'name' => THEMENAME." Blog Post Options",
		'posttype' => 'post'
	);

$post_options =  new PageLinesMetaOptions($meta_array, $post_settings);

