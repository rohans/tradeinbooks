/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_cat2tagPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import com.numiton.generic.Ref;


@Controller
@Scope("request")
public class Wp_cat2tagPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_cat2tagPage.class.getName());
    public Ref<Object> wp_cat2tag_importer = new Ref<Object>();

    @Override
    @RequestMapping("/wp-admin/import/wp-cat2tag.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/_import/wp_cat2tag";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin__import_wp_cat2tag_block1");
        gVars.webEnv = webEnv;
        wp_cat2tag_importer.value = new WP_Categories_to_Tags(gVars, gConsts);
        getIncluded(ImportPage.class, gVars, gConsts).register_importer(
            "wp-cat2tag",
            getIncluded(L10nPage.class, gVars, gConsts).__("Categories to Tags Converter", "default"),
            getIncluded(L10nPage.class, gVars, gConsts).__("Convert existing categories to tags, selectively.", "default"),
            new Array<Object>(new ArrayEntry<Object>(wp_cat2tag_importer), new ArrayEntry<Object>("init")));

        return DEFAULT_VAL;
    }
}
