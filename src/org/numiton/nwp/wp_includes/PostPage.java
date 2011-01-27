/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PostPage.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.*;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class PostPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PostPage.class.getName());

    /**
     * Generated in place of local variable 'main' from method 'get_extended'
     * because it is used inside an inner class.
     */
    String get_extended_main;

    /**
     * Generated in place of local variable 'extended' from method
     * 'get_extended' because it is used inside an inner class.
     */
    String get_extended_extended = null;
    public Array<Object> cache_lastpostmodified = new Array<Object>();

    @Override
    @RequestMapping("/wp-includes/post.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/post";
    }

    /**
     * Post functions and post utility function
     *
     * @package WordPress
     * @subpackage Post
     * @since 1.5
     */

    /**
     * get_attached_file() - Get metadata for an attached file
     *
     * {@internal Missing Long Description}}
     *
     * @package WordPress
     * @subpackage Post
     * @since 2.0
     *
     * @param int $attachment_id Attachment ID
     * @param bool $unfiltered Whether to apply filters or not
     * @return array {@internal Missing Description}}
     */
    public Object get_attached_file(int attachment_id, boolean unfiltered) {
        Object file = get_post_meta(attachment_id, "_wp_attached_file", true);

        if (unfiltered) {
            return file;
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_attached_file", file, attachment_id);
    }

    /**
     * update_attached_file() - Update attached file metadata{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $attachment_id Attachment ID
     * @param string $file {@internal Missing Description}}
     * @return bool|mixed {@internal Missing Description}}
     */
    public Object update_attached_file(int attachment_id, String file) {
        Object old_file = null;

        if (!booleanval(get_post(attachment_id, gConsts.getOBJECT(), "raw"))) {
            return false;
        }

        old_file = get_attached_file(attachment_id, true);
        file = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("update_attached_file", file, attachment_id));

        if (booleanval(old_file)) {
            return update_post_meta(attachment_id, "_wp_attached_file", file, old_file);
        } else {
            return add_post_meta(attachment_id, "_wp_attached_file", file, false);
        }
    }

    /**
     * get_children() - Get post children{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.0
     * @param mixed $args {@internal Missing Description}}
     * @param string $output {@internal Missing Description}}
     * @return mixed {@internal Missing Description}}
     */
    public Array<Object> get_children(Object args, String output)/* Do not change type */
     {
        Array<Object> defaults = new Array<Object>();
        Object r = null;
        Array<?> children;
        Array<Object> kids = new Array<Object>();
        StdClass child = null;
        Object key = null;
        Array<Object> weeuns = new Array<Object>();
        StdClass kid = null;
        Array<Object> babes = new Array<Object>();

        if (empty(args)) {
            if (isset(gVars.post)) {
                args = "post_parent=" + strval(StdClass.getValue(gVars.post, "post_parent"));
            } else {
                return new Array<Object>();
            }
        } else if (is_object(args)) {
            args = "post_parent=" + strval(((StdClass) args).fields.getValue("post_parent"));
        } else if (is_numeric(args)) {
            args = "post_parent=" + strval(args);
        }

        defaults = new Array<Object>(new ArrayEntry<Object>("numberposts", -1), new ArrayEntry<Object>("post_type", ""), new ArrayEntry<Object>("post_status", ""),
                new ArrayEntry<Object>("post_parent", 0));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        children = get_posts(r);

        if (!booleanval(children)) {
            return new Array<Object>();
        }

        update_post_cache(children);

        for (Map.Entry javaEntry524 : children.entrySet()) {
            key = javaEntry524.getKey();
            child = (StdClass) javaEntry524.getValue();
            kids.putValue(StdClass.getValue(child, "ID"), children.getRef(key));
        }

        if (equal(output, gConsts.getOBJECT())) {
            return kids;
        } else if (equal(output, gConsts.getARRAY_A())) {
            for (Map.Entry javaEntry525 : kids.entrySet()) {
                kid = (StdClass) javaEntry525.getValue();
                weeuns.putValue(StdClass.getValue(kid, "ID"), ClassHandling.get_object_vars(kids.getValue(StdClass.getValue(kid, "ID"))));
            }

            return weeuns;
        } else if (equal(output, gConsts.getARRAY_N())) {
            for (Map.Entry javaEntry526 : kids.entrySet()) {
                kid = (StdClass) javaEntry526.getValue();
                babes.putValue(StdClass.getValue(kid, "ID"), Array.array_values(ClassHandling.get_object_vars(kids.getValue(StdClass.getValue(kid, "ID")))));
            }

            return babes;
        } else {
            return kids;
        }
    }

    /**
     * get_extended() - get extended entry info (<!--more-->){@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @param string $post {@internal Missing Description}}
     * @return array {@internal Missing Description}}
     */
    public Array<Object> get_extended(String post) {
        Array<Object> matches = new Array<Object>();

    	//Match the new style more links
        if (QRegExPerl.preg_match("/<!--more(.*?)?-->/", post, matches)) {
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        get_extended_main = srcArray.getValue(0);
                        get_extended_extended = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(Strings.explode(strval(matches.getValue(0)), post, 2));
        } else {
            get_extended_main = post;
            get_extended_extended = "";
        }

        // Strip leading and trailing whitespace
        get_extended_main = QRegExPerl.preg_replace("/^[\\s]*(.*)[\\s]*$/", "\\1", get_extended_main);
        get_extended_extended = QRegExPerl.preg_replace("/^[\\s]*(.*)[\\s]*$/", "\\1", get_extended_extended);

        return new Array<Object>(new ArrayEntry<Object>("main", get_extended_main), new ArrayEntry<Object>("extended", get_extended_extended));
    }

    public Object get_post(Object post) {
        return get_post(post, gConsts.getOBJECT(), "raw");
    }

    public Object get_post(Object post, String output) {
        return get_post(post, output, "raw");
    }

    /**
     * get_post() - Retrieves post data given a post ID or post object.{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5.1
     * @uses $wpdb
     * @param int|object &$post post ID or post object
     * @param string $output {@internal Missing Description}}
     * @param string $filter {@internal Missing Description}}
     * @return mixed {@internal Missing Description}}
     */
    public Object get_post(Object post, String output, /* Do not change type */
        String filter) {
        Object _null;
        Object _post;
        Array<Object> __post;
        _null = null;

        if (empty(post)) {
            if (isset(gVars.post)) {
                _post = gVars.post;
            } else {
                return _null;
            }
        } else if (is_object(post)) {
            StdClass postObj = (StdClass) post;
            _get_post_ancestors(postObj);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(postObj, "ID"), post, "posts", 0);
            _post = post;
        } else {
            // post = intval(post);
            if (!booleanval(_post = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(post, "posts"))) {
                _post = gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.posts + " WHERE ID = %d LIMIT 1", post));

                // Added by Numiton
                if (is_null(_post)) {
                    return null;
                }

                StdClass postObj = (StdClass) _post;
                _get_post_ancestors(postObj);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(postObj, "ID"), _post, "posts", 0);
            }
        }

        _post = sanitize_post(_post, filter);

        if (equal(output, gConsts.getOBJECT())) {
            return _post;
        } else if (equal(output, gConsts.getARRAY_A())) {
            __post = ClassHandling.get_object_vars(_post);

            return __post;
        } else if (equal(output, gConsts.getARRAY_N())) {
            __post = Array.array_values(ClassHandling.get_object_vars(_post));

            return __post;
        } else {
            return _post;
        }
    }

    /**
     * get_post_ancestors() - Retrieve ancestors for a post
     *
     * @subpackage Post
     * @since 2.5
     * @param string $field {@internal Missing Description}}
     * @param int|object &$post post ID or post object
     * @return array of ancestor IDs
     */
    public Array<Object> get_post_ancestors(Object post) {
        StdClass postObj = (StdClass) get_post(null, gConsts.getOBJECT(), "raw");

        if (!empty(StdClass.getValue(postObj, "ancestors"))) {
            return postObj.fields.getArrayValue("ancestors");
        }

        return new Array<Object>();
    }

    /**
     * get_post_field() - Retrieve a field based on a post ID.
     *
     * @subpackage Post
     * @since 2.3
     * @param string $field {@internal Missing Description}}
     * @param id $post Post ID
     * @param string $context Optional. How to filter the field
     * @return WP_Error|string Value in post field or WP_Error on failure
     */
    public Object get_post_field(String field, int postId, String context) {
        // post=intval(post);
        Object post = get_post(postId, /* Do not change type */
                gConsts.getOBJECT(), "raw");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post)) {
            return post;
        }

        if (!is_object(post)) {
            return "";
        }

        if (!isset(((StdClass) post).fields.getValue(field))) {
            return "";
        }

        return sanitize_post_field(field, ((StdClass) post).fields.getValue(field), intval(((StdClass) post).fields.getValue("ID")), context);
    }

    /**
     * get_post_mime_type() - Takes a post ID, returns its mime type.
     *
     * @subpackage Post
     * @since 2.0
     * @param int $ID Post ID
     * @return bool|string False on failure or returns the mime type
     */
    public String get_post_mime_type(Object ID) {
        Object post = null;

        /* Do not change type */
        post = get_post(ID, gConsts.getOBJECT(), "raw");

        if (is_object(post)) {
            return strval(((StdClass) post).fields.getValue("post_mime_type"));
        }

        return "";
    }

    /**
     * get_post_status() - Takes a post ID and returns its status{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.0
     * @param int $ID {@internal Missing Description}}
     * @return string|bool post status or false
     */
    public String get_post_status(Object ID) {
        Object post = null;
        post = get_post(ID, gConsts.getOBJECT(), "raw");

        if (is_object(post)) {
            if (equal("attachment", ((StdClass) post).fields.getValue("post_type")) && booleanval(((StdClass) post).fields.getValue("post_parent")) &&
                    !equal(((StdClass) post).fields.getValue("ID"), ((StdClass) post).fields.getValue("post_parent"))) {
                return get_post_status(intval(((StdClass) post).fields.getValue("post_parent")));
            } else {
                return strval(((StdClass) post).fields.getValue("post_status"));
            }
        }

        return "";
    }

    /**
     * get_post_statuses( ) - Retuns the possible user post status values
     * Posts have a limited set of valid status values, this provides the
     * post_status values and descriptions.
     *
     * @subpackage Post
     * @since 2.5
     * @return array
     */
    public Array<Object> get_post_statuses() {
        Array<Object> status = new Array<Object>();
        status = new Array<Object>(
                new ArrayEntry<Object>("draft", getIncluded(L10nPage.class, gVars, gConsts).__("Draft", "default")),
                new ArrayEntry<Object>("pending", getIncluded(L10nPage.class, gVars, gConsts).__("Pending Review", "default")),
                new ArrayEntry<Object>("private", getIncluded(L10nPage.class, gVars, gConsts).__("Private", "default")),
                new ArrayEntry<Object>("publish", getIncluded(L10nPage.class, gVars, gConsts).__("Published", "default")));

        return status;
    }

    /**
     * get_page_statuses( ) - Retuns the possible user page status values
     * Pages have a limited set of valid status values, this provides the
     * post_status values and descriptions.
     *
     * @subpackage Page
     * @since 2.5
     * @return array
     */
    public Array<Object> get_page_statuses() {
        Array<Object> status = new Array<Object>();
        status = new Array<Object>(
                new ArrayEntry<Object>("draft", getIncluded(L10nPage.class, gVars, gConsts).__("Draft", "default")),
                new ArrayEntry<Object>("private", getIncluded(L10nPage.class, gVars, gConsts).__("Private", "default")),
                new ArrayEntry<Object>("publish", getIncluded(L10nPage.class, gVars, gConsts).__("Published", "default")));

        return status;
    }

    public String get_post_type() {
        return get_post_type(false);
    }

    /**
     * get_post_type() - Returns post type{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @uses $wpdb
     * @uses $posts {@internal Missing Description}}
     * @param mixed $post post object or post ID
     * @return mixed post type or false
     */
    public String get_post_type(Object post)/* Do not change type */
     {
        if (equal(false, post)) {
            post = gVars.posts.getValue(0);
        } else if (booleanval(post)) {
            post = get_post(post, gConsts.getOBJECT(), "raw");
        }

        if (is_object(post)) {
            return strval(((StdClass) post).fields.getValue("post_type"));
        }

        return "";
    }

    /**
     * set_post_type() - Set post type{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.5
     * @uses $wpdb
     * @uses $posts {@internal Missing Description}}
     * @param mixed $post_id post ID
     * @param mixed post type
     * @return bool {@internal Missing Description}}
     */
    public int set_post_type(int post_id, String post_type) {
        int _return = 0;
        post_type = strval(sanitize_post_field("post_type", post_type, post_id, "db"));
        _return = gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.posts + " SET post_type = %s WHERE ID = %d", post_type, post_id));

        if (equal("page", post_type)) {
            clean_page_cache(post_id);
        } else {
            clean_post_cache(post_id);
        }

        return _return;
    }

    /**
     * get_posts() - Returns a number of posts{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.2
     * @uses $wpdb
     * @param array $args {@internal Missing Description}}
     * @return array {@internal Missing Description}}
     */
    public Array<StdClass> get_posts(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Integer numberposts = null;
        Integer offset = null;
        Integer category = null;
        Integer post_parent = null;
        String inclusions = null;
        String include = null;
        String exclude = null;
        String meta_key = null;
        String meta_value = null;
        Array<Object> incposts = new Array<Object>();
        Object incpost = null;
        String exclusions = null;
        Array<Object> exposts = new Array<Object>();
        Object expost = null;
        String orderby = null;
        String order = null;
        String query = null;
        Object post_type = null;
        String post_status = null;
        Object post_mime_type = null;
        Array<StdClass> posts;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("numberposts", 5),
                new ArrayEntry<Object>("offset", 0),
                new ArrayEntry<Object>("category", 0),
                new ArrayEntry<Object>("orderby", "post_date"),
                new ArrayEntry<Object>("order", "DESC"),
                new ArrayEntry<Object>("include", ""),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("meta_key", ""),
                new ArrayEntry<Object>("meta_value", ""),
                new ArrayEntry<Object>("post_type", "post"),
                new ArrayEntry<Object>("post_status", "publish"),
                new ArrayEntry<Object>("post_parent", 0));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        numberposts = intval(Array.extractVar(r, "numberposts", numberposts, Array.EXTR_SKIP));
        offset = intval(Array.extractVar(r, "offset", offset, Array.EXTR_SKIP));
        category = intval(Array.extractVar(r, "category", category, Array.EXTR_SKIP));
        post_parent = intval(Array.extractVar(r, "post_parent", post_parent, Array.EXTR_SKIP));
        include = strval(Array.extractVar(r, "include", include, Array.EXTR_SKIP));
        exclude = strval(Array.extractVar(r, "exclude", exclude, Array.EXTR_SKIP));
        meta_key = strval(Array.extractVar(r, "meta_key", meta_key, Array.EXTR_SKIP));
        meta_value = strval(Array.extractVar(r, "meta_value", meta_value, Array.EXTR_SKIP));
        orderby = strval(Array.extractVar(r, "orderby", orderby, Array.EXTR_SKIP));
        order = strval(Array.extractVar(r, "order", order, Array.EXTR_SKIP));
        post_type = Array.extractVar(r, "post_type", post_type, Array.EXTR_SKIP);
        post_status = strval(Array.extractVar(r, "post_status", post_status, Array.EXTR_SKIP));
        post_mime_type = Array.extractVar(r, "post_mime_type", post_mime_type, Array.EXTR_SKIP);
        numberposts = numberposts;
        offset = offset;
        category = category;
        post_parent = post_parent;
        inclusions = "";

        if (!empty(include)) {
            offset = 0;    //ignore offset, category, exclude, meta_key, and meta_value, post_parent if using include
            category = 0;
            exclude = "";
            meta_key = "";
            meta_value = "";
            post_parent = 0;
            incposts = QRegExPerl.preg_split("/[\\s,]+/", include);
            numberposts = Array.count(incposts);  // only the number of posts included

            if (booleanval(Array.count(incposts))) {
                for (Map.Entry javaEntry527 : incposts.entrySet()) {
                    incpost = javaEntry527.getValue();

                    if (empty(inclusions)) {
                        inclusions = gVars.wpdb.prepare(" AND ( ID = %d ", incpost);
                    } else {
                        inclusions = inclusions + gVars.wpdb.prepare(" OR ID = %d ", incpost);
                    }
                }
            }
        }

        if (!empty(inclusions)) {
            inclusions = inclusions + ")";
        }

        exclusions = "";

        if (!empty(exclude)) {
            exposts = QRegExPerl.preg_split("/[\\s,]+/", exclude);

            if (booleanval(Array.count(exposts))) {
                for (Map.Entry javaEntry528 : exposts.entrySet()) {
                    expost = javaEntry528.getValue();

                    if (empty(exclusions)) {
                        exclusions = gVars.wpdb.prepare(" AND ( ID <> %d ", expost);
                    } else {
                        exclusions = exclusions + gVars.wpdb.prepare(" AND ID <> %d ", expost);
                    }
                }
            }
        }

        if (!empty(exclusions)) {
            exclusions = exclusions + ")";
        }

        // orderby
        if (QRegExPerl.preg_match("/.+ +(ASC|DESC)/i", orderby)) {
            order = ""; // orderby has its own order, so we'll use that
        }

        query = "SELECT DISTINCT * FROM " + gVars.wpdb.posts + " ";
        query = query + (empty(category)
            ? ""
            : (", " + gVars.wpdb.term_relationships + ", " + gVars.wpdb.term_taxonomy + "  "));
        query = query + (empty(meta_key)
            ? ""
            : (", " + gVars.wpdb.postmeta + " "));
        query = query + " WHERE 1=1 ";
        query = query + (empty(post_type)
            ? ""
            : gVars.wpdb.prepare("AND post_type = %s ", post_type));
        query = query + (empty(post_status)
            ? ""
            : gVars.wpdb.prepare("AND post_status = %s ", post_status));
        query = query + exclusions + " " + inclusions + " ";
        query = query +
            (empty(category)
            ? ""
            : gVars.wpdb.prepare(
                    "AND (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.term_relationships + ".object_id AND " + gVars.wpdb.term_relationships + ".term_taxonomy_id = " + gVars.wpdb.term_taxonomy +
                    ".term_taxonomy_id AND " + gVars.wpdb.term_taxonomy + ".term_id = %d AND " + gVars.wpdb.term_taxonomy + ".taxonomy = \'category\')",
                    category));
        // expected_slashed ($meta_key, $meta_value) -- Also, this looks really funky, doesn't seem like it works
        query = query + (empty(post_parent)
            ? ""
            : gVars.wpdb.prepare("AND " + gVars.wpdb.posts + ".post_parent = %d ", post_parent));
        query = query +
            (booleanval(intval(empty(meta_key)) | intval(empty(meta_value)))
            ? ""
            : (" AND (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.postmeta + ".post_id AND " + gVars.wpdb.postmeta + ".meta_key = \'" + meta_key + "\' AND " + gVars.wpdb.postmeta +
            ".meta_value = \'" + meta_value + "\' )"));
        query = query + (empty(post_mime_type)
            ? ""
            : wp_post_mime_type_where(post_mime_type));
        query = query + " GROUP BY " + gVars.wpdb.posts + ".ID ORDER BY " + orderby + " " + order;

        if (0 < numberposts) {
            query = query + gVars.wpdb.prepare(" LIMIT %d,%d", offset, numberposts);
        }

        posts = gVars.wpdb.get_results(query);
        update_post_caches(posts);

        return posts;
    }

 //
 // Post meta functions
 //

 /**
  * add_post_meta() - adds metadata for post
  *
  * {@internal Missing Long Description}}
  *
  * @package WordPress
  * @subpackage Post
  * @since 1.5
  * @uses $wpdb
  *
  * @param int $post_id post ID
  * @param string $key {@internal Missing Description}}
  * @param mixed $value {@internal Missing Description}}
  * @param bool $unique whether to check for a value with the same key
  * @return bool {@internal Missing Description}}
  */
    public boolean add_post_meta(int post_id, String meta_key, Object meta_value, boolean unique) {
    	// expected_slashed ($meta_key)
        meta_key = Strings.stripslashes(gVars.webEnv, meta_key);

        if (unique && booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT meta_key FROM " + gVars.wpdb.postmeta + " WHERE meta_key = %s AND post_id = %d", meta_key, post_id)))) {
            return false;
        }

        meta_value = getIncluded(FunctionsPage.class, gVars, gConsts).maybe_serialize(meta_value);
        gVars.wpdb.insert(gVars.wpdb.postmeta, Array.compact(new ArrayEntry("post_id", post_id), new ArrayEntry("meta_key", meta_key), new ArrayEntry("meta_value", meta_value)));
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(post_id, "post_meta");

        return true;
    }

    /**
     * delete_post_meta() - delete post metadata{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @uses $wpdb
     * @param int $post_id post ID
     * @param string $key {@internal Missing Description}}
     * @param mixed $value {@internal Missing Description}}
     * @return bool {@internal Missing Description}}
     */
    public boolean delete_post_meta(int post_id, String key, String value) {
        Object meta_id = null;
        
        post_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(post_id);
        
    	// expected_slashed ($key, $value)
        key = Strings.stripslashes(gVars.webEnv, key);
        value = Strings.stripslashes(gVars.webEnv, value);

        if (empty(value)) {
            meta_id = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT meta_id FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d AND meta_key = %s", post_id, key));
        } else {
            meta_id = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT meta_id FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d AND meta_key = %s AND meta_value = %s", post_id, key, value));
        }

        if (!booleanval(meta_id)) {
            return false;
        }

        if (empty(value)) {
            gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d AND meta_key = %s", post_id, key));
        } else {
            gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d AND meta_key = %s AND meta_value = %s", post_id, key, value));
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(post_id, "post_meta");

        return true;
    }

    /**
     * get_post_meta() - Get a post meta field{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @uses $wpdb
     * @param int $post_id post ID
     * @param string $key The meta key to retrieve
     * @param bool $single Whether to return a single value
     * @return mixed {@internal Missing Description}}
     */
    public Object get_post_meta(int post_id, String key, boolean single) {
        Array<Object> meta_cache = new Array<Object>();

        // post_id.value = intval(post_id);
        meta_cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(post_id, "post_meta");

        // Added by Numiton
        if (!isset(meta_cache)) {
            meta_cache = new Array<Object>();
        }

        if (isset(meta_cache.getValue(key))) {
            if (single) {
                return getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(meta_cache.getArrayValue(key).getValue(0));
            } else {
                return getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(meta_cache.getValue(key));
            }
        }

        if (!booleanval(meta_cache)) {
            update_postmeta_cache(post_id);
            meta_cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(post_id, "post_meta");

            // Added by Numiton
            if (!isset(meta_cache)) {
                meta_cache = new Array<Object>();
            }
        }

        if (single) {
            if (isset(meta_cache.getArrayValue(key).getValue(0))) {
                return getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(meta_cache.getArrayValue(key).getValue(0));
            } else {
                return null;
            }
        } else {
            return getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(meta_cache.getValue(key));
        }
    }

    /**
     * update_post_meta() - Update a post meta field{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @uses $wpdb
     * @param int $post_id post ID
     * @param string $key {@internal Missing Description}}
     * @param mixed $value {@internal Missing Description}}
     * @param mixed $prev_value previous value (for differentiating between meta
     * fields with the same key and post ID)
     * @return bool {@internal Missing Description}}
     */
    public boolean update_post_meta(int post_id, String meta_key, Object meta_value, Object prev_value) {
        Array<Object> data = new Array<Object>();
        Array<Object> where = new Array<Object>();
        
        meta_value = getIncluded(FunctionsPage.class, gVars, gConsts).maybe_serialize(meta_value);
        prev_value = strval(getIncluded(FunctionsPage.class, gVars, gConsts).maybe_serialize(prev_value));
        
    	// expected_slashed ($meta_key)
        meta_key = Strings.stripslashes(gVars.webEnv, meta_key);

        if (!booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT meta_key FROM " + gVars.wpdb.postmeta + " WHERE meta_key = %s AND post_id = %d", meta_key, post_id)))) {
            return false;
        }

        data = Array.compact(new ArrayEntry("meta_value", meta_value));
        where = Array.compact(new ArrayEntry("meta_key", meta_key), new ArrayEntry("post_id", post_id));

        if (!empty(prev_value)) {
            where.putValue("meta_value", prev_value);
        }

        gVars.wpdb.update(gVars.wpdb.postmeta, data, where);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(post_id, "post_meta");

        return true;
    }

    /**
     * delete_post_meta_by_key() - Delete everything from post meta matching
     * $post_meta_key
     *
     * @subpackage Post
     * @since 2.3
     * @uses $wpdb
     * @param string $post_meta_key What to search for when deleting
     * @return bool Whether the post meta key was deleted from the database
     */
    public boolean delete_post_meta_by_key(Object post_meta_key) {
        if (booleanval(gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.postmeta + " WHERE meta_key = %s", post_meta_key)))) {
    		/** @todo Get post_ids and delete cache */
    		// wp_cache_delete($post_id, 'post_meta');
            return true;
        }

        return false;
    }

    /**
     * get_post_custom() - Retrieve post custom fields{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.2
     * @uses $id
     * @uses $wpdb
     * @param int $post_id post ID
     * @return array {@internal Missing Description}}
     */
    public Array<Object> get_post_custom(int post_id) {
        if (!booleanval(post_id)) {
            post_id = intval(gVars.id);
        }

        // post_id = intval(post_id);
        if (!booleanval(getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(post_id, "post_meta"))) {
            update_postmeta_cache(post_id);
        }

        return (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(post_id, "post_meta");
    }

    /**
     * get_post_custom_keys() - Retrieve post custom field names
     *
     * @subpackage Post
     * @since 1.2
     * @param int $post_id post ID
     * @return array|null Either array of the keys, or null if keys would not be
     * retrieved
     */
    public Array<Object> get_post_custom_keys(int post_id) {
        Object custom = null;

        /* Do not change type */
        Array<Object> keys = new Array<Object>();
        custom = get_post_custom(post_id);

        if (!is_array(custom)) {
            return null;
        }

        if (booleanval(keys = Array.array_keys((Array) custom))) {
            return keys;
        }

        return new Array<Object>();
    }

    public Object get_post_custom_values(String key, int post_id) {
        Array<Object> custom = get_post_custom(post_id);

        return custom.getValue(key);
    }

    public Object sanitize_post(Object post, String context)/* Do not change type */
     {
        String field = null;

        if (equal("raw", context)) {
            return post;
        }

        if (is_object(post)) {
            StdClass postObj = (StdClass) post;

            for (Map.Entry javaEntry529 : Array.array_keys(ClassHandling.get_object_vars(post)).entrySet()) {
                field = strval(javaEntry529.getValue());
                postObj.fields.putValue(field, sanitize_post_field(strval(field), StdClass.getValue(postObj, field), intval(StdClass.getValue(postObj, "ID")), context));
            }
        } else {
            Array postArray = (Array) post;

            for (Map.Entry javaEntry530 : (Set<Map.Entry>) Array.array_keys(postArray).entrySet()) {
                field = strval(javaEntry530.getValue());
                postArray.putValue(field, sanitize_post_field(strval(field), postArray.getValue(field), intval(postArray.getValue("ID")), context));
            }
        }

        return post;
    }

    /**
     * sanitize_post_field() - Sanitize post field based on context{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.3
     * @param string $field The Post Object field name
     * @param string $value The Post Object value WRONG it can also be array
     * @param int $postid Post ID
     * @param string $context How to sanitize post fields
     * @return string Sanitized value
     */
    public Object sanitize_post_field(String field, Object value, int post_id, String context) {
        Array<Object> int_fields = new Array<Object>();
        boolean prefixed = false;
        String field_no_prefix = null;
        Array<Object> format_to_edit = new Array<Object>();
        int_fields = new Array<Object>(new ArrayEntry<Object>("ID"), new ArrayEntry<Object>("post_parent"), new ArrayEntry<Object>("menu_order"));

        if (Array.in_array(field, int_fields)) {
            value = value;
        }

        if (equal("raw", context)) {
            return value;
        }

        prefixed = false;

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(field, "post_"))) {
            prefixed = true;
            field_no_prefix = Strings.str_replace("post_", "", field);
        }

        if (equal("edit", context)) {
            format_to_edit = new Array<Object>(
                    new ArrayEntry<Object>("post_content"),
                    new ArrayEntry<Object>("post_excerpt"),
                    new ArrayEntry<Object>("post_title"),
                    new ArrayEntry<Object>("post_password"));

            if (prefixed) {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_" + field, value, post_id);
    			// Old school
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(field_no_prefix + "_edit_pre", value, post_id);
            } else {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_post_" + field, value, post_id);
            }

            if (Array.in_array(field, format_to_edit)) {
                if (equal("post_content", field)) {
                    value = getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(value), getIncluded(General_templatePage.class, gVars, gConsts).user_can_richedit());
                } else {
                    value = getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(value), false);
                }
            } else {
                if (value instanceof Array) {
                    value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escapeArray((Array) value);
                } else {
                    value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(value));
                }
            }
        } else if (equal("db", context)) {
            if (prefixed) {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_" + field, value);
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(field_no_prefix + "_save_pre", value);
            } else {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_post_" + field, value);
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(field + "_pre", value);
            }
        } else {
    		// Use display filters by default.
            if (prefixed) {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(field, value, post_id, context);
            } else {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_" + field, value, post_id, context);
            }
        }

        if (equal("attribute", context)) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(value));
        } else if (equal("js", context)) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(strval(value));
        }

        return value;
    }

    /**
     * wp_count_posts() - Count number of posts with a given type{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.5
     * @param string $type Post type
     * @return array Number of posts for each status
     */
    public StdClass wp_count_posts(String type, String perm) {
        WP_User user = null;
        String cache_key = null;
        String query = null;
        StdClass count;
        Array<Object> stats = new Array<Object>();
        Array<Object> row = new Array<Object>();
        Object row_num = null;
        user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
        cache_key = type;
        query = "SELECT post_status, COUNT( * ) AS num_posts FROM " + gVars.wpdb.posts + " WHERE post_type = %s";

        if (equal("readable", perm) && getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("read_private_" + type + "s")) {
                cache_key = cache_key + "_" + perm + "_" + user.getID();
                query = query + " AND (post_status != \'private\' OR ( post_author = \'" + user.getID() + "\' AND post_status = \'private\' ))";
            }
        }

        query = query + " GROUP BY post_status";
        count = (StdClass) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(cache_key, "counts");

        if (!strictEqual(null, count)) {
            return count;
        }

        Array<?> countArr = gVars.wpdb.get_results(gVars.wpdb.prepare(query, type), gConsts.getARRAY_A());
        stats = new Array<Object>();

        for (Map.Entry javaEntry531 : countArr.entrySet()) {
            row_num = javaEntry531.getKey();
            row = (Array<Object>) javaEntry531.getValue();
            stats.putValue(row.getValue("post_status"), row.getValue("num_posts"));
        }

        StdClass statsObj = Array.toStdClass(stats);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set(cache_key, statsObj, "counts", 0);

        return statsObj;
    }

    /**
     * wp_count_attachments() - Count number of attachments{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.5
     * @param string|array $post_mime_type Array or comma-separated list of MIME
     * patterns
     * @return array Number of posts for each post_mime_type
     */
    public StdClass wp_count_attachments(Object mime_type) {
        String and = null;
        Array<Object> count = new Array<Object>();
        Array<Object> stats = new Array<Object>();
        Array<Object> row = new Array<Object>();
        and = wp_post_mime_type_where(mime_type);
        count = gVars.wpdb.get_results(
                "SELECT post_mime_type, COUNT( * ) AS num_posts FROM " + gVars.wpdb.posts + " WHERE post_type = \'attachment\' " + and + " GROUP BY post_mime_type",
                gConsts.getARRAY_A());
        stats = new Array<Object>();

        for (Map.Entry javaEntry532 : new Array<Object>(count).entrySet()) {
            row = (Array<Object>) javaEntry532.getValue();
            stats.putValue(row.getValue("post_mime_type"), row.getValue("num_posts"));
        }

        return Array.toStdClass(stats);
    }

    /**
     * wp_match_mime_type() - Check a MIME-Type against a list{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.5
     * @param string|array $wildcard_mime_types e.g. audio/mpeg or image (same
     * as image/*) or flash (same as *flash*)
     * @param string|array $real_mime_types post_mime_type values
     * @return array array(wildcard=>array(real types))
     */
    public Array<Object> wp_match_mime_types(Object wildcard_mime_types, Object real_mime_types) {
        Array<Object> matches = new Array<Object>();
        String wild = null;
        String type;
        Array<Object> patternses = new Array<Object>();
        Object pattern = null;
        String real = null;
        Array<Object> patterns = null;
        matches = new Array<Object>();

        if (is_string(wildcard_mime_types)) {
            wildcard_mime_types = Array.array_map(new Callback("trim", Strings.class), Strings.explode(",", strval(wildcard_mime_types)));
        }

        if (is_string(real_mime_types)) {
            real_mime_types = Array.array_map(new Callback("trim", Strings.class), Strings.explode(",", strval(real_mime_types)));
        }

        wild = "[-._a-z0-9]*";

        for (Map.Entry javaEntry533 : new Array<Object>(wildcard_mime_types).entrySet()) {
            type = strval(javaEntry533.getValue());
            type = Strings.str_replace("*", wild, type);
            patternses.getArrayValue(1).putValue(type, "^" + type + "$");

            if (strictEqual(BOOLEAN_FALSE, Strings.strpos(type, "/"))) {
                patternses.getArrayValue(2).putValue(type, "^" + type + "/");
                patternses.getArrayValue(3).putValue(type, type);
            }
        }

        Array.asort(patternses);

        for (Map.Entry javaEntry534 : patternses.entrySet()) {
            patterns = (Array<Object>) javaEntry534.getValue();

            for (Map.Entry javaEntry535 : patterns.entrySet()) {
                type = strval(javaEntry535.getKey());
                pattern = javaEntry535.getValue();

                for (Map.Entry javaEntry536 : new Array<Object>(real_mime_types).entrySet()) {
                    real = strval(javaEntry536.getValue());

                    if (QRegExPerl.preg_match("#" + strval(pattern) + "#", real) && (empty(matches.getValue(type)) || strictEqual(null, Array.array_search(real, matches.getArrayValue(type))))) {
                        matches.getArrayValue(type).putValue(real);
                    }
                }
            }
        }

        return matches;
    }

    /**
     * wp_get_post_mime_type_where() - Convert MIME types into SQL
     *
     * @subpackage Post
     * @since 2.5
     * @param string|array $mime_types MIME types
     * @return string SQL AND clause
     */
    public String wp_post_mime_type_where(Object post_mime_types) {
        String where = null;
        Array<Object> wildcards = new Array<Object>();
        String mime_type;
        int slashpos = 0;
        String mime_group;
        String mime_subgroup = null;
        String mime_pattern = null;
        Array<String> wheres = new Array<String>();
        where = "";
        wildcards = new Array<Object>(new ArrayEntry<Object>(""), new ArrayEntry<Object>("%"), new ArrayEntry<Object>("%/%"));

        if (is_string(post_mime_types)) {
            post_mime_types = Array.array_map(new Callback("trim", Strings.class), Strings.explode(",", strval(post_mime_types)));
        }

        for (Map.Entry javaEntry537 : new Array<Object>(post_mime_types).entrySet()) {
            mime_type = strval(javaEntry537.getValue());
            mime_type = QRegExPerl.preg_replace("/\\s/", "", mime_type);
            slashpos = Strings.strpos(mime_type, "/");

            if (!strictEqual(BOOLEAN_FALSE, slashpos)) {
                mime_group = QRegExPerl.preg_replace("/[^-*.a-zA-Z0-9]/", "", Strings.substr(mime_type, 0, slashpos));
                mime_subgroup = QRegExPerl.preg_replace("/[^-*.a-zA-Z0-9]/", "", Strings.substr(mime_type, slashpos + 1));

                if (empty(mime_subgroup)) {
                    mime_subgroup = "*";
                } else {
                    mime_subgroup = Strings.str_replace("/", "", mime_subgroup);
                }

                mime_pattern = mime_group + "/" + mime_subgroup;
            } else {
                mime_pattern = QRegExPerl.preg_replace("/[^-*.a-zA-Z0-9]/", "", mime_type);

                if (strictEqual(BOOLEAN_FALSE, Strings.strpos(mime_pattern, "*"))) {
                    mime_pattern = mime_pattern + "/*";
                }
            }

            mime_pattern = QRegExPerl.preg_replace("/\\*+/", "%", mime_pattern);

            if (Array.in_array(mime_type, wildcards)) {
                return "";
            }

            if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(mime_pattern, "%"))) {
                wheres.putValue("post_mime_type LIKE \'" + mime_pattern + "\'");
            } else {
                wheres.putValue("post_mime_type = \'" + mime_pattern + "\'");
            }
        }

        if (!empty(wheres)) {
            where = " AND (" + Strings.join(" OR ", wheres) + ") ";
        }

        return where;
    }

    /**
     * wp_delete_post() - Deletes a Post{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @param int $postid post ID
     * @return mixed {@internal Missing Description}}
     */
    public StdClass wp_delete_post(int postid) {
        StdClass post;
        Array<Object> parent_data = new Array<Object>();
        Array<Object> parent_where = new Array<Object>();
        String children_query = null;
        Array<Object> children = new Array<Object>();
        StdClass child = null;

        if (!booleanval(post = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.posts + " WHERE ID = %d", postid)))) {
            return post;
        }

        if (equal("attachment", StdClass.getValue(post, "post_type"))) {
            return wp_delete_attachment(postid);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_post", postid);
        
    	/** @todo delete for pluggable post taxonomies too */
        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_object_term_relationships(postid, new Array<Object>(new ArrayEntry<Object>("category"), new ArrayEntry<Object>("post_tag")));
        
        parent_data = new Array<Object>(new ArrayEntry<Object>("post_parent", StdClass.getValue(post, "post_parent")));
        parent_where = new Array<Object>(new ArrayEntry<Object>("post_parent", postid));

        if (equal("page", StdClass.getValue(post, "post_type"))) {
    	 	// if the page is defined in option page_on_front or post_for_posts,
    		// adjust the corresponding options
            if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"), postid)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).update_option("show_on_front", "posts");
                getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("page_on_front");
            }

            if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts"), postid)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("page_for_posts");
            }

    		// Point children of this page to its parent, also clean the cache of affected children
            children_query = gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.posts + " WHERE post_parent = %d AND post_type=\'page\'", postid);
            children = gVars.wpdb.get_results(children_query);
            
            gVars.wpdb.update(gVars.wpdb.posts, parent_data, Array.arrayAppend(parent_where, new Array<Object>(new ArrayEntry<Object>("post_type", "page"))));
        }

        // Point all attachments to this post up one level
        gVars.wpdb.update(gVars.wpdb.posts, parent_data, Array.arrayAppend(parent_where, new Array<Object>(new ArrayEntry<Object>("post_type", "attachment"))));
        
        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.posts + " WHERE ID = %d", postid));
        
        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = %d", postid));
        
        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d", postid));

        if (equal("page", StdClass.getValue(post, "post_type"))) {
            clean_page_cache(postid);

            for (Map.Entry javaEntry538 : new Array<Object>(children).entrySet()) {
                child = (StdClass) javaEntry538.getValue();
                clean_page_cache(intval(StdClass.getValue(child, "ID")));
            }

            gVars.wp_rewrite.flush_rules();
        } else {
            clean_post_cache(postid);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("deleted_post", postid);

        return post;
    }

    /**
     * wp_get_post_categories() - Retrieve the list of categories for a post
     * Compatibility layer for themes and plugins. Also an easy layer of
     * abstraction away from the complexity of the taxonomy layer.
     *
     * @subpackage Post
     * @since 2.1
     * @uses wp_get_object_terms() Retrieves the categories. Args details can be
     * found here
     * @param int $post_id Optional. The Post ID
     * @param array $args Optional. Overwrite the defaults
     * @return array {@internal Missing Description}}
     */
    public Object wp_get_post_categories(int post_id, Array<Object> args) {
        Array<Object> defaults = new Array<Object>();
        Object cats = null;

        // post_id.value = intval(post_id);
        defaults = new Array<Object>(new ArrayEntry<Object>("fields", "ids"));
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        cats = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(post_id, "category", args);

        return cats;
    }

    /**
     * wp_get_post_tags() - Retrieve the post tags
     *
     * @subpackage Post
     * @since 2.3
     * @uses wp_get_object_terms() Gets the tags for returning. Args can be
     * found here
     * @param int $post_id Optional. The Post ID
     * @param array $args Optional. Overwrite the defaults
     * @return mixed The tags the post has currently
     */
    public Object wp_get_post_tags(int post_id, Array<Object> args) {
        Array<Object> defaults = new Array<Object>();
        Object tags = null;

        // post_id.value = intval(post_id);
        defaults = new Array<Object>(new ArrayEntry<Object>("fields", "all"));
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        tags = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(post_id, "post_tag", args);

        return tags;
    }

    /**
     * wp_get_recent_posts() - Get the $num most recent posts{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @param int $num number of posts to get
     * @return array {@internal Missing Description}}
     */
    public Array<Object> wp_get_recent_posts(int num) {
        String limit = null;
        String sql = null;
        Array<Object> result = new Array<Object>();
        
    	// Set the limit clause, if we got a limit
        num = intval(num);

        if (booleanval(num)) {
            limit = "LIMIT " + strval(num);
        }

        sql = "SELECT * FROM " + gVars.wpdb.posts + " WHERE post_type = \'post\' ORDER BY post_date DESC " + limit;
        result = gVars.wpdb.get_results(sql, gConsts.getARRAY_A());

        return booleanval(result)
        ? result
        : new Array<Object>();
    }

    /**
     * wp_get_single_post() - Get one post{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @uses $wpdb
     * @param int $postid post ID
     * @param string $mode How to return result, either OBJECT, ARRAY_N, or
     * ARRAY_A
     * @return object|array Post object or array holding post contents and
     * information
     */
    public Object wp_get_single_post(int postid, String mode) {
        Object post;

        // postid.value = intval(postid);
        post = get_post(postid, mode, "raw");

    	// Set categories and tags
        if (equal(mode, gConsts.getOBJECT())) {
            ((StdClass) post).fields.putValue("post_category", wp_get_post_categories(postid, new Array<Object>()));
            ((StdClass) post).fields.putValue("tags_input", wp_get_post_tags(postid, new Array<Object>(new ArrayEntry<Object>("fields", "names"))));
        } else {
            ((Array) post).putValue("post_category", wp_get_post_categories(postid, new Array<Object>()));
            ((Array) post).putValue("tags_input", wp_get_post_tags(postid, new Array<Object>(new ArrayEntry<Object>("fields", "names"))));
        }

        return post;
    }

    /**
     * wp_insert_post() - Insert a post{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @uses $wpdb
     * @uses $wp_rewrite
     * @uses $user_ID
     * @uses $allowedtags
     * @param array $postarr post contents
     * @return int post ID or 0 on error
     */
    public int wp_insert_post(Object postarr) {
        Array<Object> defaults = new Array<Object>();
        Boolean update = null;
        Integer ID = null;
        String previous_status = null;
        Object post_content = null;
        Object post_content_filtered = null;
        String post_title = null;
        Object post_excerpt = null;
        Object post_category = null;

        /* Do not change type */
        Integer post_author = null;
        String post_status = null;
        String post_type = null;
        Integer post_ID = null;
        Object guid = null;
        String post_name = null;
        String post_date = null;
        String post_date_gmt = null;
        String post_modified = null;
        String post_modified_gmt = null;
        String now = null;
        String comment_status = null;
        Object ping_status = null;
        String to_ping = null;
        String pinged = null;
        Integer post_parent = null;
        Integer menu_order = null;
        String post_password = null;
        Object post_name_check = null;
        Integer suffix = null;
        String alt_post_name = null;
        Array<Object> data = new Array<Object>();
        Array<Object> where = new Array<Object>();
        String post_mime_type = null;
        Object tags_input = null;
        Object current_guid = null;
        StdClass post = null;
        Object page_template = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("post_status", "draft"),
                new ArrayEntry<Object>("post_type", "post"),
                new ArrayEntry<Object>("post_author", gVars.user_ID),
                new ArrayEntry<Object>("ping_status", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status")),
                new ArrayEntry<Object>("post_parent", 0),
                new ArrayEntry<Object>("menu_order", 0),
                new ArrayEntry<Object>("to_ping", ""),
                new ArrayEntry<Object>("pinged", ""),
                new ArrayEntry<Object>("post_password", ""),
                new ArrayEntry<Object>("guid", ""),
                new ArrayEntry<Object>("post_content_filtered", ""),
                new ArrayEntry<Object>("post_excerpt", ""));
        
        postarr = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(postarr, defaults);
        Array postarrArray = (Array<Object>) sanitize_post(postarr, "db");

    	// export array as variables
        ID = intval(Array.extractVar(postarrArray, "ID", ID, Array.EXTR_SKIP));
        post_content = Array.extractVar(postarrArray, "post_content", post_content, Array.EXTR_SKIP);
        post_content_filtered = Array.extractVar(postarrArray, "post_content_filtered", post_content_filtered, Array.EXTR_SKIP);
        post_title = strval(Array.extractVar(postarrArray, "post_title", post_title, Array.EXTR_SKIP));
        post_excerpt = Array.extractVar(postarrArray, "post_excerpt", post_excerpt, Array.EXTR_SKIP);
        post_category = Array.extractVar(postarrArray, "post_category", post_category, Array.EXTR_SKIP);
        post_author = intval(Array.extractVar(postarrArray, "post_author", post_author, Array.EXTR_SKIP));
        post_status = strval(Array.extractVar(postarrArray, "post_status", post_status, Array.EXTR_SKIP));
        post_type = strval(Array.extractVar(postarrArray, "post_type", post_type, Array.EXTR_SKIP));
        post_ID = intval(Array.extractVar(postarrArray, "post_ID", post_ID, Array.EXTR_SKIP));
        guid = Array.extractVar(postarrArray, "guid", guid, Array.EXTR_SKIP);
        post_name = strval(Array.extractVar(postarrArray, "post_name", post_name, Array.EXTR_SKIP));
        post_date = strval(Array.extractVar(postarrArray, "post_date", post_date, Array.EXTR_SKIP));
        post_date_gmt = strval(Array.extractVar(postarrArray, "post_date_gmt", post_date_gmt, Array.EXTR_SKIP));
        comment_status = strval(Array.extractVar(postarrArray, "comment_status", comment_status, Array.EXTR_SKIP));
        ping_status = Array.extractVar(postarrArray, "ping_status", ping_status, Array.EXTR_SKIP);
        to_ping = strval(Array.extractVar(postarrArray, "to_ping", to_ping, Array.EXTR_SKIP));
        pinged = strval(Array.extractVar(postarrArray, "pinged", pinged, Array.EXTR_SKIP));
        post_parent = intval(Array.extractVar(postarrArray, "post_parent", post_parent, Array.EXTR_SKIP));
        menu_order = intval(Array.extractVar(postarrArray, "menu_order", menu_order, Array.EXTR_SKIP));
        post_password = strval(Array.extractVar(postarrArray, "post_password", post_password, Array.EXTR_SKIP));
        post_mime_type = strval(Array.extractVar(postarrArray, "post_mime_type", post_mime_type, Array.EXTR_SKIP));
        tags_input = Array.extractVar(postarrArray, "tags_input", tags_input, Array.EXTR_SKIP);
        page_template = Array.extractVar(postarrArray, "page_template", page_template, Array.EXTR_SKIP);
        
    	// Are we updating or creating?
        update = false;

        if (!empty(ID)) {
            update = true;
            previous_status = strval(get_post_field("post_status", ID, "display"));
        } else {
            previous_status = "new";
        }

        if (equal("", post_content) && equal("", post_title) && equal("", post_excerpt)) {
            return 0;
        }

    	// Make sure we set a valid category
        if (equal(0, Array.count(post_category)) || !is_array(post_category)) {
            post_category = new Array<Object>(new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category")));
        }

        if (empty(post_author)) {
            post_author = gVars.user_ID;
        }

        if (empty(post_status)) {
            post_status = "draft";
        }

        if (empty(post_type)) {
            post_type = "post";
        }

    	// Get the post ID and GUID
        if (update) {
            post_ID = ID;
            guid = get_post_field("guid", post_ID, "display");
        }

    	// Create a valid post name.  Drafts are allowed to have an empty
    	// post name.
        if (empty(post_name)) {
            if (!equal("draft", post_status)) {
                post_name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(post_title, "");
            }
        } else {
            post_name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(post_name, "");
        }

    	// If the post date is empty (due to having been new or a draft) and status is not 'draft', set date to now
        if (empty(post_date)) {
            if (!Array.in_array(post_status, new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
                post_date = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
            } else {
                post_date = "0000-00-00 00:00:00";
            }
        }

        if (empty(post_date_gmt)) {
            if (!Array.in_array(post_status, new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
                post_date_gmt = getIncluded(FormattingPage.class, gVars, gConsts).get_gmt_from_date(post_date);
            } else {
                post_date_gmt = "0000-00-00 00:00:00";
            }
        }

        if (update || equal("0000-00-00 00:00:00", post_date)) {
            post_modified = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
            post_modified_gmt = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
        } else {
            post_modified = post_date;
            post_modified_gmt = post_date_gmt;
        }

        if (equal("publish", post_status)) {
            now = DateTime.gmdate("Y-m-d H:i:59");

            if (intval(getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("U", post_date_gmt, true)) > intval(getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("U", now, true))) {
                post_status = "future";
            }
        }

        if (empty(comment_status)) {
            if (update) {
                comment_status = "closed";
            } else {
                comment_status = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status"));
            }
        }

        if (empty(ping_status)) {
            ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");
        }

        if (isset(to_ping)) {
            to_ping = QRegExPerl.preg_replace("|\\s+|", "\n", to_ping);
        } else {
            to_ping = "";
        }

        if (!isset(pinged)) {
            pinged = "";
        }

        if (isset(post_parent)) {
            post_parent = post_parent;
        } else {
            post_parent = 0;
        }

        if (isset(menu_order)) {
            menu_order = menu_order;
        } else {
            menu_order = 0;
        }

        if (!isset(post_password)) {
            post_password = "";
        }

        if (!equal("draft", post_status)) {
            post_name_check = gVars.wpdb.get_var(
                    gVars.wpdb.prepare(
                        "SELECT post_name FROM " + gVars.wpdb.posts + " WHERE post_name = %s AND post_type = %s AND ID != %d AND post_parent = %d LIMIT 1",
                        post_name,
                        post_type,
                        post_ID,
                        post_parent));

            if (booleanval(post_name_check) || Array.in_array(post_name, gVars.wp_rewrite.feeds)) {
                suffix = 2;

                do {
                    alt_post_name = Strings.substr(post_name, 0, 200 - Strings.strlen(strval(suffix)) + 1) + "-" + strval(suffix);
    				// expected_slashed ($alt_post_name, $post_name, $post_type)
                    post_name_check = gVars.wpdb.get_var(
                                gVars.wpdb.prepare(
                                        "SELECT post_name FROM " + gVars.wpdb.posts + " WHERE post_name = \'" + alt_post_name + "\' AND post_type = \'" + post_type +
                                        "\' AND ID != %d AND post_parent = %d LIMIT 1",
                                        post_ID,
                                        post_parent));
                    suffix++;
                } while (booleanval(post_name_check));

                post_name = alt_post_name;
            }
        }

    	// expected_slashed (everything!)
        data = Array.compact(
                new ArrayEntry("post_author", post_author),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_content_filtered", post_content_filtered),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_type", post_type),
                new ArrayEntry("comment_status", comment_status),
                new ArrayEntry("ping_status", ping_status),
                new ArrayEntry("post_password", post_password),
                new ArrayEntry("post_name", post_name),
                new ArrayEntry("to_ping", to_ping),
                new ArrayEntry("pinged", pinged),
                new ArrayEntry("post_modified", post_modified),
                new ArrayEntry("post_modified_gmt", post_modified_gmt),
                new ArrayEntry("post_parent", post_parent),
                new ArrayEntry("menu_order", menu_order),
                new ArrayEntry("guid", guid));
        data = (Array<Object>) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(data);
        where = new Array<Object>(new ArrayEntry<Object>("ID", post_ID));

        if (update) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("pre_post_update", post_ID);
            gVars.wpdb.update(gVars.wpdb.posts, data, where);
        } else {
            data.putValue("post_mime_type", Strings.stripslashes(gVars.webEnv, post_mime_type)); // This isn't in the update
            gVars.wpdb.insert(gVars.wpdb.posts, data);
            post_ID = gVars.wpdb.insert_id;
            
    		// use the newly generated $post_ID
            where = new Array<Object>(new ArrayEntry<Object>("ID", post_ID));
        }

        if (empty(post_name) && !equal("draft", post_status)) {
            post_name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(post_title, strval(post_ID));
            gVars.wpdb.update(gVars.wpdb.posts, Array.compact(new ArrayEntry("post_name", post_name)), where);
        }

        wp_set_post_categories(post_ID, post_category);
        wp_set_post_tags(post_ID, tags_input, false);
        current_guid = get_post_field("guid", post_ID, "display");

        if (equal("page", post_type)) {
            clean_page_cache(post_ID);
        } else {
            clean_post_cache(post_ID);
        }

    	// Set GUID
        if (!update && equal("", current_guid)) {
            gVars.wpdb.update(gVars.wpdb.posts, new Array<Object>(new ArrayEntry<Object>("guid", getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(post_ID, false))), where);
        }

        post = (StdClass) get_post(post_ID, gConsts.getOBJECT(), "raw");

        if (!empty(page_template)) {
            post.fields.putValue("page_template", page_template);
        }

        wp_transition_post_status(post_status, previous_status, post);

        if (update) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_post", post_ID, post);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("save_post", post_ID, post);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_insert_post", post_ID, post);

        return post_ID;
    }

    /**
     * wp_update_post() - Update a post{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @uses $wpdb
     * @param array $postarr post data
     * @return int {@internal Missing Description}}
     */
    public int wp_update_post(Object postarrObj)/* Do not change type */
     {
        Array<Object> post = new Array<Object>();
        Object post_cats = null;
        boolean clear_date = false;

        /* Modified by Numiton */
        Array postarr;

        if (is_object(postarrObj)) {
            postarr = ClassHandling.get_object_vars(postarrObj);
        } else {
            postarr = (Array) postarrObj;
        }

    	// First, get all of the original fields
        post = (Array<Object>) wp_get_single_post(intval(postarr.getValue("ID")), gConsts.getARRAY_A());
        
    	// Escape data pulled from DB.
        post = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(post);

    	// Passed post category list overwrites existing category list if not empty.
        if (isset(postarr.getValue("post_category")) && is_array(postarr.getValue("post_category")) && !equal(0, Array.count(postarr.getValue("post_category")))) {
            post_cats = postarr.getValue("post_category");
        } else {
            post_cats = post.getValue("post_category");
        }

    	// Drafts shouldn't be assigned a date unless explicitly done so by the user
        if (Array.in_array(post.getValue("post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending"))) && empty(postarr.getValue("edit_date")) &&
                empty(postarr.getValue("post_date")) && equal("0000-00-00 00:00:00", post.getValue("post_date"))) {
            clear_date = true;
        } else {
            clear_date = false;
        }

    	// Merge old and new fields with new fields overwriting old ones.
        postarr = Array.array_merge(post, postarr);
        postarr.putValue("post_category", post_cats);

        if (clear_date) {
            postarr.putValue("post_date", "");
            postarr.putValue("post_date_gmt", "");
        }

        if (equal(postarr.getValue("post_type"), "attachment")) {
            return wp_insert_attachment(postarr, "", 0);
        }

        return wp_insert_post(postarr);
    }

    /**
     * wp_publish_post() - Mark a post as "published"{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @uses $wpdb
     * @param int $post_id Post ID
     * @return int|null {@internal Missing Description}}
     */
    public void wp_publish_post(int post_id) {
        StdClass post = null;
        String old_status = null;
        Array<Object> terms = null;
        Object taxonomy = null;
        post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw");

        if (empty(post)) {
            return;
        }

        if (equal("publish", StdClass.getValue(post, "post_status"))) {
            return;
        }

        gVars.wpdb.update(gVars.wpdb.posts, new Array<Object>(new ArrayEntry<Object>("post_status", "publish")), new Array<Object>(new ArrayEntry<Object>("ID", post_id)));
        old_status = strval(StdClass.getValue(post, "post_status"));
        post.fields.putValue("post_status", "publish");
        wp_transition_post_status("publish", old_status, post);

    	// Update counts for the post's terms.
        for (Map.Entry javaEntry539 : getIncluded(TaxonomyPage.class, gVars, gConsts).get_object_taxonomies("post").entrySet()) {
            taxonomy = javaEntry539.getValue();
            terms = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(post_id, taxonomy, "fields=tt_ids");
            getIncluded(TaxonomyPage.class, gVars, gConsts).wp_update_term_count(terms, strval(taxonomy), false);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_post", post_id, post);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("save_post", post_id, post);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_insert_post", post_id, post);
    }

    public void check_and_publish_future_post(String post_id) {
        check_and_publish_future_post(intval(post_id));
    }

    /**
     * check_and_publish_future_post() - check to make sure post has correct status before
     * passing it on to be published. Invoked by cron 'publish_future_post' event
     * This safeguard prevents cron from publishing drafts, etc.
     *
     * {@internal Missing Long Description}}
     *
     * @package WordPress
     * @subpackage Post
     * @since 2.5
     * @uses $wpdb
     *
     * @param int $post_id Post ID
     * @return int|null {@internal Missing Description}}
     */
    public void check_and_publish_future_post(int post_id) {
        StdClass post = null;
        post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw");

        if (empty(post)) {
            return;
        }

        if (!equal("future", StdClass.getValue(post, "post_status"))) {
            return;
        }

        wp_publish_post(post_id);
    }

    public boolean wp_add_post_tags(int post_id, Object tags) {
        return wp_set_post_tags(post_id, tags, true);
    }

    public boolean wp_set_post_tags(int post_id, Object tags, boolean append)/* Do not change type */
     {
    	/* $append - true = don't delete existing tags, just add on, false = replace the tags with the new tags */
    	
        // post_id.value = intval(post_id);
        if (!booleanval(post_id)) {
            return false;
        }

        if (empty(tags)) {
            tags = new Array<Object>();
        }

        tags = is_array(tags)
            ? (Array<Object>) tags
            : Strings.explode(",", Strings.trim(strval(tags), " \n\t\r\0\u000B,"));
        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_set_object_terms(post_id, tags, "post_tag", append);

        return true;
    }

    /**
     * wp_set_post_categories() - Set categories for a post{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @uses $wpdb
     * @param int $post_ID post ID
     * @param array $post_categories
     * @return bool|mixed {@internal Missing Description}}
     */
    public Object wp_set_post_categories(int post_ID, Object post_categories)/* Do not change type */
     {
        // post_ID.value = intval(post_ID);
    	
    	// If $post_categories isn't already an array, make it one:
        if (!is_array(post_categories) || equal(0, Array.count(post_categories)) || empty(post_categories)) {
            post_categories = new Array<Object>(new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category")));
        } else if (equal(1, Array.count(post_categories)) && equal("", ((Array) post_categories).getValue(0))) {
            return new Array<Object>();
        }

        post_categories = Array.array_map(new Callback("intval", VarHandling.class), (Array) post_categories);
        post_categories = Array.array_unique((Array) post_categories);

        return getIncluded(TaxonomyPage.class, gVars, gConsts).wp_set_object_terms(post_ID, post_categories, "category", false);
    }	// wp_set_post_categories()

    /**
     * wp_set_post_categories() wp_set_post_categories()
     * wp_transition_post_status() - Change the post transition status{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.3
     * @param string $new_status {@internal Missing Description}}
     * @param string $old_status {@internal Missing Description}}
     * @param int $post {@internal Missing Description}}
     */
    public void wp_transition_post_status(String new_status, String old_status, StdClass post) {
        if (!equal(new_status, old_status)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("transition_post_status", new_status, old_status, post);
            getIncluded(PluginPage.class, gVars, gConsts).do_action(old_status + "_to_" + new_status, post);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action(new_status + "_" + StdClass.getValue(post, "post_type"), StdClass.getValue(post, "ID"), post);
    }

 //
 // Trackback and ping functions
 //

 /**
  * add_ping() - Add a URL to those already pung
  *
  * {@internal Missing Long Description}}
  *
  * @package WordPress
  * @subpackage Post
  * @since 1.5
  * @uses $wpdb
  *
  * @param int $post_id post ID
  * @param string $uri {@internal Missing Description}}
  * @return mixed {@internal Missing Description}}
  */
    public int add_ping(int post_id, String uri) {
        String pung = null;
        String _new = null;
        pung = strval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT pinged FROM " + gVars.wpdb.posts + " WHERE ID = %d", post_id)));
        pung = Strings.trim(pung);
        Array<String> pungArray = QRegExPerl.preg_split("/\\s/", pung);
        pungArray.putValue(uri);
        _new = Strings.implode("\n", pungArray);
        _new = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("add_ping", _new));
    	// expected_slashed ($new)
        _new = Strings.stripslashes(gVars.webEnv, _new);

        return gVars.wpdb.update(gVars.wpdb.posts, new Array<Object>(new ArrayEntry<Object>("pinged", _new)), new Array<Object>(new ArrayEntry<Object>("ID", post_id)));
    }

    public Array<Object> get_enclosed(int post_id) {
        Object custom_fields = null;

        /* Do not change type */
        Array<Object> pung = new Array<Object>();
        Object key = null;
        Object val = null;

        /* Do not change type */
        Array<String> enclosure = new Array<String>();
        String enc = null;
        custom_fields = get_post_custom(post_id);
        pung = new Array<Object>();

        if (!is_array(custom_fields)) {
            return pung;
        }

        for (Map.Entry javaEntry540 : ((Array<?>) custom_fields).entrySet()) {
            key = javaEntry540.getKey();
            val = javaEntry540.getValue();

            if (!equal("enclosure", key) || !is_array(val)) {
                continue;
            }

            for (Map.Entry javaEntry541 : ((Array<?>) val).entrySet()) {
                enc = strval(javaEntry541.getValue());
                enclosure = QRegExPosix.split("\n", enc);
                pung.putValue(Strings.trim(enclosure.getValue(0)));
            }
        }

        pung = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_enclosed", pung);

        return pung;
    }

    /**
     * get_pung() - Get URLs already pinged for a post{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @uses $wpdb
     * @param int $post_id post ID
     * @return array {@internal Missing Description}}
     */
    public Array<String> get_pung(int post_id) {
        String pung = null;
        pung = strval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT pinged FROM " + gVars.wpdb.posts + " WHERE ID = %d", post_id)));
        pung = Strings.trim(pung);

        Array<String> pungArray = QRegExPerl.preg_split("/\\s/", pung);
        pungArray = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_pung", pungArray);

        return pungArray;
    }

    /**
     * get_to_ping() - Get any URLs in the todo list{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @uses $wpdb
     * @param int $post_id post ID
     * @return array {@internal Missing Description}}
     */
    public String get_to_ping(Object post_id) {
        String to_ping = null;
        to_ping = strval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT to_ping FROM " + gVars.wpdb.posts + " WHERE ID = %d", post_id)));
        to_ping = Strings.trim(to_ping);
        to_ping = strval(QRegExPerl.preg_split("/\\s/", to_ping, -1, RegExPerl.PREG_SPLIT_NO_EMPTY));
        to_ping = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_to_ping", to_ping));

        return to_ping;
    }

    /**
     * trackback_url_list() - Do trackbacks for a list of urls{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.0.1
     * @param string $tb_list comma separated list of URLs
     * @param int $post_id post ID
     */
    public void trackback_url_list(String tb_list, int post_id) {
        Array<Object> postdata = new Array<Object>();
        String excerpt = null;
        String post_excerpt = null;
        String post_content = null;
        Array<String> trackback_urls;
        String tb_url = null;
        String post_title = null;

        if (!empty(tb_list)) {
    		// get post data
            postdata = (Array<Object>) wp_get_single_post(post_id, gConsts.getARRAY_A());
            
            // import postdata as variables
            post_excerpt = strval(Array.extractVar(postdata, "post_excerpt", post_excerpt, Array.EXTR_SKIP));
            post_content = strval(Array.extractVar(postdata, "post_content", post_content, Array.EXTR_SKIP));
            post_title = strval(Array.extractVar(postdata, "post_title", post_title, Array.EXTR_SKIP));
            
            // form an excerpt
            excerpt = Strings.strip_tags(booleanval(post_excerpt)
                    ? post_excerpt
                    : post_content);

            if (Strings.strlen(excerpt) > 255) {
                excerpt = Strings.substr(excerpt, 0, 252) + "...";
            }

            trackback_urls = Strings.explode(",", tb_list);

            for (Map.Entry javaEntry542 : trackback_urls.entrySet()) {
                tb_url = strval(javaEntry542.getValue());
                tb_url = Strings.trim(tb_url);
                getIncluded(CommentPage.class, gVars, gConsts).trackback(tb_url, Strings.stripslashes(gVars.webEnv, post_title), excerpt, post_id);
            }
        }
    }

    //
 // Page functions
 //

 /**
  * get_all_page_ids() - Get a list of page IDs
  *
  * {@internal Missing Long Description}}
  *
  * @package WordPress
  * @subpackage Post
  * @since 2.0
  * @uses $wpdb
  *
  * @return array {@internal Missing Description}}
  */
    public Array<Object> get_all_page_ids() {
        Array<Object> page_ids = new Array<Object>();

        if (!booleanval(page_ids = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("all_page_ids", "posts"))) {
            page_ids = gVars.wpdb.get_col("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_type = \'page\'");
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("all_page_ids", page_ids, "posts", 0);
        }

        return page_ids;
    }

    /**
     * get_page() - Retrieves page data given a page ID or page object{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5.1
     * @param mixed &$page page object or page ID
     * @param string $output what to output
     * @param string $filter How the return value should be filtered.
     * @return mixed {@internal Missing Description}}
     */
    public Object get_page(int page, String output, String filter) {
        if (empty(page)) {
            if (isset(gVars.page))/* Commented by Numiton: && isset(gVars.page.ID)*/
             {
                return get_post(gVars.page, output, filter);
            } else {
                return null;
            }
        }

        return get_post(page, output, filter);
    }

    /**
     * get_page_by_path() - Retrieves a page given its path{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @uses $wpdb
     * @param string $page_path page path
     * @param string $output output type
     * @return mixed {@internal Missing Description}}
     */
    public Object get_page_by_path(String page_path, String output) {
        String page_paths = null;
        String leaf_path = null;
        String full_path = null;
        String pathdir = null;
        Array<Object> pages = new Array<Object>();
        String path = null;
        StdClass curpage;
        StdClass page = null;
        page_path = URL.rawurlencode(URL.urldecode(page_path));
        page_path = Strings.str_replace("%2F", "/", page_path);
        page_path = Strings.str_replace("%20", " ", page_path);
        page_paths = "/" + Strings.trim(page_path, "/");
        leaf_path = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(FileSystemOrSocket.basename(page_paths), "");

        Array<String> page_pathsArray = Strings.explode("/", page_paths);

        for (Map.Entry javaEntry543 : page_pathsArray.entrySet()) {
            pathdir = strval(javaEntry543.getValue());
            full_path = full_path + ((!equal(pathdir, ""))
                ? "/"
                : "") + getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(pathdir, "");
        }

        pages = gVars.wpdb.get_results(
                gVars.wpdb.prepare("SELECT ID, post_name, post_parent FROM " + gVars.wpdb.posts + " WHERE post_name = %s AND (post_type = \'page\' OR post_type = \'attachment\')", leaf_path));

        if (empty(pages)) {
            return null;
        }

        for (Map.Entry javaEntry544 : pages.entrySet()) {
            page = (StdClass) javaEntry544.getValue();
            path = "/" + leaf_path;
            curpage = page;

            while (!equal(StdClass.getValue(curpage, "post_parent"), 0)) {
                curpage = (StdClass) gVars.wpdb.get_row(
                        gVars.wpdb.prepare("SELECT ID, post_name, post_parent FROM " + gVars.wpdb.posts + " WHERE ID = %d and post_type=\'page\'", StdClass.getValue(curpage, "post_parent")));
                path = "/" + StdClass.getValue(curpage, "post_name") + path;
            }

            if (equal(path, full_path)) {
                return get_page(intval(StdClass.getValue(page, "ID")), output, "raw");
            }
        }

        return null;
    }

    /**
     * get_page_by_title() - Retrieve a page given its title{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @uses $wpdb
     * @param string $page_title page title
     * @param string $output output type
     * @return mixed {@internal Missing Description}}
     */
    public Object get_page_by_title(String page_title, String output) {
        int page = intval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_title = %s AND post_type=\'page\'", page_title)));

        if (booleanval(page)) {
            return get_page(page, output, "raw");
        }

        return null;
    }

    /**
     * get_page_children() - Retrieve child pages{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5.1
     * @param int $page_id page ID
     * @param array $pages list of pages
     * @return array {@internal Missing Description}}
     */
    public Array get_page_children(int page_id, Array<?> pages) {
        Array<Object> page_list = new Array<Object>();
        StdClass page = null;
        Array<Object> children = new Array<Object>();
        page_list = new Array<Object>();

        for (Map.Entry javaEntry545 : pages.entrySet()) {
            page = (StdClass) javaEntry545.getValue();

            if (equal(StdClass.getValue(page, "post_parent"), page_id)) {
                page_list.putValue(page);

                if (booleanval(children = get_page_children(intval(StdClass.getValue(page, "ID")), pages))) {
                    page_list = Array.array_merge(page_list, children);
                }
            }
        }

        return page_list;
    }

    /**
     * get_page_hierarchy() - {@internal Missing Short Description}}
     * Fetches the pages returned as a FLAT list, but arranged in order of their
     * hierarchy, i.e., child parents immediately follow their parents.
     *
     * @subpackage Post
     * @since 2.0
     * @param array $posts posts array
     * @param int $parent parent page ID
     * @return array {@internal Missing Description}}
     */
    public Array<Object> get_page_hierarchy(Array<Object> posts, int parent) {
        Array<Object> result = new Array<Object>();
        StdClass post = null;
        Array<Object> children = null;
        result = new Array<Object>();

        if (booleanval(posts)) {
            for (Map.Entry javaEntry546 : posts.entrySet()) {
                post = (StdClass) javaEntry546.getValue();

                if (equal(StdClass.getValue(post, "post_parent"), parent)) {
                    result.putValue(StdClass.getValue(post, "ID"), StdClass.getValue(post, "post_name"));
                    children = get_page_hierarchy(posts, intval(StdClass.getValue(post, "ID")));
                    result = Array.arrayAppend(result, children); //append $children to $result
                }
            }
        }

        return result;
    }

    /**
     * get_page_uri() - Builds a page URI{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @param int $page_id page ID
     * @return string {@internal Missing Description}}
     */
    public String get_page_uri(int page_id) {
        StdClass page = null;
        String uri = null;
        page = (StdClass) get_page(page_id, gConsts.getOBJECT(), "raw");
        uri = URL.urldecode(strval(StdClass.getValue(page, "post_name")));

    	// A page cannot be it's own parent.
        if (equal(StdClass.getValue(page, "post_parent"), StdClass.getValue(page, "ID"))) {
            return uri;
        }

        while (!equal(StdClass.getValue(page, "post_parent"), 0)) {
            page = (StdClass) get_page(intval(StdClass.getValue(page, "post_parent")), gConsts.getOBJECT(), "raw");
            uri = URL.urldecode(strval(StdClass.getValue(page, "post_name"))) + "/" + uri;
        }

        return uri;
    }

    /**
     * get_pages() - Retrieve a list of pages{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.5
     * @uses $wpdb
     * @param mixed $args Optional. Array or string of options
     * @return array List of pages matching defaults or $args
     */
    public Array<StdClass> get_pages(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        String key = null;
        Array<Object> cache = new Array<Object>();
        String inclusions = null;
        String include = null;
        Integer child_of = null;
        String exclude = null;
        String meta_key = null;
        String meta_value = null;
        Boolean hierarchical = null;
        Array<Object> incpages = new Array<Object>();
        Object incpage = null;
        String exclusions = null;
        Array<Object> expages = new Array<Object>();
        Object expage = null;
        String author_query = null;
        String authors = null;
        Array<Object> post_authors = new Array<Object>();
        Object post_author = null;
        String query = null;
        Object sort_column = null;
        Object sort_order = null;
        Array<StdClass> pages = new Array<StdClass>();
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("child_of", 0),
                new ArrayEntry<Object>("sort_order", "ASC"),
                new ArrayEntry<Object>("sort_column", "post_title"),
                new ArrayEntry<Object>("hierarchical", 1),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("include", ""),
                new ArrayEntry<Object>("meta_key", ""),
                new ArrayEntry<Object>("meta_value", ""),
                new ArrayEntry<Object>("authors", ""));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        include = strval(Array.extractVar(r, "include", include, Array.EXTR_SKIP));
        child_of = intval(Array.extractVar(r, "child_of", child_of, Array.EXTR_SKIP));
        exclude = strval(Array.extractVar(r, "exclude", exclude, Array.EXTR_SKIP));
        meta_key = strval(Array.extractVar(r, "meta_key", meta_key, Array.EXTR_SKIP));
        meta_value = strval(Array.extractVar(r, "meta_value", meta_value, Array.EXTR_SKIP));
        hierarchical = booleanval(Array.extractVar(r, "hierarchical", hierarchical, Array.EXTR_SKIP));
        authors = strval(Array.extractVar(r, "authors", authors, Array.EXTR_SKIP));
        sort_column = Array.extractVar(r, "sort_column", sort_column, Array.EXTR_SKIP);
        sort_order = Array.extractVar(r, "sort_order", sort_order, Array.EXTR_SKIP);
        
        key = Strings.md5(serialize(r));

        if (booleanval(cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("get_pages", "posts"))) {
            if (isset(cache.getValue(key))) {
                return (Array<StdClass>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_pages", cache.getValue(key), r);
            }
        } else {
            cache = new Array<Object>();
        }

        inclusions = "";

        if (!empty(include)) {
            child_of = 0; //ignore child_of, exclude, meta_key, and meta_value params if using include
            exclude = "";
            meta_key = "";
            meta_value = "";
            hierarchical = false;
            incpages = QRegExPerl.preg_split("/[\\s,]+/", include);

            if (booleanval(Array.count(incpages))) {
                for (Map.Entry javaEntry547 : incpages.entrySet()) {
                    incpage = javaEntry547.getValue();

                    if (empty(inclusions)) {
                        inclusions = gVars.wpdb.prepare(" AND ( ID = %d ", incpage);
                    } else {
                        inclusions = inclusions + gVars.wpdb.prepare(" OR ID = %d ", incpage);
                    }
                }
            }
        }

        if (!empty(inclusions)) {
            inclusions = inclusions + ")";
        }

        exclusions = "";

        if (!empty(exclude)) {
            expages = QRegExPerl.preg_split("/[\\s,]+/", exclude);

            if (booleanval(Array.count(expages))) {
                for (Map.Entry javaEntry548 : expages.entrySet()) {
                    expage = javaEntry548.getValue();

                    if (empty(exclusions)) {
                        exclusions = gVars.wpdb.prepare(" AND ( ID <> %d ", expage);
                    } else {
                        exclusions = exclusions + gVars.wpdb.prepare(" AND ID <> %d ", expage);
                    }
                }
            }
        }

        if (!empty(exclusions)) {
            exclusions = exclusions + ")";
        }

        author_query = "";

        if (!empty(authors)) {
            post_authors = QRegExPerl.preg_split("/[\\s,]+/", authors);

            if (booleanval(Array.count(post_authors))) {
                for (Map.Entry javaEntry549 : post_authors.entrySet())/* ? */
                 {
                    post_author = javaEntry549.getValue();

    				//Do we have an author id or an author login?
                    if (equal(0, intval(post_author))) {
                        post_author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(strval(post_author));

                        if (empty(post_author)) {
                            continue;
                        }

                        if (empty(((StdClass) post_author).fields.getValue("ID"))) {
                            continue;
                        }

                        post_author = ((StdClass) post_author).fields.getValue("ID");
                    }

                    if (equal("", author_query)) {
                        author_query = gVars.wpdb.prepare(" post_author = %d ", post_author);
                    } else {
                        author_query = author_query + gVars.wpdb.prepare(" OR post_author = %d ", post_author);
                    }
                }

                if (!equal("", author_query)) {
                    author_query = " AND (" + author_query + ")";
                }
            }
        }

        query = "SELECT * FROM " + gVars.wpdb.posts + " ";
        query = query + (empty(meta_key)
            ? ""
            : (", " + gVars.wpdb.postmeta + " "));
        query = query + " WHERE (post_type = \'page\' AND post_status = \'publish\') " + exclusions + " " + inclusions + " ";
    	// expected_slashed ($meta_key, $meta_value) -- also, it looks funky
        query = query +
            (booleanval(intval(empty(meta_key)) | intval(empty(meta_value)))
            ? ""
            : (" AND (" + gVars.wpdb.posts + ".ID = " + gVars.wpdb.postmeta + ".post_id AND " + gVars.wpdb.postmeta + ".meta_key = \'" + meta_key + "\' AND " + gVars.wpdb.postmeta +
            ".meta_value = \'" + meta_value + "\' )"));
        query = query + author_query;
        query = query + " ORDER BY " + strval(sort_column) + " " + strval(sort_order);
        pages = gVars.wpdb.get_results(query);

        if (empty(pages)) {
            return (Array<StdClass>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_pages", new Array<Object>(), r);
        }

    	// Update cache.
        update_page_cache(pages);

        if (booleanval(child_of) || hierarchical) {
            pages = get_page_children(child_of, pages);
        }

        cache.putValue(key, pages);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("get_pages", cache, "posts", 0);
        pages = (Array<StdClass>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_pages", pages, r);

        return pages;
    }

 //
 // Attachment functions
 //

 /**
  * is_local_attachment() - Check if the attachment URI is local one and is really an attachment.
  *
  * {@internal Missing Long Description}}
  *
  * @package WordPress
  * @subpackage Post
  * @since 2.0
  *
  * @param string $url URL to check
  * @return bool {@internal Missing Description}}
  */
    public boolean is_local_attachment(String url) {
        int id;
        StdClass post = null;

        if (strictEqual(Strings.strpos(url, getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw")), BOOLEAN_FALSE)) {
            return false;
        }

        if (!strictEqual(Strings.strpos(url, getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "/?attachment_id="), BOOLEAN_FALSE)) {
            return true;
        }

        if (booleanval(id = getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(url))) {
            post = (StdClass) get_post(id, gConsts.getOBJECT(), "raw");

            if (equal("attachment", StdClass.getValue(post, "post_type"))) {
                return true;
            }
        }

        return false;
    }

    /**
     * wp_insert_attachment() - Insert an attachment{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.0
     * @uses $wpdb
     * @uses $user_ID
     * @param object $object attachment object
     * @param string $file filename
     * @param int $post_parent parent post ID
     * @return int {@internal Missing Description}}
     */
    public int wp_insert_attachment(Object objectObj, String file, int parent) {
        Array<Object> defaults = new Array<Object>();
        Object post_category = null;

        /* Do not change type */
        Integer post_author = null;

        /* Added by Numiton */
        Object post_content = null;
        Object post_content_filtered = null;
        Object post_excerpt = null;
        Object post_mime_type = null;
        Object guid = null;
        String post_type = null;
        String post_status = null;
        Boolean update = null;
        Object ID = null;
        Integer post_ID = null;
        String post_name = null;
        String post_title = null;
        Object post_name_check = null;
        Integer suffix = null;
        String alt_post_name = null;
        Integer post_parent = null;
        String post_date = null;
        String post_date_gmt = null;
        String post_modified = null;
        String post_modified_gmt = null;
        String comment_status = null;
        Object ping_status = null;
        String to_ping = null;
        Integer menu_order = null;
        String post_password = null;
        String pinged = null;
        Array<Object> data = new Array<Object>();
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("post_status", "draft"),
                new ArrayEntry<Object>("post_type", "post"),
                new ArrayEntry<Object>("post_author", gVars.user_ID),
                new ArrayEntry<Object>("ping_status", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status")),
                new ArrayEntry<Object>("post_parent", 0),
                new ArrayEntry<Object>("menu_order", 0),
                new ArrayEntry<Object>("to_ping", ""),
                new ArrayEntry<Object>("pinged", ""),
                new ArrayEntry<Object>("post_password", ""),
                new ArrayEntry<Object>("guid", ""),
                new ArrayEntry<Object>("post_content_filtered", ""),
                new ArrayEntry<Object>("post_excerpt", ""));

        Array<Object> object = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(objectObj, defaults);

        if (!empty(parent)) {
            object.putValue("post_parent", parent);
        }

        object = (Array<Object>) sanitize_post(object, "db");
        
    	// export array as variables
        post_category = Array.extractVar(object, "post_category", post_category, Array.EXTR_SKIP);
        post_author = intval(Array.extractVar(object, "post_author", post_author, Array.EXTR_SKIP));
        post_content = Array.extractVar(object, "post_content", post_content, Array.EXTR_SKIP);
        post_content_filtered = Array.extractVar(object, "post_content_filtered", post_content_filtered, Array.EXTR_SKIP);
        post_excerpt = Array.extractVar(object, "post_excerpt", post_excerpt, Array.EXTR_SKIP);
        post_mime_type = Array.extractVar(object, "post_mime_type", post_mime_type, Array.EXTR_SKIP);
        guid = Array.extractVar(object, "guid", guid, Array.EXTR_SKIP);
        ID = Array.extractVar(object, "ID", ID, Array.EXTR_SKIP);
        post_ID = intval(Array.extractVar(object, "post_ID", post_ID, Array.EXTR_SKIP));
        post_name = strval(Array.extractVar(object, "post_name", post_name, Array.EXTR_SKIP));
        post_title = strval(Array.extractVar(object, "post_title", post_title, Array.EXTR_SKIP));
        post_parent = intval(Array.extractVar(object, "post_parent", post_parent, Array.EXTR_SKIP));
        post_date = strval(Array.extractVar(object, "post_date", post_date, Array.EXTR_SKIP));
        post_date_gmt = strval(Array.extractVar(object, "post_date_gmt", post_date_gmt, Array.EXTR_SKIP));
        post_modified = strval(Array.extractVar(object, "post_modified", post_modified, Array.EXTR_SKIP));
        post_modified_gmt = strval(Array.extractVar(object, "post_modified_gmt", post_modified_gmt, Array.EXTR_SKIP));
        comment_status = strval(Array.extractVar(object, "comment_status", comment_status, Array.EXTR_SKIP));
        ping_status = Array.extractVar(object, "ping_status", ping_status, Array.EXTR_SKIP);
        to_ping = strval(Array.extractVar(object, "to_ping", to_ping, Array.EXTR_SKIP));
        menu_order = intval(Array.extractVar(object, "menu_order", menu_order, Array.EXTR_SKIP));
        post_password = strval(Array.extractVar(object, "post_password", post_password, Array.EXTR_SKIP));
        pinged = strval(Array.extractVar(object, "pinged", pinged, Array.EXTR_SKIP));

    	// Make sure we set a valid category
        if (equal(0, Array.count(post_category)) || !is_array(post_category)) {
            post_category = new Array<Object>(new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category")));
        }

        if (empty(post_author)) {
            post_author = gVars.user_ID;
        }

        post_type = "attachment";
        post_status = "inherit";
        
    	// Are we updating or creating?
        update = false;

        if (!empty(ID)) {
            update = true;
            post_ID = intval(ID);
        }

    	// Create a valid post name.
        if (empty(post_name)) {
            post_name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(post_title, "");
        } else {
            post_name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(post_name, "");
        }

    	// expected_slashed ($post_name)
        post_name_check = gVars.wpdb.get_var(
                    gVars.wpdb.prepare("SELECT post_name FROM " + gVars.wpdb.posts + " WHERE post_name = \'" + post_name + "\' AND post_status = \'inherit\' AND ID != %d LIMIT 1", post_ID));

        if (booleanval(post_name_check)) {
            suffix = 2;

            while (booleanval(post_name_check))/*
             * expected_slashed ($alt_post_name, $post_name) expected_slashed
             * ($alt_post_name, $post_name)
             */
             {
                alt_post_name = post_name + "-" + strval(suffix);
    			// expected_slashed ($alt_post_name, $post_name)
                post_name_check = gVars.wpdb.get_var(
                            gVars.wpdb.prepare(
                                    "SELECT post_name FROM " + gVars.wpdb.posts + " WHERE post_name = \'" + alt_post_name +
                                    "\' AND post_status = \'inherit\' AND ID != %d AND post_parent = %d LIMIT 1",
                                    post_ID,
                                    post_parent));
                suffix++;
            }

            post_name = alt_post_name;
        }

        if (empty(post_date)) {
            post_date = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
        }

        if (empty(post_date_gmt)) {
            post_date_gmt = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
        }

        if (empty(post_modified)) {
            post_modified = post_date;
        }

        if (empty(post_modified_gmt)) {
            post_modified_gmt = post_date_gmt;
        }

        if (empty(comment_status)) {
            if (update) {
                comment_status = "closed";
            } else {
                comment_status = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status"));
            }
        }

        if (empty(ping_status)) {
            ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");
        }

        if (isset(to_ping)) {
            to_ping = QRegExPerl.preg_replace("|\\s+|", "\n", to_ping);
        } else {
            to_ping = "";
        }

        if (isset(post_parent)) {
            post_parent = post_parent;
        } else {
            post_parent = 0;
        }

        if (isset(menu_order)) {
            menu_order = menu_order;
        } else {
            menu_order = 0;
        }

        if (!isset(post_password)) {
            post_password = "";
        }

        if (!isset(pinged)) {
            pinged = "";
        }

    	// expected_slashed (everything!)
        data = Array.compact(
                new ArrayEntry("post_author", post_author),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_content_filtered", post_content_filtered),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_type", post_type),
                new ArrayEntry("comment_status", comment_status),
                new ArrayEntry("ping_status", ping_status),
                new ArrayEntry("post_password", post_password),
                new ArrayEntry("post_name", post_name),
                new ArrayEntry("to_ping", to_ping),
                new ArrayEntry("pinged", pinged),
                new ArrayEntry("post_modified", post_modified),
                new ArrayEntry("post_modified_gmt", post_modified_gmt),
                new ArrayEntry("post_parent", post_parent),
                new ArrayEntry("menu_order", menu_order),
                new ArrayEntry("post_mime_type", post_mime_type),
                new ArrayEntry("guid", guid));
        data = (Array<Object>) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(data);

        if (update) {
            gVars.wpdb.update(gVars.wpdb.posts, data, new Array<Object>(new ArrayEntry<Object>("ID", post_ID)));
        } else {
            gVars.wpdb.insert(gVars.wpdb.posts, data);
            post_ID = gVars.wpdb.insert_id;
        }

        if (empty(post_name)) {
            post_name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(post_title, strval(post_ID));
            gVars.wpdb.update(gVars.wpdb.posts, Array.compact(new ArrayEntry("post_name", post_name)), new Array<Object>(new ArrayEntry<Object>("ID", post_ID)));
        }

        wp_set_post_categories(post_ID, post_category);

        if (booleanval(file)) {
            update_attached_file(post_ID, file);
        }

        clean_post_cache(post_ID);

        if (update) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_attachment", post_ID);
        } else {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("add_attachment", post_ID);
        }

        return post_ID;
    }

    /**
     * wp_delete_attachment() - Delete an attachment{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.0
     * @uses $wpdb
     * @param int $postid attachment Id
     * @return mixed {@internal Missing Description}}
     */
    public StdClass wp_delete_attachment(int postid) {
        StdClass post;
        Array<Object> meta = null;
        Object file = null;
        String thumbfile;
        Array<Object> sizes = null;
        Array<Object> intermediate = new Array<Object>();
        Object size = null;
        String intermediate_file = null;

        if (!booleanval(post = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.posts + " WHERE ID = %d", postid)))) {
            return post;
        }

        if (!equal("attachment", StdClass.getValue(post, "post_type"))) {
            return null;
        }

        meta = wp_get_attachment_metadata(postid, false);
        file = get_attached_file(postid, false);
        
    	/** @todo Delete for pluggable post taxonomies too */
        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_object_term_relationships(postid, new Array<Object>(new ArrayEntry<Object>("category"), new ArrayEntry<Object>("post_tag")));
        
        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.posts + " WHERE ID = %d", postid));
        
        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = %d", postid));
        
        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d ", postid));

        if (!is_null(meta) && !empty(meta.getValue("thumb"))) {
    		// Don't delete the thumb if another attachment uses it
            if (!booleanval(
                        gVars.wpdb.get_row(
                            gVars.wpdb.prepare(
                                "SELECT meta_id FROM " + gVars.wpdb.postmeta + " WHERE meta_key = \'_wp_attachment_metadata\' AND meta_value LIKE %s AND post_id <> %d",
                                "%" + meta.getValue("thumb") + "%",
                                postid)))) {
                thumbfile = Strings.str_replace(FileSystemOrSocket.basename(strval(file)), meta.getArrayValue("thumb"), strval(file));
                thumbfile = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_delete_file", thumbfile));
                JFileSystemOrSocket.unlink(gVars.webEnv, thumbfile);
            }
        }

    	// remove intermediate images if there are any
        sizes = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts)
                                    .apply_filters("intermediate_image_sizes", new Array<Object>(new ArrayEntry<Object>("thumbnail"), new ArrayEntry<Object>("medium")));

        for (Map.Entry javaEntry550 : sizes.entrySet()) {
            size = javaEntry550.getValue();

            if (booleanval(intermediate = getIncluded(MediaPage.class, gVars, gConsts).image_get_intermediate_size(postid, size))) {
                intermediate_file = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_delete_file", intermediate.getValue("path")));
                JFileSystemOrSocket.unlink(gVars.webEnv, intermediate_file);
            }
        }

        file = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_delete_file", file));

        if (!empty(file)) {
            JFileSystemOrSocket.unlink(gVars.webEnv, strval(file));
        }

        clean_post_cache(postid);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_attachment", postid);

        return post;
    }

    /**
     * wp_get_attachment_metadata() - Retrieve metadata for an attachment{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id attachment ID
     * @param bool $unfiltered Optional, default is false. If true, filters are
     * not run
     * @return array {@internal Missing Description}}
     */
    public Array<Object> wp_get_attachment_metadata(int post_id, boolean unfiltered) {
        StdClass post = null;
        Array<Object> data = null;

        // post_id.value = intval(post_id);
        if (!booleanval(post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return null;
        }

        data = (Array<Object>) get_post_meta(intval(StdClass.getValue(post, "ID")), "_wp_attachment_metadata", true);

        if (unfiltered) {
            return data;
        }

        return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_get_attachment_metadata", data, StdClass.getValue(post, "ID"));
    }

    /**
     * wp_update_attachment_metadata() - Update metadata for an attachment{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id attachment ID
     * @param array $data attachment data
     * @return int {@internal Missing Description}}
     */
    public boolean wp_update_attachment_metadata(Object post_id, Object data) {
        StdClass post = null;
        Array<Object> old_data = null;

        // post_id.value = intval(post_id);
        if (!booleanval(post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return false;
        }

        old_data = wp_get_attachment_metadata(intval(StdClass.getValue(post, "ID")), true);
        data = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_update_attachment_metadata", data, StdClass.getValue(post, "ID"));

        if (booleanval(old_data)) {
            return update_post_meta(intval(StdClass.getValue(post, "ID")), "_wp_attachment_metadata", data, old_data);
        } else {
            return add_post_meta(intval(StdClass.getValue(post, "ID")), "_wp_attachment_metadata", data, false);
        }
    }

    /**
     * wp_get_attachment_url() - Retrieve the URL for an attachment{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id attachment ID
     * @return string {@internal Missing Description}}
     */
    public String wp_get_attachment_url(int post_id) {
        StdClass post = null;
        String url = null;

        // post_id.value = intval(post_id);
        if (!booleanval(post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return "";
        }

        url = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_guid(intval(StdClass.getValue(post, "ID")));

        if (!equal("attachment", StdClass.getValue(post, "post_type")) || !booleanval(url)) {
            return "";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_get_attachment_url", url, StdClass.getValue(post, "ID")));
    }

    /**
     * wp_get_attachment_thumb_file() - Retrieve thumbnail for an attachment{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id attachment ID
     * @return mixed {@internal Missing Description}}
     */
    public String wp_get_attachment_thumb_file(int post_id) {
        StdClass post = null;
        Array<Object> imagedata = null;
        String file = null;
        String thumbfile;

        // post_id.value = intval(post_id);
        if (!booleanval(post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return "";
        }

        if (!booleanval(imagedata = wp_get_attachment_metadata(intval(StdClass.getValue(post, "ID")), false))) {
            return "";
        }

        file = strval(get_attached_file(intval(StdClass.getValue(post, "ID")), false));

        if (!empty(imagedata.getValue("thumb")) && booleanval(thumbfile = Strings.str_replace(FileSystemOrSocket.basename(file), imagedata.getArrayValue("thumb"), file)) &&
                FileSystemOrSocket.file_exists(gVars.webEnv, thumbfile)) {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_get_attachment_thumb_file", thumbfile, StdClass.getValue(post, "ID")));
        }

        return "";
    }

    /**
     * wp_get_attachment_thumb_url() - Retrieve URL for an attachment
     * thumbnail{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id attachment ID
     * @return string {@internal Missing Description}}
     */
    public String wp_get_attachment_thumb_url(int post_id) {
        StdClass post = null;
        String url;
        Array<Object> sized = new Array<Object>();
        String thumb = strval(false);

        // post_id.value = intval(post_id);
        if (!booleanval(post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return "";
        }

        if (!booleanval(url = wp_get_attachment_url(intval(StdClass.getValue(post, "ID"))))) {
            return "";
        }

        sized = getIncluded(MediaPage.class, gVars, gConsts).image_downsize(post_id, "thumbnail");

        if (booleanval(sized)) {
            return strval(sized.getValue(0));
        }

        if (!booleanval(thumb = wp_get_attachment_thumb_file(intval(StdClass.getValue(post, "ID"))))) {
            return strval(false);
        }

        url = Strings.str_replace(FileSystemOrSocket.basename(url), FileSystemOrSocket.basename(thumb), url);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_get_attachment_thumb_url", url, StdClass.getValue(post, "ID")));
    }

    /**
     * wp_attachment_is_image() - Check if the attachment is an image{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id attachment ID
     * @return bool {@internal Missing Description}}
     */
    public boolean wp_attachment_is_image(Integer post_id) {
        StdClass post = null;
        String file = null;
        String ext = null;
        Array<Object> matches = new Array<Object>();
        Array<Object> image_exts = new Array<Object>();

        // post_id.value = intval(post_id);
        if (!booleanval(post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return false;
        }

        if (!booleanval(file = strval(get_attached_file(intval(StdClass.getValue(post, "ID")), false)))) {
            return false;
        }

        ext = (QRegExPerl.preg_match("/\\.([^.]+)$/", file, matches)
            ? Strings.strtolower(strval(matches.getValue(1)))
            : strval(false));
        image_exts = new Array<Object>(new ArrayEntry<Object>("jpg"), new ArrayEntry<Object>("jpeg"), new ArrayEntry<Object>("gif"), new ArrayEntry<Object>("png"));

        if (equal("image/", Strings.substr(strval(StdClass.getValue(post, "post_mime_type")), 0, 6)) ||
                (booleanval(ext) && equal("import", StdClass.getValue(post, "post_mime_type")) && Array.in_array(ext, image_exts))) {
            return true;
        }

        return false;
    }

    /**
     * wp_mime_type_icon() - Retrieve the icon for a MIME type{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param string $mime MIME type
     * @return string|bool {@internal Missing Description}}
     */
    public String wp_mime_type_icon(Object mime) {
        Object icon = null;
        int post_id = 0;
        Array<Object> post_mimes = new Array<Object>();
        StdClass post = null;
        String ext;
        Object ext_type = null;
        Object icon_files;

        /* Do not change type */
        Object icon_dir = null;
        Object icon_dir_uri = null;
        Array<Object> dirs = new Array<Object>();
        String dir;
        Array<Object> keys = new Array<Object>();
        Object uri = null;
        int dh = 0;
        String file = null;
        Array<Object> types = new Array<Object>();
        Array<Object> matches = new Array<Object>();
        Array<Object> wilds = new Array<Object>();
        Object match = null;

        if (!is_numeric(mime)) {
            icon = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("mime_type_icon_" + strval(mime), "");
        }

        if (empty(icon)) {
            post_id = 0;
            post_mimes = new Array<Object>();

            if (is_numeric(mime)) {
                mime = intval(mime);

                if (booleanval(post = (StdClass) get_post(mime, gConsts.getOBJECT(), "raw"))) {
                    post_id = intval(StdClass.getValue(post, "ID"));
                    ext = QRegExPerl.preg_replace("/^.+?\\.([^.]+)$/", "$1", strval(StdClass.getValue(post, "guid")));

                    if (!empty(ext)) {
                        post_mimes.putValue(ext);

                        if (booleanval(ext_type = getIncluded(FunctionsPage.class, gVars, gConsts).wp_ext2type(ext))) {
                            post_mimes.putValue(ext_type);
                        }
                    }

                    mime = StdClass.getValue(post, "post_mime_type");
                } else {
                    mime = 0;
                }
            } else {
                post_mimes.putValue(mime);
            }

            icon_files = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("icon_files", "");

            if (!is_array(icon_files)) {
                icon_dir = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("icon_dir", gConsts.getABSPATH() + gConsts.getWPINC() + "/images/crystal");
                icon_dir_uri = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                            "icon_dir_uri",
                            getIncluded(FormattingPage.class, gVars, gConsts)
                                .trailingslashit(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl")) + gConsts.getWPINC() + "/images/crystal"));
                dirs = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("icon_dirs", new Array<Object>(new ArrayEntry<Object>(icon_dir, icon_dir_uri)));
                icon_files = new Array<Object>();

                while (booleanval(dirs)) {
                    dir = strval(Array.array_shift(keys = Array.array_keys(dirs)));
                    uri = Array.array_shift(dirs);

                    if (booleanval(dh = Directories.opendir(gVars.webEnv, dir))) {
                        while (!strictEqual(STRING_FALSE, file = Directories.readdir(gVars.webEnv, dh))) {
                            file = FileSystemOrSocket.basename(file);

                            if (equal(Strings.substr(file, 0, 1), ".")) {
                                continue;
                            }

                            if (!Array.in_array(
                                        Strings.strtolower(Strings.substr(file, -4)),
                                        new Array<Object>(new ArrayEntry<Object>(".png"), new ArrayEntry<Object>(".gif"), new ArrayEntry<Object>(".jpg")))) {
                                if (FileSystemOrSocket.is_dir(gVars.webEnv, dir + "/" + file)) {
                                    dirs.putValue(dir + "/" + file, strval(uri) + "/" + file);
                                }

                                continue;
                            }

                            ((Array) icon_files).putValue(dir + "/" + file, strval(uri) + "/" + file);
                        }

                        Directories.closedir(gVars.webEnv, dh);
                    }
                }

                getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("icon_files", icon_files, strval(600), 0);
            }

    		// Icon basename - extension = MIME wildcard
            for (Map.Entry javaEntry551 : ((Array<?>) icon_files).entrySet()) {
                file = strval(javaEntry551.getKey());
                uri = javaEntry551.getValue();
                types.putValue(QRegExPerl.preg_replace("/^([^.]*).*$/", "$1", FileSystemOrSocket.basename(file)), ((Array) icon_files).getValue(file));
            }

            if (!empty(mime)) {
                post_mimes.putValue(Strings.substr(strval(mime), 0, Strings.strpos(strval(mime), "/")));
                post_mimes.putValue(Strings.substr(strval(mime), Strings.strpos(strval(mime), "/") + 1));
                post_mimes.putValue(Strings.str_replace("/", "_", strval(mime)));
            }

            matches = wp_match_mime_types(Array.array_keys(types), post_mimes);
            matches.putValue("default", new Array<Object>(new ArrayEntry<Object>("default")));

            for (Map.Entry javaEntry552 : matches.entrySet()) {
                match = javaEntry552.getKey();
                wilds = (Array<Object>) javaEntry552.getValue();

                if (isset(types.getValue(wilds.getValue(0)))) {
                    icon = types.getValue(wilds.getValue(0));

                    if (!is_numeric(mime)) {
                        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("mime_type_icon_" + strval(mime), icon, "", 0);
                    }

                    break;
                }
            }
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_mime_type_icon", icon, mime, post_id)); // Last arg is 0 if function pass mime type.
    }

    /**
     * wp_check_for_changed_slugs() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.1
     * @param int $post_id The Post ID
     * @return int Same as $post_id
     */
    public int wp_check_for_changed_slugs(int post_id) {
        StdClass post = null;
        Array<Object> old_slugs = new Array<Object>();

        if (!isset(gVars.webEnv._POST.getValue("wp-old-slug")) || !booleanval(Strings.strlen(strval(gVars.webEnv._POST.getValue("wp-old-slug"))))) {
            return post_id;
        }

        post = (StdClass) get_post(post_id, gConsts.getOBJECT(), "raw");

    	// we're only concerned with published posts
        if (!equal(StdClass.getValue(post, "post_status"), "publish") || !equal(StdClass.getValue(post, "post_type"), "post")) {
            return post_id;
        }

    	// only bother if the slug has changed
        if (equal(StdClass.getValue(post, "post_name"), gVars.webEnv._POST.getValue("wp-old-slug"))) {
            return post_id;
        }

        old_slugs = new Array<Object>(get_post_meta(post_id, "_wp_old_slug", false));

    	// if we haven't added this old slug before, add it now
        if (!booleanval(Array.count(old_slugs)) || !Array.in_array(gVars.webEnv._POST.getValue("wp-old-slug"), old_slugs)) {
            add_post_meta(post_id, "_wp_old_slug", gVars.webEnv._POST.getValue("wp-old-slug"), false);
        }

    	// if the new slug was used previously, delete it from the list
        if (Array.in_array(StdClass.getValue(post, "post_name"), old_slugs)) {
            delete_post_meta(post_id, "_wp_old_slug", strval(StdClass.getValue(post, "post_name")));
        }

        return post_id;
    }

    /**
     * get_private_posts_cap_sql() - {@internal Missing Short Description}}
     * This function provides a standardized way to appropriately select on the
     * post_status of posts/pages. The function will return a piece of SQL code
     * that can be added to a WHERE clause; this SQL is constructed to allow all
     * published posts, and all private posts to which the user has access.
     *
     * @subpackage Post
     * @since 2.2
     * @uses $user_ID
     * @uses apply_filters() Call 'pub_priv_sql_capability' filter for plugins
     * with different post types
     * @param string $post_type currently only supports 'post' or 'page'.
     * @return string SQL code that can be added to a where clause.
     */
    public String get_private_posts_cap_sql(Object post_type) {
        String cap = null;
        String sql = null;
        cap = "";

    	// Private posts
        if (equal(post_type, "post")) {
            cap = "read_private_posts";
        	// Private pages
        } else if (equal(post_type, "page")) {
            cap = "read_private_pages";
        	// Dunno what it is, maybe plugins have their own post type?
        } else {
            cap = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pub_priv_sql_capability", cap));

            if (empty(cap)) {
    			// We don't know what it is, filters don't change anything,
    			// so set the SQL up to return nothing.
                return "1 = 0";
            }
        }

        sql = "(post_status = \'publish\'";

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(cap)) {
    		// Does the user have the capability to view private posts? Guess so.
            sql = sql + " OR post_status = \'private\'";
        } else if (getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
    		// Users can view their own private posts.
            sql = sql + " OR post_status = \'private\' AND post_author = \'" + strval(gVars.user_ID) + "\'";
        }

        sql = sql + ")";

        return sql;
    }

    /**
     * get_lastpostdate() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 0.71
     * @uses $wpdb
     * @uses $blog_id
     * @uses apply_filters() Calls 'get_lastpostdate' filter
     * @global mixed $cache_lastpostdate Stores the last post date
     * @global mixed $pagenow The current page being viewed
     * @param string $timezone The location to get the time. Can be 'gmt',
     * 'blog', or 'server'.
     * @return string The date of the last post.
     */
    public String get_lastpostdate(String timezone) {
        String add_seconds_server = null;
        Object lastpostdate = null;
        add_seconds_server = DateTime.date("Z");

        if (!isset(gVars.cache_lastpostdate.getArrayValue(gVars.blog_id).getValue(timezone))) {
            {
                int javaSwitchSelector86 = 0;

                if (equal(Strings.strtolower(timezone), "gmt")) {
                    javaSwitchSelector86 = 1;
                }

                if (equal(Strings.strtolower(timezone), "blog")) {
                    javaSwitchSelector86 = 2;
                }

                if (equal(Strings.strtolower(timezone), "server")) {
                    javaSwitchSelector86 = 3;
                }

                switch (javaSwitchSelector86) {
                case 1: {
                    lastpostdate = gVars.wpdb.get_var("SELECT post_date_gmt FROM " + gVars.wpdb.posts + " WHERE post_status = \'publish\' ORDER BY post_date_gmt DESC LIMIT 1");

                    break;
                }

                case 2: {
                    lastpostdate = gVars.wpdb.get_var("SELECT post_date FROM " + gVars.wpdb.posts + " WHERE post_status = \'publish\' ORDER BY post_date_gmt DESC LIMIT 1");

                    break;
                }

                case 3: {
                    lastpostdate = gVars.wpdb.get_var(
                                "SELECT DATE_ADD(post_date_gmt, INTERVAL \'" + add_seconds_server + "\' SECOND) FROM " + gVars.wpdb.posts +
                                " WHERE post_status = \'publish\' ORDER BY post_date_gmt DESC LIMIT 1");

                    break;
                }
                }
            }

            gVars.cache_lastpostdate.getArrayValue(gVars.blog_id).putValue(timezone, lastpostdate);
        } else {
            lastpostdate = gVars.cache_lastpostdate.getArrayValue(gVars.blog_id).getValue(timezone);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_lastpostdate", lastpostdate, timezone));
    }

    /**
     * get_lastpostmodified() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 1.2
     * @uses $wpdb
     * @uses $blog_id
     * @uses apply_filters() Calls 'get_lastpostmodified' filter
     * @global mixed $cache_lastpostmodified Stores the date the last post was
     * modified
     * @global mixed $pagenow The current page being viewed
     * @param string $timezone The location to get the time. Can be 'gmt',
     * 'blog', or 'server'.
     * @return string The date the post was last modified.
     */
    public String get_lastpostmodified(String timezone) {
        String add_seconds_server = null;
        Object lastpostmodified = null;
        Object lastpostdate = null;
        add_seconds_server = DateTime.date("Z");

        if (!isset(cache_lastpostmodified.getArrayValue(gVars.blog_id).getValue(timezone))) {
            {
                int javaSwitchSelector87 = 0;

                if (equal(Strings.strtolower(timezone), "gmt")) {
                    javaSwitchSelector87 = 1;
                }

                if (equal(Strings.strtolower(timezone), "blog")) {
                    javaSwitchSelector87 = 2;
                }

                if (equal(Strings.strtolower(timezone), "server")) {
                    javaSwitchSelector87 = 3;
                }

                switch (javaSwitchSelector87) {
                case 1: {
                    lastpostmodified = gVars.wpdb.get_var("SELECT post_modified_gmt FROM " + gVars.wpdb.posts + " WHERE post_status = \'publish\' ORDER BY post_modified_gmt DESC LIMIT 1");

                    break;
                }

                case 2: {
                    lastpostmodified = gVars.wpdb.get_var("SELECT post_modified FROM " + gVars.wpdb.posts + " WHERE post_status = \'publish\' ORDER BY post_modified_gmt DESC LIMIT 1");

                    break;
                }

                case 3: {
                    lastpostmodified = gVars.wpdb.get_var(
                                "SELECT DATE_ADD(post_modified_gmt, INTERVAL \'" + add_seconds_server + "\' SECOND) FROM " + gVars.wpdb.posts +
                                " WHERE post_status = \'publish\' ORDER BY post_modified_gmt DESC LIMIT 1");

                    break;
                }
                }
            }

            lastpostdate = get_lastpostdate(timezone);

            if (intval(lastpostdate) > intval(lastpostmodified)) {
                lastpostmodified = lastpostdate;
            }

            cache_lastpostmodified.getArrayValue(gVars.blog_id).putValue(timezone, lastpostmodified);
        } else {
            lastpostmodified = cache_lastpostmodified.getArrayValue(gVars.blog_id).getValue(timezone);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_lastpostmodified", lastpostmodified, timezone));
    }

    /**
     * update_post_cache() - Updates posts in cache
     * @usedby update_page_cache() update_page_cache() aliased by this function.
     *
     * @subpackage Cache
     * @since 1.5.1
     * @param array $posts Array of post objects
     */
    public void update_post_cache(Array<?> posts) {
        StdClass post = null;

        if (!booleanval(posts)) {
            return;
        }

        for (Map.Entry javaEntry553 : posts.entrySet()) {
            post = (StdClass) javaEntry553.getValue();
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(post, "ID"), post, "posts", 0);
        }
    }

    /**
     * clean_post_cache() - Will clean the post in the cache
     * Cleaning means delete from the cache of the post. Will call to clean the
     * term object cache associated with the post ID.
     *
     * @subpackage Cache
     * @since 2.0
     * @uses do_action() Will call the 'clean_post_cache' hook action.
     * @param int $id The Post ID in the cache to clean
     */
    public void clean_post_cache(Object id) {
        Array<Object> children = new Array<Object>();
        Object cid = null;
        id = intval(id);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(id, "posts");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(id, "post_meta");
        getIncluded(TaxonomyPage.class, gVars, gConsts).clean_object_term_cache(id, "post");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("wp_get_archives", "general");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("clean_post_cache", id);

        if (booleanval(children = gVars.wpdb.get_col(gVars.wpdb.prepare("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_parent = %d", id)))) {
            for (Map.Entry javaEntry554 : children.entrySet()) {
                cid = javaEntry554.getValue();
                clean_post_cache(cid);
            }
        }
    }

    /**
     * update_page_cache() - Alias of update_post_cache()
     * @see update_post_cache() Posts and pages are the same, alias is
     * intentional
     *
     * @subpackage Cache
     * @since 1.5.1
     * @param array $pages list of page objects
     */
    public void update_page_cache(Array pages) {
        update_post_cache(pages);
    }

    /**
     * clean_page_cache() - Will clean the page in the cache
     * Clean (read: delete) page from cache that matches $id. Will also clean
     * cache associated with 'all_page_ids' and 'get_pages'.
     *
     * @subpackage Cache
     * @since 2.0
     * @uses do_action() Will call the 'clean_page_cache' hook action.
     * @param int $id Page ID to clean
     */
    public void clean_page_cache(int id) {
        clean_post_cache(id);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("all_page_ids", "posts");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("get_pages", "posts");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("clean_page_cache", id);
    }

    /**
     * update_post_caches() - Call major cache updating functions for list of
     * Post objects.
     *
     * @subpackage Cache
     * @since 1.5
     * @uses $wpdb
     * @uses update_post_cache()
     * @uses update_object_term_cache()
     * @uses update_postmeta_cache()
     * @param array $posts Array of Post objects
     */
    public void update_post_caches(Array<StdClass> posts) {
        Array<Object> post_ids = new Array<Object>();
        int i = 0;

    	// No point in doing all this work if we didn't match any posts.
        if (!booleanval(posts)) {
            return;
        }

        update_post_cache(posts);
        post_ids = new Array<Object>();

        for (i = 0; i < Array.count(posts); i++)
            post_ids.putValue(posts.getValue(i).fields.getValue("ID"));

        getIncluded(TaxonomyPage.class, gVars, gConsts).update_object_term_cache(post_ids, "post");
        update_postmeta_cache(post_ids);
    }

    /**
     * update_postmeta_cache() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     *
     * @subpackage Cache
     * @since 2.1
     * @uses $wpdb
     * @param array $post_ids {@internal Missing Description}}
     * @return bool|array Returns false if there is nothing to update or an
     * array of metadata
     */
    public Array<Object> update_postmeta_cache(Object post_ids)/* Do not change type */
     {
        Array<String> ids;
        Object id = null;
        String id_list = null;
        Array<Object> cache = new Array<Object>();
        Array<Object> meta_list = new Array<Object>();
        int mpid = 0;
        Array<Object> metarow = new Array<Object>();
        Object mkey = null;
        Object mval = null;
        Object post = null;

        if (empty(post_ids)) {
            return new Array<Object>();
        }

        if (!is_array(post_ids)) {
            post_ids = QRegExPerl.preg_replace("|[^0-9,]|", "", strval(post_ids));
            post_ids = Strings.explode(",", strval(post_ids));
        }

        post_ids = Array.array_map(new Callback("intval", VarHandling.class), (Array) post_ids);
        ids = new Array<String>();

        for (Map.Entry javaEntry555 : new Array<Object>(post_ids).entrySet()) {
            id = javaEntry555.getValue();

            if (strictEqual(null, getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(id, "post_meta"))) {
                ids.putValue(id);
            }
        }

        if (empty(ids)) {
            return new Array<Object>();
        }

    	// Get post-meta info
        id_list = Strings.join(",", ids);
        cache = new Array<Object>();

        if (booleanval(
                    meta_list = gVars.wpdb.get_results(
                            "SELECT post_id, meta_key, meta_value FROM " + gVars.wpdb.postmeta + " WHERE post_id IN (" + id_list + ") ORDER BY post_id, meta_key",
                            gConsts.getARRAY_A()))) {
            for (Map.Entry javaEntry556 : new Array<Object>(meta_list).entrySet()) {
                metarow = (Array<Object>) javaEntry556.getValue();
                mpid = intval(metarow.getValue("post_id"));
                mkey = metarow.getValue("meta_key");
                mval = metarow.getValue("meta_value");

    			// Force subkeys to be array type:
                if (!isset(cache.getValue(mpid)) || !is_array(cache.getValue(mpid))) {
                    cache.putValue(mpid, new Array<Object>());
                }

                if (!isset(cache.getArrayValue(mpid).getValue(mkey)) || !is_array(cache.getArrayValue(mpid).getValue(mkey))) {
                    cache.getArrayValue(mpid).putValue(mkey, new Array<Object>());
                }

    			// Add a value to the current pid/key:
                cache.getArrayValue(mpid).getArrayValue(mkey).putValue(mval);
            }
        }

        for (Map.Entry javaEntry557 : new Array<Object>(ids).entrySet()) {
            id = javaEntry557.getValue();

            if (!isset(cache.getValue(id))) {
                cache.putValue(id, new Array<Object>());
            }
        }

        for (Map.Entry javaEntry558 : Array.array_keys(cache).entrySet()) {
            post = javaEntry558.getValue();
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set(post, cache.getValue(post), "post_meta", 0);
        }

        return cache;
    }

 //
 // Hooks
 //

 /**
  * _transition_post_status() - Hook {@internal Missing Short Description}}
  *
  * {@internal Missing Long Description}}
  *
  * @package WordPress
  * @subpackage Post
  * @since 2.3
  *
  * @uses $wpdb
  *
  * @param string $new_status {@internal Missing Description}}
  * @param string $old_status {@internal Missing Description}}
  * @param object $post Object type containing the post information
  */
    public void _transition_post_status(Object new_status, Object old_status, StdClass post) {
        if (!equal(old_status, "publish") && equal(new_status, "publish")) {
    		// Reset GUID if transitioning to publish and it is empty
            if (equal("", getIncluded(Post_templatePage.class, gVars, gConsts).get_the_guid(intval(StdClass.getValue(post, "ID"))))) {
                gVars.wpdb.update(
                    gVars.wpdb.posts,
                    new Array<Object>(new ArrayEntry<Object>("guid", getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(post, "ID"), false))),
                    new Array<Object>(new ArrayEntry<Object>("ID", StdClass.getValue(post, "ID"))));
            }

            getIncluded(PluginPage.class, gVars, gConsts).do_action("private_to_published", StdClass.getValue(post, "ID"));  // Deprecated, use private_to_publish
        }

        // Always clears the hook in case the post status bounced from future to draft.
        getIncluded(CronPage.class, gVars, gConsts).wp_clear_scheduled_hook("publish_future_post", StdClass.getValue(post, "ID"));
    }

    /**
     * _future_post_hook() - Hook used to schedule publication for a post
     * marked for the future.
     * The $post properties used and must exist are 'ID' and 'post_date_gmt'.
     *
     * @subpackage Post
     * @since 2.3
     * @param int $post_id Not Used. Can be set to null.
     * @param object $post Object type containing the post information
     */
    public void _future_post_hook(Object deprecated, StdClass post) {
        getIncluded(CronPage.class, gVars, gConsts).wp_clear_scheduled_hook("publish_future_post", StdClass.getValue(post, "ID"));
        getIncluded(CronPage.class, gVars, gConsts).wp_schedule_single_event(
            strval(QDateTime.strtotime(StdClass.getValue(post, "post_date_gmt") + " GMT")),
            "publish_future_post",
            new Array<Object>(new ArrayEntry<Object>(StdClass.getValue(post, "ID"))));
    }

    /**
     * _publish_post_hook() - Hook {@internal Missing Short Description}}{@internal Missing Long Description}}
     *
     * @subpackage Post
     * @since 2.3
     * @uses $wpdb
     * @uses XMLRPC_REQUEST
     * @uses APP_REQUEST
     * @uses do_action Calls 'xmlprc_publish_post' action if XMLRPC_REQUEST is
     * defined. Calls 'app_publish_post' action if APP_REQUEST is defined.
     * @param int $post_id The ID in the database table of the post being
     * published
     */
    public void _publish_post_hook(Object post_id) {
        Array<Object> data = new Array<Object>();

        if (gConsts.isXMLRPC_REQUESTDefined()) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_publish_post", post_id);
        }

        if (gConsts.isAPP_REQUESTDefined()) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("app_publish_post", post_id);
        }

        if (gConsts.isWP_IMPORTINGDefined()) {
            return;
        }

        data = new Array<Object>(new ArrayEntry<Object>("post_id", post_id), new ArrayEntry<Object>("meta_value", "1"));

        if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_pingback_flag"))) {
            gVars.wpdb.insert(gVars.wpdb.postmeta, Array.arrayAppend(data, new Array<Object>(new ArrayEntry<Object>("meta_key", "_pingme"))));
        }

        gVars.wpdb.insert(gVars.wpdb.postmeta, Array.arrayAppend(data, new Array<Object>(new ArrayEntry<Object>("meta_key", "_encloseme"))));
        getIncluded(CronPage.class, gVars, gConsts).wp_schedule_single_event(strval(DateTime.time()), "do_pings", new Array<Object>());
    }

    /**
     * _save_post_hook() - Hook used to prevent page/post cache and rewrite
     * rules from staying dirty
     * Does two things. If the post is a page and has a template then it will
     * update/add that template to the meta. For both pages and posts, it will
     * clean the post cache to make sure that the cache updates to the changes
     * done recently. For pages, the rewrite rules of WordPress are flushed to
     * allow for any changes.
     * The $post parameter, only uses 'post_type' property and 'page_template'
     * property.
     *
     * @subpackage Post
     * @since 2.3
     * @uses $wp_rewrite Flushes Rewrite Rules.
     * @param int $post_id The ID in the database table for the $post
     * @param object $post Object type containing the post information
     */
    public void _save_post_hook(int post_id, StdClass post) {
        if (equal(StdClass.getValue(post, "post_type"), "page")) {
            if (!empty(StdClass.getValue(post, "page_template"))) {
                if (!update_post_meta(post_id, "_wp_page_template", StdClass.getValue(post, "page_template"), "")) {
                    add_post_meta(post_id, "_wp_page_template", StdClass.getValue(post, "page_template"), true);
                }
            }

            clean_page_cache(post_id);
            gVars.wp_rewrite.flush_rules();
        } else {
            clean_post_cache(post_id);
        }
    }

 //
 // Private
 //
    public void _get_post_ancestors(StdClass _post) {
        // Added by Numiton
        if (!isset(_post)) {
            return;
        }

        Object id = null;
        Object ancestor = null;

        if (isset(StdClass.getValue(_post, "ancestors"))) {
            return;
        }

        Array<Object> ancestors = new Array<Object>();
        _post.fields.putValue("ancestors", ancestors);

        if (empty(StdClass.getValue(_post, "post_parent")) || equal(StdClass.getValue(_post, "ID"), StdClass.getValue(_post, "post_parent"))) {
            return;
        }

        id = ancestors.putValue(StdClass.getValue(_post, "post_parent"));

        while (booleanval(ancestor = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT `post_parent` FROM " + gVars.wpdb.posts + " WHERE ID = %d LIMIT 1", id)))) {
            if (equal(id, ancestor)) {
                break;
            }

            id = ancestors.putValue(ancestor);
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
