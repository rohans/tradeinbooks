/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PSpellShell.java,v 1.6 2008/10/14 13:15:49 numiton Exp $
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

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.Options;
import com.numiton.RegExPerl;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.java.JMultibyte;
import com.numiton.ntile.til.libraries.php.quercus.QMultibyte;
import com.numiton.ntile.til.libraries.php.quercus.QProgramExecution;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


/**
 * $Id: PSpellShell.java,v 1.6 2008/10/14 13:15:49 numiton Exp $
 * @author Moxiecode
 * @copyright Copyright © 2004-2007, Moxiecode Systems AB, All rights reserved.
 */
public class PSpellShell extends SpellChecker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(PSpellShell.class.getName());

    public String _tmpfile;
    public Array<Object> _config = new Array<Object>();

    public PSpellShell(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants, null);
    }

    public PSpellShell(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object config) {
        super(javaGlobalVariables, javaGlobalConstants, config);
    }

    /**
     * Spellchecks an array of words.
     * @param {String} $lang Language code like sv or en.
     * @param {Array} $words Array of words to spellcheck.
     * @return {Array} Array of misspelled words.
     */
    public Array<Object> checkWords(String lang, Array<Object> words) {
        String cmd = null;
        int fh = 0;
        Object value = null;
        Object key = null;
        String data = null;
        Array<Object> returnData = new Array<Object>();
        Array<Object> dataArr = new Array<Object>();
        Array<Object> matches = new Array<Object>();
        String dstr = null;
        cmd = this._getCMD(lang);

        if (booleanval(fh = FileSystemOrSocket.fopen(gVars.webEnv, this._tmpfile, "w"))) {
            FileSystemOrSocket.fwrite(gVars.webEnv, fh, "!\n");

            for (Map.Entry javaEntry489 : words.entrySet()) {
                key = javaEntry489.getKey();
                value = javaEntry489.getValue();
                FileSystemOrSocket.fwrite(gVars.webEnv, fh, "^" + strval(value) + "\n");
            }

            FileSystemOrSocket.fclose(gVars.webEnv, fh);
        } else {
            this.throwError("PSpell support was not found.");
        }

        data = QProgramExecution.shell_exec(cmd);
        JFileSystemOrSocket.unlink(gVars.webEnv, this._tmpfile);
        returnData = new Array<Object>();
        dataArr = QRegExPerl.preg_split("/[\r\n]/", data, -1, RegExPerl.PREG_SPLIT_NO_EMPTY);

        for (Map.Entry javaEntry490 : dataArr.entrySet()) {
            dstr = strval(javaEntry490.getValue());
            matches = new Array<Object>();

			// Skip this line.
            if (strictEqual(Strings.strpos(dstr, "@"), 0)) {
                continue;
            }

            QRegExPerl.preg_match("/\\& ([^ ]+) .*/i", dstr, matches);

            if (!empty(matches.getValue(1))) {
                returnData.putValue(XMLParser.utf8_encode(Strings.trim(strval(matches.getValue(1)))));
            }
        }

        return returnData;
    }

    /**
     * Returns suggestions of for a specific word.
     * @param {String} $lang Language code like sv or en.
     * @param {String} $word Specific word to get suggestions for.
     * @return {Array} Array of suggestions for the specified word.
     */
    public Array<String> getSuggestions(String lang, String word) {
        String cmd = null;
        int fh = 0;
        String data = null;
        Array<Object> returnData = new Array<Object>();
        Array<Object> dataArr = new Array<Object>();
        Array matches = new Array();
        String dstr = null;
        Array<String> words = new Array<String>();
        int i = 0;
        cmd = this._getCMD(lang);

        if (true)/*Modified by Numiton*/
         {
            word = QMultibyte.mb_convert_encoding(word, "ISO-8859-1", JMultibyte.mb_detect_encoding(word, "UTF-8"));
        } else {
            word = XMLParser.utf8_encode(word);
        }

        if (booleanval(fh = FileSystemOrSocket.fopen(gVars.webEnv, this._tmpfile, "w"))) {
            FileSystemOrSocket.fwrite(gVars.webEnv, fh, "!\n");
            FileSystemOrSocket.fwrite(gVars.webEnv, fh, "^" + word + "\n");
            FileSystemOrSocket.fclose(gVars.webEnv, fh);
        } else {
            this.throwError("Error opening tmp file.");
        }

        data = QProgramExecution.shell_exec(cmd);
        JFileSystemOrSocket.unlink(gVars.webEnv, this._tmpfile);
        returnData = new Array<Object>();
        dataArr = QRegExPerl.preg_split("/\n/", data, -1, RegExPerl.PREG_SPLIT_NO_EMPTY);

        for (Map.Entry javaEntry491 : dataArr.entrySet()) {
            dstr = strval(javaEntry491.getValue());
            matches = new Array<String>();

			// Skip this line.
            if (strictEqual(Strings.strpos(dstr, "@"), 0)) {
                continue;
            }

            QRegExPerl.preg_match("/\\&[^:]+:(.*)/i", dstr, matches);

            if (!empty(matches.getValue(1))) {
                words = Array.array_slice(Strings.explode(",", strval(matches.getValue(1))), 0, 10);

                for (i = 0; i < Array.count(words); i++)
                    words.putValue(i, Strings.trim(words.getValue(i)));

                return words;
            }
        }

        return new Array<String>();
    }

    public String _getCMD(Object lang) {
        this._tmpfile = FileSystemOrSocket.tempnam(gVars.webEnv, strval(this._config.getValue("PSpellShell.tmp")), "tinyspell");

        if (QRegExPerl.preg_match("#win#i", Options.php_uname())) {
            return strval(this._config.getValue("PSpellShell.aspell")) + " -a --lang=" + strval(lang) + " --encoding=utf-8 -H < " + this._tmpfile + " 2>&1";
        }

        return "cat " + this._tmpfile + " | " + strval(this._config.getValue("PSpellShell.aspell")) + " -a --encoding=utf-8 -H --lang=" + strval(lang);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
