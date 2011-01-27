/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: HeaderPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.General_templatePage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;


@Controller(value = "wp_content/themes/classic/HeaderPage")
@Scope("request")
public class HeaderPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(HeaderPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/classic/header.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/header";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block1");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes();

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block2");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("charset");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block4");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_title();

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block5");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("stylesheet_url");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block6");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("rss2_url");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block7");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("rss_url");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block8");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("atom_url");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block9");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("pingback_url");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block10");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_get_archives("type=monthly&format=link");
        //comments_popup_script(); // off by default 

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block11");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_head();

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block12");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_header_block13");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        return DEFAULT_VAL;
    }
}
