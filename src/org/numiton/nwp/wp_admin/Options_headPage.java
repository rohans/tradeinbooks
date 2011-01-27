/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_headPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.MiscPage;
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
public class Options_headPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_headPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/options-head.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_head";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_head_block1");
        gVars.webEnv = webEnv;
        getIncluded(MiscPage.class, gVars, gConsts)
            .wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action"), new ArrayEntry<Object>("standalone"), new ArrayEntry<Object>("option_group_id")));

        /* Start of block */
        super.startBlock("__wp_admin_options_head_block2");

        if (isset(gVars.webEnv._GET.getValue("updated"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p><strong>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Settings saved.", "default");
            echo(gVars.webEnv, "</strong></p></div>\n");
        } else {
        }

        return DEFAULT_VAL;
    }
}
