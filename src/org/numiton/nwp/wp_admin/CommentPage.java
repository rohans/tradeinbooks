/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CommentPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/CommentPage")
@Scope("request")
public class CommentPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CommentPage.class.getName());
    public Object formaction;
    public Object caution_msg;
    public Object button;
    public Object noredir;
    public Object comment_post_id;

    @Override
    @RequestMapping("/wp-admin/comment.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/comment";
    }

    public void comment_footer_die(Object msg) {  // $msg is assumed to contain HTML and be sanitized
        echo(gVars.webEnv, "<div class=\'wrap\'><p>" + strval(msg) + "</p></div>");
        include(gVars, gConsts, Admin_footerPage.class);
        System.exit();
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_comment_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.parent_file = "edit-comments.php";
        gVars.submenu_file = "edit-comments.php";
        getIncluded(MiscPage.class, gVars, gConsts).wp_reset_vars(this, new Array<Object>(new ArrayEntry<Object>("action")));

        if (isset(gVars.webEnv._POST.getValue("deletecomment"))) {
            gVars.action = "deletecomment";
        }

        {
            int javaSwitchSelector3 = 0;

            if (equal(gVars.action, "editcomment")) {
                javaSwitchSelector3 = 1;
            }

            if (equal(gVars.action, "cdc")) {
                javaSwitchSelector3 = 2;
            }

            if (equal(gVars.action, "mac")) {
                javaSwitchSelector3 = 3;
            }

            if (equal(gVars.action, "deletecomment")) {
                javaSwitchSelector3 = 4;
            }

            if (equal(gVars.action, "unapprovecomment")) {
                javaSwitchSelector3 = 5;
            }

            if (equal(gVars.action, "approvecomment")) {
                javaSwitchSelector3 = 6;
            }

            if (equal(gVars.action, "editedcomment")) {
                javaSwitchSelector3 = 7;
            }

            switch (javaSwitchSelector3) {
            case 1: {
                gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Comment", "default");
                
                getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("comment", false, new Array<Object>(), false);
                //wp_enqueue_script('thickbox');
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                
                gVars.comment_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("c"));

                if (!booleanval(
                            (gVars.comment = (StdClass) (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).get_comment(
                                    gVars.comment_id,
                                    gConsts.getOBJECT())))) {
                    comment_footer_die(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Oops, no comment with this ID.", "default") +
                        QStrings.sprintf(" <a href=\"%s\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Go back", "default") + "</a>!", "javascript:history.go(-1)"));
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    comment_footer_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit comments on this post.", "default"));
                }

                gVars.comment = (((org.numiton.nwp.wp_admin.includes.CommentPage) getIncluded(org.numiton.nwp.wp_admin.includes.CommentPage.class, gVars, gConsts))).get_comment_to_edit(gVars.comment_id);
                include(gVars, gConsts, Edit_form_commentPage.class);

                break;
            }

            case 2: {
            }

            case 3: {
                requireOnce(gVars, gConsts, Admin_headerPage.class);
                gVars.comment_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("c"));
                formaction = (equal("cdc", gVars.action)
                    ? "deletecomment"
                    : "approvecomment");
                gVars.nonce_action = (equal("cdc", gVars.action)
                    ? "delete-comment_"
                    : "approve-comment_");
                gVars.nonce_action = gVars.nonce_action + strval(gVars.comment_id);

                if (!booleanval(
                            gVars.comment = (((org.numiton.nwp.wp_admin.includes.CommentPage) getIncluded(org.numiton.nwp.wp_admin.includes.CommentPage.class, gVars, gConsts))).get_comment_to_edit(gVars.comment_id))) {
                    comment_footer_die(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Oops, no comment with this ID.", "default") +
                        QStrings.sprintf(" <a href=\"%s\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Go back", "default") + "</a>!", "edit.php"));
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    comment_footer_die(
                        equal("cdc", gVars.action)
                        ? getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to delete comments on this post.", "default")
                        : getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit comments on this post, so you cannot approve this comment.", "default"));
                }

                echo(gVars.webEnv, "<div class=\'wrap\'>\n\n<div class=\"narrow\">\n");

                if (equal("spam", gVars.webEnv._GET.getValue("dt"))) {
                    caution_msg = getIncluded(L10nPage.class, gVars, gConsts).__("You are about to mark the following comment as spam:", "default");
                    button = getIncluded(L10nPage.class, gVars, gConsts).__("Spam Comment", "default");
                } else if (equal("cdc", gVars.action)) {
                    caution_msg = getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete the following comment:", "default");
                    button = getIncluded(L10nPage.class, gVars, gConsts).__("Delete Comment", "default");
                } else {
                    caution_msg = getIncluded(L10nPage.class, gVars, gConsts).__("You are about to approve the following comment:", "default");
                    button = getIncluded(L10nPage.class, gVars, gConsts).__("Approve Comment", "default");
                }

                echo(gVars.webEnv, "\n<p><strong>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Caution:", "default");
                echo(gVars.webEnv, "</strong> ");
                echo(gVars.webEnv, caution_msg);
                echo(gVars.webEnv, "</p>\n\n<p>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Are you sure you want to do that?", "default");
                echo(gVars.webEnv, "</p>\n\n<form action=\'comment.php\' method=\'get\'>\n\n<table width=\"100%\">\n<tr>\n<td><input type=\'button\' class=\"button\" value=\'");
                getIncluded(L10nPage.class, gVars, gConsts)._e("No", "default");
                echo(gVars.webEnv, "\' onclick=\"self.location=\'");
                echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
                echo(gVars.webEnv, "/wp-admin/edit-comments.php\';\" /></td>\n<td class=\"textright\"><input type=\'submit\' class=\"button\" value=\'");
                echo(gVars.webEnv, button);
                echo(gVars.webEnv, "\' /></td>\n</tr>\n</table>\n\n");
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field(gVars.nonce_action, "_wpnonce", true, true);
                echo(gVars.webEnv, "<input type=\'hidden\' name=\'action\' value=\'");
                echo(gVars.webEnv, formaction);
                echo(gVars.webEnv, "\' />\n");

                if (equal("spam", gVars.webEnv._GET.getValue("dt"))) {
                    echo(gVars.webEnv, "<input type=\'hidden\' name=\'dt\' value=\'spam\' />\n");
                }

                echo(gVars.webEnv, "<input type=\'hidden\' name=\'p\' value=\'");
                echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_post_ID")));
                echo(gVars.webEnv, "\' />\n<input type=\'hidden\' name=\'c\' value=\'");
                echo(gVars.webEnv, intval(StdClass.getValue(gVars.comment, "comment_ID")));
                echo(gVars.webEnv, "\' />\n<input type=\'hidden\' name=\'noredir\' value=\'1\' />\n</form>\n\n<table class=\"form-table\" cellpadding=\"5\">\n<tr class=\"alt\">\n<th scope=\"row\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Author", "default");
                echo(gVars.webEnv, "</th>\n<td>");
                echo(gVars.webEnv, StdClass.getValue(gVars.comment, "comment_author"));
                echo(gVars.webEnv, "</td>\n</tr>\n");

                if (booleanval(StdClass.getValue(gVars.comment, "comment_author_email"))) {
                    echo(gVars.webEnv, "<tr>\n<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("E-mail", "default");
                    echo(gVars.webEnv, "</th>\n<td>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.comment, "comment_author_email"));
                    echo(gVars.webEnv, "</td>\n</tr>\n");
                }

                if (booleanval(StdClass.getValue(gVars.comment, "comment_author_url"))) {
                    echo(gVars.webEnv, "<tr>\n<th scope=\"row\">");
                    getIncluded(L10nPage.class, gVars, gConsts)._e("URL", "default");
                    echo(gVars.webEnv, "</th>\n<td><a href=\'");
                    echo(gVars.webEnv, StdClass.getValue(gVars.comment, "comment_author_url"));
                    echo(gVars.webEnv, "\'>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.comment, "comment_author_url"));
                    echo(gVars.webEnv, "</a></td>\n</tr>\n");
                }

                echo(gVars.webEnv, "<tr>\n<th scope=\"row\" valign=\"top\">");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Comment", "default");
                echo(gVars.webEnv, "</th>\n<td>");
                echo(gVars.webEnv, StdClass.getValue(gVars.comment, "comment_content"));
                echo(gVars.webEnv, "</td>\n</tr>\n</table>\n\n</div>\n</div>\n");

                break;
            }

            case 4: {
                gVars.comment_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._REQUEST.getValue("c"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("delete-comment_" + strval(gVars.comment_id), "_wpnonce");

                if (isset(gVars.webEnv._REQUEST.getValue("noredir"))) {
                    noredir = true;
                } else {
                    noredir = false;
                }

                if (!booleanval(
                            (gVars.comment = (StdClass) (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).get_comment(
                                    gVars.comment_id,
                                    gConsts.getOBJECT())))) {
                    comment_footer_die(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Oops, no comment with this ID.", "default") +
                        QStrings.sprintf(" <a href=\"%s\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Go back", "default") + "</a>!", "edit-comments.php"));
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    comment_footer_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit comments on this post.", "default"));
                }

                if (equal("spam", gVars.webEnv._REQUEST.getValue("dt"))) {
                    (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_set_comment_status(
                        intval(StdClass.getValue(gVars.comment, "comment_ID")),
                        "spam");
                } else {
                    (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_delete_comment(intval(StdClass.getValue(gVars.comment, "comment_ID")));
                }

                if (!equal("", getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()) && equal(false, noredir) &&
                        strictEqual(BOOLEAN_FALSE, Strings.strpos(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(), "comment.php"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(), 302);
                } else if (!equal("", getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_original_referer()) && equal(false, noredir)) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_original_referer(), 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/edit-comments.php", 302);
                }

                System.exit();

                break;
            }

            case 5: {
                gVars.comment_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("c"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("unapprove-comment_" + strval(gVars.comment_id), "_wpnonce");

                if (isset(gVars.webEnv._GET.getValue("noredir"))) {
                    noredir = true;
                } else {
                    noredir = false;
                }

                if (!booleanval(
                            (gVars.comment = (StdClass) (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).get_comment(
                                    gVars.comment_id,
                                    gConsts.getOBJECT())))) {
                    comment_footer_die(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Oops, no comment with this ID.", "default") +
                        QStrings.sprintf(" <a href=\"%s\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Go back", "default") + "</a>!", "edit.php"));
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    comment_footer_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit comments on this post, so you cannot disapprove this comment.", "default"));
                }

                (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_set_comment_status(
                    intval(StdClass.getValue(gVars.comment, "comment_ID")),
                    "hold");

                if (!equal("", getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()) && equal(false, noredir)) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(), 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/edit.php?p=" +
                            strval(getIncluded(FunctionsPage.class, gVars, gConsts).absint(intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) + "#comments",
                            302);
                }

                System.exit();

                break;
            }

            case 6: {
                gVars.comment_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("c"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("approve-comment_" + strval(gVars.comment_id), "_wpnonce");

                if (isset(gVars.webEnv._GET.getValue("noredir"))) {
                    noredir = true;
                } else {
                    noredir = false;
                }

                if (!booleanval(
                            (gVars.comment = (StdClass) (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).get_comment(
                                    gVars.comment_id,
                                    gConsts.getOBJECT())))) {
                    comment_footer_die(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Oops, no comment with this ID.", "default") +
                        QStrings.sprintf(" <a href=\"%s\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Go back", "default") + "</a>!", "edit.php"));
                }

                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) {
                    comment_footer_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit comments on this post, so you cannot approve this comment.", "default"));
                }

                (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_set_comment_status(
                    intval(StdClass.getValue(gVars.comment, "comment_ID")),
                    "approve");

                if (equal(true, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comments_notify"))) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_notify_postauthor(intval(StdClass.getValue(gVars.comment, "comment_ID")), "");
                }

                if (!equal("", getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()) && equal(false, noredir)) {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(), 302);
                } else {
                    getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(
                            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/edit.php?p=" +
                            strval(getIncluded(FunctionsPage.class, gVars, gConsts).absint(intval(StdClass.getValue(gVars.comment, "comment_post_ID")))) + "#comments",
                            302);
                }

                System.exit();

                break;
            }

            case 7: {
                gVars.comment_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._POST.getValue("comment_ID"));
                comment_post_id = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._POST.getValue("comment_post_id"));
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-comment_" + strval(gVars.comment_id), "_wpnonce");
                (((org.numiton.nwp.wp_admin.includes.CommentPage) getIncluded(org.numiton.nwp.wp_admin.includes.CommentPage.class, gVars, gConsts))).edit_comment();
                gVars.location = (empty(gVars.webEnv._POST.getValue("referredby"))
                    ? ("edit.php?p=" + strval(comment_post_id))
                    : strval(gVars.webEnv._POST.getValue("referredby"))) + "#comment-" + strval(gVars.comment_id);
                gVars.location = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_edit_redirect", gVars.location, gVars.comment_id));
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
                System.exit();

                break;
            }

            default: {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Unknown action.", "default"), "");

                break;
            }
            } // end switch
        }

        include(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
