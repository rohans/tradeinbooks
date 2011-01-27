/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MiscPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class MiscPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(MiscPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/misc.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/misc";
    }

    public Object got_mod_rewrite() {
        boolean got_rewrite = getIncluded(FunctionsPage.class, gVars, gConsts).apache_mod_loaded("mod_rewrite", true);

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("got_rewrite", got_rewrite);
    }

    // Returns an array of strings from a file (.htaccess ) from between BEGIN
    // and END markers.
    public Array<String> extract_from_markers(String filename, String marker) {
        Array<String> result = new Array<String>();
        Array<String> markerdata = new Array<String>();
        boolean state = false;
        String markerline = null;

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, filename)) {
            return result;
        }

        if (booleanval(markerdata = Strings.explode("\n", Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, filename))))) {
        }

        state = false;

        for (Map.Entry javaEntry167 : markerdata.entrySet()) {
            markerline = strval(javaEntry167.getValue());

            if (!strictEqual(Strings.strpos(markerline, "# END " + marker), BOOLEAN_FALSE)) {
                state = false;
            }

            if (state) {
                result.putValue(markerline);
            }

            if (!strictEqual(Strings.strpos(markerline, "# BEGIN " + marker), BOOLEAN_FALSE)) {
                state = true;
            }
        }

        return result;
    }

    // Inserts an array of strings into a file (.htaccess ), placing it between
    // BEGIN and END markers.  Replaces existing marked info.  Retains surrounding
    // data.  Creates file if none exists.
    // Returns true on write success, false on failure.
    public boolean insert_with_markers(String filename, String marker, Object insertion)/* Do not change type */
     {
        Array<String> markerdata;
        int f = 0;
        boolean foundit = false;
        boolean state = false;
        String markerline = null;
        Object n = null;
        Object insertline = null;

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, filename) || FileSystemOrSocket.is_writeable(gVars.webEnv, filename)) {
            if (!FileSystemOrSocket.file_exists(gVars.webEnv, filename)) {
                markerdata = new Array<String>();
            } else {
                markerdata = Strings.explode("\n", Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, filename)));
            }

            f = FileSystemOrSocket.fopen(gVars.webEnv, filename, "w");
            foundit = false;

            if (booleanval(markerdata)) {
                state = true;

                for (Map.Entry javaEntry168 : markerdata.entrySet()) {
                    n = javaEntry168.getKey();
                    markerline = strval(javaEntry168.getValue());

                    if (!strictEqual(Strings.strpos(markerline, "# BEGIN " + marker), BOOLEAN_FALSE)) {
                        state = false;
                    }

                    if (state) {
                        if ((intval(n) + 1) < Array.count(markerdata)) {
                            FileSystemOrSocket.fwrite(gVars.webEnv, f, markerline + "\n");
                        } else {
                            FileSystemOrSocket.fwrite(gVars.webEnv, f, markerline);
                        }
                    }

                    if (!strictEqual(Strings.strpos(markerline, "# END " + marker), BOOLEAN_FALSE)) {
                        FileSystemOrSocket.fwrite(gVars.webEnv, f, "# BEGIN " + marker + "\n");

                        if (is_array(insertion)) {
                            for (Map.Entry javaEntry169 : ((Array<?>) insertion).entrySet()) {
                                insertline = javaEntry169.getValue();
                                FileSystemOrSocket.fwrite(gVars.webEnv, f, strval(insertline) + "\n");
                            }
                        }

                        FileSystemOrSocket.fwrite(gVars.webEnv, f, "# END " + marker + "\n");
                        state = true;
                        foundit = true;
                    }
                }
            }

            if (!foundit) {
                FileSystemOrSocket.fwrite(gVars.webEnv, f, "# BEGIN " + marker + "\n");

                for (Map.Entry javaEntry170 : ((Array<?>) insertion).entrySet()) {
                    insertline = javaEntry170.getValue();
                    FileSystemOrSocket.fwrite(gVars.webEnv, f, strval(insertline) + "\n");
                }

                FileSystemOrSocket.fwrite(gVars.webEnv, f, "# END " + marker + "\n");
            }

            FileSystemOrSocket.fclose(gVars.webEnv, f);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the htaccess file with the current rules if it is writable.
     *
     * Always writes to the file if it exists and is writable to ensure that we blank out old rules.
     */
    public boolean save_mod_rewrite_rules() {
        String home_path;
        String htaccess_file = null;
        Array<String> rules = new Array<String>();
        home_path = getIncluded(FilePage.class, gVars, gConsts).get_home_path();
        htaccess_file = home_path + ".htaccess";

        // If the file doesn't already exists check for write access to the directory and whether of not we have some rules.
        // else check for write access to the file.
        if ((!FileSystemOrSocket.file_exists(gVars.webEnv, htaccess_file) && FileSystemOrSocket.is_writable(gVars.webEnv, home_path) && gVars.wp_rewrite.using_mod_rewrite_permalinks()) ||
                FileSystemOrSocket.is_writable(gVars.webEnv, htaccess_file)) {
            if (booleanval(got_mod_rewrite())) {
                rules = Strings.explode("\n", gVars.wp_rewrite.mod_rewrite_rules());

                return insert_with_markers(htaccess_file, "nWordPress", rules);
            }
        }

        return false;
    }

    public void update_recently_edited(String file) {
        Array<Object> oldfiles = new Array<Object>();
        oldfiles = new Array<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("recently_edited"));

        if (booleanval(oldfiles)) {
            oldfiles = Array.array_reverse(oldfiles);
            oldfiles.putValue(file);
            oldfiles = Array.array_reverse(oldfiles);
            oldfiles = Array.array_unique(oldfiles);

            if (5 < Array.count(oldfiles)) {
                Array.array_pop(oldfiles);
            }
        } else {
            oldfiles.putValue(file);
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("recently_edited", oldfiles);
    }

    // If siteurl or home changed, flush rewrite rules.
    public void update_home_siteurl(Object old_value, Object value) {
        if (gConsts.isWP_INSTALLINGDefined()) {
            return;
        }

        // If home changed, write rewrite rules to new location.
        gVars.wp_rewrite.flush_rules();
    }

    public String url_shorten(String url) {
        String short_url = null;
        short_url = Strings.str_replace("http://", "", Strings.stripslashes(gVars.webEnv, url));
        short_url = Strings.str_replace("www.", "", short_url);

        if (equal("/", Strings.substr(short_url, -1))) {
            short_url = Strings.substr(short_url, 0, -1);
        }

        if (Strings.strlen(short_url) > 35) {
            short_url = Strings.substr(short_url, 0, 32) + "...";
        }

        return short_url;
    }

    /**
     * Modified by Numiton
     * Processes each field name from vars. If field value is not set, try to set it from _GET or _POST.
     * @param callerInstance
     * @param vars
     */
    public void wp_reset_vars(ContextCarrierInterface callerInstance, Array<Object> vars) {
        String var = null;
        int i = 0;

        for (i = 0; i < Array.count(vars); i = i + 1) {
            var = strval(vars.getValue(i));

            // Look it up in GlobalVars
            Field field = null;
            Object instance = null;

            try {
                field = callerInstance.getGlobalVars().getClass().getField(var);
                instance = callerInstance.getGlobalVars();
            } catch (NoSuchFieldException ex) {
                // Look it up in the caller instance
                try {
                    field = callerInstance.getClass().getField(var);
                    instance = callerInstance;
                } catch (NoSuchFieldException ex2) {
                    //					LOG.warn("Could not find field '" + var + "' in GlobalVars or caller instance " + callerInstance.getClass());
                    continue;
                }
            }

            try {
                if (!isset(field.get(instance))) {
                    if (empty(gVars.webEnv._POST.getValue(var))) {
                        if (empty(gVars.webEnv._GET.getValue(var))) {
                            setDefaultFieldValue(field, instance);
                        } else {
                            field.set(instance, gVars.webEnv._GET.getValue(var));
                        }
                    } else {
                        field.set(instance, gVars.webEnv._POST.getValue(var));
                    }
                }
            } catch (Exception ex) {
                LOG.warn("Could not access field '" + var + "'");

                continue;
            }
        }
    }

    protected void setDefaultFieldValue(Field field, Object instance)
        throws IllegalAccessException {
        if (field.getClass().equals(Array.class)) {
            field.set(instance, new Array());
        } else if (field.getClass().equals(Ref.class)) {
            field.set(instance, new Ref());
        } else {
            field.set(instance, null);
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_misc_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_action("update_option_home", Callback.createCallbackArray(this, "update_home_siteurl"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("update_option_siteurl", Callback.createCallbackArray(this, "update_home_siteurl"), 10, 2);

        return DEFAULT_VAL;
    }
}
