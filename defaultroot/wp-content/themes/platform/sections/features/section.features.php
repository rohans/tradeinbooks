<?php
/*

	Section: PageLines Features
	Author: Andrew Powers
	Description: Creates a feature slider and custom post type
	Version: 1.0.0
	
*/

class PageLinesFeatures extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('PageLines Features', 'pagelines');
		$id = 'feature';
		
		$this->tax_id = 'feature-sets';
		$section_root_url = $registered_settings['base_url'];
		
		$default_settings = array(
			'description'	=> 'This is your main feature slider.  Add feature text and media through the admin panel.',
			'icon'			=> $section_root_url.'/features.png',
			'version'		=> 'pro',	
		);
		
		$settings = wp_parse_args( $registered_settings, $default_settings );
		
	   parent::__construct($name, $id, $settings);    
   }

	function section_persistent(){
		
		/* 
			Create Custom Post Type 
		*/
			$args = array(
					'label' 			=> __('Features', 'pagelines'),  
					'singular_label' 	=> __('Feature', 'pagelines'),
					'description' 		=> 'For setting slides on the feature page template',
					'taxonomies'		=> array('feature-sets')
				);
			$taxonomies = array(
				'feature-sets' => array(	
						"label" => __('Feature Sets', 'pagelines'), 
						"singular_label" => __('Feature Set', 'pagelines'), 
					)
			);
			$columns = array(
				"cb" 					=> "<input type=\"checkbox\" />",
				"title" 				=> "Title",
				"feature-description" 	=> "Text",
				"feature-media" 		=> "Media",
				"feature-sets"			=> "Feature Sets"
			);
		
			$column_value_function = 'feature_column_display';
		
			$this->post_type = new PageLinesPostType($this->id, $args, $taxonomies, $columns, $column_value_function);
		
				/* Set default posts if none are present */
				
				$this->post_type->set_default_posts('pagelines_default_features');
		
		/*
			Create meta fields for the post type
		*/
			$this->meta_array = array(
					'feature-style' => array(
							'type' => 'select',					
							'inputlabel' => 'Feature Text Position',
							'exp' => 'Select the type of feature style you would like to be shown. E.g. show text on left, right, bottom or not at all (full width)...',
							'selectvalues' => array(
								'text-left'		=> 'Text On Left',
								'text-right' 	=> 'Text On Right',
								'text-bottom' 	=> 'Text On Bottom',
								'text-none' 	=> 'Full Width Image or Media - No Text'
							),
						),
					'feature-background-image' => array(
							'exp' 			=> 'Upload an image for the feature background.',
							'inputlabel' 	=> 'Feature Background Image',
							'type' 			=> 'image_upload'
						),
					
					'feature-design' => array(
							'type'			=> 'select',
							'exp' 			=> 'Select the design style you would like this feature to have (e.g. default background color, text color, overlay? etc...).',
							'inputlabel' 	=> 'Feature Design Style',
							'selectvalues' => array(
								'fstyle-darkbg-overlay' => 'White Text - Dark Feature Background - Transparent Text Overlay (Default)',
								'fstyle-lightbg'		=> 'Black Text - Light Feature Background with Border - No Overlay',
								'fstyle-darkbg'			=> 'White Text - Dark Feature Background - No Overlay',
								'fstyle-nobg'			=> 'Black Text - No Feature Background - No Overlay',
							),
						),
						
					'feature-media-image' => array(
							'version' => 'pro',
							'type' => 'image_upload',					
							'inputlabel' => 'Feature Media Image',
							'exp' => 'Upload an image of the appropriate size for the feature media area.'
						),
					'feature-media' => array(
							'version' => 'pro',
							'type' => 'textarea',					
							'inputlabel' => 'Feature Media HTML (Youtube, Flash etc...)',
							'exp' => 'Feature Page Media HTML or Embed Code.'
						),
					'feature-thumb' => array(
							'exp' 			=> 'Add thumbnails to your post for use in thumb navigation. Create an image 50px wide by 30px tall and upload here.',
							'inputlabel' 	=> 'Feature Thumb (50px by 30px)',
							'type' 			=> 'image_upload'
						),
					'feature-link-url' => array(
							'exp' 			=> 'Adding a URL here will add a link to your feature slide',
							'inputlabel' 	=> 'Feature Link URL',
							'type' 			=> 'text'
						),
					'feature-link-text' => array(
							'default'		=> 'More',
							'exp' 			=> 'Enter the text you would like in your feature link',
							'inputlabel' 	=> 'Link Text',
							'type' 			=> 'text'
						),
					'feature-name' => array(
							'default'		=> '',
							'exp' 			=> 'Enter the title you would like to appear when the feature nav mode is set to feature names',
							'inputlabel' 	=> 'Navigation Label',
							'type' 			=> 'text'
						),
		
			);
			
			if(pagelines('feature_source') == 'posts'){
				$post_types = array($this->id, 'post');
			} else { $post_types = array($this->id); }
			
			$this->meta_settings = array(
					'id' => 'feature-meta',
					'name' => THEMENAME." Feature Options",
					'posttype' => $post_types
				);
			
			$this->meta_options =  new PageLinesMetaOptions($this->meta_array, $this->meta_settings);
			
			/*
				Create meta fields for the page template when using the Feature Template
			*/
				$meta_array = array(

						'feature_items' => array(
							'version' => 'pro',
							'type' => 'text',					
							'inputlabel' => 'Number of Feature Slides',
							'exp' => 'Enter the max number of feature slides to show on this page. Note: If left blank, the number of posts selected under reading settings in the admin will be used.'
						),
						'feature_set' => array(
							'version' => 'pro',
							'type' => 'select_taxonomy',
							
							'taxonomy_id'	=> "feature-sets",				
							'inputlabel' => 'Select Feature Set To Show',
							'exp' => 'If you are using the feature section, select the feature set you would like to show on this page.'
						)

					);
				$meta_settings = array(
						'id' => 'feature-template-meta',
						'name' => THEMENAME." Feature Section Options",
						'posttype' => 'page'
					);

				$this->meta_options =  new PageLinesMetaOptions($meta_array, $meta_settings);

	}

	function section_head() {   
		
	// Get the features from post type

	$feature_posts = $this->get_feature_posts();	
	
?><script type="text/javascript">
		/* <![CDATA[ */
			var $j = jQuery.noConflict();
			$j(document).ready(function () {

			//Feature Cycle Setup	
					$j('#cycle').cycle({ 
					    fx: '<?php if(pagelines('feffect')):?><?php echo pagelines('feffect');?><?php else:?>fade<?php endif;?>',
						sync: <?php if(pagelines('fremovesync')):?>0<?php else:?>1<?php endif;?>,
						timeout: <?php if(pagelines('timeout')):?><?php echo pagelines('timeout');?><?php else:?>0<?php endif;?>,
					    speed:  <?php if(pagelines('fspeed')):?><?php echo pagelines('fspeed');?><?php else:?>1500<?php endif;?>, 
						cleartype:true,
		    			cleartypeNoBg:true,
						pager: 'div#featurenav'
					 });<?php
					
				if(pagelines('feature_nav_type') == 'names'):?>	
				//Overide page numbers on cycle feature with custom text
					$j("div#featurenav").children("a").each(function() {
						<?php $count = 1;?>
						<?php foreach($feature_posts as $feature_post => $feature_info):?>
								if($j(this).html() == "<?php echo $count;?>") { if($j(this).html("<?php 
									if(get_post_meta($feature_info->ID,'feature-name',true)){
										echo get_post_meta($feature_info->ID,'feature-name',true);
									} else echo 'feature ' . $count;
								?>"));}
								
						<?php $count++; endforeach;?>
					});
				<?php elseif(pagelines('feature_nav_type') == 'thumbs'):?>	
				//Overide page numbers on cycle feature with custom text
					$j("div#featurenav").children("a").each(function() {
						<?php $count = 1;?>
						<?php foreach($feature_posts as $feature_post => $feature_info):?>
								if($j(this).html() == "<?php echo $count;?>") {
									$j(this).html('<span class="nav_thumb" style="background:#fff url(<?php echo get_post_meta( $feature_info->ID, "feature-thumb", true); ?>)"><span class="nav_overlay">&nbsp;</span></span>');}
								<?php $count += 1;?>
						<?php endforeach;?>
					});
				<?php elseif(pagelines('feature_nav_type') == 'arrows'):?>	
				//Overide page numbers on cycle feature with arrows
					$j(function() {
    					$j('#arrownav a span').each(function() {
        					eval($j(this).text());
    					});
					});

				<?php endif;
				
				if(pagelines('feature_playpause')):?>	
				// Play Pause
					$j('.playpause').click(function() { 
						if ($j(this).hasClass('pause')) {
							$j('#cycle').cycle('pause');
						 	$j(this).removeClass('pause').addClass('resume');
						} else {
						   	$j(this).removeClass('resume').addClass('pause');
						    $j('#cycle').cycle('resume'); 	
						}

					});
				<?php endif;?>

			});
		/* ]]> */
</script>
<?php }
	
   function section_template() {        
?>		
	<div id="feature_slider" class="fix">
		<div id="feature-area">
			<div id="cycle">
			<?php
					global $post; 
					global $pagelines_layout; 
					$current_page_post = $post;
					
	
				$feature_posts = $this->get_feature_posts();
				if(!empty($feature_posts) && is_array($feature_posts)):
					foreach($feature_posts as $post) : 
						
						// Setup For Std WP functions
						setup_postdata($post); 
						
						// Get Feature Style
						if(get_post_meta($post->ID, 'feature-style', true)) $feature_style = get_post_meta($post->ID, 'feature-style', true);
						else $feature_style = 'text-left';
						
						if(get_post_meta($post->ID, 'feature-link-text', true)) {
							$flink_text = get_post_meta($post->ID, 'feature-link-text', true); 
						}else $flink_text = __('More', 'pagelines');
					
						//Get the Thumbnail URL
						$feature_background_image = get_post_meta($post->ID, 'feature-background-image', true);
						$feature_design = (get_post_meta($post->ID, 'feature-design', true)) ? get_post_meta($post->ID, 'feature-design', true) : '';
				
						?>
						<div id="<?php echo 'feature_'.$post->ID;?>"  class="fcontainer <?php echo $feature_style.' '.$feature_design; ?> fix" >
							<div class="feature-wrap wcontent" <?php if($feature_background_image):?>style="background: url('<?php echo $feature_background_image;?>') no-repeat top center" <?php endif;?>>
								<div class="feature-pad fix">
									<?php pagelines_register_hook( 'pagelines_feature_before', $this->id ); // Hook ?>
									<div class="fcontent <?php if(get_post_meta($post->ID, 'fcontent-bg', true)) echo get_post_meta($post->ID, 'fcontent-bg', true);?>">
										<div class="dcol-pad fix">
												<?php pagelines_register_hook( 'pagelines_fcontent_before', $this->id );?>
												<div class="fheading">
													<h2 class="ftitle"><?php the_title(); ?></h2>
												</div>
												<div class="ftext">
													<?php pagelines_register_hook( 'pagelines_feature_text_top', $this->id ); // Hook ?>
													<div class="fexcerpt">
													<?php 
														if(pagelines_option('feature_source') == 'posts') the_excerpt();
													 	else the_content(); 
													?>
													</div>
													<?php if(get_post_meta($post->ID, 'feature-link-url', true)):?>
														<a class="flink" href="<?php echo get_post_meta($post->ID, 'feature-link-url', true);?>"><span class="featurelink" ><?php echo $flink_text;?></span></a>
													<?php endif;?>
													<?php pagelines_register_hook( 'pagelines_feature_text_bottom', $this->id ); // Hook ?>
													<?php edit_post_link(__('<span>Edit</span>', 'pagelines'), '', '');?>
												</div>
												<?php pagelines_register_hook( 'pagelines_fcontent_after', $this->id );?>
										</div>
										
									</div>
						
									<div class="fmedia" style="">
										<div class="dcol-pad">
											<?php pagelines_register_hook( 'pagelines_feature_media_top', $this->id ); // Hook ?>
											<?php if(get_post_meta($post->ID, 'feature-media-image', true)):?>
												<div class="media-frame">
													<img src="<?php echo get_post_meta( $post->ID, 'feature-media-image', true);?>" />
												</div>
											<?php elseif(get_post_meta( $post->ID, 'feature-media', true)): ?>
												<?php echo get_post_meta( $post->ID, 'feature-media', true); ?>
											<?php endif;?>
											
										</div>
									</div>
									<?php pagelines_register_hook( 'pagelines_feature_after', $this->id );?>
									<div class="clear"></div>
								</div>
							</div>
						</div>
					<?php endforeach; ?>
				<?php else: ?>
					<h4 style="padding: 50px; text-align: center"><?php _e('No feature posts matched this pages criteria', 'pagelines');?></h4>
				<?php endif;?>
				<?php $post = $current_page_post;?>
		
			</div>
		
			<?php if(pagelines('feature_nav_type') == 'arrows'):?> 
				<div id="arrownav">
		 			<a href="#"><span id="prev">&nbsp;</span></a> 
	       			<a href="#"><span id="next">&nbsp;</span></a>
				</div>
			<?php endif;?>
		</div>
		
		<div id="feature-footer" class="<?php e_pagelines('feature_nav_type', '');?> <?php  if( count($this->the_feature_posts) == 1) echo 'nonav';?> fix">
			<div class="feature-footer-pad">
				<?php pagelines_register_hook( 'pagelines_feature_nav_before', $this->id );?>
				<?php if(pagelines('timeout') != 0 && pagelines('feature_playpause')):?><span class="playpause pause"><span>&nbsp;</span></span><?php endif;?>
				<div id="featurenav" class="fix">
					
				</div>
				<div class="clear"></div>
			</div>
		</div>
		
	</div>
	<div class="clear"></div>
<?php }

	function get_feature_posts(){
		global $post;
		global $pagelines_ID;
		
		if(!isset($this->the_feature_posts)){
			
			if(pagelines_option('feature_source') == 'posts'){
				$query_args = array('post_type' => 'post', 'orderby' =>'ID');
				
				if(pagelines_option('feature_category')){
					$query_args = array_merge($query_args, array( 'cat' => pagelines('feature_category') ) );
				}
				
			} else {
				$query_args = array('post_type' => $this->id, 'orderby' =>'ID');
			
				if( get_pagelines_meta('feature_set', $pagelines_ID) ){
					$query_args = array_merge($query_args, array( 'feature-sets' => get_post_meta($pagelines_ID, 'feature_set', true) ) );
				} elseif (pagelines_non_meta_data_page() && pagelines_option('feature_default_tax')){
					$query_args = array_merge($query_args, array( 'feature-sets' => pagelines_option('feature_default_tax') ) );
				}
			}
	
			
			if(pagelines('feature_items', $pagelines_ID)){
				$query_args = array_merge($query_args, array( 'showposts' => pagelines_option('feature_items', $pagelines_ID) ) );
			}
			
			$feature_query = new WP_Query($query_args);
		
		 	$this->the_feature_posts = $feature_query->posts;
			
		
			
			return $this->the_feature_posts;
			
		} else {
			return $this->the_feature_posts;
		}
	
	}

	function section_scripts() {  
		
		return array(
				'cycle' => array(
						'file' => $this->base_url . '/jquery.cycle.js',
						'dependancy' => array('jquery'), 
						'location' => 'footer', 
						'version' => '2.86'
					)	
			);
		
	}

	function section_options($optionset = null, $location = null) {
		
		if($optionset == 'new' && $location == 'bottom'){
			return array(
				'feature_settings' => array(
							'feature_nav_type' => array(
								'default' => "thumbs",
								'version'	=> 'pro',
								'type' => 'radio',
								'selectvalues' => array(
									'nonav' => 'No Navigation',
									'dots' => 'Squares or Dots',
									'names' => 'Feature Names',
									'thumbs' => 'Feature Thumbs (50px by 30px)',								
									'numbers'=>'Numbers',
								),
								'inputlabel' => 'Feature navigation type?',
								'title' => 'Feature Navigation Mode',
								'shortexp' => "Select the mode for your feature navigation",
								'exp' => "Select from the three modes. Using feature names will use the names of the features, using the numbers will use incremental numbers, thumbnails will use feature thumbnails if provided."
							),
							'timeout' => array(
									'default' => 0,
									'version'	=> 'pro',
									'type' => 'text_small',
									'inputlabel' => 'Timeout (ms)',
									'title' => 'Feature Viewing Time (Timeout)',
									'shortexp' => 'The amount of time a feature is set before it transitions in milliseconds',
									'exp' => 'Set this to 0 to only transition on manual navigation. Use milliseconds, for example 10000 equals 10 seconds of timeout.'
								),
							'fspeed' => array(
									'default' => 1500,
									'version'	=> 'pro',
									'type' => 'text_small',
									'inputlabel' => 'Transition Speed (ms)',
									'title' => 'Feature Transition Time (Timeout)',
									'shortexp' => 'The time it takes for your features to transition in milliseconds',
									'exp' => 'Use milliseconds, for example 1500 equals 1.5 seconds of transition time.'
								),
							'feffect' => array(
									'default' => 'fade',
									'version'	=> 'pro',
									'type' => 'select_same',
									'selectvalues' => array('blindX','blindY','blindZ', 'cover','curtainX','curtainY','fade','fadeZoom','growX','growY','none','scrollUp','scrollDown','scrollLeft','scrollRight','scrollHorz','scrollVert','shuffle','slideX','slideY','toss','turnUp','turnDown','turnLeft','turnRight','uncover','wipe','zoom'),
									'inputlabel' => 'Select Transition Effect',
									'title' => 'Transition Effect',
									'shortexp' => "How the features transition",
									'exp' => "This controls the mode with which the features transition to one another."
								),
							'feature_playpause' => array(
									'default' => false,
									'version'	=> 'pro',
									'type' => 'check',
									'inputlabel' => 'Show play pause button?',
									'title' => 'Show Play/Pause Button (when timeout is greater than 0 (auto-transition))',
									'shortexp' => "Show a play/pause button for auto-scrolling features",
									'exp' => "Selecting this option will add a play/pause button for auto-scrolling features, that users can use to pause and watch a video, read a feature, etc.."
								),
							'feature_items' => array(
									'default' => 10,
									'version'	=> 'pro',
									'type' => 'text_small',
									'inputlabel' => 'Number of Features To Show',
									'title' => 'Number of Features',
									'shortexp' => "Limit the number of features that are shown",
									'exp' => "Use this option to limit the number of features shown."
								),
							'feature_source' => array(
									'default' => 'featureposts',
									'version'	=> 'pro',
									'type' => 'select',
									'selectvalues' => array(
										'featureposts' 	=> array("name" => 'Feature Posts (custom post type)'),
										'posts' 		=> array("name" => 'Use Post Category'),
									),
									'inputlabel' => 'Select source',
									'title' => 'Feature Post Source',
									'shortexp' => "Use feature posts or a post category",
									'exp' => "By default the feature section will use feature posts, you can also set the source for features to a blog post category. Set the category ID in its option below. <br/> <strong>NOTE: If set to posts, excerpts will be used as content (control length through them). Also a new option panel will be added on post creation and editing pages.</strong>"
								),
							'feature_category' => array(
									'default' => false,
									'version'	=> 'pro',
									'type' => 'text_small',
									'inputlabel' => 'Post category ID',
									'title' => 'Post Category ID (Blog Post Mode Only)',
									'shortexp' => "Add the category ID to use if sourcing features from blog posts",
									'exp' => "Get the ID for the category to use posts from for features"
								),
							'feature_default_tax' => array(
									'default' 		=> 'default-features',
									'version'		=> 'pro',
									'taxonomy_id'	=> 'feature-sets',
									'type' 			=> 'select_taxonomy',
									'inputlabel' 	=> 'Select Posts/404 Feature-Set',
									'title' 		=> 'Select Feature-Set for Posts & 404 Pages',
									'shortexp' 		=> "Posts pages and similar pages (404) Will Use This set ID To Source Features",
									'exp' 			=> "Posts pages and 404 pages in WordPress don't support meta data so you need to assign a set here. (If you want to use 'features' on these pages.)",
								), 
							'feature_stage_height' => array(
									'default' 		=> '330',
									'version'		=> 'pro',
									'type' 			=> 'css_option',
									'selectors'		=> '#feature-area, .feature-wrap, #feature_slider .fmedia, #feature_slider .fcontent, #feature_slider .text-bottom .fmedia .dcol-pad, #feature_slider .text-bottom .feature-pad, #feature_slider .text-none .fmedia .dcol-pad', 
									'css_prop'		=> 'height', 
									'css_units'		=> 'px',
									'inputlabel' 	=> 'Enter the height (In Pixels) of the Feature Stage Area',
									'title' 		=> 'Feature Area Height',
									'shortexp' 		=> "Use this feature to change the height of your feature area",
									'exp' 			=> "To change the height of your feature area, just enter a number in pixels here.",
								),
							'fremovesync' => array(
									'default' => false,
									'type' => 'check',
									'version'	=> 'pro',
									'inputlabel' => 'Remove Transition Syncing',
									'title' => 'Remove Feature Transition Syncing',
									'shortexp' => "Make features wait to move on until after the previous one has cleared the screen",
									'exp' => "This controls whether features can move on to the screen while another is transitioning off. If removed features will have to leave the screen before the next can transition on to it."
								)

				)
			);
		}
	} 


// End of Section Class //
}

function feature_column_display($column){
	global $post;
	
	switch ($column){
		case "feature-description":
			the_excerpt();
			break;
		case "feature-media":
		 	if(m_pagelines('feature-media', $post->ID)){
				em_pagelines('feature-media', $post->ID);
			}elseif(m_pagelines('feature-media-image', $post->ID)){
				echo '<img src="'.m_pagelines('feature-media', $post->ID).'" style="max-width: 200px; max-height: 200px" />'; 
			}elseif(m_pagelines('feature-background-image', $post->ID)){
				echo '<img src="'.m_pagelines('feature-background-image', $post->ID).'" style="max-width: 200px; max-height: 200px" />'; 
			}
			break;
		case "feature-sets":
			echo get_the_term_list($post->ID, 'feature-sets', '', ', ','');
			break;
	}
}

		
function pagelines_default_features(){
	
	$default_features = array_reverse(get_default_features());

	
	foreach($default_features as $feature){
		// Create post object
		$default_post = array();
		$default_post['post_title'] = $feature['title'];
		$default_post['post_content'] = $feature['text'];
		$default_post['post_type'] = 'feature';
		$default_post['post_status'] = 'publish';
		
		$newPostID = wp_insert_post( $default_post );
	
		update_post_meta($newPostID, 'feature-thumb', $feature['thumb']);
		update_post_meta($newPostID, 'feature-link-url', $feature['link']);
		update_post_meta($newPostID, 'feature-style', $feature['style']);
		update_post_meta($newPostID, 'feature-media', $feature['media']);
		update_post_meta($newPostID, 'feature-background-image', $feature['background']);
		update_post_meta($newPostID, 'feature-design', $feature['fcontent-design']);
		wp_set_object_terms($newPostID, 'default-features', 'feature-sets');
	}
}
