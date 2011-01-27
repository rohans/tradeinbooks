/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_cronPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Wp_cronPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_cronPage.class.getName());
    public Array<Object> crons; /* Initialized in code */
    public Object timestamp;
    public Object schedule;
    public Array<Object> new_args;
    public Object hook;
    public Array<Object> cronhooks;

    @Override
    @RequestMapping("/wp-cron.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_cron";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_cron_block1");
        gVars.webEnv = webEnv;

        Misc.ignore_user_abort(true);
        gConsts.setDOING_CRON(true);

        requireOnce(gVars, gConsts, Wp_configPage.class);

        if (!equal(gVars.webEnv._GET.getValue("check"), getIncluded(PluggablePage.class, gVars, gConsts).wp_hash("187425"))) {
            System.exit();
        }

        if (intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("doing_cron")) > DateTime.time()) {
            System.exit();
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("doing_cron", DateTime.time() + 30);
        crons = getIncluded(CronPage.class, gVars, gConsts)._get_cron_array();
        gVars.keys = Array.array_keys(crons);

        if (!is_array(crons) || (intval(gVars.keys.getValue(0)) > DateTime.time())) {
            return null;
        }

        for (Map.Entry javaEntry365 : crons.entrySet()) {
            timestamp = javaEntry365.getKey();
            cronhooks = (Array<Object>) javaEntry365.getValue();

            if (intval(timestamp) > DateTime.time()) {
                break;
            }

            for (Map.Entry javaEntry366 : cronhooks.entrySet()) {
                hook = javaEntry366.getKey();
                gVars.keys = (Array<Object>) javaEntry366.getValue();

                for (Map.Entry javaEntry367 : gVars.keys.entrySet()) {
                    gVars.key = strval(javaEntry367.getKey());
                    gVars.args = (Array<Object>) javaEntry367.getValue();
                    schedule = gVars.args.getValue("schedule");

                    if (!equal(schedule, false)) {
                        new_args = new Array<Object>(new ArrayEntry<Object>(timestamp), new ArrayEntry<Object>(schedule), new ArrayEntry<Object>(hook),
                                new ArrayEntry<Object>(gVars.args.getValue("args")));
                        FunctionHandling.call_user_func_array(new Callback("wp_reschedule_event", getIncluded(CronPage.class, gVars, gConsts)), new_args);
                    }

                    getIncluded(CronPage.class, gVars, gConsts).wp_unschedule_event(timestamp, strval(hook), gVars.args.getValue("args"));
                    getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array(strval(hook), gVars.args.getArrayValue("args"));
                }
            }
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("doing_cron", 0);

        return DEFAULT_VAL;
    }
}
