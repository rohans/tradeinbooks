/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Post_newPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Post_newPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Post_newPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/post-new.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/post_new";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_post_new_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Create New Post", "default");
        gVars.parent_file = "post-new.php";
        gVars.editing = true;
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("autosave", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("post", false, new Array<Object>(), false);

        if (booleanval(getIncluded(General_templatePage.class, gVars, gConsts).user_can_richedit())) {
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("editor", false, new Array<Object>(), false);
        }

        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("thickbox", false, new Array<Object>(), false);
        getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("media-upload", false, new Array<Object>(), false);
        requireOnce(gVars, gConsts, Admin_headerPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            echo(gVars.webEnv, "<div class=\"wrap\">\n<p>");
            QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "Since you&#8217;re a newcomer, you&#8217;ll have to wait for an admin to add the <code>edit_posts</code> capability to your user, in order to be authorized to post.<br />\nYou can also <a href=\"mailto:%s?subject=Promotion?\">e-mail the admin</a> to ask for a promotion.<br />\nWhen you&#8217;re promoted, just reload this page and you&#8217;ll be able to blog. :)",
                            "default"),
                    getIncluded(FunctionsPage.class, gVars, gConsts).get_option("admin_email"));
            echo(gVars.webEnv, "</p>\n</div>\n");
            include(gVars, gConsts, Admin_footerPage.class);
            System.exit();
        }

        if (isset(gVars.webEnv._GET.getValue("posted")) && booleanval(gVars.webEnv._GET.getValue("posted"))) {
            gVars.webEnv._GET.putValue("posted", intval(gVars.webEnv._GET.getValue("posted")));
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p><strong>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Your post has been saved.", "default");
            echo(gVars.webEnv, "</strong> <a href=\"");
            echo(gVars.webEnv, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.webEnv._GET.getValue("posted"), false));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("View post", "default");
            echo(gVars.webEnv, "</a> | <a href=\"post.php?action=edit&amp;post=");
            echo(gVars.webEnv, gVars.webEnv._GET.getValue("posted"));
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Edit post", "default");
            echo(gVars.webEnv, "</a></p></div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_post_new_block2");
        
        // Show post form.
        gVars.post = getIncluded(PostPage.class, gVars, gConsts).get_default_post_to_edit();
        include(gVars, gConsts, Edit_form_advancedPage.class);
        
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
