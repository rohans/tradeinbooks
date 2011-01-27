/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MT_Import.java,v 1.3 2008/10/10 16:48:04 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.*;
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;

import com.numiton.ClassHandling;
import com.numiton.DateTime;
import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class MT_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(MT_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> posts = new Array<Object>();
    public String file;
    public int id;
    public Array<Object> mtnames = new Array<Object>();
    public Array<Object> newauthornames = new Array<Object>();
    public int j = -1;

    public MT_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Movable Type or TypePad", "default") + "</h2>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        this.header();
        echo(gVars.webEnv, "<div class=\"narrow\">\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Howdy! We&#8217;re about to begin importing all of your Movable Type or Typepad entries into nWordPress. To begin, either choose a file to upload and click \"Upload file and import,\" or use FTP to upload your MT export file as <code>mt-export.txt</code> in your <code>/wp-content/</code> directory and then click \"Import mt-export.txt\"",
                "default");
        echo(gVars.webEnv, "</p>\n\n");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_import_upload_form(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("step", 1));
        echo(gVars.webEnv, "<form method=\"post\" action=\"");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("step", 1));
        echo(gVars.webEnv, "\" class=\"import-upload-form\">\n\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-upload", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p>\n\t<input type=\"hidden\" name=\"upload_type\" value=\"ftp\" />\n");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Or use <code>mt-export.txt</code> in your <code>/wp-content/</code> directory", "default");
        echo(gVars.webEnv, "</p>\n<p class=\"submit\">\n<input type=\"submit\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import mt-export.txt", "default")));
        echo(gVars.webEnv, "\" />\n</p>\n</form>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "The importer is smart enough not to import duplicates, so you can run this multiple times without worry if&#8212;for whatever reason&#8212;it doesn\'t finish. If you get an <strong>out of memory</strong> error try splitting up the import file into pieces.",
                "default");
        echo(gVars.webEnv, " </p>\n</div>\n");
        this.footer();
    }

    public void users_form(int n) {
        Array<Object> users = new Array<Object>();
        StdClass user = null;
        users = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.users + " ORDER BY ID");
        echo(gVars.webEnv, "<select name=\"userselect[");
        echo(gVars.webEnv, n);
        echo(gVars.webEnv, "]\">\n\t<option value=\"#NONE#\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("- Select -", "default");
        echo(gVars.webEnv, "</option>\n\t");

        for (Map.Entry javaEntry64 : users.entrySet()) {
            user = (StdClass) javaEntry64.getValue();
            echo(gVars.webEnv, "<option value=\"" + StdClass.getValue(user, "user_login") + "\">" + StdClass.getValue(user, "user_login") + "</option>");
        }

        echo(gVars.webEnv, "\t</select>\n\t");
    }

    /**
     * function to check the authorname and do the mapping
     */
    public int checkauthor(String author) {
        String pass = null;
        int user_id = 0;
        Array<Object> newauthornames = new Array<Object>();
        Object key = null;
        
		//mtnames is an array with the names in the mt import file
        pass = getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12);

        if (!Array.in_array(author, this.mtnames)) { //a new mt author name is found
            ++this.j;
            this.mtnames.putValue(this.j, author); //add that new mt author name to an array
            user_id = getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(strval(this.newauthornames.getValue(this.j))); //check if the new author name defined by the user is a pre-existing wp user

            if (!booleanval(user_id)) { //banging my head against the desk now.
                if (equal(newauthornames.getValue(this.j), "left_blank")) { //check if the user does not want to change the authorname
                    user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_create_user(author, pass, "");
                    this.newauthornames.putValue(this.j, author); //now we have a name, in the place of left_blank.
                } else {
                    user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_create_user(strval(this.newauthornames.getValue(this.j)), pass, "");
                }
            } else {
                return user_id; // return pre-existing wp username if it exists
            }
        } else {
            key = Array.array_search(author, this.mtnames); //find the array key for $author in the $mtnames array
            user_id = getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(strval(this.newauthornames.getValue(key))); //use that key to get the value of the author's name from $newauthornames
        }

        return user_id;
    }

    public Array<Object> get_mt_authors() {
        Array<Object> temp = new Array<Object>();
        Array<Object> authors = new Array<Object>();
        int handle = 0;
        boolean in_comment = false;
        String line = null;
        int y = 0;
        Object next = null;
        int x = 0;
        temp = new Array<Object>();
        authors = new Array<Object>();
        handle = FileSystemOrSocket.fopen(gVars.webEnv, this.file, "r");

        if (equal(handle, null)) {
            return new Array<Object>();
        }

        in_comment = false;

        while (booleanval(line = FileSystemOrSocket.fgets(gVars.webEnv, handle, 0))) {
            line = Strings.trim(line);

            if (equal("COMMENT:", line)) {
                in_comment = true;
            } else if (equal("-----", line)) {
                in_comment = false;
            }

            if (in_comment || !strictEqual(0, Strings.strpos(line, "AUTHOR:"))) {
                continue;
            }

            temp.putValue(Strings.trim(Strings.substr(line, Strings.strlen("AUTHOR:"))));
        }

		//we need to find unique values of author names, while preserving the order, so this function emulates the unique_value(); php function, without the sorting.
        authors.putValue(0, Array.array_shift(temp));
        y = Array.count(temp) + 1;

        for (x = 1; x < y; x++) {
            next = Array.array_shift(temp);

            if (!Array.in_array(next, authors)) {
                Array.array_push(authors, next);
            }
        }

        FileSystemOrSocket.fclose(gVars.webEnv, handle);

        return authors;
    }

    public void get_authors_from_post() {
        Array<Object> formnames = new Array<Object>();
        Array<Object> selectnames = new Array<Object>();
        String newname = null;
        String line = null;
        String key = null;
        String selected = null;
        String user = null;
        int count = 0;
        int i = 0;
        formnames = new Array<Object>();
        selectnames = new Array<Object>();

        for (Map.Entry javaEntry65 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("user").entrySet()) {
            key = strval(javaEntry65.getKey());
            line = strval(javaEntry65.getValue());
            newname = Strings.trim(Strings.stripslashes(gVars.webEnv, line));

            if (equal(newname, "")) {
                newname = "left_blank"; //passing author names from step 1 to step 2 is accomplished by using POST. left_blank denotes an empty entry in the form.
            }

            Array.array_push(formnames, newname);
        } // $formnames is the array with the form entered names

        for (Map.Entry javaEntry66 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("userselect").entrySet()) {
            user = strval(javaEntry66.getKey());
            key = strval(javaEntry66.getValue());
            selected = Strings.trim(Strings.stripslashes(gVars.webEnv, key));
            Array.array_push(selectnames, selected);
        }

        count = Array.count(formnames);

        for (i = 0; i < count; i++) {
            if (!equal(selectnames.getValue(i), "#NONE#")) { //if no name was selected from the select menu, use the name entered in the form
                Array.array_push(this.newauthornames, selectnames.getValue(i));
            } else {
                Array.array_push(this.newauthornames, formnames.getValue(i));
            }
        }
    }

    public void mt_authors_form() {
        Array<Object> authors = new Array<Object>();
        int j = 0;
        Object author = null;
        echo(gVars.webEnv, "<div class=\"wrap\">\n<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Assign Authors", "default");
        echo(gVars.webEnv, "</h2>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "To make it easier for you to edit and save the imported posts and drafts, you may want to change the name of the author of the posts. For example, you may want to import all the entries as admin\'s entries.",
                "default");
        echo(gVars.webEnv, "</p>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Below, you can see the names of the authors of the MovableType posts in <em>italics</em>. For each of these names, you can either pick an author in your nWordPress installation from the menu, or enter a name for the author in the textbox.",
                "default");
        echo(gVars.webEnv, "</p>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("If a new user is created by nWordPress, a password will be randomly generated. Manually change the user\'s details if necessary.", "default");
        echo(gVars.webEnv, "</p>\n\t");
        authors = this.get_mt_authors();
        echo(gVars.webEnv, "<ol id=\"authors\">");
        echo(gVars.webEnv, "<form action=\"?import=mt&amp;step=2&amp;id=" + strval(this.id) + "\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-mt", "_wpnonce", true, true);
        j = -1;

        for (Map.Entry javaEntry67 : authors.entrySet()) {
            author = javaEntry67.getValue();
            ++j;
            echo(
                gVars.webEnv,
                "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("Current author:", "default") + " <strong>" + strval(author) + "</strong><br />" +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Create user %1$s or map to existing", "default"),
                    " <input type=\"text\" value=\"" + strval(author) + "\" name=\"" + "user[]" + "\" maxlength=\"30\"> <br />"));
            this.users_form(j);
            echo(gVars.webEnv, "</li>");
        }

        echo(gVars.webEnv, "<input type=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Submit", "default") + "\">" + "<br />");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</ol></div>");
    }

    public void select_authors() {
        Array<Object> file = new Array<Object>();

        if (strictEqual(gVars.webEnv._POST.getValue("upload_type"), "ftp")) {
            file.putValue("file", gConsts.getABSPATH() + "wp-content/mt-export.txt");

            if (!FileSystemOrSocket.file_exists(gVars.webEnv, strval(file.getValue("file")))) {
                file.putValue("error", getIncluded(L10nPage.class, gVars, gConsts).__("<code>mt-export.txt</code> does not exist", "default"));
            }
        } else {
            file = getIncluded(ImportPage.class, gVars, gConsts).wp_import_handle_upload();
        }

        if (isset(file.getValue("error"))) {
            this.header();
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, there has been an error", "default") + ".</p>");
            echo(gVars.webEnv, "<p><strong>" + strval(file.getValue("error")) + "</strong></p>");
            this.footer();

            return;
        }

        this.file = strval(file.getValue("file"));
        this.id = intval(file.getValue("id"));
        this.mt_authors_form();
    }

    public Object save_post(StdClass postObj, Array<Object> comments, Array<Object> pings) {
        Object post_id;
        int num_comments = 0;
        Array<Object> comment = new Array<Object>();
        int num_pings = 0;
        Array<Object> ping = new Array<Object>();
        
		// Reset the counter
        Options.set_time_limit(gVars.webEnv, 30);

        Array<Object> post = ClassHandling.get_object_vars(postObj);
        post = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(post);

        //		post = (Object) post;
        if (booleanval(post_id = getIncluded(PostPage.class, gVars, gConsts).post_exists(strval(post.getValue("post_title")), "", strval(post.getValue("post_date"))))) {
            echo(gVars.webEnv, "<li>");
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Post <em>%s</em> already exists.", "default"),
                Strings.stripslashes(gVars.webEnv, strval(post.getValue("post_title"))));
        } else {
            echo(gVars.webEnv, "<li>");
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Importing post <em>%s</em>...", "default"),
                Strings.stripslashes(gVars.webEnv, strval(post.getValue("post_title"))));

            if (!equal("", Strings.trim(strval(post.getValue("extended"))))) {
                post.putValue("post_content", post.getValue("post_content") + "\n<!--more-->\n" + post.getValue("extended"));
            }

            post.putValue("post_author", this.checkauthor(strval(post.getValue("post_author")))); //just so that if a post already exists, new users are not created by checkauthor
            post_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(post);

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_id)) {
                return post_id;
            }

			// Add categories.
            if (!equal(0, Array.count(post.getValue("categories")))) {
                getIncluded(TaxonomyPage.class, gVars, gConsts).wp_create_categories(post.getArrayValue("categories"), intval(post_id));
            }

			 // Add tags or keywords
            if (1 < Strings.strlen(strval(post.getValue("post_keywords")))) {
			 	// Keywords exist.
                QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__("<br />Adding tags <i>%s</i>...", "default"),
                    Strings.stripslashes(gVars.webEnv, strval(post.getValue("post_keywords"))));
                (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_add_post_tags(intval(post_id), post.getValue("post_keywords"));
            }
        }

        num_comments = 0;

        for (Map.Entry javaEntry68 : comments.entrySet()) {
            StdClass commentObj = (StdClass) javaEntry68.getValue();
            comment = ClassHandling.get_object_vars(commentObj);
            comment = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(comment);

            if (!booleanval(getIncluded(CommentPage.class, gVars, gConsts).comment_exists(strval(comment.getValue("comment_author")), strval(comment.getValue("comment_date"))))) {
                comment.putValue("comment_post_ID", post_id);
                comment = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_filter_comment(comment);
                (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(comment);
                num_comments++;
            }
        }

        if (booleanval(num_comments)) {
            QStrings.printf(gVars.webEnv, " " + getIncluded(L10nPage.class, gVars, gConsts).__ngettext("(%s comment)", "(%s comments)", num_comments, "default"), num_comments);
        }

        num_pings = 0;

        for (Map.Entry javaEntry69 : pings.entrySet()) {
            StdClass pingObj = (StdClass) javaEntry69.getValue();
            ping = ClassHandling.get_object_vars(pingObj);
            ping = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(ping);

            if (!booleanval(getIncluded(CommentPage.class, gVars, gConsts).comment_exists(strval(ping.getValue("comment_author")), strval(ping.getValue("comment_date"))))) {
                ping.putValue("comment_content", "<strong>" + strval(ping.getValue("title")) + "</strong>\n\n" + strval(ping.getValue("comment_content")));
                ping.putValue("comment_post_ID", post_id);
                ping = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_filter_comment(ping);
                (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(ping);
                num_pings++;
            }
        }

        if (booleanval(num_pings)) {
            QStrings.printf(gVars.webEnv, " " + getIncluded(L10nPage.class, gVars, gConsts).__ngettext("(%s ping)", "(%s pings)", num_pings, "default"), num_pings);
        }

        echo(gVars.webEnv, "</li>");
		//ob_flush();flush();

        return 0;
    }

    public Object process_posts() {
        int handle = 0;
        String context = null;
        StdClass post = null;
        StdClass comment = null;
        Array<Object> comments = new Array<Object>();
        StdClass ping = null;
        Array<Object> pings = new Array<Object>();
        String line = null;
        Object result;
        String author = null;
        String title = null;
        String status = null;
        String allow = null;
        String category = null;
        String date = null;
        String date_gmt = null;
        String email = null;
        String ip = null;
        String url = null;
        String blog = null;
        handle = FileSystemOrSocket.fopen(gVars.webEnv, this.file, "r");

        if (equal(handle, null)) {
            return null;
        }

        context = "";
        post = new StdClass();
        comment = new StdClass();
        comments = new Array<Object>();
        ping = new StdClass();
        pings = new Array<Object>();
        echo(gVars.webEnv, "<div class=\'wrap\'><ol>");

        while (booleanval(line = FileSystemOrSocket.fgets(gVars.webEnv, handle, 0))) {
            line = Strings.trim(line);

            if (equal("-----", line))  {
				// Finishing a multi-line field
                if (equal("comment", context)) {
                    comments.putValue(comment);
                    comment = new StdClass();
                } else if (equal("ping", context)) {
                    pings.putValue(ping);
                    ping = new StdClass();
                }

                context = "";
            } else if (equal("--------", line)) {
				// Finishing a post.
                context = "";
                result = this.save_post(post, comments, pings);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                    return result;
                }

                post = new StdClass();
                comment = new StdClass();
                ping = new StdClass();
                comments = new Array<Object>();
                pings = new Array<Object>();
            } else if (equal("BODY:", line)) {
                context = "body";
            } else if (equal("EXTENDED BODY:", line)) {
                context = "extended";
            } else if (equal("EXCERPT:", line)) {
                context = "excerpt";
            } else if (equal("KEYWORDS:", line)) {
                context = "keywords";
            } else if (equal("COMMENT:", line)) {
                context = "comment";
            } else if (equal("PING:", line)) {
                context = "ping";
            } else if (strictEqual(0, Strings.strpos(line, "AUTHOR:"))) {
                author = Strings.trim(Strings.substr(line, Strings.strlen("AUTHOR:")));

                if (equal("", context)) {
                    post.fields.putValue("post_author", author);
                } else if (equal("comment", context)) {
                    comment.fields.putValue("comment_author", author);
                }
            } else if (strictEqual(0, Strings.strpos(line, "TITLE:"))) {
                title = Strings.trim(Strings.substr(line, Strings.strlen("TITLE:")));

                if (equal("", context)) {
                    post.fields.putValue("post_title", title);
                } else if (equal("ping", context)) {
                    ping.fields.putValue("title", title);
                }
            } else if (strictEqual(0, Strings.strpos(line, "STATUS:"))) {
                status = Strings.trim(Strings.strtolower(Strings.substr(line, Strings.strlen("STATUS:"))));

                if (empty(status)) {
                    status = "publish";
                }

                post.fields.putValue("post_status", status);
            } else if (strictEqual(0, Strings.strpos(line, "ALLOW COMMENTS:"))) {
                allow = Strings.trim(Strings.substr(line, Strings.strlen("ALLOW COMMENTS:")));

                if (equal(allow, 1)) {
                    post.fields.putValue("comment_status", "open");
                } else {
                    post.fields.putValue("comment_status", "closed");
                }
            } else if (strictEqual(0, Strings.strpos(line, "ALLOW PINGS:"))) {
                allow = Strings.trim(Strings.substr(line, Strings.strlen("ALLOW PINGS:")));

                if (equal(allow, 1)) {
                    post.fields.putValue("ping_status", "open");
                } else {
                    post.fields.putValue("ping_status", "closed");
                }
            } else if (strictEqual(0, Strings.strpos(line, "CATEGORY:"))) {
                category = Strings.trim(Strings.substr(line, Strings.strlen("CATEGORY:")));

                if (!equal("", category)) {
                    post.fields.getArrayValue("categories").putValue(category);
                }
            } else if (strictEqual(0, Strings.strpos(line, "PRIMARY CATEGORY:"))) {
                category = Strings.trim(Strings.substr(line, Strings.strlen("PRIMARY CATEGORY:")));

                if (!equal("", category)) {
                    post.fields.getArrayValue("categories").putValue(category);
                }
            } else if (strictEqual(0, Strings.strpos(line, "DATE:"))) {
                date = Strings.trim(Strings.substr(line, Strings.strlen("DATE:")));

                int dateInt = QDateTime.strtotime(date);
                date = DateTime.date("Y-m-d H:i:s", dateInt);
                date_gmt = getIncluded(FormattingPage.class, gVars, gConsts).get_gmt_from_date(date);

                if (equal("", context)) {
                    post.fields.putValue("post_modified", date);
                    post.fields.putValue("post_modified_gmt", date_gmt);
                    post.fields.putValue("post_date", date);
                    post.fields.putValue("post_date_gmt", date_gmt);
                } else if (equal("comment", context)) {
                    comment.fields.putValue("comment_date", date);
                } else if (equal("ping", context)) {
                    ping.fields.putValue("comment_date", date);
                }
            } else if (strictEqual(0, Strings.strpos(line, "EMAIL:"))) {
                email = Strings.trim(Strings.substr(line, Strings.strlen("EMAIL:")));

                if (equal("comment", context)) {
                    comment.fields.putValue("comment_author_email", email);
                } else {
                    ping.fields.putValue("comment_author_email", "");
                }
            } else if (strictEqual(0, Strings.strpos(line, "IP:"))) {
                ip = Strings.trim(Strings.substr(line, Strings.strlen("IP:")));

                if (equal("comment", context)) {
                    comment.fields.putValue("comment_author_IP", ip);
                } else {
                    ping.fields.putValue("comment_author_IP", ip);
                }
            } else if (strictEqual(0, Strings.strpos(line, "URL:"))) {
                url = Strings.trim(Strings.substr(line, Strings.strlen("URL:")));

                if (equal("comment", context)) {
                    comment.fields.putValue("comment_author_url", url);
                } else {
                    ping.fields.putValue("comment_author_url", url);
                }
            } else if (strictEqual(0, Strings.strpos(line, "BLOG NAME:"))) {
                blog = Strings.trim(Strings.substr(line, Strings.strlen("BLOG NAME:")));
                ping.fields.putValue("comment_author", blog);
            } else {
				// Processing multi-line field, check context.
                line = line + "\n";

                if (equal("body", context)) {
                    post.fields.putValue("post_content", StdClass.getValue(post, "post_content") + line);
                } else if (equal("extended", context)) {
                    post.fields.putValue("extended", StdClass.getValue(post, "extended") + line);
                } else if (equal("excerpt", context)) {
                    post.fields.putValue("post_excerpt", StdClass.getValue(post, "post_excerpt") + line);
                } else if (equal("keywords", context)) {
                    post.fields.putValue("post_keywords", StdClass.getValue(post, "post_keywords") + line);
                } else if (equal("comment", context)) {
                    comment.fields.putValue("comment_content", StdClass.getValue(comment, "comment_content") + line);
                } else if (equal("ping", context)) {
                    ping.fields.putValue("comment_content", StdClass.getValue(ping, "comment_content") + line);
                }
            }
        }

        echo(gVars.webEnv, "</ol>");
        getIncluded(ImportPage.class, gVars, gConsts).wp_import_cleanup(this.id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "mt");
        echo(gVars.webEnv,
            "<h3>" +
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("All done. <a href=\"%s\">Have fun!</a>", "default"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")) +
            "</h3></div>");

        return 0;
    }

    public Object _import() {
        Object result;
        this.id = intval(gVars.webEnv._GET.getValue("id"));

        if (equal(this.id, 0)) {
            this.file = gConsts.getABSPATH() + "wp-content/mt-export.txt";
        } else {
            this.file = strval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_attached_file(this.id, false));
        }

        this.get_authors_from_post();
        result = this.process_posts();

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            return result;
        }

        return 0;
    }

    public void dispatch() {
        int step = 0;
        Object result;

        if (empty(gVars.webEnv._GET.getValue("step"))) {
            step = 0;
        } else {
            step = intval(gVars.webEnv._GET.getValue("step"));
        }

        switch (step) {
        case 0: {
            this.greet();

            break;
        }

        case 1: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-upload", "_wpnonce");
            this.select_authors();

            break;
        }

        case 2: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-mt", "_wpnonce");
            result = this._import();

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                echo(gVars.webEnv, ((WP_Error) result).get_error_message());
            }

            break;
        }
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
