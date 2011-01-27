/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CategoryPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class CategoryPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CategoryPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/category.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/category";
    }

    public Object get_all_category_ids() {
        Object cat_ids = null;

        if (!booleanval(cat_ids = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("all_category_ids", "category"))) {
            cat_ids = getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("category", "fields=ids&get=all");
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("all_category_ids", cat_ids, "category", 0);
        }

        return cat_ids;
    }

    public Array get_categories(Object argsObj) {
        Array<Object> defaults = new Array<Object>();
        String taxonomy = null;
        Array<Object> categories = new Array<Object>();
        Object k = null;
        defaults = new Array<Object>(new ArrayEntry<Object>("type", "category"));

        Array<Object> args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(argsObj, defaults);
        taxonomy = "category";

        if (equal("link", args.getValue("type"))) {
            taxonomy = "link_category";
        }

        categories = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms(taxonomy, args);

        for (Map.Entry javaEntry399 : Array.array_keys(categories).entrySet()) {
            k = javaEntry399.getValue();
            _make_cat_compat(categories.getValue(k));
        }

        return categories;
    }

    /**
     * Retrieves category data given a category ID or category object.
     * Handles category caching.
     */
    public Object get_category(Object category, String output, String filter) {
        category = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(category, "category", output, filter);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(category)) {
            return category;
        }

        _make_cat_compat(category);

        return category;
    }

    public Object get_category_by_path(String category_path, boolean full_match, String output) {
        String category_paths = null;
        String leaf_path = null;
        String full_path = null;
        String pathdir = null;
        Array<Object> categories = new Array<Object>();
        String path = null;
        StdClass curcategory = null;
        StdClass category = null;
        
        category_path = URL.rawurlencode(URL.urldecode(category_path));
        category_path = Strings.str_replace("%2F", "/", category_path);
        category_path = Strings.str_replace("%20", " ", category_path);
        category_paths = "/" + Strings.trim(category_path, "/");
        leaf_path = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(FileSystemOrSocket.basename(category_paths), "");

        Array<String> category_pathsArray = Strings.explode("/", category_paths);
        full_path = "";

        for (Map.Entry javaEntry400 : category_pathsArray.entrySet()) {
            pathdir = strval(javaEntry400.getValue());
            full_path = full_path + ((!equal(pathdir, ""))
                ? "/"
                : "") + getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(pathdir, "");
        }

        categories = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("category", "get=all&slug=" + leaf_path);

        if (empty(categories)) {
            return null;
        }

        for (Map.Entry javaEntry401 : categories.entrySet()) {
            category = (StdClass) javaEntry401.getValue();
            path = "/" + leaf_path;
            curcategory = category;

            while (!equal(StdClass.getValue(curcategory, "parent"), 0) && !equal(StdClass.getValue(curcategory, "parent"), StdClass.getValue(curcategory, "term_id"))) {
                curcategory = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(StdClass.getValue(curcategory, "parent"), "category", gConsts.getOBJECT(), "raw");

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(curcategory)) {
                    return curcategory;
                }

                path = "/" + StdClass.getValue(curcategory, "slug") + path;
            }

            if (equal(path, full_path)) {
                return get_category(StdClass.getValue(category, "term_id"), output, "raw");
            }
        }

        // If full matching is not required, return the first cat that matches the leaf.
        if (!full_match) {
            return get_category(((StdClass) categories.getValue(0)).fields.getValue("term_id"), output, "raw");
        }

        return null;
    }

    public Object get_category_by_slug(Object slug) {
        Object category = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("slug", slug, "category", gConsts.getOBJECT(), "raw");

        if (booleanval(category)) {
            _make_cat_compat(category);
        }

        return category;
    }

    /**
     * Get the ID of a category from its name
     */
    public int get_cat_ID(int cat_name) {
        StdClass cat = null;
        cat = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("name", cat_name, "category", gConsts.getOBJECT(), "raw");

        if (booleanval(cat)) {
            return intval(StdClass.getValue(cat, "term_id"));
        }

        return 0;
    }

    /**
     * Deprecate
     */
    public String get_catname(int cat_ID) {
        return get_cat_name(cat_ID);
    }

    /**
     * Get the name of a category from its ID
     */
    public String get_cat_name(int cat_id) {
        StdClass category = null;

        //		cat_id = intval(cat_id);
        category = (StdClass) get_category(cat_id, gConsts.getOBJECT(), "raw");

        return strval(StdClass.getValue(category, "name"));
    }

    public boolean cat_is_ancestor_of(Object cat1, Object cat2) {
        if (is_int(cat1)) {
            cat1 = get_category(cat1, gConsts.getOBJECT(), "raw");
        }

        if (is_int(cat2)) {
            cat2 = get_category(cat2, gConsts.getOBJECT(), "raw");
        }

        if (!booleanval(((StdClass) cat1).fields.getValue("term_id")) || !booleanval(((StdClass) cat2).fields.getValue("parent"))) {
            return false;
        }

        if (equal(((StdClass) cat2).fields.getValue("parent"), ((StdClass) cat1).fields.getValue("term_id"))) {
            return true;
        }

        return cat_is_ancestor_of(cat1, get_category(((StdClass) cat2).fields.getValue("parent"), gConsts.getOBJECT(), "raw"));
    }

    public Object sanitize_category(Object category, String context) {
        return getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term(category, "category", context);
    }

    public Object sanitize_category_field(String field, String value, int cat_id, String context) {
        return getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field(field, value, cat_id, "category", context);
    }

    /**
     * Tags
     */
    public Array<Object> get_tags(Object args) {
        Array<Object> tags;
        tags = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("post_tag", args);

        if (empty(tags)) {
            return new Array<Object>();
        }

        tags = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_tags", tags, args);

        return tags;
    }

    public Object get_tag(int tag, String output, String filter) {
        return getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(tag, "post_tag", output, filter);
    }

    /**
     * Cache
     */
    public boolean update_category_cache() {
        return true;
    }

    public void clean_category_cache(Object id) {
        getIncluded(TaxonomyPage.class, gVars, gConsts).clean_term_cache(id, "category");
    }

    //
	// Private helpers
	//
    public void _make_cat_compat(Object category)/* Do not change type */
     {
        if (is_object(category)) {
            StdClass categoryObj = (StdClass) category;
            categoryObj.fields.putValue("cat_ID", StdClass.getValue(categoryObj, "term_id"));
            categoryObj.fields.putValue("category_count", StdClass.getValue(categoryObj, "count"));
            categoryObj.fields.putValue("category_description", StdClass.getValue(categoryObj, "description"));
            categoryObj.fields.putValue("cat_name", StdClass.getValue(categoryObj, "name"));
            categoryObj.fields.putValue("category_nicename", StdClass.getValue(categoryObj, "slug"));
            categoryObj.fields.putValue("category_parent", StdClass.getValue(categoryObj, "parent"));
        } else if (is_array(category) && isset(((Array) category).getValue("term_id"))) {
            Array categoryArray = (Array) category;
            categoryArray.putValue("cat_ID", categoryArray.getRef("term_id"));
            categoryArray.putValue("category_count", categoryArray.getRef("count"));
            categoryArray.putValue("category_description", categoryArray.getRef("description"));
            categoryArray.putValue("cat_name", categoryArray.getRef("name"));
            categoryArray.putValue("category_nicename", categoryArray.getRef("slug"));
            categoryArray.putValue("category_parent", categoryArray.getRef("parent"));
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
