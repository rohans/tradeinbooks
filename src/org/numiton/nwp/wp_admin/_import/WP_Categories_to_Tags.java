/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Categories_to_Tags.java,v 1.3 2008/10/10 16:48:04 numiton Exp $
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
package org.numiton.nwp.wp_admin._import;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.*;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class WP_Categories_to_Tags implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Categories_to_Tags.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> categories_to_convert = new Array<Object>();
    public Array<Object> all_categories = new Array<Object>();

    public WP_Categories_to_Tags(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Do nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Convert Categories to Tags", "default") + "</h2>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void populate_all_categories() {
        Array<Object> categories = new Array<Object>();
        StdClass category = null;
        categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("get=all");

        for (Map.Entry javaEntry94 : categories.entrySet()) {
            category = (StdClass) javaEntry94.getValue();

            if (!booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).tag_exists(gVars.wpdb.escape(strval(StdClass.getValue(category, "name")))))) {
                this.all_categories.putValue(category);
            }
        }
    }

    public void welcome() {
        this.populate_all_categories();
        echo(gVars.webEnv, "<div class=\"narrow\">");

        if (Array.count(this.all_categories) > 0) {
            echo(
                    gVars.webEnv,
                    "<p>" +
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "Hey there. Here you can selectively converts existing categories to tags. To get started, check the categories you wish to be converted, then click the Convert button.",
                            "default") + "</p>");
            echo(
                gVars.webEnv,
                "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Keep in mind that if you convert a category with child categories, the children become top-level orphans.", "default") +
                "</p>");
            this.categories_form();
        } else {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("You have no categories to convert!", "default") + "</p>");
        }

        echo(gVars.webEnv, "</div>");
    }

    public void categories_form() {
        Array<Object> hier = new Array<Object>();
        StdClass category = null;
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\n<!--\nvar checkflag = \"false\";\nfunction check_all_rows() {\n\tfield = document.formlist;\n\tif ( \'false\' == checkflag ) {\n\t\tfor ( i = 0; i < field.length; i++ ) {\n\t\t\tif ( \'cats_to_convert[]\' == field[i].name )\n\t\t\t\tfield[i].checked = true;\n\t\t}\n\t\tcheckflag = \'true\';\n\t\treturn \'");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Uncheck All", "default");
        echo(
                gVars.webEnv,
                "\';\n\t} else {\n\t\tfor ( i = 0; i < field.length; i++ ) {\n\t\t\tif ( \'cats_to_convert[]\' == field[i].name )\n\t\t\t\tfield[i].checked = false;\n\t\t}\n\t\tcheckflag = \'false\';\n\t\treturn \'");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Check All", "default");
        echo(gVars.webEnv, "\';\n\t}\n}\n\n//  -->\n</script>\n");
        echo(
                gVars.webEnv,
                "<form name=\"formlist\" id=\"formlist\" action=\"admin.php?import=wp-cat2tag&amp;step=2\" method=\"post\">\n\t\t<p><input type=\"button\" class=\"button-secondary\" value=\"" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Check All", "default") + "\"" + " onClick=\"this.value=check_all_rows()\"></p>");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-cat2tag", "_wpnonce", true, true);
        echo(gVars.webEnv, "<ul style=\"list-style:none\">");
        hier = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts)))._get_term_hierarchy("category");

        for (Map.Entry javaEntry95 : this.all_categories.entrySet()) {
            category = (StdClass) javaEntry95.getValue();
            category = (StdClass) (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).sanitize_term(category, "category", "display");

            if (equal(intval(StdClass.getValue(category, "parent")), 0)) {
                echo(gVars.webEnv,
                    "<li><label><input type=\"checkbox\" name=\"cats_to_convert[]\" value=\"" + strval(StdClass.getValue(category, "term_id")) + "\" /> " + StdClass.getValue(category, "name") + " (" +
                    StdClass.getValue(category, "count") + ")</label>");

                if (isset(hier.getValue(StdClass.getValue(category, "term_id")))) {
                    this._category_children(category, hier);
                }

                echo(gVars.webEnv, "</li>");
            }
        }

        echo(gVars.webEnv, "</ul>");
        echo(
            gVars.webEnv,
            "<p class=\"submit\"><input type=\"submit\" name=\"submit\" class=\"button\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Convert Tags", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
    }

    public void _category_children(StdClass parent, Array<Object> hier) {
        StdClass child;
        Object child_id = null;
        echo(gVars.webEnv, "<ul style=\"list-style:none\">");

        for (Map.Entry javaEntry96 : (Set<Map.Entry>) hier.getArrayValue(StdClass.getValue(parent, "term_id")).entrySet()) {
            child_id = javaEntry96.getValue();
            child = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(child_id, gConsts.getOBJECT(), "raw");
            echo(
                    gVars.webEnv,
                    "<li><label><input type=\"checkbox\" name=\"cats_to_convert[]\" value=\"" + strval(StdClass.getValue(child, "term_id")) + "\" /> " + StdClass.getValue(child, "name") + " (" +
                    StdClass.getValue(child, "count") + ")</label>");

            if (isset(hier.getValue(StdClass.getValue(child, "term_id")))) {
                this._category_children(child, hier);
            }

            echo(gVars.webEnv, "</li>");
        }

        echo(gVars.webEnv, "</ul>");
    }

    public boolean _category_exists(int cat_id) {
        Object maybe_exists;
        cat_id = cat_id;
        maybe_exists = getIncluded(TaxonomyPage.class, gVars, gConsts).category_exists(cat_id);

        if (booleanval(maybe_exists)) {
            return true;
        } else {
            return false;
        }
    }

    public void convert_them() {
        Array<Object> hier = new Array<Object>();
        int cat_id = 0;
        StdClass category;
        Array<Object> id = new Array<Object>();
        Array<Object> posts = new Array<Object>();
        Object post = null;
        Array<String> tt_ids = new Array<String>();
        Array<Object> terms = new Array<Object>();
        Object term = null;

        if ((!isset(gVars.webEnv._POST.getValue("cats_to_convert")) || !is_array(gVars.webEnv._POST.getValue("cats_to_convert"))) && empty(this.categories_to_convert)) {
            echo(gVars.webEnv, "<div class=\"narrow\">");
            echo(
                    gVars.webEnv,
                    "<p>" +
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Uh, oh. Something didn&#8217;t work. Please <a href=\"%s\">try again</a>.", "default"),
                        "admin.php?import=wp-cat2tag") + "</p>");
            echo(gVars.webEnv, "</div>");

            return;
        }

        if (empty(this.categories_to_convert)) {
            this.categories_to_convert = (Array<Object>) gVars.webEnv._POST.getValue("cats_to_convert");
        }

        hier = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts)))._get_term_hierarchy("category");
        echo(gVars.webEnv, "<ul>");

        for (Map.Entry javaEntry97 : new Array<Object>(this.categories_to_convert).entrySet()) {
            cat_id = intval(javaEntry97.getValue());

            //			cat_id = intval(cat_id);
            echo(gVars.webEnv, "<li>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Converting category #%s ... ", "default"), cat_id));

            if (!this._category_exists(cat_id)) {
                getIncluded(L10nPage.class, gVars, gConsts)._e("Category doesn\'t exist!", "default");
            } else {
                category = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat_id, gConsts.getOBJECT(), "raw");

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).tag_exists(gVars.wpdb.escape(strval(StdClass.getValue(category, "name")))))) {
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Category is already a tag.", "default");
                    echo(gVars.webEnv, "</li>");

                    continue;
                }

				// If the category is the default, leave category in place and create tag.
                if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category"), StdClass.getValue(category, "term_id"))) {
                    id = (Array<Object>) (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(
                            StdClass.getValue(category, "name"),
                            "post_tag",
                            new Array<Object>(new ArrayEntry<Object>("slug", StdClass.getValue(category, "slug"))));

                    String idStr = strval(id.getValue("term_taxonomy_id"));
                    posts = (Array<Object>) (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).get_objects_in_term(StdClass.getValue(
                                category,
                                "term_id"), "category", new Array<Object>());

                    for (Map.Entry javaEntry98 : posts.entrySet()) {
                        post = javaEntry98.getValue();

                        if (!booleanval(gVars.wpdb.get_var("SELECT object_id FROM " + gVars.wpdb.term_relationships + " WHERE object_id = \'" + post + "\' AND term_taxonomy_id = \'" + idStr + "\'"))) {
                            gVars.wpdb.query("INSERT INTO " + gVars.wpdb.term_relationships + " (object_id, term_taxonomy_id) VALUES (\'" + post + "\', \'" + idStr + "\')");
                        }

                        getIncluded(PostPage.class, gVars, gConsts).clean_post_cache(post);
                    }
                } else {
                    tt_ids = gVars.wpdb.get_col(
                                "SELECT term_taxonomy_id FROM " + gVars.wpdb.term_taxonomy + " WHERE term_id = \'" + StdClass.getValue(category, "term_id") + "\' AND taxonomy = \'category\'");

                    if (booleanval(tt_ids)) {
                        posts = gVars.wpdb.get_col("SELECT object_id FROM " + gVars.wpdb.term_relationships + " WHERE term_taxonomy_id IN (" + Strings.join(",", tt_ids) + ") GROUP BY object_id");

                        for (Map.Entry javaEntry99 : new Array<Object>(posts).entrySet()) {
                            post = javaEntry99.getValue();
                            getIncluded(PostPage.class, gVars, gConsts).clean_post_cache(post);
                        }
                    }

					// Change the category to a tag.
                    gVars.wpdb.query(
                        "UPDATE " + gVars.wpdb.term_taxonomy + " SET taxonomy = \'post_tag\' WHERE term_id = \'" + StdClass.getValue(category, "term_id") + "\' AND taxonomy = \'category\'");
                    terms = gVars.wpdb.get_col("SELECT term_id FROM " + gVars.wpdb.term_taxonomy + " WHERE parent = \'" + StdClass.getValue(category, "term_id") + "\' AND taxonomy = \'category\'");

                    for (Map.Entry javaEntry100 : new Array<Object>(terms).entrySet()) {
                        term = javaEntry100.getValue();
                        getIncluded(CategoryPage.class, gVars, gConsts).clean_category_cache(term);
                    }

					// Set all parents to 0 (root-level) if their parent was the converted tag
                    gVars.wpdb.query("UPDATE " + gVars.wpdb.term_taxonomy + " SET parent = 0 WHERE parent = \'" + StdClass.getValue(category, "term_id") + "\' AND taxonomy = \'category\'");
                }

				// Clean the cache
                getIncluded(CategoryPage.class, gVars, gConsts).clean_category_cache(StdClass.getValue(category, "term_id"));
                
                getIncluded(L10nPage.class, gVars, gConsts)._e("Converted successfully.", "default");
            }

            echo(gVars.webEnv, "</li>");
        }

        echo(gVars.webEnv, "</ul>");
        echo(gVars.webEnv,
            "<p>" +
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("We&#8217;re all done here, but you can always <a href=\"%s\">convert more</a>.", "default"), "admin.php?import=wp-cat2tag") +
            "</p>");
    }

    public void init() {
        int step = 0;
        step = (isset(gVars.webEnv._GET.getValue("step"))
            ? intval(gVars.webEnv._GET.getValue("step"))
            : 1);
        this.header();

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
            echo(gVars.webEnv, "<div class=\"narrow\">");
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default") + "</p>");
            echo(gVars.webEnv, "</div>");
        } else {
            if (step > 1) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-cat2tag", "_wpnonce");
            }

            switch (step) {
            case 1: {
                this.welcome();

                break;
            }

            case 2: {
                this.convert_them();

                break;
            }
            }
        }

        this.footer();
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
