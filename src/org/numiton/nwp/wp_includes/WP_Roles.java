/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Roles.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;


public class WP_Roles implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Roles.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> roles = new Array<Object>();
    public Array<WP_Role> role_objects = new Array<WP_Role>();
    public Array<Object> role_names = new Array<Object>();
    public String role_key;
    public boolean use_db = true;
    public Array<Object> wp_user_roles;

    public WP_Roles(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this._init();
    }

    public void _init() {
        Object role = null;
        Object data = null;
        Object rolesObj = null;

        this.role_key = gVars.wpdb.prefix + "user_roles";

        if (!empty(wp_user_roles)) {
            this.roles = wp_user_roles;
            this.use_db = false;
        } else {
            rolesObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option(this.role_key);
        }

        if (empty(rolesObj)) {
            return;
        }

        this.roles = (Array<Object>) rolesObj;

        this.role_objects = new Array<WP_Role>();
        this.role_names = new Array<Object>();

        for (Map.Entry javaEntry386 : this.roles.entrySet()) {
            role = javaEntry386.getKey();
            data = javaEntry386.getValue();
            this.role_objects.putValue(role, new WP_Role(gVars, gConsts, strval(role), this.roles.getArrayValue(role).getArrayValue("capabilities")));
            this.role_names.putValue(role, this.roles.getArrayValue(role).getValue("name"));
        }
    }

    public Object add_role(String role, String display_name, Array<Object> capabilities) {
        if (isset(this.roles.getValue(role))) {
            return null;
        }

        this.roles.putValue(role, new Array<Object>(new ArrayEntry<Object>("name", display_name), new ArrayEntry<Object>("capabilities", capabilities)));

        if (this.use_db) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option(this.role_key, this.roles);
        }

        this.role_objects.putValue(role, new WP_Role(gVars, gConsts, role, capabilities));
        this.role_names.putValue(role, display_name);

        return this.role_objects.getValue(role);
    }

    public void remove_role(Object role) {
        if (!isset(this.role_objects.getValue(role))) {
            return;
        }

        this.role_objects.arrayUnset(role);
        this.role_names.arrayUnset(role);
        this.roles.arrayUnset(role);

        if (this.use_db) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option(this.role_key, this.roles);
        }
    }

    public void add_cap(String role, Object cap, Object grant) {
        this.roles.getArrayValue(role).getArrayValue("capabilities").putValue(cap, grant);

        if (this.use_db) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option(this.role_key, this.roles);
        }
    }

    public void remove_cap(String role, Object cap) {
        this.roles.getArrayValue(role).getArrayValue("capabilities").arrayUnset(cap);

        if (this.use_db) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option(this.role_key, this.roles);
        }
    }

    public WP_Role get_role(String role) {
        if (isset(this.role_objects.getValue(role))) {
            return this.role_objects.getValue(role);
        } else {
            return null;
        }
    }

    public Array<Object> get_names() {
        return this.role_names;
    }

    public boolean is_role(Object role) {
        return isset(this.role_names.getValue(role));
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
