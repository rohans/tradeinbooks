/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PagePage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class PagePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PagePage.class.getName());

    @Override
    @RequestMapping("/wp-admin/page.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/page";
    }

    public void redirect_page(int page_id) {
        String referredby = null;
        String referer;
        String location;
        Object action = null;
        referredby = "";

        if (!empty(gVars.webEnv._POST.getValue("referredby"))) {
            referredby = QRegExPerl.preg_replace("|https?://[^/]+|i", "", strval(gVars.webEnv._POST.getValue("referredby")));
            referredby = getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("_wp_original_http_referer", referredby);
        }

        referer = QRegExPerl.preg_replace("|https?://[^/]+|i", "", getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());

        if (equal("post", gVars.webEnv._POST.getValue("originalaction")) && !empty(gVars.webEnv._POST.getValue("mode")) && equal("bookmarklet", gVars.webEnv._POST.getValue("mode"))) {
            location = strval(gVars.webEnv._POST.getValue("referredby"));
        } else if (equal("post", gVars.webEnv._POST.getValue("originalaction")) && !empty(gVars.webEnv._POST.getValue("mode")) && equal("sidebar", gVars.webEnv._POST.getValue("mode"))) {
            location = "sidebar.php?a=b";
        } else if (isset(gVars.webEnv._POST.getValue("save")) && (empty(referredby) || equal(referredby, referer) || !equal("redo", referredby))) {
            if (booleanval(gVars.webEnv._POST.getValue("_wp_original_http_referer")) &&
                    strictEqual(Strings.strpos(strval(gVars.webEnv._POST.getValue("_wp_original_http_referer")), "/wp-admin/page.php"), BOOLEAN_FALSE) &&
                    strictEqual(Strings.strpos(strval(gVars.webEnv._POST.getValue("_wp_original_http_referer")), "/wp-admin/page-new.php"), BOOLEAN_FALSE)) {
                location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("_wp_original_http_referer",
                        URL.urlencode(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("_wp_original_http_referer")))),
                        "page.php?action=edit&post=" + strval(page_id) + "&message=1");
            } else {
                location = "page.php?action=edit&post=" + strval(page_id) + "&message=4";
            }
        } else if (booleanval(gVars.webEnv._POST.getValue("addmeta"))) {
            location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 2, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());
            location = Strings.explode("#", location).getValue(0) + "#postcustom";
        } else if (booleanval(gVars.webEnv._POST.getValue("deletemeta"))) {
            location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 3, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());
            location = Strings.explode("#", location).getValue(0) + "#postcustom";
        } else if (!empty(referredby) && !equal(referredby, referer)) {
            location = strval(gVars.webEnv._POST.getValue("referredby"));
            location = getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("_wp_original_http_referer", location);

            if (equal(gVars.webEnv._POST.getValue("referredby"), "redo")) {
                location = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(page_id, false);
            } else if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(location, "edit-pages.php"))) {
                location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("posted", page_id, location);
            } else if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(location, "wp-admin"))) {
                location = "page-new.php?posted=" + strval(page_id);
            }
        } else if (isset(gVars.webEnv._POST.getValue("publish"))) {
            location = "page-new.php?posted=" + strval(page_id);
        } else if (equal(action, "editattachment")) {
            location = "attachments.php";
        } else {
            location = "page.php?action=edit&post=" + strval(page_id) + "&message=4";
        }

        getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(location, 302);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_page_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.parent_file = "edit.php";
        gVars.submenu_file = "edit-pages.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action")));

        if (isset(gVars.webEnv._POST.getValue("deletepost"))) {
            gVars.action = "delete";
        }

        {
            int javaSwitchSelector25 = 0;

            if (equal(gVars.action, "post")) {
                javaSwitchSelector25 = 1;
            }

            if (equal(gVars.action, "edit")) {
                javaSwitchSelector25 = 2;
            }

            if (equal(gVars.action, "editattachment")) {
                javaSwitchSelector25 = 3;
            }

            if (equal(gVars.action, "editpost")) {
                javaSwitchSelector25 = 4;
            }

            if (equal(gVars.action, "delete")) {
                javaSwitchSelector25 = 5;
            }

            switch (javaSwitchSelector25) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-page", "_wpnonce");
                gVars.page_id = getIncluded(PostPage.class, gVars, gConsts).write_post();
                redirect_page(gVars.page_id);
                System.exit();

                break;
            }

            case 2: {
                gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default");
                gVars.editing = true;
                gVars.page_id = intval(gVars.post_ID = gVars.p = intval(gVars.webEnv._GET.getValue("post")));
                gVars.post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post_to_edit(gVars.page_id);

                if (empty(StdClass.getValue(gVars.post, "ID"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__(
                            "You attempted to edit a page that doesn\'t exist. Perhaps it was deleted?",
                            "default"), "");
                }

                if (equal("post", StdClass.getValue(gVars.post, "post_type"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("post.php?action=edit&post=" + strval(gVars.post_ID), 302);
                    System.exit();
                }

                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("page", false, new Array<Object>(), false);

                if (booleanval(getIncluded(General_templatePage.class, gVars, gConsts).user_can_richedit())) {
                    getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("editor", false, new Array<Object>(), false);
                }

                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("thickbox", false, new Array<Object>(), false);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("media-upload", false, new Array<Object>(), false);

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", gVars.page_id)) {
                    if (booleanval(gVars.last = getIncluded(PostPage.class, gVars, gConsts).wp_check_post_lock(StdClass.getValue(gVars.post, "ID")))) {
                        gVars.last_user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(gVars.last);
                        gVars.last_user_name = (booleanval(gVars.last_user)
                            ? strval(StdClass.getValue(gVars.last_user, "display_name"))
                            : getIncluded(L10nPage.class, gVars, gConsts).__("Somebody", "default"));
                        gVars.message = QStrings.sprintf(
                                getIncluded(L10nPage.class, gVars, gConsts).__("Warning: %s is currently editing this page", "default"),
                                getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(gVars.last_user_name, strval(0)));
                        gVars.message = Strings.str_replace("\'", "\\\'", "<div class=\'error\'><p>" + gVars.message + "</p></div>");
                        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_notices", Callback.createCallbackArray(this, "createFunction_showMessage"), 10, 1);
                    } else {
                        getIncluded(PostPage.class, gVars, gConsts).wp_set_post_lock(intval(StdClass.getValue(gVars.post, "ID")));
                        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("autosave", false, new Array<Object>(), false);
                    }
                }

                requireOnce(gVars, gConsts, Admin_headerPage.class);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", gVars.page_id)) {
                    System.exit(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this page.", "default"));
                }

                include(gVars, gConsts, Edit_page_formPage.class);

                break;
            }

            case 3: {
                gVars.page_id = intval(gVars.post_ID = intval(gVars.webEnv._POST.getValue("post_ID")));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-attachment_" + strval(gVars.page_id), "_wpnonce");
                
                // Don't let these be changed
                gVars.webEnv._POST.arrayUnset("guid");
                gVars.webEnv._POST.putValue("post_type", "attachment");
                
                // Update the thumbnail filename
                gVars.newmeta = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_get_attachment_metadata(gVars.page_id, true);
                gVars.newmeta.putValue("thumb", gVars.webEnv._POST.getValue("thumb"));
                
                (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_attachment_metadata(gVars.newmeta, null);
            }

            case 4: {
                gVars.page_id = intval(gVars.webEnv._POST.getValue("post_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-page_" + strval(gVars.page_id), "_wpnonce");
                gVars.page_id = getIncluded(PostPage.class, gVars, gConsts).edit_post();
                redirect_page(gVars.page_id);
                System.exit();

                break;
            }

            case 5: {
                gVars.page_id = (isset(gVars.webEnv._GET.getValue("post"))
                    ? intval(gVars.webEnv._GET.getValue("post"))
                    : intval(gVars.webEnv._POST.getValue("post_ID")));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-page_" + strval(gVars.page_id), "_wpnonce");

                StdClass page = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(gVars.page_id, gConsts.getOBJECT(), "raw");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_page", gVars.page_id)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to delete this page.", "default"), "");
                }

                if (equal(StdClass.getValue(page, "post_type"), "attachment")) {
                    if (!booleanval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_delete_attachment(gVars.page_id))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Error in deleting...", "default"), "");
                    }
                } else {
                    if (!booleanval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_delete_post(gVars.page_id))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Error in deleting...", "default"), "");
                    }
                }

                gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer();

                if (!strictEqual(Strings.strpos(gVars.sendback, "page.php"), BOOLEAN_FALSE)) {
                    gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/page.php";
                } else if (!strictEqual(Strings.strpos(gVars.sendback, "attachments.php"), BOOLEAN_FALSE)) {
                    gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/attachments.php";
                }

                gVars.sendback = QRegExPerl.preg_replace("|[^a-z0-9-~+_.?#=&;,/:]|i", "", gVars.sendback);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.sendback, 302);
                System.exit();

                break;
            }

            default: {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit-pages.php", 302);
                System.exit();

                break;
            }
            } // end switch
        }

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }

    public void createFunction_showMessage() {
        echo(gVars.webEnv, gVars.message);
    }
}
