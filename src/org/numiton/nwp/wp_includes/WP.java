/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP.java,v 1.5 2008/10/14 13:15:48 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QVarHandling;
import com.numiton.string.Strings;


public class WP implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> public_query_vars = new Array<Object>(new ArrayEntry<Object>("m"), new ArrayEntry<Object>("p"), new ArrayEntry<Object>("posts"), new ArrayEntry<Object>("w"),
            new ArrayEntry<Object>("cat"), new ArrayEntry<Object>("withcomments"), new ArrayEntry<Object>("withoutcomments"), new ArrayEntry<Object>("s"), new ArrayEntry<Object>("search"),
            new ArrayEntry<Object>("exact"), new ArrayEntry<Object>("sentence"), new ArrayEntry<Object>("debug"), new ArrayEntry<Object>("calendar"), new ArrayEntry<Object>("page"),
            new ArrayEntry<Object>("paged"), new ArrayEntry<Object>("more"), new ArrayEntry<Object>("tb"), new ArrayEntry<Object>("pb"), new ArrayEntry<Object>("author"),
            new ArrayEntry<Object>("order"), new ArrayEntry<Object>("orderby"), new ArrayEntry<Object>("year"), new ArrayEntry<Object>("monthnum"), new ArrayEntry<Object>("day"),
            new ArrayEntry<Object>("hour"), new ArrayEntry<Object>("minute"), new ArrayEntry<Object>("second"), new ArrayEntry<Object>("name"), new ArrayEntry<Object>("category_name"),
            new ArrayEntry<Object>("tag"), new ArrayEntry<Object>("feed"), new ArrayEntry<Object>("author_name"), new ArrayEntry<Object>("static"), new ArrayEntry<Object>("pagename"),
            new ArrayEntry<Object>("page_id"), new ArrayEntry<Object>("error"), new ArrayEntry<Object>("comments_popup"), new ArrayEntry<Object>("attachment"),
            new ArrayEntry<Object>("attachment_id"), new ArrayEntry<Object>("subpost"), new ArrayEntry<Object>("subpost_id"), new ArrayEntry<Object>("preview"), new ArrayEntry<Object>("robots"),
            new ArrayEntry<Object>("taxonomy"), new ArrayEntry<Object>("term"));
    public Array<Object> private_query_vars = new Array<Object>(new ArrayEntry<Object>("offset"), new ArrayEntry<Object>("posts_per_page"), new ArrayEntry<Object>("posts_per_archive_page"),
            new ArrayEntry<Object>("what_to_show"), new ArrayEntry<Object>("showposts"), new ArrayEntry<Object>("nopaging"), new ArrayEntry<Object>("post_type"),
            new ArrayEntry<Object>("post_status"), new ArrayEntry<Object>("category__in"), new ArrayEntry<Object>("category__not_in"), new ArrayEntry<Object>("category__and"),
            new ArrayEntry<Object>("tag__in"), new ArrayEntry<Object>("tag__not_in"), new ArrayEntry<Object>("tag__and"), new ArrayEntry<Object>("tag_slug__in"),
            new ArrayEntry<Object>("tag_slug__and"), new ArrayEntry<Object>("tag_id"), new ArrayEntry<Object>("post_mime_type"), new ArrayEntry<Object>("perm"));
    public Array<Object> extra_query_vars = new Array<Object>();
    public Array<Object> query_vars = new Array<Object>();
    public String query_string;
    public String request;
    public Object matched_rule;
    public String matched_query;
    public boolean did_permalink = false;

    // Commented by Numiton
    //	public Object	request;
    public Object single;

    public WP(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Empty.
    }

    public void add_query_var(String qv) {
        if (!Array.in_array(qv, this.public_query_vars)) {
            this.public_query_vars.putValue(qv);
        }
    }

    public void set_query_var(Object key, Object value) {
        this.query_vars.putValue(key, value);
    }

    /**
     * Added by Numiton.
     * @param varName
     * @return
     */
    private Object getGlobalVarValue(String varName) {
        if (equal(varName, "m")) {
            return gVars.m;
        }

        if (equal(varName, "p")) {
            return gVars.p;
        }

        if (equal(varName, "posts")) {
            return gVars.posts;
        }

        //		if(equal(varName, "w")) {return gVars.w;}
        if (equal(varName, "cat")) {
            return gVars.cat;
        }

        if (equal(varName, "withcomments")) {
            return gVars.withcomments;
        }

        //        if(equal(varName, "withoutcomments")) {return gVars.withoutcomments;} 
        if (equal(varName, "s")) {
            return gVars.s;
        }

        if (equal(varName, "search")) {
            return gVars.search;
        }

        //        if(equal(varName, "exact")) {return gVars.exact;} 
        if (equal(varName, "sentence")) {
            return gVars.sentence;
        }

        if (equal(varName, "debug")) {
            return gVars.debug;
        }

        //        if(equal(varName, "calendar")) {return gVars.calendar;} 
        if (equal(varName, "page")) {
            return gVars.page;
        }

        if (equal(varName, "paged")) {
            return gVars.paged;
        }

        if (equal(varName, "more")) {
            return gVars.more;
        }

        //        if(equal(varName, "tb")) {return gVars.tb;} 

        //        if(equal(varName, "pb")) {return gVars.pb;} 
        if (equal(varName, "author")) {
            return gVars.author;
        }

        //        if(equal(varName, "order")) {return gVars.order;} 

        //        if(equal(varName, "orderby")) {return gVars.orderby;} 
        if (equal(varName, "year")) {
            return gVars.year;
        }

        if (equal(varName, "monthnum")) {
            return gVars.monthnum;
        }

        if (equal(varName, "day")) {
            return gVars.day;
        }

        //        if(equal(varName, "hour")) {return gVars.hour;} 

        //        if(equal(varName, "minute")) {return gVars.minute;} 

        //        if(equal(varName, "second")) {return gVars.second;}
        if (equal(varName, "name")) {
            return gVars.name;
        }

        //        if(equal(varName, "category_name")) {return gVars.category_name;} 
        if (equal(varName, "tag")) {
            return gVars.tag;
        }

        //        if(equal(varName, "feed")) {return gVars.feed;}

        //        if(equal(varName, "author_name")) {return gVars.author_name;} 

        //        if(equal(varName, "static")) {return gVars._static;} 

        //        if(equal(varName, "pagename")) {return gVars.pagename;} 
        if (equal(varName, "page_id")) {
            return gVars.page_id;
        }

        if (equal(varName, "error")) {
            return gVars.error;
        }

        //        if(equal(varName, "comments_popup")) {return gVars.comments_popup;} 

        //        if(equal(varName, "attachment")) {return gVars.attachment;}
        if (equal(varName, "attachment_id")) {
            return gVars.attachment_id;
        }

        //        if(equal(varName, "subpost")) {return gVars.subpost;} 

        //        if(equal(varName, "subpost_id")) {return gVars.subpost_id;} 
        if (equal(varName, "preview")) {
            return gVars.preview;
        }

        //        if(equal(varName, "robots")) {return gVars.robots;} 

        //        if(equal(varName, "taxonomy")) {return gVars.taxonomy;} 

        //        if(equal(varName, "term")) {return gVars.term;}
        if (equal(varName, "offset")) {
            return gVars.offset;
        }

        if (equal(varName, "posts_per_page")) {
            return gVars.posts_per_page;
        }

        //        if(equal(varName, "posts_per_archive_page")) {return gVars.posts_per_archive_page;} 
        if (equal(varName, "what_to_show")) {
            return gVars.what_to_show;
        }

        //        if(equal(varName, "showposts")) {return gVars.showposts;}

        //        if(equal(varName, "nopaging")) {return gVars.nopaging;} 
        if (equal(varName, "post_type")) {
            return gVars.post_type;
        }

        if (equal(varName, "post_status")) {
            return gVars.post_status;
        }

        //        if(equal(varName, "category__in")) {return gVars.category__in;} 

        //        if(equal(varName, "category__not_in")) {return gVars.category__not_in;} 

        //        if(equal(varName, "category__and")) {return gVars.category__and;} 

        //        if(equal(varName, "tag__in")) {return gVars.tag__in;} 

        //        if(equal(varName, "tag__not_in")) {return gVars.tag__not_in;} 

        //        if(equal(varName, "tag__and")) {return gVars.tag__and;} 

        //        if(equal(varName, "tag_slug__in")) {return gVars.tag_slug__in;}

        //        if(equal(varName, "tag_slug__and")) {return gVars.tag_slug__and;} 

        //        if(equal(varName, "tag_id")) {return gVars.tag_id;} 

        //        if(equal(varName, "post_mime_type")) {return gVars.post_mime_type;}

        //        if(equal(varName, "perm")) {return gVars.perm;}
        return null;
    }

    public void parse_request(Object extra_query_vars)/* Do not change type */
     {
        Array<Object> rewrite = new Array<Object>();
        String error = null;
        String pathinfo = null;
        Array<String> pathinfo_array = new Array<String>();
        String req_uri = null;
        Array<String> req_uri_array = new Array<String>();
        String self = null;
        String home_path = null;
        String request = null;
        String request_match = null;
        String match = null;
        Array<Object> matches = new Array<Object>();
        String query;
        Array<Object> perma_query_vars = new Array<Object>();
        String wpvar = null;
        int i = 0;
        String var = null;
        this.query_vars = new Array<Object>();

        if (is_array(extra_query_vars)) {
            this.extra_query_vars = (Array<Object>) extra_query_vars;
        } else if (!empty(extra_query_vars)) {
            Strings.parse_str(strval(extra_query_vars), this.extra_query_vars);
        }

        // Process PATH_INFO, REQUEST_URI, and 404 for permalinks.

		// Fetch the rewrite rules.
        rewrite = gVars.wp_rewrite.wp_rewrite_rules();

        if (!empty(rewrite)) {
			// If we match a rewrite rule, this will be cleared.
            error = "404";
            this.did_permalink = true;

            if (isset(gVars.webEnv.getPathInfo())) {
                pathinfo = gVars.webEnv.getPathInfo();
            } else {
                pathinfo = "";
            }

            pathinfo_array = Strings.explode("?", pathinfo);
            pathinfo = Strings.str_replace("%", "%25", pathinfo_array.getValue(0));
            req_uri = gVars.webEnv.getRequestURI();
            req_uri_array = Strings.explode("?", req_uri);
            req_uri = req_uri_array.getValue(0);
            self = gVars.webEnv.getPhpSelf();

            Array home_pathArray = URL.parse_url(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home")));

            if (isset(home_pathArray.getValue("path"))) {
                home_path = strval(home_pathArray.getValue("path"));
            } else {
                home_path = "";
            }

            home_path = Strings.trim(home_path, "/");
            
			// Trim path info from the end and the leading home path from the
			// front.  For path info requests, this leaves us with the requesting
			// filename, if any.  For 404 requests, this leaves us with the
			// requested permalink.
            req_uri = Strings.str_replace(pathinfo, "", URL.rawurldecode(req_uri));
            req_uri = Strings.trim(req_uri, "/");
            req_uri = QRegExPerl.preg_replace("|^" + home_path + "|", "", req_uri);
            req_uri = Strings.trim(req_uri, "/");
            pathinfo = Strings.trim(pathinfo, "/");
            pathinfo = QRegExPerl.preg_replace("|^" + home_path + "|", "", pathinfo);
            pathinfo = Strings.trim(pathinfo, "/");
            self = Strings.trim(self, "/");
            self = QRegExPerl.preg_replace("|^" + home_path + "|", "", self);
            self = Strings.trim(self, "/");

			// The requested permalink is in $pathinfo for path info requests and
			//  $req_uri for other requests.
            if (!empty(pathinfo) && !QRegExPerl.preg_match("|^.*" + gVars.wp_rewrite.index + "$|", pathinfo)) {
                request = pathinfo;
            } else {
				// If the request uri is the index, blank it out so that we don't try to match it against a rule.
                if (equal(req_uri, gVars.wp_rewrite.index)) {
                    req_uri = "";
                }

                request = req_uri;
            }

            this.request = request;
            
			// Look for matches.
            request_match = request;

            for (Map.Entry javaEntry411 : rewrite.entrySet()) {
                match = strval(javaEntry411.getKey());
                query = strval(javaEntry411.getValue());

				// If the requesting file is the anchor of the match, prepend it
				// to the path info.
                if (!empty(req_uri) && strictEqual(Strings.strpos(match, req_uri), 0) && !equal(req_uri, request)) {
                    request_match = req_uri + "/" + request;
                }

                if (QRegExPerl.preg_match("!^" + match + "!", request_match, matches) || QRegExPerl.preg_match("!^" + match + "!", URL.urldecode(request_match), matches)) {
					// Got a match.
                	this.matched_rule = match;
                	
                	// Trim the query of everything up to the '?'.
                    query = QRegExPerl.preg_replace("!^.+\\?!", "", query);

                    // Modified by Numiton. Transformed eval()

                    // TODO Transform into string replace calls
                    try {
                        StringBuilder evalExpr = new StringBuilder();
                        evalExpr.append("$error = \"" + Misc.getPhpString(error) + "\";\n");
                        evalExpr.append("$pathinfo = \"" + Misc.getPhpString(pathinfo) + "\";\n");
                        evalExpr.append("$req_uri = \"" + Misc.getPhpString(req_uri) + "\";\n");
                        evalExpr.append("$self = \"" + Misc.getPhpString(self) + "\";\n");
                        evalExpr.append("$home_path = \"" + Misc.getPhpString(home_path) + "\";\n");
                        evalExpr.append("$request = \"" + Misc.getPhpString(request) + "\";\n");
                        evalExpr.append("$request_match = \"" + Misc.getPhpString(request_match) + "\";\n");
                        evalExpr.append("$match = \"" + Misc.getPhpString(match) + "\";\n");
                        evalExpr.append("$wpvar = \"" + Misc.getPhpString(wpvar) + "\";\n");
                        evalExpr.append("$i = " + i + ";\n");
                        evalExpr.append("$var = \"" + Misc.getPhpString(var) + "\";\n");
                        evalExpr.append("$query = \"" + Misc.getPhpString(query) + "\";echo $query;");
                        LOG.debug("evalExpr = " + evalExpr);
    					// Substitute the substring matches into the query.
                        query = QVarHandling.eval(evalExpr.toString());
                        LOG.debug("query=" + query);
                    } catch (IOException ex) {
                        LOG.warn("Error while evaluating PHP code", ex);
                    }

                    this.matched_query = query;
                    
                    // Parse the query.
                    Strings.parse_str(query, perma_query_vars);

                    // If we're processing a 404 request, clear the error var
					// since we found something.
                    if (isset(gVars.webEnv._GET.getValue("error"))) {
                        gVars.webEnv._GET.arrayUnset("error");
                    }

                    if (isset(error)) {
                        error = null;
                    }

                    break;
                }
            }

			// If req_uri is empty or if it is a request for ourself, unset error.
            if (empty(request) || equal(req_uri, self) || !strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "wp-admin/"), BOOLEAN_FALSE)) {
                if (isset(gVars.webEnv._GET.getValue("error"))) {
                    gVars.webEnv._GET.arrayUnset("error");
                }

                if (isset(error)) {
                    error = null;
                }

                if (isset(perma_query_vars) && !strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "wp-admin/"), BOOLEAN_FALSE)) {
                    perma_query_vars = null;
                }

                this.did_permalink = false;
            }
        }

        this.public_query_vars = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("query_vars", this.public_query_vars);

        for (i = 0; i < Array.count(this.public_query_vars); i = i + 1) {
            wpvar = strval(this.public_query_vars.getValue(i));

            if (isset(this.extra_query_vars.getValue(wpvar))) {
                this.query_vars.putValue(wpvar, this.extra_query_vars.getValue(wpvar));
            } else if (isset(getGlobalVarValue(wpvar))) {
                this.query_vars.putValue(wpvar, getGlobalVarValue(wpvar));
            } else if (!empty(gVars.webEnv._POST.getValue(wpvar))) {
                this.query_vars.putValue(wpvar, gVars.webEnv._POST.getValue(wpvar));
            } else if (!empty(gVars.webEnv._GET.getValue(wpvar))) {
                this.query_vars.putValue(wpvar, gVars.webEnv._GET.getValue(wpvar));
            } else if (!empty(perma_query_vars.getValue(wpvar))) {
                this.query_vars.putValue(wpvar, perma_query_vars.getValue(wpvar));
            }

            if (!empty(this.query_vars.getValue(wpvar))) {
                this.query_vars.putValue(wpvar, this.query_vars.getValue(wpvar));
            }
        }

        for (Map.Entry javaEntry412 : this.private_query_vars.entrySet()) {
            var = strval(javaEntry412.getValue());

            if (isset(this.extra_query_vars.getValue(var))) {
                this.query_vars.putValue(var, this.extra_query_vars.getValue(var));
            } else if (isset(getGlobalVarValue(var)) && !equal("", getGlobalVarValue(var))) {
                this.query_vars.putValue(var, getGlobalVarValue(var));
            }
        }

        if (isset(error)) {
            this.query_vars.putValue("error", error);
        }

        this.query_vars = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("request", this.query_vars);
        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("parse_request", new Array<Object>(new ArrayEntry<Object>(this)));
    }

    public void send_headers() {
        String wp_last_modified = null;
        String wp_etag = null;
        String client_etag = null;
        String client_last_modified = null;
        int client_modified_timestamp = 0;
        int wp_modified_timestamp = 0;
        Network.header(gVars.webEnv, "X-Pingback: " + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("pingback_url", "raw"));

        if (getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
            getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
        }

        if (!empty(this.query_vars.getValue("error")) && equal("404", this.query_vars.getValue("error"))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).status_header(strval(404));

            if (!getIncluded(PluggablePage.class, gVars, gConsts).is_user_logged_in()) {
                getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
            }

            Network.header(
                gVars.webEnv,
                "Content-Type: " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type") + "; charset=" +
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));
        } else if (empty(this.query_vars.getValue("feed"))) {
            Network.header(
                gVars.webEnv,
                "Content-Type: " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type") + "; charset=" +
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));
        } else {
			// We're showing a feed, so WP is indeed the only thing that last changed
            if (!empty(this.query_vars.getValue("withcomments")) ||
                    (empty(this.query_vars.getValue("withoutcomments")) &&
                    (!empty(this.query_vars.getValue("p")) || !empty(this.query_vars.getValue("name")) || !empty(this.query_vars.getValue("page_id")) || !empty(this.query_vars.getValue("pagename")) ||
                    !empty(this.query_vars.getValue("attachment")) || !empty(this.query_vars.getValue("attachment_id"))))) {
                wp_last_modified = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("D, d M Y H:i:s", getIncluded(CommentPage.class, gVars, gConsts).get_lastcommentmodified("GMT"), false) +
                    " GMT";
            } else {
                wp_last_modified = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("D, d M Y H:i:s", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), false) +
                    " GMT";
            }

            wp_etag = "\"" + Strings.md5(wp_last_modified) + "\"";
            Network.header(gVars.webEnv, "Last-Modified: " + wp_last_modified);
            Network.header(gVars.webEnv, "ETag: " + wp_etag);

			// Support for Conditional GET
            if (isset(gVars.webEnv._SERVER.getValue("HTTP_IF_NONE_MATCH"))) {
                client_etag = Strings.stripslashes(gVars.webEnv, Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._SERVER.getValue("HTTP_IF_NONE_MATCH"))));
            } else {
                client_etag = "";
            }

            client_last_modified = (empty(gVars.webEnv._SERVER.getValue("HTTP_IF_MODIFIED_SINCE"))
                ? ""
                : Strings.trim(strval(gVars.webEnv._SERVER.getValue("HTTP_IF_MODIFIED_SINCE"))));
			// If string is empty, return 0. If not, attempt to parse into a timestamp
            client_modified_timestamp = (booleanval(client_last_modified)
                ? QDateTime.strtotime(client_last_modified)
                : 0);
            
			// Make a timestamp for our most recent modification...
            wp_modified_timestamp = QDateTime.strtotime(wp_last_modified);

            if ((booleanval(client_last_modified) && booleanval(client_etag))
                    ? ((client_modified_timestamp >= wp_modified_timestamp) && equal(client_etag, wp_etag))
                        : ((client_modified_timestamp >= wp_modified_timestamp) || equal(client_etag, wp_etag))) {
                getIncluded(FunctionsPage.class, gVars, gConsts).status_header(strval(304));
                System.exit();
            }
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("send_headers", new Array<Object>(new ArrayEntry<Object>(this)));
    }

    public void build_query_string() {
        Object wpvar = null;
        this.query_string = "";

        for (Map.Entry javaEntry413 : Array.array_keys(this.query_vars).entrySet()) {
            wpvar = javaEntry413.getValue();

            if (!equal("", this.query_vars.getValue(wpvar))) {
                this.query_string = this.query_string + ((Strings.strlen(this.query_string) < 1)
                    ? ""
                    : "&");

                if (!is_scalar(this.query_vars.getValue(wpvar))) { // Discard non-scalars.
                    continue;
                }

                this.query_string = this.query_string + strval(wpvar) + "=" + URL.rawurlencode(strval(this.query_vars.getValue(wpvar)));
            }
        }

		// query_string filter deprecated.  Use request filter instead.
        if (booleanval(getIncluded(PluginPage.class, gVars, gConsts).has_filter("query_string", false))) {  // Don't bother filtering and parsing if no plugins are hooked in.
            this.query_string = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("query_string", this.query_string));
            Strings.parse_str(this.query_string, this.query_vars);
        }
    }

    public void register_globals() {
        String key = null;
        Object value = null;

		// Extract updated query vars back into global namespace.
        for (Map.Entry javaEntry414 : gVars.wp_query.query_vars.entrySet()) {
            key = strval(javaEntry414.getKey());
            value = javaEntry414.getValue();

            // Modified by Numiton
            Field gVarField;

            try {
                gVarField = GlobalVars.class.getField(key);

                if (gVarField.getType().equals(Integer.class)) {
                    gVarField.set(gVars, intval(value));
                } else if (gVarField.getType().equals(Boolean.class)) {
                    gVarField.set(gVars, booleanval(value));
                } else if (gVarField.getType().equals(String.class)) {
                    gVarField.set(gVars, strval(value));
                } else {
                    // Do no conversion
                    gVarField.set(gVars, value);
                }
            } catch (IllegalArgumentException ex) {
                LOG.debug("Cannot set global variable with the name: " + key, ex);
            } catch (Exception ex) {
                //				LOG.debug("Cannot find global variable with the name: " + key, ex);
            }
        }

        gVars.query_string = this.query_string;
        gVars.posts = gVars.wp_query.posts;
        gVars.post = gVars.wp_query.post;

        // Commented by Numiton

        //		request = gVars.wp_query.request;
        if (getIncluded(QueryPage.class, gVars, gConsts).is_single() || getIncluded(QueryPage.class, gVars, gConsts).is_page("")) {
            gVars.more = 1;
            single = 1;
        }
    }

    public void init() {
        getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
    }

    public void query_posts() {
        this.build_query_string();
        gVars.wp_the_query.query(this.query_vars);
    }

    public void handle_404() {
		// Issue a 404 if a permalink request doesn't match any posts.  Don't
		// issue a 404 if one was already issued, if the request was a search,
		// or if the request was a regular query string request rather than a
		// permalink request.
        if (equal(0, Array.count(gVars.wp_query.posts)) && !getIncluded(QueryPage.class, gVars, gConsts).is_404() && !getIncluded(QueryPage.class, gVars, gConsts).is_search() &&
                (this.did_permalink || (!empty(gVars.webEnv.getQueryString()) && strictEqual(BOOLEAN_FALSE, Strings.strpos(gVars.webEnv.getRequestURI(), "?"))))) {
            gVars.wp_query.set_404();
            getIncluded(FunctionsPage.class, gVars, gConsts).status_header(strval(404));
            getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
        } else if (!equal(getIncluded(QueryPage.class, gVars, gConsts).is_404(), true)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).status_header(strval(200));
        }
    }

    public void main(Object query_args) {
        this.init();
        this.parse_request(query_args);
        this.send_headers();
        this.query_posts();
        this.handle_404();
        this.register_globals();
        getIncluded(PluginPage.class, gVars, gConsts).do_action_ref_array("wp", new Array<Object>(new ArrayEntry<Object>(this)));
    }

    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}
