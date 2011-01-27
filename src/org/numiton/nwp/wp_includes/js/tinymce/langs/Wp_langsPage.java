/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_langsPage.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.langs;

import static com.numiton.VarHandling.equal;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.FormattingPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.generic.PhpWebEnvironment;


@Controller
@Scope("request")
public class Wp_langsPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Wp_langsPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/js/tinymce/langs/wp-langs.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/js/tinymce/langs/wp_langs";
    }

 // escape text only if it needs translating
    public String mce_escape(String text) {
        if (equal("en", gVars.language)) {
            return text;
        } else {
            return getIncluded(FormattingPage.class, gVars, gConsts).js_escape(text);
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_js_tinymce_langs_wp_langs_block1");
        gVars.webEnv = webEnv;
        gVars.strings = "tinyMCE.addI18n({" + gVars.language + ":{\ncommon:{\nedit_confirm:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Do you want to use the WYSIWYG mode for this textarea?", "default")) + "\",\napply:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Apply", "default")) + "\",\ninsert:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert", "default")) +
            "\",\nupdate:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Update", "default")) + "\",\ncancel:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cancel", "default")) + "\",\nclose:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Close", "default")) +
            "\",\nbrowse:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Browse", "default")) + "\",\nclass_name:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Class", "default")) + "\",\nnot_set:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("-- Not set --", "default")) + "\",\nclipboard_msg:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Copy/Cut/Paste is not available in Mozilla and Firefox.", "default")) + "\",\nclipboard_no_support:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Currently not supported by your browser, use keyboard shortcuts instead.", "default")) + "\",\npopup_blocked:\"" +
            mce_escape(
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "Sorry, but we have noticed that your popup-blocker has disabled a window that provides application functionality. You will need to disable popup blocking on this site in order to fully utilize this tool.",
                            "default")) + "\",\ninvalid_data:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Error: Invalid values entered, these are marked in red.", "default")) +
            "\",\nmore_colors:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("More colors", "default")) + "\"\n},\ncontextmenu:{\nalign:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Alignment", "default")) + "\",\nleft:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Left", "default")) +
            "\",\ncenter:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Center", "default")) + "\",\nright:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Right", "default")) + "\",\nfull:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Full", "default")) +
            "\"\n},\ninsertdatetime:{\ndate_fmt:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("%Y-%m-%d", "default")) + "\",\ntime_fmt:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("%H:%M:%S", "default")) + "\",\ninsertdate_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert date", "default")) + "\",\ninserttime_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert time", "default")) + "\",\nmonths_long:\"" +
            mce_escape(
                    getIncluded(L10nPage.class, gVars, gConsts).__("January", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("February", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("March", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("April", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("May", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("June", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("July", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("August", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("September", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("October", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("November", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("December", "default")) + "\",\nmonths_short:\"" +
            mce_escape(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Jan_January_abbreviation", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Feb_February_abbreviation", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Mar_March_abbreviation", "default") +
                    "," + getIncluded(L10nPage.class, gVars, gConsts).__("Apr_April_abbreviation", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("May_May_abbreviation", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Jun_June_abbreviation", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Jul_July_abbreviation", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Aug_August_abbreviation", "default") +
                    "," + getIncluded(L10nPage.class, gVars, gConsts).__("Sep_September_abbreviation", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Oct_October_abbreviation", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Nov_November_abbreviation", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Dec_December_abbreviation", "default")) + "\",\nday_long:\"" +
            mce_escape(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Sunday", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Monday", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Tuesday", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Wednesday", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Thursday", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Friday", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Saturday", "default")) + "\",\nday_short:\"" +
            mce_escape(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Sun", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Mon", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Tue", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Wed", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Thu", "default") + "," + getIncluded(L10nPage.class, gVars, gConsts).__("Fri", "default") + "," +
                    getIncluded(L10nPage.class, gVars, gConsts).__("Sat", "default")) + "\"\n},\nprint:{\nprint_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Print", "default")) + "\"\n},\npreview:{\npreview_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Preview", "default")) + "\"\n},\ndirectionality:{\nltr_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Direction left to right", "default")) + "\",\nrtl_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Direction right to left", "default")) + "\"\n},\nlayer:{\ninsertlayer_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert new layer", "default")) + "\",\nforward_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Move forward", "default")) + "\",\nbackward_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Move backward", "default")) + "\",\nabsolute_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Toggle absolute positioning", "default")) + "\",\ncontent:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("New layer...", "default")) + "\"\n},\nsave:{\nsave_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save", "default")) + "\",\ncancel_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cancel all changes", "default")) + "\"\n},\nnonbreaking:{\nnonbreaking_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert non-breaking space character", "default")) + "\"\n},\niespell:{\niespell_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Run spell checking", "default")) + "\",\ndownload:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("ieSpell not detected. Do you want to install it now?", "default")) + "\"\n},\nadvhr:{\nadvhr_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Horizontale rule", "default")) + "\"\n},\nemotions:{\nemotions_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Emotions", "default")) + "\"\n},\nsearchreplace:{\nsearch_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Find", "default")) + "\",\nreplace_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Find/Replace", "default")) + "\"\n},\nadvimage:{\nimage_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit image", "default")) + "\"\n},\nadvlink:{\nlink_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit link", "default")) + "\"\n},\nxhtmlxtras:{\ncite_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Citation", "default")) + "\",\nabbr_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Abbreviation", "default")) + "\",\nacronym_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Acronym", "default")) + "\",\ndel_desc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Deletion", "default")) +
            "\",\nins_desc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insertion", "default")) + "\",\nattribs_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/Edit Attributes", "default")) + "\"\n},\nstyle:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Edit CSS Style", "default")) + "\"\n},\npaste:{\npaste_text_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Paste as Plain Text", "default")) + "\",\npaste_word_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Paste from Word", "default")) + "\",\nselectall_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select All", "default")) + "\"\n},\npaste_dlg:{\ntext_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Use CTRL+V on your keyboard to paste the text into the window.", "default")) + "\",\ntext_linebreaks:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Keep linebreaks", "default")) + "\",\nword_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Use CTRL+V on your keyboard to paste the text into the window.", "default")) + "\"\n},\ntable:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Inserts a new table", "default")) + "\",\nrow_before_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert row before", "default")) + "\",\nrow_after_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert row after", "default")) + "\",\ndelete_row_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Delete row", "default")) + "\",\ncol_before_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert column before", "default")) + "\",\ncol_after_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert column after", "default")) + "\",\ndelete_col_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Remove column", "default")) + "\",\nsplit_cells_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Split merged table cells", "default")) + "\",\nmerge_cells_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Merge table cells", "default")) + "\",\nrow_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Table row properties", "default")) + "\",\ncell_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Table cell properties", "default")) + "\",\nprops_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Table properties", "default")) + "\",\npaste_row_before_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Paste table row before", "default")) + "\",\npaste_row_after_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Paste table row after", "default")) + "\",\ncut_row_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cut table row", "default")) + "\",\ncopy_row_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Copy table row", "default")) + "\",\ndel:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Delete table", "default")) + "\",\nrow:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Row", "default")) +
            "\",\ncol:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Column", "default")) + "\",\ncell:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cell", "default")) + "\"\n},\nautosave:{\nunload_msg:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("The changes you made will be lost if you navigate away from this page.", "default")) + "\"\n},\nfullscreen:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Toggle fullscreen mode", "default")) + " (Alt+Shift+G)\"\n},\nmedia:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert / edit embedded media", "default")) + "\",\ndelta_width:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra width for the media popup in pixels", "default")) + "\",\ndelta_height:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra height for the media popup in pixels", "default")) + "\",\nedit:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Edit embedded media", "default")) + "\"\n},\nfullpage:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Document properties", "default")) + "\"\n},\ntemplate:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert predefined template content", "default")) + "\"\n},\nvisualchars:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Visual control characters on/off.", "default")) + "\"\n},\nspellchecker:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Toggle spellchecker", "default")) + " (Alt+Shift+N)\",\nmenu:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Spellchecker settings", "default")) + "\",\nignore_word:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Ignore word", "default")) + "\",\nignore_words:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Ignore all", "default")) + "\",\nlangs:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Languages", "default")) +
            "\",\nwait:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Please wait...", "default")) + "\",\nsug:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Suggestions", "default")) + "\",\nno_sug:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("No suggestions", "default")) + "\",\nno_mpell:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("No misspellings found.", "default")) + "\"\n},\npagebreak:{\ndesc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert page break.", "default")) + "\"\n}}});\n\ntinyMCE.addI18n(\"" + gVars.language + ".advanced\",{\nstyle_select:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Styles", "default")) + "\",\nfont_size:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Font size", "default")) +
            "\",\nfontdefault:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Font family", "default")) + "\",\nblock:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Format", "default")) + "\",\nparagraph:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Paragraph", "default")) +
            "\",\ndiv:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Div", "default")) + "\",\naddress:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Address", "default")) + "\",\npre:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Preformatted", "default")) +
            "\",\nh1:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Heading 1", "default")) + "\",\nh2:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Heading 2", "default")) + "\",\nh3:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Heading 3", "default")) +
            "\",\nh4:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Heading 4", "default")) + "\",\nh5:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Heading 5", "default")) + "\",\nh6:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Heading 6", "default")) +
            "\",\nblockquote:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Blockquote", "default")) + "\",\ncode:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Code", "default")) + "\",\nsamp:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Code sample", "default")) +
            "\",\ndt:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Definition term ", "default")) + "\",\ndd:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Definition description", "default")) + "\",\nbold_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Bold", "default")) + " (Ctrl / Alt+Shift + B)\",\nitalic_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Italic", "default")) + " (Ctrl / Alt+Shift + I)\",\nunderline_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Underline", "default")) + "\",\nstriketrough_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Strikethrough", "default")) + " (Alt+Shift+D)\",\njustifyleft_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Align left", "default")) + " (Alt+Shift+L)\",\njustifycenter_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Align center", "default")) + " (Alt+Shift+C)\",\njustifyright_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Align right", "default")) + " (Alt+Shift+R)\",\njustifyfull_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Align full", "default")) + " (Alt+Shift+J)\",\nbullist_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Unordered list", "default")) + " (Alt+Shift+U)\",\nnumlist_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Ordered list", "default")) + " (Alt+Shift+O)\",\noutdent_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Outdent", "default")) + "\",\nindent_desc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Indent", "default")) +
            "\",\nundo_desc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Undo", "default")) + " (Ctrl+Z)\",\nredo_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Redo", "default")) + " (Ctrl+Y)\",\nlink_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit link", "default")) + " (Alt+Shift+A)\",\nlink_delta_width:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra width for the link popup in pixels", "default")) + "\",\nlink_delta_height:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra height for the link popup in pixels", "default")) + "\",\nunlink_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Unlink", "default")) + " (Alt+Shift+S)\",\nimage_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit image", "default")) + " (Alt+Shift+M)\",\nimage_delta_width:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra width for the image popup in pixels", "default")) + "\",\nimage_delta_height:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra height for the image popup in pixels", "default")) + "\",\ncleanup_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cleanup messy code", "default")) + "\",\ncode_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Edit HTML Source", "default")) + "\",\nsub_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Subscript", "default")) + "\",\nsup_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Superscript", "default")) + "\",\nhr_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert horizontal ruler", "default")) + "\",\nremoveformat_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Remove formatting", "default")) + "\",\nforecolor_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select text color", "default")) + "\",\nbackcolor_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select background color", "default")) + "\",\ncharmap_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert custom character", "default")) + "\",\nvisualaid_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Toggle guidelines/invisible elements", "default")) + "\",\nanchor_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit anchor", "default")) + "\",\ncut_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cut", "default")) + "\",\ncopy_desc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Copy", "default")) +
            "\",\npaste_desc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Paste", "default")) + "\",\nimage_props_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Image properties", "default")) + "\",\nnewdocument_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("New document", "default")) + "\",\nhelp_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Help", "default")) + "\",\nblockquote_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Blockquote", "default")) + " (Alt+Shift+Q)\",\nclipboard_msg:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Copy/Cut/Paste is not available in Mozilla and Firefox.", "default")) + "\",\npath:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Path", "default")) + "\",\nnewdocument:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Are you sure you want to clear all contents?", "default")) + "\",\ntoolbar_focus:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Jump to tool buttons - Alt+Q, Jump to editor - Alt-Z, Jump to element path - Alt-X", "default")) + "\",\nmore_colors:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("More colors", "default")) + "\",\ncolorpicker_delta_width:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra width for the colorpicker popup in pixels", "default")) + "\",\ncolorpicker_delta_height:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts)._c("0| Extra height for the colorpicker popup in pixels", "default")) + "\"\n});\n\ntinyMCE.addI18n(\"" + gVars.language +
            ".advanced_dlg\",{\nabout_title:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("About TinyMCE", "default")) + "\",\nabout_general:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("About", "default")) + "\",\nabout_help:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Help", "default")) +
            "\",\nabout_license:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("License", "default")) + "\",\nabout_plugins:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Plugins", "default")) + "\",\nabout_plugin:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Plugin", "default")) + "\",\nabout_author:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Author", "default")) +
            "\",\nabout_version:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Version", "default")) + "\",\nabout_loaded:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Loaded plugins", "default")) + "\",\nanchor_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit anchor", "default")) + "\",\nanchor_name:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Anchor name", "default")) + "\",\ncode_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("HTML Source Editor", "default")) + "\",\ncode_wordwrap:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Word wrap", "default")) + "\",\ncolorpicker_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select a color", "default")) + "\",\ncolorpicker_picker_tab:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Picker", "default")) + "\",\ncolorpicker_picker_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Color picker", "default")) + "\",\ncolorpicker_palette_tab:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Palette", "default")) + "\",\ncolorpicker_palette_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Palette colors", "default")) + "\",\ncolorpicker_named_tab:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Named", "default")) + "\",\ncolorpicker_named_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Named colors", "default")) + "\",\ncolorpicker_color:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Color:", "default")) + "\",\ncolorpicker_name:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Name:", "default")) + "\",\ncharmap_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Select custom character", "default")) + "\",\nimage_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit image", "default")) + "\",\nimage_src:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Image URL", "default")) + "\",\nimage_alt:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Image description", "default")) + "\",\nimage_list:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Image list", "default")) + "\",\nimage_border:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Border", "default")) + "\",\nimage_dimensions:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Dimensions", "default")) + "\",\nimage_vspace:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Vertical space", "default")) + "\",\nimage_hspace:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Horizontal space", "default")) + "\",\nimage_align:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Alignment", "default")) + "\",\nimage_align_baseline:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Baseline", "default")) + "\",\nimage_align_top:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Top", "default")) + "\",\nimage_align_middle:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Middle", "default")) + "\",\nimage_align_bottom:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Bottom", "default")) + "\",\nimage_align_texttop:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Text top", "default")) + "\",\nimage_align_textbottom:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Text bottom", "default")) + "\",\nimage_align_left:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Left", "default")) + "\",\nimage_align_right:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Right", "default")) + "\",\nlink_title:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert/edit link", "default")) + "\",\nlink_url:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Link URL", "default")) + "\",\nlink_target:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Target", "default")) + "\",\nlink_target_same:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Open link in the same window", "default")) + "\",\nlink_target_blank:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Open link in a new window", "default")) + "\",\nlink_titlefield:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default")) + "\",\nlink_is_email:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("The URL you entered seems to be an email address, do you want to add the required mailto: prefix?", "default")) +
            "\",\nlink_is_external:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("The URL you entered seems to external link, do you want to add the required http:// prefix?", "default")) + "\",\nlink_list:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Link list", "default")) + "\"\n});\n\ntinyMCE.addI18n(\"" + gVars.language + ".media_dlg\",{\ntitle:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert / edit embedded media", "default")) + "\",\ngeneral:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("General", "default")) + "\",\nadvanced:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Advanced", "default")) +
            "\",\nfile:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("File/URL", "default")) + "\",\nlist:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("List", "default")) + "\",\nsize:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Dimensions", "default")) +
            "\",\npreview:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Preview", "default")) + "\",\nconstrain_proportions:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Constrain proportions", "default")) + "\",\ntype:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Type", "default")) + "\",\nid:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Id", "default")) +
            "\",\nname:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Name", "default")) + "\",\nclass_name:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Class", "default")) + "\",\nvspace:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("V-Space", "default")) +
            "\",\nhspace:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("H-Space", "default")) + "\",\nplay:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Auto play", "default")) + "\",\nloop:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Loop", "default")) +
            "\",\nmenu:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Show menu", "default")) + "\",\nquality:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Quality", "default")) + "\",\nscale:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Scale", "default")) +
            "\",\nalign:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Align", "default")) + "\",\nsalign:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("SAlign", "default")) + "\",\nwmode:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("WMode", "default")) +
            "\",\nbgcolor:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Background", "default")) + "\",\nbase:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Base", "default")) + "\",\nflashvars:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Flashvars", "default")) +
            "\",\nliveconnect:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("SWLiveConnect", "default")) + "\",\nautohref:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("AutoHREF", "default")) + "\",\ncache:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Cache", "default")) +
            "\",\nhidden:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Hidden", "default")) + "\",\ncontroller:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Controller", "default")) + "\",\nkioskmode:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Kiosk mode", "default")) + "\",\nplayeveryframe:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Play every frame", "default")) + "\",\ntargetcache:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Target cache", "default")) + "\",\ncorrection:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("No correction", "default")) + "\",\nenablejavascript:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Enable JavaScript", "default")) + "\",\nstarttime:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Start time", "default")) + "\",\nendtime:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("End time", "default")) + "\",\nhref:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Href", "default")) +
            "\",\nqtsrcchokespeed:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Choke speed", "default")) + "\",\ntarget:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Target", "default")) + "\",\nvolume:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Volume", "default")) +
            "\",\nautostart:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Auto start", "default")) + "\",\nenabled:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Enabled", "default")) + "\",\nfullscreen:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Fullscreen", "default")) + "\",\ninvokeurls:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Invoke URLs", "default")) + "\",\nmute:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Mute", "default")) +
            "\",\nstretchtofit:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Stretch to fit", "default")) + "\",\nwindowlessvideo:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Windowless video", "default")) + "\",\nbalance:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Balance", "default")) + "\",\nbaseurl:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Base URL", "default")) +
            "\",\ncaptioningid:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Captioning id", "default")) + "\",\ncurrentmarker:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Current marker", "default")) + "\",\ncurrentposition:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Current position", "default")) + "\",\ndefaultframe:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Default frame", "default")) + "\",\nplaycount:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Play count", "default")) + "\",\nrate:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Rate", "default")) +
            "\",\nuimode:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("UI Mode", "default")) + "\",\nflash_options:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Flash options", "default")) + "\",\nqt_options:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Quicktime options", "default")) + "\",\nwmp_options:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Windows media player options", "default")) + "\",\nrmp_options:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Real media player options", "default")) + "\",\nshockwave_options:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Shockwave options", "default")) + "\",\nautogotourl:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Auto goto URL", "default")) + "\",\ncenter:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Center", "default")) + "\",\nimagestatus:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Image status", "default")) + "\",\nmaintainaspect:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Maintain aspect", "default")) + "\",\nnojava:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("No java", "default")) + "\",\nprefetch:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Prefetch", "default")) +
            "\",\nshuffle:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Shuffle", "default")) + "\",\nconsole:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Console", "default")) + "\",\nnumloop:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Num loops", "default")) +
            "\",\ncontrols:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Controls", "default")) + "\",\nscriptcallbacks:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Script callbacks", "default")) + "\",\nswstretchstyle:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Stretch style", "default")) + "\",\nswstretchhalign:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Stretch H-Align", "default")) + "\",\nswstretchvalign:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Stretch V-Align", "default")) + "\",\nsound:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Sound", "default")) + "\",\nprogress:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Progress", "default")) +
            "\",\nqtsrc:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("QT Src", "default")) + "\",\nqt_stream_warn:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Streamed rtsp resources should be added to the QT Src field under the advanced tab.", "default")) + "\",\nalign_top:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Top", "default")) + "\",\nalign_right:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Right", "default")) +
            "\",\nalign_bottom:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Bottom", "default")) + "\",\nalign_left:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Left", "default")) + "\",\nalign_center:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Center", "default")) +
            "\",\nalign_top_left:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Top left", "default")) + "\",\nalign_top_right:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Top right", "default")) + "\",\nalign_bottom_left:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Bottom left", "default")) + "\",\nalign_bottom_right:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Bottom right", "default")) + "\",\nflv_options:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Flash video options", "default")) + "\",\nflv_scalemode:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Scale mode", "default")) + "\",\nflv_buffer:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Buffer", "default")) + "\",\nflv_startimage:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Start image", "default")) + "\",\nflv_starttime:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Start time", "default")) + "\",\nflv_defaultvolume:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Default volume", "default")) + "\",\nflv_hiddengui:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Hidden GUI", "default")) + "\",\nflv_autostart:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Auto start", "default")) + "\",\nflv_loop:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Loop", "default")) +
            "\",\nflv_showscalemodes:\"" + mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Show scale modes", "default")) + "\",\nflv_smoothvideo:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Smooth video", "default")) + "\",\nflv_jscallback:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("JS Callback", "default")) + "\"\n});\n\ntinyMCE.addI18n(\"" + gVars.language + ".wordpress\",{\nwp_adv_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Show/Hide Kitchen Sink", "default")) + " (Alt+Shift+Z)\",\nwp_more_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert More tag", "default")) + " (Alt+Shift+T)\",\nwp_page_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert Page break", "default")) + " (Alt+Shift+P)\",\nwp_help_desc:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Help", "default")) + " (Alt+Shift+H)\",\nwp_more_alt:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("More...", "default")) + "\",\nwp_page_alt:\"" +
            mce_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Next page...", "default")) + "\"\n});\n";

        return DEFAULT_VAL;
    }
}
