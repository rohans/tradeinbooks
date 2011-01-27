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
import org.numiton.nwp.wp_admin._import.*;
import org.numiton.nwp.wp_admin._import.RssPage;
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_admin.includes.UpgradePage;
import org.numiton.nwp.wp_content.plugins.akismet.AkismetPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class AdminPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(AdminPage.class.getName());
    public Object date_format;
    public Object time_format;
    public String importer;

    @Override
    @RequestMapping("/wp-admin/admin.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/admin";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_admin_block1");
        gVars.webEnv = webEnv;
        gConsts.setWP_ADMIN(true);

        if (gConsts.isABSPATHDefined()) {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, Wp_configPage.class);
        } else {
            requireOnce(gVars, gConsts, Wp_configPage.class);
        }

        if (!equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("db_version"), gVars.wp_db_version)) {
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                    getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/upgrade.php?_wp_http_referer=" +
                    URL.urlencode(Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())),
                    302);
            System.exit();
        }

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, org.numiton.nwp.wp_admin.includes.AdminPage.class);
        getIncluded(PluggablePage.class, gVars, gConsts).auth_redirect();
        getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
        getIncluded(CategoryPage.class, gVars, gConsts).update_category_cache();
        gVars.posts_per_page = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("posts_per_page");
        gVars.what_to_show = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("what_to_show");
        date_format = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format");
        time_format = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format");
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(
            this,
            new Array<Object>(
                new ArrayEntry<Object>("profile"),
                new ArrayEntry<Object>("redirect"),
                new ArrayEntry<Object>("redirect_url"),
                new ArrayEntry<Object>("a"),
                new ArrayEntry<Object>("popuptitle"),
                new ArrayEntry<Object>("popupurl"),
                new ArrayEntry<Object>("text"),
                new ArrayEntry<Object>("trackback"),
                new ArrayEntry<Object>("pingback")));
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css_color(
            "classic",
            getIncluded(L10nPage.class, gVars, gConsts).__("Classic", "default"),
            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/css/colors-classic.css",
            new Array<Object>(new ArrayEntry<Object>("#07273E"), new ArrayEntry<Object>("#14568A"), new ArrayEntry<Object>("#D54E21"), new ArrayEntry<Object>("#2683AE")));
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css_color(
            "fresh",
            getIncluded(L10nPage.class, gVars, gConsts).__("Fresh", "default"),
            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/css/colors-fresh.css",
            new Array<Object>(new ArrayEntry<Object>("#464646"), new ArrayEntry<Object>("#CEE1EF"), new ArrayEntry<Object>("#D54E21"), new ArrayEntry<Object>("#2683AE")));
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("common", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("jquery-color", false, new Array<Object>(), false);
        gVars.editing = false;

        if (isset(gVars.webEnv._GET.getValue("page"))) {
            gVars.plugin_page = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("page")));
            gVars.plugin_page = getIncluded(PluginPage.class, gVars, gConsts).plugin_basename(gVars.plugin_page);
        }

        /* Condensed dynamic construct */
        require(gVars, gConsts, MenuPage.class);

        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_init", "");

        // Handle plugin admin pages.
        if (isset(gVars.plugin_page)) {
            gVars.page_hook = (((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).get_plugin_page_hook(gVars.plugin_page, gVars.pagenow);

            if (booleanval(gVars.page_hook)) {
                getIncluded(PluginPage.class, gVars, gConsts).do_action("load-" + gVars.page_hook, "");

                if (!isset(gVars.webEnv._GET.getValue("noheader"))) {
                    /* Condensed dynamic construct */
                    requireOnce(gVars, gConsts, Admin_headerPage.class);
                }

                getIncluded(PluginPage.class, gVars, gConsts).do_action(gVars.page_hook, "");
            } else {
                if (booleanval(getIncluded(FilePage.class, gVars, gConsts).validate_file(gVars.plugin_page, new Array<String>()))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Invalid plugin page", "default"), "");
                }

                // Modified by Numiton. TODO Add support for plugins
                if (false) {
                    if (!(FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + gVars.plugin_page) &&
                            FileSystemOrSocket.is_file(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + gVars.plugin_page))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(QStrings.sprintf(
                                getIncluded(L10nPage.class, gVars, gConsts).__("Cannot load %s.", "default"),
                                Strings.htmlentities(gVars.plugin_page)), "");
                    }
                }

                getIncluded(PluginPage.class, gVars, gConsts).do_action("load-" + gVars.plugin_page, "");

                if (!isset(gVars.webEnv._GET.getValue("noheader"))) {
                    /* Condensed dynamic construct */
                    requireOnce(gVars, gConsts, Admin_headerPage.class);
                }

                // TODO Add support for multiple plugins
                if (equal(gVars.plugin_page, "akismet")) {
                    include(gVars, gConsts, AkismetPage.class);
                }
            }

            /* Condensed dynamic construct */
            include(gVars, gConsts, Admin_footerPage.class);
            System.exit();
        } else if (isset(gVars.webEnv._GET.getValue("import"))) {
            importer = strval(gVars.webEnv._GET.getValue("import"));

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("import")) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to import.", "default"), "");
            }

            if (booleanval(getIncluded(FilePage.class, gVars, gConsts).validate_file(importer, new Array<String>()))) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Invalid importer.", "default"), "");
            }

            // Allow plugins to define importers as well
            if (!VarHandling.is_callable(new Callback(gVars.wp_importers.getArrayValue(importer).getArrayValue(2)))) {
                // Modified by Numiton. TODO Add support for additional importers
                if (false) {
                    if (!FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-admin/import/" + importer + ".php")) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cannot load importer.", "default"), "");
                    }
                }

                if (equal(importer, "blogger")) {
                    include(gVars, gConsts, BloggerPage.class);
                } else if (equal(importer, "blogware")) {
                    include(gVars, gConsts, BlogwarePage.class);
                } else if (equal(importer, "btt")) {
                    include(gVars, gConsts, BttPage.class);
                } else if (equal(importer, "dotclear")) {
                    include(gVars, gConsts, DotclearPage.class);
                } else if (equal(importer, "greymatter")) {
                    include(gVars, gConsts, GreymatterPage.class);
                } else if (equal(importer, "jkw")) {
                    include(gVars, gConsts, JkwPage.class);
                } else if (equal(importer, "livejournal")) {
                    include(gVars, gConsts, LivejournalPage.class);
                } else if (equal(importer, "mt")) {
                    include(gVars, gConsts, MtPage.class);
                } else if (equal(importer, "rss")) {
                    include(gVars, gConsts, RssPage.class);
                } else if (equal(importer, "stp")) {
                    include(gVars, gConsts, StpPage.class);
                } else if (equal(importer, "textpattern")) {
                    include(gVars, gConsts, TextpatternPage.class);
                } else if (equal(importer, "utw")) {
                    include(gVars, gConsts, UtwPage.class);
                } else if (equal(importer, "wordpress")) {
                    include(gVars, gConsts, WordpressPage.class);
                } else if (equal(importer, "wp-cat2tag")) {
                    include(gVars, gConsts, Wp_cat2tagPage.class);
                } else {
                    LOG.warn("Unsupported importer: " + importer);
                }
            }

            gVars.parent_file = "edit.php";
            gVars.submenu_file = "import.php";
            gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Import", "default");

            if (!isset(gVars.webEnv._GET.getValue("noheader"))) {
                /* Condensed dynamic construct */
                requireOnce(gVars, gConsts, Admin_headerPage.class);
            }

            requireOnce(gVars, gConsts, UpgradePage.class);
            gConsts.setWP_IMPORTING(true);
            FunctionHandling.call_user_func(new Callback(gVars.wp_importers.getArrayValue(importer).getArrayValue(2)));

            /* Condensed dynamic construct */
            include(gVars, gConsts, Admin_footerPage.class);
            System.exit();
        } else {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("load-" + gVars.pagenow, "");
        }

        return DEFAULT_VAL;
    }
}
