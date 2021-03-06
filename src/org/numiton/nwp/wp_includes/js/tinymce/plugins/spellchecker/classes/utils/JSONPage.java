/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: JSONPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils;

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


@Controller
@Scope("request")
public class JSONPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(JSONPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/js/tinymce/plugins/spellchecker/classes/utils/JSON.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/plugins/spellchecker/classes/utils/JSON";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_plugins_spellchecker_classes_utils_JSON_block1");
        gVars.webEnv = webEnv;

        /**
         * $Id: JSONPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
         *
         * @package MCManager.utils
         * @author Moxiecode
         * @copyright Copyright � 2007, Moxiecode Systems AB, All rights reserved.
         */
        gConsts.setJSON_BOOL(1);
        gConsts.setJSON_INT(2);
        gConsts.setJSON_STR(3);
        gConsts.setJSON_FLOAT(4);
        gConsts.setJSON_NULL(5);
        gConsts.setJSON_START_OBJ(6);
        gConsts.setJSON_END_OBJ(7);
        gConsts.setJSON_START_ARRAY(8);
        gConsts.setJSON_END_ARRAY(9);
        gConsts.setJSON_KEY(10);
        gConsts.setJSON_SKIP(11);
        
        gConsts.setJSON_IN_ARRAY(30);
        gConsts.setJSON_IN_OBJECT(40);
        gConsts.setJSON_IN_BETWEEN(50);

        return DEFAULT_VAL;
    }
}
