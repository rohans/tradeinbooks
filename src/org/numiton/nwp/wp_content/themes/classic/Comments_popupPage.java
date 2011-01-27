/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Comments_popupPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
import org.numiton.nwp.Wp_settingsPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller(value = "wp_content/themes/classic/Comments_popupPage")
@Scope("request")
public class Comments_popupPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Comments_popupPage.class.getName());
    public StdClass commentstatus;

    @Override
    @RequestMapping("/wp-content/themes/classic/comments-popup.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/comments_popup";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_popup_block1");
        gVars.webEnv = webEnv;
        
        /* Don't remove these lines. */
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_text", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "popuplinks"));

        while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            getIncluded(QueryPage.class, gVars, gConsts).the_post();
            echo(
                    gVars.webEnv,
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n     <title>");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("blogname"));
            echo(gVars.webEnv, " - ");
            echo(gVars.webEnv, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Comments on %s"), getIncluded(Post_templatePage.class, gVars, gConsts).the_title("", "", false)));
            echo(gVars.webEnv, "</title>\n\n\t<meta http-equiv=\"Content-Type\" content=\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");
            echo(gVars.webEnv, "; charset=");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("blog_charset"));
            echo(gVars.webEnv, "\" />\n\t<style type=\"text/css\" media=\"screen\">\n\t\t@import url( ");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("stylesheet_url");
            echo(gVars.webEnv, " );\n\t\tbody { margin: 3px; }\n\t</style>\n\n</head>\n<body id=\"commentspopup\">\n\n<h1 id=\"header\"><a href=\"\" title=\"");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("blogname"));
            echo(gVars.webEnv, "\">");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("blogname"));
            echo(gVars.webEnv, "</a></h1>\n\n<h2 id=\"comments\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Comments");
            echo(gVars.webEnv, "</h2>\n\n<p><a href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_post_comments_feed_link(intval(StdClass.getValue(gVars.post, "ID"))));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("<abbr title=\"Really Simple Syndication\">RSS</abbr> feed for comments on this post.");
            echo(gVars.webEnv, "</a></p>\n\n");

            if (equal("open", StdClass.getValue(gVars.post, "ping_status"))) {
                echo(gVars.webEnv, "<p>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("The <abbr title=\"Universal Resource Locator\">URL</abbr> to TrackBack this entry is:");
                echo(gVars.webEnv, " <em>");
                getIncluded(Comment_templatePage.class, gVars, gConsts).trackback_url();
                echo(gVars.webEnv, "</em></p>\n");
            }

            echo(gVars.webEnv, "\n");
            // this line is WordPress' motor, do not delete it.
            gVars.commenter = getIncluded(CommentPage.class, gVars, gConsts).wp_get_current_commenter();

            /* Modified by Numiton */
            gVars.comment_author = strval(Array.extractVar((Array) gVars.commenter, "comment_author", gVars.comment_author, Array.EXTR_OVERWRITE));
            gVars.comment_author_email = strval(Array.extractVar((Array) gVars.commenter, "comment_author_email", gVars.comment_author_email, Array.EXTR_OVERWRITE));
            gVars.comment_author_url = strval(Array.extractVar((Array) gVars.commenter, "comment_author_url", gVars.comment_author_url, Array.EXTR_OVERWRITE));
            gVars.comments = getIncluded(CommentPage.class, gVars, gConsts).get_approved_comments(gVars.id);
            commentstatus = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(gVars.id);

            if (!empty(StdClass.getValue(commentstatus, "post_password")) &&
                    !equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(commentstatus, "post_password"))) {  // and it doesn't match the cookie
                echo(gVars.webEnv, getIncluded(Post_templatePage.class, gVars, gConsts).get_the_password_form());
            } else {
                echo(gVars.webEnv, "\n");

                if (booleanval(gVars.comments)) {
                    echo(gVars.webEnv, "<ol id=\"commentlist\">\n");

                    for (Map.Entry javaEntry359 : gVars.comments.entrySet()) {
                        gVars.comment = (StdClass) javaEntry359.getValue();
                        echo(gVars.webEnv, "\t<li id=\"comment-");
                        getIncluded(Comment_templatePage.class, gVars, gConsts).comment_ID();
                        echo(gVars.webEnv, "\">\n\t");
                        getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
                        echo(gVars.webEnv, "\t<p><cite>");
                        getIncluded(Comment_templatePage.class, gVars, gConsts).comment_type(
                            getIncluded(L10nPage.class, gVars, gConsts).__("Comment"),
                            getIncluded(L10nPage.class, gVars, gConsts).__("Trackback"),
                            getIncluded(L10nPage.class, gVars, gConsts).__("Pingback"));
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
                        echo(gVars.webEnv, "</a></cite></p>\n\t</li>\n\n");
                    } // end for each comment

                    echo(gVars.webEnv, "</ol>\n");
                } else {// this is displayed if there are no comments so far ?>
                    echo(gVars.webEnv, "\t<p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("No comments yet.");
                    echo(gVars.webEnv, "</p>\n");
                }

                echo(gVars.webEnv, "\n");

                if (equal("open", StdClass.getValue(commentstatus, "comment_status"))) {
                    echo(gVars.webEnv, "<h2>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Leave a comment");
                    echo(gVars.webEnv, "</h2>\n<p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e(
                            "Line and paragraph breaks automatic, e-mail address never displayed, <acronym title=\"Hypertext Markup Language\">HTML</acronym> allowed:");
                    echo(gVars.webEnv, " <code>");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).allowed_tags());
                    echo(gVars.webEnv, "</code></p>\n\n<form action=\"");
                    echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl"));
                    echo(gVars.webEnv, "/wp-comments-post.php\" method=\"post\" id=\"commentform\">\n");

                    if (booleanval(gVars.user_ID)) {
                        echo(gVars.webEnv, "<p>");
                        QStrings.printf(
                                gVars.webEnv,
                                getIncluded(L10nPage.class, gVars, gConsts).__("Logged in as %s."),
                                "<a href=\"" + (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl") +
                                "/wp-admin/profile.php\">" + strval(gVars.user_identity) + "</a>");
                        echo(gVars.webEnv, " <a href=\"");
                        echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("siteurl"));
                        echo(gVars.webEnv, "/wp-login.php?action=logout\" title=\"");
                        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Log out of this account")));
                        echo(gVars.webEnv, "\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("Log out &raquo;");
                        echo(gVars.webEnv, "</a></p>\n");
                    } else {
                        echo(gVars.webEnv, "\t<p>\n\t  <input type=\"text\" name=\"author\" id=\"author\" class=\"textarea\" value=\"");
                        echo(gVars.webEnv, gVars.comment_author);
                        echo(gVars.webEnv, "\" size=\"28\" tabindex=\"1\" />\n\t   <label for=\"author\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("Name");
                        echo(gVars.webEnv, "</label>\n\t<input type=\"hidden\" name=\"comment_post_ID\" value=\"");
                        echo(gVars.webEnv, gVars.id);
                        echo(gVars.webEnv, "\" />\n\t<input type=\"hidden\" name=\"redirect_to\" value=\"");
                        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.webEnv.getRequestURI()));
                        echo(gVars.webEnv, "\" />\n\t</p>\n\n\t<p>\n\t  <input type=\"text\" name=\"email\" id=\"email\" value=\"");
                        echo(gVars.webEnv, gVars.comment_author_email);
                        echo(gVars.webEnv, "\" size=\"28\" tabindex=\"2\" />\n\t   <label for=\"email\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail");
                        echo(gVars.webEnv, "</label>\n\t</p>\n\n\t<p>\n\t  <input type=\"text\" name=\"url\" id=\"url\" value=\"");
                        echo(gVars.webEnv, gVars.comment_author_url);
                        echo(gVars.webEnv, "\" size=\"28\" tabindex=\"3\" />\n\t   <label for=\"url\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("<abbr title=\"Universal Resource Locator\">URL</abbr>");
                        echo(gVars.webEnv, "</label>\n\t</p>\n");
                    }

                    echo(gVars.webEnv, "\n\t<p>\n\t  <label for=\"comment\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Your Comment");
                    echo(
                            gVars.webEnv,
                            "</label>\n\t<br />\n\t  <textarea name=\"comment\" id=\"comment\" cols=\"70\" rows=\"4\" tabindex=\"4\"></textarea>\n\t</p>\n\n\t<p>\n\t  <input name=\"submit\" type=\"submit\" tabindex=\"5\" value=\"");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Say It!");
                    echo(gVars.webEnv, "\" />\n\t</p>\n\t");
                    getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_form", StdClass.getValue(gVars.post, "ID"));
                    echo(gVars.webEnv, "</form>\n");
                } else { // comments are closed
                    echo(gVars.webEnv, "<p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Sorry, the comment form is closed at this time.");
                    echo(gVars.webEnv, "</p>\n");
                }
            } // end password check

            echo(gVars.webEnv, "\n<div><strong><a href=\"javascript:window.close()\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Close this window.");
            echo(gVars.webEnv, "</a></strong></div>\n\n");
        } // if you delete this the sky will fall on your head

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_popup_block2");
        getIncluded(Wp_settingsPage.class, gVars, gConsts).timer_stop(1);

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_comments_popup_block3");
        echo(gVars.webEnv,
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("<cite>Powered by <a href=\"http://wordpress.org\" title=\"%s\"><strong>WordPress</strong></a></cite>"),
                getIncluded(L10nPage.class, gVars, gConsts).__("Powered by WordPress, state-of-the-art semantic personal publishing platform.")));
        // Seen at http://www.mijnkopthee.nl/log2/archive/2003/05/28/esc(18)

        return DEFAULT_VAL;
    }
}
