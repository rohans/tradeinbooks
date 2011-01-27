/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_dbPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.isset;
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
public class Wp_dbPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_dbPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/wp-db.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/wp_db";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_wp_db_block1");
        gVars.webEnv = webEnv;
        
		//  WordPress DB Class
		
		//  ORIGINAL CODE FROM:
		//  Justin Vincent (justin@visunet.ie)
		//    	http://php.justinvincent.com
        gConsts.setEZSQL_VERSION("WP1.25");

        /*
         * Case insensitive constants are not supported, ignoring flag: 503430
         */
        gConsts.setOBJECT("OBJECT");

        /*
         * Case insensitive constants are not supported, ignoring flag: 503440
         */
        gConsts.setOBJECT_K("OBJECT_K");

        /*
         * Case insensitive constants are not supported, ignoring flag: 503450
         */
        gConsts.setARRAY_A("ARRAY_A");

        /*
         * Case insensitive constants are not supported, ignoring flag: 503460
         */
        gConsts.setARRAY_N("ARRAY_N");

        if (!gConsts.isSAVEQUERIESDefined()) {
            gConsts.setSAVEQUERIES(false);
        }

        if (!isset(gVars.wpdb)) {
            gVars.wpdb = new wpdb(gVars, gConsts, gConsts.getDB_USER(), gConsts.getDB_PASSWORD(), gConsts.getDB_NAME(), gConsts.getDB_HOST());
        }

        return DEFAULT_VAL;
    }
}
