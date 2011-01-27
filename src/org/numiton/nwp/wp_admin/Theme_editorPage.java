/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Theme_editorPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Theme_editorPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Theme_editorPage.class.getName());
    public Array<Object> allowed_files = new Array<Object>();
    public Object file_show;
    public Array<Object> strip;
    public String desc_header;
    public Array<Object> a_theme = new Array<Object>();
    public String template_file;
    public Object template_show;
    public Object filedesc;
    public String style_file;
    public Object style_show;

    @Override
    @RequestMapping("/wp-admin/theme-editor.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/theme_editor";
    }

    public void theme_editor_css() {
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/theme-editor");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_theme_editor_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Themes", "default");
        gVars.parent_file = "themes.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this,
            new Array<Object>(new ArrayEntry<Object>("action"), new ArrayEntry<Object>("redirect"), new ArrayEntry<Object>("profile"), new ArrayEntry<Object>("error"),
                new ArrayEntry<Object>("warning"), new ArrayEntry<Object>("a"), new ArrayEntry<Object>("file"), new ArrayEntry<Object>("theme")));
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "theme_editor_css"), 10, 1);
        gVars.themes = getIncluded(ThemePage.class, gVars, gConsts).get_themes();

        if (empty(gVars.theme)) {
            gVars.theme = getIncluded(ThemePage.class, gVars, gConsts).get_current_theme();
        } else {
            gVars.theme = Strings.stripslashes(gVars.webEnv, gVars.theme);
        }

        if (!isset(gVars.themes.getValue(gVars.theme))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("The requested theme does not exist.", "default"), "");
        }

        allowed_files = Array.array_merge(gVars.themes.getArrayValue(gVars.theme).getArrayValue("Stylesheet Files"), gVars.themes.getArrayValue(gVars.theme).getArrayValue("Template Files"));

        if (empty(gVars.file)) {
            gVars.file = strval(allowed_files.getValue(0));
        }

        gVars.file = getIncluded(FilePage.class, gVars, gConsts).validate_file_to_edit(gVars.file, allowed_files);
        gVars.real_file = getIncluded(FilePage.class, gVars, gConsts).get_real_file_to_edit(gVars.file);
        file_show = FileSystemOrSocket.basename(gVars.file);

        {
            int javaSwitchSelector29 = 0;

            if (equal(gVars.action, "update")) {
                javaSwitchSelector29 = 1;
            }

            switch (javaSwitchSelector29) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("edit-theme_" + gVars.file + gVars.theme, "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_themes")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit templates for this blog.", "default") + "</p>",
                            "");
                }

                gVars.newcontent = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("newcontent")));
                gVars.theme = URL.urlencode(gVars.theme);

                if (FileSystemOrSocket.is_writeable(gVars.webEnv, gVars.real_file)) {
                    gVars.f = FileSystemOrSocket.fopen(gVars.webEnv, gVars.real_file, "w+");
                    FileSystemOrSocket.fwrite(gVars.webEnv, gVars.f, gVars.newcontent);
                    FileSystemOrSocket.fclose(gVars.webEnv, gVars.f);
                    gVars.location = "theme-editor.php?file=" + gVars.file + "&theme=" + gVars.theme + "&a=te";
                } else {
                    gVars.location = "theme-editor.php?file=" + gVars.file + "&theme=" + gVars.theme;
                }

                gVars.location = getIncluded(KsesPage.class, gVars, gConsts).wp_kses_no_null(gVars.location);
                strip = new Array<Object>(new ArrayEntry<Object>("%0d"), new ArrayEntry<Object>("%0a"));
                gVars.location = Strings.str_replace(strip, "", gVars.location);
                Network.header(gVars.webEnv, "Location: " + gVars.location);
                System.exit();

                break;
            }

            default: {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_themes")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die("<p>" +
                        getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit themes for this blog.", "default") + "</p>", "");
                }

                requireOnce(gVars, gConsts, Admin_headerPage.class);
                getIncluded(MiscPage.class, gVars, gConsts).update_recently_edited(gVars.file);

                if (!FileSystemOrSocket.is_file(gVars.webEnv, gVars.real_file)) {
                    gVars.error = 1;
                }

                if (!booleanval(gVars.error) && (FileSystemOrSocket.filesize(gVars.webEnv, gVars.real_file) > 0)) {
                    gVars.f = FileSystemOrSocket.fopen(gVars.webEnv, gVars.real_file, "r");
                    gVars.content = FileSystemOrSocket.fread(gVars.webEnv, gVars.f, FileSystemOrSocket.filesize(gVars.webEnv, gVars.real_file));
                    gVars.content = Strings.htmlspecialchars(gVars.content);
                }

                if (isset(gVars.webEnv._GET.getValue("a"))) {
                    echo(gVars.webEnv, " <div id=\"message\" class=\"updated fade\"><p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("File edited successfully.", "default");
                    echo(gVars.webEnv, "</p></div>\n");
                } else {
                }

                gVars.description = getIncluded(FilePage.class, gVars, gConsts).get_file_description(gVars.file);
                desc_header = ((!equal(gVars.description, file_show))
                    ? (gVars.description + "</strong> (%s)")
                    : "%s");
                echo(gVars.webEnv, "<div class=\"wrap\">\n<div class=\"bordertitle\">\n\t<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Theme Editor", "default");
                echo(gVars.webEnv, "</h2>\n\t<form id=\"themeselector\" name=\"theme\" action=\"theme-editor.php\" method=\"post\">\n\t\t<strong>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Select theme to edit:", "default");
                echo(gVars.webEnv, " </strong>\n\t\t<select name=\"theme\" id=\"theme\">\n");

                for (Map.Entry javaEntry301 : gVars.themes.entrySet()) {
                    a_theme = (Array<Object>) javaEntry301.getValue();
                    gVars.theme_name = strval(a_theme.getValue("Name"));

                    if (equal(gVars.theme_name, gVars.theme)) {
                        gVars.selected = " selected=\'selected\'";
                    } else {
                        gVars.selected = "";
                    }

                    gVars.theme_name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.theme_name);
                    echo(gVars.webEnv, "\n\t<option value=\"" + gVars.theme_name + "\" " + strval(gVars.selected) + ">" + gVars.theme_name + "</option>");
                }

                echo(gVars.webEnv, "\t\t</select>\n\t\t<input type=\"submit\" name=\"Submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Select", "default");
                echo(gVars.webEnv, "\" class=\"button\" />\n\t</form>\n</div>\n<div class=\"tablenav\">\n<div class=\"alignleft\">\n<big><strong>");
                echo(gVars.webEnv, QStrings.sprintf(desc_header, file_show));
                echo(gVars.webEnv, "</big>\n</div>\n<br class=\"clear\" />\n</div>\n<br class=\"clear\" />\n\t<div id=\"templateside\">\n\t<h3 id=\"bordertitle\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Theme Files", "default");
                echo(gVars.webEnv, "</h3>\n\n");

                if (booleanval(allowed_files)) {
                    echo(gVars.webEnv, "\t<h4>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Templates", "default");
                    echo(gVars.webEnv, "</h4>\n\t<ul>\n");

                    for (Map.Entry javaEntry302 : (Set<Map.Entry>) gVars.themes.getArrayValue(gVars.theme).getArrayValue("Template Files").entrySet()) {
                        template_file = strval(javaEntry302.getValue());
                        gVars.description = getIncluded(FilePage.class, gVars, gConsts).get_file_description(template_file);
                        template_show = FileSystemOrSocket.basename(template_file);
                        filedesc = ((!equal(gVars.description, template_file))
                            ? (gVars.description + " <span class=\'nonessential\'>(" + template_show + ")</span>")
                            : gVars.description);
                        filedesc = (equal(template_file, gVars.file)
                            ? ("<span class=\'highlight\'>" + gVars.description + " <span class=\'nonessential\'>(" + template_show + ")</span></span>")
                            : strval(filedesc));
                        echo(gVars.webEnv, "\t\t<li><a href=\"theme-editor.php?file=");
                        echo(gVars.webEnv, template_file);
                        echo(gVars.webEnv, "&amp;theme=");
                        echo(gVars.webEnv, URL.urlencode(gVars.theme));
                        echo(gVars.webEnv, "\">");
                        echo(gVars.webEnv, filedesc);
                        echo(gVars.webEnv, "</a></li>\n");
                    }

                    echo(gVars.webEnv, "\t</ul>\n\t<h4>");
                    echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts)._c("Styles|Theme stylesheets in theme editor", "default"));
                    echo(gVars.webEnv, "</h4>\n\t<ul>\n");

                    for (Map.Entry javaEntry303 : (Set<Map.Entry>) gVars.themes.getArrayValue(gVars.theme).getArrayValue("Stylesheet Files").entrySet()) {
                        style_file = strval(javaEntry303.getValue());
                        gVars.description = getIncluded(FilePage.class, gVars, gConsts).get_file_description(style_file);
                        style_show = FileSystemOrSocket.basename(style_file);
                        filedesc = ((!equal(gVars.description, style_file))
                            ? (gVars.description + " <span class=\'nonessential\'>(" + style_show + ")</span>")
                            : gVars.description);
                        filedesc = (equal(style_file, gVars.file)
                            ? ("<span class=\'highlight\'>" + gVars.description + " <span class=\'nonessential\'>(" + style_show + ")</span></span>")
                            : strval(filedesc));
                        echo(gVars.webEnv, "\t\t<li><a href=\"theme-editor.php?file=");
                        echo(gVars.webEnv, style_file);
                        echo(gVars.webEnv, "&amp;theme=");
                        echo(gVars.webEnv, URL.urlencode(gVars.theme));
                        echo(gVars.webEnv, "\">");
                        echo(gVars.webEnv, filedesc);
                        echo(gVars.webEnv, "</a></li>\n");
                    }

                    echo(gVars.webEnv, "\t</ul>\n");
                } else {
                }

                echo(gVars.webEnv, "</div>\n\t");

                if (!booleanval(gVars.error)) {
                    echo(gVars.webEnv, "\t<form name=\"template\" id=\"template\" action=\"theme-editor.php\" method=\"post\">\n\t");
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("edit-theme_" + gVars.file + gVars.theme, "_wpnonce", true, true);
                    echo(gVars.webEnv, "\t\t <div><textarea cols=\"70\" rows=\"25\" name=\"newcontent\" id=\"newcontent\" tabindex=\"1\">");
                    echo(gVars.webEnv, gVars.content);
                    echo(gVars.webEnv, "</textarea>\n\t\t <input type=\"hidden\" name=\"action\" value=\"update\" />\n\t\t <input type=\"hidden\" name=\"file\" value=\"");
                    echo(gVars.webEnv, gVars.file);
                    echo(gVars.webEnv, "\" />\n\t\t <input type=\"hidden\" name=\"theme\" value=\"");
                    echo(gVars.webEnv, gVars.theme);
                    echo(gVars.webEnv, "\" />\n\t\t </div>\n\n\t\t<div>\n");

                    if (FileSystemOrSocket.is_writeable(gVars.webEnv, gVars.real_file)) {
                        echo(gVars.webEnv, "\t\t\t<p class=\"submit\">\n");
                        echo(gVars.webEnv, "<input type=\'submit\' name=\'submit\' value=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Update File", "default") + "\' tabindex=\'2\' />");
                        echo(gVars.webEnv, "</p>\n");
                    } else {
                        echo(gVars.webEnv, "<p><em>");
                        getIncluded(L10nPage.class, gVars, gConsts)._e(
                                "You need to make this file writable before you can save your changes. See <a href=\"http://codex.wordpress.org/Changing_File_Permissions\">the Codex</a> for more information.",
                                "default");
                        echo(gVars.webEnv, "</em></p>\n");
                    }

                    echo(gVars.webEnv, "\t\t</div>\n\t</form>\n\t");
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
