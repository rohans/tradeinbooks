/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PluginsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.PluginPage;
import org.numiton.nwp.wp_content.plugins.akismet.AkismetPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Options;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class PluginsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PluginsPage.class.getName());
    public Object valid;
    public Object active;
    public Object inactive;
    public Array<String> action_links = new Array<String>();
    public Array<Object> plugins_allowedtags;
    public Object plugin_info;
    public Object toggle;

    @Override
    @RequestMapping("/wp-admin/plugins.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/plugins";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_plugins_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        if (isset(gVars.webEnv._GET.getValue("action"))) {
            if (isset(gVars.webEnv._GET.getValue("plugin"))) {
                gVars.plugin = Strings.trim(strval(gVars.webEnv._GET.getValue("plugin")));
            }

            if (equal("activate", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("activate-plugin_" + strval(gVars.webEnv._GET.getValue("plugin")), "_wpnonce");
                gVars.result = getIncluded(PluginPage.class, gVars, gConsts).activate_plugin(strval(gVars.webEnv._GET.getValue("plugin")), "plugins.php?error=true&plugin=" + gVars.plugin);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.result)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(((WP_Error) gVars.result).get_error_message(), "");
                }

                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugins.php?activate=true", 302); // overrides the ?error=true one above
            } else if (equal("error_scrape", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("plugin-activation-error_" + gVars.plugin, "_wpnonce");
                valid = getIncluded(PluginPage.class, gVars, gConsts).validate_plugin(gVars.plugin);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(valid)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(valid, "");
                }

                ErrorHandling.error_reporting(gVars.webEnv, ErrorHandling.E_ALL ^ ErrorHandling.E_NOTICE);
                Options.ini_set(gVars.webEnv, "display_errors", strval(true)); //Ensure that Fatal errors are displayed.

                // TODO Add support for multiple plugins
                if (equal(gVars.plugin, "akismet")) {
                    include(gVars, gConsts, AkismetPage.class);
                }
            } else if (equal("deactivate", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("deactivate-plugin_" + strval(gVars.webEnv._GET.getValue("plugin")), "_wpnonce");
                getIncluded(PluginPage.class, gVars, gConsts).deactivate_plugins(gVars.webEnv._GET.getValue("plugin"), false);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugins.php?deactivate=true", 302);
            } else if (equal("deactivate-all", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("deactivate-all", "_wpnonce");
                getIncluded(PluginPage.class, gVars, gConsts).deactivate_all_plugins();
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugins.php?deactivate-all=true", 302);
            } else if (equal("reactivate-all", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("reactivate-all", "_wpnonce");
                getIncluded(PluginPage.class, gVars, gConsts).reactivate_all_plugins("plugins.php?errors=true");
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugins.php?reactivate-all=true", 302); // overrides the ?error=true one above
            }

            System.exit();
        }

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Manage Plugins", "default");
        requireOnce(gVars, gConsts, Admin_headerPage.class);
        getIncluded(PluginPage.class, gVars, gConsts).validate_active_plugins();

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block2");

        if (isset(gVars.webEnv._GET.getValue("error"))) {
            echo(gVars.webEnv, "\t<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin could not be activated because it triggered a <strong>fatal error</strong>.", "default");
            echo(gVars.webEnv, "</p>\n\t");
            gVars.plugin = Strings.trim(strval(gVars.webEnv._GET.getValue("plugin")));

            if (booleanval(getIncluded(PluggablePage.class, gVars, gConsts).wp_verify_nonce(gVars.webEnv._GET.getValue("_error_nonce"), "plugin-activation-error_" + gVars.plugin))) {
                echo(gVars.webEnv, "\t<iframe style=\"border:0\" width=\"100%\" height=\"70px\" src=\"");
                getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
                echo(gVars.webEnv, "/wp-admin/plugins.php?action=error_scrape&amp;plugin=");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.plugin));
                echo(gVars.webEnv, "&amp;_wpnonce=");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("_error_nonce"))));
                echo(gVars.webEnv, "\"></iframe>\n\t");
            }

            echo(gVars.webEnv, "\t</div>\n");
        } else if (isset(gVars.webEnv._GET.getValue("errors"))) {
            echo(gVars.webEnv, "\t<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Some plugins could not be reactivated because they triggered a <strong>fatal error</strong>.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else if (isset(gVars.webEnv._GET.getValue("activate"))) {
            echo(gVars.webEnv, "\t<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin <strong>activated</strong>.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else if (isset(gVars.webEnv._GET.getValue("deactivate"))) {
            echo(gVars.webEnv, "\t<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin <strong>deactivated</strong>.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else if (isset(gVars.webEnv._GET.getValue("deactivate-all"))) {
            echo(gVars.webEnv, "\t<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("All plugins <strong>deactivated</strong>.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else if (isset(gVars.webEnv._GET.getValue("reactivate-all"))) {
            echo(gVars.webEnv, "\t<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Plugins <strong>reactivated</strong>.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block3");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin Management", "default");

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Plugins extend and expand the functionality of nWordPress. Once a plugin is installed, you may activate it or deactivate it here.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block5");
        gVars.plugins = getIncluded(PluginPage.class, gVars, gConsts).get_plugins("");

        if (empty(gVars.plugins))/*
         * TODO: make more helpful
         */
         {
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Couldn&#8217;t open plugins directory or there are no plugins available.", "default");
            echo(gVars.webEnv, "</p>");
        } else {
            echo(gVars.webEnv, "\n<div class=\"tablenav\">\n\t<div class=\"alignleft\">\n\t");
            active = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");
            inactive = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("deactivated_plugins");

            if (!empty(active)) {
                echo(gVars.webEnv, "\t<a class=\"button-secondary\" href=\"");
                echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("plugins.php?action=deactivate-all", "deactivate-all"));
                echo(gVars.webEnv, "\" class=\"delete\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Deactivate All Plugins", "default");
                echo(gVars.webEnv, "</a>\n\t");
            } else if (empty(active) && !empty(inactive)) {
                echo(gVars.webEnv, "\t<a class=\"button-secondary\" href=\"");
                echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("plugins.php?action=reactivate-all", "reactivate-all"));
                echo(gVars.webEnv, "\" class=\"delete\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Reactivate Plugins", "default");
                echo(gVars.webEnv, "</a>\n\t");
            } // endif active/inactive plugin check

            echo(gVars.webEnv, "\t</div>\n\t<br class=\"clear\" />\n</div>\n\n<br class=\"clear\" />\n\n<table class=\"widefat\">\n\t<thead>\n\t<tr>\n\t\t<th>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin", "default");
            echo(gVars.webEnv, "</th>\n\t\t<th class=\"num\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Version", "default");
            echo(gVars.webEnv, "</th>\n\t\t<th>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Description", "default");
            echo(gVars.webEnv, "</th>\n\t\t<th class=\"status\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Status", "default");
            echo(gVars.webEnv, "</th>\n\t\t<th class=\"action-links\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Action", "default");
            echo(gVars.webEnv, "</th>\n\t</tr>\n\t</thead>\n\t<tbody id=\"plugins\">\n");

            for (Map.Entry javaEntry299 : gVars.plugins.entrySet()) {
                gVars.plugin_file = javaEntry299.getKey();
                gVars.plugin_data = (Array<Object>) javaEntry299.getValue();
                action_links = new Array<String>();
                gVars.style = "";

                if (getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(gVars.plugin_file)) {
                    action_links.putValue(
                            "<a href=\'" +
                            getIncluded(FunctionsPage.class, gVars, gConsts)
                                .wp_nonce_url("plugins.php?action=deactivate&amp;plugin=" + strval(gVars.plugin_file), "deactivate-plugin_" + strval(gVars.plugin_file)) + "\' title=\'" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("Deactivate this plugin", "default") + "\' class=\'delete\'>" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("Deactivate", "default") + "</a>");
                    gVars.style = "active";
                } else {
                    action_links.putValue(
                            "<a href=\'" +
                            getIncluded(FunctionsPage.class, gVars, gConsts)
                                .wp_nonce_url("plugins.php?action=activate&amp;plugin=" + strval(gVars.plugin_file), "activate-plugin_" + strval(gVars.plugin_file)) + "\' title=\'" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("Activate this plugin", "default") + "\' class=\'edit\'>" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("Activate", "default") + "</a>");
                }

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_plugins") &&
                        FileSystemOrSocket.is_writable(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.plugin_file))) {
                    action_links.putValue(
                            "<a href=\'plugin-editor.php?file=" + strval(gVars.plugin_file) + "\' title=\'" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("Open this file in the Plugin Editor", "default") + "\' class=\'edit\'>" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default") + "</a>");
                }

                plugins_allowedtags = new Array<Object>(
                        new ArrayEntry<Object>("a", new Array<Object>(new ArrayEntry<Object>("href", new Array<Object>()), new ArrayEntry<Object>("title", new Array<Object>()))),
                        new ArrayEntry<Object>("abbr", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                        new ArrayEntry<Object>("acronym", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                        new ArrayEntry<Object>("code", new Array<Object>()),
                        new ArrayEntry<Object>("em", new Array<Object>()),
                        new ArrayEntry<Object>("strong", new Array<Object>()));
                
                // Sanitize all displayed data
                gVars.plugin_data.putValue(
                    "Title",
                    getIncluded(KsesPage.class, gVars, gConsts).wp_kses(strval(gVars.plugin_data.getValue("Title")), plugins_allowedtags));
                gVars.plugin_data.putValue(
                    "Version",
                    getIncluded(KsesPage.class, gVars, gConsts).wp_kses(strval(gVars.plugin_data.getValue("Version")), plugins_allowedtags));
                gVars.plugin_data.putValue(
                    "Description",
                    getIncluded(KsesPage.class, gVars, gConsts).wp_kses(strval(gVars.plugin_data.getValue("Description")), plugins_allowedtags));
                gVars.plugin_data.putValue(
                    "Author",
                    getIncluded(KsesPage.class, gVars, gConsts).wp_kses(strval(gVars.plugin_data.getValue("Author")), plugins_allowedtags));
                gVars.author = (empty(gVars.plugin_data.getValue("Author"))
                    ? ""
                    : (" <cite>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("By %s", "default"), gVars.plugin_data.getValue("Author")) + ".</cite>"));

                if (!equal(gVars.style, "")) {
                    gVars.style = " class=\"" + gVars.style + "\"";
                }

                action_links = (Array<String>) (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters(
                        "plugin_action_links",
                        action_links,
                        gVars.plugin_file,
                        plugin_info);
                echo(
                        gVars.webEnv,
                        "\n\t<tr" + gVars.style + ">\n\t\t<td class=\'name\'>" + strval(gVars.plugin_data.getValue("Title")) + "</td>\n\t\t<td class=\'vers\'>" +
                        strval(gVars.plugin_data.getValue("Version")) + "</td>\n\t\t<td class=\'desc\'><p>" + strval(gVars.plugin_data.getValue("Description")) + gVars.author +
                        "</p></td>\n\t\t<td class=\'status\'>");

                if (getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(gVars.plugin_file)) {
                    echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("<span class=\"active\">Active</span>", "default"));
                } else {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("<span class=\"inactive\">Inactive</span>", "default");
                }

                echo(gVars.webEnv, "</td>\n\t\t<td class=\'togl action-links\'>" + strval(toggle));

                if (!empty(action_links)) {
                    echo(gVars.webEnv, Strings.implode(" | ", action_links));
                }

                echo(gVars.webEnv, "</td> \n\t</tr>");
                (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("after_plugin_row", gVars.plugin_file);
            }

            echo(gVars.webEnv, "\t</tbody>\n</table>\n\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block6");
        QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "If something goes wrong with a plugin and you can&#8217;t use nWordPress, delete or rename that file in the <code>%s</code> directory and it will be automatically deactivated.",
                        "default"),
                gConsts.getPLUGINDIR());

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Get More Plugins", "default");

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block8");
        getIncluded(L10nPage.class, gVars, gConsts)
            ._e("You can find additional plugins for your site in the <a href=\"http://wordpress.org/extend/plugins/\">nWordPress plugin directory</a>.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block9");
        QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "To install a plugin you generally just need to upload the plugin file into your <code>%s</code> directory. Once a plugin is uploaded, you may activate it here.",
                        "default"),
                gConsts.getPLUGINDIR());

        /* Start of block */
        super.startBlock("__wp_admin_plugins_block10");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
