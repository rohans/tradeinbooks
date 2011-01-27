/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: BookmarkPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.TaxonomyPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.VarHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/BookmarkPage")
@Scope("request")
public class BookmarkPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(BookmarkPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/bookmark.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/bookmark";
    }

    public int add_link() {
        return edit_link("");
    }

    public int edit_link(String link_id) {
        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
        }

        gVars.webEnv._POST.putValue("link_url", getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.webEnv._POST.getValue("link_url")), strval(0)));
        gVars.webEnv._POST.putValue("link_url", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(gVars.webEnv._POST.getValue("link_url")), null, "display"));
        gVars.webEnv._POST.putValue("link_name", getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.webEnv._POST.getValue("link_name")), strval(0)));
        gVars.webEnv._POST.putValue("link_image", getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.webEnv._POST.getValue("link_image")), strval(0)));
        gVars.webEnv._POST.putValue("link_rss", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(gVars.webEnv._POST.getValue("link_rss")), null, "display"));

        if (!equal("N", gVars.webEnv._POST.getValue("link_visible"))) {
            gVars.webEnv._POST.putValue("link_visible", "Y");
        }

        if (!empty(link_id)) {
            gVars.webEnv._POST.putValue("link_id", link_id);

            return wp_update_link(gVars.webEnv._POST);
        } else {
            return wp_insert_link(gVars.webEnv._POST);
        }
    }

    public StdClass get_default_link_to_edit() {
        StdClass link = new StdClass();

        if (isset(gVars.webEnv._GET.getValue("linkurl"))) {
            link.fields.putValue("link_url", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(gVars.webEnv._GET.getValue("linkurl")), null, "display"));
        } else {
            link.fields.putValue("link_url", "");
        }

        if (isset(gVars.webEnv._GET.getValue("name"))) {
            link.fields.putValue("link_name", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("name"))));
        } else {
            link.fields.putValue("link_name", "");
        }

        link.fields.putValue("link_visible", "Y");

        return link;
    }

    public boolean wp_delete_link(int link_id) {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_link", link_id);
        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_object_term_relationships(link_id, "link_category");
        gVars.wpdb.query("DELETE FROM " + gVars.wpdb.links + " WHERE link_id = \'" + link_id + "\'");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("deleted_link", link_id);

        return true;
    }

    public Array<Object> wp_get_link_cats(int link_id) {
        Array<Object> cats = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(link_id, "link_category", "fields=ids");

        return Array.array_unique(cats);
    }

    public StdClass get_link_to_edit(int link_id) {
        return (StdClass) (((org.numiton.nwp.wp_includes.BookmarkPage) getIncluded(org.numiton.nwp.wp_includes.BookmarkPage.class, gVars, gConsts))).get_bookmark(link_id, gConsts.getOBJECT(), "edit");
    }

    public int wp_insert_link(Object linkdata) {
        Array<Object> defaults = new Array<Object>();
        Boolean update = null;
        Integer link_id = null;
        String link_name = null;
        String link_url = null;
        Integer link_rating = null;
        String link_image = null;
        String link_target = null;
        String link_visible = null;
        Integer link_owner = null;
        String link_notes = null;
        String link_description = null;
        String link_rss = null;
        String link_rel = null;
        Object link_category = null;

        /* Do not change type */
        defaults = new Array<Object>(new ArrayEntry<Object>("link_id", 0), new ArrayEntry<Object>("link_name", ""), new ArrayEntry<Object>("link_url", ""), new ArrayEntry<Object>("link_rating", 0));
        linkdata = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(linkdata, defaults);
        linkdata = (((org.numiton.nwp.wp_includes.BookmarkPage) getIncluded(org.numiton.nwp.wp_includes.BookmarkPage.class, gVars, gConsts))).sanitize_bookmark(linkdata, "db");
        link_id = intval(Array.extractVar((Array) linkdata, "link_id", link_id, Array.EXTR_SKIP));
        link_name = strval(Array.extractVar((Array) linkdata, "link_name", link_name, Array.EXTR_SKIP));
        link_url = strval(Array.extractVar((Array) linkdata, "link_url", link_url, Array.EXTR_SKIP));
        link_rating = intval(Array.extractVar((Array) linkdata, "link_rating", link_rating, Array.EXTR_SKIP));
        link_image = strval(Array.extractVar((Array) linkdata, "link_image", link_image, Array.EXTR_SKIP));
        link_target = strval(Array.extractVar((Array) linkdata, "link_target", link_target, Array.EXTR_SKIP));
        link_visible = strval(Array.extractVar((Array) linkdata, "link_visible", link_visible, Array.EXTR_SKIP));
        link_owner = intval(Array.extractVar((Array) linkdata, "link_owner", link_owner, Array.EXTR_SKIP));
        link_notes = strval(Array.extractVar((Array) linkdata, "link_notes", link_notes, Array.EXTR_SKIP));
        link_description = strval(Array.extractVar((Array) linkdata, "link_description", link_description, Array.EXTR_SKIP));
        link_rss = strval(Array.extractVar((Array) linkdata, "link_rss", link_rss, Array.EXTR_SKIP));
        link_rel = strval(Array.extractVar((Array) linkdata, "link_rel", link_rel, Array.EXTR_SKIP));
        link_category = Array.extractVar((Array) linkdata, "link_category", link_category, Array.EXTR_SKIP);
        update = false;

        if (!empty(link_id)) {
            update = true;
        }

        if (equal(Strings.trim(link_name), "")) {
            return 0;
        }

        if (equal(Strings.trim(link_url), "")) {
            return 0;
        }

        if (empty(link_rating)) {
            link_rating = 0;
        }

        if (empty(link_image)) {
            link_image = "";
        }

        if (empty(link_target)) {
            link_target = "";
        }

        if (empty(link_visible)) {
            link_visible = "Y";
        }

        if (empty(link_owner)) {
            link_owner = gVars.current_user.getID();
        }

        if (empty(link_notes)) {
            link_notes = "";
        }

        if (empty(link_description)) {
            link_description = "";
        }

        if (empty(link_rss)) {
            link_rss = "";
        }

        if (empty(link_rel)) {
            link_rel = "";
        }

        // Make sure we set a valid category
        if (equal(0, Array.count(link_category)) || !is_array(link_category)) {
            link_category = new Array<Object>(new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category")));
        }

        if (update) {
            gVars.wpdb.query(
                    "UPDATE " + gVars.wpdb.links + " SET link_url=\'" + link_url + "\',\n\t\t\tlink_name=\'" + link_name + "\', link_image=\'" + link_image + "\',\n\t\t\tlink_target=\'" +
                    link_target + "\',\n\t\t\tlink_visible=\'" + link_visible + "\', link_description=\'" + link_description + "\',\n\t\t\tlink_rating=\'" + link_rating + "\', link_rel=\'" +
                    link_rel + "\',\n\t\t\tlink_notes=\'" + link_notes + "\', link_rss = \'" + link_rss + "\'\n\t\t\tWHERE link_id=\'" + link_id + "\'");
        } else {
            gVars.wpdb.query(
                    "INSERT INTO " + gVars.wpdb.links +
                    " (link_url, link_name, link_image, link_target, link_description, link_visible, link_owner, link_rating, link_rel, link_notes, link_rss) VALUES(\'" + link_url + "\',\'" +
                    link_name + "\', \'" + link_image + "\', \'" + link_target + "\', \'" + link_description + "\', \'" + link_visible + "\', \'" + link_owner + "\', \'" + link_rating + "\', \'" +
                    link_rel + "\', \'" + link_notes + "\', \'" + link_rss + "\')");
            link_id = gVars.wpdb.insert_id;
        }

        wp_set_link_cats(link_id, link_category);

        if (update) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_link", link_id);
        } else {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("add_link", link_id);
        }

        return link_id;
    }

    public void wp_set_link_cats(int link_id, Object link_categoriesObj)/* Do not change type */
     {
        /* Modified by Numiton */
        Array link_categories;

        // If $link_categories isn't already an array, make it one:
        if (!is_array(link_categoriesObj) || equal(0, Array.count(link_categoriesObj))) {
            link_categories = new Array<Object>(new ArrayEntry<Object>(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category")));
        } else {
            link_categories = (Array) link_categoriesObj;
        }

        link_categories = Array.array_map(new Callback("intval", VarHandling.class), link_categories);
        link_categories = Array.array_unique(link_categories);
        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_set_object_terms(link_id, link_categories, "link_category", false);
    } // wp_set_link_cats()

    /**
     * wp_set_link_cats() wp_set_link_cats()
     */
    public int wp_update_link(Array<Object> linkdata) {
        int link_id = 0;
        Array<Object> link = new Array<Object>();
        Object link_cats = null;
        
        link_id = intval(linkdata.getValue("link_id"));
        
        link = (Array<Object>) getIncluded(org.numiton.nwp.wp_includes.BookmarkPage.class, gVars, gConsts).get_link(link_id, gConsts.getARRAY_A(), "raw");
        
        // Escape data pulled from DB.
        link = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(link);

        // Passed link category list overwrites existing category list if not empty.
        if (isset(linkdata.getValue("link_category")) && is_array(linkdata.getValue("link_category")) && !equal(0, Array.count(linkdata.getValue("link_category")))) {
            link_cats = linkdata.getValue("link_category");
        } else {
            link_cats = link.getValue("link_category");
        }

        // Merge old and new fields with new fields overwriting old ones.
        linkdata = Array.array_merge(link, linkdata);
        linkdata.putValue("link_category", link_cats);

        return wp_insert_link(linkdata);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
