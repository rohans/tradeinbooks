/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_Message.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


public class IXR_Message implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_Message.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String message;
    public Object messageType;  // methodCall / methodResponse / fault

    public int faultCode;
    public String faultString;
    public String methodName;
    public Array<Object> params = new Array<Object>();

    // Current variable stacks
    public Array<Object> _arraystructs = new Array<Object>();   // The stack used to keep track of the current array/struct
    public Array<Object> _arraystructstypes = new Array<Object>(); // Stack keeping track of if things are structs or array
    public Array<Object> _currentStructName = new Array<Object>();  // A stack as well
    public Object _param;
    public Object _value;
    public Object _currentTag;
    public String _currentTagContents;
    // The XML parser
    public int _parser;
    public Object currentTag;

    public IXR_Message(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String message) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.message = message;
    }

    public boolean parse() {
        // first remove the XML declaration
        this.message = QRegExPerl.preg_replace("/<\\?xml(.*)?\\?" + ">/", "", this.message);

        if (equal(Strings.trim(this.message), "")) {
            return false;
        }

        this._parser = XMLParser.xml_parser_create(gVars.webEnv);
        // Set XML parser to take the case of tags in to account
        XMLParser.xml_parser_set_option(gVars.webEnv, this._parser, XMLParser.XML_OPTION_CASE_FOLDING, false);
        // Set XML parser callback functions
        XMLParser.xml_set_object(gVars.webEnv, this._parser, this);
        XMLParser.xml_set_element_handler(gVars.webEnv, this._parser, new Callback("tag_open", this), new Callback("tag_close", this));
        XMLParser.xml_set_character_data_handler(gVars.webEnv, this._parser, new Callback("cdata", this));

        if (!booleanval(XMLParser.xml_parse(gVars.webEnv, this._parser, this.message))) {
            /* die(sprintf('XML error: %s at line %d',
            xml_error_string(xml_get_error_code($this->_parser)),
            xml_get_current_line_number($this->_parser))); */
            return false;
        }

        XMLParser.xml_parser_free(gVars.webEnv, this._parser);

        // Grab the error messages, if any
        if (equal(this.messageType, "fault")) {
            this.faultCode = intval(this.params.getArrayValue(0).getValue("faultCode"));
            this.faultString = strval(this.params.getArrayValue(0).getValue("faultString"));
        }

        return true;
    }

    public void tag_open(int parser, String tag, Array<Object> attr) {
        this._currentTagContents = "";
        this.currentTag = tag;
        
        {
            int javaSwitchSelector44 = 0;

            if (equal(tag, "methodCall")) {
                javaSwitchSelector44 = 1;
            }

            if (equal(tag, "methodResponse")) {
                javaSwitchSelector44 = 2;
            }

            if (equal(tag, "fault")) {
                javaSwitchSelector44 = 3;
            }

            if (equal(tag, "data")) {
                javaSwitchSelector44 = 4;
            }

            if (equal(tag, "struct")) {
                javaSwitchSelector44 = 5;
            }

            switch (javaSwitchSelector44) {
            case 1: {
            }

            case 2: {
            }

            case 3: {
                this.messageType = tag;

                break;
            }

            /* Deal with stacks of arrays and structs */
            case 4: {    // data is to all intents and puposes more interesting than array
                this._arraystructstypes.putValue("array");
                this._arraystructs.putValue(new Array<Object>());

                break;
            }

            case 5: {
                this._arraystructstypes.putValue("struct");
                this._arraystructs.putValue(new Array<Object>());

                break;
            }
            }
        }
    }

    public void cdata(int parser, String cdata) {
        this._currentTagContents = this._currentTagContents + strval(cdata);
    }

    public void tag_close(int parser, String tag) {
        boolean valueFlag = false;
        Object value = null;
        valueFlag = false;
        
        {
            int javaSwitchSelector45 = 0;

            if (equal(tag, "int")) {
                javaSwitchSelector45 = 1;
            }

            if (equal(tag, "i4")) {
                javaSwitchSelector45 = 2;
            }

            if (equal(tag, "double")) {
                javaSwitchSelector45 = 3;
            }

            if (equal(tag, "string")) {
                javaSwitchSelector45 = 4;
            }

            if (equal(tag, "dateTime.iso8601")) {
                javaSwitchSelector45 = 5;
            }

            if (equal(tag, "value")) {
                javaSwitchSelector45 = 6;
            }

            if (equal(tag, "boolean")) {
                javaSwitchSelector45 = 7;
            }

            if (equal(tag, "base64")) {
                javaSwitchSelector45 = 8;
            }

            if (equal(tag, "data")) {
                javaSwitchSelector45 = 9;
            }

            if (equal(tag, "struct")) {
                javaSwitchSelector45 = 10;
            }

            if (equal(tag, "member")) {
                javaSwitchSelector45 = 11;
            }

            if (equal(tag, "name")) {
                javaSwitchSelector45 = 12;
            }

            if (equal(tag, "methodName")) {
                javaSwitchSelector45 = 13;
            }

            switch (javaSwitchSelector45) {
            case 1: {
            }

            case 2: {
                value = intval(Strings.trim(this._currentTagContents));
                valueFlag = true;

                break;
            }

            case 3: {
                value = floatval(Strings.trim(this._currentTagContents));
                valueFlag = true;

                break;
            }

            case 4: {
                value = this._currentTagContents;
                valueFlag = true;

                break;
            }

            case 5: {
                value = new IXR_Date(gVars, gConsts, Strings.trim(this._currentTagContents));
                // $value = $iso->getTimestamp();
                valueFlag = true;

                break;
            }

            case 6: {
                // "If no type is indicated, the type is string."
                if (!equal(Strings.trim(this._currentTagContents), "")) {
                    value = strval(this._currentTagContents);
                    valueFlag = true;
                }

                break;
            }

            case 7: {
                value = Strings.trim(this._currentTagContents);
                valueFlag = true;

                break;
            }

            case 8: {
                value = URL.base64_decode(Strings.trim(this._currentTagContents));
                valueFlag = true;

                break;
            }

            /* Deal with stacks of arrays and structs */
            case 9:
            case 10: {
                value = Array.array_pop(this._arraystructs);
                Array.array_pop(this._arraystructstypes);
                valueFlag = true;

                break;
            }

            case 11: {
                Array.array_pop(this._currentStructName);

                break;
            }

            case 12: {
                this._currentStructName.putValue(Strings.trim(this._currentTagContents));

                break;
            }

            case 13: {
                this.methodName = Strings.trim(this._currentTagContents);

                break;
            }
            }
        }

        if (valueFlag) {
            if (Array.count(this._arraystructs) > 0) {
                // Add value to struct or array
                if (equal(this._arraystructstypes.getValue(Array.count(this._arraystructstypes) - 1), "struct")) {
                    // Add to struct
                    this._arraystructs.getArrayValue(Array.count(this._arraystructs) - 1).putValue(this._currentStructName.getValue(Array.count(this._currentStructName) - 1), value);
                } else {
                    // Add to array
                    this._arraystructs.getArrayValue(Array.count(this._arraystructs) - 1).putValue(value);
                }
            } else {
                // Just add as a paramater
                this.params.putValue(value);
            }
        }

        this._currentTagContents = "";
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
