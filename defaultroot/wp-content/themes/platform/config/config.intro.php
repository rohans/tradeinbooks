<?php


/*
	Theme Introduction 
*/

	function get_theme_intro(){
		
		$intro = '<div class="admin_billboard fix"><div class="admin_theme_screenshot"><img class="" src="'.THEME_ROOT.'/screenshot.png" /></div>' .
					'<div class="admin_billboard_content"><div class="admin_header"><h3 class="admin_header_main">Welcome to '.THEMENAME.'!</h3><h5 class="admin_header_sub">Pro Web Software From PageLines</h5></div>'.
					'<div class="admin_billboard_text">'.THEMENAME.' has tons of customization options and editing features.<br/> Here are a few tips to get you started...<br/><small>(Note: This intro can be removed below.)</small></div>'.
				'</div></div>'.
				 '<ul class="admin_feature_list">'.
					'<li class="feature_firstrule"><div class="feature_icon"></div><strong>The First Rule</strong> <p>If you are a new customer of PageLines, it\'s time we introduce you to the first rule.  The first rule of PageLines is that you come first. We truly appreciate your business and support.</p></li> ' .
					'<li class="feature_support"><div class="feature_icon"></div><strong>Support</strong> <p>For help getting started, we offer our customers tons of support including <a href="http://www.pagelines.com/docs" target="_blank">docs</a>, <a href="http://www.pagelines.com/blog" target="_blank">video tutorials</a>, and the <a href="http://www.pagelines.com/forum" target="_blank">forum</a>, where users can post questions if they can\'t find the info they need.<br/> You can also visit our <a href="http://www.pagelines.com/support/" target="_blank">support page</a> for more info.</p></li> ' .
					'<li class="feature_options"><div class="feature_icon"></div><strong>Settings &amp; Setup</strong> <p>This panel is where you will start the customization of your website. Any options applied through this interface will make changes site-wide.</p><p> There are also several more options that you will find on the bottom of each WordPress page and post interface–where you create and edit content. These allow you to set options specifically related to that page or post.</p><p>  <br/><small>Note: create and save the page or post before setting these options.</small></p></li>' .
					'<li class="feature_templates"><div class="feature_icon"></div><strong>Template Setup</strong> <p>'.THEMENAME.' is equipped with advanced template configuration. This is how you will control where elements like feature sliders, or carousels show up on your site. Now when you create new pages, you not only get to choose the template, but get complete flexibility over how each template looks. </p><p><small>Note: Set them up under "Template Setup" in this panel</small></p></p> <p>Find more information about PageLines sections and templates in the <a href="http://www.pagelines.com/docs">docs</a>. </p></li>' .
					'<li class="feature_plugins"><div class="feature_icon"></div><strong>Plugins</strong> <p>Although '.THEMENAME.' is universally plugin compatible, we have added "advanced" graphical/functional support for several WordPress plugins.</p><p> It\'s your responsibility to install and activate each plugin, which can be done through "<strong>plugins</strong>" &gt; "<strong>Add New</strong>" or through the <strong>developers site</strong> where you can download them manually (e.g. CForms).</p><p>Pre-configured plugins:</p>'.
						'<ul>'.
							'<li class="first"><p><a href="http://buddypress.org/" target="_blank">BuddyPress</a> &amp; <a href="http://wordpress.org/extend/plugins/bp-template-pack/" target="_blank">BuddyPress Template Pack</a> - Social networking for your WordPress site.</p></li>'.
							'<li class=""><p><a href="http://bbpress.org/" target="_blank">bbPress Forum</a> - Matching forum theme for bbPress (Developer Edition Only)</p></li>'.
							'<li><p><a href="http://wordpress.org/extend/plugins/twitter-for-wordpress/" target="_blank">Twitter For WordPress</a> - Latest Twitter Post & Twitter Post Widgets</p></li>'.
							'<li class="first"><p><a href="http://wordpress.org/extend/plugins/post-types-order/" target="_blank">Post Types Order</a> - Allows you to re-order custom post types like features and boxes.</p></li>'.
							'<li><p><a href="http://www.deliciousdays.com/cforms-plugin/" target="_blank">CFormsII</a> - Advanced contact forms that can be used for creating mailing lists, etc..</p></li>'.
							'<li><p><a href="http://wordpress.org/extend/plugins/wp125/" target="_blank">WP125</a> - Used to show 125px by 125px ads or images in your sidebar. (Widget)</p></li>'.
							'<li><p><a href="http://eightface.com/wordpress/flickrrss/" target="_blank">FlickrRSS</a> - Shows pictures from your Flickr Account.  (Widget &amp; Carousel Section)</p></li>'.
							'<li><p><a href="http://wordpress.org/extend/plugins/nextgen-gallery/" target="_blank">NextGen-Gallery</a> - Allows you to create image galleries with special effects.  (Carousel Section)</p></li>'.
							'<li><p><a href="http://wordpress.org/extend/plugins/wp-pagenavi/" target="_blank">Wp-PageNavi</a> - Creates advanced "paginated" post navigation..</p></li>'.
							'<li><p><a href="http://wordpress.org/extend/plugins/breadcrumb-navxt/" target="_blank">Breadcrumb NavXT</a> - Displays a configurable breadcrumb nav on your site</p></li>'.
						'</ul>'.
					'<li class="feature_dynamic"><div class="feature_icon"></div><strong>Widgets and Dynamic Layout</strong> <p>To make it super easy to customize your layout, we have added tons of sidebars and widget areas.  You can find and set these up under "<strong>appearance</strong>" &gt; "<strong>widgets</strong>"</p> <p>Find more information about your widget areas in the <a href="http://www.pagelines.com/docs">docs</a>. </p></li>' .
				'</ul>' .
				'<br/><h3>That\'s it for now! Have fun and good luck.</h3>';

		return apply_filters('pagelines_theme_intro', $intro);
	}
	
