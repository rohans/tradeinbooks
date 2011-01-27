/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Link_categoryPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.MiscPage;
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
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Link_categoryPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Link_categoryPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/link-category.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/link_category";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_link_category_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action"), new ArrayEntry<Object>("cat")));

        {
            int javaSwitchSelector18 = 0;

            if (equal(gVars.action, "addcat")) {
                javaSwitchSelector18 = 1;
            }

            if (equal(gVars.action, "delete")) {
                javaSwitchSelector18 = 2;
            }

            if (equal(gVars.action, "edit")) {
                javaSwitchSelector18 = 3;
            }

            if (equal(gVars.action, "editedcat")) {
                javaSwitchSelector18 = 4;
            }

            switch (javaSwitchSelector18) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-link-category", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_term(gVars.webEnv._POST.getValue("name"), "link_category", gVars.webEnv._POST))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit-link-categories.php?message=1#addcat", 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit-link-categories.php?message=4#addcat", 302);
                }

                System.exit();

                break;
            }

            case 2: {
                gVars.cat_ID = intval(gVars.webEnv._GET.getValue("cat_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-link-category_" + strval(gVars.cat_ID), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                gVars.cat_name = strval(getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_field("name", gVars.cat_ID, "link_category", "display"));

                // Don't delete the default cats.
                if (equal(gVars.cat_ID, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Can&#8217;t delete the <strong>%s</strong> category: this is the default one", "default"), gVars.cat_name),
                            "");
                }

                getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_term(gVars.cat_ID, "link_category", new Array<Object>());
                gVars.location = "edit-link-categories.php";

                if (booleanval(gVars.referer = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_original_referer())) {
                    if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.referer, "edit-link-categories.php"))) {
                        gVars.location = gVars.referer;
                    }
                }

                gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 2, gVars.location);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
                System.exit();

                break;
            }

            case 3: {
                gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default");
                gVars.parent_file = "edit.php";
                gVars.submenu_file = "edit-link-categories.php";
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                gVars.cat_ID = intval(gVars.webEnv._GET.getValue("cat_ID"));
                gVars.category = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_to_edit(gVars.cat_ID, "link_category");
                include(gVars, gConsts, Edit_link_category_formPage.class);
                include(gVars, gConsts, Admin_footerPage.class);
                System.exit();

                break;
            }

            case 4: {
                gVars.cat_ID = intval(gVars.webEnv._POST.getValue("cat_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-link-category_" + strval(gVars.cat_ID), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                gVars.location = "edit-link-categories.php";

                if (booleanval(gVars.referer = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_original_referer())) {
                    if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.referer, "edit-link-categories.php"))) {
                        gVars.location = gVars.referer;
                    }
                }

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_update_term(gVars.cat_ID, "link_category", gVars.webEnv._POST))) {
                    gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 3, gVars.location);
                } else {
                    gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 5, gVars.location);
                }

                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
                System.exit();

                break;
            }
            }
        }

        return DEFAULT_VAL;
    }
}
