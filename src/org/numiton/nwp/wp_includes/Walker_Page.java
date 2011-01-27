/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Walker_Page.java,v 1.3 2008/10/03 18:45:30 numiton Exp $
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


public class Walker_Page extends Walker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Walker_Page.class.getName());

    public Walker_Page(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants);
        tree_type = "page";
        db_fields = new Array<Object>(new ArrayEntry<Object>("parent", "post_parent"), new ArrayEntry<Object>("id", "ID")); //TODO: decouple this
    }

    public void start_lvl(Ref<String> output, int depth, int deprecated, Array<Object> args) {
        start_lvl(output, depth, args);
    }

    /**
     * TODO: decouple this TODO: decouple this
     */
    public void start_lvl(Ref<String> output, int depth, Array<Object> args) {
        String indent = null;
        indent = Strings.str_repeat("\t", depth);
        output.value = output.value + "\n" + indent + "<ul>\n";
    }

    public void end_lvl(Ref<String> output, int depth, int deprecated, Array<Object> args) {
        end_lvl(output, depth, args);
    }

    public void end_lvl(Ref<String> output, int depth, Array<Object> args) {
        String indent = null;
        indent = Strings.str_repeat("\t", depth);
        output.value = output.value + indent + "</ul>\n";
    }

    public void start_el(Ref<String> output, StdClass page, int depth, Array<Object> args) {
        start_el(output, page, depth, 0, args);
    }

    public void start_el(Ref<String> output, StdClass page, int depth, int current_page, Array<Object> args) {
        String indent = null;
        String css_class = null;
        StdClass _current_page = null;
        Object show_date = null;
        String time = null;
        String date_format = null;

        if (booleanval(depth)) {
            indent = Strings.str_repeat("\t", depth);
        } else {
            indent = "";
        }

        show_date = Array.extractVar(args, "show_date", show_date, Array.EXTR_SKIP);
        date_format = strval(Array.extractVar(args, "date_format", date_format, Array.EXTR_SKIP));
        css_class = "page_item page-item-" + StdClass.getValue(page, "ID");

        if (!empty(current_page)) {
            _current_page = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page(current_page, gConsts.getOBJECT(), "raw");

            if (Array.in_array(StdClass.getValue(page, "ID"), new Array<Object>(StdClass.getValue(_current_page, "ancestors")))) {
                css_class = css_class + " current_page_ancestor";
            }

            if (equal(StdClass.getValue(page, "ID"), current_page)) {
                css_class = css_class + " current_page_item";
            } else if (booleanval(_current_page) && equal(StdClass.getValue(page, "ID"), StdClass.getValue(_current_page, "post_parent"))) {
                css_class = css_class + " current_page_parent";
            }
        }

        output.value = output.value + indent + "<li class=\"" + css_class + "\"><a href=\"" +
            getIncluded(Link_templatePage.class, gVars, gConsts).get_page_link(intval(StdClass.getValue(page, "ID")), false) + "\" title=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(page, "post_title")))) +
            "\">" + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(page, "post_title")) + "</a>";

        if (!empty(show_date)) {
            if (equal("modified", show_date)) {
                time = strval(StdClass.getValue(page, "post_modified"));
            } else {
                time = strval(StdClass.getValue(page, "post_date"));
            }

            output.value = output.value + " " + getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(date_format, time, true);
        }
    }

    public void end_el(Ref<String> output, StdClass page, int depth, Array<Object> deprecated) {
        end_el(output, page, depth, 0, deprecated);
    }

    public void end_el(Ref<String> output, StdClass page, int depth, int current_page, Array<Object> deprecated) {
        output.value = output.value + "</li>\n";
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
