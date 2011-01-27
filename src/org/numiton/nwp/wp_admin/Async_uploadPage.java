/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Async_uploadPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.MediaPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Async_uploadPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Async_uploadPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/async-upload.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/async_upload";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_async_upload_block1");
        gVars.webEnv = webEnv;

        /* This accepts file uploads from swfupload or other asynchronous upload methods.
        
        */
        if (gConsts.isABSPATHDefined()) {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, Wp_configPage.class);
        } else {
            requireOnce(gVars, gConsts, Wp_configPage.class);
        }

        // Flash often fails to send cookies with the POST or upload, so we need to pass it in GET or POST instead
        if (empty(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE())) && !empty(gVars.webEnv._REQUEST.getValue("auth_cookie"))) {
            gVars.webEnv._COOKIE.putValue(gConsts.getAUTH_COOKIE(), gVars.webEnv._REQUEST.getValue("auth_cookie"));
        }

        gVars.current_user = null;
        requireOnce(gVars, gConsts, AdminPage.class);
        Network.header(gVars.webEnv, "Content-Type: text/plain");

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("upload_files")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to upload files.", "default"), "");
        }

        // just fetch the detail form for that attachment
        if (booleanval(gVars.id = intval(gVars.webEnv._REQUEST.getValue("attachment_id"))) && booleanval(gVars.webEnv._REQUEST.getValue("fetch"))) {
            echo(gVars.webEnv, getIncluded(MediaPage.class, gVars, gConsts).get_media_item(intval(gVars.id), null));
            System.exit();
        }

        gVars.id = getIncluded(MediaPage.class, gVars, gConsts).media_handle_upload("async-upload", gVars.webEnv._REQUEST.getValue("post_id"), new Array<Object>());

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.id)) {
            echo(gVars.webEnv, "<div id=\"media-upload-error\">" + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(((WP_Error) gVars.id).get_error_message(), strval(0)) + "</div>");
            System.exit();
        }

        if (booleanval(gVars.webEnv._REQUEST.getValue("short"))) {
        	// short form response - attachment ID only
            echo(gVars.webEnv, gVars.id);
        } else {
        	// long form response - big chunk o html
            gVars.type = strval(gVars.webEnv._REQUEST.getValue("type"));
            echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("async_upload_" + gVars.type, gVars.id));
        }

        return DEFAULT_VAL;
    }
}
