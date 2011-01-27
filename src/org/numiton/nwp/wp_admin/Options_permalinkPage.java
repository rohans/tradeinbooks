/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Options_permalinkPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_admin.includes.MiscPage;
import org.numiton.nwp.wp_admin.includes.TemplatePage;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;


@Controller
@Scope("request")
public class Options_permalinkPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Options_permalinkPage.class.getName());
    public String home_path;
    public String permalink_structure;
    public String category_base;
    public String tag_base;
    public boolean writable;
    public boolean usingpi;
    public Array<Object> structures = new Array<Object>();

    @Override
    @RequestMapping("/wp-admin/options-permalink.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/options_permalink";
    }

    public void add_js() {
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\n//<![CDATA[\nfunction GetElementsWithClassName(elementName, className) {\nvar allElements = document.getElementsByTagName(elementName);\nvar elemColl = new Array();\nfor (i = 0; i < allElements.length; i++) {\nif (allElements[i].className == className) {\nelemColl[elemColl.length] = allElements[i];\n}\n}\nreturn elemColl;\n}\n\nfunction upit() {\nvar inputColl = GetElementsWithClassName(\'input\', \'tog\');\nvar structure = document.getElementById(\'permalink_structure\');\nvar inputs = \'\';\nfor (i = 0; i < inputColl.length; i++) {\nif ( inputColl[i].checked && inputColl[i].value != \'\') {\ninputs += inputColl[i].value + \' \';\n}\n}\ninputs = inputs.substr(0,inputs.length - 1);\nif ( \'custom\' != inputs )\nstructure.value = inputs;\n}\n\nfunction blurry() {\nif (!document.getElementById) return;\n\nvar structure = document.getElementById(\'permalink_structure\');\nstructure.onfocus = function () { document.getElementById(\'custom_selection\').checked = \'checked\'; }\n\nvar aInputs = document.getElementsByTagName(\'input\');\n\nfor (var i = 0; i < aInputs.length; i++) {\naInputs[i].onclick = aInputs[i].onkeyup = upit;\n}\n}\n\nwindow.onload = blurry;\n//]]>\n</script>\n");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, AdminPage.class);
        gVars.title = getIncluded(L10nPage.class, gVars, gConsts).__("Permalink Settings", "default");
        gVars.parent_file = "options-general.php";
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("admin_head", Callback.createCallbackArray(this, "add_js"), 10, 1);
        include(gVars, gConsts, Admin_headerPage.class);
        home_path = getIncluded(FilePage.class, gVars, gConsts).get_home_path();

        if (isset(gVars.webEnv._POST.getValue("permalink_structure")) || isset(gVars.webEnv._POST.getValue("category_base"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("update-permalink", "_wpnonce");

            if (isset(gVars.webEnv._POST.getValue("permalink_structure"))) {
                permalink_structure = strval(gVars.webEnv._POST.getValue("permalink_structure"));

                if (!empty(permalink_structure)) {
                    permalink_structure = QRegExPerl.preg_replace("#/+#", "/", "/" + strval(gVars.webEnv._POST.getValue("permalink_structure")));
                }

                gVars.wp_rewrite.set_permalink_structure(permalink_structure);
            }

            if (isset(gVars.webEnv._POST.getValue("category_base"))) {
                category_base = strval(gVars.webEnv._POST.getValue("category_base"));

                if (!empty(category_base)) {
                    category_base = QRegExPerl.preg_replace("#/+#", "/", "/" + strval(gVars.webEnv._POST.getValue("category_base")));
                }

                gVars.wp_rewrite.set_category_base(category_base);
            }

            if (isset(gVars.webEnv._POST.getValue("tag_base"))) {
                tag_base = strval(gVars.webEnv._POST.getValue("tag_base"));

                if (!empty(tag_base)) {
                    tag_base = QRegExPerl.preg_replace("#/+#", "/", "/" + strval(gVars.webEnv._POST.getValue("tag_base")));
                }

                gVars.wp_rewrite.set_tag_base(tag_base);
            }
        }

        permalink_structure = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("permalink_structure"));
        category_base = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("category_base"));
        tag_base = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tag_base"));

        if ((!FileSystemOrSocket.file_exists(gVars.webEnv, home_path + ".htaccess") && FileSystemOrSocket.is_writable(gVars.webEnv, home_path)) ||
                FileSystemOrSocket.is_writable(gVars.webEnv, home_path + ".htaccess")) {
            writable = true;
        } else {
            writable = false;
        }

        if (gVars.wp_rewrite.using_index_permalinks()) {
            usingpi = true;
        } else {
            usingpi = false;
        }

        gVars.wp_rewrite.flush_rules();

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block2");

        if (isset(gVars.webEnv._POST.getValue("submit"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p>");

            if (booleanval(permalink_structure) && !usingpi && !writable) {
                getIncluded(L10nPage.class, gVars, gConsts)._e("You should update your .htaccess now.", "default");
            } else {
                getIncluded(L10nPage.class, gVars, gConsts)._e("Permalink structure updated.", "default");
            }

            echo(gVars.webEnv, "</p></div>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block3");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Customize Permalink Structure", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block4");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-permalink", "_wpnonce", true, true);

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "By default nWordPress uses web <abbr title=\"Universal Resource Locator\">URL</abbr>s which have question marks and lots of numbers in them, however nWordPress offers you the ability to create a custom URL structure for your permalinks and archives. This can improve the aesthetics, usability, and forward-compatibility of your links. A <a href=\"http://codex.wordpress.org/Using_Permalinks\">number of tags are available</a>, and here are some examples to get you started.",
                "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block6");

        String prefix = "";

        if (!booleanval(getIncluded(MiscPage.class, gVars, gConsts).got_mod_rewrite())) {
            prefix = "/index.php";
        }

        structures = new Array<Object>(
                new ArrayEntry<Object>(""),
                new ArrayEntry<Object>(prefix + "/%year%/%monthnum%/%day%/%postname%/"),
                new ArrayEntry<Object>(prefix + "/%year%/%monthnum%/%postname%/"),
                new ArrayEntry<Object>(prefix + "/archives/%post_id%"));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block7");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Common settings", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block8");
        getIncluded(TemplatePage.class, gVars, gConsts).checked("", permalink_structure);

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Default", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block10");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block11");
        echo(gVars.webEnv, structures.getValue(1));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block12");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(structures.getValue(1)), permalink_structure);

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Day and name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block14");
        echo(gVars.webEnv,
            getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + prefix + "/" + DateTime.date("Y") + "/" + DateTime.date("m") + "/" + DateTime.date("d") + "/sample-post/");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block15");
        echo(gVars.webEnv, structures.getValue(2));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block16");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(structures.getValue(2)), permalink_structure);

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Month and name", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block18");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + prefix + "/" + DateTime.date("Y") + "/" + DateTime.date("m") + "/sample-post/");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block19");
        echo(gVars.webEnv, structures.getValue(3));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block20");
        getIncluded(TemplatePage.class, gVars, gConsts).checked(strval(structures.getValue(3)), permalink_structure);

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Numeric", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block22");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + prefix);

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block23");

        if (!Array.in_array(permalink_structure, structures)) {
            echo(gVars.webEnv, "\t\t\tchecked=\"checked\"\n\t\t\t");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block24");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Custom Structure", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block25");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(permalink_structure));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Optional", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block27");

        if (gVars.is_apache) {
            echo(gVars.webEnv, "\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e(
                    "If you like, you may enter custom structures for your category and tag <abbr title=\"Universal Resource Locator\">URL</abbr>s here. For example, using <code>/topics/</code> as your category base would make your category links like <code>http://example.org/topics/uncategorized/</code>. If you leave these blank the defaults will be used.",
                    "default");
            echo(gVars.webEnv, "</p>\n");
        } else {
            echo(gVars.webEnv, "\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e(
                    "If you like, you may enter custom structures for your category and tag <abbr title=\"Universal Resource Locator\">URL</abbr>s here. For example, using <code>/topics/</code> as your category base would make your category links like <code>http://example.org/index.php/topics/uncategorized/</code>. If you leave these blank the defaults will be used.",
                    "default");
            echo(gVars.webEnv, "</p>\n");
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Category base", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block29");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(category_base));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Tag base", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block31");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(tag_base));

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block32");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block33");

        if (booleanval(permalink_structure) && !usingpi && !writable) {
            echo(gVars.webEnv, "  <p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e(
                    "If your <code>.htaccess</code> file were <a href=\"http://codex.wordpress.org/Changing_File_Permissions\">writable</a>, we could do this automatically, but it isn&#8217;t so these are the mod_rewrite rules you should have in your <code>.htaccess</code> file. Click in the field and press <kbd>CTRL + a</kbd> to select all.",
                    "default");
            echo(gVars.webEnv, "</p>\n<form action=\"options-permalink.php\" method=\"post\">\n");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("update-permalink", "_wpnonce", true, true);
            echo(gVars.webEnv, "\t<p><textarea rows=\"5\" style=\"width: 98%;\" name=\"rules\" id=\"rules\">");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(gVars.wp_rewrite.mod_rewrite_rules(), strval(0)));
            echo(gVars.webEnv, "</textarea></p>\n</form>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_admin_options_permalink_block34");
        require(gVars, gConsts, Admin_footerPage.class);

        return DEFAULT_VAL;
    }
}
