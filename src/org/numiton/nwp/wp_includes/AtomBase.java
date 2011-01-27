/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: AtomBase.java,v 1.2 2008/10/03 09:42:44 numiton Exp $
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

import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;

/**
 * Added by Numiton. Base class for AtomFeed and AtomEntry.
 *
 * <p><i>Created on Jun 25, 2008</i></p>
 * <p><code>$Id: AtomBase.java,v 1.2 2008/10/03 09:42:44 numiton Exp $</code></p>
 * @author robert
 */
public class AtomBase {

	public GlobalConsts gConsts;
    public GlobalVars gVars;
	
	/**
     * * Stores Links
     * 
     * @var array
     * @access public
     * 
     */
    public Array<Object> links = new Array<Object>();
	/**
     * * Stores Categories
     * 
     * @var array
     * @access public
     * 
     */
    public Array<Object> categories = new Array<Object>();

    // Added by Numiton
    public Array title = new Array();
    public Array subtitle = new Array();
    public Array content = new Array();
    public Array summary = new Array();
    public Array rights = new Array();
    public Object id;
    public String published;
    public String updated;
    public String draft;
    
	public AtomBase() {
		super();
	}

	public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }
	
	public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}