/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_readingPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Options_readingPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_readingPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/options-reading.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_reading";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Reading Settings", "default");
        gVars.parent_file = "options-general.php";
        include(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Reading Settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block4");

        if (booleanval(getIncluded(PostPage.class, gVars, gConsts).get_pages(""))) {
            echo(gVars.webEnv, "<tr valign=\"top\">\n<th scope=\"row\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Front page displays", "default");
            echo(gVars.webEnv, "</th>\n<td>\n\t<p><label>\n\t\t<input name=\"show_on_front\" type=\"radio\" value=\"posts\" class=\"tog\" ");
            getIncluded(TemplatePage.class, gVars, gConsts).checked("posts", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")));
            echo(gVars.webEnv, " />\n\t\t");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Your latest posts", "default");
            echo(gVars.webEnv, "\t</label>\n\t</p>\n\t<p><label>\n\t\t<input name=\"show_on_front\" type=\"radio\" value=\"page\" class=\"tog\" ");
            getIncluded(TemplatePage.class, gVars, gConsts).checked("page", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")));
            echo(gVars.webEnv, " />\n\t\t");
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("A <a href=\"%s\">static page</a> (select below)", "default"), "edit-pages.php");
            echo(gVars.webEnv, "\t</label>\n\t</p>\n<ul>\n\t<li>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__("Front page: %s", "default"),
                    getIncluded(Post_templatePage.class, gVars, gConsts).wp_dropdown_pages(
                        "name=page_on_front&echo=0&show_option_none=" + getIncluded(L10nPage.class, gVars, gConsts).__("- Select -", "default") + "&selected=" +
                        getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front")));
            echo(gVars.webEnv, "</li>\n\t<li>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__("Posts page: %s", "default"),
                    getIncluded(Post_templatePage.class, gVars, gConsts).wp_dropdown_pages(
                        "name=page_for_posts&echo=0&show_option_none=" + getIncluded(L10nPage.class, gVars, gConsts).__("- Select -", "default") + "&selected=" +
                        getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts")));
            echo(gVars.webEnv, "</li>\n</ul>\n");

            if (equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) &&
                    equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_for_posts"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"))) {
                echo(gVars.webEnv, "<div id=\"front-page-warning\" class=\"updated fade-ff0000\">\n\t<p>\n\t\t");
                getIncluded(L10nPage.class, gVars, gConsts)._e("<strong>Warning:</strong> these pages should not be the same!", "default");
                echo(gVars.webEnv, "\t</p>\n</div>\n");
            } else {
            }

            echo(gVars.webEnv, "</td>\n</tr>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Blog pages show at most", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block6");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("posts_per_page");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("posts", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Syndication feeds show the most recent", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block9");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("posts_per_rss");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("posts", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("For each article in a feed, show", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block12");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(0), strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_use_excerpt")));

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Full text", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block14");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(1), strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_use_excerpt")));

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Summary", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Encoding for pages and feeds", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block17");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("blog_charset");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "The character encoding you write your blog in (UTF-8 is <a href=\"http://developer.apple.com/documentation/macos8/TextIntlSvcs/TextEncodingConversionManager/TEC1.5/TEC.b0.html\">recommended</a>)",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_reading_block20");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
