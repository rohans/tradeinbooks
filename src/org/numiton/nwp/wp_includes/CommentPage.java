/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CommentPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import java.util.Set;

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
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class CommentPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CommentPage.class.getName());
    public boolean wp_defer_comment_counting__defer = false;
    public Array<Object> wp_update_comment_count__deferred = new Array<Object>();
    public StdClass postc = new StdClass();

    @Override
    @RequestMapping("/wp-includes/comment.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/comment";
    }

    /**
     * Manages WordPress comments
     *
     * @package WordPress
     */

    /**
     * check_comment() - Checks whether a comment passes internal checks to be allowed to add
     *
     * {@internal Missing Long Description}}
     *
     * @since 1.2
     * @uses $wpdb
     *
     * @param string $author {@internal Missing Description }}
     * @param string $email {@internal Missing Description }}
     * @param string $url {@internal Missing Description }}
     * @param string $comment {@internal Missing Description }}
     * @param string $user_ip {@internal Missing Description }}
     * @param string $user_agent {@internal Missing Description }}
     * @param string $comment_type {@internal Missing Description }}
     * @return bool {@internal Missing Description }}
     */
    public boolean check_comment(String author, String email, String url, String comment, String user_ip, String user_agent, String comment_type) {
        Array out = new Array();
        String mod_keys = null;
        Array<String> words = new Array<String>();
        String word = null;
        String pattern = null;
        Array<String> uri = new Array<String>();
        Object domain = null;
        Object home_domain = null;
        Object ok_to_comment = null;

        if (equal(1, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_moderation"))) {
            return false; // If moderation is set to manual
        }

        if (QRegExPerl.preg_match_all("|(href\t*?=\t*?[\'\"]?)?(https?:)?//|i", comment, out) >= intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_max_links"))) {
            return false; // Check # of external links
        }

        mod_keys = Strings.trim(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("moderation_keys")));

        if (!empty(mod_keys)) {
            words = Strings.explode("\n", mod_keys);

            for (Map.Entry javaEntry424 : words.entrySet()) {
                word = strval(javaEntry424.getValue());
                word = Strings.trim(word);

                // Skip empty lines
                if (empty(word)) {
                    continue;
                }

                // Do some escaping magic so that '#' chars in the
                // spam words don't break things:
                word = RegExPerl.preg_quote(word, "#");

                pattern = "#" + word + "#i";

                if (QRegExPerl.preg_match(pattern, author)) {
                    return false;
                }

                if (QRegExPerl.preg_match(pattern, email)) {
                    return false;
                }

                if (QRegExPerl.preg_match(pattern, url)) {
                    return false;
                }

                if (QRegExPerl.preg_match(pattern, comment)) {
                    return false;
                }

                if (QRegExPerl.preg_match(pattern, user_ip)) {
                    return false;
                }

                if (QRegExPerl.preg_match(pattern, user_agent)) {
                    return false;
                }
            }
        }

        // Comment whitelisting:
        if (equal(1, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_whitelist"))) {
            if (equal("trackback", comment_type) || equal("pingback", comment_type)) { // check if domain is in blogroll
                uri = URL.parse_url(url);
                domain = uri.getValue("host");
                uri = URL.parse_url(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));
                home_domain = uri.getValue("host");

                if (booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT link_id FROM " + gVars.wpdb.links + " WHERE link_url LIKE (%s) LIMIT 1", "%" + domain + "%"))) ||
                        equal(domain, home_domain)) {
                    return true;
                } else {
                    return false;
                }
            } else if (!equal(author, "") && !equal(email, "")) {
                // expected_slashed ($author, $email)
                ok_to_comment = gVars.wpdb.get_var(
                            "SELECT comment_approved FROM " + gVars.wpdb.comments + " WHERE comment_author = \'" + author + "\' AND comment_author_email = \'" + email +
                            "\' and comment_approved = \'1\' LIMIT 1");

                if (equal(1, ok_to_comment) && (empty(mod_keys) || strictEqual(BOOLEAN_FALSE, Strings.strpos(email, mod_keys)))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * get_approved_comments() - Returns the approved comments for post
     * $post_id
     * @since 2.0
     * @uses $wpdb
     * @param int $post_id The ID of the post
     * @return array $comments The approved comments
     */
    public Array<StdClass> get_approved_comments(Object post_id) {
        return gVars.wpdb.get_results(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = %d AND comment_approved = \'1\' ORDER BY comment_date", post_id));
    }

    /**
     * get_comment() - Retrieves comment data given a comment ID or comment
     * object.{@internal Missing Long Description}}
     * @since 2.0
     * @uses $wpdb
     * @param object|string|int $comment {@internal Missing Description}}
     * @param string $output OBJECT or ARRAY_A or ARRAY_N constants
     * @return object|array|null Depends on $output value.
     */
    public Object get_comment(Object comment, /* Do not change type */
        String output) {
        Object _comment;

        if (empty(comment)) {
            if (isset(gVars.comment)) {
                _comment = gVars.comment;
            } else {
                _comment = null;
            }
        } else if (is_object(comment)) {
            StdClass commentObj = (StdClass) comment;
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(commentObj, "comment_ID"), comment, "comment", 0);
            _comment = comment;
        } else {
            if (isset(gVars.comment) && equal(StdClass.getValue(gVars.comment, "comment_ID"), comment)) {
                _comment = gVars.comment;
            } else if (!booleanval(_comment = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(comment, "comment"))) {
                _comment = gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_ID = %d LIMIT 1", intval(comment)));
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(((StdClass) _comment).fields.getValue("comment_ID"), _comment, "comment", 0);
            }
        }

        _comment = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_comment", _comment);

        if (equal(output, gConsts.getOBJECT())) {
            return _comment;
        } else if (equal(output, gConsts.getARRAY_A())) {
            return ClassHandling.get_object_vars(_comment);
        } else if (equal(output, gConsts.getARRAY_N())) {
            return Array.array_values(ClassHandling.get_object_vars(_comment));
        } else {
            return _comment;
        }
    }

    /**
     * get_commentdata() - Returns an array of comment data about comment $comment_ID
     *
     * get_comment() technically does the same thing as this function. This function also
     * appears to reference variables and then not use them or not update them when needed.
     * It is advised to switch to get_comment(), since this function might be deprecated in
     * favor of using get_comment().
     *
     * @deprecated Use get_comment()
     * @see get_comment()
     * @since 0.71
     *
     * @uses $postc Comment cache, might not be used any more
     * @uses $id
     * @uses $wpdb Database Object
     *
     * @param int $comment_ID The ID of the comment
     * @param int $no_cache Whether to use the cache or not (casted to bool)
     * @param bool $include_unapproved Whether to include unapproved comments or not
     * @return array The comment data
     */
    public Array<Object> get_commentdata(int comment_ID, boolean no_cache, boolean include_unapproved) { // less flexible, but saves DB queries

        String query = null;
        Array<Object> myrow = new Array<Object>();

        if (no_cache) {
            query = gVars.wpdb.prepare("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_ID = %d", comment_ID);

            if (equal(false, include_unapproved)) {
                query = query + " AND comment_approved = \'1\'";
            }

            myrow = (Array<Object>) gVars.wpdb.get_row(query, gConsts.getARRAY_A());
        } else {
            myrow.putValue("comment_ID", StdClass.getValue(postc, "comment_ID"));
            myrow.putValue("comment_post_ID", StdClass.getValue(postc, "comment_post_ID"));
            myrow.putValue("comment_author", StdClass.getValue(postc, "comment_author"));
            myrow.putValue("comment_author_email", StdClass.getValue(postc, "comment_author_email"));
            myrow.putValue("comment_author_url", StdClass.getValue(postc, "comment_author_url"));
            myrow.putValue("comment_author_IP", StdClass.getValue(postc, "comment_author_IP"));
            myrow.putValue("comment_date", StdClass.getValue(postc, "comment_date"));
            myrow.putValue("comment_content", StdClass.getValue(postc, "comment_content"));
            myrow.putValue("comment_karma", StdClass.getValue(postc, "comment_karma"));
            myrow.putValue("comment_approved", StdClass.getValue(postc, "comment_approved"));
            myrow.putValue("comment_type", StdClass.getValue(postc, "comment_type"));
        }

        return myrow;
    }

    /**
     * get_lastcommentmodified() - The date the last comment was modified{@internal Missing Long Description}}
     * @since 1.5.0
     * @uses $wpdb
     * @global array $cache_lastcommentmodified
     * @param string $timezone Which timezone to use in reference to 'gmt',
     * 'blog', or 'server' locations
     * @return string Last comment modified date
     */
    public String get_lastcommentmodified(String timezone) {
        String add_seconds_server = null;
        String lastcommentmodified = null;

        if (isset(gVars.cache_lastcommentmodified.getValue(timezone))) {
            return gVars.cache_lastcommentmodified.getValue(timezone);
        }

        add_seconds_server = DateTime.date("Z");

        {
            int javaSwitchSelector60 = 0;

            if (equal(Strings.strtolower(timezone), "gmt")) {
                javaSwitchSelector60 = 1;
            }

            if (equal(Strings.strtolower(timezone), "blog")) {
                javaSwitchSelector60 = 2;
            }

            if (equal(Strings.strtolower(timezone), "server")) {
                javaSwitchSelector60 = 3;
            }

            switch (javaSwitchSelector60) {
            case 1: {
                lastcommentmodified = strval(gVars.wpdb.get_var("SELECT comment_date_gmt FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'1\' ORDER BY comment_date_gmt DESC LIMIT 1"));

                break;
            }

            case 2: {
                lastcommentmodified = strval(gVars.wpdb.get_var("SELECT comment_date FROM " + gVars.wpdb.comments + " WHERE comment_approved = \'1\' ORDER BY comment_date_gmt DESC LIMIT 1"));

                break;
            }

            case 3: {
                lastcommentmodified = strval(
                            gVars.wpdb.get_var(
                                    gVars.wpdb.prepare(
                                            "SELECT DATE_ADD(comment_date_gmt, INTERVAL %s SECOND) FROM " + gVars.wpdb.comments +
                                            " WHERE comment_approved = \'1\' ORDER BY comment_date_gmt DESC LIMIT 1",
                                            add_seconds_server)));

                break;
            }
            }
        }

        gVars.cache_lastcommentmodified.putValue(timezone, lastcommentmodified);

        return lastcommentmodified;
    }

    /**
     * get_comment_count() - The amount of comments in a post or total
     * comments{@internal Missing Long Description}}
     * @since 2.0.0
     * @uses $wpdb
     * @param int $post_id Optional. Comment amount in post if > 0, else total
     * com ments blog wide
     * @return array The amount of spam, approved, awaiting moderation, and
     * total
     */
    public Array<Object> get_comment_count(int post_id) {
        String where = null;
        Array<Object> totals = new Array<Object>();
        Array<Object> comment_count = new Array<Object>();
        Array<Object> row = new Array<Object>();
        post_id = post_id;
        where = "";

        if (post_id > 0) {
            where = "WHERE comment_post_ID = " + strval(post_id);
        }

        totals = new Array<Object>(
                gVars.wpdb.get_results(
                    "\n\t\tSELECT comment_approved, COUNT( * ) AS total\n\t\tFROM " + gVars.wpdb.comments + "\n\t\t" + where + "\n\t\tGROUP BY comment_approved\n\t",
                    gConsts.getARRAY_A()));
        comment_count = new Array<Object>(
                new ArrayEntry<Object>("approved", 0),
                new ArrayEntry<Object>("awaiting_moderation", 0),
                new ArrayEntry<Object>("spam", 0),
                new ArrayEntry<Object>("total_comments", 0));

        for (Map.Entry javaEntry425 : totals.entrySet()) {
            row = (Array<Object>) javaEntry425.getValue();

            {
                int javaSwitchSelector61 = 0;

                if (equal(row.getValue("comment_approved"), "spam")) {
                    javaSwitchSelector61 = 1;
                }

                if (equal(row.getValue("comment_approved"), 1)) {
                    javaSwitchSelector61 = 2;
                }

                if (equal(row.getValue("comment_approved"), 0)) {
                    javaSwitchSelector61 = 3;
                }

                switch (javaSwitchSelector61) {
                case 1: {
                    comment_count.putValue("spam", row.getValue("total"));
                    comment_count.putValue("total_comments", intval(comment_count.getValue("total_comments")) + intval(row.getValue("total")));

                    break;
                }

                case 2: {
                    comment_count.putValue("approved", row.getValue("total"));
                    comment_count.putValue("total_comments", intval(comment_count.getValue("total_comments")) + intval(row.getValue("total")));

                    break;
                }

                case 3: {
                    comment_count.putValue("awaiting_moderation", row.getValue("total"));
                    comment_count.putValue("total_comments", intval(comment_count.getValue("total_comments")) + intval(row.getValue("total")));

                    break;
                }

                default:break;
                }
            }
        }

        return comment_count;
    }

    /**
     * sanitize_comment_cookies() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 2.0.4
     */
    public void sanitize_comment_cookies() {
        String comment_author = null;
        String comment_author_email = null;
        String comment_author_url = null;

        if (isset(gVars.webEnv._COOKIE.getValue("comment_author_" + gConsts.getCOOKIEHASH()))) {
            comment_author = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_author_name", gVars.webEnv._COOKIE.getValue("comment_author_" + gConsts.getCOOKIEHASH())));
            comment_author = Strings.stripslashes(gVars.webEnv, comment_author);
            comment_author = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(comment_author);
            gVars.webEnv._COOKIE.putValue("comment_author_" + gConsts.getCOOKIEHASH(), comment_author);
        }

        if (isset(gVars.webEnv._COOKIE.getValue("comment_author_email_" + gConsts.getCOOKIEHASH()))) {
            comment_author_email = strval(
                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_author_email", gVars.webEnv._COOKIE.getValue("comment_author_email_" + gConsts.getCOOKIEHASH())));
            comment_author_email = Strings.stripslashes(gVars.webEnv, comment_author_email);
            comment_author_email = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(comment_author_email);
            gVars.webEnv._COOKIE.putValue("comment_author_email_" + gConsts.getCOOKIEHASH(), comment_author_email);
        }

        if (isset(gVars.webEnv._COOKIE.getValue("comment_author_url_" + gConsts.getCOOKIEHASH()))) {
            comment_author_url = strval(
                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_author_url", gVars.webEnv._COOKIE.getValue("comment_author_url_" + gConsts.getCOOKIEHASH())));
            comment_author_url = Strings.stripslashes(gVars.webEnv, comment_author_url);
            gVars.webEnv._COOKIE.putValue("comment_author_url_" + gConsts.getCOOKIEHASH(), comment_author_url);
        }
    }

    /**
     * wp_allow_comment() - Validates whether this comment is allowed to be
     * made or not{@internal Missing Long Description}}
     * @since 2.0.0
     * @uses $wpdb
     * @uses apply_filters() Calls 'pre_comment_approved' hook on the type of
     * comment
     * @uses do_action() Calls 'check_comment_flood' hook on $comment_author_IP,
     * $comment_author_email, and $comment_date_gmt
     * @param array $commentdata Contains information on the comment
     * @return mixed Signifies the approval status (0|1|'spam')
     */
    public String wp_allow_comment(Array<Object> commentdata) {
        String dupe = null;
        Object comment_post_ID = null;
        String comment_author = null;
        String comment_author_email = null;
        String comment_content = null;
        String comment_author_IP = null;
        Object comment_date_gmt = null;
        Integer user_id = null;
        StdClass userdata = null;
        WP_User user = null;
        Object post_author = null;
        String approved = null;
        String comment_author_url = null;
        String comment_agent = null;
        String comment_type = null;
        comment_post_ID = Array.extractVar(commentdata, "comment_post_ID", comment_post_ID, Array.EXTR_SKIP);
        comment_author = strval(Array.extractVar(commentdata, "comment_author", comment_author, Array.EXTR_SKIP));
        comment_author_email = strval(Array.extractVar(commentdata, "comment_author_email", comment_author_email, Array.EXTR_SKIP));
        comment_content = strval(Array.extractVar(commentdata, "comment_content", comment_content, Array.EXTR_SKIP));
        comment_author_IP = strval(Array.extractVar(commentdata, "comment_author_IP", comment_author_IP, Array.EXTR_SKIP));
        comment_date_gmt = Array.extractVar(commentdata, "comment_date_gmt", comment_date_gmt, Array.EXTR_SKIP);
        user_id = intval(Array.extractVar(commentdata, "user_id", user_id, Array.EXTR_SKIP));
        userdata = (StdClass) Array.extractVar(commentdata, "userdata", userdata, Array.EXTR_SKIP);
        user = (WP_User) Array.extractVar(commentdata, "user", user, Array.EXTR_SKIP);
        post_author = Array.extractVar(commentdata, "post_author", post_author, Array.EXTR_SKIP);
        comment_author_url = strval(Array.extractVar(commentdata, "comment_author_url", comment_author_url, Array.EXTR_SKIP));
        comment_agent = strval(Array.extractVar(commentdata, "comment_agent", comment_agent, Array.EXTR_SKIP));
        comment_type = strval(Array.extractVar(commentdata, "comment_type", comment_type, Array.EXTR_SKIP));

        // Simple duplicate check
        // expected_slashed ($comment_post_ID, $comment_author, $comment_author_email, $comment_content)
        dupe = "SELECT comment_ID FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = \'" + strval(comment_post_ID) + "\' AND ( comment_author = \'" + comment_author + "\' ";

        if (booleanval(comment_author_email)) {
            dupe = dupe + "OR comment_author_email = \'" + comment_author_email + "\' ";
        }

        dupe = dupe + ") AND comment_content = \'" + comment_content + "\' LIMIT 1";

        if (booleanval(gVars.wpdb.get_var(dupe))) {
            getIncluded(FunctionsPage.class, gVars, gConsts)
                .wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Duplicate comment detected; it looks as though you\'ve already said that!", "default"), "");
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("check_comment_flood", comment_author_IP, comment_author_email, comment_date_gmt);

        if (booleanval(user_id)) {
            userdata = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
            user = new WP_User(gVars, gConsts, user_id);
            post_author = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT post_author FROM " + gVars.wpdb.posts + " WHERE ID = %d LIMIT 1", comment_post_ID));
        }

        if (booleanval(userdata) && (equal(user_id, post_author) || user.has_cap("level_9"))) {
    		// The author and the admins get respect.
            approved = strval(1);
        } else {
        	// Everyone else's comments will be checked.
            if (check_comment(comment_author, comment_author_email, comment_author_url, comment_content, comment_author_IP, comment_agent, comment_type)) {
                approved = strval(1);
            } else {
                approved = strval(0);
            }

            if (wp_blacklist_check(comment_author, comment_author_email, comment_author_url, comment_content, comment_author_IP, comment_agent)) {
                approved = "spam";
            }
        }

        approved = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_approved", approved));

        return approved;
    }

    /**
     * check_comment_flood_db() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 2.3.0
     * @uses $wpdb
     * @uses apply_filters() {@internal Missing Description}}
     * @uses do_action() {@internal Missing Description}}
     * @param string $ip {@internal Missing Description}}
     * @param string $email {@internal Missing Description}}
     * @param unknown_type $date {@internal Missing Description}}
     */
    public void check_comment_flood_db(String ip, String email, String date) {
        String lasttime = null;
        String time_lastcomment = null;
        String time_newcomment = null;
        Object flood_die = null;

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_options")) {
            return; // don't throttle admins
        }

        if (booleanval(
                        lasttime = strval(
                                    gVars.wpdb.get_var(
                                            "SELECT comment_date_gmt FROM " + gVars.wpdb.comments + " WHERE comment_author_IP = \'" + ip + "\' OR comment_author_email = \'" + email +
                                            "\' ORDER BY comment_date DESC LIMIT 1")))) {
            time_lastcomment = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("U", lasttime, true);
            time_newcomment = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("U", date, true);
            flood_die = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_flood_filter", false, time_lastcomment, time_newcomment);

            if (booleanval(flood_die)) {
                getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_flood_trigger", time_lastcomment, time_newcomment);
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are posting comments too quickly.  Slow down.", "default"), "");
            }
        }
    }

    /**
     * wp_blacklist_check() - Does comment contain blacklisted characters or
     * words{@internal Missing Long Description}}
     * @since 1.5.0
     * @uses do_action() Calls 'wp_blacklist_check' hook for all parameters
     * @param string $author The author of the comment
     * @param string $email The email of the comment
     * @param string $url The url used in the comment
     * @param string $comment The comment content
     * @param string $user_ip The comment author IP address
     * @param string $user_agent The author's browser user agent
     * @return bool True if comment contains blacklisted content, false if
     * comment does not
     */
    public boolean wp_blacklist_check(String author, String email, String url, String comment, String user_ip, String user_agent) {
        Array chars = new Array();
        Object _char = null;
        String mod_keys = null;
        Array<String> words = new Array<String>();
        String word = null;
        String pattern = null;
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_blacklist_check", author, email, url, comment, user_ip, user_agent);

        if (booleanval(QRegExPerl.preg_match_all("/&#(\\d+);/", comment + author + url, chars))) {
            for (Map.Entry javaEntry426 : new Array<Object>(chars.getValue(1)).entrySet()) {
                _char = javaEntry426.getValue();

    			// If it's an encoded char in the normal ASCII set, reject
                if (equal(38, _char)) {
                    continue; // Unless it's &
                }

                if (intval(_char) < 128) {
                    return true;
                }
            }
        }

        mod_keys = Strings.trim(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blacklist_keys")));

        if (equal("", mod_keys)) {
            return false; // If moderation keys are empty
        }

        words = Strings.explode("\n", mod_keys);

        for (Map.Entry javaEntry427 : new Array<Object>(words).entrySet()) {
            word = strval(javaEntry427.getValue());
            word = Strings.trim(word);

            // Skip empty lines
            if (empty(word)) {
                continue;
            }

    		// Do some escaping magic so that '#' chars in the
    		// spam words don't break things:
            word = RegExPerl.preg_quote(word, "#");
            pattern = "#" + word + "#i";

            if (QRegExPerl.preg_match(pattern, author) || QRegExPerl.preg_match(pattern, email) || QRegExPerl.preg_match(pattern, url) || QRegExPerl.preg_match(pattern, comment) ||
                    QRegExPerl.preg_match(pattern, user_ip) || QRegExPerl.preg_match(pattern, user_agent)) {
                return true;
            }
        }

        return false;
    }

    public StdClass wp_count_comments() {
        StdClass count;
        Array<Object> stats = new Array<Object>();
        Array<Object> approved = new Array<Object>();
        Array<Object> row = new Array<Object>();
        Object row_num = null;
        Object key = null;
        count = (StdClass) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("comments", "counts");

        if (!strictEqual(null, count)) {
            return count;
        }

        Array<Object> countArr = gVars.wpdb.get_results("SELECT comment_approved, COUNT( * ) AS num_comments FROM " + gVars.wpdb.comments + " GROUP BY comment_approved", gConsts.getARRAY_A());
        stats = new Array<Object>();
        approved = new Array<Object>(new ArrayEntry<Object>("0", "moderated"), new ArrayEntry<Object>("1", "approved"), new ArrayEntry<Object>("spam", "spam"));

        for (Map.Entry javaEntry428 : countArr.entrySet()) {
            row_num = javaEntry428.getKey();
            row = (Array<Object>) javaEntry428.getValue();
            stats.putValue(approved.getValue(row.getValue("comment_approved")), row.getValue("num_comments"));
        }

        for (Map.Entry javaEntry429 : approved.entrySet()) {
            key = javaEntry429.getValue();

            if (empty(stats.getValue(key))) {
                stats.putValue(key, 0);
            }
        }

        StdClass statsObj = Array.toStdClass(stats);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("comments", statsObj, "counts", 0);

        return statsObj;
    }

    /**
     * wp_delete_comment() - Removes comment ID and maybe updates post comment
     * count
     * The post comment count will be updated if the comment was approved and
     * has a post ID available.
     * @since 2.0.0
     * @uses $wpdb
     * @uses do_action() Calls 'delete_comment' hook on comment ID
     * @uses do_action() Calls 'wp_set_comment_status' hook on comment ID with
     * 'delete' set for the second parameter
     * @param int $comment_id Comment ID
     * @return bool False if delete comment query failure, true on success
     */
    public boolean wp_delete_comment(int comment_id) {
        StdClass comment = null;
        int post_id;
        getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_comment", comment_id);
        comment = (StdClass) get_comment(comment_id, gConsts.getOBJECT());

        if (!booleanval(gVars.wpdb.query("DELETE FROM " + gVars.wpdb.comments + " WHERE comment_ID=\'" + comment_id + "\' LIMIT 1"))) {
            return false;
        }

        post_id = intval(StdClass.getValue(comment, "comment_post_ID"));

        if (booleanval(post_id) && equal(StdClass.getValue(comment, "comment_approved"), 1)) {
            wp_update_comment_count(post_id, false);
        }

        clean_comment_cache(comment_id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_set_comment_status", comment_id, "delete");

        return true;
    }

    /**
     * wp_get_comment_status() - The status of a comment by ID
     * @since 1.0.0
     * @param int $comment_id Comment ID
     * @return string|bool Status might be 'deleted', 'approved', 'unapproved',
     * 'spam'. False on failure
     */
    public String wp_get_comment_status(int comment_id) {
        StdClass comment = null;
        Object approved = null;
        comment = (StdClass) get_comment(comment_id, gConsts.getOBJECT());

        if (!booleanval(comment)) {
            return strval(false);
        }

        approved = StdClass.getValue(comment, "comment_approved");

        if (equal(approved, null)) {
            return "deleted";
        } else if (equal(approved, "1")) {
            return "approved";
        } else if (equal(approved, "0")) {
            return "unapproved";
        } else if (equal(approved, "spam")) {
            return "spam";
        } else {
            return strval(false);
        }
    }

    /**
     * wp_get_current_commenter() - Get current commenter's name, email, and
     * URL
     * Expects cookies content to already be sanitized. User of this function
     * might wish to recheck the returned array for validity.
     * @see sanitize_comment_cookies() Use to sanitize cookies
     * @since 2.0.4
     * @return array Comment author, email, url respectively
     */
    public Array<Object> wp_get_current_commenter() {
        String comment_author = null;
        String comment_author_email = null;
        String comment_author_url = null;
        
    	// Cookies should already be sanitized.
        
        comment_author = "";

        if (isset(gVars.webEnv._COOKIE.getValue("comment_author_" + gConsts.getCOOKIEHASH()))) {
            comment_author = strval(gVars.webEnv._COOKIE.getValue("comment_author_" + gConsts.getCOOKIEHASH()));
        }

        comment_author_email = "";

        if (isset(gVars.webEnv._COOKIE.getValue("comment_author_email_" + gConsts.getCOOKIEHASH()))) {
            comment_author_email = strval(gVars.webEnv._COOKIE.getValue("comment_author_email_" + gConsts.getCOOKIEHASH()));
        }

        comment_author_url = "";

        if (isset(gVars.webEnv._COOKIE.getValue("comment_author_url_" + gConsts.getCOOKIEHASH()))) {
            comment_author_url = strval(gVars.webEnv._COOKIE.getValue("comment_author_url_" + gConsts.getCOOKIEHASH()));
        }

        return Array.compact(new ArrayEntry("comment_author", comment_author), new ArrayEntry("comment_author_email", comment_author_email), new ArrayEntry("comment_author_url", comment_author_url));
    }

    /**
     * wp_insert_comment() - Inserts a comment to the database{@internal Missing Long Description}}
     * @since 2.0.0
     * @uses $wpdb
     * @param array $commentdata Contains information on the comment
     * @return int The new comment's id
     */
    public int wp_insert_comment(Array<Object> commentdata) {
        String comment_author_IP = null;
        String comment_date = null;
        String comment_date_gmt = null;
        Integer comment_parent = null;
        String comment_approved = null;
        Integer user_id = null;
        Integer result = null;
        Integer comment_post_ID = null;
        Object comment_author = null;
        Object comment_author_email = null;
        Object comment_author_url = null;
        Object comment_content = null;
        Object comment_agent = null;
        Object comment_type = null;
        Integer id = null;
        comment_author_IP = strval(Array.extractVar(commentdata, "comment_author_IP", comment_author_IP, Array.EXTR_SKIP));
        comment_date = strval(Array.extractVar(commentdata, "comment_date", comment_date, Array.EXTR_SKIP));
        comment_date_gmt = strval(Array.extractVar(commentdata, "comment_date_gmt", comment_date_gmt, Array.EXTR_SKIP));
        comment_parent = intval(Array.extractVar(commentdata, "comment_parent", comment_parent, Array.EXTR_SKIP));
        comment_approved = strval(Array.extractVar(commentdata, "comment_approved", comment_approved, Array.EXTR_SKIP));
        user_id = intval(Array.extractVar(commentdata, "user_id", user_id, Array.EXTR_SKIP));
        comment_post_ID = intval(Array.extractVar(commentdata, "comment_post_ID", comment_post_ID, Array.EXTR_SKIP));
        comment_author = Array.extractVar(commentdata, "comment_author", comment_author, Array.EXTR_SKIP);
        comment_author_email = Array.extractVar(commentdata, "comment_author_email", comment_author_email, Array.EXTR_SKIP);
        comment_author_url = Array.extractVar(commentdata, "comment_author_url", comment_author_url, Array.EXTR_SKIP);
        comment_content = Array.extractVar(commentdata, "comment_content", comment_content, Array.EXTR_SKIP);
        comment_agent = Array.extractVar(commentdata, "comment_agent", comment_agent, Array.EXTR_SKIP);
        comment_type = Array.extractVar(commentdata, "comment_type", comment_type, Array.EXTR_SKIP);
        id = intval(Array.extractVar(commentdata, "id", id, Array.EXTR_SKIP));

        if (!isset(comment_author_IP)) {
            comment_author_IP = "";
        }

        if (!isset(comment_date)) {
            comment_date = strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
        }

        if (!isset(comment_date_gmt)) {
            comment_date_gmt = getIncluded(FormattingPage.class, gVars, gConsts).get_gmt_from_date(comment_date);
        }

        if (!isset(comment_parent)) {
            comment_parent = 0;
        }

        if (!isset(comment_approved)) {
            comment_approved = strval(1);
        }

        if (!isset(user_id)) {
            user_id = 0;
        }

        result = gVars.wpdb.query(
                    "INSERT INTO " + gVars.wpdb.comments +
                    "\n\t(comment_post_ID, comment_author, comment_author_email, comment_author_url, comment_author_IP, comment_date, comment_date_gmt, comment_content, comment_approved, comment_agent, comment_type, comment_parent, user_id)\n\tVALUES\n\t(\'" +
                    comment_post_ID + "\', \'" + comment_author + "\', \'" + comment_author_email + "\', \'" + comment_author_url + "\', \'" + comment_author_IP + "\', \'" + comment_date + "\', \'" +
                    comment_date_gmt + "\', \'" + comment_content + "\', \'" + comment_approved + "\', \'" + comment_agent + "\', \'" + comment_type + "\', \'" + comment_parent + "\', \'" + user_id +
                    "\')\n\t");
        id = gVars.wpdb.insert_id;

        if (equal(comment_approved, 1)) {
            wp_update_comment_count(comment_post_ID, false);
        }

        return id;
    }

    /**
     * wp_filter_comment() - Parses and returns comment information
     *
     * Sets the comment data 'filtered' field to true when finished. This
     * can be checked as to whether the comment should be filtered and to
     * keep from filtering the same comment more than once.
     *
     * @since 2.0.0
     * @uses apply_filters() Calls 'pre_user_id' hook on comment author's user ID
     * @uses apply_filters() Calls 'pre_comment_user_agent' hook on comment author's user agent
     * @uses apply_filters() Calls 'pre_comment_author_name' hook on comment author's name
     * @uses apply_filters() Calls 'pre_comment_content' hook on the comment's content
     * @uses apply_filters() Calls 'pre_comment_user_ip' hook on comment author's IP
     * @uses apply_filters() Calls 'pre_comment_author_url' hook on comment author's URL
     * @uses apply_filters() Calls 'pre_comment_author_email' hook on comment author's email address
     *
     * @param array $commentdata Contains information on the comment
     * @return array Parsed comment information
     */
    public Array<Object> wp_filter_comment(Array<Object> commentdata) {
        commentdata.putValue("user_id", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_user_id", commentdata.getValue("user_ID")));
        commentdata.putValue("comment_agent", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_user_agent", commentdata.getValue("comment_agent")));
        commentdata.putValue("comment_author", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_author_name", commentdata.getValue("comment_author")));
        commentdata.putValue("comment_content", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_content", commentdata.getValue("comment_content")));
        commentdata.putValue("comment_author_IP", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_user_ip", commentdata.getValue("comment_author_IP")));
        commentdata.putValue("comment_author_url", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_author_url", commentdata.getValue("comment_author_url")));
        commentdata.putValue("comment_author_email", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_comment_author_email", commentdata.getValue("comment_author_email")));
        commentdata.putValue("filtered", true);

        return commentdata;
    }

    /**
     * wp_throttle_comment_flood() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 2.1.0
     * @param unknown_type $block {@internal Missing Description}}
     * @param unknown_type $time_lastcomment {@internal Missing Description}}
     * @param unknown_type $time_newcomment {@internal Missing Description}}
     * @return unknown {@internal Missing Description}}
     */
    public boolean wp_throttle_comment_flood(Object block, Object time_lastcomment, Object time_newcomment) {
        if (booleanval(block)) { // a plugin has already blocked... we'll let that decision stand
            return booleanval(block);
        }

        if ((intval(time_newcomment) - intval(time_lastcomment)) < 15) {
            return true;
        }

        return false;
    }

    /**
     * wp_new_comment() - Parses and adds a new comment to the database{@internal Missing Long Description}}
     * @since 1.5.0
     * @uses apply_filters() Calls 'preprocess_comment' hook on $commentdata
     * parameter array before processing
     * @uses do_action() Calls 'comment_post' hook on $comment_ID returned from
     * adding the comment and if the comment was approved.
     * @uses wp_filter_comment() Used to filter comment before adding comment
     * @uses wp_allow_comment() checks to see if comment is approved.
     * @uses wp_insert_comment() Does the actual comment insertion to the
     * database
     * @param array $commentdata Contains information on the comment
     * @return int The ID of the comment after adding.
     */
    public int wp_new_comment(Array<Object> commentdata) {
        int comment_ID = 0;
        StdClass post;
        commentdata = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("preprocess_comment", commentdata);
        commentdata.putValue("comment_post_ID", intval(commentdata.getValue("comment_post_ID")));
        commentdata.putValue("user_ID", intval(commentdata.getValue("user_ID")));
        commentdata.putValue("comment_author_IP", QRegExPerl.preg_replace("/[^0-9a-fA-F:., ]/", "", gVars.webEnv.getRemoteAddr()));
        commentdata.putValue("comment_agent", gVars.webEnv.getHttpUserAgent());
        commentdata.putValue("comment_date", getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0));
        commentdata.putValue("comment_date_gmt", getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 1));
        commentdata = wp_filter_comment(commentdata);
        commentdata.putValue("comment_approved", wp_allow_comment(commentdata));
        comment_ID = wp_insert_comment(commentdata);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_post", comment_ID, commentdata.getValue("comment_approved"));

        if (!strictEqual("spam", commentdata.getValue("comment_approved"))) { // If it's spam save it silently for later crunching
            if (equal("0", commentdata.getValue("comment_approved"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_notify_moderator(comment_ID);
            }

            post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(commentdata.getValue("comment_post_ID"), gConsts.getOBJECT(), "raw"); // Don't notify if it's your own comment

            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comments_notify")) && booleanval(commentdata.getValue("comment_approved")) &&
                    !equal(StdClass.getValue(post, "post_author"), commentdata.getValue("user_ID"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_notify_postauthor(comment_ID, strval(commentdata.getValue("comment_type")));
            }
        }

        return comment_ID;
    }

    /**
     * wp_set_comment_status() - Sets the status of comment ID{@internal Missing Long Description}}
     * @since 1.0.0
     * @param int $comment_id Comment ID
     * @param string $comment_status New comment status, either 'hold',
     * 'approve', 'spam', or 'delete'
     * @return bool False on failure or deletion and true on success.
     */
    public boolean wp_set_comment_status(int comment_id, String comment_status) {
        String query = null;
        StdClass comment = null;

        {
            int javaSwitchSelector62 = 0;

            if (equal(comment_status, "hold")) {
                javaSwitchSelector62 = 1;
            }

            if (equal(comment_status, "approve")) {
                javaSwitchSelector62 = 2;
            }

            if (equal(comment_status, "spam")) {
                javaSwitchSelector62 = 3;
            }

            if (equal(comment_status, "delete")) {
                javaSwitchSelector62 = 4;
            }

            switch (javaSwitchSelector62) {
            case 1: {
                query = "UPDATE " + gVars.wpdb.comments + " SET comment_approved=\'0\' WHERE comment_ID=\'" + strval(comment_id) + "\' LIMIT 1";

                break;
            }

            case 2: {
                query = "UPDATE " + gVars.wpdb.comments + " SET comment_approved=\'1\' WHERE comment_ID=\'" + strval(comment_id) + "\' LIMIT 1";

                break;
            }

            case 3: {
                query = "UPDATE " + gVars.wpdb.comments + " SET comment_approved=\'spam\' WHERE comment_ID=\'" + strval(comment_id) + "\' LIMIT 1";

                break;
            }

            case 4:return wp_delete_comment(comment_id);

            default:return false;
            }
        }

        if (!booleanval(gVars.wpdb.query(query))) {
            return false;
        }

        clean_comment_cache(comment_id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_set_comment_status", comment_id, comment_status);
        comment = (StdClass) get_comment(comment_id, gConsts.getOBJECT());
        wp_update_comment_count(intval(StdClass.getValue(comment, "comment_post_ID")), false);

        return true;
    }

    /**
     * wp_update_comment() - Parses and updates an existing comment in the
     * database{@internal Missing Long Description}}
     * @since 2.0.0
     * @uses $wpdb
     * @param array $commentarr Contains information on the comment
     * @return int Comment was updated if value is 1, or was not updated if
     * value is 0.
     */
    public int wp_update_comment(Array<Object> commentarr) {
        Array<Object> comment = new Array<Object>();
        Object key = null;
        String value = null;
        Object comment_content = null;
        String comment_date_gmt = null;
        String comment_date = null;
        Object comment_author = null;
        Object comment_author_email = null;
        Object comment_approved = null;
        Object comment_author_url = null;
        Object comment_ID = null;
        Integer rval = null;
        Integer comment_post_ID = null;
        
        // First, get all of the original fields
        comment = (Array<Object>) get_comment(commentarr.getValue("comment_ID"), gConsts.getARRAY_A());

    	// Escape data pulled from DB.
        for (Map.Entry javaEntry430 : new Array<Object>(comment).entrySet()) {
            key = javaEntry430.getKey();
            value = strval(javaEntry430.getValue());
            comment.putValue(key, gVars.wpdb.escape(value));
        }

    	// Merge old and new fields with new fields overwriting old ones.
        commentarr = Array.array_merge(comment, commentarr);
        
        commentarr = wp_filter_comment(commentarr);
        
    	// Now extract the merged array.
        comment_content = Array.extractVar(commentarr, "comment_content", comment_content, Array.EXTR_SKIP);
        comment_date = strval(Array.extractVar(commentarr, "comment_date", comment_date, Array.EXTR_SKIP));
        comment_author = Array.extractVar(commentarr, "comment_author", comment_author, Array.EXTR_SKIP);
        comment_author_email = Array.extractVar(commentarr, "comment_author_email", comment_author_email, Array.EXTR_SKIP);
        comment_approved = Array.extractVar(commentarr, "comment_approved", comment_approved, Array.EXTR_SKIP);
        comment_author_url = Array.extractVar(commentarr, "comment_author_url", comment_author_url, Array.EXTR_SKIP);
        comment_ID = Array.extractVar(commentarr, "comment_ID", comment_ID, Array.EXTR_SKIP);
        comment_post_ID = intval(Array.extractVar(commentarr, "comment_post_ID", comment_post_ID, Array.EXTR_SKIP));
        
        comment_content = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_save_pre", comment_content);
        comment_date_gmt = getIncluded(FormattingPage.class, gVars, gConsts).get_gmt_from_date(comment_date);
        gVars.wpdb.query(
                "UPDATE " + gVars.wpdb.comments + " SET\n\t\t\tcomment_content      = \'" + comment_content + "\',\n\t\t\tcomment_author       = \'" + comment_author +
                "\',\n\t\t\tcomment_author_email = \'" + comment_author_email + "\',\n\t\t\tcomment_approved     = \'" + comment_approved + "\',\n\t\t\tcomment_author_url   = \'" +
                comment_author_url + "\',\n\t\t\tcomment_date         = \'" + comment_date + "\',\n\t\t\tcomment_date_gmt     = \'" + comment_date_gmt + "\'\n\t\tWHERE comment_ID = " + comment_ID);
        rval = gVars.wpdb.rows_affected;
        clean_comment_cache(comment_ID);
        wp_update_comment_count(comment_post_ID, false);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_comment", comment_ID);

        return rval;
    }

    /**
     * wp_defer_comment_counting() - Whether to defer comment counting
     * When setting $defer to true, all post comment counts will not be updated
     * until $defer is set to false. When $defer is set to false, then all
     * previously deferred updated post comment counts will then be
     * automatically updated without having to call wp_update_comment_count()
     * after.
     * @since 2.5
     * @staticvar bool $_defer
     * @param bool $defer
     * @return unknown
     */
    public boolean wp_defer_comment_counting(Object defer) {
        if (is_bool(defer)) {
            wp_defer_comment_counting__defer = booleanval(defer);

    		// flush any deferred counts
            if (!booleanval(defer)) {
                wp_update_comment_count(intval(null), true);
            }
        }

        return wp_defer_comment_counting__defer;
    }

    /**
     * wp_update_comment_count() - Updates the comment count for post(s)
     * When $do_deferred is false (is by default) and the comments have been set
     * to be deferred, the post_id will be added to a queue, which will be
     * updated at a later date and only updated once per post ID.
     * If the comments have not be set up to be deferred, then the post will be
     * updated. When $do_deferred is set to true, then all previous deferred
     * post IDs will be updated along with the current $post_id.
     * @since 2.1.0
     * @see wp_update_comment_count_now() For what could cause a false return
     * value
     * @param int $post_id Post ID
     * @param bool $do_deferred Whether to process previously deferred post
     * comment counts
     * @return bool True on success, false on failure
     */
    public boolean wp_update_comment_count(int post_id, boolean do_deferred) {
        int _post_id = 0;
        Object i = null;

        if (do_deferred) {
            wp_update_comment_count__deferred = Array.array_unique(wp_update_comment_count__deferred);

            for (Map.Entry javaEntry431 : wp_update_comment_count__deferred.entrySet()) {
                i = javaEntry431.getKey();
                _post_id = intval(javaEntry431.getValue());
                wp_update_comment_count_now(_post_id);
                wp_update_comment_count__deferred.arrayUnset(i); /** @todo Move this outside of the foreach and reset $_deferred to an array instead */
            }
        }

        if (wp_defer_comment_counting(null)) {
            wp_update_comment_count__deferred.putValue(post_id);

            return true;
        } else if (booleanval(post_id)) {
            return wp_update_comment_count_now(post_id);
        }

        return false;
    }

    /**
     * wp_update_comment_count_now() - Updates the comment count for the post
     * @since 2.5
     * @uses $wpdb
     * @uses do_action() Calls 'wp_update_comment_count' hook on $post_id, $new,
     * and $old
     * @uses do_action() Calls 'edit_posts' hook on $post_id and $post
     * @param int $post_id Post ID
     * @return bool False on '0' $post_id or if post with ID does not exist.
     * True on success.
     */
    public boolean wp_update_comment_count_now(int post_id) {
        StdClass post = null;
        int old = 0;
        int _new = 0;

        //		post_id = intval(post_id);
        if (!booleanval(post_id)) {
            return false;
        }

        if (!booleanval(post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return false;
        }

        old = intval(StdClass.getValue(post, "comment_count"));
        _new = intval(gVars.wpdb.get_var("SELECT COUNT(*) FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = \'" + post_id + "\' AND comment_approved = \'1\'"));
        gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET comment_count = \'" + _new + "\' WHERE ID = \'" + post_id + "\'");

        if (equal("page", StdClass.getValue(post, "post_type"))) {
            getIncluded(PostPage.class, gVars, gConsts).clean_page_cache(post_id);
        } else {
            getIncluded(PostPage.class, gVars, gConsts).clean_post_cache(post_id);
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("wp_update_comment_count", post_id, _new, old);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_post", post_id, post);

        return true;
    }

    /**
     * Ping and trackback functions. Ping and trackback functions.
     * discover_pingback_server_uri() - Finds a pingback server URI based on the
     * given URL{@internal Missing Long Description}}
     * @since 1.5.0
     * @uses $wp_version
     * @param string $url URL to ping
     * @param int $timeout_bytes Number of bytes to timeout at. Prevents big
     * file downloads, default is 2048.
     * @return bool|string False on failure, string containing URI on success.
     */
    public String discover_pingback_server_uri(String url, int timeout_bytes) {
        Integer byte_count = null;
        String contents = null;
        String headers = null;
        String pingback_str_dquote = null;
        String pingback_str_squote = null;
        String x_pingback_str = null;
        String host = null;
        String path = null;
        String query = null;
        Object port = null;
        Integer fp = null;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        String request = null;
        String line = null;
        Integer x_pingback_header_offset = null;
        Array<Object> matches = new Array<Object>();
        String pingback_server_url = null;
        String content_type = null;
        Integer pingback_link_offset_dquote = null;
        Integer pingback_link_offset_squote = null;
        String quote = null;
        Integer pingback_link_offset = null;
        Integer pingback_href_pos = null;
        Integer pingback_href_start = null;
        Integer pingback_href_end = null;
        Integer pingback_server_url_len = null;
        
        byte_count = 0;
        contents = "";
        headers = "";
        pingback_str_dquote = "rel=\"pingback\"";
        pingback_str_squote = "rel=\'pingback\'";
        x_pingback_str = "x-pingback: ";

        {
            Array parsedUrl = URL.parse_url(url);
            host = strval(Array.extractVar(parsedUrl, "host", host, Array.EXTR_SKIP));
            path = strval(Array.extractVar(parsedUrl, "path", path, Array.EXTR_SKIP));
            query = strval(Array.extractVar(parsedUrl, "query", query, Array.EXTR_SKIP));
            port = Array.extractVar(parsedUrl, "port", port, Array.EXTR_SKIP);
        }

        if (!isset(host)) { // Not an URL. This should never happen.
            return strval(false);
        }

        path = ((!isset(path))
            ? "/"
            : path);
        path = path + (isset(query)
            ? ("?" + query)
            : "");
        port = (isset(port)
            ? port
            : 80);
        
    	// Try to connect to the server at $host
        fp = FileSystemOrSocket.fsockopen(gVars.webEnv, host, intval(port), errno, errstr, 2);

        if (!booleanval(fp)) { // Couldn't open a connection to $host
            return strval(false);
        }

    	// Send the GET request
        request = "GET " + path + " HTTP/1.1\r\nHost: " + host + "\r\nUser-Agent: nWordPress/" + gVars.wp_version + " \r\n\r\n";
    	// ob_end_flush();
        FileSystemOrSocket.fputs(gVars.webEnv, fp, request);

    	// Let's check for an X-Pingback header first
        while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
            line = FileSystemOrSocket.fgets(gVars.webEnv, fp, 512);

            if (equal(Strings.trim(line), "")) {
                break;
            }

            headers = headers + Strings.trim(line) + "\n";
            x_pingback_header_offset = Strings.strpos(Strings.strtolower(headers), x_pingback_str);

            if (booleanval(x_pingback_header_offset)) {
    			// We got it!
                QRegExPerl.preg_match("#x-pingback: (.+)#is", headers, matches);
                pingback_server_url = Strings.trim(strval(matches.getValue(1)));

                return pingback_server_url;
            }

            if (BOOLEAN_FALSE != Strings.strpos(Strings.strtolower(headers), "content-type: ")) {
                QRegExPerl.preg_match("#content-type: (.+)#is", headers, matches);
                content_type = Strings.trim(strval(matches.getValue(1)));
            }
        }

        if (QRegExPerl.preg_match("#(image|audio|video|model)/#is", content_type)) { // Not an (x)html, sgml, or xml page, no use going further
            return strval(false);
        }

        while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
            line = FileSystemOrSocket.fgets(gVars.webEnv, fp, 1024);
            contents = contents + Strings.trim(line);
            pingback_link_offset_dquote = Strings.strpos(contents, pingback_str_dquote);
            pingback_link_offset_squote = Strings.strpos(contents, pingback_str_squote);

            if (booleanval(pingback_link_offset_dquote) || booleanval(pingback_link_offset_squote)) {
                quote = (booleanval(pingback_link_offset_dquote)
                    ? "\""
                    : "\'");
                pingback_link_offset = (equal(quote, "\"")
                    ? pingback_link_offset_dquote
                    : pingback_link_offset_squote);
                pingback_href_pos = Strings.strpos(contents, "href=", pingback_link_offset);
                pingback_href_start = pingback_href_pos + 6;
                pingback_href_end = Strings.strpos(contents, quote, pingback_href_start);
                pingback_server_url_len = pingback_href_end - pingback_href_start;
                pingback_server_url = Strings.substr(contents, pingback_href_start, pingback_server_url_len);

    			// We may find rel="pingback" but an incomplete pingback URL
                if (pingback_server_url_len > 0) { // We got it!
                    return pingback_server_url;
                }
            }

            byte_count = byte_count + Strings.strlen(line);

            if (byte_count > timeout_bytes) {
    			// It's no use going further, there probably isn't any pingback
    			// server to find in this file. (Prevents loading large files.)
                return strval(false);
            }
        }

    	// We didn't find anything.
        return strval(false);
    }

    /**
     * do_all_pings() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 2.1.0
     * @uses $wpdb
     */
    public void do_all_pings() {
        StdClass ping;
        StdClass enclosure;
        Object trackbacks;

        /* Do not change type */
        int trackback;

    	// Do pingbacks
        while (booleanval(
                        ping = (StdClass) gVars.wpdb.get_row(
                                    "SELECT * FROM " + gVars.wpdb.posts + ", " + gVars.wpdb.postmeta + " WHERE " + gVars.wpdb.posts + ".ID = " + gVars.wpdb.postmeta + ".post_id AND " +
                                    gVars.wpdb.postmeta + ".meta_key = \'_pingme\' LIMIT 1"))) {
            gVars.wpdb.query("DELETE FROM " + gVars.wpdb.postmeta + " WHERE post_id = " + StdClass.getValue(ping, "ID") + " AND meta_key = \'_pingme\';");
            pingback(strval(StdClass.getValue(ping, "post_content")), intval(StdClass.getValue(ping, "ID")));
        }

    	// Do Enclosures
        while (booleanval(
                        enclosure = (StdClass) gVars.wpdb.get_row(
                                    "SELECT * FROM " + gVars.wpdb.posts + ", " + gVars.wpdb.postmeta + " WHERE " + gVars.wpdb.posts + ".ID = " + gVars.wpdb.postmeta + ".post_id AND " +
                                    gVars.wpdb.postmeta + ".meta_key = \'_encloseme\' LIMIT 1"))) {
            gVars.wpdb.query("DELETE FROM " + gVars.wpdb.postmeta + " WHERE post_id = " + StdClass.getValue(enclosure, "ID") + " AND meta_key = \'_encloseme\';");
            getIncluded(FunctionsPage.class, gVars, gConsts).do_enclose(strval(StdClass.getValue(enclosure, "post_content")), intval(StdClass.getValue(enclosure, "ID")));
        }

    	// Do Trackbacks
        trackbacks = gVars.wpdb.get_col("SELECT ID FROM " + gVars.wpdb.posts + " WHERE to_ping <> \'\' AND post_status = \'publish\'");

        if (is_array(trackbacks)) {
            for (Map.Entry javaEntry432 : ((Array<?>) trackbacks).entrySet()) {
                trackback = intval(javaEntry432.getValue());
                do_trackbacks(trackback);
            }
        }

    	//Do Update Services/Generic Pings
        generic_ping(0);
    }

    /**
     * do_trackbacks() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 1.5.0
     * @uses $wpdb
     * @param int $post_id Post ID to do trackbacks on
     */
    public void do_trackbacks(int post_id) {
        StdClass post;
        String to_ping = null;
        Array<String> pinged;
        String excerpt = null;
        String post_title = null;
        String tb_ping = null;
        post = (StdClass) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.posts + " WHERE ID = " + post_id);
        to_ping = getIncluded(PostPage.class, gVars, gConsts).get_to_ping(post_id);
        pinged = getIncluded(PostPage.class, gVars, gConsts).get_pung(post_id);

        if (empty(to_ping)) {
            gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET to_ping = \'\' WHERE ID = \'" + post_id + "\'");

            return;
        }

        if (empty(StdClass.getValue(post, "post_excerpt"))) {
            excerpt = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_content", StdClass.getValue(post, "post_content")));
        } else {
            excerpt = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_excerpt", StdClass.getValue(post, "post_excerpt")));
        }

        excerpt = Strings.str_replace("]]>", "]]&gt;", excerpt);
        excerpt = getIncluded(FormattingPage.class, gVars, gConsts).wp_html_excerpt(excerpt, 252) + "...";
        post_title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(post, "post_title")));
        post_title = Strings.strip_tags(post_title);

        if (booleanval(to_ping)) {
            for (Map.Entry javaEntry433 : new Array<Object>(to_ping).entrySet()) {
                tb_ping = strval(javaEntry433.getValue());
                tb_ping = Strings.trim(tb_ping);

                if (!Array.in_array(tb_ping, pinged)) {
                    trackback(tb_ping, post_title, excerpt, post_id);
                    pinged.putValue(tb_ping);
                } else {
                    gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET to_ping = TRIM(REPLACE(to_ping, \'" + tb_ping + "\', \'\')) WHERE ID = \'" + post_id + "\'");
                }
            }
        }
    }

    /**
     * generic_ping() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 1.2.0
     * @param int $post_id Post ID. Not actually used.
     * @return int Same as Post ID from parameter
     */
    public int generic_ping(int post_id) {
        String services;
        String service = null;
        services = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("ping_sites"));

        Array<String> servicesArray = Strings.explode("\n", services);

        for (Map.Entry javaEntry434 : servicesArray.entrySet()) {
            service = strval(javaEntry434.getValue());
            service = Strings.trim(service);

            if (!equal("", service)) {
                weblog_ping(service, "");
            }
        }

        return post_id;
    }

    /**
     * pingback() - Pings back the links found in a post{@internal Missing Long Description}}
     * @since 0.71
     * @uses $wp_version
     * @uses IXR_Client
     * @param string $content {@internal Missing Description}}
     * @param int $post_ID {@internal Missing Description}}
     */
    public void pingback(String content, int post_ID) {
        Array<Object> post_links = new Array<Object>();
        Array<String> pung = new Array<String>();
        String ltrs = null;
        String gunk = null;
        String punc = null;
        String any = null;
        Array post_links_temp = new Array();
        String link_test = null;
        Array<String> test = new Array<String>();
        String pingback_server_url = null;
        String pagelinkedto = null;
        String pagelinkedfrom;
        IXR_Client client = null;

    	// original code by Mort (http://mort.mine.nu:8080)
        post_links = new Array<Object>();
        pung = getIncluded(PostPage.class, gVars, gConsts).get_pung(post_ID);
        
    	// Variables
        ltrs = "\\w";
        gunk = "/#~:.?+=&%@!\\-";
        punc = ".:?\\-";
        any = ltrs + gunk + punc;
        
    	// Step 1
    	// Parsing the post, external links (if any) are stored in the $post_links array
    	// This regexp comes straight from phpfreaks.com
    	// http://www.phpfreaks.com/quickcode/Extract_All_URLs_on_a_Page/15.php
        QRegExPerl.preg_match_all("{\\b http : [" + any + "] +? (?= [" + punc + "] * [^" + any + "] | $)}x", content, post_links_temp);

    	// Step 2.
    	// Walking thru the links array
    	// first we get rid of links pointing to sites, not to specific files
    	// Example:
    	// http://dummy-weblog.org
    	// http://dummy-weblog.org/
    	// http://dummy-weblog.org/post.php
    	// We don't wanna ping first and second types, even if they have a valid <link/>

        for (Map.Entry javaEntry435 : (Set<Map.Entry>) post_links_temp.getArrayValue(0).entrySet()) {
            link_test = strval(javaEntry435.getValue());

            if (!Array.in_array(link_test, pung) && !equal(getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(link_test), post_ID) // If we haven't pung it already and it isn't a link to itself 
            		&& !getIncluded(PostPage.class, gVars, gConsts).is_local_attachment(link_test)) { // Also, let's never ping local attachments.
                test = URL.parse_url(link_test);

                if (isset(test.getValue("query"))) {
                    post_links.putValue(link_test);
                } else if (!equal(test.getValue("path"), "/") && !equal(test.getValue("path"), "")) {
                    post_links.putValue(link_test);
                }
            } else {
            }
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("pre_ping", new Array<Object>(new ArrayEntry<Object>(post_links), new ArrayEntry<Object>(pung)));

        for (Map.Entry javaEntry436 : new Array<Object>(post_links).entrySet()) {
            pagelinkedto = strval(javaEntry436.getValue());
            pingback_server_url = discover_pingback_server_uri(pagelinkedto, 2048);

            if (booleanval(pingback_server_url)) {
                Options.set_time_limit(gVars.webEnv, 60);
   			 	// Now, the RPC call
                pagelinkedfrom = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(post_ID, false);
                
    			// using a timeout of 3 seconds should be enough to cover slow servers
                client = new IXR_Client(gVars, gConsts, pingback_server_url);
                client.timeout = 3;
                client.useragent = client.useragent + " -- nWordPress/" + gVars.wp_version;
                
    			// when set to true, this outputs debug messages by itself
                client.debug = false;

                if (client.query("pingback.ping", pagelinkedfrom, pagelinkedto) || (isset(client.error.code) && equal(48, client.error.code))) {
                    getIncluded(PostPage.class, gVars, gConsts).add_ping(post_ID, pagelinkedto);
                }
            }
        }
    }

    /**
     * privacy_ping_filter() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 2.1.0
     * @param unknown_type $sites {@internal Missing Description}}
     * @return unknown {@internal Missing Description}}
     */
    public String privacy_ping_filter(Object sites) {
        if (!equal("0", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_public"))) {
            return strval(sites);
        } else {
            return "";
        }
    }

    /**
     * trackback() - Send a Trackback{@internal Missing Long Description}}
     * @since 0.71
     * @uses $wpdb
     * @uses $wp_version WordPress version
     * @param string $trackback_url {@internal Missing Description}}
     * @param string $title {@internal Missing Description}}
     * @param string $excerpt {@internal Missing Description}}
     * @param int $ID {@internal Missing Description}}
     * @return unknown {@internal Missing Description}}
     */
    public int trackback(String trackback_url, String title, String excerpt, Object ID) {
        String blog_name = null;
        String tb_url = null;
        String url = null;
        String query_string = null;
        String http_request = null;
        int fs = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();

        if (empty(trackback_url)) {
            return 0;
        }

        title = URL.urlencode(title);
        excerpt = URL.urlencode(excerpt);
        blog_name = URL.urlencode(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname")));
        tb_url = trackback_url;
        url = URL.urlencode(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(ID, false));
        query_string = "title=" + title + "&url=" + url + "&blog_name=" + blog_name + "&excerpt=" + excerpt;

        Array trackback_urlArray = URL.parse_url(trackback_url);
        http_request = "POST " + strval(trackback_urlArray.getValue("path")) + (booleanval(trackback_urlArray.getValue("query"))
            ? ("?" + strval(trackback_urlArray.getValue("query")))
            : "") + " HTTP/1.0\r\n";
        http_request = http_request + "Host: " + strval(trackback_urlArray.getValue("host")) + "\r\n";
        http_request = http_request + "Content-Type: application/x-www-form-urlencoded; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\r\n";
        http_request = http_request + "Content-Length: " + strval(Strings.strlen(query_string)) + "\r\n";
        http_request = http_request + "User-Agent: nWordPress/" + gVars.wp_version;
        http_request = http_request + "\r\n\r\n";
        http_request = http_request + query_string;

        if (equal("", trackback_urlArray.getValue("port"))) {
            trackback_urlArray.putValue("port", 80);
        }

        fs = FileSystemOrSocket.fsockopen(gVars.webEnv, strval(trackback_urlArray.getValue("host")), intval(trackback_urlArray.getValue("port")), errno, errstr, 4);
        FileSystemOrSocket.fputs(gVars.webEnv, fs, http_request);
        FileSystemOrSocket.fclose(gVars.webEnv, fs);
        tb_url = Strings.addslashes(gVars.webEnv, tb_url);
        gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET pinged = CONCAT(pinged, \'\n\', \'" + tb_url + "\') WHERE ID = \'" + ID + "\'");

        return gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET to_ping = TRIM(REPLACE(to_ping, \'" + tb_url + "\', \'\')) WHERE ID = \'" + ID + "\'");
    }

    /**
     * weblog_ping() - {@internal Missing Short Description}}{@internal Missing Long Description}}
     * @since 1.2.0
     * @uses $wp_version
     * @uses IXR_Client
     * @param unknown_type $server
     * @param unknown_type $path
     */
    public void weblog_ping(String server, String path) {
        IXR_Client client = null;
        String home = null;

    	// using a timeout of 3 seconds should be enough to cover slow servers
        client = new IXR_Client(gVars, gConsts, server, (!booleanval(Strings.strlen(Strings.trim(path))) || equal("/", path))
                ? strval(false)
                : path);
        client.timeout = 3;
        client.useragent = client.useragent + " -- nWordPress/" + gVars.wp_version;
        
    	// when set to true, this outputs debug messages by itself
        client.debug = false;
        home = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));

        if (!client.query(
                    "weblogUpdates.extendedPing",
                    getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname"),
                    home,
                    (((General_templatePage) PhpWeb.getIncluded(General_templatePage.class, gVars, gConsts))).get_bloginfo("rss2_url", "raw"))) { // then try a normal ping
            client.query("weblogUpdates.ping", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogname"), home);
        }
    }

 //
 // Cache
 //

 /**
  * clean_comment_cache() - Removes comment ID from the comment cache
  *
  * @since 2.3.0
  * @package WordPress
  * @subpackage Cache
  *
  * @param int $id Comment ID to remove from cache
  */
    public void clean_comment_cache(Object id) {
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(id, "comment");
    }

    /**
     * update_comment_cache() - Updates the comment cache of given comments
     * Will add the comments in $comments to the cache. If comment ID already
     * exists in the comment cache then it will not be updated.
     * The comment is added to the cache using the comment group with the key
     * using the ID of the comments.
     * @since 2.3.0
     * @param array $comments Array of comment row objects
     */
    public void update_comment_cache(Object comments) {
        StdClass comment = null;

        for (Map.Entry javaEntry437 : new Array<Object>(comments).entrySet()) {
            comment = (StdClass) javaEntry437.getValue();
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(comment, "comment_ID"), comment, "comment", 0);
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
