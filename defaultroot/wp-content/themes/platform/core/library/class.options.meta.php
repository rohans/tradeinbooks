<?php
/**
 * 
 *
 *  PageLines Meta Option Handling
 *
 *
 *  @package PageLines Core
 *  @subpackage Post Types
 *  @since 4.0
 *
 */
class PageLinesMetaOptions {

	var $meta_array = array();	// Controller for drawing meta options
	
	/**
	 * PHP5 constructor
	 *
	 */
	function __construct($meta_array, $settings = array()) {
		$this->meta_array = $meta_array;
		
		$defaults = array(
				
				'id' => 'PageLines-Meta-Options',
				'name' => 'PageLines Custom Options',
				'posttype' => null,
				'location' => 'normal', 
				'priority' => 'low'
			);
		
		$this->settings = wp_parse_args($settings, $defaults); // settings for post type
		
		$this->register_actions();
		
	
	}
	
	function register_actions(){
		
		
		add_action("admin_menu",  array(&$this, 'add_meta_options_box'));
		add_action('save_post', array(&$this, 'save_meta_options'));
		
	}
	
	function add_meta_options_box(){
		
		if(is_array($this->settings['posttype'])){
			foreach($this->settings['posttype'] as $post_type){
				add_meta_box($this->settings['id'], $this->settings['name'], "pagelines_meta_options_callback", $post_type, $this->settings['location'], $this->settings['priority'], array( $this ));
			}
		}else{
			add_meta_box($this->settings['id'], $this->settings['name'], "pagelines_meta_options_callback", $this->settings['posttype'], $this->settings['location'], $this->settings['priority'], array( $this ));
		}

		
	}
	
	function save_meta_options($postID){
	
		if(isset($_POST['post_type'])) $current_post_type = $_POST['post_type'];
		else $current_post_type = false;
		
		if((is_array($this->settings['posttype']) && in_array($current_post_type, $this->settings['posttype'])) || (!is_array($this->settings['posttype']) && $current_post_type == $this->settings['posttype'])){
			$post_type_save = true;
			
		} else {
			$post_type_save = false;
		}

		if((isset($_POST['update']) || isset($_POST['save']) || isset($_POST['publish'])) && $post_type_save){

			foreach($this->meta_array as $optionid => $o){
				
				$option_value =  isset($_POST[$optionid]) ? $_POST[$optionid] : null;
				
				if(!empty($option_value) || get_post_meta($postID, $optionid)){
					update_post_meta($postID, $optionid, $option_value );
				}
			}
		}
	}
	
	function draw_meta_options(){
		
		global $post_ID;?>
		<div class="pagelines_pagepost_options">
	<?php foreach($this->meta_array as $optionid => $o): ?>
				<?php if(isset($o['where']) && $o['where'] != $this->settings['posttype']):?><?php else:?>
						<?php if(VPRO || (!VPRO && $o['version'] != 'pro')):?>
						<div class="page_option">
								<div class="option-description">
									<label for="<?php echo $optionid;?>"><strong><?php echo $o['inputlabel'];?></strong></label><br/>
									<small><?php echo $o['exp'];?></small>
								</div>
								<?php if($o['type']=='check'):?>
								
									<div class="option-inputs">
										
											<input class="admin_checkbox" type="checkbox" id="<?php echo $optionid;?>" name="<?php echo $optionid;?>" <?php checked((bool) m_pagelines($optionid, $post_ID)); ?> />
						
									</div>
								<?php elseif($o['type'] == 'textarea'):?>
									
										<div class="option-inputs">
											<textarea class="html-textarea"  id="<?php echo $optionid;?>" name="<?php echo $optionid;?>" /><?php em_pagelines($optionid, $post_ID); ?></textarea>
										</div>
								
								<?php elseif($o['type'] == 'text' || $o['type'] == 'text_small' || $o['type'] == 'text_link' || $o['type'] == 'text_big'):?>

										<div class="option-inputs">
											<input type="text" class="html-text <?php echo 'meta-'.$o['type'];?>"  id="<?php echo $optionid;?>" name="<?php echo $optionid;?>" value="<?php em_pagelines($optionid, $post_ID); ?>" />
										</div>
								<?php elseif($o['type'] == 'select'):?>
										
										
											<div class="option-inputs">
												<select id="<?php echo $optionid;?>" name="<?php echo $optionid;?>">
													<option value="">&mdash;<?php _e("SELECT", 'pagelines');?>&mdash;</option>

													<?php foreach($o['selectvalues'] as $sval => $sset):?>
														<?php if($o['type']=='select_same'):?>
																<option value="<?php echo $sset;?>" <?php if(get_pagelines_meta($optionid, $post_ID)==$sset) echo 'selected';?>><?php echo $sset;?></option>
														<?php elseif(is_array($sset)):
															$disabled_option = (isset($sset['version']) && $sset['version'] == 'pro' && !VPRO) ? true : false;
														?>
															<option <?php if($disabled_option) echo 'disabled="disabled" class="disabled_option"';?> value="<?php echo $sval;?>" <?php if(get_pagelines_meta($optionid, $post_ID)==$sval) echo 'selected';?>><?php echo $sset['name']; if($disabled_option) echo ' (pro)';?></option>
														<?php else:?>
																<option value="<?php echo $sval;?>" <?php if(get_pagelines_meta($optionid, $post_ID)==$sval) echo 'selected';?>><?php echo $sset;?></option>
														<?php endif;?>

													<?php endforeach;?>
												</select>
											</div>

								<?php elseif($o['type'] == 'count_select'):?>
									
									<div class="option-inputs">
									
										<select id="<?php echo $optionid;?>" name="<?php echo $optionid;?>">
											<option value="">&mdash;SELECT&mdash;</option>
											<?php if(isset($o['count_start'])): $count_start = $o['count_start']; else: $count_start = 0; endif;?>
											<?php for($i = $count_start; $i <= $o['count_number']; $i++):?>
													<option value="<?php echo $i;?>" <?php selected($i, get_pagelines_meta($optionid, $post_ID)); ?>><?php echo $i;?></option>
											<?php endfor;?>
										</select>
									</div>
									
								<?php elseif($o['type'] == 'image_upload'):?>
							
									<div class="option-inputs">
										<p>
											<label class="context" for="<?php echo $optionid;?>"><?php echo $o['inputlabel'];?></label><br/>
											<input class="regular-text uploaded_url" type="text" name="<?php echo $optionid;?>" value="<?php em_pagelines($optionid, $post_ID); ?>" /><br/><br/>


											<span id="<?php echo $optionid;?>" class="image_upload_button button">Upload Image</span>
											<span title="<?php echo $optionid;?>" id="<?php echo $optionid;?>" class="image_reset_button button">Remove</span>
											<input type="hidden" class="ajax_action_url" name="wp_ajax_action_url" value="<?php echo admin_url("admin-ajax.php"); ?>" />
											<input type="hidden" class="image_preview_size" name="img_size_<?php echo $optionid;?>" value="100"/>
										</p>
										<?php if(m_pagelines($optionid, $post_ID)):?>
											<img class="pagelines_image_preview" id="image_<?php echo $optionid;?>" src="<?php em_pagelines($optionid, $post_ID); ?>" style="max-width: 100px"/>
										<?php endif;?>
									</div>
								<?php elseif($o['type'] == 'select_taxonomy'):?>
									<?php $terms_array = get_terms( $o['taxonomy_id']); ?> 
									<div class="option-inputs">
									<?php if(is_array($terms_array) && !empty($terms_array)):?>
									
											<select id="<?php echo $optionid;?>" name="<?php echo $optionid;?>">
												<option value="">&mdash;<?php _e("SELECT", 'pagelines');?>&mdash;</option>
												<?php foreach($terms_array as $term):?>
													<option value="<?php echo $term->slug;?>" <?php if(get_pagelines_meta($optionid, $post_ID)==$term->slug) echo 'selected';?>><?php echo $term->name; ?></option>
												<?php endforeach;?>
											</select>
										
									<?php else:?>
										<div class="meta-message">No sets have been created and added to a post yet!</div>
									<?php endif;?>
									</div>
								<?php endif;?>
							<div class="clear"></div>
						</div>
						<?php endif; ?>
				<?php endif;?>
			<?php endforeach;?>
			<div class="page_option fix update-meta-options">
					<input type="hidden" name="_posttype" value="<?php echo $this->settings['posttype'];?>" />
					<input id="update" class="button-primary" type="submit" value="<?php _e("Update Options",'pagelines'); ?>" accesskey="p" tabindex="5" name="update"/>
			</div>
		</div>
			
	
	
	<?php }
		

}
/////// END OF MetaOptions CLASS ////////


function pagelines_meta_options_callback($post, $object){

	$object['args'][0]->draw_meta_options();
	
}


