/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_generalPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Options_generalPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_generalPage.class.getName());
    public Object current_offset;
    public Array<Object> offset_range;
    public String offset_name;
    public Object current_offset_name;
    public int day_index;

    @Override
    @RequestMapping("/wp-admin/options-general.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_general";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_general_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("General Settings", "default");
        gVars.parent_file = "options-general.php";
        include(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("General Settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Blog Title", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block5");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("blogname");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Tagline", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block7");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("blogdescription");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("In a few words, explain what this blog is about.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress address (URL)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block10");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("siteurl");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block11");

        if (gConsts.isWP_SITEURLDefined()) {
            echo(gVars.webEnv, " disabled\" disabled=\"disabled\"");
        } else {
            echo(gVars.webEnv, "\"");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Blog address (URL)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block13");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("home");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block14");

        if (gConsts.isWP_HOMEDefined()) {
            echo(gVars.webEnv, " disabled\" disabled=\"disabled\"");
        } else {
            echo(gVars.webEnv, "\"");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Enter the address here if you want your blog homepage <a href=\"http://codex.wordpress.org/Giving_WordPress_Its_Own_Directory\">to be different from the directory</a> you installed nWordPress.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail address", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block17");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("admin_email");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("This address is used for admin purposes, like new user notification.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Membership", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block20");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("users_can_register")));

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Anyone can register", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block22");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_registration")));

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block23");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Users must be registered and logged in to comment", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block24");
        getIncluded(L10nPage.class, gVars, gConsts)._e("New User Default Role", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block25");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_dropdown_roles(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_role"));

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Timezone", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block27");
        current_offset = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset");
        offset_range = new Array<Object>(
                new ArrayEntry<Object>(-12),
                new ArrayEntry<Object>(-11.5),
                new ArrayEntry<Object>(-11),
                new ArrayEntry<Object>(-10.5),
                new ArrayEntry<Object>(-10),
                new ArrayEntry<Object>(-9.5),
                new ArrayEntry<Object>(-9),
                new ArrayEntry<Object>(-8.5),
                new ArrayEntry<Object>(-8),
                new ArrayEntry<Object>(-7.5),
                new ArrayEntry<Object>(-7),
                new ArrayEntry<Object>(-6.5),
                new ArrayEntry<Object>(-6),
                new ArrayEntry<Object>(-5.5),
                new ArrayEntry<Object>(-5),
                new ArrayEntry<Object>(-4.5),
                new ArrayEntry<Object>(-4),
                new ArrayEntry<Object>(-3.5),
                new ArrayEntry<Object>(-3),
                new ArrayEntry<Object>(-2.5),
                new ArrayEntry<Object>(-2),
                new ArrayEntry<Object>(-1.5),
                new ArrayEntry<Object>(-1),
                new ArrayEntry<Object>(-0.5),
                new ArrayEntry<Object>(0),
                new ArrayEntry<Object>(0.5),
                new ArrayEntry<Object>(1),
                new ArrayEntry<Object>(1.5),
                new ArrayEntry<Object>(2),
                new ArrayEntry<Object>(2.5),
                new ArrayEntry<Object>(3),
                new ArrayEntry<Object>(3.5),
                new ArrayEntry<Object>(4),
                new ArrayEntry<Object>(4.5),
                new ArrayEntry<Object>(5),
                new ArrayEntry<Object>(5.5),
                new ArrayEntry<Object>(5.75),
                new ArrayEntry<Object>(6),
                new ArrayEntry<Object>(6.5),
                new ArrayEntry<Object>(7),
                new ArrayEntry<Object>(7.5),
                new ArrayEntry<Object>(8),
                new ArrayEntry<Object>(8.5),
                new ArrayEntry<Object>(8.75),
                new ArrayEntry<Object>(9),
                new ArrayEntry<Object>(9.5),
                new ArrayEntry<Object>(10),
                new ArrayEntry<Object>(10.5),
                new ArrayEntry<Object>(11),
                new ArrayEntry<Object>(11.5),
                new ArrayEntry<Object>(12),
                new ArrayEntry<Object>(12.75),
                new ArrayEntry<Object>(13),
                new ArrayEntry<Object>(13.75),
                new ArrayEntry<Object>(14));

        for (Map.Entry javaEntry291 : offset_range.entrySet()) {
            gVars.offset = javaEntry291.getValue();

            if (0 < intval(gVars.offset)) {
                offset_name = "+" + strval(gVars.offset);
            } else if (equal(0, gVars.offset)) {
                offset_name = "";
            } else {
                offset_name = strval(gVars.offset);
            }

            offset_name = Strings.str_replace(
                    new Array<String>(new ArrayEntry<String>(".25"), new ArrayEntry<String>(".5"), new ArrayEntry<String>(".75")),
                    new Array<String>(new ArrayEntry<String>(":15"), new ArrayEntry<String>(":30"), new ArrayEntry<String>(":45")),
                    offset_name);
            gVars.selected = "";

            if (equal(current_offset, gVars.offset)) {
                gVars.selected = " selected=\'selected\'";
                current_offset_name = offset_name;
            }

            echo(
                    gVars.webEnv,
                    "<option value=\"" + strval(gVars.offset) + "\"" + strval(gVars.selected) + ">" +
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("UTC %s", "default"), offset_name) + "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("hours", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block29");
        QStrings.printf(
            gVars.webEnv,
            getIncluded(L10nPage.class, gVars, gConsts).__("<abbr title=\"Coordinated Universal Time\">UTC</abbr> time is <code>%s</code>", "default"),
            DateTime.gmdate(getIncluded(L10nPage.class, gVars, gConsts).__("Y-m-d G:i:s", "default")));

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block30");

        if (booleanval(current_offset)) {
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__("UTC %1$s is <code>%2$s</code>", "default"),
                current_offset_name,
                DateTime.gmdate(getIncluded(L10nPage.class, gVars, gConsts).__("Y-m-d G:i:s", "default"), intval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("timestamp", 0))));
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block31");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Unfortunately, you have to manually update this for Daylight Savings Time. Lame, we know, but will be fixed in the future.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block32");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Date Format", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block33");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("date_format");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block34");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Output:", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block35");
        echo(
            gVars.webEnv,
            getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0)), true));

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block36");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Time Format", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block37");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("time_format");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block38");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Output:", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block39");
        echo(
            gVars.webEnv,
            DateTime.gmdate(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")), intval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("timestamp", 0))));

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block40");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "<a href=\"http://codex.wordpress.org/Formatting_Date_and_Time\">Documentation on date formatting</a>. Click \"Save Changes\" to update sample output.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block41");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Week Starts On", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block42");

        for (day_index = 0; day_index <= 6; day_index++) {
            gVars.selected = (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("start_of_week"), day_index)
                ? "selected=\"selected\""
                : "");
            echo(gVars.webEnv, "\n\t<option value=\'" + strval(day_index) + "\' " + strval(gVars.selected) + ">" + gVars.wp_locale.get_weekday(day_index) + "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block43");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block44");

        if (!gConsts.isWP_SITEURLDefined()) {
            echo(gVars.webEnv, "siteurl,");
        }

        if (!gConsts.isWP_HOMEDefined()) {
            echo(gVars.webEnv, "home,");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_general_block45");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
