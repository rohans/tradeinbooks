/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PclZip.java,v 1.3 2008/10/10 16:48:03 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.gzip.GZIP;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QMisc;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


//--------------------------------------------------------------------------------
// Class : PclZip
// Description :
//   PclZip is the class that represent a Zip archive.
//   The public methods allow the manipulation of the archive.
// Attributes :
//   Attributes must not be accessed directly.
// Methods :
//   PclZip() : Object creator
//   create() : Creates the Zip archive
//   listContent() : List the content of the Zip archive
//   extract() : Extract the content of the archive
//   properties() : List the properties of the archive
// --------------------------------------------------------------------------------
public class PclZip implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(PclZip.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    /**
     * ----- Filename of the zip file
     */
    public String zipname = "";

    /**
     * ----- File descriptor of the zip file
     * file
     */
    public int zip_fd = 0;

    /**
     * ----- Internal error handling
     */
    public int error_code = 1;
    public String error_string = "";

    // ----- Current status of the magic_quotes_runtime
    // This value store the php configuration for magic_quotes
    // The class can then disable the magic_quotes and reset it after
    public int magic_quotes_status;

    // --------------------------------------------------------------------------------
    // Function : PclZip()
    // Description :
    //   Creates a PclZip object and set the name of the associated Zip archive
    //   filename.
    //   Note that no real action is taken, if the archive does not exist it is not
    //   created. Use create() for that.
    // --------------------------------------------------------------------------------
    public PclZip(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String p_zipname) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        //--(MAGIC-PclTrace)--//PclTraceFctStart(__FILE__, __LINE__, 'PclZip::PclZip', "zipname=$p_zipname");

        // ----- Tests the zlib
        if (!true)/*Modified by Numiton*/ {
        	//--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 1, "zlib extension seems to be missing");
            System.exit("Abort " + FileSystemOrSocket.basename(SourceCodeInfo.getCurrentFile(gVars.webEnv)) + " : Missing zlib extensions");
        }

        // ----- Set the attributes
        this.zipname = p_zipname;
        this.zip_fd = 0;
        this.magic_quotes_status = -1;
        
        // ----- Return
        //--(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 1);
    }

    // --------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------
    // Function :
    //   create($p_filelist, $p_add_dir="", $p_remove_dir="")
    //   create($p_filelist, $p_option, $p_option_value, ...)
    // Description :
    //   This method supports two different synopsis. The first one is historical.
    //   This method creates a Zip Archive. The Zip file is created in the
    //   filesystem. The files and directories indicated in $p_filelist
    //   are added in the archive. See the parameters description for the
    //   supported format of $p_filelist.
    //   When a directory is in the list, the directory and its content is added
    //   in the archive.
    //   In this synopsis, the function takes an optional variable list of
    //   options. See bellow the supported options.
    // Parameters :
    //   $p_filelist : An array containing file or directory names, or
    //                 a string containing one filename or one directory name, or
    //                 a string containing a list of filenames and/or directory
    //                 names separated by spaces.
    //   $p_add_dir : A path to add before the real path of the archived file,
    //                in order to have it memorized in the archive.
    //   $p_remove_dir : A path to remove from the real path of the file to archive,
    //                   in order to have a shorter path memorized in the archive.
    //                   When $p_add_dir and $p_remove_dir are set, $p_remove_dir
    //                   is removed first, before $p_add_dir is added.
    // Options :
    //   PCLZIP_OPT_ADD_PATH :
    //   PCLZIP_OPT_REMOVE_PATH :
    //   PCLZIP_OPT_REMOVE_ALL_PATH :
    //   PCLZIP_OPT_COMMENT :
    //   PCLZIP_CB_PRE_ADD :
    //   PCLZIP_CB_POST_ADD :
    // Return Values :
    //   0 on failure,
    //   The list of the added files, with a status of the add action.
    //   (see PclZip::listContent() for list entry format)
    // --------------------------------------------------------------------------------
    public Array<Object> create(Object p_filelist, /* Do not change type */
        Object... vargs) {
        Object v_result = null;
        Array<Object> v_options = new Array<Object>();
        int v_size = 0;
        Array<Object> v_arg_list = new Array<Object>();
        Array<String> v_string_list = new Array<String>();
        Array v_att_list = new Array();
        Array<Object> v_filedescr_list = new Array<Object>();
        Array<Object> p_result_list = new Array<Object>();
        Object v_string = null;
        Array<Object> v_supported_attributes = new Array<Object>();
        Array<Object> v_entry = null;
        v_result = 1;
        this.privErrorReset();
        v_options = new Array<Object>();
        v_options.putValue(gConsts.getPCLZIP_OPT_NO_COMPRESSION(), false);

        // Added by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(p_filelist, vargs);
        v_size = FunctionHandling.func_num_args(totalArgs);

        if (v_size > 1)/*
         * ----- Get the arguments ----- Get the arguments
         */

        /*
         * ----- Remove from the options list the first argument ----- Remove
         * from the options list the first argument
         */

        /*
         * ----- Look for first arg ----- Look for first arg
         */
         {
            v_arg_list = FunctionHandling.func_get_args(totalArgs);
            Array.array_shift(v_arg_list);
            v_size--;

            if (is_integer(v_arg_list.getValue(0)) && (intval(v_arg_list.getValue(0)) > 77000))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options detected");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options detected"); ----- Parse the options
             * ----- Parse the options > 'optional' 'optional'
             */
             {
                v_result = this.privParseOptions(v_arg_list, v_size, v_options,
                        new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_ADD_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_PRE_ADD(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_POST_ADD(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_NO_COMPRESSION(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_COMMENT(), "optional")));

                if (!equal(v_result, 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 */
                 {
                    return new Array<Object>();
                }
            } else/*
             * ----- Look for 2 args ----- Look for 2 args Here we need to
             * support the first historic synopsis of the Here we need to
             * support the first historic synopsis of the method. method.
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis"); ----- Get the first argument ----- Get the
             * first argument
             */

            /*
             * ----- Look for the optional second argument ----- Look for the
             * optional second argument
             */
             {
                v_options.putValue(gConsts.getPCLZIP_OPT_ADD_PATH(), v_arg_list.getValue(0));

                if (equal(v_size, 2)) {
                    v_options.putValue(gConsts.getPCLZIP_OPT_REMOVE_PATH(), v_arg_list.getValue(1));
                } else if (v_size > 2)/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid number / type of arguments");

                    return new Array<Object>();
                }
            }
        }

        v_string_list = new Array<String>();
        v_att_list = new Array<String>();
        v_filedescr_list = new Array<Object>();
        p_result_list = new Array<Object>();

        /*
         * ----- Look if the $p_filelist is a string ----- Look if the
         * $p_filelist is a string
         */
        if (is_array(p_filelist))/*
         * ----- Look if the first element is also an array ----- Look if the
         * first element is also an array This will mean that this is a file
         * description entry This will mean that this is a file description
         * entry
         */
         {
            if (isset(((Array<String>) p_filelist).getValue(0)) && is_array(((Array<String>) p_filelist).getValue(0))) {
                v_att_list = ((Array<String>) p_filelist);
            } else/*
             * ----- The list is a list of string names ----- The list is a list
             * of string names
             */
             {
                v_string_list = ((Array<String>) p_filelist);
            }
        } else if (is_string(p_filelist))/*
         * ----- Create a list from the string ----- Create a list from the
         * string
         */
         {
            v_string_list = Strings.explode(gConsts.getPCLZIP_SEPARATOR(), strval(p_filelist));
        } else/*
         * ----- Invalid variable type for $p_filelist ----- Invalid
         * variable type for $p_filelist
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid variable type p_filelist");

            return new Array<Object>();
        }

        if (!equal(Array.sizeof(v_string_list), 0)) {
            for (Map.Entry javaEntry112 : v_string_list.entrySet())/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Ignore an empty filename");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Ignore an empty filename");
             */
             {
                v_string = javaEntry112.getValue();

                if (!equal(v_string, "")) {
                    v_att_list.putValue(new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NAME(), v_string)));
                } else {
                }
            }
        }

        v_supported_attributes = new Array<Object>(
                new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NAME(), "mandatory"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NEW_SHORT_NAME(), "optional"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NEW_FULL_NAME(), "optional"));

        for (Map.Entry javaEntry113 : (Set<Map.Entry>) v_att_list.entrySet()) {
            v_entry = (Array<Object>) javaEntry113.getValue();

            // Modified by Numiton
            Array<Object> tmpFileDescr = new Array<Object>();
            v_filedescr_list.putValue(tmpFileDescr);
            v_result = this.privFileDescrParseAtt(v_entry, tmpFileDescr, v_options, v_supported_attributes);

            if (!equal(v_result, 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             */
             {
                return new Array<Object>();
            }
        }

        v_result = this.privFileDescrExpand(v_filedescr_list, v_options);

        if (!equal(v_result, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        v_result = this.privCreate(v_filedescr_list, p_result_list, v_options);

        if (!equal(v_result, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        return p_result_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : Function : add($p_filelist, $p_add_dir="", $p_remove_dir="")
     * add($p_filelist, $p_add_dir="", $p_remove_dir="") add($p_filelist,
     * $p_option, $p_option_value, ...) add($p_filelist, $p_option,
     * $p_option_value, ...) Description : Description : This method supports
     * two synopsis. The first one is historical. This method supports two
     * synopsis. The first one is historical. This methods add the list of files
     * in an existing archive. This methods add the list of files in an existing
     * archive. If a file with the same name already exists, it is added at the
     * end of the If a file with the same name already exists, it is added at
     * the end of the archive, the first one is still present. archive, the
     * first one is still present. If the archive does not exist, it is created.
     * If the archive does not exist, it is created. Parameters : Parameters :
     * $p_filelist : An array containing file or directory names, or $p_filelist :
     * An array containing file or directory names, or a string containing one
     * filename or one directory name, or a string containing one filename or
     * one directory name, or a string containing a list of filenames and/or
     * directory a string containing a list of filenames and/or directory names
     * separated by spaces. names separated by spaces. $p_add_dir : A path to
     * add before the real path of the archived file, $p_add_dir : A path to add
     * before the real path of the archived file, in order to have it memorized
     * in the archive. in order to have it memorized in the archive.
     * $p_remove_dir : A path to remove from the real path of the file to
     * archive, $p_remove_dir : A path to remove from the real path of the file
     * to archive, in order to have a shorter path memorized in the archive. in
     * order to have a shorter path memorized in the archive. When $p_add_dir
     * and $p_remove_dir are set, $p_remove_dir When $p_add_dir and
     * $p_remove_dir are set, $p_remove_dir is removed first, before $p_add_dir
     * is added. is removed first, before $p_add_dir is added. Options : Options :
     * PCLZIP_OPT_ADD_PATH : PCLZIP_OPT_ADD_PATH : PCLZIP_OPT_REMOVE_PATH :
     * PCLZIP_OPT_REMOVE_PATH : PCLZIP_OPT_REMOVE_ALL_PATH :
     * PCLZIP_OPT_REMOVE_ALL_PATH : PCLZIP_OPT_COMMENT : PCLZIP_OPT_COMMENT :
     * PCLZIP_OPT_ADD_COMMENT : PCLZIP_OPT_ADD_COMMENT :
     * PCLZIP_OPT_PREPEND_COMMENT : PCLZIP_OPT_PREPEND_COMMENT :
     * PCLZIP_CB_PRE_ADD : PCLZIP_CB_PRE_ADD : PCLZIP_CB_POST_ADD :
     * PCLZIP_CB_POST_ADD : Return Values : Return Values : 0 on failure, 0 on
     * failure, The list of the added files, with a status of the add action.
     * The list of the added files, with a status of the add action. (see
     * PclZip::listContent() for list entry format) (see PclZip::listContent()
     * for list entry format)
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public Array<Object> add(Object p_filelist, /* Do not change type */
        Object... vargs) {
        Object v_result = null;
        Array<Object> v_options = new Array<Object>();
        int v_size = 0;
        Array<Object> v_arg_list = new Array<Object>();
        Object v_add_path = null;
        Array<String> v_string_list = new Array<String>();
        Array<Object> v_att_list = new Array<Object>();
        Array<Object> v_filedescr_list = new Array<Object>();
        Array<Object> p_result_list = new Array<Object>();
        Object v_string = null;
        Array<Object> v_supported_attributes = new Array<Object>();
        Array<Object> v_entry = null;
        v_result = 1;
        this.privErrorReset();
        v_options = new Array<Object>();
        v_options.putValue(gConsts.getPCLZIP_OPT_NO_COMPRESSION(), false);

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(p_filelist, vargs);
        v_size = FunctionHandling.func_num_args(totalArgs);

        if (v_size > 1)/*
         * ----- Get the arguments ----- Get the arguments
         */

        /*
         * ----- Remove form the options list the first argument ----- Remove
         * form the options list the first argument
         */

        /*
         * ----- Look for first arg ----- Look for first arg
         */
         {
            v_arg_list = FunctionHandling.func_get_args(totalArgs);
            Array.array_shift(v_arg_list);
            v_size--;

            if (is_integer(v_arg_list.getValue(0)) && (intval(v_arg_list.getValue(0)) > 77000))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options detected");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options detected"); ----- Parse the options
             * ----- Parse the options > 'optional' 'optional'
             */
             {
                v_result = this.privParseOptions(v_arg_list, v_size, v_options,
                        new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_ADD_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_PRE_ADD(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_POST_ADD(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_NO_COMPRESSION(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_COMMENT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_ADD_COMMENT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_PREPEND_COMMENT(), "optional")));

                if (!equal(v_result, 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 */
                 {
                    return new Array<Object>();
                }
            } else/*
             * ----- Look for 2 args ----- Look for 2 args Here we need to
             * support the first historic synopsis of the Here we need to
             * support the first historic synopsis of the method. method.
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis"); ----- Get the first argument ----- Get the
             * first argument
             */

            /*
             * ----- Look for the optional second argument ----- Look for the
             * optional second argument
             */
             {
                v_options.putValue(gConsts.getPCLZIP_OPT_ADD_PATH(), v_add_path = v_arg_list.getValue(0));

                if (equal(v_size, 2)) {
                    v_options.putValue(gConsts.getPCLZIP_OPT_REMOVE_PATH(), v_arg_list.getValue(1));
                } else if (v_size > 2)/*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid number / type of arguments");

                    return new Array<Object>();
                }
            }
        }

        v_string_list = new Array<String>();
        v_att_list = new Array<Object>();
        v_filedescr_list = new Array<Object>();
        p_result_list = new Array<Object>();

        /*
         * ----- Look if the $p_filelist is a string ----- Look if the
         * $p_filelist is a string
         */
        if (is_array(p_filelist))/*
         * ----- Look if the first element is also an array ----- Look if the
         * first element is also an array This will mean that this is a file
         * description entry This will mean that this is a file description
         * entry
         */
         {
            if (isset(((Array) p_filelist).getValue(0)) && is_array(((Array) p_filelist).getValue(0))) {
                v_att_list = (Array<Object>) p_filelist;
            } else/*
             * ----- The list is a list of string names ----- The list is a list
             * of string names
             */
             {
                v_string_list = (Array<String>) p_filelist;
            }
        } else if (is_string(p_filelist))/*
         * ----- Create a list from the string ----- Create a list from the
         * string
         */
         {
            v_string_list = Strings.explode(gConsts.getPCLZIP_SEPARATOR(), strval(p_filelist));
        } else/*
         * ----- Invalid variable type for $p_filelist ----- Invalid
         * variable type for $p_filelist
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid variable type \'" + gettype(p_filelist) + "\' for p_filelist");

            return new Array<Object>();
        }

        if (!equal(Array.sizeof(v_string_list), 0)) {
            for (Map.Entry javaEntry114 : v_string_list.entrySet()) {
                v_string = javaEntry114.getValue();
                v_att_list.putValue(new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NAME(), v_string)));
            }
        }

        v_supported_attributes = new Array<Object>(
                new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NAME(), "mandatory"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NEW_SHORT_NAME(), "optional"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ATT_FILE_NEW_FULL_NAME(), "optional"));

        for (Map.Entry javaEntry115 : v_att_list.entrySet()) {
            v_entry = (Array<Object>) javaEntry115.getValue();

            // Modified by Numiton
            Array<Object> tmpFileDescr = new Array<Object>();
            v_filedescr_list.putValue(tmpFileDescr);
            v_result = this.privFileDescrParseAtt(v_entry, tmpFileDescr, v_options, v_supported_attributes);

            if (!equal(v_result, 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             */
             {
                return new Array<Object>();
            }
        }

        v_result = this.privFileDescrExpand(v_filedescr_list, v_options);

        if (!equal(v_result, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        v_result = this.privAdd(v_filedescr_list, p_result_list, v_options);

        if (!equal(v_result, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        return p_result_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : listContent() Function : listContent() Description :
     * Description : This public method, gives the list of the files and
     * directories, with their This public method, gives the list of the files
     * and directories, with their properties. properties. The properties of
     * each entries in the list are (used also in other functions) : The
     * properties of each entries in the list are (used also in other functions) :
     * filename : Name of the file. For a create or add action it is the
     * filename filename : Name of the file. For a create or add action it is
     * the filename given by the user. For an extract function it is the
     * filename given by the user. For an extract function it is the filename of
     * the extracted file. of the extracted file. stored_filename : Name of the
     * file / directory stored in the archive. stored_filename : Name of the
     * file / directory stored in the archive. size : Size of the stored file.
     * size : Size of the stored file. compressed_size : Size of the file's data
     * compressed in the archive compressed_size : Size of the file's data
     * compressed in the archive (without the headers overhead) (without the
     * headers overhead) mtime : Last known modification date of the file (UNIX
     * timestamp) mtime : Last known modification date of the file (UNIX
     * timestamp) comment : Comment associated with the file comment : Comment
     * associated with the file folder : true | false folder : true | false
     * index : index of the file in the archive index : index of the file in the
     * archive status : status of the action (depending of the action) : status :
     * status of the action (depending of the action) : Values are : Values are :
     * ok : OK ! ok : OK ! filtered : the file / dir is not extracted (filtered
     * by user) filtered : the file / dir is not extracted (filtered by user)
     * already_a_directory : the file can not be extracted because a
     * already_a_directory : the file can not be extracted because a directory
     * with the same name already exists directory with the same name already
     * exists write_protected : the file can not be extracted because a file
     * write_protected : the file can not be extracted because a file with the
     * same name already exists and is with the same name already exists and is
     * write protected write protected newer_exist : the file was not extracted
     * because a newer file exists newer_exist : the file was not extracted
     * because a newer file exists path_creation_fail : the file is not
     * extracted because the folder path_creation_fail : the file is not
     * extracted because the folder does not exists and can not be created does
     * not exists and can not be created write_error : the file was not
     * extracted because there was a write_error : the file was not extracted
     * because there was a error while writing the file error while writing the
     * file read_error : the file was not extracted because there was a error
     * read_error : the file was not extracted because there was a error while
     * reading the file while reading the file invalid_header : the file was not
     * extracted because of an archive invalid_header : the file was not
     * extracted because of an archive format error (bad file header) format
     * error (bad file header) Note that each time a method can continue
     * operating when there Note that each time a method can continue operating
     * when there is an action error on a file, the error is only logged in the
     * file status. is an action error on a file, the error is only logged in
     * the file status. Return Values : Return Values : 0 on an unrecoverable
     * failure, 0 on an unrecoverable failure, The list of the files in the
     * archive. The list of the files in the archive.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public Array<Object> listContent() {
        int v_result = 0;
        Array<Object> p_list = new Array<Object>();
        v_result = 1;
        this.privErrorReset();

        if (!this.privCheckFormat())/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        p_list = new Array<Object>();

        if (!equal(v_result = this.privList(p_list), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0,
         * PclZip::errorInfo()); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, 0, PclZip::errorInfo());
         */
         {
            p_list = null;

            return new Array<Object>();
        }

        return p_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : Function : extract($p_path="./", $p_remove_path="")
     * extract($p_path="./", $p_remove_path="") extract([$p_option,
     * $p_option_value, ...]) extract([$p_option, $p_option_value, ...])
     * Description : Description : This method supports two synopsis. The first
     * one is historical. This method supports two synopsis. The first one is
     * historical. This method extract all the files / directories from the
     * archive to the This method extract all the files / directories from the
     * archive to the folder indicated in $p_path. folder indicated in $p_path.
     * If you want to ignore the 'root' part of path of the memorized files If
     * you want to ignore the 'root' part of path of the memorized files you can
     * indicate this in the optional $p_remove_path parameter. you can indicate
     * this in the optional $p_remove_path parameter. By default, if a newer
     * file with the same name already exists, the By default, if a newer file
     * with the same name already exists, the file is not extracted. file is not
     * extracted.
     * If both PCLZIP_OPT_PATH and PCLZIP_OPT_ADD_PATH aoptions If both
     * PCLZIP_OPT_PATH and PCLZIP_OPT_ADD_PATH aoptions are used, the path
     * indicated in PCLZIP_OPT_ADD_PATH is append are used, the path indicated
     * in PCLZIP_OPT_ADD_PATH is append at the end of the path value of
     * PCLZIP_OPT_PATH. at the end of the path value of PCLZIP_OPT_PATH.
     * Parameters : Parameters : $p_path : Path where the files and directories
     * are to be extracted $p_path : Path where the files and directories are to
     * be extracted $p_remove_path : First part ('root' part) of the memorized
     * path $p_remove_path : First part ('root' part) of the memorized path (if
     * any similar) to remove while extracting. (if any similar) to remove while
     * extracting. Options : Options : PCLZIP_OPT_PATH : PCLZIP_OPT_PATH :
     * PCLZIP_OPT_ADD_PATH : PCLZIP_OPT_ADD_PATH : PCLZIP_OPT_REMOVE_PATH :
     * PCLZIP_OPT_REMOVE_PATH : PCLZIP_OPT_REMOVE_ALL_PATH :
     * PCLZIP_OPT_REMOVE_ALL_PATH : PCLZIP_CB_PRE_EXTRACT :
     * PCLZIP_CB_PRE_EXTRACT : PCLZIP_CB_POST_EXTRACT : PCLZIP_CB_POST_EXTRACT :
     * Return Values : Return Values : 0 or a negative value on failure, 0 or a
     * negative value on failure, The list of the extracted files, with a status
     * of the action. The list of the extracted files, with a status of the
     * action. (see PclZip::listContent() for list entry format) (see
     * PclZip::listContent() for list entry format)
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public Array<Object> extract(Object... vargs) {
        Object v_result = null;
        Array<Object> v_options = new Array<Object>();
        String v_path = null;
        String v_remove_path = null;
        Object v_remove_all_path = null;
        int v_size = 0;
        Array<Object> v_arg_list = new Array<Object>();
        Array<Object> p_list = new Array<Object>();
        v_result = 1;
        this.privErrorReset();

        if (!this.privCheckFormat())/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        v_options = new Array<Object>();
        v_path = "";
        v_remove_path = "";
        v_remove_all_path = false;
        v_size = FunctionHandling.func_num_args(vargs);
        v_options.putValue(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING(), false);

        if (v_size > 0)/*
         * ----- Get the arguments ----- Get the arguments
         */

        /*
         * ----- Look for first arg ----- Look for first arg
         */
         {
            v_arg_list = FunctionHandling.func_get_args(vargs);

            if (is_integer(v_arg_list.getValue(0)) && (intval(v_arg_list.getValue(0)) > 77000))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options"); ----- Parse the options ----- Parse
             * the options
             */

            /*
             * ----- Set the arguments ----- Set the arguments
             */
             {
                v_result = this.privParseOptions(v_arg_list, v_size, v_options,
                        new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_ADD_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_PRE_EXTRACT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_POST_EXTRACT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_SET_CHMOD(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_NAME(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_EREG(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_PREG(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_INDEX(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_EXTRACT_IN_OUTPUT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REPLACE_NEWER(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_STOP_ON_ERROR(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_EXTRACT_DIR_RESTRICTION(), "optional")));

                if (!equal(v_result, 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 */
                 {
                    return new Array<Object>();
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_PATH()))) {
                    v_path = strval(v_options.getValue(gConsts.getPCLZIP_OPT_PATH()));
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_PATH()))) {
                    v_remove_path = strval(v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_PATH()));
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH()))) {
                    v_remove_all_path = v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH());
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_ADD_PATH())))/*
                 * ----- Check for '/' in last path char ----- Check for '/' in
                 * last path char
                 */
                 {
                    if ((Strings.strlen(v_path) > 0) && !equal(Strings.substr(v_path, -1), "/")) {
                        v_path = v_path + "/";
                    }

                    v_path = v_path + strval(v_options.getValue(gConsts.getPCLZIP_OPT_ADD_PATH()));
                }
            } else/*
             * ----- Look for 2 args ----- Look for 2 args Here we need to
             * support the first historic synopsis of the Here we need to
             * support the first historic synopsis of the method. method.
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis"); ----- Get the first argument ----- Get the
             * first argument
             */

            /*
             * ----- Look for the optional second argument ----- Look for the
             * optional second argument
             */
             {
                v_path = strval(v_arg_list.getValue(0));

                if (equal(v_size, 2)) {
                    v_remove_path = strval(v_arg_list.getValue(1));
                } else if (v_size > 2)/*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * 0, PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * 0, PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid number / type of arguments");

                    return new Array<Object>();
                }
            }
        }

        p_list = new Array<Object>();
        v_result = this.privExtractByRule(p_list, v_path, v_remove_path, v_remove_all_path, v_options);

        if (intval(v_result) < 1)/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0,
         * PclZip::errorInfo()); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, 0, PclZip::errorInfo());
         */
         {
            p_list = null;

            return new Array<Object>();
        }

        return p_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : Function : extractByIndex($p_index, $p_path="./",
     * $p_remove_path="") extractByIndex($p_index, $p_path="./",
     * $p_remove_path="") extractByIndex($p_index, [$p_option, $p_option_value,
     * ...]) extractByIndex($p_index, [$p_option, $p_option_value, ...])
     * Description : Description : This method supports two synopsis. The first
     * one is historical. This method supports two synopsis. The first one is
     * historical. This method is doing a partial extract of the archive. This
     * method is doing a partial extract of the archive. The extracted files or
     * folders are identified by their index in the The extracted files or
     * folders are identified by their index in the archive (from 0 to n).
     * archive (from 0 to n). Note that if the index identify a folder, only the
     * folder entry is Note that if the index identify a folder, only the folder
     * entry is extracted, not all the files included in the archive. extracted,
     * not all the files included in the archive. Parameters : Parameters :
     * $p_index : A single index (integer) or a string of indexes of files to
     * $p_index : A single index (integer) or a string of indexes of files to
     * extract. The form of the string is "0,4-6,8-12" with only numbers
     * extract. The form of the string is "0,4-6,8-12" with only numbers and '-'
     * for range or ',' to separate ranges. No spaces or ';' and '-' for range
     * or ',' to separate ranges. No spaces or ';' are allowed. are allowed.
     * $p_path : Path where the files and directories are to be extracted
     * $p_path : Path where the files and directories are to be extracted
     * $p_remove_path : First part ('root' part) of the memorized path
     * $p_remove_path : First part ('root' part) of the memorized path (if any
     * similar) to remove while extracting. (if any similar) to remove while
     * extracting. Options : Options : PCLZIP_OPT_PATH : PCLZIP_OPT_PATH :
     * PCLZIP_OPT_ADD_PATH : PCLZIP_OPT_ADD_PATH : PCLZIP_OPT_REMOVE_PATH :
     * PCLZIP_OPT_REMOVE_PATH : PCLZIP_OPT_REMOVE_ALL_PATH :
     * PCLZIP_OPT_REMOVE_ALL_PATH : PCLZIP_OPT_EXTRACT_AS_STRING : The files are
     * extracted as strings and PCLZIP_OPT_EXTRACT_AS_STRING : The files are
     * extracted as strings and not as files. not as files. The resulting
     * content is in a new field 'content' in the file The resulting content is
     * in a new field 'content' in the file structure. structure. This option
     * must be used alone (any other options are ignored). This option must be
     * used alone (any other options are ignored). PCLZIP_CB_PRE_EXTRACT :
     * PCLZIP_CB_PRE_EXTRACT : PCLZIP_CB_POST_EXTRACT : PCLZIP_CB_POST_EXTRACT :
     * Return Values : Return Values : 0 on failure, 0 on failure, The list of
     * the extracted files, with a status of the action. The list of the
     * extracted files, with a status of the action. (see PclZip::listContent()
     * for list entry format) (see PclZip::listContent() for list entry format)
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * function extractByIndex($p_index, options...) function
     * extractByIndex($p_index, options...)
     */
    public Array<Object> extractByIndex(Object p_index, Object... vargs) {
        Object v_result = null;
        Array<Object> v_options = new Array<Object>();
        String v_path = null;
        String v_remove_path = null;
        Object v_remove_all_path = null;
        int v_size = 0;
        Array<Object> v_arg_list = new Array<Object>();
        Array<Object> v_arg_trick = new Array<Object>();
        Array<Object> v_options_trick = new Array<Object>();
        Array<Object> p_list = null;
        v_result = 1;
        this.privErrorReset();

        if (!this.privCheckFormat())/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        v_options = new Array<Object>();
        v_path = "";
        v_remove_path = "";
        v_remove_all_path = false;

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(p_index, vargs);
        v_size = FunctionHandling.func_num_args(totalArgs);
        v_options.putValue(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING(), false);

        if (v_size > 1)/*
         * ----- Get the arguments ----- Get the arguments
         */

        /*
         * ----- Remove form the options list the first argument ----- Remove
         * form the options list the first argument
         */

        /*
         * ----- Look for first arg ----- Look for first arg
         */
         {
            v_arg_list = FunctionHandling.func_get_args(totalArgs);
            Array.array_shift(v_arg_list);
            v_size--;

            if (is_integer(v_arg_list.getValue(0)) && (intval(v_arg_list.getValue(0)) > 77000))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Variable list of options"); ----- Parse the options ----- Parse
             * the options
             */

            /*
             * ----- Set the arguments ----- Set the arguments
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Option PCLZIP_OPT_EXTRACT_AS_STRING not set.");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Option PCLZIP_OPT_EXTRACT_AS_STRING not set.");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Option PCLZIP_OPT_EXTRACT_AS_STRING set.");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Option PCLZIP_OPT_EXTRACT_AS_STRING set.");
             */
             {
                v_result = this.privParseOptions(v_arg_list, v_size, v_options,
                        new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_ADD_PATH(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_PRE_EXTRACT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_CB_POST_EXTRACT(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_SET_CHMOD(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_REPLACE_NEWER(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_STOP_ON_ERROR(), "optional"),
                            new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_EXTRACT_DIR_RESTRICTION(), "optional")));

                if (!equal(v_result, 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
                 */
                 {
                    return new Array<Object>();
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_PATH()))) {
                    v_path = strval(v_options.getValue(gConsts.getPCLZIP_OPT_PATH()));
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_PATH()))) {
                    v_remove_path = strval(v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_PATH()));
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH()))) {
                    v_remove_all_path = v_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH());
                }

                if (isset(v_options.getValue(gConsts.getPCLZIP_OPT_ADD_PATH())))/*
                 * ----- Check for '/' in last path char ----- Check for '/' in
                 * last path char
                 */
                 {
                    if ((Strings.strlen(v_path) > 0) && !equal(Strings.substr(v_path, -1), "/")) {
                        v_path = v_path + "/";
                    }

                    v_path = v_path + strval(v_options.getValue(gConsts.getPCLZIP_OPT_ADD_PATH()));
                }

                if (!isset(v_options.getValue(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING()))) {
                    v_options.putValue(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING(), false);
                } else {
                }
            } else/*
             * ----- Look for 2 args ----- Look for 2 args Here we need to
             * support the first historic synopsis of the Here we need to
             * support the first historic synopsis of the method. method.
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Static synopsis"); ----- Get the first argument ----- Get the
             * first argument
             */

            /*
             * ----- Look for the optional second argument ----- Look for the
             * optional second argument
             */
             {
                v_path = strval(v_arg_list.getValue(0));

                if (equal(v_size, 2)) {
                    v_remove_path = strval(v_arg_list.getValue(1));
                } else if (v_size > 2)/*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid number / type of arguments");

                    return new Array<Object>();
                }
            }
        }

        v_arg_trick = new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_INDEX()), new ArrayEntry<Object>(p_index));
        v_options_trick = new Array<Object>();
        v_result = this.privParseOptions(v_arg_trick, Array.sizeof(v_arg_trick), v_options_trick, new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_INDEX(), "optional")));

        if (!equal(v_result, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        v_options.putValue(gConsts.getPCLZIP_OPT_BY_INDEX(), v_options_trick.getValue(gConsts.getPCLZIP_OPT_BY_INDEX()));

        if (intval(v_result = this.privExtractByRule(p_list, v_path, v_remove_path, v_remove_all_path, v_options)) < 1)/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0,
         * PclZip::errorInfo()); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, 0, PclZip::errorInfo());
         */
         {
            return new Array<Object>();
        }

        return p_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : Function : delete([$p_option, $p_option_value, ...])
     * delete([$p_option, $p_option_value, ...]) Description : Description :
     * This method removes files from the archive. This method removes files
     * from the archive. If no parameters are given, then all the archive is
     * emptied. If no parameters are given, then all the archive is emptied.
     * Parameters : Parameters : None or optional arguments. None or optional
     * arguments. Options : Options : PCLZIP_OPT_BY_INDEX : PCLZIP_OPT_BY_INDEX :
     * PCLZIP_OPT_BY_NAME : PCLZIP_OPT_BY_NAME : PCLZIP_OPT_BY_EREG :
     * PCLZIP_OPT_BY_EREG : PCLZIP_OPT_BY_PREG : PCLZIP_OPT_BY_PREG : Return
     * Values : Return Values : 0 on failure, 0 on failure, The list of the
     * files which are still present in the archive. The list of the files which
     * are still present in the archive. (see PclZip::listContent() for list
     * entry format) (see PclZip::listContent() for list entry format)
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public Array<Object> delete(Object... vargs) {
        int v_result = 0;
        Array<Object> v_options = new Array<Object>();
        int v_size = 0;
        Array<Object> v_arg_list = new Array<Object>();
        Array<Object> v_list = new Array<Object>();
        v_result = 1;
        this.privErrorReset();

        if (!this.privCheckFormat())/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return new Array<Object>();
        }

        v_options = new Array<Object>();
        v_size = FunctionHandling.func_num_args(vargs);

        if (v_size > 0)/*
         * ----- Get the arguments ----- Get the arguments
         */

        /*
         * ----- Parse the options ----- Parse the options
         */
         {
            v_arg_list = FunctionHandling.func_get_args(vargs);
            v_result = this.privParseOptions(v_arg_list, v_size, v_options,
                    new Array<Object>(new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_NAME(), "optional"),
                        new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_EREG(), "optional"),
                        new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_PREG(), "optional"),
                        new ArrayEntry<Object>(gConsts.getPCLZIP_OPT_BY_INDEX(), "optional")));

            if (!equal(v_result, 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             */
             {
                return new Array<Object>();
            }
        }

        this.privDisableMagicQuotes();
        v_list = new Array<Object>();

        if (!equal(v_result = this.privDeleteByRule(v_list, v_options), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0,
         * PclZip::errorInfo()); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, 0, PclZip::errorInfo());
         */
         {
            this.privSwapBackMagicQuotes();
            v_list = null;

            return new Array<Object>();
        }

        this.privSwapBackMagicQuotes();

        return v_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : deleteByIndex() Function : deleteByIndex() Description :
     * Description : **** Deprecated ***** **** Deprecated *****
     * delete(PCLZIP_OPT_BY_INDEX, $p_index) should be prefered.
     * delete(PCLZIP_OPT_BY_INDEX, $p_index) should be prefered.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public Array<Object> deleteByIndex(Object p_index) {
        Array<Object> p_list = new Array<Object>();
        p_list = this.delete(gConsts.getPCLZIP_OPT_BY_INDEX(), p_index);

        return p_list;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : properties() Function : properties() Description : Description :
     * This method gives the properties of the archive. This method gives the
     * properties of the archive. The properties are : The properties are : nb :
     * Number of files in the archive nb : Number of files in the archive
     * comment : Comment associated with the archive file comment : Comment
     * associated with the archive file status : not_exist, ok status :
     * not_exist, ok Parameters : Parameters : None None Return Values : Return
     * Values : 0 on failure, 0 on failure, An array with the archive
     * properties. An array with the archive properties.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public Array<Object> properties() {
        Array<Object> v_prop = new Array<Object>();
        Array<Object> v_central_dir = new Array<Object>();
        int v_result = 0;
        this.privErrorReset();
        this.privDisableMagicQuotes();

        if (!this.privCheckFormat())/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            this.privSwapBackMagicQuotes();

            return new Array<Object>();
        }

        v_prop = new Array<Object>();
        v_prop.putValue("comment", "");
        v_prop.putValue("nb", 0);
        v_prop.putValue("status", "not_exist");

        if (FileSystemOrSocket.is_file(gVars.webEnv, this.zipname))/*
         * ----- Open the zip file ----- Open the zip file
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "Open
         * file in binary read mode");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "Open
         * file in binary read mode");
         *
         * ----- Read the central directory informations ----- Read the central
         * directory informations
         */

        /*
         * ----- Close the zip file ----- Close the zip file
         */

        /*
         * ----- Set the user attributes ----- Set the user attributes
         */
         {
            if (equal(this.zip_fd = FileSystemOrSocket.fopen(gVars.webEnv, this.zipname, "rb"), 0))/*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), 0);
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), 0);
             */
             {
                this.privSwapBackMagicQuotes();
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open archive \'" + this.zipname + "\' in binary read mode");

                return new Array<Object>();
            }

            v_central_dir = new Array<Object>();

            if (!equal(v_result = this.privReadEndCentralDir(v_central_dir), 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
             */
             {
                this.privSwapBackMagicQuotes();

                return new Array<Object>();
            }

            this.privCloseFd();
            v_prop.putValue("comment", v_central_dir.getValue("comment"));
            v_prop.putValue("nb", v_central_dir.getValue("entries"));
            v_prop.putValue("status", "ok");
        }

        this.privSwapBackMagicQuotes();

        return v_prop;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : duplicate() Function : duplicate() Description : Description :
     * This method creates an archive by copying the content of an other one. If
     * This method creates an archive by copying the content of an other one. If
     * the archive already exist, it is replaced by the new one without any
     * warning. the archive already exist, it is replaced by the new one without
     * any warning. Parameters : Parameters : $p_archive : The filename of a
     * valid archive, or $p_archive : The filename of a valid archive, or a
     * valid PclZip object. a valid PclZip object. Return Values : Return Values :
     * 1 on success. 1 on success. 0 or a negative value on error (error code).
     * 0 or a negative value on error (error code).
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int duplicate(Object p_archive)/* Do not change type */
     {
        int v_result = 0;
        v_result = 1;
        this.privErrorReset();

        /*
         * ----- Look if the $p_archive is a string (so a filename) ----- Look
         * if the $p_archive is a string (so a filename)
         */
        if (is_object(p_archive) && equal(ClassHandling.get_class(p_archive), "pclzip"))// TODO Numiton: BUG Invalid class name case

        /*
         * >zipname."'"); zipname."'"); ----- Duplicate the archive -----
         * Duplicate the archive
         */
         {
            v_result = this.privDuplicate(((PclZip) p_archive).zipname);
        } else if (is_string(p_archive))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "The parameter is a filename '$p_archive'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "The parameter is a filename '$p_archive'"); ----- Check that
         * $p_archive is a valid zip file ----- Check that $p_archive is a
         * valid zip file TBC : Should also check the archive format TBC :
         * Should also check the archive format
         */
         {
            if (!FileSystemOrSocket.is_file(gVars.webEnv, strval(p_archive)))/*
             * ----- Error log ----- Error log
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_MISSING_FILE(), "No file with filename \'" + strval(p_archive) + "\'");
                v_result = gConsts.getPCLZIP_ERR_MISSING_FILE();
            } else/*
             * ----- Duplicate the archive ----- Duplicate the archive
             */
             {
                v_result = this.privDuplicate(strval(p_archive));
            }
        } else/*
         * ----- Invalid variable ----- Invalid variable ----- Error log
         * ----- Error log
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid variable type p_archive_to_add");
            v_result = gConsts.getPCLZIP_ERR_INVALID_PARAMETER();
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : merge() Function : merge() Description : Description : This
     * method merge the $p_archive_to_add archive at the end of the current This
     * method merge the $p_archive_to_add archive at the end of the current one
     * ($this). one ($this). If the archive ($this) does not exist, the merge
     * becomes a duplicate. If the archive ($this) does not exist, the merge
     * becomes a duplicate. If the $p_archive_to_add archive does not exist, the
     * merge is a success. If the $p_archive_to_add archive does not exist, the
     * merge is a success. Parameters : Parameters : $p_archive_to_add : It can
     * be directly the filename of a valid zip archive, $p_archive_to_add : It
     * can be directly the filename of a valid zip archive, or a PclZip object
     * archive. or a PclZip object archive. Return Values : Return Values : 1 on
     * success, 1 on success, 0 or negative values on error (see below). 0 or
     * negative values on error (see below).
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int merge(Object p_archive_to_add)/* Do not change type */
     {
        int v_result = 0;
        PclZip v_object_archive = null;
        v_result = 1;
        this.privErrorReset();

        if (!this.privCheckFormat())/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, 0);
         */
         {
            return 0;
        }

        /*
         * ----- Look if the $p_archive_to_add is a string (so a filename) -----
         * Look if the $p_archive_to_add is a string (so a filename)
         */
        if (is_object(p_archive_to_add) && equal(ClassHandling.get_class(p_archive_to_add), "pclzip"))// TODO Numiton: BUG Invalid class name case

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "The
         * parameter is valid PclZip object");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "The
         * parameter is valid PclZip object"); ----- Merge the archive -----
         * Merge the archive
         */
         {
            v_result = this.privMerge((PclZip) p_archive_to_add);
        } else if (is_string(p_archive_to_add))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "The parameter is a filename");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "The parameter is a filename"); ----- Create a temporary archive
         * ----- Create a temporary archive
         */

        /*
         * ----- Merge the archive ----- Merge the archive
         */
         {
            v_object_archive = new PclZip(gVars, gConsts, strval(p_archive_to_add));
            v_result = this.privMerge(v_object_archive);
        } else/*
         * ----- Invalid variable ----- Invalid variable ----- Error log
         * ----- Error log
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid variable type p_archive_to_add");
            v_result = gConsts.getPCLZIP_ERR_INVALID_PARAMETER();
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : errorCode() Function : errorCode() Description : Description :
     * Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int errorCode() {
        if (equal(gConsts.getPCLZIP_ERROR_EXTERNAL(), 1)) {
            // Commented by Numiton
            throw new RuntimeException("PCLERROR extension not supported");
        } else//			return PclErrorCode();
         {
            return this.error_code;
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : errorName() Function : errorName() Description : Description :
     * Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public String errorName(boolean p_with_code) {
        Array<Object> v_name = new Array<Object>();
        String v_value = null;
        v_name = new Array<Object>(
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_NO_ERROR(), "PCLZIP_ERR_NO_ERROR"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_WRITE_OPEN_FAIL(), "PCLZIP_ERR_WRITE_OPEN_FAIL"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "PCLZIP_ERR_READ_OPEN_FAIL"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "PCLZIP_ERR_INVALID_PARAMETER"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_MISSING_FILE(), "PCLZIP_ERR_MISSING_FILE"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_FILENAME_TOO_LONG(), "PCLZIP_ERR_FILENAME_TOO_LONG"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_INVALID_ZIP(), "PCLZIP_ERR_INVALID_ZIP"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_BAD_EXTRACTED_FILE(), "PCLZIP_ERR_BAD_EXTRACTED_FILE"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_DIR_CREATE_FAIL(), "PCLZIP_ERR_DIR_CREATE_FAIL"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_BAD_EXTENSION(), "PCLZIP_ERR_BAD_EXTENSION"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "PCLZIP_ERR_BAD_FORMAT"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_DELETE_FILE_FAIL(), "PCLZIP_ERR_DELETE_FILE_FAIL"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_RENAME_FILE_FAIL(), "PCLZIP_ERR_RENAME_FILE_FAIL"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_BAD_CHECKSUM(), "PCLZIP_ERR_BAD_CHECKSUM"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_INVALID_ARCHIVE_ZIP(), "PCLZIP_ERR_INVALID_ARCHIVE_ZIP"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(), "PCLZIP_ERR_MISSING_OPTION_VALUE"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(), "PCLZIP_ERR_INVALID_OPTION_VALUE"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_UNSUPPORTED_COMPRESSION(), "PCLZIP_ERR_UNSUPPORTED_COMPRESSION"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_UNSUPPORTED_ENCRYPTION(), "PCLZIP_ERR_UNSUPPORTED_ENCRYPTION"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(), "PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE"),
                new ArrayEntry<Object>(gConsts.getPCLZIP_ERR_DIRECTORY_RESTRICTION(), "PCLZIP_ERR_DIRECTORY_RESTRICTION"));

        if (isset(v_name.getValue(this.error_code))) {
            v_value = strval(v_name.getValue(this.error_code));
        } else {
            v_value = "NoName";
        }

        if (p_with_code) {
            return v_value + " (" + strval(this.error_code) + ")";
        } else {
            return v_value;
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : errorInfo() Function : errorInfo() Description : Description :
     * Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public String errorInfo(Object p_full) {
        if (equal(gConsts.getPCLZIP_ERROR_EXTERNAL(), 1)) {
            // Commented by Numiton
            throw new RuntimeException("PCLERROR extension not supported");
        } else//			return PclErrorString();
         {
            if (booleanval(p_full)) {
                return this.errorName(true) + " : " + this.error_string;
            } else {
                return this.error_string + " [code " + strval(this.error_code) + "]";
            }
        }
    }

    public boolean privCheckFormat() {
        return privCheckFormat(0);
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * UNDER THIS LINE ARE DEFINED PRIVATE INTERNAL FUNCTIONS ***** ****
     * UNDER THIS LINE ARE DEFINED PRIVATE INTERNAL FUNCTIONS ***** **** *****
     * THESES FUNCTIONS MUST NOT BE USED DIRECTLY ***** ****
     * THESES FUNCTIONS MUST NOT BE USED DIRECTLY *****
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privCheckFormat() Function : privCheckFormat() Description :
     * Description : This method check that the archive exists and is a valid
     * zip archive. This method check that the archive exists and is a valid zip
     * archive. Several level of check exists. (futur) Several level of check
     * exists. (futur) Parameters : Parameters : $p_level : Level of check.
     * Default 0. $p_level : Level of check. Default 0. 0 : Check the first
     * bytes (magic codes) (default value)) 0 : Check the first bytes (magic
     * codes) (default value)) 1 : 0 + Check the central directory (futur) 1 : 0 +
     * Check the central directory (futur) 2 : 1 + Check each file header
     * (futur) 2 : 1 + Check each file header (futur) Return Values : Return
     * Values : true on success, true on success, false on error, the error code
     * is set. false on error, the error code is set.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public boolean privCheckFormat(int p_level) {
        boolean v_result = false;
        v_result = true;
        FileSystemOrSocket.clearstatcache(gVars.webEnv);
        this.privErrorReset();

        if (!FileSystemOrSocket.is_file(gVars.webEnv, this.zipname))/*
         * ----- Error log ----- Error log
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, false,
         * PclZip::errorInfo()); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, false, PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_MISSING_FILE(), "Missing archive file \'" + this.zipname + "\'");

            return false;
        }

        if (!FileSystemOrSocket.is_readable(gVars.webEnv, this.zipname))/*
         * ----- Error log ----- Error log
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, false,
         * PclZip::errorInfo()); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, false, PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to read archive \'" + this.zipname + "\'");

            return false;
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privParseOptions() Function : privParseOptions() Description :
     * Description : This internal methods reads the variable list of arguments
     * ($p_options_list, This internal methods reads the variable list of
     * arguments ($p_options_list, $p_size) and generate an array with the
     * options and values ($v_result_list). $p_size) and generate an array with
     * the options and values ($v_result_list). $v_requested_options contains
     * the options that can be present and those that $v_requested_options
     * contains the options that can be present and those that must be present.
     * must be present. $v_requested_options is an array, with the option value
     * as key, and 'optional', $v_requested_options is an array, with the option
     * value as key, and 'optional', or 'mandatory' as value. or 'mandatory' as
     * value. Parameters : Parameters : See above. See above. Return Values :
     * Return Values : 1 on success. 1 on success. 0 on failure. 0 on failure.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privParseOptions(Array<Object> p_options_list, int p_size, Array<Object> v_result_list, Array<Object> v_requested_options) {
        int v_result = 0;
        int i = 0;
        Array<String> v_work_list = new Array<String>();
        boolean v_sort_flag = false;
        int v_sort_value = 0;
        Array<String> v_item_list = new Array<String>();
        int j = 0;
        int v_size_item_list = 0;
        String v_function_name = null;
        Object key;
        v_result = 1;
        i = 0;

        while (i < p_size)/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
         * "Looking for table index $i, option =
         * '".PclZipUtilOptionText($p_options_list[$i])."(".$p_options_list[$i].")'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
         * "Looking for table index $i, option =
         * '".PclZipUtilOptionText($p_options_list[$i])."(".$p_options_list[$i].")'");
         * ----- Check if the option is supported ----- Check if the option is
         * supported
         *
         * ----- Look for next option ----- Look for next option
         */

        /*
         * ----- Next options ----- Next options
         */
         {
            if (!isset(v_requested_options.getValue(p_options_list.getValue(i))))/*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid optional parameter \'" + strval(p_options_list.getValue(i)) + "\' for this method");

                return gVars.getSharedPclZip().errorCode();
            }
            /*
             * ----- Look for options that request a path value ----- Look for
             * options that request a path value
             */

            /*
             * ----- Look for options that request an array of string for value
             * ----- Look for options that request an array of string for value
             */

            /*
             * ----- Look for options that request an EREG or PREG expression
             * ----- Look for options that request an EREG or PREG expression
             */

            /*
             * ----- Look for options that takes a string ----- Look for options
             * that takes a string
             */

            /*
             * ----- Look for options that request an array of index ----- Look
             * for options that request an array of index
             */

            /*
             * ----- Look for options that request no value ----- Look for
             * options that request no value
             */

            /*
             * ----- Look for options that request an octal value ----- Look for
             * options that request an octal value
             */

            /*
             * ----- Look for options that request a call-back ----- Look for
             * options that request a call-back
             */
            {
                int javaSwitchSelector10 = 0;

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_PATH())) {
                    javaSwitchSelector10 = 1;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_REMOVE_PATH())) {
                    javaSwitchSelector10 = 2;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_ADD_PATH())) {
                    javaSwitchSelector10 = 3;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_EXTRACT_DIR_RESTRICTION())) {
                    javaSwitchSelector10 = 4;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_BY_NAME())) {
                    javaSwitchSelector10 = 5;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_BY_EREG())) {
                    javaSwitchSelector10 = 6;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_BY_PREG())) {
                    javaSwitchSelector10 = 7;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_COMMENT())) {
                    javaSwitchSelector10 = 8;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_ADD_COMMENT())) {
                    javaSwitchSelector10 = 9;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_PREPEND_COMMENT())) {
                    javaSwitchSelector10 = 10;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_BY_INDEX())) {
                    javaSwitchSelector10 = 11;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH())) {
                    javaSwitchSelector10 = 12;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING())) {
                    javaSwitchSelector10 = 13;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_NO_COMPRESSION())) {
                    javaSwitchSelector10 = 14;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_EXTRACT_IN_OUTPUT())) {
                    javaSwitchSelector10 = 15;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_REPLACE_NEWER())) {
                    javaSwitchSelector10 = 16;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_STOP_ON_ERROR())) {
                    javaSwitchSelector10 = 17;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_OPT_SET_CHMOD())) {
                    javaSwitchSelector10 = 18;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_CB_PRE_EXTRACT())) {
                    javaSwitchSelector10 = 19;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_CB_POST_EXTRACT())) {
                    javaSwitchSelector10 = 20;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_CB_PRE_ADD())) {
                    javaSwitchSelector10 = 21;
                }

                if (equal(p_options_list.getValue(i), gConsts.getPCLZIP_CB_POST_ADD())) {
                    javaSwitchSelector10 = 22;
                }

                switch (javaSwitchSelector10) {
                case 1: {
                }

                case 2: {
                }

                case 3:/*
                 * ----- Check the number of parameters ----- Check the
                 * number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    v_result_list.putValue(p_options_list.getValue(i), getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilTranslateWinPath(strval(p_options_list.getValue(i + 1)), false));
                    i++;

                    break;
                }

                case 4:/*
                 * ----- Check the number of parameters ----- Check the
                 * number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." set
                 * with an empty value is ignored.");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." set
                 * with an empty value is ignored.");
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    if (is_string(p_options_list.getValue(i + 1)) && !equal(p_options_list.getValue(i + 1), ""))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2,
                     * "".PclZipUtilOptionText($p_options_list[$i])." =
                     * '".$v_result_list[$p_options_list[$i]]."'");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2,
                     * "".PclZipUtilOptionText($p_options_list[$i])." =
                     * '".$v_result_list[$p_options_list[$i]]."'");
                     */
                     {
                        v_result_list.putValue(p_options_list.getValue(i), getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilTranslateWinPath(strval(p_options_list.getValue(i + 1)), false));
                        i++;
                    } else {
                    }

                    break;
                }

                case 5:/*
                 * ----- Check the number of parameters ----- Check the
                 * number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    if (is_string(p_options_list.getValue(i + 1))) {
                        v_result_list.getArrayValue(p_options_list.getValue(i)).putValue(0, p_options_list.getValue(i + 1));
                    } else if (is_array(p_options_list.getValue(i + 1))) {
                        v_result_list.putValue(p_options_list.getValue(i), p_options_list.getValue(i + 1));
                    } else/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(),
                     * PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(),
                     * PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                            "Wrong parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    i++;

                    break;
                }

                case 6: {
                }

                case 7:/*
                 * case PCLZIP_OPT_CRYPT : case PCLZIP_OPT_CRYPT : -----
                 * Check the number of parameters ----- Check the number
                 * of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    if (is_string(p_options_list.getValue(i + 1))) {
                        v_result_list.putValue(p_options_list.getValue(i), p_options_list.getValue(i + 1));
                    } else/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                            "Wrong parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    i++;

                    break;
                }

                case 8: {
                }

                case 9: {
                }

                case 10:/*
                 * ----- Check the number of parameters ----- Check the
                 * number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    if (is_string(p_options_list.getValue(i + 1))) {
                        v_result_list.putValue(p_options_list.getValue(i), p_options_list.getValue(i + 1));
                    } else/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                            "Wrong parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    i++;

                    break;
                }

                case 11:/*
                 * ----- Check the number of parameters ----- Check the
                 * number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * ----- Reduce the index list ----- Reduce the index
                 * list each index item in the list must be a couple
                 * with a start and each index item in the list must be
                 * a couple with a start and an end value : [0,3],
                 * [5-5], [8-10], ... an end value : [0,3], [5-5],
                 * [8-10], ... ----- Check the format of each item -----
                 * Check the format of each item
                 */

                /*
                 * ----- Sort the items ----- Sort the items
                 *
                 * TBC : To Be Completed TBC : To Be Completed
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "List sorting is not yet write ...");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "List sorting is not yet write ...");
                 * ----- Next option ----- Next option
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    v_work_list = new Array<String>();

                    if (is_string(p_options_list.getValue(i + 1)))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Index value is a string
                     * '".$p_options_list[$i+1]."'");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Index value is a string
                     * '".$p_options_list[$i+1]."'"); ----- Remove spaces
                     * ----- Remove spaces
                     */

                    /*
                     * ----- Parse items ----- Parse items
                     */
                     {
                        p_options_list.putValue(i + 1, Strings.strtr(strval(p_options_list.getValue(i + 1)), " ", ""));
                        v_work_list = Strings.explode(",", strval(p_options_list.getValue(i + 1)));
                    } else if (is_integer(p_options_list.getValue(i + 1)))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Index value is an integer
                     * '".$p_options_list[$i+1]."'");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Index value is an integer
                     * '".$p_options_list[$i+1]."'");
                     */
                     {
                        v_work_list.putValue(0, strval(p_options_list.getValue(i + 1)) + "-" + strval(p_options_list.getValue(i + 1)));
                    } else if (is_array(p_options_list.getValue(i + 1)))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Index value is an array");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Index value is an array");
                     */
                     {
                        v_work_list = (Array<String>) p_options_list.getValue(i + 1);
                    } else/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(),
                     * PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(),
                     * PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                            "Value must be integer, string or array for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    v_sort_flag = false;
                    v_sort_value = 0;

                    for (j = 0; j < Array.sizeof(v_work_list); j++)/*
                     * ----- Explode the item ----- Explode the item
                     */

                    /*
                     * ----- TBC : Here we might check that each item is a
                     * ----- TBC : Here we might check that each item is a
                     * real integer ... real integer ... ----- Look for
                     * single value ----- Look for single value
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Extracted index item =
                     * [".$v_result_list[$p_options_list[$i]][$j]['start'].",".$v_result_list[$p_options_list[$i]][$j]['end']."]");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Extracted index item =
                     * [".$v_result_list[$p_options_list[$i]][$j]['start'].",".$v_result_list[$p_options_list[$i]][$j]['end']."]");
                     * ----- Look for list sort ----- Look for list sort
                     */
                     {
                        v_item_list = Strings.explode("-", v_work_list.getValue(j));
                        v_size_item_list = Array.sizeof(v_item_list);

                        if (equal(v_size_item_list, 1))/*
                         * ----- Set the option value ----- Set the option
                         * value
                         */
                         {
                            v_result_list.getArrayValue(p_options_list.getValue(i)).getArrayValue(j).putValue("start", v_item_list.getValue(0));
                            v_result_list.getArrayValue(p_options_list.getValue(i)).getArrayValue(j).putValue("end", v_item_list.getValue(0));
                        } else if (equal(v_size_item_list, 2))/*
                         * ----- Set the option value ----- Set the
                         * option value
                         */
                         {
                            v_result_list.getArrayValue(p_options_list.getValue(i)).getArrayValue(j).putValue("start", v_item_list.getValue(0));
                            v_result_list.getArrayValue(p_options_list.getValue(i)).getArrayValue(j).putValue("end", v_item_list.getValue(1));
                        } else/*
                         * ----- Error log ----- Error log
                         */

                        /*
                         * ----- Return ----- Return
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(),
                         * PclZip::errorInfo());
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(),
                         * PclZip::errorInfo());
                         */
                         {
                            gVars.getSharedPclZip().privErrorLog(
                                gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                                "Too many values in index range for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                            return gVars.getSharedPclZip().errorCode();
                        }

                        if (intval(v_result_list.getArrayValue(p_options_list.getValue(i)).getArrayValue(j).getValue("start")) < v_sort_value)/*
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The list should be sorted ...");
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The list should be sorted ...");
                         */

                        /*
                         * ----- TBC : An automatic sort should be writen
                         * ... ----- TBC : An automatic sort should be
                         * writen ... ----- Error log ----- Error log
                         */

                        /*
                         * ----- Return ----- Return
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(),
                         * PclZip::errorInfo());
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(),
                         * PclZip::errorInfo());
                         */
                         {
                            v_sort_flag = true;
                            gVars.getSharedPclZip().privErrorLog(
                                gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                                "Invalid order of index range for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                            return gVars.getSharedPclZip().errorCode();
                        }

                        v_sort_value = intval(v_result_list.getArrayValue(p_options_list.getValue(i)).getArrayValue(j).getValue("start"));
                    }

                    if (v_sort_flag) {
                    }

                    i++;

                    break;
                }

                case 12: {
                }

                case 13: {
                }

                case 14: {
                }

                case 15: {
                }

                case 16: {
                }

                case 17:/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 */
                 {
                    v_result_list.putValue(p_options_list.getValue(i), true);

                    break;
                }

                case 18:/*
                 * ----- Check the number of parameters ----- Check the
                 * number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2,
                 * "".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_result_list[$p_options_list[$i]]."'");
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    v_result_list.putValue(p_options_list.getValue(i), p_options_list.getValue(i + 1));
                    i++;

                    break;
                }

                case 19: {
                }

                case 20: {
                }

                case 21: {
                }

                case 22:/*
                 * for futur use case PCLZIP_CB_PRE_DELETE : case
                 * PCLZIP_CB_POST_DELETE : case PCLZIP_CB_PRE_LIST :
                 * case PCLZIP_CB_POST_LIST : ----- Check the number of
                 * parameters ----- Check the number of parameters
                 *
                 * ----- Get the value ----- Get the value
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "call-back
                 * ".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_function_name."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "call-back
                 * ".PclZipUtilOptionText($p_options_list[$i])." =
                 * '".$v_function_name."'"); ----- Check that the value
                 * is a valid existing function ----- Check that the
                 * value is a valid existing function
                 *
                 * ----- Set the attribute ----- Set the attribute
                 */
                 {
                    if ((i + 1) >= p_size)/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_MISSING_OPTION_VALUE(),
                            "Missing parameter value for option \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    v_function_name = strval(p_options_list.getValue(i + 1));

                    if (!VarHandling.is_callable(new Callback(v_function_name, this)))/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                                gConsts.getPCLZIP_ERR_INVALID_OPTION_VALUE(),
                                "Function \'" + v_function_name + "()\' is not an existing function for option \'" +
                                getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(p_options_list.getValue(i)) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    v_result_list.putValue(p_options_list.getValue(i), v_function_name);
                    i++;

                    break;
                }

                default:/*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Unknown parameter \'" + strval(p_options_list.getValue(i)) + "\'");

                    return gVars.getSharedPclZip().errorCode();
                }
                }
            }

            i++;
        }

        if (!empty(v_requested_options)) {
            for (key = Array.reset(v_requested_options); booleanval(key = Array.key(v_requested_options)); key = Array.next(v_requested_options))/*
             * ----- Look for mandatory option ----- Look for mandatory option
             */
             {
                if (equal(v_requested_options.getValue(key), "mandatory"))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 4, "Detect a mandatory option :
                 * ".PclZipUtilOptionText($key)."(".$key.")");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 4, "Detect a mandatory option :
                 * ".PclZipUtilOptionText($key)."(".$key.")"); ----- Look if
                 * present ----- Look if present
                 */
                 {
                    if (!isset(v_result_list.getValue(key)))/*
                     * ----- Error log ----- Error log
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_PARAMETER(),
                            "Missing mandatory parameter " + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(key) + "(" + strval(key) + ")");

                        return gVars.getSharedPclZip().errorCode();
                    }
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
     * Function : privFileDescrParseAtt() Function : privFileDescrParseAtt()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values : 1 on success. 1 on success. 0 on failure. 0 on failure.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privFileDescrParseAtt(Array<Object> p_file_list, Array<Object> p_filedescr, Array<Object> v_options, Array<Object> v_requested_options) {
        int v_result = 0;
        Object v_key = null;
        Object v_value = null;
        Object key;
        v_result = 1;

        for (Map.Entry javaEntry116 : p_file_list.entrySet())/*
         * ----- Check if the option is supported ----- Check if the option is
         * supported
         *
         * ----- Look for attribute ----- Look for attribute
         */

        /*
         * ----- Look for mandatory options ----- Look for mandatory options
         *
         * end foreach end foreach
         */
         {
            v_key = javaEntry116.getKey();
            v_value = javaEntry116.getValue();

            if (!isset(v_requested_options.getValue(v_key)))/*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid file attribute \'" + strval(v_key) + "\' for this file");

                return gVars.getSharedPclZip().errorCode();
            }

            {
                int javaSwitchSelector11 = 0;

                if (equal(v_key, gConsts.getPCLZIP_ATT_FILE_NAME())) {
                    javaSwitchSelector11 = 1;
                }

                if (equal(v_key, gConsts.getPCLZIP_ATT_FILE_NEW_SHORT_NAME())) {
                    javaSwitchSelector11 = 2;
                }

                if (equal(v_key, gConsts.getPCLZIP_ATT_FILE_NEW_FULL_NAME())) {
                    javaSwitchSelector11 = 3;
                }

                switch (javaSwitchSelector11) {
                case 1:/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "".PclZipUtilOptionText($v_key)." =
                 * '".$v_value."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "".PclZipUtilOptionText($v_key)." =
                 * '".$v_value."'");
                 */
                 {
                    if (!is_string(v_value))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(),
                            "Invalid type " + gettype(v_value) + ". String expected for attribute \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(v_key) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    p_filedescr.putValue("filename", getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathReduction(strval(v_value)));

                    if (equal(p_filedescr.getValue("filename"), ""))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(),
                            "Invalid empty filename for attribute \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(v_key) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    break;
                }

                case 2:/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "".PclZipUtilOptionText($v_key)." =
                 * '".$v_value."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "".PclZipUtilOptionText($v_key)." =
                 * '".$v_value."'");
                 */
                 {
                    if (!is_string(v_value))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(),
                            "Invalid type " + gettype(v_value) + ". String expected for attribute \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(v_key) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    p_filedescr.putValue("new_short_name", getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathReduction(strval(v_value)));

                    if (equal(p_filedescr.getValue("new_short_name"), ""))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(),
                            "Invalid empty short filename for attribute \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(v_key) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    break;
                }

                case 3:/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "".PclZipUtilOptionText($v_key)." =
                 * '".$v_value."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "".PclZipUtilOptionText($v_key)." =
                 * '".$v_value."'");
                 */
                 {
                    if (!is_string(v_value))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(),
                            "Invalid type " + gettype(v_value) + ". String expected for attribute \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(v_key) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    p_filedescr.putValue("new_full_name", getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathReduction(strval(v_value)));

                    if (equal(p_filedescr.getValue("new_full_name"), ""))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(
                            gConsts.getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(),
                            "Invalid empty full filename for attribute \'" + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(v_key) + "\'");

                        return gVars.getSharedPclZip().errorCode();
                    }

                    break;
                }

                default:/*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Unknown parameter \'" + strval(v_key) + "\'");

                    return gVars.getSharedPclZip().errorCode();
                }
                }
            }

            if (!empty(v_requested_options)) {
                for (key = Array.reset(v_requested_options); booleanval(key = Array.key(v_requested_options)); key = Array.next(v_requested_options))/*
                 * ----- Look for mandatory option ----- Look for mandatory
                 * option
                 */
                 {
                    if (equal(v_requested_options.getValue(key), "mandatory"))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Detect a mandatory option :
                     * ".PclZipUtilOptionText($key)."(".$key.")");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Detect a mandatory option :
                     * ".PclZipUtilOptionText($key)."(".$key.")"); ----- Look if
                     * present ----- Look if present
                     */
                     {
                        if (!isset(p_file_list.getValue(key)))/*
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                         */
                         {
                            gVars.getSharedPclZip().privErrorLog(
                                gConsts.getPCLZIP_ERR_INVALID_PARAMETER(),
                                "Missing mandatory parameter " + getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilOptionText(key) + "(" + strval(key) + ")");

                            return gVars.getSharedPclZip().errorCode();
                        }
                    }
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
     * Function : privFileDescrExpand() Function : privFileDescrExpand()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values : 1 on success. 1 on success. 0 on failure. 0 on failure.
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privFileDescrExpand(Array<Object> p_filedescr_list, Array<Object> p_options) {
        int v_result;
        Array<Object> v_result_list = new Array<Object>();
        Array<Object> v_descr = new Array<Object>();
        int i = 0;
        Array<Object> v_dirlist_descr = new Array<Object>();
        int v_dirlist_nb = 0;
        int v_folder_handler = 0;
        String v_item_handler = null;
        v_result = 1;
        v_result_list = new Array<Object>();

        for (i = 0; i < Array.sizeof(p_filedescr_list); i++)/*
         * ----- Get filedescr ----- Get filedescr
         */

        /*
         * ----- Reduce the filename ----- Reduce the filename
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Filedescr before reduction :'".$v_descr['filename']."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Filedescr before reduction :'".$v_descr['filename']."'");
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Filedescr after reduction :'".$v_descr['filename']."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Filedescr after reduction :'".$v_descr['filename']."'"); ----- Get
         * type of descr ----- Get type of descr
         */

        /*
         * ----- Calculate the stored filename ----- Calculate the stored
         * filename
         */

        /*
         * ----- Add the descriptor in result list ----- Add the descriptor in
         * result list
         */

        /*
         * ----- Look for folder ----- Look for folder
         */
         {
            v_descr = p_filedescr_list.getArrayValue(i);
            v_descr.putValue("filename", getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilTranslateWinPath(strval(v_descr.getValue("filename")), true));
            v_descr.putValue("filename", getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathReduction(strval(v_descr.getValue("filename"))));

            if (!FileSystemOrSocket.file_exists(gVars.webEnv, strval(v_descr.getValue("filename"))))/*
             * ----- Error log ----- Error log
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File '".$v_descr['filename']."' does not exists");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File '".$v_descr['filename']."' does not exists");
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_MISSING_FILE(), "File \'" + strval(v_descr.getValue("filename")) + "\' does not exists");

                return gVars.getSharedPclZip().errorCode();
            }

            if (FileSystemOrSocket.is_file(gVars.webEnv, strval(v_descr.getValue("filename"))))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "This is a file");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "This is a file");
             */
             {
                v_descr.putValue("type", "file");
            } else if (FileSystemOrSocket.is_dir(gVars.webEnv, strval(v_descr.getValue("filename"))))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "This is a folder");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "This is a folder");
             */
             {
                v_descr.putValue("type", "folder");
            } else if (JFileSystemOrSocket.is_link(gVars.webEnv, strval(v_descr.getValue("filename"))))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Unsupported file type : link");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Unsupported file type : link"); skip skip
             */
             {
                continue;
            } else/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Unsupported file type : unknown type");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Unsupported file type : unknown type");
             * skip skip
             */
             {
                continue;
            }

            this.privCalculateStoredFilename(v_descr, p_options);
            v_result_list.putValue(Array.sizeof(v_result_list), v_descr);

            if (equal(v_descr.getValue("type"), "folder"))/*
             * ----- List of items in folder ----- List of items in folder
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Unable to open dir '".$v_descr['filename']."' in read mode.
             * Skipped."); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 2, "Unable to open dir '".$v_descr['filename']."' in
             * read mode. Skipped."); TBC : unable to open folder in read mode
             * TBC : unable to open folder in read mode
             */

            /*
             * ----- Expand each element of the list ----- Expand each element
             * of the list
             *
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "merged result list is size '".sizeof($v_result_list)."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "merged result list is size '".sizeof($v_result_list)."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Nothing in this folder to expand.");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Nothing in this folder to expand.");
             */

            /*
             * ----- Free local array ----- Free local array
             */
             {
                v_dirlist_descr = new Array<Object>();
                v_dirlist_nb = 0;

                if (booleanval(v_folder_handler = Directories.opendir(gVars.webEnv, strval(v_descr.getValue("filename"))))) {
                    while (!strictEqual(v_item_handler = Directories.readdir(gVars.webEnv, v_folder_handler), STRING_FALSE))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Looking for '".$v_item_handler."' in the
                     * directory");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Looking for '".$v_item_handler."' in the
                     * directory"); ----- Skip '.' and '..' ----- Skip '.' and
                     * '..'
                     *
                     * ----- Compose the full filename ----- Compose the full
                     * filename
                     */

                    /*
                     * ----- Look for different stored filename ----- Look for
                     * different stored filename Because the name of the folder
                     * was changed, the name of the Because the name of the
                     * folder was changed, the name of the files/sub-folders
                     * also change files/sub-folders also change
                     */
                     {
                        if (equal(v_item_handler, ".") || equal(v_item_handler, "..")) {
                            continue;
                        }

                        v_dirlist_descr.getArrayValue(v_dirlist_nb).putValue("filename", strval(v_descr.getValue("filename")) + "/" + v_item_handler);

                        if (!equal(v_descr.getValue("stored_filename"), v_descr.getValue("filename"))) {
                            v_dirlist_descr.getArrayValue(v_dirlist_nb).putValue("new_full_name", strval(v_descr.getValue("stored_filename")) + "/" + v_item_handler);
                        }

                        v_dirlist_nb++;
                    }
                } else {
                }

                if (!equal(v_dirlist_nb, 0))/*
                 * ----- Expand ----- Expand
                 *
                 * ----- Concat the resulting list ----- Concat the resulting
                 * list --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "Merging result list (size
                 * '".sizeof($v_result_list)."') with dirlist (size
                 * '".sizeof($v_dirlist_descr)."')");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Merging result list (size '".sizeof($v_result_list)."')
                 * with dirlist (size '".sizeof($v_dirlist_descr)."')");
                 */
                 {
                    if (!equal(v_result = this.privFileDescrExpand(v_dirlist_descr, p_options), 1))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     */
                     {
                        return v_result;
                    }

                    v_result_list = Array.array_merge(v_result_list, v_dirlist_descr);
                } else {
                }

                v_dirlist_descr = null;
            }
        }

        p_filedescr_list = v_result_list;

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privCreate() Function : privCreate() Description : Description :
     * Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privCreate(Array<Object> p_filedescr_list, Array<Object> p_result_list, Array<Object> p_options) {
        int v_result = 0;
        Array<Object> v_list_detail = new Array<Object>();
        v_result = 1;
        v_list_detail = new Array<Object>();
        this.privDisableMagicQuotes();

        if (!equal(v_result = this.privOpenFd("wb"), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        v_result = this.privAddList(p_filedescr_list, p_result_list, p_options);
        this.privCloseFd();
        this.privSwapBackMagicQuotes();

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privAdd() Function : privAdd() Description : Description :
     * Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privAdd(Array<Object> p_filedescr_list, Array<Object> p_result_list, Array<Object> p_options) {
        int v_result = 0;
        Array<Object> v_list_detail = new Array<Object>();
        Array<Object> v_central_dir = new Array<Object>();
        String v_zip_temp_name = null;
        int v_zip_temp_fd;
        int v_size;
        int v_read_size;
        String v_buffer = null;
        int v_swap;
        Array<Object> v_header_list = new Array<Object>();
        int v_offset;
        int i = 0;
        int v_count = 0;
        String v_comment = null;
        v_result = 1;
        v_list_detail = new Array<Object>();

        if (!FileSystemOrSocket.is_file(gVars.webEnv, this.zipname) || equal(FileSystemOrSocket.filesize(gVars.webEnv, this.zipname), 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive does not exist, or is empty, create it.");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive does not exist, or is empty, create it."); ----- Do a create
         * ----- Do a create
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            v_result = this.privCreate(p_filedescr_list, p_result_list, p_options);

            return v_result;
        }

        this.privDisableMagicQuotes();

        if (!equal(v_result = this.privOpenFd("rb"), 1))/*
         * ----- Magic quotes trick ----- Magic quotes trick
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        v_central_dir = new Array<Object>();

        if (!equal(v_result = this.privReadEndCentralDir(v_central_dir), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);
        v_zip_temp_name = gConsts.getPCLZIP_TEMPORARY_DIR() + Misc.uniqid("pclzip-") + ".tmp";

        if (equal(v_zip_temp_fd = FileSystemOrSocket.fopen(gVars.webEnv, v_zip_temp_name, "wb"), 0))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            this.privCloseFd();
            this.privSwapBackMagicQuotes();
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open temporary file \'" + v_zip_temp_name + "\' in binary write mode");

            return gVars.getSharedPclZip().errorCode();
        }

        v_size = intval(v_central_dir.getValue("offset"));

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, v_zip_temp_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        v_swap = this.zip_fd;
        this.zip_fd = v_zip_temp_fd;
        v_zip_temp_fd = v_swap;
        v_header_list = new Array<Object>();

        if (!equal(v_result = this.privAddFileList(p_filedescr_list, v_header_list, p_options), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            FileSystemOrSocket.fclose(gVars.webEnv, v_zip_temp_fd);
            this.privCloseFd();
            JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        v_offset = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd);

        /*, null*/
        v_size = intval(v_central_dir.getValue("size"));

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, v_zip_temp_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, this.zip_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        i = 0;
        v_count = 0;

        for (; i < Array.sizeof(v_header_list); i++)/*
         * ----- Create the file header ----- Create the file header
         *
         * ----- Transform the header to a 'usable' info ----- Transform the
         * header to a 'usable' info
         */
         {
            if (equal(v_header_list.getArrayValue(i).getValue("status"), "ok")) {
                if (!equal(v_result = this.privWriteCentralFileHeader(v_header_list.getArrayValue(i)), 1))/*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result);
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result);
                 */
                 {
                    FileSystemOrSocket.fclose(gVars.webEnv, v_zip_temp_fd);
                    this.privCloseFd();
                    JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);
                    this.privSwapBackMagicQuotes();

                    return v_result;
                }

                v_count++;
            }

            this.privConvertHeader2FileInfo(v_header_list.getArrayValue(i), p_result_list.getArrayValue(i));
        }

        v_comment = strval(v_central_dir.getValue("comment"));

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_COMMENT()))) {
            v_comment = strval(p_options.getValue(gConsts.getPCLZIP_OPT_COMMENT()));
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_ADD_COMMENT()))) {
            v_comment = v_comment + strval(p_options.getValue(gConsts.getPCLZIP_OPT_ADD_COMMENT()));
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_PREPEND_COMMENT()))) {
            v_comment = strval(p_options.getValue(gConsts.getPCLZIP_OPT_PREPEND_COMMENT())) + v_comment;
        }

        v_size = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd) - v_offset;

        if (!equal(v_result = this.privWriteCentralHeader(v_count + intval(v_central_dir.getValue("entries")), v_size, v_offset, v_comment), 1))/*
         * ----- Reset the file list ----- Reset the file list
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            v_header_list = null;
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        v_swap = this.zip_fd;
        this.zip_fd = v_zip_temp_fd;
        v_zip_temp_fd = v_swap;
        this.privCloseFd();
        FileSystemOrSocket.fclose(gVars.webEnv, v_zip_temp_fd);
        this.privSwapBackMagicQuotes();
        JFileSystemOrSocket.unlink(gVars.webEnv, this.zipname);
        getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilRename(v_zip_temp_name, this.zipname);

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privOpenFd() Function : privOpenFd() Description : Description :
     * Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privOpenFd(String p_mode) {
        int v_result = 0;
        v_result = 1;

        if (!equal(this.zip_fd, 0))/*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Zip file \'" + this.zipname + "\' already open");

            return gVars.getSharedPclZip().errorCode();
        }

        if (equal(this.zip_fd = FileSystemOrSocket.fopen(gVars.webEnv, this.zipname, p_mode), 0))/*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open archive \'" + this.zipname + "\' in " + p_mode + " mode");

            return gVars.getSharedPclZip().errorCode();
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privCloseFd() Function : privCloseFd() Description :
     * Description : Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privCloseFd() {
        int v_result = 0;
        v_result = 1;

        if (!equal(this.zip_fd, 0)) {
            FileSystemOrSocket.fclose(gVars.webEnv, this.zip_fd);
        }

        this.zip_fd = 0;

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privAddList() Function : privAddList() Description :
     * Description : $p_add_dir and $p_remove_dir will give the ability to
     * memorize a path which is $p_add_dir and $p_remove_dir will give the
     * ability to memorize a path which is different from the real path of the
     * file. This is usefull if you want to have PclTar different from the real
     * path of the file. This is usefull if you want to have PclTar running in
     * any directory, and memorize relative path from an other directory.
     * running in any directory, and memorize relative path from an other
     * directory. Parameters : Parameters : $p_list : An array containing the
     * file or directory names to add in the tar $p_list : An array containing
     * the file or directory names to add in the tar $p_result_list : list of
     * added files with their properties (specially the status field)
     * $p_result_list : list of added files with their properties (specially the
     * status field) $p_add_dir : Path to add in the filename path archived
     * $p_add_dir : Path to add in the filename path archived $p_remove_dir :
     * Path to remove in the filename path archived $p_remove_dir : Path to
     * remove in the filename path archived Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * function privAddList($p_list, &$p_result_list, $p_add_dir, $p_remove_dir,
     * $p_remove_all_dir, &$p_options) function privAddList($p_list,
     * &$p_result_list, $p_add_dir, $p_remove_dir, $p_remove_all_dir,
     * &$p_options)
     */
    public int privAddList(Array<Object> p_filedescr_list, Array<Object> p_result_list, Array<Object> p_options) {
        int v_result = 0;
        Array<Object> v_header_list = new Array<Object>();
        int v_offset;
        int i = 0;
        int v_count = 0;
        String v_comment = null;
        int v_size = 0;
        v_result = 1;
        v_header_list = new Array<Object>();

        if (!equal(v_result = this.privAddFileList(p_filedescr_list, v_header_list, p_options), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        v_offset = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd);
        /*, null*/
        {
            i = 0;
            v_count = 0;

            for (; i < Array.sizeof(v_header_list); i++)/*
             * ----- Create the file header ----- Create the file header
             *
             * ----- Transform the header to a 'usable' info ----- Transform the
             * header to a 'usable' info
             */
             {
                if (equal(v_header_list.getArrayValue(i).getValue("status"), "ok")) {
                    if (!equal(v_result = this.privWriteCentralFileHeader(v_header_list.getArrayValue(i)), 1))/*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     */
                     {
                        return v_result;
                    }

                    v_count++;
                }

                this.privConvertHeader2FileInfo(v_header_list.getArrayValue(i), p_result_list.getArrayValue(i));
            }
        }

        v_comment = "";

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_COMMENT()))) {
            v_comment = strval(p_options.getValue(gConsts.getPCLZIP_OPT_COMMENT()));
        }

        v_size = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd) - v_offset;

        if (!equal(v_result = this.privWriteCentralHeader(v_count, v_size, v_offset, v_comment), 1))/*
         * ----- Reset the file list ----- Reset the file list
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            v_header_list = null;

            return v_result;
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privAddFileList() Function : privAddFileList() Description :
     * Description : Parameters : Parameters : $p_filedescr_list : An array
     * containing the file description $p_filedescr_list : An array containing
     * the file description or directory names to add in the zip or directory
     * names to add in the zip $p_result_list : list of added files with their
     * properties (specially the status field) $p_result_list : list of added
     * files with their properties (specially the status field) Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privAddFileList(Array<Object> p_filedescr_list, Array<Object> p_result_list, Array<Object> p_options) {
        int v_result = 0;
        Array<Object> v_header = new Array<Object>();
        int v_nb = 0;
        int j = 0;
        v_result = 1;
        v_header = new Array<Object>();
        v_nb = Array.sizeof(p_result_list);

        for (j = 0; (j < Array.sizeof(p_filedescr_list)) && equal(v_result, 1); j++)/*
         * ----- Format the filename ----- Format the filename
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Looking for file '".$p_filedescr_list[$j]['filename']."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Looking for file '".$p_filedescr_list[$j]['filename']."'"); -----
         * Skip empty file names ----- Skip empty file names ??
         *
         *
         * ----- Check the filename ----- Check the filename
         */

        /*
         * ----- Look if it is a file or a dir with no all path remove option
         * ----- Look if it is a file or a dir with no all path remove option
         */
         {
            p_filedescr_list.getArrayValue(j).putValue(
                "filename",
                getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilTranslateWinPath(strval(p_filedescr_list.getArrayValue(j).getValue("filename")), false));

            if (equal(p_filedescr_list.getArrayValue(j).getValue("filename"), ""))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Skip empty filename");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Skip empty filename");
             */
             {
                continue;
            }

            if (!FileSystemOrSocket.file_exists(gVars.webEnv, strval(p_filedescr_list.getArrayValue(j).getValue("filename"))))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File '".$p_filedescr_list[$j]['filename']."' does not exists");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File '".$p_filedescr_list[$j]['filename']."' does not exists");
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_MISSING_FILE(), "File \'" + strval(p_filedescr_list.getArrayValue(j).getValue("filename")) + "\' does not exists");

                return gVars.getSharedPclZip().errorCode();
            }

            if (FileSystemOrSocket.is_file(gVars.webEnv, strval(p_filedescr_list.getArrayValue(j).getValue("filename"))) ||
                    (FileSystemOrSocket.is_dir(gVars.webEnv, strval(p_filedescr_list.getArrayValue(j).getValue("filename"))) &&
                    (!isset(p_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH())) || !booleanval(p_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH())))))/*
             * ----- Add the file ----- Add the file
             */

            /*
             * ----- Store the file infos ----- Store the file infos
             */
             {
                v_result = this.privAddFile(p_filedescr_list.getArrayValue(j), v_header, p_options);

                if (!equal(v_result, 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    return v_result;
                }

                p_result_list.putValue(v_nb++, v_header);
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privAddFile() Function : privAddFile() Description :
     * Description : Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privAddFile(Array<Object> p_filedescr, Array<Object> p_header, Array<Object> p_options) {
        int v_result = 0;
        String p_filename = null;
        Object v_stored_filename = null;
        Array<Object> v_local_header = new Array<Object>();
        int v_file = 0;
        String v_content_compressed = null;
        String v_content = null;
        v_result = 1;
        p_filename = strval(p_filedescr.getValue("filename"));

        if (equal(p_filename, ""))/*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_PARAMETER(), "Invalid file list parameter (invalid or empty list)");

            return gVars.getSharedPclZip().errorCode();
        }

        if (isset(p_filedescr.getValue("stored_filename"))) {
            v_stored_filename = p_filedescr.getValue("stored_filename");
        } else/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * 'Stored filename is NOT the same "'.$v_stored_filename.'"');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * 'Stored filename is NOT the same "'.$v_stored_filename.'"');
         */
         {
            v_stored_filename = p_filedescr.getValue("stored_filename");
        }

        FileSystemOrSocket.clearstatcache(gVars.webEnv);
        p_header.putValue("version", 20);
        p_header.putValue("version_extracted", 10);
        p_header.putValue("flag", 0);
        p_header.putValue("compression", 0);
        p_header.putValue("mtime", FileSystemOrSocket.filemtime(gVars.webEnv, p_filename));
        p_header.putValue("crc", 0);
        p_header.putValue("compressed_size", 0);
        p_header.putValue("size", FileSystemOrSocket.filesize(gVars.webEnv, p_filename));
        p_header.putValue("filename_len", Strings.strlen(p_filename));
        p_header.putValue("extra_len", 0);
        p_header.putValue("comment_len", 0);
        p_header.putValue("disk", 0);
        p_header.putValue("internal", 0);
        p_header.putValue("external", FileSystemOrSocket.is_file(gVars.webEnv, p_filename)
            ? 0
            : 16);
        p_header.putValue("offset", 0);
        p_header.putValue("filename", p_filename);
        p_header.putValue("stored_filename", v_stored_filename);
        p_header.putValue("extra", "");
        p_header.putValue("comment", "");
        p_header.putValue("status", "ok");
        p_header.putValue("index", -1);

        if (isset(p_options.getValue(gConsts.getPCLZIP_CB_PRE_ADD())))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "A
         * pre-callback '".$p_options[PCLZIP_CB_PRE_ADD]."()') is defined for
         * the extraction"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 2, "A pre-callback '".$p_options[PCLZIP_CB_PRE_ADD]."()')
         * is defined for the extraction"); ----- Generate a local information
         * ----- Generate a local information
         */

        /*
         * ----- Call the callback ----- Call the callback Here I do not use
         * call_user_func() because I need to send a reference to the Here I do
         * not use call_user_func() because I need to send a reference to the
         * header. header.
         */

        /*
         * ----- Update the informations ----- Update the informations Only some
         * fields can be modified Only some fields can be modified
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "New
         * stored filename is '".$p_header['stored_filename']."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "New
         * stored filename is '".$p_header['stored_filename']."'");
         */
         {
            v_local_header = new Array<Object>();
            this.privConvertHeader2FileInfo(p_header, v_local_header);

            // Modified by Numiton
            LOG.warn("Unimplemented callback through eval");

            if (equal(v_result, 0))/*
             * ----- Change the file status ----- Change the file status
             */
             {
                p_header.putValue("status", "skipped");
                v_result = 1;
            }

            if (!equal(p_header.getValue("stored_filename"), v_local_header.getValue("stored_filename"))) {
                p_header.putValue("stored_filename", getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathReduction(strval(v_local_header.getValue("stored_filename"))));
            }
        }

        if (equal(p_header.getValue("stored_filename"), "")) {
            p_header.putValue("status", "filtered");
        }

        if (Strings.strlen(strval(p_header.getValue("stored_filename"))) > 255) {
            p_header.putValue("status", "filename_too_long");
        }

        if (equal(p_header.getValue("status"), "ok"))/*
         * ----- Look for a file ----- Look for a file
         */
         {
            if (FileSystemOrSocket.is_file(gVars.webEnv, p_filename))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "'".$p_filename."' is a file");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "'".$p_filename."' is a file"); ----- Open the source file -----
             * Open the source file
             */

            /*
             * ----- Look for encryption ----- Look for encryption
             *
             * if ((isset($p_options[PCLZIP_OPT_CRYPT])) &&
             * ($p_options[PCLZIP_OPT_CRYPT] != "")) {
             * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File need to be crypted ....");
             *  // Should be a random header $v_header = 'xxxxxxxxxxxx';
             * $v_content_compressed =
             * PclZipUtilZipEncrypt($v_content_compressed,
             * $p_header['compressed_size'], $v_header, $p_header['crc'],
             * "test");
             *
             * $p_header['compressed_size'] += 12; $p_header['flag'] = 1;
             *  // ----- Add the header to the data $v_content_compressed =
             * $v_header.$v_content_compressed;
             * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Size after header : ".strlen($v_content_compressed).""); } -----
             * Call the header generation ----- Call the header generation
             *
             * ----- Write the compressed (or not) content ----- Write the
             * compressed (or not) content
             */

            /*
             * ----- Close the file ----- Close the file
             */
             {
                if (equal(v_file = FileSystemOrSocket.fopen(gVars.webEnv, p_filename, "rb"), 0))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open file \'" + p_filename + "\' in binary read mode");

                    return gVars.getSharedPclZip().errorCode();
                }

                if (booleanval(p_options.getValue(gConsts.getPCLZIP_OPT_NO_COMPRESSION())))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "File will not be compressed");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "File will not be compressed"); ----- Read the file
                 * content ----- Read the file content
                 */

                /*
                 * ----- Calculate the CRC ----- Calculate the CRC
                 */

                /*
                 * ----- Set header parameters ----- Set header parameters
                 */
                 {
                    v_content_compressed = FileSystemOrSocket.fread(gVars.webEnv, v_file, intval(p_header.getValue("size")));
                    p_header.putValue("crc", Strings.crc32(v_content_compressed));
                    p_header.putValue("compressed_size", p_header.getValue("size"));
                    p_header.putValue("compression", 0);
                } else/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "File will be compressed");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "File will be compressed"); ----- Read the file content
                 * ----- Read the file content
                 */

                /*
                 * ----- Calculate the CRC ----- Calculate the CRC
                 */

                /*
                 * ----- Compress the file ----- Compress the file
                 */

                /*
                 * ----- Set header parameters ----- Set header parameters
                 */
                 {
                    v_content = FileSystemOrSocket.fread(gVars.webEnv, v_file, intval(p_header.getValue("size")));
                    p_header.putValue("crc", Strings.crc32(v_content));
                    v_content_compressed = GZIP.gzdeflate(v_content);
                    p_header.putValue("compressed_size", Strings.strlen(v_content_compressed));
                    p_header.putValue("compression", 8);
                }

                if (!equal(v_result = this.privWriteFileHeader(p_header), 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    FileSystemOrSocket.fclose(gVars.webEnv, v_file);

                    return v_result;
                }

                FileSystemOrSocket.fwrite(gVars.webEnv, this.zip_fd, v_content_compressed, intval(p_header.getValue("compressed_size")));
                FileSystemOrSocket.fclose(gVars.webEnv, v_file);
            } else/*
             * ----- Look for a directory ----- Look for a directory
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "'".$p_filename."' is a folder");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "'".$p_filename."' is a folder"); ----- Look for directory last
             * '/' ----- Look for directory last '/'
             *
             * ----- Set the file properties ----- Set the file properties
             */

            /*
             * $p_header['external'] = 0x41FF0010; // Value for a folder : to be
             * checked $p_header['external'] = 0x41FF0010; // Value for a folder :
             * to be checked
             */

            /*
             * Value for a folder : to be checked Value for a folder : to be
             * checked ----- Call the header generation ----- Call the header
             * generation
             */
             {
                if (!equal(Strings.substr(strval(p_header.getValue("stored_filename")), -1), "/")) {
                    p_header.putValue("stored_filename", strval(p_header.getValue("stored_filename")) + "/");
                }

                p_header.putValue("size", 0);
                p_header.putValue("external", 16);

                if (!equal(v_result = this.privWriteFileHeader(p_header), 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    return v_result;
                }
            }
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_CB_POST_ADD())))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "A
         * post-callback '".$p_options[PCLZIP_CB_POST_ADD]."()') is defined for
         * the extraction"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 2, "A post-callback '".$p_options[PCLZIP_CB_POST_ADD]."()')
         * is defined for the extraction"); ----- Generate a local information
         * ----- Generate a local information
         */

        /*
         * ----- Call the callback ----- Call the callback Here I do not use
         * call_user_func() because I need to send a reference to the Here I do
         * not use call_user_func() because I need to send a reference to the
         * header. header.
         */

        /*
         * ----- Update the informations ----- Update the informations Nothing
         * can be modified Nothing can be modified
         */
         {
            v_local_header = new Array<Object>();
            this.privConvertHeader2FileInfo(p_header, v_local_header);

            // Modified by Numiton
            LOG.warn("Unimplemented callback through eval");

            if (equal(v_result, 0))/*
             * ----- Ignored ----- Ignored
             */
             {
                v_result = 1;
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privCalculateStoredFilename() Function :
     * privCalculateStoredFilename() Description : Description : Based on file
     * descriptor properties and global options, this method Based on file
     * descriptor properties and global options, this method calculate the
     * filename that will be stored in the archive. calculate the filename that
     * will be stored in the archive. Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privCalculateStoredFilename(Array<Object> p_filedescr, Array<Object> p_options) {
        int v_result = 0;
        String p_filename = null;
        String p_add_dir = null;
        String p_remove_dir = null;
        int p_remove_all_dir = 0;
        String v_stored_filename = null;
        Array<Object> v_path_info = new Array<Object>();
        String v_dir = null;
        int v_compare = 0;
        v_result = 1;
        p_filename = strval(p_filedescr.getValue("filename"));

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_ADD_PATH()))) {
            p_add_dir = strval(p_options.getValue(gConsts.getPCLZIP_OPT_ADD_PATH()));
        } else {
            p_add_dir = "";
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_PATH()))) {
            p_remove_dir = strval(p_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_PATH()));
        } else {
            p_remove_dir = "";
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH()))) {
            p_remove_all_dir = intval(p_options.getValue(gConsts.getPCLZIP_OPT_REMOVE_ALL_PATH()));
        } else {
            p_remove_all_dir = 0;
        }

        if (isset(p_filedescr.getValue("new_full_name"))) {
            v_stored_filename = strval(p_filedescr.getValue("new_full_name"));
        } else/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
         * "Changing full name of '".$p_filename."' for
         * '".$v_stored_filename."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
         * "Changing full name of '".$p_filename."' for
         * '".$v_stored_filename."'"); ----- Look for path and/or short name
         * change ----- Look for path and/or short name change ----- Look for
         * short name change ----- Look for short name change
         */

        /*
         * ----- Look for all path to remove ----- Look for all path to remove
         *
         * ----- Look for path to add ----- Look for path to add
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "Add
         * path '$p_add_dir' in file '$p_filename' = '$v_stored_filename'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "Add
         * path '$p_add_dir' in file '$p_filename' = '$v_stored_filename'");
         */
         {
            if (isset(p_filedescr.getValue("new_short_name"))) {
                v_path_info = FileSystemOrSocket.pathinfo(p_filename);
                v_dir = "";

                if (!equal(v_path_info.getValue("dirname"), "")) {
                    v_dir = strval(v_path_info.getValue("dirname")) + "/";
                }

                v_stored_filename = v_dir + strval(p_filedescr.getValue("new_short_name"));
            } else/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Changing short name of '".$p_filename."' for
             * '".$v_stored_filename."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Changing short name of '".$p_filename."' for
             * '".$v_stored_filename."'"); ----- Calculate the stored filename
             * ----- Calculate the stored filename
             */
             {
                v_stored_filename = p_filename;
            }

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Remove all path selected change '".$p_filename."' for
             * '".$v_stored_filename."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
             * "Remove all path selected change '".$p_filename."' for
             * '".$v_stored_filename."'"); ----- Look for partial path remove
             * ----- Look for partial path remove
             */
            if (booleanval(p_remove_all_dir)) {
                v_stored_filename = FileSystemOrSocket.basename(p_filename);
            } else if (!equal(p_remove_dir, "")) {
                if (!equal(Strings.substr(p_remove_dir, -1), "/")) {
                    p_remove_dir = p_remove_dir + "/";
                }

                if (equal(Strings.substr(p_filename, 0, 2), "./") || equal(Strings.substr(p_remove_dir, 0, 2), "./")) {
                    if (equal(Strings.substr(p_filename, 0, 2), "./") && !equal(Strings.substr(p_remove_dir, 0, 2), "./")) {
                        p_remove_dir = "./" + p_remove_dir;
                    }

                    if (!equal(Strings.substr(p_filename, 0, 2), "./") && equal(Strings.substr(p_remove_dir, 0, 2), "./")) {
                        p_remove_dir = Strings.substr(p_remove_dir, 2);
                    }
                }

                v_compare = getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathInclusion(p_remove_dir, v_stored_filename);

                if (v_compare > 0)/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 4, "Result is '$v_stored_filename'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 4, "Result is '$v_stored_filename'");
                 */
                 {
                    if (equal(v_compare, 2)) {
                        v_stored_filename = "";
                    } else/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Path to remove is the current folder");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Path to remove is the current folder");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Remove path '$p_remove_dir' in file
                     * '$v_stored_filename'");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 4, "Remove path '$p_remove_dir' in file
                     * '$v_stored_filename'");
                     */
                     {
                        v_stored_filename = Strings.substr(v_stored_filename, Strings.strlen(p_remove_dir));
                    }
                }
            }

            if (!equal(p_add_dir, "")) {
                if (equal(Strings.substr(p_add_dir, -1), "/")) {
                    v_stored_filename = p_add_dir + v_stored_filename;
                } else {
                    v_stored_filename = p_add_dir + "/" + v_stored_filename;
                }
            }
        }

        v_stored_filename = getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathReduction(v_stored_filename);
        p_filedescr.putValue("stored_filename", v_stored_filename);

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privWriteFileHeader() Function : privWriteFileHeader()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privWriteFileHeader(Array<Object> p_header) {
        int v_result = 0;
        Array<Object> v_date = new Array<Object>();
        float v_mtime = 0;
        int v_mdate = 0;
        String v_binary_data = null;
        v_result = 1;
        p_header.putValue("offset", FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd));

        /*, null*/
        v_date = DateTime.getdate(intval(p_header.getValue("mtime")));
        v_mtime = floatval((intval(v_date.getValue("hours")) << 11) + (intval(v_date.getValue("minutes")) << 5)) + (floatval(v_date.getValue("seconds")) / floatval(2));
        v_mdate = ((intval(v_date.getValue("year")) - 1980) << 9) + (intval(v_date.getValue("mon")) << 5) + intval(v_date.getValue("mday"));
        v_binary_data = QMisc.pack("VvvvvvVVVvv", 67324752, p_header.getValue("version_extracted"), p_header.getValue("flag"), p_header.getValue("compression"), v_mtime, v_mdate,
                p_header.getValue("crc"), p_header.getValue("compressed_size"), p_header.getValue("size"), Strings.strlen(strval(p_header.getValue("stored_filename"))), p_header.getValue("extra_len"));
        FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, v_binary_data, 30);

        if (!equal(Strings.strlen(strval(p_header.getValue("stored_filename"))), 0)) {
            FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, strval(p_header.getValue("stored_filename")), Strings.strlen(strval(p_header.getValue("stored_filename"))));
        }

        if (!equal(p_header.getValue("extra_len"), 0)) {
            FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, strval(p_header.getValue("extra")), intval(p_header.getValue("extra_len")));
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privWriteCentralFileHeader() Function :
     * privWriteCentralFileHeader() Description : Description : Parameters :
     * Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privWriteCentralFileHeader(Array<Object> p_header) {
        int v_result = 0;
        Array<Object> v_date = new Array<Object>();
        float v_mtime = 0;
        int v_mdate = 0;
        String v_binary_data = null;
        v_result = 1;
        v_date = DateTime.getdate(intval(p_header.getValue("mtime")));
        v_mtime = floatval((intval(v_date.getValue("hours")) << 11) + (intval(v_date.getValue("minutes")) << 5)) + (floatval(v_date.getValue("seconds")) / floatval(2));
        v_mdate = ((intval(v_date.getValue("year")) - 1980) << 9) + (intval(v_date.getValue("mon")) << 5) + intval(v_date.getValue("mday"));
        v_binary_data = QMisc.pack(
                "VvvvvvvVVVvvvvvVV",
                33639248,
                p_header.getValue("version"),
                p_header.getValue("version_extracted"),
                p_header.getValue("flag"),
                p_header.getValue("compression"),
                v_mtime,
                v_mdate,
                p_header.getValue("crc"),
                p_header.getValue("compressed_size"),
                p_header.getValue("size"),
                Strings.strlen(strval(p_header.getValue("stored_filename"))),
                p_header.getValue("extra_len"),
                p_header.getValue("comment_len"),
                p_header.getValue("disk"),
                p_header.getValue("internal"),
                p_header.getValue("external"),
                p_header.getValue("offset"));
        FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, v_binary_data, 46);

        if (!equal(Strings.strlen(strval(p_header.getValue("stored_filename"))), 0)) {
            FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, strval(p_header.getValue("stored_filename")), Strings.strlen(strval(p_header.getValue("stored_filename"))));
        }

        if (!equal(p_header.getValue("extra_len"), 0)) {
            FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, strval(p_header.getValue("extra")), intval(p_header.getValue("extra_len")));
        }

        if (!equal(p_header.getValue("comment_len"), 0)) {
            FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, strval(p_header.getValue("comment")), intval(p_header.getValue("comment_len")));
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privWriteCentralHeader() Function : privWriteCentralHeader()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privWriteCentralHeader(int p_nb_entries, int p_size, int p_offset, String p_comment) {
        int v_result = 0;
        String v_binary_data = null;
        v_result = 1;
        v_binary_data = QMisc.pack("VvvvvVVv", 101010256, 0, 0, p_nb_entries, p_nb_entries, p_size, p_offset, Strings.strlen(p_comment));
        FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, v_binary_data, 22);

        if (!equal(Strings.strlen(p_comment), 0)) {
            FileSystemOrSocket.fputs(gVars.webEnv, this.zip_fd, p_comment, Strings.strlen(p_comment));
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privList() Function : privList() Description : Description :
     * Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privList(Array<Object> p_list) {
        int v_result = 0;
        Array<Object> v_central_dir = new Array<Object>();
        Array<Object> v_header = new Array<Object>();
        int i = 0;
        v_result = 1;
        this.privDisableMagicQuotes();

        if (equal(this.zip_fd = FileSystemOrSocket.fopen(gVars.webEnv, this.zipname, "rb"), 0))/*
         * ----- Magic quotes trick ----- Magic quotes trick
         */

        /*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            this.privSwapBackMagicQuotes();
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open archive \'" + this.zipname + "\' in binary read mode");

            return gVars.getSharedPclZip().errorCode();
        }

        v_central_dir = new Array<Object>();

        if (!equal(v_result = this.privReadEndCentralDir(v_central_dir), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);

        if (booleanval(FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, intval(v_central_dir.getValue("offset")))))/*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            this.privSwapBackMagicQuotes();
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_ARCHIVE_ZIP(), "Invalid archive size");

            return gVars.getSharedPclZip().errorCode();
        }

        for (i = 0; i < intval(v_central_dir.getValue("entries")); i++)/*
         * ----- Read the file header ----- Read the file header
         */

        /*
         * ----- Get the only interesting attributes ----- Get the only
         * interesting attributes
         */
         {
            if (!equal(v_result = this.privReadCentralFileHeader(v_header), 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                this.privSwapBackMagicQuotes();

                return v_result;
            }

            v_header.putValue("index", i);
            this.privConvertHeader2FileInfo(v_header, p_list.getArrayValue(i));
            v_header = null;
        }

        this.privCloseFd();
        this.privSwapBackMagicQuotes();

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privConvertHeader2FileInfo() Function :
     * privConvertHeader2FileInfo() Description : Description : This function
     * takes the file informations from the central directory This function
     * takes the file informations from the central directory entries and
     * extract the interesting parameters that will be given back. entries and
     * extract the interesting parameters that will be given back. The resulting
     * file infos are set in the array $p_info The resulting file infos are set
     * in the array $p_info $p_info['filename'] : Filename with full path. Given
     * by user (add), $p_info['filename'] : Filename with full path. Given by
     * user (add), extracted in the filesystem (extract). extracted in the
     * filesystem (extract). $p_info['stored_filename'] : Stored filename in the
     * archive. $p_info['stored_filename'] : Stored filename in the archive.
     * $p_info['size'] = Size of the file. $p_info['size'] = Size of the file.
     * $p_info['compressed_size'] = Compressed size of the file.
     * $p_info['compressed_size'] = Compressed size of the file.
     * $p_info['mtime'] = Last modification date of the file. $p_info['mtime'] =
     * Last modification date of the file. $p_info['comment'] = Comment
     * associated with the file. $p_info['comment'] = Comment associated with
     * the file. $p_info['folder'] = true/false : indicates if the entry is a
     * folder or not. $p_info['folder'] = true/false : indicates if the entry is
     * a folder or not. $p_info['status'] = status of the action on the file.
     * $p_info['status'] = status of the action on the file. Parameters :
     * Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privConvertHeader2FileInfo(Array<Object> p_header, Array<Object> p_info) {
        int v_result = 0;
        v_result = 1;
        p_info.putValue("filename", p_header.getValue("filename"));
        p_info.putValue("stored_filename", p_header.getValue("stored_filename"));
        p_info.putValue("size", p_header.getValue("size"));
        p_info.putValue("compressed_size", p_header.getValue("compressed_size"));
        p_info.putValue("mtime", p_header.getValue("mtime"));
        p_info.putValue("comment", p_header.getValue("comment"));
        p_info.putValue("folder", equal(intval(p_header.getValue("external")) & 16, 16));
        p_info.putValue("index", p_header.getValue("index"));
        p_info.putValue("status", p_header.getValue("status"));

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privExtractByRule() Function : privExtractByRule() Description :
     * Description : Extract a file or directory depending of rules (by index,
     * by name, ...) Extract a file or directory depending of rules (by index,
     * by name, ...) Parameters : Parameters : $p_file_list : An array where
     * will be placed the properties of each $p_file_list : An array where will
     * be placed the properties of each extracted file extracted file $p_path :
     * Path to add while writing the extracted files $p_path : Path to add while
     * writing the extracted files $p_remove_path : Path to remove (from the
     * file memorized path) while writing the $p_remove_path : Path to remove
     * (from the file memorized path) while writing the extracted files. If the
     * path does not match the file path, extracted files. If the path does not
     * match the file path, the file is extracted with its memorized path. the
     * file is extracted with its memorized path. $p_remove_path does not apply
     * to 'list' mode. $p_remove_path does not apply to 'list' mode. $p_path and
     * $p_remove_path are commulative. $p_path and $p_remove_path are
     * commulative. Return Values : Return Values : 1 on success,0 or less on
     * error (see error code list) 1 on success,0 or less on error (see error
     * code list)
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privExtractByRule(Array<Object> p_file_list, String p_path, String p_remove_path, Object p_remove_all_path, Array<Object> p_options) {
        int v_result = 0;
        int p_remove_path_size = 0;
        Array<Object> v_central_dir = new Array<Object>();
        int v_pos_entry;
        int j_start;
        Array<Object> v_header = new Array<Object>();
        int i = 0;
        boolean v_extract = false;
        int j;
        int v_nb_extracted = 0;
        int v_result1;
        Ref<String> v_string = new Ref<String>();
        v_result = 1;
        this.privDisableMagicQuotes();

        if (equal(p_path, "") || (!equal(Strings.substr(p_path, 0, 1), "/") && !equal(Strings.substr(p_path, 0, 3), "../") && !equal(Strings.substr(p_path, 1, 2), ":/"))) {
            p_path = "./" + p_path;
        }

        if (!equal(p_path, "./") && !equal(p_path, "/"))/*
         * ----- Look for the path end '/' ----- Look for the path end '/'
         *
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Modified to [$p_path]");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Modified to [$p_path]");
         */
         {
            while (equal(Strings.substr(p_path, -1), "/"))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Destination path [$p_path] ends by '/'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Destination path [$p_path] ends by '/'");
             */
             {
                p_path = Strings.substr(p_path, 0, Strings.strlen(p_path) - 1);
            }
        }

        if (!equal(p_remove_path, "") && !equal(Strings.substr(p_remove_path, -1), "/")) {
            p_remove_path = p_remove_path + "/";
        }

        p_remove_path_size = Strings.strlen(p_remove_path);

        if (!equal(v_result = this.privOpenFd("rb"), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        v_central_dir = new Array<Object>();

        if (!equal(v_result = this.privReadEndCentralDir(v_central_dir), 1))/*
         * ----- Close the zip file ----- Close the zip file
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();
            this.privSwapBackMagicQuotes();

            return v_result;
        }

        v_pos_entry = intval(v_central_dir.getValue("offset"));
        j_start = 0;
        i = 0;
        v_nb_extracted = 0;

        for (; i < intval(v_central_dir.getValue("entries")); i++)/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Read next file header entry : '$i'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Read next file header entry : '$i'"); ----- Read next Central
         * dir entry ----- Read next Central dir entry >zip_fd)."'");
         * zip_fd)."'");
         */

        /*
         * >zip_fd)."'"); zip_fd)."'");
         *
         * >zip_fd)."'"); zip_fd)."'"); ----- Read the file header -----
         * Read the file header
         */

        /*
         * ----- Store the index ----- Store the index
         */

        /*
         * ----- Store the file position ----- Store the file position
         */

        /*
         * ----- Look for the specific extract rules ----- Look for the
         * specific extract rules
         */

        /*
         * ----- Look for extract by name rule ----- Look for extract by
         * name rule
         */

        /*
         * ----- Check compression method ----- Check compression method
         *
         * ----- Check encrypted files ----- Check encrypted files
         */

        /*
         * ----- Look for real extraction ----- Look for real extraction
         */

        /*
         * ----- Look for real extraction ----- Look for real extraction
         */
         {
            FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);

            if (booleanval(FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, v_pos_entry)))/*
             * ----- Close the zip file ----- Close the zip file
             */

            /*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                this.privCloseFd();
                this.privSwapBackMagicQuotes();
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_ARCHIVE_ZIP(), "Invalid archive size");

                return gVars.getSharedPclZip().errorCode();
            }

            v_header = new Array<Object>();

            if (!equal(v_result = this.privReadCentralFileHeader(v_header), 1))/*
             * ----- Close the zip file ----- Close the zip file
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                this.privCloseFd();
                this.privSwapBackMagicQuotes();

                return v_result;
            }

            v_header.putValue("index", i);
            v_pos_entry = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd);

            /*, null*/
            v_extract = false;

            /*
             * ----- Look for extract by ereg rule ----- Look for extract by
             * ereg rule
             */
            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_NAME())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_NAME()), 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "Extract with rule 'ByName'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "Extract with rule 'ByName'"); ----- Look if the filename
             * is in the list ----- Look if the filename is in the list
             */
             {
                for (j = 0; (j < Array.sizeof(p_options.getValue(gConsts.getPCLZIP_OPT_BY_NAME()))) && !v_extract; j++)/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Compare with file
                 * '".$p_options[PCLZIP_OPT_BY_NAME][$j]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Compare with file
                 * '".$p_options[PCLZIP_OPT_BY_NAME][$j]."'"); ----- Look
                 * for a directory ----- Look for a directory
                 */
                 {
                    if (equal(Strings.substr(strval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)), -1), "/"))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The searched item is a directory");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The searched item is a directory");
                     * ----- Look if the directory is in the filename path
                     * ----- Look if the directory is in the filename path
                     */
                     {
                        if ((Strings.strlen(strval(v_header.getValue("stored_filename"))) > Strings.strlen(strval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))) &&
                                equal(Strings.substr(strval(v_header.getValue("stored_filename")), 0, Strings.strlen(strval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))),
                                    p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))/*
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The directory is in the file
                         * path");
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The directory is in the file
                         * path");
                         */
                         {
                            v_extract = true;
                        }
                    } else if (equal(v_header.getValue("stored_filename"), p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))/*
                     * ----- Look for a filename ----- Look for a
                     * filename
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The file is the right one.");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The file is the right one.");
                     */
                     {
                        v_extract = true;
                    }
                }
            } else
            /*
             * ----- Look for extract by preg rule ----- Look for
             * extract by preg rule
             */
            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_EREG())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_EREG()), ""))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract by ereg
             * '".$p_options[PCLZIP_OPT_BY_EREG]."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract by ereg
             * '".$p_options[PCLZIP_OPT_BY_EREG]."'");
             */
             {
                if (booleanval(RegExPosix.ereg(strval(p_options.getValue(gConsts.getPCLZIP_OPT_BY_EREG())), strval(v_header.getValue("stored_filename")))))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 */
                 {
                    v_extract = true;
                }
            } else
            /*
             * ----- Look for extract by index rule ----- Look for
             * extract by index rule
             */
            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_PREG())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_PREG()), ""))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByEreg'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByEreg'");
             */
             {
                if (QRegExPerl.preg_match(strval(p_options.getValue(gConsts.getPCLZIP_OPT_BY_PREG())), strval(v_header.getValue("stored_filename"))))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 */
                 {
                    v_extract = true;
                }
            } else if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_INDEX())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_INDEX()), 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByIndex'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByIndex'");
             * ----- Look if the index is in the list ----- Look
             * if the index is in the list
             */
             {
                for (j = j_start; (j < Array.sizeof(p_options.getValue(gConsts.getPCLZIP_OPT_BY_INDEX()))) && !v_extract; j++)/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Look if index '$i' is in
                 * [".$p_options[PCLZIP_OPT_BY_INDEX][$j]['start'].",".$p_options[PCLZIP_OPT_BY_INDEX][$j]['end']."]");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Look if index '$i' is in
                 * [".$p_options[PCLZIP_OPT_BY_INDEX][$j]['start'].",".$p_options[PCLZIP_OPT_BY_INDEX][$j]['end']."]");
                 */
                 {
                    if ((i >= intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("start"))) &&
                            (i <= intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("end"))))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Found as part of an index
                     * range");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Found as part of an index
                     * range");
                     */
                     {
                        v_extract = true;
                    }

                    if (i >= intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("end")))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Do not look this index
                     * range for next loop");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Do not look this index
                     * range for next loop");
                     */
                     {
                        j_start = j + 1;
                    }

                    if (intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("start")) > i)/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Index range is greater than
                     * index, stop loop");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Index range is greater than
                     * index, stop loop");
                     */
                     {
                        break;
                    }
                }
            } else/*
             * ----- Look for no rule, which means extract all
             * the archive ----- Look for no rule, which means
             * extract all the archive
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with no rule (extract
             * all)");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with no rule (extract
             * all)");
             */
             {
                v_extract = true;
            }

            if (v_extract && !equal(v_header.getValue("compression"), 8) && !equal(v_header.getValue("compression"), 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "Unsupported compression method
             * (".$v_header['compression'].")");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "Unsupported compression method
             * (".$v_header['compression'].")");
             */

            /*
             * ----- Look for PCLZIP_OPT_STOP_ON_ERROR ----- Look for
             * PCLZIP_OPT_STOP_ON_ERROR
             */
             {
                v_header.putValue("status", "unsupported_compression");

                if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR())) && strictEqual(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR()), true))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                 * extraction will be stopped");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                 * extraction will be stopped");
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    this.privSwapBackMagicQuotes();
                    gVars.getSharedPclZip().privErrorLog(
                        gConsts.getPCLZIP_ERR_UNSUPPORTED_COMPRESSION(),
                        "Filename \'" + strval(v_header.getValue("stored_filename")) + "\' is " + "compressed by an unsupported compression " + "method (" + strval(v_header.getValue("compression")) +
                        ") ");

                    return gVars.getSharedPclZip().errorCode();
                }
            }

            if (v_extract && equal(intval(v_header.getValue("flag")) & 1, 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "Unsupported file encryption");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "Unsupported file encryption");
             */

            /*
             * ----- Look for PCLZIP_OPT_STOP_ON_ERROR ----- Look for
             * PCLZIP_OPT_STOP_ON_ERROR
             */
             {
                v_header.putValue("status", "unsupported_encryption");

                if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR())) && strictEqual(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR()), true))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                 * extraction will be stopped");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                 * extraction will be stopped");
                 */

                /*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    this.privSwapBackMagicQuotes();
                    gVars.getSharedPclZip()
                         .privErrorLog(gConsts.getPCLZIP_ERR_UNSUPPORTED_ENCRYPTION(), "Unsupported encryption for " + " filename \'" + strval(v_header.getValue("stored_filename")) + "\'");

                    return gVars.getSharedPclZip().errorCode();
                }
            }

            if (v_extract && !equal(v_header.getValue("status"), "ok"))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "No need for extract");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "No need for extract");
             */
             {
                v_result = this.privConvertHeader2FileInfo(v_header, p_file_list.getArrayValue(v_nb_extracted++));

                if (!equal(v_result, 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result);
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result);
                 */
                 {
                    this.privCloseFd();
                    this.privSwapBackMagicQuotes();

                    return v_result;
                }

                v_extract = false;
            }

            if (v_extract)/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "Extracting file '".$v_header['filename']."', index
             * '$i'"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 2, "Extracting file '".$v_header['filename']."',
             * index '$i'"); ----- Go to the file position ----- Go to the
             * file position >zip_fd)."'"); zip_fd)."'");
             */

            /*
             * >zip_fd)."'"); zip_fd)."'");
             *
             * >zip_fd)."'"); zip_fd)."'"); ----- Look for extraction as
             * string ----- Look for extraction as string
             */
             {
                FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);

                if (booleanval(FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, intval(v_header.getValue("offset")))))/*
                 * ----- Close the zip file ----- Close the zip file
                 */

                /*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    this.privCloseFd();
                    this.privSwapBackMagicQuotes();
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_ARCHIVE_ZIP(), "Invalid archive size");

                    return gVars.getSharedPclZip().errorCode();
                }

                if (booleanval(p_options.getValue(gConsts.getPCLZIP_OPT_EXTRACT_AS_STRING())))/*
                 * ----- Extracting the file ----- Extracting the file
                 */

                /*
                 * ----- Get the only interesting attributes ----- Get the
                 * only interesting attributes
                 */

                /*
                 * ----- Set the file content ----- Set the file content
                 */

                /*
                 * ----- Next extracted file ----- Next extracted file
                 */

                /*
                 * ----- Look for user callback abort ----- Look for user
                 * callback abort
                 */
                 {
                    v_result1 = this.privExtractFileAsString(v_header, v_string);

                    if (v_result1 < 1)/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result1);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result1);
                     */
                     {
                        this.privCloseFd();
                        this.privSwapBackMagicQuotes();

                        return v_result1;
                    }

                    if (!equal(v_result = this.privConvertHeader2FileInfo(v_header, p_file_list.getArrayValue(v_nb_extracted)), 1))/*
                     * ----- Close the zip file ----- Close the zip file
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result);
                     */
                     {
                        this.privCloseFd();
                        this.privSwapBackMagicQuotes();

                        return v_result;
                    }

                    p_file_list.getArrayValue(v_nb_extracted).putValue("content", v_string);
                    v_nb_extracted++;

                    if (equal(v_result1, 2)) {
                        break;
                    }
                } else if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_EXTRACT_IN_OUTPUT())) && booleanval(p_options.getValue(gConsts.getPCLZIP_OPT_EXTRACT_IN_OUTPUT())))/*
                 * ----- Look for extraction in standard output -----
                 * Look for extraction in standard output -----
                 * Extracting the file in standard output -----
                 * Extracting the file in standard output
                 */

                /*
                 * ----- Get the only interesting attributes ----- Get
                 * the only interesting attributes
                 */

                /*
                 * ----- Look for user callback abort ----- Look for
                 * user callback abort
                 */
                 {
                    v_result1 = this.privExtractFileInOutput(v_header, p_options);

                    if (v_result1 < 1)/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result1);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result1);
                     */
                     {
                        this.privCloseFd();
                        this.privSwapBackMagicQuotes();

                        return v_result1;
                    }

                    if (!equal(v_result = this.privConvertHeader2FileInfo(v_header, p_file_list.getArrayValue(v_nb_extracted++)), 1))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result);
                     */
                     {
                        this.privCloseFd();
                        this.privSwapBackMagicQuotes();

                        return v_result;
                    }

                    if (equal(v_result1, 2)) {
                        break;
                    }
                } else/*
                 * ----- Look for normal extraction ----- Look for
                 * normal extraction ----- Extracting the file -----
                 * Extracting the file
                 */

                /*
                 * ----- Get the only interesting attributes ----- Get
                 * the only interesting attributes
                 */

                /*
                 * ----- Look for user callback abort ----- Look for
                 * user callback abort
                 */
                 {
                    v_result1 = this.privExtractFile(v_header, p_path, p_remove_path, p_remove_all_path, p_options);

                    if (v_result1 < 1)/*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result1);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result1);
                     */
                     {
                        this.privCloseFd();
                        this.privSwapBackMagicQuotes();

                        return v_result1;
                    }

                    if (!equal(v_result = this.privConvertHeader2FileInfo(v_header, p_file_list.getArrayValue(v_nb_extracted++)), 1))/*
                     * ----- Close the zip file ----- Close the zip file
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, $v_result);
                     */
                     {
                        this.privCloseFd();
                        this.privSwapBackMagicQuotes();

                        return v_result;
                    }

                    if (equal(v_result1, 2)) {
                        break;
                    }
                }
            }
        }

        this.privCloseFd();
        this.privSwapBackMagicQuotes();

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privExtractFile() Function : privExtractFile() Description :
     * Description : Parameters : Parameters : Return Values : Return Values :
     * ?
     * PCLZIP_ERR_USER_ABORTED(2) : User ask for extraction stop in callback
     * PCLZIP_ERR_USER_ABORTED(2) : User ask for extraction stop in callback
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privExtractFile(Array<Object> p_entry, String p_path, String p_remove_path, Object p_remove_all_path, Array<Object> p_options) {
        int v_result;
        Array<Object> v_header = new Array<Object>();
        int p_remove_path_size = 0;
        int v_inclusion = 0;
        Array<Object> v_local_header = new Array<Object>();
        String v_dir_to_check = null;
        int v_dest_file = 0;
        int v_size;
        int v_read_size;
        String v_buffer = null;
        String v_file_content = null;
        v_result = 1;

        if (!equal(v_result = this.privReadFileHeader(v_header), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (!equal(this.privCheckFileHeaders(v_header, p_entry), 1)) {
        }

        /*
         * ----- Look for path to remove ----- Look for path to remove
         */
        if (equal(p_remove_all_path, true))/*
         * ----- Look for folder entry that not need to be extracted ----- Look
         * for folder entry that not need to be extracted
         *
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "All
         * path is removed"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 3, "All path is removed"); ----- Get the basename of the
         * path ----- Get the basename of the path
         */
         {
            if (equal(intval(p_entry.getValue("external")) & 16, 16))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "The entry is a folder : need to be filtered");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "The entry is a folder : need to be filtered");
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                p_entry.putValue("status", "filtered");

                return v_result;
            }

            p_entry.putValue("filename", FileSystemOrSocket.basename(strval(p_entry.getValue("filename"))));
        } else if (!equal(p_remove_path, ""))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Look for some path to remove");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Look for some path to remove");
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Resulting file is '".$p_entry['filename']."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Resulting file is '".$p_entry['filename']."'");
         */
         {
            if (equal(getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilPathInclusion(p_remove_path, strval(p_entry.getValue("filename"))), 2))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "The folder is the same as the removed path
             * '".$p_entry['filename']."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "The folder is the same as the removed path
             * '".$p_entry['filename']."'"); ----- Change the file status
             * ----- Change the file status
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                p_entry.putValue("status", "filtered");

                return v_result;
            }

            p_remove_path_size = Strings.strlen(p_remove_path);

            if (equal(Strings.substr(strval(p_entry.getValue("filename")), 0, p_remove_path_size), p_remove_path))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "Found path '$p_remove_path' to remove in file
             * '".$p_entry['filename']."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "Found path '$p_remove_path' to remove in file
             * '".$p_entry['filename']."'"); ----- Remove the path -----
             * Remove the path
             */
             {
                p_entry.putValue("filename", Strings.substr(strval(p_entry.getValue("filename")), p_remove_path_size));
            }
        }

        if (!equal(p_path, "")) {
            p_entry.putValue("filename", p_path + "/" + strval(p_entry.getValue("filename")));
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_EXTRACT_DIR_RESTRICTION())))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Check the extract directory restriction");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Check the extract directory restriction");
         */
         {
            v_inclusion = getIncluded(Class_pclzipPage.class, gVars, gConsts)
                              .PclZipUtilPathInclusion(strval(p_options.getValue(gConsts.getPCLZIP_OPT_EXTRACT_DIR_RESTRICTION())), strval(p_entry.getValue("filename")));

            if (equal(v_inclusion, 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "PCLZIP_OPT_EXTRACT_DIR_RESTRICTION is selected, file is outside
             * restriction"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 2, "PCLZIP_OPT_EXTRACT_DIR_RESTRICTION is selected,
             * file is outside restriction");
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip()
                     .privErrorLog(gConsts.getPCLZIP_ERR_DIRECTORY_RESTRICTION(), "Filename \'" + strval(p_entry.getValue("filename")) + "\' is " + "outside PCLZIP_OPT_EXTRACT_DIR_RESTRICTION");

                return gVars.getSharedPclZip().errorCode();
            }
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_CB_PRE_EXTRACT())))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "A
         * pre-callback '".$p_options[PCLZIP_CB_PRE_EXTRACT]."()') is defined
         * for the extraction");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "A
         * pre-callback '".$p_options[PCLZIP_CB_PRE_EXTRACT]."()') is defined
         * for the extraction"); ----- Generate a local information -----
         * Generate a local information
         */

        /*
         * ----- Call the callback ----- Call the callback Here I do not use
         * call_user_func() because I need to send a reference to the Here I do
         * not use call_user_func() because I need to send a reference to the
         * header. header.
         */

        /*
         * ----- Look for abort result ----- Look for abort result
         */

        /*
         * ----- Update the informations ----- Update the informations Only some
         * fields can be modified Only some fields can be modified
         */
         {
            v_local_header = new Array<Object>();
            this.privConvertHeader2FileInfo(p_entry, v_local_header);

            // Modified by Numiton
            LOG.warn("Unimplemented callback through eval");

            if (equal(v_result, 0))/*
             * ----- Change the file status ----- Change the file status
             */
             {
                p_entry.putValue("status", "skipped");
                v_result = 1;
            }

            if (equal(v_result, 2))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "User callback abort the extraction");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "User callback abort the extraction"); ----- This status is
             * internal and will be changed in 'skipped' ----- This status is
             * internal and will be changed in 'skipped'
             */
             {
                p_entry.putValue("status", "aborted");
                v_result = gConsts.getPCLZIP_ERR_USER_ABORTED();
            }

            p_entry.putValue("filename", v_local_header.getValue("filename"));
        }

        if (equal(p_entry.getValue("status"), "ok"))/*
         * ----- Look for specific actions while the file exist ----- Look for
         * specific actions while the file exist
         */
         {
            if (FileSystemOrSocket.file_exists(gVars.webEnv, strval(p_entry.getValue("filename"))))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File '".$p_entry['filename']."' already exists");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "File '".$p_entry['filename']."' already exists"); ----- Look if
             * file is a directory ----- Look if file is a directory
             *
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Existing file '".$p_entry['filename']."' is older than the
             * extrated one - will be replaced by the extracted one (".date("l
             * dS of F Y h:i:s A", filemtime($p_entry['filename'])).") than the
             * extracted file (".date("l dS of F Y h:i:s A",
             * $p_entry['mtime']).")");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Existing file '".$p_entry['filename']."' is older than the
             * extrated one - will be replaced by the extracted one (".date("l
             * dS of F Y h:i:s A", filemtime($p_entry['filename'])).") than the
             * extracted file (".date("l dS of F Y h:i:s A",
             * $p_entry['mtime']).")");
             */
             {
                /*
                 * ----- Look if file is write protected ----- Look if file is
                 * write protected
                 */
                if (FileSystemOrSocket.is_dir(gVars.webEnv, strval(p_entry.getValue("filename"))))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Existing file '".$p_entry['filename']."' is a
                 * directory");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Existing file '".$p_entry['filename']."' is a
                 * directory"); ----- Change the file status ----- Change the
                 * file status
                 */

                /*
                 * ----- Look for PCLZIP_OPT_STOP_ON_ERROR ----- Look for
                 * PCLZIP_OPT_STOP_ON_ERROR For historical reason first PclZip
                 * implementation does not stop For historical reason first
                 * PclZip implementation does not stop when this kind of error
                 * occurs. when this kind of error occurs.
                 */
                 {
                    p_entry.putValue("status", "already_a_directory");

                    if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR())) && strictEqual(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR()), true))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                     * extraction will be stopped");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                     * extraction will be stopped");
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip()
                             .privErrorLog(gConsts.getPCLZIP_ERR_ALREADY_A_DIRECTORY(), "Filename \'" + strval(p_entry.getValue("filename")) + "\' is " + "already used by an existing directory");

                        return gVars.getSharedPclZip().errorCode();
                    }
                } else
                /*
                 * ----- Look if the extracted file is older ----- Look if
                 * the extracted file is older
                 */
                if (!FileSystemOrSocket.is_writeable(gVars.webEnv, strval(p_entry.getValue("filename"))))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "Existing file '".$p_entry['filename']."' is
                 * write protected");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "Existing file '".$p_entry['filename']."' is
                 * write protected"); ----- Change the file status -----
                 * Change the file status
                 */

                /*
                 * ----- Look for PCLZIP_OPT_STOP_ON_ERROR ----- Look for
                 * PCLZIP_OPT_STOP_ON_ERROR For historical reason first
                 * PclZip implementation does not stop For historical reason
                 * first PclZip implementation does not stop when this kind
                 * of error occurs. when this kind of error occurs.
                 */
                 {
                    p_entry.putValue("status", "write_protected");

                    if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR())) && strictEqual(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR()), true))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                     * extraction will be stopped");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is selected,
                     * extraction will be stopped");
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                     * __LINE__, PclZip::errorCode(), PclZip::errorInfo());
                     */
                     {
                        gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_WRITE_OPEN_FAIL(), "Filename \'" + strval(p_entry.getValue("filename")) + "\' exists " + "and is write protected");

                        return gVars.getSharedPclZip().errorCode();
                    }
                } else if (FileSystemOrSocket.filemtime(gVars.webEnv, strval(p_entry.getValue("filename"))) > intval(p_entry.getValue("mtime")))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "Existing file
                 * '".$p_entry['filename']."' is newer (".date("l dS of
                 * F Y h:i:s A", filemtime($p_entry['filename'])).")
                 * than the extracted file (".date("l dS of F Y h:i:s
                 * A", $p_entry['mtime']).")");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 2, "Existing file
                 * '".$p_entry['filename']."' is newer (".date("l dS of
                 * F Y h:i:s A", filemtime($p_entry['filename'])).")
                 * than the extracted file (".date("l dS of F Y h:i:s
                 * A", $p_entry['mtime']).")"); ----- Change the file
                 * status ----- Change the file status
                 */
                 {
                    if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_REPLACE_NEWER())) && strictEqual(p_options.getValue(gConsts.getPCLZIP_OPT_REPLACE_NEWER()), true)) {
                    } else/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "PCLZIP_OPT_REPLACE_NEWER is
                     * selected, file will be replaced");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "PCLZIP_OPT_REPLACE_NEWER is
                     * selected, file will be replaced");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "File will not be replaced");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "File will not be replaced");
                     */

                    /*
                     * ----- Look for PCLZIP_OPT_STOP_ON_ERROR -----
                     * Look for PCLZIP_OPT_STOP_ON_ERROR For historical
                     * reason first PclZip implementation does not stop
                     * For historical reason first PclZip implementation
                     * does not stop when this kind of error occurs.
                     * when this kind of error occurs.
                     */
                     {
                        p_entry.putValue("status", "newer_exist");

                        if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR())) && strictEqual(p_options.getValue(gConsts.getPCLZIP_OPT_STOP_ON_ERROR()), true))/*
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is
                         * selected, extraction will be stopped");
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 2, "PCLZIP_OPT_STOP_ON_ERROR is
                         * selected, extraction will be stopped");
                         */

                        /*
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(),
                         * PclZip::errorInfo());
                         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                         * __LINE__, PclZip::errorCode(),
                         * PclZip::errorInfo());
                         */
                         {
                            gVars.getSharedPclZip().privErrorLog(
                                gConsts.getPCLZIP_ERR_WRITE_OPEN_FAIL(),
                                "Newer version of \'" + strval(p_entry.getValue("filename")) + "\' exists " + "and option PCLZIP_OPT_REPLACE_NEWER is not selected");

                            return gVars.getSharedPclZip().errorCode();
                        }
                    }
                } else {
                }
            } else/*
             * ----- Check the directory availability and create it if necessary
             * ----- Check the directory availability and create it if necessary
             */
             {
                if (equal(intval(p_entry.getValue("external")) & 16, 16) || equal(Strings.substr(strval(p_entry.getValue("filename")), -1), "/")) {
                    v_dir_to_check = strval(p_entry.getValue("filename"));
                } else if (!booleanval(Strings.strstr(strval(p_entry.getValue("filename")), "/"))) {
                    v_dir_to_check = "";
                } else {
                    v_dir_to_check = FileSystemOrSocket.dirname(strval(p_entry.getValue("filename")));
                }

                if (!equal(v_result = this.privDirCheck(v_dir_to_check, equal(intval(p_entry.getValue("external")) & 16, 16)), 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Unable to create path for '".$p_entry['filename']."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Unable to create path for '".$p_entry['filename']."'");
                 * ----- Change the file status ----- Change the file status
                 */

                /*
                 * ----- Return ----- Return
                 * //--(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); //--(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result); return $v_result; return $v_result;
                 */
                 {
                    p_entry.putValue("status", "path_creation_fail");
                    v_result = 1;
                }
            }
        }

        if (equal(p_entry.getValue("status"), "ok"))/*
         * ----- Do the extraction (if not a folder) ----- Do the extraction (if
         * not a folder)
         */
         {
            if (!equal(intval(p_entry.getValue("external")) & 16, 16))/*
             * ----- Look for not compressed file ----- Look for not compressed
             * file
             */

            /*
             * ----- Look for chmod option ----- Look for chmod option
             *
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Extraction done");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Extraction done");
             */
             {
                if (equal(p_entry.getValue("compression"), 0))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting an un-compressed file");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting an un-compressed file"); ----- Opening
                 * destination file ----- Opening destination file
                 *
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Read '".$p_entry['size']."' bytes");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Read '".$p_entry['size']."' bytes"); ----- Read the file
                 * by PCLZIP_READ_BLOCK_SIZE octets blocks ----- Read the file
                 * by PCLZIP_READ_BLOCK_SIZE octets blocks
                 */

                /*
                 * ----- Closing the destination file ----- Closing the
                 * destination file
                 */

                /*
                 * ----- Change the file mtime ----- Change the file mtime
                 */
                 {
                    if (equal(v_dest_file = FileSystemOrSocket.fopen(gVars.webEnv, strval(p_entry.getValue("filename")), "wb"), 0))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Error while opening
                     * '".$p_entry['filename']."' in write binary mode");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Error while opening
                     * '".$p_entry['filename']."' in write binary mode"); -----
                     * Change the file status ----- Change the file status
                     */

                    /*
                     * ----- Return ----- Return
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     */
                     {
                        p_entry.putValue("status", "write_error");

                        return v_result;
                    }

                    v_size = intval(p_entry.getValue("compressed_size"));

                    while (!equal(v_size, 0))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Read $v_read_size bytes");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Read $v_read_size bytes");
                     */

                    /*
                     * Try to speed up the code $v_binary_data =
                     * pack('a'.$v_read_size, $v_buffer); @fwrite($v_dest_file,
                     * $v_binary_data, $v_read_size);
                     */
                     {
                        v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                            ? v_size
                            : gConsts.getPCLZIP_READ_BLOCK_SIZE());
                        v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, v_read_size);
                        FileSystemOrSocket.fwrite(gVars.webEnv, v_dest_file, v_buffer, v_read_size);
                        v_size = v_size - v_read_size;
                    }

                    FileSystemOrSocket.fclose(gVars.webEnv, v_dest_file);
                    JFileSystemOrSocket.touch(gVars.webEnv, strval(p_entry.getValue("filename")), intval(p_entry.getValue("mtime")));
                } else/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting a compressed file (Compression method
                 * ".$p_entry['compression'].")");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting a compressed file (Compression method
                 * ".$p_entry['compression'].")"); ----- TBC ----- TBC Need to
                 * be finished Need to be finished
                 */

                /*
                 * ----- Decompress the file ----- Decompress the file
                 */

                /*
                 * ----- Opening destination file ----- Opening destination file
                 */

                /*
                 * ----- Write the uncompressed data ----- Write the
                 * uncompressed data
                 */

                /*
                 * ----- Closing the destination file ----- Closing the
                 * destination file
                 */

                /*
                 * ----- Change the file mtime ----- Change the file mtime
                 */
                 {
                    if (equal(intval(p_entry.getValue("flag")) & 1, 1)) {
                    } else/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "File is encrypted");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "File is encrypted");
                     *  // ----- Read the encryption header
                     * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 5, "Read 12 encryption header bytes");
                     * $v_encryption_header = @fread($this->zip_fd, 12);
                     *  // ----- Read the encrypted & compressed file in a
                     * buffer
                     * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 5, "Read '".($p_entry['compressed_size']-12)."'
                     * compressed & encrypted bytes"); $v_buffer =
                     * @fread($this->zip_fd, $p_entry['compressed_size']-12);
                     *  // ----- Decrypt the buffer
                     * $this->privDecrypt($v_encryption_header, $v_buffer,
                     * $p_entry['compressed_size']-12, $p_entry['crc']);
                     * //--(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 5, "Buffer is '".$v_buffer."'");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 5, "Read '".$p_entry['compressed_size']."'
                     * compressed bytes");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 5, "Read '".$p_entry['compressed_size']."'
                     * compressed bytes"); ----- Read the compressed file in a
                     * buffer (one shot) ----- Read the compressed file in a
                     * buffer (one shot)
                     */
                     {
                        v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_entry.getValue("compressed_size")));
                    }

                    v_file_content = GZIP.gzinflate(v_buffer);
                    v_buffer = null;

                    if (strictEqual(v_file_content, STRING_FALSE))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Unable to inflate compressed file");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Unable to inflate compressed file"); -----
                     * Change the file status ----- Change the file status TBC
                     * TBC
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     */
                     {
                        p_entry.putValue("status", "error");

                        return v_result;
                    }

                    if (equal(v_dest_file = FileSystemOrSocket.fopen(gVars.webEnv, strval(p_entry.getValue("filename")), "wb"), 0))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Error while opening
                     * '".$p_entry['filename']."' in write binary mode");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 2, "Error while opening
                     * '".$p_entry['filename']."' in write binary mode"); -----
                     * Change the file status ----- Change the file status
                     */

                    /*
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                     * $v_result);
                     */
                     {
                        p_entry.putValue("status", "write_error");

                        return v_result;
                    }

                    FileSystemOrSocket.fwrite(gVars.webEnv, v_dest_file, v_file_content, intval(p_entry.getValue("size")));
                    v_file_content = null;
                    FileSystemOrSocket.fclose(gVars.webEnv, v_dest_file);
                    JFileSystemOrSocket.touch(gVars.webEnv, strval(p_entry.getValue("filename")), intval(p_entry.getValue("mtime")));
                }

                if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_SET_CHMOD())))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "chmod option activated
                 * '".$p_options[PCLZIP_OPT_SET_CHMOD]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "chmod option activated
                 * '".$p_options[PCLZIP_OPT_SET_CHMOD]."'"); ----- Change the
                 * mode of the file ----- Change the mode of the file
                 */
                 {
                    JFileSystemOrSocket.chmod(gVars.webEnv, strval(p_entry.getValue("filename")), intval(p_options.getValue(gConsts.getPCLZIP_OPT_SET_CHMOD())));
                }
            }
        }

        if (equal(p_entry.getValue("status"), "aborted")) {
            p_entry.putValue("status", "skipped");
        } else if (isset(p_options.getValue(gConsts.getPCLZIP_CB_POST_EXTRACT())))/*
         * ----- Look for post-extract callback ----- Look for post-extract
         * callback --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 2, "A post-callback
         * '".$p_options[PCLZIP_CB_POST_EXTRACT]."()') is defined for the
         * extraction"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 2, "A post-callback
         * '".$p_options[PCLZIP_CB_POST_EXTRACT]."()') is defined for the
         * extraction"); ----- Generate a local information ----- Generate a
         * local information
         */

        /*
         * ----- Call the callback ----- Call the callback Here I do not use
         * call_user_func() because I need to send a reference to the Here I
         * do not use call_user_func() because I need to send a reference to
         * the header. header.
         */

        /*
         * ----- Look for abort result ----- Look for abort result
         */
         {
            v_local_header = new Array<Object>();
            this.privConvertHeader2FileInfo(p_entry, v_local_header);

            // Modified by Numiton
            LOG.warn("Unimplemented callback through eval");

            if (equal(v_result, 2))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "User callback abort the extraction");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "User callback abort the extraction");
             */
             {
                v_result = gConsts.getPCLZIP_ERR_USER_ABORTED();
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privExtractFileInOutput() Function : privExtractFileInOutput()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privExtractFileInOutput(Array<Object> p_entry, Array<Object> p_options) {
        int v_result = 0;
        Array<Object> v_header = new Array<Object>();
        Array<Object> v_local_header = new Array<Object>();
        String v_buffer = null;
        String v_file_content = null;
        v_result = 1;

        if (!equal(v_result = this.privReadFileHeader(v_header), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (!equal(this.privCheckFileHeaders(v_header, p_entry), 1)) {
        }

        if (isset(p_options.getValue(gConsts.getPCLZIP_CB_PRE_EXTRACT())))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "A
         * pre-callback '".$p_options[PCLZIP_CB_PRE_EXTRACT]."()') is defined
         * for the extraction");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "A
         * pre-callback '".$p_options[PCLZIP_CB_PRE_EXTRACT]."()') is defined
         * for the extraction"); ----- Generate a local information -----
         * Generate a local information
         */

        /*
         * ----- Call the callback ----- Call the callback Here I do not use
         * call_user_func() because I need to send a reference to the Here I do
         * not use call_user_func() because I need to send a reference to the
         * header. header.
         */

        /*
         * ----- Look for abort result ----- Look for abort result
         */

        /*
         * ----- Update the informations ----- Update the informations Only some
         * fields can be modified Only some fields can be modified
         */
         {
            v_local_header = new Array<Object>();
            this.privConvertHeader2FileInfo(p_entry, v_local_header);

            // Modified by Numiton
            LOG.warn("Unimplemented callback through eval");

            if (equal(v_result, 0))/*
             * ----- Change the file status ----- Change the file status
             */
             {
                p_entry.putValue("status", "skipped");
                v_result = 1;
            }

            if (equal(v_result, 2))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "User callback abort the extraction");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "User callback abort the extraction"); ----- This status is
             * internal and will be changed in 'skipped' ----- This status is
             * internal and will be changed in 'skipped'
             */
             {
                p_entry.putValue("status", "aborted");
                v_result = gConsts.getPCLZIP_ERR_USER_ABORTED();
            }

            p_entry.putValue("filename", v_local_header.getValue("filename"));
        }

        if (equal(p_entry.getValue("status"), "ok"))/*
         * ----- Do the extraction (if not a folder) ----- Do the extraction (if
         * not a folder)
         *
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Extraction done");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Extraction done");
         */
         {
            if (!equal(intval(p_entry.getValue("external")) & 16, 16))/*
             * ----- Look for not compressed file ----- Look for not compressed
             * file
             */
             {
                if (equal(p_entry.getValue("compressed_size"), p_entry.getValue("size")))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting an un-compressed file");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting an un-compressed file");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Reading '".$p_entry['size']."' bytes");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Reading '".$p_entry['size']."' bytes"); ----- Read the
                 * file in a buffer (one shot) ----- Read the file in a buffer
                 * (one shot)
                 */

                /*
                 * ----- Send the file to the output ----- Send the file to the
                 * output
                 */
                 {
                    v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_entry.getValue("compressed_size")));
                    echo(gVars.webEnv, v_buffer);
                    v_buffer = null;
                } else/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting a compressed file");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 2, "Extracting a compressed file");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 5, "Reading '".$p_entry['size']."' bytes");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
                 * 5, "Reading '".$p_entry['size']."' bytes"); ----- Read the
                 * compressed file in a buffer (one shot) ----- Read the
                 * compressed file in a buffer (one shot)
                 */

                /*
                 * ----- Decompress the file ----- Decompress the file
                 */

                /*
                 * ----- Send the file to the output ----- Send the file to the
                 * output
                 */
                 {
                    v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_entry.getValue("compressed_size")));
                    v_file_content = GZIP.gzinflate(v_buffer);
                    v_buffer = null;
                    echo(gVars.webEnv, v_file_content);
                    v_file_content = null;
                }
            }
        }

        if (equal(p_entry.getValue("status"), "aborted")) {
            p_entry.putValue("status", "skipped");
        } else if (isset(p_options.getValue(gConsts.getPCLZIP_CB_POST_EXTRACT())))/*
         * ----- Look for post-extract callback ----- Look for post-extract
         * callback --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 2, "A post-callback
         * '".$p_options[PCLZIP_CB_POST_EXTRACT]."()') is defined for the
         * extraction"); --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
         * __LINE__, 2, "A post-callback
         * '".$p_options[PCLZIP_CB_POST_EXTRACT]."()') is defined for the
         * extraction"); ----- Generate a local information ----- Generate a
         * local information
         */

        /*
         * ----- Call the callback ----- Call the callback Here I do not use
         * call_user_func() because I need to send a reference to the Here I
         * do not use call_user_func() because I need to send a reference to
         * the header. header.
         */

        /*
         * ----- Look for abort result ----- Look for abort result
         */
         {
            v_local_header = new Array<Object>();
            this.privConvertHeader2FileInfo(p_entry, v_local_header);

            // Modified by Numiton
            LOG.warn("Unimplemented callback through eval");

            if (equal(v_result, 2))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "User callback abort the extraction");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "User callback abort the extraction");
             */
             {
                v_result = gConsts.getPCLZIP_ERR_USER_ABORTED();
            }
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privExtractFileAsString() Function : privExtractFileAsString()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privExtractFileAsString(Array<Object> p_entry, Ref<String> p_string) {
        int v_result = 0;
        Array<Object> v_header = new Array<Object>();
        String v_data = null;
        v_result = 1;
        v_header = new Array<Object>();

        if (!equal(v_result = this.privReadFileHeader(v_header), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (!equal(this.privCheckFileHeaders(v_header, p_entry), 1)) {
        }

        if (!equal(intval(p_entry.getValue("external")) & 16, 16))/*
         * ----- Look for not compressed file ----- Look for not compressed file
         * if ($p_entry['compressed_size'] == $p_entry['size']) if
         * ($p_entry['compressed_size'] == $p_entry['size'])
         */
         {
            if (equal(p_entry.getValue("compression"), 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Extracting an un-compressed file");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Extracting an un-compressed file");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Reading '".$p_entry['size']."' bytes");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Reading '".$p_entry['size']."' bytes"); ----- Reading the file
             * ----- Reading the file
             */
             {
                p_string.value = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_entry.getValue("compressed_size")));
            } else/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Extracting a compressed file (compression method
             * '".$p_entry['compression']."')");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Extracting a compressed file (compression method
             * '".$p_entry['compression']."')"); ----- Reading the file -----
             * Reading the file
             */

            /*
             * ----- Decompress the file ----- Decompress the file
             *
             * TBC TBC
             */
             {
                v_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_entry.getValue("compressed_size")));

                if (strictEqual(p_string.value = GZIP.gzinflate(v_data), STRING_FALSE)) {
                }
            }
        } else {
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privReadFileHeader() Function : privReadFileHeader()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privReadFileHeader(Array<Object> p_header) {
        int v_result = 0;
        String v_binary_data = null;
        Array<Object> v_data = new Array<Object>();
        int v_hour = 0;
        int v_minute = 0;
        int v_seconde = 0;
        int v_year = 0;
        int v_month = 0;
        int v_day = 0;
        v_result = 1;
        v_binary_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 4);
        v_data = QMisc.unpack("Vid", v_binary_data);

        if (!equal(v_data.getValue("id"), 67324752))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Invalid File header");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Invalid File header"); ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Invalid archive structure");

            return gVars.getSharedPclZip().errorCode();
        }

        v_binary_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 26);

        if (!equal(Strings.strlen(v_binary_data), 26))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Invalid block size : ".strlen($v_binary_data));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Invalid block size : ".strlen($v_binary_data)); ----- Error log
         * ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            p_header.putValue("filename", "");
            p_header.putValue("status", "invalid_header");
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Invalid block size : " + strval(Strings.strlen(v_binary_data)));

            return gVars.getSharedPclZip().errorCode();
        }

        v_data = QMisc.unpack("vversion/vflag/vcompression/vmtime/vmdate/Vcrc/Vcompressed_size/Vsize/vfilename_len/vextra_len", v_binary_data);
        p_header.putValue("filename", FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(v_data.getValue("filename_len"))));

        if (!equal(v_data.getValue("extra_len"), 0)) {
            p_header.putValue("extra", FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(v_data.getValue("extra_len"))));
        } else {
            p_header.putValue("extra", "");
        }

        p_header.putValue("version_extracted", v_data.getValue("version"));
        p_header.putValue("compression", v_data.getValue("compression"));
        p_header.putValue("size", v_data.getValue("size"));
        p_header.putValue("compressed_size", v_data.getValue("compressed_size"));
        p_header.putValue("crc", v_data.getValue("crc"));
        p_header.putValue("flag", v_data.getValue("flag"));
        p_header.putValue("filename_len", v_data.getValue("filename_len"));
        p_header.putValue("mdate", v_data.getValue("mdate"));
        p_header.putValue("mtime", v_data.getValue("mtime"));

        if (booleanval(p_header.getValue("mdate")) && booleanval(p_header.getValue("mtime")))/*
         * ----- Extract time ----- Extract time
         */

        /*
         * ----- Extract date ----- Extract date
         */

        /*
         * ----- Get UNIX date format ----- Get UNIX date format
         */
         {
            v_hour = (intval(p_header.getValue("mtime")) & 63488) >> 11;
            v_minute = (intval(p_header.getValue("mtime")) & 2016) >> 5;
            v_seconde = (intval(p_header.getValue("mtime")) & 31) * 2;
            v_year = ((intval(p_header.getValue("mdate")) & 65024) >> 9) + 1980;
            v_month = (intval(p_header.getValue("mdate")) & 480) >> 5;
            v_day = intval(p_header.getValue("mdate")) & 31;
            p_header.putValue("mtime", DateTime.mktime(v_hour, v_minute, v_seconde, v_month, v_day, v_year));
        } else/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, 'Date :
         * \''.date("d/m/y H:i:s", $p_header['mtime']).'\'');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, 'Date :
         * \''.date("d/m/y H:i:s", $p_header['mtime']).'\'');
         */
         {
            p_header.putValue("mtime", DateTime.time());
        }

        p_header.putValue("stored_filename", p_header.getValue("filename"));
        p_header.putValue("status", "ok");

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privReadCentralFileHeader() Function :
     * privReadCentralFileHeader() Description : Description : Parameters :
     * Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privReadCentralFileHeader(Array<Object> p_header) {
        int v_result = 0;
        String v_binary_data = null;
        Array<Object> v_data = new Array<Object>();
        int v_hour = 0;
        int v_minute = 0;
        int v_seconde = 0;
        int v_year = 0;
        int v_month = 0;
        int v_day = 0;
        v_result = 1;
        v_binary_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 4);
        v_data = QMisc.unpack("Vid", v_binary_data);

        if (!equal(v_data.getValue("id"), 33639248))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Invalid Central Dir File signature");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Invalid Central Dir File signature"); ----- Error log ----- Error
         * log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Invalid archive structure");

            return gVars.getSharedPclZip().errorCode();
        }

        v_binary_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 42);

        if (!equal(Strings.strlen(v_binary_data), 42))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Invalid block size : ".strlen($v_binary_data));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Invalid block size : ".strlen($v_binary_data)); ----- Error log
         * ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            p_header.putValue("filename", "");
            p_header.putValue("status", "invalid_header");
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Invalid block size : " + strval(Strings.strlen(v_binary_data)));

            return gVars.getSharedPclZip().errorCode();
        }

        p_header = QMisc.unpack(
                "vversion/vversion_extracted/vflag/vcompression/vmtime/vmdate/Vcrc/Vcompressed_size/Vsize/vfilename_len/vextra_len/vcomment_len/vdisk/vinternal/Vexternal/Voffset",
                v_binary_data);

        if (!equal(p_header.getValue("filename_len"), 0)) {
            p_header.putValue("filename", FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_header.getValue("filename_len"))));
        } else {
            p_header.putValue("filename", "");
        }

        if (!equal(p_header.getValue("extra_len"), 0)) {
            p_header.putValue("extra", FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_header.getValue("extra_len"))));
        } else {
            p_header.putValue("extra", "");
        }

        if (!equal(p_header.getValue("comment_len"), 0)) {
            p_header.putValue("comment", FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(p_header.getValue("comment_len"))));
        } else {
            p_header.putValue("comment", "");
        }

        if (booleanval(p_header.getValue("mdate")) && booleanval(p_header.getValue("mtime")))/*
         * ----- Extract time ----- Extract time
         */

        /*
         * ----- Extract date ----- Extract date
         */

        /*
         * ----- Get UNIX date format ----- Get UNIX date format
         */
         {
            v_hour = (intval(p_header.getValue("mtime")) & 63488) >> 11;
            v_minute = (intval(p_header.getValue("mtime")) & 2016) >> 5;
            v_seconde = (intval(p_header.getValue("mtime")) & 31) * 2;
            v_year = ((intval(p_header.getValue("mdate")) & 65024) >> 9) + 1980;
            v_month = (intval(p_header.getValue("mdate")) & 480) >> 5;
            v_day = intval(p_header.getValue("mdate")) & 31;
            p_header.putValue("mtime", DateTime.mktime(v_hour, v_minute, v_seconde, v_month, v_day, v_year));
        } else/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, 'Date :
         * \''.date("d/m/y H:i:s", $p_header['mtime']).'\'');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, 'Date :
         * \''.date("d/m/y H:i:s", $p_header['mtime']).'\'');
         */
         {
            p_header.putValue("mtime", DateTime.time());
        }

        p_header.putValue("stored_filename", p_header.getValue("filename"));
        p_header.putValue("status", "ok");

        if (equal(Strings.substr(strval(p_header.getValue("filename")), -1), "/"))/*
         * $p_header['external'] = 0x41FF0010; $p_header['external'] =
         * 0x41FF0010;
         */
         {
            p_header.putValue("external", 16);
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privCheckFileHeaders() Function : privCheckFileHeaders()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values : 1 on success, 1 on success, 0 on error; 0 on error;
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privCheckFileHeaders(Array<Object> p_local_header, Array<Object> p_central_header) {
        int v_result = 0;
        v_result = 1;

        if (!equal(p_local_header.getValue("filename"), p_central_header.getValue("filename"))) {
        }

        if (!equal(p_local_header.getValue("version_extracted"), p_central_header.getValue("version_extracted"))) {
        }

        if (!equal(p_local_header.getValue("flag"), p_central_header.getValue("flag"))) {
        }

        if (!equal(p_local_header.getValue("compression"), p_central_header.getValue("compression"))) {
        }

        if (!equal(p_local_header.getValue("mtime"), p_central_header.getValue("mtime"))) {
        }

        if (!equal(p_local_header.getValue("filename_len"), p_central_header.getValue("filename_len"))) {
        }

        if (equal(intval(p_local_header.getValue("flag")) & 8, 8))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * 'Purpose bit flag bit 3 set !');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * 'Purpose bit flag bit 3 set !');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, 'File
         * size, compression size and crc found in central header');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, 'File
         * size, compression size and crc found in central header');
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, 'Size :
         * \''.$p_local_header['size'].'\'');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, 'Size :
         * \''.$p_local_header['size'].'\'');
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * 'Compressed Size : \''.$p_local_header['compressed_size'].'\'');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * 'Compressed Size : \''.$p_local_header['compressed_size'].'\'');
         */
         {
            p_local_header.putValue("size", p_central_header.getValue("size"));
            p_local_header.putValue("compressed_size", p_central_header.getValue("compressed_size"));
            p_local_header.putValue("crc", p_central_header.getValue("crc"));
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privReadEndCentralDir() Function : privReadEndCentralDir()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privReadEndCentralDir(Array<Object> p_central_dir) {
        int v_result = 0;
        int v_size = 0;
        int v_found = 0;
        int v_pos = 0;
        String v_binary_data = null;
        Array<Object> v_data = new Array<Object>();
        int v_maximum_size = 0;
        int v_bytes = 0;
        String v_byte = null;
        v_result = 1;
        v_size = FileSystemOrSocket.filesize(gVars.webEnv, this.zipname);
        FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, v_size);

        if (!equal(FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd), /*, null*/
                    v_size))/*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Unable to go to the end of the archive \'" + this.zipname + "\'");

            return gVars.getSharedPclZip().errorCode();
        }

        v_found = 0;

        if (v_size > 26)/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, 'Look
         * for central dir with no comment');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, 'Look
         * for central dir with no comment');
         */

        /*
         * >zip_fd).'\''); zip_fd).'\'');
         *
         * ----- Read for bytes ----- Read for bytes
         */

        /*
         * %08x", $v_binary_data)."'"); 08x", $v_binary_data)."'");
         */

        /*
         * %08x", $v_data['id'])."'"); 08x", $v_data['id'])."'"); ----- Check
         * signature ----- Check signature
         */
         {
            FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, v_size - 22);

            if (!equal(v_pos = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd), /*, null*/
                        v_size - 22))/*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Unable to seek back to the middle of the archive \'" + this.zipname + "\'");

                return gVars.getSharedPclZip().errorCode();
            }

            v_binary_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 4);
            v_data = QMisc.unpack("Vid", v_binary_data);

            if (equal(v_data.getValue("id"), 101010256))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Found central dir at the default position.");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Found central dir at the default position.");
             */
             {
                v_found = 1;
            }

            v_pos = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd);
        }

        /*, null*/
        if (!booleanval(v_found))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
         * 'Start extended search of end central dir');
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4,
         * 'Start extended search of end central dir');
         */

        /*
         * 0xFFFF + 22; 0xFFFF + 22;
         */

        /*
         * >zip_fd).'\''); zip_fd).'\''); ----- Read byte per byte in order to
         * find the signature ----- Read byte per byte in order to find the
         * signature
         */

        /*
         * ----- Look if not found end of central dir ----- Look if not found
         * end of central dir
         */
         {
            v_maximum_size = 65557;

            if (v_maximum_size > v_size) {
                v_maximum_size = v_size;
            }

            FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, v_size - v_maximum_size);

            if (!equal(FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd), /*, null*/
                        v_size - v_maximum_size))/*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Unable to seek back to the middle of the archive \'" + this.zipname + "\'");

                return gVars.getSharedPclZip().errorCode();
            }

            v_pos = FileSystemOrSocket.ftell(gVars.webEnv, this.zip_fd);

            /*, null*/
            v_bytes = 0;

            while (v_pos < v_size)/*
             * ----- Read a byte ----- Read a byte
             */

            /*
             * ----- Add the byte ----- Add the byte Note we mask the old value
             * down such that once shifted we can never end up with more than a
             * 32bit number Note we mask the old value down such that once
             * shifted we can never end up with more than a 32bit number
             * Otherwise on systems where we have 64bit integers the check below
             * for the magic number will fail. Otherwise on systems where we
             * have 64bit integers the check below for the magic number will
             * fail.
             */

            /*
             * ----- Compare the bytes ----- Compare the bytes
             */
             {
                v_byte = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 1);
                v_bytes = ((v_bytes & 16777215) << 8) | Strings.ord(v_byte);

                if (equal(v_bytes, 1347093766))/*
                 * >zip_fd).'\''); zip_fd).'\'');
                 */
                 {
                    v_pos++;

                    break;
                }

                v_pos++;
            }

            if (equal(v_pos, v_size))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Unable to find End of Central Dir Record signature");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
             * "Unable to find End of Central Dir Record signature"); -----
             * Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Unable to find End of Central Dir Record signature");

                return gVars.getSharedPclZip().errorCode();
            }
        }

        v_binary_data = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, 18);

        if (!equal(Strings.strlen(v_binary_data), 18))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Invalid End of Central Dir Record size : ".strlen($v_binary_data));
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2,
         * "Invalid End of Central Dir Record size : ".strlen($v_binary_data));
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "Invalid End of Central Dir Record size : " + strval(Strings.strlen(v_binary_data)));

            return gVars.getSharedPclZip().errorCode();
        }

        v_data = QMisc.unpack("vdisk/vdisk_start/vdisk_entries/ventries/Vsize/Voffset/vcomment_size", v_binary_data);

        if (!equal(v_pos + intval(v_data.getValue("comment_size")) + 18, v_size))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "The
         * central dir is not at the end of the archive. Some trailing bytes
         * exists after the archive.");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 2, "The
         * central dir is not at the end of the archive. Some trailing bytes
         * exists after the archive."); ----- Removed in release 2.2 see readme
         * file ----- Removed in release 2.2 see readme file The check of the
         * file size is a little too strict. The check of the file size is a
         * little too strict. Some bugs where found when a zip is
         * encrypted/decrypted with 'crypt'. Some bugs where found when a zip is
         * encrypted/decrypted with 'crypt'. While decrypted, zip has training 0
         * bytes While decrypted, zip has training 0 bytes
         */
         {
            if (booleanval(0))/*
             * ----- Error log ----- Error log
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * PclZip::errorCode(), PclZip::errorInfo());
             */
             {
                gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_BAD_FORMAT(), "The central dir is not at the end of the archive." + " Some trailing bytes exists after the archive.");

                return gVars.getSharedPclZip().errorCode();
            }
        }

        if (!equal(v_data.getValue("comment_size"), 0)) {
            p_central_dir.putValue("comment", FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, intval(v_data.getValue("comment_size"))));
        } else {
            p_central_dir.putValue("comment", "");
        }

        p_central_dir.putValue("entries", v_data.getValue("entries"));
        p_central_dir.putValue("disk_entries", v_data.getValue("disk_entries"));
        p_central_dir.putValue("offset", v_data.getValue("offset"));
        p_central_dir.putValue("size", v_data.getValue("size"));
        p_central_dir.putValue("disk", v_data.getValue("disk"));
        p_central_dir.putValue("disk_start", v_data.getValue("disk_start"));

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privDeleteByRule() Function : privDeleteByRule() Description :
     * Description : Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privDeleteByRule(Array<Object> p_result_list, Array<Object> p_options) {
        int v_result = 0;
        Array<Object> v_list_detail = new Array<Object>();
        Array<Object> v_central_dir = new Array<Object>();
        int v_pos_entry;
        Array<Object> v_header_list = new Array<Object>();
        int j_start = 0;
        int v_nb_extracted = 0;
        int i = 0;
        boolean v_found = false;
        int j;
        String v_zip_temp_name = null;
        PclZip v_temp_zip = null;
        Array<Object> v_local_header = new Array<Object>();
        int v_offset;
        String v_comment = null;
        int v_size = 0;
        v_result = 1;
        v_list_detail = new Array<Object>();

        if (!equal(v_result = this.privOpenFd("rb"), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        v_central_dir = new Array<Object>();

        if (!equal(v_result = this.privReadEndCentralDir(v_central_dir), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();

            return v_result;
        }

        FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);
        v_pos_entry = intval(v_central_dir.getValue("offset"));
        FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);

        if (booleanval(FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, v_pos_entry)))/*
         * ----- Close the zip file ----- Close the zip file
         */

        /*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            this.privCloseFd();
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_ARCHIVE_ZIP(), "Invalid archive size");

            return gVars.getSharedPclZip().errorCode();
        }

        v_header_list = new Array<Object>();
        j_start = 0;
        i = 0;
        v_nb_extracted = 0;

        for (; i < intval(v_central_dir.getValue("entries")); i++)/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Read next file header entry (index '$i')");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Read next file header entry (index '$i')"); ----- Read the file
         * header ----- Read the file header
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Filename (index '$i') :
         * '".$v_header_list[$v_nb_extracted]['stored_filename']."'");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Filename (index '$i') :
         * '".$v_header_list[$v_nb_extracted]['stored_filename']."'"); -----
         * Store the index ----- Store the index
         */

        /*
         * ----- Look for the specific extract rules ----- Look for the
         * specific extract rules
         */

        /*
         * ----- Look for extract by name rule ----- Look for extract by
         * name rule
         */

        /*
         * ----- Look for deletion ----- Look for deletion
         */
         {
            v_header_list.putValue(v_nb_extracted, new Array<Object>());

            if (!equal(v_result = this.privReadCentralFileHeader(v_header_list.getArrayValue(v_nb_extracted)), 1))/*
             * ----- Close the zip file ----- Close the zip file
             */

            /*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                this.privCloseFd();

                return v_result;
            }

            v_header_list.getArrayValue(v_nb_extracted).putValue("index", i);
            v_found = false;

            /*
             * ----- Look for extract by ereg rule ----- Look for extract by
             * ereg rule
             */
            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_NAME())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_NAME()), 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "Extract with rule 'ByName'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 3, "Extract with rule 'ByName'"); ----- Look if the filename
             * is in the list ----- Look if the filename is in the list
             */
             {
                for (j = 0; (j < Array.sizeof(p_options.getValue(gConsts.getPCLZIP_OPT_BY_NAME()))) && !v_found; j++)/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Compare with file
                 * '".$p_options[PCLZIP_OPT_BY_NAME][$j]."'");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Compare with file
                 * '".$p_options[PCLZIP_OPT_BY_NAME][$j]."'"); ----- Look
                 * for a directory ----- Look for a directory
                 */
                 {
                    if (equal(Strings.substr(strval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)), -1), "/"))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The searched item is a directory");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The searched item is a directory");
                     * ----- Look if the directory is in the filename path
                     * ----- Look if the directory is in the filename path
                     */
                     {
                        if ((Strings.strlen(strval(v_header_list.getArrayValue(v_nb_extracted).getValue("stored_filename"))) > Strings.strlen(
                                        strval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))) &&
                                equal(Strings.substr(
                                        strval(v_header_list.getArrayValue(v_nb_extracted).getValue("stored_filename")),
                                        0,
                                        Strings.strlen(strval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))),
                                    p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))/*
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The directory is in the file
                         * path");
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The directory is in the file
                         * path");
                         */
                         {
                            v_found = true;
                        } else if (equal(intval(v_header_list.getArrayValue(v_nb_extracted).getValue("external")) & 16, 16) &&
                                equal(strval(v_header_list.getArrayValue(v_nb_extracted).getValue("stored_filename")) + "/", p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))/*
                         * Indicates a folder
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The entry is the searched
                         * directory");
                         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                         * __LINE__, 3, "The entry is the searched
                         * directory");
                         */
                         {
                            v_found = true;
                        }
                    } else if (equal(v_header_list.getArrayValue(v_nb_extracted).getValue("stored_filename"), p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_NAME()).getValue(j)))/*
                     * ----- Look for a filename ----- Look for a
                     * filename
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The file is the right one.");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "The file is the right one.");
                     */
                     {
                        v_found = true;
                    }
                }
            } else
            /*
             * ----- Look for extract by preg rule ----- Look for
             * extract by preg rule
             */
            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_EREG())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_EREG()), ""))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract by ereg
             * '".$p_options[PCLZIP_OPT_BY_EREG]."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract by ereg
             * '".$p_options[PCLZIP_OPT_BY_EREG]."'");
             */
             {
                if (booleanval(RegExPosix.ereg(strval(p_options.getValue(gConsts.getPCLZIP_OPT_BY_EREG())), strval(v_header_list.getArrayValue(v_nb_extracted).getValue("stored_filename")))))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 */
                 {
                    v_found = true;
                }
            } else
            /*
             * ----- Look for extract by index rule ----- Look for
             * extract by index rule
             */
            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_PREG())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_PREG()), ""))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByEreg'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByEreg'");
             */
             {
                if (QRegExPerl.preg_match(strval(p_options.getValue(gConsts.getPCLZIP_OPT_BY_PREG())), strval(v_header_list.getArrayValue(v_nb_extracted).getValue("stored_filename"))))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Filename match the regular
                 * expression");
                 */
                 {
                    v_found = true;
                }
            } else if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_BY_INDEX())) && !equal(p_options.getValue(gConsts.getPCLZIP_OPT_BY_INDEX()), 0))/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByIndex'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "Extract with rule 'ByIndex'");
             * ----- Look if the index is in the list ----- Look
             * if the index is in the list
             */
             {
                for (j = j_start; (j < Array.sizeof(p_options.getValue(gConsts.getPCLZIP_OPT_BY_INDEX()))) && !v_found; j++)/*
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Look if index '$i' is in
                 * [".$p_options[PCLZIP_OPT_BY_INDEX][$j]['start'].",".$p_options[PCLZIP_OPT_BY_INDEX][$j]['end']."]");
                 * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                 * __LINE__, 3, "Look if index '$i' is in
                 * [".$p_options[PCLZIP_OPT_BY_INDEX][$j]['start'].",".$p_options[PCLZIP_OPT_BY_INDEX][$j]['end']."]");
                 */
                 {
                    if ((i >= intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("start"))) &&
                            (i <= intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("end"))))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Found as part of an index
                     * range");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Found as part of an index
                     * range");
                     */
                     {
                        v_found = true;
                    }

                    if (i >= intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("end")))/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Do not look this index
                     * range for next loop");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Do not look this index
                     * range for next loop");
                     */
                     {
                        j_start = j + 1;
                    }

                    if (intval(p_options.getArrayValue(gConsts.getPCLZIP_OPT_BY_INDEX()).getArrayValue(j).getValue("start")) > i)/*
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Index range is greater than
                     * index, stop loop");
                     * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
                     * __LINE__, 3, "Index range is greater than
                     * index, stop loop");
                     */
                     {
                        break;
                    }
                }
            } else/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "No argument mean remove all file");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__,
             * __LINE__, 3, "No argument mean remove all file");
             */
             {
                v_found = true;
            }

            if (v_found)/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "File
             * '".$v_header_list[$v_nb_extracted]['stored_filename']."',
             * index '$i' need to be deleted");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "File
             * '".$v_header_list[$v_nb_extracted]['stored_filename']."',
             * index '$i' need to be deleted");
             */
             {
                v_header_list.arrayUnset(v_nb_extracted);
            } else/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "File
             * '".$v_header_list[$v_nb_extracted]['stored_filename']."',
             * index '$i' will not be deleted");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__,
             * 2, "File
             * '".$v_header_list[$v_nb_extracted]['stored_filename']."',
             * index '$i' will not be deleted");
             */
             {
                v_nb_extracted++;
            }
        }

        /*
         * ----- Remove every files : reset the file ----- Remove every files :
         * reset the file
         */
        if (v_nb_extracted > 0)/*
         * ----- Creates a temporay file ----- Creates a temporay file
         */

        /*
         * ----- Creates a temporary zip archive ----- Creates a temporary zip
         * archive
         */

        /*
         * ----- Open the temporary zip file in write mode ----- Open the
         * temporary zip file in write mode
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "Open
         * file in binary write mode");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3, "Open
         * file in binary write mode");
         *
         * ----- Look which file need to be kept ----- Look which file need to
         * be kept
         */

        /*
         * ----- Store the offset of the central dir ----- Store the offset of
         * the central dir
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "New
         * offset of central dir : $v_offset");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "New
         * offset of central dir : $v_offset"); ----- Re-Create the Central Dir
         * files header ----- Re-Create the Central Dir files header
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Creates the new central directory");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Creates the new central directory");
         */

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Creates the central directory footer");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Creates the central directory footer"); ----- Zip file comment -----
         * Zip file comment
         */

        /*
         * ----- Calculate the size of the central header ----- Calculate the
         * size of the central header
         */

        /*
         * ----- Create the central dir footer ----- Create the central dir
         * footer
         *
         * ----- Close ----- Close
         */

        /*
         * ----- Delete the zip file ----- Delete the zip file TBC : I should
         * test the result ... TBC : I should test the result ...
         */

        /*
         * ----- Rename the temporary file ----- Rename the temporary file TBC :
         * I should test the result ... TBC : I should test the result ...
         * >zipname); zipname);
         */

        /*
         * ----- Destroy the temporary archive ----- Destroy the temporary
         * archive
         */
         {
            v_zip_temp_name = gConsts.getPCLZIP_TEMPORARY_DIR() + Misc.uniqid("pclzip-") + ".tmp";
            v_temp_zip = new PclZip(gVars, gConsts, v_zip_temp_name);

            if (!equal(v_result = v_temp_zip.privOpenFd("wb"), 1))/*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                this.privCloseFd();

                return v_result;
            }

            for (i = 0; i < Array.sizeof(v_header_list); i++)/*
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Keep entry index '$i' : '".$v_header_list[$i]['filename']."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
             * "Keep entry index '$i' : '".$v_header_list[$i]['filename']."'");
             * ----- Calculate the position of the header ----- Calculate the
             * position of the header
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Offset='". $v_header_list[$i]['offset']."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Offset='". $v_header_list[$i]['offset']."'"); >zip_fd)."'");
             * zip_fd)."'");
             */

            /*
             * >zip_fd)."'"); zip_fd)."'");
             *
             * >zip_fd)."'"); zip_fd)."'"); ----- Read the file header -----
             * Read the file header
             */

            /*
             * ----- Check that local file header is same as central file header
             * ----- Check that local file header is same as central file header
             */

            /*
             * TBC TBC
             */

            /*
             * ----- Write the file header ----- Write the file header
             *
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Offset for this file is '".$v_header_list[$i]['offset']."'");
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Offset for this file is '".$v_header_list[$i]['offset']."'");
             * ----- Read/write the data block ----- Read/write the data block
             */
             {
                FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);

                if (booleanval(FileSystemOrSocket.fseek(gVars.webEnv, this.zip_fd, intval(v_header_list.getArrayValue(i).getValue("offset")))))/*
                 * ----- Close the zip file ----- Close the zip file
                 */

                /*
                 * ----- Error log ----- Error log
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * PclZip::errorCode(), PclZip::errorInfo());
                 */
                 {
                    this.privCloseFd();
                    v_temp_zip.privCloseFd();
                    JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);
                    gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_INVALID_ARCHIVE_ZIP(), "Invalid archive size");

                    return gVars.getSharedPclZip().errorCode();
                }

                v_local_header = new Array<Object>();

                if (!equal(v_result = this.privReadFileHeader(v_local_header), 1))/*
                 * ----- Close the zip file ----- Close the zip file
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    this.privCloseFd();
                    v_temp_zip.privCloseFd();
                    JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);

                    return v_result;
                }

                if (!equal(this.privCheckFileHeaders(v_local_header, v_header_list.getArrayValue(i)), 1)) {
                }

                v_local_header = null;

                if (!equal(v_result = v_temp_zip.privWriteFileHeader(v_header_list.getArrayValue(i)), 1))/*
                 * ----- Close the zip file ----- Close the zip file
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    this.privCloseFd();
                    v_temp_zip.privCloseFd();
                    JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);

                    return v_result;
                }

                if (!equal(v_result = getIncluded(Class_pclzipPage.class, gVars, gConsts)
                                              .PclZipUtilCopyBlock(this.zip_fd, v_temp_zip.zip_fd, intval(v_header_list.getArrayValue(i).getValue("compressed_size")), 0), 1))/*
                 * ----- Close the zip file ----- Close the zip file
                 */

                /*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    this.privCloseFd();
                    v_temp_zip.privCloseFd();
                    JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);

                    return v_result;
                }
            }

            v_offset = FileSystemOrSocket.ftell(gVars.webEnv, v_temp_zip.zip_fd);

            /*, null*/
            for (i = 0; i < Array.sizeof(v_header_list); i++)/*
             * ----- Create the file header ----- Create the file header
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Offset of file : ".$v_header_list[$i]['offset']);
             * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5,
             * "Offset of file : ".$v_header_list[$i]['offset']);
             *
             * ----- Transform the header to a 'usable' info ----- Transform the
             * header to a 'usable' info
             */
             {
                if (!equal(v_result = v_temp_zip.privWriteCentralFileHeader(v_header_list.getArrayValue(i)), 1))/*
                 * ----- Return ----- Return
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    v_temp_zip.privCloseFd();
                    this.privCloseFd();
                    JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);

                    return v_result;
                }

                v_temp_zip.privConvertHeader2FileInfo(v_header_list.getArrayValue(i), p_result_list.getArrayValue(i));
            }

            v_comment = "";

            if (isset(p_options.getValue(gConsts.getPCLZIP_OPT_COMMENT()))) {
                v_comment = strval(p_options.getValue(gConsts.getPCLZIP_OPT_COMMENT()));
            }

            v_size = FileSystemOrSocket.ftell(gVars.webEnv, v_temp_zip.zip_fd) - v_offset;

            if (!equal(v_result = v_temp_zip.privWriteCentralHeader(Array.sizeof(v_header_list), v_size, v_offset, v_comment), 1))/*
             * ----- Reset the file list ----- Reset the file list
             */

            /*
             * ----- Return ----- Return
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                v_header_list = null;
                v_temp_zip.privCloseFd();
                this.privCloseFd();
                JFileSystemOrSocket.unlink(gVars.webEnv, v_zip_temp_name);

                return v_result;
            }

            v_temp_zip.privCloseFd();
            this.privCloseFd();
            JFileSystemOrSocket.unlink(gVars.webEnv, this.zipname);
            getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilRename(v_zip_temp_name, this.zipname);
            v_temp_zip = null;
        } else if (!equal(v_central_dir.getValue("entries"), 0)) {
            this.privCloseFd();

            if (!equal(v_result = this.privOpenFd("wb"), 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                return v_result;
            }

            if (!equal(v_result = this.privWriteCentralHeader(0, 0, 0, ""), 1))/*
             * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
             * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
             * __LINE__, $v_result);
             */
             {
                return v_result;
            }

            this.privCloseFd();
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privDirCheck() Function : privDirCheck() Description :
     * Description : Check if a directory exists, if not it creates it and all
     * the parents directory Check if a directory exists, if not it creates it
     * and all the parents directory which may be useful. which may be useful.
     * Parameters : Parameters : $p_dir : Directory path to check. $p_dir :
     * Directory path to check. Return Values : Return Values : 1 : OK 1 : OK -1 :
     * Unable to create directory -1 : Unable to create directory
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privDirCheck(String p_dir, boolean p_is_dir) {
        int v_result = 1;
        String p_parent_dir = null;

        if (p_is_dir && equal(Strings.substr(p_dir, -1), "/")) {
            p_dir = Strings.substr(p_dir, 0, Strings.strlen(p_dir) - 1);
        }

        if (FileSystemOrSocket.is_dir(gVars.webEnv, p_dir) || equal(p_dir, ""))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, "'$p_dir' is
         * a directory"); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
         * __LINE__, "'$p_dir' is a directory");
         */
         {
            return 1;
        }

        p_parent_dir = FileSystemOrSocket.dirname(p_dir);

        if (!equal(p_parent_dir, p_dir))/*
         * ----- Look for parent directory ----- Look for parent directory
         */
         {
            if (!equal(p_parent_dir, "")) {
                if (!equal(v_result = this.privDirCheck(p_parent_dir, false), 1))/*
                 * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
                 * $v_result); --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__,
                 * __LINE__, $v_result);
                 */
                 {
                    return v_result;
                }
            }
        }

        if (!JFileSystemOrSocket.mkdir(gVars.webEnv, p_dir, 777))/*
         * ----- Error log ----- Error log
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_DIR_CREATE_FAIL(), "Unable to create directory \'" + p_dir + "\'");

            return gVars.getSharedPclZip().errorCode();
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privMerge() Function : privMerge() Description : Description :
     * If $p_archive_to_add does not exist, the function exit with a success
     * result. If $p_archive_to_add does not exist, the function exit with a
     * success result. Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privMerge(PclZip p_archive_to_add) {
        int v_result = 0;
        Array<Object> v_central_dir = new Array<Object>();
        Array<Object> v_central_dir_to_add = new Array<Object>();
        String v_zip_temp_name = null;
        int v_zip_temp_fd = 0;
        int v_size = 0;
        int v_read_size = 0;
        String v_buffer = null;
        int v_offset = 0;
        String v_comment = null;
        int v_swap = 0;
        Object v_header_list = null;
        v_result = 1;

        if (!FileSystemOrSocket.is_file(gVars.webEnv, p_archive_to_add.zipname))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive to add does not exist. End of merge.");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive to add does not exist. End of merge."); ----- Nothing to
         * merge, so merge is a success ----- Nothing to merge, so merge is a
         * success
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            v_result = 1;

            return v_result;
        }

        if (!FileSystemOrSocket.is_file(gVars.webEnv, this.zipname))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive does not exist, duplicate the archive_to_add.");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive does not exist, duplicate the archive_to_add."); ----- Do a
         * duplicate ----- Do a duplicate
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            v_result = this.privDuplicate(p_archive_to_add.zipname);

            return v_result;
        }

        if (!equal(v_result = this.privOpenFd("rb"), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        v_central_dir = new Array<Object>();

        if (!equal(v_result = this.privReadEndCentralDir(v_central_dir), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();

            return v_result;
        }

        FileSystemOrSocket.rewind(gVars.webEnv, this.zip_fd);

        if (!equal(v_result = p_archive_to_add.privOpenFd("rb"), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();

            return v_result;
        }

        v_central_dir_to_add = new Array<Object>();

        if (!equal(v_result = p_archive_to_add.privReadEndCentralDir(v_central_dir_to_add), 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();
            p_archive_to_add.privCloseFd();

            return v_result;
        }

        FileSystemOrSocket.rewind(gVars.webEnv, p_archive_to_add.zip_fd);
        v_zip_temp_name = gConsts.getPCLZIP_TEMPORARY_DIR() + Misc.uniqid("pclzip-") + ".tmp";

        if (equal(v_zip_temp_fd = FileSystemOrSocket.fopen(gVars.webEnv, v_zip_temp_name, "wb"), 0))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            this.privCloseFd();
            p_archive_to_add.privCloseFd();
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open temporary file \'" + v_zip_temp_name + "\' in binary write mode");

            return gVars.getSharedPclZip().errorCode();
        }

        v_size = intval(v_central_dir.getValue("offset"));

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, v_zip_temp_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        v_size = intval(v_central_dir_to_add.getValue("offset"));

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, p_archive_to_add.zip_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, v_zip_temp_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        v_offset = FileSystemOrSocket.ftell(gVars.webEnv, v_zip_temp_fd);

        /*, null*/
        v_size = intval(v_central_dir.getValue("size"));

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, this.zip_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, v_zip_temp_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        v_size = intval(v_central_dir_to_add.getValue("size"));

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 4, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, p_archive_to_add.zip_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, v_zip_temp_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        v_comment = strval(v_central_dir.getValue("comment")) + " " + strval(v_central_dir_to_add.getValue("comment"));
        v_size = FileSystemOrSocket.ftell(gVars.webEnv, v_zip_temp_fd) - v_offset;
        v_swap = this.zip_fd;
        this.zip_fd = v_zip_temp_fd;
        v_zip_temp_fd = v_swap;

        if (!equal(v_result = this.privWriteCentralHeader(intval(v_central_dir.getValue("entries")) + intval(v_central_dir_to_add.getValue("entries")), v_size, v_offset, v_comment), 1))/*
         * ----- Reset the file list ----- Reset the file list
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            this.privCloseFd();
            p_archive_to_add.privCloseFd();
            FileSystemOrSocket.fclose(gVars.webEnv, v_zip_temp_fd);
            this.zip_fd = intval(null);
            v_header_list = null;

            return v_result;
        }

        v_swap = this.zip_fd;
        this.zip_fd = v_zip_temp_fd;
        v_zip_temp_fd = v_swap;
        this.privCloseFd();
        p_archive_to_add.privCloseFd();
        FileSystemOrSocket.fclose(gVars.webEnv, v_zip_temp_fd);
        JFileSystemOrSocket.unlink(gVars.webEnv, this.zipname);
        getIncluded(Class_pclzipPage.class, gVars, gConsts).PclZipUtilRename(v_zip_temp_name, this.zipname);

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privDuplicate() Function : privDuplicate() Description :
     * Description : Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privDuplicate(String p_archive_filename) {
        int v_result = 0;
        int v_zip_temp_fd = 0;
        int v_size = 0;
        int v_read_size = 0;
        String v_buffer = null;
        v_result = 1;

        if (!FileSystemOrSocket.is_file(gVars.webEnv, p_archive_filename))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive to duplicate does not exist. End of duplicate.");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Archive to duplicate does not exist. End of duplicate."); -----
         * Nothing to duplicate, so duplicate is a success. ----- Nothing to
         * duplicate, so duplicate is a success.
         */

        /*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            v_result = 1;

            return v_result;
        }

        if (!equal(v_result = this.privOpenFd("wb"), 1))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (equal(v_zip_temp_fd = FileSystemOrSocket.fopen(gVars.webEnv, p_archive_filename, "rb"), 0))/*
         * ----- Return ----- Return
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__,
         * PclZip::errorCode(), PclZip::errorInfo());
         */
         {
            this.privCloseFd();
            gVars.getSharedPclZip().privErrorLog(gConsts.getPCLZIP_ERR_READ_OPEN_FAIL(), "Unable to open archive file \'" + p_archive_filename + "\' in binary write mode");

            return gVars.getSharedPclZip().errorCode();
        }

        v_size = FileSystemOrSocket.filesize(gVars.webEnv, p_archive_filename);

        while (!equal(v_size, 0))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Read
         * $v_read_size bytes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 5, "Read
         * $v_read_size bytes");
         */
         {
            v_read_size = ((v_size < gConsts.getPCLZIP_READ_BLOCK_SIZE())
                ? v_size
                : gConsts.getPCLZIP_READ_BLOCK_SIZE());
            v_buffer = FileSystemOrSocket.fread(gVars.webEnv, v_zip_temp_fd, v_read_size);
            FileSystemOrSocket.fwrite(gVars.webEnv, this.zip_fd, v_buffer, v_read_size);
            v_size = v_size - v_read_size;
        }

        this.privCloseFd();
        FileSystemOrSocket.fclose(gVars.webEnv, v_zip_temp_fd);

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privErrorLog() Function : privErrorLog() Description :
     * Description : Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public void privErrorLog(Object p_error_code, String p_error_string) {
        if (equal(gConsts.getPCLZIP_ERROR_EXTERNAL(), 1)) {
            // Commented by Numiton
            throw new RuntimeException("PCLERROR extension not supported");
        } else//PclError(p_error_code, p_error_string);
         {
            this.error_code = intval(p_error_code);
            this.error_string = p_error_string;
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privErrorReset() Function : privErrorReset() Description :
     * Description : Parameters : Parameters :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public void privErrorReset() {
        if (equal(gConsts.getPCLZIP_ERROR_EXTERNAL(), 1)) {
            // Commented by Numiton
            throw new RuntimeException("PCLERROR extension not supported");
        } else//			PclErrorReset();
         {
            this.error_code = 0;
            this.error_string = "";
        }
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privDecrypt() Function : privDecrypt() Description :
     * Description : Parameters : Parameters : Return Values : Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privDecrypt(Object p_encryption_header, Ref<Object> p_buffer, Object p_size, Object p_crc) {
        int v_result = 0;
        String v_pwd = null;
        v_result = 1;
        v_pwd = "test";

        // Commented by Numiton

        //		p_buffer = PclZipUtilZipDecrypt(p_buffer, p_size, p_encryption_header, p_crc, v_pwd);
        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privDisableMagicQuotes() Function : privDisableMagicQuotes()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privDisableMagicQuotes() {
        int v_result = 0;
        v_result = 1;

        if (!true || /*Modified by Numiton*/
                !true)/*Modified by Numiton*/

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Functions *et_magic_quotes_runtime are not supported");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Functions *et_magic_quotes_runtime are not supported");
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (!equal(this.magic_quotes_status, -1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "magic_quote already disabled");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "magic_quote already disabled");
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        this.magic_quotes_status = Options.get_magic_quotes_runtime(gVars.webEnv);

        if (equal(this.magic_quotes_status, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Disable magic_quotes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Disable magic_quotes");
         */
         {
            Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        }

        return v_result;
    }

    /**
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     * Function : privSwapBackMagicQuotes() Function : privSwapBackMagicQuotes()
     * Description : Description : Parameters : Parameters : Return Values :
     * Return Values :
     * --------------------------------------------------------------------------------
     * --------------------------------------------------------------------------------
     */
    public int privSwapBackMagicQuotes() {
        int v_result = 0;
        v_result = 1;

        if (!true || /*Modified by Numiton*/
                !true)/*Modified by Numiton*/

        /*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Functions *et_magic_quotes_runtime are not supported");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Functions *et_magic_quotes_runtime are not supported");
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (!equal(this.magic_quotes_status, -1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "magic_quote not modified");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "magic_quote not modified");
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         * --(MAGIC-PclTrace)--//PclTraceFctEnd(__FILE__, __LINE__, $v_result);
         */
         {
            return v_result;
        }

        if (equal(this.magic_quotes_status, 1))/*
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Enable back magic_quotes");
         * --(MAGIC-PclTrace)--//PclTraceFctMessage(__FILE__, __LINE__, 3,
         * "Enable back magic_quotes");
         */
         {
            Options.set_magic_quotes_runtime(gVars.webEnv, this.magic_quotes_status);
        }

        return v_result;
    }

    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}
