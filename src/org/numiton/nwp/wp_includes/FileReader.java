/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FileReader.java,v 1.3 2008/10/03 18:45:30 numiton Exp $
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
import com.numiton.string.Strings;


public class FileReader extends StreamReader implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(FileReader.class.getName());
    
    public int _pos;
    public int _fd;
    public int _length;
    public int error;

    public FileReader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String filename) {
        super(javaGlobalVariables, javaGlobalConstants);

        if (FileSystemOrSocket.file_exists(gVars.webEnv, filename)) {
            this._length = FileSystemOrSocket.filesize(gVars.webEnv, filename);
            this._pos = 0;
            this._fd = FileSystemOrSocket.fopen(gVars.webEnv, filename, "rb");

            if (!booleanval(this._fd)) {
                this.error = 3; // Cannot read file, probably permissions

                return;
            }
        } else {
            this.error = 2; // File doesn't exist

            return;
        }
    }

    public String read(int bytes) {
        String chunk = null;
        String data = null;

        if (booleanval(bytes)) {
            FileSystemOrSocket.fseek(gVars.webEnv, this._fd, this._pos);

            // PHP 5.1.1 does not read more than 8192 bytes in one fread()
            // the discussions at PHP Bugs suggest it's the intended behaviour
            while (bytes > 0) {
                chunk = FileSystemOrSocket.fread(gVars.webEnv, this._fd, bytes);
                data = data + chunk;
                bytes = bytes - Strings.strlen(chunk);
            }

            this._pos = FileSystemOrSocket.ftell(gVars.webEnv, this._fd);

            /*, null*/
            return data;
        } else {
            return "";
        }
    }

    public int seekto(int pos) {
        FileSystemOrSocket.fseek(gVars.webEnv, this._fd, pos);
        this._pos = FileSystemOrSocket.ftell(gVars.webEnv, this._fd);

        /*, null*/
        return this._pos;
    }

    public int currentpos() {
        return this._pos;
    }

    public int length() {
        return this._length;
    }

    public void close() {
        FileSystemOrSocket.fclose(gVars.webEnv, this._fd);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
