/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CommentsPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_content.themes.classic;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.URL;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller(value = "wp_content/themes/classic/CommentsPage")
@Scope("request")
public class CommentsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CommentsPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/classic/comments.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/comments";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_block1");
        gVars.webEnv = webEnv;

        if (!empty(StdClass.getValue(gVars.post, "post_password")) && !equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(gVars.post, "post_password"))) {
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Enter your password to view comments.");
            echo(gVars.webEnv, "</p>\n");

            return null;
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_block2");
        getIncluded(Comment_templatePage.class, gVars, gConsts).comments_number(getIncluded(L10nPage.class, gVars, gConsts).__("No Comments"),
            getIncluded(L10nPage.class, gVars, gConsts).__("1 Comment"), getIncluded(L10nPage.class, gVars, gConsts).__("% Comments"));

        if (getIncluded(Comment_templatePage.class, gVars, gConsts).comments_open()) {
            echo(gVars.webEnv, "\t<a href=\"#postcomment\" title=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Leave a comment");
            echo(gVars.webEnv, "\">&raquo;</a>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_block3");

        if (booleanval(gVars.comments)) {
            echo(gVars.webEnv, "<ol id=\"commentlist\">\n\n");

            for (Map.Entry javaEntry360 : gVars.comments.entrySet()) {
                gVars.comment = (StdClass) javaEntry360.getValue();
                echo(gVars.webEnv, "\t<li id=\"comment-");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_ID();
                echo(gVars.webEnv, "\">\n\t");
                echo(gVars.webEnv, getIncluded(PluggablePage.class, gVars, gConsts).get_avatar(gVars.comment, 32));
                echo(gVars.webEnv, "\t");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
                echo(gVars.webEnv, "\t<p><cite>");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_type(getIncluded(L10nPage.class, gVars, gConsts).__("Comment"),
                    getIncluded(L10nPage.class, gVars, gConsts).__("Trackback"), getIncluded(L10nPage.class, gVars, gConsts).__("Pingback"));
                echo(gVars.webEnv, " ");
                getIncluded(L10nPage.class, gVars, gConsts)._e("by");
                echo(gVars.webEnv, " ");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_link();
                echo(gVars.webEnv, " &#8212; ");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_date();
                echo(gVars.webEnv, " @ <a href=\"#comment-");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_ID();
                echo(gVars.webEnv, "\">");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comment_time();
                echo(gVars.webEnv, "</a></cite> ");
                getIncluded(Link_templatePage.class, gVars, gConsts).edit_comment_link(getIncluded(L10nPage.class, gVars, gConsts).__("Edit This"), " |");
                echo(gVars.webEnv, "</p>\n\t</li>\n\n");
            }

            echo(gVars.webEnv, "\n</ol>\n\n");
        } else { // If there are no comments yet ?>
            echo(gVars.webEnv, "\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No comments yet.");
            echo(gVars.webEnv, "</p>\n");
        }

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_block4");
        getIncluded(Link_templatePage.class, gVars, gConsts)
            .post_comments_feed_link(getIncluded(L10nPage.class, gVars, gConsts).__("<abbr title=\"Really Simple Syndication\">RSS</abbr> feed for comments on this post."));

        if (getIncluded(Comment_templatePage.class, gVars, gConsts).pings_open()) {
            echo(gVars.webEnv, "\t<a href=\"");
            getIncluded(Comment_templatePage.class, gVars, gConsts).trackback_url();
            echo(gVars.webEnv, "\" rel=\"trackback\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("TrackBack <abbr title=\"Universal Resource Locator\">URL</abbr>");
            echo(gVars.webEnv, "</a>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_block5");

        if (getIncluded(Comment_templatePage.class, gVars, gConsts).comments_open()) {
            echo(gVars.webEnv, "<h2 id=\"postcomment\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Leave a comment");
            echo(gVars.webEnv, "</h2>\n\n");

            if (booleanval((((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("comment_registration")) &&
                    !booleanval(gVars.user_ID)) {
                echo(gVars.webEnv, "<p>");
                QStrings.printf(
                        gVars.webEnv,
                        getIncluded(L10nPage.class, gVars, gConsts).__("You must be <a href=\"%s\">logged in</a> to post a comment."),
                        (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl") + "/wp-login.php?redirect_to=" +
                        URL.urlencode(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink()));
                echo(gVars.webEnv, "</p>\n");
            } else {
                echo(gVars.webEnv, "\n<form action=\"");
                echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl"));
                echo(gVars.webEnv, "/wp-comments-post.php\" method=\"post\" id=\"commentform\">\n\n");

                if (booleanval(gVars.user_ID)) {
                    echo(gVars.webEnv, "\n<p>");
                    QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts).__("Logged in as %s."),
                            "<a href=\"" + (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl") +
                            "/wp-admin/profile.php\">" + strval(gVars.user_identity) + "</a>");
                    echo(gVars.webEnv, " <a href=\"");
                    echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl"));
                    echo(gVars.webEnv, "/wp-login.php?action=logout\" title=\"");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Log out of this account");
                    echo(gVars.webEnv, "\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Log out &raquo;");
                    echo(gVars.webEnv, "</a></p>\n\n");
                } else {
                    echo(gVars.webEnv, "\n<p><input type=\"text\" name=\"author\" id=\"author\" value=\"");
                    echo(gVars.webEnv, gVars.comment_author);
                    echo(gVars.webEnv, "\" size=\"22\" tabindex=\"1\" />\n<label for=\"author\"><small>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Name");
                    echo(gVars.webEnv, " ");

                    if (booleanval(gVars.req)) {
                        getIncluded(L10nPage.class, gVars, gConsts)._e("(required)");
                    }

                    echo(gVars.webEnv, "</small></label></p>\n\n<p><input type=\"text\" name=\"email\" id=\"email\" value=\"");
                    echo(gVars.webEnv, gVars.comment_author_email);
                    echo(gVars.webEnv, "\" size=\"22\" tabindex=\"2\" />\n<label for=\"email\"><small>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Mail (will not be published)");
                    echo(gVars.webEnv, " ");

                    if (booleanval(gVars.req)) {
                        getIncluded(L10nPage.class, gVars, gConsts)._e("(required)");
                    }

                    echo(gVars.webEnv, "</small></label></p>\n\n<p><input type=\"text\" name=\"url\" id=\"url\" value=\"");
                    echo(gVars.webEnv, gVars.comment_author_url);
                    echo(gVars.webEnv, "\" size=\"22\" tabindex=\"3\" />\n<label for=\"url\"><small>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Website");
                    echo(gVars.webEnv, "</small></label></p>\n\n");
                }

                echo(gVars.webEnv, "\n<!--<p><small><strong>XHTML:</strong> ");
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("You can use these tags: %s"), getIncluded(General_templatePage.class, gVars, gConsts).allowed_tags());
                echo(
                        gVars.webEnv,
                        "</small></p>-->\n\n<p><textarea name=\"comment\" id=\"comment\" cols=\"100%\" rows=\"10\" tabindex=\"4\"></textarea></p>\n\n<p><input name=\"submit\" type=\"submit\" id=\"submit\" tabindex=\"5\" value=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Submit Comment")));
                echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"comment_post_ID\" value=\"");
                echo(gVars.webEnv, gVars.id);
                echo(gVars.webEnv, "\" />\n</p>\n");
                getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_form", StdClass.getValue(gVars.post, "ID"));
                echo(gVars.webEnv, "\n</form>\n\n");
            } // If registration required and not logged in

            echo(gVars.webEnv, "\n");
        } else {// Comments are closed 
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Sorry, the comment form is closed at this time.");
            echo(gVars.webEnv, "</p>\n");
        }

        return DEFAULT_VAL;
    }
}
