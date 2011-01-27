/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Script_loaderPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Script_loaderPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Script_loaderPage.class.getName());
    public WP_Scripts wp_scripts;

    @Override
    @RequestMapping("/wp-includes/script-loader.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/script_loader";
    }

    /**
     * Prints script tags in document head
     * Called by admin-header.php and by wp_head hook. Since it is called by
     * wp_head on every page load, the function does not instantiate the
     * WP_Scripts object unless script names are explicitly passed. Does make
     * use of already instantiated $wp_scripts if present. Use provided
     * wp_print_scripts hook to register/enqueue new scripts.
     * @see WP_Scripts::print_scripts()
     */
    public Array<Object> wp_print_scripts(Object handles) {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_print_scripts", "");

        if (equal("", handles)) { // for wp_head
            handles = "";
        }

        if (!ClassHandling.is_a(wp_scripts, "WP_Scripts")) {
            if (!booleanval(handles)) {
                return new Array<Object>(); // No need to instantiate if nothing's there.
            } else {
                wp_scripts = new WP_Scripts(gVars, gConsts);
            }
        }

        return wp_scripts.print_scripts(handles);
    }

    public void wp_register_script(Object handle, Object src, Object deps, Object ver) {
        if (!ClassHandling.is_a(wp_scripts, "WP_Scripts")) {
            wp_scripts = new WP_Scripts(gVars, gConsts);
        }

        wp_scripts.add(handle, src, deps, ver);
    }

    /**
     * Localizes a script
     * Localizes only if script has already been added
     * @see WP_Script::localize()
     */
    public boolean wp_localize_script(String handle, String object_name, Array<Object> l10n) {
        if (!ClassHandling.is_a(wp_scripts, "WP_Scripts")) {
            return false;
        }

        return wp_scripts.localize(handle, object_name, l10n);
    }

    public void wp_deregister_script(Object handle) {
        if (!ClassHandling.is_a(wp_scripts, "WP_Scripts")) {
            wp_scripts = new WP_Scripts(gVars, gConsts);
        }

        wp_scripts.remove(handle);
    }

    /**
     * Equeues script
     * Registers the script if src provided (does NOT overwrite) and enqueues.
     * @see WP_Script::add(), WP_Script::enqueue()
     */
    public void wp_enqueue_script(Object handle, boolean src, Array<Object> deps, boolean ver) {
        Array<String> _handle = new Array<String>();

        if (!ClassHandling.is_a(wp_scripts, "WP_Scripts")) {
            wp_scripts = new WP_Scripts(gVars, gConsts);
        }

        if (src) {
            _handle = Strings.explode("?", strval(handle));
            wp_scripts.add(_handle.getValue(0), src, deps, ver);
        }

        wp_scripts.enqueue(new Array(handle));
    }

    public Array<Object> wp_prototype_before_jquery(Array js_array) {
        Object jquery;
        Object prototype = null;

        if (strictEqual(null, jquery = Array.array_search("jquery", js_array))) {
            return js_array;
        }

        if (strictEqual(null, prototype = Array.array_search("prototype", js_array))) {
            return js_array;
        }

        if (intval(prototype) < intval(jquery)) {
            return js_array;
        }

        js_array.arrayUnset(prototype);
        Array.array_splice(js_array, intval(jquery), 0, "prototype");

        return js_array;
    }

 // These localizations require information that may not be loaded even by init
    public void wp_just_in_time_script_localization() {
        wp_localize_script("tiny_mce", "wpTinyMCEConfig", new Array<Object>(new ArrayEntry<Object>("defaultEditor", getIncluded(General_templatePage.class, gVars, gConsts).wp_default_editor())));
        wp_localize_script("autosave", "autosaveL10n",
            new Array<Object>(new ArrayEntry<Object>("autosaveInterval", gConsts.getAUTOSAVE_INTERVAL()),
                new ArrayEntry<Object>("previewPageText", getIncluded(L10nPage.class, gVars, gConsts).__("Preview this Page", "default")),
                new ArrayEntry<Object>("previewPostText", getIncluded(L10nPage.class, gVars, gConsts).__("Preview this Post", "default")),
                new ArrayEntry<Object>("requestFile", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/admin-ajax.php"),
                new ArrayEntry<Object>("savingText", getIncluded(L10nPage.class, gVars, gConsts).__("Saving Draft&#8230;", "default"))));
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_script_loader_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("wp_print_scripts", Callback.createCallbackArray(this, "wp_just_in_time_script_localization"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("print_scripts_array", Callback.createCallbackArray(this, "wp_prototype_before_jquery"), 10, 1);

        return DEFAULT_VAL;
    }
}
