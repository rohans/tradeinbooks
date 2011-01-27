/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Filesystem.java,v 1.3 2008/10/03 18:45:31 numiton Exp $
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

import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_includes.WP_Error;

import com.numiton.array.Array;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;


public abstract class WP_Filesystem {

	public GlobalConsts gConsts;
    public GlobalVars gVars;
    
    public WP_Error errors;
    
    public abstract boolean connect();

    public abstract void setDefaultPermissions(int perm);

    public abstract String find_base_dir(String base, boolean echo);

    public String get_base_dir() {
        return get_base_dir(".", false);
    }

    public abstract String get_base_dir(String base, boolean echo);

    public abstract String get_contents(String file);

    public abstract Array<String> get_contents_array(String file);

    public boolean put_contents(String file, String contents) {
        return put_contents(file, contents, 0, "");
    }

    public abstract boolean put_contents(String file, String contents, int mode, String type);

    public abstract String cwd();

    public abstract boolean chdir(String dir);

    public abstract boolean chgrp(String file, Object group);

    public abstract boolean chgrp(String file, Object group, boolean recursive);

    public abstract int chmod(String file, int mode);

    public abstract int chmod(String file, int mode, boolean recursive);

    public abstract boolean chown(String file, Object owner);

    public abstract boolean chown(String file, Object owner, boolean recursive);

    public abstract Object owner(String file);

    public abstract int getchmod(String file);

    public abstract String gethchmod(String file);

    public abstract String getnumchmodfromh(String mode);

    public abstract Object group(String file);

    public abstract boolean copy(String source, String destination, boolean overwrite);

    public abstract boolean move(String source, String destination, boolean overwrite);

    public abstract boolean delete(String file);

    public abstract boolean delete(String file, boolean recursive);

    public abstract boolean exists(String file);

    public abstract boolean is_file(String file);

    public abstract boolean is_dir(String path);

    public abstract boolean is_readable(String file);

    public abstract boolean is_writable(String file);

    public abstract int atime(String file);

    public abstract int mtime(String file);

    public abstract int size(String file);

    public abstract boolean touch(String file, int time, int atime);

    public boolean mkdir(String path, int chmod) {
        return mkdir(path, chmod, "", "");
    }

    public abstract boolean mkdir(String path, int chmod, Object chown, Object chgrp);

    public abstract boolean rmdir(String path, boolean recursive);

    public abstract Array dirlist(String path);

    public abstract Array dirlist(String path, boolean incdot);

    public abstract Array dirlist(String path, boolean incdot, boolean recursive);
    
    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }
    

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }

}
