/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: LoggerPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils;

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
public class LoggerPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(LoggerPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/js/tinymce/plugins/spellchecker/classes/utils/Logger.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/plugins/spellchecker/classes/utils/Logger";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_plugins_spellchecker_classes_utils_Logger_block1");
        gVars.webEnv = webEnv;
        
        /**
         * $Id: LoggerPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
         *
         * @package MCFileManager.filesystems
         * @author Moxiecode
         * @copyright Copyright © 2005, Moxiecode Systems AB, All rights reserved.
         */

        // File type contstants
        gConsts.setMC_LOGGER_DEBUG(0);
        gConsts.setMC_LOGGER_INFO(10);
        gConsts.setMC_LOGGER_WARN(20);
        gConsts.setMC_LOGGER_ERROR(30);
        gConsts.setMC_LOGGER_FATAL(40);

        return DEFAULT_VAL;
    }
}
