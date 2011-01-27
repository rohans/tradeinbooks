/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_commentsrss2Page.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.empty;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.Feed_rss2_commentsPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;

@Controller
@Scope("request")
public class Wp_commentsrss2Page extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Wp_commentsrss2Page.class.getName());

	@Override
	@RequestMapping("/wp-commentsrss2.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_commentsrss2";
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_commentsrss2_block1");
		gVars.webEnv = webEnv;
		if (empty(gVars.wp)) {
			requireOnce(gVars, gConsts, Wp_configPage.class);
			getIncluded(FunctionsPage.class, gVars, gConsts).wp("feed=rss2&withcomments=1");
		}
		/* Condensed dynamic construct */
		require(gVars, gConsts, Feed_rss2_commentsPage.class);
		
		return DEFAULT_VAL;
	}
}
