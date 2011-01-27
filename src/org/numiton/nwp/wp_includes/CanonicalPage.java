/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CanonicalPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.FunctionHandling;
import com.numiton.System;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class CanonicalPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CanonicalPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/canonical.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/canonical";
    }

    public String redirect_canonical() {
        return redirect_canonical(null, true);
    }

    public String redirect_canonical(String requested_url) {
        return redirect_canonical(requested_url, true);
    }

    /**
     * Canonical API to handle WordPress Redirecting
     *
     * Based on "Permalink Redirect" from Scott Yang and "Enforce www. Preference" by Mark Jaquith
     *
     * @author Scott Yang
     * @author Mark Jaquith
     * @package WordPress
     * @since 2.3
     */

    /**
     * redirect_canonical() - Redirects incoming links to the proper URL based on the site url
     *
     * Search engines consider www.somedomain.com and somedomain.com to be two different URLs
     * when they both go to the same location. This SEO enhancement prevents penality for
     * duplicate content by redirecting all incoming links to one or the other.
     *
     * Prevents redirection for feeds, trackbacks, searches, comment popup, and admin URLs.
     * Does not redirect on IIS, page/post previews, and on form data.
     *
     * Will also attempt to find the correct link when a user enters a URL that does not exist
     * based on exact WordPress query. Will instead try to parse the URL or query in an attempt
     * to figure the correct page to go to.
     *
     * @since 2.3
     * @uses $wp_rewrite
     * @uses $is_IIS
     *
     * @param string $requested_url Optional. The URL that was requested, used to figure if redirect is needed.
     * @param bool $do_redirect Optional. Redirect to the new URL.
     * @return null|false|string Null, if redirect not needed. False, if redirect not needed or the string of the URL
     */
    public String redirect_canonical(String requested_url, boolean do_redirect) {
        Array<String> original = new Array<String>();
        Array<String> redirect = new Array<String>();
        String redirect_url = null;
        String m = null;
        StdClass author;
        int paged;
        Array<String> paged_redirect = new Array<String>();
        Array<String> user_home = new Array<String>();
        String user_ts_type = null;
        String func = null;
        Object type = null;

        if (getIncluded(QueryPage.class, gVars, gConsts).is_feed() || getIncluded(QueryPage.class, gVars, gConsts).is_trackback() || getIncluded(QueryPage.class, gVars, gConsts).is_search() ||
                getIncluded(QueryPage.class, gVars, gConsts).is_comments_popup() || getIncluded(QueryPage.class, gVars, gConsts).is_admin() || gVars.is_IIS ||
                (isset(gVars.webEnv._POST) && booleanval(Array.count(gVars.webEnv._POST))) || getIncluded(QueryPage.class, gVars, gConsts).is_preview()) {
            return null;
        }

        if (!booleanval(requested_url)) {
        	// build the URL in the address bar
            requested_url = ((isset(gVars.webEnv.getHttps()) && equal(Strings.strtolower(gVars.webEnv.getHttps()), "on"))
                ? "https://"
                : "http://");
            requested_url = requested_url + gVars.webEnv.getHttpHost();
            requested_url = requested_url + gVars.webEnv.getRequestURI();
        }

        original = URL.parse_url(requested_url);

        if (empty(original)) {
            return null;
        }

    	// Some PHP setups turn requests for / into /index.php in REQUEST_URI
        original.putValue("path", QRegExPerl.preg_replace("|/index\\.php$|", "/", original.getValue("path")));
        redirect = original;
        redirect_url = strval(false);

    	// These tests give us a WP-generated permalink
        if (getIncluded(QueryPage.class, gVars, gConsts).is_404()) {
            redirect_url = redirect_guess_404_permalink();
        } else if (is_object(gVars.wp_rewrite) && gVars.wp_rewrite.using_permalinks()) {
    		// rewriting of old ?p=X, ?m=2004, ?m=200401, ?m=20040101
            if (getIncluded(QueryPage.class, gVars, gConsts).is_single() && isset(gVars.webEnv._GET.getValue("p"))) {
                if (booleanval(redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("p"), false))) {
                    redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("p", redirect.getValue("query")));
                }
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_page("") && isset(gVars.webEnv._GET.getValue("page_id"))) {
                if (booleanval(redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("page_id"), false))) {
                    redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("page_id", redirect.getValue("query")));
                }
            } else if (isset(gVars.webEnv._GET.getValue("m")) &&
                    (getIncluded(QueryPage.class, gVars, gConsts).is_year() || getIncluded(QueryPage.class, gVars, gConsts).is_month() ||
                    (((QueryPage) PhpWeb.getIncluded(QueryPage.class, gVars, gConsts))).is_day())) {
                m = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("m"));

                switch (Strings.strlen(m)) {
                case 4: { // Yearly
                    redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_year_link(m);

                    break;
                }

                case 6: { // Monthly
                    redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_month_link(Strings.substr(m, 0, 4), Strings.substr(m, 4, 2));

                    break;
                }

                case 8: { // Daily
                    redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_day_link(Strings.substr(m, 0, 4), Strings.substr(m, 4, 2), Strings.substr(m, 6, 2));

                    break;
                }
                }

                if (booleanval(redirect_url)) {
                    redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("m", redirect.getValue("query")));
                }
             // now moving on to non ?m=X year/month/day links
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_day() && booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year")) &&
                    booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum")) && isset(gVars.webEnv._GET.getValue("day"))) {
                if (booleanval(
                            redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_day_link(
                                    strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year")),
                                    strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum")),
                                    strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("day"))))) {
                    redirect.putValue(
                        "query",
                        getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(new Array<Object>(
                                new ArrayEntry<Object>("year"),
                                new ArrayEntry<Object>("monthnum"),
                                new ArrayEntry<Object>("day")), redirect.getValue("query")));
                }
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_month() && booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year")) &&
                    isset(gVars.webEnv._GET.getValue("monthnum"))) {
                if (booleanval(
                                redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts).get_month_link(
                                            strval((((QueryPage) PhpWeb.getIncluded(QueryPage.class, gVars, gConsts))).get_query_var("year")),
                                            strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum"))))) {
                    redirect.putValue(
                        "query",
                        getIncluded(FunctionsPage.class, gVars, gConsts)
                            .remove_query_arg(new Array<Object>(new ArrayEntry<Object>("year"), new ArrayEntry<Object>("monthnum")), redirect.getValue("query")));
                }
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_year() && isset(gVars.webEnv._GET.getValue("year"))) {
                if (booleanval(
                                redirect_url = getIncluded(Link_templatePage.class, gVars, gConsts)
                                                       .get_year_link(strval((((QueryPage) PhpWeb.getIncluded(QueryPage.class, gVars, gConsts))).get_query_var("year"))))) {
                    redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("year", redirect.getValue("query")));
                }
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_category(new Array<Object>()) && isset(gVars.webEnv._GET.getValue("cat"))) {
                if (booleanval(redirect_url = strval(getIncluded(Category_templatePage.class, gVars, gConsts).get_category_link(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("cat"))))) {
                    redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("cat", redirect.getValue("query")));
                }
            } else if (getIncluded(QueryPage.class, gVars, gConsts).is_author() && isset(gVars.webEnv._GET.getValue("author"))) {
                author = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("author")));

                if (!strictEqual(null, author) &&
                        booleanval(
                            redirect_url = getIncluded(DeprecatedPage.class, gVars, gConsts).get_author_link(false, intval(StdClass.getValue(author, "ID")), StdClass.getValue(author, "user_nicename")))) {
                    redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("author", redirect.getValue("author")));
                }
            }

         // paging
            if (booleanval(paged = intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("paged")))) {
                if (paged > 0) {
                    if (!booleanval(redirect_url)) {
                        redirect_url = requested_url;
                    }

                    paged_redirect = URL.parse_url(redirect_url);
                    paged_redirect.putValue("path", QRegExPerl.preg_replace("|/page/[0-9]+?(/+)?$|", "/", paged_redirect.getValue("path"))); // strip off any existing paging
                    paged_redirect.putValue("path", QRegExPerl.preg_replace("|/index.php/?$|", "/", paged_redirect.getValue("path"))); // strip off trailing /index.php/

                    if ((paged > 1) && !getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
                        paged_redirect.putValue("path", getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(paged_redirect.getValue("path")));

                        if (gVars.wp_rewrite.using_index_permalinks() && strictEqual(Strings.strpos(paged_redirect.getValue("path"), "/index.php/"), BOOLEAN_FALSE)) {
                            paged_redirect.putValue("path", paged_redirect.getValue("path") + "index.php/");
                        }

                        paged_redirect.putValue("path", paged_redirect.getValue("path") + getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit("page/" + strval(paged), "paged"));
                    } else if (!getIncluded(QueryPage.class, gVars, gConsts).is_home() && !getIncluded(QueryPage.class, gVars, gConsts).is_single()) {
                        paged_redirect.putValue("path", getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(paged_redirect.getValue("path"), "paged"));
                    }

                    redirect_url = paged_redirect.getValue("scheme") + "://" + paged_redirect.getValue("host") + paged_redirect.getValue("path");
                    redirect.putValue("path", paged_redirect.getValue("path"));
                }

                redirect.putValue("query", getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg("paged", redirect.getValue("query")));
            }
        }

    	// tack on any additional query vars
        if (booleanval(redirect_url) && booleanval(redirect.getValue("query"))) {
            if (!strictEqual(Strings.strpos(redirect_url, "?"), BOOLEAN_FALSE)) {
                redirect_url = redirect_url + "&";
            } else {
                redirect_url = redirect_url + "?";
            }

            redirect_url = redirect_url + redirect.getValue("query");
        }

        if (booleanval(redirect_url)) {
            redirect = URL.parse_url(redirect_url);
        }

    	// www.example.com vs example.com
        user_home = URL.parse_url(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));
        redirect.putValue("host", user_home.getValue("host"));

    	// Handle ports
        if (isset(user_home.getValue("port"))) {
            redirect.putValue("port", user_home.getValue("port"));
        } else {
            redirect.arrayUnset("port");
        }

    	// trailing /index.php/
        redirect.putValue("path", QRegExPerl.preg_replace("|/index.php/$|", "/", redirect.getValue("path")));

    	// strip /index.php/ when we're not using PATHINFO permalinks
        if (!gVars.wp_rewrite.using_index_permalinks()) {
            redirect.putValue("path", Strings.str_replace("/index.php/", "/", redirect.getValue("path")));
        }

    	// trailing slashes
        if (is_object(gVars.wp_rewrite) && gVars.wp_rewrite.using_permalinks() && !getIncluded(QueryPage.class, gVars, gConsts).is_404() &&
                (!getIncluded(QueryPage.class, gVars, gConsts).is_home() ||
                (getIncluded(QueryPage.class, gVars, gConsts).is_home() && (intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("paged")) > 1)))) {
            user_ts_type = "";

            if (intval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("paged")) > 0) {
                user_ts_type = "paged";
            } else {
                for (Map.Entry javaEntry385 : new Array<Object>(
                        new ArrayEntry<Object>("single"),
                        new ArrayEntry<Object>("category"),
                        new ArrayEntry<Object>("page"),
                        new ArrayEntry<Object>("day"),
                        new ArrayEntry<Object>("month"),
                        new ArrayEntry<Object>("year")).entrySet()) {
                    type = javaEntry385.getValue();
                    func = "is_" + strval(type);

                    if (booleanval(FunctionHandling.call_user_func(new Callback(func, getIncluded(QueryPage.class, gVars, gConsts))))) {
                        user_ts_type = strval(type);
                    }

                    break;
                }
            }

            redirect.putValue("path", getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(redirect.getValue("path"), user_ts_type));
        } else if (getIncluded(QueryPage.class, gVars, gConsts).is_home()) {
            redirect.putValue("path", getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(redirect.getValue("path")));
        }

    	// Always trailing slash the 'home' URL
        if (equal(redirect.getValue("path"), user_home.getValue("path"))) {
            redirect.putValue("path", getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(redirect.getValue("path")));
        }

    	// Ignore differences in host capitalization, as this can lead to infinite redirects
        if (equal(Strings.strtolower(original.getValue("host")), Strings.strtolower(redirect.getValue("host")))) {
            redirect.putValue("host", original.getValue("host"));
        }

        if (!strictEqual(
                    new Array<Object>(
                        new ArrayEntry<Object>(original.getValue("host")),
                        new ArrayEntry<Object>(original.getValue("port")),
                        new ArrayEntry<Object>(original.getValue("path")),
                        new ArrayEntry<Object>(original.getValue("query"))),
                    new Array<Object>(
                        new ArrayEntry<Object>(redirect.getValue("host")),
                        new ArrayEntry<Object>(redirect.getValue("port")),
                        new ArrayEntry<Object>(redirect.getValue("path")),
                        new ArrayEntry<Object>(redirect.getValue("query"))))) {
            redirect_url = redirect.getValue("scheme") + "://" + redirect.getValue("host");

            if (isset(redirect.getValue("port"))) {
                redirect_url = redirect_url + ":" + redirect.getValue("port");
            }

            redirect_url = redirect_url + redirect.getValue("path");

            if (booleanval(redirect.getValue("query"))) {
                redirect_url = redirect_url + "?" + redirect.getValue("query");
            }
        }

        if (!booleanval(redirect_url) || equal(redirect_url, requested_url)) {
            return strval(false);
        }

    	// Note that you can use the "redirect_canonical" filter to cancel a canonical redirect for whatever reason by returning FALSE
        redirect_url = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("redirect_canonical", redirect_url, requested_url));

        if (!booleanval(redirect_url) || equal(redirect_url, requested_url)) { // yes, again -- in case the filter aborted the request
            return strval(false);
        }

        if (do_redirect) {
    		// protect against chained redirects
            if (!booleanval(redirect_canonical(redirect_url, false))) {
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(redirect_url, 301);
                System.exit();
            } else {
                return strval(false);
            }
        } else {
            return redirect_url;
        }

        return "";
    }

    /**
     * redirect_guess_404_permalink() - Tries to guess correct post based on
     * query vars
     * @since 2.3
     * @uses $wpdb
     * @return bool|string Returns False, if it can't find post, returns correct
     * location on success.
     */
    public String redirect_guess_404_permalink() {
        String where = null;
        Object post_id = null;

        if (!booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("name"))) {
            return strval(false);
        }

        where = gVars.wpdb.prepare("post_name LIKE %s", getIncluded(QueryPage.class, gVars, gConsts).get_query_var("name") + "%");

    	// if any of year, monthnum, or day are set, use them to refine the query
        if (booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year"))) {
            where = where + gVars.wpdb.prepare(" AND YEAR(post_date) = %d", getIncluded(QueryPage.class, gVars, gConsts).get_query_var("year"));
        }

        if (booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum"))) {
            where = where + gVars.wpdb.prepare(" AND MONTH(post_date) = %d", getIncluded(QueryPage.class, gVars, gConsts).get_query_var("monthnum"));
        }

        if (booleanval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("day"))) {
            where = where + gVars.wpdb.prepare(" AND DAYOFMONTH(post_date) = %d", getIncluded(QueryPage.class, gVars, gConsts).get_query_var("day"));
        }

        post_id = gVars.wpdb.get_var("SELECT ID FROM " + gVars.wpdb.posts + " WHERE " + where + " AND post_status = \'publish\'");

        if (!booleanval(post_id)) {
            return strval(false);
        }

        return getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(post_id, false);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_canonical_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_action("template_redirect", Callback.createCallbackArray(this, "redirect_canonical"), 10, 1);

        return DEFAULT_VAL;
    }
}
