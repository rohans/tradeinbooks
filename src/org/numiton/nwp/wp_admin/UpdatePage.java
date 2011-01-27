/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UpdatePage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_content.plugins.akismet.AkismetPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Options;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/UpdatePage")
@Scope("request")
public class UpdatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(UpdatePage.class.getName());

    @Override
    @RequestMapping("/wp-admin/update.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/update";
    }

    public Object request_filesystem_credentials(Object form_post, String type, boolean error) {
        Object req_cred = null;
        Array<Object> credentials = new Array<Object>();
        Object credentialsObj;
        Array<Object> stored_credentials = new Array<Object>();
        String hostname = null;
        String username = null;
        String password = null;
        String ssl = null;
        String selected = null;
        Object value = null;
        Object key = null;
        req_cred = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("request_filesystem_credentials", "", form_post, type, error);

        if (!strictEqual("", req_cred)) {
            return req_cred;
        }

        if (empty(type)) {
            type = getIncluded(FilePage.class, gVars, gConsts).get_filesystem_method();
        }

        if (equal("direct", type)) {
            return true;
        }

        if (!booleanval(credentialsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("ftp_credentials"))) {
            credentials = new Array<Object>();
        } else {
            credentials = (Array<Object>) credentialsObj;
        }

        // If defined, set it to that, Else, If POST'd, set it to that, If not, Set it to whatever it previously was(saved details in option)
        credentials.putValue("hostname",
            gConsts.isFTP_HOSTDefined()
            ? gConsts.getFTP_HOST()
            : ((!empty(gVars.webEnv._POST.getValue("hostname")))
            ? gVars.webEnv._POST.getValue("hostname")
            : credentials.getValue("hostname")));
        credentials.putValue("username",
            gConsts.isFTP_USERDefined()
            ? gConsts.getFTP_USER()
            : ((!empty(gVars.webEnv._POST.getValue("username")))
            ? gVars.webEnv._POST.getValue("username")
            : credentials.getValue("username")));
        credentials.putValue("password",
            gConsts.isFTP_PASSDefined()
            ? gConsts.getFTP_PASS()
            : ((!empty(gVars.webEnv._POST.getValue("password")))
            ? gVars.webEnv._POST.getValue("password")
            : credentials.getValue("password")));
        credentials.putValue("ssl", gConsts.isFTP_SSLDefined()
            ? gConsts.getFTP_SSL()
            : ((!empty(gVars.webEnv._POST.getValue("ssl")))
            ? gVars.webEnv._POST.getValue("ssl")
            : credentials.getValue("ssl")));

        if (!error && !empty(credentials.getValue("password")) && !empty(credentials.getValue("username")) && !empty(credentials.getValue("hostname"))) {
            stored_credentials = credentials;
            stored_credentials.arrayUnset("password");
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("ftp_credentials", stored_credentials);

            return credentials;
        }

        hostname = "";
        username = "";
        password = "";
        ssl = "";

        if (!empty(credentials)) {
            hostname = strval(Array.extractVar(credentials, "hostname", hostname, Array.EXTR_OVERWRITE));
            username = strval(Array.extractVar(credentials, "username", username, Array.EXTR_OVERWRITE));
            password = strval(Array.extractVar(credentials, "password", password, Array.EXTR_OVERWRITE));
            ssl = strval(Array.extractVar(credentials, "ssl", ssl, Array.EXTR_OVERWRITE));
        }

        if (error) {
            echo(
                    gVars.webEnv,
                    "<div id=\"message\" class=\"error\"><p>" +
                    getIncluded(L10nPage.class, gVars, gConsts).__("<strong>Error:</strong> There was an error connecting to the server, Please verify the settings are correct.", "default") +
                    "</p></div>");
        }

        echo(gVars.webEnv, "<form action=\"");
        echo(gVars.webEnv, form_post);
        echo(gVars.webEnv, "\" method=\"post\">\n<div class=\"wrap\">\n<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("FTP Connection Information", "default");
        echo(gVars.webEnv, "</h2>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("To perform the requested update, FTP connection information is required.", "default");
        echo(gVars.webEnv, "</p>\n<table class=\"form-table\">\n<tr valign=\"top\">\n<th scope=\"row\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Hostname:", "default");
        echo(gVars.webEnv, "</th>\n<td><input name=\"hostname\" type=\"text\" id=\"hostname\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(hostname));
        echo(gVars.webEnv, "\"");

        if (gConsts.isFTP_HOSTDefined()) {
            echo(gVars.webEnv, " disabled=\"disabled\"");
        }

        echo(gVars.webEnv, " size=\"40\" /></td>\n</tr>\n<tr valign=\"top\">\n<th scope=\"row\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Username:", "default");
        echo(gVars.webEnv, "</th>\n<td><input name=\"username\" type=\"text\" id=\"username\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(username));
        echo(gVars.webEnv, "\"");

        if (gConsts.isFTP_USERDefined()) {
            echo(gVars.webEnv, " disabled=\"disabled\"");
        }

        echo(gVars.webEnv, " size=\"40\" /></td>\n</tr>\n<tr valign=\"top\">\n<th scope=\"row\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Password:", "default");
        echo(gVars.webEnv, "</th>\n<td><input name=\"password\" type=\"password\" id=\"password\" value=\"\"");

        if (gConsts.isFTP_PASSDefined()) {
            echo(gVars.webEnv, " disabled=\"disabled\"");
        }

        echo(gVars.webEnv, " size=\"40\" />");

        if (gConsts.isFTP_PASSDefined() && !empty(password)) {
            echo(gVars.webEnv, "<em>" + getIncluded(L10nPage.class, gVars, gConsts).__("(Password not shown)", "default") + "</em>");
        }

        echo(gVars.webEnv, "</td>\n</tr>\n<tr valign=\"top\">\n<th scope=\"row\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Use SSL:", "default");
        echo(gVars.webEnv, "</th>\n<td>\n<select name=\"ssl\" id=\"ssl\"");

        if (gConsts.isFTP_SSLDefined()) {
            echo(gVars.webEnv, " disabled=\"disabled\"");
        }

        echo(gVars.webEnv, ">\n");

        for (Map.Entry javaEntry307 : new Array<Object>(
                new ArrayEntry<Object>(0, getIncluded(L10nPage.class, gVars, gConsts).__("No", "default")),
                new ArrayEntry<Object>(1, getIncluded(L10nPage.class, gVars, gConsts).__("Yes", "default"))).entrySet()) {
            key = javaEntry307.getKey();
            value = javaEntry307.getValue();
            selected = (equal(ssl, value)
                ? "selected=\"selected\""
                : "");
            echo(gVars.webEnv, "\n\t<option value=\'" + strval(key) + "\' " + selected + ">" + strval(value) + "</option>");
        }

        echo(gVars.webEnv, "</select>\n</td>\n</tr>\n</table>\n<p class=\"submit\">\n<input type=\"submit\" name=\"submit\" value=\"");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Proceed", "default");
        echo(gVars.webEnv, "\" />\n</p>\n</div>\n</form>\n");

        return false;
    }

    public void show_message(Object message) {
        String messageStr = "";

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(message)) {
            if (booleanval(((WP_Error) message).get_error_data())) {
                messageStr = ((WP_Error) message).get_error_message() + ": " + ((WP_Error) message).get_error_data();
            } else {
                messageStr = ((WP_Error) message).get_error_message();
            }
        }

        echo(gVars.webEnv, "<p>" + messageStr + "</p>\n");
    }

    public void do_plugin_upgrade(String plugin) {
        Object url = null;
        Object credentials = null;
        Object message = null;
        boolean was_activated = false;
        Object result = null;
        url = getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("update.php?action=upgrade-plugin&plugin=" + plugin, "upgrade-plugin_" + plugin);

        if (strictEqual(false, credentials = request_filesystem_credentials(url, "", false))) {
            return;
        }

        if (!getIncluded(FilePage.class, gVars, gConsts).WP_Filesystem((Array<Object>) credentials, false)) {
            request_filesystem_credentials(url, "", true); //Failed to connect, Error and request again

            return;
        }

        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Upgrade Plugin", "default") + "</h2>");

        if (booleanval(gVars.wp_filesystem.errors.get_error_code())) {
            for (Map.Entry javaEntry308 : gVars.wp_filesystem.errors.get_error_messages().entrySet()) {
                message = javaEntry308.getValue();
                show_message(message);
            }

            echo(gVars.webEnv, "</div>");

            return;
        }

        was_activated = (((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).is_plugin_active(plugin); //Check now, It'll be deactivated by the next line if it is,
        result = (((org.numiton.nwp.wp_admin.includes.UpdatePage) getIncluded(org.numiton.nwp.wp_admin.includes.UpdatePage.class, gVars, gConsts))).wp_update_plugin(
                plugin,
                Callback.createCallbackArray(this, "show_message"));

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            show_message(result);
        } else {
        	//Result is the new plugin file relative to PLUGINDIR
            show_message(getIncluded(L10nPage.class, gVars, gConsts).__("Plugin upgraded successfully", "default"));

            if (booleanval(result) && was_activated) {
                show_message(getIncluded(L10nPage.class, gVars, gConsts).__("Attempting reactivation of the plugin", "default"));
                echo(
                        gVars.webEnv,
                        "<iframe style=\"border:0\" width=\"100%\" height=\"170px\" src=\"" +
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("update.php?action=activate-plugin&plugin=" + strval(result), "activate-plugin_" + strval(result)) +
                        "\"></iframe>");
            }
        }

        echo(gVars.webEnv, "</div>");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_update_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_plugins")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die("<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to update plugins for this blog.", "default") + "</p>", "");
        }

        if (isset(gVars.webEnv._GET.getValue("action"))) {
            gVars.plugin = (isset(gVars.webEnv._GET.getValue("plugin"))
                ? Strings.trim(strval(gVars.webEnv._GET.getValue("plugin")))
                : "");

            if (equal("upgrade-plugin", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("upgrade-plugin_" + gVars.plugin, "_wpnonce");
                gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Upgrade Plugin", "default");
                gVars.parent_file = "plugins.php";
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                do_plugin_upgrade(gVars.plugin);
                include(gVars, gConsts, Admin_footerPage.class);
            } else if (equal("activate-plugin", gVars.webEnv._GET.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("activate-plugin_" + gVars.plugin, "_wpnonce");

                if (!isset(gVars.webEnv._GET.getValue("failure")) && !isset(gVars.webEnv._GET.getValue("success"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                            "update.php?action=activate-plugin&failure=true&plugin=" + gVars.plugin + "&_wpnonce=" + strval(gVars.webEnv._GET.getValue("_wpnonce")),
                            302);
                    (((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).activate_plugin(gVars.plugin, "");
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                            "update.php?action=activate-plugin&success=true&plugin=" + gVars.plugin + "&_wpnonce=" + strval(gVars.webEnv._GET.getValue("_wpnonce")),
                            302);
                    System.exit();
                }

                echo(
                    gVars.webEnv,
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ");
                getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_xml_ns", "");
                echo(gVars.webEnv, " ");
                getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");
                echo(gVars.webEnv, ">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"");
                getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");
                echo(gVars.webEnv, "; charset=");
                echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));
                echo(gVars.webEnv, "\" />\n<title>");
                getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
                echo(gVars.webEnv, " &rsaquo; ");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Plugin Reactivation", "default");
                echo(gVars.webEnv, " &#8212; ");
                getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress", "default");
                echo(gVars.webEnv, "</title>\n");
                getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
                getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/colors");
                echo(gVars.webEnv, "</head>\n<body>\n");

                if (isset(gVars.webEnv._GET.getValue("success"))) {
                    echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Plugin reactivated successfully.", "default") + "</p>");
                }

                if (isset(gVars.webEnv._GET.getValue("failure"))) {
                    echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Plugin failed to reactivate due to a fatal error.", "default") + "</p>");
                    ErrorHandling.error_reporting(gVars.webEnv, ErrorHandling.E_ALL ^ ErrorHandling.E_NOTICE);
                    Options.ini_set(gVars.webEnv, "display_errors", strval(true)); //Ensure that Fatal errors are displayed.

                    // TODO Add support for multiple plugins
                    if (equal(gVars.plugin, "akismet")) {
                        include(gVars, gConsts, AkismetPage.class);
                    }
                }

                echo(gVars.webEnv, "</body></html>");
            }
        }

        return DEFAULT_VAL;
    }
}
