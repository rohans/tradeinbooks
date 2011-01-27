/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_links_opmlPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Network;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;


@Controller
@Scope("request")
public class Wp_links_opmlPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_links_opmlPage.class.getName());
    public Object cats;
    public String catname;
    public Object bookmarks;
    public StdClass bookmark;

    @Override
    @RequestMapping("/wp-links-opml.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_links_opml";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_links_opml_block1");
        gVars.webEnv = webEnv;

        if (empty(gVars.wp)) {
            requireOnce(gVars, gConsts, Wp_configPage.class);
            getIncluded(FunctionsPage.class, gVars, gConsts).wp("");
        }

        Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")), true);
        gVars.link_cat = gVars.webEnv._GET.getValue("link_cat");

        if (empty(gVars.link_cat) || equal(gVars.link_cat, "all") || equal(gVars.link_cat, "0")) {
            gVars.link_cat = "";
        } else/*
         * be safe be safe
         */
         {
            gVars.link_cat = "" + URL.urldecode(strval(gVars.link_cat)) + "";
            gVars.link_cat = intval(gVars.link_cat);
        }

        echo(gVars.webEnv, "<?xml version=\"1.0\"?" + ">\n");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("comment");

        /* Start of block */
        super.startBlock("__wp_links_opml_block2");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("name", "display") + gVars.cat_name));

        /* Start of block */
        super.startBlock("__wp_links_opml_block3");
        echo(gVars.webEnv, DateTime.gmdate("D, d M Y H:i:s"));

        /* Start of block */
        super.startBlock("__wp_links_opml_block4");

        if (empty(gVars.link_cat)) {
            cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("type=link&hierarchical=0");
        } else {
            cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("type=link&hierarchical=0&include=" + strval(gVars.link_cat));
        }

        for (Map.Entry javaEntry680 : new Array<Object>(cats).entrySet()) {
            gVars.cat = javaEntry680.getValue();
            catname = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("link_category", ((StdClass) gVars.cat).fields.getValue("name")));
            echo(gVars.webEnv, "<outline type=\"category\" title=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(catname));
            echo(gVars.webEnv, "\">\n");
            bookmarks = getIncluded(BookmarkPage.class, gVars, gConsts).get_bookmarks("category=" + ((StdClass) gVars.cat).fields.getValue("term_id"));

            for (Map.Entry javaEntry681 : new Array<Object>(bookmarks).entrySet()) {
                bookmark = (StdClass) javaEntry681.getValue();
                gVars.title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                        strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("link_title", StdClass.getValue(bookmark, "link_name"))));
                echo(gVars.webEnv, "\t<outline text=\"");
                echo(gVars.webEnv, gVars.title);
                echo(gVars.webEnv, "\" type=\"link\" xmlUrl=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(bookmark, "link_rss"))));
                echo(gVars.webEnv, "\" htmlUrl=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(bookmark, "link_url"))));
                echo(gVars.webEnv, "\" updated=\"");

                if (!equal("0000-00-00 00:00:00", StdClass.getValue(bookmark, "link_updated"))) {
                    echo(gVars.webEnv, StdClass.getValue(bookmark, "link_updated"));
                }

                echo(gVars.webEnv, "\" />\n");
            }

            echo(gVars.webEnv, "</outline>\n");
        }

        return DEFAULT_VAL;
    }
}
