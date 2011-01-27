/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Moxiecode_Logger.java,v 1.2 2008/10/03 18:45:31 numiton Exp $
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
package org.numiton.nwp.wp_includes.js.tinymce.plugins.spellchecker.classes.utils;

import static com.numiton.VarHandling.*;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.DateTime;
import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


/**
 * Logging utility class. This class handles basic logging with levels, log
 * rotation and custom log formats. It's designed to be compact but still
 * powerful and flexible.
 */
public class Moxiecode_Logger implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Moxiecode_Logger.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

	// Private fields
    public String _path;
    public String _filename;
    public String _maxSize;
    public int _maxFiles;
    public int _maxSizeBytes;
    public Object _level;
    public String _format;

    /**
     * Constructs a new logger instance.
     */
    public Moxiecode_Logger(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this._path = "";
        this._filename = "{level}.log";
        this.setMaxSize("100k");
        this._maxFiles = 10;
        this._level = gConsts.getMC_LOGGER_DEBUG();
        this._format = "[{time}] [{level}] {message}";
    }

    /**
     * Sets the current log level, use the MC_LOGGER constants.
     * @param int $level Log level instance for example MC_LOGGER_DEBUG.
     */
    public void setLevel(Object level) {
        if (is_string(level)) {
            {
                int javaSwitchSelector80 = 0;

                if (equal(Strings.strtolower(strval(level)), "debug")) {
                    javaSwitchSelector80 = 1;
                }

                if (equal(Strings.strtolower(strval(level)), "info")) {
                    javaSwitchSelector80 = 2;
                }

                if (equal(Strings.strtolower(strval(level)), "warn")) {
                    javaSwitchSelector80 = 3;
                }

                if (equal(Strings.strtolower(strval(level)), "warning")) {
                    javaSwitchSelector80 = 4;
                }

                if (equal(Strings.strtolower(strval(level)), "error")) {
                    javaSwitchSelector80 = 5;
                }

                if (equal(Strings.strtolower(strval(level)), "fatal")) {
                    javaSwitchSelector80 = 6;
                }

                switch (javaSwitchSelector80) {
                case 1: {
                    level = gConsts.getMC_LOGGER_DEBUG();

                    break;
                }

                case 2: {
                    level = gConsts.getMC_LOGGER_INFO();

                    break;
                }

                case 3: {
                }

                case 4: {
                    level = gConsts.getMC_LOGGER_WARN();

                    break;
                }

                case 5: {
                    level = gConsts.getMC_LOGGER_ERROR();

                    break;
                }

                case 6: {
                    level = gConsts.getMC_LOGGER_FATAL();

                    break;
                }

                default:
                    level = gConsts.getMC_LOGGER_FATAL();
                }
            }
        }

        this._level = level;
    }

    /**
     * Returns the current log level for example MC_LOGGER_DEBUG.
     * @return int Current log level for example MC_LOGGER_DEBUG.
     */
    public Object getLevel() {
        return this._level;
    }

    public void setPath(String path) {
        this._path = path;
    }

    public String getPath() {
        return this._path;
    }

    public void setFileName(Object file_name) {
        this._filename = strval(file_name);
    }

    public String getFileName() {
        return this._filename;
    }

    public void setFormat(String format) {
        this._format = format;
    }

    public String getFormat() {
        return this._format;
    }

    public void setMaxSize(String size) {
        int logMaxSizeBytes = 0;
        
		// Fix log max size
        logMaxSizeBytes = intval(QRegExPerl.preg_replace("/[^0-9]/", "", size));

		// Is KB
        if (Strings.strpos(Strings.strtolower(size), "k") > 0) {
            logMaxSizeBytes = logMaxSizeBytes * 1024;
        }

		// Is MB
        if (Strings.strpos(Strings.strtolower(size), "m") > 0) {
            logMaxSizeBytes = logMaxSizeBytes * 1024 * 1024;
        }

        this._maxSizeBytes = logMaxSizeBytes;
        this._maxSize = size;
    }

    public String getMaxSize() {
        return this._maxSize;
    }

    public void setMaxFiles(String max_files) {
        this._maxFiles = intval(max_files);
    }

    public int getMaxFiles() {
        return this._maxFiles;
    }

    public void debug(String msg, Object... vargs) {
        Array<String> args = new Array<String>();

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        this._logMsg(gConsts.getMC_LOGGER_DEBUG(), Strings.implode(", ", args));
    }

    public void info(String msg, Object... vargs) {
        Array<String> args = new Array<String>();

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        this._logMsg(gConsts.getMC_LOGGER_INFO(), Strings.implode(", ", args));
    }

    public void warn(String msg, Object... vargs) {
        Array<String> args = new Array<String>();

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        this._logMsg(gConsts.getMC_LOGGER_WARN(), Strings.implode(", ", args));
    }

    public void error(String msg, Object... vargs) {
        Array<String> args = new Array<String>();

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        this._logMsg(gConsts.getMC_LOGGER_ERROR(), Strings.implode(", ", args));
    }

    public void fatal(String msg, Object... vargs) {
        Array<String> args = new Array<String>();

        // Modified by Numiton
        Object[] totalArgs = FunctionHandling.buildTotalArgs(msg, vargs);
        args = FunctionHandling.func_get_args(totalArgs);
        this._logMsg(gConsts.getMC_LOGGER_FATAL(), Strings.implode(", ", args));
    }

    public boolean isDebugEnabled() {
        return intval(this._level) >= gConsts.getMC_LOGGER_DEBUG();
    }

    public boolean isInfoEnabled() {
        return intval(this._level) >= gConsts.getMC_LOGGER_INFO();
    }

    public boolean isWarnEnabled() {
        return intval(this._level) >= gConsts.getMC_LOGGER_WARN();
    }

    public boolean isErrorEnabled() {
        return intval(this._level) >= gConsts.getMC_LOGGER_ERROR();
    }

    public boolean isFatalEnabled() {
        return intval(this._level) >= gConsts.getMC_LOGGER_FATAL();
    }

    public void _logMsg(Object level, String message) {
        boolean roll = false;
        String logFile;
        String levelName = null;
        String text = null;
        int size = 0;
        String rfile;
        int i = 0;
        String nfile;
        String delfile;
        int fp = 0;
        roll = false;

        if (intval(level) < intval(this._level)) {
            return;
        }

        logFile = this.toOSPath(this._path + "/" + this._filename);

        {
            int javaSwitchSelector81 = 0;

            if (equal(level, gConsts.getMC_LOGGER_DEBUG())) {
                javaSwitchSelector81 = 1;
            }

            if (equal(level, gConsts.getMC_LOGGER_INFO())) {
                javaSwitchSelector81 = 2;
            }

            if (equal(level, gConsts.getMC_LOGGER_WARN())) {
                javaSwitchSelector81 = 3;
            }

            if (equal(level, gConsts.getMC_LOGGER_ERROR())) {
                javaSwitchSelector81 = 4;
            }

            if (equal(level, gConsts.getMC_LOGGER_FATAL())) {
                javaSwitchSelector81 = 5;
            }

            switch (javaSwitchSelector81) {
            case 1: {
                levelName = "DEBUG";

                break;
            }

            case 2: {
                levelName = "INFO";

                break;
            }

            case 3: {
                levelName = "WARN";

                break;
            }

            case 4: {
                levelName = "ERROR";

                break;
            }

            case 5: {
                levelName = "FATAL";

                break;
            }
            }
        }

        logFile = Strings.str_replace("{level}", Strings.strtolower(levelName), logFile);
        text = this._format;
        text = Strings.str_replace("{time}", DateTime.date("Y-m-d H:i:s"), text);
        text = Strings.str_replace("{level}", Strings.strtolower(levelName), text);
        text = Strings.str_replace("{message}", message, text);
        message = text + "\r\n";

		// Check filesize
        if (FileSystemOrSocket.file_exists(gVars.webEnv, logFile)) {
            size = FileSystemOrSocket.filesize(gVars.webEnv, logFile);

            if ((size + Strings.strlen(message)) > this._maxSizeBytes) {
                roll = true;
            }
        }

		// Roll if the size is right
        if (roll) {
            for (i = this._maxFiles - 1; i >= 1; i--) {
                rfile = this.toOSPath(logFile + "." + i);
                nfile = this.toOSPath(logFile + "." + strval(i + 1));

                if (FileSystemOrSocket.file_exists(gVars.webEnv, rfile)) {
                    FileSystemOrSocket.rename(gVars.webEnv, rfile, nfile);
                }
            }

            FileSystemOrSocket.rename(gVars.webEnv, logFile, this.toOSPath(logFile + ".1"));
            
			// Delete last logfile
            delfile = this.toOSPath(logFile + "." + strval(this._maxFiles + 1));

            if (FileSystemOrSocket.file_exists(gVars.webEnv, delfile)) {
                JFileSystemOrSocket.unlink(gVars.webEnv, delfile);
            }
        }
        
		// Append log line
        if (!equal(fp = FileSystemOrSocket.fopen(gVars.webEnv, logFile, "a"), null)) {
            FileSystemOrSocket.fputs(gVars.webEnv, fp, message);
            FileSystemOrSocket.fflush(gVars.webEnv, fp);
            FileSystemOrSocket.fclose(gVars.webEnv, fp);
        }
    }

    /**
     * Converts a Unix path to OS specific path.
     * @param String $path Unix path to convert.
     */
    public String toOSPath(String path) {
        return Strings.str_replace("/", Directories.DIRECTORY_SEPARATOR, path);
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
