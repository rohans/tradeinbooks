/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_tag_formPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
public class Edit_tag_formPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_tag_formPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/edit-tag-form.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_tag_form";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block1");
        gVars.webEnv = webEnv;

        if (!empty(gVars.tag_ID)) {
            gVars.heading = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Tag", "default");
            gVars.submit_text = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Tag", "default");
            gVars.form = "<form name=\"edittag\" id=\"edittag\" method=\"post\" action=\"edit-tags.php\" class=\"validate\">";
            gVars.action = "editedtag";
            gVars.nonce_action = "update-tag_" + strval(gVars.tag_ID);
            getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_tag_form_pre", gVars.tag);
        } else {
            gVars.heading = getIncluded(L10nPage.class, gVars, gConsts).__("Add Tag", "default");
            gVars.submit_text = getIncluded(L10nPage.class, gVars, gConsts).__("Add Tag", "default");
            gVars.form = "<form name=\"addtag\" id=\"addtag\" method=\"post\" action=\"edit-tags.php\" class=\"add:the-list: validate\">";
            gVars.action = "addtag";
            gVars.nonce_action = "add-tag";
            getIncluded(PluginPage.class, gVars, gConsts).do_action("add_tag_form_pre", gVars.tag);
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block2");
        echo(gVars.webEnv, gVars.heading);

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block3");
        echo(gVars.webEnv, gVars.form);

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block4");
        echo(gVars.webEnv, gVars.action);

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block5");
        echo(gVars.webEnv, is_null(gVars.tag)
            ? ""
            : ((StdClass) gVars.tag).fields.getValue("term_id"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block6");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_original_referer_field(true, "previous");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field(gVars.nonce_action, "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Tag name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block8");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(is_null(gVars.tag)
                    ? ""
                    : ((StdClass) gVars.tag).fields.getValue("name"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("The name is how the tag appears on your site.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Tag slug", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block11");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(is_null(gVars.tag)
                    ? ""
                    : ((StdClass) gVars.tag).fields.getValue("slug"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
            "The &#8220;slug&#8221; is the URL-friendly version of the name. It is usually all lowercase and contains only letters, numbers, and hyphens.",
            "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block13");
        echo(gVars.webEnv, gVars.submit_text);

        /* Start of block */
        super.startBlock("__wp_admin_edit_tag_form_block14");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_tag_form", gVars.tag);

        return DEFAULT_VAL;
    }
}
