/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: HelloPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_content.plugins;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.FormattingPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class HelloPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(HelloPage.class.getName());
    public Object chosen;
    public String lyrics;

    @Override
    @RequestMapping("/wp-content/plugins/hello.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/plugins/hello";
    }

    /**
     * This just echoes the chosen line, we'll position it later
     */
    public void hello_dolly() {
        echo(gVars.webEnv, "<p id=\'dolly\'>" + strval(chosen) + "</p>");
    }

    /**
     * We need some CSS to position the paragraph
     */
    public void dolly_css() {
        echo(
                gVars.webEnv,
                "\n\t<style type=\'text/css\'>\n\t#dolly {\n\t\tposition: absolute;\n\t\ttop: 2.3em;\n\t\tmargin: 0;\n\t\tpadding: 0;\n\t\tright: 10px;\n\t\tfont-size: 16px;\n\t\tcolor: #d54e21;\n\t}\n\t</style>\n\t");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_plugins_hello_block1");
        gVars.webEnv = webEnv;
        
        /*
        Plugin Name: Hello Dolly
        Plugin URI: http://wordpress.org/#
        Description: This is not just a plugin, it symbolizes the hope and enthusiasm of an entire generation summed up in two words sung most famously by Louis Armstrong: Hello, Dolly. When activated you will randomly see a lyric from <cite>Hello, Dolly</cite> in the upper right of your admin screen on every page.
        Author: Matt Mullenweg
        Version: 1.5
        Author URI: http://photomatt.net/
        */

        // These are the lyrics to Hello Dolly
        lyrics = "Hello, Dolly\nWell, hello, Dolly\nIt\'s so nice to have you back where you belong\nYou\'re lookin\' swell, Dolly\nI can tell, Dolly\nYou\'re still glowin\', you\'re still crowin\'\nYou\'re still goin\' strong\nWe feel the room swayin\'\nWhile the band\'s playin\'\nOne of your old favourite songs from way back when\nSo, take her wrap, fellas\nFind her an empty lap, fellas\nDolly\'ll never go away again\nHello, Dolly\nWell, hello, Dolly\nIt\'s so nice to have you back where you belong\nYou\'re lookin\' swell, Dolly\nI can tell, Dolly\nYou\'re still glowin\', you\'re still crowin\'\nYou\'re still goin\' strong\nWe feel the room swayin\'\nWhile the band\'s playin\'\nOne of your old favourite songs from way back when\nGolly, gee, fellas\nFind her a vacant knee, fellas\nDolly\'ll never go away\nDolly\'ll never go away\nDolly\'ll never go away again";

        // Here we split it into lines
        Array<String> lyricsArray = Strings.explode("\n", lyrics);
        // And then randomly choose a line
        chosen = getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(lyricsArray.getValue(Math.mt_rand(0, Array.count(lyricsArray) - 1)));
        
        // Now we set that function up to execute when the admin_footer action is called
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_footer", Callback.createCallbackArray(this, "hello_dolly"));
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_head", Callback.createCallbackArray(this, "dolly_css"));

        return DEFAULT_VAL;
    }
}
