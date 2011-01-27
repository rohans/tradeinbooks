/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MediaPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/MediaPage")
@Scope("request")
public class MediaPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(MediaPage.class.getName());
    public int att_id;
    public Object att;

    @Override
    @RequestMapping("/wp-admin/media.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/media";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_media_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.parent_file = "edit.php";
        gVars.submenu_file = "upload.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action")));

        {
            int javaSwitchSelector22 = 0;

            if (equal(gVars.action, "editattachment")) {
                javaSwitchSelector22 = 1;
            }

            if (equal(gVars.action, "edit")) {
                javaSwitchSelector22 = 2;
            }

            switch (javaSwitchSelector22) {
            case 1: {
                gVars.attachment_id = intval(gVars.webEnv._POST.getValue("attachment_id"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("media-form", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.attachment_id)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this attachment.", "default"), "");
                }

                gVars.errors = (((org.numiton.nwp.wp_admin.includes.MediaPage) getIncluded(org.numiton.nwp.wp_admin.includes.MediaPage.class, gVars, gConsts))).media_upload_form_handler();

                if (empty(gVars.errors)) {
                    gVars.location = "media.php";

                    if (booleanval(gVars.referer = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_original_referer())) {
                        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.referer, "upload.php")) ||
                                equal(getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(gVars.referer), gVars.attachment_id)) {
                            gVars.location = gVars.referer;
                        }
                    }

                    if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.location, "upload.php"))) {
                        gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("message", gVars.location);
                        gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("posted", gVars.attachment_id, gVars.location);
                    } else if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.location, "media.php"))) {
                        gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", "updated", gVars.location);
                    }

                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
                    System.exit();
                }
                
                // no break
            }

            case 2: {
                gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Media", "default");

                if (empty(gVars.errors)) {
                    gVars.errors = null;
                }

                if (empty(gVars.webEnv._GET.getValue("attachment_id"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("upload.php", 302);
                    System.exit();
                }

                att_id = intval(gVars.webEnv._GET.getValue("attachment_id"));

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", att_id)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this attachment.", "default"), "");
                }

                att = getIncluded(PostPage.class, gVars, gConsts).get_post(att_id, gConsts.getOBJECT(), "raw");
                getIncluded(PluginPage.class, gVars, gConsts).add_filter("attachment_fields_to_edit",
                    Callback.createCallbackArray(getIncluded(org.numiton.nwp.wp_admin.includes.MediaPage.class, gVars, gConsts), "media_single_attachment_fields_to_edit"), 10, 2);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("wp-ajax-response", false, new Array<Object>(), false);
                getIncluded(PluginPage.class, gVars, gConsts)
                    .add_action("admin_head", Callback.createCallbackArray(getIncluded(org.numiton.nwp.wp_admin.includes.MediaPage.class, gVars, gConsts), "media_admin_css"), 10, 1);
                require(gVars, gConsts, Admin_headerPage.class);
                gVars.message = "";
                gVars._class = "";

                if (isset(gVars.webEnv._GET.getValue("message"))) {
                    {
                        int javaSwitchSelector23 = 0;

                        if (equal(gVars.webEnv._GET.getValue("message"), "updated")) {
                            javaSwitchSelector23 = 1;
                        }

                        switch (javaSwitchSelector23) {
                        case 1: {
                            gVars.message = getIncluded(L10nPage.class, gVars, gConsts).__("Media attachment updated.", "default");
                            gVars._class = "updated fade";

                            break;
                        }
                        }
                    }
                }

                if (booleanval(gVars.message)) {
                    echo(gVars.webEnv, "<div id=\'message\' class=\'" + gVars._class + "\'><p>" + gVars.message + "</p></div>\n");
                }

                echo(gVars.webEnv, "\n<div class=\"wrap\">\n\n<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Edit Media", "default");
                echo(gVars.webEnv, "</h2>\n\n<form method=\"post\" action=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("message"), null, "display"));
                echo(gVars.webEnv, "\" class=\"media-upload-form\" id=\"media-single-form\">\n<div id=\"media-items\" class=\"media-single\">\n<div id=\'media-item-");
                echo(gVars.webEnv, att_id);
                echo(gVars.webEnv, "\' class=\'media-item\'>\n");
                echo(
                        gVars.webEnv,
                        (((org.numiton.nwp.wp_admin.includes.MediaPage) getIncluded(org.numiton.nwp.wp_admin.includes.MediaPage.class, gVars, gConsts))).get_media_item(
                            att_id,
                            new Array<Object>(new ArrayEntry<Object>("toggle", false), new ArrayEntry<Object>("send", false), new ArrayEntry<Object>("delete", false),
                                new ArrayEntry<Object>("errors", gVars.errors))));
                echo(gVars.webEnv, "</div>\n</div>\n\n<p class=\"submit\">\n<input type=\"submit\" class=\"button\" name=\"save\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");
                echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"post_id\" id=\"post_id\" value=\"");
                echo(gVars.webEnv, gVars.post_id);
                echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"attachment_id\" id=\"attachment_id\" value=\"");
                echo(gVars.webEnv, att_id);
                echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"action\" value=\"editattachment\" />\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_original_referer_field(true, "previous");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("media-form", "_wpnonce", true, true);
                echo(gVars.webEnv, "</p>\n\n\n</div>\n\n");
                require(gVars, gConsts, Admin_footerPage.class);
                System.exit();
            }

            default: {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("upload.php", 302);
                System.exit();
            }
            }
        }

        return DEFAULT_VAL;
    }
}
