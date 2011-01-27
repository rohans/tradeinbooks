/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: StreamReader.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;


/**
 * PHP-Gettext External Library: StreamReader classes
 *
 * @package External
 * @subpackage PHP-gettext
 *
 * @internal
   Copyright (c) 2003, 2005 Danilo Segan <danilo@kvota.net>.

   This file is part of PHP-gettext.

   PHP-gettext is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.

   PHP-gettext is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with PHP-gettext; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

// Simple class to wrap file streams, string streams, etc.
// seek is essential, and it should be byte stream
public class StreamReader implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(StreamReader.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public StreamReader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
    }

    // should return a string [FIXME: perhaps return array of bytes?]
    public String read(int bytes) {
        return "";
    }

    // should return new position
    public int seekto(int position) {
        return 0;
    }

    // returns current position
    public int currentpos() {
        return 0;
    }

    // returns length of entire stream (limit for seekto()s)
    public int length() {
        return 0;
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
