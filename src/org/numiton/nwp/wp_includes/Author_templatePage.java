/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Author_templatePage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Author_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Author_templatePage.class.getName());

    @Override
    @RequestMapping("/wp-includes/author-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/author_template";
    }

    /**
     * Author Template functions for use in themes.
     *
     * @package WordPress
     * @subpackage Template
     */

    /**
     * get_the_author() - Get the author of the current post in the Loop.
     *
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @uses apply_filters() Calls 'the_author' hook on the author display name.
     *
     * @param string $deprecated Deprecated.
     * @return string The author's display name.
     */
    public String get_the_author(String deprecated) {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_author", StdClass.getValue(gVars.authordata, "display_name")));
    }

    public String the_author() {
        return the_author("", true);
    }

    /**
     * the_author() - Echo the name of the author of the current post in the Loop.
     *
     * The behavior of this function is based off of old functionality predating get_the_author().
     * This function is not deprecated, but is designed to echo the value from get_the_author()
     * and as an result of any old theme that might still use the old behavior will also
     * pass the value from get_the_author().
     *
     * The normal, expected behavior of this function is to echo the author and not return it.
     * However, backwards compatiability has to be maintained.
     *
     * @since 0.71
     * @see get_the_author()
     *
     * @param string $deprecated Deprecated.
     * @param string $deprecated_echo Echo the string or return it. Deprecated, use get_the_author().
     * @return string The author's display name, from get_the_author().
     */
    public String the_author(String deprecated, boolean deprecated_echo) {
        if (deprecated_echo) {
            echo(gVars.webEnv, get_the_author(""));
        }

        return get_the_author("");
    }

    /**
     * get_the_author_description() - Get the description of the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's description.
     */
    public String get_the_author_description() {
        return strval(StdClass.getValue(gVars.authordata, "description"));
    }

    /**
     * the_author_description() - Echo the description of the author of the
     * current post in the Loop.
     * @since 1.0.0
     * @see get_the_author_description()
     */
    public void the_author_description() {
        echo(gVars.webEnv, get_the_author_description());
    }

    /**
     * get_the_author_login() - Get the login name of the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's login name (username).
     */
    public String get_the_author_login() {
        return strval(StdClass.getValue(gVars.authordata, "user_login"));
    }

    /**
     * the_author_login() - Echo the login name of the author of the current
     * post in the Loop.
     * @since 0.71
     * @see get_the_author_login()
     */
    public void the_author_login() {
        echo(gVars.webEnv, get_the_author_login());
    }

    /**
     * get_the_author_firstname() - Get the first name of the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's first name.
     */
    public String get_the_author_firstname() {
        return strval(StdClass.getValue(gVars.authordata, "first_name"));
    }

    /**
     * the_author_firstname() - Echo the first name of the author of the
     * current post in the Loop.
     * @since 0.71
     * @uses get_the_author_firstname()
     */
    public void the_author_firstname() {
        echo(gVars.webEnv, get_the_author_firstname());
    }

    /**
     * get_the_author_lastname() - Get the last name of the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's last name.
     */
    public String get_the_author_lastname() {
        return strval(StdClass.getValue(gVars.authordata, "last_name"));
    }

    /**
     * the_author_lastname() - Echo the last name of the author of the current
     * post in the Loop.
     * @since 0.71
     * @uses get_the_author_lastname()
     */
    public void the_author_lastname() {
        echo(gVars.webEnv, get_the_author_lastname());
    }

    /**
     * get_the_author_nickname() - Get the nickname of the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's nickname.
     */
    public String get_the_author_nickname() {
        return strval(StdClass.getValue(gVars.authordata, "nickname"));
    }

    /**
     * the_author_nickname() - Echo the nickname of the author of the current
     * post in the Loop.
     * @since 0.71
     * @uses get_the_author_nickname()
     */
    public void the_author_nickname() {
        echo(gVars.webEnv, get_the_author_nickname());
    }

    /**
     * get_the_author_ID() - Get the ID of the author of the current post in
     * the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return int The author's ID.
     */
    public int get_the_author_ID() {
        return intval(StdClass.getValue(gVars.authordata, "ID"));
    }

    /**
     * the_author_ID() - Echo the ID of the author of the current post in the
     * Loop.
     * @since 0.71
     * @uses get_the_author_ID()
     */
    public void the_author_ID() {
        echo(gVars.webEnv, get_the_author_ID());
    }

    /**
     * get_the_author_email() - Get the email of the author of the current
     * post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's username.
     */
    public String get_the_author_email() {
        return strval(StdClass.getValue(gVars.authordata, "user_email"));
    }

    /**
     * the_author_email() - Echo the email of the author of the current post
     * in the Loop.
     * @since 0.71
     * @uses get_the_author_email()
     */
    public void the_author_email() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_author_email", get_the_author_email()));
    }

    /**
     * get_the_author_url() - Get the URL to the home page of the author of
     * the current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The URL to the author's page.
     */
    public String get_the_author_url() {
        if (equal("http://", StdClass.getValue(gVars.authordata, "user_url"))) {
            return "";
        }

        return strval(StdClass.getValue(gVars.authordata, "user_url"));
    }

    /**
     * the_author_url() - Echo the URL to the home page of the author of the
     * current post in the Loop.
     * @since 0.71
     * @uses get_the_author_url()
     */
    public void the_author_url() {
        echo(gVars.webEnv, get_the_author_url());
    }

    /**
     * the_author_link() - If the author has a home page set, echo an HTML
     * link, otherwise just echo the author's name.
     * @since 2.1
     * @uses get_the_author_url()
     * @uses the_author()
     */
    public void the_author_link() {
        if (booleanval(get_the_author_url())) {
            echo(
                    gVars.webEnv,
                    "<a href=\"" + get_the_author_url() + "\" title=\"" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Visit %s\'s website", "default"), get_the_author("")) +
                    "\" rel=\"external\">" + get_the_author("") + "</a>");
        } else {
            the_author("", true);
        }
    }

    /**
     * get_the_author_icq() - Get the ICQ number of the author of the current
     * post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's ICQ number.
     */
    public String get_the_author_icq() {
        return strval(StdClass.getValue(gVars.authordata, "icq"));
    }

    /**
     * the_author_icq() - Echo the ICQ number of the author of the current
     * post in the Loop.
     * @since 0.71
     * @see get_the_author_icq()
     */
    public void the_author_icq() {
        echo(gVars.webEnv, get_the_author_icq());
    }

    /**
     * get_the_author_aim() - Get the AIM name of the author of the current
     * post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's AIM name.
     */
    public String get_the_author_aim() {
        return Strings.str_replace(" ", "+", strval(StdClass.getValue(gVars.authordata, "aim")));
    }

    /**
     * the_author_aim() - Echo the AIM name of the author of the current post
     * in the Loop.
     * @since 0.71
     * @see get_the_author_aim()
     */
    public void the_author_aim() {
        echo(gVars.webEnv, get_the_author_aim());
    }

    /**
     * get_the_author_yim() - Get the Yahoo! IM name of the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's Yahoo! IM name.
     */
    public String get_the_author_yim() {
        return strval(StdClass.getValue(gVars.authordata, "yim"));
    }

    /**
     * the_author_yim() - Echo the Yahoo! IM name of the author of the current
     * post in the Loop.
     * @since 0.71
     * @see get_the_author_yim()
     */
    public void the_author_yim() {
        echo(gVars.webEnv, get_the_author_yim());
    }

    /**
     * get_the_author_msn() - Get the MSN address of the author of the current
     * post in the Loop.
     * @since 1.5
     * @uses $authordata The current author's DB object.
     * @return string The author's MSN address.
     */
    public String get_the_author_msn() {
        return strval(StdClass.getValue(gVars.authordata, "msn"));
    }

    /**
     * the_author_msn() - Echo the MSN address of the author of the current
     * post in the Loop.
     * @since 0.71
     * @see get_the_author_msn()
     */
    public void the_author_msn() {
        echo(gVars.webEnv, get_the_author_msn());
    }

    /**
     * get_the_author_posts() - Get the number of posts by the author of the
     * current post in the Loop.
     * @since 1.5
     * @uses $post The current post in the Loop's DB object.
     * @uses get_usernumposts()
     * @return int The number of posts by the author.
     */
    public int get_the_author_posts() {
        return getIncluded(UserPage.class, gVars, gConsts).get_usernumposts(intval(StdClass.getValue(gVars.post, "post_author")));
    }

    /**
     * the_author_posts() - Echo the number of posts by the author of the
     * current post in the Loop.
     * @since 0.71
     * @uses get_the_author_posts() Echos returned value from function.
     */
    public void the_author_posts() {
        echo(gVars.webEnv, get_the_author_posts());
    }

    /**
     * the_author_posts_link() - Echo an HTML link to the author page of the
     * author of the current post in the Loop.
     * Does just echo get_author_posts_url() function, like the others do. The
     * reason for this, is that another function is used to help in printing the
     * link to the author's posts.
     * @since 1.2
     * @uses $authordata The current author's DB object.
     * @uses get_author_posts_url()
     * @uses get_the_author()
     * @param string $deprecated Deprecated.
     */
    public void the_author_posts_link(Object deprecated) {
        QStrings.printf(
            gVars.webEnv,
            "<a href=\"%1$s\" title=\"%2$s\">%3$s</a>",
            get_author_posts_url(intval(StdClass.getValue(gVars.authordata, "ID")), strval(StdClass.getValue(gVars.authordata, "user_nicename"))),
            QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Posts by %s", "default"), getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(get_the_author(""))),
            get_the_author(""));
    }

    /**
     * get_author_posts_url() - Get the URL to the author page of the author
     * of the current post in the Loop.
     * @since 2.1
     * @uses $wp_rewrite WP_Rewrite
     * @return string The URL to the author's page.
     */
    public String get_author_posts_url(int author_id, String author_nicename) {
        int auth_ID = 0;
        String link = null;
        String file = null;
        StdClass user;
        auth_ID = author_id;
        link = gVars.wp_rewrite.get_author_permastruct();

        if (empty(link)) {
            file = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/";
            link = file + "?author=" + strval(auth_ID);
        } else {
            if (equal("", author_nicename)) {
                user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(author_id);

                if (!empty(StdClass.getValue(user, "user_nicename"))) {
                    author_nicename = strval(StdClass.getValue(user, "user_nicename"));
                }
            }

            link = Strings.str_replace("%author%", author_nicename, link);
            link = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(link);
        }

        link = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("author_link", link, author_id, author_nicename));

        return link;
    }

    /**
     * get_author_name() - Get the specified author's preferred display name.
     * @since 1.0.0
     * @param int $auth_id The ID of the author.
     * @return string The author's display name.
     */
    public String get_author_name(int auth_id) {
        StdClass authordata;
        authordata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(auth_id);

        return strval(StdClass.getValue(authordata, "display_name"));
    }

    /**
     * wp_list_authors() - List all the authors of the blog, with several
     * options available.
     * optioncount (boolean) (false): Show the count in parenthesis next to the
     * author's name. exclude_admin (boolean) (true): Exclude the 'admin' user
     * that is installed by default. show_fullname (boolean) (false): Show their
     * full names. hide_empty (boolean) (true): Don't show authors without any
     * posts. feed (string) (''): If isn't empty, show links to author's feeds.
     * feed_image (string) (''): If isn't empty, use this image to link to
     * feeds. echo (boolean) (true): Set to false to return the output, instead
     * of echoing.
     * @since 1.2
     * @param array $args The argument array.
     * @return null|string The output, if echo is set to false.
     */
    public Object wp_list_authors(Array<Object> args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Object _return = null;
        Array<Object> authors = new Array<Object>();
        Object exclude_admin = null;
        Array<Object> author_count = new Array<Object>();
        StdClass row = null;
        StdClass author;
        Integer posts = null;
        String name = null;
        Object show_fullname = null;
        Object hide_empty = null;
        Object link = null;
        Object feed_image = null;
        Object feed = null;
        String title = null;
        String alt = null;
        Object optioncount = null;
        Object echo = null;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("optioncount", false),
                new ArrayEntry<Object>("exclude_admin", true),
                new ArrayEntry<Object>("show_fullname", false),
                new ArrayEntry<Object>("hide_empty", true),
                new ArrayEntry<Object>("feed", ""),
                new ArrayEntry<Object>("feed_image", ""),
                new ArrayEntry<Object>("feed_type", ""),
                new ArrayEntry<Object>("echo", true));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        exclude_admin = Array.extractVar(r, "exclude_admin", exclude_admin, Array.EXTR_SKIP);
        show_fullname = Array.extractVar(r, "show_fullname", show_fullname, Array.EXTR_SKIP);
        hide_empty = Array.extractVar(r, "hide_empty", hide_empty, Array.EXTR_SKIP);
        feed_image = Array.extractVar(r, "feed_image", feed_image, Array.EXTR_SKIP);
        feed = Array.extractVar(r, "feed", feed, Array.EXTR_SKIP);
        title = strval(Array.extractVar(r, "title", title, Array.EXTR_SKIP));
        alt = strval(Array.extractVar(r, "alt", alt, Array.EXTR_SKIP));
        optioncount = Array.extractVar(r, "optioncount", optioncount, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);
        _return = "";

        /** @todo Move select to get_authors(). */
        authors = gVars.wpdb.get_results("SELECT ID, user_nicename from " + gVars.wpdb.users + " " + (booleanval(exclude_admin)
                ? "WHERE user_login <> \'admin\' "
                : "") + "ORDER BY display_name");
        author_count = new Array<Object>();

        for (Map.Entry javaEntry376 : new Array<Object>(
                    gVars.wpdb.get_results(
                            "SELECT DISTINCT post_author, COUNT(ID) AS count FROM " + gVars.wpdb.posts + " WHERE post_type = \'post\' AND " +
                            getIncluded(PostPage.class, gVars, gConsts).get_private_posts_cap_sql("post") + " GROUP BY post_author")).entrySet()) {
            row = (StdClass) javaEntry376.getValue();
            author_count.putValue(StdClass.getValue(row, "post_author"), StdClass.getValue(row, "count"));
        }

        for (Map.Entry javaEntry377 : new Array<Object>(authors).entrySet()) {
            author = (StdClass) javaEntry377.getValue();
            author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(author, "ID")));
            posts = (isset(author_count.getValue(StdClass.getValue(author, "ID")))
                ? intval(author_count.getValue(StdClass.getValue(author, "ID")))
                : 0);
            name = strval(StdClass.getValue(author, "display_name"));

            if (booleanval(show_fullname) && !equal(StdClass.getValue(author, "first_name"), "") && !equal(StdClass.getValue(author, "last_name"), "")) {
                name = StdClass.getValue(author, "first_name") + " " + StdClass.getValue(author, "last_name");
            }

            if (!(equal(posts, 0) && booleanval(hide_empty))) {
                _return = strval(_return) + "<li>";
            }

            if (equal(posts, 0)) {
                if (!booleanval(hide_empty)) {
                    link = name;
                }
            } else {
                link = "<a href=\"" + get_author_posts_url(intval(StdClass.getValue(author, "ID")), strval(StdClass.getValue(author, "user_nicename"))) + "\" title=\"" +
                    QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Posts by %s", "default"),
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(author, "display_name")))) + "\">" + name + "</a>";

                if (!empty(feed_image) || !empty(feed)) {
                    link = strval(link) + " ";

                    if (empty(feed_image)) {
                        link = strval(link) + "(";
                    }

                    link = strval(link) + "<a href=\"" +
                        getIncluded(DeprecatedPage.class, gVars, gConsts).get_author_rss_link(booleanval(0), intval(StdClass.getValue(author, "ID")), StdClass.getValue(author, "user_nicename")) +
                        "\"";

                    if (!empty(feed)) {
                        title = " title=\"" + strval(feed) + "\"";
                        alt = " alt=\"" + strval(feed) + "\"";
                        name = strval(feed);
                        link = strval(link) + title;
                    }

                    link = strval(link) + ">";

                    if (!empty(feed_image)) {
                        link = strval(link) + "<img src=\"" + strval(feed_image) + "\" style=\"border: none;\"" + alt + title + " />";
                    } else {
                        link = strval(link) + name;
                    }

                    link = strval(link) + "</a>";

                    if (empty(feed_image)) {
                        link = strval(link) + ")";
                    }
                }

                if (booleanval(optioncount)) {
                    link = strval(link) + " (" + strval(posts) + ")";
                }
            }

            if (!(equal(posts, 0) && booleanval(hide_empty))) {
                _return = strval(_return) + strval(link) + "</li>";
            }
        }

        if (!booleanval(echo)) {
            return _return;
        }

        echo(gVars.webEnv, _return);

        return "";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
