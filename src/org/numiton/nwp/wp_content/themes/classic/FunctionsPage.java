/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FunctionsPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_content.themes.classic;

import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.WidgetsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;


@Controller(value = "wp_content/themes/classic/FunctionsPage")
@Scope("request")
public class FunctionsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FunctionsPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/classic/functions.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/functions";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes_classic_functions_block1");
        gVars.webEnv = webEnv;

        if ( /*Modified by Numiton*/
            true) {
            getIncluded(WidgetsPage.class, gVars, gConsts).register_sidebar(
                new Array<Object>(
                    new ArrayEntry<Object>("before_widget", "<li id=\"%1$s\" class=\"widget %2$s\">"),
                    new ArrayEntry<Object>("after_widget", "</li>"),
                    new ArrayEntry<Object>("before_title", ""),
                    new ArrayEntry<Object>("after_title", "")));
        }

        return DEFAULT_VAL;
    }
}
