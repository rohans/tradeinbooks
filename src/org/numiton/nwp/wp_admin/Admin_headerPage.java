/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Admin_headerPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Admin_headerPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Admin_headerPage.class.getName());
    public Array<Object> min_width_pages;
    public Object the_current_page;
    public boolean ie6_no_scrollbar;

    @Override
    @RequestMapping("/wp-admin/admin-header.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/admin_header";
    }

    public String add_minwidth(Object c) {
        return strval(c) + "minwidth ";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block1");
        gVars.webEnv = webEnv;
        Network.header(
            gVars.webEnv,
            "Content-Type: " + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("html_type") + "; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        if (!isset(gVars.webEnv._GET.getValue("page"))) {
            requireOnce(gVars, gConsts, AdminPage.class);
        }

        if (gVars.editing) {
            if (booleanval(getIncluded(General_templatePage.class, gVars, gConsts).user_can_richedit())) {
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("wp_tiny_mce", false, new Array<Object>(), false);
            }
        }

        min_width_pages = new Array<Object>(
                new ArrayEntry<Object>("post.php"),
                new ArrayEntry<Object>("post-new.php"),
                new ArrayEntry<Object>("page.php"),
                new ArrayEntry<Object>("page-new.php"),
                new ArrayEntry<Object>("widgets.php"),
                new ArrayEntry<Object>("comment.php"),
                new ArrayEntry<Object>("link.php"));
        the_current_page = QRegExPerl.preg_replace("|^.*/wp-admin/|i", "", gVars.webEnv.getPhpSelf());
        ie6_no_scrollbar = true;

        if (Array.in_array(the_current_page, min_width_pages)) {
            ie6_no_scrollbar = false;
            getIncluded(PluginPage.class, gVars, gConsts).add_filter("admin_body_class", Callback.createCallbackArray(this, "add_minwidth"), 10, 1);
        }

        (((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).get_admin_page_title();

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block2");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_xml_ns", "");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block4");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block5");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block6");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block7");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.strip_tags(gVars.title), strval(0)));

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block8");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("wp-admin");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/colors");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block9");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/ie");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block10");

        if (!equal(gVars.parent_file, "link-manager.php") && !equal(gVars.parent_file, "options-general.php") && ie6_no_scrollbar) {
            echo(gVars.webEnv, "<style type=\"text/css\">* html { overflow-x: hidden; }</style>\n");
        } else {
        }

        if (isset(gVars.page_hook)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_print_scripts-" + gVars.page_hook, "");
        } else if (isset(gVars.plugin_page)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_print_scripts-" + gVars.plugin_page, "");
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_print_scripts", "");

        if (isset(gVars.page_hook)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_head-" + gVars.page_hook, "");
        } else if (isset(gVars.plugin_page)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_head-" + gVars.plugin_page, "");
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_head", "");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block11");
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("admin_body_class", ""));

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block12");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block13");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"))));

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block14");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Visit Site", "default");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block15");
        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Howdy, <a href=\"%1$s\">%2$s</a>!", "default"), "profile.php", gVars.user_identity);

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block16");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Log Out", "default");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Log Out", "default");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("<a href=\"http://codex.wordpress.org/\">Help</a>", "default");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e("<a href=\"http://wordpress.org/support/\">Forums</a>", "default");

        /* Start of block */
        super.startBlock("__wp_admin_admin_header_block21");
        /* Condensed dynamic construct */
        require(gVars, gConsts, Menu_headerPage.class);

        if (equal(gVars.parent_file, "options-general.php")) {
            /* Condensed dynamic construct */
            require(gVars, gConsts, Options_headPage.class);
        }

        return DEFAULT_VAL;
    }
}
