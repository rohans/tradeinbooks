/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Filesystem_ftpsockets.java,v 1.5 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.PhpCommonConstants.*;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_includes.*;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.ftp.FTP;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class WP_Filesystem_ftpsockets extends WP_Filesystem implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Filesystem_ftpsockets.class.getName());


    public ftp_sockets ftp;
    public int timeout = 5;
    public Array<Object> options = new Array<Object>();
    public String wp_base = "";
    public int permission = 0;
    public Array<Integer> filetypes = new Array<Integer>(new ArrayEntry<Integer>("php", FTP.FTP_ASCII), new ArrayEntry<Integer>("css", FTP.FTP_ASCII), new ArrayEntry<Integer>("txt", FTP.FTP_ASCII),
            new ArrayEntry<Integer>("js", FTP.FTP_ASCII), new ArrayEntry<Integer>("html", FTP.FTP_ASCII), new ArrayEntry<Integer>("htm", FTP.FTP_ASCII), new ArrayEntry<Integer>("xml", FTP.FTP_ASCII),
            new ArrayEntry<Integer>("jpg", FTP.FTP_BINARY), new ArrayEntry<Integer>("png", FTP.FTP_BINARY), new ArrayEntry<Integer>("gif", FTP.FTP_BINARY),
            new ArrayEntry<Integer>("bmp", FTP.FTP_BINARY));

    public WP_Filesystem_ftpsockets(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Array<Object> opt) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.errors = new WP_Error(gVars, gConsts);

		//Check if possible to use ftp functions.
        if (!booleanval( /* Condensed dynamic construct */
                    includeOnce(gVars, gConsts, Class_ftpPage.class))) {
            return;
        }

        this.ftp = new ftp_sockets(javaGlobalVariables, javaGlobalConstants);

		//Set defaults:
        if (empty(opt.getValue("port"))) {
            this.options.putValue("port", 21);
        } else {
            this.options.putValue("port", opt.getValue("port"));
        }

        if (empty(opt.getValue("hostname"))) {
            this.errors.add("empty_hostname", getIncluded(L10nPage.class, gVars, gConsts).__("FTP hostname is required"));
        } else {
            this.options.putValue("hostname", opt.getValue("hostname"));
        }

        if (isset(opt.getValue("base")) && !empty(opt.getValue("base"))) {
            this.wp_base = strval(opt.getValue("base"));
        }

		// Check if the options provided are OK.
        if (empty(opt.getValue("username"))) {
            this.errors.add("empty_username", getIncluded(L10nPage.class, gVars, gConsts).__("FTP username is required"));
        } else {
            this.options.putValue("username", opt.getValue("username"));
        }

        if (empty(opt.getValue("password"))) {
            this.errors.add("empty_password", getIncluded(L10nPage.class, gVars, gConsts).__("FTP password is required"));
        } else {
            this.options.putValue("password", opt.getValue("password"));
        }
    }

    public boolean connect() {
        if (!booleanval(this.ftp)) {
            return false;
        }

		//$this->ftp->Verbose = true;
        
        if (!this.ftp.SetServer(strval(this.options.getValue("hostname")), intval(this.options.getValue("port")))) {
            this.errors.add(
                "connect",
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Failed to connect to FTP Server %1$s:%2$s"), this.options.getValue("hostname"), this.options.getValue("port")));

            return false;
        }

        if (!this.ftp.connect()) {
            this.errors.add(
                "connect",
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Failed to connect to FTP Server %1$s:%2$s"), this.options.getValue("hostname"), this.options.getValue("port")));

            return false;
        }

        if (!this.ftp.login(this.options.getValue("username"), this.options.getValue("password"))) {
            this.errors.add("auth", QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Username/Password incorrect for %s"), this.options.getValue("username")));

            return false;
        }

        this.ftp.SetType(gConsts.getFTP_AUTOASCII());
        this.ftp.Passive(true);

        return true;
    }

    public void setDefaultPermissions(int perm) {
        this.permission = perm;
    }

    public String find_base_dir() {
        return find_base_dir("", false, false);
    }

    public String find_base_dir(String base) {
        return find_base_dir(base, false, false);
    }

    public String find_base_dir(String base, boolean echo) {
        return find_base_dir(base, echo, false);
    }

    public String find_base_dir(String base, boolean echo, boolean loop) {
        String abspath = null;
        Array<Object> mat = new Array<Object>();
        int location = 0;
        String newbase = null;
        Array files = new Array();
        Array<String> arrPath = new Array<String>();
        String key = null;
        String folder = null;
        String ret = null;
        
		//Sanitize the Windows path formats, This allows easier conparison and aligns it to FTP output.
        abspath = Strings.str_replace("\\", "/", gConsts.getABSPATH()); //windows: Straighten up the paths..

        if (BOOLEAN_FALSE != Strings.strpos(abspath, ":")) { //Windows, Strip out the driveletter
            if (QRegExPerl.preg_match("|.{1}\\:(.+)|i", abspath, mat)) {
                abspath = strval(mat.getValue(1));
            }
        }

		//Set up the base directory (Which unless specified, is the current one)
        if (empty(base) || equal(".", base)) {
            base = this.cwd();
        }

        base = FormattingPage.trailingslashit(base);
        
		//Can we see the Current directory as part of the ABSPATH?
        location = Strings.strpos(abspath, base);

        if (!strictEqual(BOOLEAN_FALSE, location)) {
            newbase = getIncluded(FunctionsPage.class, gVars, gConsts).path_join(base, Strings.substr(abspath, location + Strings.strlen(base)));

            if (!strictEqual(false, this.chdir(newbase))) { //chdir sometimes returns null under certain circumstances, even when its changed correctly, FALSE will be returned if it doesnt change correctly.
                if (echo) {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Changing to %s") + "<br/>", newbase);
                }
				//Check to see if it exists in that folder.
                if (this.exists(newbase + "wp-settings.php")) {
                    if (echo) {
                        QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Found %s"), newbase + "wp-settings.php<br/>");
                    }

                    return newbase;
                }
            }
        }

		//Ok, Couldnt do a magic location from that particular folder level
		
		//Get a list of the files in the current directory, See if we can locate where we are in the folder stucture.
        files = this.dirlist(base);
        
        arrPath = Strings.explode("/", abspath);

        for (Map.Entry javaEntry127 : arrPath.entrySet()) {
            key = strval(javaEntry127.getValue());

			//Working from /home/ to /user/ to /wordpress/ see if that file exists within the current folder, 
			// If its found, change into it and follow through looking for it. 
			// If it cant find WordPress down that route, it'll continue onto the next folder level, and see if that matches, and so on.
			// If it reaches the end, and still cant find it, it'll return false for the entire function.
            if (isset(files.getValue(key))) {
				//Lets try that folder:
                folder = getIncluded(FunctionsPage.class, gVars, gConsts).path_join(base, key);

                if (echo) {
                    QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Changing to %s") + "<br/>", folder);
                }

                ret = this.find_base_dir(folder, echo, loop);

                if (booleanval(ret)) {
                    return ret;
                }
            }
        }
		//Only check this as a last resort, to prevent locating the incorrect install. All above proceeedures will fail quickly if this is the right branch to take.
        if (isset(files.getValue("wp-settings.php"))) {
            if (echo) {
                QStrings.printf(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("Found %s"), base + "wp-settings.php<br/>");
            }

            return base;
        }

        if (loop) {
            return "";//Prevent tihs function looping again.
        }

		//As an extra last resort, Change back to / if the folder wasnt found. This comes into effect when the CWD is /home/user/ but WP is at /var/www/.... mainly dedicated setups.
        return this.find_base_dir("/", echo, true);
    }

    public String get_base_dir(String base, boolean echo) {
        if (gConsts.isFTP_BASEDefined()) {
            this.wp_base = gConsts.getFTP_BASE();
        }

        if (empty(this.wp_base)) {
            this.wp_base = this.find_base_dir(base, echo);
        }

        return this.wp_base;
    }

    public String get_contents(String file) {
        return get_contents(file, 0, 0);
    }

    public String get_contents(String file, int type) {
        return get_contents(file, type, 0);
    }

    public String get_contents(String file, int type, int resumepos) {
        String extension = null;
        int temp = 0;
        String contents = null;

        if (!this.exists(file)) {
            return STRING_FALSE;
        }

        if (empty(type)) {
            extension = Strings.substr(Strings.strrchr(file, "."), 1);
            type = (isset(this.filetypes.getValue(extension))
                ? this.filetypes.getValue(extension)
                : gConsts.getFTP_AUTOASCII());
        }

        this.ftp.SetType(type);
        temp = FileSystemOrSocket.tmpfile(gVars.webEnv);

        if (!booleanval(temp)) {
            return STRING_FALSE;
        }

        if (!booleanval(this.ftp.fget(temp, file))) {
            FileSystemOrSocket.fclose(gVars.webEnv, temp);

            return ""; //Blank document, File does exist, Its just blank.
        }

        FileSystemOrSocket.fseek(gVars.webEnv, temp, 0); //Skip back to the start of the file being written to
        contents = "";

        while (!FileSystemOrSocket.feof(gVars.webEnv, temp))
            contents = contents + FileSystemOrSocket.fread(gVars.webEnv, temp, 8192);

        FileSystemOrSocket.fclose(gVars.webEnv, temp);

        return contents;
    }

    public Array<String> get_contents_array(String file) {
        return Strings.explode("\n", this.get_contents(file));
    }

    public boolean put_contents(String file, String contents) {
        return put_contents(file, contents, "");
    }

    public boolean put_contents(String file, String contents, String type) {
        return put_contents(file, contents, 0, type);
    }

    public boolean put_contents(String file, String contents, int mode, String type) {
        String extension = null;
        int temp = 0;
        boolean ret = false;

        if (empty(type)) {
            extension = Strings.substr(Strings.strrchr(file, "."), 1);
            type = strval(isset(this.filetypes.getValue(extension))
                    ? this.filetypes.getValue(extension)
                    : FTP.FTP_ASCII);
        }

        this.ftp.SetType(intval(type));
        temp = FileSystemOrSocket.tmpfile(gVars.webEnv);

        if (!booleanval(temp)) {
            return false;
        }

        FileSystemOrSocket.fwrite(gVars.webEnv, temp, contents);
        FileSystemOrSocket.fseek(gVars.webEnv, temp, 0); //Skip back to the start of the file being written to
        ret = this.ftp.fput(file, temp);
        FileSystemOrSocket.fclose(gVars.webEnv, temp);

        return ret;
    }

    public String cwd() {
        String cwd = null;
        cwd = this.ftp.pwd();

        if (booleanval(cwd)) {
            cwd = FormattingPage.trailingslashit(cwd);
        }

        return cwd;
    }

    public boolean chdir(String file) {
        return this.ftp.chdir(file);
    }

    public boolean chgrp(String file, Object group) {
        return chgrp(file, group, false);
    }

    public boolean chgrp(String file, Object group, boolean recursive) {
        return false;
    }

    public int chmod(String file, int mode) {
        return chmod(file, mode, false);
    }

    public int chmod(String file, int mode, boolean recursive) {
        Array<?> filelist = new Array();
        String filename = null;

        if (!booleanval(mode)) {
            mode = this.permission;
        }

        if (!booleanval(mode)) {
            return 0;
        }
		//if( ! $this->exists($file) )
		//	return false;
        if (!recursive || !this.is_dir(file)) {
            return intval(this.ftp.chmod(file, mode));
        }
		//Is a directory, and we want recursive
        filelist = this.dirlist(file);

        for (Map.Entry javaEntry128 : filelist.entrySet()) {
            filename = strval(javaEntry128.getValue());
            this.chmod(file + "/" + filename, mode, recursive);
        }

        return 1;
    }

    public boolean chown(String file, Object owner) {
        return chown(file, owner, false);
    }

    public boolean chown(String file, Object owner, boolean recursive) {
        return false;
    }

    public Object owner(String file) {
        Array dir = new Array();
        dir = this.dirlist(file);

        return dir.getArrayValue(file).getValue("owner");
    }

    public int getchmod(String file) {
        Array dir = new Array();
        dir = this.dirlist(file);

        return intval(dir.getArrayValue(file).getValue("permsn"));
    }

    public String gethchmod(String file) {
        Object perms = null;
        String info = null;
        
		//From the PHP.net page for ...?
        perms = this.getchmod(file);

        if (equal(intval(perms) & 49152, 49152)) {
			// Socket
            info = "s";
        } else if (equal(intval(perms) & 40960, 40960)) {
			// Symbolic Link
            info = "l";
        } else if (equal(intval(perms) & 32768, 32768)) {
			// Regular
            info = "-";
        } else if (equal(intval(perms) & 24576, 24576)) {
			// Block special
            info = "b";
        } else if (equal(intval(perms) & 16384, 16384)) {
			// Directory
            info = "d";
        } else if (equal(intval(perms) & 8192, 8192)) {
			// Character special
            info = "c";
        } else if (equal(intval(perms) & 4096, 4096)) {
			// FIFO pipe
            info = "p";
        } else {
			// Unknown
            info = "u";
        }

		// Owner
        info = info + (booleanval(intval(perms) & 256) ? "r" : "-");
		info = info + (booleanval(intval(perms) & 128) ? "w" : "-");
		info = info + (booleanval(intval(perms) & 64) ? (booleanval(intval(perms) & 2048) ? "s" : "x") : (booleanval(intval(perms) & 2048) ? "S" : "-"));
		
		// Group
		info = info + (booleanval(intval(perms) & 32) ? "r" : "-");
		info = info + (booleanval(intval(perms) & 16) ? "w" : "-");
		info = info + (booleanval(intval(perms) & 8) ? (booleanval(intval(perms) & 1024) ? "s" : "x") : (booleanval(intval(perms) & 1024) ? "S" : "-"));
		
		// World
		info = info + (booleanval(intval(perms) & 4) ? "r" : "-");
		info = info + (booleanval(intval(perms) & 2) ? "w" : "-");
		info = info + (booleanval(intval(perms) & 1) ? (booleanval(intval(perms) & 512) ? "t" : "x") : (booleanval(intval(perms) & 512) ? "T" : "-"));

        return info;
    }

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

    public Object group(String file) {
        Array dir = new Array();
        dir = this.dirlist(file);

        return dir.getArrayValue(file).getValue("group");
    }

    public boolean copy(String source, String destination, boolean overwrite) {
        String content = null;

        if (!overwrite && this.exists(destination)) {
            return false;
        }

        content = this.get_contents(source);

        if (strictEqual(STRING_FALSE, content)) {
            return false;
        }

        return this.put_contents(destination, content);
    }

    public boolean move(String source, String destination, boolean overwrite) {
        return this.ftp.rename(source, destination);
    }

    public boolean delete(String file) {
        return delete(file, false);
    }

    public boolean delete(String file, boolean recursive) {
        if (this.is_file(file)) {
            return this.ftp.delete(file);
        }

        if (!recursive) {
            return this.ftp.rmdir(file);
        }

        return this.ftp.mdel(file);
    }

    public boolean exists(String file) {
        return this.ftp.is_exists(file);
    }

    public boolean is_file(String file) {
        return this.is_dir(file)
        ? false
        : true;
    }

    public boolean is_dir(String path) {
        String cwd = null;
        cwd = this.cwd();

        if (this.chdir(path)) {
            this.chdir(cwd);

            return true;
        }

        return false;
    }

    public boolean is_readable(String file) {
		//Get dir list, Check if the file is writable by the current user??
        return true;
    }

    public boolean is_writable(String file) {
		//Get dir list, Check if the file is writable by the current user??
        return true;
    }

    public int atime(String file) {
        return 0;
    }

    public int mtime(String file) {
        return this.ftp.mdtm(file);
    }

    public int size(String file) {
        return this.ftp.filesize(file);
    }

    public boolean touch(String file, int time, int atime) {
        return false;
    }

    public boolean mkdir(String path, int chmod, Object chown, Object chgrp) {
        if (!this.ftp.mkdir(path)) {
            return false;
        }

        if (booleanval(chmod)) {
            this.chmod(path, chmod);
        }

        if (booleanval(chown)) {
            this.chown(path, chown);
        }

        if (booleanval(chgrp)) {
            this.chgrp(path, chgrp);
        }

        return true;
    }

    public boolean rmdir(String path, boolean recursive) {
        if (!recursive) {
            return this.ftp.rmdir(path);
        }

        return this.ftp.mdel(path);
    }

    public Array dirlist(String path) {
        return dirlist(path, false, false);
    }

    public Array dirlist(String path, boolean incdot) {
        return dirlist(path, incdot, false);
    }

    public Array dirlist(String path, boolean incdot, boolean recursive) {
        String limitFile = null;
        Array<Object> list = null;
        Array ret = new Array();
        Array<Object> struc = new Array<Object>();

        if (this.is_file(path)) {
            limitFile = FileSystemOrSocket.basename(path);
            path = FileSystemOrSocket.dirname(path) + "/";
        } else {
            limitFile = "";
        }

        list = this.ftp.dirlist(path);

        if (!booleanval(list)) {
            return new Array();
        }

        if (empty(list)) {
            return new Array();
        }

        ret = new Array();

        for (Map.Entry javaEntry129 : list.entrySet()) {
            struc = (Array<Object>) javaEntry129.getValue();

            if (equal("d", struc.getValue("type"))) {
                struc.putValue("files", new Array<Object>());

                if (incdot) {
					//We're including the doted starts
                    if (!equal(".", struc.getValue("name")) && !equal("..", struc.getValue("name"))) { //Ok, It isnt a special folder
                        if (recursive) {
                            struc.putValue("files", this.dirlist(path + "/" + struc.getValue("name"), incdot, recursive));
                        }
                    }
                } else { //No dots
                    if (recursive) {
                        struc.putValue("files", this.dirlist(path + "/" + struc.getValue("name"), incdot, recursive));
                    }
                }
            }
			//File
            ret.putValue(struc.getValue("name"), struc);
        }

        return ret;
    }

    public void __destruct() {
        this.ftp.quit();
    }

    protected void finalize() throws Throwable {
        __destruct();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
