<?php


// ===============================================
// = Show Options Panel after theme activation   =
// ===============================================
if(is_admin() && isset($_GET['activated'] ) && $pagenow == "themes.php" ) {

	//Do redirect
	header( 'Location: '.admin_url().'admin.php?page=pagelines&activated=true&pageaction=activated' ) ;

}


/**
 * 
 *  Checks if PHP5
 *
 *  @package PageLines
 *  @subpackage Functions Library
 *  @since 4.0.0
 *
 */
add_action('pagelines_before_optionUI', 'pagelines_check_php');
function pagelines_check_php(){
	if(floatval(phpversion()) < 5.0){
		_e('<div class="config-error"><h2>PHP Version Problem</h2>Looks like you are using PHP version: <strong>'.phpversion().'</strong>. To run this framework you will need PHP <strong>5.0</strong> or better...<br/><br/> Don\'t worry though! Just check with your host about a quick upgrade.</div>', 'pagelines');
	}

}

// ====================================================================================
// = AJAX OPTION SAVING - Used to save via AJAX theme options and image uploads 
// ====================================================================================

	add_action('wp_ajax_pagelines_ajax_post_action', 'pagelines_ajax_callback');

	function pagelines_ajax_callback() {
		global $wpdb; // this is how you get access to the database
	
		if($_POST['type']){
			$save_type = $_POST['type'];
		}else $save_type = null;
	
		//Uploads
		if($save_type == 'upload'){
		
			$clickedID = $_POST['data']; // Acts as the name
			$filename = $_FILES[$clickedID];
	       	$filename['name'] = preg_replace('/[^a-zA-Z0-9._\-]/', '', $filename['name']); 
		
			$override['test_form'] = false;
			$override['action'] = 'wp_handle_upload';    
			$uploaded_file = wp_handle_upload($filename,$override);
		 
			$upload_tracking[] = $clickedID;
			
			pagelines_update_option( $clickedID , $uploaded_file['url'] );

			if(!empty($uploaded_file['error'])) {echo 'Upload Error: ' . $uploaded_file['error']; }	
			else { echo $uploaded_file['url']; } // Is the Response
		}
		elseif($save_type == 'image_reset'){
			
				$id = $_POST['data']; // Acts as the name
				pagelines_update_option($id, null);
				
	
		}
	
		die();
	}
	
// ====================================================================================
// = AJAX TEMPLATE MAP SAVING - Used to save via AJAX theme options and image uploads =
// ====================================================================================

	add_action('wp_ajax_pagelines_save_sortable', 'ajax_save_template_map');

	function ajax_save_template_map() {
		global $wpdb; // this is how you get access to the database
		
		
		/* Full Template Map */
		
		$templatemap = get_option('pagelines_template_map');
		
		/* Order of the sections */
		$section_order =  $_GET['orderdata'];
		
		/* Get array / variable format */
		parse_str($section_order);
		
		/* Selected Template */
		$selected_template = esc_attr($_GET['template']);
		
			/* Explode by slash to get heirarchy */
			$template_heirarchy = explode('-', $selected_template);
			
			if(isset($template_heirarchy[1])){
				$templatemap[$template_heirarchy[0]]['templates'][$template_heirarchy[1]]['sections'] = urlencode_deep($section);
			} else {
				$templatemap[$selected_template]['sections'] = $section;
			}
		
		
		save_template_map($templatemap);
		
		echo true;
		
		die();
	}

	