/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Feed_atom_commentsPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import com.numiton.Network;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Feed_atom_commentsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Feed_atom_commentsPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/feed-atom-comments.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed_atom_comments";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block1");
        gVars.webEnv = webEnv;
        
        /**
         * Atom Feed Template for displaying Atom Comments feed.
         *
         * @package WordPress
         */
        Network.header(
            gVars.webEnv,
            "Content-Type: application/atom+xml; charset=" + (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("blog_charset"),
            true);
        echo(
            gVars.webEnv,
            "<?xml version=\"1.0\" encoding=\"" + (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("blog_charset") + "\" ?" + ">");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block2");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("rss_language"));

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block3");
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("atom_ns");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block4");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Comments on: %s"), getIncluded(FeedPage.class, gVars, gConsts).get_the_title_rss());
        } else if (getIncluded(QueryPage.class, gVars, gConsts).is_search()) {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Comments for %1$s searching on %2$s"),
                getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("name"),
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(General_templatePage.class, gVars, gConsts).get_search_query()));
        } else {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("Comments for %s"),
                getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("name") + getIncluded(FeedPage.class, gVars, gConsts).get_wp_title_rss());
        }

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block5");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block6");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", getIncluded(CommentPage.class, gVars, gConsts).get_lastcommentmodified("GMT")));

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block7");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("atom");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block8");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            echo(gVars.webEnv, "\t<link rel=\"alternate\" type=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("html_type");
            echo(gVars.webEnv, "\" href=\"");
            echo(gVars.webEnv, getIncluded(Comment_templatePage.class, gVars, gConsts).get_comments_link());
            echo(gVars.webEnv, "\" />\n\t<link rel=\"self\" type=\"application/atom+xml\" href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_post_comments_feed_link(intval(""), "atom"));
            echo(gVars.webEnv, "\" />\n\t<id>");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_post_comments_feed_link(intval(""), "atom"));
            echo(gVars.webEnv, "</id>\n");
        } else if (getIncluded(QueryPage.class, gVars, gConsts).is_search()) {
            echo(gVars.webEnv, "\t<link rel=\"alternate\" type=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("html_type");
            echo(gVars.webEnv, "\" href=\"");
            echo(
                    gVars.webEnv,
                    (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("home") + "?s=" +
                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(General_templatePage.class, gVars, gConsts).get_search_query()));
            echo(gVars.webEnv, "\" />\n\t<link rel=\"self\" type=\"application/atom+xml\" href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_search_comments_feed_link("", "atom"));
            echo(gVars.webEnv, "\" />\n\t<id>");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_search_comments_feed_link("", "atom"));
            echo(gVars.webEnv, "</id>\n");
        } else {
            echo(gVars.webEnv, "\t<link rel=\"alternate\" type=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("html_type");
            echo(gVars.webEnv, "\" href=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("home");
            echo(gVars.webEnv, "\" />\n\t<link rel=\"self\" type=\"application/atom+xml\" href=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("comments_atom_url");
            echo(gVars.webEnv, "\" />\n\t<id>");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("comments_atom_url");
            echo(gVars.webEnv, "</id>\n");
        }

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_comments_block9");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_comments()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_comments())/*
             * post pass post pass
             */
             {
                getIncluded(QueryPage.class, gVars, gConsts).the_comment();
                gVars.comment_post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(intval(StdClass.getValue(gVars.comment, "comment_post_ID")));
                getIncluded(PostPage.class, gVars, gConsts).get_post_custom(intval(StdClass.getValue(gVars.comment_post, "ID")));
                echo(gVars.webEnv, "\t<entry>\n\t\t<title>");

                if (!getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
                    gVars.title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.comment_post, "ID")));
                    gVars.title = strval((((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters("the_title_rss", gVars.title));
                    QStrings.printf(
                        gVars.webEnv,
                        getIncluded(L10nPage.class, gVars, gConsts).__("Comment on %1$s by %2$s"),
                        gVars.title,
                        getIncluded(FeedPage.class, gVars, gConsts).get_comment_author_rss());
                } else {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("By: %s"), getIncluded(FeedPage.class, gVars, gConsts).get_comment_author_rss());
                }

                echo(gVars.webEnv, "</title>\n\t\t<link rel=\"alternate\" href=\"");
                getIncluded(FeedPage.class, gVars, gConsts).comment_link();
                echo(gVars.webEnv, "\" type=\"");
                getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("html_type");
                echo(gVars.webEnv, "\" />\n\n\t\t<author>\n\t\t\t<name>");
                getIncluded(FeedPage.class, gVars, gConsts).comment_author_rss();
                echo(gVars.webEnv, "</name>\n\t\t\t");

                if (booleanval(getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_author_url())) {
                    echo(gVars.webEnv, "<uri>" + getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_author_url() + "</uri>");
                }

                echo(gVars.webEnv, "\n\t\t</author>\n\n\t\t<id>");
                getIncluded(FeedPage.class, gVars, gConsts).comment_link();
                echo(gVars.webEnv, "</id>\n\t\t<updated>");
                echo(
                    gVars.webEnv,
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_time("Y-m-d H:i:s", true), false));
                echo(gVars.webEnv, "</updated>\n\t\t<published>");
                echo(
                    gVars.webEnv,
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_time("Y-m-d H:i:s", true), false));
                echo(gVars.webEnv, "</published>\n");

                if (!empty(StdClass.getValue(gVars.comment_post, "post_password")) && !equal(gVars.webEnv._COOKIE.getValue("wp-postpass"), StdClass.getValue(gVars.comment_post, "post_password"))) {
                    echo(gVars.webEnv, "\t\t<content type=\"html\" xml:base=\"");
                    getIncluded(FeedPage.class, gVars, gConsts).comment_link();
                    echo(gVars.webEnv, "\"><![CDATA[");
                    echo(gVars.webEnv, getIncluded(Post_templatePage.class, gVars, gConsts).get_the_password_form());
                    echo(gVars.webEnv, "]]></content>\n");
                } else { // post pass
                    echo(gVars.webEnv, "\t\t<content type=\"html\" xml:base=\"");
                    getIncluded(FeedPage.class, gVars, gConsts).comment_link();
                    echo(gVars.webEnv, "\"><![CDATA[");
                    getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
                    echo(gVars.webEnv, "]]></content>\n");
                } // post pass

                getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_atom_entry", intval(StdClass.getValue(gVars.comment, "comment_ID")), StdClass.getValue(gVars.comment_post, "ID"));
                echo(gVars.webEnv, "\t</entry>\n");
            }
        } else {
        }

        return DEFAULT_VAL;
    }
}
