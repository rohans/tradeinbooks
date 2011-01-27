/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: GeneralPage.java,v 1.3 2008/10/14 13:15:51 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.includes;

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
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.ConfigPage;
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils.JSONPage;
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils.LoggerPage;
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils.Moxiecode_Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.FunctionHandling;
import com.numiton.SourceCodeInfo;
import com.numiton.array.Array;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class GeneralPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(GeneralPage.class.getName());
    public Object man;
    public Moxiecode_Logger mcLogger;

    @Override
    @RequestMapping("/wp-includes/js/tinymce/plugins/spellchecker/includes/general.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/plugins/spellchecker/includes/general";
    }

    public Object getRequestParam(String name, Object default_value, boolean sanitize) {
        Array<Object> newarray = new Array<Object>();
        Object value = null;

        if (!isset(gVars.webEnv._REQUEST.getValue(name))) {
            return default_value;
        }

        if (is_array(gVars.webEnv._REQUEST.getValue(name))) {
            newarray = new Array<Object>();

            for (Map.Entry javaEntry492 : (Set<Map.Entry>) gVars.webEnv._REQUEST.getArrayValue(name).entrySet()) {
                name = strval(javaEntry492.getKey());
                value = javaEntry492.getValue();
                newarray.putValue(formatParam(name, sanitize), formatParam(value, sanitize));
            }

            return newarray;
        }

        return formatParam(gVars.webEnv._REQUEST.getValue(name), sanitize);
    }

    // Added by Numiton - was missing
    protected Object formatParam(Object value, boolean sanitize) {
        return value;
    }

    public Moxiecode_Logger getLogger() {
        // Modified by Numiton
        //		if (isset(man)) {
        //			mcLogger = man.getLogger();
        //		}
        if (!booleanval(mcLogger)) {
            mcLogger = new Moxiecode_Logger(gVars, gConsts);
            
    		// Set logger options
            mcLogger.setPath(FileSystemOrSocket.dirname(SourceCodeInfo.getCurrentFile(gVars.webEnv)) + "/../logs");
            mcLogger.setMaxSize("100kb");
            mcLogger.setMaxFiles("10");
            mcLogger.setFormat("{time} - {message}");
        }

        return mcLogger;
    }

    public void debug(String msg, Object... vargs) {
        Array<String> args = new Array<String>();
        Moxiecode_Logger log = null;

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        log = getLogger();
        log.debug(Strings.implode(", ", args));
    }

    public void info(String msg, Object... vargs) {
        Array<String> args = new Array<String>();
        Moxiecode_Logger log = null;

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        log = getLogger();
        log.info(Strings.implode(", ", args));
    }

    public void error(String msg, Object... vargs) {
        Array<String> args = new Array<String>();
        Moxiecode_Logger log = null;

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        log = getLogger();
        log.error(Strings.implode(", ", args));
    }

    public void warn(String msg, Object... vargs) {
        Array<String> args = new Array<String>();
        Moxiecode_Logger log = null;

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        log = getLogger();
        log.warn(Strings.implode(", ", args));
    }

    public void fatal(String msg, Object... vargs) {
        Array<String> args = new Array<String>();
        Moxiecode_Logger log = null;

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        log = getLogger();
        log.fatal(Strings.implode(", ", args));
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_plugins_spellchecker_includes_general_block1");
        gVars.webEnv = webEnv;
        
        /**
         * general.php
         *
         * @package MCManager.includes
         * @author Moxiecode
         * @copyright Copyright © 2007, Moxiecode Systems AB, All rights reserved.
         */

        ErrorHandling.error_reporting(gVars.webEnv, ErrorHandling.E_ALL ^ ErrorHandling.E_NOTICE);
        gVars.config = new Array<Object>();
        /* Condensed dynamic construct: 386507 */ requireOnce(gVars, gConsts, LoggerPage.class);
        /* Condensed dynamic construct: 386518 */ requireOnce(gVars, gConsts, JSONPage.class);
        /* Condensed dynamic construct: 386529 */ requireOnce(gVars, gConsts, ConfigPage.class);

        // Commented by Numiton: Useless include
        //		if (isset(gVars.config.getValue("general.engine"))) {
        //			/* unresolved dynamic construct: 386572 */;
        //		}
        return DEFAULT_VAL;
    }
}
