/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Feed_rdfPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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


@Controller
@Scope("request")
public class Feed_rdfPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Feed_rdfPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/feed-rdf.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed_rdf";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block1");
        gVars.webEnv = webEnv;
        
        /**
         * RSS 1 RDF Feed Template for displaying RSS 1 Posts feed.
         *
         * @package WordPress
         */

        Network.header(gVars.webEnv, "Content-Type: application/rdf+xml; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"), true);
        gVars.more = 1;
        echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\"?" + ">");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block2");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rdf_ns", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block3");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block4");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("name");
        getIncluded(FeedPage.class, gVars, gConsts).wp_title_rss("&#187;");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block5");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block6");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block7");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), false));

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block8");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("rdf");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block9");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rdf_header", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block10");

        while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            getIncluded(QueryPage.class, gVars, gConsts).the_post();
            echo(gVars.webEnv, "\t\t\t<rdf:li rdf:resource=\"");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "\"/>\n\t\t");
        }

        /* Start of block */
        super.startBlock("__wp_includes_feed_rdf_block11");
        getIncluded(QueryPage.class, gVars, gConsts).rewind_posts();

        while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            getIncluded(QueryPage.class, gVars, gConsts).the_post();
            echo(gVars.webEnv, "<item rdf:about=\"");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "\">\n\t<title>");
            getIncluded(FeedPage.class, gVars, gConsts).the_title_rss();
            echo(gVars.webEnv, "</title>\n\t<link>");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "</link>\n\t <dc:date>");
            echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", strval(StdClass.getValue(gVars.post, "post_date_gmt")), false));
            echo(gVars.webEnv, "</dc:date>\n\t<dc:creator>");
            getIncluded(Author_templatePage.class, gVars, gConsts).the_author("", true);
            echo(gVars.webEnv, "</dc:creator>\n\t");
            getIncluded(FeedPage.class, gVars, gConsts).the_category_rss("rdf");

            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_use_excerpt"))) {
                echo(gVars.webEnv, "\t<description>");
                getIncluded(FeedPage.class, gVars, gConsts).the_excerpt_rss();
                echo(gVars.webEnv, "</description>\n");
            } else {
                echo(gVars.webEnv, "\t<description>");
                getIncluded(FeedPage.class, gVars, gConsts).the_content_rss("", 0, "", intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_excerpt_length")), 2);
                echo(gVars.webEnv, "</description>\n\t<content:encoded><![CDATA[");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_content("", 0, "");
                echo(gVars.webEnv, "]]></content:encoded>\n");
            }

            echo(gVars.webEnv, "\t");
            getIncluded(PluginPage.class, gVars, gConsts).do_action("rdf_item", "");
            echo(gVars.webEnv, "</item>\n");
        }

        return DEFAULT_VAL;
    }
}
