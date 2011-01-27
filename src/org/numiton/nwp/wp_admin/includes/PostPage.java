/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PostPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.UserPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QDateTime;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/PostPage")
@Scope("request")
public class PostPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(PostPage.class.getName());

    /**
     * Generated in place of local variable 'permalink' from method
     * 'get_sample_permalink_html' because it is used inside an inner class.
     */
    String get_sample_permalink_html_permalink = null;

    /**
     * Generated in place of local variable 'post_name' from method
     * 'get_sample_permalink_html' because it is used inside an inner class.
     */
    String get_sample_permalink_html_post_name = null;
    private int replace_id;

    @Override
    @RequestMapping("/wp-admin/includes/post.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/post";
    }

    /**
     * Update an existing post with values provided in $_POST.
     */
    public int edit_post() {
        int post_ID = 0;
        StdClass post;
        int now = 0;
        int then = 0;
        float delta = 0;
        Object timeunit = null;
        Object aa = null;
        Object mm = null;
        int jj = 0;
        int hh = 0;
        int mn = 0;
        int ss = 0;
        Object key = null;
        Array<Object> value = new Array<Object>();
        Array<Object> draft_ids = new Array<Object>();
        int draft_temp_id = 0;
        
        post_ID = intval(gVars.webEnv._POST.getValue("post_ID"));

        if (equal("page", gVars.webEnv._POST.getValue("post_type"))) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_page", post_ID)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this page.", "default"), "");
            }
        } else {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_post", post_ID)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit this post.", "default"), "");
            }
        }

    	// Autosave shouldn't save too soon after a real save
        if (equal("autosave", gVars.webEnv._POST.getValue("action"))) {
            post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(post_ID, gConsts.getOBJECT(), "raw");
            now = DateTime.time();
            then = QDateTime.strtotime(StdClass.getValue(post, "post_date_gmt") + " +0000");
            delta = gConsts.getAUTOSAVE_INTERVAL() / floatval(2);

            if (floatval(now - then) < delta) {
                return post_ID;
            }
        }

    	// Rename.
        gVars.webEnv._POST.putValue("ID", intval(gVars.webEnv._POST.getValue("post_ID")));
        gVars.webEnv._POST.putValue("post_content", gVars.webEnv._POST.getValue("content"));
        gVars.webEnv._POST.putValue("post_excerpt", gVars.webEnv._POST.getValue("excerpt"));
        gVars.webEnv._POST.putValue("post_parent", isset(gVars.webEnv._POST.getValue("parent_id"))
            ? strval(gVars.webEnv._POST.getValue("parent_id"))
            : "");
        gVars.webEnv._POST.putValue("to_ping", gVars.webEnv._POST.getValue("trackback_url"));

        if (!empty(gVars.webEnv._POST.getValue("post_author_override"))) {
            gVars.webEnv._POST.putValue("post_author", intval(gVars.webEnv._POST.getValue("post_author_override")));
        } else if (!empty(gVars.webEnv._POST.getValue("post_author"))) {
            gVars.webEnv._POST.putValue("post_author", intval(gVars.webEnv._POST.getValue("post_author")));
        } else {
            gVars.webEnv._POST.putValue("post_author", intval(gVars.webEnv._POST.getValue("user_ID")));
        }

        if (!equal(gVars.webEnv._POST.getValue("post_author"), gVars.webEnv._POST.getValue("user_ID"))) {
            if (equal("page", gVars.webEnv._POST.getValue("post_type"))) {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_pages")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit pages as this user.", "default"), "");
                }
            } else {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_posts")) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to edit posts as this user.", "default"), "");
                }
            }
        }

    	// What to do based on which button they pressed
        if (isset(gVars.webEnv._POST.getValue("saveasdraft")) && !equal("", gVars.webEnv._POST.getValue("saveasdraft"))) {
            gVars.webEnv._POST.putValue("post_status", "draft");
        }

        if (isset(gVars.webEnv._POST.getValue("saveasprivate")) && !equal("", gVars.webEnv._POST.getValue("saveasprivate"))) {
            gVars.webEnv._POST.putValue("post_status", "private");
        }

        if (isset(gVars.webEnv._POST.getValue("publish")) && !equal("", gVars.webEnv._POST.getValue("publish")) && !equal(gVars.webEnv._POST.getValue("post_status"), "private")) {
            gVars.webEnv._POST.putValue("post_status", "publish");
        }

        if (isset(gVars.webEnv._POST.getValue("advanced")) && !equal("", gVars.webEnv._POST.getValue("advanced"))) {
            gVars.webEnv._POST.putValue("post_status", "draft");
        }

        if (equal("page", gVars.webEnv._POST.getValue("post_type"))) {
            if (equal("publish", gVars.webEnv._POST.getValue("post_status")) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_pages")) {
                gVars.webEnv._POST.putValue("post_status", "pending");
            }
        } else {
            if (equal("publish", gVars.webEnv._POST.getValue("post_status")) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
                gVars.webEnv._POST.putValue("post_status", "pending");
            }
        }

        if (!isset(gVars.webEnv._POST.getValue("comment_status"))) {
            gVars.webEnv._POST.putValue("comment_status", "closed");
        }

        if (!isset(gVars.webEnv._POST.getValue("ping_status"))) {
            gVars.webEnv._POST.putValue("ping_status", "closed");
        }

        for (Map.Entry javaEntry185 : new Array<Object>(
                new ArrayEntry<Object>("aa"),
                new ArrayEntry<Object>("mm"),
                new ArrayEntry<Object>("jj"),
                new ArrayEntry<Object>("hh"),
                new ArrayEntry<Object>("mn")).entrySet()) {
            timeunit = javaEntry185.getValue();

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
            gVars.webEnv._POST.putValue("post_date", strval(aa) + "-" + strval(mm) + "-" + strval(jj) + " " + strval(hh) + ":" + strval(mn) + ":" + strval(ss));
            gVars.webEnv._POST.putValue(
                "post_date_gmt",
                getIncluded(FormattingPage.class, gVars, gConsts).get_gmt_from_date(strval(aa) + "-" + strval(mm) + "-" + strval(jj) + " " + strval(hh) + ":" + strval(mn) + ":" + strval(ss)));
        }

    	// Meta Stuff
        if (isset(gVars.webEnv._POST.getValue("meta")) && booleanval(gVars.webEnv._POST.getValue("meta"))) {
            for (Map.Entry javaEntry186 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("meta").entrySet()) {
                key = javaEntry186.getKey();
                value = (Array<Object>) javaEntry186.getValue();
                update_meta(intval(key), strval(value.getValue("key")), strval(value.getValue("value")));
            }
        }

        if (isset(gVars.webEnv._POST.getValue("deletemeta")) && booleanval(gVars.webEnv._POST.getValue("deletemeta"))) {
            for (Map.Entry javaEntry187 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("deletemeta").entrySet()) {
                key = javaEntry187.getKey();

                //				value = javaEntry187.getValue();
                delete_meta(intval(key));
            }
        }

        add_meta(post_ID);
        (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_post(gVars.webEnv._POST);

    	// Reunite any orphaned attachments with their parent
        if (!booleanval(draft_ids = (Array<Object>) getIncluded(UserPage.class, gVars, gConsts).get_user_option("autosave_draft_ids")))/*, 0*/
         {
            draft_ids = new Array<Object>();
        }

        if (booleanval(draft_temp_id = intval(Array.array_search(post_ID, draft_ids)))) {
            _relocate_children(draft_temp_id, post_ID);
        }

    	// Now that we have an ID we can fix any attachment anchor hrefs
        _fix_attachment_links(post_ID);
        wp_set_post_lock(post_ID);

        /*, gVars.current_user.getID()*/
        return post_ID;
    }

    /**
     * Default post information to use when populating the "Write Post" form.
     */
    public StdClass get_default_post_to_edit() {
        String post_title = null;
        String post_content = null;
        String text = null;
        String popupurl = null;
        String post_excerpt = null;
        StdClass post = new StdClass();

        if (!empty(gVars.webEnv._REQUEST.getValue("post_title"))) {
            post_title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("post_title"))), strval(0));
        } else if (!empty(gVars.webEnv._REQUEST.getValue("popuptitle"))) {
            post_title = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("popuptitle"))), strval(0));
            post_title = getIncluded(FormattingPage.class, gVars, gConsts).funky_javascript_fix(post_title);
        } else {
            post_title = "";
        }

        post_content = "";

        if (!empty(gVars.webEnv._REQUEST.getValue("content"))) {
            post_content = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("content"))), strval(0));
        } else if (!empty(post_title)) {
            text = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, URL.urldecode(strval(gVars.webEnv._REQUEST.getValue("text")))), strval(0));
            text = getIncluded(FormattingPage.class, gVars, gConsts).funky_javascript_fix(text);
            popupurl = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(gVars.webEnv._REQUEST.getValue("popupurl")), null, "display");
            post_content = "<a href=\"" + popupurl + "\">" + post_title + "</a>" + "\n" + text;
        }

        if (!empty(gVars.webEnv._REQUEST.getValue("excerpt"))) {
            post_excerpt = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("excerpt"))), strval(0));
        } else {
            post_excerpt = "";
        }

        post.fields.putValue("ID", 0);
        post.fields.putValue("post_name", "");
        post.fields.putValue("post_author", "");
        post.fields.putValue("post_date", "");
        post.fields.putValue("post_status", "draft");
        post.fields.putValue("post_type", "post");
        post.fields.putValue("to_ping", "");
        post.fields.putValue("pinged", "");
        post.fields.putValue("comment_status", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_comment_status"));
        post.fields.putValue("ping_status", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_ping_status"));
        post.fields.putValue("post_pingback", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_pingback_flag"));
        post.fields.putValue("post_category", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_category"));
        post.fields.putValue("post_content", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("default_content", post_content));
        post.fields.putValue("post_title", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("default_title", post_title));
        post.fields.putValue("post_excerpt", getIncluded(PluginPage.class, gVars, gConsts).apply_filters("default_excerpt", post_excerpt));
        post.fields.putValue("page_template", "default");
        post.fields.putValue("post_parent", 0);
        post.fields.putValue("menu_order", 0);

        return post;
    }

    public StdClass get_default_page_to_edit() {
        StdClass page = null;
        page = get_default_post_to_edit();
        page.fields.putValue("post_type", "page");

        return page;
    }

    /**
     * Get an existing post and format it for editing.
     */
    public StdClass get_post_to_edit(int id) {
        StdClass post = null;
        post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(id, gConsts.getOBJECT(), "edit");

        if (equal(StdClass.getValue(post, "post_type"), "page")) {
            post.fields.putValue("page_template", (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post_meta(id, "_wp_page_template", true));
        }

        return post;
    }

    public int post_exists(String title, String content, String post_date) {
        if (!empty(post_date)) {
            post_date = "AND post_date = \'" + post_date + "\'";
        }

        if (!empty(title)) {
            return intval(gVars.wpdb.get_var("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_title = \'" + title + "\' " + post_date));
        } else if (!empty(content)) {
            return intval(gVars.wpdb.get_var("SELECT ID FROM " + gVars.wpdb.posts + " WHERE post_content = \'" + content + "\' " + post_date));
        }

        return 0;
    }

    /**
     * Creates a new post from the "Write Post" form using $_POST information.
     */
    public Object wp_write_post() {
        int temp_id = 0;
        Array<Object> draft_ids = new Array<Object>();
        Object temp = null;
        Object real = null;
        Object timeunit = null;
        Object aa = null;
        Object mm = null;
        int jj = 0;
        int hh = 0;
        int mn = 0;
        int ss = 0;
        Object post_ID = 0;
        int draft_temp_id = 0;

        if (equal("page", gVars.webEnv._POST.getValue("post_type"))) {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_pages")) {
                return new WP_Error(gVars, gConsts, "edit_pages", getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to create pages on this blog.", "default"));
            }
        } else {
            if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_posts")) {
                return new WP_Error(gVars, gConsts, "edit_posts", getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to create posts or drafts on this blog.", "default"));
            }
        }

        // Check for autosave collisions
        temp_id = intval(false);

        if (isset(gVars.webEnv._POST.getValue("temp_ID"))) {
            temp_id = intval(gVars.webEnv._POST.getValue("temp_ID"));

            if (!booleanval(draft_ids = (Array<Object>) getIncluded(UserPage.class, gVars, gConsts).get_user_option("autosave_draft_ids")))/*, 0*/
             {
                draft_ids = new Array<Object>();
            }

            for (Map.Entry javaEntry188 : draft_ids.entrySet()) {
                temp = javaEntry188.getKey();
                real = javaEntry188.getValue();

                if ((DateTime.time() + intval(temp)) > 86400) { // 1 day: $temp is equal to -1 * time( then )
                    draft_ids.arrayUnset(temp);
                }
            }

            if (isset(draft_ids.getValue(temp_id))) { // Edit, don't write
                gVars.webEnv._POST.putValue("post_ID", draft_ids.getValue(temp_id));
                gVars.webEnv._POST.arrayUnset("temp_ID");
                getIncluded(UserPage.class, gVars, gConsts).update_user_option(gVars.user_ID, "autosave_draft_ids", draft_ids, false);

                return edit_post();
            }
        }

        // Rename.
        gVars.webEnv._POST.putValue("post_content", gVars.webEnv._POST.getValue("content"));
        gVars.webEnv._POST.putValue("post_excerpt", gVars.webEnv._POST.getValue("excerpt"));
        gVars.webEnv._POST.putValue("post_parent", isset(gVars.webEnv._POST.getValue("parent_id"))
            ? strval(gVars.webEnv._POST.getValue("parent_id"))
            : "");
        gVars.webEnv._POST.putValue("to_ping", gVars.webEnv._POST.getValue("trackback_url"));

        if (!empty(gVars.webEnv._POST.getValue("post_author_override"))) {
            gVars.webEnv._POST.putValue("post_author", intval(gVars.webEnv._POST.getValue("post_author_override")));
        } else {
            if (!empty(gVars.webEnv._POST.getValue("post_author"))) {
                gVars.webEnv._POST.putValue("post_author", intval(gVars.webEnv._POST.getValue("post_author")));
            } else {
                gVars.webEnv._POST.putValue("post_author", intval(gVars.webEnv._POST.getValue("user_ID")));
            }
        }

        if (!equal(gVars.webEnv._POST.getValue("post_author"), gVars.webEnv._POST.getValue("user_ID"))) {
            if (equal("page", gVars.webEnv._POST.getValue("post_type"))) {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_pages")) {
                    return new WP_Error(gVars, gConsts, "edit_others_pages", getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to create pages as this user.", "default"));
                }
            } else {
                if (!getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("edit_others_posts")) {
                    return new WP_Error(gVars, gConsts, "edit_others_posts", getIncluded(L10nPage.class, gVars, gConsts).__("You are not allowed to post as this user.", "default"));
                }
            }
        }

    	// What to do based on which button they pressed
        if (isset(gVars.webEnv._POST.getValue("saveasdraft")) && !equal("", gVars.webEnv._POST.getValue("saveasdraft"))) {
            gVars.webEnv._POST.putValue("post_status", "draft");
        }

        if (isset(gVars.webEnv._POST.getValue("saveasprivate")) && !equal("", gVars.webEnv._POST.getValue("saveasprivate"))) {
            gVars.webEnv._POST.putValue("post_status", "private");
        }

        if (isset(gVars.webEnv._POST.getValue("publish")) && !equal("", gVars.webEnv._POST.getValue("publish")) && !equal(gVars.webEnv._POST.getValue("post_status"), "private")) {
            gVars.webEnv._POST.putValue("post_status", "publish");
        }

        if (isset(gVars.webEnv._POST.getValue("advanced")) && !equal("", gVars.webEnv._POST.getValue("advanced"))) {
            gVars.webEnv._POST.putValue("post_status", "draft");
        }

        if (equal("page", gVars.webEnv._POST.getValue("post_type"))) {
            if (equal("publish", gVars.webEnv._POST.getValue("post_status")) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_pages")) {
                gVars.webEnv._POST.putValue("post_status", "pending");
            }
        } else {
            if (equal("publish", gVars.webEnv._POST.getValue("post_status")) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
                gVars.webEnv._POST.putValue("post_status", "pending");
            }
        }

        if (!isset(gVars.webEnv._POST.getValue("comment_status"))) {
            gVars.webEnv._POST.putValue("comment_status", "closed");
        }

        if (!isset(gVars.webEnv._POST.getValue("ping_status"))) {
            gVars.webEnv._POST.putValue("ping_status", "closed");
        }

        for (Map.Entry javaEntry189 : new Array<Object>(
                new ArrayEntry<Object>("aa"),
                new ArrayEntry<Object>("mm"),
                new ArrayEntry<Object>("jj"),
                new ArrayEntry<Object>("hh"),
                new ArrayEntry<Object>("mn")).entrySet()) {
            timeunit = javaEntry189.getValue();

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
            gVars.webEnv._POST.putValue("post_date", QStrings.sprintf("%04d-%02d-%02d %02d:%02d:%02d", aa, mm, jj, hh, mn, ss));
            gVars.webEnv._POST.putValue("post_date_gmt", getIncluded(FormattingPage.class, gVars, gConsts).get_gmt_from_date(strval(gVars.webEnv._POST.getValue("post_date"))));
        }

    	// Create the post.
        post_ID = getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts).wp_insert_post(gVars.webEnv._POST);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_ID)) {
            return post_ID;
        }

        if (empty(post_ID)) {
            return 0;
        }

        add_meta(intval(post_ID));

    	// Reunite any orphaned attachments with their parent
        if (!booleanval(draft_ids = (Array<Object>) getIncluded(UserPage.class, gVars, gConsts).get_user_option("autosave_draft_ids"))) {
            draft_ids = new Array<Object>();
        }

        if (booleanval(draft_temp_id = intval(Array.array_search(post_ID, draft_ids)))) {
            _relocate_children(draft_temp_id, intval(post_ID));
        }

        if (booleanval(temp_id) && !equal(temp_id, draft_temp_id)) {
            _relocate_children(temp_id, intval(post_ID));
        }

    	// Update autosave collision detection
        if (booleanval(temp_id)) {
            draft_ids.putValue(temp_id, post_ID);
            getIncluded(UserPage.class, gVars, gConsts).update_user_option(gVars.user_ID, "autosave_draft_ids", draft_ids, false);
        }

    	// Now that we have an ID we can fix any attachment anchor hrefs
        _fix_attachment_links(intval(post_ID));
        wp_set_post_lock(intval(post_ID));

        /*, gVars.current_user.getID()*/
        return post_ID;
    }

    public int write_post() {
        Object result;
        result = wp_write_post();

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(((WP_Error) result).get_error_message(), "");
        } else {
            return intval(result);
        }

        return 0;
    }

    /**
     * Post Meta
     */
    public int add_meta(int post_ID) {
        Array<Object> _protected = new Array<Object>();
        String metakeyselect = null;
        String metakeyinput = null;
        String metavalue = null;
        String metakey = null;
        post_ID = post_ID;
        _protected = new Array<Object>(
                new ArrayEntry<Object>("_wp_attached_file"),
                new ArrayEntry<Object>("_wp_attachment_metadata"),
                new ArrayEntry<Object>("_wp_old_slug"),
                new ArrayEntry<Object>("_wp_page_template"));
        metakeyselect = gVars.wpdb.escape(Strings.stripslashes(gVars.webEnv, Strings.trim(strval(gVars.webEnv._POST.getValue("metakeyselect")))));
        metakeyinput = gVars.wpdb.escape(Strings.stripslashes(gVars.webEnv, Strings.trim(strval(gVars.webEnv._POST.getValue("metakeyinput")))));
        metavalue = strval(getIncluded(FunctionsPage.class, gVars, gConsts).maybe_serialize(Strings.stripslashes(gVars.webEnv, Strings.trim(strval(gVars.webEnv._POST.getValue("metavalue"))))));
        metavalue = gVars.wpdb.escape(metavalue);

        if ((strictEqual("0", metavalue) || !empty(metavalue)) && ((!equal("#NONE#", metakeyselect) && !empty(metakeyselect)) || !empty(metakeyinput))) {
    		// We have a key/value pair. If both the select and the
    		// input for the key have data, the input takes precedence:
        	
            if (!equal("#NONE#", metakeyselect)) {
                metakey = metakeyselect;
            }

            if (booleanval(metakeyinput)) {
                metakey = metakeyinput; // default
            }

            if (Array.in_array(metakey, _protected)) {
                return intval(false);
            }

            getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(post_ID, "post_meta");
            gVars.wpdb.query(
                    "\n\t\t\t\tINSERT INTO " + gVars.wpdb.postmeta + "\n\t\t\t\t(post_id,meta_key,meta_value )\n\t\t\t\tVALUES (\'" + post_ID + "\',\'" + metakey + "\',\'" + metavalue +
                    "\' )\n\t\t\t");

            return gVars.wpdb.insert_id;
        }

        return intval(false);
    } // add_meta

    /**
     * add_meta add_meta
     */
    public int delete_meta(int mid) {
        Object post_id = null;
        mid = mid;
        post_id = gVars.wpdb.get_var("SELECT post_id FROM " + gVars.wpdb.postmeta + " WHERE meta_id = \'" + mid + "\'");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(post_id, "post_meta");

        return gVars.wpdb.query("DELETE FROM " + gVars.wpdb.postmeta + " WHERE meta_id = \'" + mid + "\'");
    }

    /**
     * Get a list of previously defined keys
     */
    public Array<Object> get_meta_keys() {
        Array<Object> keys = new Array<Object>();
        keys = gVars.wpdb.get_col("\n\t\t\tSELECT meta_key\n\t\t\tFROM " + gVars.wpdb.postmeta + "\n\t\t\tGROUP BY meta_key\n\t\t\tORDER BY meta_key");

        return keys;
    }

    public StdClass get_post_meta_by_id(Object mid) {
        StdClass meta;
        mid = intval(mid);
        meta = (StdClass) gVars.wpdb.get_row("SELECT * FROM " + gVars.wpdb.postmeta + " WHERE meta_id = \'" + mid + "\'");

        if (getIncluded(FunctionsPage.class, gVars, gConsts).is_serialized_string(StdClass.getValue(meta, "meta_value"))) {
            meta.fields.putValue("meta_value", getIncluded(FunctionsPage.class, gVars, gConsts).maybe_unserialize(strval(StdClass.getValue(meta, "meta_value"))));
        }

        return meta;
    }

    /**
     * Some postmeta stuff
     */
    public Array<Object> has_meta(int postid) {
        return gVars.wpdb.get_results(
                "\n\t\t\tSELECT meta_key, meta_value, meta_id, post_id\n\t\t\tFROM " + gVars.wpdb.postmeta + "\n\t\t\tWHERE post_id = \'" + postid + "\'\n\t\t\tORDER BY meta_key,meta_id",
                gConsts.getARRAY_A());
    }

    public int update_meta(int mid, String mkey, String mvalue) {
        Array<Object> _protected = new Array<Object>();
        Object post_id = null;
        _protected = new Array<Object>(
                new ArrayEntry<Object>("_wp_attached_file"),
                new ArrayEntry<Object>("_wp_attachment_metadata"),
                new ArrayEntry<Object>("_wp_old_slug"),
                new ArrayEntry<Object>("_wp_page_template"));

        if (Array.in_array(mkey, _protected)) {
            return intval(false);
        }

        post_id = gVars.wpdb.get_var("SELECT post_id FROM " + gVars.wpdb.postmeta + " WHERE meta_id = \'" + mid + "\'");
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(post_id, "post_meta");
        mvalue = strval(getIncluded(FunctionsPage.class, gVars, gConsts).maybe_serialize(Strings.stripslashes(gVars.webEnv, mvalue)));
        mvalue = gVars.wpdb.escape(mvalue);
        mid = mid;

        return gVars.wpdb.query("UPDATE " + gVars.wpdb.postmeta + " SET meta_key = \'" + mkey + "\', meta_value = \'" + mvalue + "\' WHERE meta_id = \'" + mid + "\'");
    }

	//
	// Private
	//
	
	// Replace hrefs of attachment anchors with up-to-date permalinks.
    public int _fix_attachment_links(int post_ID) {
        Array<Object> post = new Array<Object>();
        String search = null;
        Array anchor_matches = new Array();
        int i = 0;
        String anchor = null;
        Array<Object> id_matches = new Array<Object>();
        int id = 0;
        Array<Object> attachment = new Array<Object>();
        Array<Object> post_search = new Array<Object>();
        Array<Object> post_replace = new Array<Object>();
        post = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(post_ID, gConsts.getARRAY_A(), "raw");
        search = "#<a[^>]+rel=(\'|\")[^\'\"]*attachment[^>]*>#ie";

        // See if we have any rel="attachment" links
        if (equal(0, QRegExPerl.preg_match_all(search, strval(post.getValue("post_content")), anchor_matches, RegExPerl.PREG_PATTERN_ORDER))) {
            return 0;
        }

        i = 0;
        search = "#[\\s]+rel=(\"|\')(.*?)wp-att-(\\d+)\\1#i";

        for (Map.Entry javaEntry190 : (Set<Map.Entry>) anchor_matches.getArrayValue(0).entrySet()) {
            anchor = strval(javaEntry190.getValue());

            if (equal(0, QRegExPerl.preg_match(search, anchor, id_matches))) {
                continue;
            }

            id = intval(id_matches.getValue(3));
            
            // While we have the attachment ID, let's adopt any orphans.
            attachment = (Array<Object>) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(id, gConsts.getARRAY_A(), "raw");

            if (!empty(attachment) &&
                    !is_object(
                        (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(attachment.getValue("post_parent"), gConsts.getOBJECT(), "raw"))) {
                attachment.putValue("post_parent", post_ID);
                // Escape data pulled from DB.
                attachment = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(attachment);
                (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_post(attachment);
            }

            post_search.putValue(i, anchor);

            // Modified by Numiton
            replace_id = id;
            post_replace.putValue(i, RegExPerl.preg_replace_callback("#href=(\"|\')[^\'\"]*\\1#", new Callback("replaceStripLink", this), anchor));
            ++i;
        }

        post.putValue("post_content", Strings.str_replace(post_search, post_replace, post.getArrayValue("post_content")));
        
        // Escape data pulled from DB.
        post = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(post);

        return (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_update_post(post);
    }

    public String replaceStripLink(Array matches) {
        return Strings.stripslashes(gVars.webEnv, "href=" + matches.getValue(1)) + getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(replace_id) +
        Strings.stripslashes(gVars.webEnv, strval(matches.getValue(1)));
    }

    /**
     * Move child posts to a new parent
     */
    public int _relocate_children(int old_ID, int new_ID) {
        //		old_ID = intval(old_ID);
        //		new_ID = intval(new_ID);
        return gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_parent = " + new_ID + " WHERE post_parent = " + old_ID);
    }

    public Array<Object> get_available_post_statuses(String type) {
        StdClass stati = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_count_posts(type, "");

        return Array.array_keys(ClassHandling.get_object_vars(stati));
    }

    public Array<Object> wp_edit_posts_query(Array<Object> q) {
        Array<Object> post_stati = new Array<Object>();
        Array<Object> avail_post_stati = new Array<Object>();
        String post_status_q = null;
        String order = null;
        String orderby = null;

        // Modified by Numiton
        if (strictEqual(null, q)) {
            q = gVars.webEnv._GET;
        }

        q.putValue("m", intval(q.getValue("m")));
        q.putValue("cat", intval(q.getValue("cat")));
        post_stati = new Array<Object>(	//	array( adj, noun )
                new ArrayEntry<Object>("publish",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Published", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Published posts", "default")),
                        new ArrayEntry<Object>((((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__ngettext_noop("Published (%s)", "Published (%s)", 1, "default")))),
                new ArrayEntry<Object>("future",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Scheduled", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Scheduled posts", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Scheduled (%s)", "Scheduled (%s)", 1, "default")))),
                new ArrayEntry<Object>("pending",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Pending Review", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Pending posts", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Pending Review (%s)", "Pending Review (%s)", 1, "default")))),
                new ArrayEntry<Object>("draft",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Draft", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts)._c("Drafts|manage posts header", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Draft (%s)", "Drafts (%s)", 1, "default")))),
                new ArrayEntry<Object>("private",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Private", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Private posts", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Private (%s)", "Private (%s)", 1, "default")))));
        post_stati = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_stati", post_stati);
        avail_post_stati = get_available_post_statuses("post");
        post_status_q = "";

        if (isset(q.getValue("post_status")) && Array.in_array(q.getValue("post_status"), Array.array_keys(post_stati))) {
            post_status_q = "&post_status=" + strval(q.getValue("post_status"));
            post_status_q = post_status_q + "&perm=readable";
        }

        if (strictEqual("pending", q.getValue("post_status"))) {
            order = "ASC";
            orderby = "modified";
        } else if (strictEqual("draft", q.getValue("post_status"))) {
            order = "DESC";
            orderby = "modified";
        } else {
            order = "DESC";
            orderby = "date";
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).wp("post_type=post&what_to_show=posts" + post_status_q + "&posts_per_page=15&order=" + order + "&orderby=" + orderby);

        return new Array<Object>(new ArrayEntry<Object>(post_stati), new ArrayEntry<Object>(avail_post_stati));
    }

    public Array<Object> get_available_post_mime_types(String type) {
        Array<Object> types = new Array<Object>();
        types = gVars.wpdb.get_col(gVars.wpdb.prepare("SELECT DISTINCT post_mime_type FROM " + gVars.wpdb.posts + " WHERE post_type = %s", type));

        return types;
    }

    public Array<Object> wp_edit_attachments_query(Array<Object> q) {
        Array<Object> post_mime_types = new Array<Object>();
        Array<Object> avail_post_mime_types = new Array<Object>();

        // Modified by Numiton
        if (strictEqual(null, q)) {
            q = gVars.webEnv._GET;
        }

        q.putValue("m", intval(q.getValue("m")));
        q.putValue("cat", intval(q.getValue("cat")));
        q.putValue("post_type", "attachment");
        q.putValue("post_status", "any");
        q.putValue("posts_per_page", 15);
        post_mime_types = new Array<Object>(	//	array( adj, noun )
                new ArrayEntry<Object>("image",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Images", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Manage Images", "default")),
                        new ArrayEntry<Object>((((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__ngettext_noop("Image (%s)", "Images (%s)", 1, "default")))),
                new ArrayEntry<Object>("audio",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Audio", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Manage Audio", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__ngettext_noop("Audio (%s)", "Audio (%s)", 1, "default")))),
                new ArrayEntry<Object>("video",
                    new Array<Object>(new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Video", "default")),
                        new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Manage Video", "default")),
                        new ArrayEntry<Object>((((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__ngettext_noop("Video (%s)", "Video (%s)", 1, "default")))));
        post_mime_types = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_mime_types", post_mime_types);
        avail_post_mime_types = get_available_post_mime_types("attachment");

        if (isset(q.getValue("post_mime_type")) && !booleanval(Array.array_intersect(new Array<Object>(q.getValue("post_mime_type")), Array.array_keys(post_mime_types)))) {
            q.arrayUnset("post_mime_type");
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).wp(q);

        return new Array<Object>(new ArrayEntry<Object>(post_mime_types), new ArrayEntry<Object>(avail_post_mime_types));
    }

    public String postbox_classes(String id, String page) {
        WP_User current_user = null;
        Object closed = null;

        /* Do not change type */
        current_user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();

        if (booleanval(closed = getIncluded(UserPage.class, gVars, gConsts).get_usermeta(current_user.getID(), "closedpostboxes_" + page))) {
            if (!is_array(closed)) {
                return "";
            }

            return Array.in_array(id, (Array) closed)
            ? "if-js-closed"
            : "";
        } else {
            if (equal("tagsdiv", id) || equal("categorydiv", id)) {
                return "";
            } else {
                return "if-js-closed";
            }
        }
    }

    public Array<Object> get_sample_permalink(Object id, String title, String name) {
        StdClass post;
        Object original_status = null;
        Object original_date = null;
        Object original_name = null;
        String permalink;
        String uri = null;
        Object original_title = null;
        post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(id, gConsts.getOBJECT(), "raw");

        if (!booleanval(StdClass.getValue(post, "ID"))) {
            return new Array<Object>(new ArrayEntry<Object>(""), new ArrayEntry<Object>(""));
        }

        original_status = StdClass.getValue(post, "post_status");
        original_date = StdClass.getValue(post, "post_date");
        original_name = StdClass.getValue(post, "post_name");

    	// Hack: get_permalink would return ugly permalink for
    	// drafts, so we will fake, that our post is published
        if (Array.in_array(StdClass.getValue(post, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
            post.fields.putValue("post_status", "publish");
            post.fields.putValue("post_date", DateTime.date("Y-m-d H:i:s"));
            post.fields.putValue("post_name",
                getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(booleanval(StdClass.getValue(post, "post_name"))
                        ? StdClass.getValue(post, "post_name")
                        : StdClass.getValue(post, "post_title")), strval(StdClass.getValue(post, "ID"))));
        }

    	// If the user wants to set a new name -- override the current one
    	// Note: if empty name is supplied -- use the title instead, see #6072
        if (!is_null(name)) {
            post.fields.putValue("post_name", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(booleanval(name)
                    ? name
                    : title, strval(StdClass.getValue(post, "ID"))));
        }

        permalink = getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(post, true);

    	// Handle page hierarchy
        if (equal("page", StdClass.getValue(post, "post_type"))) {
            uri = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_page_uri(intval(StdClass.getValue(post, "ID")));
            uri = getIncluded(FormattingPage.class, gVars, gConsts).untrailingslashit(uri);
            uri = Strings.strrev(Strings.stristr(Strings.strrev(uri), "/"));
            uri = getIncluded(FormattingPage.class, gVars, gConsts).untrailingslashit(uri);

            if (!empty(uri)) {
                uri = uri + "/";
            }

            permalink = Strings.str_replace("%pagename%", uri + "%pagename%", permalink);
        }

        Array<Object> permalinkArray = new Array<Object>(new ArrayEntry<Object>(permalink), new ArrayEntry<Object>(StdClass.getValue(post, "post_name")));
        post.fields.putValue("post_status", original_status);
        post.fields.putValue("post_date", original_date);
        post.fields.putValue("post_name", original_name);
        post.fields.putValue("post_title", original_title);

        return permalinkArray;
    }

    public String get_sample_permalink_html(Object id, String new_title, String new_slug) {
        StdClass post;
        Object title = null;
        String post_name_abridged = null;
        String post_name_html = null;
        String display_link;
        String _return = null;
        post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(id, gConsts.getOBJECT(), "raw");
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    get_sample_permalink_html_permalink = strval(srcArray.getValue(0));
                    get_sample_permalink_html_post_name = strval(srcArray.getValue(1));

                    return srcArray;
                }
            }.doAssign(get_sample_permalink(StdClass.getValue(post, "ID"), new_title, new_slug));

        if (strictEqual(BOOLEAN_FALSE, Strings.strpos(get_sample_permalink_html_permalink, "%postname%")) &&
                strictEqual(BOOLEAN_FALSE, Strings.strpos(get_sample_permalink_html_permalink, "%pagename%"))) {
            return "";
        }

        title = getIncluded(L10nPage.class, gVars, gConsts).__("Click to edit this part of the permalink", "default");

        if (Strings.strlen(get_sample_permalink_html_post_name) > 30) {
            post_name_abridged = Strings.substr(get_sample_permalink_html_post_name, 0, 14) + "&hellip;" + Strings.substr(get_sample_permalink_html_post_name, -14);
        } else {
            post_name_abridged = get_sample_permalink_html_post_name;
        }

        post_name_html = "<span id=\"editable-post-name\" title=\"" + strval(title) + "\">" + post_name_abridged + "</span><span id=\"editable-post-name-full\">" +
            get_sample_permalink_html_post_name + "</span>";
        display_link = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("%pagename%"), new ArrayEntry<Object>("%postname%")), post_name_html, get_sample_permalink_html_permalink);
        _return = "<strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Permalink:", "default") + "</strong>\n" + "<span id=\"sample-permalink\">" + display_link + "</span>\n";
        _return = _return + "<span id=\"edit-slug-buttons\"><a href=\"#post_name\" class=\"edit-slug\" onclick=\"edit_permalink(" + strval(id) + "); return false;\">" +
            getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default") + "</a></span>\n";

        return _return;
    }

 // false: not locked or locked by current user
 // int: user ID of user with lock
    public int wp_check_post_lock(Object post_id) {
        StdClass post = null;
        int lock;
        int last;
        Object time_window = null;

        if (!booleanval(post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return intval(false);
        }

        lock = intval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post_meta(intval(StdClass.getValue(post, "ID")), "_edit_lock", true));
        last = intval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post_meta(intval(StdClass.getValue(post, "ID")), "_edit_last", true));
        time_window = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_check_post_lock_window", gConsts.getAUTOSAVE_INTERVAL() * 2);

        if (booleanval(lock) && (lock > (DateTime.time() - intval(time_window))) && !equal(last, gVars.current_user.getID())) {
            return last;
        }

        return intval(false);
    }

    public boolean wp_set_post_lock(int post_id) {
        StdClass post = null;
        int now = 0;

        if (!booleanval(post = (StdClass) (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post(post_id, gConsts.getOBJECT(), "raw"))) {
            return false;
        }

        if (!booleanval(gVars.current_user) || !booleanval(gVars.current_user.getID())) {
            return false;
        }

        now = DateTime.time();

        if (!(((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).add_post_meta(intval(StdClass.getValue(post, "ID")), "_edit_lock", now, true)) {
            (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).update_post_meta(intval(StdClass.getValue(post, "ID")), "_edit_lock", now, "");
        }

        if (!(((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).add_post_meta(
                    intval(StdClass.getValue(post, "ID")),
                    "_edit_last",
                    gVars.current_user.getID(),
                    true)) {
            (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).update_post_meta(
                intval(StdClass.getValue(post, "ID")),
                "_edit_last",
                gVars.current_user.getID(),
                "");
        }

        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
