<?php
/**
 * This file contains a library of common templates accessed by functions
 *
 * @package PageLines Core
 *
 **/

// ======================================
// = Sidebar Setup & Template Functions =
// ======================================

/**
 * Sidebar - Call & Markup
 *
 */

function pagelines_draw_sidebar($id, $name, $default = null){?>
	<ul id="<?php echo 'list_'.$id; ?>" class="sidebar_widgets fix">
		<?php if (!dynamic_sidebar($name)){ pagelines_default_widget( $id, $name, $default); } ?>
	</ul>
<?php }

/**
 * Sidebar - Default Widget
 *
 */
function pagelines_default_widget($id, $name, $default){
	if(isset($default) && !pagelines('sidebar_no_default')):
	
		get_template_part( $default ); 
		
	elseif(!pagelines('sidebar_no_default')):
	?>	

	<li class="widget-default no_<?php echo $id;?>">
			<h3 class="widget-title">Add Widgets (<?php echo $name;?>)</h3>
			<p>This is your <?php echo $name;?>. Edit this content that appears here in the <a href="<?php echo admin_url('widgets.php');?>">widgets panel</a> by adding or removing widgets in the <?php echo $name;?> area.
			</p>
	</li>

<?php endif;
	}

/**
 * Sidebar - Standard Sidebar Setup
 *
 */
function pagelines_standard_sidebar($name, $description){
	return array(
		'name'=> $name,
		'description' => $description,
	    'before_widget' => '<li id="%1$s" class="%2$s widget fix"><div class="widget-pad">',
	    'after_widget' => '</div></li>',
	    'before_title' => '<h3 class="widget-title">',
	    'after_title' => '</h3>'
	);
}


/**
 * Javascript Confirmation
 *
 * @param string $name Function name, to be used in the input
 * @param string $text The text of the confirmation
 */
function pl_action_confirm($name, $text){ ?>
	<script language="jscript" type="text/javascript">
		function <?php echo $name;?>(){	
			var a = confirm ("<?php echo esc_js( $text );?>");
			if(a) return true;
			else return false;
		}
	</script>
<?php }


// Title and External Script Integration
function pagelines_head_common(){
	
	/*
		Title Metatag
	*/
	echo "\n<title>";
	if( pagelines_bbpress_forum() ){
		bb_title();
	}elseif(pagelines_is_buddypress_page()){
		bp_page_title();
	}else{
		if(is_front_page()) { echo get_bloginfo('name'); } else { wp_title(''); }
	}
	echo "</title>\n";
	
	if(!VDEV) { echo "<!-- Platform WordPress Framework By PageLines - www.PageLines.com -->\n\n";}
	/*
		Meta Images
	*/
	if(pagelines_option('pagelines_favicon')){
		echo '<link rel="shortcut icon" href="'.pagelines_option('pagelines_favicon').'" type="image/x-icon" />';
	}
	if(pagelines_option('pagelines_touchicon')){
		echo '<link rel="apple-touch-icon" href="'.pagelines_option('pagelines_touchicon').'" />';
	}

	?> 
<meta http-equiv="Content-Type" content="<?php bloginfo('html_type'); ?>; charset=<?php bloginfo('charset'); ?>" />
<link rel="profile" href="http://gmpg.org/xfn/11" />
<meta name="generator" content="WordPress <?php bloginfo('version'); // For stats in WordPress ?>" /> 
<link rel="pingback" href="<?php bloginfo('pingback_url'); ?>" />
<?php if(pagelines_option('gfonts')):?>
	<link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=<?php print_pagelines_option('gfonts_families', 'molengo');?>">
<?php endif;

	if( pagelines_bbpress_forum() ){ // Load bbPress headers 	
			bb_feed_head();
			bb_head(); 
			echo '<link rel="stylesheet" href="';
			bb_stylesheet_uri();
			echo '" type="text/css" />';
			
			// Enqueued Stuff doesn't show in bbPress
			// So we have to load the CSS manually....
			if(VPRO){
				echo '<link rel="stylesheet" id="pagelines-pro-css" href="';
				echo PAGELINES_PRO_ROOT.'/pro.css';
				echo '" type="text/css" />';
			}
			
			echo '<link rel="stylesheet" id="pagelines-bbpress-css" href="';
			bloginfo('stylesheet_url');
			echo '" type="text/css" />';
		
	}
	
		if(VPRO){
			wp_register_style('pagelines-pro', PAGELINES_PRO_ROOT.'/pro.css', array(), CORE_VERSION, 'all');
		    wp_enqueue_style( 'pagelines-pro');
		}
		
		wp_register_style('pagelines-stylesheet', get_bloginfo('stylesheet_url'), array(), CORE_VERSION, 'all');
	    wp_enqueue_style( 'pagelines-stylesheet');
	
		// Queue Common Javascript Libraries
		wp_enqueue_script("jquery"); 
		if ( is_single() || is_page() ) wp_enqueue_script( 'comment-reply' ); // This makes the comment box appear where the ‘reply to this comment’ link is
}
	
// Fix IE issues to the extent possible...
function pagelines_fix_ie($imagestofix = ''){?>
<?php if(pagelines('google_ie')):?>
<!--[if lt IE 8]> <script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE8.js"></script> <![endif]-->
<?php endif;?>
<!--[if IE 6]>
<script src="<?php echo CORE_JS . '/ie.belatedpng.js';?>"></script> 
<script>DD_belatedPNG.fix('<?php echo $imagestofix;?>');</script>
<![endif]-->
<?php 

/*
	IE File Setting up with conditionals
	TODO Why doesnt WP allow you to conditionally enqueue scripts?
*/

// If IE6 add the Internet Explorer 6 specific stylesheet
	global $wp_styles;
	wp_enqueue_style('ie6-style', THEME_CSS  . '/ie6.css');
	$wp_styles->add_data( 'ie6-style', 'conditional', 'lte IE 6' );
	
	wp_enqueue_style('ie7-style', THEME_CSS  . '/ie7.css');
	$wp_styles->add_data( 'ie7-style', 'conditional', 'IE 7' );
	
} 

function pagelines_font_replacement( $default_font = ''){
	
	if(pagelines_option('typekit_script')){
		echo pagelines_option('typekit_script');
	}
	
	if(pagelines_option('fontreplacement')){
		global $cufon_font_path;
		
		if(pagelines_option('font_file')) $cufon_font_path = pagelines_option('font_file');
		elseif($default_font) $cufon_font_path = THEME_JS.'/'.$default_font;
		else $cufon_font_path = null;
		
		// ===============================
		// = Hook JS Libraries to Footer =
		// ===============================
		add_action('wp_footer', 'font_replacement_scripts');
		function font_replacement_scripts(){
			
			global $cufon_font_path;

			wp_register_script('cufon', CORE_JS.'/type.cufon.js', 'jquery', '1.09', true);
			wp_print_scripts('cufon');
			
			if(isset($cufon_font_path)){
				wp_register_script('cufon_font', $cufon_font_path, 'cufon');
				wp_print_scripts('cufon_font');
			}
		
		}
		
		add_action('wp_head', 'cufon_inline_script');
		function cufon_inline_script(){
			?><script type="text/javascript"><?php 
			if(pagelines('replace_font')): 
				?>jQuery(document).ready(function () {
					Cufon.replace('<?php echo pagelines("replace_font"); ?>', {hover: true});
				});<?php 
			endif;
			?></script><?php
		 }
 	}
}


/**
 * Does posts info
 *
 */
function pagelines_do_posts_info(){
	if(is_category() || is_archive() || is_search()):?>
		<div class="current_posts_info">
			<?php if(is_search()):?>
				<?php _e("Search results for ", 'pagelines');?> 
				<strong>"<?php the_search_query();?>"</strong>
			<?php elseif(is_category()):?>
				<?php _e("Currently viewing the category: ", 'pagelines');?> 
				<strong>"<?php single_cat_title();?>"</strong>
			<?php elseif(is_tag()):?>
				<?php _e("Currently viewing the tag: ", 'pagelines');?>
				<strong>"<?php single_tag_title(''); ?>"</strong>
			<?php elseif(is_archive()):?>
				<?php if (is_author()) { ?>
					<?php _e('Posts by author:', 'pagelines'); ?>
					<strong><?php echo get_the_author_meta('display_name'); ?></strong>
				<?php } elseif (is_day()) {	?>
				 	<?php _e('From the daily archives:', 'pagelines'); ?>
					<strong><?php the_time('l, F j, Y'); ?></strong>
				<?php } elseif (is_month()) { ?>
					<?php _e('From the monthly archives:', 'pagelines'); ?>
					<strong><?php the_time('F Y'); ?></strong>
				<?php } elseif (is_year()) { ?>
					<?php _e('From the yearly archives:', 'pagelines'); ?>
					<strong><?php the_time('Y'); ?></strong>
				<?php } else {?> 
					<?php _e("Viewing archives for ", 'pagelines');?>
					<strong>"<?php the_date();?>"</strong>
				<?php } ?>
			<?php endif;?>
		</div>
	<?php endif;
}