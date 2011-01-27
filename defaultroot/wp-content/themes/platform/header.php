<?php 
/*
	
	HEADER
	
	This file controls the HTML <head> and top graphical markup (including Navigation) for each page in your theme.
	You can control what shows up where using WordPress and PageLines PHP conditionals
	
	This theme copyright (C) 2008-2010 PageLines
	
*/ 	
	do_action('pagelines_before_html'); //hook 
?><!DOCTYPE html><!-- HTML 5 -->
<html <?php language_attributes(); ?>>
<head>
<?php 
		do_action('pagelines_code_before_head'); //hook 
		
		pagelines_head_common(); // Common header information
		
		do_action('pagelines_head'); //hook 
		
		print_pagelines_option('headerscripts'); // Header Scripts Input Option
		
		pagelines_font_replacement('calluna.font.js'); // Cufon Font Replacement
		
		pagelines_fix_ie('.pngbg, .shadow-bottom, .post-comments a, #fcolumns_container, #footer img, .branding_wrap img, .fboxgraphic img '); // Fix IE Issues. Args = .png images to fix in ie6 
		
		wp_head(); // Hook (WordPress) 
	
	global $global_pagelines_settings;
	
	$body_classes = '';
	$body_classes .= pagelines_option('site_design_mode');
	if(pagelines_is_buddypress_active() && !pagelines_bbpress_forum()){
		$body_classes .= ' buddypress';
	}

	
?></head>
<body <?php body_class( $body_classes ); ?>>

	<?php print_pagelines_option('asynch_analytics');  // Recommended Spot For Asynchronous Google Analytics ?>
	
	<?php do_action('pagelines_before_site'); //hook ?>
	<div id="site" class="<?php echo pagelines_layout_mode();?>"> <!-- #site // Wraps #header, #page-main, #footer - closed in footer -->
		
		<?php do_action('pagelines_before_page'); //hook ?>
		<div id="page"> <!-- #page // Wraps #header, #page-main - closed in footer -->
			
			<?php do_action('pagelines_before_header');?>
			<div id="header" class="container-group fix">
				<div class="outline">
					<?php do_action('pagelines_header', 'header'); //hook ?>
				</div>
			</div>
			<?php do_action('pagelines_before_main'); //hook ?>
			<div id="page-main" class="container-group fix"> <!-- #page-main // closed in footer -->
				<div class="outline fix">
					<?php if(pagelines_is_buddypress_page()):?>
						<div id="buddypress-page" class="fix">
							<div class="content fix">
					<?php endif;?>