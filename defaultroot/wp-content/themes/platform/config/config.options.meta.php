<?php

function get_edit_page_post_array(){
	
	return array(
			'_pagelines_layout_mode' => array(
					'type' => 'select',
					'selectvalues'=> array(
						'fullwidth'				=> array( 'name' => 'Fullwidth layout', 'version' => 'pro' ),
						'one-sidebar-right' 	=> array( 'name' => 'One sidebar on right' ),
						'one-sidebar-left'		=> array( 'name' => 'One sidebar on left' ),
						'two-sidebar-right' 	=> array( 'name' => 'Two sidebars on right', 'version' => 'pro' ),
						'two-sidebar-left' 		=> array( 'name' => 'Two sidebars on left', 'version' => 'pro' ),
						'two-sidebar-center' 	=> array( 'name' => 'Two sidebars, one on each side', 'version' => 'pro' ),
					),
					'inputlabel' => 'Select Layout Mode (optional)',
					'exp' => 'Use this option to change the content layout mode on this page.'
				),


		);

}
