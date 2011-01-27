/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: General_templatePage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import org.numiton.nwp.wp_content.themes._default.FooterPage;
import org.numiton.nwp.wp_content.themes._default.HeaderPage;
import org.numiton.nwp.wp_content.themes._default.SidebarPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class General_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(General_templatePage.class.getName());
    public Object previousweekday;
    public Boolean wp_rich_edit_exists;
    public Object wp_rich_edit;

    @Override
    @RequestMapping("/wp-includes/general-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/general_template";
    }

    /**
     * Note: these tags go anywhere in the template
     */
    public void get_header() {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("get_header", "");

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/header.php")) {
            // TODO Add support for multiple themes
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getTEMPLATEPATH() + "/header.php", HeaderPage.class);
        } else {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + "wp-content/themes/default/header.php", HeaderPage.class);
        }
    }

    public void get_footer() {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("get_footer", "");

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/footer.php")) {
            // TODO Add support for multiple themes
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getTEMPLATEPATH() + "/footer.php", FooterPage.class);
        } else {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + "wp-content/themes/default/footer.php", FooterPage.class);
        }
    }

    public void get_sidebar() {
        get_sidebar(null);
    }

    public void get_sidebar(String name) {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("get_sidebar", "");

        if (isset(name) && FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/sidebar-" + name + ".php")) {
            // TODO Add support for multiple themes
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getTEMPLATEPATH() + "/sidebar-" + name + ".php", null);
        } else if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/sidebar.php")) {
            // TODO Add support for multiple themes
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getTEMPLATEPATH() + "/sidebar.php", SidebarPage.class);
        } else {
            getIncluded(ThemePage.class, gVars, gConsts).load_template(gConsts.getABSPATH() + "wp-content/themes/default/sidebar.php", SidebarPage.class);
        }
    }

    public void wp_loginout() {
        String link = null;

        if (!getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
            link = "<a href=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Log in", "default") +
                "</a>";
        } else {
            link = "<a href=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php?action=logout\">" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Log out", "default") + "</a>";
        }

        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("loginout", link));
    }

    public void wp_register() {
        wp_register("<li>", "</li>");
    }

    public void wp_register(String before, String after) {
        String link = null;

        if (!getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("users_can_register"))) {
                link = before + "<a href=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-login.php?action=register\">" +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Register", "default") + "</a>" + after;
            } else {
                link = "";
            }
        } else {
            link = before + "<a href=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/\">" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Site Admin", "default") + "</a>" + after;
        }

        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("register", link));
    }

    public void wp_meta() {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_meta", "");
    }

    public String bloginfo(String show) {
        echo(gVars.webEnv, get_bloginfo(show, "display"));

        return "";
    }

    /**
     * Note: some of these values are DEPRECATED. Meaning they could be taken
     * out at any time and shouldn't be relied upon. Options without "/
     * DEPRECATED" are the preferred and recommended ways to get the
     * information.
     */
    public String get_bloginfo(String show, String filter) {
        String output = null;
        boolean url = false; {
            int javaSwitchSelector66 = 0;

            if (equal(show, "url")) {
                javaSwitchSelector66 = 1;
            }

            if (equal(show, "home")) {
                javaSwitchSelector66 = 2;
            }

            if (equal(show, "siteurl")) {
                javaSwitchSelector66 = 3;
            }

            if (equal(show, "wpurl")) {
                javaSwitchSelector66 = 4;
            }

            if (equal(show, "description")) {
                javaSwitchSelector66 = 5;
            }

            if (equal(show, "rdf_url")) {
                javaSwitchSelector66 = 6;
            }

            if (equal(show, "rss_url")) {
                javaSwitchSelector66 = 7;
            }

            if (equal(show, "rss2_url")) {
                javaSwitchSelector66 = 8;
            }

            if (equal(show, "atom_url")) {
                javaSwitchSelector66 = 9;
            }

            if (equal(show, "comments_atom_url")) {
                javaSwitchSelector66 = 10;
            }

            if (equal(show, "comments_rss2_url")) {
                javaSwitchSelector66 = 11;
            }

            if (equal(show, "pingback_url")) {
                javaSwitchSelector66 = 12;
            }

            if (equal(show, "stylesheet_url")) {
                javaSwitchSelector66 = 13;
            }

            if (equal(show, "stylesheet_directory")) {
                javaSwitchSelector66 = 14;
            }

            if (equal(show, "template_directory")) {
                javaSwitchSelector66 = 15;
            }

            if (equal(show, "template_url")) {
                javaSwitchSelector66 = 16;
            }

            if (equal(show, "admin_email")) {
                javaSwitchSelector66 = 17;
            }

            if (equal(show, "charset")) {
                javaSwitchSelector66 = 18;
            }

            if (equal(show, "html_type")) {
                javaSwitchSelector66 = 19;
            }

            if (equal(show, "version")) {
                javaSwitchSelector66 = 20;
            }

            if (equal(show, "language")) {
                javaSwitchSelector66 = 21;
            }

            if (equal(show, "text_direction")) {
                javaSwitchSelector66 = 22;
            }

            if (equal(show, "name")) {
                javaSwitchSelector66 = 23;
            }

            switch (javaSwitchSelector66) {
            case 1: { // DEPRECATED
            }

            case 2: { // DEPRECATED
            }

            case 3: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));

                break;
            }

            case 4: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));

                break;
            }

            case 5: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogdescription"));

                break;
            }

            case 6: {
                output = strval(getIncluded(Link_templatePage.class, gVars, gConsts).get_feed_link("rdf"));

                break;
            }

            case 7: {
                output = strval(getIncluded(Link_templatePage.class, gVars, gConsts).get_feed_link("rss"));

                break;
            }

            case 8: {
                output = strval(getIncluded(Link_templatePage.class, gVars, gConsts).get_feed_link("rss2"));

                break;
            }

            case 9: {
                output = strval(getIncluded(Link_templatePage.class, gVars, gConsts).get_feed_link("atom"));

                break;
            }

            case 10: {
                output = strval(getIncluded(Link_templatePage.class, gVars, gConsts).get_feed_link("comments_atom"));

                break;
            }

            case 11: {
                output = strval(getIncluded(Link_templatePage.class, gVars, gConsts).get_feed_link("comments_rss2"));

                break;
            }

            case 12: {
                output = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/xmlrpc.php";

                break;
            }

            case 13: {
                output = getIncluded(ThemePage.class, gVars, gConsts).get_stylesheet_uri();

                break;
            }

            case 14: {
                output = getIncluded(ThemePage.class, gVars, gConsts).get_stylesheet_directory_uri();

                break;
            }

            case 15: {
            }

            case 16: {
                output = getIncluded(ThemePage.class, gVars, gConsts).get_template_directory_uri();

                break;
            }

            case 17: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("admin_email"));

                break;
            }

            case 18: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

                if (equal("", output)) {
                    output = "UTF-8";
                }

                break;
            }

            case 19: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type"));

                break;
            }

            case 20: {
                output = gVars.wp_version;

                break;
            }

            case 21: {
                output = getIncluded(L10nPage.class, gVars, gConsts).get_locale();
                output = Strings.str_replace("_", "-", output);

                break;
            }

            case 22: {
                // Modified by Numiton
                output = (gVars.wp_locale != null)
                    ? gVars.wp_locale.text_direction
                    : "";

                break;
            }

            case 23: {
            }

            default: {
                output = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname"));

                break;
            }
            }
        }

        url = true;

        if (strictEqual(Strings.strpos(show, "url"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(show, "directory"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(show, "home"), BOOLEAN_FALSE)) {
            url = false;
        }

        if (equal("display", filter)) {
            if (url) {
                output = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("bloginfo_url", output, show));
            } else {
                output = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("bloginfo", output, show));
            }
        }

        return output;
    }

    public Object wp_title() {
        return wp_title("&raquo;", true, "");
    }

    public Object wp_title(String sep, boolean display, String seplocation) {
        String cat = null;
        Object tag = null;
        String category_name;
        int author;
        String author_name = null;
        String m = null;
        String year = null;
        String monthnum = null;
        String day = null;
        String title = null;
        String my_year = null;
        String my_month = null;
        int my_day = 0;
        StdClass post = null;
        String prefix = null;
        
        cat = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("cat"));
        tag = getIncluded(QueryPage.class, gVars, gConsts).get_query_var("tag_id");
        category_name = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("category_name"));
        author = intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("author"));
        author_name = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("author_name"));
        m = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("m"));
        year = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year"));
        monthnum = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum"));
        day = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("day"));
        title = "";

    	// If there's a category
        if (!empty(cat))  {
			// category exclusion
            if (!booleanval(Strings.stristr(cat, "-"))) {
                title = strval(
                        getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_cat_title", getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category_by_ID(intval(cat))));
            }
        } else if (!empty(category_name)) {
            if (booleanval(Strings.stristr(category_name, "/"))) {
                Array<String> category_nameArray = Strings.explode("/", category_name);

                if (booleanval(category_nameArray.getValue(Array.count(category_nameArray) - 1))) {
                    category_name = category_nameArray.getValue(Array.count(category_nameArray) - 1); // no trailing slash
                } else {
                    category_name = category_nameArray.getValue(Array.count(category_nameArray) - 2); // there was a trailling slash
                }
            }

            StdClass catObj = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("slug", category_name, "category", gConsts.getOBJECT(), "display");

            if (booleanval(catObj)) {
                title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_cat_title", StdClass.getValue(catObj, "name")));
            }
        }

        if (!empty(tag)) {
            Object tagObj = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(tag, "post_tag", gConsts.getOBJECT(), "display");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(tagObj)) {
                return tagObj;
            }

            if (!empty(((StdClass) tagObj).fields.getValue("name"))) {
                title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_tag_title", ((StdClass) tagObj).fields.getValue("name")));
            }
        }

    	// If there's an author
        if (!empty(author)) {
            StdClass userData = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(author);
            title = strval(StdClass.getValue(userData, "display_name"));
        }

        if (!empty(author_name)) {
    		// We do a direct query here because we don't cache by nicename.
            title = strval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT display_name FROM " + gVars.wpdb.users + " WHERE user_nicename = %s", author_name)));
        }

    	// If there's a month
        if (!empty(m)) {
            my_year = Strings.substr(m, 0, 4);
            my_month = gVars.wp_locale.get_month(Strings.substr(m, 4, 2));
            my_day = intval(Strings.substr(m, 6, 2));
            title = my_year + (booleanval(my_month)
                ? (sep + " " + my_month)
                : "") + (booleanval(my_day)
                ? (sep + " " + strval(my_day))
                : "");
        }

        if (!empty(year)) {
            title = year;

            if (!empty(monthnum)) {
                title = title + " " + sep + " " + gVars.wp_locale.get_month(monthnum);
            }

            if (!empty(day)) {
                title = title + " " + sep + " " + getIncluded(FormattingPage.class, gVars, gConsts).zeroise(day, 2);
            }
        }

    	// If there is a post
        if (getIncluded(QueryPage.class, gVars, gConsts).is_single() || getIncluded(QueryPage.class, gVars, gConsts).is_page("")) {
            post = (StdClass) gVars.wp_query.get_queried_object();
            title = Strings.strip_tags(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_post_title", StdClass.getValue(post, "post_title"))));
        }

        prefix = "";

        if (!empty(title)) {
            prefix = " " + sep + " ";
        }

     	// Determines position of the separator
        if (equal("right", seplocation)) {
            title = title + prefix;
        } else {
            title = prefix + title;
        }

        title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_title", title, sep));

    	// Send it out
        if (display) {
            echo(gVars.webEnv, title);
        } else {
            return title;
        }

        return "";
    }

    public String single_post_title(Object prefix, Object display) {
        Object p = null;
        Object name = null;
        StdClass post;
        String title = null;
        p = getIncluded(QueryPage.class, gVars, gConsts).get_query_var("p");
        name = getIncluded(QueryPage.class, gVars, gConsts).get_query_var("name");

        if (booleanval(p) || !equal("", name)) {
            if (!booleanval(p)) {
                p = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_name = %s", name));
            }

            post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(p, gConsts.getOBJECT(), "raw");
            title = strval(StdClass.getValue(post, "post_title"));
            title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_post_title", title));

            if (booleanval(display)) {
                echo(gVars.webEnv, strval(prefix) + Strings.strip_tags(title));
            } else {
                return Strings.strip_tags(title);
            }
        }

        return "";
    }

    public String single_cat_title(String prefix, boolean display) {
        String cat;
        String my_cat_name = null;
        cat = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("cat"));

        if (!empty(cat) && !equal(Strings.strtoupper(cat), "ALL")) {
            my_cat_name = strval(
                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_cat_title", getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category_by_ID(intval(cat))));

            if (!empty(my_cat_name)) {
                if (display) {
                    echo(gVars.webEnv, prefix + Strings.strip_tags(my_cat_name));
                } else {
                    return Strings.strip_tags(my_cat_name);
                }
            }
        } else if (getIncluded(QueryPage.class, gVars, gConsts).is_tag("")) {
            return single_tag_title(prefix, display);
        }

        return "";
    }

    public String single_tag_title(String prefix, boolean display) {
        Integer tag_id = null;
        Object my_tag;
        String my_tag_name = null;

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_tag("")) {
            return null;
        }

        tag_id = intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("tag_id"));

        if (!empty(tag_id)) {
            my_tag = getIncluded(TaxonomyPage.class, gVars, gConsts).get_term(tag_id, "post_tag", gConsts.getOBJECT(), "display");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(my_tag)) {
                return strval(false);
            }

            my_tag_name = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("single_tag_title", ((StdClass) my_tag).fields.getValue("name")));

            if (!empty(my_tag_name)) {
                if (display) {
                    echo(gVars.webEnv, prefix + my_tag_name);
                } else {
                    return my_tag_name;
                }
            }
        }

        return "";
    }

    public String single_month_title(String prefix, boolean display) {
        String m = null;
        Object year = null;
        String monthnum = null;
        Object my_year = null;
        Object my_month = null;
        String result = null;
        m = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("m"));
        year = getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year");
        monthnum = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum"));

        if (!empty(monthnum) && !empty(year)) {
            my_year = year;
            my_month = gVars.wp_locale.get_month(monthnum);
        } else if (!empty(m)) {
            my_year = Strings.substr(m, 0, 4);
            my_month = gVars.wp_locale.get_month(Strings.substr(m, 4, 2));
        }

        if (empty(my_month)) {
            return "";
        }

        result = prefix + strval(my_month) + prefix + strval(my_year);

        if (!display) {
            return result;
        }

        echo(gVars.webEnv, result);

        return "";
    }

    /**
     * link navigation hack by Orien http://icecode.com
     */
    public Object get_archives_link(String url, String text, Object format, Object before, Object after) {
        Object title_text = null;
        text = getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(text);
        title_text = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(text);
        url = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(url, null, "display");

        if (equal("link", format)) {
            return "\t<link rel=\'archives\' title=\'" + title_text + "\' href=\'" + url + "\' />\n";
        } else if (equal("option", format)) {
            return "\t<option value=\'" + url + "\'>" + before + " " + text + " " + after + "</option>\n";
        } else if (equal("html", format)) {
            return "\t<li>" + before + "<a href=\'" + url + "\' title=\'" + title_text + "\'>" + text + "</a>" + after + "</li>\n";
        } else { // custom
            return "\t" + before + "<a href=\'" + url + "\' title=\'" + title_text + "\'>" + text + "</a>" + after + "\n";
        }
    }

    public void wp_get_archives(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        String type = null;
        String limit = null;
        String archive_week_separator = null;
        Integer archive_date_format_over_ride = null;
        String archive_day_date_format = null;
        String archive_week_start_date_format = null;
        String archive_week_end_date_format = null;
        Object where = null;
        Object join = null;
        String query = null;
        String key = null;
        Array<Object> cache = new Array<Object>();
        Array<StdClass> arcresults = new Array<StdClass>();
        Object afterafter = null;
        Object after = null;
        String url = null;
        StdClass arcresult = null;
        String text = null;
        Object show_post_count = null;
        Object format = null;
        Object before = null;
        String date = null;
        Object start_of_week = null;
        String arc_w_last = null;
        String arc_year = null;
        Array<Object> arc_week = new Array<Object>();
        String arc_week_start = null;
        String arc_week_end = null;
        String orderby = null;
        Object arc_title = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("type", "monthly"),
                new ArrayEntry<Object>("limit", ""),
                new ArrayEntry<Object>("format", "html"),
                new ArrayEntry<Object>("before", ""),
                new ArrayEntry<Object>("after", ""),
                new ArrayEntry<Object>("show_post_count", false));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        type = strval(Array.extractVar(r, "type", type, Array.EXTR_SKIP));
        limit = strval(Array.extractVar(r, "limit", limit, Array.EXTR_SKIP));
        after = Array.extractVar(r, "after", after, Array.EXTR_SKIP);
        show_post_count = Array.extractVar(r, "show_post_count", show_post_count, Array.EXTR_SKIP);
        format = Array.extractVar(r, "format", format, Array.EXTR_SKIP);
        before = Array.extractVar(r, "before", before, Array.EXTR_SKIP);

        if (equal("", type)) {
            type = "monthly";
        }

        if (!equal("", limit)) {
            limit = strval(getIncluded(FunctionsPage.class, gVars, gConsts).absint(limit));
            limit = " LIMIT " + limit;
        }

    	// this is what will separate dates on weekly archive links
        archive_week_separator = "&#8211;";
        
    	// over-ride general date format ? 0 = no: use the date format set in Options, 1 = yes: over-ride
        archive_date_format_over_ride = 0;
        
    	// options for daily archive (only if you over-ride the general date format)
        archive_day_date_format = "Y/m/d";
        
    	// options for weekly archive (only if you over-ride the general date format)
        archive_week_start_date_format = "Y/m/d";
        archive_week_end_date_format = "Y/m/d";

        if (!booleanval(archive_date_format_over_ride)) {
            archive_day_date_format = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format"));
            archive_week_start_date_format = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format"));
            archive_week_end_date_format = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format"));
        }

    	//filters
        where = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("getarchives_where", "WHERE post_type = \'post\' AND post_status = \'publish\'", r);
        join = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("getarchives_join", "", r);

        if (equal("monthly", type)) {
            query = "SELECT DISTINCT YEAR(post_date) AS `year`, MONTH(post_date) AS `month`, count(ID) as posts FROM " + gVars.wpdb.posts + " " + strval(join) + " " + strval(where) +
                " GROUP BY YEAR(post_date), MONTH(post_date) ORDER BY post_date DESC " + limit;
            key = Strings.md5(query);
            cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("wp_get_archives", "general");

            if (cache == null) {
                cache = new Array<Object>();
            }

            if (!isset(cache.getValue(key))) {
                arcresults = gVars.wpdb.get_results(query);
                cache.putValue(key, arcresults);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("wp_get_archives", cache, "general", 0);
            } else {
                arcresults = (Array<StdClass>) cache.getValue(key);
            }

            if (booleanval(arcresults)) {
                afterafter = after;

                for (Map.Entry javaEntry477 : arcresults.entrySet()) {
                    arcresult = (StdClass) javaEntry477.getValue();
                    url = getIncluded(Link_templatePage.class, gVars, gConsts).get_month_link(strval(StdClass.getValue(arcresult, "year")), strval(StdClass.getValue(arcresult, "month")));
                    text = QStrings.sprintf(
                            getIncluded(L10nPage.class, gVars, gConsts).__("%1$s %2$d", "default"),
                            gVars.wp_locale.get_month(strval(StdClass.getValue(arcresult, "month"))),
                            strval(StdClass.getValue(arcresult, "year")));

                    if (booleanval(show_post_count)) {
                        after = "&nbsp;(" + StdClass.getValue(arcresult, "posts") + ")" + strval(afterafter);
                    }

                    echo(gVars.webEnv, get_archives_link(url, text, format, before, after));
                }
            }
        } else if (equal("yearly", type)) {
            query = "SELECT DISTINCT YEAR(post_date) AS `year`, count(ID) as posts FROM " + gVars.wpdb.posts + " " + strval(join) + " " + strval(where) +
                " GROUP BY YEAR(post_date) ORDER BY post_date DESC " + limit;
            key = Strings.md5(query);
            cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("wp_get_archives", "general");

            if (!isset(cache.getValue(key))) {
                arcresults = gVars.wpdb.get_results(query);
                cache.putValue(key, arcresults);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("wp_get_archives", cache, "general", 0);
            } else {
                arcresults = (Array<StdClass>) cache.getValue(key);
            }

            if (booleanval(arcresults)) {
                afterafter = after;

                for (Map.Entry javaEntry478 : arcresults.entrySet()) {
                    arcresult = (StdClass) javaEntry478.getValue();
                    url = getIncluded(Link_templatePage.class, gVars, gConsts).get_year_link(strval(StdClass.getValue(arcresult, "year")));
                    text = QStrings.sprintf("%d", strval(StdClass.getValue(arcresult, "year")));

                    if (booleanval(show_post_count)) {
                        after = "&nbsp;(" + strval(StdClass.getValue(arcresult, "posts")) + ")" + strval(afterafter);
                    }

                    echo(gVars.webEnv, get_archives_link(url, text, format, before, after));
                }
            }
        } else if (equal("daily", type)) {
            query = "SELECT DISTINCT YEAR(post_date) AS `year`, MONTH(post_date) AS `month`, DAYOFMONTH(post_date) AS `dayofmonth`, count(ID) as posts FROM " + gVars.wpdb.posts + " " + strval(join) +
                " " + strval(where) + " GROUP BY YEAR(post_date), MONTH(post_date), DAYOFMONTH(post_date) ORDER BY post_date DESC " + limit;
            key = Strings.md5(query);
            cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("wp_get_archives", "general");

            if (!isset(cache.getValue(key))) {
                arcresults = gVars.wpdb.get_results(query);
                cache.putValue(key, arcresults);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("wp_get_archives", cache, "general", 0);
            } else {
                arcresults = (Array<StdClass>) cache.getValue(key);
            }

            if (booleanval(arcresults)) {
                afterafter = after;

                for (Map.Entry javaEntry479 : arcresults.entrySet()) {
                    arcresult = (StdClass) javaEntry479.getValue();
                    url = getIncluded(Link_templatePage.class, gVars, gConsts).get_day_link(
                            strval(StdClass.getValue(arcresult, "year")),
                            strval(StdClass.getValue(arcresult, "month")),
                            strval(StdClass.getValue(arcresult, "dayofmonth")));
                    date = QStrings.sprintf("%1$d-%2$02d-%3$02d 00:00:00", strval(StdClass.getValue(arcresult, "year")), strval(StdClass.getValue(arcresult, "month")),
                            strval(StdClass.getValue(arcresult, "dayofmonth")));
                    text = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(archive_day_date_format, date, true);

                    if (booleanval(show_post_count)) {
                        after = "&nbsp;(" + StdClass.getValue(arcresult, "posts") + ")" + strval(afterafter);
                    }

                    echo(gVars.webEnv, get_archives_link(url, text, format, before, after));
                }
            }
        } else if (equal("weekly", type)) {
            start_of_week = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("start_of_week");
            query = "SELECT DISTINCT WEEK(post_date, " + strval(start_of_week) + ") AS `week`, YEAR(post_date) AS yr, DATE_FORMAT(post_date, \'%Y-%m-%d\') AS yyyymmdd, count(ID) as posts FROM " +
                gVars.wpdb.posts + " " + strval(join) + " " + strval(where) + " GROUP BY WEEK(post_date, " + strval(start_of_week) + "), YEAR(post_date) ORDER BY post_date DESC " + limit;
            key = Strings.md5(query);
            cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("wp_get_archives", "general");

            if (!isset(cache.getValue(key))) {
                arcresults = gVars.wpdb.get_results(query);
                cache.putValue(key, arcresults);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("wp_get_archives", cache, "general", 0);
            } else {
                arcresults = (Array<StdClass>) cache.getValue(key);
            }

            arc_w_last = "";
            afterafter = after;

            if (booleanval(arcresults)) {
                for (Map.Entry javaEntry480 : arcresults.entrySet()) {
                    arcresult = (StdClass) javaEntry480.getValue();

                    if (!equal(strval(StdClass.getValue(arcresult, "week")), arc_w_last)) {
                        arc_year = strval(StdClass.getValue(arcresult, "yr"));
                        arc_w_last = strval(StdClass.getValue(arcresult, "week"));
                        arc_week = getIncluded(FunctionsPage.class, gVars, gConsts).get_weekstartend(
                                strval(StdClass.getValue(arcresult, "yyyymmdd")),
                                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("start_of_week"));
                        arc_week_start = getIncluded(FunctionsPage.class, gVars, gConsts).date_i18n(archive_week_start_date_format, intval(arc_week.getValue("start")));
                        arc_week_end = getIncluded(FunctionsPage.class, gVars, gConsts).date_i18n(archive_week_end_date_format, intval(arc_week.getValue("end")));
                        url = QStrings.sprintf(
                                "%1$s/%2$s%3$sm%4$s%5$s%6$sw%7$s%8$d",
                                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"),
                                "",
                                "?",
                                "=",
                                arc_year,
                                "&amp;",
                                "=",
                                strval(StdClass.getValue(arcresult, "week")));
                        text = arc_week_start + archive_week_separator + arc_week_end;

                        if (booleanval(show_post_count)) {
                            after = "&nbsp;(" + StdClass.getValue(arcresult, "posts") + ")" + strval(afterafter);
                        }

                        echo(gVars.webEnv, get_archives_link(url, text, format, before, after));
                    }
                }
            }
        } else if (equal("postbypost", type) || equal("alpha", type)) {
            ExpressionHelper.execExpr(equal("alpha", type)
                ? (orderby = "post_title ASC ")
                : (orderby = "post_date DESC "));
            query = "SELECT * FROM " + gVars.wpdb.posts + " " + strval(join) + " " + strval(where) + " ORDER BY " + orderby + " " + limit;
            key = Strings.md5(query);
            cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("wp_get_archives", "general");

            if (!isset(cache.getValue(key))) {
                arcresults = gVars.wpdb.get_results(query);
                cache.putValue(key, arcresults);
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add("wp_get_archives", cache, "general", 0);
            } else {
                arcresults = (Array<StdClass>) cache.getValue(key);
            }

            if (booleanval(arcresults)) {
                for (Map.Entry javaEntry481 : arcresults.entrySet()) {
                    arcresult = (StdClass) javaEntry481.getValue();

                    if (!equal(strval(StdClass.getValue(arcresult, "post_date")), "0000-00-00 00:00:00")) {
                        url = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(new Ref(arcresult), false);

                        // Modified by Numiton
                        arc_title = strval(StdClass.getValue(arcresult, "post_title"));

                        if (booleanval(arc_title)) {
                            text = Strings.strip_tags(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", arc_title)));
                        } else {
                            text = strval(StdClass.getValue(arcresult, "ID"));
                        }

                        echo(gVars.webEnv, get_archives_link(url, text, format, before, after));
                    }
                }
            }
        }
    }

    /**
     * Used in get_calendar Used in get_calendar
     */
    public float calendar_week_mod(int num) {
        int base = 0;
        base = 7;

        return floatval(num) - (floatval(base) * Math.floor(floatval(num) / floatval(base)));
    }

    public void get_calendar(boolean initial) {
        String key = null;
        Array<Object> cache = new Array<Object>();
        Object gotsome = null;
        String w = null;
        int week_begins = 0;
        String thismonth = null;
        String thisyear = null;
        int d = 0;
        int unixmonth = 0;
        StdClass previous;
        StdClass next;
        Array<Object> myweek = new Array<Object>();
        int wdcount = 0;
        Object day_name = null;
        String wd = null;
        Array<Object> dayswithposts = new Array<Object>();
        Array<Object> daywithpost = new Array<Object>();
        Array<Object> daywith = new Array<Object>();
        String ak_title_separator = null;
        Array<Object> ak_titles_for_day = new Array<Object>();
        Array<Object> ak_post_titles = new Array<Object>();
        String post_title;
        StdClass ak_post_title = null;
        float pad = 0;
        int daysinmonth = 0;
        Boolean newrow = null;
        int day = 0;
        String output = null;
        
        key = Strings.md5(gVars.m + strval(gVars.monthnum) + strval(gVars.year));

        if (booleanval(cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("get_calendar", "calendar"))) {
            if (isset(cache.getValue(key))) {
                echo(gVars.webEnv, cache.getValue(key));

                return;
            }
        } else {
            cache = new Array<Object>();
        }

        OutputControl.ob_start(gVars.webEnv);
    	// Quick check. If we have no posts at all, abort!
        if (!booleanval(gVars.posts)) {
            gotsome = gVars.wpdb.get_var("SELECT ID from " + gVars.wpdb.posts + " WHERE post_type = \'post\' AND post_status = \'publish\' ORDER BY post_date DESC LIMIT 1");

            if (!booleanval(gotsome)) {
                return;
            }
        }

        if (isset(gVars.webEnv._GET.getValue("w"))) {
            w = "" + strval(gVars.webEnv._GET.getValue("w"));
        }

    	// week_begins = 0 stands for Sunday
        week_begins = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("start_of_week"));

    	// Let's figure out when we are
        if (!empty(gVars.monthnum) && !empty(gVars.year)) {
            thismonth = "" + getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(gVars.monthnum), 2);
            thisyear = "" + strval(gVars.year);
        } else if (!empty(w)) {
    		// We need to get the month from MySQL
            thisyear = "" + Strings.substr(gVars.m, 0, 4);
            d = ((intval(w) - 1) * 7) + 6; //it seems MySQL's weeks disagree with PHP's
            thismonth = strval(gVars.wpdb.get_var("SELECT DATE_FORMAT((DATE_ADD(\'" + thisyear + "0101\', INTERVAL " + d + " DAY) ), \'%m\')"));
        } else if (!empty(gVars.m)) {
            thisyear = "" + Strings.substr(gVars.m, 0, 4);

            if (Strings.strlen(gVars.m) < 6) {
                thismonth = "01";
            } else {
                thismonth = "" + getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(Strings.substr(gVars.m, 4, 2)), 2);
            }
        } else {
            thisyear = DateTime.gmdate("Y", intval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("timestamp", false)));
            thismonth = DateTime.gmdate("m", intval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("timestamp", false)));
        }

        unixmonth = DateTime.mktime(0, 0, 0, intval(thismonth), 1, intval(thisyear));
        
    	// Get the next and previous month and year with at least one post
        previous = (StdClass) gVars.wpdb.get_row(
                    "SELECT DISTINCT MONTH(post_date) AS month, YEAR(post_date) AS year\n\t\tFROM " + gVars.wpdb.posts + "\n\t\tWHERE post_date < \'" + thisyear + "-" + thismonth +
                    "-01\'\n\t\tAND post_type = \'post\' AND post_status = \'publish\'\n\t\t\tORDER BY post_date DESC\n\t\t\tLIMIT 1");
        next = (StdClass) gVars.wpdb.get_row(
                    "SELECT\tDISTINCT MONTH(post_date) AS month, YEAR(post_date) AS year\n\t\tFROM " + gVars.wpdb.posts + "\n\t\tWHERE post_date >\t\'" + thisyear + "-" + thismonth +
                    "-01\'\n\t\tAND MONTH( post_date ) != MONTH( \'" + thisyear + "-" + thismonth +
                    "-01\' )\n\t\tAND post_type = \'post\' AND post_status = \'publish\'\n\t\t\tORDER\tBY post_date ASC\n\t\t\tLIMIT 1");
        echo(
                gVars.webEnv,
                "<table id=\"wp-calendar\" summary=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Calendar", "default") + "\">\n\t<caption>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)._c("%1$s %2$s|Used as a calendar caption", "default"), gVars.wp_locale.get_month(thismonth), DateTime.date("Y", unixmonth)) +
                "</caption>\n\t<thead>\n\t<tr>");
        myweek = new Array<Object>();

        for (wdcount = 0; wdcount <= 6; wdcount++) {
            myweek.putValue(gVars.wp_locale.get_weekday((wdcount + week_begins) % 7));
        }

        for (Map.Entry javaEntry482 : myweek.entrySet()) {
            wd = strval(javaEntry482.getValue());
            day_name = (equal(true, initial)
                ? gVars.wp_locale.get_weekday_initial(wd)
                : gVars.wp_locale.get_weekday_abbrev(wd));
            echo(gVars.webEnv, "\n\t\t<th abbr=\"" + wd + "\" scope=\"col\" title=\"" + wd + "\">" + strval(day_name) + "</th>");
        }

        echo(gVars.webEnv, "\n\t</tr>\n\t</thead>\n\n\t<tfoot>\n\t<tr>");

        if (booleanval(previous)) {
            echo(
                    gVars.webEnv,
                    "\n\t\t" + "<td abbr=\"" + gVars.wp_locale.get_month(strval(StdClass.getValue(previous, "month"))) + "\" colspan=\"3\" id=\"prev\"><a href=\"" +
                    getIncluded(Link_templatePage.class, gVars, gConsts).get_month_link(strval(StdClass.getValue(previous, "year")), strval(StdClass.getValue(previous, "month"))) + "\" title=\"" +
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("View posts for %1$s %2$s", "default"),
                        gVars.wp_locale.get_month(strval(StdClass.getValue(previous, "month"))),
                        DateTime.date("Y", DateTime.mktime(0, 0, 0, intval(StdClass.getValue(previous, "month")), 1, intval(StdClass.getValue(previous, "year"))))) + "\">&laquo; " +
                    gVars.wp_locale.get_month_abbrev(gVars.wp_locale.get_month(strval(StdClass.getValue(previous, "month")))) + "</a></td>");
        } else {
            echo(gVars.webEnv, "\n\t\t" + "<td colspan=\"3\" id=\"prev\" class=\"pad\">&nbsp;</td>");
        }

        echo(gVars.webEnv, "\n\t\t" + "<td class=\"pad\">&nbsp;</td>");

        if (booleanval(next)) {
            echo(
                    gVars.webEnv,
                    "\n\t\t" + "<td abbr=\"" + gVars.wp_locale.get_month(strval(StdClass.getValue(next, "month"))) + "\" colspan=\"3\" id=\"next\"><a href=\"" +
                    getIncluded(Link_templatePage.class, gVars, gConsts).get_month_link(strval(StdClass.getValue(next, "year")), strval(StdClass.getValue(next, "month"))) + "\" title=\"" +
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("View posts for %1$s %2$s", "default"),
                        gVars.wp_locale.get_month(strval(StdClass.getValue(next, "month"))),
                        DateTime.date("Y", DateTime.mktime(0, 0, 0, intval(StdClass.getValue(next, "month")), 1, intval(StdClass.getValue(next, "year"))))) + "\">" +
                    gVars.wp_locale.get_month_abbrev(gVars.wp_locale.get_month(strval(StdClass.getValue(next, "month")))) + " &raquo;</a></td>");
        } else {
            echo(gVars.webEnv, "\n\t\t" + "<td colspan=\"3\" id=\"next\" class=\"pad\">&nbsp;</td>");
        }

        echo(gVars.webEnv, "\n\t</tr>\n\t</tfoot>\n\n\t<tbody>\n\t<tr>");
        
    	// Get days with posts
        dayswithposts = gVars.wpdb.get_results(
                    "SELECT DISTINCT DAYOFMONTH(post_date)\n\t\tFROM " + gVars.wpdb.posts + " WHERE MONTH(post_date) = \'" + thismonth + "\'\n\t\tAND YEAR(post_date) = \'" + thisyear +
                    "\'\n\t\tAND post_type = \'post\' AND post_status = \'publish\'\n\t\tAND post_date < \'" + getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0) + "\'",
                    gConsts.getARRAY_N());

        if (booleanval(dayswithposts)) {
            for (Map.Entry javaEntry483 : dayswithposts.entrySet()) {
                daywith = (Array<Object>) javaEntry483.getValue();
                daywithpost.putValue(daywith.getValue(0));
            }
        } else {
            daywithpost = new Array<Object>();
        }

        if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "MSIE"), BOOLEAN_FALSE) ||
                !strictEqual(Strings.strpos(Strings.strtolower(gVars.webEnv.getHttpUserAgent()), "camino"), BOOLEAN_FALSE) ||
                !strictEqual(Strings.strpos(Strings.strtolower(gVars.webEnv.getHttpUserAgent()), "safari"), BOOLEAN_FALSE)) {
            ak_title_separator = "\n";
        } else {
            ak_title_separator = ", ";
        }

        ak_titles_for_day = new Array<Object>();
        ak_post_titles = gVars.wpdb.get_results(
                    "SELECT post_title, DAYOFMONTH(post_date) as dom " + "FROM " + gVars.wpdb.posts + " " + "WHERE YEAR(post_date) = \'" + thisyear + "\' " + "AND MONTH(post_date) = \'" + thismonth +
                    "\' " + "AND post_date < \'" + getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0) + "\' " + "AND post_type = \'post\' AND post_status = \'publish\'");

        if (booleanval(ak_post_titles)) {
            for (Map.Entry javaEntry484 : ak_post_titles.entrySet()) {
                ak_post_title = (StdClass) javaEntry484.getValue();
                post_title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(ak_post_title, "post_title")));
                post_title = Strings.str_replace("\"", "&quot;", getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(post_title));

                if (empty(ak_titles_for_day.getValue("day_" + StdClass.getValue(ak_post_title, "dom")))) {
                    ak_titles_for_day.putValue("day_" + StdClass.getValue(ak_post_title, "dom"), "");
                }

                if (empty(ak_titles_for_day.getValue(StdClass.getValue(ak_post_title, "dom")))) { // first one
                    ak_titles_for_day.putValue(StdClass.getValue(ak_post_title, "dom"), post_title);
                } else {
                    ak_titles_for_day.putValue(StdClass.getValue(ak_post_title, "dom"), strval(ak_titles_for_day.getValue(StdClass.getValue(ak_post_title, "dom"))) + ak_title_separator + post_title);
                }
            }
        }

    	// See how much we should pad in the beginning
        pad = calendar_week_mod(intval(DateTime.date("w", unixmonth)) - week_begins);

        if (!equal(0, pad)) {
            echo(gVars.webEnv, "\n\t\t" + "<td colspan=\"" + strval(pad) + "\" class=\"pad\">&nbsp;</td>");
        }

        daysinmonth = intval(DateTime.date("t", unixmonth));

        for (day = 1; day <= daysinmonth; ++day)/* ? */
         {
            if (isset(newrow) && newrow) {
                echo(gVars.webEnv, "\n\t</tr>\n\t<tr>\n\t\t");
            }

            newrow = false;

            if (equal(day, DateTime.gmdate("j", DateTime.time() + intval(floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600))) &&
                    equal(thismonth, DateTime.gmdate("m", DateTime.time() + intval(floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600))) &&
                    equal(thisyear, DateTime.gmdate("Y", DateTime.time() + intval(floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)))) {
                echo(gVars.webEnv, "<td id=\"today\">");
            } else {
                echo(gVars.webEnv, "<td>");
            }

            if (Array.in_array(day, daywithpost)) { // any posts today?
                echo(
                        gVars.webEnv,
                        "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_day_link(thisyear, thismonth, strval(day)) + "\" title=\"" + strval(ak_titles_for_day.getValue(day)) +
                        "\">" + strval(day) + "</a>");
            } else {
                echo(gVars.webEnv, day);
            }

            echo(gVars.webEnv, "</td>");

            if (equal(6, calendar_week_mod(intval(DateTime.date("w", DateTime.mktime(0, 0, 0, intval(thismonth), day, intval(thisyear)))) - week_begins))) {
                newrow = true;
            }
        }

        pad = floatval(7) - calendar_week_mod(intval(DateTime.date("w", DateTime.mktime(0, 0, 0, intval(thismonth), day, intval(thisyear)))) - week_begins);

        if (!equal(pad, 0) && !equal(pad, 7)) {
            echo(gVars.webEnv, "\n\t\t" + "<td class=\"pad\" colspan=\"" + strval(pad) + "\">&nbsp;</td>");
        }

        echo(gVars.webEnv, "\n\t</tr>\n\t</tbody>\n\t</table>");
        output = OutputControl.ob_get_contents(gVars.webEnv);
        OutputControl.ob_end_clean(gVars.webEnv);
        echo(gVars.webEnv, output);
        cache.putValue(key, output);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("get_calendar", cache, "calendar", 0);
    }

    public void delete_get_calendar_cache(Object... deprecated) {
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("get_calendar", "calendar");
    }

    public String allowed_tags() {
        String allowed = null;
        String tag = null;
        Array<Object> attributes = null;
        Object attribute = null;
        Object limits = null;
        allowed = "";

        for (Map.Entry javaEntry485 : gVars.allowedtags.entrySet()) {
            tag = strval(javaEntry485.getKey());
            attributes = (Array<Object>) javaEntry485.getValue();
            allowed = allowed + "<" + tag;

            if (0 < Array.count(attributes)) {
                for (Map.Entry javaEntry486 : attributes.entrySet()) {
                    attribute = javaEntry486.getKey();
                    limits = javaEntry486.getValue();
                    allowed = allowed + " " + strval(attribute) + "=\"\"";
                }
            }

            allowed = allowed + "> ";
        }

        return Strings.htmlentities(allowed);
    }

    /***** Date/Time tags *****/
    public void the_date_xml() {
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("Y-m-d", strval(StdClass.getValue(gVars.post, "post_date")), true));
    	//echo ""+$post->post_date;
    }

    public String the_date(String d, String before, String after) {
        return the_date(d, before, after, true);
    }

    public String the_date(String d, String before, String after, boolean echo) {
        String the_date = null;
        the_date = "";

        if (!equal(gVars.day, gVars.previousday)) {
            the_date = the_date + before;

            if (equal(d, "")) {
                the_date = the_date +
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                        strval(StdClass.getValue(gVars.post, "post_date")), true);
            } else {
                the_date = the_date + getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(d, strval(StdClass.getValue(gVars.post, "post_date")), true);
            }

            the_date = the_date + after;
            gVars.previousday = gVars.day;
        }

        the_date = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_date", the_date, d, before, after));

        if (echo) {
            echo(gVars.webEnv, the_date);
        } else {
            return the_date;
        }

        return "";
    }

    public void the_modified_date(String d) {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_modified_date", get_the_modified_date(d), d));
    }

    public Object get_the_modified_date(String d) {
        Object the_time = null;

        if (equal("", d)) {
            the_time = get_post_modified_time(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")), false);
        } else {
            the_time = get_post_modified_time(d, false);
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_modified_date", the_time, d);
    }

    public void the_time() {
        the_time("");
    }

    public void the_time(String d) {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_time", get_the_time(d), d));
    }

    public Object get_the_time(String d) {
        Object the_time = null;

        if (equal("", d)) {
            the_time = get_post_time(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")), false);
        } else {
            the_time = get_post_time(d, false);
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_time", the_time, d);
    }

    public String get_post_time(String d, boolean gmt) { // returns timestamp
        String time = null;

        if (gmt) {
            time = strval(StdClass.getValue(gVars.post, "post_date_gmt"));
        } else {
            time = strval(StdClass.getValue(gVars.post, "post_date"));
        }

        time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(d, time, true);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_time", time, d, gmt));
    }

    public void the_modified_time(String d) {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_modified_time", get_the_modified_time(d), d));
    }

    public String get_the_modified_time(String d) {
        String the_time = null;

        if (equal("", d)) {
            the_time = get_post_modified_time(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")), false);
        } else {
            the_time = get_post_modified_time(d, false);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_modified_time", the_time, d));
    }

    public String get_post_modified_time(String d, boolean gmt) { // returns timestamp
        String time = null;

        if (gmt) {
            time = strval(StdClass.getValue(gVars.post, "post_modified_gmt"));
        } else {
            time = strval(StdClass.getValue(gVars.post, "post_modified"));
        }

        time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(d, time, true);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_modified_time", time, d, gmt));
    }

    public void the_weekday() {
        Object the_weekday = null;
        the_weekday = gVars.wp_locale.get_weekday(intval(getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("w", strval(StdClass.getValue(gVars.post, "post_date")), true)));
        the_weekday = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_weekday", the_weekday);
        echo(gVars.webEnv, the_weekday);
    }

    public void the_weekday_date(Object before, Object after) {
        String the_weekday_date = null;
        the_weekday_date = "";

        if (!equal(gVars.day, previousweekday)) {
            the_weekday_date = the_weekday_date + strval(before);
            the_weekday_date = the_weekday_date +
                gVars.wp_locale.get_weekday(intval(getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("w", strval(StdClass.getValue(gVars.post, "post_date")), true)));
            the_weekday_date = the_weekday_date + strval(after);
            previousweekday = gVars.day;
        }

        the_weekday_date = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_weekday_date", the_weekday_date, before, after));
        echo(gVars.webEnv, the_weekday_date);
    }

    public void wp_head() {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_head", "");
    }

    public void wp_footer() {
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_footer", "");
    }

    public void rsd_link() {
        echo(gVars.webEnv, "<link rel=\"EditURI\" type=\"application/rsd+xml\" title=\"RSD\" href=\"" + get_bloginfo("wpurl", "raw") + "/xmlrpc.php?rsd\" />\n");
    }

    public void wlwmanifest_link() {
        echo(gVars.webEnv, "<link rel=\"wlwmanifest\" type=\"application/wlwmanifest+xml\" href=\"" + get_bloginfo("wpurl", "raw") + "/wp-includes/wlwmanifest.xml\" /> " + "\n");
    }

    public void noindex() {
    	// If the blog is not public, tell robots to go away.
        if (equal("0", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_public"))) {
            echo(gVars.webEnv, "<meta name=\'robots\' content=\'noindex,nofollow\' />\n");
        }
    }

    public boolean rich_edit_exists() {
        if (!isset(wp_rich_edit_exists)) {
            wp_rich_edit_exists = FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getWPINC() + "/js/tinymce/tiny_mce.js");
        }

        return wp_rich_edit_exists;
    }

    public Object user_can_richedit() {
        Array<Object> match = new Array<Object>();

        if (!isset(wp_rich_edit)) {
            if (equal(getIncluded(UserPage.class, gVars, gConsts).get_user_option("rich_editing"), "true") && /*, 0*/
                    ((QRegExPerl.preg_match("!AppleWebKit/(\\d+)!", gVars.webEnv.getHttpUserAgent(), match) && (intval(match.getValue(1)) >= 420)) ||
                    !QRegExPerl.preg_match("!opera[ /][2-8]|konqueror|safari!i", gVars.webEnv.getHttpUserAgent())) && !equal("comment.php", gVars.pagenow)) {
                wp_rich_edit = true;
            } else {
                wp_rich_edit = false;
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("user_can_richedit", wp_rich_edit);
    }

    public String wp_default_editor() {
        String r = null;
        WP_User user = null;
        r = (booleanval(user_can_richedit())
            ? "tinymce"
            : "html"); // defaults

        if (booleanval(user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user())) { // look for cookie
            if (isset(gVars.webEnv._COOKIE.getValue("wordpress_editor_" + user.getID())) &&
                    Array.in_array(
                        gVars.webEnv._COOKIE.getValue("wordpress_editor_" + user.getID()),
                        new Array<Object>(new ArrayEntry<Object>("tinymce"), new ArrayEntry<Object>("html"), new ArrayEntry<Object>("test")))) {
                r = strval(gVars.webEnv._COOKIE.getValue("wordpress_editor_" + user.getID()));
            }
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_default_editor", r)); // filter
    }

    public void the_editor(String content, String id, String prev_id, boolean media_buttons, int tab_index) {
        String rows = null;
        String wp_default_editor = null;
        String the_editor = null;
        String the_editor_content = null;
        rows = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_post_edit_rows"));

        if ((intval(rows) < 3) || (intval(rows) > 100)) {
            rows = strval(12);
        }

        rows = "rows=\'" + rows + "\'";
        echo(gVars.webEnv, "\t<div id=\"editor-toolbar\">\n\t");

        if (booleanval(user_can_richedit())) {
            wp_default_editor = wp_default_editor();
            echo(gVars.webEnv, "\t\t<div class=\"zerosize\"><input accesskey=\"e\" type=\"button\" onclick=\"switchEditors.go(\'");
            echo(gVars.webEnv, id);
            echo(gVars.webEnv, "\')\" /></div>\n\t\t");

            if (equal("tinymce", wp_default_editor)) {
                getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_editor_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_richedit_pre"), 10, 1);
                echo(gVars.webEnv, "\t\t\t<a id=\"edButtonHTML\" onclick=\"switchEditors.go(\'");
                echo(gVars.webEnv, id);
                echo(gVars.webEnv, "\');\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("HTML", "default");
                echo(gVars.webEnv, "</a>\n\t\t\t<a id=\"edButtonPreview\" class=\"active\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Visual", "default");
                echo(gVars.webEnv, "</a>\n\t\t");
            } else if (equal("html", wp_default_editor)) {
                getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_editor_content", Callback.createCallbackArray(getIncluded(FormattingPage.class, gVars, gConsts), "wp_htmledit_pre"), 10, 1);
                echo(gVars.webEnv, "\t\t\t<a id=\"edButtonHTML\" class=\"active\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("HTML", "default");
                echo(gVars.webEnv, "</a>\n\t\t\t<a id=\"edButtonPreview\" onclick=\"switchEditors.go(\'");
                echo(gVars.webEnv, id);
                echo(gVars.webEnv, "\');\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Visual", "default");
                echo(gVars.webEnv, "</a>\n\t\t");
            }
        }

        if (media_buttons) {
            echo(gVars.webEnv, "\t\t<div id=\"media-buttons\" class=\"hide-if-no-js\">\n\t\t");
            getIncluded(PluginPage.class, gVars, gConsts).do_action("media_buttons", "");
            echo(gVars.webEnv, "\t\t</div>\n\t");
        }

        echo(gVars.webEnv, "\t</div>\n\n\t<div id=\"quicktags\">\n\t");
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_print_scripts("quicktags");
        echo(gVars.webEnv, "\t<script type=\"text/javascript\">edToolbar()</script>\n\t</div>\n\n    ");

        if (!equal("html", wp_default_editor)) {
            echo(
                    gVars.webEnv,
                    "    <script type=\"text/javascript\">\n    // <![CDATA[\n        if ( typeof tinyMCE != \"undefined\" )\n            document.getElementById(\"quicktags\").style.display=\"none\";\n    // ]]>\n    </script>\n    ");
        } else {
        } // 'html' != $wp_default_editor

        the_editor = strval(
                getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                    "the_editor",
                    "<div id=\'editorcontainer\'><textarea class=\'\' " + rows + " cols=\'40\' name=\'" + id + "\' tabindex=\'" + strval(tab_index) + "\' id=\'" + id + "\'>%s</textarea></div>\n"));
        the_editor_content = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_editor_content", content));
        QStrings.printf(gVars.webEnv, the_editor, the_editor_content);
        echo(gVars.webEnv, "    <script type=\"text/javascript\">\n    // <![CDATA[\n    edCanvas = document.getElementById(\'");
        echo(gVars.webEnv, id);
        echo(gVars.webEnv, "\');\n    ");

        if (booleanval(prev_id) && booleanval(user_can_richedit())) {
            echo(
                gVars.webEnv,
                "    // If tinyMCE is defined.\n    if ( typeof tinyMCE != \'undefined\' ) {\n    // This code is meant to allow tabbing from Title to Post (TinyMCE).\n        document.getElementById(\'");
            echo(gVars.webEnv, prev_id);
            echo(
                    gVars.webEnv,
                    "\').onkeydown = function (e) {\n            e = e || window.event;\n            if (e.keyCode == 9 && !e.shiftKey && !e.controlKey && !e.altKey) {\n                if ( tinyMCE.activeEditor ) {\n                    if ( (jQuery(\"#post_ID\").val() < 1) && (jQuery(\"#title\").val().length > 0) ) { autosave(); }\n                    e = null;\n                    if ( tinyMCE.activeEditor.isHidden() ) return true;\n                    tinyMCE.activeEditor.focus();\n                    return false;\n                }\n                return true;\n            }\n        }\n    }\n    ");
        } else {
        }

        echo(gVars.webEnv, "    // ]]>\n    </script>\n    ");
    }

    public String get_search_query() {
        return strval(
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "get_search_query",
                Strings.stripslashes(gVars.webEnv, strval((((QueryPage) PhpWeb.getIncluded(QueryPage.class, gVars, gConsts))).get_query_var("s")))));
    }

    public void the_search_query() {
        echo(
            gVars.webEnv,
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_search_query", get_search_query()))));
    }

    public void language_attributes() {
        language_attributes("html");
    }

    public void language_attributes(String doctype) {
        Array<String> attributes = new Array<String>();
        String output = null;
        String dir = null;
        String lang = null;
        output = "";

        if (booleanval(dir = get_bloginfo("text_direction", "raw"))) {
            attributes.putValue("dir=\"" + dir + "\"");
        }

        if (booleanval(lang = get_bloginfo("language", "raw"))) {
            if (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type"), "text/html") || equal(doctype, "xhtml")) {
                attributes.putValue("lang=\"" + lang + "\"");
            }

            if (!equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type"), "text/html") || equal(doctype, "xhtml")) {
                attributes.putValue("xml:lang=\"" + lang + "\"");
            }
        }

        output = Strings.implode(" ", attributes);
        output = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("language_attributes", output));
        echo(gVars.webEnv, output);
    }

    public Object paginate_links(Object args) {
        Array<Object> defaults = new Array<Object>();
        Integer total = null;
        Integer current = null;
        Integer end_size = null;
        Integer mid_size = null;
        Object add_args = null;

        /* Do not change type */
        String r = null;
        Array<String> page_links = new Array<String>();
        Integer n = null;
        Boolean dots = null;
        Object prev_next = null;
        String link = null;
        String format = null;
        String base = null;
        String prev_text = null;
        Object show_all = null;
        Object next_text = null;
        Object type = null;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("base", "%_%"), // http://example.com/all_posts.php%_% : %_% is replaced by format (below)
                new ArrayEntry<Object>("format", "?page=%#%"), // ?page=%#% : %#% is replaced by the page number
                new ArrayEntry<Object>("total", 1),
                new ArrayEntry<Object>("current", 0),
                new ArrayEntry<Object>("show_all", false),
                new ArrayEntry<Object>("prev_next", true),
                new ArrayEntry<Object>("prev_text", getIncluded(L10nPage.class, gVars, gConsts).__("&laquo; Previous", "default")),
                new ArrayEntry<Object>("next_text", getIncluded(L10nPage.class, gVars, gConsts).__("Next &raquo;", "default")),
                new ArrayEntry<Object>("end_size", 1), // How many numbers on either end including the end
                new ArrayEntry<Object>("mid_size", 2), // How many numbers to either side of current not including current
                new ArrayEntry<Object>("type", "plain"),
                new ArrayEntry<Object>("add_args", false)); // array of query args to aadd

        Array argsArray = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        total = intval(Array.extractVar(argsArray, "total", total, Array.EXTR_SKIP));
        current = intval(Array.extractVar(argsArray, "current", current, Array.EXTR_SKIP));
        end_size = intval(Array.extractVar(argsArray, "end_size", end_size, Array.EXTR_SKIP));
        mid_size = intval(Array.extractVar(argsArray, "mid_size", mid_size, Array.EXTR_SKIP));
        add_args = Array.extractVar(argsArray, "add_args", add_args, Array.EXTR_SKIP);
        prev_next = Array.extractVar(argsArray, "prev_next", prev_next, Array.EXTR_SKIP);
        format = strval(Array.extractVar(argsArray, "format", format, Array.EXTR_SKIP));
        base = strval(Array.extractVar(argsArray, "base", base, Array.EXTR_SKIP));
        prev_text = strval(Array.extractVar(argsArray, "prev_text", prev_text, Array.EXTR_SKIP));
        show_all = Array.extractVar(argsArray, "show_all", show_all, Array.EXTR_SKIP);
        next_text = Array.extractVar(argsArray, "next_text", next_text, Array.EXTR_SKIP);
        type = Array.extractVar(argsArray, "type", type, Array.EXTR_SKIP);
        
    	// Who knows what else people pass in $args
//        total = intval(total);

        if (total < 2) {
            return null;
        }

        current = current;
        end_size = ((0 < end_size)
            ? end_size
            : 1); // Out of bounds?  Make it the default.
        mid_size = ((0 <= mid_size)
            ? mid_size
            : 2);
        add_args = (is_array(add_args)
            ? add_args
            : false);
        r = "";
        page_links = new Array<String>();
        n = 0;
        dots = false;

        if (booleanval(prev_next) && booleanval(current) && (1 < current)) {
            link = Strings.str_replace("%_%", equal(2, current)
                    ? ""
                    : format, base);
            link = Strings.str_replace("%#%", strval(current - 1), link);

            if (booleanval(add_args)) {
                link = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(add_args, link);
            }

            page_links.putValue("<a class=\'prev page-numbers\' href=\'" + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(link, null, "display") + "\'>" + prev_text + "</a>");
        } else {
        }

        for (n = 1; n <= total; n++) {
            if (equal(n, current)) {
                page_links.putValue("<span class=\'page-numbers current\'>" + strval(n) + "</span>");
                dots = true;
            } else {
                if (booleanval(show_all) || (n <= end_size) || (booleanval(current) && (n >= (current - mid_size)) && (n <= (current + mid_size))) || (n > (total - end_size))) {
                    link = Strings.str_replace("%_%", equal(1, n)
                            ? ""
                            : format, base);
                    link = Strings.str_replace("%#%", strval(n), link);

                    if (booleanval(add_args)) {
                        link = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(add_args, link);
                    }

                    page_links.putValue("<a class=\'page-numbers\' href=\'" + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(link, null, "display") + "\'>" + strval(n) + "</a>");
                    dots = true;
                } else if (dots && !booleanval(show_all)) {
                    page_links.putValue("<span class=\'page-numbers dots\'>...</span>");
                    dots = false;
                } else {
                }
            }
        }

        if (booleanval(prev_next) && booleanval(current) && ((current < total) || equal(-1, total))) {
            link = Strings.str_replace("%_%", format, base);
            link = Strings.str_replace("%#%", strval(current + 1), link);

            if (booleanval(add_args)) {
                link = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(add_args, link);
            }

            page_links.putValue("<a class=\'next page-numbers\' href=\'" + getIncluded(FormattingPage.class, gVars, gConsts).clean_url(link, null, "display") + "\'>" + strval(next_text) + "</a>");
        } else {
        }

        {
            int javaSwitchSelector67 = 0;

            if (equal(type, "array")) {
                javaSwitchSelector67 = 1;
            }

            if (equal(type, "list")) {
                javaSwitchSelector67 = 2;
            }

            switch (javaSwitchSelector67) {
            case 1:return page_links;

            case 2: {
                r = r + "<ul class=\'page-numbers\'>\n\t<li>";
                r = r + Strings.join("</li>\n\t<li>", page_links);
                r = r + "</li>\n</ul>\n";

                break;
            }

            default: {
                r = Strings.join("\n", page_links);

                break;
            }
            }
        }

        return r;
    }

    public void wp_admin_css_color(String key, String name, String url, Array<Object> colors) {
        if (!isset(gVars._wp_admin_css_colors)) {
            gVars._wp_admin_css_colors = new Array<Object>();
        }

        gVars._wp_admin_css_colors.putValue(
            key,
            Array.toStdClass(new Array<Object>(new ArrayEntry<Object>("name", name), new ArrayEntry<Object>("url", url), new ArrayEntry<Object>("colors", colors))));
    }

    public Object wp_admin_css_uri(Object file) {
        String _file = null;
        String color = null;

        if (gConsts.isWP_INSTALLINGDefined()) {
            _file = "./" + strval(file) + ".css";
        } else {
            if (equal("css/colors", file) || equal("css/colors-rtl", file)) {
                color = strval(getIncluded(UserPage.class, gVars, gConsts).get_user_option("admin_color"));

                if (empty(color) || !isset(gVars._wp_admin_css_colors.getValue(color))) {
                    color = "fresh";
                }

                StdClass colorObj = (StdClass) gVars._wp_admin_css_colors.getValue(color);
                _file = strval(StdClass.getValue(colorObj, "url"));
                _file = (equal("css/colors-rtl", file)
                    ? Strings.str_replace(".css", "-rtl.css", _file)
                    : _file);
            } else {
                _file = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/" + strval(file) + ".css";
            }
        }

        _file = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("version", get_bloginfo("version", "raw"), _file);

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_admin_css_uri", _file, file);
    }

    public void wp_admin_css(String file) {
        String rtl = null;
        echo(
            gVars.webEnv,
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_admin_css", "<link rel=\'stylesheet\' href=\'" + strval(wp_admin_css_uri(file)) + "\' type=\'text/css\' />\n", file));

        if (equal("rtl", get_bloginfo("text_direction", "raw"))) {
            rtl = (equal("wp-admin", file)
                ? "rtl"
                : (file + "-rtl"));
            echo(
                gVars.webEnv,
                getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_admin_css", "<link rel=\'stylesheet\' href=\'" + strval(wp_admin_css_uri(rtl)) + "\' type=\'text/css\' />\n", rtl));
        }
    }

    /**
     * Outputs the XHTML generator that is generated on the wp_head hook.
     */
    public void wp_generator() {
        the_generator(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_generator_type", "xhtml"));
    }

    /**
     * Outputs the generator XML or Comment for RSS, ATOM, etc.
     * @param {String} $type The type of generator to return.
     */
    public void the_generator(Object type) {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_generator", get_the_generator(type), type) + "\n");
    }

    /**
     * Creates the generator XML or Comment for RSS, ATOM, etc.
     * @param {String} $type The type of generator to return.
     */
    public Object get_the_generator(Object type) {
        String gen = null;

        {
            int javaSwitchSelector68 = 0;

            if (equal(type, "html")) {
                javaSwitchSelector68 = 1;
            }

            if (equal(type, "xhtml")) {
                javaSwitchSelector68 = 2;
            }

            if (equal(type, "atom")) {
                javaSwitchSelector68 = 3;
            }

            if (equal(type, "rss2")) {
                javaSwitchSelector68 = 4;
            }

            if (equal(type, "rdf")) {
                javaSwitchSelector68 = 5;
            }

            if (equal(type, "comment")) {
                javaSwitchSelector68 = 6;
            }

            if (equal(type, "export")) {
                javaSwitchSelector68 = 7;
            }

            switch (javaSwitchSelector68) {
            case 1: {
                gen = "<meta name=\"generator\" content=\"WordPress " + get_bloginfo("version", "raw") + "\">" + "\n";

                break;
            }

            case 2: {
                gen = "<meta name=\"generator\" content=\"WordPress " + get_bloginfo("version", "raw") + "\" />" + "\n";

                break;
            }

            case 3: {
                gen = "<generator uri=\"http://wordpress.org/\" version=\"" + getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("version") + "\">WordPress</generator>";

                break;
            }

            case 4: {
                gen = "<generator>http://wordpress.org/?v=" + getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("version") + "</generator>";

                break;
            }

            case 5: {
                gen = "<admin:generatorAgent rdf:resource=\"http://wordpress.org/?v=" + getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("version") + "\" />";

                break;
            }

            case 6: {
                gen = "<!-- generator=\"WordPress/" + get_bloginfo("version", "raw") + "\" -->";

                break;
            }

            case 7: {
                gen = "<!-- generator=\"WordPress/" + getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("version") + "\" created=\"" + DateTime.date("Y-m-d H:i") + "\"-->";

                break;
            }
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_generator_" + strval(type), gen, type);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_general_template_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_action("save_post", Callback.createCallbackArray(this, "delete_get_calendar_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("delete_post", Callback.createCallbackArray(this, "delete_get_calendar_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("update_option_start_of_week", Callback.createCallbackArray(this, "delete_get_calendar_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("update_option_gmt_offset", Callback.createCallbackArray(this, "delete_get_calendar_cache"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("update_option_start_of_week", Callback.createCallbackArray(this, "delete_get_calendar_cache"), 10, 1);

        return DEFAULT_VAL;
    }
}
