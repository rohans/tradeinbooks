<?php 
/*
	
	FOOTER
	
	This file controls the ending HTML </body></html> and common graphical elements in your site footer.
	You can control what shows up where using WordPress and PageLines PHP conditionals
	
	This theme copyright (C) 2008-2010 PageLines
	
*/

// Deal with BuddyPress and BuddyPress Template Pack
if(pagelines_is_buddypress_page()):?>
		</div>
	</div>
<?php endif;

// Load MoreFoot Template
do_action('pagelines_morefoot', 'morefoot'); // Hook 

?>
			<div class="clear"></div>

			</div> <!-- END .outline -->
		</div> <!-- END #page-main from header -->
	</div> <!-- END #page from header -->

	<div  id="footer">
		<div class="outline fix">
			<?php do_action('pagelines_footer', 'footer'); // Hook ?>
			<div class="clear"></div>
			<?php if(pagelines_option('no_credit') || !VDEV): // Thank you for supporting PageLines ?>
				<div id="cred" class="pagelines">
					<a class="plimage" target="_blank" href="<?php if(get_edit_post_link()) echo get_edit_post_link(); else print_pagelines_option('partner_link', 'http://www.pagelines.com/');?>" title="<?php echo THEMENAME;?> by PageLines">
						<img src="<?php echo apply_filters('pagelines_leaf', THEME_IMAGES.'/pagelines.png');?>" alt="<?php echo THEMENAME;?> by PageLines" />
					</a>
				</div>
			<?php endif;?>
			<div class="clear"></div>
		</div>
	</div>
</div>
<?php 
	print_pagelines_option('footerscripts'); // Load footer scripts option 	
	wp_footer(); // Hook (WordPress) 
?>
</body>
</html>