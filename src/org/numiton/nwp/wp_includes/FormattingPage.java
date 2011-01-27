/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FormattingPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_includes;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QMultibyte;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class FormattingPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FormattingPage.class.getName());
    public Array<Object> wp_cockneyreplace;

    @Override
    @RequestMapping("/wp-includes/formatting.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/formatting";
    }

    public String wptexturize(String text) {
        boolean next = false;
        String output = null;
        String curl = null;
        Array<Object> textarr = new Array<Object>();
        int stop = 0;
        Array<Object> cockney = new Array<Object>();
        Array<Object> cockneyreplace = new Array<Object>();
        Array<String> static_characters;
        Array<String> static_replacements;
        Array<String> dynamic_characters;
        Array<String> dynamic_replacements;
        int i = 0;
        
        next = true;
        output = "";
        curl = "";
        textarr = QRegExPerl.preg_split("/(<.*>|\\[.*\\])/Us", text, -1, RegExPerl.PREG_SPLIT_DELIM_CAPTURE);
        stop = Array.count(textarr);

    	// if a plugin has provided an autocorrect array, use it
        if (isset(wp_cockneyreplace)) {
            cockney = Array.array_keys(wp_cockneyreplace);
            cockneyreplace = Array.array_values(wp_cockneyreplace);
        } else {
            cockney = new Array<Object>(
                    new ArrayEntry<Object>("\'tain\'t"),
                    new ArrayEntry<Object>("\'twere"),
                    new ArrayEntry<Object>("\'twas"),
                    new ArrayEntry<Object>("\'tis"),
                    new ArrayEntry<Object>("\'twill"),
                    new ArrayEntry<Object>("\'til"),
                    new ArrayEntry<Object>("\'bout"),
                    new ArrayEntry<Object>("\'nuff"),
                    new ArrayEntry<Object>("\'round"),
                    new ArrayEntry<Object>("\'cause"));
            cockneyreplace = new Array<Object>(
                    new ArrayEntry<Object>("&#8217;tain&#8217;t"),
                    new ArrayEntry<Object>("&#8217;twere"),
                    new ArrayEntry<Object>("&#8217;twas"),
                    new ArrayEntry<Object>("&#8217;tis"),
                    new ArrayEntry<Object>("&#8217;twill"),
                    new ArrayEntry<Object>("&#8217;til"),
                    new ArrayEntry<Object>("&#8217;bout"),
                    new ArrayEntry<Object>("&#8217;nuff"),
                    new ArrayEntry<Object>("&#8217;round"),
                    new ArrayEntry<Object>("&#8217;cause"));
        }

        static_characters = Array.array_merge(new Array<Object>(
                    new ArrayEntry<Object>("---"),
                    new ArrayEntry<Object>(" -- "),
                    new ArrayEntry<Object>("--"),
                    new ArrayEntry<Object>("xn&#8211;"),
                    new ArrayEntry<Object>("..."),
                    new ArrayEntry<Object>("``"),
                    new ArrayEntry<Object>("\'s"),
                    new ArrayEntry<Object>("\'\'"),
                    new ArrayEntry<Object>(" (tm)")), cockney);
        static_replacements = Array.array_merge(
                new Array<Object>(
                    new ArrayEntry<Object>("&#8212;"),
                    new ArrayEntry<Object>(" &#8212; "),
                    new ArrayEntry<Object>("&#8211;"),
                    new ArrayEntry<Object>("xn--"),
                    new ArrayEntry<Object>("&#8230;"),
                    new ArrayEntry<Object>("&#8220;"),
                    new ArrayEntry<Object>("&#8217;s"),
                    new ArrayEntry<Object>("&#8221;"),
                    new ArrayEntry<Object>(" &#8482;")),
                cockneyreplace);
        dynamic_characters = new Array<String>(
                new ArrayEntry<String>("/\'(\\d\\d(?:&#8217;|\')?s)/"),
                new ArrayEntry<String>("/(\\s|\\A|\")\'/"),
                new ArrayEntry<String>("/(\\d+)\"/"),
                new ArrayEntry<String>("/(\\d+)\'/"),
                new ArrayEntry<String>("/(\\S)\'([^\'\\s])/"),
                new ArrayEntry<String>("/(\\s|\\A)\"(?!\\s)/"),
                new ArrayEntry<String>("/\"(\\s|\\S|\\Z)/"),
                new ArrayEntry<String>("/\'([\\s.]|\\Z)/"),
                new ArrayEntry<String>("/(\\d+)x(\\d+)/"));
        dynamic_replacements = new Array<String>(
                new ArrayEntry<String>("&#8217;$1"),
                new ArrayEntry<String>("$1&#8216;"),
                new ArrayEntry<String>("$1&#8243;"),
                new ArrayEntry<String>("$1&#8242;"),
                new ArrayEntry<String>("$1&#8217;$2"),
                new ArrayEntry<String>("$1&#8220;$2"),
                new ArrayEntry<String>("&#8221;$1"),
                new ArrayEntry<String>("&#8217;$1"),
                new ArrayEntry<String>("$1&#215;$2"));

        for (i = 0; i < stop; i++) {
            curl = strval(textarr.getValue(i));

            if ( /*Modified by Numiton */
                (Strings.strlen(curl) > 0) && !equal("<", Strings.getCharAt(curl, 0)) && !equal("[", Strings.getCharAt(curl, 0)) && next) { // If it's not a tag
            	// static strings
                curl = Strings.str_replace(static_characters, static_replacements, curl);
                // regular expressions
                curl = QRegExPerl.preg_replace(dynamic_characters, dynamic_replacements, curl);
            } else if (!strictEqual(Strings.strpos(curl, "<code"), BOOLEAN_FALSE) || !strictEqual(Strings.strpos(curl, "<pre"), BOOLEAN_FALSE) ||
                    !strictEqual(Strings.strpos(curl, "<kbd"), BOOLEAN_FALSE) || !strictEqual(Strings.strpos(curl, "<style"), BOOLEAN_FALSE) ||
                    !strictEqual(Strings.strpos(curl, "<script"), BOOLEAN_FALSE)) {
                next = false;
            } else {
                next = true;
            }

            curl = QRegExPerl.preg_replace("/&([^#])(?![a-zA-Z1-4]{1,8};)/", "&#038;$1", curl);
            output = output + curl;
        }

        return output;
    }

    /**
     * Added by Numiton. Used by preg_replace_callback.
     * @param matches
     * @return
     */
    public String clean_pre(Array matches) {
        return clean_pre((Object) matches);
    }

 // Accepts matches array from preg_replace_callback in wpautop()
 // or a string
    public String clean_pre(Object matches)/* Do not change type */
     {
        String text = null;

        if (is_array(matches)) {
            text = strval(((Array) matches).getValue(1)) + strval(((Array) matches).getValue(2)) + "</pre>";
        } else {
            text = strval(matches);
        }

        text = Strings.str_replace("<br />", "", text);
        text = Strings.str_replace("<p>", "\n", text);
        text = Strings.str_replace("</p>", "", text);

        return text;
    }

    public String wpautop(String pee) {
        return wpautop(pee, 1);
    }

    public String wpautop(String pee, int br) {
        String allblocks = null;
        pee = pee + "\n"; // just to make things a little easier, pad the end
        pee = QRegExPerl.preg_replace("|<br />\\s*<br />|", "\n\n", pee);
    	// Space things out a little
        allblocks = "(?:table|thead|tfoot|caption|colgroup|tbody|tr|td|th|div|dl|dd|dt|ul|ol|li|pre|select|form|map|area|blockquote|address|math|style|input|p|h[1-6]|hr)";
        pee = QRegExPerl.preg_replace("!(<" + allblocks + "[^>]*>)!", "\n$1", pee);
        pee = QRegExPerl.preg_replace("!(</" + allblocks + ">)!", "$1\n\n", pee);
        pee = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("\r\n"), new ArrayEntry<Object>("\r")), "\n", pee); // cross-platform newlines

        if (!strictEqual(Strings.strpos(pee, "<object"), BOOLEAN_FALSE)) {
            pee = QRegExPerl.preg_replace("|\\s*<param([^>]*)>\\s*|", "<param$1>", pee); // no pee inside object/embed
            pee = QRegExPerl.preg_replace("|\\s*</embed>\\s*|", "</embed>", pee);
        }

        pee = QRegExPerl.preg_replace("/\n\n+/", "\n\n", pee); // take care of duplicates
        pee = QRegExPerl.preg_replace("/\\n?(.+?)(?:\\n\\s*\\n|\\z)/s", "<p>$1</p>\n", pee); // make paragraphs, including one at the end
        pee = QRegExPerl.preg_replace("|<p>\\s*?</p>|", "", pee); // under certain strange conditions it could create a P of entirely whitespace
        pee = QRegExPerl.preg_replace("!<p>([^<]+)\\s*?(</(?:div|address|form)[^>]*>)!", "<p>$1</p>$2", pee);
        pee = QRegExPerl.preg_replace("|<p>|", "$1<p>", pee);
        pee = QRegExPerl.preg_replace("!<p>\\s*(</?" + allblocks + "[^>]*>)\\s*</p>!", "$1", pee); // don't pee all over a tag
        pee = QRegExPerl.preg_replace("|<p>(<li.+?)</p>|", "$1", pee); // problem with nested lists
        pee = QRegExPerl.preg_replace("|<p><blockquote([^>]*)>|i", "<blockquote$1><p>", pee);
        pee = Strings.str_replace("</blockquote></p>", "</p></blockquote>", pee);
        pee = QRegExPerl.preg_replace("!<p>\\s*(</?" + allblocks + "[^>]*>)!", "$1", pee);
        pee = QRegExPerl.preg_replace("!(</?" + allblocks + "[^>]*>)\\s*</p>!", "$1", pee);

        if (booleanval(br)) {
            pee = RegExPerl.preg_replace_callback("/<(script|style).*?<\\/\\1>/s", new Callback("createFunction_replace", this), pee);
            pee = QRegExPerl.preg_replace("|(?<!<br />)\\s*\\n|", "<br />\n", pee); // optionally make line breaks
            pee = Strings.str_replace("<WPPreserveNewline />", "\n", pee);
        }

        pee = QRegExPerl.preg_replace("!(</?" + allblocks + "[^>]*>)\\s*<br />!", "$1", pee);
        pee = QRegExPerl.preg_replace("!<br />(\\s*</?(?:p|li|div|dl|dd|dt|th|pre|td|ul|ol)[^>]*>)!", "$1", pee);

        if (!strictEqual(Strings.strpos(pee, "<pre"), BOOLEAN_FALSE)) {
            pee = RegExPerl.preg_replace_callback("!(<pre.*?>)(.*?)</pre>!is", new Callback("clean_pre", this), pee);
        }

        pee = QRegExPerl.preg_replace("|\n</p>$|", "</p>", pee);
        pee = QRegExPerl.preg_replace("/<p>\\s*?(" + getIncluded(ShortcodesPage.class, gVars, gConsts).get_shortcode_regex() + ")\\s*<\\/p>/s", "$1", pee); // don't auto-p wrap shortcodes that stand alone

        return pee;
    }

    // Added by Numiton
    public String createFunction_replace(Array matches) {
        return Strings.str_replace("\n", "<WPPreserveNewline />", strval(matches.getValue(0)));
    }

    public boolean seems_utf8(String Str) { // by bmorel at ssi dot fr
        int length = 0;
        int i = 0;
        int n = 0;
        int j = 0;
        length = Strings.strlen(Str);

        for (i = 0; i < length; i++) {
            if (Strings.ord(Strings.getCharAt(Str, i)) < 128) {
                continue; // 0bbbbbbb
            } else if (equal(Strings.ord(Strings.getCharAt(Str, i)) & 224, 192)) {
                n = 1; // 110bbbbb
            } else if (equal(Strings.ord(Strings.getCharAt(Str, i)) & 240, 224)) {
                n = 2; // 1110bbbb
            } else if (equal(Strings.ord(Strings.getCharAt(Str, i)) & 248, 240)) {
                n = 3; // 11110bbb
            } else if (equal(Strings.ord(Strings.getCharAt(Str, i)) & 252, 248)) {
                n = 4; // 111110bb
            } else if (equal(Strings.ord(Strings.getCharAt(Str, i)) & 254, 252)) {
                n = 5; // 1111110b
            } else {
                return false; // Does not match any model
            }

            for (j = 0; j < n; j++) { //n bytes matching 10bbbbbb follow ?
                if (equal(++i, length) || !equal(Strings.ord(Strings.getCharAt(Str, i)) & 192, 128)) {
                    return false;
                }
            }
        }

        return true;
    }

    public String wp_specialchars(String text) {
        return wp_specialchars(text, null);
    }

    public String wp_specialchars(String text, String quotes) {
    	// Like htmlspecialchars except don't double-encode HTML entities
        text = Strings.str_replace("&&", "&#038;&", text);
        text = Strings.str_replace("&&", "&#038;&", text);
        text = QRegExPerl.preg_replace("/&(?:$|([^#])(?![a-z1-4]{1,8};))/", "&#038;$1", text);
        text = Strings.str_replace("<", "&lt;", text);
        text = Strings.str_replace(">", "&gt;", text);

        if (strictEqual("double", quotes)) {
            text = Strings.str_replace("\"", "&quot;", text);
        } else if (strictEqual("single", quotes)) {
            text = Strings.str_replace("\'", "&#039;", text);
        } else if (booleanval(quotes)) {
            text = Strings.str_replace("\"", "&quot;", text);
            text = Strings.str_replace("\'", "&#039;", text);
        }

        return text;
    }

    public Array wp_specialcharsArray(Array text, String quotes) {
        text = Strings.str_replace("&&", "&#038;&", text);
        text = Strings.str_replace("&&", "&#038;&", text);
        text = QRegExPerl.preg_replace("/&(?:$|([^#])(?![a-z1-4]{1,8};))/", "&#038;$1", text);
        text = Strings.str_replace("<", "&lt;", text);
        text = Strings.str_replace(">", "&gt;", text);

        if (strictEqual("double", quotes)) {
            text = Strings.str_replace("\"", "&quot;", text);
        } else if (strictEqual("single", quotes)) {
            text = Strings.str_replace("\'", "&#039;", text);
        } else if (booleanval(quotes)) {
            text = Strings.str_replace("\"", "&quot;", text);
            text = Strings.str_replace("\'", "&#039;", text);
        }

        return text;
    }

    public String utf8_uri_encode(String utf8_string, int length) {
        String unicode = null;
        Array<Integer> values;
        int num_octets = 0;
        int unicode_length = 0;
        int string_length = 0;
        int value = 0;
        int i = 0;
        unicode = "";
        values = new Array<Integer>();
        num_octets = 1;
        unicode_length = 0;
        string_length = Strings.strlen(utf8_string);

        for (i = 0; i < string_length; i++) {
            value = Strings.ord(Strings.getCharAt(utf8_string, i));

            if (value < 128) {
                if (booleanval(length) && (unicode_length >= length)) {
                    break;
                }

                unicode = unicode + Strings.chr(value);
                unicode_length++;
            } else {
                if (equal(Array.count(values), 0)) {
                    num_octets = ((value < 224)
                        ? 2
                        : 3);
                }

                values.putValue(value);

                if (booleanval(length) && ((unicode_length + (num_octets * 3)) > length)) {
                    break;
                }

                if (equal(Array.count(values), num_octets)) {
                    if (equal(num_octets, 3)) {
                        unicode = unicode + "%" + Math.dechex(values.getValue(0)) + "%" + Math.dechex(values.getValue(1)) + "%" + Math.dechex(values.getValue(2));
                        unicode_length = unicode_length + 9;
                    } else {
                        unicode = unicode + "%" + Math.dechex(values.getValue(0)) + "%" + Math.dechex(values.getValue(1));
                        unicode_length = unicode_length + 6;
                    }

                    values = new Array<Integer>();
                    num_octets = 1;
                }
            }
        }

        return unicode;
    }

    @SuppressWarnings("unchecked")
    public String remove_accents(String string) {
        Array<Object> chars = new Array<Object>();
        Array<Object> double_chars = new Array<Object>();

        // TODO Check this
        if (!QRegExPerl.preg_match("/[\u0080-\u00ff]/", string)) {
            return string;
        }

        if (seems_utf8(string)) {
            chars = new Array(
            		// Decompositions for Latin-1 Supplement
                    new ArrayEntry(Strings.chr(195) + Strings.chr(128), "A"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(129), "A"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(130), "A"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(131), "A"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(132), "A"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(133), "A"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(135), "C"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(136), "E"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(137), "E"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(138), "E"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(139), "E"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(140), "I"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(141), "I"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(142), "I"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(143), "I"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(145), "N"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(146), "O"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(147), "O"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(148), "O"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(149), "O"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(150), "O"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(153), "U"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(154), "U"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(155), "U"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(156), "U"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(157), "Y"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(159), "s"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(160), "a"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(161), "a"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(162), "a"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(163), "a"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(164), "a"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(165), "a"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(167), "c"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(168), "e"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(169), "e"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(170), "e"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(171), "e"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(172), "i"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(173), "i"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(174), "i"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(175), "i"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(177), "n"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(178), "o"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(179), "o"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(180), "o"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(181), "o"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(182), "o"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(182), "o"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(185), "u"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(186), "u"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(187), "u"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(188), "u"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(189), "y"),
                    new ArrayEntry(Strings.chr(195) + Strings.chr(191), "y"),
                    // Decompositions for Latin Extended-A
                    new ArrayEntry(Strings.chr(196) + Strings.chr(128), "A"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(129), "a"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(130), "A"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(131), "a"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(132), "A"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(133), "a"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(134), "C"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(135), "c"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(136), "C"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(137), "c"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(138), "C"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(139), "c"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(140), "C"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(141), "c"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(142), "D"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(143), "d"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(144), "D"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(145), "d"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(146), "E"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(147), "e"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(148), "E"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(149), "e"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(150), "E"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(151), "e"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(152), "E"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(153), "e"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(154), "E"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(155), "e"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(156), "G"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(157), "g"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(158), "G"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(159), "g"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(160), "G"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(161), "g"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(162), "G"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(163), "g"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(164), "H"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(165), "h"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(166), "H"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(167), "h"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(168), "I"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(169), "i"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(170), "I"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(171), "i"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(172), "I"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(173), "i"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(174), "I"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(175), "i"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(176), "I"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(177), "i"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(178), "IJ"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(179), "ij"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(180), "J"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(181), "j"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(182), "K"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(183), "k"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(184), "k"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(185), "L"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(186), "l"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(187), "L"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(188), "l"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(189), "L"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(190), "l"),
                    new ArrayEntry(Strings.chr(196) + Strings.chr(191), "L"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(128), "l"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(129), "L"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(130), "l"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(131), "N"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(132), "n"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(133), "N"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(134), "n"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(135), "N"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(136), "n"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(137), "N"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(138), "n"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(139), "N"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(140), "O"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(141), "o"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(142), "O"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(143), "o"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(144), "O"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(145), "o"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(146), "OE"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(147), "oe"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(148), "R"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(149), "r"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(150), "R"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(151), "r"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(152), "R"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(153), "r"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(154), "S"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(155), "s"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(156), "S"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(157), "s"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(158), "S"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(159), "s"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(160), "S"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(161), "s"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(162), "T"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(163), "t"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(164), "T"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(165), "t"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(166), "T"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(167), "t"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(168), "U"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(169), "u"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(170), "U"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(171), "u"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(172), "U"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(173), "u"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(174), "U"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(175), "u"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(176), "U"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(177), "u"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(178), "U"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(179), "u"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(180), "W"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(181), "w"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(182), "Y"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(183), "y"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(184), "Y"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(185), "Z"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(186), "z"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(187), "Z"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(188), "z"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(189), "Z"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(190), "z"),
                    new ArrayEntry(Strings.chr(197) + Strings.chr(191), "s"),
                    // Euro Sign
                    new ArrayEntry(Strings.chr(226) + Strings.chr(130) + Strings.chr(172), "E"),
                    // GBP (Pound) Sign
                    new ArrayEntry(Strings.chr(194) + Strings.chr(163), ""));
            string = Strings.strtr(string, chars);
        } else {
    		// Assume ISO-8859-1 if not UTF-8
            chars.putValue(
                    "in",
                    Strings.chr(128) + Strings.chr(131) + Strings.chr(138) + Strings.chr(142) + Strings.chr(154) + Strings.chr(158) + Strings.chr(159) + Strings.chr(162) + Strings.chr(165) +
                    Strings.chr(181) + Strings.chr(192) + Strings.chr(193) + Strings.chr(194) + Strings.chr(195) + Strings.chr(196) + Strings.chr(197) + Strings.chr(199) + Strings.chr(200) +
                    Strings.chr(201) + Strings.chr(202) + Strings.chr(203) + Strings.chr(204) + Strings.chr(205) + Strings.chr(206) + Strings.chr(207) + Strings.chr(209) + Strings.chr(210) +
                    Strings.chr(211) + Strings.chr(212) + Strings.chr(213) + Strings.chr(214) + Strings.chr(216) + Strings.chr(217) + Strings.chr(218) + Strings.chr(219) + Strings.chr(220) +
                    Strings.chr(221) + Strings.chr(224) + Strings.chr(225) + Strings.chr(226) + Strings.chr(227) + Strings.chr(228) + Strings.chr(229) + Strings.chr(231) + Strings.chr(232) +
                    Strings.chr(233) + Strings.chr(234) + Strings.chr(235) + Strings.chr(236) + Strings.chr(237) + Strings.chr(238) + Strings.chr(239) + Strings.chr(241) + Strings.chr(242) +
                    Strings.chr(243) + Strings.chr(244) + Strings.chr(245) + Strings.chr(246) + Strings.chr(248) + Strings.chr(249) + Strings.chr(250) + Strings.chr(251) + Strings.chr(252) +
                    Strings.chr(253) + Strings.chr(255));
            
            chars.putValue("out", "EfSZszYcYuAAAAAACEEEEIIIINOOOOOOUUUUYaaaaaaceeeeiiiinoooooouuuuyy");
            
            string = Strings.strtr(string, strval(chars.getValue("in")), strval(chars.getValue("out")));
            double_chars.putValue("in",
                new Array<Object>(new ArrayEntry<Object>(Strings.chr(140)),
                    new ArrayEntry<Object>(Strings.chr(156)),
                    new ArrayEntry<Object>(Strings.chr(198)),
                    new ArrayEntry<Object>(Strings.chr(208)),
                    new ArrayEntry<Object>(Strings.chr(222)),
                    new ArrayEntry<Object>(Strings.chr(223)),
                    new ArrayEntry<Object>(Strings.chr(230)),
                    new ArrayEntry<Object>(Strings.chr(240)),
                    new ArrayEntry<Object>(Strings.chr(254))));
            double_chars.putValue("out",
                new Array<Object>(new ArrayEntry<Object>("OE"),
                    new ArrayEntry<Object>("oe"),
                    new ArrayEntry<Object>("AE"),
                    new ArrayEntry<Object>("DH"),
                    new ArrayEntry<Object>("TH"),
                    new ArrayEntry<Object>("ss"),
                    new ArrayEntry<Object>("ae"),
                    new ArrayEntry<Object>("dh"),
                    new ArrayEntry<Object>("th")));
            string = Strings.str_replace(double_chars.getArrayValue("in"), double_chars.getArrayValue("out"), string);
        }

        return string;
    }

    public String sanitize_file_name(String name) { // Like sanitize_title, but with periods
        name = Strings.strtolower(name);
        name = QRegExPerl.preg_replace("/&.+?;/", "", name); // kill entities
        name = Strings.str_replace("_", "-", name);
        name = QRegExPerl.preg_replace("/[^a-z0-9\\s-.]/", "", name);
        name = QRegExPerl.preg_replace("/\\s+/", "-", name);
        name = QRegExPerl.preg_replace("|-+|", "-", name);
        name = Strings.trim(name, "-");

        return name;
    }

    public String sanitize_user(String username) {
        return sanitize_user(username, false);
    }

    public String sanitize_user(String username, boolean strict) {
        String raw_username = null;
        raw_username = username;
        username = Strings.strip_tags(username);
    	// Kill octets
        username = QRegExPerl.preg_replace("|%([a-fA-F0-9][a-fA-F0-9])|", "", username);
        username = QRegExPerl.preg_replace("/&.+?;/", "", username); // Kill entities

    	// If strict, reduce to ASCII for max portability.
        if (strict) {
            username = QRegExPerl.preg_replace("|[^a-z0-9 _.\\-@]|i", "", username);
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("sanitize_user", username, raw_username, strict));
    }

    public String sanitize_title(String title) {
        return sanitize_title(title, "");
    }

    public String sanitize_title(String title, String fallback_title) {
        title = Strings.strip_tags(title);
        title = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("sanitize_title", title));

        if (strictEqual("", title) || strictEqual(null, title)) {
            title = fallback_title;
        }

        return title;
    }

    public String sanitize_title_with_dashes(String title) {
        title = Strings.strip_tags(title);
    	// Preserve escaped octets.
        title = QRegExPerl.preg_replace("|%([a-fA-F0-9][a-fA-F0-9])|", "---$1---", title);
    	// Remove percent signs that are not part of an octet.
        title = Strings.str_replace("%", "", title);
    	// Restore octets.
        title = QRegExPerl.preg_replace("|---([a-fA-F0-9][a-fA-F0-9])---|", "%$1", title);
        
        title = remove_accents(title);

        if (seems_utf8(title)) {
            if (true)/*Modified by Numiton*/
             {
                title = QMultibyte.mb_strtolower(gVars.webEnv, title, "UTF-8");
            }

            title = utf8_uri_encode(title, 200);
        }

        title = Strings.strtolower(title);
        title = QRegExPerl.preg_replace("/&.+?;/", "", title); // kill entities
        title = QRegExPerl.preg_replace("/[^%a-z0-9 _-]/", "", title);
        title = QRegExPerl.preg_replace("/\\s+/", "-", title);
        title = QRegExPerl.preg_replace("|-+|", "-", title);
        title = Strings.trim(title, "-");

        return title;
    }

 // ensures a string is a valid SQL order by clause like: post_name ASC, ID DESC
 // accepts one or more columns, with or without ASC/DESC, and also accepts RAND()
    public boolean sanitize_sql_orderby(String orderby) {
        Array obmatches = new Array();
        QRegExPerl.preg_match("/^\\s*([a-z0-9_]+(\\s+(ASC|DESC))?(\\s*,\\s*|\\s*$))+|^\\s*RAND\\(\\s*\\)\\s*$/i", orderby, obmatches);

        if (!booleanval(obmatches)) {
            return false;
        }

        return booleanval(orderby);
    }

    public String convert_chars(String content) {
        return convert_chars(content, "");
    }

    @SuppressWarnings("unchecked")
    public String convert_chars(String content, String deprecated) {
    	// Translation of invalid Unicode references range to valid range
        Array wp_htmltranswinuni = new Array(
                new ArrayEntry("&#128;", "&#8364;"), // the Euro sign
                new ArrayEntry("&#129;", ""),
                new ArrayEntry("&#130;", "&#8218;"), // these are Windows CP1252 specific characters
                new ArrayEntry("&#131;", "&#402;"),  // they would look weird on non-Windows browsers
                new ArrayEntry("&#132;", "&#8222;"),
                new ArrayEntry("&#133;", "&#8230;"),
                new ArrayEntry("&#134;", "&#8224;"),
                new ArrayEntry("&#135;", "&#8225;"),
                new ArrayEntry("&#136;", "&#710;"),
                new ArrayEntry("&#137;", "&#8240;"),
                new ArrayEntry("&#138;", "&#352;"),
                new ArrayEntry("&#139;", "&#8249;"),
                new ArrayEntry("&#140;", "&#338;"),
                new ArrayEntry("&#141;", ""),
                new ArrayEntry("&#142;", "&#382;"),
                new ArrayEntry("&#143;", ""),
                new ArrayEntry("&#144;", ""),
                new ArrayEntry("&#145;", "&#8216;"),
                new ArrayEntry("&#146;", "&#8217;"),
                new ArrayEntry("&#147;", "&#8220;"),
                new ArrayEntry("&#148;", "&#8221;"),
                new ArrayEntry("&#149;", "&#8226;"),
                new ArrayEntry("&#150;", "&#8211;"),
                new ArrayEntry("&#151;", "&#8212;"),
                new ArrayEntry("&#152;", "&#732;"),
                new ArrayEntry("&#153;", "&#8482;"),
                new ArrayEntry("&#154;", "&#353;"),
                new ArrayEntry("&#155;", "&#8250;"),
                new ArrayEntry("&#156;", "&#339;"),
                new ArrayEntry("&#157;", ""),
                new ArrayEntry("&#158;", ""),
                new ArrayEntry("&#159;", "&#376;"));
        
    	// Remove metadata tags
        content = QRegExPerl.preg_replace("/<title>(.+?)<\\/title>/", "", content);
        content = QRegExPerl.preg_replace("/<category>(.+?)<\\/category>/", "", content);
        
    	// Converts lone & characters into &#38; (a.k.a. &amp;)
        content = QRegExPerl.preg_replace("/&([^#])(?![a-z1-4]{1,8};)/i", "&#038;$1", content);
        
    	// Fix Word pasting
        content = Strings.strtr(content, wp_htmltranswinuni);
        
    	// Just a little XHTML help
        content = Strings.str_replace("<br>", "<br />", content);
        content = Strings.str_replace("<hr>", "<hr />", content);

        return content;
    }

    public String funky_javascript_fix(String text) {
    	// Fixes for browsers' javascript bugs
        if (gVars.is_winIE || gVars.is_macIE) {
            // Modified by Numiton
            text = RegExPerl.preg_replace_callback("/\\%u([0-9A-F]{4,4})/", new Callback("replaceBaseConvert", CallbackUtils.class), text);
        }

        return text;
    }

    public String balanceTags(String text) {
        return balanceTags(text, false);
    }

    public String balanceTags(String text, boolean force) {
        if (!force && equal(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("use_balanceTags"), 0)) {
            return text;
        }

        return force_balance_tags(text);
    }

    /*
    force_balance_tags

    Balances Tags of string using a modified stack.

    @param text      Text to be balanced
    @param force     Forces balancing, ignoring the value of the option
    @return          Returns balanced text
    @author          Leonard Lin (leonard@acm.org)
    @version         v1.1
    @date            November 4, 2001
    @license         GPL v2.0
    @notes
    @changelog
    ---  Modified by Scott Reilly (coffee2code) 02 Aug 2004
   	1.2  ***TODO*** Make better - change loop condition to $text
   	1.1  Fixed handling of append/stack pop order of end text
   	     Added Cleaning Hooks
   	1.0  First Version
   */
    public String force_balance_tags(String text) {
        Array<Object> tagstack = new Array<Object>();
        int stacksize = 0;
        String tagqueue = null;
        String newtext = null;
        Array<Object> single_tags = new Array<Object>();
        Array<Object> nestable_tags = new Array<Object>();
        int i = 0;
        Array<Object> regex = new Array<Object>();
        int l = 0;
        String tag = null;
        int j = 0;
        int k = 0;
        String attributes = null;
        Object x = null;
        
        tagstack = new Array<Object>();
        stacksize = 0;
        tagqueue = "";
        newtext = "";
        
        single_tags = new Array<Object>(new ArrayEntry<Object>("br"), new ArrayEntry<Object>("hr"), new ArrayEntry<Object>("img"), new ArrayEntry<Object>("input")); //Known single-entity/self-closing tags
        nestable_tags = new Array<Object>(new ArrayEntry<Object>("blockquote"), new ArrayEntry<Object>("div"), new ArrayEntry<Object>("span")); //Tags that can be immediately nested within themselves

        // WP bug fix for comments - in case you REALLY meant to type '< !--'
        text = Strings.str_replace("< !--", "<    !--", text);
        
        // WP bug fix for LOVE <3 (and other situations with '<' before a number)
        text = QRegExPerl.preg_replace("#<([0-9]{1})#", "&lt;$1", text);

        while (QRegExPerl.preg_match("/<(\\/?\\w*)\\s*([^>]*)>/", text, regex)) {
            newtext = newtext + tagqueue;
            i = Strings.strpos(text, strval(regex.getValue(0)));
            l = Strings.strlen(strval(regex.getValue(0)));
            
            // clear the shifter
            tagqueue = "";

            // Pop or Push
            if (equal(Strings.getCharAt(strval(regex.getValue(1)), 0), "/")) { // End Tag
                tag = Strings.strtolower(Strings.substr(strval(regex.getValue(1)), 1));

                // if too many closing tags
                if (stacksize <= 0) {
                    tag = "";
                  //or close to be safe $tag = '/' . $tag;
                } 
                // if stacktop value = tag close value then pop
                else if (equal(tagstack.getValue(stacksize - 1), tag)) { // found closing tag
                    tag = "</" + tag + ">"; // Close Tag
                    // Pop
                    Array.array_pop(tagstack);
                    stacksize--;
                } else { // closing tag not at top, search for it
                    for (j = stacksize - 1; j >= 0; j--) {
                        if (equal(tagstack.getValue(j), tag)) {
                        	// add tag to tagqueue
                            for (k = stacksize - 1; k >= j; k--) {
                                tagqueue = tagqueue + "</" + strval(Array.array_pop(tagstack)) + ">";
                                stacksize--;
                            }

                            break;
                        }
                    }

                    tag = "";
                }
            } else { // Begin Tag
                tag = Strings.strtolower(strval(regex.getValue(1)));

                // Tag Cleaning

    			// If self-closing or '', don't do anything.
                if (equal(Strings.substr(strval(regex.getValue(2)), -1), "/") || equal(tag, "")) {
                } 
                // ElseIf it's a known single-entity tag but it doesn't close itself, do so
                else if (Array.in_array(tag, single_tags)) {
                    regex.putValue(2, strval(regex.getValue(2)) + "/");
                } else { // Push the tag onto the stack
    				// If the top of the stack is the same as the tag we want to push, close previous tag
                    if ((stacksize > 0) && !Array.in_array(tag, nestable_tags) && equal(tagstack.getValue(stacksize - 1), tag)) {
                        tagqueue = "</" + strval(Array.array_pop(tagstack)) + ">";
                        stacksize--;
                    }

                    stacksize = Array.array_push(tagstack, tag);
                }

                // Attributes
                attributes = strval(regex.getValue(2));

                if (booleanval(attributes)) {
                    attributes = " " + attributes;
                }

                tag = "<" + tag + attributes + ">";

    			//If already queuing a close tag, then put this tag on, too
                if (booleanval(tagqueue)) {
                    tagqueue = tagqueue + tag;
                    tag = "";
                }
            }

            newtext = newtext + Strings.substr(text, 0, i) + tag;
            text = Strings.substr(text, i + l);
        }

    	// Clear Tag Queue
        newtext = newtext + tagqueue;
        
    	// Add Remaining text
        newtext = newtext + text;

    	// Empty Stack
        while (booleanval(x = Array.array_pop(tagstack))) {
            newtext = newtext + "</" + strval(x) + ">"; // Add remaining tags to close
        }

    	// WP fix for the bug with HTML comments
        newtext = Strings.str_replace("< !--", "<!--", newtext);
        newtext = Strings.str_replace("<    !--", "< !--", newtext);

        return newtext;
    }

    public String format_to_edit(String content, Object richedit) {
        content = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("format_to_edit", content));

        if (!booleanval(richedit)) {
            content = Strings.htmlspecialchars(content);
        }

        return content;
    }

    public Object format_to_post(Object content) {
        content = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("format_to_post", content);

        return content;
    }

    public String zeroise(String number, int threshold) {
        return zeroise(intval(number), threshold);
    }

    public String zeroise(int number, int threshold) { // function to add leading zeros when necessary
        return QStrings.sprintf("%0" + strval(threshold) + "s", number);
    }

    public String backslashit(String string) {
        string = QRegExPerl.preg_replace("/^([0-9])/", "\\\\\\\\\\1", string);
        string = QRegExPerl.preg_replace("/([a-z])/i", "\\\\\\1", string);

        return string;
    }

    public static String trailingslashit(String string) {
        return untrailingslashit(string) + "/";
    }

    public static String untrailingslashit(String string) {
        return Strings.rtrim(string, "/");
    }

    public String addslashes_gpc(String gpc) {
        if (booleanval(Options.get_magic_quotes_gpc(gVars.webEnv))) {
            gpc = Strings.stripslashes(gVars.webEnv, gpc);
        }

        return gVars.wpdb.escape(gpc);
    }

    public Object stripslashes_deep(Object value)/* Do not change type */
     {
        value = (is_array(value)
            ? Array.array_map(new Callback("stripslashes_deep", this), (Array) value)
            : Strings.stripslashes(gVars.webEnv, strval(value)));

        return value;
    }

    public Object urlencode_deep(Object value)/* Do not change type */
     {
        value = (is_array(value)
            ? Array.array_map(new Callback("urlencode_deep", this), (Array) value)
            : URL.urlencode(strval(value)));

        return value;
    }

    public String antispambot(String emailaddy) {
        return antispambot(emailaddy, 0);
    }

    public String antispambot(String emailaddy, int mailto) {
        String emailNOSPAMaddy = null;
        float j = 0;
        int i = 0;
        emailNOSPAMaddy = "";
        Math.srand(intval(floatval(DateTime.microtime()) * 1000000));

        for (i = 0; i < Strings.strlen(emailaddy); i = i + 1) {
            j = Math.floor(Math.rand(0, 1 + mailto));

            if (equal(j, 0)) {
                emailNOSPAMaddy = emailNOSPAMaddy + "&#" + strval(Strings.ord(Strings.substr(emailaddy, i, 1))) + ";";
            } else if (equal(j, 1)) {
                emailNOSPAMaddy = emailNOSPAMaddy + Strings.substr(emailaddy, i, 1);
            } else if (equal(j, 2)) {
                emailNOSPAMaddy = emailNOSPAMaddy + "%" + zeroise(Math.dechex(Strings.ord(Strings.substr(emailaddy, i, 1))), 2);
            }
        }

        emailNOSPAMaddy = Strings.str_replace("@", "&#64;", emailNOSPAMaddy);

        return emailNOSPAMaddy;
    }

    public String _make_url_clickable_cb(Array<Object> matches) {
        String ret = null;
        String url = null;
        ret = "";
        url = strval(matches.getValue(2));
        url = clean_url(url, null, "display");

        if (empty(url)) {
            return strval(matches.getValue(0));
        }

    	// removed trailing [.,;:] from URL
        if (strictEqual(
                    Array.in_array(Strings.substr(url, -1), new Array<Object>(new ArrayEntry<Object>("."), new ArrayEntry<Object>(","), new ArrayEntry<Object>(";"), new ArrayEntry<Object>(":"))),
                    true)) {
            ret = Strings.substr(url, -1);
            url = Strings.substr(url, 0, Strings.strlen(url) - 1);
        }

        return matches.getValue(1) + "<a href=\"" + url + "\" rel=\"nofollow\">" + url + "</a>" + ret;
    }

    public String _make_web_ftp_clickable_cb(Array<Object> matches) {
        String ret = null;
        String dest = null;
        ret = "";
        dest = strval(matches.getValue(2));
        dest = "http://" + dest;
        dest = clean_url(dest, null, "display");

        if (empty(dest)) {
            return strval(matches.getValue(0));
        }

    	// removed trailing [,;:] from URL
        if (strictEqual(
                    Array.in_array(Strings.substr(dest, -1), new Array<Object>(new ArrayEntry<Object>("."), new ArrayEntry<Object>(","), new ArrayEntry<Object>(";"), new ArrayEntry<Object>(":"))),
                    true)) {
            ret = Strings.substr(dest, -1);
            dest = Strings.substr(dest, 0, Strings.strlen(dest) - 1);
        }

        return matches.getValue(1) + "<a href=\"" + dest + "\" rel=\"nofollow\">" + dest + "</a>" + ret;
    }

    public String _make_email_clickable_cb(Array<Object> matches) {
        String email = null;
        email = strval(matches.getValue(2)) + "@" + strval(matches.getValue(3));

        return strval(matches.getValue(1)) + "<a href=\"mailto:" + email + "\">" + email + "</a>";
    }

    public String make_clickable(String ret) {
        ret = " " + ret;
    	// in testing, using arrays here was found to be faster
        ret = RegExPerl.preg_replace_callback("#([\\s>])([\\w]+?://[\\w\\#$%&~/.\\-;:=,?@\\[\\]+]*)#is", new Callback("_make_url_clickable_cb", this), ret);
        ret = RegExPerl.preg_replace_callback("#([\\s>])((www|ftp)\\.[\\w\\#$%&~/.\\-;:=,?@\\[\\]+]*)#is", new Callback("_make_web_ftp_clickable_cb", this), ret);
        ret = RegExPerl.preg_replace_callback("#([\\s>])([.0-9a-z_+-]+)@(([0-9a-z-]+\\.)+[0-9a-z]{2,})#i", new Callback("_make_email_clickable_cb", this), ret);
    	// this one is not in an array because we need it to run last, for cleanup of accidental links within links
        ret = QRegExPerl.preg_replace("#(<a( [^>]+?>|>))<a [^>]+?>([^>]+?)</a></a>#i", "$1$3</a>", ret);
        ret = Strings.trim(ret);

        return ret;
    }

    public String wp_rel_nofollow(String text) {
    	// This is a pre save filter, so text is already escaped.
        text = Strings.stripslashes(gVars.webEnv, text);
        text = RegExPerl.preg_replace_callback("|<a (.+?)>|i", new Callback("wp_rel_nofollow_callback", this), text);
        text = gVars.wpdb.escape(text);

        return text;
    }

    public String wp_rel_nofollow_callback(Array<Object> matches) {
        String text = null;
        text = strval(matches.getValue(1));
        text = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>(" rel=\"nofollow\""), new ArrayEntry<Object>(" rel=\'nofollow\'")), "", text);

        return "<a " + text + " rel=\"nofollow\">";
    }

    public String convert_smilies(String text) {
        String output = null;
        Array<String> textarr = new Array<String>();
        int stop = 0;
        String content;
        int i = 0;
        output = "";

        if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("use_smilies")) && !empty(gVars.wp_smiliessearch) && !empty(gVars.wp_smiliesreplace)) {
    		// HTML loop taken from texturize function, could possible be consolidated
            textarr = QRegExPerl.preg_split("/(<.*>)/U", text, -1, RegExPerl.PREG_SPLIT_DELIM_CAPTURE); // capture the tags as well as in between
            stop = Array.count(textarr);// loop stuff

            for (i = 0; i < stop; i++) {
                content = textarr.getValue(i);

                if ((Strings.strlen(content) > 0) && !equal("<", Strings.getCharAt(content, 0))) { // If it's not a tag
                    content = QRegExPerl.preg_replace(gVars.wp_smiliessearch, gVars.wp_smiliesreplace, content);
                }

                output = output + content;
            }
        } else {
    		// return default text.
            output = text;
        }

        return output;
    }

    public boolean is_email(String user_email) {
        String chars = null;
        chars = "/^([a-z0-9+_]|\\-|\\.)+@(([a-z0-9_]|\\-)+\\.)+[a-z]{2,6}$/i";

        if (!strictEqual(Strings.strpos(user_email, "@"), BOOLEAN_FALSE) && !strictEqual(Strings.strpos(user_email, "."), BOOLEAN_FALSE)) {
            if (QRegExPerl.preg_match(chars, user_email)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

 // used by wp-mail to handle charsets in email subjects
    public String wp_iso_descrambler(String string) {
        Array<Object> matches = new Array<Object>();
        String subject;

        /* this may only work with iso-8859-1, I'm afraid */
        if (!QRegExPerl.preg_match("#\\=\\?(.+)\\?Q\\?(.+)\\?\\=#i", string, matches)) {
            return string;
        } else {
            subject = Strings.str_replace("_", " ", strval(matches.getValue(2)));

            // Modified by Numiton
            subject = RegExPerl.preg_replace_callback("#\\=([0-9a-f]{2})#i", new Callback("replaceTransform", this), subject);

            return subject;
        }
    }

    public String replaceTransform(Array matches) {
        return Strings.chr(Math.hexdec(Strings.strtolower(strval(matches.getValue(1)))));
    }

 // give it a date, it will give you the same date as GMT
    public String get_gmt_from_date(String string) {
        Array<Object> matches = new Array<Object>();
        int string_time = 0;
        String string_gmt = null;
        
        // note: this only substracts $time_difference from the given date
        QRegExPerl.preg_match("#([0-9]{1,4})-([0-9]{1,2})-([0-9]{1,2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})#", string, matches);
        string_time = DateTime.gmmktime(
                intval(matches.getValue(4)),
                intval(matches.getValue(5)),
                intval(matches.getValue(6)),
                intval(matches.getValue(2)),
                intval(matches.getValue(3)),
                intval(matches.getValue(1)));
        string_gmt = DateTime.gmdate("Y-m-d H:i:s", intval(string_time - (floatval((getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset"))) * 3600)));

        return string_gmt;
    }

 // give it a GMT date, it will give you the same date with $time_difference added
    public String get_date_from_gmt(String string) {
        Array<Object> matches = new Array<Object>();
        int string_time = 0;
        String string_localtime = null;
        // note: this only adds $time_difference to the given date
        QRegExPerl.preg_match("#([0-9]{1,4})-([0-9]{1,2})-([0-9]{1,2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})#", string, matches);
        string_time = DateTime.gmmktime(
                intval(matches.getValue(4)),
                intval(matches.getValue(5)),
                intval(matches.getValue(6)),
                intval(matches.getValue(2)),
                intval(matches.getValue(3)),
                intval(matches.getValue(1)));
        string_localtime = DateTime.gmdate("Y-m-d H:i:s", intval(string_time + (floatval((getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset"))) * 3600)));

        return string_localtime;
    }

 // computes an offset in seconds from an iso8601 timezone
    public int iso8601_timezone_to_offset(String timezone) {
        int offset = 0;
        int sign = 0;
        int hours = 0;
        int minutes = 0;

        // $timezone is either 'Z' or '[+|-]hhmm'
        if (equal(timezone, "Z")) {
            offset = 0;
        } else {
            sign = (equal(Strings.substr(timezone, 0, 1), "+")
                ? 1
                : (-1));
            hours = intval(Strings.substr(timezone, 1, 2));
            minutes = intval(intval(Strings.substr(timezone, 3, 4)) / floatval(60));
            offset = (sign * 3600 * hours) + minutes;
        }

        return offset;
    }

 // converts an iso8601 date to MySQL DateTime format used by post_date[_gmt]
    public String iso8601_to_datetime(String date_string, String timezone) {
        Array<Object> date_bits = new Array<Object>();
        int offset = 0;
        int timestamp = 0;

        if (equal(timezone, "GMT")) {
            QRegExPerl.preg_match("#([0-9]{4})([0-9]{2})([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})(Z|[\\+|\\-][0-9]{2,4}){0,1}#", date_string, date_bits);

            if (!empty(date_bits.getValue(7))) { // we have a timezone, so let's compute an offset
                offset = iso8601_timezone_to_offset(strval(date_bits.getValue(7)));
            } else { // we don't have a timezone, so we assume user local timezone (not server's!)
                offset = intval(3600 * floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")));
            }

            timestamp = DateTime.gmmktime(
                    intval(date_bits.getValue(4)),
                    intval(date_bits.getValue(5)),
                    intval(date_bits.getValue(6)),
                    intval(date_bits.getValue(2)),
                    intval(date_bits.getValue(3)),
                    intval(date_bits.getValue(1)));
            timestamp = timestamp - offset;

            return DateTime.gmdate("Y-m-d H:i:s", timestamp);
        } else if (equal(timezone, "USER")) {
            return QRegExPerl.preg_replace("#([0-9]{4})([0-9]{2})([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})(Z|[\\+|\\-][0-9]{2,4}){0,1}#", "$1-$2-$3 $4:$5:$6", date_string);
        }

        return "";
    }

    public String popuplinks(String text) {
    	// Comment text in popup windows should be filtered through this.
    	// Right now it's a moderately dumb function, ideally it would detect whether
    	// a target or rel attribute was already there and adjust its actions accordingly.
        text = QRegExPerl.preg_replace("/<a (.+?)>/i", "<a $1 target=\'_blank\' rel=\'external\'>", text);

        return text;
    }

    public String sanitize_email(String email) {
        return QRegExPerl.preg_replace("/[^a-z0-9+_.@-]/i", "", email);
    }

    public String human_time_diff(int from, int to) {
        int diff = 0;
        int mins = 0;
        String since = null;
        int hours = 0;
        int days = 0;

        if (empty(to)) {
            to = DateTime.time();
        }

        diff = Math.abs(to - from);

        if (diff <= 3600) {
            mins = Math.round(diff / floatval(60));

            if (mins <= 1) {
                mins = 1;
            }

            since = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s min", "%s mins", mins, "default"), mins);
        } else if ((diff <= 86400) && (diff > 3600)) {
            hours = Math.round(floatval(diff) / floatval(3600));

            if (hours <= 1) {
                hours = 1;
            }

            since = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s hour", "%s hours", hours, "default"), hours);
        } else if (diff >= 86400) {
            days = Math.round(floatval(diff) / floatval(86400));

            if (days <= 1) {
                days = 1;
            }

            since = QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("%s day", "%s days", days, "default"), days);
        }

        return since;
    }

    public String wp_trim_excerpt(String text) { // Fakes an excerpt if needed
        int excerpt_length = 0;
        Array<String> words = new Array<String>();

        if (equal("", text)) {
            text = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_content("", 0, "");
            text = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_content", text));
            text = Strings.str_replace("]]>", "]]&gt;", text);
            text = Strings.strip_tags(text);
            excerpt_length = 55;
            words = Strings.explode(" ", text, excerpt_length + 1);

            if (Array.count(words) > excerpt_length) {
                Array.array_pop(words);
                Array.array_push(words, "[...]");
                text = Strings.implode(" ", words);
            }
        }

        return text;
    }

    @SuppressWarnings("unchecked")
    public String ent2ncr(String text) {
        Array<String> to_ncr = new Array<String>();
        to_ncr = new Array<String>(
                new ArrayEntry<String>("&quot;", "&#34;"),
                new ArrayEntry<String>("&amp;", "&#38;"),
                new ArrayEntry<String>("&frasl;", "&#47;"),
                new ArrayEntry<String>("&lt;", "&#60;"),
                new ArrayEntry<String>("&gt;", "&#62;"),
                new ArrayEntry<String>("|", "&#124;"),
                new ArrayEntry<String>("&nbsp;", "&#160;"),
                new ArrayEntry<String>("&iexcl;", "&#161;"),
                new ArrayEntry<String>("&cent;", "&#162;"),
                new ArrayEntry<String>("&pound;", "&#163;"),
                new ArrayEntry<String>("&curren;", "&#164;"),
                new ArrayEntry<String>("&yen;", "&#165;"),
                new ArrayEntry<String>("&brvbar;", "&#166;"),
                new ArrayEntry<String>("&brkbar;", "&#166;"),
                new ArrayEntry<String>("&sect;", "&#167;"),
                new ArrayEntry<String>("&uml;", "&#168;"),
                new ArrayEntry<String>("&die;", "&#168;"),
                new ArrayEntry<String>("&copy;", "&#169;"),
                new ArrayEntry<String>("&ordf;", "&#170;"),
                new ArrayEntry<String>("&laquo;", "&#171;"),
                new ArrayEntry<String>("&not;", "&#172;"),
                new ArrayEntry<String>("&shy;", "&#173;"),
                new ArrayEntry<String>("&reg;", "&#174;"),
                new ArrayEntry<String>("&macr;", "&#175;"),
                new ArrayEntry<String>("&hibar;", "&#175;"),
                new ArrayEntry<String>("&deg;", "&#176;"),
                new ArrayEntry<String>("&plusmn;", "&#177;"),
                new ArrayEntry<String>("&sup2;", "&#178;"),
                new ArrayEntry<String>("&sup3;", "&#179;"),
                new ArrayEntry<String>("&acute;", "&#180;"),
                new ArrayEntry<String>("&micro;", "&#181;"),
                new ArrayEntry<String>("&para;", "&#182;"),
                new ArrayEntry<String>("&middot;", "&#183;"),
                new ArrayEntry<String>("&cedil;", "&#184;"),
                new ArrayEntry<String>("&sup1;", "&#185;"),
                new ArrayEntry<String>("&ordm;", "&#186;"),
                new ArrayEntry<String>("&raquo;", "&#187;"),
                new ArrayEntry<String>("&frac14;", "&#188;"),
                new ArrayEntry<String>("&frac12;", "&#189;"),
                new ArrayEntry<String>("&frac34;", "&#190;"),
                new ArrayEntry<String>("&iquest;", "&#191;"),
                new ArrayEntry<String>("&Agrave;", "&#192;"),
                new ArrayEntry<String>("&Aacute;", "&#193;"),
                new ArrayEntry<String>("&Acirc;", "&#194;"),
                new ArrayEntry<String>("&Atilde;", "&#195;"),
                new ArrayEntry<String>("&Auml;", "&#196;"),
                new ArrayEntry<String>("&Aring;", "&#197;"),
                new ArrayEntry<String>("&AElig;", "&#198;"),
                new ArrayEntry<String>("&Ccedil;", "&#199;"),
                new ArrayEntry<String>("&Egrave;", "&#200;"),
                new ArrayEntry<String>("&Eacute;", "&#201;"),
                new ArrayEntry<String>("&Ecirc;", "&#202;"),
                new ArrayEntry<String>("&Euml;", "&#203;"),
                new ArrayEntry<String>("&Igrave;", "&#204;"),
                new ArrayEntry<String>("&Iacute;", "&#205;"),
                new ArrayEntry<String>("&Icirc;", "&#206;"),
                new ArrayEntry<String>("&Iuml;", "&#207;"),
                new ArrayEntry<String>("&ETH;", "&#208;"),
                new ArrayEntry<String>("&Ntilde;", "&#209;"),
                new ArrayEntry<String>("&Ograve;", "&#210;"),
                new ArrayEntry<String>("&Oacute;", "&#211;"),
                new ArrayEntry<String>("&Ocirc;", "&#212;"),
                new ArrayEntry<String>("&Otilde;", "&#213;"),
                new ArrayEntry<String>("&Ouml;", "&#214;"),
                new ArrayEntry<String>("&times;", "&#215;"),
                new ArrayEntry<String>("&Oslash;", "&#216;"),
                new ArrayEntry<String>("&Ugrave;", "&#217;"),
                new ArrayEntry<String>("&Uacute;", "&#218;"),
                new ArrayEntry<String>("&Ucirc;", "&#219;"),
                new ArrayEntry<String>("&Uuml;", "&#220;"),
                new ArrayEntry<String>("&Yacute;", "&#221;"),
                new ArrayEntry<String>("&THORN;", "&#222;"),
                new ArrayEntry<String>("&szlig;", "&#223;"),
                new ArrayEntry<String>("&agrave;", "&#224;"),
                new ArrayEntry<String>("&aacute;", "&#225;"),
                new ArrayEntry<String>("&acirc;", "&#226;"),
                new ArrayEntry<String>("&atilde;", "&#227;"),
                new ArrayEntry<String>("&auml;", "&#228;"),
                new ArrayEntry<String>("&aring;", "&#229;"),
                new ArrayEntry<String>("&aelig;", "&#230;"),
                new ArrayEntry<String>("&ccedil;", "&#231;"),
                new ArrayEntry<String>("&egrave;", "&#232;"),
                new ArrayEntry<String>("&eacute;", "&#233;"),
                new ArrayEntry<String>("&ecirc;", "&#234;"),
                new ArrayEntry<String>("&euml;", "&#235;"),
                new ArrayEntry<String>("&igrave;", "&#236;"),
                new ArrayEntry<String>("&iacute;", "&#237;"),
                new ArrayEntry<String>("&icirc;", "&#238;"),
                new ArrayEntry<String>("&iuml;", "&#239;"),
                new ArrayEntry<String>("&eth;", "&#240;"),
                new ArrayEntry<String>("&ntilde;", "&#241;"),
                new ArrayEntry<String>("&ograve;", "&#242;"),
                new ArrayEntry<String>("&oacute;", "&#243;"),
                new ArrayEntry<String>("&ocirc;", "&#244;"),
                new ArrayEntry<String>("&otilde;", "&#245;"),
                new ArrayEntry<String>("&ouml;", "&#246;"),
                new ArrayEntry<String>("&divide;", "&#247;"),
                new ArrayEntry<String>("&oslash;", "&#248;"),
                new ArrayEntry<String>("&ugrave;", "&#249;"),
                new ArrayEntry<String>("&uacute;", "&#250;"),
                new ArrayEntry<String>("&ucirc;", "&#251;"),
                new ArrayEntry<String>("&uuml;", "&#252;"),
                new ArrayEntry<String>("&yacute;", "&#253;"),
                new ArrayEntry<String>("&thorn;", "&#254;"),
                new ArrayEntry<String>("&yuml;", "&#255;"),
                new ArrayEntry<String>("&OElig;", "&#338;"),
                new ArrayEntry<String>("&oelig;", "&#339;"),
                new ArrayEntry<String>("&Scaron;", "&#352;"),
                new ArrayEntry<String>("&scaron;", "&#353;"),
                new ArrayEntry<String>("&Yuml;", "&#376;"),
                new ArrayEntry<String>("&fnof;", "&#402;"),
                new ArrayEntry<String>("&circ;", "&#710;"),
                new ArrayEntry<String>("&tilde;", "&#732;"),
                new ArrayEntry<String>("&Alpha;", "&#913;"),
                new ArrayEntry<String>("&Beta;", "&#914;"),
                new ArrayEntry<String>("&Gamma;", "&#915;"),
                new ArrayEntry<String>("&Delta;", "&#916;"),
                new ArrayEntry<String>("&Epsilon;", "&#917;"),
                new ArrayEntry<String>("&Zeta;", "&#918;"),
                new ArrayEntry<String>("&Eta;", "&#919;"),
                new ArrayEntry<String>("&Theta;", "&#920;"),
                new ArrayEntry<String>("&Iota;", "&#921;"),
                new ArrayEntry<String>("&Kappa;", "&#922;"),
                new ArrayEntry<String>("&Lambda;", "&#923;"),
                new ArrayEntry<String>("&Mu;", "&#924;"),
                new ArrayEntry<String>("&Nu;", "&#925;"),
                new ArrayEntry<String>("&Xi;", "&#926;"),
                new ArrayEntry<String>("&Omicron;", "&#927;"),
                new ArrayEntry<String>("&Pi;", "&#928;"),
                new ArrayEntry<String>("&Rho;", "&#929;"),
                new ArrayEntry<String>("&Sigma;", "&#931;"),
                new ArrayEntry<String>("&Tau;", "&#932;"),
                new ArrayEntry<String>("&Upsilon;", "&#933;"),
                new ArrayEntry<String>("&Phi;", "&#934;"),
                new ArrayEntry<String>("&Chi;", "&#935;"),
                new ArrayEntry<String>("&Psi;", "&#936;"),
                new ArrayEntry<String>("&Omega;", "&#937;"),
                new ArrayEntry<String>("&alpha;", "&#945;"),
                new ArrayEntry<String>("&beta;", "&#946;"),
                new ArrayEntry<String>("&gamma;", "&#947;"),
                new ArrayEntry<String>("&delta;", "&#948;"),
                new ArrayEntry<String>("&epsilon;", "&#949;"),
                new ArrayEntry<String>("&zeta;", "&#950;"),
                new ArrayEntry<String>("&eta;", "&#951;"),
                new ArrayEntry<String>("&theta;", "&#952;"),
                new ArrayEntry<String>("&iota;", "&#953;"),
                new ArrayEntry<String>("&kappa;", "&#954;"),
                new ArrayEntry<String>("&lambda;", "&#955;"),
                new ArrayEntry<String>("&mu;", "&#956;"),
                new ArrayEntry<String>("&nu;", "&#957;"),
                new ArrayEntry<String>("&xi;", "&#958;"),
                new ArrayEntry<String>("&omicron;", "&#959;"),
                new ArrayEntry<String>("&pi;", "&#960;"),
                new ArrayEntry<String>("&rho;", "&#961;"),
                new ArrayEntry<String>("&sigmaf;", "&#962;"),
                new ArrayEntry<String>("&sigma;", "&#963;"),
                new ArrayEntry<String>("&tau;", "&#964;"),
                new ArrayEntry<String>("&upsilon;", "&#965;"),
                new ArrayEntry<String>("&phi;", "&#966;"),
                new ArrayEntry<String>("&chi;", "&#967;"),
                new ArrayEntry<String>("&psi;", "&#968;"),
                new ArrayEntry<String>("&omega;", "&#969;"),
                new ArrayEntry<String>("&thetasym;", "&#977;"),
                new ArrayEntry<String>("&upsih;", "&#978;"),
                new ArrayEntry<String>("&piv;", "&#982;"),
                new ArrayEntry<String>("&ensp;", "&#8194;"),
                new ArrayEntry<String>("&emsp;", "&#8195;"),
                new ArrayEntry<String>("&thinsp;", "&#8201;"),
                new ArrayEntry<String>("&zwnj;", "&#8204;"),
                new ArrayEntry<String>("&zwj;", "&#8205;"),
                new ArrayEntry<String>("&lrm;", "&#8206;"),
                new ArrayEntry<String>("&rlm;", "&#8207;"),
                new ArrayEntry<String>("&ndash;", "&#8211;"),
                new ArrayEntry<String>("&mdash;", "&#8212;"),
                new ArrayEntry<String>("&lsquo;", "&#8216;"),
                new ArrayEntry<String>("&rsquo;", "&#8217;"),
                new ArrayEntry<String>("&sbquo;", "&#8218;"),
                new ArrayEntry<String>("&ldquo;", "&#8220;"),
                new ArrayEntry<String>("&rdquo;", "&#8221;"),
                new ArrayEntry<String>("&bdquo;", "&#8222;"),
                new ArrayEntry<String>("&dagger;", "&#8224;"),
                new ArrayEntry<String>("&Dagger;", "&#8225;"),
                new ArrayEntry<String>("&bull;", "&#8226;"),
                new ArrayEntry<String>("&hellip;", "&#8230;"),
                new ArrayEntry<String>("&permil;", "&#8240;"),
                new ArrayEntry<String>("&prime;", "&#8242;"),
                new ArrayEntry<String>("&Prime;", "&#8243;"),
                new ArrayEntry<String>("&lsaquo;", "&#8249;"),
                new ArrayEntry<String>("&rsaquo;", "&#8250;"),
                new ArrayEntry<String>("&oline;", "&#8254;"),
                new ArrayEntry<String>("&frasl;", "&#8260;"),
                new ArrayEntry<String>("&euro;", "&#8364;"),
                new ArrayEntry<String>("&image;", "&#8465;"),
                new ArrayEntry<String>("&weierp;", "&#8472;"),
                new ArrayEntry<String>("&real;", "&#8476;"),
                new ArrayEntry<String>("&trade;", "&#8482;"),
                new ArrayEntry<String>("&alefsym;", "&#8501;"),
                new ArrayEntry<String>("&crarr;", "&#8629;"),
                new ArrayEntry<String>("&lArr;", "&#8656;"),
                new ArrayEntry<String>("&uArr;", "&#8657;"),
                new ArrayEntry<String>("&rArr;", "&#8658;"),
                new ArrayEntry<String>("&dArr;", "&#8659;"),
                new ArrayEntry<String>("&hArr;", "&#8660;"),
                new ArrayEntry<String>("&forall;", "&#8704;"),
                new ArrayEntry<String>("&part;", "&#8706;"),
                new ArrayEntry<String>("&exist;", "&#8707;"),
                new ArrayEntry<String>("&empty;", "&#8709;"),
                new ArrayEntry<String>("&nabla;", "&#8711;"),
                new ArrayEntry<String>("&isin;", "&#8712;"),
                new ArrayEntry<String>("&notin;", "&#8713;"),
                new ArrayEntry<String>("&ni;", "&#8715;"),
                new ArrayEntry<String>("&prod;", "&#8719;"),
                new ArrayEntry<String>("&sum;", "&#8721;"),
                new ArrayEntry<String>("&minus;", "&#8722;"),
                new ArrayEntry<String>("&lowast;", "&#8727;"),
                new ArrayEntry<String>("&radic;", "&#8730;"),
                new ArrayEntry<String>("&prop;", "&#8733;"),
                new ArrayEntry<String>("&infin;", "&#8734;"),
                new ArrayEntry<String>("&ang;", "&#8736;"),
                new ArrayEntry<String>("&and;", "&#8743;"),
                new ArrayEntry<String>("&or;", "&#8744;"),
                new ArrayEntry<String>("&cap;", "&#8745;"),
                new ArrayEntry<String>("&cup;", "&#8746;"),
                new ArrayEntry<String>("&int;", "&#8747;"),
                new ArrayEntry<String>("&there4;", "&#8756;"),
                new ArrayEntry<String>("&sim;", "&#8764;"),
                new ArrayEntry<String>("&cong;", "&#8773;"),
                new ArrayEntry<String>("&asymp;", "&#8776;"),
                new ArrayEntry<String>("&ne;", "&#8800;"),
                new ArrayEntry<String>("&equiv;", "&#8801;"),
                new ArrayEntry<String>("&le;", "&#8804;"),
                new ArrayEntry<String>("&ge;", "&#8805;"),
                new ArrayEntry<String>("&sub;", "&#8834;"),
                new ArrayEntry<String>("&sup;", "&#8835;"),
                new ArrayEntry<String>("&nsub;", "&#8836;"),
                new ArrayEntry<String>("&sube;", "&#8838;"),
                new ArrayEntry<String>("&supe;", "&#8839;"),
                new ArrayEntry<String>("&oplus;", "&#8853;"),
                new ArrayEntry<String>("&otimes;", "&#8855;"),
                new ArrayEntry<String>("&perp;", "&#8869;"),
                new ArrayEntry<String>("&sdot;", "&#8901;"),
                new ArrayEntry<String>("&lceil;", "&#8968;"),
                new ArrayEntry<String>("&rceil;", "&#8969;"),
                new ArrayEntry<String>("&lfloor;", "&#8970;"),
                new ArrayEntry<String>("&rfloor;", "&#8971;"),
                new ArrayEntry<String>("&lang;", "&#9001;"),
                new ArrayEntry<String>("&rang;", "&#9002;"),
                new ArrayEntry<String>("&larr;", "&#8592;"),
                new ArrayEntry<String>("&uarr;", "&#8593;"),
                new ArrayEntry<String>("&rarr;", "&#8594;"),
                new ArrayEntry<String>("&darr;", "&#8595;"),
                new ArrayEntry<String>("&harr;", "&#8596;"),
                new ArrayEntry<String>("&loz;", "&#9674;"),
                new ArrayEntry<String>("&spades;", "&#9824;"),
                new ArrayEntry<String>("&clubs;", "&#9827;"),
                new ArrayEntry<String>("&hearts;", "&#9829;"),
                new ArrayEntry<String>("&diams;", "&#9830;"));

        return Strings.str_replace(Array.array_keys(to_ncr), Array.array_values(to_ncr), text);
    }

    public String wp_richedit_pre(String text) {
        String output = null;

    	// Filtering a blank results in an annoying <br />\n
        if (empty(text)) {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("richedit_pre", ""));
        }

        output = text;
        output = convert_chars(output, "");
        output = wpautop(output, 1);
        
    	// These must be double-escaped or planets will collide.
        output = Strings.str_replace("&lt;", "&amp;lt;", output);
        output = Strings.str_replace("&gt;", "&amp;gt;", output);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("richedit_pre", output));
    }

    public Object wp_htmledit_pre(String output) {
        if (!empty(output)) {
            output = Strings.htmlspecialchars(output, Strings.ENT_NOQUOTES); // convert only < > &
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("htmledit_pre", output);
    }

    public String clean_url(String url) {
        return clean_url(url, null, "display");
    }

    public String clean_url(String url, Object protocols, /* Do not change type */
        String context) {
        Object original_url = null;
        Array<Object> strip = new Array<Object>();
        original_url = url;

        if (equal("", url)) {
            return url;
        }

        url = QRegExPerl.preg_replace("|[^a-z0-9-~+_.?#=!&;,/:%@()]|i", "", url);
        strip = new Array<Object>(new ArrayEntry<Object>("%0d"), new ArrayEntry<Object>("%0a"));
        url = Strings.str_replace(strip, "", url);
        url = Strings.str_replace(";//", "://", url);

    	/* If the URL doesn't appear to contain a scheme, we
    	 * presume it needs http:// appended (unless a relative
    	 * link starting with / or a php file).
    	*/
        if (strictEqual(Strings.strpos(url, ":"), BOOLEAN_FALSE) && !equal(Strings.substr(url, 0, 1), "/") && !QRegExPerl.preg_match("/^[a-z0-9-]+?\\.php/i", url)) {
            url = "http://" + url;
        }

    	// Replace ampersands ony when displaying.
        if (equal("display", context)) {
            url = QRegExPerl.preg_replace("/&([^#])(?![a-z]{2,8};)/", "&#038;$1", url);
        }

        if (!is_array(protocols)) {
            protocols = new Array<String>(
                    new ArrayEntry<String>("http"),
                    new ArrayEntry<String>("https"),
                    new ArrayEntry<String>("ftp"),
                    new ArrayEntry<String>("ftps"),
                    new ArrayEntry<String>("mailto"),
                    new ArrayEntry<String>("news"),
                    new ArrayEntry<String>("irc"),
                    new ArrayEntry<String>("gopher"),
                    new ArrayEntry<String>("nntp"),
                    new ArrayEntry<String>("feed"),
                    new ArrayEntry<String>("telnet"));
        }

        if (!equal(getIncluded(KsesPage.class, gVars, gConsts).wp_kses_bad_protocol(url, (Array<String>) protocols), url)) {
            return "";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("clean_url", url, original_url, context));
    }

    public String sanitize_url(String url) {
        return sanitize_url(url, null);
    }

    public String sanitize_url(String url, Array<Object> protocols) {
        return clean_url(url, protocols, "db");
    }

 // Borrowed from the PHP Manual user notes. Convert entities, while
 // preserving already-encoded entities:
    public String htmlentities2(String myHTML) {
        Array<String> translation_table = new Array<String>();
        translation_table = Strings.get_html_translation_table(Strings.HTML_ENTITIES, Strings.ENT_QUOTES);
        translation_table.putValue(Strings.chr(38), "&");

        return QRegExPerl.preg_replace("/&(?![A-Za-z]{0,4}\\w{2,3};|#[0-9]{2,3};)/", "&amp;", Strings.strtr(myHTML, translation_table));
    }

    /**
     * Escape single quotes, specialchar double quotes, and fix line endings.
     */
    public String js_escape(String text) {
        String safe_text = null;
        safe_text = wp_specialchars(text, "double");
        // Escaped ? for Quercus
        safe_text = QRegExPerl.preg_replace("/&#(x)?0*(\\?(1)27|39);?/i", "\'", Strings.stripslashes(gVars.webEnv, safe_text));
        safe_text = QRegExPerl.preg_replace("/\r?\n/", "\\n", Strings.addslashes(gVars.webEnv, safe_text));

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("js_escape", safe_text, text));
    }

    /**
     * Escaping for HTML attributes Escaping for HTML attributes
     */
    public String attribute_escape(String text) {
        String safe_text = null;
        safe_text = wp_specialchars(text, strval(true));

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attribute_escape", safe_text, text));
    }

    public Array attribute_escapeArray(Array text) {
        Array safe_text = null;
        safe_text = wp_specialcharsArray(text, strval(true));

        return (Array) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attribute_escape", safe_text, text);
    }

    /**
     * Escape a HTML tag name
     */
    public String tag_escape(String tag_name) {
        String safe_tag = null;
        safe_tag = Strings.strtolower(QRegExPerl.preg_replace("[^a-zA-Z_:]", "", tag_name));

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tag_escape", safe_tag, tag_name));
    }

    /**
     * Escapes text for SQL LIKE special characters % and _
     * @param string text the text to be escaped
     * @return string text, safe for inclusion in LIKE query
     */
    public String like_escape(String text) {
        return Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("%"), new ArrayEntry<Object>("_")), new Array<Object>(new ArrayEntry<Object>("\\%"), new ArrayEntry<Object>("\\_")), text);
    }

    public String wp_make_link_relative(String link) {
        return QRegExPerl.preg_replace("|https?://[^/]+(/.*)|i", "$1", link);
    }

    public Object sanitize_option(String option, Object value) { // Remember to call stripslashes!
        {
            int javaSwitchSelector63 = 0;

            if (equal(option, "admin_email")) {
                javaSwitchSelector63 = 1;
            }

            if (equal(option, "default_post_edit_rows")) {
                javaSwitchSelector63 = 2;
            }

            if (equal(option, "mailserver_port")) {
                javaSwitchSelector63 = 3;
            }

            if (equal(option, "comment_max_links")) {
                javaSwitchSelector63 = 4;
            }

            if (equal(option, "page_on_front")) {
                javaSwitchSelector63 = 5;
            }

            if (equal(option, "rss_excerpt_length")) {
                javaSwitchSelector63 = 6;
            }

            if (equal(option, "default_category")) {
                javaSwitchSelector63 = 7;
            }

            if (equal(option, "default_email_category")) {
                javaSwitchSelector63 = 8;
            }

            if (equal(option, "default_link_category")) {
                javaSwitchSelector63 = 9;
            }

            if (equal(option, "posts_per_page")) {
                javaSwitchSelector63 = 10;
            }

            if (equal(option, "posts_per_rss")) {
                javaSwitchSelector63 = 11;
            }

            if (equal(option, "default_ping_status")) {
                javaSwitchSelector63 = 12;
            }

            if (equal(option, "default_comment_status")) {
                javaSwitchSelector63 = 13;
            }

            if (equal(option, "blogdescription")) {
                javaSwitchSelector63 = 14;
            }

            if (equal(option, "blogname")) {
                javaSwitchSelector63 = 15;
            }

            if (equal(option, "blog_charset")) {
                javaSwitchSelector63 = 16;
            }

            if (equal(option, "date_format")) {
                javaSwitchSelector63 = 17;
            }

            if (equal(option, "time_format")) {
                javaSwitchSelector63 = 18;
            }

            if (equal(option, "mailserver_url")) {
                javaSwitchSelector63 = 19;
            }

            if (equal(option, "mailserver_login")) {
                javaSwitchSelector63 = 20;
            }

            if (equal(option, "mailserver_pass")) {
                javaSwitchSelector63 = 21;
            }

            if (equal(option, "ping_sites")) {
                javaSwitchSelector63 = 22;
            }

            if (equal(option, "upload_path")) {
                javaSwitchSelector63 = 23;
            }

            if (equal(option, "gmt_offset")) {
                javaSwitchSelector63 = 24;
            }

            if (equal(option, "siteurl")) {
                javaSwitchSelector63 = 25;
            }

            if (equal(option, "home")) {
                javaSwitchSelector63 = 26;
            }

            switch (javaSwitchSelector63) {
            case 1: {
                value = sanitize_email(strval(value));

                break;
            }

            case 2: {
            }

            case 3: {
            }

            case 4: {
            }

            case 5: {
            }

            case 6: {
            }

            case 7: {
            }

            case 8: {
            }

            case 9: {
                value = Math.abs(intval(value));

                break;
            }

            case 10: {
            }

            case 11: {
                value = intval(value);

                if (empty(value)) {
                    value = 1;
                }

                if (intval(value) < -1) {
                    value = Math.abs(intval(value));
                }

                break;
            }

            case 12: {
            }

            case 13: {
    			// Options that if not there have 0 value but need to be something like "closed"
                if (equal(value, "0") || equal(value, "")) {
                    value = "closed";
                }

                break;
            }

            case 14: {
            }

            case 15: {
                value = Strings.addslashes(gVars.webEnv, strval(value));
                value = getIncluded(KsesPage.class, gVars, gConsts).wp_filter_post_kses(strval(value)); // calls stripslashes then addslashes
                value = Strings.stripslashes(gVars.webEnv, strval(value));
                value = wp_specialchars(strval(value), strval(0));

                break;
            }

            case 16: {
                value = QRegExPerl.preg_replace("/[^a-zA-Z0-9_-]/", "", strval(value)); // strips slashes

                break;
            }

            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23: {
                value = Strings.strip_tags(strval(value));
                value = Strings.addslashes(gVars.webEnv, strval(value));
                value = getIncluded(KsesPage.class, gVars, gConsts).wp_filter_kses(strval(value)); // calls stripslashes then addslashes
                value = Strings.stripslashes(gVars.webEnv, strval(value));

                break;
            }

            case 24: {
                value = QRegExPerl.preg_replace("/[^0-9:.-]/", "", strval(value)); // strips slashes

                break;
            }

            case 25: {
            }

            case 26: {
                value = Strings.stripslashes(gVars.webEnv, strval(value));
                value = clean_url(strval(value), null, "display");

                break;
            }

            default: {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("sanitize_option_" + option, value, option);

                break;
            }
            }
        }

        return value;
    }

    public void wp_parse_str(String string, Array<Object> array) {
        Strings.parse_str(string, array);

        if (booleanval(Options.get_magic_quotes_gpc(gVars.webEnv))) {
            array = (Array<Object>) stripslashes_deep(array); // parse_str() adds slashes if magicquotes is on.  See: http://php.net/parse_str
        }

        // Modified by Numiton
        Array.arrayReplace(array, (Array) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_parse_str", array));
    }

 // Convert lone less than signs.  KSES already converts lone greater than signs.
    public String wp_pre_kses_less_than(String text) {
        return RegExPerl.preg_replace_callback("%<[^>]*?((?=<)|>|$)%", new Callback("wp_pre_kses_less_than_callback", this), /*??*/
            text);
    }

    public String wp_pre_kses_less_than_callback(Array<Object> matches) {
        if (strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(matches.getValue(0)), ">"))) {
            return wp_specialchars(strval(matches.getValue(0)), strval(0));
        }

        return strval(matches.getValue(0));
    }

    /**
     * wp_sprintf() - sprintf() with filters
     */
    public String wp_sprintf(String pattern, Object... vargs) {
        Array<Object> args = new Array<Object>();
        int len = 0;
        int start = 0;
        String result = null;
        int arg_index = 0;
        int end = 0;
        String fragment = null;
        Array<Object> matches = new Array<Object>();
        String arg = null;
        String _fragment = null;
        args = FunctionHandling.func_get_args(FunctionHandling.buildTotalArgs(pattern, vargs));
        len = Strings.strlen(pattern);
        start = 0;
        result = "";
        arg_index = 0;

        while (len > start) {
    		// Last character: append and break
            if (equal(Strings.strlen(pattern) - 1, start)) {
                result = result + Strings.substr(pattern, -1);

                break;
            }

    		// Literal %: append and continue
            if (equal(Strings.substr(pattern, start, 2), "%%")) {
                start = start + 2;
                result = result + "%";

                continue;
            }

    		// Get fragment before next %
            end = Strings.strpos(pattern, "%", start + 1);

            if (strictEqual(BOOLEAN_FALSE, end)) {
                end = len;
            }

            fragment = Strings.substr(pattern, start, end - start);

    		// Fragment has a specifier
            if (equal(Strings.getCharAt(pattern, start), "%")) {
    			// Find numbered arguments or take the next one in order
                if (QRegExPerl.preg_match("/^%(\\d+)\\$/", fragment, matches)) {
                    arg = (isset(args.getValue(matches.getValue(1)))
                        ? strval(args.getValue(matches.getValue(1)))
                        : "");
                    fragment = Strings.str_replace("%" + strval(matches.getValue(1)) + "$", "%", fragment);
                } else {
                    ++arg_index;
                    arg = (isset(args.getValue(arg_index))
                        ? strval(args.getValue(arg_index))
                        : "");
                }

    			// Apply filters OR sprintf
                _fragment = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_sprintf", fragment, arg));

                if (!equal(_fragment, fragment)) {
                    fragment = _fragment;
                } else {
                    fragment = QStrings.sprintf(fragment, arg);
                }
            }

    		// Append to result and move to next fragment
            result = result + fragment;
            start = end;
        }

        return result;
    }

    /**
     * wp_sprintf_l - List specifier %l for wp_sprintf
     * @param unknown_type $pattern
     * @param unknown_type $args
     * @return unknown
     */
    public String wp_sprintf_l(String pattern, Array<Object> args) {
        Array<Object> l = new Array<Object>();
        String result = null;
        Object arg = null;
        Object i = null;

    	// Not a match
        if (!equal(Strings.substr(pattern, 0, 2), "%l")) {
            return pattern;
        }

    	// Nothing to work with
        if (empty(args)) {
            return "";
        }

    	// Translate and filter the delimiter set (avoid ampersands and entities here)
        l = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_sprintf_l",
                new Array<Object>(new ArrayEntry<Object>("between", getIncluded(L10nPage.class, gVars, gConsts)._c(", |between list items", "default")),
                    new ArrayEntry<Object>("between_last_two", getIncluded(L10nPage.class, gVars, gConsts)._c(", and |between last two list items", "default")),
                    new ArrayEntry<Object>("between_only_two", getIncluded(L10nPage.class, gVars, gConsts)._c(" and |between only two list items", "default"))));
        args = new Array<Object>(args);
        result = strval(Array.array_shift(args));

        if (equal(Array.count(args), 1)) {
            result = result + strval(l.getValue("between_only_two")) + strval(Array.array_shift(args));
        }

    	// Loop when more than two args
        while (booleanval(Array.count(args))) {
            arg = Array.array_shift(args);

            if (equal(i, 1)) {
                result = result + strval(l.getValue("between_last_two")) + strval(arg);
            } else {
                result = result + strval(l.getValue("between")) + strval(arg);
            }
        }

        return result + Strings.substr(pattern, 2);
    }

    /**
     * Safely extracts not more than the first $count characters from html
     * string
     * UTF-8, tags and entities safe prefix extraction. Entities inside will
     * NOT* be counted as one character. For example &amp; will be counted as
     * 4, &lt; as 3, etc.
     * @param integer $str String to get the excerpt from
     * @param integer $count Maximum number of characters to take
     * @eaturn string the excerpt
     */
    public String wp_html_excerpt(String str, int count) {
        str = Strings.strip_tags(str);
        str = QMultibyte.mb_strcut(gVars.webEnv, str, 0, count);
    	// remove part of an entity at the end
        str = QRegExPerl.preg_replace("/&[^;\\s]{0,6}$/", "", str);

        return str;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
