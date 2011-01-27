/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: RpcPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.*;
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils.Moxiecode_JSON;
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.includes.GeneralPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.DynamicConstructEvaluator;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class RpcPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(RpcPage.class.getName());
    public String raw;
    public Array<Object> _GLOBALS = new Array<Object>();
    public int fp;
    public Moxiecode_JSON json;
    public Object input;
    public SpellChecker spellchecker;

    @Override
    @RequestMapping("/wp-includes/js/tinymce/plugins/spellchecker/rpc.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/plugins/spellchecker/rpc";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_plugins_spellchecker_rpc_block1");
        gVars.webEnv = webEnv;

        /**
         * $Id: RpcPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
         *
         * @author Moxiecode
         * @copyright Copyright © 2004-2007, Moxiecode Systems AB, All rights reserved.
         */
        requireOnce(gVars, gConsts, GeneralPage.class);

        // Set RPC response headers
        Network.header(gVars.webEnv, "Content-Type: text/plain");
        Network.header(gVars.webEnv, "Content-Encoding: UTF-8");
        Network.header(gVars.webEnv, "Expires: Mon, 26 Jul 1997 05:00:00 GMT");
        Network.header(gVars.webEnv, "Last-Modified: " + DateTime.gmdate("D, d M Y H:i:s") + " GMT");
        Network.header(gVars.webEnv, "Cache-Control: no-store, no-cache, must-revalidate");
        Network.header(gVars.webEnv, "Cache-Control: post-check=0, pre-check=0", false);
        Network.header(gVars.webEnv, "Pragma: no-cache");
        raw = "";

        // Try param
        if (isset(gVars.webEnv._POST.getValue("json_data"))) {
            raw = strval(getIncluded(GeneralPage.class, gVars, gConsts).getRequestParam("json_data", false, false));
        }

        // Try globals array
        if (!booleanval(raw) && isset(_GLOBALS) && isset(_GLOBALS.getValue("HTTP_RAW_POST_DATA"))) {
            raw = strval(_GLOBALS.getValue("HTTP_RAW_POST_DATA"));
        }

        // Try globals variable
        if (!booleanval(raw) && isset(gVars.webEnv.HTTP_RAW_POST_DATA)) {
            raw = gVars.webEnv.HTTP_RAW_POST_DATA;
        }

        // Try stream
        if (!booleanval(raw)) {
            if (!true) {
                fp = FileSystemOrSocket.fopen(gVars.webEnv, "php://input", "r");

                if (booleanval(fp)) {
                    raw = "";

                    while (!FileSystemOrSocket.feof(gVars.webEnv, fp))
                        raw = FileSystemOrSocket.fread(gVars.webEnv, fp, 1024);

                    FileSystemOrSocket.fclose(gVars.webEnv, fp);
                }
            } else {
                raw = "" + FileSystemOrSocket.file_get_contents(gVars.webEnv, "php://input");
            }
        }

        // No input data
        if (!booleanval(raw)) {
            System.exit("{\"result\":null,\"id\":null,\"error\":{\"errstr\":\"Could not get raw post data.\",\"errfile\":\"\",\"errline\":null,\"errcontext\":\"\",\"level\":\"FATAL\"}}");
        }

        // Get JSON data
        json = new Moxiecode_JSON(gVars, gConsts);
        input = json.decode(raw);

        // Execute RPC
        if (isset(gVars.config.getValue("general.engine"))) {
            spellchecker = new DynamicConstructEvaluator<SpellChecker>() {
                        public SpellChecker evaluate() {
                            if (equal("GoogleSpell", gVars.config.getValue("general.engine"))) {
                                return new GoogleSpell(gVars, gConsts, gVars.config);
                            }

                            if (equal("PSpell", gVars.config.getValue("general.engine"))) {
                                return new PSpell(gVars, gConsts, gVars.config);
                            }

                            if (equal("PSpellShell", gVars.config.getValue("general.engine"))) {
                                return new PSpellShell(gVars, gConsts, gVars.config);
                            }

                            LOG.warn("Unknown spellchecker: " + gVars.config.getValue("general.engine")); // Added by Numiton

                            return null;
                        }
                    }.evaluate();

            gVars.result = FunctionHandling.call_user_func_array(new Callback(strval(((Array) input).getValue("method")), spellchecker), ((Array) input).getArrayValue("params"));
        } else {
            System.exit(
                    "{\"result\":null,\"id\":null,\"error\":{\"errstr\":\"You must choose an spellchecker engine in the config.php file.\",\"errfile\":\"\",\"errline\":null,\"errcontext\":\"\",\"level\":\"FATAL\"}}");
        }

        // Request and response id should always be the same
        gVars.output = new Array<Object>(new ArrayEntry<Object>("id", ((Array) input).getValue("id")), new ArrayEntry<Object>("result", gVars.result), new ArrayEntry<Object>("error", null));

        // Return JSON encoded string
        echo(gVars.webEnv, json.encode(gVars.output));

        return DEFAULT_VAL;
    }
}
