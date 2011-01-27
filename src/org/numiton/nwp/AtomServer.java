/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: AtomServer.java,v 1.3 2008/10/10 16:48:04 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


public class AtomServer implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(AtomServer.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String ATOM_CONTENT_TYPE = "application/atom+xml";
    public String CATEGORIES_CONTENT_TYPE = "application/atomcat+xml";
    public String SERVICE_CONTENT_TYPE = "application/atomsvc+xml";
    public String ATOM_NS = "http://www.w3.org/2005/Atom";
    public String ATOMPUB_NS = "http://www.w3.org/2007/app";
    public String ENTRIES_PATH = "posts";
    public String CATEGORIES_PATH = "categories";
    public String MEDIA_PATH = "attachments";
    public String ENTRY_PATH = "post";
    public String SERVICE_PATH = "service";
    public String MEDIA_SINGLE_PATH = "attachment";
    public Array<Object> params = new Array<Object>();
    public Array<Object> media_content_types = new Array<Object>(new ArrayEntry<Object>("image/*"), new ArrayEntry<Object>("audio/*"), new ArrayEntry<Object>("video/*"));
    public Array<Object> atom_content_types = new Array<Object>(new ArrayEntry<Object>("application/atom+xml"));
    public Array<Object> selectors = new Array<Object>();

    /** support for head */
    public boolean do_output = true;

    /**
     * Generated in place of local variable 'content_type' from method
     * 'echo_entry' because it is used inside an inner class.
     */
    Object echo_entry_content_type = null;

    /**
     * Generated in place of local variable 'content' from method 'echo_entry'
     * because it is used inside an inner class.
     */
    Object echo_entry_content = null;

    /**
     * Generated in place of local variable 'type' from method
     * 'get_accepted_content_type' because it is used inside an inner class.
     */
    String get_accepted_content_type_type = null;

    /**
     * Generated in place of local variable 'subtype' from method
     * 'get_accepted_content_type' because it is used inside an inner class.
     */
    String get_accepted_content_type_subtype = null;

    /**
     * Generated in place of local variable 'acceptedType' from method
     * 'get_accepted_content_type' because it is used inside an inner class.
     */
    Object get_accepted_content_type_acceptedType = null;

    /**
     * Generated in place of local variable 'acceptedSubtype' from method
     * 'get_accepted_content_type' because it is used inside an inner class.
     */
    Object get_accepted_content_type_acceptedSubtype = null;
    public Object script_name;
    public String app_base;

    public AtomServer(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.script_name = Array.array_pop(Strings.explode("/", gVars.webEnv.getScriptName()));
        this.app_base = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "/" + strval(this.script_name) + "/";

        if (isset(gVars.webEnv.getHttps()) && equal(Strings.strtolower(gVars.webEnv.getHttps()), "on")) {
            this.app_base = QRegExPerl.preg_replace("/^http:\\/\\//", "https://", this.app_base);
        }

        this.selectors = new Array<Object>(
                new ArrayEntry<Object>("@/service$@", new Array<Object>(new ArrayEntry<Object>("GET", "get_service"))),
                new ArrayEntry<Object>("@/categories$@", new Array<Object>(new ArrayEntry<Object>("GET", "get_categories_xml"))),
                new ArrayEntry<Object>(
                    "@/post/(\\d+)$@",
                    new Array<Object>(new ArrayEntry<Object>("GET", "get_post"), new ArrayEntry<Object>("PUT", "put_post"), new ArrayEntry<Object>("DELETE", "delete_post"))),
                new ArrayEntry<Object>("@/posts/?(\\d+)?$@", new Array<Object>(new ArrayEntry<Object>("GET", "get_posts"), new ArrayEntry<Object>("POST", "create_post"))),
                new ArrayEntry<Object>("@/attachments/?(\\d+)?$@", new Array<Object>(new ArrayEntry<Object>("GET", "get_attachment"), new ArrayEntry<Object>("POST", "create_attachment"))),
                new ArrayEntry<Object>(
                    "@/attachment/file/(\\d+)$@",
                    new Array<Object>(new ArrayEntry<Object>("GET", "get_file"), new ArrayEntry<Object>("PUT", "put_file"), new ArrayEntry<Object>("DELETE", "delete_file"))),
                new ArrayEntry<Object>(
                    "@/attachment/(\\d+)$@",
                    new Array<Object>(new ArrayEntry<Object>("GET", "get_attachment"), new ArrayEntry<Object>("PUT", "put_attachment"), new ArrayEntry<Object>("DELETE", "delete_attachment"))));
    }

    public void handle_request() {
        String path = null;
        String method = null;
        String regex = null;
        Array<Object> matches = new Array<Object>();
        Array<Object> funcs = new Array<Object>();
        WP_User u = null;

        path = gVars.webEnv.getPathInfo();
        method = gVars.webEnv.getRequestMethod();

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("REQUEST", method + " " + path + "\n================");

        this.process_conditionals();

        //$this->process_conditionals();

        // exception case for HEAD (treat exactly as GET, but don't output)
        if (equal(method, "HEAD")) {
            this.do_output = false;
            method = "GET";
        }

        // redirect to /service in case no path is found.
        if (equal(Strings.strlen(path), 0) || equal(path, "/")) {
            this.redirect(this.get_service_url());
        }

        // dispatch
        for (Map.Entry javaEntry339 : this.selectors.entrySet()) {
            regex = strval(javaEntry339.getKey());
            funcs = (Array<Object>) javaEntry339.getValue();

            if (QRegExPerl.preg_match(regex, path, matches)) {
                if (isset(funcs.getValue(method))) {
                    // authenticate regardless of the operation and set the current
                    //user. each handler will decide if auth is required or not.
                    this.authenticate();
                    u = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();

                    if (!isset(u) || equal(u.getID(), 0)) {
                        if (booleanval(gVars.always_authenticate)) {
                            this.auth_required("Credentials required.");
                        }
                    }

                    Array.array_shift(matches);
                    FunctionHandling.call_user_func_array(new Callback(strval(funcs.getValue(method)), this), matches);
                    System.exit();
                } else/*
                 * only allow what we have handlers for...
                 */
                 {
                    this.not_allowed(Array.array_keys(funcs));
                }
            }
        }

        // oops, nothing found
        this.not_found();
    }

    public void get_service() {
        Object entries_url = null;
        Object categories_url = null;
        Object media_url = null;
        String accepted_media_types = null;
        Object med = null;
        String atom_prefix = null;
        String atom_blogname = null;
        String service_doc = null;

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_service()");

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to access this blog.", "default"));
        }

        entries_url = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(this.get_entries_url());
        categories_url = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(this.get_categories_url());
        media_url = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(this.get_attachments_url());

        for (Map.Entry javaEntry340 : this.media_content_types.entrySet()) {
            med = javaEntry340.getValue();
            accepted_media_types = accepted_media_types + "<accept>" + strval(med) + "</accept>";
        }

        atom_prefix = "atom";
        atom_blogname = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("name", "raw");
        service_doc = "<service xmlns=\"" + this.ATOMPUB_NS + "\" xmlns:" + atom_prefix + "=\"" + this.ATOM_NS + "\">\n  <workspace>\n    <" + atom_prefix + ":title>" + atom_blogname +
            " Workspace</" + atom_prefix + ":title>\n    <collection href=\"" + strval(entries_url) + "\">\n      <" + atom_prefix + ":title>" + atom_blogname + " Posts</" + atom_prefix +
            ":title>\n      <accept>" + this.ATOM_CONTENT_TYPE + ";type=entry</accept>\n      <categories href=\"" + strval(categories_url) + "\" />\n    </collection>\n    <collection href=\"" +
            strval(media_url) + "\">\n      <" + atom_prefix + ":title>" + atom_blogname + " Media</" + atom_prefix + ":title>\n      " + accepted_media_types +
            "\n    </collection>\n  </workspace>\n</service>\n\n";
        
        this.output(service_doc, this.SERVICE_CONTENT_TYPE);
    }

    public void get_categories_xml() {
        String home = null;
        String categories = null;
        Array<Object> cats = new Array<Object>();
        StdClass cat = null;
        String output = null;
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_categories_xml()");

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to access this blog.", "default"));
        }

        home = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("home"));
        categories = "";
        cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("hierarchical=0&hide_empty=0");

        for (Map.Entry javaEntry341 : new Array<Object>(cats).entrySet()) {
            cat = (StdClass) javaEntry341.getValue();
            categories = categories + "    <category term=\"" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(cat, "name"))) + "\" />\n";
        }

        output = "<app:categories xmlns:app=\"" + this.ATOMPUB_NS + "\"\n\txmlns=\"" + this.ATOM_NS + "\"\n\tfixed=\"yes\" scheme=\"" + home + "\">\n\t" + categories + "\n</app:categories>\n";
        this.output(output, this.CATEGORIES_CONTENT_TYPE);
    }

    /**
     * Create Post (No arguments)
     */
    public void create_post() {
        AtomParser parser = null;
        AtomBase entry = null;
        Array<Object> catnames = new Array<Object>();
        Array<Object> cat = new Array<Object>();
        Array<Object> wp_cats = new Array<Object>();
        Array<Object> post_category = new Array<Object>();
        boolean publish = false;
        String cap = null;
        int blog_ID = 0;
        String post_status = null;
        int post_author = 0;
        String post_title = null;
        String post_content = null;
        String post_excerpt = null;
        Array<Object> pubtimes = new Array<Object>();
        Object post_date = null;
        Object post_date_gmt = null;
        Object post_name = null;
        Array<Object> post_data = new Array<Object>();
        Object postID = 0;
        String output = null;
        
        this.get_accepted_content_type(this.atom_content_types);
        parser = new AtomParser(gVars, gConsts);

        if (!parser.parse()) {
            this.client_error();
        }

        entry = (AtomBase) Array.array_pop(parser.feed.entries);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Received entry:", print_r(gVars.webEnv, entry, true));
        catnames = new Array<Object>();

        for (Map.Entry javaEntry342 : entry.categories.entrySet()) {
            cat = (Array<Object>) javaEntry342.getValue();
            Array.array_push(catnames, cat.getValue("term"));
        }

        wp_cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories(new Array<Object>(new ArrayEntry<Object>("hide_empty", false)));
        post_category = new Array<Object>();

        for (Map.Entry javaEntry343 : wp_cats.entrySet()) {
            StdClass catObj = (StdClass) javaEntry343.getValue();

            if (Array.in_array(StdClass.getValue(catObj, "name"), catnames)) {
                Array.array_push(post_category, StdClass.getValue(catObj, "term_id"));
            }
        }

        publish = ((isset(entry.draft) && equal(Strings.trim(entry.draft), "yes"))
            ? false
            : true);
        cap = (publish
            ? "publish_posts"
            : "edit_posts");

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(cap)) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit/publish new posts.", "default"));
        }

        blog_ID = gVars.blog_id;
        post_status = (publish
            ? "publish"
            : "draft");
        post_author = gVars.user_ID;
        post_title = strval(entry.title.getValue(1));
        post_content = strval(entry.content.getValue(1));
        post_excerpt = strval(entry.summary.getValue(1));
        pubtimes = this.get_publish_time(entry.published);
        post_date = pubtimes.getValue(0);
        post_date_gmt = pubtimes.getValue(1);

        if (isset(gVars.webEnv._SERVER.getValue("HTTP_SLUG"))) {
            post_name = gVars.webEnv._SERVER.getValue("HTTP_SLUG");
        }

        post_data = Array.compact(
                new ArrayEntry("blog_ID", blog_ID),
                new ArrayEntry("post_author", post_author),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("post_name", post_name));
        
        this.escape(post_data);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Inserting Post. Data:", print_r(gVars.webEnv, post_data, true));
        
        postID = getIncluded(PostPage.class, gVars, gConsts).wp_insert_post(post_data);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(postID)) {
            this.internal_error(((WP_Error) postID).get_error_message());
        }

        if (!booleanval(postID)) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, your entry could not be posted. Something wrong happened.", "default"));
        }

        // getting warning here about unable to set headers
		// because something in the cache is printing to the buffer
		// could we clean up wp_set_post_categories or cache to not print
		// this could affect our ability to send back the right headers
        getIncluded(PostPage.class, gVars, gConsts).wp_set_post_categories(intval(postID), post_category);
        
        output = this.get_entry(intval(postID));
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "create_post(" + strval(postID) + ")");
        this.created(intval(postID), output);
    }

    public void get_post(int postID) {
        String output = null;

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", postID)) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to access this post.", "default"));
        }

        this.set_current_entry(postID);
        output = this.get_entry(postID);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_post(" + strval(postID) + ")");
        this.output(output);
    }

    public void put_post(int postID) {
        AtomParser parser = null;
        AtomBase parsed = null;
        boolean publish = false;
        String post_title = null;
        String post_content = null;
        String post_excerpt = null;
        Array<Object> pubtimes = new Array<Object>();
        Object post_date = null;
        Object post_date_gmt = null;
        Object post_modified = null;
        Object post_modified_gmt = null;
        String post_status = null;
        Object post_category = null;
        Object ID = null;
        Array<Object> postdata = new Array<Object>();
        int result = 0;
        
        // checked for valid content-types (atom+xml)
		// quick check and exit
        this.get_accepted_content_type(this.atom_content_types);
        parser = new AtomParser(gVars, gConsts);

        if (!parser.parse()) {
            this.bad_request();
        }

        parsed = (AtomBase) Array.array_pop(parser.feed.entries);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Received UPDATED entry:", print_r(gVars.webEnv, parsed, true));
        
        // check for not found
        this.set_current_entry(postID);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.entry.getValue("ID"))) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit this post.", "default"));
        }

        publish = ((isset(parsed.draft) && equal(Strings.trim(parsed.draft), "yes"))
            ? false
            : true);
        
        post_status = strval(Array.extractVar(gVars.entry, "post_status", post_status, Array.EXTR_OVERWRITE));
        post_category = Array.extractVar(gVars.entry, "post_category", post_category, Array.EXTR_OVERWRITE);
        ID = Array.extractVar(gVars.entry, "ID", ID, Array.EXTR_OVERWRITE);
        post_title = strval(parsed.title.getValue(1));
        post_content = strval(parsed.content.getValue(1));
        post_excerpt = strval(parsed.summary.getValue(1));
        pubtimes = this.get_publish_time(parsed.published);

        // Modified by Numiton gVars.entry.published);
        post_date = pubtimes.getValue(0);
        post_date_gmt = pubtimes.getValue(1);
        pubtimes = this.get_publish_time(parsed.updated);
        post_modified = pubtimes.getValue(0);
        post_modified_gmt = pubtimes.getValue(1);

        // let's not go backwards and make something draft again.
        if (!publish && equal(post_status, "draft")) {
            post_status = (publish ? "publish" : "draft");
        } else if (publish) {
            post_status = "publish";
        }

        postdata = Array.compact(
                new ArrayEntry("ID", ID),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_modified", post_modified),
                new ArrayEntry("post_modified_gmt", post_modified_gmt));
        this.escape(postdata);
        
        result = getIncluded(PostPage.class, gVars, gConsts).wp_update_post(postdata);

        if (!booleanval(result)) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("For some strange yet very annoying reason, this post could not be edited.", "default"));
        }

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "put_post(" + strval(postID) + ")");
        this.ok();
    }

    public void delete_post(int postID) {
        Object result;
        
        // check for not found
        this.set_current_entry(postID);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", postID)) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to delete this post.", "default"));
        }

        if (equal(gVars.entry.getValue("post_type"), "attachment")) {
            this.delete_attachment(postID);
        } else {
            result = getIncluded(PostPage.class, gVars, gConsts).wp_delete_post(postID);

            if (!booleanval(result)) {
                this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("For some strange yet very annoying reason, this post could not be deleted.", "default"));
            }

            getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "delete_post(" + strval(postID) + ")");
            this.ok();
        }
    }

    public void get_attachment(int postID) {
        String output = null;

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("upload_files")) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have permission to upload files.", "default"));
        }

        if (!isset(postID)) {
            this.get_attachments();
        } else {
            this.set_current_entry(postID);
            output = this.get_entry(postID, "attachment");
            getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_attachment(" + strval(postID) + ")");
            this.output(output);
        }
    }

    public void create_attachment() {
        String type = null;
        int fp = 0;
        String bits = null;
        String slug = null;
        String ext;
        Array<Object> file = new Array<Object>();
        Object url = null;
        Array<Object> attachment = new Array<Object>();
        int postID = 0;
        String output = null;
        type = this.get_accepted_content_type();

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("upload_files")) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to upload files.", "default"));
        }

        fp = FileSystemOrSocket.fopen(gVars.webEnv, "php://input", "rb");
        bits = strval(null);

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
            bits = bits + FileSystemOrSocket.fread(gVars.webEnv, fp, 4096);
        }

        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        slug = "";

        if (isset(gVars.webEnv._SERVER.getValue("HTTP_SLUG"))) {
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_file_name(strval(gVars.webEnv._SERVER.getValue("HTTP_SLUG")));
        } else if (isset(gVars.webEnv._SERVER.getValue("HTTP_TITLE"))) {
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_file_name(strval(gVars.webEnv._SERVER.getValue("HTTP_TITLE")));
        } else if (empty(slug)) {
            slug = Strings.substr(Strings.md5(Misc.uniqid(strval(DateTime.microtime()))), 0, 7);
        }

        ext = QRegExPerl.preg_replace("|.*/([a-z0-9]+)|", "$1", strval(gVars.webEnv._SERVER.getValue("CONTENT_TYPE")));
        slug = slug + "." + ext;
        file = getIncluded(FunctionsPage.class, gVars, gConsts).wp_upload_bits(slug, intval(null), bits, null);
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("wp_upload_bits returns:", print_r(gVars.webEnv, file, true));
        
        url = file.getValue("url");
        String fileStr = strval(file.getValue("file"));
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_create_file_in_uploads", fileStr);
        
        // Construct the attachment array
        attachment = new Array<Object>(
                new ArrayEntry<Object>("post_title", slug),
                new ArrayEntry<Object>("post_content", slug),
                new ArrayEntry<Object>("post_status", "attachment"),
                new ArrayEntry<Object>("post_parent", 0),
                new ArrayEntry<Object>("post_mime_type", type),
                new ArrayEntry<Object>("guid", url));
        
        // Save the data
        postID = getIncluded(PostPage.class, gVars, gConsts).wp_insert_attachment(attachment, fileStr, 0);

        if (!booleanval(postID)) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, your entry could not be posted. Something wrong happened.", "default"));
        }

        output = this.get_entry(postID, "attachment");
        this.created(postID, output, "attachment");
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "create_attachment(" + strval(postID) + ")");
    }

    public void put_attachment(int postID) {
        AtomParser parser = null;
        AtomBase parsed = null;
        Object post_title = null;
        Object post_content = null;

        // Added by Numiton
        Object ID = null;
        Object post_category = null;
        Object post_status = null;
        Object post_excerpt = null;
        Array<Object> pubtimes = new Array<Object>();
        Object post_modified = null;
        Object post_modified_gmt = null;
        Array<Object> postdata = new Array<Object>();
        int result = 0;
        
        // checked for valid content-types (atom+xml)
		// quick check and exit
        this.get_accepted_content_type(this.atom_content_types);
        
        parser = new AtomParser(gVars, gConsts);

        if (!parser.parse()) {
            this.bad_request();
        }

        parsed = (AtomBase) Array.array_pop(parser.feed.entries);
        
     // check for not found
        this.set_current_entry(postID);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.entry.getValue("ID"))) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit this post.", "default"));
        }

        ID = Array.extractVar(gVars.entry, "ID", ID, Array.EXTR_OVERWRITE);
        post_category = Array.extractVar(gVars.entry, "post_category", post_category, Array.EXTR_OVERWRITE);
        post_status = Array.extractVar(gVars.entry, "post_status", post_status, Array.EXTR_OVERWRITE);
        post_excerpt = Array.extractVar(gVars.entry, "post_excerpt", post_excerpt, Array.EXTR_OVERWRITE);
        
        post_title = parsed.title.getValue(1);
        post_content = parsed.content.getValue(1);
        pubtimes = this.get_publish_time(parsed.updated);
        post_modified = pubtimes.getValue(0);
        post_modified_gmt = pubtimes.getValue(1);
        
        postdata = Array.compact(
                new ArrayEntry("ID", ID),
                new ArrayEntry("post_content", post_content),
                new ArrayEntry("post_title", post_title),
                new ArrayEntry("post_category", post_category),
                new ArrayEntry("post_status", post_status),
                new ArrayEntry("post_excerpt", post_excerpt),
                new ArrayEntry("post_modified", post_modified),
                new ArrayEntry("post_modified_gmt", post_modified_gmt));
        this.escape(postdata);
        
        result = getIncluded(PostPage.class, gVars, gConsts).wp_update_post(postdata);

        if (!booleanval(result)) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("For some strange yet very annoying reason, this post could not be edited.", "default"));
        }

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "put_attachment(" + strval(postID) + ")");
        this.ok();
    }

    public void delete_attachment(int postID) {
        String location = null;
        Array<Object> filetype = new Array<Object>();
        Object result;
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "delete_attachment(" + strval(postID) + "). File \'" + location + "\' deleted.");
        
        // check for not found
        this.set_current_entry(postID);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", postID)) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to delete this post.", "default"));
        }

        location = strval(getIncluded(PostPage.class, gVars, gConsts).get_post_meta(intval(gVars.entry.getValue("ID")), "_wp_attached_file", true));
        filetype = getIncluded(FunctionsPage.class, gVars, gConsts).wp_check_filetype(location, null);

        if (!isset(location) || !equal("attachment", gVars.entry.getValue("post_type")) || empty(filetype.getValue("ext"))) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("Error ocurred while accessing post metadata for file location.", "default"));
        }

        // delete file
        JFileSystemOrSocket.unlink(gVars.webEnv, location);
        
        // delete attachment
        result = getIncluded(PostPage.class, gVars, gConsts).wp_delete_post(postID);

        if (!booleanval(result)) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("For some strange yet very annoying reason, this post could not be deleted.", "default"));
        }

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "delete_attachment(" + strval(postID) + "). File \'" + location + "\' deleted.");
        this.ok();
    }

    public void get_file(int postID) {
        String location = null;
        Array<Object> filetype = new Array<Object>();
        int fp = 0;
        
        // check for not found
        this.set_current_entry(postID);

        // then whether user can edit the specific post
        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", postID)) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit this post.", "default"));
        }

        location = strval(getIncluded(PostPage.class, gVars, gConsts).get_post_meta(intval(gVars.entry.getValue("ID")), "_wp_attached_file", true));
        filetype = getIncluded(FunctionsPage.class, gVars, gConsts).wp_check_filetype(location, null);

        if (!isset(location) || !equal("attachment", gVars.entry.getValue("post_type")) || empty(filetype.getValue("ext"))) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("Error ocurred while accessing post metadata for file location.", "default"));
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("200");
        Network.header(gVars.webEnv, "Content-Type: " + strval(gVars.entry.getValue("post_mime_type")));
        Network.header(gVars.webEnv, "Connection: close");
        
        fp = FileSystemOrSocket.fopen(gVars.webEnv, location, "rb");

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
            echo(gVars.webEnv, FileSystemOrSocket.fread(gVars.webEnv, fp, 4096));
        }

        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_file(" + strval(postID) + ")");
        System.exit();
    }

    public void put_file(int postID) {
        String location = null;
        Array<Object> filetype = new Array<Object>();
        int fp = 0;
        int localfp = 0;
        Object ID = null;
        Array<Object> pubtimes = new Array<Object>();
        Object post_date = null;
        Object post_date_gmt = null;
        StdClass parsed = new StdClass();

        // TODO Where is it populated?
        Object post_modified = null;
        Object post_modified_gmt = null;
        Array<Object> post_data = new Array<Object>();
        int result = 0;

        // first check if user can upload
        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("upload_files")) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to upload files.", "default"));
        }

        // check for not found
        this.set_current_entry(postID);

        // then whether user can edit the specific post
        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", postID)) {
            this.auth_required(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, you do not have the right to edit this post.", "default"));
        }

        location = strval(getIncluded(PostPage.class, gVars, gConsts).get_post_meta(intval(gVars.entry.getValue("ID")), "_wp_attached_file", true));
        filetype = getIncluded(FunctionsPage.class, gVars, gConsts).wp_check_filetype(location, null);

        if (!isset(location) || !equal("attachment", gVars.entry.getValue("post_type")) || empty(filetype.getValue("ext"))) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("Error ocurred while accessing post metadata for file location.", "default"));
        }

        fp = FileSystemOrSocket.fopen(gVars.webEnv, "php://input", "rb");
        localfp = FileSystemOrSocket.fopen(gVars.webEnv, location, "w+");

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
            FileSystemOrSocket.fwrite(gVars.webEnv, localfp, FileSystemOrSocket.fread(gVars.webEnv, fp, 4096));
        }

        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        FileSystemOrSocket.fclose(gVars.webEnv, localfp);
        ID = gVars.entry.getValue("ID");
        pubtimes = this.get_publish_time(strval(StdClass.getValue(parsed, "published")));

        // Modified by Numiton entry.published);
        post_date = pubtimes.getValue(0);
        post_date_gmt = pubtimes.getValue(1);
        pubtimes = this.get_publish_time(strval(StdClass.getValue(parsed, "updated")));
        post_modified = pubtimes.getValue(0);
        post_modified_gmt = pubtimes.getValue(1);
        
        post_data = Array.compact(
                new ArrayEntry("ID", ID),
                new ArrayEntry("post_date", post_date),
                new ArrayEntry("post_date_gmt", post_date_gmt),
                new ArrayEntry("post_modified", post_modified),
                new ArrayEntry("post_modified_gmt", post_modified_gmt));
        result = getIncluded(PostPage.class, gVars, gConsts).wp_update_post(post_data);

        if (!booleanval(result)) {
            this.internal_error(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, your entry could not be posted. Something wrong happened.", "default"));
        }

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "put_file(" + strval(postID) + ")");
        this.ok();
    }

    public String get_entries_url() {
        return get_entries_url(null);
    }

    public String get_entries_url(Integer page) {
        String path = null;
        String url = null;

        if (equal(gVars.post_type, "attachment")) {
            path = this.MEDIA_PATH;
        } else {
            path = this.ENTRIES_PATH;
        }

        url = this.app_base + path;

        if (isset(page) && is_int(page)) {
            url = url + "/" + strval(page);
        }

        return url;
    }

    public void the_entries_url() {
        the_entries_url(null);
    }

    public void the_entries_url(Integer page) {
        echo(gVars.webEnv, this.get_entries_url(page));
    }

    public String get_categories_url() {
        return get_categories_url(null);
    }

    public String get_categories_url(String deprecated) {
        return this.app_base + this.CATEGORIES_PATH;
    }

    public void the_categories_url() {
        echo(gVars.webEnv, this.get_categories_url());
    }

    public String get_attachments_url() {
        return get_attachments_url(null);
    }

    public String get_attachments_url(Integer page) {
        String url = null;
        url = this.app_base + this.MEDIA_PATH;

        if (isset(page) && is_int(page)) {
            url = url + "/" + strval(page);
        }

        return url;
    }

    public void the_attachments_url() {
        the_attachments_url(null);
    }

    public void the_attachments_url(Integer page) {
        echo(gVars.webEnv, this.get_attachments_url(page));
    }

    public String get_service_url() {
        return this.app_base + this.SERVICE_PATH;
    }

    public String get_entry_url(int postID) {
        String url = null;

        if (!isset(postID)) {
            postID = intval(StdClass.getValue(gVars.post, "ID"));
        }

        url = this.app_base + this.ENTRY_PATH + "/" + strval(postID);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_entry_url() = " + url);

        return url;
    }

    public void the_entry_url() {
        the_entry_url(0);
    }

    public void the_entry_url(int postID) {
        echo(gVars.webEnv, this.get_entry_url(postID));
    }

    public String get_media_url(int postID) {
        String url = null;

        if (!isset(postID)) {
            postID = intval(StdClass.getValue(gVars.post, "ID"));
        }

        url = this.app_base + this.MEDIA_SINGLE_PATH + "/file/" + strval(postID);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_media_url() = " + url);

        return url;
    }

    public void the_media_url() {
        the_media_url(0);
    }

    public void the_media_url(int postID) {
        echo(gVars.webEnv, this.get_media_url(postID));
    }

    public void set_current_entry(int postID) {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "set_current_entry(" + strval(postID) + ")");

        if (!isset(postID)) {
        	// $this->bad_request();
            this.not_found();
        }

        gVars.entry = (Array<Object>) getIncluded(PostPage.class, gVars, gConsts).wp_get_single_post(postID, gConsts.getARRAY_A());

        if (!isset(gVars.entry) || !isset(gVars.entry.getValue("ID"))) {
            this.not_found();
        }
    }

    //		return null;
    public void get_posts(Object page, Object post_type) {
        String feed = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_posts(" + strval(page) + ", \'" + strval(post_type) + "\')");
        feed = this.get_feed(intval(page), strval(post_type));
        this.output(feed);
    }

    public void get_attachments() {
        get_attachments(1, "attachment");
    }

    public void get_attachments(int page) {
        get_attachments(page, "attachment");
    }

    public void get_attachments(int page, String post_type) {
        String feed = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_attachments(" + strval(page) + ", \'" + post_type + "\')");
        post_type = post_type;
        feed = this.get_feed(page, post_type);
        this.output(feed);
    }

    public String get_feed(int page, String post_type) {
        Object count = null;
        int last_page = 0;
        Integer next_page = null;
        Integer prev_page = null;
        int self_page = 0;
        String feed = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_feed(" + strval(page) + ", \'" + post_type + "\')");
        OutputControl.ob_start(gVars.webEnv);

        if (!isset(page)) {
            page = 1;
        }

//        page = page;
        count = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("posts_per_rss");
        
        getIncluded(FunctionsPage.class, gVars, gConsts).wp("what_to_show=posts&posts_per_page=" + strval(count) + "&offset=" + strval(intval(count) * (page - 1)) + "&orderby=modified");
        
        gVars.post = gVars.post;
        gVars.posts = gVars.posts;
        gVars.wp = gVars.wp;
        gVars.wp_query = gVars.wp_query;
        gVars.wpdb = gVars.wpdb;
        gVars.blog_id = gVars.blog_id;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "query_posts(# " + print_r(gVars.webEnv, gVars.wp_query, true) + "#)");
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "total_count(# " + gVars.wp_query.max_num_pages + " #)");
        last_page = gVars.wp_query.max_num_pages;
        next_page = (((page + 1) > last_page)
            ? null
            : (page + 1));
        prev_page = (((page - 1) < 1)
            ? null
            : (page - 1));
        last_page = ((equal(last_page, 1) || equal(last_page, 0))
            ? null
            : last_page);
        self_page = ((page > 1)
            ? page
            : null);
        echo(gVars.webEnv, "<feed xmlns=\"");
        echo(gVars.webEnv, this.ATOM_NS);
        echo(gVars.webEnv, "\" xmlns:app=\"");
        echo(gVars.webEnv, this.ATOMPUB_NS);
        echo(gVars.webEnv, "\" xml:lang=\"");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_language"));
        echo(gVars.webEnv, "\">\n<id>");
        this.the_entries_url();
        echo(gVars.webEnv, "</id>\n<updated>");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), true));
        echo(gVars.webEnv, "</updated>\n<title type=\"text\">");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("name");
        echo(gVars.webEnv, "</title>\n<subtitle type=\"text\">");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");
        echo(gVars.webEnv, "</subtitle>\n<link rel=\"first\" type=\"");
        echo(gVars.webEnv, this.ATOM_CONTENT_TYPE);
        echo(gVars.webEnv, "\" href=\"");
        this.the_entries_url();
        echo(gVars.webEnv, "\" />\n");

        if (isset(prev_page)) {
            echo(gVars.webEnv, "<link rel=\"previous\" type=\"");
            echo(gVars.webEnv, this.ATOM_CONTENT_TYPE);
            echo(gVars.webEnv, "\" href=\"");
            this.the_entries_url(prev_page);
            echo(gVars.webEnv, "\" />\n");
        } else {
        }

        if (isset(next_page)) {
            echo(gVars.webEnv, "<link rel=\"next\" type=\"");
            echo(gVars.webEnv, this.ATOM_CONTENT_TYPE);
            echo(gVars.webEnv, "\" href=\"");
            this.the_entries_url(next_page);
            echo(gVars.webEnv, "\" />\n");
        } else {
        }

        echo(gVars.webEnv, "<link rel=\"last\" type=\"");
        echo(gVars.webEnv, this.ATOM_CONTENT_TYPE);
        echo(gVars.webEnv, "\" href=\"");
        this.the_entries_url(last_page);
        echo(gVars.webEnv, "\" />\n<link rel=\"self\" type=\"");
        echo(gVars.webEnv, this.ATOM_CONTENT_TYPE);
        echo(gVars.webEnv, "\" href=\"");
        this.the_entries_url(self_page);
        echo(gVars.webEnv, "\" />\n<rights type=\"text\">Copyright ");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y", getIncluded(PostPage.class, gVars, gConsts).get_lastpostdate("blog"), true));
        echo(gVars.webEnv, "</rights>\n");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("atom");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                this.echo_entry();
            }
        }

        echo(gVars.webEnv, "</feed>\n");
        feed = OutputControl.ob_get_contents(gVars.webEnv);
        OutputControl.ob_end_clean(gVars.webEnv);

        return feed;
    }

    public String get_entry(int postID) {
        return get_entry(postID, "post");
    }

    public String get_entry(int postID, String post_type) {
        String varname = null;
        String entry = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "get_entry(" + strval(postID) + ", \'" + post_type + "\')");
        OutputControl.ob_start(gVars.webEnv);

        {
            int javaSwitchSelector34 = 0;

            if (equal(post_type, "post")) {
                javaSwitchSelector34 = 1;
            }

            if (equal(post_type, "attachment")) {
                javaSwitchSelector34 = 2;
            }

            switch (javaSwitchSelector34) {
            case 1: {
                varname = "p";

                break;
            }

            case 2: {
                varname = "attachment_id";

                break;
            }
            }
        }

        getIncluded(QueryPage.class, gVars, gConsts).query_posts(varname + "=" + strval(postID));

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                this.echo_entry();
                getIncluded(Wp_appPage.class, gVars, gConsts).log_app("$post", print_r(gVars.webEnv, gVars.post, true));
                entry = OutputControl.ob_get_contents(gVars.webEnv);

                break;
            }
        }

        OutputControl.ob_end_clean(gVars.webEnv);
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("get_entry returning:", entry);

        return entry;
    }

    public void echo_entry() {
        StdClass category = null;
        echo(gVars.webEnv, "<entry xmlns=\"");
        echo(gVars.webEnv, this.ATOM_NS);
        echo(gVars.webEnv, "\"\n       xmlns:app=\"");
        echo(gVars.webEnv, this.ATOMPUB_NS);
        echo(gVars.webEnv, "\" xml:lang=\"");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_language"));
        echo(gVars.webEnv, "\">\n\t<id>");
        getIncluded(Post_templatePage.class, gVars, gConsts).the_guid(intval(StdClass.getValue(gVars.post, "ID")));
        echo(gVars.webEnv, "</id>\n");
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    echo_entry_content_type = srcArray.getValue(0);
                    echo_entry_content = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(getIncluded(FeedPage.class, gVars, gConsts).prep_atom_text_construct(getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0)));
        echo(gVars.webEnv, "\t<title type=\"");
        echo(gVars.webEnv, echo_entry_content_type);
        echo(gVars.webEnv, "\">");
        echo(gVars.webEnv, echo_entry_content);
        echo(gVars.webEnv, "</title>\n\t<updated>");
        echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).get_post_modified_time("Y-m-d\\TH:i:s\\Z", true));
        echo(gVars.webEnv, "</updated>\n\t<published>");
        echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("Y-m-d\\TH:i:s\\Z", true));
        echo(gVars.webEnv, "</published>\n\t<app:edited>");
        echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).get_post_modified_time("Y-m-d\\TH:i:s\\Z", true));
        echo(gVars.webEnv, "</app:edited>\n\t<app:control>\n\t\t<app:draft>");
        echo(gVars.webEnv, equal(StdClass.getValue(gVars.post, "post_status"), "draft")
            ? "yes"
            : "no");
        echo(gVars.webEnv, "</app:draft>\n\t</app:control>\n\t<author>\n\t\t<name>");
        getIncluded(Author_templatePage.class, gVars, gConsts).the_author("", true);
        echo(gVars.webEnv, "</name>\n");

        if (booleanval(getIncluded(Author_templatePage.class, gVars, gConsts).get_the_author_url()) &&
                !equal(getIncluded(Author_templatePage.class, gVars, gConsts).get_the_author_url(), "http://")) {
            echo(gVars.webEnv, "\t\t<uri>");
            getIncluded(Author_templatePage.class, gVars, gConsts).the_author_url();
            echo(gVars.webEnv, "</uri>\n");
        }

        echo(gVars.webEnv, "\t</author>\n");

        if (equal(StdClass.getValue(gVars.post, "post_type"), "attachment")) {
            echo(gVars.webEnv, "\t<link rel=\"edit-media\" href=\"");
            this.the_media_url();
            echo(gVars.webEnv, "\" />\n\t<content type=\"");
            echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_mime_type"));
            echo(gVars.webEnv, "\" src=\"");
            getIncluded(Post_templatePage.class, gVars, gConsts).the_guid(0);
            echo(gVars.webEnv, "\"/>\n");
        } else {
            echo(gVars.webEnv, "\t<link href=\"");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "\" />\n");

            if (booleanval(Strings.strlen(strval(StdClass.getValue(gVars.post, "post_content"))))) {
                new ListAssigner<Object>() {
                        public Array<Object> doAssign(Array<Object> srcArray) {
                            if (strictEqual(srcArray, null)) {
                                return null;
                            }

                            echo_entry_content_type = srcArray.getValue(0);
                            echo_entry_content = srcArray.getValue(1);

                            return srcArray;
                        }
                    }.doAssign(
                    getIncluded(FeedPage.class, gVars, gConsts).prep_atom_text_construct(getIncluded(Post_templatePage.class, gVars, gConsts).get_the_content("(more...)", 0, "")));
                echo(gVars.webEnv, "\t<content type=\"");
                echo(gVars.webEnv, echo_entry_content_type);
                echo(gVars.webEnv, "\">");
                echo(gVars.webEnv, echo_entry_content);
                echo(gVars.webEnv, "</content>\n");
            } else {
            }
        }

        echo(gVars.webEnv, "\t<link rel=\"edit\" href=\"");
        this.the_entry_url();
        echo(gVars.webEnv, "\" />\n");

        for (Map.Entry javaEntry344 : (Set<Map.Entry>) getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(false)).entrySet()) {
            category = (StdClass) javaEntry344.getValue();
            echo(gVars.webEnv, "\t<category scheme=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("home");
            echo(gVars.webEnv, "\" term=\"");
            echo(gVars.webEnv, StdClass.getValue(category, "name"));
            echo(gVars.webEnv, "\" />\n");
        }

        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    echo_entry_content_type = srcArray.getValue(0);
                    echo_entry_content = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(getIncluded(FeedPage.class, gVars, gConsts).prep_atom_text_construct(getIncluded(Post_templatePage.class, gVars, gConsts).get_the_excerpt("")));
        echo(gVars.webEnv, "\t<summary type=\"");
        echo(gVars.webEnv, echo_entry_content_type);
        echo(gVars.webEnv, "\">");
        echo(gVars.webEnv, echo_entry_content);
        echo(gVars.webEnv, "</summary>\n</entry>\n");
    }

    public void ok() {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "200: OK");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("200");
        System.exit();
    }

    public void no_content() {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "204: No Content");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("204");
        echo(gVars.webEnv, "Deleted.");
        System.exit();
    }

    public void internal_error(Object msg) {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "500: Server Error");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("500");
        echo(gVars.webEnv, msg);
        System.exit();
    }

    public void bad_request() {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "400: Bad Request");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("400");
        System.exit();
    }

    public void length_required() {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "411: Length Required");
        Network.header(gVars.webEnv, "HTTP/1.1 411 Length Required");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("411");
        System.exit();
    }

    public void invalid_media() {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "415: Unsupported Media Type");
        Network.header(gVars.webEnv, "HTTP/1.1 415 Unsupported Media Type");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        System.exit();
    }

    public void not_found() {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "404: Not Found");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("404");
        System.exit();
    }

    public void not_allowed(Array<?> allow) {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "405: Not Allowed");
        Network.header(gVars.webEnv, "Allow: " + Strings.join(",", allow));
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("405");
        System.exit();
    }

    public void redirect(String url) {
        Object escaped_url = null;
        Object content = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "302: Redirect");
        escaped_url = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(url);
        content = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n<html>\n  <head>\n    <title>302 Found</title>\n  </head>\n<body>\n  <h1>Found</h1>\n  <p>The document has moved <a href=\"" +
            strval(escaped_url) + "\">here</a>.</p>\n  </body>\n</html>\n\n";
        Network.header(gVars.webEnv, "HTTP/1.1 302 Moved");
        Network.header(gVars.webEnv, "Content-Type: text/html");
        Network.header(gVars.webEnv, "Location: " + url);
        echo(gVars.webEnv, content);
        System.exit();
    }

    public void client_error() {
        client_error("Client Error");
    }

    public void client_error(String msg) {
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "400: Client Error");
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header(intval("400"));
        System.exit();
    }

    public void created(int post_ID, String content) {
        created(post_ID, content, "post");
    }

    public void created(int post_ID, String content, String post_type) {
        String edit = null;
        String ctloc = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("created()::$post_ID", strval(post_ID) + ", " + post_type);
        edit = this.get_entry_url(post_ID);

        {
            int javaSwitchSelector35 = 0;

            if (equal(post_type, "post")) {
                javaSwitchSelector35 = 1;
            }

            if (equal(post_type, "attachment")) {
                javaSwitchSelector35 = 2;
            }

            switch (javaSwitchSelector35) {
            case 1: {
                ctloc = this.get_entry_url(post_ID);

                break;
            }

            case 2: {
                edit = this.app_base + "attachments/" + strval(post_ID);

                break;
            }
            }
        }

        Network.header(gVars.webEnv, "Content-Type: " + this.ATOM_CONTENT_TYPE);

        if (isset(ctloc)) {
            Network.header(gVars.webEnv, "Content-Location: " + ctloc);
        }

        Network.header(gVars.webEnv, "Location: " + edit);
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("201");
        echo(gVars.webEnv, content);
        System.exit();
    }

    public void auth_required(String msg) {
        String content = null;
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Status", "401: Auth Required");
        getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
        Network.header(gVars.webEnv, "WWW-Authenticate: Basic realm=\"WordPress Atom Protocol\"");
        Network.header(gVars.webEnv, "HTTP/1.1 401 " + msg);
        Network.header(gVars.webEnv, "Status: " + msg);
        Network.header(gVars.webEnv, "Content-Type: text/html");
        content = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n<html>\n  <head>\n    <title>401 Unauthorized</title>\n  </head>\n<body>\n    <h1>401 Unauthorized</h1>\n    <p>" + msg +
            "</p>\n  </body>\n</html>\n\n";
        echo(gVars.webEnv, content);
        System.exit();
    }

    public void output(String xml) {
        output(xml, "application/atom+xml");
    }

    public void output(String xml, String ctype) {
        getIncluded(FunctionsPage.class, gVars, gConsts).status_header("200");
        xml = "<?xml version=\"1.0\" encoding=\"" + Strings.strtolower(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"))) + "\"?>" + "\n" + xml;
        Network.header(gVars.webEnv, "Connection: close");
        Network.header(gVars.webEnv, "Content-Length: " + strval(Strings.strlen(xml)));
        Network.header(gVars.webEnv, "Content-Type: " + ctype);
        Network.header(gVars.webEnv, "Content-Disposition: attachment; filename=atom.xml");
        Network.header(gVars.webEnv, "Date: " + DateTime.date("r"));

        if (this.do_output) {
            echo(gVars.webEnv, xml);
        }

        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("function", "output:\n" + xml);
        System.exit();
    }

    public void escape(Array<Object> array) {
        Object v = null;

        /* Do not change type */
        Object k = null;

        for (Map.Entry javaEntry345 : array.entrySet()) {
            k = javaEntry345.getKey();
            v = javaEntry345.getValue();

            if (is_array(v)) {
                this.escape(array.getArrayValue(k));
            } else if (is_object(v)) {
            } else/*
             * skip skip
             */
             {
                array.putValue(k, gVars.wpdb.escape(strval(v)));
            }
        }
    }

    /**
     * Access credential through various methods and perform login
     */
    public void authenticate() {
        Array<Object> login_data;
        boolean already_md5 = false;
        WP_User current_user = null;
        
        login_data = new Array<Object>();
        already_md5 = false;
        
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("authenticate()", print_r(gVars.webEnv, gVars.webEnv._ENV, true));

        // if using mod_rewrite/ENV hack
		// http://www.besthostratings.com/articles/http-auth-php-cgi.html
        if (isset(gVars.webEnv._SERVER.getValue("HTTP_AUTHORIZATION"))) {
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        gVars.webEnv._SERVER.putValue("PHP_AUTH_USER", srcArray.getValue(0));
                        gVars.webEnv._SERVER.putValue("PHP_AUTH_PW", srcArray.getValue(1));

                        return srcArray;
                    }
                }.doAssign(Strings.explode(":", URL.base64_decode(Strings.substr(strval(gVars.webEnv._SERVER.getValue("HTTP_AUTHORIZATION")), 6))));
        }

        // If Basic Auth is working...
        if (isset(gVars.webEnv.getPhpAuthUser()) && isset(gVars.webEnv.getPhpAuthPw())) {
            login_data = new Array<Object>(new ArrayEntry<Object>("login", gVars.webEnv.getPhpAuthUser()), new ArrayEntry<Object>("password", gVars.webEnv.getPhpAuthPw()));
            getIncluded(Wp_appPage.class, gVars, gConsts).log_app("Basic Auth", strval(login_data.getValue("login")));
        } else {
        	// else, do cookie-based authentication
            if (true)/*Modified by Numiton*/
             {
                login_data = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_cookie_login();
                already_md5 = true;
            }
        }

        // call wp_login and set current user
        if (!empty(login_data) && getIncluded(PluggablePage.class, gVars, gConsts).wp_login(strval(login_data.getValue("login")), strval(login_data.getValue("password")), already_md5)) {
            current_user = new WP_User(gVars, gConsts, 0, strval(login_data.getValue("login")));
            getIncluded(Wp_appPage.class, gVars, gConsts).wp_set_current_user(current_user.getID(), "");
            getIncluded(Wp_appPage.class, gVars, gConsts).log_app("authenticate()", strval(login_data.getValue("login")));
        }
    }

    public String get_accepted_content_type() {
        return get_accepted_content_type(null);
    }

    public String get_accepted_content_type(Array<Object> types) {
        String t = null;

        if (!isset(types)) {
            types = this.media_content_types;
        }

        if (!isset(gVars.webEnv._SERVER.getValue("CONTENT_LENGTH")) || !isset(gVars.webEnv._SERVER.getValue("CONTENT_TYPE"))) {
            this.length_required();
        }

        get_accepted_content_type_type = strval(gVars.webEnv._SERVER.getValue("CONTENT_TYPE"));
        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    get_accepted_content_type_type = srcArray.getValue(0);
                    get_accepted_content_type_subtype = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(Strings.explode("/", get_accepted_content_type_type));
        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    get_accepted_content_type_subtype = srcArray.getValue(0);

                    return srcArray;
                }
            }.doAssign(Strings.explode(";", get_accepted_content_type_subtype));
        getIncluded(Wp_appPage.class, gVars, gConsts).log_app("get_accepted_content_type", "type=" + get_accepted_content_type_type + ", subtype=" + get_accepted_content_type_subtype);

        for (Map.Entry javaEntry346 : types.entrySet()) {
            t = strval(javaEntry346.getValue());
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        get_accepted_content_type_acceptedType = srcArray.getValue(0);
                        get_accepted_content_type_acceptedSubtype = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(Strings.explode("/", t));

            if (equal(get_accepted_content_type_acceptedType, "*") || equal(get_accepted_content_type_acceptedType, get_accepted_content_type_type)) {
                if (equal(get_accepted_content_type_acceptedSubtype, "*") || equal(get_accepted_content_type_acceptedSubtype, get_accepted_content_type_subtype)) {
                    return get_accepted_content_type_type + "/" + get_accepted_content_type_subtype;
                }
            }
        }

        this.invalid_media();

        return "";
    }

    public void process_conditionals() {
        String wp_last_modified = null;
        String wp_etag = null;
        String client_etag = null;
        String client_last_modified = null;
        int client_modified_timestamp = 0;
        int wp_modified_timestamp = 0;

        if (empty(this.params)) {
            return;
        }

        if (equal(gVars.webEnv.getRequestMethod(), "DELETE")) {
            return;
        }

        {
            int javaSwitchSelector36 = 0;

            if (equal(this.params.getValue(0), this.ENTRY_PATH)) {
                javaSwitchSelector36 = 1;
            }

            if (equal(this.params.getValue(0), this.ENTRIES_PATH)) {
                javaSwitchSelector36 = 2;
            }

            switch (javaSwitchSelector36) {
            case 1: {
                gVars.post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).wp_get_single_post(intval(this.params.getValue(1)), gConsts.getOBJECT());
                wp_last_modified = getIncluded(General_templatePage.class, gVars, gConsts).get_post_modified_time("D, d M Y H:i:s", true);
                gVars.post = null;

                break;
            }

            case 2: {
                wp_last_modified = getIncluded(FunctionsPage.class, gVars, gConsts)
                                         .mysql2date("D, d M Y H:i:s", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), false) + " GMT";

                break;
            }

            default:return;
            }
        }

        wp_etag = Strings.md5(wp_last_modified);
        Network.header(gVars.webEnv, "Last-Modified: " + wp_last_modified);
        Network.header(gVars.webEnv, "ETag: " + wp_etag);

        // Support for Conditional GET
        if (isset(gVars.webEnv._SERVER.getValue("HTTP_IF_NONE_MATCH"))) {
            client_etag = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._SERVER.getValue("HTTP_IF_NONE_MATCH")));
        } else {
            client_etag = strval(false);
        }

        client_last_modified = Strings.trim(strval(gVars.webEnv._SERVER.getValue("HTTP_IF_MODIFIED_SINCE")));
        // If string is empty, return 0. If not, attempt to parse into a timestamp
        client_modified_timestamp = (booleanval(client_last_modified)
            ? QDateTime.strtotime(client_last_modified)
            : 0);
        
        // Make a timestamp for our most recent modification...
        wp_modified_timestamp = QDateTime.strtotime(wp_last_modified);

        if ((booleanval(client_last_modified) && booleanval(client_etag))
                ? ((client_modified_timestamp >= wp_modified_timestamp) && equal(client_etag, wp_etag))
                    : ((client_modified_timestamp >= wp_modified_timestamp) || equal(client_etag, wp_etag))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).status_header(strval(304));
            System.exit();
        }
    }

    public int rfc3339_str2time(String str) {
        Array<Object> match = new Array<Object>();

        //		match = false;
        if (!QRegExPerl.preg_match("/(\\d{4}-\\d{2}-\\d{2})T(\\d{2}\\:\\d{2}\\:\\d{2})\\.?\\d{0,3}(Z|[+-]+\\d{2}\\:\\d{2})/", str, match)) {
            return intval(false);
        }

        if (equal(match.getValue(3), "Z")) {
            equal(match.getValue(3), "+0000");
        }

        return QDateTime.strtotime(strval(match.getValue(1)) + " " + strval(match.getValue(2)) + " " + strval(match.getValue(3)));
    }

    public Array<Object> get_publish_time(String published) {
        int pubtime = 0;
        pubtime = this.rfc3339_str2time(published);

        if (!booleanval(pubtime)) {
            return new Array<Object>(
                new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0)),
                new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1)));
        } else {
            return new Array<Object>(new ArrayEntry<Object>(DateTime.date("Y-m-d H:i:s", pubtime)), new ArrayEntry<Object>(DateTime.gmdate("Y-m-d H:i:s", pubtime)));
        }
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
