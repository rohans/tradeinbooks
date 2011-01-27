<?php
/*

	Section: Navigation
	Author: Adam Munns
	Description: Creates site navigation, with optional superfish dropdowns.
	Version: 1.0.0
	
*/

class PageLinesNav extends PageLinesSection {

   function __construct( $registered_settings = array() ) {
	
		$name = __('Navigation', 'pagelines');
		$id = 'primary-nav';
	
		
		$default_settings = array(
			'type' => 'header',
			'workswith' => array('header'),
			'description' => 'Primary Site Navigation.',
			'folder' => 'nav', 
			'init_file' => 'nav.php',
			'icon'			=> CORE_IMAGES . '/admin/map.png'
		);
		
		$settings = wp_parse_args( $registered_settings, $default_settings );

	   parent::__construct($name, $id, $settings);    
   }

	// PHP that always loads no matter if section is added or not -- e.g. creates menus, locations, admin stuff...
	function section_persistent(){
		
		register_nav_menus( array( 'primary' => __( 'Primary Navigation', 'pagelines' ) ) );
		
		
	}
	
   function section_template() { 
		global $post; 			?>
	<div id="nav_row" class="fix">

<?php 	
		global $additional_menu_classes;
		$additional_menu_classes = '';
		if(pagelines_option('hidesearch')){ $additional_menu_classes .= ' nosearch';}
		if(pagelines_option('enable_drop_down')){ $additional_menu_classes .= ' sf-menu';}
		
		function nav_fallback() {?>
			
			<?php global $additional_menu_classes;?>
			<ul id="menu-nav" class="main-nav<?php echo $additional_menu_classes ;?>">

			  	<?php wp_list_pages( 'title_li=&sort_column=menu_order&depth=3'); ?>
			</ul><?php
		}
		if(function_exists('wp_nav_menu')):
		wp_nav_menu( array('menu_class'  => 'main-nav'.$additional_menu_classes, 'container' => null, 'container_class' => '', 'depth' => 3, 'theme_location'=>'primary', 'fallback_cb'=>'nav_fallback') );
		else:
		nav_fallback();
		endif;
	
		if(!pagelines_option('hidesearch')){
			get_search_form(); 
		}
		
		?>
	</div>
	<?php if(!is_404() && isset($post) && is_object($post) && !pagelines_option('hide_sub_header') && ($post->post_parent || wp_list_pages("title_li=&child_of=".$post->ID."&echo=0"))):?>
	<div id="subnav_row" class="fix">
		<div id="subnav" class="fix">
			<ul>
				<?php 
					if(count($post->ancestors)>=2){
						$reverse_ancestors = array_reverse($post->ancestors);
						$children = wp_list_pages("title_li=&depth=1&child_of=".$reverse_ancestors[0]."&echo=0&sort_column=menu_order");	
					}elseif($post->post_parent){ $children = wp_list_pages("title_li=&depth=1&child_of=".$post->post_parent."&echo=0&sort_column=menu_order");
					}else{	$children = wp_list_pages("title_li=&depth=1&child_of=".$post->ID."&echo=0&sort_column=menu_order");}

					if ($children) { echo $children;}
				?>
			</ul>
		</div><!-- /sub nav -->
	</div>
	<?php endif;?>
<?php }

	function section_styles(){
		if(pagelines('enable_drop_down')){
			wp_register_style('superfish', $this->base_url . '/superfish.css', array(), CORE_VERSION, 'screen');
		 	wp_enqueue_style( 'superfish' );
		
			if(pagelines('drop_down_shadow')){
				wp_register_style('superfish-shadow', $this->base_url . '/superfish_shadow.css', array(), CORE_VERSION, 'screen');
				wp_enqueue_style( 'superfish-shadow' );
			}
			
			if(pagelines('drop_down_arrows')){
				wp_register_style('superfish-arrows', $this->base_url . '/superfish_arrows.css', array(), CORE_VERSION, 'screen');
				wp_enqueue_style( 'superfish-arrows' );
			}
		}
	}
	
	function section_head(){
		
		if(pagelines('enable_drop_down')):?>
	<script type="text/javascript"> 
		/* <![CDATA[ */
		var $j = jQuery.noConflict();
		   $j(document).ready(function() { 
		        $j('ul.sf-menu').superfish({ 
		            delay:       100,		// one second delay on mouseout 
		            speed:       'fast',	// faster animation speed 
		            autoArrows:  true,		// disable generation of arrow mark-up 
		            dropShadows: true		// disable drop shadows 
		        }); 
		    });
		/* ]]> */
	</script>			
<?php endif;
}

	function section_scripts() {  
		
		return array(
				'superfish' => array(
						'file' => $this->base_url . '/superfish.js',
						'dependancy' => array('jquery'), 
						'location' => 'footer'
					), 
				'bgiframe' => array(
					'file' => $this->base_url . '/jquery.bgiframe.min.js',
					'dependancy' => array('jquery', 'superfish'), 
					'location' => 'footer'
					),
					
			);
		
	}
	
	function section_options($optionset = null, $location = null) {
	
		if($optionset == 'header_and_nav' && $location == 'bottom'){
			return array(
					'drop_down_options' => array(
							'default' => '',
							'type' => 'check_multi',
							'selectvalues'=> array(
							
								'enable_drop_down' => array(
									'default' => false,
									'type' => 'check',
									'scope' => '',
									'inputlabel' => 'Enable Drop Down Navigation?',
									'title' => 'Drop Down Navigation',
									'shortexp' => 'Enable universal drop down navigation',
									'exp' => 'Checking this option will create drop down menus for all child pages when ' . 
											 'users hover over main navigation items.'
									),
								'drop_down_shadow' => array(
									'default' => true,
									'type' => 'check',
									'scope' => '',
									'inputlabel' => 'Enable Shadow on Drop Down Menu?',
									'title' => 'Drop Down Shadow',
									'shortexp' => 'Enable shadow for drop down navigation',
									'exp' => 'Checking this option will create shadows for the drop down menus'
									),
								'drop_down_arrows' => array(
									'default' => true,
									'type' => 'check',
									'scope' => '',
									'inputlabel' => 'Enable Arrows on Drop Down Menu?',
									'title' => 'Drop Down Arrows',
									'shortexp' => 'Enable arrows for drop down navigation',
									'exp' => 'Checking this option will create arrows for the drop down menus'
									)),
							'inputlabel' => 'Select Which Drop Down Options To Show',
							'title' => 'Drop Down Navigation Options',						
							'shortexp' => 'Select Which To Show',
							'exp' => "Enable drop downs and choose the options you would like to show" 
								 
							),
					'hidesearch' => array(
							'version' => 'pro',
							'default' => false,
							'type' => 'check',
							'inputlabel' => 'Hide search on top of theme?',
							'title' => 'Hide Search',						
							'shortexp' => 'Remove the search field from the top of theme (in sub header and top of sidebar)',
							'exp' => 'Removes the search field from the sub nav and sidebar.'
						), 
					'hide_sub_header' => array(
							'version' => 'pro',
							'default' => false,
							'type' => 'check',
							'inputlabel' => 'Hide Sub Header?',
							'title' => 'Hide Sub Navigation',						
							'shortexp' => 'Removes the sub header that includes the subnav.',
							'exp' => 'This option removes the sub navigation generated when top-level pages are selected that have child pages.'
						),
				
				);

		}
	
	}

}
/*
	End of section class
*/