/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_category_formPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;


@Controller
@Scope("request")
public class Edit_category_formPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_category_formPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/edit-category-form.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_category_form";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block1");
        gVars.webEnv = webEnv;

        if (!empty(gVars.cat_ID)) {
            gVars.heading = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Category", "default");
            gVars.submit_text = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Category", "default");
            gVars.form = "<form name=\"editcat\" id=\"editcat\" method=\"post\" action=\"categories.php\" class=\"validate\">";
            gVars.action = "editedcat";
            gVars.nonce_action = "update-category_" + strval(gVars.cat_ID);
            getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_category_form_pre", gVars.category);
        } else {
            gVars.heading = getIncluded(L10nPage.class, gVars, gConsts).__("Add Category", "default");
            gVars.submit_text = getIncluded(L10nPage.class, gVars, gConsts).__("Add Category", "default");
            gVars.form = "<form name=\"addcat\" id=\"addcat\" method=\"post\" action=\"categories.php\" class=\"add:the-list: validate\">";
            gVars.action = "addcat";
            gVars.nonce_action = "add-category";
            getIncluded(PluginPage.class, gVars, gConsts).do_action("add_category_form_pre", gVars.category);
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block2");
        echo(gVars.webEnv, gVars.heading);

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block3");
        echo(gVars.webEnv, gVars.form);

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block4");
        echo(gVars.webEnv, gVars.action);

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block5");
        echo(gVars.webEnv, is_null(gVars.category)
            ? ""
            : StdClass.getValue(gVars.category, "term_id"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block6");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field(gVars.nonce_action, "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Category Name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block8");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(is_null(gVars.category)
                    ? ""
                    : StdClass.getValue(gVars.category, "name"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("The name is used to identify the category almost everywhere, for example under the post or in the category widget.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Category Slug", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block11");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(is_null(gVars.category)
                    ? ""
                    : StdClass.getValue(gVars.category, "slug"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
            "The &#8220;slug&#8221; is the URL-friendly version of the name. It is usually all lowercase and contains only letters, numbers, and hyphens.",
            "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Category Parent", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block14");
        getIncluded(Category_templatePage.class, gVars, gConsts).wp_dropdown_categories(
            "hide_empty=0&name=category_parent&orderby=name&selected=" + (is_null(gVars.category)
            ? ""
            : StdClass.getValue(gVars.category, "parent")) + "&hierarchical=1&show_option_none=" + getIncluded(L10nPage.class, gVars, gConsts).__("None", "default"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Categories, unlike tags, can have a hierarchy. You might have a Jazz category, and under that have children categories for Bebop and Big Band. Totally optional.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Description", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block17");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(is_null(gVars.category)
                    ? ""
                    : StdClass.getValue(gVars.category, "description")), strval(0)));

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("The description is not prominent by default, however some themes may show it.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block19");
        echo(gVars.webEnv, gVars.submit_text);

        /* Start of block */
        super.startBlock("__wp_admin_edit_category_form_block20");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_category_form", gVars.category);

        return DEFAULT_VAL;
    }
}
