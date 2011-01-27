/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Query.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.Math;
import com.numiton.URL;
import com.numiton.VarHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


/**
 * WP_Query
 */
public class WP_Query implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Query.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Object query;
    public Array<Object> query_vars;
    public /* Array or null */ StdClass queried_object;
    public Integer queried_object_id;
    public String request;
    public Array<StdClass> posts;
    public /* Array or null */ int post_count = 0;
    public int current_post = -1;
    public boolean in_the_loop = false;
    public StdClass post;
    public Array<StdClass> comments;
    public int comment_count = 0;
    public int current_comment = -1;
    public StdClass comment;
    public int found_posts = 0;
    public int max_num_pages = 0;
    public boolean is_single = false;
    public boolean is_preview = false;
    public boolean is_page = false;
    public boolean is_archive = false;
    public boolean is_date = false;
    public boolean is_year = false;
    public boolean is_month = false;
    public boolean is_day = false;
    public boolean is_time = false;
    public boolean is_author = false;
    public boolean is_category = false;
    public boolean is_tag = false;
    public boolean is_tax = false;
    public boolean is_search = false;
    public boolean is_feed = false;
    public boolean is_comment_feed = false;
    public boolean is_trackback = false;
    public boolean is_home = false;
    public boolean is_404 = false;
    public boolean is_comments_popup = false;
    public boolean is_admin = false;
    public boolean is_attachment = false;
    public boolean is_singular = false;
    public boolean is_robots = false;
    public boolean is_posts_page = false;
    public boolean is_paged;

    public WP_Query(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        this(javaGlobalVariables, javaGlobalConstants, null);
    }

    public WP_Query(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object query) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        if (!empty(query)) {
            this.query(query);
        }
    }

    public void init_query_flags() {
        this.is_single = false;
        this.is_page = false;
        this.is_archive = false;
        this.is_date = false;
        this.is_year = false;
        this.is_month = false;
        this.is_day = false;
        this.is_time = false;
        this.is_author = false;
        this.is_category = false;
        this.is_tag = false;
        this.is_tax = false;
        this.is_search = false;
        this.is_feed = false;
        this.is_comment_feed = false;
        this.is_trackback = false;
        this.is_home = false;
        this.is_404 = false;
        this.is_paged = false;
        this.is_admin = false;
        this.is_attachment = false;
        this.is_singular = false;
        this.is_robots = false;
        this.is_posts_page = false;
    }

    public void init() {
        this.posts = null;
        this.query = null;
        this.query_vars = new Array<Object>();
        this.queried_object = null;
        this.queried_object_id = null;
        this.post_count = 0;
        this.current_post = -1;
        this.in_the_loop = false;
        this.init_query_flags();
    }

    /**
     * Reparse the query vars.
     */
    public void parse_query_vars() {
        this.parse_query("");
    }

    public Array<Object> fill_query_vars(Array<Object> array) {
        Array<Object> keys = new Array<Object>();
        Object key = null;
        Array<Object> array_keys = new Array<Object>();
        keys = new Array<Object>(
                new ArrayEntry<Object>("error"),
                new ArrayEntry<Object>("m"),
                new ArrayEntry<Object>("p"),
                new ArrayEntry<Object>("subpost"),
                new ArrayEntry<Object>("subpost_id"),
                new ArrayEntry<Object>("attachment"),
                new ArrayEntry<Object>("attachment_id"),
                new ArrayEntry<Object>("name"),
                new ArrayEntry<Object>("hour"),
                new ArrayEntry<Object>("static"),
                new ArrayEntry<Object>("pagename"),
                new ArrayEntry<Object>("page_id"),
                new ArrayEntry<Object>("second"),
                new ArrayEntry<Object>("minute"),
                new ArrayEntry<Object>("hour"),
                new ArrayEntry<Object>("day"),
                new ArrayEntry<Object>("monthnum"),
                new ArrayEntry<Object>("year"),
                new ArrayEntry<Object>("w"),
                new ArrayEntry<Object>("category_name"),
                new ArrayEntry<Object>("tag"),
                new ArrayEntry<Object>("tag_id"),
                new ArrayEntry<Object>("author_name"),
                new ArrayEntry<Object>("feed"),
                new ArrayEntry<Object>("tb"),
                new ArrayEntry<Object>("paged"),
                new ArrayEntry<Object>("comments_popup"),
                new ArrayEntry<Object>("preview"));

        for (Map.Entry javaEntry559 : keys.entrySet()) {
            key = javaEntry559.getValue();

            if (!isset(array.getValue(key))) {
                array.putValue(key, "");
            }
        }

        array_keys = new Array<Object>(
                new ArrayEntry<Object>("category__in"),
                new ArrayEntry<Object>("category__not_in"),
                new ArrayEntry<Object>("category__and"),
                new ArrayEntry<Object>("tag__in"),
                new ArrayEntry<Object>("tag__not_in"),
                new ArrayEntry<Object>("tag__and"),
                new ArrayEntry<Object>("tag_slug__in"),
                new ArrayEntry<Object>("tag_slug__and"));

        for (Map.Entry javaEntry560 : array_keys.entrySet()) {
            key = javaEntry560.getValue();

            if (!isset(array.getValue(key))) {
                array.putValue(key, new Array<Object>());
            }
        }

        return array;
    }

    /**
     * Parse a query string and set query type booleans.
     */
    public void parse_query(Object query)/* Do not change type */
     {
        Array<Object> qv = new Array<Object>();
        StdClass t = null;

        // Modified by Numiton
        if (!empty(query) || !isset(this.query)) {
            this.init();

            if (is_array(query)) {
                this.query_vars = (Array<Object>) query;
                this.query = Array.arrayCopy((Array) query);
            } else {
                Strings.parse_str(strval(query), this.query_vars);
                this.query = query;
            }
        }

        this.query_vars = this.fill_query_vars(this.query_vars);
        qv = this.query_vars;

        if (!empty(qv.getValue("robots"))) {
            this.is_robots = true;
        }

        qv.putValue("p", intval(qv.getValue("p")));
        qv.putValue("page_id", intval(qv.getValue("page_id")));
        qv.putValue("year", intval(qv.getValue("year")));
        qv.putValue("monthnum", intval(qv.getValue("monthnum")));
        qv.putValue("day", intval(qv.getValue("day")));
        qv.putValue("w", intval(qv.getValue("w")));
        qv.putValue("m", strval(qv.getValue("m")));
        qv.putValue("cat", QRegExPerl.preg_replace("|[^0-9,-]|", "", strval(qv.getValue("cat")))); // comma separated list of positive or negative integers

        if (!equal("", qv.getValue("hour"))) {
            qv.putValue("hour", intval(qv.getValue("hour")));
        }

        if (!equal("", qv.getValue("minute"))) {
            qv.putValue("minute", intval(qv.getValue("minute")));
        }

        if (!equal("", qv.getValue("second"))) {
            qv.putValue("second", intval(qv.getValue("second")));
        }

        // Compat.  Map subpost to attachment.
        if (!equal("", qv.getValue("subpost"))) {
            qv.putValue("attachment", qv.getValue("subpost"));
        }

        if (!equal("", qv.getValue("subpost_id"))) {
            qv.putValue("attachment_id", qv.getValue("subpost_id"));
        }

        qv.putValue("attachment_id", intval(qv.getValue("attachment_id")));

        if (!equal("", qv.getValue("attachment")) || !empty(qv.getValue("attachment_id"))) {
            this.is_single = true;
            this.is_attachment = true;
        } else if (!equal("", qv.getValue("name"))) {
            this.is_single = true;
        } else if (booleanval(qv.getValue("p"))) {
            this.is_single = true;
        } else if (!equal("", qv.getValue("hour")) && !equal("", qv.getValue("minute")) && !equal("", qv.getValue("second")) && !equal("", qv.getValue("year")) && !equal("", qv.getValue("monthnum")) &&
                !equal("", qv.getValue("day"))) {
            // If year, month, day, hour, minute, and second are set, a single
            // post is being queried.
            this.is_single = true;
        } else if (!equal("", qv.getValue("static")) || !equal("", qv.getValue("pagename")) || !empty(qv.getValue("page_id"))) {
            this.is_page = true;
            this.is_single = false;
        } else if (!empty(qv.getValue("s"))) {
            this.is_search = true;
        } else {
            // Look for archive queries.  Dates, categories, authors.
            if (!equal("", qv.getValue("second"))) {
                this.is_time = true;
                this.is_date = true;
            }

            if (!equal("", qv.getValue("minute"))) {
                this.is_time = true;
                this.is_date = true;
            }

            if (!equal("", qv.getValue("hour"))) {
                this.is_time = true;
                this.is_date = true;
            }

            if (booleanval(qv.getValue("day"))) {
                if (!this.is_date) {
                    this.is_day = true;
                    this.is_date = true;
                }
            }

            if (booleanval(qv.getValue("monthnum"))) {
                if (!this.is_date) {
                    this.is_month = true;
                    this.is_date = true;
                }
            }

            if (booleanval(qv.getValue("year"))) {
                if (!this.is_date) {
                    this.is_year = true;
                    this.is_date = true;
                }
            }

            if (booleanval(qv.getValue("m"))) {
                this.is_date = true;

                if (Strings.strlen(strval(qv.getValue("m"))) > 9) {
                    this.is_time = true;
                } else if (Strings.strlen(strval(qv.getValue("m"))) > 7) {
                    this.is_day = true;
                } else if (Strings.strlen(strval(qv.getValue("m"))) > 5) {
                    this.is_month = true;
                } else {
                    this.is_year = true;
                }
            }

            if (!equal("", qv.getValue("w"))) {
                this.is_date = true;
            }

            if (empty(qv.getValue("cat")) || equal(qv.getValue("cat"), "0")) {
                this.is_category = false;
            } else {
                if (!strictEqual(Strings.strpos(strval(qv.getValue("cat")), "-"), BOOLEAN_FALSE)) {
                    this.is_category = false;
                } else {
                    this.is_category = true;
                }
            }

            if (!equal("", qv.getValue("category_name"))) {
                this.is_category = true;
            }

            if (!is_array(qv.getValue("category__in")) || empty(qv.getValue("category__in"))) {
                qv.putValue("category__in", new Array<Object>());
            } else {
                qv.putValue("category__in", Array.array_map(new Callback("intval", VarHandling.class), qv.getArrayValue("category__in")));
                this.is_category = true;
            }

            if (!is_array(qv.getValue("category__not_in")) || empty(qv.getValue("category__not_in"))) {
                qv.putValue("category__not_in", new Array<Object>());
            } else {
                qv.putValue("category__not_in", Array.array_map(new Callback("intval", VarHandling.class), qv.getArrayValue("category__not_in")));
            }

            if (!is_array(qv.getValue("category__and")) || empty(qv.getValue("category__and"))) {
                qv.putValue("category__and", new Array<Object>());
            } else {
                qv.putValue("category__and", Array.array_map(new Callback("intval", VarHandling.class), qv.getArrayValue("category__and")));
                this.is_category = true;
            }

            if (!equal("", qv.getValue("tag"))) {
                this.is_tag = true;
            }

            qv.putValue("tag_id", intval(qv.getValue("tag_id")));

            if (!empty(qv.getValue("tag_id"))) {
                this.is_tag = true;
            }

            if (!is_array(qv.getValue("tag__in")) || empty(qv.getValue("tag__in"))) {
                qv.putValue("tag__in", new Array<Object>());
            } else {
                qv.putValue("tag__in", Array.array_map(new Callback("intval", VarHandling.class), qv.getArrayValue("tag__in")));
                this.is_tag = true;
            }

            if (!is_array(qv.getValue("tag__not_in")) || empty(qv.getValue("tag__not_in"))) {
                qv.putValue("tag__not_in", new Array<Object>());
            } else {
                qv.putValue("tag__not_in", Array.array_map(new Callback("intval", VarHandling.class), qv.getArrayValue("tag__not_in")));
            }

            if (!is_array(qv.getValue("tag__and")) || empty(qv.getValue("tag__and"))) {
                qv.putValue("tag__and", new Array<Object>());
            } else {
                qv.putValue("tag__and", Array.array_map(new Callback("intval", VarHandling.class), qv.getArrayValue("tag__and")));
                this.is_category = true;
            }

            if (!is_array(qv.getValue("tag_slug__in")) || empty(qv.getValue("tag_slug__in"))) {
                qv.putValue("tag_slug__in", new Array<Object>());
            } else {
                qv.putValue("tag_slug__in", Array.array_map(new Callback("sanitize_title", getIncluded(FormattingPage.class, gVars, gConsts)), qv.getArrayValue("tag_slug__in")));
                this.is_tag = true;
            }

            if (!is_array(qv.getValue("tag_slug__and")) || empty(qv.getValue("tag_slug__and"))) {
                qv.putValue("tag_slug__and", new Array<Object>());
            } else {
                qv.putValue("tag_slug__and", Array.array_map(new Callback("sanitize_title", getIncluded(FormattingPage.class, gVars, gConsts)), qv.getArrayValue("tag_slug__and")));
                this.is_tag = true;
            }

            if (empty(qv.getValue("taxonomy")) || empty(qv.getValue("term"))) {
                this.is_tax = false;

                for (Map.Entry javaEntry561 : gVars.wp_taxonomies.entrySet()) {
                    t = (StdClass) javaEntry561.getValue();

                    if (isset(StdClass.getValue(t, "query_var")) && !equal("", qv.getValue(StdClass.getValue(t, "query_var")))) {
                        this.is_tax = true;

                        break;
                    }
                }
            } else {
                this.is_tax = true;
            }

            if (empty(qv.getValue("author")) || equal(qv.getValue("author"), "0")) {
                this.is_author = false;
            } else {
                this.is_author = true;
            }

            if (!equal("", qv.getValue("author_name"))) {
                this.is_author = true;
            }

            if (this.is_date || this.is_author || this.is_category || this.is_tag) {
                this.is_archive = true;
            }
        }

        if (!equal("", qv.getValue("feed"))) {
            this.is_feed = true;
        }

        if (!equal("", qv.getValue("tb"))) {
            this.is_trackback = true;
        }

        if (!equal("", qv.getValue("paged"))) {
            this.is_paged = true;
        }

        if (!equal("", qv.getValue("comments_popup"))) {
            this.is_comments_popup = true;
        }

        // if we're previewing inside the write screen
        if (!equal("", qv.getValue("preview"))) {
            this.is_preview = true;
        }

        if (getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
            this.is_admin = true;
        }

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(qv.getValue("feed")), "comments-"))) {
            qv.putValue("feed", Strings.str_replace("comments-", "", strval(qv.getValue("feed"))));
            qv.putValue("withcomments", 1);
        }

        this.is_singular = this.is_single || this.is_page || this.is_attachment;

        if (this.is_feed && (!empty(qv.getValue("withcomments")) || (empty(qv.getValue("withoutcomments")) && this.is_singular))) {
            this.is_comment_feed = true;
        }

        if (!(this.is_singular || this.is_archive || this.is_search || this.is_feed || this.is_trackback || this.is_404 || this.is_admin || this.is_comments_popup)) {
            this.is_home = true;
        }

		// Correct is_* for page_on_front and page_for_posts
        if (this.is_home && (empty(this.query) || equal(qv.getValue("preview"), "true")) && equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) &&
                booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"))) {
            this.is_page = true;
            this.is_home = false;
            qv.putValue("page_id", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"));
        }

        if (!equal("", qv.getValue("pagename"))) {
            this.queried_object = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page_by_path(strval(qv.getValue("pagename")), gConsts.getOBJECT());

            if (!empty(this.queried_object)) {
                this.queried_object_id = intval(StdClass.getValue(this.queried_object, "ID"));
            } else {
                this.queried_object = null;
            }

            if (equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) && isset(this.queried_object_id) &&
                    equal(this.queried_object_id, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts"))) {
                this.is_page = false;
                this.is_home = true;
                this.is_posts_page = true;
            }
        }

        if (booleanval(qv.getValue("page_id"))) {
            if (equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) &&
                    equal(qv.getValue("page_id"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts"))) {
                this.is_page = false;
                this.is_home = true;
                this.is_posts_page = true;
            }
        }

        if (!empty(qv.getValue("post_type"))) {
            qv.putValue("post_type", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(qv.getValue("post_type")), true));
        }

        if (!empty(qv.getValue("post_status"))) {
            qv.putValue("post_status", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(qv.getValue("post_status")), true));
        }

        if (this.is_posts_page && !booleanval(qv.getValue("withcomments"))) {
            this.is_comment_feed = false;
        }

        this.is_singular = this.is_single || this.is_page || this.is_attachment;
		// Done correcting is_* for page_on_front and page_for_posts

        if (equal("404", qv.getValue("error"))) {
            this.set_404();
        }

        if (!empty(query)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("parse_query", new Array<Object>(new ArrayEntry<Object>(this)));
        }
    }

    public void set_404() {
        Object is_feed = null;
        is_feed = this.is_feed;
        this.init_query_flags();
        this.is_404 = true;
        this.is_feed = booleanval(is_feed);
    }

    public String get(Object query_var) {
        if ( /*Added by Numiton*/
            isset(this.query_vars) && isset(this.query_vars.getValue(query_var))) {
            return strval(this.query_vars.getValue(query_var));
        }

        return "";
    }

    public void set(Object query_var, Object value) {
        this.query_vars.putValue(query_var, value);
    }

    public String createFunction_trim(Object a) {
        return Strings.trim(strval(a), "\"'\n\r ");
    }

    public Object get_posts() {
        Array<Object> q = new Array<Object>();
        String distinct = null;
        String whichcat = null;
        String whichauthor = null;
        String whichmimetype = null;
        Object where = null;
        Object limits = null;
        String join = null;
        String search = null;
        String groupby = null;
        boolean post_status_join = false;
        String post_type = null;
        Integer reqpage = null;
        String page_paths = null;
        StdClass reqpage_obj = null;
        String attach_paths = null;
        Array matches = new Array();
        String n = null;
        String searchand = null;
        String term = null;
        Array<Object> cat_array = new Array<Object>();
        Array<String> req_cats = new Array<String>();
        int cat = 0;
        boolean in = false;
        String include_cats = null;
        Object ids;

        /* Do not change type */
        String out_posts = null;
        int reqcat;
        String cat_paths = null;
        String cat_path = null;
        String pathdir = null;
        String in_cats = null;
        Array<Object> tags = new Array<Object>();
        Object tag = null;
        String include_tags = null;
        Array<Object> reqtag = new Array<Object>();
        Array<Object> intersections = new Array<Object>();
        Object item = null;
        String taxonomy_field = null;
        String tsql = null;
        Object taxonomy = null;
        Array post_ids = null;
        Array<Object> tt = new Array<Object>();
        Object terms = null;
        StdClass t = null;
        Array<Object> term_ids = new Array<Object>();
        String eq = null;
        String andor = null;
        Array<Object> author_array = new Array<Object>();
        int i = 0;
        Array<Object> allowed_keys = new Array<Object>();
        Array<String> orderby_array = new Array<String>();
        String orderby = null;
        Array<Object> statuswheres = new Array<Object>();
        Array<String> q_status = new Array<String>();
        Array<String> r_status = new Array<String>();
        Array<String> p_status = new Array<String>();
        Object index = null;
        String statuswhere = null;
        Integer page = null;
        String pgstrt = null;
        String cjoin = null;
        Object cwhere = null;
        String cgroupby = null;
        StdClass comment = null;
        Object fields = null;
        String found_rows = null;
        Object request = null;
        String comments_request = null;
        String found_posts_query = null;
        Object status = null;
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("pre_get_posts", new Array<Object>(new ArrayEntry<Object>(this)));
        
		// Shorthand.
        q = (Array<Object>) this.query_vars;
        
        q = this.fill_query_vars(q);
        
        // First let's clear some variables
        distinct = "";
        whichcat = "";
        whichauthor = "";
        whichmimetype = "";
        where = "";
        limits = "";
        join = "";
        search = "";
        groupby = "";
        post_status_join = false;

        if (!isset(q.getValue("post_type"))) {
            if (this.is_search) {
                q.putValue("post_type", "any");
            } else {
                q.putValue("post_type", "post");
            }
        }

        post_type = strval(q.getValue("post_type"));

        if (!isset(q.getValue("posts_per_page")) || equal(q.getValue("posts_per_page"), 0)) {
            q.putValue("posts_per_page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("posts_per_page"));
        }

        if (isset(q.getValue("showposts")) && booleanval(q.getValue("showposts"))) {
            q.putValue("showposts", intval(q.getValue("showposts")));
            q.putValue("posts_per_page", q.getValue("showposts"));
        }

        if (isset(q.getValue("posts_per_archive_page")) && !equal(q.getValue("posts_per_archive_page"), 0) && (this.is_archive || this.is_search)) {
            q.putValue("posts_per_page", q.getValue("posts_per_archive_page"));
        }

        if (!isset(q.getValue("nopaging"))) {
            if (equal(q.getValue("posts_per_page"), -1)) {
                q.putValue("nopaging", true);
            } else {
                q.putValue("nopaging", false);
            }
        }

        if (this.is_feed) {
            q.putValue("posts_per_page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("posts_per_rss"));
            q.putValue("nopaging", false);
        }

        q.putValue("posts_per_page", intval(q.getValue("posts_per_page")));

        if (intval(q.getValue("posts_per_page")) < -1) {
            q.putValue("posts_per_page", Math.abs(intval(q.getValue("posts_per_page"))));
        } else if (equal(q.getValue("posts_per_page"), 0)) {
            q.putValue("posts_per_page", 1);
        }

        if (this.is_home && (empty(this.query) || equal(q.getValue("preview"), "true")) && equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) &&
                booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"))) {
            this.is_page = true;
            this.is_home = false;
            q.putValue("page_id", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"));
        }

        if (isset(q.getValue("page"))) {
            q.putValue("page", Strings.trim(strval(q.getValue("page")), "/"));
            q.putValue("page", intval(q.getValue("page")));
            q.putValue("page", Math.abs(intval(q.getValue("page"))));
        }

		// If a month is specified in the querystring, load that month
        if (booleanval(q.getValue("m"))) {
            q.putValue("m", "" + QRegExPerl.preg_replace("|[^0-9]|", "", strval(q.getValue("m"))));
            where = strval(where) + " AND YEAR(" + gVars.wpdb.posts + ".post_date)=" + Strings.substr(strval(q.getValue("m")), 0, 4);

            if (Strings.strlen(strval(q.getValue("m"))) > 5) {
                where = strval(where) + " AND MONTH(" + gVars.wpdb.posts + ".post_date)=" + Strings.substr(strval(q.getValue("m")), 4, 2);
            }

            if (Strings.strlen(strval(q.getValue("m"))) > 7) {
                where = strval(where) + " AND DAYOFMONTH(" + gVars.wpdb.posts + ".post_date)=" + Strings.substr(strval(q.getValue("m")), 6, 2);
            }

            if (Strings.strlen(strval(q.getValue("m"))) > 9) {
                where = strval(where) + " AND HOUR(" + gVars.wpdb.posts + ".post_date)=" + Strings.substr(strval(q.getValue("m")), 8, 2);
            }

            if (Strings.strlen(strval(q.getValue("m"))) > 11) {
                where = strval(where) + " AND MINUTE(" + gVars.wpdb.posts + ".post_date)=" + Strings.substr(strval(q.getValue("m")), 10, 2);
            }

            if (Strings.strlen(strval(q.getValue("m"))) > 13) {
                where = strval(where) + " AND SECOND(" + gVars.wpdb.posts + ".post_date)=" + Strings.substr(strval(q.getValue("m")), 12, 2);
            }
        }

        if (!equal("", q.getValue("hour"))) {
            where = strval(where) + " AND HOUR(" + gVars.wpdb.posts + ".post_date)=\'" + strval(q.getValue("hour")) + "\'";
        }

        if (!equal("", q.getValue("minute"))) {
            where = strval(where) + " AND MINUTE(" + gVars.wpdb.posts + ".post_date)=\'" + strval(q.getValue("minute")) + "\'";
        }

        if (!equal("", q.getValue("second"))) {
            where = strval(where) + " AND SECOND(" + gVars.wpdb.posts + ".post_date)=\'" + strval(q.getValue("second")) + "\'";
        }

        if (booleanval(q.getValue("year"))) {
            where = strval(where) + " AND YEAR(" + gVars.wpdb.posts + ".post_date)=\'" + strval(q.getValue("year")) + "\'";
        }

        if (booleanval(q.getValue("monthnum"))) {
            where = strval(where) + " AND MONTH(" + gVars.wpdb.posts + ".post_date)=\'" + strval(q.getValue("monthnum")) + "\'";
        }

        if (booleanval(q.getValue("day"))) {
            where = strval(where) + " AND DAYOFMONTH(" + gVars.wpdb.posts + ".post_date)=\'" + strval(q.getValue("day")) + "\'";
        }

        if (!equal("", q.getValue("name"))) {
            q.putValue("name", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(q.getValue("name")), ""));
            where = strval(where) + " AND " + gVars.wpdb.posts + ".post_name = \'" + strval(q.getValue("name")) + "\'";
        } else if (!equal("", q.getValue("pagename"))) {
            if (isset(this.queried_object_id)) {
                reqpage = this.queried_object_id;
            } else {
                StdClass reqpageObj = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page_by_path(strval(q.getValue("pagename")), gConsts.getOBJECT());

                if (!empty(reqpageObj)) {
                    reqpage = intval(StdClass.getValue(reqpageObj, "ID"));
                } else {
                    reqpage = 0;
                }
            }

            if (!equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) ||
                    !equal(reqpage, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts"))) {
                q.putValue("pagename", Strings.str_replace("%2F", "/", URL.urlencode(URL.urldecode(strval(q.getValue("pagename"))))));
                page_paths = "/" + Strings.trim(strval(q.getValue("pagename")), "/");
                q.putValue("pagename", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(FileSystemOrSocket.basename(page_paths), ""));
                q.putValue("name", q.getValue("pagename"));
                where = strval(where) + " AND (ID = \'" + strval(reqpage) + "\')";
                reqpage_obj = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page(reqpage, gConsts.getOBJECT(), "raw");

                if (equal("attachment", StdClass.getValue(reqpage_obj, "post_type"))) {
                    this.is_attachment = true;
                    this.is_page = true;
                    q.putValue("attachment_id", reqpage);
                }
            }
        } else if (!equal("", q.getValue("attachment"))) {
            q.putValue("attachment", Strings.str_replace("%2F", "/", URL.urlencode(URL.urldecode(strval(q.getValue("attachment"))))));
            attach_paths = "/" + Strings.trim(strval(q.getValue("attachment")), "/");
            q.putValue("attachment", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(FileSystemOrSocket.basename(attach_paths), ""));
            q.putValue("name", q.getValue("attachment"));
            where = strval(where) + " AND " + gVars.wpdb.posts + ".post_name = \'" + strval(q.getValue("attachment")) + "\'";
        }

        if (booleanval(q.getValue("w"))) {
            where = strval(where) + " AND WEEK(" + gVars.wpdb.posts + ".post_date, 1)=\'" + strval(q.getValue("w")) + "\'";
        }

        if (booleanval(q.getValue("comments_popup"))) {
            q.putValue("p", intval(q.getValue("comments_popup")));
        }

		// If an attachment is requested by number, let it supercede any post number.
        if (booleanval(q.getValue("attachment_id"))) {
            q.putValue("p", q.getValue("attachment_id"));
        }

		// If a post number is specified, load that post
        if (booleanval(q.getValue("p"))) {
            where = " AND " + gVars.wpdb.posts + ".ID = " + strval(q.getValue("p"));
        }

        if (booleanval(q.getValue("page_id"))) {
            if (!equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) ||
                    !equal(q.getValue("page_id"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts"))) {
                q.putValue("p", q.getValue("page_id"));
                where = " AND " + gVars.wpdb.posts + ".ID = " + strval(q.getValue("page_id"));
            }
        }

		// If a search pattern is specified, load the posts that match
        if (!empty(q.getValue("s"))) {
        	// added slashes screw with quote grouping when done early, so done later
            q.putValue("s", Strings.stripslashes(gVars.webEnv, strval(q.getValue("s"))));

            if (booleanval(q.getValue("sentence"))) {
                q.putValue("search_terms", new Array<Object>(new ArrayEntry<Object>(q.getValue("s"))));
            } else {
                QRegExPerl.preg_match_all("/\".*?(\"|$)|((?<=[\\s\",+])|^)[^\\s\",+]+/", strval(q.getValue("s")), matches);
                q.putValue("search_terms", Array.array_map(new Callback("createFunction_trim", this), matches.getArrayValue(0)));
            }

            n = (booleanval(q.getValue("exact"))
                ? ""
                : "%");
            searchand = "";

            for (Map.Entry javaEntry562 : new Array<Object>(q.getValue("search_terms")).entrySet()) {
                term = strval(javaEntry562.getValue());
                term = getIncluded(FormattingPage.class, gVars, gConsts).addslashes_gpc(term);
                search = search + searchand + "((" + gVars.wpdb.posts + ".post_title LIKE \'" + n + term + n + "\') OR (" + gVars.wpdb.posts + ".post_content LIKE \'" + n + term + n + "\'))";
                searchand = " AND ";
            }

            term = gVars.wpdb.escape(strval(q.getValue("s")));

            if (!booleanval(q.getValue("sentence")) && (Array.count(q.getValue("search_terms")) > 1) && !equal(q.getArrayValue("search_terms").getValue(0), q.getValue("s"))) {
                search = search + " OR (" + gVars.wpdb.posts + ".post_title LIKE \'" + n + term + n + "\') OR (" + gVars.wpdb.posts + ".post_content LIKE \'" + n + term + n + "\')";
            }

            if (!empty(search)) {
                search = " AND (" + search + ") ";
            }
        }

		// Category stuff
        
        if (empty(q.getValue("cat")) || equal(q.getValue("cat"), "0") || 
				// Bypass cat checks if fetching specific posts
        		this.is_singular) {
            whichcat = "";
        } else {
            q.putValue("cat", "" + URL.urldecode(strval(q.getValue("cat"))) + "");
            q.putValue("cat", getIncluded(FormattingPage.class, gVars, gConsts).addslashes_gpc(strval(q.getValue("cat"))));
            cat_array = QRegExPerl.preg_split("/[,\\s]+/", strval(q.getValue("cat")));
            q.putValue("cat", "");
            req_cats = new Array<String>();

            for (Map.Entry javaEntry563 : cat_array.entrySet()) {
                cat = intval(javaEntry563.getValue());

                //				cat = floatval(intval(cat));
                req_cats.putValue(cat);
                in = cat > floatval(0);
                cat = Math.abs(cat);

                if (in) {
                    q.getArrayValue("category__in").putValue(cat);
                    q.putValue("category__in", Array.array_merge(q.getArrayValue("category__in"), (Array) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_children(cat, "category")));
                } else {
                    q.getArrayValue("category__not_in").putValue(cat);
                    q.putValue("category__not_in", Array.array_merge(q.getArrayValue("category__not_in"), (Array) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_children(cat, "category")));
                }
            }

            q.putValue("cat", Strings.implode(",", req_cats));
        }

        if (!empty(q.getValue("category__in")) || !empty(q.getValue("category__not_in")) || !empty(q.getValue("category__and"))) {
            groupby = gVars.wpdb.posts + ".ID";
        }

        if (!empty(q.getValue("category__in"))) {
            join = " INNER JOIN " + gVars.wpdb.term_relationships + " ON (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.term_relationships + ".object_id) INNER JOIN " + gVars.wpdb.term_taxonomy +
                " ON (" + gVars.wpdb.term_relationships + ".term_taxonomy_id = " + gVars.wpdb.term_taxonomy + ".term_taxonomy_id) ";
            whichcat = whichcat + " AND " + gVars.wpdb.term_taxonomy + ".taxonomy = \'category\' ";
            include_cats = "\'" + Strings.implode("\', \'", q.getArrayValue("category__in")) + "\'";
            whichcat = whichcat + " AND " + gVars.wpdb.term_taxonomy + ".term_id IN (" + include_cats + ") ";
        }

        if (!empty(q.getValue("category__not_in"))) {
            ids = getIncluded(TaxonomyPage.class, gVars, gConsts).get_objects_in_term(q.getValue("category__not_in"), "category", new Array<Object>());

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ids)) {
                return ids;
            }

            if (is_array(ids) && booleanval(Array.count(ids) > 0)) {
                out_posts = "\'" + Strings.implode("\', \'", (Array) ids) + "\'";
                whichcat = whichcat + " AND " + gVars.wpdb.posts + ".ID NOT IN (" + out_posts + ")";
            }
        }

		// Category stuff for nice URLs
        if (!equal("", q.getValue("category_name")) && !this.is_singular) {
            StdClass reqcatObj = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category_by_path(strval(q.getValue("category_name")), true, gConsts.getOBJECT());
            q.putValue("category_name", Strings.str_replace("%2F", "/", URL.urlencode(URL.urldecode(strval(q.getValue("category_name"))))));
            cat_paths = "/" + Strings.trim(strval(q.getValue("category_name")), "/");
            q.putValue("category_name", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(FileSystemOrSocket.basename(cat_paths), ""));
            cat_paths = "/" + Strings.trim(URL.urldecode(strval(q.getValue("category_name"))), "/");
            q.putValue("category_name", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(FileSystemOrSocket.basename(cat_paths), ""));

            Array<?> cat_pathsArray = Strings.explode("/", cat_paths);
            cat_path = "";

            for (Map.Entry javaEntry564 : cat_pathsArray.entrySet()) {
                pathdir = strval(javaEntry564.getValue());
                cat_path = cat_path + ((!equal(pathdir, ""))
                    ? "/"
                    : "") + getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(pathdir, "");
            }

            //if we don't match the entire hierarchy fallback on just matching the nicename
            if (empty(reqcatObj)) {
                reqcatObj = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category_by_path(strval(q.getValue("category_name")), false, gConsts.getOBJECT());
            }

            if (!empty(reqcatObj)) {
                reqcat = intval(StdClass.getValue(reqcatObj, "term_id"));
            } else {
                reqcat = 0;
            }

            q.putValue("cat", reqcat);
            join = " INNER JOIN " + gVars.wpdb.term_relationships + " ON (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.term_relationships + ".object_id) INNER JOIN " + gVars.wpdb.term_taxonomy +
                " ON (" + gVars.wpdb.term_relationships + ".term_taxonomy_id = " + gVars.wpdb.term_taxonomy + ".term_taxonomy_id) ";
            whichcat = " AND " + gVars.wpdb.term_taxonomy + ".taxonomy = \'category\' ";

            Array in_catsArray = new Array<Object>(new ArrayEntry<Object>(q.getValue("cat")));
            in_catsArray = Array.array_merge(in_catsArray, (Array) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_children(q.getValue("cat"), "category"));
            in_cats = "\'" + Strings.implode("\', \'", in_catsArray) + "\'";
            whichcat = whichcat + "AND " + gVars.wpdb.term_taxonomy + ".term_id IN (" + in_cats + ")";
            groupby = gVars.wpdb.posts + ".ID";
        }

		// Tags
        if (!equal("", q.getValue("tag"))) {
            if (!strictEqual(Strings.strpos(strval(q.getValue("tag")), ","), BOOLEAN_FALSE)) {
                tags = QRegExPerl.preg_split("/[,\\s]+/", strval(q.getValue("tag")));

                for (Map.Entry javaEntry565 : new Array<Object>(tags).entrySet()) {
                    tag = javaEntry565.getValue();
                    tag = getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("slug", tag, 0, "post_tag", "db");
                    q.getArrayValue("tag_slug__in").putValue(tag);
                }
            } else if (QRegExPerl.preg_match("/[+\\s]+/", strval(q.getValue("tag")))) {
                tags = QRegExPerl.preg_split("/[+\\s]+/", strval(q.getValue("tag")));

                for (Map.Entry javaEntry566 : new Array<Object>(tags).entrySet()) {
                    tag = javaEntry566.getValue();
                    tag = getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("slug", tag, 0, "post_tag", "db");
                    q.getArrayValue("tag_slug__and").putValue(tag);
                }
            } else {
                q.putValue("tag", getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("slug", strval(q.getValue("tag")), 0, "post_tag", "db"));
                q.getArrayValue("tag_slug__in").putValue(strval(q.getValue("tag")));
            }
        }

        if (!empty(q.getValue("tag__in")) || !empty(q.getValue("tag__not_in")) || !empty(q.getValue("tag__and")) || !empty(q.getValue("tag_slug__in")) || !empty(q.getValue("tag_slug__and"))) {
            groupby = gVars.wpdb.posts + ".ID";
        }

        if (!empty(q.getValue("tag__in"))) {
            join = " INNER JOIN " + gVars.wpdb.term_relationships + " ON (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.term_relationships + ".object_id) INNER JOIN " + gVars.wpdb.term_taxonomy +
                " ON (" + gVars.wpdb.term_relationships + ".term_taxonomy_id = " + gVars.wpdb.term_taxonomy + ".term_taxonomy_id) ";
            whichcat = whichcat + " AND " + gVars.wpdb.term_taxonomy + ".taxonomy = \'post_tag\' ";
            include_tags = "\'" + Strings.implode("\', \'", q.getArrayValue("tag__in")) + "\'";
            whichcat = whichcat + " AND " + gVars.wpdb.term_taxonomy + ".term_id IN (" + include_tags + ") ";
            reqtag = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).is_term(q.getArrayValue("tag__in").getValue(0), "post_tag");

            if (!empty(reqtag)) {
                q.putValue("tag_id", reqtag.getValue("term_id"));
            }
        }

        if (!empty(q.getValue("tag_slug__in"))) {
            join = " INNER JOIN " + gVars.wpdb.term_relationships + " ON (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.term_relationships + ".object_id) INNER JOIN " + gVars.wpdb.term_taxonomy +
                " ON (" + gVars.wpdb.term_relationships + ".term_taxonomy_id = " + gVars.wpdb.term_taxonomy + ".term_taxonomy_id) INNER JOIN " + gVars.wpdb.terms + " ON (" + gVars.wpdb.term_taxonomy +
                ".term_id = " + gVars.wpdb.terms + ".term_id) ";
            whichcat = whichcat + " AND " + gVars.wpdb.term_taxonomy + ".taxonomy = \'post_tag\' ";
            include_tags = "\'" + Strings.implode("\', \'", q.getArrayValue("tag_slug__in")) + "\'";
            whichcat = whichcat + " AND " + gVars.wpdb.terms + ".slug IN (" + include_tags + ") ";
            reqtag = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).is_term(q.getArrayValue("tag_slug__in").getValue(0), "post_tag");

            if (!empty(reqtag)) {
                q.putValue("tag_id", reqtag.getValue("term_id"));
            }
        }

        if (!empty(q.getValue("tag__not_in"))) {
            ids = getIncluded(TaxonomyPage.class, gVars, gConsts).get_objects_in_term(q.getValue("tag__not_in"), "post_tag", new Array<Object>());

            if (is_array(ids) && booleanval(Array.count(intval(ids) > 0))) {
                out_posts = "\'" + Strings.implode("\', \'", (Array) ids) + "\'";
                whichcat = whichcat + " AND " + gVars.wpdb.posts + ".ID NOT IN (" + out_posts + ")";
            }
        }

		// Tag and slug intersections.
        intersections = new Array<Object>(new ArrayEntry<Object>("category__and", "category"), new ArrayEntry<Object>("tag__and", "post_tag"), new ArrayEntry<Object>("tag_slug__and", "post_tag"));

        for (Map.Entry javaEntry567 : intersections.entrySet()) {
            item = javaEntry567.getKey();
            taxonomy = javaEntry567.getValue();

            if (empty(q.getValue(item))) {
                continue;
            }

            if (!equal(item, "category__and")) {
                reqtag = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).is_term(q.getArrayValue(item).getValue(0), "post_tag");

                if (!empty(reqtag)) {
                    q.putValue("tag_id", reqtag.getValue("term_id"));
                }
            }

            taxonomy_field = (equal(item, "tag_slug__and")
                ? "slug"
                : "term_id");
            q.putValue(item, Array.array_unique(q.getArrayValue(item)));
            tsql = "SELECT p.ID FROM " + gVars.wpdb.posts + " p INNER JOIN " + gVars.wpdb.term_relationships + " tr ON (p.ID = tr.object_id) INNER JOIN " + gVars.wpdb.term_taxonomy +
                " tt ON (tr.term_taxonomy_id = tt.term_taxonomy_id) INNER JOIN " + gVars.wpdb.terms + " t ON (tt.term_id = t.term_id)";
            tsql = tsql + " WHERE tt.taxonomy = \'" + strval(taxonomy) + "\' AND t." + taxonomy_field + " IN (\'" + Strings.implode("\', \'", q.getArrayValue(item)) + "\')";
            tsql = tsql + " GROUP BY p.ID HAVING count(p.ID) = " + strval(Array.count(q.getValue(item)));
            post_ids = gVars.wpdb.get_col(tsql);

            if (booleanval(Array.count(post_ids))) {
                whichcat = whichcat + " AND " + gVars.wpdb.posts + ".ID IN (" + Strings.implode(", ", post_ids) + ") ";
            } else {
                whichcat = " AND 0 = 1";

                break;
            }
        }

		// Taxonomies
        if (this.is_tax) {
            if (!equal("", q.getValue("taxonomy"))) {
                taxonomy = q.getValue("taxonomy");
                tt.putValue(taxonomy, q.getValue("term"));
                terms = getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms(q.getValue("taxonomy"), new Array<Object>(new ArrayEntry<Object>("slug", q.getValue("term"))));
            } else {
                for (Map.Entry javaEntry568 : gVars.wp_taxonomies.entrySet()) {
                    taxonomy = javaEntry568.getKey();
                    t = (StdClass) javaEntry568.getValue();

                    if (isset(StdClass.getValue(t, "query_var")) && !equal("", q.getValue(StdClass.getValue(t, "query_var")))) {
                        terms = getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms(taxonomy, new Array<Object>(new ArrayEntry<Object>("slug", q.getValue(StdClass.getValue(t, "query_var")))));

                        if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(terms)) {
                            break;
                        }
                    }
                }
            }

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(terms) || empty(terms)) {
                whichcat = " AND 0 ";
            } else {
                for (Map.Entry javaEntry569 : ((Array<?>) terms).entrySet()) {
                    StdClass termObj = (StdClass) javaEntry569.getValue();
                    term_ids.putValue(StdClass.getValue(termObj, "term_id"));
                }

                post_ids = (Array) getIncluded(TaxonomyPage.class, gVars, gConsts).get_objects_in_term(term_ids, taxonomy, new Array<Object>());

                if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_ids) && booleanval(Array.count(post_ids))) {
                    whichcat = whichcat + " AND " + gVars.wpdb.posts + ".ID IN (" + Strings.implode(", ", (Array) post_ids) + ") ";
                    post_type = "any";
                    q.putValue("post_status", "publish");
                    post_status_join = true;
                } else {
                    whichcat = " AND 0 ";
                }
            }
        }

		// Author/user stuff
        
        if (empty(strval(q.getValue("author"))) || equal(strval(q.getValue("author")), "0")) {
            whichauthor = "";
        } else {
            q.putValue("author", "" + URL.urldecode(strval(q.getValue("author"))) + "");
            q.putValue("author", getIncluded(FormattingPage.class, gVars, gConsts).addslashes_gpc(strval(q.getValue("author"))));

            if (!strictEqual(Strings.strpos(strval(q.getValue("author")), "-"), BOOLEAN_FALSE)) {
                eq = "!=";
                andor = "AND";
                q.putValue("author", Strings.explode("-", strval(q.getValue("author"))));
                q.putValue("author", "" + strval(q.getArrayValue("author").getValue(1)));
            } else {
                eq = "=";
                andor = "OR";
            }

            author_array = QRegExPerl.preg_split("/[,\\s]+/", strval(q.getValue("author")));
            whichauthor = whichauthor + " AND (" + gVars.wpdb.posts + ".post_author " + eq + " " + strval(author_array.getValue(0));

            for (i = 1; i < Array.count(author_array); i = i + 1) {
                whichauthor = whichauthor + " " + andor + " " + gVars.wpdb.posts + ".post_author " + eq + " " + strval(author_array.getValue(i));
            }

            whichauthor = whichauthor + ")";
        }

		// Author stuff for nice URLs
        
        if (!equal("", q.getValue("author_name"))) {
            if (!strictEqual(Strings.strpos(strval(q.getValue("author_name")), "/"), BOOLEAN_FALSE)) {
                q.putValue("author_name", Strings.explode("/", strval(q.getValue("author_name"))));

                if (booleanval(q.getArrayValue("author_name").getValue(Array.count(q.getValue("author_name")) - 1))) {
                    q.putValue("author_name", q.getArrayValue("author_name").getValue(Array.count(q.getValue("author_name")) - 1));//no trailing slash
                } else {
                    q.putValue("author_name", q.getArrayValue("author_name").getValue(Array.count(q.getValue("author_name")) - 2));//there was a trailling slash
                }
            }

            q.putValue("author_name", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(q.getValue("author_name")), ""));
            q.putValue("author", gVars.wpdb.get_var("SELECT ID FROM " + gVars.wpdb.users + " WHERE user_nicename=\'" + strval(q.getValue("author_name")) + "\'"));
            whichauthor = whichauthor + " AND (" + gVars.wpdb.posts + ".post_author = " + strval(q.getValue("author")) + ")";
        }

		// MIME-Type stuff for attachment browsing
        
        if (isset(q.getValue("post_mime_type")) && !equal("", q.getValue("post_mime_type"))) {
            whichmimetype = getIncluded(PostPage.class, gVars, gConsts).wp_post_mime_type_where(q.getValue("post_mime_type"));
        }

        where = strval(where) + search + whichcat + whichauthor + whichmimetype;

        if (empty(q.getValue("order")) || (!equal(Strings.strtoupper(strval(q.getValue("order"))), "ASC") && !equal(Strings.strtoupper(strval(q.getValue("order"))), "DESC"))) {
            q.putValue("order", "DESC");
        }

        // Order by
        if (empty(q.getValue("orderby"))) {
            q.putValue("orderby", gVars.wpdb.posts + ".post_date " + strval(q.getValue("order")));
        } else {
			// Used to filter values
            allowed_keys = new Array<Object>(
                    new ArrayEntry<Object>("author"),
                    new ArrayEntry<Object>("date"),
                    new ArrayEntry<Object>("category"),
                    new ArrayEntry<Object>("title"),
                    new ArrayEntry<Object>("modified"),
                    new ArrayEntry<Object>("menu_order"),
                    new ArrayEntry<Object>("parent"),
                    new ArrayEntry<Object>("ID"),
                    new ArrayEntry<Object>("rand"));
            q.putValue("orderby", URL.urldecode(strval(q.getValue("orderby"))));
            q.putValue("orderby", getIncluded(FormattingPage.class, gVars, gConsts).addslashes_gpc(strval(q.getValue("orderby"))));
            orderby_array = Strings.explode(" ", strval(q.getValue("orderby")));

            if (empty(orderby_array)) {
                orderby_array.putValue(q.getValue("orderby"));
            }

            q.putValue("orderby", "");

            for (i = 0; i < Array.count(orderby_array); i++) {
            	// Only allow certain values for safety
                orderby = orderby_array.getValue(i);

                {
                    int javaSwitchSelector88 = 0;

                    if (equal(orderby, "menu_order")) {
                        javaSwitchSelector88 = 1;
                    }

                    if (equal(orderby, "ID")) {
                        javaSwitchSelector88 = 2;
                    }

                    if (equal(orderby, "rand")) {
                        javaSwitchSelector88 = 3;
                    }

                    switch (javaSwitchSelector88) {
                    case 1:break;

                    case 2: {
                        orderby = gVars.wpdb.posts + ".ID";

                        break;
                    }

                    case 3: {
                        orderby = "RAND()";

                        break;
                    }

                    default:orderby = gVars.wpdb.posts + ".post_" + orderby;
                    }
                }

                if (Array.in_array(orderby_array.getValue(i), allowed_keys)) {
                    q.putValue("orderby", strval(q.getValue("orderby")) + (equal(i, 0)
                        ? ""
                        : ",") + orderby);
                }
            }

			// append ASC or DESC at the end
            if (!empty(q.getValue("orderby"))) {
                q.putValue("orderby", strval(q.getValue("orderby")) + " " + strval(q.getValue("order")));
            }

            if (empty(q.getValue("orderby"))) {
                q.putValue("orderby", gVars.wpdb.posts + ".post_date " + strval(q.getValue("order")));
            }
        }

        if (this.is_attachment) {
            where = strval(where) + " AND " + gVars.wpdb.posts + ".post_type = \'attachment\'";
        } else if (this.is_page) {
            where = strval(where) + " AND " + gVars.wpdb.posts + ".post_type = \'page\'";
        } else if (this.is_single) {
            where = strval(where) + " AND " + gVars.wpdb.posts + ".post_type = \'post\'";
        } else if (equal("any", post_type)) {
            where = strval(where) + "";
        } else {
            where = strval(where) + " AND " + gVars.wpdb.posts + ".post_type = \'" + post_type + "\'";
        }

        if (isset(q.getValue("post_status")) && !equal("", q.getValue("post_status"))) {
            statuswheres = new Array<Object>();
            q_status = Strings.explode(",", strval(q.getValue("post_status")));
            r_status = new Array<String>();
            p_status = new Array<String>();

            if (Array.in_array("draft", q_status)) {
                r_status.putValue(gVars.wpdb.posts + ".post_status = \'draft\'");
            }

            if (Array.in_array("pending", q_status)) {
                r_status.putValue(gVars.wpdb.posts + ".post_status = \'pending\'");
            }

            if (Array.in_array("future", q_status)) {
                r_status.putValue(gVars.wpdb.posts + ".post_status = \'future\'");
            }

            if (Array.in_array("inherit", q_status)) {
                r_status.putValue(gVars.wpdb.posts + ".post_status = \'inherit\'");
            }

            if (Array.in_array("private", q_status)) {
                p_status.putValue(gVars.wpdb.posts + ".post_status = \'private\'");
            }

            if (Array.in_array("publish", q_status)) {
                r_status.putValue(gVars.wpdb.posts + ".post_status = \'publish\'");
            }

            if (empty(q.getValue("perm")) || !equal("readable", q.getValue("perm"))) {
                r_status = Array.array_merge(r_status, p_status);
                p_status = null;
            }

            if (!empty(r_status)) {
                if (!empty(q.getValue("perm")) && equal("editable", q.getValue("perm")) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_" + post_type + "s")) {
                    statuswheres.putValue("(" + gVars.wpdb.posts + ".post_author = " + strval(gVars.user_ID) + " " + "AND (" + Strings.join(" OR ", r_status) + "))");
                } else {
                    statuswheres.putValue("(" + Strings.join(" OR ", r_status) + ")");
                }
            }

            if (!empty(p_status)) {
                if (!empty(q.getValue("perm")) && equal("readable", q.getValue("perm")) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("read_private_" + post_type + "s")) {
                    statuswheres.putValue("(" + gVars.wpdb.posts + ".post_author = " + strval(gVars.user_ID) + " " + "AND (" + Strings.join(" OR ", p_status) + "))");
                } else {
                    statuswheres.putValue("(" + Strings.join(" OR ", p_status) + ")");
                }
            }

            if (post_status_join) {
                join = join + " LEFT JOIN " + gVars.wpdb.posts + " AS p2 ON (" + gVars.wpdb.posts + ".post_parent = p2.ID) ";

                for (Map.Entry javaEntry570 : statuswheres.entrySet()) {
                    index = javaEntry570.getKey();
                    statuswhere = strval(javaEntry570.getValue());
                    statuswheres.putValue(index, "(" + statuswhere + " OR (" + gVars.wpdb.posts + ".post_status = \'inherit\' AND " + Strings.str_replace(gVars.wpdb.posts, "p2", statuswhere) + "))");
                }
            }

            for (Map.Entry javaEntry571 : statuswheres.entrySet()) {
                statuswhere = strval(javaEntry571.getValue());
                where = strval(where) + " AND " + statuswhere;
            }
        } else if (!this.is_singular) {
            where = strval(where) + " AND (" + gVars.wpdb.posts + ".post_status = \'publish\'";

            if (getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
                where = strval(where) + " OR " + gVars.wpdb.posts + ".post_status = \'future\' OR " + gVars.wpdb.posts + ".post_status = \'draft\' OR " + gVars.wpdb.posts +
                    ".post_status = \'pending\'";
            }

            if (getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
                where = strval(where) +
                    (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("read_private_" + post_type + "s")
                    ? (" OR " + gVars.wpdb.posts + ".post_status = \'private\'")
                    : (" OR " + gVars.wpdb.posts + ".post_author = " + strval(gVars.user_ID) + " AND " + gVars.wpdb.posts + ".post_status = \'private\'"));
            }

            where = strval(where) + ")";
        }

		// Apply filters on where and join prior to paging so that any
		// manipulations to them are reflected in the paging by day queries.
        where = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_where", where);
        join = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_join", join));

		// Paging
        if (empty(q.getValue("nopaging")) && !this.is_singular) {
            page = getIncluded(FunctionsPage.class, gVars, gConsts).absint(q.getValue("paged"));

            if (empty(page)) {
                page = 1;
            }

            if (empty(q.getValue("offset"))) {
                pgstrt = "";
                pgstrt = strval((page - 1) * intval(q.getValue("posts_per_page"))) + ", ";
                limits = "LIMIT " + pgstrt + strval(q.getValue("posts_per_page"));
            } else { // we're ignoring $page and using 'offset'
                q.putValue("offset", getIncluded(FunctionsPage.class, gVars, gConsts).absint(q.getValue("offset")));
                pgstrt = strval(q.getValue("offset")) + ", ";
                limits = "LIMIT " + pgstrt + strval(q.getValue("posts_per_page"));
            }
        }

		// Comments feeds
        if (this.is_comment_feed && (this.is_archive || this.is_search || !this.is_singular)) {
            if (this.is_archive || this.is_search) {
                cjoin = "LEFT JOIN " + gVars.wpdb.posts + " ON (" + gVars.wpdb.comments + ".comment_post_ID = " + gVars.wpdb.posts + ".ID) " + join + " ";
                cwhere = "WHERE comment_approved = \'1\' " + strval(where);
                cgroupby = "GROUP BY " + gVars.wpdb.comments + ".comment_id";
            } else { // Other non singular e.g. front
                cjoin = "LEFT JOIN " + gVars.wpdb.posts + " ON ( " + gVars.wpdb.comments + ".comment_post_ID = " + gVars.wpdb.posts + ".ID )";
                cwhere = "WHERE post_status = \'publish\' AND comment_approved = \'1\'";
                cgroupby = "";
            }

            cjoin = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_feed_join", cjoin));
            cwhere = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_feed_where", cwhere);
            cgroupby = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_feed_groupby", cgroupby));
            this.comments = (gVars.wpdb.get_results(
                        "SELECT " + distinct + " " + gVars.wpdb.comments + ".* FROM " + gVars.wpdb.comments + " " + cjoin + " " + cwhere + " " + cgroupby + " ORDER BY comment_date_gmt DESC LIMIT " +
                        getIncluded(FunctionsPage.class, gVars, gConsts).get_option("posts_per_rss")));
            this.comment_count = Array.count(this.comments);
            post_ids = new Array<Object>();

            for (Map.Entry javaEntry572 : this.comments.entrySet()) {
                comment = (StdClass) javaEntry572.getValue();
                ((Array) post_ids).putValue(StdClass.getValue(comment, "comment_post_ID"));
            }

            String post_idsStr = Strings.join(",", (Array) post_ids);
            join = "";

            if (booleanval(post_idsStr)) {
                where = "AND " + gVars.wpdb.posts + ".ID IN (" + post_idsStr + ") ";
            } else {
                where = "AND 0";
            }
        }

		// Apply post-paging filters on where and join.  Only plugins that
		// manipulate paging queries should use these hooks.
        
        where = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_where_paged", where);
        groupby = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_groupby", groupby));
        join = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_join_paged", join));
        orderby = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_orderby", q.getValue("orderby")));
        distinct = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_distinct", distinct));
        fields = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_fields", gVars.wpdb.posts + ".*");
        limits = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_limits", limits);
        
        // Announce current selection parameters.  For use by caching plugins.
        getIncluded(PluginPage.class, gVars, gConsts).do_action("posts_selection", strval(where) + groupby + orderby + strval(limits) + join);
        
        // Filter again for the benefit of caching plugins.  Regular plugins should use the hooks above.
        where = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_where_request", where);
        groupby = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_groupby_request", groupby));
        join = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_join_request", join));
        orderby = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_orderby_request", orderby));
        distinct = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_distinct_request", distinct));
        fields = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_fields_request", fields);
        limits = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_limits_request", limits);

        if (!empty(groupby)) {
            groupby = "GROUP BY " + groupby;
        }

        if (!empty(orderby)) {
            orderby = "ORDER BY " + orderby;
        }

        found_rows = "";

        if (!empty(limits)) {
            found_rows = "SQL_CALC_FOUND_ROWS";
        }

        request = " SELECT " + found_rows + " " + distinct + " " + strval(fields) + " FROM " + gVars.wpdb.posts + " " + join + " WHERE 1=1 " + strval(where) + " " + groupby + " " + orderby + " " +
            strval(limits);
        this.request = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_request", request));
        
        this.posts = gVars.wpdb.get_results(this.request);
		// Raw results filter.  Prior to status checks.
        this.posts = (Array<StdClass>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("posts_results", this.posts);

        if (!empty(this.posts) && this.is_comment_feed && this.is_singular) {
            cjoin = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_feed_join", ""));
            cwhere = getIncluded(PluginPage.class, gVars, gConsts)
                         .apply_filters("comment_feed_where", "WHERE comment_post_ID = \'" + this.posts.getValue(0).fields.getValue("ID") + "\' AND comment_approved = \'1\'");
            comments_request = "SELECT " + gVars.wpdb.comments + ".* FROM " + gVars.wpdb.comments + " " + cjoin + " " + strval(cwhere) + " ORDER BY comment_date_gmt DESC LIMIT " +
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("posts_per_rss");
            this.comments = gVars.wpdb.get_results(comments_request);
            this.comment_count = Array.count(this.comments);
        }

        if (!empty(limits)) {
            found_posts_query = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("found_posts_query", "SELECT FOUND_ROWS()"));
            this.found_posts = intval(gVars.wpdb.get_var(found_posts_query));
            this.found_posts = intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("found_posts", this.found_posts));
            this.max_num_pages = Math.ceil(floatval(this.found_posts) / floatval(q.getValue("posts_per_page")));
        }

        // Check post status to determine if post should be displayed.
        if (!empty(this.posts) && (this.is_single || this.is_page)) {
            status = getIncluded(PostPage.class, gVars, gConsts).get_post_status(this.posts.getValue(0));
            //$type = get_post_type($this->posts[0]);
            if (!equal("publish", status)) {
                if (!getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
                	// User must be logged in to view unpublished posts.
                    this.posts = new Array<StdClass>();
                } else {
                    if (Array.in_array(status, new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
                    	// User must have edit permissions on the draft to preview.
                        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", this.posts.getValue(0).fields.getValue("ID"))) {
                            this.posts = new Array<StdClass>();
                        } else {
                            this.is_preview = true;
                            this.posts.getValue(0).fields.putValue("post_date", getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
                        }
                    } else if (equal("future", status)) {
                        this.is_preview = true;

                        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", this.posts.getValue(0).fields.getValue("ID"))) {
                            this.posts = new Array<StdClass>();
                        }
                    } else {
                        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("read_post", this.posts.getValue(0).fields.getValue("ID"))) {
                            this.posts = new Array<StdClass>();
                        }
                    }
                }
            }
        }

        this.posts = (Array<StdClass>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_posts", this.posts);
        getIncluded(PostPage.class, gVars, gConsts).update_post_caches(this.posts);
        this.post_count = Array.count(this.posts);

        if (this.post_count > 0) {
            this.post = this.posts.getValue(0);
        }

        return this.posts;
    }

    public StdClass next_post() {
        this.current_post++;
        this.post = this.posts.getValue(this.current_post);

        return this.post;
    }

    public void the_post() {
        this.in_the_loop = true;
        gVars.post = this.next_post();
        getIncluded(QueryPage.class, gVars, gConsts).setup_postdata(gVars.post);

        if (equal(this.current_post, 0)) { // loop has just started
            getIncluded(PluginPage.class, gVars, gConsts).do_action("loop_start", "");
        }
    }

    public boolean have_posts() {
        if ((this.current_post + 1) < this.post_count) {
            return true;
        } else if (equal(this.current_post + 1, this.post_count)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("loop_end", "");
            // Do some cleaning up after the loop
            this.rewind_posts();
        }

        this.in_the_loop = false;

        return false;
    }

    public void rewind_posts() {
        this.current_post = -1;

        if (this.post_count > 0) {
            this.post = this.posts.getValue(0);
        }
    }

    public StdClass next_comment() {
        this.current_comment++;
        this.comment = this.comments.getValue(this.current_comment);

        return this.comment;
    }

    public void the_comment() {
        gVars.comment = this.next_comment();

        if (equal(this.current_comment, 0)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_loop_start", "");
        }
    }

    public boolean have_comments() {
        if ((this.current_comment + 1) < this.comment_count) {
            return true;
        } else if (equal(this.current_comment + 1, this.comment_count)) {
            this.rewind_comments();
        }

        return false;
    }

    public void rewind_comments() {
        this.current_comment = -1;

        if (this.comment_count > 0) {
            this.comment = this.comments.getValue(0);
        }
    }

    public Object query(Object query) {
        this.parse_query(query);

        return this.get_posts();
    }

    public Object get_queried_object() {
        Object cat = null;
        StdClass category;
        Object tag_id = null;
        StdClass tag;
        Object tax = null;
        Object slug = null;
        StdClass term;
        int author_id = 0;
        StdClass author;

        if (isset(this.queried_object)) {
            return this.queried_object;
        }

        this.queried_object = null;
        this.queried_object_id = 0;

        if (this.is_category) {
            cat = this.get("cat");
            category = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat, gConsts.getOBJECT(), "raw");
            this.queried_object = category;
            this.queried_object_id = intval(cat);
        } else if (this.is_tag) {
            tag_id = this.get("tag_id");
            tag = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(tag_id, "post_tag", gConsts.getOBJECT(), "raw");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(tag)) {
                return tag;
            }

            this.queried_object = tag;
            this.queried_object_id = intval(tag_id);
        } else if (this.is_tax) {
            tax = this.get("taxonomy");
            slug = this.get("term");
            term = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms(tax, new Array<Object>(new ArrayEntry<Object>("slug", slug)));

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(term)) {
                return term;
            }

            this.queried_object = term;
            this.queried_object_id = intval(StdClass.getValue(term, "term_id"));
        } else if (this.is_posts_page) {
            this.queried_object = (StdClass) getIncluded(PostPage.class, gVars, gConsts)
                                                 .get_page(intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts")), gConsts.getOBJECT(), "raw");
            this.queried_object_id = intval(StdClass.getValue(this.queried_object, "ID"));
        } else if (this.is_single) {
            this.queried_object = this.post;
            this.queried_object_id = intval(StdClass.getValue(this.post, "ID"));
        } else if (this.is_page) {
            this.queried_object = this.post;
            this.queried_object_id = intval(StdClass.getValue(this.post, "ID"));
        } else if (this.is_author) {
            author_id = intval(this.get("author"));
            author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(author_id);
            this.queried_object = author;
            this.queried_object_id = author_id;
        }

        return this.queried_object;
    }

    public int get_queried_object_id() {
        this.get_queried_object();

        if (isset(this.queried_object_id)) {
            return this.queried_object_id;
        }

        return 0;
    }

    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}
