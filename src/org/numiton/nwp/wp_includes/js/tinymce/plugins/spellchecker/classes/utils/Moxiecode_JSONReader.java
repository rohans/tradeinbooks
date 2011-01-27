/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Moxiecode_JSONReader.java,v 1.3 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils;

import static com.numiton.VarHandling.*;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.string.Strings;


public class Moxiecode_JSONReader implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Moxiecode_JSONReader.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String _data;
    public int _len;
    public int _pos;
    public String _value;
    public int _token;
    public int _location;
    public Array<Object> _lastLocations = new Array<Object>();
    public boolean _needProp;
    public Array<Integer> _lastLocation = new Array<Integer>();

    public Moxiecode_JSONReader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String data) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this._data = data;
        this._len = Strings.strlen(data);
        this._pos = -1;
        this._location = gConsts.getJSON_IN_BETWEEN();
        this._lastLocations = new Array<Object>();
        this._needProp = false;
    }

    public int getToken() {
        return this._token;
    }

    public int getLocation() {
        return this._location;
    }

    public String getTokenName() {
        {
            int javaSwitchSelector70 = 0;

            if (equal(this._token, gConsts.getJSON_BOOL())) {
                javaSwitchSelector70 = 1;
            }

            if (equal(this._token, gConsts.getJSON_INT())) {
                javaSwitchSelector70 = 2;
            }

            if (equal(this._token, gConsts.getJSON_STR())) {
                javaSwitchSelector70 = 3;
            }

            if (equal(this._token, gConsts.getJSON_FLOAT())) {
                javaSwitchSelector70 = 4;
            }

            if (equal(this._token, gConsts.getJSON_NULL())) {
                javaSwitchSelector70 = 5;
            }

            if (equal(this._token, gConsts.getJSON_START_OBJ())) {
                javaSwitchSelector70 = 6;
            }

            if (equal(this._token, gConsts.getJSON_END_OBJ())) {
                javaSwitchSelector70 = 7;
            }

            if (equal(this._token, gConsts.getJSON_START_ARRAY())) {
                javaSwitchSelector70 = 8;
            }

            if (equal(this._token, gConsts.getJSON_END_ARRAY())) {
                javaSwitchSelector70 = 9;
            }

            if (equal(this._token, gConsts.getJSON_KEY())) {
                javaSwitchSelector70 = 10;
            }

            switch (javaSwitchSelector70) {
            case 1:return "JSON_BOOL";

            case 2:return "JSON_INT";

            case 3:return "JSON_STR";

            case 4:return "JSON_FLOAT";

            case 5:return "JSON_NULL";

            case 6:return "JSON_START_OBJ";

            case 7:return "JSON_END_OBJ";

            case 8:return "JSON_START_ARRAY";

            case 9:return "JSON_END_ARRAY";

            case 10:return "JSON_KEY";
            }
        }

        return "UNKNOWN";
    }

    public String getValue() {
        return this._value;
    }

    public boolean readToken() {
        String chr = null;
        chr = this.read();

        if (!equal(chr, null)) { {
                int javaSwitchSelector71 = 0;

                if (equal(chr, "[")) {
                    javaSwitchSelector71 = 1;
                }

                if (equal(chr, "]")) {
                    javaSwitchSelector71 = 2;
                }

                if (equal(chr, "{")) {
                    javaSwitchSelector71 = 3;
                }

                if (equal(chr, "}")) {
                    javaSwitchSelector71 = 4;
                }

				// String
                if (equal(chr, "\"")) {
                    javaSwitchSelector71 = 5;
                }

                if (equal(chr, "\'")) {
                    javaSwitchSelector71 = 6;
                }

				// Null
                if (equal(chr, "n")) {
                    javaSwitchSelector71 = 7;
                }

				// Bool
                if (equal(chr, "t")) {
                    javaSwitchSelector71 = 8;
                }

                if (equal(chr, "f")) {
                    javaSwitchSelector71 = 9;
                }

                switch (javaSwitchSelector71) {
                case 1: {
                    this._lastLocation.putValue(this._location);
                    this._location = gConsts.getJSON_IN_ARRAY();
                    this._token = gConsts.getJSON_START_ARRAY();
                    this._value = null;
                    this.readAway();

                    return true;
                }

                case 2: {
                    this._location = Array.array_pop(this._lastLocation);
                    this._token = gConsts.getJSON_END_ARRAY();
                    this._value = null;
                    this.readAway();

                    if (equal(this._location, gConsts.getJSON_IN_OBJECT())) {
                        this._needProp = true;
                    }

                    return true;
                }

                case 3: {
                    this._lastLocation.putValue(this._location);
                    this._location = gConsts.getJSON_IN_OBJECT();
                    this._needProp = true;
                    this._token = gConsts.getJSON_START_OBJ();
                    this._value = null;
                    this.readAway();

                    return true;
                }

                case 4: {
                    this._location = Array.array_pop(this._lastLocation);
                    this._token = gConsts.getJSON_END_OBJ();
                    this._value = null;
                    this.readAway();

                    if (equal(this._location, gConsts.getJSON_IN_OBJECT())) {
                        this._needProp = true;
                    }

                    return true;
                }

                case 5: {
                }

                case 6:return this._readString(chr);

                case 7:return this._readNull();

                case 8: {
                }

                case 9:return this._readBool(chr);

                default: {
					// Is number
                    if (is_numeric(chr) || equal(chr, "-") || equal(chr, ".")) {
                        return this._readNumber(chr);
                    }

                    return true;
                }
                }
            }
        }

        return false;
    }

    public boolean _readBool(String chr) {
        this._token = gConsts.getJSON_BOOL();
        this._value = strval(equal(chr, "t"));

        if (equal(chr, "t")) {
            this.skip(3); // rue
        } else {
            this.skip(4); // alse
        }

        this.readAway();

        if (equal(this._location, gConsts.getJSON_IN_OBJECT()) && !this._needProp) {
            this._needProp = true;
        }

        return true;
    }

    public boolean _readNull() {
        this._token = gConsts.getJSON_NULL();
        this._value = null;
        
        this.skip(3); // ull
        this.readAway();

        if (equal(this._location, gConsts.getJSON_IN_OBJECT()) && !this._needProp) {
            this._needProp = true;
        }

        return true;
    }

    public boolean _readString(String quote) {
        String output = null;
        boolean endString = false;
        String chr = null;
        output = "";
        this._token = gConsts.getJSON_STR();
        endString = false;

        while (!equal(chr = this.peek(), -1)) {
            {
                int javaSwitchSelector72 = 0;

				// Read away slash
                if (equal(chr, "\\")) {
                    javaSwitchSelector72 = 1;
                }

                if (equal(chr, "\'")) {
                    javaSwitchSelector72 = 2;
                }

                if (equal(chr, "\"")) {
                    javaSwitchSelector72 = 3;
                }

                switch (javaSwitchSelector72) {
                case 1: {
					// Read away slash
                    this.read();
                    
					// Read escape code
                    chr = this.read();

                    {
                        int javaSwitchSelector73 = 0;

                        if (equal(chr, "t")) {
                            javaSwitchSelector73 = 1;
                        }

                        if (equal(chr, "b")) {
                            javaSwitchSelector73 = 2;
                        }

                        if (equal(chr, "f")) {
                            javaSwitchSelector73 = 3;
                        }

                        if (equal(chr, "r")) {
                            javaSwitchSelector73 = 4;
                        }

                        if (equal(chr, "n")) {
                            javaSwitchSelector73 = 5;
                        }

                        if (equal(chr, "u")) {
                            javaSwitchSelector73 = 6;
                        }

                        switch (javaSwitchSelector73) {
                        case 1: {
                            output = output + "\t";

                            break;
                        }

                        case 2: {
                            output = output + "\\b";

                            break;
                        }

                        case 3: {
                            output = output + "\\f";

                            break;
                        }

                        case 4: {
                            output = output + "\r";

                            break;
                        }

                        case 5: {
                            output = output + "\n";

                            break;
                        }

                        case 6: {
                            output = output + this._int2utf8(Math.hexdec(this.read(4)));

                            break;
                        }

                        default: {
                            output = output + chr;

                            break;
                        }
                        }
                    }

                    break;
                }

                case 2: {
                }

                case 3: {
                    if (equal(chr, quote)) {
                        endString = true;
                    }

                    chr = this.read();

                    if (!equal(chr, -1) && !equal(chr, quote)) {
                        output = output + chr;
                    }

                    break;
                }

                default:output = output + this.read();
                }
            }

			// String terminated
            if (endString) {
                break;
            }
        }

        this.readAway();
        this._value = output;

		// Needed a property
        if (this._needProp) {
            this._token = gConsts.getJSON_KEY();
            this._needProp = false;

            return true;
        }

        if (equal(this._location, gConsts.getJSON_IN_OBJECT()) && !this._needProp) {
            this._needProp = true;
        }

        return true;
    }

    public String _int2utf8(int _int) {
        _int = _int;

        {
            int javaSwitchSelector74 = 0;

            if (equal(_int, 0)) {
                javaSwitchSelector74 = 1;
            }

            if (equal(_int, _int & 127)) {
                javaSwitchSelector74 = 2;
            }

            if (equal(_int, _int & 2047)) {
                javaSwitchSelector74 = 3;
            }

            if (equal(_int, _int & 65535)) {
                javaSwitchSelector74 = 4;
            }

            if (equal(_int, _int & 2097151)) {
                javaSwitchSelector74 = 5;
            }

            switch (javaSwitchSelector74) {
            case 1:return Strings.chr(0);

            case 2:return Strings.chr(_int);

            case 3:return Strings.chr(192 | ((_int >> 6) & 31)) + Strings.chr(128 | (_int & 63));

            case 4:return Strings.chr(224 | ((_int >> 12) & 15)) + Strings.chr(128 | ((_int >> 6) & 63)) + Strings.chr(128 | (_int & 63));

            case 5:return Strings.chr(240 | (_int >> 18)) + Strings.chr(128 | ((_int >> 12) & 63)) + Strings.chr(128 | ((_int >> 6) & 63)) + Strings.chr(128 | (_int & 63));
            }
        }

        return "";
    }

    public boolean _readNumber(String start) {
        String value = null;
        boolean isFloat = false;
        String chr = null;
        value = "";
        isFloat = false;
        this._token = gConsts.getJSON_INT();
        value = value + start;

        while (!equal(chr = this.peek(), -1)) {
            if (is_numeric(chr) || equal(chr, "-") || equal(chr, ".")) {
                if (equal(chr, ".")) {
                    isFloat = true;
                }

                value = value + this.read();
            } else {
                break;
            }
        }

        this.readAway();

        if (isFloat) {
            this._token = gConsts.getJSON_FLOAT();
            this._value = value;
        } else {
            this._value = value;
        }

        if (equal(this._location, gConsts.getJSON_IN_OBJECT()) && !this._needProp) {
            this._needProp = true;
        }

        return true;
    }

    public void readAway() {
        Object chr = null;

        while (!equal(chr = this.peek(), null)) {
            if (!equal(chr, ":") && !equal(chr, ",") && !equal(chr, " ")) {
                return;
            }

            this.read();
        }
    }

    public String read() {
        return read(1);
    }

    public String read(int len) {
        String str = null;

        if (this._pos < this._len) {
            if (len > 1) {
                str = Strings.substr(this._data, this._pos + 1, len);
                this._pos = this._pos + len;

                return str;
            } else {
                return Strings.getCharAt(this._data, ++this._pos);
            }
        }

        return null;
    }

    public void skip(int len) {
        this._pos = this._pos + len;
    }

    public String peek() {
        if (this._pos < this._len) {
            return Strings.getCharAt(this._data, this._pos + 1);
        }

        return null;
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
