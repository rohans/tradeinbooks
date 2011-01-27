<?php 
/**
 * 
 *
 *  Options Layout Class
 *
 *
 *  @package PageLines Core
 *  @subpackage Options
 *  @since 4.0
 *
 */

class PageLinesOptionsUI {

/*
	Build The Layout
*/
	function __construct() {
		$this->option_array = get_option_array();
		
		$this->build_header();
		$this->build_body();
		$this->build_footer();	
		
	}
		
/**
 * Option Interface Header
 *
 */
function build_header(){?>
			<div class='wrap'>
				<table id="optionstable"><tbody><tr><td valign="top" width="100%">

				  <form method="post" action="options.php">

						 <!-- hidden fields -->
							<?php wp_nonce_field('update-options') ?>
							<?php settings_fields(PAGELINES_SETTINGS); // important! ?>
							
							<input type="hidden" name="<?php echo PAGELINES_SETTINGS; ?>[theme_version]>" value="<?php echo esc_attr(pagelines_option('theme_version')); ?>" />
							<input type="hidden" name="<?php echo PAGELINES_SETTINGS; ?>[selectedtab]" id="selectedtab" value="<?php print_pagelines_option('selectedtab', 0); ?>" />
							<input type="hidden" name="<?php echo PAGELINES_SETTINGS; ?>[just_saved]" id="just_saved" value="1" />
							
							<?php $this->_get_confirmations_and_system_checking(); ?>
							

					<?php
						if(isset($_GET['selectedtab']) && !empty($_GET['selectedtab'])) {
							$tab = $_GET['selectedtab'];
						} else {
							$tab = 0;
						}
					?>
				
						<script type="text/javascript">
								jQuery.noConflict();
								jQuery(document).ready(function($) {						
									var $myTabs = $("#tabs").tabs({ fx: { opacity: "toggle", duration: "fast" }, selected: <?php print_pagelines_option('selectedtab', 0); ?>});

									$('#tabs').bind('tabsshow', function(event, ui) {
										$("#selectedtab").val($('#tabs').tabs('option', 'selected'));
									});

									$('#newoption_button').click(function() { // bind click event to link
									    $myTabs.tabs('select', 8); // switch to third tab
									    return false;
									});
								});
						</script>
								<div id="optionsheader">

									<div class="hl"></div>
									<div id="optionstop" class="fix">
										<!-- Form Title -->

										<!-- Configuration intro text -->
										<div class="options_intro">
											<div class="form_title">
												<?php echo THEMENAME;?> <?php _e('Theme Options', 'pagelines');?>
											</div>
											<small><strong>Welcome to <?php echo THEMENAME;?> settings.</strong> 
											This section allows you to customize the form and function of your site. <br/>
											<?php if(VPRO):?> 
												
												We hope you are enjoying this premium product from <a href="http://www.pagelines.com" target="_blank" title="PageLines WordPress Design">PageLines</a>.</small>
											<?php else:?>
												If you'd like more features; please check out <a href="<?php echo PROVERSIONOVERVIEW;?>"><?php echo PROVERSION;?></a> for tons more templates, options and support.
											</small>
											<?php endif;?>
										</div>

										<!-- Pagelines Link -->
										<a class="optionsheader_plink" href="http://www.pagelines.com/" target="_blank">&nbsp;</a>
									</div>



									<div id="optionssubheader">
										<div class="hl"></div>
										<div class="padding fix">
											<div class="subheader_links">
												<a class="sh_preview" href="<?php echo home_url(); ?>/" target="_blank" target-position="front">View Site</a>
												<a class="sh_docs" href="http://www.pagelines.com/docs/" target="_blank" ><?php _e('Docs', 'pagelines');?></a>
												<a class="sh_forum" href="http://www.pagelines.com/forum/" target="_blank" ><?php _e('Forum', 'pagelines');?></a>
											
											</div>

											<div class="subheader_right">

												<input class="button-primary" type="submit" name="submit" value="<?php _e('Save Options', 'pagelines');?>" />
											</div>
										</div>
									</div>



									<div class="clear"></div>
								</div>
		<?php }
		
		function _get_confirmations_and_system_checking(){
			if(isset($_GET['updated']) || isset($_GET['pageaction']) || isset($_GET['reset'])):?>
				
					<div id="message" class="confirmation fade <?php if(isset($_GET['reset']) && !isset($_GET['updated'])) echo ' reset'; elseif(isset($_GET['pageaction']) && $_GET['pageaction']=='activated' && !isset($_GET['updated'])) echo ' activated'; elseif($_GET['pageaction']=='import' && !isset($_GET['updated'])) echo 'settings-import';?>" style="">
						<div class="confirmation-pad">
<?php 							if(isset($_GET['updated'])) echo THEMENAME.' Settings Saved. &nbsp;<a class="sh_preview" href="'.get_option('home').'/" target="_blank" target-position="front">View Your Site &rarr;</a>' ;
								elseif(isset($_GET['pageaction']) && $_GET['pageaction']=='activated' && !isset($_GET['updated'])) echo "Congratulations! ".THEMENAME ." Has Been Successfully Activated.";
								elseif(isset($_GET['pageaction']) && $_GET['pageaction']=='import' && isset($_GET['imported']) && !isset($_GET['updated'])) echo "Congratulations! New settings have been successfully imported.";
								elseif(isset($_GET['pageaction']) && $_GET['pageaction']=='import' && isset($_GET['error']) && !isset($_GET['updated'])) echo "There was an error with import. Please make sure you are using the correct file.";
								elseif(isset($_GET['reset']) && isset($_GET['opt_id']) && $_GET['opt_id'] == 'resettemplates') echo "Template Configuration Restored To Default.";
								elseif(isset($_GET['reset']) && isset($_GET['opt_id']) && $_GET['opt_id'] == 'resetlayout') echo "Layout Dimensions Restored To Default.";
								elseif(isset($_GET['reset'])) echo "Settings Restored To Default.";
						?>
						</div>
					</div>
			<?php endif;?>
			
			<?php if(!is_writable(CORE . '/css/dynamic.css')):?>
			<div id="message" class="confirmation plerror fade">	
				<div class="confirmation-pad">
					<div class="confirmation-head">Dynamic CSS File Not Writable</div>
					<div class="confirmation-subtext">
						Your dynamic CSS file is not writable by the server. To fix just change the file permissions of your dynamic.css file to 666, and you'll be all set. After setting your file permissions, hit the save button so your dynamic CSS can be generated.  <br/>
						<strong>The file is located here: <?php echo CORE . '/css/dynamic.css'; ?></strong>
					</div>
				</div>
			</div>
			<?php endif;?>
			
			<?php if(floatval(phpversion()) < 5.0):?>
			<div id="message" class="confirmation plerror fade">	
				<div class="confirmation-pad">
					<div class="confirmation-head">You are using PHP version <?php echo phpversion(); ?></div>
					<div class="confirmation-subtext">
						Version 5 or higher is required for this theme to work correctly. Please check with your host about upgrading to a newer version. 
					</div>
				</div>
			</div>
			<?php endif;?>
			
			<?php if(ie_version() && ie_version() < 8):?>
			<div id="message" class="confirmation plerror fade">	
				<div class="confirmation-pad">
					<div class="confirmation-head">You are using Internet Explorer version: <?php echo ie_version();?></div>
					<div class="confirmation-subtext">
						Advanced options don't support Internet Explorer version 7 or lower. Please switch to a standards based browser that will allow you to easily configure your site (e.g. Firefox, Chrome, Safari, even IE8 or better would work).
					</div>
				</div>
			</div>
<?php 		endif;
			
			do_action('pagelines_config_checking');

		}
		
		/**
		 * Option Interface Body, including vertical tabbed nav
		 *
		 */
		function build_body(){
			global $pl_section_factory; 
?>
			<div id="tabs">	
				<ul id="tabsnav">
					<li><span class="graphic top">&nbsp;</span></li>
				
					<?php foreach($this->option_array as $menuitem => $options):?>
						<li>
							<a class="<?php echo $menuitem;?>  tabnav-element" href="#<?php echo $menuitem;?>">
								<span><?php echo ucwords(str_replace('_',' ',$menuitem));?></span>
							</a>
						</li>
					<?php endforeach;?>

					<li><span class="graphic bottom">&nbsp;</span></li>
				</ul>
				<div id="thetabs" class="fix">
					<?php if(!VPRO):?>
						<div id="vpro_billboard" class="">
							<div class="vpro_billboard_height">
								<a class="vpro_thumb" href="<?php echo PROVERSIONOVERVIEW;?>"><img src="<?php echo THEME_IMAGES;?>/pro-thumb-125x50.png" alt="<?php echo PROVERSION;?>" /></a>
								<div class="vpro_desc">
									<strong style="font-size: 1.2em">Get the Pro Version </strong><br/>
									<?php echo THEMENAME;?> is the free version of <?php echo PROVERSION;?>, a premium product by <a href="http://www.pagelines.com" target="_blank">PageLines</a>.<br/> 
									Buy <?php echo PROVERSION;?> for tons more options, sections and templates.<br/> 	
								
									<a class="vpro_link" href="#" onClick="jQuery(this).parent().parent().parent().find('.whatsmissing').slideToggle();">Pro Features &darr;</a>
									<a class="vpro_link" href="<?php echo PROVERSIONOVERVIEW;?>">Why Pro?</a>
									<a class="vpro_link"  href="<?php echo PROVERSIONDEMO;?>"><?php echo PROVERSION;?> Demo</a>
									<?php if(defined('PROBUY')):?><a class="vpro_link vpro_call"  href="<?php echo PROBUY;?>"><strong>Buy Now &rarr;</strong></a><?php endif;?>
								
								</div>
							
							</div>
							<div class="whatsmissing">
								 <h3>Pro Only Features</h3>
								<?php if(isset($pl_section_factory->unavailable_sections) && is_array($pl_section_factory->unavailable_sections)):?>
									<p class="mod"><strong>Pro Sections</strong> (drag &amp; drop)<br/>
									<?php foreach( $pl_section_factory->unavailable_sections as $unavailable_section ):?>
										<?php echo $unavailable_section->name;if($unavailable_section !== end($pl_section_factory->unavailable_sections)) echo ' &middot; ';?>
									<?php endforeach;?></p>
								<?php endif;?>
								
								<?php 
								$unavailable_section_areas = get_unavailable_section_areas();
								if(isset($unavailable_section_areas) && is_array($unavailable_section_areas)):?>
									<p class="mod"><strong>Pro Templates &amp; Section Areas</strong> (i.e. places to put sections)<br/>
									<?php foreach( $unavailable_section_areas as $unavailable_section_area_name ):?>
										<?php echo $unavailable_section_area_name; if($unavailable_section_area_name !== end($unavailable_section_areas)) echo ' &middot; ';?> 
									<?php endforeach;?></p>
								<?php endif;?>
								
								<p class="mod"><strong>Pro Settings &amp; Options</strong><br/>
								<?php foreach( get_option_array(true) as $optionset ):?>
									<?php foreach ( $optionset as $optionid => $option_settings): ?>
										<?php if( isset($option_settings['version']) && $option_settings['version'] == 'pro' ):?>
										<?php echo $option_settings['title']; echo ' &middot; ';?>
										<?php endif;?>
									<?php endforeach; ?>
								<?php endforeach;?></p>
								
								<p class="mod"><strong>Plus additional meta options, integrated plugins, technical support, and more...</strong></p>
							
							</div>
						</div>
					<?php endif;?>
					<?php foreach($this->option_array as $menuitem => $options):?>

						<div id="<?php echo $menuitem;?>" class="tabinfo">
							
							<?php if( stripos($menuitem, '_') !== 0 ): ?>
								<div class="tabtitle"><?php echo ucwords(str_replace('_',' ',$menuitem));?></div>
							<?php endif;?>
							
							<?php foreach($options as $optionid => $option_settings){
								$this->option_engine($optionid, $option_settings);
							} ?>
							<div class="clear"></div>
						</div>

					<?php endforeach; ?>	
				</div> <!-- End the tabs -->
			</div> <!-- End tabs -->
<?php 	}
		
/**
 * Option generation engine
 *
 */
function option_engine($optionid, $option_settings){
	
	$defaults = array(
		'default' 				=> '',
		'default_free'		 	=> null,
		'inputlabel' 			=> '',
		'type' 					=> 'check',
		'title' 				=> '',				
		'shortexp' 				=> '',
		'exp'					=> '',
		'wp_option'				=> false,
		'version' 				=> null,
		'version_set_default' 	=> 'free',
		'imagepreview' 			=> 200, 
		'selectvalues' 			=> array(),
		'optionicon' 			=> '', 
		'layout' 				=> 'normal', 
		'count_number' 			=> 10, 
		'selectors'				=> '', 
		'inputsize'				=> 'regular',
		'callback'				=> '',
		'css_prop'				=> '',
	);

	$option_settings = wp_parse_args( $option_settings, $defaults );

	if($option_settings['wp_option']) {
		$optionvalue = get_option($optionid);
	} else {
		$optionvalue = pagelines_option($optionid);
	}

if( !isset( $option_settings['version'] ) || ( isset($option_settings['version']) && $option_settings['version'] == 'free') || (isset($option_settings['version']) && $option_settings['version'] == 'pro' && VPRO ) 
): 
?>
			<div class="optionrow fix <?php if( isset( $option_settings['layout'] ) && $option_settings['layout']=='full' ) echo 'wideinputs'; if( $option_settings['type'] == 'options_info' ) echo ' options_info_row';?>">
				<?php if( $option_settings['title'] ): ?>
				<div class="optiontitle ">
					<?php if( $option_settings['optionicon'] ):?>
						<img src="<?php echo $option_settings['optionicon'];?>" class="optionicon" />
					<?php endif;?>
					<strong><?php echo $option_settings['title'];?></strong><br/>
					<small><?php echo $option_settings['shortexp'];?></small><br/>
				</div>
				<?php endif;?>
				<div class="theinputs ">
					<div class="optioninputs">

<?php 				if($option_settings['type'] == 'image_upload'):

						$this->_get_image_upload_option($optionid, $option_settings, $optionvalue);

 					elseif($option_settings['type'] == 'check'):?>
						<p>
							<label for="<?php echo $optionid;?>" class="context">
								<input class="admin_checkbox" type="checkbox" id="<?php echo $optionid;?>" name="<?php pagelines_option_name($optionid); ?>" <?php checked((bool) $optionvalue); ?> />
								<?php echo $option_settings['inputlabel'];?>
							</label>
						</p>
<?php 				elseif($option_settings['type'] == 'check_multi'):?>

							<?php foreach($option_settings['selectvalues'] as $multi_optionid => $multi_o):?>
							<p>
								<label for="<?php echo $multi_optionid;?>" class="context"><input class="admin_checkbox" type="checkbox" id="<?php echo $multi_optionid;?>" name="<?php pagelines_option_name($multi_optionid); ?>" <?php checked((bool) pagelines_option($multi_optionid)); ?>  /><?php echo $multi_o['inputlabel'];?></label>
							</p>
							<?php endforeach;?>		
<?php 				elseif($option_settings['type'] == 'text_multi'):?>

							<?php foreach($option_settings['selectvalues'] as $multi_optionid => $multi_o):?>
							<p>
								<label for="<?php echo $multi_optionid;?>" class="context"><?php echo $multi_o['inputlabel'];?></label><br/>
								<input class="<?php echo $option_settings['inputsize'];?>-text" type="text" id="<?php echo $multi_optionid;?>" name="<?php pagelines_option_name($multi_optionid); ?>" value="<?php echo esc_attr( pagelines_option($multi_optionid) ); ?>"  />
							</p>
							<?php endforeach;?>
<?php 				elseif($option_settings['type'] == 'text_small' || $option_settings['type'] == 'css_option' ):?>
						<p>
							<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label><br/>
							<input class="small-text"  type="text" name="<?php pagelines_option_name($optionid); ?>" id="<?php echo $optionid;?>" value="<?php esc_attr_e( pagelines_option($optionid) ); ?>" />
						</p>
<?php 				elseif($option_settings['type'] == 'text'):?>
						<p>
							<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label>
							<input class="regular-text"  type="text" name="<?php pagelines_option_name($optionid); ?>" id="<?php echo $optionid;?>" value="<?php esc_attr_e( pagelines_option($optionid) ); ?>" />
						</p>
<?php 				elseif($option_settings['type'] == 'textarea' || $option_settings['type'] == 'textarea_big'):?>
						<p>
							<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label><br/>
							<textarea name="<?php pagelines_option_name($optionid); ?>" class="html-textarea <?php if($option_settings['type']=='textarea_big') echo "longtext";?>" cols="70%" rows="5"><?php esc_attr_e( pagelines_option($optionid) ); ?></textarea>
						</p>
<?php 				elseif($option_settings['type'] == 'count_select'):

						$this->_get_count_select_option($optionid, $option_settings);

					elseif($option_settings['type'] == 'radio'):

						$this->_get_radio_option($optionid, $option_settings);

					elseif($option_settings['type'] == 'select' || $option_settings['type'] == 'select_same'):

						$this->_get_select_option($optionid, $option_settings);
					
					elseif($option_settings['type'] == 'colorpicker'):
					
						$this->_get_color_picker($optionid, $option_settings);
						
					elseif($option_settings['type'] == 'layout'):
					
						$this->_get_layout_builder($optionid, $option_settings); 
					
					elseif($option_settings['type'] == 'layout_select'):	
					
						$this->_get_layout_select($optionid, $option_settings); 
					
					elseif($option_settings['type'] == 'templates'):
					
						$this->_get_template_builder(); 
					
					elseif($option_settings['type'] == 'select_taxonomy'):

						$this->_get_taxonomy_select($optionid, $option_settings);
				
					elseif($option_settings['type'] == 'text_content'):?>
					
						<div class="text_content fix"><?php echo $option_settings['exp'];?></div>
					
<?php 				elseif($option_settings['type'] == 'options_info'):?>

						<span class="toggle_option_info" onClick="jQuery(this).next().slideToggle();">Additional <?php echo ucwords(str_replace('_', ' ', $optionid));?> Info &darr;</span>
						<div class="text_content admin_option_info fix">
							<h3>More Information on <?php echo ucwords(str_replace('_', ' ', $optionid));?></h3>
							<?php echo $option_settings['exp'];?>
						</div>
					
<?php 				elseif($option_settings['type'] == 'reset'):?>
					
						<div class="insidebox context">
							<input class="button-secondary" type="submit" name="<?php pagelines_option_name($optionid); ?>" onClick="return Confirm<?php echo $optionid;?>();" value="<?php echo $option_settings['inputlabel'];?>" /> <?php echo $option_settings['exp'];?>
						</div>
					
						<?php pl_action_confirm('Confirm'.$optionid, 'Are you sure?');?>
					
<?php 				else:?>
					
						<p>Option Type Not Found</p>
					
<?php 				endif;?>

						</div>
					</div>

					<?php if($option_settings['exp'] && $option_settings['type'] != 'text_content' && $option_settings['type'] != 'options_info'):?>
					<div class="theexplanation">
						<div class="context">More Info</div>
						<p><?php echo $option_settings['exp'];?></p>

					</div>
					<?php endif;?>
					<div class="clear"></div>
				</div>
			<?php endif;?>
		<?php }
		
		
/**
 * Option Interface Footer
 *
 */
function build_footer(){?>
		<div id="optionsfooter">
			<div class="hl"></div>
				<div class="theinputs">
					<?php if(VPRO):?><a class="admin_footerlink" href="http://www.pagelines.com/feedback/" target="_blank"><?php _e('Customer Feedback Form &raquo;', 'pagelines');?></a><?php endif;?>
	  	  			<input class="button-primary" type="submit" name="submit" value="<?php _e('Save Options', 'pagelines');?>" />
				</div>
			<div class="clear"></div>
		</div>

		<div class="optionrestore">
				<h4><?php _e('Restore Settings', 'pagelines'); ?></h4>
				<p>
					<div class="context"><input class="button-secondary" type="submit" name="<?php pagelines_option_name('reset'); ?>" onClick="return ConfirmRestore();" value="Restore Options To Default" />Use this button to restore settings to their defaults. (Note: Restore template and layout information on their individual pages.)</div>
					<?php pl_action_confirm('ConfirmRestore', 'Are you sure? This will restore your settings information to default.');?>
				</p>
			
		</div>

		 <!-- close entire form -->
	  	</form>
	
		<div class="optionrestore restore_column_holder fix">
			<div class="restore_column_split">
				<h4><?php _e('Export Settings', 'pagelines'); ?></h4>
				<p class="fix">
					<a class="button-secondary download-button" href="<?php echo admin_url('admin.php?page=pagelines&amp;download=settings'); ?>">Download Theme Settings</a>
				</p>
			</div>
			
			<div class="restore_column_split">
				<h4><?php _e('Import Settings', 'pagelines'); ?></h4>
				<form method="post" enctype="multipart/form-data">
					<input type="hidden" name="settings_upload" value="settings" />
					<p class="form_input">
						<input type="file" class="text_input" name="file" id="settings-file" />
						<input class="button-secondary" type="submit" value="Upload New Settings" onClick="return ConfirmImportSettings();" />
					</p>
				</form>

				<?php pl_action_confirm('ConfirmImportSettings', 'Are you sure? This will overwrite your current settings and configurations with the information in this file!');?>
			</div>
		</div>
	</td></tr></tbody></table>

	<div class="clear"></div>
	</div>
<?php }
		
function _get_image_upload_option( $optionid, $option_settings, $optionvalue = ''){ 
	
	if(get_option($optionid)) {
		pagelines_update_option($optionid, get_option($optionid));
		update_option($optionid, null);
	} 
	?><p>	
		<label class="context" for="<?php echo $optionid;?>"><?php echo $option_settings['inputlabel'];?></label><br/>
		<input class="regular-text uploaded_url" type="text" name="<?php pagelines_option_name($optionid); ?>" value="<?php echo esc_url(pagelines_option($optionid));?>" /><br/><br/>
		<span id="<?php echo $optionid; ?>" class="image_upload_button button">Upload Image</span>
		<span title="<?php echo $optionid;?>" id="reset_<?php echo $optionid; ?>" class="image_reset_button button">Remove</span>
		<input type="hidden" class="ajax_action_url" name="wp_ajax_action_url" value="<?php echo admin_url("admin-ajax.php"); ?>" />
		<input type="hidden" class="image_preview_size" name="img_size_<?php echo $optionid;?>" value="<?php echo $option_settings['imagepreview'];?>"/>
	</p>
	<?php if(pagelines_option($optionid)):?>
		<img class="pagelines_image_preview" id="image_<?php echo $optionid;?>" src="<?php echo pagelines_option($optionid);?>" style="width:<?php echo $option_settings['imagepreview'];?>px"/>
	<?php endif;?>
	
<?php }
		
function _get_count_select_option( $optionid, $option_settings, $optionvalue = '' ){ ?>
	
		<p>
			<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label><br/>
			<select id="<?php echo $optionid;?>" name="<?php pagelines_option_name($optionid); ?>">
				<option value="">&mdash;SELECT&mdash;</option>
				<?php if(isset($option_settings['count_start'])): $count_start = $option_settings['count_start']; else: $count_start = 0; endif;?>
				<?php for($i = $count_start; $i <= $option_settings['count_number']; $i++):?>
						<option value="<?php echo $i;?>" <?php selected($i, pagelines_option($optionid)); ?>><?php echo $i;?></option>
				<?php endfor;?>
			</select>
		</p>
	
<?php }

function _get_radio_option( $optionid, $option_settings ){ ?>
	
		<?php foreach($option_settings['selectvalues'] as $selectid => $selecttext):?>
			<p>
				<input type="radio" id="<?php echo $optionid;?>_<?php echo $selectid;?>" name="<?php pagelines_option_name($optionid); ?>" value="<?php echo $selectid;?>" <?php checked($selectid, pagelines_option($optionid)); ?>> 
				<label for="<?php echo $optionid;?>_<?php echo $selectid;?>"><?php echo $selecttext;?></label>
			</p>
		<?php endforeach;?>
	
<?php }

function _get_select_option( $optionid, $option_settings ){ ?>
	
		<p>
			<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label><br/>
			<select id="<?php echo $optionid;?>" name="<?php pagelines_option_name($optionid); ?>">
				<option value="">&mdash;SELECT&mdash;</option>

				<?php foreach($option_settings['selectvalues'] as $sval => $select_set):?>
					<?php if($option_settings['type'] == 'select_same'):?>
							<option value="<?php echo $select_set;?>" <?php selected($select_set, pagelines_option($optionid)); ?>><?php echo $select_set;?></option>
					<?php else:?>
							<option value="<?php echo $sval;?>" <?php selected($sval, pagelines_option($optionid)); ?>><?php echo $select_set['name'];?></option>
					<?php endif;?>

				<?php endforeach;?>
			</select>
		</p>
	
<?php }

function _get_taxonomy_select( $optionid, $option_settings ){ 
	$terms_array = get_terms( $option_settings['taxonomy_id']); 
	
	if(is_array($terms_array) && !empty($terms_array)):	?>
		<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label><br/>
		<select id="<?php echo $optionid;?>" name="<?php pagelines_option_name($optionid); ?>">
			<option value="">&mdash;<?php _e("SELECT", 'pagelines');?>&mdash;</option>
			<?php foreach($terms_array as $term):?>
				<option value="<?php echo $term->slug;?>" <?php if( pagelines_option($optionid) == $term->slug ) echo 'selected';?>><?php echo $term->name; ?></option>
			<?php endforeach;?>
		</select>
<?php else:?>
		<div class="meta-message"><?php _e('No sets have been created and added to a post yet!', 'pagelines');?></div>
<?php endif;

}

		/**
		 * 
		 *
		 *  Layout Builder (Layout Drag & Drop)
		 *
		 *
		 *  @package PageLines Core
		 *  @subpackage Options
		 *  @since 4.0
		 *
		 */
		function _get_layout_builder($optionid, $option_settings){ ?>
			<div class="layout_controls selected_template">
			

				<div id="layout-dimensions" class="template-edit-panel">
					<h3>Configure Layout Dimensions</h3>
					<div class="select-edit-layout">
						<div class="layout-selections layout-builder-select fix">
							<div class="layout-overview">Select Layout To Edit</div>
							<?php


							global $pagelines_layout;
							foreach(get_the_layouts() as $layout):
							?>
							<div class="layout-select-item">
								<span class="layout-image-border <?php if($pagelines_layout->layout_map['last_edit'] == $layout) echo 'selectedlayout';?>"><span class="layout-image <?php echo $layout;?>">&nbsp;</span></span>
								<input type="radio" class="layoutinput" name="<?php echo PAGELINES_SETTINGS; ?>[layout][last_edit]" value="<?php echo $layout;?>" <?php if($pagelines_layout->layout_map['last_edit'] == $layout) echo 'checked';?>>
							</div>
							<?php endforeach;?>

						</div>	
					</div>
					<?php

				foreach(get_the_layouts() as $layout):

				$buildlayout = new PageLinesLayout($layout);
					?>
				<div class="layouteditor <?php echo $layout;?> <?php if($buildlayout->layout_map['last_edit'] == $layout) echo 'selectededitor';?>">
						<div class="layout-main-content" style="width:<?php echo $buildlayout->builder->bwidth;?>px">

							<div id="innerlayout" class="layout-inner-content" >
								<?php if($buildlayout->west->id != 'hidden'):?>
								<div id="<?php echo $buildlayout->west->id;?>" class="ui-layout-west innerwest loelement locontent"  style="width:<?php echo $buildlayout->west->bwidth;?>px">
									<div class="loelement-pad">
										<div class="loelement-info">
											<div class="layout_text"><?php echo $buildlayout->west->text;?></div>
											<div class="width "><span><?php echo $buildlayout->west->width;?></span>px</div>
										</div>
									</div>
								</div>
								<?php endif;?>
								<div id="<?php echo $buildlayout->center->id;?>" class="ui-layout-center loelement locontent innercenter">
									<div class="loelement-pad">
										<div class="loelement-info">
											<div class="layout_text"><?php echo $buildlayout->center->text;?></div>
											<div class="width "><span><?php echo $buildlayout->center->width;?></span>px</div>
										</div>
									</div>
								</div>
								<?php if( $buildlayout->east->id != 'hidden'):?>
								<div id="<?php echo $buildlayout->east->id;?>" class="ui-layout-east innereast loelement locontent" style="width:<?php echo $buildlayout->east->bwidth;?>px">
									<div class="loelement-pad">
										<div class="loelement-info">
											<div class="layout_text"><?php echo $buildlayout->east->text;?></div>
											<div class="width "><span><?php echo $buildlayout->east->width;?></span>px</div>
										</div>
									</div>
								</div>
								<?php endif;?>
								<div id="contentwidth" class="ui-layout-south loelement locontent" style="background: #fff;">
									<div class="loelement-pad"><div class="loelement-info"><div class="width"><span><?php echo $buildlayout->content->width;?></span>px</div></div></div>
								</div>
								<div id="top" class="ui-layout-north loelement locontent"><div class="loelement-pad"><div class="loelement-info">Content Area</div></div></div>
							</div>
							<div class="margin-west loelement"><div class="loelement-pad"><div class="loelement-info">Margin<div class="width"></div></div></div></div>
							<div class="margin-east loelement"><div class="loelement-pad"><div class="loelement-info">Margin<div class="width"></div></div></div></div>

						</div>


							<div class="layoutinputs">
								<label class="context" for="input-content-width">Global Content Width</label>
								<input type="text" name="<?php echo PAGELINES_SETTINGS; ?>[layout][content_width]" id="input-content-width" value="<?php echo $buildlayout->content->width;?>" size=5 readonly/>
								<label class="context"  for="input-maincolumn-width">Main Column Width</label>
								<input type="text" name="<?php echo PAGELINES_SETTINGS; ?>[layout][<?php echo $layout;?>][maincolumn_width]" id="input-maincolumn-width" value="<?php echo $buildlayout->main_content->width;?>" size=5 readonly/>

								<label class="context"  for="input-primarysidebar-width">Sidebar1 Width</label>
								<input type="text" name="<?php echo PAGELINES_SETTINGS; ?>[layout][<?php echo $layout;?>][primarysidebar_width]" id="input-primarysidebar-width" value="<?php echo  $buildlayout->sidebar1->width;?>" size=5 readonly/>
							</div>
				</div>
				<?php endforeach;?>

			</div>
		</div>
		<?php }
		
/**
 * 
 *
 *  Layout Select (Layout Selector)
 *
 *
 *  @package PageLines Core
 *  @subpackage Options
 *  @since 4.0
 *
 */
function _get_layout_select($optionid, $option_settings){ ?>
	<div id="layout_selector" class="template-edit-panel">

		<div class="layout-selections layout-select-default fix">
			<div class="layout-overview">Default Layout</div>
			<?php


			global $pagelines_layout;
			foreach(get_the_layouts() as $layout):
			?>
			<div class="layout-select-item">
				<span class="layout-image-border <?php if($pagelines_layout->layout_map['saved_layout'] == $layout) echo 'selectedlayout';?>"><span class="layout-image <?php echo $layout;?>">&nbsp;</span></span>
				<input type="radio" class="layoutinput" name="<?php echo PAGELINES_SETTINGS; ?>[layout][saved_layout]" value="<?php echo $layout;?>" <?php if($pagelines_layout->layout_map['saved_layout'] == $layout) echo 'checked';?>>
			</div>
			<?php endforeach;?>

		</div>

	</div>
	<div class="clear"></div>
<?php }

/**
 * 
 *
 *  Template Builder (Sections Drag & Drop)
 *
 *
 *  @package PageLines Core
 *  @subpackage Options
 *  @since 4.0
 *
 */
function _get_template_builder(){
	
		global $pagelines_template;
		global $unavailable_section_areas;

	?>
	<div class="confirm_save">Template Configuration Saved!</div>
	<label for="tselect" class="tselect_label">Select Template</label>
	<select name="tselect" id="tselect" class="template_select" >
<?php 	foreach(the_template_map() as $hook => $hook_info):?>
	
	 <?php if(isset($hook_info['templates'])): ?>
		
				<optgroup label="<?php echo $hook_info['name'];?>" class="selectgroup_header">
			<?php foreach($hook_info['templates'] as $template => $tfield):
					if(!isset($tfield['version']) || ($tfield['version'] == 'pro' && VPRO)):
			?>				
						<option value="<?php echo $hook . '-' . $template;?>"><?php echo $tfield['name'];?></option>
				<?php endif;?>
				<?php endforeach;?>
				</optgroup>
			<?php else: ?>
		
		<?php 
				if(!isset($hook_info['version']) || ($hook_info['version'] == 'pro' && VPRO)):
?>
			<option value="<?php echo $hook;?>" <?php if($hook == 'default') echo 'selected="selected"';?>><?php echo $hook_info['name'];?></option>
<?php endif; ?>
			<?php endif;?>
		
	<?php endforeach;?>
	</select>
	<div class="the_template_builder">
		<?php 
		foreach($pagelines_template->map as $hook_id => $hook_info){
			 if(isset($hook_info['templates'])){
				foreach($hook_info['templates'] as $template_id => $template_info ){
					$this->_sortable_section($template_id, $template_info, $hook_id, $hook_info);
				}
			} else {
				$this->_sortable_section($hook_id, $hook_info);
			}

		}?>
	</div>
	<?php 
	
}

/**
 * 
 *
 *  Get Sortable Sections (Sections Drag & Drop)
 *
 *
 *  @package PageLines Core
 *  @subpackage Options
 *  @since 4.0
 *
 */
function _sortable_section($template, $tfield, $hook_id = null, $hook_info = array()){
		global $pl_section_factory;
		
		$available_sections = $pl_section_factory->sections;
		
		$template_slug = ( isset($hook_id) ) ? $hook_id.'-'.$template : $template;
		
		$template_area = ( isset($hook_id) ) ? $hook_id : $template;
		
			?>

				<div id="template_data" class="<?php echo $template_slug; ?> layout-type-<?php echo $template_area;?>">
					<div class="editingtemplate fix">
						<span class="edit_template_title"><?php echo $tfield['name'];?> Template Sections</span>

					</div>
					<div class="section_layout_description">
						<div class="config_title">Place Template Sections <span class="makesubtle">(drag &amp; drop)</span></div>

						<div class="layout-type-frame">
							<div class="layout-type-thumb"></div>
							Template Area: <?php echo ucwords( str_replace('_', ' ', $template_area) );?>
						</div>
					</div>
					<div id="section_map" class="template-edit-panel ">

						<div class="sbank template_layout">

							<div class="bank_title">Displayed <?php echo $tfield['name'];?> Sections</div>

							<ul id="sortable_template" class="connectedSortable ">
								<?php if(is_array($tfield['sections'])):?>
									<?php foreach($tfield['sections'] as $section):
									
									 		if(isset( $pl_section_factory->sections[$section] )):
									
												$s = $pl_section_factory->sections[$section];
												
												$section_id =  $s->id;
											
										?>
										<li id="section_<?php echo $section; ?>" class="section_bar <?php if($s->settings['required'] == true) echo 'required-section';?>">
											<div class="section-pad fix" style="background: url(<?php echo $s->settings['icon'];?>) no-repeat 10px 10px;">
												
												<h4><?php echo $s->name;?></h4>
												<?php echo $s->settings['description'];

												

												
												$section_control = pagelines_option('section-control');
												
												// Options 
												$check_name = PAGELINES_SETTINGS.'[section-control]['.$template_slug.']['.$section.'][hide]';
												$check_value = isset($section_control[$template_slug][$section]['hide']) ? $section_control[$template_slug][$section]['hide'] : null;
												
												

												$posts_check_type = ($check_value) ? 'show' : 'hide';
												
												if($template == 'posts' || $template == 'single' || $template == '404' ){
													$default_display_check_disabled = true;
												} else {
													$default_display_check_disabled = false;
												}

												if($template_area == 'main' || $template_area == 'templates'){
													
													$posts_check_disabled = true;
												} else {
													$posts_check_label = ucfirst($posts_check_type) .' On Posts Pages';
													$posts_check_name = PAGELINES_SETTINGS.'[section-control]['.$template_slug.']['.$section.'][posts-page]['.$posts_check_type.']';
													$posts_check_value = isset($section_control[$template_slug][$section]['posts-page'][$posts_check_type]) ? $section_control[$template_slug][$section]['posts-page'][$posts_check_type] : null;
													$posts_check_disabled = false;
												}
												
												// Hooks
											
												pagelines_ob_section_template( $s );
												global $registered_hooks;
												
												?>
												<div class="section-moreinfo">
													<div><span class="section-moreinfo-toggle" onClick="jQuery(this).parent().next('.section-moreinfo-info').slideToggle();">Advanced Setup &darr;</span></div>
													<div class="section-moreinfo-info">
														<?php if(!$default_display_check_disabled):?>
														<strong>Settings</strong> 
														<div class="section-options">
															<div class="section-options-row">
																<input class="section_control_check" type="checkbox" id="<?php echo $check_name; ?>" name="<?php echo $check_name; ?>" <?php checked((bool) $check_value); ?> />
																<label for="<?php echo $check_name; ?>">Hide This By Default</label>
															</div>
															<?php if(!$posts_check_disabled):?>
															<div class="section-options-row">
																	<input class="section_control_check" type="checkbox" id="<?php echo $posts_check_name; ?>" name="<?php echo $posts_check_name; ?>" <?php checked((bool) $posts_check_value); ?>/>
																	<label for="<?php echo $posts_check_name; ?>" class="<?php echo 'check_type_'.$posts_check_type; ?>"><?php echo $posts_check_label;?></label>
															</div>
															<?php endif;?>
														</div>
														<?php endif;?>
														<p>
															 <strong>Custom Code Hooks:</strong> 
															<div class="moreinfolist">
																<span>pagelines_before_<?php echo $section_id; ?></span>
																<span>pagelines_inside_top_<?php echo  $section_id; ?></span>
																<span>pagelines_inside_bottom_<?php echo  $section_id; ?></span>
																<span>pagelines_after_<?php echo $section_id; ?></span>
																<?php if(isset($registered_hooks[$section_id]) && is_array($registered_hooks[$section_id])){
																	foreach($registered_hooks[$section_id] as $reg_hook){
																		echo '<span>'.$reg_hook.'</span>';
																	}
																}?>
															</div>
														</p>
														<p><strong>CSS Selectors: </strong>
															<div class="moreinfolist">
																<?php if( (isset($tfield['markup']) && $tfield['markup'] == 'content') || (isset($hook_info['markup']) && $hook_info['markup'] == 'content') ):?>
																	<span>#<?php echo $section_id; ?> <small>(Full Screen Width)</small></span>
																	<span>#<?php echo $section_id; ?> .content <small>(Content Width)</small></span>
																	<span>#<?php echo $section_id; ?> .content-pad <small>(Content Inner)</small></span>
																<?php elseif( (isset($tfield['markup']) && $tfield['markup'] == 'copy') || (isset($hook_info['markup']) && $hook_info['markup'] == 'copy') ):?>
																	<span>#<?php echo $section_id; ?> <small>(Width of Container)</small></span>
																	<span>#<?php echo $section_id; ?> .copy <small>(Section Width)</small></span>
																	<span>#<?php echo $section_id; ?> .copy-pad <small>(Section Inner)</small></span>
																<?php endif;?>
															</div>
														</p>
													</div>
												</div>
											</div>
										</li>
										<?php if(isset($available_sections[$section])) { unset($available_sections[$section]); } ?>
							
									<?php endif; endforeach;?>

								<?php endif;?>
							</ul>
						</div>
						<div class="sbank available_sections">

							<div class="bank_title">Available/Disabled Sections</div>
							<ul id="sortable_sections" class="connectedSortable ">
								<?php 
								foreach($available_sections as $sectionclass => $section):
								
							
										/* Flip values and keys */
										$works_with = array_flip($section->settings['workswith']);
										$fails_with = array_flip($section->settings['failswith']);
										
										$markup_type = (!empty($hook_info)) ? $hook_info['markup'] : $tfield['markup'];
									
										if(isset( $works_with[$template] ) || isset( $works_with[$hook_id]) || isset($works_with[$hook_id.'-'.$template]) || isset($works_with[$markup_type])):?>
											<?php if( !isset($fails_with[$template]) && !isset($fails_with[$hook_id]) ):?>
											<li id="section_<?php echo $sectionclass;?>" class="section_bar" >
												<div class="section-pad fix" style="background: url(<?php echo $section->settings['icon'];?>) no-repeat 10px 10px;">
													<h4><?php echo $section->name;?></h4>
													<?php echo $section->settings['description'];?>
												</div>
											</li>
											<?php endif;?>
										<?php endif;?>
									
								<?php endforeach;?>
							</ul>
						</div>

						<div class="clear"></div>
					</div>


					<div class="clear"></div>
						<div class="vpro_sections_call">
							<?php if(!VPRO):?>
					
									<p>
										<strong>A Note To Free Version Users:</strong><br/> 
										In the Pro version of this product you will find several more "template areas" and HTML sections to play around with.
									</p>
									<p class="mod">
										<?php if(isset($pl_section_factory->unavailable_sections) && is_array($pl_section_factory->unavailable_sections)):?>
											<strong>Missing Pro Sections</strong><br/>
											<?php foreach( $pl_section_factory->unavailable_sections as $unavailable_section ):?>
												<?php echo $unavailable_section->name;if($unavailable_section !== end($pl_section_factory->unavailable_sections)) echo ',';?>
											<?php endforeach;?>
										<?php endif;?>
									</p>
									<p class="mod">
										<?php if(isset($unavailable_section_areas) && is_array($unavailable_section_areas)):?>
											<strong>Missing Pro Templates &amp; Section Areas</strong> (i.e. places to put sections)<br/>
											<?php foreach( $unavailable_section_areas as $unavailable_section_area_name ):?>
												<?php echo $unavailable_section_area_name; if($unavailable_section_area_name !== end($unavailable_section_areas)) echo ',';?> 
											<?php endforeach;?>
										<?php endif;?>
									</p>
						
						
							<?php endif;?>
							
							<p>
								<strong>Section Quick Start</strong><br/> 
								Sections are a super powerful way to control the content on your website. Building your site using sections has just 3 steps...
								<ol>
									<li><strong>Place</strong> Place sections in your templates using the interface above. This controls their order and loading.</li>
									<li><strong>Control</strong> If you want more control over where cross-template sections show; use section settings (under "Advanced Setup").  You can hide 'cross-template' sections, like sidebars, by default and activate them on individual pages/posts or on your blog page.</li>
									<li><strong>Customize</strong> Customize your sections using the theme settings on individual pages/posts and in this panel.  You can also do advanced customization through hooks and custom css (for more info please see the <a href="http://www.pagelines.com/docs/">docs</a>).</li>
								</ol>
							</p>
							
						</div>
						
 
				</div>
	
<?php
}


function _get_color_picker($optionid, $option_settings){ // Color Picker Template?>
	<div>
		<label for="<?php echo $optionid;?>" class="context"><?php echo $option_settings['inputlabel'];?></label><br/>
		<div id="<?php echo $optionid;?>_picker" class="colorSelector"><div></div></div>
		<input class="colorpickerclass"  type="text" name="<?php pagelines_option_name($optionid); ?>" id="<?php echo $optionid;?>" value="<?php echo pagelines_option($optionid); ?>" />
	</div>
<?php  }

} 
// ===============================
// = END OF OPTIONS LAYOUT CLASS =
// ===============================