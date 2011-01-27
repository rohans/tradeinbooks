/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_form_commentPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Edit_form_commentPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_form_commentPage.class.getName());
    public Object submitbutton_text;
    public Object toprow_title;

    @Override
    @RequestMapping("/wp-admin/edit-form-comment.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_form_comment";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block1");
        gVars.webEnv = webEnv;
        submitbutton_text = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Comment", "default");
        toprow_title = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Editing Comment # %s", "default"), intval(StdClass.getValue(gVars.comment, "comment_ID")));
        gVars.form_action = "editedcomment";
        gVars.form_extra = "\' />\n<input type=\'hidden\' name=\'comment_ID\' value=\'" + intval(StdClass.getValue(gVars.comment, "comment_ID")) +
            "\' />\n<input type=\'hidden\' name=\'comment_post_ID\' value=\'" + intval(StdClass.getValue(gVars.comment, "comment_post_ID"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block2");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-comment_" + intval(StdClass.getValue(gVars.comment, "comment_ID")), "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block3");
        echo(gVars.webEnv, toprow_title);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block4");
        echo(gVars.webEnv, gVars.user_ID);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block5");
        echo(gVars.webEnv, gVars.form_action + gVars.form_extra);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block6");
        echo(gVars.webEnv, getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_link());

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("View this Comment", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block8");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Approval Status", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block9");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.comment, "comment_approved")), "1");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Approved", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block11");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.comment, "comment_approved")), "0");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Moderated", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block13");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.comment, "comment_approved")), "spam");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block14");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Spam", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block15");
        gVars.stamp = getIncluded(L10nPage.class, gVars, gConsts).__("%1$s at %2$s", "default");
        gVars.date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                strval(StdClass.getValue(gVars.comment, "comment_date")),
                true);
        gVars.time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")),
                strval(StdClass.getValue(gVars.comment, "comment_date")),
                true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block16");
        QStrings.printf(gVars.webEnv, gVars.stamp, gVars.date, gVars.time);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Edit", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block18");
        getIncluded(TemplatePage.class, gVars, gConsts).touch_time(equal("editcomment", gVars.action), 0, 5);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block20");
        echo(
                gVars.webEnv,
                "<a class=\'submitdelete\' href=\'" +
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url(
                        "comment.php?action=deletecomment&amp;c=" + intval(StdClass.getValue(gVars.comment, "comment_ID")) + "&amp;_wp_original_http_referer=" +
                        getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(),
                        "delete-comment_" + intval(StdClass.getValue(gVars.comment, "comment_ID"))) + "\' onclick=\"if ( confirm(\'" +
                getIncluded(FormattingPage.class, gVars, gConsts).js_escape(
                        getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete this comment. \n  \'Cancel\' to stop, \'OK\' to delete.", "default")) +
                "\') ) { return 1;}return 0;\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Delete comment", "default") + "</a>");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Related", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block22");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block23");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Moderate Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block24");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_relatedlinks_list", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block25");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("submitcomment_box", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block27");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.comment, "comment_author"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block29");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.comment, "comment_author_email"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("URL", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block31");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.comment, "comment_author_url"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block32");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comment", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block33");
        getIncluded(General_templatePage.class, gVars, gConsts).the_editor(strval(StdClass.getValue(gVars.comment, "comment_content")), "content", "newcomment_author_url", false, 4);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("closedpostboxes", "closedpostboxesnonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block34");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("comment", "normal", gVars.comment);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block35");
        echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_ID")));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block36");
        echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_post_ID")));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block37");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer());

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_comment_block38");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_original_referer_field(true, "previous");

        return DEFAULT_VAL;
    }
}
