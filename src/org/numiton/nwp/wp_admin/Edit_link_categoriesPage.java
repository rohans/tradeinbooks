/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_link_categoriesPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_link_categoriesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_link_categoriesPage.class.getName());
    public int catsperpage;

    @Override
    @RequestMapping("/wp-admin/edit-link-categories.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_link_categories";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        // Handle bulk deletes
        if (isset(gVars.webEnv._GET.getValue("deleteit")) && isset(gVars.webEnv._GET.getValue("delete"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-link-categories", "_wpnonce");

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
            }

            for (Map.Entry javaEntry12 : new Array<Object>(gVars.webEnv._GET.getValue("delete")).entrySet()) {
                gVars.cat_ID = intval(javaEntry12.getValue());
                gVars.cat_name = strval(getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_field("name", gVars.cat_ID, "link_category", "display"));

                // Don't delete the default cats.
                if (equal(gVars.cat_ID, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Can&#8217;t delete the <strong>%s</strong> category: this is the default one", "default"), gVars.cat_name),
                            "");
                }

                getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_term(gVars.cat_ID, "link_category", new Array<Object>());
            }

            gVars.location = "edit-link-categories.php";

            if (booleanval(gVars.referer = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer())) {
                if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.referer, "edit-link-categories.php"))) {
                    gVars.location = gVars.referer;
                }
            }

            gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 6, gVars.location);
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
            System.exit();
        } else if (!empty(gVars.webEnv._GET.getValue("_wp_http_referer"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(
                        new ArrayEntry<Object>("_wp_http_referer"),
                        new ArrayEntry<Object>("_wpnonce")), Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())), 302);
            System.exit();
        }

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Link Categories", "default");
        gVars.parent_file = "edit.php";
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-categories", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
        requireOnce(gVars, gConsts, Admin_headerPage.class);
        gVars.messages.putValue(1, getIncluded(L10nPage.class, gVars, gConsts).__("Category added.", "default"));
        gVars.messages.putValue(2, getIncluded(L10nPage.class, gVars, gConsts).__("Category deleted.", "default"));
        gVars.messages.putValue(3, getIncluded(L10nPage.class, gVars, gConsts).__("Category updated.", "default"));
        gVars.messages.putValue(4, getIncluded(L10nPage.class, gVars, gConsts).__("Category not added.", "default"));
        gVars.messages.putValue(5, getIncluded(L10nPage.class, gVars, gConsts).__("Category not updated.", "default"));
        gVars.messages.putValue(6, getIncluded(L10nPage.class, gVars, gConsts).__("Categories deleted.", "default"));

        if (isset(gVars.webEnv._GET.getValue("message"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p>");
            echo(gVars.webEnv, gVars.messages.getValue(gVars.webEnv._GET.getValue("message")));
            echo(gVars.webEnv, "</p></div>\n");
            gVars.webEnv._SERVER.putValue(
                "REQUEST_URI",
                getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(new ArrayEntry<Object>("message")), gVars.webEnv.getRequestURI()));
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block2");

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
            echo(gVars.webEnv, "\t<h2>");
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Manage Link Categories (<a href=\"%s\">add new</a>)", "default"), "#addcat");
            echo(gVars.webEnv, " </h2>\n");
        } else {
            echo(gVars.webEnv, "\t<h2>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Manage Link Categories", "default");
            echo(gVars.webEnv, " </h2>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block3");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("s")))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Search Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block5");
        gVars.pagenum = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("pagenum"));

        if (empty(gVars.pagenum)) {
            gVars.pagenum = 1;
        }

        if (!booleanval(catsperpage) || (catsperpage < 0)) {
            catsperpage = 20;
        }

        gVars.page_links = strval(
                getIncluded(General_templatePage.class, gVars, gConsts).paginate_links(
                    new Array<Object>(
                        new ArrayEntry<Object>("base", getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("pagenum", "%#%")),
                        new ArrayEntry<Object>("format", ""),
                        new ArrayEntry<Object>("total", Math.ceil(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_count_terms("link_category", new Array<Object>()) / floatval(catsperpage))),
                        new ArrayEntry<Object>("current", gVars.pagenum))));

        if (booleanval(gVars.page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block7");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-link-categories", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Description", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Links", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block11");
        gVars.start = (gVars.pagenum - floatval(1)) * floatval(catsperpage);
        gVars.args = new Array<Object>(new ArrayEntry<Object>("offset", gVars.start), new ArrayEntry<Object>("number", catsperpage), new ArrayEntry<Object>("hide_empty", 0));

        if (!empty(gVars.webEnv._GET.getValue("s"))) {
            gVars.args.putValue("search", gVars.webEnv._GET.getValue("s"));
        }

        gVars.categories = (Array<StdClass>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("link_category", gVars.args);

        if (booleanval(gVars.categories)) {
            gVars.output = "";

            for (Map.Entry javaEntry13 : gVars.categories.entrySet()) {
                gVars.category = (StdClass) javaEntry13.getValue();
                gVars.category = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term(gVars.category, "link_category", "display");
                gVars.output = strval(gVars.output) + getIncluded(TemplatePage.class, gVars, gConsts).link_cat_row(gVars.category);
            }

            gVars.output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("cat_rows", gVars.output);
            echo(gVars.webEnv, gVars.output);
            gVars.category = null;
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block12");

        if (booleanval(gVars.page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block13");

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
            echo(gVars.webEnv, "<div class=\"wrap\">\n<p>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "<strong>Note:</strong><br />Deleting a category does not delete the links in that category. Instead, links that were only assigned to the deleted category are set to the category <strong>%s</strong>.",
                            "default"),
                    getIncluded(TaxonomyPage.class, gVars, gConsts)
                        .get_term_field("name", intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category")), "link_category", "display"));
            echo(gVars.webEnv, "</p>\n</div>\n\n");
            include(gVars, gConsts, Edit_link_category_formPage.class);
            echo(gVars.webEnv, "\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_categories_block14");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
