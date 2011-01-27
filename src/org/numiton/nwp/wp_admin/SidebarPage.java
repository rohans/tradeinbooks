/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: SidebarPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class SidebarPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(SidebarPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/sidebar.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/sidebar";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_sidebar_block1");
        gVars.webEnv = webEnv;
        gVars.mode = "sidebar";
        requireOnce(gVars, gConsts, AdminPage.class);

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Cheatin&#8217; uh?", "default"), "");
        }

        if (equal("b", gVars.webEnv._GET.getValue("a"))) {
            echo(
                    gVars.webEnv,
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");
            echo(gVars.webEnv, "; charset=UTF-8\" />\n<title>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress &#8250; Posted", "default");
            echo(gVars.webEnv, "</title>\n");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("wp-admin");
            echo(gVars.webEnv, "</head>\n<body>\n\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Posted !", "default");
            echo(gVars.webEnv, "</p>\n\t<p>");
            QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"%s\">Click here</a> to post again.", "default"), "sidebar.php");
            echo(gVars.webEnv, "</p>\n</body>\n</html>");
        } else {
            echo(
                    gVars.webEnv,
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");
            echo(gVars.webEnv, "; charset=");
            getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("blog_charset");
            echo(gVars.webEnv, "\" />\n<title>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress &#8250; Sidebar", "default");
            echo(gVars.webEnv, "</title>\n");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
            getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("wp-admin");
            echo(
                    gVars.webEnv,
                    "<style type=\"text/css\" media=\"screen\">\nform {\n\tpadding: 3px;\n}\n.sidebar-categories {\n\tdisplay: block;\n\theight: 6.6em;\n\toverflow: auto;\n\tbackground-color: #f4f4f4;\n}\n.sidebar-categories label {\n\tfont-size: 10px;\n\tdisplay: block;\n\twidth: 90%;\n}\n</style>\n</head>\n<body id=\"sidebar\">\n<h1 id=\"wphead\"><a href=\"http://wordpress.org/\" rel=\"external\">WordPress</a></h1>\n<form name=\"post\" action=\"post.php\" method=\"post\">\n<div>\n<input type=\"hidden\" name=\"action\" value=\"post\" />\n<input type=\"hidden\" name=\"user_ID\" value=\"");
            echo(gVars.webEnv, gVars.user_ID);
            echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"mode\" value=\"sidebar\" />\n");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-post", "_wpnonce", true, true);
            echo(gVars.webEnv, "<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Title:", "default");
            echo(gVars.webEnv, "<input type=\"text\" name=\"post_title\" size=\"20\" tabindex=\"1\" style=\"width: 100%;\" />\n</p>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Categories:", "default");
            echo(gVars.webEnv, "<span class=\"sidebar-categories\">\n");
            getIncluded(TemplatePage.class, gVars, gConsts).dropdown_categories(0, 0, new Array<Object>());
            echo(
                    gVars.webEnv,
                    "</span>\n</p>\n<p>\nPost:\n<textarea rows=\"8\" cols=\"12\" style=\"width: 100%\" name=\"content\" tabindex=\"2\"></textarea>\n</p>\n<p>\n\t<input name=\"saveasdraft\" type=\"submit\" id=\"saveasdraft\" tabindex=\"9\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Save as Draft", "default");
            echo(gVars.webEnv, "\" />\n");

            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
                echo(gVars.webEnv, "\t<input name=\"publish\" type=\"submit\" id=\"publish\" tabindex=\"6\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Publish", "default");
                echo(gVars.webEnv, "\" class=\"button button-highlighted\" />\n");
            } else {
            }

            echo(gVars.webEnv, "</p>\n</div>\n</form>\n\n</body>\n</html>\n");
        }

        return DEFAULT_VAL;
    }
}
