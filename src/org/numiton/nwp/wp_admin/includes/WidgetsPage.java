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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.FunctionHandling;
import com.numiton.RegExPerl;
import com.numiton.VarHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/WidgetsPage")
@Scope("request")
public class WidgetsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(WidgetsPage.class.getName());
    public int wp_list_widget_controls_dynamic_sidebar_i = 0;

    @Override
    @RequestMapping("/wp-admin/includes/widgets.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/widgets";
    }

    /**
     * $_search is unsanitized
     */
    public void wp_list_widgets(String show, String _search) {
        String search;
        Array<Object> search_terms = new Array<Object>();
        boolean no_widgets_shown = false;
        Array<Object> already_shown = new Array<Object>();
        Array<Object> widget = new Array<Object>();
        boolean hit = false;
        String search_text = null;
        String search_term = null;
        Object sidebar = null;
        Array<Object> args = new Array<Object>();
        Array<Object> sidebar_args = new Array<Object>();
        String widget_control_template = null;
        Object widget_id = null;
        boolean is_multi = false;
        Array<Object> add_query = new Array<Object>();
        int num = 0;
        Object id_base = null;
        String action = null;
        Object add_url = null;
        Object edit_url = null;
        Object widget_title = null;
        Object widget_description = null;
        Object name = null;

        if (booleanval(_search)) {
    		// sanitize
            search = QRegExPerl.preg_replace("/[^\\w\\s]/", "", _search);
            // array of terms
            search_terms = QRegExPerl.preg_split("/[\\s]/", search, -1, RegExPerl.PREG_SPLIT_NO_EMPTY);
        } else {
            search_terms = new Array<Object>();
        }

        if (!Array.in_array(show, new Array<Object>(new ArrayEntry<Object>("all"), new ArrayEntry<Object>("unused"), new ArrayEntry<Object>("used")))) {
            show = "all";
        }

        echo(gVars.webEnv, "\n\t<ul id=\'widget-list\'>\n\t\t");
        no_widgets_shown = true;
        already_shown = new Array<Object>();

        for (Map.Entry javaEntry260 : gVars.wp_registered_widgets.entrySet()) {
            name = javaEntry260.getKey();
            widget = (Array<Object>) javaEntry260.getValue();

            if (equal("all", show) && Array.in_array(widget.getArrayValue("callback").getValue(1), already_shown)) { // We already showed this multi-widget
                continue;
            }

            if (booleanval(search_terms)) {
                hit = false;
                // Simple case-insensitive search.  Boolean OR.
                search_text = QRegExPerl.preg_replace("/[^\\w]/", "", strval(widget.getValue("name")));

                if (isset(widget.getValue("description"))) {
                    search_text = search_text + QRegExPerl.preg_replace("/[^\\w]/", "", strval(widget.getValue("description")));
                }

                for (Map.Entry javaEntry261 : search_terms.entrySet()) {
                    search_term = strval(javaEntry261.getValue());

                    if (booleanval(Strings.stristr(search_text, search_term))) {
                        hit = true;

                        break;
                    }
                }

                if (!hit) {
                    continue;
                }
            }

            sidebar = (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).is_active_widget(
                    widget.getValue("callback"),
                    intval(widget.getValue("id")));

            if ((equal("unused", show) && booleanval(sidebar)) || (equal("used", show) && !booleanval(sidebar))) {
                continue;
            }

            OutputControl.ob_start(gVars.webEnv);
            
            args = wp_list_widget_controls_dynamic_sidebar(
                    new Array<Object>(
                        new ArrayEntry<Object>(
                            0,
                            new Array<Object>(
                                new ArrayEntry<Object>("widget_id", widget.getValue("id")),
                                new ArrayEntry<Object>("widget_name", widget.getValue("name")),
                                new ArrayEntry<Object>("_display", "template"),
                                new ArrayEntry<Object>("_show", show))), new ArrayEntry<Object>(1, widget.getArrayValue("params").getValue(0))));
            
            sidebar_args = (Array<Object>) FunctionHandling.call_user_func_array(new Callback("wp_widget_control", this), args);
            widget_control_template = OutputControl.ob_get_contents(gVars.webEnv);
            OutputControl.ob_end_clean(gVars.webEnv);
            
            widget_id = widget.getValue("id"); // save this for later in case we mess with $widget['id']
            
            is_multi = !strictEqual(BOOLEAN_FALSE, Strings.strpos(widget_control_template, "%i%"));

            if (!booleanval(sidebar) || is_multi) {
                add_query = new Array<Object>(new ArrayEntry<Object>("sidebar", sidebar), new ArrayEntry<Object>("key", false), new ArrayEntry<Object>("edit", false));

                if (equal("all", show) && is_multi) {
                	// it's a multi-widget.  We only need to show it in the list once.
                    already_shown.putValue(widget.getArrayValue("callback").getValue(1));
                    num = intval(Array.array_pop(Strings.explode("-", strval(widget.getValue("id")))));
                    id_base = gVars.wp_registered_widget_controls.getArrayValue(widget.getValue("id")).getValue("id_base");

					// so that we always add a new one when clicking "add"
                    while (isset(gVars.wp_registered_widgets.getValue(id_base + "-" + num)))
                        num++;

                    widget.putValue("id", strval(id_base) + "-" + strval(num));
                    add_query.putValue("base", id_base);
                    add_query.putValue("key", num);
                    add_query.putValue("sidebar", gVars.sidebar);
                }

                add_query.putValue("add", widget.getValue("id"));
                action = "add";
                add_url = getIncluded(FunctionsPage.class, gVars, gConsts)
                              .wp_nonce_url(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(add_query), "add-widget_" + strval(widget.getValue("id")));
            } else {
                action = "edit";
                edit_url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                            new Array<Object>(
                                new ArrayEntry<Object>("sidebar", sidebar),
                                new ArrayEntry<Object>("edit", widget.getValue("id")),
                                new ArrayEntry<Object>("key", Array.array_search(widget.getValue("id"), gVars.sidebars_widgets.getArrayValue(sidebar))))), null, "display");
                widget_control_template = "<textarea rows=\"1\" cols=\"1\">" + Strings.htmlspecialchars(widget_control_template) + "</textarea>";
            }

            widget_control_template = strval(sidebar_args.getValue("before_widget")) + widget_control_template + strval(sidebar_args.getValue("after_widget"));
            no_widgets_shown = false;

            if (!equal("all", show) && booleanval(sidebar_args.getValue("_widget_title"))) {
                widget_title = sidebar_args.getValue("_widget_title");
            } else {
                widget_title = widget.getValue("name");
            }

            echo(gVars.webEnv, "\n\t\t<li id=\"widget-list-item-");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(widget.getValue("id"))));
            echo(gVars.webEnv, "\" class=\"widget-list-item\">\n\t\t\t<h4 class=\"widget-title widget-draggable\">\n\n\t\t\t\t");
            echo(gVars.webEnv, widget_title);
            echo(gVars.webEnv, "\n\t\t\t\t");

            if (equal("add", action)) {
                echo(gVars.webEnv, "\n\t\t\t\t<a class=\"widget-action widget-control-add\" href=\"");
                echo(gVars.webEnv, add_url);
                echo(gVars.webEnv, "\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Add", "default");
                echo(gVars.webEnv, "</a>\n\n\t\t\t\t");
            } else if (equal("edit", action)) {
				// We echo a hidden edit link for the sake of the JS.  Edit links are shown (needlessly?) after a widget is added.
                echo(gVars.webEnv, "\n\t\t\t\t<a class=\"widget-action widget-control-edit\" href=\"");
                echo(gVars.webEnv, edit_url);
                echo(gVars.webEnv, "\" style=\"display: none;\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Edit", "default");
                echo(gVars.webEnv, "</a>\n\n\t\t\t\t");
            } else {
            }

            echo(gVars.webEnv, "\n\t\t\t</h4>\n\n\n\t\t\t<ul id=\"widget-control-info-");
            echo(gVars.webEnv, widget.getValue("id"));
            echo(gVars.webEnv, "\" class=\"widget-control-info\">\n\n\t\t\t\t");
            echo(gVars.webEnv, widget_control_template);
            echo(gVars.webEnv, "\n\t\t\t</ul>\n\n\t\t\t");

            if (equal("add", action)) {
                echo(gVars.webEnv, "\t\t\t");
            } else {
            }

            echo(gVars.webEnv, "\n\t\t\t<div class=\"widget-description\">\n\t\t\t\t");
            echo(gVars.webEnv,
                booleanval(widget_description = (((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).wp_widget_description(widget_id))
                ? strval(widget_description)
                : "&nbsp;");
            echo(gVars.webEnv, "\t\t\t</div>\n\n\t\t\t<br class=\"clear\" />\n\n\t\t</li>\n\n\t\t");
        }

        if (no_widgets_shown) {
            echo(gVars.webEnv, "\n\t\t<li>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No matching widgets", "default");
            echo(gVars.webEnv, "</li>\n\n\t\t");
        } else {
        }

        echo(gVars.webEnv, "\n\t</ul>\n");
    }

    public void wp_list_widget_controls(Object sidebar) {
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("dynamic_sidebar_params", Callback.createCallbackArray(this, "wp_list_widget_controls_dynamic_sidebar"), 10, 1);
        echo(gVars.webEnv, "\n\t<ul class=\"widget-control-list\">\n\n\t\t");

        if (!(((org.numiton.nwp.wp_includes.WidgetsPage) getIncluded(org.numiton.nwp.wp_includes.WidgetsPage.class, gVars, gConsts))).dynamic_sidebar(sidebar)) {
            echo(gVars.webEnv, "<li />");
        }

        echo(gVars.webEnv, "\n\t</ul>\n\n");
    }

    public Array<Object> wp_list_widget_controls_dynamic_sidebar(Array<Object> params) {
        Object widget_id = null;
        wp_list_widget_controls_dynamic_sidebar_i++;
        widget_id = params.getArrayValue(0).getValue("widget_id");
        params.getArrayValue(0).putValue(
            "before_widget",
            "<li id=\'widget-list-control-item-" + strval(wp_list_widget_controls_dynamic_sidebar_i) + "-" + strval(widget_id) + "\' class=\'widget-list-control-item widget-sortable\'>\n");
        params.getArrayValue(0).putValue("after_widget", "</li>");
        params.getArrayValue(0).putValue("before_title", "%BEG_OF_TITLE%");
        params.getArrayValue(0).putValue("after_title", "%END_OF_TITLE%");

        if (VarHandling.is_callable(new Callback(gVars.wp_registered_widgets.getArrayValue(widget_id).getArrayValue("callback")))) {
            gVars.wp_registered_widgets.getArrayValue(widget_id).putValue("_callback", gVars.wp_registered_widgets.getArrayValue(widget_id).getValue("callback"));
            gVars.wp_registered_widgets.getArrayValue(widget_id).putValue("callback", Callback.createCallbackArray(this, "wp_widget_control"));
        }

        return params;
    }

    /*
     * Meta widget used to display the control form for a widget.  Called from dynamic_sidebar()
     */
    public Array<Object> wp_widget_control(Array<Object> sidebar_args, Object... vargs) {
        Object widget_id = null;
        Object sidebar_id = null;
        Array<Object> control = new Array<Object>();
        Array<Object> widget = new Array<Object>();
        String key = null;
        boolean edit = false;
        String id_format = null;
        String widget_title = null;
        Array<Object> args = new Array<Object>();
        
        widget_id = sidebar_args.getValue("widget_id");
        sidebar_id = (isset(sidebar_args.getValue("id"))
            ? sidebar_args.getValue("id")
            : false);
        
        control = Array.arrayCopy((Array<Object>) gVars.wp_registered_widget_controls.getValue(widget_id));
        widget = Array.arrayCopy((Array<Object>) gVars.wp_registered_widgets.getValue(widget_id));
        
        key = (booleanval(sidebar_id)
            ? strval(Array.array_search(widget_id, gVars.sidebars_widgets.getArrayValue(sidebar_id)))
            : "no-key"); // position of widget in sidebar
        
        edit = (-1 < intval(gVars.edit_widget)) && is_numeric(key) && strictEqual(gVars.edit_widget, key); // (bool) are we currently editing this widget
        
        id_format = strval(widget.getValue("id"));

        // We aren't showing a widget control, we're outputing a template for a mult-widget control
        if (equal("all", sidebar_args.getValue("_show")) && equal("template", sidebar_args.getValue("_display")) && isset(control) && isset(control.getValue("params")) &&
                isset(control.getArrayValue("params").getValue(0)) && isset(control.getArrayValue("params").getArrayValue(0).getValue("number"))) {
        	// number == -1 implies a template where id numbers are replaced by a generic '%i%'
            control.getArrayValue("params").getArrayValue(0).putValue("number", -1);

            // if given, id_base means widget id's should be constructed like {$id_base}-{$id_number}
            if (isset(control.getValue("id_base"))) {
                id_format = strval(control.getValue("id_base")) + "-%i%";
            }
        }

        widget_title = "";

        // We grab the normal widget output to find the widget's title
        if ((!equal("all", sidebar_args.getValue("_show")) || !equal("template", sidebar_args.getValue("_display"))) && VarHandling.is_callable(new Callback(widget.getArrayValue("_callback")))) {
            OutputControl.ob_start(gVars.webEnv);

            // Modified by Numiton
            Object[] totalArgs = FunctionHandling.buildTotalArgs(sidebar_args, vargs);
            args = FunctionHandling.func_get_args(totalArgs);
            FunctionHandling.call_user_func_array(new Callback(widget.getArrayValue("_callback")), args);
            widget_title = OutputControl.ob_get_clean(gVars.webEnv);
            widget_title = wp_widget_control_ob_filter(widget_title);
        }

        gVars.wp_registered_widgets.getArrayValue(widget_id).putValue("callback", gVars.wp_registered_widgets.getArrayValue(widget_id).getValue("_callback"));
        gVars.wp_registered_widgets.getArrayValue(widget_id).arrayUnset("_callback");

        if (booleanval(widget_title) && !equal(widget_title, sidebar_args.getValue("widget_name"))) {
            widget_title = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s: %2$s|1: widget name, 2: widget title", "default"), sidebar_args.getValue("widget_name"), widget_title);
        } else {
            widget_title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(strval(sidebar_args.getValue("widget_name"))), strval(0));
        }

        sidebar_args.putValue("_widget_title", widget_title);

        if (empty(sidebar_args.getValue("_display")) || !equal("template", sidebar_args.getValue("_display"))) {
            echo(gVars.webEnv, sidebar_args.getValue("before_widget"));
        }

        echo(gVars.webEnv, "\t\t<h4 class=\"widget-title\">");
        echo(gVars.webEnv, widget_title);
        echo(gVars.webEnv, "\n\t\t\t");

        if (edit) {
            echo(gVars.webEnv, "\n\t\t\t<a class=\"widget-action widget-control-edit\" href=\"");
            echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(new ArrayEntry<Object>("edit"), new ArrayEntry<Object>("key"))));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Cancel", "default");
            echo(gVars.webEnv, "</a>\n\n\t\t\t");
        } else {
            echo(gVars.webEnv, "\n\t\t\t<a class=\"widget-action widget-control-edit\" href=\"");
            echo(gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                        new Array<Object>(new ArrayEntry<Object>("edit", id_format), new ArrayEntry<Object>("key", key))), null, "display"));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Edit", "default");
            echo(gVars.webEnv, "</a>\n\n\t\t\t");
        }

        echo(gVars.webEnv, "\n\t\t</h4>\n\n\t\t<div class=\"widget-control\"");

        if (edit) {
            echo(gVars.webEnv, " style=\"display: block;\"");
        }

        echo(gVars.webEnv, ">\n\n\t\t\t");

        if (booleanval(control)) {
            FunctionHandling.call_user_func_array(new Callback(control.getArrayValue("callback")), control.getArrayValue("params"));
        } else {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("There are no options for this widget.", "default") + "</p>");
        }

        echo(gVars.webEnv, "\n\t\t\t<input type=\"hidden\" name=\"widget-id[]\" value=\"");
        echo(gVars.webEnv, id_format);
        echo(gVars.webEnv, "\" />\n\t\t\t<input type=\"hidden\" class=\"widget-width\" value=\"");
        echo(gVars.webEnv, is_null(control)
            ? ""
            : control.getValue("width"));
        echo(gVars.webEnv, "\" />\n\n\t\t\t<div class=\"widget-control-actions\">\n\n\t\t\t\t");

        if (booleanval(control)) {
            echo(gVars.webEnv, "\n\t\t\t\t<a class=\"widget-action widget-control-save wp-no-js-hidden edit alignleft\" href=\"#save:");
            echo(gVars.webEnv, id_format);
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Change", "default");
            echo(gVars.webEnv, "</a>\n\n\t\t\t\t");
        } else {
        }

        echo(gVars.webEnv, "\n\t\t\t\t<a class=\"widget-action widget-control-remove delete alignright\" href=\"");
        echo(
                gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts).clean_url(
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                                new Array<Object>(new ArrayEntry<Object>("remove", id_format), new ArrayEntry<Object>("key", key))), "remove-widget_" + strval(widget.getValue("id"))),
                        null,
                        "display"));
        echo(gVars.webEnv, "\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Remove", "default");
        echo(gVars.webEnv, "</a>\n\t\t\t\t<br class=\"clear\" />\n\t\t\t</div>\n\t\t</div>\n");

        if (empty(sidebar_args.getValue("_display")) || !equal("template", sidebar_args.getValue("_display"))) {
            echo(gVars.webEnv, sidebar_args.getValue("after_widget"));
        }

        return sidebar_args;
    }

    public String wp_widget_control_ob_filter(String string) {
        int beg = 0;
        int end = 0;

        if (strictEqual(BOOLEAN_FALSE, beg = Strings.strpos(string, "%BEG_OF_TITLE%"))) {
            return "";
        }

        if (strictEqual(BOOLEAN_FALSE, end = Strings.strpos(string, "%END_OF_TITLE%"))) {
            return "";
        }

        string = Strings.substr(string, beg + 14, end - beg - 14);
        string = Strings.str_replace("&nbsp;", " ", string);

        return Strings.trim(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(string), strval(0)));
    }

    public void widget_css() {
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/widgets");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_widgets_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "widget_css"), 10, 1);

        return DEFAULT_VAL;
    }
}
