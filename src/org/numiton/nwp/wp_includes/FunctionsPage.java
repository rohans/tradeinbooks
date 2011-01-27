/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FunctionsPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.curl.Curl;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class FunctionsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FunctionsPage.class.getName());
    public Object post_default_category;
    public Object wpsmiliestrans;

    @Override
    @RequestMapping("/wp-includes/functions.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/functions";
    }

    public String mysql2date(String dateformatstring, String mysqlstring) {
        return mysql2date(dateformatstring, mysqlstring, true);
    }

    public String mysql2date(String dateformatstring, String mysqlstring, boolean translate) {
        String m = null;
        int i = 0;
        String datemonth = null;
        String datemonth_abbrev = null;
        String dateweekday = null;
        String dateweekday_abbrev = null;
        String datemeridiem = null;
        String datemeridiem_capital = null;
        String j = null;
        m = mysqlstring;

        if (empty(m)) {
            return strval(false);
        }

        if (equal("G", dateformatstring)) {
            return strval(
                DateTime.gmmktime(intval(Strings.substr(m, 11, 2)), intval(Strings.substr(m, 14, 2)), intval(Strings.substr(m, 17, 2)), intval(Strings.substr(m, 5, 2)),
                    intval(Strings.substr(m, 8, 2)), intval(Strings.substr(m, 0, 4))));
        }

        i = DateTime.mktime(
                intval(Strings.substr(m, 11, 2)),
                intval(Strings.substr(m, 14, 2)),
                intval(Strings.substr(m, 17, 2)),
                intval(Strings.substr(m, 5, 2)),
                intval(Strings.substr(m, 8, 2)),
                intval(Strings.substr(m, 0, 4)));

        if (equal("U", dateformatstring)) {
            return strval(i);
        }

        if (equal(-1, i) || equal(false, i)) {
            i = 0;
        }

        if (!empty(gVars.wp_locale.month) && !empty(gVars.wp_locale.weekday) && translate) {
            datemonth = gVars.wp_locale.get_month(DateTime.date("m", i));
            datemonth_abbrev = gVars.wp_locale.get_month_abbrev(datemonth);
            dateweekday = gVars.wp_locale.get_weekday(intval(DateTime.date("w", i)));
            dateweekday_abbrev = gVars.wp_locale.get_weekday_abbrev(dateweekday);
            datemeridiem = gVars.wp_locale.get_meridiem(DateTime.date("a", i));
            datemeridiem_capital = gVars.wp_locale.get_meridiem(DateTime.date("A", i));
            dateformatstring = " " + dateformatstring;
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])D/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(dateweekday_abbrev), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])F/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemonth), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])l/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(dateweekday), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])M/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemonth_abbrev), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])a/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemeridiem), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])A/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemeridiem_capital), dateformatstring);
            dateformatstring = Strings.substr(dateformatstring, 1, Strings.strlen(dateformatstring) - 1);
        }

        j = DateTime.date(dateformatstring, i);

        /*
            if ( !$j ) // for debug purposes
                    echo $i." ".$mysqlstring;
            */
        return j;
    }

    public Object current_time(String type, int gmt) {
        return current_time(type, booleanval(gmt));
    }

    public Object /* String or int */ current_time(String type, boolean gmt) {
        {
            int javaSwitchSelector64 = 0;

            if (equal(type, "mysql")) {
                javaSwitchSelector64 = 1;
            }

            if (equal(type, "timestamp")) {
                javaSwitchSelector64 = 2;
            }

            switch (javaSwitchSelector64) {
            case 1:
                return gmt
                ? DateTime.gmdate("Y-m-d H:i:s")
                : DateTime.gmdate("Y-m-d H:i:s", intval(DateTime.time() + (floatval(get_option("gmt_offset")) * 3600)));

            case 2:
                return intval(gmt
                    ? DateTime.time()
                    : (DateTime.time() + (floatval(get_option("gmt_offset")) * 3600)));
            }
        }

        return 0;
    }

    public String date_i18n(String dateformatstring, int unixtimestamp) {
        int i = 0;
        String datemonth = null;
        String datemonth_abbrev = null;
        String dateweekday = null;
        String dateweekday_abbrev = null;
        String datemeridiem = null;
        String datemeridiem_capital = null;
        String j = null;
        i = unixtimestamp;

        if (!empty(gVars.wp_locale.month) && !empty(gVars.wp_locale.weekday)) {
            datemonth = gVars.wp_locale.get_month(DateTime.date("m", i));
            datemonth_abbrev = gVars.wp_locale.get_month_abbrev(datemonth);
            dateweekday = gVars.wp_locale.get_weekday(intval(DateTime.date("w", i)));
            dateweekday_abbrev = gVars.wp_locale.get_weekday_abbrev(dateweekday);
            datemeridiem = gVars.wp_locale.get_meridiem(DateTime.date("a", i));
            datemeridiem_capital = gVars.wp_locale.get_meridiem(DateTime.date("A", i));
            dateformatstring = " " + dateformatstring;
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])D/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(dateweekday_abbrev), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])F/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemonth), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])l/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(dateweekday), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])M/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemonth_abbrev), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])a/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemeridiem), dateformatstring);
            dateformatstring = QRegExPerl.preg_replace("/([^\\\\])A/", "\\1" + getIncluded(FormattingPage.class, gVars, gConsts).backslashit(datemeridiem_capital), dateformatstring);
            dateformatstring = Strings.substr(dateformatstring, 1, Strings.strlen(dateformatstring) - 1);
        }

        j = DateTime.date(dateformatstring, i);

        return j;
    }

    public String number_format_i18n(float number, Object decimals) {
        // let the user override the precision only
        decimals = (is_null(decimals)
            ? intval(gVars.wp_locale.number_format.getValue("decimals"))
            : intval(decimals));

        return Strings.number_format(number, intval(decimals), strval(gVars.wp_locale.number_format.getValue("decimal_point")), strval(gVars.wp_locale.number_format.getValue("thousands_sep")));
    }

    public String size_format(Object bytes, Object decimals) {
        Array<Object> quant = new Array<Object>();
        Object mag = null;
        Object unit = null;
        
    	// technically the correct unit names for powers of 1024 are KiB, MiB etc
    	// see http://en.wikipedia.org/wiki/Byte
        quant = new Array<Object>(
        		// ========================= Origin ====
                new ArrayEntry<Object>("TB", 1099511627776L),	// pow( 1024, 4)
                new ArrayEntry<Object>("GB", 1073741824),		// pow( 1024, 3)
                new ArrayEntry<Object>("MB", 1048576),			// pow( 1024, 2)
                new ArrayEntry<Object>("kB", 1024),				// pow( 1024, 1)
                new ArrayEntry<Object>("B ", 1));				// pow( 1024, 0)

        for (Map.Entry javaEntry466 : quant.entrySet()) {
            unit = javaEntry466.getKey();
            mag = javaEntry466.getValue();

            if (doubleval(bytes) >= floatval(mag)) {
                return number_format_i18n(floatval(bytes) / floatval(mag), decimals) + " " + strval(unit);
            }
        }

        return strval(false);
    }

    public Array<Object> get_weekstartend(String mysqlstring, Object start_of_week) {
        int my;
        int mm;
        int md;
        int day = 0;
        Object weekday = null;
        int i = 0;
        Array<Object> week = new Array<Object>();
        my = intval(Strings.substr(mysqlstring, 0, 4));
        mm = intval(Strings.substr(mysqlstring, 8, 2));
        md = intval(Strings.substr(mysqlstring, 5, 2));
        day = DateTime.mktime(0, 0, 0, md, mm, my);
        weekday = DateTime.date("w", day);
        i = 86400;

        if (!is_numeric(start_of_week)) {
            start_of_week = get_option("start_of_week");
        }

        if (intval(weekday) < intval(start_of_week)) {
            weekday = 7 - intval(start_of_week) - intval(weekday);
        }

        while (intval(weekday) > intval(start_of_week)) {
            weekday = DateTime.date("w", day);

            if (intval(weekday) < intval(start_of_week)) {
                weekday = 7 - intval(start_of_week) - intval(weekday);
            }

            day = day - 86400;
            i = 0;
        }

        week.putValue("start", (day + 86400) - i);
        week.putValue("end", intval(week.getValue("start")) + 604799);

        return week;
    }

    public Object maybe_unserialize(Object original) {
        Object gm = null;

        // Modified by Numiton
        //		if (is_serialized(original)) { // don't attempt to unserialize data that wasn't serialized going in
        if (!strictEqual(null, gm = unserialize(strval(original), false))) {
            return gm;
        }

        //		}
        return original;
    }

    // Modified by Numiton
    public boolean is_serialized(Object data) {
    	// if it isn't a string, it isn't serialized
        return VarHandling.unserialize(strval(data), false) != null;
    }

    // Modified by Numiton
    public boolean is_serialized_string(Object data) {
    	// if it isn't a string, it isn't a serialized string
        if (!is_string(data)) {
            return false;
        }

        return VarHandling.unserialize(strval(data), false) != null;
    }

    /* Options functions */

 // expects $setting to already be SQL-escaped
    public Object get_option(String setting) {
        Object pre;
        Array<Object> notoptions = new Array<Object>();
        Array<Object> alloptions = new Array<Object>();
        Object value = null;
        boolean supress = false;
        Object row;

        /* Do not change type */
        boolean suppress = false;
        
    	// Allow plugins to short-circuit options.
        pre = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_option_" + setting, false);

        if (!strictEqual(false, pre)) {
            return pre;
        }

    	// prevent non-existent options from triggering multiple queries
        notoptions = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("notoptions", "options");

        // Modified by Numiton
        if (isset(notoptions) && isset(notoptions.getValue(setting))) {
            return null;
        } else {
            notoptions = new Array<Object>();
        }

        alloptions = wp_load_alloptions();

        if ( /* Added by Numiton */
            isset(alloptions) && isset(alloptions.getValue(setting))) {
            value = alloptions.getValue(setting);
        } else {
            value = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(setting, "options");

            if (strictEqual(null, value) && /* Added by Numiton */
                    isset(gVars.wpdb)) {
                if (gConsts.isWP_INSTALLINGDefined()) {
                    supress = gVars.wpdb.suppress_errors();
                }

    			// expected_slashed ($setting)
                row = gVars.wpdb.get_row("SELECT option_value FROM " + gVars.wpdb.options + " WHERE option_name = \'" + setting + "\' LIMIT 1");

                if (gConsts.isWP_INSTALLINGDefined()) {
                    gVars.wpdb.suppress_errors(suppress);
                }

                if (is_object(row)) { // Has to be get_row instead of get_var because of funkiness with 0, false, null values
                    value = ((StdClass) row).fields.getValue("option_value");
                    getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(setting, value, "options", 0);
                } else { // option does not exist, so we must cache its non-existence
                    notoptions.putValue(setting, true);
                    getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("notoptions", notoptions, "options", 0);

                    return null;
                }
            }
        }

    	// If home is not set use siteurl.
        if (equal("home", setting) && equal("", value)) {
            return get_option("siteurl");
        }

        if (Array.in_array(setting, new Array<Object>(new ArrayEntry<Object>("siteurl"), new ArrayEntry<Object>("home"), new ArrayEntry<Object>("category_base"), new ArrayEntry<Object>("tag_base")))) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).untrailingslashit(strval(value));
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("option_" + setting, maybe_unserialize(value));
    }

    public void wp_protect_special_option(String option) {
        Array<Object> _protected = new Array<Object>();
        _protected = new Array<Object>(new ArrayEntry<Object>("alloptions"), new ArrayEntry<Object>("notoptions"));

        if (Array.in_array(option, _protected)) {
            System.exit(
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("%s is a protected WP option and may not be modified", "default"),
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(option, strval(0))));
        }
    }

    public void form_option(String option) {
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(get_option(option))));
    }

    public StdClass get_alloptions() {
        boolean show = false;
        Array<Object> options = new Array<Object>();
        StdClass option = null;
        Object value = null;
        StdClass all_options = new StdClass();
        show = gVars.wpdb.hide_errors();

        if (!booleanval(options = gVars.wpdb.get_results("SELECT option_name, option_value FROM " + gVars.wpdb.options + " WHERE autoload = \'yes\'"))) {
            options = gVars.wpdb.get_results("SELECT option_name, option_value FROM " + gVars.wpdb.options);
        }

        gVars.wpdb.show_errors(show);

        for (Map.Entry javaEntry467 : options.entrySet()) {
            option = (StdClass) javaEntry467.getValue();

    		// "When trying to design a foolproof system,
    		//  never underestimate the ingenuity of the fools :)" -- Dougal
            if (Array.in_array(StdClass.getValue(option, "option_name"), new Array<Object>(new ArrayEntry<Object>("siteurl"), new ArrayEntry<Object>("home"), new ArrayEntry<Object>("category_base")))) {
                option.fields.putValue("option_value", getIncluded(FormattingPage.class, gVars, gConsts).untrailingslashit(strval(StdClass.getValue(option, "option_value"))));
            }

            value = maybe_unserialize(strval(StdClass.getValue(option, "option_value")));
            all_options.fields.putValue(
                StdClass.getValue(option, "option_name"),
                getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_option_" + StdClass.getValue(option, "option_name"), value));
        }

        return (StdClass) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("all_options", all_options);
    }

    public Array<Object> wp_load_alloptions() {
        Array<Object> alloptions = new Array<Object>();
        boolean suppress;
        Array<Object> alloptions_db = new Array<Object>();
        StdClass o = null;
        alloptions = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("alloptions", "options");

        if (!booleanval(alloptions) && isset(gVars.wpdb) /* Added by Numiton */) {
            suppress = gVars.wpdb.suppress_errors();

            if (!booleanval(alloptions_db = gVars.wpdb.get_results("SELECT option_name, option_value FROM " + gVars.wpdb.options + " WHERE autoload = \'yes\'"))) {
                alloptions_db = gVars.wpdb.get_results("SELECT option_name, option_value FROM " + gVars.wpdb.options);
            }

            gVars.wpdb.suppress_errors(suppress);
            alloptions = new Array<Object>();

            for (Map.Entry javaEntry468 : new Array<Object>(alloptions_db).entrySet()) {
                o = (StdClass) javaEntry468.getValue();
                alloptions.putValue(StdClass.getValue(o, "option_name"), StdClass.getValue(o, "option_value"));
            }

            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("alloptions", alloptions, "options", 0);
        }

        return alloptions;
    }

 // expects $option_name to NOT be SQL-escaped
    public boolean update_option(String option_name, Object newvalue) {
        String safe_option_name = null;
        Object oldvalue = null;
        Object notoptions;

        /* Do not change type */
        Object _newvalue = null;
        Array<Object> alloptions = new Array<Object>();
        wp_protect_special_option(option_name);
        
        safe_option_name = gVars.wpdb.escape(option_name);
        newvalue = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_option(option_name, newvalue);
        
    	// If the new and old values are the same, no need to update.
        oldvalue = get_option(safe_option_name);

        if (strictEqual(newvalue, oldvalue)) {
            return false;
        }

        if (strictEqual(null, oldvalue)) {
            add_option(option_name, newvalue, "", "yes");

            return true;
        }

        notoptions = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("notoptions", "options");

        if (is_array(notoptions) && isset(((Array) notoptions).getValue(option_name))) {
            ((Array) notoptions).arrayUnset(option_name);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("notoptions", notoptions, "options", 0);
        }

        _newvalue = is_array(newvalue)
            ? Array.arrayCopy((Array) newvalue)
            : newvalue;
        newvalue = maybe_serialize(newvalue);
        alloptions = wp_load_alloptions();

        if (isset(alloptions.getValue(option_name))) {
            alloptions.putValue(option_name, newvalue);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("alloptions", alloptions, "options", 0);
        } else {
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set(option_name, newvalue, "options", 0);
        }

        gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.options + " SET option_value = %s WHERE option_name = %s", newvalue, option_name));

        if (equal(gVars.wpdb.rows_affected, 1)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("update_option_" + option_name, oldvalue, _newvalue);

            return true;
        }

        return false;
    }

 // thx Alex Stapleton, http://alex.vort-x.net/blog/
 // expects $name to NOT be SQL-escaped
    public void add_option(String name, Object value, String deprecated, String autoload) {
        String safe_name = null;
        Object notoptions;

        /* Do not change type */
        Array<Object> alloptions = new Array<Object>();
        wp_protect_special_option(name);
        safe_name = gVars.wpdb.escape(name);
        value = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_option(name, value);
        
    	// Make sure the option doesn't already exist. We can check the 'notoptions' cache before we ask for a db query
        notoptions = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("notoptions", "options");

        if (!is_array(notoptions) || !isset(((Array) notoptions).getValue(name))) {
            if (!strictEqual(null, get_option(safe_name))) {
                return;
            }
        }

        value = maybe_serialize(value);
        autoload = (strictEqual("no", autoload)
            ? "no"
            : "yes");

        if (equal("yes", autoload)) {
            alloptions = wp_load_alloptions();
            alloptions.putValue(name, value);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("alloptions", alloptions, "options", 0);
        } else {
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set(name, value, "options", 0);
        }

    	// This option exists now
        notoptions = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("notoptions", "options"); // yes, again... we need it to be fresh

        if (is_array(notoptions) && isset(((Array) notoptions).getValue(name))) {
            ((Array) notoptions).arrayUnset(name);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("notoptions", notoptions, "options", 0);
        }

        gVars.wpdb.query(gVars.wpdb.prepare("INSERT INTO " + gVars.wpdb.options + " (option_name, option_value, autoload) VALUES (%s, %s, %s)", name, value, autoload));
        getIncluded(PluginPage.class, gVars, gConsts).do_action("add_option_" + name, name, value);

        return;
    }

    public boolean delete_option(String name) {
        StdClass option;
        Array<Object> alloptions = new Array<Object>();
        
        wp_protect_special_option(name);
        
    	// Get the ID, if no ID then return
    	// expected_slashed ($name)
        option = (StdClass) gVars.wpdb.get_row("SELECT option_id, autoload FROM " + gVars.wpdb.options + " WHERE option_name = \'" + name + "\'");

        if (is_null(option) || !booleanval(StdClass.getValue(option, "option_id"))) {
            return false;
        }

    	// expected_slashed ($name)
        gVars.wpdb.query("DELETE FROM " + gVars.wpdb.options + " WHERE option_name = \'" + name + "\'");

        if (equal("yes", StdClass.getValue(option, "autoload"))) {
            alloptions = wp_load_alloptions();

            if (isset(alloptions.getValue(name))) {
                alloptions.arrayUnset(name);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("alloptions", alloptions, "options", 0);
            }
        } else {
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(name, "options");
        }

        return true;
    }

    public Object maybe_serialize(Object data) /* Do not change type */ {
        if (is_string(data)) {
            data = Strings.trim(strval(data));
        } else if (is_array(data) || is_object(data)) {
            return serialize(data);
        }

        if (is_serialized(data)) {
            return serialize(data);
        }

        return data;
    }

    public String make_url_footnote(String content) {
        Array<Array<Object>> matches = new Array<Array<Object>>();
        int j = 0;
        Object links_summary = null;
        String link_match = null;
        int i = 0;
        String link_number = null;
        String link_url = null;
        String link_text = null;
        QRegExPerl.preg_match_all("/<a(.+?)href=\\\"(.+?)\\\"(.*?)>(.+?)<\\/a>/", content, matches);
        j = 0;

        for (i = 0; i < Array.count(matches.getValue(0)); i++) {
            links_summary = ((!booleanval(j))
                ? "\n"
                : strval(links_summary));
            j++;
            link_match = strval(matches.getArrayValue(0).getValue(i));
            link_number = "[" + strval(i + 1) + "]";
            link_url = strval(matches.getArrayValue(2).getValue(i));
            link_text = strval(matches.getArrayValue(4).getValue(i));
            content = Strings.str_replace(link_match, link_text + " " + link_number, content);
            link_url = ((!equal(Strings.strtolower(Strings.substr(link_url, 0, 7)), "http://") && !equal(Strings.strtolower(Strings.substr(link_url, 0, 8)), "https://"))
                ? (strval(get_option("home")) + link_url)
                : link_url);
            links_summary = strval(links_summary) + "\n" + link_number + " " + link_url;
        }

        content = Strings.strip_tags(content);
        content = content + strval(links_summary);

        return content;
    }

    public String xmlrpc_getposttitle(String content) {
        Array<Object> matchtitle = new Array<Object>();
        String post_title = null;

        if (QRegExPerl.preg_match("/<title>(.+?)<\\/title>/is", content, matchtitle)) {
            post_title = strval(matchtitle.getValue(0));
            post_title = QRegExPerl.preg_replace("/<title>/si", "", post_title);
            post_title = QRegExPerl.preg_replace("/<\\/title>/si", "", post_title);
        } else {
            post_title = gVars.post_default_title;
        }

        return post_title;
    }

    public Object /*Array, String */ xmlrpc_getpostcategory(String content) {
        Array<Object> matchcat = new Array<Object>();
        Object post_category = null;

        if (QRegExPerl.preg_match("/<category>(.+?)<\\/category>/is", content, matchcat)) {
            post_category = Strings.trim(strval(matchcat.getValue(1)), ",");
            post_category = Strings.explode(",", strval(post_category));
        } else {
            post_category = post_default_category;
        }

        return post_category;
    }

    public String xmlrpc_removepostdata(String content) {
        content = QRegExPerl.preg_replace("/<title>(.+?)<\\/title>/si", "", content);
        content = QRegExPerl.preg_replace("/<category>(.+?)<\\/category>/si", "", content);
        content = Strings.trim(content);

        return content;
    }

    public int debug_fopen(String filename, String mode) {
        int fp = 0;

        if (equal(1, gVars.debug)) {
            fp = FileSystemOrSocket.fopen(gVars.webEnv, filename, mode);

            return fp;
        } else {
            return intval(false);
        }
    }

    public void debug_fwrite(int fp, String string) {
        if (equal(1, gVars.debug)) {
            FileSystemOrSocket.fwrite(gVars.webEnv, fp, string);
        }
    }

    public void debug_fclose(int fp) {
        if (equal(1, gVars.debug)) {
            FileSystemOrSocket.fclose(gVars.webEnv, fp);
        }
    }

    public void do_enclose(String content, int post_ID) {
        int log = 0;
        Array<Object> post_links = new Array<Object>();
        Array<Object> pung = new Array<Object>();
        String ltrs = null;
        String gunk = null;
        String punc = null;
        String any = null;
        Array post_links_temp = new Array();
        String link_test = null;
        Array<String> test;
        String url = null;
        Array<String> headers = new Array<String>();
        int len = 0;
        String type = null;
        Array<Object> allowed_types = new Array<Object>();
        String meta_value = null;

        log = debug_fopen(gConsts.getABSPATH() + "enclosures.log", "a");
        post_links = new Array<Object>();
        debug_fwrite(log, "BEGIN " + DateTime.date("YmdHis", DateTime.time()) + "\n");
        pung = getIncluded(PostPage.class, gVars, gConsts).get_enclosed(post_ID);
        ltrs = "\\w";
        gunk = "/#~:.?+=&%@!\\-";
        punc = ".:?\\-";
        any = ltrs + gunk + punc;
        QRegExPerl.preg_match_all("{\\b http : [" + any + "] +? (?= [" + punc + "] * [^" + any + "] | $)}x", content, post_links_temp);
        debug_fwrite(log, "Post contents:");
        debug_fwrite(log, content + "\n");

        for (Map.Entry javaEntry469 : (Set<Map.Entry>) post_links_temp.getArrayValue(0).entrySet()) {
            link_test = strval(javaEntry469.getValue());

            if (!Array.in_array(link_test, pung)) { // If we haven't pung it already
                test = URL.parse_url(link_test);

                if (isset(test.getValue("query"))) {
                    post_links.putValue(link_test);
                } else if (!equal(test.getValue("path"), "/") && !equal(test.getValue("path"), "")) {
                    post_links.putValue(link_test);
                }
            }
        }

        for (Map.Entry javaEntry470 : post_links.entrySet()) {
            url = strval(javaEntry470.getValue());

            if (!equal(url, "") &&
                    !booleanval(
                        gVars.wpdb.get_var(
                            gVars.wpdb.prepare("SELECT post_id FROM " + gVars.wpdb.postmeta + " WHERE post_id = %d AND meta_key = \'enclosure\' AND meta_value LIKE (%s)", post_ID, url + "%")))) {
                if (booleanval(headers = wp_get_http_headers(url, 1))) {
                    len = intval(headers.getValue("content-length"));
                    type = gVars.wpdb.escape(headers.getValue("content-type"));
                    allowed_types = new Array<Object>(new ArrayEntry<Object>("video"), new ArrayEntry<Object>("audio"));

                    if (Array.in_array(Strings.substr(type, 0, Strings.strpos(type, "/")), allowed_types)) {
                        meta_value = url + "\n" + strval(len) + "\n" + type + "\n";
                        gVars.wpdb.query(
                            gVars.wpdb.prepare("INSERT INTO `" + gVars.wpdb.postmeta + "` ( `post_id` , `meta_key` , `meta_value` )\n\t\t\t\t\tVALUES ( %d, \'enclosure\' , %s)", post_ID, meta_value));
                    }
                }
            }
        }
    }

 // perform a HTTP HEAD or GET request
 // if $file_path is a writable filename, this will do a GET request and write the file to that path
 // returns a list of HTTP headers
    public Array<String> wp_get_http(String url, String file_path, int red) {
        Array<String> parts = new Array<String>();
        String file = null;
        String host = null;
        String request_type = null;
        String head = null;
        int fp = 0;
        Ref<Integer> err_num = new Ref<Integer>();
        Ref<String> err_msg = new Ref<String>();
        String response = null;
        Array matches = new Array();
        int count = 0;
        String key = null;
        int i = 0;
        Array<String> headers = new Array<String>();
        Array<Object> _return = new Array<Object>();
        Object code = null;
        Object content_length = null;
        int got_bytes = 0;
        int out_fp = 0;
        String buf = null;
        Options.set_time_limit(gVars.webEnv, 60);

        if (red > 5) {
            return new Array<String>();
        }

        parts = URL.parse_url(url);
        file = parts.getValue("path") + (booleanval(parts.getValue("query"))
            ? ("?" + parts.getValue("query"))
            : "");
        host = parts.getValue("host");

        if (!isset(parts.getValue("port"))) {
            parts.putValue("port", 80);
        }

        if (booleanval(file_path)) {
            request_type = "GET";
        } else {
            request_type = "HEAD";
        }

        head = request_type + " " + file + " HTTP/1.1\r\nHOST: " + host + "\r\nUser-Agent: nWordPress/" + gVars.wp_version + "\r\n\r\n";
        fp = FileSystemOrSocket.fsockopen(gVars.webEnv, host, intval(parts.getValue("port")), err_num, err_msg, 3);

        if (!booleanval(fp)) {
            return new Array<String>();
        }

        response = "";
        FileSystemOrSocket.fputs(gVars.webEnv, fp, head);

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp) && equal(Strings.strpos(response, "\r\n\r\n"), false))
            response = response + FileSystemOrSocket.fgets(gVars.webEnv, fp, 2048);

        QRegExPerl.preg_match_all("/(.*?): (.*)\\r/", response, matches);
        count = Array.count(matches.getValue(1));

        for (i = 0; i < count; i++) {
            key = Strings.strtolower(strval(matches.getArrayValue(1).getValue(i)));
            headers.putValue(key, matches.getArrayValue(2).getValue(i));
        }

        QRegExPerl.preg_match("/.*([0-9]{3}).*/", response, _return);
        headers.putValue("response", _return.getValue(1)); // HTTP response code eg 204, 200, 404
        
        code = headers.getValue("response");

        if ((equal("302", code) || equal("301", code)) && isset(headers.getValue("location"))) {
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return wp_get_http(headers.getValue("location"), file_path, ++red);
        }

    	// make a note of the final location, so the caller can tell if we were redirected or not
        headers.putValue("x-final-location", url);

    	// HEAD request only
        if (!booleanval(file_path)) {
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return headers;
        }

    	// GET request - fetch and write it to the supplied filename
        content_length = headers.getValue("content-length");
        got_bytes = 0;
        out_fp = FileSystemOrSocket.fopen(gVars.webEnv, file_path, "w");

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
            buf = FileSystemOrSocket.fread(gVars.webEnv, fp, 4096);
            FileSystemOrSocket.fwrite(gVars.webEnv, out_fp, buf);
            got_bytes = got_bytes + Strings.strlen(buf);

    		// don't read past the content-length
            if (booleanval(content_length) && (got_bytes >= intval(content_length))) {
                break;
            }
        }

        FileSystemOrSocket.fclose(gVars.webEnv, out_fp);
        FileSystemOrSocket.fclose(gVars.webEnv, fp);

        return headers;
    }

    public Array<String> wp_get_http_headers(String url, int red) {
        return wp_get_http(url, "", red);
    }

    public int is_new_day() {
        if (!equal(gVars.day, gVars.previousday)) {
            return 1;
        } else {
            return 0;
        }
    }

    public String build_query(Array<Object> data) {
        return getIncluded(CompatPage.class, gVars, gConsts)._http_build_query(data, null, "&", "", false);
    }

    /*
    add_query_arg: Returns a modified querystring by adding
    a single key & value or an associative array.
    Setting a key value to emptystring removes the key.
    Omitting oldquery_or_uri uses the $_SERVER value.

    Parameters:
    add_query_arg(newkey, newvalue, oldquery_or_uri) or
    add_query_arg(associative_array, oldquery_or_uri)
    */
    public String add_query_arg(Object... args) {
        String ret = null;
        String uri = null;
        String frag = null;
        Array<Object> matches = new Array<Object>();
        String protocol = null;
        Array<String> parts = new Array<String>();
        String base = null;
        String query = null;
        Array<Object> qs = new Array<Object>();
        Array<Object> kayvees = null;
        Object v = null;
        Object k = null;
        ret = "";

        if (is_array(FunctionHandling.func_get_arg(args, 0))) {
            if ((FunctionHandling.func_num_args(args) < 2) || strictEqual(null, FunctionHandling.func_get_arg(args, 1))) {
                uri = gVars.webEnv.getRequestURI();
            } else {
                uri = strval(FunctionHandling.func_get_arg(args, 1));
            }
        } else {
            if ((FunctionHandling.func_num_args(args) < 3) || strictEqual(null, FunctionHandling.func_get_arg(args, 2))) {
                uri = gVars.webEnv.getRequestURI();
            } else {
                uri = strval(FunctionHandling.func_get_arg(args, 2));
            }
        }

        if (booleanval(frag = Strings.strstr(uri, "#"))) {
            uri = Strings.substr(uri, 0, -Strings.strlen(frag));
        } else {
            frag = "";
        }

        if (QRegExPerl.preg_match("|^https?://|i", uri, matches)) {
            protocol = strval(matches.getValue(0));
            uri = Strings.substr(uri, Strings.strlen(protocol));
        } else {
            protocol = "";
        }

        if (!strictEqual(Strings.strpos(uri, "?"), BOOLEAN_FALSE)) {
            parts = Strings.explode("?", uri, 2);

            if (equal(1, Array.count(parts))) {
                base = "?";
                query = parts.getValue(0);
            } else {
                base = parts.getValue(0) + "?";
                query = parts.getValue(1);
            }
        } else if (!empty(protocol) || strictEqual(Strings.strpos(uri, "="), BOOLEAN_FALSE)) {
            base = uri + "?";
            query = "";
        } else {
            base = "";
            query = uri;
        }

        getIncluded(FormattingPage.class, gVars, gConsts).wp_parse_str(query, qs);
        qs = (Array<Object>) getIncluded(FormattingPage.class, gVars, gConsts).urlencode_deep(qs); // this re-URL-encodes things that were already in the query string

        // Modified by Numiton
        if (is_array(FunctionHandling.func_get_arg(args, 0))) {
            kayvees = (Array<Object>) FunctionHandling.func_get_arg(args, 0);
            qs = Array.array_merge(qs, kayvees);
        } else {
            qs.putValue(FunctionHandling.func_get_arg(args, 0), FunctionHandling.func_get_arg(args, 1));
        }

        for (Map.Entry javaEntry471 : qs.entrySet()) {
            k = javaEntry471.getKey();
            v = javaEntry471.getValue();

            if (equal(v, false)) {
                qs.arrayUnset(k);
            }
        }

        ret = build_query(qs);
        ret = Strings.trim(ret, "?");
        ret = QRegExPerl.preg_replace("#=(&|$)#", "$1", ret);
        ret = protocol + base + ret + frag;
        ret = Strings.rtrim(ret, "?");

        return ret;
    }

    public String remove_query_arg(Object key) {
        return remove_query_arg(key, null);
    }

    /*
    remove_query_arg: Returns a modified querystring by removing
    a single key or an array of keys.
    Omitting oldquery_or_uri uses the $_SERVER value.

    Parameters:
    remove_query_arg(removekey, [oldquery_or_uri]) or
    remove_query_arg(removekeyarray, [oldquery_or_uri])
    */

    public String remove_query_arg(Object key, /* Do not change type */
        String query) {
        Object k = null;

        if (is_array(key)) { // removing multiple keys
            for (Map.Entry javaEntry472 : new Array<Object>(key).entrySet()) {
                k = javaEntry472.getValue();
                query = add_query_arg(k, false, query);
            }

            return query;
        }

        return add_query_arg(key, false, query);
    }

    public Array<Object> add_magic_quotes(Array<Object> array) {
        Object v = null;

        /* Do not change type */
        Object k = null;

        for (Map.Entry javaEntry473 : array.entrySet()) {
            k = javaEntry473.getKey();
            v = javaEntry473.getValue();

            if (is_array(v)) {
                array.putValue(k, add_magic_quotes((Array) v));
            } else {
                array.putValue(k, gVars.wpdb.escape(strval(v)));
            }
        }

        return array;
    }

    public String wp_remote_fopen(String uri) {
        int timeout = 0;
        Array parsed_url;

        /* Array or null */
        int fp = 0;
        String linea = null;
        String remote_read = null;
        int handle = 0;
        String buffer;
        timeout = 10;
        parsed_url = URL.parse_url(uri);

        if (!booleanval(parsed_url) || !is_array(parsed_url)) {
            return strval(false);
        }

        if (!isset(parsed_url.getValue("scheme")) || !Array.in_array(parsed_url.getValue("scheme"), new Array<Object>(new ArrayEntry<Object>("http"), new ArrayEntry<Object>("https")))) {
            uri = "http://" + uri;
        }

        if (booleanval(Options.ini_get(gVars.webEnv, "allow_url_fopen")))  {
            fp = FileSystemOrSocket.fopen(gVars.webEnv, uri, "r");

            if (!booleanval(fp)) {
                return strval(false);
            }

    		//stream_set_timeout($fp, $timeout); // Requires php 4.3
            linea = "";

            while (booleanval(remote_read = FileSystemOrSocket.fread(gVars.webEnv, fp, 4096)))
                linea = linea + remote_read;

            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return linea;
        } else if (true) /*Modified by Numiton*/ {
            handle = Curl.curl_init(gVars.webEnv);
            Curl.curl_setopt(gVars.webEnv, handle, Curl.CURLOPT_URL, uri);
            Curl.curl_setopt(gVars.webEnv, handle, Curl.CURLOPT_CONNECTTIMEOUT, 1);
            Curl.curl_setopt(gVars.webEnv, handle, Curl.CURLOPT_RETURNTRANSFER, 1);
            Curl.curl_setopt(gVars.webEnv, handle, Curl.CURLOPT_TIMEOUT, timeout);
            buffer = Curl.curl_exec(gVars.webEnv, handle);
            Curl.curl_close(gVars.webEnv, handle);

            return buffer;
        } else {
            return strval(false);
        }
    }

    public void wp(Object query_vars) {
        gVars.wp.main(query_vars);

        if (!isset(gVars.wp_the_query)) {
            gVars.wp_the_query = gVars.wp_query;
        }
    }

    @SuppressWarnings("unchecked")
    public String get_status_header_desc(int code) {
        code = absint(code);

        if (!isset(gVars.wp_header_to_desc)) {
            gVars.wp_header_to_desc = new Array<Object>(
                    new ArrayEntry<Object>(100, "Continue"),
                    new ArrayEntry<Object>(101, "Switching Protocols"),
                    
                    new ArrayEntry<Object>(200, "OK"),
                    new ArrayEntry<Object>(201, "Created"),
                    new ArrayEntry<Object>(202, "Accepted"),
                    new ArrayEntry<Object>(203, "Non-Authoritative Information"),
                    new ArrayEntry<Object>(204, "No Content"),
                    new ArrayEntry<Object>(205, "Reset Content"),
                    new ArrayEntry<Object>(206, "Partial Content"),
                    
                    new ArrayEntry<Object>(300, "Multiple Choices"),
                    new ArrayEntry<Object>(301, "Moved Permanently"),
                    new ArrayEntry<Object>(302, "Found"),
                    new ArrayEntry<Object>(303, "See Other"),
                    new ArrayEntry<Object>(304, "Not Modified"),
                    new ArrayEntry<Object>(305, "Use Proxy"),
                    new ArrayEntry<Object>(307, "Temporary Redirect"),
                    
                    new ArrayEntry<Object>(400, "Bad Request"),
                    new ArrayEntry<Object>(401, "Unauthorized"),
                    new ArrayEntry<Object>(403, "Forbidden"),
                    new ArrayEntry<Object>(404, "Not Found"),
                    new ArrayEntry<Object>(405, "Method Not Allowed"),
                    new ArrayEntry<Object>(406, "Not Acceptable"),
                    new ArrayEntry<Object>(407, "Proxy Authentication Required"),
                    new ArrayEntry<Object>(408, "Request Timeout"),
                    new ArrayEntry<Object>(409, "Conflict"),
                    new ArrayEntry<Object>(410, "Gone"),
                    new ArrayEntry<Object>(411, "Length Required"),
                    new ArrayEntry<Object>(412, "Precondition Failed"),
                    new ArrayEntry<Object>(413, "Request Entity Too Large"),
                    new ArrayEntry<Object>(414, "Request-URI Too Long"),
                    new ArrayEntry<Object>(415, "Unsupported Media Type"),
                    new ArrayEntry<Object>(416, "Requested Range Not Satisfiable"),
                    new ArrayEntry<Object>(417, "Expectation Failed"),
                    
                    new ArrayEntry<Object>(500, "Internal Server Error"),
                    new ArrayEntry<Object>(501, "Not Implemented"),
                    new ArrayEntry<Object>(502, "Bad Gateway"),
                    new ArrayEntry<Object>(503, "Service Unavailable"),
                    new ArrayEntry<Object>(504, "Gateway Timeout"),
                    new ArrayEntry<Object>(505, "HTTP Version Not Supported"));
        }

        if (isset(gVars.wp_header_to_desc.getValue(code))) {
            return strval(gVars.wp_header_to_desc.getValue(code));
        } else {
            return "";
        }
    }

    public void status_header(String header) {
        status_header(intval(header));
    }

    public void status_header(int header) {
        String text = null;
        String protocol = null;
        String status_header = null;
        text = get_status_header_desc(header);

        if (empty(text)) {
            return;
        }

        protocol = gVars.webEnv.getServerProtocol();

        if (!equal("HTTP/1.1", protocol) && !equal("HTTP/1.0", protocol)) {
            protocol = "HTTP/1.0";
        }

        status_header = protocol + " " + header + " " + text;

        if (true) /*Modified by Numiton*/ {
            status_header = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("status_header", status_header, header, text, protocol));
        }

        if (booleanval(Options.version_compare(Options.phpversion(), "4.3.0", ">="))) {
            Network.header(gVars.webEnv, status_header, true, header);
        } else {
            Network.header(gVars.webEnv, status_header);
        }
    }

    public void nocache_headers() {
    	// why are these @-silenced when other header calls aren't?
        Network.header(gVars.webEnv, "Expires: Wed, 11 Jan 1984 05:00:00 GMT");
        Network.header(gVars.webEnv, "Last-Modified: " + DateTime.gmdate("D, d M Y H:i:s") + " GMT");
        Network.header(gVars.webEnv, "Cache-Control: no-cache, must-revalidate, max-age=0");
        Network.header(gVars.webEnv, "Pragma: no-cache");
    }

    public void cache_javascript_headers() {
        int expiresOffset = 0;
        expiresOffset = 864000; // 10 days
        Network.header(gVars.webEnv, "Content-Type: text/javascript; charset=" + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("charset", "raw"));
        Network.header(gVars.webEnv, "Vary: Accept-Encoding"); // Handle proxies
        Network.header(gVars.webEnv, "Expires: " + DateTime.gmdate("D, d M Y H:i:s", DateTime.time() + expiresOffset) + " GMT");
    }

    public int get_num_queries() {
        return gVars.wpdb.num_queries;
    }

    public boolean bool_from_yn(String yn) {
        return equal(Strings.strtolower(yn), "y");
    }

    public void do_feed() {
        String feed;
        String hook = null;
        String message = null;
        
        feed = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("feed"));
        
    	// Remove the pad, if present.
        feed = QRegExPerl.preg_replace("/^_+/", "", feed);

        if (equal(feed, "") || equal(feed, "feed")) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        hook = "do_feed_" + feed;

        if (!booleanval(getIncluded(PluginPage.class, gVars, gConsts).has_action(hook, false))) {
            message = QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("ERROR: %s is not a valid feed template", "default"),
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(feed, strval(0)));
            wp_die(message, "");
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action(hook, gVars.wp_query.is_comment_feed);
    }

    public void do_feed_rdf() {
        getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + gConsts.getWPINC() + "/feed-rdf.php", Feed_rdfPage.class);
    }

    public void do_feed_rss() {
        getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + gConsts.getWPINC() + "/feed-rss.php", Feed_rssPage.class);
    }

    public void do_feed_rss2(Object for_comments) {
        if (booleanval(for_comments)) {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + gConsts.getWPINC() + "/feed-rss2-comments.php", Feed_rss2_commentsPage.class);
        } else {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + gConsts.getWPINC() + "/feed-rss2.php", Feed_rss2Page.class);
        }
    }

    public void do_feed_atom(Object for_comments) {
        if (booleanval(for_comments)) {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + gConsts.getWPINC() + "/feed-atom-comments.php", Feed_atom_commentsPage.class);
        } else {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + gConsts.getWPINC() + "/feed-atom.php", Feed_atomPage.class);
        }
    }

    public void do_robots() {
        Network.header(gVars.webEnv, "Content-Type: text/plain; charset=utf-8");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("do_robotstxt", "");

        if (equal("0", get_option("blog_public"))) {
            echo(gVars.webEnv, "User-agent: *\n");
            echo(gVars.webEnv, "Disallow: /\n");
        } else {
            echo(gVars.webEnv, "User-agent: *\n");
            echo(gVars.webEnv, "Disallow:\n");
        }
    }

    public boolean is_blog_installed() {
        boolean suppress;
        Boolean installed = null;

    	// Check cache first.  If options table goes away and we have true cached, oh well.
        if (booleanval(getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("is_blog_installed", ""))) {
            return true;
        }

        suppress = gVars.wpdb.suppress_errors();
        installed = booleanval(gVars.wpdb.get_var("SELECT option_value FROM " + gVars.wpdb.options + " WHERE option_name = \'siteurl\'"));
        gVars.wpdb.suppress_errors(suppress);
        
        installed = ((!empty(installed))
            ? true
            : false);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("is_blog_installed", installed, "", 0);

        return installed;
    }

    public String wp_nonce_url(String actionurl, String action) {
        actionurl = Strings.str_replace("&amp;", "&", actionurl);

        return getIncluded(FormattingPage.class, gVars, gConsts)
                   .wp_specialchars(add_query_arg("_wpnonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce(action), actionurl), strval(0));
    }

    public String wp_nonce_field(String action) {
        return wp_nonce_field(action, "_wpnonce", true, true);
    }

    public String wp_nonce_field(String action, String name, boolean referer, boolean echo) {
        String nonce_field = null;
        name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(name);
        nonce_field = "<input type=\"hidden\" id=\"" + name + "\" name=\"" + name + "\" value=\"" + getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce(action) + "\" />";

        if (echo) {
            echo(gVars.webEnv, nonce_field);
        }

        if (referer) {
            wp_referer_field(echo);
        }

        /* , "previous" */
        return nonce_field;
    }

    public Object wp_referer_field(boolean echo) {
        Object ref = null;
        Object referer_field = null;
        ref = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.webEnv.getRequestURI());
        referer_field = "<input type=\"hidden\" name=\"_wp_http_referer\" value=\"" + strval(ref) + "\" />";

        if (echo) {
            echo(gVars.webEnv, referer_field);
        }

        return referer_field;
    }

    public String wp_original_referer_field(boolean echo, String jump_back_to) {
        String ref = null;
        String orig_referer_field = null;
        jump_back_to = (equal("previous", jump_back_to)
            ? wp_get_referer()
            : gVars.webEnv.getRequestURI());
        ref = (booleanval(wp_get_original_referer())
            ? wp_get_original_referer()
            : jump_back_to);
        orig_referer_field = "<input type=\"hidden\" name=\"_wp_original_http_referer\" value=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, ref)) + "\" />";

        if (echo) {
            echo(gVars.webEnv, orig_referer_field);
        }

        return orig_referer_field;
    }

    public String wp_get_referer() {
        String ref = null;

        if (!empty(gVars.webEnv._REQUEST.getValue("_wp_http_referer"))) {
            ref = strval(gVars.webEnv._REQUEST.getValue("_wp_http_referer"));
        } else if (!empty(gVars.webEnv.getHttpReferer())) {
            ref = gVars.webEnv.getHttpReferer();
        }

        if (!strictEqual(ref, gVars.webEnv.getRequestURI())) {
            return ref;
        }

        return strval(false);
    }

    public String wp_get_original_referer() {
        if (!empty(gVars.webEnv._REQUEST.getValue("_wp_original_http_referer"))) {
            return strval(gVars.webEnv._REQUEST.getValue("_wp_original_http_referer"));
        }

        return "";
    }

    public boolean wp_mkdir_p(String target) {
        Array<Object> stat = new Array<Object>();
        int dir_perms = 0;
        
    	// from php.net/mkdir user contributed notes
        target = Strings.str_replace("//", "/", target);

        if (FileSystemOrSocket.file_exists(gVars.webEnv, target)) {
            return FileSystemOrSocket.is_dir(gVars.webEnv, target);
        }

    	// Attempting to create the directory may clutter up our display.
        if (JFileSystemOrSocket.mkdir(gVars.webEnv, target)) {
            stat = QFileSystemOrSocket.stat(gVars.webEnv, FileSystemOrSocket.dirname(target));
            dir_perms = intval(stat.getValue("mode")) & 0007777; // Get the permission bits.
            JFileSystemOrSocket.chmod(gVars.webEnv, target, dir_perms);

            return true;
        } else if (FileSystemOrSocket.is_dir(gVars.webEnv, FileSystemOrSocket.dirname(target))) {
            return false;
        }

    	// If the above failed, attempt to create the parent node, then try again.
        if (wp_mkdir_p(FileSystemOrSocket.dirname(target))) {
            return wp_mkdir_p(target);
        }

        return false;
    }

 // Test if a give filesystem path is absolute ('/foo/bar', 'c:\windows')
    public boolean path_is_absolute(String path) {
    	// this is definitive if true but fails if $path does not exist or contains a symbolic link
        if (equal(FileSystemOrSocket.realpath(gVars.webEnv, path), path)) {
            return true;
        }

        if (equal(Strings.strlen(path), 0) || equal(Strings.getCharAt(path, 0), ".")) {
            return false;
        }

    	// windows allows absolute paths like this
        if (QRegExPerl.preg_match("#^[a-zA-Z]:\\\\#", path)) {
            return true;
        }

    	// a path starting with / or \ is absolute; anything else is relative
        return QRegExPerl.preg_match("#^[/\\\\]#", path);
    }

 // Join two filesystem paths together (e.g. 'give me $path relative to $base')
    public String path_join(String base, String path) {
        if (path_is_absolute(path)) {
            return path;
        }

        return Strings.rtrim(base, "/") + "/" + Strings.ltrim(path, "/");
    }

 // Returns an array containing the current upload directory's path and url, or an error message.
    public Array<Object> wp_upload_dir(String time) {
        String siteurl = null;
        String upload_path = null;
        String dir = null;
        String path;
        Object url = null;
        String subdir = null;
        String y = null;
        String m = null;
        String message = null;
        Array<Object> uploads = new Array<Object>();
        siteurl = strval(get_option("siteurl"));
        upload_path = strval(get_option("upload_path"));

        if (strictEqual(Strings.trim(upload_path), "")) {
            upload_path = "wp-content/uploads";
        }

        dir = upload_path;
        
    	// $dir is absolute, $path is (maybe) relative to ABSPATH
        dir = path_join(gConsts.getABSPATH(), upload_path);
        path = Strings.str_replace(gConsts.getABSPATH(), "", Strings.trim(upload_path));

        if (!booleanval(url = get_option("upload_url_path"))) {
            url = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(siteurl) + path;
        }

        if (gConsts.isUPLOADSDefined()) {
            dir = gConsts.getABSPATH() + gConsts.getUPLOADS();
            url = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(siteurl) + gConsts.getUPLOADS();
        }

        subdir = "";

        if (booleanval(get_option("uploads_use_yearmonth_folders"))) {
    		// Generate the yearly and monthly dirs
            if (!booleanval(time)) {
                time = strval(current_time("mysql", 0));
            }

            y = Strings.substr(time, 0, 4);
            m = Strings.substr(time, 5, 2);
            subdir = "/" + y + "/" + m;
        }

        dir = dir + subdir;
        url = strval(url) + subdir;

    	// Make sure we have an uploads dir
        if (!wp_mkdir_p(dir)) {
            message = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Unable to create directory %s. Is its parent directory writable by the server?", "default"), dir);

            return new Array<Object>(new ArrayEntry<Object>("error", message));
        }

        uploads = new Array<Object>(new ArrayEntry<Object>("path", dir), new ArrayEntry<Object>("url", url), new ArrayEntry<Object>("subdir", subdir), new ArrayEntry<Object>("error", false));

        return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("upload_dir", uploads);
    }

    /**
     * return a filename that is sanitized and unique for the given directory
     */
    public String wp_unique_filename(String dir, String filename, Callback unique_filename_callback) {
        Array<Object> info = new Array<Object>();
        String ext = null;
        String name = null;
        String number;
        
        filename = Strings.strtolower(filename);
    	// separate the filename into a name and extension
        info = FileSystemOrSocket.pathinfo(filename);
        ext = strval(info.getValue("extension"));
        name = FileSystemOrSocket.basename(filename, "." + ext);

    	// edge case: if file is named '.ext', treat as an empty name
        if (strictEqual(name, "." + ext)) {
            name = "";
        }

        // Modified by Numiton
    	// Increment the file number until we have a unique file to save in $dir. Use $override['unique_filename_callback'] if supplied.
        if (booleanval(unique_filename_callback) && booleanval(unique_filename_callback.getMethodName()) && VarHandling.is_callable(unique_filename_callback)) {
            filename = strval(FunctionHandling.call_user_func(unique_filename_callback, dir, name));
        } else /*
        * % so the server doesn't try to decode entities. so the server doesn't
        * try to decode entities.
        */
         {
            number = "";

            if (empty(ext)) {
                ext = "";
            } else {
                ext = Strings.strtolower("." + ext);
            }

            filename = Strings.str_replace(ext, "", filename);
    		// Strip % so the server doesn't try to decode entities.
            filename = Strings.str_replace("%", "", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title_with_dashes(filename)) + ext;

            while (FileSystemOrSocket.file_exists(gVars.webEnv, dir + "/" + filename)) {
                // Modified by Numiton. Awful PHP code
                if (equal("", number + ext)) {
                    filename = filename + strval(number = strval(intval(number) + 1)) + ext;
                } else {
                    filename = Strings.str_replace(number + ext, strval(number = strval(intval(number) + 1)) + ext, filename);
                }
            }
        }

        return filename;
    }

    public Array<Object> wp_upload_bits(String name, int deprecated, String bits, String time) {
        Array<Object> wp_filetype = new Array<Object>();
        Array<Object> upload = new Array<Object>();
        String filename = null;
        String new_file = null;
        String message = null;
        int ifp = 0;
        Array<Object> stat = new Array<Object>();
        int perms = 0;
        String url = null;

        if (empty(name)) {
            return new Array<Object>(new ArrayEntry<Object>("error", getIncluded(L10nPage.class, gVars, gConsts).__("Empty filename", "default")));
        }

        wp_filetype = wp_check_filetype(name, null);

        if (!booleanval(wp_filetype.getValue("ext"))) {
            return new Array<Object>(new ArrayEntry<Object>("error", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid file type", "default")));
        }

        upload = wp_upload_dir(time);

        if (!equal(upload.getValue("error"), false)) {
            return upload;
        }

        filename = wp_unique_filename(strval(upload.getValue("path")), name, null);
        new_file = strval(upload.getValue("path")) + "/" + filename;

        if (!wp_mkdir_p(FileSystemOrSocket.dirname(new_file))) {
            message = QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Unable to create directory %s. Is its parent directory writable by the server?", "default"),
                    FileSystemOrSocket.dirname(new_file));

            return new Array<Object>(new ArrayEntry<Object>("error", message));
        }

        ifp = FileSystemOrSocket.fopen(gVars.webEnv, new_file, "wb");

        if (!booleanval(ifp)) {
            return new Array<Object>(new ArrayEntry<Object>("error", QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Could not write file %s", "default"), new_file)));
        }

        FileSystemOrSocket.fwrite(gVars.webEnv, ifp, bits);
        FileSystemOrSocket.fclose(gVars.webEnv, ifp);
    	// Set correct file permissions
        stat = QFileSystemOrSocket.stat(gVars.webEnv, FileSystemOrSocket.dirname(new_file));
        perms = intval(stat.getValue("mode")) & 0007777;
        perms = perms & 0000666;
        JFileSystemOrSocket.chmod(gVars.webEnv, new_file, perms);
        
    	// Compute the URL
        url = strval(upload.getValue("url")) + "/" + filename;

        return new Array<Object>(new ArrayEntry<Object>("file", new_file), new ArrayEntry<Object>("url", url), new ArrayEntry<Object>("error", false));
    }

    public String wp_ext2type(String ext) {
        Array<Object> ext2type = null;
        Array<Object> exts = null;
        String type = null;
        ext2type = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("ext2type",
                new Array<Object>(new ArrayEntry<Object>("audio",
                        new Array<Object>(new ArrayEntry<Object>("aac"),
                            new ArrayEntry<Object>("ac3"),
                            new ArrayEntry<Object>("aif"),
                            new ArrayEntry<Object>("aiff"),
                            new ArrayEntry<Object>("mp1"),
                            new ArrayEntry<Object>("mp2"),
                            new ArrayEntry<Object>("mp3"),
                            new ArrayEntry<Object>("m3a"),
                            new ArrayEntry<Object>("m4a"),
                            new ArrayEntry<Object>("m4b"),
                            new ArrayEntry<Object>("ogg"),
                            new ArrayEntry<Object>("ram"),
                            new ArrayEntry<Object>("wav"),
                            new ArrayEntry<Object>("wma"))),
                    new ArrayEntry<Object>("video",
                        new Array<Object>(new ArrayEntry<Object>("asf"),
                            new ArrayEntry<Object>("avi"),
                            new ArrayEntry<Object>("divx"),
                            new ArrayEntry<Object>("dv"),
                            new ArrayEntry<Object>("mov"),
                            new ArrayEntry<Object>("mpg"),
                            new ArrayEntry<Object>("mpeg"),
                            new ArrayEntry<Object>("mp4"),
                            new ArrayEntry<Object>("mpv"),
                            new ArrayEntry<Object>("ogm"),
                            new ArrayEntry<Object>("qt"),
                            new ArrayEntry<Object>("rm"),
                            new ArrayEntry<Object>("vob"),
                            new ArrayEntry<Object>("wmv"))),
                    new ArrayEntry<Object>(
                        "document",
                        new Array<Object>(new ArrayEntry<Object>("doc"), new ArrayEntry<Object>("pages"), new ArrayEntry<Object>("odt"), new ArrayEntry<Object>("rtf"), new ArrayEntry<Object>("pdf"))),
                    new ArrayEntry<Object>("spreadsheet", new Array<Object>(new ArrayEntry<Object>("xls"), new ArrayEntry<Object>("numbers"), new ArrayEntry<Object>("ods"))),
                    new ArrayEntry<Object>("interactive", new Array<Object>(new ArrayEntry<Object>("ppt"), new ArrayEntry<Object>("key"), new ArrayEntry<Object>("odp"), new ArrayEntry<Object>("swf"))),
                    new ArrayEntry<Object>("text", new Array<Object>(new ArrayEntry<Object>("txt"))),
                    new ArrayEntry<Object>("archive",
                        new Array<Object>(new ArrayEntry<Object>("tar"),
                            new ArrayEntry<Object>("bz2"),
                            new ArrayEntry<Object>("gz"),
                            new ArrayEntry<Object>("cab"),
                            new ArrayEntry<Object>("dmg"),
                            new ArrayEntry<Object>("rar"),
                            new ArrayEntry<Object>("sea"),
                            new ArrayEntry<Object>("sit"),
                            new ArrayEntry<Object>("sqx"),
                            new ArrayEntry<Object>("zip"))),
                    new ArrayEntry<Object>("code", new Array<Object>(new ArrayEntry<Object>("css"), new ArrayEntry<Object>("html"), new ArrayEntry<Object>("php"), new ArrayEntry<Object>("js")))));

        for (Map.Entry javaEntry474 : ext2type.entrySet()) {
            type = strval(javaEntry474.getKey());
            exts = (Array<Object>) javaEntry474.getValue();

            if (Array.in_array(ext, exts)) {
                return type;
            }
        }

        return "";
    }

    public Array<Object> wp_check_filetype(String filename, Object mimes) /* Do not change type */ {
        Object type = null;
        Object ext = null;
        String ext_preg = null;
        Array<Object> ext_matches = new Array<Object>();
        Object mime_match = null;
        
    	// Accepted MIME types are set here as PCRE unless provided.
        mimes = (is_array(mimes)
            ? mimes
            : getIncluded(PluginPage.class, gVars, gConsts).apply_filters("upload_mimes",
                new Array<Object>(new ArrayEntry<Object>("jpg|jpeg|jpe", "image/jpeg"),
                    new ArrayEntry<Object>("gif", "image/gif"),
                    new ArrayEntry<Object>("png", "image/png"),
                    new ArrayEntry<Object>("bmp", "image/bmp"),
                    new ArrayEntry<Object>("tif|tiff", "image/tiff"),
                    new ArrayEntry<Object>("ico", "image/x-icon"),
                    new ArrayEntry<Object>("asf|asx|wax|wmv|wmx", "video/asf"),
                    new ArrayEntry<Object>("avi", "video/avi"),
                    new ArrayEntry<Object>("mov|qt", "video/quicktime"),
                    new ArrayEntry<Object>("mpeg|mpg|mpe|mp4", "video/mpeg"),
                    new ArrayEntry<Object>("txt|c|cc|h", "text/plain"),
                    new ArrayEntry<Object>("rtx", "text/richtext"),
                    new ArrayEntry<Object>("css", "text/css"),
                    new ArrayEntry<Object>("htm|html", "text/html"),
                    new ArrayEntry<Object>("mp3|m4a", "audio/mpeg"),
                    new ArrayEntry<Object>("ra|ram", "audio/x-realaudio"),
                    new ArrayEntry<Object>("wav", "audio/wav"),
                    new ArrayEntry<Object>("ogg", "audio/ogg"),
                    new ArrayEntry<Object>("mid|midi", "audio/midi"),
                    new ArrayEntry<Object>("wma", "audio/wma"),
                    new ArrayEntry<Object>("rtf", "application/rtf"),
                    new ArrayEntry<Object>("js", "application/javascript"),
                    new ArrayEntry<Object>("pdf", "application/pdf"),
                    new ArrayEntry<Object>("doc", "application/msword"),
                    new ArrayEntry<Object>("pot|pps|ppt", "application/vnd.ms-powerpoint"),
                    new ArrayEntry<Object>("wri", "application/vnd.ms-write"),
                    new ArrayEntry<Object>("xla|xls|xlt|xlw", "application/vnd.ms-excel"),
                    new ArrayEntry<Object>("mdb", "application/vnd.ms-access"),
                    new ArrayEntry<Object>("mpp", "application/vnd.ms-project"),
                    new ArrayEntry<Object>("swf", "application/x-shockwave-flash"),
                    new ArrayEntry<Object>("class", "application/java"),
                    new ArrayEntry<Object>("tar", "application/x-tar"),
                    new ArrayEntry<Object>("zip", "application/zip"),
                    new ArrayEntry<Object>("gz|gzip", "application/x-gzip"),
                    new ArrayEntry<Object>("exe", "application/x-msdownload"),
                    // openoffice formats
                    new ArrayEntry<Object>("odt", "application/vnd.oasis.opendocument.text"),
                    new ArrayEntry<Object>("odp", "application/vnd.oasis.opendocument.presentation"),
                    new ArrayEntry<Object>("ods", "application/vnd.oasis.opendocument.spreadsheet"),
                    new ArrayEntry<Object>("odg", "application/vnd.oasis.opendocument.graphics"),
                    new ArrayEntry<Object>("odc", "application/vnd.oasis.opendocument.chart"),
                    new ArrayEntry<Object>("odb", "application/vnd.oasis.opendocument.database"),
                    new ArrayEntry<Object>("odf", "application/vnd.oasis.opendocument.formula"))));
        
        type = false;
        ext = false;

        for (Map.Entry javaEntry475 : ((Array<?>) mimes).entrySet()) {
            ext_preg = strval(javaEntry475.getKey());
            mime_match = javaEntry475.getValue();
            ext_preg = "!\\.(" + ext_preg + ")$!i";

            if (QRegExPerl.preg_match(ext_preg, filename, ext_matches)) {
                type = mime_match;
                ext = ext_matches.getValue(1);

                break;
            }
        }

        return Array.compact(new ArrayEntry("ext", ext), new ArrayEntry("type", type));
    }

    @SuppressWarnings("unchecked")
    public String wp_explain_nonce(Object action) {
        Array<Object> matches = new Array<Object>();
        Object verb = null;
        Object noun = null;
        Array<Object> trans = new Array<Object>();
        Object lookup = null;
        String object = null;

        if (!equal(action, -1) && QRegExPerl.preg_match("/([a-z]+)-([a-z]+)(_(.+))?/", strval(action), matches)) {
            verb = matches.getValue(1);
            noun = matches.getValue(2);
            trans = new Array<Object>();
            trans.getArrayValue("update").putValue(
                "attachment",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this attachment: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry(Callback.createCallbackArray(getIncluded(Post_templatePage.class, gVars, gConsts), "get_the_title"))));
            trans.getArrayValue("add").putValue(
                "category",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to add this category has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("delete").putValue(
                "category",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to delete this category: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>(Callback.createCallbackArray(getIncluded(CategoryPage.class, gVars, gConsts), "get_catname"))));
            trans.getArrayValue("update").putValue(
                "category",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this category: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>(Callback.createCallbackArray(getIncluded(CategoryPage.class, gVars, gConsts), "get_catname"))));
            trans.getArrayValue("delete").putValue(
                "comment",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to delete this comment: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("unapprove").putValue(
                "comment",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to unapprove this comment: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("approve").putValue(
                "comment",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to approve this comment: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("update").putValue(
                "comment",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this comment: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("bulk").putValue(
                "comments",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to bulk modify comments has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("moderate").putValue(
                "comments",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to moderate comments has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("add").putValue(
                "bookmark",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to add this link has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("delete").putValue(
                "bookmark",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to delete this link: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("update").putValue(
                "bookmark",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this link: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("bulk").putValue(
                "bookmarks",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to bulk modify links has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("add").putValue(
                "page",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to add this page has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("delete").putValue(
                "page",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to delete this page: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry(Callback.createCallbackArray(getIncluded(Post_templatePage.class, gVars, gConsts), "get_the_title"))));
            trans.getArrayValue("update").putValue(
                "page",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this page: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry(Callback.createCallbackArray(getIncluded(Post_templatePage.class, gVars, gConsts), "get_the_title"))));
            trans.getArrayValue("edit").putValue(
                "plugin",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this plugin file: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("activate").putValue(
                "plugin",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to activate this plugin: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("deactivate").putValue(
                "plugin",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to deactivate this plugin: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("upgrade").putValue(
                "plugin",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to upgrade this plugin: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("add").putValue(
                "post",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to add this post has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("delete").putValue(
                "post",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to delete this post: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry(Callback.createCallbackArray(getIncluded(Post_templatePage.class, gVars, gConsts), "get_the_title"))));
            trans.getArrayValue("update").putValue(
                "post",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this post: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry(Callback.createCallbackArray(getIncluded(Post_templatePage.class, gVars, gConsts), "get_the_title"))));
            trans.getArrayValue("add").putValue(
                "user",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to add this user has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("delete").putValue(
                "users",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to delete users has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("bulk").putValue(
                "users",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to bulk modify users has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("update").putValue(
                "user",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this user: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>(Callback.createCallbackArray(getIncluded(Author_templatePage.class, gVars, gConsts), "get_author_name"))));
            trans.getArrayValue("update").putValue(
                "profile",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to modify the profile for: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>(Callback.createCallbackArray(getIncluded(Author_templatePage.class, gVars, gConsts), "get_author_name"))));
            trans.getArrayValue("update").putValue(
                "options",
                new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit your settings has failed.", "default")), new ArrayEntry<Object>(null)));
            trans.getArrayValue("update").putValue(
                "permalink",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to change your permalink structure to: %s has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("edit").putValue(
                "file",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this file: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("edit").putValue(
                "theme",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to edit this theme file: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));
            trans.getArrayValue("switch").putValue(
                "theme",
                new Array<Object>(
                    new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Your attempt to switch to this theme: &quot;%s&quot; has failed.", "default")),
                    new ArrayEntry<Object>("use_id")));

            if (isset(trans.getArrayValue(verb).getValue(noun))) {
                if (!empty(trans.getArrayValue(verb).getArrayValue(noun).getValue(1))) {
                    lookup = trans.getArrayValue(verb).getArrayValue(noun).getValue(1);
                    object = strval(matches.getValue(4));

                    if (!equal("use_id", lookup)) {
                        object = strval(FunctionHandling.call_user_func(new Callback((Array) lookup), object));
                    }

                    return QStrings.sprintf(strval(trans.getArrayValue(verb).getArrayValue(noun).getValue(0)), getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(object, strval(0)));
                } else {
                    return strval(trans.getArrayValue(verb).getArrayValue(noun).getValue(0));
                }
            }
        }

        return strval(
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "explain_nonce_" + strval(verb) + "-" + strval(noun),
                getIncluded(L10nPage.class, gVars, gConsts).__("Are you sure you want to do this?", "default"),
                matches.getValue(4)));
    }

    public void wp_nonce_ays(String action) {
        Object title = null;
        Object html = null;
        title = getIncluded(L10nPage.class, gVars, gConsts).__("nWordPress Failure Notice", "default");
        html = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(wp_explain_nonce(action), strval(0)) + "</p>";

        if (booleanval(wp_get_referer())) {
            html = strval(html) + "<p><a href=\'" + remove_query_arg("updated", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(wp_get_referer(), null, "display")) + "\'>" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Please try again.", "default") + "</a>";
        }

        wp_die(html, title);
    }

    public void wp_die(Object message, Object title) {
        Object error_data;

        /* Do not change type */
        Array errors = new Array();
        Object admin_dir = null;

        if (true && /*Modified by Numiton*/
                getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(message)) {
            if (empty(title)) {
                error_data = ((WP_Error) message).get_error_data();

                if (is_array(error_data) && isset(((Array) error_data).getValue("title"))) {
                    title = ((Array) error_data).getValue("title");
                }
            }

            errors = ((WP_Error) message).get_error_messages();

            switch (Array.count(errors)) {
            case 0: {
                message = "";

                break;
            }

            case 1: {
                message = "<p>" + strval(errors.getValue(0)) + "</p>";

                break;
            }

            default: {
                message = "<ul>\n\t\t<li>" + Strings.join("</li>\n\t\t<li>", errors) + "</li>\n\t</ul>";

                break;
            }
            }
        } else if (is_string(message)) {
            message = "<p>" + strval(message) + "</p>";
        }

        if (gConsts.isWP_SITEURLDefined() && !equal("", gConsts.getWP_SITEURL())) {
            admin_dir = gConsts.getWP_SITEURL() + "/wp-admin/";
        } else if (true && /*Modified by Numiton*/
                !equal("", getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("wpurl", "raw"))) {
            admin_dir = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("wpurl", "raw") + "/wp-admin/";
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "wp-admin"), BOOLEAN_FALSE)) {
            admin_dir = "";
        } else {
            admin_dir = "wp-admin/";
        }

        if (!true || /*Modified by Numiton*/
                !booleanval(getIncluded(PluginPage.class, gVars, gConsts).did_action("admin_head"))) {
            if (!Network.headers_sent(gVars.webEnv)) {
                status_header(strval(500));
                nocache_headers();
                Network.header(gVars.webEnv, "Content-Type: text/html; charset=utf-8");
            }

            if (empty(title)) {
                if (true) /*Modified by Numiton*/ {
                    title = getIncluded(L10nPage.class, gVars, gConsts).__("nWordPress &rsaquo; Error", "default");
                } else {
                    title = "nWordPress &rsaquo; Error";
                }
            }

            echo(
                gVars.webEnv,
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ");

            if (true) /*Modified by Numiton*/ {
                getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");
            }

            echo(gVars.webEnv, ">\n<head>\n\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n\t<title>");
            echo(gVars.webEnv, title);
            echo(gVars.webEnv, "</title>\n\t<link rel=\"stylesheet\" href=\"");
            echo(gVars.webEnv, admin_dir);
            echo(gVars.webEnv, "css/install.css\" type=\"text/css\" />\n");

            if (booleanval(gVars.wp_locale) && equal("rtl", gVars.wp_locale.text_direction)) {
                echo(gVars.webEnv, "\t<link rel=\"stylesheet\" href=\"");
                echo(gVars.webEnv, admin_dir);
                echo(gVars.webEnv, "css/install-rtl.css\" type=\"text/css\" />\n");
            } else {
            }

            echo(gVars.webEnv, "</head>\n<body id=\"error-page\">\n");
        } else {
        }

        echo(gVars.webEnv, "\t");
        echo(gVars.webEnv, message);
        echo(gVars.webEnv, "</body>\n</html>\n");
        System.exit();
    }

    public String _config_wp_home(String url) {
        if (gConsts.isWP_HOMEDefined()) {
            return gConsts.getWP_HOME();
        }

        return url;
    }

    public String _config_wp_siteurl(String url) {
        if (gConsts.isWP_SITEURLDefined()) {
            return gConsts.getWP_SITEURL();
        }

        return url;
    }

    public Array<Object> _mce_set_direction(Array<Object> input) {
        if (equal("rtl", gVars.wp_locale.text_direction)) {
            input.putValue("directionality", "rtl");
            input.putValue("plugins", strval(input.getValue("plugins")) + ",directionality");
            input.putValue("theme_advanced_buttons1", strval(input.getValue("theme_advanced_buttons1")) + ",ltr");
        }

        return input;
    }

    public void smilies_init() {
        Object siteurl = null;
        String smiley = null;
        Object smiley_masked = null;
        Object img = null;

    	// don't bother setting up smilies if they are disabled
        if (!booleanval(get_option("use_smilies"))) {
            return;
        }

        if (!isset(wpsmiliestrans)) {
            wpsmiliestrans = new Array<Object>(
                    new ArrayEntry<Object>(":mrgreen:", "icon_mrgreen.gif"),
                    new ArrayEntry<Object>(":neutral:", "icon_neutral.gif"),
                    new ArrayEntry<Object>(":twisted:", "icon_twisted.gif"),
                    new ArrayEntry<Object>(":arrow:", "icon_arrow.gif"),
                    new ArrayEntry<Object>(":shock:", "icon_eek.gif"),
                    new ArrayEntry<Object>(":smile:", "icon_smile.gif"),
                    new ArrayEntry<Object>(":???:", "icon_confused.gif"),
                    new ArrayEntry<Object>(":cool:", "icon_cool.gif"),
                    new ArrayEntry<Object>(":evil:", "icon_evil.gif"),
                    new ArrayEntry<Object>(":grin:", "icon_biggrin.gif"),
                    new ArrayEntry<Object>(":idea:", "icon_idea.gif"),
                    new ArrayEntry<Object>(":oops:", "icon_redface.gif"),
                    new ArrayEntry<Object>(":razz:", "icon_razz.gif"),
                    new ArrayEntry<Object>(":roll:", "icon_rolleyes.gif"),
                    new ArrayEntry<Object>(":wink:", "icon_wink.gif"),
                    new ArrayEntry<Object>(":cry:", "icon_cry.gif"),
                    new ArrayEntry<Object>(":eek:", "icon_surprised.gif"),
                    new ArrayEntry<Object>(":lol:", "icon_lol.gif"),
                    new ArrayEntry<Object>(":mad:", "icon_mad.gif"),
                    new ArrayEntry<Object>(":sad:", "icon_sad.gif"),
                    new ArrayEntry<Object>("8-)", "icon_cool.gif"),
                    new ArrayEntry<Object>("8-O", "icon_eek.gif"),
                    new ArrayEntry<Object>(":-(", "icon_sad.gif"),
                    new ArrayEntry<Object>(":-)", "icon_smile.gif"),
                    new ArrayEntry<Object>(":-?", "icon_confused.gif"),
                    new ArrayEntry<Object>(":-D", "icon_biggrin.gif"),
                    new ArrayEntry<Object>(":-P", "icon_razz.gif"),
                    new ArrayEntry<Object>(":-o", "icon_surprised.gif"),
                    new ArrayEntry<Object>(":-x", "icon_mad.gif"),
                    new ArrayEntry<Object>(":-|", "icon_neutral.gif"),
                    new ArrayEntry<Object>(";-)", "icon_wink.gif"),
                    new ArrayEntry<Object>("8)", "icon_cool.gif"),
                    new ArrayEntry<Object>("8O", "icon_eek.gif"),
                    new ArrayEntry<Object>(":(", "icon_sad.gif"),
                    new ArrayEntry<Object>(":)", "icon_smile.gif"),
                    new ArrayEntry<Object>(":?", "icon_confused.gif"),
                    new ArrayEntry<Object>(":D", "icon_biggrin.gif"),
                    new ArrayEntry<Object>(":P", "icon_razz.gif"),
                    new ArrayEntry<Object>(":o", "icon_surprised.gif"),
                    new ArrayEntry<Object>(":x", "icon_mad.gif"),
                    new ArrayEntry<Object>(":|", "icon_neutral.gif"),
                    new ArrayEntry<Object>(";)", "icon_wink.gif"),
                    new ArrayEntry<Object>(":!:", "icon_exclaim.gif"),
                    new ArrayEntry<Object>(":?:", "icon_question.gif"));
        }

        siteurl = get_option("siteurl");

        for (Map.Entry javaEntry476 : new Array<Object>(wpsmiliestrans).entrySet()) {
            smiley = strval(javaEntry476.getKey());
            img = javaEntry476.getValue();
            gVars.wp_smiliessearch.putValue("/(\\s|^)" + RegExPerl.preg_quote(smiley, "/") + "(\\s|$)/");
            smiley_masked = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.trim(smiley));
            gVars.wp_smiliesreplace.putValue(" <img src=\'" + strval(siteurl) + "/wp-includes/images/smilies/" + strval(img) + "\' alt=\'" + strval(smiley_masked) + "\' class=\'wp-smiley\' /> ");
        }
    }

    public Array<Object> wp_parse_args(Object args) {
        return wp_parse_args(args, "");
    }

    public Array<Object> wp_parse_args(Object args, /* Do not change type */
        Object defaults) {
        Array<Object> r = new Array<Object>();

        if (is_object(args)) {
            r = ClassHandling.get_object_vars(args);
        } else if (is_array(args)) {
            r = (Array<Object>) args;
        } else {
            getIncluded(FormattingPage.class, gVars, gConsts).wp_parse_str(strval(args), r);
        }

        if (is_array(defaults)) {
            return Array.array_merge((Array<Object>) defaults, r);
        }

        return r;
    }

    public void wp_maybe_load_widgets() {
        if (true) /*Modified by Numiton*/ {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, WidgetsPage.class);
            getIncluded(PluginPage.class, gVars, gConsts).add_action("_admin_menu", Callback.createCallbackArray(this, "wp_widgets_add_menu"), 10, 1);
        }
    }

    public void wp_widgets_add_menu() {
        gVars.submenu.getArrayValue("themes.php").putValue(
            7,
            new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Widgets", "default")), new ArrayEntry<Object>("switch_themes"),
                new ArrayEntry<Object>("widgets.php")));
        Array.ksort(gVars.submenu.getArrayValue("themes.php"), Array.SORT_NUMERIC);
    }

 // For PHP 5.2, make sure all output buffers are flushed
 // before our singletons our destroyed.
    public void wp_ob_end_flush_all() {
        while (OutputControl.ob_end_flush(gVars.webEnv)) {
        }
    }

    /*
     * require_wp_db() - require_once the correct database class file.
     *
     * This function is used to load the database class file either at runtime or by wp-admin/setup-config.php
     * We must globalise $wpdb to ensure that it is defined globally by the inline code in wp-db.php
     *
     * @global $wpdb
     */
    public void require_wp_db() {
        // Commented by Numiton

        //		if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-content/db.php")) {

        //			/* unresolved dynamic construct: 362697 */;

        //		}

        //		else

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, Wp_dbPage.class);
    }

    public void dead_db() {
        // Commented by Numiton

    	// Load custom DB error template, if present.
        //		if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + "wp-content/db-error.php")) {
        //			/* unresolved dynamic construct: 362731 */;
        //			System.exit();

        //		}
    	
    	// If installing or in the admin, provide the verbose message.
        if (gConsts.isWP_INSTALLINGDefined() || gConsts.isWP_ADMINDefined()) {
            wp_die(gVars.wpdb.error, "");
        }

    	// Otherwise, be terse.
        status_header(strval(500));
        nocache_headers();
        Network.header(gVars.webEnv, "Content-Type: text/html; charset=utf-8");
        echo(gVars.webEnv,
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ");

        if (true) /*Modified by Numiton*/ {
            getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");
        }

        echo(
                gVars.webEnv,
                ">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n\t<title>Database Error</title>\n\n</head>\n<body>\n\t<h1>Error establishing a database connection</h1>\n</body>\n</html>\n");
        System.exit();
    }

    /**
     * Converts input to an absolute integer
     * @param mixed $maybeint data you wish to have convered to an absolute
     * integer
     * @return int an absolute integer
     */
    public int absint(Object maybeint) {
        return Math.abs(intval(maybeint));
    }

    /**
     * Determines if the blog can be accessed over SSL
     * @return bool whether of not SSL access is available
     */
    public boolean url_is_accessable_via_ssl(String url) {
        String ssl;
        int ch = 0;
        String status = null;

        if (true) /*Modified by Numiton*/ {
            ssl = QRegExPerl.preg_replace("/^http:\\/\\//", "https://", url);
            ch = Curl.curl_init(gVars.webEnv);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_URL, ssl);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_FAILONERROR, true);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_RETURNTRANSFER, true);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_SSL_VERIFYPEER, false);
            Curl.curl_exec(gVars.webEnv, ch);
            status = Curl.curl_getinfo(gVars.webEnv, ch, Curl.CURLINFO_HTTP_CODE);
            Curl.curl_close(gVars.webEnv, ch);

            if (equal(status, 200) || equal(status, 401)) {
                return true;
            }
        }

        return false;
    }

    public String atom_service_url_filter(String url) {
        if (url_is_accessable_via_ssl(url)) {
            return QRegExPerl.preg_replace("/^http:\\/\\//", "https://", url);
        } else {
            return url;
        }
    }

    /**
     * _deprecated_function() - Marks a function as deprecated and informs when it has been used.
     *
     * There is a hook deprecated_function_run that will be called that can be used to get the backtrace
     * up to what file and function called the deprecated function.
     *
     * The current behavior is to trigger an user error if WP_DEBUG is defined and is true.
     *
     * This function is to be used in every function in depreceated.php
     *
     * @package WordPress
     * @package Debug
     * @since 2.5
     * @access private
     *
     * @uses do_action() Calls 'deprecated_function_run' and passes the function name and what to use instead.
     * @uses apply_filters() Calls 'deprecated_function_trigger_error' and expects boolean value of true to do trigger or false to not trigger error.
     *
     * @param string $function The function that was called
     * @param string $version The version of WordPress that depreceated the function
     * @param string $replacement Optional. The function that should have been called
     */
    public void _deprecated_function(String function, String version, Object replacement) {
        Object s = null;
        getIncluded(PluginPage.class, gVars, gConsts).do_action("deprecated_function_run", function, replacement);

    	// Allow plugin to filter the output error trigger
        if (gConsts.isWP_DEBUGDefined() && strictEqual(true, gConsts.getWP_DEBUG()) &&
                booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("deprecated_function_trigger_error", true))) {
            if (!is_null(replacement)) {
                ErrorHandling.trigger_error(gVars.webEnv,
                    strval(QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts)
                                .__("%1" + strval(s) + " is <strong>deprecated</strong> since version %2" + strval(s) + "! Use %3" + strval(s) + " instead.", "default"),
                            function,
                            version,
                            replacement)));
            } else {
                ErrorHandling.trigger_error(gVars.webEnv,
                    strval(QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts)
                                .__("%1" + strval(s) + " is <strong>deprecated</strong> since version %2" + strval(s) + " with no alternative available.", "default"),
                            function,
                            version)));
            }
        }
    }

    /**
     * _deprecated_file() - Marks a file as deprecated and informs when it has been used.
     *
     * There is a hook deprecated_file_included that will be called that can be used to get the backtrace
     * up to what file and function included the deprecated file.
     *
     * The current behavior is to trigger an user error if WP_DEBUG is defined and is true.
     *
     * This function is to be used in every file that is depreceated
     *
     * @package WordPress
     * @package Debug
     * @since 2.5
     * @access private
     *
     * @uses do_action() Calls 'deprecated_file_included' and passes the file name and what to use instead.
     * @uses apply_filters() Calls 'deprecated_file_trigger_error' and expects boolean value of true to do trigger or false to not trigger error.
     *
     * @param string $file The file that was included
     * @param string $version The version of WordPress that depreceated the function
     * @param string $replacement Optional. The function that should have been called
     */
    public void _deprecated_file(String file, String version, String replacement) {
        Object s = null;
        getIncluded(PluginPage.class, gVars, gConsts).do_action("deprecated_file_included", file, replacement);

    	// Allow plugin to filter the output error trigger
        if (gConsts.isWP_DEBUGDefined() && strictEqual(true, gConsts.getWP_DEBUG()) && booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("deprecated_file_trigger_error", true))) {
            if (!is_null(replacement)) {
                ErrorHandling.trigger_error(gVars.webEnv,
                    strval(QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts)
                                .__("%1" + strval(s) + " is <strong>deprecated</strong> since version %2" + strval(s) + "! Use %3" + strval(s) + " instead.", "default"),
                            file,
                            version,
                            replacement)));
            } else {
                ErrorHandling.trigger_error(gVars.webEnv,
                    strval(QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts)
                                .__("%1" + strval(s) + " is <strong>deprecated</strong> since version %2" + strval(s) + " with no alternative available.", "default"),
                            file,
                            version)));
            }
        }
    }

    /**
     * is_lighttpd_before_150() - Is the server running earlier than 1.5.0
     * version of lighttpd
     * @return bool Whether the server is running lighttpd < 1.5.0
     */
    public boolean is_lighttpd_before_150() {
        Array<String> server_parts = new Array<String>();
        server_parts = Strings.explode("/", isset(gVars.webEnv.getServerSoftware())
                ? gVars.webEnv.getServerSoftware()
                : "");
        server_parts.putValue(1, isset(server_parts.getValue(1))
            ? server_parts.getValue(1)
            : "");

        return equal("lighttpd", server_parts.getValue(0)) && equal(-1, Options.version_compare(server_parts.getValue(1), "1.5.0"));
    }

    /**
     * apache_mod_loaded() - Does the specified module exist in the apache
     * config?
     * @param string $mod e.g. mod_rewrite
     * @param bool $default The default return value if the module is not found
     * @return bool
     */
    public boolean apache_mod_loaded(String mod, boolean _default) {
        Array<Object> mods = new Array<Object>();
        String phpinfo = null;

        if (!gVars.is_apache) {
            return false;
        }

        if (false) /*Modified by Numiton*/ {
            mods = Unsupported.apache_get_modules();

            if (Array.in_array(mod, mods)) {
                return true;
            }
        } else if (false) /*Modified by Numiton*/ {
            OutputControl.ob_start(gVars.webEnv);
            Unsupported.phpinfo(strval(8));
            phpinfo = OutputControl.ob_get_clean(gVars.webEnv);

            if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(phpinfo, mod))) {
                return true;
            }
        }

        return _default;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
