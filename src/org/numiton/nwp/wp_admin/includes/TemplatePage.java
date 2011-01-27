/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: TemplatePage.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.numiton.nwp.wp_includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.UserPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QArray;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;

@Controller
@Scope("request")
public class TemplatePage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(TemplatePage.class.getName());

	@Override
	@RequestMapping("/wp-admin/includes/template.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_admin/includes/template";
	}
	public String	display_page_row_class;
	public boolean	_list_meta_row_update_nonce	= false;

	//
	// Big Mess
	//

	// Dandy new recursive multiple category stuff.
	public void cat_rows(int parent, int level, Object categories) {
		Array<Object> args = new Array<Object>();
		Array<Object> children = new Array<Object>();
		StdClass category = null;
		String output = null;
		if (!booleanval(categories)) {
			args = new Array<Object>(new ArrayEntry<Object>("hide_empty", 0));
			if (!empty(gVars.webEnv._GET.getValue("s"))) {
				args.putValue("search", gVars.webEnv._GET.getValue("s"));
			}
			categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories(args);
		}
		children = getIncluded(TaxonomyPage.class, gVars, gConsts)._get_term_hierarchy("category");
		if (booleanval(categories)) {
			OutputControl.ob_start(gVars.webEnv);
			for (Map.Entry javaEntry196 : ((Array<?>) categories).entrySet()) {
				category = (StdClass) javaEntry196.getValue();
				if (equal(StdClass.getValue(category, "parent"), parent)) {
					echo(gVars.webEnv, "\t" + _cat_row(category, level, strval(false)));
					if (isset(children.getValue(StdClass.getValue(category, "term_id")))) {
						cat_rows(intval(StdClass.getValue(category, "term_id")), level + 1, categories);
					}
				}
			}
			output = OutputControl.ob_get_contents(gVars.webEnv);
			OutputControl.ob_end_clean(gVars.webEnv);
			output = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("cat_rows", output));
			echo(gVars.webEnv, output);
		}
		else {
			return;
		}
	}

	public Object _cat_row(Object category, int level, String name_override) {
		String pad = null;
		String name = null;
		String edit = null;
		String posts_count = null;
		String output = null;
		category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(category, gConsts.getOBJECT(), "raw");
		pad = Strings.str_repeat("&#8212; ", level);
		name = (booleanval(name_override) ? name_override : (pad + " " + ((StdClass) category).fields.getValue("name")));
		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
			edit = "<a class=\'row-title\' href=\'categories.php?action=edit&amp;cat_ID="
			        + ((StdClass) category).fields.getValue("term_id")
			        + "\' title=\'"
			        + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)
			                .__("Edit \"%s\"", "default"), ((StdClass) category).fields.getValue("name"))) + "\'>" + name + "</a>";
		}
		else {
			edit = name;
		}
		gVars._class = (equal(" class=\'alternate\'", gVars._class) ? "" : " class=\'alternate\'");
		((StdClass) category).fields.putValue("count", getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(floatval(((StdClass) category).fields
		        .getValue("count")), null));
		posts_count = ((intval(((StdClass) category).fields.getValue("count")) > 0) ? ("<a href=\'edit.php?cat=" + ((StdClass) category).fields.getValue("term_id") + "\'>"
		        + ((StdClass) category).fields.getValue("count") + "</a>") : strval(((StdClass) category).fields.getValue("count")));
		output = "<tr id=\'cat-" + ((StdClass) category).fields.getValue("term_id") + "\'" + gVars._class + ">\n\t\t\t   <th scope=\'row\' class=\'check-column\'>";
		if (!equal(getIncluded(FunctionsPage.class, gVars, gConsts).absint(getIncluded(FunctionsPage.class, gVars, gConsts)
		        .get_option("default_category")), ((StdClass) category).fields.getValue("term_id"))) {
			output = output + "<input type=\'checkbox\' name=\'delete[]\' value=\'" + ((StdClass) category).fields.getValue("term_id") + "\' />";
		}
		else {
			output = output + "&nbsp;";
		}
		output = output + "</th>\n\t\t\t\t<td>" + edit + "</td>\n\t\t\t\t<td>" + ((StdClass) category).fields.getValue("description") + "</td>\n\t\t\t\t<td class=\'num\'>" + posts_count
		        + "</td>\n\t</tr>\n";
		return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("cat_row", output);
	}

	public Object link_cat_row(Object category) {
		String name = null;
		String name_override = null;
		String edit = null;
		int default_cat_id = 0;
		String count = null;
		Object output = null;
		if (!booleanval((category = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(category, "link_category", gConsts.getOBJECT(), "raw")))) {
			return false;
		}
		if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(category)) {
			return category;
		}
		name = strval((booleanval(name_override) ? name_override : ((StdClass) category).fields.getValue("name")));
		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
			edit = "<a class=\'row-title\' href=\'link-category.php?action=edit&amp;cat_ID="
			        + ((StdClass) category).fields.getValue("term_id")
			        + "\' title=\'"
			        + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)
			                .__("Edit \"%s\"", "default"), ((StdClass) category).fields.getValue("name"))) + "\' class=\'edit\'>" + name + "</a>";
			default_cat_id = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category"));
		}
		else {
			edit = name;
		}
		gVars._class = (equal(" class=\'alternate\'", gVars._class) ? "" : " class=\'alternate\'");
		((StdClass) category).fields.putValue("count", getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(floatval(((StdClass) category).fields
		        .getValue("count")), null));
		count = ((intval(((StdClass) category).fields.getValue("count")) > 0) ? ("<a href=\'link-manager.php?cat_id=" + ((StdClass) category).fields.getValue("term_id") + "\'>"
		        + ((StdClass) category).fields.getValue("count") + "</a>") : strval(((StdClass) category).fields.getValue("count")));
		output = "<tr id=\'link-cat-" + ((StdClass) category).fields.getValue("term_id") + "\'" + gVars._class + ">\n\t\t\t   <th scope=\'row\' class=\'check-column\'>";
		if (!equal(getIncluded(FunctionsPage.class, gVars, gConsts).absint(getIncluded(FunctionsPage.class, gVars, gConsts)
		        .get_option("default_link_category")), ((StdClass) category).fields.getValue("term_id"))) {
			output = strval(output) + "<input type=\'checkbox\' name=\'delete[]\' value=\'" + ((StdClass) category).fields.getValue("term_id") + "\' />";
		}
		else {
			output = strval(output) + "&nbsp;";
		}
		output = strval(output) + "</th>\n\t\t\t\t<td>" + edit + "</td>\n\t\t\t\t<td>" + ((StdClass) category).fields.getValue("description") + "</td>\n\t\t\t\t<td class=\'num\'>" + count
		        + "</td></tr>";
		return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("link_cat_row", output);
	}

	public void checked(Object checked, Object current) {
		if (equal(checked, current)) {
			echo(gVars.webEnv, " checked=\"checked\"");
		}
	}

	public void selected(Object selected, Object current) {
		if (equal(selected, current)) {
			echo(gVars.webEnv, " selected=\"selected\"");
		}
	}

	//
	// Category Checklists
	//

	// Deprecated. Use wp_link_category_checklist
	public void dropdown_categories(int _default, Object parent, Array<Object> popular_ids) {
		wp_category_checklist(intval(gVars.post_ID), 0, false);
	}

	public void wp_category_checklist(int post_id, int descendants_and_self, Object selected_cats)
	/* Do not change type */
	{
		Ref<Walker_Category_Checklist> walker = new Ref<Walker_Category_Checklist>();
		Array<Object> args = new Array<Object>();
		Array<Object> categories = new Array<Object>();
		Object self = null;
		Object output = null;
		walker.value = new Walker_Category_Checklist(gVars, gConsts);

		//		descendants_and_self = intval(descendants_and_self);

		args = new Array<Object>();
		if (booleanval(post_id)) {
			args.putValue("selected_cats", getIncluded(PostPage.class, gVars, gConsts).wp_get_post_categories(post_id, new Array<Object>()));
		}
		else
			args.putValue("selected_cats", new Array<Object>());
		if (is_array(selected_cats)) {
			args.putValue("selected_cats", selected_cats);
		}
		args.putValue("popular_cats", getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("category", new Array<Object>(new ArrayEntry<Object>("fields", "ids"),
		        new ArrayEntry<Object>("orderby", "count"), new ArrayEntry<Object>("order", "DESC"), new ArrayEntry<Object>("number", 10), new ArrayEntry<Object>("hierarchical", false))));
		if (booleanval(descendants_and_self)) {
			categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("child_of=" + strval(descendants_and_self) + "&hierarchical=0&hide_empty=0");
			self = getIncluded(CategoryPage.class, gVars, gConsts).get_category(descendants_and_self, gConsts.getOBJECT(), "raw");
			Array.array_unshift(categories, self);
		}
		else {
			categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("get=all");
		}
		args = new Array<Object>(new ArrayEntry<Object>(categories), new ArrayEntry<Object>(0), new ArrayEntry<Object>(args));
		output = FunctionHandling.call_user_func_array(new Callback("walk", walker), args);
		echo(gVars.webEnv, output);
	}

	public Array<Object> wp_popular_terms_checklist(String taxonomy, int _default, int number) {
		Array<Object> checked_categories = new Array<Object>();
		Object categories = null;
		Array<Object> popular_ids = new Array<Object>();
		StdClass category = null;
		String id = null;
		if (booleanval(gVars.post_ID)) {
			checked_categories = (Array<Object>) getIncluded(PostPage.class, gVars, gConsts).wp_get_post_categories(intval(gVars.post_ID), new Array<Object>());
		}
		else
			checked_categories = new Array<Object>();
		categories = getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms(taxonomy, new Array<Object>(new ArrayEntry<Object>("orderby", "count"),
		        new ArrayEntry<Object>("order", "DESC"), new ArrayEntry<Object>("number", number), new ArrayEntry<Object>("hierarchical", false)));
		popular_ids = new Array<Object>();
		for (Map.Entry javaEntry197 : new Array<Object>(categories).entrySet()) {
			category = (StdClass) javaEntry197.getValue();
			popular_ids.putValue(StdClass.getValue(category, "term_id"));
			id = "popular-category-" + StdClass.getValue(category, "term_id");
			echo(gVars.webEnv, "\n\t\t<li id=\"");
			echo(gVars.webEnv, id);
			echo(gVars.webEnv, "\" class=\"popular-category\">\n\t\t\t<label class=\"selectit\" for=\"in-");
			echo(gVars.webEnv, id);
			echo(gVars.webEnv, "\">\n\t\t\t<input id=\"in-");
			echo(gVars.webEnv, id);
			echo(gVars.webEnv, "\" type=\"checkbox\" value=\"");
			echo(gVars.webEnv, intval(StdClass.getValue(category, "term_id")));
			echo(gVars.webEnv, "\" />\n\t\t\t\t");
			echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(
			        strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_category", StdClass.getValue(category, "name"))), strval(0)));
			echo(gVars.webEnv, "\t\t\t</label>\n\t\t</li>\n\n\t\t");
		}
		return popular_ids;
	}

	// Deprecated. Use wp_link_category_checklist
	public void dropdown_link_categories(Object _default) {
		wp_link_category_checklist(gVars.link_id);
	}

	public void wp_link_category_checklist(Integer link_id) {
		Array<Object> checked_categories = new Array<Object>();
		Object _default = null;
		Array<Object> categories = null;
		Object cat_id = null;
		StdClass category = null;
		Object name = null;
		boolean checked = false;
		if (booleanval(link_id)) {
			checked_categories = getIncluded(BookmarkPage.class, gVars, gConsts).wp_get_link_cats(link_id);
			if (equal(Array.count(checked_categories), 0)) {
				// No selected categories, strange
				checked_categories.putValue(_default);
			}
		}
		else {
			checked_categories.putValue(_default);
		}
		categories = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("link_category", "orderby=count&hide_empty=0");
		if (empty(categories)) {
			return;
		}
		for (Map.Entry javaEntry198 : categories.entrySet()) {
			category = (StdClass) javaEntry198.getValue();
			cat_id = StdClass.getValue(category, "term_id");
			name = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(getIncluded(PluginPage.class, gVars, gConsts)
			        .apply_filters("the_category", StdClass.getValue(category, "name"))), strval(0));
			checked = Array.in_array(cat_id, checked_categories);
			
			echo(
					gVars.webEnv,
					"<li id=\"link-category-"
							+ cat_id
							+ "\"><label for=\"in-link-category-"
							+ cat_id
							+ "\" class=\"selectit\"><input value=\""
							+ cat_id
							+ "\" type=\"checkbox\" name=\"link_category[]\" id=\"in-link-category-"
							+ cat_id + "\"" 
							+ (checked ? " checked=\"checked\"": "") 
							+ "/> " + name + "</label></li>");
			
		}
	}

// Tag stuff

// Returns a single tag row (see tag_rows below)
// Note: this is also used in admin-ajax.php!
	public Object _tag_row(StdClass tag, String _class) {
		String count = null;
		Object name = null;
		Object out = null;
		count = getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(floatval(StdClass.getValue(tag, "count")), null);
		count = ((intval(count) > 0) ? ("<a href=\'edit.php?tag=" + StdClass.getValue(tag, "slug") + "\'>" + count + "</a>") : count);
		name = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_name", StdClass.getValue(tag, "name"));
		out = "";
		out = strval(out) + "<tr id=\"tag-" + StdClass.getValue(tag, "term_id") + "\"" + _class + ">";
		out = strval(out) + "<th scope=\"row\" class=\"check-column\"> <input type=\"checkbox\" name=\"delete_tags[]\" value=\"" + StdClass.getValue(tag, "term_id") + "\" /></th>";
		out = strval(out)
		        + "<td><strong><a class=\"row-title\" href=\"edit-tags.php?action=edit&amp;tag_ID="
		        + StdClass.getValue(tag, "term_id")
		        + "\" title=\""
		        + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__(
		                "Edit \"%s\"", "default"), name)) + "\">" + strval(name) + "</a></td>";
		out = strval(out) + "<td class=\'num\'>" + count + "</td>";
		out = strval(out) + "</tr>";
		return out;
	}

	// Outputs appropriate rows for the Nth page of the Tag Management screen,
	// assuming M tags displayed at a time on the page
	// Returns the number of tags displayed
	public int tag_rows(float page, int pagesize, String searchterms) {
		float start = 0;
		Array<Object> args = new Array<Object>();
		Array<Object> tags = null;
		Object out = null;
		String _class = null;
		int count = 0;
		StdClass tag = null;
		
		// Get a page worth of tags
		start = (page - floatval(1)) * floatval(pagesize);
		args = new Array<Object>(new ArrayEntry<Object>("offset", start), new ArrayEntry<Object>("number", pagesize), new ArrayEntry<Object>("hide_empty", 0));
		if (!empty(searchterms)) {
			args.putValue("search", searchterms);
		}
		
		tags = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("post_tag", args);
		
		// convert it to table rows
		out = "";
		_class = "";
		count = 0;
		for (Map.Entry javaEntry199 : tags.entrySet()) {
			tag = (StdClass) javaEntry199.getValue();
			out = strval(out) + strval(_tag_row(tag, booleanval(++count % 2) ? " class=\"alternate\"" : ""));
		}
		
		// filter and send to screen
		out = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tag_rows", out);
		echo(gVars.webEnv, out);
		return count;
	}

	// define the columns to display, the syntax is 'internal name' => 'display name'
	public Array<Object> wp_manage_posts_columns() {
		Array<Object> posts_columns = new Array<Object>();
		posts_columns = new Array<Object>();
		posts_columns.putValue("cb", "<input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" />");
		if (strictEqual("draft", gVars.webEnv._GET.getValue("post_status"))) {
			posts_columns.putValue("modified", getIncluded(L10nPage.class, gVars, gConsts).__("Modified", "default"));
		}
		else
			if (strictEqual("pending", gVars.webEnv._GET.getValue("post_status"))) {
				posts_columns.putValue("modified", getIncluded(L10nPage.class, gVars, gConsts).__("Submitted", "default"));
			}
			else
				posts_columns.putValue("date", getIncluded(L10nPage.class, gVars, gConsts).__("Date", "default"));
		posts_columns.putValue("title", getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default"));
		posts_columns.putValue("author", getIncluded(L10nPage.class, gVars, gConsts).__("Author", "default"));
		posts_columns.putValue("categories", getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default"));
		posts_columns.putValue("tags", getIncluded(L10nPage.class, gVars, gConsts).__("Tags", "default"));
		if (!Array.in_array(gVars.webEnv._GET.getValue("post_status"), new Array<Object>(new ArrayEntry<Object>("pending"), new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("future")))) {
			posts_columns.putValue("comments", "<div class=\"vers\"><img alt=\"Comments\" src=\"images/comment-grey-bubble.png\" /></div>");
		}
		posts_columns.putValue("status", getIncluded(L10nPage.class, gVars, gConsts).__("Status", "default"));
		posts_columns = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("manage_posts_columns", posts_columns);
		return posts_columns;
	}

	// define the columns to display, the syntax is 'internal name' => 'display name'
	public Array<Object> wp_manage_media_columns() {
		Array<Object> posts_columns = new Array<Object>();
		posts_columns = new Array<Object>();
		posts_columns.putValue("cb", "<input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" />");
		posts_columns.putValue("icon", "");
		posts_columns.putValue("media", getIncluded(L10nPage.class, gVars, gConsts)._c("Media|media column header", "default"));
		posts_columns.putValue("desc", getIncluded(L10nPage.class, gVars, gConsts)._c("Description|media column header", "default"));
		posts_columns.putValue("date", getIncluded(L10nPage.class, gVars, gConsts)._c("Date Added|media column header", "default"));
		posts_columns.putValue("parent", getIncluded(L10nPage.class, gVars, gConsts)._c("Appears with|media column header", "default"));
		posts_columns.putValue("comments", "<div class=\"vers\"><img alt=\"Comments\" src=\"images/comment-grey-bubble.png\" /></div>");
		posts_columns.putValue("location", getIncluded(L10nPage.class, gVars, gConsts)._c("Location|media column header", "default"));
		posts_columns = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("manage_media_columns", posts_columns);
		return posts_columns;
	}

	public Array<Object> wp_manage_pages_columns() {
		Array<Object> posts_columns = new Array<Object>();
		posts_columns = new Array<Object>();
		posts_columns.putValue("cb", "<input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" />");
		if (strictEqual("draft", gVars.webEnv._GET.getValue("post_status"))) {
			posts_columns.putValue("modified", getIncluded(L10nPage.class, gVars, gConsts).__("Modified", "default"));
		}
		else
			if (strictEqual("pending", gVars.webEnv._GET.getValue("post_status"))) {
				posts_columns.putValue("modified", getIncluded(L10nPage.class, gVars, gConsts).__("Submitted", "default"));
			}
			else
				posts_columns.putValue("date", getIncluded(L10nPage.class, gVars, gConsts).__("Date", "default"));
		posts_columns.putValue("title", getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default"));
		posts_columns.putValue("author", getIncluded(L10nPage.class, gVars, gConsts).__("Author", "default"));
		if (!Array.in_array(gVars.webEnv._GET.getValue("post_status"), new Array<Object>(new ArrayEntry<Object>("pending"), new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("future")))) {
			posts_columns.putValue("comments", "<div class=\"vers\"><img alt=\"\" src=\"images/comment-grey-bubble.png\" /></div>");
		}
		posts_columns.putValue("status", getIncluded(L10nPage.class, gVars, gConsts).__("Status", "default"));
		posts_columns = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("manage_pages_columns", posts_columns);
		return posts_columns;
	}

	/*
	 * display one row if the page doesn't have any children
	 * otherwise, display the row and its children in subsequent rows
	 */
	public boolean display_page_row(StdClass page, Array<Object> children_pages, int level) {
		String pad = null;
		int id = 0;
		Array<Object> posts_columns = new Array<Object>();
		Object title = null;
		Object column_name = null;
		Object t_time = null;
		String h_time = null;
		String m_time = null;
		int time;
		int left;
		String pending_phrase = null;
		Object column_display_name = null;
		StdClass child = null;
		int i = 0;
		gVars.post = page;
		getIncluded(QueryPage.class, gVars, gConsts).setup_postdata(page);
		page.fields.putValue("post_title", getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(page, "post_title")), strval(0)));
		pad = Strings.str_repeat("&#8212; ", level);
		id = intval(StdClass.getValue(page, "ID"));
		display_page_row_class = (equal("alternate", display_page_row_class) ? "" : "alternate");
		posts_columns = wp_manage_pages_columns();
		title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0);
		if (empty(title)) {
			title = getIncluded(L10nPage.class, gVars, gConsts).__("(no title)", "default");
		}
		echo(gVars.webEnv, "  <tr id=\'page-");
		echo(gVars.webEnv, id);
		echo(gVars.webEnv, "\' class=\'");
		echo(gVars.webEnv, display_page_row_class);
		echo(gVars.webEnv, "\'>\n\n\n ");
		for (Map.Entry javaEntry200 : posts_columns.entrySet()) {
			column_name = javaEntry200.getKey();
			column_display_name = javaEntry200.getValue();
			{
				int javaSwitchSelector12 = 0;
				if (equal(column_name, "cb"))
					javaSwitchSelector12 = 1;
				if (equal(column_name, "modified"))
					javaSwitchSelector12 = 2;
				if (equal(column_name, "date"))
					javaSwitchSelector12 = 3;
				if (equal(column_name, "title"))
					javaSwitchSelector12 = 4;
				if (equal(column_name, "comments"))
					javaSwitchSelector12 = 5;
				if (equal(column_name, "author"))
					javaSwitchSelector12 = 6;
				if (equal(column_name, "status"))
					javaSwitchSelector12 = 7;
				switch (javaSwitchSelector12) {
					case 1: {
						echo(gVars.webEnv, "\t\t<th scope=\"row\" class=\"check-column\"><input type=\"checkbox\" name=\"delete[]\" value=\"");
						getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
						echo(gVars.webEnv, "\" /></th>\n\t\t");
						break;
					}
					case 2: {
					}
					case 3: {
						if (equal("0000-00-00 00:00:00", StdClass.getValue(page, "post_date")) && equal("date", column_name)) {
							t_time = h_time = getIncluded(L10nPage.class, gVars, gConsts).__("Unpublished", "default");
						}
						else {
							if (equal("modified", column_name)) {
								t_time = getIncluded(General_templatePage.class, gVars, gConsts).get_the_modified_time(getIncluded(
								        L10nPage.class, gVars, gConsts).__("Y/m/d g:i:s A", "default"));
								m_time = strval(StdClass.getValue(page, "post_modified"));
								time = intval(getIncluded(General_templatePage.class, gVars, gConsts).get_post_modified_time("G", true));
							}
							else {
								t_time = getIncluded(General_templatePage.class, gVars, gConsts).get_the_time(getIncluded(L10nPage.class, gVars,
								        gConsts).__("Y/m/d g:i:s A", "default"));
								m_time = strval(StdClass.getValue(page, "post_date"));
								time = intval(getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("G", true));
							}
							if (Math.abs(DateTime.time() - time) < floatval(86400)) {
								if (equal("future", StdClass.getValue(page, "post_status"))) {
									h_time = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%s from now", "default"), getIncluded(
									        FormattingPage.class, gVars, gConsts).human_time_diff(time, intval("")));
								}
								else
									h_time = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%s ago", "default"), getIncluded(
									        FormattingPage.class, gVars, gConsts).human_time_diff(time, intval("")));
							}
							else {
								h_time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(getIncluded(L10nPage.class, gVars, gConsts).__(
								        "Y/m/d", "default"), m_time, true);
							}
						}
						echo(gVars.webEnv, "\t\t<td><abbr title=\"");
						echo(gVars.webEnv, t_time);
						echo(gVars.webEnv, "\">");
						echo(gVars.webEnv, h_time);
						echo(gVars.webEnv, "</abbr></td>\n\t\t");
						break;
					}
					case 4: {
						echo(gVars.webEnv, "\t\t<td><strong><a class=\"row-title\" href=\"page.php?action=edit&amp;post=");
						getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
						echo(gVars.webEnv, "\" title=\"");
						echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(QStrings.sprintf(getIncluded(
						        L10nPage.class, gVars, gConsts).__("Edit \"%s\"", "default"), title)));
						echo(gVars.webEnv, "\">");
						echo(gVars.webEnv, pad);
						echo(gVars.webEnv, title);
						echo(gVars.webEnv, "</a></strong>\n\t\t");
						if (equal("private", StdClass.getValue(page, "post_status"))) {
							getIncluded(L10nPage.class, gVars, gConsts)._e(" &#8212; <strong>Private</strong>", "default");
						}
						echo(gVars.webEnv, "</td>\n\t\t");
						break;
					}
					case 5: {
						echo(gVars.webEnv, "\t\t<td class=\"num\"><div class=\"post-com-count-wrapper\">\n\t\t");
						left = intval(getIncluded(CommentPage.class, gVars, gConsts).get_pending_comments_num(StdClass.getValue(page, "ID")));
						pending_phrase = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%s pending", "default"), Strings.number_format(left));
						if (booleanval(left)) {
							echo(gVars.webEnv, "<strong>");
						}
						getIncluded(Comment_templatePage.class, gVars, gConsts).comments_number("<a href=\'edit-pages.php?page_id=" + strval(id) + "\' title=\'"
						        + pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("0", "default")
						        + "</span></a>", "<a href=\'edit-pages.php?page_id=" + strval(id) + "\' title=\'" + pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>"
						        + getIncluded(L10nPage.class, gVars, gConsts).__("1", "default") + "</span></a>", "<a href=\'edit-pages.php?page_id=" + strval(id)
						        + "\' title=\'" + pending_phrase + "\' class=\'post-com-count\'><span class=\'comment-count\'>"
						        + getIncluded(L10nPage.class, gVars, gConsts).__("%", "default") + "</span></a>", "");
						if (booleanval(left)) {
							echo(gVars.webEnv, "</strong>");
						}
						echo(gVars.webEnv, "\t\t</div></td>\n\t\t");
						break;
					}
					case 6: {
						echo(gVars.webEnv, "\t\t<td><a href=\"edit-pages.php?author=");
						getIncluded(Author_templatePage.class, gVars, gConsts).the_author_ID();
						echo(gVars.webEnv, "\">");
						getIncluded(Author_templatePage.class, gVars, gConsts).the_author("", true);
						echo(gVars.webEnv, "</a></td>\n\t\t");
						break;
					}
					case 7: {
						echo(gVars.webEnv, "\t\t<td>\n\t\t<a href=\"");
						getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
						echo(gVars.webEnv, "\" title=\"");
						echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(QStrings.sprintf(getIncluded(
						        L10nPage.class, gVars, gConsts).__("View \"%s\"", "default"), title)));
						echo(gVars.webEnv, "\" rel=\"permalink\">\n\t\t");
						{
							int javaSwitchSelector13 = 0;
							if (equal(StdClass.getValue(page, "post_status"), "publish"))
								javaSwitchSelector13 = 1;
							if (equal(StdClass.getValue(page, "post_status"), "private"))
								javaSwitchSelector13 = 2;
							if (equal(StdClass.getValue(page, "post_status"), "future"))
								javaSwitchSelector13 = 3;
							if (equal(StdClass.getValue(page, "post_status"), "pending"))
								javaSwitchSelector13 = 4;
							if (equal(StdClass.getValue(page, "post_status"), "draft"))
								javaSwitchSelector13 = 5;
							switch (javaSwitchSelector13) {
								case 1: {
								}
								case 2: {
									getIncluded(L10nPage.class, gVars, gConsts)._e("Published", "default");
									break;
								}
								case 3: {
									getIncluded(L10nPage.class, gVars, gConsts)._e("Scheduled", "default");
									break;
								}
								case 4: {
									getIncluded(L10nPage.class, gVars, gConsts)._e("Pending Review", "default");
									break;
								}
								case 5: {
									getIncluded(L10nPage.class, gVars, gConsts)._e("Unpublished", "default");
									break;
								}
							}
						}
						echo(gVars.webEnv, "\t\t</a>\n\t\t</td>\n\t\t");
						break;
					}
					default: {
						echo(gVars.webEnv, "\t\t<td>");
						getIncluded(PluginPage.class, gVars, gConsts).do_action("manage_pages_custom_column", column_name, id);
						echo(gVars.webEnv, "</td>\n\t\t");
						break;
					}
				}
			}
		}
		echo(gVars.webEnv, "\n   </tr>\n\n");
		if (!booleanval(children_pages)) {
			return true;
		}
		for (i = 0; i < Array.count(children_pages); i++) {
			child = (StdClass) children_pages.getValue(i);
			if (equal(StdClass.getValue(child, "post_parent"), id)) {
				Array.array_splice(children_pages, i, 1);
				display_page_row(child, children_pages, level + 1);
				i = -1; //as numeric keys in $children_pages are not preserved after splice
			}
		}
		return false;
	}

	/** 
	 * displays pages in hierarchical order
	 */
	public boolean page_rows(Array<?> pages) {
		Array<Object> top_level_pages = new Array<Object>();
		Array<Object> children_pages = new Array<Object>();
		StdClass page = null;
		Array<Object> empty_array = new Array<Object>();
		StdClass orphan_page = null;
		
		if (!booleanval(pages)) {
			pages = getIncluded(PostPage.class, gVars, gConsts).get_pages("sort_column=menu_order");
		}
		
		if (!booleanval(pages)) {
			return false;
		}
		
		// splice pages into two parts: those without parent and those with parent
		
		top_level_pages = new Array<Object>();
		children_pages = new Array<Object>();
		for (Map.Entry javaEntry201 : pages.entrySet()) {
			page = (StdClass) javaEntry201.getValue();

			// catch and repair bad pages
			if (equal(StdClass.getValue(page, "post_parent"), StdClass.getValue(page, "ID"))) {
				page.fields.putValue("post_parent", 0);
				gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb./* By Numiton: bug fix */posts + " SET post_parent = \'0\' WHERE ID = %d", StdClass.getValue(page, "ID")));
				getIncluded(PostPage.class, gVars, gConsts).clean_page_cache(intval(StdClass.getValue(page, "ID")));
			}
			if (equal(0, StdClass.getValue(page, "post_parent"))) {
				top_level_pages.putValue(page);
			}
			else
				children_pages.putValue(page);
		}
		for (Map.Entry javaEntry202 : top_level_pages.entrySet()) {
			page = (StdClass) javaEntry202.getValue();
			display_page_row(page, children_pages, 0);
		}
		
		/*
		 * display the remaining children_pages which are orphans
		 * having orphan requires parental attention
		 */
		if (Array.count(children_pages) > 0) {
			empty_array = new Array<Object>();
			for (Map.Entry javaEntry203 : children_pages.entrySet()) {
				orphan_page = (StdClass) javaEntry203.getValue();
				getIncluded(PostPage.class, gVars, gConsts).clean_page_cache(intval(StdClass.getValue(orphan_page, "ID")));
				display_page_row(orphan_page, empty_array, 0);
			}
		}
		return false;
	}

	public Object user_row(Object user_objectObj, String /* Do not change type */style, String role) {
		WP_User current_user = null;
		String email = null;
		String url = null;
		String short_url = null;
		Object numposts = null;
		String edit = null;
		Object role_name = null;
		Object r = null;
		current_user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
		WP_User user_object;
		if (!(is_object(user_objectObj) && ClassHandling.is_a(user_objectObj, "WP_User"))) {
			user_object = new WP_User(gVars, gConsts, intval(user_objectObj));
		}
		else {
			user_object = (WP_User) user_objectObj;
		}
		email = user_object.getUser_email();
		url = user_object.getUser_url();
		short_url = Strings.str_replace("http://", "", url);
		short_url = Strings.str_replace("www.", "", short_url);
		if (equal("/", Strings.substr(short_url, -1))) {
			short_url = Strings.substr(short_url, 0, -1);
		}
		if (Strings.strlen(short_url) > 35) {
			short_url = Strings.substr(short_url, 0, 32) + "...";
		}
		numposts = getIncluded(UserPage.class, gVars, gConsts).get_usernumposts(user_object.getID());
		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_user", user_object.getID())) {
			if (equal(current_user.getID(), user_object.getID())) {
				edit = "profile.php";
			}
			else {
				edit = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts)
				        .add_query_arg("wp_http_referer", URL.urlencode(getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.stripslashes(gVars.webEnv,
				                gVars.webEnv.getRequestURI()), null, "display")), "user-edit.php?user_id=" + user_object.getID()), null, "display");
			}
			edit = "<a href=\"" + edit + "\">" + user_object.getUser_login() + "</a>";
		}
		else {
			edit = user_object.getUser_login();
		}
		role_name = getIncluded(L10nPage.class, gVars, gConsts).translate_with_context(strval(gVars.wp_roles.role_names.getValue(role)), "default");
		r = "<tr id=\'user-" + user_object.getID() + "\'" + style + ">\n\t\t<th scope=\'row\' class=\'check-column\'><input type=\'checkbox\' name=\'users[]\' id=\'user_" + user_object.getID()
		        + "\' class=\'" + role + "\' value=\'" + user_object.getID() + "\' /></th>\n\t\t<td><strong>" + edit + "</strong></td>\n\t\t<td>" + user_object.getFirst_name() + " "
		        + user_object.getLast_name() + "</td>\n\t\t<td><a href=\'mailto:" + email + "\' title=\'"
		        + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("e-mail: %s", "default"), email) + "\'>" + email + "</a></td>\n\t\t<td>" + strval(role_name)
		        + "</td>";
		r = strval(r) + "\n\t\t<td class=\'num\'>";
		if (intval(numposts) > 0) {
			r = strval(r) + "<a href=\'edit.php?author=" + user_object.getID() + "\' title=\'"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("View posts by this author", "default") + "\' class=\'edit\'>";
			r = strval(r) + strval(numposts);
			r = strval(r) + "</a>";
		}
		else {
			r = strval(r) + strval(0);
		}
		r = strval(r) + "</td>\n\t</tr>";
		return r;
	}

	public Array<Object> _wp_get_comment_list(String status, String s, Object start, int num) {
		String approved = null;
		Array<Object> comments = new Array<Object>();
		Object total = null;
		start = Math.abs(intval(start));
		num = num;
		if (equal("moderated", status)) {
			approved = "comment_approved = \'0\'";
		}
		else
			if (equal("approved", status)) {
				approved = "comment_approved = \'1\'";
			}
			else
				if (equal("spam", status)) {
					approved = "comment_approved = \'spam\'";
				}
				else
					approved = "( comment_approved = \'0\' OR comment_approved = \'1\' )";
		if (booleanval(s)) {
			s = gVars.wpdb.escape(s);
			comments = gVars.wpdb.get_results("SELECT SQL_CALC_FOUND_ROWS * FROM " + gVars.wpdb.comments + " WHERE\n\t\t\t(comment_author LIKE \'%" + s + "%\' OR\n\t\t\tcomment_author_email LIKE \'%"
			        + s + "%\' OR\n\t\t\tcomment_author_url LIKE (\'%" + s + "%\') OR\n\t\t\tcomment_author_IP LIKE (\'%" + s + "%\') OR\n\t\t\tcomment_content LIKE (\'%" + s + "%\') ) AND\n\t\t\t"
			        + approved + "\n\t\t\tORDER BY comment_date_gmt DESC LIMIT " + start + ", " + num);
		}
		else {
			comments = gVars.wpdb.get_results("SELECT SQL_CALC_FOUND_ROWS * FROM " + gVars.wpdb.comments + " USE INDEX (comment_date_gmt) WHERE " + approved + " ORDER BY comment_date_gmt DESC LIMIT "
			        + start + ", " + num);
		}
		(((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).update_comment_cache(comments);
		total = gVars.wpdb.get_var("SELECT FOUND_ROWS()");
		return new Array<Object>(new ArrayEntry<Object>(comments), new ArrayEntry<Object>(total));
	}

	public void _wp_comment_row(Object comment_id, String mode, String comment_status, boolean checkbox) {
		StdClass authordata;
		String the_comment_status = null;
		String _class = null;
		String post_link = null;
		String edit_link_start = null;
		String edit_link_end = null;
		String author_url = null;
		String author_url_display = null;
		String ptime = null;
		Object delete_url = null;
		Object approve_url = null;
		Object unapprove_url = null;
		Object spam_url = null;
		Array<Object> actions = new Array<Object>();
		Object action = null;
		Object link = null;
		gVars.comment = (StdClass) (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).get_comment(comment_id, gConsts.getOBJECT());
		gVars.post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(intval(StdClass.getValue(gVars.comment, "comment_post_ID")), gConsts.getOBJECT(), "raw");
		authordata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(gVars.post, "post_author")));
		the_comment_status = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_get_comment_status(intval(StdClass.getValue(gVars.comment, "comment_ID")));
		_class = (equal("unapproved", the_comment_status) ? "unapproved" : "");
		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
			post_link = "<a href=\'" + getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_link() + "\'>";
			post_link = post_link + getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.comment, "comment_post_ID")))
			        + "</a>";
			edit_link_start = "<a class=\'row-title\' href=\'comment.php?action=editcomment&amp;c=" + intval(StdClass.getValue(gVars.comment, "comment_ID")) + "\' title=\'"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Edit comment", "default") + "\'>";
			edit_link_end = "</a>";
		}
		else {
			post_link = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.comment, "comment_post_ID")));
			edit_link_start = edit_link_end = "";
		}
		author_url = getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_author_url();
		if (equal("http://", author_url)) {
			author_url = "";
		}
		author_url_display = author_url;
		if (Strings.strlen(author_url_display) > 50) {
			author_url_display = Strings.substr(author_url_display, 0, 49) + "...";
		}
		ptime = DateTime.date("G", QDateTime.strtotime(strval(StdClass.getValue(gVars.comment, "comment_date"))));
		if (Math.abs(DateTime.time() - intval(ptime)) < floatval(86400)) {
			ptime = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("%s ago", "default"), getIncluded(FormattingPage.class, gVars,
			        gConsts).human_time_diff(intval(ptime), intval("")));
		}
		else
			ptime = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(getIncluded(L10nPage.class, gVars, gConsts).__("Y/m/d \\a\\t g:i A",
			        "default"), strval(StdClass.getValue(gVars.comment, "comment_date")), true);
		delete_url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url(
		        "comment.php?action=deletecomment&p=" + intval(StdClass.getValue(gVars.comment, "comment_post_ID")) + "&c=" + intval(StdClass.getValue(gVars.comment, "comment_ID")), "delete-comment_"
		                + intval(StdClass.getValue(gVars.comment, "comment_ID"))), null, "display");
		approve_url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url(
		        "comment.php?action=approvecomment&p=" + intval(StdClass.getValue(gVars.comment, "comment_post_ID")) + "&c=" + intval(StdClass.getValue(gVars.comment, "comment_ID")), "approve-comment_"
		                + intval(StdClass.getValue(gVars.comment, "comment_ID"))), null, "display");
		unapprove_url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts)
		        .wp_nonce_url("comment.php?action=unapprovecomment&p=" + intval(StdClass.getValue(gVars.comment, "comment_post_ID")) + "&c=" + intval(StdClass.getValue(gVars.comment, "comment_ID")),
		                "unapprove-comment_" + intval(StdClass.getValue(gVars.comment, "comment_ID"))), null, "display");
		spam_url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url(
		        "comment.php?action=deletecomment&dt=spam&p=" + intval(StdClass.getValue(gVars.comment, "comment_post_ID")) + "&c=" + intval(StdClass.getValue(gVars.comment, "comment_ID")),
		        "delete-comment_" + intval(StdClass.getValue(gVars.comment, "comment_ID"))), null, "display");
		echo(gVars.webEnv, "  <tr id=\"comment-");
		echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_ID")));
		echo(gVars.webEnv, "\" class=\'");
		echo(gVars.webEnv, _class);
		echo(gVars.webEnv, "\'>\n");
		if (checkbox) {
			echo(gVars.webEnv, "    <td class=\"check-column\">");
			if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
				echo(gVars.webEnv, "<input type=\"checkbox\" name=\"delete_comments[]\" value=\"");
				echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_ID")));
				echo(gVars.webEnv, "\" />");
			} //current_user_can
			echo(gVars.webEnv, "</td>\n");
		}
		else {
		}
		echo(gVars.webEnv, "    <td class=\"comment\">\n    <p class=\"comment-author\"><strong>");
		echo(gVars.webEnv, edit_link_start);
		getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author();
		echo(gVars.webEnv, edit_link_end);
		echo(gVars.webEnv, "</strong><br />\n    ");
		if (!empty(author_url)) {
			echo(gVars.webEnv, "    <a href=\"");
			echo(gVars.webEnv, author_url);
			echo(gVars.webEnv, "\">");
			echo(gVars.webEnv, author_url_display);
			echo(gVars.webEnv, "</a> |\n    ");
		}
		else {
		}
		echo(gVars.webEnv, "    ");
		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
			echo(gVars.webEnv, "    ");
			if (!empty(StdClass.getValue(gVars.comment, "comment_author_email"))) {
				echo(gVars.webEnv, "    ");
				getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_email_link("", "", "");
				echo(gVars.webEnv, " |\n    ");
			}
			else {
			}
			echo(gVars.webEnv, "    <a href=\"edit-comments.php?s=");
			getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_IP();
			echo(gVars.webEnv, "&amp;mode=detail\">");
			getIncluded(Comment_templatePage.class, gVars, gConsts).comment_author_IP();
			echo(gVars.webEnv, "</a>\n\t");
		}
		else {
		}
		echo(gVars.webEnv, "    \n    </p>\n   \t");
		if (equal("detail", mode)) {
			getIncluded(Comment_templatePage.class, gVars, gConsts).comment_text();
		}
		echo(gVars.webEnv, "   \t<p>");
		QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("From %1$s, %2$s", "default"), post_link, ptime);
		echo(gVars.webEnv, "</p>\n    </td>\n    <td>");
		getIncluded(Comment_templatePage.class, gVars, gConsts)
		        .comment_date(getIncluded(L10nPage.class, gVars, gConsts).__("Y/m/d", "default"));
		echo(gVars.webEnv, "</td>\n    <td class=\"action-links\">\n");
		
		actions = new Array<Object>();
		
		actions.putValue("approve", "<a href=\'" + strval(approve_url) + "\' class=\'dim:the-comment-list:comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID"))
		        + ":unapproved:e7e7d3:e7e7d3\' title=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Approve this comment", "default") + "\'>"
		        + getIncluded(L10nPage.class, gVars, gConsts).__("Approve", "default") + "</a> | ");
		actions.putValue("unapprove", "<a href=\'" + strval(unapprove_url) + "\' class=\'dim:the-comment-list:comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID"))
		        + ":unapproved:e7e7d3:e7e7d3\' title=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Unapprove this comment", "default") + "\'>"
		        + getIncluded(L10nPage.class, gVars, gConsts).__("Unapprove", "default") + "</a> | ");
		
		// we're looking at list of only approved or only unapproved comments
		if (equal("moderated", comment_status)) {
			actions.putValue("approve", "<a href=\'" + strval(approve_url) + "\' class=\'delete:the-comment-list:comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID"))
			        + ":e7e7d3:action=dim-comment\' title=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Approve this comment", "default") + "\'>"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Approve", "default") + "</a> | ");
			actions.arrayUnset("unapprove");
		}
		else
			if (equal("approved", comment_status)) {
				actions.putValue("unapprove", "<a href=\'" + strval(unapprove_url) + "\' class=\'delete:the-comment-list:comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID"))
				        + ":e7e7d3:action=dim-comment\' title=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Unapprove this comment", "default") + "\'>"
				        + getIncluded(L10nPage.class, gVars, gConsts).__("Unapprove", "default") + "</a> | ");
				actions.arrayUnset("approve");
			}
		if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
			actions.putValue("spam", "<a href=\'" + strval(spam_url) + "\' class=\'delete:the-comment-list:comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID")) + "::spam=1\' title=\'"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Mark this comment as spam", "default") + "\'>"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Spam", "default") + "</a> | ");
			actions.putValue("delete", "<a href=\'" + strval(delete_url) + "\' class=\'delete:the-comment-list:comment-" + intval(StdClass.getValue(gVars.comment, "comment_ID")) + " delete\'>"
			        + getIncluded(L10nPage.class, gVars, gConsts).__("Delete", "default") + "</a>");
			for (Map.Entry javaEntry204 : actions.entrySet()) {
				action = javaEntry204.getKey();
				link = javaEntry204.getValue();
				echo(gVars.webEnv, "<span class=\'" + strval(action) + "\'>" + strval(link) + "</span>");
			}
		}
		echo(gVars.webEnv, "\t</td>\n  </tr>\n\t");
	}

	public void wp_dropdown_cats(Object currentcat, Object currentparent, Object parent, int level, Array<Object> categories) {
		StdClass category = null;
		String pad = null;
		if (!booleanval(categories)) {
			categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("hide_empty=0");
		}
		if (booleanval(categories)) {
			for (Map.Entry javaEntry205 : categories.entrySet()) {
				category = (StdClass) javaEntry205.getValue();
				if (!equal(currentcat, StdClass.getValue(category, "term_id")) && equal(parent, StdClass.getValue(category, "parent"))) {
					pad = Strings.str_repeat("&#8211; ", level);
					category.fields
					        .putValue("name", getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(category, "name")), strval(0)));
					echo(gVars.webEnv, "\n\t<option value=\'" + StdClass.getValue(category, "term_id") + "\'");
					if (equal(currentparent, StdClass.getValue(category, "term_id"))) {
						echo(gVars.webEnv, " selected=\'selected\'");
					}
					echo(gVars.webEnv, ">" + pad + StdClass.getValue(category, "name") + "</option>");
					wp_dropdown_cats(currentcat, currentparent, StdClass.getValue(category, "term_id"), level + 1, categories);
				}
			}
		}
		else {
			return;
		}
	}

	public/*false*/void list_meta(Array<Object> meta) {
		Ref<Integer> count = new Ref<Integer>();
		Array<Object> entry = null;
		
		// Exit if no meta
		if (!booleanval(meta)) {
			echo(gVars.webEnv, "<tbody id=\"the-list\" class=\"list:meta\"><tr style=\"display: none;\"><td>&nbsp;</td></tr></tbody>"); //TBODY needed for list-manipulation JS
			return;
		}
		count.value = 0;
		echo(gVars.webEnv, "\t<thead>\n\t<tr>\n\t\t<th>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Key", "default");
		echo(gVars.webEnv, "</th>\n\t\t<th>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Value", "default");
		echo(gVars.webEnv, "</th>\n\t\t<th colspan=\'2\'>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Action", "default");
		echo(gVars.webEnv, "</th>\n\t</tr>\n\t</thead>\n\t<tbody id=\'the-list\' class=\'list:meta\'>\n");
		for (Map.Entry javaEntry206 : meta.entrySet()) {
			entry = (Array<Object>) javaEntry206.getValue();
			echo(gVars.webEnv, _list_meta_row(entry, count));
		}
		echo(gVars.webEnv, "\n\t</tbody>");
	}

	public Object _list_meta_row(Array<Object> entry, Ref<Integer> count) {
		Object r = null;
		String style = null;
		String delete_nonce = null;
		if (!_list_meta_row_update_nonce) {
			_list_meta_row_update_nonce = booleanval(getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("add-meta"));
		}
		r = "";
		count.value = count.value + 1; // By Numiton: ++count.value generates a javac error with JDK 1.5 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6348760)
		if (booleanval(count.value % 2)) {
			style = "alternate";
		}
		else
			style = "";
		if (equal("_", Strings.getCharAt(strval(entry.getValue("meta_key")), 0))) {
			style = style + " hidden";
		}
		if (getIncluded(FunctionsPage.class, gVars, gConsts).is_serialized(entry.getValue("meta_value"))) {
			if (getIncluded(FunctionsPage.class, gVars, gConsts).is_serialized_string(strval(entry.getValue("meta_value")))) {
				// this is a serialized string, so we should display it
				entry.putValue("meta_value", getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(entry.getValue("meta_value")));
			}
			else {
				// this is a serialized array/object so we should NOT display it
				count.value = count.value - 1; // By Numiton: --count.value generates a javac error with JDK 1.5 (http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6348760)
				return null;
			}
		}
		entry.putValue("meta_key", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(entry.getValue("meta_key"))));
		entry.putValue("meta_value", Strings.htmlspecialchars(strval(entry.getValue("meta_value")))); // using a <textarea />
		entry.putValue("meta_id", intval(entry.getValue("meta_id")));
		
		delete_nonce = getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("delete-meta_" + strval(entry.getValue("meta_id")));
		r = strval(r) + "\n\t<tr id=\'meta-" + strval(entry.getValue("meta_id")) + "\' class=\'" + style + "\'>";
		r = strval(r) + "\n\t\t<td valign=\'top\'><input name=\'meta[" + strval(entry.getValue("meta_id")) + "][key]\' tabindex=\'6\' type=\'text\' size=\'20\' value=\'"
		        + strval(entry.getValue("meta_key")) + "\' /></td>";
		r = strval(r) + "\n\t\t<td><textarea name=\'meta[" + strval(entry.getValue("meta_id")) + "][value]\' tabindex=\'6\' rows=\'2\' cols=\'30\'>" + strval(entry.getValue("meta_value"))
		        + "</textarea></td>";
		r = strval(r)
		        + "\n\t\t<td style=\'text-align: center;\'><input name=\'updatemeta\' type=\'submit\' tabindex=\'6\' value=\'"
		        + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Update",
		                "default")) + "\' class=\'add:the-list:meta-" + strval(entry.getValue("meta_id")) + "::_ajax_nonce=" + strval(_list_meta_row_update_nonce) + " updatemeta\' /><br />";
		r = strval(r) + "\n\t\t<input name=\'deletemeta[" + strval(entry.getValue("meta_id")) + "]\' type=\'submit\' ";
		r = strval(r)
		        + "class=\'delete:the-list:meta-"
		        + strval(entry.getValue("meta_id"))
		        + "::_ajax_nonce="
		        + delete_nonce
		        + " deletemeta\' tabindex=\'6\' value=\'"
		        + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Delete",
		                "default")) + "\' />";
		r = strval(r) + getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("change-meta", "_ajax_nonce", false, false);
		r = strval(r) + "</td>\n\t</tr>";
		return r;
	}

	public void meta_form() {
		int limit = 0;
		Ref<Array<Object>> keys = new Ref(new Array<Object>());
		String key = null;
		limit = intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("postmeta_form_limit", 30));
		keys.value = gVars.wpdb.get_col("\n\t\tSELECT meta_key\n\t\tFROM " + gVars.wpdb.postmeta + "\n\t\tWHERE meta_key NOT LIKE \'\\_%\'\n\t\tGROUP BY meta_key\n\t\tORDER BY meta_id DESC\n\t\tLIMIT "
		        + limit);
		if (booleanval(keys)) {
			QArray.natcasesort(keys);
		}
		echo(gVars.webEnv, "<p><strong>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Add a new custom field:", "default");
		echo(gVars.webEnv, "</strong></p>\n<table id=\"newmeta\" cellspacing=\"3\" cellpadding=\"3\">\n\t<tr>\n<th colspan=\"2\">");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Key", "default");
		echo(gVars.webEnv, "</th>\n<th>");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Value", "default");
		echo(gVars.webEnv, "</th>\n</tr>\n\t<tr valign=\"top\">\n\t\t<td style=\"width: 18%;\" class=\"textright\">\n");
		if (booleanval(keys)) {
			echo(gVars.webEnv, "<select id=\"metakeyselect\" name=\"metakeyselect\" tabindex=\"7\">\n<option value=\"#NONE#\">");
			getIncluded(L10nPage.class, gVars, gConsts)._e("- Select -", "default");
			echo(gVars.webEnv, "</option>\n");
			for (Map.Entry javaEntry207 : keys.value.entrySet()) {
				key = strval(javaEntry207.getValue());
				key = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(key);
				echo(gVars.webEnv, "\n\t<option value=\'" + key + "\'>" + key + "</option>");
			}
			echo(gVars.webEnv, "</select> ");
			getIncluded(L10nPage.class, gVars, gConsts)._e("or", "default");
		}
		else {
		}
		echo(
		        gVars.webEnv,
		        "</td>\n<td><input type=\"text\" id=\"metakeyinput\" name=\"metakeyinput\" tabindex=\"7\" /></td>\n\t\t<td><textarea id=\"metavalue\" name=\"metavalue\" rows=\"3\" cols=\"25\" tabindex=\"8\"></textarea></td>\n\t</tr>\n<tr class=\"submit\"><td colspan=\"3\">\n\t");
		getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-meta", "_ajax_nonce", false, true);
		echo(gVars.webEnv, "\t<input type=\"submit\" id=\"addmetasub\" name=\"addmeta\" class=\"add:the-list:newmeta::post_id=");
		echo(gVars.webEnv, booleanval(gVars.post_ID) ? intval(gVars.post_ID) : gVars.temp_ID);
		echo(gVars.webEnv, "\" tabindex=\"9\" value=\"");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Add Custom Field", "default");
		echo(gVars.webEnv, "\" />\n</td></tr>\n</table>\n");
	}

	public void touch_time(boolean edit, int for_post, int tab_index) {
		String tab_index_attribute = null;
		int time_adj;
		String post_date = null;
		String ss = null;
		String month = null;
		int i = 0;
		String day = null;
		String year = null;
		String hour = null;
		String minute = null;
		
		if (booleanval(for_post)) {
			edit = ((Array.in_array(StdClass.getValue(gVars.post, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending"))) && (!booleanval(StdClass.getValue(gVars.post, "post_date")) || equal("0000-00-00 00:00:00", StdClass.getValue(gVars.post, "post_date")))) ? false : true);
		}
		
		tab_index_attribute = "";
		if (tab_index > 0) {
			tab_index_attribute = " tabindex=\"" + strval(tab_index) + "\"";
		}
		
		// echo '<label for="timestamp" style="display: block;"><input type="checkbox" class="checkbox" name="edit_date" value="1" id="timestamp"'.$tab_index_attribute.' /> '.__( 'Edit timestamp' ).'</label><br />';
		
		time_adj = intval(DateTime.time() + floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600);
		post_date = strval(booleanval(for_post) ? StdClass.getValue(gVars.post, "post_date") : StdClass.getValue(gVars.comment, "comment_date"));
		final String jj = (edit ? getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("d", post_date, true) : DateTime.gmdate("d", time_adj));
		final String mm = (edit ? getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("m", post_date, true) : DateTime.gmdate("m", time_adj));
		final String aa = (edit ? getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y", post_date, true) : DateTime.gmdate("Y", time_adj));
		final String hh = (edit ? getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("H", post_date, true) : DateTime.gmdate("H", time_adj));
		final String mn = (edit ? getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("i", post_date, true) : DateTime.gmdate("i", time_adj));
		ss = (edit ? getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("s", post_date, true) : DateTime.gmdate("s", time_adj));
		month = "<select id=\"mm\" name=\"mm\"" + tab_index_attribute + ">\n";
		for (i = 1; i < 13; i = i + 1) {
			month = month + "\t\t\t" + "<option value=\"" + getIncluded(FormattingPage.class, gVars, gConsts).zeroise(i, 2) + "\"";
			if (equal(i, mm)) {
				month = month + " selected=\"selected\"";
			}
			month = month + ">" + gVars.wp_locale.get_month(i) + "</option>\n";
		}
		month = month + "</select>";
		day = "<input type=\"text\" id=\"jj\" name=\"jj\" value=\"" + jj + "\" size=\"2\" maxlength=\"2\"" + tab_index_attribute + " autocomplete=\"off\"  />";
		year = "<input type=\"text\" id=\"aa\" name=\"aa\" value=\"" + aa + "\" size=\"4\" maxlength=\"5\"" + tab_index_attribute + " autocomplete=\"off\"  />";
		hour = "<input type=\"text\" id=\"hh\" name=\"hh\" value=\"" + hh + "\" size=\"2\" maxlength=\"2\"" + tab_index_attribute + " autocomplete=\"off\"  />";
		minute = "<input type=\"text\" id=\"mn\" name=\"mn\" value=\"" + mn + "\" size=\"2\" maxlength=\"2\"" + tab_index_attribute + " autocomplete=\"off\"  />";
		QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts)._c(
		        "%1$s%2$s, %3$s <br />@ %4$s : %5$s|1: month input, 2: day input, 3: year input, 4: hour input, 5: minute input", "default"), month, day, year, hour, minute);
		echo(gVars.webEnv, "\n\n");
		for (Map.Entry javaEntry208 : new Array<Object>(new ArrayEntry<Object>("mm"), new ArrayEntry<Object>("jj"), new ArrayEntry<Object>("aa"), new ArrayEntry<Object>("hh"),
		        new ArrayEntry<Object>("mn")).entrySet()) {
			final String timeunit = strval(javaEntry208.getValue());
			echo(gVars.webEnv, "<input type=\"hidden\" id=\"hidden_" + timeunit + "\" name=\"hidden_" + timeunit + "\" value=\"" + new DynamicConstructEvaluator() {
				public Object evaluate() {
					if (equal(timeunit, "mm")) {
						return mm;
					}
					if (equal(timeunit, "jj")) {
						return jj;
					}
					if (equal(timeunit, "aa")) {
						return aa;
					}
					if (equal(timeunit, "hh")) {
						return hh;
					}
					if (equal(timeunit, "mn")) {
						return mn;
					}
					return null;
				}
			}.evaluate() + "\" />" + "\n");
		}
		echo(gVars.webEnv, "\n<input type=\"hidden\" id=\"ss\" name=\"ss\" value=\"");
		echo(gVars.webEnv, ss);
		echo(gVars.webEnv, "\" size=\"2\" maxlength=\"2\" />\n");
	}

	public void page_template_dropdown(Object _default) {
		Array<Object> templates = new Array<Object>();
		Object template = null;
		String selected = null;
		templates = getIncluded(ThemePage.class, gVars, gConsts).get_page_templates();
		Array.ksort(templates);
		for (Map.Entry javaEntry209 : Array.array_keys(templates).entrySet()) {
			template = javaEntry209.getValue();
			if (equal(_default, templates.getValue(template))) {
				selected = " selected=\'selected\'";
			}
			else
				selected = "";
			echo(gVars.webEnv, "\n\t<option value=\'" + strval(templates.getValue(template)) + "\' " + selected + ">" + strval(template) + "</option>");
		}
	}

	public boolean parent_dropdown(Object _default, int parent, int level) {
		Array<Object> items = new Array<Object>();
		StdClass item = null;
		String pad = null;
		String current = null;
		items = gVars.wpdb.get_results("SELECT ID, post_parent, post_title FROM " + gVars.wpdb.posts + " WHERE post_parent = " + parent + " AND post_type = \'page\' ORDER BY menu_order");
		if (booleanval(items)) {
			for (Map.Entry javaEntry210 : items.entrySet()) {
				item = (StdClass) javaEntry210.getValue();
				
				// A page cannot be its own parent.
				if (!empty(gVars.post_ID)) {
					if (equal(StdClass.getValue(item, "ID"), gVars.post_ID)) {
						continue;
					}
				}
				pad = Strings.str_repeat("&nbsp;", level * 3);
				if (equal(StdClass.getValue(item, "ID"), _default)) {
					current = " selected=\"selected\"";
				}
				else
					current = "";
				echo(gVars.webEnv, "\n\t<option value=\'" + StdClass.getValue(item, "ID") + "\'" + current + ">" + pad + " "
				        + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(item, "post_title")), strval(0)) + "</option>");
				parent_dropdown(_default, intval(StdClass.getValue(item, "ID")), level + 1);
			}
		}
		else {
			return false;
		}
		return false;
	}

	public void browse_happy() {
		Object getit = null;
		getit = getIncluded(L10nPage.class, gVars, gConsts).__("nWordPress recommends a better browser", "default");
		echo(gVars.webEnv, "\n\t\t<span id=\"bh\" class=\"alignright\"><a href=\"http://browsehappy.com/\" title=\"" + strval(getit)
		        + "\"><img src=\"images/browse-happy.gif\" alt=\"Browse Happy\" /></a></span>\n\t\t");
	}

	public boolean the_attachment_links(int id) {
		StdClass post;
		String icon;
		Array<Object> attachment_data = new Array<Object>();
		boolean thumb = false;
		id = id;
		post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");
		if (!equal(StdClass.getValue(post, "post_type"), "attachment")) {
			return false;
		}
		icon = getIncluded(Post_templatePage.class, gVars, gConsts).get_attachment_icon(intval(StdClass.getValue(post, "ID")), false, new Array<Object>());
		attachment_data = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_metadata(id, false);
		thumb = isset(attachment_data.getValue("thumb"));
		echo(gVars.webEnv, "<form id=\"the-attachment-links\">\n<table>\n\t<col />\n\t<col class=\"widefat\" />\n\t<tr>\n\t\t<th scope=\"row\">");
		getIncluded(L10nPage.class, gVars, gConsts)._e("URL", "default");
		echo(gVars.webEnv, "</th>\n\t\t<td><textarea rows=\"1\" cols=\"40\" type=\"text\" class=\"attachmentlinks\" readonly=\"readonly\">");
		echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(0));
		echo(gVars.webEnv, "</textarea></td>\n\t</tr>\n");
		if (booleanval(icon)) {
			echo(gVars.webEnv, "\t<tr>\n\t\t<th scope=\"row\">");
			if (thumb) {
				getIncluded(L10nPage.class, gVars, gConsts)._e("Thumbnail linked to file", "default");
			}
			else {
				getIncluded(L10nPage.class, gVars, gConsts)._e("Image linked to file", "default");
			}
			echo(gVars.webEnv, "</th>\n\t\t<td><textarea rows=\"1\" cols=\"40\" type=\"text\" class=\"attachmentlinks\" readonly=\"readonly\"><a href=\"");
			echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(0));
			echo(gVars.webEnv, "\">");
			echo(gVars.webEnv, icon);
			echo(gVars.webEnv, "</a></textarea></td>\n\t</tr>\n\t<tr>\n\t\t<th scope=\"row\">");
			if (thumb) {
				getIncluded(L10nPage.class, gVars, gConsts)._e("Thumbnail linked to page", "default");
			}
			else {
				getIncluded(L10nPage.class, gVars, gConsts)._e("Image linked to page", "default");
			}
			echo(gVars.webEnv, "</th>\n\t\t<td><textarea rows=\"1\" cols=\"40\" type=\"text\" class=\"attachmentlinks\" readonly=\"readonly\"><a href=\"");
			echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(intval(StdClass.getValue(post, "ID"))));
			echo(gVars.webEnv, "\" rel=\"attachment wp-att-");
			echo(gVars.webEnv, StdClass.getValue(post, "ID"));
			echo(gVars.webEnv, "\">");
			echo(gVars.webEnv, icon);
			echo(gVars.webEnv, "</a></textarea></td>\n\t</tr>\n");
		}
		else {
			echo(gVars.webEnv, "\t<tr>\n\t\t<th scope=\"row\">");
			getIncluded(L10nPage.class, gVars, gConsts)._e("Link to file", "default");
			echo(gVars.webEnv, "</th>\n\t\t<td><textarea rows=\"1\" cols=\"40\" type=\"text\" class=\"attachmentlinks\" readonly=\"readonly\"><a href=\"");
			echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(0));
			echo(gVars.webEnv, "\" class=\"attachmentlink\">");
			echo(gVars.webEnv, FileSystemOrSocket.basename(getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(0)));
			echo(gVars.webEnv, "</a></textarea></td>\n\t</tr>\n\t<tr>\n\t\t<th scope=\"row\">");
			getIncluded(L10nPage.class, gVars, gConsts)._e("Link to page", "default");
			echo(gVars.webEnv, "</th>\n\t\t<td><textarea rows=\"1\" cols=\"40\" type=\"text\" class=\"attachmentlinks\" readonly=\"readonly\"><a href=\"");
			echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(intval(StdClass.getValue(post, "ID"))));
			echo(gVars.webEnv, "\" rel=\"attachment wp-att-");
			echo(gVars.webEnv, StdClass.getValue(post, "ID"));
			echo(gVars.webEnv, "\">");
			getIncluded(Post_templatePage.class, gVars, gConsts).the_title("", "", true);
			echo(gVars.webEnv, "</a></textarea></td>\n\t</tr>\n");
		}
		echo(gVars.webEnv, "</table>\n</form>\n");
		return false;
	}

	public void wp_dropdown_roles(Object _default) {
		Object r = null;
		String name = null;
		Object role = null;
		Object p = null;
		r = "";
		for (Map.Entry javaEntry211 : gVars.wp_roles.role_names.entrySet()) {
			role = javaEntry211.getKey();
			
			name = strval(javaEntry211.getValue());
			name = getIncluded(L10nPage.class, gVars, gConsts).translate_with_context(name, "default");
			
			if (equal(_default, role)) { // Make default first in list
				p = "\n\t<option selected=\'selected\' value=\'" + strval(role) + "\'>" + name + "</option>";
			}
			else
				r = strval(r) + "\n\t<option value=\'" + strval(role) + "\'>" + name + "</option>";
		}
		echo(gVars.webEnv, strval(p) + strval(r));
	}

	public int wp_convert_hr_to_bytes(String size) {
		int bytes = 0;
		size = Strings.strtolower(size);
		bytes = intval(size);
		if (!strictEqual(Strings.strpos(size, "k"), BOOLEAN_FALSE)) {
			bytes = intval(size) * 1024;
		}
		else
			if (!strictEqual(Strings.strpos(size, "m"), BOOLEAN_FALSE)) {
				bytes = intval(size) * 1024 * 1024;
			}
			else
				if (!strictEqual(Strings.strpos(size, "g"), BOOLEAN_FALSE)) {
					bytes = intval(size) * 1024 * 1024 * 1024;
				}
		return bytes;
	}

	public String wp_convert_bytes_to_hr(int bytes) {
		Array<Object> units = new Array<Object>();
		float log = 0;
		int power = 0;
		int size = 0;
		units = new Array<Object>(new ArrayEntry<Object>(0, "B"), new ArrayEntry<Object>(1, "kB"), new ArrayEntry<Object>(2, "MB"), new ArrayEntry<Object>(3, "GB"));
		log = Math.log(bytes, 1024);
		power = intval(log);
		size = intval(Math.pow(1024, log - power));
		return strval(size) + strval(units.getValue(power));
	}

	public Object wp_max_upload_size() {
		int u_bytes = 0;
		int p_bytes = 0;
		Object bytes = null;
		u_bytes = wp_convert_hr_to_bytes(Options.ini_get(gVars.webEnv, "upload_max_filesize"));
		p_bytes = wp_convert_hr_to_bytes(Options.ini_get(gVars.webEnv, "post_max_size"));
		bytes = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("upload_size_limit", Math.min(u_bytes, p_bytes), u_bytes, p_bytes);
		return bytes;
	}

	public void wp_import_upload_form(String action) {
		int bytes;
		String size = null;
		bytes = intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("import_upload_size_limit", wp_max_upload_size()));
		size = wp_convert_bytes_to_hr(bytes);
		echo(gVars.webEnv, "<form enctype=\"multipart/form-data\" id=\"import-upload-form\" method=\"post\" action=\"");
		echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(action));
		echo(gVars.webEnv, "\">\n<p>\n");
		getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-upload", "_wpnonce", true, true);
		echo(gVars.webEnv, "<label for=\"upload\">");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Choose a file from your computer:", "default");
		echo(gVars.webEnv, "</label> (");
		QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Maximum size: %s", "default"), size);
		echo(gVars.webEnv,
		        ")\n<input type=\"file\" id=\"upload\" name=\"import\" size=\"25\" />\n<input type=\"hidden\" name=\"action\" value=\"save\" />\n<input type=\"hidden\" name=\"max_file_size\" value=\"");
		echo(gVars.webEnv, bytes);
		echo(gVars.webEnv, "\" />\n</p>\n<p class=\"submit\">\n<input type=\"submit\" class=\"button\" value=\"");
		getIncluded(L10nPage.class, gVars, gConsts)._e("Upload file and import", "default");
		echo(gVars.webEnv, "\" />\n</p>\n</form>\n");
	}

	public void wp_remember_old_slug() {
		String name = null;
		name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_name"))); // just in case
		if (booleanval(Strings.strlen(name))) {
			echo(gVars.webEnv, "<input type=\"hidden\" id=\"wp-old-slug\" name=\"wp-old-slug\" value=\"" + name + "\" />");
		}
	}

	/**
	 * add_meta_box() - Add a meta box to an edit form
	 *
	 * @since 2.5
	 *
	 * @param string $id String for use in the 'id' attribute of tags.
	 * @param string $title Title of the meta box
	 * @param string $callback Function that fills the box with the desired content.  The function should echo its output.
	 * @param string $page The type of edit page on which to show the box (post, page, link)
	 * @param string $context The context within the page where the boxes should show ('normal', 'advanced')
	 */
	public void add_meta_box(Object id, Object title, Array<Object> callback, Object page, Object context) {
		if (!isset(wp_meta_boxes)) {
			wp_meta_boxes = new Array<Object>();
		}
		if (!isset(wp_meta_boxes.getValue(page))) {
			wp_meta_boxes.putValue(page, new Array<Object>());
		}
		if (!isset(wp_meta_boxes.getArrayValue(page).getValue(context))) {
			wp_meta_boxes.getArrayValue(page).putValue(context, new Array<Object>());
		}
		wp_meta_boxes.getArrayValue(page).getArrayValue(context).putValue(id,
		        new Array<Object>(new ArrayEntry<Object>("id", id), new ArrayEntry<Object>("title", title), new ArrayEntry<Object>("callback", callback)));
	}

	public void do_meta_boxes(String page, String context, Object object) {
		Array<Object> box = new Array<Object>();
		if (!isset(wp_meta_boxes) || !isset(wp_meta_boxes.getValue(page)) || !isset(wp_meta_boxes.getArrayValue(page).getValue(context))) {
			return;
		}
		for (Map.Entry javaEntry212 : new Array<Object>(wp_meta_boxes.getArrayValue(page).getValue(context)).entrySet()) {
			box = (Array<Object>) javaEntry212.getValue();
			echo(gVars.webEnv, "<div id=\"" + strval(box.getValue("id")) + "\" class=\"postbox "
			        + (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes(strval(box.getValue("id")), page) + "\">"
			        + "\n");
			echo(gVars.webEnv, "<h3>" + strval(box.getValue("title")) + "</h3>\n");
			echo(gVars.webEnv, "<div class=\"inside\">" + "\n");
			FunctionHandling.call_user_func(new Callback(box.getArrayValue("callback")), object, box);
			echo(gVars.webEnv, "</div>\n");
			echo(gVars.webEnv, "</div>\n");
		}
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_admin_includes_template_block1");
		gVars.webEnv = webEnv;
		if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "MSIE"), BOOLEAN_FALSE)) {
			getIncluded(PluginPage.class, gVars, gConsts).add_action("in_admin_footer", Callback.createCallbackArray(this, "browse_happy"), 10, 1);
		}
		return DEFAULT_VAL;
	}
	public Array<Object>	wp_meta_boxes	= new Array<Object>();
}
