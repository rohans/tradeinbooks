/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IndexPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.DashboardPage;
import org.numiton.nwp.wp_admin.includes.ThemePage;
import org.numiton.nwp.wp_admin.includes.UpdatePage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PostPage;
import org.numiton.nwp.wp_includes.WidgetsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/IndexPage")
@Scope("request")
public class IndexPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(IndexPage.class.getName());
    public String today;
    public boolean can_edit_posts;
    public boolean can_edit_pages;
    public StdClass num_pages;
    public int num_cats;
    public int num_tags;
    public Array<String> post_type_texts = new Array<String>();
    public String post_text;
    public String pending_text;
    public String cats_text;
    public String tags_text;
    public String post_type_text;
    public int num_widgets;
    public String widgets_text;
    public boolean can_switch_themes;

    @Override
    @RequestMapping("/wp-admin/index.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/index";
    }

    public void index_js() {
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\njQuery(function($) {\n\tvar ajaxWidgets = {\n\t\tdashboard_incoming_links: \'incominglinks\',\n\t\tdashboard_primary: \'devnews\',\n\t\tdashboard_secondary: \'planetnews\',\n\t\tdashboard_plugins: \'plugins\'\n\t};\n\t$.each( ajaxWidgets, function(i,a) {\n\t\tvar e = jQuery(\'#\' + i + \' div.dashboard-widget-content\').not(\'.dashboard-widget-control\').find(\'.widget-loading\');\n\t\tif ( e.size() ) { e.parent().load(\'index-extra.php?jax=\' + a); }\n\t} );\n});\n</script>\n");
    }

    public void index_css() {
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/dashboard");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_index_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, DashboardPage.class);
        getIncluded(DashboardPage.class, gVars, gConsts).wp_dashboard_setup();
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "index_js"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "index_css"), 10, 1);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("jquery", false, new Array<Object>(), false);
        
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Dashboard", "default");
        gVars.parent_file = "index.php";
        
        requireOnce(gVars, gConsts, Admin_headerPage.class);
        
        today = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));

        /* Start of block */
        super.startBlock("__wp_admin_index_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Dashboard", "default");

        /* Start of block */
        super.startBlock("__wp_admin_index_block3");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Right Now", "default");

        /* Start of block */
        super.startBlock("__wp_admin_index_block4");

        if (can_edit_posts = getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            echo(gVars.webEnv, "\t<a href=\"post-new.php\" class=\"rbutton\"><strong>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Write a New Post", "default");
            echo(gVars.webEnv, "</strong></a>\n");
        } else {
        }

        if (can_edit_pages = getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_pages")) {
            echo(gVars.webEnv, "\t<a href=\"page-new.php\" class=\"rbutton\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Write a New Page", "default");
            echo(gVars.webEnv, "</a>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_index_block5");
        gVars.num_posts = getIncluded(PostPage.class, gVars, gConsts).wp_count_posts("post", "");
        num_pages = getIncluded(PostPage.class, gVars, gConsts).wp_count_posts("page", "");
        num_cats = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_count_terms("category", new Array<Object>());
        num_tags = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_count_terms("post_tag", new Array<Object>());
        post_type_texts = new Array<String>();

        if (!empty(StdClass.getValue(gVars.num_posts, "publish"))) { // with feeds, anyone can tell how many posts there are.  Just unlink if !current_user_can
            post_text = QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s post", "%s posts", intval(StdClass.getValue(gVars.num_posts, "publish")), "default"),
                    getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(floatval(StdClass.getValue(gVars.num_posts, "publish")), null));
            post_type_texts.putValue(can_edit_posts
                ? ("<a href=\'edit.php\'>" + post_text + "</a>")
                : post_text);
        }

        if (can_edit_pages && !empty(StdClass.getValue(num_pages, "publish"))) { // how many pages is not exposed in feeds.  Don't show if !current_user_can
            post_type_texts.putValue(
                "<a href=\"edit-pages.php\">" +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s page", "%s pages", intval(StdClass.getValue(num_pages, "publish")), "default"),
                    getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(intval(StdClass.getValue(num_pages, "publish")), null)) + "</a>");
        }

        if (can_edit_posts && !empty(StdClass.getValue(gVars.num_posts, "draft"))) {
            post_type_texts.putValue(
                "<a href=\"edit.php?post_status=draft\">" +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s draft", "%s drafts", intval(StdClass.getValue(gVars.num_posts, "draft")), "default"),
                    (((FunctionsPage) PhpWeb.getIncluded(FunctionsPage.class, gVars, gConsts))).number_format_i18n(floatval(StdClass.getValue(gVars.num_posts, "draft")), null)) + "</a>");
        }

        if (can_edit_posts && !empty(StdClass.getValue(gVars.num_posts, "future"))) {
            post_type_texts.putValue(
                "<a href=\"edit.php?post_status=future\">" +
                QStrings.sprintf(
                    getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s scheduled post", "%s scheduled posts", intval(StdClass.getValue(gVars.num_posts, "future")), "default"),
                    getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(intval(StdClass.getValue(gVars.num_posts, "future")), null)) + "</a>");
        }

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts") && !empty(StdClass.getValue(gVars.num_posts, "pending"))) {
            pending_text = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                        "There is <a href=\"%1$s\">%2$s post</a> pending your review.",
                        "There are <a href=\"%1$s\">%2$s posts</a> pending your review.",
                        intval(StdClass.getValue(gVars.num_posts, "pending")),
                        "default"), "edit.php?post_status=pending", getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(intval(StdClass.getValue(gVars.num_posts, "pending")), null));
        } else {
            pending_text = "";
        }

        cats_text = QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s category", "%s categories", num_cats, "default"),
                (((FunctionsPage) PhpWeb.getIncluded(FunctionsPage.class, gVars, gConsts))).number_format_i18n(num_cats, null));
        tags_text = QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s tag", "%s tags", num_tags, "default"),
                getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(num_tags, null));

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_categories")) {
            cats_text = "<a href=\'categories.php\'>" + cats_text + "</a>";
            tags_text = "<a href=\'edit-tags.php\'>" + tags_text + "</a>";
        }

        post_type_text = Strings.implode(", ", post_type_texts);
        
        // There is always a category
        gVars.sentence = QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("You have %1$s, contained within %2$s and %3$s. %4$s", "default"),
                post_type_text,
                cats_text,
                tags_text,
                pending_text);
        gVars.sentence = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("dashboard_count_sentence", gVars.sentence, post_type_text, cats_text, tags_text, pending_text));

        /* Start of block */
        super.startBlock("__wp_admin_index_block6");
        echo(gVars.webEnv, gVars.sentence);

        /* Start of block */
        super.startBlock("__wp_admin_index_block7");
        gVars.ct = getIncluded(ThemePage.class, gVars, gConsts).current_theme_info();
        gVars.sidebars_widgets = getIncluded(WidgetsPage.class, gVars, gConsts).wp_get_sidebars_widgets(true);
        num_widgets = intval(Array.array_reduce(gVars.sidebars_widgets, new Callback("createFunction_sum", this)));
        widgets_text = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%d widget", "%d widgets", num_widgets, "default"), num_widgets);

        if (can_switch_themes = getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("switch_themes")) {
            widgets_text = "<a href=\'widgets.php\'>" + widgets_text + "</a>";
        }

        /* Start of block */
        super.startBlock("__wp_admin_index_block8");
        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("You are using %1$s theme with %2$s.", "default"), StdClass.getValue(gVars.ct, "title"), widgets_text);

        /* Start of block */
        super.startBlock("__wp_admin_index_block9");

        if (can_switch_themes) {
            echo(gVars.webEnv, "\t\t<a href=\"themes.php\" class=\"rbutton\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Change Theme", "default");
            echo(gVars.webEnv, "</a>\n\t");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_index_block10");
        getIncluded(UpdatePage.class, gVars, gConsts).update_right_now_message();

        /* Start of block */
        super.startBlock("__wp_admin_index_block11");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rightnow_end", "");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("activity_box_end", "");

        /* Start of block */
        super.startBlock("__wp_admin_index_block12");
        getIncluded(DashboardPage.class, gVars, gConsts).wp_dashboard();

        /* Start of block */
        super.startBlock("__wp_admin_index_block13");
        require(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }

    public int createFunction_sum(int prev, Object curr) {
        return prev + Array.count(curr);
    }
}
