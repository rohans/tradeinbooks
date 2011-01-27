/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: SpellChecker.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.string.Strings;


/**
 * * $Id: SpellChecker.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
 *
 * @author Moxiecode
 * @copyright Copyright © 2004-2007, Moxiecode Systems AB, All rights reserved.
 *
 */
public class SpellChecker implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(SpellChecker.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Object _config;

    /**
     * * Constructor.
     *
     * @param $config Configuration name/value array.
     *
     */
    public SpellChecker(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object config) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this._config = config;
    }

    /**
     * * Simple loopback function everything that gets in will be send back.
     *
     * @param $args.. Arguments.
     * @return {Array} Array of all input arguments. args..
     */
    public Array<Object> loopback(Object... vargs) {
        // Modified by Numiton
        return FunctionHandling.func_get_args(vargs);
    }

    /**
     * * Spellchecks an array of words.
     *
     * @param {String} $lang Language code like sv or en.
     * @param {Array} $words Array of words to spellcheck.
     * @return {Array} Array of misspelled words.
     *
     */
    public Ref<Object> checkWords(Object lang, Ref<Object> words) {
        return words;
    }

    /**
     * * Returns suggestions of for a specific word.
     *
     * @param {String} $lang Language code like sv or en.
     * @param {String} $word Specific word to get suggestions for.
     * @return {Array} Array of suggestions for the specified word.
     *
     */
    public Array<Object> getSuggestions(Object lang, Object word) {
        return new Array<Object>();
    }

    /**
     * * Throws an error message back to the user. This will stop all execution.
     *
     * @param {String} $str Message to send back to user.
     *
     */
    public void throwError(String str) {
        System.exit("{\"result\":null,\"id\":null,\"error\":{\"errstr\":\"" + Strings.addslashes(gVars.webEnv, str) + "\",\"errfile\":\"\",\"errline\":null,\"errcontext\":\"\",\"level\":\"FATAL\"}}");
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
