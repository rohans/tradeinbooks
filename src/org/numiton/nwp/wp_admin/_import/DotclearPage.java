/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: DotclearPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin._import;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.CommonInterface4;
import org.numiton.nwp.CommonInterface5;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.ImportPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Iconv;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class DotclearPage extends NumitonController implements CommonInterface4, CommonInterface5 {
    protected static final Logger LOG = Logger.getLogger(DotclearPage.class.getName());
    public Object dc_import;

    @Override
    @RequestMapping("/wp-admin/import/dotclear.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/_import/dotclear";
    }
    
    /*
     * DotClear import plugin
     * by Thomas Quinot - http://thomas.quinot.org/
     */

    /**
    	Add These Functions to make our lives easier
    **/

    public Object get_comment_count(int post_ID) {
        return gVars.wpdb.get_var("SELECT count(*) FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = " + post_ID);
    }

    public Object link_exists(String linkname) {
        return gVars.wpdb.get_var("SELECT link_id FROM " + gVars.wpdb.links + " WHERE link_name = \"" + linkname + "\"");
    }

    /*
	 * Identify UTF-8 text Taken from
	 * http://www.php.net/manual/fr/function.mb-detect-encoding.php#50087
	 */
	//
	// utf8 encoding validation developed based on Wikipedia entry at:
	// http://en.wikipedia.org/wiki/UTF-8
	//
	// Implemented as a recursive descent parser based on a simple state machine
	// copyright 2005 Maarten Meijer
	//
	// This cries out for a C-implementation to be included in PHP core
	//

    public boolean valid_1byte(int _char) {
        if (!is_int(_char)) {
            return false;
        }

        return equal(_char & 128, 0);
    }

    public boolean valid_2byte(int _char) {
        if (!is_int(_char)) {
            return false;
        }

        return equal(_char & 224, 192);
    }

    public boolean valid_3byte(int _char) {
        if (!is_int(_char)) {
            return false;
        }

        return equal(_char & 240, 224);
    }

    public boolean valid_4byte(int _char) {
        if (!is_int(_char)) {
            return false;
        }

        return equal(_char & 248, 240);
    }

    public boolean valid_nextbyte(int _char) {
        if (!is_int(_char)) {
            return false;
        }

        return equal(_char & 192, 128);
    }

    public boolean valid_utf8(String string) {
        int len = 0;
        int i = 0;
        int _char = 0;
        len = Strings.strlen(string);
        i = 0;

        while (i < len) {
            _char = Strings.ord(Strings.substr(string, i++, 1));

            if (valid_1byte(_char)) {    // continue
                continue;
            } else if (valid_2byte(_char)) { // check 1 byte
                if (!valid_nextbyte(Strings.ord(Strings.substr(string, i++, 1)))) {
                    return false;
                }
            } else if (valid_3byte(_char)) { // check 2 bytes
                if (!valid_nextbyte(Strings.ord(Strings.substr(string, i++, 1)))) {
                    return false;
                }

                if (!valid_nextbyte(Strings.ord(Strings.substr(string, i++, 1)))) {
                    return false;
                }
            } else if (valid_4byte(_char)) { // check 3 bytes
                if (!valid_nextbyte(Strings.ord(Strings.substr(string, i++, 1)))) {
                    return false;
                }

                if (!valid_nextbyte(Strings.ord(Strings.substr(string, i++, 1)))) {
                    return false;
                }

                if (!valid_nextbyte(Strings.ord(Strings.substr(string, i++, 1)))) {
                    return false;
                }
            } // goto next char
        }

        return true; // done
    }

    public String csc(String s) {
        if (valid_utf8(s)) {
            return s;
        } else {
            return Iconv.iconv(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dccharset")), "UTF-8", s);
        }
    }

    public String textconv(String s) {
        return csc(QRegExPerl.preg_replace("|(?<!<br />)\\s*\\n|", " ", s));
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin__import_dotclear_block1");
        gVars.webEnv = webEnv;

        // Removed by Numiton. All functions are declared.
        dc_import = new Dotclear_Import(gVars, gConsts);
        getIncluded(ImportPage.class, gVars, gConsts).register_importer(
            "dotclear",
            getIncluded(L10nPage.class, gVars, gConsts).__("DotClear", "default"),
            getIncluded(L10nPage.class, gVars, gConsts).__("Import categories, users, posts, comments, and links from a DotClear blog.", "default"),
            new Array<Object>(new ArrayEntry<Object>(dc_import), new ArrayEntry<Object>("dispatch")));

        return DEFAULT_VAL;
    }
}
