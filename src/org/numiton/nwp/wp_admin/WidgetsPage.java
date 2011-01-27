/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WidgetsPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Set;

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

import com.numiton.*;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.ExpressionHelper;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/WidgetsPage")
@Scope("request")
public class WidgetsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(WidgetsPage.class.getName());
    public Array<Object> already_done = new Array<Object>();
    public Array<Object> control = new Array<Object>();
    public String control_output;
    public Object new_sidebar;
    public Object query_args;
    public Array<Object> control_callback;
    public int num;
    public Object sidebar_widget_id;
    public int sidebar_widget_count;
    public int sidebars_count;
    public String sidebar_info_text;
    public String widget_search;
    public Array<Object> show_values = new Array<Object>();
    public String show;
    public String show_value;
    public String show_text;
    public String sidebar_id;
    public Array<Object> registered_sidebar = new Array<Object>();

    @Override
    @RequestMapping("/wp-admin/widgets.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/widgets";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_widgets_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, org.numiton.nwp.wp_admin.includes.WidgetsPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("switch_themes")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
        }

        getIncluded(Script_loaderPage.class, gVars, gConsts)
            .wp_enqueue_script(new Array<Object>(new ArrayEntry<Object>("wp-lists"), new ArrayEntry<Object>("admin-widgets")), false, new Array<Object>(), false);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("sidebar_admin_setup", "");
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Widgets", "default");
        gVars.parent_file = "themes.php";

        // $sidebar = What sidebar are we editing?
        if (isset(gVars.webEnv._GET.getValue("sidebar")) && isset(gVars.wp_registered_sidebars.getValue(gVars.webEnv._GET.getValue("sidebar")))) {
            gVars.sidebar = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("sidebar")));
        } else if (is_array(gVars.wp_registered_sidebars) && !empty(gVars.wp_registered_sidebars)) {
        	// By default we look at the first defined sidebar
            gVars.sidebar = Array.array_shift(gVars.keys = Array.array_keys(gVars.wp_registered_sidebars));
        } else {
        	// If no sidebars, die.
            requireOnce(gVars, gConsts, Admin_headerPage.class);
            echo(gVars.webEnv, "\n\t<div class=\"error\">\n\t\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No Sidebars Defined", "default");
            echo(gVars.webEnv, "</p>\n\t</div>\n\n\t<div class=\"wrap\">\n\t\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e(
                    "You are seeing this message because the theme you are currently using isn&#8217;t widget-aware, meaning that it has no sidebars that you are able to change. For information on making your theme widget-aware, please <a href=\"http://automattic.com/code/widgets/themes/\">follow these instructions</a>.",
                    "default");
            echo(gVars.webEnv, "</p>\n\t</div>\n\n");
            requireOnce(gVars, gConsts, Admin_footerPage.class);
            System.exit();
        }

        // These are the widgets grouped by sidebar
        gVars.sidebars_widgets = (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).wp_get_sidebars_widgets(true);

        if (empty(gVars.sidebars_widgets)) {
            gVars.sidebars_widgets = (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).wp_get_widget_defaults();
        }

        // for the sake of PHP warnings
        if (empty(gVars.sidebars_widgets.getValue(gVars.sidebar))) {
            gVars.sidebars_widgets.putValue(gVars.sidebar, new Array<Object>());
        }

        gVars.http_post = equal("post", Strings.strtolower(gVars.webEnv.getRequestMethod()));

        // We're updating a sidebar
        if (gVars.http_post && isset(gVars.sidebars_widgets.getValue(gVars.webEnv._POST.getValue("sidebar")))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("edit-sidebar_" + strval(gVars.webEnv._POST.getValue("sidebar")), "_wpnonce");
            
            /* Hack #1
        	 * The widget_control is overloaded.  It updates the widget's options AND echoes out the widget's HTML form.
        	 * Since we want to update before sending out any headers, we have to catch it with an output buffer,
        	 */
            OutputControl.ob_start(gVars.webEnv);
            
            /* There can be multiple widgets of the same type, but the widget_control for that
    		 * widget type needs only be called once if it's a multi-widget.
    		 */
            already_done = new Array<Object>();

            for (Map.Entry javaEntry333 : gVars.wp_registered_widget_controls.entrySet()) {
                gVars.name = strval(javaEntry333.getKey());
                control = (Array<Object>) javaEntry333.getValue();

                if (Array.in_array(control.getValue("callback"), already_done)) {
                    continue;
                }

                if (VarHandling.is_callable(new Callback(control.getArrayValue("callback")))) {
                    FunctionHandling.call_user_func_array(new Callback(control.getArrayValue("callback")), control.getArrayValue("params"));
                    control_output = OutputControl.ob_get_contents(gVars.webEnv);

                    if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(control_output, "%i%"))) {  // if it's a multi-widget, only call control function once.
                        already_done.putValue(control.getValue("callback"));
                    }
                }

                OutputControl.ob_clean(gVars.webEnv);
            }

            OutputControl.ob_end_clean(gVars.webEnv);

            // Prophylactic.  Take out empty ids.
            for (Map.Entry javaEntry334 : new Array<Object>(gVars.webEnv._POST.getValue("widget-id")).entrySet()) {
                gVars.key = strval(javaEntry334.getKey());
                gVars.val = javaEntry334.getValue();

                if (!booleanval(gVars.val)) {
                    gVars.webEnv._POST.getArrayValue("widget-id").arrayUnset(gVars.key);
                }
            }

            // Reset the key numbering and store
            new_sidebar = ((isset(gVars.webEnv._POST.getValue("widget-id")) && is_array(gVars.webEnv._POST.getValue("widget-id")))
                ? Array.array_values(gVars.webEnv._POST.getArrayValue("widget-id"))
                : new Array<Object>());
            gVars.sidebars_widgets.putValue(gVars.webEnv._POST.getValue("sidebar"), new_sidebar);
            (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).wp_set_sidebars_widgets(gVars.sidebars_widgets);
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("message", "updated"), 302);
            System.exit();
        }

        // What widget (if any) are we editing
        gVars.edit_widget = strval(-1);
        
        query_args = new Array<Object>(new ArrayEntry<Object>("add"), new ArrayEntry<Object>("remove"), new ArrayEntry<Object>("key"), new ArrayEntry<Object>("edit"),
                new ArrayEntry<Object>("_wpnonce"), new ArrayEntry<Object>("message"), new ArrayEntry<Object>("base"));

        if (isset(gVars.webEnv._GET.getValue("add")) && booleanval(gVars.webEnv._GET.getValue("add"))) {
        	// Add to the end of the sidebar
            ExpressionHelper.execExpr(control_callback);

            if (isset(gVars.wp_registered_widgets.getValue(gVars.webEnv._GET.getValue("add")))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-widget_" + strval(gVars.webEnv._GET.getValue("add")), "_wpnonce");
                gVars.sidebars_widgets.getArrayValue(gVars.sidebar).putValue(gVars.webEnv._GET.getValue("add"));
                (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).wp_set_sidebars_widgets(gVars.sidebars_widgets);
            } else if (isset(gVars.webEnv._GET.getValue("base")) && isset(gVars.webEnv._GET.getValue("key"))) { // It's a multi-widget
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("add-widget_" + strval(gVars.webEnv._GET.getValue("add")), "_wpnonce");

                // Copy minimal info from an existing instance of this widget to a new instance
                for (Map.Entry javaEntry335 : gVars.wp_registered_widget_controls.entrySet()) {
                    control = (Array<Object>) javaEntry335.getValue();

                    if (equal(gVars.webEnv._GET.getValue("base"), control.getValue("id_base"))) {
                        control_callback = control.getArrayValue("callback");
                        num = intval(gVars.webEnv._GET.getValue("key"));
                        control.getArrayValue("params").getArrayValue(0).putValue("number", num);
                        control.putValue("id", strval(control.getValue("id_base")) + "-" + num);
                        gVars.wp_registered_widget_controls.putValue(control.getValue("id"), control);
                        gVars.sidebars_widgets.getArrayValue(gVars.sidebar).putValue(control.getValue("id"));

                        break;
                    }
                }
            }

            // it's a multi-widget.  The only way to add multi-widgets without JS is to actually submit POST content...
        	// so here we go
            if (VarHandling.is_callable(new Callback(control_callback))) {
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                echo(gVars.webEnv, "\t\t<div class=\"wrap\">\n\t\t<h2>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Add Widget", "default");
                echo(gVars.webEnv, "</h2>\n\t\t<br />\n\t\t<form action=\"");
                echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(query_args), null, "display"));
                echo(gVars.webEnv, "\" method=\"post\">\n\t\t\n\t\t\t<ul class=\"widget-control-list\">\n\t\t\t\t<li class=\"widget-list-control-item\">\n\t\t\t\t\t<h4 class=\"widget-title\">");
                echo(gVars.webEnv, control.getValue("name"));
                echo(gVars.webEnv, "</h4>\n\t\t\t\t\t<div class=\"widget-control\" style=\"display: block;\">\n\t");
                FunctionHandling.call_user_func_array(new Callback(control_callback), control.getArrayValue("params"));
                echo(gVars.webEnv, "\t\t\t\t\t\t<div class=\"widget-control-actions\">\n\t\t\t\t\t\t\t<input type=\"submit\" class=\"button\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Add Widget", "default");
                echo(gVars.webEnv, "\" />\n\t\t\t\t\t\t\t<input type=\"hidden\" id=\'sidebar\' name=\'sidebar\' value=\"");
                echo(gVars.webEnv, gVars.sidebar);
                echo(gVars.webEnv, "\" />\n\t");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("edit-sidebar_" + strval(gVars.sidebar), "_wpnonce", true, true);

                for (Map.Entry javaEntry336 : (Set<Map.Entry>) gVars.sidebars_widgets.getArrayValue(gVars.sidebar).entrySet()) {
                    sidebar_widget_id = javaEntry336.getValue();
                    echo(gVars.webEnv, "\t\t\t\t\t\t\t<input type=\"hidden\" name=\'widget-id[]\' value=\"");
                    echo(gVars.webEnv, sidebar_widget_id);
                    echo(gVars.webEnv, "\" />\n\t");
                }

                echo(gVars.webEnv, "\t\t\t\t\t\t</div>\n\t\t\t\t\t</div>\n\t\t\t\t</li>\n\t\t\t</ul>\n\t\t</form>\n\t\t</div>\n\t");
                requireOnce(gVars, gConsts, Admin_footerPage.class);
                System.exit();
            }

            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(query_args), 302);
            System.exit();
        } else if (isset(gVars.webEnv._GET.getValue("remove")) && booleanval(gVars.webEnv._GET.getValue("remove")) && isset(gVars.webEnv._GET.getValue("key")) &&
                is_numeric(gVars.webEnv._GET.getValue("key"))) {
        	// Remove from sidebar the widget of type $_GET['remove'] and in position $_GET['key']
            gVars.key = strval(gVars.webEnv._GET.getValue("key"));

            if ((-1 < intval(gVars.key)) && booleanval(gVars.keys = Array.array_keys(gVars.sidebars_widgets.getArrayValue(gVars.sidebar), gVars.webEnv._GET.getValue("remove"))) &&
                    Array.in_array(gVars.key, gVars.keys)) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("remove-widget_" + strval(gVars.webEnv._GET.getValue("remove")), "_wpnonce");
                gVars.sidebars_widgets.getArrayValue(gVars.sidebar).arrayUnset(gVars.key);
                gVars.sidebars_widgets.putValue(gVars.sidebar, Array.array_values(gVars.sidebars_widgets.getArrayValue(gVars.sidebar)));
                (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).wp_set_sidebars_widgets(gVars.sidebars_widgets);
            }

            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(query_args), 302);
            System.exit();
        } else if (isset(gVars.webEnv._GET.getValue("edit")) && booleanval(gVars.webEnv._GET.getValue("edit")) && isset(gVars.webEnv._GET.getValue("key")) &&
                is_numeric(gVars.webEnv._GET.getValue("key"))) {
        	// Edit widget of type $_GET['edit'] and position $_GET['key']
            gVars.key = strval(gVars.webEnv._GET.getValue("key"));

            if ((-1 < intval(gVars.key)) && booleanval(gVars.keys = Array.array_keys(gVars.sidebars_widgets.getArrayValue(gVars.sidebar), gVars.webEnv._GET.getValue("edit"))) &&
                    Array.in_array(gVars.key, gVars.keys)) {
                gVars.edit_widget = gVars.key;
            }
        }

        // Total number of registered sidebars
        sidebar_widget_count = Array.count(gVars.sidebars_widgets.getValue(gVars.sidebar));

        // This is sort of lame since "widget" won't be converted to "widgets" in the JS
        if (1 < (sidebars_count = Array.count(gVars.wp_registered_sidebars))) {
            sidebar_info_text = getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                    "You are using %1$s widget in the \"%2$s\" sidebar.",
                    "You are using %1$s widgets in the \"%2$s\" sidebar.",
                    sidebar_widget_count,
                    "default");
        } else {
            sidebar_info_text = getIncluded(L10nPage.class, gVars, gConsts)
                                    .__ngettext("You are using %1$s widget in the sidebar.", "You are using %1$s widgets in the sidebar.", sidebar_widget_count, "default");
        }

        sidebar_info_text = QStrings.sprintf(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(sidebar_info_text, strval(0)),
                "<span id=\'widget-count\'>" + strval(sidebar_widget_count) + "</span>", gVars.wp_registered_sidebars.getArrayValue(gVars.sidebar).getValue("name"));
        gVars.page = (isset(gVars.webEnv._GET.getValue("apage"))
            ? Math.abs(intval(gVars.webEnv._GET.getValue("apage")))
            : 1);
        
        /* TODO: Paginate widgets list
        $page_links = paginate_links( array(
        	'base'    => add_query_arg( 'apage', '%#%' ),
        	'format'  => '',
        	'total'   => ceil(($total = 105 )/ 10),
        	'current' => $page
        ));
        */
        gVars.page_links = "&nbsp;";
        
        // Unsanitized!
        widget_search = strval(isset(gVars.webEnv._GET.getValue("s"))
                ? gVars.webEnv._GET.getValue("s")
                : false);
        
        // Not entirely sure what all should be here
        show_values = new Array<Object>(
                new ArrayEntry<Object>("",
                    booleanval(widget_search)
                    ? getIncluded(L10nPage.class, gVars, gConsts).__("Show any widgets", "default")
                    : getIncluded(L10nPage.class, gVars, gConsts).__("Show all widgets", "default")),
                new ArrayEntry<Object>("unused", getIncluded(L10nPage.class, gVars, gConsts).__("Show unused widgets", "default")),
                new ArrayEntry<Object>("used", getIncluded(L10nPage.class, gVars, gConsts).__("Show used widgets", "default")));
        show = strval(
                ((isset(gVars.webEnv._GET.getValue("show")) && isset(show_values.getValue(gVars.webEnv._GET.getValue("show"))))
                ? getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("show")))
                : false));
        gVars.messages = new Array<Object>(new ArrayEntry<Object>("updated", getIncluded(L10nPage.class, gVars, gConsts).__("Changes saved.", "default")));
        requireOnce(gVars, gConsts, Admin_headerPage.class);

        if (isset(gVars.webEnv._GET.getValue("message")) && isset(gVars.messages.getValue(gVars.webEnv._GET.getValue("message")))) {
            echo(gVars.webEnv, "\n<div id=\"message\" class=\"updated fade\"><p>");
            echo(gVars.webEnv, gVars.messages.getValue(gVars.webEnv._GET.getValue("message")));
            echo(gVars.webEnv, "</p></div>\n\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Widgets", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block3");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(widget_search));

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Search Widgets", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Available Widgets", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block6");

        for (Map.Entry javaEntry337 : show_values.entrySet()) {
            show_value = strval(javaEntry337.getKey());
            show_text = strval(javaEntry337.getValue());
            show_value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(show_value);
            echo(gVars.webEnv, "\t\t\t\t<option value=\'");
            echo(gVars.webEnv, show_value);
            echo(gVars.webEnv, "\'");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(show_value, show);
            echo(gVars.webEnv, ">");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(show_text, strval(0)));
            echo(gVars.webEnv, "</option>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block8");
        echo(gVars.webEnv, gVars.page_links);

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Current Widgets", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block10");

        for (Map.Entry javaEntry338 : gVars.wp_registered_sidebars.entrySet()) {
            sidebar_id = strval(javaEntry338.getKey());
            registered_sidebar = (Array<Object>) javaEntry338.getValue();
            sidebar_id = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(sidebar_id);
            echo(gVars.webEnv, "\t\t\t\t<option value=\'");
            echo(gVars.webEnv, sidebar_id);
            echo(gVars.webEnv, "\'");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(sidebar_id, strval(gVars.sidebar));
            echo(gVars.webEnv, ">");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(registered_sidebar.getValue("name")), strval(0)));
            echo(gVars.webEnv, "</option>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block12");
        getIncluded(org.numiton.nwp.wp_admin.includes.WidgetsPage.class, gVars, gConsts).wp_list_widgets(show, widget_search); // This lists all the widgets for the query ( $show, $search )

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block13");
        echo(gVars.webEnv, gVars.page_links);

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block14");
        echo(gVars.webEnv, sidebar_info_text);

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Add more from the Available Widgets section.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block16");
        getIncluded(org.numiton.nwp.wp_admin.includes.WidgetsPage.class, gVars, gConsts).wp_list_widget_controls(gVars.sidebar); // Show the control forms for each of the widgets in this sidebar

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block17");
        echo(gVars.webEnv, gVars.sidebar);

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block18");
        echo(gVars.webEnv, DateTime.time() - 1199145600); // Jan 1, 2008

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block20");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("edit-sidebar_" + strval(gVars.sidebar), "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block21");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("sidebar_admin_page", "");

        /* Start of block */
        super.startBlock("__wp_admin_widgets_block22");
        requireOnce(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
