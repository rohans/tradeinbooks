/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Custom_Image_Header.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.FilePage;
import org.numiton.nwp.wp_admin.includes.ImagePage;
import org.numiton.nwp.wp_admin.includes.PluginPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PostPage;

import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QImage;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class Custom_Image_Header implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Custom_Image_Header.class.getName());

    // Added by Numiton
    public static final int HEADER_IMAGE_WIDTH = 1;
    public static final int HEADER_IMAGE_HEIGHT = 1;
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> admin_header_callback;

    /**
     * Generated in place of local variable 'type' from method 'step_2' because
     * it is used inside an inner class.
     */
    Object step_2_type = null;

    /**
     * Generated in place of local variable 'width' from method 'step_2' because
     * it is used inside an inner class.
     */
    int step_2_width;

    /**
     * Generated in place of local variable 'height' from method 'step_2'
     * because it is used inside an inner class.
     */
    int step_2_height;

    /**
     * Generated in place of local variable 'attr' from method 'step_2' because
     * it is used inside an inner class.
     */
    Object step_2_attr = null;

    public Custom_Image_Header(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Array<Object> admin_header_callback) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.admin_header_callback = admin_header_callback;
    }

    public void init() {
        String page = null;
        page = getIncluded(PluginPage.class, gVars, gConsts).add_theme_page(
                getIncluded(L10nPage.class, gVars, gConsts).__("Custom Image Header", "default"),
                getIncluded(L10nPage.class, gVars, gConsts).__("Custom Image Header", "default"),
                "edit_themes",
                "custom-header",
                Callback.createCallbackArray(this, "admin_page"));
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_print_scripts-" + page,
            new Array<Object>(new ArrayEntry<Object>(this), new ArrayEntry<Object>("js_includes")), 10, 1);
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_head-" + page, Callback.createCallbackArray(this, "take_action"), 50, 1);
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_head-" + page, Callback.createCallbackArray(this, "js"), 50, 1);
        (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).add_action("admin_head-" + page, this.admin_header_callback, 51, 1);
    }

    public int step() {
        int step = 0;
        step = intval(gVars.webEnv._GET.getValue("step"));

        if ((step < 1) || (3 < step)) {
            step = 1;
        }

        return step;
    }

    public void js_includes() {
        int step = 0;
        step = this.step();

        if (equal(1, step)) {
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("colorpicker", false, new Array<Object>(), false);
        } else if (equal(2, step)) {
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("cropper", false, new Array<Object>(), false);
        }
    }

    public void take_action() {
        String color;

        if (isset(gVars.webEnv._POST.getValue("textcolor"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("custom-header", "_wpnonce");

            if (equal("blank", gVars.webEnv._POST.getValue("textcolor"))) {
                getIncluded(ThemePage.class, gVars, gConsts).set_theme_mod("header_textcolor", "blank");
            } else {
                color = QRegExPerl.preg_replace("/[^0-9a-fA-F]/", "", strval(gVars.webEnv._POST.getValue("textcolor")));

                if (equal(Strings.strlen(color), 6) || equal(Strings.strlen(color), 3)) {
                    getIncluded(ThemePage.class, gVars, gConsts).set_theme_mod("header_textcolor", color);
                }
            }
        }

        if (isset(gVars.webEnv._POST.getValue("resetheader"))) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("custom-header", "_wpnonce");
            getIncluded(ThemePage.class, gVars, gConsts).remove_theme_mods();
        }
    }

    public void js() {
        int step = 0;
        step = this.step();

        if (equal(1, step)) {
            this.js_1();
        } else if (equal(2, step)) {
            this.js_2();
        }
    }

    public void js_1() {
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\n\tvar cp = new ColorPicker();\n\n\tfunction pickColor(color) {\n\t\t$(\'name\').style.color = color;\n\t\t$(\'desc\').style.color = color;\n\t\t$(\'textcolor\').value = color;\n\t}\n\tfunction PopupWindow_hidePopup(magicword) {\n\t\tif ( magicword != \'prettyplease\' )\n\t\t\treturn false;\n\t\tif (this.divName != null) {\n\t\t\tif (this.use_gebi) {\n\t\t\t\tdocument.getElementById(this.divName).style.visibility = \"hidden\";\n\t\t\t}\n\t\t\telse if (this.use_css) {\n\t\t\t\tdocument.all[this.divName].style.visibility = \"hidden\";\n\t\t\t}\n\t\t\telse if (this.use_layers) {\n\t\t\t\tdocument.layers[this.divName].visibility = \"hidden\";\n\t\t\t}\n\t\t}\n\t\telse {\n\t\t\tif (this.popupWindow && !this.popupWindow.closed) {\n\t\t\t\tthis.popupWindow.close();\n\t\t\t\tthis.popupWindow = null;\n\t\t\t}\n\t\t}\n\t\treturn false;\n\t}\n\tfunction colorSelect(t,p) {\n\t\tif ( cp.p == p && document.getElementById(cp.divName).style.visibility != \"hidden\" ) {\n\t\t\tcp.hidePopup(\'prettyplease\');\n\t\t} else {\n\t\t\tcp.p = p;\n\t\t\tcp.select(t,p);\n\t\t}\n\t}\n\tfunction colorDefault() {\n\t\tpickColor(\'#");
        echo(gVars.webEnv, "HEADER_TEXTCOLOR");
        echo(
                gVars.webEnv,
                "\');\n\t}\n\n\tfunction hide_text() {\n\t\t$(\'name\').style.display = \'none\';\n\t\t$(\'desc\').style.display = \'none\';\n\t\t$(\'pickcolor\').style.display = \'none\';\n\t\t$(\'defaultcolor\').style.display = \'none\';\n\t\t$(\'textcolor\').value = \'blank\';\n\t\t$(\'hidetext\').value = \'");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Show Text", "default");
        echo(
                gVars.webEnv,
                "\';\n//\t\t$(\'hidetext\').onclick = \'show_text()\';\n\t\tEvent.observe( $(\'hidetext\'), \'click\', show_text );\n\t}\n\n\tfunction show_text() {\n\t\t$(\'name\').style.display = \'block\';\n\t\t$(\'desc\').style.display = \'block\';\n\t\t$(\'pickcolor\').style.display = \'inline\';\n\t\t$(\'defaultcolor\').style.display = \'inline\';\n\t\t$(\'textcolor\').value = \'");
        echo(gVars.webEnv, "HEADER_TEXTCOLOR");
        echo(gVars.webEnv, "\';\n\t\t$(\'hidetext\').value = \'");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Hide Text", "default");
        echo(gVars.webEnv, "\';\n\t\tEvent.stopObserving( $(\'hidetext\'), \'click\', show_text );\n\t\tEvent.observe( $(\'hidetext\'), \'click\', hide_text );\n\t}\n\n\t");

        if (equal("blank", getIncluded(ThemePage.class, gVars, gConsts).get_theme_mod("header_textcolor", "HEADER_TEXTCOLOR"))) {
            echo(gVars.webEnv, "Event.observe( window, \'load\', hide_text );\n\t");
        }

        echo(gVars.webEnv, "\n</script>\n");
    }

    public void js_2() {
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\n\tfunction onEndCrop( coords, dimensions ) {\n\t\t$( \'x1\' ).value = coords.x1;\n\t\t$( \'y1\' ).value = coords.y1;\n\t\t$( \'x2\' ).value = coords.x2;\n\t\t$( \'y2\' ).value = coords.y2;\n\t\t$( \'width\' ).value = dimensions.width;\n\t\t$( \'height\' ).value = dimensions.height;\n\t}\n\n\t// with a supplied ratio\n\tEvent.observe(\n\t\twindow,\n\t\t\'load\',\n\t\tfunction() {\n\t\t\tvar xinit = ");
        echo(gVars.webEnv, HEADER_IMAGE_WIDTH);
        echo(gVars.webEnv, ";\n\t\t\tvar yinit = ");
        echo(gVars.webEnv, HEADER_IMAGE_HEIGHT);
        echo(
                gVars.webEnv,
                ";\n\t\t\tvar ratio = xinit / yinit;\n\t\t\tvar ximg = $(\'upload\').width;\n\t\t\tvar yimg = $(\'upload\').height;\n\t\t\tif ( yimg < yinit || ximg < xinit ) {\n\t\t\t\tif ( ximg / yimg > ratio ) {\n\t\t\t\t\tyinit = yimg;\n\t\t\t\t\txinit = yinit * ratio;\n\t\t\t\t} else {\n\t\t\t\t\txinit = ximg;\n\t\t\t\t\tyinit = xinit / ratio;\n\t\t\t\t}\n\t\t\t}\n\t\t\tnew Cropper.Img(\n\t\t\t\t\'upload\',\n\t\t\t\t{\n\t\t\t\t\tratioDim: { x: xinit, y: yinit },\n\t\t\t\t\tdisplayOnInit: true,\n\t\t\t\t\tonEndCrop: onEndCrop\n\t\t\t\t}\n\t\t\t)\n\t\t}\n\t);\n</script>\n");
    }

    public void step_1() {
        if (booleanval(gVars.webEnv._GET.getValue("updated"))) {
            echo(gVars.webEnv, "<div id=\"message\" class=\"updated fade\">\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Header updated.", "default");
            echo(gVars.webEnv, "</p>\n</div>\n\t\t");
        }

        echo(gVars.webEnv, "\n<div class=\"wrap\">\n<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Your Header Image", "default");
        echo(gVars.webEnv, "</h2>\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("This is your header image. You can change the text color or upload and crop a new image.", "default");
        echo(gVars.webEnv, "</p>\n\n<div id=\"headimg\" style=\"background-image: url(");
        getIncluded(FormattingPage.class, gVars, gConsts).clean_url(getIncluded(ThemePage.class, gVars, gConsts).header_image(), null, "display");
        echo(gVars.webEnv, ");\">\n<h1><a onclick=\"return false;\" href=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("url");
        echo(gVars.webEnv, "\" title=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
        echo(gVars.webEnv, "\" id=\"name\">");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
        echo(gVars.webEnv, "</a></h1>\n<div id=\"desc\">");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("description");
        echo(gVars.webEnv, "</div>\n</div>\n");

        if (!gConsts.isNO_HEADER_TEXTDefined()) {
            echo(gVars.webEnv, "<form method=\"post\" action=\"");
            echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"));
            echo(gVars.webEnv, "/wp-admin/themes.php?page=custom-header&amp;updated=true\">\n<input type=\"button\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Hide Text", "default");
            echo(gVars.webEnv, "\" onclick=\"hide_text()\" id=\"hidetext\" />\n<input type=\"button\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Select a Text Color", "default");
            echo(gVars.webEnv, "\" onclick=\"colorSelect($(\'textcolor\'), \'pickcolor\')\" id=\"pickcolor\" /><input type=\"button\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Use Original Color", "default");
            echo(gVars.webEnv, "\" onclick=\"colorDefault()\" id=\"defaultcolor\" />\n");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("custom-header", "_wpnonce", true, true);
            echo(gVars.webEnv, "<input type=\"hidden\" name=\"textcolor\" id=\"textcolor\" value=\"#");

            //Modified by Numiton
            ( /*getIncluded(FormattingPage.class, gVars, gConsts)
                          .attribute_escape(*/
            ((ThemePage) getIncluded(ThemePage.class, gVars, gConsts))).header_textcolor();

            /*)*/
            echo(gVars.webEnv, "\" /><input name=\"submit\" type=\"submit\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Save Changes", "default");
            echo(gVars.webEnv, "\" /></form>\n");
        }

        echo(gVars.webEnv, "\n<div id=\"colorPickerDiv\" style=\"z-index: 100;background:#eee;border:1px solid #ccc;position:absolute;visibility:hidden;\"> </div>\n</div>\n<div class=\"wrap\">\n<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Upload New Header Image", "default");
        echo(gVars.webEnv, "</h2><p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Here you can upload a custom header image to be shown at the top of your blog instead of the default one. On the next screen you will be able to crop the image.",
                "default");
        echo(gVars.webEnv, "</p>\n<p>");
        QStrings.printf(
            gVars.webEnv,
            getIncluded(L10nPage.class, gVars, gConsts).__("Images of exactly <strong>%1$d x %2$d pixels</strong> will be used as-is.", "default"),
            HEADER_IMAGE_WIDTH,
            HEADER_IMAGE_HEIGHT);
        echo(gVars.webEnv, "</p>\n\n<form enctype=\"multipart/form-data\" id=\"uploadForm\" method=\"POST\" action=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("step", 2)));
        echo(gVars.webEnv, "\" style=\"margin: auto; width: 50%;\">\n<label for=\"upload\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Choose an image from your computer:", "default");
        echo(gVars.webEnv, "</label><br /><input type=\"file\" id=\"upload\" name=\"import\" />\n<input type=\"hidden\" name=\"action\" value=\"save\" />\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("custom-header", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\">\n<input type=\"submit\" value=\"");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Upload", "default");
        echo(gVars.webEnv, "\" />\n</p>\n</form>\n\n</div>\n\n\t\t");

        if (booleanval(getIncluded(ThemePage.class, gVars, gConsts).get_theme_mod("header_image", strval(false))) ||
                booleanval(getIncluded(ThemePage.class, gVars, gConsts).get_theme_mod("header_textcolor", strval(false)))) {
            echo(gVars.webEnv, "<div class=\"wrap\">\n<h2>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Reset Header Image and Color", "default");
            echo(gVars.webEnv, "</h2>\n<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("This will restore the original header image and color. You will not be able to retrieve any customizations.", "default");
            echo(gVars.webEnv, "</p>\n<form method=\"post\" action=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("step", 1)));
            echo(gVars.webEnv, "\">\n");
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("custom-header", "_wpnonce", true, true);
            echo(gVars.webEnv, "<input type=\"submit\" name=\"resetheader\" value=\"");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Restore Original Header", "default");
            echo(gVars.webEnv, "\" />\n</form>\n</div>\n\t\t");
        } else {
        }
    }

    public void step_2() {
        Array<Object> overrides = new Array<Object>();
        Array<Object> file = new Array<Object>();
        String url;
        String filename = null;
        Array<Object> object = new Array<Object>();
        int id = 0;
        Object oitar = null;
        String image = null;
        getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("custom-header", "_wpnonce");
        overrides = new Array<Object>(new ArrayEntry<Object>("test_form", false));
        file = getIncluded(FilePage.class, gVars, gConsts).wp_handle_upload(gVars.webEnv._FILES.getArrayValue("import"), overrides);

        if (isset(file.getValue("error"))) {
            System.exit(intval(file.getValue("error")));
        }

        url = strval(file.getValue("url"));
        step_2_type = file.getValue("type");

        String fileStr = strval(file.getValue("file"));
        filename = FileSystemOrSocket.basename(fileStr);
        
        // Construct the object array
        object = new Array<Object>(
                new ArrayEntry<Object>("post_title", filename),
                new ArrayEntry<Object>("post_content", url),
                new ArrayEntry<Object>("post_mime_type", step_2_type),
                new ArrayEntry<Object>("guid", url));
        
        // Save the data
        id = getIncluded(PostPage.class, gVars, gConsts).wp_insert_attachment(object, fileStr, 0);
        
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    step_2_width = intval(srcArray.getValue(0));
                    step_2_height = intval(srcArray.getValue(1));
                    step_2_type = srcArray.getValue(2);
                    step_2_attr = srcArray.getValue(3);

                    return srcArray;
                }
            }.doAssign(QImage.getimagesize(gVars.webEnv, fileStr));

        if (equal(step_2_width, HEADER_IMAGE_WIDTH) && equal(step_2_height, HEADER_IMAGE_HEIGHT)) {
        	// Add the meta-data
            getIncluded(PostPage.class, gVars, gConsts).wp_update_attachment_metadata(id, getIncluded(ImagePage.class, gVars, gConsts).wp_generate_attachment_metadata(id, fileStr));
            
            getIncluded(ThemePage.class, gVars, gConsts).set_theme_mod("header_image", getIncluded(FormattingPage.class, gVars, gConsts).clean_url(url, null, "display"));
            (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).do_action("wp_create_file_in_uploads", fileStr, id); // For replication
            this.finished();

            return;
        } else if (step_2_width > HEADER_IMAGE_WIDTH) {
            oitar = floatval(step_2_width) / floatval(HEADER_IMAGE_WIDTH);
            image = getIncluded(ImagePage.class, gVars, gConsts).wp_crop_image(
                    fileStr,
                    0,
                    0,
                    step_2_width,
                    step_2_height,
                    HEADER_IMAGE_WIDTH,
                    intval(floatval(step_2_height) / floatval(oitar)),
                    false,
                    Strings.str_replace(FileSystemOrSocket.basename(fileStr), "midsize-" + FileSystemOrSocket.basename(fileStr), fileStr));
            image = strval((((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters("wp_create_file_in_uploads", image, id)); // For replication
            
            url = Strings.str_replace(FileSystemOrSocket.basename(url), FileSystemOrSocket.basename(image), url);
            step_2_width = intval(floatval(step_2_width) / floatval(oitar));
            step_2_height = intval(floatval(step_2_height) / floatval(oitar));
        } else {
            oitar = 1;
        }

        echo(gVars.webEnv, "\n<div class=\"wrap\">\n\n<form method=\"POST\" action=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("step", 3)));
        echo(gVars.webEnv, "\">\n\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Choose the part of the image you want to use as your header.", "default");
        echo(gVars.webEnv, "</p>\n<div id=\"testWrap\" style=\"position: relative\">\n<img src=\"");
        echo(gVars.webEnv, url);
        echo(gVars.webEnv, "\" id=\"upload\" width=\"");
        echo(gVars.webEnv, step_2_width);
        echo(gVars.webEnv, "\" height=\"");
        echo(gVars.webEnv, step_2_height);
        echo(
                gVars.webEnv,
                "\" />\n</div>\n\n<p class=\"submit\">\n<input type=\"hidden\" name=\"x1\" id=\"x1\" />\n<input type=\"hidden\" name=\"y1\" id=\"y1\" />\n<input type=\"hidden\" name=\"x2\" id=\"x2\" />\n<input type=\"hidden\" name=\"y2\" id=\"y2\" />\n<input type=\"hidden\" name=\"width\" id=\"width\" />\n<input type=\"hidden\" name=\"height\" id=\"height\" />\n<input type=\"hidden\" name=\"attachment_id\" id=\"attachment_id\" value=\"");
        echo(gVars.webEnv, id);
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"oitar\" id=\"oitar\" value=\"");
        echo(gVars.webEnv, oitar);
        echo(gVars.webEnv, "\" />\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("custom-header", "_wpnonce", true, true);
        echo(gVars.webEnv, "<input type=\"submit\" value=\"");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Crop Header", "default");
        echo(gVars.webEnv, "\" />\n</p>\n\n</form>\n</div>\n\t\t");
    }

    public void step_3() {
        String original = null;
        String cropped = null;
        StdClass parent = null;
        String parent_url = null;
        String url;
        Array<Object> object = new Array<Object>();
        String medium;
        getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("custom-header", "_wpnonce");

        if (intval(gVars.webEnv._POST.getValue("oitar")) > 1) {
            gVars.webEnv._POST.putValue("x1", intval(gVars.webEnv._POST.getValue("x1")) * intval(gVars.webEnv._POST.getValue("oitar")));
            gVars.webEnv._POST.putValue("y1", intval(gVars.webEnv._POST.getValue("y1")) * intval(gVars.webEnv._POST.getValue("oitar")));
            gVars.webEnv._POST.putValue("width", intval(gVars.webEnv._POST.getValue("width")) * intval(gVars.webEnv._POST.getValue("oitar")));
            gVars.webEnv._POST.putValue("height", intval(gVars.webEnv._POST.getValue("height")) * intval(gVars.webEnv._POST.getValue("oitar")));
        }

        original = strval(getIncluded(PostPage.class, gVars, gConsts).get_attached_file(intval(gVars.webEnv._POST.getValue("attachment_id")), false));
        
        cropped = getIncluded(ImagePage.class, gVars, gConsts).wp_crop_image(
                gVars.webEnv._POST.getValue("attachment_id"),
                intval(gVars.webEnv._POST.getValue("x1")),
                intval(gVars.webEnv._POST.getValue("y1")),
                intval(gVars.webEnv._POST.getValue("width")),
                intval(gVars.webEnv._POST.getValue("height")),
                HEADER_IMAGE_WIDTH,
                HEADER_IMAGE_HEIGHT,
                false,
                "");
        cropped = strval(
                (((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters(
                    "wp_create_file_in_uploads",
                    cropped,
                    gVars.webEnv._POST.getValue("attachment_id")));
        
        parent = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(gVars.webEnv._POST.getValue("attachment_id"), gConsts.getOBJECT(), "raw");
        parent_url = strval(StdClass.getValue(parent, "guid"));
        url = Strings.str_replace(FileSystemOrSocket.basename(parent_url), FileSystemOrSocket.basename(cropped), parent_url);
        
        // Construct the object array
        object = new Array<Object>(
                new ArrayEntry<Object>("ID", gVars.webEnv._POST.getValue("attachment_id")),
                new ArrayEntry<Object>("post_title", FileSystemOrSocket.basename(cropped)),
                new ArrayEntry<Object>("post_content", url),
                new ArrayEntry<Object>("post_mime_type", "image/jpeg"),
                new ArrayEntry<Object>("guid", url));
        
        // Update the attachment
        getIncluded(PostPage.class, gVars, gConsts).wp_insert_attachment(object, cropped, 0);
        getIncluded(PostPage.class, gVars, gConsts).wp_update_attachment_metadata(
            gVars.webEnv._POST.getValue("attachment_id"),
            getIncluded(ImagePage.class, gVars, gConsts).wp_generate_attachment_metadata(intval(gVars.webEnv._POST.getValue("attachment_id")), cropped));
        
        getIncluded(ThemePage.class, gVars, gConsts).set_theme_mod("header_image", url);
        
        // cleanup
        medium = Strings.str_replace(FileSystemOrSocket.basename(original), "midsize-" + FileSystemOrSocket.basename(original), original);
        JFileSystemOrSocket.unlink(gVars.webEnv, strval((((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters("wp_delete_file", medium)));
        JFileSystemOrSocket.unlink(gVars.webEnv, strval((((org.numiton.nwp.wp_includes.PluginPage) getIncluded(org.numiton.nwp.wp_includes.PluginPage.class, gVars, gConsts))).apply_filters("wp_delete_file", original)));
        
        this.finished();
    }

    public void finished() {
        echo(gVars.webEnv, "<div class=\"wrap\">\n<h2>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header complete!", "default");
        echo(gVars.webEnv, "</h2>\n\n<p>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Visit your site and you should see the new header now.", "default");
        echo(gVars.webEnv, "</p>\n\n</div>\n\t\t");
    }

    public void admin_page() {
        int step = 0;
        step = this.step();

        if (equal(1, step)) {
            this.step_1();
        } else if (equal(2, step)) {
            this.step_2();
        } else if (equal(3, step)) {
            this.step_3();
        }
    }

    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}
