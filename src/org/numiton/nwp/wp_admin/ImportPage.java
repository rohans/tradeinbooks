/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ImportPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin._import.*;
import org.numiton.nwp.wp_includes.FormattingPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class ImportPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ImportPage.class.getName());
    public Array<Object> importers;

    @Override
    @RequestMapping("/wp-admin/import.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/_import";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin__import_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Import", "default");
        gVars.parent_file = "edit.php";
        requireOnce(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin__import_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Import", "default");

        /* Start of block */
        super.startBlock("__wp_admin__import_block3");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "If you have posts or comments in another system, nWordPress can import those into this blog. To get started, choose a system to import from below:",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin__import_block4");
        
        // Load all importers so that they can register.
        requireOnce(gVars, gConsts, BloggerPage.class);
        requireOnce(gVars, gConsts, BlogwarePage.class);
        requireOnce(gVars, gConsts, BttPage.class);
        requireOnce(gVars, gConsts, DotclearPage.class);
        requireOnce(gVars, gConsts, GreymatterPage.class);
        requireOnce(gVars, gConsts, JkwPage.class);
        requireOnce(gVars, gConsts, LivejournalPage.class);
        requireOnce(gVars, gConsts, MtPage.class);
        requireOnce(gVars, gConsts, RssPage.class);
        requireOnce(gVars, gConsts, StpPage.class);
        requireOnce(gVars, gConsts, TextpatternPage.class);
        requireOnce(gVars, gConsts, UtwPage.class);
        requireOnce(gVars, gConsts, WordpressPage.class);
        requireOnce(gVars, gConsts, Wp_cat2tagPage.class);
        importers = (((org.numiton.nwp.wp_admin.includes.ImportPage) getIncluded(org.numiton.nwp.wp_admin.includes.ImportPage.class, gVars, gConsts))).get_importers();

        if (empty(importers)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No importers are available.", "default") + "</p>"); // TODO: make more helpful
        } else {
            echo(gVars.webEnv, "<table class=\"widefat\">\n\n");
            gVars.style = "";

            for (Map.Entry javaEntry101 : importers.entrySet()) {
                gVars.id = javaEntry101.getKey();
                gVars.data = javaEntry101.getValue();
                gVars.style = ((equal("class=\"alternate\"", gVars.style) || equal("class=\"alternate active\"", gVars.style))
                    ? ""
                    : "alternate");
                gVars.action = "<a href=\'admin.php?import=" + strval(gVars.id) + "\' title=\'" +
                    getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(Strings.strip_tags(strval(((Array) gVars.data).getValue(1)))) + "\'>" + strval(((Array) gVars.data).getValue(0)) +
                    "</a>";

                if (!equal(gVars.style, "")) {
                    gVars.style = "class=\"" + gVars.style + "\"";
                }

                echo(
                    gVars.webEnv,
                    "\n\t\t\t<tr " + gVars.style + ">\n\t\t\t\t<td class=\'import-system row-title\'>" + strval(gVars.action) + "</td>\n\t\t\t\t<td class=\'desc\'>" +
                    strval(((Array) gVars.data).getValue(1)) + "</td>\n\t\t\t</tr>");
            }

            echo(gVars.webEnv, "\n</table>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin__import_block5");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
