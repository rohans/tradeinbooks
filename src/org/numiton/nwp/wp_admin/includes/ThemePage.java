/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ThemePage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/ThemePage")
@Scope("request")
public class ThemePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ThemePage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/theme.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/theme";
    }

    public StdClass current_theme_info() {
        Array<Array<Object>> themes = new Array<Array<Object>>();
        String current_theme = null;
        StdClass ct = new StdClass();

        // Added by Numiton
        themes = (((org.numiton.nwp.wp_includes.ThemePage) getIncluded(org.numiton.nwp.wp_includes.ThemePage.class, gVars, gConsts))).get_themes();
        current_theme = (((org.numiton.nwp.wp_includes.ThemePage) getIncluded(org.numiton.nwp.wp_includes.ThemePage.class, gVars, gConsts))).get_current_theme();
        ct.fields.putValue("name", current_theme);
        ct.fields.putValue("title", themes.getArrayValue(current_theme).getValue("Title"));
        ct.fields.putValue("version", themes.getArrayValue(current_theme).getValue("Version"));
        ct.fields.putValue("parent_theme", themes.getArrayValue(current_theme).getValue("Parent Theme"));
        ct.fields.putValue("template_dir", themes.getArrayValue(current_theme).getValue("Template Dir"));
        ct.fields.putValue("stylesheet_dir", themes.getArrayValue(current_theme).getValue("Stylesheet Dir"));
        ct.fields.putValue("template", themes.getArrayValue(current_theme).getValue("Template"));
        ct.fields.putValue("stylesheet", themes.getArrayValue(current_theme).getValue("Stylesheet"));
        ct.fields.putValue("screenshot", themes.getArrayValue(current_theme).getValue("Screenshot"));
        ct.fields.putValue("description", themes.getArrayValue(current_theme).getValue("Description"));
        ct.fields.putValue("author", themes.getArrayValue(current_theme).getValue("Author"));
        ct.fields.putValue("tags", themes.getArrayValue(current_theme).getValue("Tags"));

        return ct;
    }

    public Array<Object> get_broken_themes() {
        (((org.numiton.nwp.wp_includes.ThemePage) getIncluded(org.numiton.nwp.wp_includes.ThemePage.class, gVars, gConsts))).get_themes();

        return gVars.wp_broken_themes;
    }

    public Array<Object> get_page_templates() {
        Array themes;
        String theme = null;
        Object templates = null;

        /* Do not change type */
        Array<Object> page_templates = new Array<Object>();
        String template_data = null;
        String template = null;
        Array<Object> name = new Array<Object>();
        Array<Object> description = new Array<Object>();
        themes = (((org.numiton.nwp.wp_includes.ThemePage) getIncluded(org.numiton.nwp.wp_includes.ThemePage.class, gVars, gConsts))).get_themes();
        theme = (((org.numiton.nwp.wp_includes.ThemePage) getIncluded(org.numiton.nwp.wp_includes.ThemePage.class, gVars, gConsts))).get_current_theme();
        templates = themes.getArrayValue(theme).getValue("Template Files");
        page_templates = new Array<Object>();

        if (is_array(templates)) {
            for (Map.Entry javaEntry213 : ((Array<?>) templates).entrySet()) {
                template = strval(javaEntry213.getValue());
                template_data = Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, gConsts.getABSPATH() + template));
                QRegExPerl.preg_match("|Template Name:(.*)$|mi", template_data, name);
                QRegExPerl.preg_match("|Description:(.*)$|mi", template_data, description);

                String nameStr = strval(name.getValue(1));
                String descriptionStr = strval(description.getValue(1));

                if (!empty(name)) {
                    page_templates.putValue(Strings.trim(nameStr), FileSystemOrSocket.basename(template));
                }
            }
        }

        return page_templates;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
