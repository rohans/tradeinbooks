/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ArchivePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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


@Controller
@Scope("request")
public class ArchivePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ArchivePage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/archive.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/archive";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes__default_archive_block1");
        gVars.webEnv = webEnv;
        getIncluded(General_templatePage.class, gVars, gConsts).get_header();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_archive_block2");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts())/* If this is a category archive */
         {
            echo(gVars.webEnv, "\n \t  ");
            gVars.post = gVars.posts.getValue(0); // Hack. Set $post so that the_date() works. 
            echo(gVars.webEnv, " \t  ");

            /* If this is a category archive */ if (getIncluded(QueryPage.class, gVars, gConsts).is_category()) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Archive for the &#8216;");
                getIncluded(General_templatePage.class, gVars, gConsts).single_cat_title("", true);
                echo(gVars.webEnv, "&#8217; Category</h2>\n \t  ");
                
                /* If this is a tag archive */ 
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_tag("")) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Posts Tagged &#8216;");
                getIncluded(General_templatePage.class, gVars, gConsts).single_tag_title("", true);
                echo(gVars.webEnv, "&#8217;</h2>\n \t  ");
                
                /* If this is a daily archive */
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_day()) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Archive for ");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time("F jS, Y");
                echo(gVars.webEnv, "</h2>\n \t  ");
                
                /* If this is a monthly archive */
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_month()) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Archive for ");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time("F, Y");
                echo(gVars.webEnv, "</h2>\n \t  ");
                
                /* If this is a yearly archive */
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_year()) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Archive for ");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time("Y");
                echo(gVars.webEnv, "</h2>\n\t  ");
                
                /* If this is an author archive */ 
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_author()) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Author Archive</h2>\n \t  ");
                
                /* If this is a paged archive */ 
            } else if (isset(gVars.webEnv._GET.getValue("paged")) && !empty(gVars.webEnv._GET.getValue("paged"))) {
                echo(gVars.webEnv, "\t\t<h2 class=\"pagetitle\">Blog Archives</h2>\n \t  ");
            }

            echo(gVars.webEnv, "\n\n\t\t<div class=\"navigation\">\n\t\t\t<div class=\"alignleft\">");
            getIncluded(Link_templatePage.class, gVars, gConsts).next_posts_link("&laquo; Older Entries", 0);
            echo(gVars.webEnv, "</div>\n\t\t\t<div class=\"alignright\">");
            getIncluded(Link_templatePage.class, gVars, gConsts).previous_posts_link("Newer Entries &raquo;");
            echo(gVars.webEnv, "</div>\n\t\t</div>\n\n\t\t");

            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                echo(gVars.webEnv, "\t\t<div class=\"post\">\n\t\t\t\t<h3 id=\"post-");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                echo(gVars.webEnv, "\"><a href=\"");
                getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
                echo(gVars.webEnv, "\" rel=\"bookmark\" title=\"Permanent Link to ");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_title_attribute("");
                echo(gVars.webEnv, "\">");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_title("", "", true);
                echo(gVars.webEnv, "</a></h3>\n\t\t\t\t<small>");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time("l, F jS, Y");
                echo(gVars.webEnv, "</small>\n\n\t\t\t\t<div class=\"entry\">\n\t\t\t\t\t");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_content("(more...)", 0, "");
                echo(gVars.webEnv, "\t\t\t\t</div>\n\n\t\t\t\t<p class=\"postmetadata\">");
                getIncluded(Category_templatePage.class, gVars, gConsts).the_tags("Tags: ", ", ", "<br />");
                echo(gVars.webEnv, " Posted in ");
                getIncluded(Category_templatePage.class, gVars, gConsts).the_category(", ", "", 0);
                echo(gVars.webEnv, " | ");
                getIncluded(Link_templatePage.class, gVars, gConsts).edit_post_link("Edit", "", " | ");
                echo(gVars.webEnv, "  ");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comments_popup_link("No Comments &#187;", "1 Comment &#187;", "% Comments &#187;", "", "Comments Off");
                echo(gVars.webEnv, "</p>\n\n\t\t\t</div>\n\n\t\t");
            }

            echo(gVars.webEnv, "\n\t\t<div class=\"navigation\">\n\t\t\t<div class=\"alignleft\">");
            getIncluded(Link_templatePage.class, gVars, gConsts).next_posts_link("&laquo; Older Entries", 0);
            echo(gVars.webEnv, "</div>\n\t\t\t<div class=\"alignright\">");
            getIncluded(Link_templatePage.class, gVars, gConsts).previous_posts_link("Newer Entries &raquo;");
            echo(gVars.webEnv, "</div>\n\t\t</div>\n\n\t");
        } else {
            echo(gVars.webEnv, "\n\t\t<h2 class=\"center\">Not Found</h2>\n\t\t");
            /* Condensed dynamic construct: 248513 */ include(gVars, gConsts, SearchformPage.class);
            echo(gVars.webEnv, "\n\t");
        }

        /* Start of block */
        super.startBlock("__wp_content_themes__default_archive_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).get_sidebar(null);

        /* Start of block */
        super.startBlock("__wp_content_themes__default_archive_block4");
        getIncluded(General_templatePage.class, gVars, gConsts).get_footer();

        return DEFAULT_VAL;
    }
}
