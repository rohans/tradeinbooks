<?php 

// ====================================
// = Build PageLines Option Interface =
// ====================================


//	This function adds the top-level menu
add_action('admin_menu', 'pagelines_add_admin_menu');
function pagelines_add_admin_menu() {
	global $menu;

	// Create the new separator
	$menu['58.995'] = array( '', 'manage_options', 'separator-pagelines', '', 'wp-menu-separator' );

	// Create the new top-level Menu
	add_menu_page ('Page Title', THEMENAME, 'manage_options','pagelines', 'pagelines_build_option_interface', CORE_IMAGES. '/favicon-pagelines.png', '58.996');
}

// Create theme options panel
add_action('admin_menu', 'pagelines_add_admin_submenus');
function pagelines_add_admin_submenus() {
	global $_pagelines_options_page_hook;
	
	$_pagelines_options_page_hook = add_submenu_page('pagelines', 'Settings', 'Settings', 'manage_options', 'pagelines','pagelines_build_option_interface'); // Default
}

// Build option interface
function pagelines_build_option_interface(){ 
	do_action('pagelines_before_optionUI');
	$optionUI = new PageLinesOptionsUI;
}

/**
 * This is a necessary go-between to get our scripts and boxes loaded
 * on the theme settings page only, and not the rest of the admin
 */
add_action('admin_menu', 'pagelines_theme_settings_init');
function pagelines_theme_settings_init() {
	global $_pagelines_options_page_hook;
	
	wp_enqueue_script( 'jquery' );
	wp_enqueue_script( 'jquery-ajaxupload', CORE_JS . '/jquery.ajaxupload.js');

	add_action('load-'.$_pagelines_options_page_hook, 'pagelines_theme_settings_scripts');
	wp_enqueue_script( 'platform-admin-js', CORE_JS . '/platform.admin.js');
}

function pagelines_theme_settings_scripts() {	
	wp_enqueue_script( 'jquery-ui-core' );
	wp_enqueue_script( 'jquery-ui-tabs' );	
	wp_enqueue_script( 'jquery-ui-draggable' );	
	wp_enqueue_script( 'jquery-ui-sortable' );
	wp_enqueue_script( 'jquery-layout', CORE_JS . '/jquery.layout.js');
}

add_action( 'admin_head', 'load_head' );
function load_head(){

	// Always Load
	echo '<link rel="stylesheet" href="'.CORE_CSS.'/admin.css" type="text/css" media="screen" />';
	if(pagelines_option('pagelines_favicon'))  echo '<link rel="shortcut icon" href="'.pagelines_option('pagelines_favicon').'" type="image/x-icon" />';

	// Load on PageLines pages
	if(isset($_GET['page']) && ($_GET['page'] == 'pagelines')){
			include( CORE_LIB.'/admin.head.php' );
	}

}


/**
 * This registers the settings field and adds defaults to the options table.
 * It also handles settings resets by pushing in the defaults.
 */
add_action('admin_init', 'pagelines_register_settings', 5);
function pagelines_register_settings() {
	
	
	register_setting( PAGELINES_SETTINGS, PAGELINES_SETTINGS );
	
	 /*
	 	Set default settings
	 */
		add_option( PAGELINES_SETTINGS, pagelines_settings_defaults() ); // only fires first time
	
		pagelines_wp_option_defaults(); // Add stand alone wp options, only fires first time
	

	if ( !isset($_REQUEST['page']) || $_REQUEST['page'] != 'pagelines' )
		return;	
	
	/*
		Import/Exporting
	*/
	pagelines_import_export();
			
		
	pagelines_process_reset_options();

	if ( isset($_GET['activated']) || isset($_GET['updated']) || isset($_GET['reset']) ) {
		pagelines_build_dynamic_css();
	}
	
	if ( pagelines_option('reset') ) {
		update_option(PAGELINES_SETTINGS, pagelines_settings_defaults());
		pagelines_wp_option_defaults(true);
		pagelines_build_dynamic_css();
		wp_redirect( admin_url( 'admin.php?page=pagelines&reset=true' ) );
		exit;
	}

}


/*
	Section ON Page disabling
*/

add_action("admin_menu", 'add_section_control_box');

add_action('save_post', 'save_section_control_box');

function add_section_control_box(){
	
	add_meta_box('section_control', 'PageLines Section Control', "pagelines_section_control_callback", 'page', 'side', 'low');

	add_meta_box('section_control', 'PageLines Section Control', "pagelines_section_control_callback", 'post', 'side', 'low');
}

function pagelines_section_control_callback(){
	global $post; 
	global $global_pagelines_settings; 
	global $pl_section_factory;
	global $pagelines_template;
	
	$section_control = pagelines_option('section-control');
	
	echo '<div class="section_control_desc"><p>Below are all the sections that are active for this template.</p> <p>Here you can turn sections off or on (if hidden by default) for this individual page/post.</p> <p><small><strong>Note:</strong> Individual page settings do not work on the blog page (<em>use the settings panel</em>).</small></p></div>';?>
		
		<div class="admin_section_control section_control_individual">
			<div class="section_control_pad">
			<?php  pagelines_process_template_map('section_control_checkbox', array('area_titles'=> true)); ?>
	 		</div>
		</div>
		
<?php 	
}

function section_control_checkbox($section, $template_slug, $template_area, $template){ 
		global $pl_section_factory;
		global $post;
		
		$s = $pl_section_factory->sections[$section];
		
		// Load Global Section Control Options
		$section_control = pagelines_option('section-control');
		$hidden_by_default = isset($section_control[$template_slug][$section]['hide']) ? $section_control[$template_slug][$section]['hide'] : null;
		
		$check_type = ($hidden_by_default) ? 'show' : 'hide';
		
		// used to be _hide_SectionClass;
		// needs to be _hide_TemplateSlug_SectionClass
		// Why? 
		$check_name = "_".$check_type."_".$section;
		
		$check_label = ucfirst($check_type)." ".$s->name;
		
		$check_value = get_pagelines_meta($check_name, $post->ID);
		
	?>
	
	<div class="section_checkbox_row <?php echo 'type_'.$check_type;?>" >
	
		<input class="section_control_check" type="checkbox" id="<?php echo $check_name; ?>" name="<?php echo $check_name; ?>" <?php checked((bool) $check_value); ?> />
		<label for="<?php echo $check_name;?>"><?php echo $check_label;?></label>
		
	</div>
<?php }

function save_section_control_box($postID){
	global $pagelines_template;
	
	global $post; 

	$save_template = new PageLinesTemplate();

	if(isset($_POST['update']) || isset($_POST['save']) || isset($_POST['publish'])){

		foreach($save_template->default_all_template_sections as $section){
					
			$option_value =  isset($_POST['_hide_'.$section]) ? $_POST['_hide_'.$section] : null;
			
			if(!empty($option_value) || get_post_meta($postID, '_hide_'.$section)){
				update_post_meta($postID, '_hide_'.$section, $option_value );
			}
			
			$option_value =  isset($_POST['_show_'.$section]) ? $_POST['_show_'.$section] : null;
			
			if(!empty($option_value) || get_post_meta($postID, '_show_'.$section)){
				update_post_meta($postID, '_show_'.$section, $option_value );
			}
		}
	}
}


