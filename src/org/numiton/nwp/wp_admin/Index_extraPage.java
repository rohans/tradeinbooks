/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Index_extraPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.equal;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.DashboardPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.RssPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Index_extraPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Index_extraPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/index-extra.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/index_extra";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_index_extra_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        require(gVars, gConsts, DashboardPage.class);
        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, RssPage.class);

        Network.header(
            gVars.webEnv,
            "Content-Type: " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type") + "; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        {
            int javaSwitchSelector17 = 0;

            if (equal(gVars.webEnv._GET.getValue("jax"), "incominglinks")) {
                javaSwitchSelector17 = 1;
            }

            if (equal(gVars.webEnv._GET.getValue("jax"), "devnews")) {
                javaSwitchSelector17 = 2;
            }

            if (equal(gVars.webEnv._GET.getValue("jax"), "planetnews")) {
                javaSwitchSelector17 = 3;
            }

            if (equal(gVars.webEnv._GET.getValue("jax"), "plugins")) {
                javaSwitchSelector17 = 4;
            }

            switch (javaSwitchSelector17) {
            case 1: {
                getIncluded(DashboardPage.class, gVars, gConsts).wp_dashboard_incoming_links_output();

                break;
            }

            case 2: {
                getIncluded(DashboardPage.class, gVars, gConsts).wp_dashboard_rss_output("dashboard_primary");

                break;
            }

            case 3: {
                getIncluded(DashboardPage.class, gVars, gConsts).wp_dashboard_secondary_output();

                break;
            }

            case 4: {
                getIncluded(DashboardPage.class, gVars, gConsts).wp_dashboard_plugins_output();

                break;
            }
            }
        }

        return DEFAULT_VAL;
    }
}
