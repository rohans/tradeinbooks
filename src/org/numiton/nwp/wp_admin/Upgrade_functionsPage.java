/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Upgrade_functionsPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.UpgradePage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.SourceCodeInfo;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Upgrade_functionsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Upgrade_functionsPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/upgrade-functions.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/upgrade_functions";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_upgrade_functions_block1");
        gVars.webEnv = webEnv;
        
        // Deprecated.  Use includes/upgrade.php.
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_file(FileSystemOrSocket.basename(SourceCodeInfo.getCurrentFile(gVars.webEnv)), "2.5", "wp-admin/includes/upgrade.php");
        /* Condensed dynamic construct */
        requireOnce(gVars, gConsts, UpgradePage.class);

        return DEFAULT_VAL;
    }
}
