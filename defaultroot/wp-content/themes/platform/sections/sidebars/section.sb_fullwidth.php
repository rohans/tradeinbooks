<?php
/*

	Section: Full Width Sidebar
	Author: Andrew Powers
	Description: Shows full width widgetized sidebar
	Version: 1.0.0
	
*/

class FullWidthSidebar extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('Full Width Sidebar', 'pagelines');
		$id = 'fullwidth_sidebar';
		$this->handle = "Full Width Sidebar";
	
		
		$settings = array(
			'description' 	=> __('A widgetized full width sidebar. This sidebar will span the entire width of your website.', 'pagelines'),
			'workswith' 	=> array('templates', 'footer', 'morefoot'),
			'folder' 		=> '', 
			'init_file' 	=> 'fullwidth_sidebar.php', 
			'icon'			=> CORE_IMAGES . '/admin/sidebar.png',
			'version'		=> 'pro'
		);
		

	   parent::__construct($name, $id, $settings);    
   }

   function section_persistent() { 
		$setup = pagelines_standard_sidebar($this->name, $this->settings['description']);
		register_sidebar($setup);
	
	}

   function section_template() { 
		 pagelines_draw_sidebar($this->id, $this->name);
	}

}

/*
	End of section class
*/