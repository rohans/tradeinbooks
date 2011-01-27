/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ExportPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.L10nPage;
import org.numiton.nwp.wp_includes.PluggablePage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;


@Controller
@Scope("request")
public class ExportPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ExportPage.class.getName());
    public StdClass o;

    @Override
    @RequestMapping("/wp-admin/export.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/export";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_export_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        requireOnce(gVars, gConsts, org.numiton.nwp.wp_admin.includes.ExportPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Export", "default");
        gVars.parent_file = "edit.php";

        if (isset(gVars.webEnv._GET.getValue("download"))) {
            (((org.numiton.nwp.wp_admin.includes.ExportPage) getIncluded(org.numiton.nwp.wp_admin.includes.ExportPage.class, gVars, gConsts))).export_wp(gVars.webEnv._GET.getValue("author"));
            webEnv.getNonNullRequest().setAttribute("EXIT_INVOKED", "true");
            System.exit();
        }

        requireOnce(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_export_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Export", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block3");
        getIncluded(L10nPage.class, gVars, gConsts)._e("When you click the button below nWordPress will create an XML file for you to save to your computer.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block4");
        getIncluded(L10nPage.class, gVars, gConsts)
            ._e("This format, which we call WordPress eXtended RSS or WXR, will contain your posts, pages, comments, custom fields, categories, and tags.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Once you&#8217;ve saved the download file, you can use the Import function on another WordPress/nWordPress blog to import this blog.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Options", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Restrict Author", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("All Authors", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block9");
        gVars.authors = gVars.wpdb.get_col("SELECT post_author FROM " + gVars.wpdb.posts + " GROUP BY post_author");

        for (Map.Entry javaEntry28 : gVars.authors.entrySet()) {
            gVars.id = javaEntry28.getValue();
            o = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(gVars.id));
            echo(gVars.webEnv, "<option value=\'" + StdClass.getValue(o, "ID") + "\'>" + StdClass.getValue(o, "display_name") + "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_export_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Download Export File", "default");

        /* Start of block */
        super.startBlock("__wp_admin_export_block11");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
