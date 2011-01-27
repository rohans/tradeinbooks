/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_commentsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.CommentPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ListAssigner;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_commentsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_commentsPage.class.getName());
    public int comments_deleted;
    public int comments_approved;
    public int comments_unapproved;
    public int comments_spammed;
    public String comment_status;
    public String search_dirty;
    public int approved;
    public int spam;
    public StdClass num_comments;
    public Array<Object> stati;
    public Array<StdClass> _comments;
    public Array<StdClass> extra_comments;

    @Override
    @RequestMapping("/wp-admin/edit-comments.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_comments";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Comments", "default");
        gVars.parent_file = "edit-comments.php";
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-comments", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);

        if (!empty(gVars.webEnv._REQUEST.getValue("delete_comments"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-comments", "_wpnonce");
            comments_deleted = comments_approved = comments_unapproved = comments_spammed = 0;

            for (Map.Entry javaEntry7 : (Set<Map.Entry>) gVars.webEnv._REQUEST.getArrayValue("delete_comments").entrySet()) {// Check the permissions on each
                int comment = intval(javaEntry7.getValue());

                //				comment = intval(comment);
                gVars.post_id = intval(gVars.wpdb.get_var("SELECT comment_post_ID FROM " + gVars.wpdb.comments + " WHERE comment_ID = " + comment));

                // $authordata = get_userdata( $wpdb->get_var("SELECT post_author FROM $wpdb->posts WHERE ID = $post_id") );
                
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.post_id)) {
                    continue;
                }

                if (!empty(gVars.webEnv._REQUEST.getValue("spamit"))) {
                    getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(comment, "spam");
                    comments_spammed++;
                } else if (!empty(gVars.webEnv._REQUEST.getValue("deleteit"))) {
                    getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(comment, "delete");
                    comments_deleted++;
                } else if (!empty(gVars.webEnv._REQUEST.getValue("approveit"))) {
                    getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(comment, "approve");
                    comments_approved++;
                } else if (!empty(gVars.webEnv._REQUEST.getValue("unapproveit"))) {
                    getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(comment, "hold");
                    comments_unapproved++;
                }
            }

            gVars.redirect_to = "edit-comments.php" /*FileSystemOrSocket.basename(SourceCodeInfo.getCurrentFile(gVars.webEnv))*/ + "?deleted=" + strval(comments_deleted) + "&approved=" +
                strval(comments_approved) + "&spam=" + strval(comments_spammed) + "&unapproved=" + strval(comments_unapproved);

            if (!empty(gVars.webEnv._REQUEST.getValue("mode"))) {
                gVars.redirect_to = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("mode", gVars.webEnv._REQUEST.getValue("mode"), gVars.redirect_to);
            }

            if (!empty(gVars.webEnv._REQUEST.getValue("comment_status"))) {
                gVars.redirect_to = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("comment_status", gVars.webEnv._REQUEST.getValue("comment_status"), gVars.redirect_to);
            }

            if (!empty(gVars.webEnv._REQUEST.getValue("s"))) {
                gVars.redirect_to = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("s", gVars.webEnv._REQUEST.getValue("s"), gVars.redirect_to);
            }

            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect_to, 302);
        } else if (!empty(gVars.webEnv._GET.getValue("_wp_http_referer"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(
                        new ArrayEntry<Object>("_wp_http_referer"),
                        new ArrayEntry<Object>("_wpnonce")), Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())), 302);
            System.exit();
        }

        requireOnce(gVars, gConsts, Admin_headerPage.class);

        if (empty(gVars.webEnv._GET.getValue("mode"))) {
            gVars.mode = "detail";
        } else {
            gVars.mode = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("mode")));
        }

        if (isset(gVars.webEnv._GET.getValue("comment_status"))) {
            comment_status = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("comment_status")));
        } else {
            comment_status = "";
        }

        if (isset(gVars.webEnv._GET.getValue("s"))) {
            search_dirty = strval(gVars.webEnv._GET.getValue("s"));
        } else {
            search_dirty = "";
        }

        gVars.search = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(search_dirty);

        if (isset(gVars.webEnv._GET.getValue("approved")) || isset(gVars.webEnv._GET.getValue("deleted")) || isset(gVars.webEnv._GET.getValue("spam"))) {
            approved = (isset(gVars.webEnv._GET.getValue("approved"))
                ? intval(gVars.webEnv._GET.getValue("approved"))
                : 0);
            gVars.deleted = (isset(gVars.webEnv._GET.getValue("deleted"))
                ? intval(gVars.webEnv._GET.getValue("deleted"))
                : 0);
            spam = (isset(gVars.webEnv._GET.getValue("spam"))
                ? intval(gVars.webEnv._GET.getValue("spam"))
                : 0);

            if ((approved > 0) || (gVars.deleted > 0) || (spam > 0)) {
                echo(gVars.webEnv, "<div id=\"moderated\" class=\"updated fade\"><p>");

                if (approved > 0) {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s comment approved", "%s comments approved", approved, "default"), approved);
                    echo(gVars.webEnv, "<br />");
                }

                if (gVars.deleted > 0) {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s comment deleted", "%s comments deleted", gVars.deleted, "default"), gVars.deleted);
                    echo(gVars.webEnv, "<br />");
                }

                if (spam > 0) {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s comment marked as spam", "%s comments marked as spam", spam, "default"), spam);
                    echo(gVars.webEnv, "<br />");
                }

                echo(gVars.webEnv, "</p></div>");
            }
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block3");
        gVars.status_links = new Array<Object>();
        num_comments = getIncluded(CommentPage.class, gVars, gConsts).wp_count_comments();
        stati = new Array<Object>(
                new ArrayEntry<Object>("moderated",
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)
                                         .__ngettext("Awaiting Moderation (%s)", "Awaiting Moderation (%s)", intval(StdClass.getValue(num_comments, "moderated")), "default"),
                        "<span class=\'comment-count\'>" + StdClass.getValue(num_comments, "moderated") + "</span>")),
                new ArrayEntry<Object>("approved", getIncluded(L10nPage.class, gVars, gConsts)._c("Approved|plural", "default")));
        gVars._class = (strictEqual("", comment_status)
            ? " class=\"current\""
            : "");
        gVars.status_links.putValue("<li><a href=\"edit-comments.php\"" + gVars._class + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("Show All Comments", "default") + "</a>");

        for (Map.Entry javaEntry8 : stati.entrySet()) {
            gVars.status = strval(javaEntry8.getKey());

            String label = strval(javaEntry8.getValue());
            gVars._class = "";

            if (equal(gVars.status, comment_status)) {
                gVars._class = " class=\"current\"";
            }

            gVars.status_links.putValue("<li><a href=\"edit-comments.php?comment_status=" + gVars.status + "\"" + gVars._class + ">" + label + "</a>");
        }

        gVars.status_links = (Array) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_status_links", gVars.status_links);
        echo(gVars.webEnv, Strings.implode(" | </li>", gVars.status_links) + "</li>");
        gVars.status_links = null;

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block4");
        echo(gVars.webEnv, gVars.search);

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Search Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block6");
        echo(gVars.webEnv, gVars.mode);

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block7");
        echo(gVars.webEnv, comment_status);

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block8");

        if (equal("detail", gVars.mode)) {
            echo(gVars.webEnv, "class=\'current\'");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block9");
        echo(
            gVars.webEnv,
            getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("mode", "detail", gVars.webEnv.getRequestURI()), null, "display"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Detail View", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block11");

        if (equal("list", gVars.mode)) {
            echo(gVars.webEnv, "class=\'current\'");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block12");
        echo(
            gVars.webEnv,
            getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("mode", "list", gVars.webEnv.getRequestURI()), null, "display"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("List View", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block14");

        if (isset(gVars.webEnv._GET.getValue("apage"))) {
            gVars.page = Math.abs(intval(gVars.webEnv._GET.getValue("apage")));
        } else {
            gVars.page = 1;
        }

        gVars.start = gVars.offset = (intval(gVars.page) - 1) * 20;
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    _comments = srcArray.getArrayValue(0);
                    gVars.total = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(getIncluded(TemplatePage.class, gVars, gConsts)._wp_get_comment_list(comment_status, search_dirty, gVars.start, 25));
        gVars.comments = Array.array_slice(_comments, 0, 20);
        extra_comments = Array.array_slice(_comments, 20);
        gVars.page_links = strval(
                getIncluded(General_templatePage.class, gVars, gConsts).paginate_links(
                    new Array<Object>(
                        new ArrayEntry<Object>("base", getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("apage", "%#%")),
                        new ArrayEntry<Object>("format", ""),
                        new ArrayEntry<Object>("total", Math.ceil(floatval(gVars.total) / floatval(20))),
                        new ArrayEntry<Object>("current", gVars.page))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block15");

        if (booleanval(gVars.page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block16");

        if (!equal("approved", comment_status)) {
            echo(gVars.webEnv, "<input type=\"submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Approve", "default");
            echo(gVars.webEnv, "\" name=\"approveit\" class=\"button-secondary\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Mark as Spam", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block18");

        if (!equal("moderated", comment_status)) {
            echo(gVars.webEnv, "<input type=\"submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Unapprove", "default");
            echo(gVars.webEnv, "\" name=\"unapproveit\" class=\"button-secondary\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block20");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("manage_comments_nav", comment_status);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-comments", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block21");

        if (booleanval(gVars.comments)) {
            echo(
                    gVars.webEnv,
                    "<table class=\"widefat\">\n<thead>\n  <tr>\n    <th scope=\"col\" class=\"check-column\"><input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'comments-form\'));\" /></th>\n    <th scope=\"col\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Comment", "default");
            echo(gVars.webEnv, "</th>\n    <th scope=\"col\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Date", "default");
            echo(gVars.webEnv, "</th>\n    <th scope=\"col\" class=\"action-links\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Actions", "default");
            echo(gVars.webEnv, "</th>\n  </tr>\n</thead>\n<tbody id=\"the-comment-list\" class=\"list:comment\">\n");

            for (Map.Entry javaEntry9 : gVars.comments.entrySet()) {
                gVars.comment = (StdClass) javaEntry9.getValue();
                getIncluded(TemplatePage.class, gVars, gConsts)._wp_comment_row(intval(StdClass.getValue(gVars.comment, "comment_ID")), gVars.mode, comment_status, true);
            }

            echo(gVars.webEnv, "</tbody>\n<tbody id=\"the-extra-comment-list\" class=\"list:comment\" style=\"display: none;\">\n");

            for (Map.Entry javaEntry10 : extra_comments.entrySet()) {
                gVars.comment = (StdClass) javaEntry10.getValue();
                getIncluded(TemplatePage.class, gVars, gConsts)._wp_comment_row(intval(StdClass.getValue(gVars.comment, "comment_ID")), gVars.mode, comment_status, true);
            }

            echo(
                    gVars.webEnv,
                    "</tbody>\n</table>\n\n</form>\n\n<form id=\"get-extra-comments\" method=\"post\" action=\"\" class=\"add:the-extra-comment-list:\" style=\"display: none;\">\n\t<input type=\"hidden\" name=\"s\" value=\"");
            echo(gVars.webEnv, gVars.search);
            echo(gVars.webEnv, "\" />\n\t<input type=\"hidden\" name=\"mode\" value=\"");
            echo(gVars.webEnv, gVars.mode);
            echo(gVars.webEnv, "\" />\n\t<input type=\"hidden\" name=\"comment_status\" value=\"");
            echo(gVars.webEnv, comment_status);
            echo(gVars.webEnv, "\" />\n\t<input type=\"hidden\" name=\"page\" value=\"");
            echo(gVars.webEnv, isset(gVars.webEnv._REQUEST.getValue("page"))
                ? getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._REQUEST.getValue("page"))
                : floatval(1));
            echo(gVars.webEnv, "\" />\n\t");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-comment", "_ajax_nonce", false, true);
            echo(gVars.webEnv, "</form>\n\n<div id=\"ajax-response\"></div>\n");
        } else if (equal("moderated", gVars.webEnv._GET.getValue("comment_status"))) {
            echo(gVars.webEnv, "<p>\n");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No comments awaiting moderation&hellip; yet.", "default");
            echo(gVars.webEnv, "</p>\n");
        } else {
            echo(gVars.webEnv, "<p>\n");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No results found.", "default");
            echo(gVars.webEnv, "</p>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block22");

        if (booleanval(gVars.page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_comments_block23");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
