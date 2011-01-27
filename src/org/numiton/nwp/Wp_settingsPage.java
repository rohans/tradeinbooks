/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_settingsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_content.plugins.akismet.AkismetPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Wp_settingsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_settingsPage.class.getName());
    public int timestart;
    public int timeend;
    public String cookiehash;
    public Object current_plugins;

    /* Do not change type */ public String locale_file;

    @Override
    @RequestMapping("/wp-settings.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_settings";
    }

    /**
     * wp_unregister_GLOBALS() - Turn register globals off
     *
     * @access private
     * @since 2.1.0
     * @return null Will return null if register_globals PHP directive was disabled
     */
    public void wp_unregister_GLOBALS() {
        // Commented by Numiton: "register_globals" unavailable in Java
        //		Array<Object> noUnset = new Array<Object>();
        //		Array<Object> input = new Array<Object>();
        //		Object k = null;
        //		Object v = null;
        //		if (!booleanval(Options.ini_get(gVars.webEnv, "register_globals"))) {
        //			return;
        //		}
        //		if (isset(gVars.webEnv._REQUEST.getValue("GLOBALS"))) {
        //			System.exit("GLOBALS overwrite attempt detected");
        //		}
        //		noUnset = new Array<Object>(new ArrayEntry<Object>("GLOBALS"), new ArrayEntry<Object>("_GET"), new ArrayEntry<Object>("_POST"), new ArrayEntry<Object>("_COOKIE"), new ArrayEntry<Object>(
        //		        "_REQUEST"), new ArrayEntry<Object>("_SERVER"), new ArrayEntry<Object>("_ENV"), new ArrayEntry<Object>("_FILES"), new ArrayEntry<Object>("table_prefix"));
        //		input = Array.array_merge(gVars.webEnv._GET, gVars.webEnv._POST, gVars.webEnv._COOKIE, gVars.webEnv._SERVER, gVars.webEnv.getEnv(), gVars.webEnv._FILES, (isset(gVars.webEnv._SESSION) && is_array(gVars.webEnv._SESSION))
        //		        ? gVars.webEnv._SESSION : new Array<Object>());
        //		for (Map.Entry javaEntry685 : input.entrySet()) {
        //			k = javaEntry685.getKey();
        //			v = javaEntry685.getValue();
        //
        //			/*
        //			 * Unsupported GLOBALS referencing with non-static expression:
        //			 * com.numiton.ntile.til.model.expressions.impl.VariableRefImpl@275
        //			 * (generated: false, leadingText: null, translationHint: null,
        //			 * text: k, id: 514052) (declarationId: 0)
        //			 */
        //			if (!Array.in_array(k, noUnset) && isset(GLOBALS.getValue(k))) {
        //
        //				/*
        //				 * Unsupported GLOBALS referencing with non-static
        //				 * expression:
        //				 * com.numiton.ntile.til.model.expressions.impl.VariableRefImpl@275
        //				 * (generated: false, leadingText: null, translationHint: null,
        //				 * text: k, id: 514062) (declarationId: 0)
        //				 */
        //				GLOBALS.putValue(k, null);
        //
        //				/*
        //				 * Unsupported GLOBALS referencing with non-static
        //				 * expression:
        //				 * com.numiton.ntile.til.model.expressions.impl.VariableRefImpl@275
        //				 * (generated: false, leadingText: null, translationHint: null,
        //				 * text: k, id: 514072) (declarationId: 0)
        //				 */
        //				GLOBALS.arrayUnset(k);
        //			}
        //		}
    }

    /**
     * timer_start() - PHP 4 standard microtime start capture
     *
     * @access private
     * @since 0.71
     * @global int $timestart Seconds and Microseconds added together from when function is called
     * @return bool Always returns true
     */
    public boolean timer_start() {
        Array<String> mtime = Strings.explode(" ", strval(DateTime.microtime()));
        timestart = intval(mtime.getValue(1)) + intval(mtime.getValue(0));

        return true;
    }

    public float timer_stop() {
        return timer_stop(0, 3);
    }

    public float timer_stop(int display) {
        return timer_stop(display, 3);
    }

    /**
     * timer_stop() - Return and/or display the time from the page start to when function is called.
     *
     * You can get the results and print them by doing:
     * <code>
     * $nTimePageTookToExecute = timer_stop();
     * echo $nTimePageTookToExecute;
     * </code>
     *
     * Or instead, you can do:
     * <code>
     * timer_stop(1);
     * </code>
     * which will do what the above does. If you need the result, you can assign it to a variable, but
     * most cases, you only need to echo it.
     *
     * @since 0.71
     * @global int $timestart Seconds and Microseconds added together from when timer_start() is called
     * @global int $timeend  Seconds and Microseconds added together from when function is called
     *
     * @param int $display Use '0' or null to not echo anything and 1 to echo the total time
     * @param int $precision The amount of digits from the right of the decimal to display. Default is 3.
     * @return float The "second.microsecond" finished time calculation
     */
    public float timer_stop(int display, int precision) {
        Array<String> mtime = new Array<String>();
        int timetotal = 0;
        float r = 0;
        String mtimeStr = strval(DateTime.microtime());
        mtime = Strings.explode(" ", mtimeStr);

        int mtimeInt = intval(mtime.getValue(1)) + intval(mtime.getValue(0));
        timeend = mtimeInt;
        timetotal = timeend - timestart;
        r = floatval((true
                ? /*Modified by Numiton*/
            getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(floatval(timetotal), precision)
                : Strings.number_format(timetotal, precision)));

        if (booleanval(display)) {
            echo(gVars.webEnv, r);
        }

        return r;
    }

    /**
     * shutdown_action_hook() - Runs just before PHP shuts down execution.
     *
     * @access private
     * @since 1.2
     */
    public void shutdown_action_hook() {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("shutdown", "");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_close();
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_settings_block1");
        gVars.webEnv = webEnv;

        //		 Commented by Numiton. Meaningless in Java.
        //		/**
        //		 * Used to setup and fix common variables and include
        //		 * the WordPress procedural and class library.
        //		 *
        //		 * You should not have to change this file and allows
        //		 * for some configuration in wp-config.php.
        //		 *
        //		 * @package WordPress
        //		 */
        //				if (!gConsts.isWP_MEMORY_LIMITDefined()) {
        //					gConsts.setWP_MEMORY_LIMIT("32M");
        //				}
        //				if (/* Usage of unsupported runtime function 'function_exists' */Unsupported.function_exists("memory_get_usage")
        //				        && floatval(intval(Options.ini_get(gVars.webEnv, "memory_limit"))) < Math.abs(intval(gConsts.getWP_MEMORY_LIMIT()))) {
        //					Options.ini_set(gVars.webEnv, "memory_limit", gConsts.getWP_MEMORY_LIMIT());
        //				}
        wp_unregister_GLOBALS();
        // Modified by Numiton
        gVars.wp_filter = new Array<Object>();
        gVars.cache_lastcommentmodified = new Array<String>();
        gVars.cache_lastpostdate = new Array<Object>();

        /**
         * The $blog_id global, which you can change in the config allows you to create a simple
         * multiple blog installation using just one WordPress and changing $blog_id around.
         *
         * @global int $blog_id
         * @since 2.0.0
         */
        if (!isset(gVars.blog_id)) {
            gVars.blog_id = 1;
        }

        // Fix for IIS, which doesn't set REQUEST_URI
        if (empty(gVars.webEnv.getRequestURI())) {
            // IIS Mod-Rewrite
            if (isset(gVars.webEnv._SERVER.getValue("HTTP_X_ORIGINAL_URL"))) {
                gVars.webEnv._SERVER.putValue("REQUEST_URI", gVars.webEnv._SERVER.getValue("HTTP_X_ORIGINAL_URL"));
            }
            // IIS Isapi_Rewrite
            else if (isset(gVars.webEnv._SERVER.getValue("HTTP_X_REWRITE_URL"))) {
                gVars.webEnv._SERVER.putValue("REQUEST_URI", gVars.webEnv._SERVER.getValue("HTTP_X_REWRITE_URL"));
            } else {
                // Some IIS + PHP configurations puts the script-name in the path-info (No need to append it twice)
                if (isset(gVars.webEnv.getPathInfo())) {
                    if (equal(gVars.webEnv.getPathInfo(), gVars.webEnv.getScriptName())) {
                        gVars.webEnv._SERVER.putValue("REQUEST_URI", gVars.webEnv.getPathInfo());
                    } else {
                        gVars.webEnv._SERVER.putValue("REQUEST_URI", gVars.webEnv.getScriptName() + gVars.webEnv.getPathInfo());
                    }
                }

                // Append the query string if it exists and isn't null
                if (isset(gVars.webEnv.getQueryString()) && !empty(gVars.webEnv.getQueryString())) {
                    gVars.webEnv._SERVER.putValue("REQUEST_URI", gVars.webEnv.getRequestURI() + "?" + gVars.webEnv.getQueryString());
                }
            }
        }

        // Fix for PHP as CGI hosts that set SCRIPT_FILENAME to something ending in php.cgi for all requests
        if (isset(gVars.webEnv.getScriptFilename()) && equal(Strings.strpos(gVars.webEnv.getScriptFilename(), "php.cgi"), Strings.strlen(gVars.webEnv.getScriptFilename()) - 7)) {
            gVars.webEnv._SERVER.putValue("SCRIPT_FILENAME", gVars.webEnv.getPathTranslated());
        }

        // Fix for Dreamhost and other PHP as CGI hosts
        if (!strictEqual(Strings.strpos(gVars.webEnv.getScriptName(), "php.cgi"), BOOLEAN_FALSE)) {
            gVars.webEnv._SERVER.putValue("PATH_INFO", null);
        }

        // Fix empty PHP_SELF
        gVars.PHP_SELF = gVars.webEnv.getPhpSelf();

        if (empty(gVars.PHP_SELF)) {
            gVars.PHP_SELF = QRegExPerl.preg_replace("/(\\?.*)?$/", "", gVars.webEnv.getRequestURI());
            gVars.webEnv._SERVER.putValue("PHP_SELF", gVars.PHP_SELF);
        }

        if (booleanval(Options.version_compare("4.3", Options.phpversion(), ">"))) {
            System.exit("Your server is running PHP version " + Options.phpversion() + " but nWordPress requires at least 4.3.");
        }

        /*Commented by Numiton. All items exist*/

        //		if (!Options.extension_loaded("mysql") && !FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-content/db.php")) {

        //			System.exit("Your PHP installation appears to be missing the MySQL extension which is required by WordPress.");

        //		}
        timer_start();

        // Add define('WP_DEBUG',true); to wp-config.php to enable display of notices during development.
        if (gConsts.isWP_DEBUGDefined() && equal(gConsts.getWP_DEBUG(), true)) {
            ErrorHandling.error_reporting(gVars.webEnv, ErrorHandling.E_ALL);
        } else {
            ErrorHandling.error_reporting(gVars.webEnv, ErrorHandling.E_ALL ^ ErrorHandling.E_NOTICE ^ ErrorHandling.E_USER_NOTICE);
        }

        // For an advanced caching plugin to use, static because you would only want one
        if (gConsts.isWP_CACHEDefined()) {
            // TODO Add support for advanced caching 
        }

        /**
         * Stores the location of the WordPress directory of functions, classes, and core content.
         *
         * @since 1.0.0
         */
        gConsts.setWPINC("/wp-includes"); // Modified by Numiton

        if (!gConsts.isLANGDIRDefined()) {
            /**
             * Stores the location of the language directory. First looks for language folder in wp-content
             * and uses that folder if it exists. Or it uses the "languages" folder in WPINC.
             *
             * @since 2.1.0
             */
            if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-content/languages") && FileSystemOrSocket.is_dir(gVars.webEnv, gConsts.getABSPATH() + "wp-content/languages")) {
                gConsts.setLANGDIR("wp-content/languages"); // no leading slash, no trailing slash
            } else {
                gConsts.setLANGDIR(gConsts.getWPINC() + "/languages"); // no leading slash, no trailing slash
            }
        }

        /**
         * Allows for the plugins directory to be moved from the default location.
         *
         * This isn't used everywhere. Constant is not used in plugin_basename()
         * which might cause conflicts with changing this.
         *
         * @since 2.1
         */
        if (!gConsts.isPLUGINDIRDefined()) {
            gConsts.setPLUGINDIR("wp-content/plugins");
        }

        require(gVars, gConsts, CompatPage.class);
        require(gVars, gConsts, FunctionsPage.class);
        require(gVars, gConsts, ClassesPage.class);

        getIncluded(FunctionsPage.class, gVars, gConsts).require_wp_db();

        if (!empty(gVars.wpdb.error)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).dead_db();
        }

        gVars.prefix = gVars.wpdb.set_prefix(gVars.table_prefix);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.prefix)) {
            getIncluded(FunctionsPage.class, gVars, gConsts)
                .wp_die("<strong>ERROR</strong>: <code>$table_prefix</code> in <code>wp-config.php</code> can only contain numbers, letters, and underscores.", "");
        }

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-content/object-cache.php")) {
            // TODO Add support for object cache
        } else {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, CachePage.class);
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_init();

        /* Condensed dynamic construct */
        require(gVars, gConsts, PluginPage.class);

        /* Condensed dynamic construct */
        require(gVars, gConsts, Default_filtersPage.class);

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, L10nPage.class);

        if (!getIncluded(FunctionsPage.class, gVars, gConsts).is_blog_installed() && strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "install.php"), BOOLEAN_FALSE) &&
                !gConsts.isWP_INSTALLINGDefined()) {
            String link;

            if (gConsts.isWP_SITEURLDefined()) {
                link = gConsts.getWP_SITEURL() + "/wp-admin/install.php";
            } else if (!strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "wp-admin"), BOOLEAN_FALSE)) {
                link = QRegExPerl.preg_replace("|/wp-admin/?.*?$|", "/", gVars.webEnv.getPhpSelf()) + "wp-admin/install.php";
            } else {
                link = QRegExPerl.preg_replace("|/[^/]+?$|", "/", gVars.webEnv.getPhpSelf()) + "wp-admin/install.php";
            }

            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, KsesPage.class);

            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, PluggablePage.class);
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(link, 302);
            System.exit(); // have to die here ~ Mark
        }

        require(gVars, gConsts, FormattingPage.class);
        require(gVars, gConsts, CapabilitiesPage.class);
        require(gVars, gConsts, QueryPage.class);
        require(gVars, gConsts, ThemePage.class);
        require(gVars, gConsts, UserPage.class);
        require(gVars, gConsts, General_templatePage.class);
        require(gVars, gConsts, Link_templatePage.class);
        require(gVars, gConsts, Author_templatePage.class);
        require(gVars, gConsts, PostPage.class);
        require(gVars, gConsts, Post_templatePage.class);
        require(gVars, gConsts, CategoryPage.class);
        require(gVars, gConsts, Category_templatePage.class);
        require(gVars, gConsts, CommentPage.class);
        require(gVars, gConsts, Comment_templatePage.class);
        require(gVars, gConsts, RewritePage.class);
        require(gVars, gConsts, FeedPage.class);
        require(gVars, gConsts, BookmarkPage.class);
        require(gVars, gConsts, Bookmark_templatePage.class);
        require(gVars, gConsts, KsesPage.class);
        require(gVars, gConsts, CronPage.class);
        require(gVars, gConsts, VersionPage.class);
        require(gVars, gConsts, DeprecatedPage.class);
        require(gVars, gConsts, Script_loaderPage.class);
        require(gVars, gConsts, TaxonomyPage.class);
        require(gVars, gConsts, UpdatePage.class);
        require(gVars, gConsts, CanonicalPage.class);
        require(gVars, gConsts, ShortcodesPage.class);
        require(gVars, gConsts, MediaPage.class);

        if (strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "install.php"), BOOLEAN_FALSE)) {
            // Used to guarantee unique hash cookies
            cookiehash = Strings.md5(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl")));

            /**
             * Used to guarantee unique hash cookies
             * @since 1.5
             */
            gConsts.setCOOKIEHASH(cookiehash);
        }

        /**
         * Should be exactly the same as the default value of SECRET_KEY in wp-config-sample.php
         * @since 2.5
         */
        gVars.wp_default_secret_key = "put your unique phrase here";

        /**
         * It is possible to define this in wp-config.php
         * @since 2.0.0
         */
        if (!gConsts.isUSER_COOKIEDefined()) {
            gConsts.setUSER_COOKIE("wordpressuser_" + gConsts.getCOOKIEHASH());
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 2.0.0
         */
        if (!gConsts.isPASS_COOKIEDefined()) {
            gConsts.setPASS_COOKIE("wordpresspass_" + gConsts.getCOOKIEHASH());
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 2.5
         */
        if (!gConsts.isAUTH_COOKIEDefined()) {
            gConsts.setAUTH_COOKIE("wordpress_" + gConsts.getCOOKIEHASH());
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 2.3.0
         */
        if (!gConsts.isTEST_COOKIEDefined()) {
            gConsts.setTEST_COOKIE("wordpress_test_cookie");
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 1.2.0
         */
        if (!gConsts.isCOOKIEPATHDefined()) {
            gConsts.setCOOKIEPATH(QRegExPerl.preg_replace("|https?://[^/]+|i", "", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/"));
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 1.5.0
         */
        if (!gConsts.isSITECOOKIEPATHDefined()) {
            gConsts.setSITECOOKIEPATH(QRegExPerl.preg_replace("|https?://[^/]+|i", "", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/"));
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 2.0.0
         */
        if (!gConsts.isCOOKIE_DOMAINDefined()) {
            gConsts.setCOOKIE_DOMAIN(strval(false));
        }

        /**
         * It is possible to define this in wp-config.php
         * @since 2.5.0
         */
        if (!gConsts.isAUTOSAVE_INTERVALDefined()) {
            gConsts.setAUTOSAVE_INTERVAL(60);
        }

        require(gVars, gConsts, VarsPage.class);

        // Check for hacks file if the option is enabled
        if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("hack_file"))) {
            if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "my-hacks.php")) {
                // Commented by Numiton
                //				require(ABSPATH . 'my-hacks.php');
            }
        }

        if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins"))) {
            current_plugins = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("active_plugins");

            if (is_array(current_plugins)) {
                for (Map.Entry javaEntry686 : ((Array<?>) current_plugins).entrySet()) {
                    gVars.plugin = strval(javaEntry686.getValue());

                    // TODO Add support for multiple plugins
                    if (equal("akismet", gVars.plugin)) {
                        include(gVars, gConsts, AkismetPage.class);
                    }
                }
            }
        }

        require(gVars, gConsts, PluggablePage.class);

        if (true) /*Modified by Numiton*/ {
            if (!Multibyte.mb_internal_encoding(gVars.webEnv, strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")))) {
                Multibyte.mb_internal_encoding(gVars.webEnv, "UTF-8");
            }
        }

        if (gConsts.isWP_CACHEDefined() && false) /*Modified by Numiton*/ {
            // Commented by Numiton

            //			wp_cache_postload();
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("plugins_loaded", "");

        // If already slashed, strip.
        if (booleanval(Options.get_magic_quotes_gpc(gVars.webEnv))) {
            gVars.webEnv._GET = (Array) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(gVars.webEnv._GET);
            gVars.webEnv._POST = (Array) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(gVars.webEnv._POST);
            gVars.webEnv._COOKIE = (Array) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(gVars.webEnv._COOKIE);
        }

        // Escape with wpdb.
        gVars.webEnv._GET = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(gVars.webEnv._GET);
        gVars.webEnv._POST = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(gVars.webEnv._POST);
        gVars.webEnv._COOKIE = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(gVars.webEnv._COOKIE);
        gVars.webEnv._SERVER = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(gVars.webEnv._SERVER);

        getIncluded(PluginPage.class, gVars, gConsts).do_action("sanitize_comment_cookies", "");

        /**
         * WordPress Query object
         * @global object $wp_the_query
         * @since 2.0.0
         */
        gVars.wp_the_query = new WP_Query(gVars, gConsts);

        /**
         * Holds the reference to @see $wp_the_query
         * Use this global for WordPress queries
         * @global object $wp_query
         * @since 1.5.0
         */
        gVars.wp_query = gVars.wp_the_query;

        /**
         * Holds the WordPress Rewrite object for creating pretty URLs
         * @global object $wp_rewrite
         * @since 1.5.0
         */
        gVars.wp_rewrite = new WP_Rewrite(gVars, gConsts);

        /**
         * WordPress Object
         * @global object $wp
         * @since 2.0.0
         */
        gVars.wp = new WP(gVars, gConsts);

        /**
         * Web Path to the current active template directory
         * @since 1.5
         */
        gConsts.setTEMPLATEPATH("wp-content/themes/default");

        /**
         * Web Path to the current active template stylesheet directory
         * @since 2.1
         */
        gConsts.setSTYLESHEETPATH("wp-content/themes/default");

        // Load the default text localization domain.
        getIncluded(L10nPage.class, gVars, gConsts).load_default_textdomain();

        /**
         * The locale of the blog
         * @since 1.5.0
         */
        gVars.locale = getIncluded(L10nPage.class, gVars, gConsts).get_locale();
        locale_file = gConsts.getABSPATH() + gConsts.getLANGDIR() + "/" + gVars.locale + ".php";

        if (FileSystemOrSocket.is_readable(gVars.webEnv, locale_file)) {
        }

        // Pull in locale data after loading text domain.
        // TODO Add support for multiple languages

        /**
         * WordPress Locale object for loading locale domain date and various strings.
         * @global object $wp_locale
         * @since 2.1.0
         */
        gVars.wp_locale = new WP_Locale(gVars, gConsts);

        // Load functions for active theme.
        if (!strictEqual(gConsts.getTEMPLATEPATH(), gConsts.getSTYLESHEETPATH()) && FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getSTYLESHEETPATH() + "/functions.php")) {
            /* Condensed dynamic construct: 515928 */ include(gVars, gConsts, org.numiton.nwp.wp_content.themes._default.FunctionsPage.class);
        }

        if (true)/*Modified by Numiton. TODO Add support for multiple themes */
         {
            /* Condensed dynamic construct: 515947 */ include(gVars, gConsts, org.numiton.nwp.wp_content.themes._default.FunctionsPage.class);
        }

        FunctionHandling.register_shutdown_function(gVars.webEnv, new Callback("shutdown_action_hook", this));

        gVars.wp.init(); // Sets up current user.

        // Everything is loaded and initialized.
        getIncluded(PluginPage.class, gVars, gConsts).do_action("init", "");

        return DEFAULT_VAL;
    }
}
