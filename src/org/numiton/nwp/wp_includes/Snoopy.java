/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Snoopy.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.VarHandling.*;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QProgramExecution;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;

/**
 * Snoopy - the PHP net client
 * @author Monte Ohrt <monte@ispi.net>
 * @copyright 1999-2000 ispi, all rights reserved
 * @version 1.01
 * @license GNU Lesser GPL
 * @link http://snoopy.sourceforge.net/
 * @package Snoopy
 */
/*************************************************

Snoopy - the PHP net client
Author: Monte Ohrt <monte@ispi.net>
Copyright (c): 1999-2000 ispi, all rights reserved
Version: 1.01

 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

You may contact the author of Snoopy by e-mail at:
monte@ispi.net

Or, write to:
Monte Ohrt
CTO, ispi
237 S. 70th suite 220
Lincoln, NE 68510

The latest version of Snoopy can be obtained from:
http://snoopy.sourceforge.net/

*************************************************/
public class Snoopy implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	       LOG	            = Logger.getLogger(Snoopy.class.getName());
	public GlobalConsts	                   gConsts;
	public GlobalVars	                   gVars;
	
	/**** Public variables ****/

	/* user definable vars */

	public String	                       host	            = "www.php.net";// host name we are connecting to
	public int	                           port	            = 80;// port we are connecting to
	public String	                       proxy_host	    = "";//  proxy host to use
	public int	                           proxy_port	    = 0;//  proxy port to use
	public String	                       proxy_user	    = "";//  proxy user to use
	public String	                       proxy_pass	    = "";//  proxy password to use
	public String	                       agent	        = "Snoopy v1.2.3";// agent we masquerade as
	public String	                       referer	        = "";//  referer info to pass
	public Object	                       cookies;	// array of cookies to pass
													// $cookies["username"]="joe";
	/* Do not change type */public Object	rawheaders;	// array of raw headers to send
														// $rawheaders["Content-type"]="text/html";
	
	/* Do not change type */public int	   maxredirs	    = 5;// http redirection depth maximum. 0 = disallow
	public String	                       lastredirectaddr	= "";// contains address of last redirected address
	public boolean	                       offsiteok	    = true;// allows redirection off-site
	public int	                           maxframes	    = 0;// frame content depth maximum. 0 = disallow
	public boolean	                       expandlinks	    = true;	// expand links to fully qualified URLs.
																	// this only applies to fetchlinks()
																	// submitlinks(), and submittext()
	public boolean	                       passcookies	    = true;	// pass set cookies back through redirects
																	// NOTE: this currently does not respect
																	// dates, domains or paths.
	public String	                       user	            = "";//  user for http authentication
	public String	                       pass	            = "";// password for http authentication
	
	// http accept types
	public String	                       accept	        = "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, */*";
	public Object	                       results;//  where the content is put
	/* Do not change type */public String	error	        = "";//  error messages sent here error messages sent here
	public String	                       response_code	= "";//  response code returned from server response code returned from server
	public Array<Object>	               headers	        = new Array<Object>();// headers returned from server sent here
	public int	                           maxlength	    = 8192;// max return data length (body)
	public int	                           read_timeout	    = 0;	// timeout on read operations, in seconds
																	// supported only since PHP 4 Beta 4
																	// set to 0 to disallow timeouts
	public boolean	                       timed_out	    = false;//  if a read operation timed out
	public int	                           status	        = 0;//  http request status
	public String	                       temp_dir	        = "/tmp";	// temporary directory that the webserver
																		// has permission to write to.
																		// under Windows, this should be C:\temp
	public String	                       curl_path	    = "/usr/local/bin/curl";// Snoopy will use cURL for fetching
																					// SSL content if a full system path to
																					// the cURL binary is supplied here.
																					// set to false if you do not have
																					// cURL installed. See http://curl.haxx.se
																					// for details on installing cURL.
																					// Snoopy does *not* use the cURL
																					// library functions built into php,
																					// as these functions are not stable
																					// as of this Snoopy release.
	/**** Private variables ****/

	public int	                           _maxlinelen	    = 4096;// max line length (headers)
	public String	                       _httpmethod	    = "GET";//  default http request method
	public String	                       _httpversion	    = "HTTP/1.0";//  default http request version
	public String	                       _submit_method	= "POST";//  default submit method
	public String	                       _submit_type	    = "application/x-www-form-urlencoded";//  default submit type
	public String	                       _mime_boundary	= "";// MIME boundary for multipart/form-data submit type
	public String	                       _redirectaddr	= "";// will be set if page fetched is a redirect
	public int	                           _redirectdepth	= 0;//  increments on an http redirect
	public Array<Object>	               _frameurls	    = new Array<Object>();//  frame src urls
	public int	                           _framedepth	    = 0;//  increments on frame depth
	public boolean	                       _isproxy	        = false;//  set if using a proxy server
	public int	                           _fp_timeout	    = 30;// timeout for socket connection

	public Snoopy(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
		setContext(javaGlobalVariables, javaGlobalConstants);
	}
	/** 
	 * Generated in place of local variable 'frameurl' from method 'fetch'
	 * because it is used inside an inner class.
	 */
	Object	fetch_frameurl	= null;

/*======================================================================*\
	Function:	fetch
	Purpose:	fetch the contents of a web page
				(and possibly other protocols in the
				future like ftp, nntp, gopher, etc.)
	Input:		$URI	the location of the page to fetch
	Output:		$this->results	the output text from the fetch
\*======================================================================*/
	public boolean fetch(String URI) {
		Array<String> URI_PARTS = new Array<String>();
		Ref<Integer> fp = new Ref<Integer>();
		String path = null;
		Array<Object> frameurls = new Array<Object>();
		
		//preg_match("|^([^:]+)://([^:/]+)(:[\d]+)*(.*)|",$URI,$URI_PARTS);
		URI_PARTS = URL.parse_url(URI);
		if (!empty(URI_PARTS.getValue("user"))) {
			this.user = URI_PARTS.getValue("user");
		}
		if (!empty(URI_PARTS.getValue("pass"))) {
			this.pass = URI_PARTS.getValue("pass");
		}
		if (empty(URI_PARTS.getValue("query"))) {
			URI_PARTS.putValue("query", "");
		}
		if (empty(URI_PARTS.getValue("path"))) {
			URI_PARTS.putValue("path", "");
		}
		{
			int javaSwitchSelector55 = 0;
			if (equal(Strings.strtolower(URI_PARTS.getValue("scheme")), "http"))
				javaSwitchSelector55 = 1;
			if (equal(Strings.strtolower(URI_PARTS.getValue("scheme")), "https"))
				javaSwitchSelector55 = 2;
			switch (javaSwitchSelector55) {
				case 1: {
					this.host = URI_PARTS.getValue("host");
					if (!empty(URI_PARTS.getValue("port"))) {
						this.port = intval(URI_PARTS.getValue("port"));
					}
					if (this._connect(fp)) {
						if (this._isproxy) {
							// using proxy, send entire URI
							this._httprequest(URI, fp.value, URI, this._httpmethod);
						}
						else {
							path = URI_PARTS.getValue("path") + (booleanval(URI_PARTS.getValue("query")) ? ("?" + URI_PARTS.getValue("query")) : "");
							// no proxy, send only the path
							this._httprequest(path, fp.value, URI, this._httpmethod);
						}
						this._disconnect(fp.value);
						if (booleanval(this._redirectaddr)) {
							/* url was redirected, check if we've hit the max depth */
							if (this.maxredirs > this._redirectdepth) {
								// only follow redirect if it's on this site, or offsiteok is true
								if (QRegExPerl.preg_match("|^http://" + RegExPerl.preg_quote(this.host) + "|i", this._redirectaddr) || this.offsiteok) {
									/* follow the redirect */
									this._redirectdepth++;
									this.lastredirectaddr = this._redirectaddr;
									this.fetch(this._redirectaddr);
								}
							}
						}
						if (this._framedepth < this.maxframes && Array.count(this._frameurls) > 0) {
							frameurls = this._frameurls;
							this._frameurls = new Array<Object>();
							while (booleanval(new ListAssigner<Object>() {
								public Array<Object> doAssign(Array<Object> srcArray) {
									if (strictEqual(srcArray, null)) {
										return null;
									}
									fetch_frameurl = srcArray.getValue(1);
									return srcArray;
								}
							}.doAssign(Array.each(frameurls)))) {
								if (this._framedepth < this.maxframes) {
									this.fetch(strval(fetch_frameurl));
									this._framedepth++;
								}
								else
									break;
							}
						}
					}
					else {
						return false;
					}
					return true;
				}
				case 2: {
					if (!booleanval(this.curl_path)) {
						return false;
					}
					if (true)
					/*Modified by Numiton*/
					{
						if (!JFileSystemOrSocket.is_executable(gVars.webEnv, this.curl_path)) {
							return false;
						}
					}
					this.host = URI_PARTS.getValue("host");
					if (!empty(URI_PARTS.getValue("port"))) {
						this.port = intval(URI_PARTS.getValue("port"));
					}
					if (this._isproxy) {
						// using proxy, send entire URI
						this._httpsrequest(URI, URI, this._httpmethod);
					}
					else {
						path = URI_PARTS.getValue("path") + (booleanval(URI_PARTS.getValue("query")) ? ("?" + URI_PARTS.getValue("query")) : "");
						// no proxy, send only the path
						this._httpsrequest(path, URI, this._httpmethod);
					}
					if (booleanval(this._redirectaddr)) {
						/* url was redirected, check if we've hit the max depth */
						if (this.maxredirs > this._redirectdepth) {
							// only follow redirect if it's on this site, or offsiteok is true
							if (QRegExPerl.preg_match("|^http://" + RegExPerl.preg_quote(this.host) + "|i", this._redirectaddr) || this.offsiteok) {
								/* follow the redirect */
								this._redirectdepth++;
								this.lastredirectaddr = this._redirectaddr;
								this.fetch(this._redirectaddr);
							}
						}
					}
					if (this._framedepth < this.maxframes && Array.count(this._frameurls) > 0) {
						frameurls = this._frameurls;
						this._frameurls = new Array<Object>();
						while (booleanval(new ListAssigner<Object>() {
							public Array<Object> doAssign(Array<Object> srcArray) {
								if (strictEqual(srcArray, null)) {
									return null;
								}
								fetch_frameurl = srcArray.getValue(1);
								return srcArray;
							}
						}.doAssign(Array.each(frameurls)))) {
							if (this._framedepth < this.maxframes) {
								this.fetch(strval(fetch_frameurl));
								this._framedepth++;
							}
							else
								break;
						}
					}
					return true;
				}
				default: {
					// not a valid protocol
					this.error = "Invalid protocol \"" + URI_PARTS.getValue("scheme") + "\"\\n";
					return false;
				}
			}
		}
	}
	//		return true;
	/** 
	 * Generated in place of local variable 'frameurl' from method 'submit'
	 * because it is used inside an inner class.
	 */
	Object	submit_frameurl	= null;

/*======================================================================*\
	Function:	submit
	Purpose:	submit an http form
	Input:		$URI	the location to post the data
				$formvars	the formvars to use.
					format: $formvars["var"] = "val";
				$formfiles  an array of files to submit
					format: $formfiles["var"] = "/dir/filename.ext";
	Output:		$this->results	the text output from the post
\*======================================================================*/

	public boolean submit(String URI, Object formvars, Object formfiles) {
		String postdata = null;
		Array<String> URI_PARTS = new Array<String>();
		Ref<Integer> fp = new Ref<Integer>();
		String path = null;
		Array<Object> frameurls = new Array<Object>();
		postdata = null;
		postdata = this._prepare_post_body(formvars, formfiles);
		URI_PARTS = URL.parse_url(URI);
		if (!empty(URI_PARTS.getValue("user"))) {
			this.user = URI_PARTS.getValue("user");
		}
		if (!empty(URI_PARTS.getValue("pass"))) {
			this.pass = URI_PARTS.getValue("pass");
		}
		if (empty(URI_PARTS.getValue("query"))) {
			URI_PARTS.putValue("query", "");
		}
		if (empty(URI_PARTS.getValue("path"))) {
			URI_PARTS.putValue("path", "");
		}
		{
			int javaSwitchSelector56 = 0;
			if (equal(Strings.strtolower(URI_PARTS.getValue("scheme")), "http"))
				javaSwitchSelector56 = 1;
			if (equal(Strings.strtolower(URI_PARTS.getValue("scheme")), "https"))
				javaSwitchSelector56 = 2;
			switch (javaSwitchSelector56) {
				case 1: {
					this.host = URI_PARTS.getValue("host");
					if (!empty(URI_PARTS.getValue("port"))) {
						this.port = intval(URI_PARTS.getValue("port"));
					}
					if (this._connect(fp)) {
						if (this._isproxy) {
							// using proxy, send entire URI
							this._httprequest(URI, fp.value, URI, this._submit_method, this._submit_type, postdata);
						}
						else {
							path = URI_PARTS.getValue("path") + (booleanval(URI_PARTS.getValue("query")) ? ("?" + URI_PARTS.getValue("query")) : "");
							// no proxy, send only the path
							this._httprequest(path, fp.value, URI, this._submit_method, this._submit_type, postdata);
						}
						this._disconnect(fp.value);
						if (booleanval(this._redirectaddr)) {
							/* url was redirected, check if we've hit the max depth */
							
							if (this.maxredirs > this._redirectdepth) {
								if (!QRegExPerl.preg_match("|^" + URI_PARTS.getValue("scheme") + "://|", this._redirectaddr)) {
									this._redirectaddr = this._expandlinks(this._redirectaddr, URI_PARTS.getValue("scheme") + "://" + URI_PARTS.getValue("host"));
								}
								
								// only follow redirect if it's on this site, or offsiteok is true
								if (QRegExPerl.preg_match("|^http://" + RegExPerl.preg_quote(this.host) + "|i", this._redirectaddr) || this.offsiteok) {
									/* follow the redirect */
									this._redirectdepth++;
									this.lastredirectaddr = this._redirectaddr;
									if (Strings.strpos(this._redirectaddr, "?") > 0) {
										this.fetch(this._redirectaddr); // the redirect has changed the request method from post to get
									}
									else
										this.submit(this._redirectaddr, formvars, formfiles);
								}
							}
						}
						if (this._framedepth < this.maxframes && Array.count(this._frameurls) > 0) {
							frameurls = this._frameurls;
							this._frameurls = new Array<Object>();
							while (booleanval(new ListAssigner<Object>() {
								public Array<Object> doAssign(Array<Object> srcArray) {
									if (strictEqual(srcArray, null)) {
										return null;
									}
									submit_frameurl = srcArray.getValue(1);
									return srcArray;
								}
							}.doAssign(Array.each(frameurls)))) {
								if (this._framedepth < this.maxframes) {
									this.fetch(strval(submit_frameurl));
									this._framedepth++;
								}
								else
									break;
							}
						}
					}
					else {
						return false;
					}
					return true;
				}
				case 2: {
					if (!booleanval(this.curl_path)) {
						return false;
					}
					if (true)
					/*Modified by Numiton*/
					{
						if (!JFileSystemOrSocket.is_executable(gVars.webEnv, this.curl_path)) {
							return false;
						}
					}
					this.host = URI_PARTS.getValue("host");
					if (!empty(URI_PARTS.getValue("port"))) {
						this.port = intval(URI_PARTS.getValue("port"));
					}
					if (this._isproxy) {
						// using proxy, send entire URI
						this._httpsrequest(URI, URI, this._submit_method, this._submit_type, postdata);
					}
					else {
						path = URI_PARTS.getValue("path") + (booleanval(URI_PARTS.getValue("query")) ? ("?" + URI_PARTS.getValue("query")) : "");
						// no proxy, send only the path
						this._httpsrequest(path, URI, this._submit_method, this._submit_type, postdata);
					}
					if (booleanval(this._redirectaddr)) {
						/* url was redirected, check if we've hit the max depth */
						if (this.maxredirs > this._redirectdepth) {
							if (!QRegExPerl.preg_match("|^" + URI_PARTS.getValue("scheme") + "://|", this._redirectaddr)) {
								this._redirectaddr = this._expandlinks(this._redirectaddr, URI_PARTS.getValue("scheme") + "://" + URI_PARTS.getValue("host"));
							}
							
							// only follow redirect if it's on this site, or offsiteok is true
							if (QRegExPerl.preg_match("|^http://" + RegExPerl.preg_quote(this.host) + "|i", this._redirectaddr) || this.offsiteok) {
								/* follow the redirect */
								this._redirectdepth++;
								this.lastredirectaddr = this._redirectaddr;
								if (Strings.strpos(this._redirectaddr, "?") > 0) {
									this.fetch(this._redirectaddr); // the redirect has changed the request method from post to get
								}
								else
									this.submit(this._redirectaddr, formvars, formfiles);
							}
						}
					}
					if (this._framedepth < this.maxframes && Array.count(this._frameurls) > 0) {
						frameurls = this._frameurls;
						this._frameurls = new Array<Object>();
						while (booleanval(new ListAssigner<Object>() {
							public Array<Object> doAssign(Array<Object> srcArray) {
								if (strictEqual(srcArray, null)) {
									return null;
								}
								submit_frameurl = srcArray.getValue(1);
								return srcArray;
							}
						}.doAssign(Array.each(frameurls)))) {
							if (this._framedepth < this.maxframes) {
								this.fetch(strval(submit_frameurl));
								this._framedepth++;
							}
							else
								break;
						}
					}
					return true;
				}
				default: {
					// not a valid protocol
					this.error = "Invalid protocol \"" + URI_PARTS.getValue("scheme") + "\"\\n";
					return false;
				}
			}
		}
		
//		return true;
	}

/*======================================================================*\
	Function:	fetchlinks
	Purpose:	fetch the links from a web page
	Input:		$URI	where you are fetching from
	Output:		$this->results	an array of the URLs
\*======================================================================*/
	public boolean fetchlinks(String URI) {
		int x = 0;
		if (this.fetch(URI)) {
			if (booleanval(this.lastredirectaddr)) {
				URI = this.lastredirectaddr;
			}
			if (is_array(this.results)) {
				for (x = 0; x < Array.count(this.results); x++)
					((Array) this.results).putValue(x, this._striplinks(strval(((Array) this.results).getValue(x))));
			}
			else
				this.results = strval(this._striplinks(strval(this.results)));
			if (this.expandlinks) {
				this.results = this._expandlinks(strval(this.results), URI);
			}
			return true;
		}
		else
			return false;
	}

/*======================================================================*\
	Function:	fetchform
	Purpose:	fetch the form elements from a web page
	Input:		$URI	where you are fetching from
	Output:		$this->results	the resulting html form
\*======================================================================*/
	public boolean fetchform(String URI) {
		int x = 0;
		if (this.fetch(URI)) {
			if (is_array(this.results)) {
				for (x = 0; x < Array.count(this.results); x++)
					((Array) this.results).putValue(x, this._stripform(strval(((Array) this.results).getValue(x))));
			}
			else
				this.results = this._stripform(strval(this.results));
			return true;
		}
		else
			return false;
	}

/*======================================================================*\
	Function:	fetchtext
	Purpose:	fetch the text from a web page, stripping the links
	Input:		$URI	where you are fetching from
	Output:		$this->results	the text from the web page
\*======================================================================*/
	public boolean fetchtext(String URI) {
		int x = 0;
		if (this.fetch(URI)) {
			if (is_array(this.results)) {
				for (x = 0; x < Array.count(this.results); x++)
					((Array) this.results).putValue(x, this._striptext(strval(((Array) this.results).getValue(x))));
			}
			else
				this.results = this._striptext(strval(this.results));
			return true;
		}
		else
			return false;
	}


	/*======================================================================*\
		Function:	submitlinks
		Purpose:	grab links from a form submission
		Input:		$URI	where you are submitting from
		Output:		$this->results	an array of the links from the post
	\*======================================================================*/

	public boolean submitlinks(String URI, Object formvars, Object formfiles) {
		int x = 0;
		if (this.submit(URI, formvars, formfiles)) {
			if (booleanval(this.lastredirectaddr)) {
				URI = this.lastredirectaddr;
			}
			if (is_array(this.results)) {
				for (x = 0; x < Array.count(this.results); x++) {
					((Array) this.results).putValue(x, this._striplinks(strval(((Array) this.results).getValue(x))));
					if (this.expandlinks) {
						((Array) this.results).putValue(x, this._expandlinks(((Array) this.results).getArrayValue(x), URI));
					}
				}
			}
			else {
				this.results = this._striplinks(strval(this.results));
				if (this.expandlinks) {
					this.results = this._expandlinks((Array) this.results, URI);
				}
			}
			return true;
		}
		else
			return false;
	}


	/*======================================================================*\
		Function:	submittext
		Purpose:	grab text from a form submission
		Input:		$URI	where you are submitting from
		Output:		$this->results	the text from the web page
	\*======================================================================*/

	public boolean submittext(String URI, Object formvars, Object formfiles) {
		int x = 0;
		if (this.submit(URI, formvars, formfiles)) {
			if (booleanval(this.lastredirectaddr)) {
				URI = this.lastredirectaddr;
			}
			if (is_array(this.results)) {
				for (x = 0; x < Array.count(this.results); x++) {
					((Array) this.results).putValue(x, this._striptext(strval(((Array) this.results).getValue(x))));
					if (this.expandlinks) {
						((Array) this.results).putValue(x, this._expandlinks(((Array) this.results).getArrayValue(x), URI));
					}
				}
			}
			else {
				this.results = this._striptext(strval(this.results));
				if (this.expandlinks) {
					this.results = this._expandlinks((Array) this.results, URI);
				}
			}
			return true;
		}
		else
			return false;
	}


	/*======================================================================*\
		Function:	set_submit_multipart
		Purpose:	Set the form submission content type to
					multipart/form-data
	\*======================================================================*/
	public void set_submit_multipart() {
		this._submit_type = "multipart/form-data";
	}


	/*======================================================================*\
		Function:	set_submit_normal
		Purpose:	Set the form submission content type to
					application/x-www-form-urlencoded
	\*======================================================================*/
	public void set_submit_normal() {
		this._submit_type = "application/x-www-form-urlencoded";
	}
	/** 
	 * Generated in place of local variable 'val' from method '_striplinks'
	 * because it is used inside an inner class.
	 */
	Object	_striplinks_val	= null;
	/** 
	 * Generated in place of local variable 'key' from method '_striplinks'
	 * because it is used inside an inner class.
	 */
	Object	_striplinks_key	= null;


	/*======================================================================*\
		Private functions
	\*======================================================================*/


	/*======================================================================*\
		Function:	_striplinks
		Purpose:	strip the hyperlinks from an html document
		Input:		$document	document to strip.
		Output:		$match		an array of the links
	\*======================================================================*/
	public Array<Object> _striplinks(String document) {
		Array links = new Array();
		Array<Object> match = new Array<Object>();
		RegExPerl
		        .preg_match_all(
		                "\'<\\s*a\\s.*?href\\s*=\\s*\t\t\t# find <a href=\n\t\t\t\t\t\t([\"\\\'])?\t\t\t\t\t# find single or double quote\n\t\t\t\t\t\t(?(1) (.*?)\\1 | ([^\\s\\>]+))\t\t# if quote found, match up to next matching\n\t\t\t\t\t\t\t\t\t\t\t\t\t# quote, otherwise match up to next space\n\t\t\t\t\t\t\'isx",
		                document, links);
		while (booleanval(new ListAssigner<Object>() {
			public Array<Object> doAssign(Array<Object> srcArray) {
				if (strictEqual(srcArray, null)) {
					return null;
				}
				_striplinks_key = srcArray.getValue(0);
				_striplinks_val = srcArray.getValue(1);
				return srcArray;
			}
		}.doAssign(Array.each(links.getArrayValue(2))))) {
			if (!empty(_striplinks_val)) {
				match.putValue(_striplinks_val);
			}
		}
		
		// catenate the non-empty matches from the conditional subpattern
		while (booleanval(new ListAssigner<Object>() {
			public Array<Object> doAssign(Array<Object> srcArray) {
				if (strictEqual(srcArray, null)) {
					return null;
				}
				_striplinks_key = srcArray.getValue(0);
				_striplinks_val = srcArray.getValue(1);
				return srcArray;
			}
		}.doAssign(Array.each(links.getArrayValue(3))))) {
			if (!empty(_striplinks_val)) {
				match.putValue(_striplinks_val);
			}
		}
		
		// return the links
		return match;
	}

/*======================================================================*\
	Function:	_stripform
	Purpose:	strip the form elements from an html document
	Input:		$document	document to strip.
	Output:		$match		an array of the links
\*======================================================================*/

	public String _stripform(String document) {
		Array elements = new Array();
		String match = null;
		
		QRegExPerl.preg_match_all("\'<\\/?(FORM|INPUT|SELECT|TEXTAREA|(OPTION))[^<>]*>(?(2)(.*(?=<\\/?(option|select)[^<>]*>[\r\n]*)|(?=[\r\n]*))|(?=[\r\n]*))\'Usi", document, elements);
		
		// catenate the matches
		match = Strings.implode("\r\n", elements.getArrayValue(0));
		
		// return the links
		return match;
	}


	/*======================================================================*\
		Function:	_striptext
		Purpose:	strip the text from an html document
		Input:		$document	document to strip.
		Output:		$text		the resulting text
	\*======================================================================*/

	public String _striptext(String document) {
		Array<String> search;
		Array<String> replace;
		String text;
		
		// I didn't use preg eval (//e) since that is only available in PHP 4.0.
		// so, list your entities one by one here. I included some of the
		// more common ones.
		
		search = new Array<String>(
                new ArrayEntry<String>("\'<script[^>]*?>.*?</script>\'si"),
                new ArrayEntry<String>("\'<[\\/\\!]*?[^<>]*?>\'si"),
                new ArrayEntry<String>("\'([\r\n])[\\s]+\'"),
                new ArrayEntry<String>("\'&(quot|#34|#034|#x22);\'i"),
                new ArrayEntry<String>("\'&(amp|#38|#038|#x26);\'i"),
                new ArrayEntry<String>("\'&(lt|#60|#060|#x3c);\'i"),
                new ArrayEntry<String>("\'&(gt|#62|#062|#x3e);\'i"),
                new ArrayEntry<String>("\'&(nbsp|#160|#xa0);\'i"),
                new ArrayEntry<String>("\'&(iexcl|#161);\'i"),
                new ArrayEntry<String>("\'&(cent|#162);\'i"),
                new ArrayEntry<String>("\'&(pound|#163);\'i"),
                new ArrayEntry<String>("\'&(copy|#169);\'i"),
                new ArrayEntry<String>("\'&(reg|#174);\'i"),
                new ArrayEntry<String>("\'&(deg|#176);\'i"),
                new ArrayEntry<String>("\'&(#39|#039|#x27);\'"),
                new ArrayEntry<String>("\'&(euro|#8364);\'i"),
                new ArrayEntry<String>("\'&a(uml|UML);\'"),
                new ArrayEntry<String>("\'&o(uml|UML);\'"),
                new ArrayEntry<String>("\'&u(uml|UML);\'"),
                new ArrayEntry<String>("\'&A(uml|UML);\'"),
                new ArrayEntry<String>("\'&O(uml|UML);\'"),
                new ArrayEntry<String>("\'&U(uml|UML);\'"),
                new ArrayEntry<String>("\'&szlig;\'i"));
        replace = new Array<String>(
                new ArrayEntry<String>(""),
                new ArrayEntry<String>(""),
                new ArrayEntry<String>("\\1"),
                new ArrayEntry<String>("\""),
                new ArrayEntry<String>("&"),
                new ArrayEntry<String>("<"),
                new ArrayEntry<String>(">"),
                new ArrayEntry<String>(" "),
                new ArrayEntry<String>(Strings.chr(161)),
                new ArrayEntry<String>(Strings.chr(162)),
                new ArrayEntry<String>(Strings.chr(163)),
                new ArrayEntry<String>(Strings.chr(169)),
                new ArrayEntry<String>(Strings.chr(174)),
                new ArrayEntry<String>(Strings.chr(176)),
                new ArrayEntry<String>(Strings.chr(39)),
                new ArrayEntry<String>(Strings.chr(128)),
                new ArrayEntry<String>("ä"),
                new ArrayEntry<String>("ö"),
                new ArrayEntry<String>("ü"),
                new ArrayEntry<String>("Ä"),
                new ArrayEntry<String>("Ö"),
                new ArrayEntry<String>("Ü"),
                new ArrayEntry<String>("ß"));
        text = QRegExPerl.preg_replace(search, replace, document);
		return text;
	}


	/*======================================================================*\
		Function:	_expandlinks
		Purpose:	expand each link into a fully qualified URL
		Input:		$links			the links to qualify
					$URI			the full URI to get the base from
		Output:		$expandedLinks	the expanded links
	\*======================================================================*/
	public String _expandlinks(String links, String URI) {
		Array match = new Array();
		Array<String> match_part = new Array<String>();
		String match_root = null;
		Array search = new Array<Object>();
		Array replace = new Array<Object>();
		String expandedLinks;
		QRegExPerl.preg_match("/^[^\\?]+/", URI, match);
		String matchStr = QRegExPerl.preg_replace("|/[^\\/\\.]+\\.[^\\/\\.]+$|", "", strval(match.getValue(0)));
		matchStr = QRegExPerl.preg_replace("|/$|", "", matchStr);
		match_part = URL.parse_url(matchStr);
		match_root = match_part.getValue("scheme") + "://" + match_part.getValue("host");
		search = new Array<Object>(new ArrayEntry<Object>("|^http://" + RegExPerl.preg_quote(this.host) + "|i"), new ArrayEntry<Object>("|^(\\/)|i"), new ArrayEntry<Object>(
		        "|^(?!http://)(?!mailto:)|i"), new ArrayEntry<Object>("|/\\./|"), new ArrayEntry<Object>("|/[^\\/]+/\\.\\./|"));
		replace = new Array<Object>(new ArrayEntry<Object>(""), new ArrayEntry<Object>(match_root + "/"), new ArrayEntry<Object>(matchStr + "/"), new ArrayEntry<Object>("/"), new ArrayEntry<Object>(
		        "/"));
		expandedLinks = QRegExPerl.preg_replace(search, replace, links);
		return expandedLinks;
	}

	public Array<String> _expandlinks(Array<String> links, String URI) {
		Array match = new Array();
		Array<String> match_part = new Array<String>();
		String match_root = null;
		Array search = new Array<Object>();
		Array replace = new Array<Object>();
		Array<String> expandedLinks = new Array<String>();
		QRegExPerl.preg_match("/^[^\\?]+/", URI, match);
		String matchStr = QRegExPerl.preg_replace("|/[^\\/\\.]+\\.[^\\/\\.]+$|", "", strval(match.getValue(0)));
		matchStr = QRegExPerl.preg_replace("|/$|", "", matchStr);
		match_part = URL.parse_url(matchStr);
		match_root = match_part.getValue("scheme") + "://" + match_part.getValue("host");
		search = new Array<Object>(new ArrayEntry<Object>("|^http://" + RegExPerl.preg_quote(this.host) + "|i"), new ArrayEntry<Object>("|^(\\/)|i"), new ArrayEntry<Object>(
		        "|^(?!http://)(?!mailto:)|i"), new ArrayEntry<Object>("|/\\./|"), new ArrayEntry<Object>("|/[^\\/]+/\\.\\./|"));
		replace = new Array<Object>(new ArrayEntry<Object>(""), new ArrayEntry<Object>(match_root + "/"), new ArrayEntry<Object>(matchStr + "/"), new ArrayEntry<Object>("/"), new ArrayEntry<Object>(
		        "/"));
		expandedLinks = QRegExPerl.preg_replace(search, replace, links);
		return expandedLinks;
	}
	/** 
	 * Generated in place of local variable 'headerKey' from method
	 * '_httprequest' because it is used inside an inner class.
	 */
	Object	_httprequest_headerKey	= null;
	/** 
	 * Generated in place of local variable 'headerVal' from method
	 * '_httprequest' because it is used inside an inner class.
	 */
	Object	_httprequest_headerVal	= null;

	public boolean _httprequest(String url, int fp, String URI, String http_method) {
		return _httprequest(url, fp, URI, http_method, "", "");
	}

	public boolean _httprequest(String url, int fp, String URI, String http_method, String content_type) {
		return _httprequest(url, fp, URI, http_method, content_type, "");
	}


	/*======================================================================*\
		Function:	_httprequest
		Purpose:	go get the http data from the server
		Input:		$url		the url to fetch
					$fp			the current open file pointer
					$URI		the full URI
					$body		body contents to send if any (POST)
		Output:
	\*======================================================================*/

	public boolean _httprequest(String url, int fp, String URI, String http_method, String content_type, String body) {
		String cookie_headers = null;
		Array<String> URI_PARTS = new Array<String>();
		String headers = null;
		Object cookieKey = null;
		String cookieVal = null;
		String currentHeader = null;
		Array<Object> matches = new Array<Object>();
		Array<Object> status = new Array<Object>();
		String results = null;
		String _data = null;
		Array match = new Array();
		int x = 0;
		cookie_headers = "";
		if (this.passcookies && booleanval(this._redirectaddr)) {
			this.setcookies();
		}
		URI_PARTS = URL.parse_url(URI);
		if (empty(url)) {
			url = "/";
		}
		headers = http_method + " " + url + " " + this._httpversion + "\r\n";
		if (!empty(this.agent)) {
			headers = headers + "User-Agent: " + this.agent + "\r\n";
		}
		if (!empty(this.host) && !isset(((Array) this.rawheaders).getValue("Host"))) {
			headers = headers + "Host: " + this.host;
			if (!empty(this.port) && !equal(this.port, 80)) {
				headers = headers + ":" + strval(this.port);
			}
			headers = headers + "\r\n";
		}
		if (!empty(this.accept)) {
			headers = headers + "Accept: " + this.accept + "\r\n";
		}
		if (!empty(this.referer)) {
			headers = headers + "Referer: " + this.referer + "\r\n";
		}
		if (!empty(this.cookies)) {
			if (!is_array(this.cookies)) {
				this.cookies = new Array<Object>(this.cookies);
			}
			Array.reset((Array) this.cookies);
			if (Array.count(this.cookies) > 0) {
				cookie_headers = cookie_headers + "Cookie: ";
				for (Map.Entry javaEntry409 : ((Array<?>) this.cookies).entrySet()) {
					cookieKey = javaEntry409.getKey();
					cookieVal = strval(javaEntry409.getValue());
					cookie_headers = cookie_headers + strval(cookieKey) + "=" + URL.urlencode(cookieVal) + "; ";
				}
				headers = headers + Strings.substr(cookie_headers, 0, -2) + "\r\n";
			}
		}
		if (!empty(this.rawheaders)) {
			if (!is_array(this.rawheaders)) {
				this.rawheaders = new Array<Object>(this.rawheaders);
			}
			while (booleanval(new ListAssigner<Object>() {
				public Array<Object> doAssign(Array<Object> srcArray) {
					if (strictEqual(srcArray, null)) {
						return null;
					}
					_httprequest_headerKey = srcArray.getValue(0);
					_httprequest_headerVal = srcArray.getValue(1);
					return srcArray;
				}
			}.doAssign(Array.each((Array) this.rawheaders))))
				headers = headers + strval(_httprequest_headerKey) + ": " + strval(_httprequest_headerVal) + "\r\n";
		}
		if (!empty(content_type)) {
			headers = headers + "Content-type: " + content_type;
			if (equal(content_type, "multipart/form-data")) {
				headers = headers + "; boundary=" + this._mime_boundary;
			}
			headers = headers + "\r\n";
		}
		if (!empty(body)) {
			headers = headers + "Content-length: " + strval(Strings.strlen(body)) + "\r\n";
		}
		if (!empty(this.user) || !empty(this.pass)) {
			headers = headers + "Authorization: Basic " + URL.base64_encode(this.user + ":" + this.pass) + "\r\n";
		}
		
		//add proxy auth headers
		if (!empty(this.proxy_user)) {
			headers = headers + "Proxy-Authorization: " + "Basic " + URL.base64_encode(this.proxy_user + ":" + this.proxy_pass) + "\r\n";
		}
		headers = headers + "\r\n";
		
		// set the read timeout if needed
		if (this.read_timeout > 0) {
			FileSystemOrSocket.socket_set_timeout(gVars.webEnv, fp, this.read_timeout);
		}
		this.timed_out = false;
		FileSystemOrSocket.fwrite(gVars.webEnv, fp, headers + body, Strings.strlen(headers + body));
		this._redirectaddr = strval(false);
		this.headers = new Array<Object>();
		while (booleanval(currentHeader = FileSystemOrSocket.fgets(gVars.webEnv, fp, this._maxlinelen))) {
			if (this.read_timeout > 0 && this._check_timeout(fp)) {
				this.status = -100;
				return false;
			}
			if (equal(currentHeader, "\r\n")) {
				break;
			}
			
			// if a header begins with Location: or URI:, set the redirect
			if (QRegExPerl.preg_match("/^(Location:|URI:)/i", currentHeader)) {
				// get URL portion of the redirect
				QRegExPerl.preg_match("/^(Location:|URI:)[ ]+(.*)/i", Strings.chop(currentHeader), matches);
				// look for :// in the Location header to see if hostname is included
				if (!QRegExPerl.preg_match("|\\:\\/\\/|", strval(matches.getValue(2)))) {
					// no host in the path, so prepend
					this._redirectaddr = URI_PARTS.getValue("scheme") + "://" + this.host + ":" + strval(this.port);
					// eliminate double slash
					if (!QRegExPerl.preg_match("|^/|", strval(matches.getValue(2)))) {
						this._redirectaddr = this._redirectaddr + "/" + strval(matches.getValue(2));
					}
					else
						this._redirectaddr = this._redirectaddr + strval(matches.getValue(2));
				}
				else
					this._redirectaddr = strval(matches.getValue(2));
			}
			if (QRegExPerl.preg_match("|^HTTP/|", currentHeader)) {
				if (QRegExPerl.preg_match("|^HTTP/[^\\s]*\\s(.*?)\\s|", currentHeader, status)) {
					this.status = intval(status.getValue(1));
				}
				this.response_code = currentHeader;
			}
			this.headers.putValue(currentHeader);
		}
		results = "";
		do {
			_data = FileSystemOrSocket.fread(gVars.webEnv, fp, this.maxlength);
			if (equal(Strings.strlen(_data), 0)) {
				break;
			}
			results = results + _data;
		}
		while (true);
		
		if (this.read_timeout > 0 && this._check_timeout(fp)) {
			this.status = -100;
			return false;
		}
		
		// check if there is a a redirect meta tag
		
		if (QRegExPerl.preg_match("\'<meta[\\s]*http-equiv[^>]*?content[\\s]*=[\\s]*[\"\\\']?\\d+;[\\s]*URL[\\s]*=[\\s]*([^\"\\\']*?)[\"\\\']?>\'i", results, match)) {
			this._redirectaddr = this._expandlinks(strval(match.getValue(1)), URI);
		}
		
		// have we hit our frame depth and is there frame src to fetch?
		if (this._framedepth < this.maxframes && booleanval(QRegExPerl.preg_match_all("\'<frame\\s+.*src[\\s]*=[\\\'\"]?([^\\\'\"\\>]+)\'i", results, match))) {
			((Array) this.results).putValue(results);
			for (x = 0; x < Array.count(match.getValue(1)); x++)
				this._frameurls.putValue(this._expandlinks(strval(match.getArrayValue(1).getValue(x)), URI_PARTS.getValue("scheme") + "://" + this.host));
		}
		// have we already fetched framed content?
		else
			if (is_array(this.results)) {
				((Array) this.results).putValue(results);
			}
			// no framed content
			else
				this.results = results;
		return true;
	}
	/** 
	 * Generated in place of local variable 'headerKey' from method
	 * '_httpsrequest' because it is used inside an inner class.
	 */
	Object	_httpsrequest_headerKey	= null;
	/** 
	 * Generated in place of local variable 'headerVal' from method
	 * '_httpsrequest' because it is used inside an inner class.
	 */
	int	   _httpsrequest_headerVal	= 0;

	public boolean _httpsrequest(String url, String URI, String http_method) {
		return _httpsrequest(url, URI, http_method, "", "");
	}

	public boolean _httpsrequest(String url, String URI, String http_method, String content_type) {
		return _httpsrequest(url, URI, http_method, content_type, "");
	}


	/*======================================================================*\
		Function:	_httpsrequest
		Purpose:	go get the https data from the server using curl
		Input:		$url		the url to fetch
					$URI		the full URI
					$body		body contents to send if any (POST)
		Output:
	\*======================================================================*/

	public boolean _httpsrequest(String url, String URI, String http_method, String content_type, String body) {
		Array<Object> headers = new Array<Object>();
		Array<String> URI_PARTS = new Array<String>();
		String cookie_str = null;
		Object cookieKey = null;
		String cookieVal = null;
		String safer_header = null;
		int curr_header = 0;
		String cmdline_params = null;
		String headerfile = null;
		String temp_dir = null;
		String safer_URI = null;
		Array<String> results = new Array<String>();
		Ref<Integer> _return = new Ref<Integer>();
		Array<String> result_headers;
		int currentHeader = 0;
		Array<Object> matches = new Array<Object>();
		Array match = new Array();
		int x = 0;
		if (this.passcookies && booleanval(this._redirectaddr)) {
			this.setcookies();
		}
		headers = new Array<Object>();
		URI_PARTS = URL.parse_url(URI);
		if (empty(url)) {
			url = "/";
		}
		
		// GET ... header not needed for curl
		//$headers[] = $http_method." ".$url." ".$this->_httpversion;
		if (!empty(this.agent)) {
			headers.putValue("User-Agent: " + this.agent);
		}
		if (!empty(this.host)) {
			if (!empty(this.port)) {
				headers.putValue("Host: " + this.host + ":" + strval(this.port));
			}
			else
				headers.putValue("Host: " + this.host);
		}
		if (!empty(this.accept)) {
			headers.putValue("Accept: " + this.accept);
		}
		if (!empty(this.referer)) {
			headers.putValue("Referer: " + this.referer);
		}
		if (!empty(this.cookies)) {
			if (!is_array(this.cookies)) {
				this.cookies = new Array<Object>(this.cookies);
			}
			Array.reset((Array) this.cookies);
			if (Array.count(this.cookies) > 0) {
				cookie_str = "Cookie: ";
				for (Map.Entry javaEntry410 : ((Array<?>) this.cookies).entrySet()) {
					cookieKey = javaEntry410.getKey();
					cookieVal = strval(javaEntry410.getValue());
					cookie_str = cookie_str + strval(cookieKey) + "=" + URL.urlencode(cookieVal) + "; ";
				}
				headers.putValue(Strings.substr(cookie_str, 0, -2));
			}
		}
		if (!empty(this.rawheaders)) {
			if (!is_array(this.rawheaders)) {
				this.rawheaders = new Array<Object>(this.rawheaders);
			}
			while (booleanval(new ListAssigner<Object>() {
				public Array<Object> doAssign(Array<Object> srcArray) {
					if (strictEqual(srcArray, null)) {
						return null;
					}
					_httpsrequest_headerKey = srcArray.getValue(0);
					_httpsrequest_headerVal = intval(srcArray.getValue(1));
					return srcArray;
				}
			}.doAssign(Array.each((Array) this.rawheaders))))
				headers.putValue(strval(_httpsrequest_headerKey) + ": " + strval(_httpsrequest_headerVal));
		}
		if (!empty(content_type)) {
			if (equal(content_type, "multipart/form-data")) {
				headers.putValue("Content-type: " + content_type + "; boundary=" + this._mime_boundary);
			}
			else
				headers.putValue("Content-type: " + content_type);
		}
		if (!empty(body)) {
			headers.putValue("Content-length: " + strval(Strings.strlen(body)));
		}
		if (!empty(this.user) || !empty(this.pass)) {
			headers.putValue("Authorization: BASIC " + URL.base64_encode(this.user + ":" + this.pass));
		}
		for (curr_header = 0; curr_header < Array.count(headers); curr_header++) {
			safer_header = Strings.strtr(strval(headers.getValue(curr_header)), "\"", " ");
			cmdline_params = cmdline_params + " -H \"" + safer_header + "\"";
		}
		if (!empty(body)) {
			cmdline_params = cmdline_params + " -d \"" + body + "\"";
		}
		if (this.read_timeout > 0) {
			cmdline_params = cmdline_params + " -m " + strval(this.read_timeout);
		}
		headerfile = FileSystemOrSocket.tempnam(gVars.webEnv, temp_dir, "sno");
		safer_URI = Strings.strtr(URI, "\"", " "); // strip quotes from the URI to avoid shell access
		QProgramExecution.exec(ProgramExecution.escapeshellcmd(this.curl_path + " -D \"" + headerfile + "\"" + cmdline_params + " \"" + safer_URI + "\""), results, _return);
		if (booleanval(_return)) {
			this.error = "Error: cURL could not retrieve the document, error " + strval(_return.value) + ".";
			return false;
		}
		String resultsStr = Strings.implode("\r\n", results);
		result_headers = FileSystemOrSocket.file(gVars.webEnv, headerfile);
		this._redirectaddr = strval(false);
		this.headers = new Array<Object>();
		for (currentHeader = 0; currentHeader < Array.count(result_headers); currentHeader++) {
			// if a header begins with Location: or URI:, set the redirect
			if (QRegExPerl.preg_match("/^(Location: |URI: )/i", result_headers.getValue(currentHeader))) {
				// get URL portion of the redirect
				QRegExPerl.preg_match("/^(Location: |URI:)\\s+(.*)/", Strings.chop(result_headers.getValue(currentHeader)), matches);
				if (!QRegExPerl.preg_match("|\\:\\/\\/|", strval(matches.getValue(2)))) {
					// no host in the path, so prepend
					this._redirectaddr = URI_PARTS.getValue("scheme") + "://" + this.host + ":" + strval(this.port);
					// eliminate double slash
					if (!QRegExPerl.preg_match("|^/|", strval(matches.getValue(2)))) {
						this._redirectaddr = this._redirectaddr + "/" + strval(matches.getValue(2));
					}
					else
						this._redirectaddr = this._redirectaddr + strval(matches.getValue(2));
				}
				else
					this._redirectaddr = strval(matches.getValue(2));
			}
			if (QRegExPerl.preg_match("|^HTTP/|", result_headers.getValue(currentHeader))) {
				this.response_code = result_headers.getValue(currentHeader);
			}
			this.headers.putValue(result_headers.getValue(currentHeader));
		}
		
		// check if there is a a redirect meta tag
		
		if (QRegExPerl.preg_match("\'<meta[\\s]*http-equiv[^>]*?content[\\s]*=[\\s]*[\"\\\']?\\d+;[\\s]*URL[\\s]*=[\\s]*([^\"\\\']*?)[\"\\\']?>\'i", resultsStr, match)) {
			this._redirectaddr = this._expandlinks(strval(match.getValue(1)), URI);
		}
		
		// have we hit our frame depth and is there frame src to fetch?
		
		if (this._framedepth < this.maxframes && booleanval(QRegExPerl.preg_match_all("\'<frame\\s+.*src[\\s]*=[\\\'\"]?([^\\\'\"\\>]+)\'i", resultsStr, match))) {
			((Array) this.results).putValue(resultsStr);
			for (x = 0; x < Array.count(match.getValue(1)); x++)
				this._frameurls.putValue(this._expandlinks(strval(match.getArrayValue(1).getValue(x)), URI_PARTS.getValue("scheme") + "://" + this.host));
		}
		// have we already fetched framed content?
		else
			if (is_array(this.results)) {
				((Array) this.results).putValue(resultsStr);
			}
			// no framed content
			else
				this.results = resultsStr;
		JFileSystemOrSocket.unlink(gVars.webEnv, headerfile);
		return true;
	}


	/*======================================================================*\
		Function:	setcookies()
		Purpose:	set cookies for a redirection
	\*======================================================================*/

	public void setcookies() {
		int x = 0;
		Array<Object> match = new Array<Object>();
		for (x = 0; x < Array.count(this.headers); x++) {
			if (QRegExPerl.preg_match("/^set-cookie:[\\s]+([^=]+)=([^;]+)/i", strval(this.headers.getValue(x)), match)) {
				((Array) this.cookies).putValue(match.getValue(1), URL.urldecode(strval(match.getValue(2))));
			}
		}
	}


	/*======================================================================*\
		Function:	_check_timeout
		Purpose:	checks whether timeout has occurred
		Input:		$fp	file pointer
	\*======================================================================*/
	public boolean _check_timeout(int fp) {
		Array<Object> fp_status = new Array<Object>();
		if (this.read_timeout > 0) {
			fp_status = FileSystemOrSocket.socket_get_status(gVars.webEnv, fp);
			if (booleanval(fp_status.getValue("timed_out"))) {
				this.timed_out = true;
				return true;
			}
		}
		return false;
	}

/*======================================================================*\
	Function:	_connect
	Purpose:	make a socket connection
	Input:		$fp	file pointer
\*======================================================================*/

	public boolean _connect(Ref<Integer> fp) {
		String host = null;
		int port = 0;
		Ref<Integer> errno = new Ref<Integer>();
		Ref<String> errstr = new Ref<String>();
		if (!empty(this.proxy_host) && !empty(this.proxy_port)) {
			this._isproxy = true;
			host = this.proxy_host;
			port = this.proxy_port;
		}
		else {
			host = this.host;
			port = this.port;
		}
		this.status = 0;
		if (booleanval(fp.value = FileSystemOrSocket.fsockopen(gVars.webEnv, host, port, errno, errstr, this._fp_timeout))) {
			// socket connection succeeded
			
			return true;
		}
		else {
			// socket connection failed
			this.status = intval(errno);
			{
				int javaSwitchSelector57 = 0;
				if (equal(errno, -3))
					javaSwitchSelector57 = 1;
				if (equal(errno, -4))
					javaSwitchSelector57 = 2;
				if (equal(errno, -5))
					javaSwitchSelector57 = 3;
				switch (javaSwitchSelector57) {
					case 1: {
						this.error = "socket creation failed (-3)";
					}
					case 2: {
						this.error = "dns lookup failure (-4)";
					}
					case 3: {
						this.error = "connection refused or timed out (-5)";
					}
					default: {
						this.error = "connection failed (" + strval(errno) + ")";
					}
				}
			}
			return false;
		}
	}

/*======================================================================*\
	Function:	_disconnect
	Purpose:	disconnect a socket connection
	Input:		$fp	file pointer
\*======================================================================*/
	public boolean _disconnect(int fp) {
		return FileSystemOrSocket.fclose(gVars.webEnv, fp);
	}
	/** 
	 * Generated in place of local variable 'val' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	Object	                        _prepare_post_body_val;
	/** 
	 * Generated in place of local variable 'key' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	/* Do not change type */String	_prepare_post_body_key	      = null;
	/** 
	 * Generated in place of local variable 'cur_val' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	String	                        _prepare_post_body_cur_val	  = null;
	/** 
	 * Generated in place of local variable 'cur_key' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	Object	                        _prepare_post_body_cur_key	  = null;
	/** 
	 * Generated in place of local variable 'file_names' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	Array<Object>	                _prepare_post_body_file_names;
	/** 
	 * Generated in place of local variable 'file_name' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	String	                        _prepare_post_body_file_name	= null;
	/** 
	 * Generated in place of local variable 'field_name' from method
	 * '_prepare_post_body' because it is used inside an inner class.
	 */
	String	                        _prepare_post_body_field_name	= null;


	/*======================================================================*\
		Function:	_prepare_post_body
		Purpose:	Prepare post body according to encoding type
		Input:		$formvars  - form variables
					$formfiles - form upload files
		Output:		post body
	\*======================================================================*/
	public String _prepare_post_body(Object formvarsObj, Object formfilesObj) {
		String postdata = null;
		int fp = 0;
		String file_content = null;
		String base_name = null;
		Array formvars = new Array(formvarsObj);
		Array formfiles = new Array(formfilesObj);
		postdata = "";
		if (equal(Array.count(formvars), 0) && equal(Array.count(formfiles), 0)) {
			return null;
		}
		{
			int javaSwitchSelector58 = 0;
			if (equal(this._submit_type, "application/x-www-form-urlencoded"))
				javaSwitchSelector58 = 1;
			if (equal(this._submit_type, "multipart/form-data"))
				javaSwitchSelector58 = 2;
			switch (javaSwitchSelector58) {
				case 1: {
					Array.reset(formvars);
					while (booleanval(new ListAssigner<Object>() {
						public Array<Object> doAssign(Array<Object> srcArray) {
							if (strictEqual(srcArray, null)) {
								return null;
							}
							_prepare_post_body_key = strval(srcArray.getValue(0));
							_prepare_post_body_val = srcArray.getValue(1);
							return srcArray;
						}
					}.doAssign(Array.each(formvars)))) {
						if (is_array(_prepare_post_body_val) || is_object(_prepare_post_body_val)) {
							while (booleanval(new ListAssigner<Object>() {
								public Array<Object> doAssign(Array<Object> srcArray) {
									if (strictEqual(srcArray, null)) {
										return null;
									}
									_prepare_post_body_cur_key = srcArray.getValue(0);
									_prepare_post_body_cur_val = strval(srcArray.getValue(1));
									return srcArray;
								}
							}.doAssign(Array.each((Array) _prepare_post_body_val)))) {
								postdata = postdata + URL.urlencode(_prepare_post_body_key) + "[]=" + URL.urlencode(_prepare_post_body_cur_val) + "&";
							}
						}
						else
							postdata = postdata + URL.urlencode(_prepare_post_body_key) + "=" + URL.urlencode(strval(_prepare_post_body_val)) + "&";
					}
					break;
				}
				case 2: {
					this._mime_boundary = "Snoopy" + Strings.md5(Misc.uniqid(strval(DateTime.microtime())));
					Array.reset(formvars);
					while (booleanval(new ListAssigner<Object>() {
						public Array<Object> doAssign(Array<Object> srcArray) {
							if (strictEqual(srcArray, null)) {
								return null;
							}
							_prepare_post_body_key = strval(srcArray.getValue(0));
							_prepare_post_body_val = srcArray.getValue(1);
							return srcArray;
						}
					}.doAssign(Array.each(formvars)))) {
						if (is_array(_prepare_post_body_val) || is_object(_prepare_post_body_val)) {
							while (booleanval(new ListAssigner<Object>() {
								public Array<Object> doAssign(Array<Object> srcArray) {
									if (strictEqual(srcArray, null)) {
										return null;
									}
									_prepare_post_body_cur_key = srcArray.getValue(0);
									_prepare_post_body_cur_val = strval(srcArray.getValue(1));
									return srcArray;
								}
							}.doAssign(Array.each((Array) _prepare_post_body_val)))) {
								postdata = postdata + "--" + this._mime_boundary + "\r\n";
								postdata = postdata + "Content-Disposition: form-data; name=\"" + _prepare_post_body_key + "\\[\\]\"\r\n\r\n";
								postdata = postdata + _prepare_post_body_cur_val + "\r\n";
							}
						}
						else {
							postdata = postdata + "--" + this._mime_boundary + "\r\n";
							postdata = postdata + "Content-Disposition: form-data; name=\"" + _prepare_post_body_key + "\"\r\n\r\n";
							postdata = postdata + strval(_prepare_post_body_val) + "\r\n";
						}
					}
					Array.reset(formfiles);
					while (booleanval(new ListAssigner<Object>() {
						public Array<Object> doAssign(Array<Object> srcArray) {
							if (strictEqual(srcArray, null)) {
								return null;
							}
							_prepare_post_body_field_name = strval(srcArray.getValue(0));
							_prepare_post_body_file_names = (Array<Object>) srcArray.getValue(1);
							return srcArray;
						}
					}.doAssign(Array.each(formfiles)))) {

						//						Unsupported.settype(_prepare_post_body_file_names, "array");

						while (booleanval(new ListAssigner<Object>() {
							public Array<Object> doAssign(Array<Object> srcArray) {
								if (strictEqual(srcArray, null)) {
									return null;
								}
								_prepare_post_body_file_name = strval(srcArray.getValue(1));
								return srcArray;
							}
						}.doAssign(Array.each(_prepare_post_body_file_names)))) {
							if (!FileSystemOrSocket.is_readable(gVars.webEnv, _prepare_post_body_file_name)) {
								continue;
							}
							fp = FileSystemOrSocket.fopen(gVars.webEnv, _prepare_post_body_file_name, "r");
							while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
								file_content = file_content + FileSystemOrSocket.fread(gVars.webEnv, fp, FileSystemOrSocket.filesize(gVars.webEnv, _prepare_post_body_file_name));
							}
							FileSystemOrSocket.fclose(gVars.webEnv, fp);
							base_name = FileSystemOrSocket.basename(_prepare_post_body_file_name);
							postdata = postdata + "--" + this._mime_boundary + "\r\n";
							postdata = postdata + "Content-Disposition: form-data; name=\"" + _prepare_post_body_field_name + "\"; filename=\"" + base_name + "\"\r\n\r\n";
							postdata = postdata + file_content + "\r\n";
						}
					}
					postdata = postdata + "--" + this._mime_boundary + "--\r\n";
					break;
				}
			}
		}
		return postdata;
	}
	public Object	use_gzip;

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
