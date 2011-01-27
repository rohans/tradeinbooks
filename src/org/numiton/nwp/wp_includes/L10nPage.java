/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: L10nPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class L10nPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(L10nPage.class.getName());
    Array<Object> l10n = new Array<Object>();

    @Override
    @RequestMapping("/wp-includes/l10n.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/l10n";
    }

    /**
     * WordPress Translation API
     *
     * @package WordPress
     * @subpackage i18n
     */

    /**
     * get_locale() - Gets the current locale
     *
     * If the locale is set, then it will filter the locale
     * in the 'locale' filter hook and return the value.
     *
     * If the locale is not set already, then the WPLANG
     * constant is used if it is defined. Then it is filtered
     * through the 'locale' filter hook and the value for the
     * locale global set and the locale is returned.
     *
     * The process to get the locale should only be done once
     * but the locale will always be filtered using the
     * 'locale' hook.
     *
     * @since 1.5.0
     * @uses apply_filters() Calls 'locale' hook on locale value
     * @uses $locale Gets the locale stored in the global
     *
     * @return string The locale of the blog or from the 'locale' hook
     */
    public String get_locale() {
        if (isset(gVars.locale)) {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("locale", gVars.locale));
        }

    	// WPLANG is defined in wp-config.
        if (gConsts.isWPLANGDefined()) {
            gVars.locale = gConsts.getWPLANG();
        }

        if (empty(gVars.locale)) {
            gVars.locale = "";
        }

        gVars.locale = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("locale", gVars.locale));

        return gVars.locale;
    }

    /**
     * translate() - Retrieve the translated text
     * If the domain is set in the $l10n global, then the text is run through
     * the domain's translate method. After it is passed to the 'gettext' filter
     * hook, along with the untranslated text as the second parameter.
     * If the domain is not set, the $text is just returned.
     * @since 2.2.0
     * @uses $l10n Gets list of domain translated string (gettext_reader)
     * objects
     * @uses apply_filters() Calls 'gettext' on domain translated text with the
     * untranslated text as second parameter
     * @param string $text Text to translate
     * @param string $domain Domain to retrieve the translated text
     * @return string Translated text
     */
    public String translate(String text, String domain) {
        if (isset(l10n.getValue(domain))) {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("gettext", ((gettext_reader) l10n.getValue(domain)).translate(text), text));
        } else {
            return text;
        }
    }

    /**
     * translate_with_context() - Retrieve the translated text and strip
     * context
     * If the domain is set in the $l10n global, then the text is run through
     * the domain's translate method. After it is passed to the 'gettext' filter
     * hook, along with the untranslated text as the second parameter.
     * If the domain is not set, the $text is just returned.
     * @since 2.5
     * @uses translate()
     * @param string $text Text to translate
     * @param string $domain Domain to retrieve the translated text
     * @return string Translated text
     */
    public String translate_with_context(String text, String domain) {
        String whole = null;
        int last_bar = 0;
        whole = translate(text, domain);
        last_bar = Strings.strrpos(whole, "|");

        if (equal(false, last_bar)) {
            return whole;
        } else {
            return Strings.substr(whole, 0, last_bar);
        }
    }

    public String __(String text) {
        return __(text, "");
    }

    /**
     * __() - Retrieve a translated string
     * __() is a convenience function which retrieves the translated string from
     * the translate().
     * @see translate() An alias of translate()
     * @since 2.1.0
     * @param string $text Text to translate
     * @param string $domain Optional. Domain to retrieve the translated text
     * @return string Translated text
     */
    public String __(String text, String domain) {
        return translate(text, domain);
    }

    public void _e(String text) {
        _e(text, "");
    }

    /**
     * . . _e() - Display a translated string
     * _e() is a convenience function which displays the returned translated
     * text from translate().
     * @see translate() Echos returned translate() string
     * @since 1.2.0
     * @param string $text Text to translate
     * @param string $domain Optional. Domain to retrieve the translated text
     */
    public void _e(String text, String domain) {
        echo(gVars.webEnv, translate(text, domain));
    }

    public String _c(String text) {
        return _c(text, "default");
    }

    /**
     * _c() - Retrieve context translated string
     * Quite a few times, there will be collisions with similar translatable
     * text found in more than two places but with different translated context.
     * In order to use the separate contexts, the _c() function is used and the
     * translatable string uses a pipe ('|') which has the context the string is
     * in.
     * When the translated string is returned, it is everything before the pipe,
     * not including the pipe character. If there is no pipe in the translated
     * text then everything is returned.
     * @since 2.2.0
     * @param string $text Text to translate
     * @param string $domain Optional. Domain to retrieve the translated text
     * @return string Translated context string without pipe
     */
    public String _c(String text, String domain) {
        return translate_with_context(text, domain);
    }

    public String __ngettext(String single, String plural, int number) {
        return __ngettext(single, plural, number, "default");
    }

    /**
     * __ngettext() - Retrieve the plural or single form based on the amount
     * If the domain is not set in the $l10n list, then a comparsion will be
     * made and either $plural or $single parameters returned.
     * If the domain does exist, then the parameters $single, $plural, and
     * $number will first be passed to the domain's ngettext method. Then it
     * will be passed to the 'ngettext' filter hook along with the same
     * parameters. The expected type will be a string.
     * @since 1.2.0
     * @uses $l10n Gets list of domain translated string (gettext_reader)
     * objects
     * @uses apply_filters() Calls 'ngettext' hook on domains text returned,
     * along with $single, $plural, and $number parameters. Expected to
     * return string.
     * @param string $single The text that will be used if $number is 1
     * @param string $plural The text that will be used if $number is not 1
     * @param int $number The number to compare against to use either $single or
     * $plural
     * @param string $domain Optional. The domain identifier the text should be
     * retrieved in
     * @return string Either $single or $plural translated text
     */
    public String __ngettext(String single, String plural, int number, String domain) {
        if (isset(l10n.getValue(domain))) {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("ngettext", ((gettext_reader) l10n.getValue(domain)).ngettext(single, plural, number), single, plural, number));
        } else {
            if (!equal(number, 1)) {
                return plural;
            } else {
                return single;
            }
        }
    }

    /**
     * __ngettext_noop() - register plural strings in POT file, but don't translate them
     *
     * Used when you want do keep structures with translatable plural strings and
     * use them later.
     *
     * Example:
     *  $messages = array(
     *  	'post' => ngettext_noop('%s post', '%s posts'),
     *  	'page' => ngettext_noop('%s pages', '%s pages')
     *  );
     *  ...
     *  $message = $messages[$type];
     *  $usable_text = sprintf(__ngettext($message[0], $message[1], $count), $count);
     *
     * @since 2.5
     * @param $single Single form to be i18ned
     * @param $plural Plural form to be i18ned
     * @param $number Not used, here for compatibility with __ngettext, optional
     * @param $domain Not used, here for compatibility with __ngettext, optional
     * @return array array($single, $plural)
     */
    public Array<String> __ngettext_noop(String single, String plural, int number, String domain) {
        return new Array<String>(new ArrayEntry<String>(single), new ArrayEntry<String>(plural));
    }

    /**
     * load_textdomain() - Loads MO file into the list of domains
     *
     * If the domain already exists, the inclusion will fail. If the
     * MO file is not readable, the inclusion will fail.
     *
     * On success, the mofile will be placed in the $l10n global by
     * $domain and will be an gettext_reader object.
     *
     * @since 1.5.0
     * @uses $l10n Gets list of domain translated string (gettext_reader) objects
     * @uses CacheFileReader Reads the MO file
     * @uses gettext_reader Allows for retrieving translated strings
     *
     * @param string $domain Unique identifier for retrieving translated strings
     * @param string $mofile Path to the .mo file
     * @return null On failure returns null and also on success returns nothing.
     */
    public void load_textdomain(String domain, String mofile) {
        CachedFileReader input = null;

        if (isset(l10n.getValue(domain))) {
            return;
        }

        if (FileSystemOrSocket.is_readable(gVars.webEnv, mofile)) {
            input = new CachedFileReader(gVars, gConsts, mofile);
        } else {
            return;
        }

        l10n.putValue(domain, new gettext_reader(gVars, gConsts, input));
    }

    /**
     * load_default_textdomain() - Loads default translated strings based on
     * locale
     * Loads the .mo file in LANGDIR constant path from WordPress root. The
     * translated (.mo) file is named based off of the locale.
     * @since 1.5.0
     */
    public void load_default_textdomain() {
        String locale = null;
        String mofile = null;
        locale = get_locale();

        if (empty(locale)) {
            locale = "en_US";
        }

        mofile = gConsts.getABSPATH() + gConsts.getLANGDIR() + "/" + locale + ".mo";
        load_textdomain("default", mofile);
    }

    /**
     * load_plugin_textdomain() - Loads the plugin's translated strings
     * If the path is not given then it will be the root of the plugin
     * directory. The .mo file should be named based on the domain with a dash
     * followed by a dash, and then the locale exactly.
     * The plugin may place all of the .mo files in another folder and set the
     * $path based on the relative location from ABSPATH constant. The plugin
     * may use the constant PLUGINDIR and/or plugin_basename() to get path of
     * the plugin and then add the folder which holds the .mo files.
     * @since 1.5.0
     * @param string $domain Unique identifier for retrieving translated strings
     * @param string $path Optional. Path of the folder where the .mo files
     * reside.
     */
    public void load_plugin_textdomain(String domain, String path) {
        String locale = null;
        String mofile = null;
        locale = get_locale();

        if (empty(locale)) {
            locale = "en_US";
        }

        if (equal(false, path)) {
            path = gConsts.getPLUGINDIR();
        }

        mofile = gConsts.getABSPATH() + path + "/" + domain + "-" + locale + ".mo";
        load_textdomain(domain, mofile);
    }

    /**
     * load_theme_textdomain() - Includes theme's translated strings for the
     * theme
     * If the current locale exists as a .mo file in the theme's root directory,
     * it will be included in the translated strings by the $domain.
     * The .mo files must be named based on the locale exactly.
     * @since 1.5.0
     * @param string $domain Unique identifier for retrieving translated strings
     */
    public void load_theme_textdomain(Object domain) {
        String locale = null;
        String mofile = null;
        locale = get_locale();

        if (empty(locale)) {
            locale = "en_US";
        }

        mofile = getIncluded(ThemePage.class, gVars, gConsts).get_template_directory() + "/" + locale + ".mo";
        load_textdomain(strval(domain), mofile);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
