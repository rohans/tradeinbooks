<?php
/**
 * @package WordPress
 * @subpackage constructor
 */
__('Single', 'constructor'); // required for correct translation
?>
<div id="content" class="box shadow opacity <?php the_constructor_layout_class() ?>">
    <div id="container" >
    <?php get_constructor_slideshow(true) ?>

    <?php if (have_posts()) : ?>
        <div id="posts">
        <?php while (have_posts()) : the_post(); ?>
            <div <?php post_class(); ?> id="post-<?php the_ID() ?>">
                <div class="title opacity box">
                    <h1><a href="<?php the_permalink() ?>" rel="bookmark" title="<?php printf(__('Permanent Link to %s', 'constructor'), the_title_attribute('echo=0')); ?>"><?php the_title(); ?></a></h1>
                </div>
                <div class="entry">
                    <?php echo get_the_post_thumbnail(NULL, 'tile-post-thumbnail', array('class'=>'aligncenter')) ?>
                    <?php the_content(__('Read the rest of this entry &raquo;', 'constructor')) ?>
				    <?php wp_link_pages(array('before' => '<p class="pages"><strong>'.__('Pages', 'constructor').':</strong> ', 'after' => '</p>', 'next_or_number' => 'number')); ?>
                </div>
                <div class="footer">
                    <div class="links">
                        <?php the_date() ?> |
                        <?php get_constructor_author('', ' |') ?>
                        <?php the_tags(__('Tags', 'constructor') . ': ', ', ', ' |'); ?>
                        <?php edit_post_link(__('Edit', 'constructor'), '', ' | '); ?>
                        <?php comments_popup_link(
                                  __('No Comments &#187;', 'constructor'),
                                  __('1 Comment &#187;', 'constructor'),
                                  __('% Comments &#187;', 'constructor'),
                                  'comments-link',
                                  __('Comments Closed', 'constructor')); ?>
                    </div>
                </div>
            </div>
        <?php endwhile; ?>
        </div>
        <?php comments_template(); ?>
        <?php get_constructor_navigation(); ?>
    <?php endif; ?>
    </div><!-- id='container' -->
    <?php get_constructor_sidebar(); ?>
</div><!-- id='content' -->