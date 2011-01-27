/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Walker_Category_Checklist.java,v 1.3 2008/10/03 18:45:31 numiton Exp $
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
package org.numiton.nwp.wp_admin.includes;

import static com.numiton.VarHandling.strval;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_includes.FormattingPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.Walker;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.Ref;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


public class Walker_Category_Checklist extends Walker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Walker_Category_Checklist.class.getName());

    public Walker_Category_Checklist(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants);
        tree_type = "category";
        db_fields = new Array<Object>(new ArrayEntry<Object>("parent", "parent"), new ArrayEntry<Object>("id", "term_id")); //TODO: decouple this
    }

    public void start_lvl(Ref<String> output, int depth, Array<Object> args) {
        String indent = null;
        indent = Strings.str_repeat("\t", depth);
        output.value = output.value + indent + "<ul class=\'children\'>\n";
    }

    public void end_lvl(Ref<String> output, int depth, Array<Object> args) {
        String indent = null;
        indent = Strings.str_repeat("\t", depth);
        output.value = output.value + indent + "</ul>\n";
    }

    public void start_el(Ref<String> output, StdClass category, int depth, Array<Object> args) {
        String _class = null;
        Array<Object> popular_cats = null;
        Array<Object> selected_cats = null;
        popular_cats = (Array<Object>) Array.extractVar(args, "popular_cats", popular_cats, Array.EXTR_OVERWRITE);
        selected_cats = (Array<Object>) Array.extractVar(args, "selected_cats", selected_cats, Array.EXTR_OVERWRITE);
        _class = (Array.in_array(StdClass.getValue(category, "term_id"), popular_cats)
            ? " class=\"popular-category\""
            : "");
        output.value = output.value + "\n<li id=\'category-" + StdClass.getValue(category, "term_id") + "\'" + _class + ">" + "<label for=\"in-category-" + StdClass.getValue(category, "term_id") +
            "\" class=\"selectit\"><input value=\"" + StdClass.getValue(category, "term_id") + "\" type=\"checkbox\" name=\"post_category[]\" id=\"in-category-" +
            StdClass.getValue(category, "term_id") + "\"" + (Array.in_array(StdClass.getValue(category, "term_id"), selected_cats)
            ? " checked=\"checked\""
            : "") + "/> " +
            getIncluded(FormattingPage.class, gVars, gConsts)
                .wp_specialchars(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_category", StdClass.getValue(category, "name"))), strval(0)) + "</label>";
    }

    public void end_el(Ref<String> output, StdClass category, int depth, Array<Object> args) {
        output.value = output.value + "</li>\n";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
