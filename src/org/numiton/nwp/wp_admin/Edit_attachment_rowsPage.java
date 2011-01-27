/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_attachment_rowsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_attachment_rowsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_attachment_rowsPage.class.getName());
    public String att_title;
    public int t_diff;
    public String parent_title;

    @Override
    @RequestMapping("/wp-admin/edit-attachment-rows.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_attachment_rows";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_attachment_rows_block1");
        gVars.webEnv = webEnv;

        if (!gConsts.isABSPATHDefined()) {
            System.exit();
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_attachment_rows_block2");
        gVars.posts_columns = getIncluded(TemplatePage.class, gVars, gConsts).wp_manage_media_columns();

        for (Map.Entry javaEntry5 : gVars.posts_columns.entrySet()) {
            gVars.post_column_key = javaEntry5.getKey();
            gVars.column_display_name = javaEntry5.getValue();

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
        super.startBlock("__wp_admin_edit_attachment_rows_block3");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            gVars.bgcolor = "";
            getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_title", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_specialchars"), 10, 1);

            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                gVars._class = (equal("alternate", gVars._class)
                    ? ""
                    : "alternate");
                gVars.post_owner = (equal(gVars.current_user.getID(), StdClass.getValue(gVars.post, "post_author"))
                    ? "self"
                    : "other");
                att_title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0);

                if (empty(att_title)) {
                    att_title = getIncluded(L10nPage.class, gVars, gConsts).__("(no title)", "default");
                }

                echo(gVars.webEnv, "\t<tr id=\'post-");
                echo(gVars.webEnv, gVars.id);
                echo(gVars.webEnv, "\' class=\'");
                echo(gVars.webEnv, Strings.trim(gVars._class + " author-" + gVars.post_owner + " status-" + StdClass.getValue(gVars.post, "post_status")));
                echo(gVars.webEnv, "\' valign=\"top\">\n\n");

                for (Map.Entry javaEntry6 : gVars.posts_columns.entrySet()) {
                    gVars.column_name = javaEntry6.getKey();
                    gVars.column_display_name = javaEntry6.getValue();

                    {
                        int javaSwitchSelector4 = 0;

                        if (equal(gVars.column_name, "cb")) {
                            javaSwitchSelector4 = 1;
                        }

                        if (equal(gVars.column_name, "icon")) {
                            javaSwitchSelector4 = 2;
                        }

                        if (equal(gVars.column_name, "media")) {
                            javaSwitchSelector4 = 3;
                        }

                        if (equal(gVars.column_name, "desc")) {
                            javaSwitchSelector4 = 4;
                        }

                        if (equal(gVars.column_name, "date")) {
                            javaSwitchSelector4 = 5;
                        }

                        if (equal(gVars.column_name, "parent")) {
                            javaSwitchSelector4 = 6;
                        }

                        if (equal(gVars.column_name, "comments")) {
                            javaSwitchSelector4 = 7;
                        }

                        if (equal(gVars.column_name, "location")) {
                            javaSwitchSelector4 = 8;
                        }

                        switch (javaSwitchSelector4) {
                        case 1: {
                            echo(gVars.webEnv, "\t\t<th scope=\"row\" class=\"check-column\"><input type=\"checkbox\" name=\"delete[]\" value=\"");
                            getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                            echo(gVars.webEnv, "\" /></th>\n\t\t");

                            break;
                        }

                        case 2:/*
                         * TODO TODO
                         */
                         {
                            echo(gVars.webEnv, "\t\t<td class=\"media-icon\">");
                            echo(
                                gVars.webEnv,
                                getIncluded(Post_templatePage.class, gVars, gConsts).wp_get_attachment_link(intval(StdClass.getValue(gVars.post, "ID")),
                                    new Array<Object>(new ArrayEntry<Object>(80), new ArrayEntry<Object>(60)), false, true));
                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 3: {
                            echo(gVars.webEnv, "\t\t<td><strong><a href=\"media.php?action=edit&amp;attachment_id=");
                            getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                            echo(gVars.webEnv, "\" title=\"");
                            echo(
                                gVars.webEnv,
                                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Edit \"%s\"", "default"), att_title)));
                            echo(gVars.webEnv, "\">");
                            echo(gVars.webEnv, att_title);
                            echo(gVars.webEnv, "</a></strong><br />\n\t\t");
                            echo(gVars.webEnv,
                                Strings.strtoupper(
                                    QRegExPerl.preg_replace("/^.*?\\.(\\w+)$/", "$1",
                                        strval(getIncluded(PostPage.class, gVars, gConsts).get_attached_file(intval(StdClass.getValue(gVars.post, "ID")), false)))));
                            echo(gVars.webEnv, "\t\t");
                            getIncluded(PluginPage.class, gVars, gConsts).do_action("manage_media_media_column", StdClass.getValue(gVars.post, "ID"));
                            echo(gVars.webEnv, "\t\t</td>\n\t\t");

                            break;
                        }

                        case 4: {
                            echo(gVars.webEnv, "\t\t<td>");
                            echo(gVars.webEnv, getIncluded(Post_templatePage.class, gVars, gConsts).has_excerpt(0)
                                ? StdClass.getValue(gVars.post, "post_excerpt")
                                : "");
                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 5: {
                            if (equal("0000-00-00 00:00:00", StdClass.getValue(gVars.post, "post_date")) && equal("date", gVars.column_name)) {
                                gVars.t_time = gVars.h_time = getIncluded(L10nPage.class, gVars, gConsts).__("Unpublished", "default");
                            } else {
                                gVars.t_time = getIncluded(General_templatePage.class, gVars, gConsts).get_the_time(getIncluded(L10nPage.class, gVars, gConsts).__("Y/m/d g:i:s A", "default"));
                                gVars.m_time = strval(StdClass.getValue(gVars.post, "post_date"));
                                gVars.time = getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("G", true);

                                if (Math.abs(t_diff = DateTime.time() - intval(gVars.time)) < floatval(86400)) {
                                    if (t_diff < 0) {
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

                            echo(gVars.webEnv, "\t\t<td>");
                            echo(gVars.webEnv, gVars.h_time);
                            echo(gVars.webEnv, "</td>\n\t\t");

                            break;
                        }

                        case 6:/*
                         * override below override below
                         */
                         {
                            gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("(no title)", "default");

                            if (intval(StdClass.getValue(gVars.post, "post_parent")) > 0) {
                                if (booleanval(getIncluded(PostPage.class, gVars, gConsts).get_post(StdClass.getValue(gVars.post, "post_parent"), gConsts.getOBJECT(), "raw"))) {
                                    parent_title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.post, "post_parent")));

                                    if (!empty(parent_title)) {
                                        gVars.title = parent_title;
                                    }
                                }

                                echo(gVars.webEnv, "\t\t\t<td><strong><a href=\"post.php?action=edit&amp;post=");
                                echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_parent"));
                                echo(gVars.webEnv, "\">");
                                echo(gVars.webEnv, gVars.title);
                                echo(gVars.webEnv, "</a></strong></td>\n\t\t\t");
                            } else {
                                echo(gVars.webEnv, "\t\t\t<td>&nbsp;</td>\n\t\t\t");
                            }

                            break;
                        }

                        case 7: {
                            echo(gVars.webEnv, "\t\t<td class=\"num\"><div class=\"post-com-count-wrapper\">\n\t\t");

                            int left = intval(getIncluded(CommentPage.class, gVars, gConsts).get_pending_comments_num(StdClass.getValue(gVars.post, "ID")));
                            gVars.pending_phrase = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%s pending", "default"), Strings.number_format(left));

                            if (booleanval(left)) {
                                echo(gVars.webEnv, "<strong>");
                            }

                            getIncluded(Comment_templatePage.class, gVars, gConsts).comments_number(
                                    "<a href=\'upload.php?attachment_id=" + strval(gVars.id) + "\' title=\'" + gVars.pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("0", "default") + "</span></a>",
                                    "<a href=\'upload.php?attachment_id=" + strval(gVars.id) + "\' title=\'" + gVars.pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("1", "default") + "</span></a>",
                                    "<a href=\'upload.php?attachment_id=" + strval(gVars.id) + "\' title=\'" + gVars.pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("%", "default") + "</span></a>",
                                    "");

                            if (booleanval(left)) {
                                echo(gVars.webEnv, "</strong>");
                            }

                            echo(gVars.webEnv, "\t\t</div></td>\n\t\t");

                            break;
                        }

                        case 8: {
                            echo(gVars.webEnv, "\t\t<td><a href=\"");
                            getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
                            echo(gVars.webEnv, "\">");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("Permalink", "default");
                            echo(gVars.webEnv, "</a></td>\n\t\t");

                            break;
                        }

                        default: {
                            echo(gVars.webEnv, "\t\t<td>");
                            getIncluded(PluginPage.class, gVars, gConsts).do_action("manage_media_custom_column", gVars.column_name, gVars.id);
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
