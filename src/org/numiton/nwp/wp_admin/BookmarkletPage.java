/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: BookmarkletPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import com.numiton.URL;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class BookmarkletPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(BookmarkletPage.class.getName());
    public Object a;
    public String popuptitle;
    public String text;
    public Object popupurl;

    @Override
    @RequestMapping("/wp-admin/bookmarklet.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/bookmarklet";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block1");
        gVars.webEnv = webEnv;
        gVars.mode = "bookmarklet";
        requireOnce(gVars, gConsts, AdminPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
        }

        if (equal("b", a)) {
            echo(
                    gVars.webEnv,
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<script type=\"text/javascript\">\n<!--\nwindow.close()\n-->\n</script>\n</head>\n<body></body>\n</html>\n");
            System.exit();
        } else {
        }

        gVars.post = getIncluded(PostPage.class, gVars, gConsts).get_default_post_to_edit();
        popuptitle = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, popuptitle), strval(0));
        text = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, URL.urldecode(text)), strval(0));
        popuptitle = getIncluded(FormattingPage.class, gVars, gConsts).funky_javascript_fix(popuptitle);
        text = getIncluded(FormattingPage.class, gVars, gConsts).funky_javascript_fix(text);
        gVars.post_title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.webEnv._REQUEST.getValue("post_title")), strval(0));

        if (!empty(gVars.post_title)) {
            gVars.post.fields.putValue("post_title", Strings.stripslashes(gVars.webEnv, gVars.post_title));
        } else {
            gVars.post.fields.putValue("post_title", popuptitle);
        }

        gVars.content = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(gVars.webEnv._REQUEST.getValue("content")), strval(0));
        popupurl = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(gVars.webEnv._REQUEST.getValue("popupurl")), null, "display");

        if (!empty(gVars.content)) {
            gVars.post.fields.putValue(
                "post_content",
                getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("content"))), strval(0)));
        } else {
            gVars.post.fields.putValue("post_content", "<a href=\"" + strval(popupurl) + "\">" + popuptitle + "</a>" + "\n" + text);
        }

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block2");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block4");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block5");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("wp-admin");

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block6");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block7");
        require(gVars, gConsts, Edit_formPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_bookmarklet_block8");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_footer", "");

        return DEFAULT_VAL;
    }
}
