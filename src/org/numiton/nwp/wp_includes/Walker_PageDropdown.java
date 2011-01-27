/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Walker_PageDropdown.java,v 1.4 2008/10/03 18:45:30 numiton Exp $
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
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.Ref;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


public class Walker_PageDropdown extends Walker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Walker_PageDropdown.class.getName());

    public Walker_PageDropdown(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants);
        tree_type = "page";
        db_fields = new Array<Object>(new ArrayEntry<Object>("parent", "post_parent"), new ArrayEntry<Object>("id", "ID")); //TODO: decouple this
    }

    public void start_lvl(Ref<String> output, int depth, Array<Object> args) {
    }

    public void end_lvl(Ref<String> output, int depth, Array<Object> args) {
    }

    public void end_el(Ref<String> output, StdClass page, int depth, Array<Object> deprecated) {
    }

    /**
     * TODO: decouple this
     */
    public void start_el(Ref<String> output, StdClass page, int depth, Array<Object> args) {
        String pad = null;
        String title = null;
        pad = Strings.str_repeat("&nbsp;", depth * 3);
        output.value = output.value + "\t<option value=\"" + StdClass.getValue(page, "ID") + "\"";

        if (equal(StdClass.getValue(page, "ID"), args.getValue("selected"))) {
            output.value = output.value + " selected=\"selected\"";
        }

        output.value = output.value + ">";
        title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(page, "post_title")), strval(0));
        output.value = output.value + pad + title;
        output.value = output.value + "</option>\n";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
