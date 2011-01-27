<?php
/*

	Section: PageLines Boxes
	Author: Andrew Powers
	Description: Creates boxes and box layouts
	Version: 1.0.0
	
*/

class PageLinesBoxes extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('PageLines Boxes', 'pagelines');
		$id = 'boxes';
		
		$default_settings = array(
			'description' 	=> 'Inline boxes on your page that support images and media.  Great for feature lists, and media.',
			'icon'			=> CORE_IMAGES.'/admin/boxes.png', 
			'version'		=> 'pro',
		);
		
		$settings = wp_parse_args( $registered_settings, $default_settings );
		
		parent::__construct($name, $id, $settings);    
   }


	/*
		Loads php that will run on every page load (admin and site)
		Used for creating administrative information, like post types
	*/

	function section_persistent(){
		/* 
			Create Custom Post Type 
		*/
			$args = array(
					'label' => __('Boxes', 'pagelines'),  
					'singular_label' => __('Box', 'pagelines'),
					'description' => 'For creating boxes in box type layouts.'
				);
			$taxonomies = array(
				"box-sets" => array(	
						"label" => __('Box Sets', 'pagelines'), 
						"singular_label" => __('Box Set', 'pagelines'), 
					)
			);
			$columns = array(
				"cb" => "<input type=\"checkbox\" />",
				"title" => "Title",
				"bdescription" => "Text",
				"bmedia" => "Media",
				"box-sets" => "Box Sets"
			);
		
			$column_value_function = 'box_column_display';
		
			$this->post_type = new PageLinesPostType($this->id, $args, $taxonomies, $columns, $column_value_function);
		
				/* Set default posts if none are present */
				$this->post_type->set_default_posts('pagelines_default_boxes');


		/*
			Meta Options
		*/
		
				/*
					Create meta fields for the post type
				*/
					$this->meta_array = array(
							'the_box_icon' 		=> array(
									'version' => 'pro',
									'type' => 'image_upload',					
									'inputlabel' => 'Box Image',
									'exp' => 'Upload an image for the box.<br/> Depending on your settings this image will be used as an icon, or splash image; so desired size may vary.'
								), 
							'the_box_icon_link'		=> array(
									'version' => 'pro',
									'type' => 'text',					
									'inputlabel' => 'Box Image Link (Optional)',
									'exp' => 'Make the box image clickable by adding a link here (optional)...'
								)
					);

					$post_types = array($this->id); 

					$this->meta_settings = array(
							'id' => 'box-meta',
							'name' => THEMENAME." Box Options",
							'posttype' => $post_types
						);

					$this->meta_options =  new PageLinesMetaOptions($this->meta_array, $this->meta_settings);

					/*
						Create meta fields for the page template when using the Feature Template
					*/
						$meta_array = array(

								'box_set' => array(
									'version' => 'pro',
									'type' => 'select_taxonomy',

									'taxonomy_id'	=> "box-sets",				
									'inputlabel' => 'Select Box Set To Show',
									'exp' => 'If you are using the box section, select the box set you would like to show on this page.'
								), 
								'box_col_number' => array(
									'type' 			=> 'count_select',
									'count_number'	=> '5', 
									'count_start'	=> '2',
									'inputlabel' 	=> 'Number of Feature Box Columns',
									'title' 		=> 'Box Columns',
									'shortexp' 		=> "Select the number of columns to show boxes in.",
									'exp' 			=> "The number you select here will be the number of boxes listed in a row on a page. Note: This won't work on the blog page (use the global option)."
								), 
								'box_thumb_type' => array(
									'version' => 'pro',
									'type' => 'select',
									'selectvalues'	=> array(
											'inline_thumbs'	=> array("name" => "Thumbs On Left"),
											'top_thumbs'	=> array("name" => "Thumbs On Top")
										), 
									'inputlabel' => 'Box Thumb Position',				
									'exp' => 'Choose between thumbs on left and thumbs on top of boxes.',
									
								),
								'box_thumb_size' => array(
									'version'		=> 'pro',
									'type' 			=> 'text',
									'inputlabel' 	=> 'Box Icon Size (in Pixels)',
									'shortexp' 		=> "Add the icon size in pixels",
									'exp' 			=> "Select the default icon size in pixels, set the images when creating new boxes.",
								),
								
								

							);
						$meta_settings = array(
								'id' => 'box-template-meta',
								'name' => THEMENAME." Box Section Options",
								'posttype' => 'page'
							);

						$this->meta_options =  new PageLinesMetaOptions($meta_array, $meta_settings);
	}

   function section_template() {    
	
		
		global $post; 
		$current_post = $post;
		
		// inserts a clearing element at the end of each line of boxes
		$perline = (isset($post) && pagelines_option('box_col_number', $post->ID)) ? pagelines_option('box_col_number', $post->ID) : 3;
		$count = $perline;
		
		$box_thumb_size = (isset($post) && pagelines_option('box_thumb_size', $post->ID)) ? pagelines_option('box_thumb_size', $post->ID) : pagelines_option('box_thumb_size');
		
		$box_thumb_type = (isset($post) && pagelines_option('box_thumb_type', $post->ID)) ? pagelines_option('box_thumb_type', $post->ID) : 'inline_thumbs';
		
?>		
	<div class="dcol_container_<?php echo $perline;?> fix">
<?php 
			
 			// Let's Do This...
			$theposts = $this->get_box_posts();
			$boxes = (is_array($theposts)) ? $theposts : array();
			foreach($boxes as $post) : setup_postdata($post); $custom = get_post_custom($post->ID); ?>
			
					<div class="dcol_<?php echo $perline;?> dcol">
						<div class="dcol-pad <?php echo $box_thumb_type;?>">	
							<?php if(get_post_meta($post->ID, 'the_box_icon', true)):?>
									<div class="fboxgraphic">
										
										<?php if(get_post_meta($post->ID, 'the_box_icon_link', true)):?>
										<a href="<?php echo get_post_meta($post->ID, 'the_box_icon_link', true);?>" alt="">
										<?php endif;?>
										
											<img src="<?php echo get_post_meta($post->ID, 'the_box_icon', true);?>" style="width:<?php echo $box_thumb_size;?>px">
										
										<?php if(get_post_meta($post->ID, 'the_box_icon_link', true)):?>
										</a>
										<?php endif;?>
										
						            </div>
							<?php endif;?>
							
								<div class="fboxinfo fix">
									<div class="fboxtitle"><h3><?php the_title(); ?></h3></div>
									<div class="fboxtext"><?php the_content(); ?><?php edit_post_link(__('[Edit Box]', 'pagelines'), '', '');?></div>
									
								</div>
								<?php pagelines_register_hook( 'pagelines_box_inside_bottom', $this->id ); // Hook ?>
						</div>
					</div>
					<?php $end = ($count+1) / $perline;  if(is_int($end)):?><div class="clear"></div><?php endif;?>
					<?php $count++;?>
			<?php endforeach;?>
	</div>
	<div class="clear"></div>
<?php  }

	
	function get_box_posts(){
		global $post;
		
		if(!isset($this->the_feature_boxes) && isset($post)){
			
			$query_args = array('post_type' => $this->id, 'orderby' =>'ID');
		
			if( get_pagelines_meta('box_set', $post->ID) ){
				$query_args = array_merge($query_args, array( 'box-sets' => get_post_meta($post->ID, 'box_set', true) ) );
			}elseif (pagelines_non_meta_data_page() && pagelines_option('box_default_tax')){
				$query_args = array_merge($query_args, array( 'box-sets' => pagelines_option('box_default_tax') ) );
			}
	
			
			if(pagelines('box_items', $post->ID)){
				$query_args = array_merge($query_args, array( 'showposts' => pagelines_option('box_items', $post->ID) ) );
			}

		
			$boxes_query = new WP_Query($query_args);
		
		 	$this->the_feature_boxes = $boxes_query->posts;
			
		 	if(is_array($this->the_feature_boxes)) return $this->the_feature_boxes;
			else return array();
			
		} elseif(isset($post)) {
			return $this->the_feature_boxes;
		}
	
	}

	function section_head() {   }

	function section_scripts() {  }

	function section_options($optionset = null, $location = null) {
		
		if($optionset == 'new' && $location == 'bottom'){
			return array(
				'box_settings' => array(
							'box_col_number' => array(
									'default' 		=> 3,
									'version'		=> 'pro',
									'type' 			=> 'count_select',
									'count_number'	=> '5', 
									'count_start'	=> '2',
									'inputlabel' 	=> 'Default Number of Feature Box Columns',
									'title' 		=> 'Box Columns',
									'shortexp' 		=> "Select the number of columns to show boxes in.",
									'exp' 			=> "The number you select here will be the number of boxes listed in a row on a page."
								),
							'box_items' => array(
									'default' 		=> 5,
									'version'		=> 'pro',
									'type' 			=> 'text_small',
									'inputlabel' 	=> 'Maximum Boxes To Show',
									'title' 		=> 'Default Number of Boxes',
									'shortexp' 		=> "Select the max number of boxes to show.",
									'exp' 			=> "This will be the maximum number of boxes shown on an individual page.",
								), 
							'box_thumb_size' => array(
									'default' 		=> 64,
									'version'		=> 'pro',
									'type' 			=> 'text_small',
									'inputlabel' 	=> 'Box Icon Size (in Pixels)',
									'title' 		=> 'Default Box Icon Size',
									'shortexp' 		=> "Add the icon size in pixels",
									'exp' 			=> "Select the default icon size in pixels, set the images when creating new boxes.",
								), 
							'box_default_tax' => array(
									'default' 		=> 'default-boxes',
									'version'		=> 'pro',
									'taxonomy_id'	=> 'box-sets',
									'type' 			=> 'select_taxonomy',
									'inputlabel' 	=> 'Select Posts/404 Box Set',
									'title' 		=> 'Posts Page and 404 Box-Set',
									'shortexp' 		=> "Posts pages and similar pages (404) will use this box-set ID",
									'exp' 			=> "Posts pages and 404 pages in WordPress don't support meta data so you need to assign a set here. (If you want to use 'boxes' on these pages.)",
								)
						)
				);
		}
	}


// End of Section Class //
}

function box_column_display($column){
	global $post;
	
	switch ($column){
		case "bdescription":
			the_excerpt();
			break;
		case "bmedia":
			if(get_post_meta($post->ID, 'the_box_icon', true )){
			
				echo '<img src="'.get_post_meta($post->ID, 'the_box_icon', true ).'" style="width: 80px; margin: 10px; border: 1px solid #ccc; padding: 5px; background: #fff" />';	
			}
			
			break;
		case "box-sets":
			echo get_the_term_list($post->ID, 'box-sets', '', ', ','');
			break;
	}
}

		
function pagelines_default_boxes($post_type){
	
	$default_posts = array_reverse(get_default_fboxes());
	
	foreach($default_posts as $dpost){
		// Create post object
		$default_post = array();
		$default_post['post_title'] = $dpost['title'];
		$default_post['post_content'] = $dpost['text'];
		$default_post['post_type'] = $post_type;
		$default_post['post_status'] = 'publish';
		
		$newPostID = wp_insert_post( $default_post );

		update_post_meta($newPostID, 'the_box_icon', $dpost['media']);
		wp_set_object_terms($newPostID, 'default-boxes', 'box-sets');
		

	}
}
