/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FooterPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_content.themes.classic;

import static com.numiton.VarHandling.echo;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.Wp_settingsPage;
import org.numiton.nwp.wp_includes.General_templatePage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller(value = "wp_content/themes/classic/FooterPage")
@Scope("request")
public class FooterPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FooterPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/classic/footer.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/classic/footer";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_footer_block1");
        getIncluded(General_templatePage.class, gVars, gConsts).get_sidebar();

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_footer_block2");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_includes.FunctionsPage) getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts))).get_num_queries());

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_footer_block3");
        getIncluded(Wp_settingsPage.class, gVars, gConsts).timer_stop(1);

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_footer_block4");
        echo(gVars.webEnv,
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Powered by <a href=\'http://wordpress.org/\' title=\'%s\'><strong>WordPress</strong></a>"),
                getIncluded(L10nPage.class, gVars, gConsts).__("Powered by WordPress, state-of-the-art semantic personal publishing platform.")));

        /* Start of block */
        super.startBlock("__wp_content_themes_classic_footer_block5");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_footer();

        return DEFAULT_VAL;
    }
}
