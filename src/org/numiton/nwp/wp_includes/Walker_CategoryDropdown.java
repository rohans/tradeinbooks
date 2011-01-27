/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Walker_CategoryDropdown.java,v 1.3 2008/10/03 18:45:30 numiton Exp $
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

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.Ref;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;

public class Walker_CategoryDropdown extends Walker implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	    = Logger.getLogger(Walker_CategoryDropdown.class.getName());

	public Walker_CategoryDropdown(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
		super(javaGlobalVariables, javaGlobalConstants);
		tree_type	= "category";
		db_fields	= new Array<Object>(new ArrayEntry<Object>("parent", "parent"), new ArrayEntry<Object>("id", "term_id")); //TODO: decouple this
	}

	public void start_lvl(Ref<String> output, int depth, Array<Object> args) {}
	public void end_lvl(Ref<String> output, int depth, Array<Object> args) {}
	public void end_el(Ref<String> output, StdClass page, int depth, Array<Object> deprecated) {}
	
	public void start_el(Ref<String> output, StdClass category, int depth, Array<Object> args) {
		String pad = null;
		Object cat_name = null;
		String format = null;
		pad = Strings.str_repeat("&nbsp;", intval(depth) * 3);
		cat_name = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("list_cats", StdClass.getValue(category, "name"), category);
		output.value = strval(output.value) + "\t<option value=\"" + StdClass.getValue(category, "term_id") + "\"";
		if (equal(StdClass.getValue(category, "term_id"), args.getValue("selected"))) {
			output.value = strval(output.value) + " selected=\"selected\"";
		}
		output.value = strval(output.value) + ">";
		output.value = strval(output.value) + pad + strval(cat_name);
		if (booleanval(args.getValue("show_count"))) {
			output.value = strval(output.value) + "&nbsp;&nbsp;(" + StdClass.getValue(category, "count") + ")";
		}
		if (booleanval(args.getValue("show_last_update"))) {
			format = "Y-m-d";
			output.value = strval(output.value) + "&nbsp;&nbsp;" + DateTime.gmdate(format, intval(StdClass.getValue(category, "last_update_timestamp")));
		}
		output.value = strval(output.value) + "</option>\n";
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
