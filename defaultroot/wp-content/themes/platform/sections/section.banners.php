<?php
/*

	Section: PageLines Banners	
	Author: Andrew Powers
	Description: Creates banners, great for product tours.
	Version: 1.0.0
	
*/

class PageLinesBanners extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('PageLines Banners', 'pagelines');
		$id = 'banners';
		
		$default_settings = array(
			'description' 	=> 'Creates "banners" (image on one side text on the other). Great for product tours, portfolios, etc...',
			'icon'			=> CORE_IMAGES.'/admin/banners.png', 
			'version'		=> 'pro',
			'failswith'		=> array('404', 'posts')
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
					'label' => __('Banners', 'pagelines'),  
					'singular_label' => __('Banner', 'pagelines'),
					'description' => 'For creating banners in banner section layouts.'
				);
			$taxonomies = array(
				"banner-sets" => array(	
						"label" => __('Banner Sets', 'pagelines'), 
						"singular_label" => __('Banner Set', 'pagelines'), 
					)
			);
			$columns = array(
				"cb" => "<input type=\"checkbox\" />",
				"title" => "Title",
				"banner-description" => "Text",
				"banner-media" => "Media",
				"banner-sets" => "Banner Sets"
			);
		
			$column_value_function = 'banner_column_display';
		
			$this->post_type = new PageLinesPostType($this->id, $args, $taxonomies, $columns, $column_value_function);
		
				/* Set default posts if none are present */
				//$this->post_type->set_default_posts('pagelines_default_banners');


		/*
			Meta Options
		*/
		
				/*
					Create meta fields for the post type
				*/
					$this->meta_array = array(
						
						'the_banner_image' 	=> array(
								'version' => 'pro',
								'type' => 'image_upload',					
								'inputlabel' => 'Banner Media',
								'exp' => 'Upload an image for the banner.'
							),
						'the_banner_media' 		=> array(
								'version' => 'pro',
								'type' => 'textarea',					
								'inputlabel' => 'Banner Media',
								'exp' => 'Add HTML Media for the banner (used if there is no image).'
							),
						'banner_text_width' => array(
								'version' => 'pro',
								'type' => 'text',					
								'inputlabel' => 'Banner Text Width (In %)',
								'exp' => 'Set the width of the text area as a percentage of full content width.  The media area will fill the rest.'
							),
						'banner_align' => array(
							'version' => 'pro',
							'type' => 'select',
							'selectvalues'	=> array(
									'banner_right'	=> array("name" => "Banner Right"),
									'banner_left'	=> array("name" => "Banner Left")
								), 
							'inputlabel' => 'Banner Alignment',				
							'exp' => 'Put the media on the right or the left?',
							
						),
						'banner_text_padding' => array(
							'version' => 'pro',
							'type' => 'text',					
							'inputlabel' => 'Banner Text Padding',
							'exp' => '(optional) Set the padding for the text area. Use CSS shorthand, for example:<strong> 25px 30px 25px 35px</strong>; for top, right, bottom, then left padding.'
							
						),
					);

					$post_types = array($this->id); 

					$this->meta_settings = array(
							'id' => 'banner-meta',
							'name' => THEMENAME." Banner Options",
							'posttype' => $post_types
						);

					$this->meta_options =  new PageLinesMetaOptions($this->meta_array, $this->meta_settings);

					/*
						Create meta fields for the page template when using the Feature Template
					*/
						$meta_array = array(

								'banner_set' => array(
									'version' => 'pro',
									'type' => 'select_taxonomy',

									'taxonomy_id'	=> "banner-sets",				
									'inputlabel' => 'Select Banner Set To Show',
									'exp' => 'If you are using the Banner section, select the banner set you would like to show on this page.'
								),
								
								

							);
						$meta_settings = array(
								'id' => 'banner-template-meta',
								'name' => THEMENAME." Banner Section Options",
								'posttype' => 'page'
							);

						$this->meta_options =  new PageLinesMetaOptions($meta_array, $meta_settings);
	}

   function section_template() {    
	
		
		global $post; 
		$current_post = $post;

		
		
		
?>		
	<div class="banner_container fix">
<?php 
			
 			// Let's Do This...
			$custom_posts = $this->get_banner_posts();
			$custom_posts = (is_array($custom_posts)) ? $custom_posts : array();
			foreach($custom_posts as $post) : setup_postdata($post); $custom = get_post_custom($post->ID); 
			
				$banner_text_width = (pagelines('banner_text_width', $post->ID)) ? pagelines('banner_text_width', $post->ID) : 50;
				$banner_media_width = 100 - $banner_text_width;

				$banner_align = (get_pagelines_meta('banner_align', $post->ID)) ? get_pagelines_meta('banner_align', $post->ID) : 'banner_left';

				if(get_pagelines_meta('the_banner_image', $post->ID)){
					$banner_media = '<img src="'.get_pagelines_meta('the_banner_image', $post->ID).'" alt="'.get_the_title().'" />';
				} elseif(get_pagelines_meta('the_banner_media', $post->ID)){
					$banner_media = get_pagelines_meta('the_banner_media', $post->ID);
				} else { $banner_media = ''; }
				
				$banner_text_padding = (get_pagelines_meta('banner_text_padding', $post->ID)) ? "padding:".get_pagelines_meta('banner_text_padding', $post->ID).";" : "";
			?>
			
				<div class="banner-area <?php echo $banner_align;?>">
					<div class="banner-text" style="width:<?php echo $banner_text_width; ?>%;">
						<div class="banner-text-pad" style="<?php echo $banner_text_padding;?>">
								<div class="banner-title"><h2><?php the_title(); ?></h2></div>
								<div class="banner-content">
									<?php the_content(); ?>
									<?php edit_post_link(__('<small>[Edit Banner]</small>', 'pagelines'), '', '');?>
								</div>
							
						</div>
					</div>
					<div class="banner-media" style="width:<?php echo $banner_media_width; ?>%;" >
						<div class="banner-media-pad">
							<?php echo $banner_media;?>
						</div>
					</div>
					<div class="clear"></div>
				</div>
				
			<?php endforeach;?>
	</div>
	<div class="clear"></div>
<?php  }

	
	function get_banner_posts(){
		global $post;
		
		if(!isset($this->the_banners) && isset($post)){
			
			$query_args = array('post_type' => $this->id, 'orderby' =>'ID');
		
			if( get_pagelines_meta( 'banner_set', $post->ID ) ){
				$query_args = array_merge($query_args, array( 'banner-sets' => get_post_meta($post->ID, 'banner_set', true) ) );
			}
		
			$banner_query = new WP_Query($query_args);
		
		 	$this->the_banners = $banner_query->posts;
			
		 	if(is_array($this->the_banners)) return $this->the_banners;
			else return array();
			
		} elseif(isset($post)) {
			return $this->the_banners;
		}
	
	}


	
// End of Section Class //
}

function banner_column_display($column){
	global $post;
	
	switch ($column){
		case "banner-description":
			the_excerpt();
			break;
		case "banner-media":
			if(get_post_meta($post->ID, 'the_banner_image', true )){
			
				echo '<img src="'.get_post_meta($post->ID, 'the_banner_image', true ).'" style="width: 80px; margin: 10px; border: 1px solid #ccc; padding: 5px; background: #fff" />';	
			}
			
			break;
		case "banner-sets":
			echo get_the_term_list($post->ID, 'banner-sets', '', ', ','');
			break;
	}
}

		
function pagelines_default_banners($post_type){
	
	$default_posts = array_reverse(get_default_banners());
	
	foreach($default_posts as $dpost){
		// Create post object
		$default_post = array();
		$default_post['post_title'] = $dpost['title'];
		$default_post['post_content'] = $dpost['text'];
		$default_post['post_type'] = $post_type;
		$default_post['post_status'] = 'publish';
		
		$newPostID = wp_insert_post( $default_post );

		update_post_meta($newPostID, 'the_banner_image', $dpost['media']);
		wp_set_object_terms($newPostID, 'default-banners', 'banner-sets');
		

	}
}
