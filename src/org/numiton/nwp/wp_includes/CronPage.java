/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CronPage.java,v 1.5 2008/10/14 14:23:04 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.Ref;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class CronPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CronPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/cron.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/cron";
    }

    public void wp_schedule_single_event(String timestamp, String hook, Array<Object> args) {
        Array<Object> crons = new Array<Object>();
        String key = null;
        crons = _get_cron_array();
        key = Strings.md5(serialize(args));
        crons.getArrayValue(timestamp).getArrayValue(hook).putValue(key, new Array<Object>(new ArrayEntry<Object>("schedule", false), new ArrayEntry<Object>("args", args)));
        Array.uksort(crons, new Callback("strnatcasecmp", CallbackUtils.class));
        _set_cron_array(crons);
    }

    public boolean wp_schedule_event(int timestamp, Object recurrence, Object hook, Object args) {
        Array<Object> crons = new Array<Object>();
        Array<Object> schedules = new Array<Object>();
        String key = null;
        crons = _get_cron_array();
        schedules = wp_get_schedules();
        key = Strings.md5(serialize(args));

        if (!isset(schedules.getValue(recurrence))) {
            return false;
        }

        crons.getArrayValue(timestamp).getArrayValue(hook).putValue(key,
            new Array<Object>(new ArrayEntry<Object>("schedule", recurrence),
                new ArrayEntry<Object>("args", args),
                new ArrayEntry<Object>("interval", schedules.getArrayValue(recurrence).getValue("interval"))));
        Array.uksort(crons, new Callback("strnatcasecmp", CallbackUtils.class));
        _set_cron_array(crons);

        return false;
    }

    public boolean wp_reschedule_event(int timestamp, Object recurrence, Object hook, Object args) {
        Array<Object> crons = new Array<Object>();
        Array<Object> schedules = new Array<Object>();
        String key = null;
        int interval = 0;
        crons = _get_cron_array();
        schedules = wp_get_schedules();
        key = Strings.md5(serialize(args));
        interval = 0;

        // First we try to get it from the schedule
        if (equal(0, interval)) {
            interval = intval(schedules.getArrayValue(recurrence).getValue("interval"));
        }

        // Now we try to get it from the saved interval in case the schedule disappears
        if (equal(0, interval)) {
            interval = intval(crons.getArrayValue(timestamp).getArrayValue(hook).getArrayValue(key).getValue("interval"));
        }

        // Now we assume something is wrong and fail to schedule
        if (equal(0, interval)) {
            return false;
        }

        while (timestamp < (DateTime.time() + 1))
            timestamp = timestamp + interval;

        wp_schedule_event(timestamp, recurrence, hook, args);

        return false;
    }

    public void wp_unschedule_event(Object timestamp, String hook, Object args) {
        Array<Object> crons = new Array<Object>();
        String key = null;
        crons = _get_cron_array();
        key = Strings.md5(serialize(args));
        crons.getArrayValue(timestamp).getArrayValue(hook).arrayUnset(key);

        if (empty(crons.getArrayValue(timestamp).getValue(hook))) {
            crons.getArrayValue(timestamp).arrayUnset(hook);
        }

        if (empty(crons.getValue(timestamp))) {
            crons.arrayUnset(timestamp);
        }

        _set_cron_array(crons);
    }

    public void wp_clear_scheduled_hook(String hook, Object... vargs) {
        Array<Object> args = new Array<Object>();
        Object timestamp = null;

        //Modified by Numiton
        args = FunctionHandling.func_get_args(vargs);

        while (booleanval(timestamp = wp_next_scheduled(hook, args)))
            wp_unschedule_event(timestamp, hook, args);
    }

    public Object wp_next_scheduled(String hook, Array<Object> args) {
        Array<Object> crons = new Array<Object>();
        String key = null;
        Array<Object> cron = new Array<Object>();
        Object timestamp = null;
        crons = _get_cron_array();
        key = Strings.md5(serialize(args));

        if (empty(crons)) {
            return false;
        }

        for (Map.Entry javaEntry439 : crons.entrySet()) {
            timestamp = javaEntry439.getKey();
            cron = (Array<Object>) javaEntry439.getValue();

            if (isset(cron.getArrayValue(hook).getValue(key))) {
                return timestamp;
            }
        }

        return false;
    }

    public boolean spawn_cron() {
        Array<Object> crons;

        /* Array or null */
        Array<Object> keys = new Array<Object>();
        String cron_url = null;
        Array<String> parts;
        int port = 0;
        int argyle = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        crons = _get_cron_array();

        if (!is_array(crons)) {
            return false;
        }

        keys = Array.array_keys(crons);

        if (intval(Array.array_shift(keys)) > DateTime.time()) {
            return false;
        }

        cron_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-cron.php";
        parts = URL.parse_url(cron_url);

        if (equal(parts.getValue("scheme"), "https")) {
        	// support for SSL was added in 4.3.0
            if (booleanval(Options.version_compare(Options.phpversion(), "4.3.0", ">=")) && false)/*Modified by Numiton*/
             {
                port = (isset(parts.getValue("port"))
                    ? intval(parts.getValue("port"))
                    : 443);
                argyle = FileSystemOrSocket.fsockopen(gVars.webEnv, "ssl://" + parts.getValue("host"), port, errno, errstr, 0.01);
            } else {
                return false;
            }
        } else {
            port = (isset(parts.getValue("port"))
                ? intval(parts.getValue("port"))
                : 80);
            argyle = FileSystemOrSocket.fsockopen(gVars.webEnv, parts.getValue("host"), port, errno, errstr, 0.01);
        }

        if (booleanval(argyle)) {
            FileSystemOrSocket.fputs(
                gVars.webEnv,
                argyle,
                "GET " + parts.getValue("path") + "?check=" + getIncluded(PluggablePage.class, gVars, gConsts).wp_hash("187425") + " HTTP/1.0\r\n" + "Host: " + gVars.webEnv.getHttpHost() +
                "\r\n\r\n");
        }

        return false;
    }

    public void wp_cron() {
        Array<Object> crons;

        /* Array or null */
        Array<Object> keys = new Array<Object>();
        Array<Object> schedules = new Array<Object>();
        Object timestamp = null;
        Object hook = null;
        Array<Object> cronhooks = null;
        Object args = null;

        // Prevent infinite loops caused by lack of wp-cron.php
        if (!strictEqual(Strings.strpos(gVars.webEnv.getRequestURI(), "/wp-cron.php"), BOOLEAN_FALSE)) {
            return;
        }

        crons = _get_cron_array();

        if (!is_array(crons)) {
            return;
        }

        keys = Array.array_keys(crons);

        if (isset(keys.getValue(0)) && (intval(keys.getValue(0)) > DateTime.time())) {
            return;
        }

        schedules = wp_get_schedules();
outer: 
        for (Map.Entry javaEntry440 : crons.entrySet()) {
            timestamp = javaEntry440.getKey();
            cronhooks = (Array<Object>) javaEntry440.getValue();

            if (intval(timestamp) > DateTime.time()) {
                break;
            }

            for (Map.Entry javaEntry441 : cronhooks.entrySet()) {
                hook = javaEntry441.getKey();
                args = javaEntry441.getValue();

                if (isset(schedules.getArrayValue(hook).getValue("callback")) && !booleanval(FunctionHandling.call_user_func(new Callback(schedules.getArrayValue(hook).getArrayValue("callback"))))) {
                    continue;
                }

                spawn_cron();

                break outer;
            }
        }
    }

    public Array<Object> wp_get_schedules() {
        Array<Object> schedules = new Array<Object>();
        schedules = new Array<Object>(
                new ArrayEntry<Object>(
                    "hourly",
                    new Array<Object>(new ArrayEntry<Object>("interval", 3600), new ArrayEntry<Object>("display", getIncluded(L10nPage.class, gVars, gConsts).__("Once Hourly", "default")))),
                new ArrayEntry<Object>(
                    "daily",
                    new Array<Object>(new ArrayEntry<Object>("interval", 86400), new ArrayEntry<Object>("display", getIncluded(L10nPage.class, gVars, gConsts).__("Once Daily", "default")))));

        return Array.array_merge((Array) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("cron_schedules", new Array<Object>()), schedules);
    }

    public Object wp_get_schedule(Object hook, Object args) {
        Array<Object> crons = new Array<Object>();
        String key = null;
        Array<Object> cron = new Array<Object>();
        Object timestamp = null;
        crons = _get_cron_array();
        key = Strings.md5(serialize(args));

        if (empty(crons)) {
            return false;
        }

        for (Map.Entry javaEntry442 : crons.entrySet()) {
            timestamp = javaEntry442.getKey();
            cron = (Array<Object>) javaEntry442.getValue();

            if (isset(cron.getArrayValue(hook).getValue(key))) {
                return cron.getArrayValue(hook).getArrayValue(key).getValue("schedule");
            }
        }

        return false;
    }

 //
 // Private functions
 //
    public Array<Object> _get_cron_array() {
        Object cron;

        /* Do not change type */
        cron = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("cron");

        if (!is_array(cron)) {
            return new Array<Object>();
        }

        if (!isset(((Array) cron).getValue("version"))) {
            cron = _upgrade_cron_array((Array) cron);
        }

        ((Array) cron).arrayUnset("version");

        return (Array) cron;
    }

    public void _set_cron_array(Array<Object> cron) {
        cron.putValue("version", 2);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("cron", cron);
    }

    public Array<Object> _upgrade_cron_array(Array<Object> cron) {
        Array<Object> new_cron = new Array<Object>();
        String key = null;
        Array<Object> args = new Array<Object>();
        Object timestamp = null;
        Object hook = null;
        Array<Object> hooks = null;

        if (isset(cron.getValue("version")) && equal(2, cron.getValue("version"))) {
            return cron;
        }

        new_cron = new Array<Object>();

        for (Map.Entry javaEntry443 : cron.entrySet()) {
            timestamp = javaEntry443.getKey();
            hooks = (Array<Object>) javaEntry443.getValue();

            for (Map.Entry javaEntry444 : hooks.entrySet()) {
                hook = javaEntry444.getKey();
                args = (Array<Object>) javaEntry444.getValue();
                key = Strings.md5(serialize(args.getValue("args")));
                new_cron.getArrayValue(timestamp).getArrayValue(hook).putValue(key, args);
            }
        }

        new_cron.putValue("version", 2);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("cron", new_cron);

        return new_cron;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
