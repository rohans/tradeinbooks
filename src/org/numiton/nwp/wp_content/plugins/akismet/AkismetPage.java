/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: AkismetPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_content.plugins.akismet;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.CommonInterface3;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;

@Controller
@Scope("request")
public class AkismetPage extends NumitonController implements CommonInterface3 {
	protected static final Logger	LOG	= Logger.getLogger(AkismetPage.class.getName());

	@Override
	@RequestMapping("/wp-content/plugins/akismet/akismet.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_content/plugins/akismet/akismet";
	}

	public void akismet_init() {
		if (booleanval(wpcom_api_key)) {
			akismet_api_host = wpcom_api_key + ".rest.akismet.com";
		}
		else
			akismet_api_host = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("wordpress_api_key") + ".rest.akismet.com";
		akismet_api_port = 80;
		(((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_menu", Callback.createCallbackArray(this, "akismet_config_page"));
	}

	// Modified by Numiton
	public Object akismet_nonce_field(String action) {
		// Commented by Numiton
//		if (!Unsupported.function_exists("wp_nonce_field")) {
//			return null;
//		}
//		else {
			return getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field(action);
//		}
	}

	public String number_format_i18n(double number, int decimals) {
		return Strings.number_format(number, decimals);
	}

	public void akismet_config_page() {
		if (true)
		/*Modified by Numiton*/
		{
			(((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).add_submenu_page("plugins.php", getIncluded(
			        L10nPage.class, gVars, gConsts).__("Akismet Configuration"), getIncluded(L10nPage.class, gVars, gConsts).__("Akismet Configuration"), "manage_options",
			        "akismet-key-config", Callback.createCallbackArray(this, "akismet_conf"));
		}
	}

	public void akismet_conf() {
		String key;
		String key_status = null;
		Array<Object> ms = new Array<Object>();
		Array<Object> messages = new Array<Object>();
		Object m = null;
		Object invalid_key = null;
		if (isset(gVars.webEnv._POST.getValue("submit"))) {
			if (true && /*Modified by Numiton*/!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
				System.exit(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?"));
			}
			getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer(akismet_nonce);
			key = QRegExPerl.preg_replace("/[^a-h0-9]/i", "", strval(gVars.webEnv._POST.getValue("key")));
			if (empty(key)) {
				key_status = "empty";
				ms.putValue("new_key_empty");
				getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("wordpress_api_key");
			}
			else {
				key_status = akismet_verify_key(key);
			}
			if (equal(key_status, "valid")) {
				getIncluded(FunctionsPage.class, gVars, gConsts).update_option("wordpress_api_key", key);
				ms.putValue("new_key_valid");
			}
			else
				if (equal(key_status, "invalid")) {
					ms.putValue("new_key_invalid");
				}
				else
					if (equal(key_status, "failed")) {
						ms.putValue("new_key_failed");
					}
			if (isset(gVars.webEnv._POST.getValue("akismet_discard_month"))) {
				getIncluded(FunctionsPage.class, gVars, gConsts).update_option("akismet_discard_month", "true");
			}
			else
				getIncluded(FunctionsPage.class, gVars, gConsts).update_option("akismet_discard_month", "false");
		}
		if (!equal(key_status, "valid")) {
			key = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("wordpress_api_key"));
			if (empty(key)) {
				if (!equal(key_status, "failed")) {
					if (equal(akismet_verify_key("1234567890ab"), "failed")) {
						ms.putValue("no_connection");
					}
					else
						ms.putValue("key_empty");
				}
				key_status = "empty";
			}
			else {
				key_status = akismet_verify_key(key);
			}
			if (equal(key_status, "valid")) {
				ms.putValue("key_valid");
			}
			else
				if (equal(key_status, "invalid")) {
					getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("wordpress_api_key");
					ms.putValue("key_empty");
				}
				else
					if (!empty(key) && equal(key_status, "failed")) {
						ms.putValue("key_failed");
					}
		}
		messages = new Array<Object>(new ArrayEntry<Object>("new_key_empty", new Array<Object>(new ArrayEntry<Object>("color", "aa0"), new ArrayEntry<Object>("text", getIncluded(
		        L10nPage.class, gVars, gConsts).__("Your key has been cleared.")))), new ArrayEntry<Object>("new_key_valid", new Array<Object>(new ArrayEntry<Object>("color", "2d2"),
		        new ArrayEntry<Object>("text", getIncluded(L10nPage.class, gVars, gConsts).__("Your key has been verified. Happy blogging!")))), new ArrayEntry<Object>(
		        "new_key_invalid", new Array<Object>(new ArrayEntry<Object>("color", "d22"), new ArrayEntry<Object>("text", getIncluded(L10nPage.class, gVars, gConsts)
		                .__("The key you entered is invalid. Please double-check it.")))), new ArrayEntry<Object>("new_key_failed", new Array<Object>(new ArrayEntry<Object>("color", "d22"),
		        new ArrayEntry<Object>("text", getIncluded(L10nPage.class, gVars, gConsts)
		                .__("The key you entered could not be verified because a connection to akismet.com could not be established. Please check your server configuration.")))),
		        new ArrayEntry<Object>("no_connection", new Array<Object>(new ArrayEntry<Object>("color", "d22"), new ArrayEntry<Object>("text", getIncluded(L10nPage.class, gVars,
		                gConsts).__("There was a problem connecting to the Akismet server. Please check your server configuration.")))), new ArrayEntry<Object>("key_empty", new Array<Object>(
		                new ArrayEntry<Object>("color", "aa0"), new ArrayEntry<Object>("text", QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)
		                        .__("Please enter an API key. (<a href=\"%s\" style=\"color:#fff\">Get your key.</a>)"), "http://wordpress.com/profile/")))), new ArrayEntry<Object>("key_valid",
		                new Array<Object>(new ArrayEntry<Object>("color", "2d2"), new ArrayEntry<Object>("text", getIncluded(L10nPage.class, gVars, gConsts)
		                        .__("This key is valid.")))), new ArrayEntry<Object>("key_failed", new Array<Object>(new ArrayEntry<Object>("color", "aa0"), new ArrayEntry<Object>("text",
		                getIncluded(L10nPage.class, gVars, gConsts)
		                        .__("The key below was previously validated but a connection to akismet.com can not be established at this time. Please check your server configuration.")))));
		if (!empty(gVars.webEnv._POST)) {
			echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p><strong>");
			getIncluded(L10nPage.class, gVars, gConsts)._e("Options saved.");
			echo(gVars.webEnv, "</strong></p></div>\n");
		}
		else {
		}
		echo(gVars.webEnv, "<div class=\"wrap\">\n<h2>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Akismet Configuration");
		echo(gVars.webEnv, "</h2>\n<div class=\"narrow\">\n<form action=\"\" method=\"post\" id=\"akismet-conf\" style=\"margin: auto; width: 400px; \">\n");
		if (!booleanval(wpcom_api_key)) {
			echo(gVars.webEnv, "\t<p>");
			QStrings
			        .printf(
			                gVars.webEnv,
			                getIncluded(L10nPage.class, gVars, gConsts)
			                        .__("For many people, <a href=\"%1$s\">Akismet</a> will greatly reduce or even completely eliminate the comment and trackback spam you get on your site. If one does happen to get through, simply mark it as \"spam\" on the moderation screen and Akismet will learn from the mistakes. If you don\'t have a WordPress.com account yet, you can get one at <a href=\"%2$s\">WordPress.com</a>."),
			                "http://akismet.com/", "http://wordpress.com/api-keys/");
			echo(gVars.webEnv, "</p>\n\n");
			this.akismet_nonce_field(akismet_nonce);
			echo(gVars.webEnv, "<h3><label for=\"key\">");
			getIncluded(L10nPage.class, gVars, gConsts)._e("WordPress.com API Key");
			echo(gVars.webEnv, "</label></h3>\n");
			for (Map.Entry javaEntry347 : ms.entrySet()) {
				m = javaEntry347.getValue();
				echo(gVars.webEnv, "\t<p style=\"padding: .5em; background-color: #");
				echo(gVars.webEnv, messages.getArrayValue(m).getValue("color"));
				echo(gVars.webEnv, "; color: #fff; font-weight: bold;\">");
				echo(gVars.webEnv, messages.getArrayValue(m).getValue("text"));
				echo(gVars.webEnv, "</p>\n");
			}
			echo(gVars.webEnv, "<p><input id=\"key\" name=\"key\" type=\"text\" size=\"15\" maxlength=\"12\" value=\"");
			echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("wordpress_api_key"));
			echo(gVars.webEnv, "\" style=\"font-family: \'Courier New\', Courier, mono; font-size: 1.5em;\" /> (");
			getIncluded(L10nPage.class, gVars, gConsts)._e("<a href=\"http://faq.wordpress.com/2005/10/19/api-key/\">What is this?</a>");
			echo(gVars.webEnv, ")</p>\n");
			if (booleanval(invalid_key)) {
				echo(gVars.webEnv, "<h3>");
				getIncluded(L10nPage.class, gVars, gConsts)._e("Why might my key be invalid?");
				echo(gVars.webEnv, "</h3>\n<p>");
				getIncluded(L10nPage.class, gVars, gConsts)
				        ._e("This can mean one of two things, either you copied the key wrong or that the plugin is unable to reach the Akismet servers, which is most often caused by an issue with your web host around firewalls or similar.");
				echo(gVars.webEnv, "</p>\n");
			}
		}
		echo(gVars.webEnv, "<p><label><input name=\"akismet_discard_month\" id=\"akismet_discard_month\" value=\"true\" type=\"checkbox\" ");
		if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_discard_month"), "true")) {
			echo(gVars.webEnv, " checked=\"checked\" ");
		}
		echo(gVars.webEnv, " /> ");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Automatically discard spam comments on posts older than a month.");
		echo(gVars.webEnv, "</label></p>\n\t<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Update options &raquo;");
		echo(gVars.webEnv, "\" /></p>\n</form>\n</div>\n</div>\n");
	}

	public String akismet_verify_key(String key) {
		String blog = null;
		Object response = null;

		/* Do not change type */
		blog = URL.urlencode(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));
		if (booleanval(wpcom_api_key)) {
			key = wpcom_api_key;
		}
		response = akismet_http_post("key=" + key + "&blog=" + blog, "rest.akismet.com", "/1.1/verify-key", akismet_api_port);
		if (!is_array(response) || !isset(((Array) response).getValue(1)) || !equal(((Array) response).getValue(1), "valid") && !equal(((Array) response).getValue(1), "invalid")) {
			return "failed";
		}
		return strval(((Array) response).getValue(1));
	}

	public void akismet_warning() {
		echo(gVars.webEnv, "\n\t\t<div id=\'akismet-warning\' class=\'updated fade\'><p><strong>"
		        + getIncluded(L10nPage.class, gVars, gConsts).__("Akismet is almost ready.")
		        + "</strong> "
		        + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("You must <a href=\"%1$s\">enter your WordPress.com API key</a> for it to work."),
		                "plugins.php?page=akismet-key-config") + "</p></div>\n\t\t");
	}

	// Returns array with headers in $response[0] and body in $response[1]
	public Array<String> akismet_http_post(String request, String host, String path, int port) {
		String http_request = null;
		Array<String> response = null;
		int fs = 0;
		Ref<Integer> errno = new Ref<Integer>();
		Ref<String> errstr = new Ref<String>();
		http_request = "POST " + path + " HTTP/1.0\r\n";
		http_request = http_request + "Host: " + host + "\r\n";
		http_request = http_request + "Content-Type: application/x-www-form-urlencoded; charset="
		        + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\r\n";
		http_request = http_request + "Content-Length: " + strval(Strings.strlen(request)) + "\r\n";
		http_request = http_request + "User-Agent: WordPress/" + gVars.wp_version + " | Akismet/2.0\r\n";
		http_request = http_request + "\r\n";
		http_request = http_request + request;
		response = new Array<String>();
		if (!equal(false, fs = FileSystemOrSocket.fsockopen(gVars.webEnv, host, port, errno, errstr, 10))) {
			FileSystemOrSocket.fwrite(gVars.webEnv, fs, http_request);
			String responseStr = "";
			while (!FileSystemOrSocket.feof(gVars.webEnv, fs))
				responseStr = responseStr + FileSystemOrSocket.fgets(gVars.webEnv, fs, 1160); // One TCP-IP packet
			FileSystemOrSocket.fclose(gVars.webEnv, fs);
			response = Strings.explode("\r\n\r\n", responseStr, 2);
		}
		return response;
	}

	public Array<Object> akismet_auto_check_comment(Array<Object> comment) {
		Array<Object> ignore = new Array<Object>();
		String key = null;
		Object value = null;
		String query_string = null;
		String data = null;
		Array<String> response = null;
		StdClass post = null;
		int last_updated = 0;
		float diff = 0;
		comment.putValue("user_ip", QRegExPerl.preg_replace("/[^0-9., ]/", "", gVars.webEnv.getRemoteAddr()));
		comment.putValue("user_agent", gVars.webEnv.getHttpUserAgent());
		comment.putValue("referrer", gVars.webEnv.getHttpReferer());
		comment.putValue("blog", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
		ignore = new Array<Object>(new ArrayEntry<Object>("HTTP_COOKIE"));
		for (Map.Entry javaEntry348 : gVars.webEnv._SERVER.entrySet()) {
			key = strval(javaEntry348.getKey());
			value = javaEntry348.getValue();
			if (!Array.in_array(key, ignore)) {
				comment.putValue(key, value);
			}
		}
		query_string = "";
		for (Map.Entry javaEntry349 : comment.entrySet()) {
			key = strval(javaEntry349.getKey());
			data = strval(javaEntry349.getValue());
			query_string = query_string + key + "=" + URL.urlencode(Strings.stripslashes(gVars.webEnv, data)) + "&";
		}
		response = akismet_http_post(query_string, akismet_api_host, "/1.1/comment-check", akismet_api_port);
		if (equal("true", response.getValue(1))) {
			getIncluded(PluginPage.class, gVars, gConsts).add_filter("pre_comment_approved", Callback.createCallbackArray(this, "createFunction_pre_comment_approved"));
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("akismet_spam_count", intval(getIncluded(FunctionsPage.class, gVars,
			        gConsts).get_option("akismet_spam_count")) + 1);
			(((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("akismet_spam_caught");
			post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(comment.getValue("comment_post_ID"));
			last_updated = QDateTime.strtotime(strval(StdClass.getValue(post, "post_modified_gmt")));
			diff = floatval(DateTime.time() - last_updated);
			diff = diff / floatval(86400);
			if (equal(StdClass.getValue(post, "post_type"), "post") && diff > floatval(30)
			        && equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_discard_month"), "true")) {
				System.exit();
			}
		}
		akismet_delete_old();
		return comment;
	}

	// Added by Numiton
	public String createFunction_pre_comment_approved(Object a) {
		return "spam";
	}

	public void akismet_delete_old() {
		String now_gmt = null;
		int n = 0;
		now_gmt = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
		gVars.wpdb.query("DELETE FROM " + gVars.wpdb.comments + " WHERE DATE_SUB(\'" + now_gmt + "\', INTERVAL 15 DAY) > comment_date_gmt AND comment_approved = \'spam\'");
		n = Math.mt_rand(1, 5000);
		if (equal(n, 11)) { // lucky number
			gVars.wpdb.query("OPTIMIZE TABLE " + gVars.wpdb.comments);
		}
	}

	public void akismet_submit_nonspam_comment(int comment_id) {
		StdClass comment;
		String query_string = null;
		Object key = null;
		String data = null;
		Array<String> response = null;
		comment_id = comment_id;
		comment = (StdClass) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_ID = \'" + comment_id + "\'");
		if (!booleanval(comment)) { // it was deleted
			return;
		}
		comment.fields.putValue("blog", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
		query_string = "";
		for (Map.Entry javaEntry350 : ClassHandling.get_object_vars(comment).entrySet()) {
			key = javaEntry350.getKey();
			data = strval(javaEntry350.getValue());
			query_string = query_string + strval(key) + "=" + URL.urlencode(Strings.stripslashes(gVars.webEnv, data)) + "&";
		}
		response = akismet_http_post(query_string, akismet_api_host, "/1.1/submit-ham", akismet_api_port);
	}

	public void akismet_submit_spam_comment(int comment_id) {
		StdClass comment;
		String query_string = null;
		String key = null;
		String data = null;
		Array<String> response = null;

		//		comment_id = intval(comment_id);

		comment = (StdClass) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_ID = \'" + comment_id + "\'");
		if (!booleanval(comment)) {
			return;
		}
		if (!equal("spam", StdClass.getValue(comment, "comment_approved"))) {
			return;
		}
		comment.fields.putValue("blog", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
		query_string = "";
		for (Map.Entry javaEntry351 : ClassHandling.get_object_vars(comment).entrySet()) {
			key = strval(javaEntry351.getKey());
			data = strval(javaEntry351.getValue());
			query_string = query_string + key + "=" + URL.urlencode(Strings.stripslashes(gVars.webEnv, data)) + "&";
		}
		response = akismet_http_post(query_string, akismet_api_host, "/1.1/submit-spam", akismet_api_port);
	}

	// Total spam in queue
	// get_option( 'akismet_spam_count' ) is the total caught ever
	public int akismet_spam_count(String type) {
		Object count;
		if (!booleanval(type)) { // total
			count = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("akismet_spam_count", "widget");
			if (strictEqual(null, count)) {
				if (true)
				/*Modified by Numiton*/
				{
					StdClass countObj = getIncluded(CommentPage.class, gVars, gConsts).wp_count_comments();
					count = intval(StdClass.getValue(countObj, "spam"));
				}
				else {
					count = intval(gVars.wpdb.get_var("SELECT COUNT(comment_ID) FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'spam\'"));
				}
				getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("akismet_spam_count", count, "widget", 3600);
			}
			return intval(count);
		}
		else
			if (equal("comments", type) || equal("comment", type)) { // comments
				type = "";
			}
			else { // pingback, trackback, ...
				type = gVars.wpdb.escape(type);
			}
		return intval(gVars.wpdb.get_var("SELECT COUNT(comment_ID) FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'spam\' AND comment_type=\'" + type + "\'"));
	}

	public Array<Object> akismet_spam_comments(String type, int page, int per_page) {
		int start = 0;
		int end = 0;
		page = page;
		if (page < 2) {
			page = 1;
		}
		per_page = per_page;
		if (per_page < 1) {
			per_page = 50;
		}
		start = (page - 1) * per_page;
		end = start + per_page;
		if (booleanval(type)) {
			if (equal("comments", type) || equal("comment", type)) {
				type = "";
			}
			else
				type = gVars.wpdb.escape(type);
			return gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'spam\' AND comment_type=\'" + type + "\' ORDER BY comment_date DESC LIMIT " + start
			        + ", " + end);
		}
		
		// All
		return gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'spam\' ORDER BY comment_date DESC LIMIT " + start + ", " + end);
	}

	// Totals for each comment type
	// returns array( type => count, ... )
	public Array<Object> akismet_spam_totals() {
		Array<Object> totals = new Array<Object>();
		Array<Object> _return = new Array<Object>();
		StdClass total = null;
		totals = gVars.wpdb.get_results("SELECT comment_type, COUNT(*) AS cc FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'spam\' GROUP BY comment_type");
		_return = new Array<Object>();
		for (Map.Entry javaEntry352 : totals.entrySet()) {
			total = (StdClass) javaEntry352.getValue();
			_return.putValue(booleanval(StdClass.getValue(total, "comment_type")) ? StdClass.getValue(total, "comment_type") : "comment", StdClass.getValue(total, "cc"));
		}
		return _return;
	}

	public void akismet_manage_page() {
		String count = null;
		count = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Akismet Spam (%s)"), akismet_spam_count(strval(false)));
		if (isset(gVars.submenu.getValue("edit-comments.php"))) {
			(((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).add_submenu_page("edit-comments.php", (((L10nPage) PhpWeb
			        .getIncluded(L10nPage.class, gVars, gConsts))).__("Akismet Spam"), count, "moderate_comments", "akismet-admin", Callback.createCallbackArray(this, "akismet_caught"));
		}
		else
			if (true)
			/*Modified by Numiton*/
			{
				(((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).add_management_page(getIncluded(
				        L10nPage.class, gVars, gConsts).__("Akismet Spam"), count, "moderate_comments", "akismet-admin", Callback.createCallbackArray(this, "akismet_caught"));
			}
	}

	public void akismet_caught() {
		int i = 0;
		String to = null;
		String delete_time = null;
		int nuked = 0;
		String link = null;
		int count = 0;
		int spam_count = 0;
		String s = null;
		Array<Object> comments = new Array<Object>();
		int page = 0;
		String current_type;
		int total = 0;
		Array<Object> totals = new Array<Object>();
		String type = null;
		String show = null;
		int type_count = 0;
		String extra = null;
		float total_pages = 0;
		String r = null;
		Array<Object> args = new Array<Object>();
		int page_num = 0;
		boolean p = false;
		boolean in = false;
		Object comment_date = null;
		StdClass post = null;
		String post_title = null;
		String _class = null;
		akismet_recheck_queue();
		if (isset(gVars.webEnv._POST.getValue("submit")) && equal("recover", gVars.webEnv._POST.getValue("action")) && !empty(gVars.webEnv._POST.getValue("not_spam"))) {
			getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer(akismet_nonce);
			if (true && /*Modified by Numiton*/!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("moderate_comments")) {
				System.exit(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permission to moderate comments."));
			}
			i = 0;
			for (Map.Entry javaEntry353 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("not_spam").entrySet()) {

				// Modified by Numiton

				int commentId = intval(javaEntry353.getValue());
				if (true)
				/*Modified by Numiton*/
				{
					getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(commentId, "approve");
				}
				else
					gVars.wpdb.query("UPDATE " + gVars.wpdb.comments + " SET comment_approved = \'1\' WHERE comment_ID = \'" + commentId + "\'");
				akismet_submit_nonspam_comment(commentId);
				++i;
			}
			to = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("recovered", i, gVars.webEnv.getHttpReferer());
			getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(to);
			System.exit();
		}
		if (equal("delete", gVars.webEnv._POST.getValue("action"))) {
			getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer(akismet_nonce);
			if (true && /*Modified by Numiton*/!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("moderate_comments")) {
				System.exit(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permission to moderate comments."));
			}
			delete_time = gVars.wpdb.escape(strval(gVars.webEnv._POST.getValue("display_time")));
			nuked = gVars.wpdb.query("DELETE FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'spam\' AND \'" + delete_time + "\' > comment_date_gmt");
			getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("akismet_spam_count", "widget");
			to = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("deleted", "all", gVars.webEnv.getHttpReferer());
			getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(to);
			System.exit();
		}
		if (isset(gVars.webEnv._GET.getValue("recovered"))) {
			i = intval(gVars.webEnv._GET.getValue("recovered"));
			echo(gVars.webEnv, "<div class=\"updated\"><p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%1$s comments recovered."), i) + "</p></div>");
		}
		if (isset(gVars.webEnv._GET.getValue("deleted"))) {
			echo(gVars.webEnv, "<div class=\"updated\"><p>" + getIncluded(L10nPage.class, gVars, gConsts).__("All spam deleted.") + "</p></div>");
		}
		if (isset(gVars.submenu.getValue("edit-comments.php"))) {
			link = "edit-comments.php";
		}
		else
			link = "edit.php";
		echo(
		        gVars.webEnv,
		        "<style type=\"text/css\">\n.akismet-tabs {\n\tlist-style: none;\n\tmargin: 0;\n\tpadding: 0;\n\tclear: both;\n\tborder-bottom: 1px solid #ccc;\n\theight: 31px;\n\tmargin-bottom: 20px;\n\tbackground: #ddd;\n\tborder-top: 1px solid #bdbdbd;\n}\n.akismet-tabs li {\n\tfloat: left;\n\tmargin: 5px 0 0 20px;\n}\n.akismet-tabs a {\n\tdisplay: block;\n\tpadding: 4px .5em 3px;\n\tborder-bottom: none;\n\tcolor: #036;\n}\n.akismet-tabs .active a {\n\tbackground: #fff;\n\tborder: 1px solid #ccc;\n\tborder-bottom: none;\n\tcolor: #000;\n\tfont-weight: bold;\n\tpadding-bottom: 4px;\n}\n#akismetsearch {\n\tfloat: right;\n\tmargin-top: -.5em;\n}\n\n#akismetsearch p {\n\tmargin: 0;\n\tpadding: 0;\n}\n</style>\n<div class=\"wrap\">\n<h2>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Caught Spam");
		echo(gVars.webEnv, "</h2>\n");
		count = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_spam_count"));
		if (booleanval(count)) {
			echo(gVars.webEnv, "<p>");
			QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Akismet has caught <strong>%1$s spam</strong> for you since you first installed it."),
			        number_format_i18n(count, 0));
			echo(gVars.webEnv, "</p>\n");
		}
		spam_count = akismet_spam_count(strval(false));
		if (equal(0, spam_count)) {
			echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("You have no spam currently in the queue. Must be your lucky day. :)") + "</p>");
			echo(gVars.webEnv, "</div>");
		}
		else {
			echo(
			        gVars.webEnv,
			        "<p>"
			                + getIncluded(L10nPage.class, gVars, gConsts)
			                        .__("You can delete all of the spam from your database with a single click. This operation cannot be undone, so you may wish to check to ensure that no legitimate comments got through first. Spam is automatically deleted after 15 days, so don&#8217;t sweat it.")
			                + "</p>");
			if (!isset(gVars.webEnv._POST.getValue("s"))) {
				echo(gVars.webEnv, "<form method=\"post\" action=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FunctionsPage.class, gVars,
				        gConsts).add_query_arg("noheader", "true")));
				echo(gVars.webEnv, "\">\n");
				this.akismet_nonce_field(akismet_nonce);
				echo(gVars.webEnv, "<input type=\"hidden\" name=\"action\" value=\"delete\" />\n");
				QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("There are currently %1$s comments identified as spam."), spam_count);
				echo(gVars.webEnv, "&nbsp; &nbsp; <input type=\"submit\" class=\"button delete\" name=\"Submit\" value=\"");
				getIncluded(L10nPage.class, gVars, gConsts)._e("Delete all");
				echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"display_time\" value=\"");
				echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
				echo(gVars.webEnv, "\" />\n</form>\n");
			}
			echo(gVars.webEnv, "</div>\n<div class=\"wrap\">\n");
			if (isset(gVars.webEnv._POST.getValue("s"))) {
				echo(gVars.webEnv, "<h2>");
				getIncluded(L10nPage.class, gVars, gConsts)._e("Search");
				echo(gVars.webEnv, "</h2>\n");
			}
			else {
				echo(
				        gVars.webEnv,
				        "<p>"
				                + getIncluded(L10nPage.class, gVars, gConsts)
				                        .__("These are the latest comments identified as spam by Akismet. If you see any mistakes, simply mark the comment as \"not spam\" and Akismet will learn from the submission. If you wish to recover a comment from spam, simply select the comment, and click Not Spam. After 15 days we clean out the junk for you.")
				                + "</p>");
			}
			if (isset(gVars.webEnv._POST.getValue("s"))) {
				s = gVars.wpdb.escape(strval(gVars.webEnv._POST.getValue("s")));
				comments = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + "  WHERE\n\t\t(comment_author LIKE \'%" + s + "%\' OR\n\t\tcomment_author_email LIKE \'%" + s
				        + "%\' OR\n\t\tcomment_author_url LIKE (\'%" + s + "%\') OR\n\t\tcomment_author_IP LIKE (\'%" + s + "%\') OR\n\t\tcomment_content LIKE (\'%" + s
				        + "%\') ) AND\n\t\tcomment_approved = \'spam\'\n\t\tORDER BY comment_date DESC");
			}
			else {
				if (isset(gVars.webEnv._GET.getValue("apage"))) {
					page = intval(gVars.webEnv._GET.getValue("apage"));
				}
				else
					page = 1;
				if (page < 2) {
					page = 1;
				}
				current_type = strval(false);
				if (isset(gVars.webEnv._GET.getValue("ctype"))) {
					current_type = QRegExPerl.preg_replace("|[^a-z]|", "", strval(gVars.webEnv._GET.getValue("ctype")));
				}
				comments = akismet_spam_comments(current_type, 1, 50);
				total = akismet_spam_count(current_type);
				totals = akismet_spam_totals();
				echo(gVars.webEnv, "<ul class=\"akismet-tabs\">\n<li ");
				if (!isset(gVars.webEnv._GET.getValue("ctype"))) {
					echo(gVars.webEnv, " class=\"active\"");
				}
				echo(gVars.webEnv, "><a href=\"edit-comments.php?page=akismet-admin\">");
				getIncluded(L10nPage.class, gVars, gConsts)._e("All");
				echo(gVars.webEnv, "</a></li>\n");
				for (Map.Entry javaEntry354 : totals.entrySet()) {
					type = strval(javaEntry354.getKey());
					type_count = intval(javaEntry354.getValue());
					if (equal("comment", type)) {
						type = "comments";
						show = getIncluded(L10nPage.class, gVars, gConsts).__("Comments");
					}
					else {
						show = Strings.ucwords(type);
					}
					String type_countStr = number_format_i18n(type_count, 0);
					extra = (strictEqual(current_type, type) ? " class=\"active\"" : "");
					echo(gVars.webEnv, "<li " + extra + "><a href=\'edit-comments.php?page=akismet-admin&amp;ctype=" + type + "\'>" + show + " (" + type_countStr + ")</a></li>");
				}
				(((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("akismet_tabs"); // so plugins can add more tabs easily
				echo(gVars.webEnv, "</ul>\n");
			}
			if (booleanval(comments)) {
				echo(gVars.webEnv, "<form method=\"post\" action=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(link + "?page=akismet-admin"));
				echo(gVars.webEnv, "\" id=\"akismetsearch\">\n<p>  <input type=\"text\" name=\"s\" value=\"");
				if (isset(gVars.webEnv._POST.getValue("s"))) {
					echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._POST.getValue("s"))));
				}
				echo(gVars.webEnv, "\" size=\"17\" />\n  <input type=\"submit\" class=\"button\" name=\"submit\" value=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts)
				        .__("Search Spam &raquo;")));
				echo(gVars.webEnv, "\"  />  </p>\n</form>\n");
				if (total > 50) {
					total_pages = Math.ceil(floatval(total) / floatval(50));
					r = "";
					if (1 < page) {
						args.putValue("apage", equal(1, page - 1) ? "" : strval(page - 1));
						r = r
						        + "<a class=\"prev\" href=\""
						        + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts)
						                .add_query_arg(args)) + "\">" + getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Previous Page") + "</a>" + "\n";
					}
					if ((total_pages = Math.ceil(floatval(total) / floatval(50))) > floatval(1)) {
						for (page_num = 1; floatval(page_num) <= total_pages; page_num++) {
							if (equal(page, page_num)) {
								r = r + "<strong>" + strval(page_num) + "</strong>\n";
							}
							else {
								p = false;
								if (page_num < 3 || page_num >= page - 3 && page_num <= page + 3 || floatval(page_num) > total_pages - floatval(3)) {
									args.putValue("apage", equal(1, page_num) ? "" : strval(page_num));
									r = r
									        + "<a class=\"page-numbers\" href=\""
									        + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars,
									                gConsts).add_query_arg(args)) + "\">" + strval(page_num) + "</a>\n";
									in = true;
								}
								else
									if (equal(in, true)) {
										r = r + "...\n";
										in = false;
									}
									else {
									}
							}
						}
					}
					if (page * 50 < total || equal(-1, total)) {
						args.putValue("apage", page + 1);
						r = r
						        + "<a class=\"next\" href=\""
						        + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts)
						                .add_query_arg(args)) + "\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Next Page &raquo;") + "</a>" + "\n";
					}
					echo(gVars.webEnv, "<p>" + r + "</p>");
					echo(gVars.webEnv, "\n");
				}
				echo(gVars.webEnv, "<form style=\"clear: both;\" method=\"post\" action=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FunctionsPage.class, gVars,
				        gConsts).add_query_arg("noheader", "true")));
				echo(gVars.webEnv, "\">\n");
				this.akismet_nonce_field(akismet_nonce);
				echo(gVars.webEnv, "<input type=\"hidden\" name=\"action\" value=\"recover\" />\n<ul id=\"spam-list\" class=\"commentlist\" style=\"list-style: none; margin: 0; padding: 0;\">\n");
				i = 0;
				for (Map.Entry javaEntry355 : comments.entrySet()) {
					gVars.comment = (StdClass) javaEntry355.getValue();
					i++;
					comment_date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(getIncluded(FunctionsPage.class, gVars, gConsts)
					        .get_option("date_format")
					        + " @ " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format"), strval(StdClass.getValue(gVars.comment, "comment_date")));
					post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(intval(StdClass.getValue(gVars.comment, "comment_post_ID")));
					post_title = strval(StdClass.getValue(post, "post_title"));
					if (booleanval(i % 2)) {
						_class = "class=\"alternate\"";
					}
					else
						_class = "";
					echo(gVars.webEnv, "\n\t<li id=\'comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID")) + "\' " + _class + ">");
					echo(gVars.webEnv, "\n<p><strong>");
					getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author();
					echo(gVars.webEnv, "</strong> ");
					if (booleanval(StdClass.getValue(gVars.comment, "comment_author_email"))) {
						echo(gVars.webEnv, "| ");
						getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_email_link();
						echo(gVars.webEnv, " ");
					}
					if (booleanval(StdClass.getValue(gVars.comment, "comment_author_url")) && !equal("http://", StdClass.getValue(gVars.comment, "comment_author_url"))) {
						echo(gVars.webEnv, " | ");
						getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_url_link();
						echo(gVars.webEnv, " ");
					}
					echo(gVars.webEnv, "| ");
					getIncluded(L10nPage.class, gVars, gConsts)._e("IP:");
					echo(gVars.webEnv, " <a href=\"http://ws.arin.net/cgi-bin/whois.pl?queryinput=");
					getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_IP();
					echo(gVars.webEnv, "\">");
					getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_IP();
					echo(gVars.webEnv, "</a></p>\n\n");
					getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
					echo(gVars.webEnv, "\n<p><label for=\"spam-");
					echo(gVars.webEnv, StdClass.getValue(gVars.comment, "comment_ID"));
					echo(gVars.webEnv, "\">\n<input type=\"checkbox\" id=\"spam-");
					echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_ID")));
					echo(gVars.webEnv, "\" name=\"not_spam[]\" value=\"");
					echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_ID")));
					echo(gVars.webEnv, "\" />\n");
					getIncluded(L10nPage.class, gVars, gConsts)._e("Not Spam");
					echo(gVars.webEnv, "</label> &#8212; ");
					getIncluded(Comment_templatePage.class, gVars, gConsts).comment_date("M j, g:i A");
					echo(gVars.webEnv, " &#8212; [\n");
					post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(intval(StdClass.getValue(gVars.comment, "comment_post_ID")));
					post_title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(post, "post_title")), "double");
					post_title = (equal("", post_title) ? ("# " + intval(StdClass.getValue(gVars.comment, "comment_post_ID"))) : post_title);
					echo(gVars.webEnv, " <a href=\"");
					echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(intval(StdClass.getValue(gVars.comment, "comment_post_ID"))));
					echo(gVars.webEnv, "\" title=\"");
					echo(gVars.webEnv, post_title);
					echo(gVars.webEnv, "\">");
					getIncluded(L10nPage.class, gVars, gConsts)._e("View Post");
					echo(gVars.webEnv, "</a> ] </p>\n\n\n");
				}
				echo(gVars.webEnv, "</ul>\n");
				if (total > 50) {
					total_pages = Math.ceil(floatval(total) / floatval(50));
					r = "";
					if (1 < page) {
						args.putValue("apage", equal(1, page - 1) ? "" : strval(page - 1));
						r = r
						        + "<a class=\"prev\" href=\""
						        + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts)
						                .add_query_arg(args)) + "\">" + getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Previous Page") + "</a>" + "\n";
					}
					if ((total_pages = Math.ceil(floatval(total) / floatval(50))) > floatval(1)) {
						for (page_num = 1; floatval(page_num) <= total_pages; page_num++) {
							if (equal(page, page_num)) {
								r = r + "<strong>" + strval(page_num) + "</strong>\n";
							}
							else {
								p = false;
								if (page_num < 3 || page_num >= page - 3 && page_num <= page + 3 || floatval(page_num) > total_pages - floatval(3)) {
									args.putValue("apage", equal(1, page_num) ? "" : strval(page_num));
									r = r
									        + "<a class=\"page-numbers\" href=\""
									        + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars,
									                gConsts).add_query_arg(args)) + "\">" + strval(page_num) + "</a>\n";
									in = true;
								}
								else
									if (equal(in, true)) {
										r = r + "...\n";
										in = false;
									}
									else {
									}
							}
						}
					}
					if (page * 50 < total || equal(-1, total)) {
						args.putValue("apage", page + 1);
						r = r
						        + "<a class=\"next\" href=\""
						        + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts)
						                .add_query_arg(args)) + "\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Next Page &raquo;") + "</a>" + "\n";
					}
					echo(gVars.webEnv, "<p>" + r + "</p>");
				}
				echo(gVars.webEnv, "<p class=\"submit\">\n<input type=\"submit\" name=\"submit\" value=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts)
				        .__("De-spam marked comments &raquo;")));
				echo(gVars.webEnv, "\" />\n</p>\n<p>");
				getIncluded(L10nPage.class, gVars, gConsts)._e("Comments you de-spam will be submitted to Akismet as mistakes so it can learn and get better.");
				echo(gVars.webEnv, "</p>\n</form>\n");
			}
			else {
				echo(gVars.webEnv, "<p>");
				getIncluded(L10nPage.class, gVars, gConsts)._e("No results found.");
				echo(gVars.webEnv, "</p>\n");
			}
			echo(gVars.webEnv, "\n");
			if (!isset(gVars.webEnv._POST.getValue("s"))) {
				echo(gVars.webEnv, "<form method=\"post\" action=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FunctionsPage.class, gVars,
				        gConsts).add_query_arg("noheader", "true")));
				echo(gVars.webEnv, "\">\n");
				this.akismet_nonce_field(akismet_nonce);
				echo(gVars.webEnv, "<p><input type=\"hidden\" name=\"action\" value=\"delete\" />\n");
				QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("There are currently %1$s comments identified as spam."), spam_count);
				echo(gVars.webEnv, "&nbsp; &nbsp; <input type=\"submit\" name=\"Submit\" class=\"button\" value=\"");
				echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts)
				        .__("Delete all")));
				echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"display_time\" value=\"");
				echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
				echo(gVars.webEnv, "\" /></p>\n</form>\n");
			}
			echo(gVars.webEnv, "</div>\n");
		}
	}

	/** 
	 * WP < 2.5
	 */
	public void akismet_stats() {
		int count;
		String path = null;
		String link = null;
		if (!true || /*Modified by Numiton*/booleanval(getIncluded(PluginPage.class, gVars, gConsts).did_action("rightnow_end"))) { // We already displayed this info in the "Right Now" section
			return;
		}
		if (!booleanval((count = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_spam_count"))))) {
			return;
		}
		path = getIncluded(PluginPage.class, gVars, gConsts).plugin_basename(SourceCodeInfo.getCurrentFile(gVars.webEnv));
		echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Spam") + "</h3>");
		if (isset(gVars.submenu.getValue("edit-comments.php"))) {
			link = "edit-comments.php";
		}
		else
			link = "edit.php";
		echo(gVars.webEnv, "<p>"
		        + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)
		                .__("<a href=\"%1$s\">Akismet</a> has protected your site from <a href=\"%2$s\">%3$s spam comments</a>."), "http://akismet.com/", getIncluded(
		                FormattingPage.class, gVars, gConsts).clean_url(link + "?page=akismet-admin"), number_format_i18n(count, intval(null))) + "</p>");
	}

	/** 
	 * WP 2.5+
	 */
	public void akismet_rightnow() {
		String link = null;
		int count = 0;
		String intro = null;
		int queue_count = 0;
		String queue_text = null;
		String text = null;
		if (isset(gVars.submenu.getValue("edit-comments.php"))) {
			link = "edit-comments.php";
		}
		else
			link = "edit.php";
		if (booleanval(count = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_spam_count")))) {
			intro = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
			        "<a href=\"%1$s\">Akismet</a> has protected your site from %2$s spam comment already,", "<a href=\"%1$s\">Akismet</a> has protected your site from %2$s spam comments already,",
			        count), "http://akismet.com/", number_format_i18n(count, 0));
		}
		else {
			intro = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"%1$s\">Akismet</a> blocks spam from getting to your blog,"), "http://akismet.com/");
		}
		if (booleanval(queue_count = akismet_spam_count(strval(false)))) {
			queue_text = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("and there\'s <a href=\"%2$s\">%1$s comment</a> in your spam queue right now.",
			        "and there are <a href=\"%2$s\">%1$s comments</a> in your spam queue right now.", queue_count), number_format_i18n(queue_count, 0), getIncluded(
			        FormattingPage.class, gVars, gConsts).clean_url(link + "?page=akismet-admin"));
		}
		else {
			queue_text = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("but there\'s nothing in your <a href=\'%1$s\'>spam queue</a> at the moment."),
			        getIncluded(FormattingPage.class, gVars, gConsts).clean_url(link + "?page=akismet-admin"));
		}
		text = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s %2$s|akismet_rightnow"), intro, queue_text);
		echo(gVars.webEnv, "<p class=\'akismet-right-now\'>" + text + "</p>\n");
	}

	// For WP <= 2.3.x
	public String akismet_recheck_button(String page) {
		String link = null;
		String button = null;
		if (isset(gVars.submenu.getValue("edit-comments.php"))) {
			link = "edit-comments.php";
		}
		else
			link = "edit.php";
		button = "<a href=\'"
		        + link
		        + "?page=akismet-admin&amp;recheckqueue=1&amp;noheader=1\' style=\'display: block; width: 100px; position: absolute; right: 7%; padding: 5px; font-size: 14px; text-decoration: underline; background: #fff; border: 1px solid #ccc;\'>"
		        + getIncluded(L10nPage.class, gVars, gConsts).__("Recheck Queue for Spam") + "</a>";
		page = Strings.str_replace("<div class=\"wrap\">", "<div class=\"wrap\">" + button, page);
		return page;
	}

	// For WP >= 2.5
	public void akismet_check_for_spam_button(Object comment_status) {
		StdClass count = null;
		if (!equal("moderated", comment_status)) {
			return;
		}
		count = getIncluded(CommentPage.class, gVars, gConsts).wp_count_comments();
		if (!empty(StdClass.getValue(count, "moderated"))) {
			echo(gVars.webEnv, "<a href=\'edit-comments.php?page=akismet-admin&amp;recheckqueue=true&amp;noheader=true\'>"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Check for Spam") + "</a>");
		}
	}

	public void akismet_recheck_queue() {
		Array moderation = new Array();
		Array<Object> c = new Array<Object>();
		int id = 0;
		String query_string = null;
		Object key = null;
		String data = null;
		Array<String> response = null;
		if (!isset(gVars.webEnv._GET.getValue("recheckqueue"))) {
			return;
		}
		moderation = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'0\'", gConsts.getARRAY_A());
		for (Map.Entry javaEntry356 : (Set<Map.Entry>) moderation.entrySet()) {
			c = (Array<Object>) javaEntry356.getValue();
			c.putValue("user_ip", c.getValue("comment_author_IP"));
			c.putValue("user_agent", c.getValue("comment_agent"));
			c.putValue("referrer", "");
			c.putValue("blog", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
			id = intval(c.getValue("comment_ID"));
			query_string = "";
			for (Map.Entry javaEntry357 : c.entrySet()) {
				key = javaEntry357.getKey();
				data = strval(javaEntry357.getValue());
				query_string = query_string + strval(key) + "=" + URL.urlencode(Strings.stripslashes(gVars.webEnv, data)) + "&";
			}
			response = akismet_http_post(query_string, akismet_api_host, "/1.1/comment-check", akismet_api_port);
			if (equal("true", response.getValue(1))) {
				gVars.wpdb.query("UPDATE " + gVars.wpdb.comments + " SET comment_approved = \'spam\' WHERE comment_ID = " + id);
			}
		}
		getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.webEnv.getHttpReferer());
		System.exit();
	}

	public Object akismet_check_db_comment(int id) {
		Array<Object> c = new Array<Object>();
		String query_string = null;
		Object key = null;
		String data = null;
		Array response = null;
		id = id;
		c = (Array<Object>) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_ID = \'" + id + "\'", gConsts.getARRAY_A());
		if (!booleanval(c)) {
			return null;
		}
		c.putValue("user_ip", c.getValue("comment_author_IP"));
		c.putValue("user_agent", c.getValue("comment_agent"));
		c.putValue("referrer", "");
		c.putValue("blog", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
		id = intval(c.getValue("comment_ID"));
		query_string = "";
		for (Map.Entry javaEntry358 : c.entrySet()) {
			key = javaEntry358.getKey();
			data = strval(javaEntry358.getValue());
			query_string = query_string + strval(key) + "=" + URL.urlencode(Strings.stripslashes(gVars.webEnv, data)) + "&";
		}
		response = (Array) akismet_http_post(query_string, akismet_api_host, "/1.1/comment-check", akismet_api_port);
		return response.getValue(1);
	}

	/** 
	 * This option causes tons of FPs, was removed in 2.1
	 */
	public int akismet_kill_proxy_check(Object option) {
		return 0;
	}

	// Widget stuff
	public void widget_akismet(Array<Object> args) {
		Array<Object> options;
		String count = null;
		Object before_widget = null;
		Object before_title = null;
		Object after_title = null;
		Object after_widget = null;
		{
			before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
			before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE);
			after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
			after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
		}
		options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_akismet");
		count = number_format_i18n(intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_spam_count")), intval(null));
		echo(gVars.webEnv, "\t\t\t");
		echo(gVars.webEnv, before_widget);
		echo(gVars.webEnv, "\t\t\t\t");
		echo(gVars.webEnv, strval(before_title) + strval(options.getValue("title")) + strval(after_title));
		echo(gVars.webEnv, "\t\t\t\t<div id=\"akismetwrap\"><div id=\"akismetstats\"><a id=\"aka\" href=\"http://akismet.com\" title=\"\">");
		QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("%1$s %2$sspam comments%3$s %4$sblocked by%5$s<br />%6$sAkismet%7$s"),
		        "<div id=\"akismet1\"><span id=\"akismetcount\">" + count + "</span>", "<span id=\"akismetsc\">", "</span></div>", "<div id=\"akismet2\"><span id=\"akismetbb\">", "</span>",
		        "<span id=\"akismeta\">", "</span></div>");
		echo(gVars.webEnv, "</a></div></div>\n\t\t\t");
		echo(gVars.webEnv, after_widget);
		echo(gVars.webEnv, "\t");
	}

	public void widget_akismet_style() {
		echo(
		        gVars.webEnv,
		        "<style type=\"text/css\">\n#aka,#aka:link,#aka:hover,#aka:visited,#aka:active{color:#fff;text-decoration:none}\n#aka:hover{border:none;text-decoration:none}\n#aka:hover #akismet1{display:none}\n#aka:hover #akismet2,#akismet1{display:block}\n#akismet2{display:none;padding-top:2px}\n#akismeta{font-size:16px;font-weight:bold;line-height:18px;text-decoration:none}\n#akismetcount{display:block;font:15px Verdana,Arial,Sans-Serif;font-weight:bold;text-decoration:none}\n#akismetwrap #akismetstats{background:url(");
		echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
		echo(
		        gVars.webEnv,
		        "/wp-content/plugins/akismet/akismet.gif) no-repeat top left;border:none;color:#fff;font:11px \'Trebuchet MS\',\'Myriad Pro\',sans-serif;height:40px;line-height:100%;overflow:hidden;padding:8px 0 0;text-align:center;width:120px}\n</style>\n\t\t");
	}

	// Counter for non-widget users
	public void widget_akismet_control() {
		Array<Object> options = new Array<Object>();
		Array<Object> newoptions = new Array<Object>();
		String title = null;
		options = Array.arrayCopy(newoptions = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_akismet"));
		if (booleanval(gVars.webEnv._POST.getValue("akismet-submit"))) {
			newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("akismet-title")))));
			if (empty(newoptions.getValue("title"))) {
				newoptions.putValue("title", "Spam Blocked");
			}
		}
		if (!equal(options, newoptions)) {
			options = newoptions;
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_akismet", options);
		}
		title = Strings.htmlspecialchars(strval(options.getValue("title")), Strings.ENT_QUOTES);
		echo(gVars.webEnv, "\t\t\t\t<p><label for=\"akismet-title\">");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Title:");
		echo(gVars.webEnv, " <input style=\"width: 250px;\" id=\"akismet-title\" name=\"akismet-title\" type=\"text\" value=\"");
		echo(gVars.webEnv, title);
		echo(gVars.webEnv, "\" /></label></p>\n\t\t\t\t<input type=\"hidden\" id=\"akismet-submit\" name=\"akismet-submit\" value=\"1\" />\n\t");
	}

	/** 
	 * Widget stuff Widget stuff
	 */
	public void widget_akismet_register() {
		if (true)
		/*Modified by Numiton*/
		{
			getIncluded(WidgetsPage.class, gVars, gConsts).register_sidebar_widget("Akismet", "widget_akismet", null, "akismet");
			getIncluded(WidgetsPage.class, gVars, gConsts).register_widget_control("Akismet", "widget_akismet_control", null, 75, "akismet");
			if (booleanval(getIncluded(WidgetsPage.class, gVars, gConsts).is_active_widget("widget_akismet"))) {
				getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(this, "widget_akismet_style"));
			}
		}
		else {
		}
	}

	/** 
	 * Counter for non-widget users Counter for non-widget users
	 */
	public void akismet_counter() {
		String count = null;
		echo(
		        gVars.webEnv,
		        "<style type=\"text/css\">\n#akismetwrap #aka,#aka:link,#aka:hover,#aka:visited,#aka:active{color:#fff;text-decoration:none}\n#aka:hover{border:none;text-decoration:none}\n#aka:hover #akismet1{display:none}\n#aka:hover #akismet2,#akismet1{display:block}\n#akismet2{display:none;padding-top:2px}\n#akismeta{font-size:16px;font-weight:bold;line-height:18px;text-decoration:none}\n#akismetcount{display:block;font:15px Verdana,Arial,Sans-Serif;font-weight:bold;text-decoration:none}\n#akismetwrap #akismetstats{background:url(");
		echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
		echo(
		        gVars.webEnv,
		        "/wp-content/plugins/akismet/akismet.gif) no-repeat top left;border:none;color:#fff;font:11px \'Trebuchet MS\',\'Myriad Pro\',sans-serif;height:40px;line-height:100%;overflow:hidden;padding:8px 0 0;text-align:center;width:120px}\n</style>\n");
		count = number_format_i18n(doubleval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("akismet_spam_count")), 0);
		echo(gVars.webEnv, "<div id=\"akismetwrap\"><div id=\"akismetstats\"><a id=\"aka\" href=\"http://akismet.com\" title=\"\"><div id=\"akismet1\"><span id=\"akismetcount\">");
		echo(gVars.webEnv, count);
		echo(gVars.webEnv, "</span> <span id=\"akismetsc\">");
		getIncluded(L10nPage.class, gVars, gConsts)._e("spam comments");
		echo(gVars.webEnv, "</span></div> <div id=\"akismet2\"><span id=\"akismetbb\">");
		getIncluded(L10nPage.class, gVars, gConsts)._e("blocked by");
		echo(gVars.webEnv, "</span><br /><span id=\"akismeta\">Akismet</span></div></a></div></div>\n");
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_content_plugins_akismet_akismet_block1");
		gVars.webEnv = webEnv;
		
		/*
		Plugin Name: Akismet
		Plugin URI: http://akismet.com/
		Description: Akismet checks your comments against the Akismet web service to see if they look like spam or not. You need a <a href="http://wordpress.com/api-keys/">WordPress.com API key</a> to use it. You can review the spam it catches under "Comments." To show off your Akismet stats just put <code>&lt;?php akismet_counter(); ?></code> in your template. See also: <a href="http://wordpress.org/extend/plugins/stats/">WP Stats plugin</a>.
		Version: 2.1.4
		Author: Matt Mullenweg
		Author URI: http://photomatt.net/
		*/

		// If you hardcode a WP.com API key here, all key config screens will be hidden
		wpcom_api_key = "";
		(((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("init", Callback.createCallbackArray(this, "akismet_init"));
		if (!true)
		/*Modified by Numiton*/
		{
			akismet_nonce = strval(-1);
		}
		else {
			akismet_nonce = "akismet-update-key";
		}
		if (!true)
		/*Modified by Numiton*/
		{
		}
		if (!booleanval(((FunctionsPage) getIncluded(FunctionsPage.class, gVars, gConsts)).get_option("wordpress_api_key")) && !booleanval(wpcom_api_key)
		        && !isset(gVars.webEnv._POST.getValue("submit"))) {
			getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_notices", Callback.createCallbackArray(this, "akismet_warning"));
			return null;
		}
		getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_set_comment_status", Callback.createCallbackArray(this, "akismet_submit_spam_comment"));
		getIncluded(PluginPage.class, gVars, gConsts).add_action("edit_comment", Callback.createCallbackArray(this, "akismet_submit_spam_comment"));
		getIncluded(PluginPage.class, gVars, gConsts).add_action("preprocess_comment", Callback.createCallbackArray(this, "akismet_auto_check_comment"), 1);
		getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_menu", Callback.createCallbackArray(this, "akismet_manage_page"));
		getIncluded(PluginPage.class, gVars, gConsts).add_action("activity_box_end", Callback.createCallbackArray(this, "akismet_stats"));
		getIncluded(PluginPage.class, gVars, gConsts).add_action("rightnow_end", Callback.createCallbackArray(this, "akismet_rightnow"));
		if (equal("moderation.php", gVars.pagenow)) {
			if (booleanval(gVars.wpdb.get_var("SELECT COUNT(*) FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'0\'"))) {
				OutputControl.ob_start(gVars.webEnv, "akismet_recheck_button");
			}
		}
		getIncluded(PluginPage.class, gVars, gConsts).add_action("manage_comments_nav", Callback.createCallbackArray(this, "akismet_check_for_spam_button"));
		getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_open_proxy_check", Callback.createCallbackArray(this, "akismet_kill_proxy_check"));
		getIncluded(PluginPage.class, gVars, gConsts).add_action("init", Callback.createCallbackArray(this, "widget_akismet_register"));
		return DEFAULT_VAL;
	}
	public String	wpcom_api_key;
	public String	akismet_api_host;
	public int	  akismet_api_port;
	public String	akismet_nonce;
}
