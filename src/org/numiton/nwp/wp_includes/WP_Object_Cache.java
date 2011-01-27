/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Object_Cache.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;


/**
 * WordPress Object Cache
 *
 * The WordPress Object Cache is used to save on trips to the database.
 * The Object Cache stores all of the cache data to memory and makes the
 * cache contents available by using a key, which is used to name and
 * later retrieve the cache contents.
 *
 * The Object Cache can be replaced by other caching mechanisms by placing
 * files in the wp-content folder which is looked at in wp-settings. If
 * that file exists, then this file will not be included.
 *
 * @package WordPress
 * @subpackage Cache
 * @since 2.0
 */
public class WP_Object_Cache implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Object_Cache.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    /**
     * Holds the cached objects
     * @var array
     * @access private
     * @since 2.0
     */
    public Array<Object> cache = new Array<Object>();

    /**
     * Cache objects that do not exist in the cache
     * @var array
     * @access private
     * @since 2.0
     */
    public Array<Object> non_existant_objects = new Array<Object>();

    /**
     * Object caches that are global
     * @var array
     * @access private
     * @since 2.0
     */
    public Array<Object> global_groups = new Array<Object>(new ArrayEntry<Object>("users"), new ArrayEntry<Object>("userlogins"), new ArrayEntry<Object>("usermeta"));

    /**
     * The amount of times the cache data was already stored in the cache.
     * @since 2.5
     * @access private
     * @var int
     */
    public int cache_hits = 0;

    /**
     * Amount of times the cache did not have the request in cache
     * @var int
     * @access public
     * @since 2.0
     */
    public int cache_misses = 0;

    // Commented by Numiton
    //	public WP_Object_Cache(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
    //		setContext(javaGlobalVariables, javaGlobalConstants);
    //
    //		return this.__construct();
    //	}
    /**
     * Sets up object properties; PHP 5 style constructor
     * @since 2.0.8
     * @return null|WP_Object_Cache If cache is disabled, returns null.
     */
    public WP_Object_Cache(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        FunctionHandling.register_shutdown_function(gVars.webEnv, new Callback("__destruct", this)); /** @todo This should be moved to the PHP4 style constructor, PHP5 already calls __destruct() */
    }

    /**
     * Adds data to the cache if it doesn't already exist.
     * @uses WP_Object_Cache::get Checks to see if the cache already has data.
     * @uses WP_Object_Cache::set Sets the data after the checking the cache
     * contents existance.
     * @since 2.0
     * @param int|string $id What to call the contents in the cache
     * @param mixed $data The contents to store in the cache
     * @param string $group Where to group the cache contents
     * @param int $expire When to expire the cache contents
     * @return bool False if cache ID and group already exists, true on success
     */
    public boolean add(Object id, Object data, String group, int expire) {
        if (empty(group)) {
            group = "default";
        }

        if (!strictEqual(null, this.get(id, group)))/*, false*/
         {
            return false;
        }

        return this.set(id, data, group, expire);
    }

    public boolean delete(Object id) {
        return delete(id, "default", false);
    }

    public boolean delete(Object id, String group) {
        return delete(id, group, false);
    }

	/**
	 * Remove the contents of the cache ID in the group
	 *
	 * If the cache ID does not exist in the group and $force parameter
	 * is set to false, then nothing will happen. The $force parameter
	 * is set to false by default.
	 *
	 * On success the group and the id will be added to the
	 * $non_existant_objects property in the class.
	 *
	 * @since 2.0
	 *
	 * @param int|string $id What the contents in the cache are called
	 * @param string $group Where the cache contents are grouped
	 * @param bool $force Optional. Whether to force the unsetting of the cache ID in the group
	 * @return bool False if the contents weren't deleted and true on success
	 */
    public boolean delete(Object id, String group, boolean force) {
        if (empty(group)) {
            group = "default";
        }

        if (!force && strictEqual(null, this.get(id, group)))/*, false*/
         {
            return false;
        }

        this.cache.getArrayValue(group).arrayUnset(id);
        this.non_existant_objects.getArrayValue(group).putValue(id, true);

        return true;
    }

    /**
     * Clears the object cache of all data
     * @since 2.0
     * @return bool Always returns true
     */
    public boolean flush() {
        this.cache = new Array<Object>();

        return true;
    }

    public Object get(Object id) {
        return get(id, "default");
    }

	/**
	 * Retrieves the cache contents, if it exists
	 *
	 * The contents will be first attempted to be retrieved by searching
	 * by the ID in the cache group. If the cache is hit (success) then
	 * the contents are returned.
	 *
	 * On failure, the $non_existant_objects property is checked and if
	 * the cache group and ID exist in there the cache misses will not be
	 * incremented. If not in the nonexistant objects property, then the
	 * cache misses will be incremented and the cache group and ID will
	 * be added to the nonexistant objects.
	 *
	 * @since 2.0
	 *
	 * @param int|string $id What the contents in the cache are called
	 * @param string $group Where the cache contents are grouped
	 * @return bool|mixed False on failure to retrieve contents or the cache contents on success
	 */
    public Object get(Object id, String group) {
        if (empty(group)) {
            group = "default";
        }

        if (isset(this.cache.getArrayValue(group).getValue(id))) {
            this.cache_hits = this.cache_hits + 1;

            return this.cache.getArrayValue(group).getValue(id);
        }

        if (isset(this.non_existant_objects.getArrayValue(group).getValue(id))) {
            return null;
        }

        this.non_existant_objects.getArrayValue(group).putValue(id, true);
        this.cache_misses = this.cache_misses + 1;

        return null;
    }

    /**
     * Replace the contents in the cache, if contents already exist
     * @since 2.0
     * @see WP_Object_Cache::set()
     * @param int|string $id What to call the contents in the cache
     * @param mixed $data The contents to store in the cache
     * @param string $group Where to group the cache contents
     * @param int $expire When to expire the cache contents
     * @return bool False if not exists, true if contents were replaced
     */
    public boolean replace(Object id, Object data, String group, int expire) {
        if (empty(group)) {
            group = "default";
        }

        if (strictEqual(null, this.get(id, group)))/*, false*/
         {
            return false;
        }

        return this.set(id, data, group, expire);
    }

	/**
	 * Sets the data contents into the cache
	 *
	 * The cache contents is grouped by the $group parameter followed
	 * by the $id. This allows for duplicate ids in unique groups.
	 * Therefore, naming of the group should be used with care and
	 * should follow normal function naming guidelines outside of
	 * core WordPress usage.
	 *
	 * The $expire parameter is not used, because the cache will
	 * automatically expire for each time a page is accessed and PHP
	 * finishes. The method is more for cache plugins which use files.
	 *
	 * @since 2.0
	 *
	 * @param int|string $id What to call the contents in the cache
	 * @param mixed $data The contents to store in the cache
	 * @param string $group Where to group the cache contents
	 * @param int $expire Not Used
	 * @return bool Always returns true
	 */
    public boolean set(Object id, Object data, String group, int expire) {
        if (empty(group)) {
            group = "default";
        }

        if (strictEqual(null, data)) {
            data = "";
        }

        this.cache.getArrayValue(group).putValue(id, data);

        if (isset(this.non_existant_objects.getArrayValue(group).getValue(id))) {
            this.non_existant_objects.getArrayValue(group).arrayUnset(id);
        }

        return true;
    }

    /**
     * Echos the stats of the caching.
     * Gives the cache hits, and cache misses. Also prints every cached group,
     * key and the data.
     * @since 2.0
     */
    public void stats() {
        Object group = null;
        Object cache = null;
        echo(gVars.webEnv, "<p>");
        echo(gVars.webEnv, "<strong>Cache Hits:</strong> " + strval(this.cache_hits) + "<br />");
        echo(gVars.webEnv, "<strong>Cache Misses:</strong> " + strval(this.cache_misses) + "<br />");
        echo(gVars.webEnv, "</p>");

        for (Map.Entry javaEntry384 : this.cache.entrySet()) {
            group = javaEntry384.getKey();
            cache = javaEntry384.getValue();
            echo(gVars.webEnv, "<p>");
            echo(gVars.webEnv, "<strong>Group:</strong> " + strval(group) + "<br />");
            echo(gVars.webEnv, "<strong>Cache:</strong>");
            echo(gVars.webEnv, "<pre>");
            print_r(gVars.webEnv, cache);
            echo(gVars.webEnv, "</pre>");
        }
    }

    /**
     * Will save the object cache before object is completely destroyed.
     * Called upon object destruction, which should be when PHP ends.
     * @since 2.0.8
     * @return bool True value. Won't be used by PHP
     */
    public boolean __destruct() {
        return true;
    }

    protected void finalize() throws Throwable {
        __destruct();
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
