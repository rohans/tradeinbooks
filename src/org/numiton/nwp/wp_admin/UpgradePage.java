/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UpgradePage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.Wp_configPage;
import org.numiton.nwp.Wp_settingsPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.URL;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class UpgradePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(UpgradePage.class.getName());
    public String backto;

    @Override
    @RequestMapping("/wp-admin/upgrade.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/upgrade";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block1");
        gVars.webEnv = webEnv;
        gConsts.setWP_INSTALLING(true);

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, "../wp-config.php")) {
            System.exit(
                    "There doesn\'t seem to be a <code>wp-config.php</code> file. I need this before we can get started. Need more help? <a href=\'http://codex.wordpress.org/Installing_WordPress#Step_3:_Set_up_wp-config.php\'>We got it</a>. You can create a <code>wp-config.php</code> file through a web interface, but this doesn\'t work for all server setups. The safest way is to manually create the file.</p><p><a href=\'setup-config.php\' class=\'button\'>Create a Configuration File</a>");
        }

        require(gVars, gConsts, Wp_configPage.class);
        getIncluded(Wp_settingsPage.class, gVars, gConsts).timer_start();

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, UpgradePage.class);

        if (isset(gVars.webEnv._GET.getValue("step"))) {
            gVars.step = intval(gVars.webEnv._GET.getValue("step"));
        } else {
            gVars.step = 0;
        }

        Network.header(
            gVars.webEnv,
            "Content-Type: " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type") + "; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block2");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");

        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");

        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block4");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("WordPress &rsaquo; Upgrade", "default");

        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block6");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/install");

        /* Start of block */
        super.startBlock("__wp_admin_upgrade_block7");

        if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("db_version"), gVars.wp_db_version)) {
            echo(gVars.webEnv, "\n<h2>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No Upgrade Required", "default");
            echo(gVars.webEnv, "</h2>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Your WordPress database is already up-to-date!", "default");
            echo(gVars.webEnv, "</p>\n<h2 class=\"step\"><a href=\"");
            echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
            echo(gVars.webEnv, "/\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Continue", "default");
            echo(gVars.webEnv, "</a></h2>\n\n");
        } else {
            switch (gVars.step) {
            case 0: {
                gVars.goback = Strings.stripslashes(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());
                gVars.goback = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(gVars.goback, null, "url");
                gVars.goback = URL.urlencode(gVars.goback);
                echo(gVars.webEnv, "<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Database Upgrade Required", "default");
                echo(gVars.webEnv, "</h2>\n<p>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Your WordPress database is out-of-date, and must be upgraded before you can continue.", "default");
                echo(gVars.webEnv, "</p>\n<p>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("The upgrade process may take a while, so please be patient.", "default");
                echo(gVars.webEnv, "</p>\n<h2 class=\"step\"><a href=\"upgrade.php?step=1&amp;backto=");
                echo(gVars.webEnv, gVars.goback);
                echo(gVars.webEnv, "\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Upgrade WordPress", "default");
                echo(gVars.webEnv, "</a></h2>\n");

                break;
            }

            case 1: {
                (((org.numiton.nwp.wp_admin.includes.UpgradePage) getIncluded(org.numiton.nwp.wp_admin.includes.UpgradePage.class, gVars, gConsts))).wp_upgrade();

                if (empty(gVars.webEnv._GET.getValue("backto"))) {
                    backto = strval((((org.numiton.nwp.wp_admin.includes.UpgradePage) getIncluded(org.numiton.nwp.wp_admin.includes.UpgradePage.class, gVars, gConsts))).__get_option("home")) + "/";
                } else {
                    backto = Strings.stripslashes(gVars.webEnv, URL.urldecode(strval(gVars.webEnv._GET.getValue("backto"))));
                    backto = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(backto, null, "url");
                }

                echo(gVars.webEnv, "<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Upgrade Complete", "default");
                echo(gVars.webEnv, "</h2>\n\t<p>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Your WordPress database has been successfully upgraded!", "default");
                echo(gVars.webEnv, "</p>\n\t<h2 class=\"step\"><a href=\"");
                echo(gVars.webEnv, backto);
                echo(gVars.webEnv, "\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Continue", "default");
                echo(gVars.webEnv, "</a></h2>\n\n<!--\n<pre>\n");
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("%s queries", "default"), gVars.wpdb.num_queries);
                echo(gVars.webEnv, "\n");
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("%s seconds", "default"), getIncluded(Wp_settingsPage.class, gVars, gConsts).timer_stop(0, 3));
                echo(gVars.webEnv, "</pre>\n-->\n\n");

                break;
            }
            }
        }

        return DEFAULT_VAL;
    }
}
