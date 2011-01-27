/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: RssPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.includes.GeneralPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.generic.ListAssigner;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class RssPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(RssPage.class.getName());

    /**
     * Generated in place of local variable 'field' from method
     * '_response_to_rss' because it is used inside an inner class.
     */
    Object _response_to_rss_field = null;

    /**
     * Generated in place of local variable 'val' from method '_response_to_rss'
     * because it is used inside an inner class.
     */
    String _response_to_rss_val = null;

    /**
     * Generated in place of local variable 'year' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_year;

    /**
     * Generated in place of local variable 'month' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_month;

    /**
     * Generated in place of local variable 'day' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_day;

    /**
     * Generated in place of local variable 'hours' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_hours;

    /**
     * Generated in place of local variable 'minutes' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_minutes;

    /**
     * Generated in place of local variable 'seconds' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_seconds;

    /**
     * Generated in place of local variable 'tz_mod' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    Object parse_w3cdtf_tz_mod = null;

    /**
     * Generated in place of local variable 'tz_hour' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_tz_hour = 0;

    /**
     * Generated in place of local variable 'tz_min' from method 'parse_w3cdtf'
     * because it is used inside an inner class.
     */
    int parse_w3cdtf_tz_min = 0;

    @Override
    @RequestMapping("/wp-includes/rss.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/rss";
    }

    public MagpieRSS fetch_rss(String url) {
        Snoopy resp = null;
        RSSCache cache = null;
        String cache_status;
        Array<Object> request_headers = new Array<Object>();
        MagpieRSS rss = null;
        String errormsg = null;
        String http_error = null;
        
    	// initialize constants
        init();

        if (!isset(url)) {
    		// error("fetch_rss called without a url");
            return null;
        }

    	// if cache is disabled
        if (!booleanval(gConsts.getMAGPIE_CACHE_ON())) {
    		// fetch file, and parse it
            resp = _fetch_remote_file(url, "");

            if (is_success(resp.status)) {
                return _response_to_rss(resp);
            } else {
    			// error("Failed to fetch $url and cache is off");
                return null;
            }
        } 
    	// else cache is ON
        else {
    		// Flow
    		// 1. check cache
    		// 2. if there is a hit, make sure its fresh
    		// 3. if cached obj fails freshness check, fetch remote
    		// 4. if remote fails, return stale object, or error

            cache = new RSSCache(gVars, gConsts, gConsts.getMAGPIE_CACHE_DIR(), gConsts.getMAGPIE_CACHE_AGE());

            if (booleanval(gConsts.getMAGPIE_DEBUG()) && booleanval(cache.ERROR)) {
                getIncluded(GeneralPage.class, gVars, gConsts).debug(cache.ERROR, ErrorHandling.E_USER_WARNING);
            }

            cache_status = "";		// response of check_cache
            request_headers = new Array<Object>(); // HTTP headers to send with fetch
            rss = null;		// parsed RSS object
            errormsg = strval(0);		// errors, if any

            if (!booleanval(cache.ERROR)) {
    			// return cache HIT, MISS, or STALE
                cache_status = cache.check_cache(url);
            }

    		// if object cached, and cache is fresh, return cached obj
            if (equal(cache_status, "HIT")) {
                rss = (MagpieRSS) cache.get(url);

                if (isset(rss) && booleanval(rss)) {
                    rss.from_cache = 1;

                    if (gConsts.getMAGPIE_DEBUG() > 1) {
                        getIncluded(GeneralPage.class, gVars, gConsts).debug("MagpieRSS: Cache HIT", ErrorHandling.E_USER_NOTICE);
                    }

                    return rss;
                }
            }

    		// else attempt a conditional get

    		// setup headers
            if (equal(cache_status, "STALE")) {
                rss = (MagpieRSS) cache.get(url);

                if (booleanval(rss) && booleanval(rss.etag) && booleanval(rss.last_modified)) {
                    request_headers.putValue("If-None-Match", Strings.trim(rss.etag, "\"\n\r")); //Modified by Numiton
                    request_headers.putValue("If-Last-Modified", rss.last_modified);
                }
            }

            resp = _fetch_remote_file(url, request_headers);

            if (isset(resp) && booleanval(resp)) {
                if (equal(resp.status, "304")) {
    				// we have the most current copy
                    if (gConsts.getMAGPIE_DEBUG() > 1) {
                        getIncluded(GeneralPage.class, gVars, gConsts).debug("Got 304 for " + url);
                    }

    				// reset cache on 304 (at minutillo insistent prodding)
                    cache.set(url, rss);

                    return rss;
                } else if (is_success(resp.status)) {
                    rss = _response_to_rss(resp);

                    if (booleanval(rss)) {
                        if (gConsts.getMAGPIE_DEBUG() > 1) {
                            getIncluded(GeneralPage.class, gVars, gConsts).debug("Fetch successful");
                        }

    					// add object to cache
                        cache.set(url, rss);

                        return rss;
                    }
                } else {
                    errormsg = "Failed to fetch " + url + ". ";

                    if (booleanval(resp.error)) {
    					// compensate for Snoopy's annoying habbit to tacking
    					// on '\n'
                        http_error = Strings.substr(resp.error, 0, -2);
                        errormsg = errormsg + "(HTTP Error: " + http_error + ")";
                    } else {
                        errormsg = errormsg + "(HTTP Response: " + resp.response_code + ")";
                    }
                }
            } else {
                errormsg = "Unable to retrieve RSS file for unknown reasons.";
            }

    		// else fetch failed

    		// attempt to return cached object
            if (booleanval(rss)) {
                if (booleanval(gConsts.getMAGPIE_DEBUG())) {
                    getIncluded(GeneralPage.class, gVars, gConsts).debug("Returning STALE object for " + url);
                }

                return rss;
            }

    		// else we totally failed
    		// error( $errormsg );
            
            return null;
        } // end if ( !MAGPIE_CACHE_ON ) {
    } // end fetch_rss()

    public Snoopy _fetch_remote_file(String url, Object headers)/* Do not change type */
     {
        Snoopy client = null;
        
    	// Snoopy is an HTTP client in PHP
        client = new Snoopy(gVars, gConsts);
        client.agent = gConsts.getMAGPIE_USER_AGENT();
        client.read_timeout = gConsts.getMAGPIE_FETCH_TIME_OUT();
        client.use_gzip = gConsts.getMAGPIE_USE_GZIP();

        if (is_array(headers)) {
            client.rawheaders = (Array<Object>) headers;
        }

        client.fetch(url);

        return client;
    }

    public MagpieRSS _response_to_rss(Snoopy resp) {
        MagpieRSS rss = null;
        String h = null;
        String errormsg = null;
        rss = new MagpieRSS(gVars, gConsts, strval(resp.results));

    	// if RSS parsed successfully
        if (booleanval(rss) && !booleanval(rss.ERROR)) {

    		// find Etag, and Last-Modified
            for (Map.Entry javaEntry588 : resp.headers.entrySet()) {
                h = strval(javaEntry588.getValue());

    			// 2003-03-02 - Nicola Asuni (www.tecnick.com) - fixed bug "Undefined offset: 1"
                if (BOOLEAN_FALSE != Strings.strpos(h, ": ")) {
                    new ListAssigner<String>() {
                            public Array<String> doAssign(Array<String> srcArray) {
                                if (strictEqual(srcArray, null)) {
                                    return null;
                                }

                                _response_to_rss_field = srcArray.getValue(0);
                                _response_to_rss_val = srcArray.getValue(1);

                                return srcArray;
                            }
                        }.doAssign(Strings.explode(": ", h, 2));
                } else {
                    _response_to_rss_field = h;
                    _response_to_rss_val = "";
                }

                if (equal(_response_to_rss_field, "ETag")) {
                    rss.etag = _response_to_rss_val;
                }

                if (equal(_response_to_rss_field, "Last-Modified")) {
                    rss.last_modified = _response_to_rss_val;
                }
            }

            return rss;
        } // else construct error message 
        else {
            errormsg = "Failed to parse RSS file.";

            if (booleanval(rss)) {
                errormsg = errormsg + " (" + rss.ERROR + ")";
            }
    		// error($errormsg);

            return null;
        } // end if ($rss and !$rss->error)
    }

/*=======================================================================*\
	Function:	init
	Purpose:	setup constants with default values
				check for user overrides
\*=======================================================================*/
    public void init() {
        String ua = null;

        if (gConsts.isMAGPIE_INITALIZEDDefined()) {
            return;
        } else {
            gConsts.setMAGPIE_INITALIZED(1);
        }

        if (!gConsts.isMAGPIE_CACHE_ONDefined()) {
            gConsts.setMAGPIE_CACHE_ON(1);
        }

        if (!gConsts.isMAGPIE_CACHE_DIRDefined()) {
            gConsts.setMAGPIE_CACHE_DIR("./cache");
        }

        if (!gConsts.isMAGPIE_CACHE_AGEDefined()) {
            gConsts.setMAGPIE_CACHE_AGE(60 * 60); // one hour
        }

        if (!gConsts.isMAGPIE_CACHE_FRESH_ONLYDefined()) {
            gConsts.setMAGPIE_CACHE_FRESH_ONLY(0);
        }

        if (!gConsts.isMAGPIE_DEBUGDefined()) {
            gConsts.setMAGPIE_DEBUG(0);
        }

        if (!gConsts.isMAGPIE_USER_AGENTDefined()) {
            ua = "nWordPress/" + gVars.wp_version;

            if (booleanval(gConsts.getMAGPIE_CACHE_ON())) {
                ua = ua + ")";
            } else {
                ua = ua + "; No cache)";
            }

            gConsts.setMAGPIE_USER_AGENT(ua);
        }

        if (!gConsts.isMAGPIE_FETCH_TIME_OUTDefined()) {
            gConsts.setMAGPIE_FETCH_TIME_OUT(2);	// 2 second timeout
        }

    	// use gzip encoding to fetch rss files if supported?
        if (!gConsts.isMAGPIE_USE_GZIPDefined()) {
            gConsts.setMAGPIE_USE_GZIP(true);
        }
    }

    public boolean is_info(Object sc) {
        return (intval(sc) >= 100) && (intval(sc) < 200);
    }

    public boolean is_success(int sc) {
        return (sc >= 200) && (sc < 300);
    }

    public boolean is_redirect(Object sc) {
        return (intval(sc) >= 300) && (intval(sc) < 400);
    }

    public boolean is_error(Object sc) {
        return (intval(sc) >= 400) && (intval(sc) < 600);
    }

    public boolean is_client_error(Object sc) {
        return (intval(sc) >= 400) && (intval(sc) < 500);
    }

    public boolean is_server_error(Object sc) {
        return (intval(sc) >= 500) && (intval(sc) < 600);
    }

    public int parse_w3cdtf(String date_str) {
        String pat = null;
        Array<Object> match = new Array<Object>();
        int epoch = 0;
        int offset = 0;
        int offset_secs = 0;
        
    	// regex to match wc3dtf
        pat = "/(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2})(:(\\d{2}))?(?:([-+])(\\d{2}):?(\\d{2})|(Z))?/";

        if (QRegExPerl.preg_match(pat, date_str, match)) {
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        parse_w3cdtf_year = intval(srcArray.getValue(0));
                        parse_w3cdtf_month = intval(srcArray.getValue(1));
                        parse_w3cdtf_day = intval(srcArray.getValue(2));
                        parse_w3cdtf_hours = intval(srcArray.getValue(3));
                        parse_w3cdtf_minutes = intval(srcArray.getValue(4));
                        parse_w3cdtf_seconds = intval(srcArray.getValue(5));

                        return srcArray;
                    }
                }.doAssign(
                new Array<Object>(new ArrayEntry<Object>(match.getValue(1)), new ArrayEntry<Object>(match.getValue(2)), new ArrayEntry<Object>(match.getValue(3)),
                    new ArrayEntry<Object>(match.getValue(4)), new ArrayEntry<Object>(match.getValue(5)), new ArrayEntry<Object>(match.getValue(7))));
                
            // calc epoch for current date assuming GMT
            epoch = DateTime.gmmktime(parse_w3cdtf_hours, parse_w3cdtf_minutes, parse_w3cdtf_seconds, parse_w3cdtf_month, parse_w3cdtf_day, parse_w3cdtf_year);
            offset = 0;

            if (equal(match.getValue(11), "Z")) {
            	// zulu time, aka GMT
            } else {
                new ListAssigner<Object>() {
                        public Array<Object> doAssign(Array<Object> srcArray) {
                            if (strictEqual(srcArray, null)) {
                                return null;
                            }

                            parse_w3cdtf_tz_mod = srcArray.getValue(0);
                            parse_w3cdtf_tz_hour = intval(srcArray.getValue(1));
                            parse_w3cdtf_tz_min = intval(srcArray.getValue(2));

                            return srcArray;
                        }
                    }.doAssign(new Array<Object>(new ArrayEntry<Object>(match.getValue(8)), new ArrayEntry<Object>(match.getValue(9)), new ArrayEntry<Object>(match.getValue(10))));

        		// zero out the variables
                if (!booleanval(parse_w3cdtf_tz_hour)) {
                    parse_w3cdtf_tz_hour = 0;
                }

                if (!booleanval(parse_w3cdtf_tz_min)) {
                    parse_w3cdtf_tz_min = 0;
                }

                offset_secs = ((parse_w3cdtf_tz_hour * 60) + parse_w3cdtf_tz_min) * 60;

    			// is timezone ahead of GMT?  then subtract offset
    			//
                if (equal(parse_w3cdtf_tz_mod, "+")) {
                    offset_secs = offset_secs * (-1);
                }

                offset = offset_secs;
            }

            epoch = epoch + offset;

            return epoch;
        } else {
            return -1;
        }
    }

    public void wp_rss(String url, int num_items) {
        MagpieRSS rss = null;
        Array<Object> item = new Array<Object>();

        if (booleanval(rss = fetch_rss(url))) {
            echo(gVars.webEnv, "<ul>");

            if (!strictEqual(num_items, -1)) {
                rss.items = Array.array_slice(rss.items, 0, num_items);
            }

            for (Map.Entry javaEntry589 : rss.items.entrySet()) {
                item = (Array<Object>) javaEntry589.getValue();
                QStrings.printf(
                    gVars.webEnv,
                    "<li><a href=\"%1$s\" title=\"%2$s\">%3$s</a></li>",
                    getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(item.getValue("link")), null, "display"),
                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.strip_tags(strval(item.getValue("description")))),
                    Strings.htmlentities(strval(item.getValue("title"))));
            }

            echo(gVars.webEnv, "</ul>");
        } else {
            getIncluded(L10nPage.class, gVars, gConsts)._e("An error has occurred, which probably means the feed is down. Try again later.", "default");
        }
    }

    public boolean get_rss(String url, int num_items) { // Like get posts, but for RSS
        MagpieRSS rss = null;
        Array<Object> item = new Array<Object>();
        rss = fetch_rss(url);

        if (booleanval(rss)) {
            rss.items = Array.array_slice(rss.items, 0, num_items);

            for (Map.Entry javaEntry590 : rss.items.entrySet()) {
                item = (Array<Object>) javaEntry590.getValue();
                echo(gVars.webEnv, "<li>\n");
                echo(gVars.webEnv, "<a href=\'" + strval(item.getValue("link")) + "\' title=\'" + strval(item.getValue("description")) + "\'>");
                echo(gVars.webEnv, Strings.htmlentities(strval(item.getValue("title"))));
                echo(gVars.webEnv, "</a><br />\n");
                echo(gVars.webEnv, "</li>\n");
            }
        } else {
            return false;
        }

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_rss_block1");
        gVars.webEnv = webEnv;
        
        /**
         * MagpieRSS: a simple RSS integration tool
         *
         * A compiled file for RSS syndication
         *
         * @author Kellan Elliott-McCrea <kellan@protest.net>
         * @version 0.51
         * @license GPL
         *
         * @package External
         * @subpackage MagpieRSS
         */

        /*
         * Hook to use another RSS object instead of MagpieRSS
         */
        getIncluded(PluginPage.class, gVars, gConsts).do_action("load_feed_engine", "");
        
        gConsts.setRSS("RSS");
        gConsts.setATOM("Atom");
        gConsts.setMAGPIE_USER_AGENT("nWordPress/" + gVars.wp_version);

        return DEFAULT_VAL;
    }
}
