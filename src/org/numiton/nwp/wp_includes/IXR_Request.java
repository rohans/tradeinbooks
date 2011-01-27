/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_Request.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.VarHandling.strval;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.string.Strings;


public class IXR_Request implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_Request.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public int method;
    public Array<Object> args = new Array<Object>();
    public String xml;

    public IXR_Request(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, int method, Array<Object> args) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        IXR_Value v = null;
        Object arg = null;
        this.method = method;
        this.args = args;
        this.xml = "<?xml version=\"1.0\"?>\n<methodCall>\n<methodName>" + strval(this.method) + "</methodName>\n<params>\n\n";

        for (Map.Entry javaEntry407 : this.args.entrySet()) {
            arg = javaEntry407.getValue();
            this.xml = this.xml + "<param><value>";
            v = new IXR_Value(gVars, gConsts, arg);
            this.xml = this.xml + v.getXml();
            this.xml = this.xml + "</value></param>\n";
        }

        this.xml = this.xml + "</params></methodCall>";
    }

    public int getLength() {
        return Strings.strlen(this.xml);
    }

    public String getXml() {
        return this.xml;
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
