/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Class_ftpPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_admin.includes;

import static com.numiton.VarHandling.equal;
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

import com.numiton.Options;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Class_ftpPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Class_ftpPage.class.getName());
    public Object mod_sockets;

    @Override
    @RequestMapping("/wp-admin/includes/class-ftp.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/class_ftp";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_class_ftp_block1");
        gVars.webEnv = webEnv;

        /**
         * PemFTP - A Ftp implementation in pure PHP
         *
         * @package PemFTP
         * @since 2.5
         *
         * @version 1.0
         * @copyright Alexey Dotsenko
         * @author Alexey Dotsenko
         * @link http://www.phpclasses.org/browse/package/1743.html Site
         * @license LGPL License http://www.opensource.org/licenses/lgpl-license.html
         */
        if (!gConsts.isCRLFDefined()) {
            gConsts.setCRLF("\r\n");
        }

        if (!gConsts.isFTP_AUTOASCIIDefined()) {
            gConsts.setFTP_AUTOASCII(-1);
        }

        if (!gConsts.isFTP_BINARYDefined()) {
            gConsts.setFTP_BINARY(1);
        }

        if (!gConsts.isFTP_ASCIIDefined()) {
            gConsts.setFTP_ASCII(0);
        }

        if (!gConsts.isFTP_FORCEDefined()) {
            gConsts.setFTP_FORCE(true);
        }

        gConsts.setFTP_OS_Unix("u");
        gConsts.setFTP_OS_Windows("w");
        gConsts.setFTP_OS_Mac("m");
        mod_sockets = true;

        if (!Options.extension_loaded("sockets")) {
            gVars.prefix = (equal("PHP_SHLIB_SUFFIX", "dll")
                ? "php_"
                : "");

            if (!false)/*Modified by Numiton*/
             {
                mod_sockets = false;
            }
        }

        // Commented by Numiton: Useless import
        return DEFAULT_VAL;
    }
}
