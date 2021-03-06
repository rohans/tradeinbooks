/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: _404Page.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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


@Controller
@Scope("request")
public class _404Page extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(_404Page.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/404.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/_404";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes__default__404_block1");
        gVars.webEnv = webEnv;
        getIncluded(General_templatePage.class, gVars, gConsts).get_header();

        /* Start of block */
        super.startBlock("__wp_content_themes__default__404_block2");
        getIncluded(General_templatePage.class, gVars, gConsts).get_sidebar();

        /* Start of block */
        super.startBlock("__wp_content_themes__default__404_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).get_footer();

        return DEFAULT_VAL;
    }
}
