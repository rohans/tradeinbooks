/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Feed_rss2Page.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Feed_rss2Page extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Feed_rss2Page.class.getName());

    @Override
    @RequestMapping("/wp-includes/feed-rss2.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed_rss2";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        webEnv.getNonNullRequest().setAttribute("RENDER_AS_XML", "true");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block1");
        gVars.webEnv = webEnv;
        
        /**
         * RSS2 Feed Template for displaying RSS2 Posts feed.
         *
         * @package WordPress
         */

        Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"), true);
        gVars.more = 1;
        echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\"?" + ">");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block2");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rss2_ns", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block3");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("name");
        getIncluded(FeedPage.class, gVars, gConsts).wp_title_rss("&#187;");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block4");
        getIncluded(FeedPage.class, gVars, gConsts).self_link();

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block5");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block6");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block7");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("D, d M Y H:i:s +0000", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), false));

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block8");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("rss2");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block9");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_language"));

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block10");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rss2_head", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss2_block11");

        while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            getIncluded(QueryPage.class, gVars, gConsts).the_post();
            echo(gVars.webEnv, "\t<item>\n\t\t<title>");
            getIncluded(FeedPage.class, gVars, gConsts).the_title_rss();
            echo(gVars.webEnv, "</title>\n\t\t<link>");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "</link>\n\t\t<comments>");
            getIncluded(Comment_templatePage.class, gVars, gConsts).comments_link("", "");
            echo(gVars.webEnv, "</comments>\n\t\t<pubDate>");
            echo(
                gVars.webEnv,
                getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("D, d M Y H:i:s +0000", getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("Y-m-d H:i:s", true), false));
            echo(gVars.webEnv, "</pubDate>\n\t\t<dc:creator>");
            getIncluded(Author_templatePage.class, gVars, gConsts).the_author("", true);
            echo(gVars.webEnv, "</dc:creator>\n\t\t");
            getIncluded(FeedPage.class, gVars, gConsts).the_category_rss("rss");
            echo(gVars.webEnv, "\n\t\t<guid isPermaLink=\"false\">");
            getIncluded(Post_templatePage.class, gVars, gConsts).the_guid(0);
            echo(gVars.webEnv, "</guid>\n");

            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_use_excerpt"))) {
                echo(gVars.webEnv, "\t\t<description><![CDATA[");
                getIncluded(FeedPage.class, gVars, gConsts).the_excerpt_rss();
                echo(gVars.webEnv, "]]></description>\n");
            } else {
                echo(gVars.webEnv, "\t\t<description><![CDATA[");
                getIncluded(FeedPage.class, gVars, gConsts).the_excerpt_rss();
                echo(gVars.webEnv, "]]></description>\n\t");

                if (Strings.strlen(strval(StdClass.getValue(gVars.post, "post_content"))) > 0) {
                    echo(gVars.webEnv, "\t\t<content:encoded><![CDATA[");
                    getIncluded(Post_templatePage.class, gVars, gConsts).the_content("(more...)", 0, "");
                    echo(gVars.webEnv, "]]></content:encoded>\n\t");
                } else {
                    echo(gVars.webEnv, "\t\t<content:encoded><![CDATA[");
                    getIncluded(FeedPage.class, gVars, gConsts).the_excerpt_rss();
                    echo(gVars.webEnv, "]]></content:encoded>\n\t");
                }
            }

            echo(gVars.webEnv, "\t\t<wfw:commentRss>");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_post_comments_feed_link(intval(""), ""));
            echo(gVars.webEnv, "</wfw:commentRss>\n");
            getIncluded(FeedPage.class, gVars, gConsts).rss_enclosure();
            echo(gVars.webEnv, "\t");
            getIncluded(PluginPage.class, gVars, gConsts).do_action("rss2_item", "");
            echo(gVars.webEnv, "\t</item>\n\t");
        }

        return DEFAULT_VAL;
    }
}
