/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: SidebarPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.VarHandling.echo;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

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

import com.numiton.generic.PhpWebEnvironment;


@Controller(value = "wp_content/themes/classic/SidebarPage")
@Scope("request")
public class SidebarPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(SidebarPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/classic/sidebar.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/sidebar";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_sidebar_block1");

        /* Widgetized sidebar, if you have the plugin installed. */
        if (!true /*Modified by Numiton*/ || !getIncluded(WidgetsPage.class, gVars, gConsts).dynamic_sidebar()) {
            echo(gVars.webEnv, "\t");
            getIncluded(Post_templatePage.class, gVars, gConsts).wp_list_pages("title_li=" + getIncluded(L10nPage.class, gVars, gConsts).__("Pages:"));
            echo(gVars.webEnv, "\t");
            getIncluded(Bookmark_templatePage.class, gVars, gConsts).wp_list_bookmarks("title_after=&title_before=");
            echo(gVars.webEnv, "\t");
            getIncluded(Category_templatePage.class, gVars, gConsts).wp_list_categories("title_li=" + getIncluded(L10nPage.class, gVars, gConsts).__("Categories:"));
            echo(gVars.webEnv, " <li id=\"search\">\n   <label for=\"s\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Search:");
            echo(gVars.webEnv, "</label>\n   <form id=\"searchform\" method=\"get\" action=\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("home");
            echo(gVars.webEnv, "\">\n\t<div>\n\t\t<input type=\"text\" name=\"s\" id=\"s\" size=\"15\" /><br />\n\t\t<input type=\"submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Search");
            echo(gVars.webEnv, "\" />\n\t</div>\n\t</form>\n </li>\n <li id=\"archives\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Archives:");
            echo(gVars.webEnv, "\t<ul>\n\t ");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_get_archives("type=monthly");
            echo(gVars.webEnv, "\t</ul>\n </li>\n <li id=\"meta\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Meta:");
            echo(gVars.webEnv, "\t<ul>\n\t\t");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_register();
            echo(gVars.webEnv, "\t\t<li>");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_loginout();
            echo(gVars.webEnv, "</li>\n\t\t<li><a href=\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("rss2_url");
            echo(gVars.webEnv, "\" title=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Syndicate this site using RSS");
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("<abbr title=\"Really Simple Syndication\">RSS</abbr>");
            echo(gVars.webEnv, "</a></li>\n\t\t<li><a href=\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("comments_rss2_url");
            echo(gVars.webEnv, "\" title=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("The latest comments to all posts in RSS");
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Comments <abbr title=\"Really Simple Syndication\">RSS</abbr>");
            echo(gVars.webEnv, "</a></li>\n\t\t<li><a href=\"http://validator.w3.org/check/referer\" title=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("This page validates as XHTML 1.0 Transitional");
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Valid <abbr title=\"eXtensible HyperText Markup Language\">XHTML</abbr>");
            echo(gVars.webEnv, "</a></li>\n\t\t<li><a href=\"http://gmpg.org/xfn/\"><abbr title=\"XHTML Friends Network\">XFN</abbr></a></li>\n\t\t<li><a href=\"http://wordpress.org/\" title=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Powered by WordPress, state-of-the-art semantic personal publishing platform.");
            echo(gVars.webEnv, "\"><abbr title=\"WordPress\">WP</abbr></a></li>\n\t\t");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_meta();
            echo(gVars.webEnv, "\t</ul>\n </li>\n");
        } else {
        }

        return DEFAULT_VAL;
    }
}
