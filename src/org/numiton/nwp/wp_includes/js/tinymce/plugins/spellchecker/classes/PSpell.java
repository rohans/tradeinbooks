/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PSpell.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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

import com.numiton.Unsupported;
import com.numiton.array.Array;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


/**
 * * $Id: PSpell.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
 *
 * @author Moxiecode
 * @copyright Copyright © 2004-2007, Moxiecode Systems AB, All rights reserved.
 *
 */
public class PSpell extends SpellChecker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(PSpell.class.getName());

    public Array<Object> _config = new Array<Object>();

    public PSpell(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        super(javaGlobalVariables, javaGlobalConstants, null);
    }

    public PSpell(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object config) {
        super(javaGlobalVariables, javaGlobalConstants, config);
    }

    /**
     * * Spellchecks an array of words.
     *
     * @param {String} $lang Language code like sv or en.
     * @param {Array} $words Array of words to spellcheck.
     * @return {Array} Array of misspelled words.
     *
     */
    public Array<Object> checkWords(String lang, Array<String> words) {
        int plink = 0;
        Array<Object> outWords = new Array<Object>();
        String word = null;
        plink = this._getPLink(lang);
        outWords = new Array<Object>();

        for (Map.Entry javaEntry488 : words.entrySet()) {
            word = strval(javaEntry488.getValue());

            if (!Unsupported.pspell_check(plink, Strings.trim(word))) {
                outWords.putValue(XMLParser.utf8_encode(word));
            }
        }

        return outWords;
    }

    /**
     * * Returns suggestions of for a specific word.
     *
     * @param {String} $lang Language code like sv or en.
     * @param {String} $word Specific word to get suggestions for.
     * @return {Array} Array of suggestions for the specified word.
     *
     */
    public Array<Object> getSuggestions(String lang, String word) {
        Array<Object> words = new Array<Object>();
        int i = 0;
        words = Unsupported.pspell_suggest(this._getPLink(lang), word);

        for (i = 0; i < Array.count(words); i++)
            words.putValue(i, XMLParser.utf8_encode(strval(words.getValue(i))));

        return words;
    }

    /**
     * * Opens a link for pspell.
     *
     */
    public int _getPLink(String lang) {
        int plink;

        // Modified by Numiton
		// Check for native PSpell support
        if (true) {
            this.throwError("PSpell support not found in PHP installation.");
        }

		// Setup PSpell link
        plink = Unsupported.pspell_new(
                lang,
                strval(this._config.getValue("PSpell.spelling")),
                strval(this._config.getValue("PSpell.jargon")),
                strval(this._config.getValue("PSpell.encoding")),
                intval(this._config.getValue("PSpell.mode")));

		// Setup PSpell link
        /*		if (!$plink) {
        			$pspellConfig = pspell_config_create(
        				$lang,
        				$this->_config['PSpell.spelling'],
        				$this->_config['PSpell.jargon'],
        				$this->_config['PSpell.encoding']
        			);

        			$plink = pspell_new_config($pspell_config);
        		}*/
        
        if (!booleanval(plink)) {
            this.throwError("No PSpell link found opened.");
        }

        return plink;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
