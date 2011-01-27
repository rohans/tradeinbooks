/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: TaxonomyPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/TaxonomyPage")
@Scope("request")
public class TaxonomyPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(TaxonomyPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/taxonomy.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/taxonomy";
    }

    /**
     * Category
     */
    public Object category_exists(Object cat_name) {
        Object id =  /* Do not change type */(((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).is_term(cat_name, "category");

        if (is_array(id)) {
            id = ((Array) id).getValue("term_id");
        }

        return id;
    }

    public Object get_category_to_edit(int id) {
        Object category = null;
        category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(id, gConsts.getOBJECT(), "edit");

        return category;
    }

    public Object wp_create_category(Object cat_name, int parent) {
        Object id;

        if (booleanval(id = category_exists(cat_name))) {
            return id;
        }

        return wp_insert_category(new Array<Object>(new ArrayEntry<Object>("cat_name", cat_name), new ArrayEntry<Object>("category_parent", parent)), false);
    }

    public Array<Object> wp_create_categories(Array<Object> categories, int post_id) {
        Array<Object> cat_ids = new Array<Object>();
        Object id = null;
        Object category = null;
        cat_ids = new Array<Object>();

        for (Map.Entry javaEntry194 : categories.entrySet()) {
            category = javaEntry194.getValue();

            if (booleanval(id = category_exists(category))) {
                cat_ids.putValue(id);
            } else if (booleanval(id = wp_create_category(category, 0))) {
                cat_ids.putValue(id);
            }
        }

        if (booleanval(post_id)) {
            getIncluded(PostPage.class, gVars, gConsts).wp_set_post_categories(post_id, cat_ids);
        }

        return cat_ids;
    }

    public int wp_delete_category(int cat_ID) {
        Object _default = null;

        //		cat_ID = intval(cat_ID);
        _default = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category");

    	// Don't delete the default cat
        if (equal(cat_ID, _default)) {
            return 0;
        }

        return intval((((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_delete_term(cat_ID, "category", "default=" + strval(_default)));
    }

    public Object wp_insert_category(Object catarr, boolean wp_error) {
        Array<Object> cat_defaults = new Array<Object>();
        Array<Object> cat_arr = null;
        String cat_name = null;
        Object cat_ID = null;
        Boolean update = null;
        String name = null;
        Object description = null;
        Object category_description = null;
        Object slug = null;
        Object category_nicename = null;
        Integer parent = null;
        Object category_parent = null;
        Array<Object> args = new Array<Object>();
        
        cat_defaults = new Array<Object>(
                new ArrayEntry<Object>("cat_ID", 0),
                new ArrayEntry<Object>("cat_name", ""),
                new ArrayEntry<Object>("category_description", ""),
                new ArrayEntry<Object>("category_nicename", ""),
                new ArrayEntry<Object>("category_parent", ""));
        cat_arr = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(catarr, cat_defaults);
        
        // Bug fix by Numiton
        {
            cat_name = strval(Array.extractVar(cat_arr, "cat_name", cat_name, Array.EXTR_SKIP));
            cat_ID = Array.extractVar(cat_arr, "cat_ID", cat_ID, Array.EXTR_SKIP);
            category_description = Array.extractVar(cat_arr, "category_description", category_description, Array.EXTR_SKIP);
            category_nicename = Array.extractVar(cat_arr, "category_nicename", category_nicename, Array.EXTR_SKIP);
            category_parent = Array.extractVar(cat_arr, "category_parent", category_parent, Array.EXTR_SKIP);
        }

        if (equal(Strings.trim(cat_name), "")) {
            if (!wp_error) {
                return 0;
            } else {
                return new WP_Error(gVars, gConsts, "cat_name", getIncluded(L10nPage.class, gVars, gConsts).__("You did not enter a category name.", "default"));
            }
        }

        cat_ID = intval(cat_ID);

    	// Are we updating or creating?
        if (!empty(cat_ID)) {
            update = true;
        } else {
            update = false;
        }

        name = cat_name;
        description = category_description;
        slug = category_nicename;
        parent = intval(category_parent);
        parent = parent;

        if (parent < 0) {
            parent = 0;
        }

        if (empty(parent) || !booleanval(category_exists(parent)) || (booleanval(cat_ID) && getIncluded(CategoryPage.class, gVars, gConsts).cat_is_ancestor_of(cat_ID, parent))) {
            parent = 0;
        }

        args = Array.compact(new ArrayEntry("name", name), new ArrayEntry("slug", slug), new ArrayEntry("parent", parent), new ArrayEntry("description", description));

        if (update) {
            cat_ID = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_update_term(intval(cat_ID), "category", args);
        } else {
            cat_ID = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(cat_name, "category", args);
        }

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(cat_ID)) {
            if (wp_error) {
                return cat_ID;
            } else {
                return 0;
            }
        }

        return ((Array) cat_ID).getValue("term_id");
    }

    public Object wp_update_category(Array<Object> catarr) {
        int cat_ID = 0;
        Array<Object> category = null;
        cat_ID = intval(catarr.getValue("cat_ID"));

        if (equal(cat_ID, catarr.getValue("category_parent"))) {
            return false;
        }

    	// First, get all of the original fields
        category = (Array<Object>) getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat_ID, gConsts.getARRAY_A(), "raw");
        
        // Escape data pulled from DB.
        category = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(category);
        
        // Merge old and new fields with new fields overwriting old ones.
        catarr = Array.array_merge(category, catarr);

        return wp_insert_category(catarr, false);
    }

    /**
     * Tags
     */
    public String get_tags_to_edit(int post_id) {
        Array<Object> tags = null;
        Array<String> tag_names = new Array<String>();
        StdClass tag = null;
        String tags_to_edit = null;

        //		post_id = intval(post_id);
        if (!booleanval(post_id)) {
            return strval(false);
        }

        tags = (Array<Object>) getIncluded(PostPage.class, gVars, gConsts).wp_get_post_tags(post_id, new Array<Object>());

        if (!booleanval(tags)) {
            return strval(false);
        }

        for (Map.Entry javaEntry195 : tags.entrySet()) {
            tag = (StdClass) javaEntry195.getValue();
            tag_names.putValue(StdClass.getValue(tag, "name"));
        }

        tags_to_edit = Strings.join(", ", tag_names);
        tags_to_edit = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(tags_to_edit);
        tags_to_edit = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tags_to_edit", tags_to_edit));

        return tags_to_edit;
    }

    public Object tag_exists(String tag_name) {
        return (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).is_term(tag_name, "post_tag");
    }

    public Object wp_create_tag(String tag_name) {
        Object id;

        if (booleanval(id = tag_exists(tag_name))) {
            return id;
        }

        return (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(tag_name, "post_tag", new Array<Object>());
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
