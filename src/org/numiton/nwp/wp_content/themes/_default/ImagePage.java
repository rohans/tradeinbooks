/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ImagePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
import com.numiton.generic.StdClass;


@Controller(value = "wp_content/themes/_default/ImagePage")
@Scope("request")
public class ImagePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ImagePage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/image.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/image";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes__default_image_block1");
        gVars.webEnv = webEnv;
        getIncluded(General_templatePage.class, gVars, gConsts).get_header();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_image_block2");

        if (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
            while (getIncluded(QueryPage.class, gVars, gConsts).have_posts()) {
                getIncluded(QueryPage.class, gVars, gConsts).the_post();
                echo(gVars.webEnv, "\n\t\t<div class=\"post\" id=\"post-");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                echo(gVars.webEnv, "\">\n\t\t\t<h2><a href=\"");
                echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(gVars.post, "post_parent")));
                echo(gVars.webEnv, "\" rev=\"attachment\">");
                echo(gVars.webEnv, getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.post, "post_parent"))));
                echo(gVars.webEnv, "</a> &raquo; ");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_title();
                echo(gVars.webEnv, "</h2>\n\t\t\t<div class=\"entry\">\n\t\t\t\t<p class=\"attachment\"><a href=\"");
                echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(intval(StdClass.getValue(gVars.post, "ID"))));
                echo(gVars.webEnv, "\">");
                echo(gVars.webEnv, getIncluded(MediaPage.class, gVars, gConsts).wp_get_attachment_image(intval(StdClass.getValue(gVars.post, "ID")), "medium"));
                echo(gVars.webEnv, "</a></p>\n                <div class=\"caption\">");

                if (!empty(StdClass.getValue(gVars.post, "post_excerpt"))) {
                    getIncluded(Post_templatePage.class, gVars, gConsts).the_excerpt(); // this is the "caption" 
                }

                echo(gVars.webEnv, "</div>\n\n\t\t\t\t");
                getIncluded(Post_templatePage.class, gVars, gConsts).the_content("<p class=\"serif\">Read the rest of this entry &raquo;</p>");
                echo(gVars.webEnv, "\n\t\t\t\t<div class=\"navigation\">\n\t\t\t\t\t<div class=\"alignleft\">");
                getIncluded(MediaPage.class, gVars, gConsts).previous_image_link();
                echo(gVars.webEnv, "</div>\n\t\t\t\t\t<div class=\"alignright\">");
                getIncluded(MediaPage.class, gVars, gConsts).next_image_link();
                echo(gVars.webEnv, "</div>\n\t\t\t\t</div>\n\t\t\t\t<br class=\"clear\" />\n\n\t\t\t\t<p class=\"postmetadata alt\">\n\t\t\t\t\t<small>\n\t\t\t\t\t\tThis entry was posted on ");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time("l, F jS, Y");
                echo(gVars.webEnv, " at ");
                getIncluded(General_templatePage.class, gVars, gConsts).the_time();
                echo(gVars.webEnv, "\t\t\t\t\t\tand is filed under ");
                getIncluded(Category_templatePage.class, gVars, gConsts).the_category(", ");
                echo(gVars.webEnv, ".\n\t\t\t\t\t\t");
                getIncluded(TaxonomyPage.class, gVars, gConsts).the_taxonomies();
                echo(gVars.webEnv, "\t\t\t\t\t\tYou can follow any responses to this entry through the ");
                getIncluded(Link_templatePage.class, gVars, gConsts).post_comments_feed_link("RSS 2.0");
                echo(gVars.webEnv, " feed.\n\n\t\t\t\t\t\t");

                if (equal("open", StdClass.getValue(gVars.post, "comment_status")) && equal("open", StdClass.getValue(gVars.post, "ping_status"))) {
					// Both Comments and Pings are open ?>
                    echo(gVars.webEnv, "\t\t\t\t\t\t\tYou can <a href=\"#respond\">leave a response</a>, or <a href=\"");
                    getIncluded(Comment_templatePage.class, gVars, gConsts).trackback_url();
                    echo(gVars.webEnv, "\" rel=\"trackback\">trackback</a> from your own site.\n\n\t\t\t\t\t\t");
                } else if (!equal("open", StdClass.getValue(gVars.post, "comment_status")) && equal("open", StdClass.getValue(gVars.post, "ping_status"))) {
					// Only Pings are Open
                    echo(gVars.webEnv, "\t\t\t\t\t\t\tResponses are currently closed, but you can <a href=\"");
                    getIncluded(Comment_templatePage.class, gVars, gConsts).trackback_url();
                    echo(gVars.webEnv, " \" rel=\"trackback\">trackback</a> from your own site.\n\n\t\t\t\t\t\t");
                } else if (equal("open", StdClass.getValue(gVars.post, "comment_status")) && !equal("open", StdClass.getValue(gVars.post, "ping_status"))) {
					// Comments are open, Pings are not ?>
                    echo(gVars.webEnv, "\t\t\t\t\t\t\tYou can skip to the end and leave a response. Pinging is currently not allowed.\n\n\t\t\t\t\t\t");
                } else if (!equal("open", StdClass.getValue(gVars.post, "comment_status")) && !equal("open", StdClass.getValue(gVars.post, "ping_status"))) {
					// Neither Comments, nor Pings are open ?>
                    echo(gVars.webEnv, "\t\t\t\t\t\t\tBoth comments and pings are currently closed.\n\n\t\t\t\t\t\t");
                }

                getIncluded(Link_templatePage.class, gVars, gConsts).edit_post_link("Edit this entry.", "", "");
                echo(gVars.webEnv, "\n\t\t\t\t\t</small>\n\t\t\t\t</p>\n\n\t\t\t</div>\n\n\t\t</div>\n\n\t");
                getIncluded(Comment_templatePage.class, gVars, gConsts).comments_template();
                echo(gVars.webEnv, "\n\t");
            }
        } else {
            echo(gVars.webEnv, "\n\t\t<p>Sorry, no attachments matched your criteria.</p>\n\n");
        }

        /* Start of block */
        super.startBlock("__wp_content_themes__default_image_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).get_footer();

        return DEFAULT_VAL;
    }
}
