<?php

// ========================
// = Theme Custom Widgets =
// ========================

if(VPRO && class_exists('WP_Widget')){

	// Grand Child Navigation
		class PageLines_GrandChild extends WP_Widget {
	
		   function PageLines_GrandChild() {
			   $widget_ops = array('description' => 'Creates a third tier navigation (Grandchild). Shows on pages when there are three levels; based on page heirarchy.' );
			   parent::WP_Widget(false, $name = __('PageLines Pro - Grandchild Nav', 'pagelines'), $widget_ops);    
		   }
	
		   function widget($args, $instance) {        
			   extract( $args );
		
				// THE TEMPLATE
			  	include(THEME_WIDGETS.'/widget.grandchildnav.php');
		   }
	
		   function update($new_instance, $old_instance) {                
			   return $new_instance;
		   }
	
		   function form($instance) { ?>    	   
			<p>	<?php _e('There are no options for this widget.','pagelines');?></p>
		<?php 
		   }
	
		} 
		register_widget('PageLines_GrandChild');



}


