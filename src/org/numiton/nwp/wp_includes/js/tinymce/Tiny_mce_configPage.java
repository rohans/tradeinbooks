/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Tiny_mce_configPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce;

import static com.numiton.PhpCommonConstants.*;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.Wp_configPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.js.tinymce.langs.Wp_langsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.gzip.GZIP;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Tiny_mce_configPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Tiny_mce_configPage.class.getName());
    public boolean https;
    public String baseurl;
    public String mce_css;
    public Object mce_locale;
    public Object mce_spellchecker_languages;
    public Array<Object> mce_external_plugins;
    public Object ext_plugins;
    public Array<Object> mce_external_languages;
    public Array<Object> loaded_langs = new Array<Object>();
    public Object plugurl;
    public Array<String> mce_buttons;
    public Array<String> mce_buttons_2;
    public Array<String> mce_buttons_3;
    public Array<String> mce_buttons_4;
    public Array<Object> initArray = new Array<Object>();
    public String mce_deprecated;
    public Object disk_cache;
    public boolean compress;
    public int old_cache_max;
    public int msie;
    public int ie_ver;
    public String cache_path;
    public String cache_ext;
    public String cacheKey;
    public String mce_options;
    public Object n;
    public String cache_file;
    public int expiresOffset;
    public Object mtime;
    public Object k;
    public Array<Object> old_cache = new Array<Object>();
    public Object saved;
    public Array<Object> del_cache;

    @Override
    @RequestMapping("/wp-includes/js/tinymce/tiny_mce_config.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/tiny_mce_config";
    }

    public String getFileContents(String path) {
        String content = null;
        int fp = 0;

        if (true)/*Modified by Numiton*/
         {
            path = FileSystemOrSocket.realpath(gVars.webEnv, path);
        }

        if (!booleanval(path) || !FileSystemOrSocket.is_file(gVars.webEnv, path)) {
            return "";
        }

        if (true)/*Modified by Numiton*/
         {
            return FileSystemOrSocket.file_get_contents(gVars.webEnv, path);
        }

        content = "";
        fp = FileSystemOrSocket.fopen(gVars.webEnv, path, "r");

        if (!booleanval(fp)) {
            return "";
        }

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp))
            content = content + FileSystemOrSocket.fgets(gVars.webEnv, fp, 0);

        FileSystemOrSocket.fclose(gVars.webEnv, fp);

        return content;
    }

    public int putFileContents(String path, String content) {
        int newfile = 0;
        int fp = 0;

        if (true)/*Modified by Numiton*/
         {
            return FileSystemOrSocket.file_put_contents(gVars.webEnv, path, content);
        }

        newfile = intval(false);
        fp = FileSystemOrSocket.fopen(gVars.webEnv, path, "wb");

        if (booleanval(fp)) {
            newfile = FileSystemOrSocket.fwrite(gVars.webEnv, fp, content);
            FileSystemOrSocket.fclose(gVars.webEnv, fp);
        }

        return newfile;
    }

    @SuppressWarnings("unchecked")
    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_tiny_mce_config_block1");
        gVars.webEnv = webEnv;

        // some code below is from:
        /**
         * $Id: Tiny_mce_configPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
         *
         * @author Moxiecode
         * @copyright Copyright © 2005-2006, Moxiecode Systems AB, All rights reserved.
         *
         * This file compresses the TinyMCE JavaScript using GZip.
         **/

        // Discard any buffers
        while (OutputControl.ob_end_clean(gVars.webEnv)) {
        }

        require(gVars, gConsts, Wp_configPage.class);
        
        // Set up init variables
        https = ((isset(gVars.webEnv.getHttps()) && equal("on", Strings.strtolower(gVars.webEnv.getHttps())))
            ? true
            : false);
        baseurl = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl")) + "/wp-includes/js/tinymce";

        if (https) {
            baseurl = Strings.str_replace("http://", "https://", baseurl);
        }

        mce_css = baseurl + "/wordpress.css";
        mce_css = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_css", mce_css));

        if (https) {
            mce_css = Strings.str_replace("http://", "https://", mce_css);
        }

        mce_locale = (equal("", getIncluded(L10nPage.class, gVars, gConsts).get_locale())
            ? "en"
            : Strings.strtolower(Strings.substr(getIncluded(L10nPage.class, gVars, gConsts).get_locale(), 0, 2))); // only ISO 639-1

        /*
        The following filter allows localization scripts to change the languages displayed in the spellchecker's drop-down menu.
        By default it uses Google's spellchecker API, but can be configured to use PSpell/ASpell if installed on the server.
        The + sign marks the default language. More information:
        http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/spellchecker
        */
        mce_spellchecker_languages = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "mce_spellchecker_languages",
                "+English=en,Danish=da,Dutch=nl,Finnish=fi,French=fr,German=de,Italian=it,Polish=pl,Portuguese=pt,Spanish=es,Swedish=sv");
        
        gVars.plugins = new Array<String>(
                new ArrayEntry<String>("safari"),
                new ArrayEntry<String>("inlinepopups"),
                new ArrayEntry<String>("autosave"),
                new ArrayEntry<String>("spellchecker"),
                new ArrayEntry<String>("paste"),
                new ArrayEntry<String>("wordpress"),
                new ArrayEntry<String>("media"),
                new ArrayEntry<String>("fullscreen"));
        
        /* 
        The following filter takes an associative array of external plugins for TinyMCE in the form 'plugin_name' => 'url'.
        It adds the plugin's name to TinyMCE's plugins init and the call to PluginManager to load the plugin. 
        The url should be absolute and should include the js file name to be loaded. Example: 
        array( 'myplugin' => 'http://my-site.com/wp-content/plugins/myfolder/mce_plugin.js' )
        If the plugin uses a button, it should be added with one of the "$mce_buttons" filters.
        */
        mce_external_plugins = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_external_plugins", new Array<Object>());
        
        ext_plugins = "\n";

        if (!empty(mce_external_plugins)) {
        	/*
        	The following filter loads external language files for TinyMCE plugins.
        	It takes an associative array 'plugin_name' => 'path', where path is the 
        	include path to the file. The language file should follow the same format as 
        	/tinymce/langs/wp-langs.php and should define a variable $strings that 
        	holds all translated strings. Example: 
        	$strings = 'tinyMCE.addI18n("' . $mce_locale . '.mypluginname_dlg",{tab_general:"General", ... })';
        	*/
            mce_external_languages = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_external_languages", new Array<Object>());
            
            loaded_langs = new Array<Object>();
            gVars.strings = "";

            if (!empty(mce_external_languages)) {
                for (Map.Entry javaEntry493 : mce_external_languages.entrySet()) {
                    gVars.name = strval(javaEntry493.getKey());
                    gVars.path = strval(javaEntry493.getValue());

                    if (FileSystemOrSocket.is_file(gVars.webEnv, gVars.path) && FileSystemOrSocket.is_readable(gVars.webEnv, gVars.path)) {
                        /* Commented by Numiton: unresolved dynamic construct: 387705 */
                        ext_plugins = strval(ext_plugins) + gVars.strings;
                        loaded_langs.putValue(gVars.name);
                    }
                }
            }

            for (Map.Entry javaEntry494 : mce_external_plugins.entrySet()) {
                gVars.name = strval(javaEntry494.getKey());
                gVars.url = strval(javaEntry494.getValue());

                if (https) {
                    gVars.url = Strings.str_replace("http://", "https://", gVars.url);
                }

                gVars.plugins.putValue("-" + gVars.name);

                if (Array.in_array(gVars.name, loaded_langs)) {
                    plugurl = FileSystemOrSocket.dirname(gVars.url);
                    ext_plugins = strval(ext_plugins) + "tinyMCEPreInit.load_ext(\"" + plugurl + "\", \"" + mce_locale + "\");" + "\n";
                }

                ext_plugins = strval(ext_plugins) + "tinymce.PluginManager.load(\"" + gVars.name + "\", \"" + gVars.url + "\");" + "\n";
            }
        }

        String plugins = Strings.implode(gVars.plugins, ",");
        
        mce_buttons = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_buttons",
                new Array<String>(new ArrayEntry<String>("bold"),
                    new ArrayEntry<String>("italic"),
                    new ArrayEntry<String>("strikethrough"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("bullist"),
                    new ArrayEntry<String>("numlist"),
                    new ArrayEntry<String>("blockquote"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("justifyleft"),
                    new ArrayEntry<String>("justifycenter"),
                    new ArrayEntry<String>("justifyright"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("link"),
                    new ArrayEntry<String>("unlink"),
                    new ArrayEntry<String>("image"),
                    new ArrayEntry<String>("wp_more"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("spellchecker"),
                    new ArrayEntry<String>("fullscreen"),
                    new ArrayEntry<String>("wp_adv")));

        String mce_buttonsStr = Strings.implode(mce_buttons, ",");
        
        mce_buttons_2 = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_buttons_2",
                new Array<String>(new ArrayEntry<String>("formatselect"),
                    new ArrayEntry<String>("underline"),
                    new ArrayEntry<String>("justifyfull"),
                    new ArrayEntry<String>("forecolor"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("pastetext"),
                    new ArrayEntry<String>("pasteword"),
                    new ArrayEntry<String>("removeformat"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("media"),
                    new ArrayEntry<String>("charmap"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("outdent"),
                    new ArrayEntry<String>("indent"),
                    new ArrayEntry<String>("|"),
                    new ArrayEntry<String>("undo"),
                    new ArrayEntry<String>("redo"),
                    new ArrayEntry<String>("wp_help")));

        String mce_buttons_2Str = Strings.implode(mce_buttons_2, ",");
        mce_buttons_3 = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_buttons_3", new Array<String>());

        String mce_buttons_3Str = Strings.implode(mce_buttons_3, ",");
        mce_buttons_4 = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("mce_buttons_4", new Array<String>());

        String mce_buttons_4Str = Strings.implode(mce_buttons_4, ",");
        
        // TinyMCE init settings
        initArray = new Array<Object>(
                new ArrayEntry<Object>("mode", "none"),
                new ArrayEntry<Object>("onpageload", "wpEditorInit"),
                new ArrayEntry<Object>("width", "100%"),
                new ArrayEntry<Object>("theme", "advanced"),
                new ArrayEntry<Object>("skin", "wp_theme"),
                new ArrayEntry<Object>("theme_advanced_buttons1", mce_buttonsStr),
                new ArrayEntry<Object>("theme_advanced_buttons2", mce_buttons_2Str),
                new ArrayEntry<Object>("theme_advanced_buttons3", mce_buttons_3Str),
                new ArrayEntry<Object>("theme_advanced_buttons4", mce_buttons_4Str),
                new ArrayEntry<Object>("language", mce_locale),
                new ArrayEntry<Object>("spellchecker_languages", mce_spellchecker_languages),
                new ArrayEntry<Object>("theme_advanced_toolbar_location", "top"),
                new ArrayEntry<Object>("theme_advanced_toolbar_align", "left"),
                new ArrayEntry<Object>("theme_advanced_statusbar_location", "bottom"),
                new ArrayEntry<Object>("theme_advanced_resizing", true),
                new ArrayEntry<Object>("theme_advanced_resize_horizontal", false),
                new ArrayEntry<Object>("dialog_type", "modal"),
                new ArrayEntry<Object>("relative_urls", false),
                new ArrayEntry<Object>("remove_script_host", false),
                new ArrayEntry<Object>("convert_urls", false),
                new ArrayEntry<Object>("apply_source_formatting", false),
                new ArrayEntry<Object>("remove_linebreaks", true),
                new ArrayEntry<Object>("paste_convert_middot_lists", true),
                new ArrayEntry<Object>("paste_remove_spans", true),
                new ArrayEntry<Object>("paste_remove_styles", true),
                new ArrayEntry<Object>("gecko_spellcheck", true),
                new ArrayEntry<Object>("entities", "38,amp,60,lt,62,gt"),
                new ArrayEntry<Object>("accessibility_focus", false),
                new ArrayEntry<Object>("tab_focus", ":next"),
                new ArrayEntry<Object>("content_css", mce_css),
                new ArrayEntry<Object>("save_callback", "switchEditors.saveCallback"),
                new ArrayEntry<Object>("plugins", plugins),
                // pass-through the settings for compression and caching, so they can be changed with "tiny_mce_before_init"
                new ArrayEntry<Object>("disk_cache", true),
                new ArrayEntry<Object>("compress", true),
                new ArrayEntry<Object>("old_cache_max", "1") // number of cache files to keep
            );
        
	    // For people who really REALLY know what they're doing with TinyMCE
	    // You can modify initArray to add, remove, change elements of the config before tinyMCE.init (changed from action to filter)
        initArray = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tiny_mce_before_init", initArray);

		// Setting "valid_elements", "invalid_elements" and "extended_valid_elements" can be done through "tiny_mce_before_init".
		// Best is to use the default cleanup by not specifying valid_elements, as TinyMCE contains full set of XHTML 1.0.
		
		// support for deprecated actionsOutputControl.ob_start(gVars.webEnv);
        OutputControl.ob_start(gVars.webEnv);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("mce_options", "");
        mce_deprecated = OutputControl.ob_get_contents(gVars.webEnv);
        OutputControl.ob_end_clean(gVars.webEnv);

        //		mce_deprecated = (String) mce_deprecated;
        if ((Strings.strlen(mce_deprecated) < 10) || (BOOLEAN_FALSE == Strings.strpos(mce_deprecated, ":")) || (BOOLEAN_FALSE == Strings.strpos(mce_deprecated, ","))) {
            mce_deprecated = "";
        }

        // Settings for the gzip compression and cache
        disk_cache = ((!isset(initArray.getValue("disk_cache")) || equal(false, initArray.getValue("disk_cache")))
            ? false
            : true);
        compress = ((!isset(initArray.getValue("compress")) || equal(false, initArray.getValue("compress")))
            ? false
            : true);
        old_cache_max = (isset(initArray.getValue("old_cache_max"))
            ? intval(initArray.getValue("old_cache_max"))
            : 0);
        
        initArray.putValue("disk_cache", initArray.putValue("compress", initArray.putValue("old_cache_max", null)));
        initArray.arrayUnset("disk_cache");
        initArray.arrayUnset("compress");
        initArray.arrayUnset("old_cache_max");

        // Anybody still using IE5/5.5? It can't handle gzip compressed js well.
        if (booleanval(msie = Strings.strpos(gVars.webEnv.getHttpUserAgent(), "MSIE"))) {
            ie_ver = intval(Strings.substr(gVars.webEnv.getHttpUserAgent(), msie + 5, 3));

            if (booleanval(ie_ver) && (ie_ver < 6)) {
                compress = false;
            }
        }

        // Cache path, this is where the .gz files will be stored
        cache_path = gConsts.getABSPATH() + "wp-content/uploads/js_cache";

        if (booleanval(disk_cache) && !FileSystemOrSocket.is_dir(gVars.webEnv, cache_path)) {
            disk_cache = getIncluded(FunctionsPage.class, gVars, gConsts).wp_mkdir_p(cache_path);
        }

        cache_ext = ".js";
        gVars.plugins = Strings.explode(",", strval(initArray.getValue("plugins")));
        gVars.theme = (equal("simple", initArray.getValue("theme"))
            ? "simple"
            : "advanced");
        gVars.language = (isset(initArray.getValue("language"))
            ? Strings.substr(strval(initArray.getValue("language")), 0, 2)
            : "en");
        cacheKey = mce_options = "";

        // FIXME Temporary
        //		// Check if browser supports gzip
        //		if (compress && isset(gVars.webEnv.getHttpAcceptEncoding())) {
        //			if ((!strictEqual(BOOLEAN_FALSE, Strings.strpos(Strings.strtolower(gVars.webEnv.getHttpAcceptEncoding()), "gzip")) 
        //					|| isset(gVars.webEnv._SERVER.getValue("---------------"))) 
        //					&& true
        //			        && /*Modified by Numiton*/!booleanval(Options.ini_get(gVars.webEnv, "zlib.output_compression"))) {
        //				cache_ext = ".gz";
        //			}
        //		}
        
        // Setup cache info
        if (booleanval(disk_cache)) {
            cacheKey = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tiny_mce_version", "20080414"));

            for (Map.Entry javaEntry495 : initArray.entrySet()) {
                gVars.v = strval(javaEntry495.getValue());
                cacheKey = cacheKey + gVars.v;
            }

            if (!empty(mce_external_plugins)) {
                for (Map.Entry javaEntry496 : mce_external_plugins.entrySet()) {
                    n = javaEntry496.getKey();
                    gVars.v = strval(javaEntry496.getValue());
                    cacheKey = cacheKey + strval(n);
                }
            }

            cacheKey = Strings.md5(cacheKey);
            cache_file = cache_path + "/tinymce_" + cacheKey + cache_ext;
        }

        expiresOffset = 864000; // 10 days
        Network.header(gVars.webEnv, "Content-Type: application/x-javascript; charset=UTF-8");
        Network.header(gVars.webEnv, "Vary: Accept-Encoding"); // Handle proxies
        Network.header(gVars.webEnv, "Expires: " + DateTime.gmdate("D, d M Y H:i:s", DateTime.time() + expiresOffset) + " GMT");

        // Use cached file if exists
        if (booleanval(disk_cache) && FileSystemOrSocket.is_file(gVars.webEnv, cache_file) && FileSystemOrSocket.is_readable(gVars.webEnv, cache_file)) {
            mtime = DateTime.gmdate("D, d M Y H:i:s", FileSystemOrSocket.filemtime(gVars.webEnv, cache_file)) + " GMT";

            if (isset(gVars.webEnv._SERVER.getValue("HTTP_IF_MODIFIED_SINCE")) && equal(gVars.webEnv._SERVER.getValue("HTTP_IF_MODIFIED_SINCE"), mtime)) {
                Network.header(gVars.webEnv, "HTTP/1.1 304 Not Modified");
                System.exit();
            }

            Network.header(gVars.webEnv, "Last-Modified: " + mtime);
            Network.header(gVars.webEnv, "Cache-Control: must-revalidate", false);
            gVars.content = getFileContents(cache_file);

            if (equal(".gz", cache_ext)) {
                Network.header(gVars.webEnv, "Content-Encoding: gzip");
            }

            echo(gVars.webEnv, gVars.content);
            System.exit();
        }

        for (Map.Entry javaEntry497 : initArray.entrySet()) {
            k = javaEntry497.getKey();
            gVars.v = strval(javaEntry497.getValue());
            mce_options = mce_options + strval(k) + ":\"" + gVars.v + "\",";
        }

        if (booleanval(mce_deprecated)) {
            mce_options = mce_options + mce_deprecated;
        }

        mce_options = Strings.rtrim(Strings.trim(mce_options), "\\n\\r,");
        gVars.content = "var tinyMCEPreInit = { settings : { themes : \"" + gVars.theme + "\", plugins : \"" + strval(initArray.getValue("plugins")) + "\", languages : \"" + gVars.language +
            "\", debug : false }, base : \"" + baseurl + "\", suffix : \"\" };";
        
        // Load patch
        gVars.content = gVars.content + getFileContents("tiny_mce_ext.js");
        
        // Add core
        gVars.content = gVars.content + getFileContents("tiny_mce.js");
        
        // Patch loading functions
        gVars.content = gVars.content + "tinyMCEPreInit.start();";
        
        // Add all languages (WP)
        /* Condensed dynamic construct: 389528 */ includeOnce(gVars, gConsts, Wp_langsPage.class);
        gVars.content = gVars.content + gVars.strings;
        
        // Add themes
        gVars.content = gVars.content + getFileContents("themes/" + gVars.theme + "/editor_template.js");

        // Add plugins
        for (Map.Entry javaEntry498 : gVars.plugins.entrySet()) {
            gVars.plugin = strval(javaEntry498.getValue());
            gVars.content = gVars.content + getFileContents("plugins/" + gVars.plugin + "/editor_plugin.js");
        }

        // Add external plugins and init
        gVars.content = gVars.content + strval(ext_plugins) + "tinyMCE.init({" + mce_options + "});";

        // Generate GZIP'd content
        if (equal(".gz", cache_ext)) {
            Network.header(gVars.webEnv, "Content-Encoding: gzip");
            gVars.content = GZIP.gzencode(gVars.content, 9, GZIP.FORCE_GZIP);
        }

        // Stream to client
        echo(gVars.webEnv, Misc.getAsByteArray(gVars.content) /*Modified by Numiton*/);

        // Write file
        if (!equal("", cacheKey) && FileSystemOrSocket.is_dir(gVars.webEnv, cache_path) && FileSystemOrSocket.is_readable(gVars.webEnv, cache_path)) {
            old_cache = new Array<Object>();
            gVars.handle = Directories.opendir(gVars.webEnv, cache_path);

            while (!strictEqual(STRING_FALSE, gVars.file = Directories.readdir(gVars.webEnv, gVars.handle))) {
                if (equal(gVars.file, ".") || equal(gVars.file, "..")) {
                    continue;
                }

                saved = JFileSystemOrSocket.filectime(gVars.webEnv, cache_path + "/" + gVars.file);

                if (!strictEqual(Strings.strpos(gVars.file, "tinymce_"), BOOLEAN_FALSE) && equal(Strings.substr(gVars.file, -3), cache_ext)) {
                    old_cache.putValue(saved, gVars.file);
                }
            }

            Directories.closedir(gVars.webEnv, gVars.handle);
            Array.krsort(old_cache);

            if (1 >= old_cache_max) {
                del_cache = old_cache;
            } else {
                del_cache = Array.array_slice(old_cache, old_cache_max - 1);
            }

            for (Map.Entry javaEntry499 : del_cache.entrySet()) {
                gVars.key = strval(javaEntry499.getValue());
                JFileSystemOrSocket.unlink(gVars.webEnv, cache_path + "/" + gVars.key);
            }

            putFileContents(cache_file, gVars.content);
        }

        return DEFAULT_VAL;
    }
}
