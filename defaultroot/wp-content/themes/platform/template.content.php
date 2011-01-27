<?php global $pagelines_layout;?>
<div id="pagelines_content" class="<?php echo $pagelines_layout->layout_mode;?> fix">
	
	<?php pagelines_register_hook( 'pagelines_content_before_columns', 'maincontent' );?>
	<div id="column-wrap" class="fix">
		
		<?php pagelines_register_hook( 'pagelines_content_before_maincolumn', 'maincontent' );?>
		<div id="column-main" class="mcolumn fix">
			<div class="mcolumn-pad" >
				<?php do_action('pagelines_main', 'main'); ?>
			</div>
		</div>
		
		<?php if($pagelines_layout->layout_mode == 'two-sidebar-center'):?>
			<?php pagelines_register_hook( 'pagelines_content_before_sidebar1', 'maincontent' );?>
			<div id="sidebar1" class="scolumn fix">
				<div class="scolumn-pad">
					<?php do_action('pagelines_sidebar1', 'sidebar1'); ?>
				</div>
			</div>
			<?php pagelines_register_hook( 'pagelines_content_after_sidebar1', 'maincontent' );?>
		<?php endif;?>
		
	</div>	
	
	<?php get_sidebar(); ?>

</div>