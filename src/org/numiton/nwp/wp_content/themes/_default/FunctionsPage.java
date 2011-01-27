/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FunctionsPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_content.themes._default;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller(value = "wp_content/themes/_default/FunctionsPage")
@Scope("request")
public class FunctionsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FunctionsPage.class.getName());

    @Override
    @RequestMapping("/wp-content/themes/default/functions.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_content/themes/_default/functions";
    }

    public void kubrick_head() {
        String head = null;
        Object output = null;
        Object url = null;
        Object color = null;
        Object display = null;
        String foot = null;
        head = "<style type=\'text/css\'>\n<!--";
        output = "";

        if (booleanval(kubrick_header_image())) {
            url = kubrick_header_image_url();
            output = strval(output) + "#header { background: url(\'" + strval(url) + "\') no-repeat bottom center; }\n";
        }

        if (!equal(false, color = kubrick_header_color())) {
            output = strval(output) + "#headerimg h1 a, #headerimg h1 a:visited, #headerimg .description { color: " + strval(color) + "; }\n";
        }

        if (!equal(false, display = kubrick_header_display())) {
            output = strval(output) + "#headerimg { display: " + strval(display) + " }\n";
        }

        foot = "--></style>\n";

        if (!equal("", output)) {
            echo(gVars.webEnv, head + strval(output) + foot);
        }
    }

    public String kubrick_header_image() {
        return strval(
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "kubrick_header_image",
                getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).get_option("kubrick_header_image")));
    }

    public String kubrick_upper_color() {
        String url = null;
        Array<Object> q = new Array<Object>();

        if (!strictEqual(Strings.strpos(url = kubrick_header_image_url(), "header-img.php?"), BOOLEAN_FALSE)) {
            Strings.parse_str(Strings.substr(url, Strings.strpos(url, "?") + 1), q);

            return strval(q.getValue("upper"));
        } else {
            return "e8eef7";
        }
    }

    public String kubrick_lower_color() {
        String url = null;
        Array<Object> q = new Array<Object>();

        if (!strictEqual(Strings.strpos(url = kubrick_header_image_url(), "header-img.php?"), BOOLEAN_FALSE)) {
            Strings.parse_str(Strings.substr(url, Strings.strpos(url, "?") + 1), q);

            return strval(q.getValue("lower"));
        } else {
            return "023d88";
        }
    }

    public String kubrick_header_image_url() {
        Object image = null;
        String url = null;

        if (booleanval(image = kubrick_header_image())) {
            url = getIncluded(ThemePage.class, gVars, gConsts).get_template_directory_uri() + "/images/" + strval(image);
        } else {
            url = getIncluded(ThemePage.class, gVars, gConsts).get_template_directory_uri() + "/images/kubrickheader.jpg";
        }

        return url;
    }

    public String kubrick_header_color() {
        return strval(
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "kubrick_header_color",
                getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).get_option("kubrick_header_color")));
    }

    public String kubrick_header_color_string() {
        String color = null;
        color = kubrick_header_color();

        if (equal(false, color)) {
            return "white";
        }

        return color;
    }

    public String kubrick_header_display() {
        return strval(
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "kubrick_header_display",
                getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).get_option("kubrick_header_display")));
    }

    public String kubrick_header_display_string() {
        String display = null;
        display = kubrick_header_display();

        return booleanval(display)
        ? display
        : "inline";
    }

    public void kubrick_add_theme_page() {
        String fontcolor;
        Array<Object> uc = new Array<Object>();
        Array<Object> lc = new Array<Object>();
        String headerimage;

        if (isset(gVars.webEnv._GET.getValue("page")) &&
                equal(gVars.webEnv._GET.getValue("page"), /*Modified by Numiton*/
                    "functions.php" /*FileSystemOrSocket.basename(SourceCodeInfo.getCurrentFile(gVars.webEnv))*/)) {
            if (isset(gVars.webEnv._REQUEST.getValue("action")) && equal("save", gVars.webEnv._REQUEST.getValue("action"))) {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("kubrick-header", "_wpnonce");

                if (isset(gVars.webEnv._REQUEST.getValue("njform"))) {
                    if (isset(gVars.webEnv._REQUEST.getValue("defaults"))) {
                        getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_image");
                        getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_color");
                        getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_display");
                    } else {
                        if (equal("", gVars.webEnv._REQUEST.getValue("njfontcolor"))) {
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_color");
                        } else {
                            fontcolor = QRegExPerl.preg_replace("/^.*(#[0-9a-fA-F]{6})?.*$/", "$1", strval(gVars.webEnv._REQUEST.getValue("njfontcolor")));
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).update_option("kubrick_header_color", fontcolor);
                        }

                        if (QRegExPerl.preg_match("/[0-9A-F]{6}|[0-9A-F]{3}/i", strval(gVars.webEnv._REQUEST.getValue("njuppercolor")), uc) &&
                                QRegExPerl.preg_match("/[0-9A-F]{6}|[0-9A-F]{3}/i", strval(gVars.webEnv._REQUEST.getValue("njlowercolor")), lc)) {
                            String ucStr = (equal(Strings.strlen(strval(uc.getValue(0))), 3)
                                ? (Strings.getCharAt(strval(uc.getValue(0)), 0) + Strings.getCharAt(strval(uc.getValue(0)), 0) + Strings.getCharAt(strval(uc.getValue(0)), 1) +
                                Strings.getCharAt(strval(uc.getValue(0)), 1) + Strings.getCharAt(strval(uc.getValue(0)), 2) + Strings.getCharAt(strval(uc.getValue(0)), 2))
                                : strval(uc.getValue(0)));
                            String lcStr = (equal(Strings.strlen(strval(lc.getValue(0))), 3)
                                ? (Strings.getCharAt(strval(lc.getValue(0)), 0) + Strings.getCharAt(strval(lc.getValue(0)), 0) + Strings.getCharAt(strval(lc.getValue(0)), 1) +
                                Strings.getCharAt(strval(lc.getValue(0)), 1) + Strings.getCharAt(strval(lc.getValue(0)), 2) + Strings.getCharAt(strval(lc.getValue(0)), 2))
                                : strval(lc.getValue(0)));
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).update_option(
                                "kubrick_header_image",
                                "header-img.php?upper=" + ucStr + "&lower=" + lcStr);
                        }

                        if (isset(gVars.webEnv._REQUEST.getValue("toggledisplay"))) {
                            if (equal(false, getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).get_option("kubrick_header_display"))) {
                                getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).update_option("kubrick_header_display", "none");
                            } else {
                                getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_display");
                            }
                        }
                    }
                } else {
                    if (isset(gVars.webEnv._REQUEST.getValue("headerimage"))) {
                        getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("kubrick-header", "_wpnonce");

                        if (equal("", gVars.webEnv._REQUEST.getValue("headerimage"))) {
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_image");
                        } else {
                            headerimage = QRegExPerl.preg_replace("/^.*?(header-img.php\\?upper=[0-9a-fA-F]{6}&lower=[0-9a-fA-F]{6})?.*$/", "$1", strval(gVars.webEnv._REQUEST.getValue("headerimage")));
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).update_option("kubrick_header_image", headerimage);
                        }
                    }

                    if (isset(gVars.webEnv._REQUEST.getValue("fontcolor"))) {
                        getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("kubrick-header", "_wpnonce");

                        if (equal("", gVars.webEnv._REQUEST.getValue("fontcolor"))) {
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_color");
                        } else {
                            fontcolor = QRegExPerl.preg_replace("/^.*?(#[0-9a-fA-F]{6})?.*$/", "$1", strval(gVars.webEnv._REQUEST.getValue("fontcolor")));
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).update_option("kubrick_header_color", fontcolor);
                        }
                    }

                    if (isset(gVars.webEnv._REQUEST.getValue("fontdisplay"))) {
                        getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("kubrick-header", "_wpnonce");

                        if (equal("", gVars.webEnv._REQUEST.getValue("fontdisplay")) || equal("inline", gVars.webEnv._REQUEST.getValue("fontdisplay"))) {
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).delete_option("kubrick_header_display");
                        } else {
                            getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).update_option("kubrick_header_display", "none");
                        }
                    }
                }

                //print_r($_REQUEST);
                getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("themes.php?page=functions.php&saved=true", 302);
                System.exit();
            }

            getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "kubrick_theme_page_head"), 10, 1);
        }

        (((org.numiton.nwp.wp_admin.includes.PluginPage) getIncluded(org.numiton.nwp.wp_admin.includes.PluginPage.class, gVars, gConsts))).add_theme_page(getIncluded(L10nPage.class, gVars, gConsts).__(
                "Customize Header",
                "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Header Image and Color", "default"), "edit_themes", /*FileSystemOrSocket.basename(SourceCodeInfo.getCurrentFile(gVars.webEnv))*/ /*Modified by Numiton*/ "functions.php",
            Callback.createCallbackArray(this, "kubrick_theme_page"));
    }

    public void kubrick_theme_page_head() {
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\" src=\"../wp-includes/js/colorpicker.js\"></script>\n<script type=\'text/javascript\'>\n// <![CDATA[\n\tfunction pickColor(color) {\n\t\tColorPicker_targetInput.value = color;\n\t\tkUpdate(ColorPicker_targetInput.id);\n\t}\n\tfunction PopupWindow_populate(contents) {\n\t\tcontents += \'<br /><p style=\"text-align:center;margin-top:0px;\"><input type=\"button\" class=\"button-secondary\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Close Color Picker", "default")));
        echo(
                gVars.webEnv,
                "\" onclick=\"cp.hidePopup(\\\'prettyplease\\\')\"></input></p>\';\n\t\tthis.contents = contents;\n\t\tthis.populated = false;\n\t}\n\tfunction PopupWindow_hidePopup(magicword) {\n\t\tif ( magicword != \'prettyplease\' )\n\t\t\treturn false;\n\t\tif (this.divName != null) {\n\t\t\tif (this.use_gebi) {\n\t\t\t\tdocument.getElementById(this.divName).style.visibility = \"hidden\";\n\t\t\t}\n\t\t\telse if (this.use_css) {\n\t\t\t\tdocument.all[this.divName].style.visibility = \"hidden\";\n\t\t\t}\n\t\t\telse if (this.use_layers) {\n\t\t\t\tdocument.layers[this.divName].visibility = \"hidden\";\n\t\t\t}\n\t\t}\n\t\telse {\n\t\t\tif (this.popupWindow && !this.popupWindow.closed) {\n\t\t\t\tthis.popupWindow.close();\n\t\t\t\tthis.popupWindow = null;\n\t\t\t}\n\t\t}\n\t\treturn false;\n\t}\n\tfunction colorSelect(t,p) {\n\t\tif ( cp.p == p && document.getElementById(cp.divName).style.visibility != \"hidden\" )\n\t\t\tcp.hidePopup(\'prettyplease\');\n\t\telse {\n\t\t\tcp.p = p;\n\t\t\tcp.select(t,p);\n\t\t}\n\t}\n\tfunction PopupWindow_setSize(width,height) {\n\t\tthis.width = 162;\n\t\tthis.height = 210;\n\t}\n\n\tvar cp = new ColorPicker();\n\tfunction advUpdate(val, obj) {\n\t\tdocument.getElementById(obj).value = val;\n\t\tkUpdate(obj);\n\t}\n\tfunction kUpdate(oid) {\n\t\tif ( \'uppercolor\' == oid || \'lowercolor\' == oid ) {\n\t\t\tuc = document.getElementById(\'uppercolor\').value.replace(\'#\', \'\');\n\t\t\tlc = document.getElementById(\'lowercolor\').value.replace(\'#\', \'\');\n\t\t\thi = document.getElementById(\'headerimage\');\n\t\t\thi.value = \'header-img.php?upper=\'+uc+\'&lower=\'+lc;\n\t\t\tdocument.getElementById(\'header\').style.background = \'url(\"");
        echo(gVars.webEnv, getIncluded(ThemePage.class, gVars, gConsts).get_template_directory_uri());
        echo(
                gVars.webEnv,
                "/images/\'+hi.value+\'\") center no-repeat\';\n\t\t\tdocument.getElementById(\'advuppercolor\').value = \'#\'+uc;\n\t\t\tdocument.getElementById(\'advlowercolor\').value = \'#\'+lc;\n\t\t}\n\t\tif ( \'fontcolor\' == oid ) {\n\t\t\tdocument.getElementById(\'header\').style.color = document.getElementById(\'fontcolor\').value;\n\t\t\tdocument.getElementById(\'advfontcolor\').value = document.getElementById(\'fontcolor\').value;\n\t\t}\n\t\tif ( \'fontdisplay\' == oid ) {\n\t\t\tdocument.getElementById(\'headerimg\').style.display = document.getElementById(\'fontdisplay\').value;\n\t\t}\n\t}\n\tfunction toggleDisplay() {\n\t\ttd = document.getElementById(\'fontdisplay\');\n\t\ttd.value = ( td.value == \'none\' ) ? \'inline\' : \'none\';\n\t\tkUpdate(\'fontdisplay\');\n\t}\n\tfunction toggleAdvanced() {\n\t\ta = document.getElementById(\'jsAdvanced\');\n\t\tif ( a.style.display == \'none\' )\n\t\t\ta.style.display = \'block\';\n\t\telse\n\t\t\ta.style.display = \'none\';\n\t}\n\tfunction kDefaults() {\n\t\tdocument.getElementById(\'headerimage\').value = \'\';\n\t\tdocument.getElementById(\'advuppercolor\').value = document.getElementById(\'uppercolor\').value = \'#e8eef7\';\n\t\tdocument.getElementById(\'advlowercolor\').value = document.getElementById(\'lowercolor\').value = \'#023d88\';\n\t\tdocument.getElementById(\'header\').style.background = \'url(\"");
        echo(gVars.webEnv, getIncluded(ThemePage.class, gVars, gConsts).get_template_directory_uri());
        echo(
                gVars.webEnv,
                "/images/kubrickheader.jpg\") center no-repeat\';\n\t\tdocument.getElementById(\'header\').style.color = \'#FFFFFF\';\n\t\tdocument.getElementById(\'advfontcolor\').value = document.getElementById(\'fontcolor\').value = \'\';\n\t\tdocument.getElementById(\'fontdisplay\').value = \'inline\';\n\t\tdocument.getElementById(\'headerimg\').style.display = document.getElementById(\'fontdisplay\').value;\n\t}\n\tfunction kRevert() {\n\t\tdocument.getElementById(\'headerimage\').value = \'");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).js_escape(kubrick_header_image()));
        echo(gVars.webEnv, "\';\n\t\tdocument.getElementById(\'advuppercolor\').value = document.getElementById(\'uppercolor\').value = \'#");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).js_escape(kubrick_upper_color()));
        echo(gVars.webEnv, "\';\n\t\tdocument.getElementById(\'advlowercolor\').value = document.getElementById(\'lowercolor\').value = \'#");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).js_escape(kubrick_lower_color()));
        echo(gVars.webEnv, "\';\n\t\tdocument.getElementById(\'header\').style.background = \'url(\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).js_escape(kubrick_header_image_url()));
        echo(
            gVars.webEnv,
            "\") center no-repeat\';\n\t\tdocument.getElementById(\'header\').style.color = \'\';\n\t\tdocument.getElementById(\'advfontcolor\').value = document.getElementById(\'fontcolor\').value = \'");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).js_escape(kubrick_header_color_string()));
        echo(gVars.webEnv, "\';\n\t\tdocument.getElementById(\'fontdisplay\').value = \'");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).js_escape(kubrick_header_display_string()));
        echo(
                gVars.webEnv,
                "\';\n\t\tdocument.getElementById(\'headerimg\').style.display = document.getElementById(\'fontdisplay\').value;\n\t}\n\tfunction kInit() {\n\t\tdocument.getElementById(\'jsForm\').style.display = \'block\';\n\t\tdocument.getElementById(\'nonJsForm\').style.display = \'none\';\n\t}\n\taddLoadEvent(kInit);\n// ]]>\n</script>\n<style type=\'text/css\'>\n\t#headwrap {\n\t\ttext-align: center;\n\t}\n\t#kubrick-header {\n\t\tfont-size: 80%;\n\t}\n\t#kubrick-header .hibrowser {\n\t\twidth: 780px;\n\t\theight: 260px;\n\t\toverflow: scroll;\n\t}\n\t#kubrick-header #hitarget {\n\t\tdisplay: none;\n\t}\n\t#kubrick-header #header h1 {\n\t\tfont-family: \'Trebuchet MS\', \'Lucida Grande\', Verdana, Arial, Sans-Serif;\n\t\tfont-weight: bold;\n\t\tfont-size: 4em;\n\t\ttext-align: center;\n\t\tpadding-top: 70px;\n\t\tmargin: 0;\n\t}\n\n\t#kubrick-header #header .description {\n\t\tfont-family: \'Lucida Grande\', Verdana, Arial, Sans-Serif;\n\t\tfont-size: 1.2em;\n\t\ttext-align: center;\n\t}\n\t#kubrick-header #header {\n\t\ttext-decoration: none;\n\t\tcolor: ");
        echo(gVars.webEnv, kubrick_header_color_string());
        echo(gVars.webEnv, ";\n\t\tpadding: 0;\n\t\tmargin: 0;\n\t\theight: 200px;\n\t\ttext-align: center;\n\t\tbackground: url(\'");
        echo(gVars.webEnv, kubrick_header_image_url());
        echo(gVars.webEnv, "\') center no-repeat;\n\t}\n\t#kubrick-header #headerimg {\n\t\tmargin: 0;\n\t\theight: 200px;\n\t\twidth: 100%;\n\t\tdisplay: ");
        echo(gVars.webEnv, kubrick_header_display_string());
        echo(
                gVars.webEnv,
                ";\n\t}\n\t#jsForm {\n\t\tdisplay: none;\n\t\ttext-align: center;\n\t}\n\t#jsForm input.submit, #jsForm input.button, #jsAdvanced input.button {\n\t\tpadding: 0px;\n\t\tmargin: 0px;\n\t}\n\t#advanced {\n\t\ttext-align: center;\n\t\twidth: 620px;\n\t}\n\thtml>body #advanced {\n\t\ttext-align: center;\n\t\tposition: relative;\n\t\tleft: 50%;\n\t\tmargin-left: -380px;\n\t}\n\t#jsAdvanced {\n\t\ttext-align: right;\n\t}\n\t#nonJsForm {\n\t\tposition: relative;\n\t\ttext-align: left;\n\t\tmargin-left: -370px;\n\t\tleft: 50%;\n\t}\n\t#nonJsForm label {\n\t\tpadding-top: 6px;\n\t\tpadding-right: 5px;\n\t\tfloat: left;\n\t\twidth: 100px;\n\t\ttext-align: right;\n\t}\n\t.defbutton {\n\t\tfont-weight: bold;\n\t}\n\t.zerosize {\n\t\twidth: 0px;\n\t\theight: 0px;\n\t\toverflow: hidden;\n\t}\n\t#colorPickerDiv a, #colorPickerDiv a:hover {\n\t\tpadding: 1px;\n\t\ttext-decoration: none;\n\t\tborder-bottom: 0px;\n\t}\n</style>\n");
    }

    public void kubrick_theme_page() {
        if (isset(gVars.webEnv._REQUEST.getValue("saved"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\"><p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Options saved.", "default") + "</strong></p></div>");
        }

        echo(gVars.webEnv, "<div class=\'wrap\'>\n\t<div id=\"kubrick-header\">\n\t<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header Image and Color", "default");
        echo(gVars.webEnv, "</h2>\n\t\t<div id=\"headwrap\">\n\t\t\t<div id=\"header\">\n\t\t\t\t<div id=\"headerimg\">\n\t\t\t\t\t<h1>");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
        echo(gVars.webEnv, "</h1>\n\t\t\t\t\t<div class=\"description\">");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("description");
        echo(gVars.webEnv, "</div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t\t<br />\n\t\t<div id=\"nonJsForm\">\n\t\t\t<form method=\"post\" action=\"\">\n\t\t\t\t");
        getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).wp_nonce_field("kubrick-header", "_wpnonce", true, true);
        echo(gVars.webEnv, "\t\t\t\t<div class=\"zerosize\"><input type=\"submit\" name=\"defaultsubmit\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save", "default")));
        echo(gVars.webEnv, "\" /></div>\n\t\t\t\t\t<label for=\"njfontcolor\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Font Color:", "default");
        echo(gVars.webEnv, "</label><input type=\"text\" name=\"njfontcolor\" id=\"njfontcolor\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_header_color()));
        echo(gVars.webEnv, "\" /> ");
        QStrings.printf(
            gVars.webEnv,
            getIncluded(L10nPage.class, gVars, gConsts).__("Any CSS color (%s or %s or %s)", "default"),
            "<code>red</code>",
            "<code>#FF0000</code>",
            "<code>rgb(255, 0, 0)</code>");
        echo(gVars.webEnv, "<br />\n\t\t\t\t\t<label for=\"njuppercolor\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Upper Color:", "default");
        echo(gVars.webEnv, "</label><input type=\"text\" name=\"njuppercolor\" id=\"njuppercolor\" value=\"#");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_upper_color()));
        echo(gVars.webEnv, "\" /> ");
        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("HEX only (%s or %s)", "default"), "<code>#FF0000</code>", "<code>#F00</code>");
        echo(gVars.webEnv, "<br />\n\t\t\t\t<label for=\"njlowercolor\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Lower Color:", "default");
        echo(gVars.webEnv, "</label><input type=\"text\" name=\"njlowercolor\" id=\"njlowercolor\" value=\"#");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_lower_color()));
        echo(gVars.webEnv, "\" /> ");
        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("HEX only (%s or %s)", "default"), "<code>#FF0000</code>", "<code>#F00</code>");
        echo(gVars.webEnv, "<br />\n\t\t\t\t<input type=\"hidden\" name=\"hi\" id=\"hi\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_header_image()));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"submit\" name=\"toggledisplay\" id=\"toggledisplay\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Toggle Text", "default")));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"submit\" name=\"defaults\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Use Defaults", "default")));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"submit\" class=\"defbutton\" name=\"submitform\" value=\"&nbsp;&nbsp;");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Save", "default");
        echo(
                gVars.webEnv,
                "&nbsp;&nbsp;\" />\n\t\t\t\t<input type=\"hidden\" name=\"action\" value=\"save\" />\n\t\t\t\t<input type=\"hidden\" name=\"njform\" value=\"true\" />\n\t\t\t</form>\n\t\t</div>\n\t\t<div id=\"jsForm\">\n\t\t\t<form style=\"display:inline;\" method=\"post\" name=\"hicolor\" id=\"hicolor\" action=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.webEnv.getRequestURI()));
        echo(gVars.webEnv, "\">\n\t\t\t\t");
        getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).wp_nonce_field("kubrick-header", "_wpnonce", true, true);
        echo(
            gVars.webEnv,
            "\t<input type=\"button\"  class=\"button-secondary\" onclick=\"tgt=document.getElementById(\'fontcolor\');colorSelect(tgt,\'pick1\');return false;\" name=\"pick1\" id=\"pick1\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Font Color", "default")));
        echo(
                gVars.webEnv,
                "\"></input>\n\t\t<input type=\"button\" class=\"button-secondary\" onclick=\"tgt=document.getElementById(\'uppercolor\');colorSelect(tgt,\'pick2\');return false;\" name=\"pick2\" id=\"pick2\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Upper Color", "default")));
        echo(
                gVars.webEnv,
                "\"></input>\n\t\t<input type=\"button\" class=\"button-secondary\" onclick=\"tgt=document.getElementById(\'lowercolor\');colorSelect(tgt,\'pick3\');return false;\" name=\"pick3\" id=\"pick3\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Lower Color", "default")));
        echo(gVars.webEnv, "\"></input>\n\t\t\t\t<input type=\"button\" class=\"button-secondary\" name=\"revert\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Revert", "default")));
        echo(gVars.webEnv, "\" onclick=\"kRevert()\" />\n\t\t\t\t<input type=\"button\" class=\"button-secondary\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Advanced", "default")));
        echo(
            gVars.webEnv,
            "\" onclick=\"toggleAdvanced()\" />\n\t\t\t\t<input type=\"hidden\" name=\"action\" value=\"save\" />\n\t\t\t\t<input type=\"hidden\" name=\"fontdisplay\" id=\"fontdisplay\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_header_display()));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"hidden\" name=\"fontcolor\" id=\"fontcolor\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_header_color()));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"hidden\" name=\"uppercolor\" id=\"uppercolor\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_upper_color()));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"hidden\" name=\"lowercolor\" id=\"lowercolor\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_lower_color()));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<input type=\"hidden\" name=\"headerimage\" id=\"headerimage\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_header_image()));
        echo(gVars.webEnv, "\" />\n\t\t\t\t<p class=\"submit\"><input type=\"submit\" name=\"submitform\" class=\"defbutton\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Update Header", "default")));
        echo(
                gVars.webEnv,
                "\" onclick=\"cp.hidePopup(\'prettyplease\')\" /></p>\n\t\t\t</form>\n\t\t\t<div id=\"colorPickerDiv\" style=\"z-index: 100;background:#eee;border:1px solid #ccc;position:absolute;visibility:hidden;\"> </div>\n\t\t\t<div id=\"advanced\">\n\t\t\t\t<form id=\"jsAdvanced\" style=\"display:none;\" action=\"\">\n\t\t\t\t\t");
        getIncluded(org.numiton.nwp.wp_includes.FunctionsPage.class, gVars, gConsts).wp_nonce_field("kubrick-header", "_wpnonce", true, true);
        echo(gVars.webEnv, "\t\t\t\t\t<label for=\"advfontcolor\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Font Color (CSS):", "default");
        echo(gVars.webEnv, " </label><input type=\"text\" id=\"advfontcolor\" onchange=\"advUpdate(this.value, \'fontcolor\')\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_header_color()));
        echo(gVars.webEnv, "\" /><br />\n\t\t\t\t\t<label for=\"advuppercolor\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Upper Color (HEX):", "default");
        echo(gVars.webEnv, " </label><input type=\"text\" id=\"advuppercolor\" onchange=\"advUpdate(this.value, \'uppercolor\')\" value=\"#");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_upper_color()));
        echo(gVars.webEnv, "\" /><br />\n\t\t\t\t\t<label for=\"advlowercolor\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Lower Color (HEX):", "default");
        echo(gVars.webEnv, " </label><input type=\"text\" id=\"advlowercolor\" onchange=\"advUpdate(this.value, \'lowercolor\')\" value=\"#");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(kubrick_lower_color()));
        echo(gVars.webEnv, "\" /><br />\n\t\t\t\t\t<input type=\"button\" class=\"button-secondary\" name=\"default\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select Default Colors", "default")));
        echo(gVars.webEnv,
            "\" onclick=\"kDefaults()\" /><br />\n\t\t\t\t\t<input type=\"button\" class=\"button-secondary\" onclick=\"toggleDisplay();return false;\" name=\"pick\" id=\"pick\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Toggle Text Display", "default")));
        echo(gVars.webEnv, "\"></input><br />\n\t\t\t\t</form>\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</div>\n");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_content_themes__default_functions_block1");
        gVars.webEnv = webEnv;

        if (true)/*Modified by Numiton*/
         {
            getIncluded(WidgetsPage.class, gVars, gConsts).register_sidebar(
                new Array<Object>(
                    new ArrayEntry<Object>("before_widget", "<li id=\"%1$s\" class=\"widget %2$s\">"),
                    new ArrayEntry<Object>("after_widget", "</li>"),
                    new ArrayEntry<Object>("before_title", "<h2 class=\"widgettitle\">"),
                    new ArrayEntry<Object>("after_title", "</h2>")));
        }

        getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", Callback.createCallbackArray(this, "kubrick_head"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_menu", Callback.createCallbackArray(this, "kubrick_add_theme_page"), 10, 1);

        return DEFAULT_VAL;
    }
}
