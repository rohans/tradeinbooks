/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_page_formPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_admin.includes.ThemePage;
import org.numiton.nwp.wp_admin.includes.UserPage;
import org.numiton.nwp.wp_includes.*;
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
public class Edit_page_formPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_page_formPage.class.getName());
    public Object sendto;

    @Override
    @RequestMapping("/wp-admin/edit-page-form.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_page_form";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block1");
        gVars.webEnv = webEnv;

        if (isset(gVars.webEnv._GET.getValue("message"))) {
            gVars.webEnv._GET.putValue("message", getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("message")));
        }

        gVars.messages.putValue(1,
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("Page updated. Continue editing below or <a href=\"%s\">go back</a>.", "default"),
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.stripslashes(gVars.webEnv, strval(gVars.webEnv._GET.getValue("_wp_original_http_referer"))))));
        gVars.messages.putValue(2, getIncluded(L10nPage.class, gVars, gConsts).__("Custom field updated.", "default"));
        gVars.messages.putValue(3, getIncluded(L10nPage.class, gVars, gConsts).__("Custom field deleted.", "default"));
        gVars.messages.putValue(4, getIncluded(L10nPage.class, gVars, gConsts).__("Page updated.", "default"));

        if (isset(gVars.webEnv._GET.getValue("message"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p>");
            echo(gVars.webEnv, gVars.messages.getValue(gVars.webEnv._GET.getValue("message")));
            echo(gVars.webEnv, "</p></div>\n");
        } else {
        }

        if (!isset(gVars.post_ID) || equal(0, gVars.post_ID)) {
            gVars.form_action = "post";
            gVars.nonce_action = "add-page";
            gVars.temp_ID = -1 * DateTime.time(); // don't change this formula without looking at wp_write_post()
            gVars.form_extra = "<input type=\'hidden\' id=\'post_ID\' name=\'temp_ID\' value=\'" + strval(gVars.temp_ID) + "\' />";
        } else {
            gVars.post_ID = intval(gVars.post_ID);
            gVars.form_action = "editpost";
            gVars.nonce_action = "update-page_" + strval(gVars.post_ID);
            gVars.form_extra = "<input type=\'hidden\' id=\'post_ID\' name=\'post_ID\' value=\'" + strval(gVars.post_ID) + "\' />";
        }

        gVars.temp_ID = gVars.temp_ID;
        gVars.user_ID = gVars.user_ID;
        sendto = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.stripslashes(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()), null, "display");

        if (!equal(0, gVars.post_ID) && equal(sendto, getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.post_ID, false))) {
            sendto = "redo";
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block2");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Write Page", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block3");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field(gVars.nonce_action, "_wpnonce", true, true);

        if (isset(gVars.mode) && equal("bookmarklet", gVars.mode)) {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"mode\" value=\"bookmarklet\" />");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block4");
        echo(gVars.webEnv, gVars.user_ID);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block5");
        echo(gVars.webEnv, gVars.form_action);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block6");
        echo(gVars.webEnv, gVars.form_action);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block7");
        echo(gVars.webEnv, gVars.form_extra);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block8");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_type"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block9");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_status"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block10");

        if (strictEqual(Strings.strpos(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer(), "/wp-admin/"), BOOLEAN_FALSE) && booleanval(gVars.post_ID) &&
                strictEqual(getIncluded(RewritePage.class, gVars, gConsts).url_to_postid(getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()), intval(gVars.post_ID))) {
            echo(gVars.webEnv, "redo");
        } else {
            echo(
                gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.stripslashes(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).wp_get_referer()), null, "display"));
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block11");

        if (!equal("draft", StdClass.getValue(gVars.post, "post_status"))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_original_referer_field(true, "previous");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block12");

        if (equal("publish", StdClass.getValue(gVars.post, "post_status"))) {
            echo(gVars.webEnv, "<a href=\"");
            echo(
                gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts)
                    .clean_url(getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(gVars.post, "ID"), false), null, "display"));
            echo(gVars.webEnv, "\" target=\"_blank\"  tabindex=\"4\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("View this Page", "default");
            echo(gVars.webEnv, "</a>\n");
        } else if (equal("edit", gVars.action)) {
            echo(gVars.webEnv, "<a href=\"");
            echo(gVars.webEnv,
                getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("preview_post_link",
                            getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                                "preview",
                                "true",
                                getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(gVars.post, "ID"), false)))), null, "display"));
            echo(gVars.webEnv, "\" target=\"_blank\" tabindex=\"4\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Preview this Page", "default");
            echo(gVars.webEnv, "</a>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Publish Status", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block14");

        if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_pages")) {
            echo(gVars.webEnv, "<option");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "publish");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "private");
            echo(gVars.webEnv, " value=\'publish\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Published", "default");
            echo(gVars.webEnv, "</option>\n");
        } else {
            echo(gVars.webEnv, "<option");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "private");
            echo(gVars.webEnv, " value=\'private\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Published", "default");
            echo(gVars.webEnv, "</option>\n");
        }

        if (equal("future", StdClass.getValue(gVars.post, "post_status"))) {
            echo(gVars.webEnv, "<option");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "future");
            echo(gVars.webEnv, " value=\'future\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Pending", "default");
            echo(gVars.webEnv, "</option>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block15");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "pending");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Pending Review", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block17");
        getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(StdClass.getValue(gVars.post, "post_status")), "draft");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Unpublished", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block19");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.post, "post_status")), "private");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Keep this page private", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block21");

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

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block22");
        QStrings.printf(gVars.webEnv, gVars.stamp, gVars.date, gVars.time);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block23");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Edit", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block24");
        getIncluded(TemplatePage.class, gVars, gConsts).touch_time(equal(gVars.action, "edit"), 1, 4);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block25");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block26");

        if (!Array.in_array(StdClass.getValue(gVars.post, "post_status"), new Array<Object>(new ArrayEntry<Object>("publish"), new ArrayEntry<Object>("future"))) || equal(0, gVars.post_ID)) {
            if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("publish_pages")) {
                echo(gVars.webEnv, "\t<input name=\"publish\" type=\"submit\" class=\"button\" id=\"publish\" tabindex=\"5\" accesskey=\"p\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Publish", "default");
                echo(gVars.webEnv, "\" />\n");
            } else {
                echo(gVars.webEnv, "\t<input name=\"publish\" type=\"submit\" class=\"button\" id=\"publish\" tabindex=\"5\" accesskey=\"p\" value=\"");
                getIncluded(L10nPage.class, gVars, gConsts)._e("Submit for Review", "default");
                echo(gVars.webEnv, "\" />\n");
            }
        }

        if (equal("edit", gVars.action) && getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("delete_page", gVars.post_ID)) {
            echo(
                    gVars.webEnv,
                    "<a class=\'submitdelete\' href=\'" +
                    getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("page.php?action=delete&amp;post=" + strval(gVars.post_ID), "delete-page_" + strval(gVars.post_ID)) +
                    "\' onclick=\"if ( confirm(\'" +
                    getIncluded(FormattingPage.class, gVars, gConsts).js_escape(
                        QStrings.sprintf(
                            equal("draft", StdClass.getValue(gVars.post, "post_status"))
                            ? getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete this draft \'%s\'\n  \'Cancel\' to stop, \'OK\' to delete.", "default")
                            : getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete this page \'%s\'\n  \'Cancel\' to stop, \'OK\' to delete.", "default"),
                            StdClass.getValue(gVars.post, "post_title"))) + "\') ) { return 1;}return 0;\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Delete&nbsp;page", "default") + "</a>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block27");

        if (booleanval(gVars.post_ID)) {
            if (booleanval(
                            gVars.last_id = intval((((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).get_post_meta(
                                        intval(gVars.post_ID),
                                        "_edit_last",
                                        true)))) {
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
        super.startBlock("__wp_admin_edit_page_form_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Related", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block29");

        if (booleanval(gVars.post_ID)) {
            echo(gVars.webEnv, "<li><a href=\"edit-pages.php?page_id=");
            echo(gVars.webEnv, gVars.post_ID);
            echo(gVars.webEnv, "\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("See Comments on this Page", "default");
            echo(gVars.webEnv, "</a></li>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block31");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Pages", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block32");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("page_relatedlinks_list", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block33");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("submitpage_box", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block34");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Title", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block35");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_title"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block36");
        gVars.sample_permalink_html = (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).get_sample_permalink_html(StdClass.getValue(
                    gVars.post,
                    "ID"), null, null);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block37");

        if (!empty(StdClass.getValue(gVars.post, "ID")) && !empty(gVars.sample_permalink_html)) {
            echo(gVars.webEnv, gVars.sample_permalink_html);
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block38");
        echo(gVars.webEnv, booleanval(getIncluded(General_templatePage.class, gVars, gConsts).user_can_richedit())
            ? "postdivrich"
            : "postdiv");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block39");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block40");
        getIncluded(General_templatePage.class, gVars, gConsts).the_editor(strval(StdClass.getValue(gVars.post, "post_content")), "content", "title", true, 2);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("autosave", "autosavenonce", false, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("closedpostboxes", "closedpostboxesnonce", false, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("getpermalink", "getpermalinknonce", false, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("samplepermalink", "samplepermalinknonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block41");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("page", "normal", gVars.post);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block42");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_page_form", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block43");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced Options", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block44");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pagepostcustom", "page"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block45");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Custom Fields", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block46");
        gVars.metadata = (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).has_meta(intval(gVars.post_ID));
        getIncluded(TemplatePage.class, gVars, gConsts).list_meta(gVars.metadata);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block47");
        getIncluded(TemplatePage.class, gVars, gConsts).meta_form();

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block48");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Custom fields can be used to add extra metadata to a post that you can <a href=\"http://codex.wordpress.org/Using_Custom_Fields\" target=\"_blank\">use in your theme</a>.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block49");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pagecommentstatusdiv", "page"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block50");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Comments &amp; Pings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block51");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.post, "comment_status")), "open");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block52");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Allow Comments", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block53");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.post, "ping_status")), "open");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block54");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Allow Pings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block55");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "These settings apply to this page only. &#8220;Pings&#8221; are <a href=\"http://codex.wordpress.org/Introduction_to_Blogging#Managing_Comments\" target=\"_blank\">trackbacks and pingbacks</a>.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block56");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pagepassworddiv", "page"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block57");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Password Protect This Page", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block58");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_password"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block59");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Setting a password will require people who visit your blog to enter the above password to view this page and its comments.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block60");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pageslugdiv", "page"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block61");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page Slug", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block62");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(gVars.post, "post_name"))));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block63");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pageparentdiv", "page"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block64");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page Parent", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block65");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Main Page (no parent)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block66");
        getIncluded(TemplatePage.class, gVars, gConsts).parent_dropdown(StdClass.getValue(gVars.post, "post_parent"), 0, 0);

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block67");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "You can arrange your pages in hierarchies, for example you could have an &#8220;About&#8221; page that has &#8220;Life Story&#8221; and &#8220;My Dog&#8221; pages under it. There are no limits to how deeply nested you can make pages.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block68");

        if (!equal(0, Array.count(getIncluded(ThemePage.class, gVars, gConsts).get_page_templates()))) {
            echo(gVars.webEnv, "<div id=\"pagetemplatediv\" class=\"postbox ");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pagetemplatediv", "page"));
            echo(gVars.webEnv, "\">\n<h3>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Page Template", "default");
            echo(gVars.webEnv, "</h3>\n<div class=\"inside\">\n<select name=\"page_template\">\n<option value=\'default\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Default Template", "default");
            echo(gVars.webEnv, "</option>\n");
            getIncluded(TemplatePage.class, gVars, gConsts).page_template_dropdown(StdClass.getValue(gVars.post, "page_template"));
            echo(gVars.webEnv, "</select>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e(
                    "Some themes have custom templates you can use for certain pages that might have additional features or custom layouts. If so, you&#8217;ll see them above.",
                    "default");
            echo(gVars.webEnv, "</p>\n</div>\n</div>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block69");
        echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pageorderdiv", "page"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block70");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Page Order", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block71");
        echo(gVars.webEnv, StdClass.getValue(gVars.post, "menu_order"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block72");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Pages are usually ordered alphabetically, but you can put a number above to change the order pages appear in. (We know this is a little janky, it&#8217;ll be better in future releases.)",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_page_form_block73");
        gVars.authors = getIncluded(UserPage.class, gVars, gConsts).get_editable_user_ids(gVars.current_user.getID(), true); // TODO: ROLE SYSTEM

        if (booleanval(StdClass.getValue(gVars.post, "post_author")) && !Array.in_array(StdClass.getValue(gVars.post, "post_author"), gVars.authors)) {
            gVars.authors.putValue(StdClass.getValue(gVars.post, "post_author"));
        }

        if (booleanval(gVars.authors) && (Array.count(gVars.authors) > 1)) {
            echo(gVars.webEnv, "<div id=\"pageauthordiv\" class=\"postbox ");
            echo(gVars.webEnv, (((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).postbox_classes("pageauthordiv", "page"));
            echo(gVars.webEnv, "\">\n<h3>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Page Author", "default");
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
        super.startBlock("__wp_admin_edit_page_form_block74");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("page", "advanced", gVars.post);

        return DEFAULT_VAL;
    }
}
