<?php


function get_default_features(){
	return array(
			'1' => array(
		        	'title' 			=> 'Welcome to PlatformPro',
		        	'text' 				=> 'Welcome to PlatformPro Framework, we hope you are enjoying this premium product from PageLines.',
		        	'media' 			=> '',
					'style'				=> 'text-none',
		        	'link' 				=> '#fake_link',
					'background' 		=> THEME_IMAGES.'/feature1.jpg',
					'name'				=>'PlatformPro',
					'fcontent-design'	=> '',
					'thumb'				=> THEME_IMAGES.'/fthumb1.png'
		    ),
			'2' => array(
		        	'title' 		=> 'YouTube Video',
		        	'text' 			=> 'A video on changing things.',
		        	'media'		 	=> '<object width="960" height="330"><param name="movie" value="http://www.youtube.com/v/T6MhAwQ64c0&amp;hl=en_US&amp;fs=1?hd=1&amp;showinfo=0"></param><param name="allowFullScreen" value="true"></param><param name="allowscriptaccess" value="always"></param><embed src="http://www.youtube.com/v/T6MhAwQ64c0&amp;hl=en_US&amp;fs=1?hd=1&amp;showinfo=0" type="application/x-shockwave-flash" allowscriptaccess="always" allowfullscreen="true" width="960" height="330"></embed></object>',
		        	'style'			=> 'text-none',
					'link' 			=> '#fake_link',
					'background' 	=> '',
					'name'			=>	'Media',
					'fcontent-design'	=> '',
					'thumb'				=> THEME_IMAGES.'/fthumb2.png'
		    ),
			'3' => array(
				 	'title' 		=> '<small>WordPress Framework By</small> PageLines',
		        	'text' 			=> 'Welcome to a professional WordPress framework by PageLines. Designed for you in San Diego, California.',
		        	'media' 		=> '',
		        	'style'			=> 'text-right',
					'link' 			=> '#fake_link',
					'background' 	=> THEME_IMAGES.'/feature2.jpg',
					'name'			=>	'Design',
					'fcontent-design'	=> '',
					'thumb'				=> THEME_IMAGES.'/fthumb3.png'
		    ),
			'4' => array(
				 	'title' 		=> '<small>Web Design</small> Redesigned.',
		        	'text' 			=> 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
		        	'media' 		=> '',
		        	'style'			=> 'text-left',
					'link' 			=> '#fake_link',
					'background' 	=> THEME_IMAGES.'/feature3.jpg',
					'name'			=> 'Pro',
					'fcontent-design'	=> '',
					'thumb'				=> THEME_IMAGES.'/fthumb4.png'
		    ), 
			'5' => array(
		        	'title' 		=> '<small>Make An</small> Impression',
		        	'text' 			=> 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quam quam, dignissim eu dignissim et,<br/> accumsan ullamcorper risus. Aliquam rutrum, lorem et ornare malesuada, mi magna placerat mi, bibendum volutpat lectus. Morbi nec purus dolor.',
		        	'media'		 	=> '',
		        	'style'			=> 'text-bottom',
					'link' 			=> '#fake_link',
					'background' 	=> THEME_IMAGES.'/feature4.jpg',
					'name'			=>'Media',
					'fcontent-design'	=> '',
					'thumb'				=> THEME_IMAGES.'/fthumb5.png'
		    ),
	);
}


function get_default_fboxes(){
	return array(
		'1' => array(
	        	'title' => 'Rock The Web!',
	        	'text' => 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim.', 
				'media' => THEME_IMAGES.'/fbox1.png'
	    ),
		'2' => array(
	        	'title' => 'PageLines Framework',
	        	'text' => 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in.',
				'media' => THEME_IMAGES.'/fbox2.png'
	    ),
		'3' => array(
	        	'title' => 'Drag&amp;Drop Design',
	        	'text' => 'In voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur occaecat cupidatat non proident, in culpas officia deserunt.',
				'media' => THEME_IMAGES.'/fbox3.png'
	    )
	);
}

function get_default_banners(){
	return array();
}
