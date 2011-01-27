/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Feed_rssPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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


@Controller
@Scope("request")
public class Feed_rssPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Feed_rssPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/feed-rss.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed_rss";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block1");
        gVars.webEnv = webEnv;
        
        /**
         * RSS 0.92 Feed Template for displaying RSS 0.92 Posts feed.
         *
         * @package WordPress
         */

        Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"), true);
        gVars.more = 1;
        echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\"?" + ">");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("comment");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block2");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("name");
        getIncluded(FeedPage.class, gVars, gConsts).wp_title_rss("&#187;");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block3");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block4");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block5");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("D, d M Y H:i:s +0000", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), false));

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block6");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_language"));

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block7");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rss_head", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rss_block8");

        while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            getIncluded(QueryPage.class, gVars, gConsts).the_post();
            echo(gVars.webEnv, "\t<item>\n\t\t<title>");
            getIncluded(FeedPage.class, gVars, gConsts).the_title_rss();
            echo(gVars.webEnv, "</title>\n");

            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_use_excerpt"))) {
                echo(gVars.webEnv, "\t\t<description><![CDATA[");
                getIncluded(FeedPage.class, gVars, gConsts).the_excerpt_rss();
                echo(gVars.webEnv, "]]></description>\n");
            } else { // use content
                echo(gVars.webEnv, "\t\t<description>");
                getIncluded(FeedPage.class, gVars, gConsts).the_content_rss("", 0, "", intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_excerpt_length")), 0);
                echo(gVars.webEnv, "</description>\n");
            }

            echo(gVars.webEnv, "\t\t<link>");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "</link>\n\t\t");
            getIncluded(PluginPage.class, gVars, gConsts).do_action("rss_item", "");
            echo(gVars.webEnv, "\t</item>\n");
        }

        return DEFAULT_VAL;
    }
}
