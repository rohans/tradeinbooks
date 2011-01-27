<?php
/*

	Section: Carousel
	Author: Andrew Powers
	Description: Creates a flickr, nextgen, or featured image carousel.
	Version: 1.0.0
	
*/

class PageLinesCarousel extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('PageLines Carousel', 'pagelines');
		$id = 'carousel';
	
		
		$default_settings = array(
			'description' 	=> 'This is a javascript carousel that can show images and links from posts, FlickRSS, or NextGen Gallery.', 
			'workswith'		=> array('content', 'header', 'footer'),
			'icon'			=> CORE_IMAGES . '/admin/carousel.png',
			'version'		=> 'pro',
		);
		
		$settings = wp_parse_args( $registered_settings, $default_settings );
		
		parent::__construct($name, $id, $settings);    
   }


	function section_admin(){
		
		
			
		/*
			Create meta fields for the post type
		*/
			$meta_array = array(
			
					'carousel_items' => array(
						'version' => 'pro',
						'type' => 'text',					
						'inputlabel' => 'Max Carousel Items (Carousel Page Template)',
						'exp' => 'The number of items/thumbnails to show in the carousel.'
					),
					'carousel_mode' => array(
						'version' => 'pro',
						'type' => 'select',	
						'selectvalues'=> array(
							'flickr'=> 'Flickr (default)',
							'posts' => 'Post Thumbnails',
							'ngen_gallery' => 'NextGen Gallery'
						),					
						'inputlabel' => 'Carousel Image/Link Mode (Carousel Page Template)',
						'exp' => 'Select the mode that the carousel should use for its thumbnails.<br/><br/>' .
								 '<strong>Flickr</strong> - (default) Uses thumbs from FlickrRSS plugin.<br/><strong> Post Thumbnails</strong> - Uses links and thumbnails from posts <br/>' .
								 '<strong>NextGen Gallery</strong> - Uses an image gallery from the NextGen Gallery Plugin'
					),
					'carousel_ngen_gallery' => array(
						'version' => 'pro',
						'type' => 'text',					
						'inputlabel' => 'NextGen Gallery ID For Carousel (Carousel Page Template / NextGen Mode)',
						'exp' => 'Enter the ID of the NextGen Image gallery for the carousel. <strong>The NextGen Gallery and carousel template must be selected.</strong>'
					),
					'carousel_post_id' => array(
						'version' => 'pro',
						'type' => 'text',					
						'inputlabel' => 'Enter Category Slug (Carousel Posts Mode)',
						'exp' => 'Enter the name or slug of the category that the carousel should use for its images (posts mode only).'
					)
					
				);
			$meta_settings = array(
					'id' => 'carousel-meta',
					'name' => THEMENAME." Carousel Options",
					'posttype' => 'page'
				);
			
			$this->meta_options =  new PageLinesMetaOptions($meta_array, $meta_settings);
			$meta_settings = array(
					'id' => 'carousel-meta',
					'name' => THEMENAME." Carousel Options",
					'posttype' => 'post'
				);
			
			$this->meta_options =  new PageLinesMetaOptions($meta_array, $meta_settings);
	}
	
   function section_template() {        
?>		

	<div class="thecarousel">
		<ul id="mycarousel" class="mycarousel">
			<?php 
			global $post;
			global $pagelines_ID;
			
			// Pictures in Carousel
			if(pagelines('carouselitems', $pagelines_ID)) $carouselitems = pagelines('carouselitems', $pagelines_ID);
			else $carouselitems = 30;
			
			$carousel_post_id = (pagelines_option('carousel_post_id', $pagelines_ID)) ? pagelines_option('carousel_post_id', $pagelines_ID) : null;

			if(pagelines('carousel_image_width', $pagelines_ID)) $carousel_image_width = pagelines('carousel_image_width', $pagelines_ID);
			else $carousel_image_width = 64;
			if(pagelines('carousel_image_height', $pagelines_ID)) $carousel_image_height = pagelines('carousel_image_height', $pagelines_ID);
			else $carousel_image_height = 64;
			
			if(function_exists('nggDisplayRandomImages')  && pagelines('carousel_mode', $post->ID) == 'ngen_gallery'):
				
				if(pagelines('carousel_ngen_gallery', $pagelines_ID)) $ngen_id = pagelines('carousel_ngen_gallery', $pagelines_ID);
				else $ngen_id = 1; 
						
				echo do_shortcode('[nggallery id='.$ngen_id.' template=plcarousel]');
				
			elseif(function_exists('get_flickrRSS') && pagelines('carousel_mode', $pagelines_ID) == 'flickr'):
			
					get_flickrRSS( array(
						'num_items' => $carouselitems, 
						'html' => '<li><a href="%flickr_page%" title="%title%"><img src="%image_square%" alt="%title%"/><span class="list-title">%title%</span></a></li>'	
					));
			
			elseif( (pagelines('carousel_mode', $pagelines_ID) == 'flickr' && !function_exists('get_flickrRSS')) || (pagelines('carousel_mode', $pagelines_ID) == 'ngen_gallery' && !function_exists('nggDisplayRandomImages'))):?>
				
				<div class="carousel_text">
				<?php _e("The plugin for the selected carousel mode (NextGen-Gallery or FlickrRSS) needs to be installed and activated.", 'pagelines');?>
				</div>
				
			<?php else:
			
				$carousel_post_query = 'numberposts='.$carouselitems;
				
				if($carousel_post_id) $carousel_post_query .= '&category_name='.$carousel_post_id;
				
				$recentposts = get_posts($carousel_post_query);
				
				foreach($recentposts as $key => $rpost):?>
					<?php setup_postdata($rpost);?>

						<li class="list-item fix">
							<a class="carousel_image_link" href="<?php echo get_permalink($rpost->ID); ?>">
							<?php if(has_post_thumbnail()): ?>
		                              <?php the_post_thumbnail(array( $carousel_image_width, $carousel_image_height ),array( 'class' => 'list_thumb list-thumb' ));?>
							<?php else: ?>
								<img class="list_thumb list-thumb" src="<?php echo CORE_IMAGES;?>/post-blank.jpg" class="sidebar_thumb" />
							<?php endif;?> 
								<span class="list-title"><?php echo $rpost->post_title; ?></span>
							</a>

						</li>

				<?php endforeach;?>
			<?php endif;?>
		</ul>
	</div>
		
<?php  }

	function section_styles() {
	
		wp_register_style('carousel', $this->base_url . '/carousel.css', array(), CORE_VERSION, 'screen');
	 	wp_enqueue_style( 'carousel' );
		
	}   

	function section_head() {   ?>
		<script type="text/javascript">
		/* <![CDATA[ */
		var $j = jQuery.noConflict();

		$j(document).ready(function () {
		    $j(".thecarousel").show();
			$j(".thecarousel").jcarousel({
				wrap: 'circular', 
				visible: <?php e_pagelines('carousel_display_items', 9); ?>, 
				easing: 'swing',
				scroll: <?php e_pagelines('carousel_scroll_items', 6); ?>,
				animation: 'slow'
			});
		});
		/* ]]> */
		</script>

	<?php }

	function section_scripts() {  
		
		return array(
				'jcarousel' => array(
						'file' => $this->base_url . '/carousel.jcarousel.js',
						'dependancy' => array('jquery'), 
						'location' => 'footer'
					)
						
			);
		
	}

	function section_options($optionset = null, $location = null) {
	
		if($optionset == 'new' && $location == 'bottom'){
			return array(
				'carousel_settings' => array(
					'carousel_mode' => array(
							'type'			=> 'select',
							'default'		=> 'posts',
							'title'			=> 'Carousel Image Mode', 
							'shortexp'		=> 'Where the carousel is going to get its images.', 
							'selectvalues'=> array(
								'posts' 		=> array("name" => 'Post Featured Images (default)'),
								'flickr'		=> array("name" => 'FlickrRSS Plugin'),
								'ngen_gallery' 	=> array("name" => 'NextGen Gallery Plugin')
							),					
							'inputlabel' 	=> 'Carousel Image/Link Mode',
							'exp' 			=> 'Select the mode that the carousel should use for its thumbnails.<br/><br/>' .
									 		'<strong> Post Featured Images</strong> - Uses featured images from posts <br/><strong>FlickrRSS</strong> - Uses thumbs from FlickrRSS plugin.<br/>' .
									 		'<strong>NextGen Gallery</strong> - Uses an image gallery from the NextGen Gallery Plugin'
						),
					'carousel_items' => array(
						'default'		=> 30, 
						'type' 			=> 'text_small',		
						'title'			=> 'Rotating Carousel Items', 
						'shortexp'		=> 'The number of rotating images in your carousel',
						'inputlabel' 	=> 'Max Rotating Carousel Items',
						'exp' 			=> 'This option sets the number of items that will be rotated through in your carousel.'
					),
					'carousel_display_items' => array(
						'default'		=> 7, 
						'type' 			=> 'text_small',		
						'title'			=> 'Displayed Carousel Items', 
						'shortexp'		=> 'The number of displayed images in your carousel',
						'inputlabel' 	=> 'Displayed Carousel Items',
						'exp' 			=> 'This option sets the number of images that will be displayed in the carousel at any given time.'
					),
					'carousel_scroll_items' => array(
						'default'		=> 4, 
						'type' 			=> 'text_small',		
						'title'			=> 'Scrolled Carousel Items', 
						'shortexp'		=> 'The number of images scrolled in one click',
						'inputlabel' 	=> 'Items to scroll',
						'exp' 			=> 'This option sets the number of images that will scroll when a user clicks the arrows, etc..'
					),
					'carousel_image_dimensions' => array(
							'type' => 'text_multi',
							'selectvalues'=> array(
								'carousel_image_width'		=> array('inputlabel'=>'Max Image Width (in pixels)', 'default'	=> 64),
								'carousel_image_height'		=> array('inputlabel'=>'Max Image Height (in pixels)', 'default' => 64),
							),
							'title' => 'Carousel Image Dimensions (Posts Mode Only)',
							'shortexp' => 'Control the dimensions of the carousel images',
							'exp' => 'Use this option to control the max height and width of the images in the carousel. You may have to use this option in conjunction with the scroll items option.<br/><br/> For the FlickrRSS and NextGen Gallery modes, image sizes are set by Flickr thumb sizes and the NextGen Gallery plugin respectively.'
					),
					'carousel_post_id' => array(
						'default'		=> '', 
						'type' 			=> 'text',		
						'title'			=> 'Carousel - Post Category Name', 
						'shortexp'		=> 'The category slug to pull posts from',
						'inputlabel' 	=> 'Category Slug (Optional)',
						'exp' 			=> 'Posts Mode - Select the default category for carousel post images.  If not set, the carousel will get the most recent posts.'
					),
					'carousel_ngen_gallery' => array(
							'type' 			=> 'text_small',
							'default'		=> '', 
							'title'			=> 'NextGen Gallery ID (NextGen Gallery Mode Only)', 
							'shortexp'		=> 'The ID of the NextGen Gallery selection you would like to use.', 
							'inputlabel' 	=> 'NextGen Gallery ID For Carousel (<em>NextGen Gallery Mode Only</em>)',
							'exp' 			=> 'Enter the ID of the NextGen Image gallery for the carousel. <strong>The NextGen Gallery and carousel template must be selected.</strong>'
						),
					)
				);

	} 
}

// End of Section Class //
}