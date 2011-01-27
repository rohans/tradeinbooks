/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Role.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;


public class WP_Role implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Role.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String name;
    public Array<Object> capabilities = new Array<Object>();

    public WP_Role(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String role, Array<Object> capabilities) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.name = role;
        this.capabilities = capabilities;
    }

    public void add_cap(String cap) {
        add_cap(cap, true);
    }

    public void add_cap(String cap, boolean grant) {
        if (!isset(gVars.wp_roles)) {
            gVars.wp_roles = new WP_Roles(gVars, gConsts);
        }

        this.capabilities.putValue(cap, grant);
        gVars.wp_roles.add_cap(this.name, cap, grant);
    }

    public void remove_cap(String cap) {
        if (!isset(gVars.wp_roles)) {
            gVars.wp_roles = new WP_Roles(gVars, gConsts);
        }

        this.capabilities.arrayUnset(cap);
        gVars.wp_roles.remove_cap(this.name, cap);
    }

    public boolean has_cap(String cap) {
        Array<Object> capabilities = new Array<Object>();
        capabilities = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("role_has_cap", this.capabilities, cap, this.name);

        if (!empty(capabilities.getValue(cap))) {
            return booleanval(capabilities.getValue(cap));
        } else {
            return false;
        }
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
