/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UsersPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_admin.includes.UserPage;
import org.numiton.nwp.wp_admin.includes.WP_User_Search;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class UsersPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(UsersPage.class.getName());
    public Object update;
    public Array<Object> userids;
    public int delete_count;
    public boolean go_delete;
    public Object all_logins;
    public Object user_dropdown;
    public StdClass login;
    public Object add_user_errors;
    public WP_User_Search wp_user_search;
    public Array<String> role_links = new Array<String>();
    public Array<Object> avail_roles = new Array<Object>();
    public Object users_of_blog;
    public Object b_roles;
    public StdClass b_user;
    public Object b_role;
    public Object current_role;
    public Object userid;
    public Array<Object> roles;
    public Object formpost;
    public String new_user_login;
    public String new_user_firstname;
    public String new_user_lastname;
    public String new_user_email;
    public String new_user_uri;
    public String new_user_role;

    @Override
    @RequestMapping("/wp-admin/users.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/users";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_users_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, RegistrationPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
        }

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Users", "default");
        gVars.parent_file = "users.php";
        gVars.action = gVars.webEnv._REQUEST.getValue("action");
        update = "";

        if (empty(gVars.action)) {
            if (isset(gVars.webEnv._GET.getValue("deleteit"))) {
                gVars.action = "delete";
            } else if (isset(gVars.webEnv._GET.getValue("changeit")) && !empty(gVars.webEnv._GET.getValue("new_role"))) {
                gVars.action = "promote";
            }
        }

        if (empty(gVars.webEnv._REQUEST)) {
            gVars.referer = "<input type=\"hidden\" name=\"wp_http_referer\" value=\"" +
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())) + "\" />";
        } else if (isset(gVars.webEnv._REQUEST.getValue("wp_http_referer"))) {
            gVars.redirect = getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(
                        new ArrayEntry<Object>("wp_http_referer"),
                        new ArrayEntry<Object>("updated"),
                        new ArrayEntry<Object>("delete_count")), Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("wp_http_referer"))));
            gVars.referer = "<input type=\"hidden\" name=\"wp_http_referer\" value=\"" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.redirect) + "\" />";
        } else {
            gVars.redirect = "users.php";
        }

        {
            int javaSwitchSelector31 = 0;

            if (equal(gVars.action, "promote")) {
                javaSwitchSelector31 = 1;
            }

            if (equal(gVars.action, "dodelete")) {
                javaSwitchSelector31 = 2;
            }

            if (equal(gVars.action, "delete")) {
                javaSwitchSelector31 = 3;
            }

            if (equal(gVars.action, "adduser")) {
                javaSwitchSelector31 = 4;
            }

            switch (javaSwitchSelector31) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-users", "_wpnonce");

                if (empty(gVars.webEnv._REQUEST.getValue("users"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect, 302);
                    System.exit();
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You can&#8217;t edit users.", "default"), "");
                }

                userids = gVars.webEnv._REQUEST.getArrayValue("users");
                update = "promote";

                for (Map.Entry javaEntry321 : userids.entrySet()) {
                    gVars.id = javaEntry321.getValue();

                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_user", gVars.id)) {
                    	// The new role of the current user must also have edit_users caps
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You can&#8217;t edit that user.", "default"), "");
                    }

                    if (equal(gVars.id, gVars.current_user.getID()) && !gVars.wp_roles.role_objects.getValue(gVars.webEnv._REQUEST.getValue("new_role")).has_cap("edit_users")) {
                        update = "err_admin_role";

                        continue;
                    }

                    gVars.user = new WP_User(gVars, gConsts, gVars.id);
                    ((WP_User) gVars.user).set_role(gVars.webEnv._REQUEST.getValue("new_role"));
                }

                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("update", update, gVars.redirect), 302);
                System.exit();

                break;
            }

            case 2: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-users", "_wpnonce");

                if (empty(gVars.webEnv._REQUEST.getValue("users"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect, 302);
                    System.exit();
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_users")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You can&#8217;t delete users.", "default"), "");
                }

                userids = gVars.webEnv._REQUEST.getArrayValue("users");
                update = "del";
                delete_count = 0;

                for (Map.Entry javaEntry322 : new Array<Object>(userids).entrySet()) {
                    gVars.id = javaEntry322.getValue();

                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_user", gVars.id)) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You can&#8217;t delete that user.", "default"), "");
                    }

                    if (equal(gVars.id, gVars.current_user.getID())) {
                        update = "err_admin_del";

                        continue;
                    }

                    {
                        int javaSwitchSelector32 = 0;

                        if (equal(gVars.webEnv._REQUEST.getValue("delete_option"), "delete")) {
                            javaSwitchSelector32 = 1;
                        }

                        if (equal(gVars.webEnv._REQUEST.getValue("delete_option"), "reassign")) {
                            javaSwitchSelector32 = 2;
                        }

                        switch (javaSwitchSelector32) {
                        case 1: {
                            getIncluded(UserPage.class, gVars, gConsts).wp_delete_user(gVars.id, "novalue");

                            break;
                        }

                        case 2: {
                            getIncluded(UserPage.class, gVars, gConsts).wp_delete_user(gVars.id, strval(gVars.webEnv._REQUEST.getValue("reassign_user")));

                            break;
                        }
                        }
                    }

                    ++delete_count;
                }

                gVars.redirect = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(new Array<Object>(
                            new ArrayEntry<Object>("delete_count", delete_count),
                            new ArrayEntry<Object>("update", update)), gVars.redirect);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect, 302);
                System.exit();

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-users", "_wpnonce");

                if (empty(gVars.webEnv._REQUEST.getValue("users"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect, 302);
                    System.exit();
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_users")) {
                    gVars.errors = new WP_Error(gVars, gConsts, "edit_users", getIncluded(L10nPage.class, gVars, gConsts).__("You can&#8217;t delete users.", "default"));
                }

                userids = gVars.webEnv._REQUEST.getArrayValue("users");
                include(gVars, gConsts, Admin_headerPage.class);
                echo(gVars.webEnv, "<form action=\"\" method=\"post\" name=\"updateusers\" id=\"updateusers\">\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("delete-users", "_wpnonce", true, true);
                echo(gVars.webEnv, gVars.referer);
                echo(gVars.webEnv, "\n<div class=\"wrap\">\n<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Delete Users", "default");
                echo(gVars.webEnv, "</h2>\n<p>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("You have specified these users for deletion:", "default");
                echo(gVars.webEnv, "</p>\n<ul>\n");
                go_delete = false;

                for (Map.Entry javaEntry323 : new Array<Object>(userids).entrySet()) {
                    gVars.id = javaEntry323.getValue();
                    gVars.user = new WP_User(gVars, gConsts, gVars.id);

                    if (equal(gVars.id, gVars.current_user.getID())) {
                        echo(gVars.webEnv,
                            "<li>" +
                            QStrings.sprintf(
                                getIncluded(L10nPage.class, gVars, gConsts).__("ID #%1s: %2s <strong>The current user will not be deleted.</strong>", "default"),
                                gVars.id,
                                ((WP_User) gVars.user).getUser_login()) + "</li>\n");
                    } else {
                        echo(
                            gVars.webEnv,
                            "<li><input type=\"hidden\" name=\"users[]\" value=\"" + strval(gVars.id) + "\" />" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("ID #%1s: %2s", "default"), gVars.id, ((WP_User) gVars.user).getUser_login()) + "</li>\n");
                        go_delete = true;
                    }
                }

                all_logins = gVars.wpdb.get_results("SELECT ID, user_login FROM " + gVars.wpdb.users + " ORDER BY user_login");
                user_dropdown = "<select name=\"reassign_user\">";

                for (Map.Entry javaEntry324 : new Array<Object>(all_logins).entrySet()) {
                    login = (StdClass) javaEntry324.getValue();

                    if (equal(StdClass.getValue(login, "ID"), gVars.current_user.getID()) || !Array.in_array(StdClass.getValue(login, "ID"), userids)) {
                        user_dropdown = user_dropdown + "<option value=\"" + StdClass.getValue(login, "ID") + "\">" + StdClass.getValue(login, "user_login") + "</option>";
                    }
                }

                user_dropdown = user_dropdown + "</select>";
                echo(gVars.webEnv, "\t</ul>\n");

                if (go_delete) {
                    echo(gVars.webEnv, "\t<p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("What should be done with posts and links owned by this user?", "default");
                    echo(
                        gVars.webEnv,
                        "</p>\n\t<ul style=\"list-style:none;\">\n\t\t<li><label><input type=\"radio\" id=\"delete_option0\" name=\"delete_option\" value=\"delete\" checked=\"checked\" />\n\t\t");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Delete all posts and links.", "default");
                    echo(gVars.webEnv, "</label></li>\n\t\t<li><input type=\"radio\" id=\"delete_option1\" name=\"delete_option\" value=\"reassign\" />\n\t\t");
                    echo(gVars.webEnv, "<label for=\"delete_option1\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Attribute all posts and links to:", "default") + "</label> " + user_dropdown);
                    echo(gVars.webEnv, "</li>\n\t</ul>\n\t<input type=\"hidden\" name=\"action\" value=\"dodelete\" />\n\t<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Confirm Deletion", "default");
                    echo(gVars.webEnv, "\" class=\"button-secondary\" /></p>\n");
                } else {
                    echo(gVars.webEnv, "\t<p>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("There are no valid users selected for deletion.", "default");
                    echo(gVars.webEnv, "</p>\n");
                }

                echo(gVars.webEnv, "</div>\n</form>\n");

                break;
            }

            case 4: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-user", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("create_users")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You can&#8217;t create users.", "default"), "");
                }

                gVars.user_id = getIncluded(UserPage.class, gVars, gConsts).add_user();
                update = "add";

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.user_id)) {
                    add_user_errors = gVars.user_id;
                } else {
                    new_user_login = strval(
                            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                                "pre_user_login",
                                getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("user_login"))), true)));
                    gVars.redirect = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(new Array<Object>(
                                new ArrayEntry<Object>("usersearch", URL.urlencode(new_user_login)),
                                new ArrayEntry<Object>("update", update)), gVars.redirect);
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect + "#user-" + strval(gVars.user_id), 302);
                    System.exit();
                }
            }

            default:/*
             * Query the users Query the users
             */

            /*
             * var_dump($users_of_blog); var_dump($users_of_blog);
             */
             {
                if (!empty(gVars.webEnv._GET.getValue("_wp_http_referer"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                            getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(
                                new Array<Object>(new ArrayEntry<Object>("_wp_http_referer"), new ArrayEntry<Object>("_wpnonce")),
                                Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())),
                            302);
                    System.exit();
                }

                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-users", false, new Array<Object>(), false);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
                
                include(gVars, gConsts, Admin_headerPage.class);
                
                // Query the users
                wp_user_search = new WP_User_Search(gVars, gConsts, gVars.webEnv._GET.getValue("usersearch"), gVars.webEnv._GET.getValue("userspage"), gVars.webEnv._GET.getValue("role"));

                if (isset(gVars.webEnv._GET.getValue("update"))) {
                    {
                        int javaSwitchSelector33 = 0;

                        if (equal(gVars.webEnv._GET.getValue("update"), "del")) {
                            javaSwitchSelector33 = 1;
                        }

                        if (equal(gVars.webEnv._GET.getValue("update"), "del_many")) {
                            javaSwitchSelector33 = 2;
                        }

                        if (equal(gVars.webEnv._GET.getValue("update"), "add")) {
                            javaSwitchSelector33 = 3;
                        }

                        if (equal(gVars.webEnv._GET.getValue("update"), "promote")) {
                            javaSwitchSelector33 = 4;
                        }

                        if (equal(gVars.webEnv._GET.getValue("update"), "err_admin_role")) {
                            javaSwitchSelector33 = 5;
                        }

                        if (equal(gVars.webEnv._GET.getValue("update"), "err_admin_del")) {
                            javaSwitchSelector33 = 6;
                        }

                        switch (javaSwitchSelector33) {
                        case 1: {
                        }

                        case 2: {
                            echo(gVars.webEnv, "\t\t\t");
                            delete_count = intval(gVars.webEnv._GET.getValue("delete_count"));
                            echo(gVars.webEnv, "\t\t\t<div id=\"message\" class=\"updated fade\"><p>");
                            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s user deleted", "%s users deleted", delete_count, "default"), delete_count);
                            echo(gVars.webEnv, "</p></div>\n\t\t");

                            break;
                        }

                        case 3: {
                            echo(gVars.webEnv, "\t\t\t<div id=\"message\" class=\"updated fade\"><p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("New user created.", "default");
                            echo(gVars.webEnv, "</p></div>\n\t\t");

                            break;
                        }

                        case 4: {
                            echo(gVars.webEnv, "\t\t\t<div id=\"message\" class=\"updated fade\"><p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("Changed roles.", "default");
                            echo(gVars.webEnv, "</p></div>\n\t\t");

                            break;
                        }

                        case 5: {
                            echo(gVars.webEnv, "\t\t\t<div id=\"message\" class=\"error\"><p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("The current user\'s role must have user editing capabilities.", "default");
                            echo(gVars.webEnv, "</p></div>\n\t\t\t<div id=\"message\" class=\"updated fade\"><p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("Other user roles have been changed.", "default");
                            echo(gVars.webEnv, "</p></div>\n\t\t");

                            break;
                        }

                        case 6: {
                            echo(gVars.webEnv, "\t\t\t<div id=\"message\" class=\"error\"><p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("You can\'t delete the current user.", "default");
                            echo(gVars.webEnv, "</p></div>\n\t\t\t<div id=\"message\" class=\"updated fade\"><p>");
                            getIncluded(L10nPage.class, gVars, gConsts)._e("Other users have been deleted.", "default");
                            echo(gVars.webEnv, "</p></div>\n\t\t");

                            break;
                        }
                        }
                    }
                } else {
                }

                echo(gVars.webEnv, "\n");

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.errors)) {
                    echo(gVars.webEnv, "\t<div class=\"error\">\n\t\t<ul>\n\t\t");

                    for (Map.Entry javaEntry325 : ((WP_Error) gVars.errors).get_error_messages().entrySet()) {
                        gVars.message = strval(javaEntry325.getValue());
                        echo(gVars.webEnv, "<li>" + gVars.message + "</li>");
                    }

                    echo(gVars.webEnv, "\t\t</ul>\n\t</div>\n");
                } else {
                }

                echo(gVars.webEnv, "\n<div class=\"wrap\">\n<form id=\"posts-filter\" action=\"\" method=\"get\">\n\t");

                if (wp_user_search.is_search()) {
                    echo(gVars.webEnv, "\t\t<h2>");
                    QStrings.printf(
                        gVars.webEnv,
                        getIncluded(L10nPage.class, gVars, gConsts).__("Users Matching \"%s\"", "default"),
                        getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(wp_user_search.search_term, strval(0)));
                    echo(gVars.webEnv, "</h2>\n\t");
                } else {
                    echo(gVars.webEnv, "\t\t<h2>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Manage Users", "default");
                    echo(gVars.webEnv, "</h2>\n\t");
                }

                echo(gVars.webEnv, "\n<ul class=\"subsubsub\">\n");
                role_links = new Array<String>();
                avail_roles = new Array<Object>();
                users_of_blog = (((org.numiton.nwp.wp_includes.UserPage) getIncluded(org.numiton.nwp.wp_includes.UserPage.class, gVars, gConsts))).get_users_of_blog(intval(""));
                //var_dump($users_of_blog);

                for (Map.Entry javaEntry326 : new Array<Object>(users_of_blog).entrySet()) {
                    b_user = (StdClass) javaEntry326.getValue();
                    b_roles = unserialize(strval(StdClass.getValue(b_user, "meta_value")));

                    for (Map.Entry javaEntry327 : new Array<Object>(b_roles).entrySet()) {
                        b_role = javaEntry327.getKey();
                        gVars.val = javaEntry327.getValue();

                        if (!isset(avail_roles.getValue(b_role))) {
                            avail_roles.putValue(b_role, 0);
                        }

                        avail_roles.incValue(b_role);
                    }
                }

                users_of_blog = null;
                current_role = false;
                gVars._class = (empty(gVars.webEnv._GET.getValue("role"))
                    ? " class=\"current\""
                    : "");
                role_links.putValue("<li><a href=\"users.php\"" + gVars._class + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("All Users", "default") + "</a>");

                for (Map.Entry javaEntry328 : gVars.wp_roles.get_names().entrySet()) {
                    gVars.role = strval(javaEntry328.getKey());
                    gVars.name = strval(javaEntry328.getValue());

                    if (!isset(avail_roles.getValue(gVars.role))) {
                        continue;
                    }

                    gVars._class = "";

                    if (equal(gVars.role, gVars.webEnv._GET.getValue("role"))) {
                        current_role = gVars.webEnv._GET.getValue("role");
                        gVars._class = " class=\"current\"";
                    }

                    gVars.name = getIncluded(L10nPage.class, gVars, gConsts).translate_with_context(gVars.name, "default");
                    gVars.name = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s (%2$s)|user role with count", "default"), gVars.name, avail_roles.getValue(gVars.role));
                    role_links.putValue("<li><a href=\"users.php?role=" + gVars.role + "\"" + gVars._class + ">" + gVars.name + "</a>");
                }

                echo(gVars.webEnv, Strings.implode(" |</li>", role_links) + "</li>");
                role_links = null;
                echo(gVars.webEnv, "</ul>\n\t<p id=\"post-search\">\n\t<input type=\"text\" id=\"post-search-input\" name=\"usersearch\" value=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(wp_user_search.search_term));
                echo(gVars.webEnv, "\" />\n\t<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Search Users", "default");
                echo(gVars.webEnv, "\" class=\"button\" />\n\t</p>\n\n<div class=\"tablenav\">\n\n");

                if (wp_user_search.results_are_paged()) {
                    echo(gVars.webEnv, "\t<div class=\"tablenav-pages\">");
                    wp_user_search.page_links();
                    echo(gVars.webEnv, "</div>\n");
                } else {
                }

                echo(gVars.webEnv, "\n<div class=\"alignleft\">\n<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");
                echo(gVars.webEnv, "\" name=\"deleteit\" class=\"button-secondary delete\" />\n<select name=\"new_role\"><option value=\'\'>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Change role to&hellip;", "default");
                echo(gVars.webEnv, "</option>\"");
                getIncluded(TemplatePage.class, gVars, gConsts).wp_dropdown_roles(false);
                echo(gVars.webEnv, "</select>\n<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Change", "default");
                echo(gVars.webEnv, "\" name=\"changeit\" class=\"button-secondary\" />\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-users", "_wpnonce", true, true);
                echo(gVars.webEnv, "</div>\n\n<br class=\"clear\" />\n</div>\n\n<br class=\"clear\" />\n\n\t");

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(wp_user_search.search_errors)) {
                    echo(gVars.webEnv, "\t\t<div class=\"error\">\n\t\t\t<ul>\n\t\t\t");

                    for (Map.Entry javaEntry329 : ((WP_Error) wp_user_search.search_errors).get_error_messages().entrySet()) {
                        gVars.message = strval(javaEntry329.getValue());
                        echo(gVars.webEnv, "<li>" + gVars.message + "</li>");
                    }

                    echo(gVars.webEnv, "\t\t\t</ul>\n\t\t</div>\n\t");
                } else {
                }

                echo(gVars.webEnv, "\n\n");

                if (booleanval(wp_user_search.get_results())) {
                    echo(gVars.webEnv, "\n\t");

                    if (wp_user_search.is_search()) {
                        echo(gVars.webEnv, "\t\t<p><a href=\"users.php\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("&laquo; Back to All Users", "default");
                        echo(gVars.webEnv, "</a></p>\n\t");
                    } else {
                    }

                    echo(
                            gVars.webEnv,
                            "\n<table class=\"widefat\">\n<thead>\n<tr class=\"thead\">\n\t<th scope=\"col\" class=\"check-column\"><input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" /> </th>\n\t<th>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Username", "default");
                    echo(gVars.webEnv, "</th>\n\t<th>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");
                    echo(gVars.webEnv, "</th>\n\t<th>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail", "default");
                    echo(gVars.webEnv, "</th>\n\t<th>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Role", "default");
                    echo(gVars.webEnv, "</th>\n\t<th class=\"num\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Posts", "default");
                    echo(gVars.webEnv, "</th>\n</tr>\n</thead>\n<tbody id=\"users\" class=\"list:user user-list\">\n");
                    gVars.style = "";

                    for (Map.Entry javaEntry330 : wp_user_search.get_results().entrySet()) {
                        userid = javaEntry330.getValue();
                        gVars.user_object = new WP_User(gVars, gConsts, userid);
                        roles = Array.arrayCopy(gVars.user_object.getRoles());
                        gVars.role = strval(Array.array_shift(roles));
                        gVars.style = (equal(" class=\"alternate\"", gVars.style)
                            ? ""
                            : " class=\"alternate\"");
                        echo(gVars.webEnv, "\n\t" + strval(getIncluded(TemplatePage.class, gVars, gConsts).user_row(gVars.user_object, gVars.style, gVars.role)));
                    }

                    echo(gVars.webEnv, "</tbody>\n</table>\n\n<div class=\"tablenav\">\n\n");

                    if (wp_user_search.results_are_paged()) {
                        echo(gVars.webEnv, "\t<div class=\"tablenav-pages\">");
                        wp_user_search.page_links();
                        echo(gVars.webEnv, "</div>\n");
                    } else {
                    }

                    echo(gVars.webEnv, "\n<br class=\"clear\" />\n</div>\n\n");
                } else {
                }

                echo(gVars.webEnv, "\n</form>\n</div>\n\n");

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(add_user_errors)) {
                    for (Map.Entry javaEntry331 : new Array<Object>(
                            new ArrayEntry<Object>("user_login", "user_login"),
                            new ArrayEntry<Object>("first_name", "user_firstname"),
                            new ArrayEntry<Object>("last_name", "user_lastname"),
                            new ArrayEntry<Object>("email", "user_email"),
                            new ArrayEntry<Object>("url", "user_uri"),
                            new ArrayEntry<Object>("role", "user_role")).entrySet()) {
                        formpost = javaEntry331.getKey();
                        gVars.var = strval(javaEntry331.getValue());
                        gVars.var = "new_" + gVars.var;

                        // Modified by Numiton
                        String value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(webEnv, strval(gVars.webEnv._REQUEST.getValue(formpost))));

                        if (equal(gVars.var, "new_user_login")) {
                            new_user_login = value;
                        } else if (equal(gVars.var, "new_user_firstname")) {
                            new_user_firstname = value;
                        } else if (equal(gVars.var, "new_user_lastname")) {
                            new_user_lastname = value;
                        } else if (equal(gVars.var, "new_user_email")) {
                            new_user_email = value;
                        } else if (equal(gVars.var, "new_user_uri")) {
                            new_user_uri = value;
                        } else if (equal(gVars.var, "new_user_role")) {
                            new_user_role = value;
                        } else {
                            LOG.warn("Unsupported field name: " + gVars.var);
                        }
                    }

                    gVars.name = null;
                }

                echo(gVars.webEnv, "\n<br class=\"clear\" />\n");

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("create_users")) {
                    echo(gVars.webEnv, "\n<div class=\"wrap\">\n<h2 id=\"add-new-user\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Add New User", "default");
                    echo(gVars.webEnv, "</h2>\n\n");

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(add_user_errors)) {
                        echo(gVars.webEnv, "\t<div class=\"error\">\n\t\t");

                        for (Map.Entry javaEntry332 : ((WP_Error) add_user_errors).get_error_messages().entrySet()) {
                            gVars.message = strval(javaEntry332.getValue());
                            echo(gVars.webEnv, "<p>" + gVars.message + "</p>");
                        }

                        echo(gVars.webEnv, "\t</div>\n");
                    } else {
                    }

                    echo(gVars.webEnv, "<div id=\"ajax-response\"></div>\n\n");

                    if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("users_can_register"))) {
                        echo(
                            gVars.webEnv,
                            "<p>" +
                            QStrings.sprintf(
                                getIncluded(L10nPage.class, gVars, gConsts).__("Users can <a href=\"%1$s\">register themselves</a> or you can manually create users here.", "default"),
                                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-register.php") + "</p>");
                    } else {
                        echo(
                            gVars.webEnv,
                            "<p>" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__(
                                    "Users cannot currently <a href=\"%1$s\">register themselves</a>, but you can manually create users here.",
                                    "default"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/options-general.php#users_can_register") + "</p>");
                    }

                    echo(gVars.webEnv, "<form action=\"#add-new-user\" method=\"post\" name=\"adduser\" id=\"adduser\" class=\"add:users: validate\">\n");
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-user", "_wpnonce", true, true);
                    echo(gVars.webEnv, "<table class=\"form-table\">\n\t<tr class=\"form-field form-required\">\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Username (required)", "default");
                    echo(gVars.webEnv, "<input name=\"action\" type=\"hidden\" id=\"action\" value=\"adduser\" /></th>\n\t\t<td ><input name=\"user_login\" type=\"text\" id=\"user_login\" value=\"");
                    echo(gVars.webEnv, new_user_login);
                    echo(gVars.webEnv, "\" /></td>\n\t</tr>\n\t<tr class=\"form-field\">\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("First Name", "default");
                    echo(gVars.webEnv, " </th>\n\t\t<td><input name=\"first_name\" type=\"text\" id=\"first_name\" value=\"");
                    echo(gVars.webEnv, new_user_firstname);
                    echo(gVars.webEnv, "\" /></td>\n\t</tr>\n\t<tr class=\"form-field\">\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Last Name", "default");
                    echo(gVars.webEnv, " </th>\n\t\t<td><input name=\"last_name\" type=\"text\" id=\"last_name\" value=\"");
                    echo(gVars.webEnv, new_user_lastname);
                    echo(gVars.webEnv, "\" /></td>\n\t</tr>\n\t<tr class=\"form-field form-required\">\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail (required)", "default");
                    echo(gVars.webEnv, "</th>\n\t\t<td><input name=\"email\" type=\"text\" id=\"email\" value=\"");
                    echo(gVars.webEnv, new_user_email);
                    echo(gVars.webEnv, "\" /></td>\n\t</tr>\n\t<tr class=\"form-field\">\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Website", "default");
                    echo(gVars.webEnv, "</th>\n\t\t<td><input name=\"url\" type=\"text\" id=\"url\" value=\"");
                    echo(gVars.webEnv, new_user_uri);
                    echo(gVars.webEnv, "\" /></td>\n\t</tr>\n\n");

                    if (booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("show_password_fields", true))) {
                        echo(gVars.webEnv, "\t<tr class=\"form-field form-required\">\n\t\t<th scope=\"row\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("Password (twice)", "default");
                        echo(
                            gVars.webEnv,
                            " </th>\n\t\t<td><input name=\"pass1\" type=\"password\" id=\"pass1\" />\n\t\t<br />\n\t\t<input name=\"pass2\" type=\"password\" id=\"pass2\" /></td>\n\t</tr>\n");
                    } else {
                    }

                    echo(gVars.webEnv, "\n\t<tr class=\"form-field\">\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Role", "default");
                    echo(gVars.webEnv, "</th>\n\t\t<td><select name=\"role\" id=\"role\">\n\t\t\t");

                    if (!booleanval(new_user_role)) {
                        new_user_role = strval(booleanval(current_role)
                                ? current_role
                                : getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_role"));
                    }

                    getIncluded(TemplatePage.class, gVars, gConsts).wp_dropdown_roles(new_user_role);
                    echo(gVars.webEnv, "\t\t\t</select>\n\t\t</td>\n\t</tr>\n</table>\n<p class=\"submit\">\n\t");
                    echo(gVars.webEnv, gVars.referer);
                    echo(gVars.webEnv, "\t<input name=\"adduser\" type=\"submit\" id=\"addusersub\" value=\"");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Add User", "default");
                    echo(gVars.webEnv, "\" />\n</p>\n</form>\n\n</div>\n\n");
                }

                break;
            }
            }
        } // end of the $action switch

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
