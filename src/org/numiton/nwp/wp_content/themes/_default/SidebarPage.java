/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: SidebarPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_content.themes._default;

import static com.numiton.VarHandling.*;
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


@Controller(value = "wp_content/themes/_default/SidebarPage")
@Scope("request")
public class SidebarPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(SidebarPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/sidebar.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/sidebar";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_content_themes__default_sidebar_block1");

        /* Widgetized sidebar, if you have the plugin installed. */
        if (!true || /*Modified by Numiton*/
                !getIncluded(WidgetsPage.class, gVars, gConsts).dynamic_sidebar(strval(1)))/* If this is the frontpage */
         {
            echo(gVars.webEnv, "\t\t\t<li>\n\t\t\t\t");
            /* Condensed dynamic construct: 253511 */ includeOnce(gVars, gConsts, SearchformPage.class);
            echo(
                    gVars.webEnv,
                    "\t\t\t</li>\n\n\t\t\t<!-- Author information is disabled per default. Uncomment and fill in your details if you want to use it.\n\t\t\t<li><h2>Author</h2>\n\t\t\t<p>A little something about you, the author. Nothing lengthy, just an overview.</p>\n\t\t\t</li>\n\t\t\t-->\n\n\t\t\t");

            if (getIncluded(QueryPage.class, gVars, gConsts).is_404() || getIncluded(QueryPage.class, gVars, gConsts).is_category() || getIncluded(QueryPage.class, gVars, gConsts).is_day() ||
                    getIncluded(QueryPage.class, gVars, gConsts).is_month() || getIncluded(QueryPage.class, gVars, gConsts).is_year() || getIncluded(QueryPage.class, gVars, gConsts).is_search() ||
                    getIncluded(QueryPage.class, gVars, gConsts).is_paged())/* If this is a 404 page */
             {
                echo(gVars.webEnv, " <li>\n\n\t\t\t");

                /* If this is a 404 page */ if (getIncluded(QueryPage.class, gVars, gConsts).is_404()) {
                    echo(gVars.webEnv, "\t\t\t");
                    
                    /* If this is a category archive */
                } else if (getIncluded(QueryPage.class, gVars, gConsts).is_category()) {
                    echo(gVars.webEnv, "\t\t\t<p>You are currently browsing the archives for the ");
                    getIncluded(General_templatePage.class, gVars, gConsts).single_cat_title("", true);
                    echo(gVars.webEnv, " category.</p>\n\n\t\t\t");
                    
                    /* If this is a yearly archive */ 
                } else if (getIncluded(QueryPage.class, gVars, gConsts).is_day()) {
                    echo(gVars.webEnv, "\t\t\t<p>You are currently browsing the <a href=\"");
                    getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
                    echo(gVars.webEnv, "/\">");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name"));
                    echo(gVars.webEnv, "</a> blog archives\n\t\t\tfor the day ");
                    getIncluded(General_templatePage.class, gVars, gConsts).the_time("l, F jS, Y");
                    echo(gVars.webEnv, ".</p>\n\n\t\t\t");
                    
                    /* If this is a monthly archive */
                } else if (getIncluded(QueryPage.class, gVars, gConsts).is_month()) {
                    echo(gVars.webEnv, "\t\t\t<p>You are currently browsing the <a href=\"");
                    getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
                    echo(gVars.webEnv, "/\">");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name"));
                    echo(gVars.webEnv, "</a> blog archives\n\t\t\tfor ");
                    getIncluded(General_templatePage.class, gVars, gConsts).the_time("F, Y");
                    echo(gVars.webEnv, ".</p>\n\n\t\t\t");
                    
                    /* If this is a yearly archive */
                } else if (getIncluded(QueryPage.class, gVars, gConsts).is_year()) {
                    echo(gVars.webEnv, "\t\t\t<p>You are currently browsing the <a href=\"");
                    getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
                    echo(gVars.webEnv, "/\">");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name"));
                    echo(gVars.webEnv, "</a> blog archives\n\t\t\tfor the year ");
                    getIncluded(General_templatePage.class, gVars, gConsts).the_time("Y");
                    echo(gVars.webEnv, ".</p>\n\n\t\t\t");
                    
                    /* If this is a monthly archive */
                } else if (getIncluded(QueryPage.class, gVars, gConsts).is_search()) {
                    echo(gVars.webEnv, "\t\t\t<p>You have searched the <a href=\"");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url"));
                    echo(gVars.webEnv, "/\">");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name"));
                    echo(gVars.webEnv, "</a> blog archives\n\t\t\tfor <strong>\'");
                    getIncluded(General_templatePage.class, gVars, gConsts).the_search_query();
                    echo(gVars.webEnv, "\'</strong>. If you are unable to find anything in these search results, you can try one of these links.</p>\n\n\t\t\t");
                    
                    /* If this is a monthly archive */ 
                } else if (isset(gVars.webEnv._GET.getValue("paged")) && !empty(gVars.webEnv._GET.getValue("paged"))) {
                    echo(gVars.webEnv, "\t\t\t<p>You are currently browsing the <a href=\"");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url"));
                    echo(gVars.webEnv, "/\">");
                    echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name"));
                    echo(gVars.webEnv, "</a> blog archives.</p>\n\n\t\t\t");
                }

                echo(gVars.webEnv, "\n\t\t\t</li> ");
            }

            echo(gVars.webEnv, "\n\t\t\t");
            getIncluded(Post_templatePage.class, gVars, gConsts).wp_list_pages("title_li=<h2>Pages</h2>");
            echo(gVars.webEnv, "\n\t\t\t<li><h2>Archives</h2>\n\t\t\t\t<ul>\n\t\t\t\t");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_get_archives("type=monthly");
            echo(gVars.webEnv, "\t\t\t\t</ul>\n\t\t\t</li>\n\n\t\t\t");
            getIncluded(Category_templatePage.class, gVars, gConsts).wp_list_categories("show_count=1&title_li=<h2>Categories</h2>");
            echo(gVars.webEnv, "\n\t\t\t");

            /* If this is the frontpage */ 
            if (getIncluded(QueryPage.class, gVars, gConsts).is_home() || getIncluded(QueryPage.class, gVars, gConsts).is_page("")) {
                echo(gVars.webEnv, "\t\t\t\t");
                getIncluded(Bookmark_templatePage.class, gVars, gConsts).wp_list_bookmarks("");
                echo(gVars.webEnv, "\n\t\t\t\t<li><h2>Meta</h2>\n\t\t\t\t<ul>\n\t\t\t\t\t");
                getIncluded(General_templatePage.class, gVars, gConsts).wp_register("<li>", "</li>");
                echo(gVars.webEnv, "\t\t\t\t\t<li>");
                getIncluded(General_templatePage.class, gVars, gConsts).wp_loginout();
                echo(
                        gVars.webEnv,
                        "</li>\n\t\t\t\t\t<li><a href=\"http://validator.w3.org/check/referer\" title=\"This page validates as XHTML 1.0 Transitional\">Valid <abbr title=\"eXtensible HyperText Markup Language\">XHTML</abbr></a></li>\n\t\t\t\t\t<li><a href=\"http://gmpg.org/xfn/\"><abbr title=\"XHTML Friends Network\">XFN</abbr></a></li>\n\t\t\t\t\t<li><a href=\"http://wordpress.org/\" title=\"Powered by WordPress, state-of-the-art semantic personal publishing platform.\">WordPress</a></li>\n\t\t\t\t\t");
                getIncluded(General_templatePage.class, gVars, gConsts).wp_meta();
                echo(gVars.webEnv, "\t\t\t\t</ul>\n\t\t\t\t</li>\n\t\t\t");
            }

            echo(gVars.webEnv, "\n\t\t\t");
        } else {
        }

        return DEFAULT_VAL;
    }
}
