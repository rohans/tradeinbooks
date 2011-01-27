/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Default_filtersPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
 *
 **********************************************************************************/

/**********************************************************************************
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 **********************************************************************************/

/***************************************************************************
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 ***************************************************************************/
package org.numiton.nwp.wp_includes;

import static com.numiton.VarHandling.strval;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Default_filtersPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Default_filtersPage.class.getName());
    public Array<Object> filters;
    public String filter;

    @Override
    @RequestMapping("/wp-includes/default-filters.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/default_filters";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_default_filters_block1");
        gVars.webEnv = webEnv;
        
        /**
         * Sets up the default filters and actions for most
         * of the WordPress hooks.
         *
         * If you need to remove a default hook, this file will
         * give you the priority for which to use to remove the
         * hook.
         *
         * Not all of the default hooks are found in default-filters.php
         *
         * @package WordPress
         */

        // Strip, trim, kses, special chars for string saves
        
        filters = new Array<Object>(
                new ArrayEntry<Object>("pre_term_name"),
                new ArrayEntry<Object>("pre_comment_author_name"),
                new ArrayEntry<Object>("pre_link_name"),
                new ArrayEntry<Object>("pre_link_target"),
                new ArrayEntry<Object>("pre_link_rel"),
                new ArrayEntry<Object>("pre_user_display_name"),
                new ArrayEntry<Object>("pre_user_first_name"),
                new ArrayEntry<Object>("pre_user_last_name"),
                new ArrayEntry<Object>("pre_user_nickname"));

        for (Map.Entry javaEntry445 : filters.entrySet()) {
            filter = strval(javaEntry445.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "strip_tags"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "trim"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(KsesPage.class, gVars, gConsts), "wp_filter_kses"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 30, 1);
        }

        // Kses only for textarea saves
        filters = new Array<Object>(
                new ArrayEntry<Object>("pre_term_description"),
                new ArrayEntry<Object>("pre_link_description"),
                new ArrayEntry<Object>("pre_link_notes"),
                new ArrayEntry<Object>("pre_user_description"));

        for (Map.Entry javaEntry446 : filters.entrySet()) {
            filter = strval(javaEntry446.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(KsesPage.class, gVars, gConsts), "wp_filter_kses"), 10, 1);
        }

        // Email
        filters = new Array<Object>(new ArrayEntry<Object>("pre_comment_author_email"), new ArrayEntry<Object>("pre_user_email"));

        for (Map.Entry javaEntry447 : filters.entrySet()) {
            filter = strval(javaEntry447.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "trim"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "sanitize_email"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(KsesPage.class, gVars, gConsts), "wp_filter_kses"), 10, 1);
        }

        // Save URL
        filters = new Array<Object>(
                new ArrayEntry<Object>("pre_comment_author_url"),
                new ArrayEntry<Object>("pre_user_url"),
                new ArrayEntry<Object>("pre_link_url"),
                new ArrayEntry<Object>("pre_link_image"),
                new ArrayEntry<Object>("pre_link_rss"));

        for (Map.Entry javaEntry448 : filters.entrySet()) {
            filter = strval(javaEntry448.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "strip_tags"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "trim"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "sanitize_url"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(KsesPage.class, gVars, gConsts), "wp_filter_kses"), 10, 1);
        }

        // Display URL
        filters = new Array<Object>(
                new ArrayEntry<Object>("user_url"),
                new ArrayEntry<Object>("link_url"),
                new ArrayEntry<Object>("link_image"),
                new ArrayEntry<Object>("link_rss"),
                new ArrayEntry<Object>("comment_url"));

        for (Map.Entry javaEntry449 : filters.entrySet()) {
            filter = strval(javaEntry449.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "strip_tags"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(Strings.class, "trim"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "clean_url"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(KsesPage.class, gVars, gConsts), "wp_filter_kses"), 10, 1);
        }

        // Slugs
        filters = new Array<Object>(new ArrayEntry<Object>("pre_term_slug"));

        for (Map.Entry javaEntry450 : filters.entrySet()) {
            filter = strval(javaEntry450.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "sanitize_title"), 10, 1);
        }

        // Keys
        filters = new Array<Object>(new ArrayEntry<Object>("pre_post_type"));

        for (Map.Entry javaEntry451 : filters.entrySet()) {
            filter = strval(javaEntry451.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "sanitize_user"), 10, 1);
        }

        // Places to balance tags on input
        filters = new Array<Object>(
                new ArrayEntry<Object>("content_save_pre"),
                new ArrayEntry<Object>("excerpt_save_pre"),
                new ArrayEntry<Object>("comment_save_pre"),
                new ArrayEntry<Object>("pre_comment_content"));

        for (Map.Entry javaEntry452 : filters.entrySet()) {
            filter = strval(javaEntry452.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "balanceTags"), 50, 1);
        }

        // Format strings for display.
        filters = new Array<Object>(
                new ArrayEntry<Object>("comment_author"),
                new ArrayEntry<Object>("term_name"),
                new ArrayEntry<Object>("link_name"),
                new ArrayEntry<Object>("link_description"),
                new ArrayEntry<Object>("link_notes"),
                new ArrayEntry<Object>("bloginfo"),
                new ArrayEntry<Object>("wp_title"));

        for (Map.Entry javaEntry453 : filters.entrySet()) {
            filter = strval(javaEntry453.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 10, 1);
        }

        // Format text area for display.
        filters = new Array<Object>(new ArrayEntry<Object>("term_description"));

        for (Map.Entry javaEntry454 : filters.entrySet()) {
            filter = strval(javaEntry454.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wpautop"), 10, 1);
        }

        // Format for RSS
        filters = new Array<Object>(new ArrayEntry<Object>("term_name_rss"));

        for (Map.Entry javaEntry455 : filters.entrySet()) {
            filter = strval(javaEntry455.getValue());
            getIncluded(PluginPage.class, gVars, gConsts).add_filter(filter, Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        }

        // Display filters
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title", Callback.createCallbackArray(Strings.class, "trim"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_smilies"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wpautop"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content", Callback.createCallbackArray(getIncluded(Post_templatePage.class, gVars, gConsts), "prepend_attachment"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_excerpt", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_excerpt", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_smilies"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_excerpt", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_excerpt", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wpautop"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("get_the_excerpt", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_trim_excerpt"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "make_clickable"), 9, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "force_balance_tags"), 25, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_smilies"), 20, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wpautop"), 30, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_excerpt", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("list_cats", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("single_post_title", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wptexturize"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("wp_sprintf", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_sprintf_l"), 10, 2);
        
        // RSS filters
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title_rss", Callback.createCallbackArray(Strings.class, "strip_tags"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_excerpt_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "convert_chars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_excerpt_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_author_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("bloginfo_rss", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_author", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "ent2ncr"), 8, 1);
        
        // Misc filters
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_ping_sites", Callback.createCallbackArray(getIncluded(CommentPage.class, gVars, gConsts), "privacy_ping_filter"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_blog_charset", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_home", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "_config_wp_home"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_siteurl", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "_config_wp_siteurl"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("tiny_mce_before_init", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "_mce_set_direction"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("pre_kses", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_pre_kses_less_than"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("sanitize_title", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "sanitize_title_with_dashes"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("check_comment_flood", Callback.createCallbackArray(getIncluded(CommentPage.class, gVars, gConsts), "check_comment_flood_db"), 10, 3);
        getIncluded(PluginPage.class, gVars, gConsts)
            .add_filter("comment_flood_filter", Callback.createCallbackArray(getIncluded(CommentPage.class, gVars, gConsts), "wp_throttle_comment_flood"), 10, 3);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("pre_comment_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_rel_nofollow"), 15, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_email", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "antispambot"), 10, 1);
        
        //Atom SSL support
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("atom_service_url", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "atom_service_url_filter"), 10, 1);
        
        // Actions
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(getIncluded(General_templatePage.class, gVars, gConsts), "rsd_link"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(getIncluded(General_templatePage.class, gVars, gConsts), "wlwmanifest_link"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(getIncluded(ThemePage.class, gVars, gConsts), "locale_stylesheet"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts)
            .add_action("publish_future_post", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "check_and_publish_future_post"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(getIncluded(General_templatePage.class, gVars, gConsts), "noindex"), 1, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(getIncluded(Script_loaderPage.class, gVars, gConsts), "wp_print_scripts"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(getIncluded(General_templatePage.class, gVars, gConsts), "wp_generator"), 10, 1);

        if (!gConsts.isDOING_CRONDefined()) {
            getIncluded(PluginPage.class, gVars, gConsts).add_action("init", Callback.createCallbackArray(getIncluded(CronPage.class, gVars, gConsts), "wp_cron"), 10, 1);
        }

        getIncluded(PluginPage.class, gVars, gConsts).add_action("do_feed_rdf", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "do_feed_rdf"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("do_feed_rss", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "do_feed_rss"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("do_feed_rss2", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "do_feed_rss2"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("do_feed_atom", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "do_feed_atom"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("do_pings", Callback.createCallbackArray(getIncluded(CommentPage.class, gVars, gConsts), "do_all_pings"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("do_robots", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "do_robots"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts)
            .add_action("sanitize_comment_cookies", Callback.createCallbackArray(getIncluded(CommentPage.class, gVars, gConsts), "sanitize_comment_cookies"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_print_scripts", Callback.createCallbackArray(getIncluded(Script_loaderPage.class, gVars, gConsts), "wp_print_scripts"), 20, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("init", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "smilies_init"), 5, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("plugins_loaded", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "wp_maybe_load_widgets"), 0, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("shutdown", Callback.createCallbackArray(getIncluded(FunctionsPage.class, gVars, gConsts), "wp_ob_end_flush_all"), 1, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("publish_post", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "_publish_post_hook"), 5, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("future_post", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "_future_post_hook"), 5, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("future_page", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "_future_post_hook"), 5, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("save_post", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "_save_post_hook"), 5, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("transition_post_status", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "_transition_post_status"), 5, 3);
        getIncluded(PluginPage.class, gVars, gConsts)
            .add_action("comment_form", Callback.createCallbackArray(getIncluded(Comment_templatePage.class, gVars, gConsts), "wp_comment_form_unfiltered_html_nonce"), 10, 1);
        
        // Redirect Old Slugs
        getIncluded(PluginPage.class, gVars, gConsts).add_action("template_redirect", Callback.createCallbackArray(getIncluded(QueryPage.class, gVars, gConsts), "wp_old_slug_redirect"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("edit_post", Callback.createCallbackArray(getIncluded(PostPage.class, gVars, gConsts), "wp_check_for_changed_slugs"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("edit_form_advanced", Callback.createCallbackArray(getIncluded(TemplatePage.class, gVars, gConsts), "wp_remember_old_slug"), 10, 1);

        return DEFAULT_VAL;
    }
}
