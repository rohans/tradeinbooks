/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: RegistrationPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_includes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ClassHandling;
import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;


@Controller
@Scope("request")
public class RegistrationPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(RegistrationPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/registration.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/registration";
    }

    /**
     * User Registration API
     *
     * @package WordPress
     */

    /**
     * username_exists() - Checks whether the given username exists.
     *
     * @since 2.0.0
     *
     * @param string $username Username.
     * @return null|int The user's ID on success, and null on failure.
     */
    public int username_exists(String username) {
        StdClass user;

        if (booleanval(user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(username))) {
            return intval(StdClass.getValue(user, "ID"));
        } else {
            return intval(null);
        }
    }

    /**
     * email_exists() - Checks whether the given email exists.
     * @since 2.1.0
     * @uses $wpdb
     * @param string $email Email.
     * @return bool|int The user's ID on success, and false on failure.
     */
    public int email_exists(String email) {
        StdClass user;

        if (booleanval(user = getIncluded(PluggablePage.class, gVars, gConsts).get_user_by_email(email))) {
            return intval(StdClass.getValue(user, "ID"));
        }

        return intval(false);
    }

    /**
     * validate_username() - Checks whether an username is valid.
     * @since 2.0.1
     * @uses apply_filters() Calls 'validate_username' hook on $valid check and
     * $username as parameters
     * @param string $username Username.
     * @return bool Whether username given is valid
     */
    public boolean validate_username(String username) {
        Object sanitized = null;
        Object valid = null;
        sanitized = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(username, true);
        valid = equal(sanitized, username);

        return booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("validate_username", valid, username));
    }

    /**
     * wp_insert_user() - Insert an user into the database.
     *
     * Can update a current user or insert a new user based on whether
     * the user's ID is present.
     *
     * Can be used to update the user's info (see below), set the user's
     * role, and set the user's preference on whether they want the rich
     * editor on.
     *
     * Most of the $userdata array fields have filters associated with
     * the values. The exceptions are 'rich_editing', 'role', 'jabber',
     * 'aim', 'yim', 'user_registered', and 'ID'. The filters have the
     * prefix 'pre_user_' followed by the field name. An example using
     * 'description' would have the filter called, 'pre_user_description'
     * that can be hooked into.
     *
     * The $userdata array can contain the following fields:
     * 'ID' - An integer that will be used for updating an existing user.
     * 'user_pass' - A string that contains the plain text password for the user.
     * 'user_login' - A string that contains the user's username for logging in.
     * 'user_nicename' - A string that contains a nicer looking name for the user.
     *                The default is the user's username.
     * 'user_url' - A string containing the user's URL for the user's web site.
     * 'user_email' - A string containing the user's email address.
     * 'display_name' - A string that will be shown on the site. Defaults to user's username.
     *                It is likely that you will want to change this, for both appearance and security
     *                through obscurity (that is if you don't use and delete the default 'admin' user).
     * 'nickname' - The user's nickname, defaults to the user's username.
     * 'first_name' - The user's first name.
     * 'last_name' - The user's last name.
     * 'description' - A string containing content about the user.
     * 'rich_editing' - A string for whether to enable the rich editor or not. False if not
     *                empty.
     * 'user_registered' - The date the user registered. Format is 'Y-m-d H:i:s'.
     * 'role' - A string used to set the user's role.
     * 'jabber' - User's Jabber account.
     * 'aim' - User's AOL IM account.
     * 'yim' - User's Yahoo IM account.
     *
     * @since 2.0.0
     * @uses $wpdb WordPress database layer.
     * @uses apply_filters() Calls filters for most of the $userdata fields with the prefix 'pre_user'. See note above.
     * @uses do_action() Calls 'profile_update' hook when updating giving the user's ID
     * @uses do_action() Calls 'user_register' hook when creating a new user giving the user's ID
     *
     * @param array $userdata An array of user data.
     * @return int The newly created user's ID.
     */
    public int wp_insert_user(Array<Object> userdata) {
        Integer ID = null;
        Boolean update = null;
        String user_pass = null;
        String user_login = null;
        Object user_nicename = null;
        String user_url = null;
        String user_email = null;
        Object display_name = null;
        Object nickname = null;
        String first_name = null;
        String last_name = null;
        String description = null;
        String rich_editing = null;
        String admin_color = null;
        String user_registered = null;
        Array<Object> data = new Array<Object>();
        Integer user_id = null;
        Object jabber = null;
        Object aim = null;
        Object yim = null;
        Object role = null;
        WP_User user = null;

        ID = intval(Array.extractVar(userdata, "ID", ID, Array.EXTR_SKIP));
        user_pass = strval(Array.extractVar(userdata, "user_pass", user_pass, Array.EXTR_SKIP));
        user_login = strval(Array.extractVar(userdata, "user_login", user_login, Array.EXTR_SKIP));
        user_nicename = Array.extractVar(userdata, "user_nicename", user_nicename, Array.EXTR_SKIP);
        user_url = strval(Array.extractVar(userdata, "user_url", user_url, Array.EXTR_SKIP));
        user_email = strval(Array.extractVar(userdata, "user_email", user_email, Array.EXTR_SKIP));
        display_name = Array.extractVar(userdata, "display_name", display_name, Array.EXTR_SKIP);
        nickname = Array.extractVar(userdata, "nickname", nickname, Array.EXTR_SKIP);
        first_name = strval(Array.extractVar(userdata, "first_name", first_name, Array.EXTR_SKIP));
        last_name = strval(Array.extractVar(userdata, "last_name", last_name, Array.EXTR_SKIP));
        description = strval(Array.extractVar(userdata, "description", description, Array.EXTR_SKIP));
        rich_editing = strval(Array.extractVar(userdata, "rich_editing", rich_editing, Array.EXTR_SKIP));
        admin_color = strval(Array.extractVar(userdata, "admin_color", admin_color, Array.EXTR_SKIP));
        user_registered = strval(Array.extractVar(userdata, "user_registered", user_registered, Array.EXTR_SKIP));
        jabber = Array.extractVar(userdata, "jabber", jabber, Array.EXTR_SKIP);
        aim = Array.extractVar(userdata, "aim", aim, Array.EXTR_SKIP);
        yim = Array.extractVar(userdata, "yim", yim, Array.EXTR_SKIP);
        role = Array.extractVar(userdata, "role", role, Array.EXTR_SKIP);

        // Are we updating or creating?
        if (!empty(ID)) {
            ID = ID;
            update = true;
        } else {
            update = false;
            // Hash the password
            user_pass = getIncluded(PluggablePage.class, gVars, gConsts).wp_hash_password(user_pass);
        }

        user_login = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(user_login, true);
        user_login = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_login", user_login));

        if (empty(user_nicename)) {
            user_nicename = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(user_login, "");
        }

        user_nicename = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_nicename", user_nicename);

        if (empty(user_url)) {
            user_url = "";
        }

        user_url = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_url", user_url));

        if (empty(user_email)) {
            user_email = "";
        }

        user_email = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_email", user_email));

        if (empty(display_name)) {
            display_name = user_login;
        }

        display_name = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_display_name", display_name);

        if (empty(nickname)) {
            nickname = user_login;
        }

        nickname = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_nickname", nickname);

        if (empty(first_name)) {
            first_name = "";
        }

        first_name = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_first_name", first_name));

        if (empty(last_name)) {
            last_name = "";
        }

        last_name = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_last_name", last_name));

        if (empty(description)) {
            description = "";
        }

        description = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_description", description));

        if (empty(rich_editing)) {
            rich_editing = "true";
        }

        if (empty(admin_color)) {
            admin_color = "fresh";
        }

        admin_color = QRegExPerl.preg_replace("|[^a-z0-9 _.\\-@]|i", "", admin_color);

        if (empty(user_registered)) {
            user_registered = DateTime.gmdate("Y-m-d H:i:s");
        }

        data = Array.compact(
                new ArrayEntry("user_pass", user_pass),
                new ArrayEntry("user_email", user_email),
                new ArrayEntry("user_url", user_url),
                new ArrayEntry("user_nicename", user_nicename),
                new ArrayEntry("display_name", display_name),
                new ArrayEntry("user_registered", user_registered));
        data = (Array<Object>) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(data);

        if (update) {
            gVars.wpdb.update(gVars.wpdb.users, data, Array.compact(new ArrayEntry("ID", ID)));
            user_id = ID;
        } else {
            gVars.wpdb.insert(gVars.wpdb.users, Array.arrayAppend(data, Array.compact(new ArrayEntry("user_login", user_login))));
            user_id = gVars.wpdb.insert_id;
        }

        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "first_name", first_name);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "last_name", last_name);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "nickname", nickname);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "description", description);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "jabber", jabber);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "aim", aim);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "yim", yim);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "rich_editing", rich_editing);
        getIncluded(UserPage.class, gVars, gConsts).update_usermeta(user_id, "admin_color", admin_color);

        if (update && isset(role)) {
            user = new WP_User(gVars, gConsts, user_id);
            user.set_role(role);
        }

        if (!update) {
            user = new WP_User(gVars, gConsts, user_id);
            user.set_role(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_role"));
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user_id, "users");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user_login, "userlogins");

        if (update) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("profile_update", user_id);
        } else {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("user_register", user_id);
        }

        return user_id;
    }

    /**
     * wp_update_user() - Update an user in the database
     *
     * It is possible to update a user's password by specifying the
     * 'user_pass' value in the $userdata parameter array.
     *
     * If $userdata does not contain an 'ID' key, then a new user
     * will be created and the new user's ID will be returned.
     *
     * If current user's password is being updated, then the cookies
     * will be cleared.
     *
     * @since 2.0.0
     * @see wp_insert_user() For what fields can be set in $userdata
     * @uses wp_insert_user() Used to update existing user or add new one if user doesn't exist already
     *
     * @param array $userdata An array of user data.
     * @return int The updated user's ID.
     */
    public int wp_update_user(Array<Object> userdata) {
        int ID = 0;
        Array<Object> user = new Array<Object>();
        Object plaintext_pass = null;
        int user_id = 0;
        WP_User current_user = null;

        ID = intval(userdata.getValue("ID"));

        // First, get all of the original fields
        StdClass userObj = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(ID);

        // Escape data pulled from DB.
        user = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(ClassHandling.get_object_vars(userObj));

        // If password is changing, hash it now.
        if (!empty(userdata.getValue("user_pass"))) {
            plaintext_pass = userdata.getValue("user_pass");
            userdata.putValue("user_pass", getIncluded(PluggablePage.class, gVars, gConsts).wp_hash_password(strval(userdata.getValue("user_pass"))));
        }

        // Merge old and new fields with new fields overwriting old ones.
        userdata = Array.array_merge(user, userdata);
        user_id = wp_insert_user(userdata);

        // Update the cookies if the password changed.
        current_user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();

        if (equal(current_user.getID(), ID)) {
            if (isset(plaintext_pass)) {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_clear_auth_cookie();
                getIncluded(PluggablePage.class, gVars, gConsts).wp_set_auth_cookie(ID, false);
            }
        }

        return user_id;
    }

    /**
     * wp_create_user() - A simpler way of inserting an user into the database.
     *
     * Creates a new user with just the username, password, and email. For a more
     * detail creation of a user, use wp_insert_user() to specify more infomation.
     *
     * @since 2.0.0
     * @see wp_insert_user() More complete way to create a new user
     * @uses $wpdb Escapes $username and $email parameters
     *
     * @param string $username The user's username.
     * @param string $password The user's password.
     * @param string $email The user's email (optional).
     * @return int The new user's ID.
     */
    public int wp_create_user(String username, String password, String email) {
        String user_login = null;
        String user_email = null;
        Object user_pass = null;
        Array<Object> userdata = new Array<Object>();
        user_login = gVars.wpdb.escape(username);
        user_email = gVars.wpdb.escape(email);
        user_pass = password;
        userdata = Array.compact(new ArrayEntry("user_login", user_login), new ArrayEntry("user_email", user_email), new ArrayEntry("user_pass", user_pass));

        return wp_insert_user(userdata);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
