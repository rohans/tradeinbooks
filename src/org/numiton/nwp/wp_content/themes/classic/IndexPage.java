/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IndexPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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


@Controller(value = "wp_content/themes/classic/IndexPage")
@Scope("request")
public class IndexPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(IndexPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/classic/index.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/index";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes_classic_index_block1");
        gVars.webEnv = webEnv;
        getIncluded(General_templatePage.class, gVars, gConsts).get_header();

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_index_block2");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                echo(gVars.webEnv, "\n");
                getIncluded(General_templatePage.class, gVars, gConsts).the_date("", "<h2>", "</h2>");
                echo(gVars.webEnv, "\n<div class=\"post\" id=\"post-");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                echo(gVars.webEnv, "\">\n\t <h3 class=\"storytitle\"><a href=\"");
                getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
                echo(gVars.webEnv, "\" rel=\"bookmark\">");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_title();
                echo(gVars.webEnv, "</a></h3>\n\t<div class=\"meta\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Filed under:");
                echo(gVars.webEnv, " ");
                getIncluded(Category_templatePage.class, gVars, gConsts).the_category(",");
                echo(gVars.webEnv, " &#8212; ");
                getIncluded(Category_templatePage.class, gVars, gConsts).the_tags(getIncluded(L10nPage.class, gVars, gConsts).__("Tags: "), ", ", " &#8212; ");
                echo(gVars.webEnv, " ");
                getIncluded(Author_templatePage.class, gVars, gConsts).the_author();
                echo(gVars.webEnv, " @ ");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time();
                echo(gVars.webEnv, " ");
                getIncluded(Link_templatePage.class, gVars, gConsts).edit_post_link(getIncluded(L10nPage.class, gVars, gConsts).__("Edit This"));
                echo(gVars.webEnv, "</div>\n\n\t<div class=\"storycontent\">\n\t\t");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_content(getIncluded(L10nPage.class, gVars, gConsts).__("(more...)"));
                echo(gVars.webEnv, "\t</div>\n\n\t<div class=\"feedback\">\n\t\t");
                getIncluded(Post_templatePage.class, gVars, gConsts).wp_link_pages();
                echo(gVars.webEnv, "\t\t");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comments_popup_link(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Comments (0)"),
                    getIncluded(L10nPage.class, gVars, gConsts).__("Comments (1)"),
                    getIncluded(L10nPage.class, gVars, gConsts).__("Comments (%)"));
                echo(gVars.webEnv, "\t</div>\n\n</div>\n\n");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comments_template(); // Get wp-comments.php template
                echo(gVars.webEnv, "\n");
            }
        } else {
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Sorry, no posts matched your criteria.");
            echo(gVars.webEnv, "</p>\n");
        }

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_index_block3");
        getIncluded(Link_templatePage.class, gVars, gConsts).posts_nav_link(
            " &#8212; ",
            getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Older Posts"),
            getIncluded(L10nPage.class, gVars, gConsts).__("Newer Posts &raquo;"));

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_index_block4");
        getIncluded(General_templatePage.class, gVars, gConsts).get_footer();

        return DEFAULT_VAL;
    }
}
