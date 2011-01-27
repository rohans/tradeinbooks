/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Import.java,v 1.3 2008/10/10 16:48:04 numiton Exp $
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
package org.numiton.nwp.wp_admin._import;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.*;
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.UserPage;

import com.numiton.FunctionHandling;
import com.numiton.Options;
import com.numiton.RegExPerl;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.gzip.GZIP;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class WP_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Import.class.getName());
    
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    
    public Array<Object> post_ids_processed = new Array<Object>();
    public Array<Object> orphans = new Array<Object>();
    public String file;
    public int id;
    public Array<Object> mtnames = new Array<Object>();
    public Array<Object> newauthornames = new Array<Object>();
    public Array<Object> allauthornames = new Array<Object>();
    public Array<Object> author_ids = new Array<Object>();
    public Array<Object> tags = new Array<Object>();
    public Array<Object> categories = new Array<Object>();
    public Object j = -1;
    public boolean fetch_attachments = false;
    public Array<Object> url_remap = new Array<Object>();
    public String post;

    public WP_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import nWordPress", "default") + "</h2>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public String unhtmlentities(String string) { // From php.net for < 4.3 compat
        Array<String> trans_tbl = new Array<String>();
        trans_tbl = Strings.get_html_translation_table(Strings.HTML_ENTITIES);
        trans_tbl = Array.array_flip(trans_tbl);

        return Strings.strtr(string, trans_tbl);
    }

    public void greet() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "Howdy! Upload your nWordPress eXtended RSS (WXR) file and we&#8217;ll import the posts, comments, custom fields, and categories into this blog.",
                        "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Choose a nWordPress WXR file to upload, then click Upload file and import.", "default") + "</p>");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_import_upload_form("admin.php?import=wordpress&amp;step=1");
        echo(gVars.webEnv, "</div>");
    }

    public String get_tag(String string, String tag) {
        Array<Object> _return = new Array<Object>();
        QRegExPerl.preg_match("|<" + tag + ".*?>(.*?)</" + tag + ">|is", string, _return);

        String _returnStr = QRegExPerl.preg_replace("|^<!\\[CDATA\\[(.*)\\]\\]>$|s", "$1", strval(_return.getValue(1)));
        _returnStr = gVars.wpdb.escape(Strings.trim(_returnStr));

        return _returnStr;
    }

    public boolean has_gzip() {
        return true;
    }

    /*Modified by Numiton*/ public int fopen(String filename, String mode) {
        if (this.has_gzip()) {
            return GZIP.gzopen(gVars.webEnv, filename, mode);
        }

        return FileSystemOrSocket.fopen(gVars.webEnv, filename, mode);
    }

    public boolean feof(int fp) {
        if (this.has_gzip()) {
            return GZIP.gzeof(gVars.webEnv, fp);
        }

        return FileSystemOrSocket.feof(gVars.webEnv, fp);
    }

    public String fgets(int fp) {
        return fgets(fp, 8192);
    }

    public String fgets(int fp, int len) {
        if (this.has_gzip()) {
            return GZIP.gzgets(gVars.webEnv, fp, len);
        }

        return FileSystemOrSocket.fgets(gVars.webEnv, fp, len);
    }

    public boolean fclose(int fp) {
        if (this.has_gzip()) {
            return GZIP.gzclose(gVars.webEnv, fp);
        }

        return FileSystemOrSocket.fclose(gVars.webEnv, fp);
    }

    public boolean get_entries() {
        return get_entries(null);
    }

    public boolean get_entries(Array<Object> process_post_func) {
        boolean doing_entry = false;
        boolean is_wxr_file = false;
        int fp = 0;
        String importline = null;
        Array<Object> category = new Array<Object>();
        Array<Object> tag = new Array<Object>();
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        doing_entry = false;
        is_wxr_file = false;
        fp = this.fopen(this.file, "r");

        if (booleanval(fp)) {
            while (!this.feof(fp)) {
                importline = Strings.rtrim(this.fgets(fp));

				// this doesn't check that the file is perfectly valid but will at least confirm that it's not the wrong format altogether
                if (!is_wxr_file && QRegExPerl.preg_match("|xmlns:wp=\"http://wordpress[.]org/export/\\d+[.]\\d+/\"|", importline)) {
                    is_wxr_file = true;
                }

                if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(importline, "<wp:category>"))) {
                    QRegExPerl.preg_match("|<wp:category>(.*?)</wp:category>|is", importline, category);
                    this.categories.putValue(category.getValue(1));

                    continue;
                }

                if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(importline, "<wp:tag>"))) {
                    QRegExPerl.preg_match("|<wp:tag>(.*?)</wp:tag>|is", importline, tag);
                    this.tags.putValue(tag.getValue(1));

                    continue;
                }

                if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(importline, "<item>"))) {
                    this.post = "";
                    doing_entry = true;

                    continue;
                }

                if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(importline, "</item>"))) {
                    doing_entry = false;

                    if (booleanval(process_post_func)) {
                        FunctionHandling.call_user_func(new Callback(process_post_func), this.post);
                    }

                    continue;
                }

                if (doing_entry) {
                    this.post = this.post + importline + "\n";
                }
            }

            this.fclose(fp);
        }

        return is_wxr_file;
    }

    public Array<Object> get_wp_authors() {
        Array<Object> temp = new Array<Object>();
        Array<Object> authors = new Array<Object>();
        int y = 0;
        Object next = null;
        int x = 0;
        
		// We need to find unique values of author names, while preserving the order, so this function emulates the unique_value(); php function, without the sorting.
        temp = this.allauthornames;
        authors.putValue(0, Array.array_shift(temp));
        y = Array.count(temp) + 1;

        for (x = 1; x < y; x++) {
            next = Array.array_shift(temp);

            if (!Array.in_array(next, authors)) {
                Array.array_push(authors, next);
            }
        }

        return authors;
    }

    public void get_authors_from_post() {
        Object i = null;
        StdClass user;
        Object in_author_name = null;
        String new_author_name = null;
        int user_id = 0;

		// this will populate $this->author_ids with a list of author_names => user_ids
        
        for (Map.Entry javaEntry82 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("author_in").entrySet()) {
            i = javaEntry82.getKey();
            in_author_name = javaEntry82.getValue();

            if (!empty(gVars.webEnv._POST.getArrayValue("user_select").getValue(i))) {
				// an existing user was selected in the dropdown list
                user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(gVars.webEnv._POST.getArrayValue("user_select").getValue(i)));

                if (isset(StdClass.getValue(user, "ID"))) {
                    this.author_ids.putValue(in_author_name, StdClass.getValue(user, "ID"));
                }
            } else if (booleanval(this.allow_create_users())) {
				// nothing was selected in the dropdown list, so we'll use the name in the text field
            	
                new_author_name = Strings.trim(strval(gVars.webEnv._POST.getArrayValue("user_create").getValue(i)));
				// if the user didn't enter a name, assume they want to use the same name as in the import file
                if (empty(new_author_name)) {
                    new_author_name = strval(in_author_name);
                }

                user_id = getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(new_author_name);

                if (!booleanval(user_id)) {
                    user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_create_user(new_author_name, getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12), "");
                }

                this.author_ids.putValue(in_author_name, user_id);
            }

			// failsafe: if the user_id was invalid, default to the current user
            if (empty(this.author_ids.getValue(in_author_name))) {
                this.author_ids.putValue(in_author_name, gVars.current_user.getID());
            }
        }
    }

    public void wp_authors_form() {
        Array<Object> authors = new Array<Object>();
        int j = 0;
        String author = null;
        echo(gVars.webEnv, "<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Assign Authors", "default");
        echo(gVars.webEnv, "</h2>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "To make it easier for you to edit and save the imported posts and drafts, you may want to change the name of the author of the posts. For example, you may want to import all the entries as <code>admin</code>s entries.",
                "default");
        echo(gVars.webEnv, "</p>\n");

        if (booleanval(this.allow_create_users())) {
            echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts)
                    .__("If a new user is created by nWordPress, a password will be randomly generated. Manually change the user\'s details if necessary.", "default") + "</p>\n");
        }

        authors = this.get_wp_authors();
        echo(gVars.webEnv, "<ol id=\"authors\">");
        echo(gVars.webEnv, "<form action=\"?import=wordpress&amp;step=2&amp;id=" + strval(this.id) + "\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-wordpress", "_wpnonce", true, true);
        j = -1;

        for (Map.Entry javaEntry83 : authors.entrySet()) {
            author = strval(javaEntry83.getValue());
            ++j;
            echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import author:", "default") + " <strong>" + author + "</strong><br />");
            this.users_form(j, author);
            echo(gVars.webEnv, "</li>");
        }

        if (booleanval(this.allow_fetch_attachments())) {
            echo(gVars.webEnv, "</ol>\n<h2>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Import Attachments", "default");
            echo(gVars.webEnv, "</h2>\n<p>\n\t<input type=\"checkbox\" value=\"1\" name=\"attachments\" id=\"import-attachments\" />\n\t<label for=\"import-attachments\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Download and import file attachments", "default");
            echo(gVars.webEnv, "</label>\n</p>\n\n");
        }

        echo(
            gVars.webEnv,
            "<input type=\"submit\" value=\"" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Submit", "default")) + "\">" +
            "<br />");
        echo(gVars.webEnv, "</form>");
    }

    public void users_form(int n, String author) {
        Array<Object> users = new Array<Object>();
        StdClass user = null;

        if (booleanval(this.allow_create_users())) {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Create user %1$s or map to existing", "default"),
                " <input type=\"text\" value=\"" + author + "\" name=\"" + "user_create[" + strval(n) + "]" + "\" maxlength=\"30\"> <br />");
        } else {
            echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Map to existing", "default") + "<br />");
        }

		// keep track of $n => $author name
        echo(gVars.webEnv, "<input type=\"hidden\" name=\"author_in[" + strval(n) + "]\" value=\"" + Strings.htmlspecialchars(author) + "\" />");
        
        users = getIncluded(UserPage.class, gVars, gConsts).get_users_of_blog(intval(""));
        
        echo(gVars.webEnv, "<select name=\"user_select[");
        echo(gVars.webEnv, n);
        echo(gVars.webEnv, "]\">\n\t<option value=\"0\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("- Select -", "default");
        echo(gVars.webEnv, "</option>\n\t");

        for (Map.Entry javaEntry84 : users.entrySet()) {
            user = (StdClass) javaEntry84.getValue();
            echo(gVars.webEnv, "<option value=\"" + StdClass.getValue(user, "user_id") + "\">" + StdClass.getValue(user, "user_login") + "</option>");
        }

        echo(gVars.webEnv, "\t</select>\n\t");
    }

    public void select_authors() {
        boolean is_wxr_file = false;
        is_wxr_file = this.get_entries(Callback.createCallbackArray(this, "process_author"));

        if (is_wxr_file) {
            this.wp_authors_form();
        } else {
            echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Invalid file", "default") + "</h2>");
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Please upload a valid WXR (WordPress eXtended RSS) export file.", "default") + "</p>");
        }
    }

	// fetch the user ID for a given author name, respecting the mapping preferences
    public int checkauthor(String author) {
        if (!empty(this.author_ids.getValue(author))) {
            return intval(this.author_ids.getValue(author));
        }

		// failsafe: map to the current user
        return gVars.current_user.getID();
    }

    public void process_categories() {
        Array<Object> cat_names = new Array<Object>();
        String cat_name = null;
        String c = null;
        String category_nicename;
        int posts_private = 0;
        int links_private = 0;
        String parent;
        String category_parent = null;
        Array<Object> catarr = new Array<Object>();
        Object cat_ID = null;
        cat_names = new Array<Object>(getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("category", "fields=names"));

        while (booleanval(c = strval(Array.array_shift(this.categories)))) {
            cat_name = Strings.trim(this.get_tag(c, "wp:cat_name"));

			// If the category exists we leave it alone
            if (Array.in_array(cat_name, cat_names)) {
                continue;
            }

            category_nicename = this.get_tag(c, "wp:category_nicename");
            posts_private = intval(this.get_tag(c, "wp:posts_private"));
            links_private = intval(this.get_tag(c, "wp:links_private"));
            parent = this.get_tag(c, "wp:category_parent");

            if (empty(parent)) {
                category_parent = "0";
            } else {
                category_parent = strval((((org.numiton.nwp.wp_admin.includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_admin.includes.TaxonomyPage.class, gVars, gConsts))).category_exists(parent));
            }

            catarr = Array.compact(
                    new ArrayEntry("category_nicename", category_nicename),
                    new ArrayEntry("category_parent", category_parent),
                    new ArrayEntry("posts_private", posts_private),
                    new ArrayEntry("links_private", links_private),
                    new ArrayEntry("posts_private", posts_private),
                    new ArrayEntry("cat_name", cat_name));
            cat_ID = (((org.numiton.nwp.wp_admin.includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_admin.includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_category(catarr, false);
        }
    }

    public void process_tags() {
        Array<Object> tag_names = new Array<Object>();
        String tag_name = null;
        String c = null;
        Object slug;
        Object description;
        Array<Object> tagarr = new Array<Object>();
        Object tag_ID = null;
        tag_names = new Array<Object>(getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("post_tag", "fields=names"));

        while (booleanval(c = strval(Array.array_shift(this.tags)))) {
            tag_name = Strings.trim(this.get_tag(c, "wp:tag_name"));

			// If the category exists we leave it alone
            if (Array.in_array(tag_name, tag_names)) {
                continue;
            }

            slug = this.get_tag(c, "wp:tag_slug");
            description = this.get_tag(c, "wp:tag_description");
            tagarr = Array.compact(new ArrayEntry("slug", slug), new ArrayEntry("description", description));
            tag_ID = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_term(tag_name, "post_tag", tagarr);
        }
    }

    public void process_author(String post) {
        String author = this.get_tag(post, "dc:creator");

        if (booleanval(author)) {
            this.allauthornames.putValue(author);
        }
    }

    public Object process_posts() {
        int i = 0;
        i = -1;
        echo(gVars.webEnv, "<ol>");
        this.get_entries(Callback.createCallbackArray(this, "process_post"));
        echo(gVars.webEnv, "</ol>");
        getIncluded(ImportPage.class, gVars, gConsts).wp_import_cleanup(this.id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "wordpress");
        echo(
                gVars.webEnv,
                "<h3>" +
                QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("All done.", "default") + " <a href=\"%s\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Have fun!", "default") + "</a>",
                        getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")) + "</h3>");

        return 0;
    }

    public Object process_post(String post) {
        int post_ID = 0;
        String post_title;
        String post_date;
        String post_date_gmt;
        String comment_status;
        String ping_status;
        String post_status;
        String post_name;
        String post_parent;
        String menu_order;
        String post_type;
        String post_password;
        String guid;
        String post_author;
        String post_content;
        Array tags = new Array();
        int tag_index = 0;
        String tag = null;
        Array categories = new Array();
        int cat_index = 0;
        String category = null;
        int post_exists = 0;
        int parent;
        Array<Object> postdata = new Array<Object>();
        String remote_url;
        Object comment_post_ID = null;
        Object post_id = null;
        Array<Object> post_cats = new Array<Object>();
        Object slug = null;
        StdClass cat = null;
        Object cat_ID = null;
        Array<Object> post_tags = new Array<Object>();
        StdClass tag_obj = null;
        int tag_id;
        Array comments = new Array();
        int num_comments = 0;
        String comment_author;
        String comment = null;
        String comment_author_email;
        String comment_author_IP;
        String comment_author_url;
        String comment_date;
        String comment_date_gmt;
        String comment_content;
        String comment_approved;
        String comment_type;
        String comment_parent;
        Array<Object> commentdata = new Array<Object>();
        Array postmeta = new Array();
        String key;
        String p = null;
        String value = null;
        
        post_ID = intval(this.get_tag(post, "wp:post_id"));

        if (booleanval(post_ID) && !empty(this.post_ids_processed.getValue(post_ID))) { // Processed already
            return 0;
        }

        Options.set_time_limit(gVars.webEnv, 60);
        
		// There are only ever one of these
        post_title = this.get_tag(post, "title");
        post_date = this.get_tag(post, "wp:post_date");
        post_date_gmt = this.get_tag(post, "wp:post_date_gmt");
        comment_status = this.get_tag(post, "wp:comment_status");
        ping_status = this.get_tag(post, "wp:ping_status");
        post_status = this.get_tag(post, "wp:status");
        post_name = this.get_tag(post, "wp:post_name");
        post_parent = this.get_tag(post, "wp:post_parent");
        menu_order = this.get_tag(post, "wp:menu_order");
        post_type = this.get_tag(post, "wp:post_type");
        post_password = this.get_tag(post, "wp:post_password");
        guid = this.get_tag(post, "guid");
        post_author = this.get_tag(post, "dc:creator");
        
        post_content = this.get_tag(post, "content:encoded");
        // Modified by Numiton
        post_content = RegExPerl.preg_replace_callback("|<(/?[A-Z]+)|", new Callback("htmlTagToLowercase", CallbackUtils.class), post_content);
        post_content = Strings.str_replace("<br>", "<br />", post_content);
        post_content = Strings.str_replace("<hr>", "<hr />", post_content);
        QRegExPerl.preg_match_all("|<category domain=\"tag\">(.*?)</category>|is", post, tags);
        tags = tags.getArrayValue(1);
        tag_index = 0;

        for (Map.Entry javaEntry85 : (Set<Map.Entry>) tags.entrySet()) {
            tag = strval(javaEntry85.getValue());
            tags.putValue(tag_index, gVars.wpdb.escape(this.unhtmlentities(Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("<![CDATA["), new ArrayEntry<Object>("]]>")), "", tag))));
            tag_index++;
        }

        QRegExPerl.preg_match_all("|<category>(.*?)</category>|is", post, categories);
        categories = categories.getArrayValue(1);
        cat_index = 0;

        for (Map.Entry javaEntry86 : (Set<Map.Entry>) categories.entrySet()) {
            category = strval(javaEntry86.getValue());
            categories.putValue(
                cat_index,
                gVars.wpdb.escape(this.unhtmlentities(Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("<![CDATA["), new ArrayEntry<Object>("]]>")), "", category))));
            cat_index++;
        }

        post_exists = getIncluded(PostPage.class, gVars, gConsts).post_exists(post_title, "", post_date);

        if (booleanval(post_exists)) {
            echo(gVars.webEnv, "<li>");
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Post <em>%s</em> already exists.", "default"), Strings.stripslashes(gVars.webEnv, post_title));
        } else {
        	
			// If it has parent, process parent first.
            int post_parentInt = intval(post_parent);

            if (booleanval(post_parentInt)) {
				// if we already know the parent, map it to the local ID
                if (booleanval(parent = intval(this.post_ids_processed.getValue(post_parentInt)))) {
                    post_parentInt = parent;  // new ID of the parent
                } else {
					// record the parent for later
                    this.orphans.putValue(post_ID, post_parentInt);
                }
            }

            echo(gVars.webEnv, "<li>");
            
            post_author = strval(this.checkauthor(post_author)); //just so that if a post already exists, new users are not created by checkauthor
            
            postdata = Array.compact(
                    new ArrayEntry("post_author", post_author),
                    new ArrayEntry("post_date", post_date),
                    new ArrayEntry("post_date_gmt", post_date_gmt),
                    new ArrayEntry("post_content", post_content),
                    new ArrayEntry("post_title", post_title),
                    new ArrayEntry("post_status", post_status),
                    new ArrayEntry("post_name", post_name),
                    new ArrayEntry("comment_status", comment_status),
                    new ArrayEntry("ping_status", ping_status),
                    new ArrayEntry("guid", guid),
                    new ArrayEntry("post_parent", post_parentInt),
                    new ArrayEntry("menu_order", menu_order),
                    new ArrayEntry("post_type", post_type),
                    new ArrayEntry("post_password", post_password));

            if (equal(post_type, "attachment")) {
                remote_url = this.get_tag(post, "wp:attachment_url");

                if (!booleanval(remote_url)) {
                    remote_url = guid;
                }

                comment_post_ID = post_id = this.process_attachment(postdata, remote_url);

                if (!booleanval(post_id) || getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_id)) {
                    return post_id;
                }
            } else {
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Importing post <em>%s</em>...", "default"), Strings.stripslashes(gVars.webEnv, post_title));
                comment_post_ID = post_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(postdata);
            }

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_id)) {
                return post_id;
            }

			// Memorize old and new ID.
            if (booleanval(post_id) && booleanval(post_ID)) {
                this.post_ids_processed.putValue(post_ID, intval(post_id));
            }

			// Add categories.
            if (Array.count(categories) > 0) {
                post_cats = new Array<Object>();

                for (Map.Entry javaEntry87 : (Set<Map.Entry>) categories.entrySet()) {
                    category = strval(javaEntry87.getValue());
                    slug = getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("slug", category, 0, "category", "db");
                    cat = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("slug", slug, "category", gConsts.getOBJECT(), "raw");
                    cat_ID = 0;

                    if (!empty(cat)) {
                        cat_ID = StdClass.getValue(cat, "term_id");
                    }

                    if (equal(cat_ID, 0)) {
                        category = gVars.wpdb.escape(category);
                        cat_ID = (((org.numiton.nwp.wp_admin.includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_admin.includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_category(
                                    new Array<Object>(new ArrayEntry<Object>("cat_name", category)),
                                    false);
                    }

                    post_cats.putValue(cat_ID);
                }

                (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_set_post_categories(intval(post_id), post_cats);
            }

			// Add tags.
            if (Array.count(tags) > 0) {
                post_tags = new Array<Object>();

                for (Map.Entry javaEntry88 : (Set<Map.Entry>) tags.entrySet()) {
                    tag = strval(javaEntry88.getValue());
                    slug = getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("slug", tag, 0, "post_tag", "db");
                    tag_obj = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("slug", slug, "post_tag", gConsts.getOBJECT(), "raw");
                    tag_id = 0;

                    if (!empty(tag_obj)) {
                        tag_id = intval(StdClass.getValue(tag_obj, "term_id"));
                    }

                    if (equal(tag_id, 0)) {
                        tag = gVars.wpdb.escape(tag);

                        Array tag_idArray = (Array) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_term(tag, "post_tag", new Array<Object>());
                        tag_id = intval(tag_idArray.getValue("term_id"));
                    }

                    post_tags.putValue(tag_id);
                }

                (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_set_post_tags(intval(post_id), strval(post_tags), false);
            }
        }

		// Now for comments
        QRegExPerl.preg_match_all("|<wp:comment>(.*?)</wp:comment>|is", post, comments);
        comments = comments.getArrayValue(1);
        num_comments = 0;

        if (booleanval(comments)) {
            for (Map.Entry javaEntry89 : (Set<Map.Entry>) comments.entrySet()) {
                comment = strval(javaEntry89.getValue());
                comment_author = this.get_tag(comment, "wp:comment_author");
                comment_author_email = this.get_tag(comment, "wp:comment_author_email");
                comment_author_IP = this.get_tag(comment, "wp:comment_author_IP");
                comment_author_url = this.get_tag(comment, "wp:comment_author_url");
                comment_date = this.get_tag(comment, "wp:comment_date");
                comment_date_gmt = this.get_tag(comment, "wp:comment_date_gmt");
                comment_content = this.get_tag(comment, "wp:comment_content");
                comment_approved = this.get_tag(comment, "wp:comment_approved");
                comment_type = this.get_tag(comment, "wp:comment_type");
                comment_parent = this.get_tag(comment, "wp:comment_parent");

    			// if this is a new post we can skip the comment_exists() check
                if (!booleanval(post_exists) || !booleanval(getIncluded(CommentPage.class, gVars, gConsts).comment_exists(comment_author, comment_date))) {
                    commentdata = Array.compact(
                            new ArrayEntry("comment_post_ID", comment_post_ID),
                            new ArrayEntry("comment_author", comment_author),
                            new ArrayEntry("comment_author_url", comment_author_url),
                            new ArrayEntry("comment_author_email", comment_author_email),
                            new ArrayEntry("comment_author_IP", comment_author_IP),
                            new ArrayEntry("comment_date", comment_date),
                            new ArrayEntry("comment_date_gmt", comment_date_gmt),
                            new ArrayEntry("comment_content", comment_content),
                            new ArrayEntry("comment_approved", comment_approved),
                            new ArrayEntry("comment_type", comment_type),
                            new ArrayEntry("comment_parent", comment_parent));
                    (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(commentdata);
                    num_comments++;
                }
            }
        }

        if (booleanval(num_comments)) {
            QStrings.printf(gVars.webEnv, " " + getIncluded(L10nPage.class, gVars, gConsts).__ngettext("(%s comment)", "(%s comments)", num_comments, "default"), num_comments);
        }

		// Now for post meta
        QRegExPerl.preg_match_all("|<wp:postmeta>(.*?)</wp:postmeta>|is", post, postmeta);
        postmeta = (Array) postmeta.getValue(1);

        if (booleanval(postmeta)) {
            for (Map.Entry javaEntry90 : (Set<Map.Entry>) postmeta.entrySet())/*
             * add_post_meta() will escape. add_post_meta() will escape.
             */
             {
                p = strval(javaEntry90.getValue());
                key = this.get_tag(p, "wp:meta_key");
                value = this.get_tag(p, "wp:meta_value");
                value = Strings.stripslashes(gVars.webEnv, value); // add_post_meta() will escape.
                
                this.process_post_meta(intval(post_id), key, value);
            }
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_post_added", post_id);
        print(gVars.webEnv, "</li>\n");

        return 0;
    }

    public void process_post_meta(int post_id, String key, String value) {
        String _key = null;
        
		// the filter can return false to skip a particular metadata key
        _key = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("import_post_meta_key", key));

        if (booleanval(_key)) {
            (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).add_post_meta(post_id, _key, value, false);
            getIncluded(PluginPage.class, gVars, gConsts).do_action("import_post_meta", post_id, _key, value);
        }
    }

    public Object process_attachment(Array<Object> postdata, String remote_url) {
        Object upload;
        Array<Object> info = new Array<Object>();
        int post_id = 0;
        String thumb_url = null;
        Array<Object> parts = new Array<Object>();
        String ext = null;
        String name = null;

        if (this.fetch_attachments && booleanval(remote_url)) {
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Importing attachment <em>%s</em>... ", "default"), Strings.htmlspecialchars(remote_url));
            upload = this.fetch_remote_file(postdata, remote_url);

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(upload)) {
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Remote file error: %s", "default"), Strings.htmlspecialchars(((WP_Error) upload).get_error_message()));

                return upload;
            } else {
                print(gVars.webEnv, "(" + getIncluded(FunctionsPage.class, gVars, gConsts).size_format(FileSystemOrSocket.filesize(gVars.webEnv, strval(((Array) upload).getValue("file"))), null) +
                    ")");
            }

            if (booleanval(info = getIncluded(FunctionsPage.class, gVars, gConsts).wp_check_filetype(strval(((Array) upload).getValue("file")), null))) {
                postdata.putValue("post_mime_type", info.getValue("type"));
            } else {
                print(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid file type", "default"));

                return null;
            }

            postdata.putValue("guid", ((Array) upload).getValue("url"));
            
			// as per wp-admin/includes/upload.php
            post_id = getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).wp_insert_attachment(postdata, strval(((Array) upload).getValue("file")), 0);
            getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).wp_update_attachment_metadata(
                post_id,
                getIncluded(ImagePage.class, gVars, gConsts).wp_generate_attachment_metadata(post_id, strval(((Array) upload).getValue("file"))));

			// remap the thumbnail url.  this isn't perfect because we're just guessing the original url.
            if (QRegExPerl.preg_match("@^image/@", strval(info.getValue("type"))) &&
                    booleanval(thumb_url = getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).wp_get_attachment_thumb_url(post_id))) {
                parts = FileSystemOrSocket.pathinfo(remote_url);
                ext = strval(parts.getValue("extension"));
                name = FileSystemOrSocket.basename(strval(parts.getValue("basename")), "." + ext);
                this.url_remap.putValue(parts.getValue("dirname") + "/" + name + ".thumbnail." + ext, thumb_url);
            }

            return post_id;
        } else {
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Skipping attachment <em>%s</em>", "default"), Strings.htmlspecialchars(remote_url));
        }

        return 0;
    }

    public Object fetch_remote_file(Array<Object> post, String url) {
        Array<Object> upload = new Array<Object>();
        String file_name = null;
        Array<String> headers;
        int max_size;
        
        upload = getIncluded(FunctionsPage.class, gVars, gConsts).wp_upload_dir(strval(post.getValue("post_date")));
        
		// extract the file name and extension from the url
        file_name = FileSystemOrSocket.basename(url);
        
		// get placeholder file in the upload dir with a unique sanitized filename
        upload = getIncluded(FunctionsPage.class, gVars, gConsts).wp_upload_bits(file_name, 0, "", strval(post.getValue("post_date")));

        if (booleanval(upload.getValue("error"))) {
            echo(gVars.webEnv, upload.getValue("error"));

            return new WP_Error(gVars, gConsts, "upload_dir_error", strval(upload.getValue("error")));
        }

		// fetch the remote url and write it to the placeholder file
        headers = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_http(url, strval(upload.getValue("file")), 1);

		// make sure the fetch was successful
        if (!equal(headers.getValue("response"), "200")) {
            JFileSystemOrSocket.unlink(gVars.webEnv, strval(upload.getValue("file")));

            return new WP_Error(gVars, gConsts, "import_file_error",
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Remote file returned error response %d", "default"), intval(headers.getValue("response"))));
        } else if (isset(headers.getValue("content-length")) && !equal(FileSystemOrSocket.filesize(gVars.webEnv, strval(upload.getValue("file"))), headers.getValue("content-length"))) {
            JFileSystemOrSocket.unlink(gVars.webEnv, strval(upload.getValue("file")));

            return new WP_Error(gVars, gConsts, "import_file_error", getIncluded(L10nPage.class, gVars, gConsts).__("Remote file is incorrect size", "default"));
        }

        max_size = this.max_attachment_size();

        if (!empty(max_size) && (FileSystemOrSocket.filesize(gVars.webEnv, strval(upload.getValue("file"))) > max_size)) {
            JFileSystemOrSocket.unlink(gVars.webEnv, strval(upload.getValue("file")));

            return new WP_Error(gVars, gConsts, "import_file_error",
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Remote file is too large, limit is %s", getIncluded(FunctionsPage.class, gVars, gConsts).size_format(max_size, null))));
        }

		// keep track of the old and new urls so we can substitute them later
        this.url_remap.putValue(url, upload.getValue("url"));
		// if the remote url is redirected somewhere else, keep track of the destination too
        if (!equal(headers.getValue("x-final-location"), url)) {
            this.url_remap.putValue(headers.getValue("x-final-location"), upload.getValue("url"));
        }

        return upload;
    }

    /**
     * sort by strlen, longest string first
     */
    public int cmpr_strlen(String a, String b) {
        return Strings.strlen(b) - Strings.strlen(a);
    }

    /**
     * update url references in post bodies to point to the new local files
     */
    public void backfill_attachment_urls() {
        Object from_url = null;
        Object to_url = null;
        int result = 0;
        
		// make sure we do the longest urls first, in case one is a substring of another
        Array.uksort(this.url_remap, new Callback("cmpr_strlen", this));

        for (Map.Entry javaEntry91 : this.url_remap.entrySet()) {
            from_url = javaEntry91.getKey();
            to_url = javaEntry91.getValue();
            
			// remap urls in post_content
            gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.posts + " SET post_content = REPLACE(post_content, \'%s\', \'%s\')", from_url, to_url));
            
			// remap enclosure urls
            result = gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.postmeta + " SET meta_value = REPLACE(meta_value, \'%s\', \'%s\') WHERE meta_key=\'enclosure\'", from_url, to_url));
        }
    }

    /**
     * update the post_parent of orphans now that we know the local id's of all
     * parents
     */
    public void backfill_parents() {
        Object local_child_id = null;
        Object child_id = null;
        Object local_parent_id = null;
        Object parent_id = null;

        for (Map.Entry javaEntry92 : this.orphans.entrySet()) {
            child_id = javaEntry92.getKey();
            parent_id = javaEntry92.getValue();
            local_child_id = this.post_ids_processed.getValue(child_id);
            local_parent_id = this.post_ids_processed.getValue(parent_id);

            if (booleanval(local_child_id) && booleanval(local_parent_id)) {
                gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.posts + " SET post_parent = %d WHERE ID = %d", local_parent_id, local_child_id));
            }
        }
    }

    public boolean is_valid_meta_key(Object key) {
		// skip _wp_attached_file metadata since we'll regenerate it from scratch
        if (equal(key, "_wp_attached_file")) {
            return false;
        }

        return booleanval(key);
    }

	// give the user the option of creating new users to represent authors in the import file?
    public Object allow_create_users() {
        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("import_allow_create_users", true);
    }

    /**
     * give the user the option of downloading and importing attached files
     */
    public Object allow_fetch_attachments() {
        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("import_allow_fetch_attachments", true);
    }

    public int max_attachment_size() {
		// can be overridden with a filter - 0 means no limit
        return intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("import_attachment_size_limit", 0));
    }

    public void import_start() {
        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_defer_term_counting(true);
        (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_defer_comment_counting(true);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_start", "");
    }

    public void import_end() {
        Object post_id = null;
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_end", "");

		// clear the caches after backfilling
        for (Map.Entry javaEntry93 : this.post_ids_processed.entrySet()) {
            post_id = javaEntry93.getValue();
            getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).clean_post_cache(post_id);
        }

        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_defer_term_counting(false);
        (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_defer_comment_counting(false);
    }

    public Object _import(Object id, Object fetch_attachments) {
        String file = null;
        this.id = intval(id);
        this.fetch_attachments = booleanval(this.allow_fetch_attachments()) && booleanval(fetch_attachments);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("import_post_meta_key", Callback.createCallbackArray(this, "is_valid_meta_key"), 10, 1);
        file = strval(getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).get_attached_file(this.id, false));
        this.import_file(file);

        return "";
    }

    public Object import_file(String file) {
        Object result = null;
        this.file = file;
        this.import_start();
        this.get_authors_from_post();
        this.get_entries();
        this.process_categories();
        this.process_tags();
        result = this.process_posts();
        this.backfill_parents();
        this.backfill_attachment_urls();
        this.import_end();

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            return result;
        }

        return 0;
    }

    public boolean handle_upload() {
        Array<Object> file = new Array<Object>();
        file = getIncluded(ImportPage.class, gVars, gConsts).wp_import_handle_upload();

        if (isset(file.getValue("error"))) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, there has been an error.", "default") + "</p>");
            echo(gVars.webEnv, "<p><strong>" + strval(file.getValue("error")) + "</strong></p>");

            return false;
        }

        this.file = strval(file.getValue("file"));
        this.id = intval(file.getValue("id"));

        return true;
    }

    public void dispatch() {
        int step = 0;
        Object result = null;

        if (empty(gVars.webEnv._GET.getValue("step"))) {
            step = 0;
        } else {
            step = intval(gVars.webEnv._GET.getValue("step"));
        }

        this.header();

        switch (step) {
        case 0: {
            this.greet();

            break;
        }

        case 1: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-upload", "_wpnonce");

            if (this.handle_upload()) {
                this.select_authors();
            }

            break;
        }

        case 2: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-wordpress", "_wpnonce");
            result = this._import(gVars.webEnv._GET.getValue("id"), gVars.webEnv._POST.getValue("attachments"));

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                echo(gVars.webEnv, ((WP_Error) result).get_error_message());
            }

            break;
        }
        }

        this.footer();
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
