<?php 

/**
 * Define framework version
 */
define('CORE_VERSION', "4.0.0");

/**
 * If Pro Version
 */
if(file_exists(TEMPLATEPATH.'/pro/init_pro.php')){
	define('VPRO',true);
}else{ define('VPRO',false);}
	
/**
 * If Dev Version
 */
if(file_exists(TEMPLATEPATH.'/dev/init_dev.php')){
	define('VDEV',true);
}else{ define('VDEV',false); }

/**
 * Set Theme Name
 */
if(VPRO) $theme = 'PlatformPro';
else $theme = 'Platform';

define('THEMENAME', $theme);
define('CHILDTHEMENAME', get_option('stylesheet'));


define('PARENT_DIR', TEMPLATEPATH);
define('CHILD_DIR', STYLESHEETPATH);

define('PARENT_URL', get_bloginfo('template_directory'));
define('CHILD_URL', get_bloginfo('stylesheet_directory'));
define('CHILD_IMAGES', CHILD_URL . '/images');



/**
 * Define Settings Constants for option DB storage
 */
define('PAGELINES_SETTINGS', apply_filters('pagelines_settings_field', 'pagelines-settings'));
define('PAGELINES_LAYOUT_SETTINGS', apply_filters('pagelines_layout_settings_field', 'pagelines-layout-settings'));	
define('PAGELINES_SECTION_SETTINGS', apply_filters('pagelines_section_settings_field', 'pagelines-section-settings'));	

/**
 * Define core path constants
 */
define('CORE_PLUGINS', CORE . '/plugins');
define('CORE_CLASSES', CORE . '/classes');
define('CORE_ADMIN', CORE . '/admin');
define('CORE_INITS', CORE . '/inits');
define('CORE_LIB', CORE . '/library');
define('CORE_TEMPLATES', CORE . '/templates');

/**
 * Define theme path constants
 */
define('THEME_CONFIG', TEMPLATEPATH . '/config');


define('THEME_WIDGETS', THEME_CONFIG . '/widgets');
define('THEME_SECTIONS', TEMPLATEPATH . '/sections');


/**
 * Define web constants
 */

define('THEME_ROOT', get_bloginfo('template_url'));
define('CORE_ROOT', THEME_ROOT . '/'.CORENAME);
define('CONFIG_ROOT', THEME_ROOT . '/config');
define('SECTION_ROOT', THEME_ROOT . '/sections');
	
/**
 * Define core web constants
 */
define('CORE_JS', CORE_ROOT . '/js');
define('CORE_CSS', CORE_ROOT . '/css');
define('CORE_IMAGES', CORE_ROOT . '/images');

/**
 * Define theme web constants
 */
define('THEME_CSS', THEME_ROOT . '/css');
define('THEME_JS', THEME_ROOT . '/js');
define('THEME_IMAGES', THEME_ROOT . '/images');
	
/**
 * Define version constants
 */
define('PAGELINES_PRO', TEMPLATEPATH . '/pro' );
define('PRO_SECTIONS', PAGELINES_PRO . '/sections');
define('PAGELINES_DEV', TEMPLATEPATH . '/dev' );

define('PAGELINES_PRO_ROOT', THEME_ROOT . '/pro' );
define('PRO_SECTION_ROOT', PAGELINES_PRO_ROOT . '/sections' );

/**
 * Define language constants
 */
define('PAGELINES_LANGUAGE_DIR', TEMPLATEPATH.'/language');

