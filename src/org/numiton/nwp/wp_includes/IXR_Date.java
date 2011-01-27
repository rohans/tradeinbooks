/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_Date.java,v 1.2 2008/10/03 18:45:29 numiton Exp $
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

import com.numiton.DateTime;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.string.Strings;


public class IXR_Date implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(IXR_Date.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String year;
    public String month;
    public String day;
    public String hour;
    public String minute;
    public String second;
    public String timezone;

    public IXR_Date(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String time) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        // $time can be a PHP timestamp or an ISO one
        if (is_numeric(time)) {
            this.parseTimestamp(intval(time));
        } else {
            this.parseIso(time);
        }
    }

    public void parseTimestamp(int timestamp) {
        this.year = DateTime.date("Y", timestamp);
        this.month = DateTime.date("m", timestamp);
        this.day = DateTime.date("d", timestamp);
        this.hour = DateTime.date("H", timestamp);
        this.minute = DateTime.date("i", timestamp);
        this.second = DateTime.date("s", timestamp);
    }

    public void parseIso(String iso) {
        this.year = Strings.substr(iso, 0, 4);
        this.month = Strings.substr(iso, 4, 2);
        this.day = Strings.substr(iso, 6, 2);
        this.hour = Strings.substr(iso, 9, 2);
        this.minute = Strings.substr(iso, 12, 2);
        this.second = Strings.substr(iso, 15, 2);
        this.timezone = Strings.substr(iso, 17);
    }

    public String getIso() {
        return this.year + this.month + this.day + "T" + this.hour + ":" + this.minute + ":" + this.second + this.timezone;
    }

    public String getXml() {
        return "<dateTime.iso8601>" + this.getIso() + "</dateTime.iso8601>";
    }

    public int getTimestamp() {
        return DateTime.mktime(intval(this.hour), intval(this.minute), intval(this.second), intval(this.month), intval(this.day), intval(this.year));
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
