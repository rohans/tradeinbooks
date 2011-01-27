/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: BookmarkPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ClassHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class BookmarkPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(BookmarkPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/bookmark.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/bookmark";
    }

    /**
     * Link/Bookmark API
     *
     * @package WordPress
     * @subpackage Bookmark
     */

    /**
     * get_bookmark() - Get Bookmark data based on ID
     *
     * @since 2.1
     * @uses $wpdb Database Object
     *
     * @param int $bookmark_id
     * @param string $output Optional. Either OBJECT, ARRAY_N, or ARRAY_A constant
     * @param string $filter Optional, default is 'raw'.
     * @return array|object Type returned depends on $output value.
     */
    public Object get_bookmark(Object bookmark_id, Object output, String filter) {
        StdClass link = null;
        link = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.links + " WHERE link_id = %d LIMIT 1", bookmark_id));
        link.fields.putValue(
            "link_category",
            Array.array_unique((Array) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(StdClass.getValue(link, "link_id"), "link_category", "fields=ids")));
        link = (StdClass) sanitize_bookmark(link, filter);

        if (equal(output, gConsts.getOBJECT())) {
            return link;
        } else if (equal(output, gConsts.getARRAY_A())) {
            return ClassHandling.get_object_vars(link);
        } else if (equal(output, gConsts.getARRAY_N())) {
            return Array.array_values(ClassHandling.get_object_vars(link));
        } else {
            return link;
        }
    }

    /**
     * get_bookmark_field() - Gets single bookmark data item or field.
     * @since 2.3
     * @uses get_bookmark() Gets bookmark object using $bookmark as ID
     * @uses sanitize_bookmark_field() Sanitizes Bookmark field based on
     * $context.
     * @param string $field The name of the data field to return
     * @param int $bookmark The bookmark ID to get field
     * @param string $context Optional. The context of how the field will be
     * used.
     * @return string
     */
    public Object get_bookmark_field(Object field, int bookmark, Object context) {
        //		bookmark=intval(bookmark);
        Object bookmarkObj = get_bookmark(bookmark, gConsts.getOBJECT(), "raw");

        /* Do not change type */
        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(bookmarkObj)) {
            return bookmarkObj;
        }

        if (!is_object(bookmarkObj)) {
            return "";
        }

        if (!isset(((StdClass) bookmarkObj).fields.getValue(field))) {
            return "";
        }

        return sanitize_bookmark_field(strval(field), ((StdClass) bookmarkObj).fields.getValue(field), ((StdClass) bookmarkObj).fields.getValue("link_id"), strval(context));
    }

    /**
     * get_link() - Returns bookmark data based on ID.
     * @since 2.0
     * @deprecated Use get_bookmark()
     * @see get_bookmark()
     * @param int $bookmark_id ID of link
     * @param string $output Either OBJECT, ARRAY_N, or ARRAY_A
     * @return object|array
     */
    public Object get_link(int bookmark_id, Object output, String filter) {
        return get_bookmark(bookmark_id, output, filter);
    }

    /**
     * get_bookmarks() - Retrieves the list of bookmarks
     *
     * Attempts to retrieve from the cache first based on MD5 hash of arguments. If
     * that fails, then the query will be built from the arguments and executed. The
     * results will be stored to the cache.
     *
     * List of default arguments are as follows:
     * 'orderby' - Default is 'name' (string). How to order the links by. String is based off of the bookmark scheme.
     * 'order' - Default is 'ASC' (string). Either 'ASC' or 'DESC'. Orders in either ascending or descending order.
     * 'limit' - Default is -1 (integer) or show all. The amount of bookmarks to display.
     * 'category' - Default is empty string (string). Include the links in what category ID(s).
     * 'category_name' - Default is empty string (string). Get links by category name.
     * 'hide_invisible' - Default is 1 (integer). Whether to show (default) or hide links marked as 'invisible'.
     * 'show_updated' - Default is 0 (integer). Will show the time of when the bookmark was last updated.
     * 'include' - Default is empty string (string). Include other categories separated by commas.
     * 'exclude' - Default is empty string (string). Exclude other categories separated by commas.
     *
     * @since 2.1
     * @uses $wpdb Database Object
     *
     * @param string|array $args List of arguments to overwrite the defaults
     * @return array List of bookmark row objects
     */
    public Array<Object> get_bookmarks(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        String key = null;
        Array<Object> cache = new Array<Object>();
        String inclusions = null;
        String include = null;
        String exclude = null;
        String category = null;
        String category_name = null;
        Array<Object> inclinks = new Array<Object>();
        Object inclink = null;
        String exclusions = null;
        Array<Object> exlinks = new Array<Object>();
        Object exlink = null;
        String search = null;
        String category_query = null;
        String join = null;
        Array<Object> incategories = new Array<Object>();
        Object incat = null;
        String recently_updated_test = null;
        String get_updated = null;
        Object show_updated = null;
        String orderby = null;
        String length = null;
        String visible = null;
        Object hide_invisible = null;
        String query = null;
        Object order = null;
        Object limit = null;
        Array<Object> results = new Array<Object>();
        defaults = new Array<Object>(
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("limit", -1),
                new ArrayEntry<Object>("category", ""),
                new ArrayEntry<Object>("category_name", ""),
                new ArrayEntry<Object>("hide_invisible", 1),
                new ArrayEntry<Object>("show_updated", 0),
                new ArrayEntry<Object>("include", ""),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("search", ""));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        include = strval(Array.extractVar(r, "include", include, Array.EXTR_SKIP));
        exclude = strval(Array.extractVar(r, "exclude", exclude, Array.EXTR_SKIP));
        category = strval(Array.extractVar(r, "category", category, Array.EXTR_SKIP));
        category_name = strval(Array.extractVar(r, "category_name", category_name, Array.EXTR_SKIP));
        search = strval(Array.extractVar(r, "search", search, Array.EXTR_SKIP));
        show_updated = Array.extractVar(r, "show_updated", show_updated, Array.EXTR_SKIP);
        orderby = strval(Array.extractVar(r, "orderby", orderby, Array.EXTR_SKIP));
        hide_invisible = Array.extractVar(r, "hide_invisible", hide_invisible, Array.EXTR_SKIP);
        order = Array.extractVar(r, "order", order, Array.EXTR_SKIP);
        limit = Array.extractVar(r, "limit", limit, Array.EXTR_SKIP);
        key = Strings.md5(serialize(r));

        if (booleanval(cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("get_bookmarks", "bookmark"))) {
            if (isset(cache.getValue(key))) {
                return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_bookmarks", cache.getValue(key), r);
            }
        } else {
            cache = new Array<Object>();
        }

        inclusions = "";

        if (!empty(include)) {
            exclude = "";  //ignore exclude, category, and category_name params if using include
            category = "";
            category_name = "";
            inclinks = QRegExPerl.preg_split("/[\\s,]+/", include);

            if (booleanval(Array.count(inclinks))) {
                for (Map.Entry javaEntry380 : inclinks.entrySet()) {
                    inclink = javaEntry380.getValue();

                    if (empty(inclusions)) {
                        inclusions = " AND ( link_id = " + strval(inclink) + " ";
                    } else {
                        inclusions = inclusions + " OR link_id = " + strval(inclink) + " ";
                    }
                }
            }
        }

        if (!empty(inclusions)) {
            inclusions = inclusions + ")";
        }

        exclusions = "";

        if (!empty(exclude)) {
            exlinks = QRegExPerl.preg_split("/[\\s,]+/", exclude);

            if (booleanval(Array.count(exlinks))) {
                for (Map.Entry javaEntry381 : exlinks.entrySet()) {
                    exlink = javaEntry381.getValue();

                    if (empty(exclusions)) {
                        exclusions = " AND ( link_id <> " + strval(exlink) + " ";
                    } else {
                        exclusions = exclusions + " AND link_id <> " + strval(exlink) + " ";
                    }
                }
            }
        }

        if (!empty(exclusions)) {
            exclusions = exclusions + ")";
        }

        if (!empty(category_name)) {
            StdClass categoryObj;

            if (booleanval(categoryObj = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("name", category_name, "link_category", gConsts.getOBJECT(), "raw"))) {
                category = strval(StdClass.getValue(categoryObj, "term_id"));
            }
        }

        if (!empty(search)) {
            search = getIncluded(FormattingPage.class, gVars, gConsts).like_escape(search);
            search = " AND ( (link_url LIKE \'%" + search + "%\') OR (link_name LIKE \'%" + search + "%\') OR (link_description LIKE \'%" + search + "%\') ) ";
        }

        category_query = "";
        join = "";

        if (!empty(category)) {
            incategories = QRegExPerl.preg_split("/[\\s,]+/", category);

            if (booleanval(Array.count(incategories))) {
                for (Map.Entry javaEntry382 : incategories.entrySet()) {
                    incat = javaEntry382.getValue();

                    if (empty(category_query)) {
                        category_query = " AND ( tt.term_id = " + strval(incat) + " ";
                    } else {
                        category_query = category_query + " OR tt.term_id = " + strval(incat) + " ";
                    }
                }
            }
        }

        if (!empty(category_query)) {
            category_query = category_query + ") AND taxonomy = \'link_category\'";
            join = " INNER JOIN " + gVars.wpdb.term_relationships + " AS tr ON (" + gVars.wpdb.links + ".link_id = tr.object_id) INNER JOIN " + gVars.wpdb.term_taxonomy +
                " as tt ON tt.term_taxonomy_id = tr.term_taxonomy_id";
        }

        if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_recently_updated_time"))) {
            recently_updated_test = ", IF (DATE_ADD(link_updated, INTERVAL " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_recently_updated_time") +
                " MINUTE) >= NOW(), 1,0) as recently_updated ";
        } else {
            recently_updated_test = "";
        }

        get_updated = (booleanval(show_updated)
            ? ", UNIX_TIMESTAMP(link_updated) AS link_updated_f "
            : "");
        orderby = Strings.strtolower(orderby);
        length = "";

        {
            int javaSwitchSelector38 = 0;

            if (equal(orderby, "length")) {
                javaSwitchSelector38 = 1;
            }

            if (equal(orderby, "rand")) {
                javaSwitchSelector38 = 2;
            }

            switch (javaSwitchSelector38) {
            case 1: {
                length = ", CHAR_LENGTH(link_name) AS length";

                break;
            }

            case 2: {
                orderby = "rand()";

                break;
            }

            default:
                orderby = "link_" + orderby;
            }
        }

        if (equal("link_id", orderby)) {
            orderby = gVars.wpdb.links + ".link_id";
        }

        visible = "";

        if (booleanval(hide_invisible)) {
            visible = "AND link_visible = \'Y\'";
        }

        query = "SELECT * " + length + " " + recently_updated_test + " " + get_updated + " FROM " + gVars.wpdb.links + " " + join + " WHERE 1=1 " + visible + " " + category_query;
        query = query + " " + exclusions + " " + inclusions + " " + search;
        query = query + " ORDER BY " + orderby + " " + strval(order);

        if (!equal(limit, -1)) {
            query = query + " LIMIT " + strval(limit);
        }

        results = gVars.wpdb.get_results(query);
        cache.putValue(key, results);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("get_bookmarks", cache, "bookmark", 0);

        return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_bookmarks", results, r);
    }

    /**
     * sanitize_bookmark() - Sanitizes all bookmark fields
     * @since 2.3
     * @param object|array $bookmark Bookmark row
     * @param string $context Optional, default is 'display'. How to filter the
     * fields
     * @return object|array Same type as $bookmark but with fields sanitized.
     */
    public Object sanitize_bookmark(Object bookmark, /* Do not change type */
        String context) {
        Array<Object> fields = new Array<Object>();
        boolean do_object = false;
        Object field = null;
        fields = new Array<Object>(
                new ArrayEntry<Object>("link_id"),
                new ArrayEntry<Object>("link_url"),
                new ArrayEntry<Object>("link_name"),
                new ArrayEntry<Object>("link_image"),
                new ArrayEntry<Object>("link_target"),
                new ArrayEntry<Object>("link_category"),
                new ArrayEntry<Object>("link_description"),
                new ArrayEntry<Object>("link_visible"),
                new ArrayEntry<Object>("link_owner"),
                new ArrayEntry<Object>("link_rating"),
                new ArrayEntry<Object>("link_updated"),
                new ArrayEntry<Object>("link_rel"),
                new ArrayEntry<Object>("link_notes"),
                new ArrayEntry<Object>("link_rss"));
        do_object = false;

        if (is_object(bookmark)) {
            do_object = true;
        }

        for (Map.Entry javaEntry383 : fields.entrySet()) {
            field = javaEntry383.getValue();

            if (do_object) {
                ((StdClass) bookmark).fields.putValue(
                    field,
                    sanitize_bookmark_field(strval(field), ((StdClass) bookmark).fields.getValue(field), ((StdClass) bookmark).fields.getValue("link_id"), context));
            } else {
                ((Array) bookmark).putValue(field, sanitize_bookmark_field(strval(field), ((Array) bookmark).getValue(field), ((Array) bookmark).getValue("link_id"), context));
            }
        }

        return bookmark;
    }

    /**
     * sanitize_bookmark_field() - Sanitizes a bookmark field
     *
     * Sanitizes the bookmark fields based on what the field name is. If the field has a
     * strict value set, then it will be tested for that, else a more generic filtering is
     * applied. After the more strict filter is applied, if the $context is 'raw' then the
     * value is immediately return.
     *
     * Hooks exist for the more generic cases. With the 'edit' context, the 'edit_$field'
     * filter will be called and passed the $value and $bookmark_id respectively. With the
     * 'db' context, the 'pre_$field' filter is called and passed the value. The 'display'
     * context is the final context and has the $field has the filter name and is passed the
     * $value, $bookmark_id, and $context respectively.
     *
     * @since 2.3
     *
     * @param string $field The bookmark field
     * @param mixed $value The bookmark field value
     * @param int $bookmark_id Bookmark ID
     * @param string $context How to filter the field value. Either 'raw', 'edit', 'attribute', 'js', 'db', or 'display'
     * @return mixed The filtered value
     */
    public Object sanitize_bookmark_field(String field, Object value, Object bookmark_id, String context) {
        Array<Object> int_fields = new Array<Object>();
        Array<Object> yesno = new Array<Object>();
        Array<Object> targets = new Array<Object>();
        Array<Object> format_to_edit = new Array<Object>();
        int_fields = new Array<Object>(new ArrayEntry<Object>("link_id"), new ArrayEntry<Object>("link_rating"));

        if (Array.in_array(field, int_fields)) {
            value = intval(value);
        }

        yesno = new Array<Object>(new ArrayEntry<Object>("link_visible"));

        if (Array.in_array(field, yesno)) {
            value = QRegExPerl.preg_replace("/[^YNyn]/", "", strval(value));
        }

        if (equal("link_target", field)) {
            targets = new Array<Object>(new ArrayEntry<Object>("_top"), new ArrayEntry<Object>("_blank"));

            if (!Array.in_array(value, targets)) {
                value = "";
            }
        }

        if (equal("raw", context)) {
            return value;
        }

        if (equal("edit", context)) {
            format_to_edit = new Array<Object>(new ArrayEntry<Object>("link_notes"));
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_" + field, value, bookmark_id);

            if (Array.in_array(field, format_to_edit)) {
                value = getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(value), false);
            } else {
                if (value instanceof Array) {
                    value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escapeArray((Array) value);
                } else {
                    value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(value));
                }
            }
        } else if (equal("db", context)) {
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_" + field, value);
        } else {
        	// Use display filters by default.
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(field, value, bookmark_id, context);
        }

        if (equal("attribute", context)) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(value));
        } else if (equal("js", context)) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(strval(value));
        }

        return value;
    }

    /**
     * delete_get_bookmark_cache() - Deletes entire bookmark cache
     * @since 2.1
     * @uses wp_cache_delete() Deletes the contents of 'get_bookmarks'
     */
    public void delete_get_bookmark_cache(Object... deprecated) {
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("get_bookmarks", "bookmark");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_bookmark_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_action("add_link", Callback.createCallbackArray(this, "delete_get_bookmark_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("edit_link", Callback.createCallbackArray(this, "delete_get_bookmark_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("delete_link", Callback.createCallbackArray(this, "delete_get_bookmark_cache"), 10, 1);

        return DEFAULT_VAL;
    }
}
