/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: EditPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class EditPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(EditPage.class.getName());
    public Object dropdown_options;

    @Override
    @RequestMapping("/wp-admin/edit.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);

        // Handle bulk deletes
        if (isset(gVars.webEnv._GET.getValue("deleteit")) && isset(gVars.webEnv._GET.getValue("delete"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-posts", "_wpnonce");

            for (Map.Entry javaEntry24 : new Array<Object>(gVars.webEnv._GET.getValue("delete")).entrySet()) {
                gVars.post_id_del = intval(javaEntry24.getValue());
                gVars.post_del = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(gVars.post_id_del, gConsts.getOBJECT(), "raw");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_post", gVars.post_id_del)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to delete this post.", "default"), "");
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

            if (!strictEqual(Strings.strpos(gVars.sendback, "post.php"), BOOLEAN_FALSE)) {
                gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/post-new.php";
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

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Posts", "default");
        gVars.parent_file = "edit.php";
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    gVars.post_stati = srcArray.getArrayValue(0);
                    gVars.avail_post_stati = srcArray.getArrayValue(1);

                    return srcArray;
                }
            }.doAssign((((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).wp_edit_posts_query(null));

        if (equal(1, Array.count(gVars.posts)) && getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-comments", false, new Array<Object>(), false);
        }

        requireOnce(gVars, gConsts, Admin_headerPage.class);

        if (!isset(gVars.webEnv._GET.getValue("paged"))) {
            gVars.webEnv._GET.putValue("paged", 1);
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_block2");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Comments on %s", "default"),
                getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(gVars.post, "post_title")));
        } else/*
         * Use $_GET instead of is_ since they can override each other Use $_GET
         * instead of is_ since they can override each other
         */
         {
            gVars.post_status_label = getIncluded(L10nPage.class, gVars, gConsts)._c("Manage Posts|manage posts header", "default");

            if (isset(gVars.webEnv._GET.getValue("post_status")) && Array.in_array(gVars.webEnv._GET.getValue("post_status"), Array.array_keys(gVars.post_stati))) {
                gVars.post_status_label = gVars.post_stati.getArrayValue(gVars.webEnv._GET.getValue("post_status")).getValue(1);
            }

            if (booleanval(gVars.post_listing_pageable) && !getIncluded(QueryPage.class, gVars, gConsts).is_archive() && !getIncluded(QueryPage.class, gVars, gConsts).is_search()) {
                gVars.h2_noun = (getIncluded(QueryPage.class, gVars, gConsts).is_paged()
                    ? QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Previous %s", "default"), gVars.post_status_label)
                    : QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Latest %s", "default"), gVars.post_status_label));
            } else {
                gVars.h2_noun = strval(gVars.post_status_label);
            }

            // Use $_GET instead of is_ since they can override each other
            gVars.h2_author = "";
            gVars.webEnv._GET.putValue("author", intval(gVars.webEnv._GET.getValue("author")));

            if (!equal(gVars.webEnv._GET.getValue("author"), 0)) { // author exclusion
                if (equal(gVars.webEnv._GET.getValue("author"), "-" + strval(gVars.user_ID))) {
                    gVars.h2_author = " " + getIncluded(L10nPage.class, gVars, gConsts).__("by other authors", "default");
                } else {
                    gVars.author_user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("author")));
                    gVars.h2_author = " " +
                        QStrings.sprintf(
                            getIncluded(L10nPage.class, gVars, gConsts).__("by %s", "default"),
                            getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(gVars.author_user, "display_name")), strval(0)));
                }
            }

            gVars.h2_search = ((isset(gVars.webEnv._GET.getValue("s")) && booleanval(gVars.webEnv._GET.getValue("s")))
                ? (" " +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("matching &#8220;%s&#8221;", "default"),
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(getIncluded(General_templatePage.class, gVars, gConsts).get_search_query(), strval(0))))
                : "");
            gVars.h2_cat = ((isset(gVars.webEnv._GET.getValue("cat")) && booleanval(gVars.webEnv._GET.getValue("cat")))
                ? (" " +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("in &#8220;%s&#8221;", "default"), getIncluded(General_templatePage.class, gVars, gConsts).single_cat_title("", false)))
                : "");
            gVars.h2_tag = ((isset(gVars.webEnv._GET.getValue("tag")) && booleanval(gVars.webEnv._GET.getValue("tag")))
                ? (" " +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("tagged with &#8220;%s&#8221;", "default"),
                    getIncluded(General_templatePage.class, gVars, gConsts).single_tag_title("", false)))
                : "");
            gVars.h2_month = ((isset(gVars.webEnv._GET.getValue("m")) && booleanval(gVars.webEnv._GET.getValue("m")))
                ? (" " +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("during %s", "default"), getIncluded(General_templatePage.class, gVars, gConsts).single_month_title(" ", false)))
                : "");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts)._c(
                        "%1$s%2$s%3$s%4$s%5$s%6$s|You can reorder these: 1: Posts, 2: by {s}, 3: matching {s}, 4: in {s}, 5: tagged with {s}, 6: during {s}",
                        "default"),
                    gVars.h2_noun,
                    gVars.h2_author,
                    gVars.h2_search,
                    gVars.h2_cat,
                    gVars.h2_tag,
                    gVars.h2_month);
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_block3");
        gVars.status_links = new Array<Object>();
        gVars.num_posts = getIncluded(PostPage.class, gVars, gConsts).wp_count_posts("post", "readable");
        gVars._class = (empty(gVars.webEnv._GET.getValue("post_status"))
            ? " class=\"current\""
            : "");
        gVars.status_links.putValue("<li><a href=\'edit.php\' " + gVars._class + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("All Posts", "default") + "</a>");

        for (Map.Entry javaEntry25 : gVars.post_stati.entrySet()) {
            gVars.status = strval(javaEntry25.getKey());
            gVars.label = (Array<Object>) javaEntry25.getValue();
            gVars._class = "";

            if (!Array.in_array(gVars.status, gVars.avail_post_stati)) {
                continue;
            }

            if (empty(StdClass.getValue(gVars.num_posts, gVars.status))) {
                continue;
            }

            if (equal(gVars.status, gVars.webEnv._GET.getValue("post_status"))) {
                gVars._class = " class=\"current\"";
            }

            gVars.status_links.putValue(
                    "<li><a href=\'edit.php?post_status=" + gVars.status + "\' " + gVars._class + ">" +
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                            strval(gVars.label.getArrayValue(2).getValue(0)),
                            strval(gVars.label.getArrayValue(2).getValue(1)),
                            intval(StdClass.getValue(gVars.num_posts, gVars.status)),
                            "default"), (((FunctionsPage) PhpWeb.getIncluded(FunctionsPage.class, gVars, gConsts))).number_format_i18n(intval(StdClass.getValue(gVars.num_posts, gVars.status)), null)) +
                    "</a>");
        }

        echo(gVars.webEnv, Strings.implode(" |</li>", gVars.status_links) + "</li>");
        gVars.status_links = null;

        /* Start of block */
        super.startBlock("__wp_admin_edit_block4");

        if (isset(gVars.webEnv._GET.getValue("post_status"))) {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"post_status\" value=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("post_status"))));
            echo(gVars.webEnv, "\" />\n");
        } else {
        }

        if (isset(gVars.webEnv._GET.getValue("posted")) && booleanval(gVars.webEnv._GET.getValue("posted"))) {
            gVars.webEnv._GET.putValue("posted", intval(gVars.webEnv._GET.getValue("posted")));
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p><strong>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Your post has been saved.", "default");
            echo(gVars.webEnv, "</strong> <a href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.webEnv._GET.getValue("posted"), false));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("View post", "default");
            echo(gVars.webEnv, "</a> | <a href=\"post.php?action=edit&amp;post=");
            echo(gVars.webEnv, gVars.webEnv._GET.getValue("posted"));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Edit post", "default");
            echo(gVars.webEnv, "</a></p></div>\n");
            gVars.webEnv._SERVER.putValue(
                "REQUEST_URI",
                getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(new ArrayEntry<Object>("posted")), gVars.webEnv.getRequestURI()));
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_block5");
        getIncluded(General_templatePage.class, gVars, gConsts).the_search_query();

        /* Start of block */
        super.startBlock("__wp_admin_edit_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Search Posts", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_block7");
        gVars.page_links = strval(
                getIncluded(General_templatePage.class, gVars, gConsts).paginate_links(
                    new Array<Object>(
                        new ArrayEntry<Object>("base", getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("paged", "%#%")),
                        new ArrayEntry<Object>("format", ""),
                        new ArrayEntry<Object>("total", gVars.wp_query.max_num_pages),
                        new ArrayEntry<Object>("current", gVars.webEnv._GET.getValue("paged")))));

        if (booleanval(gVars.page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_block9");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-posts", "_wpnonce", true, true);

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            gVars.arc_query = "SELECT DISTINCT YEAR(post_date) AS yyear, MONTH(post_date) AS mmonth FROM " + gVars.wpdb.posts + " WHERE post_type = \'post\' ORDER BY post_date DESC";
            gVars.arc_result = gVars.wpdb.get_results(gVars.arc_query);
            gVars.month_count = Array.count(gVars.arc_result);

            if (booleanval(gVars.month_count) && !(equal(1, gVars.month_count) && equal(0, gVars.arc_result.getValue(0).fields.getValue("mmonth")))) {
                echo(gVars.webEnv, "<select name=\'m\'>\n<option");
                getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(gVars.webEnv._GET.getValue("m")), strval(0));
                echo(gVars.webEnv, " value=\'0\'>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Show all dates", "default");
                echo(gVars.webEnv, "</option>\n");

                for (Map.Entry javaEntry26 : gVars.arc_result.entrySet()) {
                    gVars.arc_row = (StdClass) javaEntry26.getValue();

                    if (equal(StdClass.getValue(gVars.arc_row, "yyear"), 0)) {
                        continue;
                    }

                    gVars.arc_row.fields.putValue("mmonth", getIncluded(FormattingPage.class, gVars, gConsts).zeroise(strval(StdClass.getValue(gVars.arc_row, "mmonth")), 2));

                    if (equal(strval(StdClass.getValue(gVars.arc_row, "yyear")) + strval(StdClass.getValue(gVars.arc_row, "mmonth")), gVars.webEnv._GET.getValue("m"))) {
                        gVars._default = " selected=\"selected\"";
                    } else {
                        gVars._default = "";
                    }

                    echo(gVars.webEnv, "<option" + gVars._default + " value=\'" + strval(StdClass.getValue(gVars.arc_row, "yyear")) + strval(StdClass.getValue(gVars.arc_row, "mmonth")) + "\'>");
                    echo(gVars.webEnv, gVars.wp_locale.get_month(strval(StdClass.getValue(gVars.arc_row, "mmonth"))) + " " + strval(StdClass.getValue(gVars.arc_row, "yyear")));
                    echo(gVars.webEnv, "</option>\n");
                }

                echo(gVars.webEnv, "</select>\n");
            }

            echo(gVars.webEnv, "\n");
            dropdown_options = new Array<Object>(
                    new ArrayEntry<Object>("show_option_all", getIncluded(L10nPage.class, gVars, gConsts).__("View all categories", "default")),
                    new ArrayEntry<Object>("hide_empty", 0),
                    new ArrayEntry<Object>("hierarchical", 1),
                    new ArrayEntry<Object>("show_count", 0),
                    new ArrayEntry<Object>("orderby", "name"),
                    new ArrayEntry<Object>("selected", gVars.cat));
            getIncluded(Category_templatePage.class, gVars, gConsts).wp_dropdown_categories(dropdown_options);
            getIncluded(PluginPage.class, gVars, gConsts).do_action("restrict_manage_posts", "");
            echo(gVars.webEnv, "<input type=\"submit\" id=\"post-query-submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Filter", "default");
            echo(gVars.webEnv, "\" class=\"button-secondary\" />\n\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_block10");
        include(gVars, gConsts, Edit_post_rowsPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_edit_block11");

        if (booleanval(gVars.page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_block12");

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

                for (Map.Entry javaEntry27 : gVars.comments.entrySet()) {
                    gVars.comment = (StdClass) javaEntry27.getValue();
                    getIncluded(TemplatePage.class, gVars, gConsts)._wp_comment_row(intval(StdClass.getValue(gVars.comment, "comment_ID")), "detail", strval(false), false);
                }

                echo(gVars.webEnv, "</tbody>\n</table>\n\n");
            } else {
            } // comments
        } else {
        } // posts;

        /* Start of block */
        super.startBlock("__wp_admin_edit_block13");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
