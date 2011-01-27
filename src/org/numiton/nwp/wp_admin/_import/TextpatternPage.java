/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: TextpatternPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class TextpatternPage extends NumitonController implements CommonInterface4, CommonInterface5 {
    protected static final Logger LOG = Logger.getLogger(TextpatternPage.class.getName());
    public Object txp_import;

    @Override
    @RequestMapping("/wp-admin/import/textpattern.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/_import/textpattern";
    }

    /**
	 * Add These Functions to make our lives easier
	 */

    public Object get_comment_count(int post_ID) {
        return gVars.wpdb.get_var("SELECT count(*) FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = " + post_ID);
    }

    public Object link_exists(String linkname) {
        return gVars.wpdb.get_var("SELECT link_id FROM " + gVars.wpdb.links + " WHERE link_name = \"" + gVars.wpdb.escape(linkname) + "\"");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin__import_textpattern_block1");
        gVars.webEnv = webEnv;

        // Removed by Numiton. All functions are declared.
        txp_import = new Textpattern_Import(gVars, gConsts);
        getIncluded(ImportPage.class, gVars, gConsts).register_importer(
            "textpattern",
            getIncluded(L10nPage.class, gVars, gConsts).__("Textpattern", "default"),
            getIncluded(L10nPage.class, gVars, gConsts).__("Import categories, users, posts, comments, and links from a Textpattern blog.", "default"),
            new Array<Object>(new ArrayEntry<Object>(txp_import), new ArrayEntry<Object>("dispatch")));

        return DEFAULT_VAL;
    }
}
