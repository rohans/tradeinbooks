/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CachedFileReader.java,v 1.3 2008/10/03 18:45:29 numiton Exp $
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

import static com.numiton.VarHandling.booleanval;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ContextCarrierInterface;


//Preloads entire file in memory first, then creates a StringReader
//over it (it assumes knowledge of StringReader internals)
public class CachedFileReader extends StringReader implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(CachedFileReader.class.getName());

    public int error;

    public CachedFileReader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String filename) {
        super(javaGlobalVariables, javaGlobalConstants, null);

        int length = 0;
        int fd = 0;

        if (FileSystemOrSocket.file_exists(gVars.webEnv, filename)) {
            length = FileSystemOrSocket.filesize(gVars.webEnv, filename);
            fd = FileSystemOrSocket.fopen(gVars.webEnv, filename, "rb");

            if (!booleanval(fd)) {
                this.error = 3; // Cannot read file, probably permissions

                return;
            }

            this._str = FileSystemOrSocket.fread(gVars.webEnv, fd, length);
            FileSystemOrSocket.fclose(gVars.webEnv, fd);
        } else {
            this.error = 2; // File doesn't exist

            return;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
