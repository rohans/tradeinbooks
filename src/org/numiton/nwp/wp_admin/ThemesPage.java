/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ThemesPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.Ref;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QArray;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class ThemesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ThemesPage.class.getName());
    public Ref<Array<Object>> theme_names = new Ref(new Array<Object>());
    public String stylesheet;
    public Object version;
    public Object screenshot;
    public Object stylesheet_dir;
    public Object activate_link;
    public Array<Object> broken_themes = new Array<Object>();

    @Override
    @RequestMapping("/wp-admin/themes.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/themes";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_themes_block1");
        gVars.webEnv = webEnv;

        requireOnce(gVars, gConsts, AdminPage.class);

        if (isset(gVars.webEnv._GET.getValue("action"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("switch-theme_" + strval(gVars.webEnv._GET.getValue("template")), "_wpnonce");

            if (equal("activate", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(ThemePage.class, gVars, gConsts).switch_theme(strval(gVars.webEnv._GET.getValue("template")), strval(gVars.webEnv._GET.getValue("stylesheet")));
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("themes.php?activated=true", 302);
                System.exit();
            }
        }

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Manage Themes", "default");
        gVars.parent_file = "themes.php";
        requireOnce(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_themes_block2");

        if (!getIncluded(ThemePage.class, gVars, gConsts).validate_current_theme()) {
            echo(gVars.webEnv, "<div id=\"message1\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("The active theme is broken.  Reverting to the default theme.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else if (isset(gVars.webEnv._GET.getValue("activated"))) {
            echo(gVars.webEnv, "<div id=\"message2\" class=\"updated fade\"><p>");
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("New theme activated. <a href=\"%s\">Visit site</a>", "default"),
                getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "/");
            echo(gVars.webEnv, "</p></div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_themes_block3");
        gVars.themes = getIncluded(ThemePage.class, gVars, gConsts).get_themes();
        gVars.ct = (((org.numiton.nwp.wp_admin.includes.ThemePage) getIncluded(org.numiton.nwp.wp_admin.includes.ThemePage.class, gVars, gConsts))).current_theme_info();

        /* Start of block */
        super.startBlock("__wp_admin_themes_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Current Theme", "default");

        /* Start of block */
        super.startBlock("__wp_admin_themes_block5");

        if (booleanval(StdClass.getValue(gVars.ct, "screenshot"))) {
            echo(gVars.webEnv, "<img src=\"");
            echo(
                gVars.webEnv,
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/" + StdClass.getValue(gVars.ct, "stylesheet_dir") + "/" + StdClass.getValue(gVars.ct, "screenshot"));
            echo(gVars.webEnv, "\" alt=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Current theme preview", "default");
            echo(gVars.webEnv, "\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_themes_block6");
        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s %2$s by %3$s|1: theme title, 2: theme version, 3: theme author", "default"),
            StdClass.getValue(gVars.ct, "title"), StdClass.getValue(gVars.ct, "version"), StdClass.getValue(gVars.ct, "author"));

        /* Start of block */
        super.startBlock("__wp_admin_themes_block7");
        echo(gVars.webEnv, StdClass.getValue(gVars.ct, "description"));

        /* Start of block */
        super.startBlock("__wp_admin_themes_block8");

        if (booleanval(StdClass.getValue(gVars.ct, "parent_theme"))) {
            echo(gVars.webEnv, "\t<p>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "The template files are located in <code>%2$s</code>.  The stylesheet files are located in <code>%3$s</code>.  <strong>%4$s</strong> uses templates from <strong>%5$s</strong>.  Changes made to the templates will affect both themes.",
                            "default"),
                    StdClass.getValue(gVars.ct, "title"),
                    StdClass.getValue(gVars.ct, "template_dir"),
                    StdClass.getValue(gVars.ct, "stylesheet_dir"),
                    StdClass.getValue(gVars.ct, "title"),
                    StdClass.getValue(gVars.ct, "parent_theme"));
            echo(gVars.webEnv, "</p>\n");
        } else {
            echo(gVars.webEnv, "\t<p>");
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("All of this theme&#8217;s files are located in <code>%2$s</code>.", "default"),
                StdClass.getValue(gVars.ct, "title"), StdClass.getValue(gVars.ct, "template_dir"), StdClass.getValue(gVars.ct, "stylesheet_dir"));
            echo(gVars.webEnv, "</p>\n");
        }

        if (booleanval(StdClass.getValue(gVars.ct, "tags"))) {
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Tags:", "default");
            echo(gVars.webEnv, " ");
            echo(gVars.webEnv, Strings.join(", ", gVars.ct.fields.getArrayValue("tags")));
            echo(gVars.webEnv, "</p>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_themes_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Available Themes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_themes_block10");

        if (1 < Array.count(gVars.themes)) {
            echo(gVars.webEnv, "\n");
            gVars.style = "";
            theme_names.value = Array.array_keys(gVars.themes);
            QArray.natcasesort(theme_names);

            for (Map.Entry javaEntry304 : theme_names.value.entrySet()) {
                gVars.theme_name = strval(javaEntry304.getValue());

                if (equal(gVars.theme_name, StdClass.getValue(gVars.ct, "name"))) {
                    continue;
                }

                gVars.template = gVars.themes.getArrayValue(gVars.theme_name).getValue("Template");
                stylesheet = strval(gVars.themes.getArrayValue(gVars.theme_name).getValue("Stylesheet"));
                gVars.title = strval(gVars.themes.getArrayValue(gVars.theme_name).getValue("Title"));
                version = intval(gVars.themes.getArrayValue(gVars.theme_name).getValue("Version"));
                gVars.description = strval(gVars.themes.getArrayValue(gVars.theme_name).getValue("Description"));
                gVars.author = strval(gVars.themes.getArrayValue(gVars.theme_name).getValue("Author"));
                screenshot = gVars.themes.getArrayValue(gVars.theme_name).getValue("Screenshot");
                stylesheet_dir = gVars.themes.getArrayValue(gVars.theme_name).getValue("Stylesheet Dir");
                gVars.tags = gVars.themes.getArrayValue(gVars.theme_name).getArrayValue("Tags");
                activate_link = getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url(
                            "themes.php?action=activate&amp;template=" + URL.urlencode(strval(gVars.template)) + "&amp;stylesheet=" + URL.urlencode(stylesheet),
                            "switch-theme_" + strval(gVars.template));
                echo(gVars.webEnv, "<div class=\"available-theme\">\n<h3><a href=\"");
                echo(gVars.webEnv, activate_link);
                echo(gVars.webEnv, "\">");
                echo(gVars.webEnv, gVars.title);
                echo(gVars.webEnv, "</a></h3>\n\n<a href=\"");
                echo(gVars.webEnv, activate_link);
                echo(gVars.webEnv, "\" class=\"screenshot\">\n");

                if (booleanval(screenshot)) {
                    echo(gVars.webEnv, "<img src=\"");
                    echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/" + strval(stylesheet_dir) + "/" + strval(screenshot));
                    echo(gVars.webEnv, "\" alt=\"\" />\n");
                } else {
                }

                echo(gVars.webEnv, "</a>\n\n<p>");
                echo(gVars.webEnv, gVars.description);
                echo(gVars.webEnv, "</p>\n");

                if (booleanval(gVars.tags)) {
                    echo(gVars.webEnv, "<p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Tags:", "default");
                    echo(gVars.webEnv, " ");
                    echo(gVars.webEnv, Strings.join(", ", gVars.tags));
                    echo(gVars.webEnv, "</p>\n");
                } else {
                }

                echo(gVars.webEnv, "</div>\n");
            }

            echo(gVars.webEnv, "\n");
        } // end foreach theme_names

        /* Start of block */
        super.startBlock("__wp_admin_themes_block11");
        
        // List broken themes, if any.
        broken_themes = (((org.numiton.nwp.wp_admin.includes.ThemePage) getIncluded(org.numiton.nwp.wp_admin.includes.ThemePage.class, gVars, gConsts))).get_broken_themes();

        if (booleanval(Array.count(broken_themes))) {
            echo(gVars.webEnv, "\n<h2>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Broken Themes", "default");
            echo(gVars.webEnv, "</h2>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("The following themes are installed but incomplete.  Themes must have a stylesheet and a template.", "default");
            echo(gVars.webEnv, "</p>\n\n<table width=\"100%\" cellpadding=\"3\" cellspacing=\"3\">\n\t<tr>\n\t\t<th>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");
            echo(gVars.webEnv, "</th>\n\t\t<th>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Description", "default");
            echo(gVars.webEnv, "</th>\n\t</tr>\n");
            gVars.theme = "";
            theme_names.value = Array.array_keys(broken_themes);
            QArray.natcasesort(theme_names);

            for (Map.Entry javaEntry305 : theme_names.value.entrySet()) {
                gVars.theme_name = strval(javaEntry305.getValue());
                gVars.title = strval(broken_themes.getArrayValue(gVars.theme_name).getValue("Title"));
                gVars.description = strval(broken_themes.getArrayValue(gVars.theme_name).getValue("Description"));
                gVars.theme = (equal("class=\"alternate\"", gVars.theme)
                    ? ""
                    : "class=\"alternate\"");
                echo(gVars.webEnv, "\n\t\t<tr " + gVars.theme + ">\n\t\t\t <td>" + gVars.title + "</td>\n\t\t\t <td>" + gVars.description + "</td>\n\t\t</tr>");
            }

            echo(gVars.webEnv, "</table>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_themes_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Get More Themes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_themes_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "You can find additional themes for your site in the <a href=\"http://wordpress.org/extend/themes/\">nWordPress theme directory</a>. To install a theme you generally just need to upload the theme folder into your <code>wp-content/themes</code> directory. Once a theme is uploaded, you should see it on this page.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_themes_block14");
        require(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
