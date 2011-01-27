/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CachePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.generic.PhpWeb.DEFAULT_VAL;

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

import com.numiton.generic.PhpWebEnvironment;

@Controller
@Scope("request")
public class CachePage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(CachePage.class.getName());

	@Override
	@RequestMapping("/wp-includes/cache.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_includes/cache";
	}

	/**
	 * Object Cache API
	 *
	 * @package WordPress
	 * @subpackage Cache
	 */

	/**
	 * wp_cache_add() - Adds data to the cache, if the cache key doesn't aleady exist
	 *
	 * @since 2.0
	 * @uses $wp_object_cache Object Cache Class
	 * @see WP_Object_Cache::add()
	 *
	 * @param int|string $key The cache ID to use for retrieval later
	 * @param mixed $data The data to add to the cache store
	 * @param string $flag The group to add the cache to
	 * @param int $expire When the cache data should be expired
	 * @return unknown
	 */
	public Object wp_cache_add(Object key, Object data, String flag, int expire) {
		// Modified by Numiton
		if(wp_object_cache != null) {
			return wp_object_cache.add(key, data, flag, expire);
		}
		
		return null;
	}

	/**
	 * * wp_cache_close() - Closes the cache
	 * 
	 * This function has ceased to do anything since WordPress 2.5. The
	 * functionality was removed along with the rest of the persistant cache.
	 * 
	 * @since 2.0
	 * 
	 * @return bool Always returns True
	 * 
	 */
	public boolean wp_cache_close() {
		return true;
	}

	/**
	 * * wp_cache_delete() - Removes the cache contents matching ID and flag
	 * 
	 * @since 2.0
	 * @uses $wp_object_cache Object Cache Class
	 * @see WP_Object_Cache::delete()
	 * 
	 * @param int|string $id What the contents in the cache are called
	 * @param string $flag Where the cache contents are grouped
	 * @return bool True on successful removal, false on failure
	 * 
	 */
	public boolean wp_cache_delete(Object id, String flag) {
		return wp_object_cache.delete(id, flag);
	}

	/**
	 * * wp_cache_flush() - Removes all cache items
	 * 
	 * @since 2.0
	 * @uses $wp_object_cache Object Cache Class
	 * @see WP_Object_Cache::flush()
	 * 
	 * @return bool Always returns true
	 * 
	 */
	public boolean wp_cache_flush() {
		return wp_object_cache.flush();
	}

	/**
	 * * wp_cache_get() - Retrieves the cache contents from the cache by ID and
	 * flag
	 * 
	 * @since 2.0
	 * @uses $wp_object_cache Object Cache Class
	 * @see WP_Object_Cache::get()
	 * 
	 * @param int|string $id What the contents in the cache are called
	 * @param string $flag Where the cache contents are grouped
	 * @return bool|mixed False on failure to retrieve contents or the cache
	 *         contents on success
	 * 
	 */
	public Object wp_cache_get(Object id, String flag) {
		// Modified by Numiton
		if(wp_object_cache != null) {
			return wp_object_cache.get(id, flag);
		} else {
			return null;
		}
	}

	/**
	 * * wp_cache_init() - Sets up Object Cache Global and assigns it
	 * 
	 * @since 2.0
	 * @global WP_Object_Cache $wp_object_cache nWordPress Object Cache
	 * 
	 */
	public void wp_cache_init() {
		wp_object_cache = new WP_Object_Cache(gVars, gConsts);
	}

	/**
	 * * wp_cache_replace() - Replaces the contents of the cache with new data
	 * 
	 * @since 2.0
	 * @uses $wp_object_cache Object Cache Class
	 * @see WP_Object_Cache::replace()
	 * 
	 * @param int|string $id What to call the contents in the cache
	 * @param mixed $data The contents to store in the cache
	 * @param string $flag Where to group the cache contents
	 * @param int $expire When to expire the cache contents
	 * @return bool False if cache ID and group already exists, true on success
	 * 
	 */
	public boolean wp_cache_replace(Object key, Object data, String flag, int expire) {
		return wp_object_cache.replace(key, data, flag, expire);
	}

	/**
	 * * wp_cache_set() - Saves the data to the cache
	 * 
	 * @since 2.0
	 * @uses $wp_object_cache Object Cache Class
	 * @see WP_Object_Cache::set()
	 * 
	 * @param int|string $id What to call the contents in the cache
	 * @param mixed $data The contents to store in the cache
	 * @param string $flag Where to group the cache contents
	 * @param int $expire When to expire the cache contents
	 * @return bool False if cache ID and group already exists, true on success
	 * 
	 */
	public boolean wp_cache_set(Object key, Object data, String flag, int expire) {
		return wp_object_cache.set(key, data, flag, expire);
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {
		gVars.webEnv = webEnv;
		return DEFAULT_VAL;
	}

	public WP_Object_Cache	wp_object_cache;
}
