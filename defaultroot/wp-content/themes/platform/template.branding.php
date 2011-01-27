<div class="branding_wrap">
<?php if(pagelines_option('pagelines_custom_logo')):?>
	<a href="<?php echo home_url();?>" title="<?php bloginfo('name');?> <?php _e('Home', 'pagelines');?>">
		<?php pagelines_register_hook( 'pagelines_before_header_image', 'branding' );?>
		<img src="<?php echo esc_url(pagelines_option('pagelines_custom_logo'));?>" alt="<?php bloginfo('name');?>" />
	</a>
<?php else:?>
	<h1 class="site-title">

		<a class="home" href="<?php echo esc_url(get_option('home')); ?>/" title="<?php _e('Home','pagelines');?>">
			<?php bloginfo('name');?>
		</a>
	</h1>
	<h6 class="site-description"><?php bloginfo('description');?></h6>
<?php endif;?>
		
	<?php pagelines_register_hook( 'pagelines_before_branding_icons', 'branding' );?>

	<div class="icons" style="bottom: <?php echo intval(pagelines_option('icon_pos_bottom'));?>px; right: <?php echo pagelines_option('icon_pos_right');?>px;">

		<?php if(pagelines('rsslink')):?>
		<a target="_blank" href="<?php echo get_bloginfo('rss2_url');?>" class="rsslink"></a>
		<?php endif;?>
		
		<?php if(VPRO):?>
			<?php pagelines_register_hook( 'pagelines_branding_icons_start', 'branding' );?>
			<?php if(pagelines_option('twitterlink')):?>
			<a target="_blank" href="<?php echo pagelines_option('twitterlink');?>" class="twitterlink"></a>
			<?php endif;?>
			<?php if(pagelines_option('facebooklink')):?>
			<a target="_blank" href="<?php echo pagelines_option('facebooklink');?>" class="facebooklink"></a>
			<?php endif;?>
			<?php if(pagelines_option('linkedinlink')):?>
			<a target="_blank" href="<?php echo pagelines_option('linkedinlink');?>" class="linkedinlink"></a>
			<?php endif;?>
			<?php if(pagelines_option('youtubelink')):?>
			<a target="_blank" href="<?php echo pagelines_option('youtubelink');?>" class="youtubelink"></a>
			<?php endif;?>
			<?php pagelines_register_hook( 'pagelines_branding_icons_end', 'branding' );?>
		<?php endif;?>
		
	</div>
</div>
<?php pagelines_register_hook( 'pagelines_after_branding_wrap', 'branding' );?>
