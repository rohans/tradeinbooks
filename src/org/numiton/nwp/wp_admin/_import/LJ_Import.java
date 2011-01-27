/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: LJ_Import.java,v 1.2 2008/10/03 18:45:31 numiton Exp $
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
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.*;
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;

import com.numiton.DateTime;
import com.numiton.Options;
import com.numiton.RegExPerl;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class LJ_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(LJ_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String file;

    public LJ_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import LiveJournal", "default") + "</h2>");
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
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! Upload your LiveJournal XML export file and we&#8217;ll import the posts into this blog.", "default") +
            "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Choose a LiveJournal XML file to upload, then click Upload file and import.", "default") + "</p>");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_import_upload_form("admin.php?import=livejournal&amp;step=1");
        echo(gVars.webEnv, "</div>");
    }

    public Object import_posts() {
        String importdata = null;
        Array posts = new Array();
        String post = null;
        Array<Object> post_title = new Array<Object>();
        String post_date = null;
        Array<Object> post_dateArray = new Array<Object>();
        String post_content = null;
        Array<Object> post_contentArray = new Array<Object>();
        int post_author = 0;
        String post_status = null;
        Object post_id;
        Array<Object> postdata = new Array<Object>();
        Array comments = new Array();
        int comment_post_ID = 0;
        int num_comments = 0;
        String comment = null;
        String comment_content = null;
        Array<Object> comment_contentArray = new Array<Object>();
        String comment_date = null;
        Array<Object> comment_dateArray = new Array<Object>();
        Array<Object> comment_author = new Array<Object>();
        Array<Object> comment_author_email = new Array<Object>();
        int comment_approved = 0;
        Array<Object> commentdata = new Array<Object>();
        
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);

        Array<String> importdataArray = FileSystemOrSocket.file(gVars.webEnv, this.file); // Read the file into an array
        importdata = Strings.implode("", importdataArray); // squish it
        importdata = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("\r\n"), new ArrayEntry<Object>("\r")), "\n", importdata);
        QRegExPerl.preg_match_all("|<entry>(.*?)</entry>|is", importdata, posts);
        posts = posts.getArrayValue(1);
        importdata = null;
        echo(gVars.webEnv, "<ol>");

        for (Map.Entry javaEntry62 : (Set<Map.Entry>) posts.entrySet())/*
         * Clean up content Clean up content
         */
         {
            post = strval(javaEntry62.getValue());
            QRegExPerl.preg_match("|<subject>(.*?)</subject>|is", post, post_title);

            String post_titleStr = gVars.wpdb.escape(Strings.trim(strval(post_title.getValue(1))));

            if (empty(post_titleStr)) {
                QRegExPerl.preg_match("|<itemid>(.*?)</itemid>|is", post, post_title);
                post_titleStr = gVars.wpdb.escape(Strings.trim(strval(post_title.getValue(1))));
            }

            QRegExPerl.preg_match("|<eventtime>(.*?)</eventtime>|is", post, post_dateArray);
            post_date = strval(QDateTime.strtotime(strval(post_dateArray.getValue(1))));
            post_date = DateTime.date("Y-m-d H:i:s", intval(post_date));
            QRegExPerl.preg_match("|<event>(.*?)</event>|is", post, post_contentArray);
            post_content = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("<![CDATA["), new ArrayEntry<Object>("]]>")), "", Strings.trim(strval(post_contentArray.getValue(1))));
            post_content = this.unhtmlentities(post_content);

			// Clean up content
            // Modified by Numiton
            post_content = RegExPerl.preg_replace_callback("|<(/?[A-Z]+)|", new Callback("htmlTagToLowercase", CallbackUtils.class), post_content);
            post_content = Strings.str_replace("<br>", "<br />", post_content);
            post_content = Strings.str_replace("<hr>", "<hr />", post_content);
            post_content = gVars.wpdb.escape(post_content);

            post_author = gVars.current_user.getID();
            post_status = "publish";
            echo(gVars.webEnv, "<li>");

            if (booleanval(post_id = getIncluded(PostPage.class, gVars, gConsts).post_exists(post_titleStr, post_content, post_date))) {
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Post <em>%s</em> already exists.", "default"), Strings.stripslashes(gVars.webEnv, post_titleStr));
            } else {
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Importing post <em>%s</em>...", "default"), Strings.stripslashes(gVars.webEnv, post_titleStr));
                postdata = Array.compact(new ArrayEntry("post_author", post_author), new ArrayEntry("post_date", post_date), new ArrayEntry("post_content", post_content),
                        new ArrayEntry("post_title", post_titleStr), new ArrayEntry("post_status", post_status));
                post_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(postdata);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_id)) {
                    return post_id;
                }

                if (!booleanval(post_id)) {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Couldn\'t get post ID", "default");
                    echo(gVars.webEnv, "</li>");

                    break;
                }
            }

            QRegExPerl.preg_match_all("|<comment>(.*?)</comment>|is", post, comments);
            comments = comments.getArrayValue(1);

            if (booleanval(comments)) {
                comment_post_ID = intval(post_id);
                num_comments = 0;

                for (Map.Entry javaEntry63 : (Set<Map.Entry>) comments.entrySet()) {
                    comment = strval(javaEntry63.getValue());
                    QRegExPerl.preg_match("|<event>(.*?)</event>|is", comment, comment_contentArray);
                    comment_content = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("<![CDATA["), new ArrayEntry<Object>("]]>")), "",
                            Strings.trim(strval(comment_contentArray.getValue(1))));
                    comment_content = this.unhtmlentities(comment_content);

					// Clean up content
                    // Modified by Numiton
                    comment_content = RegExPerl.preg_replace_callback("|<(/?[A-Z]+)|", new Callback("htmlTagToLowercase", CallbackUtils.class), comment_content);
                    comment_content = Strings.str_replace("<br>", "<br />", comment_content);
                    comment_content = Strings.str_replace("<hr>", "<hr />", comment_content);
                    comment_content = gVars.wpdb.escape(comment_content);
                    
                    QRegExPerl.preg_match("|<eventtime>(.*?)</eventtime>|is", comment, comment_dateArray);
                    comment_date = Strings.trim(strval(comment_dateArray.getValue(1)));
                    comment_date = DateTime.date("Y-m-d H:i:s", QDateTime.strtotime(comment_date));
                    
                    QRegExPerl.preg_match("|<name>(.*?)</name>|is", comment, comment_author);
                    String comment_authorStr = gVars.wpdb.escape(Strings.trim(strval(comment_author.getValue(1))));
                    
                    QRegExPerl.preg_match("|<email>(.*?)</email>|is", comment, comment_author_email);
                    String comment_author_emailStr = gVars.wpdb.escape(Strings.trim(strval(comment_author_email.getValue(1))));
                    
                    comment_approved = 1;
					// Check if it's already there
                    if (!booleanval(getIncluded(CommentPage.class, gVars, gConsts).comment_exists(comment_authorStr, comment_date))) {
                        commentdata = Array.compact(new ArrayEntry("comment_post_ID", comment_post_ID), new ArrayEntry("comment_author", comment_authorStr),
                                new ArrayEntry("comment_author_email", comment_author_emailStr), new ArrayEntry("comment_date", comment_date), new ArrayEntry("comment_content", comment_content),
                                new ArrayEntry("comment_approved", comment_approved));
                        commentdata = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_filter_comment(commentdata);
                        (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(commentdata);
                        num_comments++;
                    }
                }
            }

            if (booleanval(num_comments)) {
                echo(gVars.webEnv, " ");
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("(%s comment)", "(%s comments)", num_comments, "default"), num_comments);
            }

            echo(gVars.webEnv, "</li>");
        }

        echo(gVars.webEnv, "</ol>");

        return "";
    }

    public Object _import() {
        Array<Object> file = new Array<Object>();
        Object result;
        file = getIncluded(ImportPage.class, gVars, gConsts).wp_import_handle_upload();

        if (isset(file.getValue("error"))) {
            echo(gVars.webEnv, file.getValue("error"));

            return 0;
        }

        this.file = strval(file.getValue("file"));
        result = this.import_posts();

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            return result;
        }

        getIncluded(ImportPage.class, gVars, gConsts).wp_import_cleanup(intval(file.getValue("id")));
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "livejournal");
        echo(gVars.webEnv, "<h3>");
        QStrings.printf(
            gVars.webEnv,
            getIncluded(L10nPage.class, gVars, gConsts).__("All done. <a href=\"%s\">Have fun!</a>", "default"),
            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
        echo(gVars.webEnv, "</h3>");

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

        this.header();

        switch (step) {
        case 0: {
            this.greet();

            break;
        }

        case 1: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-upload", "_wpnonce");
            result = this._import();

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
