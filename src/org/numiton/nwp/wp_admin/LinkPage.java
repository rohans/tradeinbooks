/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: LinkPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.BookmarkPage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class LinkPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(LinkPage.class.getName());
    public Array<String> linkcheck;
    public Object all_links;

    @Override
    @RequestMapping("/wp-admin/link.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/link";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_link_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);
        
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this,
            new Array<Object>(new ArrayEntry<Object>("action"),
                new ArrayEntry<Object>("cat_id"),
                new ArrayEntry<Object>("linkurl"),
                new ArrayEntry<Object>("name"),
                new ArrayEntry<Object>("image"),
                new ArrayEntry<Object>("description"),
                new ArrayEntry<Object>("visible"),
                new ArrayEntry<Object>("target"),
                new ArrayEntry<Object>("category"),
                new ArrayEntry<Object>("link_id"),
                new ArrayEntry<Object>("submit"),
                new ArrayEntry<Object>("order_by"),
                new ArrayEntry<Object>("links_show_cat_id"),
                new ArrayEntry<Object>("rating"),
                new ArrayEntry<Object>("rel"),
                new ArrayEntry<Object>("notes"),
                new ArrayEntry<Object>("linkcheck[]")));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
            getIncluded(FunctionsPage.class, gVars, gConsts)
                .wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit the links for this blog.", "default"), "");
        }

        if (!equal("", gVars.webEnv._POST.getValue("deletebookmarks"))) {
            gVars.action = "deletebookmarks";
        }

        if (!equal("", gVars.webEnv._POST.getValue("move"))) {
            gVars.action = "move";
        }

        if (!equal("", gVars.webEnv._POST.getValue("linkcheck"))) {
            linkcheck = (Array<String>) gVars.webEnv._POST.getValue("linkcheck");
        }

        gVars.this_file = "link-manager.php";

        {
            int javaSwitchSelector21 = 0;

            if (equal(gVars.action, "deletebookmarks")) {
                javaSwitchSelector21 = 1;
            }

            if (equal(gVars.action, "move")) {
                javaSwitchSelector21 = 2;
            }

            if (equal(gVars.action, "add")) {
                javaSwitchSelector21 = 3;
            }

            if (equal(gVars.action, "save")) {
                javaSwitchSelector21 = 4;
            }

            if (equal(gVars.action, "delete")) {
                javaSwitchSelector21 = 5;
            }

            if (equal(gVars.action, "edit")) {
                javaSwitchSelector21 = 6;
            }

            switch (javaSwitchSelector21) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-bookmarks", "_wpnonce");

                //for each link id (in $linkcheck[]) change category to selected value
                if (equal(Array.count(linkcheck), 0)) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.this_file, 302);
                    System.exit();
                }

                gVars.deleted = 0;

                for (Map.Entry javaEntry277 : linkcheck.entrySet()) {
                    gVars.link_id = intval(javaEntry277.getValue());

                    //						gVars.link_id = intval(gVars.link_id);
                    if (getIncluded(BookmarkPage.class, gVars, gConsts).wp_delete_link(gVars.link_id)) {
                        gVars.deleted++;
                    }
                }

                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.this_file + "?deleted=" + strval(gVars.deleted), 302);
                System.exit();

                break;
            }

            case 2: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-bookmarks", "_wpnonce");

                //for each link id (in $linkcheck[]) change category to selected value
                if (equal(Array.count(linkcheck), 0)) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.this_file, 302);
                    System.exit();
                }

                all_links = Strings.join(",", linkcheck);
                // should now have an array of links we can change
        		//$q = $wpdb->query("update $wpdb->links SET link_category='$category' WHERE link_id IN ($all_links)");
                
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.this_file, 302);
                System.exit();

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-bookmark", "_wpnonce");
                getIncluded(BookmarkPage.class, gVars, gConsts).add_link();
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer() + "?added=true", 302);
                System.exit();

                break;
            }

            case 4: {
                gVars.link_id = intval(gVars.webEnv._POST.getValue("link_id"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-bookmark_" + strval(gVars.link_id), "_wpnonce");
                getIncluded(BookmarkPage.class, gVars, gConsts).edit_link(strval(gVars.link_id));
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.this_file, 302);
                System.exit();

                break;
            }

            case 5: {
                gVars.link_id = intval(gVars.webEnv._GET.getValue("link_id"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-bookmark_" + strval(gVars.link_id), "_wpnonce");
                getIncluded(BookmarkPage.class, gVars, gConsts).wp_delete_link(gVars.link_id);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.this_file, 302);
                System.exit();

                break;
            }

            case 6: {
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("link", false, new Array<Object>(), false);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("xfn", false, new Array<Object>(), false);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("thickbox", false, new Array<Object>(), false);
                gVars.parent_file = "edit.php";
                gVars.submenu_file = "link-manager.php";
                gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Link", "default");
                gVars.link_id = intval(gVars.webEnv._GET.getValue("link_id"));

                if (!booleanval(gVars.link = getIncluded(BookmarkPage.class, gVars, gConsts).get_link_to_edit(gVars.link_id))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Link not found.", "default"), "");
                }

                includeOnce(gVars, gConsts, Admin_headerPage.class);
                include(gVars, gConsts, Edit_link_formPage.class);
                include(gVars, gConsts, Admin_footerPage.class);

                break;
            }

            default:break;
            }
        }

        return DEFAULT_VAL;
    }
}
