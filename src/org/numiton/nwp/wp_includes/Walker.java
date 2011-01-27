/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Walker.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;


/*
 * A class for displaying various tree-like structures.
 * Extend the Walker class to use it, see examples at the bottom
 */
public abstract class Walker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Walker.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String tree_type;
    public Array<Object> db_fields = new Array<Object>();

    public Walker(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
    }

    /**
     * abstract callbacks
     */
    public abstract void start_lvl(Ref<String> output, int depth, Array<Object> args);

    public abstract void end_lvl(Ref<String> output, int depth, Array<Object> args);

    public abstract void start_el(Ref<String> output, StdClass page, int depth, Array<Object> args);

    public abstract void end_el(Ref<String> output, StdClass page, int depth, Array<Object> deprecated);

    /**
     * display one element if the element doesn't have any children 
     * otherwise, display the element and its children
     */
    public void display_element(StdClass element, Array<Object> children_elements, int max_depth, int depth, Array<Object> args, Ref<String> output) {
        String id_field = null;
        String parent_field = null;
        Array<Object> cb_args = new Array<Object>();
        StdClass child = null;
        int i = 0;
        Boolean newlevel = null;

        if (!booleanval(element)) {
            return;
        }

        id_field = strval(this.db_fields.getValue("id"));
        parent_field = strval(this.db_fields.getValue("parent"));
        
		//display this element
        cb_args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>(output), new ArrayEntry<Object>(element), new ArrayEntry<Object>(depth)), args);
        FunctionHandling.call_user_func_array(new Callback("start_el", this), cb_args);

        if (equal(max_depth, 0) || (!equal(max_depth, 0) && (max_depth > (depth + 1)))) { //whether to descend
            for (i = 0; i < Array.sizeof(children_elements); i++) {
                child = (StdClass) children_elements.getValue(i);

                if (equal(StdClass.getValue(child, parent_field), StdClass.getValue(element, id_field))) {
                    if (!isset(newlevel)) {
                        newlevel = true;
						//start the child delimiter
                        cb_args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>(output), new ArrayEntry<Object>(depth)), args);
                        FunctionHandling.call_user_func_array(new Callback("start_lvl", this), cb_args);
                    }

                    Array.array_splice(children_elements, i, 1);
                    this.display_element(child, children_elements, max_depth, depth + 1, args, output);
                    i = -1;
                }
            }
        }

        if (isset(newlevel) && newlevel) {
			//end the child delimiter
            cb_args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>(output), new ArrayEntry<Object>(depth)), args);
            FunctionHandling.call_user_func_array(new Callback("end_lvl", this), cb_args);
        }

		//end this element
        cb_args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>(output), new ArrayEntry<Object>(element), new ArrayEntry<Object>(depth)), args);
        FunctionHandling.call_user_func_array(new Callback("end_el", this), cb_args);
    }

	/*
 	* displays array of elements hierarchically
 	* it is a generic function which does not assume any existing order of elements
 	* max_depth = -1 means flatly display every element
 	* max_depth = 0  means display all levels
 	* max_depth > 0  specifies the number of display levels.
 	*/
    public String walk(Array<Object> elements, int max_depth, Object... vargs) {
        Array<Object> args = new Array<Object>();
        Ref<String> output = new Ref<String>();
        String id_field = null;
        String parent_field = null;
        Array<Object> empty_array = new Array<Object>();
        StdClass e = null;
        Array<Object> top_level_elements = new Array<Object>();
        Array<Object> children_elements = new Array<Object>();
        StdClass root = null;
        StdClass child = null;
        int i = 0;
        StdClass orphan_e = null;

        // Modified by Numiton
        args = FunctionHandling.func_get_args(vargs);
        output.value = "";

        if (max_depth < -1) { //invalid parameter
            return output.value;
        }

        if (empty(elements)) { //nothing to walk
            return output.value;
        }

        id_field = strval(this.db_fields.getValue("id"));
        parent_field = strval(this.db_fields.getValue("parent"));

		// flat display
        if (equal(-1, max_depth)) {
            empty_array = new Array<Object>();

            for (Map.Entry javaEntry416 : elements.entrySet()) {
                e = (StdClass) javaEntry416.getValue();
                this.display_element(e, empty_array, 1, 0, args, output);
            }

            return output.value;
        }

		/*
		 * need to display in hierarchical order
		 * splice elements into two buckets: those without parent and those with parent
		 */
        top_level_elements = new Array<Object>();
        children_elements = new Array<Object>();

        for (Map.Entry javaEntry417 : elements.entrySet()) {
            e = (StdClass) javaEntry417.getValue();

            if (equal(0, StdClass.getValue(e, parent_field))) {
                top_level_elements.putValue(e);
            } else {
                children_elements.putValue(e);
            }
        }

		/*
		 * none of the elements is top level
		 * the first one must be root of the sub elements
		 */
        if (!booleanval(top_level_elements)) {
            root = (StdClass) children_elements.getValue(0);

            for (i = 0; i < Array.sizeof(children_elements); i++) {
                child = (StdClass) children_elements.getValue(i);

                if (equal(StdClass.getValue(root, parent_field), StdClass.getValue(child, parent_field))) {
                    top_level_elements.putValue(child);
                    Array.array_splice(children_elements, i, 1);
                    i--;
                }
            }
        }

        for (Map.Entry javaEntry418 : top_level_elements.entrySet()) {
            e = (StdClass) javaEntry418.getValue();
            this.display_element(e, children_elements, max_depth, 0, args, output);
        }

		/*
		* if we are displaying all levels, and remaining children_elements is not empty,
		* then we got orphans, which should be displayed regardless
	 	*/
        if (equal(max_depth, 0) && (Array.sizeof(children_elements) > 0)) {
            empty_array = new Array<Object>();

            for (Map.Entry javaEntry419 : children_elements.entrySet()) {
                orphan_e = (StdClass) javaEntry419.getValue();
                this.display_element(orphan_e, empty_array, 1, 0, args, output);
            }
        }

        return output.value;
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
