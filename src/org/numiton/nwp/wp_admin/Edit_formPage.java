/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_formPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Edit_formPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_formPage.class.getName());
    public Object rows;
    public String refby;

    @Override
    @RequestMapping("/wp-admin/edit-form.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_form";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block1");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Write Post", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block2");

        if (isset(gVars.mode) && equal("bookmarklet", gVars.mode)) {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"mode\" value=\"bookmarklet\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block3");
        echo(gVars.webEnv, gVars.user_ID);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Help on titles", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block6");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_title"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Help on categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block9");
        getIncluded(TemplatePage.class, gVars, gConsts).dropdown_categories(intval(StdClass.getValue(gVars.post, "post_category")), 0, new Array<Object>());

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Help with post field", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Post", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block12");
        rows = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_post_edit_rows");

        if ((intval(rows) < 3) || (intval(rows) > 100)) {
            rows = 10;
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block13");
        echo(gVars.webEnv, rows);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block14");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_content"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block15");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("autosave", "autosavenonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block16");
        echo(gVars.webEnv, intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_pingback_flag")));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block17");
        QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "<a href=\"%s\" title=\"Help on trackbacks\"><strong>TrackBack</strong> a <abbr title=\"Universal Resource Locator\">URL</abbr></a>:</label> (Separate multiple <abbr title=\"Universal Resource Locator\">URL</abbr>s with spaces.)",
                        "default"),
                "http://wordpress.org/docs/reference/post/#trackback");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save as Draft", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save as Private", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block20");

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            echo(gVars.webEnv, "\t<input name=\"publish\" type=\"submit\" id=\"publish\" tabindex=\"6\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Publish", "default");
            echo(gVars.webEnv, "\" class=\"button button-highlighted\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block21");

        if (!equal("bookmarklet", gVars.mode)) {
            echo(
                gVars.webEnv,
                "<input name=\"advanced\" type=\"submit\" id=\"advancededit\" tabindex=\"7\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Advanced Editing", "default") + "\" />");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block22");

        if (booleanval(refby = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer())) {
            echo(gVars.webEnv, URL.urlencode(refby));
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_block23");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("simple_edit_form", "");

        return DEFAULT_VAL;
    }
}
