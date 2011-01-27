/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_blog_headerPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
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

import com.numiton.SourceCodeInfo;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Wp_blog_headerPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_blog_headerPage.class.getName());
    public Object wp_did_header;

    @Override
    @RequestMapping("/wp-blog-header.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_blog_header";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_blog_header_block1");
        gVars.webEnv = webEnv;

        if (!isset(wp_did_header)) {
            if (!FileSystemOrSocket.file_exists(gVars.webEnv, FileSystemOrSocket.dirname(SourceCodeInfo.getCurrentFile(gVars.webEnv)) + "/wp-config.php")) {
                if (!strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "wp-admin"), BOOLEAN_FALSE)) {
                    gVars.path = "";
                } else {
                    gVars.path = "wp-admin/";
                }

                /* Condensed dynamic construct: 236806 */ requireOnce(gVars, gConsts, ClassesPage.class);
                /* Condensed dynamic construct: 236817 */ requireOnce(gVars, gConsts, FunctionsPage.class);
                /* Condensed dynamic construct: 236828 */ requireOnce(gVars, gConsts, PluginPage.class);
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                        "There doesn\'t seem to be a <code>wp-config.php</code> file. I need this before we can get started. Need more help? <a href=\'http://codex.wordpress.org/Editing_wp-config.php\'>We got it</a>. You can create a <code>wp-config.php</code> file through a web interface, but this doesn\'t work for all server setups. The safest way is to manually create the file.</p><p><a href=\'" +
                        gVars.path + "setup-config.php\' class=\'button\'>Create a Configuration File</a>",
                        "nWordPress &rsaquo; Error");
            }

            wp_did_header = true;
            /* Condensed dynamic construct: 237518 */ requireOnce(gVars, gConsts, Wp_configPage.class);
            getIncluded(FunctionsPage.class, gVars, gConsts).wp("");
            /* Condensed dynamic construct */
            requireOnce(gVars, gConsts, Template_loaderPage.class);
        } else {
        }

        return DEFAULT_VAL;
    }
}
