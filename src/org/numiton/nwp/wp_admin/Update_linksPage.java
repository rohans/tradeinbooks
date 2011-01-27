/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Update_linksPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.INVALID_RESOURCE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.Wp_configPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.Ref;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Update_linksPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Update_linksPage.class.getName());
    public Array link_uris;
    public String http_request;
    public String response;
    public int fs;
    public Ref<Integer> errno = new Ref<Integer>();
    public Ref<String> errstr = new Ref<String>();
    public String body;
    public Array<String> returns;
    public String _return;
    public Object uri;

    @Override
    @RequestMapping("/wp-admin/update-links.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/update_links";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_update_links_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, Wp_configPage.class);

        if (!booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("use_linksupdate"))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Feature disabled.", "default"), "");
        }

        link_uris = gVars.wpdb.get_col("SELECT link_url FROM " + gVars.wpdb.links);

        if (!booleanval(link_uris)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("No links", "default"), "");
        }

        String link_urisStr = URL.urlencode(Strings.join(link_uris, "\n"));
        gVars.query_string = "uris=" + link_urisStr;
        http_request = "POST /updated-batch/ HTTP/1.0\r\n";
        http_request = http_request + "Host: api.pingomatic.com\r\n";
        http_request = http_request + "Content-Type: application/x-www-form-urlencoded; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\r\n";
        http_request = http_request + "Content-Length: " + strval(Strings.strlen(gVars.query_string)) + "\r\n";
        http_request = http_request + "User-Agent: nWordPress/" + gVars.wp_version + "\r\n";
        http_request = http_request + "\r\n";
        http_request = http_request + gVars.query_string;
        response = "";

        if (!strictEqual(INVALID_RESOURCE, fs = FileSystemOrSocket.fsockopen(gVars.webEnv, "api.pingomatic.com", 80, errno, errstr, 5))) {
            FileSystemOrSocket.fwrite(gVars.webEnv, fs, http_request);

            while (!FileSystemOrSocket.feof(gVars.webEnv, fs))
                response = response + FileSystemOrSocket.fgets(gVars.webEnv, fs, 1160); // One TCP-IP packet

            FileSystemOrSocket.fclose(gVars.webEnv, fs);

            Array<String> responseArray = Strings.explode("\r\n\r\n", response, 2);
            body = Strings.trim(responseArray.getValue(1));
            body = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("\r\n"), new ArrayEntry<Object>("\r")), "\n", body);
            returns = Strings.explode("\n", body);

            for (Map.Entry javaEntry306 : returns.entrySet()) {
                _return = strval(javaEntry306.getValue());
                gVars.time = gVars.wpdb.escape(Strings.substr(_return, 0, 19));
                uri = gVars.wpdb.escape(QRegExPerl.preg_replace("/(.*?) | (.*?)/", "$2", _return));
                gVars.wpdb.query("UPDATE " + gVars.wpdb.links + " SET link_updated = \'" + gVars.time + "\' WHERE link_url = \'" + uri + "\'");
            }
        }

        return DEFAULT_VAL;
    }
}
