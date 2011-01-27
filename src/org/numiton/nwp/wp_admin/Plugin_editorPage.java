/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Plugin_editorPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_admin.includes.PluginPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
public class Plugin_editorPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Plugin_editorPage.class.getName());
    public Array<Object> plugin_files = new Array<Object>();

    @Override
    @RequestMapping("/wp-admin/plugin-editor.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/plugin_editor";
    }

    public void theme_editor_css() {
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/theme-editor");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_plugin_editor_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Plugins", "default");
        gVars.parent_file = "plugins.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this,
            new Array<Object>(new ArrayEntry<Object>("action"), new ArrayEntry<Object>("redirect"), new ArrayEntry<Object>("profile"), new ArrayEntry<Object>("error"),
                new ArrayEntry<Object>("warning"), new ArrayEntry<Object>("a"), new ArrayEntry<Object>("file")));
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_head", Callback.createCallbackArray(this, "theme_editor_css"), 10, 1);
        gVars.plugins = getIncluded(PluginPage.class, gVars, gConsts).get_plugins("");
        plugin_files = Array.array_keys(gVars.plugins);

        if (empty(gVars.file)) {
            gVars.file = strval(plugin_files.getValue(0));
        }

        gVars.file = getIncluded(FilePage.class, gVars, gConsts).validate_file_to_edit(gVars.file, plugin_files);
        gVars.real_file = getIncluded(FilePage.class, gVars, gConsts).get_real_file_to_edit(gConsts.getPLUGINDIR() + "/" + gVars.file);

        {
            int javaSwitchSelector26 = 0;

            if (equal(gVars.action, "update")) {
                javaSwitchSelector26 = 1;
            }

            switch (javaSwitchSelector26) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("edit-plugin_" + gVars.file, "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_plugins")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit templates for this blog.", "default") + "</p>",
                            "");
                }

                gVars.newcontent = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("newcontent")));

                if (FileSystemOrSocket.is_writeable(gVars.webEnv, gVars.real_file)) {
                    gVars.f = FileSystemOrSocket.fopen(gVars.webEnv, gVars.real_file, "w+");
                    FileSystemOrSocket.fwrite(gVars.webEnv, gVars.f, gVars.newcontent);
                    FileSystemOrSocket.fclose(gVars.webEnv, gVars.f);

                    // Deactivate so we can test it.
                    if (getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(gVars.file) || isset(gVars.webEnv._POST.getValue("phperror"))) {
                        if (getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(gVars.file)) {
                            getIncluded(PluginPage.class, gVars, gConsts).deactivate_plugins(gVars.file, true);
                        }

                        getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                                "_wpnonce",
                                getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("edit-plugin-test_" + gVars.file),
                                "plugin-editor.php?file=" + gVars.file + "&liveupdate=1"), 302);
                        System.exit();
                    }

                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugin-editor.php?file=" + gVars.file + "&a=te", 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugin-editor.php?file=" + gVars.file, 302);
                }

                System.exit();

                break;
            }

            default: {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_plugins")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die("<p>" +
                        getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit plugins for this blog.", "default") + "</p>", "");
                }

                if (isset(gVars.webEnv._GET.getValue("liveupdate")))/*
                 * we'll
                 * override
                 * this
                 * later
                 * if
                 * the
                 * plugin
                 * can
                 * be
                 * included
                 * without
                 * fatal
                 * error
                 * we'll
                 * override
                 * this
                 * later
                 * if
                 * the
                 * plugin
                 * can
                 * be
                 * included
                 * without
                 * fatal
                 * error
                 */
                 {
                    getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("edit-plugin-test_" + gVars.file, "_wpnonce");
                    gVars.error = getIncluded(PluginPage.class, gVars, gConsts).validate_plugin(gVars.file);

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.error)) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(gVars.error, "");
                    }

                    if (!getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(gVars.file)) {
                        getIncluded(PluginPage.class, gVars, gConsts).activate_plugin(gVars.file, "plugin-editor.php?file=" + gVars.file + "&phperror=1");
                    }

                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("plugin-editor.php?file=" + gVars.file + "&a=te", 302);
                    System.exit();
                }

                requireOnce(gVars, gConsts, Admin_headerPage.class);
                getIncluded(MiscPage.class, gVars, gConsts).update_recently_edited(gConsts.getPLUGINDIR() + "/" + gVars.file);

                if (!FileSystemOrSocket.is_file(gVars.webEnv, gVars.real_file)) {
                    gVars.error = 1;
                }

                if (!booleanval(gVars.error)) {
                    gVars.content = Strings.htmlspecialchars(FileSystemOrSocket.file_get_contents(gVars.webEnv, gVars.real_file));
                }

                if (isset(gVars.webEnv._GET.getValue("a"))) {
                    echo(gVars.webEnv, " <div id=\"message\" class=\"updated fade\"><p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("File edited successfully.", "default");
                    echo(gVars.webEnv, "</p></div>\n");
                } else if (isset(gVars.webEnv._GET.getValue("phperror"))) {
                    echo(gVars.webEnv, " <div id=\"message\" class=\"updated fade\"><p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("This plugin has been deactivated because your changes resulted in a <strong>fatal error</strong>.", "default");
                    echo(gVars.webEnv, "</p>\n\t");

                    if (booleanval(getIncluded(PluggablePage.class, gVars, gConsts).wp_verify_nonce(gVars.webEnv._GET.getValue("_error_nonce"), "plugin-activation-error_" + gVars.file))) {
                        echo(gVars.webEnv, "\t<iframe style=\"border:0\" width=\"100%\" height=\"70px\" src=\"");
                        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
                        echo(gVars.webEnv, "/wp-admin/plugins.php?action=error_scrape&amp;plugin=");
                        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.file));
                        echo(gVars.webEnv, "&amp;_wpnonce=");
                        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("_error_nonce"))));
                        echo(gVars.webEnv, "\"></iframe>\n\t");
                    }

                    echo(gVars.webEnv, "</div>\n");
                } else {
                }

                echo(gVars.webEnv, " <div class=\"wrap\">\n<div class=\"bordertitle\">\n\t<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin Editor", "default");
                echo(gVars.webEnv, "</h2>\n</div>\n<div class=\"tablenav\">\n<div class=\"alignleft\">\n<big><strong>");

                if (getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(gVars.file)) {
                    if (FileSystemOrSocket.is_writeable(gVars.webEnv, gVars.real_file)) {
                        echo(gVars.webEnv, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Editing <strong>%s</strong> (active)", "default"), gVars.file));
                    } else {
                        echo(gVars.webEnv, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Browsing <strong>%s</strong> (active)", "default"), gVars.file));
                    }
                } else {
                    if (FileSystemOrSocket.is_writeable(gVars.webEnv, gVars.real_file)) {
                        echo(gVars.webEnv, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Editing <strong>%s</strong> (inactive)", "default"), gVars.file));
                    } else {
                        echo(gVars.webEnv, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Browsing <strong>%s</strong> (inactive)", "default"), gVars.file));
                    }
                }

                echo(gVars.webEnv, "</strong></big>\n</div>\n<br class=\"clear\" />\n</div>\n<br class=\"clear\" />\n\t<div id=\"templateside\">\n\t<h3 id=\"bordertitle\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin Files", "default");
                echo(gVars.webEnv, "</h3>\n\n\t<h4>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Plugins", "default");
                echo(gVars.webEnv, "</h4>\n\t<ul>\n");

                for (Map.Entry javaEntry298 : plugin_files.entrySet()) {
                    gVars.plugin_file = javaEntry298.getValue();
                    echo(gVars.webEnv, "\t\t<li><a href=\"plugin-editor.php?file=");
                    echo(gVars.webEnv, gVars.plugin_file);
                    echo(gVars.webEnv, "\">");
                    echo(gVars.webEnv, gVars.plugins.getArrayValue(gVars.plugin_file).getValue("Name"));
                    echo(gVars.webEnv, "</a></li>\n");
                }

                echo(gVars.webEnv, "\t</ul>\n\t</div>\n");

                if (!booleanval(gVars.error)) {
                    echo(gVars.webEnv, "\t<form name=\"template\" id=\"template\" action=\"plugin-editor.php\" method=\"post\">\n\t");
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("edit-plugin_" + gVars.file, "_wpnonce", true, true);
                    echo(gVars.webEnv, "\t\t<div><textarea cols=\"70\" rows=\"25\" name=\"newcontent\" id=\"newcontent\" tabindex=\"1\">");
                    echo(gVars.webEnv, gVars.content);
                    echo(gVars.webEnv, "</textarea>\n\t\t<input type=\"hidden\" name=\"action\" value=\"update\" />\n\t\t<input type=\"hidden\" name=\"file\" value=\"");
                    echo(gVars.webEnv, gVars.file);
                    echo(gVars.webEnv, "\" />\n\t\t</div>\n");

                    if (FileSystemOrSocket.is_writeable(gVars.webEnv, gVars.real_file)) {
                        echo(gVars.webEnv, "\t");

                        if (Array.in_array(gVars.file, new Array<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins")))) {
                            echo(gVars.webEnv, "\t\t<p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e(
                                    "<strong>Warning:</strong> Making changes to active plugins is not recommended.  If your changes cause a fatal error, the plugin will be automatically deactivated.",
                                    "default");
                            echo(gVars.webEnv, "</p>\n\t");
                        }

                        echo(gVars.webEnv, "\t<p class=\"submit\">\n\t");

                        if (isset(gVars.webEnv._GET.getValue("phperror"))) {
                            echo(
                                    gVars.webEnv,
                                    "<input type=\'hidden\' name=\'phperror\' value=\'1\' /><input type=\'submit\' name=\'submit\' value=\'" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("Update File and Attempt to Reactivate", "default") + "\' tabindex=\'2\' />");
                        } else {
                            echo(gVars.webEnv, "<input type=\'submit\' name=\'submit\' value=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Update File", "default") + "\' tabindex=\'2\' />");
                        }

                        echo(gVars.webEnv, "\t</p>\n");
                    } else {
                        echo(gVars.webEnv, "\t<p><em>");
                        getIncluded(L10nPage.class, gVars, gConsts)._e(
                                "You need to make this file writable before you can save your changes. See <a href=\"http://codex.wordpress.org/Changing_File_Permissions\">the Codex</a> for more information.",
                                "default");
                        echo(gVars.webEnv, "</em></p>\n");
                    }

                    echo(gVars.webEnv, " </form>\n");
                } else {
                    echo(
                        gVars.webEnv,
                        "<div class=\"error\"><p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Oops, no such file exists! Double check the name and try again, merci.", "default") +
                        "</p></div>");
                }

                echo(gVars.webEnv, "<div class=\"clear\"> &nbsp; </div>\n</div>\n");

                break;
            }
            }
        }

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
