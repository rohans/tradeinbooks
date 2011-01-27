/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Link_templatePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Link_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Link_templatePage.class.getName());

    @Override
    @RequestMapping("/wp-includes/link-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/link_template";
    }

    public void the_permalink() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_permalink", get_permalink(0, false)));
    }

    /**
     * Conditionally adds a trailing slash if the permalink structure has a
     * trailing slash, strips the trailing slash if not
     * @global object Uses $wp_rewrite
     * @param $string string a URL with or without a trailing slash
     * @param $type_of_url string the type of URL being considered (e.g. single,
     * category, etc) for use in the filter
     * @return string
     */
    public String user_trailingslashit(String string, String type_of_url) {
        if (gVars.wp_rewrite.use_trailing_slashes) {
            string = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(string);
        } else {
            string = getIncluded(FormattingPage.class, gVars, gConsts).untrailingslashit(string);
        }

        // Note that $type_of_url can be one of following:
        // single, single_trackback, single_feed, single_paged, feed, category, page, year, month, day, paged
        string = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("user_trailingslashit", string, type_of_url));

        return string;
    }

    public void permalink_anchor(String mode) {
        String title = null;

        {
            int javaSwitchSelector83 = 0;

            if (equal(Strings.strtolower(mode), "title")) {
                javaSwitchSelector83 = 1;
            }

            if (equal(Strings.strtolower(mode), "id")) {
                javaSwitchSelector83 = 2;
            }

            switch (javaSwitchSelector83) {
            case 1: {
                title = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(StdClass.getValue(gVars.post, "post_title")), "") + "-" + StdClass.getValue(gVars.post, "ID");
                echo(gVars.webEnv, "<a id=\"" + title + "\"></a>");

                break;
            }

            case 2: {
            }

            default: {
                echo(gVars.webEnv, "<a id=\"post-" + StdClass.getValue(gVars.post, "ID") + "\"></a>");

                break;
            }
            }
        }
    }

    public String get_permalink() {
        return get_permalink(0, false);
    }

    public String get_permalink(Object id) {
        return get_permalink(id, false);
    }

    public String get_permalink(Object id, boolean leavename) {
        Array<Object> rewritecode = new Array<Object>();
        StdClass post;
        String permalink = null;
        int unixtime = 0;
        Object category = null;
        Array<Object> cats = new Array<Object>();
        Object parent = null;
        Object default_category = null;
        String author = null;
        StdClass authordata;
        Array<String> date = new Array<String>();
        Array<Object> rewritereplace = new Array<Object>();
        rewritecode = new Array<Object>(
                new ArrayEntry<Object>("%year%"),
                new ArrayEntry<Object>("%monthnum%"),
                new ArrayEntry<Object>("%day%"),
                new ArrayEntry<Object>("%hour%"),
                new ArrayEntry<Object>("%minute%"),
                new ArrayEntry<Object>("%second%"),
                new ArrayEntry<Object>(leavename
                    ? ""
                    : "%postname%"),
                new ArrayEntry<Object>("%post_id%"),
                new ArrayEntry<Object>("%category%"),
                new ArrayEntry<Object>("%author%"),
                new ArrayEntry<Object>(leavename
                    ? ""
                    : "%pagename%"));
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");

        if (empty(StdClass.getValue(post, "ID"))) {
            return strval(false);
        }

        if (equal(StdClass.getValue(post, "post_type"), "page")) {
            return get_page_link(intval(StdClass.getValue(post, "ID")), leavename);
        } else if (equal(StdClass.getValue(post, "post_type"), "attachment")) {
            return get_attachment_link(intval(StdClass.getValue(post, "ID")));
        }

        permalink = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure"));

        if (!equal("", permalink) && !Array.in_array(StdClass.getValue(post, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
            unixtime = QDateTime.strtotime(strval(StdClass.getValue(post, "post_date")));
            category = "";

            if (!strictEqual(Strings.strpos(permalink, "%category%"), BOOLEAN_FALSE)) {
                cats = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(StdClass.getValue(post, "ID")));

                if (booleanval(cats)) {
                    Array.usort(cats, new Callback("_usort_terms_by_ID", getIncluded(Category_templatePage.class, gVars, gConsts))); // order by ID
                }

                category = ((StdClass) cats.getValue(0)).fields.getValue("slug");

                if (booleanval(parent = ((StdClass) cats.getValue(0)).fields.getValue("parent"))) {
                    category = getIncluded(Category_templatePage.class, gVars, gConsts).get_category_parents(parent, false, "/", true) + strval(category);
                }

                // show default category in permalinks, without
                // having to assign it explicitly
                if (empty(category)) {
                    default_category = getIncluded(CategoryPage.class, gVars, gConsts)
                                           .get_category(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category"), gConsts.getOBJECT(), "raw");
                    category = (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(default_category)
                        ? ""
                        : ((StdClass) default_category).fields.getValue("slug"));
                }
            }

            author = "";

            if (!strictEqual(Strings.strpos(permalink, "%author%"), BOOLEAN_FALSE)) {
                authordata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(post, "post_author")));
                author = strval(StdClass.getValue(authordata, "user_nicename"));
            }

            date = Strings.explode(" ", DateTime.date("Y m d H i s", unixtime));
            rewritereplace = new Array<Object>(
                    new ArrayEntry<Object>(date.getValue(0)),
                    new ArrayEntry<Object>(date.getValue(1)),
                    new ArrayEntry<Object>(date.getValue(2)),
                    new ArrayEntry<Object>(date.getValue(3)),
                    new ArrayEntry<Object>(date.getValue(4)),
                    new ArrayEntry<Object>(date.getValue(5)),
                    new ArrayEntry<Object>(StdClass.getValue(post, "post_name")),
                    new ArrayEntry<Object>(StdClass.getValue(post, "ID")),
                    new ArrayEntry<Object>(category),
                    new ArrayEntry<Object>(author),
                    new ArrayEntry<Object>(StdClass.getValue(post, "post_name")));
            permalink = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + Strings.str_replace(rewritecode, rewritereplace, permalink);
            permalink = user_trailingslashit(permalink, "single");

            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_link", permalink, post));
        } else { // if they're not using the fancy permalink option
            permalink = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?p=" + StdClass.getValue(post, "ID");

            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_link", permalink, post));
        }
    }

    /**
     * get permalink from post ID
     */
    public String post_permalink(Object post_id, String deprecated) {
        return get_permalink(post_id, false);
    }

    /**
     * Respects page_on_front. Use this one.
     */
    public String get_page_link(int id, boolean leavename) {
        String link = null;
        id = id;

        if (!booleanval(id)) {
            id = intval(StdClass.getValue(gVars.post, "ID"));
        }

        if (equal("page", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_on_front")) && equal(id, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("page_on_front"))) {
            link = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));
        } else {
            // Modified by Numiton. FIXME Why is this necessary?
            StdClass tmpPost = gVars.post;
            link = _get_page_link(id, leavename);
            gVars.post = tmpPost;
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("page_link", link, id));
    }

    /**
     * Ignores page_on_front. Internal use only.
     */
    public String _get_page_link(int id, boolean leavename) {
        String pagestruct = null;
        String link = null;

        if (!booleanval(id)) {
            id = intval(StdClass.getValue(gVars.post, "ID"));
        } else {
            gVars.post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");
        }

        pagestruct = gVars.wp_rewrite.get_page_permastruct();

        if (!equal("", pagestruct) && isset(StdClass.getValue(gVars.post, "post_status")) && !equal("draft", StdClass.getValue(gVars.post, "post_status"))) {
            link = getIncluded(PostPage.class, gVars, gConsts).get_page_uri(id);
            link = (leavename
                ? pagestruct
                : Strings.str_replace("%pagename%", link, pagestruct));
            link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/" + link;
            link = user_trailingslashit(link, "page");
        } else {
            link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?page_id=" + strval(id);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("_get_page_link", link, id));
    }

    public String get_attachment_link(int id) {
        String link = null;
        StdClass object = null;
        StdClass parent = null;
        String parentlink;
        String name = null;
        link = strval(false);

        if (!booleanval(id)) {
            id = intval(StdClass.getValue(gVars.post, "ID"));
        }

        object = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw");

        if (gVars.wp_rewrite.using_permalinks() && (intval(StdClass.getValue(object, "post_parent")) > 0) && !equal(StdClass.getValue(object, "post_parent"), id)) {
            parent = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(StdClass.getValue(object, "post_parent"), gConsts.getOBJECT(), "raw");

            if (equal("page", StdClass.getValue(parent, "post_type"))) {
                parentlink = _get_page_link(intval(StdClass.getValue(object, "post_parent")), false); // Ignores page_on_front
            } else {
                parentlink = get_permalink(StdClass.getValue(object, "post_parent"), false);
            }

            if (is_numeric(StdClass.getValue(object, "post_name")) ||
                    !strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure")), "%category%"))) {
                name = "attachment/" + StdClass.getValue(object, "post_name"); // <permalink>/<int>/ is paged so we use the explicit attachment marker
            } else {
                name = strval(StdClass.getValue(object, "post_name"));
            }

            if (strictEqual(Strings.strpos(parentlink, "?"), BOOLEAN_FALSE)) {
                link = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(parentlink) + name + "/";
            }
        }

        if (!booleanval(link)) {
            link = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "/?attachment_id=" + strval(id);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attachment_link", link, id));
    }

    public String get_year_link(String year) {
        String yearlink;

        if (!booleanval(year)) {
            year = DateTime.gmdate("Y", intval(DateTime.time() + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)));
        }

        yearlink = gVars.wp_rewrite.get_year_permastruct();

        if (!empty(yearlink)) {
            yearlink = Strings.str_replace("%year%", year, yearlink);

            return strval(
                getIncluded(PluginPage.class, gVars, gConsts)
                    .apply_filters("year_link", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + user_trailingslashit(yearlink, "year"), year));
        } else {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("year_link", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?m=" + year, year));
        }
    }

    public String get_month_link(String year, String month) {
        String monthlink;

        if (!booleanval(year)) {
            year = DateTime.gmdate("Y", intval(DateTime.time() + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)));
        }

        if (!booleanval(month)) {
            month = DateTime.gmdate("m", intval(DateTime.time() + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)));
        }

        monthlink = gVars.wp_rewrite.get_month_permastruct();

        if (!empty(monthlink)) {
            monthlink = Strings.str_replace("%year%", year, monthlink);
            monthlink = Strings.str_replace("%monthnum%", getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(month), 2), monthlink);

            return strval(
                getIncluded(PluginPage.class, gVars, gConsts)
                    .apply_filters("month_link", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + user_trailingslashit(monthlink, "month"), year, month));
        } else {
            return strval(
                getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                    "month_link",
                    getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?m=" + year + getIncluded(FormattingPage.class, gVars, gConsts).zeroise(month, 2),
                    year,
                    month));
        }
    }

    public String get_day_link(String year, String month, String day) {
        String daylink;

        if (!booleanval(year)) {
            year = DateTime.gmdate("Y", intval(DateTime.time() + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)));
        }

        if (!booleanval(month)) {
            month = DateTime.gmdate("m", intval(DateTime.time() + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)));
        }

        if (!booleanval(day)) {
            day = DateTime.gmdate("j", intval(DateTime.time() + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)));
        }

        daylink = gVars.wp_rewrite.get_day_permastruct();

        if (!empty(daylink)) {
            daylink = Strings.str_replace("%year%", year, daylink);
            daylink = Strings.str_replace("%monthnum%", getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(month), 2), daylink);
            daylink = Strings.str_replace("%day%", getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(day), 2), daylink);

            return strval(
                getIncluded(PluginPage.class, gVars, gConsts)
                    .apply_filters("day_link", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + user_trailingslashit(daylink, "day"), year, month, day));
        } else {
            return strval(
                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                            "day_link",
                            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?m=" + year + getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(month), 2) +
                            getIncluded(FormattingPage.class, gVars, gConsts).zeroise(intval(day), 2),
                            year,
                            month,
                            day));
        }
    }

    public Object get_feed_link(String feed) {
        String permalink;
        Object output = null;
        permalink = gVars.wp_rewrite.get_feed_permastruct();

        if (!equal("", permalink)) {
            if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(feed, "comments_"))) {
                feed = Strings.str_replace("comments_", "", feed);
                permalink = gVars.wp_rewrite.get_comment_feed_permastruct();
            }

            if (equal(getIncluded(FeedPage.class, gVars, gConsts).get_default_feed(), feed)) {
                feed = "";
            }

            permalink = Strings.str_replace("%feed%", feed, permalink);
            permalink = QRegExPerl.preg_replace("#/+#", "/", "/" + permalink);
            output = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + user_trailingslashit(permalink, "feed");
        } else {
            if (empty(feed)) {
                feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
            }

            if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(feed, "comments_"))) {
                feed = Strings.str_replace("comments_", "comments-", feed);
            }

            output = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?feed=" + feed;
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("feed_link", output, feed);
    }

    public String get_post_comments_feed_link() {
        return get_post_comments_feed_link(0, "");
    }

    public String get_post_comments_feed_link(int post_id) {
        return get_post_comments_feed_link(post_id, "");
    }

    public String get_post_comments_feed_link(int post_id, String feed) {
        String url = null;
        Object type = null;

        if (empty(post_id)) {
            post_id = intval(gVars.id);
        }

        if (empty(feed)) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        if (!equal("", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure"))) {
            url = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(get_permalink(post_id, false)) + "feed";

            if (!equal(feed, getIncluded(FeedPage.class, gVars, gConsts).get_default_feed())) {
                url = url + "/" + feed;
            }

            url = user_trailingslashit(url, "single_feed");
        } else {
            type = getIncluded(PostPage.class, gVars, gConsts).get_post_field("post_type", post_id, "display");

            if (equal("page", type)) {
                url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?feed=" + feed + "&amp;page_id=" + strval(post_id);
            } else {
                url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/?feed=" + feed + "&amp;p=" + strval(post_id);
            }
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_comments_feed_link", url));
    }

    public void post_comments_feed_link() {
        post_comments_feed_link("", 0, "");
    }

    public void post_comments_feed_link(String link_text) {
        post_comments_feed_link(link_text, 0, "");
    }

    public void post_comments_feed_link(String link_text, int post_id) {
        post_comments_feed_link(link_text, post_id, "");
    }

    /**
     * post_comments_feed_link() - Output the comment feed link for a post.
     * Prints out the comment feed link for a post. Link text is placed in the
     * anchor. If no link text is specified, default text is used. If no post ID
     * is specified, the current post is used.
     *
     * @subpackage Feed
     * @since 2.5
     * @param string Descriptive text
     * @param int Optional post ID. Default to current post.
     * @return string Link to the comment feed for the current post
     */
    public void post_comments_feed_link(String link_text, int post_id, String feed) {
        Object url = null;
        url = get_post_comments_feed_link(post_id, feed);

        if (empty(link_text)) {
            link_text = getIncluded(L10nPage.class, gVars, gConsts).__("Comments Feed", "default");
        }

        echo(gVars.webEnv, "<a href=\'" + strval(url) + "\'>" + link_text + "</a>");
    }

    public String get_author_feed_link(int author_id, String feed) {
        Object permalink_structure = null;
        String link = null;

        //		author_id = intval(author_id);
        permalink_structure = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure");

        if (empty(feed)) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        if (equal("", permalink_structure)) {
            link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "?feed=rss2&amp;author=" + strval(author_id);
        } else {
            link = getIncluded(Author_templatePage.class, gVars, gConsts).get_author_posts_url(author_id, "");
            link = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(link) + user_trailingslashit("feed", "feed");
        }

        link = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("author_feed_link", link));

        return link;
    }

    /**
     * get_category_feed_link() - Get the feed link for a given category
     * Returns a link to the feed for all post in a given category. A specific
     * feed can be requested or left blank to get the default feed.
     *
     * @subpackage Feed
     * @since 2.5
     * @param int $cat_id ID of a category
     * @param string $feed Feed type
     * @return string Link to the feed for the category specified by $cat_id
     */
    public String get_category_feed_link(int cat_id, String feed) {
        Object category = null;
        Object permalink_structure = null;
        String link = null;
        String feed_link = null;
        cat_id = cat_id;
        category = getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat_id, gConsts.getOBJECT(), "raw");

        if (empty(category) || getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(category)) {
            return "";
        }

        if (empty(feed)) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        permalink_structure = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure");

        if (equal("", permalink_structure)) {
            link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "?feed=" + feed + "&amp;cat=" + strval(cat_id);
        } else {
            link = strval(getIncluded(Category_templatePage.class, gVars, gConsts).get_category_link(cat_id));

            if (equal(feed, getIncluded(FeedPage.class, gVars, gConsts).get_default_feed())) {
                feed_link = "feed";
            } else {
                feed_link = "feed/" + feed;
            }

            link = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(link) + user_trailingslashit(feed_link, "feed");
        }

        link = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("category_feed_link", link, feed));

        return link;
    }

    public String get_tag_feed_link(int tag_id, Object feed) {
        Object tag = null;
        Object permalink_structure = null;
        String link = null;
        String feed_link = null;
        tag_id = tag_id;
        tag = getIncluded(CategoryPage.class, gVars, gConsts).get_tag(tag_id, gConsts.getOBJECT(), "raw");

        if (empty(tag) || getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(tag)) {
            return "";
        }

        permalink_structure = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure");

        if (empty(feed)) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        if (equal("", permalink_structure)) {
            link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "?feed=" + strval(feed) + "&amp;tag=" + ((StdClass) tag).fields.getValue("slug");
        } else {
            link = strval(getIncluded(Category_templatePage.class, gVars, gConsts).get_tag_link(((StdClass) tag).fields.getValue("term_id")));

            if (equal(feed, getIncluded(FeedPage.class, gVars, gConsts).get_default_feed())) {
                feed_link = "feed";
            } else {
                feed_link = "feed/" + strval(feed);
            }

            link = link + user_trailingslashit(feed_link, "feed");
        }

        link = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tag_feed_link", link, feed));

        return link;
    }

    public String get_search_feed_link(String search_query, String feed) {
        String search = null;
        String link = null;

        if (empty(search_query)) {
            search = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(General_templatePage.class, gVars, gConsts).get_search_query());
        } else {
            search = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, search_query));
        }

        if (empty(feed)) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "?s=" + search + "&amp;feed=" + feed;
        link = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("search_feed_link", link));

        return link;
    }

    public String get_search_comments_feed_link(String search_query, String feed) {
        String search = null;
        String link = null;

        if (empty(search_query)) {
            search = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(General_templatePage.class, gVars, gConsts).get_search_query());
        } else {
            search = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, search_query));
        }

        if (empty(feed)) {
            feed = getIncluded(FeedPage.class, gVars, gConsts).get_default_feed();
        }

        link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "?s=" + search + "&amp;feed=comments-" + feed;
        link = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("search_feed_link", link));

        return link;
    }

    public Object get_edit_post_link(Object id) {
        StdClass post;
        String file = null;
        String var = null;

        if (!booleanval(post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(id, gConsts.getOBJECT(), "raw"))) {
            return null;
        }

        {
            int javaSwitchSelector84 = 0;

            if (equal(StdClass.getValue(post, "post_type"), "page")) {
                javaSwitchSelector84 = 1;
            }

            if (equal(StdClass.getValue(post, "post_type"), "attachment")) {
                javaSwitchSelector84 = 2;
            }

            switch (javaSwitchSelector84) {
            case 1: {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", StdClass.getValue(post, "ID"))) {
                    return null;
                }

                file = "page";
                var = "post";

                break;
            }

            case 2: {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(post, "ID"))) {
                    return null;
                }

                file = "media";
                var = "attachment_id";

                break;
            }

            default: {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(post, "ID"))) {
                    return null;
                }

                file = "post";
                var = "post";

                break;
            }
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
            "get_edit_post_link",
            getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("wpurl", "raw") + "/wp-admin/" + file + ".php?action=edit&amp;" + var + "=" + StdClass.getValue(post, "ID"),
            StdClass.getValue(post, "ID"));
    }

    public void edit_post_link(String link) {
        edit_post_link(link, "", "");
    }

    public void edit_post_link(String link, String before, String after) {
        if (equal(StdClass.getValue(gVars.post, "post_type"), "page")) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", StdClass.getValue(gVars.post, "ID"))) {
                return;
            }
        } else {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
                return;
            }
        }

        link = "<a href=\"" + get_edit_post_link(StdClass.getValue(gVars.post, "ID")) + "\" title=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Edit post", "default") + "\">" + link + "</a>";
        echo(gVars.webEnv, before + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_post_link", link, StdClass.getValue(gVars.post, "ID")) + after);
    }

    public String get_edit_comment_link(Object comment_id) {
        StdClass comment;
        StdClass post;
        String location = null;
        comment = (StdClass) getIncluded(CommentPage.class, gVars, gConsts).get_comment(comment_id, gConsts.getOBJECT());
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(StdClass.getValue(comment, "comment_post_ID"), gConsts.getOBJECT(), "raw");

        if (equal(StdClass.getValue(post, "post_type"), "page")) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", StdClass.getValue(post, "ID"))) {
                return null;
            }
        } else {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(post, "ID"))) {
                return null;
            }
        }

        location = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("wpurl", "raw") + "/wp-admin/comment.php?action=editcomment&amp;c=" + StdClass.getValue(comment, "comment_ID");

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_edit_comment_link", location));
    }

    public void edit_comment_link() {
        edit_comment_link("Edit This", "", "");
    }

    public void edit_comment_link(String link) {
        edit_comment_link(link, "", "");
    }

    public void edit_comment_link(String link, String before) {
        edit_comment_link(link, before, "");
    }

    public void edit_comment_link(String link, String before, String after) {
        if (equal(StdClass.getValue(gVars.post, "post_type"), "attachment")) {
        } else if (equal(StdClass.getValue(gVars.post, "post_type"), "page")) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", StdClass.getValue(gVars.post, "ID"))) {
                return;
            }
        } else {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", StdClass.getValue(gVars.post, "ID"))) {
                return;
            }
        }

        link = "<a href=\"" + get_edit_comment_link(intval(StdClass.getValue(gVars.comment, "comment_ID"))) + "\" title=\"" +
            getIncluded(L10nPage.class, gVars, gConsts).__("Edit comment", "default") + "\">" + link + "</a>";
        echo(gVars.webEnv, before + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_comment_link", link, intval(StdClass.getValue(gVars.comment, "comment_ID"))) + after);
    }

    /**
     * Navigation links
     */
    public StdClass get_previous_post(boolean in_same_cat, String excluded_categories) {
        return get_adjacent_post(in_same_cat, excluded_categories, true);
    }

    public StdClass get_next_post(boolean in_same_cat, String excluded_categories) {
        return get_adjacent_post(in_same_cat, excluded_categories, false);
    }

    public StdClass get_adjacent_post(boolean in_same_cat, String excluded_categories, boolean previous) {
        Object excluded_categoriesObj = excluded_categories;
        Object current_post_date = null;
        String join = null;
        String posts_in_ex_cats_sql = null;
        Array<String> cat_array = null;
        String adjacent = null;
        String op = null;
        String order = null;
        Object where = null;
        Object sort = null;

        if (empty(gVars.post) || !getIncluded(QueryPage.class, gVars, gConsts).is_single() || getIncluded(QueryPage.class, gVars, gConsts).is_attachment()) {
            return null;
        }

        current_post_date = StdClass.getValue(gVars.post, "post_date");
        join = "";
        posts_in_ex_cats_sql = "";

        if (in_same_cat || !empty(excluded_categoriesObj)) {
            join = " INNER JOIN " + gVars.wpdb.term_relationships + " AS tr ON p.ID = tr.object_id INNER JOIN " + gVars.wpdb.term_taxonomy + " tt ON tr.term_taxonomy_id = tt.term_taxonomy_id";

            if (in_same_cat) {
                cat_array = (Array<String>) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(StdClass.getValue(gVars.post, "ID"), "category", "fields=ids");
                join = join + " AND tt.taxonomy = \'category\' AND tt.term_id IN (" + Strings.implode(cat_array, ",") + ")";
            }

            posts_in_ex_cats_sql = "AND tt.taxonomy = \'category\'";

            if (!empty(excluded_categoriesObj)) {
                excluded_categoriesObj = Array.array_map(new Callback("intval", VarHandling.class), Strings.explode(" and ", strval(excluded_categoriesObj)));

                if (!empty(cat_array)) {
                    excluded_categoriesObj = Array.array_diff((Array) excluded_categoriesObj, cat_array);
                    posts_in_ex_cats_sql = "";
                }

                if (!empty(excluded_categoriesObj)) {
                    posts_in_ex_cats_sql = " AND tt.taxonomy = \'category\' AND tt.term_id NOT IN (" + Strings.implode((Array) excluded_categoriesObj, ",") + ")";
                }
            }
        }

        adjacent = (previous
            ? "previous"
            : "next");
        op = (previous
            ? "<"
            : ">");
        order = (previous
            ? "DESC"
            : "ASC");
        join = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_" + adjacent + "_post_join", join, in_same_cat, excluded_categoriesObj));
        where = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "get_" + adjacent + "_post_where",
                gVars.wpdb.prepare("WHERE p.post_date " + op + " %s AND p.post_type = \'post\' AND p.post_status = \'publish\' " + posts_in_ex_cats_sql, current_post_date),
                in_same_cat,
                excluded_categoriesObj);
        sort = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_" + adjacent + "_post_sort", "ORDER BY p.post_date " + order + " LIMIT 1");

        return (StdClass) gVars.wpdb.get_row("SELECT p.* FROM " + gVars.wpdb.posts + " AS p " + join + " " + where + " " + sort);
    }

    public void previous_post_link(String format) {
        previous_post_link(format, "%title", false, "");
    }

    public void previous_post_link(String format, String link, boolean in_same_cat, String excluded_categories) {
        adjacent_post_link(format, link, in_same_cat, excluded_categories, true);
    }

    public void next_post_link(String format) {
        next_post_link(format, "%title", false, "");
    }

    public void next_post_link(String format, String link, boolean in_same_cat, String excluded_categories) {
        adjacent_post_link(format, link, in_same_cat, excluded_categories, false);
    }

    public void adjacent_post_link(String format, String link, boolean in_same_cat, String excluded_categories, boolean previous) {
        StdClass post;
        String title = null;
        String string = null;

        if (previous && getIncluded(QueryPage.class, gVars, gConsts).is_attachment()) {
            post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(StdClass.getValue(gVars.post, "post_parent"), gConsts.getOBJECT(), "raw");
        } else {
            post = get_adjacent_post(in_same_cat, excluded_categories, previous);
        }

        if (!booleanval(post)) {
            return;
        }

        title = strval(StdClass.getValue(post, "post_title"));

        if (empty(StdClass.getValue(post, "post_title"))) {
            title = (previous
                ? getIncluded(L10nPage.class, gVars, gConsts).__("Previous Post", "default")
                : getIncluded(L10nPage.class, gVars, gConsts).__("Next Post", "default"));
        }

        title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", title, post));
        string = "<a href=\"" + get_permalink(post, false) + "\">";
        link = Strings.str_replace("%title", title, link);
        link = string + link + "</a>";
        format = Strings.str_replace("%link", link, format);
        echo(gVars.webEnv, format);
    }

    public String get_pagenum_link(int pagenum) {
        String request = null;
        String home_rootStr = null;
        Array<String> home_root;
        String base = null;
        String result = null;
        String qs_regex = null;
        Array<Object> qs_match = new Array<Object>();
        String query_string = null;
        pagenum = pagenum;
        request = getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("paged");
        home_root = URL.parse_url(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));
        home_rootStr = (isset(home_root.getValue("path"))
            ? home_root.getValue("path")
            : "");
        home_rootStr = RegExPerl.preg_quote(getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(home_rootStr), "|");
        request = QRegExPerl.preg_replace("|^" + home_rootStr + "|", "", request);
        request = QRegExPerl.preg_replace("|^/+|", "", request);

        if (!gVars.wp_rewrite.using_permalinks() || getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
            base = getIncluded(FormattingPage.class, gVars, gConsts)
                       .trailingslashit((((General_templatePage) PhpWeb.getIncluded(General_templatePage.class, gVars, gConsts))).get_bloginfo("home", "raw"));

            if (pagenum > 1) {
                result = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("paged", pagenum, base + request);
            } else {
                result = base + request;
            }
        } else {
            qs_regex = "|\\?.*?$|";
            QRegExPerl.preg_match(qs_regex, request, qs_match);

            if (!empty(qs_match.getValue(0))) {
                query_string = strval(qs_match.getValue(0));
                request = QRegExPerl.preg_replace(qs_regex, "", request);
            } else {
                query_string = "";
            }

            request = QRegExPerl.preg_replace("|page/\\d+/?$|", "", request);
            request = QRegExPerl.preg_replace("|^index\\.php|", "", request);
            request = Strings.ltrim(request, "/");
            base = getIncluded(FormattingPage.class, gVars, gConsts)
                       .trailingslashit((((General_templatePage) PhpWeb.getIncluded(General_templatePage.class, gVars, gConsts))).get_bloginfo("url", "raw"));

            if (gVars.wp_rewrite.using_index_permalinks() && ((pagenum > 1) || !equal("", request))) {
                base = base + "index.php/";
            }

            if (pagenum > 1) {
                request = ((!empty(request))
                    ? getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(request)
                    : request) + user_trailingslashit("page/" + strval(pagenum), "paged");
            }

            result = base + request + query_string;
        }

        result = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_pagenum_link", result));

        return result;
    }

    public String get_next_posts_page_link(int max_page) {
        int nextpage = 0;

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
            if (!booleanval(gVars.paged)) {
                gVars.paged = 1;
            }

            nextpage = gVars.paged + 1;

            if (!booleanval(max_page) || (max_page >= nextpage)) {
                return get_pagenum_link(nextpage);
            }
        }

        return "";
    }

    public void next_posts(int max_page) {
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).clean_url(get_next_posts_page_link(max_page), null, "display"));
    }

    public void next_posts_link(String label, int max_page) {
        int nextpage = 0;

        if (!booleanval(max_page)) {
            max_page = gVars.wp_query.max_num_pages;
        }

        if (!booleanval(gVars.paged)) {
            gVars.paged = 1;
        }

        nextpage = gVars.paged + 1;

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_single() && (empty(gVars.paged) || (nextpage <= max_page))) {
            echo(gVars.webEnv, "<a href=\"");
            next_posts(max_page);
            echo(gVars.webEnv, "\">" + QRegExPerl.preg_replace("/&([^#])(?![a-z]{1,8};)/", "&#038;$1", label) + "</a>");
        }
    }

    public String get_previous_posts_page_link() {
        int nextpage = 0;

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
            nextpage = gVars.paged - 1;

            if (nextpage < 1) {
                nextpage = 1;
            }

            return get_pagenum_link(nextpage);
        }

        return "";
    }

    public void previous_posts() {
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).clean_url(get_previous_posts_page_link(), null, "display"));
    }

    public void previous_posts_link(String label) {
        if (!getIncluded(QueryPage.class, gVars, gConsts).is_single() && (gVars.paged > 1)) {
            echo(gVars.webEnv, "<a href=\"");
            previous_posts();
            echo(gVars.webEnv, "\">" + QRegExPerl.preg_replace("/&([^#])(?![a-z]{1,8};)/", "&#038;$1", label) + "</a>");
        }
    }

    public void posts_nav_link(String sep, String prelabel, String nxtlabel) {
        Object max_num_pages = null;
        Object paged = null;

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_singular()) {
            max_num_pages = gVars.wp_query.max_num_pages;
            paged = getIncluded(QueryPage.class, gVars, gConsts).get_query_var("paged");

    		//only have sep if there's both prev and next results
            if ((intval(paged) < 2) || (intval(paged) >= intval(max_num_pages))) {
                sep = "";
            }

            if (intval(max_num_pages) > 1) {
                previous_posts_link(prelabel);
                echo(gVars.webEnv, QRegExPerl.preg_replace("/&([^#])(?![a-z]{1,8};)/", "&#038;$1", sep));
                next_posts_link(nxtlabel, 0);
            }
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
