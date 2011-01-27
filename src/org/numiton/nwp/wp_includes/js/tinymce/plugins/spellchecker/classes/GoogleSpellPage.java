/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: GoogleSpellPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.DEFAULT_VAL;

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

import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class GoogleSpellPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(GoogleSpellPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/js/tinymce/plugins/spellchecker/classes/GoogleSpell.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/plugins/spellchecker/classes/GoogleSpell";
    }

 // Patch in multibyte support
    public String mb_substr(String str, int start, int len, String encoding) {
        int limit = 0;
        int s = 0;
        int e = 0;
        limit = Strings.strlen(str);

        for (s = 0; start > 0; --start) {// found the real start
            if (s >= limit) {
                break;
            }

            if (intval(Strings.getCharAt(str, s)) <= intval("\u007F")) {
                ++s;
            } else {
                ++s; // skip length

                while ((intval(Strings.getCharAt(str, s)) >= intval("\u0080")) && (intval(Strings.getCharAt(str, s)) <= intval("\u00BF")))
                    ++s;
            }
        }

        if (equal(len, "")) {
            return Strings.substr(str, s);
        } else {
            for (e = s; len > 0; --len) {//found the real end
                if (e >= limit) {
                    break;
                }

                if (intval(Strings.getCharAt(str, e)) <= intval("\u007F")) {
                    ++e;
                } else {
                    ++e;//skip length

                    while ((intval(Strings.getCharAt(str, e)) >= intval("\u0080")) && (intval(Strings.getCharAt(str, e)) <= intval("\u00BF")) && (e < limit))
                        ++e;
                }
            }
        }

        return Strings.substr(str, s, e - s);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        // Removed by Numiton. All functions are declared.
        return DEFAULT_VAL;
    }
}
