/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_form_advancedPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_admin.includes.UserPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_form_advancedPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_form_advancedPage.class.getName());
    public Object form_pingback;
    public Object form_prevstatus;
    public Object form_trackback;
    public Object pings;
    public Array<String> already_pinged;
    public String pinged_url;
    public Object saveasdraft;
    public Object popular_ids;

    @Override
    @RequestMapping("/wp-admin/edit-form-advanced.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_form_advanced";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block1");
        gVars.action = (isset(gVars.action)
            ? strval(gVars.action)
            : "");

        if (isset(gVars.webEnv._GET.getValue("message"))) {
            gVars.webEnv._GET.putValue("message", getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("message")));
        }

        gVars.messages.putValue(1,
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Post updated. Continue editing below or <a href=\"%s\">go back</a>.", "default"),
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("_wp_original_http_referer"))))));
        gVars.messages.putValue(2, getIncluded(L10nPage.class, gVars, gConsts).__("Custom field updated.", "default"));
        gVars.messages.putValue(3, getIncluded(L10nPage.class, gVars, gConsts).__("Custom field deleted.", "default"));
        gVars.messages.putValue(4, getIncluded(L10nPage.class, gVars, gConsts).__("Post updated.", "default"));

        if (isset(gVars.webEnv._GET.getValue("message"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p>");
            echo(gVars.webEnv, gVars.messages.getValue(gVars.webEnv._GET.getValue("message")));
            echo(gVars.webEnv, "</p></div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block2");

        if ((isset(gVars.mode) && equal("bookmarklet", gVars.mode)) || isset(gVars.webEnv._GET.getValue("popupurl"))) {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"mode\" value=\"bookmarklet\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block3");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Write Post", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block4");

        if (!isset(gVars.post_ID) || equal(0, gVars.post_ID)) {
            gVars.form_action = "post";
            gVars.temp_ID = -1 * DateTime.time(); // don't change this formula without looking at wp_write_post()
            gVars.form_extra = "<input type=\'hidden\' id=\'post_ID\' name=\'temp_ID\' value=\'" + strval(gVars.temp_ID) + "\' />";
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-post", "_wpnonce", true, true);
        } else {
            gVars.post_ID = intval(gVars.post_ID);
            gVars.form_action = "editpost";
            gVars.form_extra = "<input type=\'hidden\' id=\'post_ID\' name=\'post_ID\' value=\'" + strval(gVars.post_ID) + "\' />";
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-post_" + strval(gVars.post_ID), "_wpnonce", true, true);
        }

        form_pingback = "<input type=\"hidden\" name=\"post_pingback\" value=\"" + strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("default_pingback_flag")) +
            "\" id=\"post_pingback\" />";
        form_prevstatus = "<input type=\"hidden\" name=\"prev_status\" value=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_status"))) + "\" />";
        form_trackback = "<input type=\"text\" name=\"trackback_url\" style=\"width: 415px\" id=\"trackback\" tabindex=\"7\" value=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.str_replace("\n", " ", strval(StdClass.getValue(gVars.post, "to_ping")))) + "\" />";

        if (!equal("", StdClass.getValue(gVars.post, "pinged"))) {
            pings = "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Already pinged:", "default") + "</p><ul>";
            already_pinged = Strings.explode("\n", Strings.trim(strval(StdClass.getValue(gVars.post, "pinged"))));

            for (Map.Entry javaEntry11 : already_pinged.entrySet()) {
                pinged_url = strval(javaEntry11.getValue());
                pings = pings + "\n\t<li>" + getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(pinged_url, strval(0)) + "</li>";
            }

            pings = pings + "</ul>";
        }

        saveasdraft = "<input name=\"save\" type=\"submit\" id=\"save\" class=\"button\" tabindex=\"3\" value=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save and Continue Editing", "default")) + "\" />";

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block5");
        echo(gVars.webEnv, gVars.user_ID);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block6");
        echo(gVars.webEnv, gVars.form_action);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block7");
        echo(gVars.webEnv, gVars.form_action);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block8");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_author"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block9");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_type"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block10");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_status"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block11");

        if (!empty(gVars.webEnv._REQUEST.getValue("popupurl"))) {
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._REQUEST.getValue("popupurl"))), null, "display"));
        } else if (strictEqual(Strings.strpos(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(), "/wp-admin/"), BOOLEAN_FALSE) && booleanval(gVars.post_ID) &&
                strictEqual(getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()), intval(gVars.post_ID))) {
            echo(gVars.webEnv, "redo");
        } else {
            echo(
                gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.stripslashes(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()), null, "display"));
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block12");

        if (!equal("draft", StdClass.getValue(gVars.post, "post_status"))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_original_referer_field(true, "previous");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block13");
        echo(gVars.webEnv, gVars.form_extra);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block14");

        if (equal("publish", StdClass.getValue(gVars.post, "post_status"))) {
            echo(gVars.webEnv, "<a href=\"");
            echo(
                gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts)
                    .clean_url(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(gVars.post, "ID"), false), null, "display"));
            echo(gVars.webEnv, "\" target=\"_blank\" tabindex=\"4\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("View this Post", "default");
            echo(gVars.webEnv, "</a>\n");
        } else if (equal("edit", gVars.action)) {
            echo(gVars.webEnv, "<a href=\"");
            echo(gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("preview_post_link",
                            getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                                "preview",
                                "true",
                                getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(gVars.post, "ID"), false)))), null, "display"));
            echo(gVars.webEnv, "\" target=\"_blank\"  tabindex=\"4\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Preview this Post", "default");
            echo(gVars.webEnv, "</a>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Publish Status", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block16");

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {// Contributors only get "Unpublished" and "Pending Review" ?>
            echo(gVars.webEnv, "<option");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "publish");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "private");
            echo(gVars.webEnv, " value=\'publish\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Published", "default");
            echo(gVars.webEnv, "</option>\n");

            if (equal("future", StdClass.getValue(gVars.post, "post_status"))) {
                echo(gVars.webEnv, "<option");
                getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "future");
                echo(gVars.webEnv, " value=\'future\'>");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Scheduled", "default");
                echo(gVars.webEnv, "</option>\n");
            } else {
            }
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block17");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "pending");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Pending Review", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block19");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "draft");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Unpublished", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block21");

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
            echo(gVars.webEnv, "<p><label for=\"post_status_private\" class=\"selectit\"><input id=\"post_status_private\" name=\"post_status\" type=\"checkbox\" value=\"private\" ");
            getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.post, "post_status")), "private");
            echo(gVars.webEnv, " tabindex=\"4\" /> ");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Keep this post private", "default");
            echo(gVars.webEnv, "</label></p>\n");
        } else {
        }

        if (booleanval(gVars.post_ID)) {
            if (equal("future", StdClass.getValue(gVars.post, "post_status"))) { // scheduled for publishing at a future date
                gVars.stamp = getIncluded(L10nPage.class, gVars, gConsts).__("Scheduled for:<br />%1$s at %2$s", "default");
            } else if (equal("publish", StdClass.getValue(gVars.post, "post_status"))) { // already published
                gVars.stamp = getIncluded(L10nPage.class, gVars, gConsts).__("Published on:<br />%1$s at %2$s", "default");
            } else if (equal("0000-00-00 00:00:00", StdClass.getValue(gVars.post, "post_date"))) { // draft, 1 or more saves, no date specified
                gVars.stamp = getIncluded(L10nPage.class, gVars, gConsts).__("Publish immediately", "default");
            } else { // draft, 1 or more saves, date specified
                gVars.stamp = getIncluded(L10nPage.class, gVars, gConsts).__("Publish on:<br />%1$s at %2$s", "default");
            }

            gVars.date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                    strval(StdClass.getValue(gVars.post, "post_date")),
                    true);
            gVars.time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")),
                    strval(StdClass.getValue(gVars.post, "post_date")),
                    true);
        } else { // draft (no saves, and thus no date specified)
            gVars.stamp = getIncluded(L10nPage.class, gVars, gConsts).__("Publish immediately", "default");
            gVars.date = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0)),
                    true);
            gVars.time = getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")),
                    strval(getIncluded(FunctionsPage.class, gVars, gConsts).current_time("mysql", 0)),
                    true);
        }

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) { // Contributors don't get to choose the date of publish
            echo(gVars.webEnv, "<p class=\"curtime\">");
            QStrings.printf(gVars.webEnv, gVars.stamp, gVars.date, gVars.time);
            echo(gVars.webEnv, "&nbsp;<a href=\"#edit_timestamp\" class=\"edit-timestamp hide-if-no-js\" tabindex=\'4\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Edit", "default");
            echo(gVars.webEnv, "</a></p>\n\n<div id=\'timestampdiv\' class=\'hide-if-js\'>");
            getIncluded(TemplatePage.class, gVars, gConsts).touch_time(equal(gVars.action, "edit"), 1, 4);
            echo(gVars.webEnv, "</div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block22");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block23");

        if (!Array.in_array(StdClass.getValue(gVars.post, "post_status"), new Array<Object>(new ArrayEntry<Object>("publish"), new ArrayEntry<Object>("future"))) || equal(0, gVars.post_ID)) {
            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_posts")) {
                echo(gVars.webEnv, "\t<input name=\"publish\" type=\"submit\" class=\"button\" id=\"publish\" tabindex=\"5\" accesskey=\"p\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Publish", "default");
                echo(gVars.webEnv, "\" />\n");
            } else {
                echo(gVars.webEnv, "\t<input name=\"publish\" type=\"submit\" class=\"button\" id=\"publish\" tabindex=\"5\" accesskey=\"p\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Submit for Review", "default");
                echo(gVars.webEnv, "\" />\n");
            }
        }

        if (equal("edit", gVars.action) && getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_post", gVars.post_ID)) {
            echo(
                    gVars.webEnv,
                    "<a class=\'submitdelete\' href=\'" +
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("post.php?action=delete&amp;post=" + strval(gVars.post_ID), "delete-post_" + strval(gVars.post_ID)) +
                    "\' onclick=\"if ( confirm(\'" +
                    getIncluded(FormattingPage.class, gVars, gConsts).js_escape(
                        QStrings.sprintf(
                            equal("draft", StdClass.getValue(gVars.post, "post_status"))
                            ? getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete this draft \'%s\'\n  \'Cancel\' to stop, \'OK\' to delete.", "default")
                            : getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete this post \'%s\'\n  \'Cancel\' to stop, \'OK\' to delete.", "default"),
                            StdClass.getValue(gVars.post, "post_title"))) + "\') ) { return 1;}return 0;\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Delete&nbsp;post", "default") + "</a>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block24");

        if (booleanval(gVars.post_ID)) {
            if (booleanval(gVars.last_id = intval(getIncluded(PostPage.class, gVars, gConsts).get_post_meta(intval(gVars.post_ID), "_edit_last", true)))) {
                gVars.last_user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(gVars.last_id);
                QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__("Last edited by %1$s on %2$s at %3$s", "default"),
                    getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(strval(StdClass.getValue(gVars.last_user, "display_name")), strval(0)),
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                        strval(StdClass.getValue(gVars.post, "post_modified")), true),
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")),
                        strval(StdClass.getValue(gVars.post, "post_modified")), true));
            } else {
                QStrings.printf(
                    gVars.webEnv,
                    getIncluded(L10nPage.class, gVars, gConsts).__("Last edited on %1$s at %2$s", "default"),
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("date_format")),
                        strval(StdClass.getValue(gVars.post, "post_modified")), true),
                    getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")),
                        strval(StdClass.getValue(gVars.post, "post_modified")), true));
            }

            echo(gVars.webEnv, "<br class=\"clear\" />\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block25");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Related", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block26");

        if (booleanval(gVars.post_ID)) {
            echo(gVars.webEnv, "<li><a href=\"edit.php?p=");
            echo(gVars.webEnv, gVars.post_ID);
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("See Comments on this Post", "default");
            echo(gVars.webEnv, "</a></li>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block27");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Posts", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block29");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Tags", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block31");
        getIncluded(L10nPage.class, gVars, gConsts)._e("View Drafts", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block32");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("post_relatedlinks_list", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block33");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("submitpost_box", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block34");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block35");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_title"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block36");
        gVars.sample_permalink_html = (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).get_sample_permalink_html(StdClass.getValue(
                    gVars.post,
                    "ID"), null, null);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block37");

        if (!empty(StdClass.getValue(gVars.post, "ID")) && !empty(gVars.sample_permalink_html)) {
            echo(gVars.webEnv, gVars.sample_permalink_html);
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block38");
        echo(gVars.webEnv, booleanval(getIncluded(General_templatePage.class, gVars, gConsts).user_can_richedit())
            ? "postdivrich"
            : "postdiv");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block39");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Post", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block40");
        getIncluded(General_templatePage.class, gVars, gConsts).the_editor(strval(StdClass.getValue(gVars.post, "post_content")), "content", "title", true, 2);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("autosave", "autosavenonce", false, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("closedpostboxes", "closedpostboxesnonce", false, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("getpermalink", "getpermalinknonce", false, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("samplepermalink", "samplepermalinknonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block41");
        echo(gVars.webEnv, form_pingback);
        echo(gVars.webEnv, form_prevstatus);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block42");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("tagsdiv", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block43");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Tags", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block44");
        echo(gVars.webEnv, getIncluded(TaxonomyPage.class, gVars, gConsts).get_tags_to_edit(intval(gVars.post_ID)));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block45");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("categorydiv", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block46");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block47");
        getIncluded(L10nPage.class, gVars, gConsts)._e("+ Add New Category", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block48");
        getIncluded(L10nPage.class, gVars, gConsts)._e("New category name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block49");
        getIncluded(Category_templatePage.class, gVars, gConsts).wp_dropdown_categories(
            new Array<Object>(new ArrayEntry<Object>("hide_empty", 0), new ArrayEntry<Object>("name", "newcat_parent"), new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("hierarchical", 1), new ArrayEntry<Object>("show_option_none", getIncluded(L10nPage.class, gVars, gConsts).__("Parent category", "default")),
                new ArrayEntry<Object>("tab_index", 3)));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block50");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Add", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block51");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-category", "_ajax_nonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block52");
        getIncluded(L10nPage.class, gVars, gConsts)._e("All Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block53");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Most Used", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block54");
        popular_ids = getIncluded(TemplatePage.class, gVars, gConsts).wp_popular_terms_checklist("category", 0, 10);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block55");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_category_checklist(intval(gVars.post_ID), 0, false);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block56");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("post", "normal", gVars.post);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block57");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_form_advanced", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block58");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced Options", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block59");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("postexcerpt", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block60");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Excerpt", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block61");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_excerpt"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block62");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Excerpts are optional hand-crafted summaries of your content. You can <a href=\"http://codex.wordpress.org/Template_Tags/the_excerpt\" target=\"_blank\">use them in your template</a>",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block63");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("trackbacksdiv", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block64");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Trackbacks", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block65");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Send trackbacks to:", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block66");
        echo(gVars.webEnv, form_trackback);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block67");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Separate multiple URLs with spaces", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block68");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Trackbacks are a way to notify legacy blog systems that you&#8217;ve linked to them. If you link other WordPress/nWordPress blogs they&#8217;ll be notified automatically using <a href=\"http://codex.wordpress.org/Introduction_to_Blogging#Managing_Comments\" target=\"_blank\">pingbacks</a>, no other action necessary.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block69");

        if (!empty(pings)) {
            echo(gVars.webEnv, pings);
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block70");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("postcustom", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block71");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Custom Fields", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block72");
        gVars.metadata = (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).has_meta(intval(gVars.post_ID));
        getIncluded(TemplatePage.class, gVars, gConsts).list_meta(gVars.metadata);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block73");
        getIncluded(TemplatePage.class, gVars, gConsts).meta_form();

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block74");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Custom fields can be used to add extra metadata to a post that you can <a href=\"http://codex.wordpress.org/Using_Custom_Fields\" target=\"_blank\">use in your theme</a>.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block75");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("dbx_post_advanced", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block76");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("commentstatusdiv", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block77");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comments &amp; Pings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block78");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.post, "comment_status")), "open");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block79");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Allow Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block80");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.post, "ping_status")), "open");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block81");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Allow Pings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block82");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "These settings apply to this post only. &#8220;Pings&#8221; are <a href=\"http://codex.wordpress.org/Introduction_to_Blogging#Managing_Comments\" target=\"_blank\">trackbacks and pingbacks</a>.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block83");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("passworddiv", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block84");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Password Protect This Post", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block85");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_password"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block86");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Setting a password will require people who visit your blog to enter the above password to view this post and its comments.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block87");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("slugdiv", "post"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block88");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Post Slug", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block89");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_name"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block90");
        gVars.authors = getIncluded(UserPage.class, gVars, gConsts).get_editable_user_ids(gVars.current_user.getID(), true); // TODO: ROLE SYSTEM

        if (booleanval(StdClass.getValue(gVars.post, "post_author")) && !Array.in_array(StdClass.getValue(gVars.post, "post_author"), gVars.authors)) {
            gVars.authors.putValue(StdClass.getValue(gVars.post, "post_author"));
        }

        if (booleanval(gVars.authors) && (Array.count(gVars.authors) > 1)) {
            echo(gVars.webEnv, "<div id=\"authordiv\" class=\"postbox ");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("authordiv", "post"));
            echo(gVars.webEnv, "\">\n<h3>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Post Author", "default");
            echo(gVars.webEnv, "</h3>\n<div class=\"inside\">\n");
            (((org.numiton.nwp.wp_includes.UserPage) getIncluded(org.numiton.nwp.wp_includes.UserPage.class, gVars, gConsts))).wp_dropdown_users(
                new Array<Object>(new ArrayEntry<Object>("include", gVars.authors), new ArrayEntry<Object>("name", "post_author_override"),
                    new ArrayEntry<Object>("selected", empty(gVars.post_ID)
                        ? gVars.user_ID
                        : StdClass.getValue(gVars.post, "post_author"))));
            echo(gVars.webEnv, "</div>\n</div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block91");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("post", "advanced", gVars.post);

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block92");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("dbx_post_sidebar", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_form_advanced_block93");

        if ((isset(StdClass.getValue(gVars.post, "post_title")) && equal("", StdClass.getValue(gVars.post, "post_title"))) ||
                (isset(gVars.webEnv._GET.getValue("message")) && (2 > intval(gVars.webEnv._GET.getValue("message"))))) {
            echo(gVars.webEnv, "<script type=\"text/javascript\">\ntry{document.post.title.focus();}catch(e){}\n</script>\n");
        } else {
        }

        return DEFAULT_VAL;
    }
}
