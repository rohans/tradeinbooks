/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Menu_headerPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.PluginPage;
import org.numiton.nwp.wp_includes.CapabilitiesPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Menu_headerPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Menu_headerPage.class.getName());
    public String self;
    public String menu_hook;
    public Array<String> side_items = new Array<String>();

    @Override
    @RequestMapping("/wp-admin/menu-header.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/menu_header";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_menu_header_block1");
        gVars.webEnv = webEnv;
        
        self = QRegExPerl.preg_replace("|^.*/wp-admin/|i", "", gVars.webEnv.getPhpSelf());
        self = QRegExPerl.preg_replace("|^.*/plugins/|i", "", self);
        
        getIncluded(PluginPage.class, gVars, gConsts).get_admin_page_parent();

        // We're going to do this loop three times
        
        /* Start of block */
        super.startBlock("__wp_admin_menu_header_block2");

        for (Map.Entry javaEntry278 : gVars.menu.entrySet()) {
            gVars.key = strval(javaEntry278.getKey());
            gVars.item = (Array<Object>) javaEntry278.getValue();

            if (3 < intval(gVars.key)) { // get each menu item before 3
                continue;
            }

            gVars._class = "";
            
            // 0 = name, 1 = capability, 2 = file
            if ((equal(Strings.strcmp(self, strval(gVars.item.getValue(2))), 0) && empty(gVars.parent_file)) || (booleanval(gVars.parent_file) && equal(gVars.item.getValue(2), gVars.parent_file))) {
                gVars._class = " class=\"current\"";
            }

            if (!empty(gVars.submenu.getValue(gVars.item.getValue(2)))) /*
            * Re-index. Re-index.
            */ {
                gVars.submenu.putValue(gVars.item.getValue(2), Array.array_values(gVars.submenu.getArrayValue(gVars.item.getValue(2))));  // Re-index.
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts)
                                .get_plugin_page_hook(strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)), strval(gVars.item.getValue(2)));

                if (FileSystemOrSocket.file_exists(
                            gVars.webEnv,
                            gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2))) || !empty(menu_hook)) {
                    echo(
                            gVars.webEnv,
                            "\n\t<li><a href=\'admin.php?page=" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a></li>");
                } else {
                    echo(
                            gVars.webEnv,
                            "\n\t<li><a href=\'" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a></li>");
                }
            } else if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(gVars.item.getValue(1)))) {
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts).get_plugin_page_hook(strval(gVars.item.getValue(2)), "admin.php");

                if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.item.getValue(2))) || !empty(menu_hook)) {
                    echo(gVars.webEnv, "\n\t<li><a href=\'admin.php?page=" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                } else {
                    echo(gVars.webEnv, "\n\t<li><a href=\'" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                }
            }
        }

        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("dashmenu", "");

        /* Start of block */
        super.startBlock("__wp_admin_menu_header_block3");

        for (Map.Entry javaEntry279 : gVars.menu.entrySet()) {
            gVars.key = strval(javaEntry279.getKey());
            gVars.item = (Array<Object>) javaEntry279.getValue();

            if ((5 > intval(gVars.key)) || (intval(gVars.key) > 25)) { // get each menu item before 3
                continue;
            }

            gVars._class = "";

            // 0 = name, 1 = capability, 2 = file
            if ((equal(Strings.strcmp(self, strval(gVars.item.getValue(2))), 0) && empty(gVars.parent_file)) || (booleanval(gVars.parent_file) && equal(gVars.item.getValue(2), gVars.parent_file))) {
                gVars._class = " class=\"current\"";
            }

            if (!empty(gVars.submenu.getValue(gVars.item.getValue(2)))) {
                gVars.submenu.putValue(gVars.item.getValue(2), Array.array_values(gVars.submenu.getArrayValue(gVars.item.getValue(2))));  // Re-index.
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts)
                                .get_plugin_page_hook(strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)), strval(gVars.item.getValue(2)));

                if (FileSystemOrSocket.file_exists(
                            gVars.webEnv,
                            gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2))) || !empty(menu_hook)) {
                    echo(
                            gVars.webEnv,
                            "\n\t<li><a href=\'admin.php?page=" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a></li>");
                } else {
                    echo(
                            gVars.webEnv,
                            "\n\t<li><a href=\'" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a></li>");
                }
            } else if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(gVars.item.getValue(1)))) {
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts).get_plugin_page_hook(strval(gVars.item.getValue(2)), "admin.php");

                if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.item.getValue(2))) || !empty(menu_hook)) {
                    echo(gVars.webEnv, "\n\t<li><a href=\'admin.php?page=" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                } else {
                    echo(gVars.webEnv, "\n\t<li><a href=\'" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                }
            }
        }

        for (Map.Entry javaEntry280 : gVars.menu.entrySet()) {
            gVars.key = strval(javaEntry280.getKey());
            gVars.item = (Array<Object>) javaEntry280.getValue();

            if (intval(gVars.key) < 41) { // there is a more efficient way to do this!
                continue;
            }

            gVars._class = "";

            // 0 = name, 1 = capability, 2 = file
            if ((equal(Strings.strcmp(self, strval(gVars.item.getValue(2))), 0) && empty(gVars.parent_file)) || (booleanval(gVars.parent_file) && equal(gVars.item.getValue(2), gVars.parent_file))) {
                gVars._class = " class=\"current\"";
            }

            if (!empty(gVars.submenu.getValue(gVars.item.getValue(2)))) {
                gVars.submenu.putValue(gVars.item.getValue(2), Array.array_values(gVars.submenu.getArrayValue(gVars.item.getValue(2))));  // Re-index.
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts)
                                .get_plugin_page_hook(strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)), strval(gVars.item.getValue(2)));

                if (FileSystemOrSocket.file_exists(
                            gVars.webEnv,
                            gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2))) || !empty(menu_hook)) {
                    echo(
                            gVars.webEnv,
                            "\n\t<li><a href=\'admin.php?page=" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a></li>");
                } else {
                    echo(
                            gVars.webEnv,
                            "\n\t<li><a href=\'" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a></li>");
                }
            } else if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(gVars.item.getValue(1)))) {
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts).get_plugin_page_hook(strval(gVars.item.getValue(2)), "admin.php");

                if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.item.getValue(2))) || !empty(menu_hook)) {
                    echo(gVars.webEnv, "\n\t<li><a href=\'admin.php?page=" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                } else {
                    echo(gVars.webEnv, "\n\t<li><a href=\'" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                }
            }
        }

        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("adminmenu", "");

        /* Start of block */
        super.startBlock("__wp_admin_menu_header_block4");
        side_items = new Array<String>();

        for (Map.Entry javaEntry281 : gVars.menu.entrySet()) {
            gVars.key = strval(javaEntry281.getKey());
            gVars.item = (Array<Object>) javaEntry281.getValue();

            if ((26 > intval(gVars.key)) || (intval(gVars.key) > 40)) {
                continue;
            }

            gVars._class = "";

            // 0 = name, 1 = capability, 2 = file
            if ((equal(Strings.strcmp(self, strval(gVars.item.getValue(2))), 0) && empty(gVars.parent_file)) || (booleanval(gVars.parent_file) && equal(gVars.item.getValue(2), gVars.parent_file))) {
                gVars._class = " class=\"current\"";
            }

            if (!empty(gVars.submenu.getValue(gVars.item.getValue(2)))) {
                gVars.submenu.putValue(gVars.item.getValue(2), Array.array_values(gVars.submenu.getArrayValue(gVars.item.getValue(2))));  // Re-index.
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts)
                                .get_plugin_page_hook(strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)), strval(gVars.item.getValue(2)));

                if (FileSystemOrSocket.file_exists(
                            gVars.webEnv,
                            gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2))) || !empty(menu_hook)) {
                    side_items.putValue(
                            "\n\t<li><a href=\'admin.php?page=" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a>");
                } else {
                    side_items.putValue(
                            "\n\t<li><a href=\'" + strval(gVars.submenu.getArrayValue(gVars.item.getValue(2)).getArrayValue(0).getValue(2)) + "\'" + gVars._class + ">" +
                            strval(gVars.item.getValue(0)) + "</a>");
                }
            } else if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(gVars.item.getValue(1)))) {
                menu_hook = getIncluded(PluginPage.class, gVars, gConsts).get_plugin_page_hook(strval(gVars.item.getValue(2)), "admin.php");

                if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.item.getValue(2))) || !empty(menu_hook)) {
                    side_items.putValue("\n\t<li><a href=\'admin.php?page=" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a>");
                } else {
                    side_items.putValue("\n\t<li><a href=\'" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a>");
                }
            }
        }

        echo(gVars.webEnv, Strings.implode(" </li>", side_items) + "</li>");
        side_items = null;
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("sidemenu", "");

        /* Start of block */
        super.startBlock("__wp_admin_menu_header_block5");

        // Sub-menu
        if (isset(gVars.submenu.getValue(gVars.parent_file))) {
            echo(gVars.webEnv, "<ul id=\"submenu\">\n");

            for (Map.Entry javaEntry282 : (Set<Map.Entry>) gVars.submenu.getArrayValue(gVars.parent_file).entrySet()) {
                gVars.item = (Array<Object>) javaEntry282.getValue();

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can(strval(gVars.item.getValue(1)))) {
                    continue;
                }

                if (isset(gVars.submenu_file)) {
                    if (equal(gVars.submenu_file, gVars.item.getValue(2))) {
                        gVars._class = " class=\"current\"";
                    } else {
                        gVars._class = "";
                    }
                } else if ((isset(gVars.plugin_page) && equal(gVars.plugin_page, gVars.item.getValue(2))) || (!isset(gVars.plugin_page) && equal(self, gVars.item.getValue(2)))) {
                    gVars._class = " class=\"current\"";
                } else {
                    gVars._class = "";
                }

                menu_hook = getIncluded(PluginPage.class, gVars, gConsts).get_plugin_page_hook(strval(gVars.item.getValue(2)), gVars.parent_file);

                if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + gConsts.getPLUGINDIR() + "/" + strval(gVars.item.getValue(2))) || !empty(menu_hook)) {
                    if (equal("admin.php", gVars.pagenow)) {
                        echo(gVars.webEnv, "\n\t<li><a href=\'admin.php?page=" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                    } else {
                        echo(gVars.webEnv,
                            "\n\t<li><a href=\'" + gVars.parent_file + "?page=" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                    }
                } else {
                    echo(gVars.webEnv, "\n\t<li><a href=\'" + strval(gVars.item.getValue(2)) + "\'" + gVars._class + ">" + strval(gVars.item.getValue(0)) + "</a></li>");
                }
            }

            echo(gVars.webEnv, "\n</ul>\n");
        } else {
            echo(gVars.webEnv, "<div id=\"minisub\"></div>\n");
        }

        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("admin_notices", "");

        return DEFAULT_VAL;
    }
}
