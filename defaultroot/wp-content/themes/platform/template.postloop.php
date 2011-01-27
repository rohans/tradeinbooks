<?php 
/*
	
	THE LOOP (Posts, Single Post Content, and Page Content)
	
	This file contains the WordPress "loop" which controls the content in your pages & posts. 
	You can control what shows up where using WordPress and PageLines PHP conditionals
	
	This theme copyright (C) 2008-2010 PageLines
	
*/

	global $pagelines_layout; 
	global $post;
	global $wp_query;
	
	$count = 1;  // Used to get the number of the post as we loop through them.
	$clipcount = 2; // The number of clips in a row
	
	$post_count = $wp_query->post_count;  // Used to prevent markup issues when there aren't an even # of posts.
	$paged = intval(get_query_var('paged')); // Control output if on a paginated page

	if(is_admin()) query_posts('showposts=1'); // For parsing in admin, no posts so set it to one.

	$thumb_space = get_option('thumbnail_size_w') + 33; // Space for thumb with padding


// Get post info (if applicable)
pagelines_do_posts_info();

// Start of 'The Loop'	
if(have_posts()){
while (have_posts()) : the_post(); 
 
if(!pagelines_show_clip($count, $paged) || is_admin()):

?><div <?php post_class('fpost') ?> id="post-<?php the_ID(); ?>">
		<?php pagelines_register_hook( 'pagelines_loop_post_start', 'theloop' ); //hook ?>
		
		<?php if(pagelines('pagetitles') && is_page()):?>
			<h1 class="pagetitle"><?php the_title(); ?></h1>
			<?php pagelines_register_hook( 'pagelines_loop_page_title_after', 'theloop' );?>
		<?php endif;?>
		
				<?php if(!is_page()):?>	
						<div class="post-meta fix">	
							<?php if(pagelines_show_thumb( get_the_ID() )): // Thumbnails ?>
				            		<div class="post-thumb" style="margin-right:-<?php echo $thumb_space;?>px">
										<a href="<?php the_permalink(); ?>" rel="bookmark" title="<?php _e('Permanent Link To', 'pagelines');?> <?php the_title_attribute();?>">
											<?php the_post_thumbnail('thumbnail');?>
										</a>
						            </div>
							<?php endif; ?>

							<div class="post-header fix <?php if(!pagelines_show_thumb($post->ID)) echo 'post-nothumb';?>" style="<?php 
							
								if(pagelines_show_thumb($post->ID)){
									echo 'margin-left:'.$thumb_space.'px';
								}
								
								
								?>" >
								<?php pagelines_register_hook( 'pagelines_loop_post_header_start', 'theloop' ); //hook ?>
								<div class="post-title-section fix">

									<div class="post-title fix">
										<h2>
											<a href="<?php the_permalink(); ?>" rel="bookmark" title="<?php _e('Permanent Link to','pagelines');?> <?php the_title_attribute(); ?>"><?php 
											pagelines_register_hook( 'pagelines_loop_posttitle_start', 'theloop' ); //hook 
											the_title(); 
											?></a>
										</h2>
										<?php pagelines_register_hook( 'pagelines_loop_post_title_after', 'theloop' );?>
										<div class="metabar">
											<em>
												<?php pagelines_register_hook( 'pagelines_loop_metabar_start', 'theloop' ); //hook ?>

												<?php if(pagelines('byline_author')):?>
												<span class="sword"><?php _e('by','pagelines');?></span> <span class="post-author vcard"><a href="<?php echo get_author_posts_url(get_the_author_meta('ID')); ?>"><?php echo get_the_author(); ?></a></span>

												<?php endif;?>
												
												<?php if(pagelines('byline_date')):?>
													<span class="sword"><?php _e('on','pagelines');?></span> <abbr title="<?php echo get_the_time('Y-m-d');?>" class="published"><?php the_time(get_option('date_format')); ?></abbr>
												<?php endif;?>
												
												<?php if(pagelines('byline_comments')):?>
												<?php echo '&middot;';?>
												<a rel="nofollow" class="comments-num" href="<?php the_permalink(); ?>#comments" title="<?php _e('View Comments', 'pagelines'); ?>"><?php comments_number(); ?></a>
												<?php endif;?>
												
												<?php if(pagelines('byline_categories')):?>
													<?php echo '&middot;';?>
													<span class="sword"><?php _e('in','pagelines');?></span>&nbsp;<?php the_category(', ') ?>
												<?php endif;?>
												
												<?php edit_post_link(__('<span class="editpage">Edit</span>', 'pagelines'), '[', ']');?>
												<?php pagelines_register_hook( 'pagelines_loop_metabar_end', 'theloop' ); //hook ?>
											</em>
										</div>
									</div>


								</div>
								<?php if(pagelines_show_excerpt( get_the_ID() )): // Post Excerpt ?>
										<div class="post-excerpt">
											<?php 
												pagelines_register_hook( 'pagelines_loop_before_post_excerpt', 'theloop' ); //hook 
												the_excerpt(); 
												pagelines_register_hook( 'pagelines_loop_after_post_excerpt', 'theloop' ); //hook 
											?>
										</div>

										<?php if(pagelines_is_posts_page() && !pagelines_show_content( get_the_ID() )): // 'Continue Reading' link ?>
											<a class="continue_reading_link" href="<?php the_permalink(); ?>" title="<?php _e("View", 'pagelines');?> <?php the_title_attribute(); ?>">
												<?php e_pagelines('continue_reading_text', __('Continue Reading', 'pagelines'));?> <span class="right_arrow"><?php _e('&rarr;', 'pagelines');?></span>
											</a>

										<?php endif;?>
								<?php endif; ?>
							</div>				
						</div>
					<?php endif;?>

					<?php  if(pagelines_show_content( get_the_ID() )): // Post and Page Content ?>  	
						<div class="entry_wrap fix">
						<?php pagelines_register_hook( 'pagelines_loop_before_post_content', 'theloop' ); //hook ?>

							<div class="entry_content">
								<?php 
									the_content(__('<p>Continue reading &raquo;</p>','pagelines'));?>
									<div class="clear"></div> 
									<?php if(is_single()) wp_link_pages(__('<p><strong>Pages:</strong>', 'pagelines'), '</p>', __('number', 'pagelines')); 
									$edit_type = (is_page()) ? __('Edit Page','pagelines') : __('Edit Post','pagelines');
									edit_post_link( '['.$edit_type.']', '', ''); 
									pagelines_register_hook( 'pagelines_loop_after_post_content', 'theloop' ); //hook 
								?>
							</div>	
							<div class="tags">
								<?php the_tags(__('Tagged with: ', 'pagelines'),' &bull; ','<br />'); ?>&nbsp;
							</div>
						</div>
					<?php endif;?>
		<?php pagelines_register_hook( 'pagelines_loop_post_end', 'theloop' ); //hook ?>
	</div>

<?php 
endif; // End of Full-Width Post Area 


if(pagelines_show_clip($count, $paged) || is_admin()): // Start Clips 

	if($clipcount % 2 == 0):?>
		<div class="clip_box fix">
		<?php pagelines_register_hook( 'pagelines_loop_clipbox_start', 'theloop' ); //hook ?>
		<?php $clips_in_row = 1;?>
	<?php endif;?>
		<?php $clip_class = (($clipcount+1) % 2 == 0) ? $clip_class = 'clip clip-right' : $clip_class = 'clip';?>
			<div <?php post_class($clip_class) ?> id="post-<?php the_ID(); ?>">
				<?php pagelines_register_hook( 'pagelines_loop_clip_start', 'theloop' ); //hook ?>
					<div class="clip-meta fix">
						<?php if(pagelines_show_thumb( get_the_ID(), 'clip' )): // Thumbnails ?>
			            		<div class="clip-thumb">
									<a href="<?php the_permalink(); ?>" rel="bookmark" title="<?php _e('Link To', 'pagelines');?> <?php the_title_attribute();?>">
										<?php the_post_thumbnail(array( 40, 40 ));?>
									</a>
					            </div>
						<?php endif; ?>
						<div class="clip-header">
							<h4 class="post-title">	
								<a href="<?php the_permalink(); ?>" rel="bookmark" title="<?php _e('Link to','pagelines');?> <?php the_title_attribute(); ?>"><?php 
									the_title(); 
								?></a>
							</h4>
							<div class="metabar">
								<em> 
								<?php the_time(get_option('date_format')); ?>
									<span class="sword"><?php _e('by','pagelines');?></span> <span class="post-author vcard"><a href="<?php echo get_author_posts_url(get_the_author_meta('ID')); ?>"><?php echo get_the_author(); ?></a></span>
									<?php edit_post_link(__('<span class="editpage">Edit</span>', 'pagelines'), '[', ']');?>
									
								<?php pagelines_register_hook( 'pagelines_loop_clipmeta_end', 'theloop' ); //hook?>
								</em>
							</div>
						</div>
					</div>
					<?php if(pagelines_show_excerpt( get_the_ID() )): // Excerpt ?>
					<div class="post-excerpt">
						<?php the_excerpt(); ?>
						<a class="continue_reading_link" href="<?php the_permalink(); ?>" title="<?php _e("View", 'pagelines');?> <?php the_title_attribute(); ?>">
							<?php e_pagelines('continue_reading_text', __('Continue Reading', 'pagelines'));?>  <span class="right_arrow"><?php _e('&rarr;', 'pagelines');?></span>
						</a>
						<?php pagelines_register_hook( 'pagelines_loop_clip_excerpt_end', 'theloop' ); //hook ?>
					</div>
					<?php endif;?>
					<?php pagelines_register_hook( 'pagelines_loop_clip_end', 'theloop' ); //hook ?>
			</div>	
	<?php if(($clipcount+1) % 2 == 0 || $count == $post_count ):?>
		<?php pagelines_register_hook( 'pagelines_loop_clipbox_end', 'theloop' ); //hook ?>
		</div>  <!-- closes .clip_box -->
	<?php endif; $clipcount++;
	
endif; // End of Clips
 
$count++;  // Increment the post counter for formatting purposes.

endwhile; // End of 'The Loop'

// or if no posts... 
} else { ?>
	
	<div class="billboard">
		<?php if(is_search()):?>
			<h2 class="center"><?php _e('No results for ');?>"<?php the_search_query();?>"</h2>
			
			<p class="subhead center"><?php _e('Try another search?', 'pagelines');?></p>
		<?php else:?>
			<h2 class="center"><?php _e('Nothing Found','pagelines');?></h2>
			
			<p class="subhead center"><?php _e('Sorry, what you are looking for isn\'t here.', 'pagelines');?></p>
		<?php endif;?>
		<div class="center fix"><?php get_search_form(); ?> </div>
	</div>
<?php }