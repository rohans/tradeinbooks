/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Class_pclzipPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
import static com.numiton.generic.PhpWeb.DEFAULT_VAL;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.gzip.GZIP;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Class_pclzipPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Class_pclzipPage.class.getName());
    public Object g_pclzip_version;

    @Override
    @RequestMapping("/wp-admin/includes/class-pclzip.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/class_pclzip";
    }

    public String PclZipUtilPathReduction(String p_dir) {
        String v_result = null;
        Array<String> v_list = new Array<String>();
        int v_skip = 0;
        int i = 0;
        v_result = "";

        if (!equal(p_dir, ""))/*
         * ----- Explode path by directory names ----- Explode path by directory
         * names
         */

        /*
         * ----- Study directories from last to first ----- Study directories
         * from last to first
         */

        /*
         * ----- Look for skip ----- Look for skip
         */
         {
            v_list = Strings.explode("/", p_dir);
            v_skip = 0;

            for (i = Array.sizeof(v_list) - 1; i >= 0; i--)/*
             * ----- Look for current path ----- Look for current path
             */
             {
                /*
                 * ----- Ignore this directory ----- Ignore this directory
                 * Should be the first $i=0, but no check is done Should be the
                 * first $i=0, but no check is done
                 */
                if (equal(v_list.getValue(i), ".")) {
                } else if (equal(v_list.getValue(i), "..")) {
                    v_skip++;
                } else if (equal(v_list.getValue(i), ""))/*
                 * ----- First '/' i.e. root slash ----- First '/' i.e.
                 * root slash
                 *
                 * ----- Double '/' inside the path ----- Double '/'
                 * inside the path ----- Ignore only the double '//' in
                 * path, ----- Ignore only the double '//' in path, but
                 * not the first and last '/' but not the first and last
                 * '/'
                 */
                 {
                    /*
                     * ----- Last '/' i.e. indicates a directory -----
                     * Last '/' i.e. indicates a directory
                     */
                    if (equal(i, 0)) {
                        v_result = "/" + v_result;

                        if (v_skip > 0)/*
                         * ----- It is an invalid path, so the path is
                         * not modified ----- It is an invalid path, so
                         * the path is not modified TBC TBC
                         */

                        /*
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "Invalid path is unchanged");
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "Invalid path is unchanged");
                         */
                         {
                            v_result = p_dir;
                            v_skip = 0;
                        }
                    } else if (equal(i, Array.sizeof(v_list) - 1)) {
                        v_result = v_list.getValue(i);
                    } else {
                    }
                } else/*
                 * ----- Look for item to skip ----- Look for item to
                 * skip
                 */
                 {
                    if (v_skip > 0) {
                        v_skip--;
                    } else {
                        v_result = v_list.getValue(i) + ((!equal(i, Array.sizeof(v_list) - 1))
                            ? ("/" + v_result)
                            : "");
                    }
                }
            }

            if (v_skip > 0) {
                while (v_skip > 0) {
                    v_result = "../" + v_result;
                    v_skip--;
                }
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : PclZipUtilPathInclusion() Function : PclZipUtilPathInclusion()
     * Description : Description : This function indicates if the path $p_path
     * is under the $p_dir tree. Or, This function indicates if the path $p_path
     * is under the $p_dir tree. Or, said in an other way, if the file or
     * sub-dir $p_path is inside the dir said in an other way, if the file or
     * sub-dir $p_path is inside the dir $p_dir. $p_dir. The function indicates
     * also if the path is exactly the same as the dir. The function indicates
     * also if the path is exactly the same as the dir. This function supports
     * path with duplicated '/' like '//', but does not This function supports
     * path with duplicated '/' like '//', but does not support '.' or '..'
     * statements. support '.' or '..' statements. Parameters : Parameters :
     * Return Values : Return Values : 0 if $p_path is not inside directory
     * $p_dir 0 if $p_path is not inside directory $p_dir 1 if $p_path is inside
     * directory $p_dir 1 if $p_path is inside directory $p_dir 2 if $p_path is
     * exactly the same as $p_dir 2 if $p_path is exactly the same as $p_dir
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int PclZipUtilPathInclusion(String p_dir, String p_path) {
        int v_result = 0;
        Array<String> v_list_dir = new Array<String>();
        int v_list_dir_size = 0;
        Array<String> v_list_path = new Array<String>();
        int v_list_path_size = 0;
        int i = 0;
        int j = 0;
        v_result = 1;

        if (equal(p_dir, ".") || ((Strings.strlen(p_dir) >= 2) && equal(Strings.substr(p_dir, 0, 2), "./"))) {
            p_dir = PclZipUtilTranslateWinPath(Directories.getcwd(gVars.webEnv), false) + "/" + Strings.substr(p_dir, 1);
        }

        if (equal(p_path, ".") || ((Strings.strlen(p_path) >= 2) && equal(Strings.substr(p_path, 0, 2), "./"))) {
            p_path = PclZipUtilTranslateWinPath(Directories.getcwd(gVars.webEnv), false) + "/" + Strings.substr(p_path, 1);
        }

        v_list_dir = Strings.explode("/", p_dir);
        v_list_dir_size = Array.sizeof(v_list_dir);
        v_list_path = Strings.explode("/", p_path);
        v_list_path_size = Array.sizeof(v_list_path);
        i = 0;
        j = 0;

        while ((i < v_list_dir_size) && (j < v_list_path_size) && booleanval(v_result))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
         * "Working on dir($i)='".$v_list_dir[$i]."' and
         * path($j)='".$v_list_path[$j]."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
         * "Working on dir($i)='".$v_list_dir[$i]."' and
         * path($j)='".$v_list_path[$j]."'"); ----- Look for empty dir (path
         * reduction) ----- Look for empty dir (path reduction)
         */

        /*
         * ----- Compare the items ----- Compare the items
         */

        /*
         * ----- Next items ----- Next items
         */
         {
            if (equal(v_list_dir.getValue(i), "")) {
                i++;

                continue;
            }

            if (equal(v_list_path.getValue(j), "")) {
                j++;

                continue;
            }

            if (!equal(v_list_dir.getValue(i), v_list_path.getValue(j)) && !equal(v_list_dir.getValue(i), "") && !equal(v_list_path.getValue(j), ""))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Items ($i,$j) are different");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Items ($i,$j) are different");
             */
             {
                v_result = 0;
            }

            i++;
            j++;
        }

        if (booleanval(v_result))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Look
         * for tie break"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 5, "Look for tie break"); ----- Skip all the empty items
         * ----- Skip all the empty items
         */

        /*
         * ??$v_list_path[$j]:'')."'"); $v_list_path[$j]:'')."'");
         */
         {
            while ((j < v_list_path_size) && equal(v_list_path.getValue(j), ""))
                j++;

            while ((i < v_list_dir_size) && equal(v_list_dir.getValue(i), ""))
                i++;

            if ((i >= v_list_dir_size) && (j >= v_list_path_size))/*
             * ----- There are exactly the same ----- There are exactly the same
             */
             {
                v_result = 2;
            } else if (i < v_list_dir_size)/*
             * ----- The path is shorter than the dir ----- The path is
             * shorter than the dir
             */
             {
                v_result = 0;
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : PclZipUtilCopyBlock() Function : PclZipUtilCopyBlock()
     * Description : Description : Parameters : Parameters : $p_mode :
     * read/write compression mode $p_mode : read/write compression mode 0 : src &
     * dest normal 0 : src & dest normal 1 : src gzip, dest normal 1 : src gzip,
     * dest normal 2 : src normal, dest gzip 2 : src normal, dest gzip 3 : src &
     * dest gzip 3 : src & dest gzip Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int PclZipUtilCopyBlock(int p_src, int p_dest, int p_size, int p_mode) {
        int v_result = 0;
        int v_read_size;
        String v_buffer = null;
        v_result = 1;

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Src
         * offset after read :".(@ftell($p_src)));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Src
         * offset after read :".(@ftell($p_src)));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Dest
         * offset after write :".(@ftell($p_dest)));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Dest
         * offset after write :".(@ftell($p_dest)));
         */
        if (equal(p_mode, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Src
         * offset before read :".(@ftell($p_src)));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Src
         * offset before read :".(@ftell($p_src)));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Dest
         * offset before write :".(@ftell($p_dest)));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Dest
         * offset before write :".(@ftell($p_dest)));
         */
         {
            while (!equal(p_size, 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Read $v_read_size bytes");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Read $v_read_size bytes");
             */
             {
                v_read_size = ((p_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                    ? p_size
                    : gConsts.getPCLZIP_READ_BLOCK_SIZE());
                v_buffer = FileSystemOrSocket.fread(gVars.webEnv, p_src, v_read_size);
                FileSystemOrSocket.fwrite(gVars.webEnv, p_dest, v_buffer, v_read_size);
                p_size = p_size - v_read_size;
            }
        } else if (equal(p_mode, 1)) {
            while (!equal(p_size, 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 4, "Read $v_read_size bytes");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 4, "Read $v_read_size bytes");
             */
             {
                v_read_size = ((p_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                    ? p_size
                    : gConsts.getPCLZIP_READ_BLOCK_SIZE());
                v_buffer = GZIP.gzread(gVars.webEnv, p_src, v_read_size);
                FileSystemOrSocket.fwrite(gVars.webEnv, p_dest, v_buffer, v_read_size);
                p_size = p_size - v_read_size;
            }
        } else if (equal(p_mode, 2)) {
            while (!equal(p_size, 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 4, "Read $v_read_size bytes");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 4, "Read $v_read_size bytes");
             */
             {
                v_read_size = ((p_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                    ? p_size
                    : gConsts.getPCLZIP_READ_BLOCK_SIZE());
                v_buffer = FileSystemOrSocket.fread(gVars.webEnv, p_src, v_read_size);
                GZIP.gzwrite(gVars.webEnv, p_dest, v_buffer, v_read_size);
                p_size = p_size - v_read_size;
            }
        } else if (equal(p_mode, 3)) {
            while (!equal(p_size, 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 4, "Read $v_read_size bytes");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 4, "Read $v_read_size bytes");
             */
             {
                v_read_size = ((p_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                    ? p_size
                    : gConsts.getPCLZIP_READ_BLOCK_SIZE());
                v_buffer = GZIP.gzread(gVars.webEnv, p_src, v_read_size);
                GZIP.gzwrite(gVars.webEnv, p_dest, v_buffer, v_read_size);
                p_size = p_size - v_read_size;
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : PclZipUtilRename() Function : PclZipUtilRename() Description :
     * Description : This function tries to do a simple rename() function. If it
     * fails, it This function tries to do a simple rename() function. If it
     * fails, it tries to copy the $p_src file in a new $p_dest file and then
     * unlink the tries to copy the $p_src file in a new $p_dest file and then
     * unlink the first one. first one. Parameters : Parameters : $p_src : Old
     * filename $p_src : Old filename $p_dest : New filename $p_dest : New
     * filename Return Values : Return Values : 1 on success, 0 on failure. 1 on
     * success, 0 on failure.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int PclZipUtilRename(String p_src, String p_dest) {
        int v_result = 0;
        v_result = 1;

        if (!FileSystemOrSocket.rename(gVars.webEnv, p_src, p_dest))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Fail
         * to rename file, try copy+unlink");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Fail
         * to rename file, try copy+unlink"); ----- Try to copy & unlink the src
         * ----- Try to copy & unlink the src
         */
         {
            if (!FileSystemOrSocket.copy(gVars.webEnv, p_src, p_dest))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Fail to copy file");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Fail to copy file");
             */
             {
                v_result = 0;
            } else if (!JFileSystemOrSocket.unlink(gVars.webEnv, p_src))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 5, "Fail to unlink old filename");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 5, "Fail to unlink old filename");
             */
             {
                v_result = 0;
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : PclZipUtilOptionText() Function : PclZipUtilOptionText()
     * Description : Description : Translate option value in text. Mainly for
     * debug purpose. Translate option value in text. Mainly for debug purpose.
     * Parameters : Parameters : $p_option : the option value. $p_option : the
     * option value. Return Values : Return Values : The option text value. The
     * option text value.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public String PclZipUtilOptionText(Object p_option) {
        // Modified by Numiton
        Set<String> v_list = gConsts.constDefs;
        String v_prefix = null;
        String v_result = null;

        for (String v_key : v_list) {
            v_prefix = Strings.substr(v_key, 0, 10);

            if ((equal(v_prefix, "PCLZIP_OPT") || equal(v_prefix, "PCLZIP_CB_") || equal(v_prefix, "PCLZIP_ATT")) && equal(gConsts.getConstantValue(v_key), p_option))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_key);
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_key);
             */
             {
                return v_key;
            }
        }

        v_result = "Unknown";

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : PclZipUtilTranslateWinPath() Function :
     * PclZipUtilTranslateWinPath() Description : Description : Translate
     * windows path by replacing '\' by '/' and optionally removing Translate
     * windows path by replacing '\' by '/' and optionally removing drive
     * letter. drive letter. Parameters : Parameters : $p_path : path to
     * translate. $p_path : path to translate. $p_remove_disk_letter : true |
     * false $p_remove_disk_letter : true | false Return Values : Return Values :
     * The path translated. The path translated.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public String PclZipUtilTranslateWinPath(String p_path, boolean p_remove_disk_letter) {
        int v_position = 0;

        if (booleanval(Strings.stristr(Options.php_uname(), "windows")))/*
         * ----- Look for potential disk letter ----- Look for potential disk
         * letter
         *
         * ----- Change potential windows directory separator ----- Change
         * potential windows directory separator
         */
         {
            if (p_remove_disk_letter && !equal(v_position = Strings.strpos(p_path, ":"), false)) {
                p_path = Strings.substr(p_path, v_position + 1);
            }

            if ((Strings.strpos(p_path, "\\") > 0) || equal(Strings.substr(p_path, 0, 1), "\\")) {
                p_path = Strings.strtr(p_path, "\\", "/");
            }
        }

        return p_path;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_class_pclzip_block1");
        gVars.webEnv = webEnv;
        
		 // --------------------------------------------------------------------------------
		 // PhpConcept Library - Zip Module 2.5
		 // --------------------------------------------------------------------------------
		 // License GNU/LGPL - Vincent Blavet - March 2006
		 // http://www.phpconcept.net
		 // --------------------------------------------------------------------------------
		 //
		 // Presentation :
		 //   PclZip is a PHP library that manage ZIP archives.
		 //   So far tests show that archives generated by PclZip are readable by
		 //   WinZip application and other tools.
		 //
		 // Description :
		 //   See readme.txt and http://www.phpconcept.net
		 //
		 // Warning :
		 //   This library and the associated files are non commercial, non professional
		 //   work.
		 //   It should not have unexpected results. However if any damage is caused by
		 //   this software the author can not be responsible.
		 //   The use of this software is at the risk of the user.
		 //
		 // --------------------------------------------------------------------------------
		 // $Id: Class_pclzipPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
		 // --------------------------------------------------------------------------------
		
		// ----- Constants
        gConsts.setPCLZIP_READ_BLOCK_SIZE(2048);
        
        // ----- File list separator
        // In version 1.x of PclZip, the separator for file list is a space
        // (which is not a very smart choice, specifically for windows paths !).
        // A better separator should be a comma (,). This constant gives you the
        // abilty to change that.
        // However notice that changing this value, may have impact on existing
        // scripts, using space separated filenames.
        // Recommanded values for compatibility with older versions :
        //define( 'PCLZIP_SEPARATOR', ' ' );
        // Recommanded values for smart separation of filenames.
        gConsts.setPCLZIP_SEPARATOR(",");
        
        // ----- Error configuration
        // 0 : PclZip Class integrated error handling
        // 1 : PclError external library error handling. By enabling this
        //     you must ensure that you have included PclError library.
        // [2,...] : reserved for futur use
        gConsts.setPCLZIP_ERROR_EXTERNAL(0);
        
        // ----- Optional static temporary directory
        //       By default temporary files are generated in the script current
        //       path.
        //       If defined :
        //       - MUST BE terminated by a '/'.
        //       - MUST be a valid, already created directory
        //       Samples :
        // define( 'PCLZIP_TEMPORARY_DIR', '/temp/' );
        // define( 'PCLZIP_TEMPORARY_DIR', 'C:/Temp/' );
        gConsts.setPCLZIP_TEMPORARY_DIR("");
        
        // --------------------------------------------------------------------------------
		// ***** UNDER THIS LINE NOTHING NEEDS TO BE MODIFIED *****
		// --------------------------------------------------------------------------------

		// ----- Global variables
        g_pclzip_version = "2.5";
        
        // ----- Error codes
        //   -1 : Unable to open file in binary write mode
        //   -2 : Unable to open file in binary read mode
        //   -3 : Invalid parameters
        //   -4 : File does not exist
        //   -5 : Filename is too long (max. 255)
        //   -6 : Not a valid zip file
        //   -7 : Invalid extracted file size
        //   -8 : Unable to create directory
        //   -9 : Invalid archive extension
        //  -10 : Invalid archive format
        //  -11 : Unable to delete file (unlink)
        //  -12 : Unable to rename file (rename)
        //  -13 : Invalid header checksum
        //  -14 : Invalid archive size
        gConsts.setPCLZIP_ERR_USER_ABORTED(2);
        gConsts.setPCLZIP_ERR_NO_ERROR(0);
        gConsts.setPCLZIP_ERR_WRITE_OPEN_FAIL(-1);
        gConsts.setPCLZIP_ERR_READ_OPEN_FAIL(-2);
        gConsts.setPCLZIP_ERR_INVALID_PARAMETER(-3);
        gConsts.setPCLZIP_ERR_MISSING_FILE(-4);
        gConsts.setPCLZIP_ERR_FILENAME_TOO_LONG(-5);
        gConsts.setPCLZIP_ERR_INVALID_ZIP(-6);
        gConsts.setPCLZIP_ERR_BAD_EXTRACTED_FILE(-7);
        gConsts.setPCLZIP_ERR_DIR_CREATE_FAIL(-8);
        gConsts.setPCLZIP_ERR_BAD_EXTENSION(-9);
        gConsts.setPCLZIP_ERR_BAD_FORMAT(-10);
        gConsts.setPCLZIP_ERR_DELETE_FILE_FAIL(-11);
        gConsts.setPCLZIP_ERR_RENAME_FILE_FAIL(-12);
        gConsts.setPCLZIP_ERR_BAD_CHECKSUM(-13);
        gConsts.setPCLZIP_ERR_INVALID_ARCHIVE_ZIP(-14);
        gConsts.setPCLZIP_ERR_MISSING_OPTION_VALUE(-15);
        gConsts.setPCLZIP_ERR_INVALID_OPTION_VALUE(-16);
        gConsts.setPCLZIP_ERR_ALREADY_A_DIRECTORY(-17);
        gConsts.setPCLZIP_ERR_UNSUPPORTED_COMPRESSION(-18);
        gConsts.setPCLZIP_ERR_UNSUPPORTED_ENCRYPTION(-19);
        gConsts.setPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(-20);
        gConsts.setPCLZIP_ERR_DIRECTORY_RESTRICTION(-21);
        
        // ----- Options values
        gConsts.setPCLZIP_OPT_PATH(77001);
        gConsts.setPCLZIP_OPT_ADD_PATH(77002);
        gConsts.setPCLZIP_OPT_REMOVE_PATH(77003);
        gConsts.setPCLZIP_OPT_REMOVE_ALL_PATH(77004);
        gConsts.setPCLZIP_OPT_SET_CHMOD(77005);
        gConsts.setPCLZIP_OPT_EXTRACT_AS_STRING(77006);
        gConsts.setPCLZIP_OPT_NO_COMPRESSION(77007);
        gConsts.setPCLZIP_OPT_BY_NAME(77008);
        gConsts.setPCLZIP_OPT_BY_INDEX(77009);
        gConsts.setPCLZIP_OPT_BY_EREG(77010);
        gConsts.setPCLZIP_OPT_BY_PREG(77011);
        gConsts.setPCLZIP_OPT_COMMENT(77012);
        gConsts.setPCLZIP_OPT_ADD_COMMENT(77013);
        gConsts.setPCLZIP_OPT_PREPEND_COMMENT(77014);
        gConsts.setPCLZIP_OPT_EXTRACT_IN_OUTPUT(77015);
        gConsts.setPCLZIP_OPT_REPLACE_NEWER(77016);
        gConsts.setPCLZIP_OPT_STOP_ON_ERROR(77017);
        
        // Having big trouble with crypt. Need to multiply 2 long int
        // which is not correctly supported by PHP ...
        //define( 'PCLZIP_OPT_CRYPT', 77018 );
        gConsts.setPCLZIP_OPT_EXTRACT_DIR_RESTRICTION(77019);
        
        // ----- File description attributes
        gConsts.setPCLZIP_ATT_FILE_NAME(79001);
        gConsts.setPCLZIP_ATT_FILE_NEW_SHORT_NAME(79002);
        gConsts.setPCLZIP_ATT_FILE_NEW_FULL_NAME(79003);
        
        // ----- Call backs values
        gConsts.setPCLZIP_CB_PRE_EXTRACT(78001);
        gConsts.setPCLZIP_CB_POST_EXTRACT(78002);
        gConsts.setPCLZIP_CB_PRE_ADD(78003);
        gConsts.setPCLZIP_CB_POST_ADD(78004);
        
        /* For futur use
        define( 'PCLZIP_CB_PRE_LIST', 78005 );
        define( 'PCLZIP_CB_POST_LIST', 78006 );
        define( 'PCLZIP_CB_PRE_DELETE', 78007 );
        define( 'PCLZIP_CB_POST_DELETE', 78008 );
        */

        return DEFAULT_VAL;
    }
}
