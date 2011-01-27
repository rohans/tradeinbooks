/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_appPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;

@Controller
@Scope("request")
public class Wp_appPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Wp_appPage.class.getName());
	public boolean	              app_logging;
	public AtomServer	          server;

	@Override
	@RequestMapping("/wp-app.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_app";
	}

	public void log_app(String label, String msg) {
		int fp = 0;
		String date = null;
		if (app_logging) {
			fp = FileSystemOrSocket.fopen(gVars.webEnv, "wp-app.log", "a+");
			date = DateTime.gmdate("Y-m-d H:i:s");
			FileSystemOrSocket.fwrite(gVars.webEnv, fp, "\n\n" + date + " - " + label + "\n" + msg + "\n");
			FileSystemOrSocket.fclose(gVars.webEnv, fp);
		}
	}

	public WP_User wp_set_current_user(int id, String name) {
		if (isset(gVars.current_user) && equal(id, gVars.current_user.getID())) {
			return gVars.current_user;
		}
		gVars.current_user = new WP_User(gVars, gConsts, id, name);
		return gVars.current_user;
	}

	public Array<String> wa_posts_where_include_drafts_filter(Array<String> where) {
		where = Strings.str_replace("post_status = \'publish\'", "post_status = \'publish\' OR post_status = \'future\' OR post_status = \'draft\' OR post_status = \'inherit\'", where);
		return where;
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_app_block1");
		gVars.webEnv = webEnv;
		
		/*
		 * wp-app.php - Atom Publishing Protocol support for WordPress
		 * Original code by: Elias Torres, http://torrez.us/archives/2006/08/31/491/
		 * Modified by: Dougal Campbell, http://dougal.gunters.org/
		 *
		 * Version: 1.0.5-dc
		 */
		
		gConsts.setAPP_REQUEST(true);
		requireOnce(gVars, gConsts, Wp_configPage.class);

		/* Condensed dynamic construct */
		requireOnce(gVars, gConsts, Post_templatePage.class);

		/* Condensed dynamic construct */
		requireOnce(gVars, gConsts, FeedPage.class);
		gVars.webEnv._SERVER.putValue("PATH_INFO", QRegExPerl.preg_replace("/.*\\/wp-app\\.php/", "", gVars.webEnv.getRequestURI()));
		app_logging = false;
		
		// TODO: Should be an option somewhere
		gVars.always_authenticate = 1;
		
		getIncluded(PluginPage.class, gVars, gConsts).add_filter("posts_where", Callback.createCallbackArray(this, "wa_posts_where_include_drafts_filter"), 10, 1);
		server = new AtomServer(gVars, gConsts);
		server.handle_request();
		return DEFAULT_VAL;
	}
}
