/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FilePage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Options;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.java.JOptions;
import com.numiton.ntile.til.libraries.php.quercus.QFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class FilePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FilePage.class.getName());

    /**
     * Generated in place of local variable 'upload_error_handler' from method
     * 'wp_handle_upload' because it is used inside an inner class.
     */
    String wp_handle_upload_upload_error_handler = null;
    /**
     * Generated in place of local variable 'upload_error_strings' from method
     * 'wp_handle_upload' because it is used inside an inner class.
     */
    Array<Object> wp_handle_upload_upload_error_strings = new Array<Object>();
    /**
     * Generated in place of local variable 'uploads' from method
     * 'wp_handle_upload' because it is used inside an inner class.
     */
    Array<Object> wp_handle_upload_uploads = new Array<Object>();

    @Override
    @RequestMapping("/wp-admin/includes/file.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/file";
    }

    public String get_file_description(String file) {
        String template_data = null;
        Array<Object> name = new Array<Object>();

        if (isset(gVars.wp_file_descriptions.getValue(FileSystemOrSocket.basename(file)))) {
            return strval(gVars.wp_file_descriptions.getValue(FileSystemOrSocket.basename(file)));
        } else if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getABSPATH() + file) && FileSystemOrSocket.is_file(gVars.webEnv, gConsts.getABSPATH() + file)) {
            template_data = Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, gConsts.getABSPATH() + file));

            if (QRegExPerl.preg_match("|Template Name:(.*)|i", template_data, name)) {
                return strval(name.getValue(1));
            }
        }

        return FileSystemOrSocket.basename(file);
    }

    public String get_home_path() {
        String home = null;
        String home_path;
        String root;
        home = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home"));

        if (!equal(home, "") && !equal(home, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl"))) {
            Array home_pathArr = URL.parse_url(home);
            home_path = strval(home_pathArr.getValue("path"));
            root = Strings.str_replace(gVars.webEnv.getPhpSelf(), "", gVars.webEnv.getScriptFilename());
            home_path = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(root + home_path);
        } else {
            home_path = gConsts.getABSPATH();
        }

        return home_path;
    }

    public String get_real_file_to_edit(String file) {
        String real_file = null;

        if (equal("index.php", file) || equal(".htaccess", file)) {
            real_file = get_home_path() + file;
        } else {
            real_file = gConsts.getABSPATH() + file;
        }

        return real_file;
    }

    public String get_temp_dir() {
        String temp = null;

        if (gConsts.isWP_TEMP_DIRDefined()) {
            return getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(gConsts.getWP_TEMP_DIR());
        }

        temp = gConsts.getABSPATH() + "wp-content/";

        if (FileSystemOrSocket.is_dir(gVars.webEnv, temp) && FileSystemOrSocket.is_writable(gVars.webEnv, temp)) {
            return temp;
        }

        if (true) /*Modified by Numiton*/ {
            return getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(Options.sys_get_temp_dir());
        }

        return "/tmp/";
    }

    public int validate_file(String file, Array<String> allowed_files) {
        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(file, ".."))) {
            return 1;
        }

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(file, "./"))) {
            return 1;
        }

        if (equal(":", Strings.substr(file, 1, 1))) {
            return 2;
        }

        if (!empty(allowed_files) && !Array.in_array(file, allowed_files)) {
            return 3;
        }

        return 0;
    }

    public String validate_file_to_edit(String file, Array allowed_files) {
        int code = 0;
        file = Strings.stripslashes(gVars.webEnv, file);
        code = validate_file(file, allowed_files);

        if (!booleanval(code)) {
            return file;
        }

        switch (code) {
        case 1:
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "Sorry, can&#8217;t edit files with \"..\" in the name. If you are trying to edit a file in your nWordPress home directory, you can just type the name of the file in.",
                            "default"),
                    "");

        case 2:
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, can&#8217;t call files with their real path.", "default"), "");

        case 3:
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Sorry, that file cannot be edited.", "default"), "");
        }

        return "";
    }

    public Array<Object> wp_handle_upload_error(Array<Object> file, Object message) {
        return new Array<Object>(new ArrayEntry<Object>("error", message));
    }

 // array wp_handle_upload ( array &file [, array overrides] )
 // file: reference to a single element of $_FILES. Call the function once for each uploaded file.
 // overrides: an associative array of names=>values to override default variables with extract( $overrides, EXTR_OVERWRITE ).
 // On success, returns an associative array of file attributes.
 // On failure, returns $overrides['upload_error_handler'](&$file, $message ) or array( 'error'=>$message ).
    public Array<Object> wp_handle_upload(final Array<Object> file, Object overrides) /* Do not change type */ {
        String action = null;
        boolean test_form = false;
        boolean test_size = false;
        boolean test_type = false;
        Object mimes = null;
        Array<Object> wp_filetype = new Array<Object>();
        Object type = null;
        String ext = null;
        String filename = null;
        String unique_filename_callback = null;
        String new_file = null;
        Array<Object> stat = new Array<Object>();
        int perms = 0;
        String url = null;
        Array<Object> _return = null;

        // The default error handler.
        // Removed by Numiton. All functions are declared.
        
        // You may define your own function and pass the name in $overrides['upload_error_handler']
        wp_handle_upload_upload_error_handler = "wp_handle_upload_error";
        
        // $_POST['action'] must be set and its value must equal $overrides['action'] or this:
        action = "wp_handle_upload";
        
        // Courtesy of php.net, the strings that describe the error indicated in $_FILES[{form field}]['error'].
        wp_handle_upload_upload_error_strings = new Array<Object>(
                new ArrayEntry<Object>(false),
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("The uploaded file exceeds the <code>upload_max_filesize</code> directive in <code>php.ini</code>.", "default")),
                new ArrayEntry<Object>(
                    getIncluded(L10nPage.class, gVars, gConsts).__("The uploaded file exceeds the <em>MAX_FILE_SIZE</em> directive that was specified in the HTML form.", "default")),
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("The uploaded file was only partially uploaded.", "default")),
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("No file was uploaded.", "default")),
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Missing a temporary folder.", "default")),
                new ArrayEntry<Object>(getIncluded(L10nPage.class, gVars, gConsts).__("Failed to write file to disk.", "default")));
        
        // All tests are on by default. Most can be turned off by $override[{test_name}] = false;
        test_form = true;
        test_size = true;
        
        // If you override this, you must provide $ext and $type!!!!
        test_type = true;

        // Install user overrides. Did we mention that this voids your warranty?
        if (is_array(overrides)) {
            mimes = Array.extractVar((Array) overrides, "mimes", mimes, Array.EXTR_OVERWRITE);
            type = Array.extractVar((Array) overrides, "type", type, Array.EXTR_OVERWRITE);
            ext = strval(Array.extractVar((Array) overrides, "ext", ext, Array.EXTR_OVERWRITE));
            unique_filename_callback = strval(Array.extractVar((Array) overrides, "unique_filename_callback", unique_filename_callback, Array.EXTR_OVERWRITE));
            wp_handle_upload_upload_error_handler = strval(Array.extractVar((Array) overrides, "upload_upload_error_handler", wp_handle_upload_upload_error_handler, Array.EXTR_OVERWRITE));
            test_form = booleanval(Array.extractVar((Array) overrides, "test_form", test_form, Array.EXTR_OVERWRITE));

            // Added by Numiton
            if (!equal(wp_handle_upload_upload_error_handler, "wp_handle_upload_error")) {
                LOG.warn("Unsupported custom error handler: " + wp_handle_upload_upload_error_handler);
            }
        }

        // A correct form post will pass this test.
        if (test_form && (!isset(gVars.webEnv._POST.getValue("action")) || !equal(gVars.webEnv._POST.getValue("action"), action))) {
            return wp_handle_upload_error( /* Condensed dynamic construct: 121961 */
                file, getIncluded(L10nPage.class, gVars, gConsts).__("Invalid form submission.", "default"));
        }

        // A successful upload will pass this test. It makes no sense to override this one.
        if (intval(file.getValue("error")) > 0) {
            return wp_handle_upload_error( /* Condensed dynamic construct: 121989 */
                file, wp_handle_upload_upload_error_strings.getValue(file.getValue("error")));
        }

        // A non-empty file will pass this test.
        if (test_size && !(intval(file.getValue("size")) > 0)) {
            return wp_handle_upload_error( /* Condensed dynamic construct: 122016 */
                file,
                getIncluded(L10nPage.class, gVars, gConsts).__(
                    "File is empty. Please upload something more substantial. This error could also be caused by uploads being disabled in your php.ini.",
                    "default"));
        }

        // A properly uploaded file will pass this test. There should be no reason to override this one.
        if (!FileSystemOrSocket.is_uploaded_file(gVars.webEnv, strval(file.getValue("tmp_name")))) {
            return wp_handle_upload_error( /* Condensed dynamic construct: 122038 */
                file, getIncluded(L10nPage.class, gVars, gConsts).__("Specified file failed upload test.", "default"));
        }

        // A correct MIME type will pass this test. Override $mimes or use the upload_mimes filter.
        if (test_type) {
            wp_filetype = getIncluded(FunctionsPage.class, gVars, gConsts).wp_check_filetype(strval(file.getValue("name")), mimes);
            type = Array.extractVar(wp_filetype, "type", type, Array.EXTR_OVERWRITE);
            ext = strval(Array.extractVar(wp_filetype, "ext", ext, Array.EXTR_OVERWRITE));
            unique_filename_callback = strval(Array.extractVar(wp_filetype, "unique_filename_callback", unique_filename_callback, Array.EXTR_OVERWRITE));

            if ((!booleanval(type) || !booleanval(ext)) && !getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("unfiltered_upload")) {
                return wp_handle_upload_error( /* Condensed dynamic construct: 122085 */
                    file, getIncluded(L10nPage.class, gVars, gConsts).__("File type does not meet security guidelines. Try another.", "default"));
            }

            if (!booleanval(ext)) {
                ext = Strings.ltrim(Strings.strrchr(strval(file.getValue("name")), "."), ".");
            }

            if (!booleanval(type)) {
                type = file.getValue("type");
            }
        }

        // A writable uploads dir will pass this test. Again, there's no point overriding this one.
        if (!(booleanval(wp_handle_upload_uploads = getIncluded(FunctionsPage.class, gVars, gConsts).wp_upload_dir(null)) && equal(false, wp_handle_upload_uploads.getValue("error")))) {
            return wp_handle_upload_error( /* Condensed dynamic construct: 122161 */
                file, wp_handle_upload_uploads.getValue("error"));
        }

        filename = getIncluded(FunctionsPage.class, gVars, gConsts)
                       .wp_unique_filename(strval(wp_handle_upload_uploads.getValue("path")), strval(file.getValue("name")), new Callback(unique_filename_callback, this));
        
        // Move the file to the uploads dir
        new_file = strval(wp_handle_upload_uploads.getValue("path")) + "/" + filename;

        if (strictEqual(false, FileSystemOrSocket.move_uploaded_file(gVars.webEnv, strval(file.getValue("tmp_name")), new_file))) {
            return wp_handle_upload_error( /* Condensed dynamic construct: 122231 */
                file, QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("The uploaded file could not be moved to %s.", "default"), wp_handle_upload_uploads.getValue("path")));
        }

        // Set correct file permissions
        stat = QFileSystemOrSocket.stat(gVars.webEnv, FileSystemOrSocket.dirname(new_file));
        perms = intval(stat.getValue("mode")) & 0000666;
        JFileSystemOrSocket.chmod(gVars.webEnv, new_file, perms);
        
        // Compute the URL
        url = strval(wp_handle_upload_uploads.getValue("url")) + "/" + filename;
        
        _return = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "wp_handle_upload",
                new Array<Object>(new ArrayEntry<Object>("file", new_file), new ArrayEntry<Object>("url", url), new ArrayEntry<Object>("type", type)));

        return _return;
    }

    /**
     * Downloads a url to a local file using the Snoopy HTTP Class
     * @param string $url the URL of the file to download
     * @return mixed WP_Error on failure, string Filename on success.
     */
    public Object download_url(String url) {
        String tmpfname = null;
        int handle = 0;
        Snoopy snoopy = null;

        //WARNING: The file is not automatically deleted, The script must unlink() the file.
        if (!booleanval(url)) {
            return new WP_Error(gVars, gConsts, "http_no_url", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid URL Provided", "default"));
        }

        tmpfname = FileSystemOrSocket.tempnam(gVars.webEnv, get_temp_dir(), "wpupdate");

        if (!booleanval(tmpfname)) {
            return new WP_Error(gVars, gConsts, "http_no_file", getIncluded(L10nPage.class, gVars, gConsts).__("Could not create Temporary file", "default"));
        }

        handle = FileSystemOrSocket.fopen(gVars.webEnv, tmpfname, "w");

        if (!booleanval(handle)) {
            return new WP_Error(gVars, gConsts, "http_no_file", getIncluded(L10nPage.class, gVars, gConsts).__("Could not create Temporary file", "default"));
        }

        snoopy = new Snoopy(gVars, gConsts);
        snoopy.fetch(url);

        if (!equal(snoopy.status, "200")) {
            FileSystemOrSocket.fclose(gVars.webEnv, handle);
            JFileSystemOrSocket.unlink(gVars.webEnv, tmpfname);

            return new WP_Error(gVars, gConsts, "http_404", Strings.trim(snoopy.response_code));
        }

        FileSystemOrSocket.fwrite(gVars.webEnv, handle, strval(snoopy.results));
        FileSystemOrSocket.fclose(gVars.webEnv, handle);

        return tmpfname;
    }

    public Object unzip_file(String file, String to) {
        WP_Filesystem fs;
        PclZip archive = null;
        Array<Object> archive_files;
        Array<String> path = new Array<String>();
        String tmppath = null;
        int j = 0;

        if (!booleanval(gVars.wp_filesystem) || !is_object(gVars.wp_filesystem)) {
            return new WP_Error(gVars, gConsts, "fs_unavailable", getIncluded(L10nPage.class, gVars, gConsts).__("Could not access filesystem.", "default"));
        }

        fs = gVars.wp_filesystem;

        // Condensed dynamic construct
        requireOnce(gVars, gConsts, Class_pclzipPage.class);
        archive = new PclZip(gVars, gConsts, file);

        // Is the archive valid?
        if (equal(false, archive_files = archive.extract(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING()))) {
            return new WP_Error(gVars, gConsts, "incompatible_archive", getIncluded(L10nPage.class, gVars, gConsts).__("Incompatible archive", "default"), archive.errorInfo(true));
        }

        if (equal(0, Array.count(archive_files))) {
            return new WP_Error(gVars, gConsts, "empty_archive", getIncluded(L10nPage.class, gVars, gConsts).__("Empty archive", "default"));
        }

        to = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(to);
        path = Strings.explode("/", to);
        tmppath = "";

        for (j = 0; j < (Array.count(path) - 1); j++) {
            tmppath = tmppath + path.getValue(j) + "/";

            if (!fs.is_dir(tmppath)) {
                fs.mkdir(tmppath, 755);
            }
        }

        for (Map.Entry javaEntry145 : archive_files.entrySet()) {
            Array<Object> fileArray = (Array<Object>) javaEntry145.getValue();
            path = Strings.explode("/", strval(fileArray.getValue("filename")));
            tmppath = "";

            // Loop through each of the items and check that the folder exists.
            for (j = 0; j < (Array.count(path) - 1); j++) {
                tmppath = tmppath + path.getValue(j) + "/";

                if (!fs.is_dir(to + tmppath)) {
                    if (!fs.mkdir(to + tmppath, 755)) {
                        return new WP_Error(gVars, gConsts, "mkdir_failed", getIncluded(L10nPage.class, gVars, gConsts).__("Could not create directory", "default"));
                    }
                }
            }

            // We've made sure the folders are there, so let's extract the file now:
            if (!booleanval(fileArray.getValue("folder"))) {
                if (!fs.put_contents(to + fileArray.getValue("filename"), strval(fileArray.getValue("content")))) {
                    return new WP_Error(gVars, gConsts, "copy_failed", getIncluded(L10nPage.class, gVars, gConsts).__("Could not copy file", "default"));
                }
            }

            fs.chmod(to + fileArray.getValue("filename"), 644);
        }

        return true;
    }

    public boolean copy_dir(String from, String to) {
        Object dirlist = null;
        Array<Object> fileinfo = new Array<Object>();
        Object filename = null;
        dirlist = gVars.wp_filesystem.dirlist(from);
        from = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(from);
        to = getIncluded(FormattingPage.class, gVars, gConsts).trailingslashit(to);

        for (Map.Entry javaEntry146 : new Array<Object>(dirlist).entrySet()) {
            filename = javaEntry146.getKey();
            fileinfo = (Array<Object>) javaEntry146.getValue();

            if (equal("f", fileinfo.getValue("type"))) {
                if (!gVars.wp_filesystem.copy(from + filename, to + filename, true)) {
                    return false;
                }

                gVars.wp_filesystem.chmod(to + filename, 644);
            } else if (equal("d", fileinfo.getValue("type"))) {
                if (!gVars.wp_filesystem.mkdir(to + filename, 755)) {
                    return false;
                }

                if (!copy_dir(from + strval(filename), to + strval(filename))) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean WP_Filesystem(Array<Object> args, boolean preference) {
        String method = null;
        method = get_filesystem_method();

        /*preference*/
        if (!booleanval(method)) {
            return false;
        }

        // Modified by Numiton

        //		method = "WP_Filesystem_" + method;
        if (method.equalsIgnoreCase("Direct")) {
            gVars.wp_filesystem = new WP_Filesystem_Direct(gVars, gConsts, args);
        } else if (method.equalsIgnoreCase("FTPext")) {
            gVars.wp_filesystem = new WP_Filesystem_FTPext(gVars, gConsts, args);
        } else if (method.equalsIgnoreCase("ftpsockets")) {
            gVars.wp_filesystem = new WP_Filesystem_ftpsockets(gVars, gConsts, args);
        } else {
            throw new RuntimeException("Invalid wp_filesystem method: " + method);
        }

        if (booleanval(gVars.wp_filesystem.errors.get_error_code())) {
            return false;
        }

        if (!gVars.wp_filesystem.connect()) {
            return false; //There was an erorr connecting to the server.
        }

        return true;
    }

    public String get_filesystem_method() {
        String tempFile = null;
        tempFile = FileSystemOrSocket.tempnam(gVars.webEnv, get_temp_dir(), "WPU");

        if (equal(JOptions.getmyuid(), JFileSystemOrSocket.fileowner(gVars.webEnv, tempFile))) {
            JFileSystemOrSocket.unlink(gVars.webEnv, tempFile);

            return "direct";
        } else {
            JFileSystemOrSocket.unlink(gVars.webEnv, tempFile);
        }

        if (Options.extension_loaded("ftp")) {
            return "ftpext";
        }

        if (Options.extension_loaded("sockets") || true) /*Modified by Numiton*/ {
            return "ftpsockets";  //Sockets: Socket extension; PHP Mode: FSockopen / fwrite / fread
        }

        return strval(false);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_file_block1");
        gVars.webEnv = webEnv;
        gVars.wp_file_descriptions = new Array<Object>(
                new ArrayEntry<Object>("index.php", getIncluded(L10nPage.class, gVars, gConsts).__("Main Index Template", "default")),
                new ArrayEntry<Object>("style.css", getIncluded(L10nPage.class, gVars, gConsts).__("Stylesheet", "default")),
                new ArrayEntry<Object>("rtl.css", getIncluded(L10nPage.class, gVars, gConsts).__("RTL Stylesheet", "default")),
                new ArrayEntry<Object>("comments.php", getIncluded(L10nPage.class, gVars, gConsts).__("Comments", "default")),
                new ArrayEntry<Object>("comments-popup.php", getIncluded(L10nPage.class, gVars, gConsts).__("Popup Comments", "default")),
                new ArrayEntry<Object>("footer.php", getIncluded(L10nPage.class, gVars, gConsts).__("Footer", "default")),
                new ArrayEntry<Object>("header.php", getIncluded(L10nPage.class, gVars, gConsts).__("Header", "default")),
                new ArrayEntry<Object>("sidebar.php", getIncluded(L10nPage.class, gVars, gConsts).__("Sidebar", "default")),
                new ArrayEntry<Object>("archive.php", getIncluded(L10nPage.class, gVars, gConsts).__("Archives", "default")),
                new ArrayEntry<Object>("category.php", getIncluded(L10nPage.class, gVars, gConsts).__("Category Template", "default")),
                new ArrayEntry<Object>("page.php", getIncluded(L10nPage.class, gVars, gConsts).__("Page Template", "default")),
                new ArrayEntry<Object>("search.php", getIncluded(L10nPage.class, gVars, gConsts).__("Search Results", "default")),
                new ArrayEntry<Object>("searchform.php", getIncluded(L10nPage.class, gVars, gConsts).__("Search Form", "default")),
                new ArrayEntry<Object>("single.php", getIncluded(L10nPage.class, gVars, gConsts).__("Single Post", "default")),
                new ArrayEntry<Object>("404.php", getIncluded(L10nPage.class, gVars, gConsts).__("404 Template", "default")),
                new ArrayEntry<Object>("link.php", getIncluded(L10nPage.class, gVars, gConsts).__("Links Template", "default")),
                new ArrayEntry<Object>("functions.php", getIncluded(L10nPage.class, gVars, gConsts).__("Theme Functions", "default")),
                new ArrayEntry<Object>("attachment.php", getIncluded(L10nPage.class, gVars, gConsts).__("Attachment Template", "default")),
                new ArrayEntry<Object>("my-hacks.php", getIncluded(L10nPage.class, gVars, gConsts).__("my-hacks.php (legacy hacks support)", "default")),
                new ArrayEntry<Object>(".htaccess", getIncluded(L10nPage.class, gVars, gConsts).__(".htaccess (for rewrite rules )", "default")),
                // Deprecated files
                new ArrayEntry<Object>("wp-layout.css", getIncluded(L10nPage.class, gVars, gConsts).__("Stylesheet", "default")),
                new ArrayEntry<Object>("wp-comments.php", getIncluded(L10nPage.class, gVars, gConsts).__("Comments Template", "default")),
                new ArrayEntry<Object>("wp-comments-popup.php", getIncluded(L10nPage.class, gVars, gConsts).__("Popup Comments Template", "default")));

        return DEFAULT_VAL;
    }
}
