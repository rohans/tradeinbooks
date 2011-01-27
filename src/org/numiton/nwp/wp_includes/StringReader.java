/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: StringReader.java,v 1.3 2008/10/03 18:45:30 numiton Exp $
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

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.generic.ContextCarrierInterface;
import com.numiton.string.Strings;


public class StringReader extends StreamReader implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(StringReader.class.getName());

    public int _pos;
    public String _str;

    public StringReader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String str) {
        super(javaGlobalVariables, javaGlobalConstants);
        this._str = str;
        this._pos = 0;
    }

    public String read(int bytes) {
        String data = null;
        data = Strings.substr(this._str, this._pos, bytes);
        this._pos = this._pos + bytes;

        if (Strings.strlen(this._str) < this._pos) {
            this._pos = Strings.strlen(this._str);
        }

        return data;
    }

    public int seekto(int pos) {
        this._pos = pos;

        if (Strings.strlen(this._str) < this._pos) {
            this._pos = Strings.strlen(this._str);
        }

        return this._pos;
    }

    public int currentpos() {
        return this._pos;
    }

    public int length() {
        return Strings.strlen(this._str);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
