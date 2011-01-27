/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: _WP_Script.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.generic.*;


public class _WP_Script implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(_WP_Script.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Object handle;
    public String src;
    public Object deps; /* Do not change type */
    public String ver;
    public String l10n_object = "";
    public Array<Object> l10n;

    public _WP_Script(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object... vargs) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    _WP_Script.this.handle = srcArray.getValue(0);
                    _WP_Script.this.src = strval(srcArray.getValue(1));
                    _WP_Script.this.deps = srcArray.getValue(2);
                    _WP_Script.this.ver = strval(srcArray.getValue(3));

                    return srcArray;
                }
            }.doAssign(FunctionHandling.func_get_args(vargs)); // Modified by Numiton

        if (!is_array(this.deps)) {
            this.deps = new Array<Object>();
        }

        if (!booleanval(this.ver)) {
            this.ver = "";
        }
    }

    public boolean localize(String object_name, Object l10n /* Do not change type */) {
        if (!booleanval(object_name) || !is_array(l10n)) {
            return false;
        }

        this.l10n_object = object_name;
        this.l10n = (Array<Object>) l10n;

        return true;
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
