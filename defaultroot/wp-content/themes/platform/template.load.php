<?php 
/*
	
	MAIN TEMPLATE FILE LOADER
	
	This file is controlled throught the template class; and template setup in the admin.
	HTML can be modified through filed prefixed by 'template' in the root and sections which can be found in the 'sections' folder.
	
	This theme copyright (C) 2008-2010 PageLines
	
*/

get_header();

do_action('pagelines_template', 'templates');
		
get_footer();