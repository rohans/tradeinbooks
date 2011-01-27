/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WidgetsPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_includes;

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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class WidgetsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(WidgetsPage.class.getName());
    public boolean wp_widget_text_control_updated = false;
    public boolean wp_widget_categories_control_updated = false;
    public boolean wp_widget_rss_control_updated = false;
    String url = null;
    int items = 0;
    int show_summary = 0;
    int show_author = 0;
    int show_date = 0;
    String title = null;

    @Override
    @RequestMapping("/wp-includes/widgets.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/widgets";
    }

    /**
     * Template tags & API functions
     */
    public void register_sidebars(int number, Object argsObj) {
        Array<Object> _args = new Array<Object>();
        int i = 0;
        int n = 0;
        number = number;

        /* Modified by Numiton */
        Array args;

        if (is_string(argsObj)) {
            args = new Array();
            Strings.parse_str(strval(argsObj), args);
        } else {
            args = (Array) argsObj;
        }

        for (i = 1; i <= number; i++) {
            _args = args;

            if (number > 1) {
                _args.putValue("name",
                    isset(args.getValue("name"))
                    ? QStrings.sprintf(strval(args.getValue("name")), i)
                    : QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Sidebar %d", "default"), i));
            } else {
                _args.putValue("name", isset(args.getValue("name"))
                    ? args.getValue("name")
                    : getIncluded(L10nPage.class, gVars, gConsts).__("Sidebar", "default"));
            }

            if (isset(args.getValue("id"))) {
                _args.putValue("id", args.getValue("id"));
            } else {
                n = Array.count(gVars.wp_registered_sidebars);

                do {
                    n++;
                    _args.putValue("id", "sidebar-" + strval(n));
                } while (isset(gVars.wp_registered_sidebars.getValue(_args.getValue("id"))));
            }

            register_sidebar(_args);
        }
    }

    public Object register_sidebar(Object argsObj) {
        int i = 0;
        Array<Object> defaults = new Array<Object>();
        Array<Object> sidebar = new Array<Object>();

        /* Modified by Numiton */
        Array args;

        if (is_string(argsObj)) {
            args = new Array();
            Strings.parse_str(strval(argsObj), args);
        } else {
            args = (Array) argsObj;
        }

        i = Array.count(gVars.wp_registered_sidebars) + 1;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("name", QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Sidebar %d", "default"), i)),
                new ArrayEntry<Object>("id", "sidebar-" + strval(i)),
                new ArrayEntry<Object>("before_widget", "<li id=\"%1$s\" class=\"widget %2$s\">"),
                new ArrayEntry<Object>("after_widget", "</li>\n"),
                new ArrayEntry<Object>("before_title", "<h2 class=\"widgettitle\">"),
                new ArrayEntry<Object>("after_title", "</h2>\n"));
        sidebar = Array.array_merge(defaults, args);
        gVars.wp_registered_sidebars.putValue(sidebar.getValue("id"), sidebar);

        return sidebar.getValue("id");
    }

    public void unregister_sidebar(Object name) {
        if (isset(gVars.wp_registered_sidebars.getValue(name))) {
            gVars.wp_registered_sidebars.arrayUnset(name);
        }
    }

    public void register_sidebar_widget(Object name, /* Do not change type */
        Object output_callback, Object classname, Object... vargs) {
        String id = null;
        Array<Object> options = new Array<Object>();
        Array<Object> params = new Array<Object>();
        Array<Object> args = new Array<Object>();

        // Compat
        if (is_array(name)) {
            if (equal(Array.count(name), 3)) {
                name = QStrings.sprintf(strval(((Array) name).getValue(0)), strval(((Array) name).getValue(2)));
            } else {
                name = strval(((Array) name).getValue(0));
            }
        }

        id = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(name), "");
        options = new Array<Object>();

        if (!empty(classname) && is_string(classname)) {
            options.putValue("classname", classname);
        }

        // Modified by Numiton
        params = FunctionHandling.func_get_args(vargs);
        args = new Array<Object>(new ArrayEntry<Object>(id), new ArrayEntry<Object>(name), new ArrayEntry<Object>(output_callback), new ArrayEntry<Object>(options));

        if (!empty(params)) {
            args = Array.array_merge(args, params);
        }

        FunctionHandling.call_user_func_array(new Callback("wp_register_sidebar_widget", this), args);
    }

    public void wp_register_sidebar_widget(String id, Object name, Array<Object> output_callback, Array<Object> options, Object... vargs) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> widget = new Array<Object>();

        if (empty(output_callback)) {
            gVars.wp_registered_widgets.arrayUnset(id);

            return;
        }

        defaults = new Array<Object>(new ArrayEntry<Object>("classname", output_callback));
        options = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(options, defaults);
        widget = new Array<Object>(new ArrayEntry<Object>("name", name), new ArrayEntry<Object>("id", id), new ArrayEntry<Object>("callback", output_callback),
                new ArrayEntry<Object>("params", FunctionHandling.func_get_args(vargs)));

        /* Modified by Numiton */
        widget = Array.array_merge(widget, options);

        if (VarHandling.is_callable(new Callback(output_callback)) &&
                (!isset(gVars.wp_registered_widgets.getValue(id)) || booleanval(getIncluded(PluginPage.class, gVars, gConsts).did_action("widgets_init")))) {
            gVars.wp_registered_widgets.putValue(id, widget);
        }
    }

    public Object wp_widget_description(Object id) {
        if (!is_scalar(id)) {
            return null;
        }

        if (isset(gVars.wp_registered_widgets.getArrayValue(id).getValue("description"))) {
            return getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.wp_registered_widgets.getArrayValue(id).getValue("description")), strval(0));
        }

        return null;
    }

    public void unregister_sidebar_widget(Object id) {
        wp_unregister_sidebar_widget(id);
    }

    public void wp_unregister_sidebar_widget(Object id) {
        wp_register_sidebar_widget(strval(id), "", Callback.createCallbackArray(null, null), new Array<Object>());
        wp_unregister_widget_control(id);
    }

    public void register_widget_control(Object name, /* Do not change type */
        Object control_callback, Object width, Object height, Object... vargs) {
        String id = null;
        Array<Object> options = new Array<Object>();
        Array<Object> params = new Array<Object>();
        Array<Object> args = new Array<Object>();

    	// Compat
        if (is_array(name)) {
            if (equal(Array.count(name), 3)) {
                name = QStrings.sprintf(strval(((Array) name).getValue(0)), strval(((Array) name).getValue(2)));
            } else {
                name = strval(((Array) name).getValue(0));
            }
        }

        id = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(name), "");
        options = new Array<Object>();

        if (!empty(width)) {
            options.putValue("width", width);
        }

        if (!empty(height)) {
            options.putValue("height", height);
        }

        // Modified by Numiton
        params = FunctionHandling.func_get_args(vargs);
        args = new Array<Object>(new ArrayEntry<Object>(id), new ArrayEntry<Object>(name), new ArrayEntry<Object>(control_callback), new ArrayEntry<Object>(options));

        if (!empty(params)) {
            args = Array.array_merge(args, params);
        }

        FunctionHandling.call_user_func_array(new Callback("wp_register_widget_control", this), args);
    }

    /* $options: height, width, id_base
     *   height: never used
     *   width:  width of fully expanded control form.  Try hard to use the default width.
     *   id_base: for multi-widgets (widgets which allow multiple instances such as the text widget), an id_base must be provided.
     *            the widget id will ennd up looking like {$id_base}-{$unique_number}
     */
    public void wp_register_widget_control(String id, Object name, Array<Object> control_callback, Array<Object> options, Object... vargs) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> widget = new Array<Object>();

        if (empty(control_callback)) {
            gVars.wp_registered_widget_controls.arrayUnset(id);

            return;
        }

        if (isset(gVars.wp_registered_widget_controls.getValue(id)) && !booleanval(getIncluded(PluginPage.class, gVars, gConsts).did_action("widgets_init"))) {
            return;
        }

        defaults = new Array<Object>(new ArrayEntry<Object>("width", 250), new ArrayEntry<Object>("height", 200)); // height is never used
        options = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(options, defaults);
        options.putValue("width", intval(options.getValue("width")));
        options.putValue("height", intval(options.getValue("height")));
        widget = new Array<Object>(new ArrayEntry<Object>("name", name), new ArrayEntry<Object>("id", id), new ArrayEntry<Object>("callback", control_callback),
                new ArrayEntry<Object>("params", FunctionHandling.func_get_args(vargs)));

        /* Modified by Numiton */
        widget = Array.array_merge(widget, options);
        gVars.wp_registered_widget_controls.putValue(id, widget);
    }

    public void unregister_widget_control(Object id) {
        wp_unregister_widget_control(id);
    }

    public void wp_unregister_widget_control(Object id) {
        wp_register_widget_control(strval(id), "", null, new Array<Object>());
    }

    public boolean dynamic_sidebar() {
        return dynamic_sidebar(1);
    }

    public boolean dynamic_sidebar(Object index) {
        Array<Object> value = new Array<Object>();
        Object key = null;
        Array<Object> sidebars_widgets = new Array<Object>();
        Array<Object> sidebar = null;
        boolean did_one = false;
        Array<Object> params = new Array<Object>();
        Object id = null;
        String classname_ = null;
        Object cn = null;

        /* Do not change type */
        Array<Object> callback = null;

        if (is_int(index)) {
            index = "sidebar-" + strval(index);
        } else {
            index = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(index), "");

            for (Map.Entry javaEntry646 : gVars.wp_registered_sidebars.entrySet()) {
                key = javaEntry646.getKey();
                value = (Array<Object>) javaEntry646.getValue();

                if (equal(getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(value.getValue("name")), ""), index)) {
                    index = strval(key);

                    break;
                }
            }
        }

        sidebars_widgets = wp_get_sidebars_widgets(true);

        if (empty(gVars.wp_registered_sidebars.getValue(index)) || !Array.array_key_exists(index, sidebars_widgets) || !is_array(sidebars_widgets.getValue(index)) ||
                empty(sidebars_widgets.getValue(index))) {
            return false;
        }

        sidebar = gVars.wp_registered_sidebars.getArrayValue(index);
        did_one = false;

        for (Map.Entry javaEntry647 : (Set<Map.Entry>) sidebars_widgets.getArrayValue(index).entrySet()) {
            id = javaEntry647.getValue();
            params = Array.array_merge(
                    new Array<Object>(
                        new ArrayEntry<Object>(
                            Array.array_merge(
                                sidebar,
                                new Array<Object>(new ArrayEntry<Object>("widget_id", id), new ArrayEntry<Object>("widget_name", gVars.wp_registered_widgets.getArrayValue(id).getValue("name")))))),
                    new Array<Object>(gVars.wp_registered_widgets.getArrayValue(id).getValue("params")));
            
    		// Substitute HTML id and class attributes into before_widget
            classname_ = "";

            for (Map.Entry javaEntry648 : new Array<Object>(gVars.wp_registered_widgets.getArrayValue(id).getValue("classname")).entrySet()) {
                cn = javaEntry648.getValue();

                if (is_string(cn)) {
                    classname_ = classname_ + "_" + strval(cn);
                } else if (is_object(cn)) {
                    classname_ = classname_ + "_" + ClassHandling.get_class(cn);
                }
            }

            classname_ = Strings.ltrim(classname_, "_");
            params.getArrayValue(0).putValue("before_widget", QStrings.sprintf(strval(params.getArrayValue(0).getValue("before_widget")), id, classname_));
            params = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("dynamic_sidebar_params", params);
            callback = gVars.wp_registered_widgets.getArrayValue(id).getArrayValue("callback");

            if (VarHandling.is_callable(new Callback(callback))) {
                FunctionHandling.call_user_func_array(new Callback(callback), params);
                did_one = true;
            }
        }

        return did_one;
    }

    public Object is_active_widget(Object callback) {
        return is_active_widget(callback, 0);
    }

    /**
     * @return mixed false if widget is not active or id of sidebar in which the
     * widget is active
     */
    public Object is_active_widget(Object callback, int widget_id) {
        Object sidebars_widgets;

        /* Do not change type */
        Object widgets = null;

        /* Do not change type */
        Object widget = null;
        Object sidebar = null;
        sidebars_widgets = wp_get_sidebars_widgets(false);

        if (is_array(sidebars_widgets)) {
            for (Map.Entry javaEntry649 : ((Array<?>) sidebars_widgets).entrySet()) {
                sidebar = javaEntry649.getKey();
                widgets = javaEntry649.getValue();

                if (is_array(widgets)) {
                    for (Map.Entry javaEntry650 : ((Array<?>) widgets).entrySet()) {
                        widget = javaEntry650.getValue();

                        if (isset(gVars.wp_registered_widgets.getArrayValue(widget).getValue("callback")) && equal(gVars.wp_registered_widgets.getArrayValue(widget).getValue("callback"), callback)) {
                            if (!booleanval(widget_id) || equal(widget_id, gVars.wp_registered_widgets.getArrayValue(widget).getValue("id"))) {
                                return sidebar;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean is_dynamic_sidebar() {
        Array<Object> sidebars_widgets = new Array<Object>();
        Object index = null;
        Object widget = null;
        Object sidebar = null;
        sidebars_widgets = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("sidebars_widgets");

        for (Map.Entry javaEntry651 : gVars.wp_registered_sidebars.entrySet()) {
            index = javaEntry651.getKey();
            sidebar = javaEntry651.getValue();

            if (booleanval(Array.count(sidebars_widgets.getValue(index)))) {
                for (Map.Entry javaEntry652 : (Set<Map.Entry>) sidebars_widgets.getArrayValue(index).entrySet()) {
                    widget = javaEntry652.getValue();

                    if (Array.array_key_exists(widget, gVars.wp_registered_widgets)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /* Internal Functions */
    
    public Array<Object> wp_get_sidebars_widgets(boolean update) {
        Array<Object> sidebars_widgets = new Array<Object>();
        Array<Object> _sidebars_widgets = new Array<Object>();
        Object sidebar = null;

        /* Do not change type */
        String id = null;
        String name = null;
        Object index = null;
        Object i = null;
        boolean found = false;
        Array<Object> widget = new Array<Object>();
        Object widget_id = null;
        Array<Object> sidebars = new Array<Object>();
        Object widgets = null;
        sidebars_widgets = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("sidebars_widgets");

        // Added by Numiton
        if (is_null(sidebars_widgets)) {
            sidebars_widgets = new Array<Object>();
        }

        _sidebars_widgets = new Array<Object>();

        if (!isset(sidebars_widgets.getValue("array_version"))) {
            sidebars_widgets.putValue("array_version", 1);
        }

        {
            int javaSwitchSelector91 = 0;

            if (equal(sidebars_widgets.getValue("array_version"), 1)) {
                javaSwitchSelector91 = 1;
            }

            if (equal(sidebars_widgets.getValue("array_version"), 2)) {
                javaSwitchSelector91 = 2;
            }

            switch (javaSwitchSelector91) {
            case 1: {
                for (Map.Entry javaEntry653 : sidebars_widgets.entrySet()) {
                    index = javaEntry653.getKey();
                    sidebar = javaEntry653.getValue();

                    if (is_array(sidebar)) {
                        for (Map.Entry javaEntry654 : ((Array<?>) sidebar).entrySet()) {
                            i = javaEntry654.getKey();
                            name = strval(javaEntry654.getValue());
                            id = Strings.strtolower(name);

                            if (isset(gVars.wp_registered_widgets.getValue(id))) {
                                _sidebars_widgets.getArrayValue(index).putValue(i, id);

                                continue;
                            }

                            id = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(name, "");

                            if (isset(gVars.wp_registered_widgets.getValue(id))) {
                                _sidebars_widgets.getArrayValue(index).putValue(i, id);

                                continue;
                            }

                            found = false;

                            for (Map.Entry javaEntry655 : gVars.wp_registered_widgets.entrySet()) {
                                widget_id = javaEntry655.getKey();
                                widget = (Array<Object>) javaEntry655.getValue();

                                if (equal(Strings.strtolower(strval(widget.getValue("name"))), Strings.strtolower(name))) {
                                    _sidebars_widgets.getArrayValue(index).putValue(i, widget.getValue("id"));
                                    found = true;

                                    break;
                                } else if (equal(getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(widget.getValue("name")), ""),
                                            getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(name, ""))) {
                                    _sidebars_widgets.getArrayValue(index).putValue(i, widget.getValue("id"));
                                    found = true;

                                    break;
                                }
                            }

                            if (found) {
                                continue;
                            }

                            _sidebars_widgets.getArrayValue(index).arrayUnset(i);
                        }
                    }
                }

                _sidebars_widgets.putValue("array_version", 2);
                sidebars_widgets = Array.arrayCopy(_sidebars_widgets);
                _sidebars_widgets = new Array<Object>(); // Modified by Numiton
            }

            case 2: {
                sidebars = Array.array_keys(gVars.wp_registered_sidebars);

                if (!empty(sidebars)) {
    				// Move the known-good ones first
                    for (Map.Entry javaEntry656 : sidebars.entrySet()) {
                        Object idObj = javaEntry656.getValue();

                        if (Array.array_key_exists(idObj, sidebars_widgets)) {
                            _sidebars_widgets.putValue(idObj, sidebars_widgets.getValue(idObj));
                            sidebars_widgets.arrayUnset(idObj);
                            sidebars.arrayUnset(idObj);
                        }
                    }

    				// Assign to each unmatched registered sidebar the first available orphan
                    sidebars_widgets.arrayUnset("array_version");

                    while (booleanval(sidebar = Array.array_shift(sidebars)) && booleanval(widgets = Array.array_shift(sidebars_widgets)))
                        _sidebars_widgets.putValue(sidebar, widgets);

                    _sidebars_widgets.putValue("array_version", 3);
                    sidebars_widgets = Array.arrayCopy(_sidebars_widgets);
                    _sidebars_widgets = new Array<Object>();
                }

                if (update) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).update_option("sidebars_widgets", sidebars_widgets);
                }
            }
            }
        }

        sidebars_widgets.arrayUnset("array_version");

        return sidebars_widgets;
    }

    public void wp_set_sidebars_widgets(Array<Object> sidebars_widgets) {
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("sidebars_widgets", sidebars_widgets);
    }

    public Array<Object> wp_get_widget_defaults() {
        Array<Object> defaults = new Array<Object>();
        Object index = null;
        Object sidebar = null;
        defaults = new Array<Object>();

        for (Map.Entry javaEntry657 : gVars.wp_registered_sidebars.entrySet()) {
            index = javaEntry657.getKey();
            sidebar = javaEntry657.getValue();
            defaults.putValue(index, new Array<Object>());
        }

        return defaults;
    }

    /* Default Widgets */
    
    public void wp_widget_pages(Array<Object> args) {
        Array<Object> options = new Array<Object>();
        Object title = null;
        String sortby = null;
        String exclude = null;
        Object out = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_pages");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        title = (empty(options.getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Pages", "default")
            : options.getValue("title"));
        sortby = (empty(options.getValue("sortby"))
            ? "menu_order"
            : strval(options.getValue("sortby")));
        exclude = (empty(options.getValue("exclude"))
            ? ""
            : strval(options.getValue("exclude")));

        if (equal(sortby, "menu_order")) {
            sortby = "menu_order, post_title";
        }

        out = getIncluded(Post_templatePage.class, gVars, gConsts).wp_list_pages(
                new Array<Object>(new ArrayEntry<Object>("title_li", ""), new ArrayEntry<Object>("echo", 0), new ArrayEntry<Object>("sort_column", sortby), new ArrayEntry<Object>("exclude", exclude)));

        if (!empty(out)) {
            echo(gVars.webEnv, "\t");
            echo(gVars.webEnv, before_widget);
            echo(gVars.webEnv, "\t\t");
            echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
            echo(gVars.webEnv, "\t\t<ul>\n\t\t\t");
            echo(gVars.webEnv, out);
            echo(gVars.webEnv, "\t\t</ul>\n\t");
            echo(gVars.webEnv, after_widget);
        }
    }

    public void wp_widget_pages_control() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        String sortby = null;
        String title = null;
        String exclude = null;

        // Modified by Numiton
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_pages");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("pages-submit"))) {
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("pages-title")))));
            sortby = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("pages-sortby")));

            if (Array.in_array(sortby, new Array<Object>(new ArrayEntry<Object>("post_title"), new ArrayEntry<Object>("menu_order"), new ArrayEntry<Object>("ID")))) {
                newoptions.putValue("sortby", sortby);
            } else {
                newoptions.putValue("sortby", "menu_order");
            }

            newoptions.putValue("exclude", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("pages-exclude")))));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_pages", options);
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));
        exclude = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("exclude")));
        echo(gVars.webEnv, "\t\t<p><label for=\"pages-title\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input class=\"widefat\" id=\"pages-title\" name=\"pages-title\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label></p>\n\t\t<p>\n\t\t\t<label for=\"pages-sortby\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Sort by:", "default");
        echo(gVars.webEnv, "\t\t\t\t<select name=\"pages-sortby\" id=\"pages-sortby\" class=\"widefat\">\n\t\t\t\t\t<option value=\"post_title\"");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(options.getValue("sortby")), "post_title");
        echo(gVars.webEnv, ">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page title", "default");
        echo(gVars.webEnv, "</option>\n\t\t\t\t\t<option value=\"menu_order\"");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(options.getValue("sortby")), "menu_order");
        echo(gVars.webEnv, ">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page order", "default");
        echo(gVars.webEnv, "</option>\n\t\t\t\t\t<option value=\"ID\"");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(options.getValue("sortby")), "ID");
        echo(gVars.webEnv, ">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page ID", "default");
        echo(gVars.webEnv, "</option>\n\t\t\t\t</select>\n\t\t\t</label>\n\t\t</p>\n\t\t<p>\n\t\t\t<label for=\"pages-exclude\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Exclude:", "default");
        echo(gVars.webEnv, " <input type=\"text\" value=\"");
        echo(gVars.webEnv, exclude);
        echo(gVars.webEnv, "\" name=\"pages-exclude\" id=\"pages-exclude\" class=\"widefat\" /></label>\n\t\t\t<br />\n\t\t\t<small>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page IDs, separated by commas.", "default");
        echo(gVars.webEnv, "</small>\n\t\t</p>\n\t\t<input type=\"hidden\" id=\"pages-submit\" name=\"pages-submit\" value=\"1\" />\n");
    }

    public void wp_widget_links(Array<Object> args) {
        String before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = strval(Array.extractVar(args, "before_widget", before_widget, Array.EXTR_SKIP));
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_SKIP);
        before_widget = QRegExPerl.preg_replace("/id=\"[^\"]*\"/", "id=\"%id\"", before_widget);
        getIncluded(Bookmark_templatePage.class, gVars, gConsts).wp_list_bookmarks(
            new Array<Object>(
                new ArrayEntry<Object>("title_before", before_title),
                new ArrayEntry<Object>("title_after", after_title),
                new ArrayEntry<Object>("category_before", before_widget),
                new ArrayEntry<Object>("category_after", after_widget),
                new ArrayEntry<Object>("show_images", true),
                new ArrayEntry<Object>("class", "linkcat widget")));
    }

    public void wp_widget_search(Array<Object> args) {
        Object before_widget = null;
        Object after_widget = null;
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        echo(gVars.webEnv, "\t\t");
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, "\t\t\t<form id=\"searchform\" method=\"get\" action=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("home");
        echo(gVars.webEnv, "\">\n\t\t\t<div>\n\t\t\t<input type=\"text\" name=\"s\" id=\"s\" size=\"15\" /><br />\n\t\t\t<input type=\"submit\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Search", "default")));
        echo(gVars.webEnv, "\" />\n\t\t\t</div>\n\t\t\t</form>\n\t\t");
        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_archives(Array<Object> args) {
        Array<Object> options = new Array<Object>();
        String c = null;
        String d = null;
        Object title = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_archives");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        c = (booleanval(options.getValue("count"))
            ? "1"
            : "0");
        d = (booleanval(options.getValue("dropdown"))
            ? "1"
            : "0");
        title = (empty(options.getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Archives", "default")
            : options.getValue("title"));
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));

        if (booleanval(d)) {
            echo(gVars.webEnv, "\t\t<select name=\"archive-dropdown\" onchange=\'document.location.href=this.options[this.selectedIndex].value;\'> <option value=\"\">");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select Month", "default")));
            echo(gVars.webEnv, "</option> ");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_get_archives("type=monthly&format=option&show_post_count=" + c);
            echo(gVars.webEnv, " </select>\n");
        } else {
            echo(gVars.webEnv, "\t\t<ul>\n\t\t");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_get_archives("type=monthly&show_post_count=" + c);
            echo(gVars.webEnv, "\t\t</ul>\n");
        }

        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_archives_control() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        String count = null;
        String dropdown = null;
        Object title = null;
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_archives");

        // Modified by Numiton
        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("archives-submit"))) {
            newoptions.putValue("count", isset(gVars.webEnv._POST.getValue("archives-count")));
            newoptions.putValue("dropdown", isset(gVars.webEnv._POST.getValue("archives-dropdown")));
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("archives-title")))));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_archives", options);
        }

        count = (booleanval(options.getValue("count"))
            ? "checked=\"checked\""
            : "");
        dropdown = (booleanval(options.getValue("dropdown"))
            ? "checked=\"checked\""
            : "");
        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));
        echo(gVars.webEnv, "\t\t\t<p><label for=\"archives-title\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input class=\"widefat\" id=\"archives-title\" name=\"archives-title\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label></p>\n\t\t\t<p>\n\t\t\t\t<label for=\"archives-count\"><input class=\"checkbox\" type=\"checkbox\" ");
        echo(gVars.webEnv, count);
        echo(gVars.webEnv, " id=\"archives-count\" name=\"archives-count\" /> ");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show post counts", "default");
        echo(gVars.webEnv, "</label>\n\t\t\t\t<br />\n\t\t\t\t<label for=\"archives-dropdown\"><input class=\"checkbox\" type=\"checkbox\" ");
        echo(gVars.webEnv, dropdown);
        echo(gVars.webEnv, " id=\"archives-dropdown\" name=\"archives-dropdown\" /> ");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Display as a drop down", "default");
        echo(gVars.webEnv, "</label>\n\t\t\t</p>\n\t\t\t<input type=\"hidden\" id=\"archives-submit\" name=\"archives-submit\" value=\"1\" />\n");
    }

    public void wp_widget_meta(Array<Object> args) {
        Array<Object> options = new Array<Object>();
        Object title = null;
        String before_widget = null;
        String before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = strval(Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE));
        before_title = strval(Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE));
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_meta");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        title = (empty(options.getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Meta", "default")
            : options.getValue("title"));
        echo(gVars.webEnv, "\t\t");
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, "\t\t\t");
        echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
        echo(gVars.webEnv, "\t\t\t<ul>\n\t\t\t");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_register("<li>", "</li>");
        echo(gVars.webEnv, "\t\t\t<li>");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_loginout();
        echo(gVars.webEnv, "</li>\n\t\t\t<li><a href=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("rss2_url");
        echo(gVars.webEnv, "\" title=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Syndicate this site using RSS 2.0", "default")));
        echo(gVars.webEnv, "\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Entries <abbr title=\"Really Simple Syndication\">RSS</abbr>", "default");
        echo(gVars.webEnv, "</a></li>\n\t\t\t<li><a href=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("comments_rss2_url");
        echo(gVars.webEnv, "\" title=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("The latest comments to all posts in RSS", "default")));
        echo(gVars.webEnv, "\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comments <abbr title=\"Really Simple Syndication\">RSS</abbr>", "default");
        echo(gVars.webEnv, "</a></li>\n\t\t\t<li><a href=\"http://wordpress.org/\" title=\"");
        echo(
            gVars.webEnv,
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Powered by WordPress, state-of-the-art semantic personal publishing platform.", "default")));
        echo(gVars.webEnv, "\">WordPress.org</a></li>\n\t\t\t");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_meta();
        echo(gVars.webEnv, "\t\t\t</ul>\n\t\t");
        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_meta_control() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        String title = null;

        // Modified by Numiton
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_meta");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("meta-submit"))) {
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("meta-title")))));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_meta", options);
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));
        echo(gVars.webEnv, "\t\t\t<p><label for=\"meta-title\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input class=\"widefat\" id=\"meta-title\" name=\"meta-title\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label></p>\n\t\t\t<input type=\"hidden\" id=\"meta-submit\" name=\"meta-submit\" value=\"1\" />\n");
    }

    public void wp_widget_calendar(Array<Object> args) {
        Array<Object> options = new Array<Object>();
        String title = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_calendar");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        title = strval(options.getValue("title"));

        if (empty(title)) {
            title = "&nbsp;";
        }

        echo(gVars.webEnv, strval(before_widget) + strval(before_title) + title + strval(after_title));
        echo(gVars.webEnv, "<div id=\"calendar_wrap\">");
        getIncluded(General_templatePage.class, gVars, gConsts).get_calendar(true);
        echo(gVars.webEnv, "</div>");
        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_calendar_control() {
        Array<Object> options;
        Array<Object> newoptions;
        Object title = null;

        // Modified by Numiton
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_calendar");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("calendar-submit"))) {
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("calendar-title")))));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_calendar", options);
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));
        echo(gVars.webEnv, "\t\t\t<p><label for=\"calendar-title\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input class=\"widefat\" id=\"calendar-title\" name=\"calendar-title\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label></p>\n\t\t\t<input type=\"hidden\" id=\"calendar-submit\" name=\"calendar-submit\" value=\"1\" />\n");
    }

 // See large comment section at end of this file
    public void wp_widget_text(Array<Object> args, Object widget_argsObj) {
        Array<Object> options = new Array<Object>();
        Object number = null;
        Object title = null;
        Object text = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        number = Array.extractVar(args, "number", number, Array.EXTR_SKIP);
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_SKIP);

        if (is_numeric(widget_argsObj)) {
            widget_argsObj = new Array<Object>(new ArrayEntry<Object>("number", widget_argsObj));
        }

        Array widget_args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(widget_argsObj, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        number = Array.extractVar(widget_args, "number", number, Array.EXTR_SKIP);
        before_widget = Array.extractVar(widget_args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(widget_args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(widget_args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(widget_args, "after_widget", after_widget, Array.EXTR_SKIP);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_text");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        if (!isset(options.getValue(number))) {
            return;
        }

        title = options.getArrayValue(number).getValue("title");
        text = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("widget_text", options.getArrayValue(number).getValue("text"));
        echo(gVars.webEnv, "\t\t");
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, "\t\t\t");

        if (!empty(title)) {
            echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
        }

        echo(gVars.webEnv, "\t\t\t<div class=\"textwidget\">");
        echo(gVars.webEnv, text);
        echo(gVars.webEnv, "</div>\n\t\t");
        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_text_control(Object widget_argsObj) {
        Object optionsObj;

        /* Do not change type */
        String sidebar = null;
        Array<Object> sidebars_widgets = new Array<Object>();
        Array<Object> this_sidebar = new Array<Object>();
        Object _widget_id = null;
        Object widget_number = null;
        Array<Object> widget_text = new Array<Object>();
        String title = null;
        String text = null;
        String number = null;

        if (is_numeric(widget_argsObj)) {
            widget_argsObj = new Array<Object>(new ArrayEntry<Object>("number", widget_argsObj));
        }

        Array widget_args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(widget_argsObj, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        number = strval(Array.extractVar(widget_args, "number", number, Array.EXTR_SKIP));

        /* Modified by Numiton */
        optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_text");

        Array options;

        if (!is_array(optionsObj)) {
            options = new Array<Object>();
        } else {
            options = (Array) optionsObj;
        }

        if (!wp_widget_text_control_updated && !empty(gVars.webEnv._POST.getValue("sidebar"))) {
            sidebar = strval(gVars.webEnv._POST.getValue("sidebar"));
            sidebars_widgets = wp_get_sidebars_widgets(true);

            if (isset(sidebars_widgets.getValue(sidebar))) {
                this_sidebar = sidebars_widgets.getArrayValue(sidebar);
            } else {
                this_sidebar = new Array<Object>();
            }

            for (Map.Entry javaEntry658 : this_sidebar.entrySet()) {
                _widget_id = javaEntry658.getValue();

                if (equal("wp_widget_text", gVars.wp_registered_widgets.getArrayValue(_widget_id).getValue("callback")) &&
                        isset(gVars.wp_registered_widgets.getArrayValue(_widget_id).getArrayValue("params").getArrayValue(0).getValue("number"))) {
                    widget_number = gVars.wp_registered_widgets.getArrayValue(_widget_id).getArrayValue("params").getArrayValue(0).getValue("number");

                    if (!Array.in_array("text-" + strval(widget_number), gVars.webEnv._POST.getArrayValue("widget-id"))) { // the widget has been removed.
                        ((Array) options).arrayUnset(widget_number);
                    }
                }
            }

            for (Map.Entry javaEntry659 : new Array<Object>(gVars.webEnv._POST.getValue("widget-text")).entrySet()) {
                widget_number = javaEntry659.getKey();
                widget_text = (Array<Object>) javaEntry659.getValue();

                if (!isset(widget_text.getValue("text")) && isset(options.getValue(widget_number))) { // user clicked cancel
                    continue;
                }

                title = Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(widget_text.getValue("title"))));

                if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("unfiltered_html")) {
                    text = Strings.stripslashes(gVars.webEnv, strval(widget_text.getValue("text")));
                } else {
                    text = Strings.stripslashes(gVars.webEnv, getIncluded(KsesPage.class, gVars, gConsts).wp_filter_post_kses(strval(widget_text.getValue("text"))));
                }

                options.putValue(widget_number, Array.compact(new ArrayEntry("title", title), new ArrayEntry("text", text)));
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_text", options);
            wp_widget_text_control_updated = true;
        }

        if (equal(-1, number)) {
            title = "";
            text = "";
            number = "%i%";
        } else {
            title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getArrayValue(number).getValue("title")));
            text = getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(options.getArrayValue(number).getValue("text")), false);
        }

        echo(gVars.webEnv, "\t\t<p>\n\t\t\t<input class=\"widefat\" id=\"text-title-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" name=\"widget-text[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][title]\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" />\n\t\t\t<textarea class=\"widefat\" rows=\"16\" cols=\"20\" id=\"text-text-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" name=\"widget-text[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][text]\">");
        echo(gVars.webEnv, text);
        echo(gVars.webEnv, "</textarea>\n\t\t\t<input type=\"hidden\" name=\"widget-text[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][submit]\" value=\"1\" />\n\t\t</p>\n");
    }

    public void wp_widget_text_register() {
        Array<Object> options = new Array<Object>();
        Array<Object> widget_ops = new Array<Object>();
        Array<Object> control_ops = new Array<Object>();
        Object name = null;
        String id = null;
        Object o = null;
        Object optionsObj = null;

        if (!booleanval(optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_text"))) {
            options = new Array<Object>();
        } else {
            options = (Array<Object>) optionsObj;
        }

        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_text"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("Arbitrary text or HTML", "default")));
        control_ops = new Array<Object>(new ArrayEntry<Object>("width", 400), new ArrayEntry<Object>("height", 350), new ArrayEntry<Object>("id_base", "text"));
        name = getIncluded(L10nPage.class, gVars, gConsts).__("Text", "default");
        id = strval(false);

        for (Map.Entry javaEntry660 : Array.array_keys(options).entrySet()) {
            o = javaEntry660.getValue();

    		// Old widgets can have null values for some reason
            if (!isset(options.getArrayValue(o).getValue("title")) || !isset(options.getArrayValue(o).getValue("text"))) {
                continue;
            }

            id = "text-" + strval(o); // Never never never translate an id
            wp_register_sidebar_widget(id, name, Callback.createCallbackArray(this, "wp_widget_text"), widget_ops, new Array<Object>(new ArrayEntry<Object>("number", o)));
            wp_register_widget_control(id, name, Callback.createCallbackArray(this, "wp_widget_text_control"), control_ops, new Array<Object>(new ArrayEntry<Object>("number", o)));
        }

    	// If there are none, we register the widget's existance with a generic template
        if (!booleanval(id)) {
            wp_register_sidebar_widget("text-1", name, Callback.createCallbackArray(this, "wp_widget_text"), widget_ops, new Array<Object>(new ArrayEntry<Object>("number", -1)));
            wp_register_widget_control("text-1", name, Callback.createCallbackArray(this, "wp_widget_text_control"), control_ops, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        }
    }

 // See large comment section at end of this file
    public void wp_widget_categories(Array<Object> args, Object widget_argsObj) {
        Array<Object> options = new Array<Object>();
        Object number = null;
        String c = null;
        String h = null;
        String d = null;
        Object title = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        String cat_args = null;
        Object after_widget = null;
        number = Array.extractVar(args, "number", number, Array.EXTR_SKIP);
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_SKIP);

        if (is_numeric(widget_argsObj)) {
            widget_argsObj = new Array<Object>(new ArrayEntry<Object>("number", widget_argsObj));
        }

        Array widget_args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(widget_argsObj, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        number = Array.extractVar(widget_args, "number", number, Array.EXTR_SKIP);
        before_widget = Array.extractVar(widget_args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(widget_args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(widget_args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(widget_args, "after_widget", after_widget, Array.EXTR_SKIP);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_categories");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        if (!isset(options.getValue(number))) {
            return;
        }

        c = (booleanval(options.getArrayValue(number).getValue("count"))
            ? "1"
            : "0");
        h = (booleanval(options.getArrayValue(number).getValue("hierarchical"))
            ? "1"
            : "0");
        d = (booleanval(options.getArrayValue(number).getValue("dropdown"))
            ? "1"
            : "0");
        title = (empty(options.getArrayValue(number).getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default")
            : options.getArrayValue(number).getValue("title"));
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
        cat_args = "orderby=name&show_count=" + c + "&hierarchical=" + h;

        if (booleanval(d)) {
            getIncluded(Category_templatePage.class, gVars, gConsts)
                .wp_dropdown_categories(cat_args + "&show_option_none= " + getIncluded(L10nPage.class, gVars, gConsts).__("Select Category", "default"));
            echo(
                    gVars.webEnv,
                    "\n<script type=\'text/javascript\'>\n/* <![CDATA[ */\n    var dropdown = document.getElementById(\"cat\");\n    function onCatChange() {\n\t\tif ( dropdown.options[dropdown.selectedIndex].value > 0 ) {\n\t\t\tlocation.href = \"");
            echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
            echo(gVars.webEnv, "/?cat=\"+dropdown.options[dropdown.selectedIndex].value;\n\t\t}\n    }\n    dropdown.onchange = onCatChange;\n/* ]]> */\n</script>\n\n");
        } else {
            echo(gVars.webEnv, "\t\t<ul>\n\t\t");
            getIncluded(Category_templatePage.class, gVars, gConsts).wp_list_categories(cat_args + "&title_li=");
            echo(gVars.webEnv, "\t\t</ul>\n");
        }

        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_categories_control(Object widget_argsObj) {
        Object optionsObj;

        /* Do not change type */
        String sidebar = null;
        Array<Object> sidebars_widgets = new Array<Object>();
        Array<Object> this_sidebar = new Array<Object>();
        Object _widget_id = null;
        Object widget_number = null;
        Array<Object> widget_cat = new Array<Object>();
        String title = null;
        Boolean count = null;
        Boolean hierarchical = null;
        Boolean dropdown = null;
        String number = null;

        if (is_numeric(widget_argsObj)) {
            widget_argsObj = new Array<Object>(new ArrayEntry<Object>("number", widget_argsObj));
        }

        Array widget_args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(widget_argsObj, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        number = strval(Array.extractVar(widget_args, "number", number, Array.EXTR_SKIP));
        optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_categories");

        /* Modified by Numiton */
        Array options;

        if (!is_array(optionsObj)) {
            options = new Array<Object>();
        } else {
            options = (Array) optionsObj;
        }

        if (!wp_widget_categories_control_updated && !empty(gVars.webEnv._POST.getValue("sidebar"))) {
            sidebar = strval(gVars.webEnv._POST.getValue("sidebar"));
            sidebars_widgets = wp_get_sidebars_widgets(true);

            if (isset(sidebars_widgets.getValue(sidebar))) {
                this_sidebar = sidebars_widgets.getArrayValue(sidebar);
            } else {
                this_sidebar = new Array<Object>();
            }

            for (Map.Entry javaEntry661 : this_sidebar.entrySet()) {
                _widget_id = javaEntry661.getValue();

                if (equal("wp_widget_categories", gVars.wp_registered_widgets.getArrayValue(_widget_id).getValue("callback")) &&
                        isset(gVars.wp_registered_widgets.getArrayValue(_widget_id).getArrayValue("params").getArrayValue(0).getValue("number"))) {
                    widget_number = gVars.wp_registered_widgets.getArrayValue(_widget_id).getArrayValue("params").getArrayValue(0).getValue("number");

                    if (!Array.in_array("categories-" + strval(widget_number), gVars.webEnv._POST.getArrayValue("widget-id"))) { // the widget has been removed.
                        options.arrayUnset(widget_number);
                    }
                }
            }

            for (Map.Entry javaEntry662 : new Array<Object>(gVars.webEnv._POST.getValue("widget-categories")).entrySet()) {
                widget_number = javaEntry662.getKey();
                widget_cat = (Array<Object>) javaEntry662.getValue();

                if (!isset(widget_cat.getValue("title")) && isset(options.getValue(widget_number))) { // user clicked cancel
                    continue;
                }

                title = Strings.trim(Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(widget_cat.getValue("title")))));
                count = isset(widget_cat.getValue("count"));
                hierarchical = isset(widget_cat.getValue("hierarchical"));
                dropdown = isset(widget_cat.getValue("dropdown"));
                options.putValue(
                    widget_number,
                    Array.compact(new ArrayEntry("title", title), new ArrayEntry("count", count), new ArrayEntry("hierarchical", hierarchical), new ArrayEntry("dropdown", dropdown)));
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_categories", options);
            wp_widget_categories_control_updated = true;
        }

        if (equal(-1, number)) {
            title = "";
            count = false;
            hierarchical = false;
            dropdown = false;
            number = "%i%";
        } else {
            title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getArrayValue(number).getValue("title")));
            count = booleanval(options.getArrayValue(number).getValue("count"));
            hierarchical = booleanval(options.getArrayValue(number).getValue("hierarchical"));
            dropdown = booleanval(options.getArrayValue(number).getValue("dropdown"));
        }

        echo(gVars.webEnv, "\t\t\t<p>\n\t\t\t\t<label for=\"categories-title-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\">\n\t\t\t\t\t");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, "\t\t\t\t\t<input class=\"widefat\" id=\"categories-title-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" name=\"widget-categories[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][title]\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" />\n\t\t\t\t</label>\n\t\t\t</p>\n\n\t\t\t<p>\n\t\t\t\t<label for=\"categories-dropdown-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\">\n\t\t\t\t\t<input type=\"checkbox\" class=\"checkbox\" id=\"categories-dropdown-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" name=\"widget-categories[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][dropdown]\"");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(dropdown, true);
        echo(gVars.webEnv, " />\n\t\t\t\t\t");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show as dropdown", "default");
        echo(gVars.webEnv, "\t\t\t\t</label>\n\t\t\t\t<br />\n\t\t\t\t<label for=\"categories-count-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\">\n\t\t\t\t\t<input type=\"checkbox\" class=\"checkbox\" id=\"categories-count-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" name=\"widget-categories[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][count]\"");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(count, true);
        echo(gVars.webEnv, " />\n\t\t\t\t\t");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show post counts", "default");
        echo(gVars.webEnv, "\t\t\t\t</label>\n\t\t\t\t<br />\n\t\t\t\t<label for=\"categories-hierarchical-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\">\n\t\t\t\t\t<input type=\"checkbox\" class=\"checkbox\" id=\"categories-hierarchical-");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" name=\"widget-categories[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][hierarchical]\"");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(hierarchical, true);
        echo(gVars.webEnv, " />\n\t\t\t\t\t");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show hierarchy", "default");
        echo(gVars.webEnv, "\t\t\t\t</label>\n\t\t\t</p>\n\n\t\t\t<input type=\"hidden\" name=\"widget-categories[");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "][submit]\" value=\"1\" />\n");
    }

    public void wp_widget_categories_register() {
        Object options;
        Array<Object> widget_ops = new Array<Object>();
        Object name = null;
        String id = null;
        Object o = null;

        if (!booleanval(options = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_categories"))) {
            options = new Array<Object>();
        }

        if (isset(((Array) options).getValue("title"))) {
            options = wp_widget_categories_upgrade();
        }

        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_categories"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("A list or dropdown of categories", "default")));
        name = getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default");
        id = strval(false);

        for (Map.Entry javaEntry663 : (Set<Map.Entry>) Array.array_keys((Array) options).entrySet()) {
            o = javaEntry663.getValue();

    		// Old widgets can have null values for some reason
            if (!isset(((Array) options).getArrayValue(o).getValue("title"))) {
                continue;
            }

            id = "categories-" + strval(o);
            wp_register_sidebar_widget(id, name, Callback.createCallbackArray(this, "wp_widget_categories"), widget_ops, new Array<Object>(new ArrayEntry<Object>("number", o)));
            wp_register_widget_control(
                id,
                name,
                Callback.createCallbackArray(this, "wp_widget_categories_control"),
                new Array<Object>(new ArrayEntry<Object>("id_base", "categories")),
                new Array<Object>(new ArrayEntry<Object>("number", o)));
        }

    	// If there are none, we register the widget's existance with a generic template
        if (!booleanval(id)) {
            wp_register_sidebar_widget("categories-1", name, Callback.createCallbackArray(this, "wp_widget_categories"), widget_ops, new Array<Object>(new ArrayEntry<Object>("number", -1)));
            wp_register_widget_control(
                "categories-1",
                name,
                Callback.createCallbackArray(this, "wp_widget_categories_control"),
                new Array<Object>(new ArrayEntry<Object>("id_base", "categories")),
                new Array<Object>(new ArrayEntry<Object>("number", -1)));
        }
    }

    public Array<Object> wp_widget_categories_upgrade() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        Object sidebars_widgets = null;

        /* Do not change type */
        Object widgets = null;

        /* Do not change type */
        Array<Object> new_widgets = new Array<Object>();
        Object sidebar = null;
        Object widget = null;
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_categories");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        if (!isset(options.getValue("title"))) {
            return options;
        }

        newoptions = new Array<Object>(new ArrayEntry<Object>(1, options));
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_categories", newoptions);
        sidebars_widgets = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("sidebars_widgets");

        if (is_array(sidebars_widgets)) {
            for (Map.Entry javaEntry664 : ((Array<?>) sidebars_widgets).entrySet()) {
                sidebar = javaEntry664.getKey();
                widgets = javaEntry664.getValue();

                if (is_array(widgets)) {
                    for (Map.Entry javaEntry665 : ((Array<?>) widgets).entrySet()) {
                        widget = javaEntry665.getValue();
                        new_widgets.getArrayValue(sidebar).putValue(equal(widget, "categories")
                            ? "categories-1"
                            : strval(widget));
                    }
                } else {
                    new_widgets.putValue(sidebar, widgets);
                }
            }

            if (!equal(new_widgets, sidebars_widgets)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).update_option("sidebars_widgets", new_widgets);
            }
        }

        return newoptions;
    }

    public int wp_widget_recent_entries(Array<Object> args) {
        Object output = null;
        Array<Object> options = new Array<Object>();
        Object title = null;
        int number = 0;
        WP_Query r = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;

        if (!equal("%BEG_OF_TITLE%", args.getValue("before_title"))) {
            if (booleanval(output = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("widget_recent_entries", "widget"))) {
                return print(gVars.webEnv, output);
            }

            OutputControl.ob_start(gVars.webEnv);
        }

        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_recent_entries");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        title = (empty(options.getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Recent Posts", "default")
            : options.getValue("title"));

        if (!booleanval(number = intval(options.getValue("number")))) {
            number = 10;
        } else if (number < 1) {
            number = 1;
        } else if (number > 15) {
            number = 15;
        }

        r = new WP_Query(gVars, gConsts, "showposts=" + strval(number) + "&what_to_show=posts&nopaging=0&post_status=publish");

        if (r.have_posts()) {
            echo(gVars.webEnv, "\t\t");
            echo(gVars.webEnv, before_widget);
            echo(gVars.webEnv, "\t\t\t");
            echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
            echo(gVars.webEnv, "\t\t\t<ul>\n\t\t\t");

            while (r.have_posts()) {
                r.the_post();
                echo(gVars.webEnv, "\t\t\t<li><a href=\"");
                getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
                echo(gVars.webEnv, "\">");

                if (booleanval(getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0))) {
                    getIncluded(Post_templatePage.class, gVars, gConsts).the_title("", "", true);
                } else {
                    getIncluded(Post_templatePage.class, gVars, gConsts).the_ID();
                }

                echo(gVars.webEnv, " </a></li>\n\t\t\t");
            }

            echo(gVars.webEnv, "\t\t\t</ul>\n\t\t");
            echo(gVars.webEnv, after_widget);
            getIncluded(QueryPage.class, gVars, gConsts).wp_reset_query();  // Restore global post data stomped by the_post().
        } else {
        }

        if (!equal("%BEG_OF_TITLE%", args.getValue("before_title"))) {
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("widget_recent_entries", OutputControl.ob_get_flush(gVars.webEnv), "widget", 0);
        }

        return 0;
    }

    public void wp_flush_widget_recent_entries(Object... deprecated) {
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("widget_recent_entries", "widget");
    }

    public void wp_widget_recent_entries_control() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        Object title = null;
        int number = 0;
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_recent_entries");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("recent-entries-submit"))) {
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("recent-entries-title")))));
            newoptions.putValue("number", intval(gVars.webEnv._POST.getValue("recent-entries-number")));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_recent_entries", options);
            wp_flush_widget_recent_entries();
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));

        if (!booleanval(number = intval(options.getValue("number")))) {
            number = 5;
        }

        echo(gVars.webEnv, "\n\t\t\t<p><label for=\"recent-entries-title\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input class=\"widefat\" id=\"recent-entries-title\" name=\"recent-entries-title\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label></p>\n\t\t\t<p>\n\t\t\t\t<label for=\"recent-entries-number\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Number of posts to show:", "default");
        echo(gVars.webEnv, " <input style=\"width: 25px; text-align: center;\" id=\"recent-entries-number\" name=\"recent-entries-number\" type=\"text\" value=\"");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" /></label>\n\t\t\t\t<br />\n\t\t\t\t<small>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("(at most 15)", "default");
        echo(gVars.webEnv, "</small>\n\t\t\t</p>\n\t\t\t<input type=\"hidden\" id=\"recent-entries-submit\" name=\"recent-entries-submit\" value=\"1\" />\n");
    }

    public void wp_widget_recent_comments(Array<Object> args) {
        Array<Object> options = new Array<Object>();
        Object title = null;
        Integer number = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_SKIP);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_recent_comments");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        title = (empty(options.getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Recent Comments", "default")
            : options.getValue("title"));

        if (!booleanval(number = intval(options.getValue("number")))) {
            number = 5;
        } else if (number < 1) {
            number = 1;
        } else if (number > 15) {
            number = 15;
        }

        if (!booleanval(gVars.comments = (Array<StdClass>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("recent_comments", "widget"))) {
            gVars.comments = gVars.wpdb.get_results(
                        "SELECT comment_author, comment_author_url, comment_ID, comment_post_ID FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'1\' ORDER BY comment_date_gmt DESC LIMIT " +
                        number);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("recent_comments", gVars.comments, "widget", 0);
        }

        echo(gVars.webEnv, "\n\t\t");
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, "\t\t\t");
        echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
        echo(gVars.webEnv, "\t\t\t<ul id=\"recentcomments\">");

        if (booleanval(gVars.comments)) {
            for (Map.Entry javaEntry666 : gVars.comments.entrySet()) {
                gVars.comment = (StdClass) javaEntry666.getValue();
                echo(
                        gVars.webEnv,
                        "<li class=\"recentcomments\">" +
                        QStrings.sprintf(
                                getIncluded(L10nPage.class, gVars, gConsts).__("%1$s on %2$s", "default"),
                                getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_author_link(),
                                "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(intval(StdClass.getValue(gVars.comment, "comment_post_ID")), false) + "#comment-" +
                                intval(StdClass.getValue(gVars.comment, "comment_ID")) + "\">" +
                                getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(intval(StdClass.getValue(gVars.comment, "comment_post_ID"))) + "</a>") + "</li>");
            }
        } else {
        }

        echo(gVars.webEnv, "</ul>\n\t\t");
        echo(gVars.webEnv, after_widget);
    }

    public void wp_delete_recent_comments_cache(Object... deprecated) {
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("recent_comments", "widget");
    }

    public void wp_widget_recent_comments_control() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        Object title = null;
        int number = 0;
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_recent_comments");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("recent-comments-submit"))) {
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("recent-comments-title")))));
            newoptions.putValue("number", intval(gVars.webEnv._POST.getValue("recent-comments-number")));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_recent_comments", options);
            wp_delete_recent_comments_cache();
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));

        if (!booleanval(number = intval(options.getValue("number")))) {
            number = 5;
        }

        echo(gVars.webEnv, "\t\t\t<p><label for=\"recent-comments-title\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input class=\"widefat\" id=\"recent-comments-title\" name=\"recent-comments-title\" type=\"text\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label></p>\n\t\t\t<p>\n\t\t\t\t<label for=\"recent-comments-number\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Number of comments to show:", "default");
        echo(gVars.webEnv, " <input style=\"width: 25px; text-align: center;\" id=\"recent-comments-number\" name=\"recent-comments-number\" type=\"text\" value=\"");
        echo(gVars.webEnv, number);
        echo(gVars.webEnv, "\" /></label>\n\t\t\t\t<br />\n\t\t\t\t<small>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("(at most 15)", "default");
        echo(gVars.webEnv, "</small>\n\t\t\t</p>\n\t\t\t<input type=\"hidden\" id=\"recent-comments-submit\" name=\"recent-comments-submit\" value=\"1\" />\n");
    }

    public void wp_widget_recent_comments_style() {
        echo(gVars.webEnv, "<style type=\"text/css\">.recentcomments a{display:inline !important;padding: 0 !important;margin: 0 !important;}</style>\n");
    }

    public void wp_widget_recent_comments_register() {
        Array<Object> widget_ops = new Array<Object>();
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_recent_comments"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("The most recent comments", "default")));
        wp_register_sidebar_widget(
            "recent-comments",
            getIncluded(L10nPage.class, gVars, gConsts).__("Recent Comments", "default"),
            Callback.createCallbackArray(this, "wp_widget_recent_comments"),
            widget_ops);
        wp_register_widget_control(
            "recent-comments",
            getIncluded(L10nPage.class, gVars, gConsts).__("Recent Comments", "default"),
            Callback.createCallbackArray(this, "wp_widget_recent_comments_control"),
            new Array<Object>());

        if (booleanval(is_active_widget("wp_widget_recent_comments", intval(false)))) {
            getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(this, "wp_widget_recent_comments_style"), 10, 1);
        }
    }

 // See large comment section at end of this file
    public void wp_widget_rss(Array<Object> args, Object widget_argsObj) {
        Array<Object> options = new Array<Object>();
        Object number = null;
        String url = null;
        MagpieRSS rss = null;
        String link = null;
        Object desc = null;
        Object title = null;
        String icon = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        number = Array.extractVar(args, "number", number, Array.EXTR_SKIP);
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_SKIP);

        if (is_numeric(widget_argsObj)) {
            widget_argsObj = new Array<Object>(new ArrayEntry<Object>("number", widget_argsObj));
        }

        /* Bug fix by Numiton */
        Array widget_args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(widget_argsObj, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        number = Array.extractVar(widget_args, "number", number, Array.EXTR_SKIP);
        before_widget = Array.extractVar(widget_args, "before_widget", before_widget, Array.EXTR_SKIP);
        before_title = Array.extractVar(widget_args, "before_title", before_title, Array.EXTR_SKIP);
        after_title = Array.extractVar(widget_args, "after_title", after_title, Array.EXTR_SKIP);
        after_widget = Array.extractVar(widget_args, "after_widget", after_widget, Array.EXTR_SKIP);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_rss");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        if (!isset(options.getValue(number))) {
            return;
        }

        if (isset(options.getArrayValue(number).getValue("error")) && booleanval(options.getArrayValue(number).getValue("error"))) {
            return;
        }

        url = strval(options.getArrayValue(number).getValue("url"));

        while (!equal(Strings.strstr(url, "http"), url))
            url = Strings.substr(url, 1);

        if (empty(url)) {
            return;
        }

        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, RssPage.class);
        rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(url);
        link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.strip_tags(strval(rss.channel.getValue("link"))), null, "display");

        while (!equal(Strings.strstr(link, "http"), link))
            link = Strings.substr(link, 1);

        desc = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.strip_tags(Strings.html_entity_decode(strval(rss.channel.getValue("description")), Strings.ENT_QUOTES)));
        title = options.getArrayValue(number).getValue("title");

        if (empty(title)) {
            title = Strings.htmlentities(Strings.strip_tags(strval(rss.channel.getValue("title"))));
        }

        if (empty(title)) {
            title = desc;
        }

        if (empty(title)) {
            title = getIncluded(L10nPage.class, gVars, gConsts).__("Unknown Feed", "default");
        }

        url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.strip_tags(url), null, "display");

        if (FileSystemOrSocket.file_exists(gVars.webEnv, FileSystemOrSocket.dirname(SourceCodeInfo.getCurrentFile(gVars.webEnv)) + "/rss.png")) {
            icon = Strings.str_replace(
                    gConsts.getABSPATH(),
                    getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/",
                    FileSystemOrSocket.dirname(SourceCodeInfo.getCurrentFile(gVars.webEnv))) + "/rss.png";
        } else {
            icon = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-includes/images/rss.png";
        }

        title = "<a class=\'rsswidget\' href=\'" + url + "\' title=\'" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Syndicate this content", "default")) +
            "\'><img style=\'background:orange;color:white;border:none;\' width=\'14\' height=\'14\' src=\'" + icon + "\' alt=\'RSS\' /></a> <a class=\'rsswidget\' href=\'" + link + "\' title=\'" +
            strval(desc) + "\'>" + strval(title) + "</a>";
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
        wp_widget_rss_output(rss, options.getArrayValue(number));
        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_rss_output(Object rss, /* Do not change type */
        Array<Object> args) {
        Integer items = null;
        Integer show_summary = null;
        Integer show_author = null;
        Integer show_date = null;
        Array<Object> item = new Array<Object>();
        Object link = null;
        Object title = null;
        String desc = null;
        String summary = null;
        String date = null;
        Integer date_stamp = null;
        String author = null;

        if (is_string(rss)) {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, RssPage.class);

            if (!booleanval(rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(strval(rss)))) {
                return;
            }
        } else if (is_array(rss) && isset(((Array) rss).getValue("url"))) {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, RssPage.class);
            args = (Array<Object>) rss;

            if (!booleanval(rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(strval(((Array) rss).getValue("url"))))) {
                return;
            }
        } else if (!is_object(rss)) {
            return;
        }

        items = intval(Array.extractVar(args, "items", items, Array.EXTR_SKIP));
        show_summary = intval(Array.extractVar(args, "show_summary", show_summary, Array.EXTR_SKIP));
        show_author = intval(Array.extractVar(args, "show_author", show_author, Array.EXTR_SKIP));
        show_date = intval(Array.extractVar(args, "show_date", show_date, Array.EXTR_SKIP));

        //		items = intval(items);
        if ((items < 1) || (20 < items)) {
            items = 10;
        }

        //		show_summary = intval(show_summary);

        //		show_author = intval(show_author);

        //		show_date = intval(show_date);
        if (is_array(((MagpieRSS) rss).items) && !empty(((MagpieRSS) rss).items)) {
            ((MagpieRSS) rss).items = Array.array_slice(((MagpieRSS) rss).items, 0, items);
            echo(gVars.webEnv, "<ul>");

            for (Map.Entry javaEntry667 : ((MagpieRSS) rss).items.entrySet()) {
                item = (Array<Object>) javaEntry667.getValue();

                while (!equal(Strings.strstr(strval(item.getValue("link")), "http"), item.getValue("link")))
                    item.putValue("link", Strings.substr(strval(item.getValue("link")), 1));

                link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.strip_tags(strval(item.getValue("link"))), null, "display");
                title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.strip_tags(strval(item.getValue("title"))));

                if (empty(title)) {
                    title = getIncluded(L10nPage.class, gVars, gConsts).__("Untitled", "default");
                }

                desc = "";

                if (isset(item.getValue("description")) && is_string(item.getValue("description"))) {
                    desc = Strings.str_replace(
                            new Array<Object>(new ArrayEntry<Object>("\n"), new ArrayEntry<Object>("\r")),
                            " ",
                            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                                Strings.strip_tags(Strings.html_entity_decode(strval(item.getValue("description")), Strings.ENT_QUOTES))));
                } else if (isset(item.getValue("summary")) && is_string(item.getValue("summary"))) {
                    desc = Strings.str_replace(
                            new Array<Object>(new ArrayEntry<Object>("\n"), new ArrayEntry<Object>("\r")),
                            " ",
                            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.strip_tags(Strings.html_entity_decode(strval(item.getValue("summary")), Strings.ENT_QUOTES))));
                }

                summary = "";

                if (isset(item.getValue("description")) && is_string(item.getValue("description"))) {
                    summary = strval(item.getValue("description"));
                } else if (isset(item.getValue("summary")) && is_string(item.getValue("summary"))) {
                    summary = strval(item.getValue("summary"));
                }

                desc = Strings.str_replace(
                        new Array<Object>(new ArrayEntry<Object>("\n"), new ArrayEntry<Object>("\r")),
                        " ",
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.strip_tags(Strings.html_entity_decode(summary, Strings.ENT_QUOTES))));

                if (booleanval(show_summary)) {
                    desc = "";
                    summary = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(summary, strval(0));
                    summary = "<div class=\'rssSummary\'>" + summary + "</div>";
                } else {
                    summary = "";
                }

                date = "";

                if (booleanval(show_date)) {
                    if (isset(item.getValue("pubdate"))) {
                        date = strval(item.getValue("pubdate"));
                    } else if (isset(item.getValue("published"))) {
                        date = strval(item.getValue("published"));
                    }

                    if (booleanval(date)) {
                        if (booleanval(date_stamp = QDateTime.strtotime(date))) {
                            date = "<span class=\"rss-date\">" +
                                getIncluded(FunctionsPage.class, gVars, gConsts).date_i18n(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")), date_stamp) + "</span>";
                        } else {
                            date = "";
                        }
                    }
                }

                author = "";

                if (booleanval(show_author)) {
                    if (isset(item.getArrayValue("dc").getValue("creator"))) {
                        author = " <cite>" + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(strval(item.getArrayValue("dc").getValue("creator"))), strval(0)) +
                            "</cite>";
                    } else if (isset(item.getValue("author_name"))) {
                        author = " <cite>" + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(strval(item.getValue("author_name"))), strval(0)) + "</cite>";
                    }
                }

                echo(gVars.webEnv, "<li><a class=\'rsswidget\' href=\'" + strval(link) + "\' title=\'" + desc + "\'>" + strval(title) + "</a>" + date + summary + author + "</li>");
            }

            echo(gVars.webEnv, "</ul>");
        } else {
            echo(gVars.webEnv, "<ul><li>" + getIncluded(L10nPage.class, gVars, gConsts).__("An error has occurred; the feed is probably down. Try again later.", "default") + "</li></ul>");
        }
    }

    public void wp_widget_rss_control(Object widget_argsObj) {
        Array<Object> options;
        Array<Object> urls = new Array<Object>();
        Array<Object> option = new Array<Object>();
        String sidebar = null;
        Array<Object> sidebars_widgets = new Array<Object>();
        Array<Object> this_sidebar = new Array<Object>();
        Object _widget_id = null;
        Object widget_number = null;
        Array<Object> widget_rss = new Array<Object>();
        String url = null;
        String number = null;
        String title = null;
        Integer items = null;
        Boolean error = null;
        Integer show_summary = null;
        Integer show_author = null;
        Integer show_date = null;

        if (is_numeric(widget_argsObj)) {
            widget_argsObj = new Array<Object>(new ArrayEntry<Object>("number", widget_argsObj));
        }

        Array widget_args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(widget_argsObj, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        number = strval(Array.extractVar(widget_args, "number", number, Array.EXTR_SKIP));
        title = strval(Array.extractVar(widget_args, "title", title, Array.EXTR_SKIP));
        items = intval(Array.extractVar(widget_args, "items", items, Array.EXTR_SKIP));
        error = booleanval(Array.extractVar(widget_args, "error", error, Array.EXTR_SKIP));
        show_summary = intval(Array.extractVar(widget_args, "show_summary", show_summary, Array.EXTR_SKIP));
        show_author = intval(Array.extractVar(widget_args, "show_author", show_author, Array.EXTR_SKIP));
        show_date = intval(Array.extractVar(widget_args, "show_date", show_date, Array.EXTR_SKIP));

        Object optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_rss");

        if (!is_array(optionsObj)) {
            options = new Array<Object>();
        } else {
            options = (Array) optionsObj;
        }

        urls = new Array<Object>();

        for (Map.Entry javaEntry668 : options.entrySet()) {
            option = (Array<Object>) javaEntry668.getValue();

            if (isset(option.getValue("url"))) {
                urls.putValue(option.getValue("url"), true);
            }
        }

        if (!wp_widget_rss_control_updated && equal("POST", gVars.webEnv.getRequestMethod()) && !empty(gVars.webEnv._POST.getValue("sidebar"))) {
            sidebar = strval(gVars.webEnv._POST.getValue("sidebar"));
            sidebars_widgets = wp_get_sidebars_widgets(true);

            if (isset(sidebars_widgets.getValue(sidebar))) {
                this_sidebar = sidebars_widgets.getArrayValue(sidebar);
            } else {
                this_sidebar = new Array<Object>();
            }

            for (Map.Entry javaEntry669 : this_sidebar.entrySet()) {
                _widget_id = javaEntry669.getValue();

                if (equal("wp_widget_rss", gVars.wp_registered_widgets.getArrayValue(_widget_id).getValue("callback")) &&
                        isset(gVars.wp_registered_widgets.getArrayValue(_widget_id).getArrayValue("params").getArrayValue(0).getValue("number"))) {
                    widget_number = gVars.wp_registered_widgets.getArrayValue(_widget_id).getArrayValue("params").getArrayValue(0).getValue("number");

                    if (!Array.in_array("rss-" + strval(widget_number), gVars.webEnv._POST.getArrayValue("widget-id"))) { // the widget has been removed.
                        options.arrayUnset(widget_number);
                    }
                }
            }

            for (Map.Entry javaEntry670 : new Array<Object>(gVars.webEnv._POST.getValue("widget-rss")).entrySet()) {
                widget_number = javaEntry670.getKey();
                widget_rss = (Array<Object>) javaEntry670.getValue();

                if (!isset(widget_rss.getValue("url")) && isset(options.getValue(widget_number))) { // user clicked cancel
                    continue;
                }

                widget_rss = (Array<Object>) getIncluded(FormattingPage.class, gVars, gConsts).stripslashes_deep(widget_rss);
                url = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_url(Strings.strip_tags(strval(widget_rss.getValue("url"))), null);
                options.putValue(widget_number, wp_widget_rss_process(widget_rss, !isset(urls.getValue(url))));
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_rss", options);
            wp_widget_rss_control_updated = true;
        }

        if (equal(-1, number)) {
            title = "";
            url = "";
            items = 10;
            error = false;
            number = "%i%";
            show_summary = 0;
            show_author = 0;
            show_date = 0;
        } else {
            number = strval(Array.extractVar(options.getArrayValue(number), "number", number, Array.EXTR_OVERWRITE));
            title = strval(Array.extractVar(options.getArrayValue(number), "title", title, Array.EXTR_OVERWRITE));
            items = intval(Array.extractVar(options.getArrayValue(number), "items", items, Array.EXTR_OVERWRITE));
            error = booleanval(Array.extractVar(options.getArrayValue(number), "error", error, Array.EXTR_OVERWRITE));
            show_summary = intval(Array.extractVar(options.getArrayValue(number), "show_summary", show_summary, Array.EXTR_OVERWRITE));
            show_author = intval(Array.extractVar(options.getArrayValue(number), "show_author", show_author, Array.EXTR_OVERWRITE));
            show_date = intval(Array.extractVar(options.getArrayValue(number), "show_date", show_date, Array.EXTR_OVERWRITE));
            url = strval(Array.extractVar(options.getArrayValue(number), "url", url, Array.EXTR_OVERWRITE));
        }

        wp_widget_rss_form(Array.compact(
                new ArrayEntry("number", number),
                new ArrayEntry("title", title),
                new ArrayEntry("url", url),
                new ArrayEntry("items", items),
                new ArrayEntry("error", error),
                new ArrayEntry("show_summary", show_summary),
                new ArrayEntry("show_author", show_author),
                new ArrayEntry("show_date", show_date)), null);
    }

    public void wp_widget_rss_form(Array<Object> args, Object inputsObj) {
        Array<Object> default_inputs = new Array<Object>();
        String number = null;
        int i = 0;
        String id;
        default_inputs = new Array<Object>(
                new ArrayEntry<Object>("url", true),
                new ArrayEntry<Object>("title", true),
                new ArrayEntry<Object>("items", true),
                new ArrayEntry<Object>("show_summary", true),
                new ArrayEntry<Object>("show_author", true),
                new ArrayEntry<Object>("show_date", true));

        Array inputs = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(inputsObj, default_inputs);
        number = strval(Array.extractVar(args, "number", number, Array.EXTR_OVERWRITE));
        title = strval(Array.extractVar(args, "title", title, Array.EXTR_OVERWRITE));
        url = strval(Array.extractVar(args, "url", url, Array.EXTR_OVERWRITE));
        items = intval(Array.extractVar(args, "items", items, Array.EXTR_OVERWRITE));
        show_summary = intval(Array.extractVar(args, "show_summary", show_summary, Array.EXTR_OVERWRITE));
        show_author = intval(Array.extractVar(args, "show_author", show_author, Array.EXTR_OVERWRITE));
        show_date = intval(Array.extractVar(args, "show_date", show_date, Array.EXTR_OVERWRITE));
        number = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(number);
        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(title);
        url = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(url);

        //		items = intval(items);
        if ((items < 1) || (20 < items)) {
            items = 10;
        }

        //		show_summary = intval(show_summary);
        //		show_author = intval(show_author);
        //		show_date = intval(show_date);
        if (booleanval(inputs.getValue("url"))) {
            echo(gVars.webEnv, "\t<p>\n\t\t<label for=\"rss-url-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Enter the RSS feed URL here:", "default");
            echo(gVars.webEnv, "\t\t\t<input class=\"widefat\" id=\"rss-url-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][url]\" type=\"text\" value=\"");
            echo(gVars.webEnv, url);
            echo(gVars.webEnv, "\" />\n\t\t</label>\n\t</p>\n");
        } else {
        }

        if (booleanval(inputs.getValue("title"))) {
            echo(gVars.webEnv, "\t<p>\n\t\t<label for=\"rss-title-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Give the feed a title (optional):", "default");
            echo(gVars.webEnv, "\t\t\t<input class=\"widefat\" id=\"rss-title-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][title]\" type=\"text\" value=\"");
            echo(gVars.webEnv, title);
            echo(gVars.webEnv, "\" />\n\t\t</label>\n\t</p>\n");
        } else {
        }

        if (booleanval(inputs.getValue("items"))) {
            echo(gVars.webEnv, "\t<p>\n\t\t<label for=\"rss-items-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("How many items would you like to display?", "default");
            echo(gVars.webEnv, "\t\t\t<select id=\"rss-items-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][items]\">\n\t\t\t\t");

            for (i = 1; i <= 20; ++i)
                echo(gVars.webEnv, "<option value=\'" + strval(i) + "\' " + (equal(items, i)
                    ? "selected=\'selected\'"
                    : "") + ">" + strval(i) + "</option>");

            echo(gVars.webEnv, "\t\t\t</select>\n\t\t</label>\n\t</p>\n");
        } else {
        }

        if (booleanval(inputs.getValue("show_summary"))) {
            echo(gVars.webEnv, "\t<p>\n\t\t<label for=\"rss-show-summary-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\">\n\t\t\t<input id=\"rss-show-summary-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][show_summary]\" type=\"checkbox\" value=\"1\" ");

            if (booleanval(show_summary)) {
                echo(gVars.webEnv, "checked=\"checked\"");
            }

            echo(gVars.webEnv, "/>\n\t\t\t");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Display item content?", "default");
            echo(gVars.webEnv, "\t\t</label>\n\t</p>\n");
        } else {
        }

        if (booleanval(inputs.getValue("show_author"))) {
            echo(gVars.webEnv, "\t<p>\n\t\t<label for=\"rss-show-author-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\">\n\t\t\t<input id=\"rss-show-author-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][show_author]\" type=\"checkbox\" value=\"1\" ");

            if (booleanval(show_author)) {
                echo(gVars.webEnv, "checked=\"checked\"");
            }

            echo(gVars.webEnv, "/>\n\t\t\t");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Display item author if available?", "default");
            echo(gVars.webEnv, "\t\t</label>\n\t</p>\n");
        } else {
        }

        if (booleanval(inputs.getValue("show_date"))) {
            echo(gVars.webEnv, "\t<p>\n\t\t<label for=\"rss-show-date-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\">\n\t\t\t<input id=\"rss-show-date-");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][show_date]\" type=\"checkbox\" value=\"1\" ");

            if (booleanval(show_date)) {
                echo(gVars.webEnv, "checked=\"checked\"");
            }

            echo(gVars.webEnv, "/>\n\t\t\t");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Display item date?", "default");
            echo(gVars.webEnv, "\t\t</label>\n\t</p>\n\t<input type=\"hidden\" name=\"widget-rss[");
            echo(gVars.webEnv, number);
            echo(gVars.webEnv, "][submit]\" value=\"1\" />\n");
        } else {
        }

        for (Map.Entry javaEntry671 : Array.array_keys(default_inputs).entrySet()) {
            final String input = strval(javaEntry671.getValue());

            if (strictEqual("hidden", inputs.getValue(input))) {
                id = Strings.str_replace("_", "-", input);
                echo(gVars.webEnv, "\t<input type=\"hidden\" id=\"rss-");
                echo(gVars.webEnv, id);
                echo(gVars.webEnv, "-");
                echo(gVars.webEnv, number);
                echo(gVars.webEnv, "\" name=\"widget-rss[");
                echo(gVars.webEnv, number);
                echo(gVars.webEnv, "][");
                echo(gVars.webEnv, input);
                echo(gVars.webEnv, "]\" value=\"");
                echo(gVars.webEnv,
                    new DynamicConstructEvaluator() {
                        public Object evaluate() {
                            if (equal(input, "url")) {
                                return url;
                            }

                            if (equal(input, "title")) {
                                return title;
                            }

                            if (equal(input, "items")) {
                                return items;
                            }

                            if (equal(input, "show_summary")) {
                                return show_summary;
                            }

                            if (equal(input, "show_author")) {
                                return show_author;
                            }

                            if (equal(input, "show_date")) {
                                return show_date;
                            }

                            return null;
                        }
                    }.evaluate());
                echo(gVars.webEnv, "\" />\n");
            } else {
            }
        }
    }

 // Expects unescaped data
    public Array<Object> wp_widget_rss_process(Array<Object> widget_rss, boolean check_feed) {
        int items = 0;
        String url = null;
        String title = null;
        int show_summary = 0;
        int show_author = 0;
        int show_date = 0;
        Object rss = null;

        /* Do not change type */
        String error = null;
        String link = null;
        Object widget_number = null;
        items = intval(widget_rss.getValue("items"));

        if ((items < 1) || (20 < items)) {
            items = 10;
        }

        url = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_url(Strings.strip_tags(strval(widget_rss.getValue("url"))), null);
        title = Strings.trim(Strings.strip_tags(strval(widget_rss.getValue("title"))));
        show_summary = intval(widget_rss.getValue("show_summary"));
        show_author = intval(widget_rss.getValue("show_author"));
        show_date = intval(widget_rss.getValue("show_date"));

        if (check_feed) {
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, RssPage.class);
            rss = getIncluded(RssPage.class, gVars, gConsts).fetch_rss(url);

            error = strval(false);
            link = "";

            if (!is_object(rss)) {
                url = getIncluded(FormattingPage.class, gVars, gConsts)
                          .wp_specialchars(getIncluded(L10nPage.class, gVars, gConsts).__("Error: could not find an RSS or ATOM feed at that URL.", "default"), strval(1));
                error = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Error in RSS %1$d", "default"), widget_number);
            } else {
                link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.strip_tags(strval(((MagpieRSS) rss).channel.getValue("link"))), null, "display");

                while (!equal(Strings.strstr(link, "http"), link))
                    link = Strings.substr(link, 1);
            }
        }

        return Array.compact(
            new ArrayEntry("title", title),
            new ArrayEntry("url", url),
            new ArrayEntry("link", link),
            new ArrayEntry("items", items),
            new ArrayEntry("error", error),
            new ArrayEntry("show_summary", show_summary),
            new ArrayEntry("show_author", show_author),
            new ArrayEntry("show_date", show_date));
    }

    public void wp_widget_rss_register() {
        Array<Object> options = new Array<Object>();
        Object optionsObj;
        Array<Object> widget_ops = new Array<Object>();
        Array<Object> control_ops = new Array<Object>();
        Object name = null;
        String id = null;
        Object o = null;

        if (!booleanval(optionsObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_rss"))) {
            options = new Array<Object>();
        } else {
            options = (Array<Object>) optionsObj;
        }

        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_rss"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("Entries from any RSS or Atom feed", "default")));
        control_ops = new Array<Object>(new ArrayEntry<Object>("width", 400), new ArrayEntry<Object>("height", 200), new ArrayEntry<Object>("id_base", "rss"));
        name = getIncluded(L10nPage.class, gVars, gConsts).__("RSS", "default");
        id = strval(false);

        for (Map.Entry javaEntry672 : Array.array_keys(options).entrySet()) {
            o = javaEntry672.getValue();

    		// Old widgets can have null values for some reason
            if (!isset(options.getArrayValue(o).getValue("url")) || !isset(options.getArrayValue(o).getValue("title")) || !isset(options.getArrayValue(o).getValue("items"))) {
                continue;
            }

            id = "rss-" + strval(o); // Never never never translate an id
            wp_register_sidebar_widget(id, name, Callback.createCallbackArray(this, "wp_widget_rss"), widget_ops, new Array<Object>(new ArrayEntry<Object>("number", o)));
            wp_register_widget_control(id, name, Callback.createCallbackArray(this, "wp_widget_rss_control"), control_ops, new Array<Object>(new ArrayEntry<Object>("number", o)));
        }

    	// If there are none, we register the widget's existance with a generic template
        if (!booleanval(id)) {
            wp_register_sidebar_widget("rss-1", name, Callback.createCallbackArray(this, "wp_widget_rss"), widget_ops, new Array<Object>(new ArrayEntry<Object>("number", -1)));
            wp_register_widget_control("rss-1", name, Callback.createCallbackArray(this, "wp_widget_rss_control"), control_ops, new Array<Object>(new ArrayEntry<Object>("number", -1)));
        }
    }

    public void wp_widget_tag_cloud(Array<Object> args) {
        Array<Object> options = new Array<Object>();
        Object title = null;
        Object before_widget = null;
        Object before_title = null;
        Object after_title = null;
        Object after_widget = null;
        before_widget = Array.extractVar(args, "before_widget", before_widget, Array.EXTR_OVERWRITE);
        before_title = Array.extractVar(args, "before_title", before_title, Array.EXTR_OVERWRITE);
        after_title = Array.extractVar(args, "after_title", after_title, Array.EXTR_OVERWRITE);
        after_widget = Array.extractVar(args, "after_widget", after_widget, Array.EXTR_OVERWRITE);
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_tag_cloud");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        title = (empty(options.getValue("title"))
            ? getIncluded(L10nPage.class, gVars, gConsts).__("Tags", "default")
            : options.getValue("title"));
        echo(gVars.webEnv, before_widget);
        echo(gVars.webEnv, strval(before_title) + strval(title) + strval(after_title));
        getIncluded(Category_templatePage.class, gVars, gConsts).wp_tag_cloud("");
        echo(gVars.webEnv, after_widget);
    }

    public void wp_widget_tag_cloud_control() {
        Array<Object> options = new Array<Object>();
        Array<Object> newoptions = new Array<Object>();
        Object title = null;
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("widget_tag_cloud");

        if (is_null(options)) {
            options = new Array<Object>();
        }

        newoptions = Array.arrayCopy(options);

        if (booleanval(gVars.webEnv._POST.getValue("tag-cloud-submit"))) {
            newoptions.putValue("title", Strings.strip_tags(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("tag-cloud-title")))));
        }

        if (!equal(options, newoptions)) {
            options = Array.arrayCopy(newoptions);
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("widget_tag_cloud", options);
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(options.getValue("title")));
        echo(gVars.webEnv, "\t<p><label for=\"tag-cloud-title\">\n\t");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
        echo(gVars.webEnv, " <input type=\"text\" class=\"widefat\" id=\"tag-cloud-title\" name=\"tag-cloud-title\" value=\"");
        echo(gVars.webEnv, title);
        echo(gVars.webEnv, "\" /></label>\n\t</p>\n\t<input type=\"hidden\" name=\"tag-cloud-submit\" id=\"tag-cloud-submit\" value=\"1\" />\n");
    }

    public void wp_widgets_init() {
        Array<Object> widget_ops = new Array<Object>();

        if (!getIncluded(FunctionsPage.class, gVars, gConsts).is_blog_installed()) {
            return;
        }

        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_pages"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("Your blog\'s nWordPress Pages", "default")));
        wp_register_sidebar_widget("pages", getIncluded(L10nPage.class, gVars, gConsts).__("Pages", "default"), Callback.createCallbackArray(this, "wp_widget_pages"), widget_ops);
        wp_register_widget_control("pages", getIncluded(L10nPage.class, gVars, gConsts).__("Pages", "default"), Callback.createCallbackArray(this, "wp_widget_pages_control"), new Array<Object>());
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_calendar"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("A calendar of your blog\'s posts", "default")));
        wp_register_sidebar_widget("calendar", getIncluded(L10nPage.class, gVars, gConsts).__("Calendar", "default"), Callback.createCallbackArray(this, "wp_widget_calendar"), widget_ops);
        wp_register_widget_control(
            "calendar",
            getIncluded(L10nPage.class, gVars, gConsts).__("Calendar", "default"),
            Callback.createCallbackArray(this, "wp_widget_calendar_control"),
            new Array<Object>());
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_archive"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("A monthly archive of your blog\'s posts", "default")));
        wp_register_sidebar_widget("archives", getIncluded(L10nPage.class, gVars, gConsts).__("Archives", "default"), Callback.createCallbackArray(this, "wp_widget_archives"), widget_ops);
        wp_register_widget_control(
            "archives",
            getIncluded(L10nPage.class, gVars, gConsts).__("Archives", "default"),
            Callback.createCallbackArray(this, "wp_widget_archives_control"),
            new Array<Object>());
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_links"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("Your blogroll", "default")));
        wp_register_sidebar_widget("links", getIncluded(L10nPage.class, gVars, gConsts).__("Links", "default"), Callback.createCallbackArray(this, "wp_widget_links"), widget_ops);
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_meta"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("Log in/out, admin, feed and WordPress/nWordPress links", "default")));
        wp_register_sidebar_widget("meta", getIncluded(L10nPage.class, gVars, gConsts).__("Meta", "default"), Callback.createCallbackArray(this, "wp_widget_meta"), widget_ops);
        wp_register_widget_control("meta", getIncluded(L10nPage.class, gVars, gConsts).__("Meta", "default"), Callback.createCallbackArray(this, "wp_widget_meta_control"), new Array<Object>());
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_search"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("A search form for your blog", "default")));
        wp_register_sidebar_widget("search", getIncluded(L10nPage.class, gVars, gConsts).__("Search", "default"), Callback.createCallbackArray(this, "wp_widget_search"), widget_ops);
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_recent_entries"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("The most recent posts on your blog", "default")));
        wp_register_sidebar_widget("recent-posts", getIncluded(L10nPage.class, gVars, gConsts).__("Recent Posts", "default"), Callback.createCallbackArray(this, "wp_widget_recent_entries"), widget_ops);
        wp_register_widget_control(
            "recent-posts",
            getIncluded(L10nPage.class, gVars, gConsts).__("Recent Posts", "default"),
            Callback.createCallbackArray(this, "wp_widget_recent_entries_control"),
            new Array<Object>());
        widget_ops = new Array<Object>(
                new ArrayEntry<Object>("classname", "widget_tag_cloud"),
                new ArrayEntry<Object>("description", getIncluded(L10nPage.class, gVars, gConsts).__("Your most used tags in cloud format", "default")));
        wp_register_sidebar_widget("tag_cloud", getIncluded(L10nPage.class, gVars, gConsts).__("Tag Cloud", "default"), Callback.createCallbackArray(this, "wp_widget_tag_cloud"), widget_ops);
        wp_register_widget_control(
            "tag_cloud",
            getIncluded(L10nPage.class, gVars, gConsts).__("Tag Cloud", "default"),
            Callback.createCallbackArray(this, "wp_widget_tag_cloud_control"),
            new Array<Object>());
        wp_widget_categories_register();
        wp_widget_text_register();
        wp_widget_rss_register();
        wp_widget_recent_comments_register();
        getIncluded(PluginPage.class, gVars, gConsts).do_action("widgets_init", "");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_widgets_block1");
        gVars.webEnv = webEnv;
        gVars.wp_registered_sidebars = new Array<Object>();
        gVars.wp_registered_widgets = new Array<Object>();
        gVars.wp_registered_widget_controls = new Array<Object>();
        getIncluded(PluginPage.class, gVars, gConsts).add_action("save_post", Callback.createCallbackArray(this, "wp_flush_widget_recent_entries"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("deleted_post", Callback.createCallbackArray(this, "wp_flush_widget_recent_entries"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("switch_theme", Callback.createCallbackArray(this, "wp_flush_widget_recent_entries"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("comment_post", Callback.createCallbackArray(this, "wp_delete_recent_comments_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_set_comment_status", Callback.createCallbackArray(this, "wp_delete_recent_comments_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("init", Callback.createCallbackArray(this, "wp_widgets_init"), 1, 1);

        return DEFAULT_VAL;
    }
    
/* Pattern for multi-widget (allows multiple instances such as the text widget).

 // Displays widget on blag
 // $widget_args: number
//     number: which of the several widgets of this type do we mean
 function widget_many( $args, $widget_args = 1 ) {
 	extract( $args, EXTR_SKIP );
 	if ( is_numeric($widget_args) )
 		$widget_args = array( 'number' => $widget_args );
 	$widget_args = wp_parse_args( $widget_args, array( 'number' => -1 ) );
 	extract( $widget_args, EXTR_SKIP );

 	// Data should be stored as array:  array( number => data for that instance of the widget, ... )
 	$options = get_option('widget_many');
 	if ( !isset($options[$number]) )
 		return;

 	echo $before_widget;

 	// Do stuff for this widget, drawing data from $options[$number]

 	echo $after_widget;
 }

 // Displays form for a particular instance of the widget.  Also updates the data after a POST submit
 // $widget_args: number
//     number: which of the several widgets of this type do we mean
 function widget_many_control( $widget_args = 1 ) {
 	global $wp_registered_widgets;
 	static $updated = false; // Whether or not we have already updated the data after a POST submit

 	if ( is_numeric($widget_args) )
 		$widget_args = array( 'number' => $widget_args );
 	$widget_args = wp_parse_args( $widget_args, array( 'number' => -1 ) );
 	extract( $widget_args, EXTR_SKIP );

 	// Data should be stored as array:  array( number => data for that instance of the widget, ... )
 	$options = get_option('widget_many');
 	if ( !is_array($options) )
 		$options = array();

 	// We need to update the data
 	if ( !$updated && !empty($_POST['sidebar']) ) {
 		// Tells us what sidebar to put the data in
 		$sidebar = (string) $_POST['sidebar'];

 		$sidebars_widgets = wp_get_sidebars_widgets();
 		if ( isset($sidebars_widgets[$sidebar]) )
 			$this_sidebar =& $sidebars_widgets[$sidebar];
 		else
 			$this_sidebar = array();

 		foreach ( $this_sidebar as $_widget_id ) {
 			// Remove all widgets of this type from the sidebar.  We'll add the new data in a second.  This makes sure we don't get any duplicate data
 			// since widget ids aren't necessarily persistent across multiple updates
 			if ( 'widget_many' == $wp_registered_widgets[$_widget_id]['callback'] && isset($wp_registered_widgets[$_widget_id]['params'][0]['number']) ) {
 				$widget_number = $wp_registered_widgets[$_widget_id]['params'][0]['number'];
 				if ( !in_array( "many-$widget_number", $_POST['widget-id'] ) ) // the widget has been removed. "many-$widget_number" is "{id_base}-{widget_number}
 					unset($options[$widget_number]);
 			}
 		}

 		foreach ( (array) $_POST['widget-many'] as $widget_number => $widget_many_instance ) {
 			// compile data from $widget_many_instance
 			if ( !isset($widget_many_instance['something']) && isset($options[$widget_number]) ) // user clicked cancel
 				continue;
 			$something = wp_specialchars( $widget_many_instance['something'] );
 			$options[$widget_number] = array( 'something' => $something );  // Even simple widgets should store stuff in array, rather than in scalar
 		}

 		update_option('widget_text', $options);

 		$updated = true; // So that we don't go through this more than once
 	}


 	// Here we echo out the form
 	if ( -1 == $number ) { // We echo out a template for a form which can be converted to a specific form later via JS
 		$something = '';
 		$number = '%i%';
 	} else {
 		$something = attribute_escape($options[$number]['something']);
 	}

 	// The form has inputs with names like widget-many[$number][something] so that all data for that instance of
 	// the widget are stored in one $_POST variable: $_POST['widget-many'][$number]
 ?>
 		<p>
 			<input class="widefat" id="widget-many-something-<?php echo $number; ?>" name="widget-many[<?php echo $number; ?>][something]" type="text" value="<?php echo $data; ?>" />
 			<input type="hidden" id="widget-many-submit-<?php echo $number; ?>" name="widget-many[<?php echo $number; ?>][submit]" value="1" />
 		</p>
 <?php
 }

 // Registers each instance of our widget on startup
 function widget_many_register() {
 	if ( !$options = get_option('widget_many') )
 		$options = array();

 	$widget_ops = array('classname' => 'widget_many', 'description' => __('Widget which allows multiple instances'));
 	$control_ops = array('width' => 400, 'height' => 350, 'id_base' => 'many');
 	$name = __('Many');

 	$registered = false;
 	foreach ( array_keys($options) as $o ) {
 		// Old widgets can have null values for some reason
 		if ( !isset($options[$o]['something']) ) // we used 'something' above in our exampple.  Replace with with whatever your real data are.
 			continue;

 		// $id should look like {$id_base}-{$o}
 		$id = "many-$o"; // Never never never translate an id
 		$registered = true;
 		wp_register_sidebar_widget( $id, $name, 'wp_widget_text', $widget_ops, array( 'number' => $o ) );
 		wp_register_widget_control( $id, $name, 'wp_widget_text_control', $control_ops, array( 'number' => $o ) );
 	}

 	// If there are none, we register the widget's existance with a generic template
 	if ( !$registered ) {
 		wp_register_sidebar_widget( 'many-1', $name, 'widget_many', $widget_ops, array( 'number' => -1 ) );
 		wp_register_widget_control( 'many-1', $name, 'widget_many_control', $control_ops, array( 'number' => -1 ) );
 	}
 }

 // This is important
 add_action( 'widgets_init', 'widget_many_register' )

 */
}
