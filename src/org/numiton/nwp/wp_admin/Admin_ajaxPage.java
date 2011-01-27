/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Admin_ajaxPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.Wp_configPage;
import org.numiton.nwp.wp_admin.includes.*;
import org.numiton.nwp.wp_admin.includes.AdminPage;
import org.numiton.nwp.wp_admin.includes.BookmarkPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_admin.includes.UserPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.CommentPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ClassHandling;
import com.numiton.DateTime;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Admin_ajaxPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Admin_ajaxPage.class.getName());
    public Array<String> results = new Array<String>();
    public WP_Ajax_Response x;
    public StdClass meta;
    public Array<Object> checked_categories = new Array<Object>();
    public String category_nicename;
    public String slug;
    public int level;
    public String cat_full_name;
    public StdClass _cat;
    public Object term_id;
    public String tag_full_name;
    public Object comment_list_item;
    public Object pid;
    public int now;
    public int mid;
    public Object u;
    public Object nonce_age;
    public boolean do_autosave;
    public boolean do_lock;
    public Array<Object> supplemental = new Array<Object>();
    public Object closed;

    @Override
    @RequestMapping("/wp-admin/admin-ajax.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/admin_ajax";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_admin_ajax_block1");
        gVars.webEnv = webEnv;
        
        gConsts.setDOING_AJAX(true);
        
        requireOnce(gVars, gConsts, Wp_configPage.class);
        requireOnce(gVars, gConsts, AdminPage.class);

        if (!getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
            System.exit("-1");
        }

        if (isset(gVars.webEnv._GET.getValue("action")) && equal("ajax-tag-search", gVars.webEnv._GET.getValue("action"))) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                System.exit("-1");
            }

            gVars.s = strval(gVars.webEnv._GET.getValue("q")); // is this slashed already?

            if (booleanval(Strings.strstr(gVars.s, ","))) {
                System.exit(); // it's a multiple tag insert, we won't find anything
            }

            results = gVars.wpdb.get_col("SELECT name FROM " + gVars.wpdb.terms + " WHERE name LIKE (\'%" + gVars.s + "%\')");
            echo(gVars.webEnv, Strings.join(results, "\n"));
            System.exit();
        }

        gVars.id = (isset(gVars.webEnv._POST.getValue("id"))
            ? intval(gVars.webEnv._POST.getValue("id"))
            : 0);

        {
            int javaSwitchSelector1 = 0;

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-comment")) {
                javaSwitchSelector1 = 1;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-cat")) {
                javaSwitchSelector1 = 2;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-tag")) {
                javaSwitchSelector1 = 3;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-link-cat")) {
                javaSwitchSelector1 = 4;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-link")) {
                javaSwitchSelector1 = 5;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-meta")) {
                javaSwitchSelector1 = 6;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-post")) {
                javaSwitchSelector1 = 7;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "delete-page")) {
                javaSwitchSelector1 = 8;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "dim-comment")) {
                javaSwitchSelector1 = 9;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-category")) {
                javaSwitchSelector1 = 10;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-link-category")) {
                javaSwitchSelector1 = 11;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-cat")) {
                javaSwitchSelector1 = 12;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-link-cat")) {
                javaSwitchSelector1 = 13;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-tag")) {
                javaSwitchSelector1 = 14;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-comment")) {
                javaSwitchSelector1 = 15;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-meta")) {
                javaSwitchSelector1 = 16;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "add-user")) {
                javaSwitchSelector1 = 17;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "autosave")) {
                javaSwitchSelector1 = 18;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "autosave-generate-nonces")) {
                javaSwitchSelector1 = 19;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "closed-postboxes")) {
                javaSwitchSelector1 = 20;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "get-permalink")) {
                javaSwitchSelector1 = 21;
            }

            if (equal(gVars.action = gVars.webEnv._POST.getValue("action"), "sample-permalink")) {
                javaSwitchSelector1 = 22;
            }

            switch (javaSwitchSelector1) {
            case 1: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("delete-comment_" + strval(gVars.id), strval(false), true);

                if (!booleanval((gVars.comment = (StdClass) getIncluded(CommentPage.class, gVars, gConsts).get_comment(gVars.id, gConsts.getOBJECT())))) {
                    System.exit("0");
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    System.exit("-1");
                }

                if (isset(gVars.webEnv._POST.getValue("spam")) && equal(1, gVars.webEnv._POST.getValue("spam"))) {
                    gVars.r = getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(intval(StdClass.getValue(gVars.comment, "comment_ID")), "spam");
                } else {
                    gVars.r = getIncluded(CommentPage.class, gVars, gConsts).wp_delete_comment(intval(StdClass.getValue(gVars.comment, "comment_ID")));
                }

                System.exit(booleanval(gVars.r)
                    ? "1"
                    : "0");

                break;
            }

            case 2: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("delete-category_" + strval(gVars.id), strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_delete_category(intval(gVars.id)))) {
                    System.exit("1");
                } else {
                    System.exit("0");
                }

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("delete-tag_" + strval(gVars.id), strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                if (booleanval(
                            (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_delete_term(intval(gVars.id), "post_tag", new Array<Object>()))) {
                    System.exit("1");
                } else {
                    System.exit("0");
                }

                break;
            }

            case 4:/*
             * Don't delete the default cats. Don't delete the default
             * cats.
             */
             {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("delete-link-category_" + strval(gVars.id), strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                gVars.cat_name = strval(
                        (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).get_term_field("name", intval(gVars.id), "link_category", "display"));

             // Don't delete the default cats.
                if (equal(gVars.id, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_link_category"))) {
                    x = new WP_Ajax_Response(
                            gVars,
                            gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "link-cat"), new ArrayEntry<Object>("id", gVars.id),
                                new ArrayEntry<Object>("data",
                                    new WP_Error(gVars,
                                        gConsts,
                                        "default-link-cat",
                                        QStrings.sprintf(
                                            getIncluded(L10nPage.class, gVars, gConsts).__("Can&#8217;t delete the <strong>%s</strong> category: this is the default one", "default"),
                                            gVars.cat_name)))));
                    x.send();
                }

                gVars.r = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_delete_term(intval(gVars.id), "link_category", new Array<Object>());

                if (!booleanval(gVars.r)) {
                    System.exit("0");
                }

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.r)) {
                    x = new WP_Ajax_Response(
                            gVars,
                            gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "link-cat"), new ArrayEntry<Object>("id", gVars.id), new ArrayEntry<Object>("data", gVars.r)));
                    x.send();
                }

                System.exit("1");

                break;
            }

            case 5: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("delete-bookmark_" + strval(gVars.id), strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
                    System.exit("-1");
                }

                if (getIncluded(BookmarkPage.class, gVars, gConsts).wp_delete_link(intval(gVars.id))) {
                    System.exit("1");
                } else {
                    System.exit("0");
                }

                break;
            }

            case 6: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("delete-meta_" + strval(gVars.id), strval(false), true);

                if (!booleanval(meta = getIncluded(PostPage.class, gVars, gConsts).get_post_meta_by_id(gVars.id))) {
                    System.exit("0");
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(meta, "post_id"))) {
                    System.exit("-1");
                }

                if (booleanval(getIncluded(PostPage.class, gVars, gConsts).delete_meta(intval(StdClass.getValue(meta, "meta_id"))))) {
                    System.exit("1");
                }

                System.exit("0");

                break;
            }

            case 7: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer(strval(gVars.action) + "_" + strval(gVars.id), strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_post", gVars.id)) {
                    System.exit("-1");
                }

                if (booleanval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_delete_post(intval(gVars.id)))) {
                    System.exit("1");
                } else {
                    System.exit("0");
                }

                break;
            }

            case 8: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer(strval(gVars.action) + "_" + strval(gVars.id), strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_page", gVars.id)) {
                    System.exit("-1");
                }

                if (booleanval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_delete_post(intval(gVars.id)))) {
                    System.exit("1");
                } else {
                    System.exit("0");
                }

                break;
            }

            case 9: {
                if (!booleanval((gVars.comment = (StdClass) getIncluded(CommentPage.class, gVars, gConsts).get_comment(gVars.id, gConsts.getOBJECT())))) {
                    System.exit("0");
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    System.exit("-1");
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("moderate_comments")) {
                    System.exit("-1");
                }

                if (equal("unapproved", getIncluded(CommentPage.class, gVars, gConsts).wp_get_comment_status(intval(StdClass.getValue(gVars.comment, "comment_ID"))))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("approve-comment_" + strval(gVars.id), strval(false), true);

                    if (getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(intval(StdClass.getValue(gVars.comment, "comment_ID")), "approve")) {
                        System.exit("1");
                    }
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("unapprove-comment_" + strval(gVars.id), strval(false), true);

                    if (getIncluded(CommentPage.class, gVars, gConsts).wp_set_comment_status(intval(StdClass.getValue(gVars.comment, "comment_ID")), "hold")) {
                        System.exit("1");
                    }
                }

                System.exit("0");

                break;
            }

            case 10: { // On the Fly
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer(gVars.action, strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                gVars.names = Strings.explode(",", strval(gVars.webEnv._POST.getValue("newcat")));

                if (0 > (gVars.parent = intval(gVars.webEnv._POST.getValue("newcat_parent")))) {
                    gVars.parent = 0;
                }

                gVars.post_category = (isset(gVars.webEnv._POST.getValue("post_category"))
                    ? new Array<Object>(gVars.webEnv._POST.getValue("post_category"))
                    : new Array<Object>());
                checked_categories = Array.array_map(new Callback("absint", getIncluded(FunctionsPage.class, gVars, gConsts)), new Array<Object>(gVars.post_category));
                x = new WP_Ajax_Response(gVars, gConsts);

                for (Map.Entry javaEntry1 : gVars.names.entrySet())/*
                 * Do these all at once in a second Do these all at once in
                 * a second
                 */
                 {
                    gVars.cat_name = strval(javaEntry1.getValue());
                    gVars.cat_name = Strings.trim(gVars.cat_name);
                    category_nicename = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(gVars.cat_name, "");

                    if (strictEqual("", category_nicename)) {
                        continue;
                    }

                    gVars.cat_id = strval(getIncluded(TaxonomyPage.class, gVars, gConsts).wp_create_category(gVars.cat_name, gVars.parent));
                    checked_categories.putValue(gVars.cat_id);

                    if (booleanval(gVars.parent)) { // Do these all at once in a second
                        continue;
                    }

                    gVars.category = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(gVars.cat_id, gConsts.getOBJECT(), "raw");
                    OutputControl.ob_start(gVars.webEnv);
                    getIncluded(TemplatePage.class, gVars, gConsts).wp_category_checklist(0, intval(gVars.cat_id), checked_categories);
                    gVars.data = OutputControl.ob_get_contents(gVars.webEnv);
                    OutputControl.ob_end_clean(gVars.webEnv);
                    x.add(
                        new Array<Object>(
                            new ArrayEntry<Object>("what", "category"),
                            new ArrayEntry<Object>("id", gVars.cat_id),
                            new ArrayEntry<Object>("data", gVars.data),
                            new ArrayEntry<Object>("position", -1)));
                }

                if (booleanval(gVars.parent)) { // Foncy - replace the parent and all its children
                    StdClass parent = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(gVars.parent, gConsts.getOBJECT(), "raw");
                    OutputControl.ob_start(gVars.webEnv);
                    getIncluded(TemplatePage.class, gVars, gConsts).dropdown_categories(0, parent, new Array<Object>());
                    gVars.data = OutputControl.ob_get_contents(gVars.webEnv);
                    OutputControl.ob_end_clean(gVars.webEnv);
                    x.add(
                        new Array<Object>(new ArrayEntry<Object>("what", "category"), new ArrayEntry<Object>("id", StdClass.getValue(parent, "term_id")),
                            new ArrayEntry<Object>("old_id", StdClass.getValue(parent, "term_id")), new ArrayEntry<Object>("data", gVars.data), new ArrayEntry<Object>("position", -1)));
                }

                x.send();

                break;
            }

            case 11: { // On the Fly
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer(gVars.action, strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                gVars.names = Strings.explode(",", strval(gVars.webEnv._POST.getValue("newcat")));
                x = new WP_Ajax_Response(gVars, gConsts);

                for (Map.Entry javaEntry2 : gVars.names.entrySet()) {
                    gVars.cat_name = strval(javaEntry2.getValue());
                    gVars.cat_name = Strings.trim(gVars.cat_name);
                    slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(gVars.cat_name, "");

                    if (strictEqual("", slug)) {
                        continue;
                    }

                    Array catArray;

                    if (!booleanval(
                                catArray = (Array) (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).is_term(gVars.cat_name, "link_category"))) {
                        catArray = (Array) (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(
                                gVars.cat_name,
                                "link_category",
                                new Array<Object>());
                    }

                    gVars.cat_id = strval(catArray.getValue("term_id"));
                    gVars.cat_name = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, gVars.cat_name), strval(0));
                    x.add(
                            new Array<Object>(
                                    new ArrayEntry<Object>("what", "link-category"),
                                    new ArrayEntry<Object>("id", gVars.cat_id),
                                    new ArrayEntry<Object>(
                                            "data",
                                            "<li id=\'link-category-" + gVars.cat_id + "\'><label for=\'in-link-category-" + gVars.cat_id + "\' class=\'selectit\'><input value=\'" + gVars.cat_id +
                                            "\' type=\'checkbox\' checked=\'checked\' name=\'link_category[]\' id=\'in-link-category-" + gVars.cat_id + "\'/> " + gVars.cat_name + "</label></li>"),
                                    new ArrayEntry<Object>("position", -1)));
                }

                x.send();

                break;
            }

            case 12: { // From Manage->Categories
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("add-category", strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                if (strictEqual("", Strings.trim(strval(gVars.webEnv._POST.getValue("cat_name"))))) {
                    x = new WP_Ajax_Response(gVars, gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "cat"),
                                new ArrayEntry<Object>("id", new WP_Error(gVars, gConsts, "cat_name", getIncluded(L10nPage.class, gVars, gConsts).__("You did not enter a category name.", "default")))));
                    x.send();
                }

                if (booleanval(getIncluded(TaxonomyPage.class, gVars, gConsts).category_exists(Strings.trim(strval(gVars.webEnv._POST.getValue("cat_name")))))) {
                    x = new WP_Ajax_Response(gVars, gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "cat"),
                                new ArrayEntry<Object>("id",
                                    new WP_Error(gVars,
                                        gConsts,
                                        "cat_exists",
                                        getIncluded(L10nPage.class, gVars, gConsts).__("The category you are trying to create already exists.", "default"),
                                        new Array<Object>(new ArrayEntry<Object>("form-field", "cat_name"))))));
                    x.send();
                }

                gVars.cat = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_category(gVars.webEnv._POST, true);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.cat)) {
                    x = new WP_Ajax_Response(gVars, gConsts, new Array<Object>(new ArrayEntry<Object>("what", "cat"), new ArrayEntry<Object>("id", gVars.cat)));
                    x.send();
                }

                if (!booleanval(gVars.cat) || !booleanval(gVars.cat = getIncluded(CategoryPage.class, gVars, gConsts).get_category(gVars.cat, gConsts.getOBJECT(), "raw"))) {
                    System.exit("0");
                }

                level = 0;
                cat_full_name = strval(((StdClass) gVars.cat).fields.getValue("name"));
                _cat = (StdClass) gVars.cat;

                while (booleanval(StdClass.getValue(_cat, "parent"))) {
                    _cat = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(StdClass.getValue(_cat, "parent"), gConsts.getOBJECT(), "raw");
                    cat_full_name = StdClass.getValue(_cat, "name") + " &#8212; " + cat_full_name;
                    level++;
                }

                cat_full_name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(cat_full_name);
                x = new WP_Ajax_Response(
                        gVars,
                        gConsts,
                        new Array<Object>(new ArrayEntry<Object>("what", "cat"), new ArrayEntry<Object>("id", ((StdClass) gVars.cat).fields.getValue("term_id")),
                            new ArrayEntry<Object>("data", getIncluded(TemplatePage.class, gVars, gConsts)._cat_row(gVars.cat, level, cat_full_name)),
                            new ArrayEntry<Object>("supplemental",
                                new Array<Object>(new ArrayEntry<Object>("name", cat_full_name),
                                    new ArrayEntry<Object>("show-link",
                                        QStrings.sprintf(
                                            getIncluded(L10nPage.class, gVars, gConsts).__("Category <a href=\"#%s\">%s</a> added", "default"),
                                            "cat-" + ((StdClass) gVars.cat).fields.getValue("term_id"),
                                            cat_full_name))))));
                x.send();

                break;
            }

            case 13: { // From Blogroll -> Categories
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("add-link-category", strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                if (strictEqual("", Strings.trim(strval(gVars.webEnv._POST.getValue("name"))))) {
                    x = new WP_Ajax_Response(gVars, gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "link-cat"),
                                new ArrayEntry<Object>("id", new WP_Error(gVars, gConsts, "name", getIncluded(L10nPage.class, gVars, gConsts).__("You did not enter a category name.", "default")))));
                    x.send();
                }

                gVars.r = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(
                        gVars.webEnv._POST.getValue("name"),
                        "link_category",
                        gVars.webEnv._POST);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.r)) {
                    x = new WP_Ajax_Response(gVars, gConsts, new Array<Object>(new ArrayEntry<Object>("what", "link-cat"), new ArrayEntry<Object>("id", gVars.r)));
                    x.send();
                }

                /* Modified by Numiton */
                term_id = Array.extractVar((Array) gVars.r, "term_id", term_id, Array.EXTR_SKIP);
                gVars.term_taxonomy_id = Array.extractVar((Array) gVars.r, "term_taxonomy_id", gVars.term_taxonomy_id, Array.EXTR_SKIP);

                if (!booleanval(gVars.link_cat = getIncluded(TemplatePage.class, gVars, gConsts).link_cat_row(term_id))) {
                    System.exit("0");
                }

                x = new WP_Ajax_Response(
                        gVars,
                        gConsts,
                        new Array<Object>(new ArrayEntry<Object>("what", "link-cat"), new ArrayEntry<Object>("id", term_id), new ArrayEntry<Object>("data", gVars.link_cat)));
                x.send();

                break;
            }

            case 14: { // From Manage->Tags
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("add-tag", strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
                    System.exit("-1");
                }

                if (strictEqual("", Strings.trim(strval(gVars.webEnv._POST.getValue("name"))))) {
                    x = new WP_Ajax_Response(gVars, gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "tag"),
                                new ArrayEntry<Object>("id", new WP_Error(gVars, gConsts, "name", getIncluded(L10nPage.class, gVars, gConsts).__("You did not enter a tag name.", "default")))));
                    x.send();
                }

                gVars.tag = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(
                        gVars.webEnv._POST.getValue("name"),
                        "post_tag",
                        gVars.webEnv._POST);

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.tag)) {
                    x = new WP_Ajax_Response(gVars, gConsts, new Array<Object>(new ArrayEntry<Object>("what", "tag"), new ArrayEntry<Object>("id", gVars.tag)));
                    x.send();
                }

                if (!booleanval(gVars.tag) ||
                        !booleanval(
                            gVars.tag = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).get_term(
                                    ((Array) gVars.tag).getValue("term_id"),
                                    "post_tag",
                                    gConsts.getOBJECT(),
                                    "raw"))) {
                    System.exit("0");
                }

                tag_full_name = strval(((StdClass) gVars.tag).fields.getValue("name"));
                tag_full_name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(tag_full_name);
                x = new WP_Ajax_Response(
                        gVars,
                        gConsts,
                        new Array<Object>(new ArrayEntry<Object>("what", "tag"), new ArrayEntry<Object>("id", ((StdClass) gVars.tag).fields.getValue("term_id")),
                            new ArrayEntry<Object>("data", getIncluded(TemplatePage.class, gVars, gConsts)._tag_row((StdClass) gVars.tag, "")),
                            new ArrayEntry<Object>("supplemental",
                                new Array<Object>(new ArrayEntry<Object>("name", tag_full_name),
                                    new ArrayEntry<Object>("show-link",
                                        QStrings.sprintf(
                                            (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Tag <a href=\"#%s\">%s</a> added", "default"),
                                            "tag-" + ((StdClass) gVars.tag).fields.getValue("term_id"),
                                            tag_full_name))))));
                x.send();

                break;
            }

            case 15: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer(gVars.action, strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.id)) {
                    System.exit("-1");
                }

                gVars.search = (isset(gVars.webEnv._POST.getValue("s"))
                    ? gVars.webEnv._POST.getValue("s")
                    : false);
                gVars.start = (isset(gVars.webEnv._POST.getValue("page"))
                    ? ((intval(gVars.webEnv._POST.getValue("page")) * 25) - 1)
                    : 24);
                gVars.status = strval(isset(gVars.webEnv._POST.getValue("comment_status"))
                        ? gVars.webEnv._POST.getValue("comment_status")
                        : false);
                gVars.mode = (isset(gVars.webEnv._POST.getValue("mode"))
                    ? strval(gVars.webEnv._POST.getValue("mode"))
                    : "detail");
                new ListAssigner<Object>() {
                        public Array<Object> doAssign(Array<Object> srcArray) {
                            if (strictEqual(srcArray, null)) {
                                return null;
                            }

                            gVars.comments = srcArray.getArrayValue(0);
                            gVars.total = srcArray.getValue(1);

                            return srcArray;
                        }
                    }.doAssign(getIncluded(TemplatePage.class, gVars, gConsts)._wp_get_comment_list(gVars.status, strval(gVars.search), gVars.start, 1));

                if (!booleanval(gVars.comments)) {
                    System.exit("1");
                }

                x = new WP_Ajax_Response(gVars, gConsts);

                for (Map.Entry javaEntry3 : new Array<Object>(gVars.comments).entrySet()) {
                    gVars.comment = (StdClass) javaEntry3.getValue();
                    getIncluded(CommentPage.class, gVars, gConsts).get_comment(gVars.comment, gConsts.getOBJECT());
                    OutputControl.ob_start(gVars.webEnv);
                    getIncluded(TemplatePage.class, gVars, gConsts)._wp_comment_row(intval(StdClass.getValue(gVars.comment, "comment_ID")), gVars.mode, strval(false), true);
                    comment_list_item = OutputControl.ob_get_contents(gVars.webEnv);
                    OutputControl.ob_end_clean(gVars.webEnv);
                    x.add(
                        new Array<Object>(
                            new ArrayEntry<Object>("what", "comment"),
                            new ArrayEntry<Object>("id", intval(StdClass.getValue(gVars.comment, "comment_ID"))),
                            new ArrayEntry<Object>("data", comment_list_item)));
                }

                x.send();

                break;
            }

            case 16: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("add-meta", strval(false), true);

                Ref<Integer> c = new Ref<Integer>(0);

                // Modified by Numiton
                pid = intval(gVars.webEnv._POST.getValue("post_id"));

                if (isset(gVars.webEnv._POST.getValue("metakeyselect")) || isset(gVars.webEnv._POST.getValue("metakeyinput"))) {
                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", pid)) {
                        System.exit("-1");
                    }

                    if (equal("#NONE#", gVars.webEnv._POST.getValue("metakeyselect")) && empty(gVars.webEnv._POST.getValue("metakeyinput"))) {
                        System.exit("1");
                    }

                    if (intval(pid) < 0) {
                        now = intval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("timestamp", 1));

                        if (booleanval(
                                        pid = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(
                                                    new Array<Object>(
                                                        new ArrayEntry<Object>("post_title",
                                                            QStrings.sprintf(
                                                                "Draft created on %s at %s",
                                                                DateTime.date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")), now),
                                                                DateTime.date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")), now))))))) {
                            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(pid)) {
                                x = new WP_Ajax_Response(gVars, gConsts, new Array<Object>(new ArrayEntry<Object>("what", "meta"), new ArrayEntry<Object>("data", pid)));
                                x.send();
                            }

                            mid = getIncluded(PostPage.class, gVars, gConsts).add_meta(intval(pid));
                        } else {
                            System.exit("0");
                        }
                    } else if (!booleanval(mid = getIncluded(PostPage.class, gVars, gConsts).add_meta(intval(pid)))) {
                        System.exit("0");
                    }

                    meta = getIncluded(PostPage.class, gVars, gConsts).get_post_meta_by_id(mid);
                    pid = intval(StdClass.getValue(meta, "post_id"));

                    Array metaArray = ClassHandling.get_object_vars(meta);
                    x = new WP_Ajax_Response(gVars, gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "meta"),
                                new ArrayEntry<Object>("id", mid),
                                new ArrayEntry<Object>("data", getIncluded(TemplatePage.class, gVars, gConsts)._list_meta_row(metaArray, c)),
                                new ArrayEntry<Object>("position", 1),
                                new ArrayEntry<Object>("supplemental", new Array<Object>(new ArrayEntry<Object>("postid", pid)))));
                } else {
                    mid = intval(Array.array_pop(Array.array_keys(gVars.webEnv._POST.getArrayValue("meta"))));
                    gVars.key = strval(gVars.webEnv._POST.getArrayValue("meta").getArrayValue(mid).getValue("key"));
                    gVars.value = gVars.webEnv._POST.getArrayValue("meta").getArrayValue(mid).getValue("value");

                    if (!booleanval(meta = getIncluded(PostPage.class, gVars, gConsts).get_post_meta_by_id(mid))) {
                        System.exit("0"); // if meta doesn't exist
                    }

                    if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(meta, "post_id"))) {
                        System.exit("-1");
                    }

                    if (!booleanval(u = getIncluded(PostPage.class, gVars, gConsts).update_meta(mid, gVars.key, strval(gVars.value)))) {
                        System.exit("1"); // We know meta exists; we also know it's unchanged (or DB error, in which case there are bigger problems).
                    }

                    gVars.key = Strings.stripslashes(gVars.webEnv, gVars.key);
                    gVars.value = Strings.stripslashes(gVars.webEnv, strval(gVars.value));
                    x = new WP_Ajax_Response(
                            gVars,
                            gConsts,
                            new Array<Object>(new ArrayEntry<Object>("what", "meta"), new ArrayEntry<Object>("id", mid), new ArrayEntry<Object>("old_id", mid),
                                new ArrayEntry<Object>("data",
                                    getIncluded(TemplatePage.class, gVars, gConsts)._list_meta_row(new Array<Object>(
                                            new ArrayEntry<Object>("meta_key", gVars.key),
                                            new ArrayEntry<Object>("meta_value", gVars.value),
                                            new ArrayEntry<Object>("meta_id", mid)), c)), new ArrayEntry<Object>("position", 0),
                                new ArrayEntry<Object>("supplemental", new Array<Object>(new ArrayEntry<Object>("postid", StdClass.getValue(meta, "post_id"))))));
                }

                x.send();

                break;
            }

            case 17: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer(gVars.action, strval(false), true);

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("create_users")) {
                    System.exit("-1");
                }

                /* Condensed dynamic construct */
                requireOnce(gVars, gConsts, RegistrationPage.class);

                if (!booleanval(gVars.user_id = getIncluded(UserPage.class, gVars, gConsts).add_user())) {
                    System.exit("0");
                } else if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.user_id)) {
                    x = new WP_Ajax_Response(gVars, gConsts, new Array<Object>(new ArrayEntry<Object>("what", "user"), new ArrayEntry<Object>("id", gVars.user_id)));
                    x.send();
                }

                gVars.user_object = new WP_User(gVars, gConsts, gVars.user_id);
                x = new WP_Ajax_Response(gVars, gConsts,
                        new Array<Object>(new ArrayEntry<Object>("what", "user"), new ArrayEntry<Object>("id", gVars.user_id),
                            new ArrayEntry<Object>("data", getIncluded(TemplatePage.class, gVars, gConsts).user_row(gVars.user_object, "", strval(gVars.user_object.getRoles().getValue(0)))),
                            new ArrayEntry<Object>(
                                "supplemental",
                                new Array<Object>(
                                    new ArrayEntry<Object>("show-link",
                                        QStrings.sprintf(
                                            (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("User <a href=\"#%s\">%s</a> added", "default"),
                                            "user-" + strval(gVars.user_id),
                                            gVars.user_object.getUser_login())), new ArrayEntry<Object>("role", gVars.user_object.getRoles().getValue(0))))));
                x.send();

                break;
            }

            case 18: { // The name of this action is hardcoded in edit_post()
                nonce_age = getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("autosave", "autosavenonce", true);
                gVars.webEnv._POST.putValue("post_status", "draft");
                gVars.webEnv._POST.putValue("post_category", Strings.explode(",", strval(gVars.webEnv._POST.getValue("catslist"))));
                gVars.webEnv._POST.putValue("tags_input", Strings.explode(",", strval(gVars.webEnv._POST.getValue("tags_input"))));

                if (equal(gVars.webEnv._POST.getValue("post_type"), "page") || empty(gVars.webEnv._POST.getValue("post_category"))) {
                    gVars.webEnv._POST.arrayUnset("post_category");
                }

                do_autosave = booleanval(gVars.webEnv._POST.getValue("autosave"));
                do_lock = true;
                gVars.data = "";
                gVars.message = QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Draft Saved at %s.", "default"),
                        DateTime.date(getIncluded(L10nPage.class, gVars, gConsts).__("g:i:s a", "default"), intval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("timestamp", true))));
                supplemental = new Array<Object>();
                gVars.id = 0;

                if (intval(gVars.webEnv._POST.getValue("post_ID")) < 0) {
                    gVars.webEnv._POST.putValue("temp_ID", gVars.webEnv._POST.getValue("post_ID"));

                    if (do_autosave) {
                        gVars.id = getIncluded(PostPage.class, gVars, gConsts).wp_write_post();
                        gVars.data = gVars.message;
                    }
                } else {
                    gVars.post_ID = intval(gVars.webEnv._POST.getValue("post_ID"));
                    gVars.webEnv._POST.putValue("ID", gVars.post_ID);
                    gVars.post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(gVars.post_ID, gConsts.getOBJECT(), "raw");

                    if (booleanval(gVars.last = getIncluded(PostPage.class, gVars, gConsts).wp_check_post_lock(StdClass.getValue(gVars.post, "ID")))) {
                        do_autosave = do_lock = false;
                        gVars.last_user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(gVars.last);
                        gVars.last_user_name = strval(
                                (booleanval(gVars.last_user)
                                ? StdClass.getValue(gVars.last_user, "display_name")
                                : getIncluded(L10nPage.class, gVars, gConsts).__("Someone", "default")));
                        gVars.data = new WP_Error(gVars, gConsts, "locked",
                                QStrings.sprintf(
                                    equal(gVars.webEnv._POST.getValue("post_type"), "page")
                                    ? getIncluded(L10nPage.class, gVars, gConsts).__("Autosave disabled: %s is currently editing this page.", "default")
                                    : getIncluded(L10nPage.class, gVars, gConsts).__("Autosave disabled: %s is currently editing this post.", "default"),
                                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(gVars.last_user_name, strval(0))));
                        supplemental.putValue("disable_autosave", "disable");
                    }

                    if (equal("page", StdClass.getValue(gVars.post, "post_type"))) {
                        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", gVars.post_ID)) {
                            System.exit(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this page.", "default"));
                        }
                    } else {
                        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.post_ID)) {
                            System.exit(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this post.", "default"));
                        }
                    }

                    if (do_autosave) {
                        gVars.id = getIncluded(PostPage.class, gVars, gConsts).edit_post();
                        gVars.data = gVars.message;
                    } else {
                        gVars.id = StdClass.getValue(gVars.post, "ID");
                    }
                }

                if (do_lock && booleanval(gVars.id) && is_numeric(gVars.id)) {
                    getIncluded(PostPage.class, gVars, gConsts).wp_set_post_lock(intval(gVars.id));
                }

                if (equal(nonce_age, 2)) {
                    supplemental.putValue("replace-autosavenonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("autosave"));
                    supplemental.putValue("replace-getpermalinknonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("getpermalink"));
                    supplemental.putValue("replace-samplepermalinknonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("samplepermalink"));
                    supplemental.putValue("replace-closedpostboxesnonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("closedpostboxes"));

                    if (booleanval(gVars.id)) {
                        if (equal(gVars.webEnv._POST.getValue("post_type"), "post")) {
                            supplemental.putValue("replace-_wpnonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("update-post_" + strval(gVars.id)));
                        } else if (equal(gVars.webEnv._POST.getValue("post_type"), "page")) {
                            supplemental.putValue("replace-_wpnonce", getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("update-page_" + strval(gVars.id)));
                        }
                    }
                }

                x = new WP_Ajax_Response(
                        gVars,
                        gConsts,
                        new Array<Object>(new ArrayEntry<Object>("what", "autosave"), new ArrayEntry<Object>("id", gVars.id), new ArrayEntry<Object>("data", booleanval(gVars.id)
                                ? gVars.data
                                : ""), new ArrayEntry<Object>("supplemental", supplemental)));
                x.send();

                break;
            }

            case 19: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("autosave", "autosavenonce", true);
                gVars.ID = intval(gVars.webEnv._POST.getValue("post_ID"));

                if (equal(gVars.webEnv._POST.getValue("post_type"), "post")) {
                    if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", gVars.ID)) {
                        System.exit(getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("update-post_" + strval(gVars.ID)));
                    }
                }

                if (equal(gVars.webEnv._POST.getValue("post_type"), "page")) {
                    if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", gVars.ID)) {
                        System.exit(getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("update-page_" + strval(gVars.ID)));
                    }
                }

                System.exit("0");

                break;
            }

            case 20: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("closedpostboxes", "closedpostboxesnonce", true);
                closed = (isset(gVars.webEnv._POST.getValue("closed"))
                    ? strval(gVars.webEnv._POST.getValue("closed"))
                    : "");
                closed = Strings.explode(",", strval(gVars.webEnv._POST.getValue("closed")));

                String page = (isset(gVars.webEnv._POST.getValue("page"))
                    ? strval(gVars.webEnv._POST.getValue("page"))
                    : "");

                if (!QRegExPerl.preg_match("/^[a-z-]+$/", page)) {
                    System.exit(-1);
                }

                if (!is_array(closed)) {
                    break;
                }

                gVars.current_user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
                (((org.numiton.nwp.wp_includes.UserPage) getIncluded(org.numiton.nwp.wp_includes.UserPage.class, gVars, gConsts))).update_usermeta(
                    gVars.current_user.getID(),
                    "closedpostboxes_" + gVars.page,
                    (Array) closed);

                break;
            }

            case 21: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("getpermalink", "getpermalinknonce", true);
                gVars.post_id = (isset(gVars.webEnv._POST.getValue("post_id"))
                    ? intval(gVars.webEnv._POST.getValue("post_id"))
                    : 0);
                System.exit(
                    getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                        new Array<Object>(new ArrayEntry<Object>("preview", "true")),
                        getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.post_id, false)));

                break;
            }

            case 22: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_ajax_referer("samplepermalink", "samplepermalinknonce", true);
                gVars.post_id = (isset(gVars.webEnv._POST.getValue("post_id"))
                    ? intval(gVars.webEnv._POST.getValue("post_id"))
                    : 0);
                gVars.title = (isset(gVars.webEnv._POST.getValue("new_title"))
                    ? strval(gVars.webEnv._POST.getValue("new_title"))
                    : "");
                slug = (isset(gVars.webEnv._POST.getValue("new_slug"))
                    ? strval(gVars.webEnv._POST.getValue("new_slug"))
                    : "");
                System.exit(getIncluded(PostPage.class, gVars, gConsts).get_sample_permalink_html(gVars.post_id, gVars.title, slug));

                break;
            }

            default: {
                getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_ajax_" + strval(gVars.webEnv._POST.getValue("action")), "");
                System.exit("0");

                break;
            }
            }
        }

        return DEFAULT_VAL;
    }
}
