/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: wp_xmlrpc_server.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_admin.includes.ImagePage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.*;

import com.numiton.Misc;
import com.numiton.RegExPerl;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.PhpWeb;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class wp_xmlrpc_server extends IXR_Server implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(wp_xmlrpc_server.class.getName());

    public Array<Object> methods = new Array<Object>();
    public IXR_Error error;

    // Modified by Numiton
    public wp_xmlrpc_server(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants, initMethods(javaGlobalVariables, javaGlobalConstants));
        this.methods = initMethods(javaGlobalVariables, javaGlobalConstants);
    }

    @SuppressWarnings("unchecked")
    private static Array<Object> initMethods(GlobalVars gVars, GlobalConsts gConsts) {
        Array<Object> methods = new Array<Object>(
            // WordPress API
            new ArrayEntry<Object>("wp.getPage", "this:wp_getPage"), new ArrayEntry<Object>("wp.getPages", "this:wp_getPages"), new ArrayEntry<Object>("wp.newPage", "this:wp_newPage"),
                new ArrayEntry<Object>("wp.deletePage", "this:wp_deletePage"), new ArrayEntry<Object>("wp.editPage", "this:wp_editPage"),
                new ArrayEntry<Object>("wp.getPageList", "this:wp_getPageList"), new ArrayEntry<Object>("wp.getAuthors", "this:wp_getAuthors"),
                new ArrayEntry<Object>("wp.getCategories", "this:mw_getCategories") /*Alias*/, new ArrayEntry<Object>("wp.newCategory", "this:wp_newCategory"),
                new ArrayEntry<Object>("wp.deleteCategory", "this:wp_deleteCategory"), new ArrayEntry<Object>("wp.suggestCategories", "this:wp_suggestCategories"),
                new ArrayEntry<Object>("wp.uploadFile", "this:mw_newMediaObject"), /*Alias*/
                new ArrayEntry<Object>("wp.getCommentCount", "this:wp_getCommentCount"), new ArrayEntry<Object>("wp.getPostStatusList", "this:wp_getPostStatusList"),
                new ArrayEntry<Object>("wp.getPageStatusList", "this:wp_getPageStatusList"), 
            // Blogger API
            new ArrayEntry<Object>("blogger.getUsersBlogs", "this:blogger_getUsersBlogs"), new ArrayEntry<Object>("blogger.getUserInfo", "this:blogger_getUserInfo"),
                new ArrayEntry<Object>("blogger.getPost", "this:blogger_getPost"), new ArrayEntry<Object>("blogger.getRecentPosts", "this:blogger_getRecentPosts"),
                new ArrayEntry<Object>("blogger.getTemplate", "this:blogger_getTemplate"), new ArrayEntry<Object>("blogger.setTemplate", "this:blogger_setTemplate"),
                new ArrayEntry<Object>("blogger.newPost", "this:blogger_newPost"), new ArrayEntry<Object>("blogger.editPost", "this:blogger_editPost"),
                new ArrayEntry<Object>("blogger.deletePost", "this:blogger_deletePost"), 
            // MetaWeblog API (with MT extensions to structs)
            new ArrayEntry<Object>("metaWeblog.newPost", "this:mw_newPost"), new ArrayEntry<Object>("metaWeblog.editPost", "this:mw_editPost"),
                new ArrayEntry<Object>("metaWeblog.getPost", "this:mw_getPost"), new ArrayEntry<Object>("metaWeblog.getRecentPosts", "this:mw_getRecentPosts"),
                new ArrayEntry<Object>("metaWeblog.getCategories", "this:mw_getCategories"), new ArrayEntry<Object>("metaWeblog.newMediaObject", "this:mw_newMediaObject"),
                
            // MetaWeblog API aliases for Blogger API
            // see http://www.xmlrpc.com/stories/storyReader$2460
            new ArrayEntry<Object>("metaWeblog.deletePost", "this:blogger_deletePost"), new ArrayEntry<Object>("metaWeblog.getTemplate", "this:blogger_getTemplate"),
                new ArrayEntry<Object>("metaWeblog.setTemplate", "this:blogger_setTemplate"), new ArrayEntry<Object>("metaWeblog.getUsersBlogs", "this:blogger_getUsersBlogs"),
                
            // MovableType API
            new ArrayEntry<Object>("mt.getCategoryList", "this:mt_getCategoryList"), new ArrayEntry<Object>("mt.getRecentPostTitles", "this:mt_getRecentPostTitles"),
                new ArrayEntry<Object>("mt.getPostCategories", "this:mt_getPostCategories"), new ArrayEntry<Object>("mt.setPostCategories", "this:mt_setPostCategories"),
                new ArrayEntry<Object>("mt.supportedMethods", "this:mt_supportedMethods"), new ArrayEntry<Object>("mt.supportedTextFilters", "this:mt_supportedTextFilters"),
                new ArrayEntry<Object>("mt.getTrackbackPings", "this:mt_getTrackbackPings"), new ArrayEntry<Object>("mt.publishPost", "this:mt_publishPost"),
                
            // PingBack
            new ArrayEntry<Object>("pingback.ping", "this:pingback_ping"), new ArrayEntry<Object>("pingback.extensions.getPingbacks", "this:pingback_extensions_getPingbacks"),
                new ArrayEntry<Object>("demo.sayHello", "this:sayHello"), new ArrayEntry<Object>("demo.addTwoNumbers", "this:addTwoNumbers"));

        methods = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("xmlrpc_methods", methods);

        return methods;
    }

    public String sayHello(Object args) {
        return "Hello!";
    }

    public int addTwoNumbers(Array<Object> args) {
        Object number1 = null;
        Object number2 = null;
        number1 = args.getValue(0);
        number2 = args.getValue(1);

        return intval(number1) + intval(number2);
    }

    public boolean login_pass_ok(String user_login, String user_pass) {
        if (!getIncluded(UserPage.class, gVars, gConsts).user_pass_ok(user_login, user_pass)) {
            this.error = new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("Bad login/pass combination.", "default"));

            return false;
        }

        return true;
    }

    public String escape(Object array) /* Do not change type */ {
        Object v = null;

        /* Do not change type */
        Object k = null;

        if (!is_array(array)) {
            return gVars.wpdb.escape(strval(array));
        } else {
            for (Map.Entry javaEntry687 : new Array<Object>(array).entrySet()) {
                k = javaEntry687.getKey();
                v = javaEntry687.getValue();

                if (is_array(v)) {
                    this.escape(((Array) array).getValue(k));
                } else if (is_object(v)) {
                } else /*
                * skip skip
                */
                 {
                    ((Array) array).putValue(k, gVars.wpdb.escape(strval(v)));
                }
            }
        }

        return "";
    }

    public Array<Object> get_custom_fields(int post_id) {
        Array<Object> custom_fields = new Array<Object>();
        Array<Object> meta = new Array<Object>();
        post_id = post_id;
        custom_fields = new Array<Object>();

        for (Map.Entry javaEntry688 : new Array<Object>(getIncluded(PostPage.class, gVars, gConsts).has_meta(post_id)).entrySet()) {
            meta = (Array<Object>) javaEntry688.getValue();

            // Don't expose protected fields.
            if (strictEqual(Strings.strpos(strval(meta.getValue("meta_key")), "_wp_"), 0)) {
                continue;
            }

            custom_fields.putValue(
                new Array<Object>(
                    new ArrayEntry<Object>("id", meta.getValue("meta_id")),
                    new ArrayEntry<Object>("key", meta.getValue("meta_key")),
                    new ArrayEntry<Object>("value", meta.getValue("meta_value"))));
        }

        return custom_fields;
    }

    public void set_custom_fields(int post_id, Object fields) {
        Array<Object> meta = new Array<Object>();
        post_id = post_id;

        for (Map.Entry javaEntry689 : new Array<Object>(fields).entrySet()) {
            meta = (Array<Object>) javaEntry689.getValue();

            if (isset(meta.getValue("id"))) {
                meta.putValue("id", intval(meta.getValue("id")));

                if (isset(meta.getValue("key"))) {
                    getIncluded(PostPage.class, gVars, gConsts).update_meta(intval(meta.getValue("id")), strval(meta.getValue("key")), strval(meta.getValue("value")));
                } else {
                    getIncluded(PostPage.class, gVars, gConsts).delete_meta(intval(meta.getValue("id")));
                }
            } else {
                gVars.webEnv._POST.putValue("metakeyinput", meta.getValue("key"));
                gVars.webEnv._POST.putValue("metavalue", meta.getValue("value"));
                getIncluded(PostPage.class, gVars, gConsts).add_meta(post_id);
            }
        }
    }

    /**
         * WordPress XML-RPC API
         * wp_getPage
         */
    public Object wp_getPage(Array<Object> args) {
        int blog_id = 0;
        int page_id = 0;
        Object username = null;
        Object password = null;
        StdClass page = null;
        Array<Object> full_page = new Array<Object>();
        String link;
        String parent_title = null;
        StdClass parent = null;
        int allow_comments = 0;
        int allow_pings = 0;
        String page_date = null;
        String page_date_gmt = null;
        Array<Object> categories = new Array<Object>();
        int cat_id = 0;
        StdClass author;
        Array<Object> page_struct = new Array<Object>();

        this.escape(args);

        blog_id = intval(args.getValue(0));
        page_id = intval(args.getValue(1));
        username = args.getValue(2);
        password = args.getValue(3);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", page_id)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit this page.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getPage");

        // Lookup page info.
        page = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_page(page_id, gConsts.getOBJECT(), "raw");

        // If we found the page then format the data.
        if (booleanval(StdClass.getValue(page, "ID")) && equal(StdClass.getValue(page, "post_type"), "page")) {
            // Get all of the page content and link.
            full_page = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_extended(strval(StdClass.getValue(page, "post_content")));
            link = getIncluded(Link_templatePage.class, gVars, gConsts).post_permalink(StdClass.getValue(page, "ID"), "");

            // Get info the page parent if there is one.
            parent_title = "";

            if (!empty(StdClass.getValue(page, "post_parent"))) {
                parent = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_page(
                        intval(StdClass.getValue(page, "post_parent")),
                        gConsts.getOBJECT(),
                        "raw");
                parent_title = strval(StdClass.getValue(parent, "post_title"));
            }

            // Determine comment and ping settings.
            allow_comments = (equal("open", StdClass.getValue(page, "comment_status"))
                ? 1
                : 0);
            allow_pings = (equal("open", StdClass.getValue(page, "ping_status"))
                ? 1
                : 0);

            // Format page date.
            page_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(StdClass.getValue(page, "post_date")), true);
            page_date_gmt = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(StdClass.getValue(page, "post_date_gmt")), true);

            // Pull the categories info together.
            categories = new Array<Object>();

            for (Map.Entry javaEntry690 : ((Array<?>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(
                        intval(StdClass.getValue(page, "ID")),
                        new Array<Object>())).entrySet()) {
                cat_id = intval(javaEntry690.getValue());
                categories.putValue(getIncluded(CategoryPage.class, gVars, gConsts).get_cat_name(cat_id));
            }

            // Get the author info.
            author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(page, "post_author")));
            page_struct = new Array<Object>(
                    new ArrayEntry<Object>("dateCreated", new IXR_Date(gVars, gConsts, page_date)),
                    new ArrayEntry<Object>("userid", StdClass.getValue(page, "post_author")),
                    new ArrayEntry<Object>("page_id", StdClass.getValue(page, "ID")),
                    new ArrayEntry<Object>("page_status", StdClass.getValue(page, "post_status")),
                    new ArrayEntry<Object>("description", full_page.getValue("main")),
                    new ArrayEntry<Object>("title", StdClass.getValue(page, "post_title")),
                    new ArrayEntry<Object>("link", link),
                    new ArrayEntry<Object>("permaLink", link),
                    new ArrayEntry<Object>("categories", categories),
                    new ArrayEntry<Object>("excerpt", StdClass.getValue(page, "post_excerpt")),
                    new ArrayEntry<Object>("text_more", full_page.getValue("extended")),
                    new ArrayEntry<Object>("mt_allow_comments", allow_comments),
                    new ArrayEntry<Object>("mt_allow_pings", allow_pings),
                    new ArrayEntry<Object>("wp_slug", StdClass.getValue(page, "post_name")),
                    new ArrayEntry<Object>("wp_password", StdClass.getValue(page, "post_password")),
                    new ArrayEntry<Object>("wp_author", StdClass.getValue(author, "display_name")),
                    new ArrayEntry<Object>("wp_page_parent_id", StdClass.getValue(page, "post_parent")),
                    new ArrayEntry<Object>("wp_page_parent_title", parent_title),
                    new ArrayEntry<Object>("wp_page_order", StdClass.getValue(page, "menu_order")),
                    new ArrayEntry<Object>("wp_author_id", StdClass.getValue(author, "ID")),
                    new ArrayEntry<Object>("wp_author_display_name", StdClass.getValue(author, "display_name")),
                    new ArrayEntry<Object>("date_created_gmt", new IXR_Date(gVars, gConsts, page_date_gmt)),
                    new ArrayEntry<Object>("custom_fields", this.get_custom_fields(page_id)));

            return page_struct;
        }
        // If the page doesn't exist indicate that.
        else {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such page.", "default"));
        }
    }

    /**
         * WordPress XML-RPC API
          * wp_getPages
         */
    public Object wp_getPages(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        Array<StdClass> pages = new Array<StdClass>();
        int num_pages = 0;
        Array<Object> pages_struct = new Array<Object>();
        Object page = null;
        int i = 0;

        this.escape(args);

        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_pages")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit pages.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getPages");

        // Lookup info on pages.
        pages = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_pages("");
        num_pages = Array.count(pages);

        // If we have pages, put together their info.
        if (num_pages >= 1) {
            pages_struct = new Array<Object>();

            for (i = 0; i < num_pages; i++) {
                page = gVars.getSharedwp_xmlrpc_server().wp_getPage(
                        new Array<Object>(
                            new ArrayEntry<Object>(blog_id),
                            new ArrayEntry<Object>(pages.getValue(i).fields.getValue("ID")),
                            new ArrayEntry<Object>(username),
                            new ArrayEntry<Object>(password)));
                pages_struct.putValue(page);
            }

            return pages_struct;
        }
        // If no pages were found return an error.
        else {
            return new Array<Object>();
        }
    }

    /**
         * WordPress XML-RPC API
          * wp_newPage
         */
    public Object wp_newPage(Array<Object> args) {
        String username = null;
        String password = null;
        Object page = null;
        Object publish = null;
        WP_User user = null;

        // Items not escaped here will be escaped in newPost.
        username = this.escape(args.getValue(1));
        password = this.escape(args.getValue(2));
        page = args.getValue(3);
        publish = args.getValue(4);

        if (!this.login_pass_ok(username, password)) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.newPage");

        // Set the user context and check if they are allowed
        // to add new pages.
        user = getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, username);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_pages")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not add new pages.", "default"));
        }

        // Mark this as content for a page.
        args.getArrayValue(3).putValue("post_type", "page");

        // Let mw_newPost do all of the heavy lifting.
        return this.mw_newPost(args);
    }

    /**
         * WordPress XML-RPC API
         * wp_deletePage
         */
    public Object wp_deletePage(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        int page_id = 0;
        Array<Object> actual_page = new Array<Object>();
        StdClass result;

        this.escape(args);

        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);
        page_id = intval(args.getValue(3));

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.deletePage");

        // Get the current page based on the page_id and
        // make sure it is a page and not a post.
        actual_page = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(page_id, gConsts.getARRAY_A());

        if (!booleanval(actual_page) || !equal(actual_page.getValue("post_type"), "page")) {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such page.", "default"));
        }

        // Set the user context and make sure they can delete pages.
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_page", page_id)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to delete this page.", "default"));
        }

        // Attempt to delete the page.
        result = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_delete_post(page_id);

        if (!booleanval(result)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Failed to delete the page.", "default"));
        }

        return true;
    }

    /**
         * WordPress XML-RPC API
         * wp_editPage
         */
    public Object wp_editPage(Array<Object> args) {
        int blog_id = 0;
        int page_id = 0;
        String username = null;
        String password = null;
        Array<Object> content = new Array<Object>();
        Object publish = null;
        Array<Object> actual_page = new Array<Object>();

        // Items not escaped here will be escaped in editPost.
        blog_id = intval(args.getValue(0));
        page_id = intval(this.escape(args.getValue(1)));
        username = this.escape(args.getValue(2));
        password = this.escape(args.getValue(3));
        content = args.getArrayValue(4);
        publish = args.getValue(5);

        if (!this.login_pass_ok(username, password)) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.editPage");

        // Get the page data and make sure it is a page.
        actual_page = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(page_id, gConsts.getARRAY_A());

        if (!booleanval(actual_page) || !equal(actual_page.getValue("post_type"), "page")) {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such page.", "default"));
        }

        // Set the user context and make sure they are allowed to edit pages.
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, username);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", page_id)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit this page.", "default"));
        }

        // Mark this as content for a page.
        content.putValue("post_type", "page");

        // Arrange args in the way mw_editPost understands.
        args = new Array<Object>(new ArrayEntry<Object>(page_id), new ArrayEntry<Object>(username), new ArrayEntry<Object>(password), new ArrayEntry<Object>(content), new ArrayEntry<Object>(publish));

        // Let mw_editPost do all of the heavy lifting.
        return this.mw_editPost(args);
    }

    /**
	 * WordPress XML-RPC API
	 * wp_getPageList
	 */
    public Object wp_getPageList(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        Array<StdClass> page_list = new Array<StdClass>();
        int num_pages = 0;
        String post_date = null;
        int i = 0;
        String post_date_gmt = null;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_pages")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit pages.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getPageList");
        
        // Get list of pages ids and titles
        page_list = gVars.wpdb.get_results(
                    "\n\t\t\tSELECT ID page_id,\n\t\t\t\tpost_title page_title,\n\t\t\t\tpost_parent page_parent_id,\n\t\t\t\tpost_date_gmt,\n\t\t\t\tpost_date\n\t\t\tFROM " + gVars.wpdb.posts +
                    "\n\t\t\tWHERE post_type = \'page\'\n\t\t\tORDER BY ID\n\t\t");
        
        // The date needs to be formated properly.
        num_pages = Array.count(page_list);

        for (i = 0; i < num_pages; i++) {
            post_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(page_list.getValue(i).fields.getValue("post_date")), true);
            post_date_gmt = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(page_list.getValue(i).fields.getValue("post_date_gmt")), true);
            
            page_list.getValue(i).fields.putValue("dateCreated", new IXR_Date(gVars, gConsts, post_date));
            page_list.getValue(i).fields.putValue("date_created_gmt", new IXR_Date(gVars, gConsts, post_date_gmt));
            
            page_list.getValue(i).fields.putValue("post_date_gmt", null);
            page_list.getValue(i).fields.putValue("post_date", null);
        }

        return page_list;
    }

    /**
	 * WordPress XML-RPC API
	 * wp_getAuthors
	 */
    public Object wp_getAuthors(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        Array<Object> authors = new Array<Object>();
        StdClass row = null;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit posts on this blog.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getAuthors");
        authors = new Array<Object>();

        for (Map.Entry javaEntry691 : new Array<Object>(getIncluded(UserPage.class, gVars, gConsts).get_users_of_blog(intval(""))).entrySet()) {
            row = (StdClass) javaEntry691.getValue();
            authors.putValue(
                new Array<Object>(
                    new ArrayEntry<Object>("user_id", StdClass.getValue(row, "user_id")),
                    new ArrayEntry<Object>("user_login", StdClass.getValue(row, "user_login")),
                    new ArrayEntry<Object>("display_name", StdClass.getValue(row, "display_name"))));
        }

        return authors;
    }

    /**
	 * WordPress XML-RPC API
	 * wp_newCategory
	 */
    public Object wp_newCategory(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        Array<Object> category = new Array<Object>();
        Array<Object> new_category = new Array<Object>();
        Object cat_id = null;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);
        category = args.getArrayValue(3);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.newCategory");
        
        // Set the user context and make sure they are
		// allowed to add a category.
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to add a category.", "default"));
        }

        // If no slug was provided make it empty so that
		// WordPress will generate one.
        if (empty(category.getValue("slug"))) {
            category.putValue("slug", "");
        }

        // If no parent_id was provided make it empty
		// so that it will be a top level page (no parent).
        if (!isset(category.getValue("parent_id"))) {
            category.putValue("parent_id", "");
        }

        // If no description was provided make it empty.
        if (empty(category.getValue("description"))) {
            category.putValue("description", "");
        }

        new_category = new Array<Object>(
                new ArrayEntry<Object>("cat_name", category.getValue("name")),
                new ArrayEntry<Object>("category_nicename", category.getValue("slug")),
                new ArrayEntry<Object>("category_parent", category.getValue("parent_id")),
                new ArrayEntry<Object>("category_description", category.getValue("description")));
        
        cat_id = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_category(new_category, false);

        if (!booleanval(cat_id)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, the new category failed.", "default"));
        }

        return cat_id;
    }

    /**
	 * WordPress XML-RPC API
	 * wp_deleteCategory
	 */
    public Object wp_deleteCategory(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        int category_id = 0;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);
        category_id = intval(args.getValue(3));

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.deleteCategory");
        
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to delete a category.", "default"));
        }

        return getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_category(category_id);
    }

    /**
	 * WordPress XML-RPC API
	 * wp_suggestCategories
	 */
    public Object wp_suggestCategories(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        Object category = null;
        int max_results = 0;
        Array<Object> category_suggestions = new Array<Object>();
        StdClass cat = null;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);
        category = args.getValue(3);
        max_results = intval(args.getValue(4));

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you must be able to edit posts to this blog in order to view categories.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.suggestCategories");
        
        category_suggestions = new Array<Object>();
        args = new Array<Object>(new ArrayEntry<Object>("get", "all"), new ArrayEntry<Object>("number", max_results), new ArrayEntry<Object>("name__like", category));

        for (Map.Entry javaEntry692 : new Array<Object>(getIncluded(CategoryPage.class, gVars, gConsts).get_categories(args)).entrySet()) {
            cat = (StdClass) javaEntry692.getValue();
            category_suggestions.putValue(
                new Array<Object>(new ArrayEntry<Object>("category_id", StdClass.getValue(cat, "cat_ID")), new ArrayEntry<Object>("category_name", StdClass.getValue(cat, "cat_name"))));
        }

        return category_suggestions;
    }

    public Object wp_getCommentCount(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        int post_id = 0;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);
        post_id = intval(args.getValue(3));

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("Bad login/pass combination.", "default"));
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed access to details about comments.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getCommentCount");

        // TODO Is this the right get_comment_count() call target?
        return getIncluded(CommentPage.class, gVars, gConsts).get_comment_count(post_id);
    }

    public Object wp_getPostStatusList(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("Bad login/pass combination.", "default"));
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed access to details about this blog.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getPostStatusList");

        return (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post_statuses();
    }

    public Object wp_getPageStatusList(Array<Object> args) {
        int blog_id = 0;
        Object username = null;
        Object password = null;
        
        this.escape(args);
        
        blog_id = intval(args.getValue(0));
        username = args.getValue(1);
        password = args.getValue(2);

        if (!this.login_pass_ok(strval(username), strval(password))) {
            return new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("Bad login/pass combination.", "default"));
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(username));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 403, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed access to details about this blog.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "wp.getPageStatusList");

        return (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_page_statuses();
    }

    /* Blogger API functions
	 * specs on http://plant.blogger.com/api and http://groups.yahoo.com/group/bloggerDev/
	 */


	/* blogger.getUsersBlogs will make more sense once we support multiple blogs */
    public Object blogger_getUsersBlogs(Array<Object> args) {
        Object user_login = null;
        Object user_pass = null;
        Object is_admin = null;
        Array<Object> struct = new Array<Object>();
        
        this.escape(args);
        
        user_login = args.getValue(1);
        user_pass = args.getValue(2);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.getUsersBlogs");
        
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));
        is_admin = getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("level_8");
        
        struct = new Array<Object>(
                new ArrayEntry<Object>("isAdmin", is_admin),
                new ArrayEntry<Object>("url", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")) + "/"),
                new ArrayEntry<Object>("blogid", "1"),
                new ArrayEntry<Object>("blogName", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")));

        return new Array<Object>(new ArrayEntry<Object>(struct));
    }

    /* blogger.getUsersInfo gives your client some info about you, so you don't have to */
    public Object blogger_getUserInfo(Array<Object> args) {
        String user_login = null;
        String user_pass = null;
        StdClass user_data;
        Array<Object> struct = new Array<Object>();
        
        this.escape(args);
        
        user_login = strval(args.getValue(1));
        user_pass = strval(args.getValue(2));

        if (!this.login_pass_ok(user_login, user_pass)) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, user_login);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have access to user data on this blog.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.getUserInfo");
        
        user_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(user_login);
        
        struct = new Array<Object>(
                new ArrayEntry<Object>("nickname", StdClass.getValue(user_data, "nickname")),
                new ArrayEntry<Object>("userid", StdClass.getValue(user_data, "ID")),
                new ArrayEntry<Object>("url", StdClass.getValue(user_data, "user_url")),
                new ArrayEntry<Object>("lastname", StdClass.getValue(user_data, "last_name")),
                new ArrayEntry<Object>("firstname", StdClass.getValue(user_data, "first_name")));

        return struct;
    }

    /**
     * blogger.getPost ...gets a post
     */
    public Object blogger_getPost(Array<Object> args) {
        int post_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> post_data = new Array<Object>();
        String categories = null;
        String content = null;
        Array<Object> struct = new Array<Object>();
        
        this.escape(args);
        
        post_ID = intval(args.getValue(1));
        user_login = args.getValue(2);
        user_pass = args.getValue(3);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit this post.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.getPost");
        
        post_data = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());
        
        categories = Strings.implode(
                ",",
                (Array<String>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(post_ID, new Array<Object>()));
        
        content = "<title>" + Strings.stripslashes(gVars.webEnv, strval(post_data.getValue("post_title"))) + "</title>";
        content = content + "<category>" + categories + "</category>";
        content = content + Strings.stripslashes(gVars.webEnv, strval(post_data.getValue("post_content")));
        
        struct = new Array<Object>(
                new ArrayEntry<Object>("userid", post_data.getValue("post_author")),
                new ArrayEntry<Object>(
                    "dateCreated",
                    new IXR_Date(gVars, gConsts, (((FunctionsPage) PhpWeb.getIncluded(FunctionsPage.class, gVars, gConsts))).mysql2date("Ymd\\TH:i:s", strval(post_data.getValue("post_date")), true))),
                new ArrayEntry<Object>("content", content),
                new ArrayEntry<Object>("postid", post_data.getValue("ID")));

        return struct;
    }

    /**
     * blogger.getRecentPosts ...gets recent posts
     */
    public Object blogger_getRecentPosts(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        int num_posts;
        Array<Object> posts_list = new Array<Object>();
        Array<Object> entry = new Array<Object>();
        String post_date = null;
        String categories = null;
        String content = null;
        Array<Object> struct = new Array<Object>();
        Array<Object> recent_posts = new Array<Object>();
        int j = 0;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(1)); /* though we don't use it yet */
        user_login = args.getValue(2);
        user_pass = args.getValue(3);
        num_posts = intval(args.getValue(4));

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.getRecentPosts");
        
        posts_list = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_recent_posts(num_posts);
        
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!booleanval(posts_list)) {
            this.error = new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Either there are no posts, or something went wrong.", "default"));

            return this.error;
        }

        for (Map.Entry javaEntry693 : posts_list.entrySet()) {
            entry = (Array<Object>) javaEntry693.getValue();

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", entry.getValue("ID"))) {
                continue;
            }

            post_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(entry.getValue("post_date")), true);
            categories = Strings.implode(
                    ",",
                    (Array<String>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(
                        intval(entry.getValue("ID")),
                        new Array<Object>()));
            
            content = "<title>" + Strings.stripslashes(gVars.webEnv, strval(entry.getValue("post_title"))) + "</title>";
            content = content + "<category>" + categories + "</category>";
            content = content + Strings.stripslashes(gVars.webEnv, strval(entry.getValue("post_content")));
            
            struct.putValue(
                new Array<Object>(
                    new ArrayEntry<Object>("userid", entry.getValue("post_author")),
                    new ArrayEntry<Object>("dateCreated", new IXR_Date(gVars, gConsts, post_date)),
                    new ArrayEntry<Object>("content", content),
                    new ArrayEntry<Object>("postid", entry.getValue("ID"))));
        }

        recent_posts = new Array<Object>();

        for (j = 0; j < Array.count(struct); j++) {
            Array.array_push(recent_posts, struct.getValue(j));
        }

        return recent_posts;
    }

    /**
     * blogger.getTemplate returns your blog_filename
     */
    public Object blogger_getTemplate(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Object template = null;
        String filename = null;
        int f = 0;
        String content = null;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(1));
        user_login = args.getValue(2);
        user_pass = args.getValue(3);
        template = args.getValue(4); /* could be 'main' or 'archiveIndex', but we don't use it */

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.getTemplate");
        
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_themes")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, this user can not edit the template.", "default"));
        }

        /* warning: here we make the assumption that the blog's URL is on the same server */
        filename = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")) + "/";
        filename = QRegExPerl.preg_replace("#https?://.+?/#", gVars.webEnv.getDocumentRoot() + "/", filename);
        
        f = FileSystemOrSocket.fopen(gVars.webEnv, filename, "r");
        content = FileSystemOrSocket.fread(gVars.webEnv, f, FileSystemOrSocket.filesize(gVars.webEnv, filename));
        FileSystemOrSocket.fclose(gVars.webEnv, f);

        /* so it is actually editable with a windows/mac client */
  	  	// FIXME: (or delete me) do we really want to cater to bad clients at the expense of good ones by BEEPing up their line breaks? commented.     $content = str_replace("\n", "\r\n", $content);
        
        return content;
    }

    /**
     * blogger.setTemplate updates the content of blog_filename
     */
    public Object blogger_setTemplate(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        String content = null;
        Object template = null;
        String filename = null;
        int f = 0;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(1));
        user_login = args.getValue(2);
        user_pass = args.getValue(3);
        content = strval(args.getValue(4));
        template = args.getValue(5); /* could be 'main' or 'archiveIndex', but we don't use it */

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.setTemplate");
        
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_themes")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, this user can not edit the template.", "default"));
        }

        /* warning: here we make the assumption that the blog's URL is on the same server */
        filename = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")) + "/";
        filename = QRegExPerl.preg_replace("#https?://.+?/#", gVars.webEnv.getDocumentRoot() + "/", filename);

        if (booleanval(f = FileSystemOrSocket.fopen(gVars.webEnv, filename, "w+"))) {
            FileSystemOrSocket.fwrite(gVars.webEnv, f, content);
            FileSystemOrSocket.fclose(gVars.webEnv, f);
        } else {
            return new IXR_Error(gVars, gConsts, 500,
                getIncluded(L10nPage.class, gVars, gConsts).__("Either the file is not writable, or something wrong happened. The file has not been updated.", "default"));
        }

        return true;
    }

    /**
     * blogger.newPost ...creates a new post
     */
    public Object blogger_newPost(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        String content = null;
        Object publish = null;
        String cap = null;
        WP_User user = null;
        String post_status = null;
        int post_author = 0;
        String post_title = null;
        Object post_category = null;
        String post_content = null;
        Object post_date = null;
        Object post_date_gmt = null;
        Array<Object> post_data = new Array<Object>();
        Object post_ID;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(1)); /* though we don't use it yet */
        user_login = args.getValue(2);
        user_pass = args.getValue(3);
        content = strval(args.getValue(4));
        publish = args.getValue(5);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.newPost");
        
        cap = (booleanval(publish) ? "publish_posts" : "edit_posts");
        user = getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(cap)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you are not allowed to post on this blog.", "default"));
        }

        post_status = (booleanval(publish) ? "publish" : "draft");
        
        post_author = user.getID();
        
        post_title = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_getposttitle(content);
        post_category = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_getpostcategory(content);
        post_content = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_removepostdata(content);
        
        post_date = getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0);
        post_date_gmt = getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1);
        
        post_data = Array.compact(
                new ArrayEntry("blog_ID", blog_ID),
                new ArrayEntry("post_author", post_author),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status));
        
        post_ID = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(post_data);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_ID)) {
            return new IXR_Error(gVars, gConsts, 500, ((WP_Error) post_ID).get_error_message());
        }

        if (!booleanval(post_ID)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, your entry could not be posted. Something wrong happened.", "default"));
        }

        this.attach_uploads(intval(post_ID), post_content);
        
        getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "Posted ! ID: " + strval(post_ID));

        return post_ID;
    }

    /**
     * blogger.editPost ...edits a post
     */
    public Object blogger_editPost(Array<Object> args) {
        Integer post_ID = null;
        String user_login = null;
        String user_pass = null;
        String content = null;
        Object publish = null;
        Array<Object> actual_post = new Array<Object>();
        Object post_status = null;
        Object post_excerpt = null;
        String post_title = null;
        Object post_category = null;
        String post_content = null;
        Array<Object> postdata = new Array<Object>();
        Integer result = null;
        Object ID = null;
        
        this.escape(args);
        
        post_ID = intval(args.getValue(1));
        user_login = strval(args.getValue(2));
        user_pass = strval(args.getValue(3));
        content = strval(args.getValue(4));
        publish = args.getValue(5);

        if (!this.login_pass_ok(user_login, user_pass)) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.editPost");
        
        actual_post = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());

        if (!booleanval(actual_post)) {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such post.", "default"));
        }

        this.escape(actual_post);
        
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, user_login);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit this post.", "default"));
        }

        post_status = Array.extractVar(actual_post, "post_status", post_status, Array.EXTR_SKIP);
        post_excerpt = Array.extractVar(actual_post, "post_excerpt", post_excerpt, Array.EXTR_SKIP);
        ID = Array.extractVar(actual_post, "ID", ID, Array.EXTR_SKIP);

        if (equal("publish", post_status) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to publish this post.", "default"));
        }

        post_title = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_getposttitle(content);
        post_category = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_getpostcategory(content);
        post_content = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_removepostdata(content);
        
        postdata = Array.compact(
                new ArrayEntry("ID", ID),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_excerpt", post_excerpt));
        
        result = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_post(postdata);

        if (!booleanval(result)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("For some strange yet very annoying reason, this post could not be edited.", "default"));
        }

        this.attach_uploads(intval(ID), post_content);

        return true;
    }

    /**
     * blogger.deletePost ...deletes a post
     */
    public Object blogger_deletePost(Array<Object> args) {
        int post_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Object publish = null;
        Array<Object> actual_post = new Array<Object>();
        StdClass result;
        
        this.escape(args);
        
        post_ID = intval(args.getValue(1));
        user_login = args.getValue(2);
        user_pass = args.getValue(3);
        publish = args.getValue(4);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "blogger.deletePost");
        
        actual_post = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());

        if (!booleanval(actual_post)) {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such post.", "default"));
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to delete this post.", "default"));
        }

        result = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_delete_post(post_ID);

        if (!booleanval(result)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("For some strange yet very annoying reason, this post could not be deleted.", "default"));
        }

        return true;
    }

    /* MetaWeblog API functions
	 * specs on wherever Dave Winer wants them to be
	 */

	/* metaweblog.newPost creates a post */
    public Object mw_newPost(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> content_struct = new Array<Object>();
        Object publish = null;
        WP_User user = null;
        String cap = null;
        String error_message = null;
        String post_type = null;
        String post_name = null;
        Object post_password = null;
        Object post_parent = null;
        Object menu_order = null;
        int post_author = 0;
        Object post_title = null;
        String post_content = null;
        String post_status = null;
        Object post_excerpt = null;
        Object post_more = null;
        Object tags_input = null;
        Object comment_status = null;
        Object ping_status = null;
        Object to_ping = null;

        /* Do not change type */
        String dateCreated = null;
        String post_date = null;
        String post_date_gmt = null;
        Object catnames = null;

        /* Do not change type */
        Array<Object> post_category = new Array<Object>();
        int cat;
        Array<Object> postdata = new Array<Object>();
        Object post_ID;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(0)); // we will support this in the near future
        user_login = args.getValue(1);
        user_pass = args.getValue(2);
        content_struct = args.getArrayValue(3);
        publish = args.getValue(4);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        user = getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "metaWeblog.newPost");
        
        cap = (booleanval(publish)
            ? "publish_posts"
            : "edit_posts");
        error_message = getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you are not allowed to publish posts on this blog.", "default");
        post_type = "post";

        if (!empty(content_struct.getValue("post_type"))) {
            if (equal(content_struct.getValue("post_type"), "page")) {
                cap = (booleanval(publish)
                    ? "publish_pages"
                    : "edit_pages");
                error_message = getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you are not allowed to publish pages on this blog.", "default");
                post_type = "page";
            } else if (equal(content_struct.getValue("post_type"), "post")) {
            	// This is the default, no changes needed
            } else {
            	// No other post_type values are allowed here
                return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid post type.", "default"));
            }
        }

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(cap)) {
            return new IXR_Error(gVars, gConsts, 401, error_message);
        }

        // Let WordPress generate the post_name (slug) unless
		// one has been provided.
        post_name = "";

        if (isset(content_struct.getValue("wp_slug"))) {
            post_name = strval(content_struct.getValue("wp_slug"));
        }

        // Only use a password if one was given.
        if (isset(content_struct.getValue("wp_password"))) {
            post_password = content_struct.getValue("wp_password");
        }

        // Only set a post parent if one was provided.
        if (isset(content_struct.getValue("wp_page_parent_id"))) {
            post_parent = content_struct.getValue("wp_page_parent_id");
        }

        // Only set the menu_order if it was provided.
        if (isset(content_struct.getValue("wp_page_order"))) {
            menu_order = content_struct.getValue("wp_page_order");
        }

        post_author = user.getID();
        
        // If an author id was provided then use it instead.
        if (isset(content_struct.getValue("wp_author_id")) && !equal(user.getID(), content_struct.getValue("wp_author_id"))) {
            {
                int javaSwitchSelector93 = 0;

                if (equal(post_type, "post")) {
                    javaSwitchSelector93 = 1;
                }

                if (equal(post_type, "page")) {
                    javaSwitchSelector93 = 2;
                }

                switch (javaSwitchSelector93) {
                case 1: {
                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_posts")) {
                        return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to post as this user", "default"));
                    }

                    break;
                }

                case 2: {
                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_pages")) {
                        return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to create pages as this user", "default"));
                    }

                    break;
                }

                default:
                    return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid post type.", "default"));
                }
            }

            post_author = intval(content_struct.getValue("wp_author_id"));
        }

        post_title = content_struct.getValue("title");
        post_content = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("content_save_pre", content_struct.getValue("description")));
        
        post_status = (booleanval(publish) ? "publish" : "draft");

        if (isset(content_struct.getValue(post_type + "_status"))) {
            {
                int javaSwitchSelector94 = 0;

                if (equal(content_struct.getValue(post_type + "_status"), "draft")) {
                    javaSwitchSelector94 = 1;
                }

                if (equal(content_struct.getValue(post_type + "_status"), "private")) {
                    javaSwitchSelector94 = 2;
                }

                if (equal(content_struct.getValue(post_type + "_status"), "publish")) {
                    javaSwitchSelector94 = 3;
                }

                if (equal(content_struct.getValue(post_type + "_status"), "pending")) {
                    javaSwitchSelector94 = 4;
                }

                switch (javaSwitchSelector94) {
                case 1: {
                }

                case 2: {
                }

                case 3: {
                    post_status = strval(content_struct.getValue(post_type + "_status"));

                    break;
                }

                case 4: {
                	// Pending is only valid for posts, not pages.
                    if (strictEqual(post_type, "post")) {
                        post_status = strval(content_struct.getValue(post_type + "_status"));
                    }

                    break;
                }

                default: {
                    post_status = (booleanval(publish)
                        ? "publish"
                        : "draft");

                    break;
                }
                }
            }
        }

        post_excerpt = content_struct.getValue("mt_excerpt");
        post_more = content_struct.getValue("mt_text_more");
        
        tags_input = content_struct.getValue("mt_keywords");

        if (isset(content_struct.getValue("mt_allow_comments"))) {
            if (!is_numeric(content_struct.getValue("mt_allow_comments"))) {
                {
                    int javaSwitchSelector95 = 0;

                    if (equal(content_struct.getValue("mt_allow_comments"), "closed")) {
                        javaSwitchSelector95 = 1;
                    }

                    if (equal(content_struct.getValue("mt_allow_comments"), "open")) {
                        javaSwitchSelector95 = 2;
                    }

                    switch (javaSwitchSelector95) {
                    case 1: {
                        comment_status = "closed";

                        break;
                    }

                    case 2: {
                        comment_status = "open";

                        break;
                    }

                    default: {
                        comment_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status");

                        break;
                    }
                    }
                }
            } else {
                switch (intval(content_struct.getValue("mt_allow_comments"))) {
                case 0: {
                }

                case 2: {
                    comment_status = "closed";

                    break;
                }

                case 1: {
                    comment_status = "open";

                    break;
                }

                default: {
                    comment_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status");

                    break;
                }
                }
            }
        } else {
            comment_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status");
        }

        if (isset(content_struct.getValue("mt_allow_pings"))) {
            if (!is_numeric(content_struct.getValue("mt_allow_pings"))) {
                {
                    int javaSwitchSelector96 = 0;

                    if (equal(content_struct.getValue("mt_allow_pings"), "closed")) {
                        javaSwitchSelector96 = 1;
                    }

                    if (equal(content_struct.getValue("mt_allow_pings"), "open")) {
                        javaSwitchSelector96 = 2;
                    }

                    switch (javaSwitchSelector96) {
                    case 1: {
                        ping_status = "closed";

                        break;
                    }

                    case 2: {
                        ping_status = "open";

                        break;
                    }

                    default: {
                        ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");

                        break;
                    }
                    }
                }
            } else {
                switch (intval(content_struct.getValue("mt_allow_pings"))) {
                case 0: {
                    ping_status = "closed";

                    break;
                }

                case 1: {
                    ping_status = "open";

                    break;
                }

                default: {
                    ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");

                    break;
                }
                }
            }
        } else {
            ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");
        }

        if (booleanval(post_more)) {
            post_content = post_content + "\n<!--more-->\n" + strval(post_more);
        }

        to_ping = content_struct.getValue("mt_tb_ping_urls");

        if (is_array(to_ping)) {
            to_ping = Strings.implode(" ", (Array) to_ping);
        }

        // Do some timestamp voodoo
        if (!empty(content_struct.getValue("date_created_gmt"))) {
            dateCreated = Strings.str_replace("Z", "", ((IXR_Date) content_struct.getValue("date_created_gmt")).getIso()) + "Z";
        } else if (!empty(content_struct.getValue("dateCreated"))) {
            dateCreated = ((IXR_Date) content_struct.getValue("dateCreated")).getIso();
        }

        if (!empty(dateCreated)) {
            post_date = getIncluded(FormattingPage.class, gVars, gConsts).get_date_from_gmt(getIncluded(FormattingPage.class, gVars, gConsts).iso8601_to_datetime(dateCreated, "USER"));
            post_date_gmt = getIncluded(FormattingPage.class, gVars, gConsts).iso8601_to_datetime(dateCreated, "GMT");
        } else {
            post_date = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
            post_date_gmt = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
        }

        catnames = content_struct.getValue("categories");
        getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "Post cats: " + var_export(gVars.webEnv, catnames, true));
        post_category = new Array<Object>();

        if (is_array(catnames)) {
            for (Map.Entry javaEntry694 : ((Array<?>) catnames).entrySet()) {
                cat = intval(javaEntry694.getValue());
                post_category.putValue(getIncluded(CategoryPage.class, gVars, gConsts).get_cat_ID(cat));
            }
        }

        // We've got all the data -- post it:
        postdata = Array.compact(
                new ArrayEntry("post_author", post_author),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("comment_status", comment_status),
                new ArrayEntry("ping_status", ping_status),
                new ArrayEntry("to_ping", to_ping),
                new ArrayEntry("post_type", post_type),
                new ArrayEntry("post_name", post_name),
                new ArrayEntry("post_password", post_password),
                new ArrayEntry("post_parent", post_parent),
                new ArrayEntry("menu_order", menu_order),
                new ArrayEntry("tags_input", tags_input));
        post_ID = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(postdata);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_ID)) {
            return new IXR_Error(gVars, gConsts, 500, ((WP_Error) post_ID).get_error_message());
        }

        if (!booleanval(post_ID)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, your entry could not be posted. Something wrong happened.", "default"));
        }

        if (isset(content_struct.getValue("custom_fields"))) {
            this.set_custom_fields(intval(post_ID), content_struct.getValue("custom_fields"));
        }

        this.attach_uploads(intval(post_ID), post_content);
        getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "Posted ! ID: " + strval(post_ID));

        return post_ID;
    }

    public void attach_uploads(int post_ID, String post_content) {
        Object attachments;

        /* Do not change type */
        StdClass file = null;
        
        // find any unattached files
        attachments = gVars.wpdb.get_results("SELECT ID, guid FROM " + gVars.wpdb.posts + " WHERE post_parent = \'-1\' AND post_type = \'attachment\'");

        if (is_array(attachments)) {
            for (Map.Entry javaEntry695 : ((Array<?>) attachments).entrySet()) {
                file = (StdClass) javaEntry695.getValue();

                if (!strictEqual(Strings.strpos(post_content, strval(StdClass.getValue(file, "guid"))), BOOLEAN_FALSE)) {
                    gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_parent = \'" + post_ID + "\' WHERE ID = \'" + StdClass.getValue(file, "ID") + "\'");
                }
            }
        }
    }

    /**
     * metaweblog.editPost ...edits a post
     */
    public Object mw_editPost(Array<Object> args) {
        Integer post_ID = null;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> content_struct = new Array<Object>();
        Object publish = null;
        WP_User user = null;
        String cap = null;
        String error_message = null;
        String post_type = null;
        Array<Object> postdata = new Array<Object>();
        String post_name = null;
        Object post_password = null;
        String post_parent = null;
        Object menu_order = null;
        Object post_author = null;
        Object comment_status = null;
        Object ping_status = null;
        Object post_title = null;
        String post_content = null;
        Object catnames = null;

        /* Do not change type */
        Array<Object> post_category = new Array<Object>();
        int cat;
        Object post_excerpt = null;
        Object post_more = null;
        String post_status = null;
        Object tags_input = null;
        Object to_ping = null;

        /* Do not change type */
        String dateCreated = null;
        String post_date = null;
        String post_date_gmt = null;
        Array<Object> newpost = new Array<Object>();
        Integer result = null;
        Object ID = null;
        
        this.escape(args);
        
        post_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);
        content_struct = args.getArrayValue(3);
        publish = args.getValue(4);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        user = getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "metaWeblog.editPost");
        
        cap = (booleanval(publish) ? "publish_posts" : "edit_posts");
        error_message = getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you are not allowed to publish posts on this blog.", "default");
        post_type = "post";

        if (!empty(content_struct.getValue("post_type"))) {
            if (equal(content_struct.getValue("post_type"), "page")) {
                cap = (booleanval(publish) ? "publish_pages" : "edit_pages");
                error_message = getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you are not allowed to publish pages on this blog.", "default");
                post_type = "page";
            } else if (equal(content_struct.getValue("post_type"), "post")) {
            	// This is the default, no changes needed
            } else {
            	// No other post_type values are allowed here
                return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid post type.", "default"));
            }
        }

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(cap)) {
            return new IXR_Error(gVars, gConsts, 401, error_message);
        }

        postdata = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());

        // If there is no post data for the give post id, stop
		// now and return an error.  Other wise a new post will be
		// created (which was the old behavior).
        if (empty(postdata.getValue("ID"))) {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid post id.", "default"));
        }

        this.escape(postdata);
        post_password = Array.extractVar(postdata, "post_password", post_password, Array.EXTR_SKIP);
        post_parent = strval(Array.extractVar(postdata, "post_parent", post_parent, Array.EXTR_SKIP));
        menu_order = Array.extractVar(postdata, "menu_order", menu_order, Array.EXTR_SKIP);
        dateCreated = strval(Array.extractVar(postdata, "dateCreated", dateCreated, Array.EXTR_SKIP));
        ID = Array.extractVar(postdata, "ID", ID, Array.EXTR_SKIP);
        
        // Let WordPress manage slug if none was provided.
        post_name = "";

        if (isset(content_struct.getValue("wp_slug"))) {
            post_name = strval(content_struct.getValue("wp_slug"));
        }

        // Only use a password if one was given.
        if (isset(content_struct.getValue("wp_password"))) {
            post_password = content_struct.getValue("wp_password");
        }

        // Only set a post parent if one was given.
        if (isset(content_struct.getValue("wp_page_parent_id"))) {
            post_parent = strval(content_struct.getValue("wp_page_parent_id"));
        }

        // Only set the menu_order if it was given.
        if (isset(content_struct.getValue("wp_page_order"))) {
            menu_order = content_struct.getValue("wp_page_order");
        }

        post_author = postdata.getValue("post_author");

        // Only set the post_author if one is set.
        if (isset(content_struct.getValue("wp_author_id")) && !equal(user.getID(), content_struct.getValue("wp_author_id"))) {
            {
                int javaSwitchSelector97 = 0;

                if (equal(post_type, "post")) {
                    javaSwitchSelector97 = 1;
                }

                if (equal(post_type, "page")) {
                    javaSwitchSelector97 = 2;
                }

                switch (javaSwitchSelector97) {
                case 1: {
                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_posts")) {
                        return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to change the post author as this user.", "default"));
                    }

                    break;
                }

                case 2: {
                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_pages")) {
                        return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to change the page author as this user.", "default"));
                    }

                    break;
                }

                default:
                    return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid post type.", "default"));
                }
            }

            post_author = content_struct.getValue("wp_author_id");
        }

        if (isset(content_struct.getValue("mt_allow_comments"))) {
            if (!is_numeric(content_struct.getValue("mt_allow_comments"))) {
                {
                    int javaSwitchSelector98 = 0;

                    if (equal(content_struct.getValue("mt_allow_comments"), "closed")) {
                        javaSwitchSelector98 = 1;
                    }

                    if (equal(content_struct.getValue("mt_allow_comments"), "open")) {
                        javaSwitchSelector98 = 2;
                    }

                    switch (javaSwitchSelector98) {
                    case 1: {
                        comment_status = "closed";

                        break;
                    }

                    case 2: {
                        comment_status = "open";

                        break;
                    }

                    default: {
                        comment_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status");

                        break;
                    }
                    }
                }
            } else {
                switch (intval(content_struct.getValue("mt_allow_comments"))) {
                case 0: {
                }

                case 2: {
                    comment_status = "closed";

                    break;
                }

                case 1: {
                    comment_status = "open";

                    break;
                }

                default: {
                    comment_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status");

                    break;
                }
                }
            }
        }

        if (isset(content_struct.getValue("mt_allow_pings"))) {
            if (!is_numeric(content_struct.getValue("mt_allow_pings"))) {
                {
                    int javaSwitchSelector99 = 0;

                    if (equal(content_struct.getValue("mt_allow_pings"), "closed")) {
                        javaSwitchSelector99 = 1;
                    }

                    if (equal(content_struct.getValue("mt_allow_pings"), "open")) {
                        javaSwitchSelector99 = 2;
                    }

                    switch (javaSwitchSelector99) {
                    case 1: {
                        ping_status = "closed";

                        break;
                    }

                    case 2: {
                        ping_status = "open";

                        break;
                    }

                    default: {
                        ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");

                        break;
                    }
                    }
                }
            } else {
                switch (intval(content_struct.getValue("mt_allow_pings"))) {
                case 0: {
                    ping_status = "closed";

                    break;
                }

                case 1: {
                    ping_status = "open";

                    break;
                }

                default: {
                    ping_status = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status");

                    break;
                }
                }
            }
        }

        post_title = content_struct.getValue("title");
        post_content = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("content_save_pre", content_struct.getValue("description")));
        catnames = content_struct.getValue("categories");
        
        post_category = new Array<Object>();

        if (is_array(catnames)) {
            for (Map.Entry javaEntry696 : ((Array<?>) catnames).entrySet()) {
                cat = intval(javaEntry696.getValue());
                post_category.putValue(getIncluded(CategoryPage.class, gVars, gConsts).get_cat_ID(cat));
            }
        }

        post_excerpt = content_struct.getValue("mt_excerpt");
        post_more = content_struct.getValue("mt_text_more");
        
        post_status = (booleanval(publish) ? "publish" : "draft");

        if (isset(content_struct.getValue(post_type + "_status"))) {
            {
                int javaSwitchSelector100 = 0;

                if (equal(content_struct.getValue(post_type + "_status"), "draft")) {
                    javaSwitchSelector100 = 1;
                }

                if (equal(content_struct.getValue(post_type + "_status"), "private")) {
                    javaSwitchSelector100 = 2;
                }

                if (equal(content_struct.getValue(post_type + "_status"), "publish")) {
                    javaSwitchSelector100 = 3;
                }

                if (equal(content_struct.getValue(post_type + "_status"), "pending")) {
                    javaSwitchSelector100 = 4;
                }

                switch (javaSwitchSelector100) {
                case 1: {
                }

                case 2: {
                }

                case 3: {
                    post_status = strval(content_struct.getValue(post_type + "_status"));

                    break;
                }

                case 4: {
                    if (strictEqual(post_type, "post")) {
                    	// Pending is only valid for posts, not pages.
                        post_status = strval(content_struct.getValue(post_type + "_status"));
                    }

                    break;
                }

                default: {
                    post_status = (booleanval(publish)
                        ? "publish"
                        : "draft");

                    break;
                }
                }
            }
        }

        tags_input = content_struct.getValue("mt_keywords");

        if (equal("publish", post_status)) {
            if (equal("page", post_type) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_pages")) {
                return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to publish this page.", "default"));
            } else if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
                return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to publish this post.", "default"));
            }
        }

        if (booleanval(post_more)) {
            post_content = post_content + "\n<!--more-->\n" + strval(post_more);
        }

        to_ping = content_struct.getValue("mt_tb_ping_urls");

        if (is_array(to_ping)) {
            to_ping = Strings.implode(" ", (Array) to_ping);
        }

        // Do some timestamp voodoo
        if (!empty(content_struct.getValue("date_created_gmt"))) {
            dateCreated = Strings.str_replace("Z", "", ((IXR_Date) content_struct.getValue("date_created_gmt")).getIso()) + "Z";
        } else if (!empty(content_struct.getValue("dateCreated"))) {
            dateCreated = ((IXR_Date) content_struct.getValue("dateCreated")).getIso();
        }

        if (!empty(dateCreated)) {
            post_date = getIncluded(FormattingPage.class, gVars, gConsts).get_date_from_gmt(getIncluded(FormattingPage.class, gVars, gConsts).iso8601_to_datetime(dateCreated, "USER"));
            post_date_gmt = getIncluded(FormattingPage.class, gVars, gConsts).iso8601_to_datetime(dateCreated, "GMT");
        } else {
            post_date = strval(postdata.getValue("post_date"));
            post_date_gmt = strval(postdata.getValue("post_date_gmt"));
        }

        // We've got all the data -- post it:
        newpost = Array.compact(
                new ArrayEntry("ID", ID),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("comment_status", comment_status),
                new ArrayEntry("ping_status", ping_status),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("to_ping", to_ping),
                new ArrayEntry("post_name", post_name),
                new ArrayEntry("post_password", post_password),
                new ArrayEntry("post_parent", post_parent),
                new ArrayEntry("menu_order", menu_order),
                new ArrayEntry("post_author", post_author),
                new ArrayEntry("tags_input", tags_input));
        result = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_post(newpost);

        if (!booleanval(result)) {
            return new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, your entry could not be edited. Something wrong happened.", "default"));
        }

        if (isset(content_struct.getValue("custom_fields"))) {
            this.set_custom_fields(post_ID, content_struct.getValue("custom_fields"));
        }

        this.attach_uploads(intval(ID), post_content);
        getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "(MW) Edited ! ID: " + strval(post_ID));

        return true;
    }

    /**
     * metaweblog.getPost ...returns a post
     */
    public Object mw_getPost(Array<Object> args) {
        int post_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> postdata = new Array<Object>();
        String post_date = null;
        String post_date_gmt = null;
        Array<Object> categories = new Array<Object>();
        Array<Object> catids = null;
        int catid;
        String tagnames = null;
        Array<Object> tags = null;
        StdClass tag = null;
        Array<Object> post = new Array<Object>();
        String link;
        StdClass author;
        int allow_comments = 0;
        int allow_pings = 0;
        Array<Object> resp = new Array<Object>();
        
        this.escape(args);
        
        post_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit this post.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "metaWeblog.getPost");
        
        postdata = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());

        if (!equal(postdata.getValue("post_date"), ""))  {
            post_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(postdata.getValue("post_date")), true);
            post_date_gmt = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(postdata.getValue("post_date_gmt")), true);
            
            categories = new Array<Object>();
            catids = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(post_ID, new Array<Object>());

            for (Map.Entry javaEntry697 : catids.entrySet()) {
                catid = intval(javaEntry697.getValue());
                categories.putValue(getIncluded(CategoryPage.class, gVars, gConsts).get_cat_name(catid));
            }

            Array<String> tagnamesArray = new Array<String>();
            tags = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_tags(post_ID, new Array<Object>());

            if (!empty(tags)) {
                for (Map.Entry javaEntry698 : tags.entrySet()) {
                    tag = (StdClass) javaEntry698.getValue();
                    tagnamesArray.putValue(StdClass.getValue(tag, "name"));
                }

                tagnames = Strings.implode(", ", tagnamesArray);
            } else {
                tagnames = "";
            }

            post = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_extended(strval(postdata.getValue("post_content")));
            link = getIncluded(Link_templatePage.class, gVars, gConsts).post_permalink(postdata.getValue("ID"), "");
            
            // Get the author info.
            author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(postdata.getValue("post_author")));
            
            allow_comments = (equal("open", postdata.getValue("comment_status"))
                ? 1
                : 0);
            allow_pings = (equal("open", postdata.getValue("ping_status"))
                ? 1
                : 0);

            // Consider future posts as published
            if (equal(postdata.getValue("post_status"), "future")) {
                postdata.putValue("post_status", "publish");
            }

            resp = new Array<Object>(
                    new ArrayEntry<Object>("dateCreated", new IXR_Date(gVars, gConsts, post_date)),
                    new ArrayEntry<Object>("userid", postdata.getValue("post_author")),
                    new ArrayEntry<Object>("postid", postdata.getValue("ID")),
                    new ArrayEntry<Object>("description", post.getValue("main")),
                    new ArrayEntry<Object>("title", postdata.getValue("post_title")),
                    new ArrayEntry<Object>("link", link),
                    new ArrayEntry<Object>("permaLink", link),
                    // commented out because no other tool seems to use this
    				//	      'content' => $entry['post_content'],
                    new ArrayEntry<Object>("categories", categories),
                    new ArrayEntry<Object>("mt_excerpt", postdata.getValue("post_excerpt")),
                    new ArrayEntry<Object>("mt_text_more", post.getValue("extended")),
                    new ArrayEntry<Object>("mt_allow_comments", allow_comments),
                    new ArrayEntry<Object>("mt_allow_pings", allow_pings),
                    new ArrayEntry<Object>("mt_keywords", tagnames),
                    new ArrayEntry<Object>("wp_slug", postdata.getValue("post_name")),
                    new ArrayEntry<Object>("wp_password", postdata.getValue("post_password")),
                    new ArrayEntry<Object>("wp_author_id", StdClass.getValue(author, "ID")),
                    new ArrayEntry<Object>("wp_author_display_name", StdClass.getValue(author, "display_name")),
                    new ArrayEntry<Object>("date_created_gmt", new IXR_Date(gVars, gConsts, post_date_gmt)),
                    new ArrayEntry<Object>("post_status", postdata.getValue("post_status")),
                    new ArrayEntry<Object>("custom_fields", this.get_custom_fields(post_ID)));

            return resp;
        } else {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such post.", "default"));
        }
    }

    /**
     * metaweblog.getRecentPosts ...returns recent posts
     */
    public Object mw_getRecentPosts(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        int num_posts = 0;
        Array<Object> posts_list = new Array<Object>();
        Array<Object> entry = new Array<Object>();
        String post_date = null;
        String post_date_gmt = null;
        Array<Object> categories = new Array<Object>();
        Array<Object> catids;
        int catid;
        String tagnames = null;
        Array<Object> tags;
        StdClass tag;
        Array<Object> post = new Array<Object>();
        String link;
        StdClass author;
        int allow_comments = 0;
        int allow_pings = 0;
        Array<Object> struct = new Array<Object>();
        Array<Object> recent_posts = new Array<Object>();
        int j = 0;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);
        num_posts = intval(args.getValue(3));

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "metaWeblog.getRecentPosts");
        
        posts_list = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_recent_posts(num_posts);

        if (!booleanval(posts_list)) {
            this.error = new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Either there are no posts, or something went wrong.", "default"));

            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        for (Map.Entry javaEntry699 : posts_list.entrySet()) {
            entry = (Array<Object>) javaEntry699.getValue();

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", entry.getValue("ID"))) {
                continue;
            }

            post_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(entry.getValue("post_date")), true);
            post_date_gmt = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(entry.getValue("post_date_gmt")), true);
            
            categories = new Array<Object>();
            catids = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(
                    intval(entry.getValue("ID")),
                    new Array<Object>());

            for (Map.Entry javaEntry700 : catids.entrySet()) {
                catid = intval(javaEntry700.getValue());
                categories.putValue(getIncluded(CategoryPage.class, gVars, gConsts).get_cat_name(catid));
            }

            Array<String> tagnamesArray = new Array<String>();
            tags = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_tags(intval(entry.getValue("ID")), new Array<Object>());

            if (!empty(tags)) {
                for (Map.Entry javaEntry701 : tags.entrySet()) {
                    tag = (StdClass) javaEntry701.getValue();
                    tagnamesArray.putValue(StdClass.getValue(tag, "name"));
                }

                tagnames = Strings.implode(", ", tagnamesArray);
            } else {
                tagnames = "";
            }

            post = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_extended(strval(entry.getValue("post_content")));
            link = getIncluded(Link_templatePage.class, gVars, gConsts).post_permalink(entry.getValue("ID"), "");
            
            // Get the post author info.
            author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(entry.getValue("post_author")));
            
            allow_comments = (equal("open", entry.getValue("comment_status")) ? 1 : 0);
            allow_pings = (equal("open", entry.getValue("ping_status")) ? 1 : 0);

            // Consider future posts as published
            if (equal(entry.getValue("post_status"), "future")) {
                entry.putValue("post_status", "publish");
            }

            struct.putValue(
                new Array<Object>(
                    new ArrayEntry<Object>("dateCreated", new IXR_Date(gVars, gConsts, post_date)),
                    new ArrayEntry<Object>("userid", entry.getValue("post_author")),
                    new ArrayEntry<Object>("postid", entry.getValue("ID")),
                    new ArrayEntry<Object>("description", post.getValue("main")),
                    new ArrayEntry<Object>("title", entry.getValue("post_title")),
                    new ArrayEntry<Object>("link", link),
                    new ArrayEntry<Object>("permaLink", link),
// commented out because no other tool seems to use this
//          	      'content' => $entry['post_content'],
                    new ArrayEntry<Object>("categories", categories),
                    new ArrayEntry<Object>("mt_excerpt", entry.getValue("post_excerpt")),
                    new ArrayEntry<Object>("mt_text_more", post.getValue("extended")),
                    new ArrayEntry<Object>("mt_allow_comments", allow_comments),
                    new ArrayEntry<Object>("mt_allow_pings", allow_pings),
                    new ArrayEntry<Object>("mt_keywords", tagnames),
                    new ArrayEntry<Object>("wp_slug", entry.getValue("post_name")),
                    new ArrayEntry<Object>("wp_password", entry.getValue("post_password")),
                    new ArrayEntry<Object>("wp_author_id", StdClass.getValue(author, "ID")),
                    new ArrayEntry<Object>("wp_author_display_name", StdClass.getValue(author, "display_name")),
                    new ArrayEntry<Object>("date_created_gmt", new IXR_Date(gVars, gConsts, post_date_gmt)),
                    new ArrayEntry<Object>("post_status", entry.getValue("post_status")),
                    new ArrayEntry<Object>("custom_fields", this.get_custom_fields(intval(entry.getValue("ID"))))));
        }

        recent_posts = new Array<Object>();

        for (j = 0; j < Array.count(struct); j++) {
            Array.array_push(recent_posts, struct.getValue(j));
        }

        return recent_posts;
    }

    /**
     * metaweblog.getCategories ...returns the list of categories on a given blog
     */
    public Object mw_getCategories(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> categories_struct = new Array<Object>();
        Array<Object> cats = new Array<Object>();
        Array<Object> struct = new Array<Object>();
        StdClass cat = null;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you must be able to edit posts on this blog in order to view categories.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "metaWeblog.getCategories");
        
        categories_struct = new Array<Object>();

        if (booleanval(cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("get=all"))) {
            for (Map.Entry javaEntry702 : cats.entrySet()) {
                cat = (StdClass) javaEntry702.getValue();
                struct.putValue("categoryId", StdClass.getValue(cat, "term_id"));
                struct.putValue("parentId", StdClass.getValue(cat, "parent"));
                struct.putValue("description", StdClass.getValue(cat, "name"));
                struct.putValue("categoryName", StdClass.getValue(cat, "name"));
                struct.putValue(
                    "htmlUrl",
                    getIncluded(FormattingPage.class, gVars, gConsts)
                        .wp_specialchars(strval(getIncluded(Category_templatePage.class, gVars, gConsts).get_category_link(StdClass.getValue(cat, "term_id"))), strval(0)));
                struct.putValue(
                    "rssUrl",
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(getIncluded(DeprecatedPage.class, gVars, gConsts).get_category_rss_link(false,
                            intval(StdClass.getValue(cat, "term_id")), StdClass.getValue(cat, "name")), strval(0)));
                categories_struct.putValue(struct);
            }
        }

        return categories_struct;
    }

    /**
     * metaweblog.newMediaObject uploads a file, following your settings
     */
    public Object mw_newMediaObject(Array<Object> args) {
        int blog_ID = 0;
        String user_login = null;
        String user_pass = null;
        Array<Object> data = new Array<Object>();
        String name = null;
        Object type = null;
        String bits = null;
        String upload_err = null;
        StdClass old_file;
        String filename;
        Array<Object> upload = new Array<Object>();
        String errorString = null;
        int post_id = 0;
        Array<Object> attachment = new Array<Object>();
        int id = 0;
        
        // adapted from a patch by Johann Richard
		// http://mycvs.org/archives/2004/06/30/file-upload-to-wordpress-in-ecto/
        blog_ID = intval(args.getValue(0));
        user_login = gVars.wpdb.escape(strval(args.getValue(1)));
        user_pass = gVars.wpdb.escape(strval(args.getValue(2)));
        data = args.getArrayValue(3);
        
        name = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_file_name(strval(data.getValue("name")));
        type = data.getValue("type");
        bits = strval(data.getValue("bits"));
        
        getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "(MW) Received " + strval(Strings.strlen(bits)) + " bytes");

        if (!this.login_pass_ok(user_login, user_pass)) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "metaWeblog.newMediaObject");
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, user_login);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("upload_files")) {
            getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "(MW) User does not have upload_files capability");
            this.error = new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to upload files to this site.", "default"));

            return this.error;
        }

        if (booleanval(upload_err = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_upload_error", false)))) {
            return new IXR_Error(gVars, gConsts, 500, upload_err);
        }

        if (!empty(data.getValue("overwrite")) && equal(data.getValue("overwrite"), true)) {
        	// Get postmeta info on the object.
            old_file = (StdClass) gVars.wpdb.get_row(
                        "\n\t\t\t\tSELECT ID\n\t\t\t\tFROM " + gVars.wpdb.posts + "\n\t\t\t\tWHERE post_title = \'" + name + "\'\n\t\t\t\t\tAND post_type = \'attachment\'\n\t\t\t");
            
            // Delete previous file.
            getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).wp_delete_attachment(intval(StdClass.getValue(old_file, "ID")));

            // Make sure the new name is different by pre-pending the
			// previous post id.
            filename = QRegExPerl.preg_replace("/^wpid\\d+-/", "", name);
            name = "wpid" + StdClass.getValue(old_file, "ID") + "-" + filename;
        }

        upload = getIncluded(FunctionsPage.class, gVars, gConsts).wp_upload_bits(name, intval(type), bits, null);

        if (!empty(upload.getValue("error"))) {
            errorString = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Could not write file %1$s (%2$s)", "default"), name, upload.getValue("error"));
            getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "(MW) " + errorString);

            return new IXR_Error(gVars, gConsts, 500, errorString);
        }
        // Construct the attachment array
		// attach to post_id -1
        post_id = -1;
        attachment = new Array<Object>(
                new ArrayEntry<Object>("post_title", name),
                new ArrayEntry<Object>("post_content", ""),
                new ArrayEntry<Object>("post_type", "attachment"),
                new ArrayEntry<Object>("post_parent", post_id),
                new ArrayEntry<Object>("post_mime_type", type),
                new ArrayEntry<Object>("guid", upload.getValue("url")));
        
        // Save the data
        id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_attachment(attachment, strval(upload.getValue("file")), post_id);
        (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_attachment_metadata(
            id,
            getIncluded(ImagePage.class, gVars, gConsts).wp_generate_attachment_metadata(id, strval(upload.getValue("file"))));

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
            "wp_handle_upload",
            new Array<Object>(new ArrayEntry<Object>("file", name), new ArrayEntry<Object>("url", upload.getValue("url")), new ArrayEntry<Object>("type", type)));
    }

    /* MovableType API functions
	 * specs on http://www.movabletype.org/docs/mtmanual_programmatic.html
	 */

	/* mt.getRecentPostTitles ...returns recent posts' titles */
    public Object mt_getRecentPostTitles(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        int num_posts = 0;
        Array<Object> posts_list = new Array<Object>();
        Array<Object> entry = new Array<Object>();
        String post_date = null;
        String post_date_gmt = null;
        Array<Object> struct = new Array<Object>();
        Array<Object> recent_posts = new Array<Object>();
        int j = 0;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);
        num_posts = intval(args.getValue(3));

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.getRecentPostTitles");
        
        posts_list = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_recent_posts(num_posts);

        if (!booleanval(posts_list)) {
            this.error = new IXR_Error(gVars, gConsts, 500, getIncluded(L10nPage.class, gVars, gConsts).__("Either there are no posts, or something went wrong.", "default"));

            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        for (Map.Entry javaEntry703 : posts_list.entrySet()) {
            entry = (Array<Object>) javaEntry703.getValue();

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", entry.getValue("ID"))) {
                continue;
            }

            post_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(entry.getValue("post_date")), true);
            post_date_gmt = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Ymd\\TH:i:s", strval(entry.getValue("post_date_gmt")), true);
            
            struct.putValue(
                new Array<Object>(
                    new ArrayEntry<Object>("dateCreated", new IXR_Date(gVars, gConsts, post_date)),
                    new ArrayEntry<Object>("userid", entry.getValue("post_author")),
                    new ArrayEntry<Object>("postid", entry.getValue("ID")),
                    new ArrayEntry<Object>("title", entry.getValue("post_title")),
                    new ArrayEntry<Object>("date_created_gmt", new IXR_Date(gVars, gConsts, post_date_gmt))));
        }

        recent_posts = new Array<Object>();

        for (j = 0; j < Array.count(struct); j++) {
            Array.array_push(recent_posts, struct.getValue(j));
        }

        return recent_posts;
    }

    /**
     * mt.getCategoryList ...returns the list of categories on a given blog
     */
    public Object mt_getCategoryList(Array<Object> args) {
        int blog_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> categories_struct = new Array<Object>();
        Array<Object> cats = new Array<Object>();
        Array<Object> struct = new Array<Object>();
        StdClass cat = null;
        
        this.escape(args);
        
        blog_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you must be able to edit posts on this blog in order to view categories.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.getCategoryList");
        categories_struct = new Array<Object>();

        if (booleanval(cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("hide_empty=0&hierarchical=0"))) {
            for (Map.Entry javaEntry704 : cats.entrySet()) {
                cat = (StdClass) javaEntry704.getValue();
                struct.putValue("categoryId", StdClass.getValue(cat, "term_id"));
                struct.putValue("categoryName", StdClass.getValue(cat, "name"));
                
                categories_struct.putValue(struct);
            }
        }

        return categories_struct;
    }

    /**
     * mt.getPostCategories ...returns a post's categories
     */
    public Object mt_getPostCategories(Array<Object> args) {
        int post_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> categories = new Array<Object>();
        Array<Object> catids = null;
        boolean isPrimary = false;
        int catid;
        
        this.escape(args);
        
        post_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit this post.", "default"));
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.getPostCategories");
        
        categories = new Array<Object>();
        catids = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(post_ID, new Array<Object>());
        // first listed category will be the primary category
        isPrimary = true;

        for (Map.Entry javaEntry705 : catids.entrySet()) {
            catid = intval(javaEntry705.getValue());
            categories.putValue(
                new Array<Object>(
                    new ArrayEntry<Object>("categoryName", getIncluded(CategoryPage.class, gVars, gConsts).get_cat_name(catid)),
                    new ArrayEntry<Object>("categoryId", strval(catid)),
                    new ArrayEntry<Object>("isPrimary", isPrimary)));
            isPrimary = false;
        }

        return categories;
    }

    /**
     * mt.setPostCategories ...sets a post's categories
     */
    public Object mt_setPostCategories(Array<Object> args) {
        int post_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> categories = null;
        Array<Object> catids = new Array<Object>();
        Array<Object> cat = new Array<Object>();
        
        this.escape(args);
        
        post_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);
        categories = (Array<Object>) args.getValue(3);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.setPostCategories");
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit this post.", "default"));
        }

        for (Map.Entry javaEntry706 : categories.entrySet()) {
            cat = (Array<Object>) javaEntry706.getValue();
            catids.putValue(cat.getValue("categoryId"));
        }

        (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_set_post_categories(post_ID, catids);

        return true;
    }

    /**
     * mt.supportedMethods ...returns an array of methods supported by this server
     */
    public Array<Object> mt_supportedMethods(Object args) {
        Array<Object> supported_methods = new Array<Object>();
        Object key = null;
        Object value = null;
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.supportedMethods");
        supported_methods = new Array<Object>();

        for (Map.Entry javaEntry707 : this.methods.entrySet()) {
            key = javaEntry707.getKey();
            value = javaEntry707.getValue();
            supported_methods.putValue(key);
        }

        return supported_methods;
    }

    /**
     * mt.supportedTextFilters ...returns an empty array because we don't
     * support per-post text filters yet
     */
    public void mt_supportedTextFilters(Object args) {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.supportedTextFilters");
        getIncluded(PluginPage.class, gVars, gConsts).apply_filters("xmlrpc_text_filters", new Array<Object>());
    }

    /**
     * mt.getTrackbackPings ...returns trackbacks sent to a given post
     */
    public Object mt_getTrackbackPings(Object args) {
        int post_ID = 0;
        Array<Object> actual_post = new Array<Object>();
        Array<Object> comments = new Array<Object>();
        Array<Object> trackback_pings = new Array<Object>();
        StdClass comment = null;
        String content = null;
        String title = null;
        
        post_ID = intval(args);
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.getTrackbackPings");
        
        actual_post = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());

        if (!booleanval(actual_post)) {
            return new IXR_Error(gVars, gConsts, 404, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, no such post.", "default"));
        }

        comments = gVars.wpdb.get_results("SELECT comment_author_url, comment_content, comment_author_IP, comment_type FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = " + post_ID);

        if (!booleanval(comments)) {
            return new Array<Object>();
        }

        trackback_pings = new Array<Object>();

        for (Map.Entry javaEntry708 : comments.entrySet()) {
            comment = (StdClass) javaEntry708.getValue();

            if (equal("trackback", StdClass.getValue(comment, "comment_type"))) {
                content = strval(StdClass.getValue(comment, "comment_content"));
                title = Strings.substr(content, 8, Strings.strpos(content, "</strong>") - 8);
                trackback_pings.putValue(
                    new Array<Object>(
                        new ArrayEntry<Object>("pingTitle", title),
                        new ArrayEntry<Object>("pingURL", StdClass.getValue(comment, "comment_author_url")),
                        new ArrayEntry<Object>("pingIP", StdClass.getValue(comment, "comment_author_IP"))));
            }
        }

        return trackback_pings;
    }

    /**
     * mt.publishPost ...sets a post's publish status to 'publish'
     */
    public Object mt_publishPost(Array<Object> args) {
        int post_ID = 0;
        Object user_login = null;
        Object user_pass = null;
        Array<Object> postdata = new Array<Object>();
        Object cats = null;
        int result = 0;
        
        this.escape(args);
        
        post_ID = intval(args.getValue(0));
        user_login = args.getValue(1);
        user_pass = args.getValue(2);

        if (!this.login_pass_ok(strval(user_login), strval(user_pass))) {
            return this.error;
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "mt.publishPost");
        getIncluded(PluggablePage.class, gVars, gConsts).set_current_user(0, strval(user_login));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
            return new IXR_Error(gVars, gConsts, 401, getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you can not edit this post.", "default"));
        }

        postdata = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());
        
        postdata.putValue("post_status", "publish");
        
        // retain old cats
        cats = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_post_categories(post_ID, new Array<Object>());
        postdata.putValue("post_category", cats);
        this.escape(postdata);
        
        result = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_post(postdata);

        return result;
    }

    /* PingBack functions
	 * specs on www.hixie.ch/specs/pingback/pingback
	 */

	/* pingback.ping gets a pingback and registers it */
    public Object pingback_ping(Array<Object> args) {
        String pagelinkedfrom;
        String pagelinkedto;
        String title = null;
        int pos1 = 0;
        Array<String> urltest = new Array<String>();
        int post_ID;
        String way = null;
        Array<Object> match = new Array<Object>();
        Array<String> blah = new Array<String>();
        String sql = null;
        StdClass post = null;
        String linea = null;
        Array<Object> matchtitle = new Array<Object>();
        Array<String> p = new Array<String>();
        String preg_target = null;
        String para = null;
        Array context = null;
        String excerpt = null;
        String marker = null;
        String preg_marker = null;
        int comment_post_ID = 0;
        String comment_author = null;
        String comment_author_url;
        String comment_content = null;
        String comment_type = null;
        Array<Object> commentdata = new Array<Object>();
        int comment_ID = 0;
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "pingback.ping");
        
        this.escape(args);
        
        pagelinkedfrom = strval(args.getValue(0));
        pagelinkedto = strval(args.getValue(1));
        
        title = "";
        
        pagelinkedfrom = Strings.str_replace("&amp;", "&", pagelinkedfrom);
        pagelinkedto = Strings.str_replace("&amp;", "&", pagelinkedto);
        pagelinkedto = Strings.str_replace("&", "&amp;", pagelinkedto);
        
        // Check if the page linked to is in our site
        pos1 = Strings.strpos(
                pagelinkedto,
                Strings.str_replace(
                    new Array<Object>(new ArrayEntry<Object>("http://www."), new ArrayEntry<Object>("http://"), new ArrayEntry<Object>("https://www."), new ArrayEntry<Object>("https://")), "",
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"))));

        if (!booleanval(pos1)) {
            return new IXR_Error(gVars, gConsts, 0, getIncluded(L10nPage.class, gVars, gConsts).__("Is there no link to us?", "default"));
        }

        // let's find which post is linked to
		// FIXME: does url_to_postid() cover all these cases already?
		//        if so, then let's use it and drop the old code.
        urltest = URL.parse_url(pagelinkedto);

        if (booleanval(post_ID = getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(pagelinkedto))) {
            way = "url_to_postid()";
        } else if (QRegExPerl.preg_match("#p/[0-9]{1,}#", urltest.getValue("path"), match)) {
        	// the path defines the post_ID (archives/p/XXXX)
            blah = Strings.explode("/", strval(match.getValue(0)));
            post_ID = intval(blah.getValue(1));
            way = "from the path";
        } else if (QRegExPerl.preg_match("#p=[0-9]{1,}#", urltest.getValue("query"), match)) {
        	// the querystring defines the post_ID (?p=XXXX)
            blah = Strings.explode("=", strval(match.getValue(0)));
            post_ID = intval(blah.getValue(1));
            way = "from the querystring";
        } else if (isset(urltest.getValue("fragment"))) {
            if (booleanval(urltest.getValue("fragment"))) {
            	// an #anchor is there, it's either...
                post_ID = intval(urltest.getValue("fragment"));
                way = "from the fragment (numeric)";
            } else if (QRegExPerl.preg_match("/post-[0-9]+/", urltest.getValue("fragment"))) {
            	// ...a post id in the form 'post-###'
                post_ID = intval(QRegExPerl.preg_replace("/[^0-9]+/", "", urltest.getValue("fragment")));
                way = "from the fragment (post-###)";
            } else if (is_string(urltest.getValue("fragment"))) {
            	// ...or a string #title, a little more complicated
                title = QRegExPerl.preg_replace("/[^a-z0-9]/i", ".", urltest.getValue("fragment"));
                sql = "SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_title RLIKE \'" + title + "\'";

                if (!booleanval(post_ID = intval(gVars.wpdb.get_var(sql)))) {
                	// returning unknown error '0' is better than die()ing
                    return new IXR_Error(gVars, gConsts, 0, "");
                }

                way = "from the fragment (title)";
            }
        } else {
        	// TODO: Attempt to extract a post ID from the given URL
            return new IXR_Error(
                gVars,
                gConsts,
                33,
                getIncluded(L10nPage.class, gVars, gConsts).__("The specified target URL cannot be used as a target. It either doesn\'t exist, or it is not a pingback-enabled resource.", "default"));
        }

        post_ID = post_ID;
        
        getIncluded(XmlrpcPage.class, gVars, gConsts).logIO("O", "(PB) URL=\'" + pagelinkedto + "\' ID=\'" + strval(post_ID) + "\' Found=\'" + way + "\'");
        
        post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(post_ID, gConsts.getOBJECT(), "raw");

        if (!booleanval(post)) { // Post_ID not found
            return new IXR_Error(
                gVars,
                gConsts,
                33,
                getIncluded(L10nPage.class, gVars, gConsts).__("The specified target URL cannot be used as a target. It either doesn\'t exist, or it is not a pingback-enabled resource.", "default"));
        }

        if (equal(post_ID, getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(pagelinkedfrom))) {
            return new IXR_Error(gVars, gConsts, 0, getIncluded(L10nPage.class, gVars, gConsts).__("The source URL and the target URL cannot both point to the same resource.", "default"));
        }

        // Check if pings are on
        if (equal("closed", StdClass.getValue(post, "ping_status"))) {
            return new IXR_Error(
                gVars,
                gConsts,
                33,
                getIncluded(L10nPage.class, gVars, gConsts).__("The specified target URL cannot be used as a target. It either doesn\'t exist, or it is not a pingback-enabled resource.", "default"));
        }

        // Let's check that the remote site didn't already pingback this entry
        gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = \'" + post_ID + "\' AND comment_author_url = \'" + pagelinkedfrom + "\'");

        if (booleanval(gVars.wpdb.num_rows)) {
            return new IXR_Error(gVars, gConsts, 48, getIncluded(L10nPage.class, gVars, gConsts).__("The pingback has already been registered.", "default"));
        }

        // very stupid, but gives time to the 'from' server to publish !
        Misc.sleep(1);
        
        // Let's check the remote site
        linea = getIncluded(FunctionsPage.class, gVars, gConsts).wp_remote_fopen(pagelinkedfrom);

        if (!booleanval(linea)) {
            return new IXR_Error(gVars, gConsts, 16, getIncluded(L10nPage.class, gVars, gConsts).__("The source URL does not exist.", "default"));
        }

        linea = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_remote_source", linea, pagelinkedto));
        
        // Work around bug in strip_tags():
        linea = Strings.str_replace("<!DOC", "<DOC", linea);
        linea = QRegExPerl.preg_replace("/[\\s\\r\\n\\t]+/", " ", linea);
        linea = QRegExPerl.preg_replace("/ <(h1|h2|h3|h4|h5|h6|p|th|td|li|dt|dd|pre|caption|input|textarea|button|body)[^>]*>/", "\n\n", linea);
        
        QRegExPerl.preg_match("|<title>([^<]*?)</title>|is", linea, matchtitle);
        title = strval(matchtitle.getValue(1));

        if (empty(title)) {
            return new IXR_Error(gVars, gConsts, 32, getIncluded(L10nPage.class, gVars, gConsts).__("We cannot find a title on that page.", "default"));
        }

        linea = Strings.strip_tags(linea, "<a>"); // just keep the tag we need
        
        p = Strings.explode("\n\n", linea);
        
        preg_target = RegExPerl.preg_quote(pagelinkedto);

        for (Map.Entry javaEntry709 : p.entrySet()) {
            para = strval(javaEntry709.getValue());

            if (!strictEqual(Strings.strpos(para, pagelinkedto), BOOLEAN_FALSE)) { // it exists, but is it a link?
                QRegExPerl.preg_match("|<a[^>]+?" + preg_target + "[^>]*>([^>]+?)</a>|", para, context);

                // If the URL isn't in a link context, keep looking
                if (empty(context)) {
                    continue;
                }

                // We're going to use this fake tag to mark the context in a bit
				// the marker is needed in case the link text appears more than once in the paragraph
                excerpt = QRegExPerl.preg_replace("|\\</?wpcontext\\>|", "", para);

                // prevent really long link text
                if (Strings.strlen(strval(context.getValue(1))) > 100) {
                    context.putValue(1, Strings.substr(strval(context.getValue(1)), 0, 100) + "...");
                }

                marker = "<wpcontext>" + strval(context.getValue(1)) + "</wpcontext>";    // set up our marker
                excerpt = Strings.str_replace(context.getArrayValue(0), marker, excerpt); // swap out the link for our marker
                excerpt = Strings.strip_tags(excerpt, "<wpcontext>");        // strip all tags but our context marker
                excerpt = Strings.trim(excerpt);
                preg_marker = RegExPerl.preg_quote(marker);
                excerpt = QRegExPerl.preg_replace("|.*?\\s(.{0,100}" + preg_marker + ".{0,100})\\s.*|s", "$1", excerpt);
                excerpt = Strings.strip_tags(excerpt); // YES, again, to remove the marker wrapper

                break;
            }
        }

        if (empty(context)) { // Link to target not found
            return new IXR_Error(gVars, gConsts, 17,
                getIncluded(L10nPage.class, gVars, gConsts).__("The source URL does not contain a link to the target URL, and so cannot be used as a source.", "default"));
        }

        pagelinkedfrom = Strings.str_replace("&", "&amp;", pagelinkedfrom);

        String contextStr = "[...] " + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(excerpt, strval(0)) + " [...]";
        pagelinkedfrom = gVars.wpdb.escape(pagelinkedfrom);
        
        comment_post_ID = post_ID;
        comment_author = title;
        this.escape(comment_author);
        comment_author_url = pagelinkedfrom;
        comment_content = contextStr;
        this.escape(comment_content);
        comment_type = "pingback";
        
        commentdata = Array.compact(
                new ArrayEntry("comment_post_ID", comment_post_ID),
                new ArrayEntry("comment_author", comment_author),
                new ArrayEntry("comment_author_url", comment_author_url),
                new ArrayEntry("comment_content", comment_content),
                new ArrayEntry("comment_type", comment_type));
        
        comment_ID = getIncluded(CommentPage.class, gVars, gConsts).wp_new_comment(commentdata);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("pingback_post", comment_ID);

        return QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Pingback from %1$s to %2$s registered. Keep the web talking! :-)", "default"), pagelinkedfrom, pagelinkedto);
    }

    /**
     * pingback.extensions.getPingbacks returns an array of URLs 
     * that pingbacked the given URL 
     * specs on http://www.aquarionics.com/misc/archives/blogite/0198.html
     */
    public Object pingback_extensions_getPingbacks(Object args) {
        String url = null;
        int post_ID = 0;
        Array<Object> actual_post = new Array<Object>();
        Array<Object> comments = new Array<Object>();
        Array<Object> pingbacks = new Array<Object>();
        StdClass comment = null;
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("xmlrpc_call", "pingback.extensions.getPingsbacks");
        
        this.escape(args);
        
        url = strval(args);
        
        post_ID = getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(url);

        if (!booleanval(post_ID)) {
        	// We aren't sure that the resource is available and/or pingback enabled
            return new IXR_Error(
                gVars,
                gConsts,
                33,
                getIncluded(L10nPage.class, gVars, gConsts).__("The specified target URL cannot be used as a target. It either doesn\'t exist, or it is not a pingback-enabled resource.", "default"));
        }

        actual_post = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_single_post(post_ID, gConsts.getARRAY_A());

        if (!booleanval(actual_post)) {
        	// No such post = resource not found
            return new IXR_Error(gVars, gConsts, 32, getIncluded(L10nPage.class, gVars, gConsts).__("The specified target URL does not exist.", "default"));
        }

        comments = gVars.wpdb.get_results("SELECT comment_author_url, comment_content, comment_author_IP, comment_type FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = " + post_ID);

        if (!booleanval(comments)) {
            return new Array<Object>();
        }

        pingbacks = new Array<Object>();

        for (Map.Entry javaEntry710 : comments.entrySet()) {
            comment = (StdClass) javaEntry710.getValue();

            if (equal("pingback", StdClass.getValue(comment, "comment_type"))) {
                pingbacks.putValue(StdClass.getValue(comment, "comment_author_url"));
            }
        }

        return pingbacks;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
