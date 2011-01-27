/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CompatPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ClassHandling;
import com.numiton.Options;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.DynamicConstructEvaluator;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QMisc;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class CompatPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CompatPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/compat.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/compat";
    }

    /**
     * WordPress implementation for PHP functions missing from older PHP versions.
     *
     * @package PHP
     * @access private
     */

    // Added in PHP 5.0
    
    public String http_build_query(Array<Object> data, Object prefix, String sep) {
        return _http_build_query(data, prefix, sep, "", true);
    }

    // from php.net (modified by Mark Jaquith to behave like the native PHP5 function)
    public String _http_build_query(Array<Object> data, Object prefix, String sep, String key, boolean urlencode) {
        Array<String> ret;
        String k = null;
        Object v = null;

        /* Do not change type */
        ret = new Array<String>();

        for (Map.Entry javaEntry438 : new Array<Object>(data).entrySet()) {
            k = strval(javaEntry438.getKey());
            v = javaEntry438.getValue();

            if (urlencode) {
                k = URL.urlencode(k);
            }

            if (is_int(k) && !equal(prefix, null)) {
                k = strval(prefix) + k;
            }

            if (!empty(key)) {
                k = key + "%5B" + k + "%5D";
            }

            if (strictEqual(v, "")) {
                continue;
            } else if (strictEqual(v, false)) {
                v = "0";
            }

            if (is_array(v)) {
                // Modified by Numiton
                Array.array_push(ret, _http_build_query((Array) v, "", sep, k, urlencode));
            } else if (is_object(v)) {
                Array.array_push(ret, _http_build_query(ClassHandling.get_object_vars(v), "", sep, k, urlencode));
            } else if (urlencode) {
                Array.array_push(ret, k + "=" + URL.urlencode(strval(v)));
            } else {
                Array.array_push(ret, k + "=" + v);
            }
        }

        if (equal("", sep)) {
            sep = Options.ini_get(gVars.webEnv, "arg_separator.output");
        }

        return Strings.implode(sep, ret);
    }

    public String _(String string) {
        return string;
    }

    public int stripos(String haystack, String needle, int offset) {
        return Strings.strpos(Strings.strtolower(haystack), Strings.strtolower(needle), offset);
    }

    public static String hash_hmac(final String algo, String data, String key) {
        return hash_hmac(algo, data, key, null);
    }

    public static String hash_hmac(final String algo, String data, String key, Object raw_output) {
        Array<Object> packs = new Array<Object>();
        String pack = null;
        int ipad = 0;
        int opad = 0;
        packs = new Array<Object>(new ArrayEntry<Object>("md5", "H32"), new ArrayEntry<Object>("sha1", "H40"));

        if (!isset(packs.getValue(algo))) {
            return strval(false);
        }

        pack = strval(packs.getValue(algo));

        // Modified by Numiton
        if (Strings.strlen(key) > 64) {
            key = QMisc.pack(pack, new AlgoEvaluator(algo, key).evaluate());
        } else if (Strings.strlen(key) < 64) {
            key = Strings.str_pad(key, 64, Strings.chr(0), 0);
        }

        ipad = intval(Strings.substr(key, 0, 64)) ^ intval(Strings.str_repeat(Strings.chr(54), 64));
        opad = intval(Strings.substr(key, 0, 64)) ^ intval(Strings.str_repeat(Strings.chr(92), 64));

        return strval(new AlgoEvaluator(algo, opad + QMisc.pack(pack, new AlgoEvaluator(algo, strval(ipad) + data).evaluate())).evaluate());
    }

    public String mb_strcut(String str, int start, int length, String encoding) {
        return _mb_strcut(str, start, length, encoding);
    }

    public String _mb_strcut(String str, int start, int length, String encoding) {
        String charset = null;
        Array<Array<Object>> match = new Array<Array<Object>>();
        Array<String> chars = new Array<String>();
        
    	// the solution below, works only for utf-8, so in case of a different
    	// charset, just use built-in substr
        charset = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));

        if (!Array.in_array(charset, new Array<Object>(new ArrayEntry<Object>("utf8"), new ArrayEntry<Object>("utf-8"), new ArrayEntry<Object>("UTF8"), new ArrayEntry<Object>("UTF-8")))) {
            return (length == 0)
            ? Strings.substr(str, start)
            : Strings.substr(str, start, length);
        }

        // use the regex unicode support to separate the UTF-8 characters into an array
        QRegExPerl.preg_match_all("/./us", str, match);

        // Modified by Numiton
        chars = ((length == 0)
            ? Array.array_slice(match.getArrayValue(0), start)
            : Array.array_slice(match.getArrayValue(0), start, length));

        return Strings.implode("", chars);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        // Removed by Numiton. All functions are declared.
        return DEFAULT_VAL;
    }

    // Added by Numiton
    public static class AlgoEvaluator implements DynamicConstructEvaluator {
        protected String algo;
        protected String key;

        /**
        * @param algo
        * @param key
        */
        public AlgoEvaluator(String algo, String key) {
            this.algo = algo;
            this.key = key;
        }

        public Object evaluate() {
            if (equal(algo, "md5")) {
                return Strings.md5(key);
            } else if (equal(algo, "sha1")) {
                //return Strings.sha1(key);
                throw new RuntimeException("Unsupported algorithm: " + algo);
            }

            return null;
        }
    }
}
