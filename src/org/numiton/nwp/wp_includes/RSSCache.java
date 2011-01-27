/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: RSSCache.java,v 1.3 2008/10/03 18:45:30 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.DateTime;
import com.numiton.error.ErrorHandling;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.string.Strings;


public class RSSCache implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(RSSCache.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String BASE_CACHE = "wp-content/cache";	// where the cache files are stored
    public int MAX_AGE = 43200;  		// when are files stale, default twelve hours
    public String ERROR = "";			// accumulate error messages
    
    public RSSCache(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object base, Object age) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        if (booleanval(base)) {
            this.BASE_CACHE = strval(base);
        }

        if (booleanval(age)) {
            this.MAX_AGE = intval(age);
        }
    }

/*=======================================================================*\
	Function:	set
	Purpose:	add an item to the cache, keyed on url
	Input:		url from wich the rss file was fetched
	Output:		true on sucess
\*=======================================================================*/
    public String set(String url, Object rss) {
        String cache_option = null;
        String cache_timestamp = null;
        
        cache_option = "rss_" + this.file_name(url);
        cache_timestamp = "rss_" + this.file_name(url) + "_ts";

		// shouldn't these be using get_option() ?
        if (!booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT option_name FROM " + gVars.wpdb.options + " WHERE option_name = %s", cache_option)))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option(cache_option, "", "", "no");
        }

        if (!booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT option_name FROM " + gVars.wpdb.options + " WHERE option_name = %s", cache_timestamp)))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option(cache_timestamp, "", "", "no");
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option(cache_option, rss);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option(cache_timestamp, DateTime.time());

        return cache_option;
    }

/*=======================================================================*\
	Function:	get
	Purpose:	fetch an item from the cache
	Input:		url from wich the rss file was fetched
	Output:		cached object on HIT, false on MISS
\*=======================================================================*/
    public Object get(String url) {
        String cache_option = null;
        Object rss = null;
        this.ERROR = "";
        cache_option = "rss_" + this.file_name(url);

        if (!booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option(cache_option))) {
            this.debug("Cache doesn\'t contain: " + url + " (cache option: " + cache_option + ")");

            return null;
        }

        rss = getIncluded(FunctionsPage.class, gVars, gConsts).get_option(cache_option);

        return rss;
    }

/*=======================================================================*\
	Function:	check_cache
	Purpose:	check a url for membership in the cache
				and whether the object is older then MAX_AGE (ie. STALE)
	Input:		url from wich the rss file was fetched
	Output:		cached object on HIT, false on MISS
\*=======================================================================*/
    public String check_cache(String url) {
        String cache_option = null;
        String cache_timestamp = null;
        Object mtime = null;
        int age = 0;
        this.ERROR = "";
        cache_option = this.file_name(url);
        cache_timestamp = "rss_" + this.file_name(url) + "_ts";

        if (booleanval(mtime = getIncluded(FunctionsPage.class, gVars, gConsts).get_option(cache_timestamp))) {
			// find how long ago the file was added to the cache
			// and whether that is longer then MAX_AGE
            age = DateTime.time() - intval(mtime);

            if (this.MAX_AGE > age) {
				// object exists and is current
                return "HIT";
            } else {
				// object exists but is old
                return "STALE";
            }
        } else {
			// object does not exist
            return "MISS";
        }
    }

    // Commented by Numiton: infinite recursive loops
//    /**
//     * =======================================================================*\
//     * Function: serialize
//     * \*=======================================================================
//     */
//    public String serialize(Object rss) {
//        return serialize(rss);
//    }
//
//    /**
//     * =======================================================================*\
//     * Function: unserialize
//     * \*=======================================================================
//     */
//    public Object unserialize(Object data) {
//        return unserialize(data);
//    }

    /**
     * =======================================================================*\
     * Function: file_name Purpose: map url to location in cache Input: url from
     * wich the rss file was fetched Output: a file name
     * \*=======================================================================
     */
    public String file_name(String url) {
        return Strings.md5(url);
    }

    /**
     * =======================================================================*\
     * Function: error Purpose: register error
     * \*=======================================================================
     */
    public void error(String errormsg, int lvl) {
		// append PHP's error message if track_errors enabled
        if (isset(ErrorHandling.getLastErrorMessage(gVars.webEnv))) {
            errormsg = errormsg + " (" + ErrorHandling.getLastErrorMessage(gVars.webEnv) + ")";
        }

        this.ERROR = errormsg;

        if (booleanval(gConsts.getMAGPIE_DEBUG())) {
            ErrorHandling.trigger_error(gVars.webEnv, errormsg, lvl);
        } else {
            ErrorHandling.error_log(gVars.webEnv, errormsg, 0);
        }
    }

    public void debug(String debugmsg) {
        debug(debugmsg, ErrorHandling.E_USER_NOTICE);
    }

    public void debug(String debugmsg, int lvl) {
        if (booleanval(gConsts.getMAGPIE_DEBUG())) {
            this.error("MagpieRSS [debug] " + debugmsg, lvl);
        }
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
