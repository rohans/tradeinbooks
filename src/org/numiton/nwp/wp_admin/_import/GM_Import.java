/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: GM_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_includes.*;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class GM_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(GM_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> gmnames = new Array<Object>();

    public GM_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
     // Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import GreyMatter", "default") + "</h2>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        this.header();
        echo(gVars.webEnv, "<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("This is a basic GreyMatter to nWordPress import script.", "default");
        echo(gVars.webEnv, "</p>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("What it does:", "default");
        echo(gVars.webEnv, "</p>\n<ul>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Parses gm-authors.cgi to import (new) authors. Everyone is imported at level 1.", "default");
        echo(gVars.webEnv, "</li>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Parses the entries cgi files to import posts, comments, and karma on posts (although karma is not used on nWordPress yet).<br />If authors are found not to be in gm-authors.cgi, imports them at level 0.",
                "default");
        echo(gVars.webEnv, "</li>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Detects duplicate entries or comments. If you don\'t import everything the first time, or this import should fail in the middle, duplicate entries will not be made when you try again.",
                "default");
        echo(gVars.webEnv, "</li>\n</ul>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("What it does not:", "default");
        echo(gVars.webEnv, "</p>\n<ul>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
            "Parse gm-counter.cgi, gm-banlist.cgi, gm-cplog.cgi (you can make a CP log hack if you really feel like it, but I question the need of a CP log).",
            "default");
        echo(gVars.webEnv, "</li>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Import gm-templates.", "default");
        echo(gVars.webEnv, "</li>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Doesn\'t keep entries on top.", "default");
        echo(
            gVars.webEnv,
            "</li>\n</ul>\n<p>&nbsp;</p>\n\n<form name=\"stepOne\" method=\"get\">\n<input type=\"hidden\" name=\"import\" value=\"greymatter\" />\n<input type=\"hidden\" name=\"step\" value=\"1\" />\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-greymatter", "_wpnonce", true, true);
        echo(gVars.webEnv, "<h3>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Second step: GreyMatter details:", "default");
        echo(gVars.webEnv, "</h3>\n<table class=\"form-table\">\n<tr>\n<td>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Path to GM files:", "default");
        echo(gVars.webEnv, "</td>\n<td><input type=\"text\" style=\"width:300px\" name=\"gmpath\" value=\"/home/my/site/cgi-bin/greymatter/\" /></td>\n</tr>\n<tr>\n<td>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Path to GM entries:", "default");
        echo(gVars.webEnv, "</td>\n<td><input type=\"text\" style=\"width:300px\" name=\"archivespath\" value=\"/home/my/site/cgi-bin/greymatter/archives/\" /></td>\n</tr>\n<tr>\n<td>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Last entry\'s number:", "default");
        echo(gVars.webEnv, "</td>\n<td><input type=\"text\" name=\"lastentry\" value=\"00000001\" /><br />\n\t");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "This importer will search for files 00000001.cgi to 000-whatever.cgi,<br />so you need to enter the number of the last GM post here.<br />(if you don\'t know that number, just log into your FTP and look it out<br />in the entries\' folder)",
                "default");
        echo(gVars.webEnv, "</td>\n</tr>\n</table>\n</p>\n<p><input type=\"submit\" name=\"submit\" value=\"");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Start Importing", "default");
        echo(gVars.webEnv, "\" class=\"button\" /></p>\n</form>\n");
        this.footer();
    }

    public String gm2autobr(String string) { // transforms GM's |*| into b2's <br />\n
        string = Strings.str_replace("|*|", "<br />\n", string);

        return string;
    }

    public Object _import() {
        Array<Object> wpvarstoreset = new Array<Object>();
        Object wpvar = null;
        int i = 0;
        Ref<String> archivespath = new Ref<String>();
        Ref<String> gmpath = new Ref<String>();
        Ref<Integer> lastentry = new Ref<Integer>();
        Array<String> userbase = new Array<String>();
        Array<String> userdata = new Array<String>();

        /* Added by Numiton */
        Object post_date_gmt = null;
        Object post_excerpt = null;
        Object post_modified = null;
        Object post_modified_gmt = null;
        String user = null;
        String user_ip = null;
        String user_domain = null;
        String user_browser = null;
        String s = null;
        String user_joindate = null;
        String user_login = null;
        String pass1 = null;
        String user_nickname = null;
        String user_email = null;
        String user_url = null;
        int user_id = 0;
        Array<Object> user_info = new Array<Object>();
        String entryfile = null;
        Array<String> entry = new Array<String>();
        Array<String> postinfo = new Array<String>();
        String postmaincontent;
        String postmorecontent;
        String post_author = null;
        String post_title;
        Object postyear = null;
        String postmonth = null;
        String postday = null;
        int posthour = 0;
        String postminute = null;
        String postsecond = null;
        String post_date = null;
        String post_content = null;
        Object post_karma = null;
        String post_status = null;
        String comment_status = null;
        String ping_status = null;
        Object post_ID = 0;
        Array<Object> postdata = new Array<Object>();
        int c = 0;
        int numAddedComments = 0;
        int numComments = 0;
        int j = 0;
        Array<String> commentinfo = new Array<String>();
        int comment_post_ID = 0;
        String comment_author = null;
        String comment_author_email = null;
        String comment_author_url = null;
        String comment_author_IP = null;
        Object commentyear = null;
        String commentmonth = null;
        String commentday = null;
        int commenthour = 0;
        String commentminute = null;
        String commentsecond = null;
        String comment_date = null;
        String comment_content = null;
        Array<Object> commentdata = new Array<Object>();
        int preExisting = 0;
        wpvarstoreset = new Array<Object>(new ArrayEntry<Object>("gmpath"), new ArrayEntry<Object>("archivespath"), new ArrayEntry<Object>("lastentry"));

        Array<Ref<?>> wpvarstoresetArray = new Array(new ArrayEntry("gmpath", gmpath), new ArrayEntry("archivespath", archivespath), new ArrayEntry("lastentry", lastentry));

        for (i = 0; i < Array.count(wpvarstoreset); i = i + 1) {
            wpvar = wpvarstoreset.getValue(i);

            /* Modified by Numiton */
            Ref varObj = wpvarstoresetArray.getRef(wpvar);

            if (!empty(varObj.value)) {
                if (empty(gVars.webEnv._POST.getValue(wpvar))) {
                    if (empty(gVars.webEnv._GET.getValue(wpvar))) {
                        varObj.value = "";
                    } else {
                        varObj.value = gVars.webEnv._GET.getValue(wpvar);
                    }
                } else {
                    varObj.value = gVars.webEnv._POST.getValue(wpvar);
                }
            }
        }

        if (!Directories.chdir(gVars.webEnv, archivespath.value)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Wrong path, the path to the GM entries does not exist on the server", "default"), "");
        }

        if (!Directories.chdir(gVars.webEnv, gmpath.value)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Wrong path, the path to the GM files does not exist on the server", "default"), "");
        }

        //		lastentry.value = intval(lastentry.value);
        this.header();
        echo(gVars.webEnv, "<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("The importer is running...", "default");
        echo(gVars.webEnv, "</p>\n<ul>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("importing users...", "default");
        echo(gVars.webEnv, "<ul>");
        Directories.chdir(gVars.webEnv, gmpath.value);
        userbase = FileSystemOrSocket.file(gVars.webEnv, "gm-authors.cgi");

        for (Map.Entry javaEntry57 : userbase.entrySet()) {
            user = strval(javaEntry57.getValue());
            userdata = Strings.explode("|", user);
            user_ip = "127.0.0.1";
            user_domain = "localhost";
            user_browser = "server";
            s = userdata.getValue(4);
            user_joindate = Strings.substr(s, 6, 4) + "-" + Strings.substr(s, 0, 2) + "-" + Strings.substr(s, 3, 2) + " 00:00:00";
            user_login = gVars.wpdb.escape(userdata.getValue(0));
            pass1 = gVars.wpdb.escape(userdata.getValue(1));
            user_nickname = gVars.wpdb.escape(userdata.getValue(0));
            user_email = gVars.wpdb.escape(userdata.getValue(2));
            user_url = gVars.wpdb.escape(userdata.getValue(3));
            user_joindate = gVars.wpdb.escape(user_joindate);
            user_id = getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(user_login);

            if (booleanval(user_id)) {
                QStrings.printf(
                        gVars.webEnv,
                        "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("user %s", "default") + "<strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Already exists", "default") +
                        "</strong></li>",
                        "<em>" + user_login + "</em>");
                this.gmnames.putValue(userdata.getValue(0), user_id);

                continue;
            }

            user_info = new Array<Object>(
                    new ArrayEntry<Object>("user_login", user_login),
                    new ArrayEntry<Object>("user_pass", pass1),
                    new ArrayEntry<Object>("user_nickname", user_nickname),
                    new ArrayEntry<Object>("user_email", user_email),
                    new ArrayEntry<Object>("user_url", user_url),
                    new ArrayEntry<Object>("user_ip", user_ip),
                    new ArrayEntry<Object>("user_domain", user_domain),
                    new ArrayEntry<Object>("user_browser", user_browser),
                    new ArrayEntry<Object>("dateYMDhour", user_joindate),
                    new ArrayEntry<Object>("user_level", "1"),
                    new ArrayEntry<Object>("user_idmode", "nickname"));
            user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(user_info);
            this.gmnames.putValue(userdata.getValue(0), user_id);
            QStrings.printf(
                gVars.webEnv,
                "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("user %s...", "default") + " <strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Done", "default") + "</strong></li>",
                "<em>" + user_login + "</em>");
        }

        echo(gVars.webEnv, "</ul><strong>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Done", "default");
        echo(gVars.webEnv, "</strong></li>\n<li>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("importing posts, comments, and karma...", "default");
        echo(gVars.webEnv, "<br /><ul>");
        Directories.chdir(gVars.webEnv, archivespath.value);

        for (i = 0; i <= lastentry.value; i = i + 1) {
            entryfile = "";

            if (i < 10000000) {
                entryfile = entryfile + "0";

                if (i < 1000000) {
                    entryfile = entryfile + "0";

                    if (i < 100000) {
                        entryfile = entryfile + "0";

                        if (i < 10000) {
                            entryfile = entryfile + "0";

                            if (i < 1000) {
                                entryfile = entryfile + "0";

                                if (i < 100) {
                                    entryfile = entryfile + "0";

                                    if (i < 10) {
                                        entryfile = entryfile + "0";
                                    }
                                }
                            }
                        }
                    }
                }
            }

            entryfile = entryfile + strval(i);

            if (FileSystemOrSocket.is_file(gVars.webEnv, entryfile + ".cgi"))/*
             * in greymatter, there are no drafts in greymatter, there are no
             * drafts
             */
             {
                entry = FileSystemOrSocket.file(gVars.webEnv, entryfile + ".cgi");
                postinfo = Strings.explode("|", entry.getValue(0));
                postmaincontent = this.gm2autobr(entry.getValue(2));
                postmorecontent = this.gm2autobr(entry.getValue(3));
                post_author = Strings.trim(gVars.wpdb.escape(postinfo.getValue(1)));
                post_title = this.gm2autobr(postinfo.getValue(2));
                QStrings.printf(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("entry # %s : %s : by %s", "default"), entryfile, post_title, postinfo.getValue(1));
                post_title = gVars.wpdb.escape(post_title);
                postyear = postinfo.getValue(6);
                postmonth = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(postinfo.getValue(4), 2);
                postday = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(postinfo.getValue(5), 2);
                posthour = intval(getIncluded(FormattingPage.class, gVars, gConsts).zeroise(postinfo.getValue(7), 2));
                postminute = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(postinfo.getValue(8), 2);
                postsecond = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(postinfo.getValue(9), 2);

                if (equal(postinfo.getValue(10), "PM") && !equal(posthour, "12")) {
                    posthour = posthour + 12;
                }

                post_date = strval(postyear) + "-" + postmonth + "-" + postday + " " + strval(posthour) + ":" + postminute + ":" + postsecond;
                post_content = postmaincontent;

                if (Strings.strlen(postmorecontent) > 3) {
                    post_content = post_content + "<!--more--><br /><br />" + postmorecontent;
                }

                post_content = gVars.wpdb.escape(post_content);
                
                post_karma = postinfo.getValue(12);
                
                post_status = "publish"; //in greymatter, there are no drafts
                comment_status = "open";
                ping_status = "closed";

                if (booleanval(post_ID = getIncluded(PostPage.class, gVars, gConsts).post_exists(post_title, "", post_date))) {
                    echo(gVars.webEnv, " ");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("(already exists)", "default");
                } else {
    				//just so that if a post already exists, new users are not created by checkauthor
    				// we'll check the author is registered, or if it's a deleted author
                    user_id = getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(post_author);

                    if (!booleanval(user_id)) {	// if deleted from GM, we register the author as a level 0 user
                        user_ip = "127.0.0.1";
                        user_domain = "localhost";
                        user_browser = "server";
                        user_joindate = "1979-06-06 00:41:00";
                        user_login = gVars.wpdb.escape(post_author);
                        pass1 = gVars.wpdb.escape("password");
                        user_nickname = gVars.wpdb.escape(post_author);
                        user_email = gVars.wpdb.escape("user@deleted.com");
                        user_url = gVars.wpdb.escape("");
                        user_joindate = gVars.wpdb.escape(user_joindate);
                        user_info = new Array<Object>(
                                new ArrayEntry<Object>("user_login", user_login),
                                new ArrayEntry<Object>("user_pass", pass1),
                                new ArrayEntry<Object>("user_nickname", user_nickname),
                                new ArrayEntry<Object>("user_email", user_email),
                                new ArrayEntry<Object>("user_url", user_url),
                                new ArrayEntry<Object>("user_ip", user_ip),
                                new ArrayEntry<Object>("user_domain", user_domain),
                                new ArrayEntry<Object>("user_browser", user_browser),
                                new ArrayEntry<Object>("dateYMDhour", user_joindate),
                                new ArrayEntry<Object>("user_level", 0),
                                new ArrayEntry<Object>("user_idmode", "nickname"));
                        user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(user_info);
                        this.gmnames.putValue(postinfo.getValue(1), user_id);
                        echo(gVars.webEnv, ": ");
                        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("registered deleted user %s at level 0 ", "default"), "<em>" + user_login + "</em>");
                    }

                    if (Array.array_key_exists(postinfo.getValue(1), this.gmnames)) {
                        post_author = strval(this.gmnames.getValue(postinfo.getValue(1)));
                    } else {
                        post_author = strval(user_id);
                    }

                    postdata = Array.compact(
                            new ArrayEntry("post_author", post_author),
                            new ArrayEntry("post_date", post_date),
                            new ArrayEntry("post_date_gmt", post_date_gmt),
                            new ArrayEntry("post_content", post_content),
                            new ArrayEntry("post_title", post_title),
                            new ArrayEntry("post_excerpt", post_excerpt),
                            new ArrayEntry("post_status", post_status),
                            new ArrayEntry("comment_status", comment_status),
                            new ArrayEntry("ping_status", ping_status),
                            new ArrayEntry("post_modified", post_modified),
                            new ArrayEntry("post_modified_gmt", post_modified_gmt));
                    post_ID = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(postdata);

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_ID)) {
                        return post_ID;
                    }
                }

                c = Array.count(entry);

                if (c > 4) {
                    numAddedComments = 0;
                    numComments = 0;

                    for (j = 4; j < c; j++) {
                        entry.putValue(j, this.gm2autobr(entry.getValue(j)));
                        commentinfo = Strings.explode("|", entry.getValue(j));
                        comment_post_ID = intval(post_ID);
                        comment_author = gVars.wpdb.escape(commentinfo.getValue(0));
                        comment_author_email = gVars.wpdb.escape(commentinfo.getValue(2));
                        comment_author_url = gVars.wpdb.escape(commentinfo.getValue(3));
                        comment_author_IP = gVars.wpdb.escape(commentinfo.getValue(1));
                        commentyear = commentinfo.getValue(7);
                        commentmonth = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(commentinfo.getValue(5), 2);
                        commentday = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(commentinfo.getValue(6), 2);
                        commenthour = intval(getIncluded(FormattingPage.class, gVars, gConsts).zeroise(commentinfo.getValue(8), 2));
                        commentminute = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(commentinfo.getValue(9), 2);
                        commentsecond = getIncluded(FormattingPage.class, gVars, gConsts).zeroise(commentinfo.getValue(10), 2);

                        Object comment_approved = null;

                        // Added by Numiton
                        if (equal(commentinfo.getValue(11), "PM") && !equal(commenthour, "12")) {
                            commenthour = commenthour + 12;
                        }

                        comment_date = strval(commentyear) + "-" + commentmonth + "-" + commentday + " " + strval(commenthour) + ":" + commentminute + ":" + commentsecond;
                        comment_content = gVars.wpdb.escape(commentinfo.getValue(12));

                        if (!booleanval(getIncluded(CommentPage.class, gVars, gConsts).comment_exists(comment_author, comment_date))) {
                            commentdata = Array.compact(new ArrayEntry("comment_post_ID", comment_post_ID), new ArrayEntry("comment_author", comment_author),
                                    new ArrayEntry("comment_author_url", comment_author_url), new ArrayEntry("comment_author_email", comment_author_email),
                                    new ArrayEntry("comment_author_IP", comment_author_IP), new ArrayEntry("comment_date", comment_date), new ArrayEntry("comment_content", comment_content),
                                    new ArrayEntry("comment_approved", comment_approved));
                            commentdata = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_filter_comment(commentdata);
                            (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(commentdata);
                            numAddedComments++;
                        }

                        numComments++;
                    }

                    if (numAddedComments > 0) {
                        echo(gVars.webEnv, ": ");
                        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("imported %s comment", "imported %s comments", numAddedComments, "default"),
                            numAddedComments);
                    }

                    preExisting = numComments - intval("numAddedComments");

                    if (preExisting > 0) {
                        echo(gVars.webEnv, " ");
                        QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts).__ngettext("ignored %s pre-existing comment", "ignored %s pre-existing comments", preExisting, "default"),
                            preExisting);
                    }
                }

                echo(gVars.webEnv, "... <strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Done", "default") + "</strong></li>");
            }
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "greymatter");
        echo(gVars.webEnv, "</ul><strong>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Done", "default");
        echo(gVars.webEnv, "</strong></li></ul>\n<p>&nbsp;</p>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Completed GreyMatter import!", "default");
        echo(gVars.webEnv, "</p>\n");
        this.footer();

        return 0;
    }

    public void dispatch() {
        int step = 0;
        Object result = 0;

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
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-greymatter", "_wpnonce");
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
