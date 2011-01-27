/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UtwPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin._import;

import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.ImportPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;

@Controller
@Scope("request")
public class UtwPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(UtwPage.class.getName());

	@Override
	@RequestMapping("/wp-admin/import/utw.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_admin/_import/utw";
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_admin__import_utw_block1");
		gVars.webEnv = webEnv;
		
		// create the import object
		utw_import = new UTW_Import(gVars, gConsts);
		
		// add it to the import page!
		getIncluded(ImportPage.class, gVars, gConsts).register_importer("utw", "Ultimate Tag Warrior", getIncluded(L10nPage.class, gVars, gConsts).__(
		        "Import Ultimate Tag Warrior tags into nWordPress tags.", "default"), new Array<Object>(new ArrayEntry<Object>(utw_import), new ArrayEntry<Object>("dispatch")));
		return DEFAULT_VAL;
	}

	public Object	utw_import;
}
