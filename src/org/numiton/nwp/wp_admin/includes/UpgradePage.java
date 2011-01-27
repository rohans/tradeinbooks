/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UpgradePage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.*;
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
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PostPage;
import org.numiton.nwp.wp_includes.UserPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;

@Controller(value="wp_admin/includes/UpgradePage")
@Scope("request")
public class UpgradePage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(UpgradePage.class.getName());

	@Override
	@RequestMapping("/wp-admin/includes/upgrade.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_admin/includes/upgrade";
	}

	public Array<Object> wp_install(String blog_title, String user_name, String user_email, boolean _public, String deprecated) {
		String schema = null;
		Object guessurl = null;
		int user_id = 0;
		String random_password = null;
		WP_User user = null;
		wp_check_mysql_version();
		getIncluded(CachePage.class, gVars, gConsts).wp_cache_flush();
		make_db_current_silent();
		getIncluded(SchemaPage.class, gVars, gConsts).populate_options();
		getIncluded(SchemaPage.class, gVars, gConsts).populate_roles();
		getIncluded(FunctionsPage.class, gVars, gConsts).update_option("blogname", blog_title);
		getIncluded(FunctionsPage.class, gVars, gConsts).update_option("admin_email", user_email);
		getIncluded(FunctionsPage.class, gVars, gConsts).update_option("blog_public", _public);
		schema = ((isset(gVars.webEnv.getHttps()) && equal(Strings.strtolower(gVars.webEnv.getHttps()), "on")) ? "https://" : "http://");
		if (gConsts.isWP_SITEURLDefined() && !equal("", gConsts.getWP_SITEURL())) {
			guessurl = gConsts.getWP_SITEURL();
		}
		else
			guessurl = QRegExPerl.preg_replace("|/wp-admin/.*|i", "", schema + gVars.webEnv.getHttpHost() + gVars.webEnv.getRequestURI());
		getIncluded(FunctionsPage.class, gVars, gConsts).update_option("siteurl", guessurl);
		if (!_public) {
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("default_pingback_flag", 0);
		}
		user_id = getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(user_name);
		if (!booleanval(user_id)) {
			random_password = getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12);
			user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_create_user(user_name, random_password, user_email);
		}
		else {
			random_password = getIncluded(L10nPage.class, gVars, gConsts).__("User already exists.  Password inherited.", "default");
		}
		user = new WP_User(gVars, gConsts, user_id);
		user.set_role("administrator");
		wp_install_defaults(user_id);
		gVars.wp_rewrite.flush_rules();
		wp_new_blog_notification(blog_title, guessurl, user_id, random_password);
		getIncluded(CachePage.class, gVars, gConsts).wp_cache_flush();
		return new Array<Object>(new ArrayEntry<Object>("url", guessurl), new ArrayEntry<Object>("user_id", user_id), new ArrayEntry<Object>("password", random_password));
	}

	public void wp_install_defaults(int user_id) {
		String cat_name = null;
		String cat_slug = null;
		String now = null;
		String now_gmt = null;
		Object first_post_guid = null;
		cat_name = gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__("Uncategorized", "default"));
		cat_slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(getIncluded(L10nPage.class, gVars, gConsts)._c(
		        "Uncategorized|Default category slug", "default"), "");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.terms + " (name, slug, term_group) VALUES (\'" + cat_name + "\', \'" + cat_slug + "\', \'0\')");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'1\', \'category\', \'\', \'0\', \'1\')");
		cat_name = gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__("Blogroll", "default"));
		cat_slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(getIncluded(L10nPage.class, gVars, gConsts)._c(
		        "Blogroll|Default link category slug", "default"), "");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.terms + " (name, slug, term_group) VALUES (\'" + cat_name + "\', \'" + cat_slug + "\', \'0\')");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'2\', \'link_category\', \'\', \'0\', \'7\')");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.links + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://codex.wordpress.org/\', \'WordPress Docs\', 0, \'\', \'\');");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (1, 2)");
//		gVars.wpdb
//		        .query("INSERT INTO "
//		                + gVars.wpdb.links
//		                + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://wordpress.org/development/\', \'Development Blog\', 0, \'http://wordpress.org/development/feed/\', \'\');");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (2, 2)");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.links
//		        + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://wordpress.org/extend/ideas/\', \'Suggest Ideas\', 0, \'\', \'\');");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (3, 2)");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.links
		        + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://wordpress.org/support/\', \'Support Forum\', 0, \'\', \'\');");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (2, 2)");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.links
//		        + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://wordpress.org/extend/plugins/\', \'Plugins\', 0, \'\', \'\');");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (5, 2)");
//		gVars.wpdb
//		        .query("INSERT INTO " + gVars.wpdb.links + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://wordpress.org/extend/themes/\', \'Themes\', 0, \'\', \'\');");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (6, 2)");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.links
//		        + " (link_url, link_name, link_category, link_rss, link_notes) VALUES (\'http://planet.wordpress.org/\', \'WordPress Planet\', 0, \'\', \'\');");
//		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (7, 2)");
		now = DateTime.date("Y-m-d H:i:s");
		now_gmt = DateTime.gmdate("Y-m-d H:i:s");
		first_post_guid = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?p=1";
		gVars.wpdb
		        .query("INSERT INTO "
		                + gVars.wpdb.posts
		                + " (post_author, post_date, post_date_gmt, post_content, post_excerpt, post_title, post_category, post_name, post_modified, post_modified_gmt, guid, comment_count, to_ping, pinged, post_content_filtered) VALUES ("
		                + user_id
		                + ", \'"
		                + now
		                + "\', \'"
		                + now_gmt
		                + "\', \'"
		                + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__(
		                        "Welcome to nWordPress. This is your first post. Edit or delete it, then start blogging!", "default")) + "\', \'\', \'"
		                + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__("Hello world!", "default")) + "\', \'0\', \'"
		                + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts)._c("hello-world|Default post slug", "default")) + "\', \'" + now + "\', \'" + now_gmt
		                + "\', \'" + first_post_guid + "\', \'1\', \'\', \'\', \'\')");
		gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (`object_id`, `term_taxonomy_id`) VALUES (1, 1)");
		gVars.wpdb.query("INSERT INTO "
		        + gVars.wpdb.comments
		        + " (comment_post_ID, comment_author, comment_author_email, comment_author_url, comment_date, comment_date_gmt, comment_content) VALUES (\'1\', \'"
		        + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__("Mr nWordPress", "default"))
		        + "\', \'\', \'http://wordpress.org/\', \'"
		        + now
		        + "\', \'"
		        + now_gmt
		        + "\', \'"
		        + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__(
		                "Hi, this is a comment.<br />To delete a comment, just log in and view the post&#039;s comments. There you will have the option to edit or delete them.", "default")) + "\')");
		first_post_guid = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?page_id=2";
		gVars.wpdb
		        .query("INSERT INTO "
		                + gVars.wpdb.posts
		                + " (post_author, post_date, post_date_gmt, post_content, post_excerpt, post_title, post_category, post_name, post_modified, post_modified_gmt, guid, post_status, post_type, to_ping, pinged, post_content_filtered) VALUES ("
		                + user_id
		                + ", \'"
		                + now
		                + "\', \'"
		                + now_gmt
		                + "\', \'"
		                + gVars.wpdb
		                        .escape(getIncluded(L10nPage.class, gVars, gConsts)
		                                .__(
		                                        "This is an example of a nWordPress page, you could edit this to put information about yourself or your site so readers know where you are coming from. You can create as many pages like this one or sub-pages as you like and manage all of your content inside of nWordPress.",
		                                        "default")) + "\', \'\', \'" + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts).__("About", "default"))
		                + "\', \'0\', \'" + gVars.wpdb.escape(getIncluded(L10nPage.class, gVars, gConsts)._c("about|Default page slug", "default")) + "\', \'" + now + "\', \'"
		                + now_gmt + "\',\'" + first_post_guid + "\', \'publish\', \'page\', \'\', \'\', \'\')");
	}

	public void wp_new_blog_notification(String blog_title, Object blog_url, int user_id, Object password) {
		WP_User user = null;
		String email = null;
		Object name = null;
		String message_headers = null;
		String message = null;
		user = new WP_User(gVars, gConsts, user_id);
		email = user.getUser_email();
		name = user.getUser_login();
		message_headers = "From: \"" + blog_title + "\" <wordpress@" + gVars.webEnv.getServerName() + ">";
		message = QStrings
		        .sprintf(
		                getIncluded(L10nPage.class, gVars, gConsts)
		                        .__(
		                                "Your new nWordPress blog has been successfully set up at:\n\n%1$s\n\nYou can log in to the administrator account with the following information:\n\nUsername: %2$s\nPassword: %3$s\n\nWe hope you enjoy your new blog. Thanks!\n\n--The nWordPress Team\nhttp://nwordpress.sourceforge.net/\n",
		                                "default"), blog_url, name, password);
		getIncluded(PluggablePage.class, gVars, gConsts).wp_mail(email, getIncluded(L10nPage.class, gVars, gConsts)
		        .__("New nWordPress Blog", "default"), message, message_headers);
	}

	public void wp_upgrade() {
		wp_current_db_version = __get_option("db_version");
		if (equal(gVars.wp_db_version, wp_current_db_version)) {
			return;
		}
		wp_check_mysql_version();
		getIncluded(CachePage.class, gVars, gConsts).wp_cache_flush();
		make_db_current_silent();
		upgrade_all();
		getIncluded(CachePage.class, gVars, gConsts).wp_cache_flush();
	}

	/** 
	 * Functions to be called in install and upgrade scripts Functions to be
	 * called in install and upgrade scripts
	 */
	public void upgrade_all() {
		Object template = null;
		wp_current_db_version = __get_option("db_version");
		if (equal(gVars.wp_db_version, wp_current_db_version)) {
			return;
		}
		if (empty(wp_current_db_version))
		/*
		 * If the template option exists, we have 1.5. If the template option
		 * exists, we have 1.5.
		 */
		{
			wp_current_db_version = 0;
			template = __get_option("template");
			if (!empty(template)) {
				wp_current_db_version = 2541;
			}
		}
		if (intval(wp_current_db_version) < 6039) {
			upgrade_230_options_table();
		}
		getIncluded(SchemaPage.class, gVars, gConsts).populate_options();
		if (intval(wp_current_db_version) < 2541) {
			upgrade_100();
			upgrade_101();
			upgrade_110();
			upgrade_130();
		}
		if (intval(wp_current_db_version) < 3308) {
			upgrade_160();
		}
		if (intval(wp_current_db_version) < 4772) {
			upgrade_210();
		}
		if (intval(wp_current_db_version) < 4351) {
			upgrade_old_slugs();
		}
		if (intval(wp_current_db_version) < 5539) {
			upgrade_230();
		}
		if (intval(wp_current_db_version) < 6124) {
			upgrade_230_old_tables();
		}
		if (intval(wp_current_db_version) < 7499) {
			upgrade_250();
		}
		if (intval(wp_current_db_version) < 7796) {
			upgrade_251();
		}
		maybe_disable_automattic_widgets();
		gVars.wp_rewrite.flush_rules();
		getIncluded(FunctionsPage.class, gVars, gConsts).update_option("db_version", gVars.wp_db_version);
	}

	public void upgrade_100() {
		Array<Object> posts = new Array<Object>();
		StdClass post = null;
		String newtitle = null;
		Array<Object> categories = new Array<Object>();
		StdClass category = null;
		Array<Object> done_ids = new Array<Object>();
		Array<String> done_posts = new Array<String>();
		StdClass done_id = null;
		String catwhere = null;
		Array<Object> allposts = new Array<Object>();
		Array<Object> cat = new Array<Object>();
		posts = gVars.wpdb.get_results("SELECT ID, post_title, post_name FROM " + gVars.wpdb.posts + " WHERE post_name = \'\'");
		if (booleanval(posts)) {
			for (Map.Entry javaEntry215 : posts.entrySet()) {
				post = (StdClass) javaEntry215.getValue();
				if (equal("", StdClass.getValue(post, "post_name"))) {
					newtitle = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(StdClass.getValue(post, "post_title")), "");
					gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_name = \'" + newtitle + "\' WHERE ID = \'" + StdClass.getValue(post, "ID") + "\'");
				}
			}
		}
		categories = gVars.wpdb.get_results("SELECT cat_ID, cat_name, category_nicename FROM " + gVars.wpdb.categories);
		for (Map.Entry javaEntry216 : categories.entrySet()) {
			category = (StdClass) javaEntry216.getValue();
			if (equal("", StdClass.getValue(category, "category_nicename"))) {
				newtitle = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(StdClass.getValue(category, "cat_name")), "");
				gVars.wpdb.query("UPDATE " + gVars.wpdb.categories + " SET category_nicename = \'" + newtitle + "\' WHERE cat_ID = \'" + StdClass.getValue(category, "cat_ID") + "\'");
			}
		}
		gVars.wpdb
		        .query("UPDATE "
		                + gVars.wpdb.options
		                + " SET option_value = REPLACE(option_value, \'wp-links/links-images/\', \'wp-images/links/\')\n\tWHERE option_name LIKE \'links_rating_image%\'\n\tAND option_value LIKE \'wp-links/links-images/%\'");
		done_ids = gVars.wpdb.get_results("SELECT DISTINCT post_id FROM " + gVars.wpdb.post2cat);
		if (booleanval(done_ids)) {
			for (Map.Entry javaEntry217 : done_ids.entrySet()) {
				done_id = (StdClass) javaEntry217.getValue();
				done_posts.putValue(StdClass.getValue(done_id, "post_id"));
			}
			catwhere = " AND ID NOT IN (" + Strings.implode(",", done_posts) + ")";
		}
		else {
			catwhere = "";
		}
		allposts = gVars.wpdb.get_results("SELECT ID, post_category FROM " + gVars.wpdb.posts + " WHERE post_category != \'0\' " + catwhere);
		if (booleanval(allposts)) {
			for (Map.Entry javaEntry218 : allposts.entrySet())
			/*
			 * Check to see if it's already been imported Check to see if it's
			 * already been imported
			 */
			{
				post = (StdClass) javaEntry218.getValue();
				cat = (Array<Object>) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.post2cat + " WHERE post_id = " + StdClass.getValue(post, "ID") + " AND category_id = "
				        + StdClass.getValue(post, "post_category"));
				if (!booleanval(cat) && !equal(0, StdClass.getValue(post, "post_category")))
				/*
				 * If there's no result If there's no result
				 */
				{
					gVars.wpdb.query("\n\t\t\t\t\tINSERT INTO " + gVars.wpdb.post2cat + "\n\t\t\t\t\t(post_id, category_id)\n\t\t\t\t\tVALUES\n\t\t\t\t\t(\'" + StdClass.getValue(post, "ID") + "\', \'"
					        + StdClass.getValue(post, "post_category") + "\')\n\t\t\t\t\t");
				}
			}
		}
		else {
		}
	}

	public void upgrade_101() {
		add_clean_index(gVars.wpdb.posts, "post_name");
		add_clean_index(gVars.wpdb.posts, "post_status");
		add_clean_index(gVars.wpdb.categories, "category_nicename");
		add_clean_index(gVars.wpdb.comments, "comment_approved");
		add_clean_index(gVars.wpdb.comments, "comment_post_ID");
		add_clean_index(gVars.wpdb.links, "link_category");
		add_clean_index(gVars.wpdb.links, "link_visible");
	}

	public void upgrade_110() {
		Array<Object> users = new Array<Object>();
		StdClass user = null;
		String newname = null;
		StdClass row = null;
		StdClass all_options = null;
		int time_difference;
		int server_time = 0;
		Object weblogger_time = null;
		int gmt_time = 0;
		float diff_gmt_server = 0;
		Object diff_weblogger_server = null;
		Object diff_gmt_weblogger = null;
		Object gmt_offset = null;
		boolean got_gmt_fields = false;
		int add_hours = 0;
		int add_minutes = 0;
		users = gVars.wpdb.get_results("SELECT ID, user_nickname, user_nicename FROM " + gVars.wpdb.users);
		for (Map.Entry javaEntry219 : users.entrySet()) {
			user = (StdClass) javaEntry219.getValue();
			if (equal("", StdClass.getValue(user, "user_nicename"))) {
				newname = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(StdClass.getValue(user, "user_nickname")), "");
				gVars.wpdb.query("UPDATE " + gVars.wpdb.users + " SET user_nicename = \'" + newname + "\' WHERE ID = \'" + StdClass.getValue(user, "ID") + "\'");
			}
		}
		users = gVars.wpdb.get_results("SELECT ID, user_pass from " + gVars.wpdb.users);
		for (Map.Entry javaEntry220 : users.entrySet()) {
			row = (StdClass) javaEntry220.getValue();
			if (!QRegExPerl.preg_match("/^[A-Fa-f0-9]{32}$/", strval(StdClass.getValue(row, "user_pass")))) {
				gVars.wpdb.query("UPDATE " + gVars.wpdb.users + " SET user_pass = MD5(\'" + StdClass.getValue(row, "user_pass") + "\') WHERE ID = \'" + StdClass.getValue(row, "ID") + "\'");
			}
		}
		all_options = get_alloptions_110();
		time_difference = intval(StdClass.getValue(all_options, "time_difference"));
		server_time = DateTime.time() + intval(DateTime.date("Z"));
		weblogger_time = server_time + time_difference * 3600;
		gmt_time = DateTime.time();
		diff_gmt_server = floatval(gmt_time - server_time) / floatval(3600);
		diff_weblogger_server = floatval(intval(weblogger_time) - server_time) / floatval(3600);
		diff_gmt_weblogger = diff_gmt_server - floatval(diff_weblogger_server);
		gmt_offset = -intval(diff_gmt_weblogger);
		getIncluded(FunctionsPage.class, gVars, gConsts).add_option("gmt_offset", gmt_offset, "", "yes");
		got_gmt_fields = (equal(gVars.wpdb.get_var("SELECT MAX(post_date_gmt) FROM " + gVars.wpdb.posts), "0000-00-00 00:00:00") ? false : true);
		if (!got_gmt_fields)
		/*
		 * Add or substract time to all dates, to get GMT dates Add or substract
		 * time to all dates, to get GMT dates
		 */
		{
			add_hours = intval(diff_gmt_weblogger);
			add_minutes = 60 * (intval(diff_gmt_weblogger) - add_hours);
			gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_date_gmt = DATE_ADD(post_date, INTERVAL \'" + add_hours + ":" + add_minutes + "\' HOUR_MINUTE)");
			gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_modified = post_date");
			gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_modified_gmt = DATE_ADD(post_modified, INTERVAL \'" + add_hours + ":" + add_minutes
			        + "\' HOUR_MINUTE) WHERE post_modified != \'0000-00-00 00:00:00\'");
			gVars.wpdb.query("UPDATE " + gVars.wpdb.comments + " SET comment_date_gmt = DATE_ADD(comment_date, INTERVAL \'" + add_hours + ":" + add_minutes + "\' HOUR_MINUTE)");
			gVars.wpdb.query("UPDATE " + gVars.wpdb.users + " SET user_registered = DATE_ADD(user_registered, INTERVAL \'" + add_hours + ":" + add_minutes + "\' HOUR_MINUTE)");
		}
	}

	public void upgrade_130() {
		Array<Object> posts = new Array<Object>();
		String post_content = null;
		StdClass post = null;
		String post_title = null;
		String post_excerpt = null;
		String guid = "";
		Array<Object> comments = new Array<Object>();
		String comment_content = null;
		StdClass comment = null;
		String comment_author = null;
		Array<Object> links = new Array<Object>();
		String link_name = null;
		StdClass link = null;
		String link_description = null;
		Object active_plugins = null;

		/* Do not change type */
		Array<Object> options = new Array<Object>();
		StdClass option = null;
		int limit = 0;
		Array dupe_ids = new Array();
		posts = gVars.wpdb.get_results("SELECT ID, post_title, post_content, post_excerpt, guid, post_date, post_name, post_status, post_author FROM " + gVars.wpdb.posts);
		if (booleanval(posts)) {
			for (Map.Entry javaEntry221 : posts.entrySet()) {
				post = (StdClass) javaEntry221.getValue();
				post_content = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(post, "post_content"))));
				post_title = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(post, "post_title"))));
				post_excerpt = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(post, "post_excerpt"))));
				if (empty(StdClass.getValue(post, "guid"))) {
					guid = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(post, "ID"), false);
				}
				else
					guid = strval(StdClass.getValue(post, "guid"));
				gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_title = \'" + post_title + "\', post_content = \'" + post_content + "\', post_excerpt = \'" + post_excerpt + "\', guid = \'"
				        + guid + "\' WHERE ID = \'" + StdClass.getValue(post, "ID") + "\'");
			}
		}
		comments = gVars.wpdb.get_results("SELECT comment_ID, comment_author, comment_content FROM " + gVars.wpdb.comments);
		if (booleanval(comments)) {
			for (Map.Entry javaEntry222 : comments.entrySet()) {
				comment = (StdClass) javaEntry222.getValue();
				comment_content = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(comment, "comment_content"))));
				comment_author = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(comment, "comment_author"))));
				gVars.wpdb.query("UPDATE " + gVars.wpdb.comments + " SET comment_content = \'" + comment_content + "\', comment_author = \'" + comment_author + "\' WHERE comment_ID = \'"
				        + StdClass.getValue(comment, "comment_ID") + "\'");
			}
		}
		links = gVars.wpdb.get_results("SELECT link_id, link_name, link_description FROM " + gVars.wpdb.links);
		if (booleanval(links)) {
			for (Map.Entry javaEntry223 : links.entrySet()) {
				link = (StdClass) javaEntry223.getValue();
				link_name = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(link, "link_name"))));
				link_description = Strings.addslashes(gVars.webEnv, deslash(strval(StdClass.getValue(link, "link_description"))));
				gVars.wpdb.query("UPDATE " + gVars.wpdb.links + " SET link_name = \'" + link_name + "\', link_description = \'" + link_description + "\' WHERE link_id = \'"
				        + StdClass.getValue(link, "link_id") + "\'");
			}
		}
		if (equal(gVars.wpdb.get_var("SELECT option_value FROM " + gVars.wpdb.options + " WHERE option_name = \'what_to_show\'"), "paged")) {
			gVars.wpdb.query("UPDATE " + gVars.wpdb.options + " SET option_value = \'posts\' WHERE option_name = \'what_to_show\'");
		}
		active_plugins = __get_option("active_plugins");
		if (!is_array(active_plugins)) {
			active_plugins = Strings.explode("\n", Strings.trim(strval(active_plugins)));
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("active_plugins", active_plugins);
		}
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "optionvalues");
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "optiontypes");
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "optiongroups");
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "optiongroup_options");
		gVars.wpdb.query("UPDATE " + gVars.wpdb.comments
		        + " SET comment_type=\'trackback\', comment_content = REPLACE(comment_content, \'<trackback />\', \'\') WHERE comment_content LIKE \'<trackback />%\'");
		gVars.wpdb.query("UPDATE " + gVars.wpdb.comments
		        + " SET comment_type=\'pingback\', comment_content = REPLACE(comment_content, \'<pingback />\', \'\') WHERE comment_content LIKE \'<pingback />%\'");
		options = gVars.wpdb.get_results("SELECT option_name, COUNT(option_name) AS dupes FROM `" + gVars.wpdb.options + "` GROUP BY option_name");
		for (Map.Entry javaEntry224 : options.entrySet()) {
			option = (StdClass) javaEntry224.getValue();
			if (!equal(1, StdClass.getValue(option, "dupes")))
			/* ? */
			{
				limit = intval(StdClass.getValue(option, "dupes")) - 1;
				dupe_ids = gVars.wpdb.get_col("SELECT option_id FROM " + gVars.wpdb.options + " WHERE option_name = \'" + StdClass.getValue(option, "option_name") + "\' LIMIT " + limit);
				String dupe_idsStr = Strings.join(dupe_ids, ",");
				gVars.wpdb.query("DELETE FROM " + gVars.wpdb.options + " WHERE option_id IN (" + dupe_idsStr + ")");
			}
		}
		make_site_theme();
	}

	public void upgrade_160() {
		Array<Object> users = new Array<Object>();
		StdClass user = null;
		Object idmode = null;
		String id = null;
		String caps = null;
		String level = null;
		String role = null;
		Array<Object> old_user_fields = new Array<Object>();
		Object old = null;
		Object comments;

		/* Do not change type */
		StdClass comment = null;
		Array<Object> objects = new Array<Object>();
		StdClass object = null;
		Array<Object> meta = new Array<Object>();
		getIncluded(SchemaPage.class, gVars, gConsts).populate_roles_160();
		users = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.users);
		for (Map.Entry javaEntry225 : users.entrySet())
		/*
		 * FIX ME: RESET_CAPS is temporary code to reset roles and caps if flag is set.
		 */
		{
			user = (StdClass) javaEntry225.getValue();
			if (!empty(StdClass.getValue(user, "user_firstname"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "first_name", gVars.wpdb.escape(strval(StdClass.getValue(user, "user_firstname"))));
			}
			if (!empty(StdClass.getValue(user, "user_lastname"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "last_name", gVars.wpdb.escape(strval(StdClass.getValue(user, "user_lastname"))));
			}
			if (!empty(StdClass.getValue(user, "user_nickname"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "nickname", gVars.wpdb.escape(strval(StdClass.getValue(user, "user_nickname"))));
			}
			if (!empty(StdClass.getValue(user, "user_level"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), gVars.wpdb.prefix + "user_level", StdClass.getValue(user, "user_level"));
			}
			if (!empty(StdClass.getValue(user, "user_icq"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "icq", gVars.wpdb
				        .escape(strval(StdClass.getValue(user, "user_icq"))));
			}
			if (!empty(StdClass.getValue(user, "user_aim"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "aim", gVars.wpdb
				        .escape(strval(StdClass.getValue(user, "user_aim"))));
			}
			if (!empty(StdClass.getValue(user, "user_msn"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "msn", gVars.wpdb
				        .escape(strval(StdClass.getValue(user, "user_msn"))));
			}
			if (!empty(StdClass.getValue(user, "user_yim"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "yim", gVars.wpdb
				        .escape(strval(StdClass.getValue(user, "user_icq"))));
			}
			if (!empty(StdClass.getValue(user, "user_description"))) {
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), "description", gVars.wpdb.escape(strval(StdClass.getValue(user, "user_description"))));
			}
			if (isset(StdClass.getValue(user, "user_idmode"))) {
				idmode = StdClass.getValue(user, "user_idmode");
				if (equal(idmode, "nickname")) {
					id = strval(StdClass.getValue(user, "user_nickname"));
				}
				if (equal(idmode, "login")) {
					id = strval(StdClass.getValue(user, "user_login"));
				}
				if (equal(idmode, "firstname")) {
					id = strval(StdClass.getValue(user, "user_firstname"));
				}
				if (equal(idmode, "lastname")) {
					id = strval(StdClass.getValue(user, "user_lastname"));
				}
				if (equal(idmode, "namefl")) {
					id = StdClass.getValue(user, "user_firstname") + " " + StdClass.getValue(user, "user_lastname");
				}
				if (equal(idmode, "namelf")) {
					id = StdClass.getValue(user, "user_lastname") + " " + StdClass.getValue(user, "user_firstname");
				}
				if (!booleanval(idmode)) {
					id = strval(StdClass.getValue(user, "user_nickname"));
				}
				id = gVars.wpdb.escape(id);
				gVars.wpdb.query("UPDATE " + gVars.wpdb.users + " SET display_name = \'" + id + "\' WHERE ID = \'" + StdClass.getValue(user, "ID") + "\'");
			}
			else {
			}
			caps = strval(getIncluded(UserPage.class, gVars, gConsts).get_usermeta(intval(StdClass.getValue(user, "ID")), gVars.wpdb.prefix + "capabilities"));
			if (empty(caps) || gConsts.isRESET_CAPSDefined()) {
				level = strval(getIncluded(UserPage.class, gVars, gConsts).get_usermeta(intval(StdClass.getValue(user, "ID")), gVars.wpdb.prefix + "user_level"));
				role = translate_level_to_role(level);
				getIncluded(UserPage.class, gVars, gConsts).update_usermeta(intval(StdClass.getValue(user, "ID")), gVars.wpdb.prefix + "capabilities", new Array<Object>(
				        new ArrayEntry<Object>(role, true)));
			}
		}
		old_user_fields = new Array<Object>(new ArrayEntry<Object>("user_firstname"), new ArrayEntry<Object>("user_lastname"), new ArrayEntry<Object>("user_icq"), new ArrayEntry<Object>("user_aim"),
		        new ArrayEntry<Object>("user_msn"), new ArrayEntry<Object>("user_yim"), new ArrayEntry<Object>("user_idmode"), new ArrayEntry<Object>("user_ip"),
		        new ArrayEntry<Object>("user_domain"), new ArrayEntry<Object>("user_browser"), new ArrayEntry<Object>("user_description"), new ArrayEntry<Object>("user_nickname"),
		        new ArrayEntry<Object>("user_level"));
		gVars.wpdb.hide_errors();
		for (Map.Entry javaEntry226 : old_user_fields.entrySet()) {
			old = javaEntry226.getValue();
			gVars.wpdb.query("ALTER TABLE " + gVars.wpdb.users + " DROP " + old);
		}
		gVars.wpdb.show_errors();
		comments = gVars.wpdb.get_results("SELECT comment_post_ID, COUNT(*) as c FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'1\' GROUP BY comment_post_ID");
		if (is_array(comments)) {
			for (Map.Entry javaEntry227 : ((Array<?>) comments).entrySet()) {
				comment = (StdClass) javaEntry227.getValue();
				gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET comment_count = " + StdClass.getValue(comment, "c") + " WHERE ID = \'" + StdClass.getValue(comment, "comment_post_ID") + "\'");
			}
		}
		if (intval(wp_current_db_version) > 2541 && intval(wp_current_db_version) <= 3091) {
			objects = gVars.wpdb.get_results("SELECT ID, post_type FROM " + gVars.wpdb.posts + " WHERE post_status = \'object\'");
			for (Map.Entry javaEntry228 : objects.entrySet()) {
				object = (StdClass) javaEntry228.getValue();
				gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_status = \'attachment\',\n\t\t\tpost_mime_type = \'" + StdClass.getValue(object, "post_type")
				        + "\',\n\t\t\tpost_type = \'\'\n\t\t\tWHERE ID = " + StdClass.getValue(object, "ID"));
				meta = (Array<Object>) getIncluded(PostPage.class, gVars, gConsts).get_post_meta(intval(StdClass.getValue(object, "ID")), "imagedata", true);
				if (!empty(meta.getValue("file"))) {
					getIncluded(PostPage.class, gVars, gConsts).update_attached_file(intval(StdClass.getValue(object, "ID")), strval(meta.getValue("file")));
				}
			}
		}
	}

	public void upgrade_210() {
		Array<Object> posts = new Array<Object>();
		String status = null;
		StdClass post = null;
		String type = null;
		String now = null;
		if (intval(wp_current_db_version) < 3506)
		/*
		 * Update status and type. Update status and type.
		 */
		{
			posts = gVars.wpdb.get_results("SELECT ID, post_status FROM " + gVars.wpdb.posts);
			if (!empty(posts)) {
				for (Map.Entry javaEntry229 : posts.entrySet()) {
					post = (StdClass) javaEntry229.getValue();
					status = strval(StdClass.getValue(post, "post_status"));
					type = "post";
					if (equal("static", status)) {
						status = "publish";
						type = "page";
					}
					else
						if (equal("attachment", status)) {
							status = "inherit";
							type = "attachment";
						}
					gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_status = \'" + status + "\', post_type = \'" + type + "\' WHERE ID = \'" + StdClass.getValue(post, "ID") + "\'");
				}
			}
		}
		if (intval(wp_current_db_version) < 3845) {
			getIncluded(SchemaPage.class, gVars, gConsts).populate_roles_210();
		}
		if (intval(wp_current_db_version) < 3531)
		/*
		 * Give future posts a post_status of future. Give future posts a
		 * post_status of future.
		 */
		{
			now = DateTime.gmdate("Y-m-d H:i:59");
			gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_status = \'future\' WHERE post_status = \'publish\' AND post_date_gmt > \'" + now + "\'");
			posts = gVars.wpdb.get_results("SELECT ID, post_date FROM " + gVars.wpdb.posts + " WHERE post_status =\'future\'");
			if (!empty(posts)) {
				for (Map.Entry javaEntry230 : posts.entrySet()) {
					post = (StdClass) javaEntry230.getValue();
					getIncluded(CronPage.class, gVars, gConsts).wp_schedule_single_event(getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
					        "U", strval(StdClass.getValue(post, "post_date")), true), "publish_future_post", new Array<Object>(new ArrayEntry<Object>(StdClass.getValue(post, "ID"))));
				}
			}
		}
	}

	public void upgrade_230() {
		Array<Object> tt_ids = new Array<Object>();
		boolean have_tags = false;
		Array<Object> categories = new Array<Object>();
		Integer term_id = null;
		StdClass category = null;
		String name = null;
		String description = null;
		Object slug = null;
		String parent = null;
		Integer term_group = null;
		Array<StdClass> exists;
		int id;
		int num = 0;
		Object alt_slug = null;
		String slug_check = null;
		Integer count = null;
		String taxonomy = null;
		String select = null;
		Array<Object> posts = new Array<Object>();
		int post_id = 0;
		StdClass post = null;
		Object tt_id = null;
		Array<Object> link_cat_id_map = new Array<Object>();
		int default_link_cat = 0;
		Array<Object> link_cats = new Array<Object>();
		int cat_id = 0;
		Array<Object> links = new Array<Object>();
		StdClass link = null;
		int link_id = 0;
		Array<Object> terms = new Array<Object>();
		StdClass term = null;
		if (intval(wp_current_db_version) < 5200) {
			getIncluded(SchemaPage.class, gVars, gConsts).populate_roles_230();
		}
		tt_ids = new Array<Object>();
		have_tags = false;
		categories = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.categories + " ORDER BY cat_ID");
		for (Map.Entry javaEntry231 : categories.entrySet())
		/*
		 * Associate terms with the same slug in a term group and make slugs
		 * unique. Associate terms with the same slug in a term group and make
		 * slugs unique.
		 */
		{
			category = (StdClass) javaEntry231.getValue();
			term_id = intval(StdClass.getValue(category, "cat_ID"));
			name = gVars.wpdb.escape(strval(StdClass.getValue(category, "cat_name")));
			description = gVars.wpdb.escape(strval(StdClass.getValue(category, "category_description")));
			slug = gVars.wpdb.escape(strval(StdClass.getValue(category, "category_nicename")));
			parent = gVars.wpdb.escape(strval(StdClass.getValue(category, "category_parent")));
			term_group = 0;
			if (booleanval(exists = gVars.wpdb.get_results("SELECT term_id, term_group FROM " + gVars.wpdb.terms + " WHERE slug = \'" + slug + "\'"))) {
				term_group = intval(exists.getValue(0).fields.getValue("term_group"));
				id = intval(exists.getValue(0).fields.getValue("term_id"));
				num = 2;
				do {
					alt_slug = strval(slug) + "-" + strval(num);
					num++;
					slug_check = strval(gVars.wpdb.get_var("SELECT slug FROM " + gVars.wpdb.terms + " WHERE slug = \'" + alt_slug + "\'"));
				}
				while (booleanval(slug_check));
				slug = alt_slug;
				if (empty(term_group)) {
					term_group = intval(gVars.wpdb.get_var("SELECT MAX(term_group) FROM " + gVars.wpdb.terms + " GROUP BY term_group")) + 1;
					gVars.wpdb.query("UPDATE " + gVars.wpdb.terms + " SET term_group = \'" + term_group + "\' WHERE term_id = \'" + id + "\'");
				}
			}
			gVars.wpdb.query("INSERT INTO " + gVars.wpdb.terms + " (term_id, name, slug, term_group) VALUES (\'" + term_id + "\', \'" + name + "\', \'" + slug + "\', \'" + term_group + "\')");
			count = 0;
			if (!empty(StdClass.getValue(category, "category_count"))) {
				count = intval(StdClass.getValue(category, "category_count"));
				taxonomy = "category";
				gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'" + term_id + "\', \'" + taxonomy + "\', \'" + description
				        + "\', \'" + parent + "\', \'" + count + "\')");
				tt_ids.getArrayValue(term_id).putValue(taxonomy, gVars.wpdb.insert_id);
			}
			if (!empty(StdClass.getValue(category, "link_count"))) {
				count = intval(StdClass.getValue(category, "link_count"));
				taxonomy = "link_category";
				gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'" + term_id + "\', \'" + taxonomy + "\', \'" + description
				        + "\', \'" + parent + "\', \'" + count + "\')");
				tt_ids.getArrayValue(term_id).putValue(taxonomy, gVars.wpdb.insert_id);
			}
			if (!empty(StdClass.getValue(category, "tag_count"))) {
				have_tags = true;
				count = intval(StdClass.getValue(category, "tag_count"));
				taxonomy = "post_tag";
				gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'" + term_id + "\', \'" + taxonomy + "\', \'" + description
				        + "\', \'" + parent + "\', \'" + count + "\')");
				tt_ids.getArrayValue(term_id).putValue(taxonomy, gVars.wpdb.insert_id);
			}
			if (empty(count)) {
				count = 0;
				taxonomy = "category";
				gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'" + term_id + "\', \'" + taxonomy + "\', \'" + description
				        + "\', \'" + parent + "\', \'" + count + "\')");
				tt_ids.getArrayValue(term_id).putValue(taxonomy, gVars.wpdb.insert_id);
			}
		}
		select = "post_id, category_id";
		if (have_tags) {
			select = select + ", rel_type";
		}
		posts = gVars.wpdb.get_results("SELECT " + select + " FROM " + gVars.wpdb.post2cat + " GROUP BY post_id, category_id");
		for (Map.Entry javaEntry232 : posts.entrySet()) {
			post = (StdClass) javaEntry232.getValue();
			post_id = intval(StdClass.getValue(post, "post_id"));
			term_id = intval(StdClass.getValue(post, "category_id"));
			taxonomy = "category";
			if (!empty(StdClass.getValue(post, "rel_type")) && equal("tag", StdClass.getValue(post, "rel_type"))) {
				taxonomy = "tag";
			}
			tt_id = tt_ids.getArrayValue(term_id).getValue(taxonomy);
			if (empty(tt_id)) {
				continue;
			}
			gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (object_id, term_taxonomy_id) VALUES (\'" + post_id + "\', \'" + tt_id + "\')");
		}
		if (intval(wp_current_db_version) < 3570)
		/*
		 * Create link_category terms for link categories. Create a map of link
		 * cat IDs Create link_category terms for link categories. Create a map
		 * of link cat IDs to link_category terms. to link_category terms.
		 */

		/*
		 * Associate links to cats. Associate links to cats.
		 */

		/*
		 * Set default to the last category we grabbed during the upgrade loop.
		 * Set default to the last category we grabbed during the upgrade loop.
		 */
		{
			link_cat_id_map = new Array<Object>();
			default_link_cat = 0;
			tt_ids = new Array<Object>();
			link_cats = gVars.wpdb.get_results("SELECT cat_id, cat_name FROM " + gVars.wpdb.prefix + "linkcategories");
			for (Map.Entry javaEntry233 : link_cats.entrySet())
			/*
			 * Associate terms with the same slug in a term group and make slugs
			 * unique. Associate terms with the same slug in a term group and
			 * make slugs unique.
			 */
			{
				category = (StdClass) javaEntry233.getValue();
				cat_id = intval(StdClass.getValue(category, "cat_id"));
				term_id = 0;
				name = gVars.wpdb.escape(strval(StdClass.getValue(category, "cat_name")));
				slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(name, "");
				term_group = 0;
				if (booleanval(exists = gVars.wpdb.get_results("SELECT term_id, term_group FROM " + gVars.wpdb.terms + " WHERE slug = \'" + slug + "\'"))) {
					term_group = intval(exists.getValue(0).fields.getValue("term_group"));
					term_id = intval(exists.getValue(0).fields.getValue("term_id"));
				}
				if (empty(term_id)) {
					gVars.wpdb.query("INSERT INTO " + gVars.wpdb.terms + " (name, slug, term_group) VALUES (\'" + name + "\', \'" + slug + "\', \'" + term_group + "\')");
					term_id = gVars.wpdb.insert_id;
				}
				link_cat_id_map.putValue(cat_id, term_id);
				default_link_cat = term_id;
				gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_taxonomy + " (term_id, taxonomy, description, parent, count) VALUES (\'" + term_id + "\', \'link_category\', \'\', \'0\', \'0\')");
				tt_ids.putValue(term_id, gVars.wpdb.insert_id);
			}
			links = gVars.wpdb.get_results("SELECT link_id, link_category FROM " + gVars.wpdb.links);
			if (!empty(links)) {
				for (Map.Entry javaEntry234 : links.entrySet()) {
					link = (StdClass) javaEntry234.getValue();
					if (equal(0, StdClass.getValue(link, "link_category"))) {
						continue;
					}
					if (!isset(link_cat_id_map.getValue(StdClass.getValue(link, "link_category")))) {
						continue;
					}
					term_id = intval(link_cat_id_map.getValue(StdClass.getValue(link, "link_category")));
					tt_id = tt_ids.getValue(term_id);
					if (empty(tt_id)) {
						continue;
					}
					gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (object_id, term_taxonomy_id) VALUES (\'" + StdClass.getValue(link, "link_id") + "\', \'" + tt_id + "\')");
				}
			}
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("default_link_category", default_link_cat);
		}
		else {
			links = gVars.wpdb.get_results("SELECT link_id, category_id FROM " + gVars.wpdb.link2cat + " GROUP BY link_id, category_id");
			for (Map.Entry javaEntry235 : links.entrySet()) {
				link = (StdClass) javaEntry235.getValue();
				link_id = intval(StdClass.getValue(link, "link_id"));
				term_id = intval(StdClass.getValue(link, "category_id"));
				taxonomy = "link_category";
				tt_id = tt_ids.getArrayValue(term_id).getValue(taxonomy);
				if (empty(tt_id)) {
					continue;
				}
				gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (object_id, term_taxonomy_id) VALUES (\'" + link_id + "\', \'" + tt_id + "\')");
			}
		}
		if (intval(wp_current_db_version) < 4772)
		/*
		 * Obsolete linkcategories table Obsolete linkcategories table
		 */
		{
			gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "linkcategories");
		}
		terms = gVars.wpdb.get_results("SELECT term_taxonomy_id, taxonomy FROM " + gVars.wpdb.term_taxonomy);
		for (Map.Entry javaEntry236 : new Array<Object>(terms).entrySet()) {
			term = (StdClass) javaEntry236.getValue();
			if (equal("post_tag", StdClass.getValue(term, "taxonomy")) || equal("category", StdClass.getValue(term, "taxonomy"))) {
				count = intval(gVars.wpdb.get_var("SELECT COUNT(*) FROM " + gVars.wpdb.term_relationships + ", " + gVars.wpdb.posts + " WHERE " + gVars.wpdb.posts + ".ID = "
				        + gVars.wpdb.term_relationships + ".object_id AND post_status = \'publish\' AND post_type = \'post\' AND term_taxonomy_id = \'" + StdClass.getValue(term, "term_taxonomy_id")
				        + "\'"));
			}
			else
				count = intval(gVars.wpdb.get_var("SELECT COUNT(*) FROM " + gVars.wpdb.term_relationships + " WHERE term_taxonomy_id = \'" + StdClass.getValue(term, "term_taxonomy_id") + "\'"));
			gVars.wpdb.query("UPDATE " + gVars.wpdb.term_taxonomy + " SET count = \'" + count + "\' WHERE term_taxonomy_id = \'" + StdClass.getValue(term, "term_taxonomy_id") + "\'");
		}
	}

	public void upgrade_230_options_table() {
		Array<Object> old_options_fields = new Array<Object>();
		Object old = null;
		old_options_fields = new Array<Object>(new ArrayEntry<Object>("option_can_override"), new ArrayEntry<Object>("option_type"), new ArrayEntry<Object>("option_width"), new ArrayEntry<Object>(
		        "option_height"), new ArrayEntry<Object>("option_description"), new ArrayEntry<Object>("option_admin_level"));
		gVars.wpdb.hide_errors();
		for (Map.Entry javaEntry237 : old_options_fields.entrySet()) {
			old = javaEntry237.getValue();
			gVars.wpdb.query("ALTER TABLE " + gVars.wpdb.options + " DROP " + old);
		}
		gVars.wpdb.show_errors();
	}

	public void upgrade_230_old_tables() {
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "categories");
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "link2cat");
		gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + "post2cat");
	}

	public void upgrade_old_slugs() {
		gVars.wpdb.query("UPDATE " + gVars.wpdb.postmeta + " SET meta_key = \'_wp_old_slug\' WHERE meta_key = \'old_slug\'");
	}

	public void upgrade_250() {
		if (intval(wp_current_db_version) < 6689) {
			getIncluded(SchemaPage.class, gVars, gConsts).populate_roles_250();
		}
	}

	public void upgrade_251() {
		getIncluded(FunctionsPage.class, gVars, gConsts).update_option("secret", getIncluded(PluggablePage.class, gVars, gConsts)
		        .wp_generate_password(64));
	}

	/** 
	 * The functions we use to actually do stuff The functions we use to
	 * actually do stuff General General
	 */
	public boolean maybe_create_table(String table_name, String create_ddl) {
		Object table = null;
		int q = 0;
		for (Map.Entry javaEntry238 : gVars.wpdb.get_col("SHOW TABLES", 0).entrySet()) {
			table = javaEntry238.getValue();
			if (equal(table, table_name)) {
				return true;
			}
		}
		q = gVars.wpdb.query(create_ddl);
		for (Map.Entry javaEntry239 : gVars.wpdb.get_col("SHOW TABLES", 0).entrySet()) {
			table = javaEntry239.getValue();
			if (equal(table, table_name)) {
				return true;
			}
		}
		return false;
	}

	public boolean drop_index(String table, String index) {
		int i = 0;
		gVars.wpdb.hide_errors();
		gVars.wpdb.query("ALTER TABLE `" + table + "` DROP INDEX `" + index + "`");
		for (i = 0; i < 25; i++) {
			gVars.wpdb.query("ALTER TABLE `" + table + "` DROP INDEX `" + index + "_" + i + "`");
		}
		gVars.wpdb.show_errors();
		return true;
	}

	public boolean add_clean_index(String table, String index) {
		drop_index(table, index);
		gVars.wpdb.query("ALTER TABLE `" + table + "` ADD INDEX ( `" + index + "` )");
		return true;
	}

	/** 
	 * maybe_add_column() * Add column to db table if it doesn't exist. *
	 * Returns: true if already exists or on successful completion * false on
	 * error
	 */
	public boolean maybe_add_column(String table_name, String column_name, String create_ddl) {
		Object column = null;
		int q = 0;
		for (Map.Entry javaEntry240 : gVars.wpdb.get_col("DESC " + table_name, 0).entrySet()) {
			column = javaEntry240.getValue();
			if (booleanval(gVars.debug)) {
				echo(gVars.webEnv, "checking " + strval(column) + " == " + column_name + "<br />");
			}
			if (equal(column, column_name)) {
				return true;
			}
		}
		q = gVars.wpdb.query(create_ddl);
		for (Map.Entry javaEntry241 : gVars.wpdb.get_col("DESC " + table_name, 0).entrySet()) {
			column = javaEntry241.getValue();
			if (equal(column, column_name)) {
				return true;
			}
		}
		return false;
	}

	/** 
	 * get_alloptions as it was for 1.2. get_alloptions as it was for 1.2.
	 */
	public StdClass get_alloptions_110() {
		Array<Object> options;
		StdClass option = null;
		StdClass all_options = new StdClass();
		if (booleanval(options = gVars.wpdb.get_results("SELECT option_name, option_value FROM " + gVars.wpdb.options))) {
			for (Map.Entry javaEntry242 : options.entrySet())
			/*
			 * "When trying to design a foolproof system, "When trying to design
			 * a foolproof system, never underestimate the ingenuity of the
			 * fools :)" -- Dougal never underestimate the ingenuity of the
			 * fools :)" -- Dougal
			 */
			{
				option = (StdClass) javaEntry242.getValue();
				if (equal("siteurl", StdClass.getValue(option, "option_name"))) {
					option.fields.putValue("option_value", QRegExPerl.preg_replace("|/+$|", "", strval(StdClass.getValue(option, "option_value"))));
				}
				if (equal("home", StdClass.getValue(option, "option_name"))) {
					option.fields.putValue("option_value", QRegExPerl.preg_replace("|/+$|", "", strval(StdClass.getValue(option, "option_value"))));
				}
				if (equal("category_base", StdClass.getValue(option, "option_name"))) {
					option.fields.putValue("option_value", QRegExPerl.preg_replace("|/+$|", "", strval(StdClass.getValue(option, "option_value"))));
				}

				// Modified by Numiton

				all_options.fields.putValue(StdClass.getValue(option, "option_name"), Strings.stripslashes(gVars.webEnv, strval(StdClass.getValue(option, "option_value"))));
			}
		}
		return all_options;
	}

	/** 
	 * Version of get_option that is private to install/upgrade. Version of
	 * get_option that is private to install/upgrade.
	 */
	public Object __get_option(String setting) {
		String option;
		Object kellogs = null;
		if (equal(setting, "home") && gConsts.isWP_HOMEDefined()) {
			return QRegExPerl.preg_replace("|/+$|", "", gConsts.getWP_HOME());
		}
		if (equal(setting, "siteurl") && gConsts.isWP_SITEURLDefined()) {
			return QRegExPerl.preg_replace("|/+$|", "", gConsts.getWP_SITEURL());
		}
		option = strval(gVars.wpdb.get_var("SELECT option_value FROM " + gVars.wpdb.options + " WHERE option_name = \'" + setting + "\'"));
		if (equal("home", setting) && equal("", option)) {
			return __get_option("siteurl");
		}
		if (equal("siteurl", setting) || equal("home", setting) || equal("category_base", setting)) {
			option = QRegExPerl.preg_replace("|/+$|", "", option);
		}
		
		kellogs = unserialize(option);
		
		if (!strictEqual(kellogs, null)) {
			return kellogs;
		}
		else
			return option;
	}

	public String deslash(String content) {
		content = QRegExPerl.preg_replace("/\\\\+\'/", "\'", content);
		content = QRegExPerl.preg_replace("/\\\\+\"/", "\"", content);
		content = QRegExPerl.preg_replace("/\\\\+/", "\\", content);
		return content;
	}

	public Array<Object> dbDelta(Object queries, /* Do not change type */boolean execute) {
		Array<Object> cqueries = new Array<Object>();
		Array<Object> iqueries = new Array<Object>();
		Array<Object> for_update = new Array<Object>();
		String qry = null;
		Array<Object> matches = new Array<Object>();
		Array<Object> tables = new Array<Object>();
		String table = null;
		Array<Object> cfields = new Array<Object>();
		Array<Object> indices = new Array<Object>();
		Array<Object> match2 = new Array<Object>();
		String qryline = null;
		Array<String> flds = new Array<String>();
		String fld = null;
		Array<Object> fvals = new Array<Object>();
		String fieldname = null;
		boolean validfield = false;
		Array<Object> tablefields = new Array<Object>();
		StdClass tablefield = null;
		Object fieldtype = null;
		Object default_value = null;
		Object fielddef = null;
		Array<Object> tableindices = new Array<Object>();
		Array<Object> index_ary = new Array<Object>();
		Object keyname = null;
		StdClass tableindex = null;
		String index_string = null;
		Object index_name = null;
		Array<Object> index_data = new Array<Object>();
		String index_columns = null;
		Array<Object> column_data = new Array<Object>();
		Object aindex = null;
		Object index = null;
		Array<Object> allqueries = new Array<Object>();
		String query = null;
		if (!is_array(queries)) {
			queries = Strings.explode(";", strval(queries));
			if (equal("", ((Array) queries).getValue(Array.count(queries) - 1))) {
				Array.array_pop((Array) queries);
			}
		}
		cqueries = new Array<Object>();
		iqueries = new Array<Object>();
		for_update = new Array<Object>();
		for (Map.Entry javaEntry243 : ((Array<?>) queries).entrySet())
		/*
		 * Unrecognized query type Unrecognized query type
		 */
		{
			qry = strval(javaEntry243.getValue());
			if (QRegExPerl.preg_match("|CREATE TABLE ([^ ]*)|", qry, matches)) {
				cqueries.putValue(Strings.strtolower(strval(matches.getValue(1))), qry);
				for_update.putValue(matches.getValue(1), "Created table " + strval(matches.getValue(1)));
			}
			else
				if (QRegExPerl.preg_match("|CREATE DATABASE ([^ ]*)|", qry, matches)) {
					Array.array_unshift(cqueries, qry);
				}
				else
					if (QRegExPerl.preg_match("|INSERT INTO ([^ ]*)|", qry, matches)) {
						iqueries.putValue(qry);
					}
					else
						if (QRegExPerl.preg_match("|UPDATE ([^ ]*)|", qry, matches)) {
							iqueries.putValue(qry);
						}
						else {
						}
		}
		if (booleanval(tables = gVars.wpdb.get_col("SHOW TABLES;")))
		/*
		 * For every table in the database For every table in the database
		 */
		{
			for (Map.Entry javaEntry244 : tables.entrySet())
			/*
			 * If a table query exists for the database table... If a table
			 * query exists for the database table...
			 *  ?
			 */
			{
				table = strval(javaEntry244.getValue());
				if (Array.array_key_exists(Strings.strtolower(table), cqueries))
				/*
				 * Clear the field and index arrays Clear the field and index
				 * arrays
				 */

				/*
				 * Get all of the field names in the query from between the
				 * parens Get all of the field names in the query from between
				 * the parens
				 */

				/*
				 * Separate field lines into an array Separate field lines into
				 * an array
				 */

				/*
				 * >>>>"; "; For every field line specified in the query For
				 * every field line specified in the query
				 */

				/*
				 * Fetch the table column structure from the database Fetch the
				 * table column structure from the database
				 */

				/*
				 * For every field in the table For every field in the table
				 */

				/*
				 * For every remaining field specified for the table For every
				 * remaining field specified for the table
				 */

				/*
				 * Index stuff goes here Index stuff goes here Fetch the table
				 * index structure from the database Fetch the table index
				 * structure from the database
				 */

				/*
				 * For every remaining index specified for the table For every
				 * remaining index specified for the table
				 */

				/*
				 * Remove the original table creation query from processing
				 * Remove the original table creation query from processing
				 */
				{
					cfields = null;
					indices = null;
					QRegExPerl.preg_match("|\\((.*)\\)|ms", strval(cqueries.getValue(Strings.strtolower(table))), match2);
					qryline = Strings.trim(strval(match2.getValue(1)));
					flds = Strings.explode("\n", qryline);
					for (Map.Entry javaEntry245 : flds.entrySet())
					/*
					 * Extract the field name Extract the field name
					 */

					/*
					 * Verify the found field name Verify the found field name
					 */

					/*
					 * If it's a valid field, add it to the field array If it's
					 * a valid field, add it to the field array
					 */
					{
						fld = strval(javaEntry245.getValue());
						QRegExPerl.preg_match("|^([^ ]*)|", Strings.trim(fld), fvals);
						fieldname = strval(fvals.getValue(1));
						validfield = true;
						{
							int javaSwitchSelector15 = 0;
							if (equal(Strings.strtolower(fieldname), ""))
								javaSwitchSelector15 = 1;
							if (equal(Strings.strtolower(fieldname), "primary"))
								javaSwitchSelector15 = 2;
							if (equal(Strings.strtolower(fieldname), "index"))
								javaSwitchSelector15 = 3;
							if (equal(Strings.strtolower(fieldname), "fulltext"))
								javaSwitchSelector15 = 4;
							if (equal(Strings.strtolower(fieldname), "unique"))
								javaSwitchSelector15 = 5;
							if (equal(Strings.strtolower(fieldname), "key"))
								javaSwitchSelector15 = 6;
							switch (javaSwitchSelector15) {
								case 1: {
								}
								case 2: {
								}
								case 3: {
								}
								case 4: {
								}
								case 5: {
								}
								case 6: {
									validfield = false;
									indices.putValue(Strings.trim(Strings.trim(fld), ", \n"));
									break;
								}
							}
						}
						fld = Strings.trim(fld);
						if (validfield) {
							cfields.putValue(Strings.strtolower(fieldname), Strings.trim(fld, ", \n"));
						}
					}
					tablefields = gVars.wpdb.get_results("DESCRIBE " + table + ";");
					for (Map.Entry javaEntry246 : tablefields.entrySet())
					/*
					 * If the table field exists in the field array... If the
					 * table field exists in the field array...
					 *  ?
					 */
					{
						tablefield = (StdClass) javaEntry246.getValue();
						if (Array.array_key_exists(Strings.strtolower(strval(StdClass.getValue(tablefield, "Field"))), cfields))
						/*
						 * Get the field type from the query Get the field type
						 * from the query
						 */

						/*
						 * ?
						 * 
						 * 
						 * Get the default value from the array Get the default
						 * value from the array >>"; ";
						 */

						/*
						 * Remove the field from the array (so it's not added)
						 * Remove the field from the array (so it's not added)
						 */
						{
							QRegExPerl.preg_match("|" + StdClass.getValue(tablefield, "Field") + " ([^ ]*( unsigned)?)|i", strval(cfields.getValue(Strings.strtolower(strval(StdClass.getValue(tablefield, "Field"))))), matches);
							fieldtype = matches.getValue(1);
							if (!equal(StdClass.getValue(tablefield, "Type"), fieldtype))
							/*
							 * Add a query to change the column type Add a query
							 * to change the column type
							 */
							{
								cqueries.putValue("ALTER TABLE " + table + " CHANGE COLUMN " + strval(StdClass.getValue(tablefield, "Field")) + " "
								        + strval(cfields.getValue(Strings.strtolower(strval(StdClass.getValue(tablefield, "Field"))))));
								for_update.putValue(table + "." + strval(StdClass.getValue(tablefield, "Field")), "Changed type of " + table + "." + strval(StdClass.getValue(tablefield, "Field"))
								        + " from " + StdClass.getValue(tablefield, "Type") + " to " + strval(fieldtype));
							}
							if (QRegExPerl.preg_match("| DEFAULT \'(.*)\'|i", strval(cfields.getValue(Strings.strtolower(strval(StdClass.getValue(tablefield, "Field"))))), matches)) {
								default_value = matches.getValue(1);
								if (!equal(StdClass.getValue(tablefield, "Default"), default_value))
								/*
								 * Add a query to change the column's default
								 * value Add a query to change the column's
								 * default value
								 */
								{
									cqueries.putValue("ALTER TABLE " + table + " ALTER COLUMN " + strval(StdClass.getValue(tablefield, "Field")) + " SET DEFAULT \'" + strval(default_value) + "\'");
									for_update.putValue(table + "." + strval(StdClass.getValue(tablefield, "Field")), "Changed default value of " + table + "."
									        + strval(StdClass.getValue(tablefield, "Field")) + " from " + StdClass.getValue(tablefield, "Default") + " to " + strval(default_value));
								}
							}
							cfields.arrayUnset(Strings.strtolower(strval(StdClass.getValue(tablefield, "Field"))));
						}
						else {
						}
					}
					for (Map.Entry javaEntry247 : cfields.entrySet())
					/*
					 * Push a query line into $cqueries that adds the field to
					 * that table Push a query line into $cqueries that adds the
					 * field to that table
					 */
					{
						fieldname = strval(javaEntry247.getKey());
						fielddef = javaEntry247.getValue();
						cqueries.putValue("ALTER TABLE " + table + " ADD COLUMN " + strval(fielddef));
						for_update.putValue(table + "." + fieldname, "Added column " + table + "." + fieldname);
					}
					tableindices = gVars.wpdb.get_results("SHOW INDEX FROM " + table + ";");
					if (booleanval(tableindices))
					/*
					 * Clear the index array Clear the index array
					 */

					/*
					 * For every index in the table For every index in the table
					 */

					/*
					 * For each actual index in the index array For each actual
					 * index in the index array
					 */
					{
						index_ary = null;
						for (Map.Entry javaEntry248 : tableindices.entrySet())
						/*
						 * Add the index to the index data array Add the index
						 * to the index data array
						 */
						{
							tableindex = (StdClass) javaEntry248.getValue();
							keyname = StdClass.getValue(tableindex, "Key_name");
							index_ary.getArrayValue(keyname).getArrayValue("columns").putValue(
							        new Array<Object>(new ArrayEntry<Object>("fieldname", StdClass.getValue(tableindex, "Column_name")), new ArrayEntry<Object>("subpart", StdClass.getValue(tableindex, "Sub_part"))));
							index_ary.getArrayValue(keyname).putValue("unique", equal(StdClass.getValue(tableindex, "Non_unique"), 0) ? true : false);
						}
						for (Map.Entry javaEntry249 : index_ary.entrySet())
						/*
						 * Build a create string to compare to the query Build a
						 * create string to compare to the query
						 */

						/*
						 * For each column in the index For each column in the
						 * index
						 */

						/*
						 * Add the column list to the index create string Add
						 * the column list to the index create string
						 */

						/*
						 * >>>\n"; \n"; >>>>>>\n"; \n";
						 */
						{
							index_name = javaEntry249.getKey();
							index_data = (Array<Object>) javaEntry249.getValue();
							index_string = "";
							if (equal(index_name, "PRIMARY")) {
								index_string = index_string + "PRIMARY ";
							}
							else
								if (booleanval(index_data.getValue("unique"))) {
									index_string = index_string + "UNIQUE ";
								}
							index_string = index_string + "KEY ";
							if (!equal(index_name, "PRIMARY")) {
								index_string = index_string + strval(index_name);
							}
							index_columns = "";
							for (Map.Entry javaEntry250 : (Set<Map.Entry>) index_data.getArrayValue("columns").entrySet())
							/*
							 * Add the field to the column list string Add the
							 * field to the column list string
							 */
							{
								column_data = (Array<Object>) javaEntry250.getValue();
								if (!equal(index_columns, "")) {
									index_columns = index_columns + ",";
								}
								index_columns = index_columns + strval(column_data.getValue("fieldname"));
								if (!equal(column_data.getValue("subpart"), "")) {
									index_columns = index_columns + "(" + strval(column_data.getValue("subpart")) + ")";
								}
							}
							index_string = index_string + " (" + index_columns + ")";
							if (!strictEqual(aindex = Array.array_search(index_string, indices), null)) {
								indices.arrayUnset(aindex);
							}
						}
					}
					for (Map.Entry javaEntry251 : new Array<Object>(indices).entrySet())
					/*
					 * Push a query line into $cqueries that adds the index to
					 * that table Push a query line into $cqueries that adds the
					 * index to that table
					 */
					{
						index = javaEntry251.getValue();
						cqueries.putValue("ALTER TABLE " + table + " ADD " + strval(index));
						for_update.putValue(table + "." + fieldname, "Added index " + table + " " + strval(index));
					}
					cqueries.arrayUnset(Strings.strtolower(table));
					for_update.arrayUnset(Strings.strtolower(table));
				}
				else {
				}
			}
		}
		allqueries = Array.array_merge(cqueries, iqueries);
		if (execute) {
			for (Map.Entry javaEntry252 : allqueries.entrySet())
			/*
			 * >>\n"; \n";
			 */
			{
				query = strval(javaEntry252.getValue());
				gVars.wpdb.query(query);
			}
		}
		return for_update;
	}

	public void make_db_current() {
		Array<Object> alterations = new Array<Object>();
		Object alteration = null;
		alterations = dbDelta(gVars.wp_queries, true);
		echo(gVars.webEnv, "<ol>\n");
		for (Map.Entry javaEntry253 : alterations.entrySet()) {
			alteration = javaEntry253.getValue();
			echo(gVars.webEnv, "<li>" + strval(alteration) + "</li>\n");
		}
		echo(gVars.webEnv, "</ol>\n");
	}

	public void make_db_current_silent() {
		Array<Object> alterations = new Array<Object>();
		alterations = dbDelta(gVars.wp_queries, true);
	}

	public boolean make_site_theme_from_oldschool(Object theme_name, String template) {
		String home_path;
		String site_dir = null;
		Array<Object> files = new Array<Object>();
		Object oldfile = null;
		String oldpath;
		String index = null;
		Object newfile = null;
		Array<String> lines = new Array<String>();
		int f = 0;
		String line = null;
		String header = null;
		String stylelines = null;
		home_path = getIncluded(FilePage.class, gVars, gConsts).get_home_path();
		site_dir = gConsts.getABSPATH() + "wp-content/themes/" + template;

		// Modified by Numiton

		if (false) {
			if (!FileSystemOrSocket.file_exists(gVars.webEnv, home_path + "/index.php")) {
				return false;
			}
		}
		files = new Array<Object>(new ArrayEntry<Object>("index.php", "index.php"), new ArrayEntry<Object>("wp-layout.css", "style.css"), new ArrayEntry<Object>("wp-comments.php", "comments.php"),
		        new ArrayEntry<Object>("wp-comments-popup.php", "comments-popup.php"));
		for (Map.Entry javaEntry254 : files.entrySet())
		/*
		 * Update the blog header include in each file. Update the blog header
		 * include in each file.
		 */
		{
			oldfile = javaEntry254.getKey();
			newfile = javaEntry254.getValue();
			if (equal(oldfile, "index.php")) {
				oldpath = home_path;
			}
			else
				oldpath = gConsts.getABSPATH();
			if (equal(oldfile, "index.php"))
			/*
			 * Check to make sure it's not a new index Check to make sure it's
			 * not a new index
			 */

			/*
			 * Don't copy anything Don't copy anything
			 */
			{
				index = Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, oldpath + "/" + strval(oldfile)));
				if (!strictEqual(Strings.strpos(index, "WP_USE_THEMES"), BOOLEAN_FALSE)) {
					if (!FileSystemOrSocket.copy(gVars.webEnv, gConsts.getABSPATH() + "wp-content/themes/default/index.php", site_dir + "/" + strval(newfile))) {
						return false;
					}
					continue;
				}
			}
			if (!FileSystemOrSocket.copy(gVars.webEnv, oldpath + "/" + strval(oldfile), site_dir + "/" + strval(newfile))) {
				return false;
			}
			JFileSystemOrSocket.chmod(gVars.webEnv, site_dir + "/" + strval(newfile), 777);
			lines = Strings.explode("\n", Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, site_dir + "/" + strval(newfile))));
			if (booleanval(lines)) {
				f = FileSystemOrSocket.fopen(gVars.webEnv, site_dir + "/" + strval(newfile), "w");
				for (Map.Entry javaEntry255 : lines.entrySet())
				/*
				 * Update stylesheet references. Update stylesheet references.
				 */

				/*
				 * Update comments template inclusion. Update comments template
				 * inclusion.
				 */
				{
					line = strval(javaEntry255.getValue());
					if (QRegExPerl.preg_match("/require.*wp-blog-header/", line)) {
						line = "//" + line;
					}
					line = Strings.str_replace("<?php echo __get_option(\'siteurl\'); ?>/wp-layout.css", "<?php bloginfo(\'stylesheet_url\'); ?>", line);
					line = Strings.str_replace("<?php include(ABSPATH . \'wp-comments.php\'); ?>", "<?php comments_template(); ?>", line);
					FileSystemOrSocket.fwrite(gVars.webEnv, f, line + "\n");
				}
				FileSystemOrSocket.fclose(gVars.webEnv, f);
			}
		}
		header = "/*\nTheme Name: " + strval(theme_name) + "\nTheme URI: " + strval(__get_option("siteurl"))
		        + "\nDescription: A theme automatically created by the upgrade.\nVersion: 1.0\nAuthor: Moi\n*/\n";
		stylelines = FileSystemOrSocket.file_get_contents(gVars.webEnv, site_dir + "/style.css");
		if (booleanval(stylelines)) {
			f = FileSystemOrSocket.fopen(gVars.webEnv, site_dir + "/style.css", "w");
			FileSystemOrSocket.fwrite(gVars.webEnv, f, header);
			FileSystemOrSocket.fwrite(gVars.webEnv, f, stylelines);
			FileSystemOrSocket.fclose(gVars.webEnv, f);
		}
		return true;
	}

	public boolean make_site_theme_from_default(Object theme_name, String template) {
		String site_dir = null;
		String default_dir = null;
		int theme_dir = 0;
		String theme_file = null;
		Array<String> stylelines = new Array<String>();
		int f = 0;
		String line = null;
		int images_dir = 0;
		String image = null;
		site_dir = gConsts.getABSPATH() + "wp-content/themes/" + template;
		default_dir = gConsts.getABSPATH() + "wp-content/themes/default";
		theme_dir = Directories.opendir(gVars.webEnv, default_dir);
		if (booleanval(theme_dir)) {
			while (!strictEqual(theme_file = Directories.readdir(gVars.webEnv, theme_dir), STRING_FALSE)) {
				if (FileSystemOrSocket.is_dir(gVars.webEnv, default_dir + "/" + theme_file)) {
					continue;
				}
				if (!FileSystemOrSocket.copy(gVars.webEnv, default_dir + "/" + theme_file, site_dir + "/" + theme_file)) {
					return false;
				}
				JFileSystemOrSocket.chmod(gVars.webEnv, site_dir + "/" + theme_file, 777);
			}
		}
		Directories.closedir(gVars.webEnv, theme_dir);
		stylelines = Strings.explode("\n", Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, site_dir + "/style.css")));
		if (booleanval(stylelines)) {
			f = FileSystemOrSocket.fopen(gVars.webEnv, site_dir + "/style.css", "w");
			for (Map.Entry javaEntry256 : stylelines.entrySet()) {
				line = strval(javaEntry256.getValue());
				if (!strictEqual(Strings.strpos(line, "Theme Name:"), BOOLEAN_FALSE)) {
					line = "Theme Name: " + strval(theme_name);
				}
				else
					if (!strictEqual(Strings.strpos(line, "Theme URI:"), BOOLEAN_FALSE)) {
						line = "Theme URI: " + strval(__get_option("url"));
					}
					else
						if (!strictEqual(Strings.strpos(line, "Description:"), BOOLEAN_FALSE)) {
							line = "Description: Your theme.";
						}
						else
							if (!strictEqual(Strings.strpos(line, "Version:"), BOOLEAN_FALSE)) {
								line = "Version: 1";
							}
							else
								if (!strictEqual(Strings.strpos(line, "Author:"), BOOLEAN_FALSE)) {
									line = "Author: You";
								}
				FileSystemOrSocket.fwrite(gVars.webEnv, f, line + "\n");
			}
			FileSystemOrSocket.fclose(gVars.webEnv, f);
		}
		JFileSystemOrSocket.umask(0);
		if (!JFileSystemOrSocket.mkdir(gVars.webEnv, site_dir + "/images", 777)) {
			return false;
		}
		images_dir = Directories.opendir(gVars.webEnv, default_dir + "/images");
		if (booleanval(images_dir)) {
			while (!strictEqual(image = Directories.readdir(gVars.webEnv, images_dir), STRING_FALSE)) {
				if (FileSystemOrSocket.is_dir(gVars.webEnv, default_dir + "/images/" + image)) {
					continue;
				}
				if (!FileSystemOrSocket.copy(gVars.webEnv, default_dir + "/images/" + image, site_dir + "/images/" + image)) {
					return false;
				}
				JFileSystemOrSocket.chmod(gVars.webEnv, site_dir + "/images/" + image, 777);
			}
		}
		Directories.closedir(gVars.webEnv, images_dir);
		return false;
	}

	/** 
	 * Create a site theme from the default theme. Create a site theme from the
	 * default theme.
	 */
	public String make_site_theme() {
		String theme_name = null;
		String template = null;
		String site_dir = null;
		Object current_template = null;
		theme_name = strval(__get_option("blogname"));
		template = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(theme_name, "");
		site_dir = gConsts.getABSPATH() + "wp-content/themes/" + template;
		if (FileSystemOrSocket.is_dir(gVars.webEnv, site_dir)) {
			return strval(false);
		}
		if (!FileSystemOrSocket.is_writable(gVars.webEnv, gConsts.getABSPATH() + "wp-content/themes")) {
			return strval(false);
		}
		JFileSystemOrSocket.umask(0);
		if (!JFileSystemOrSocket.mkdir(gVars.webEnv, site_dir, 777)) {
			return strval(false);
		}
		if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-layout.css")) {
			if (!make_site_theme_from_oldschool(theme_name, template))
			/*
			 * TODO: rm -rf the site theme directory. TODO: rm -rf the site
			 * theme directory.
			 */
			{
				return strval(false);
			}
		}
		else
		/*
		 * TODO: rm -rf the site theme directory. TODO: rm -rf the site theme
		 * directory.
		 */
		{
			if (!make_site_theme_from_default(theme_name, template)) {
				return strval(false);
			}
		}
		current_template = __get_option("template");
		if (equal(current_template, "default")) {
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("template", template);
			getIncluded(FunctionsPage.class, gVars, gConsts).update_option("stylesheet", template);
		}
		return template;
	}

	public String translate_level_to_role(String level) {
		{
			int javaSwitchSelector16 = 0;
			if (equal(level, 10))
				javaSwitchSelector16 = 1;
			if (equal(level, 9))
				javaSwitchSelector16 = 2;
			if (equal(level, 8))
				javaSwitchSelector16 = 3;
			if (equal(level, 7))
				javaSwitchSelector16 = 4;
			if (equal(level, 6))
				javaSwitchSelector16 = 5;
			if (equal(level, 5))
				javaSwitchSelector16 = 6;
			if (equal(level, 4))
				javaSwitchSelector16 = 7;
			if (equal(level, 3))
				javaSwitchSelector16 = 8;
			if (equal(level, 2))
				javaSwitchSelector16 = 9;
			if (equal(level, 1))
				javaSwitchSelector16 = 10;
			if (equal(level, 0))
				javaSwitchSelector16 = 11;
			switch (javaSwitchSelector16) {
				case 1: {
				}
				case 2: {
				}
				case 3: {
					return "administrator";
				}
				case 4: {
				}
				case 5: {
				}
				case 6: {
					return "editor";
				}
				case 7: {
				}
				case 8: {
				}
				case 9: {
					return "author";
				}
				case 10: {
					return "contributor";
				}
				case 11: {
					return "subscriber";
				}
			}
		}
		return "";
	}

	public void wp_check_mysql_version() {
		Object result = null;
		result = gVars.wpdb.check_database_version();
		if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
			System.exit(((WP_Error) result).get_error_message());
		}
	}

	public void maybe_disable_automattic_widgets() {
		Array<Object> plugins;
		String plugin = null;
		plugins = (Array<Object>) __get_option("active_plugins");
		for (Map.Entry javaEntry257 : plugins.entrySet()) {
			plugin = strval(javaEntry257.getValue());
			if (equal(FileSystemOrSocket.basename(plugin), "widgets.php")) {
				Array.array_splice(plugins, intval(Array.array_search(plugin, plugins)), 1);
				getIncluded(FunctionsPage.class, gVars, gConsts).update_option("active_plugins", plugins);
				break;
			}
		}
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_admin_includes_upgrade_block1");
		gVars.webEnv = webEnv;

		// Commented by Numiton

		//		if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-content/install.php")) {

		//			requireOnce(gVars, gConsts, InstallPage.class);

		//		}

		// Condensed dynamic construct

		requireOnce(gVars, gConsts, AdminPage.class);

		// Condensed dynamic construct

		requireOnce(gVars, gConsts, SchemaPage.class);

		// Removed by Numiton. All functions are declared.

		return DEFAULT_VAL;
	}
	public Object	wp_current_db_version;
}
