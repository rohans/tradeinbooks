/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UpdatePage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

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

import com.numiton.DateTime;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/UpdatePage")
@Scope("request")
public class UpdatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(UpdatePage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/update.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/update";
    }

 // The admin side of our 1.1 update system
    public String core_update_footer(Object msg) {
        StdClass cur = null;

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
            return QStrings.sprintf("| " + getIncluded(L10nPage.class, gVars, gConsts).__("Version %s", "default"), gVars.wp_version);
        }

        cur = (StdClass) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_core");

        {
            int javaSwitchSelector14 = 0;

            if (equal(StdClass.getValue(cur, "response"), "development")) {
                javaSwitchSelector14 = 1;
            }

            if (equal(StdClass.getValue(cur, "response"), "upgrade")) {
                javaSwitchSelector14 = 2;
            }

            if (equal(StdClass.getValue(cur, "response"), "latest")) {
                javaSwitchSelector14 = 3;
            }

            switch (javaSwitchSelector14) {
            case 1:return QStrings.sprintf(
                    "| " + getIncluded(L10nPage.class, gVars, gConsts).__("You are using a development version (%s). Cool! Please <a href=\"%s\">stay updated</a>.", "default"),
                    gVars.wp_version,
                    StdClass.getValue(cur, "url"),
                    StdClass.getValue(cur, "current"));

            //					break;
            case 2: {
                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
                    return QStrings.sprintf(
                        "| <strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"%2$s\">Get Version %3$s</a>", "default") + "</strong>",
                        gVars.wp_version,
                        StdClass.getValue(cur, "url"),
                        StdClass.getValue(cur, "current"));
                }
            }

            //						break;
            case 3: {
            }

            default:return QStrings.sprintf("| " + getIncluded(L10nPage.class, gVars, gConsts).__("Version %s", "default"), gVars.wp_version, StdClass.getValue(cur, "url"),
                    StdClass.getValue(cur, "current"));
            }
        }
    }

    //					break;
    public boolean update_nag() {
        // Commented by Numiton. TODO Add support for update checks
        //		StdClass cur = null;
        //		String msg = null;
        //		cur = (StdClass) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_core");
        //		if (!isset(StdClass.getValue(cur, "response")) || !equal(StdClass.getValue(cur, "response"), "upgrade")) {
        //			return false;
        //		}
        //		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
        //			msg = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("WordPress %2$s is available! <a href=\"%1$s\">Please update now</a>.", "default"), cur.fields
        //			        .getValue("url"), StdClass.getValue(cur, "current"));
        //		}
        //		else
        //			msg = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("WordPress %2$s is available! Please notify the site administrator.", "default"), cur.fields
        //			        .getValue("url"), StdClass.getValue(cur, "current"));
        //		echo(gVars.webEnv, "<div id=\'update-nag\'>" + msg + "</div>");
        return false;
    }

    /**
     * Called directly from dashboard
     */
    public void update_right_now_message() {
        // Commented by Numiton TODO Add support for version checks

        //		StdClass cur = null;
        //		String msg = null;
        //		cur = (StdClass) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_core");
        //		msg = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("This is WordPress version %s.", "default"), gVars.wp_version);
        //		if (isset(StdClass.getValue(cur, "response")) && equal(StdClass.getValue(cur, "response"), "upgrade")
        //		        && getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
        //			msg = msg
        //			        + " <a href=\'"
        //			        + StdClass.getValue(cur, "url")
        //			        + "\' class=\'rbutton\'>"
        //			        + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Update to %s", "default"), booleanval(StdClass.getValue(cur, "current")) ? cur.fields
        //			                .getValue("current") : getIncluded(L10nPage.class, gVars, gConsts).__("Latest", "default")) + "</a>";
        //		}
        //		echo(gVars.webEnv, "<span id=\'wp-version-message\'>" + msg + "</span>");
    }

    public boolean wp_update_plugins() {
        Array<Object> plugins = new Array<Object>();
        Object active = null;
        StdClass current = null;
        StdClass new_option = null;
        boolean plugin_changed = false;
        Object file = null;
        Array<Object> p = new Array<Object>();
        StdClass to_send = new StdClass();
        String send = null;
        String request = null;
        String http_request = null;
        String response = null;
        Array<String> responseArray = new Array<String>();
        int fs = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();

        if (!true)/*Modified by Numiton*/
         {
            return false;
        }

        plugins = getIncluded(PluginPage.class, gVars, gConsts).get_plugins("");
        active = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");
        current = (StdClass) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_plugins");
        new_option = new StdClass();
        new_option.fields.putValue("last_checked", DateTime.time());
        plugin_changed = false;

        for (Map.Entry javaEntry214 : plugins.entrySet()) {
            file = javaEntry214.getKey();
            p = (Array<Object>) javaEntry214.getValue();
            new_option.fields.getArrayValue("checked").putValue(file, p.getValue("Version"));

            if (!isset(current.fields.getArrayValue("checked").getValue(file))) {
                plugin_changed = true;

                continue;
            }

            if (!strictEqual(strval(current.fields.getArrayValue("checked").getValue(file)), strval(p.getValue("Version")))) {
                plugin_changed = true;
            }
        }

        if (isset(current) && isset(StdClass.getValue(current, "last_checked")) && (43200 > (DateTime.time() - intval(StdClass.getValue(current, "last_checked")))) && !plugin_changed) {
            return false;
        }

        to_send.fields.putValue("plugins", plugins);
        to_send.fields.putValue("active", active);
        send = serialize(to_send);
        request = "plugins=" + URL.urlencode(send);
        http_request = "POST /plugins/update-check/1.0/ HTTP/1.0\r\n";
        http_request = http_request + "Host: api.wordpress.org\r\n";
        http_request = http_request + "Content-Type: application/x-www-form-urlencoded; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\r\n";
        http_request = http_request + "Content-Length: " + strval(Strings.strlen(request)) + "\r\n";
        http_request = http_request + "User-Agent: nWordPress/" + gVars.wp_version + "; " + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "\r\n";
        http_request = http_request + "\r\n";
        http_request = http_request + request;
        response = "";

        // Commented by Numiton
        //		if (!equal(false, fs = FileSystemOrSocket.fsockopen(gVars.webEnv, "api.wordpress.org", 80, errno, errstr, 3)) && is_resource(fs))
        //		/*
        //		 * One TCP-IP packet
        //		 */
        //		{
        //			FileSystemOrSocket.fwrite(gVars.webEnv, fs, http_request);
        //			while (!FileSystemOrSocket.feof(gVars.webEnv, fs))
        //				response = response + FileSystemOrSocket.fgets(gVars.webEnv, fs, 1160); // One TCP-IP packet
        //			FileSystemOrSocket.fclose(gVars.webEnv, fs);
        //			responseArray = Strings.explode("\r\n\r\n", response, 2);
        //			response = strval(unserialize(responseArray.getValue(1)));
        //		}

        // Moved by Numiton
        if (booleanval(response)) {
            new_option.fields.putValue("response", response);
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("update_plugins", new_option);

        return false;
    }

    public boolean wp_plugin_update_row(Object file) {
        StdClass current = null;
        StdClass r = null;
        current = (StdClass) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_plugins");

        // Added by Numiton
        if (is_null(current)) {
            current = new StdClass();
        }

        if (!isset(current.fields.getArrayValue("response").getValue(file))) {
            return false;
        }

        r = (StdClass) current.fields.getArrayValue("response").getValue(file);

        // Added by Numiton
        if (is_null(r)) {
            r = new StdClass();
        }

        echo(gVars.webEnv, "<tr><td colspan=\'5\' class=\'plugin-update\'>");

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_plugins")) {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("There is a new version of %1$s available. <a href=\"%2$s\">Download version %3$s here</a>.", "default"),
                gVars.plugin_data.getValue("Name"),
                StdClass.getValue(r, "url"),
                StdClass.getValue(r, "new_version"));
        } else if (empty(StdClass.getValue(r, "package"))) {
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "There is a new version of %1$s available. <a href=\"%2$s\">Download version %3$s here</a> <em>automatic upgrade unavailable for this plugin</em>.",
                            "default"),
                    gVars.plugin_data.getValue("Name"),
                    StdClass.getValue(r, "url"),
                    StdClass.getValue(r, "new_version"));
        } else {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                    "There is a new version of %1$s available. <a href=\"%2$s\">Download version %3$s here</a> or <a href=\"%4$s\">upgrade automatically</a>.",
                    "default"),
                gVars.plugin_data.getValue("Name"),
                StdClass.getValue(r, "url"),
                StdClass.getValue(r, "new_version"),
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("update.php?action=upgrade-plugin&amp;plugin=" + strval(file), "upgrade-plugin_" + strval(file)));
        }

        echo(gVars.webEnv, "</td></tr>");

        return false;
    }

    public Object wp_update_plugin(String plugin, Array<Object> feedback) {
        StdClass current = null;
        Object base = null;
        StdClass r = null;
        String _package = null;
        Object file = null;
        String working_dir = null;
        Object result = null;
        String plugin_dir = null;
        Object deleted = null;
        Array<Object> filelist = new Array<Object>();
        Object folder = null;
        Array<Object> pluginfiles = new Array<Object>();

        if (!empty(feedback)) {
            (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_filter("update_feedback", feedback, 10, 1);
        }

    	// Is an update available?
        current = (StdClass) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_plugins");

        // Added by Numiton
        if (is_null(current)) {
            current = new StdClass();
        }

        if (!isset(current.fields.getArrayValue("response").getValue(plugin))) {
            return new WP_Error(gVars, gConsts, "up_to_date", getIncluded(L10nPage.class, gVars, gConsts).__("The plugin is at the latest version.", "default"));
        }

    	// Is a filesystem accessor setup?
        if (!booleanval(gVars.wp_filesystem) || !is_object(gVars.wp_filesystem)) {
            getIncluded(FilePage.class, gVars, gConsts).WP_Filesystem(new Array<Object>(), false);
        }

        if (!is_object(gVars.wp_filesystem)) {
            return new WP_Error(gVars, gConsts, "fs_unavailable", getIncluded(L10nPage.class, gVars, gConsts).__("Could not access filesystem.", "default"));
        }

        if (booleanval(gVars.wp_filesystem.errors.get_error_code())) {
            return new WP_Error(gVars, gConsts, "fs_error", getIncluded(L10nPage.class, gVars, gConsts).__("Filesystem error", "default"), gVars.wp_filesystem.errors);
        }

    	//Get the Base folder
        base = gVars.wp_filesystem.get_base_dir();

        if (empty(base)) {
            return new WP_Error(gVars, gConsts, "fs_nowordpress", getIncluded(L10nPage.class, gVars, gConsts).__("Unable to locate nWordPress directory.", "default"));
        }

    	// Get the URL to the zip file
        r = (StdClass) current.fields.getArrayValue("response").getValue(plugin);

        // Added by Numiton
        if (is_null(r)) {
            r = new StdClass();
        }

        if (empty(StdClass.getValue(r, "package"))) {
            return new WP_Error(gVars, gConsts, "no_package", getIncluded(L10nPage.class, gVars, gConsts).__("Upgrade package not available.", "default"));
        }

    	// Download the package
        _package = strval(StdClass.getValue(r, "package"));
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters(
            "update_feedback",
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Downloading update from %s", "default"), _package));
        file = getIncluded(FilePage.class, gVars, gConsts).download_url(_package);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(file)) {
            return new WP_Error(gVars, gConsts, "download_failed", getIncluded(L10nPage.class, gVars, gConsts).__("Download failed.", "default"), ((WP_Error) file).get_error_message());
        }

        working_dir = strval(base) + "wp-content/upgrade/" + FileSystemOrSocket.basename(plugin, ".php");

    	// Clean up working directory
        if (gVars.wp_filesystem.is_dir(working_dir)) {
            gVars.wp_filesystem.delete(working_dir, true);
        }

        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters(
            "update_feedback",
            getIncluded(L10nPage.class, gVars, gConsts).__("Unpacking the update", "default"));
    	// Unzip package to working directory
        result = getIncluded(FilePage.class, gVars, gConsts).unzip_file(strval(file), working_dir);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            JFileSystemOrSocket.unlink(gVars.webEnv, strval(file));
            gVars.wp_filesystem.delete(working_dir, true);

            return result;
        }

    	// Once extracted, delete the package
        JFileSystemOrSocket.unlink(gVars.webEnv, strval(file));

        if (getIncluded(PluginPage.class, gVars, gConsts).is_plugin_active(plugin)) {
    		//Deactivate the plugin silently, Prevent deactivation hooks from running.
            getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts).apply_filters("update_feedback",
            					getIncluded(L10nPage.class, gVars, gConsts).__("Deactivating the plugin", "default"));
            getIncluded(PluginPage.class, gVars, gConsts).deactivate_plugins(plugin, true);
        }

    	// Remove the existing plugin.
        getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts).apply_filters("update_feedback",
					            getIncluded(L10nPage.class, gVars, gConsts).__("Removing the old version of the plugin", "default"));
        plugin_dir = FileSystemOrSocket.dirname(strval(base) + gConsts.getPLUGINDIR() + "/" + plugin);
        plugin_dir = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(plugin_dir);

    	// If plugin is in its own directory, recursively delete the directory.
        if ((BOOLEAN_FALSE != Strings.strpos(plugin, "/")) && !equal(plugin_dir, strval(base) + gConsts.getPLUGINDIR() + "/")) { //base check on if plugin includes directory seperator AND that its not the root plugin folder
            deleted = gVars.wp_filesystem.delete(plugin_dir, true);
        } else {
            deleted = gVars.wp_filesystem.delete(base + gConsts.getPLUGINDIR() + "/" + plugin);
        }

        if (!booleanval(deleted)) {
            gVars.wp_filesystem.delete(working_dir, true);

            return new WP_Error(gVars, gConsts, "delete_failed", getIncluded(L10nPage.class, gVars, gConsts).__("Could not remove the old plugin", "default"));
        }

        getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts).apply_filters("update_feedback",
        					getIncluded(L10nPage.class, gVars, gConsts).__("Installing the latest version", "default"));

    	// Copy new version of plugin into place.
        if (!getIncluded(FilePage.class, gVars, gConsts).copy_dir(working_dir, strval(base) + gConsts.getPLUGINDIR())) {
        	//$wp_filesystem->delete($working_dir, true); //TODO: Uncomment? This DOES mean that the new files are available in the upgrade folder if it fails.
            return new WP_Error(gVars, gConsts, "install_failed", getIncluded(L10nPage.class, gVars, gConsts).__("Installation failed", "default"));
        }

        //Get a list of the directories in the working directory before we delete it, We need to know the new folder for the plugin
        filelist = Array.array_keys(gVars.wp_filesystem.dirlist(working_dir));
        
        // Remove working directory
        gVars.wp_filesystem.delete(working_dir, true);
        
    	// Force refresh of plugin update information
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("update_plugins");

        if (empty(filelist)) {
            return false; //We couldnt find any files in the working dir
        }

        folder = filelist.getValue(0);

        Array<Object> pluginArray = getIncluded(PluginPage.class, gVars, gConsts).get_plugins("/" + strval(folder)); //Pass it with a leading slash, search out the plugins in the folder,
        pluginfiles = Array.array_keys(pluginArray); //Assume the requested plugin is the first in the list

        return strval(folder) + "/" + strval(pluginfiles.getValue(0)); //Pass it without a leading slash as WP requires
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_update_block1");
        gVars.webEnv = webEnv;
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_filter("update_footer", Callback.createCallbackArray(this, "core_update_footer"), 10, 1);
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_notices", Callback.createCallbackArray(this, "update_nag"), 3, 1);
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("load-plugins.php", Callback.createCallbackArray(this, "wp_update_plugins"),
            10, 1);
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action(
            "after_plugin_row",
            Callback.createCallbackArray(this, "wp_plugin_update_row"),
            10,
            1);

        return DEFAULT_VAL;
    }
}
