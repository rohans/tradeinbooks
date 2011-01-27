<?php

// ==============================
// = PageLines Function Library =
// ==============================




/**
 * 
 *  Sets up global post ID and $post global for handling, reference and consistency
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 4.0.0
 *
 */
function pagelines_id_setup(){
	global $post;
	global $pagelines_ID;
	global $pagelines_post;

	if(isset($post) && is_object($post)){
		$pagelines_ID = $post->ID;
		$pagelines_post = $post;	
	}
	else {
		$pagelines_post = '';
		$pagelines_ID = '';
	}
	
}

/**
 * 
 *  Registered PageLines Hooks. Stores for reference or use elsewhere.
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 4.0.0
 *
 */
function pagelines_register_hook( $hook_name, $hook_area_id){
	

	if(is_admin()){
		/*
			Register The Hook
		*/
		
		global $registered_hooks;
		
		if( !isset($registered_hooks[$hook_area_id]) ) $registered_hooks[$hook_area_id] = array();
	
		if( isset($registered_hooks[$hook_area_id]) && is_array($registered_hooks[$hook_area_id]) ){
			$flipped_hooks = array_flip($registered_hooks[$hook_area_id]);
		}
	
		if( !isset($flipped_hooks[$hook_name]) ){
			$registered_hooks[$hook_area_id][] = $hook_name;
		}
		
	} else {
		
		/*
			Do The Hook
		*/
		do_action( $hook_name );
		
	}

}

/**
 * 
 *  Check the authentication level for administrator status (security)
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 1.x.x
 *
 */
function checkauthority(){
	if (!current_user_can('edit_themes'))
	wp_die('Sorry, but you don&#8217;t have the administrative privileges needed to do this.');
}

/**
 * 
 *  Checks for IE and Returns Version
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 4.0.0
 *
 */
function ie_version() {
  $match=preg_match('/MSIE ([0-9]\.[0-9])/',$_SERVER['HTTP_USER_AGENT'],$reg);
  if($match==0)
    return false;
  else
    return floatval($reg[1]);
}

/**
 * 
 *  Gets a 'tiny' url. Returns URL if fopen doesn't work
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 4.0.0
 *
 */
function getTinyUrl($url) {   
	if( ini_get('allow_url_fopen') ) {
		return file_get_contents("http://tinyurl.com/api-create.php?url=".$url);
	} else {
		return $url;
	}
}

/**
 * 
 *  Returns Current Layout Mode
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 4.0.0
 *
 */
function pagelines_layout_mode() {

	global $pagelines_layout;
	global $post;

	if(!pagelines_is_posts_page() && isset($post) && get_post_meta($post->ID, '_pagelines_layout_mode', true)){
		$pagelines_layout->build_layout(get_post_meta($post->ID, '_pagelines_layout_mode', true));
		return get_post_meta($post->ID, '_pagelines_layout_mode', true);
	} elseif(pagelines_is_posts_page() && pagelines_option('posts_page_layout')){
		$pagelines_layout->build_layout(pagelines_option('posts_page_layout'));
		return pagelines_option('posts_page_layout');
	} else {
		return $pagelines_layout->layout_mode;
	}

}

/**
 * 
 *  Checks if currently viewed page is part of BuddyPress Template
 *
 *  @package PageLines
 *  @subpackage BuddyPress Support
 *  @since 4.0
 *
 */
function pagelines_is_buddypress_page(){
	global $bp; 
	if(isset($bp) && isset($bp->current_component) && !empty($bp->current_component)){
		return true;
	}else{
		return false;
	}
}

/**
 * 
 *   Checks if BuddyPress is active
 *
 *  @package PageLines
 *  @subpackage BuddyPress Support
 *  @since 4.0
 *
 */
function pagelines_is_buddypress_active(){
	global $bp; 
	if(isset($bp)){
		return true;
	}else{
		return false;
	}
}


/**
 * Checks to see if there is more than one page for nav.
 * TODO does this add a query?
 * 
 * @since 4.0.0
 */
function show_posts_nav() {
	global $wp_query;
	return ($wp_query->max_num_pages > 1);
}


/**
 * Pulls a global identifier from a bbPress forum installation
 * @since 3.x.x
 */
function pagelines_bbpress_forum(){
	global $bbpress_forum;
	if($bbpress_forum ){
		return true;
	} else return false;
}


/**
 * Displays query information in footer (For testing - NOT FOR PRODUCTION)
 * @since 4.0.0
 */
function show_query_analysis(){
	if (current_user_can('administrator')){
	    global $wpdb;
	    echo "<pre>";
	    print_r($wpdb->queries);
	    echo "</pre>";
	}
}

function custom_trim_excerpt($text, $length) {
	
	$text = strip_shortcodes( $text ); // optional
	$text = strip_tags($text);
	
	$words = explode(' ', $text, $length + 1);
	if ( count($words) > $length) {
		array_pop($words);
		$text = implode(' ', $words);
	}
	return $text.'&nbsp;[&hellip;]';
}
	
function pagelines_show_thumb($post = null, $location = null){
	
	 if( function_exists('the_post_thumbnail') && has_post_thumbnail($post) ){
	
		// For Hook Parsing
		if(is_admin() || !get_option(PAGELINES_SETTINGS)) return true;
		
		if($location == 'clip' && pagelines_option('thumb_clip')) return true;
		
		if( !isset($location) ){
			// Thumb Page
			if(is_single() && pagelines_option('thumb_single')) return true;

			// Blog Page
			elseif(is_home() && pagelines_option('thumb_blog')) return true;

			// Search Page
			elseif(is_search() && pagelines_option('thumb_search')) return true;

			// Category Page
			elseif(is_category() && pagelines_option('thumb_category')) return true;

			// Archive Page
			elseif(is_archive() && pagelines_option('thumb_archive')) return true;

			else return false;
		} else return false;
	} else return false;
	
}

function pagelines_show_excerpt($post = null){
	
		// For Hook Parsing
		if(is_admin() || !get_option(PAGELINES_SETTINGS)) return true;
		
		// Thumb Page
		if(is_single() && pagelines_option('excerpt_single')) return true;
		
		// Blog Page
		elseif(is_home() && pagelines_option('excerpt_blog')) return true;
		
		// Search Page
		elseif(is_search() && pagelines_option('excerpt_search')) return true;
		
		// Category Page
		elseif(is_category() && pagelines_option('excerpt_category')) return true;
		
		// Archive Page
		elseif(is_archive() && pagelines_option('excerpt_archive')) return true;

		else return false;
}

function pagelines_show_content($post = null){
		// For Hook Parsing
		if(is_admin()) return true;
		
		// show on single post pages only
		if(is_page() || is_single()) return true;
		
		// Blog Page
		elseif(is_home() && pagelines_option('content_blog')) return true;

		// Search Page
		elseif(is_search() && pagelines_option('content_search')) return true;

		// Category Page
		elseif(is_category() && pagelines_option('content_category')) return true;
		
		// Archive Page
		elseif(is_archive() && pagelines_option('content_archive')) return true;
		
		else return false;

}

/*
	Show clip or full width post
*/
function pagelines_show_clip($count, $paged){
	
	if(!VPRO) return false;
	
	// For Hook Parsing
	if(is_admin()) return true;
	
	if(is_home() && pagelines_option('blog_layout_mode') == 'magazine' && $count <= pagelines_option('full_column_posts') && $paged == 0){
		return false;
	}
	
	elseif(pagelines_option('blog_layout_mode') != 'magazine') return false;
	
	elseif(is_page() || is_single()) return false;
	
	else return true;
}
