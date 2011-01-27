/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: RewritePage.java,v 1.5 2008/10/14 13:15:48 numiton Exp $
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

import com.numiton.Misc;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QVarHandling;
import com.numiton.phpcaller.PhpCaller;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class RewritePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(RewritePage.class.getName());

    @Override
    @RequestMapping("/wp-includes/rewrite.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/rewrite";
    }

    /* WP_Rewrite API
    *******************************************************************************/

    //Add a straight rewrite rule
    public void add_rewrite_rule(String regex, String redirect, String after) {
        gVars.wp_rewrite.add_rule(regex, redirect, after);
    }

  //Add a new tag (like %postname%)
  //warning: you must call this on init or earlier, otherwise the query var addition stuff won't work
    public void add_rewrite_tag(String tagname, Object regex) {
        String qv = null;

    	//validation
        if ((Strings.strlen(tagname) < 3) || !equal(Strings.getCharAt(tagname, 0), "%") || !equal(Strings.getCharAt(tagname, Strings.strlen(tagname) - 1), "%")) {
            return;
        }

        qv = Strings.trim(tagname, "%");
        gVars.wp.add_query_var(qv);
        gVars.wp_rewrite.add_rewrite_tag(tagname, strval(regex), qv + "=");
    }

  //Add a new feed type like /atom1/
    public String add_feed(String feedname, Array<Object> function) {
        String hook = null;

        if (!Array.in_array(feedname, gVars.wp_rewrite.feeds)) { //override the file if it is
            gVars.wp_rewrite.feeds.putValue(feedname);
        }

        hook = "do_feed_" + feedname;
        getIncluded(PluginPage.class, gVars, gConsts).remove_action(hook, function, 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action(hook, function, 10, 1);

        return hook;
    }

  //and an endpoint, like /trackback/
    public void add_rewrite_endpoint(Object name, Object places) {
        gVars.wp_rewrite.add_endpoint(name, places);
    }

 // examine a url (supposedly from this blog) and try to
 // determine the post ID it represents.
    public int url_to_postid(String url) {
        Array<Object> values = new Array<Object>();
        int id = 0;
        Array<Object> rewrite = new Array<Object>();
        Array<String> url_split = new Array<String>();
        Array<String> home_path = new Array<String>();
        String request = null;
        String request_match = null;
        String match = null;
        Array matches = new Array();
        String query;
        Array<Object> query_vars = new Array<Object>();
        Object key = null;
        Object value = null;
        
        url = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("url_to_postid", url));

    	// First, check to see if there is a 'p=N' or 'page_id=N' to match against
        if (QRegExPerl.preg_match("#[?&](p|page_id|attachment_id)=(\\d+)#", url, values)) {
            id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(values.getValue(2));

            if (booleanval(id)) {
                return id;
            }
        }

    	// Check to see if we are using rewrite rules
        rewrite = gVars.wp_rewrite.wp_rewrite_rules();

    	// Not using rewrite rules, and 'p=N' and 'page_id=N' methods failed, so we're out of options
        if (empty(rewrite)) {
            return 0;
        }

    	// $url cleanup by Mark Jaquith
    	// This fixes things like #anchors, ?query=strings, missing 'www.',
    	// added 'www.', or added 'index.php/' that will mess up our WP_Query
    	// and return a false negative

    	// Get rid of the #anchor
        url_split = Strings.explode("#", url);
        url = url_split.getValue(0);
        
    	// Get rid of URL ?query=string
        url_split = Strings.explode("?", url);
        url = url_split.getValue(0);

    	// Add 'www.' if it is absent and should be there
        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")), "://www.")) &&
                strictEqual(BOOLEAN_FALSE, Strings.strpos(url, "://www."))) {
            url = Strings.str_replace("://", "://www.", url);
        }

    	// Strip 'www.' if it is present and shouldn't be
        if (strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")), "://www."))) {
            url = Strings.str_replace("://www.", "://", url);
        }

    	// Strip 'index.php/' if we're not using path info permalinks
        if (!gVars.wp_rewrite.using_index_permalinks()) {
            url = Strings.str_replace("index.php/", "", url);
        }

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(url, strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"))))) {
        	// Chop off http://domain.com
            url = Strings.str_replace(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")), "", url);
        } else {
        	// Chop off /path/to/blog
            home_path = URL.parse_url(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));

            String home_pathStr = home_path.getValue("path");
            url = Strings.str_replace(home_pathStr, "", url);
        }

        // Trim leading and lagging slashes
        url = Strings.trim(url, "/");
        
        request = url;
        
        // Done with cleanup

    	// Look for matches.
        request_match = request;

        for (Map.Entry javaEntry573 : rewrite.entrySet()) {
            match = strval(javaEntry573.getKey());
            query = strval(javaEntry573.getValue());

    		// If the requesting file is the anchor of the match, prepend it
    		// to the path info.
            if (!empty(url) && strictEqual(Strings.strpos(match, url), 0) && !equal(url, request)) {
                request_match = url + "/" + request;
            }

            if (QRegExPerl.preg_match("!^" + match + "!", request_match, matches)) {
    			// Got a match.
    			// Trim the query of everything up to the '?'.
                query = QRegExPerl.preg_replace("!^.+\\?!", "", query);

                // Modified by Numiton. Transformed eval()

                // Substitute the substring matches into the query.
                // TODO Transform into string replace calls
                try {
                    StringBuilder evalExpr = new StringBuilder();
                    evalExpr.append("$request = \"" + Misc.getPhpString(request) + "\";\n");
                    evalExpr.append("$request_match = \"" + Misc.getPhpString(request_match) + "\";\n");
                    evalExpr.append("$match = \"" + Misc.getPhpString(match) + "\";\n");
                    evalExpr.append("$id = " + id + ";\n");
                    evalExpr.append("$query = \"" + Misc.getPhpString(query) + "\";echo $query;");
                    LOG.debug("evalExpr = " + evalExpr);
                    query = PhpCaller.getJavaString(QVarHandling.eval(evalExpr.toString()));
                    LOG.debug("query=" + query);
                } catch (IOException ex) {
                    LOG.warn("Error while evaluating PHP code", ex);
                }

                // Filter out non-public query vars
                Strings.parse_str(query, query_vars);

                Array<Object> queryArray = new Array<Object>();

                for (Map.Entry javaEntry574 : query_vars.entrySet()) {
                    key = javaEntry574.getKey();
                    value = javaEntry574.getValue();

                    if (Array.in_array(key, gVars.wp.public_query_vars)) {
                        queryArray.putValue(key, value);
                    }
                }

    			// Do the query
                WP_Query queryObj = new WP_Query(gVars, gConsts, queryArray);

                if (queryObj.is_single || queryObj.is_page) {
                    return intval(StdClass.getValue(queryObj.post, "ID"));
                } else {
                    return 0;
                }
            }
        }

        return 0;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_rewrite_block1");
        gVars.webEnv = webEnv;
        
        gConsts.setEP_PERMALINK(1);
        gConsts.setEP_ATTACHMENT(2);
        gConsts.setEP_DATE(4);
        gConsts.setEP_YEAR(8);
        gConsts.setEP_MONTH(16);
        gConsts.setEP_DAY(32);
        gConsts.setEP_ROOT(64);
        gConsts.setEP_COMMENTS(128);
        gConsts.setEP_SEARCH(256);
        gConsts.setEP_CATEGORIES(512);
        gConsts.setEP_TAGS(1024);
        gConsts.setEP_AUTHORS(2048);
        gConsts.setEP_PAGES(4096);
        //pseudo-places
        gConsts.setEP_NONE(0);
        gConsts.setEP_ALL(8191);

        return DEFAULT_VAL;
    }
}
