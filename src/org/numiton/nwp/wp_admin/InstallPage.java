/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: InstallPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.*;
import org.numiton.nwp.wp_admin.includes.UpgradePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class InstallPage extends NumitonController implements CommonInterface1, CommonInterface2 {
    protected static final Logger LOG = Logger.getLogger(InstallPage.class.getName());
    public CommonInterface1 commonInterface1;
    public String weblog_title;
    public String admin_email;
    public boolean _public;
    public String password;

    @Override
    @RequestMapping("/wp-admin/install.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/install";
    }

    public void display_header() {
        Network.header(gVars.webEnv, "Content-Type: text/html; charset=utf-8");
        echo(gVars.webEnv,
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");
        echo(gVars.webEnv, ">\n<head>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n\t<title>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress &rsaquo; Installation", "default");
        echo(gVars.webEnv, "</title>\n\t");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/install");
        echo(gVars.webEnv, "</head>\n<body>\n<h1 id=\"logo\"><img alt=\"nWordPress\" src=\"images/wordpress-logo.png\" /></h1>\n\n");
    }//end function display_header();

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_install_block1");
        gVars.webEnv = webEnv;
        gConsts.setWP_INSTALLING(true);

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, "../wp-config.php")) {
            requireOnce(gVars, gConsts, CompatPage.class);
            requireOnce(gVars, gConsts, FunctionsPage.class);
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                    "There doesn\'t seem to be a <code>wp-config.php</code> file. I need this before we can get started. Need more help? <a href=\'http://codex.wordpress.org/Editing_wp-config.php\'>We got it</a>. You can create a <code>wp-config.php</code> file through a web interface, but this doesn\'t work for all server setups. The safest way is to manually create the file.</p><p><a href=\'setup-config.php\' class=\'button\'>Create a Configuration File</a>",
                    "nWordPress &rsaquo; Error");
        }

        requireOnce(gVars, gConsts, Wp_configPage.class);
        requireOnce(gVars, gConsts, UpgradePage.class);

        if (isset(gVars.webEnv._GET.getValue("step"))) {
            gVars.step = intval(gVars.webEnv._GET.getValue("step"));
        } else {
            gVars.step = 0;
        }

        // Let's check to make sure WP isn't already installed.
        if (getIncluded(FunctionsPage.class, gVars, gConsts).is_blog_installed()) {
            this.display_header();
            System.exit(
                    "<h1>" + getIncluded(L10nPage.class, gVars, gConsts).__("Already Installed", "default") + "</h1><p>" +
                    getIncluded(L10nPage.class, gVars, gConsts).__("You appear to have already installed nWordPress. To reinstall please clear your old database tables first.", "default") +
                    "</p></body></html>");
        }

        switch (gVars.step) {
        case 0: {
        }

        case 1: { // in case people are directly linking to this
            this.display_header();
            echo(gVars.webEnv, "<h1>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Welcome", "default");
            echo(gVars.webEnv, "</h1>\n<p>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "Welcome to the famous five minute nWordPress installation process! You may want to browse the <a href=\"%s\">ReadMe documentation</a> at your leisure.  Otherwise, just fill in the information below and you\'ll be on your way to using the most extendable and powerful personal publishing platform in the world.",
                            "default"),
                    "../readme.html");
            echo(gVars.webEnv, "</p>\n<!--<h2 class=\"step\"><a href=\"install.php?step=1\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("First Step", "default");
            echo(gVars.webEnv, "</a></h2>-->\n\n<h1>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Information needed", "default");
            echo(gVars.webEnv, "</h1>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Please provide the following information.  Don\'t worry, you can always change these settings later.", "default");
            echo(gVars.webEnv,
                "</p>\n\n<form id=\"setup\" method=\"post\" action=\"install.php?step=2\">\n\t<table class=\"form-table\">\n\t\t<tr>\n\t\t\t<th scope=\"row\"><label for=\"weblog_title\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Blog Title", "default");
            echo(
                gVars.webEnv,
                "</label></th>\n\t\t\t<td><input name=\"weblog_title\" type=\"text\" id=\"weblog_title\" size=\"25\" /></td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th scope=\"row\"><label for=\"admin_email\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Your E-mail", "default");
            echo(gVars.webEnv, "</label></th>\n\t\t\t<td><input name=\"admin_email\" type=\"text\" id=\"admin_email\" size=\"25\" /><br />\n\t\t\t");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Double-check your email address before continuing.", "default");
            echo(gVars.webEnv, "\t\t</tr>\n\t\t<tr>\n\t\t\t<td colspan=\"2\"><label><input type=\"checkbox\" name=\"blog_public\" value=\"1\" checked=\"checked\" /> ");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Allow my blog to appear in search engines like Google and Technorati.", "default");
            echo(gVars.webEnv, "</label></td>\n\t\t</tr>\n\t</table>\n\t<input type=\"submit\" name=\"Submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Install nWordPress", "default");
            echo(gVars.webEnv, "\" class=\"button\" />\n</form>\n\n");

            break;
        }

        case 2: { // Fill in the data we gathered
            if (!empty(gVars.wpdb.error)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(gVars.wpdb.error.get_error_message(), "");
            }

            this.display_header();
            weblog_title = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("weblog_title")));
            admin_email = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("admin_email")));
            _public = booleanval(gVars.webEnv._POST.getValue("blog_public"));

            // check e-mail address
            if (empty(admin_email)) {
            	// TODO: poka-yoke
                System.exit("<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: you must provide an e-mail address.", "default") + "</p>");
            } else if (!getIncluded(FormattingPage.class, gVars, gConsts).is_email(admin_email)) {
            	// TODO: poka-yoke
                System.exit(
                        "<p>" +
                        getIncluded(L10nPage.class, gVars, gConsts).__(
                            "<strong>ERROR</strong>: that isn&#8217;t a valid e-mail address.  E-mail addresses look like: <code>username@example.com</code>",
                            "default") + "</p>");
            }

            gVars.wpdb.show_errors();
            gVars.result = getIncluded(UpgradePage.class, gVars, gConsts).wp_install(weblog_title, "admin", admin_email, _public, "");

            /* Modified by Numiton */
            gVars.url = strval(Array.extractVar((Array) gVars.result, "url", gVars.url, Array.EXTR_SKIP));
            gVars.user_id = Array.extractVar((Array) gVars.result, "user_id", gVars.user_id, Array.EXTR_SKIP);
            password = strval(Array.extractVar((Array) gVars.result, "password", password, Array.EXTR_SKIP));
            echo(gVars.webEnv, "\n<h1>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Success!", "default");
            echo(gVars.webEnv, "</h1>\n\n<p>");
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("nWordPress has been installed. Were you expecting more steps? Sorry to disappoint.", "default"), "");
            echo(gVars.webEnv, "</p>\n\n<table class=\"form-table\">\n\t<tr>\n\t\t<th>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Username", "default");
            echo(gVars.webEnv, "</th>\n\t\t<td><code>admin</code></td>\n\t</tr>\n\t<tr>\n\t\t<th>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Password", "default");
            echo(gVars.webEnv, "</th>\n\t\t<td><code>");
            echo(gVars.webEnv, password);
            echo(gVars.webEnv, "</code><br />\n\t\t\t");
            echo(
                    gVars.webEnv,
                    "<p>" +
                    getIncluded(L10nPage.class, gVars, gConsts)
                        .__("<strong><em>Note that password</em></strong> carefully! It is a <em>random</em> password that was generated just for you.", "default") + "</p>");
            echo(gVars.webEnv, "</td>\n\t</tr>\n</table>\n\n<p><a href=\"../wp-login.php\" class=\"button\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Log In", "default");
            echo(gVars.webEnv, "</a>\n\n");

            break;
        }
        }

        return DEFAULT_VAL;
    }
}
