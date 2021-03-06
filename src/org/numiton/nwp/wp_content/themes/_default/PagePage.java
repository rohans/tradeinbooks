/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PagePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;


@Controller(value = "wp_content/themes/_default/PagePage")
@Scope("request")
public class PagePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PagePage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/page.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/page";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes__default_page_block1");
        gVars.webEnv = webEnv;
        getIncluded(General_templatePage.class, gVars, gConsts).get_header();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_page_block2");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                echo(gVars.webEnv, "\t\t<div class=\"post\" id=\"post-");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                echo(gVars.webEnv, "\">\n\t\t<h2>");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_title();
                echo(gVars.webEnv, "</h2>\n\t\t\t<div class=\"entry\">\n\t\t\t\t");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_content("<p class=\"serif\">Read the rest of this page &raquo;</p>");
                echo(gVars.webEnv, "\n\t\t\t\t");
                getIncluded(Post_templatePage.class, gVars, gConsts).wp_link_pages(
                    new Array<Object>(new ArrayEntry<Object>("before", "<p><strong>Pages:</strong> "), new ArrayEntry<Object>("after", "</p>"), new ArrayEntry<Object>("next_or_number", "number")));
                echo(gVars.webEnv, "\n\t\t\t</div>\n\t\t</div>\n\t\t");
            }
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_content_themes__default_page_block3");
        getIncluded(Link_templatePage.class, gVars, gConsts).edit_post_link("Edit this entry.", "<p>", "</p>");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_page_block4");
        getIncluded(General_templatePage.class, gVars, gConsts).get_sidebar();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_page_block5");
        getIncluded(General_templatePage.class, gVars, gConsts).get_footer();

        return DEFAULT_VAL;
    }
}
