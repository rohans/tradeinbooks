/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Edit_link_formPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Edit_link_formPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Edit_link_formPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/edit-link-form.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/edit_link_form";
    }

    public void xfn_check(String _class, String value, String deprecated) {
        String link_rel = null;
        Array<Object> rels = new Array<Object>();
        link_rel = strval(StdClass.getValue(gVars.link, "link_rel"));
        rels = QRegExPerl.preg_split("/\\s+/", link_rel);

        if (!equal("", value) && Array.in_array(value, rels)) {
            echo(gVars.webEnv, " checked=\"checked\"");
        }

        if (equal("", value)) {
            if (equal("family", _class) && strictEqual(Strings.strpos(link_rel, "child"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(link_rel, "parent"), BOOLEAN_FALSE) &&
                    strictEqual(Strings.strpos(link_rel, "sibling"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(link_rel, "spouse"), BOOLEAN_FALSE) &&
                    strictEqual(Strings.strpos(link_rel, "kin"), BOOLEAN_FALSE)) {
                echo(gVars.webEnv, " checked=\"checked\"");
            }

            if (equal("friendship", _class) && strictEqual(Strings.strpos(link_rel, "friend"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(link_rel, "acquaintance"), BOOLEAN_FALSE) &&
                    strictEqual(Strings.strpos(link_rel, "contact"), false)) {
                echo(gVars.webEnv, " checked=\"checked\"");
            }

            if (equal("geographical", _class) && strictEqual(Strings.strpos(link_rel, "co-resident"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(link_rel, "neighbor"), BOOLEAN_FALSE)) {
                echo(gVars.webEnv, " checked=\"checked\"");
            }

            if (equal("identity", _class) && Array.in_array("me", rels)) {
                echo(gVars.webEnv, " checked=\"checked\"");
            }
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block1");
        gVars.webEnv = webEnv;

        if (!empty(gVars.link_id)) {
            gVars.heading = getIncluded(L10nPage.class, gVars, gConsts).__("Edit Link", "default");
            gVars.submit_text = getIncluded(L10nPage.class, gVars, gConsts).__("Save Changes", "default");
            gVars.form = "<form name=\"editlink\" id=\"editlink\" method=\"post\" action=\"link.php\">";
            gVars.nonce_action = "update-bookmark_" + strval(gVars.link_id);
        } else {
            gVars.heading = getIncluded(L10nPage.class, gVars, gConsts).__("Add Link", "default");
            gVars.submit_text = getIncluded(L10nPage.class, gVars, gConsts).__("Add Link", "default");
            gVars.form = "<form name=\"addlink\" id=\"addlink\" method=\"post\" action=\"link.php\">";
            gVars.nonce_action = "add-bookmark";
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block2");
        echo(gVars.webEnv, gVars.form);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field(gVars.nonce_action, "_wpnonce", true, true);
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("closedpostboxes", "closedpostboxesnonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block3");
        echo(gVars.webEnv, gVars.heading);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block4");

        if (!empty(gVars.link_id)) {
            echo(gVars.webEnv, "<a href=\"");
            echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_url"));
            echo(gVars.webEnv, "\" target=\"_blank\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Visit Link", "default");
            echo(gVars.webEnv, "</a>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block5");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(StdClass.getValue(gVars.link, "link_visible")), "N");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block6");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Keep this link private", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block8");

        if (equal("edit", gVars.action) && getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("manage_links")) {
            echo(gVars.webEnv,
                "<a class=\'submitdelete\' href=\'" +
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("link.php?action=delete&amp;link_id=" + strval(gVars.link_id), "delete-bookmark_" + strval(gVars.link_id)) +
                "\' onclick=\"if ( confirm(\'" +
                getIncluded(FormattingPage.class, gVars, gConsts).js_escape(
                        QStrings.sprintf(
                            getIncluded(L10nPage.class, gVars, gConsts).__("You are about to delete this link \'%s\'\n\'Cancel\' to stop, \'OK\' to delete.", "default"),
                            StdClass.getValue(gVars.link, "link_name"))) + "\') ) { return 1;}return 0;\">" + getIncluded(L10nPage.class, gVars, gConsts).__("Delete&nbsp;link", "default") + "</a>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Related", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Links", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Manage All Link Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Import Links", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block13");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("link_relatedlinks_list", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block14");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("submitlink_box", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block16");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_name"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Example: Nifty blogging software", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Web Address", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block19");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_url"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Example: <code>http://wordpress.org/</code> &#8212; don&#8217;t forget the <code>http://</code>", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Description", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block22");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_description"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block23");
        getIncluded(L10nPage.class, gVars, gConsts)._e("This will be shown when someone hovers over the link in the blogroll, or optionally below the link.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block24");
        echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).postbox_classes("linkcategorydiv", "link"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block25");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e("+ Add New Category", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block27");
        getIncluded(L10nPage.class, gVars, gConsts)._e("New category name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Add", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block29");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("add-link-category", "_ajax_nonce", false, true);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("All Categories", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block31");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Most Used", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block32");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_link_category_checklist(gVars.link_id);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block33");
        getIncluded(TemplatePage.class, gVars, gConsts).wp_popular_terms_checklist("link_category", 0, 10);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block34");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("link", "normal", gVars.link);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block35");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced Options", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block36");
        echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).postbox_classes("linktargetdiv", "link"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block37");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Target", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block38");
        echo(gVars.webEnv, equal(StdClass.getValue(gVars.link, "link_target"), "_blank")
            ? "checked=\"checked\""
            : "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block39");
        echo(gVars.webEnv, equal(StdClass.getValue(gVars.link, "link_target"), "_top")
            ? "checked=\"checked\""
            : "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block40");
        echo(gVars.webEnv, equal(StdClass.getValue(gVars.link, "link_target"), "")
            ? "checked=\"checked\""
            : "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block41");
        getIncluded(L10nPage.class, gVars, gConsts)._e("none", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block42");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Choose the frame your link targets. Essentially this means if you choose <code>_blank</code> your link will open in a new window.", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block43");
        echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).postbox_classes("linkxfndiv", "link"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block44");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Link Relationship (XFN)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block45");
        getIncluded(L10nPage.class, gVars, gConsts)._e("rel:", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block46");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_rel"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block47");
        getIncluded(L10nPage.class, gVars, gConsts)._e("identity", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block48");
        xfn_check("identity", "me", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block49");
        getIncluded(L10nPage.class, gVars, gConsts)._e("another web address of mine", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block50");
        getIncluded(L10nPage.class, gVars, gConsts)._e("friendship", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block51");
        xfn_check("friendship", "contact", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block52");
        getIncluded(L10nPage.class, gVars, gConsts)._e("contact", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block53");
        xfn_check("friendship", "acquaintance", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block54");
        getIncluded(L10nPage.class, gVars, gConsts)._e("acquaintance", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block55");
        xfn_check("friendship", "friend", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block56");
        getIncluded(L10nPage.class, gVars, gConsts)._e("friend", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block57");
        xfn_check("friendship", "", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block58");
        getIncluded(L10nPage.class, gVars, gConsts)._e("none", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block59");
        getIncluded(L10nPage.class, gVars, gConsts)._e("physical", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block60");
        xfn_check("physical", "met", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block61");
        getIncluded(L10nPage.class, gVars, gConsts)._e("met", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block62");
        getIncluded(L10nPage.class, gVars, gConsts)._e("professional", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block63");
        xfn_check("professional", "co-worker", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block64");
        getIncluded(L10nPage.class, gVars, gConsts)._e("co-worker", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block65");
        xfn_check("professional", "colleague", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block66");
        getIncluded(L10nPage.class, gVars, gConsts)._e("colleague", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block67");
        getIncluded(L10nPage.class, gVars, gConsts)._e("geographical", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block68");
        xfn_check("geographical", "co-resident", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block69");
        getIncluded(L10nPage.class, gVars, gConsts)._e("co-resident", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block70");
        xfn_check("geographical", "neighbor", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block71");
        getIncluded(L10nPage.class, gVars, gConsts)._e("neighbor", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block72");
        xfn_check("geographical", "", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block73");
        getIncluded(L10nPage.class, gVars, gConsts)._e("none", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block74");
        getIncluded(L10nPage.class, gVars, gConsts)._e("family", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block75");
        xfn_check("family", "child", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block76");
        getIncluded(L10nPage.class, gVars, gConsts)._e("child", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block77");
        xfn_check("family", "kin", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block78");
        getIncluded(L10nPage.class, gVars, gConsts)._e("kin", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block79");
        xfn_check("family", "parent", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block80");
        getIncluded(L10nPage.class, gVars, gConsts)._e("parent", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block81");
        xfn_check("family", "sibling", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block82");
        getIncluded(L10nPage.class, gVars, gConsts)._e("sibling", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block83");
        xfn_check("family", "spouse", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block84");
        getIncluded(L10nPage.class, gVars, gConsts)._e("spouse", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block85");
        xfn_check("family", "", "radio");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block86");
        getIncluded(L10nPage.class, gVars, gConsts)._e("none", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block87");
        getIncluded(L10nPage.class, gVars, gConsts)._e("romantic", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block88");
        xfn_check("romantic", "muse", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block89");
        getIncluded(L10nPage.class, gVars, gConsts)._e("muse", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block90");
        xfn_check("romantic", "crush", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block91");
        getIncluded(L10nPage.class, gVars, gConsts)._e("crush", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block92");
        xfn_check("romantic", "date", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block93");
        getIncluded(L10nPage.class, gVars, gConsts)._e("date", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block94");
        xfn_check("romantic", "sweetheart", "");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block95");
        getIncluded(L10nPage.class, gVars, gConsts)._e("sweetheart", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block96");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "If the link is to a person, you can specify your relationship with them using the above form. If you would like to learn more about the idea check out <a href=\"http://gmpg.org/xfn/\">XFN</a>.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block97");
        echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).postbox_classes("linkadvanceddiv", "link"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block98");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block99");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Image Address", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block100");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_image"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block101");
        getIncluded(L10nPage.class, gVars, gConsts)._e("RSS Address", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block102");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_rss"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block103");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Notes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block104");
        echo(gVars.webEnv, StdClass.getValue(gVars.link, "link_notes"));

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block105");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Rating", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block106");

        for (int r = 0; r < 10; r++) {
            echo(gVars.webEnv, "            <option value=\"" + strval(r) + "\" ");

            if (equal(StdClass.getValue(gVars.link, "link_rating"), r)) {
                echo(gVars.webEnv, "selected=\"selected\"");
            }

            echo(gVars.webEnv, ">" + strval(r) + "</option>");
        }

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block107");
        getIncluded(L10nPage.class, gVars, gConsts)._e("(Leave at 0 for no rating.)", "default");

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block108");
        getIncluded(TemplatePage.class, gVars, gConsts).do_meta_boxes("link", "advanced", gVars.link);

        /* Start of block */
        super.startBlock("__wp_admin_edit_link_form_block109");

        if (booleanval(gVars.link_id)) {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"action\" value=\"save\" />\n<input type=\"hidden\" name=\"link_id\" value=\"");
            echo(gVars.webEnv, gVars.link_id);
            echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"order_by\" value=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.order_by));
            echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"cat_id\" value=\"");
            echo(gVars.webEnv, intval(gVars.cat_id));
            echo(gVars.webEnv, "\" />\n");
        } else {
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"action\" value=\"add\" />\n");
        }

        return DEFAULT_VAL;
    }
}
