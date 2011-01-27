<?php
function get_option_array( $load_unavailable = false ){
	
	$default_options = array(
		'global_options' => array(
			
				'pagelines_custom_logo' => array(
					'default' 		=> THEME_IMAGES.'/logo-platformpro.png',
					'default_free'	=> THEME_IMAGES.'/logo-platform.png',
					'type' 			=> 'image_upload',
					'imagepreview' 	=> '270',
					'inputlabel' 	=> 'Upload custom logo',
					'title' 		=> 'Custom Header Image',						
					'shortexp' 		=> 'Input Full URL to your custom header or logo image.',
					'exp' 			=> 'Optional way to replace "heading" and "description" text for your website ' . 
							 			'with an image.'
					),
				'pagelines_favicon' => array(
					'default' 		=> 	CORE_IMAGES."/favicon-pagelines.ico",
					'type' 			=> 	'image_upload',
					'imagepreview' 	=> 	'16',
					'title' 		=> 	'Favicon Image',						
					'shortexp' 		=> 	'Input Full URL to favicon image ("favicon.ico" image file)',
					'exp' 			=> 	'Enter the full URL location of your custom "favicon" which is visible in ' .
							 			'browser favorites and tabs.<br/> (<strong>Must be .png or .ico file - 16px by 16px</strong> ).'
				),		
				'twittername' => array(
						'default' => '',
						'type' => 'text',
						'inputlabel' => 'Your Twitter Username',
						'title' => 'Twitter Integration',
						'shortexp' => 'Places your Twitter feed in your site (<em>"Twitter for WordPress" plugin required</em>)',
						'exp' => 'This places your Twitter feed on the site. Leave blank if you want to hide or not use.<br/><br/><strong>Note: "Twitter for WordPress" plugin is required for this to work.</strong>'
				),
				'site_design_mode'		=> array(
						'version' => 'pro',
						'default' => 'full_width',
						'type' => 'select',
						'selectvalues'	=> array(
								'full_width'	=> array("name" => "Full-Width Design Framework"),
								'fixed_width'	=> array("name" => "Fixed-Width Design Framework", "version" => "pro")
							), 
						'inputlabel' => 'Site Design Mode',
						'title' => 'Site Design Mode',						
						'shortexp' => 'Choose between full width HTML or fixed width HTML',
						'exp' => 'There are two css design modes available for '.THEMENAME.'.<br/><br/><strong>Full-Width Mode</strong> Full width design mode allows you to have aspects of your site that are the full-width of your screen; while others are the width of the content area.<br/><br/><strong>Fixed-Width Mode</strong> Fixed width design mode creates a fixed with "page" that can be used as the area for your design.  You can set a background to the page; and the content will have a seperate "fixed-width" background area (i.e. the width of the content).'
					),		
				'pagelines_touchicon' => array(
					'version' 		=> 'pro',
					'default' 		=> '',
					'type' 			=> 	'image_upload',
					'imagepreview' 	=> 	'60',
					'title' 		=> 'Apple Touch Image',						
					'shortexp' 		=> 'Input Full URL to Apple touch image (.jpg, .gif, .png)',
					'exp' => 'Enter the full URL location of your Apple Touch Icon which is visible when ' .
							 'your users set your site as a <strong>webclip</strong> in Apple Iphone and ' . 
							 'Touch Products. It is an image approximately 57px by 57px in either .jpg, ' .
							 '.gif or .png format.'
				),
				
				'pagebg' => array(				
						'default' 		=> '#FFFFFF',
						'type' 			=> 'colorpicker',
						'selectors'		=>	'body #page',
						'css_prop'		=> 'background',
						'inputlabel' 	=> 'Hex Code',
						'title' 		=> 'Page Content Background Color',
						'shortexp' 		=> 'Change the background color of the page content area',
						'exp' 			=> 'Use this option to quickly change the color of the page content background.<br/><br/><strong>Note:</strong> For advanced background effects, we recommend using custom CSS.'
				),
				'bodybg' => array(				
						'default' 		=> '',
						'type' 			=> 'colorpicker',
						'css_prop'		=> 'background',
						'selectors'		=>	'body, body.fixed_width',
						'inputlabel' 	=> 'Hex Code',
						'title' 		=> 'Body Background Color',
						'shortexp' 		=> 'Change the color of the body',
						'exp' 			=> 'Use this option to quickly change the color of the page body, in full-width mode; this is only apparent in the footer. In fixed-width mode this includes the background.<br/><br/> <strong>Note:</strong> For advanced background effects, we recommend using custom CSS.'
				),
			),
		'template_setup' => array(
			'template_setup' => array(
					'type' 			=> 'options_info',
					'layout' 		=> 'full',
					'exp' 			=> '<p>"Template Setup" is where you set up the various templates for your site (e.g. sidebars, page-templates, etc..).</p>'.
									'<p>Your templates structure can be configured using theme "sections" which are drag-and-drop bits of HTML that can moved around in your theme.</p>'.
									'<p><strong>To Use:</strong> Select the template you would like to edit in the drop down menu. Once you have your template selected, you will see a list of its current displayed sections on the left of the "drag and drop" area, and sections that are available to use on the right.<br />'.
									'To activate sections in your template, just drag the section you want to use to the "Displayed Page Content Area" column. To remove a section, just drag the section back to the "Available/Disabled" column on the right.</p>'
				),
			'templates' => array(
					'default' => '',
					'type' => 'templates',
					'layout' => 'full',
					'title' => THEMENAME.' Template Setup',						
					'shortexp' => 'Drag and drop control over your website\'s templates.<br/> Note: Select "Hidden by Default" to hide the section by default; and activate with individual page/post options.',
				),
				
			'resettemplates' => array(
				'default' => '',
				'inputlabel' => __("Reset Template Section Order", 'pagelines'),
				'type' => 'reset',
				'callback'	=> 'reset_templates_to_default',
				'title' => 'Reset Section Order To Default',	
				'layout' => 'full',					
				'shortexp' => 'Changes your template sections back to their default order and layout (options settings are not affected)',
				),
		
				
			
			),
			
		'layout_editor' => array(
			
			'layout_default' => array(
				'default' 		=> 	"one-sidebar-right",
				'type' 			=> 	'layout_select',
				'title' 		=> 	'Default Layout Mode',	
				'layout' 		=> 'full',						
				'shortexp' 		=> 	'Select your default layout mode, this can be changed on individual pages.<br />Once selected, you can adjust the layout in the Layout Builder.',
				'exp' 			=> 	'The default layout for your site; your blog page will always have this layout. Dimensions can be changed using the content layout editor.'
			),
			'layout' => array(
				'default' => 'one-sidebar-right',
				'type' => 'layout',
				'layout' => 'full',
				'title' => 'Content Layout Editor',						
				'shortexp' => 'Configure the default layout for your site which is initially selected in the Default Layout Mode option in Global Options. <br/>This option allows you to adjust columns and margins for the default layout.',
				), 
			'resetlayout' => array(
				'default' => '',
				'inputlabel' => __("Reset Layout", 'pagelines'),
				'type' 		=> 'reset',
				'callback'	=> 'reset_layout_to_default',
				'title' 	=> 'Reset Layout To Default',	
				'layout' => 'full',					
				'shortexp' => 'Changes layout mode and dimensions back to default',
				)
			),
		'header_and_nav' => array(
				'icon_position' => array(
						'version' => 'pro',
						'type' => 'text_multi',
						'inputsize'	=> 'tiny',
						'selectvalues'=> array(
							'icon_pos_bottom'		=> array('inputlabel'=>'Distance From Bottom (in pixels)', 'default'=> 21),
							'icon_pos_right'	=> array('inputlabel'=>'Distance From Right (in pixels)', 'default'=> 1),
						),
						'title' => 'Social Icon Position',
						'shortexp' => 'Control the location of the social icons in the branding section',
						'exp' => 'Set the position of your header icons with these options. They will be relative to the "branding" section of your site.'
				),
				'rsslink' => array(
						'default' => true,
						'type' => 'check',
						'inputlabel' => 'Display the Blog RSS icon and link?',
						'title' => 'News/Blog RSS Icon',
						'shortexp' => 'Places News/Blog RSS icon in your header',
						'exp' => ''
					),
				'icon_social' => array(
						'version' => 'pro',
						'type' => 'text_multi',
						'inputsize'	=> 'regular',
						'selectvalues'=> array(
							'facebooklink'		=> array('inputlabel'=>'Your Facebook Profile URL', 'default'=> ''),
							'twitterlink'		=> array('inputlabel'=>'Your Twitter Profile URL', 'default'=> ''),
							'linkedinlink'		=> array('inputlabel'=>'Your LinkedIn Profile URL', 'default'=> ''),
							'youtubelink'		=> array('inputlabel'=>'Your YouTube Profile URL', 'default'=> ''),
						),
						'title' => 'Social Icons',
						'shortexp' => 'Add social network profile icons to your header',
						'exp' => 'Fill in the URLs of your social networking profiles. This option will create icons in the header/branding section of your site.'
				),
			),
		'blog_and_posts' => array(
				'blog_layout_mode'		=> array(
						'version' => 'pro',
						'default' => 'magazine',
						'type' => 'select',
						'selectvalues'	=> array(
								'magazine'	=> array("name" => "Magazine Layout Mode", "version" => "pro"),
								'blog'		=> array("name" => "Blog Layout Mode")
							), 
						'inputlabel' => 'Post Layout Mode',
						'title' => 'Blog Post Layout Mode',						
						'shortexp' => 'Choose between magazine style and blog style layout.',
						'exp' => 'Choose between two magazine or blog layout mode. <br/><br/> <strong>Magazine Layout Mode</strong><br/> Magazine layout mode makes use of post "clips". These are summarized excerpts shown at half the width of the main content column.<br/>  <strong>Note:</strong> There is an option for showing "full-width" posts on your main "posts" page.<br/><br/><strong>Blog Layout Mode</strong><br/> This is your classical blog layout. Posts span the entire width of the main content column.'
					), 
				'full_column_posts'	=> array(
						'version' => 'pro',
						'default' => 2,
						'type' => 'count_select',
						'count_number'	=> get_option('posts_per_page'),
						'inputlabel' => 'Number of Full Width Posts?',
						'title' => 'Full Width Posts (Magazine Layout Mode Only)',						
						'shortexp' => 'When using magazine layout mode, select the number of "featured" or full-width posts.',
						'exp' => 'Select the number of posts you would like shown at the full width of the main content column in magazine layout mode (the rest will be half-width post "clips").'
					),
					
				'posts_page_layout' => array(
						'type' => 'select',
						'selectvalues'=> array(
							'fullwidth'				=> array( 'name' => 'Fullwidth layout', 'version' => 'pro' ),
							'one-sidebar-right' 	=> array( 'name' => 'One sidebar on right' ),
							'one-sidebar-left'		=> array( 'name' => 'One sidebar on left' ),
							'two-sidebar-right' 	=> array( 'name' => 'Two sidebars on right', 'version' => 'pro' ),
							'two-sidebar-left' 		=> array( 'name' => 'Two sidebars on left', 'version' => 'pro' ),
							'two-sidebar-center' 	=> array( 'name' => 'Two sidebars, one on each side', 'version' => 'pro' ),
						),
						'title'			=> "Posts Page-Content Layout",
						'shortexp'		=> "Select the content layout on posts pages only",
						'inputlabel' => 'Posts Page Layout Mode (optional)',
						'exp' => 'Use this option to change the content layout mode on all posts pages (if different than default layout).'
					),
				'thumb_handling' => array(
						'type' => 'check_multi',
						'selectvalues'=> array(
							'thumb_blog'		=> array('inputlabel'=>'Posts/Blog Page', 'default'=> true),
							'thumb_single'		=> array('inputlabel'=>'Single Post Pages', 'default'=> false),
							'thumb_search' 		=> array('inputlabel'=>'Search Results', 'default'=> false),
							'thumb_category' 	=> array('inputlabel'=>'Category Lists', 'default'=> true),
							'thumb_archive' 	=> array('inputlabel'=>'Post Archives', 'default'=> true),
							'thumb_clip' 		=> array('inputlabel'=>'In Post Clips (Magazine Mode)', 'default'=> true),
						),
						'title' => 'Post Thumbnail Placement',
						'shortexp' => 'Where should the theme use post thumbnails?',
						'exp' => 'Use this option to control where post "featured images" or thumbnails are used. Note: The post clips option only applies when magazine layout is selected.'
				),
				'byline_handling' => array(
						'type' => 'check_multi',
						'selectvalues'=> array(
							'byline_author'			=> array('inputlabel'=>'Author', 'default'=> true),
							'byline_date'			=> array('inputlabel'=>'Date', 'default'=> true),
							'byline_comments' 		=> array('inputlabel'=>'Comments', 'default'=> true),
							'byline_categories' 	=> array('inputlabel'=>'Categories', 'default'=> false),
						),
						'title' => 'Post Byline Information (Blog Mode and Full-Width Posts Only)',
						'shortexp' => 'What should be shown in post bylines?',
						'exp' => 'The byline shows meta information about who wrote the post, what category it is in, etc... Use this option to control what is shown.'
				),
				'excerpt_handling' => array(
						'type' => 'check_multi',
						'selectvalues'=> array(
							'excerpt_blog'		=> array('inputlabel'=>'Posts/Blog Page', 'default'=> true),
							'excerpt_single'	=> array('inputlabel'=>'Single Post Pages', 'default'=> false),
							'excerpt_search'	=> array('inputlabel'=>'Search Results', 'default'=> true),
							'excerpt_category' 	=> array('inputlabel'=>'Category Lists', 'default'=> true),
							'excerpt_archive' 	=> array('inputlabel'=>'Post Archives', 'default'=> true),
						),
						'title' => 'Post Excerpt or Summary Handling',
						'shortexp' => 'Where should the theme use post excerpts when showing full column posts?',
						'exp' => 'This option helps you control where post excerpts are displayed.<br/><br/> <strong>About:</strong> Excerpts are small summaries of articles filled out when creating a post.'
				),
				'pagetitles' => array(
						'version' => 'pro',
						'default' => '',
						'type' => 'check',
						'inputlabel' => 'Automatically show Page titles?',
						'title' => 'Page Titles',						
						'shortexp' => 'Show the title of pages above the page content.',
						'exp' => 'This option will automatically place page titles on all pages.'
				),
				'continue_reading_text' => array(
						'version' => 'pro',
						'default' => 'Continue Reading',
						'type' => 'text',
						'inputlabel' => 'Continue Reading Link Text',
						'title' => '"Continue Reading" Link Text (When Using Excerpts)',						
						'shortexp' => 'The link at the end of your excerpt.',
						'exp' => "This text will be used as the link to your full article when viewing articles on your posts page (when excerpts are turned on)."
				),
				'content_handling' => array(
						'type' => 'check_multi',
						'selectvalues'=> array(
							'content_blog'		=> array('inputlabel'=>'Posts/Blog Page', 'default'=> false),
							'content_search'	=> array('inputlabel'=>'Search Results', 'default'=> false),
							'content_category' 	=> array('inputlabel'=>'Category Lists', 'default'=> false),
							'content_archive' 	=> array('inputlabel'=>'Post Archives', 'default'=> false),
						),
						'title' => 'Full Post Content',
						'shortexp' => 'In addition to single post pages and page templates, where should the theme place the full content of posts?',
						'exp' => 'Choose where the full content of posts is displayed. Choose between all posts pages or just single post pages (i.e. posts pages can just show excerpts or titles).'
				),
				
				'post_footer_social_text' => array(
						'default' => 'If you enjoyed this article, please consider sharing it!',
						'type' => 'text',
						'inputlabel' => 'Post Footer Social Links Text',
						'title' => 'Post Footer Social Links Text',						
						'shortexp' => 'The text next to your social icons',
						'exp' => "Set the text next to your social links shown on single post pages or on all " . 
								 "posts pages if the post footer link is set to 'always sharing links'."
				),
				
				'post_footer_share_links' => array(
						'default' => '',
						'type' => 'check_multi',
						'selectvalues'=> array(
							'share_facebook'=> array('inputlabel'=>'Facebook Sharing Icon', 'default'=> true),
							'share_twitter'=> array('inputlabel'=>'Twitter Sharing Icon', 'default'=> true),
							'share_delicious' => array('inputlabel'=>'Del.icio.us Sharing Icon', 'default'=> true),
							'share_mixx' => array('inputlabel'=>'Mixx Sharing Icon', 'default'=> false),
							'share_digg' => array('inputlabel'=>'Digg Sharing Icon', 'default'=> true),
							'share_stumbleupon' => array('inputlabel'=>'StumbleUpon Sharing Icon', 'default'=> false)
						),
						'inputlabel' => 'Select Which Share Links To Show',
						'title' => 'Post Footer Sharing Icons',						
						'shortexp' => 'Select Which To Show',
						'exp' => "Select which icons you would like to show in your post footer when sharing " . 
								 "links are shown."
			    )
			),
		
		'sidebar_options' => array(
			
				'sidebar_no_default' => array(
						'default' => '',
						'type' => 'check',
						'inputlabel' => 'Hide Sidebars When Empty (no widgets)',
						'title' => 'Remove Default Sidebars When Empty',
						'shortexp' => 'Hide default sidebars when sidebars have no widgets in them',
						'exp' => 'This allows you to remove sidebars completely when they have no widgets in them.'
				),
				'sidebar_wrap_widgets' => array(
						'default' 			=> 'top',
						'version'			=> 'pro',
						'type' 				=> 'select',
						'selectvalues'	=> array(
								'top'			=> array("name" => 'On Top of Sidebar'),
								'bottom'		=> array("name" => 'On Bottom of Sidebar')
							),
						'inputlabel' 		=> 'Sidebar Wrap Widgets Position',
						'title' 			=> 'Sidebar Wrap Widgets',
						'shortexp' 			=> 'Choose whether to show the sidebar wrap widgets on the top or bottom of the sidebar.',
						'exp' 				=> 'You can select whether to show the widgets that you place in the sidebar wrap template in either the top or the bottom of the sidebar.'
				),
				
			),
			
		'footer_options' => array(
				
				'footer_logo' => array(
						'version' => 'pro',
						'default' => THEME_IMAGES.'/logo-platformpro-small.png',
						'type' => 'image_upload',
						'imagepreview' => '100',
						'inputlabel' => 'Add Footer logo',
						'title' => 'Footer Logo',
						'shortexp' => 'Show a logo in the footer',
						'exp' => 'Add the full url of an image for use in the footer. Recommended size: 140px wide.'
				),
				'footer_more' => array(
						'default' => "Thanks for dropping by! Feel free to join the discussion by leaving " . 
									 "comments, and stay updated by subscribing to the <a href='".get_bloginfo('rss2_url')."'>RSS feed</a>.",
						'type' => 'textarea',
						'inputlabel' => 'More Statement In Footer',
						'title' => 'More Statement',
						'shortexp' => 'Add a quick statement for users who want to know more...',
						'exp' => "This statement will show in the footer columns under the word more. It is for users who may want to know more about your company or service."
				),
				'footer_terms' => array(
						'default' => '&copy; '.date('Y').' '.get_bloginfo('name'),
						'type' => 'textarea',
						'inputlabel' => 'Terms line in footer:',
						'title' => 'Site Terms Statement',
						'shortexp' => 'A line in your footer for "terms and conditions text" or similar',
						'exp' => "It's sometimes a good idea to give your users a terms and conditions " .
								 "statement so they know how they should use your service or content."
				)
			),
	
		'text_and_fonts' => array(
				'gfonts' => array(
						'version' => 'pro',
						'default' => false,
						'type' => 'check',
						'inputlabel' => 'Use Google Fonts API',
						'title' => 'Use Google Fonts API',
						'shortexp' => 'Use the Google Fonts API to add cross-browser fonts to your site',
						'exp' => 'Google has recently released a new API for including new "web safe" fonts on your site. To learn more about it, visit the <a href="http://code.google.com/webfonts">Google Fonts homepage</a>.'
				),
				'gfonts_families' => array(
						'version' => 'pro',
						'default' => 'Crimson+Text|Droid+Sans',
						'type' => 'text',
						'inputlabel' => 'Google Font Families',
						'title' => 'Google API: Font Family or Families',
						'shortexp' => 'The fonts used with the Google API',
						'exp' => 'Use this option to setup various Google fonts (Google fonts must be activated).  Learn more on the Google Font <a href="http://code.google.com/apis/webfonts/docs/getting_started.html#Quick_Start">Quick Start</a> page.'
				),
				'typekit_script' => array(
						'version' => 'pro',
						'default' => "",
						'type' => 'textarea',
						'inputlabel' => 'Typekit Header Script',
						'title' => 'Typekit Font Replacement',
						'shortexp' => 'Typekit is a service that allows you to use tons of new fonts on your site.',
						'exp' => 'Typekit is a new service and technique that allows you to use fonts outside of the 10 or so "web-safe" fonts. <br/><br/>' .
								 'Visit <a href="www.typekit.com" target="_blank">Typekit.com</a> to get the script for this option. Instructions for setting up Typekit are <a href="http://typekit.assistly.com/portal/article/6780-Adding-fonts-to-your-site" target="_blank">here</a>.'
				),
				'fontreplacement' => array(
						'version' => 'pro',
						'default' => false,
						'type' => 'check',
						'inputlabel' => 'Use Cufon font replacement?',
						'title' => 'Use Cufon Font Replacement',
						'shortexp' => 'Use a special font replacement technique for certain text',
						'exp' => 'Cufon is a special technique for allowing you to use fonts outside of the 10 or so "web-safe" fonts. <br/><br/>' .
								 THEMENAME.' is equipped to use it.  Select this option to enable it. Visit the <a href="http://cufon.shoqolate.com/generate/">Cufon site</a>.'
				),
				'font_file' => array(
						'version' => 'pro',
						'default' => THEME_JS.'/calluna.font.js',
						'type' => 'text',
						'inputlabel' => 'Cufon replacement font file URL',
						'title' => 'Cufon: Replacement Font File URL',
						'shortexp' => 'The font file used to replace text.',
						'exp' => 'Use the <a href="http://cufon.shoqolate.com/generate/">Cufon site</a> to generate a font file for use with this theme.  Place it in your theme folder and add the full URL to it here. The default font is Museo Sans.'
				),
				'replace_font' => array(
						'version' => 'pro',
						'default' => 'h1',
						'type' => 'text',
						'inputlabel' => 'CSS elements for font replacement',
						'title' => 'Cufon: CSS elements for font replacement',
						'shortexp' => 'Add selectors of elements you would like replaced.',
						'exp' => 'Use standard CSS selectors to replace them with your Cufon font. Font replacement must be enabled.'
				),
				'headercolor' => array(				
						'version' 		=> 'pro',
						'default' 		=> '#000000',
						'type' 			=> 'colorpicker',
						'selectors'		=>	'h1, h2, h3, h4, h5, h6, h1 a, h2 a, h3 a, h4 a, h5 a, .fpost .post-title h2 a',
						'inputlabel' 	=> 'Hex Code',
						'title' 		=> 'Text Header Color (H1,H2, etc...)',
						'shortexp' 		=> 'Change the color of your titles and subtitles',
						'exp' 			=> 'Use "hex" colors. For example #000000 for black, #3399CC for light blue, ' . 
								 		'etc... Visit <a href="http://html-color-codes.com/">this site</a> for a reference.'
				),
				'linkcolor' => array(
						'default'		=> '#225E9B',
						'type' 			=> 'colorpicker',
						'selectors'		=>	'a, #subnav_row li.current_page_item a, #subnav_row li a:hover, #grandchildnav .current_page_item > a, .branding h1 a:hover, .post-comments a:hover, .bbcrumb a:hover, 	#feature_slider .fcontent.fstyle-lightbg a, #feature_slider .fcontent.fstyle-nobg a',
						'inputlabel' 	=> 'Hex Code:',
						'title' 		=> 'Text Link Color',						
						'shortexp' 		=> 'Change the default color of your links as well as other similar elements.',
						'exp' 			=> 'Select a hex color for your site\'s text links.'
				),
				'linkcolor_hover' => array(
						'default' 		=> '',
						'type' 			=> 'colorpicker',
						'selectors'		=>	'a:hover,.commentlist cite a:hover,  #grandchildnav .current_page_item a:hover, .headline h1 a:hover',
						'inputlabel' 	=> 'Hex Code:',
						'title' 		=> 'Text Link Hover Color',						
						'shortexp' 		=> 'Change the default color of your links when users hover over them.',
						'exp' 			=> 'Select a hex color for when users hover over text links.'
				),	
			),
		
		'custom_code' => array(
				'partner_link' => array(
						'default' => '',
						'type' => 'text',
						'inputlabel' => 'Enter Partner Link',
						'title' => 'PageLines Partner Link',
						'shortexp' => 'Change your PageLines footer link to a partner link',
						'exp' => 'If you are a <a href="http://www.pagelines.com/partners">PageLines Partner</a> enter your link here and the footer link will become a partner or affiliate link.'
					),
				'forum_options' => array(
						'default' => '',
						'type' => 'check',
						'inputlabel' => 'Show bbPress Forum Addon Options',
						'title' => 'Activate Forum Options',
						'shortexp' => 'If you have integrated a PageLines bbPress forum, activate its options here.',
						'exp' => 'This theme has some integrated options for its bbPress forum addon (if installed).'
					),
				'customcss' => array(
						'version' => 'pro',
						'default' => 'body{}',
						'type' => 'textarea',
						'layout' => 'full',
						'inputlabel' => 'CSS Rules',
						'title' => 'Custom CSS',
						'shortexp' => 'Insert custom CSS styling here (this will override any default styling)',
						'exp' => '<div class="theexample">Example:<br/> <strong>body{<br/> &nbsp;&nbsp;color:  #3399CC;<br/>&nbsp;&nbsp;line-height: 20px;<br/>&nbsp;&nbsp;font-size: 11px<br/>}</strong></div>Enter CSS Rules to change the style of your site.<br/><br/> A lot can be accomplished by simply changing the default styles of the "body" tag such as "line-height", "font-size", or "color" (as in text color).'
					),	
				'google_ie' => array(
						'default' => false,
						'type' => 'check',
						'inputlabel' => 'Include Google IE Compatibility Script?',
						'title' => 'Google IE Compatibility Fix',
						'shortexp' => 'Include a Google JS script that fixes problems with IE.',
						'exp' => 'More info on this can be found here: <strong>http://code.google.com/p/ie7-js/</strong>.'
				),
				'headerscripts' => array(
						'version' => 'pro',
						'default' => '',
						'type' => 'textarea',
						'layout' => 'full',
						'inputlabel' => 'Headerscripts Code',
						'title' => 'Header Scripts',
						'shortexp' => 'Scripts inserted directly before the end of the HTML &lt;head&gt; tag',
						'exp' => ''
					),
				'footerscripts' => array(
						'default' => '',						
						'type' => 'textarea',
						'layout' => 'full',
						'inputlabel' => 'Footerscripts Code or Analytics',
						'title' => 'Footer Scripts &amp; Analytics',
						'shortexp' => 'Any footer scripts including Google Analytics',
						'exp' => ""
					),
				'asynch_analytics' => array(
						'version' => 'pro',
						'default' => '',						
						'type' => 'textarea',
						'layout' => 'full',
						'inputlabel' => 'Asynchronous Analytics',
						'title' => 'Asynchronous Analytics',
						'shortexp' => 'Placeholder for Google asynchronous analytics. Goes underneath "body" tag.',
						'exp' => ""
					),
				
				'hide_introduction' => array(
						'default' => '',
						'version'	=> 'pro',
						'type' => 'check',
						'inputlabel' => '',
						'inputlabel' => 'Hide the introduction?',
						'title' => 'Show Theme Introduction',
						'shortexp' => 'Uncheck this option to show theme introduction.',
						'exp' => ""
					)
			)
	);
	
	
	if(!pagelines_option('hide_introduction') && VPRO){
		$welcome = array(
		
				'_welcome' => array(
						'theme_introduction' => array(
								'type' => 'text_content',
								'layout' => 'full',
								'exp' => get_theme_intro()
							),
						'hide_introduction' => array(
								'default' => '',
								'type' => 'check',
								'inputlabel' => '',
								'inputlabel' => 'Hide the introduction',
								'title' => 'Remove This Theme Introduction',
								'shortexp' => 'Remove this introduction from the admin.',
								'exp' => "This introduction can be added back under the 'custom code' tab (once hidden)..."
							),
					)
			);
	}else{$welcome = array();}
	
	if( pagelines_option('forum_options') ){
		$forum_options = array(
				'forum_settings' => array(
						'forum_tags' => array(
								'default' => true,
								'type' => 'check',
								'inputlabel' => 'Show tags in sidebar?',
								'title' => 'Tag Cloud In Sidebar',
								'shortexp' => 'Including post tags on the forum sidebar.',
								'exp' => 'Tags are added by users and moderators on your forum and can help people locate posts.'
							),
						'forum_image_1' => array(
								'default' => '',
								'type' => 'image_upload',
								'inputlabel' => 'Upload Forum Image',
								'imagepreview'	=> 125,
								'title' => 'Forum Sidebar Image #1',
								'shortexp' => 'Add a 125px by 125px image to your forum sidebar',
								'exp' => "Spice up your forum with a promotional image in the forum sidebar."
							),
						'forum_image_link_1' => array(
								'default' => '',
								'type' => 'text',
								'inputlabel' => 'Image Link URL',
								'title' => 'Forum Image #1 Link',
								'shortexp' => 'Full URL for your forum image.',
								'exp' => "Add the full url for your forum image."
							),
						'forum_image_2' => array(
								'default' => '',
								'type' => 'image_upload',
								'imagepreview'	=> 125,
								'inputlabel' => 'Upload Forum Image',
								'title' => 'Forum Sidebar Image #2',
								'shortexp' => 'Add a 125px by 125px image to your forum sidebar',
								'exp' => "Spice up your forum with a promotional image in the forum sidebar."
							),
						'forum_image_link_2' => array(
								'default' => '',
								'type' => 'text',
								'inputlabel' => 'Image Link URL',
								'title' => 'Forum Image #2 Link',
								'shortexp' => 'Full URL for your forum image.',
								'exp' => "Add the full url for your forum image."
							),
						'forum_sidebar_link' => array(
								'default' => '#',
								'type' => 'text',
								'inputlabel' => 'Forum Image Caption URL',
								'title' => 'Forum Caption Link URL (Text Link)',
								'shortexp' => 'Add the URL for your forum caption (optional)',
								'exp' => "Text link underneath your forum images."
							),
						'forum_sidebar_link_text' => array(
								'default' => 'About '.get_bloginfo('name'),
								'type' => 'text',
								'inputlabel' => 'Forum Sidebar Link Text',
								'title' => 'Forum Sidebar Link Text',
								'shortexp' => 'The text of your image caption link',
								'exp' => "Change the text of the caption placed under your forum images."
							)
					)
			);
	}else{$forum_options = array();}
	
	/*
		Merge preset functions
	*/
	$optionarray = array_merge($welcome, $default_options, $forum_options);

	/*
		Load Section Options
	*/
	// Comes before, so you can load on to 'new' option sets
	$optionarray =  array_merge(load_section_options('new', 'top', $load_unavailable), $optionarray, load_section_options('new', 'bottom', $load_unavailable));
	
	foreach($optionarray as $optionset => $options){
		$optionarray[$optionset] = array_merge( load_section_options($optionset, 'top', $load_unavailable), $options, load_section_options($optionset, 'bottom', $load_unavailable) );
	}
		
	
	return apply_filters('pagelines_options_array', $optionarray); 
	
}
