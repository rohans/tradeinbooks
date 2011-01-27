/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CommentsPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_content.themes._default;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.URL;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;


@Controller
@Scope("request")
public class CommentsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CommentsPage.class.getName());
    public Object oddcomment;

    @Override
    @RequestMapping("/wp-content/themes/default/comments.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/comments";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes__default_comments_block1");
        gVars.webEnv = webEnv;

        // Do not delete these lines
        if (!empty(gVars.webEnv.getScriptFilename()) && equal("comments.php", FileSystemOrSocket.basename(gVars.webEnv.getScriptFilename()))) {
            System.exit("Please do not load this page directly. Thanks!");
        }

        if (!empty(StdClass.getValue(gVars.post, "post_password"))) { // if there's a password
            if (!equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(gVars.post, "post_password"))) {  // and it doesn't match the cookie
                echo(gVars.webEnv, "\n\t\t\t<p class=\"nocomments\">This post is password protected. Enter the password to view comments.</p>\n\n\t\t\t");

                return null;
            }
        }

        /* This variable is for alternating comment background */
        oddcomment = "class=\"alt\" ";

        /* Start of block */
        super.startBlock("__wp_content_themes__default_comments_block2");

        if (booleanval(gVars.comments)) {
            echo(gVars.webEnv, "\t<h3 id=\"comments\">");
            getIncluded(Comment_templatePage.class, gVars, gConsts).comments_number("No Responses", "One Response", "% Responses", "");
            echo(gVars.webEnv, " to &#8220;");
            getIncluded(Post_templatePage.class, gVars, gConsts).the_title("", "", true);
            echo(gVars.webEnv, "&#8221;</h3>\n\n\t<ol class=\"commentlist\">\n\n\t");

            for (Map.Entry javaEntry362 : gVars.comments.entrySet()) {
                gVars.comment = (StdClass) javaEntry362.getValue();
                echo(gVars.webEnv, "\n\t\t<li ");
                echo(gVars.webEnv, oddcomment);
                echo(gVars.webEnv, "id=\"comment-");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_ID();
                echo(gVars.webEnv, "\">\n\t\t\t");
                echo(gVars.webEnv, getIncluded(PluggablePage.class, gVars, gConsts).get_avatar(gVars.comment, 32, ""));
                echo(gVars.webEnv, "\t\t\t<cite>");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_link();
                echo(gVars.webEnv, "</cite> Says:\n\t\t\t");

                if (equal(StdClass.getValue(gVars.comment, "comment_approved"), "0")) {
                    echo(gVars.webEnv, "\t\t\t<em>Your comment is awaiting moderation.</em>\n\t\t\t");
                } else {
                }

                echo(gVars.webEnv, "\t\t\t<br />\n\n\t\t\t<small class=\"commentmetadata\"><a href=\"#comment-");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_ID();
                echo(gVars.webEnv, "\" title=\"\">");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_date("F jS, Y");
                echo(gVars.webEnv, " at ");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_time("");
                echo(gVars.webEnv, "</a> ");
                getIncluded(Link_templatePage.class, gVars, gConsts).edit_comment_link("edit", "&nbsp;&nbsp;", "");
                echo(gVars.webEnv, "</small>\n\n\t\t\t");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
                echo(gVars.webEnv, "\n\t\t</li>\n\n\t");
                
                /* Changes every other comment to a different class */
                oddcomment = (empty(oddcomment)
                    ? "class=\"alt\" "
                    : "");
                echo(gVars.webEnv, "\n\t");
            } /* end for each comment */

            echo(gVars.webEnv, "\n\t</ol>\n\n ");
        } else { // this is displayed if there are no comments so far 
            echo(gVars.webEnv, "\n\t");

            if (equal("open", StdClass.getValue(gVars.post, "comment_status"))) {
                echo(gVars.webEnv, "\t\t<!-- If comments are open, but there are no comments. -->\n\n\t ");
            } else { // comments are closed 
                echo(gVars.webEnv, "\t\t<!-- If comments are closed. -->\n\t\t<p class=\"nocomments\">Comments are closed.</p>\n\n\t");
            }
        }

        /* Start of block */
        super.startBlock("__wp_content_themes__default_comments_block3");

        if (equal("open", StdClass.getValue(gVars.post, "comment_status"))) {
            echo(gVars.webEnv, "\n<h3 id=\"respond\">Leave a Reply</h3>\n\n");

            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_registration")) && !booleanval(gVars.user_ID)) {
                echo(gVars.webEnv, "<p>You must be <a href=\"");
                echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
                echo(gVars.webEnv, "/wp-login.php?redirect_to=");
                echo(gVars.webEnv, URL.urlencode(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false)));
                echo(gVars.webEnv, "\">logged in</a> to post a comment.</p>\n");
            } else {
                echo(gVars.webEnv, "\n<form action=\"");
                echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
                echo(gVars.webEnv, "/wp-comments-post.php\" method=\"post\" id=\"commentform\">\n\n");

                if (booleanval(gVars.user_ID)) {
                    echo(gVars.webEnv, "\n<p>Logged in as <a href=\"");
                    echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
                    echo(gVars.webEnv, "/wp-admin/profile.php\">");
                    echo(gVars.webEnv, gVars.user_identity);
                    echo(gVars.webEnv, "</a>. <a href=\"");
                    echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
                    echo(gVars.webEnv, "/wp-login.php?action=logout\" title=\"Log out of this account\">Log out &raquo;</a></p>\n\n");
                } else {
                    echo(gVars.webEnv, "\n<p><input type=\"text\" name=\"author\" id=\"author\" value=\"");
                    echo(gVars.webEnv, gVars.comment_author);
                    echo(gVars.webEnv, "\" size=\"22\" tabindex=\"1\" />\n<label for=\"author\"><small>Name ");

                    if (booleanval(gVars.req)) {
                        echo(gVars.webEnv, "(required)");
                    }

                    echo(gVars.webEnv, "</small></label></p>\n\n<p><input type=\"text\" name=\"email\" id=\"email\" value=\"");
                    echo(gVars.webEnv, gVars.comment_author_email);
                    echo(gVars.webEnv, "\" size=\"22\" tabindex=\"2\" />\n<label for=\"email\"><small>Mail (will not be published) ");

                    if (booleanval(gVars.req)) {
                        echo(gVars.webEnv, "(required)");
                    }

                    echo(gVars.webEnv, "</small></label></p>\n\n<p><input type=\"text\" name=\"url\" id=\"url\" value=\"");
                    echo(gVars.webEnv, gVars.comment_author_url);
                    echo(gVars.webEnv, "\" size=\"22\" tabindex=\"3\" />\n<label for=\"url\"><small>Website</small></label></p>\n\n");
                }

                echo(gVars.webEnv, "\n<!--<p><small><strong>XHTML:</strong> You can use these tags: <code>");
                echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).allowed_tags());
                echo(
                        gVars.webEnv,
                        "</code></small></p>-->\n\n<p><textarea name=\"comment\" id=\"comment\" cols=\"100%\" rows=\"10\" tabindex=\"4\"></textarea></p>\n\n<p><input name=\"submit\" type=\"submit\" id=\"submit\" tabindex=\"5\" value=\"Submit Comment\" />\n<input type=\"hidden\" name=\"comment_post_ID\" value=\"");
                echo(gVars.webEnv, gVars.id);
                echo(gVars.webEnv, "\" />\n</p>\n");
                getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_form", StdClass.getValue(gVars.post, "ID"));
                echo(gVars.webEnv, "\n</form>\n\n");
            } // If registration required and not logged in 

            echo(gVars.webEnv, "\n");
        } else {
        } // if you delete this the sky will fall on your head 

        return DEFAULT_VAL;
    }
}
