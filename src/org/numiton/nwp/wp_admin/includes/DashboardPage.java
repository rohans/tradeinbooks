/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: DashboardPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin.includes;

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
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.WidgetsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class DashboardPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(DashboardPage.class.getName());

    /**
     * Generated in place of local variable 'key' from method
     * 'wp_dashboard_dynamic_sidebar_params' because it is used inside an inner
     * class.
     */
    Object wp_dashboard_dynamic_sidebar_params_key = null;

    /**
     * Generated in place of local variable 'link' from method
     * 'wp_dashboard_plugins_output' because it is used inside an inner class.
     */
    String wp_dashboard_plugins_output_link = null;

    /**
     * Generated in place of local variable 'frag' from method
     * 'wp_dashboard_plugins_output' because it is used inside an inner class.
     */
    String wp_dashboard_plugins_output_frag = null;
    public Array<Object> wp_dashboard_sidebars;

    @Override
    @RequestMapping("/wp-admin/includes/dashboard.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/dashboard";
    }

 // Registers dashboard widgets, handles POST data, sets up filters
    public void wp_dashboard_setup() {
        boolean update = false;
        Object widget_optionsObj;

        /* Do not change type */
        int mod_comments = 0;
        String notice = null;
        Array<Object> dashboard_widgets = new Array<Object>();
        update = false;
        widget_optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dashboard_widget_options");

        /* Modified by Numiton */
        Array widget_options;

        if (!booleanval(widget_optionsObj) || !is_array(widget_optionsObj)) {
            widget_options = new Array<Object>();
        } else {
            widget_options = (Array) widget_optionsObj;
        }

        /* Register WP Dashboard Dynamic Sidebar */
        getIncluded(WidgetsPage.class, gVars, gConsts).register_sidebar(
            new Array<Object>(
                new ArrayEntry<Object>("name", "nWordPress Dashboard"),
                new ArrayEntry<Object>("id", "wp_dashboard"),
                new ArrayEntry<Object>("before_widget", "\t<div class=\'dashboard-widget-holder %2$s\' id=\'%1$s\'>\n\n\t\t<div class=\'dashboard-widget\'>\n\n"),
                new ArrayEntry<Object>("after_widget", "\t\t</div>\n\n\t</div>\n\n"),
                new ArrayEntry<Object>("before_title", "\t\t\t<h3 class=\'dashboard-widget-title\'>"),
                new ArrayEntry<Object>("after_title", "</h3>\n\n")));

        /* Register Widgets and Controls */

    	// Recent Comments Widget
        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("moderate_comments") &&
                booleanval(mod_comments = intval(gVars.wpdb.get_var("SELECT COUNT(*) FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'0\'")))) {
            notice = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%d comment awaiting moderation", "%d comments awaiting moderation", mod_comments, "default"), mod_comments);
            notice = "<a href=\'edit-comments.php?comment_status=moderated\'>" + notice + "</a>";
        } else {
            notice = "";
        }

        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_sidebar_widget(
            "dashboard_recent_comments",
            getIncluded(L10nPage.class, gVars, gConsts).__("Recent Comments", "default"),
            Callback.createCallbackArray(this, "wp_dashboard_recent_comments"),
            new Array<Object>(new ArrayEntry<Object>("all_link", "edit-comments.php"), new ArrayEntry<Object>("notice", notice), new ArrayEntry<Object>("width", "half")));

        // Incoming Links Widget
        if (!isset(widget_options.getValue("dashboard_incoming_links"))) {
            update = true;
            widget_options.putValue(
                    "dashboard_incoming_links",
                    new Array<Object>(
                            new ArrayEntry<Object>(
                                    "link",
                                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                                            "dashboard_incoming_links_link",
                                            "http://blogsearch.google.com/blogsearch?hl=en&scoring=d&partner=wordpress&q=link:" +
                                            getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"))))),
                            new ArrayEntry<Object>(
                                    "url",
                                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                                            "dashboard_incoming_links_feed",
                                            "http://blogsearch.google.com/blogsearch_feeds?hl=en&scoring=d&ie=utf-8&num=10&output=rss&partner=wordpress&q=link:" +
                                            getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"))))),
                            new ArrayEntry<Object>("items", 5),
                            new ArrayEntry<Object>("show_date", 0)));
        }

        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_sidebar_widget(
            "dashboard_incoming_links",
            getIncluded(L10nPage.class, gVars, gConsts).__("Incoming Links", "default"),
            Callback.createCallbackArray(this, "wp_dashboard_empty"),
            new Array<Object>(
                new ArrayEntry<Object>("all_link", widget_options.getArrayValue("dashboard_incoming_links").getValue("link")),
                new ArrayEntry<Object>("feed_link", widget_options.getArrayValue("dashboard_incoming_links").getValue("url")),
                new ArrayEntry<Object>("width", "half")),
            "wp_dashboard_cached_rss_widget",
            "wp_dashboard_incoming_links_output");
        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_widget_control(
            "dashboard_incoming_links",
            getIncluded(L10nPage.class, gVars, gConsts).__("Incoming Links", "default"),
            Callback.createCallbackArray(this, "wp_dashboard_rss_control"),
            new Array<Object>(),
            new Array<Object>(
                new ArrayEntry<Object>("widget_id", "dashboard_incoming_links"),
                new ArrayEntry<Object>(
                    "form_inputs",
                    new Array<Object>(new ArrayEntry<Object>("title", false), new ArrayEntry<Object>("show_summary", false), new ArrayEntry<Object>("show_author", false)))));

        // WP Plugins Widget
        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_sidebar_widget(
            "dashboard_plugins",
            getIncluded(L10nPage.class, gVars, gConsts).__("Plugins", "default"),
            Callback.createCallbackArray(this, "wp_dashboard_empty"),
            new Array<Object>(
                new ArrayEntry<Object>("all_link", "http://wordpress.org/extend/plugins/"),
                new ArrayEntry<Object>("feed_link", "http://wordpress.org/extend/plugins/rss/topics/"),
                new ArrayEntry<Object>("width", "half")),
            "wp_dashboard_cached_rss_widget",
            "wp_dashboard_plugins_output",
            new Array<Object>(
                new ArrayEntry<Object>("http://wordpress.org/extend/plugins/rss/browse/popular/"),
                new ArrayEntry<Object>("http://wordpress.org/extend/plugins/rss/browse/new/"),
                new ArrayEntry<Object>("http://wordpress.org/extend/plugins/rss/browse/updated/")));

        // Primary feed (Dev Blog) Widget
        if (!isset(widget_options.getValue("dashboard_primary"))) {
            update = true;
            widget_options.putValue("dashboard_primary",
                new Array<Object>(new ArrayEntry<Object>(
                        "link",
                        getIncluded(PluginPage.class, gVars, gConsts)
                            .apply_filters("dashboard_primary_link", getIncluded(L10nPage.class, gVars, gConsts).__("http://wordpress.org/development/", "default"))),
                    new ArrayEntry<Object>(
                        "url",
                        getIncluded(PluginPage.class, gVars, gConsts)
                            .apply_filters("dashboard_primary_feed", getIncluded(L10nPage.class, gVars, gConsts).__("http://wordpress.org/development/feed/", "default"))),
                    new ArrayEntry<Object>(
                        "title",
                        getIncluded(PluginPage.class, gVars, gConsts).apply_filters("dashboard_primary_title", getIncluded(L10nPage.class, gVars, gConsts).__("WordPress Development Blog", "default"))),
                    new ArrayEntry<Object>("items", 2),
                    new ArrayEntry<Object>("show_summary", 1),
                    new ArrayEntry<Object>("show_author", 0),
                    new ArrayEntry<Object>("show_date", 1)));
        }

        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_sidebar_widget(
            "dashboard_primary",
            widget_options.getArrayValue("dashboard_primary").getValue("title"),
            Callback.createCallbackArray(this, "wp_dashboard_empty"),
            new Array<Object>(new ArrayEntry<Object>("all_link", widget_options.getArrayValue("dashboard_primary").getValue("link")),
                new ArrayEntry<Object>("feed_link", widget_options.getArrayValue("dashboard_primary").getValue("url")), new ArrayEntry<Object>("width", "half"),
                new ArrayEntry<Object>("class", "widget_rss")),
            "wp_dashboard_cached_rss_widget",
            "wp_dashboard_rss_output");
        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_widget_control(
            "dashboard_primary",
            getIncluded(L10nPage.class, gVars, gConsts).__("Primary Feed", "default"),
            Callback.createCallbackArray(this, "wp_dashboard_rss_control"),
            new Array<Object>(),
            new Array<Object>(new ArrayEntry<Object>("widget_id", "dashboard_primary")));

        // Secondary Feed (Planet) Widget
        if (!isset(widget_options.getValue("dashboard_secondary"))) {
            update = true;
            widget_options.putValue("dashboard_secondary",
                new Array<Object>(new ArrayEntry<Object>(
                        "link",
                        getIncluded(PluginPage.class, gVars, gConsts)
                            .apply_filters("dashboard_secondary_link", getIncluded(L10nPage.class, gVars, gConsts).__("http://planet.wordpress.org/", "default"))),
                    new ArrayEntry<Object>(
                        "url",
                        getIncluded(PluginPage.class, gVars, gConsts)
                            .apply_filters("dashboard_secondary_feed", getIncluded(L10nPage.class, gVars, gConsts).__("http://planet.wordpress.org/feed/", "default"))),
                    new ArrayEntry<Object>(
                        "title",
                        getIncluded(PluginPage.class, gVars, gConsts).apply_filters("dashboard_secondary_title", getIncluded(L10nPage.class, gVars, gConsts).__("Other WordPress News", "default"))),
                    new ArrayEntry<Object>("items", 15)));
        }

        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_sidebar_widget(
            "dashboard_secondary",
            widget_options.getArrayValue("dashboard_secondary").getValue("title"),
            Callback.createCallbackArray(this, "wp_dashboard_empty"),
            new Array<Object>(
                new ArrayEntry<Object>("all_link", widget_options.getArrayValue("dashboard_secondary").getValue("link")),
                new ArrayEntry<Object>("feed_link", widget_options.getArrayValue("dashboard_secondary").getValue("url")),
                new ArrayEntry<Object>("width", "full")),
            "wp_dashboard_cached_rss_widget",
            "wp_dashboard_secondary_output");
        getIncluded(WidgetsPage.class, gVars, gConsts).wp_register_widget_control(
            "dashboard_secondary",
            getIncluded(L10nPage.class, gVars, gConsts).__("Secondary Feed", "default"),
            Callback.createCallbackArray(this, "wp_dashboard_rss_control"),
            new Array<Object>(),
            new Array<Object>(
                new ArrayEntry<Object>("widget_id", "dashboard_secondary"),
                new ArrayEntry<Object>(
                    "form_inputs",
                    new Array<Object>(new ArrayEntry<Object>("show_summary", false), new ArrayEntry<Object>("show_author", false), new ArrayEntry<Object>("show_date", false)))));
        
        /* Dashboard Widget Template
		wp_register_sidebar_widget( $widget_id (unique slug) , $widget_title, $output_callback,
			array(
				'all_link'  => full url for "See All" link,
				'feed_link' => full url for "RSS" link,
				'width'     => 'fourth', 'third', 'half', 'full' (defaults to 'half'),
				'height'    => 'single', 'double' (defaults to 'single'),
			),
			$wp_dashboard_empty_callback (only needed if using 'wp_dashboard_empty' as your $output_callback),
			$arg, $arg, $arg... (further args passed to callbacks)
		);

		// optional: if you want users to be able to edit the settings of your widget, you need to register a widget_control
		wp_register_widget_control( $widget_id, $widget_control_title, $control_output_callback,
			array(), // leave an empty array here: oddity in widget code
			array(
				'widget_id' => $widget_id, // Yes - again.  This is required: oddity in widget code
				'arg'       => an arg to pass to the $control_output_callback,
				'another'   => another arg to pass to the $control_output_callback,
				...
			)
		);
		*/

        // Hook to register new widgets
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_dashboard_setup", "");
        
        // Hard code the sidebar's widgets and order
        dashboard_widgets = new Array<Object>();
        dashboard_widgets.putValue("dashboard_recent_comments");
        dashboard_widgets.putValue("dashboard_incoming_links");

        // Commented by Numiton TODO Add something else instead
        //dashboard_widgets.putValue("dashboard_primary");

        //        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("activate_plugins")) {
        //            dashboard_widgets.putValue("dashboard_plugins");
        //        }

        //        dashboard_widgets.putValue("dashboard_secondary");
        dashboard_widgets = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_dashboard_widgets", dashboard_widgets);
        
        // Filter widget order
        wp_dashboard_sidebars = new Array<Object>(new ArrayEntry<Object>("wp_dashboard", dashboard_widgets), new ArrayEntry<Object>("array_version", 3.5));
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("dynamic_sidebar_params", Callback.createCallbackArray(this, "wp_dashboard_dynamic_sidebar_params"), 10, 1);

        if (equal("POST", gVars.webEnv.getRequestMethod()) && isset(gVars.webEnv._POST.getValue("widget_id"))) {
            OutputControl.ob_start(gVars.webEnv); // hack - but the same hack wp-admin/widgets.php uses
            wp_dashboard_trigger_widget_control(gVars.webEnv._POST.getValue("widget_id"));
            OutputControl.ob_end_clean(gVars.webEnv);
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("edit"), 302);
            System.exit();
        }

        if (update) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("dashboard_widget_options", widget_options);
        }
    }

    /**
     * Echoes out the dashboard
     */
    public void wp_dashboard() {
        echo(gVars.webEnv, "<div id=\'dashboard-widgets\'>\n\n");
        
        // We're already filtering dynamic_sidebar_params obove
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_sidebars_widgets", Callback.createCallbackArray(this, "wp_dashboard_sidebars_widgets"), 10, 1); // here there be hackery
        getIncluded(WidgetsPage.class, gVars, gConsts).dynamic_sidebar("wp_dashboard");
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("option_sidebars_widgets", "wp_dashboard_sidebars_widgets", 10, 1);
        
        echo(gVars.webEnv, "<br class=\'clear\' />\n</div>\n\n\n");
    }

    public Array<Object> wp_dashboard_sidebars_widgets(Array deprecated) {
        return wp_dashboard_sidebars_widgets();
    }

    /**
     * Makes sidebar_widgets option reflect the dashboard settings
     */
    public Array<Object> wp_dashboard_sidebars_widgets() {
        return wp_dashboard_sidebars;
    }

 // Modifies sidbar params on the fly to set up ids, class names, titles for each widget (called once per widget)
 // Switches widget to edit mode if $_GET['edit']
    public Array<Object> wp_dashboard_dynamic_sidebar_params(Array<Object> params) {
        String sidebar_widget_id;
        Array<String> the_classes = new Array<String>();
        Object widget_width = null;
        Object widget_height = null;
        Object widget_class = null;
        String sidebar_before_widget = null;
        Array<String> links = new Array<String>();
        String widget_all_link = null;
        String content_class = null;
        Object widget_id = null;
        String sidebar_widget_name = null;
        String sidebar_after_widget = null;
        String widget_feed_link = null;
        String sidebar_before_title = null;
        String sidebar_after_title = null;
        Object widget_notice = null;
        Object widget_error = null;

        /* Modified by Numiton */
        sidebar_widget_id = "";
        sidebar_before_widget = "";
        sidebar_after_widget = "";
        sidebar_before_title = "";
        sidebar_after_title = "";

        /* Modified by Numiton */
        sidebar_widget_name = strval(Array.extractVar(params.getArrayValue(0), "widget_name", sidebar_widget_name, Array.EXTR_OVERWRITE));
        sidebar_widget_id = strval(Array.extractVar(params.getArrayValue(0), "widget_id", sidebar_widget_id, Array.EXTR_OVERWRITE));
        sidebar_before_widget = strval(Array.extractVar(params.getArrayValue(0), "before_widget", sidebar_before_widget, Array.EXTR_OVERWRITE));
        sidebar_after_widget = strval(Array.extractVar(params.getArrayValue(0), "after_widget", sidebar_after_widget, Array.EXTR_OVERWRITE));
        sidebar_before_title = strval(Array.extractVar(params.getArrayValue(0), "before_title", sidebar_before_title, Array.EXTR_OVERWRITE));
        sidebar_after_title = strval(Array.extractVar(params.getArrayValue(0), "after_title", sidebar_after_title, Array.EXTR_OVERWRITE));

        if (!isset(gVars.wp_registered_widgets.getValue(sidebar_widget_id)) || !is_array(gVars.wp_registered_widgets.getValue(sidebar_widget_id))) {
            return params;
        }

        /* Modified by Numiton */
        widget_id = "";
        widget_width = "";
        widget_height = "";
        widget_class = "";
        widget_feed_link = "";
        widget_all_link = "";
        widget_notice = false;
        widget_error = false;

        /* Modified by Numiton */
        widget_id = Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "id", widget_id, Array.EXTR_OVERWRITE);
        widget_width = Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "width", widget_width, Array.EXTR_OVERWRITE);
        widget_height = Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "height", widget_height, Array.EXTR_OVERWRITE);
        widget_class = Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "class", widget_class, Array.EXTR_OVERWRITE);
        widget_feed_link = strval(Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "feed_link", widget_feed_link, Array.EXTR_OVERWRITE));
        widget_all_link = strval(Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "all_link", widget_all_link, Array.EXTR_OVERWRITE));
        widget_notice = Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "notice", widget_notice, Array.EXTR_OVERWRITE);
        widget_error = Array.extractVar(gVars.wp_registered_widgets.getArrayValue(sidebar_widget_id), "error", widget_error, Array.EXTR_OVERWRITE);
        the_classes = new Array<String>();

        if (Array.in_array(widget_width, new Array<Object>(new ArrayEntry<Object>("third"), new ArrayEntry<Object>("fourth"), new ArrayEntry<Object>("full")))) {
            the_classes.putValue(widget_width);
        }

        if (equal("double", widget_height)) {
            the_classes.putValue("double");
        }

        if (booleanval(widget_class)) {
            the_classes.putValue(widget_class);
        }

        // Add classes to the widget holder
        if (booleanval(the_classes)) {
            sidebar_before_widget = Strings.str_replace("<div class=\'dashboard-widget-holder ", "<div class=\'dashboard-widget-holder " + Strings.join(" ", the_classes) + " ", sidebar_before_widget);
        }

        links = new Array<String>();

        if (booleanval(widget_all_link)) {
            links.putValue(
                    "<a href=\"" + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(widget_all_link, null, "display") + "\">" +
                    getIncluded(L10nPage.class, gVars, gConsts).__("See&nbsp;All", "default") + "</a>");
        }

        content_class = "dashboard-widget-content";

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_dashboard") && isset(gVars.wp_registered_widget_controls.getValue(widget_id)) &&
                VarHandling.is_callable(new Callback(gVars.wp_registered_widget_controls.getArrayValue(widget_id).getArrayValue("callback")))) {
        	// Switch this widget to edit mode
            if (isset(gVars.webEnv._GET.getValue("edit")) && equal(gVars.webEnv._GET.getValue("edit"), widget_id)) {
                content_class = content_class + " dashboard-widget-control";
                gVars.wp_registered_widgets.getArrayValue(widget_id).putValue("callback", Callback.createCallbackArray(this, "wp_dashboard_empty"));
                sidebar_widget_name = strval(gVars.wp_registered_widget_controls.getArrayValue(widget_id).getValue("name"));
                params.putValue(1, "wp_dashboard_trigger_widget_control");
                sidebar_before_widget = sidebar_before_widget + "<form action=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("edit") + "\" method=\"post\">";
                sidebar_after_widget = "<div class=\'dashboard-widget-submit\'><input type=\'hidden\' name=\'sidebar\' value=\'wp_dashboard\' /><input type=\'hidden\' name=\'widget_id\' value=\'" +
                    strval(widget_id) + "\' /><input type=\'submit\' value=\'" + getIncluded(L10nPage.class, gVars, gConsts).__("Save", "default") + "\' /></div></form>" + sidebar_after_widget;
                links.putValue(
                        "<a href=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("edit") + "\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Cancel", "default") +
                        "</a>");
            } else {
                links.putValue(
                    "<a href=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("edit", widget_id) + "#" + strval(widget_id) + "\">" +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default") + "</a>");
            }
        }

        if (booleanval(widget_feed_link)) {
            links.putValue(
                    "<img class=\"rss-icon\" src=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/" + gConsts.getWPINC() + "/images/rss.png\" alt=\"" +
                    getIncluded(L10nPage.class, gVars, gConsts).__("rss icon", "default") + "\" /> <a href=\"" +
                    getIncluded(FormattingPage.class, gVars, gConsts).clean_url(widget_feed_link, null, "display") + "\">" + getIncluded(L10nPage.class, gVars, gConsts).__("RSS", "default") + "</a>");
        }

        links = (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_dashboard_widget_links_" + strval(widget_id), links);

        // Add links to widget's title bar
        if (booleanval(links)) {
            sidebar_before_title = sidebar_before_title + "<span>";
            sidebar_after_title = "</span><small>" + Strings.join("&nbsp;|&nbsp;", links) + "</small><br class=\'clear\' />" + sidebar_after_title;
        }

        // Could have put this in widget-content.  Doesn't really matter
        if (booleanval(widget_notice)) {
            sidebar_after_title = sidebar_after_title + "\t\t\t<div class=\'dashboard-widget-notice\'>" + strval(widget_notice) + "</div>\n\n";
        }

        if (booleanval(widget_error)) {
            sidebar_after_title = sidebar_after_title + "\t\t\t<div class=\'dashboard-widget-error\'>" + strval(widget_error) + "</div>\n\n";
        }

        sidebar_after_title = sidebar_after_title + "\t\t\t<div class=\'" + content_class + "\'>\n\n";
        sidebar_after_widget = sidebar_after_widget + "\t\t\t</div>\n\n";

        // Modified by Numiton
        params.putValue(0,
            Array.compact(
                new ArrayEntry("widget_name", sidebar_widget_name),
                new ArrayEntry("widget_id", sidebar_widget_id),
                new ArrayEntry("before_widget", sidebar_before_widget),
                new ArrayEntry("after_widget", sidebar_after_widget),
                new ArrayEntry("before_title", sidebar_before_title),
                new ArrayEntry("after_title", sidebar_after_title)));

        return params;
    }

    public int createFunction_option_posts_per_rss(Object deprecated) {
        return 5;
    }

    /**
     * Dashboard Widgets
     */
    public void wp_dashboard_recent_comments(Array sidebar_args) {
        Object before_widget = null;
        Object before_title = null;
        Object widget_name = null;
        Object after_title = null;
        WP_Query comments_query = null;
        Boolean is_first = null;
        Object comment_post_url = null;
        Object comment_post_title = null;
        Object comment_post_link = null;
        String comment_link = null;
        String comment_meta = null;
        Object after_widget = null;
        
        before_widget = Array.extractVar(sidebar_args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(sidebar_args, "before_title", before_title, Array.EXTR_SKIP);
        widget_name = Array.extractVar(sidebar_args, "widget_name", widget_name, Array.EXTR_SKIP);
        after_title = Array.extractVar(sidebar_args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(sidebar_args, "after_widget", after_widget, Array.EXTR_SKIP);
        
        echo(gVars.webEnv, before_widget);
        
        echo(gVars.webEnv, before_title);
        echo(gVars.webEnv, widget_name);
        echo(gVars.webEnv, after_title);
        
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("option_posts_per_rss", Callback.createCallbackArray(this, "createFunction_option_posts_per_rss"), 10, 1); // hack - comments query doesn't accept per_page parameter
        comments_query = new WP_Query(gVars, gConsts, "feed=rss2&withcomments=1");
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("option_posts_per_rss", Callback.createCallbackArray(this, "createFunction_option_posts_per_rss"), 10, 1);
        is_first = true;

        if (comments_query.have_comments()) /*
        * is_first is_first
        */ {
            while (comments_query.have_comments()) {
                comments_query.the_comment();
                comment_post_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(intval(StdClass.getValue(gVars.comment, "comment_post_ID")), false);
                comment_post_title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.comment, "comment_post_ID")));
                comment_post_link = "<a href=\'" + strval(comment_post_url) + "\'>" + strval(comment_post_title) + "</a>";
                comment_link = "<a class=\"comment-link\" href=\"" + getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_link() + "\">#</a>";
                comment_meta = QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("From <strong>%1$s</strong> on %2$s %3$s", "default"),
                        getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_author(),
                        comment_post_link,
                        comment_link);

                if (is_first) {
                    is_first = false;
                    echo(gVars.webEnv, "\t\t\t\t<blockquote><p>&#8220;");
                    getIncluded(Comment_templatePage.class, gVars, gConsts).comment_excerpt();
                    echo(gVars.webEnv, "&#8221;</p></blockquote>\n\t\t\t\t<p class=\'comment-meta\'>");
                    echo(gVars.webEnv, comment_meta);
                    echo(gVars.webEnv, "</p>\n");

                    if (comments_query.comment_count > 1) {
                        echo(gVars.webEnv, "\t\t\t\t<ul id=\"dashboard-comments-list\">\n");
                    } else {
                    } // comment_count
                } else { // is_first
                    echo(gVars.webEnv, "\n\t\t\t\t\t<li class=\'comment-meta\'>");
                    echo(gVars.webEnv, comment_meta);
                    echo(gVars.webEnv, "</li>\n");
                } // is_first
            }

            if (comments_query.comment_count > 1) {
                echo(gVars.webEnv, "\t\t\t\t</ul>\n");
            } else {
            } // comment_count;
        }

        echo(gVars.webEnv, after_widget);
    }

    public void wp_dashboard_incoming_links_output(String deprecated, String deprecated2) {
        wp_dashboard_incoming_links_output();
    }

    /**
     * $sidebar_args are handled by wp_dashboard_empty()
     */
    public void wp_dashboard_incoming_links_output() {
        Array<Object> widgets = new Array<Object>();
        MagpieRSS rss = null;
        String url = null;
        Integer items = null;
        String publisher = null;
        String site_link = null;
        String link = null;
        String content = null;
        String date = null;
        Array<Object> item = new Array<Object>();
        String text = null;
        Object show_date = null;
        Object show_author = null;
        Object show_summary = null;
        
        widgets = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dashboard_widget_options");
        url = strval(Array.extractVar(widgets.getArrayValue("dashboard_incoming_links"), "url", url, Array.EXTR_SKIP));
        items = intval(Array.extractVar(widgets.getArrayValue("dashboard_incoming_links"), "items", items, Array.EXTR_SKIP));
        show_date = Array.extractVar(widgets.getArrayValue("dashboard_incoming_links"), "show_date", show_date, Array.EXTR_SKIP);
        show_author = Array.extractVar(widgets.getArrayValue("dashboard_incoming_links"), "show_author", show_author, Array.EXTR_SKIP);
        show_summary = Array.extractVar(widgets.getArrayValue("dashboard_incoming_links"), "show_summary", show_summary, Array.EXTR_SKIP);
        rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(url);

        if (isset(rss.items) && (1 < Array.count(rss.items))) { // Technorati returns a 1-item feed when it has no results
            echo(gVars.webEnv, "<ul>\n");
            rss.items = Array.array_slice(rss.items, 0, items);

            for (Map.Entry javaEntry133 : rss.items.entrySet()) {
                item = (Array<Object>) javaEntry133.getValue();
                publisher = "";
                site_link = "";
                link = "";
                content = "";
                date = "";
                link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.strip_tags(strval(item.getValue("link"))), null, "display");

                if (isset(item.getValue("author_uri"))) {
                    site_link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.strip_tags(strval(item.getValue("author_uri"))), null, "display");
                }

                if (!booleanval(
                            publisher = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(
                                        strval(isset(item.getArrayValue("dc").getValue("publisher"))
                                            ? item.getArrayValue("dc").getValue("publisher")
                                                : item.getValue("author_name"))), strval(0)))) {
                    publisher = getIncluded(L10nPage.class, gVars, gConsts).__("Somebody", "default");
                }

                if (booleanval(site_link)) {
                    publisher = "<a href=\'" + site_link + "\'>" + publisher + "</a>";
                } else {
                    publisher = "<strong>" + publisher + "</strong>";
                }

                if (isset(item.getValue("description"))) {
                    content = strval(item.getValue("description"));
                } else if (isset(item.getValue("summary"))) {
                    content = strval(item.getValue("summary"));
                } else if (isset(item.getValue("atom_content"))) {
                    content = strval(item.getValue("atom_content"));
                } else {
                    content = getIncluded(L10nPage.class, gVars, gConsts).__("something", "default");
                }

                content = getIncluded(FormattingPage.class, gVars, gConsts).wp_html_excerpt(content, 50) + " ...";

                if (booleanval(link)) {
                    text = getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s linked here <a href=\"%2$s\">saying</a>, \"%3$s\"|feed_display", "default");
                } else {
                    text = getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s linked here saying, \"%3$s\"|feed_display", "default");
                }

                if (booleanval(show_date)) {
                    if (booleanval(show_author) || booleanval(show_summary)) {
                        text = text + getIncluded(L10nPage.class, gVars, gConsts)._c(" on %4$s|feed_display", "default");
                    }

                    date = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(
                                strval(isset(item.getValue("pubdate"))
                                    ? item.getValue("pubdate")
                                    : item.getValue("published"))), strval(0));

                    int dateInt = QDateTime.strtotime(date);
                    date = DateTime.gmdate(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")), dateInt);
                }

                echo(gVars.webEnv, "\t<li>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)._c(text + "|feed_display", "default"), publisher, link, content, date) + "</li>\n");
            }

            echo(gVars.webEnv, "</ul>\n");
        } else {
            echo(
                    gVars.webEnv,
                    "<p>" +
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "This dashboard widget queries <a href=\"http://blogsearch.google.com/\">Google Blog Search</a> so that when another blog links to your site it will show up here. It has found no incoming links&hellip; yet. It&#8217;s okay &#8212; there is no rush.",
                            "default") + "</p>\n");
        }
    }

    /**
     * $sidebar_args are handled by wp_dashboard_empty()
     */
    public void wp_dashboard_rss_output(String widget_id, Object... deprecated) {
        Array<Object> widgets = new Array<Object>();
        widgets = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dashboard_widget_options");
        getIncluded(WidgetsPage.class, gVars, gConsts).wp_widget_rss_output(widgets.getValue(widget_id), new Array<Object>());
    }

    public boolean wp_dashboard_secondary_output(String deprecated, String deprecated2) {
        return wp_dashboard_secondary_output();
    }

    /**
     * $sidebar_args are handled by wp_dashboard_empty()
     */
    public boolean wp_dashboard_secondary_output() {
        Array<Object> widgets = new Array<Object>();
        MagpieRSS rss = null;
        String url = null;
        Integer items = null;
        Object title = null;
        Array<Object> item = new Array<Object>();
        String author;
        String post;
        Object link = null;
        widgets = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dashboard_widget_options");
        url = strval(Array.extractVar(widgets.getArrayValue("dashboard_secondary"), "url", url, Array.EXTR_SKIP));
        items = intval(Array.extractVar(widgets.getArrayValue("dashboard_secondary"), "items", items, Array.EXTR_SKIP));
        rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(url);

        if (!isset(rss.items) || equal(0, Array.count(rss.items))) {
            return false;
        }

        echo(gVars.webEnv, "<ul id=\'planetnews\'>\n");
        rss.items = Array.array_slice(rss.items, 0, items);

        for (Map.Entry javaEntry134 : rss.items.entrySet()) {
            item = (Array<Object>) javaEntry134.getValue();
            title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(item.getValue("title")), strval(0));
            author = QRegExPerl.preg_replace("|(.+?):.+|s", "$1", strval(item.getValue("title")));
            post = QRegExPerl.preg_replace("|.+?:(.+)|s", "$1", strval(item.getValue("title")));
            link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(item.getValue("link")), null, "display");
            echo(gVars.webEnv, "\t<li><a href=\'" + strval(link) + "\'><span class=\'post\'>" + post + "</span><span class=\'hidden\'> - </span><cite>" + author + "</cite></a></li>\n");
        }

        echo(gVars.webEnv, "</ul>\n<br class=\'clear\' />\n");

        return false;
    }

    /**
     * $sidebar_args are handled by wp_dashboard_empty()
     */
    public Object wp_dashboard_plugins_output(Object... deprecated) {
        MagpieRSS popular = null;
        MagpieRSS _new = null;
        MagpieRSS updated = null;
        Object feed = null;
        Object item_key = null;
        Array<Object> item = new Array<Object>();
        Array<Object> matches = new Array<Object>();
        String title = null;
        Object description = null;
        String dlink = null;
        Object label = null;
        popular = getIncluded(RssPage.class, gVars, gConsts).fetch_rss("http://wordpress.org/extend/plugins/rss/browse/popular/");
        _new = getIncluded(RssPage.class, gVars, gConsts).fetch_rss("http://wordpress.org/extend/plugins/rss/browse/new/");
        updated = getIncluded(RssPage.class, gVars, gConsts).fetch_rss("http://wordpress.org/extend/plugins/rss/browse/updated/");

        for (Map.Entry javaEntry135 : new Array<Object>(
                new ArrayEntry<Object>("popular", getIncluded(L10nPage.class, gVars, gConsts).__("Most Popular", "default")),
                new ArrayEntry<Object>("new", getIncluded(L10nPage.class, gVars, gConsts).__("Newest Plugins", "default")),
                new ArrayEntry<Object>("updated", getIncluded(L10nPage.class, gVars, gConsts).__("Recently Updated", "default"))).entrySet()) {
            feed = javaEntry135.getKey();
            label = javaEntry135.getValue();

            /* Modified by Numiton */
            MagpieRSS feedObj;

            if (equal(feed, "popular")) {
                feedObj = popular;
            } else if (equal(feed, "new")) {
                feedObj = _new;
            } else {
                assert equal(feed, "updated") : feed;
                feedObj = updated;
            }

            if (!isset(feedObj.items) || equal(0, Array.count(feedObj.items))) {
                continue;
            }

            feedObj.items = Array.array_slice(feedObj.items, 0, 5);
            item_key = Array.array_rand(feedObj.items);

            // Eliminate some common badly formed plugin descriptions
            while (!strictEqual(null, item_key = Array.array_rand(feedObj.items)) &&
                    !strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(feedObj.items.getArrayValue(item_key).getValue("description")), "Plugin Name:")))
                feedObj.items.arrayUnset(item_key);

            if (!isset(feedObj.items.getValue(item_key))) {
                continue;
            }

            item = feedObj.items.getArrayValue(item_key);

            // current bbPress feed item titles are: user on "topic title"
            if (QRegExPerl.preg_match("/\"(.*)\"/s", strval(item.getValue("title")), matches)) {
                title = strval(matches.getValue(1));
            } else { // but let's make it forward compatible if things change
                title = strval(item.getValue("title"));
            }

            title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(title, strval(0));
            description = getIncluded(FormattingPage.class, gVars, gConsts)
                              .wp_specialchars(Strings.strip_tags(Strings.html_entity_decode(strval(item.getValue("description")), Strings.ENT_QUOTES)), strval(0));
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        wp_dashboard_plugins_output_link = srcArray.getValue(0);
                        wp_dashboard_plugins_output_frag = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(Strings.explode("#", strval(item.getValue("link"))));
            wp_dashboard_plugins_output_link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(wp_dashboard_plugins_output_link, null, "display");
            dlink = Strings.rtrim(wp_dashboard_plugins_output_link, "/") + "/download/";
            echo(gVars.webEnv, "<h4>" + strval(label) + "</h4>\n");
            echo(
                gVars.webEnv,
                "<h5><a href=\'" + wp_dashboard_plugins_output_link + "\'>" + title + "</a></h5>&nbsp;<span>(<a href=\'" + dlink + "\'>" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Download", "default") + "</a>)</span>\n");
            echo(gVars.webEnv, "<p>" + strval(description) + "</p>\n");
        }

        return "";
    }

    public boolean wp_dashboard_cached_rss_widget(Object widget_id, Object callback) {
        return wp_dashboard_cached_rss_widget(widget_id, callback, null);
    }

 // Checks to see if all of the feed url in $check_urls are cached.
 // If $check_urls is empty, look for the rss feed url found in the dashboard widget optios of $widget_id.
 // If cached, call $callback, a function that echoes out output for this widget.
 // If not cache, echo a "Loading..." stub which is later replaced by AJAX call (see top of /wp-admin/index.php)
    public boolean wp_dashboard_cached_rss_widget(Object widget_id, Object callback, Array<Object> check_urls) {
        String loading = null;
        Array<Object> widgets = new Array<Object>();
        RSSCache cache = null;
        String status = null;
        String check_url = null;
        Array<Object> args = new Array<Object>();
        loading = "<p class=\"widget-loading\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Loading&#8230;", "default") + "</p>";

        if (empty(check_urls)) {
            widgets = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dashboard_widget_options");

            if (empty(widgets.getArrayValue(widget_id).getValue("url"))) {
                echo(gVars.webEnv, loading);

                return false;
            }

            check_urls = new Array<Object>(new ArrayEntry<Object>(widgets.getArrayValue(widget_id).getValue("url")));
        }

        // Condensed dynamic construct
        requireOnce(gVars, gConsts, RssPage.class);
        getIncluded(RssPage.class, gVars, gConsts).init(); // initialize rss constants
        
        cache = new RSSCache(gVars, gConsts, gConsts.getMAGPIE_CACHE_DIR(), gConsts.getMAGPIE_CACHE_AGE());

        for (Map.Entry javaEntry136 : check_urls.entrySet()) {
            check_url = strval(javaEntry136.getValue());
            status = cache.check_cache(check_url);

            if (!strictEqual("HIT", status)) {
                echo(gVars.webEnv, loading);

                return false;
            }
        }

        if (booleanval(callback)) /* && Unsupported.is_callable(callback) */ {
            // Modified by Numiton

            // TODO Check
            args = check_urls;
            Array.array_unshift(args, widget_id);

            // Modified by Numiton
            Callback c;

            if (is_array(callback)) {
                c = new Callback((Array) callback);
            } else {
                c = new Callback(strval(callback), this);
            }

            FunctionHandling.call_user_func_array(c, args);
        }

        return true;
    }

    public void wp_dashboard_empty(Array sidebar_args) {
        wp_dashboard_empty(sidebar_args, null);
    }

 // Empty widget used for JS/AJAX created output.
 // Callback inserts content between before_widget and after_widget.  Used when widget is in edit mode.  Can also be used for custom widgets.
    public void wp_dashboard_empty(Array sidebar_args, Object callback, Object... vargs) {
        Object before_widget = null;
        Object before_title = null;
        Object widget_name = null;
        Object after_title = null;
        Array<Object> args = new Array<Object>();
        Object widget_id = null;
        Object after_widget = null;
        // callback = Array.extractVar(sidebar_args, "callback", callback,

        // Array.EXTR_SKIP);
        before_widget = Array.extractVar(sidebar_args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(sidebar_args, "before_title", before_title, Array.EXTR_SKIP);
        widget_name = Array.extractVar(sidebar_args, "widget_name", widget_name, Array.EXTR_SKIP);
        after_title = Array.extractVar(sidebar_args, "after_title", after_title, Array.EXTR_SKIP);

        // args = Array.extractVar(sidebar_args, "args", args,

        // Array.EXTR_SKIP);
        widget_id = Array.extractVar(sidebar_args, "widget_id", widget_id, Array.EXTR_SKIP);
        after_widget = Array.extractVar(sidebar_args, "after_widget", after_widget, Array.EXTR_SKIP);
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, before_title);
        echo(gVars.webEnv, widget_name);
        echo(gVars.webEnv, after_title);

        // Modified by Numiton
        // When in edit mode, the callback passed to this function is the widget_control callback
        if (booleanval(callback)) /* && Unsupported.is_callable(callback) */ {
            args = new Array<Object>();
            args.putAllValues(vargs);
            Array.array_unshift(args, widget_id);

            Callback c;

            if (is_array(callback)) {
                c = new Callback((Array) callback);
            } else {
                c = new Callback(strval(callback), this);
            }

            // TODO Check

            // context
            FunctionHandling.call_user_func_array(c, args);
        }

        echo(gVars.webEnv, after_widget);
    }

/* Dashboard Widgets Controls. Ssee also wp_dashboard_empty() */

 // Calls widget_control callback
    public void wp_dashboard_trigger_widget_control(Object widget_control_id, Object... deprecated) {
        if (is_scalar(widget_control_id) && booleanval(widget_control_id) && isset(gVars.wp_registered_widget_controls.getValue(widget_control_id)) &&
                VarHandling.is_callable(new Callback(gVars.wp_registered_widget_controls.getArrayValue(widget_control_id).getArrayValue("callback")))) {
            FunctionHandling.call_user_func_array(
                new Callback(gVars.wp_registered_widget_controls.getArrayValue(widget_control_id).getArrayValue("callback")),
                gVars.wp_registered_widget_controls.getArrayValue(widget_control_id).getArrayValue("params"));
        }
    }

    /**
     * Sets up $args to be used as input to wp_widget_rss_form(), handles POST
     * data from RSS-type widgets
     */
    public boolean wp_dashboard_rss_control(Array<Object> args) {
        Object widget_id = null;
        Array<Object> widget_options = new Array<Object>();
        Object widget_optionsObj;
        String number;
        MagpieRSS rss = null;
        Array<Object> form_inputs = null;
        widget_id = Array.extractVar(args, "widget_id", widget_id, Array.EXTR_OVERWRITE);
        form_inputs = (Array<Object>) Array.extractVar(args, "form_inputs", form_inputs, Array.EXTR_OVERWRITE);

        if (!booleanval(widget_id)) {
            return false;
        }

        if (!booleanval(widget_optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dashboard_widget_options"))) {
            widget_options = new Array<Object>();
        } else {
            widget_options = (Array<Object>) widget_optionsObj;
        }

        if (!isset(widget_options.getValue(widget_id))) {
            widget_options.putValue(widget_id, new Array<Object>());
        }

        number = "1"; // Hack to use wp_widget_rss_form()
        widget_options.getArrayValue(widget_id).putValue("number", number);

        if (equal("POST", gVars.webEnv.getRequestMethod()) && isset(gVars.webEnv._POST.getArrayValue("widget-rss").getValue(number))) {
            gVars.webEnv._POST.getArrayValue("widget-rss")
                              .putValue(number, getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(gVars.webEnv._POST.getArrayValue("widget-rss").getValue(number)));
            widget_options.putValue(widget_id, getIncluded(WidgetsPage.class, gVars, gConsts).wp_widget_rss_process(gVars.webEnv._POST.getArrayValue("widget-rss").getArrayValue(number), true));

            // title is optional.  If black, fill it if possible
            if (!booleanval(widget_options.getArrayValue(widget_id).getValue("title")) && isset(gVars.webEnv._POST.getArrayValue("widget-rss").getArrayValue(number).getValue("title"))) {
                // Condensed dynamic construct
                requireOnce(gVars, gConsts, RssPage.class);
                rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(strval(widget_options.getArrayValue(widget_id).getValue("url")));
                widget_options.getArrayValue(widget_id).putValue("title", Strings.htmlentities(Strings.strip_tags(strval(rss.channel.getValue("title")))));
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("dashboard_widget_options", widget_options);
        }

        getIncluded(WidgetsPage.class, gVars, gConsts).wp_widget_rss_form(widget_options.getArrayValue(widget_id), form_inputs);

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
