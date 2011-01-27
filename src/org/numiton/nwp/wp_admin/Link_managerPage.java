/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Link_managerPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.BookmarkPage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Link_managerPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Link_managerPage.class.getName());
    public Object sqlorderby;
    public Object select_cat;
    public Object select_order;
    public Array<Object> link_columns;
    public Array<Object> links;
    public String short_url;
    public Object visible;
    public Array<String> cat_names = new Array<String>();

    @Override
    @RequestMapping("/wp-admin/link-manager.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/link_manager";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);

        // Handle bulk deletes
        if (isset(gVars.webEnv._GET.getValue("deleteit")) && isset(gVars.webEnv._GET.getValue("linkcheck"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("bulk-bookmarks", "_wpnonce");

            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit the links for this blog.", "default"), "");
            }

            for (Map.Entry javaEntry270 : new Array<Object>(gVars.webEnv._GET.getValue("linkcheck")).entrySet()) {
                gVars.link_id = intval(javaEntry270.getValue());

                //				gVars.link_id = intval(gVars.link_id);
                getIncluded(BookmarkPage.class, gVars, gConsts).wp_delete_link(gVars.link_id);
            }

            gVars.sendback = getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer();
            gVars.sendback = QRegExPerl.preg_replace("|[^a-z0-9-~+_.?#=&;,/:]|i", "", gVars.sendback);
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.sendback, 302);
            System.exit();
        } else if (!empty(gVars.webEnv._GET.getValue("_wp_http_referer"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(
                        new ArrayEntry<Object>("_wp_http_referer"),
                        new ArrayEntry<Object>("_wpnonce")), Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI())), 302);
            System.exit();
        }

        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("admin-forms", false, new Array<Object>(), false);
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this,
            new Array<Object>(new ArrayEntry<Object>("action"),
                new ArrayEntry<Object>("cat_id"),
                new ArrayEntry<Object>("linkurl"),
                new ArrayEntry<Object>("name"),
                new ArrayEntry<Object>("image"),
                new ArrayEntry<Object>("description"),
                new ArrayEntry<Object>("visible"),
                new ArrayEntry<Object>("target"),
                new ArrayEntry<Object>("category"),
                new ArrayEntry<Object>("link_id"),
                new ArrayEntry<Object>("submit"),
                new ArrayEntry<Object>("order_by"),
                new ArrayEntry<Object>("links_show_cat_id"),
                new ArrayEntry<Object>("rating"),
                new ArrayEntry<Object>("rel"),
                new ArrayEntry<Object>("notes"),
                new ArrayEntry<Object>("linkcheck[]")));

        if (empty(gVars.cat_id)) {
            gVars.cat_id = "all";
        }

        if (empty(gVars.order_by)) {
            gVars.order_by = "order_name";
        }

        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Manage Links", "default");
        gVars.this_file = gVars.parent_file = "edit.php";
        includeOnce(gVars, gConsts, Admin_headerPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
            getIncluded(FunctionsPage.class, gVars, gConsts)
                .wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You do not have sufficient permissions to edit the links for this blog.", "default"), "");
        }

        {
            int javaSwitchSelector19 = 0;

            if (equal(gVars.order_by, "order_id")) {
                javaSwitchSelector19 = 1;
            }

            if (equal(gVars.order_by, "order_url")) {
                javaSwitchSelector19 = 2;
            }

            if (equal(gVars.order_by, "order_desc")) {
                javaSwitchSelector19 = 3;
            }

            if (equal(gVars.order_by, "order_owner")) {
                javaSwitchSelector19 = 4;
            }

            if (equal(gVars.order_by, "order_rating")) {
                javaSwitchSelector19 = 5;
            }

            if (equal(gVars.order_by, "order_name")) {
                javaSwitchSelector19 = 6;
            }

            switch (javaSwitchSelector19) {
            case 1: {
                sqlorderby = "id";

                break;
            }

            case 2: {
                sqlorderby = "url";

                break;
            }

            case 3: {
                sqlorderby = "description";

                break;
            }

            case 4: {
                sqlorderby = "owner";

                break;
            }

            case 5: {
                sqlorderby = "rating";

                break;
            }

            case 6: {
            }

            default: {
                sqlorderby = "name";

                break;
            }
            }
        }

        if (isset(gVars.webEnv._GET.getValue("deleted"))) {
            echo(gVars.webEnv, "<div style=\"background-color: rgb(207, 235, 247);\" id=\"message\" class=\"updated fade\"><p>");
            gVars.deleted = intval(gVars.webEnv._GET.getValue("deleted"));
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s link deleted.", "%s links deleted", gVars.deleted, "default"), gVars.deleted);
            echo(gVars.webEnv, "</p></div>");
            gVars.webEnv._SERVER.putValue(
                "REQUEST_URI",
                getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(new ArrayEntry<Object>("deleted")), gVars.webEnv.getRequestURI()));
        }

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block2");
        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Manage Links (<a href=\"%s\">add new</a>)", "default"), "link-add.php");

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block3");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("s")))));

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Search Links", "default");

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Delete", "default");

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block6");
        gVars.categories = (Array<StdClass>) getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms("link_category", "hide_empty=1");
        select_cat = "<select name=\"cat_id\">\n";
        select_cat = strval(select_cat) + "<option value=\"all\"" + (equal(gVars.cat_id, "all")
            ? " selected=\'selected\'"
            : "") + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("View all Categories", "default") + "</option>\n";

        for (Map.Entry javaEntry271 : new Array<Object>(gVars.categories).entrySet()) {
            gVars.cat = javaEntry271.getValue();
            select_cat = strval(select_cat) + "<option value=\"" + ((StdClass) gVars.cat).fields.getValue("term_id") + "\"" +
                (equal(((StdClass) gVars.cat).fields.getValue("term_id"), gVars.cat_id)
                ? " selected=\'selected\'"
                : "") + ">" +
                getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field(
                    "name",
                    ((StdClass) gVars.cat).fields.getValue("name"),
                    intval(((StdClass) gVars.cat).fields.getValue("term_id")),
                    "link_category",
                    "display") + "</option>\n";
        }

        select_cat = strval(select_cat) + "</select>\n";
        select_order = "<select name=\"order_by\">\n";
        select_order = select_order + "<option value=\"order_id\"" + (equal(gVars.order_by, "order_id")
            ? " selected=\'selected\'"
            : "") + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("Order by Link ID", "default") + "</option>\n";
        select_order = select_order + "<option value=\"order_name\"" + (equal(gVars.order_by, "order_name")
            ? " selected=\'selected\'"
            : "") + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("Order by Name", "default") + "</option>\n";
        select_order = select_order + "<option value=\"order_url\"" + (equal(gVars.order_by, "order_url")
            ? " selected=\'selected\'"
            : "") + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("Order by Address", "default") + "</option>\n";
        select_order = select_order + "<option value=\"order_rating\"" + (equal(gVars.order_by, "order_rating")
            ? " selected=\'selected\'"
            : "") + ">" + getIncluded(L10nPage.class, gVars, gConsts).__("Order by Rating", "default") + "</option>\n";
        select_order = select_order + "</select>\n";
        echo(gVars.webEnv, select_cat);
        echo(gVars.webEnv, select_order);

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Filter", "default");

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block8");
        link_columns = new Array<Object>(
                new ArrayEntry<Object>("name", "<th style=\"width: 15%;\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Name", "default") + "</th>"),
                new ArrayEntry<Object>("url", "<th>" + getIncluded(L10nPage.class, gVars, gConsts).__("URL", "default") + "</th>"),
                new ArrayEntry<Object>("categories", "<th>" + getIncluded(L10nPage.class, gVars, gConsts).__("Categories", "default") + "</th>"),
                new ArrayEntry<Object>("rel", "<th style=\"text-align: center\">" + getIncluded(L10nPage.class, gVars, gConsts).__("rel", "default") + "</th>"),
                new ArrayEntry<Object>("visible", "<th style=\"text-align: center\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Visible", "default") + "</th>"));
        link_columns = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("manage_link_columns", link_columns);

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block9");

        if (equal("all", gVars.cat_id)) {
            gVars.cat_id = "";
        }

        gVars.args = new Array<Object>(
                new ArrayEntry<Object>("category", gVars.cat_id),
                new ArrayEntry<Object>("hide_invisible", 0),
                new ArrayEntry<Object>("orderby", sqlorderby),
                new ArrayEntry<Object>("hide_empty", 0));

        if (!empty(gVars.webEnv._GET.getValue("s"))) {
            gVars.args.putValue("search", gVars.webEnv._GET.getValue("s"));
        }

        links = (((org.numiton.nwp.wp_includes.BookmarkPage) getIncluded(org.numiton.nwp.wp_includes.BookmarkPage.class, gVars, gConsts))).get_bookmarks(gVars.args);

        if (booleanval(links)) {
            echo(gVars.webEnv, "\n");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("bulk-bookmarks", "_wpnonce", true, true);
            echo(
                gVars.webEnv,
                "<table class=\"widefat\">\n\t<thead>\n\t<tr>\n\t<th scope=\"col\" class=\"check-column\"><input type=\"checkbox\" onclick=\"checkAll(document.getElementById(\'posts-filter\'));\" /></th>\n");

            for (Map.Entry javaEntry272 : link_columns.entrySet()) {
                gVars.column_display_name = javaEntry272.getValue();
                echo(gVars.webEnv, gVars.column_display_name);
            }

            echo(gVars.webEnv, "\t</tr>\n\t</thead>\n\t<tbody>\n");

            for (Map.Entry javaEntry273 : links.entrySet()) {
                gVars.link = (StdClass) javaEntry273.getValue();
                gVars.link = (StdClass) (((org.numiton.nwp.wp_includes.BookmarkPage) getIncluded(org.numiton.nwp.wp_includes.BookmarkPage.class, gVars, gConsts))).sanitize_bookmark(gVars.link, "display");
                gVars.link.fields.putValue("link_name", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.link, "link_name"))));
                gVars.link.fields.putValue("link_category", getIncluded(BookmarkPage.class, gVars, gConsts).wp_get_link_cats(intval(StdClass.getValue(gVars.link, "link_id"))));
                short_url = Strings.str_replace("http://", "", strval(StdClass.getValue(gVars.link, "link_url")));
                short_url = Strings.str_replace("www.", "", short_url);

                if (equal("/", Strings.substr(short_url, -1))) {
                    short_url = Strings.substr(short_url, 0, -1);
                }

                if (Strings.strlen(short_url) > 35) {
                    short_url = Strings.substr(short_url, 0, 32) + "...";
                }

                visible = (equal(StdClass.getValue(gVars.link, "link_visible"), "Y")
                    ? getIncluded(L10nPage.class, gVars, gConsts).__("Yes", "default")
                    : getIncluded(L10nPage.class, gVars, gConsts).__("No", "default"));
                ++gVars.i;
                gVars.style = (booleanval(gVars.i % 2)
                    ? ""
                    : " class=\"alternate\"");
                echo(gVars.webEnv, "<tr id=\"link-");
                echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_id"));
                echo(gVars.webEnv, "\" valign=\"middle\" ");
                echo(gVars.webEnv, gVars.style);
                echo(gVars.webEnv, ">");
                echo(gVars.webEnv, "<th scope=\"row\" class=\"check-column\"><input type=\"checkbox\" name=\"linkcheck[]\" value=\"" + StdClass.getValue(gVars.link, "link_id") + "\" /></th>");

                for (Map.Entry javaEntry274 : link_columns.entrySet()) {
                    gVars.column_name = javaEntry274.getKey();
                    gVars.column_display_name = javaEntry274.getValue();

                    {
                        int javaSwitchSelector20 = 0;

                        if (equal(gVars.column_name, "name")) {
                            javaSwitchSelector20 = 1;
                        }

                        if (equal(gVars.column_name, "url")) {
                            javaSwitchSelector20 = 2;
                        }

                        if (equal(gVars.column_name, "categories")) {
                            javaSwitchSelector20 = 3;
                        }

                        if (equal(gVars.column_name, "rel")) {
                            javaSwitchSelector20 = 4;
                        }

                        if (equal(gVars.column_name, "visible")) {
                            javaSwitchSelector20 = 5;
                        }

                        switch (javaSwitchSelector20) {
                        case 1: {
                            echo(
                                    gVars.webEnv,
                                    "<td><strong><a class=\'row-title\' href=\'link.php?link_id=" + StdClass.getValue(gVars.link, "link_id") + "&amp;action=edit\' title=\'" +
                                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                                        QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Edit \"%s\"", "default"), StdClass.getValue(gVars.link, "link_name"))) + "\' class=\'edit\'>" +
                                    StdClass.getValue(gVars.link, "link_name") + "</a></strong><br />");
                            echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_description") + "</td>");

                            break;
                        }

                        case 2: {
                            echo(gVars.webEnv,
                                "<td><a href=\'" + StdClass.getValue(gVars.link, "link_url") + "\' title=\'" +
                                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Visit %s", "default"), StdClass.getValue(gVars.link, "link_name")) + "\'>" + short_url + "</a></td>");

                            break;
                        }

                        case 3: {
                            echo(gVars.webEnv, "<td>");
                            cat_names = new Array<String>();

                            for (Map.Entry javaEntry275 : (Set<Map.Entry>) gVars.link.fields.getArrayValue("link_category").entrySet()) {
                                Object category = javaEntry275.getValue();
                                gVars.cat = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(category, "link_category", gConsts.getOBJECT(), "display");

                                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.cat)) {
                                    echo(gVars.webEnv, ((WP_Error) gVars.cat).get_error_message());
                                }

                                gVars.cat_name = strval(((StdClass) gVars.cat).fields.getValue("name"));

                                if (!equal(gVars.cat_id, category)) {
                                    gVars.cat_name = "<a href=\'link-manager.php?cat_id=" + strval(category) + "\'>" + gVars.cat_name + "</a>";
                                }

                                cat_names.putValue(gVars.cat_name);
                            }

                            echo(gVars.webEnv, Strings.implode(", ", cat_names));
                            echo(gVars.webEnv, " </td>");

                            break;
                        }

                        case 4: {
                            echo(gVars.webEnv, "<td>");
                            echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_rel"));
                            echo(gVars.webEnv, "</td>");

                            break;
                        }

                        case 5: {
                            echo(gVars.webEnv, "<td style=\'text-align: center;\'>");
                            echo(gVars.webEnv, visible);
                            echo(gVars.webEnv, "</td>");

                            break;
                        }

                        default: {
                            echo(gVars.webEnv, "\t\t\t\t\t<td>");
                            getIncluded(PluginPage.class, gVars, gConsts).do_action("manage_link_custom_column", gVars.column_name, StdClass.getValue(gVars.link, "link_id"));
                            echo(gVars.webEnv, "</td>\n\t\t\t\t\t");

                            break;
                        }
                        }
                    }
                }

                echo(gVars.webEnv, "\n    </tr>\n");
            }

            echo(gVars.webEnv, "\t</tbody>\n</table>\n\n");
        } else {
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("No links found.", "default");
            echo(gVars.webEnv, "</p>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_link_manager_block10");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
