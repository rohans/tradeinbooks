/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PluginPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.PhpCommonConstants.STRING_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_content.plugins.akismet.AkismetPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.Ref;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/PluginPage")
@Scope("request")
public class PluginPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PluginPage.class.getName());
    public Array<Object> wp_plugins = new Array<Object>();

    @Override
    @RequestMapping("/wp-admin/includes/plugin.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/plugin";
    }

    public Array<Object> get_plugin_data(String plugin_file) {
        String plugin_data = null;
        Array<Object> plugin_name = new Array<Object>();
        Array<Object> plugin_uri = new Array<Object>();
        Array<Object> description = new Array<Object>();
        Array<Object> author_name = new Array<Object>();
        Array<Object> author_uri = new Array<Object>();
        String version = null;
        Array<Object> versionArray = new Array<Object>();
        String name = null;
        String plugin = null;
        String author = null;
        plugin_data = Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, plugin_file));
        QRegExPerl.preg_match("|Plugin Name:(.*)$|mi", plugin_data, plugin_name);
        QRegExPerl.preg_match("|Plugin URI:(.*)$|mi", plugin_data, plugin_uri);
        QRegExPerl.preg_match("|Description:(.*)$|mi", plugin_data, description);
        QRegExPerl.preg_match("|Author:(.*)$|mi", plugin_data, author_name);
        QRegExPerl.preg_match("|Author URI:(.*)$|mi", plugin_data, author_uri);

        if (QRegExPerl.preg_match("|Version:(.*)|i", plugin_data, versionArray)) {
            version = Strings.trim(strval(versionArray.getValue(1)));
        } else {
            version = "";
        }

        String descriptionStr = getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(Strings.trim(strval(description.getValue(1))));
        name = strval(plugin_name.getValue(1));
        name = Strings.trim(name);
        plugin = name;

        if (!equal("", Strings.trim(strval(plugin_uri.getValue(1)))) && !equal("", name)) {
            plugin = "<a href=\"" + Strings.trim(strval(plugin_uri.getValue(1))) + "\" title=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Visit plugin homepage", "default") + "\">" + plugin +
                "</a>";
        }

        if (equal("", author_uri.getValue(1))) {
            author = Strings.trim(strval(author_name.getValue(1)));
        } else {
            author = "<a href=\"" + Strings.trim(strval(author_uri.getValue(1))) + "\" title=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Visit author homepage", "default") + "\">" +
                Strings.trim(strval(author_name.getValue(1))) + "</a>";
        }

        return new Array<Object>(new ArrayEntry<Object>("Name", name), new ArrayEntry<Object>("Title", plugin), new ArrayEntry<Object>("Description", descriptionStr),
            new ArrayEntry<Object>("Author", author), new ArrayEntry<Object>("Version", version));
    }

    public Array<Object> get_plugins(String plugin_folder) {
        String plugin_root = null;
        int plugins_dir = 0;
        String file = null;
        int plugins_subdir = 0;
        String subfile = null;
        Array<Object> plugin_files = new Array<Object>();
        String plugin_file = null;
        Array<Object> plugin_data = new Array<Object>();

        if (isset(wp_plugins)) {
            return wp_plugins;
        }

        wp_plugins = new Array<Object>();
        plugin_root = gConsts.getABSPATH() + gConsts.getPLUGINDIR();

        if (!empty(plugin_folder)) {
            plugin_root = plugin_root + plugin_folder;
        }

        // Files in wp-content/plugins directory
        plugins_dir = Directories.opendir(gVars.webEnv, plugin_root);

        if (booleanval(plugins_dir)) {
            while (!strictEqual(file = Directories.readdir(gVars.webEnv, plugins_dir), STRING_FALSE)) {
                if (equal(Strings.substr(file, 0, 1), ".")) {
                    continue;
                }

                if (FileSystemOrSocket.is_dir(gVars.webEnv, plugin_root + "/" + file)) {
                    plugins_subdir = Directories.opendir(gVars.webEnv, plugin_root + "/" + file);

                    if (booleanval(plugins_subdir)) {
                        while (!strictEqual(subfile = Directories.readdir(gVars.webEnv, plugins_subdir), STRING_FALSE)) {
                            if (equal(Strings.substr(subfile, 0, 1), ".")) {
                                continue;
                            }

                            if (equal(Strings.substr(subfile, -4), ".php")) {
                                plugin_files.putValue(file + "/" + subfile);
                            }
                        }
                    }
                } else {
                    if (equal(Strings.substr(file, -4), ".php")) {
                        plugin_files.putValue(file);
                    }
                }
            }
        }

        Directories.closedir(gVars.webEnv, plugins_dir);
        Directories.closedir(gVars.webEnv, plugins_subdir);

        if (!booleanval(plugins_dir) || !booleanval(plugin_files)) {
            return wp_plugins;
        }

        for (Map.Entry javaEntry171 : plugin_files.entrySet()) {
            plugin_file = strval(javaEntry171.getValue());

            if (!FileSystemOrSocket.is_readable(gVars.webEnv, plugin_root + "/" + plugin_file)) {
                continue;
            }

            plugin_data = get_plugin_data(plugin_root + "/" + plugin_file);

            if (empty(plugin_data.getValue("Name"))) {
                continue;
            }

            wp_plugins.putValue((((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).plugin_basename(plugin_file), plugin_data);
        }

        Array.uasort(wp_plugins, new Callback("createFunction_cmp", this));

        return wp_plugins;
    }

    public int createFunction_cmp(Array a, Array b) {
        return Strings.strnatcasecmp(strval(a.getValue("Name")), strval(b.getValue("Name")));
    }

    public boolean is_plugin_active(Object plugin) {
        return Array.in_array(plugin, (Array) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins"));
    }

    public Object activate_plugin(String plugin, String redirect) {
        Array<Object> current = new Array<Object>();
        Object valid = null;
        current = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");
        plugin = Strings.trim(plugin);
        valid = validate_plugin(plugin);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(valid)) {
            return valid;
        }

        if (!Array.in_array(plugin, current))/*
         * we'll override this later if the plugin can be included without fatal
         * error we'll override this later if the plugin can be included without
         * fatal error
         */
         {
            if (!empty(redirect)) {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                        "_error_nonce",
                        getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("plugin-activation-error_" + plugin),
                        redirect), 302);
            }

            OutputControl.ob_start(gVars.webEnv);

            // TODO Add support for multiple plugins
            if (equal(plugin, "akismet")) {
                include(gVars, gConsts, AkismetPage.class);
            }

            current.putValue(plugin);
            Array.sort(current);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("active_plugins", current);
            (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("activate_" + plugin, "");
            OutputControl.ob_end_clean(gVars.webEnv);
        }

        return null;
    }

    public void deactivate_plugins(Object plugins, boolean /* Do not change type */ silent) {
        Array<Object> current;
        String plugin = null;
        current = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");

        if (!is_array(plugins)) {
            plugins = new Array<Object>(new ArrayEntry<Object>(plugins));
        }

        for (Map.Entry javaEntry172 : ((Array<?>) plugins).entrySet()) {
            plugin = strval(javaEntry172.getValue());

            if (!is_plugin_active(plugin)) {
                continue;
            }

            Array.array_splice(new Ref(current), intval(Array.array_search(plugin, current)), 1); // Fixed Array-fu!

            if (!silent) { //Used by Plugin updater to internally deactivate plugin, however, not to notify plugins of the fact to prevent plugin output.
                getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts).do_action("deactivate_" + Strings.trim(plugin), "");
            }
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("active_plugins", current);
    }

    public void deactivate_all_plugins() {
        Object current = null;
        current = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");

        if (empty(current)) {
            return;
        }

        deactivate_plugins(current, false);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("deactivated_plugins", current);
    }

    public Object reactivate_all_plugins(String redirect) {
        Object plugins = null;
        Object plugin = null;
        Array<Object> errors = new Array<Object>();
        Object result = null;
        plugins = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("deactivated_plugins");

        if (empty(plugins)) {
            return null;
        }

        if (!empty(redirect)) {
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                    "_error_nonce",
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("plugin-activation-error_" + strval(plugin)),
                    redirect), 302);
        }

        errors = new Array<Object>();

        for (Map.Entry javaEntry173 : new Array<Object>(plugins).entrySet()) {
            plugin = javaEntry173.getValue();
            result = activate_plugin(strval(plugin), "");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                errors.putValue(plugin, result);
            }
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("deactivated_plugins");

        if (!empty(errors)) {
            return new WP_Error(gVars, gConsts, "plugins_invalid", getIncluded(L10nPage.class, gVars, gConsts).__("One of the plugins is invalid.", "default"), strval(errors));
        }

        return true;
    }

    public void validate_active_plugins() {
        Object check_plugins = null;

        /* Do not change type */
        Object check_plugin = null;
        Array<Object> current = new Array<Object>();
        Object key = null;
        check_plugins = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");

        // Sanity check.  If the active plugin list is not an array, make it an
    	// empty array.
        if (!is_array(check_plugins)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("active_plugins", new Array<Object>());

            return;
        }

        // If a plugin file does not exist, remove it from the list of active
    	// plugins.
        for (Map.Entry javaEntry174 : ((Array<?>) check_plugins).entrySet()) {
            check_plugin = javaEntry174.getValue();

            // Modified by Numiton. TODO Add support for plugins
            if (false) {
                if (!FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(check_plugin))) {
                    current = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");
                    key = Array.array_search(check_plugin, current);

                    if (!strictEqual(false, key) && !strictEqual(null, key)) {
                        current.arrayUnset(key);
                        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("active_plugins", current);
                    }
                }
            }
        }
    }

    public Object validate_plugin(String plugin) {
        if (booleanval(getIncluded(FilePage.class, gVars, gConsts).validate_file(plugin, new Array<String>()))) {
            return new WP_Error(gVars, gConsts, "plugin_invalid", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid plugin.", "default"));
        }

        // Modified by Numiton. TODO Add support for plugins
        if (false) {
            if (!FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + plugin)) {
                return new WP_Error(gVars, gConsts, "plugin_not_found", getIncluded(L10nPage.class, gVars, gConsts).__("Plugin file does not exist.", "default"));
            }
        }

        return 0;
    }

    /**
     * Menu
     */
    public String add_menu_page(String page_title, String menu_title, Object access_level, String file, Array<Object> function) {
        String hookname = null;
        file = (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).plugin_basename(file);
        gVars.menu.putValue(new Array<Object>(new ArrayEntry<Object>(menu_title), new ArrayEntry<Object>(access_level), new ArrayEntry<Object>(file), new ArrayEntry<Object>(page_title)));
        gVars.admin_page_hooks.putValue(file, getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(menu_title, ""));
        hookname = get_plugin_page_hookname(file, "");

        if (!empty(function) && !empty(hookname)) {
            (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action(hookname, function, 10, 1);
        }

        return hookname;
    }

    public String add_submenu_page(String parent, String page_title, String menu_title, String access_level, String file, Array<Object> function) {
        Array<Object> parent_menu = new Array<Object>();
        String hookname = null;
        file = (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).plugin_basename(file);
        parent = (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).plugin_basename(parent);

        if (isset(gVars._wp_real_parent_file.getValue(parent))) {
            parent = strval(gVars._wp_real_parent_file.getValue(parent));
        }

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(access_level)) {
            gVars._wp_submenu_nopriv.getArrayValue(parent).putValue(file, true);

            return strval(false);
        }

    	// If the parent doesn't already have a submenu, add a link to the parent
    	// as the first item in the submenu.  If the submenu file is the same as the
    	// parent file someone is trying to link back to the parent manually.  In
    	// this case, don't automatically add a link back to avoid duplication.
        if (!isset(gVars.submenu.getValue(parent)) && !equal(file, parent)) {
            for (Map.Entry javaEntry175 : gVars.menu.entrySet()) {
                parent_menu = (Array<Object>) javaEntry175.getValue();

                if (equal(parent_menu.getValue(2), parent) && getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(parent_menu.getValue(1)))) {
                    gVars.submenu.getArrayValue(parent).putValue(parent_menu);
                }
            }
        }

        gVars.submenu.getArrayValue(parent).putValue(
            new Array<Object>(new ArrayEntry<Object>(menu_title), new ArrayEntry<Object>(access_level), new ArrayEntry<Object>(file), new ArrayEntry<Object>(page_title)));
        hookname = get_plugin_page_hookname(file, parent);

        if (!empty(function) && !empty(hookname)) {
            (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action(hookname, function, 10, 1);
        }

        return hookname;
    }

    public String add_management_page(String page_title, String menu_title, String access_level, String file, Array<Object> function) {
        return add_submenu_page("edit.php", page_title, menu_title, access_level, file, function);
    }

    public String add_options_page(String page_title, String menu_title, String access_level, String file, Array<Object> function) {
        return add_submenu_page("options-general.php", page_title, menu_title, access_level, file, function);
    }

    public String add_theme_page(String page_title, String menu_title, String access_level, String file, Array<Object> function) {
        return add_submenu_page("themes.php", page_title, menu_title, access_level, file, function);
    }

    public String add_users_page(String page_title, String menu_title, String access_level, String file, Array<Object> function) {
        String parent = null;

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users")) {
            parent = "users.php";
        } else {
            parent = "profile.php";
        }

        return add_submenu_page(parent, page_title, menu_title, access_level, file, function);
    }

    /**
     * Pluggable Menu Support -- Private
     */
    public String get_admin_page_parent() {
        Array<Object> parent_menu = new Array<Object>();
        Object parent = null;
        Array<Object> submenu_array = new Array<Object>();

        if (!empty(gVars.parent_file)) {
            if (isset(gVars._wp_real_parent_file.getValue(gVars.parent_file))) {
                gVars.parent_file = strval(gVars._wp_real_parent_file.getValue(gVars.parent_file));
            }

            return gVars.parent_file;
        }

        if (equal(gVars.pagenow, "admin.php") && isset(gVars.plugin_page)) {
            for (Map.Entry javaEntry176 : gVars.menu.entrySet()) {
                parent_menu = (Array<Object>) javaEntry176.getValue();

                if (equal(parent_menu.getValue(2), gVars.plugin_page)) {
                    gVars.parent_file = gVars.plugin_page;

                    if (isset(gVars._wp_real_parent_file.getValue(gVars.parent_file))) {
                        gVars.parent_file = strval(gVars._wp_real_parent_file.getValue(gVars.parent_file));
                    }

                    return gVars.parent_file;
                }
            }

            if (isset(gVars._wp_menu_nopriv.getValue(gVars.plugin_page))) {
                gVars.parent_file = gVars.plugin_page;

                if (isset(gVars._wp_real_parent_file.getValue(gVars.parent_file))) {
                    gVars.parent_file = strval(gVars._wp_real_parent_file.getValue(gVars.parent_file));
                }

                return gVars.parent_file;
            }
        }

        if (isset(gVars.plugin_page) && isset(gVars._wp_submenu_nopriv.getArrayValue(gVars.pagenow).getValue(gVars.plugin_page))) {
            gVars.parent_file = gVars.pagenow;

            if (isset(gVars._wp_real_parent_file.getValue(gVars.parent_file))) {
                gVars.parent_file = strval(gVars._wp_real_parent_file.getValue(gVars.parent_file));
            }

            return gVars.parent_file;
        }

        for (Map.Entry javaEntry177 : Array.array_keys(gVars.submenu).entrySet()) {
            parent = javaEntry177.getValue();

            for (Map.Entry javaEntry178 : (Set<Map.Entry>) gVars.submenu.getArrayValue(parent).entrySet()) {
                submenu_array = (Array<Object>) javaEntry178.getValue();

                if (isset(gVars._wp_real_parent_file.getValue(parent))) {
                    parent = gVars._wp_real_parent_file.getValue(parent);
                }

                if (equal(submenu_array.getValue(2), gVars.pagenow)) {
                    gVars.parent_file = strval(parent);

                    return strval(parent);
                } else if (isset(gVars.plugin_page) && equal(gVars.plugin_page, submenu_array.getValue(2))) {
                    gVars.parent_file = strval(parent);

                    return strval(parent);
                }
            }
        }

        gVars.parent_file = "";

        return "";
    }

    public Object get_admin_page_title() {
        String hook = null;
        String parent = null;
        String parent1 = null;
        Array<Object> menu_array = new Array<Object>();
        Array<Object> submenu_array = new Array<Object>();

        if (isset(gVars.title) && !empty(gVars.title)) {
            return gVars.title;
        }

        hook = get_plugin_page_hook(gVars.plugin_page, gVars.pagenow);
        parent = parent1 = get_admin_page_parent();

        if (empty(parent)) {
            for (Map.Entry javaEntry179 : gVars.menu.entrySet()) {
                menu_array = (Array<Object>) javaEntry179.getValue();

                if (isset(menu_array.getValue(3))) {
                    if (equal(menu_array.getValue(2), gVars.pagenow)) {
                        gVars.title = strval(menu_array.getValue(3));

                        return menu_array.getValue(3);
                    } else if (isset(gVars.plugin_page) && equal(gVars.plugin_page, menu_array.getValue(2)) && equal(hook, menu_array.getValue(3))) {
                        gVars.title = strval(menu_array.getValue(3));

                        return menu_array.getValue(3);
                    }
                } else {
                    gVars.title = strval(menu_array.getValue(0));

                    return gVars.title;
                }
            }
        } else {
            for (Map.Entry javaEntry180 : Array.array_keys(gVars.submenu).entrySet()) {
                parent = strval(javaEntry180.getValue());

                for (Map.Entry javaEntry181 : (Set<Map.Entry>) gVars.submenu.getArrayValue(parent).entrySet()) {
                    submenu_array = (Array<Object>) javaEntry181.getValue();

                    if (isset(gVars.plugin_page) && equal(gVars.plugin_page, submenu_array.getValue(2)) &&
                            (equal(parent, gVars.pagenow) || equal(parent, gVars.plugin_page) || equal(gVars.plugin_page, hook) ||
                            (equal(gVars.pagenow, "admin.php") && !equal(parent1, submenu_array.getValue(2))))) {
                        gVars.title = strval(submenu_array.getValue(3));

                        return submenu_array.getValue(3);
                    }

                    if (!equal(submenu_array.getValue(2), gVars.pagenow) || isset(gVars.webEnv._GET.getValue("page"))) { // not the current page
                        continue;
                    }

                    if (isset(submenu_array.getValue(3))) {
                        gVars.title = strval(submenu_array.getValue(3));

                        return submenu_array.getValue(3);
                    } else {
                        gVars.title = strval(submenu_array.getValue(0));

                        return gVars.title;
                    }
                }
            }
        }

        return gVars.title;
    }

    public String get_plugin_page_hook(String plugin_page, String parent_page) {
        String hook = null;
        hook = get_plugin_page_hookname(plugin_page, parent_page);

        if (booleanval((((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).has_action(hook, false))) {
            return hook;
        } else {
            return null;
        }
    }

    public String get_plugin_page_hookname(String plugin_page, String parent_page) {
        String parent = null;
        String page_type = null;
        String plugin_name;
        parent = get_admin_page_parent();

        if (empty(parent_page) || equal("admin.php", parent_page)) {
            if (isset(gVars.admin_page_hooks.getValue(plugin_page))) {
                page_type = "toplevel";
            } else if (isset(gVars.admin_page_hooks.getValue(parent))) {
                page_type = strval(gVars.admin_page_hooks.getValue(parent));
            }
        } else if (isset(gVars.admin_page_hooks.getValue(parent_page))) {
            page_type = strval(gVars.admin_page_hooks.getValue(parent_page));
        } else {
            page_type = "admin";
        }

        plugin_name = QRegExPerl.preg_replace("!\\.php!", "", plugin_page);

        return page_type + "_page_" + plugin_name;
    }

    public boolean user_can_access_admin_page() {
        String parent = null;
        Object key = null;
        Array<Object> submenu_array = new Array<Object>();
        Array<Object> menu_array = new Array<Object>();
        parent = get_admin_page_parent();

        if (isset(gVars._wp_submenu_nopriv.getArrayValue(parent).getValue(gVars.pagenow))) {
            return false;
        }

        if (isset(gVars.plugin_page) && isset(gVars._wp_submenu_nopriv.getArrayValue(parent).getValue(gVars.plugin_page))) {
            return false;
        }

        if (empty(parent)) {
            if (isset(gVars._wp_menu_nopriv.getValue(gVars.pagenow))) {
                return false;
            }

            if (isset(gVars._wp_submenu_nopriv.getArrayValue(gVars.pagenow).getValue(gVars.pagenow))) {
                return false;
            }

            if (isset(gVars.plugin_page) && isset(gVars._wp_submenu_nopriv.getArrayValue(gVars.pagenow).getValue(gVars.plugin_page))) {
                return false;
            }

            for (Map.Entry javaEntry182 : Array.array_keys(gVars._wp_submenu_nopriv).entrySet()) {
                key = javaEntry182.getValue();

                if (isset(gVars._wp_submenu_nopriv.getArrayValue(key).getValue(gVars.pagenow))) {
                    return false;
                }

                if (isset(gVars.plugin_page) && isset(gVars._wp_submenu_nopriv.getArrayValue(key).getValue(gVars.plugin_page))) {
                    return false;
                }
            }

            return true;
        }

        if (isset(gVars.submenu.getValue(parent))) {
            for (Map.Entry javaEntry183 : (Set<Map.Entry>) gVars.submenu.getArrayValue(parent).entrySet()) {
                submenu_array = (Array<Object>) javaEntry183.getValue();

                if (isset(gVars.plugin_page) && equal(submenu_array.getValue(2), gVars.plugin_page)) {
                    if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(submenu_array.getValue(1)))) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (equal(submenu_array.getValue(2), gVars.pagenow)) {
                    if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(submenu_array.getValue(1)))) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        for (Map.Entry javaEntry184 : gVars.menu.entrySet()) {
            menu_array = (Array<Object>) javaEntry184.getValue();

            if (equal(menu_array.getValue(2), parent)) {
                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(menu_array.getValue(1)))) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
