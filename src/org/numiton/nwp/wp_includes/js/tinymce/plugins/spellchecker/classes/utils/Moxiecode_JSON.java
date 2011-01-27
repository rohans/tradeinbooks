/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Moxiecode_JSON.java,v 1.3 2008/10/03 18:45:31 numiton Exp $
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

import com.numiton.ClassHandling;
import com.numiton.Misc;
import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.*;
import com.numiton.string.Strings;


/**
 * This class handles JSON stuff.
 * @package MCManager.utils
 */
public class Moxiecode_JSON implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Moxiecode_JSON.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> data = new Array<Object>();
    public Array<Object> parents = new Array<Object>();
    public Array<Object> cur = new Array<Object>();

    public Moxiecode_JSON(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
    }

    public Object decode(String input) {
        Moxiecode_JSONReader reader = null;
        reader = new Moxiecode_JSONReader(gVars, gConsts, input);

        return this.readValue(reader);
    }

    public Object readValue(Moxiecode_JSONReader reader) {
        Object key = null;
        Object loc = null;
        Ref<Object> cur = new Ref<Object>();
        Ref<Object> obj = new Ref<Object>();
        this.data = new Array<Object>();
        this.parents = new Array<Object>();
        this.cur = this.data;
        key = null;
        loc = gConsts.getJSON_IN_ARRAY();

        while (reader.readToken()) {
            {
                int javaSwitchSelector75 = 0;

                if (equal(reader.getToken(), gConsts.getJSON_STR())) {
                    javaSwitchSelector75 = 1;
                }

                if (equal(reader.getToken(), gConsts.getJSON_INT())) {
                    javaSwitchSelector75 = 2;
                }

                if (equal(reader.getToken(), gConsts.getJSON_BOOL())) {
                    javaSwitchSelector75 = 3;
                }

                if (equal(reader.getToken(), gConsts.getJSON_FLOAT())) {
                    javaSwitchSelector75 = 4;
                }

                if (equal(reader.getToken(), gConsts.getJSON_NULL())) {
                    javaSwitchSelector75 = 5;
                }

                if (equal(reader.getToken(), gConsts.getJSON_KEY())) {
                    javaSwitchSelector75 = 6;
                }

                if (equal(reader.getToken(), gConsts.getJSON_START_OBJ())) {
                    javaSwitchSelector75 = 7;
                }

                if (equal(reader.getToken(), gConsts.getJSON_START_ARRAY())) {
                    javaSwitchSelector75 = 8;
                }

                if (equal(reader.getToken(), gConsts.getJSON_END_OBJ())) {
                    javaSwitchSelector75 = 9;
                }

                if (equal(reader.getToken(), gConsts.getJSON_END_ARRAY())) {
                    javaSwitchSelector75 = 10;
                }

                switch (javaSwitchSelector75) {
                case 1: {
                }

                case 2: {
                }

                case 3: {
                }

                case 4: {
                }

                case 5: {
                    {
                        int javaSwitchSelector76 = 0;

                        if (equal(reader.getLocation(), gConsts.getJSON_IN_OBJECT())) {
                            javaSwitchSelector76 = 1;
                        }

                        if (equal(reader.getLocation(), gConsts.getJSON_IN_ARRAY())) {
                            javaSwitchSelector76 = 2;
                        }

                        switch (javaSwitchSelector76) {
                        case 1: {
                            this.cur.putValue(key, reader.getValue());

                            break;
                        }

                        case 2: {
                            this.cur.putValue(reader.getValue());

                            break;
                        }

                        default:return reader.getValue();
                        }
                    }

                    break;
                }

                case 6: {
                    key = reader.getValue();

                    break;
                }

                case 7: {
                }

                case 8: {
                    if (equal(loc, gConsts.getJSON_IN_OBJECT())) {
                        this.addArray(key);
                    } else {
                        this.addArray(null);
                    }

                    cur = obj;
                    loc = reader.getLocation();

                    break;
                }

                case 9: {
                }

                case 10: {
                    loc = reader.getLocation();

                    if (Array.count(this.parents) > 0) {
                        this.cur = this.parents.getArrayValue(Array.count(this.parents) - 1);
                        Array.array_pop(this.parents);
                    }

                    break;
                }
                }
            }
        }

        return this.data.getValue(0);
    }

	// This method was needed since PHP is crapy and doesn't have pointers/references
    public void addArray(Object key) {
        Array<Object> ar = new Array<Object>();
        this.parents.putValue(this.cur);
        ar = new Array<Object>();

        if (booleanval(key)) {
            this.cur.putValue(key, ar);
        } else {
            this.cur.putValue(ar);
        }

        this.cur = ar;
    }

    public String getDelim(int index, Moxiecode_JSONReader reader) {
        {
            int javaSwitchSelector77 = 0;

            if (equal(reader.getLocation(), gConsts.getJSON_IN_ARRAY())) {
                javaSwitchSelector77 = 1;
            }

            if (equal(reader.getLocation(), gConsts.getJSON_IN_OBJECT())) {
                javaSwitchSelector77 = 2;
            }

            switch (javaSwitchSelector77) {
            case 1: {
            }

            case 2: {
                if (index > 0) {
                    return ",";
                }

                break;
            }
            }
        }

        return "";
    }

    public Object encode(Object input) {
        {
            int javaSwitchSelector78 = 0;

            if (equal(gettype(input), "boolean")) {
                javaSwitchSelector78 = 1;
            }

            if (equal(gettype(input), "integer")) {
                javaSwitchSelector78 = 2;
            }

            if (equal(gettype(input), "float")) {
                javaSwitchSelector78 = 3;
            }

            if (equal(gettype(input), "double")) {
                javaSwitchSelector78 = 4;
            }

            if (equal(gettype(input), "NULL")) {
                javaSwitchSelector78 = 5;
            }

            if (equal(gettype(input), "string")) {
                javaSwitchSelector78 = 6;
            }

            if (equal(gettype(input), "array")) {
                javaSwitchSelector78 = 7;
            }

            if (equal(gettype(input), "object")) {
                javaSwitchSelector78 = 8;
            }

            switch (javaSwitchSelector78) {
            case 1:return booleanval(input)
                ? "true"
                : "false";

            case 2:return intval(input);

            case 3: {
            }

            case 4:return floatval(input);

            case 5:return "null";

            case 6:return this.encodeString(strval(input));

            case 7:return this._encodeArray((Array<Object>) input);

            case 8:return this._encodeArray(ClassHandling.get_object_vars(input));
            }
        }

        return "";
    }

    public String encodeString(String input) {
		// Needs to be escaped
        String output;
        int _byte = 0;
        int i = 0;
        String _char = null;

        if (QRegExPerl.preg_match("/[^a-zA-Z0-9]/", input)) {
            output = "";

            for (i = 0; i < Strings.strlen(input); i++) {
                {
                    int javaSwitchSelector79 = 0;

                    if (equal(Strings.getCharAt(input, i), "\\b")) {
                        javaSwitchSelector79 = 1;
                    }

                    if (equal(Strings.getCharAt(input, i), "\t")) {
                        javaSwitchSelector79 = 2;
                    }

                    if (equal(Strings.getCharAt(input, i), "\\f")) {
                        javaSwitchSelector79 = 3;
                    }

                    if (equal(Strings.getCharAt(input, i), "\r")) {
                        javaSwitchSelector79 = 4;
                    }

                    if (equal(Strings.getCharAt(input, i), "\n")) {
                        javaSwitchSelector79 = 5;
                    }

                    if (equal(Strings.getCharAt(input, i), "\\")) {
                        javaSwitchSelector79 = 6;
                    }

                    if (equal(Strings.getCharAt(input, i), "\'")) {
                        javaSwitchSelector79 = 7;
                    }

                    if (equal(Strings.getCharAt(input, i), "\"")) {
                        javaSwitchSelector79 = 8;
                    }

                    switch (javaSwitchSelector79) {
                    case 1: {
                        output = output + "\\b";

                        break;
                    }

                    case 2: {
                        output = output + "\\t";

                        break;
                    }

                    case 3: {
                        output = output + "\\f";

                        break;
                    }

                    case 4: {
                        output = output + "\\r";

                        break;
                    }

                    case 5: {
                        output = output + "\\n";

                        break;
                    }

                    case 6: {
                        output = output + "\\\\";

                        break;
                    }

                    case 7: {
                        output = output + "\\\'";

                        break;
                    }

                    case 8: {
                        output = output + "\\\"";

                        break;
                    }

                    default: {
                        _byte = Strings.ord(Strings.getCharAt(input, i));

                        if (equal(_byte & 224, 192)) {
                            _char = QMisc.pack("C*", _byte, Strings.ord(Strings.getCharAt(input, i + 1)));
                            i = i + 1;
                            output = output + QStrings.sprintf("\\u%04s", Strings.bin2hex(this._utf82utf16(_char)));
                        }

                        if (equal(_byte & 240, 224)) {
                            _char = QMisc.pack("C*", _byte, Strings.ord(Strings.getCharAt(input, i + 1)), Strings.ord(Strings.getCharAt(input, i + 2)));
                            i = i + 2;
                            output = output + QStrings.sprintf("\\u%04s", Strings.bin2hex(this._utf82utf16(_char)));
                        }

                        if (equal(_byte & 248, 240)) {
                            _char = Misc.pack("C*", _byte, Strings.ord(Strings.getCharAt(input, i + 1)), Strings.ord(Strings.getCharAt(input, i + 2)), Strings.ord(Strings.getCharAt(input, i + 3)));
                            i = i + 3;
                            output = output + QStrings.sprintf("\\u%04s", Strings.bin2hex(this._utf82utf16(_char)));
                        }

                        if (equal(_byte & 252, 248)) {
                            _char = QMisc.pack(
                                    "C*",
                                    _byte,
                                    Strings.ord(Strings.getCharAt(input, i + 1)),
                                    Strings.ord(Strings.getCharAt(input, i + 2)),
                                    Strings.ord(Strings.getCharAt(input, i + 3)),
                                    Strings.ord(Strings.getCharAt(input, i + 4)));
                            i = i + 4;
                            output = output + QStrings.sprintf("\\u%04s", Strings.bin2hex(this._utf82utf16(_char)));
                        }

                        if (equal(_byte & 254, 252)) {
                            _char = QMisc.pack(
                                    "C*",
                                    _byte,
                                    Strings.ord(Strings.getCharAt(input, i + 1)),
                                    Strings.ord(Strings.getCharAt(input, i + 2)),
                                    Strings.ord(Strings.getCharAt(input, i + 3)),
                                    Strings.ord(Strings.getCharAt(input, i + 4)),
                                    Strings.ord(Strings.getCharAt(input, i + 5)));
                            i = i + 5;
                            output = output + QStrings.sprintf("\\u%04s", Strings.bin2hex(this._utf82utf16(_char)));
                        } else if (_byte < 128) {
                            output = output + Strings.getCharAt(input, i);
                        }
                    }
                    }
                }
            }

            return "\"" + output + "\"";
        }

        return "\"" + input + "\"";
    }

    public String _utf82utf16(String utf8) {
        if (true)/*Modified by Numiton*/
         {
            return QMultibyte.mb_convert_encoding(utf8, "UTF-16", "UTF-8");
        }

        switch (Strings.strlen(utf8)) {
        case 1:return utf8;

        case 2:return Strings.chr(7 & (Strings.ord(Strings.getCharAt(utf8, 0)) >> 2)) +
            Strings.chr((192 & (Strings.ord(Strings.getCharAt(utf8, 0)) << 6)) | (63 & Strings.ord(Strings.getCharAt(utf8, 1))));

        case 3:return Strings.chr((240 & (Strings.ord(Strings.getCharAt(utf8, 0)) << 4)) | (15 & (Strings.ord(Strings.getCharAt(utf8, 1)) >> 2))) +
            Strings.chr((192 & (Strings.ord(Strings.getCharAt(utf8, 1)) << 6)) | (127 & Strings.ord(Strings.getCharAt(utf8, 2))));
        }

        return "";
    }

    public String _encodeArray(Array<Object> input) {
        Object output = null;
        boolean isIndexed;
        Array<Object> keys = new Array<Object>();
        int i = 0;
        output = "";
        isIndexed = true;
        keys = Array.array_keys(input);

        for (i = 0; i < Array.count(keys); i++) {
            if (!is_int(keys.getValue(i))) {
                output = strval(output) + this.encodeString(strval(keys.getValue(i))) + ":" + strval(this.encode(input.getValue(keys.getValue(i))));
                isIndexed = false;
            } else {
                output = strval(output) + strval(this.encode(input.getValue(keys.getValue(i))));
            }

            if (!equal(i, Array.count(keys) - 1)) {
                output = strval(output) + ",";
            }
        }

        return isIndexed
        ? ("[" + output + "]")
        : ("{" + output + "}");
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
