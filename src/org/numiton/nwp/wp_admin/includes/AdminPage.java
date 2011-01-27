/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: AdminPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin.includes;

import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.RegistrationPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;


@Controller(value = "wp_admin/includes/AdminPage")
@Scope("request")
public class AdminPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(AdminPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/admin.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/admin";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_admin_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, BookmarkPage.class);
        requireOnce(gVars, gConsts, CommentPage.class);
        requireOnce(gVars, gConsts, FilePage.class);
        requireOnce(gVars, gConsts, ImagePage.class);
        requireOnce(gVars, gConsts, ImportPage.class);
        requireOnce(gVars, gConsts, MediaPage.class);
        requireOnce(gVars, gConsts, MiscPage.class);
        requireOnce(gVars, gConsts, PluginPage.class);
        requireOnce(gVars, gConsts, PostPage.class);
        requireOnce(gVars, gConsts, TaxonomyPage.class);
        requireOnce(gVars, gConsts, TemplatePage.class);
        requireOnce(gVars, gConsts, ThemePage.class);
        requireOnce(gVars, gConsts, UserPage.class);
        requireOnce(gVars, gConsts, UpdatePage.class);
        
        requireOnce(gVars, gConsts, RegistrationPage.class);

        return DEFAULT_VAL;
    }
}
