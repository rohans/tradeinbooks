/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Link_addPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.BookmarkPage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.numiton.nwp.wp_includes.Script_loaderPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Link_addPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Link_addPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/link-add.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/link_add";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_link_add_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, AdminPage.class);
        
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Add Link", "default");
        gVars.this_file = "link-manager.php";
        gVars.parent_file = "post-new.php";
        
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this,
            new Array<Object>(new ArrayEntry<Object>("action"),
                new ArrayEntry<Object>("cat_id"),
                new ArrayEntry<Object>("linkurl"),
                new ArrayEntry<Object>("name"),
                new ArrayEntry<Object>("image"),
                new ArrayEntry<Object>("description"),
                new ArrayEntry<Object>("visible"),
                new ArrayEntry<Object>("target"),
                new ArrayEntry<Object>("category"),
                new ArrayEntry<Object>("link_id"),
                new ArrayEntry<Object>("submit"),
                new ArrayEntry<Object>("order_by"),
                new ArrayEntry<Object>("links_show_cat_id"),
                new ArrayEntry<Object>("rating"),
                new ArrayEntry<Object>("rel"),
                new ArrayEntry<Object>("notes"),
                new ArrayEntry<Object>("linkcheck[]")));
        
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("link", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("xfn", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("thickbox", false, new Array<Object>(), false);
        require(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_link_add_block2");

        if (booleanval(gVars.webEnv._GET.getValue("added")) && !equal("", gVars.webEnv._POST.getValue("link_name"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Link added.", "default");
            echo(gVars.webEnv, "</p></div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_link_add_block3");
        gVars.link = getIncluded(BookmarkPage.class, gVars, gConsts).get_default_link_to_edit();
        include(gVars, gConsts, Edit_link_formPage.class);
        require(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
