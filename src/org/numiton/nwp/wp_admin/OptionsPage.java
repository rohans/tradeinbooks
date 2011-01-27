/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: OptionsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class OptionsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(OptionsPage.class.getName());
    public Object any_changed;
    public Array<String> options = new Array<String>();
    public StdClass option;
    public Object disabled;
    public Array<String> options_to_update = new Array<String>();

    @Override
    @RequestMapping("/wp-admin/options.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Settings", "default");
        gVars.this_file = "options.php";
        gVars.parent_file = "options-general.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action")));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
        }

        {
            int javaSwitchSelector24 = 0;

            if (equal(gVars.action, "update")) {
                javaSwitchSelector24 = 1;
            }

            switch (javaSwitchSelector24) {
            case 1: {
                any_changed = 0;
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-options", "_wpnonce");

                if (!booleanval(gVars.webEnv._POST.getValue("page_options"))) {
                    for (Map.Entry javaEntry295 : new Array<Object>(gVars.webEnv._POST).entrySet()) {
                        gVars.key = strval(javaEntry295.getKey());
                        gVars.value = javaEntry295.getValue();

                        if (!Array.in_array(gVars.key, new Array<Object>(new ArrayEntry<Object>("_wpnonce"), new ArrayEntry<Object>("_wp_http_referer")))) {
                            options.putValue(gVars.key);
                        }
                    }
                } else {
                    options = Strings.explode(",", Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("page_options"))));
                }

                if (booleanval(options)) {
                    for (Map.Entry javaEntry296 : options.entrySet()) {
                        String optionStr = strval(javaEntry296.getValue());
                        optionStr = Strings.trim(optionStr);
                        gVars.value = gVars.webEnv._POST.getValue(optionStr);

                        if (!is_array(gVars.value)) {
                            gVars.value = Strings.trim(strval(gVars.value));
                        }

                        gVars.value = getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(gVars.value);
                        getIncluded(FunctionsPage.class, gVars, gConsts).update_option(optionStr, gVars.value);
                    }
                }

                gVars.goback = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("updated", "true", getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.goback, 302);

                break;
            }

            default: {
                include(gVars, gConsts, Admin_headerPage.class);
                echo(gVars.webEnv, "\n<div class=\"wrap\">\n  <h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("All Settings", "default");
                echo(gVars.webEnv, "</h2>\n  <form name=\"form\" action=\"options.php\" method=\"post\" id=\"all-options\">\n  ");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);
                echo(gVars.webEnv, "  <input type=\"hidden\" name=\"action\" value=\"update\" />\n  <table class=\"form-table\">\n");
                options = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.options + " ORDER BY option_name");

                for (Map.Entry javaEntry297 : new Array<Object>(options).entrySet()) {
                    option = (StdClass) javaEntry297.getValue();
                    disabled = "";
                    option.fields.putValue("option_name", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(option, "option_name"))));

                    if (getIncluded(FunctionsPage.class, gVars, gConsts).is_serialized(StdClass.getValue(option, "option_value"))) {
                        if (getIncluded(FunctionsPage.class, gVars, gConsts).is_serialized_string(StdClass.getValue(option, "option_value"))) {
                        	// this is a serialized string, so we should display it
                            gVars.value = getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(StdClass.getValue(option, "option_value"));
                            options_to_update.putValue(StdClass.getValue(option, "option_name"));
                            gVars._class = "all-options";
                        } else {
                            gVars.value = "SERIALIZED DATA";
                            disabled = " disabled=\"disabled\"";
                            gVars._class = "all-options disabled";
                        }
                    } else {
                        gVars.value = StdClass.getValue(option, "option_value");
                        options_to_update.putValue(StdClass.getValue(option, "option_name"));
                        gVars._class = "all-options";
                    }

                    echo(gVars.webEnv, "\n<tr>\n\t<th scope=\'row\'>" + StdClass.getValue(option, "option_name") + "</th>\n<td>");

                    if (!strictEqual(Strings.strpos(strval(gVars.value), "\n"), BOOLEAN_FALSE)) {
                        echo(
                                gVars.webEnv,
                                "<textarea class=\'" + gVars._class + "\' name=\'" + StdClass.getValue(option, "option_name") + "\' id=\'" + StdClass.getValue(option, "option_name") +
                                "\' cols=\'30\' rows=\'5\'>" + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.value), strval(0)) + "</textarea>");
                    } else {
                        echo(
                                gVars.webEnv,
                                "<input class=\'" + gVars._class + "\' type=\'text\' name=\'" + StdClass.getValue(option, "option_name") + "\' id=\'" + StdClass.getValue(option, "option_name") +
                                "\' size=\'30\' value=\'" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.value)) + "\'" + disabled + " />");
                    }

                    echo(gVars.webEnv, "</td>\n</tr>");
                }

                echo(gVars.webEnv, "  </table>\n");

                String options_to_updateStr = Strings.implode(",", options_to_update);
                echo(gVars.webEnv, "<p class=\"submit\"><input type=\"hidden\" name=\"page_options\" value=\"");
                echo(gVars.webEnv, options_to_updateStr);
                echo(gVars.webEnv, "\" /><input type=\"submit\" name=\"Update\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");
                echo(gVars.webEnv, "\" /></p>\n  </form>\n</div>\n\n\n");

                break;
            }
            } // end switch
        }

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
