/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: XmlrpcPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_admin.includes.AdminPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class XmlrpcPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(XmlrpcPage.class.getName());
    public Object xmlrpc_logging;
    public Object wp_xmlrpc_server;

    @Override
    @RequestMapping("/xmlrpc.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "xmlrpc";
    }

    public boolean logIO(String io, String msg) {
        int fp = 0;
        String date = null;
        String iot = null;

        if (booleanval(xmlrpc_logging)) {
            fp = FileSystemOrSocket.fopen(gVars.webEnv, "../xmlrpc.log", "a+");
            date = DateTime.gmdate("Y-m-d H:i:s ");
            iot = (equal(io, "I")
                ? " Input: "
                : " Output: ");
            FileSystemOrSocket.fwrite(gVars.webEnv, fp, "\n\n" + date + iot + msg);
            FileSystemOrSocket.fclose(gVars.webEnv, fp);
        }

        return true;
    }

    public String starify(String string) {
        int i = 0;
        i = Strings.strlen(string);

        return Strings.str_repeat("*", i);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__xmlrpc_block1");
        gVars.webEnv = webEnv;

        gConsts.setXMLRPC_REQUEST(true);

        // Some browser-embedded clients send cookies. We don't want them.
        gVars.webEnv._COOKIE = new Array<Object>();

        // Commented by Numiton. Useless in Java
        // A bug in PHP < 5.2.2 makes $HTTP_RAW_POST_DATA not set by default,
        // but we can do it ourself.
        //		if (!isset(gVars.webEnv.HTTP_RAW_POST_DATA)) {
        //			gVars.webEnv.HTTP_RAW_POST_DATA = FileSystemOrSocket.file_get_contents(gVars.webEnv, "php://input");
        //		}

        // fix for mozBlog and other cases where '<?xml' isn't on the very first line
        if (isset(gVars.webEnv.HTTP_RAW_POST_DATA)) {
            gVars.webEnv.HTTP_RAW_POST_DATA = Strings.trim(gVars.webEnv.HTTP_RAW_POST_DATA);
        }

        include(gVars, gConsts, Wp_configPage.class);

        if (isset(gVars.webEnv._GET.getValue("rsd"))) { // http://archipelago.phrasewise.com/rsd
            Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")), true);
            echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")) + "\"?" + ">");
            echo(
                    gVars.webEnv,
                    "<rsd version=\"1.0\" xmlns=\"http://archipelago.phrasewise.com/rsd\">\n  <service>\n    <engineName>WordPress</engineName>\n    <engineLink>http://wordpress.org/</engineLink>\n    <homePageLink>");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");
            echo(gVars.webEnv, "</homePageLink>\n    <apis>\n      <api name=\"WordPress\" blogID=\"1\" preferred=\"true\" apiLink=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("wpurl");
            echo(gVars.webEnv, "/xmlrpc.php\" />\n      <api name=\"Movable Type\" blogID=\"1\" preferred=\"false\" apiLink=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("wpurl");
            echo(gVars.webEnv, "/xmlrpc.php\" />\n      <api name=\"MetaWeblog\" blogID=\"1\" preferred=\"false\" apiLink=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("wpurl");
            echo(gVars.webEnv, "/xmlrpc.php\" />\n      <api name=\"Blogger\" blogID=\"1\" preferred=\"false\" apiLink=\"");
            getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("wpurl");
            echo(gVars.webEnv, "/xmlrpc.php\" />\n      <api name=\"Atom\" blogID=\"\" preferred=\"false\" apiLink=\"");
            echo(
                gVars.webEnv,
                getIncluded(PluginPage.class, gVars, gConsts)
                    .apply_filters("atom_service_url", getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "/wp-app.php/service"));
            echo(gVars.webEnv, "\" />\n    </apis>\n  </service>\n</rsd>\n");
            System.exit();
        }

        /* Condensed dynamic construct */
        includeOnce(gVars, gConsts, AdminPage.class);

        // Turn off all warnings and errors.
        // error_reporting(0);
        gVars.post_default_title = ""; // posts submitted via the xmlrpc interface get that title

        xmlrpc_logging = 0;

        if (isset(gVars.webEnv.HTTP_RAW_POST_DATA)) {
            logIO("I", gVars.webEnv.HTTP_RAW_POST_DATA);
        }

        wp_xmlrpc_server = new wp_xmlrpc_server(gVars, gConsts);

        return DEFAULT_VAL;
    }
}
