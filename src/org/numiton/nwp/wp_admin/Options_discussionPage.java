/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_discussionPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Options_discussionPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_discussionPage.class.getName());
    public Array<Object> yesorno;
    public Array<Object> ratings;
    public Object rating;

    @Override
    @RequestMapping("/wp-admin/options-discussion.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_discussion";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Discussion Settings", "default");
        gVars.parent_file = "options-general.php";
        include(gVars, gConsts, Admin_headerPage.class);

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Discussion Settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-options", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block4");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Default article settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block5");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_pingback_flag")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Attempt to notify any blogs linked to from the article (slows down posting.)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block7");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("open", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Allow link notifications from other blogs (pingbacks and trackbacks.)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block9");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("open", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Allow people to post comments on the article", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block11");
        echo(gVars.webEnv, "(" + getIncluded(L10nPage.class, gVars, gConsts).__("These settings may be overridden for individual articles.", "default") + ")");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail me whenever", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block13");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comments_notify")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block14");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Anyone posts a comment", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block15");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("moderation_notify")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("A comment is held for moderation", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Before a comment appears", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block18");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_moderation")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("An administrator must always approve the comment", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block20");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("require_name_email")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comment author must fill out name and e-mail", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block22");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("1", strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_whitelist")));

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block23");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comment author must have a previously approved comment", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block24");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comment Moderation", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block25");
        QStrings.printf(
            gVars.webEnv,
            getIncluded(L10nPage.class, gVars, gConsts).__(
                "Hold a comment in the queue if it contains %s or more links. (A common characteristic of comment spam is a large number of hyperlinks.)",
                "default"),
            "<input name=\"comment_max_links\" type=\"text\" id=\"comment_max_links\" size=\"3\" value=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_max_links") +
            "\" />");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "When a comment contains any of these words in its content, name, URL, e-mail, or IP, it will be held in the <a href=\"edit-comments.php?comment_status=moderated\">moderation queue</a>. One word or IP per line. It will match inside words, so \"press\" will match \"WordPress\".",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block27");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("moderation_keys");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comment Blacklist", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block29");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "When a comment contains any of these words in its content, name, URL, e-mail, or IP, it will be marked as spam. One word or IP per line. It will match inside words, so \"press\" will match \"WordPress\".",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block30");
        getIncluded(FunctionsPage.class, gVars, gConsts).form_option("blacklist_keys");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block31");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Avatars", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block32");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "By default nWordPress uses <a href=\"http://gravatar.com/\">Gravatars</a> &#8212; short for Globally Recognized Avatars &#8212; for the pictures that show up next to comments. Plugins may override this.",
                "default");

        // the above would be a good place to link to codex documentation on the gravatar functions, for putting it in themes. anything like that? ?>
        
        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block33");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Avatar display", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block34");
        yesorno = new Array<Object>(
                new ArrayEntry<Object>(0, getIncluded(L10nPage.class, gVars, gConsts).__("Don&#8217;t show Avatars", "default")),
                new ArrayEntry<Object>(1, getIncluded(L10nPage.class, gVars, gConsts).__("Show Avatars", "default")));

        for (Map.Entry javaEntry289 : yesorno.entrySet()) {
            gVars.key = strval(javaEntry289.getKey());
            gVars.value = javaEntry289.getValue();
            gVars.selected = (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_avatars"), gVars.key)
                ? "checked=\"checked\""
                : "");
            echo(gVars.webEnv, "\n\t<label><input type=\'radio\' name=\'show_avatars\' value=\'" + gVars.key + "\' " + strval(gVars.selected) + "> " + strval(gVars.value) + "</label><br />");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block35");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Maximum Rating", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block36");
        ratings = new Array<Object>(
                new ArrayEntry<Object>("G", getIncluded(L10nPage.class, gVars, gConsts).__("G &#8212; Suitable for all audiences", "default")),
                new ArrayEntry<Object>("PG", getIncluded(L10nPage.class, gVars, gConsts).__("PG &#8212; Possibly offensive, usually for audiences 13 and above", "default")),
                new ArrayEntry<Object>("R", getIncluded(L10nPage.class, gVars, gConsts).__("R &#8212; Intended for adult audiences above 17", "default")),
                new ArrayEntry<Object>("X", getIncluded(L10nPage.class, gVars, gConsts).__("X &#8212; Even more mature than above", "default")));

        for (Map.Entry javaEntry290 : ratings.entrySet()) {
            gVars.key = strval(javaEntry290.getKey());
            rating = javaEntry290.getValue();
            gVars.selected = (equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("avatar_rating"), gVars.key)
                ? "checked=\"checked\""
                : "");
            echo(gVars.webEnv, "\n\t<label><input type=\'radio\' name=\'avatar_rating\' value=\'" + gVars.key + "\' " + strval(gVars.selected) + "> " + strval(rating) + "</label><br />");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block37");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_discussion_block38");
        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
