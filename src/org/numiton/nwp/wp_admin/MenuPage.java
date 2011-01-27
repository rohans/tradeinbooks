/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MenuPage.java,v 1.5 2008/10/14 14:23:04 numiton Exp $
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
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.PluginPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.CommentPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class MenuPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(MenuPage.class.getName());
    public String awaiting_mod;
    public Array<Object> menu_page = new Array<Object>();
    public Array<Object> sub;
    public Array<Object> subs = new Array<Object>();
    public Array<Object> first_sub = new Array<Object>();
    public Object old_parent;
    public Object new_parent;

    @Override
    @RequestMapping("/wp-admin/menu.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/menu";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_menu_block1");
        gVars.webEnv = webEnv;
        
        // This array constructs the admin menu bar.
        //
        // Menu item name
        // The minimum level the user needs to access the item: between 0 and 10
        // The URL of the item's file
        gVars.menu.putValue(
            0,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Dashboard", "default")), new ArrayEntry<Object>("read"), new ArrayEntry<Object>("index.php")));

        if (!strictEqual(Strings.strpos(gVars.webEnv.getRequestURI(), "edit-pages.php"), BOOLEAN_FALSE)) {
            gVars.menu.putValue(
                5,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Write", "default")), new ArrayEntry<Object>("edit_pages"),
                    new ArrayEntry<Object>("page-new.php")));
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getRequestURI(), "link-manager.php"), BOOLEAN_FALSE)) {
            gVars.menu.putValue(
                5,
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Write", "default")),
                    new ArrayEntry<Object>("manage_links"),
                    new ArrayEntry<Object>("link-add.php")));
        } else {
            gVars.menu.putValue(
                5,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Write", "default")), new ArrayEntry<Object>("edit_posts"),
                    new ArrayEntry<Object>("post-new.php")));
        }

        if (!strictEqual(Strings.strpos(gVars.webEnv.getRequestURI(), "page-new.php"), BOOLEAN_FALSE)) {
            gVars.menu.putValue(
                10,
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Manage", "default")),
                    new ArrayEntry<Object>("edit_pages"),
                    new ArrayEntry<Object>("edit-pages.php")));
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getRequestURI(), "link-add.php"), BOOLEAN_FALSE)) {
            gVars.menu.putValue(10,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Manage", "default")),
                    new ArrayEntry<Object>("manage_links"),
                    new ArrayEntry<Object>("link-manager.php")));
        } else {
            gVars.menu.putValue(
                10,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Manage", "default")), new ArrayEntry<Object>("edit_posts"), new ArrayEntry<Object>("edit.php")));
        }

        StdClass awaiting_modObj = getIncluded(CommentPage.class, gVars, gConsts).wp_count_comments();
        awaiting_mod = strval(StdClass.getValue(awaiting_modObj, "moderated"));
        gVars.menu.putValue(
            15,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Design", "default")), new ArrayEntry<Object>("switch_themes"), new ArrayEntry<Object>(
                    "themes.php")));
        gVars.menu.putValue(20,
            new Array<Object>(new ArrayEntry<Object>(
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Comments %s", "default"),
                        "<span id=\'awaiting-mod\' class=\'count-" + awaiting_mod + "\'><span class=\'comment-count\'>" + awaiting_mod + "</span></span>")),
                new ArrayEntry<Object>("edit_posts"),
                new ArrayEntry<Object>("edit-comments.php")));
        gVars.menu.putValue(30,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Settings", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-general.php")));
        gVars.menu.putValue(
            35,
            new Array<Object>(
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Plugins", "default")),
                new ArrayEntry<Object>("activate_plugins"),
                new ArrayEntry<Object>("plugins.php")));

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users")) {
            gVars.menu.putValue(
                40,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Users", "default")), new ArrayEntry<Object>("edit_users"), new ArrayEntry<Object>("users.php")));
        } else {
            gVars.menu.putValue(
                40,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Profile", "default")), new ArrayEntry<Object>("read"), new ArrayEntry<Object>("profile.php")));
        }

        gVars._wp_real_parent_file.putValue("post.php", "post-new.php");
        gVars.submenu.getArrayValue("post-new.php").putValue(
            5,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Post", "default")), new ArrayEntry<Object>("edit_posts"), new ArrayEntry<Object>("post-new.php")));
        gVars.submenu.getArrayValue("post-new.php").putValue(
            10,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Page", "default")), new ArrayEntry<Object>("edit_pages"), new ArrayEntry<Object>("page-new.php")));
        gVars.submenu.getArrayValue("post-new.php").putValue(
            15,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Link", "default")), new ArrayEntry<Object>("manage_links"), new ArrayEntry<Object>("link-add.php")));
        gVars.submenu.getArrayValue("edit-comments.php").putValue(
            5,
            new Array<Object>(
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Comments", "default")),
                new ArrayEntry<Object>("edit_posts"),
                new ArrayEntry<Object>("edit-comments.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            5,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Posts", "default")), new ArrayEntry<Object>("edit_posts"), new ArrayEntry<Object>("edit.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            10,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Pages", "default")), new ArrayEntry<Object>("edit_pages"), new ArrayEntry<Object>(
                    "edit-pages.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            15,
            new Array<Object>(
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Links", "default")),
                new ArrayEntry<Object>("manage_links"),
                new ArrayEntry<Object>("link-manager.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(20,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default")),
                new ArrayEntry<Object>("manage_categories"),
                new ArrayEntry<Object>("categories.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            25,
            new Array<Object>(
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Tags", "default")),
                new ArrayEntry<Object>("manage_categories"),
                new ArrayEntry<Object>("edit-tags.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(30,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Link Categories", "default")),
                new ArrayEntry<Object>("manage_categories"),
                new ArrayEntry<Object>("edit-link-categories.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            35,
            new Array<Object>(
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Media Library", "default")),
                new ArrayEntry<Object>("upload_files"),
                new ArrayEntry<Object>("upload.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            40,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Import", "default")), new ArrayEntry<Object>("import"), new ArrayEntry<Object>("import.php")));
        gVars.submenu.getArrayValue("edit.php").putValue(
            45,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Export", "default")), new ArrayEntry<Object>("import"), new ArrayEntry<Object>("export.php")));

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users")) /*
        * Back-compat
        * for
        * plugins
        * adding
        * submenus
        * to
        * profile.php.
        * Back-compat
        * for
        * plugins
        * adding
        * submenus
        * to
        * profile.php.
        */ {
            gVars._wp_real_parent_file.putValue("profile.php", "users.php");
            gVars.submenu.getArrayValue("users.php").putValue(5,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Authors &amp; Users", "default")),
                    new ArrayEntry<Object>("edit_users"),
                    new ArrayEntry<Object>("users.php")));
            gVars.submenu.getArrayValue("users.php").putValue(
                10,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your Profile", "default")), new ArrayEntry<Object>("read"),
                    new ArrayEntry<Object>("profile.php")));
        } else {
            gVars._wp_real_parent_file.putValue("users.php", "profile.php");
            gVars.submenu.getArrayValue("profile.php").putValue(
                5,
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your Profile", "default")), new ArrayEntry<Object>("read"),
                    new ArrayEntry<Object>("profile.php")));
        }

        gVars.submenu.getArrayValue("options-general.php").putValue(10,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("General", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-general.php")));
        gVars.submenu.getArrayValue("options-general.php").putValue(15,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Writing", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-writing.php")));
        gVars.submenu.getArrayValue("options-general.php").putValue(20,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Reading", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-reading.php")));
        gVars.submenu.getArrayValue("options-general.php").putValue(25,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Discussion", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-discussion.php")));
        gVars.submenu.getArrayValue("options-general.php").putValue(30,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Privacy", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-privacy.php")));
        gVars.submenu.getArrayValue("options-general.php").putValue(35,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Permalinks", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-permalink.php")));
        gVars.submenu.getArrayValue("options-general.php").putValue(40,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Miscellaneous", "default")),
                new ArrayEntry<Object>("manage_options"),
                new ArrayEntry<Object>("options-misc.php")));
        gVars.submenu.getArrayValue("plugins.php").putValue(
            5,
            new Array<Object>(
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Plugins", "default")),
                new ArrayEntry<Object>("activate_plugins"),
                new ArrayEntry<Object>("plugins.php")));
        gVars.submenu.getArrayValue("plugins.php").putValue(10,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Plugin Editor", "default")),
                new ArrayEntry<Object>("edit_plugins"),
                new ArrayEntry<Object>("plugin-editor.php")));
        gVars.submenu.getArrayValue("themes.php").putValue(
            5,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Themes", "default")), new ArrayEntry<Object>("switch_themes"), new ArrayEntry<Object>(
                    "themes.php")));
        gVars.submenu.getArrayValue("themes.php").putValue(10,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Theme Editor", "default")),
                new ArrayEntry<Object>("edit_themes"),
                new ArrayEntry<Object>("theme-editor.php")));
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("_admin_menu", "");

        // Create list of page plugin hook names.
        for (Map.Entry javaEntry283 : gVars.menu.entrySet()) {
            menu_page = (Array<Object>) javaEntry283.getValue();
            gVars.admin_page_hooks.putValue(menu_page.getValue(2), getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(menu_page.getValue(0)), ""));
        }

        gVars._wp_submenu_nopriv = new Array<Object>();
        gVars._wp_menu_nopriv = new Array<Object>();

        // Loop over submenus and remove pages for which the user does not have privs.
        for (Map.Entry javaEntry284 : gVars.submenu.entrySet()) {
            String parentStr = strval(javaEntry284.getKey());
            sub = (Array<Object>) javaEntry284.getValue();

            for (Map.Entry javaEntry285 : sub.entrySet()) {
                gVars.index = javaEntry285.getKey();
                gVars.data = javaEntry285.getValue();

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(((Array) gVars.data).getValue(1)))) {
                    gVars.submenu.getArrayValue(parentStr).arrayUnset(gVars.index);
                    gVars._wp_submenu_nopriv.getArrayValue(parentStr).putValue(((Array) gVars.data).getValue(2), true);
                }
            }

            if (empty(gVars.submenu.getValue(parentStr))) {
                gVars.submenu.arrayUnset(parentStr);
            }
        }

	    // Loop over the top-level menu.
	    // Menus for which the original parent is not acessible due to lack of privs will have the next
	    // submenu in line be assigned as the new menu parent.
        for (Map.Entry javaEntry286 : gVars.menu.entrySet()) {
            gVars.id = javaEntry286.getKey();
            gVars.data = javaEntry286.getValue();

            if (empty(gVars.submenu.getValue(((Array) gVars.data).getValue(2)))) {
                continue;
            }

            subs = Array.arrayCopy(gVars.submenu.getArrayValue(((Array) gVars.data).getValue(2)));
            first_sub = (Array<Object>) Array.array_shift(subs);
            old_parent = ((Array) gVars.data).getValue(2);
            new_parent = first_sub.getValue(2);

            // If the first submenu is not the same as the assigned parent,
        	// make the first submenu the new parent.
            if (!equal(new_parent, old_parent)) {
                gVars._wp_real_parent_file.putValue(old_parent, new_parent);
                gVars.menu.getArrayValue(gVars.id).putValue(2, new_parent);

                for (Map.Entry javaEntry287 : (Set<Map.Entry>) gVars.submenu.getArrayValue(old_parent).entrySet()) {
                    gVars.index = javaEntry287.getKey();
                    gVars.data = javaEntry287.getValue();
                    gVars.submenu.getArrayValue(new_parent).putValue(gVars.index, gVars.submenu.getArrayValue(old_parent).getValue(gVars.index));
                    gVars.submenu.getArrayValue(old_parent).arrayUnset(gVars.index);
                }

                gVars.submenu.arrayUnset(old_parent);
                gVars._wp_submenu_nopriv.putValue(new_parent, gVars._wp_submenu_nopriv.getValue(old_parent));
            }
        }

        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("admin_menu", "");

	    // Remove menus that have no accessible submenus and require privs that the user does not have.
	    // Run re-parent loop again.
        for (Map.Entry javaEntry288 : gVars.menu.entrySet()) {
            gVars.id = javaEntry288.getKey();
            gVars.data = javaEntry288.getValue();
            
            // If submenu is empty...
            if (empty(gVars.submenu.getValue(((Array) gVars.data).getValue(2)))) {
            	// And user doesn't have privs, remove menu.
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(((Array) gVars.data).getValue(1)))) {
                    gVars._wp_menu_nopriv.putValue(((Array) gVars.data).getValue(2), true);
                    gVars.menu.arrayUnset(gVars.id);
                }
            }
        }

        gVars.id = null;
        
        Array.uksort(gVars.menu, new Callback("strnatcasecmp", CallbackUtils.class)); // make it all pretty

        if (!getIncluded(PluginPage.class, gVars, gConsts).user_can_access_admin_page()) {
            (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("admin_page_access_denied", "");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to access this page.", "default"), "");
        }

        return DEFAULT_VAL;
    }
}
