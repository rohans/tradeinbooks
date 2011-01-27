/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: IXR_Client.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import static com.numiton.PhpCommonConstants.STRING_FALSE;
import static com.numiton.VarHandling.*;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.string.Strings;

public class IXR_Client implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	= Logger.getLogger(IXR_Client.class.getName());
	public GlobalConsts	          gConsts;
	public GlobalVars	          gVars;
	public String	              server;
	public int	                  port;
	public String	              path;
	public String	              useragent;
	public Object	              response;
	public IXR_Message	          message;
	public boolean	              debug	= false;
	public int	                  timeout;
	
	/** 
	 * Storage place for an error message
	 */
	public IXR_Error	          error;

	public IXR_Client(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String server) {
		this(javaGlobalVariables, javaGlobalConstants, server, "", 80, 0);
	}

	public IXR_Client(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String server, String path) {
		this(javaGlobalVariables, javaGlobalConstants, server, path, 80, 0);
	}

	public IXR_Client(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String server, String path, int port) {
		this(javaGlobalVariables, javaGlobalConstants, server, path, port, 0);
	}

	public IXR_Client(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String server, String path, int port, int timeout) {
		setContext(javaGlobalVariables, javaGlobalConstants);
		Array<String> bits = new Array<String>();
		if (!booleanval(path)) {
            // Assume we have been given a URL instead
			bits = URL.parse_url(server);
			this.server = bits.getValue("host");
			this.port = (isset(bits.getValue("port")) ? intval(bits.getValue("port")) : 80);
			this.path = (isset(bits.getValue("path")) ? bits.getValue("path") : "/");
			
            // Make absolutely sure we have a path
			if (!booleanval(this.path)) {
				this.path = "/";
			}
		}
		else {
			this.server = server;
			this.path = path;
			this.port = port;
		}
		this.useragent = "Incutio XML-RPC";
		this.timeout = timeout;
	}

	public boolean query(Object... vargs) {
		Array<Object> args = new Array<Object>();
		int method = 0;
		IXR_Request request = null;
		Object length = null;
		Object xml = null;
		Object r = null;
		int fp = 0;
		Ref<Integer> errno = new Ref<Integer>();
		Ref<String> errstr = new Ref<String>();
		String contents = null;
		boolean gotFirstLine = false;
		boolean gettingHeaders = false;
		String line = null;
		args = FunctionHandling.func_get_args(vargs);
		method = intval(Array.array_shift(args));
		request = new IXR_Request(gVars, gConsts, method, args);
		length = request.getLength();
		xml = request.getXml();
		r = "\r\n";
		String requestStr = "POST " + this.path + " HTTP/1.0" + strval(r);
		requestStr = requestStr + "Host: " + this.server + strval(r);
		requestStr = requestStr + "Content-Type: text/xml" + strval(r);
		requestStr = requestStr + "User-Agent: " + this.useragent + strval(r);
		requestStr = requestStr + "Content-length: " + strval(length) + strval(r) + strval(r);
		requestStr = requestStr + strval(xml);
        // Now send the request
		if (this.debug) {
			echo(gVars.webEnv, "<pre>" + Strings.htmlspecialchars(requestStr) + "\n</pre>\n\n");
		}
		if (booleanval(this.timeout)) {
			fp = FileSystemOrSocket.fsockopen(gVars.webEnv, this.server, this.port, errno, errstr, this.timeout);
		}
		else {
			fp = FileSystemOrSocket.fsockopen(gVars.webEnv, this.server, this.port, errno, errstr);
		}
		if (!booleanval(fp)) {
			this.error = new IXR_Error(gVars, gConsts, -32300, "transport error - could not open socket: " + strval(errno.value) + " " + errstr.value);
			return false;
		}
		FileSystemOrSocket.fputs(gVars.webEnv, fp, requestStr);
		contents = "";
		gotFirstLine = false;
		gettingHeaders = true;
		while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
			line = FileSystemOrSocket.fgets(gVars.webEnv, fp, 4096);
			if (!gotFirstLine) {
                // Check line for '200'
				if (strictEqual(Strings.strstr(line, "200"), STRING_FALSE)) {
					this.error = new IXR_Error(gVars, gConsts, -32300, "transport error - HTTP status code was not 200");
					return false;
				}
				gotFirstLine = true;
			}
			if (equal(Strings.trim(line), "")) {
				gettingHeaders = false;
			}
			if (!gettingHeaders) {
				contents = contents + Strings.trim(line) + "\n";
			}
		}
		if (this.debug) {
			echo(gVars.webEnv, "<pre>" + Strings.htmlspecialchars(contents) + "\n</pre>\n\n");
		}
        // Now parse what we've got back
		this.message = new IXR_Message(gVars, gConsts, contents);
		if (!this.message.parse()) {
            // XML error
			this.error = new IXR_Error(gVars, gConsts, -32700, "parse error. not well formed");
			return false;
		}
        // Is the message a fault?
		if (equal(this.message.messageType, "fault")) {
			this.error = new IXR_Error(gVars, gConsts, this.message.faultCode, this.message.faultString);
			return false;
		}
        // Message must be OK
		return true;
	}

	public Object getResponse() {
        // methodResponses can only have one param - return that
		return this.message.params.getValue(0);
	}

	public boolean isError() {
		return is_object(this.error);
	}

	public int getErrorCode() {
		return this.error.code;
	}

	public String getErrorMessage() {
		return this.error.message;
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
