/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_mce_helpPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.Wp_configPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.numiton.nwp.wp_includes.General_templatePage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


@Controller
@Scope("request")
public class Wp_mce_helpPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_mce_helpPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/js/tinymce/wp-mce-help.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/wp_mce_help";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, Wp_configPage.class);
        Network.header(gVars.webEnv, "Content-Type: text/html; charset=" + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("charset", "raw"));

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block2");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block3");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block4");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block5");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Rich Editor Help", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block6");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("wp-admin");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block7");

        if (equal("rtl", gVars.wp_locale.text_direction)) {
            echo(
                    gVars.webEnv,
                    "<style type=\"text/css\">\n\t#wphead, #adminmenu {\n\t\tpadding-left: auto;\n\t\tpadding-right: 15px;\n\t}\n\t#flipper {\n\t\tmargin: 5px 0 3px 10px;\n\t}\n\t.keys .left, .top, .action { text-align: right; }\n\t.keys .right { text-align: left; }\n\ttd b { font-family: Tahoma, \"Times New Roman\", Times, serif }\n</style>\n");
        } else {
        }

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block8");
        echo(gVars.webEnv, getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("blogtitle", "raw"));

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block9");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Basics of Rich Editing", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block10");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Basics", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block11");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced use of the Rich Editor", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block12");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block13");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Hotkeys", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block14");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Hotkeys", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block15");
        getIncluded(L10nPage.class, gVars, gConsts)._e("About the software", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block16");
        getIncluded(L10nPage.class, gVars, gConsts)._e("About", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block17");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Rich Editing Basics", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block18");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "<em>Rich editing</em>, also called WYSIWYG for What You See Is What You Get, means your text is formatted as you type. The rich editor creates HTML code behind the scenes while you concentrate on writing. Font styles, links and images all appear approximately as they will on the internet.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block19");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "nWordPress includes a rich HTML editor that works well in all major web browsers used today. However editing HTML is not the same as typing text. Each web page has two major components: the structure, which is the actual HTML code and is produced by the editor as you type, and the display, that is applied to it by the currently selected nWordPress theme and is defined in style.css. nWordPress is producing valid XHTML 1.0 which means that inserting multiple line breaks (BR tags) after a paragraph would not produce white space on the web page. The BR tags will be removed as invalid by the internal HTML correcting functions.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block20");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "While using the editor, most basic keyboard shortcuts work like in any other text editor. For example: Shift+Enter inserts line break, Ctrl+C = copy, Ctrl+X = cut, Ctrl+Z = undo, Ctrl+Y = redo, Ctrl+A = select all, etc. (on Mac use the Command key instead of Ctrl). See the Hotkeys tab for all available keyboard shortcuts.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block21");
        getIncluded(L10nPage.class, gVars, gConsts)._e("If you do not like the way the rich editor works, you may turn it off from Your Profile submenu, under Users in the admin menu.", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block22");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Advanced Rich Editing", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block23");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Images and Attachments", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block24");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "There is a button in the editor toolbar for inserting images that are already hosted somewhere on the internet. If you have a URL for an image, click this button and enter the URL in the box which appears.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block25");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "If you need to upload an image or another media file from your computer, you can use the Media Library buttons above the editor. The media library will attempt to create a thumbnail-sized copy from each uploaded image. To insert your image into the post, first click on the thumbnail to reveal a menu of options. When you have selected the options you like, click \"Send to Editor\" and your image or file will appear in the post you are editing. If you are inserting a movie, there are additional options in the \"Media\" dialog that can be opened from the second toolbar row.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block26");
        getIncluded(L10nPage.class, gVars, gConsts)._e("HTML in the Rich Editor", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block27");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Any HTML entered directly into the rich editor will show up as text when the post is viewed. What you see is what you get. When you want to include HTML elements that cannot be generated with the toolbar buttons, you must enter it by hand in the HTML editor. Examples are tables and &lt;code&gt;. To do this, click the HTML tab and edit the code, then switch back to Visual mode. If the code is valid and understood by the editor, you should see it rendered immediately.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block28");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Pasting in the Rich Editor", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block29");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "When pasting content from another web page the results can be inconsistent and depend on your browser and on the web page you are pasting from. The editor tries to correct any invalid HTML code that was pasted, but for best results try using the HTML tab or one of the paste buttons that are on the second row. Alternatively try pasting paragraph by paragraph. In most browsers to select one paragraph at a time, triple-click on it.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block30");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
            "Pasting content from another application, like Word or Excel, is best done with the Paste from Word button on the second row, or in HTML mode.",
            "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block31");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Writing at Full Speed", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block32");
        getIncluded(L10nPage.class, gVars, gConsts)._e(
                "Rather than reaching for your mouse to click on the toolbar, use these access keys. Windows and Linux use Ctrl + letter. Macintosh uses Command + letter.",
                "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block33");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Letter", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block34");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Action", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block35");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Letter", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block36");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Action", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block37");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Copy", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block38");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Paste", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block39");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Select all", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block40");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Cut", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block41");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Undo", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block42");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Redo", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block43");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Bold", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block44");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Italic", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block45");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Underline", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block46");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header 1", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block47");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header 2", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block48");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header 3", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block49");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header 4", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block50");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header 5", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block51");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Header 6", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block52");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Address", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block53");
        getIncluded(L10nPage.class, gVars, gConsts)._e("The following shortcuts use different access keys: Alt + Shift + letter.", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block54");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Letter", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block55");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Action", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block56");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Letter", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block57");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Action", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block58");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Bold", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block59");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Italic", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block60");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Check Spelling", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block61");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Align Left", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block62");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Justify Text", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block63");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Align Center", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block64");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Strikethrough", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block65");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Align Right", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block66");
        getIncluded(L10nPage.class, gVars, gConsts)._e("List", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block67");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Insert link", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block68");
        getIncluded(L10nPage.class, gVars, gConsts)._e("List", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block69");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Remove link", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block70");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Quote", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block71");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Insert Image", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block72");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Full Screen", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block73");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Insert More Tag", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block74");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Insert Page Break tag", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block75");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Help", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block76");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Switch to HTML mode", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block77");
        getIncluded(L10nPage.class, gVars, gConsts)._e("About TinyMCE", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block78");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Version:", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block79");
        QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "TinyMCE is a platform independent web based Javascript HTML WYSIWYG editor control released as Open Source under %sLGPL</a>\tby Moxiecode Systems AB. It has the ability to convert HTML TEXTAREA fields or other HTML elements to editor instances.",
                        "default"),
                "<a href=\"" + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw") + "/wp-includes/js/tinymce/license.txt\" target=\"_blank\" title=\"" +
                getIncluded(L10nPage.class, gVars, gConsts).__("GNU Library General Public Licence", "default") + "\">");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block80");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Copyright &copy; 2003-2007, <a href=\"http://www.moxiecode.com\" target=\"_blank\">Moxiecode Systems AB</a>, All rights reserved.", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block81");
        getIncluded(L10nPage.class, gVars, gConsts)._e("For more information about this software visit the <a href=\"http://tinymce.moxiecode.com\" target=\"_blank\">TinyMCE website</a>.", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block82");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Got Moxie?", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block83");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Hosted By Sourceforge", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block84");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Also on freshmeat", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block85");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Close", "default");

        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_wp_mce_help_block86");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Close", "default");

        return DEFAULT_VAL;
    }
}
