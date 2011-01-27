/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: KsesPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

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

import com.numiton.RegExPerl;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class KsesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(KsesPage.class.getName());
    public Array<String> _kses_allowed_protocols;
    public Array<Object> allowedposttags;
    private Array replace_allowed_html;
    private Array replace_allowed_protocols;

    @Override
    @RequestMapping("/wp-includes/kses.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/kses";
    }

    public String wp_kses(String string, Array<Object> allowed_html) {
        return wp_kses(string, allowed_html,
            new Array<Object>(new ArrayEntry<Object>("http"),
                new ArrayEntry<Object>("https"),
                new ArrayEntry<Object>("ftp"),
                new ArrayEntry<Object>("ftps"),
                new ArrayEntry<Object>("mailto"),
                new ArrayEntry<Object>("news"),
                new ArrayEntry<Object>("irc"),
                new ArrayEntry<Object>("gopher"),
                new ArrayEntry<Object>("nntp"),
                new ArrayEntry<Object>("feed"),
                new ArrayEntry<Object>("telnet")));
    }

    public String wp_kses(String string, Array<Object> allowed_html, Array allowed_protocols) {
        Array<Object> allowed_html_fixed = new Array<Object>();
        string = wp_kses_no_null(string);
        string = wp_kses_js_entities(string);
        string = wp_kses_normalize_entities(string);
        allowed_html_fixed = wp_kses_array_lc(allowed_html);
        string = wp_kses_hook(string, allowed_html_fixed, allowed_protocols); // WP changed the order of these funcs and added args to wp_kses_hook

        return wp_kses_split(string, allowed_html_fixed, allowed_protocols);
    }

    /**
     * wp_kses_hook() - You add any kses hooks here.
     * There is currently only one kses WordPress hook and it is called here.
     * All parameters are passed to the hooks and expected to recieve a string.
     * @since 1.0.0
     * @param string $string Content to filter through kses
     * @param array $allowed_html List of allowed HTML elements
     * @param array $allowed_protocols Allowed protocol in links
     * @return string Filtered content through 'pre_kses' hook
     */
    public String wp_kses_hook(String string, Array<Object> allowed_html, Array<Object> allowed_protocols) {
        string = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_kses", string, allowed_html, allowed_protocols));

        return string;
    }

    /**
     * wp_kses_version() - This function returns kses' version number.
     * @since 1.0.0
     * @return string Version Number
     */
    public String wp_kses_version() {
        return "0.2.2";
    }

    /**
     * wp_kses_split() - Searches for HTML tags, no matter how malformed
     * It also matches stray ">" characters.
     * @since 1.0.0
     * @param string $string Content to filter
     * @param array $allowed_html Allowed HTML elements
     * @param array $allowed_protocols Allowed protocols to keep
     * @return string Content with fixed HTML tags
     */
    public String wp_kses_split(String string, Array<Object> allowed_html, Array<Object> allowed_protocols) {
        // Modified by Numiton
        replace_allowed_html = allowed_html;
        replace_allowed_protocols = allowed_protocols;

        return RegExPerl.preg_replace_callback("%((<!--.*?(-->|$))|(<[^>]*(>|$)|>))%", new Callback("replaceSplit", this), string);
    }

    public String replaceSplit(Array matches) {
        return wp_kses_split2(strval(matches.getValue(1)), replace_allowed_html, replace_allowed_protocols);
    }

    /**
     * wp_kses_split2() - Callback for wp_kses_split for fixing malformed HTML
     * tags
     * This function does a lot of work. It rejects some very malformed things
     * like <:::>. It returns an empty string, if the element isn't allowed
     * (look ma, no strip_tags()!). Otherwise it splits the tag into an element
     * and an attribute list.
     * After the tag is split into an element and an attribute list, it is run
     * through another filter which will remove illegal attributes and once that
     * is completed, will be returned.
     * @since 1.0.0
     * @uses wp_kses_attr()
     * @param string $string Content to filter
     * @param array $allowed_html Allowed HTML elements
     * @param array $allowed_protocols Allowed protocols to keep
     * @return string Fixed HTML element
     */
    public String wp_kses_split2(String string, Array<Object> allowed_html, Array<String> allowed_protocols) {
        Array<Object> matches = new Array<Object>();
        String newstring;
        String slash = null;
        String elem = null;
        String attrlist = null;
        string = wp_kses_stripslashes(string);

        if (!equal(Strings.substr(string, 0, 1), "<")) {
            return "&gt;";
        }
        // It matched a ">" character

        if (QRegExPerl.preg_match("%^<!--(.*?)(-->)?$%", string, matches)) {
            string = Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("<!--"), new ArrayEntry<Object>("-->")), "", strval(matches.getValue(1)));

            while (!equal(string, newstring = wp_kses(string, allowed_html, allowed_protocols)))
                string = newstring;

            if (equal(string, "")) {
                return "";
            }

            return "<!--" + string + "-->";
        }
        // Allow HTML comments

        if (!QRegExPerl.preg_match("%^<\\s*(/\\s*)?([a-zA-Z0-9]+)([^>]*)>?$%", string, matches)) {
            return "";
        }
        // It's seriously malformed
        
        slash = Strings.trim(strval(matches.getValue(1)));
        elem = strval(matches.getValue(2));
        attrlist = strval(matches.getValue(3));

        if (!isset(allowed_html.getValue(Strings.strtolower(elem)))) {
            return "";
        }
        
        // They are using a not allowed HTML element

        if (!equal(slash, "")) {
            return "<" + slash + elem + ">";
        }
        // No attributes are allowed for closing elements

        return wp_kses_attr(slash + elem, attrlist, allowed_html, allowed_protocols);
    }

    /**
     * wp_kses_attr() - Removes all attributes, if none are allowed for this
     * element
     * If some are allowed it calls wp_kses_hair() to split them further, and
     * then it builds up new HTML code from the data that kses_hair() returns.
     * It also removes "<" and ">" characters, if there are any left. One more
     * thing it does is to check if the tag has a closing XHTML slash, and if it
     * does, it puts one in the returned code as well.
     * @since 1.0.0
     * @param string $element HTML element/tag
     * @param string $attr HTML attributes from HTML element to closing HTML
     * element tag
     * @param array $allowed_html Allowed HTML elements
     * @param array $allowed_protocols Allowed protocols to keep
     * @return string Sanitized HTML element
     */
    public String wp_kses_attr(String element, String attr, Array<Object> allowed_html, Array<String> allowed_protocols) {
        String xhtml_slash = null;
        Array<String> attrarr = new Array<String>();
        String attr2 = null;
        Array<Object> arreach = new Array<Object>();
        Object current = null;

        /* Do not change type */
        boolean ok = false;
        String currkey = null;
        Object currval = null;
        
        // Is there a closing XHTML slash at the end of the attributes?
        xhtml_slash = "";

        if (QRegExPerl.preg_match("%\\s/\\s*$%", attr)) {
            xhtml_slash = " /";
        }

        // Are any attributes allowed at all for this element?
        if (equal(Array.count(allowed_html.getValue(Strings.strtolower(element))), 0)) {
            return "<" + element + xhtml_slash + ">";
        }

        // Split it
        
        attrarr = wp_kses_hair(attr, allowed_protocols);
        
        // Go through $attrarr, and save the allowed attributes for this element
    	// in $attr2
        
        attr2 = "";
        
        for (Map.Entry javaEntry500 : attrarr.entrySet()) {
            arreach = (Array<Object>) javaEntry500.getValue();

            if (!isset(allowed_html.getArrayValue(Strings.strtolower(element)).getValue(Strings.strtolower(strval(arreach.getValue("name")))))) {
                continue; // the attribute is not allowed
            }

            current = allowed_html.getArrayValue(Strings.strtolower(element)).getValue(Strings.strtolower(strval(arreach.getValue("name"))));

            if (equal(current, "")) {
                continue; // the attribute is not allowed
            }

            if (!is_array(current)) {
                attr2 = attr2 + " " + strval(arreach.getValue("whole"));
                // there are no checks
            } else {
            	// there are some checks
                ok = true;

                for (Map.Entry javaEntry501 : ((Array<?>) current).entrySet()) {
                    currkey = strval(javaEntry501.getKey());
                    currval = javaEntry501.getValue();

                    if (!wp_kses_check_attr_val(strval(arreach.getValue("value")), strval(arreach.getValue("vless")), currkey, currval)) {
                        ok = false;

                        break;
                    }
                }

                if (ok) {
                    attr2 = attr2 + " " + strval(arreach.getValue("whole")); // it passed them
                }
            }// if !is_array($current)
        } // foreach

        // Remove any "<" or ">" characters
        attr2 = QRegExPerl.preg_replace("/[<>]/", "", attr2);

        return "<" + element + attr2 + xhtml_slash + ">";
    }

    /**
     * wp_kses_hair() - Builds an attribute list from string containing
     * attributes.
     * This function does a lot of work. It parses an attribute list into an
     * array with attribute data, and tries to do the right thing even if it
     * gets weird input. It will add quotes around attribute values that don't
     * have any quotes or apostrophes around them, to make it easier to produce
     * HTML code that will conform to W3C's HTML specification. It will also
     * remove bad URL protocols from attribute values.
     * @since 1.0.0
     * @param string $attr Attribute list from HTML element to closing HTML
     * element tag
     * @param array $allowed_protocols Allowed protocols to keep
     * @return array List of attributes after parsing
     */
    public Array<String> wp_kses_hair(String attr, Array<String> allowed_protocols) {
        int mode = 0;
        String attrname = null;
        int working = 0;
        Array<Object> match = new Array<Object>();
        Object thisval = null;
        Array<String> attrarr = new Array<String>();
        mode = 0;
        attrname = "";

        // Loop through the whole attribute list
        
        while (!equal(Strings.strlen(attr), 0)) {
            working = 0; // Was the last operation successful?

            switch (mode) {
            case 0: { // attribute name, href for instance
                if (QRegExPerl.preg_match("/^([-a-zA-Z]+)/", attr, match)) {
                    attrname = strval(match.getValue(1));
                    working = mode = 1;
                    attr = QRegExPerl.preg_replace("/^[-a-zA-Z]+/", "", attr);
                }

                break;
            }

            case 1: { // equals sign or valueless ("selected")
                if (QRegExPerl.preg_match("/^\\s*=\\s*/", attr)) { // equals sign
                    working = 1;
                    mode = 2;
                    attr = QRegExPerl.preg_replace("/^\\s*=\\s*/", "", attr);

                    break;
                }

                if (QRegExPerl.preg_match("/^\\s+/", attr)) { // valueless
                    working = 1;
                    mode = 0;
                    attrarr.putValue(
                        new Array<Object>(new ArrayEntry<Object>("name", attrname), new ArrayEntry<Object>("value", ""), new ArrayEntry<Object>("whole", attrname), new ArrayEntry<Object>("vless", "y")));
                    attr = QRegExPerl.preg_replace("/^\\s+/", "", attr);
                }

                break;
            }

            case 2: { // attribute value, a URL after href= for instance
                if (QRegExPerl.preg_match("/^\"([^\"]*)\"(\\s+|$)/", attr, match)) { // "value"
                    thisval = wp_kses_bad_protocol(strval(match.getValue(1)), allowed_protocols);
                    attrarr.putValue(
                        new Array<Object>(
                            new ArrayEntry<Object>("name", attrname),
                            new ArrayEntry<Object>("value", thisval),
                            new ArrayEntry<Object>("whole", attrname + "=\"" + strval(thisval) + "\""),
                            new ArrayEntry<Object>("vless", "n")));
                    working = 1;
                    mode = 0;
                    attr = QRegExPerl.preg_replace("/^\"[^\"]*\"(\\s+|$)/", "", attr);

                    break;
                }

                if (QRegExPerl.preg_match("/^\'([^\']*)\'(\\s+|$)/", attr, match)) { // "value"
                    thisval = wp_kses_bad_protocol(strval(match.getValue(1)), allowed_protocols);
                    attrarr.putValue(
                        new Array<Object>(
                            new ArrayEntry<Object>("name", attrname),
                            new ArrayEntry<Object>("value", thisval),
                            new ArrayEntry<Object>("whole", attrname + "=\'" + strval(thisval) + "\'"),
                            new ArrayEntry<Object>("vless", "n")));
                    working = 1;
                    mode = 0;
                    attr = QRegExPerl.preg_replace("/^\'[^\']*\'(\\s+|$)/", "", attr);

                    break;
                }

                if (QRegExPerl.preg_match("%^([^\\s\"\']+)(\\s+|$)%", attr, match)) { // "value"
                    thisval = wp_kses_bad_protocol(strval(match.getValue(1)), allowed_protocols);
                    attrarr.putValue(
                        new Array<Object>(
                            new ArrayEntry<Object>("name", attrname),
                            new ArrayEntry<Object>("value", thisval),
                            new ArrayEntry<Object>("whole", attrname + "=\"" + strval(thisval) + "\""),
                            new ArrayEntry<Object>("vless", "n")));
                    
                    // We add quotes to conform to W3C's HTML spec.
                    working = 1;
                    mode = 0;
                    attr = QRegExPerl.preg_replace("%^[^\\s\"\']+(\\s+|$)%", "", attr);
                }

                break;
            }
            } // switch

            if (equal(working, 0)) { // not well formed, remove and try again
                attr = wp_kses_html_error(attr);
                mode = 0;
            }
        } // while

        if (equal(mode, 1)) {
        	// special case, for when the attribute list ends with a valueless
    		// attribute like "selected"
            attrarr.putValue(
                new Array<Object>(new ArrayEntry<Object>("name", attrname), new ArrayEntry<Object>("value", ""), new ArrayEntry<Object>("whole", attrname), new ArrayEntry<Object>("vless", "y")));
        }

        return attrarr;
    }

    /**
     * wp_kses_check_attr_val() - Performs different checks for attribute
     * values.
     * The currently implemented checks are "maxlen", "minlen", "maxval",
     * "minval" and "valueless" with even more checks to come soon.
     * @since 1.0.0
     * @param string $value Attribute value
     * @param string $vless Whether the value is valueless or not. Use 'y' or
     * 'n'
     * @param string $checkname What $checkvalue is checking for.
     * @param mixed $checkvalue What constraint the value should pass
     * @return bool Whether check passes (true) or not (false)
     */
    public boolean wp_kses_check_attr_val(String value, String vless, String checkname, Object checkvalue) {
        boolean ok = false;
        ok = true;

        {
            int javaSwitchSelector82 = 0;

            if (equal(Strings.strtolower(checkname), "maxlen")) {
                javaSwitchSelector82 = 1;
            }

            if (equal(Strings.strtolower(checkname), "minlen")) {
                javaSwitchSelector82 = 2;
            }

            if (equal(Strings.strtolower(checkname), "maxval")) {
                javaSwitchSelector82 = 3;
            }

            if (equal(Strings.strtolower(checkname), "minval")) {
                javaSwitchSelector82 = 4;
            }

            if (equal(Strings.strtolower(checkname), "valueless")) {
                javaSwitchSelector82 = 5;
            }

            switch (javaSwitchSelector82) {
            case 1: {
            	// The maxlen check makes sure that the attribute value has a length not
    			// greater than the given value. This can be used to avoid Buffer Overflows
    			// in WWW clients and various Internet servers.
                if (Strings.strlen(value) > intval(checkvalue)) {
                    ok = false;
                }

                break;
            }

            case 2: {
    			// The minlen check makes sure that the attribute value has a length not
    			// smaller than the given value.
            	
                if (Strings.strlen(value) < intval(checkvalue)) {
                    ok = false;
                }

                break;
            }

            case 3: {
    			// The maxval check does two things: it checks that the attribute value is
    			// an integer from 0 and up, without an excessive amount of zeroes or
    			// whitespace (to avoid Buffer Overflows). It also checks that the attribute
    			// value is not greater than the given value.
    			// This check can be used to avoid Denial of Service attacks.
                if (!QRegExPerl.preg_match("/^\\s{0,6}[0-9]{1,6}\\s{0,6}$/", value)) {
                    ok = false;
                }

                if (intval(value) > intval(checkvalue)) {
                    ok = false;
                }

                break;
            }

            case 4: {
    			// The minval check checks that the attribute value is a positive integer,
    			// and that it is not smaller than the given value.
                if (!QRegExPerl.preg_match("/^\\s{0,6}[0-9]{1,6}\\s{0,6}$/", value)) {
                    ok = false;
                }

                if (intval(value) < intval(checkvalue)) {
                    ok = false;
                }

                break;
            }

            case 5: {
            	// The valueless check checks if the attribute has a value
    			// (like <a href="blah">) or not (<option selected>). If the given value
    			// is a "y" or a "Y", the attribute must not have a value.
    			// If the given value is an "n" or an "N", the attribute must have one.
                if (!equal(Strings.strtolower(strval(checkvalue)), vless)) {
                    ok = false;
                }

                break;
            }
            }
        } // switch

        return ok;
    }

    /**
     * wp_kses_bad_protocol() - Sanitize string from bad protocols
     * This function removes all non-allowed protocols from the beginning of
     * $string. It ignores whitespace and the case of the letters, and it does
     * understand HTML entities. It does its work in a while loop, so it won't
     * be fooled by a string like "javascript:javascript:alert(57)".
     * @since 1.0.0
     * @param string $string Content to filter bad protocols from
     * @param array $allowed_protocols Allowed protocols to keep
     * @return string Filtered content
     */
    public String wp_kses_bad_protocol(String string, Array<String> allowed_protocols) {
        Object string2 = null;
        string = wp_kses_no_null(string);
        string = QRegExPerl.preg_replace("/\u00ad+/", "", string); // deals with Opera "feature"
        string2 = string + "a";

        while (!equal(string, string2)) {
            string2 = string;
            string = wp_kses_bad_protocol_once(string, allowed_protocols);
        } // while

        return string;
    }

    /**
     * wp_kses_no_null() - Removes any NULL characters in $string.
     * @since 1.0.0
     * @param string $string
     * @return string
     */
    public String wp_kses_no_null(String string) {
        string = QRegExPerl.preg_replace("/\\0+/", "", string);
        string = QRegExPerl.preg_replace("/(\\\\0)+/", "", string);

        return string;
    }

    /**
     * wp_kses_stripslashes() - Strips slashes from in front of quotes
     * This function changes the character sequence \" to just " It leaves all
     * other slashes alone. It's really weird, but the quoting from
     * preg_replace(//e) seems to require this.
     * @since 1.0.0
     * @param string $string String to strip slashes
     * @return string Fixed strings with quoted slashes
     */
    public String wp_kses_stripslashes(String string) {
        return QRegExPerl.preg_replace("%\\\\\"%", "\"", string);
    }

    /**
     * wp_kses_array_lc() - Goes through an array and changes the keys to all
     * lower case.
     * @since 1.0.0
     * @param array $inarray Unfiltered array
     * @return array Fixed array with all lowercase keys
     */
    public Array<Object> wp_kses_array_lc(Array<Object> inarray) {
        Array<Object> outarray = new Array<Object>();
        String outkey = null;
        String inkey = null;
        String outkey2 = null;
        String inkey2 = null;
        Object inval2 = null;
        Array<Object> inval = null;
        outarray = new Array<Object>();

        for (Map.Entry javaEntry502 : inarray.entrySet()) {
            inkey = strval(javaEntry502.getKey());
            inval = (Array<Object>) javaEntry502.getValue();
            outkey = Strings.strtolower(inkey);
            outarray.putValue(outkey, new Array<Object>());

            for (Map.Entry javaEntry503 : inval.entrySet()) {
                inkey2 = strval(javaEntry503.getKey());
                inval2 = javaEntry503.getValue();
                outkey2 = Strings.strtolower(inkey2);
                outarray.getArrayValue(outkey).putValue(outkey2, inval2);
            } // foreach $inval
        } // foreach $inarray

        return outarray;
    }

    /**
     * wp_kses_js_entities() - Removes the HTML JavaScript entities found in
     * early versions of Netscape 4.
     * @since 1.0.0
     * @param string $string
     * @return string
     */
    public String wp_kses_js_entities(String string) {
        return QRegExPerl.preg_replace("%&\\s*\\{[^}]*(\\}\\s*;?|$)%", "", string);
    }

    /**
     * wp_kses_html_error() - Handles parsing errors in wp_kses_hair()
     * The general plan is to remove everything to and including some
     * whitespace, but it deals with quotes and apostrophes as well.
     * @since 1.0.0
     * @param string $string
     * @return string
     */
    public String wp_kses_html_error(String string) {
        return QRegExPerl.preg_replace("/^(\"[^\"]*(\"|$)|\'[^\']*(\'|$)|\\S)*\\s*/", "", string);
    }

    /**
     * wp_kses_bad_protocol_once() - Sanitizes content from bad protocols and
     * other characters
     * This function searches for URL protocols at the beginning of $string,
     * while handling whitespace and HTML entities.
     * @since 1.0.0
     * @param string $string Content to check for bad protocols
     * @param string $allowed_protocols Allowed protocols
     * @return string Sanitized content
     */
    public String wp_kses_bad_protocol_once(String string, Array<String> allowed_protocols) {
        Array<String> string2 = new Array<String>();
        _kses_allowed_protocols = allowed_protocols;
        string2 = QRegExPerl.preg_split("/:|&#58;|&#x3a;/i", string, 2);

        if (isset(string2.getValue(1)) && !QRegExPerl.preg_match("%/\\?%", string2.getValue(0))) {
            string = wp_kses_bad_protocol_once2(string2.getValue(0), allowed_protocols) + Strings.trim(string2.getValue(1));
        } else {
            string = RegExPerl.preg_replace_callback("/^((&[^;]*;|[\\sA-Za-z0-9])*)" + "(:|&#58;|&#[Xx]3[Aa];)\\s*/", new Callback("createFunction_get_protocol", this), string);
        }

        return string;
    }

    public String createFunction_get_protocol(Array matches) {
        return wp_kses_bad_protocol_once2(strval(matches.getValue(1)), _kses_allowed_protocols);
    }

    /**
     * wp_kses_bad_protocol_once2() - Callback for wp_kses_bad_protocol_once()
     * regular expression.
     * This function processes URL protocols, checks to see if they're in the
     * white-list or not, and returns different data depending on the answer.
     * @since 1.0.0
     * @param string $string Content to check for bad protocols
     * @param array $allowed_protocols Allowed protocols
     * @return string Sanitized content
     */
    public String wp_kses_bad_protocol_once2(String string, Array<String> allowed_protocols) {
        String string2 = null;
        boolean allowed = false;
        String one_protocol = null;
        string2 = wp_kses_decode_entities(string);
        string2 = QRegExPerl.preg_replace("/\\s/", "", string2);
        string2 = wp_kses_no_null(string2);
        string2 = QRegExPerl.preg_replace("/\u00ad+/", "", string2);
        // deals with Opera "feature"
        string2 = Strings.strtolower(string2);
        
        allowed = false;

        for (Map.Entry javaEntry504 : allowed_protocols.entrySet()) {
            one_protocol = strval(javaEntry504.getValue());

            if (equal(Strings.strtolower(one_protocol), string2)) {
                allowed = true;

                break;
            }
        }

        if (allowed) {
            return string2 + ":";
        } else {
            return "";
        }
    }

    /**
     * wp_kses_normalize_entities() - Converts and fixes HTML entities
     * This function normalizes HTML entities. It will convert "AT&T" to the
     * correct "AT&amp;T", "&#00058;" to "&#58;", "&#XYZZY;" to "&amp;#XYZZY;"
     * and so on.
     * @since 1.0.0
     * @param string $string Content to normalize entities
     * @return string Content with normalized entities
     */
    public String wp_kses_normalize_entities(String string) {
    	// Disarm all entities by converting & to &amp;
    	
        string = Strings.str_replace("&", "&amp;", string);
        
        // Change back the allowed entities in our entity whitelist
        
        string = QRegExPerl.preg_replace("/&amp;([A-Za-z][A-Za-z0-9]{0,19});/", "&\\1;", string);
        string = RegExPerl.preg_replace_callback("/&amp;#0*([0-9]{1,5});/", new Callback("createFunction_normalize", this), string);
        string = QRegExPerl.preg_replace("/&amp;#([Xx])0*(([0-9A-Fa-f]{2}){1,2});/", "&#\\1\\2;", string);

        return string;
    }

    public String createFunction_normalize(Array matches) {
        return wp_kses_normalize_entities2(intval(matches.getValue(1)));
    }

    /**
     * wp_kses_normalize_entities2() - Callback for
     * wp_kses_normalize_entities() regular expression
     * This function helps wp_kses_normalize_entities() to only accept 16 bit
     * values and nothing more for &#number; entities.
     * @since 1.0.0
     * @param int $i Number encoded entity
     * @return string Correctly encoded entity
     */
    public String wp_kses_normalize_entities2(int i) {
        return (i > 65535)
        ? ("&amp;#" + strval(i) + ";")
        : ("&#" + strval(i) + ";");
    }

    /**
     * wp_kses_decode_entities() - Convert all entities to their character
     * counterparts.
     * This function decodes numeric HTML entities (&#65; and &#x41;). It
     * doesn't do anything with other entities like &auml;, but we don't need
     * them in the URL protocol whitelisting system anyway.
     * @since 1.0.0
     * @param string $string Content to change entities
     * @return string Content after decoded entities
     */
    public String wp_kses_decode_entities(String string) {
        // Modified by Numiton
        string = RegExPerl.preg_replace_callback("/&#([0-9]+);/", new Callback("replaceChr", CallbackUtils.class), string);

        // Modified by Numiton
        string = RegExPerl.preg_replace_callback("/&#[Xx]([0-9A-Fa-f]+);/", new Callback("replaceChrHexDec", CallbackUtils.class), string);

        return string;
    }

    /**
     * wp_filter_kses() - Sanitize content with allowed HTML Kses rules
     * @since 1.0.0
     * @uses $allowedtags
     * @param string $data Content to filter
     * @return string Filtered content
     */
    public String wp_filter_kses(String data) {
        return Strings.addslashes(gVars.webEnv,
            wp_kses(Strings.stripslashes(gVars.webEnv, data), gVars.allowedtags,
                new Array<Object>(
                    new ArrayEntry<Object>("http"),
                    new ArrayEntry<Object>("https"),
                    new ArrayEntry<Object>("ftp"),
                    new ArrayEntry<Object>("ftps"),
                    new ArrayEntry<Object>("mailto"),
                    new ArrayEntry<Object>("news"),
                    new ArrayEntry<Object>("irc"),
                    new ArrayEntry<Object>("gopher"),
                    new ArrayEntry<Object>("nntp"),
                    new ArrayEntry<Object>("feed"),
                    new ArrayEntry<Object>("telnet"))));
    }

    /**
     * wp_filter_post_kses() - Sanitize content for allowed HTML tags for post
     * content
     * Post content refers to the page contents of the 'post' type and not
     * $_POST data from forms.
     * @since 2.0.0
     * @uses $allowedposttags
     * @param string $data Post content to filter
     * @return string Filtered post content with allowed HTML tags and
     * attributes intact.
     */
    public String wp_filter_post_kses(String data) {
        return Strings.addslashes(gVars.webEnv,
            wp_kses(Strings.stripslashes(gVars.webEnv, data), allowedposttags,
                new Array<Object>(
                    new ArrayEntry<Object>("http"),
                    new ArrayEntry<Object>("https"),
                    new ArrayEntry<Object>("ftp"),
                    new ArrayEntry<Object>("ftps"),
                    new ArrayEntry<Object>("mailto"),
                    new ArrayEntry<Object>("news"),
                    new ArrayEntry<Object>("irc"),
                    new ArrayEntry<Object>("gopher"),
                    new ArrayEntry<Object>("nntp"),
                    new ArrayEntry<Object>("feed"),
                    new ArrayEntry<Object>("telnet"))));
    }

    /**
     * wp_filter_nohtml_kses() - Strips all of the HTML in the content
     * @since 2.1.0
     * @param string $data Content to strip all HTML from
     * @return string Filtered content without any HTML
     */
    public String wp_filter_nohtml_kses(String data) {
        return Strings.addslashes(gVars.webEnv,
            wp_kses(Strings.stripslashes(gVars.webEnv, data), new Array<Object>(),
                new Array<Object>(
                    new ArrayEntry<Object>("http"),
                    new ArrayEntry<Object>("https"),
                    new ArrayEntry<Object>("ftp"),
                    new ArrayEntry<Object>("ftps"),
                    new ArrayEntry<Object>("mailto"),
                    new ArrayEntry<Object>("news"),
                    new ArrayEntry<Object>("irc"),
                    new ArrayEntry<Object>("gopher"),
                    new ArrayEntry<Object>("nntp"),
                    new ArrayEntry<Object>("feed"),
                    new ArrayEntry<Object>("telnet"))));
    }

    /**
     * kses_init_filters() - Adds all Kses input form content filters
     * All hooks have default priority. The wp_filter_kses() fucntion is added
     * to the 'pre_comment_content' and 'title_save_pre' hooks. The
     * wp_filter_post_kses() function is added to the 'content_save_pre',
     * 'excerpt_save_pre', and 'content_filtered_save_pre' hooks.
     * @since 2.0.0
     * @uses add_filter() See description for what functions are added to what
     * hooks.
     */
    public void kses_init_filters() {
    	// Normal filtering.
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("pre_comment_content", Callback.createCallbackArray(this, "wp_filter_kses"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("title_save_pre", Callback.createCallbackArray(this, "wp_filter_kses"), 10, 1);
        
    	// Post filtering
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("content_save_pre", Callback.createCallbackArray(this, "wp_filter_post_kses"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("excerpt_save_pre", Callback.createCallbackArray(this, "wp_filter_post_kses"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("content_filtered_save_pre", Callback.createCallbackArray(this, "wp_filter_post_kses"), 10, 1);
    }

    /**
     * kses_remove_filters() - Removes all Kses input form content filters
     * A quick procedural method to removing all of the filters that kses uses
     * for content in WordPress Loop.
     * Does not remove the kses_init() function from 'init' hook (priority is
     * default). Also does not remove kses_init() function from
     * 'set_current_user' hook (priority is also default).
     * @since 2.0.6
     */
    public void kses_remove_filters() {
    	// Normal filtering.
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("pre_comment_content", "wp_filter_kses", 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("title_save_pre", "wp_filter_kses", 10, 1);
        
        // Post filtering
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("content_save_pre", "wp_filter_post_kses", 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("excerpt_save_pre", "wp_filter_post_kses", 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).remove_filter("content_filtered_save_pre", "wp_filter_post_kses", 10, 1);
    }

    /**
     * kses_init() - Sets up most of the Kses filters for input form content
     * If you remove the kses_init() function from 'init' hook and
     * 'set_current_user' (priority is default), then none of the Kses filter
     * hooks will be added.
     * First removes all of the Kses filters in case the current user does not
     * need to have Kses filter the content. If the user does not have
     * unfiltered html capability, then Kses filters are added.
     * @uses kses_remove_filters() Removes the Kses filters
     * @uses kses_init_filters() Adds the Kses filters back if the user does not
     * have unfiltered HTML capability.
     * @since 2.0.0
     */
    public void kses_init() {
        kses_remove_filters();

        if (equal(getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("unfiltered_html"), false)) {
            kses_init_filters();
        }
    }

    @SuppressWarnings("unchecked")
    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_kses_block1");
        gVars.webEnv = webEnv;

        /**
         * HTML/XHTML filter that only allows some elements and attributes
         *
         * Added wp_ prefix to avoid conflicts with existing kses users
         *
         * @version 0.2.2
         * @copyright (C) 2002, 2003, 2005
         * @author Ulf Harnhammar <metaur@users.sourceforge.net>
         *
         * @package External
         * @subpackage KSES
         *
         * @internal
         * *** CONTACT INFORMATION ***
         * E-mail:      metaur at users dot sourceforge dot net
         * Web page:    http://sourceforge.net/projects/kses
         * Paper mail:  Ulf Harnhammar
         *              Ymergatan 17 C
         *              753 25  Uppsala
         *              SWEDEN
         *
         * [kses strips evil scripts!]
         */

        /**
         * You can override this in your my-hacks.php file
         * You can also override this in a plugin file. The
         * my-hacks.php is deprecated in its usage.
         *
         * @since 1.2.0
         */
        
        if (!gConsts.isCUSTOM_TAGSDefined()) {
            gConsts.setCUSTOM_TAGS(false);
        }

        if (!gConsts.getCUSTOM_TAGS()) {
        	/**
        	 * Kses global for default allowable HTML tags
        	 *
        	 * Can be override by using CUSTOM_TAGS constant
        	 * @global array $allowedposttags
        	 * @since 2.0.0
        	 */
            allowedposttags = new Array<Object>(
                    new ArrayEntry<Object>("address", new Array<Object>()),
                    new ArrayEntry<Object>("a",
                        new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("href", new Array<Object>()),
                            new ArrayEntry<Object>("id", new Array<Object>()),
                            new ArrayEntry<Object>("title", new Array<Object>()),
                            new ArrayEntry<Object>("rel", new Array<Object>()),
                            new ArrayEntry<Object>("rev", new Array<Object>()),
                            new ArrayEntry<Object>("name", new Array<Object>()),
                            new ArrayEntry<Object>("target", new Array<Object>()))),
                    new ArrayEntry<Object>("abbr", new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()), new ArrayEntry<Object>("title", new Array<Object>()))),
                    new ArrayEntry<Object>("acronym", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                    new ArrayEntry<Object>("b", new Array<Object>()),
                    new ArrayEntry<Object>("big", new Array<Object>()),
                    new ArrayEntry<Object>("blockquote",
                        new Array<Object>(new ArrayEntry<Object>("id", new Array<Object>()),
                            new ArrayEntry<Object>("cite", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("lang", new Array<Object>()),
                            new ArrayEntry<Object>("xml:lang", new Array<Object>()))),
                    new ArrayEntry<Object>("br", new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("button",
                        new Array<Object>(new ArrayEntry<Object>("disabled", new Array<Object>()),
                            new ArrayEntry<Object>("name", new Array<Object>()),
                            new ArrayEntry<Object>("type", new Array<Object>()),
                            new ArrayEntry<Object>("value", new Array<Object>()))),
                    new ArrayEntry<Object>("caption", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("cite",
                        new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("lang", new Array<Object>()),
                            new ArrayEntry<Object>("title", new Array<Object>()))),
                    new ArrayEntry<Object>("code", new Array<Object>(new ArrayEntry<Object>("style", new Array<Object>()))),
                    new ArrayEntry<Object>("col",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("span", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()),
                            new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("del", new Array<Object>(new ArrayEntry<Object>("datetime", new Array<Object>()))),
                    new ArrayEntry<Object>("dd", new Array<Object>()),
                    new ArrayEntry<Object>("div",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("lang", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("xml:lang", new Array<Object>()))),
                    new ArrayEntry<Object>("dl", new Array<Object>()),
                    new ArrayEntry<Object>("dt", new Array<Object>()),
                    new ArrayEntry<Object>("em", new Array<Object>()),
                    new ArrayEntry<Object>("fieldset", new Array<Object>()),
                    new ArrayEntry<Object>(
                        "font",
                        new Array<Object>(new ArrayEntry<Object>("color", new Array<Object>()), new ArrayEntry<Object>("face", new Array<Object>()), new ArrayEntry<Object>("size", new Array<Object>()))),
                    new ArrayEntry<Object>("form",
                        new Array<Object>(new ArrayEntry<Object>("action", new Array<Object>()),
                            new ArrayEntry<Object>("accept", new Array<Object>()),
                            new ArrayEntry<Object>("accept-charset", new Array<Object>()),
                            new ArrayEntry<Object>("enctype", new Array<Object>()),
                            new ArrayEntry<Object>("method", new Array<Object>()),
                            new ArrayEntry<Object>("name", new Array<Object>()),
                            new ArrayEntry<Object>("target", new Array<Object>()))),
                    new ArrayEntry<Object>("h1", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("h2", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("h3", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("h4", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("h5", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("h6", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("hr",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("noshade", new Array<Object>()), new ArrayEntry<Object>("size", new Array<Object>()), new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("i", new Array<Object>()),
                    new ArrayEntry<Object>("img",
                        new Array<Object>(new ArrayEntry<Object>("alt", new Array<Object>()),
                            new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("border", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("height", new Array<Object>()),
                            new ArrayEntry<Object>("hspace", new Array<Object>()),
                            new ArrayEntry<Object>("longdesc", new Array<Object>()),
                            new ArrayEntry<Object>("vspace", new Array<Object>()),
                            new ArrayEntry<Object>("src", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("ins", new Array<Object>(new ArrayEntry<Object>("datetime", new Array<Object>()), new ArrayEntry<Object>("cite", new Array<Object>()))),
                    new ArrayEntry<Object>("kbd", new Array<Object>()),
                    new ArrayEntry<Object>("label", new Array<Object>(new ArrayEntry<Object>("for", new Array<Object>()))),
                    new ArrayEntry<Object>("legend", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()))),
                    new ArrayEntry<Object>("li", new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()), new ArrayEntry<Object>("class", new Array<Object>()))),
                    new ArrayEntry<Object>("p",
                        new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("lang", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("xml:lang", new Array<Object>()))),
                    new ArrayEntry<Object>("pre", new Array<Object>(new ArrayEntry<Object>("style", new Array<Object>()), new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("q", new Array<Object>(new ArrayEntry<Object>("cite", new Array<Object>()))),
                    new ArrayEntry<Object>("s", new Array<Object>()),
                    new ArrayEntry<Object>("span",
                        new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("title", new Array<Object>()))),
                    new ArrayEntry<Object>("strike", new Array<Object>()),
                    new ArrayEntry<Object>("strong", new Array<Object>()),
                    new ArrayEntry<Object>("sub", new Array<Object>()),
                    new ArrayEntry<Object>("sup", new Array<Object>()),
                    new ArrayEntry<Object>("table",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("bgcolor", new Array<Object>()),
                            new ArrayEntry<Object>("border", new Array<Object>()),
                            new ArrayEntry<Object>("cellpadding", new Array<Object>()),
                            new ArrayEntry<Object>("cellspacing", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("id", new Array<Object>()),
                            new ArrayEntry<Object>("rules", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("summary", new Array<Object>()),
                            new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("tbody",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()))),
                    new ArrayEntry<Object>("td",
                        new Array<Object>(new ArrayEntry<Object>("abbr", new Array<Object>()),
                            new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("axis", new Array<Object>()),
                            new ArrayEntry<Object>("bgcolor", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("colspan", new Array<Object>()),
                            new ArrayEntry<Object>("dir", new Array<Object>()),
                            new ArrayEntry<Object>("headers", new Array<Object>()),
                            new ArrayEntry<Object>("height", new Array<Object>()),
                            new ArrayEntry<Object>("nowrap", new Array<Object>()),
                            new ArrayEntry<Object>("rowspan", new Array<Object>()),
                            new ArrayEntry<Object>("scope", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()),
                            new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("textarea",
                        new Array<Object>(new ArrayEntry<Object>("cols", new Array<Object>()),
                            new ArrayEntry<Object>("rows", new Array<Object>()),
                            new ArrayEntry<Object>("disabled", new Array<Object>()),
                            new ArrayEntry<Object>("name", new Array<Object>()),
                            new ArrayEntry<Object>("readonly", new Array<Object>()))),
                    new ArrayEntry<Object>("tfoot",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()))),
                    new ArrayEntry<Object>("th",
                        new Array<Object>(new ArrayEntry<Object>("abbr", new Array<Object>()),
                            new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("axis", new Array<Object>()),
                            new ArrayEntry<Object>("bgcolor", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("colspan", new Array<Object>()),
                            new ArrayEntry<Object>("headers", new Array<Object>()),
                            new ArrayEntry<Object>("height", new Array<Object>()),
                            new ArrayEntry<Object>("nowrap", new Array<Object>()),
                            new ArrayEntry<Object>("rowspan", new Array<Object>()),
                            new ArrayEntry<Object>("scope", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()),
                            new ArrayEntry<Object>("width", new Array<Object>()))),
                    new ArrayEntry<Object>("thead",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()))),
                    new ArrayEntry<Object>("title", new Array<Object>()),
                    new ArrayEntry<Object>("tr",
                        new Array<Object>(new ArrayEntry<Object>("align", new Array<Object>()),
                            new ArrayEntry<Object>("bgcolor", new Array<Object>()),
                            new ArrayEntry<Object>("char", new Array<Object>()),
                            new ArrayEntry<Object>("charoff", new Array<Object>()),
                            new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("valign", new Array<Object>()))),
                    new ArrayEntry<Object>("tt", new Array<Object>()),
                    new ArrayEntry<Object>("u", new Array<Object>()),
                    new ArrayEntry<Object>(
                        "ul",
                        new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()), new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("type", new Array<Object>()))),
                    new ArrayEntry<Object>("ol",
                        new Array<Object>(new ArrayEntry<Object>("class", new Array<Object>()),
                            new ArrayEntry<Object>("start", new Array<Object>()),
                            new ArrayEntry<Object>("style", new Array<Object>()),
                            new ArrayEntry<Object>("type", new Array<Object>()))),
                    new ArrayEntry<Object>("var", new Array<Object>()));
            
        	/**
        	 * Kses allowed HTML elements
        	 *
        	 * @global array $allowedtags
        	 * @since 1.0.0
        	 */
            gVars.allowedtags = new Array<Object>(
                        new ArrayEntry<Object>("a", new Array<Object>(new ArrayEntry<Object>("href", new Array<Object>()), new ArrayEntry<Object>("title", new Array<Object>()))),
                        new ArrayEntry<Object>("abbr", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                        new ArrayEntry<Object>("acronym", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                        new ArrayEntry<Object>("b", new Array<Object>()),
                        new ArrayEntry<Object>("blockquote", new Array<Object>(new ArrayEntry<Object>("cite", new Array<Object>()))),
                        new ArrayEntry<Object>("cite", new Array<Object>()),
                		//	'br' => array(),
                        new ArrayEntry<Object>("code", new Array<Object>()),
                        new ArrayEntry<Object>("del", new Array<Object>(new ArrayEntry<Object>("datetime", new Array<Object>()))),
                		//	'dd' => array(),
                		//	'dl' => array(),
                		//	'dt' => array(),
                        new ArrayEntry<Object>("em", new Array<Object>()),
                        new ArrayEntry<Object>("i", new Array<Object>()),
                		//	'ins' => array('datetime' => array(), 'cite' => array()),
                		//	'li' => array(),
                		//	'ol' => array(),
                		//	'p' => array(),
                        new ArrayEntry<Object>("q", new Array<Object>(new ArrayEntry<Object>("cite", new Array<Object>()))),
                        new ArrayEntry<Object>("strike", new Array<Object>()),
                        new ArrayEntry<Object>("strong", new Array<Object>())
	            		//	'sub' => array(),
						//	'sup' => array(),
						//	'u' => array(),
						//	'ul' => array(),            
            		);
        }

        getIncluded(PluginPage.class, gVars, gConsts).add_action("init", Callback.createCallbackArray(this, "kses_init"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("set_current_user", Callback.createCallbackArray(this, "kses_init"), 10, 1);

        return DEFAULT_VAL;
    }
}
