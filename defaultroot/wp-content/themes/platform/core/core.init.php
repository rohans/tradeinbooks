<?php
/**
 * This file initializes the PageLines framework 
 *
 * @package PageLines Core
 *
 **/

/**
 * Run the starting hook
 */
do_action('pagelines_hook_pre'); //hook

define('CORE', TEMPLATEPATH . "/core");
define('CORENAME', "core");

/**
 * Setup all the globals for the framework
 */
require_once( CORE . '/core.globals.php');

/**
 * Load master configuration file
 */
require_once( THEME_CONFIG . '/config.theme.php');

/**
 * Localization - Needs to come after config_theme and before localized config files
 */
require_once( CORE_LIB . '/library.I18n.php');

/**
 * Load core functions
 */
require_once( CORE_LIB . '/library.functions.php');

/**
 * Load Options Functions 
 */
require_once( CORE_LIB . '/library.options.php' );

/**
 * Load template related functions
 */
require_once( CORE_LIB . '/library.templates.php');

/**
 * Load shortcode library
 */
require_once( CORE_LIB . '/library.shortcodes.php');


/**
 * Theme configuration files
 */
require_once( THEME_CONFIG . '/config.options.php' ); 
require_once( THEME_CONFIG . '/config.options.meta.php' ); 
require_once( THEME_CONFIG . '/config.posttypes.php' );
require_once( THEME_CONFIG . '/config.widgets.php' );
require_once( THEME_CONFIG . '/config.intro.php' );

/* Options Singleton */
$GLOBALS['global_pagelines_settings'] = get_option(PAGELINES_SETTINGS);	
	
/**
 * Load Custom Post Type Class
 */
require_once( CORE_LIB . '/class.types.php' );

/**
 * Load layout class and setup layout singleton
 * @global object $pagelines_layout
 */
require_once( CORE_LIB . '/class.layout.php' ); 
$GLOBALS['pagelines_layout'] = new PageLinesLayout();
	
/**
 * Load sections handling class
 */
require_once( CORE_LIB . '/class.sections.php' );

/**
 * Load template handling class
 */	
require_once( CORE_LIB . '/class.template.php' );

/**
 * Load meta option handling class
 */
require_once( CORE_LIB . '/class.options.meta.php' );

/**
 * Load options handling classes
 */
require_once( CORE_LIB . '/class.options.ui.php' );

/**
 * Load dynamic CSS handling
 */
require_once( CORE_LIB . '/class.css.php' );


/**
 * PageLines Section Factory Object (Singleton)
 * Note: Must load before the config template file
 * @global object $pl_section_factory
 * @since 4.0.0
 */
$GLOBALS['pl_section_factory'] = new PageLinesSectionFactory();
require_once( THEME_CONFIG . '/config.templates.php' );

// Load persistent section functions (e.g. custom post types)
load_section_persistent();
if(is_admin()) load_section_admin();

require_once( CORE_LIB . '/admin.meta.php' );

	
/**
 * Support optional WordPress functionality
 */
add_theme_support( 'post-thumbnails', array('post') );
add_theme_support( 'menus' );
add_theme_support( 'automatic-feed-links' );

/**
 * Setup Framework Versions
 */
if(VPRO) require_once(PAGELINES_PRO . '/init_pro.php');
if(VDEV) require_once(PAGELINES_DEV . '/init_dev.php');	
	


/**
 * Load admin actions
 */
require_once (CORE_LIB.'/actions.admin.php'); 

/**
 * Load option actions
 */
require_once (CORE_LIB.'/actions.options.php');

/**
 * Load site actions
 */
require_once (CORE_LIB.'/actions.site.php'); 

/**
 * Run the pagelines_init Hook
 */
do_action('pagelines_hook_init'); //hook
