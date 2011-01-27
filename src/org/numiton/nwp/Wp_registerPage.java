/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_registerPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.PluggablePage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;

@Controller
@Scope("request")
public class Wp_registerPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Wp_registerPage.class.getName());

	@Override
	@RequestMapping("/wp-register.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_register";
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_register_block1");
		gVars.webEnv = webEnv;
		
		// This file is deprecated, but you shouldn't have been linking to it directly anyway :P
		// Use wp_register() to create a registration link instead, it's much better ;)
		require(gVars, gConsts, Wp_configPage.class);
		getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("wp-login.php?action=register", 302);
		return DEFAULT_VAL;
	}
}
