/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Comment_templatePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
import org.numiton.nwp.wp_content.themes._default.CommentsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.Ref;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Comment_templatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Comment_templatePage.class.getName());
    public Object wpcommentspopupfile;
    public Object wpcommentsjavascript;

    @Override
    @RequestMapping("/wp-includes/comment-template.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/comment_template";
    }

    /**
     * Comment template functions
     *
     * These functions are meant to live inside of the WordPress loop.
     *
     * @package WordPress
     * @subpackage Template
     */

    /**
     * get_comment_author() - Retrieve the author of the current comment
     *
     * If the comment has an empty comment_author field, then 'Anonymous' person
     * is assumed.
     *
     * @since 1.5
     * @uses apply_filters() Calls 'get_comment_author' hook on the comment author
     *
     * @return string The comment author
     */
    public String get_comment_author() {
        String author = null;

        if (empty(StdClass.getValue(gVars.comment, "comment_author"))) {
            author = getIncluded(L10nPage.class, gVars, gConsts).__("Anonymous", "default");
        } else {
            author = strval(StdClass.getValue(gVars.comment, "comment_author"));
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_author", author));
    }

    /**
     * comment_author() - Displays the author of the current comment
     * @since 0.71
     * @uses apply_filters() Calls 'comment_author' on comment author before
     * displaying
     */
    public void comment_author() {
        Object author = null;
        author = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_author", get_comment_author());
        echo(gVars.webEnv, author);
    }

    /**
     * get_comment_author_email() - Retrieve the email of the author of the
     * current comment
     * @since 1.5
     * @uses apply_filters() Calls the 'get_comment_author_email' hook on the
     * comment author email
     * @uses $comment
     * @return string The current comment author's email
     */
    public String get_comment_author_email() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_author_email", StdClass.getValue(gVars.comment, "comment_author_email")));
    }

    /**
     * comment_author_email() - Display the email of the author of the current
     * global $comment
     * Care should be taken to protect the email address and assure that email
     * harvesters do not capture your commentors' email address. Most assume
     * that their email address will not appear in raw form on the blog. Doing
     * so will enable anyone, including those that people don't want to get the
     * email address and use it for their own means good and bad.
     * @since 0.71
     * @uses apply_filters() Calls 'author_email' hook on the author email
     */
    public void comment_author_email() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("author_email", get_comment_author_email()));
    }

    public void comment_author_email_link() {
        comment_author_email_link("", "", "");
    }

    public void comment_author_email_link(String linktext) {
        comment_author_email_link(linktext, "", "");
    }

    public void comment_author_email_link(String linktext, String before) {
        comment_author_email_link(linktext, before, "");
    }

    /**
     * comment_author_email_link() - Display the html email link to the author
     * of the current comment
     * Care should be taken to protect the email address and assure that email
     * harvesters do not capture your commentors' email address. Most assume
     * that their email address will not appear in raw form on the blog. Doing
     * so will enable anyone, including those that people don't want to get the
     * email address and use it for their own means good and bad.
     * @since 0.71
     * @uses apply_filters() Calls 'comment_email' hook for the display of the
     * comment author's email
     * @global object $comment The current Comment row object
     * @param string $linktext The text to display instead of the comment
     * author's email address
     * @param string $before The text or HTML to display before the email link.
     * @param string $after The text or HTML to display after the email link.
     */
    public void comment_author_email_link(String linktext, String before, String after) {
        Object email = null;
        Object display = null;
        email = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_email", StdClass.getValue(gVars.comment, "comment_author_email"));

        if (!empty(email) && !equal(email, "@")) {
            display = ((!equal(linktext, ""))
                ? linktext
                : strval(email));
            echo(gVars.webEnv, before);
            echo(gVars.webEnv, "<a href=\'mailto:" + strval(email) + "\'>" + strval(display) + "</a>");
            echo(gVars.webEnv, after);
        }
    }

    /**
     * get_comment_author_link() - Retrieve the html link to the url of the
     * author of the current comment
     * @since 1.5
     * @uses apply_filters() Calls 'get_comment_author_link' hook on the
     * complete link HTML or author
     * @return string Comment Author name or HTML link for author's URL
     */
    public String get_comment_author_link() {
        String url = null;
        String author = null;
        String _return = null;
        
    	/** @todo Only call these functions when they are needed. Include in if... else blocks */
        url = get_comment_author_url();
        author = get_comment_author();

        if (empty(url) || equal("http://", url)) {
            _return = author;
        } else {
            _return = "<a href=\'" + url + "\' rel=\'external nofollow\'>" + author + "</a>";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_author_link", _return));
    }

    /**
     * comment_author_link() - Display the html link to the url of the author
     * of the current comment
     * @since 0.71
     * @see get_comment_author_link() Echos result
     */
    public void comment_author_link() {
        echo(gVars.webEnv, get_comment_author_link());
    }

    /**
     * get_comment_author_IP() - Retrieve the IP address of the author of the
     * current comment
     * @since 1.5
     * @uses $comment
     * @uses apply_filters()
     * @return unknown
     */
    public String get_comment_author_IP() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_author_IP", StdClass.getValue(gVars.comment, "comment_author_IP")));
    }

    /**
     * comment_author_IP() - Displays the IP address of the author of the
     * current comment
     * @since 0.71
     * @see get_comment_author_IP() Echos Result
     */
    public void comment_author_IP() {
        echo(gVars.webEnv, get_comment_author_IP());
    }

    /**
     * get_comment_author_url() - Returns the url of the author of the current
     * comment
     * @since 1.5
     * @uses apply_filters() Calls 'get_comment_author_url' hook on the comment
     * author's URL
     * @return string
     */
    public String get_comment_author_url() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_author_url", StdClass.getValue(gVars.comment, "comment_author_url")));
    }

    /**
     * comment_author_url() - Display the url of the author of the current
     * comment
     * @since 0.71
     * @uses apply_filters()
     * @uses get_comment_author_url() Retrieves the comment author's URL
     */
    public void comment_author_url() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_url", get_comment_author_url()));
    }

    public void get_comment_author_url_link() {
        get_comment_author_url_link("", "", "");
    }

    public void get_comment_author_url_link(String linktext) {
        get_comment_author_url_link(linktext, "", "");
    }

    public void get_comment_author_url_link(String linktext, String before) {
        get_comment_author_url_link(linktext, before, "");
    }

    /**
     * get_comment_author_url_link() - Retrieves the HTML link of the url of
     * the author of the current comment
     * $linktext parameter is only used if the URL does not exist for the
     * comment author. If the URL does exist then the URL will be used and the
     * $linktext will be ignored.
     * Encapsulate the HTML link between the $before and $after. So it will
     * appear in the order of $before, link, and finally $after.
     * @since 1.5
     * @uses apply_filters() Calls the 'get_comment_author_url_link' on the
     * complete HTML before returning.
     * @param string $linktext The text to display instead of the comment
     * author's email address
     * @param string $before The text or HTML to display before the email link.
     * @param string $after The text or HTML to display after the email link.
     * @return string The HTML link between the $before and $after parameters
     */
    public String get_comment_author_url_link(String linktext, String before, String after) {
        String url = null;
        String display = null;
        String _return = null;
        url = get_comment_author_url();
        display = ((!equal(linktext, ""))
            ? linktext
            : url);
        display = Strings.str_replace("http://www.", "", display);
        display = Strings.str_replace("http://", "", display);

        if (equal("/", Strings.substr(display, -1))) {
            display = Strings.substr(display, 0, -1);
        }

        _return = before + "<a href=\'" + url + "\' rel=\'external\'>" + display + "</a>" + after;

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_author_url_link", _return));
    }

    public void comment_author_url_link() {
        comment_author_url_link("", "", "");
    }

    public void comment_author_url_link(String linktext) {
        comment_author_url_link(linktext, "", "");
    }

    public void comment_author_url_link(String linktext, String before) {
        comment_author_url_link(linktext, before, "");
    }

    /**
     * comment_author_url_link() - Displays the HTML link of the url of the
     * author of the current comment
     * @since 0.71
     * @see get_comment_author_url_link() Echos result
     * @param string $linktext The text to display instead of the comment
     * author's email address
     * @param string $before The text or HTML to display before the email link.
     * @param string $after The text or HTML to display after the email link.
     */
    public void comment_author_url_link(String linktext, String before, String after) {
        echo(gVars.webEnv, get_comment_author_url_link(linktext, before, after));
    }

    public String get_comment_date() {
        return get_comment_date("");
    }

    /**
     * get_comment_date() - Retrieve the comment date of the current comment
     * @since 1.5
     * @uses apply_filters() Calls 'get_comment_date' hook with the formated
     * date and the $d parameter respectively
     * @uses $comment
     * @param string $d The format of the date (defaults to user's config)
     * @return string The comment's date
     */
    public String get_comment_date(String d) {
        String date = null;

        if (equal("", d)) {
            date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                    strval(StdClass.getValue(gVars.comment, "comment_date")), true);
        } else {
            date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(d, strval(StdClass.getValue(gVars.comment, "comment_date")), true);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_date", date, d));
    }

    public void comment_date() {
        comment_date("");
    }

    /**
     * comment_date() - Display the comment date of the current comment
     * @since 0.71
     * @param string $d The format of the date (defaults to user's config)
     */
    public void comment_date(String d) {
        echo(gVars.webEnv, get_comment_date(d));
    }

    /**
     * get_comment_excerpt() - Retrieve the excerpt of the current comment
     * Will cut each word and only output the first 20 words with '...' at the
     * end. If the word count is less than 20, then no truncating is done and no
     * '...' will appear.
     * @since 1.5
     * @uses $comment
     * @uses apply_filters() Calls 'get_comment_excerpt' on truncated comment
     * @return string The maybe truncated comment with 20 words or less
     */
    public String get_comment_excerpt() {
        String comment_text = null;
        Array<String> blah = new Array<String>();
        int k = 0;
        int use_dotdotdot = 0;
        String excerpt = null;
        int i = 0;
        comment_text = Strings.strip_tags(strval(StdClass.getValue(gVars.comment, "comment_content")));
        blah = Strings.explode(" ", comment_text);

        if (Array.count(blah) > 20) {
            k = 20;
            use_dotdotdot = 1;
        } else {
            k = Array.count(blah);
            use_dotdotdot = 0;
        }

        excerpt = "";

        for (i = 0; i < k; i++) {
            excerpt = excerpt + blah.getValue(i) + " ";
        }

        excerpt = excerpt + (booleanval(use_dotdotdot)
            ? "..."
            : "");

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_excerpt", excerpt));
    }

    /**
     * comment_excerpt() - Returns the excerpt of the current comment
     * @since 1.2
     * @uses apply_filters() Calls 'comment_excerpt' hook before displaying
     * excerpt
     */
    public void comment_excerpt() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_excerpt", get_comment_excerpt()));
    }

    /**
     * get_comment_ID() - Retrieve the comment id of the current comment
     * @since 1.5
     * @uses $comment
     * @uses apply_filters() Calls the 'get_comment_ID' hook for the comment ID
     * @return int The comment ID
     */
    public int get_comment_ID() {
        return intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_ID", intval(StdClass.getValue(gVars.comment, "comment_ID"))));
    }

    /**
     * comment_ID() - Displays the comment id of the current comment
     * @since 0.71
     * @see get_comment_ID() Echos Result
     */
    public void comment_ID() {
        echo(gVars.webEnv, get_comment_ID());
    }

    /**
     * get_comment_link() - Retrieve the link to the current comment
     * @since 1.5
     * @uses $comment
     * @return string The permalink to the current comment
     */
    public String get_comment_link() {
        return getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(intval(StdClass.getValue(gVars.comment, "comment_post_ID")), false) + "#comment-" +
        intval(StdClass.getValue(gVars.comment, "comment_ID"));
    }

    /**
     * get_comments_link() - Retrieves the link to the current post comments
     * @since 1.5
     * @return string The link to the comments
     */
    public String get_comments_link() {
        return getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "#comments";
    }

    public void comments_link() {
        comments_link("", "");
    }

    public void comments_link(String deprecated) {
        comments_link(deprecated, "");
    }

    /**
     * comments_link() - Displays the link to the current post comments
     * @since 0.71
     * @param string $deprecated Not Used
     * @param bool $deprecated Not Used
     */
    public void comments_link(String deprecated, String deprecated1)/* Modified by Numiton */
     {
        echo(gVars.webEnv, get_comments_link());
    }

    public int get_comments_number() {
        return get_comments_number(0);
    }

    /**
     * get_comments_number() - Retrieve the amount of comments a post has
     * @since 1.5
     * @uses apply_filters() Calls the 'get_comments_number' hook on the number
     * of comments
     * @param int $post_id The Post ID
     * @return int The number of comments a post has
     */
    public int get_comments_number(int post_id) {
        StdClass post = null;
        int count = 0;
        post_id = post_id;

        if (!booleanval(post_id)) {
            post_id = intval(gVars.id);
        }

        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(post_id, gConsts.getOBJECT(), "raw");

        if (!isset(StdClass.getValue(post, "comment_count"))) {
            count = 0;
        } else {
            count = intval(StdClass.getValue(post, "comment_count"));
        }

        return intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comments_number", count));
    }

    public void comments_number() {
        comments_number("", "", "", "");
    }

    public void comments_number(String zero) {
        comments_number(zero, "", "", "");
    }

    public void comments_number(String zero, String one) {
        comments_number(zero, one, "", "");
    }

    public void comments_number(String zero, String one, String more) {
        comments_number(zero, one, more, "");
    }

    /**
     * comments_number() - Display the language string for the number of
     * comments the current post has
     * @since 0.71
     * @uses $id
     * @uses apply_filters() Calls the 'comments_number' hook on the output and
     * number of comments respectively.
     * @param string $zero Text for no comments
     * @param string $one Text for one comment
     * @param string $more Text for more than one comment
     * @param string $deprecated Not used.
     */
    public void comments_number(String zero, String one, String more, String deprecated) {
        String output;
        int number = get_comments_number(intval(gVars.id));

        if (number > 1) {
            output = Strings.str_replace("%", strval(number), equal(false, more)
                    ? getIncluded(L10nPage.class, gVars, gConsts).__("% Comments", "default")
                    : more);
        } else if (equal(number, 0)) {
            output = (equal(false, zero)
                ? getIncluded(L10nPage.class, gVars, gConsts).__("No Comments", "default")
                : zero);
        } else { // must be one
            output = (equal(false, one)
                ? getIncluded(L10nPage.class, gVars, gConsts).__("1 Comment", "default")
                : one);
        }

        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comments_number", output, number));
    }

    /**
     * get_comment_text() - Retrieve the text of the current comment
     * @since 1.5
     * @uses $comment
     * @return string The comment content
     */
    public String get_comment_text() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_text", StdClass.getValue(gVars.comment, "comment_content")));
    }

    /**
     * comment_text() - Displays the text of the current comment
     * @since 0.71
     * @uses apply_filters() Passes the comment content through the
     * 'comment_text' hook before display
     * @uses get_comment_text() Gets the comment content
     */
    public void comment_text() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_text", get_comment_text()));
    }

    public String get_comment_time() {
        return get_comment_time("", false);
    }

    public String get_comment_time(String d) {
        return get_comment_time(d, false);
    }

    /**
     * get_comment_time() - Retrieve the comment time of the current comment
     * @since 1.5
     * @uses $comment
     * @uses apply_filter() Calls 'get_comment_time' hook with the formatted
     * time, the $d parameter, and $gmt parameter passed.
     * @param string $d Optional. The format of the time (defaults to user's
     * config)
     * @param bool $gmt Whether to use the GMT date
     * @return string The formatted time
     */
    public String get_comment_time(String d, boolean gmt) {
        String comment_date = null;
        String date = null;
        comment_date = strval(gmt
                ? StdClass.getValue(gVars.comment, "comment_date_gmt")
                : StdClass.getValue(gVars.comment, "comment_date"));

        if (equal("", d)) {
            date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")), comment_date, true);
        } else {
            date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(d, comment_date, true);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_time", date, d, gmt));
    }

    public void comment_time() {
        comment_time("");
    }

    /**
     * comment_time() - Display the comment time of the current comment
     * @since 0.71
     * @param string $d Optional. The format of the time (defaults to user's
     * config)
     */
    public void comment_time(String d) {
        echo(gVars.webEnv, get_comment_time(d, false));
    }

    /**
     * get_comment_type() - Retrieve the comment type of the current comment
     * @since 1.5
     * @uses $comment
     * @uses apply_filters() Calls the 'get_comment_type' hook on the comment
     * type
     * @return string The comment type
     */
    public String get_comment_type() {
        if (equal("", StdClass.getValue(gVars.comment, "comment_type"))) {
            gVars.comment.fields.putValue("comment_type", "comment");
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment_type", StdClass.getValue(gVars.comment, "comment_type")));
    }

    public void comment_type() {
        comment_type("Comment", "Trackback", "Pingback");
    }

    public void comment_type(String commenttxt) {
        comment_type(commenttxt, "Trackback", "Pingback");
    }

    public void comment_type(String commenttxt, String trackbacktxt) {
        comment_type(commenttxt, trackbacktxt, "Pingback");
    }

    /**
     * comment_type() - Display the comment type of the current comment
     * @since 0.71
     * @param string $commenttxt The string to display for comment type
     * @param string $trackbacktxt The string to display for trackback type
     * @param string $pingbacktxt The string to display for pingback type
     */
    public void comment_type(String commenttxt, String trackbacktxt, String pingbacktxt) {
        Object type = null;
        type = get_comment_type();

        {
            int javaSwitchSelector59 = 0;

            if (equal(type, "trackback")) {
                javaSwitchSelector59 = 1;
            }

            if (equal(type, "pingback")) {
                javaSwitchSelector59 = 2;
            }

            switch (javaSwitchSelector59) {
            case 1: {
                echo(gVars.webEnv, trackbacktxt);

                break;
            }

            case 2: {
                echo(gVars.webEnv, pingbacktxt);

                break;
            }

            default:echo(gVars.webEnv, commenttxt);
            }
        }
    }

    /**
     * get_trackback_url() - Retrieve The current post's trackback URL
     * There is a check to see if permalink's have been enabled and if so, will
     * retrieve the pretty path. If permalinks weren't enabled, the ID of the
     * current post is used and appended to the correct page to go to.
     * @since 1.5
     * @uses apply_filters() Calls 'trackback_url' on the resulting trackback
     * URL
     * @uses $id
     * @return string The trackback URL after being filtered
     */
    public String get_trackback_url() {
        String tb_url = null;

        if (!equal("", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure"))) {
            tb_url = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false)) +
                getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit("trackback", "single_trackback");
        } else {
            tb_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-trackback.php?p=" + strval(gVars.id);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("trackback_url", tb_url));
    }

    public String trackback_url() {
        return trackback_url(true);
    }

    /**
     * trackback_url() - Displays the current post's trackback URL
     * @since 0.71
     * @uses get_trackback_url() Gets the trackback url for the current post
     * @param bool $deprecated Remove backwards compat in 2.5
     * @return void|string Should only be used to echo the trackback URL, use
     * get_trackback_url() for the result instead.
     */
    public String trackback_url(boolean deprecated) {
        if (deprecated) {
            echo(gVars.webEnv, get_trackback_url());

            return "";
        } else {
            return get_trackback_url();
        }
    }

    public void trackback_rdf() {
        trackback_rdf(0);
    }

    /**
     * trackback_rdf() - Generates and displays the RDF for the trackback
     * information of current post
     * @since 0.71
     * @param int $deprecated Not used (Was $timezone = 0)
     */
    public void trackback_rdf(int deprecated) {
        if (strictEqual(Strings.stripos(gVars.webEnv.getHttpUserAgent(), "W3C_Validator"), BOOLEAN_FALSE)) {
            echo(
                    gVars.webEnv,
                    "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n\t\t\t\txmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n\t\t\t\txmlns:trackback=\"http://madskills.com/public/xml/rss/module/trackback/\">\n\t\t\t<rdf:Description rdf:about=\"");
            getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
            echo(gVars.webEnv, "\"" + "\n");
            echo(gVars.webEnv, "    dc:identifier=\"");
            getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
            echo(gVars.webEnv, "\"" + "\n");
            echo(
                gVars.webEnv,
                "    dc:title=\"" +
                Strings.str_replace(
                    "--",
                    "&#x2d;&#x2d;",
                    getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(Strings.strip_tags(getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0)))) + "\"" + "\n");
            echo(gVars.webEnv, "    trackback:ping=\"" + get_trackback_url() + "\"" + " />\n");
            echo(gVars.webEnv, "</rdf:RDF>");
        }
    }

    public boolean comments_open() {
        return comments_open(0);
    }

    /**
     * comments_open() - Whether the current post is open for comments
     * @since 1.5
     * @uses $post
     * @param int $post_id An optional post ID to check instead of the current
     * post.
     * @return bool True if the comments are open
     */
    public boolean comments_open(int post_id) {
        StdClass _post = null;
        boolean open = false;
        _post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(post_id, gConsts.getOBJECT(), "raw");
        open = equal("open", StdClass.getValue(_post, "comment_status"));

        return booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comments_open", open, post_id));
    }

    public boolean pings_open() {
        return pings_open(0);
    }

    /**
     * pings_open() - Whether the current post is open for pings
     * @since 1.5
     * @uses $post
     * @param int $post_id An optional post ID to check instead of the current
     * post.
     * @return bool True if pings are accepted
     */
    public boolean pings_open(int post_id) {
        StdClass _post = null;
        boolean open = false;
        _post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(new Ref<Integer>(post_id), gConsts.getOBJECT(), "raw");
        open = equal("open", StdClass.getValue(_post, "ping_status"));

        return booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pings_open", open, post_id));
    }

    /**
     * wp_comment_form_unfiltered_html_nonce() - Displays form token for
     * unfiltered comments
     * Will only display nonce token if the current user has permissions for
     * unfiltered html. Won't display the token for other users.
     * The function was backported to 2.0.10 and was added to versions 2.1.3 and
     * above. Does not exist in versions prior to 2.0.10 in the 2.0 branch and
     * in the 2.1 branch, prior to 2.1.3. Technically added in 2.2.0.
     * @since 2.0.10 Backported to 2.0 branch
     * @since 2.1.3
     * @uses $post Gets the ID of the current post for the token
     */
    public void wp_comment_form_unfiltered_html_nonce(Object... deprecated) {
        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("unfiltered_html")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("unfiltered-html-comment_" + StdClass.getValue(gVars.post, "ID"), "_wp_unfiltered_html_comment", false, true);
        }
    }

    public void comments_template() {
        comments_template("/comments.php");
    }

    /**
     * comments_template() - Loads the comment template specified in $file
     * Will not display the comments template if not on single post or page, or
     * if the post does not have comments.
     * Uses the WordPress database object to query for the comments. The
     * comments are passed through the 'comments_array' filter hook with the
     * list of comments and the post ID respectively.
     * The $file path is passed through a filter hook called,
     * 'comments_template' which includes the TEMPLATEPATH and $file combined.
     * Tries the $filtered path first and if it fails it will require the
     * default comment themplate from the default theme. If either does not
     * exist, then the WordPress process will be halted. It is advised for that
     * reason, that the default theme is not deleted.
     * @since 1.5
     * @global array $comment List of comment objects for the current post
     * @uses $wpdb
     * @uses $id
     * @uses $post
     * @uses $withcomments Will not try to get the comments if the post has
     * none.
     * @param string $file Optional, default '/comments.php'. The file to load
     * @return null Returns null if no comments appear
     */
    public void comments_template(String file) {
        Object req = null;
        Array<Object> commenter = new Array<Object>();
        Array<StdClass> comments = null;
        String comment_author = null;
        String comment_author_email = null;
        String comment_author_url = null;
        String include = null;

        if (!(getIncluded(QueryPage.class, gVars, gConsts).is_single() || getIncluded(QueryPage.class, gVars, gConsts).is_page("") || booleanval(gVars.withcomments))) {
            return;
        }

        req = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("require_name_email");
        commenter = getIncluded(CommentPage.class, gVars, gConsts).wp_get_current_commenter();
        req = Array.extractVar(commenter, "req", req, Array.EXTR_SKIP);
        comment_author = strval(Array.extractVar(commenter, "comment_author", comment_author, Array.EXTR_SKIP));
        comment_author_email = strval(Array.extractVar(commenter, "comment_author_email", comment_author_email, Array.EXTR_SKIP));
        comment_author_url = strval(Array.extractVar(commenter, "comment_author_url", comment_author_url, Array.EXTR_SKIP));
        
    	/** @todo Use API instead of SELECTs. */
        if (booleanval(gVars.user_ID)) {
            comments = gVars.wpdb.get_results(
                        gVars.wpdb.prepare(
                                "SELECT * FROM " + gVars.wpdb.comments +
                                " WHERE comment_post_ID = %d AND (comment_approved = \'1\' OR ( user_id = %d AND comment_approved = \'0\' ) )  ORDER BY comment_date",
                                StdClass.getValue(gVars.post, "ID"),
                                gVars.user_ID));
        } else if (empty(comment_author)) {
            comments = gVars.wpdb.get_results(
                    gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = %d AND comment_approved = \'1\' ORDER BY comment_date", StdClass.getValue(gVars.post, "ID")));
        } else {
            comments = gVars.wpdb.get_results(
                        gVars.wpdb.prepare(
                                "SELECT * FROM " + gVars.wpdb.comments +
                                " WHERE comment_post_ID = %d AND ( comment_approved = \'1\' OR ( comment_author = %s AND comment_author_email = %s AND comment_approved = \'0\' ) ) ORDER BY comment_date",
                                StdClass.getValue(gVars.post, "ID"),
                                comment_author,
                                comment_author_email));
        }

    	// keep $comments for legacy's sake (remember $table*? ;) )
        gVars.wp_query.comments = (Array<StdClass>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comments_array", comments, StdClass.getValue(gVars.post, "ID"));
        comments = Array.arrayCopy(gVars.wp_query.comments);
        gVars.wp_query.comment_count = Array.count(gVars.wp_query.comments);
        getIncluded(CommentPage.class, gVars, gConsts).update_comment_cache(comments);
        
        gConsts.setCOMMENTS_TEMPLATE(true);
        include = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comments_template", gConsts.getTEMPLATEPATH() + file));

        // Added by Numiton. Propagate local context
        gVars.comments = Array.arrayCopy(comments);

        if (FileSystemOrSocket.file_exists(gVars.webEnv, include)) {
            // Condensed dynamic construct
            if (equal(include, "wp-content/themes/default/comments.php")) {
                // Added by Numiton. Transfer the local context
                gVars.req = req;
                gVars.comments = Array.arrayCopy(comments);
                gVars.comment_author = comment_author;
                gVars.comment_author_email = comment_author_email;
                gVars.comment_author_url = comment_author_url;

                require(gVars, gConsts, CommentsPage.class);

                // Added by Numiton. Restore the local context
                req = gVars.req;
                comments = Array.arrayCopy(gVars.comments);
                comment_author = gVars.comment_author;
                comment_author_email = gVars.comment_author_email;
                comment_author_url = gVars.comment_author_url;
            } else {
                // TODO Implement support for multiple themes
            }
        } else {
            // Added by Numiton. Transfer the local context
            gVars.req = req;
            gVars.comments = Array.arrayCopy(comments);
            gVars.comment_author = comment_author;
            gVars.comment_author_email = comment_author_email;
            gVars.comment_author_url = comment_author_url;

            require(gVars, gConsts, CommentsPage.class);

            // Added by Numiton. Restore the local context
            req = gVars.req;
            comments = Array.arrayCopy(gVars.comments);
            comment_author = gVars.comment_author;
            comment_author_email = gVars.comment_author_email;
            comment_author_url = gVars.comment_author_url;
        }
    }

    public void comments_popup_script() {
        comments_popup_script(400, 400, "");
    }

    public void comments_popup_script(int width) {
        comments_popup_script(width, 400, "");
    }

    public void comments_popup_script(int width, int height) {
        comments_popup_script(width, height, "");
    }

    /**
     * comments_popup_script() - Displays the JS popup script to show a
     * comment
     * If the $file parameter is empty, then the home page is assumed. The
     * defaults for the window are 400px by 400px.
     * For the comment link popup to work, this function has to be called or the
     * normal comment link will be assumed.
     * @since 0.71
     * @global string $wpcommentspopupfile The URL to use for the popup window
     * @global int $wpcommentsjavascript Whether to use JavaScript or not. Set
     * when function is called
     * @param int $width Optional. The width of the popup window
     * @param int $height Optional. The height of the popup window
     * @param string $file Optional. Sets the location of the popup window
     */
    public void comments_popup_script(int width, int height, String file) {
        String javascript = null;

        if (empty(file)) {
            wpcommentspopupfile = "";  // Use the index.
        } else {
            wpcommentspopupfile = file;
        }

        wpcommentsjavascript = 1;
        javascript = "<script type=\'text/javascript\'>\nfunction wpopen (macagna) {\n    window.open(macagna, \'_blank\', \'width=" + strval(width) + ",height=" + strval(height) +
            ",scrollbars=yes,status=yes\');\n}\n</script>\n";
        echo(gVars.webEnv, javascript);
    }

    public void comments_popup_link() {
        comments_popup_link("No Comments", "1 Comment", "% Comments", "", "Comments Off");
    }

    public void comments_popup_link(String zero) {
        comments_popup_link(zero, "1 Comment", "% Comments", "", "Comments Off");
    }

    public void comments_popup_link(String zero, String one) {
        comments_popup_link(zero, one, "% Comments", "", "Comments Off");
    }

    public void comments_popup_link(String zero, String one, String more) {
        comments_popup_link(zero, one, more, "", "Comments Off");
    }

    public void comments_popup_link(String zero, String one, String more, String css_class) {
        comments_popup_link(zero, one, more, css_class, "Comments Off");
    }

    /**
     * comments_popup_link() - Displays the link to the comments popup window
     * for the current post ID.
     * Is not meant to be displayed on single posts and pages. Should be used on
     * the lists of posts
     * @since 0.71
     * @uses $id
     * @uses $wpcommentspopupfile
     * @uses $wpcommentsjavascript
     * @uses $post
     * @param string $zero The string to display when no comments
     * @param string $one The string to display when only one comment is
     * available
     * @param string $more The string to display when there are more than one
     * comment
     * @param string $css_class The CSS class to use for comments
     * @param string $none The string to display when comments have been turned
     * off
     * @return null Returns null on single posts and pages.
     */
    public void comments_popup_link(String zero, String one, String more, String css_class, String none) {
        int number;
        Object home = null;
        Object title = null;

        if (getIncluded(QueryPage.class, gVars, gConsts).is_single() || getIncluded(QueryPage.class, gVars, gConsts).is_page("")) {
            return;
        }

        number = get_comments_number(intval(gVars.id));

        if (equal(0, number) && equal("closed", StdClass.getValue(gVars.post, "comment_status")) && equal("closed", StdClass.getValue(gVars.post, "ping_status"))) {
            echo(gVars.webEnv, "<span" + ((!empty(css_class))
                ? (" class=\"" + css_class + "\"")
                : "") + ">" + none + "</span>");

            return;
        }

        if (!empty(StdClass.getValue(gVars.post, "post_password"))) { // if there's a password
            if (!isset(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH())) ||
                    !equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(gVars.post, "post_password"))) {  // and it doesn't match the cookie
                echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Enter your password to view comments", "default"));

                return;
            }
        }

        echo(gVars.webEnv, "<a href=\"");

        if (booleanval(wpcommentsjavascript)) {
            if (empty(wpcommentspopupfile)) {
                home = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home");
            } else {
                home = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl");
            }

            echo(gVars.webEnv, strval(home) + "/" + wpcommentspopupfile + "?comments_popup=" + strval(gVars.id));
            echo(gVars.webEnv, "\" onclick=\"wpopen(this.href); return false\"");
        } else { // if comments_popup_script() is not in the template, display simple comment link
            if (equal(0, number)) {
                echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false) + "#respond");
            } else {
                comments_link("", "");
            }

            echo(gVars.webEnv, "\"");
        }

        if (!empty(css_class)) {
            echo(gVars.webEnv, " class=\"" + css_class + "\" ");
        }

        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0));
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comments_popup_link_attributes", ""));
        echo(gVars.webEnv, " title=\"" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Comment on %s", "default"), title) + "\">");
        comments_number(zero, one, more, strval(number));
        echo(gVars.webEnv, "</a>");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
