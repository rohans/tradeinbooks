/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UserPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class UserPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(UserPage.class.getName());

    /**
     * Generated in place of local variable 'level' from method '_fill_user'
     * because it is used inside an inner class.
     */
    String _fill_user_level = null;
    
    public Object user_level;
    public Object user_url;
    public Object user_pass_md5;

    @Override
    @RequestMapping("/wp-includes/user.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/user";
    }

    public Object wp_signon(Array<Object> credentials) {
        Object user = null;
        WP_Error error = null;

        if (empty(credentials)) {
            if (!empty(gVars.webEnv._POST.getValue("log"))) {
                credentials.putValue("user_login", gVars.webEnv._POST.getValue("log"));
            }

            if (!empty(gVars.webEnv._POST.getValue("pwd"))) {
                credentials.putValue("user_password", gVars.webEnv._POST.getValue("pwd"));
            }

            if (!empty(gVars.webEnv._POST.getValue("rememberme"))) {
                credentials.putValue("remember", gVars.webEnv._POST.getValue("rememberme"));
            }
        }

        if (!empty(credentials.getValue("user_login"))) {
            credentials.putValue("user_login", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(credentials.getValue("user_login")), false));
        }

        if (!empty(credentials.getValue("user_password"))) {
            credentials.putValue("user_password", Strings.trim(strval(credentials.getValue("user_password"))));
        }

        if (!empty(credentials.getValue("remember"))) {
            credentials.putValue("remember", true);
        } else {
            credentials.putValue("remember", false);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array(
            "wp_authenticate",
            new Array<Object>(new ArrayEntry<Object>(credentials.getValue("user_login")), new ArrayEntry<Object>(credentials.getValue("user_password"))));

    	// If no credential info provided, check cookie.
        if (empty(credentials.getValue("user_login")) && empty(credentials.getValue("user_password"))) {
            user = getIncluded(PluggablePage.class, gVars, gConsts).wp_validate_auth_cookie("");

            if (booleanval(user)) {
                return new WP_User(gVars, gConsts, user);
            }

            if (!empty(gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE()))) {
                return new WP_Error(gVars, gConsts, "expired_session", getIncluded(L10nPage.class, gVars, gConsts).__("Please log in again.", "default"));
            }

			// If the cookie is not set, be silent.
            return new WP_Error(gVars, gConsts);
        }

        if (empty(credentials.getValue("user_login")) || empty(credentials.getValue("user_password"))) {
            error = new WP_Error(gVars, gConsts);

            if (empty(credentials.getValue("user_login"))) {
                error.add("empty_username", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: The username field is empty.", "default"));
            }

            if (empty(credentials.getValue("user_password"))) {
                error.add("empty_password", getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: The password field is empty.", "default"));
            }

            return error;
        }

        user = getIncluded(PluggablePage.class, gVars, gConsts).wp_authenticate(strval(credentials.getValue("user_login")), strval(credentials.getValue("user_password")));

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(user)) {
            return user;
        }

        getIncluded(PluggablePage.class, gVars, gConsts).wp_set_auth_cookie(((WP_User) user).getID(), credentials.getValue("remember"));
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_login", credentials.getValue("user_login"));

        return user;
    }

    public Object get_profile(String field, String user) {
        if (!booleanval(user)) {
            user = gVars.wpdb.escape(strval(gVars.webEnv._COOKIE.getValue(gConsts.getUSER_COOKIE())));
        }

        return gVars.wpdb.get_var("SELECT " + field + " FROM " + gVars.wpdb.users + " WHERE user_login = \'" + user + "\'");
    }

    public int get_usernumposts(int userid) {
        // userid = intval(userid);
        return intval(
                gVars.wpdb.get_var(
                        "SELECT COUNT(*) FROM " + gVars.wpdb.posts + " WHERE post_author = \'" + userid + "\' AND post_type = \'post\' AND " +
                        getIncluded(PostPage.class, gVars, gConsts).get_private_posts_cap_sql("post")));
    }

 // TODO: xmlrpc only.  Maybe move to xmlrpc.php.
    public boolean user_pass_ok(String user_login, String user_pass) {
        Object user = null;
        user = getIncluded(PluggablePage.class, gVars, gConsts).wp_authenticate(user_login, user_pass);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(user)) {
            return false;
        }

        return true;
    }

 //
 // User option functions
 //
    
    public Object get_user_option(String option)/*Commented by Numiton: , Object user*/
     {
        Object result = null;
        option = QRegExPerl.preg_replace("|[^a-z0-9_]|i", "", option);

        // Commented by Numiton. user always empty

        //		if (empty(user)) {
        WP_User user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();

        //		}

        //		else

        //			user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user.getID());

        // Modified by Numiton
        if (isset(StdClass.getValue(user, gVars.wpdb.prefix + option))) { // Blog specific
            result = StdClass.getValue(user, gVars.wpdb.prefix + option);
        } else if (isset(StdClass.getValue(user, option))) { // User specific and cross-blog
            result = StdClass.getValue(user, option);
        } else { // Blog global
            result = getIncluded(FunctionsPage.class, gVars, gConsts).get_option(option);
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_user_option_" + option, result, option, user);
    }

    public boolean update_user_option(int user_id, String option_name, Array<Object> newvalue, boolean global) {
        if (!global) {
            option_name = gVars.wpdb.prefix + option_name;
        }

        return update_usermeta(user_id, option_name, newvalue);
    }

 // Get users with capabilities for the current blog.
 // For setups that use the multi-blog feature.
    public Array<Object> get_users_of_blog(int id) {
        Array<Object> users = new Array<Object>();

        if (empty(id)) {
            id = gVars.blog_id;
        }

        users = gVars.wpdb.get_results(
                    "SELECT user_id, user_login, display_name, user_email, meta_value FROM " + gVars.wpdb.users + ", " + gVars.wpdb.usermeta + " WHERE " + gVars.wpdb.users + ".ID = " +
                    gVars.wpdb.usermeta + ".user_id AND meta_key = \'" + gVars.wpdb.prefix + "capabilities\' ORDER BY " + gVars.wpdb.usermeta + ".user_id");

        return users;
    }

 //
 // User meta functions
 //
    
    public boolean delete_usermeta(int user_id, String meta_key, Object meta_value)/* Do not change type */
     {
        if (!is_numeric(user_id)) {
            return false;
        }

        meta_key = QRegExPerl.preg_replace("|[^a-z0-9_]|i", "", meta_key);

        if (is_array(meta_value) || is_object(meta_value)) {
            meta_value = serialize(meta_value);
        }

        meta_value = Strings.trim(strval(meta_value));

        if (!empty(meta_value)) {
            gVars.wpdb.query("DELETE FROM " + gVars.wpdb.usermeta + " WHERE user_id = \'" + user_id + "\' AND meta_key = \'" + meta_key + "\' AND meta_value = \'" + meta_value + "\'");
        } else {
            gVars.wpdb.query("DELETE FROM " + gVars.wpdb.usermeta + " WHERE user_id = \'" + user_id + "\' AND meta_key = \'" + meta_key + "\'");
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user_id, "users");

        return true;
    }

    public Object get_usermeta(int user_id, String meta_key) {
        StdClass user = null;
        Array<Object> metas = new Array<Object>();
        user_id = user_id;

        if (!booleanval(user_id)) {
            return strval(false);
        }

        if (!empty(meta_key)) {
            meta_key = QRegExPerl.preg_replace("|[^a-z0-9_]|i", "", meta_key);
            user = (StdClass) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(user_id, "users");

    		// Check the cached user object
            if (!strictEqual(null, user) && isset(StdClass.getValue(user, meta_key))) {
                metas = new Array<Object>(new ArrayEntry<Object>(StdClass.getValue(user, meta_key)));
            } else {
                metas = gVars.wpdb.get_col(gVars.wpdb.prepare("SELECT meta_value FROM " + gVars.wpdb.usermeta + " WHERE user_id = %d AND meta_key = %s", user_id, meta_key));
            }
        } else {
            metas = gVars.wpdb.get_col(gVars.wpdb.prepare("SELECT meta_value FROM " + gVars.wpdb.usermeta + " WHERE user_id = %d", user_id));
        }

        if (empty(metas)) {
            if (empty(meta_key)) {
                return new Array<Object>();
            } else {
                return "";
            }
        }

        metas = Array.array_map(new Callback("maybe_unserialize", getIncluded(FunctionsPage.class, gVars, gConsts)), metas);

        if (equal(Array.count(metas), 1)) {
            return metas.getValue(0);
        } else {
            return metas;
        }
    }

    public boolean update_usermeta(int user_id, String meta_key, Object meta_value) {
        StdClass cur;

        if (!is_numeric(user_id)) {
            return false;
        }

        meta_key = QRegExPerl.preg_replace("|[^a-z0-9_]|i", "", meta_key);

    	// FIXME: usermeta data is assumed to be already escaped
        if (is_string(meta_value)) {
            meta_value = Strings.stripslashes(gVars.webEnv, strval(meta_value));
        }

        meta_value = strval(getIncluded(FunctionsPage.class, gVars, gConsts).maybe_serialize(meta_value));
        meta_value = gVars.wpdb.escape(strval(meta_value));

        if (empty(meta_value)) {
            return delete_usermeta(user_id, meta_key, "");
        }

        cur = (StdClass) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.usermeta + " WHERE user_id = \'" + user_id + "\' AND meta_key = \'" + meta_key + "\'");

        if (!booleanval(cur)) {
            gVars.wpdb.query("INSERT INTO " + gVars.wpdb.usermeta + " ( user_id, meta_key, meta_value )\n\t\tVALUES\n\t\t( \'" + user_id + "\', \'" + meta_key + "\', \'" + meta_value + "\' )");
        } else if (!equal(StdClass.getValue(cur, "meta_value"), meta_value)) {
            gVars.wpdb.query("UPDATE " + gVars.wpdb.usermeta + " SET meta_value = \'" + meta_value + "\' WHERE user_id = \'" + user_id + "\' AND meta_key = \'" + meta_key + "\'");
        } else {
            return false;
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(user_id, "users");

        return true;
    }

 //
 // Private helper functions
 //

 // Setup global user vars.  Used by set_current_user() for back compat.
    public void setup_userdata(int user_id) {
        WP_User user = null;

        if (equal("", user_id)) {
            user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
        } else {
            user = new WP_User(gVars, gConsts, user_id);
        }

        if (equal(0, user.getID())) {
            return;
        }

        gVars.userdata = user.data;
        gVars.user_login = user.getUser_login();
        user_level = (isset(user.getUser_level())
            ? intval(user.getUser_level())
            : 0);
        gVars.user_ID = user.getID();
        gVars.user_email = user.getUser_email();
        user_url = user.getUser_url();
        user_pass_md5 = Strings.md5(user.getUser_pass());
        gVars.user_identity = user.getDisplay_name();
    }

    public Object wp_dropdown_users(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        String query = null;
        Array<String> query_where = new Array<String>();
        Object include = null;

        /* Do not change type */
        Object exclude = null;

        /* Do not change type */
        String orderby = null;
        String order = null;
        Array<Object> users = new Array<Object>();
        Object output = null;
        Object name = null;
        Object _class = null;
        Object show_option_all = null;
        Object show_option_none = null;
        StdClass user = null;
        String _selected = null;
        Object selected = null;
        Object show = null;
        Object echo = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("show_option_all", ""),
                new ArrayEntry<Object>("show_option_none", ""),
                new ArrayEntry<Object>("orderby", "display_name"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("include", ""),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("show", "display_name"),
                new ArrayEntry<Object>("echo", 1),
                new ArrayEntry<Object>("selected", 0),
                new ArrayEntry<Object>("name", "user"),
                new ArrayEntry<Object>("class", ""));
        
        defaults.putValue("selected", getIncluded(QueryPage.class, gVars, gConsts).is_author()
            ? getIncluded(QueryPage.class, gVars, gConsts).get_query_var("author")
            : 0);
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        include = Array.extractVar(r, "include", include, Array.EXTR_SKIP);
        exclude = Array.extractVar(r, "exclude", exclude, Array.EXTR_SKIP);
        orderby = strval(Array.extractVar(r, "orderby", orderby, Array.EXTR_SKIP));
        order = strval(Array.extractVar(r, "order", order, Array.EXTR_SKIP));
        name = Array.extractVar(r, "name", name, Array.EXTR_SKIP);
        _class = Array.extractVar(r, "class", _class, Array.EXTR_SKIP);
        show_option_all = Array.extractVar(r, "show_option_all", show_option_all, Array.EXTR_SKIP);
        show_option_none = Array.extractVar(r, "show_option_none", show_option_none, Array.EXTR_SKIP);
        selected = Array.extractVar(r, "selected", selected, Array.EXTR_SKIP);
        show = Array.extractVar(r, "show", show, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);
        
        query = "SELECT * FROM " + gVars.wpdb.users;
        query_where = new Array<String>();

        if (is_array(include)) {
            include = Strings.join(",", (Array) include);
        }

        include = QRegExPerl.preg_replace("/[^0-9,]/", "", strval(include)); // (int)

        if (booleanval(include)) {
            query_where.putValue("ID IN (" + include + ")");
        }

        if (is_array(exclude)) {
            exclude = Strings.join(",", (Array) exclude); // (int)
        }

        exclude = QRegExPerl.preg_replace("/[^0-9,]/", "", strval(exclude));

        if (booleanval(exclude)) {
            query_where.putValue("ID NOT IN (" + exclude + ")");
        }

        if (booleanval(query_where)) {
            query = query + " WHERE " + Strings.join(" AND", query_where);
        }

        query = query + " ORDER BY " + strval(orderby) + " " + strval(order);
        users = gVars.wpdb.get_results(query);
        output = "";

        if (!empty(users)) {
            output = "<select name=\'" + strval(name) + "\' id=\'" + strval(name) + "\' class=\'" + strval(_class) + "\'>\n";

            if (booleanval(show_option_all)) {
                output = strval(output) + "\t<option value=\'0\'>" + strval(show_option_all) + "</option>\n";
            }

            if (booleanval(show_option_none)) {
                output = strval(output) + "\t<option value=\'-1\'>" + strval(show_option_none) + "</option>\n";
            }

            for (Map.Entry javaEntry644 : users.entrySet()) {
                user = (StdClass) javaEntry644.getValue();

                // user.ID=intval(user.ID);
                _selected = (equal(StdClass.getValue(user, "ID"), selected)
                    ? " selected=\'selected\'"
                    : "");
                output = strval(output) + "\t<option value=\'" + StdClass.getValue(user, "ID") + "\'" + _selected + ">" +
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(user, strval(show))), strval(0)) + "</option>\n";
            }

            output = strval(output) + "</select>";
        }

        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_dropdown_users", output);

        if (booleanval(echo)) {
            echo(gVars.webEnv, output);
        }

        return output;
    }

    public void _fill_user(StdClass user) {
        boolean show = false;
        Array<Object> metavalues = new Array<Object>();
        Object value = null;
        StdClass meta = null;
        show = gVars.wpdb.hide_errors();
        metavalues = gVars.wpdb.get_results(gVars.wpdb.prepare("SELECT meta_key, meta_value FROM " + gVars.wpdb.usermeta + " WHERE user_id = %d", StdClass.getValue(user, "ID")));
        gVars.wpdb.show_errors(show);

        if (booleanval(metavalues)) {
            for (Map.Entry javaEntry645 : metavalues.entrySet()) {
                meta = (StdClass) javaEntry645.getValue();
                value = getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(StdClass.getValue(meta, "meta_value"));
                user.fields.putValue(StdClass.getValue(meta, "meta_key"), value);
            }
        }

        _fill_user_level = gVars.wpdb.prefix + "user_level";

        if (isset(StdClass.getValue(user, _fill_user_level))) {
            user.fields.putValue("user_level", StdClass.getValue(user, _fill_user_level));
        }

    	// For backwards compat.
        if (isset(StdClass.getValue(user, "first_name"))) {
            user.fields.putValue("user_firstname", StdClass.getValue(user, "first_name"));
        }

        if (isset(StdClass.getValue(user, "last_name"))) {
            user.fields.putValue("user_lastname", StdClass.getValue(user, "last_name"));
        }

        if (isset(StdClass.getValue(user, "description"))) {
            user.fields.putValue("user_description", StdClass.getValue(user, "description"));
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(user, "ID"), user, "users", 0);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(user, "user_login"), StdClass.getValue(user, "ID"), "userlogins", 0);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(user, "user_email"), StdClass.getValue(user, "ID"), "useremail", 0);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
