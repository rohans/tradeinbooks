/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Ajax_Response.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.ClassHandling;
import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;


public class WP_Ajax_Response implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Ajax_Response.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> responses = new Array<Object>();

    public WP_Ajax_Response(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        this(javaGlobalVariables, javaGlobalConstants, null);
    }

    public WP_Ajax_Response(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object args) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        if (!empty(args)) {
            this.add(args);
        }
    }

	// a WP_Error object can be passed in 'id' or 'data'
    public String add(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        String position = null;
        Object id = null;
        Object data = null;
        String response = null;
        String code = null;
        Object error_data;

        /* Do not change type */
        String _class = null;
        Object k = null;
        Object v = null;
        String s = null;
        Object supplemental = null;

        /* Do not change type */
        Object action = null;
        String x = null;
        Object what = null;
        Object old_id = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("what", "object"),
                new ArrayEntry<Object>("action", false),
                new ArrayEntry<Object>("id", "0"),
                new ArrayEntry<Object>("old_id", false),
                new ArrayEntry<Object>("position", 1), // -1 = top, 1 = bottom, html ID = after, -html ID = before
                new ArrayEntry<Object>("data", ""),
                new ArrayEntry<Object>("supplemental", new Array<Object>()));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        position = strval(Array.extractVar(r, "position", position, Array.EXTR_SKIP));
        id = Array.extractVar(r, "id", id, Array.EXTR_SKIP);
        data = Array.extractVar(r, "data", data, Array.EXTR_SKIP);
        supplemental = Array.extractVar(r, "supplemental", supplemental, Array.EXTR_SKIP);
        action = Array.extractVar(r, "action", action, Array.EXTR_SKIP);
        what = Array.extractVar(r, "what", what, Array.EXTR_SKIP);
        old_id = Array.extractVar(r, "old_id", old_id, Array.EXTR_SKIP);
        position = QRegExPerl.preg_replace("/[^a-z0-9:_-]/i", "", position);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
            data = id;
            id = 0;
        }

        response = "";

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(data)) {
            for (Map.Entry javaEntry420 : ((WP_Error) data).get_error_codes().entrySet()) {
                code = strval(javaEntry420.getValue());
                response = response + "<wp_error code=\'" + code + "\'><![CDATA[" + ((WP_Error) data).get_error_message(code) + "]]></wp_error>";

                if (!booleanval((error_data = ((WP_Error) data).get_error_data(code)))) {
                    continue;
                }

                _class = "";

                if (is_object(error_data)) {
                    _class = " class=\"" + ClassHandling.get_class(error_data) + "\"";
                    error_data = ClassHandling.get_object_vars(error_data);
                }

                response = response + "<wp_error_data code=\'" + code + "\'" + _class + ">";

                if (is_scalar(error_data)) {
                    response = response + "<![CDATA[" + strval(error_data) + "]]>";
                } else if (is_array(error_data)) {
                    for (Map.Entry javaEntry421 : ((Array<?>) error_data).entrySet()) {
                        k = javaEntry421.getKey();
                        v = javaEntry421.getValue();
                        response = response + "<" + strval(k) + "><![CDATA[" + strval(v) + "]]></" + strval(k) + ">";
                    }
                }

                response = response + "</wp_error_data>";
            }
        } else {
            response = "<response_data><![CDATA[" + strval(data) + "]]></response_data>";
        }

        s = "";

        if (booleanval(supplemental)) {
            for (Map.Entry javaEntry422 : ((Array<?>) supplemental).entrySet()) {
                k = javaEntry422.getKey();
                v = javaEntry422.getValue();
                s = s + "<" + strval(k) + "><![CDATA[" + strval(v) + "]]></" + strval(k) + ">";
            }

            s = "<supplemental>" + s + "</supplemental>";
        }

        if (equal(false, action)) {
            action = gVars.webEnv._POST.getValue("action");
        }

        x = "";
        x = x + "<response action=\'" + strval(action) + "_" + strval(id) + "\'>"; // The action attribute in the xml output is formatted like a nonce action
        x = x + "<" + strval(what) + " id=\'" + strval(id) + "\' " + (equal(false, old_id)
            ? ""
            : ("old_id=\'" + strval(old_id) + "\' ")) + "position=\'" + position + "\'>";
        x = x + response;
        x = x + s;
        x = x + "</" + strval(what) + ">";
        x = x + "</response>";
        this.responses.putValue(x);

        return x;
    }

    public void send() {
        Object response = null;
        Network.header(gVars.webEnv, "Content-Type: text/xml");
        echo(gVars.webEnv, "<?xml version=\'1.0\' standalone=\'yes\'?><wp_ajax>");

        for (Map.Entry javaEntry423 : this.responses.entrySet()) {
            response = javaEntry423.getValue();
            echo(gVars.webEnv, response);
        }

        echo(gVars.webEnv, "</wp_ajax>");
        System.exit();
    }

    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}
