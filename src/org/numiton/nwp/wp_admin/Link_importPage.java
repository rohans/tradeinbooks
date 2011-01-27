/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Link_importPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.BookmarkPage;
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Link_importPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Link_importPage.class.getName());
    public String opmltype;
    public String opml_url;
    public boolean blogrolling;
    public Array<Object> overrides;
    public int link_count;
    public Array<String> titles = new Array<String>();

    @Override
    @RequestMapping("/wp-admin/link-import.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/link_import";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_link_import_block1");
        gVars.webEnv = webEnv;
        
	     // Links
	     // Copyright (C) 2002 Mike Little -- mike@zed1.com
        
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.parent_file = "edit.php";
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Import Blogroll", "default");
        gVars.step = intval(gVars.webEnv._POST.getValue("step"));

        if (!booleanval(gVars.step)) {
            gVars.step = 0;
        }

        switch (gVars.step) {
        case 0: {
            includeOnce(gVars, gConsts, Admin_headerPage.class);

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
            }

            opmltype = "blogrolling"; // default.
            echo(gVars.webEnv, "\n<div class=\"wrap\">\n\n<h2>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Import your blogroll from another system", "default");
            echo(gVars.webEnv, " </h2>\n<form enctype=\"multipart/form-data\" action=\"link-import.php\" method=\"post\" name=\"blogroll\">\n");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-bookmarks", "_wpnonce", true, true);
            echo(gVars.webEnv, "\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("If a program or website you use allows you to export your links or subscriptions as OPML you may import them here.", "default");
            echo(
                    gVars.webEnv,
                    "</p>\n<div style=\"width: 70%; margin: auto; height: 8em;\">\n<input type=\"hidden\" name=\"step\" value=\"1\" />\n<input type=\"hidden\" name=\"MAX_FILE_SIZE\" value=\"30000\" />\n<div style=\"width: 48%;\" class=\"alignleft\">\n<h3>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Specify an OPML URL:", "default");
            echo(gVars.webEnv, "</h3>\n<input type=\"text\" name=\"opml_url\" size=\"50\" style=\"width: 90%;\" value=\"http://\" />\n</div>\n\n<div style=\"width: 48%;\" class=\"alignleft\">\n<h3>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Or choose from your local disk:", "default");
            echo(gVars.webEnv, "</h3>\n<input id=\"userfile\" name=\"userfile\" type=\"file\" size=\"30\" />\n</div>\n\n</div>\n\n<p style=\"clear: both; margin-top: 1em;\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Now select a category you want to put these links in.", "default");
            echo(gVars.webEnv, "<br />\n");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Category:", "default");
            echo(gVars.webEnv, " <select name=\"cat_id\">\n");
            gVars.categories = (Array<StdClass>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("link_category", "get=all");

            for (Map.Entry javaEntry269 : gVars.categories.entrySet()) {
                gVars.category = (StdClass) javaEntry269.getValue();
                echo(gVars.webEnv, "<option value=\"");
                echo(gVars.webEnv, StdClass.getValue(gVars.category, "term_id"));
                echo(gVars.webEnv, "\">");
                echo(gVars.webEnv,
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(getIncluded(PluginPage.class, gVars, gConsts)
                                                                                                 .apply_filters("link_category", StdClass.getValue(gVars.category, "name"))), strval(0)));
                echo(gVars.webEnv, "</option>\n");
            } // end foreach

            echo(gVars.webEnv, "</select></p>\n\n<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Import OPML File", "default");
            echo(gVars.webEnv, "\" /></p>\n</form>\n\n</div>\n");

            break;
        } // end case 0

        case 1: {
            {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-bookmarks", "_wpnonce");
                includeOnce(gVars, gConsts, Admin_headerPage.class);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                echo(gVars.webEnv, "<div class=\"wrap\">\n\n<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Importing...", "default");
                echo(gVars.webEnv, "</h2>\n");
                gVars.cat_id = strval(Math.abs(intval(gVars.webEnv._POST.getValue("cat_id"))));

                if (intval(gVars.cat_id) < 1) {
                    gVars.cat_id = strval(1);
                }

                opml_url = strval(gVars.webEnv._POST.getValue("opml_url"));

                if (isset(opml_url) && !equal(opml_url, "") && !equal(opml_url, "http://")) {
                    blogrolling = true;
                } else { // try to get the upload file.
                    overrides = new Array<Object>(new ArrayEntry<Object>("test_form", false), new ArrayEntry<Object>("test_type", false));

                    //Modified by Numiton
                    Array<Object> fileArray = getIncluded(FilePage.class, gVars, gConsts).wp_handle_upload(gVars.webEnv._FILES.getArrayValue("userfile"), overrides);

                    if (isset(fileArray.getValue("error"))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(fileArray.getValue("error"), "");
                    }

                    gVars.url = strval(fileArray.getValue("url"));
                    opml_url = strval(fileArray.getValue("file"));
                    blogrolling = false;
                }

                if (isset(opml_url) && !equal(opml_url, "")) {
                    if (strictEqual(blogrolling, true)) {
                        gVars.opml = getIncluded(FunctionsPage.class, gVars, gConsts).wp_remote_fopen(opml_url);
                    } else {
                        gVars.opml = FileSystemOrSocket.file_get_contents(gVars.webEnv, opml_url);
                    }

                    includeOnce(gVars, gConsts, Link_parse_opmlPage.class);
                    link_count = Array.count(gVars.names);

                    for (gVars.i = 0; gVars.i < link_count; gVars.i++) {
                        if (equal("Last", Strings.substr(titles.getValue(gVars.i), 0, 4))) {
                            titles.putValue(gVars.i, "");
                        }

                        if (equal("http", Strings.substr(titles.getValue(gVars.i), 0, 4))) {
                            titles.putValue(gVars.i, "");
                        }

                        Array<Object> link = new Array<Object>(new ArrayEntry<Object>("link_url", gVars.urls.getValue(gVars.i)),
                                new ArrayEntry<Object>("link_name", gVars.wpdb.escape(gVars.names.getValue(gVars.i))),
                                new ArrayEntry<Object>("link_category", new Array<Object>(new ArrayEntry<Object>(gVars.cat_id))),
                                new ArrayEntry<Object>("link_description", gVars.wpdb.escape(strval(gVars.descriptions.getValue(gVars.i)))), new ArrayEntry<Object>("link_owner", gVars.user_ID),
                                new ArrayEntry<Object>("link_rss", gVars.feeds.getValue(gVars.i)));
                        getIncluded(BookmarkPage.class, gVars, gConsts).wp_insert_link(link);
                        echo(gVars.webEnv, QStrings.sprintf("<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Inserted <strong>%s</strong>", "default") + "</p>", gVars.names.getValue(gVars.i)));
                    }

                    echo(gVars.webEnv, "\n<p>");
                    QStrings.printf(gVars.webEnv,
                        getIncluded(L10nPage.class, gVars, gConsts).__("Inserted %1$d links into category %2$s. All done! Go <a href=\"%3$s\">manage those links</a>.", "default"), link_count,
                        gVars.cat_id, "link-manager.php");
                    echo(gVars.webEnv, "</p>\n\n");
                } // end if got url 
                else {
                    echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("You need to supply your OPML url. Press back on your browser and try again", "default") + "</p>\n");
                } // end else

                if (!blogrolling) {
                    getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_delete_file", opml_url);
                }

                JFileSystemOrSocket.unlink(gVars.webEnv, opml_url);
                echo(gVars.webEnv, "</div>\n");

                break;
            }
        } // end case 1
        } // end switch

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
