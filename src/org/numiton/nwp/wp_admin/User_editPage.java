/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: User_editPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_admin.includes.UserPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class User_editPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(User_editPage.class.getName());
    public boolean is_profile_page;
    public String wp_http_referer;
    public WP_User profileuser;
    public String current_color;
    public String html_color;
    public StdClass color_info;
    public Object role_list;
    public boolean user_has_role;
    public Array<Object> public_display = new Array<Object>();
    public Object show_password_fields;
    public Object cap;

    @Override
    @RequestMapping("/wp-admin/user-edit.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/user_edit";
    }

    public void profile_js() {
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\n\tfunction check_pass_strength ( ) {\n\n\t\tvar pass = jQuery(\'#pass1\').val();\n\t\tvar user = jQuery(\'#user_login\').val();\n\n\t\t// get the result as an object, i\'m tired of typing it\n\t\tvar res = jQuery(\'#pass-strength-result\');\n\n\t\tvar strength = passwordStrength(pass, user);\n\n\t\tjQuery(res).removeClass(\'short bad good strong\');\n\n\t\tif ( strength == pwsL10n.bad ) {\n\t\t\tjQuery(res).addClass(\'bad\');\n\t\t\tjQuery(res).html( pwsL10n.bad );\n\t\t}\n\t\telse if ( strength == pwsL10n.good ) {\n\t\t\tjQuery(res).addClass(\'good\');\n\t\t\tjQuery(res).html( pwsL10n.good );\n\t\t}\n\t\telse if ( strength == pwsL10n.strong ) {\n\t\t\tjQuery(res).addClass(\'strong\');\n\t\t\tjQuery(res).html( pwsL10n.strong );\n\t\t}\n\t\telse {\n\t\t\t// this catches \'Too short\' and the off chance anything else comes along\n\t\t\tjQuery(res).addClass(\'short\');\n\t\t\tjQuery(res).html( pwsL10n.short );\n\t\t}\n\n\t}\n\n\tjQuery(function($) { \n\t\t$(\'#pass1\').keyup( check_pass_strength ) \n\t\t$(\'.color-palette\').click(function(){$(this).siblings(\'input[name=admin_color]\').attr(\'checked\', \'checked\')});\n\t} );\n\t\n\tjQuery(document).ready( function() {\n\t\tjQuery(\'#pass1,#pass2\').attr(\'autocomplete\',\'off\');\n    });\n</script>\n");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_user_edit_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        if (gConsts.isIS_PROFILE_PAGEDefined() && gConsts.getIS_PROFILE_PAGE()) {
            is_profile_page = true;
        } else {
            is_profile_page = false;
        }

        if (is_profile_page) {
            getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "profile_js"), 10, 1);
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("jquery", false, new Array<Object>(), false);
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("password-strength-meter", false, new Array<Object>(), false);
        }

        gVars.title = (is_profile_page
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Profile", "default")
            : getIncluded(L10nPage.class, gVars, gConsts).__("Edit User", "default"));

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_users") && !is_profile_page) {
            gVars.submenu_file = "users.php";
        } else {
            gVars.submenu_file = "profile.php";
        }

        gVars.parent_file = "users.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this,
            new Array<Object>(new ArrayEntry<Object>("action"),
                new ArrayEntry<Object>("redirect"),
                new ArrayEntry<Object>("profile"),
                new ArrayEntry<Object>("user_id"),
                new ArrayEntry<Object>("wp_http_referer")));
        wp_http_referer = getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(
                new Array<Object>(new ArrayEntry<Object>("update"), new ArrayEntry<Object>("delete_count")),
                Strings.stripslashes(gVars.webEnv, wp_http_referer));
        gVars.user_id = intval(gVars.user_id);

        if (!booleanval(gVars.user_id)) {
            if (is_profile_page) {
                gVars.current_user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
                gVars.user_id = gVars.current_user.getID();
            } else {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Invalid user ID.", "default"), "");
            }
        }

        {
            int javaSwitchSelector30 = 0;

            if (equal(gVars.action, "switchposts")) {
                javaSwitchSelector30 = 1;
            }

            if (equal(gVars.action, "update")) {
                javaSwitchSelector30 = 2;
            }

            switch (javaSwitchSelector30) {
            case 1:/* TODO: Switch all posts from one user to another user */
             {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer(strval(-1), "_wpnonce");
                
                /* TODO: Switch all posts from one user to another user */

                break;
            }

            case 2: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-user_" + strval(gVars.user_id), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_user", gVars.user_id)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to edit this user.", "default"), "");
                }

                if (is_profile_page) {
                    getIncluded(PluginPage.class, gVars, gConsts).do_action("personal_options_update", "");
                }

                gVars.errors = getIncluded(UserPage.class, gVars, gConsts).edit_user(intval(gVars.user_id));

                if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.errors)) {
                    gVars.redirect = (is_profile_page
                        ? "profile.php?"
                        : ("user-edit.php?user_id=" + strval(gVars.user_id) + "&")) + "updated=true";
                    gVars.redirect = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("wp_http_referer", URL.urlencode(wp_http_referer), gVars.redirect);
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.redirect, 302);
                    System.exit();
                }
            }

            default: {
                profileuser = getIncluded(UserPage.class, gVars, gConsts).get_user_to_edit(gVars.user_id);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_user", gVars.user_id)) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to edit this user.", "default"), "");
                }

                include(gVars, gConsts, Admin_headerPage.class);
                echo(gVars.webEnv, "\n");

                if (isset(gVars.webEnv._GET.getValue("updated"))) {
                    echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\">\n\t<p><strong>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("User updated.", "default");
                    echo(gVars.webEnv, "</strong></p>\n\t");

                    if (booleanval(wp_http_referer) && !is_profile_page) {
                        echo(gVars.webEnv, "\t<p><a href=\"users.php\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("&laquo; Back to Authors and Users", "default");
                        echo(gVars.webEnv, "</a></p>\n\t");
                    } else {
                    }

                    echo(gVars.webEnv, "</div>\n");
                } else {
                }

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.errors)) {
                    echo(gVars.webEnv, "<div class=\"error\">\n\t<ul>\n\t");

                    for (Map.Entry javaEntry315 : ((WP_Error) gVars.errors).get_error_messages().entrySet()) {
                        gVars.message = strval(javaEntry315.getValue());
                        echo(gVars.webEnv, "<li>" + gVars.message + "</li>");
                    }

                    echo(gVars.webEnv, "\t</ul>\n</div>\n");
                } else {
                }

                echo(gVars.webEnv, "\n<div class=\"wrap\" id=\"profile-page\">\n<h2>");

                if (is_profile_page) {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Your Profile and Personal Options", "default");
                } else {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Edit User", "default");
                }

                echo(gVars.webEnv, "</h2>\n\n<form name=\"profile\" id=\"your-profile\" action=\"\" method=\"post\">\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-user_" + strval(gVars.user_id), "_wpnonce", true, true);

                if (booleanval(wp_http_referer)) {
                    echo(gVars.webEnv, "\t<input type=\"hidden\" name=\"wp_http_referer\" value=\"");
                    echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).clean_url(wp_http_referer, null, "display"));
                    echo(gVars.webEnv, "\" />\n");
                } else {
                }

                echo(gVars.webEnv, "<p>\n<input type=\"hidden\" name=\"from\" value=\"profile\" />\n<input type=\"hidden\" name=\"checkuser_id\" value=\"");
                echo(gVars.webEnv, gVars.user_ID);
                echo(gVars.webEnv, "\" />\n</p>\n\n<h3>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Personal Options", "default");
                echo(gVars.webEnv, "</h3>\n\n<table class=\"form-table\">\n");

                if (getIncluded(General_templatePage.class, gVars, gConsts).rich_edit_exists()) { // don't bother showing the option if the editor has been removed
                    echo(gVars.webEnv, "\t<tr>\n\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Visual Editor", "default");
                    echo(gVars.webEnv, "</th>\n\t\t<td><label for=\"rich_editing\"><input name=\"rich_editing\" type=\"checkbox\" id=\"rich_editing\" value=\"true\" ");
                    getIncluded(TemplatePage.class, gVars, gConsts).checked("true", profileuser.getRich_editing());
                    echo(gVars.webEnv, " /> ");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Use the visual editor when writing", "default");
                    echo(gVars.webEnv, "</label></td>\n\t</tr>\n");
                } else {
                }

                echo(gVars.webEnv, "<tr>\n<th scope=\"row\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Admin Color Scheme", "default");
                echo(gVars.webEnv, "</th>\n<td>\n");
                current_color = strval((((org.numiton.nwp.wp_includes.UserPage) getIncluded(org.numiton.nwp.wp_includes.UserPage.class, gVars, gConsts))).get_user_option("admin_color"));

                /*, 0*/
                if (empty(current_color)) {
                    current_color = "fresh";
                }

                for (Map.Entry javaEntry316 : gVars._wp_admin_css_colors.entrySet()) {
                    gVars.color = strval(javaEntry316.getKey());
                    color_info = (StdClass) javaEntry316.getValue();
                    echo(gVars.webEnv, "<div class=\"color-option\"><input name=\"admin_color\" id=\"admin_color_");
                    echo(gVars.webEnv, gVars.color);
                    echo(gVars.webEnv, "\" type=\"radio\" value=\"");
                    echo(gVars.webEnv, gVars.color);
                    echo(gVars.webEnv, "\" class=\"tog\" ");
                    getIncluded(TemplatePage.class, gVars, gConsts).checked(gVars.color, current_color);
                    echo(gVars.webEnv, " />\n\t<table class=\"color-palette\">\n\t<tr>\n\t");

                    for (Map.Entry javaEntry317 : (Set<Map.Entry>) color_info.fields.getArrayValue("colors").entrySet()) {
                        html_color = strval(javaEntry317.getValue());
                        echo(gVars.webEnv, "\t<td style=\"background-color: ");
                        echo(gVars.webEnv, html_color);
                        echo(gVars.webEnv, "\" title=\"");
                        echo(gVars.webEnv, gVars.color);
                        echo(gVars.webEnv, "\">&nbsp;</td>\n\t");
                    }

                    echo(gVars.webEnv, "\t</tr>\n\t</table>\n\t\n\t<label for=\"admin_color_");
                    echo(gVars.webEnv, gVars.color);
                    echo(gVars.webEnv, "\">");
                    echo(gVars.webEnv, StdClass.getValue(color_info, "name"));
                    echo(gVars.webEnv, "</label>\n</div>\n");
                }

                echo(gVars.webEnv, "</td>\n</tr>\n</table>\n\n");

                if (is_profile_page) {
                    getIncluded(PluginPage.class, gVars, gConsts).do_action("profile_personal_options", "");
                }

                echo(gVars.webEnv, "\n<h3>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");
                echo(gVars.webEnv, "</h3>\n\n<table class=\"form-table\">\n\t<tr>\n\t\t<th><label for=\"user_login\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Username", "default");
                echo(gVars.webEnv, "</label></th>\n\t\t<td><input type=\"text\" name=\"user_login\" id=\"user_login\" value=\"");
                echo(gVars.webEnv, profileuser.getUser_login());
                echo(gVars.webEnv, "\" disabled=\"disabled\" /> ");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Your username cannot be changed", "default");
                echo(gVars.webEnv, "</td>\n\t</tr>\n\n");

                if (!is_profile_page) {
                    echo(gVars.webEnv, "<tr><th><label for=\"role\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Role:", "default");
                    echo(gVars.webEnv, "</label></th>\n");
                    // print_r($profileuser);
                    echo(gVars.webEnv, "<td><select name=\"role\" id=\"role\">");
                    role_list = "";
                    user_has_role = false;

                    for (Map.Entry javaEntry318 : gVars.wp_roles.role_names.entrySet()) {
                        gVars.role = strval(javaEntry318.getKey());
                        gVars.name = strval(javaEntry318.getValue());
                        gVars.name = getIncluded(L10nPage.class, gVars, gConsts).translate_with_context(gVars.name, "default");

                        if (profileuser.has_cap(gVars.role)) {
                            gVars.selected = " selected=\"selected\"";
                            user_has_role = true;
                        } else {
                            gVars.selected = "";
                        }

                        role_list = strval(role_list) + "<option value=\"" + gVars.role + "\"" + strval(gVars.selected) + ">" + gVars.name + "</option>";
                    }

                    if (user_has_role) {
                        role_list = strval(role_list) + "<option value=\"\">" + getIncluded(L10nPage.class, gVars, gConsts).__("&mdash; No role for this blog &mdash;", "default") + "</option>";
                    } else {
                        role_list = strval(role_list) + "<option value=\"\" selected=\"selected\">" +
                            getIncluded(L10nPage.class, gVars, gConsts).__("&mdash; No role for this blog &mdash;", "default") + "</option>";
                    }

                    echo(gVars.webEnv, strval(role_list) + "</select></td></tr>");
                } else {
                }

                echo(gVars.webEnv, "\n<tr>\n\t<th><label for=\"first_name\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("First name", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"first_name\" id=\"first_name\" value=\"");
                echo(gVars.webEnv, profileuser.getFirst_name());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n\n<tr>\n\t<th><label for=\"last_name\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Last name", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"last_name\" id=\"last_name\" value=\"");
                echo(gVars.webEnv, profileuser.getLast_name());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n\n<tr>\n\t<th><label for=\"nickname\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Nickname", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"nickname\" id=\"nickname\" value=\"");
                echo(gVars.webEnv, profileuser.getNickname());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n\n<tr>\n\t<th><label for=\"display_name\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Display name publicly&nbsp;as", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td>\n\t\t<select name=\"display_name\" id=\"display_name\">\n\t\t");
                public_display = new Array<Object>();
                public_display.putValue(profileuser.getDisplay_name());
                public_display.putValue(profileuser.getNickname());
                public_display.putValue(profileuser.getUser_login());
                public_display.putValue(profileuser.getFirst_name());
                public_display.putValue(profileuser.getFirst_name() + " " + profileuser.getLast_name());
                public_display.putValue(profileuser.getLast_name() + " " + profileuser.getFirst_name());
                public_display = Array.array_unique(Array.array_filter(Array.array_map(new Callback("trim", Strings.class), public_display)));

                for (Map.Entry javaEntry319 : public_display.entrySet()) {
                    String item = strval(javaEntry319.getValue());
                    echo(gVars.webEnv, "\t\t\t<option value=\"");
                    echo(gVars.webEnv, item);
                    echo(gVars.webEnv, "\">");
                    echo(gVars.webEnv, item);
                    echo(gVars.webEnv, "</option>\n\t\t");
                }

                echo(gVars.webEnv, "\t\t</select>\n\t</td>\n</tr>\n</table>\n\n<h3>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Contact Info", "default");
                echo(gVars.webEnv, "</h3>\n\n<table class=\"form-table\">\n<tr>\n\t<th><label for=\"email\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"email\" id=\"email\" value=\"");
                echo(gVars.webEnv, profileuser.getUser_email());
                echo(gVars.webEnv, "\" /> ");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Required", "default");
                echo(gVars.webEnv, "</td>\n</tr>\n\n<tr>\n\t<th><label for=\"url\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Website", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"url\" id=\"url\" value=\"");
                echo(gVars.webEnv, profileuser.getUser_url());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n\n<tr>\n\t<th><label for=\"aim\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("AIM", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"aim\" id=\"aim\" value=\"");
                echo(gVars.webEnv, profileuser.getAim());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n\n<tr>\n\t<th><label for=\"yim\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Yahoo IM", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"yim\" id=\"yim\" value=\"");
                echo(gVars.webEnv, profileuser.getYim());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n\n<tr>\n\t<th><label for=\"jabber\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Jabber / Google Talk", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"text\" name=\"jabber\" id=\"jabber\" value=\"");
                echo(gVars.webEnv, profileuser.getJabber());
                echo(gVars.webEnv, "\" /></td>\n</tr>\n</table>\n\n<h3>");

                if (is_profile_page) {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("About Yourself", "default");
                } else {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("About the user", "default");
                }

                echo(gVars.webEnv, "</h3>\n\n<table class=\"form-table\">\n<tr>\n\t<th><label for=\"description\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Biographical Info", "default");
                echo(gVars.webEnv, "</label></th>\n\t<td><textarea name=\"description\" id=\"description\" rows=\"5\" cols=\"30\">");
                echo(gVars.webEnv, profileuser.getDescription());
                echo(gVars.webEnv, "</textarea><br />");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Share a little biographical information to fill out your profile. This may be shown publicly.", "default");
                echo(gVars.webEnv, "</td>\n</tr>\n\n");
                show_password_fields = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("show_password_fields", true);

                if (booleanval(show_password_fields)) {
                    echo(gVars.webEnv, "<tr>\n\t<th><label for=\"pass1\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("New Password:", "default");
                    echo(gVars.webEnv, "</label></th>\n\t<td><input type=\"password\" name=\"pass1\" id=\"pass1\" size=\"16\" value=\"\" /> ");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("If you would like to change the password type a new one. Otherwise leave this blank.", "default");
                    echo(gVars.webEnv, "<br />\n\t\t<input type=\"password\" name=\"pass2\" id=\"pass2\" size=\"16\" value=\"\" /> ");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Type your new password again.", "default");
                    echo(gVars.webEnv, "<br />\n\t\t");

                    if (is_profile_page) {
                        echo(gVars.webEnv, "\t\t<p><strong>");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("Password Strength:", "default");
                        echo(gVars.webEnv, "</strong></p>\n\t\t<div id=\"pass-strength-result\">");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("Too short", "default");
                        echo(gVars.webEnv, "</div> ");
                        getIncluded(L10nPage.class, gVars, gConsts)._e("Hint: Use upper and lower case characters, numbers and symbols like !\"?$%^&amp;( in your password.", "default");
                        echo(gVars.webEnv, "\t\t");
                    } else {
                    }

                    echo(gVars.webEnv, "\t</td>\n</tr>\n");
                } else {
                }

                echo(gVars.webEnv, "</table>\n\n");

                if (is_profile_page) {
                    getIncluded(PluginPage.class, gVars, gConsts).do_action("show_user_profile", "");
                } else {
                    getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_user_profile", "");
                }

                echo(gVars.webEnv, "\n");

                if (Array.count(profileuser.getCaps()) > Array.count(profileuser.getRoles())) {
                    echo(
                        gVars.webEnv,
                        "<br class=\"clear\" />\n\t<table width=\"99%\" style=\"border: none;\" cellspacing=\"2\" cellpadding=\"3\" class=\"editform\">\n\t\t<tr>\n\t\t\t<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Additional Capabilities:", "default");
                    echo(gVars.webEnv, "</th>\n\t\t\t<td>");
                    gVars.output = "";

                    for (Map.Entry javaEntry320 : (Set<Map.Entry>) ((Array) profileuser.getCaps()).entrySet()) {
                        cap = javaEntry320.getKey();
                        gVars.value = javaEntry320.getValue();

                        if (!gVars.wp_roles.is_role(cap)) {
                            if (!equal(gVars.output, "")) {
                                gVars.output = strval(gVars.output) + ", ";
                            }

                            gVars.output = strval(gVars.output) + (booleanval(gVars.value)
                                ? strval(cap)
                                : ("Denied: " + strval(cap)));
                        }
                    }

                    echo(gVars.webEnv, gVars.output);
                    echo(gVars.webEnv, "</td>\n\t\t</tr>\n\t</table>\n");
                } else {
                }

                echo(gVars.webEnv, "\n<p class=\"submit\">\n\t<input type=\"hidden\" name=\"action\" value=\"update\" />\n\t<input type=\"hidden\" name=\"user_id\" id=\"user_id\" value=\"");
                echo(gVars.webEnv, gVars.user_id);
                echo(gVars.webEnv, "\" />\n\t<input type=\"submit\" value=\"");

                if (is_profile_page) {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Update Profile", "default");
                } else {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Update User", "default");
                }

                echo(gVars.webEnv, "\" name=\"submit\" />\n </p>\n</form>\n</div>\n");

                break;
            }
            }
        }

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
