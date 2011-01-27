/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Walker_Category.java,v 1.3 2008/10/03 18:45:29 numiton Exp $
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
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;

public class Walker_Category extends Walker implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	    = Logger.getLogger(Walker_Category.class.getName());

	public Walker_Category(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
		super(javaGlobalVariables, javaGlobalConstants);
		tree_type	= "category";
		db_fields	= new Array<Object>(new ArrayEntry<Object>("parent", "parent"), new ArrayEntry<Object>("id", "term_id"));
	}

	/** 
	 * TODO: decouple this TODO: decouple this
	 */
	public void start_lvl(Ref<String> output, int depth, Array<Object> args) {
		String indent = null;
		if (!equal("list", args.getValue("style"))) {
			return;
		}
		indent = Strings.str_repeat("\t", depth);
		output.value = output.value + indent + "<ul class=\'children\'>\n";
	}

	public void end_lvl(Ref<String> output, int depth, Array<Object> args) {
		String indent = null;
		if (!equal("list", args.getValue("style"))) {
			return;
		}
		indent = Strings.str_repeat("\t", depth);
		output.value = output.value + indent + "</ul>\n";
	}

	public void start_el(Ref<String> output, StdClass category, int depth, Array<Object> args) {
		String cat_name = null;
		String link = null;
		Object use_desc_for_title = null;
		Object feed_image = null;
		Object feed = null;
		String feed_type = null;
		String alt = null;
		String title = null;
		Object name = null;
		Object show_count = null;
		Object show_date = null;
		Object current_category = null;
		Object _current_category = null;
		String _class = null;
		{

			// output = Array.extractVar(args, "output", output,

			// Array.EXTR_OVERWRITE);

			// category = Array.extractVar(args, "category", category,

			// Array.EXTR_OVERWRITE);

			// depth = Array.extractVar(args, "depth", depth,

			// Array.EXTR_OVERWRITE);

			cat_name = strval(Array.extractVar(args, "cat_name", cat_name, Array.EXTR_OVERWRITE));
			link = strval(Array.extractVar(args, "link", link, Array.EXTR_OVERWRITE));
			use_desc_for_title = Array.extractVar(args, "use_desc_for_title", use_desc_for_title, Array.EXTR_OVERWRITE);
			feed_image = Array.extractVar(args, "feed_image", feed_image, Array.EXTR_OVERWRITE);
			feed = Array.extractVar(args, "feed", feed, Array.EXTR_OVERWRITE);
			feed_type = strval(Array.extractVar(args, "feed_type", feed_type, Array.EXTR_OVERWRITE));
			alt = strval(Array.extractVar(args, "alt", alt, Array.EXTR_OVERWRITE));
			title = strval(Array.extractVar(args, "title", title, Array.EXTR_OVERWRITE));
			name = Array.extractVar(args, "name", name, Array.EXTR_OVERWRITE);
			show_count = Array.extractVar(args, "show_count", show_count, Array.EXTR_OVERWRITE);
			show_date = Array.extractVar(args, "show_date", show_date, Array.EXTR_OVERWRITE);
			current_category = Array.extractVar(args, "current_category", current_category, Array.EXTR_OVERWRITE);
			_current_category = Array.extractVar(args, "_current_category", _current_category, Array.EXTR_OVERWRITE);
			_class = strval(Array.extractVar(args, "class", _class, Array.EXTR_OVERWRITE));
		}
		cat_name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(category, "name")));
		cat_name = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("list_cats", cat_name, category));
		link = "<a href=\"" + getIncluded(Category_templatePage.class, gVars, gConsts).get_category_link(StdClass.getValue(category, "term_id")) + "\" ";
		if (equal(use_desc_for_title, 0) || empty(StdClass.getValue(category, "description"))) {
			link = link + "title=\"" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts filed under %s", "default"), cat_name) + "\"";
		}
		else
			link = link
			        + "title=\""
			        + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(getIncluded(PluginPage.class, gVars, gConsts)
			                .apply_filters("category_description", StdClass.getValue(category, "description"), category))) + "\"";
		link = link + ">";
		link = link + cat_name + "</a>";
		if (!empty(feed_image) || !empty(feed)) {
			link = link + " ";
			if (empty(feed_image)) {
				link = link + "(";
			}
			link = link + "<a href=\""
			        + getIncluded(Link_templatePage.class, gVars, gConsts).get_category_feed_link(intval(StdClass.getValue(category, "term_id")), feed_type) + "\"";
			if (empty(feed)) {
				alt = " alt=\"" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Feed for all posts filed under %s", "default"), cat_name) + "\"";
			}
			else {
				title = " title=\"" + strval(feed) + "\"";
				alt = " alt=\"" + strval(feed) + "\"";
				name = feed;
				link = link + title;
			}
			link = link + ">";
			if (empty(feed_image)) {
				link = link + strval(name);
			}
			else
				link = link + "<img src=\'" + strval(feed_image) + "\'" + alt + title + " />";
			link = link + "</a>";
			if (empty(feed_image)) {
				link = link + ")";
			}
		}
		if (isset(show_count) && booleanval(show_count)) {
			link = link + " (" + strval(StdClass.getValue(category, "count")) + ")";
		}
		if (isset(show_date) && booleanval(show_date)) {
			link = link + " " + DateTime.gmdate("Y-m-d", intval(StdClass.getValue(category, "last_update_timestamp")));
		}
		if (isset(current_category) && booleanval(current_category)) {
			_current_category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(current_category, gConsts.getOBJECT(), "raw");
		}
		if (equal("list", args.getValue("style"))) {
			output.value = output.value + "\t<li";
			_class = "cat-item cat-item-" + StdClass.getValue(category, "term_id");
			if (isset(current_category) && booleanval(current_category) && equal(StdClass.getValue(category, "term_id"), current_category)) {
				_class = _class + " current-cat";
			}
			else
				if (isset(_current_category) && _current_category instanceof StdClass && equal(StdClass.getValue(category, "term_id"), ((StdClass) _current_category).fields.getValue("parent"))) {
					_class = _class + " current-cat-parent";
				}
			output.value = output.value + " class=\"" + _class + "\"";
			output.value = output.value + ">" + link + "\n";
		}
		else {
			output.value = output.value + "\t" + link + "<br />\n";
		}
	}

	public void end_el(Ref<String> output, StdClass page, int depth, Array<Object> args) {
		if (!equal("list", args.getValue("style"))) {
			return;
		}
		output.value = output.value + "</li>\n";
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
