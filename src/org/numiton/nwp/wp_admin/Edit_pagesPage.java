/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_pagesPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.CommentPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_pagesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_pagesPage.class.getName());
    public Object post_status_q;
    public Object query_str;
    public Object all;

    @Override
    @RequestMapping("/wp-admin/edit-pages.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_pages";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);

        // Handle bulk deletes
        if (isset(gVars.webEnv._GET.getValue("deleteit")) && isset(gVars.webEnv._GET.getValue("delete"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-pages", "_wpnonce");

            for (Map.Entry javaEntry14 : new Array<Object>(gVars.webEnv._GET.getValue("delete")).entrySet()) {
                gVars.post_id_del = intval(javaEntry14.getValue());
                gVars.post_del = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(gVars.post_id_del, gConsts.getOBJECT(), "raw");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_page", gVars.post_id_del)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to delete this page.", "default"), "");
                }

                if (equal(StdClass.getValue(gVars.post_del, "post_type"), "attachment")) {
                    if (!booleanval(getIncluded(PostPage.class, gVars, gConsts).wp_delete_attachment(gVars.post_id_del))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Error in deleting...", "default"), "");
                    }
                } else {
                    if (!booleanval(getIncluded(PostPage.class, gVars, gConsts).wp_delete_post(gVars.post_id_del))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Error in deleting...", "default"), "");
                    }
                }
            }

            gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer();

            if (!strictEqual(Strings.strpos(gVars.sendback, "page.php"), BOOLEAN_FALSE)) {
                gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/page-new.php";
            } else if (!strictEqual(Strings.strpos(gVars.sendback, "attachments.php"), BOOLEAN_FALSE)) {
                gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/attachments.php";
            }

            gVars.sendback = QRegExPerl.preg_replace("|[^a-z0-9-~+_.?#=&;,/:]|i", "", gVars.sendback);
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.sendback, 302);
            System.exit();
        } else if (!empty(gVars.webEnv._GET.getValue("_wp_http_referer"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(
                        new ArrayEntry<Object>("_wp_http_referer"),
                        new ArrayEntry<Object>("_wpnonce")), Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())), 302);
            System.exit();
        }

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Pages", "default");
        gVars.parent_file = "edit.php";
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
        
        gVars.post_stati = new Array<Object>(	//	array( adj, noun )
                new ArrayEntry<Object>("publish",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Published", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Published pages", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Published (%s)", "Published (%s)", 1, "default")))),
                new ArrayEntry<Object>("future",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Scheduled", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Scheduled pages", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Scheduled (%s)", "Scheduled (%s)", 1, "default")))),
                new ArrayEntry<Object>("pending",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Pending Review", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Pending pages", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Pending Review (%s)", "Pending Review (%s)", 1, "default")))),
                new ArrayEntry<Object>("draft",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Draft", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts)._c("Drafts|manage posts header", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Draft (%s)", "Drafts (%s)", 1, "default")))),
                new ArrayEntry<Object>("private",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Private", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Private pages", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Private (%s)", "Private (%s)", 1, "default")))));
        
        gVars.post_status_label = getIncluded(L10nPage.class, gVars, gConsts).__("Manage Pages", "default");
        post_status_q = "";

        if (isset(gVars.webEnv._GET.getValue("post_status")) && Array.in_array(gVars.webEnv._GET.getValue("post_status"), Array.array_keys(gVars.post_stati))) {
            gVars.post_status_label = gVars.post_stati.getArrayValue(gVars.webEnv._GET.getValue("post_status")).getValue(1);
            post_status_q = "&post_status=" + strval(gVars.webEnv._GET.getValue("post_status"));
            post_status_q = post_status_q + "&perm=readable";
        }

        query_str = "post_type=page&orderby=menu_order title&what_to_show=posts" + post_status_q + "&posts_per_page=-1&posts_per_archive_page=-1&order=asc";
        query_str = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("manage_pages_query", query_str));
        getIncluded(FunctionsPage.class, gVars, gConsts).wp(query_str);

        if (getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-comments", false, new Array<Object>(), false);
        }

        requireOnce(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block2");
        
        // Use $_GET instead of is_ since they can override each other
        gVars.h2_search = ((isset(gVars.webEnv._GET.getValue("s")) && booleanval(gVars.webEnv._GET.getValue("s")))
            ? (" " +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("matching &#8220;%s&#8221;", "default"),
                getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("s"))), strval(0))))
            : "");
        gVars.h2_author = "";

        if (isset(gVars.webEnv._GET.getValue("author")) && booleanval(gVars.webEnv._GET.getValue("author"))) {
            gVars.author_user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(gVars.webEnv._GET.getValue("author")));
            gVars.h2_author = " " +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("by %s", "default"),
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(gVars.author_user, "display_name")), strval(0)));
        }

        QStrings.printf(
            gVars.webEnv,
            getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s%2$s%3$s|You can reorder these: 1: Pages, 2: by {s}, 3: matching {s}", "default"),
            gVars.post_status_label,
            gVars.h2_author,
            gVars.h2_search);

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block3");
        gVars.avail_post_stati = (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).get_available_post_statuses("page");
        gVars.status_links = new Array<Object>();
        gVars.num_posts = getIncluded(PostPage.class, gVars, gConsts).wp_count_posts("page", "readable");
        gVars._class = (empty(gVars.webEnv._GET.getValue("post_status"))
            ? " class=\"current\""
            : "");
        gVars.status_links.putValue("<li><a href=\"edit-pages.php\"" + gVars._class + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("All Pages", "default") + "</a>");

        for (Map.Entry javaEntry15 : gVars.post_stati.entrySet()) {
            gVars.status = strval(javaEntry15.getKey());
            gVars.label = (Array<Object>) javaEntry15.getValue();
            gVars._class = "";

            if (!Array.in_array(gVars.status, gVars.avail_post_stati)) {
                continue;
            }

            if (equal(gVars.status, gVars.webEnv._GET.getValue("post_status"))) {
                gVars._class = " class=\"current\"";
            }

            gVars.status_links.putValue(
                    "<li><a href=\"edit-pages.php?post_status=" + gVars.status + "\"" + gVars._class + ">" +
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                            strval(gVars.label.getArrayValue(2).getValue(0)),
                            strval(gVars.label.getArrayValue(2).getValue(1)),
                            intval(StdClass.getValue(gVars.num_posts, gVars.status)),
                            "default"), getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(intval(StdClass.getValue(gVars.num_posts, gVars.status)), null)) + "</a>");
        }

        echo(gVars.webEnv, Strings.implode(" |</li>", gVars.status_links) + "</li>");
        gVars.status_links = null;

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block4");

        if (isset(gVars.webEnv._GET.getValue("post_status"))) {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"post_status\" value=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("post_status"))));
            echo(gVars.webEnv, "\" />\n");
        } else {
        }

        if (isset(gVars.webEnv._GET.getValue("posted")) && booleanval(gVars.webEnv._GET.getValue("posted"))) {
            gVars.webEnv._GET.putValue("posted", intval(gVars.webEnv._GET.getValue("posted")));
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p><strong>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Your page has been saved.", "default");
            echo(gVars.webEnv, "</strong> <a href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.webEnv._GET.getValue("posted"), false));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("View page", "default");
            echo(gVars.webEnv, "</a> | <a href=\"page.php?action=edit&amp;post=");
            echo(gVars.webEnv, gVars.webEnv._GET.getValue("posted"));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Edit page", "default");
            echo(gVars.webEnv, "</a></p></div>\n");
            gVars.webEnv._SERVER.putValue(
                "REQUEST_URI",
                getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(new ArrayEntry<Object>("posted")), gVars.webEnv.getRequestURI()));
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block5");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("s")))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Search Pages", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block8");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-pages", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block9");
        all = !(booleanval(gVars.h2_search) || booleanval(post_status_q));

        if (booleanval(gVars.posts)) {
            echo(gVars.webEnv, "<table class=\"widefat\">\n  <thead>\n  <tr>\n");
            gVars.posts_columns = getIncluded(TemplatePage.class, gVars, gConsts).wp_manage_pages_columns();

            for (Map.Entry javaEntry16 : gVars.posts_columns.entrySet()) {
                gVars.post_column_key = javaEntry16.getKey();
                gVars.column_display_name = javaEntry16.getValue();

                if (strictEqual("cb", gVars.post_column_key)) {
                    gVars._class = " class=\"check-column\"";
                } else if (strictEqual("comments", gVars.post_column_key)) {
                    gVars._class = " class=\"num\"";
                } else {
                    gVars._class = "";
                }

                echo(gVars.webEnv, "\t<th scope=\"col\"");
                echo(gVars.webEnv, gVars._class);
                echo(gVars.webEnv, ">");
                echo(gVars.webEnv, gVars.column_display_name);
                echo(gVars.webEnv, "</th>\n");
            }

            echo(gVars.webEnv, "  </tr>\n  </thead>\n  <tbody>\n  ");
            getIncluded(TemplatePage.class, gVars, gConsts).page_rows(gVars.posts);
            echo(gVars.webEnv, "  </tbody>\n</table>\n\n</form>\n\n<div id=\"ajax-response\"></div>\n\n");
        } else {
            echo(gVars.webEnv, "</form>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No pages found.", "default");
            echo(gVars.webEnv, "</p>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block10");

        if (equal(1, Array.count(gVars.posts)) && getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            gVars.comments = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = " + gVars.id + " AND comment_approved != \'spam\' ORDER BY comment_date");

            if (booleanval(gVars.comments)) {
            	// Make sure comments, post, and post_author are cached
                getIncluded(CommentPage.class, gVars, gConsts).update_comment_cache(gVars.comments);
                gVars.post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(gVars.id, gConsts.getOBJECT(), "raw");
                gVars.authordata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(gVars.post, "post_author")));
                echo(gVars.webEnv, "\n<br class=\"clear\" />\n\n<table class=\"widefat\" style=\"margin-top: .5em\">\n<thead>\n  <tr>\n    <th scope=\"col\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Comment", "default");
                echo(gVars.webEnv, "</th>\n    <th scope=\"col\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Date", "default");
                echo(gVars.webEnv, "</th>\n    <th scope=\"col\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Actions", "default");
                echo(gVars.webEnv, "</th>\n  </tr>\n</thead>\n<tbody id=\"the-comment-list\" class=\"list:comment\">\n");

                for (Map.Entry javaEntry17 : gVars.comments.entrySet()) {
                    gVars.comment = (StdClass) javaEntry17.getValue();
                    getIncluded(TemplatePage.class, gVars, gConsts)._wp_comment_row(intval(StdClass.getValue(gVars.comment, "comment_ID")), "detail", strval(false), false);
                }

                echo(gVars.webEnv, "</tbody>\n</table>\n\n");
            } else {
            } // comments
        } else {
        } // posts;

        /* Start of block */
        super.startBlock("__wp_admin_edit_pages_block11");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
