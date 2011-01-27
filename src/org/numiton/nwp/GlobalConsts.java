/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: GlobalConsts.java,v 1.1 2008/09/19 09:44:34 numiton Exp $
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
package org.numiton.nwp;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.numiton.generic.GlobalConstantsInterface;

public class GlobalConsts implements GlobalConstantsInterface {
	protected static final Logger	LOG	    = Logger.getLogger(GlobalConsts.class.getName());
	public Set<String>	              constDefs	= new HashSet<String>();
	public boolean	              WP_USE_THEMES;
	public boolean	              DOING_AJAX;
	public boolean	              WP_ADMIN;
	public String	              ABSPATH;
	public boolean	              WP_IMPORTING;
	public Object	              NO_HEADER_TEXT;
	public int	              MAX_RESULTS;
	public int	              MAX_EXECUTION_TIME;
	public int	              STATUS_INTERVAL;
	public String	              CRLF;
	public int	                  FTP_AUTOASCII;
	public int   	              FTP_BINARY;
	public int	                  FTP_ASCII;
	public boolean	              FTP_FORCE;
	public String	              FTP_OS_Unix;
	public String	              FTP_OS_Windows;
	public String	              FTP_OS_Mac;
	public int	              PCLZIP_READ_BLOCK_SIZE;
	public String	              PCLZIP_SEPARATOR;
	public int	              PCLZIP_ERROR_EXTERNAL;
	public String	              PCLZIP_TEMPORARY_DIR;
	public int	              PCLZIP_ERR_USER_ABORTED;
	public int	              PCLZIP_ERR_NO_ERROR;
	public int	              PCLZIP_ERR_WRITE_OPEN_FAIL;
	public int	              PCLZIP_ERR_READ_OPEN_FAIL;
	public int	              PCLZIP_ERR_INVALID_PARAMETER;
	public int	              PCLZIP_ERR_MISSING_FILE;
	public int	              PCLZIP_ERR_FILENAME_TOO_LONG;
	public int	              PCLZIP_ERR_INVALID_ZIP;
	public int	              PCLZIP_ERR_BAD_EXTRACTED_FILE;
	public int	              PCLZIP_ERR_DIR_CREATE_FAIL;
	public int	              PCLZIP_ERR_BAD_EXTENSION;
	public int	              PCLZIP_ERR_BAD_FORMAT;
	public int	              PCLZIP_ERR_DELETE_FILE_FAIL;
	public int	              PCLZIP_ERR_RENAME_FILE_FAIL;
	public int	              PCLZIP_ERR_BAD_CHECKSUM;
	public int	              PCLZIP_ERR_INVALID_ARCHIVE_ZIP;
	public int	              PCLZIP_ERR_MISSING_OPTION_VALUE;
	public int	              PCLZIP_ERR_INVALID_OPTION_VALUE;
	public int	              PCLZIP_ERR_ALREADY_A_DIRECTORY;
	public int	              PCLZIP_ERR_UNSUPPORTED_COMPRESSION;
	public int	              PCLZIP_ERR_UNSUPPORTED_ENCRYPTION;
	public int	              PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE;
	public int	              PCLZIP_ERR_DIRECTORY_RESTRICTION;
	public int	              PCLZIP_OPT_PATH;
	public int	              PCLZIP_OPT_ADD_PATH;
	public int	              PCLZIP_OPT_REMOVE_PATH;
	public int	              PCLZIP_OPT_REMOVE_ALL_PATH;
	public int	              PCLZIP_OPT_SET_CHMOD;
	public int	              PCLZIP_OPT_EXTRACT_AS_STRING;
	public int	              PCLZIP_OPT_NO_COMPRESSION;
	public int	              PCLZIP_OPT_BY_NAME;
	public int	              PCLZIP_OPT_BY_INDEX;
	public int	              PCLZIP_OPT_BY_EREG;
	public int	              PCLZIP_OPT_BY_PREG;
	public int	              PCLZIP_OPT_COMMENT;
	public int	              PCLZIP_OPT_ADD_COMMENT;
	public int	              PCLZIP_OPT_PREPEND_COMMENT;
	public int	              PCLZIP_OPT_EXTRACT_IN_OUTPUT;
	public int	              PCLZIP_OPT_REPLACE_NEWER;
	public int	              PCLZIP_OPT_STOP_ON_ERROR;
	public int	              PCLZIP_OPT_EXTRACT_DIR_RESTRICTION;
	public int	              PCLZIP_ATT_FILE_NAME;
	public int	              PCLZIP_ATT_FILE_NEW_SHORT_NAME;
	public int	              PCLZIP_ATT_FILE_NEW_FULL_NAME;
	public int	              PCLZIP_CB_PRE_EXTRACT;
	public int	              PCLZIP_CB_POST_EXTRACT;
	public int	              PCLZIP_CB_PRE_ADD;
	public int	              PCLZIP_CB_POST_ADD;
	public String	              FTP_BASE;
	public String	              WXR_VERSION;
	public String	              WP_TEMP_DIR;
	public boolean	              WP_INSTALLING;
	public String	              WP_SITEURL;
	public Object	              RESET_CAPS;
	public String	              WP_HOME;
	public boolean	              IS_PROFILE_PAGE;
	public String	              WPINC;
	public String	              DB_NAME;
	public String	              DB_USER;
	public String	              DB_PASSWORD;
	public String	              DB_HOST;
	public String	              FTP_HOST;
	public String	              FTP_USER;
	public String	              FTP_PASS;
	public Object	              FTP_SSL;
	public boolean	              APP_REQUEST;
	public String	              DB_CHARSET;
	public String	              DB_COLLATE;
	public String	              SECRET_KEY;
	public String	              WPLANG;
	public boolean	              DOING_CRON;
	public boolean	              COMMENTS_TEMPLATE;
	public String	              UPLOADS;
	public boolean	              WP_DEBUG;
	public int	              JSON_BOOL;
	public int	              JSON_INT;
	public int	              JSON_STR;
	public int	              JSON_FLOAT;
	public int	              JSON_NULL;
	public int	              JSON_START_OBJ;
	public int	              JSON_END_OBJ;
	public int	              JSON_START_ARRAY;
	public int	              JSON_END_ARRAY;
	public int	              JSON_KEY;
	public int	              JSON_SKIP;
	public int	              JSON_IN_ARRAY;
	public int	              JSON_IN_OBJECT;
	public int	              JSON_IN_BETWEEN;
	public int	              MC_LOGGER_DEBUG;
	public int	              MC_LOGGER_INFO;
	public int	              MC_LOGGER_WARN;
	public int	              MC_LOGGER_ERROR;
	public int	              MC_LOGGER_FATAL;
	public boolean	              CUSTOM_TAGS;
	public boolean	              XMLRPC_REQUEST;
	public String	              SECRET_SALT;
	public int	              EP_PERMALINK;
	public int	              EP_ATTACHMENT;
	public int	              EP_DATE;
	public int	              EP_YEAR;
	public int	              EP_MONTH;
	public int	              EP_DAY;
	public int	              EP_ROOT;
	public int	              EP_COMMENTS;
	public int	              EP_SEARCH;
	public int	              EP_CATEGORIES;
	public int	              EP_TAGS;
	public int	              EP_AUTHORS;
	public int	              EP_PAGES;
	public int	          EP_NONE;
	public int	              EP_ALL;
	public int	              MAGPIE_INITALIZED;
	public int	              MAGPIE_CACHE_ON;
	public String	              MAGPIE_CACHE_DIR;
	public int	              MAGPIE_CACHE_AGE;
	public int	              MAGPIE_CACHE_FRESH_ONLY;
	public int	              MAGPIE_DEBUG;
	public String	              MAGPIE_USER_AGENT;
	public int	              MAGPIE_FETCH_TIME_OUT;
	public boolean	              MAGPIE_USE_GZIP;
	public String	              RSS;
	public String	              ATOM;
	public String	              EZSQL_VERSION;
	public String	              OBJECT;
	public String	              OBJECT_K;
	public String	              ARRAY_A;
	public String	              ARRAY_N;
	public boolean	              SAVEQUERIES;
	public String	              CUSTOM_USER_TABLE;
	public String	              CUSTOM_USER_META_TABLE;
	public boolean	              RELOCATE;
	public String	              WP_MEMORY_LIMIT;
	public boolean	              WP_CACHE;
	public String	              LANGDIR;
	public String	              PLUGINDIR;
	public String	              COOKIEHASH;
	public String	              USER_COOKIE;
	public String	              PASS_COOKIE;
	public String	              AUTH_COOKIE;
	public String	              TEST_COOKIE;
	public String	              COOKIEPATH;
	public String	              SITECOOKIEPATH;
	public String	              COOKIE_DOMAIN;
	public int	              AUTOSAVE_INTERVAL;
	public String	              TEMPLATEPATH;
	public String	              STYLESHEETPATH;

	public GlobalConsts() {
	}

	public void setWP_USE_THEMES(boolean initialValue) {
		if (!constDefs.contains("WP_USE_THEMES")) {
			WP_USE_THEMES = initialValue;
			constDefs.add("WP_USE_THEMES");
		}
	}

	public boolean getWP_USE_THEMES() {
		return WP_USE_THEMES;
	}

	public boolean isWP_USE_THEMESDefined() {
		return constDefs.contains("WP_USE_THEMES");
	}

	public void setDOING_AJAX(boolean initialValue) {
		if (!constDefs.contains("DOING_AJAX")) {
			DOING_AJAX = initialValue;
			constDefs.add("DOING_AJAX");
		}
	}

	public boolean getDOING_AJAX() {
		return DOING_AJAX;
	}

	public boolean isDOING_AJAXDefined() {
		return constDefs.contains("DOING_AJAX");
	}

	public void setWP_ADMIN(boolean initialValue) {
		if (!constDefs.contains("WP_ADMIN")) {
			WP_ADMIN = initialValue;
			constDefs.add("WP_ADMIN");
		}
	}

	public boolean getWP_ADMIN() {
		return WP_ADMIN;
	}

	public boolean isWP_ADMINDefined() {
		return constDefs.contains("WP_ADMIN");
	}

	public void setABSPATH(String initialValue) {
		if (!constDefs.contains("ABSPATH")) {
			ABSPATH = initialValue;
			constDefs.add("ABSPATH");
		}
	}

	public String getABSPATH() {
		return ABSPATH;
	}

	public boolean isABSPATHDefined() {
		return constDefs.contains("ABSPATH");
	}

	public void setWP_IMPORTING(boolean initialValue) {
		if (!constDefs.contains("WP_IMPORTING")) {
			WP_IMPORTING = initialValue;
			constDefs.add("WP_IMPORTING");
		}
	}

	public boolean getWP_IMPORTING() {
		return WP_IMPORTING;
	}

	public boolean isWP_IMPORTINGDefined() {
		return constDefs.contains("WP_IMPORTING");
	}

	public void setNO_HEADER_TEXT(Object initialValue) {
		if (!constDefs.contains("NO_HEADER_TEXT")) {
			NO_HEADER_TEXT = initialValue;
			constDefs.add("NO_HEADER_TEXT");
		}
	}

	public Object getNO_HEADER_TEXT() {
		return NO_HEADER_TEXT;
	}

	public boolean isNO_HEADER_TEXTDefined() {
		return constDefs.contains("NO_HEADER_TEXT");
	}

	public void setMAX_RESULTS(int initialValue) {
		if (!constDefs.contains("MAX_RESULTS")) {
			MAX_RESULTS = initialValue;
			constDefs.add("MAX_RESULTS");
		}
	}

	public int getMAX_RESULTS() {
		return MAX_RESULTS;
	}

	public boolean isMAX_RESULTSDefined() {
		return constDefs.contains("MAX_RESULTS");
	}

	public void setMAX_EXECUTION_TIME(int initialValue) {
		if (!constDefs.contains("MAX_EXECUTION_TIME")) {
			MAX_EXECUTION_TIME = initialValue;
			constDefs.add("MAX_EXECUTION_TIME");
		}
	}

	public int getMAX_EXECUTION_TIME() {
		return MAX_EXECUTION_TIME;
	}

	public boolean isMAX_EXECUTION_TIMEDefined() {
		return constDefs.contains("MAX_EXECUTION_TIME");
	}

	public void setSTATUS_INTERVAL(int initialValue) {
		if (!constDefs.contains("STATUS_INTERVAL")) {
			STATUS_INTERVAL = initialValue;
			constDefs.add("STATUS_INTERVAL");
		}
	}

	public int getSTATUS_INTERVAL() {
		return STATUS_INTERVAL;
	}

	public boolean isSTATUS_INTERVALDefined() {
		return constDefs.contains("STATUS_INTERVAL");
	}

	public void setCRLF(String initialValue) {
		if (!constDefs.contains("CRLF")) {
			CRLF = initialValue;
			constDefs.add("CRLF");
		}
	}

	public String getCRLF() {
		return CRLF;
	}

	public boolean isCRLFDefined() {
		return constDefs.contains("CRLF");
	}

	public void setFTP_AUTOASCII(int initialValue) {
		if (!constDefs.contains("FTP_AUTOASCII")) {
			FTP_AUTOASCII = initialValue;
			constDefs.add("FTP_AUTOASCII");
		}
	}

	public int getFTP_AUTOASCII() {
		return FTP_AUTOASCII;
	}

	public boolean isFTP_AUTOASCIIDefined() {
		return constDefs.contains("FTP_AUTOASCII");
	}

	public void setFTP_BINARY(int initialValue) {
		if (!constDefs.contains("FTP_BINARY")) {
			FTP_BINARY = initialValue;
			constDefs.add("FTP_BINARY");
		}
	}

	public int getFTP_BINARY() {
		return FTP_BINARY;
	}

	public boolean isFTP_BINARYDefined() {
		return constDefs.contains("FTP_BINARY");
	}

	public void setFTP_ASCII(int initialValue) {
		if (!constDefs.contains("FTP_ASCII")) {
			FTP_ASCII = initialValue;
			constDefs.add("FTP_ASCII");
		}
	}

	public int getFTP_ASCII() {
		return FTP_ASCII;
	}

	public boolean isFTP_ASCIIDefined() {
		return constDefs.contains("FTP_ASCII");
	}

	public void setFTP_FORCE(boolean initialValue) {
		if (!constDefs.contains("FTP_FORCE")) {
			FTP_FORCE = initialValue;
			constDefs.add("FTP_FORCE");
		}
	}

	public boolean getFTP_FORCE() {
		return FTP_FORCE;
	}

	public boolean isFTP_FORCEDefined() {
		return constDefs.contains("FTP_FORCE");
	}

	public void setFTP_OS_Unix(String initialValue) {
		if (!constDefs.contains("FTP_OS_Unix")) {
			FTP_OS_Unix = initialValue;
			constDefs.add("FTP_OS_Unix");
		}
	}

	public String getFTP_OS_Unix() {
		return FTP_OS_Unix;
	}

	public boolean isFTP_OS_UnixDefined() {
		return constDefs.contains("FTP_OS_Unix");
	}

	public void setFTP_OS_Windows(String initialValue) {
		if (!constDefs.contains("FTP_OS_Windows")) {
			FTP_OS_Windows = initialValue;
			constDefs.add("FTP_OS_Windows");
		}
	}

	public String getFTP_OS_Windows() {
		return FTP_OS_Windows;
	}

	public boolean isFTP_OS_WindowsDefined() {
		return constDefs.contains("FTP_OS_Windows");
	}

	public void setFTP_OS_Mac(String initialValue) {
		if (!constDefs.contains("FTP_OS_Mac")) {
			FTP_OS_Mac = initialValue;
			constDefs.add("FTP_OS_Mac");
		}
	}

	public String getFTP_OS_Mac() {
		return FTP_OS_Mac;
	}

	public boolean isFTP_OS_MacDefined() {
		return constDefs.contains("FTP_OS_Mac");
	}

	public void setPCLZIP_READ_BLOCK_SIZE(int initialValue) {
		if (!constDefs.contains("PCLZIP_READ_BLOCK_SIZE")) {
			PCLZIP_READ_BLOCK_SIZE = initialValue;
			constDefs.add("PCLZIP_READ_BLOCK_SIZE");
		}
	}

	public int getPCLZIP_READ_BLOCK_SIZE() {
		return PCLZIP_READ_BLOCK_SIZE;
	}

	public boolean isPCLZIP_READ_BLOCK_SIZEDefined() {
		return constDefs.contains("PCLZIP_READ_BLOCK_SIZE");
	}

	public void setPCLZIP_SEPARATOR(String initialValue) {
		if (!constDefs.contains("PCLZIP_SEPARATOR")) {
			PCLZIP_SEPARATOR = initialValue;
			constDefs.add("PCLZIP_SEPARATOR");
		}
	}

	public String getPCLZIP_SEPARATOR() {
		return PCLZIP_SEPARATOR;
	}

	public boolean isPCLZIP_SEPARATORDefined() {
		return constDefs.contains("PCLZIP_SEPARATOR");
	}

	public void setPCLZIP_ERROR_EXTERNAL(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERROR_EXTERNAL")) {
			PCLZIP_ERROR_EXTERNAL = initialValue;
			constDefs.add("PCLZIP_ERROR_EXTERNAL");
		}
	}

	public int getPCLZIP_ERROR_EXTERNAL() {
		return PCLZIP_ERROR_EXTERNAL;
	}

	public boolean isPCLZIP_ERROR_EXTERNALDefined() {
		return constDefs.contains("PCLZIP_ERROR_EXTERNAL");
	}

	public void setPCLZIP_TEMPORARY_DIR(String initialValue) {
		if (!constDefs.contains("PCLZIP_TEMPORARY_DIR")) {
			PCLZIP_TEMPORARY_DIR = initialValue;
			constDefs.add("PCLZIP_TEMPORARY_DIR");
		}
	}

	public String getPCLZIP_TEMPORARY_DIR() {
		return PCLZIP_TEMPORARY_DIR;
	}

	public boolean isPCLZIP_TEMPORARY_DIRDefined() {
		return constDefs.contains("PCLZIP_TEMPORARY_DIR");
	}

	public void setPCLZIP_ERR_USER_ABORTED(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_USER_ABORTED")) {
			PCLZIP_ERR_USER_ABORTED = initialValue;
			constDefs.add("PCLZIP_ERR_USER_ABORTED");
		}
	}

	public int getPCLZIP_ERR_USER_ABORTED() {
		return PCLZIP_ERR_USER_ABORTED;
	}

	public boolean isPCLZIP_ERR_USER_ABORTEDDefined() {
		return constDefs.contains("PCLZIP_ERR_USER_ABORTED");
	}

	public void setPCLZIP_ERR_NO_ERROR(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_NO_ERROR")) {
			PCLZIP_ERR_NO_ERROR = initialValue;
			constDefs.add("PCLZIP_ERR_NO_ERROR");
		}
	}

	public int getPCLZIP_ERR_NO_ERROR() {
		return PCLZIP_ERR_NO_ERROR;
	}

	public boolean isPCLZIP_ERR_NO_ERRORDefined() {
		return constDefs.contains("PCLZIP_ERR_NO_ERROR");
	}

	public void setPCLZIP_ERR_WRITE_OPEN_FAIL(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_WRITE_OPEN_FAIL")) {
			PCLZIP_ERR_WRITE_OPEN_FAIL = initialValue;
			constDefs.add("PCLZIP_ERR_WRITE_OPEN_FAIL");
		}
	}

	public int getPCLZIP_ERR_WRITE_OPEN_FAIL() {
		return PCLZIP_ERR_WRITE_OPEN_FAIL;
	}

	public boolean isPCLZIP_ERR_WRITE_OPEN_FAILDefined() {
		return constDefs.contains("PCLZIP_ERR_WRITE_OPEN_FAIL");
	}

	public void setPCLZIP_ERR_READ_OPEN_FAIL(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_READ_OPEN_FAIL")) {
			PCLZIP_ERR_READ_OPEN_FAIL = initialValue;
			constDefs.add("PCLZIP_ERR_READ_OPEN_FAIL");
		}
	}

	public int getPCLZIP_ERR_READ_OPEN_FAIL() {
		return PCLZIP_ERR_READ_OPEN_FAIL;
	}

	public boolean isPCLZIP_ERR_READ_OPEN_FAILDefined() {
		return constDefs.contains("PCLZIP_ERR_READ_OPEN_FAIL");
	}

	public void setPCLZIP_ERR_INVALID_PARAMETER(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_INVALID_PARAMETER")) {
			PCLZIP_ERR_INVALID_PARAMETER = initialValue;
			constDefs.add("PCLZIP_ERR_INVALID_PARAMETER");
		}
	}

	public int getPCLZIP_ERR_INVALID_PARAMETER() {
		return PCLZIP_ERR_INVALID_PARAMETER;
	}

	public boolean isPCLZIP_ERR_INVALID_PARAMETERDefined() {
		return constDefs.contains("PCLZIP_ERR_INVALID_PARAMETER");
	}

	public void setPCLZIP_ERR_MISSING_FILE(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_MISSING_FILE")) {
			PCLZIP_ERR_MISSING_FILE = initialValue;
			constDefs.add("PCLZIP_ERR_MISSING_FILE");
		}
	}

	public int getPCLZIP_ERR_MISSING_FILE() {
		return PCLZIP_ERR_MISSING_FILE;
	}

	public boolean isPCLZIP_ERR_MISSING_FILEDefined() {
		return constDefs.contains("PCLZIP_ERR_MISSING_FILE");
	}

	public void setPCLZIP_ERR_FILENAME_TOO_LONG(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_FILENAME_TOO_LONG")) {
			PCLZIP_ERR_FILENAME_TOO_LONG = initialValue;
			constDefs.add("PCLZIP_ERR_FILENAME_TOO_LONG");
		}
	}

	public int getPCLZIP_ERR_FILENAME_TOO_LONG() {
		return PCLZIP_ERR_FILENAME_TOO_LONG;
	}

	public boolean isPCLZIP_ERR_FILENAME_TOO_LONGDefined() {
		return constDefs.contains("PCLZIP_ERR_FILENAME_TOO_LONG");
	}

	public void setPCLZIP_ERR_INVALID_ZIP(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_INVALID_ZIP")) {
			PCLZIP_ERR_INVALID_ZIP = initialValue;
			constDefs.add("PCLZIP_ERR_INVALID_ZIP");
		}
	}

	public int getPCLZIP_ERR_INVALID_ZIP() {
		return PCLZIP_ERR_INVALID_ZIP;
	}

	public boolean isPCLZIP_ERR_INVALID_ZIPDefined() {
		return constDefs.contains("PCLZIP_ERR_INVALID_ZIP");
	}

	public void setPCLZIP_ERR_BAD_EXTRACTED_FILE(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_BAD_EXTRACTED_FILE")) {
			PCLZIP_ERR_BAD_EXTRACTED_FILE = initialValue;
			constDefs.add("PCLZIP_ERR_BAD_EXTRACTED_FILE");
		}
	}

	public int getPCLZIP_ERR_BAD_EXTRACTED_FILE() {
		return PCLZIP_ERR_BAD_EXTRACTED_FILE;
	}

	public boolean isPCLZIP_ERR_BAD_EXTRACTED_FILEDefined() {
		return constDefs.contains("PCLZIP_ERR_BAD_EXTRACTED_FILE");
	}

	public void setPCLZIP_ERR_DIR_CREATE_FAIL(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_DIR_CREATE_FAIL")) {
			PCLZIP_ERR_DIR_CREATE_FAIL = initialValue;
			constDefs.add("PCLZIP_ERR_DIR_CREATE_FAIL");
		}
	}

	public int getPCLZIP_ERR_DIR_CREATE_FAIL() {
		return PCLZIP_ERR_DIR_CREATE_FAIL;
	}

	public boolean isPCLZIP_ERR_DIR_CREATE_FAILDefined() {
		return constDefs.contains("PCLZIP_ERR_DIR_CREATE_FAIL");
	}

	public void setPCLZIP_ERR_BAD_EXTENSION(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_BAD_EXTENSION")) {
			PCLZIP_ERR_BAD_EXTENSION = initialValue;
			constDefs.add("PCLZIP_ERR_BAD_EXTENSION");
		}
	}

	public int getPCLZIP_ERR_BAD_EXTENSION() {
		return PCLZIP_ERR_BAD_EXTENSION;
	}

	public boolean isPCLZIP_ERR_BAD_EXTENSIONDefined() {
		return constDefs.contains("PCLZIP_ERR_BAD_EXTENSION");
	}

	public void setPCLZIP_ERR_BAD_FORMAT(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_BAD_FORMAT")) {
			PCLZIP_ERR_BAD_FORMAT = initialValue;
			constDefs.add("PCLZIP_ERR_BAD_FORMAT");
		}
	}

	public int getPCLZIP_ERR_BAD_FORMAT() {
		return PCLZIP_ERR_BAD_FORMAT;
	}

	public boolean isPCLZIP_ERR_BAD_FORMATDefined() {
		return constDefs.contains("PCLZIP_ERR_BAD_FORMAT");
	}

	public void setPCLZIP_ERR_DELETE_FILE_FAIL(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_DELETE_FILE_FAIL")) {
			PCLZIP_ERR_DELETE_FILE_FAIL = initialValue;
			constDefs.add("PCLZIP_ERR_DELETE_FILE_FAIL");
		}
	}

	public int getPCLZIP_ERR_DELETE_FILE_FAIL() {
		return PCLZIP_ERR_DELETE_FILE_FAIL;
	}

	public boolean isPCLZIP_ERR_DELETE_FILE_FAILDefined() {
		return constDefs.contains("PCLZIP_ERR_DELETE_FILE_FAIL");
	}

	public void setPCLZIP_ERR_RENAME_FILE_FAIL(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_RENAME_FILE_FAIL")) {
			PCLZIP_ERR_RENAME_FILE_FAIL = initialValue;
			constDefs.add("PCLZIP_ERR_RENAME_FILE_FAIL");
		}
	}

	public int getPCLZIP_ERR_RENAME_FILE_FAIL() {
		return PCLZIP_ERR_RENAME_FILE_FAIL;
	}

	public boolean isPCLZIP_ERR_RENAME_FILE_FAILDefined() {
		return constDefs.contains("PCLZIP_ERR_RENAME_FILE_FAIL");
	}

	public void setPCLZIP_ERR_BAD_CHECKSUM(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_BAD_CHECKSUM")) {
			PCLZIP_ERR_BAD_CHECKSUM = initialValue;
			constDefs.add("PCLZIP_ERR_BAD_CHECKSUM");
		}
	}

	public int getPCLZIP_ERR_BAD_CHECKSUM() {
		return PCLZIP_ERR_BAD_CHECKSUM;
	}

	public boolean isPCLZIP_ERR_BAD_CHECKSUMDefined() {
		return constDefs.contains("PCLZIP_ERR_BAD_CHECKSUM");
	}

	public void setPCLZIP_ERR_INVALID_ARCHIVE_ZIP(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_INVALID_ARCHIVE_ZIP")) {
			PCLZIP_ERR_INVALID_ARCHIVE_ZIP = initialValue;
			constDefs.add("PCLZIP_ERR_INVALID_ARCHIVE_ZIP");
		}
	}

	public int getPCLZIP_ERR_INVALID_ARCHIVE_ZIP() {
		return PCLZIP_ERR_INVALID_ARCHIVE_ZIP;
	}

	public boolean isPCLZIP_ERR_INVALID_ARCHIVE_ZIPDefined() {
		return constDefs.contains("PCLZIP_ERR_INVALID_ARCHIVE_ZIP");
	}

	public void setPCLZIP_ERR_MISSING_OPTION_VALUE(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_MISSING_OPTION_VALUE")) {
			PCLZIP_ERR_MISSING_OPTION_VALUE = initialValue;
			constDefs.add("PCLZIP_ERR_MISSING_OPTION_VALUE");
		}
	}

	public int getPCLZIP_ERR_MISSING_OPTION_VALUE() {
		return PCLZIP_ERR_MISSING_OPTION_VALUE;
	}

	public boolean isPCLZIP_ERR_MISSING_OPTION_VALUEDefined() {
		return constDefs.contains("PCLZIP_ERR_MISSING_OPTION_VALUE");
	}

	public void setPCLZIP_ERR_INVALID_OPTION_VALUE(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_INVALID_OPTION_VALUE")) {
			PCLZIP_ERR_INVALID_OPTION_VALUE = initialValue;
			constDefs.add("PCLZIP_ERR_INVALID_OPTION_VALUE");
		}
	}

	public int getPCLZIP_ERR_INVALID_OPTION_VALUE() {
		return PCLZIP_ERR_INVALID_OPTION_VALUE;
	}

	public boolean isPCLZIP_ERR_INVALID_OPTION_VALUEDefined() {
		return constDefs.contains("PCLZIP_ERR_INVALID_OPTION_VALUE");
	}

	public void setPCLZIP_ERR_ALREADY_A_DIRECTORY(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_ALREADY_A_DIRECTORY")) {
			PCLZIP_ERR_ALREADY_A_DIRECTORY = initialValue;
			constDefs.add("PCLZIP_ERR_ALREADY_A_DIRECTORY");
		}
	}

	public int getPCLZIP_ERR_ALREADY_A_DIRECTORY() {
		return PCLZIP_ERR_ALREADY_A_DIRECTORY;
	}

	public boolean isPCLZIP_ERR_ALREADY_A_DIRECTORYDefined() {
		return constDefs.contains("PCLZIP_ERR_ALREADY_A_DIRECTORY");
	}

	public void setPCLZIP_ERR_UNSUPPORTED_COMPRESSION(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_UNSUPPORTED_COMPRESSION")) {
			PCLZIP_ERR_UNSUPPORTED_COMPRESSION = initialValue;
			constDefs.add("PCLZIP_ERR_UNSUPPORTED_COMPRESSION");
		}
	}

	public int getPCLZIP_ERR_UNSUPPORTED_COMPRESSION() {
		return PCLZIP_ERR_UNSUPPORTED_COMPRESSION;
	}

	public boolean isPCLZIP_ERR_UNSUPPORTED_COMPRESSIONDefined() {
		return constDefs.contains("PCLZIP_ERR_UNSUPPORTED_COMPRESSION");
	}

	public void setPCLZIP_ERR_UNSUPPORTED_ENCRYPTION(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_UNSUPPORTED_ENCRYPTION")) {
			PCLZIP_ERR_UNSUPPORTED_ENCRYPTION = initialValue;
			constDefs.add("PCLZIP_ERR_UNSUPPORTED_ENCRYPTION");
		}
	}

	public int getPCLZIP_ERR_UNSUPPORTED_ENCRYPTION() {
		return PCLZIP_ERR_UNSUPPORTED_ENCRYPTION;
	}

	public boolean isPCLZIP_ERR_UNSUPPORTED_ENCRYPTIONDefined() {
		return constDefs.contains("PCLZIP_ERR_UNSUPPORTED_ENCRYPTION");
	}

	public void setPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE")) {
			PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE = initialValue;
			constDefs.add("PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE");
		}
	}

	public int getPCLZIP_ERR_INVALID_ATTRIBUTE_VALUE() {
		return PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE;
	}

	public boolean isPCLZIP_ERR_INVALID_ATTRIBUTE_VALUEDefined() {
		return constDefs.contains("PCLZIP_ERR_INVALID_ATTRIBUTE_VALUE");
	}

	public void setPCLZIP_ERR_DIRECTORY_RESTRICTION(int initialValue) {
		if (!constDefs.contains("PCLZIP_ERR_DIRECTORY_RESTRICTION")) {
			PCLZIP_ERR_DIRECTORY_RESTRICTION = initialValue;
			constDefs.add("PCLZIP_ERR_DIRECTORY_RESTRICTION");
		}
	}

	public int getPCLZIP_ERR_DIRECTORY_RESTRICTION() {
		return PCLZIP_ERR_DIRECTORY_RESTRICTION;
	}

	public boolean isPCLZIP_ERR_DIRECTORY_RESTRICTIONDefined() {
		return constDefs.contains("PCLZIP_ERR_DIRECTORY_RESTRICTION");
	}

	public void setPCLZIP_OPT_PATH(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_PATH")) {
			PCLZIP_OPT_PATH = initialValue;
			constDefs.add("PCLZIP_OPT_PATH");
		}
	}

	public int getPCLZIP_OPT_PATH() {
		return PCLZIP_OPT_PATH;
	}

	public boolean isPCLZIP_OPT_PATHDefined() {
		return constDefs.contains("PCLZIP_OPT_PATH");
	}

	public void setPCLZIP_OPT_ADD_PATH(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_ADD_PATH")) {
			PCLZIP_OPT_ADD_PATH = initialValue;
			constDefs.add("PCLZIP_OPT_ADD_PATH");
		}
	}

	public int getPCLZIP_OPT_ADD_PATH() {
		return PCLZIP_OPT_ADD_PATH;
	}

	public boolean isPCLZIP_OPT_ADD_PATHDefined() {
		return constDefs.contains("PCLZIP_OPT_ADD_PATH");
	}

	public void setPCLZIP_OPT_REMOVE_PATH(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_REMOVE_PATH")) {
			PCLZIP_OPT_REMOVE_PATH = initialValue;
			constDefs.add("PCLZIP_OPT_REMOVE_PATH");
		}
	}

	public int getPCLZIP_OPT_REMOVE_PATH() {
		return PCLZIP_OPT_REMOVE_PATH;
	}

	public boolean isPCLZIP_OPT_REMOVE_PATHDefined() {
		return constDefs.contains("PCLZIP_OPT_REMOVE_PATH");
	}

	public void setPCLZIP_OPT_REMOVE_ALL_PATH(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_REMOVE_ALL_PATH")) {
			PCLZIP_OPT_REMOVE_ALL_PATH = initialValue;
			constDefs.add("PCLZIP_OPT_REMOVE_ALL_PATH");
		}
	}

	public int getPCLZIP_OPT_REMOVE_ALL_PATH() {
		return PCLZIP_OPT_REMOVE_ALL_PATH;
	}

	public boolean isPCLZIP_OPT_REMOVE_ALL_PATHDefined() {
		return constDefs.contains("PCLZIP_OPT_REMOVE_ALL_PATH");
	}

	public void setPCLZIP_OPT_SET_CHMOD(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_SET_CHMOD")) {
			PCLZIP_OPT_SET_CHMOD = initialValue;
			constDefs.add("PCLZIP_OPT_SET_CHMOD");
		}
	}

	public int getPCLZIP_OPT_SET_CHMOD() {
		return PCLZIP_OPT_SET_CHMOD;
	}

	public boolean isPCLZIP_OPT_SET_CHMODDefined() {
		return constDefs.contains("PCLZIP_OPT_SET_CHMOD");
	}

	public void setPCLZIP_OPT_EXTRACT_AS_STRING(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_EXTRACT_AS_STRING")) {
			PCLZIP_OPT_EXTRACT_AS_STRING = initialValue;
			constDefs.add("PCLZIP_OPT_EXTRACT_AS_STRING");
		}
	}

	public int getPCLZIP_OPT_EXTRACT_AS_STRING() {
		return PCLZIP_OPT_EXTRACT_AS_STRING;
	}

	public boolean isPCLZIP_OPT_EXTRACT_AS_STRINGDefined() {
		return constDefs.contains("PCLZIP_OPT_EXTRACT_AS_STRING");
	}

	public void setPCLZIP_OPT_NO_COMPRESSION(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_NO_COMPRESSION")) {
			PCLZIP_OPT_NO_COMPRESSION = initialValue;
			constDefs.add("PCLZIP_OPT_NO_COMPRESSION");
		}
	}

	public int getPCLZIP_OPT_NO_COMPRESSION() {
		return PCLZIP_OPT_NO_COMPRESSION;
	}

	public boolean isPCLZIP_OPT_NO_COMPRESSIONDefined() {
		return constDefs.contains("PCLZIP_OPT_NO_COMPRESSION");
	}

	public void setPCLZIP_OPT_BY_NAME(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_BY_NAME")) {
			PCLZIP_OPT_BY_NAME = initialValue;
			constDefs.add("PCLZIP_OPT_BY_NAME");
		}
	}

	public int getPCLZIP_OPT_BY_NAME() {
		return PCLZIP_OPT_BY_NAME;
	}

	public boolean isPCLZIP_OPT_BY_NAMEDefined() {
		return constDefs.contains("PCLZIP_OPT_BY_NAME");
	}

	public void setPCLZIP_OPT_BY_INDEX(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_BY_INDEX")) {
			PCLZIP_OPT_BY_INDEX = initialValue;
			constDefs.add("PCLZIP_OPT_BY_INDEX");
		}
	}

	public int getPCLZIP_OPT_BY_INDEX() {
		return PCLZIP_OPT_BY_INDEX;
	}

	public boolean isPCLZIP_OPT_BY_INDEXDefined() {
		return constDefs.contains("PCLZIP_OPT_BY_INDEX");
	}

	public void setPCLZIP_OPT_BY_EREG(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_BY_EREG")) {
			PCLZIP_OPT_BY_EREG = initialValue;
			constDefs.add("PCLZIP_OPT_BY_EREG");
		}
	}

	public int getPCLZIP_OPT_BY_EREG() {
		return PCLZIP_OPT_BY_EREG;
	}

	public boolean isPCLZIP_OPT_BY_EREGDefined() {
		return constDefs.contains("PCLZIP_OPT_BY_EREG");
	}

	public void setPCLZIP_OPT_BY_PREG(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_BY_PREG")) {
			PCLZIP_OPT_BY_PREG = initialValue;
			constDefs.add("PCLZIP_OPT_BY_PREG");
		}
	}

	public int getPCLZIP_OPT_BY_PREG() {
		return PCLZIP_OPT_BY_PREG;
	}

	public boolean isPCLZIP_OPT_BY_PREGDefined() {
		return constDefs.contains("PCLZIP_OPT_BY_PREG");
	}

	public void setPCLZIP_OPT_COMMENT(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_COMMENT")) {
			PCLZIP_OPT_COMMENT = initialValue;
			constDefs.add("PCLZIP_OPT_COMMENT");
		}
	}

	public int getPCLZIP_OPT_COMMENT() {
		return PCLZIP_OPT_COMMENT;
	}

	public boolean isPCLZIP_OPT_COMMENTDefined() {
		return constDefs.contains("PCLZIP_OPT_COMMENT");
	}

	public void setPCLZIP_OPT_ADD_COMMENT(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_ADD_COMMENT")) {
			PCLZIP_OPT_ADD_COMMENT = initialValue;
			constDefs.add("PCLZIP_OPT_ADD_COMMENT");
		}
	}

	public int getPCLZIP_OPT_ADD_COMMENT() {
		return PCLZIP_OPT_ADD_COMMENT;
	}

	public boolean isPCLZIP_OPT_ADD_COMMENTDefined() {
		return constDefs.contains("PCLZIP_OPT_ADD_COMMENT");
	}

	public void setPCLZIP_OPT_PREPEND_COMMENT(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_PREPEND_COMMENT")) {
			PCLZIP_OPT_PREPEND_COMMENT = initialValue;
			constDefs.add("PCLZIP_OPT_PREPEND_COMMENT");
		}
	}

	public int getPCLZIP_OPT_PREPEND_COMMENT() {
		return PCLZIP_OPT_PREPEND_COMMENT;
	}

	public boolean isPCLZIP_OPT_PREPEND_COMMENTDefined() {
		return constDefs.contains("PCLZIP_OPT_PREPEND_COMMENT");
	}

	public void setPCLZIP_OPT_EXTRACT_IN_OUTPUT(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_EXTRACT_IN_OUTPUT")) {
			PCLZIP_OPT_EXTRACT_IN_OUTPUT = initialValue;
			constDefs.add("PCLZIP_OPT_EXTRACT_IN_OUTPUT");
		}
	}

	public int getPCLZIP_OPT_EXTRACT_IN_OUTPUT() {
		return PCLZIP_OPT_EXTRACT_IN_OUTPUT;
	}

	public boolean isPCLZIP_OPT_EXTRACT_IN_OUTPUTDefined() {
		return constDefs.contains("PCLZIP_OPT_EXTRACT_IN_OUTPUT");
	}

	public void setPCLZIP_OPT_REPLACE_NEWER(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_REPLACE_NEWER")) {
			PCLZIP_OPT_REPLACE_NEWER = initialValue;
			constDefs.add("PCLZIP_OPT_REPLACE_NEWER");
		}
	}

	public int getPCLZIP_OPT_REPLACE_NEWER() {
		return PCLZIP_OPT_REPLACE_NEWER;
	}

	public boolean isPCLZIP_OPT_REPLACE_NEWERDefined() {
		return constDefs.contains("PCLZIP_OPT_REPLACE_NEWER");
	}

	public void setPCLZIP_OPT_STOP_ON_ERROR(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_STOP_ON_ERROR")) {
			PCLZIP_OPT_STOP_ON_ERROR = initialValue;
			constDefs.add("PCLZIP_OPT_STOP_ON_ERROR");
		}
	}

	public int getPCLZIP_OPT_STOP_ON_ERROR() {
		return PCLZIP_OPT_STOP_ON_ERROR;
	}

	public boolean isPCLZIP_OPT_STOP_ON_ERRORDefined() {
		return constDefs.contains("PCLZIP_OPT_STOP_ON_ERROR");
	}

	public void setPCLZIP_OPT_EXTRACT_DIR_RESTRICTION(int initialValue) {
		if (!constDefs.contains("PCLZIP_OPT_EXTRACT_DIR_RESTRICTION")) {
			PCLZIP_OPT_EXTRACT_DIR_RESTRICTION = initialValue;
			constDefs.add("PCLZIP_OPT_EXTRACT_DIR_RESTRICTION");
		}
	}

	public int getPCLZIP_OPT_EXTRACT_DIR_RESTRICTION() {
		return PCLZIP_OPT_EXTRACT_DIR_RESTRICTION;
	}

	public boolean isPCLZIP_OPT_EXTRACT_DIR_RESTRICTIONDefined() {
		return constDefs.contains("PCLZIP_OPT_EXTRACT_DIR_RESTRICTION");
	}

	public void setPCLZIP_ATT_FILE_NAME(int initialValue) {
		if (!constDefs.contains("PCLZIP_ATT_FILE_NAME")) {
			PCLZIP_ATT_FILE_NAME = initialValue;
			constDefs.add("PCLZIP_ATT_FILE_NAME");
		}
	}

	public int getPCLZIP_ATT_FILE_NAME() {
		return PCLZIP_ATT_FILE_NAME;
	}

	public boolean isPCLZIP_ATT_FILE_NAMEDefined() {
		return constDefs.contains("PCLZIP_ATT_FILE_NAME");
	}

	public void setPCLZIP_ATT_FILE_NEW_SHORT_NAME(int initialValue) {
		if (!constDefs.contains("PCLZIP_ATT_FILE_NEW_SHORT_NAME")) {
			PCLZIP_ATT_FILE_NEW_SHORT_NAME = initialValue;
			constDefs.add("PCLZIP_ATT_FILE_NEW_SHORT_NAME");
		}
	}

	public int getPCLZIP_ATT_FILE_NEW_SHORT_NAME() {
		return PCLZIP_ATT_FILE_NEW_SHORT_NAME;
	}

	public boolean isPCLZIP_ATT_FILE_NEW_SHORT_NAMEDefined() {
		return constDefs.contains("PCLZIP_ATT_FILE_NEW_SHORT_NAME");
	}

	public void setPCLZIP_ATT_FILE_NEW_FULL_NAME(int initialValue) {
		if (!constDefs.contains("PCLZIP_ATT_FILE_NEW_FULL_NAME")) {
			PCLZIP_ATT_FILE_NEW_FULL_NAME = initialValue;
			constDefs.add("PCLZIP_ATT_FILE_NEW_FULL_NAME");
		}
	}

	public int getPCLZIP_ATT_FILE_NEW_FULL_NAME() {
		return PCLZIP_ATT_FILE_NEW_FULL_NAME;
	}

	public boolean isPCLZIP_ATT_FILE_NEW_FULL_NAMEDefined() {
		return constDefs.contains("PCLZIP_ATT_FILE_NEW_FULL_NAME");
	}

	public void setPCLZIP_CB_PRE_EXTRACT(int initialValue) {
		if (!constDefs.contains("PCLZIP_CB_PRE_EXTRACT")) {
			PCLZIP_CB_PRE_EXTRACT = initialValue;
			constDefs.add("PCLZIP_CB_PRE_EXTRACT");
		}
	}

	public int getPCLZIP_CB_PRE_EXTRACT() {
		return PCLZIP_CB_PRE_EXTRACT;
	}

	public boolean isPCLZIP_CB_PRE_EXTRACTDefined() {
		return constDefs.contains("PCLZIP_CB_PRE_EXTRACT");
	}

	public void setPCLZIP_CB_POST_EXTRACT(int initialValue) {
		if (!constDefs.contains("PCLZIP_CB_POST_EXTRACT")) {
			PCLZIP_CB_POST_EXTRACT = initialValue;
			constDefs.add("PCLZIP_CB_POST_EXTRACT");
		}
	}

	public int getPCLZIP_CB_POST_EXTRACT() {
		return PCLZIP_CB_POST_EXTRACT;
	}

	public boolean isPCLZIP_CB_POST_EXTRACTDefined() {
		return constDefs.contains("PCLZIP_CB_POST_EXTRACT");
	}

	public void setPCLZIP_CB_PRE_ADD(int initialValue) {
		if (!constDefs.contains("PCLZIP_CB_PRE_ADD")) {
			PCLZIP_CB_PRE_ADD = initialValue;
			constDefs.add("PCLZIP_CB_PRE_ADD");
		}
	}

	public int getPCLZIP_CB_PRE_ADD() {
		return PCLZIP_CB_PRE_ADD;
	}

	public boolean isPCLZIP_CB_PRE_ADDDefined() {
		return constDefs.contains("PCLZIP_CB_PRE_ADD");
	}

	public void setPCLZIP_CB_POST_ADD(int initialValue) {
		if (!constDefs.contains("PCLZIP_CB_POST_ADD")) {
			PCLZIP_CB_POST_ADD = initialValue;
			constDefs.add("PCLZIP_CB_POST_ADD");
		}
	}

	public int getPCLZIP_CB_POST_ADD() {
		return PCLZIP_CB_POST_ADD;
	}

	public boolean isPCLZIP_CB_POST_ADDDefined() {
		return constDefs.contains("PCLZIP_CB_POST_ADD");
	}

	public void setFTP_BASE(String initialValue) {
		if (!constDefs.contains("FTP_BASE")) {
			FTP_BASE = initialValue;
			constDefs.add("FTP_BASE");
		}
	}

	public String getFTP_BASE() {
		return FTP_BASE;
	}

	public boolean isFTP_BASEDefined() {
		return constDefs.contains("FTP_BASE");
	}

	public void setWXR_VERSION(String initialValue) {
		if (!constDefs.contains("WXR_VERSION")) {
			WXR_VERSION = initialValue;
			constDefs.add("WXR_VERSION");
		}
	}

	public String getWXR_VERSION() {
		return WXR_VERSION;
	}

	public boolean isWXR_VERSIONDefined() {
		return constDefs.contains("WXR_VERSION");
	}

	public void setWP_TEMP_DIR(String initialValue) {
		if (!constDefs.contains("WP_TEMP_DIR")) {
			WP_TEMP_DIR = initialValue;
			constDefs.add("WP_TEMP_DIR");
		}
	}

	public String getWP_TEMP_DIR() {
		return WP_TEMP_DIR;
	}

	public boolean isWP_TEMP_DIRDefined() {
		return constDefs.contains("WP_TEMP_DIR");
	}

	public void setWP_INSTALLING(boolean initialValue) {
		if (!constDefs.contains("WP_INSTALLING")) {
			WP_INSTALLING = initialValue;
			constDefs.add("WP_INSTALLING");
		}
	}

	public boolean getWP_INSTALLING() {
		return WP_INSTALLING;
	}

	public boolean isWP_INSTALLINGDefined() {
		return constDefs.contains("WP_INSTALLING");
	}

	public void setWP_SITEURL(String initialValue) {
		if (!constDefs.contains("WP_SITEURL")) {
			WP_SITEURL = initialValue;
			constDefs.add("WP_SITEURL");
		}
	}

	public String getWP_SITEURL() {
		return WP_SITEURL;
	}

	public boolean isWP_SITEURLDefined() {
		return constDefs.contains("WP_SITEURL");
	}

	public void setRESET_CAPS(Object initialValue) {
		if (!constDefs.contains("RESET_CAPS")) {
			RESET_CAPS = initialValue;
			constDefs.add("RESET_CAPS");
		}
	}

	public Object getRESET_CAPS() {
		return RESET_CAPS;
	}

	public boolean isRESET_CAPSDefined() {
		return constDefs.contains("RESET_CAPS");
	}

	public void setWP_HOME(String initialValue) {
		if (!constDefs.contains("WP_HOME")) {
			WP_HOME = initialValue;
			constDefs.add("WP_HOME");
		}
	}

	public String getWP_HOME() {
		return WP_HOME;
	}

	public boolean isWP_HOMEDefined() {
		return constDefs.contains("WP_HOME");
	}

	public void setIS_PROFILE_PAGE(boolean initialValue) {
		if (!constDefs.contains("IS_PROFILE_PAGE")) {
			IS_PROFILE_PAGE = initialValue;
			constDefs.add("IS_PROFILE_PAGE");
		}
	}

	public boolean getIS_PROFILE_PAGE() {
		return IS_PROFILE_PAGE;
	}

	public boolean isIS_PROFILE_PAGEDefined() {
		return constDefs.contains("IS_PROFILE_PAGE");
	}

	public void setWPINC(String initialValue) {
		if (!constDefs.contains("WPINC")) {
			WPINC = initialValue;
			constDefs.add("WPINC");
		}
	}

	public String getWPINC() {
		return WPINC;
	}

	public boolean isWPINCDefined() {
		return constDefs.contains("WPINC");
	}

	public void setDB_NAME(String initialValue) {
		if (!constDefs.contains("DB_NAME")) {
			DB_NAME = initialValue;
			constDefs.add("DB_NAME");
		}
	}

	public String getDB_NAME() {
		return DB_NAME;
	}

	public boolean isDB_NAMEDefined() {
		return constDefs.contains("DB_NAME");
	}

	public void setDB_USER(String initialValue) {
		if (!constDefs.contains("DB_USER")) {
			DB_USER = initialValue;
			constDefs.add("DB_USER");
		}
	}

	public String getDB_USER() {
		return DB_USER;
	}

	public boolean isDB_USERDefined() {
		return constDefs.contains("DB_USER");
	}

	public void setDB_PASSWORD(String initialValue) {
		if (!constDefs.contains("DB_PASSWORD")) {
			DB_PASSWORD = initialValue;
			constDefs.add("DB_PASSWORD");
		}
	}

	public String getDB_PASSWORD() {
		return DB_PASSWORD;
	}

	public boolean isDB_PASSWORDDefined() {
		return constDefs.contains("DB_PASSWORD");
	}

	public void setDB_HOST(String initialValue) {
		if (!constDefs.contains("DB_HOST")) {
			DB_HOST = initialValue;
			constDefs.add("DB_HOST");
		}
	}

	public String getDB_HOST() {
		return DB_HOST;
	}

	public boolean isDB_HOSTDefined() {
		return constDefs.contains("DB_HOST");
	}

	public void setFTP_HOST(String initialValue) {
		if (!constDefs.contains("FTP_HOST")) {
			FTP_HOST = initialValue;
			constDefs.add("FTP_HOST");
		}
	}

	public String getFTP_HOST() {
		return FTP_HOST;
	}

	public boolean isFTP_HOSTDefined() {
		return constDefs.contains("FTP_HOST");
	}

	public void setFTP_USER(String initialValue) {
		if (!constDefs.contains("FTP_USER")) {
			FTP_USER = initialValue;
			constDefs.add("FTP_USER");
		}
	}

	public String getFTP_USER() {
		return FTP_USER;
	}

	public boolean isFTP_USERDefined() {
		return constDefs.contains("FTP_USER");
	}

	public void setFTP_PASS(String initialValue) {
		if (!constDefs.contains("FTP_PASS")) {
			FTP_PASS = initialValue;
			constDefs.add("FTP_PASS");
		}
	}

	public String getFTP_PASS() {
		return FTP_PASS;
	}

	public boolean isFTP_PASSDefined() {
		return constDefs.contains("FTP_PASS");
	}

	public void setFTP_SSL(Object initialValue) {
		if (!constDefs.contains("FTP_SSL")) {
			FTP_SSL = initialValue;
			constDefs.add("FTP_SSL");
		}
	}

	public Object getFTP_SSL() {
		return FTP_SSL;
	}

	public boolean isFTP_SSLDefined() {
		return constDefs.contains("FTP_SSL");
	}

	public void setAPP_REQUEST(boolean initialValue) {
		if (!constDefs.contains("APP_REQUEST")) {
			APP_REQUEST = initialValue;
			constDefs.add("APP_REQUEST");
		}
	}

	public boolean getAPP_REQUEST() {
		return APP_REQUEST;
	}

	public boolean isAPP_REQUESTDefined() {
		return constDefs.contains("APP_REQUEST");
	}

	public void setDB_CHARSET(String initialValue) {
		if (!constDefs.contains("DB_CHARSET")) {
			DB_CHARSET = initialValue;
			constDefs.add("DB_CHARSET");
		}
	}

	public String getDB_CHARSET() {
		return DB_CHARSET;
	}

	public boolean isDB_CHARSETDefined() {
		return constDefs.contains("DB_CHARSET");
	}

	public void setDB_COLLATE(String initialValue) {
		if (!constDefs.contains("DB_COLLATE")) {
			DB_COLLATE = initialValue;
			constDefs.add("DB_COLLATE");
		}
	}

	public String getDB_COLLATE() {
		return DB_COLLATE;
	}

	public boolean isDB_COLLATEDefined() {
		return constDefs.contains("DB_COLLATE");
	}

	public void setSECRET_KEY(String initialValue) {
		if (!constDefs.contains("SECRET_KEY")) {
			SECRET_KEY = initialValue;
			constDefs.add("SECRET_KEY");
		}
	}

	public String getSECRET_KEY() {
		return SECRET_KEY;
	}

	public boolean isSECRET_KEYDefined() {
		return constDefs.contains("SECRET_KEY");
	}

	public void setWPLANG(String initialValue) {
		if (!constDefs.contains("WPLANG")) {
			WPLANG = initialValue;
			constDefs.add("WPLANG");
		}
	}

	public String getWPLANG() {
		return WPLANG;
	}

	public boolean isWPLANGDefined() {
		return constDefs.contains("WPLANG");
	}

	public void setDOING_CRON(boolean initialValue) {
		if (!constDefs.contains("DOING_CRON")) {
			DOING_CRON = initialValue;
			constDefs.add("DOING_CRON");
		}
	}

	public boolean getDOING_CRON() {
		return DOING_CRON;
	}

	public boolean isDOING_CRONDefined() {
		return constDefs.contains("DOING_CRON");
	}

	public void setCOMMENTS_TEMPLATE(boolean initialValue) {
		if (!constDefs.contains("COMMENTS_TEMPLATE")) {
			COMMENTS_TEMPLATE = initialValue;
			constDefs.add("COMMENTS_TEMPLATE");
		}
	}

	public boolean getCOMMENTS_TEMPLATE() {
		return COMMENTS_TEMPLATE;
	}

	public boolean isCOMMENTS_TEMPLATEDefined() {
		return constDefs.contains("COMMENTS_TEMPLATE");
	}

	public void setUPLOADS(String initialValue) {
		if (!constDefs.contains("UPLOADS")) {
			UPLOADS = initialValue;
			constDefs.add("UPLOADS");
		}
	}

	public String getUPLOADS() {
		return UPLOADS;
	}

	public boolean isUPLOADSDefined() {
		return constDefs.contains("UPLOADS");
	}

	public void setWP_DEBUG(boolean initialValue) {
		if (!constDefs.contains("WP_DEBUG")) {
			WP_DEBUG = initialValue;
			constDefs.add("WP_DEBUG");
		}
	}

	public boolean getWP_DEBUG() {
		return WP_DEBUG;
	}

	public boolean isWP_DEBUGDefined() {
		return constDefs.contains("WP_DEBUG");
	}

	public void setJSON_BOOL(int initialValue) {
		if (!constDefs.contains("JSON_BOOL")) {
			JSON_BOOL = initialValue;
			constDefs.add("JSON_BOOL");
		}
	}

	public int getJSON_BOOL() {
		return JSON_BOOL;
	}

	public boolean isJSON_BOOLDefined() {
		return constDefs.contains("JSON_BOOL");
	}

	public void setJSON_INT(int initialValue) {
		if (!constDefs.contains("JSON_INT")) {
			JSON_INT = initialValue;
			constDefs.add("JSON_INT");
		}
	}

	public int getJSON_INT() {
		return JSON_INT;
	}

	public boolean isJSON_INTDefined() {
		return constDefs.contains("JSON_INT");
	}

	public void setJSON_STR(int initialValue) {
		if (!constDefs.contains("JSON_STR")) {
			JSON_STR = initialValue;
			constDefs.add("JSON_STR");
		}
	}

	public int getJSON_STR() {
		return JSON_STR;
	}

	public boolean isJSON_STRDefined() {
		return constDefs.contains("JSON_STR");
	}

	public void setJSON_FLOAT(int initialValue) {
		if (!constDefs.contains("JSON_FLOAT")) {
			JSON_FLOAT = initialValue;
			constDefs.add("JSON_FLOAT");
		}
	}

	public int getJSON_FLOAT() {
		return JSON_FLOAT;
	}

	public boolean isJSON_FLOATDefined() {
		return constDefs.contains("JSON_FLOAT");
	}

	public void setJSON_NULL(int initialValue) {
		if (!constDefs.contains("JSON_NULL")) {
			JSON_NULL = initialValue;
			constDefs.add("JSON_NULL");
		}
	}

	public int getJSON_NULL() {
		return JSON_NULL;
	}

	public boolean isJSON_NULLDefined() {
		return constDefs.contains("JSON_NULL");
	}

	public void setJSON_START_OBJ(int initialValue) {
		if (!constDefs.contains("JSON_START_OBJ")) {
			JSON_START_OBJ = initialValue;
			constDefs.add("JSON_START_OBJ");
		}
	}

	public int getJSON_START_OBJ() {
		return JSON_START_OBJ;
	}

	public boolean isJSON_START_OBJDefined() {
		return constDefs.contains("JSON_START_OBJ");
	}

	public void setJSON_END_OBJ(int initialValue) {
		if (!constDefs.contains("JSON_END_OBJ")) {
			JSON_END_OBJ = initialValue;
			constDefs.add("JSON_END_OBJ");
		}
	}

	public int getJSON_END_OBJ() {
		return JSON_END_OBJ;
	}

	public boolean isJSON_END_OBJDefined() {
		return constDefs.contains("JSON_END_OBJ");
	}

	public void setJSON_START_ARRAY(int initialValue) {
		if (!constDefs.contains("JSON_START_ARRAY")) {
			JSON_START_ARRAY = initialValue;
			constDefs.add("JSON_START_ARRAY");
		}
	}

	public int getJSON_START_ARRAY() {
		return JSON_START_ARRAY;
	}

	public boolean isJSON_START_ARRAYDefined() {
		return constDefs.contains("JSON_START_ARRAY");
	}

	public void setJSON_END_ARRAY(int initialValue) {
		if (!constDefs.contains("JSON_END_ARRAY")) {
			JSON_END_ARRAY = initialValue;
			constDefs.add("JSON_END_ARRAY");
		}
	}

	public int getJSON_END_ARRAY() {
		return JSON_END_ARRAY;
	}

	public boolean isJSON_END_ARRAYDefined() {
		return constDefs.contains("JSON_END_ARRAY");
	}

	public void setJSON_KEY(int initialValue) {
		if (!constDefs.contains("JSON_KEY")) {
			JSON_KEY = initialValue;
			constDefs.add("JSON_KEY");
		}
	}

	public int getJSON_KEY() {
		return JSON_KEY;
	}

	public boolean isJSON_KEYDefined() {
		return constDefs.contains("JSON_KEY");
	}

	public void setJSON_SKIP(int initialValue) {
		if (!constDefs.contains("JSON_SKIP")) {
			JSON_SKIP = initialValue;
			constDefs.add("JSON_SKIP");
		}
	}

	public int getJSON_SKIP() {
		return JSON_SKIP;
	}

	public boolean isJSON_SKIPDefined() {
		return constDefs.contains("JSON_SKIP");
	}

	public void setJSON_IN_ARRAY(int initialValue) {
		if (!constDefs.contains("JSON_IN_ARRAY")) {
			JSON_IN_ARRAY = initialValue;
			constDefs.add("JSON_IN_ARRAY");
		}
	}

	public int getJSON_IN_ARRAY() {
		return JSON_IN_ARRAY;
	}

	public boolean isJSON_IN_ARRAYDefined() {
		return constDefs.contains("JSON_IN_ARRAY");
	}

	public void setJSON_IN_OBJECT(int initialValue) {
		if (!constDefs.contains("JSON_IN_OBJECT")) {
			JSON_IN_OBJECT = initialValue;
			constDefs.add("JSON_IN_OBJECT");
		}
	}

	public int getJSON_IN_OBJECT() {
		return JSON_IN_OBJECT;
	}

	public boolean isJSON_IN_OBJECTDefined() {
		return constDefs.contains("JSON_IN_OBJECT");
	}

	public void setJSON_IN_BETWEEN(int initialValue) {
		if (!constDefs.contains("JSON_IN_BETWEEN")) {
			JSON_IN_BETWEEN = initialValue;
			constDefs.add("JSON_IN_BETWEEN");
		}
	}

	public int getJSON_IN_BETWEEN() {
		return JSON_IN_BETWEEN;
	}

	public boolean isJSON_IN_BETWEENDefined() {
		return constDefs.contains("JSON_IN_BETWEEN");
	}

	public void setMC_LOGGER_DEBUG(int initialValue) {
		if (!constDefs.contains("MC_LOGGER_DEBUG")) {
			MC_LOGGER_DEBUG = initialValue;
			constDefs.add("MC_LOGGER_DEBUG");
		}
	}

	public int getMC_LOGGER_DEBUG() {
		return MC_LOGGER_DEBUG;
	}

	public boolean isMC_LOGGER_DEBUGDefined() {
		return constDefs.contains("MC_LOGGER_DEBUG");
	}

	public void setMC_LOGGER_INFO(int initialValue) {
		if (!constDefs.contains("MC_LOGGER_INFO")) {
			MC_LOGGER_INFO = initialValue;
			constDefs.add("MC_LOGGER_INFO");
		}
	}

	public int getMC_LOGGER_INFO() {
		return MC_LOGGER_INFO;
	}

	public boolean isMC_LOGGER_INFODefined() {
		return constDefs.contains("MC_LOGGER_INFO");
	}

	public void setMC_LOGGER_WARN(int initialValue) {
		if (!constDefs.contains("MC_LOGGER_WARN")) {
			MC_LOGGER_WARN = initialValue;
			constDefs.add("MC_LOGGER_WARN");
		}
	}

	public int getMC_LOGGER_WARN() {
		return MC_LOGGER_WARN;
	}

	public boolean isMC_LOGGER_WARNDefined() {
		return constDefs.contains("MC_LOGGER_WARN");
	}

	public void setMC_LOGGER_ERROR(int initialValue) {
		if (!constDefs.contains("MC_LOGGER_ERROR")) {
			MC_LOGGER_ERROR = initialValue;
			constDefs.add("MC_LOGGER_ERROR");
		}
	}

	public int getMC_LOGGER_ERROR() {
		return MC_LOGGER_ERROR;
	}

	public boolean isMC_LOGGER_ERRORDefined() {
		return constDefs.contains("MC_LOGGER_ERROR");
	}

	public void setMC_LOGGER_FATAL(int initialValue) {
		if (!constDefs.contains("MC_LOGGER_FATAL")) {
			MC_LOGGER_FATAL = initialValue;
			constDefs.add("MC_LOGGER_FATAL");
		}
	}

	public int getMC_LOGGER_FATAL() {
		return MC_LOGGER_FATAL;
	}

	public boolean isMC_LOGGER_FATALDefined() {
		return constDefs.contains("MC_LOGGER_FATAL");
	}

	public void setCUSTOM_TAGS(boolean initialValue) {
		if (!constDefs.contains("CUSTOM_TAGS")) {
			CUSTOM_TAGS = initialValue;
			constDefs.add("CUSTOM_TAGS");
		}
	}

	public boolean getCUSTOM_TAGS() {
		return CUSTOM_TAGS;
	}

	public boolean isCUSTOM_TAGSDefined() {
		return constDefs.contains("CUSTOM_TAGS");
	}

	public void setXMLRPC_REQUEST(boolean initialValue) {
		if (!constDefs.contains("XMLRPC_REQUEST")) {
			XMLRPC_REQUEST = initialValue;
			constDefs.add("XMLRPC_REQUEST");
		}
	}

	public boolean getXMLRPC_REQUEST() {
		return XMLRPC_REQUEST;
	}

	public boolean isXMLRPC_REQUESTDefined() {
		return constDefs.contains("XMLRPC_REQUEST");
	}

	public void setSECRET_SALT(String initialValue) {
		if (!constDefs.contains("SECRET_SALT")) {
			SECRET_SALT = initialValue;
			constDefs.add("SECRET_SALT");
		}
	}

	public String getSECRET_SALT() {
		return SECRET_SALT;
	}

	public boolean isSECRET_SALTDefined() {
		return constDefs.contains("SECRET_SALT");
	}

	public void setEP_PERMALINK(int initialValue) {
		if (!constDefs.contains("EP_PERMALINK")) {
			EP_PERMALINK = initialValue;
			constDefs.add("EP_PERMALINK");
		}
	}

	public int getEP_PERMALINK() {
		return EP_PERMALINK;
	}

	public boolean isEP_PERMALINKDefined() {
		return constDefs.contains("EP_PERMALINK");
	}

	public void setEP_ATTACHMENT(int initialValue) {
		if (!constDefs.contains("EP_ATTACHMENT")) {
			EP_ATTACHMENT = initialValue;
			constDefs.add("EP_ATTACHMENT");
		}
	}

	public int getEP_ATTACHMENT() {
		return EP_ATTACHMENT;
	}

	public boolean isEP_ATTACHMENTDefined() {
		return constDefs.contains("EP_ATTACHMENT");
	}

	public void setEP_DATE(int initialValue) {
		if (!constDefs.contains("EP_DATE")) {
			EP_DATE = initialValue;
			constDefs.add("EP_DATE");
		}
	}

	public int getEP_DATE() {
		return EP_DATE;
	}

	public boolean isEP_DATEDefined() {
		return constDefs.contains("EP_DATE");
	}

	public void setEP_YEAR(int initialValue) {
		if (!constDefs.contains("EP_YEAR")) {
			EP_YEAR = initialValue;
			constDefs.add("EP_YEAR");
		}
	}

	public int getEP_YEAR() {
		return EP_YEAR;
	}

	public boolean isEP_YEARDefined() {
		return constDefs.contains("EP_YEAR");
	}

	public void setEP_MONTH(int initialValue) {
		if (!constDefs.contains("EP_MONTH")) {
			EP_MONTH = initialValue;
			constDefs.add("EP_MONTH");
		}
	}

	public int getEP_MONTH() {
		return EP_MONTH;
	}

	public boolean isEP_MONTHDefined() {
		return constDefs.contains("EP_MONTH");
	}

	public void setEP_DAY(int initialValue) {
		if (!constDefs.contains("EP_DAY")) {
			EP_DAY = initialValue;
			constDefs.add("EP_DAY");
		}
	}

	public int getEP_DAY() {
		return EP_DAY;
	}

	public boolean isEP_DAYDefined() {
		return constDefs.contains("EP_DAY");
	}

	public void setEP_ROOT(int initialValue) {
		if (!constDefs.contains("EP_ROOT")) {
			EP_ROOT = initialValue;
			constDefs.add("EP_ROOT");
		}
	}

	public int getEP_ROOT() {
		return EP_ROOT;
	}

	public boolean isEP_ROOTDefined() {
		return constDefs.contains("EP_ROOT");
	}

	public void setEP_COMMENTS(int initialValue) {
		if (!constDefs.contains("EP_COMMENTS")) {
			EP_COMMENTS = initialValue;
			constDefs.add("EP_COMMENTS");
		}
	}

	public int getEP_COMMENTS() {
		return EP_COMMENTS;
	}

	public boolean isEP_COMMENTSDefined() {
		return constDefs.contains("EP_COMMENTS");
	}

	public void setEP_SEARCH(int initialValue) {
		if (!constDefs.contains("EP_SEARCH")) {
			EP_SEARCH = initialValue;
			constDefs.add("EP_SEARCH");
		}
	}

	public int getEP_SEARCH() {
		return EP_SEARCH;
	}

	public boolean isEP_SEARCHDefined() {
		return constDefs.contains("EP_SEARCH");
	}

	public void setEP_CATEGORIES(int initialValue) {
		if (!constDefs.contains("EP_CATEGORIES")) {
			EP_CATEGORIES = initialValue;
			constDefs.add("EP_CATEGORIES");
		}
	}

	public int getEP_CATEGORIES() {
		return EP_CATEGORIES;
	}

	public boolean isEP_CATEGORIESDefined() {
		return constDefs.contains("EP_CATEGORIES");
	}

	public void setEP_TAGS(int initialValue) {
		if (!constDefs.contains("EP_TAGS")) {
			EP_TAGS = initialValue;
			constDefs.add("EP_TAGS");
		}
	}

	public int getEP_TAGS() {
		return EP_TAGS;
	}

	public boolean isEP_TAGSDefined() {
		return constDefs.contains("EP_TAGS");
	}

	public void setEP_AUTHORS(int initialValue) {
		if (!constDefs.contains("EP_AUTHORS")) {
			EP_AUTHORS = initialValue;
			constDefs.add("EP_AUTHORS");
		}
	}

	public int getEP_AUTHORS() {
		return EP_AUTHORS;
	}

	public boolean isEP_AUTHORSDefined() {
		return constDefs.contains("EP_AUTHORS");
	}

	public void setEP_PAGES(int initialValue) {
		if (!constDefs.contains("EP_PAGES")) {
			EP_PAGES = initialValue;
			constDefs.add("EP_PAGES");
		}
	}

	public int getEP_PAGES() {
		return EP_PAGES;
	}

	public boolean isEP_PAGESDefined() {
		return constDefs.contains("EP_PAGES");
	}

	public void setEP_NONE(int initialValue) {
		if (!constDefs.contains("EP_NONE")) {
			EP_NONE = initialValue;
			constDefs.add("EP_NONE");
		}
	}

	public int getEP_NONE() {
		return EP_NONE;
	}

	public boolean isEP_NONEDefined() {
		return constDefs.contains("EP_NONE");
	}

	public void setEP_ALL(int initialValue) {
		if (!constDefs.contains("EP_ALL")) {
			EP_ALL = initialValue;
			constDefs.add("EP_ALL");
		}
	}

	public int getEP_ALL() {
		return EP_ALL;
	}

	public boolean isEP_ALLDefined() {
		return constDefs.contains("EP_ALL");
	}

	public void setMAGPIE_INITALIZED(int initialValue) {
		if (!constDefs.contains("MAGPIE_INITALIZED")) {
			MAGPIE_INITALIZED = initialValue;
			constDefs.add("MAGPIE_INITALIZED");
		}
	}

	public int getMAGPIE_INITALIZED() {
		return MAGPIE_INITALIZED;
	}

	public boolean isMAGPIE_INITALIZEDDefined() {
		return constDefs.contains("MAGPIE_INITALIZED");
	}

	public void setMAGPIE_CACHE_ON(int initialValue) {
		if (!constDefs.contains("MAGPIE_CACHE_ON")) {
			MAGPIE_CACHE_ON = initialValue;
			constDefs.add("MAGPIE_CACHE_ON");
		}
	}

	public int getMAGPIE_CACHE_ON() {
		return MAGPIE_CACHE_ON;
	}

	public boolean isMAGPIE_CACHE_ONDefined() {
		return constDefs.contains("MAGPIE_CACHE_ON");
	}

	public void setMAGPIE_CACHE_DIR(String initialValue) {
		if (!constDefs.contains("MAGPIE_CACHE_DIR")) {
			MAGPIE_CACHE_DIR = initialValue;
			constDefs.add("MAGPIE_CACHE_DIR");
		}
	}

	public String getMAGPIE_CACHE_DIR() {
		return MAGPIE_CACHE_DIR;
	}

	public boolean isMAGPIE_CACHE_DIRDefined() {
		return constDefs.contains("MAGPIE_CACHE_DIR");
	}

	public void setMAGPIE_CACHE_AGE(int initialValue) {
		if (!constDefs.contains("MAGPIE_CACHE_AGE")) {
			MAGPIE_CACHE_AGE = initialValue;
			constDefs.add("MAGPIE_CACHE_AGE");
		}
	}

	public int getMAGPIE_CACHE_AGE() {
		return MAGPIE_CACHE_AGE;
	}

	public boolean isMAGPIE_CACHE_AGEDefined() {
		return constDefs.contains("MAGPIE_CACHE_AGE");
	}

	public void setMAGPIE_CACHE_FRESH_ONLY(int initialValue) {
		if (!constDefs.contains("MAGPIE_CACHE_FRESH_ONLY")) {
			MAGPIE_CACHE_FRESH_ONLY = initialValue;
			constDefs.add("MAGPIE_CACHE_FRESH_ONLY");
		}
	}

	public int getMAGPIE_CACHE_FRESH_ONLY() {
		return MAGPIE_CACHE_FRESH_ONLY;
	}

	public boolean isMAGPIE_CACHE_FRESH_ONLYDefined() {
		return constDefs.contains("MAGPIE_CACHE_FRESH_ONLY");
	}

	public void setMAGPIE_DEBUG(int initialValue) {
		if (!constDefs.contains("MAGPIE_DEBUG")) {
			MAGPIE_DEBUG = initialValue;
			constDefs.add("MAGPIE_DEBUG");
		}
	}

	public int getMAGPIE_DEBUG() {
		return MAGPIE_DEBUG;
	}

	public boolean isMAGPIE_DEBUGDefined() {
		return constDefs.contains("MAGPIE_DEBUG");
	}

	public void setMAGPIE_USER_AGENT(String initialValue) {
		if (!constDefs.contains("MAGPIE_USER_AGENT")) {
			MAGPIE_USER_AGENT = initialValue;
			constDefs.add("MAGPIE_USER_AGENT");
		}
	}

	public String getMAGPIE_USER_AGENT() {
		return MAGPIE_USER_AGENT;
	}

	public boolean isMAGPIE_USER_AGENTDefined() {
		return constDefs.contains("MAGPIE_USER_AGENT");
	}

	public void setMAGPIE_FETCH_TIME_OUT(int initialValue) {
		if (!constDefs.contains("MAGPIE_FETCH_TIME_OUT")) {
			MAGPIE_FETCH_TIME_OUT = initialValue;
			constDefs.add("MAGPIE_FETCH_TIME_OUT");
		}
	}

	public int getMAGPIE_FETCH_TIME_OUT() {
		return MAGPIE_FETCH_TIME_OUT;
	}

	public boolean isMAGPIE_FETCH_TIME_OUTDefined() {
		return constDefs.contains("MAGPIE_FETCH_TIME_OUT");
	}

	public void setMAGPIE_USE_GZIP(boolean initialValue) {
		if (!constDefs.contains("MAGPIE_USE_GZIP")) {
			MAGPIE_USE_GZIP = initialValue;
			constDefs.add("MAGPIE_USE_GZIP");
		}
	}

	public boolean getMAGPIE_USE_GZIP() {
		return MAGPIE_USE_GZIP;
	}

	public boolean isMAGPIE_USE_GZIPDefined() {
		return constDefs.contains("MAGPIE_USE_GZIP");
	}

	public void setRSS(String initialValue) {
		if (!constDefs.contains("RSS")) {
			RSS = initialValue;
			constDefs.add("RSS");
		}
	}

	public String getRSS() {
		return RSS;
	}

	public boolean isRSSDefined() {
		return constDefs.contains("RSS");
	}

	public void setATOM(String initialValue) {
		if (!constDefs.contains("ATOM")) {
			ATOM = initialValue;
			constDefs.add("ATOM");
		}
	}

	public String getATOM() {
		return ATOM;
	}

	public boolean isATOMDefined() {
		return constDefs.contains("ATOM");
	}

	public void setEZSQL_VERSION(String initialValue) {
		if (!constDefs.contains("EZSQL_VERSION")) {
			EZSQL_VERSION = initialValue;
			constDefs.add("EZSQL_VERSION");
		}
	}

	public String getEZSQL_VERSION() {
		return EZSQL_VERSION;
	}

	public boolean isEZSQL_VERSIONDefined() {
		return constDefs.contains("EZSQL_VERSION");
	}

	public void setOBJECT(String initialValue) {
		if (!constDefs.contains("OBJECT")) {
			OBJECT = initialValue;
			constDefs.add("OBJECT");
		}
	}

	public String getOBJECT() {
		return OBJECT;
	}

	public boolean isOBJECTDefined() {
		return constDefs.contains("OBJECT");
	}

	public void setOBJECT_K(String initialValue) {
		if (!constDefs.contains("OBJECT_K")) {
			OBJECT_K = initialValue;
			constDefs.add("OBJECT_K");
		}
	}

	public String getOBJECT_K() {
		return OBJECT_K;
	}

	public boolean isOBJECT_KDefined() {
		return constDefs.contains("OBJECT_K");
	}

	public void setARRAY_A(String initialValue) {
		if (!constDefs.contains("ARRAY_A")) {
			ARRAY_A = initialValue;
			constDefs.add("ARRAY_A");
		}
	}

	public String getARRAY_A() {
		return ARRAY_A;
	}

	public boolean isARRAY_ADefined() {
		return constDefs.contains("ARRAY_A");
	}

	public void setARRAY_N(String initialValue) {
		if (!constDefs.contains("ARRAY_N")) {
			ARRAY_N = initialValue;
			constDefs.add("ARRAY_N");
		}
	}

	public String getARRAY_N() {
		return ARRAY_N;
	}

	public boolean isARRAY_NDefined() {
		return constDefs.contains("ARRAY_N");
	}

	public void setSAVEQUERIES(boolean initialValue) {
		if (!constDefs.contains("SAVEQUERIES")) {
			SAVEQUERIES = initialValue;
			constDefs.add("SAVEQUERIES");
		}
	}

	public boolean getSAVEQUERIES() {
		return SAVEQUERIES;
	}

	public boolean isSAVEQUERIESDefined() {
		return constDefs.contains("SAVEQUERIES");
	}

	public void setCUSTOM_USER_TABLE(String initialValue) {
		if (!constDefs.contains("CUSTOM_USER_TABLE")) {
			CUSTOM_USER_TABLE = initialValue;
			constDefs.add("CUSTOM_USER_TABLE");
		}
	}

	public String getCUSTOM_USER_TABLE() {
		return CUSTOM_USER_TABLE;
	}

	public boolean isCUSTOM_USER_TABLEDefined() {
		return constDefs.contains("CUSTOM_USER_TABLE");
	}

	public void setCUSTOM_USER_META_TABLE(String initialValue) {
		if (!constDefs.contains("CUSTOM_USER_META_TABLE")) {
			CUSTOM_USER_META_TABLE = initialValue;
			constDefs.add("CUSTOM_USER_META_TABLE");
		}
	}

	public String getCUSTOM_USER_META_TABLE() {
		return CUSTOM_USER_META_TABLE;
	}

	public boolean isCUSTOM_USER_META_TABLEDefined() {
		return constDefs.contains("CUSTOM_USER_META_TABLE");
	}

	public void setRELOCATE(boolean initialValue) {
		if (!constDefs.contains("RELOCATE")) {
			RELOCATE = initialValue;
			constDefs.add("RELOCATE");
		}
	}

	public boolean getRELOCATE() {
		return RELOCATE;
	}

	public boolean isRELOCATEDefined() {
		return constDefs.contains("RELOCATE");
	}

	public void setWP_MEMORY_LIMIT(String initialValue) {
		if (!constDefs.contains("WP_MEMORY_LIMIT")) {
			WP_MEMORY_LIMIT = initialValue;
			constDefs.add("WP_MEMORY_LIMIT");
		}
	}

	public String getWP_MEMORY_LIMIT() {
		return WP_MEMORY_LIMIT;
	}

	public boolean isWP_MEMORY_LIMITDefined() {
		return constDefs.contains("WP_MEMORY_LIMIT");
	}

	public void setWP_CACHE(boolean initialValue) {
		if (!constDefs.contains("WP_CACHE")) {
			WP_CACHE = initialValue;
			constDefs.add("WP_CACHE");
		}
	}

	public boolean getWP_CACHE() {
		return WP_CACHE;
	}

	public boolean isWP_CACHEDefined() {
		return constDefs.contains("WP_CACHE");
	}

	public void setLANGDIR(String initialValue) {
		if (!constDefs.contains("LANGDIR")) {
			LANGDIR = initialValue;
			constDefs.add("LANGDIR");
		}
	}

	public String getLANGDIR() {
		return LANGDIR;
	}

	public boolean isLANGDIRDefined() {
		return constDefs.contains("LANGDIR");
	}

	public void setPLUGINDIR(String initialValue) {
		if (!constDefs.contains("PLUGINDIR")) {
			PLUGINDIR = initialValue;
			constDefs.add("PLUGINDIR");
		}
	}

	public String getPLUGINDIR() {
		return PLUGINDIR;
	}

	public boolean isPLUGINDIRDefined() {
		return constDefs.contains("PLUGINDIR");
	}

	public void setCOOKIEHASH(String initialValue) {
		if (!constDefs.contains("COOKIEHASH")) {
			COOKIEHASH = initialValue;
			constDefs.add("COOKIEHASH");
		}
	}

	public String getCOOKIEHASH() {
		return COOKIEHASH;
	}

	public boolean isCOOKIEHASHDefined() {
		return constDefs.contains("COOKIEHASH");
	}

	public void setUSER_COOKIE(String initialValue) {
		if (!constDefs.contains("USER_COOKIE")) {
			USER_COOKIE = initialValue;
			constDefs.add("USER_COOKIE");
		}
	}

	public String getUSER_COOKIE() {
		return USER_COOKIE;
	}

	public boolean isUSER_COOKIEDefined() {
		return constDefs.contains("USER_COOKIE");
	}

	public void setPASS_COOKIE(String initialValue) {
		if (!constDefs.contains("PASS_COOKIE")) {
			PASS_COOKIE = initialValue;
			constDefs.add("PASS_COOKIE");
		}
	}

	public String getPASS_COOKIE() {
		return PASS_COOKIE;
	}

	public boolean isPASS_COOKIEDefined() {
		return constDefs.contains("PASS_COOKIE");
	}

	public void setAUTH_COOKIE(String initialValue) {
		if (!constDefs.contains("AUTH_COOKIE")) {
			AUTH_COOKIE = initialValue;
			constDefs.add("AUTH_COOKIE");
		}
	}

	public String getAUTH_COOKIE() {
		return AUTH_COOKIE;
	}

	public boolean isAUTH_COOKIEDefined() {
		return constDefs.contains("AUTH_COOKIE");
	}

	public void setTEST_COOKIE(String initialValue) {
		if (!constDefs.contains("TEST_COOKIE")) {
			TEST_COOKIE = initialValue;
			constDefs.add("TEST_COOKIE");
		}
	}

	public String getTEST_COOKIE() {
		return TEST_COOKIE;
	}

	public boolean isTEST_COOKIEDefined() {
		return constDefs.contains("TEST_COOKIE");
	}

	public void setCOOKIEPATH(String initialValue) {
		if (!constDefs.contains("COOKIEPATH")) {
			COOKIEPATH = initialValue;
			constDefs.add("COOKIEPATH");
		}
	}

	public String getCOOKIEPATH() {
		return COOKIEPATH;
	}

	public boolean isCOOKIEPATHDefined() {
		return constDefs.contains("COOKIEPATH");
	}

	public void setSITECOOKIEPATH(String initialValue) {
		if (!constDefs.contains("SITECOOKIEPATH")) {
			SITECOOKIEPATH = initialValue;
			constDefs.add("SITECOOKIEPATH");
		}
	}

	public String getSITECOOKIEPATH() {
		return SITECOOKIEPATH;
	}

	public boolean isSITECOOKIEPATHDefined() {
		return constDefs.contains("SITECOOKIEPATH");
	}

	public void setCOOKIE_DOMAIN(String initialValue) {
		if (!constDefs.contains("COOKIE_DOMAIN")) {
			COOKIE_DOMAIN = initialValue;
			constDefs.add("COOKIE_DOMAIN");
		}
	}

	public String getCOOKIE_DOMAIN() {
		return COOKIE_DOMAIN;
	}

	public boolean isCOOKIE_DOMAINDefined() {
		return constDefs.contains("COOKIE_DOMAIN");
	}

	public void setAUTOSAVE_INTERVAL(int initialValue) {
		if (!constDefs.contains("AUTOSAVE_INTERVAL")) {
			AUTOSAVE_INTERVAL = initialValue;
			constDefs.add("AUTOSAVE_INTERVAL");
		}
	}

	public int getAUTOSAVE_INTERVAL() {
		return AUTOSAVE_INTERVAL;
	}

	public boolean isAUTOSAVE_INTERVALDefined() {
		return constDefs.contains("AUTOSAVE_INTERVAL");
	}

	public void setTEMPLATEPATH(String initialValue) {
		if (!constDefs.contains("TEMPLATEPATH")) {
			TEMPLATEPATH = initialValue;
			constDefs.add("TEMPLATEPATH");
		}
	}

	public String getTEMPLATEPATH() {
		return TEMPLATEPATH;
	}

	public boolean isTEMPLATEPATHDefined() {
		return constDefs.contains("TEMPLATEPATH");
	}

	public void setSTYLESHEETPATH(String initialValue) {
		if (!constDefs.contains("STYLESHEETPATH")) {
			STYLESHEETPATH = initialValue;
			constDefs.add("STYLESHEETPATH");
		}
	}

	public String getSTYLESHEETPATH() {
		return STYLESHEETPATH;
	}

	public boolean isSTYLESHEETPATHDefined() {
		return constDefs.contains("STYLESHEETPATH");
	}

	public Object getConstantValue(String constName) {
	    if(!constDefs.contains(constName)) {
	    	return null;
	    } else {
	    	try {
	            Field constField = this.getClass().getField(constName);
	            return constField.get(this);
            }
            catch (Exception ex) {
            	LOG.warn("Trying to retrieve the value of an undefined constant: " + constName);
            	return null;
            }
	    }
    }
}
