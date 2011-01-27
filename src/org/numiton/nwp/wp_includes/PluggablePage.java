/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PluggablePage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ListAssigner;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class PluggablePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PluggablePage.class.getName());

    /**
     * Generated in place of local variable 'name' from method 'wp_mail' because
     * it is used inside an inner class.
     */
    String wp_mail_name = null;

    /**
     * Generated in place of local variable 'content' from method 'wp_mail'
     * because it is used inside an inner class.
     */
    String wp_mail_content = null;

    /**
     * Generated in place of local variable 'type' from method 'wp_mail' because
     * it is used inside an inner class.
     */
    String wp_mail_type = null;

    /**
     * Generated in place of local variable 'charset' from method 'wp_mail'
     * because it is used inside an inner class.
     */
    String wp_mail_charset = null;

    /**
     * Generated in place of local variable 'username' from method
     * 'wp_validate_auth_cookie' because it is used inside an inner class.
     */
    String wp_validate_auth_cookie_username = null;

    /**
     * Generated in place of local variable 'expiration' from method
     * 'wp_validate_auth_cookie' because it is used inside an inner class.
     */
    String wp_validate_auth_cookie_expiration = null;

    /**
     * Generated in place of local variable 'hmac' from method
     * 'wp_validate_auth_cookie' because it is used inside an inner class.
     */
    String wp_validate_auth_cookie_hmac = null;
    public PHPMailer phpmailer;
    public PasswordHash wp_hasher;

    @Override
    @RequestMapping("/wp-includes/pluggable.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/pluggable";
    }

    /**
     * set_current_user() - Populates global user information for any user
     * Set $id to null and specify a name if you do not know a user's ID
     * @since 2.0.1
     * @see wp_set_current_user() An alias of wp_set_current_user()
     * @param int|null $id User ID.
     * @param string $name Optional. The user's username
     * @return object returns wp_set_current_user()
     */
    public WP_User set_current_user(int id, String name) {
        return wp_set_current_user(id, name);
    }

    /**
     * wp_set_current_user() - Changes the current user by ID or name
     * Set $id to null and specify a name if you do not know a user's ID
     * Some WordPress functionality is based on the current user and not based
     * on the signed in user. Therefore, it opens the ability to edit and
     * perform actions on users who aren't signed in.
     * @since 2.0.4
     * @global object $current_user The current user object which holds the user
     * data.
     * @uses do_action() Calls 'set_current_user' hook after setting the current
     * user.
     * @param int $id User ID
     * @param string $name User's username
     * @return WP_User Current user User object
     */
    public WP_User wp_set_current_user(int id, String name) {
        if (isset(gVars.current_user) && equal(id, gVars.current_user.getID())) {
            return gVars.current_user;
        }

        gVars.current_user = new WP_User(gVars, gConsts, id, name);
        getIncluded(UserPage.class, gVars, gConsts).setup_userdata(gVars.current_user.getID());
        getIncluded(PluginPage.class, gVars, gConsts).do_action("set_current_user", "");

        return gVars.current_user;
    }

    /**
     * wp_get_current_user() - Retrieve the current user object
     * @since 2.0.4
     * @return WP_User Current user WP_User object
     */
    public WP_User wp_get_current_user() {
        get_currentuserinfo();

        return gVars.current_user;
    }

    /**
     * get_currentuserinfo() - Populate global variables with information about the currently logged in user
     *
     * Will set the current user, if the current user is not set. The current
     * user will be set to the logged in person. If no user is logged in, then
     * it will set the current user to 0, which is invalid and won't have any
     * permissions.
     *
     * @since 0.71
     * @uses $current_user Checks if the current user is set
     * @uses wp_validate_auth_cookie() Retrieves current logged in user.
     *
     * @return bool|null False on XMLRPC Request and invalid auth cookie. Null when current user set
     */
    public boolean get_currentuserinfo() {
        int user;

        if (gConsts.isXMLRPC_REQUESTDefined() && gConsts.getXMLRPC_REQUEST()) {
            return false;
        }

        if (!empty(gVars.current_user)) {
            return false;
        }

        if (!booleanval(user = wp_validate_auth_cookie(""))) {
            wp_set_current_user(0, "");

            return false;
        }

        wp_set_current_user(user, "");

        return false;
    }

    /**
     * get_userdata() - Retrieve user info by user ID
     * @since 0.71
     * @param int $user_id User ID
     * @return bool|object False on failure, User DB row object
     */
    public StdClass get_userdata(int user_id) {
        StdClass user;
        user_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(user_id);

        if (equal(user_id, 0)) {
            return null;
        }

        user = (StdClass) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(user_id, "users");

        if (booleanval(user)) {
            return user;
        }

        if (!booleanval(user = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.users + " WHERE ID = %d LIMIT 1", user_id)))) {
            return null;
        }

        getIncluded(UserPage.class, gVars, gConsts)._fill_user(user);

        return user;
    }

    /**
     * update_user_cache() - Updates a users cache when overridden by a plugin
     * Core function does nothing.
     * @since 1.5
     * @return bool Only returns true
     */
    public boolean update_user_cache() {
        return true;
    }

    /**
     * get_userdatabylogin() - Retrieve user info by login name
     * @since 0.71
     * @param string $user_login User's username
     * @return bool|object False on failure, User DB row object
     */
    public StdClass get_userdatabylogin(String user_login) {
        Object user_id = null;
        StdClass user;
        user_login = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(user_login, false);

        if (empty(user_login)) {
            return null;
        }

        user_id = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(user_login, "userlogins");
        user = null;

        if (!strictEqual(null, user_id)) {
            user = (StdClass) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(user_id, "users");
        }

        if (!strictEqual(null, user)) {
            return user;
        }

        if (!booleanval(user = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.users + " WHERE user_login = %s", user_login)))) {
            return null;
        }

        getIncluded(UserPage.class, gVars, gConsts)._fill_user(user);

        return user;
    }

    /**
     * get_user_by_email() - Retrieve user info by email
     * @since 2.5
     * @param string $email User's email address
     * @return bool|object False on failure, User DB row object
     */
    public StdClass get_user_by_email(String email) {
        Object user_id = null;
        StdClass user;
        user_id = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(email, "useremail");
        user = null;

        if (!strictEqual(null, user_id)) {
            user = (StdClass) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(user_id, "users");
        }

        if (!strictEqual(null, user)) {
            return user;
        }

        if (!booleanval(user = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.users + " WHERE user_email = %s", email)))) {
            return null;
        }

        getIncluded(UserPage.class, gVars, gConsts)._fill_user(user);

        return user;
    }

    /**
     * wp_mail() - Function to send mail, similar to PHP's mail
     *
     * A true return value does not automatically mean that the
     * user received the email successfully. It just only means
     * that the method used was able to process the request
     * without any errors.
     *
     * Using the two 'wp_mail_from' and 'wp_mail_from_name' hooks
     * allow from creating a from address like 'Name <email@address.com>'
     * when both are set. If just 'wp_mail_from' is set, then just
     * the email address will be used with no name.
     *
     * The default content type is 'text/plain' which does not
     * allow using HTML. However, you can set the content type
     * of the email by using the 'wp_mail_content_type' filter.
     *
     * The default charset is based on the charset used on the
     * blog. The charset can be set using the 'wp_mail_charset'
     * filter.
     *
     * @since 1.2.1
     * @uses apply_filters() Calls 'wp_mail' hook on an array of all of the parameters.
     * @uses apply_filters() Calls 'wp_mail_from' hook to get the from email address.
     * @uses apply_filters() Calls 'wp_mail_from_name' hook to get the from address name.
     * @uses apply_filters() Calls 'wp_mail_content_type' hook to get the email content type.
     * @uses apply_filters() Calls 'wp_mail_charset' hook to get the email charset
     * @uses do_action_ref_array() Calls 'phpmailer_init' hook on the reference to
     *                phpmailer object.
     * @uses PHPMailer
     * @
     *
     * @param string $to Email address to send message
     * @param string $subject Email subject
     * @param string $message Message contents
     * @param string|array $headers Optional. Additional headers.
     * @return bool Whether the email contents were sent successfully.
     */
    public boolean wp_mail(String to, String subject, String message, Object headers)/* Do not change type */
     {
        Array<String> tempheaders = new Array<String>();
        String header = null;
        String from_name = null;
        String from_email = null;
        String content_type = null;
        String sitename = null;
        boolean result;

        // Compact the input, apply the filters, and extract them back out
        Array<Object> filteredArray = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "wp_mail",
                Array.compact(new ArrayEntry("to", to), new ArrayEntry("subject", subject), new ArrayEntry("message", message), new ArrayEntry("headers", headers)));
        to = strval(Array.extractVar(filteredArray, "to", to, Array.EXTR_OVERWRITE));
        subject = strval(Array.extractVar(filteredArray, "subject", subject, Array.EXTR_OVERWRITE));
        message = strval(Array.extractVar(filteredArray, "message", message, Array.EXTR_OVERWRITE));
        headers = Array.extractVar(filteredArray, "headers", headers, Array.EXTR_OVERWRITE);

        // (Re)create it, if it's gone missing
        if (!is_object(phpmailer) || !ClassHandling.is_a(phpmailer, "PHPMailer")) {
            phpmailer = new PHPMailer(gVars, gConsts);
        }

        // Headers
        if (empty(headers)) {
            headers = new Array<Object>();
        } else if (!is_array(headers)) {
            // Explode the headers out, so this function can take both
            // string headers and an array of headers.
            tempheaders = Strings.explode("\n", strval(headers));
            headers = new Array<Object>();

            // If it's actually got contents
            if (!empty(tempheaders)) {
                // Iterate through the raw headers
                for (Map.Entry javaEntry515 : tempheaders.entrySet()) {
                    header = strval(javaEntry515.getValue());

                    if (strictEqual(Strings.strpos(header, ":"), BOOLEAN_FALSE)) {
                        continue;
                    }

                    // Explode them out
                    new ListAssigner<String>() {
                            public Array<String> doAssign(Array<String> srcArray) {
                                if (strictEqual(srcArray, null)) {
                                    return null;
                                }

                                wp_mail_name = srcArray.getValue(0);
                                wp_mail_content = srcArray.getValue(1);

                                return srcArray;
                            }
                        }.doAssign(Strings.explode(":", Strings.trim(header), 2));

                    // Cleanup crew
                    wp_mail_name = Strings.trim(wp_mail_name);
                    wp_mail_content = Strings.trim(wp_mail_content);

                    // Mainly for legacy -- process a From: header if it's there
                    if (equal("from", Strings.strtolower(wp_mail_name))) {
                        if (!strictEqual(Strings.strpos(wp_mail_content, "<"), BOOLEAN_FALSE)) {
                            // So... making my life hard again?
                            from_name = Strings.substr(wp_mail_content, 0, Strings.strpos(wp_mail_content, "<") - 1);
                            from_name = Strings.str_replace("\"", "", from_name);
                            from_name = Strings.trim(from_name);

                            from_email = Strings.substr(wp_mail_content, Strings.strpos(wp_mail_content, "<") + 1);
                            from_email = Strings.str_replace(">", "", from_email);
                            from_email = Strings.trim(from_email);
                        } else {
                            from_name = Strings.trim(wp_mail_content);
                        }
                    } else if (equal("content-type", Strings.strtolower(wp_mail_name))) {
                        if (!strictEqual(Strings.strpos(wp_mail_content, ";"), BOOLEAN_FALSE)) {
                            new ListAssigner<String>() {
                                    public Array<String> doAssign(Array<String> srcArray) {
                                        if (strictEqual(srcArray, null)) {
                                            return null;
                                        }

                                        wp_mail_type = srcArray.getValue(0);
                                        wp_mail_charset = srcArray.getValue(1);

                                        return srcArray;
                                    }
                                }.doAssign(Strings.explode(";", wp_mail_content));
                            content_type = Strings.trim(wp_mail_type);
                            wp_mail_charset = Strings.trim(Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("charset="), new ArrayEntry<Object>("\"")), "", wp_mail_charset));
                        } else {
                            content_type = Strings.trim(wp_mail_content);
                        }
                    } else {
                        // Add it to our grand headers array
                        ((Array) headers).putValue(Strings.trim(wp_mail_name), Strings.trim(wp_mail_content));
                    }
                }
            }
        }

        // Empty out the values that may be set
        phpmailer.ClearAddresses();
        phpmailer.ClearAllRecipients();
        phpmailer.ClearAttachments();
        phpmailer.ClearBCCs();
        phpmailer.ClearCCs();
        phpmailer.ClearCustomHeaders();
        phpmailer.ClearReplyTos();

        // From email and name
        // If we don't have a name from the input headers
        if (!isset(from_name)) {
            from_name = "nWordPress";
        }

        // If we don't have an email from the input headers
        if (!isset(from_email)) {
            // Get the site domain and get rid of www.
            sitename = Strings.strtolower(gVars.webEnv.getServerName());

            if (equal(Strings.substr(sitename, 0, 4), "www.")) {
                sitename = Strings.substr(sitename, 4);
            }

            from_email = "wordpress@" + sitename;
        }

        // Set the from name and email
        phpmailer.From = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_mail_from", from_email));
        phpmailer.FromName = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_mail_from_name", from_name));

        // Set destination address
        phpmailer.AddAddress(to);

        // Set mail's subject and body
        phpmailer.Subject = subject;
        phpmailer.Body = message;

        // Set to use PHP's mail()
        phpmailer.IsMail();

        // Set Content-Type and charset
        // If we don't have a content-type from the input headers
        if (!isset(content_type)) {
            content_type = "text/plain";
        }

        content_type = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_mail_content_type", content_type));

        // Set whether it's plaintext or not, depending on $content_type
        if (equal(content_type, "text/html")) {
            phpmailer.IsHTML(true);
        } else {
            phpmailer.IsHTML(false);
        }

        // If we don't have a charset from the input headers
        if (!isset(wp_mail_charset)) {
            wp_mail_charset = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("charset", "raw");
        }

        // Set the content-type and charset
        phpmailer.CharSet = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_mail_charset", wp_mail_charset));

        // Set custom headers
        if (!empty(headers)) {
            for (Map.Entry javaEntry516 : ((Array<?>) headers).entrySet()) {
                wp_mail_name = strval(javaEntry516.getKey());
                wp_mail_content = strval(javaEntry516.getValue());
                phpmailer.AddCustomHeader(QStrings.sprintf("%1$s: %2$s", wp_mail_name, wp_mail_content));
            }
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("phpmailer_init", new Array<Object>(new ArrayEntry<Object>(phpmailer)));

        // Send!
        result = phpmailer.Send();

        return result;
    }

    /**
     * wp_authenticate() - Checks a user's login information and logs them in if it checks out
     * @since 2.5
     *
     * @param string $username User's username
     * @param string $password User's password
     * @return WP_Error|WP_User WP_User object if login successful, otherwise WP_Error object.
     */
    public Object wp_authenticate(String username, String password) {
        StdClass user;
        username = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(username, false);

        if (equal("", username)) {
            return new WP_Error(gVars, gConsts, "empty_username", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: The username field is empty.", "default"));
        }

        if (equal("", password)) {
            return new WP_Error(gVars, gConsts, "empty_password", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: The password field is empty.", "default"));
        }

        user = get_userdatabylogin(username);

        if (!booleanval(user) || !equal(StdClass.getValue(user, "user_login"), username)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_login_failed", username);

            return new WP_Error(gVars, gConsts, "invalid_username", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Invalid username.", "default"));
        }

        user = (StdClass) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_authenticate_user", user, password);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(user)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_login_failed", username);

            return user;
        }

        if (!wp_check_password(password, strval(StdClass.getValue(user, "user_pass")), intval(StdClass.getValue(user, "ID")))) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_login_failed", username);

            return new WP_Error(gVars, gConsts, "incorrect_password", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: Incorrect password.", "default"));
        }

        return new WP_User(gVars, gConsts, StdClass.getValue(user, "ID"));
    }

    /**
     * wp_logout() - Log the current user out
     * @since 2.5
     *
     */
    public void wp_logout() {
        wp_clear_auth_cookie();
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_logout", "");
    }

    /**
     * wp_validate_auth_cookie() - Validates authentication cookie
     *
     * The checks include making sure that the authentication cookie
     * is set and pulling in the contents (if $cookie is not used).
     *
     * Makes sure the cookie is not expired. Verifies the hash in
     * cookie is what is should be and compares the two.
     *
     * @since 2.5
     *
     * @param string $cookie Optional. If used, will validate contents instead of cookie's
     * @return bool|int False if invalid cookie, User ID if valid.
     */
    public int wp_validate_auth_cookie(String cookie) {
        Array<String> cookie_elements = new Array<String>();
        int expired = 0;
        String key = null;
        String hash = null;
        StdClass user;

        if (empty(cookie)) {
            if (empty(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE()))) {
                return intval(false);
            }

            cookie = strval(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE()));
        }

        cookie_elements = Strings.explode("|", cookie);

        if (!equal(Array.count(cookie_elements), 3)) {
            return intval(false);
        }

        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    wp_validate_auth_cookie_username = srcArray.getValue(0);
                    wp_validate_auth_cookie_expiration = srcArray.getValue(1);
                    wp_validate_auth_cookie_hmac = srcArray.getValue(2);

                    return srcArray;
                }
            }.doAssign(cookie_elements);

        expired = intval(wp_validate_auth_cookie_expiration);

        // Allow a grace period for POST and AJAX requests
        if (gConsts.isDOING_AJAXDefined() || equal("POST", gVars.webEnv.getRequestMethod())) {
            expired = expired + 3600;
        }

    	// Quick check to see if an honest cookie has expired
        if (expired < DateTime.time()) {
            return intval(false);
        }

        key = wp_hash(wp_validate_auth_cookie_username + "|" + wp_validate_auth_cookie_expiration);
        hash = CompatPage.hash_hmac("md5", wp_validate_auth_cookie_username + "|" + wp_validate_auth_cookie_expiration, key);

        if (!equal(wp_validate_auth_cookie_hmac, hash)) {
            return intval(false);
        }

        user = get_userdatabylogin(wp_validate_auth_cookie_username);

        if (!booleanval(user)) {
            return intval(false);
        }

        return intval(StdClass.getValue(user, "ID"));
    }

    /**
     * wp_generate_auth_cookie() - Generate authentication cookie contents
     * @since 2.5
     * @uses apply_filters() Calls 'auth_cookie' hook on $cookie contents, User
     * ID and expiration of cookie.
     * @param int $user_id User ID
     * @param int $expiration Cookie expiration in seconds
     * @return string Authentication cookie contents
     */
    public String wp_generate_auth_cookie(int user_id, int expiration) {
        StdClass user;
        String key = null;
        String hash = null;
        String cookie = null;
        user = get_userdata(user_id);
        key = wp_hash(StdClass.getValue(user, "user_login") + "|" + strval(expiration));
        hash = CompatPage.hash_hmac("md5", StdClass.getValue(user, "user_login") + "|" + strval(expiration), key);
        cookie = StdClass.getValue(user, "user_login") + "|" + strval(expiration) + "|" + hash;

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("auth_cookie", cookie, user_id, expiration));
    }

    /**
     * wp_set_auth_cookie() - Sets the authentication cookies based User ID
     * The $remember parameter increases the time that the cookie will be kept.
     * The default the cookie is kept without remembering is two days. When
     * $remember is set, the cookies will be kept for 14 days or two weeks.
     * @since 2.5
     * @param int $user_id User ID
     * @param bool $remember Whether to remember the user or not
     */
    public void wp_set_auth_cookie(int user_id, Object remember) {
        int expiration = 0;
        int expire = 0;
        String cookie = null;

        if (booleanval(remember)) {
            expiration = expire = DateTime.time() + 1209600;
        } else {
            expiration = DateTime.time() + 172800;
            expire = 0;
        }

        cookie = wp_generate_auth_cookie(user_id, expiration);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("set_auth_cookie", cookie, expire);
        Network.setcookie(gVars.webEnv, gConsts.getAUTH_COOKIE(), cookie, expire, gConsts.getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());

        if (!equal(gConsts.getCOOKIEPATH(), gConsts.getSITECOOKIEPATH())) {
            Network.setcookie(gVars.webEnv, gConsts.getAUTH_COOKIE(), cookie, expire, gConsts.getSITECOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
        }
    }

    /**
     * wp_clear_auth_cookie() - Deletes all of the cookies associated with
     * authentication
     * @since 2.5
     */
    public void wp_clear_auth_cookie() {
        Network.setcookie(gVars.webEnv, gConsts.getAUTH_COOKIE(), " ", DateTime.time() - 31536000, gConsts.getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
        Network.setcookie(gVars.webEnv, gConsts.getAUTH_COOKIE(), " ", DateTime.time() - 31536000, gConsts.getSITECOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
        
    	// Old cookies
        Network.setcookie(gVars.webEnv, gConsts.getUSER_COOKIE(), " ", DateTime.time() - 31536000, gConsts.getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
        Network.setcookie(gVars.webEnv, gConsts.getPASS_COOKIE(), " ", DateTime.time() - 31536000, gConsts.getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
        Network.setcookie(gVars.webEnv, gConsts.getUSER_COOKIE(), " ", DateTime.time() - 31536000, gConsts.getSITECOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
        Network.setcookie(gVars.webEnv, gConsts.getPASS_COOKIE(), " ", DateTime.time() - 31536000, gConsts.getSITECOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
    }

    /**
     * is_user_logged_in() - Checks if the current visitor is a logged in user
     * @since 2.0.0
     * @return bool True if user is logged in, false if not logged in.
     */
    public boolean is_user_logged_in() {
        WP_User user = null;
        user = wp_get_current_user();

        if (equal(user.getID(), 0)) {
            return false;
        }

        return true;
    }

    /**
     * auth_redirect() - Checks if a user is logged in, if not it redirects
     * them to the login page
     * @since 1.5
     */
    public void auth_redirect() {
    	// Checks if a user is logged in, if not redirects them to the login page
        if ((!empty(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE())) && !booleanval(wp_validate_auth_cookie(strval(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE()))))) ||
                empty(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE()))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
            
            wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php?redirect_to=" + URL.urlencode(gVars.webEnv.getRequestURI()), 302);
            System.exit();
        }
    }

    public int check_admin_referer() {
        return check_admin_referer("-1", "_wpnonce");
    }

    public int check_admin_referer(String action) {
        return check_admin_referer(action, "_wpnonce");
    }

    /**
     * check_admin_referer() - Makes sure that a user was referred from
     * another admin page, to avoid security exploits
     * @since 1.2.0
     * @uses do_action() Calls 'check_admin_referer' on $action.
     * @param string $action Action nonce
     * @param string $query_arg where to look for nonce in $_REQUEST (since 2.5)
     */
    public int check_admin_referer(String action, String query_arg) {
        String adminurl = null;
        String referer = null;
        int result = 0;
        adminurl = Strings.strtolower(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"))) + "/wp-admin";
        referer = Strings.strtolower(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());
        result = wp_verify_nonce(gVars.webEnv._REQUEST.getValue(query_arg), action);

        if (!booleanval(result) && !(equal(-1, action) && !strictEqual(Strings.strpos(referer, adminurl), BOOLEAN_FALSE))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_ays(action);
            System.exit();
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("check_admin_referer", action, result);

        return result;
    }

    /**
     * check_ajax_referer() - Verifies the AJAX request to prevent processing
     * requests external of the blog.
     * @since 2.0.4
     * @param string $action Action nonce
     * @param string $query_arg where to look for nonce in $_REQUEST (since 2.5)
     */
    public int check_ajax_referer(Object action, String query_arg, boolean die) {
        Object nonce = null;
        int result = 0;

        if (booleanval(query_arg)) {
            nonce = gVars.webEnv._REQUEST.getValue(query_arg);
        } else {
            nonce = (booleanval(gVars.webEnv._REQUEST.getValue("_ajax_nonce"))
                ? gVars.webEnv._REQUEST.getValue("_ajax_nonce")
                : gVars.webEnv._REQUEST.getValue("_wpnonce"));
        }

        result = wp_verify_nonce(nonce, action);

        if (die && equal(false, result)) {
            System.exit("-1");
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("check_ajax_referer", action, result);

        return result;
    }

    public boolean wp_redirect(String location) {
        return wp_redirect(location, 302);
    }

    /**
     * wp_redirect() - Redirects to another page, with a workaround for the
     * IIS Set-Cookie bug
     * @link http://support.microsoft.com/kb/q176113
     * @since 1.5.1
     * @uses apply_filters() Calls 'wp_redirect' hook on $location and $status.
     * @param string $location The path to redirect to
     * @param int $status Status code to use
     * @return bool False if $location is not set
     */
    public boolean wp_redirect(String location, int status) {
        location = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_redirect", location, status));
        status = intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_redirect_status", status, location));

        if (!booleanval(location)) { // allows the wp_redirect filter to cancel a redirect
            return false;
        }

        location = wp_sanitize_redirect(location);

        if (gVars.is_IIS) {
            Network.header(gVars.webEnv, "Refresh: 0;url=" + location);
        } else {
            if (true)/*Modified by Numiton*/
             {
                getIncluded(FunctionsPage.class, gVars, gConsts).status_header(status); // This causes problems on IIS and some FastCGI setups
            }

            Network.header(gVars.webEnv, "Location: " + location);
        }

        return false;
    }

    /**
     * wp_sanitize_redirect() - Sanitizes a URL for use in a redirect
     * @since 2.3
     * @return string redirect-sanitized URL
     */
    public String wp_sanitize_redirect(String location) {
        Array<Object> strip = new Array<Object>();
        boolean found = false;
        String val = null;
        
        location = QRegExPerl.preg_replace("|[^a-z0-9-~+_.?#=&;,/:%]|i", "", location);
        location = getIncluded(KsesPage.class, gVars, gConsts).wp_kses_no_null(location);
        
    	// remove %0d and %0a from location
        strip = new Array<Object>(new ArrayEntry<Object>("%0d"), new ArrayEntry<Object>("%0a"));
        found = true;

        while (found) {
            found = false;

            for (Map.Entry javaEntry517 : strip.entrySet()) {
                val = strval(javaEntry517.getValue());

                while (!strictEqual(Strings.strpos(location, val), BOOLEAN_FALSE)) {
                    found = true;
                    location = Strings.str_replace(val, "", location);
                }
            }
        }

        return location;
    }

    /**
     * wp_safe_redirect() - Performs a safe (local) redirect, using
     * wp_redirect()
     * Checks whether the $location is using an allowed host, if it has an
     * absolute path. A plugin can therefore set or remove allowed host(s) to or
     * from the list.
     * If the host is not allowed, then the redirect is to wp-admin on the
     * siteurl instead. This prevents malicious redirects which redirect to
     * another host, but only used in a few places.
     * @since 2.3
     * @uses apply_filters() Calls 'allowed_redirect_hosts' on an array
     * containing WordPress host string and $location host string.
     * @return void Does not return anything
     */
    public void wp_safe_redirect(String location, int status) {
        Array<String> lp = new Array<String>();
        Array<String> wpp = new Array<String>();
        Array<Object> allowed_hosts = new Array<Object>();
        
    	// Need to look at the URL the way it will end up in wp_redirect()
        location = wp_sanitize_redirect(location);

    	// Need to look at the URL the way it will end up in wp_redirect()
        if (equal(Strings.substr(location, 0, 2), "//")) {
            location = "http:" + location;
        }

        lp = URL.parse_url(location);
        wpp = URL.parse_url(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));
        allowed_hosts = new Array<Object>(
                getIncluded(PluginPage.class, gVars, gConsts).apply_filters("allowed_redirect_hosts", new Array<Object>(new ArrayEntry<Object>(wpp.getValue("host"))),
                    isset(lp.getValue("host"))
                    ? lp.getValue("host")
                    : ""));

        if (isset(lp.getValue("host")) && !Array.in_array(lp.getValue("host"), allowed_hosts) && !equal(lp.getValue("host"), Strings.strtolower(wpp.getValue("host")))) {
            location = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/";
        }

        wp_redirect(location, status);
    }

    /**
     * wp_notify_postauthor() - Notify an author of a
     * comment/trackback/pingback to one of their posts
     * @since 1.0.0
     * @param int $comment_id Comment ID
     * @param string $comment_type Optional. The comment type either 'comment'
     * (default), 'trackback', or 'pingback'
     * @return bool False if user email does not exist. True on completion.
     */
    public boolean wp_notify_postauthor(int comment_id, String comment_type) {
        StdClass comment = null;
        StdClass post = null;
        StdClass user;
        String comment_author_domain = null;
        Object blogname = null;
        String notify_message = null;
        String subject = null;
        String wp_email = null;
        Object from = null;
        String reply_to = null;
        Object message_headers = null;
        
        comment = (StdClass) getIncluded(CommentPage.class, gVars, gConsts).get_comment(comment_id, gConsts.getOBJECT());
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(StdClass.getValue(comment, "comment_post_ID"), gConsts.getOBJECT(), "raw");
        user = get_userdata(intval(StdClass.getValue(post, "post_author")));

        if (equal("", StdClass.getValue(user, "user_email"))) {
            return false; // If there's no email to send the comment to
        }

        comment_author_domain = Network.gethostbyaddr(strval(StdClass.getValue(comment, "comment_author_IP")));
        blogname = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname");

        if (empty(comment_type)) {
            comment_type = "comment";
        }

        if (equal("comment", comment_type)) {
            notify_message = QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("New comment on your post #%1$s \"%2$s\"", "default"),
                    StdClass.getValue(comment, "comment_post_ID"),
                    StdClass.getValue(post, "post_title")) + "\r\n";
            notify_message = notify_message +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Author : %1$s (IP: %2$s , %3$s)", "default"), StdClass.getValue(comment, "comment_author"),
                    StdClass.getValue(comment, "comment_author_IP"), comment_author_domain) + "\r\n";
            notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("E-mail : %s", "default"), StdClass.getValue(comment, "comment_author_email")) + "\r\n";
            notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("URL    : %s", "default"), StdClass.getValue(comment, "comment_author_url")) + "\r\n";
            notify_message = notify_message +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Whois  : http://ws.arin.net/cgi-bin/whois.pl?queryinput=%s", "default"),
                    StdClass.getValue(comment, "comment_author_IP")) + "\r\n";
            notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("Comment: ", "default") + "\r\n" + StdClass.getValue(comment, "comment_content") + "\r\n\r\n";
            notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("You can see all comments on this post here: ", "default") + "\r\n";
            subject = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%1$s] Comment: \"%2$s\"", "default"), blogname, StdClass.getValue(post, "post_title"));
        } else if (equal("trackback", comment_type)) {
            notify_message = QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("New trackback on your post #%1$s \"%2$s\"", "default"),
                    StdClass.getValue(comment, "comment_post_ID"),
                    StdClass.getValue(post, "post_title")) + "\r\n";
            notify_message = notify_message +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Website: %1$s (IP: %2$s , %3$s)", "default"), StdClass.getValue(comment, "comment_author"),
                    StdClass.getValue(comment, "comment_author_IP"), comment_author_domain) + "\r\n";
            notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("URL    : %s", "default"), StdClass.getValue(comment, "comment_author_url")) + "\r\n";
            notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("Excerpt: ", "default") + "\r\n" + StdClass.getValue(comment, "comment_content") + "\r\n\r\n";
            notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("You can see all trackbacks on this post here: ", "default") + "\r\n";
            subject = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%1$s] Trackback: \"%2$s\"", "default"), blogname, StdClass.getValue(post, "post_title"));
        } else if (equal("pingback", comment_type)) {
            notify_message = QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("New pingback on your post #%1$s \"%2$s\"", "default"),
                    StdClass.getValue(comment, "comment_post_ID"),
                    StdClass.getValue(post, "post_title")) + "\r\n";
            notify_message = notify_message +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Website: %1$s (IP: %2$s , %3$s)", "default"), StdClass.getValue(comment, "comment_author"),
                    StdClass.getValue(comment, "comment_author_IP"), comment_author_domain) + "\r\n";
            notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("URL    : %s", "default"), StdClass.getValue(comment, "comment_author_url")) + "\r\n";
            notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("Excerpt: ", "default") + "\r\n" +
                QStrings.sprintf("[...] %s [...]", StdClass.getValue(comment, "comment_content")) + "\r\n\r\n";
            notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("You can see all pingbacks on this post here: ", "default") + "\r\n";
            subject = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%1$s] Pingback: \"%2$s\"", "default"), blogname, StdClass.getValue(post, "post_title"));
        }

        notify_message = notify_message + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(comment, "comment_post_ID"), false) + "#comments\r\n\r\n";
        notify_message = notify_message +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Delete it: %s", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/comment.php?action=cdc&c=" + strval(comment_id)) + "\r\n";
        notify_message = notify_message +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Spam it: %s", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/comment.php?action=cdc&dt=spam&c=" + strval(comment_id)) + "\r\n";
        wp_email = "wordpress@" + QRegExPerl.preg_replace("#^www\\.#", "", Strings.strtolower(gVars.webEnv.getServerName()));

        if (equal("", StdClass.getValue(comment, "comment_author"))) {
            from = "From: \"" + strval(blogname) + "\" <" + wp_email + ">";

            if (!equal("", StdClass.getValue(comment, "comment_author_email"))) {
                reply_to = "Reply-To: " + StdClass.getValue(comment, "comment_author_email");
            }
        } else {
            from = "From: \"" + StdClass.getValue(comment, "comment_author") + "\" <" + wp_email + ">";

            if (!equal("", StdClass.getValue(comment, "comment_author_email"))) {
                reply_to = "Reply-To: \"" + StdClass.getValue(comment, "comment_author_email") + "\" <" + StdClass.getValue(comment, "comment_author_email") + ">";
            }
        }

        message_headers = strval(from) + "\n" + "Content-Type: text/plain; charset=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\"\n";

        if (isset(reply_to)) {
            message_headers = strval(message_headers) + reply_to + "\n";
        }

        notify_message = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_notification_text", notify_message, comment_id));
        subject = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_notification_subject", subject, comment_id));
        message_headers = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_notification_headers", message_headers, comment_id);
        wp_mail(strval(StdClass.getValue(user, "user_email")), subject, notify_message, message_headers);

        return true;
    }

    /**
     * wp_notify_moderator() - Notifies the moderator of the blog about a new
     * comment that is awaiting approval
     * @since 1.0
     * @uses $wpdb
     * @param int $comment_id Comment ID
     * @return bool Always returns true
     */
    public boolean wp_notify_moderator(int comment_id) {
        StdClass comment;
        StdClass post;
        String comment_author_domain = null;
        int comments_waiting;
        String notify_message = null;
        String strCommentsPending = null;
        String subject = null;
        String admin_email = null;

        if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("moderation_notify"), 0)) {
            return true;
        }

        comment = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_ID=%d LIMIT 1", comment_id));
        post = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.posts + " WHERE ID=%d LIMIT 1", StdClass.getValue(comment, "comment_post_ID")));
        comment_author_domain = Network.gethostbyaddr(strval(StdClass.getValue(comment, "comment_author_IP")));
        comments_waiting = intval(gVars.wpdb.get_var("SELECT count(comment_ID) FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'0\'"));

        {
            int javaSwitchSelector85 = 0;

            if (equal(StdClass.getValue(comment, "comment_type"), "trackback")) {
                javaSwitchSelector85 = 1;
            }

            if (equal(StdClass.getValue(comment, "comment_type"), "pingback")) {
                javaSwitchSelector85 = 2;
            }

            switch (javaSwitchSelector85) {
            case 1: {
                notify_message = QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("A new trackback on the post #%1$s \"%2$s\" is waiting for your approval", "default"),
                        StdClass.getValue(post, "ID"),
                        StdClass.getValue(post, "post_title")) + "\r\n";
                notify_message = notify_message + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(comment, "comment_post_ID"), false) + "\r\n\r\n";
                notify_message = notify_message +
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Website : %1$s (IP: %2$s , %3$s)", "default"),
                        StdClass.getValue(comment, "comment_author"),
                        StdClass.getValue(comment, "comment_author_IP"),
                        comment_author_domain) + "\r\n";
                notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("URL    : %s", "default"), StdClass.getValue(comment, "comment_author_url")) +
                    "\r\n";
                notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("Trackback excerpt: ", "default") + "\r\n" + StdClass.getValue(comment, "comment_content") +
                    "\r\n\r\n";

                break;
            }

            case 2: {
                notify_message = QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("A new pingback on the post #%1$s \"%2$s\" is waiting for your approval", "default"),
                        StdClass.getValue(post, "ID"),
                        StdClass.getValue(post, "post_title")) + "\r\n";
                notify_message = notify_message + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(comment, "comment_post_ID"), false) + "\r\n\r\n";
                notify_message = notify_message +
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Website : %1$s (IP: %2$s , %3$s)", "default"),
                        StdClass.getValue(comment, "comment_author"),
                        StdClass.getValue(comment, "comment_author_IP"),
                        comment_author_domain) + "\r\n";
                notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("URL    : %s", "default"), StdClass.getValue(comment, "comment_author_url")) +
                    "\r\n";
                notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("Pingback excerpt: ", "default") + "\r\n" + StdClass.getValue(comment, "comment_content") +
                    "\r\n\r\n";

                break;
            }

            default: { //Comments
                notify_message = QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("A new comment on the post #%1$s \"%2$s\" is waiting for your approval", "default"),
                        StdClass.getValue(post, "ID"),
                        StdClass.getValue(post, "post_title")) + "\r\n";
                notify_message = notify_message + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(comment, "comment_post_ID"), false) + "\r\n\r\n";
                notify_message = notify_message +
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Author : %1$s (IP: %2$s , %3$s)", "default"), StdClass.getValue(comment, "comment_author"),
                        StdClass.getValue(comment, "comment_author_IP"), comment_author_domain) + "\r\n";
                notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("E-mail : %s", "default"), StdClass.getValue(comment, "comment_author_email")) +
                    "\r\n";
                notify_message = notify_message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("URL    : %s", "default"), StdClass.getValue(comment, "comment_author_url")) +
                    "\r\n";
                notify_message = notify_message +
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Whois  : http://ws.arin.net/cgi-bin/whois.pl?queryinput=%s", "default"),
                        StdClass.getValue(comment, "comment_author_IP")) + "\r\n";
                notify_message = notify_message + getIncluded(L10nPage.class, gVars, gConsts).__("Comment: ", "default") + "\r\n" + StdClass.getValue(comment, "comment_content") + "\r\n\r\n";

                break;
            }
            }
        }

        notify_message = notify_message +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Approve it: %s", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/comment.php?action=mac&c=" + strval(comment_id)) + "\r\n";
        notify_message = notify_message +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Delete it: %s", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/comment.php?action=cdc&c=" + strval(comment_id)) + "\r\n";
        notify_message = notify_message +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Spam it: %s", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/comment.php?action=cdc&dt=spam&c=" + strval(comment_id)) + "\r\n";
        strCommentsPending = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s comment", "%s comments", comments_waiting, "default"), comments_waiting);
        notify_message = notify_message +
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Currently %s are waiting for approval. Please visit the moderation panel:", "default"), strCommentsPending) + "\r\n";
        notify_message = notify_message + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/edit-comments.php?comment_status=moderated\r\n";
        subject = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%1$s] Please moderate: \"%2$s\"", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname"), StdClass.getValue(post, "post_title"));
        admin_email = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("admin_email"));
        notify_message = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_moderation_text", notify_message, comment_id));
        subject = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_moderation_subject", subject, comment_id));
        wp_mail(admin_email, subject, notify_message, "");

        return true;
    }

    /**
     * wp_new_user_notification() - Notify the blog admin of a new user,
     * normally via email
     * @since 2.0
     * @param int $user_id User ID
     * @param string $plaintext_pass Optional. The user's plaintext password
     */
    public void wp_new_user_notification(Object user_id, String plaintext_pass) {
        WP_User user = null;
        String user_login = null;
        String user_email = null;
        String message = null;
        user = new WP_User(gVars, gConsts, user_id);
        user_login = Strings.stripslashes(gVars.webEnv, user.getUser_login());
        user_email = Strings.stripslashes(gVars.webEnv, user.getUser_email());
        message = QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("New user registration on your blog %s:", "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")) + "\r\n\r\n";
        message = message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Username: %s", "default"), user_login) + "\r\n\r\n";
        message = message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("E-mail: %s", "default"), user_email) + "\r\n";
        wp_mail(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("admin_email")),
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%s] New User Registration", "default"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")),
            message, "");

        if (empty(plaintext_pass)) {
            return;
        }

        message = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Username: %s", "default"), user_login) + "\r\n";
        message = message + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Password: %s", "default"), plaintext_pass) + "\r\n";
        message = message + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php\r\n";
        wp_mail(user_email,
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("[%s] Your username and password", "default"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")),
            message, "");
    }

    /**
     * wp_nonce_tick() - Get the time-dependent variable for nonce creation
     * A nonce has a lifespan of two ticks. Nonces in their second tick may be
     * updated, e.g. by autosave.
     * @since 2.5
     * @return int
     */
    public int wp_nonce_tick() {
        Object nonce_life = null;
        nonce_life = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("nonce_life", 86400);

        return Math.ceil(floatval(DateTime.time()) / floatval(nonce_life) / floatval(2));
    }

    /**
     * wp_verify_nonce() - Verify that correct nonce was used with time limit
     * The user is given an amount of time to use the token, so therefore, since
     * the UID and $action remain the same, the independent variable is the
     * time.
     * @since 2.0.4
     * @param string $nonce Nonce that was used in the form to verify
     * @param string|int $action Should give context to what is taking place and
     * be the same when nonce was created.
     * @return bool Whether the nonce check passed or failed.
     */
    public int wp_verify_nonce(Object nonce, Object action) {
        WP_User user = null;
        int uid = 0;
        int i = 0;
        user = wp_get_current_user();
        uid = user.getID();
        i = wp_nonce_tick();

    	// Nonce generated 0-12 hours ago
        if (equal(Strings.substr(wp_hash(strval(i) + strval(action) + strval(uid)), -12, 10), nonce)) {
            return 1;
        }

        // Nonce generated 12-24 hours ago
        if (equal(Strings.substr(wp_hash(strval(i - 1) + strval(action) + strval(uid)), -12, 10), nonce)) {
            return 2;
        }

        // Invalid nonce
        //return intval(false);
        //FIXME Temporary
        return intval(true);
    }

    /**
     * wp_create_nonce() - Creates a random, one time use token
     * @since 2.0.4
     * @param string|int $action Scalar value to add context to the nonce.
     * @return string The one use form token
     */
    public String wp_create_nonce(Object action) {
        WP_User user = null;
        int uid = 0;
        float i = 0;
        user = wp_get_current_user();
        uid = user.getID();
        i = wp_nonce_tick();

        return Strings.substr(wp_hash(strval(i) + strval(action) + strval(uid)), -12, 10);
    }

    /**
     * wp_salt() - Get salt to add to hashes to help prevent attacks
     * You can set the salt by defining two areas. One is in the database and
     * the other is in your wp-config.php file. The database location is defined
     * in the option named 'secret', but most likely will not need to be
     * changed.
     * The second, located in wp-config.php, is a constant named 'SECRET_KEY',
     * but is not required. If the constant is not defined then the database
     * constants will be used, since they are most likely given to be unique.
     * However, given that the salt will be added to the password and can be
     * seen, the constant is recommended to be set manually.
     * <code>
     * define('SECRET_KEY', 'mAry1HadA15|\/|b17w55w1t3asSn09w');
     * </code>
     * Attention: Do not use above example!
     * Salting passwords helps against tools which has stored hashed values of
     * common dictionary strings. The added values makes it harder to crack if
     * given salt string is not weak.
     * Salting only helps if the string is not predictable and should be made up
     * of various characters. Think of the salt as a password for securing your
     * passwords, but common among all of your passwords. Therefore the salt
     * should be as long as possible as as difficult as possible, because you
     * will not have to remember it.
     * @since 2.5
     * @return string Salt value from either 'SECRET_KEY' or 'secret' option
     */
    public String wp_salt() {
        String secret_key = null;
        String salt = null;
        secret_key = "";

        if (gConsts.isSECRET_KEYDefined() && !equal("", gConsts.getSECRET_KEY()) && !equal(gVars.wp_default_secret_key, gConsts.getSECRET_KEY())) {
            secret_key = gConsts.getSECRET_KEY();
        }

        if (gConsts.isSECRET_SALTDefined()) {
            salt = gConsts.getSECRET_SALT();
        } else {
            salt = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("secret"));

            if (empty(salt)) {
                salt = wp_generate_password(12);
                getIncluded(FunctionsPage.class, gVars, gConsts).update_option("secret", salt);
            }
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("salt", secret_key + salt));
    }

    /**
     * wp_hash() - Get hash of given string
     * @since 2.0.4
     * @uses wp_salt() Get WordPress salt
     * @param string $data Plain text to hash
     * @return string Hash of $data
     */
    public String wp_hash(String data) {
        String salt = null;
        salt = wp_salt();

        return CompatPage.hash_hmac("md5", data, salt);
    }

    /**
     * wp_hash_password() - Create a hash (encrypt) of a plain text password
     * For integration with other applications, this function can be overwritten
     * to instead use the other package password checking algorithm.
     * @since 2.5
     * @global object $wp_hasher PHPass object
     * @uses PasswordHash::HashPassword
     * @param string $password Plain text user password to hash
     * @return string The hash string of the password
     */
    public String wp_hash_password(String password) {
        if (empty(wp_hasher)) {
    		// By default, use the portable hash from phpass
            wp_hasher = new PasswordHash(gVars, gConsts, 8, true);
        }

        return wp_hasher.HashPassword(password);
    }

    /**
     * wp_check_password() - Checks the plaintext password against the encrypted Password
     *
     * Maintains compatibility between old version and the new cookie
     * authentication protocol using PHPass library. The $hash parameter
     * is the encrypted password and the function compares the plain text
     * password when encypted similarly against the already encrypted
     * password to see if they match.
     *
     * For integration with other applications, this function can be
     * overwritten to instead use the other package password checking
     * algorithm.
     *
     * @since 2.5
     * @global object $wp_hasher PHPass object used for checking the password
     *	against the $hash + $password
     * @uses PasswordHash::CheckPassword
     *
     * @param string $password Plaintext user's password
     * @param string $hash Hash of the user's password to check against.
     * @return bool False, if the $password does not match the hashed password
     */
    public boolean wp_check_password(String password, String hash, int user_id) {
        boolean check = false;

    	// If the hash is still md5...
        if (Strings.strlen(hash) <= 32) {
            check = equal(hash, Strings.md5(password));

            if (check && booleanval(user_id)) {
            	// Rehash using new hash.
                wp_set_password(password, user_id);
                hash = wp_hash_password(password);
            }

            return booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("check_password", check, password, hash, user_id));
        }

        // If the stored hash is longer than an MD5, presume the
    	// new style phpass portable hash.
        if (empty(wp_hasher)) {
        	// By default, use the portable hash from phpass
            wp_hasher = new PasswordHash(gVars, gConsts, 8, true);
        }

        check = wp_hasher.CheckPassword(password, hash);

        return booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("check_password", check, password, hash, user_id));
    }

    public String wp_generate_password() {
        return wp_generate_password(12);
    }

    /**
     * wp_generate_password() - Generates a random password drawn from the
     * defined set of characters
     * @since 2.5
     * @return string The random password
     */
    public String wp_generate_password(int length) {
        String chars = null;
        String password = null;
        int i = 0;
        chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; /* Modified by Numiton: fix for WordPress bug http://trac.wordpress.org/ticket/6842 */
                                                                                  //!@#$%^&*()";
        password = "";

        for (i = 0; i < length; i++)
            password = password + Strings.substr(chars, Math.mt_rand(0, Strings.strlen(chars) - 1), 1);

        return password;
    }

    /**
     * wp_set_password() - Updates the user's password with a new encrypted
     * one
     * For integration with other applications, this function can be overwritten
     * to instead use the other package password checking algorithm.
     * @since 2.5
     * @uses $wpdb WordPress database object for queries
     * @uses wp_hash_password() Used to encrypt the user's password before
     * passing to the database
     * @param string $password The plaintext new user password
     * @param int $user_id User ID
     */
    public void wp_set_password(String password, int user_id) {
        String hash = null;
        String query = null;
        hash = wp_hash_password(password);
        query = gVars.wpdb.prepare("UPDATE " + gVars.wpdb.users + " SET user_pass = %s, user_activation_key = \'\' WHERE ID = %d", hash, user_id);
        gVars.wpdb.query(query);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user_id, "users");
    }

    public String get_avatar(Object id_or_email) {
        return get_avatar(id_or_email, "96", "");
    }

    public String get_avatar(Object id_or_email, Object size) {
        return get_avatar(id_or_email, size, "");
    }

    /**
     * get_avatar() - Get avatar for a user
     * Retrieve the avatar for a user provided a user ID or email address
     * @since 2.5
     * @param int|string|object $id_or_email A user ID, email address, or
     * comment object
     * @param int $size Size of the avatar image
     * @param string $default URL to a default image to use if no avatar is
     * available
     * @return string <img> tag for the user's avatar
     */
    public String get_avatar(Object id_or_email, Object /* Do not change type */ size, String _default) {
        String email = null;
        int id = 0;
        StdClass user;
        Object out = null;
        Object rating = null;
        Object avatar = null;

        if (!booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_avatars"))) {
            return "";
        }

        if (!is_numeric(size)) {
            size = "96";
        }

        email = "";

        if (is_numeric(id_or_email)) {
            id = intval(id_or_email);
            user = get_userdata(id);

            if (booleanval(user)) {
                email = strval(StdClass.getValue(user, "user_email"));
            }
        } else if (is_object(id_or_email)) {
            if (!empty(((StdClass) id_or_email).fields.getValue("user_id"))) {
                id = intval(((StdClass) id_or_email).fields.getValue("user_id"));
                user = get_userdata(id);

                if (booleanval(user)) {
                    email = strval(StdClass.getValue(user, "user_email"));
                }
            } else if (!empty(((StdClass) id_or_email).fields.getValue("comment_author_email"))) {
                email = strval(((StdClass) id_or_email).fields.getValue("comment_author_email"));
            }
        } else {
            email = strval(id_or_email);
        }

        if (empty(_default)) {
            _default = "http://www.gravatar.com/avatar/ad516503a11cd5ca435acc9bb6523536?s=" + size; // ad516503a11cd5ca435acc9bb6523536 == md5('unknown@gravatar.com')
        }

        if (!empty(email)) {
            out = "http://www.gravatar.com/avatar/";
            out = strval(out) + Strings.md5(Strings.strtolower(email));
            out = strval(out) + "?s=" + size;
            out = strval(out) + "&amp;d=" + URL.urlencode(_default);
            rating = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("avatar_rating");

            if (!empty(rating)) {
                out = strval(out) + "&amp;r=" + strval(rating);
            }

            avatar = "<img alt=\'\' src=\'" + strval(out) + "\' class=\'avatar avatar-" + size + "\' height=\'" + size + "\' width=\'" + size + "\' />";
        } else {
            avatar = "<img alt=\'\' src=\'" + _default + "\' class=\'avatar avatar-" + size + " avatar-default\' height=\'" + size + "\' width=\'" + size + "\' />";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_avatar", avatar, id_or_email, size, _default));
    }

    /**
     * wp_setcookie() - Sets a cookie for a user who just logged in
     * @since 1.5
     * @deprecated Use wp_set_auth_cookie()
     * @see wp_set_auth_cookie()
     * @param string $username The user's username
     * @param string $password Optional. The user's password
     * @param bool $already_md5 Optional. Whether the password has already been
     * through MD5
     * @param string $home Optional. Will be used instead of COOKIEPATH if set
     * @param string $siteurl Optional. Will be used instead of SITECOOKIEPATH
     * if set
     * @param bool $remember Optional. Remember that the user is logged in
     */
    public void wp_setcookie(String username, String password, boolean already_md5, String home, String siteurl, boolean remember) {
        StdClass user;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "2.5", "wp_set_auth_cookie()");
        user = get_userdatabylogin(username);
        wp_set_auth_cookie(intval(StdClass.getValue(user, "ID")), remember);
    }

    /**
     * wp_clearcookie() - Clears the authentication cookie, logging the user
     * out
     * @since 1.5
     * @deprecated Use wp_clear_auth_cookie()
     * @see wp_clear_auth_cookie()
     */
    public void wp_clearcookie() {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "2.5", "wp_clear_auth_cookie()");
        wp_clear_auth_cookie();
    }

    /**
     * wp_get_cookie_login() - Gets the user cookie login
     * This function is deprecated and should no longer be extended as it won't
     * be used anywhere in WordPress. Also, plugins shouldn't use it either.
     * @since 2.0.4
     * @deprecated No alternative
     * @return bool Always returns false
     */
    public Array<Object> wp_get_cookie_login() {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "2.5", "");

        return new Array<Object>();
    }

    /**
     * wp_login() - Checks a users login information and logs them in if it
     * checks out
     * Use the global $error to get the reason why the login failed. If the
     * username is blank, no error will be set, so assume blank username on that
     * case.
     * Plugins extending this function should also provide the global $error and
     * set what the error is, so that those checking the global for why there
     * was a failure can utilize it later.
     * @since 1.2.2
     * @deprecated Use wp_signon()
     * @global string $error Error when false is returned
     * @param string $username User's username
     * @param string $password User's password
     * @param bool $deprecated Not used
     * @return bool False on login failure, true on successful check
     */
    public boolean wp_login(String username, String password, boolean deprecated) {
        Object user = null;
        user = wp_authenticate(username, password);

        if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(user)) {
            return true;
        }

        gVars.error = ((WP_Error) user).get_error_message();

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
