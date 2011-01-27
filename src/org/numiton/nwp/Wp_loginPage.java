/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_loginPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;

@Controller
@Scope("request")
public class Wp_loginPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Wp_loginPage.class.getName());

	@Override
	@RequestMapping("/wp-login.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_login";
	}

	/** 
	 * Rather than duplicating this HTML all over the place, we'll stick it in function
	 */
	public void login_header(String title, String message, WP_Error wp_error) {
		String errors = null;
		String messages = null;
		Object severity = null;
		String code = null;
		if (empty(wp_error)) {
			wp_error = new WP_Error(gVars, gConsts);
		}
		echo(gVars.webEnv,
		        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ");
		getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");
		echo(gVars.webEnv, ">\n<head>\n\t<title>");
		getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
		echo(gVars.webEnv, " &rsaquo; ");
		echo(gVars.webEnv, title);
		echo(gVars.webEnv, "</title>\n\t<meta http-equiv=\"Content-Type\" content=\"");
		getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");
		echo(gVars.webEnv, "; charset=");
		getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("charset");
		echo(gVars.webEnv, "\" />\n\t");
		getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/login");
		getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/colors-fresh");
		echo(gVars.webEnv, "\t<script type=\"text/javascript\">\n\t\tfunction focusit() {\n\t\t\tdocument.getElementById(\'user_login\').focus();\n\t\t}\n\t\twindow.onload = focusit;\n\t</script>\n");
		getIncluded(PluginPage.class, gVars, gConsts).do_action("login_head", "");
		echo(gVars.webEnv, "</head>\n<body class=\"login\">\n\n<div id=\"login\"><h1><a href=\"");
		echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("login_headerurl", "http://wordpress.org/"));
		echo(gVars.webEnv, "\" title=\"");
		echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("login_headertitle", getIncluded(L10nPage.class, gVars, gConsts)
		        .__("Powered by nWordPress", "default")));
		echo(gVars.webEnv, "\">");
		getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
		echo(gVars.webEnv, "</a></h1>\n");
		
		if (!empty(message)) {
			echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("login_message", message) + "\n");
		}
		
		// Incase a plugin uses $error rather than the $errors object
		if (!empty(gVars.error)) {
			wp_error.add("error", strval(gVars.error));
			{
				gVars.error = null;
			}
		}
		if (booleanval(wp_error.get_error_code())) {
			errors = "";
			messages = "";
			for (Map.Entry javaEntry682 : wp_error.get_error_codes().entrySet()) {
				code = strval(javaEntry682.getValue());
				severity = wp_error.get_error_data(code);
				for (Map.Entry javaEntry683 : wp_error.get_error_messages(code).entrySet()) {
					gVars.error = javaEntry683.getValue();
					if (equal("message", severity)) {
						messages = messages + "\t" + strval(gVars.error) + "<br />\n";
					}
					else
						errors = errors + "\t" + strval(gVars.error) + "<br />\n";
				}
			}
			if (!empty(errors)) {
				echo(gVars.webEnv, "<div id=\"login_error\">" + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("login_errors", errors) + "</div>\n");
			}
			if (!empty(messages)) {
				echo(gVars.webEnv, "<p class=\"message\">" + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("login_messages", messages) + "</p>\n");
			}
		}
	} // End of login_header()

	public Object retrieve_password() {
		WP_Error errors = null;
		StdClass user_data;
		String login = null;
		String user_login = null;
		String user_email = null;
		String key = null;
		String message = null;
		
		errors = new WP_Error(gVars, gConsts);
		
		if (empty(gVars.webEnv._POST.getValue("user_login")) && empty(gVars.webEnv._POST.getValue("user_email"))) {
			errors.add("empty_username", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Enter a username or e-mail address.", "default"));
		}
		
		if (booleanval(Strings.strstr(strval(gVars.webEnv._POST.getValue("user_login")), "@"))) {
			user_data = getIncluded(PluggablePage.class, gVars, gConsts).get_user_by_email(Strings.trim(strval(gVars.webEnv._POST.getValue("user_login"))));
			if (empty(user_data)) {
				errors.add("invalid_email", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: There is no user registered with that email address.",
				        "default"));
			}
		}
		else {
			login = Strings.trim(strval(gVars.webEnv._POST.getValue("user_login")));
			user_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(login);
		}
		
		getIncluded(PluginPage.class, gVars, gConsts).do_action("lostpassword_post", "");
		
		if (booleanval(errors.get_error_code())) {
			return errors;
		}
		
		if (!booleanval(user_data)) {
			errors.add("invalidcombo", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Invalid username or e-mail.", "default"));
			return errors;
		}
		
		// redefining user_login ensures we return the right case in the email
		user_login = strval(StdClass.getValue(user_data, "user_login"));
		user_email = strval(StdClass.getValue(user_data, "user_email"));
		
		getIncluded(PluginPage.class, gVars, gConsts).do_action("retreive_password", user_login);
		getIncluded(PluginPage.class, gVars, gConsts).do_action("retrieve_password", user_login);
		
		key = strval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT user_activation_key FROM " + gVars.wpdb.users + " WHERE user_login = %s", user_login)));
		if (empty(key)) {
			// Generate something random for a key...
			key = getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password();
			getIncluded(PluginPage.class, gVars, gConsts).do_action("retrieve_password_key", user_login, key);
			// Now insert the new md5 key into the db
			gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.users + " SET user_activation_key = %s WHERE user_login = %s", key, user_login));
		}
		message = getIncluded(L10nPage.class, gVars, gConsts).__("Someone has asked to reset the password for the following site and username.", "default") + "\r\n\r\n";
		message = message + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "\r\n\r\n";
		message = message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Username: %s", "default"), user_login) + "\r\n\r\n";
		message = message
		        + getIncluded(L10nPage.class, gVars, gConsts).__("To reset your password visit the following address, otherwise just ignore this email and nothing will happen.",
		                "default") + "\r\n\r\n";
		message = message + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php?action=rp&key=" + key + "\r\n";
		
		if (!getIncluded(PluggablePage.class, gVars, gConsts).wp_mail(user_email, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__(
		        "[%s] Password Reset", "default"), strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname"))), message, "")) {
			System.exit("<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("The e-mail could not be sent.", "default") + "<br />\n"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Possible reason: your host may have disabled the mail() function...", "default") + "</p>");
		}
		
		return true;
	}

	public Object reset_password(String key) {
		StdClass user;
		String new_pass = null;
		String message = null;
		
		key = QRegExPerl.preg_replace("/[^a-z0-9]/i", "", key);
		
		if (empty(key)) {
			return new WP_Error(gVars, gConsts, "invalid_key", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid key", "default"));
		}
		
		user = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.users + " WHERE user_activation_key = %s", key));
		if (empty(user)) {
			return new WP_Error(gVars, gConsts, "invalid_key", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid key", "default"));
		}
		
		getIncluded(PluginPage.class, gVars, gConsts).do_action("password_reset", user);
		
		// Generate something random for a password...
		new_pass = getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password();
		getIncluded(PluggablePage.class, gVars, gConsts).wp_set_password(new_pass, intval(StdClass.getValue(user, "ID")));
		message = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Username: %s", "default"), StdClass.getValue(user, "user_login")) + "\r\n";
		message = message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Password: %s", "default"), new_pass) + "\r\n";
		message = message + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php\r\n";
		
		if (!getIncluded(PluggablePage.class, gVars, gConsts).wp_mail(strval(StdClass.getValue(user, "user_email")), QStrings.sprintf(getIncluded(
		        L10nPage.class, gVars, gConsts).__("[%s] Your new password", "default"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")),
		        message, "")) {
			System.exit("<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("The e-mail could not be sent.", "default") + "<br />\n"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Possible reason: your host may have disabled the mail() function...", "default") + "</p>");
		}
		
		// send a copy of password change notification to the admin
		// but check to see if it's the admin whose password we're changing, and skip this
		if (!equal(StdClass.getValue(user, "user_email"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("admin_email"))) {
			message = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Password Lost and Changed for user: %s", "default"), StdClass.getValue(user, "user_login"))
			        + "\r\n";
			getIncluded(PluggablePage.class, gVars, gConsts).wp_mail(strval(getIncluded(FunctionsPage.class, gVars, gConsts)
			        .get_option("admin_email")), QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%s] Password Lost/Changed", "default"),
			        getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")), message, "");
		}
		
		return true;
	}

	public Object register_new_user(String user_login, String user_email) {
		WP_Error errors = null;
		String user_pass = null;
		int user_id = 0;
		
		errors = new WP_Error(gVars, gConsts);
		
		user_login = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(user_login, false);
		user_email = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("user_registration_email", user_email));
		
		// Check the username
		if (equal(user_login, "")) {
			errors.add("empty_username", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please enter a username.", "default"));
		}
		else
			if (!getIncluded(RegistrationPage.class, gVars, gConsts).validate_username(user_login)) {
				errors.add("invalid_username", getIncluded(L10nPage.class, gVars, gConsts).__(
				        "<strong>ERROR</strong>: This username is invalid.  Please enter a valid username.", "default"));
				user_login = "";
			}
			else
				if (booleanval(getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(user_login))) {
					errors.add("username_exists", getIncluded(L10nPage.class, gVars, gConsts).__(
					        "<strong>ERROR</strong>: This username is already registered, please choose another one.", "default"));
				}
		
		// Check the e-mail address
		if (equal(user_email, "")) {
			errors.add("empty_email", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please type your e-mail address.", "default"));
		}
		else
			if (!getIncluded(FormattingPage.class, gVars, gConsts).is_email(user_email)) {
				errors.add("invalid_email", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: The email address isn&#8217;t correct.", "default"));
				user_email = "";
			}
			else
				if (booleanval(getIncluded(RegistrationPage.class, gVars, gConsts).email_exists(user_email))) {
					errors.add("email_exists", getIncluded(L10nPage.class, gVars, gConsts).__(
					        "<strong>ERROR</strong>: This email is already registered, please choose another one.", "default"));
				}
		
		getIncluded(PluginPage.class, gVars, gConsts).do_action("register_post", user_login, user_email, errors);
		
		errors = (WP_Error) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("registration_errors", errors);
		
		if (booleanval(errors.get_error_code())) {
			return errors;
		}
		
		user_pass = getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12);
		user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_create_user(user_login, user_pass, user_email);
		if (!booleanval(user_id)) {
			errors.add("registerfail", QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__(
			        "<strong>ERROR</strong>: Couldn&#8217;t register you... please contact the <a href=\"mailto:%s\">webmaster</a> !", "default"), getIncluded(
			        FunctionsPage.class, gVars, gConsts).get_option("admin_email")));
			return errors;
		}
		
		getIncluded(PluggablePage.class, gVars, gConsts).wp_new_user_notification(user_id, user_pass);
		
		return user_id;
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_login_block1");
		gVars.webEnv = webEnv;
		require( /* Condensed dynamic construct: 509138 */gVars, gConsts, Wp_configPage.class);
		
		//
		// Main
		//
		gVars.action = (isset(gVars.webEnv._REQUEST.getValue("action")) ? strval(gVars.webEnv._REQUEST.getValue("action")) : "");
		gVars.errors = new WP_Error(gVars, gConsts);
		
		if (isset(gVars.webEnv._GET.getValue("key"))) {
			gVars.action = "resetpass";
		}
		
		getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
		
		Network.header(gVars.webEnv, "Content-Type: " + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("html_type", "raw") + "; charset="
		        + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("charset", "raw"));
		
		if (gConsts.isRELOCATEDefined()) { // Move flag is set
			if (isset(gVars.webEnv.getPathInfo()) && !equal(gVars.webEnv.getPathInfo(), gVars.webEnv.getPhpSelf())) {
				gVars.webEnv._SERVER.putValue("PHP_SELF", Strings.str_replace(gVars.webEnv.getPathInfo(), "", gVars.webEnv.getPhpSelf()));
			}
			
			schema = ((isset(gVars.webEnv.getHttps()) && equal(Strings.strtolower(gVars.webEnv.getHttps()), "on")) ? "https://" : "http://");
			if (!equal(FileSystemOrSocket.dirname(schema + gVars.webEnv.getHttpHost() + gVars.webEnv.getPhpSelf()), getIncluded(FunctionsPage.class, gVars, gConsts)
			        .get_option("siteurl"))) {
				getIncluded(FunctionsPage.class, gVars, gConsts).update_option("siteurl", FileSystemOrSocket.dirname(schema + gVars.webEnv.getHttpHost()
				        + gVars.webEnv.getPhpSelf()));
			}
		}
		
		//Set a cookie now to see if they are supported by the browser.
		Network.setcookie(gVars.webEnv, gConsts.getTEST_COOKIE(), "WP Cookie check", 0, gConsts.getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
		if (!equal(gConsts.getSITECOOKIEPATH(), gConsts.getCOOKIEPATH())) {
			Network.setcookie(gVars.webEnv, gConsts.getTEST_COOKIE(), "WP Cookie check", 0, gConsts.getSITECOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
		}
		gVars.http_post = equal("POST", gVars.webEnv.getRequestMethod());
		
		{
			int javaSwitchSelector92 = 0;
			if (equal(gVars.action, "logout"))
				javaSwitchSelector92 = 1;
			if (equal(gVars.action, "lostpassword"))
				javaSwitchSelector92 = 2;
			if (equal(gVars.action, "retrievepassword"))
				javaSwitchSelector92 = 3;
			if (equal(gVars.action, "resetpass"))
				javaSwitchSelector92 = 4;
			if (equal(gVars.action, "rp"))
				javaSwitchSelector92 = 5;
			if (equal(gVars.action, "register"))
				javaSwitchSelector92 = 6;
			if (equal(gVars.action, "login"))
				javaSwitchSelector92 = 7;
			switch (javaSwitchSelector92) {
				case 1: {
					getIncluded(PluggablePage.class, gVars, gConsts).wp_logout();
					gVars.redirect_to = "wp-login.php?loggedout=true";
					if (isset(gVars.webEnv._REQUEST.getValue("redirect_to"))) {
						gVars.redirect_to = strval(gVars.webEnv._REQUEST.getValue("redirect_to"));
					}
					getIncluded(PluggablePage.class, gVars, gConsts).wp_safe_redirect(gVars.redirect_to, 302);
					System.exit();
					break;
				}
				case 2: {
				}
				case 3: {
					if (gVars.http_post) {
						gVars.errors = retrieve_password();
						if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.errors)) {
							getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("wp-login.php?checkemail=confirm", 302);
							System.exit();
						}
					}
					if (equal("invalidkey", gVars.webEnv._GET.getValue("error"))) {
						((WP_Error) gVars.errors).add("invalidkey", getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, that key does not appear to be valid.", "default"));
					}
					getIncluded(PluginPage.class, gVars, gConsts).do_action("lost_password", "");
					login_header(getIncluded(L10nPage.class, gVars, gConsts).__("Lost Password", "default"), "<p class=\"message\">"
					        + getIncluded(L10nPage.class, gVars, gConsts).__("Please enter your username or e-mail address. You will receive a new password via e-mail.",
					                "default") + "</p>", (WP_Error) gVars.errors);
					echo(gVars.webEnv, "\n<form name=\"lostpasswordform\" id=\"lostpasswordform\" action=\"wp-login.php?action=lostpassword\" method=\"post\">\n\t<p>\n\t\t<label>");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Username or E-mail:", "default");
					echo(gVars.webEnv, "<br />\n\t\t<input type=\"text\" name=\"user_login\" id=\"user_login\" class=\"input\" value=\"");
					echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST
					        .getValue("user_login")))));
					echo(gVars.webEnv, "\" size=\"20\" tabindex=\"10\" /></label>\n\t</p>\n");
					getIncluded(PluginPage.class, gVars, gConsts).do_action("lostpassword_form", "");
					echo(gVars.webEnv, "\t<p class=\"submit\"><input type=\"submit\" name=\"wp-submit\" id=\"wp-submit\" value=\"");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Get New Password", "default");
					echo(gVars.webEnv, "\" tabindex=\"100\" /></p>\n</form>\n\n<p id=\"nav\">\n");
					if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("users_can_register"))) {
						echo(gVars.webEnv, "<a href=\"");
						getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
						echo(gVars.webEnv, "/wp-login.php\">");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Log in", "default");
						echo(gVars.webEnv, "</a> |\n<a href=\"");
						getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
						echo(gVars.webEnv, "/wp-login.php?action=register\">");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Register", "default");
						echo(gVars.webEnv, "</a>\n");
					}
					else {
						echo(gVars.webEnv, "<a href=\"");
						getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
						echo(gVars.webEnv, "/wp-login.php\">");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Log in", "default");
						echo(gVars.webEnv, "</a>\n");
					}
					echo(gVars.webEnv, "</p>\n\n</div>\n\n<p id=\"backtoblog\"><a href=\"");
					getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
					echo(gVars.webEnv, "/\" title=\"");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Are you lost?", "default");
					echo(gVars.webEnv, "\">");
					QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Back to %s", "default"), getIncluded(
					        General_templatePage.class, gVars, gConsts).get_bloginfo("title", "display"));
					echo(gVars.webEnv, "</a></p>\n\n</body>\n</html>\n");
					break;
				}
				case 4: {
				}
				case 5: {
					gVars.errors = reset_password(strval(gVars.webEnv._GET.getValue("key")));
					if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.errors)) {
						getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("wp-login.php?checkemail=newpass", 302);
						System.exit();
					}
					getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("wp-login.php?action=lostpassword&error=invalidkey", 302);
					System.exit();
					break;
				}
				case 6: {
					if (!booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("users_can_register"))) {
						getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("wp-login.php?registration=disabled", 302);
						System.exit();
					}
					gVars.user_login = "";
					gVars.user_email = "";
					if (gVars.http_post) {

						/* Condensed dynamic construct */
						requireOnce(gVars, gConsts, RegistrationPage.class);
						gVars.user_login = strval(gVars.webEnv._POST.getValue("user_login"));
						gVars.user_email = strval(gVars.webEnv._POST.getValue("user_email"));
						gVars.errors = register_new_user(gVars.user_login, gVars.user_email);
						if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.errors)) {
							getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("wp-login.php?checkemail=registered", 302);
							System.exit();
						}
					}
					login_header(getIncluded(L10nPage.class, gVars, gConsts).__("Registration Form", "default"), "<p class=\"message register\">"
					        + getIncluded(L10nPage.class, gVars, gConsts).__("Register For This Site", "default") + "</p>", (WP_Error) gVars.errors);
					echo(gVars.webEnv, "\n<form name=\"registerform\" id=\"registerform\" action=\"wp-login.php?action=register\" method=\"post\">\n\t<p>\n\t\t<label>");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Username", "default");
					echo(gVars.webEnv, "<br />\n\t\t<input type=\"text\" name=\"user_login\" id=\"user_login\" class=\"input\" value=\"");
					echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, gVars.user_login)));
					echo(gVars.webEnv, "\" size=\"20\" tabindex=\"10\" /></label>\n\t</p>\n\t<p>\n\t\t<label>");
					getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail", "default");
					echo(gVars.webEnv, "<br />\n\t\t<input type=\"text\" name=\"user_email\" id=\"user_email\" class=\"input\" value=\"");
					echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, gVars.user_email)));
					echo(gVars.webEnv, "\" size=\"25\" tabindex=\"20\" /></label>\n\t</p>\n");
					getIncluded(PluginPage.class, gVars, gConsts).do_action("register_form", "");
					echo(gVars.webEnv, "\t<p id=\"reg_passmail\">");
					getIncluded(L10nPage.class, gVars, gConsts)._e("A password will be e-mailed to you.", "default");
					echo(gVars.webEnv, "</p>\n\t<p class=\"submit\"><input type=\"submit\" name=\"wp-submit\" id=\"wp-submit\" value=\"");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Register", "default");
					echo(gVars.webEnv, "\" tabindex=\"100\" /></p>\n</form>\n\n<p id=\"nav\">\n<a href=\"");
					getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
					echo(gVars.webEnv, "/wp-login.php\">");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Log in", "default");
					echo(gVars.webEnv, "</a> |\n<a href=\"");
					getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
					echo(gVars.webEnv, "/wp-login.php?action=lostpassword\" title=\"");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Password Lost and Found", "default");
					echo(gVars.webEnv, "\">");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Lost your password?", "default");
					echo(gVars.webEnv, "</a>\n</p>\n\n</div>\n\n<p id=\"backtoblog\"><a href=\"");
					getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
					echo(gVars.webEnv, "/\" title=\"");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Are you lost?", "default");
					echo(gVars.webEnv, "\">");
					QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Back to %s", "default"), getIncluded(
					        General_templatePage.class, gVars, gConsts).get_bloginfo("title", "display"));
					echo(gVars.webEnv, "</a></p>\n\n</body>\n</html>\n");
					break;
				}
				case 7: {
				}
				default:

					/*
					 * Clear errors if loggedout is set. Clear errors if
					 * loggedout is set.
					 * 
					 * If cookies are disabled we can't log in even with a valid
					 * user+pass If cookies are disabled we can't log in even
					 * with a valid user+pass
					 */

					/*
					 * Some parts of this script use the main login form to
					 * display a message Some parts of this script use the main
					 * login form to display a message
					 */
				{
					if (isset(gVars.webEnv._REQUEST.getValue("redirect_to"))) {
						gVars.redirect_to = strval(gVars.webEnv._REQUEST.getValue("redirect_to"));
					}
					else
						gVars.redirect_to = "wp-admin/";
					gVars.user = getIncluded(UserPage.class, gVars, gConsts).wp_signon(new Array<Object>());
					if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.user)) {
						// If the user can't edit posts, send them to their profile.
						if (!((WP_User) gVars.user).has_cap("edit_posts") && (empty(gVars.redirect_to) || equal(gVars.redirect_to, "wp-admin/"))) {
							gVars.redirect_to = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/profile.php";
						}
						getIncluded(PluggablePage.class, gVars, gConsts).wp_safe_redirect(gVars.redirect_to, 302);
						System.exit();
					}
					
					gVars.errors = gVars.user;
					// Clear errors if loggedout is set.
					if (!empty(gVars.webEnv._GET.getValue("loggedout"))) {
						gVars.errors = new WP_Error(gVars, gConsts);
					}
					
					// If cookies are disabled we can't log in even with a valid user+pass
					if (isset(gVars.webEnv._POST.getValue("testcookie")) && empty(gVars.webEnv._COOKIE.getValue(gConsts.getTEST_COOKIE()))) {
						((WP_Error) gVars.errors)
						        .add(
						                "test_cookie",
						                getIncluded(L10nPage.class, gVars, gConsts)
						                        .__(
						                                "<strong>ERROR</strong>: Cookies are blocked or not supported by your browser. You must <a href=\'http://www.google.com/cookies.html\'>enable cookies</a> to use nWordPress.",
						                                "default"));
					}
					
					// Some parts of this script use the main login form to display a message
					if (isset(gVars.webEnv._GET.getValue("loggedout")) && equal(true, gVars.webEnv._GET.getValue("loggedout"))) {
						((WP_Error) gVars.errors).add("loggedout", getIncluded(L10nPage.class, gVars, gConsts).__("You are now logged out.", "default"), "message");
					}
					else
						if (isset(gVars.webEnv._GET.getValue("registration")) && equal("disabled", gVars.webEnv._GET.getValue("registration"))) {
							((WP_Error) gVars.errors).add("registerdiabled", getIncluded(L10nPage.class, gVars, gConsts).__("User registration is currently not allowed.",
							        "default"));
						}
						else
							if (isset(gVars.webEnv._GET.getValue("checkemail")) && equal("confirm", gVars.webEnv._GET.getValue("checkemail"))) {
								((WP_Error) gVars.errors).add("confirm", getIncluded(L10nPage.class, gVars, gConsts)
								        .__("Check your e-mail for the confirmation link.", "default"), "message");
							}
							else
								if (isset(gVars.webEnv._GET.getValue("checkemail")) && equal("newpass", gVars.webEnv._GET.getValue("checkemail"))) {
									((WP_Error) gVars.errors).add("newpass", getIncluded(L10nPage.class, gVars, gConsts)
									        .__("Check your e-mail for your new password.", "default"), "message");
								}
								else
									if (isset(gVars.webEnv._GET.getValue("checkemail")) && equal("registered", gVars.webEnv._GET.getValue("checkemail"))) {
										((WP_Error) gVars.errors).add("registered", getIncluded(L10nPage.class, gVars, gConsts).__(
										        "Registration complete. Please check your e-mail.", "default"), "message");
									}
					login_header(getIncluded(L10nPage.class, gVars, gConsts).__("Login", "default"), "", (WP_Error) gVars.errors);
					echo(gVars.webEnv, "\n<form name=\"loginform\" id=\"loginform\" action=\"wp-login.php\" method=\"post\">\n");
					if (!isset(gVars.webEnv._GET.getValue("checkemail"))
					        || !Array.in_array(gVars.webEnv._GET.getValue("checkemail"), new Array<Object>(new ArrayEntry<Object>("confirm"), new ArrayEntry<Object>("newpass")))) {
						echo(gVars.webEnv, "\t<p>\n\t\t<label>");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Username", "default");
						echo(gVars.webEnv, "<br />\n\t\t<input type=\"text\" name=\"log\" id=\"user_login\" class=\"input\" value=\"");
						echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, gVars.user_login)));
						echo(gVars.webEnv, "\" size=\"20\" tabindex=\"10\" /></label>\n\t</p>\n\t<p>\n\t\t<label>");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Password", "default");
						echo(gVars.webEnv, "<br />\n\t\t<input type=\"password\" name=\"pwd\" id=\"user_pass\" class=\"input\" value=\"\" size=\"20\" tabindex=\"20\" /></label>\n\t</p>\n");
						getIncluded(PluginPage.class, gVars, gConsts).do_action("login_form", "");
						echo(gVars.webEnv, "\t<p class=\"forgetmenot\"><label><input name=\"rememberme\" type=\"checkbox\" id=\"rememberme\" value=\"forever\" tabindex=\"90\" /> ");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Remember Me", "default");
						echo(gVars.webEnv, "</label></p>\n\t<p class=\"submit\">\n\t\t<input type=\"submit\" name=\"wp-submit\" id=\"wp-submit\" value=\"");
						getIncluded(L10nPage.class, gVars, gConsts)._e("Log In", "default");
						echo(gVars.webEnv, "\" tabindex=\"100\" />\n\t\t<input type=\"hidden\" name=\"redirect_to\" value=\"");
						echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.redirect_to));
						echo(gVars.webEnv, "\" />\n\t\t<input type=\"hidden\" name=\"testcookie\" value=\"1\" />\n\t</p>\n");
					}
					else {
						echo(gVars.webEnv, "\t<p>&nbsp;</p>\n");
					}
					echo(gVars.webEnv, "</form>\n\n<p id=\"nav\">\n");
					if (isset(gVars.webEnv._GET.getValue("checkemail"))
					        && Array.in_array(gVars.webEnv._GET.getValue("checkemail"), new Array<Object>(new ArrayEntry<Object>("confirm"), new ArrayEntry<Object>("newpass")))) {
					}
					else
						if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("users_can_register"))) {
							echo(gVars.webEnv, "<a href=\"");
							getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
							echo(gVars.webEnv, "/wp-login.php?action=register\">");
							getIncluded(L10nPage.class, gVars, gConsts)._e("Register", "default");
							echo(gVars.webEnv, "</a> |\n<a href=\"");
							getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
							echo(gVars.webEnv, "/wp-login.php?action=lostpassword\" title=\"");
							getIncluded(L10nPage.class, gVars, gConsts)._e("Password Lost and Found", "default");
							echo(gVars.webEnv, "\">");
							getIncluded(L10nPage.class, gVars, gConsts)._e("Lost your password?", "default");
							echo(gVars.webEnv, "</a>\n");
						}
						else {
							echo(gVars.webEnv, "<a href=\"");
							getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("wpurl");
							echo(gVars.webEnv, "/wp-login.php?action=lostpassword\" title=\"");
							getIncluded(L10nPage.class, gVars, gConsts)._e("Password Lost and Found", "default");
							echo(gVars.webEnv, "\">");
							getIncluded(L10nPage.class, gVars, gConsts)._e("Lost your password?", "default");
							echo(gVars.webEnv, "</a>\n");
						}
					echo(gVars.webEnv, "</p>\n\n</div>\n\n<p id=\"backtoblog\"><a href=\"");
					getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
					echo(gVars.webEnv, "/\" title=\"");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Are you lost?", "default");
					echo(gVars.webEnv, "\">");
					QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Back to %s", "default"), getIncluded(
					        General_templatePage.class, gVars, gConsts).get_bloginfo("title", "display"));
					echo(gVars.webEnv, "</a></p>\n\n</body>\n</html>\n");
					break;
				}
			}
		}// end action switch
		
		return DEFAULT_VAL;
	}
	public Object	schema;
}
