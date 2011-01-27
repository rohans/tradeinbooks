/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: VarsPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

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

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class VarsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(VarsPage.class.getName());
    public Array<Object> self_matches = new Array<Object>();
    public boolean is_lynx;
    public boolean is_gecko;
    public boolean is_opera;
    public boolean is_NS4;
    public boolean is_IE;

    @Override
    @RequestMapping("/wp-includes/vars.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/vars";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_vars_block1");
        gVars.webEnv = webEnv;

        /**
         * Creates common globals for the rest of WordPress
         *
         * Sets $pagenow global which is the current page. Checks
         * for the browser to set which one is currently being used.
         *
         * Detects which user environment WordPress is being used on.
         * Only attempts to check for Apache and IIS. Two web servers
         * with known permalink capability.
         *
         * @package WordPress
         */

        // On which page are we ?
        if (getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
        	// wp-admin pages are checked more carefully
            QRegExPerl.preg_match("#/wp-admin/?(.*?)$#i", gVars.PHP_SELF, self_matches);
            gVars.pagenow = strval(self_matches.getValue(1));
            gVars.pagenow = QRegExPerl.preg_replace("#\\?.*?$#", "", gVars.pagenow);

            if (strictEqual("", gVars.pagenow) || strictEqual("index", gVars.pagenow) || strictEqual("index.php", gVars.pagenow)) {
                gVars.pagenow = "index.php";
            } else {
                QRegExPerl.preg_match("#(.*?)(/|$)#", gVars.pagenow, self_matches);
                gVars.pagenow = Strings.strtolower(strval(self_matches.getValue(1)));

                if (!strictEqual(".php", Strings.substr(gVars.pagenow, -4, 4))) {
                    gVars.pagenow = gVars.pagenow + ".php"; // for Options +Multiviews: /wp-admin/themes/index.php (themes.php is queried)
                }
            }
        } else {
            if (QRegExPerl.preg_match("#([^/]+\\.php)([?/].*?)?$#i", gVars.PHP_SELF, self_matches)) {
                gVars.pagenow = Strings.strtolower(strval(self_matches.getValue(1)));
            } else {
                gVars.pagenow = "index.php";
            }
        }

        // Simple browser detection
        is_lynx = is_gecko = gVars.is_winIE = gVars.is_macIE = is_opera = is_NS4 = false;

        if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Lynx"), BOOLEAN_FALSE)) {
            is_lynx = true;
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Gecko"), BOOLEAN_FALSE)) {
            is_gecko = true;
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "MSIE"), BOOLEAN_FALSE) && !strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Win"), BOOLEAN_FALSE)) {
            gVars.is_winIE = true;
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "MSIE"), BOOLEAN_FALSE) && !strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Mac"), BOOLEAN_FALSE)) {
            gVars.is_macIE = true;
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Opera"), BOOLEAN_FALSE)) {
            is_opera = true;
        } else if (!strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Nav"), BOOLEAN_FALSE) && !strictEqual(Strings.strpos(gVars.webEnv.getHttpUserAgent(), "Mozilla/4."), BOOLEAN_FALSE)) {
            is_NS4 = true;
        }

        is_IE = gVars.is_macIE || gVars.is_winIE;
        
        // Server detection

        /**
         * Whether the server software is Apache or something else
         * @global bool $is_apache
         */
        gVars.is_apache = ((!strictEqual(Strings.strpos(gVars.webEnv.getServerSoftware(), "Apache"), BOOLEAN_FALSE) ||
            !strictEqual(Strings.strpos(gVars.webEnv.getServerSoftware(), "LiteSpeed"), BOOLEAN_FALSE))
            ? true
            : false);
        
        /**
         * Whether the server software is IIS or something else
         * @global bool $is_IIS
         */
        gVars.is_IIS = ((!strictEqual(Strings.strpos(gVars.webEnv.getServerSoftware(), "Microsoft-IIS"), BOOLEAN_FALSE))
            ? true
            : false);

        return DEFAULT_VAL;
    }
}
