/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_writingPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Options_writingPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_writingPage.class.getName());
    public Array<Object> link_categories;

    @Override
    @RequestMapping("/wp-admin/options-writing.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_writing";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Writing Settings", "default");
        gVars.parent_file = "options-general.php";
        include(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Writing Settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Size of the post box", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block5");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("default_post_edit_rows");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("lines", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Formatting", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block8");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("use_smilies")));

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Convert emoticons like <code>:-)</code> and <code>:-P</code> to graphics on display", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block10");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("use_balanceTags")));

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress should correct invalidly nested XHTML automatically", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Default Post Category", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block13");
        gVars.categories = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("get=all");

        for (Map.Entry javaEntry292 : gVars.categories.entrySet()) {
            gVars.category = (StdClass) javaEntry292.getValue();
            gVars.category = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).sanitize_category(gVars.category, "display");

            if (equal(StdClass.getValue(gVars.category, "term_id"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category"))) {
                gVars.selected = " selected=\'selected\'";
            } else {
                gVars.selected = "";
            }

            echo(gVars.webEnv, "\n\t<option value=\'" + StdClass.getValue(gVars.category, "term_id") + "\' " + strval(gVars.selected) + ">" + StdClass.getValue(gVars.category, "name") + "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block14");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Default Link Category", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block15");
        link_categories = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("link_category", "get=all");

        for (Map.Entry javaEntry293 : link_categories.entrySet()) {
            gVars.category = (StdClass) javaEntry293.getValue();
            gVars.category = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term(gVars.category, "link_category", "display");

            if (equal(StdClass.getValue(gVars.category, "term_id"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category"))) {
                gVars.selected = " selected=\'selected\'";
            } else {
                gVars.selected = "";
            }

            echo(gVars.webEnv, "\n\t<option value=\'" + StdClass.getValue(gVars.category, "term_id") + "\' " + strval(gVars.selected) + ">" + StdClass.getValue(gVars.category, "name") + "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Post via e-mail", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block17");
        QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "To post to nWordPress by e-mail you must set up a secret e-mail account with POP3 access. Any mail received at this address will be posted, so it&#8217;s a good idea to keep this address very secret. Here are three random strings you could use: <code>%s</code>, <code>%s</code>, <code>%s</code>.",
                        "default"),
                getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12),
                getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12),
                getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(12));

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Mail Server", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block19");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("mailserver_url");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Port", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block21");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("mailserver_port");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block22");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Login Name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block23");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("mailserver_login");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block24");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Password", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block25");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("mailserver_pass");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Default Mail Category", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block27");

        //Alreay have $categories from default_category
        for (Map.Entry javaEntry294 : gVars.categories.entrySet()) {
            gVars.category = (StdClass) javaEntry294.getValue();
            gVars.category = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).sanitize_category(gVars.category, "display");

            if (equal(StdClass.getValue(gVars.category, "cat_ID"), getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_email_category"))) {
                gVars.selected = " selected=\'selected\'";
            } else {
                gVars.selected = "";
            }

            echo(gVars.webEnv, "\n\t<option value=\'" + StdClass.getValue(gVars.category, "cat_ID") + "\' " + strval(gVars.selected) + ">" + StdClass.getValue(gVars.category, "cat_name") +
                "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Update Services", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block29");

        if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_public"))) {
            echo(gVars.webEnv, "\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e(
                    "When you publish a new post, nWordPress automatically notifies the following site update services. For more about this, see <a href=\"http://codex.wordpress.org/Update_Services\">Update Services</a> on the Codex. Separate multiple service <abbr title=\"Universal Resource Locator\">URL</abbr>s with line breaks.",
                    "default");
            echo(gVars.webEnv, "</p>\n\n<textarea name=\"ping_sites\" id=\"ping_sites\" style=\"width: 98%;\" rows=\"3\" cols=\"50\">");
            getIncluded(FunctionsPage.class, gVars, gConsts).form_option("ping_sites");
            echo(gVars.webEnv, "</textarea>\n\n");
        } else {
            echo(gVars.webEnv, "\n\t<p>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "nWordPress is not notifying any <a href=\"http://codex.wordpress.org/Update_Services\">Update Services</a> because of your blog\'s <a href=\"%s\">privacy settings</a>.",
                            "default"),
                    "options-privacy.php");
            echo(gVars.webEnv, "</p>\n\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_writing_block31");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
