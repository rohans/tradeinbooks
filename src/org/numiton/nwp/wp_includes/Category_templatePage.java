/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Category_templatePage.java,v 1.5 2008/10/14 14:23:04 numiton Exp $
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
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.FunctionHandling;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Category_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Category_templatePage.class.getName());

    @Override
    @RequestMapping("/wp-includes/category-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/category_template";
    }

    public Object get_category_children(Object id, Object before, Object after) {
        Object chain = null;
        Array<Object> cat_ids = null;
        Object cat_id = null;
        Object category = null;

        if (equal(0, id)) {
            return "";
        }

        chain = "";
        // TODO: consult hierarchy
        cat_ids = (Array<Object>) getIncluded(CategoryPage.class, gVars, gConsts).get_all_category_ids();

        for (Map.Entry javaEntry391 : cat_ids.entrySet()) {
            cat_id = javaEntry391.getValue();

            if (equal(cat_id, id)) {
                continue;
            }

            category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat_id, gConsts.getOBJECT(), "raw");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(category)) {
                return category;
            }

            if (equal(((StdClass) category).fields.getValue("parent"), id)) {
                chain = strval(chain) + strval(before) + ((StdClass) category).fields.getValue("term_id") + strval(after);
                chain = strval(chain) + get_category_children(((StdClass) category).fields.getValue("term_id"), before, after);
            }
        }

        return chain;
    }

    public Object get_category_link(Object category_id) {
        String catlink = null;
        String file = null;
        Object category;
        String category_nicename = null;
        Object parent = null;
        catlink = gVars.wp_rewrite.get_category_permastruct();

        if (empty(catlink)) {
            file = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/";
            catlink = file + "?cat=" + strval(category_id);
        } else {
            category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(category_id, gConsts.getOBJECT(), "raw");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(category)) {
                return category;
            }

            category_nicename = strval(((StdClass) category).fields.getValue("slug"));

            if (booleanval(parent = ((StdClass) category).fields.getValue("parent"))) {
                category_nicename = get_category_parents(parent, false, "/", true) + category_nicename;
            }

            catlink = Strings.str_replace("%category%", category_nicename, catlink);
            catlink = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(catlink, "category");
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("category_link", catlink, category_id));
    }

    public Object get_category_parents(Object id, boolean link, String separator, boolean nicename) {
        String chain = null;
        Object parent = null;
        String name = null;
        chain = "";
        parent = getIncluded(CategoryPage.class, gVars, gConsts).get_category(id, gConsts.getOBJECT(), "raw");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(parent)) {
            return parent;
        }

        if (nicename) {
            name = strval(((StdClass) parent).fields.getValue("slug"));
        } else {
            name = strval(((StdClass) parent).fields.getValue("cat_name"));
        }

        if (booleanval(((StdClass) parent).fields.getValue("parent")) && !equal(((StdClass) parent).fields.getValue("parent"), ((StdClass) parent).fields.getValue("term_id"))) {
            chain = chain + get_category_parents(((StdClass) parent).fields.getValue("parent"), link, separator, nicename);
        }

        if (link) {
            chain = chain + "<a href=\"" + strval(get_category_link(((StdClass) parent).fields.getValue("term_id"))) + "\" title=\"" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), ((StdClass) parent).fields.getValue("cat_name")) + "\">" + name + "</a>" +
                separator;
        } else {
            chain = chain + name + separator;
        }

        return chain;
    }

    public Array get_the_category(int id) {
        Array<Object> categories = new Array<Object>();
        Object key = null;
        id = id;

        if (!booleanval(id)) {
            id = intval(StdClass.getValue(gVars.post, "ID"));
        }

        categories = getIncluded(TaxonomyPage.class, gVars, gConsts).get_object_term_cache(id, "category");

        if (strictEqual(null, categories)) {
            categories = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(id, "category", new Array<Object>());
        }

        if (!empty(categories)) {
            Array.usort(categories, new Callback("_usort_terms_by_name", this));
        } else {
            categories = new Array<Object>();
        }

        for (Map.Entry javaEntry392 : Array.array_keys(categories).entrySet()) {
            key = javaEntry392.getValue();
            getIncluded(CategoryPage.class, gVars, gConsts)._make_cat_compat(categories.getValue(key));
        }

        return categories;
    }

    public int _usort_terms_by_name(Object a, Object b) {
        StdClass aObj = (StdClass) a;
        StdClass bObj = (StdClass) b;

        return Strings.strcmp(strval(StdClass.getValue(aObj, "name")), strval(StdClass.getValue(bObj, "name")));
    }

    public int _usort_terms_by_ID(Object a, Object b) {
        StdClass aObj = (StdClass) a;
        StdClass bObj = (StdClass) b;

        if (intval(StdClass.getValue(aObj, "term_id")) > intval(StdClass.getValue(bObj, "term_id"))) {
            return 1;
        } else if (intval(StdClass.getValue(aObj, "term_id")) < intval(StdClass.getValue(bObj, "term_id"))) {
            return -1;
        } else {
            return 0;
        }
    }

    public Object get_the_category_by_ID(int cat_ID) {
        Object category;

        //		cat_ID = intval(cat_ID);
        category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat_ID, gConsts.getOBJECT(), "raw");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(category)) {
            return category;
        }

        return ((StdClass) category).fields.getValue("name");
    }

    public Object get_the_category_list(String separator, String parents, int post_id) {
        Array<Object> categories = new Array<Object>();
        String rel = null;
        Object thelist = null;
        StdClass category = null;
        int i = 0;
        categories = get_the_category(post_id);

        if (empty(categories)) {
            return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_category", getIncluded(L10nPage.class, gVars, gConsts).__("Uncategorized", "default"), separator, parents);
        }

        rel = ((is_object(gVars.wp_rewrite) && gVars.wp_rewrite.using_permalinks())
            ? "rel=\"category tag\""
            : "rel=\"category\"");
        thelist = "";

        if (equal("", separator)) {
            thelist = strval(thelist) + "<ul class=\"post-categories\">";

            for (Map.Entry javaEntry393 : categories.entrySet()) {
                category = (StdClass) javaEntry393.getValue();
                thelist = strval(thelist) + "\n\t<li>";

                {
                    int javaSwitchSelector40 = 0;

                    if (equal(Strings.strtolower(parents), "multiple")) {
                        javaSwitchSelector40 = 1;
                    }

                    if (equal(Strings.strtolower(parents), "single")) {
                        javaSwitchSelector40 = 2;
                    }

                    if (equal(Strings.strtolower(parents), "")) {
                        javaSwitchSelector40 = 3;
                    }

                    switch (javaSwitchSelector40) {
                    case 1: {
                        if (booleanval(StdClass.getValue(category, "parent"))) {
                            thelist = strval(thelist) + strval(get_category_parents(StdClass.getValue(category, "parent"), true, "/", false));
                        }

                        thelist = strval(thelist) + "<a href=\"" + strval(get_category_link(StdClass.getValue(category, "term_id"))) + "\" title=\"" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), StdClass.getValue(category, "name")) + "\" " + rel + ">" +
                            StdClass.getValue(category, "name") + "</a></li>";

                        break;
                    }

                    case 2: {
                        thelist = strval(thelist) + "<a href=\"" + strval(get_category_link(StdClass.getValue(category, "term_id"))) + "\" title=\"" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), StdClass.getValue(category, "name")) + "\" " + rel + ">";

                        if (booleanval(StdClass.getValue(category, "parent"))) {
                            thelist = strval(thelist) + strval(get_category_parents(StdClass.getValue(category, "parent"), false, "/", false));
                        }

                        thelist = strval(thelist) + StdClass.getValue(category, "name") + "</a></li>";

                        break;
                    }

                    case 3: {
                    }

                    default:thelist = strval(thelist) + "<a href=\"" + strval(get_category_link(StdClass.getValue(category, "term_id"))) + "\" title=\"" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), StdClass.getValue(category, "name")) + "\" " + rel + ">" +
                            StdClass.getValue(category, "cat_name") + "</a></li>";
                    }
                }
            }

            thelist = strval(thelist) + "</ul>";
        } else {
            i = 0;

            for (Map.Entry javaEntry394 : categories.entrySet()) {
                category = (StdClass) javaEntry394.getValue();

                if (0 < i) {
                    thelist = strval(thelist) + separator + " ";
                }

                {
                    int javaSwitchSelector41 = 0;

                    if (equal(Strings.strtolower(parents), "multiple")) {
                        javaSwitchSelector41 = 1;
                    }

                    if (equal(Strings.strtolower(parents), "single")) {
                        javaSwitchSelector41 = 2;
                    }

                    if (equal(Strings.strtolower(parents), "")) {
                        javaSwitchSelector41 = 3;
                    }

                    switch (javaSwitchSelector41) {
                    case 1: {
                        if (booleanval(StdClass.getValue(category, "parent"))) {
                            thelist = strval(thelist) + strval(get_category_parents(StdClass.getValue(category, "parent"), true, "/", false));
                        }

                        thelist = strval(thelist) + "<a href=\"" + strval(get_category_link(StdClass.getValue(category, "term_id"))) + "\" title=\"" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), StdClass.getValue(category, "name")) + "\" " + rel + ">" +
                            StdClass.getValue(category, "cat_name") + "</a>";

                        break;
                    }

                    case 2: {
                        thelist = strval(thelist) + "<a href=\"" + strval(get_category_link(StdClass.getValue(category, "term_id"))) + "\" title=\"" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), StdClass.getValue(category, "name")) + "\" " + rel + ">";

                        if (booleanval(StdClass.getValue(category, "parent"))) {
                            thelist = strval(thelist) + strval(get_category_parents(StdClass.getValue(category, "parent"), false, "/", false));
                        }

                        thelist = strval(thelist) + StdClass.getValue(category, "cat_name") + "</a>";

                        break;
                    }

                    case 3: {
                    }

                    default:thelist = strval(thelist) + "<a href=\"" + strval(get_category_link(StdClass.getValue(category, "term_id"))) + "\" title=\"" +
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("View all posts in %s", "default"), StdClass.getValue(category, "name")) + "\" " + rel + ">" +
                            StdClass.getValue(category, "name") + "</a>";
                    }
                }

                ++i;
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_category", thelist, separator, parents);
    }

    /*
     * in_category() - Checks whether the current post is within a particular category
     *
     * This function checks to see if the post is within the supplied category.  The categoy
     * can be specified by number or name and will be checked as a name first to allow for categories with numeric names.
     * Note: Prior to v2.5 of WordPress category names where not supported.
     *
     * @since 1.2.0
     *
     * @param int|string $category
     * @return bool true if the post is in the supplied category
    */
    public boolean in_category(int category) { // Check if the current post is in the given category
        int cat_ID = 0;
        Array<Object> categories;

        if (empty(category)) {
            return false;
        }

        cat_ID = getIncluded(CategoryPage.class, gVars, gConsts).get_cat_ID(category);

        if (booleanval(cat_ID)) {
            category = cat_ID;
        }

        categories = getIncluded(TaxonomyPage.class, gVars, gConsts).get_object_term_cache(StdClass.getValue(gVars.post, "ID"), "category");

        if (strictEqual(null, categories)) {
            categories = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(StdClass.getValue(gVars.post, "ID"), "category", new Array<Object>());
        }

        if (Array.array_key_exists(category, categories)) {
            return true;
        } else {
            return false;
        }
    }

    public void the_category() {
        the_category("", "", 0);
    }

    public void the_category(String separator) {
        the_category(separator, "", 0);
    }

    public void the_category(String separator, String parents) {
        the_category(separator, parents, 0);
    }

    public void the_category(String separator, String parents, int post_id) {
        echo(gVars.webEnv, get_the_category_list(separator, parents, post_id));
    }

    public Object category_description(Object category) {
        if (!booleanval(category)) {
            category = gVars.cat;
        }

        return getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_field("description", intval(category), "category", "display");
    }

    public Object wp_dropdown_categories(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = new Array<Object>();
        String tab_index_attribute = null;
        Object tab_index = null;
        Array<Object> categories = new Array<Object>();
        Object output = null;
        Object name = null;
        Object _class = null;
        Object show_option_all = null;
        Object show_option_none = null;
        Object hierarchical = null;
        int depth = 0;
        Object echo = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("show_option_all", ""),
                new ArrayEntry<Object>("show_option_none", ""),
                new ArrayEntry<Object>("orderby", "ID"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("show_last_update", 0),
                new ArrayEntry<Object>("show_count", 0),
                new ArrayEntry<Object>("hide_empty", 1),
                new ArrayEntry<Object>("child_of", 0),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("echo", 1),
                new ArrayEntry<Object>("selected", 0),
                new ArrayEntry<Object>("hierarchical", 0),
                new ArrayEntry<Object>("name", "cat"),
                new ArrayEntry<Object>("class", "postform"),
                new ArrayEntry<Object>("depth", 0),
                new ArrayEntry<Object>("tab_index", 0));
        
        defaults.putValue("selected", getIncluded(QueryPage.class, gVars, gConsts).is_category(new Array<Object>())
            ? getIncluded(QueryPage.class, gVars, gConsts).get_query_var("cat")
            : 0);
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        r.putValue("include_last_update_time", r.getValue("show_last_update"));
        tab_index = Array.extractVar(r, "tab_index", tab_index, Array.EXTR_OVERWRITE);
        name = Array.extractVar(r, "name", name, Array.EXTR_OVERWRITE);
        _class = Array.extractVar(r, "class", _class, Array.EXTR_OVERWRITE);
        show_option_all = Array.extractVar(r, "show_option_all", show_option_all, Array.EXTR_OVERWRITE);
        show_option_none = Array.extractVar(r, "show_option_none", show_option_none, Array.EXTR_OVERWRITE);
        hierarchical = Array.extractVar(r, "hierarchical", hierarchical, Array.EXTR_OVERWRITE);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_OVERWRITE);
        tab_index_attribute = "";

        if (intval(tab_index) > 0) {
            tab_index_attribute = " tabindex=\"" + strval(tab_index) + "\"";
        }

        categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories(r);
        output = "";

        if (!empty(categories)) {
            output = "<select name=\'" + strval(name) + "\' id=\'" + strval(name) + "\' class=\'" + strval(_class) + "\' " + tab_index_attribute + ">\n";

            if (booleanval(show_option_all)) {
                show_option_all = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("list_cats", show_option_all);
                output = strval(output) + "\t<option value=\'0\'>" + strval(show_option_all) + "</option>\n";
            }

            if (booleanval(show_option_none)) {
                show_option_none = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("list_cats", show_option_none);
                output = strval(output) + "\t<option value=\'-1\'>" + strval(show_option_none) + "</option>\n";
            }

            if (booleanval(hierarchical)) {
                depth = intval(r.getValue("depth"));  // Walk the full depth.
            } else {
                depth = -1; // Flat.
            }

            output = strval(output) + walk_category_dropdown_tree(categories, depth, r);
            output = strval(output) + "</select>\n";
        }

        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_dropdown_cats", output);

        if (booleanval(echo)) {
            echo(gVars.webEnv, output);
        }

        return output;
    }

    public Object wp_list_categories(String args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = new Array<Object>();
        Array<Object> categories = new Array<Object>();
        Object output = null;
        Object title_li = null;
        Object style = null;
        Object show_option_all = null;
        Object hierarchical = null;
        int depth = 0;
        Object echo = null;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("show_option_all", ""),
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("show_last_update", 0),
                new ArrayEntry<Object>("style", "list"),
                new ArrayEntry<Object>("show_count", 0),
                new ArrayEntry<Object>("hide_empty", 1),
                new ArrayEntry<Object>("use_desc_for_title", 1),
                new ArrayEntry<Object>("child_of", 0),
                new ArrayEntry<Object>("feed", ""),
                new ArrayEntry<Object>("feed_type", ""),
                new ArrayEntry<Object>("feed_image", ""),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("hierarchical", true),
                new ArrayEntry<Object>("title_li", getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default")),
                new ArrayEntry<Object>("echo", 1),
                new ArrayEntry<Object>("depth", 0));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);

        if (!isset(r.getValue("pad_counts")) && booleanval(r.getValue("show_count")) && booleanval(r.getValue("hierarchical"))) {
            r.putValue("pad_counts", true);
        }

        if (isset(r.getValue("show_date"))) {
            r.putValue("include_last_update_time", r.getValue("show_date"));
        }

        title_li = Array.extractVar(r, "title_li", title_li, Array.EXTR_OVERWRITE);
        style = Array.extractVar(r, "style", style, Array.EXTR_OVERWRITE);
        show_option_all = Array.extractVar(r, "show_option_all", show_option_all, Array.EXTR_OVERWRITE);
        hierarchical = Array.extractVar(r, "hierarchical", hierarchical, Array.EXTR_OVERWRITE);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_OVERWRITE);
        categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories(r);
        output = "";

        if (booleanval(title_li) && equal("list", style)) {
            output = "<li class=\"categories\">" + strval(r.getValue("title_li")) + "<ul>";
        }

        if (empty(categories)) {
            if (equal("list", style)) {
                output = strval(output) + "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("No categories", "default") + "</li>";
            } else {
                output = strval(output) + getIncluded(L10nPage.class, gVars, gConsts).__("No categories", "default");
            }
        } else/*
         * Flat. Flat.
         */
         {
            if (!empty(show_option_all)) {
                if (equal("list", style)) {
                    output = strval(output) + "<li><a href=\"" + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "\">" + strval(show_option_all) + "</a></li>";
                } else {
                    output = strval(output) + "<a href=\"" + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "\">" + strval(show_option_all) + "</a>";
                }
            }

            if (getIncluded(QueryPage.class, gVars, gConsts).is_category(new Array<Object>())) {
                r.putValue("current_category", gVars.wp_query.get_queried_object_id());
            }

            if (booleanval(hierarchical)) {
                depth = intval(r.getValue("depth"));
            } else {
                depth = -1; // Flat.
            }

            output = strval(output) + walk_category_tree(categories, depth, r);
        }

        if (booleanval(title_li) && equal("list", style)) {
            output = strval(output) + "</ul></li>";
        }

        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_list_categories", output);

        if (booleanval(echo)) {
            echo(gVars.webEnv, output);
        } else {
            return output;
        }

        return "";
    }

    public Object wp_tag_cloud(Object args) {
        Array<Object> defaults = new Array<Object>();
        Object tags = null;
        Object _return = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("smallest", 8),
                new ArrayEntry<Object>("largest", 22),
                new ArrayEntry<Object>("unit", "pt"),
                new ArrayEntry<Object>("number", 45),
                new ArrayEntry<Object>("format", "flat"),
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("include", ""));
        
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        
        tags = getIncluded(CategoryPage.class, gVars, gConsts)
                   .get_tags(Array.array_merge((Array) args, new Array<Object>(new ArrayEntry<Object>("orderby", "count"), new ArrayEntry<Object>("order", "DESC")))); // Always query top tags

        if (empty(tags)) {
            return null;
        }

        _return = wp_generate_tag_cloud(tags, args); // Here's where those top tags get sorted according to $args

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(_return)) {
            return false;
        }

        _return = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_tag_cloud", _return, args);

        if (equal("array", ((Array) args).getValue("format"))) {
            return _return;
        }

        echo(gVars.webEnv, _return);

        return false;
    }

 // $tags = prefetched tag array ( get_tags() )
 // $args['format'] = 'flat' => whitespace separated, 'list' => UL, 'array' => array()
 // $args['orderby'] = 'name', 'count'
    public Object wp_generate_tag_cloud(Object tags, Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> counts = new Array<Object>();
        Array<Object> tag_links = new Array<Object>();
        StdClass tag;
        Array<Object> tag_ids = new Array<Object>();
        Object min_count = null;
        int spread = 0;
        int font_spread = 0;
        Object largest = null;
        Object smallest = null;
        float font_step = 0;
        String orderby = null;
        Object order = null;
        Array<Object> keys = new Array<Object>();
        Array<Object> temp = new Array<Object>();
        Object key = null;
        Array<String> a = new Array<String>();
        String rel = null;
        Object tag_id = null;
        Object tag_link = null;
        int count = 0;
        Object unit = null;
        Object _return;
        Object format = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("smallest", 8),
                new ArrayEntry<Object>("largest", 22),
                new ArrayEntry<Object>("unit", "pt"),
                new ArrayEntry<Object>("number", 45),
                new ArrayEntry<Object>("format", "flat"),
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("order", "ASC"));

        Array argsArray = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        tag_ids = (Array<Object>) Array.extractVar(argsArray, "tag_ids", tag_ids, Array.EXTR_OVERWRITE);
        largest = Array.extractVar(argsArray, "largest", largest, Array.EXTR_OVERWRITE);
        smallest = Array.extractVar(argsArray, "smallest", smallest, Array.EXTR_OVERWRITE);
        orderby = strval(Array.extractVar(argsArray, "orderby", orderby, Array.EXTR_OVERWRITE));
        order = Array.extractVar(argsArray, "order", order, Array.EXTR_OVERWRITE);
        unit = Array.extractVar(argsArray, "unit", unit, Array.EXTR_OVERWRITE);
        format = Array.extractVar(argsArray, "format", format, Array.EXTR_OVERWRITE);

        if (!booleanval(tags)) {
            return null;
        }

        counts = new Array<Object>();
        tag_links = new Array<Object>();

        for (Map.Entry javaEntry395 : new Array<Object>(tags).entrySet()) {
            tag = (StdClass) javaEntry395.getValue();
            counts.putValue(StdClass.getValue(tag, "name"), StdClass.getValue(tag, "count"));
            tag_links.putValue(StdClass.getValue(tag, "name"), get_tag_link(StdClass.getValue(tag, "term_id")));

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(tag_links.getValue(StdClass.getValue(tag, "name")))) {
                return tag_links.getValue(StdClass.getValue(tag, "name"));
            }

            tag_ids.putValue(StdClass.getValue(tag, "name"), StdClass.getValue(tag, "term_id"));
        }

        min_count = Math.min(counts);
        spread = intval(Math.max(counts)) - intval(min_count);

        if (spread <= 0) {
            spread = 1;
        }

        font_spread = intval(largest) - intval(smallest);

        if (font_spread <= 0) {
            font_spread = 1;
        }

        font_step = floatval(font_spread) / floatval(spread);

    	// SQL cannot save you; this is a second (potentially different) sort on a subset of data.
        if (equal("name", orderby)) {
            Array.uksort(counts, new Callback("strnatcasecmp", CallbackUtils.class));
        } else {
            Array.asort(counts);
        }

        if (equal("DESC", order)) {
            counts = Array.array_reverse(counts, true);
        } else if (equal("RAND", order)) {
            keys = Array.array_rand(counts, Array.count(counts));

            for (Map.Entry javaEntry396 : keys.entrySet()) {
                key = javaEntry396.getValue();
                temp.putValue(key, counts.getValue(key));
            }

            counts = temp;
            temp = null;
        }

        a = new Array<String>();
        rel = ((is_object(gVars.wp_rewrite) && gVars.wp_rewrite.using_permalinks())
            ? " rel=\"tag\""
            : "");

        for (Map.Entry javaEntry397 : counts.entrySet()) {
            String tagStr = strval(javaEntry397.getKey());
            count = intval(javaEntry397.getValue());
            tag_id = tag_ids.getValue(tagStr);
            tag_link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(tag_links.getValue(tagStr)), null, "display");
            tagStr = Strings.str_replace(" ", "&nbsp;", getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(tagStr, strval(0)));
            a.putValue(
                    "<a href=\'" + strval(tag_link) + "\' class=\'tag-link-" + strval(tag_id) + "\' title=\'" +
                    getIncluded(FormattingPage.class, gVars, gConsts)
                        .attribute_escape(QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%d topic", "%d topics", count, "default"), count)) + "\'" + rel +
                    " style=\'font-size: " + strval(intval(smallest) + ((count - intval(min_count)) * intval(font_step))) + strval(unit) + ";\'>" + tagStr + "</a>");
        }

        {
            int javaSwitchSelector42 = 0;

            if (equal(format, "array")) {
                javaSwitchSelector42 = 1;
            }

            if (equal(format, "list")) {
                javaSwitchSelector42 = 2;
            }

            switch (javaSwitchSelector42) {
            case 1: {
                _return = a;

                break;
            }

            case 2: {
                _return = "<ul class=\'wp-tag-cloud\'>\n\t<li>";
                _return = _return + Strings.join("</li>\n\t<li>", a);
                _return = _return + "</li>\n</ul>\n";

                break;
            }

            default: {
                _return = Strings.join("\n", a);

                break;
            }
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_generate_tag_cloud", _return, tags, args);
    }

    //
    // Helper functions
    //
    public Object walk_category_tree(Object... vargs) {
        Walker_Category walker;
        Array<Object> args = new Array<Object>();
        walker = new Walker_Category(gVars, gConsts);
        args = FunctionHandling.func_get_args(vargs);

        return FunctionHandling.call_user_func_array(new Callback("walk", walker), args);
    }

    public Object walk_category_dropdown_tree(Object... vargs) {
        Ref<Walker_CategoryDropdown> walker = new Ref<Walker_CategoryDropdown>();
        Array<Object> args = new Array<Object>();
        walker.value = new Walker_CategoryDropdown(gVars, gConsts);
        args = FunctionHandling.func_get_args(vargs);

        return FunctionHandling.call_user_func_array(new Callback("walk", walker), args);
    }

    //
	// Tags
	//
    public Object get_tag_link(Object tag_id) {
        String taglink = null;
        Object tag;
        String slug = null;
        String file = null;
        taglink = gVars.wp_rewrite.get_tag_permastruct();
        tag = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(tag_id, "post_tag", gConsts.getOBJECT(), "raw");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(tag)) {
            return tag;
        }

        slug = strval(((StdClass) tag).fields.getValue("slug"));

        if (empty(taglink)) {
            file = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/";
            taglink = file + "?tag=" + slug;
        } else {
            taglink = Strings.str_replace("%tag%", slug, taglink);
            taglink = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(taglink, "category");
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tag_link", taglink, tag_id);
    }

    public Object get_the_tags(int id) {
        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_tags", get_the_terms(id, "post_tag"));
    }

    public Object get_the_tag_list(String before, String sep, String after) {
        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_tags", get_the_term_list(0, "post_tag", before, sep, after));
    }

    public boolean the_tags(String before, String sep, String after) {
        return the_terms(0, "post_tag", before, sep, after);
    }

    public Object get_the_terms(int id, String taxonomy) {
        Object terms = null;
//        id = intval(id);

        if (!booleanval(id) && !getIncluded(QueryPage.class, gVars, gConsts).in_the_loop()) {
            return null; // in-the-loop function
        }

        if (!booleanval(id)) {
            id = intval(StdClass.getValue(gVars.post, "ID"));
        }

        terms = getIncluded(TaxonomyPage.class, gVars, gConsts).get_object_term_cache(id, taxonomy);

        if (strictEqual(null, terms)) {
            terms = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(id, taxonomy, new Array<Object>());
        }

        if (empty(terms)) {
            return null;
        }

        return terms;
    }

    public Object get_the_term_list(int id, String taxonomy, String before, String sep, String after) {
        Object terms = null;
        Object link = null;
        StdClass term = null;
        Array<String> term_links = new Array<String>();
        terms = get_the_terms(id, taxonomy);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(terms)) {
            return terms;
        }

        if (empty(terms)) {
            return null;
        }

        for (Map.Entry javaEntry398 : ((Array<?>) terms).entrySet()) {
            term = (StdClass) javaEntry398.getValue();
            link = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_link(term, taxonomy);

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(link)) {
                return link;
            }

            term_links.putValue("<a href=\"" + strval(link) + "\" rel=\"tag\">" + StdClass.getValue(term, "name") + "</a>");
        }

        term_links = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_links-" + taxonomy, term_links);

        return before + Strings.join(sep, term_links) + after;
    }

    public boolean the_terms(int id, String taxonomy, String before, String sep, String after) {
        Object _return = null;
        _return = get_the_term_list(id, taxonomy, before, sep, after);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(_return)) {
            return false;
        } else {
            echo(gVars.webEnv, strval(_return));
        }

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
