/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: QueryPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class QueryPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(QueryPage.class.getName());
    public Object currentmonth;

    @Override
    @RequestMapping("/wp-includes/query.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/query";
    }

    /*
     * The Big Query.
     */
    
    public Object get_query_var(String var) {
        return gVars.wp_query.get(var);
    }

    public void set_query_var(Object var, Object value) {
        gVars.wp_query.set(var, value);
    }

    public Object query_posts(String query) {
        gVars.wp_query = null;
        gVars.wp_query = new WP_Query(gVars, gConsts);

        return gVars.wp_query.query(query);
    }

    public void wp_reset_query() {
        gVars.wp_query = null;
        gVars.wp_query = gVars.wp_the_query;

        if (!empty(gVars.wp_query.post)) {
            gVars.post = gVars.wp_query.post;
            setup_postdata(gVars.wp_query.post);
        }
    }

    /**
     * Query type checks.
     */
    public boolean is_admin() {
        if (gConsts.isWP_ADMINDefined()) {
            return gConsts.getWP_ADMIN();
        }

        return false;
    }

    public boolean is_archive() {
        return gVars.wp_query.is_archive;
    }

    public boolean is_attachment() {
        return gVars.wp_query.is_attachment;
    }

    public boolean is_author() {
        return is_author(new Array<Object>());
    }

    public boolean is_author(Array<Object> author) {
        StdClass author_obj = null;

        if (!gVars.wp_query.is_author) {
            return false;
        }

        if (empty(author)) {
            return true;
        }

        author_obj = (StdClass) gVars.wp_query.get_queried_object();

        //		author = new Array<Object>(author);
        if (Array.in_array(StdClass.getValue(author_obj, "ID"), author)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(author_obj, "nickname"), author)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(author_obj, "user_nicename"), author)) {
            return true;
        }

        return false;
    }

    public boolean is_category() {
        return is_category(new Array<Object>());
    }

    public boolean is_category(Array<Object> category) {
        StdClass cat_obj = null;

        if (!gVars.wp_query.is_category) {
            return false;
        }

        if (empty(category)) {
            return true;
        }

        cat_obj = (StdClass) gVars.wp_query.get_queried_object();
        category = new Array<Object>(category);

        if (Array.in_array(StdClass.getValue(cat_obj, "term_id"), category)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(cat_obj, "name"), category)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(cat_obj, "slug"), category)) {
            return true;
        }

        return false;
    }

    public boolean is_tag(Object slug) {
        StdClass tag_obj = null;

        if (!gVars.wp_query.is_tag) {
            return false;
        }

        if (empty(slug)) {
            return true;
        }

        tag_obj = (StdClass) gVars.wp_query.get_queried_object();
        slug = new Array<Object>(slug);

        if (Array.in_array(StdClass.getValue(tag_obj, "slug"), (Array) slug)) {
            return true;
        }

        return false;
    }

    public boolean is_tax(Array<Object> slug) {
        StdClass term = null;

        if (!gVars.wp_query.is_tax) {
            return false;
        }

        if (empty(slug)) {
            return true;
        }

        term = (StdClass) gVars.wp_query.get_queried_object();
        slug = new Array<Object>(slug);

        if (Array.in_array(StdClass.getValue(term, "slug"), slug)) {
            return true;
        }

        return false;
    }

    public boolean is_comments_popup() {
        return gVars.wp_query.is_comments_popup;
    }

    public boolean is_date() {
        return gVars.wp_query.is_date;
    }

    public boolean is_day() {
        return gVars.wp_query.is_day;
    }

    public boolean is_feed() {
        return gVars.wp_query.is_feed;
    }

    /**
     * is_front_page() - Is it the front of the site, whether blog view or a
     * WP Page?
     * @since 2.5
     * @uses is_home
     * @uses get_option
     * @return bool True if front of site
     */
    public boolean is_front_page() {
    	// most likely case
        if (equal("posts", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) && is_home()) {
            return true;
        } else if (equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) &&
                booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front")) && is_page(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * is_home() - Is it the blog view homepage?
     * @since 2.1
     * @global object $wp_query
     * @return bool True if blog view homepage
     */
    public boolean is_home() {
        return gVars.wp_query.is_home;
    }

    public boolean is_month() {
        return gVars.wp_query.is_month;
    }

    public boolean is_page(Object page) {
        StdClass page_obj = null;

        if (!gVars.wp_query.is_page) {
            return false;
        }

        if (empty(page)) {
            return true;
        }

        page_obj = (StdClass) gVars.wp_query.get_queried_object();
        page = new Array<Object>(page);

        if (Array.in_array(StdClass.getValue(page_obj, "ID"), (Array) page)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(page_obj, "post_title"), (Array) page)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(page_obj, "post_name"), (Array) page)) {
            return true;
        }

        return false;
    }

    public boolean is_paged() {
        return gVars.wp_query.is_paged;
    }

    public boolean is_plugin_page() {
        if (isset(gVars.plugin_page)) {
            return true;
        }

        return false;
    }

    public boolean is_preview() {
        return gVars.wp_query.is_preview;
    }

    public boolean is_robots() {
        return gVars.wp_query.is_robots;
    }

    public boolean is_search() {
        return gVars.wp_query.is_search;
    }

    public boolean is_single() {
        return is_single(new Array<Object>());
    }

    public boolean is_single(Array<Object> post) {
        StdClass post_obj = null;

        if (!gVars.wp_query.is_single) {
            return false;
        }

        if (empty(post)) {
            return true;
        }

        post_obj = (StdClass) gVars.wp_query.get_queried_object();

        //		post = new Array<Object>(post);
        if (Array.in_array(StdClass.getValue(post_obj, "ID"), post)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(post_obj, "post_title"), post)) {
            return true;
        } else if (Array.in_array(StdClass.getValue(post_obj, "post_name"), post)) {
            return true;
        }

        return false;
    }

    public boolean is_singular() {
        return gVars.wp_query.is_singular;
    }

    public boolean is_time() {
        return gVars.wp_query.is_time;
    }

    public boolean is_trackback() {
        return gVars.wp_query.is_trackback;
    }

    public boolean is_year() {
        return gVars.wp_query.is_year;
    }

    public boolean is_404() {
        return gVars.wp_query.is_404;
    }

    /**
     * The Loop. Post loop control.
     */
    public boolean have_posts() {
        return gVars.wp_query.have_posts();
    }

    public boolean in_the_loop() {
        return gVars.wp_query.in_the_loop;
    }

    public void rewind_posts() {
        gVars.wp_query.rewind_posts();
    }

    public void the_post() {
        gVars.wp_query.the_post();
    }

    /**
     * Comments loop.
     */
    public boolean have_comments() {
        return gVars.wp_query.have_comments();
    }

    public void the_comment() {
        gVars.wp_query.the_comment();
    }

    /**
     * Redirect old slugs
     */
    public void wp_old_slug_redirect() {
        String query = null;
        int id = 0;
        String link;

        if (is_404() && !equal("", gVars.wp_query.query_vars.getValue("name"))) {
            query = "SELECT post_id FROM " + gVars.wpdb.postmeta + ", " + gVars.wpdb.posts + " WHERE ID = post_id AND meta_key = \'_wp_old_slug\' AND meta_value=\'" +
                strval(gVars.wp_query.query_vars.getValue("name")) + "\'";

            // if year, monthnum, or day have been specified, make our query more precise
    		// just in case there are multiple identical _wp_old_slug values
            if (!equal("", gVars.wp_query.query_vars.getValue("year"))) {
                query = query + " AND YEAR(post_date) = \'" + strval(gVars.wp_query.query_vars.getValue("year")) + "\'";
            }

            if (!equal("", gVars.wp_query.query_vars.getValue("monthnum"))) {
                query = query + " AND MONTH(post_date) = \'" + strval(gVars.wp_query.query_vars.getValue("monthnum")) + "\'";
            }

            if (!equal("", gVars.wp_query.query_vars.getValue("day"))) {
                query = query + " AND DAYOFMONTH(post_date) = \'" + strval(gVars.wp_query.query_vars.getValue("day")) + "\'";
            }

            id = intval(gVars.wpdb.get_var(query));

            if (!booleanval(id)) {
                return;
            }

            link = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(id, false);

            if (!booleanval(link)) {
                return;
            }

            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(link, intval("301")); // Permanent redirect
            System.exit();
        } else {
        }
    }

 //
 // Private helper functions
 //

 // Setup global post data.
    public boolean setup_postdata(StdClass post) {
        String content;
        gVars.id = intval(StdClass.getValue(post, "ID"));
        gVars.authordata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(post, "post_author")));
        gVars.day = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("d.m.y", strval(StdClass.getValue(post, "post_date")), true);
        currentmonth = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("m", strval(StdClass.getValue(post, "post_date")), true);
        gVars.numpages = 1;
        gVars.page = intval(get_query_var("page"));

        if (!booleanval(gVars.page)) {
            gVars.page = 1;
        }

        if (is_single() || is_page("") || is_feed()) {
            gVars.more = 1;
        }

        content = strval(StdClass.getValue(post, "post_content"));

        if (QRegExPerl.preg_match("/<!--nextpage-->/", content)) {
            if (intval(gVars.page) > 1) {
                gVars.more = 1;
            }

            gVars.multipage = 1;
            content = Strings.str_replace("\n<!--nextpage-->\n", "<!--nextpage-->", content);
            content = Strings.str_replace("\n<!--nextpage-->", "<!--nextpage-->", content);
            content = Strings.str_replace("<!--nextpage-->\n", "<!--nextpage-->", content);
            gVars.pages = Strings.explode("<!--nextpage-->", content);
            gVars.numpages = Array.count(gVars.pages);
        } else {
            gVars.pages.putValue(0, StdClass.getValue(post, "post_content"));
            gVars.multipage = 0;
        }

        return true;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
