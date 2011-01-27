<?php

/*
	TEMPLATES CONFIG
	
	This file is used to configure page template defaults and 
	to set up the HTML/JS sections that will be used in the site. 
	Platform themes should add sections through the appropriate hooks
	
*/

/*
	SECTION REGISTER 
	Register HTML sections for use in the theme.
*/
	
// Common WP 
	pagelines_register_section('PageLinesContent', 'wp', 'content');
	pagelines_register_section('PageLinesPostLoop', 'wp', 'postloop');
	pagelines_register_section('PageLinesPostNav', 'wp', 'postnav');
	pagelines_register_section('PageLinesComments', 'wp', 'comments');
	pagelines_register_section('PageLinesPagination', 'wp', 'pagination');
	pagelines_register_section('PageLinesShareBar', 'wp', 'sharebar');
	pagelines_register_section('PageLinesNoPosts', 'wp', 'noposts');
	pagelines_register_section('PageLinesPostAuthor', 'wp', 'postauthor');
	
// In Header
	pagelines_register_section('PageLinesNav', 'nav');
	pagelines_register_section('PageLinesBranding', 'wp', 'branding');	
	pagelines_register_section('PageLinesBreadcrumb', 'breadcrumb');

// Sections With Custom Post Types
	pagelines_register_section('PageLinesFeatures', 'features'); // 'features'
	pagelines_register_section('PageLinesBoxes', 'boxes'); // 'boxes'
	pagelines_register_section('PageLinesBanners', 'banners'); // 'boxes'

// Sidebar Sections & Widgets
	pagelines_register_section('PrimarySidebar', 'sidebars', 'sb_primary');
	pagelines_register_section('SecondarySidebar', 'sidebars', 'sb_secondary');
	pagelines_register_section('TertiarySidebar', 'sidebars', 'sb_tertiary');
	pagelines_register_section('UniversalSidebar', 'sidebars', 'sb_universal');
	
	pagelines_register_section('FullWidthSidebar', 'sidebars', 'sb_fullwidth');
	pagelines_register_section('ContentSidebar', 'sidebars', 'sb_content');
	
	pagelines_register_section('PageLinesMorefoot', 'sidebars', 'morefoot');
	pagelines_register_section('PageLinesFootCols', 'sidebars', 'footcols');
	
// Misc & Dependent Sections

	pagelines_register_section('PageLinesSoapbox', 'soapbox');
	pagelines_register_section('PageLinesCarousel', 'carousel');
	pagelines_register_section('PageLinesHighlight', 'highlight');
	pagelines_register_section('PageLinesTwitterBar', 'twitterbar');
	pagelines_register_section('PageLinesSimpleFooterNav', 'footer_nav');

	pagelines_register_section('PageLinesCallout','callout');


// Do a hook for registering sections
	do_action('pagelines_register_sections'); //hook


/*
	TEMPLATE MAP
	
	This array controls the default template map of section in the theme
	Each top level needs a hook; and the top-level template needs to be included 
	as an arg in said hook...
*/
function the_template_map() {
	$template_map = array(
		'header' 		=> array(
				'hook' 			=> 'pagelines_header', 
				'name'			=> 'Site Header',
				'markup'		=> 'content', 
				'sections' 		=> array( 'PageLinesBranding' , 'PageLinesNav' )
			),
		'footer'		=> array(
				'hook' 			=> 'pagelines_footer', 
				'name'			=> 'Site Footer', 
				'markup'		=> 'content', 
				'sections' 		=> array('PageLinesFootCols')
			),
		'templates'			=> array(
				'hook'			=> 'pagelines_template', 
				'name'			=> 'Page Templates', 
				'markup'		=> 'content', 
				'templates'		=> array(
						'default' => array(
								'name'			=> 'Default Page',
								'sections' 		=> array('PageLinesContent')
						),
						'posts' => array(
								'name'			=> 'Posts Pages',
								'sections' 		=> array('PageLinesContent')
							),
						'single' => array(
								'name'			=> 'Single Post Page',
								'sections' 		=> array('PageLinesContent')
							),
						'alpha' => array(
								'name'			=> 'Feature Page',
								'sections' 		=> array('PageLinesFeatures', 'PageLinesBoxes', 'PageLinesContent'),
								'version'		=> 'pro'
							),
						'beta' => 	array(
								'name'			=> 'Carousel Page',
								'sections' 		=> array('PageLinesCarousel', 'PageLinesContent'),
								'version'		=> 'pro'
							),
						'gamma' => 	array(
								'name'			=> 'Box Page',
								'sections' 		=> array( 'PageLinesHighlight', 'PageLinesSoapbox', 'PageLinesBoxes' ),
								'version'		=> 'pro'
							),
						'delta' => 	array(
								'name'			=> 'Highlight Page',
								'sections' 		=> array( 'PageLinesHighlight', 'PageLinesContent' ),
								'version'		=> 'pro'
							),
						'epsilon' => 	array(
								'name'			=> 'Banner Page',
								'sections' 		=> array( 'PageLinesHighlight', 'PageLinesBanners', 'PageLinesContent' ),
								'version'		=> 'pro'
							),
						'404' => array(
								'name'			=> '404 Error Page',
								'sections' 		=> array( 'PageLinesNoPosts' ),
							),
					)
			), 
		'main'			=> array(
				'hook'			=>	'pagelines_main', 
				'name'			=>	'Text Content Area',
				'markup'		=> 'copy', 
				'templates'		=>	array(
						'default' => array(
								'name'			=> 'Page Content Area',
								'sections' 		=> array('PageLinesPostLoop', 'PageLinesComments')
							),
						'posts' => array(
								'name'			=> 'Posts Page Content Area',
								'sections' 		=> array('PageLinesPostLoop', 'PageLinesPagination')
							),

						'single' => array(
								'name'			=> 'Single Post Content Area',
								'sections' 		=> array('PageLinesPostNav', 'PageLinesPostLoop', 'PageLinesShareBar', 'PageLinesComments', 'PageLinesPagination')
							)
					)
			), 
		'morefoot'		=> array(
				'name'			=> 'Morefoot Area',
				'hook' 			=> 'pagelines_morefoot',
				'markup'		=> 'content', 
				'version'		=> 'pro',
				'sections' 		=> array('PageLinesMorefoot', 'PageLinesTwitterBar')
			), 
		'sidebar1'		=> array(
				'name'			=> 'Sidebar 1',
				'hook' 			=> 'pagelines_sidebar1',
				'markup'		=> 'copy', 
				'sections' 		=> array('PrimarySidebar')
			),
		'sidebar2'		=> array(
				'name'			=> 'Sidebar 2',
				'hook' 			=> 'pagelines_sidebar2',
				'markup'		=> 'copy', 
				'sections' 		=> array('SecondarySidebar')
			),
		'sidebar_wrap'	=> array(
				'name'			=> 'Sidebar Wrap',
				'hook' 			=> 'pagelines_sidebar_wrap',
				'markup'		=> 'copy', 
				'version'		=> 'pro',
				'sections' 		=> array()
			)
		
	);
	
	return apply_filters('pagelines_template_map', $template_map); 
}



				

