/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: HeaderPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
import org.numiton.nwp.wp_includes.General_templatePage;
import org.numiton.nwp.wp_includes.QueryPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class HeaderPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(HeaderPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/header.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/header";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block1");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block2");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("charset");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block4");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block5");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
            echo(gVars.webEnv, " &raquo; Blog Archive ");
        }

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block6");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_title();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block7");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("stylesheet_url");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block8");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block9");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("rss2_url");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block10");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("pingback_url");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block11");

        // Checks to see whether it needs a sidebar or not
        if (!empty(gVars.withcomments) && !getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
            echo(gVars.webEnv, "\t#page { background: url(\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("stylesheet_directory");
            echo(gVars.webEnv, "/images/kubrickbg-");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("text_direction");
            echo(gVars.webEnv, ".jpg\") repeat-y top; border: none; }\n");
        } else { // No sidebar 
            echo(gVars.webEnv, "\t#page { background: url(\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("stylesheet_directory");
            echo(gVars.webEnv, "/images/kubrickbgwide.jpg\") repeat-y top; border: none; }\n");
        }

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block12");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_head();

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block13");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_option("home"));

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block14");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_content_themes__default_header_block15");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("description");

        return DEFAULT_VAL;
    }
}
