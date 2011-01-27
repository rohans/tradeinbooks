/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: GoogleSpell.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.RegExPerl;
import com.numiton.array.Array;
import com.numiton.curl.Curl;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.Ref;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


/**
 * $Id: GoogleSpell.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
 * @author Moxiecode
 * @copyright Copyright © 2004-2007, Moxiecode Systems AB, All rights reserved.
 */
public class GoogleSpell extends SpellChecker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(GoogleSpell.class.getName());

    public GoogleSpell(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants, null);
    }

    public GoogleSpell(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object config) {
        super(javaGlobalVariables, javaGlobalConstants, config);
    }

    /**
     * Spellchecks an array of words.
     * @param {String} $lang Language code like sv or en.
     * @param {Array} $words Array of words to spellcheck.
     * @return {Array} Array of misspelled words.
     */
    public Array<String> checkWords(String lang, Array<String> words) {
        String wordstr = null;
        Array<Object> matches = new Array<Object>();
        int i = 0;
        wordstr = Strings.implode(" ", words);
        matches = this._getMatches(lang, wordstr);
        words = new Array<String>();

        for (i = 0; i < Array.count(matches); i++)
            words.putValue(
                this._unhtmlentities(
                    getIncluded(GoogleSpellPage.class, gVars, gConsts).mb_substr(wordstr, intval(matches.getArrayValue(i).getValue(1)), intval(matches.getArrayValue(i).getValue(2)), "UTF-8")));

        return words;
    }

    /**
     * Returns suggestions of for a specific word.
     * @param {String} $lang Language code like sv or en.
     * @param {String} $word Specific word to get suggestions for.
     * @return {Array} Array of suggestions for the specified word.
     */
    public Array<Object> getSuggestions(String lang, String word) {
        Array<String> sug;
        Array<Object> osug = new Array<Object>();
        Array<Object> matches = new Array<Object>();
        Object item = null;
        sug = new Array<String>();
        osug = new Array<Object>();
        matches = this._getMatches(lang, word);

        if (Array.count(matches) > 0) {
            sug = Strings.explode("\t", XMLParser.utf8_encode(this._unhtmlentities(strval(matches.getArrayValue(0).getValue(4)))));
        }

		// Remove empty
        for (Map.Entry javaEntry487 : sug.entrySet()) {
            item = javaEntry487.getValue();

            if (booleanval(item)) {
                osug.putValue(item);
            }
        }

        return osug;
    }

    public Array<Object> _getMatches(String lang, String str) {
        String server = null;
        int port = 0;
        String path = null;
        String host = null;
        String url = null;
        String xml = null;
        String header = null;
        int ch = 0;
        int fp = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        Array matches = new Array();
        
        server = "www.google.com";
        port = 443;
        path = "/tbproxy/spell?lang=" + lang + "&hl=en";
        host = "www.google.com";
        url = "https://" + server;
        
		// Setup XML request
        xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><spellrequest textalreadyclipped=\"0\" ignoredups=\"0\" ignoredigits=\"1\" ignoreallcaps=\"1\"><text>" + str + "</text></spellrequest>";
        
        header = "POST " + path + " HTTP/1.0 \r\n";
        header = header + "MIME-Version: 1.0 \r\n";
        header = header + "Content-type: application/PTI26 \r\n";
        header = header + "Content-length: " + strval(Strings.strlen(xml)) + " \r\n";
        header = header + "Content-transfer-encoding: text \r\n";
        header = header + "Request-number: 1 \r\n";
        header = header + "Document-type: Request \r\n";
        header = header + "Interface-Version: Test 1.4 \r\n";
        header = header + "Connection: close \r\n\r\n";
        header = header + xml;

		// Use curl if it exists
        if (true)/*Modified by Numiton*/ {
			// Use curl
            ch = Curl.curl_init(gVars.webEnv);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_URL, url);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_RETURNTRANSFER, 1);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_CUSTOMREQUEST, header);
            Curl.curl_setopt(gVars.webEnv, ch, Curl.CURLOPT_SSL_VERIFYPEER, false);
            xml = Curl.curl_exec(gVars.webEnv, ch);
            Curl.curl_close(gVars.webEnv, ch);
        } else {
			// Use raw sockets
            fp = FileSystemOrSocket.fsockopen(gVars.webEnv, "ssl://" + server, port, errno, errstr, 30);

            if (booleanval(fp)) {
				// Send request
                FileSystemOrSocket.fwrite(gVars.webEnv, fp, header);
                
				// Read response
                xml = "";

                while (!FileSystemOrSocket.feof(gVars.webEnv, fp))
                    xml = xml + FileSystemOrSocket.fgets(gVars.webEnv, fp, 128);

                FileSystemOrSocket.fclose(gVars.webEnv, fp);
            } else {
                echo(gVars.webEnv, "Could not open SSL connection to google.");
            }
        }

		// Grab and parse content
        matches = new Array<Object>();
        QRegExPerl.preg_match_all("/<c o=\"([^\"]*)\" l=\"([^\"]*)\" s=\"([^\"]*)\">([^<]*)<\\/c>/", xml, matches, RegExPerl.PREG_SET_ORDER);

        return matches;
    }

    public String _unhtmlentities(String string) {
        Array<String> trans_tbl = new Array<String>();

        // Modified by Numiton
        string = RegExPerl.preg_replace_callback("~&#x([0-9a-f]+);~i", new Callback("replaceChrHexDec", CallbackUtils.class), string);

        // Modified by Numiton
        string = RegExPerl.preg_replace_callback("~&#([0-9]+);~", new Callback("replaceChr", CallbackUtils.class), string);
        trans_tbl = Strings.get_html_translation_table(Strings.HTML_ENTITIES);
        trans_tbl = Array.array_flip(trans_tbl);

        return Strings.strtr(string, trans_tbl);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
