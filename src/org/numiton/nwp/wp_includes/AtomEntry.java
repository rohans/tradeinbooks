/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: AtomEntry.java,v 1.3 2008/10/03 18:45:30 numiton Exp $
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


/**
 * Structure that store Atom Entry Properties
 *
 * @package AtomLib
 *
 */
public class AtomEntry extends AtomBase implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(AtomEntry.class.getName());

    public AtomEntry(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
