/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_miscPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.FormattingPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Options_miscPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_miscPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/options-misc.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_misc";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Miscellaneous Settings", "default");
        gVars.parent_file = "options-general.php";
        include(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Miscellaneous Settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Uploading", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Store uploads in this folder", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block6");
        echo(gVars.webEnv,
            getIncluded(FormattingPage.class, gVars, gConsts)
                .attribute_escape(Strings.str_replace(gConsts.getABSPATH(), "", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("upload_path")))));

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Default is <code>wp-content/uploads</code>", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Full URL path to files (optional)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block9");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("upload_url_path"))));

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block10");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("uploads_use_yearmonth_folders")));

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Organize my uploads into month- and year-based folders", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Image sizes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("The sizes listed below determine the maximum dimensions to use when inserting an image into the body of a post.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block14");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Thumbnail size", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Width", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block16");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("thumbnail_size_w");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Height", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block18");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("thumbnail_size_h");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block19");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("thumbnail_crop")));

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Crop thumbnail to exact dimensions (normally thumbnails are proportional)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Medium size", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block22");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Max Width", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block23");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("medium_size_w");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block24");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Max Height", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block25");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("medium_size_h");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block26");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("use_linksupdate")));

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block27");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Track Links&#8217; Update Times", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block28");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("hack_file")));

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block29");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Use legacy <code>my-hacks.php</code> file support", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_misc_block31");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
