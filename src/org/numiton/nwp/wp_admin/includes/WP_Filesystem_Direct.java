/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Filesystem_Direct.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin.includes;

import static com.numiton.PhpCommonConstants.STRING_FALSE;
import static com.numiton.VarHandling.*;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_includes.FormattingPage;
import org.numiton.nwp.wp_includes.WP_Error;

import com.numiton.DateTime;
import com.numiton.Unsupported;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.Directory;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


public class WP_Filesystem_Direct extends WP_Filesystem implements ContextCarrierInterface, Serializable, Cloneable {
    private static final long serialVersionUID = -3286577715357168871L;
    protected static final Logger LOG = Logger.getLogger(WP_Filesystem_Direct.class.getName());
    
    public int permission;

    public WP_Filesystem_Direct(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Array<Object> arg) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.errors = new WP_Error(gVars, gConsts);
        this.permission = JFileSystemOrSocket.umask();
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#connect()
     */
    public boolean connect() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#setDefaultPermissions(int)
     */
    public void setDefaultPermissions(int perm) {
        this.permission = perm;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#find_base_dir(java.lang.String, java.lang.Object)
     */
    public String find_base_dir(String base, boolean echo) {
        return Strings.str_replace("\\", "/", gConsts.getABSPATH());
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#get_base_dir(java.lang.String, java.lang.Object)
     */
    public String get_base_dir(String base, boolean echo) {
        return this.find_base_dir(base, echo);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#get_contents(java.lang.String)
     */
    public String get_contents(String file) {
        return FileSystemOrSocket.file_get_contents(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#get_contents_array(java.lang.String)
     */
    public Array<String> get_contents_array(String file) {
        return FileSystemOrSocket.file(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#put_contents(java.lang.String, java.lang.String, int, java.lang.String)
     */
    public boolean put_contents(String file, String contents, int mode, String type) {
        int fp = 0;

        if (!booleanval(fp = FileSystemOrSocket.fopen(gVars.webEnv, file, "w" + type))) {
            return false;
        }

        FileSystemOrSocket.fwrite(gVars.webEnv, fp, contents);
        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        this.chmod(file, mode);

        return true;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#cwd()
     */
    public String cwd() {
        return Directories.getcwd(gVars.webEnv);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chdir(java.lang.String)
     */
    public boolean chdir(String dir) {
        return Directories.chdir(gVars.webEnv, dir);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chgrp(java.lang.String, java.lang.Object)
     */
    public boolean chgrp(String file, Object group) {
        return chgrp(file, group, false);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chgrp(java.lang.String, java.lang.Object, boolean)
     */
    public boolean chgrp(String file, Object group, boolean recursive) {
        Array<?> filelist = new Array();
        String filename = null;

        if (!this.exists(file)) {
            return false;
        }

        if (!recursive) {
            return FileSystemOrSocket.chgrp(gVars.webEnv, file, group);
        }

        if (!this.is_dir(file)) {
            return FileSystemOrSocket.chgrp(gVars.webEnv, file, group);
        }

        //Is a directory, and we want recursive
        file = FormattingPage.trailingslashit(file);
        filelist = this.dirlist(file);

        for (Map.Entry javaEntry117 : filelist.entrySet()) {
            filename = strval(javaEntry117.getValue());
            this.chgrp(file + filename, group, recursive);
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chmod(java.lang.String, int)
     */
    public int chmod(String file, int mode) {
        return chmod(file, mode, false);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chmod(java.lang.String, int, boolean)
     */
    public int chmod(String file, int mode, boolean recursive) {
        Array<?> filelist = new Array();
        String filename = null;

        if (!booleanval(mode)) {
            mode = this.permission;
        }

        if (!this.exists(file)) {
            return 0;
        }

        if (!recursive) {
            return intval(JFileSystemOrSocket.chmod(gVars.webEnv, file, mode));
        }

        if (!this.is_dir(file)) {
            return intval(JFileSystemOrSocket.chmod(gVars.webEnv, file, mode));
        }

        //Is a directory, and we want recursive
        file = FormattingPage.trailingslashit(file);
        filelist = this.dirlist(file);

        for (Map.Entry javaEntry118 : filelist.entrySet()) {
            filename = strval(javaEntry118.getValue());
            this.chmod(file + filename, mode, recursive);
        }

        return 1;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chown(java.lang.String, java.lang.Object)
     */
    public boolean chown(String file, Object owner) {
        return chown(file, owner, false);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#chown(java.lang.String, java.lang.Object, boolean)
     */
    public boolean chown(String file, Object owner, boolean recursive) {
        Array<?> filelist = new Array();
        String filename = null;

        if (!this.exists(file)) {
            return false;
        }

        if (!recursive) {
            return FileSystemOrSocket.chown(gVars.webEnv, file, owner);
        }

        if (!this.is_dir(file)) {
            return FileSystemOrSocket.chown(gVars.webEnv, file, owner);
        }

        //Is a directory, and we want recursive
        filelist = this.dirlist(file);

        for (Map.Entry javaEntry119 : filelist.entrySet()) {
            filename = strval(javaEntry119.getValue());
            this.chown(file + "/" + filename, owner, recursive);
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#owner(java.lang.String)
     */
    public Object owner(String file) {
        int owneruid = 0;
        Array<Object> ownerarray = new Array<Object>();
        owneruid = JFileSystemOrSocket.fileowner(gVars.webEnv, file);

        if (!booleanval(owneruid)) {
            return null;
        }

        if (true)/*Modified by Numiton*/
         {
            return owneruid;
        }

        ownerarray = Unsupported.posix_getpwuid(owneruid);

        return ownerarray.getValue("name");
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#getchmod(java.lang.String)
     */
    public int getchmod(String file) {
        return JFileSystemOrSocket.fileperms(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#gethchmod(java.lang.String)
     */
    public String gethchmod(String file) {
        int perms = 0;
        String info = null;
        
        //From the PHP.net page for ...?
        perms = this.getchmod(file);

        if (equal(perms & 49152, 49152)) {
        	// Socket
            info = "s";
        } else if (equal(perms & 40960, 40960)) {
        	// Symbolic Link
            info = "l";
        } else if (equal(perms & 32768, 32768)) {
        	// Regular
            info = "-";
        } else if (equal(perms & 24576, 24576)) {
        	// Block special
            info = "b";
        } else if (equal(perms & 16384, 16384)) {
        	// Directory
            info = "d";
        } else if (equal(perms & 8192, 8192)) {
        	// Character special
            info = "c";
        } else if (equal(perms & 4096, 4096)) {
        	// FIFO pipe
            info = "p";
        } else {
        	// Unknown
            info = "u";
        }

        // Owner
        info = info + (booleanval(perms & 256)
            ? "r"
            : "-");
        info = info + (booleanval(perms & 128)
            ? "w"
            : "-");
        info = info + (booleanval(perms & 64)
            ? (booleanval(perms & 2048)
            ? "s"
            : "x")
            : (booleanval(perms & 2048)
            ? "S"
            : "-"));
        
        // Group
        info = info + (booleanval(perms & 32)
            ? "r"
            : "-");
        info = info + (booleanval(perms & 16)
            ? "w"
            : "-");
        info = info + (booleanval(perms & 8)
            ? (booleanval(perms & 1024)
            ? "s"
            : "x")
            : (booleanval(perms & 1024)
            ? "S"
            : "-"));
        
        // World
        info = info + (booleanval(perms & 4)
            ? "r"
            : "-");
        info = info + (booleanval(perms & 2)
            ? "w"
            : "-");
        info = info + (booleanval(perms & 1)
            ? (booleanval(perms & 512)
            ? "t"
            : "x")
            : (booleanval(perms & 512)
            ? "T"
            : "-"));

        return info;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#getnumchmodfromh(java.lang.String)
     */
    public String getnumchmodfromh(String mode) {
        String realmode = null;
        Array<Object> legal = new Array<Object>();
        Array<Object> attarray = new Array<Object>();
        Object key = null;
        int i = 0;
        Array<Object> trans = new Array<Object>();
        String newmode = null;
        realmode = "";
        legal = new Array<Object>(new ArrayEntry<Object>(""), new ArrayEntry<Object>("w"), new ArrayEntry<Object>("r"), new ArrayEntry<Object>("x"), new ArrayEntry<Object>("-"));
        attarray = QRegExPerl.preg_split("//", mode);

        for (i = 0; i < Array.count(attarray); i++) {
            if (booleanval(key = Array.array_search(attarray.getValue(i), legal))) {
                realmode = realmode + strval(legal.getValue(key));
            }
        }

        mode = Strings.str_pad(realmode, 9, "-", 0);
        trans = new Array<Object>(new ArrayEntry<Object>("-", "0"), new ArrayEntry<Object>("r", "4"), new ArrayEntry<Object>("w", "2"), new ArrayEntry<Object>("x", "1"));
        mode = Strings.strtr(mode, trans);
        newmode = "";
        newmode = newmode + strval(intval(Strings.getCharAt(mode, 0)) + intval(Strings.getCharAt(mode, 1)) + intval(Strings.getCharAt(mode, 2)));
        newmode = newmode + strval(intval(Strings.getCharAt(mode, 3)) + intval(Strings.getCharAt(mode, 4)) + intval(Strings.getCharAt(mode, 5)));
        newmode = newmode + strval(intval(Strings.getCharAt(mode, 6)) + intval(Strings.getCharAt(mode, 7)) + intval(Strings.getCharAt(mode, 8)));

        return newmode;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#group(java.lang.String)
     */
    public Object group(String file) {
        int gid = 0;
        Array<Object> grouparray = new Array<Object>();
        gid = JFileSystemOrSocket.filegroup(gVars.webEnv, file);

        if (!booleanval(gid)) {
            return null;
        }

        if (true)/*Modified by Numiton*/
         {
            return gid;
        }

        grouparray = Unsupported.posix_getgrgid(gid);

        return grouparray.getValue("name");
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#copy(java.lang.String, java.lang.String, boolean)
     */
    public boolean copy(String source, String destination, boolean overwrite) {
        if (!overwrite && this.exists(destination)) {
            return false;
        }

        return FileSystemOrSocket.copy(gVars.webEnv, source, destination);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#move(java.lang.String, java.lang.String, boolean)
     */
    public boolean move(String source, String destination, boolean overwrite) {
    	
    	//Possible to use rename()?
        if (this.copy(source, destination, overwrite) && this.exists(destination)) {
            this.delete(source);

            return true;
        } else {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#delete(java.lang.String)
     */
    public boolean delete(String file) {
        return delete(file, false);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#delete(java.lang.String, boolean)
     */
    public boolean delete(String file, boolean recursive) {
        Object filelist;

        /* Do not change type */
        boolean retval = false;
        String filename = null;
        String fileinfo = null;
        
        file = Strings.str_replace("\\", "/", file); //for win32, occasional problems deleteing files otherwise

        if (this.is_file(file)) {
            return JFileSystemOrSocket.unlink(gVars.webEnv, file);
        }

        if (!recursive && this.is_dir(file)) {
            return FileSystemOrSocket.rmdir(gVars.webEnv, file);
        }

        //At this point its a folder, and we're in recursive mode
        file = FormattingPage.trailingslashit(file);
        filelist = this.dirlist(file, true);
        retval = true;

        if (is_array(filelist)) { //false if no files, So check first.
            for (Map.Entry javaEntry120 : ((Array<?>) filelist).entrySet()) {
                filename = strval(javaEntry120.getKey());
                fileinfo = strval(javaEntry120.getValue());

                if (!this.delete(file + filename, recursive)) {
                    retval = false;
                }
            }
        }

        if (!FileSystemOrSocket.rmdir(gVars.webEnv, file)) {
            return false;
        }

        return retval;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#exists(java.lang.String)
     */
    public boolean exists(String file) {
        return FileSystemOrSocket.file_exists(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#is_file(java.lang.String)
     */
    public boolean is_file(String file) {
        return FileSystemOrSocket.is_file(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#is_dir(java.lang.String)
     */
    public boolean is_dir(String path) {
        return FileSystemOrSocket.is_dir(gVars.webEnv, path);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#is_readable(java.lang.String)
     */
    public boolean is_readable(String file) {
        return FileSystemOrSocket.is_readable(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#is_writable(java.lang.String)
     */
    public boolean is_writable(String file) {
        return FileSystemOrSocket.is_writable(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#atime(java.lang.String)
     */
    public int atime(String file) {
        return JFileSystemOrSocket.fileatime(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#mtime(java.lang.String)
     */
    public int mtime(String file) {
        return FileSystemOrSocket.filemtime(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#size(java.lang.String)
     */
    public int size(String file) {
        return FileSystemOrSocket.filesize(gVars.webEnv, file);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#touch(java.lang.String, int, int)
     */
    public boolean touch(String file, int time, int atime) {
        if (equal(time, 0)) {
            time = DateTime.time();
        }

        if (equal(atime, 0)) {
            atime = DateTime.time();
        }

        return JFileSystemOrSocket.touch(gVars.webEnv, file, time, atime);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#mkdir(java.lang.String, int, java.lang.Object, java.lang.Object)
     */
    public boolean mkdir(String path, int chmod, Object chown, Object chgrp) {
        if (!booleanval(chmod)) {
            chmod = this.permission;
        }

        if (!JFileSystemOrSocket.mkdir(gVars.webEnv, path, chmod)) {
            return false;
        }

        if (booleanval(chown)) {
            this.chown(path, chown);
        }

        if (booleanval(chgrp)) {
            this.chgrp(path, chgrp);
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#rmdir(java.lang.String, boolean)
     */
    public boolean rmdir(String path, boolean recursive) {
        Array<?> filelist;
        String filename = null;
        Object det = null;

        //Currently unused and untested, Use delete() instead.
        if (!recursive) {
            return FileSystemOrSocket.rmdir(gVars.webEnv, path);
        }

        //recursive:
        filelist = this.dirlist(path);

        for (Map.Entry javaEntry121 : filelist.entrySet()) {
            filename = strval(javaEntry121.getKey());
            det = javaEntry121.getValue();

            if (equal("/", Strings.substr(filename, -1, 1))) {
                this.rmdir(path + "/" + filename, recursive);
            }

            FileSystemOrSocket.rmdir(gVars.webEnv, filename);
        }

        return FileSystemOrSocket.rmdir(gVars.webEnv, path);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#dirlist(java.lang.String)
     */
    public Array dirlist(String path) {
        return dirlist(path, false, false);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#dirlist(java.lang.String, boolean)
     */
    public Array dirlist(String path, boolean incdot) {
        return dirlist(path, incdot, false);
    }

    /* (non-Javadoc)
     * @see org.numiton.nwp.wp_admin.includes.WP_Filesystem#dirlist(java.lang.String, boolean, boolean)
     */
    public Array dirlist(String path, boolean incdot, boolean recursive) {
        String limitFile = null;
        Array ret;
        Directory dir = null;
        Array<Object> struc = new Array<Object>();
        String entry = null;

        if (this.is_file(path)) {
            limitFile = FileSystemOrSocket.basename(path);
            path = FileSystemOrSocket.dirname(path);
        } else {
            limitFile = strval(false);
        }

        if (!this.is_dir(path)) {
            return null;
        }

        ret = new Array();
        dir = Directories.dir(gVars.webEnv, path);

        while (!strictEqual(STRING_FALSE, entry = dir.read())) {
            struc = new Array<Object>();
            struc.putValue("name", entry);

            if (equal(".", struc.getValue("name")) || equal("..", struc.getValue("name"))) {
                continue; //Do not care about these folders.
            }

            if (equal(".", struc.getArrayValue("name").getValue(0)) && !incdot) {
                continue;
            }

            if (booleanval(limitFile) && !equal(struc.getValue("name"), limitFile)) {
                continue;
            }

            struc.putValue("perms", this.gethchmod(path + "/" + entry));
            struc.putValue("permsn", this.getnumchmodfromh(strval(struc.getValue("perms"))));
            struc.putValue("number", false);
            struc.putValue("owner", this.owner(path + "/" + entry));
            struc.putValue("group", this.group(path + "/" + entry));
            struc.putValue("size", this.size(path + "/" + entry));
            struc.putValue("lastmodunix", this.mtime(path + "/" + entry));
            struc.putValue("lastmod", DateTime.date("M j", intval(struc.getValue("lastmodunix"))));
            struc.putValue("time", DateTime.date("h:i:s", intval(struc.getValue("lastmodunix"))));
            struc.putValue("type", this.is_dir(path + "/" + entry)
                ? "d"
                : "f");

            if (equal("d", struc.getValue("type"))) {
                if (recursive) {
                    struc.putValue("files", this.dirlist(path + "/" + struc.getValue("name"), incdot, recursive));
                } else {
                    struc.putValue("files", new Array<Object>());
                }
            }

            ret.putValue(struc.getValue("name"), struc);
        }

        dir.close();
        dir = null;

        return ret;
    }

    public void __destruct() {
    }

    protected void finalize() throws Throwable {
        __destruct();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
