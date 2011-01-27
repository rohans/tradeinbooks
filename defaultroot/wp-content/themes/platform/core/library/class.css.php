<?php
/**
 * 
 *
 *  Write Dynamic CSS to file
 *
 *
 *  @package PageLines Core
 *  @subpackage Sections
 *  @since 4.0
 *
 */
class PageLinesCSS {

	
	function create() {
		
		$this->intro();
		$this->layout();
		$this->dynamic_grid();
		$this->options();
		$this->custom_css();
		
	}

	function intro(){
		$this->css .= "/* PageLines Dynamic CSS - Copyright 2008 - 2010 */\n\n";
		
	}

	function layout(){
		
		global $pagelines_layout; 
		global $post; 

		$this->css .= '/* Dynamic Layout */'."\n\n";
		
		if(VPRO){
			/* Fixed Width Page */
			$fixed_page = $pagelines_layout->content->width + 20;
			$this->css .= ".fixed_width #page, .fixed_width #footer{width:".$fixed_page."px}\n";
		}

		
		/* Content Width */
		$content_with_border = $pagelines_layout->content->width + 2;
		$this->css .= "#page-main .content{width:".$content_with_border."px}\n";
		$this->css .= "#site{min-width:".$content_with_border."px}\n"; // Fix small horizontal scroll issue
		$this->css .= "#site .content, .wcontent, #primary-nav ul.main-nav.nosearch{width:".$pagelines_layout->content->width."px}\n";
		
		/* Navigation Width */
		$nav_width = $pagelines_layout->content->width - 220;
		$this->css .= "#primary-nav ul{width:".$nav_width."px}\n";
		$this->css .= "\n";
		
		/* Layout Modes */
		foreach(get_the_layouts() as $layout_mode){
			$pagelines_layout->build_layout($layout_mode);
		
			//Setup for CSS
			$mode = '.'.$layout_mode.' ';
			$this->css .= $mode."#pagelines_content #column-main, ".$mode.".wmain, ".$mode."#buddypress-page #container{width:". $pagelines_layout->main_content->width."px}\n";
			$this->css .= $mode."#pagelines_content #sidebar1, ".$mode."#buddypress-page #sidebar1{width:". $pagelines_layout->sidebar1->width."px}\n";
			$this->css .= $mode."#pagelines_content #sidebar2, ".$mode."#buddypress-page #sidebar2{width:". $pagelines_layout->sidebar2->width."px}\n";
			$this->css .= $mode."#pagelines_content #column-wrap, ".$mode."#buddypress-page #container{width:". $pagelines_layout->column_wrap->width."px}\n";
			$this->css .= $mode."#pagelines_content #sidebar-wrap, ".$mode."#buddypress-page #sidebar-wrap{width:". $pagelines_layout->sidebar_wrap->width."px}\n\n";
		}
		
	}
	
	function dynamic_grid(){
		global $pagelines_layout; 
		
		/*
			Generate Dynamic Column Widths & Padding
		*/
		$this->css .= '/* Dynamic Grid */'."\n\n";
		for($i = 2; $i <= 5; $i++){
			$this->css .= '.dcol_container_'.$i.'{width: '.$pagelines_layout->dcol[$i]->container_width.'px; float: right;}'."\n";
			$this->css .= '.dcol_'.$i.'{width: '.$pagelines_layout->dcol[$i]->width.'px; margin-left: '.$pagelines_layout->dcol[$i]->gutter_width.'px;}'."\n\n";
		}
		
	}
	
	function options(){
		/*
			Handle Color Select Options and output the required CSS for them...
		*/
		$this->css .= '/* Options */'."\n\n";
		foreach (get_option_array() as $menuitem){

			foreach($menuitem as $optionid => $option_info){ 
				
				if($option_info['type'] == 'css_option' && pagelines_option($optionid)){
					if(isset($option_info['css_prop']) && isset($option_info['selectors'])){
						
						$css_units = (isset($option_info['css_units'])) ? $option_info['css_units'] : '';
						
						$this->css .= $option_info['selectors'].'{'.$option_info['css_prop'].':'.pagelines_option($optionid).$css_units.';}'."\n";
					}

				}
				
				if($option_info['type'] == 'colorpicker' && pagelines_option($optionid)){
					if(isset($option_info['css_prop'])){
						$this->css .= $option_info['selectors'].'{'.$option_info['css_prop'].':'.pagelines_option($optionid).';}'."\n";
					} else {
						$this->css .= $option_info['selectors'].'{color:'.pagelines_option($optionid).';}'."\n";
					}

				} 
			} 
		}
		$this->css .= "\n\n";
	}
	
	function custom_css(){
		$this->css .= '/* Custom CSS */'."\n\n";
		$this->css .= pagelines_option('customcss');
		$this->css .= "\n\n";
	}



}

function pagelines_build_dynamic_css(){
	if (is_writable(CORE . '/css/dynamic.css')) {
		$pagelines_dynamic_css = new PageLinesCSS;
		$pagelines_dynamic_css->create();
		$lid = @fopen(CORE . '/css/dynamic.css', 'w');
		@fwrite($lid, $pagelines_dynamic_css->css);
		@fclose($lid);
	}
}
/********** END OF CSS CLASS  **********/