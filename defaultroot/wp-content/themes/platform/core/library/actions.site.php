<?php

/*
	Global Used in bbPress Integration 
*/ 
global $pagelines_template;

// ===================================================================================================
// = Set up Section loading & create pagelines_template global in page (give access to conditionals) =
// ===================================================================================================



/**
 * Build PageLines Template Global (Singleton)
 * Must be built inside the page (wp_head) so conditionals can be used to identify the template
 * In the admin, the template doesn't need to be identified so its loaded in the init action
 * @global object $pagelines_template
 * @since 4.0.0
 */
add_action('pagelines_before_html', 'build_pagelines_template');
//add_action('admin_init', 'build_pagelines_template');

// In Admin
add_action('admin_head', 'build_pagelines_template');

// In Site
add_action('wp_head', array(&$pagelines_template, 'print_template_section_headers'));
add_action('wp_print_styles', 'workaround_pagelines_template_styles'); // Used as workaround on WP login page (and other pages with wp_print_styles and no wp_head/pagelines_before_html)
add_action('pagelines_head', array(&$pagelines_template, 'hook_and_print_sections'));
add_action('wp_footer', array(&$pagelines_template, 'print_template_section_scripts'));

/**
 * Creates a global page ID for reference in editing and meta options (no unset warnings)
 * 
 * @since 4.0.0
 */
add_action('pagelines_before_html', 'pagelines_id_setup');
