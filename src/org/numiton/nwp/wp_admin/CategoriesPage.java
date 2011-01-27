/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CategoriesPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class CategoriesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CategoriesPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/categories.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/categories";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_categories_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default");
        gVars.parent_file = "edit.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action"), new ArrayEntry<Object>("cat")));

        if (isset(gVars.webEnv._GET.getValue("deleteit")) && isset(gVars.webEnv._GET.getValue("delete"))) {
            gVars.action = "bulk-delete";
        }

        {
            int javaSwitchSelector2 = 0;

            if (equal(gVars.action, "addcat")) {
                javaSwitchSelector2 = 1;
            }

            if (equal(gVars.action, "delete")) {
                javaSwitchSelector2 = 2;
            }

            if (equal(gVars.action, "bulk-delete")) {
                javaSwitchSelector2 = 3;
            }

            if (equal(gVars.action, "edit")) {
                javaSwitchSelector2 = 4;
            }

            if (equal(gVars.action, "editedcat")) {
                javaSwitchSelector2 = 5;
            }

            switch (javaSwitchSelector2) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-category", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_category(gVars.webEnv._POST, false))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("categories.php?message=1#addcat", 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("categories.php?message=4#addcat", 302);
                }

                System.exit();

                break;
            }

            case 2: {
                gVars.cat_ID = intval(gVars.webEnv._GET.getValue("cat_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-category_" + strval(gVars.cat_ID), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                gVars.cat_name = getIncluded(CategoryPage.class, gVars, gConsts).get_catname(gVars.cat_ID);

                // Don't delete the default cats.
                if (equal(gVars.cat_ID, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Can&#8217;t delete the <strong>%s</strong> category: this is the default one", "default"), gVars.cat_name),
                            "");
                }

                getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_category(gVars.cat_ID);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("categories.php?message=2", 302);
                System.exit();

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-categories", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to delete categories.", "default"), "");
                }

                for (Map.Entry javaEntry4 : new Array<Object>(gVars.webEnv._GET.getValue("delete")).entrySet()) {
                    gVars.cat_ID = intval(javaEntry4.getValue());
                    gVars.cat_name = getIncluded(CategoryPage.class, gVars, gConsts).get_catname(gVars.cat_ID);

                    // Don't delete the default cats.
                    if (equal(gVars.cat_ID, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category"))) {
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Can&#8217;t delete the <strong>%s</strong> category: this is the default one", "default"),
                                    gVars.cat_name),
                                "");
                    }

                    getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_category(gVars.cat_ID);
                }

                gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer();
                gVars.sendback = QRegExPerl.preg_replace("|[^a-z0-9-~+_.?#=&;,/:]|i", "", gVars.sendback);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.sendback, 302);
                System.exit();

                break;
            }

            case 4: {
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                gVars.cat_ID = intval(gVars.webEnv._GET.getValue("cat_ID"));
                gVars.category = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_category_to_edit(gVars.cat_ID);
                include(gVars, gConsts, Edit_category_formPage.class);

                break;
            }

            case 5: {
                gVars.cat_ID = intval(gVars.webEnv._POST.getValue("cat_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-category_" + strval(gVars.cat_ID), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_update_category(gVars.webEnv._POST))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("categories.php?message=3", 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("categories.php?message=5", 302);
                }

                System.exit();

                break;
            }

            default: {
                if (!empty(gVars.webEnv._GET.getValue("_wp_http_referer"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                            getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(
                                new Array<Object>(new ArrayEntry<Object>("_wp_http_referer"), new ArrayEntry<Object>("_wpnonce")),
                                Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())),
                            302);
                    System.exit();
                }

                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-categories", false, new Array<Object>(), false);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                gVars.messages.putValue(1, getIncluded(L10nPage.class, gVars, gConsts).__("Category added.", "default"));
                gVars.messages.putValue(2, getIncluded(L10nPage.class, gVars, gConsts).__("Category deleted.", "default"));
                gVars.messages.putValue(3, getIncluded(L10nPage.class, gVars, gConsts).__("Category updated.", "default"));
                gVars.messages.putValue(4, getIncluded(L10nPage.class, gVars, gConsts).__("Category not added.", "default"));
                gVars.messages.putValue(5, getIncluded(L10nPage.class, gVars, gConsts).__("Category not updated.", "default"));
                echo(gVars.webEnv, "\n");

                if (isset(gVars.webEnv._GET.getValue("message"))) {
                    echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p>");
                    echo(gVars.webEnv, gVars.messages.getValue(gVars.webEnv._GET.getValue("message")));
                    echo(gVars.webEnv, "</p></div>\n");
                    gVars.webEnv._SERVER.putValue(
                        "REQUEST_URI",
                        getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(new ArrayEntry<Object>("message")), gVars.webEnv.getRequestURI()));
                } else {
                }

                echo(gVars.webEnv, "\n<div class=\"wrap\">\n<form id=\"posts-filter\" action=\"\" method=\"get\">\n");

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    echo(gVars.webEnv, "\t<h2>");
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Manage Categories (<a href=\"%s\">add new</a>)", "default"), "#addcat");
                    echo(gVars.webEnv, " </h2>\n");
                } else {
                    echo(gVars.webEnv, "\t<h2>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Manage Categories", "default");
                    echo(gVars.webEnv, " </h2>\n");
                }

                echo(gVars.webEnv, "\n<p id=\"post-search\">\n\t<input type=\"text\" id=\"post-search-input\" name=\"s\" value=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("s")))));
                echo(gVars.webEnv, "\" />\n\t<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Search Categories", "default");
                echo(gVars.webEnv, "\" class=\"button\" />\n</p>\n\n<br class=\"clear\" />\n\n<div class=\"tablenav\">\n\n<div class=\"alignleft\">\n<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");
                echo(gVars.webEnv, "\" name=\"deleteit\" class=\"button-secondary delete\" />\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-categories", "_wpnonce", true, true);
                echo(
                        gVars.webEnv,
                        "</div>\n\n<br class=\"clear\" />\n</div>\n\n<br class=\"clear\" />\n\n<table class=\"widefat\">\n\t<thead>\n\t<tr>\n\t\t<th scope=\"col\" class=\"check-column\"><input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" /></th>\n        <th scope=\"col\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");
                echo(gVars.webEnv, "</th>\n        <th scope=\"col\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Description", "default");
                echo(gVars.webEnv, "</th>\n        <th scope=\"col\" class=\"num\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Posts", "default");
                echo(gVars.webEnv, "</th>\n\t</tr>\n\t</thead>\n\t<tbody id=\"the-list\" class=\"list:cat\">\n");
                getIncluded(TemplatePage.class, gVars, gConsts).cat_rows(0, 0, 0);
                echo(gVars.webEnv, "\t</tbody>\n</table>\n</form>\n\n<div class=\"tablenav\">\n<br class=\"clear\" />\n</div>\n<br class=\"clear\" />\n\n</div>\n\n");

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    echo(gVars.webEnv, "<div class=\"wrap\">\n<p>");
                    QStrings.printf(
                            gVars.webEnv,
                            getIncluded(L10nPage.class, gVars, gConsts).__(
                                    "<strong>Note:</strong><br />Deleting a category does not delete the posts in that category. Instead, posts that were only assigned to the deleted category are set to the category <strong>%s</strong>.",
                                    "default"),
                            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                                "the_category",
                                getIncluded(CategoryPage.class, gVars, gConsts).get_catname(intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category")))));
                    echo(gVars.webEnv, "</p>\n<p>");
                    QStrings.printf(
                        gVars.webEnv,
                        getIncluded(L10nPage.class, gVars, gConsts).__("Categories can be selectively converted to tags using the <a href=\"%s\">category to tag converter</a>.", "default"),
                        "admin.php?import=wp-cat2tag");
                    echo(gVars.webEnv, "</p>\n</div>\n\n");
                    include(gVars, gConsts, Edit_category_formPage.class);
                    echo(gVars.webEnv, "\n");
                } else {
                }

                echo(gVars.webEnv, "\n");

                break;
            }
            }
        }

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
