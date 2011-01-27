/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Post_templatePage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.FunctionHandling;
import com.numiton.Math;
import com.numiton.RegExPerl;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QImage;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Post_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Post_templatePage.class.getName());
    /**
     * Generated in place of local variable 'src' from method
     * 'get_attachment_icon' because it is used inside an inner class.
     */
    Array<Object> get_attachment_icon_src = new Array<Object>();

    /**
     * Generated in place of local variable 'src_file' from method
     * 'get_attachment_icon' because it is used inside an inner class.
     */
    String get_attachment_icon_src_file = null;

    @Override
    @RequestMapping("/wp-includes/post-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/post_template";
    }

    //
	// "The Loop" post functions
	//
    public void the_ID() {
        echo(gVars.webEnv, gVars.id);
    }

    public Object get_the_ID() {
        return gVars.id;
    }

    public String the_title() {
        return the_title("", "", true);
    }

    public String the_title(String before) {
        return the_title(before, "", true);
    }

    public String the_title(String before, String after) {
        return the_title(before, after, true);
    }

    public String the_title(String before, String after, boolean echo) {
        String title = null;
        title = get_the_title(0);

        if (equal(Strings.strlen(title), 0)) {
            return null;
        }

        title = before + title + after;

        if (echo) {
            echo(gVars.webEnv, title);
        } else {
            return title;
        }

        return "";
    }

    public String the_title_attribute(Object args) {
        String title = null;
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Object before = null;
        Object after = null;
        Object echo = null;
        title = get_the_title(0);

        if (equal(Strings.strlen(title), 0)) {
            return null;
        }

        defaults = new Array<Object>(new ArrayEntry<Object>("before", ""), new ArrayEntry<Object>("after", ""), new ArrayEntry<Object>("echo", true));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        title = strval(Array.extractVar(r, "title", title, Array.EXTR_SKIP));
        before = Array.extractVar(r, "before", before, Array.EXTR_SKIP);
        after = Array.extractVar(r, "after", after, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);
        title = strval(before) + title + strval(after);
        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.strip_tags(title));

        if (booleanval(echo)) {
            echo(gVars.webEnv, title);
        } else {
            return title;
        }

        return "";
    }

    public String get_the_title(String id) {
        return get_the_title(intval(id));
    }

    public String get_the_title(int id) {
        StdClass post;
        String title = null;
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");
        title = strval(StdClass.getValue(post, "post_title"));

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
            if (!empty(StdClass.getValue(post, "post_password"))) {
                title = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Protected: %s", "default"), title);
            } else if (isset(StdClass.getValue(post, "post_status")) && equal("private", StdClass.getValue(post, "post_status"))) {
                title = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Private: %s", "default"), title);
            }
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", title));
    }

    public void the_guid(int id) {
        echo(gVars.webEnv, get_the_guid(id));
    }

    public String get_the_guid(int id) {
        StdClass post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_guid", StdClass.getValue(post, "guid")));
    }

    public void the_content() {
        the_content("(more...)", 0, "");
    }

    public void the_content(String more_link_text) {
        the_content(more_link_text, 0, "");
    }

    public void the_content(String more_link_text, int stripteaser) {
        the_content(more_link_text, stripteaser, "");
    }

    public void the_content(String more_link_text, int stripteaser, String more_file) {
        String content = null;
        content = get_the_content(more_link_text, stripteaser, more_file);
        content = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_content", content));
        content = Strings.str_replace("]]>", "]]&gt;", content);
        echo(gVars.webEnv, content);
    }

    public String get_the_content(String more_link_text, int stripteaser, String more_file) {
        String output = null;
        String file = null;
        Array content = new Array();
        Array matches = new Array();
        String teaser = null;
        output = "";

        if (!empty(StdClass.getValue(gVars.post, "post_password"))) { // if there's a password
            if (!isset(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH())) ||
                    !equal(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()))), StdClass.getValue(gVars.post, "post_password"))) {	// and it doesn't match the cookie
                output = get_the_password_form();

                return output;
            }
        }

        if (!equal(more_file, "")) {
            file = more_file;
        } else {
            file = gVars.pagenow; //$_SERVER['PHP_SELF'];
        }

        if (intval(gVars.page) > Array.count(gVars.pages)) { // if the requested page doesn't exist
            gVars.page = Array.count(gVars.pages); // give them the highest numbered page that DOES exist
        }

        String contentStr = gVars.pages.getValue(intval(gVars.page) - 1);

        if (QRegExPerl.preg_match("/<!--more(.*?)?-->/", contentStr, matches)) {
            content = Strings.explode(strval(matches.getValue(0)), contentStr, 2);

            if (!empty(matches.getValue(1)) && !empty(more_link_text)) {
                more_link_text = Strings.strip_tags(getIncluded(KsesPage.class, gVars, gConsts).wp_kses_no_null(Strings.trim(strval(matches.getValue(1)))));
            }
        } else {
            content = new Array<Object>(new ArrayEntry<Object>(contentStr));
        }

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(StdClass.getValue(gVars.post, "post_content")), "<!--noteaser-->")) && (!booleanval(gVars.multipage) || equal(gVars.page, 1))) {
            stripteaser = 1;
        }

        teaser = strval(content.getValue(0));

        if (booleanval(gVars.more) && booleanval(stripteaser)) {
            teaser = "";
        }

        output = output + teaser;

        if (Array.count(content) > 1) {
            if (booleanval(gVars.more)) {
                output = output + "<span id=\"more-" + strval(gVars.id) + "\"></span>" + strval(content.getValue(1));
            } else {
                output = getIncluded(FormattingPage.class, gVars, gConsts).balanceTags(output, false);

                if (!empty(more_link_text)) {
                    output = output + " <a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "#more-" + strval(gVars.id) + "\" class=\"more-link\">" +
                        more_link_text + "</a>";
                }
            }
        }

        if (booleanval(gVars.preview)) { // preview fix for javascript bug with foreign languages
            // Modified by Numiton
            output = RegExPerl.preg_replace_callback("/\\%u([0-9A-F]{4,4})/", new Callback("replaceBaseConvert", CallbackUtils.class), output);
        }

        return output;
    }

    public void the_excerpt() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_excerpt", get_the_excerpt("")));
    }

    public String get_the_excerpt(String deprecated) {
        String output = null;
        output = "";
        output = strval(StdClass.getValue(gVars.post, "post_excerpt"));

        if (!empty(StdClass.getValue(gVars.post, "post_password"))) { // if there's a password
            if (!isset(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH())) ||
                    !equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(gVars.post, "post_password"))) {  // and it doesn't match the cookie
                output = getIncluded(L10nPage.class, gVars, gConsts).__("There is no excerpt because this is a protected post.", "default");

                return output;
            }
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_the_excerpt", output));
    }

    public boolean has_excerpt(Object id) {
        StdClass post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");

        return !empty(StdClass.getValue(post, "post_excerpt"));
    }

    public Object wp_link_pages() {
        return wp_link_pages(new Array());
    }

    public Object wp_link_pages(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Object more_file = null;
        String file = null;
        Object output = null;
        Object next_or_number = null;
        Object before = null;
        String j;
        int i;
        String pagelink = null;
        Object after = null;
        Object previouspagelink = null;
        Object nextpagelink = null;
        Object echo = null;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("before", "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Pages:", "default")),
                new ArrayEntry<Object>("after", "</p>"),
                new ArrayEntry<Object>("next_or_number", "number"),
                new ArrayEntry<Object>("nextpagelink", getIncluded(L10nPage.class, gVars, gConsts).__("Next page", "default")),
                new ArrayEntry<Object>("previouspagelink", getIncluded(L10nPage.class, gVars, gConsts).__("Previous page", "default")),
                new ArrayEntry<Object>("pagelink", "%"),
                new ArrayEntry<Object>("more_file", ""),
                new ArrayEntry<Object>("echo", 1));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        more_file = Array.extractVar(r, "more_file", more_file, Array.EXTR_SKIP);
        next_or_number = Array.extractVar(r, "next_or_number", next_or_number, Array.EXTR_SKIP);
        before = Array.extractVar(r, "before", before, Array.EXTR_SKIP);
        pagelink = strval(Array.extractVar(r, "pagelink", pagelink, Array.EXTR_SKIP));
        after = Array.extractVar(r, "after", after, Array.EXTR_SKIP);
        previouspagelink = Array.extractVar(r, "previouspagelink", previouspagelink, Array.EXTR_SKIP);
        nextpagelink = Array.extractVar(r, "nextpagelink", nextpagelink, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);

        if (!equal(more_file, "")) {
            file = strval(more_file);
        } else {
            file = gVars.pagenow;
        }

        output = "";

        if (booleanval(gVars.multipage)) {
            if (equal("number", next_or_number)) {
                output = strval(output) + strval(before);

                for (i = 1; i < (gVars.numpages + 1); i = i + 1) {
                    j = Strings.str_replace("%", strval(i), pagelink);
                    output = strval(output) + " ";

                    if (!equal(i, gVars.page) || (!booleanval(gVars.more) && equal(gVars.page, 1))) {
                        if (equal(1, i)) {
                            output = strval(output) + "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "\">";
                        } else {
                            if (equal("", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure")) ||
                                    Array.in_array(StdClass.getValue(gVars.post, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
                                output = strval(output) + "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "&amp;page=" + strval(i) + "\">";
                            } else {
                                output = strval(output) + "<a href=\"" +
                                    getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false)) +
                                    getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(strval(i), "single_paged") + "\">";
                            }
                        }
                    }

                    output = strval(output) + j;

                    if (!equal(i, gVars.page) || (!booleanval(gVars.more) && equal(gVars.page, 1))) {
                        output = strval(output) + "</a>";
                    }
                }

                output = strval(output) + strval(after);
            } else {
                if (booleanval(gVars.more)) {
                    output = strval(output) + strval(before);
                    i = intval(gVars.page) - 1;

                    if (booleanval(i) && booleanval(gVars.more)) {
                        if (equal(1, i)) {
                            output = strval(output) + "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "\">" + strval(previouspagelink) + "</a>";
                        } else {
                            if (equal("", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure")) ||
                                    Array.in_array(StdClass.getValue(gVars.post, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
                                output = strval(output) + "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "&amp;page=" + strval(i) + "\">" +
                                    strval(previouspagelink) + "</a>";
                            } else {
                                output = strval(output) + "<a href=\"" +
                                    getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false)) +
                                    getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(strval(i), "single_paged") + "\">" + strval(previouspagelink) + "</a>";
                            }
                        }
                    }

                    i = intval(gVars.page) + 1;

                    if ((i <= gVars.numpages) && booleanval(gVars.more)) {
                        if (equal(1, i)) {
                            output = strval(output) + "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "\">" + strval(nextpagelink) + "</a>";
                        } else {
                            if (equal("", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure")) ||
                                    Array.in_array(StdClass.getValue(gVars.post, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
                                output = strval(output) + "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "&amp;page=" + strval(i) + "\">" +
                                    strval(nextpagelink) + "</a>";
                            } else {
                                output = strval(output) + "<a href=\"" +
                                    getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false)) +
                                    getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(strval(i), "single_paged") + "\">" + strval(nextpagelink) + "</a>";
                            }
                        }
                    }

                    output = strval(output) + strval(after);
                }
            }
        }

        if (booleanval(echo)) {
            echo(gVars.webEnv, output);
        }

        return output;
    }

    /**
     * Post-meta: Custom per-post fields.
     */
    public Object post_custom(Object key) {
        Array<Object> custom = new Array<Object>();
        custom = getIncluded(PostPage.class, gVars, gConsts).get_post_custom(0);

        if (equal(1, Array.count(custom.getValue(key)))) {
            return custom.getArrayValue(key).getValue(0);
        } else {
            return custom.getValue(key);
        }
    }

    /**
     * this will probably change at some point...
     */
    public void the_meta() {
        Array<Object> keys = null;
        String keyt = null;
        String key = null;
        Array values = new Array();
        String value = null;

        if (booleanval(keys = getIncluded(PostPage.class, gVars, gConsts).get_post_custom_keys(0))) {
            echo(gVars.webEnv, "<ul class=\'post-meta\'>\n");

            for (Map.Entry javaEntry523 : keys.entrySet()) {
                key = strval(javaEntry523.getValue());
                keyt = Strings.trim(key);

                if (equal("_", Strings.getCharAt(keyt, 0))) {
                    continue;
                }

                values = Array.array_map(new Callback("trim", Strings.class), (Array) getIncluded(PostPage.class, gVars, gConsts).get_post_custom_values(key, 0));
                value = Strings.implode(values, ", ");
                echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_meta_key", "<li><span class=\'post-meta-key\'>" + key + ":</span> " + value + "</li>\n", key, value));
            }

            echo(gVars.webEnv, "</ul>\n");
        }
    }

    /**
     * Pages
     */
    public Object wp_dropdown_pages(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Array<StdClass> pages;
        Object output = null;
        Object name = null;
        Object show_option_none = null;
        Object depth = null;
        Object echo = null;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("depth", 0),
                new ArrayEntry<Object>("child_of", 0),
                new ArrayEntry<Object>("selected", 0),
                new ArrayEntry<Object>("echo", 1),
                new ArrayEntry<Object>("name", "page_id"),
                new ArrayEntry<Object>("show_option_none", ""));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        name = Array.extractVar(r, "name", name, Array.EXTR_SKIP);
        show_option_none = Array.extractVar(r, "show_option_none", show_option_none, Array.EXTR_SKIP);
        depth = Array.extractVar(r, "depth", depth, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);
        pages = getIncluded(PostPage.class, gVars, gConsts).get_pages(r);
        output = "";

        if (!empty(pages)) {
            output = "<select name=\'" + strval(name) + "\'>\n";

            if (booleanval(show_option_none)) {
                output = strval(output) + "\t<option value=\'\'>" + strval(show_option_none) + "</option>\n";
            }

            output = strval(output) + walk_page_dropdown_tree(pages, depth, r);
            output = strval(output) + "</select>\n";
        }

        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_dropdown_pages", output);

        if (booleanval(echo)) {
            echo(gVars.webEnv, output);
        }

        return output;
    }

    public Object wp_list_pages(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = new Array<Object>();
        Object output = null;
        Object current_page = null;
        Array<StdClass> pages = new Array<StdClass>();
        
        defaults = new Array<Object>(new ArrayEntry<Object>("depth", 0), new ArrayEntry<Object>("show_date", ""),
                new ArrayEntry<Object>("date_format", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")), new ArrayEntry<Object>("child_of", 0),
                new ArrayEntry<Object>("exclude", ""), new ArrayEntry<Object>("title_li", getIncluded(L10nPage.class, gVars, gConsts).__("Pages", "default")), new ArrayEntry<Object>("echo", 1),
                new ArrayEntry<Object>("authors", ""), new ArrayEntry<Object>("sort_column", "menu_order, post_title"));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        
        output = "";
        current_page = 0;
        
    	// sanitize, mostly to keep spaces out
        r.putValue("exclude", QRegExPerl.preg_replace("[^0-9,]", "", strval(r.getValue("exclude"))));
        
        // Allow plugins to filter an array of excluded pages
        r.putValue(
            "exclude",
            Strings.implode(",", (Array<String>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_list_pages_excludes", Strings.explode(",", strval(r.getValue("exclude"))))));
        
        // Query pages.
        r.putValue("hierarchical", 0);
        pages = getIncluded(PostPage.class, gVars, gConsts).get_pages(r);

        if (!empty(pages)) {
            if (booleanval(r.getValue("title_li"))) {
                output = strval(output) + "<li class=\"pagenav\">" + strval(r.getValue("title_li")) + "<ul>";
            }

            if (getIncluded(QueryPage.class, gVars, gConsts).is_page("") || gVars.wp_query.is_posts_page) {
                current_page = gVars.wp_query.get_queried_object_id();
            }

            output = strval(output) + walk_page_tree(pages, r.getValue("depth"), current_page, r);

            if (booleanval(r.getValue("title_li"))) {
                output = strval(output) + "</ul></li>";
            }
        }

        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_list_pages", output);

        if (booleanval(r.getValue("echo"))) {
            echo(gVars.webEnv, output);
        } else {
            return output;
        }

        return "";
    }

    /**
     * Page helpers
     */
    public Object walk_page_tree(Object... vargs) {
        Ref<Walker_Page> walker = new Ref<Walker_Page>();
        Array<Object> args = new Array<Object>();
        walker.value = new Walker_Page(gVars, gConsts);

        // Modified by Numiton
        args = FunctionHandling.func_get_args(vargs);

        return FunctionHandling.call_user_func_array(new Callback("walk", walker), args);
    }

    public Object walk_page_dropdown_tree(Object... vargs) {
        Ref<Walker_PageDropdown> walker = new Ref<Walker_PageDropdown>();
        Array<Object> args = new Array<Object>();
        walker.value = new Walker_PageDropdown(gVars, gConsts);

        // Modified by Numiton
        args = FunctionHandling.func_get_args(vargs);

        return FunctionHandling.call_user_func_array(new Callback("walk", walker), args);
    }

    /**
     * Attachments
     */
    public void the_attachment_link(int id, boolean fullsize, Object deprecated, Object permalink) {
        if (fullsize) {
            echo(gVars.webEnv, wp_get_attachment_link(id, "full", booleanval(permalink), false));
        } else {
            echo(gVars.webEnv, wp_get_attachment_link(id, "thumbnail", booleanval(permalink), false));
        }
    }

    /**
     * get an attachment page link using an image or icon if possible
     */
    public String wp_get_attachment_link(int id, Object size, boolean permalink, boolean icon) {
        Ref<StdClass> _post = new Ref<StdClass>();
        String url = null;
        String post_title = null;
        String link_text = null;

        //		id = intval(id);
        _post.value = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");

        if (!equal("attachment", StdClass.getValue(_post.value, "post_type")) ||
                !booleanval(url = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(intval(StdClass.getValue(_post.value, "ID"))))) {
            return getIncluded(L10nPage.class, gVars, gConsts).__("Missing Attachment", "default");
        }

        if (permalink) {
            url = getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(intval(StdClass.getValue(_post.value, "ID")));
        }

        post_title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(_post.value, "post_title")));
        link_text = getIncluded(MediaPage.class, gVars, gConsts).wp_get_attachment_image(id, size, icon);

        if (!booleanval(link_text)) {
            link_text = strval(StdClass.getValue(_post.value, "post_title"));
        }

        return "<a href=\'" + url + "\' title=\'" + post_title + "\'>" + link_text + "</a>";
    }

    /**
     * deprecated - use wp_get_attachment_link()
     */
    public String get_the_attachment_link(int id, boolean fullsize, Array<Object> max_dims, Object permalink) {
        StdClass _post;
        String url = null;
        String post_title = null;
        String innerHTML = "";

        //		id = intval(id);
        _post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");

        if (!equal("attachment", StdClass.getValue(_post, "post_type")) ||
                !booleanval(url = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(intval(StdClass.getValue(_post, "ID"))))) {
            return getIncluded(L10nPage.class, gVars, gConsts).__("Missing Attachment", "default");
        }

        if (booleanval(permalink)) {
            url = getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(intval(StdClass.getValue(_post, "ID")));
        }

        post_title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(_post, "post_title")));
        innerHTML = get_attachment_innerHTML(intval(StdClass.getValue(_post, "ID")), fullsize, max_dims);

        return "<a href=\'" + url + "\' title=\'" + post_title + "\'>" + innerHTML + "</a>";
    }

    /**
     * deprecated: use wp_get_attachment_image_src()
     */
    public Array<Object> get_attachment_icon_src(int id, boolean fullsize) {
        StdClass post;
        Ref<String> file = new Ref<String>();
        String src = null;
        Object src_file = null;
        String _class = null;
        Object icon_dir = null;
        id = id;

        if (!booleanval(post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw"))) {
            return new Array<Object>();
        }

        file.value = strval(getIncluded(PostPage.class, gVars, gConsts).get_attached_file(intval(StdClass.getValue(post, "ID")), false));

        if (!fullsize && booleanval(src = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_thumb_url(intval(StdClass.getValue(post, "ID"))))) {
    		// We have a thumbnail desired, specified and existing
        	
            src_file = FileSystemOrSocket.basename(src);
            _class = "attachmentthumb";
        } else if (getIncluded(PostPage.class, gVars, gConsts).wp_attachment_is_image(intval(StdClass.getValue(post, "ID")))) {
        	// We have an image without a thumbnail
        	
            src = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(intval(StdClass.getValue(post, "ID")));
            src_file = file;
            _class = "attachmentimage";
        } else if (booleanval(src = getIncluded(PostPage.class, gVars, gConsts).wp_mime_type_icon(intval(StdClass.getValue(post, "ID"))))) {
        	// No thumb, no image. We'll look for a mime-related icon instead.
        	
            icon_dir = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("icon_dir", getIncluded(ThemePage.class, gVars, gConsts).get_template_directory() + "/images");
            src_file = strval(icon_dir) + "/" + FileSystemOrSocket.basename(src);
        }

        if (!isset(src) || !booleanval(src)) {
            return new Array<Object>();
        }

        return new Array<Object>(new ArrayEntry<Object>(src), new ArrayEntry<Object>(src_file));
    }

    /**
     * deprecated: use wp_get_attachment_image()
     */
    public String get_attachment_icon(int id, boolean fullsize, Array<Object> max_dims) {
        StdClass post;
        Array<Object> imagesize = new Array<Object>();
        float actual_aspect = 0;
        float desired_aspect = 0;
        float height = 0;
        String constraint = null;
        float width = 0;
        String post_title = null;
        String icon = null;
        id = id;

        if (!booleanval(post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw"))) {
            return "";
        }

        if (!booleanval(get_attachment_icon_src = get_attachment_icon_src(intval(StdClass.getValue(post, "ID")), fullsize))) {
            return "";
        }

        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    get_attachment_icon_src = (Array<Object>) srcArray.getValue(0);
                    get_attachment_icon_src_file = strval(srcArray.getValue(1));

                    return srcArray;
                }
            }.doAssign(get_attachment_icon_src);

        // Do we need to constrain the image?
        if (booleanval(max_dims = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attachment_max_dims", max_dims)) &&
                FileSystemOrSocket.file_exists(gVars.webEnv, get_attachment_icon_src_file)) {
            imagesize = QImage.getimagesize(gVars.webEnv, get_attachment_icon_src_file);

            if ((intval(imagesize.getValue(0)) > intval(max_dims.getValue(0))) || (intval(imagesize.getValue(1)) > intval(max_dims.getValue(1)))) {
                actual_aspect = floatval(imagesize.getValue(0)) / floatval(imagesize.getValue(1));
                desired_aspect = floatval(max_dims.getValue(0)) / floatval(max_dims.getValue(1));

                if (actual_aspect >= desired_aspect) {
                    height = actual_aspect * floatval(max_dims.getValue(0));
                    constraint = "width=\'" + strval(max_dims.getValue(0)) + "\' ";
                    post.fields.putValue("iconsize", new Array<Object>(new ArrayEntry<Object>(max_dims.getValue(0)), new ArrayEntry<Object>(height)));
                } else {
                    width = floatval(max_dims.getValue(1)) / actual_aspect;
                    constraint = "height=\'" + strval(max_dims.getValue(1)) + "\' ";
                    post.fields.putValue("iconsize", new Array<Object>(new ArrayEntry<Object>(width), new ArrayEntry<Object>(max_dims.getValue(1))));
                }
            } else {
                post.fields.putValue("iconsize", new Array<Object>(new ArrayEntry<Object>(imagesize.getValue(0)), new ArrayEntry<Object>(imagesize.getValue(1))));
                constraint = "";
            }
        } else {
            constraint = "";
        }

        post_title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(post, "post_title")));
        icon = "<img src=\'" + strval(get_attachment_icon_src) + "\' title=\'" + post_title + "\' alt=\'" + post_title + "\' " + constraint + "/>";

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attachment_icon", icon, StdClass.getValue(post, "ID")));
    }

    /**
     * deprecated: use wp_get_attachment_image()
     */
    public String get_attachment_innerHTML(int id, boolean fullsize, Array<Object> max_dims) {
        StdClass post;
        String innerHTML;
        id = id;

        if (!booleanval(post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw"))) {
            return "";
        }

        if (booleanval(innerHTML = get_attachment_icon(intval(StdClass.getValue(post, "ID")), fullsize, max_dims))) {
            return innerHTML;
        }

        innerHTML = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(post, "post_title")));

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attachment_innerHTML", innerHTML, StdClass.getValue(post, "ID")));
    }

    public Object prepend_attachment(Object content) {
        Object p = null;

        if (empty(StdClass.getValue(gVars.post, "post_type")) || !equal(StdClass.getValue(gVars.post, "post_type"), "attachment")) {
            return content;
        }

        p = "<p class=\"attachment\">";
    	// show the medium sized image representation of the attachment if available, and link to the raw file
        p = strval(p) + wp_get_attachment_link(0, "medium", false, false);
        p = strval(p) + "</p>";
        p = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("prepend_attachment", p);

        return p + "\n" + content;
    }

 //
 // Misc
 //

    public String get_the_password_form() {
        String label = null;
        String output = null;
        label = "pwbox-" + strval(empty(StdClass.getValue(gVars.post, "ID"))
                ? Math.rand()
                : StdClass.getValue(gVars.post, "ID"));
        output = "<form action=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-pass.php\" method=\"post\">\n\t<p>" +
            getIncluded(L10nPage.class, gVars, gConsts).__("This post is password protected. To view it please enter your password below:", "default") + "</p>\n\t<p><label for=\"" + label + "\">" +
            getIncluded(L10nPage.class, gVars, gConsts).__("Password:", "default") + " <input name=\"post_password\" id=\"" + label +
            "\" type=\"password\" size=\"20\" /></label> <input type=\"submit\" name=\"Submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Submit", "default") +
            "\" /></p>\n\t</form>\n\t";

        return output;
    }

    /**
     * is_page_template() - Determine wether or not we are in a page template
     * This template tag allows you to determine wether or not you are in a page
     * template. You can optional provide a template name and then the check
     * will be specific to that template.
     * @package Template Tags
     * @global object $wp_query
     * @param string $template The specific template name if specific matching
     * is required
     */
    public boolean is_page_template(Object template) {
        StdClass page = null;
        Array<Object> custom_fields = new Array<Object>();
        Object page_template = null;

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_page("")) {
            return false;
        }

        page = (StdClass) gVars.wp_query.get_queried_object();
        custom_fields = (Array<Object>) getIncluded(PostPage.class, gVars, gConsts).get_post_custom_values("_wp_page_template", intval(StdClass.getValue(page, "ID")));
        page_template = custom_fields.getValue(0);

    	// We have no argument passed so just see if a page_template has been specified
        if (empty(template)) {
            if (!empty(page_template)) {
                return true;
            }
        } else if (equal(template, page_template)) {
            return true;
        }

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
