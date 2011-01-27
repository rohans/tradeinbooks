/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ConfigPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker;

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
public class ConfigPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(ConfigPage.class.getName());

	@Override
	@RequestMapping("/wp-includes/js/tinymce/plugins/spellchecker/config.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_includes/js/tinymce/plugins/spellchecker/config";
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {
		gVars.webEnv = webEnv;
		
		// General settings
		gVars.config.putValue("general.engine", "GoogleSpell");
		
		//$config['general.engine'] = 'PSpell';
		//$config['general.engine'] = 'PSpellShell';

		// PSpell settings
		gVars.config.putValue("PSpell.mode", "PSPELL_FAST");
		gVars.config.putValue("PSpell.spelling", "");
		gVars.config.putValue("PSpell.jargon", "");
		gVars.config.putValue("PSpell.encoding", "");
		
		// PSpellShell settings
		gVars.config.putValue("PSpellShell.mode", "PSPELL_FAST");
		gVars.config.putValue("PSpellShell.aspell", "/usr/bin/aspell");
		gVars.config.putValue("PSpellShell.tmp", "/tmp");
		
		// Windows PSpellShell settings
		//$config['PSpellShell.aspell'] = '"c:\Program Files\Aspell\bin\aspell.exe"';
		//$config['PSpellShell.tmp'] = 'c:/temp';
		return DEFAULT_VAL;
	}
}
