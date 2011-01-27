/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CommentPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_admin.includes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.VarHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/CommentPage")
@Scope("request")
public class CommentPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CommentPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/comment.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/comment";
    }

    public Object comment_exists(String comment_author, String comment_date) {
        return gVars.wpdb.get_var("SELECT comment_post_ID FROM " + gVars.wpdb.comments + "\n\t\t\tWHERE comment_author = \'" + comment_author + "\' AND comment_date = \'" + comment_date + "\'");
    }

    public void edit_comment() {
        int comment_post_ID = 0;
        Object timeunit = null;
        Object aa = null;
        Object mm = null;
        int jj = 0;
        int hh = 0;
        int mn = 0;
        int ss = 0;
        comment_post_ID = intval(gVars.webEnv._POST.getValue("comment_post_ID"));

        if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", comment_post_ID)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__(
                    "You are not allowed to edit comments on this post, so you cannot edit this comment.",
                    "default"), "");
        }

        gVars.webEnv._POST.putValue("comment_author", gVars.webEnv._POST.getValue("newcomment_author"));
        gVars.webEnv._POST.putValue("comment_author_email", gVars.webEnv._POST.getValue("newcomment_author_email"));
        gVars.webEnv._POST.putValue("comment_author_url", gVars.webEnv._POST.getValue("newcomment_author_url"));
        gVars.webEnv._POST.putValue("comment_approved", gVars.webEnv._POST.getValue("comment_status"));
        gVars.webEnv._POST.putValue("comment_content", gVars.webEnv._POST.getValue("content"));
        gVars.webEnv._POST.putValue("comment_ID", intval(gVars.webEnv._POST.getValue("comment_ID")));

        for (Map.Entry javaEntry130 : new Array<Object>(
                new ArrayEntry<Object>("aa"),
                new ArrayEntry<Object>("mm"),
                new ArrayEntry<Object>("jj"),
                new ArrayEntry<Object>("hh"),
                new ArrayEntry<Object>("mn")).entrySet()) {
            timeunit = javaEntry130.getValue();

            if (!empty(gVars.webEnv._POST.getValue("hidden_" + strval(timeunit))) && !equal(gVars.webEnv._POST.getValue("hidden_" + strval(timeunit)), gVars.webEnv._POST.getValue(timeunit))) {
                gVars.webEnv._POST.putValue("edit_date", "1");

                break;
            }
        }

        if (!empty(gVars.webEnv._POST.getValue("edit_date"))) {
            aa = gVars.webEnv._POST.getValue("aa");
            mm = gVars.webEnv._POST.getValue("mm");
            jj = intval(gVars.webEnv._POST.getValue("jj"));
            hh = intval(gVars.webEnv._POST.getValue("hh"));
            mn = intval(gVars.webEnv._POST.getValue("mn"));
            ss = intval(gVars.webEnv._POST.getValue("ss"));
            jj = ((jj > 31)
                ? 31
                : jj);
            hh = ((hh > 23)
                ? (hh - 24)
                : hh);
            mn = ((mn > 59)
                ? (mn - 60)
                : mn);
            ss = ((ss > 59)
                ? (ss - 60)
                : ss);
            gVars.webEnv._POST.putValue("comment_date", strval(aa) + "-" + strval(mm) + "-" + strval(jj) + " " + strval(hh) + ":" + strval(mn) + ":" + strval(ss));
        }

        (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_update_comment(gVars.webEnv._POST);
    }

    public StdClass get_comment_to_edit(int id) {
        StdClass comment = null;

        if (!booleanval(comment = (StdClass) (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).get_comment(id, gConsts.getOBJECT()))) {
            return null;
        }

        comment.fields.putValue("comment_ID", intval(StdClass.getValue(comment, "comment_ID")));
        comment.fields.putValue("comment_post_ID", intval(StdClass.getValue(comment, "comment_post_ID")));
        comment.fields.putValue("comment_content", getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(StdClass.getValue(comment, "comment_content")), false));
        comment.fields.putValue("comment_content", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_edit_pre", StdClass.getValue(comment, "comment_content")));
        comment.fields.putValue("comment_author", getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(StdClass.getValue(comment, "comment_author")), false));
        comment.fields.putValue("comment_author_email", getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(StdClass.getValue(comment, "comment_author_email")), false));
        comment.fields.putValue("comment_author_url", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(StdClass.getValue(comment, "comment_author_url")), null, "display"));
        comment.fields.putValue("comment_author_url", getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(StdClass.getValue(comment, "comment_author_url")), false));

        return comment;
    }

    public Object get_pending_comments_num(Object post_id)/* Do not change type */
     {
        boolean single = false;
        Array<Object> pending = new Array<Object>();
        Array<Object> pending_keyed = new Array<Object>();
        Array<Object> pend = new Array<Object>();
        single = false;

        if (!is_array(post_id)) {
            post_id = new Array<Object>(post_id);
            single = true;
        }

        post_id = Array.array_map(new Callback("intval", VarHandling.class), (Array) post_id);

        String post_idStr = "\'" + Strings.implode("\', \'", (Array) post_id) + "\'";
        pending = gVars.wpdb.get_results(
                    "SELECT comment_post_ID, COUNT(comment_ID) as num_comments FROM " + gVars.wpdb.comments + " WHERE comment_post_ID IN ( " + post_idStr +
                    " ) AND comment_approved = \'0\' GROUP BY comment_post_ID",
                    gConsts.getARRAY_N());

        if (empty(pending)) {
            return null;
        }

        if (single) {
            return pending.getArrayValue(0).getValue(1);
        }

        pending_keyed = new Array<Object>();

        for (Map.Entry javaEntry131 : pending.entrySet()) {
            pend = (Array<Object>) javaEntry131.getValue();
            pending_keyed.putValue(pend.getValue(0), pend.getValue(1));
        }

        return pending_keyed;
    }

 // Add avatars to relevant places in admin, or try to
    public String floated_admin_avatar(String name) {
        Object id;
        String avatar;
        id = avatar = "";

        if (booleanval(StdClass.getValue(gVars.comment, "comment_author_email"))) {
            id = strval(StdClass.getValue(gVars.comment, "comment_author_email"));
        }

        if (booleanval(StdClass.getValue(gVars.comment, "user_id"))) {
            id = intval(StdClass.getValue(gVars.comment, "user_id"));
        }

        if (booleanval(id)) {
            avatar = getIncluded(PluggablePage.class, gVars, gConsts).get_avatar(id, 32, "");
        }

        return avatar + " " + name;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_comment_block1");
        gVars.webEnv = webEnv;

        if (getIncluded(QueryPage.class, gVars, gConsts).is_admin() && (equal("edit-comments.php", gVars.pagenow) || equal("edit.php", gVars.pagenow))) {
            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("show_avatars"))) {
                getIncluded(PluginPage.class, gVars, gConsts).add_filter("comment_author", Callback.createCallbackArray(this, "floated_admin_avatar"), 10, 1);
            }
        }

        return DEFAULT_VAL;
    }
}
