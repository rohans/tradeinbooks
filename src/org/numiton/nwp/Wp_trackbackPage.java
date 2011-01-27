/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_trackbackPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QMultibyte;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Wp_trackbackPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_trackbackPage.class.getName());
    public Object request_array;
    public int tb_id;
    public Object tb_url;
    public String charset;
    public String excerpt;
    public String blog_name;
    public Object dupe;

    @Override
    @RequestMapping("/wp-trackback.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_trackback";
    }

    public void trackback_response(int error, String error_message) {
        Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")));

        if (booleanval(error)) {
            echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"utf-8\"?" + ">\n");
            echo(gVars.webEnv, "<response>\n");
            echo(gVars.webEnv, "<error>1</error>\n");
            echo(gVars.webEnv, "<message>" + error_message + "</message>\n");
            echo(gVars.webEnv, "</response>");
            System.exit();
        } else {
            echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"utf-8\"?" + ">\n");
            echo(gVars.webEnv, "<response>\n");
            echo(gVars.webEnv, "<error>0</error>\n");
            echo(gVars.webEnv, "</response>");
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_trackback_block1");
        gVars.webEnv = webEnv;

        if (empty(gVars.wp)) {
            requireOnce(gVars, gConsts, Wp_configPage.class);
            getIncluded(FunctionsPage.class, gVars, gConsts).wp("tb=1");
        }

        // trackback is done by a POST
        request_array = "HTTP_POST_VARS";

        if (!booleanval(gVars.webEnv._GET.getValue("tb_id"))) {
            Array tb_idArray = Strings.explode("/", gVars.webEnv.getRequestURI());
            tb_id = intval(tb_idArray.getValue(Array.count(tb_idArray) - 1));
        }

        tb_url = intval(gVars.webEnv._POST.getValue("url"));
        charset = strval(gVars.webEnv._POST.getValue("charset"));

        // These three are stripslashed here so that they can be properly escaped after mb_convert_encoding()
        gVars.title = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("title")));
        excerpt = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("excerpt")));
        blog_name = Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._POST.getValue("blog_name")));

        if (booleanval(charset)) {
            charset = Strings.strtoupper(Strings.trim(charset));
        } else {
            charset = "ASCII, UTF-8, ISO-8859-1, JIS, EUC-JP, SJIS";
        }

        // No valid uses for UTF-7
        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(charset, "UTF-7"))) {
            System.exit();
        }

        if (true) /*Modified by Numiton*/
                  // For international trackbacks
         {
            gVars.title = QMultibyte.mb_convert_encoding(gVars.title, strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")), charset);
            excerpt = QMultibyte.mb_convert_encoding(excerpt, strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")), charset);
            blog_name = QMultibyte.mb_convert_encoding(blog_name, strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")), charset);
        }

        // Now that mb_convert_encoding() has been given a swing, we need to escape these three
        gVars.title = gVars.wpdb.escape(gVars.title);
        excerpt = gVars.wpdb.escape(excerpt);
        blog_name = gVars.wpdb.escape(blog_name);

        if (getIncluded(QueryPage.class, gVars, gConsts).is_single() || getIncluded(QueryPage.class, gVars, gConsts).is_page("")) {
            tb_id = intval(gVars.posts.getValue(0).fields.getValue("ID"));
        }

        if (!booleanval(tb_id)) {
            trackback_response(1, "I really need an ID for this to work.");
        }

        if (empty(gVars.title) && empty(tb_url) && empty(blog_name)) {
            // If it doesn't look like a trackback at all...
            getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(tb_id, false), 302);
            System.exit();
        }

        if (!empty(tb_url) && !empty(gVars.title)) {
            Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset")));

            if (!getIncluded(Comment_templatePage.class, gVars, gConsts).pings_open(tb_id)) {
                trackback_response(1, "Sorry, trackbacks are closed for this item.");
            }

            gVars.title = getIncluded(FormattingPage.class, gVars, gConsts).wp_html_excerpt(gVars.title, 250) + "...";
            excerpt = getIncluded(FormattingPage.class, gVars, gConsts).wp_html_excerpt(excerpt, 252) + "...";
            gVars.comment_post_ID = tb_id;
            gVars.comment_author = blog_name;
            gVars.comment_author_email = "";
            gVars.comment_author_url = strval(tb_url);
            gVars.comment_content = "<strong>" + gVars.title + "</strong>\n\n" + excerpt;
            gVars.comment_type = "trackback";
            dupe = gVars.wpdb.get_results(
                        "SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = \'" + gVars.comment_post_ID + "\' AND comment_author_url = \'" + gVars.comment_author_url + "\'");

            if (booleanval(dupe)) {
                trackback_response(1, "We already have a ping from that URL for this post.");
            }

            gVars.commentdata = Array.compact(new ArrayEntry("comment_post_ID", gVars.comment_post_ID), new ArrayEntry("comment_author", gVars.comment_author),
                    new ArrayEntry("comment_author_email", gVars.comment_author_email), new ArrayEntry("comment_author_url", gVars.comment_author_url),
                    new ArrayEntry("comment_content", gVars.comment_content), new ArrayEntry("comment_type", gVars.comment_type));
            getIncluded(CommentPage.class, gVars, gConsts).wp_new_comment(gVars.commentdata);
            getIncluded(PluginPage.class, gVars, gConsts).do_action("trackback_post", gVars.wpdb.insert_id);
            trackback_response(0, "");
        }

        return DEFAULT_VAL;
    }
}
