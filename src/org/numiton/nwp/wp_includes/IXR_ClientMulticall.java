/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_ClientMulticall.java,v 1.3 2008/10/03 18:45:29 numiton Exp $
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

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;


public class IXR_ClientMulticall extends IXR_Client implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_ClientMulticall.class.getName());

    public Array<Object> calls = new Array<Object>();

    public IXR_ClientMulticall(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String server, String path, int port) {
        super(javaGlobalVariables, javaGlobalConstants, server, path, port, 0);
        this.useragent = "The Incutio XML-RPC PHP Library (multicall client)";
    }

    public void addCall(Object... vargs) {
        Array<Object> args = new Array<Object>();
        Object methodName = null;
        Array<Object> struct = new Array<Object>();
        args = FunctionHandling.func_get_args(vargs);
        methodName = Array.array_shift(args);
        struct = new Array<Object>(new ArrayEntry<Object>("methodName", methodName), new ArrayEntry<Object>("params", args));
        this.calls.putValue(struct);
    }

    public boolean query() {
        // Prepare multicall, then call the parent::query() method
        return super.query("system.multicall", this.calls);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
