/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_post_rowsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_post_rowsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_post_rowsPage.class.getName());
    public StdClass a_post;
    public Object comment_pending_count;
    public Array<String> out = new Array<String>();

    @Override
    @RequestMapping("/wp-admin/edit-post-rows.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_post_rows";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_post_rows_block1");
        gVars.webEnv = webEnv;

        if (!gConsts.isABSPATHDefined()) {
            System.exit();
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_post_rows_block2");
        gVars.posts_columns = getIncluded(TemplatePage.class, gVars, gConsts).wp_manage_posts_columns();

        for (Map.Entry javaEntry18 : gVars.posts_columns.entrySet()) {
            gVars.post_column_key = javaEntry18.getKey();
            gVars.column_display_name = javaEntry18.getValue();

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

        /* Start of block */
        super.startBlock("__wp_admin_edit_post_rows_block3");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            gVars.bgcolor = "";
            getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 10, 1);

            // Create array of post IDs.
            gVars.post_ids = new Array<Object>();

            for (Map.Entry javaEntry19 : gVars.wp_query.posts.entrySet()) {
                a_post = (StdClass) javaEntry19.getValue();
                gVars.post_ids.putValue(StdClass.getValue(a_post, "ID"));
            }

            comment_pending_count = getIncluded(CommentPage.class, gVars, gConsts).get_pending_comments_num(gVars.post_ids);

            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                gVars._class = (equal("alternate", gVars._class)
                    ? ""
                    : "alternate");
                gVars.post_owner = (equal(gVars.current_user.getID(), StdClass.getValue(gVars.post, "post_author"))
                    ? "self"
                    : "other");
                gVars.title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0);

                if (empty(gVars.title)) {
                    gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("(no title)", "default");
                }

                echo(gVars.webEnv, "\t<tr id=\'post-");
                echo(gVars.webEnv, gVars.id);
                echo(gVars.webEnv, "\' class=\'");
                echo(gVars.webEnv, Strings.trim(gVars._class + " author-" + gVars.post_owner + " status-" + StdClass.getValue(gVars.post, "post_status")));
                echo(gVars.webEnv, "\' valign=\"top\">\n\n");

                for (Map.Entry javaEntry20 : gVars.posts_columns.entrySet()) {
                    gVars.column_name = javaEntry20.getKey();
                    gVars.column_display_name = javaEntry20.getValue();

                    {
                        int javaSwitchSelector5 = 0;

                        if (equal(gVars.column_name, "cb")) {
                            javaSwitchSelector5 = 1;
                        }

                        if (equal(gVars.column_name, "modified")) {
                            javaSwitchSelector5 = 2;
                        }

                        if (equal(gVars.column_name, "date")) {
                            javaSwitchSelector5 = 3;
                        }

                        if (equal(gVars.column_name, "title")) {
                            javaSwitchSelector5 = 4;
                        }

                        if (equal(gVars.column_name, "categories")) {
                            javaSwitchSelector5 = 5;
                        }

                        if (equal(gVars.column_name, "tags")) {
                            javaSwitchSelector5 = 6;
                        }

                        if (equal(gVars.column_name, "comments")) {
                            javaSwitchSelector5 = 7;
                        }

                        if (equal(gVars.column_name, "author")) {
                            javaSwitchSelector5 = 8;
                        }

                        if (equal(gVars.column_name, "status")) {
                            javaSwitchSelector5 = 9;
                        }

                        if (equal(gVars.column_name, "control_view")) {
                            javaSwitchSelector5 = 10;
                        }

                        if (equal(gVars.column_name, "control_edit")) {
                            javaSwitchSelector5 = 11;
                        }

                        if (equal(gVars.column_name, "control_delete")) {
                            javaSwitchSelector5 = 12;
                        }

                        switch (javaSwitchSelector5) {
                        case 1: {
                            echo(gVars.webEnv, "\t\t<th scope=\"row\" class=\"check-column\">");

                            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
                                echo(gVars.webEnv, "<input type=\"checkbox\" name=\"delete[]\" value=\"");
                                getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                                echo(gVars.webEnv, "\" />");
                            }

                            echo(gVars.webEnv, "</th>\n\t\t");

                            break;
                        }

                        case 2: {
                        }

                        case 3: {
                            if (equal("0000-00-00 00:00:00", StdClass.getValue(gVars.post, "post_date")) && equal("date", gVars.column_name)) {
                                gVars.t_time = gVars.h_time = getIncluded(L10nPage.class, gVars, gConsts).__("Unpublished", "default");
                            } else {
                                if (equal("modified", gVars.column_name)) {
                                    gVars.t_time = getIncluded(General_templatePage.class, gVars, gConsts)
                                                       .get_the_modified_time(getIncluded(L10nPage.class, gVars, gConsts).__("Y/m/d g:i:s A", "default"));
                                    gVars.m_time = strval(StdClass.getValue(gVars.post, "post_modified"));
                                    gVars.time = getIncluded(General_templatePage.class, gVars, gConsts).get_post_modified_time("G", true);
                                } else {
                                    gVars.t_time = getIncluded(General_templatePage.class, gVars, gConsts).get_the_time(getIncluded(L10nPage.class, gVars, gConsts).__("Y/m/d g:i:s A", "default"));
                                    gVars.m_time = strval(StdClass.getValue(gVars.post, "post_date"));
                                    gVars.time = getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("G", true);
                                }

                                if (Math.abs(DateTime.time() - intval(gVars.time)) < floatval(86400)) {
                                    if (equal("future", StdClass.getValue(gVars.post, "post_status"))) {
                                        gVars.h_time = QStrings.sprintf(
                                                getIncluded(L10nPage.class, gVars, gConsts).__("%s from now", "default"),
                                                (((FormattingPage) PhpWeb.getIncluded(FormattingPage.class, gVars, gConsts))).human_time_diff(intval(gVars.time), intval("")));
                                    } else {
                                        gVars.h_time = QStrings.sprintf(
                                                getIncluded(L10nPage.class, gVars, gConsts).__("%s ago", "default"),
                                                (((FormattingPage) PhpWeb.getIncluded(FormattingPage.class, gVars, gConsts))).human_time_diff(intval(gVars.time), intval("")));
                                    }
                                } else {
                                    gVars.h_time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(getIncluded(L10nPage.class, gVars, gConsts).__("Y/m/d", "default"), gVars.m_time, true);
                                }
                            }

                            echo(gVars.webEnv, "\t\t<td><abbr title=\"");
                            echo(gVars.webEnv, gVars.t_time);
                            echo(gVars.webEnv, "\">");
                            echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_date_column_time", gVars.h_time, gVars.post, gVars.column_name));
                            echo(gVars.webEnv, "</abbr></td>\n\t\t");

                            break;
                        }

                        case 4: {
                            echo(gVars.webEnv, "\t\t<td><strong>");

                            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
                                echo(gVars.webEnv, "<a class=\"row-title\" href=\"post.php?action=edit&amp;post=");
                                getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                                echo(gVars.webEnv, "\" title=\"");
                                echo(gVars.webEnv,
                                    getIncluded(FormattingPage.class, gVars, gConsts)
                                        .attribute_escape(QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Edit \"%s\"", "default"), gVars.title)));
                                echo(gVars.webEnv, "\">");
                                echo(gVars.webEnv, gVars.title);
                                echo(gVars.webEnv, "</a>");
                            } else {
                                echo(gVars.webEnv, gVars.title);
                            }

                            echo(gVars.webEnv, "</strong>\n\t\t");

                            if (!empty(StdClass.getValue(gVars.post, "post_password"))) {
                                getIncluded(L10nPage.class, gVars, gConsts)._e(" &#8212; <strong>Protected</strong>", "default");
                            } else if (equal("private", StdClass.getValue(gVars.post, "post_status"))) {
                                getIncluded(L10nPage.class, gVars, gConsts)._e(" &#8212; <strong>Private</strong>", "default");
                            }

                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 5: {
                            echo(gVars.webEnv, "\t\t<td>");
                            gVars.categories = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(false));

                            if (!empty(gVars.categories)) {
                                out = new Array<String>();

                                for (Map.Entry javaEntry21 : gVars.categories.entrySet()) {
                                    StdClass c = (StdClass) javaEntry21.getValue();

                                    // Modified by Numiton
                                    out.putValue(
                                            "<a href=\'edit.php?category_name=" + StdClass.getValue(c, "slug") + "\'> " +
                                            getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field(
                                                        "name",
                                                        StdClass.getValue(c, "name"),
                                                        intval(StdClass.getValue(c, "term_id")),
                                                        "category",
                                                        "display")), strval(0)) + "</a>");
                                }

                                echo(gVars.webEnv, Strings.join(", ", out));
                            } else {
                                getIncluded(L10nPage.class, gVars, gConsts)._e("Uncategorized", "default");
                            }

                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 6: {
                            echo(gVars.webEnv, "\t\t<td>");
                            gVars.tags = (Array) getIncluded(Category_templatePage.class, gVars, gConsts).get_the_tags(0);

                            if (!empty(gVars.tags)) {
                                out = new Array<String>();

                                for (Map.Entry javaEntry22 : gVars.tags.entrySet()) {
                                    StdClass c = (StdClass) javaEntry22.getValue();
                                    out.putValue(
                                            "<a href=\'edit.php?tag=" + StdClass.getValue(c, "slug") + "\'> " +
                                            getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field(
                                                        "name",
                                                        StdClass.getValue(c, "name"),
                                                        intval(StdClass.getValue(c, "term_id")),
                                                        "post_tag",
                                                        "display")), strval(0)) + "</a>");
                                }

                                echo(gVars.webEnv, Strings.join(", ", out));
                            } else {
                                getIncluded(L10nPage.class, gVars, gConsts)._e("No Tags", "default");
                            }

                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 7: {
                            echo(gVars.webEnv, "\t\t<td class=\"num\"><div class=\"post-com-count-wrapper\">\n\t\t");

                            int left = (isset(comment_pending_count)
                                ? intval(((Array) comment_pending_count).getValue(StdClass.getValue(gVars.post, "ID")))
                                : 0);
                            gVars.pending_phrase = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%s pending", "default"), Strings.number_format(left));

                            if (booleanval(left)) {
                                echo(gVars.webEnv, "<strong>");
                            }

                            getIncluded(Comment_templatePage.class, gVars, gConsts).comments_number(
                                    "<a href=\'edit.php?p=" + strval(gVars.id) + "\' title=\'" + gVars.pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("0", "default") + "</span></a>",
                                    "<a href=\'edit.php?p=" + strval(gVars.id) + "\' title=\'" + gVars.pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("1", "default") + "</span></a>",
                                    "<a href=\'edit.php?p=" + strval(gVars.id) + "\' title=\'" + gVars.pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("%", "default") + "</span></a>",
                                    "");

                            if (booleanval(left)) {
                                echo(gVars.webEnv, "</strong>");
                            }

                            echo(gVars.webEnv, "\t\t</div></td>\n\t\t");

                            break;
                        }

                        case 8: {
                            echo(gVars.webEnv, "\t\t<td><a href=\"edit.php?author=");
                            getIncluded(Author_templatePage.class, gVars, gConsts).the_author_ID();
                            echo(gVars.webEnv, "\">");
                            getIncluded(Author_templatePage.class, gVars, gConsts).the_author("", true);
                            echo(gVars.webEnv, "</a></td>\n\t\t");

                            break;
                        }

                        case 9: {
                            echo(gVars.webEnv, "\t\t<td>\n\t\t<a href=\"");
                            getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
                            echo(gVars.webEnv, "\" title=\"");
                            echo(
                                gVars.webEnv,
                                getIncluded(FormattingPage.class, gVars, gConsts)
                                    .attribute_escape(QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View \"%s\"", "default"), gVars.title)));
                            echo(gVars.webEnv, "\" rel=\"permalink\">\n\t\t");

                            {
                                int javaSwitchSelector6 = 0;

                                if (equal(StdClass.getValue(gVars.post, "post_status"), "publish")) {
                                    javaSwitchSelector6 = 1;
                                }

                                if (equal(StdClass.getValue(gVars.post, "post_status"), "private")) {
                                    javaSwitchSelector6 = 2;
                                }

                                if (equal(StdClass.getValue(gVars.post, "post_status"), "future")) {
                                    javaSwitchSelector6 = 3;
                                }

                                if (equal(StdClass.getValue(gVars.post, "post_status"), "pending")) {
                                    javaSwitchSelector6 = 4;
                                }

                                if (equal(StdClass.getValue(gVars.post, "post_status"), "draft")) {
                                    javaSwitchSelector6 = 5;
                                }

                                switch (javaSwitchSelector6) {
                                case 1: {
                                }

                                case 2: {
                                    getIncluded(L10nPage.class, gVars, gConsts)._e("Published", "default");

                                    break;
                                }

                                case 3: {
                                    getIncluded(L10nPage.class, gVars, gConsts)._e("Scheduled", "default");

                                    break;
                                }

                                case 4: {
                                    getIncluded(L10nPage.class, gVars, gConsts)._e("Pending Review", "default");

                                    break;
                                }

                                case 5: {
                                    getIncluded(L10nPage.class, gVars, gConsts)._e("Unpublished", "default");

                                    break;
                                }
                                }
                            }

                            echo(gVars.webEnv, "\t\t</a>\n\t\t</td>\n\t\t");

                            break;
                        }

                        case 10: {
                            echo(gVars.webEnv, "\t\t<td><a href=\"");
                            getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
                            echo(gVars.webEnv, "\" rel=\"permalink\" class=\"view\">");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("View", "default");
                            echo(gVars.webEnv, "</a></td>\n\t\t");

                            break;
                        }

                        case 11: {
                            echo(gVars.webEnv, "\t\t<td>");

                            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
                                echo(
                                    gVars.webEnv,
                                    "<a href=\'post.php?action=edit&amp;post=" + strval(gVars.id) + "\' class=\'edit\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default") + "</a>");
                            }

                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 12: {
                            echo(gVars.webEnv, "\t\t<td>");

                            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_post", StdClass.getValue(gVars.post, "ID"))) {
                                echo(gVars.webEnv,
                                    "<a href=\'" +
                                    getIncluded(FunctionsPage.class, gVars, gConsts)
                                        .wp_nonce_url("post.php?action=delete&amp;post=" + strval(gVars.id), "delete-post_" + StdClass.getValue(gVars.post, "ID")) + "\' class=\'delete\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("Delete", "default") + "</a>");
                            }

                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        default: {
                            echo(gVars.webEnv, "\t\t<td>");
                            getIncluded(PluginPage.class, gVars, gConsts).do_action("manage_posts_custom_column", gVars.column_name, gVars.id);
                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }
                        }
                    }
                }

                echo(gVars.webEnv, "\t</tr>\n");
            }
        } else {
            echo(gVars.webEnv, "  <tr style=\'background-color: ");
            echo(gVars.webEnv, gVars.bgcolor);
            echo(gVars.webEnv, "\'>\n    <td colspan=\"8\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No posts found.", "default");
            echo(gVars.webEnv, "</td>\n  </tr>\n");
        } // end if ( have_posts() )

        return DEFAULT_VAL;
    }
}
