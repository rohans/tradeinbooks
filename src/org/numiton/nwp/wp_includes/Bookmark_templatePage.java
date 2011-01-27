/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Bookmark_templatePage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Bookmark_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Bookmark_templatePage.class.getName());

    @Override
    @RequestMapping("/wp-includes/bookmark-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/bookmark_template";
    }

    /**
     * Bookmark Template Functions for usage in Themes
     *
     * @package WordPress
     * @subpackage Template
     */

    /**
     * _walk_bookmarks() - The formatted output of a list of bookmarks
     *
     * The $bookmarks array must contain bookmark objects and will be iterated over
     * to retrieve the bookmark to be used in the output.
     *
     * The output is formatted as HTML with no way to change that format. However, what
     * is between, before, and after can be changed. The link itself will be HTML.
     *
     * This function is used internally by wp_list_bookmarks() and should not be used by
     * themes.
     *
     * The defaults for overwriting are:
     * 'show_updated' - Default is 0 (integer). Will show the time of when the bookmark was last updated.
     * 'show_description' - Default is 0 (integer). Whether to show the description of the bookmark.
     * 'show_images' - Default is 1 (integer). Whether to show link image if available.
     * 'before' - Default is '<li>' (string). The html or text to prepend to each bookmarks.
     * 'after' - Default is '</li>' (string). The html or text to append to each bookmarks.
     * 'between' - Default is '\n' (string). The string for use in between the link, description, and image.
     * 'show_rating' - Default is 0 (integer). Whether to show the link rating.
     *
     * @since 2.1
     * @access private
     * @usedby wp_list_bookmarks()
     *
     * @param array $bookmarks List of bookmarks to traverse
     * @param string|array $args Optional. Overwrite the defaults.
     * @return string Formatted output in HTML
     */
    public Object _walk_bookmarks(Array<Object> bookmarks, Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Object output = null;
        StdClass bookmark = null;
        Object before = null;
        Object show_updated = null;
        String the_link = null;
        String rel = null;
        Object desc = null;
        Object name = null;
        Object title = null;
        Object alt = null;
        String target = null;
        Object show_images = null;
        Object show_description = null;
        Object between = null;
        Object show_rating = null;
        Object after = null;
        
        defaults = new Array<Object>(new ArrayEntry<Object>("show_updated", 0), new ArrayEntry<Object>("show_description", 0), new ArrayEntry<Object>("show_images", 1),
                new ArrayEntry<Object>("before", "<li>"), new ArrayEntry<Object>("after", "</li>"), new ArrayEntry<Object>("between", "\n"), new ArrayEntry<Object>("show_rating", 0));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        before = Array.extractVar(r, "before", before, Array.EXTR_SKIP);
        show_updated = Array.extractVar(r, "show_updated", show_updated, Array.EXTR_SKIP);
        show_images = Array.extractVar(r, "show_images", show_images, Array.EXTR_SKIP);
        show_description = Array.extractVar(r, "show_description", show_description, Array.EXTR_SKIP);
        between = Array.extractVar(r, "between", between, Array.EXTR_SKIP);
        show_rating = Array.extractVar(r, "show_rating", show_rating, Array.EXTR_SKIP);
        after = Array.extractVar(r, "after", after, Array.EXTR_SKIP);
        
        output = ""; // Blank string to start with.

        for (Map.Entry javaEntry378 : bookmarks.entrySet()) {
            bookmark = (StdClass) javaEntry378.getValue();

            if (!isset(StdClass.getValue(bookmark, "recently_updated"))) {
                bookmark.fields.putValue("recently_updated", false);
            }

            output = strval(output) + strval(before);

            if (booleanval(show_updated) && booleanval(StdClass.getValue(bookmark, "recently_updated"))) {
                output = strval(output) + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_recently_updated_prepend");
            }

            the_link = "#";

            if (!empty(StdClass.getValue(bookmark, "link_url"))) {
                the_link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(StdClass.getValue(bookmark, "link_url")), null, "display");
            }

            rel = strval(StdClass.getValue(bookmark, "link_rel"));

            if (!equal("", rel)) {
                rel = " rel=\"" + rel + "\"";
            }

            desc = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                    strval(getIncluded(BookmarkPage.class, gVars, gConsts)
                               .sanitize_bookmark_field("link_description", StdClass.getValue(bookmark, "link_description"), StdClass.getValue(bookmark, "link_id"), "display")));
            name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                    strval(getIncluded(BookmarkPage.class, gVars, gConsts)
                               .sanitize_bookmark_field("link_name", StdClass.getValue(bookmark, "link_name"), StdClass.getValue(bookmark, "link_id"), "display")));
            title = desc;

            if (booleanval(show_updated)) {
                if (!equal("00", Strings.substr(strval(StdClass.getValue(bookmark, "link_updated_f")), 0, 2))) {
                    title = strval(title) + " ";
                    title = strval(title) +
                        QStrings.sprintf(
                            getIncluded(L10nPage.class, gVars, gConsts).__("Last updated: %s", "default"),
                            DateTime.date(
                                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_updated_date_format")),
                                intval(intval(StdClass.getValue(bookmark, "link_updated_f")) + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600))));
                    title = strval(title) + ")";
                }
            }

            if (!equal("", title)) {
                title = " title=\"" + strval(title) + "\"";
            }

            alt = " alt=\"" + strval(name) + "\"";
            target = strval(StdClass.getValue(bookmark, "link_target"));

            if (!equal("", target)) {
                target = " target=\"" + target + "\"";
            }

            output = strval(output) + "<a href=\"" + the_link + "\"" + rel + strval(title) + target + ">";

            if (!equal(StdClass.getValue(bookmark, "link_image"), null) && booleanval(show_images)) {
                if (!strictEqual(Strings.strpos(strval(StdClass.getValue(bookmark, "link_image")), "http"), BOOLEAN_FALSE)) {
                    output = strval(output) + "<img src=\"" + StdClass.getValue(bookmark, "link_image") + "\" " + strval(alt) + " " + strval(title) + " />";
                } else { // If it's a relative path
                    output = strval(output) + "<img src=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + StdClass.getValue(bookmark, "link_image") + "\" " + strval(alt) +
                        " " + strval(title) + " />";
                }
            } else {
                output = strval(output) + strval(name);
            }

            output = strval(output) + "</a>";

            if (booleanval(show_updated) && booleanval(StdClass.getValue(bookmark, "recently_updated"))) {
                output = strval(output) + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_recently_updated_append");
            }

            if (booleanval(show_description) && !equal("", desc)) {
                output = strval(output) + strval(between) + strval(desc);
            }

            if (booleanval(show_rating)) {
                output = strval(output) + strval(between) + getIncluded(DeprecatedPage.class, gVars, gConsts).get_linkrating(bookmark);
            }

            output = strval(output) + strval(after) + "\n";
        } // end while

        return output;
    }

    public String wp_list_bookmarks() {
        return wp_list_bookmarks(new Array<Object>());
    }

    /**
     * wp_list_bookmarks() - Retrieve or echo all of the bookmarks
     *
     * List of default arguments are as follows:
     * 'orderby' - Default is 'name' (string). How to order the links by. String is based off of the bookmark scheme.
     * 'order' - Default is 'ASC' (string). Either 'ASC' or 'DESC'. Orders in either ascending or descending order.
     * 'limit' - Default is -1 (integer) or show all. The amount of bookmarks to display.
     * 'category' - Default is empty string (string). Include the links in what category ID(s).
     * 'category_name' - Default is empty string (string). Get links by category name.
     * 'hide_invisible' - Default is 1 (integer). Whether to show (default) or hide links marked as 'invisible'.
     * 'show_updated' - Default is 0 (integer). Will show the time of when the bookmark was last updated.
     * 'echo' - Default is 1 (integer). Whether to echo (default) or return the formatted bookmarks.
     * 'categorize' - Default is 1 (integer). Whether to show links listed by category (default) or show links in one column.
     *
     * These options define how the Category name will appear before the category links are displayed, if 'categorize' is 1.
     * If 'categorize' is 0, then it will display for only the 'title_li' string and only if 'title_li' is not empty.
     * 'title_li' - Default is 'Bookmarks' (translatable string). What to show before the links appear.
     * 'title_before' - Default is '<h2>' (string). The HTML or text to show before the 'title_li' string.
     * 'title_after' - Default is '</h2>' (string). The HTML or text to show after the 'title_li' string.
     * 'class' - Default is 'linkcat' (string). The CSS class to use for the 'title_li'.
     *
     * 'category_before' - Default is '<li id="%id" class="%class">'. String must contain '%id' and '%class' to get
     * the id of the category and the 'class' argument. These are used for formatting in themes. Argument will be displayed
     * before the 'title_before' argument.
     * 'category_after' - Default is '</li>' (string). The HTML or text that will appear after the list of links.
     *
     * These are only used if 'categorize' is set to 1 or true.
     * 'category_orderby' - Default is 'name'. How to order the bookmark category based on term scheme.
     * 'category_order' - Default is 'ASC'. Set the order by either ASC (ascending) or DESC (descending).
     *
     * @see _walk_bookmarks() For other arguments that can be set in this function and passed to _walk_bookmarks().
     * @see get_bookmarks() For other arguments that can be set in this function and passed to get_bookmarks().
     *
     * @since 2.1
     * @uses _list_bookmarks() Used to iterate over all of the bookmarks and return the html
     * @uses get_terms() Gets all of the categories that are for links.
     *
     * @param string|array $args Optional. Overwrite the defaults of the function
     * @return string|null Will only return if echo option is set to not echo. Default is not return anything.
     */
    public String wp_list_bookmarks(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        String output = null;
        Object categorize = null;
        Object cats = null;
        Object category_name = null;
        Object category = null;
        Object category_orderby = null;
        Object category_order = null;
        Array<Object> params = new Array<Object>();
        StdClass cat = null;
        Array<Object> bookmarks = null;
        String _class = null;
        String category_before = null;
        Object catname = null;
        Object title_before = null;
        Object title_after = null;
        Object category_after = null;
        Object title_li = null;
        Object echo = null;
        
        defaults = new Array<Object>(
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("limit", -1),
                new ArrayEntry<Object>("category", ""),
                new ArrayEntry<Object>("category_name", ""),
                new ArrayEntry<Object>("hide_invisible", 1),
                new ArrayEntry<Object>("show_updated", 0),
                new ArrayEntry<Object>("echo", 1),
                new ArrayEntry<Object>("categorize", 1),
                new ArrayEntry<Object>("title_li", getIncluded(L10nPage.class, gVars, gConsts).__("Bookmarks", "default")),
                new ArrayEntry<Object>("title_before", "<h2>"),
                new ArrayEntry<Object>("title_after", "</h2>"),
                new ArrayEntry<Object>("category_orderby", "name"),
                new ArrayEntry<Object>("category_order", "ASC"),
                new ArrayEntry<Object>("class", "linkcat"),
                new ArrayEntry<Object>("category_before", "<li id=\"%id\" class=\"%class\">"),
                new ArrayEntry<Object>("category_after", "</li>"));
        
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        categorize = Array.extractVar(r, "categorize", categorize, Array.EXTR_SKIP);
        category_name = Array.extractVar(r, "category_name", category_name, Array.EXTR_SKIP);
        category = Array.extractVar(r, "category", category, Array.EXTR_SKIP);
        category_orderby = Array.extractVar(r, "category_orderby", category_orderby, Array.EXTR_SKIP);
        category_order = Array.extractVar(r, "category_order", category_order, Array.EXTR_SKIP);
        _class = strval(Array.extractVar(r, "class", _class, Array.EXTR_SKIP));
        category_before = strval(Array.extractVar(r, "category_before", category_before, Array.EXTR_SKIP));
        title_before = Array.extractVar(r, "title_before", title_before, Array.EXTR_SKIP);
        title_after = Array.extractVar(r, "title_after", title_after, Array.EXTR_SKIP);
        category_after = Array.extractVar(r, "category_after", category_after, Array.EXTR_SKIP);
        title_li = Array.extractVar(r, "title_li", title_li, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);
        output = "";

        if (booleanval(categorize)) {
    		//Split the bookmarks into ul's for each category
            cats = getIncluded(TaxonomyPage.class, gVars, gConsts).get_terms(
                    "link_category",
                    "category_name=" + strval(category_name) + "&include=" + strval(category) + "&orderby=" + strval(category_orderby) + "&order=" + strval(category_order) + "&hierarchical=0");

            for (Map.Entry javaEntry379 : new Array<Object>(cats).entrySet()) {
                cat = (StdClass) javaEntry379.getValue();
                params = Array.array_merge(r, new Array<Object>(new ArrayEntry<Object>("category", StdClass.getValue(cat, "term_id"))));
                bookmarks = getIncluded(BookmarkPage.class, gVars, gConsts).get_bookmarks(params);

                if (empty(bookmarks)) {
                    continue;
                }

                output = output +
                    Strings.str_replace(new Array<String>(new ArrayEntry<String>("%id"), new ArrayEntry<String>("%class")),
                        new Array<String>(new ArrayEntry<String>("linkcat-" + StdClass.getValue(cat, "term_id")), new ArrayEntry<String>(_class)), category_before);
                catname = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("link_category", StdClass.getValue(cat, "name"));
                output = output + strval(title_before) + strval(catname) + strval(title_after) + "\n\t<ul>\n";
                output = output + strval(_walk_bookmarks(bookmarks, r));
                output = output + "\n\t</ul>\n" + strval(category_after) + "\n";
            }
        } else {
    		//output one single list using title_li for the title
            bookmarks = getIncluded(BookmarkPage.class, gVars, gConsts).get_bookmarks(r);

            if (!empty(bookmarks)) {
                if (!empty(title_li)) {
                    output = output +
                        Strings.str_replace(
                            new Array<String>(new ArrayEntry<String>("%id"), new ArrayEntry<String>("%class")),
                            new Array<String>(new ArrayEntry<String>("linkcat-" + strval(category)), new ArrayEntry<String>(_class)),
                            category_before);
                    output = output + strval(title_before) + strval(title_li) + strval(title_after) + "\n\t<ul>\n";
                    output = output + strval(_walk_bookmarks(bookmarks, r));
                    output = output + "\n\t</ul>\n" + strval(category_after) + "\n";
                } else {
                    output = output + strval(_walk_bookmarks(bookmarks, r));
                }
            }
        }

        output = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_list_bookmarks", output));

        if (!booleanval(echo)) {
            return output;
        }

        echo(gVars.webEnv, output);

        return "";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
