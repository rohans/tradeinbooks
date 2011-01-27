/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Template_loaderPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.Wp_trackbackPage;
import org.numiton.nwp.wp_content.themes._default.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Template_loaderPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Template_loaderPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/template-loader.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/template_loader";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_template_loader_block1");
        gVars.webEnv = webEnv;

        /**
         * Loads the correct template based on the visitor's url
         * @package WordPress
         */
        if (gConsts.isWP_USE_THEMESDefined() && gConsts.getWP_USE_THEMES()) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("template_redirect", "");

            if (getIncluded(QueryPage.class, gVars, gConsts).is_robots()) {
                getIncluded(PluginPage.class, gVars, gConsts).do_action("do_robots", "");

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_feed()) {
                getIncluded(FunctionsPage.class, gVars, gConsts).do_feed();

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_trackback()) {
                /* Condensed dynamic construct */
                include(gVars, gConsts, Wp_trackbackPage.class);

                return null;
            } else
            // TODO Hardcoded to the default theme
            if (getIncluded(QueryPage.class, gVars, gConsts).is_404() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_404_template())) {
                include(gVars, gConsts, _404Page.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_search() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_search_template())) {
                include(gVars, gConsts, SearchPage.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_home() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_home_template())) {
                include(gVars, gConsts, IndexPage.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_attachment() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_attachment_template())) {
                getIncluded(PluginPage.class, gVars, gConsts).remove_filter("the_content", "prepend_attachment", 10, 1);

                if (equal("wp-content/themes/default/image.php", gVars.template)) {
                    include(gVars, gConsts, ImagePage.class);
                } else {
                    LOG.warn("Cannot find page class for: " + gVars.template);
                }

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_single() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_single_template())) {
                include(gVars, gConsts, SinglePage.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_page("") && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_page_template())) {
                include(gVars, gConsts, PagePage.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_category(new Array<Object>()) &&
                    booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_category_template())) {
                LOG.warn("Cannot find page class for: " + gVars.template);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_tag("") && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_tag_template())) {
                LOG.warn("Cannot find page class for: " + gVars.template);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_tax(new Array<Object>()) && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_taxonomy_template())) {
                LOG.warn("Cannot find page class for: " + gVars.template);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_author() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_author_template())) {
                LOG.warn("Cannot find page class for: " + gVars.template);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_date() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_date_template())) {
                LOG.warn("Cannot find page class for: " + gVars.template);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_archive() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_archive_template())) {
                include(gVars, gConsts, ArchivePage.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_comments_popup() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_comments_popup_template())) {
                include(gVars, gConsts, Comments_popupPage.class);

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_paged() && booleanval(gVars.template = getIncluded(ThemePage.class, gVars, gConsts).get_paged_template())) {
                LOG.warn("Cannot find page class for: " + gVars.template);

                return null;
            } else if (true)/*Modified by Numiton*/
             {
                include(gVars, gConsts, IndexPage.class);

                return null;
            }
        } else {
        	// Process feeds and trackbacks even if not using themes.
            if (getIncluded(QueryPage.class, gVars, gConsts).is_robots()) {
                getIncluded(PluginPage.class, gVars, gConsts).do_action("do_robots", "");

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_feed()) {
                getIncluded(FunctionsPage.class, gVars, gConsts).do_feed();

                return null;
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_trackback()) {
                /* Condensed dynamic construct */
                include(gVars, gConsts, Wp_trackbackPage.class);

                return null;
            }
        }

        return DEFAULT_VAL;
    }
}
