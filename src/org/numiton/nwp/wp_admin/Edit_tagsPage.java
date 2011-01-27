/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_tagsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.MiscPage;
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
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_tagsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_tagsPage.class.getName());
    public Object ret;
    public int tagsperpage;
    public String searchterms;

    @Override
    @RequestMapping("/wp-admin/edit-tags.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_tags";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_tags_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Tags", "default");
        gVars.parent_file = "edit.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action"), new ArrayEntry<Object>("tag")));

        if (isset(gVars.webEnv._GET.getValue("deleteit")) && isset(gVars.webEnv._GET.getValue("delete_tags"))) {
            gVars.action = "bulk-delete";
        }

        {
            int javaSwitchSelector7 = 0;

            if (equal(gVars.action, "addtag")) {
                javaSwitchSelector7 = 1;
            }

            if (equal(gVars.action, "delete")) {
                javaSwitchSelector7 = 2;
            }

            if (equal(gVars.action, "bulk-delete")) {
                javaSwitchSelector7 = 3;
            }

            if (equal(gVars.action, "edit")) {
                javaSwitchSelector7 = 4;
            }

            if (equal(gVars.action, "editedtag")) {
                javaSwitchSelector7 = 5;
            }

            switch (javaSwitchSelector7) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-tag", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                ret = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_term(gVars.webEnv._POST.getValue("name"), "post_tag", gVars.webEnv._POST);

                if (booleanval(ret) && !getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ret)) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit-tags.php?message=1#addtag", 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit-tags.php?message=4#addtag", 302);
                }

                System.exit();

                break;
            }

            case 2: {
                gVars.tag_ID = intval(gVars.webEnv._GET.getValue("tag_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-tag_" + strval(gVars.tag_ID), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_term(gVars.tag_ID, "post_tag", new Array<Object>());
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit-tags.php?message=2", 302);
                System.exit();

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-tags", "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                gVars.tags = gVars.webEnv._GET.getArrayValue("delete_tags");

                for (Map.Entry javaEntry23 : gVars.tags.entrySet()) {
                    gVars.tag_ID = intval(javaEntry23.getValue());
                    getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_term(gVars.tag_ID, "post_tag", new Array<Object>());
                }

                gVars.location = "edit-tags.php";

                if (booleanval(gVars.referer = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer())) {
                    if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.referer, "edit-tags.php"))) {
                        gVars.location = gVars.referer;
                    }
                }

                gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 6, gVars.location);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
                System.exit();

                break;
            }

            case 4: {
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                gVars.tag_ID = intval(gVars.webEnv._GET.getValue("tag_ID"));
                gVars.tag = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(gVars.tag_ID, "post_tag", gConsts.getOBJECT(), "edit");
                include(gVars, gConsts, Edit_tag_formPage.class);

                break;
            }

            case 5: {
                gVars.tag_ID = intval(gVars.webEnv._POST.getValue("tag_ID"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-tag_" + strval(gVars.tag_ID), "_wpnonce");

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
                }

                ret = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_update_term(gVars.tag_ID, "post_tag", gVars.webEnv._POST);
                gVars.location = "edit-tags.php";

                if (booleanval(gVars.referer = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_original_referer())) {
                    if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.referer, "edit-tags.php"))) {
                        gVars.location = gVars.referer;
                    }
                }

                if (booleanval(ret) && !getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ret)) {
                    gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 3, gVars.location);
                } else {
                    gVars.location = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", 5, gVars.location);
                }

                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
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

                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-tags", false, new Array<Object>(), false);
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                gVars.messages.putValue(1, getIncluded(L10nPage.class, gVars, gConsts).__("Tag added.", "default"));
                gVars.messages.putValue(2, getIncluded(L10nPage.class, gVars, gConsts).__("Tag deleted.", "default"));
                gVars.messages.putValue(3, getIncluded(L10nPage.class, gVars, gConsts).__("Tag updated.", "default"));
                gVars.messages.putValue(4, getIncluded(L10nPage.class, gVars, gConsts).__("Tag not added.", "default"));
                gVars.messages.putValue(5, getIncluded(L10nPage.class, gVars, gConsts).__("Tag not updated.", "default"));
                gVars.messages.putValue(6, getIncluded(L10nPage.class, gVars, gConsts).__("Tags deleted.", "default"));
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

                echo(gVars.webEnv, "\n<div class=\"wrap\">\n\n<form id=\"posts-filter\" action=\"\" method=\"get\">\n");

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    echo(gVars.webEnv, "\t<h2>");
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Manage Tags (<a href=\"%s\">add new</a>)", "default"), "#addtag");
                    echo(gVars.webEnv, " </h2>\n");
                } else {
                    echo(gVars.webEnv, "\t<h2>");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("Manage Tags", "default");
                    echo(gVars.webEnv, " </h2>\n");
                }

                echo(gVars.webEnv, "\n<p id=\"post-search\">\n\t<input type=\"text\" id=\"post-search-input\" name=\"s\" value=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("s")))));
                echo(gVars.webEnv, "\" />\n\t<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Search Tags", "default");
                echo(gVars.webEnv, "\" class=\"button\" />\n</p>\n\n<br class=\"clear\" />\n\n<div class=\"tablenav\">\n\n");
                gVars.pagenum = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("pagenum"));

                if (empty(gVars.pagenum)) {
                    gVars.pagenum = 1;
                }

                if (!booleanval(tagsperpage) || (tagsperpage < 0)) {
                    tagsperpage = 20;
                }

                gVars.page_links = strval(
                        getIncluded(General_templatePage.class, gVars, gConsts).paginate_links(
                            new Array<Object>(
                                new ArrayEntry<Object>("base", getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("pagenum", "%#%")),
                                new ArrayEntry<Object>("format", ""),
                                new ArrayEntry<Object>("total", Math.ceil(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_count_terms("post_tag", new Array<Object>()) / floatval(tagsperpage))),
                                new ArrayEntry<Object>("current", gVars.pagenum))));

                if (booleanval(gVars.page_links)) {
                    echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
                }

                echo(gVars.webEnv, "\n<div class=\"alignleft\">\n<input type=\"submit\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");
                echo(gVars.webEnv, "\" name=\"deleteit\" class=\"button-secondary delete\" />\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-tags", "_wpnonce", true, true);
                echo(
                        gVars.webEnv,
                        "</div>\n\n<br class=\"clear\" />\n</div>\n\n<br class=\"clear\" />\n\n<table class=\"widefat\">\n\t<thead>\n\t<tr>\n\t<th scope=\"col\" class=\"check-column\"><input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" /></th>\n        <th scope=\"col\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");
                echo(gVars.webEnv, "</th>\n        <th scope=\"col\" class=\"num\" style=\"width: 90px\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Posts", "default");
                echo(gVars.webEnv, "</th>\n\t</tr>\n\t</thead>\n\t<tbody id=\"the-list\" class=\"list:tag\">\n");
                searchterms = Strings.trim(strval(gVars.webEnv._GET.getValue("s")));
                gVars.count = getIncluded(TemplatePage.class, gVars, gConsts).tag_rows(gVars.pagenum, tagsperpage, searchterms);
                echo(gVars.webEnv, "\t</tbody>\n</table>\n</form>\n\n<div class=\"tablenav\">\n\n");

                if (booleanval(gVars.page_links)) {
                    echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + gVars.page_links + "</div>");
                }

                echo(gVars.webEnv, "<br class=\"clear\" />\n</div>\n<br class=\"clear\" />\n\n</div>\n\n");

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    echo(gVars.webEnv, "\n<br />\n");
                    include(gVars, gConsts, Edit_tag_formPage.class);
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
