/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_Server.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.string.Strings;


public class IXR_Server implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_Server.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Object data;
    public Array<Object> callbacks = new Array<Object>();
    public IXR_Message message;
    public Array<Object> capabilities = new Array<Object>();

    public IXR_Server(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Array<Object> callbacks) {
        this(javaGlobalVariables, javaGlobalConstants, callbacks, "");
    }

    public IXR_Server(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Array<Object> callbacks, String data) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.setCapabilities();

        if (booleanval(callbacks)) {
            this.callbacks = callbacks;
        }

        this.setCallbacks();
        this.serve(data);
    }

    // Added by Numiton
    public IXR_Server(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        this(javaGlobalVariables, javaGlobalConstants, null, null);
    }

    public void serve(String data) {
        Object result = null;
        IXR_Value r = null;
        String resultxml = null;
        String xml = null;

        if (!booleanval(data)) {
            if (!booleanval(gVars.webEnv.HTTP_RAW_POST_DATA)) {
                System.exit("XML-RPC server accepts POST requests only.");
            }

            data = gVars.webEnv.HTTP_RAW_POST_DATA;
        }

        this.message = new IXR_Message(gVars, gConsts, data);

        if (!this.message.parse()) {
            this.error(-32700, "parse error. not well formed");
        }

        if (!equal(this.message.messageType, "methodCall")) {
            this.error(-32600, "server error. invalid xml-rpc. not conforming to spec. Request must be a methodCall");
        }

        result = this.call(this.message.methodName, this.message.params);

        // Is the result an error?
        if (ClassHandling.is_a(result, "IXR_Error")) {
            this.error(result);
        }

        // Encode the result
        r = new IXR_Value(gVars, gConsts, result);
        resultxml = r.getXml();
        // Create the XML
        xml = "<methodResponse>\n  <params>\n    <param>\n      <value>\n        " + resultxml + "\n      </value>\n    </param>\n  </params>\n</methodResponse>\n\n";
        // Send it
        this.output(xml);
    }

    public Object call(String methodname, Object args) {
        Object method = null;

        /* Do not change type */
        Object result = null;

        if (!this.hasMethod(methodname)) {
            return new IXR_Error(gVars, gConsts, -32601, "server error. requested method " + methodname + " does not exist.");
        }

        method = this.callbacks.getValue(methodname);
        // Perform the callback and send the response
        if (equal(Array.count(args), 1)) {
            // If only one paramater just send that instead of the whole array
            args = ((Array) args).getValue(0);
        }

        // Are we dealing with a function or a method?
        if (equal(Strings.substr(strval(method), 0, 5), "this:")) {
            // It's a class method - check it exists
            method = Strings.substr(strval(method), 5);

            if (!ClassHandling.method_exists(this, strval(method))) {
                return new IXR_Error(gVars, gConsts, -32601, "server error. requested class method \"" + method + "\" does not exist.");
            }

            // Modified by Numiton
            // Call the method
            result = FunctionHandling.call_user_func(new Callback(strval(method), this), args);
        } else {
            // It's a function - does it exist?
            if (is_array(method)) {
                if (!ClassHandling.method_exists(strval(((Array) method).getValue(0)), strval(((Array) method).getValue(1)))) {
                    return new IXR_Error(gVars, gConsts, -32601, "server error. requested object method \"" + strval(((Array) method).getValue(1)) + "\" does not exist.");
                }
            } else if (!VarHandling.is_callable(new Callback(strval(method), this))) {
                return new IXR_Error(gVars, gConsts, -32601, "server error. requested function \"" + method + "\" does not exist.");
            }

            // Call the function
            result = FunctionHandling.call_user_func(new Callback(strval(method), this), args);
        }

        return result;
    }

    public void error(Object error) {
        error(error, "");
    }

    public void error(Object error, /* Do not change type */String message) {
        // Accepts either an error object or an error code and message
        if (booleanval(message) && !is_object(error)) {
            error = new IXR_Error(gVars, gConsts, intval(error), message);
        }

        this.output(((IXR_Error) error).getXml());
    }

    public void output(String xml) {
        int length = 0;
        xml = "<?xml version=\"1.0\"?>" + "\n" + xml;
        length = Strings.strlen(xml);
        Network.header(gVars.webEnv, "Connection: close");
        Network.header(gVars.webEnv, "Content-Length: " + strval(length));
        Network.header(gVars.webEnv, "Content-Type: text/xml");
        Network.header(gVars.webEnv, "Date: " + DateTime.date("r"));
        echo(gVars.webEnv, xml);
        System.exit();
    }

    public boolean hasMethod(String method) {
        return Array.in_array(method, Array.array_keys(this.callbacks));
    }

    public void setCapabilities() {
        // Initialises capabilities array
        this.capabilities = new Array<Object>(
                new ArrayEntry<Object>("xmlrpc", new Array<Object>(new ArrayEntry<Object>("specUrl", "http://www.xmlrpc.com/spec"), new ArrayEntry<Object>("specVersion", 1))),
                new ArrayEntry<Object>(
                    "faults_interop",
                    new Array<Object>(new ArrayEntry<Object>("specUrl", "http://xmlrpc-epi.sourceforge.net/specs/rfc.fault_codes.php"), new ArrayEntry<Object>("specVersion", 20010516))),
                new ArrayEntry<Object>("system.multicall",
                    new Array<Object>(new ArrayEntry<Object>("specUrl", "http://www.xmlrpc.com/discuss/msgReader$1208"), new ArrayEntry<Object>("specVersion", 1))));
    }

    public Array<Object> getCapabilities(Object args) {
        return this.capabilities;
    }

    public void setCallbacks() {
        this.callbacks.putValue("system.getCapabilities", "this:getCapabilities");
        this.callbacks.putValue("system.listMethods", "this:listMethods");
        this.callbacks.putValue("system.multicall", "this:multiCall");
    }

    public Array<Object> listMethods(Object args) {
        // Returns a list of methods - uses array_reverse to ensure user defined
        // methods are listed before server defined methods
        return Array.array_reverse(Array.array_keys(this.callbacks));
    }

    public Array<Object> multiCall(Array<Object> methodcalls) {
        Array<Object> _return = new Array<Object>();
        Object method = null;
        Array<Object> call = new Array<Object>();
        Object params = null;
        Object result = null;
        
        // See http://www.xmlrpc.com/discuss/msgReader$1208
        _return = new Array<Object>();

        for (Map.Entry javaEntry406 : methodcalls.entrySet()) {
            call = (Array<Object>) javaEntry406.getValue();
            method = call.getValue("methodName");
            params = call.getValue("params");

            if (equal(method, "system.multicall")) {
                result = new IXR_Error(gVars, gConsts, -32600, "Recursive calls to system.multicall are forbidden");
            } else {
                result = this.call(strval(method), params);
            }

            if (ClassHandling.is_a(result, "IXR_Error")) {
                _return.putValue(new Array<Object>(new ArrayEntry<Object>("faultCode", ((IXR_Error) result).code), new ArrayEntry<Object>("faultString", ((IXR_Error) result).message)));
            } else {
                _return.putValue(new Array<Object>(new ArrayEntry<Object>(result)));
            }
        }

        return _return;
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
