/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Error.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;


public class WP_Error implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Error.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> errors = new Array<Object>();
    public Array<Object> error_data = new Array<Object>();
    public Object name;
    public Object parent;
    public Object term_id;

    public WP_Error(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        this(javaGlobalVariables, javaGlobalConstants, "", "", "");
    }

    public WP_Error(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String code, String message) {
        this(javaGlobalVariables, javaGlobalConstants, code, message, "");
    }

    public WP_Error(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String code, String message, Object data) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        if (empty(code)) {
            return;
        }

        this.errors.getArrayValue(code).putValue(message);

        if (!empty(data)) {
            this.error_data.putValue(code, data);
        }
    }

    public Array<Object> get_error_codes() {
        if (empty(this.errors)) {
            return new Array<Object>();
        }

        return Array.array_keys(this.errors);
    }

    public String get_error_code() {
        Array<Object> codes = new Array<Object>();
        codes = this.get_error_codes();

        if (empty(codes)) {
            return "";
        }

        return strval(codes.getValue(0));
    }

    public Array<Object> get_error_messages() {
        return get_error_messages("");
    }

    public Array<Object> get_error_messages(String code) {
        Array<Object> all_messages = new Array<Object>();
        Array<Object> messages = null;

		// Return all messages if no code specified.
        if (empty(code)) {
            all_messages = new Array<Object>();

            for (Map.Entry javaEntry415 : this.errors.entrySet()) {
                code = strval(javaEntry415.getKey());
                messages = (Array<Object>) javaEntry415.getValue();
                all_messages = Array.array_merge(all_messages, messages);
            }

            return all_messages;
        }

        if (isset(this.errors.getValue(code))) {
            return new Array<Object>(this.errors.getValue(code));
        } else {
            return new Array<Object>();
        }
    }

    public String get_error_message() {
        return get_error_message(null);
    }

    public String get_error_message(String code) {
        Array<Object> messages = new Array<Object>();

        if (empty(code)) {
            code = this.get_error_code();
        }

        messages = this.get_error_messages(code);

        if (empty(messages)) {
            return "";
        }

        return strval(messages.getValue(0));
    }

    public Object get_error_data() {
        return get_error_data("");
    }

    public Object get_error_data(String code) {
        if (empty(code)) {
            code = this.get_error_code();
        }

        if (isset(this.error_data.getValue(code))) {
            return this.error_data.getValue(code);
        }

        return null;
    }

    public void add(String code, String message) {
        add(code, message, "");
    }

    public void add(String code, String message, Object data) {
        this.errors.getArrayValue(code).putValue(message);

        if (!empty(data)) {
            this.error_data.putValue(code, data);
        }
    }

    public void add_data(Object data) {
        add_data(data, "");
    }

    public void add_data(Object data, String code) {
        if (empty(code)) {
            code = this.get_error_code();
        }

        this.error_data.putValue(code, data);
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
