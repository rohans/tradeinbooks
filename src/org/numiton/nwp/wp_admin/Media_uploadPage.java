/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Media_uploadPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Media_uploadPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Media_uploadPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/media-upload.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/media_upload";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_media_upload_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("swfupload", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("swfupload-degrade", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("swfupload-queue", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("swfupload-handlers", false, new Array<Object>(), false);
        
        Network.header(
            gVars.webEnv,
            "Content-Type: " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type") + "; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("upload_files")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to upload files.", "default"), "");
        }

        // IDs should be integers
        gVars.ID = (isset(gVars.ID)
            ? gVars.ID
            : 0);
        gVars.post_id = (isset(gVars.post_id)
            ? gVars.post_id
            : 0);

        // Require an ID for the edit screen
        if (isset(gVars.action) && equal(gVars.action, "edit") && !booleanval(gVars.ID)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to be here", "default"), "");
        }

        // upload type: image, video, file, ..?
        if (isset(gVars.webEnv._GET.getValue("type"))) {
            gVars.type = strval(gVars.webEnv._GET.getValue("type"));
        } else {
            gVars.type = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("media_upload_default_type", "file"));
        }

        // tab: gallery, library, or type-specific
        if (isset(gVars.webEnv._GET.getValue("tab"))) {
            gVars.tab = strval(gVars.webEnv._GET.getValue("tab"));
        } else {
            gVars.tab = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("media_upload_default_tab", "type"));
        }

        gVars.body_id = "media-upload";

        // let the action code decide how to handle the request
        if (equal(gVars.tab, "type")) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("media_upload_" + gVars.type, "");
        } else {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("media_upload_" + gVars.tab, "");
        }

        return DEFAULT_VAL;
    }
}
