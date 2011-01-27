<?php
/**
 * @package WordPress
 * @subpackage constructor
 */
// Stupid Parser This is Theme support the Gravatar Service
// get_avatar();

// load header.php
get_header();

// load one of layout pages (layouts/*.php) based on settings
get_constructor_layout('index');

// load footer.php
get_footer();
?>