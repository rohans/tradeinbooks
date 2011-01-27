/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_mailPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.generic.ExpressionHelper;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Wp_mailPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_mailPage.class.getName());
    public int time_difference;
    public String phone_delim;
    public POP3 pop3;
    public String content_type;
    public String content_transfer_encoding;
    public String boundary;
    public Object bodysignal;
    public int post_author;
    public Object author_found;
    public Array<Object> dmonths = new Array<Object>();
    public String subject;
    public String ddate;
    public Array<String> date_arr = new Array<String>();
    public Array<String> date_time = new Array<String>();
    public int ddate_H;
    public int ddate_i;
    public int ddate_s;
    public int ddate_m;
    public int ddate_d;
    public int ddate_Y;
    public int j;
    public int time_zn;
    public int ddate_U;
    public Object post_date;
    public Object post_date_gmt;
    public Object post_content;
    public Array<Object> post_categories = new Array<Object>();
    public Array<Object> post_data;

    @Override
    @RequestMapping("/wp-mail.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_mail";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_mail_block1");
        gVars.webEnv = webEnv;
        /* Condensed dynamic construct: 512070 */ require(gVars, gConsts, Wp_configPage.class);

        ErrorHandling.error_reporting(gVars.webEnv, 2037);

        time_difference = intval(floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600);

        phone_delim = "::";

        pop3 = new POP3(gVars, gConsts);

        if (!pop3.connect(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mailserver_url")), intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mailserver_port")))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(pop3.ERROR, strval(0)), "");
        }

        if (!pop3.user(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mailserver_login"))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(pop3.ERROR, strval(0)), "");
        }

        gVars.count = pop3.pass(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mailserver_pass")));

        if (strictEqual(BOOLEAN_FALSE, gVars.count)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(pop3.ERROR, strval(0)), "");
        }

        if (equal(0, gVars.count)) {
            echo(gVars.webEnv, "<p>There doesn\'t seem to be any new mail.</p>\n"); // will fall-through to end of for loop
        }

        for (gVars.i = 1; gVars.i <= gVars.count; gVars.i++) {
            Array<Object> messageArray = pop3.get(gVars.i);

            gVars.content = "";
            content_type = "";
            content_transfer_encoding = "";
            boundary = "";
            bodysignal = 0;
            post_author = 1;
            author_found = false;
            dmonths = new Array<Object>(
                    new ArrayEntry<Object>("Jan"),
                    new ArrayEntry<Object>("Feb"),
                    new ArrayEntry<Object>("Mar"),
                    new ArrayEntry<Object>("Apr"),
                    new ArrayEntry<Object>("May"),
                    new ArrayEntry<Object>("Jun"),
                    new ArrayEntry<Object>("Jul"),
                    new ArrayEntry<Object>("Aug"),
                    new ArrayEntry<Object>("Sep"),
                    new ArrayEntry<Object>("Oct"),
                    new ArrayEntry<Object>("Nov"),
                    new ArrayEntry<Object>("Dec"));

            for (Map.Entry javaEntry684 : messageArray.entrySet()) {
                gVars.line = strval(javaEntry684.getValue());

                if (Strings.strlen(gVars.line) < 3) {
                    bodysignal = 1;
                }

                if (booleanval(bodysignal)) {
                    gVars.content = gVars.content + gVars.line;
                } else {
                    if (QRegExPerl.preg_match("/Content-Type: /i", gVars.line)) {
                        content_type = Strings.trim(gVars.line);
                        content_type = Strings.substr(content_type, 14, Strings.strlen(content_type) - 14);

                        Array<String> content_typeArray = Strings.explode(";", content_type);
                        content_type = content_typeArray.getValue(0);
                    }

                    if (QRegExPerl.preg_match("/Content-Transfer-Encoding: /i", gVars.line)) {
                        content_transfer_encoding = Strings.trim(gVars.line);
                        content_transfer_encoding = Strings.substr(content_transfer_encoding, 27, Strings.strlen(content_transfer_encoding) - 14);
                        content_transfer_encoding = Strings.explode(";", content_transfer_encoding).getValue(0);
                    }

                    if (equal(content_type, "multipart/alternative") && QRegExPerl.preg_match("/boundary=\"/", gVars.line) && equal(boundary, "")) {
                        boundary = Strings.trim(gVars.line);
                        boundary = Strings.explode("\"", boundary).getValue(1);
                    }

                    if (QRegExPerl.preg_match("/Subject: /i", gVars.line)) {
                        subject = Strings.trim(gVars.line);
                        subject = Strings.substr(subject, 9, Strings.strlen(subject) - 9);
                        subject = getIncluded(FormattingPage.class, gVars, gConsts).wp_iso_descrambler(subject);

                        // Captures any text in the subject before $phone_delim as the subject
                        Array<String> subjectArray = Strings.explode(phone_delim, subject);
                        subject = subjectArray.getValue(0);
                    }

                    // Set the author using the email address (From or Reply-To, the last used)
        			// otherwise use the site admin
                    if (QRegExPerl.preg_match("/(From|Reply-To): /", gVars.line)) { // of the form '20 Mar 2002 20:32:37'
                        if (QRegExPerl.preg_match("|[a-z0-9_.-]+@[a-z0-9_.-]+(?!.*<)|i", gVars.line, gVars.matches)) {
                            gVars.author = strval(gVars.matches.getValue(0));
                        } else {
                            gVars.author = Strings.trim(gVars.line);
                        }

                        gVars.author = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_email(gVars.author);

                        if (getIncluded(FormattingPage.class, gVars, gConsts).is_email(gVars.author)) {
                            echo(gVars.webEnv, "Author = " + gVars.author + " <p>");
                            gVars.userdata = getIncluded(PluggablePage.class, gVars, gConsts).get_user_by_email(gVars.author);

                            if (!booleanval(gVars.userdata)) {
                                post_author = 1;
                                author_found = false;
                            } else {
                                post_author = intval(StdClass.getValue(gVars.userdata, "ID"));
                                author_found = true;
                            }
                        } else {
                            post_author = 1;
                            author_found = false;
                        }
                    }

                    if (QRegExPerl.preg_match("/Date: /i", gVars.line))/*
                     * of the form '20 Mar 2002 20:32:37' of the form '20 Mar
                     * 2002 20:32:37'
                     */
                     {
                        ddate = Strings.trim(gVars.line);
                        ddate = Strings.str_replace("Date: ", "", ddate);

                        if (BOOLEAN_FALSE != Strings.strpos(ddate, ",")) {
                            ddate = Strings.trim(Strings.substr(ddate, Strings.strpos(ddate, ",") + 1, Strings.strlen(ddate)));
                        }

                        date_arr = Strings.explode(" ", ddate);
                        date_time = Strings.explode(":", date_arr.getValue(3));
                        ddate_H = intval(date_time.getValue(0));
                        ddate_i = intval(date_time.getValue(1));
                        ddate_s = intval(date_time.getValue(2));
                        ddate_m = intval(date_arr.getValue(1));
                        ddate_d = intval(date_arr.getValue(0));
                        ddate_Y = intval(date_arr.getValue(2));

                        for (j = 0; j < 12; j++) {
                            if (equal(ddate_m, dmonths.getValue(j))) {
                                ddate_m = j + 1;
                            }
                        }

                        time_zn = intval(date_arr.getValue(4)) * 36;
                        ddate_U = DateTime.gmmktime(ddate_H, ddate_i, ddate_s, ddate_m, ddate_d, ddate_Y);
                        ddate_U = ddate_U - time_zn;
                        post_date = DateTime.gmdate("Y-m-d H:i:s", ddate_U + time_difference);
                        post_date_gmt = DateTime.gmdate("Y-m-d H:i:s", ddate_U);
                    }
                }
            }

            // Set $post_status based on $author_found and on author's publish_posts capability
            if (booleanval(author_found)) {
                gVars.user = new WP_User(gVars, gConsts, post_author);

                if (((WP_User) gVars.user).has_cap("publish_posts")) {
                    gVars.post_status = "publish";
                } else {
                    gVars.post_status = "pending";
                }
            } else {
            	// Author not found in DB, set status to pending.  Author already set to admin.
                gVars.post_status = "pending";
            }

            subject = Strings.trim(subject);

            Array contentArray;

            if (equal(content_type, "multipart/alternative")) {
                contentArray = Strings.explode("--" + boundary, gVars.content);
                gVars.content = strval(contentArray.getValue(2));
                contentArray = Strings.explode("Content-Transfer-Encoding: quoted-printable", gVars.content);
                gVars.content = Strings.strip_tags(strval(contentArray.getValue(1)), "<img><p><br><i><b><u><em><strong><strike><font><span><div>");
            }

            gVars.content = Strings.trim(gVars.content);

            if (!strictEqual(Strings.stripos(content_transfer_encoding, "quoted-printable"), BOOLEAN_FALSE)) {
                gVars.content = Strings.quoted_printable_decode(gVars.content);
            }

            // Captures any text in the body after $phone_delim as the body
            contentArray = Strings.explode(phone_delim, gVars.content);
            ExpressionHelper.execExpr(booleanval(contentArray.getValue(1))
                ? (gVars.content = strval(contentArray.getValue(1)))
                : (gVars.content = strval(contentArray.getValue(0))));
            
            gVars.content = Strings.trim(gVars.content);
            
            post_content = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("phone_content", gVars.content);
            
            gVars.post_title = getIncluded(FunctionsPage.class, gVars, gConsts).xmlrpc_getposttitle(gVars.content);

            if (equal(gVars.post_title, "")) {
                gVars.post_title = subject;
            }

            if (empty(post_categories)) {
                post_categories.putValue(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_email_category"));
            }

            gVars.post_category = post_categories;
            
            post_data = Array.compact(new ArrayEntry("post_content", post_content), new ArrayEntry("post_title", gVars.post_title), new ArrayEntry("post_date", post_date),
                    new ArrayEntry("post_date_gmt", post_date_gmt), new ArrayEntry("post_author", post_author), new ArrayEntry("post_category", gVars.post_category),
                    new ArrayEntry("post_status", gVars.post_status));
            post_data = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(post_data);
            
            gVars.post_ID = getIncluded(PostPage.class, gVars, gConsts).wp_insert_post(post_data);

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(gVars.post_ID)) {
                echo(gVars.webEnv, "\n" + ((WP_Error) gVars.post_ID).get_error_message());
            }

            if (!booleanval(gVars.post_ID)) {
            	// we couldn't post, for whatever reason. better move forward to the next email
                continue;
            }

            getIncluded(PluginPage.class, gVars, gConsts).do_action("publish_phone", gVars.post_ID);
            
            echo(gVars.webEnv, "\n<p><b>Author:</b> " + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(post_author), strval(0)) + "</p>");
            echo(gVars.webEnv, "\n<p><b>Posted title:</b> " + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(gVars.post_title, strval(0)) + "<br />");

            if (!pop3.delete(gVars.i)) {
                echo(gVars.webEnv, "<p>Oops " + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(pop3.ERROR, strval(0)) + "</p></div>");
                pop3.reset();
                System.exit();
            } else {
                echo(gVars.webEnv, "<p>Mission complete, message <strong>" + strval(gVars.i) + "</strong> deleted.</p>");
            }
        }

        pop3.quit();

        return DEFAULT_VAL;
    }
}
