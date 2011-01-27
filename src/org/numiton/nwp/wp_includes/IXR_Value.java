/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_Value.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.ClassHandling;
import com.numiton.array.Array;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.string.Strings;


/**
 * IXR - The Inutio XML-RPC Library
 *
 * @package IXR
 * @since 1.5
 *
 * @copyright Incutio Ltd 2002-2005
 * @version 1.7 (beta) 23rd May 2005
 * @author Simon Willison
 * @link http://scripts.incutio.com/xmlrpc/ Site
 * @link http://scripts.incutio.com/xmlrpc/manual.php Manual
 * @license BSD License http://www.opensource.org/licenses/bsd-license.php
 */
public class IXR_Value implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_Value.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Object data;

    /* Do not change type */ public String type;

    public IXR_Value(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object data) {
        this(javaGlobalVariables, javaGlobalConstants, data, "");
    }

    public IXR_Value(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object data, String type) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        Object key = null;
        Object value = null;
        int i = 0;
        int j = 0;
        this.data = data;

        if (!booleanval(type)) {
            type = this.calculateType();
        }

        this.type = type;

        if (equal(type, "struct")) {
            Array<Object> tmpData = (Array<Object>) data;

            /* Turn all the values in the array in to new IXR_Value objects */
            for (Map.Entry javaEntry402 : tmpData.entrySet()) {
                key = javaEntry402.getKey();
                value = javaEntry402.getValue();
                tmpData.putValue(key, new IXR_Value(gVars, gConsts, value));
            }
        }

        if (equal(type, "array")) {
            {
                Array<Object> tmpData = (Array<Object>) data;
                i = 0;
                j = Array.count(tmpData);

                for (; i < j; i++) {
                    tmpData.putValue(i, new IXR_Value(gVars, gConsts, tmpData.getValue(i)));
                }
            }
        }
    }

    public String calculateType() {
        if (strictEqual(this.data, true) || strictEqual(this.data, false)) {
            return "boolean";
        }

        if (is_integer(this.data)) {
            return "int";
        }

        if (is_double(this.data)) {
            return "double";
        }

        // Deal with IXR object types base64 and date
        if (is_object(this.data) && ClassHandling.is_a(this.data, "IXR_Date")) {
            return "date";
        }

        if (is_object(this.data) && ClassHandling.is_a(this.data, "IXR_Base64")) {
            return "base64";
        }

        // If it is a normal PHP object convert it in to a struct
        if (is_object(this.data)) {
            this.data = ClassHandling.get_object_vars(this.data);

            return "struct";
        }

        if (!is_array(this.data)) {
            return "string";
        }

        /* We have an array - is it an array or a struct ? */
        if (this.isStruct((Array<Object>) this.data)) {
            return "struct";
        } else {
            return "array";
        }
    }

    public String getXml() {
        String _return = null;
        Object item = null;
        String name = null;
        Object value = null;

        /* Return XML for this value */
        {
            int javaSwitchSelector43 = 0;

            if (equal(this.type, "boolean")) {
                javaSwitchSelector43 = 1;
            }

            if (equal(this.type, "int")) {
                javaSwitchSelector43 = 2;
            }

            if (equal(this.type, "double")) {
                javaSwitchSelector43 = 3;
            }

            if (equal(this.type, "string")) {
                javaSwitchSelector43 = 4;
            }

            if (equal(this.type, "array")) {
                javaSwitchSelector43 = 5;
            }

            if (equal(this.type, "struct")) {
                javaSwitchSelector43 = 6;
            }

            if (equal(this.type, "date")) {
                javaSwitchSelector43 = 7;
            }

            if (equal(this.type, "base64")) {
                javaSwitchSelector43 = 8;
            }

            switch (javaSwitchSelector43) {
            case 1:return "<boolean>" + (booleanval(this.data)
                ? "1"
                : "0") + "</boolean>";

            case 2:return "<int>" + strval(this.data) + "</int>";

            case 3:return "<double>" + strval(this.data) + "</double>";

            case 4:return "<string>" + Strings.htmlspecialchars(strval(this.data)) + "</string>";

            case 5: {
                _return = "<array><data>" + "\n";

                for (Map.Entry javaEntry403 : ((Array<Object>) this.data).entrySet()) {
                    item = javaEntry403.getValue();
                    _return = _return + "  <value>" + ((IXR_Value) item).getXml() + "</value>\n";
                }

                _return = _return + "</data></array>";

                return _return;
            }

            case 6: {
                _return = "<struct>" + "\n";

                for (Map.Entry javaEntry404 : ((Array<Object>) this.data).entrySet()) {
                    name = strval(javaEntry404.getKey());
                    value = javaEntry404.getValue();
                    name = Strings.htmlspecialchars(name);
                    _return = _return + "  <member><name>" + name + "</name><value>";
                    _return = _return + ((IXR_Value) item).getXml() + "</value></member>\n";
                }

                _return = _return + "</struct>";

                return _return;
            }

            case 7: {
            }

            case 8:return ((IXR_Value) this.data).getXml();
            }
        }

        return "";
    }

    public boolean isStruct(Array<Object> array) {
        int expected = 0;
        Object key = null;
        Object value = null;
        
        /* Nasty function to check if an array is a struct or not */
        expected = 0;

        for (Map.Entry javaEntry405 : array.entrySet()) {
            key = javaEntry405.getKey();
            value = javaEntry405.getValue();

            if (!equal(key, expected)) {
                return true;
            }

            expected++;
        }

        return false;
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
