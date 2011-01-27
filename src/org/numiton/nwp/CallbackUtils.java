/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CallbackUtils.java,v 1.3 2008/10/14 14:23:04 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.VarHandling.*;

import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.string.Strings;

// Added by Numiton
public class CallbackUtils {
	public static String htmlTagToLowercase(Array matches) {
		return "<" + Strings.strtolower(strval(matches.getValue(1)));
	}
	
	public static String replaceChrHexDec(Array matches) {
		return Strings.chr(Math.hexdec(strval(matches.getValue(1))));
	}
	
	public static String replaceChr(Array matches) {
		return Strings.chr(intval(matches.getValue(1)));
	}
	
	public static String replaceBaseConvert(Array matches) {
		return "&#" + Math.base_convert(strval(matches.getValue(1)),16,10) + ";";
	}
	
	public static int strnatcasecmp(Object str1, Object str2) {
        return Strings.strnatcasecmp(strval(str1), strval(str2));
    }
}
