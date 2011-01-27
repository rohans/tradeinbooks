/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UserPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ClassHandling;
import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;

@Controller(value="wp_admin/includes/UserPage")
@Scope("request")
public class UserPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(UserPage.class.getName());

	@Override
	@RequestMapping("/wp-admin/includes/user.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_admin/includes/user";
	}

	/** 
	 * Creates a new user from the "Users" form using $_POST information.
	 */
	public Object add_user(Object... vargs) {
		int user_id = 0;
		WP_User user = null;

		// Modified by Numiton

		if (booleanval(FunctionHandling.func_num_args(vargs))) { // The hackiest hack that ever did hack
			user_id = intval(FunctionHandling.func_get_arg(vargs, 0));
			if (isset(gVars.webEnv._POST.getValue("role"))) {
				if (!equal(user_id, gVars.current_user.getID()) || gVars.wp_roles.role_objects.getValue(gVars.webEnv._POST.getValue("role")).has_cap("edit_users")) {
					user = new WP_User(gVars, gConsts, user_id);
					user.set_role(gVars.webEnv._POST.getValue("role"));
				}
			}
		}
		else {
			getIncluded(PluginPage.class, gVars, gConsts).add_action("user_register", Callback.createCallbackArray(this, "add_user"), 10, 1); // See above
			return edit_user(0);
		}
		return null;
	}

	public Object edit_user(int user_id) {
		boolean update = false;
		WP_User user = new WP_User(gVars, gVars.gConsts, user_id);
		StdClass userdata;
		String pass1;
		String pass2;
		WP_Error errors = null;
		if (!equal(user_id, 0)) {
			update = true;
			user.setID(user_id);
			userdata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
			user.setUser_login(gVars.wpdb.escape(strval(StdClass.getValue(userdata, "user_login"))));
		}
		else {
			update = false;
			user = new WP_User(gVars, gVars.gConsts, user_id);
		}
		if (isset(gVars.webEnv._POST.getValue("user_login"))) {
			user.setUser_login(getIncluded(FormattingPage.class, gVars, gConsts)
			        .wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("user_login"))), strval(0)));
		}
		pass1 = pass2 = "";
		if (isset(gVars.webEnv._POST.getValue("pass1"))) {
			pass1 = strval(gVars.webEnv._POST.getValue("pass1"));
		}
		if (isset(gVars.webEnv._POST.getValue("pass2"))) {
			pass2 = strval(gVars.webEnv._POST.getValue("pass2"));
		}
		if (isset(gVars.webEnv._POST.getValue("role")) && getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users")) {
			if (!equal(user_id, gVars.current_user.getID()) || gVars.wp_roles.role_objects.getValue(gVars.webEnv._POST.getValue("role")).has_cap("edit_users")) {
				user.setRole(gVars.webEnv._POST.getValue("role"));
			}
		}
		if (isset(gVars.webEnv._POST.getValue("email"))) {
			user.setUser_email(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("email"))), strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("url"))) {
			user.setUser_url(getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.trim(strval(gVars.webEnv._POST.getValue("url"))), null, "display"));
			user.setUser_url((QRegExPerl.preg_match("/^(https?|ftps?|mailto|news|irc|gopher|nntp|feed|telnet):/is", user.getUser_url()) ? user.getUser_url() : ("http://" + user.getUser_url())));
		}
		if (isset(gVars.webEnv._POST.getValue("first_name"))) {
			user.setFirst_name(getIncluded(FormattingPage.class, gVars, gConsts)
			        .wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("first_name"))), strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("last_name"))) {
			user.setLast_name(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("last_name"))), strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("nickname"))) {
			user.setNickname(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("nickname"))), strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("display_name"))) {
			user.setDisplay_name(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("display_name"))),
			        strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("description"))) {
			user.setDescription(Strings.trim(strval(gVars.webEnv._POST.getValue("description"))));
		}
		if (isset(gVars.webEnv._POST.getValue("jabber"))) {
			user.setJabber(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("jabber"))), strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("aim"))) {
			user.setAim(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("aim"))), strval(0)));
		}
		if (isset(gVars.webEnv._POST.getValue("yim"))) {
			user.setYim(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.trim(strval(gVars.webEnv._POST.getValue("yim"))), strval(0)));
		}

		if (!update) {
			user.setRich_editing("true");  // Default to true for new users.
		}
		else
			if (isset(gVars.webEnv._POST.getValue("rich_editing"))) {
				user.setRich_editing(strval(gVars.webEnv._POST.getValue("rich_editing")));
			}
			else
				user.setRich_editing("false");

		if (!update) {
			user.setAdmin_color("fresh");  // Default to fresh for new users.
		}
		else
			if (isset(gVars.webEnv._POST.getValue("admin_color"))) {
				user.setAdmin_color(strval(gVars.webEnv._POST.getValue("admin_color")));
			}
			else {
				user.setAdmin_color("fresh");
			}
		
		errors = new WP_Error(gVars, gConsts);
		
		/* checking that username has been typed */
		if (equal(user.getUser_login(), "")) {
			errors.add("user_login", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please enter a username.", "default"));
		}
		
		/* checking the password has been typed twice */
		getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("check_passwords", new Array<Object>(new ArrayEntry<Object>(user.getUser_login()),
		        new ArrayEntry<Object>(pass1), new ArrayEntry<Object>(pass2)));
		
		if (update) {
			if (empty(pass1) && !empty(pass2)) {
				errors.add("pass", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: You entered your new password only once.", "default"),
				        new Array<Object>(new ArrayEntry<Object>("form-field", "pass1")));
			}
			else
				if (!empty(pass1) && empty(pass2)) {
					errors.add("pass", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: You entered your new password only once.", "default"),
					        new Array<Object>(new ArrayEntry<Object>("form-field", "pass2")));
				}
		}
		else {
			if (empty(pass1)) {
				errors.add("pass", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please enter your password.", "default"), new Array<Object>(
				        new ArrayEntry<Object>("form-field", "pass1")));
			}
			else
				if (empty(pass2)) {
					errors.add("pass", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please enter your password twice.", "default"), new Array<Object>(
					        new ArrayEntry<Object>("form-field", "pass2")));
				}
		}
		
		/* Check for "\" in password */
		if (BOOLEAN_FALSE != Strings.strpos(" " + pass1, "\\")) {
			errors.add("pass", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Passwords may not contain the character \"\\\".", "default"),
			        new Array<Object>(new ArrayEntry<Object>("form-field", "pass1")));
		}
		
		/* checking the password has been typed twice the same */
		if (!equal(pass1, pass2)) {
			errors.add("pass", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please enter the same password in the two password fields.", "default"),
			        new Array<Object>(new ArrayEntry<Object>("form-field", "pass1")));
		}
		if (!empty(pass1)) {
			user.setUser_pass(pass1);
		}
		if (!update && !getIncluded(RegistrationPage.class, gVars, gConsts).validate_username(user.getUser_login())) {
			errors.add("user_login", getIncluded(L10nPage.class, gVars, gConsts)
			        .__("<strong>ERROR</strong>: This username is invalid. Please enter a valid username.", "default"));
		}
		if (!update && booleanval(getIncluded(RegistrationPage.class, gVars, gConsts).username_exists(user.getUser_login()))) {
			errors.add("user_login", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: This username is already registered. Please choose another one.",
			        "default"));
		}
		
		/* checking e-mail address */
		if (empty(user.getUser_email())) {
			errors.add("user_email", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Please enter an e-mail address.", "default"), new Array<Object>(
			        new ArrayEntry<Object>("form-field", "email")));
		}
		else
			if (!getIncluded(FormattingPage.class, gVars, gConsts).is_email(user.getUser_email())) {
				errors.add("user_email", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: The e-mail address isn\'t correct.", "default"),
				        new Array<Object>(new ArrayEntry<Object>("form-field", "email")));
			}
		if (booleanval(errors.get_error_codes())) {
			return errors;
		}
		if (update) {
			user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_update_user(ClassHandling.get_object_vars(user));
		}
		else {
			user_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(ClassHandling.get_object_vars(user));
			getIncluded(PluggablePage.class, gVars, gConsts).wp_new_user_notification(user_id, "");
		}
		return user_id;
	}

	public Array<Object> get_author_user_ids() {
		String level_key = null;
		String query = null;
		level_key = gVars.wpdb.prefix + "user_level";
		query = "SELECT user_id FROM " + gVars.wpdb.usermeta + " WHERE meta_key = \'" + level_key + "\' AND meta_value != \'0\'";
		return gVars.wpdb.get_col(query);
	}

	public Array<Object> get_editable_authors(Object user_id) {
		Array editable = null;
		Array<Object> authors = new Array<Object>();
		editable = get_editable_user_ids(intval(user_id), true);
		if (!booleanval(editable)) {
			return new Array<Object>();
		}
		else {
			String editableStr = Strings.join(",", editable);
			authors = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.users + " WHERE ID IN (" + editableStr + ") ORDER BY display_name");
		}
		return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_editable_authors", authors);
	}

	public Array<Object> get_editable_user_ids(int user_id, boolean exclude_zeros) {
		WP_User user = null;
		String level_key = null;
		String query = null;
		user = new WP_User(gVars, gConsts, user_id);
		if (!user.has_cap("edit_others_posts")) {
			if (user.has_cap("edit_posts") || equal(exclude_zeros, false)) {
				return new Array<Object>(new ArrayEntry<Object>(user.getID()));
			}
			else
				return new Array<Object>();
		}
		level_key = gVars.wpdb.prefix + "user_level";
		query = "SELECT user_id FROM " + gVars.wpdb.usermeta + " WHERE meta_key = \'" + level_key + "\'";
		if (exclude_zeros) {
			query = query + " AND meta_value != \'0\'";
		}
		return gVars.wpdb.get_col(query);
	}

	public Array<Object> get_nonauthor_user_ids() {
		String level_key = null;
		String query = null;
		level_key = gVars.wpdb.prefix + "user_level";
		query = "SELECT user_id FROM " + gVars.wpdb.usermeta + " WHERE meta_key = \'" + level_key + "\' AND meta_value = \'0\'";
		return gVars.wpdb.get_col(query);
	}

	public Array get_others_unpublished_posts(Object user_id, String type) {
		Array editable = null;
		String type_sql = null;
		String dir = null;
		Array other_unpubs = null;
		editable = get_editable_user_ids(intval(user_id), true);
		if (Array.in_array(type, new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
			type_sql = " post_status = \'" + type + "\' ";
		}
		else
			type_sql = " ( post_status = \'draft\' OR post_status = \'pending\' ) ";
		dir = (equal("pending", type) ? "ASC" : "DESC");
		if (!booleanval(editable)) {
			other_unpubs = new Array();
		}
		else {
			String editableStr = Strings.join(",", editable);
			other_unpubs = gVars.wpdb.get_results("SELECT ID, post_title, post_author FROM " + gVars.wpdb.posts + " WHERE post_type = \'post\' AND " + type_sql + " AND post_author IN (" + editableStr
			        + ") AND post_author != \'" + user_id + "\' ORDER BY post_modified " + dir);
		}
		return (Array) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_others_drafts", other_unpubs);
	}

	public Array get_others_drafts(Object user_id) {
		return get_others_unpublished_posts(user_id, "draft");
	}

	public Array get_others_pending(Object user_id) {
		return get_others_unpublished_posts(user_id, "pending");
	}

	public WP_User get_user_to_edit(Object user_id) {
		WP_User user = null;
		user = new WP_User(gVars, gConsts, user_id);
		user.setUser_login(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getUser_login()));
		user.setUser_email(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getUser_email()));
		user.setUser_url(getIncluded(FormattingPage.class, gVars, gConsts).clean_url(user.getUser_url(), null, "display"));
		user.setFirst_name(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getFirst_name()));
		user.setLast_name(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getLast_name()));
		user.setDisplay_name(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getDisplay_name()));
		user.setNickname(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getNickname()));
		user.setAim(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getAim()));
		user.setYim(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getYim()));
		user.setJabber(getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(user.getJabber()));
		user.setDescription(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(user.getDescription(), strval(0)));
		return user;
	}

	public Array<Object> get_users_drafts(int user_id) {
		String query = null;
		user_id = user_id;
		query = "SELECT ID, post_title FROM " + gVars.wpdb.posts + " WHERE post_type = \'post\' AND post_status = \'draft\' AND post_author = " + strval(user_id) + " ORDER BY post_modified DESC";
		query = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_users_drafts", query));
		return gVars.wpdb.get_results(query);
	}

	public boolean wp_delete_user(Object id, String reassign) {
		Array<Object> post_ids = new Array<Object>();
		Object post_id = null;
		Object user = null;
		id = intval(id);
		if (equal(reassign, "novalue")) {
			post_ids = gVars.wpdb.get_col("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_author = " + id);
			if (booleanval(post_ids)) {
				for (Map.Entry javaEntry258 : post_ids.entrySet()) {
					post_id = javaEntry258.getValue();
					getIncluded(PostPage.class, gVars, gConsts).wp_delete_post(intval(post_id));
				}
			}
			
			// Clean links
			gVars.wpdb.query("DELETE FROM " + gVars.wpdb.links + " WHERE link_owner = " + id);
		}
		else {
			reassign = reassign;
			gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_author = " + reassign + " WHERE post_author = " + id);
			gVars.wpdb.query("UPDATE " + gVars.wpdb.links + " SET link_owner = " + reassign + " WHERE link_owner = " + id);
		}
		
		// FINALLY, delete user
		getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_user", id);
		
		gVars.wpdb.query("DELETE FROM " + gVars.wpdb.users + " WHERE ID = " + id);
		gVars.wpdb.query("DELETE FROM " + gVars.wpdb.usermeta + " WHERE user_id = \'" + id + "\'");
		getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(id, "users");
		

		// Commented by Numiton TODO Is this a bug?
		//		getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user.getUser_login(), "userlogins");
		//		getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user.getUser_email(), "useremail");

		return true;
	}

	public void wp_revoke_user(int id) {
		WP_User user = null;
		id = id;
		user = new WP_User(gVars, gConsts, id);
		user.remove_all_caps();
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {
		gVars.webEnv = webEnv;

		// Removed by Numiton. All classes defined.

		return DEFAULT_VAL;
	}
}
