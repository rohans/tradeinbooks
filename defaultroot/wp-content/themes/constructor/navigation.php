<?php
/**
 * You can change navigation in this is file
 * 
 * @package WordPress
 * @subpackage constructor
 */
?>
<div class="navigation">
    <?php if (is_singular()) : // Whether is single post, is a page, or is an attachment ?>
        <div class="alignleft"><?php next_post_link('%link', '<span>&laquo;</span> %title') ?></div>
        <div class="alignright"><?php previous_posts_link('%link', '%title <span>&raquo;</span>') ?></div>
    <?php elseif (function_exists('wp_pagenavi')) : // Plugin pagenavi ?>
        <?php wp_pagenavi(); ?>
    <?php else: // Default page navigation ?>
        <div class="alignleft"><?php next_posts_link(__('<span>&laquo;</span> Older Entries', 'constructor')) ?></div>
        <div class="alignright"><?php previous_posts_link(__('Newer Entries <span>&raquo;</span>', 'constructor')) ?></div>
    <?php endif; ?>
</div>