/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ImportPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.ClassesPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/ImportPage")
@Scope("request")
public class ImportPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ImportPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/includes/import.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/_import";
    }

    public int functionCreate_docmp(Object a, Object b) {
        return Strings.strcmp(strval(((Array) a).getValue(0)), strval(((Array) b).getValue(0)));
    }

    public Array<Object> get_importers() {
        if (is_array(gVars.wp_importers)) {
            Array.uasort(gVars.wp_importers, new Callback("functionCreate_docmp", this));
        }

        return gVars.wp_importers;
    }

    public Object register_importer(String id, String name, Object description, Object callback) {
        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(callback)) {
            return callback;
        }

        gVars.wp_importers.putValue(id, new Array<Object>(new ArrayEntry<Object>(name), new ArrayEntry<Object>(description), new ArrayEntry<Object>(callback)));

        return 0;
    }

    public void wp_import_cleanup(int id) {
        getIncluded(PostPage.class, gVars, gConsts).wp_delete_attachment(id);
    }

    public Array<Object> wp_import_handle_upload() {
        Array<Object> overrides = new Array<Object>();
        Array<Object> file = new Array<Object>();
        Object url = null;
        Object type = null;
        String filename = null;
        Array<Object> object = new Array<Object>();
        int id = 0;
        overrides = new Array<Object>(new ArrayEntry<Object>("test_form", false), new ArrayEntry<Object>("test_type", false));
        gVars.webEnv._FILES.getArrayValue("import").putValue("name", strval(gVars.webEnv._FILES.getArrayValue("import").getValue("name")) + ".import");
        file = getIncluded(FilePage.class, gVars, gConsts).wp_handle_upload(gVars.webEnv._FILES.getArrayValue("import"), overrides);

        if (isset(file.getValue("error"))) {
            return file;
        }

        url = file.getValue("url");
        type = file.getValue("type");

        String fileStr = Strings.addslashes(gVars.webEnv, strval(file.getValue("file")));
        filename = FileSystemOrSocket.basename(fileStr);
        
        // Construct the object array
        object = new Array<Object>(
                new ArrayEntry<Object>("post_title", filename),
                new ArrayEntry<Object>("post_content", url),
                new ArrayEntry<Object>("post_mime_type", type),
                new ArrayEntry<Object>("guid", url));
        
        // Save the data
        id = getIncluded(PostPage.class, gVars, gConsts).wp_insert_attachment(object, fileStr, 0);

        return new Array<Object>(new ArrayEntry<Object>("file", fileStr), new ArrayEntry<Object>("id", id));
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
