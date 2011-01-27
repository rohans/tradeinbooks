/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UpdatePage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_includes;

import static com.numiton.PhpCommonConstants.*;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class UpdatePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(UpdatePage.class.getName());

    @Override
    @RequestMapping("/wp-includes/update.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/update";
    }

    /**
     * A simple set of functions to check our version 1.0 update service
     *
     * @since 2.3 wp_version_check() - Check WordPress version against the
     * newest version.
     * The WordPress version, PHP version, and Locale is sent. Checks against
     * the WordPress server at api.wordpress.org server. Will only check if PHP
     * has fsockopen enabled and WordPress isn't installing.
     *
     * @since 2.3
     * @uses $wp_version Used to check against the newest WordPress version.
     * @return mixed Returns null if update is unsupported. Returns false if
     * check is too soon.
     */
    public boolean wp_version_check() {
        String php_version = null;
        StdClass current = null;
        String locale = null;
        StdClass new_option = null;
        String http_request = null;
        String response = null;
        int fs = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        String body = null;
        Array<String> returns = new Array<String>();

        if ( /* Commented by Numiton: !Unsupported.function_exists("fsockopen") ||*/
            !strictEqual(Strings.strpos(gVars.webEnv.getPhpSelf(), "install.php"), BOOLEAN_FALSE) || gConsts.isWP_INSTALLINGDefined()) {
            return false;
        }

        php_version = Options.phpversion();

        Object currentObj = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("update_core");

        // Added by Numiton
        if (empty(currentObj)) {
            current = new StdClass();
        } else {
            current = (StdClass) currentObj;
        }

        locale = getIncluded(L10nPage.class, gVars, gConsts).get_locale();

        if (isset(StdClass.getValue(current, "last_checked")) && (43200 > (DateTime.time() - intval(StdClass.getValue(current, "last_checked")))) &&
                equal(StdClass.getValue(current, "version_checked"), gVars.wp_version)) {
            return false;
        }

        new_option = new StdClass();
        new_option.fields.putValue("last_checked", DateTime.time()); // this gets set whether we get a response or not, so if something is down or misconfigured it won't delay the page load for more than 3 seconds, twice a day
        new_option.fields.putValue("version_checked", gVars.wp_version);
        http_request = "GET /core/version-check/1.1/?version=" + gVars.wp_version + "&php=" + php_version + "&locale=" + locale + " HTTP/1.0\r\n";
        http_request = http_request + "Host: api.wordpress.org\r\n";
        http_request = http_request + "Content-Type: application/x-www-form-urlencoded; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset") + "\r\n";
        http_request = http_request + "User-Agent: nWordPress/" + gVars.wp_version + "; " + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "\r\n";
        http_request = http_request + "\r\n";
        response = "";

        if (!strictEqual(INVALID_RESOURCE, fs = FileSystemOrSocket.fsockopen(gVars.webEnv, "api.wordpress.org", 80, errno, errstr, 3)) && is_resource(fs))/*
         * One
         * TCP-IP
         * packet
         */
         {
            FileSystemOrSocket.fwrite(gVars.webEnv, fs, http_request);

            while (!FileSystemOrSocket.feof(gVars.webEnv, fs))
                response = response + FileSystemOrSocket.fgets(gVars.webEnv, fs, 1160); // One TCP-IP packet

            FileSystemOrSocket.fclose(gVars.webEnv, fs);

            Array<String> responseArray = Strings.explode("\r\n\r\n", response, 2);

            if (!QRegExPerl.preg_match("|HTTP/.*? 200|", responseArray.getValue(0))) {
                return false;
            }

            body = Strings.trim(responseArray.getValue(1));
            body = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("\r\n"), new ArrayEntry<Object>("\r")), "\n", body);
            returns = Strings.explode("\n", body);
            new_option.fields.putValue("response", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(returns.getValue(0)));

            if (isset(returns.getValue(1))) {
                new_option.fields.putValue("url", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(returns.getValue(1), null, "display"));
            }

            if (isset(returns.getValue(2))) {
                new_option.fields.putValue("current", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(returns.getValue(2)));
            }
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("update_core", new_option);

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_update_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_action("init", Callback.createCallbackArray(this, "wp_version_check"), 10, 1);

        return DEFAULT_VAL;
    }
}
