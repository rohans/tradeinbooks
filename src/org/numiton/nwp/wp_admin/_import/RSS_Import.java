/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: RSS_Import.java,v 1.3 2008/10/10 16:48:04 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
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


public class RSS_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(RSS_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<?> posts = new Array();
    public String file;

    public RSS_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import RSS", "default") + "</h2>");
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
                        "Howdy! This importer allows you to extract posts from an RSS 2.0 file into your blog. This is useful if you want to import your posts from a system that is not handled by a custom import tool. Pick an RSS file to upload and click Import.",
                        "default") + "</p>");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_import_upload_form("admin.php?import=rss&amp;step=1");
        echo(gVars.webEnv, "</div>");
    }

    public void get_posts() {
        Array<String> datalines = new Array<String>();
        String importdata = null;
        int index = 0;
        String post = null;
        String post_title = null;
        Array<Object> post_titleArray = new Array<Object>();
        String post_date_gmt = null;
        Array<Object> post_date_gmtArray = new Array<Object>();
        String post_date = null;
        Array categories = new Array();
        int cat_index = 0;
        String category = null;
        String guid = null;
        Array<Object> guidArray = new Array<Object>();
        String post_content = null;
        Array<Object> post_contentArray = new Array<Object>();
        int post_author = 0;
        String post_status = null;
        
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        datalines = FileSystemOrSocket.file(gVars.webEnv, this.file); // Read the file into an array
        importdata = Strings.implode("", datalines); // squish it
        importdata = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("\r\n"), new ArrayEntry<Object>("\r")), "\n", importdata);
        QRegExPerl.preg_match_all("|<item>(.*?)</item>|is", importdata, (Array)this.posts);
        this.posts = Array.arrayCopy(this.posts.getArrayValue(1));
        index = 0;

        for (Map.Entry javaEntry70 : this.posts.entrySet()) {
            post = strval(javaEntry70.getValue());
            QRegExPerl.preg_match("|<title>(.*?)</title>|is", post, post_titleArray);
            post_title = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("<![CDATA["), new ArrayEntry<Object>("]]>")), "",
                    gVars.wpdb.escape(Strings.trim(strval(post_titleArray.getValue(1)))));
            QRegExPerl.preg_match("|<pubdate>(.*?)</pubdate>|is", post, post_date_gmtArray);

            if (booleanval(post_date_gmtArray)) {
                post_date_gmt = strval(QDateTime.strtotime(strval(post_date_gmtArray.getValue(1))));
            } else {
				// if we don't already have something from pubDate
                QRegExPerl.preg_match("|<dc:date>(.*?)</dc:date>|is", post, post_date_gmtArray);
                post_date_gmt = QRegExPerl.preg_replace("|([-+])([0-9]+):([0-9]+)$|", "\\1\\2\\3", strval(post_date_gmtArray.getValue(1)));
                post_date_gmt = Strings.str_replace("T", " ", post_date_gmt);
                post_date_gmt = strval(QDateTime.strtotime(post_date_gmt));
            }

            post_date_gmt = DateTime.gmdate("Y-m-d H:i:s", intval(post_date_gmt));
            post_date = getIncluded(FormattingPage.class, gVars, gConsts).get_date_from_gmt(post_date_gmt);
            QRegExPerl.preg_match_all("|<category>(.*?)</category>|is", post, categories);
            categories = Array.arrayCopy(categories.getArrayValue(1));

            if (!booleanval(categories)) {
                QRegExPerl.preg_match_all("|<dc:subject>(.*?)</dc:subject>|is", post, categories);
                categories = Array.arrayCopy(categories.getArrayValue(1));
            }

            cat_index = 0;

            for (Map.Entry javaEntry71 : (Set<Map.Entry>) categories.entrySet()) {
                category = strval(javaEntry71.getValue());
                categories.putValue(cat_index, gVars.wpdb.escape(this.unhtmlentities(category)));
                cat_index++;
            }

            QRegExPerl.preg_match("|<guid.*?>(.*?)</guid>|is", post, guidArray);

            if (booleanval(guidArray)) {
                guid = gVars.wpdb.escape(Strings.trim(strval(guidArray.getValue(1))));
            } else {
                guid = "";
            }

            QRegExPerl.preg_match("|<content:encoded>(.*?)</content:encoded>|is", post, post_contentArray);
            post_content = Strings.str_replace(
                    new Array<Object>(new ArrayEntry<Object>("<![CDATA["), new ArrayEntry<Object>("]]>")),
                    "",
                    gVars.wpdb.escape(Strings.trim(strval(post_contentArray.getValue(1)))));

            if (!booleanval(post_content)) {
            	// This is for feeds that put content in description
                QRegExPerl.preg_match("|<description>(.*?)</description>|is", post, post_contentArray);
                post_content = gVars.wpdb.escape(this.unhtmlentities(Strings.trim(strval(post_contentArray.getValue(1)))));
            }

         // Clean up content
            // Modified by Numiton
            post_content = RegExPerl.preg_replace_callback("|<(/?[A-Z]+)|", new Callback("htmlTagToLowercase", CallbackUtils.class), post_content);
            post_content = Strings.str_replace("<br>", "<br />", post_content);
            post_content = Strings.str_replace("<hr>", "<hr />", post_content);
            
            post_author = 1;
            post_status = "publish";
            this.posts.putValue(index,
                Array.compact(new ArrayEntry("post_author", post_author), new ArrayEntry("post_date", post_date), new ArrayEntry("post_date_gmt", post_date_gmt),
                    new ArrayEntry("post_content", post_content), new ArrayEntry("post_title", post_title), new ArrayEntry("post_status", post_status), new ArrayEntry("guid", guid),
                    new ArrayEntry("categories", categories)));
            index++;
        }
    }

    public Object import_posts() {
        Array<Object> post = null;
        Object post_id;
        Object post_title = null;
        Object post_content = null;
        Object post_date = null;
        Array<Object> categories = null;
        echo(gVars.webEnv, "<ol>");

        for (Map.Entry javaEntry72 : this.posts.entrySet()) {
            post = (Array<Object>) javaEntry72.getValue();
            echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing post...", "default"));
            post_title = Array.extractVar(post, "post_title", post_title, Array.EXTR_OVERWRITE);
            post_content = Array.extractVar(post, "post_content", post_content, Array.EXTR_OVERWRITE);
            post_date = Array.extractVar(post, "post_date", post_date, Array.EXTR_OVERWRITE);
            categories = (Array<Object>) Array.extractVar(post, "categories", categories, Array.EXTR_OVERWRITE);

            if (booleanval(post_id = getIncluded(PostPage.class, gVars, gConsts).post_exists(strval(post_title), strval(post_content), strval(post_date)))) {
                getIncluded(L10nPage.class, gVars, gConsts)._e("Post already imported", "default");
            } else {
                post_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(post);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_id)) {
                    return post_id;
                }

                if (!booleanval(post_id)) {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Couldn\'t get post ID", "default");

                    return 0;
                }

                if (!equal(0, Array.count(categories))) {
                    getIncluded(TaxonomyPage.class, gVars, gConsts).wp_create_categories(categories, intval(post_id));
                }

                getIncluded(L10nPage.class, gVars, gConsts)._e("Done !", "default");
            }

            echo(gVars.webEnv, "</li>");
        }

        echo(gVars.webEnv, "</ol>");

        return 0;
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
        this.get_posts();
        result = this.import_posts();

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            return result;
        }

        getIncluded(ImportPage.class, gVars, gConsts).wp_import_cleanup(intval(file.getValue("id")));
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "rss");
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
