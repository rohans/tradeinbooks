/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Feed_atomPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
public class Feed_atomPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Feed_atomPage.class.getName());
    public Object author_url;

    @Override
    @RequestMapping("/wp-includes/feed-atom.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed_atom";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block1");
        gVars.webEnv = webEnv;
        
        /**
         * Atom Feed Template for displaying Atom Posts feed.
         *
         * @package WordPress
         */
        Network.header(gVars.webEnv, "Content-Type: application/atom+xml; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"), true);
        gVars.more = 1;
        echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\"?" + ">");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block2");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_language"));

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block3");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("home");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block4");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("atom_ns", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block5");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("name");
        getIncluded(FeedPage.class, gVars, gConsts).wp_title_rss("&#187;");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block6");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block7");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d\\TH:i:s\\Z", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), true));

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block8");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("atom");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block9");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("home");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block10");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("atom_url");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block11");
        getIncluded(FeedPage.class, gVars, gConsts).self_link();

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block12");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("atom_head", "");

        /* Start of block */
        super.startBlock("__wp_includes_feed_atom_block13");

        while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            getIncluded(QueryPage.class, gVars, gConsts).the_post();
            echo(gVars.webEnv, "\t<entry>\n\t\t<author>\n\t\t\t<name>");
            getIncluded(Author_templatePage.class, gVars, gConsts).the_author("", true);
            echo(gVars.webEnv, "</name>\n\t\t\t");
            author_url = getIncluded(Author_templatePage.class, gVars, gConsts).get_the_author_url();

            if (!empty(author_url)) {
                echo(gVars.webEnv, "\t\t\t<uri>");
                getIncluded(Author_templatePage.class, gVars, gConsts).the_author_url();
                echo(gVars.webEnv, "</uri>\n\t\t\t");
            } else {
            }

            echo(gVars.webEnv, "\t\t</author>\n\t\t<title type=\"");
            getIncluded(FeedPage.class, gVars, gConsts).html_type_rss();
            echo(gVars.webEnv, "\"><![CDATA[");
            getIncluded(FeedPage.class, gVars, gConsts).the_title_rss();
            echo(gVars.webEnv, "]]></title>\n\t\t<link rel=\"alternate\" type=\"text/html\" href=\"");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "\" />\n\t\t<id>");
            getIncluded(Post_templatePage.class, gVars, gConsts).the_guid(0);
            echo(gVars.webEnv, "</id>\n\t\t<updated>");
            echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).get_post_modified_time("Y-m-d\\TH:i:s\\Z", true));
            echo(gVars.webEnv, "</updated>\n\t\t<published>");
            echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("Y-m-d\\TH:i:s\\Z", true));
            echo(gVars.webEnv, "</published>\n\t\t");
            getIncluded(FeedPage.class, gVars, gConsts).the_category_rss("atom");
            echo(gVars.webEnv, "\t\t<summary type=\"");
            getIncluded(FeedPage.class, gVars, gConsts).html_type_rss();
            echo(gVars.webEnv, "\"><![CDATA[");
            getIncluded(FeedPage.class, gVars, gConsts).the_excerpt_rss();
            echo(gVars.webEnv, "]]></summary>\n");

            if (!booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_use_excerpt"))) {
                echo(gVars.webEnv, "\t\t<content type=\"");
                getIncluded(FeedPage.class, gVars, gConsts).html_type_rss();
                echo(gVars.webEnv, "\" xml:base=\"");
                getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
                echo(gVars.webEnv, "\"><![CDATA[");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_content("", 0, "");
                echo(gVars.webEnv, "]]></content>\n");
            } else {
            }

            getIncluded(FeedPage.class, gVars, gConsts).atom_enclosure();
            getIncluded(PluginPage.class, gVars, gConsts).do_action("atom_entry", "");
            echo(gVars.webEnv, "\t\t<link rel=\"replies\" type=\"text/html\" href=\"");
            getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
            echo(gVars.webEnv, "#comments\" thr:count=\"");
            echo(gVars.webEnv, getIncluded(Comment_templatePage.class, gVars, gConsts).get_comments_number(0));
            echo(gVars.webEnv, "\"/>\n\t\t<link rel=\"replies\" type=\"application/atom+xml\" href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_post_comments_feed_link(0, "atom"));
            echo(gVars.webEnv, "\" thr:count=\"");
            echo(gVars.webEnv, getIncluded(Comment_templatePage.class, gVars, gConsts).get_comments_number(0));
            echo(gVars.webEnv, "\"/>\n\t\t<thr:total>");
            echo(gVars.webEnv, getIncluded(Comment_templatePage.class, gVars, gConsts).get_comments_number(0));
            echo(gVars.webEnv, "</thr:total>\n\t</entry>\n\t");
        }

        return DEFAULT_VAL;
    }
}
