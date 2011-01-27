/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Feed_rss2_commentsPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_includes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Network;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Feed_rss2_commentsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Feed_rss2_commentsPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/feed-rss2-comments.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed_rss2_comments";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block1");
        gVars.webEnv = webEnv;
        
        /**
         * RSS2 Feed Template for displaying RSS2 Comments feed.
         *
         * @package WordPress
         */

        Network.header(gVars.webEnv, "Content-Type: text/xml;charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"), true);
        echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\"?" + ">");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block2");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Comments on: %s", "default"), getIncluded(FeedPage.class, gVars, gConsts).get_the_title_rss());
        } else if (getIncluded(QueryPage.class, gVars, gConsts).is_search()) {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Comments for %s searching on %s", "default"),
                getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("name"),
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.wp_query.query_vars.getValue("s"))));
        } else {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Comments for %s", "default"),
                getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("name") + getIncluded(FeedPage.class, gVars, gConsts).get_wp_title_rss("&#187;"));
        }

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block3");
        getIncluded(FeedPage.class, gVars, gConsts).self_link();

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block4");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
        } else {
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");
        }

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block5");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block6");
        echo(gVars.webEnv, DateTime.gmdate("r"));

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block7");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("rss2");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_comments_block8");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("commentsrss2_head", "");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_comments()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_comments())/*
             * post pass post pass
             */
             {
                getIncluded(QueryPage.class, gVars, gConsts).the_comment();
                gVars.comment_post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(intval(StdClass.getValue(gVars.comment, "comment_post_ID")), gConsts.getOBJECT(), "raw");
                getIncluded(PostPage.class, gVars, gConsts).get_post_custom(intval(StdClass.getValue(gVars.comment_post, "ID")));
                echo(gVars.webEnv, "\t<item>\n\t\t<title>");

                if (!getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
                    gVars.title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.comment_post, "ID")));
                    gVars.title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title_rss", gVars.title));
                    QStrings.printf(
                        gVars.webEnv,
                        getIncluded(L10nPage.class, gVars, gConsts).__("Comment on %1$s by %2$s", "default"),
                        gVars.title,
                        getIncluded(FeedPage.class, gVars, gConsts).get_comment_author_rss());
                } else {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("By: %s", "default"), getIncluded(FeedPage.class, gVars, gConsts).get_comment_author_rss());
                }

                echo(gVars.webEnv, "</title>\n\t\t<link>");
                getIncluded(FeedPage.class, gVars, gConsts).comment_link();
                echo(gVars.webEnv, "</link>\n\t\t<dc:creator>");
                echo(gVars.webEnv, getIncluded(FeedPage.class, gVars, gConsts).get_comment_author_rss());
                echo(gVars.webEnv, "</dc:creator>\n\t\t<pubDate>");
                echo(
                    gVars.webEnv,
                    getIncluded(FunctionsPage.class, gVars, gConsts)
                        .mysql2date("D, d M Y H:i:s +0000", getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_time("Y-m-d H:i:s", true), false));
                echo(gVars.webEnv, "</pubDate>\n\t\t<guid isPermaLink=\"false\">");
                getIncluded(FeedPage.class, gVars, gConsts).comment_guid();
                echo(gVars.webEnv, "</guid>\n");

                if (!empty(StdClass.getValue(gVars.comment_post, "post_password")) && !equal(gVars.webEnv._COOKIE.getValue("wp-postpass"), StdClass.getValue(gVars.comment_post, "post_password"))) {
                    echo(gVars.webEnv, "\t\t<description>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Protected Comments: Please enter your password to view comments.", "default");
                    echo(gVars.webEnv, "</description>\n\t\t<content:encoded><![CDATA[");
                    echo(gVars.webEnv, getIncluded(Post_templatePage.class, gVars, gConsts).get_the_password_form());
                    echo(gVars.webEnv, "]]></content:encoded>\n");
                } else { // post pass
                    echo(gVars.webEnv, "\t\t<description>");
                    getIncluded(FeedPage.class, gVars, gConsts).comment_text_rss();
                    echo(gVars.webEnv, "</description>\n\t\t<content:encoded><![CDATA[");
                    getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
                    echo(gVars.webEnv, "]]></content:encoded>\n");
                } // post pass

                getIncluded(PluginPage.class, gVars, gConsts).do_action("commentrss2_item", intval(StdClass.getValue(gVars.comment, "comment_ID")), StdClass.getValue(gVars.comment_post, "ID"));
                echo(gVars.webEnv, "\t</item>\n");
            }
        } else {
        }

        return DEFAULT_VAL;
    }
}
