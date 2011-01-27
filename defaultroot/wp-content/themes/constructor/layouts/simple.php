<?php
/**
 * @package WordPress
 * @subpackage constructor
 */
__('Simple', 'constructor'); // required for correct translation
?>
<div id="content" class="box shadow opacity <?php the_constructor_layout_class() ?>">
    <div id="container" >
    <?php get_constructor_slideshow(true) ?>
    <?php if (have_posts()) : ?>
        <div id="posts">
        <?php while (have_posts()) : the_post();?>
            <div <?php post_class(); ?> id="post-<?php the_ID() ?>">
                <div class="title opacity box">
                    <h2><a href="<?php the_permalink() ?>" rel="bookmark" title="<?php printf(__('Permanent Link to %s', 'constructor'), the_title_attribute('echo=0')); ?>"><?php the_title(); ?></a></h2>
                </div>
                <div class="entry">
                	<?php
                        // without "more" link
                        the_content('');
                    ?>
                </div>
                <div class="footer"></div>
            </div>
        <?php endwhile; ?>
        </div>
        <?php comments_template(); ?>
        <?php get_constructor_navigation(); ?>
    <?php endif; ?>
    </div>
    <?php get_constructor_sidebar(); ?>
</div><!-- id='content' -->
