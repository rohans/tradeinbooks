/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_User.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import com.numiton.FunctionHandling;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;

public class WP_User extends StdClass implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	= Logger.getLogger(WP_User.class.getName());
	public GlobalConsts	          gConsts;
	public GlobalVars	          gVars;
	public StdClass	              data;

	//	public int	                  id	  = 0; // Deprecated, use $ID instead.
	//	public int	                  ID	  = 0;
	//	public Array<Object>	      caps; /* Is initialized in code */
	//	public String	              cap_key;
	//	public Array<Object>	      roles	  = new Array<Object>();
	//	public Array<Object>	      allcaps	= new Array<Object>();
	//	public Object	user_level;
	//	public String	user_login;
	//	public String	user_email;
	//	public String	user_url;
	//	public String	first_name;
	//	public String	last_name;
	//	public String	display_name;
	//	public String	nickname;
	//	public String	aim;
	//	public String	yim;
	//	public String	jabber;
	//	public String	description;
	//	public String	rich_editing;
	//	public Array<Object>	capabilities;
	//	public String	user_pass;
	//	public String admin_color;
	//	public Object role;
	public WP_User(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object id) {
		this(javaGlobalVariables, javaGlobalConstants, id, "");
	}

	public WP_User(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object id, String name) {
		setContext(javaGlobalVariables, javaGlobalConstants);
		Object key = null;
		Object value = null;
		if (empty(id) && empty(name)) {
			return;
		}
		if (!is_numeric(id)) {
			name = strval(id);
			id = 0;
		}
		if (!empty(id)) {
			this.data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(id));
		}
		else
			this.data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(name);
		if (empty(StdClass.getValue(this.data, "ID"))) {
			return;
		}
		for (Map.Entry javaEntry387 : ClassHandling.get_object_vars(this.data).entrySet()) {
			key = javaEntry387.getKey();
			value = javaEntry387.getValue();
			this.fields.putValue(key, value);
		}

		//		this.id = this.ID;

		this._init_caps();
	}

	public void _init_caps() {
		setCap_key(gVars.wpdb.prefix + "capabilities");
		
		// Modified by Numiton
		Object tmpObj = this.fields.getValue(getCap_key());
		
		if (!is_array(tmpObj)) {
			this.setCaps(new Array<Object>());
		} else {
			setCaps(tmpObj);
		}
		this.get_role_caps();
	}

	public void get_role_caps() {
		String role = null;
		
		if (!isset(gVars.wp_roles)) {
			gVars.wp_roles = new WP_Roles(gVars, gConsts);
		}
		
		//Filter out caps that are not role names and assign to $this->roles
		if (is_array(this.getCaps())) {
			this.setRoles(Array.array_filter(Array.array_keys((Array) this.getCaps()), new Callback("is_role", gVars.wp_roles)));
		}
		
		//Build $allcaps from role caps, overlay user's $caps
		this.setAllcaps(new Array<Object>());
		for (Map.Entry javaEntry388 : new Array<Object>(this.getRoles()).entrySet()) {
			role = strval(javaEntry388.getValue());
			WP_Role roleObj = gVars.wp_roles.get_role(role);
			this.setAllcaps(Array.array_merge(this.getAllcaps(), roleObj.capabilities));
		}
		this.setAllcaps(Array.array_merge(this.getAllcaps(), (Array) this.getCaps()));
	}

	public void add_role(Object role) {
		((Array) this.getCaps()).putValue(role, true);
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), this.getCap_key(), this.getCaps());
		this.get_role_caps();
		this.update_user_level_from_caps();
	}

	public void remove_role(Object role) {
		if (empty(this.getRoles().getValue(role)) || Array.count(this.getRoles()) <= 1) {
			return;
		}
		((Array) this.getCaps()).arrayUnset(role);
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), this.getCap_key(), this.getCaps());
		this.get_role_caps();
	}

	public void set_role(Object role) {
		Object oldrole = null;
		for (Map.Entry javaEntry389 : this.getRoles().entrySet()) {
			oldrole = javaEntry389.getValue();
			((Array) this.getCaps()).arrayUnset(oldrole);
		}
		if (!empty(role)) {
			((Array) this.getCaps()).putValue(role, true);
			this.setRoles(new Array<Object>(new ArrayEntry<Object>(role, true)));
		}
		else {
			this.setRoles(new Array<Object>());
		}
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), this.getCap_key(), this.getCaps());
		this.get_role_caps();
		this.update_user_level_from_caps();
	}

	public int level_reduction(int max, String item) {
		Array matches = new Array();
		int level = 0;
		if (QRegExPerl.preg_match("/^level_(10|[0-9])$/i", item, matches)) {
			level = intval(matches.getValue(1));
			return intval(Math.max(max, level));
		}
		else {
			return max;
		}
	}

	public void update_user_level_from_caps() {
		this.setUser_level(Array.array_reduce(Array.array_keys(this.getAllcaps()), new Callback("level_reduction", this), 0));
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), gVars.wpdb.prefix + "user_level", this.getUser_level());
	}

	public void add_cap(Object cap, Object grant) {
		((Array) this.getCaps()).putValue(cap, grant);
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), this.getCap_key(), this.getCaps());
	}

	public void remove_cap(Object cap) {
		if (empty(((Array) this.getCaps()).getValue(cap))) {
			return;
		}
		((Array) this.getCaps()).arrayUnset(cap);
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), this.getCap_key(), this.getCaps());
	}

	public void remove_all_caps() {
		this.setCaps(new Array<Object>());
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), this.getCap_key(), "");
		getIncluded(UserPage.class, gVars, gConsts).update_usermeta(this.getID(), gVars.wpdb.prefix + "user_level", "");
		this.get_role_caps();
	}

	//has_cap(capability_or_role_name) or
	//has_cap('edit_post', post_id)
	public boolean has_cap(Object cap, Object... vargs) {
		Array<Object> args = new Array<Object>();
		Array<Object> caps = null;
		Array<Object> capabilities = new Array<Object>();
		if (is_numeric(cap)) {
			cap = this.translate_level_to_cap(cap);
		}

		// Modified by Numiton

		args = FunctionHandling.func_get_args(vargs);
		args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>(cap), new ArrayEntry<Object>(this.getID())), args);
		caps = (Array<Object>) FunctionHandling.call_user_func_array(new Callback("map_meta_cap", getIncluded(CapabilitiesPage.class, gVars, gConsts)), args);
		
		// Must have ALL requested caps
		capabilities = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("user_has_cap", this.getAllcaps(), caps, args);
		
		// Added by Numiton
		if(!isset(caps)) {
			caps = new Array<Object>();
		}
		
		for (Map.Entry javaEntry390 : caps.entrySet()) {
			cap = javaEntry390.getValue();
			
			//echo "Checking cap $cap<br />";
			if (empty(capabilities.getValue(cap)) || !booleanval(capabilities.getValue(cap))) {
				return false;
			}
		}
		return true;
	}

	public String translate_level_to_cap(Object level) {
		return "level_" + strval(level);
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

	public StdClass getData() {
		return data;
	}

	public void setData(StdClass data) {
		this.data = data;
	}

	public int getID() {
		return intval(fields.getValue("ID"));
	}

	public void setID(int id) {
		this.fields.putValue("ID", id);
	}

	public Array<Object> getCaps() {
		return fields.getArrayValue("caps");
	}

	public void setCaps(Object caps) {
		this.fields.putValue("caps", caps);
	}

	public String getCap_key() {
		return strval(fields.getValue("cap_key"));
	}

	public void setCap_key(String cap_key) {
		this.fields.putValue("cap_key", cap_key);
	}

	public Array<Object> getRoles() {
		return fields.getArrayValue("roles");
	}

	public void setRoles(Array<Object> roles) {
		this.fields.putValue("roles", roles);
	}

	public Array<Object> getAllcaps() {
		return fields.getArrayValue("allcaps");
	}

	public void setAllcaps(Array<Object> allcaps) {
		this.fields.putValue("allcaps", allcaps);
	}

	public Object getUser_level() {
		return fields.getValue("user_level");
	}

	public void setUser_level(Object user_level) {
		this.fields.putValue("user_level", user_level);
	}

	public String getUser_login() {
		return strval(fields.getValue("user_login"));
	}

	public void setUser_login(String user_login) {
		this.fields.putValue("user_login", user_login);
	}

	public String getUser_email() {
		return strval(fields.getValue("user_email"));
	}

	public void setUser_email(String user_email) {
		this.fields.putValue("user_email", user_email);
	}

	public String getUser_url() {
		return strval(fields.getValue("user_url"));
	}

	public void setUser_url(String user_url) {
		this.fields.putValue("user_url", user_url);
	}

	public String getFirst_name() {
		return strval(fields.getValue("first_name"));
	}

	public void setFirst_name(String first_name) {
		this.fields.putValue("first_name", first_name);
	}

	public String getLast_name() {
		return strval(fields.getValue("last_name"));
	}

	public void setLast_name(String last_name) {
		this.fields.putValue("last_name", last_name);
	}

	public String getDisplay_name() {
		return strval(fields.getValue("display_name"));
	}

	public void setDisplay_name(String display_name) {
		this.fields.putValue("display_name", display_name);
	}

	public String getNickname() {
		return strval(fields.getValue("nickname"));
	}

	public void setNickname(String nickname) {
		this.fields.putValue("nickname", nickname);
	}

	public String getAim() {
		return strval(fields.getValue("aim"));
	}

	public void setAim(String aim) {
		this.fields.putValue("aim", aim);
	}

	public String getYim() {
		return strval(fields.getValue("yim"));
	}

	public void setYim(String yim) {
		this.fields.putValue("yim", yim);
	}

	public String getJabber() {
		return strval(fields.getValue("jabber"));
	}

	public void setJabber(String jabber) {
		this.fields.putValue("jabber", jabber);
	}

	public String getDescription() {
		return strval(fields.getValue("description"));
	}

	public void setDescription(String description) {
		this.fields.putValue("description", description);
	}

	public String getRich_editing() {
		return strval(fields.getValue("rich_editing"));
	}

	public void setRich_editing(String rich_editing) {
		this.fields.putValue("rich_editing", rich_editing);
	}

	public Array<Object> getCapabilities() {
		return fields.getArrayValue("capabilities");
	}

	public void setCapabilities(Array<Object> capabilities) {
		this.fields.putValue("capabilities", capabilities);
	}

	public String getUser_pass() {
		return strval(fields.getValue("user_pass"));
	}

	public void setUser_pass(String user_pass) {
		this.fields.putValue("user_pass", user_pass);
	}

	public String getAdmin_color() {
		return strval(fields.getValue("admin_color"));
	}

	public void setAdmin_color(String admin_color) {
		this.fields.putValue("admin_color", admin_color);
	}

	public Object getRole() {
		return fields.getValue("role");
	}

	public void setRole(Object role) {
		this.fields.putValue("role", role);
	}
}
