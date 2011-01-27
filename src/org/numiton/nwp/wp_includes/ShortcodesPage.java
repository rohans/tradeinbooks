/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ShortcodesPage.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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

/**********************************************************************************
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 **********************************************************************************/
package org.numiton.nwp.wp_includes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.FunctionHandling;
import com.numiton.RegExPerl;
import com.numiton.VarHandling;
import com.numiton.array.Array;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class ShortcodesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ShortcodesPage.class.getName());
    public Array<Object> shortcode_tags;

    @Override
    @RequestMapping("/wp-includes/shortcodes.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/shortcodes";
    }

    public void add_shortcode(String tag, Array<Object> func) {
        if (VarHandling.is_callable(new Callback(func))) {
            shortcode_tags.putValue(tag, func);
        }
    }

    public void remove_shortcode(Object tag) {
        shortcode_tags.arrayUnset(tag);
    }

    public void remove_all_shortcodes() {
        shortcode_tags = new Array<Object>();
    }

    public String do_shortcode(String content) {
        String pattern = null;

        if (empty(shortcode_tags) || !is_array(shortcode_tags)) {
            return content;
        }

        pattern = get_shortcode_regex();

        return RegExPerl.preg_replace_callback("/" + pattern + "/s", new Callback("do_shortcode_tag", this), content);
    }

    public String get_shortcode_regex() {
        Array<Object> tagnames = new Array<Object>();
        String tagregexp = null;
        tagnames = Array.array_keys(shortcode_tags);
        tagregexp = Strings.join("|", Array.array_map(new Callback("preg_quote", RegExPerl.class), tagnames));

        return "\\[(" + tagregexp + ")\\b(.*?)(?:(\\/))?\\](?:(.+?)\\[\\/\\1\\])?";
    }

    public Object do_shortcode_tag(Array<Object> m) {
        Object tag = null;
        Object attr = null;
        tag = m.getValue(1);
        attr = shortcode_parse_atts(strval(m.getValue(2)));

        if (isset(m.getValue(4))) {
    		// enclosing tag - extra parameter
            return FunctionHandling.call_user_func(new Callback(shortcode_tags.getArrayValue(tag)), attr, m.getValue(4));
        } else {
    		// self-closing tag
            return FunctionHandling.call_user_func(new Callback(shortcode_tags.getArrayValue(tag)), attr);
        }
    }

    public Object shortcode_parse_atts(String text) {
        Array<String> atts = null;
        String pattern = null;
        Array<Array<Object>> match = new Array<Array<Object>>();
        Array<String> m = new Array<String>();
        atts = new Array<String>();
        pattern = "/(\\w+)\\s*=\\s*\"([^\"]*)\"(?:\\s|$)|(\\w+)\\s*=\\s*\'([^\']*)\'(?:\\s|$)|(\\w+)\\s*=\\s*([^\\s\'\"]+)(?:\\s|$)|\"([^\"]*)\"(?:\\s|$)|(\\S+)(?:\\s|$)/";
        text = QRegExPerl.preg_replace("/[\\x{00a0}\\x{200b}]+/u", " ", text);

        if (booleanval(QRegExPerl.preg_match_all(pattern, text, match, RegExPerl.PREG_SET_ORDER))) {
            for (Map.Entry javaEntry597 : match.entrySet()) {
                m = (Array<String>) javaEntry597.getValue();

                if (!empty(m.getValue(1))) {
                    atts.putValue(Strings.strtolower(m.getValue(1)), Strings.stripcslashes(m.getValue(2)));
                } else if (!empty(m.getValue(3))) {
                    atts.putValue(Strings.strtolower(m.getValue(3)), Strings.stripcslashes(m.getValue(4)));
                } else if (!empty(m.getValue(5))) {
                    atts.putValue(Strings.strtolower(m.getValue(5)), Strings.stripcslashes(m.getValue(6)));
                } else if (isset(m.getValue(7)) && booleanval(Strings.strlen(m.getValue(7)))) {
                    atts.putValue(Strings.stripcslashes(m.getValue(7)));
                } else if (isset(m.getValue(8))) {
                    atts.putValue(Strings.stripcslashes(m.getValue(8)));
                }
            }
        } else {
            return Strings.ltrim(text);
        }

        return atts;
    }

    public Array<Object> shortcode_atts(Array<Object> pairs, Array<Object> atts) {
        Array<Object> out = new Array<Object>();
        Object name = null;
        Object _default = null;
        atts = new Array<Object>(atts);
        out = new Array<Object>();

        for (Map.Entry javaEntry598 : pairs.entrySet()) {
            name = javaEntry598.getKey();
            _default = javaEntry598.getValue();

            if (Array.array_key_exists(name, atts)) {
                out.putValue(name, atts.getValue(name));
            } else {
                out.putValue(name, _default);
            }
        }

        return out;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_shortcodes_block1");
        gVars.webEnv = webEnv;
        
        /*

        An API for creating shortcode tags that support attributes and enclosed content, such as:

        [shortcode /]
        [shortcode foo="bar" baz="bing" /]
        [shortcode foo="bar"]content[/shortcode]

        tag and attrbute parsing regexp code based on the Textpattern tag parser.

        To apply shortcode tags to content:

        $out = do_shortcode($content);

        Simplest example of a shortcode tag using the API:

        // [footag foo="bar"]
        function footag_func($atts) {
        	return "foo = {$atts[foo]}";
        }
        add_shortcode('footag', 'footag_func');

        Example with nice attribute defaults:

        // [bartag foo="bar"]
        function bartag_func($atts) {
        	extract(shortcode_atts(array(
        		'foo' => 'no foo',
        		'baz' => 'default baz',
        	), $atts));

        	return "foo = {$foo}";
        }
        add_shortcode('bartag', 'bartag_func');

        Example with enclosed content:

        // [baztag]content[/baztag]
        function baztag_func($atts, $content='') {
        	return "content = $content";
        }
        add_shortcode('baztag', 'baztag_func');

        */
        shortcode_tags = new Array<Object>();
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("the_content", Callback.createCallbackArray(this, "do_shortcode"), 11, 1); // AFTER wpautop() 

        return DEFAULT_VAL;
    }
}
