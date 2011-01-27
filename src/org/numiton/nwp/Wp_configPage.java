/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_configPage.java,v 1.2 2008/10/14 13:15:49 numiton Exp $
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;

@Controller
@Scope("request")
public class Wp_configPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Wp_configPage.class.getName());

	public static final String WP_CONFIG_PROPERTIES_FILE = "wp-config.properties";
	public static final String DB_TABLE_PREFIX_KEY = "DB_TABLE_PREFIX";
	public static final String DB_HOST_KEY = "DB_HOST";
	public static final String DB_PASSWORD_KEY = "DB_PASSWORD";
	public static final String DB_USER_KEY = "DB_USER";
	public static final String DB_NAME_KEY = "DB_NAME";
	public static final String SECRET_KEY = "SECRET_KEY";
	
	
	/* Added by Numiton */
	private static Properties iniProps;
	
	@Override
	@RequestMapping("/wp-config.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_config";
	}

	// Added by Numiton
	private static boolean readiniFile(GlobalVars gVars) throws IOException {
		
		File iniFile = new File(gVars.webEnv.getNonNullRequest().getSession().getServletContext().getRealPath("/") 
								+ "/" + WP_CONFIG_PROPERTIES_FILE);
		String iniFileContents = FileUtils.readFileToString(iniFile, null);
		
		iniProps = new Properties();
		iniProps.load(new ByteArrayInputStream(iniFileContents.getBytes()));
		
		return true;
	}
	
	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_config_block1");
		gVars.webEnv = webEnv;
		
		// Modified by Numiton. Loading data from wp-config.properties
		if(iniProps == null) {
			if(!readiniFile(gVars)) {
				return DEFAULT_VAL;
			}
		}
		
		// ** MySQL settings ** //
		gConsts.setDB_NAME(iniProps.getProperty(DB_NAME_KEY));    // The name of the database
		gConsts.setDB_USER(iniProps.getProperty(DB_USER_KEY));     // Your MySQL username
		gConsts.setDB_PASSWORD(iniProps.getProperty(DB_PASSWORD_KEY)); // ...and password
		gConsts.setDB_HOST(iniProps.getProperty(DB_HOST_KEY));    // 99% chance you won't need to change this value
		gConsts.setDB_CHARSET("utf8");
		gConsts.setDB_COLLATE("");
		
		// Change SECRET_KEY to a unique phrase.  You won't have to remember it later,
		// so make it long and complicated.  You can visit http://api.wordpress.org/secret-key/1.0/
		// to get a secret key generated for you, or just make something up.
		gConsts.setSECRET_KEY(iniProps.getProperty(SECRET_KEY));
		
		// You can have multiple installations in one database if you give each a unique prefix
		gVars.table_prefix = iniProps.getProperty(DB_TABLE_PREFIX_KEY); // Only numbers, letters, and underscores please!
		
		// Change this to localize WordPress.  A corresponding MO file for the
		// chosen language must be installed to wp-content/languages.
		// For example, install de.mo to wp-content/languages and set WPLANG to 'de'
		// to enable German language support.
		gConsts.setWPLANG("");
		
		/* That's all, stop editing! Happy blogging. */
		gConsts.setABSPATH(webEnv.getNonNullRequest().getSession().getServletContext().getRealPath("/") + "/");
		
		/* Condensed dynamic construct */
		requireOnce(gVars, gConsts, Wp_settingsPage.class);
		
		return DEFAULT_VAL;
	}
}
