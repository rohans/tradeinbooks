<?php
/*

	Section: PostLoop
	Author: Andrew Powers
	Description: Paginates posts, shows a numerical post navigation
	Version: 1.0.0
	
*/

class PageLinesPostLoop extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('Main Content <small>(The Loop - Required)</small>', 'pagelines');
		$id = 'theloop';
	
		
		$settings = array(
			'type' 			=> 'main',
			'description' 	=> 'The Main Posts Loop. Includes content and post information',
			'workswith' 	=> array('main-single', 'main-default', 'main-posts'),
			'folder' 		=> 'wp', 
			'init_file' 	=> 'postloop',
			'required'		=> true, 
			'icon'			=> CORE_IMAGES . '/admin/document.png'
		);
		

	   parent::__construct($name, $id, $settings);    
   }

   function section_template() { 
		//Included in theme root for easy editing.
		get_template_part( 'template.postloop' ); 
	}

}

/*
	End of section class
*/