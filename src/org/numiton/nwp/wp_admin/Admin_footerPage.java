/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Admin_footerPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.echo;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.L10nPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Admin_footerPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Admin_footerPage.class.getName());
    public Object upgrade;

    @Override
    @RequestMapping("/wp-admin/admin-footer.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/admin_footer";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_admin_admin_footer_block1");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("in_admin_footer", "");
        upgrade = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("update_footer", "");

        // Commented by Numiton
        echo(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "Thank you for creating with <a href=\"http://nwordpress.sourceforge.net/\">nWordPress</a>, the Java migration of <a href=\"http://wordpress.org/\">WordPress</a> 2.5.1",
                        "default") /*+ " | "
            + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://codex.wordpress.org/\">Documentation</a>", "default") + " | "
            + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://wordpress.org/support/forum/4\">Feedback</a>", "default") + " " + strval(upgrade)*/);

        /* Start of block */
        super.startBlock("__wp_admin_admin_footer_block2");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_footer", "");

        return DEFAULT_VAL;
    }
}
