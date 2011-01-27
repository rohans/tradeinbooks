/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_IntrospectionServer.java,v 1.5 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.ClassHandling;
import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;


public class IXR_IntrospectionServer extends IXR_Server implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_IntrospectionServer.class.getName());
    public Array<Array<Object>> signatures = new Array<Array<Object>>();
    public Array<Object> help = new Array<Object>();

    public IXR_IntrospectionServer(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants);
        this.setCallbacks();
        this.setCapabilities();
        this.capabilities.putValue("introspection", new Array<Object>(new ArrayEntry<Object>("specUrl", "http://xmlrpc.usefulinc.com/doc/reserved.html"), new ArrayEntry<Object>("specVersion", 1)));
        this.addCallback(
            "system.methodSignature",
            "this:methodSignature",
            new Array<Object>(new ArrayEntry<Object>("array"), new ArrayEntry<Object>("string")),
            "Returns an array describing the return type and required parameters of a method");
        this.addCallback(
            "system.getCapabilities",
            "this:getCapabilities",
            new Array<Object>(new ArrayEntry<Object>("struct")),
            "Returns a struct describing the XML-RPC specifications supported by this server");
        this.addCallback("system.listMethods", "this:listMethods", new Array<Object>(new ArrayEntry<Object>("array")), "Returns an array of available methods on this server");
        this.addCallback(
            "system.methodHelp",
            "this:methodHelp",
            new Array<Object>(new ArrayEntry<Object>("string"), new ArrayEntry<Object>("string")),
            "Returns a documentation string for the specified method");
    }

    public void addCallback(String method, String callback, Array<Object> args, String help) {
        this.callbacks.putValue(method, callback);
        this.signatures.putValue(method, args);
        this.help.putValue(method, help);
    }

    public Object call(Object methodname, Object args)/* Do not change type */
     {
        Object method = null;
        Array<Object> signature;
        Object returnType = null;
        boolean ok = false;
        Array<Object> argsbackup = new Array<Object>();
        Object arg = null;

        /* Do not change type */
        Object type = null;
        int i = 0;
        int j = 0;

        // Make sure it's in an array
        if (booleanval(args) && !is_array(args)) {
            args = new Array<Object>(new ArrayEntry<Object>(args));
        }

        // Over-rides default call method, adds signature check
        if (!this.hasMethod(strval(methodname))) {
            return new IXR_Error(gVars, gConsts, -32601, "server error. requested method \"" + this.message.methodName + "\" not specified.");
        }

        method = this.callbacks.getValue(methodname);
        signature = Array.arrayCopy(this.signatures.getArrayValue(methodname));
        returnType = Array.array_shift(signature);

        // Check the number of arguments
        if (!equal(Array.count(args), Array.count(signature))) {
            return new IXR_Error(gVars, gConsts, -32602, "server error. wrong number of method parameters");
        }

        // Check the argument types
        ok = true;
        argsbackup = (Array<Object>) args;
        i = 0;
        j = Array.count(args);

        for (; i < j; i++) {
            arg = Array.array_shift((Array) args);
            type = Array.array_shift(signature);

            {
                int javaSwitchSelector46 = 0;

                if (equal(type, "int")) {
                    javaSwitchSelector46 = 1;
                }

                if (equal(type, "i4")) {
                    javaSwitchSelector46 = 2;
                }

                if (equal(type, "base64")) {
                    javaSwitchSelector46 = 3;
                }

                if (equal(type, "string")) {
                    javaSwitchSelector46 = 4;
                }

                if (equal(type, "boolean")) {
                    javaSwitchSelector46 = 5;
                }

                if (equal(type, "float")) {
                    javaSwitchSelector46 = 6;
                }

                if (equal(type, "double")) {
                    javaSwitchSelector46 = 7;
                }

                if (equal(type, "date")) {
                    javaSwitchSelector46 = 8;
                }

                if (equal(type, "dateTime.iso8601")) {
                    javaSwitchSelector46 = 9;
                }

                switch (javaSwitchSelector46) {
                case 1: {
                }

                case 2: {
                    if (is_array(arg) || !is_int(arg)) {
                        ok = false;
                    }

                    break;
                }

                case 3: {
                }

                case 4: {
                    if (!is_string(arg)) {
                        ok = false;
                    }

                    break;
                }

                case 5: {
                    if (!strictEqual(arg, false) && !strictEqual(arg, true)) {
                        ok = false;
                    }

                    break;
                }

                case 6: {
                }

                case 7: {
                    if (!is_float(arg)) {
                        ok = false;
                    }

                    break;
                }

                case 8: {
                }

                case 9: {
                    if (!ClassHandling.is_a(arg, "IXR_Date")) {
                        ok = false;
                    }

                    break;
                }
                }
            }

            if (!ok) {
                return new IXR_Error(gVars, gConsts, -32602, "server error. invalid method parameters");
            }
        }

        // It passed the test - run the "real" method call
        return super.call(strval(methodname), argsbackup);
    }

    public Object methodSignature(String method) {
        Array<Object> types = null;
        Array<Object> _return = new Array<Object>();
        Object type = null;

        if (!this.hasMethod(method)) {
            return new IXR_Error(gVars, gConsts, -32601, "server error. requested method \"" + method + "\" not specified.");
        }

        // We should be returning an array of types
        types = this.signatures.getArrayValue(method);
        _return = new Array<Object>();

        for (Map.Entry javaEntry408 : types.entrySet()) {
            type = javaEntry408.getValue();

            {
                int javaSwitchSelector47 = 0;

                if (equal(type, "string")) {
                    javaSwitchSelector47 = 1;
                }

                if (equal(type, "int")) {
                    javaSwitchSelector47 = 2;
                }

                if (equal(type, "i4")) {
                    javaSwitchSelector47 = 3;
                }

                if (equal(type, "double")) {
                    javaSwitchSelector47 = 4;
                }

                if (equal(type, "dateTime.iso8601")) {
                    javaSwitchSelector47 = 5;
                }

                if (equal(type, "boolean")) {
                    javaSwitchSelector47 = 6;
                }

                if (equal(type, "base64")) {
                    javaSwitchSelector47 = 7;
                }

                if (equal(type, "array")) {
                    javaSwitchSelector47 = 8;
                }

                if (equal(type, "struct")) {
                    javaSwitchSelector47 = 9;
                }

                switch (javaSwitchSelector47) {
                case 1: {
                    _return.putValue("string");

                    break;
                }

                case 2: {
                }

                case 3: {
                    _return.putValue(42);

                    break;
                }

                case 4: {
                    _return.putValue(3.1415);

                    break;
                }

                case 5: {
                    _return.putValue(new IXR_Date(gVars, gConsts, strval(DateTime.time())));

                    break;
                }

                case 6: {
                    _return.putValue(true);

                    break;
                }

                case 7: {
                    _return.putValue(new IXR_Base64(gVars, gConsts, "base64"));

                    break;
                }

                case 8: {
                    _return.putValue(new Array<Object>(new ArrayEntry<Object>("array")));

                    break;
                }

                case 9: {
                    _return.putValue(new Array<Object>(new ArrayEntry<Object>("struct", "struct")));

                    break;
                }
                }
            }
        }

        return _return;
    }

    public Object methodHelp(Object method) {
        return this.help.getValue(method);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
