/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_privacyPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.strval;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Options_privacyPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_privacyPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/options-privacy.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_privacy";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Privacy Settings", "default");
        gVars.parent_file = "options-general.php";
        include(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Privacy Settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Blog Visibility", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block5");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_public")));

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("I would like my blog to be visible to everyone, including search engines (like Google, Sphere, Technorati) and archivers", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block7");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("0", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_public")));

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("I would like to block search engines, but allow normal visitors", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block9");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("blog_privacy_selector", "");

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_privacy_block11");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
