/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PHPMailer.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QMail;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;

////////////////////////////////////////////////////
//PHPMailer - PHP email class
//
//Class for sending email using either
//sendmail, PHP mail(), or SMTP.  Methods are
//based upon the standard AspEmail(tm) classes.
//
//Copyright (C) 2001 - 2003  Brent R. Matzelle
//
//License: LGPL, see LICENSE
////////////////////////////////////////////////////

/**
* PHPMailer - PHP email transport class
* @package PHPMailer
* @author Brent R. Matzelle
* @copyright 2001 - 2003 Brent R. Matzelle
*/
public class PHPMailer implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	           = Logger.getLogger(PHPMailer.class.getName());
	public GlobalConsts	          gConsts;
	public GlobalVars	          gVars;
    /////////////////////////////////////////////////
    // PUBLIC VARIABLES
    /////////////////////////////////////////////////

    /**
     * Email priority (1 = High, 3 = Normal, 5 = low).
     * @var int
     */
	public int	                  Priority	       = 3;
	/** 
	 * Sets the CharSet of the message.
	 * @var string
	 */
	public String	              CharSet	       = "UTF-8";
	/** 
	 * Sets the Content-type of the message.
	 * @var string
	 */
	public String	              ContentType	   = "text/plain";
	/** 
	 * Sets the Encoding of the message. Options for this are "8bit", "7bit",
	 * "binary", "base64", and "quoted-printable".
	 * @var string
	 */
	public String	              Encoding	       = "8bit";
	/** 
	 * Holds the most recent mailer error message.
	 * @var string
	 */
	public String	              ErrorInfo	       = "";
	/** 
	 * Sets the From email address for the message.
	 * @var string
	 */
	public String	              From	           = "localhost.localdomain";
	/** 
	 * Sets the From name of the message.
	 * @var string
	 */
	public String	              FromName	       = "Support";
	/** 
	 * Sets the Sender email (Return-Path) of the message. If not empty, will
	 * be sent via -f to sendmail or as 'MAIL FROM' in smtp mode.
	 * @var string
	 */
	public String	              Sender	       = "";
	/** 
	 * Sets the Subject of the message.
	 * @var string
	 */
	public String	              Subject	       = "";
	/** 
	 * Sets the Body of the message. This can be either an HTML or text body.
	 * If HTML then run IsHTML(true).
	 * @var string
	 */
	public String	              Body	           = "";
	/** 
	 * Sets the text-only body of the message. This automatically sets the
	 * email to multipart/alternative. This body can be read by mail clients
	 * that do not have HTML email capability such as mutt. Clients that can
	 * read HTML will view the normal Body.
	 * @var string
	 */
	public String	              AltBody	       = "";
	/** 
	 * Sets word wrapping on the body of the message to a given number of
	 * characters.
	 * @var int
	 */
	public int	                  WordWrap	       = 0;
	/** 
	 * Method to send mail: ("mail", "sendmail", or "smtp").
	 * @var string
	 */
	public String	              Mailer	       = "mail";
	/** 
	 * Sets the path of the sendmail program.
	 * @var string
	 */
	public String	              Sendmail	       = "/usr/sbin/sendmail";
	/** 
	 * Path to PHPMailer plugins. This is now only useful if the SMTP class is
	 * in a different directory than the PHP include path.
	 * @var string
	 */
	public String	              PluginDir	       = "";
	/** 
	 * Holds PHPMailer version.
	 * @var string
	 */
	public String	              Version	       = "1.73";
	/** 
	 * Sets the email address that a reading confirmation will be sent.
	 * @var string
	 */
	public String	              ConfirmReadingTo	= "";
	/** 
	 * Sets the hostname to use in Message-Id and Received headers and as
	 * default HELO string. If empty, the value returned by SERVER_NAME is used
	 * or 'localhost.localdomain'.
	 * @var string
	 */
	public String	              Hostname	       = "";
    /////////////////////////////////////////////////
    // SMTP VARIABLES
    /////////////////////////////////////////////////

    /**
     *  Sets the SMTP hosts.  All hosts must be separated by a
     *  semicolon.  You can also specify a different port
     *  for each host by using this format: [hostname:port]
     *  (e.g. "smtp1.example.com:25;smtp2.example.com").
     *  Hosts will be tried in order.
     *  @var string
     */
	public String	              Host	           = "localhost";
	/** 
	 * Sets the default SMTP server port.
	 * @var int
	 */
	public int	                  Port	           = 25;
	/** 
	 * Sets the SMTP HELO of the message (Default is $Hostname).
	 * @var string
	 */
	public String	              Helo	           = "";
	/** 
	 * Sets SMTP authentication. Utilizes the Username and Password variables.
	 * @var bool
	 */
	public boolean	              SMTPAuth	       = false;
	/** 
	 * Sets SMTP username.
	 * @var string
	 */
	public String	              Username	       = "";
	/** 
	 * Sets SMTP password.
	 * @var string
	 */
	public String	              Password	       = "";
	/** 
	 * Sets the SMTP server timeout in seconds. This function will not work
	 * with the win32 version.
	 * @var int
	 */
	public int	                  Timeout	       = 10;
	/** 
	 * Sets SMTP class debugging on or off.
	 * @var bool
	 */
	public int	                  SMTPDebug;
	/** 
	 * Prevents the SMTP connection from being closed after each mail sending.
	 * If this is set to true then to close the connection requires an explicit
	 * call to SmtpClose().
	 * @var bool
	 */
	public boolean	              SMTPKeepAlive	   = false;
	/** 
	 * #@+
	 * @access private
	 */
	public SMTP	                  smtp;
	public Array<Object>	      to	           = new Array<Object>();
	public Array<Object>	      cc	           = new Array<Object>();
	public Array<Object>	      bcc	           = new Array<Object>();
	public Array<Object>	      ReplyTo	       = new Array<Object>();
	public Array<Object>	      attachment	   = new Array<Object>();
	public Array<Object>	      CustomHeader	   = new Array<Object>();
	public String	              message_type	   = "";
	public Array<Object>	      boundary	       = new Array<Object>();
	public Array<Object>	      language	       = new Array<Object>();
	public int	                  error_count	   = 0;
	public String	              LE	           = "\r\n"; // Added by Numiton: As requested by RFC, see http://cr.yp.to/docs/smtplf.html
    /**#@-*/

    /////////////////////////////////////////////////
    // VARIABLE METHODS
    /////////////////////////////////////////////////

	public PHPMailer(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
		setContext(javaGlobalVariables, javaGlobalConstants);
	}

	/**
     * Sets message type to HTML.
     * @param bool $bool
     * @return void
     */
	public void IsHTML(boolean bool) {
		if (equal(bool, true)) {
			this.ContentType = "text/html";
		}
		else
			this.ContentType = "text/plain";
	}

	/** 
	 * Sets Mailer to send message using SMTP.
	 * @return void
	 */
	public void IsSMTP() {
		this.Mailer = "smtp";
	}

	/** 
	 * Sets Mailer to send message using PHP mail() function.
	 * @return void
	 */
	public void IsMail() {
		this.Mailer = "mail";
	}

	/** 
	 * Sets Mailer to send message using the $Sendmail program.
	 * @return void
	 */
	public void IsSendmail() {
		this.Mailer = "sendmail";
	}

	/** 
	 * Sets Mailer to send message using the qmail MTA.
	 * @return void
	 */
	public void IsQmail() {
		this.Sendmail = "/var/qmail/bin/sendmail";
		this.Mailer = "sendmail";
	}

	public void AddAddress(String address) {
		AddAddress(address, "");
	}

    /////////////////////////////////////////////////
    // RECIPIENT METHODS
    /////////////////////////////////////////////////

    /**
     * Adds a "To" address.
     * @param string $address
     * @param string $name
     * @return void
     */
	public void AddAddress(String address, String name) {
		int cur = 0;
		cur = Array.count(this.to);
		this.to.getArrayValue(cur).putValue(0, Strings.trim(address));
		this.to.getArrayValue(cur).putValue(1, name);
	}

	/** 
	 * Adds a "Cc" address. Note: this function works with the SMTP mailer on
	 * win32, not with the "mail" mailer.
	 * @param string $address
	 * @param string $name
	 * @return void
	 */
	public void AddCC(String address, String name) {
		int cur = 0;
		cur = Array.count(this.cc);
		this.cc.getArrayValue(cur).putValue(0, Strings.trim(address));
		this.cc.getArrayValue(cur).putValue(1, name);
	}

	/** 
	 * Adds a "Bcc" address. Note: this function works with the SMTP mailer on
	 * win32, not with the "mail" mailer.
	 * @param string $address
	 * @param string $name
	 * @return void
	 */
	public void AddBCC(String address, String name) {
		int cur = 0;
		cur = Array.count(this.bcc);
		this.bcc.getArrayValue(cur).putValue(0, Strings.trim(address));
		this.bcc.getArrayValue(cur).putValue(1, name);
	}

	/** 
	 * Adds a "Reply-to" address.
	 * @param string $address
	 * @param string $name
	 * @return void
	 */
	public void AddReplyTo(String address, String name) {
		int cur = 0;
		cur = Array.count(this.ReplyTo);
		this.ReplyTo.getArrayValue(cur).putValue(0, Strings.trim(address));
		this.ReplyTo.getArrayValue(cur).putValue(1, name);
	}

    /////////////////////////////////////////////////
    // MAIL SENDING METHODS
    /////////////////////////////////////////////////

    /**
     * Creates message and assigns Mailer. If the message is
     * not sent successfully then it returns false.  Use the ErrorInfo
     * variable to view description of the error.
     * @return bool
     */
	public boolean Send() {
		String header = null;
		String body = null;
		boolean result = false;
		header = "";
		body = "";
		result = true;
		if (Array.count(this.to) + Array.count(this.cc) + Array.count(this.bcc) < 1) {
			this.SetError(this.Lang("provide_address"));
			return false;
		}
		
        // Set whether the message is multipart/alternative
		if (!empty(this.AltBody)) {
			this.ContentType = "multipart/alternative";
		}
		this.error_count = 0; // reset errors
		this.SetMessageType();
		header = header + this.CreateHeader();
		body = this.CreateBody();
		if (equal(body, "")) {
			return false;
		}
		
        // Choose the mailer
		{
			int javaSwitchSelector48 = 0;
			if (equal(this.Mailer, "sendmail"))
				javaSwitchSelector48 = 1;
			if (equal(this.Mailer, "mail"))
				javaSwitchSelector48 = 2;
			if (equal(this.Mailer, "smtp"))
				javaSwitchSelector48 = 3;
			switch (javaSwitchSelector48) {
				case 1: {
					result = this.SendmailSend(header, body);
					break;
				}
				case 2: {
					result = this.MailSend(header, body);
					break;
				}
				case 3: {
					result = this.SmtpSend(header, body);
					break;
				}
				default: {
					this.SetError(this.Mailer + this.Lang("mailer_not_supported"));
					result = false;
					break;
				}
			}
		}
		return result;
	}

	/** 
	 * Sends mail using the $Sendmail program.
	 * @access private
	 * @return bool
	 */
	public boolean SendmailSend(String header, String body) {
		String sendmail = null;
		int mail = 0;
		int result = 0;
		if (!equal(this.Sender, "")) {
			sendmail = QStrings.sprintf("%s -oi -f %s -t", this.Sendmail, ProgramExecution.escapeshellarg(this.Sender));
		}
		else
			sendmail = QStrings.sprintf("%s -oi -t", this.Sendmail);

		// Modified by Numiton

		Process process = null;
		int exitValue = 0;
		try {
			process = Runtime.getRuntime().exec(sendmail);
			process.getOutputStream().write(header.getBytes());
			process.getOutputStream().write(body.getBytes());
			process.getOutputStream().close();
			exitValue = process.exitValue();
		}
		catch (Exception ex) {
			LOG.warn(ex, ex);
		}
		if (!booleanval(process)) {
			this.SetError(this.Lang("execute") + this.Sendmail);
			return false;
		}
		result = exitValue >> 8 & 255;
		if (!equal(result, 0)) {
			this.SetError(this.Lang("execute") + this.Sendmail);
			return false;
		}
		return true;
	}

	/** 
	 * Sends mail using the PHP mail() function.
	 * @access private
	 * @return bool
	 */
	public boolean MailSend(String header, String body) {
		String to = null;
		int i = 0;
		String old_from = null;
		String params = null;
		boolean rt = false;
		to = "";
		for (i = 0; i < Array.count(this.to); i++) {
			if (!equal(i, 0)) {
				to = to + ", ";
			}
			to = to + strval(this.to.getArrayValue(i).getValue(0));
		}
		if (!equal(this.Sender, "") && Strings.strlen(Options.ini_get(gVars.webEnv, "safe_mode")) < 1) {
			old_from = Options.ini_get(gVars.webEnv, "sendmail_from");
			Options.ini_set(gVars.webEnv, "sendmail_from", this.Sender);
			params = QStrings.sprintf("-oi -f %s", this.Sender);
			rt = QMail.mail(gVars.webEnv, to, this.EncodeHeader(this.Subject), body, header, params);
		}
		else
			rt = QMail.mail(gVars.webEnv, to, this.EncodeHeader(this.Subject), body, header);
		if (isset(old_from)) {
			Options.ini_set(gVars.webEnv, "sendmail_from", old_from);
		}
		if (!rt) {
			this.SetError(this.Lang("instantiate"));
			return false;
		}
		return true;
	}

	/** 
	 * Sends mail via SMTP using PhpSMTP (Author: Chris Ryan). Returns bool.
	 * Returns false if there is a bad MAIL FROM, RCPT, or DATA input.
	 * @access private
	 * @return bool
	 */
	public boolean SmtpSend(String header, String body) {
		String error = null;
		Array<Object> bad_rcpt = new Array<Object>();
		String smtp_from = null;
		int i = 0;
		
		error = "";
		bad_rcpt = new Array<Object>();
		
		if (!this.SmtpConnect()) {
			return false;
		}
		smtp_from = (equal(this.Sender, "") ? this.From : this.Sender);
		if (!this.smtp.Mail(smtp_from)) {
			error = this.Lang("from_failed") + smtp_from;
			this.SetError(error);
			this.smtp.Reset();
			return false;
		}
		
        // Attempt to send attach all recipients
		for (i = 0; i < Array.count(this.to); i++) {
			if (!this.smtp.Recipient(this.to.getArrayValue(i).getValue(0))) {
				bad_rcpt.putValue(this.to.getArrayValue(i).getValue(0));
			}
		}
		for (i = 0; i < Array.count(this.cc); i++) {
			if (!this.smtp.Recipient(this.cc.getArrayValue(i).getValue(0))) {
				bad_rcpt.putValue(this.cc.getArrayValue(i).getValue(0));
			}
		}
		for (i = 0; i < Array.count(this.bcc); i++) {
			if (!this.smtp.Recipient(this.bcc.getArrayValue(i).getValue(0))) {
				bad_rcpt.putValue(this.bcc.getArrayValue(i).getValue(0));
			}
		}
		if (Array.count(bad_rcpt) > 0) { // Create error message
			for (i = 0; i < Array.count(bad_rcpt); i++) {
				if (!equal(i, 0)) {
					error = error + ", ";
				}
				error = error + strval(bad_rcpt.getValue(i));
			}
			error = this.Lang("recipients_failed") + error;
			this.SetError(error);
			this.smtp.Reset();
			return false;
		}
		if (!this.smtp.Data(header + body)) {
			this.SetError(this.Lang("data_not_accepted"));
			this.smtp.Reset();
			return false;
		}
		if (equal(this.SMTPKeepAlive, true)) {
			this.smtp.Reset();
		}
		else
			this.SmtpClose();
		return true;
	}
	/** 
	 * Generated in place of local variable 'host' from method 'SmtpConnect'
	 * because it is used inside an inner class.
	 */
	String	SmtpConnect_host	= null;
	/** 
	 * Generated in place of local variable 'port' from method 'SmtpConnect'
	 * because it is used inside an inner class.
	 */
	int	   SmtpConnect_port	 = 0;

	/** 
	 * Initiates a connection to an SMTP server. Returns false if the
	 * operation failed.
	 * @access private
	 * @return bool
	 */
	public boolean SmtpConnect() {
		Array<String> hosts = new Array<String>();
		int index = 0;
		boolean connection = false;
		
		if (equal(this.smtp, null)) {
			this.smtp = new SMTP(gVars, gConsts);
		}
		this.smtp.do_debug = this.SMTPDebug;
		hosts = Strings.explode(";", this.Host);
		index = 0;
		connection = this.smtp.Connected();
		
        // Retry while there is no connection
		while (index < Array.count(hosts) && equal(connection, false)) {
			if (booleanval(Strings.strstr(hosts.getValue(index), ":"))) {
				new ListAssigner<String>() {
					public Array<String> doAssign(Array<String> srcArray) {
						if (strictEqual(srcArray, null)) {
							return null;
						}
						SmtpConnect_host = srcArray.getValue(0);
						SmtpConnect_port = intval(srcArray.getValue(1));
						return srcArray;
					}
				}.doAssign(Strings.explode(":", hosts.getValue(index)));
			}
			else {
				SmtpConnect_host = hosts.getValue(index);
				SmtpConnect_port = this.Port;
			}
			if (this.smtp.Connect(SmtpConnect_host, SmtpConnect_port, this.Timeout)) {
				if (!equal(this.Helo, "")) {
					this.smtp.Hello(this.Helo);
				}
				else
					this.smtp.Hello(this.ServerHostname());
				if (this.SMTPAuth) {
					if (!this.smtp.Authenticate(this.Username, this.Password)) {
						this.SetError(this.Lang("authenticate"));
						this.smtp.Reset();
						connection = false;
					}
				}
				connection = true;
			}
			index++;
		}
		if (!connection) {
			this.SetError(this.Lang("connect_host"));
		}
		return connection;
	}

	/** 
	 * Closes the active SMTP session if one exists.
	 * @return void
	 */
	public void SmtpClose() {
		if (!equal(this.smtp, null)) {
			if (this.smtp.Connected()) {
				this.smtp.Quit();
				this.smtp.Close();
			}
		}
	}

	public boolean SetLanguage(String lang_type) {
		return SetLanguage(lang_type, "language/");
	}

	/** 
	 * Sets the language for all class error messages. Returns false if it
	 * cannot load the language file. The default language type is English.
	 * @param string $lang_type Type of language (e.g. Portuguese: "br")
	 * @param string $lang_path Path to the language file directory
	 * @access public
	 * @return bool
	 */
	public boolean SetLanguage(String lang_type, String lang_path) {
		Object PHPMAILER_LANG = null;
		if (FileSystemOrSocket.file_exists(gVars.webEnv, lang_path + "phpmailer.lang-" + lang_type + ".php")) {
		}
		else
			// Commented by Numiton: This is never found

			if (FileSystemOrSocket.file_exists(gVars.webEnv, lang_path + "phpmailer.lang-en.php")) {
			}
			else
			// Commented by Numiton: This is never found

			{
				this.SetError("Could not load language file");
				return false;
			}

		// Modified by Numiton: PHPMAILER_LANG is always empty

		this.language = new Array(PHPMAILER_LANG);
		return true;
	}

    /////////////////////////////////////////////////
    // MESSAGE CREATION METHODS
    /////////////////////////////////////////////////

    /**
     * Creates recipient headers.
     * @access private
     * @return string
     */
	public String AddrAppend(String type, Array<Object> addr) {
		String addr_str = null;
		int i = 0;
		addr_str = type + ": ";
		addr_str = addr_str + this.AddrFormat(addr.getArrayValue(0));
		if (Array.count(addr) > 1) {
			for (i = 1; i < Array.count(addr); i++)
				addr_str = addr_str + ", " + this.AddrFormat(addr.getArrayValue(i));
		}
		addr_str = addr_str + this.LE;
		return addr_str;
	}

	/** 
	 * Formats an address correctly.
	 * @access private
	 * @return string
	 */
	public String AddrFormat(Array<Object> addr) {
		String formatted = null;
		if (empty(addr.getValue(1))) {
			formatted = strval(addr.getValue(0));
		}
		else {
			formatted = this.EncodeHeader(strval(addr.getValue(1)), "phrase") + " <" + strval(addr.getValue(0)) + ">";
		}
		return formatted;
	}

	public String WrapText(String message, int length) {
		return WrapText(message, length, false);
	}

	/** 
	 * Wraps message for use with mailers that do not automatically perform
	 * wrapping and for quoted-printable. Original written by philippe.
	 * @access private
	 * @return string
	 */
	public String WrapText(String message, int length, boolean qp_mode) {
		String soft_break = null;
		Array<String> line = new Array<String>();
		Array<String> line_part = new Array<String>();
		int i = 0;
		String buf = null;
		String word = null;
		int e = 0;
		int space_left = 0;
		int len = 0;
		String part = null;
		String buf_o = null;
		soft_break = (qp_mode ? QStrings.sprintf(" =%s", this.LE) : this.LE);
		message = this.FixEOL(message);
		if (equal(Strings.substr(message, -1), this.LE)) {
			message = Strings.substr(message, 0, -1);
		}
		line = Strings.explode(this.LE, message);
		message = "";
		for (i = 0; i < Array.count(line); i++) {
			line_part = Strings.explode(" ", line.getValue(i));
			buf = "";
			for (e = 0; e < Array.count(line_part); e++) {
				word = line_part.getValue(e);
				if (qp_mode && Strings.strlen(word) > length) {
					space_left = length - Strings.strlen(buf) - 1;
					if (!equal(e, 0)) {
						if (space_left > 20) {
							len = space_left;
							if (equal(Strings.substr(word, len - 1, 1), "=")) {
								len--;
							}
							else
								if (equal(Strings.substr(word, len - 2, 1), "=")) {
									len = len - 2;
								}
							part = Strings.substr(word, 0, len);
							word = Strings.substr(word, len);
							buf = buf + " " + part;
							message = message + buf + QStrings.sprintf("=%s", this.LE);
						}
						else {
							message = message + buf + soft_break;
						}
						buf = "";
					}
					while (Strings.strlen(word) > 0) {
						len = length;
						if (equal(Strings.substr(word, len - 1, 1), "=")) {
							len--;
						}
						else
							if (equal(Strings.substr(word, len - 2, 1), "=")) {
								len = len - 2;
							}
						part = Strings.substr(word, 0, len);
						word = Strings.substr(word, len);
						if (Strings.strlen(word) > 0) {
							message = message + part + QStrings.sprintf("=%s", this.LE);
						}
						else
							buf = part;
					}
				}
				else {
					buf_o = buf;
					buf = buf + (equal(e, 0) ? word : (" " + word));
					if (Strings.strlen(buf) > length && !equal(buf_o, "")) {
						message = message + buf_o + soft_break;
						buf = word;
					}
				}
			}
			message = message + buf + this.LE;
		}
		return message;
	}

	/** 
	 * Set the body wrapping.
	 * @access private
	 * @return void
	 */
	public void SetWordWrap() {
		if (this.WordWrap < 1) {
			return;
		}

		{
			int javaSwitchSelector49 = 0;
			if (equal(this.message_type, "alt"))
				javaSwitchSelector49 = 1;
			if (equal(this.message_type, "alt_attachments"))
				javaSwitchSelector49 = 2;
			switch (javaSwitchSelector49) {
				case 1: {
		              // fall through
				}
				case 2: {
					this.AltBody = this.WrapText(this.AltBody, this.WordWrap);
					break;
				}
				default: {
					this.Body = this.WrapText(this.Body, this.WordWrap);
					break;
				}
			}
		}
	}

	/** 
	 * Assembles message header.
	 * @access private
	 * @return string
	 */
	public String CreateHeader() {
		String result = null;
		String uniq_id = null;
		Array<Object> from = new Array<Object>();
		int index = 0;
		
		result = "";
		
        // Set the boundaries
		uniq_id = Strings.md5(Misc.uniqid(strval(DateTime.time())));
		this.boundary.putValue(1, "b1_" + uniq_id);
		this.boundary.putValue(2, "b2_" + uniq_id);
		result += this.HeaderLine("Date", this.RFCDate());
		if (equal(this.Sender, "")) {
			result += this.HeaderLine("Return-Path", Strings.trim(this.From));
		}
		else
			result += this.HeaderLine("Return-Path", Strings.trim(this.Sender));
		
        // To be created automatically by mail()
		if (!equal(this.Mailer, "mail")) {
			if (Array.count(this.to) > 0) {
				result += this.AddrAppend("To", this.to);
			}
			else
				if (equal(Array.count(this.cc), 0)) {
					result += this.HeaderLine("To", "undisclosed-recipients:;");
				}
			if (Array.count(this.cc) > 0) {
				result += this.AddrAppend("Cc", this.cc);
			}
		}
		from = new Array<Object>();
		from.getArrayValue(0).putValue(0, Strings.trim(this.From));
		from.getArrayValue(0).putValue(1, this.FromName);
		result += this.AddrAppend("From", from);
		
        // sendmail and mail() extract Bcc from the header before sending
		if ((equal(this.Mailer, "sendmail") || equal(this.Mailer, "mail")) && Array.count(this.bcc) > 0) {
			result += this.AddrAppend("Bcc", this.bcc);
		}
		
		if (Array.count(this.ReplyTo) > 0) {
			result += this.AddrAppend("Reply-to", this.ReplyTo);
		}
		
        // mail() sets the subject itself
		if (!equal(this.Mailer, "mail")) {
			result += this.HeaderLine("Subject", this.EncodeHeader(Strings.trim(this.Subject)));
		}
		result += QStrings.sprintf("Message-ID: <%s@%s>%s", uniq_id, this.ServerHostname(), this.LE);
		result += this.HeaderLine("X-Priority", this.Priority);
		if (!equal(this.ConfirmReadingTo, "")) {
			result += this.HeaderLine("Disposition-Notification-To", "<" + Strings.trim(this.ConfirmReadingTo) + ">");
		}
		
        // Add custom headers
		for (index = 0; index < Array.count(this.CustomHeader); index++) {
			result += this.HeaderLine(Strings.trim(strval(this.CustomHeader.getArrayValue(index).getValue(0))), this.EncodeHeader(Strings.trim(strval(this.CustomHeader.getArrayValue(index)
			        .getValue(1)))));
		}
		result += this.HeaderLine("MIME-Version", "1.0");

		{
			int javaSwitchSelector50 = 0;
			if (equal(this.message_type, "plain"))
				javaSwitchSelector50 = 1;
			if (equal(this.message_type, "attachments"))
				javaSwitchSelector50 = 2;
			if (equal(this.message_type, "alt_attachments"))
				javaSwitchSelector50 = 3;
			if (equal(this.message_type, "alt"))
				javaSwitchSelector50 = 4;
			switch (javaSwitchSelector50) {
				case 1: {
					result += this.HeaderLine("Content-Transfer-Encoding", this.Encoding);
					result += QStrings.sprintf("Content-Type: %s; charset=\"%s\"", this.ContentType, this.CharSet);
					break;
				}
				case 2:
					// fall through
				case 3: {
					if (this.InlineImageExists()) {
						result = result + QStrings.sprintf("Content-Type: %s;%s\ttype=\"text/html\";%s\tboundary=\"%s\"%s", "multipart/related", this.LE, this.LE, this.boundary.getValue(1), this.LE);
					}
					else {
						result += this.HeaderLine("Content-Type", "multipart/mixed;");
						result += this.TextLine("\tboundary=\"" + this.boundary.getValue(1) + "\"");
					}
					break;
				}
				case 4: {
					result += this.HeaderLine("Content-Type", "multipart/alternative;");
					result += this.TextLine("\tboundary=\"" + this.boundary.getValue(1) + "\"");
					break;
				}
			}
		}
		if (!equal(this.Mailer, "mail")) {
			result += this.LE + this.LE;
		}
		return result;
	}

	/** 
	 * Assembles the message body. Returns an empty string on failure.
	 * @access private
	 * @return string
	 */
	public String CreateBody() {
		String result = null;
		result = "";
		this.SetWordWrap();
		{
			int javaSwitchSelector51 = 0;
			if (equal(this.message_type, "alt"))
				javaSwitchSelector51 = 1;
			if (equal(this.message_type, "plain"))
				javaSwitchSelector51 = 2;
			if (equal(this.message_type, "attachments"))
				javaSwitchSelector51 = 3;
			if (equal(this.message_type, "alt_attachments"))
				javaSwitchSelector51 = 4;
			switch (javaSwitchSelector51) {
				case 1: {
					result = result + this.GetBoundary(this.boundary.getValue(1), "", "text/plain", "");
					result = result + this.EncodeString(this.AltBody, this.Encoding);
					result = result + this.LE + this.LE;
					result = result + this.GetBoundary(this.boundary.getValue(1), "", "text/html", "");
					result = result + this.EncodeString(this.Body, this.Encoding);
					result = result + this.LE + this.LE;
					result = result + this.EndBoundary(this.boundary.getValue(1));
					break;
				}
				case 2: {
					result = result + this.EncodeString(this.Body, this.Encoding);
					break;
				}
				case 3: {
					result = result + this.GetBoundary(this.boundary.getValue(1), "", "", "");
					result = result + this.EncodeString(this.Body, this.Encoding);
					result = result + this.LE;
					result = result + this.AttachAll();
					break;
				}
				case 4: {
					result = result + QStrings.sprintf("--%s%s", this.boundary.getValue(1), this.LE);
					result = result + QStrings.sprintf("Content-Type: %s;%s" + "\tboundary=\"%s\"%s", "multipart/alternative", this.LE, this.boundary.getValue(2), this.LE + this.LE);
					
	                // Create text body
					result = result + this.GetBoundary(this.boundary.getValue(2), "", "text/plain", "") + this.LE;
					result = result + this.EncodeString(this.AltBody, this.Encoding);
					result = result + this.LE + this.LE;
					
	                // Create the HTML body
					result = result + this.GetBoundary(this.boundary.getValue(2), "", "text/html", "") + this.LE;
					result = result + this.EncodeString(this.Body, this.Encoding);
					result = result + this.LE + this.LE;
					result = result + this.EndBoundary(this.boundary.getValue(2));
					result = result + this.AttachAll();
					break;
				}
			}
		}
		if (this.IsError()) {
			result = "";
		}
		return result;
	}

	/** 
	 * Returns the start of a message boundary.
	 * @access private
	 */
	public String GetBoundary(Object boundary, String charSet, String contentType, String encoding) {
		String result = null;
		result = "";
		if (equal(charSet, "")) {
			charSet = this.CharSet;
		}
		if (equal(contentType, "")) {
			contentType = this.ContentType;
		}
		if (equal(encoding, "")) {
			encoding = this.Encoding;
		}
		result = result + this.TextLine("--" + boundary);
		result = result + QStrings.sprintf("Content-Type: %s; charset = \"%s\"", contentType, charSet);
		result = result + this.LE;
		result = result + this.HeaderLine("Content-Transfer-Encoding", encoding);
		result = result + this.LE;
		return result;
	}

	/** 
	 * Returns the end of a message boundary.
	 * @access private
	 */
	public String EndBoundary(Object boundary) {
		return this.LE + "--" + strval(boundary) + "--" + this.LE;
	}

	/** 
	 * Sets the message type.
	 * @access private
	 * @return void
	 */
	public void SetMessageType() {
		if (Array.count(this.attachment) < 1 && Strings.strlen(this.AltBody) < 1) {
			this.message_type = "plain";
		}
		else {
			if (Array.count(this.attachment) > 0) {
				this.message_type = "attachments";
			}
			if (Strings.strlen(this.AltBody) > 0 && Array.count(this.attachment) < 1) {
				this.message_type = "alt";
			}
			if (Strings.strlen(this.AltBody) > 0 && Array.count(this.attachment) > 0) {
				this.message_type = "alt_attachments";
			}
		}
	}

	/** 
	 * Returns a formatted header line.
	 * @access private
	 * @return string
	 */
	public String HeaderLine(String name, Object value) {
		return name + ": " + value + this.LE;
	}

	/** 
	 * Returns a formatted mail line.
	 * @access private
	 * @return string
	 */
	public String TextLine(String value) {
		return value + this.LE;
	}

    /////////////////////////////////////////////////
    // ATTACHMENT METHODS
    /////////////////////////////////////////////////

    /**
     * Adds an attachment from a path on the filesystem.
     * Returns false if the file could not be found
     * or accessed.
     * @param string $path Path to the attachment.
     * @param string $name Overrides the attachment name.
     * @param string $encoding File encoding (see $Encoding).
     * @param string $type File extension (MIME) type.
     * @return bool
     */
	public boolean AddAttachment(String path, String name, String encoding, String type) {
		String filename = null;
		int cur = 0;
		if (!FileSystemOrSocket.is_file(gVars.webEnv, path)) {
			this.SetError(this.Lang("file_access") + path);
			return false;
		}
		filename = FileSystemOrSocket.basename(path);
		if (equal(name, "")) {
			name = filename;
		}
		cur = Array.count(this.attachment);
		this.attachment.getArrayValue(cur).putValue(0, path);
		this.attachment.getArrayValue(cur).putValue(1, filename);
		this.attachment.getArrayValue(cur).putValue(2, name);
		this.attachment.getArrayValue(cur).putValue(3, encoding);
		this.attachment.getArrayValue(cur).putValue(4, type);
		this.attachment.getArrayValue(cur).putValue(5, false); // isStringAttachment
		this.attachment.getArrayValue(cur).putValue(6, "attachment");
		this.attachment.getArrayValue(cur).putValue(7, 0);
		return true;
	}

	/** 
	 * Attaches all fs, string, and binary attachments to the message. Returns
	 * an empty string on failure.
	 * @access private
	 * @return string
	 */
	public String AttachAll() {
		Array<String> mime;
		Object bString = null;
		int i = 0;
		String string = null;
		String path = null;
		Object filename = null;
		Object name = null;
		String encoding = null;
		Object type = null;
		Object disposition = null;
		Object cid = null;
		
		mime = new Array<String>();
		
        // Return text of body
		for (i = 0; i < Array.count(this.attachment); i++) {
            // Check for string attachment
			bString = this.attachment.getArrayValue(i).getValue(5);
			
			if (booleanval(bString)) {
				string = strval(this.attachment.getArrayValue(i).getValue(0));
			}
			else
				path = strval(this.attachment.getArrayValue(i).getValue(0));
			filename = this.attachment.getArrayValue(i).getValue(1);
			name = this.attachment.getArrayValue(i).getValue(2);
			encoding = strval(this.attachment.getArrayValue(i).getValue(3));
			type = this.attachment.getArrayValue(i).getValue(4);
			disposition = this.attachment.getArrayValue(i).getValue(6);
			cid = this.attachment.getArrayValue(i).getValue(7);
			mime.putValue(QStrings.sprintf("--%s%s", this.boundary.getValue(1), this.LE));
			mime.putValue(QStrings.sprintf("Content-Type: %s; name=\"%s\"%s", type, name, this.LE));
			mime.putValue(QStrings.sprintf("Content-Transfer-Encoding: %s%s", encoding, this.LE));
			if (equal(disposition, "inline")) {
				mime.putValue(QStrings.sprintf("Content-ID: <%s>%s", cid, this.LE));
			}
			mime.putValue(QStrings.sprintf("Content-Disposition: %s; filename=\"%s\"%s", disposition, name, this.LE + this.LE));
			
            // Encode as string attachment
			if (booleanval(bString)) {
				mime.putValue(this.EncodeString(string, encoding));
				if (this.IsError()) {
					return "";
				}
				mime.putValue(this.LE + this.LE);
			}
			else {
				mime.putValue(this.EncodeFile(path, encoding));
				if (this.IsError()) {
					return "";
				}
				mime.putValue(this.LE + this.LE);
			}
		}
		mime.putValue(QStrings.sprintf("--%s--%s", this.boundary.getValue(1), this.LE));
		return Strings.join("", mime);
	}

	/** 
	 * Encodes attachment in requested format. Returns an empty string on
	 * failure.
	 * @access private
	 * @return string
	 */
	public String EncodeFile(String path, String encoding) {
		int fd = 0;
		int magic_quotes = 0;
		String file_buffer = null;
		if (!booleanval(fd = FileSystemOrSocket.fopen(gVars.webEnv, path, "rb"))) {
			this.SetError(this.Lang("file_open") + path);
			return "";
		}
		magic_quotes = Options.get_magic_quotes_runtime(gVars.webEnv);
		Options.set_magic_quotes_runtime(gVars.webEnv, 0);
		file_buffer = FileSystemOrSocket.fread(gVars.webEnv, fd, FileSystemOrSocket.filesize(gVars.webEnv, path));
		file_buffer = this.EncodeString(file_buffer, encoding);
		FileSystemOrSocket.fclose(gVars.webEnv, fd);
		Options.set_magic_quotes_runtime(gVars.webEnv, magic_quotes);
		return file_buffer;
	}

	/** 
	 * Encodes string to requested format. Returns an empty string on failure.
	 * @access private
	 * @return string
	 */
	public String EncodeString(String str, String encoding) {
		String encoded = null;
		encoded = "";
		{
			int javaSwitchSelector52 = 0;
			if (equal(Strings.strtolower(encoding), "base64"))
				javaSwitchSelector52 = 1;
			if (equal(Strings.strtolower(encoding), "7bit"))
				javaSwitchSelector52 = 2;
			if (equal(Strings.strtolower(encoding), "8bit"))
				javaSwitchSelector52 = 3;
			if (equal(Strings.strtolower(encoding), "binary"))
				javaSwitchSelector52 = 4;
			if (equal(Strings.strtolower(encoding), "quoted-printable"))
				javaSwitchSelector52 = 5;
			switch (javaSwitchSelector52) {
				case 1: {
		              // chunk_split is found in PHP >= 3.0.6
					encoded = Strings.chunk_split(URL.base64_encode(str), 76, this.LE);
					break;
				}
				case 2: {
				}
				case 3: {
					encoded = this.FixEOL(str);
					if (!equal(Strings.substr(encoded, -Strings.strlen(this.LE)), this.LE)) {
						encoded = encoded + this.LE;
					}
					break;
				}
				case 4: {
					encoded = str;
					break;
				}
				case 5: {
					encoded = this.EncodeQP(str);
					break;
				}
				default: {
					this.SetError(this.Lang("encoding") + encoding);
					break;
				}
			}
		}
		return encoded;
	}

	public String EncodeHeader(String str) {
		return EncodeHeader(str, "text");
	}

	/** 
	 * Encode a header string to best of Q, B, quoted or none.
	 * @access private
	 * @return string
	 */
	public String EncodeHeader(String str, String position) {
		int x = 0;
		String encoded = null;
		Array matches = new Array();
		int maxlen = 0;
		String encoding = null;
		x = 0;

		{
			int javaSwitchSelector53 = 0;
			if (equal(Strings.strtolower(position), "phrase"))
				javaSwitchSelector53 = 1;
			if (equal(Strings.strtolower(position), "comment"))
				javaSwitchSelector53 = 2;
			if (equal(Strings.strtolower(position), "text"))
				javaSwitchSelector53 = 3;
			switch (javaSwitchSelector53) {
				case 1: {
					if (!QRegExPerl.preg_match("/[\\200-\\377]/", str)) {
			            // Can't use addslashes as we don't know what value has magic_quotes_sybase.
						encoded = Strings.addcslashes(str, "\u0000..\u0025\u00B1\\\"");
						if (equal(str, encoded) && !QRegExPerl.preg_match("/[^A-Za-z0-9!#$%&\'*+\\/=?^_`{|}~ -]/", str)) {
							return encoded;
						}
						else
							return "\"" + encoded + "\"";
					}
					x = QRegExPerl.preg_match_all("/[^\\040\\041\\043-\\133\\135-\\176]/", str, matches);
					break;
				}
				case 2: {
					x = QRegExPerl.preg_match_all("/[()\"]/", str, matches);
			        // Fall-through
				}
				case 3: {
				}
				default: {
					x = x + QRegExPerl.preg_match_all("/[\\000-\\010\\013\\014\\016-\\037\\177-\\377]/", str, matches);
					break;
				}
			}
		}
		if (equal(x, 0)) {
			return str;
		}
		
		maxlen = 75 - 7 - Strings.strlen(this.CharSet);
		
	    // Try to select the encoding which should produce the shortest output
		if (floatval(Strings.strlen(str)) / floatval(3) < floatval(x)) {
			encoding = "B";
			encoded = URL.base64_encode(str);
			maxlen = maxlen - maxlen % 4;
			encoded = Strings.trim(Strings.chunk_split(encoded, maxlen, "\n"));
		}
		else {
			encoding = "Q";
			encoded = this.EncodeQ(str, position);
			encoded = this.WrapText(encoded, maxlen, true);
			encoded = Strings.str_replace("=" + this.LE, "\n", Strings.trim(encoded));
		}
		encoded = QRegExPerl.preg_replace("/^(.*)$/m", " =?" + this.CharSet + "?" + encoding + "?\\1?=", encoded);
		encoded = Strings.trim(Strings.str_replace("\n", this.LE, encoded));
		return encoded;
	}

	/** 
	 * Encode string to quoted-printable.
	 * @access private
	 * @return string
	 */
	public String EncodeQP(String str) {
		String encoded = null;
		encoded = this.FixEOL(str);
		if (!equal(Strings.substr(encoded, -Strings.strlen(this.LE)), this.LE)) {
			encoded = encoded + this.LE;
		}

		// Modified by Numiton
        // Replace every high ascii, control and = characters
		encoded = RegExPerl.preg_replace_callback("/([\\000-\\010\\013\\014\\016-\\037\\075\\177-\\377])/", new Callback("replaceSprintfOrd", this), encoded);

		// Modified by Numiton
        // Replace every spaces and tabs when it's the last character on a line
		encoded = RegExPerl.preg_replace_callback("/([\t ])" + this.LE + "/", new Callback("replaceSprintfOrdLE", this), encoded);
		
        // Maximum line length of 76 characters before CRLF (74 + space + '=')
		encoded = this.WrapText(encoded, 74, true);
		return encoded;
	}

	/** 
	 * Encode string to q encoding.
	 * @access private
	 * @return string
	 */
	public String EncodeQ(String str, String position) {
        // There should not be any EOL in the string
		String encoded = QRegExPerl.preg_replace("[\r\n]", "", str);
		{
			int javaSwitchSelector54 = 0;
			if (equal(Strings.strtolower(position), "phrase"))
				javaSwitchSelector54 = 1;
			if (equal(Strings.strtolower(position), "comment"))
				javaSwitchSelector54 = 2;
			if (equal(Strings.strtolower(position), "text"))
				javaSwitchSelector54 = 3;
			switch (javaSwitchSelector54) {
				case 1: {

					// Modified by Numiton

					encoded = RegExPerl.preg_replace_callback("/([^A-Za-z0-9!*+\\/ -])/", new Callback("replaceSprintfOrd", this), encoded);
					break;
				}
				case 2: {

					// Modified by Numiton

					encoded = RegExPerl.preg_replace_callback("/([\\(\\)\"])/", new Callback("replaceSprintfOrd", this), encoded);
				}
				case 3: {
				}
				default: {

					// Modified by Numiton
		            // Replace every high ascii, control =, ? and _ characters
					encoded = RegExPerl.preg_replace_callback("/([\\000-\\011\\013\\014\\016-\\037\\075\\077\\137\\177-\\377])/", new Callback("replaceSprintfOrd", this), encoded);
					break;
				}
			}
		}
		
        // Replace every spaces to _ (more readable than =20)
		encoded = Strings.str_replace(" ", "_", encoded);
		return encoded;
	}

	/** 
	 * Adds a string or binary attachment (non-filesystem) to the list. This
	 * method can be used to attach ascii or binary data, such as a BLOB record
	 * from a database.
	 * @param string $string String attachment data.
	 * @param string $filename Name of the attachment.
	 * @param string $encoding File encoding (see $Encoding).
	 * @param string $type File extension (MIME) type.
	 * @return void
	 */
	public void AddStringAttachment(Object string, Object filename, Object encoding, Object type) {
		int cur = 0;
		
        // Append to $attachment array
		cur = Array.count(this.attachment);
		this.attachment.getArrayValue(cur).putValue(0, string);
		this.attachment.getArrayValue(cur).putValue(1, filename);
		this.attachment.getArrayValue(cur).putValue(2, filename);
		this.attachment.getArrayValue(cur).putValue(3, encoding);
		this.attachment.getArrayValue(cur).putValue(4, type);
		this.attachment.getArrayValue(cur).putValue(5, true); // isString
		this.attachment.getArrayValue(cur).putValue(6, "attachment");
		this.attachment.getArrayValue(cur).putValue(7, 0);
	}

	/** 
	 * Adds an embedded attachment. This can include images, sounds, and just
	 * about any other document. Make sure to set the $type to an image type.
	 * For JPEG images use "image/jpeg" and for GIF images use "image/gif".
	 * @param string $path Path to the attachment.
	 * @param string $cid Content ID of the attachment. Use this to identify the
	 * Id for accessing the image in an HTML form.
	 * @param string $name Overrides the attachment name.
	 * @param string $encoding File encoding (see $Encoding).
	 * @param string $type File extension (MIME) type.
	 * @return bool
	 */
	public boolean AddEmbeddedImage(String path, String cid, String name, String encoding, String type) {
		String filename = null;
		int cur = 0;
		if (!FileSystemOrSocket.is_file(gVars.webEnv, path)) {
			this.SetError(this.Lang("file_access") + path);
			return false;
		}
		filename = FileSystemOrSocket.basename(path);
		if (equal(name, "")) {
			name = filename;
		}
		
        // Append to $attachment array
		cur = Array.count(this.attachment);
		this.attachment.getArrayValue(cur).putValue(0, path);
		this.attachment.getArrayValue(cur).putValue(1, filename);
		this.attachment.getArrayValue(cur).putValue(2, name);
		this.attachment.getArrayValue(cur).putValue(3, encoding);
		this.attachment.getArrayValue(cur).putValue(4, type);
		this.attachment.getArrayValue(cur).putValue(5, false);
		this.attachment.getArrayValue(cur).putValue(6, "inline");
		this.attachment.getArrayValue(cur).putValue(7, cid);
		return true;
	}

	/** 
	 * Returns true if an inline attachment is present.
	 * @access private
	 * @return bool
	 */
	public boolean InlineImageExists() {
		boolean result = false;
		int i = 0;
		result = false;
		for (i = 0; i < Array.count(this.attachment); i++) {
			if (equal(this.attachment.getArrayValue(i).getValue(6), "inline")) {
				result = true;
				break;
			}
		}
		return result;
	}

    /////////////////////////////////////////////////
    // MESSAGE RESET METHODS
    /////////////////////////////////////////////////

    /**
     * Clears all recipients assigned in the TO array.  Returns void.
     * @return void
     */
	public void ClearAddresses() {
		this.to = new Array<Object>();
	}

	/** 
	 * Clears all recipients assigned in the CC array. Returns void.
	 * @return void
	 */
	public void ClearCCs() {
		this.cc = new Array<Object>();
	}

	/** 
	 * Clears all recipients assigned in the BCC array. Returns void.
	 * @return void
	 */
	public void ClearBCCs() {
		this.bcc = new Array<Object>();
	}

	/** 
	 * Clears all recipients assigned in the ReplyTo array. Returns void.
	 * @return void
	 */
	public void ClearReplyTos() {
		this.ReplyTo = new Array<Object>();
	}

	/** 
	 * Clears all recipients assigned in the TO, CC and BCC array. Returns
	 * void.
	 * @return void
	 */
	public void ClearAllRecipients() {
		this.to = new Array<Object>();
		this.cc = new Array<Object>();
		this.bcc = new Array<Object>();
	}

	/** 
	 * Clears all previously set filesystem, string, and binary attachments.
	 * Returns void.
	 * @return void
	 */
	public void ClearAttachments() {
		this.attachment = new Array<Object>();
	}

	/** 
	 * Clears all custom headers. Returns void.
	 * @return void
	 */
	public void ClearCustomHeaders() {
		this.CustomHeader = new Array<Object>();
	}

    /////////////////////////////////////////////////
    // MISCELLANEOUS METHODS
    /////////////////////////////////////////////////

    /**
     * Adds the error message to the error container.
     * Returns void.
     * @access private
     * @return void
     */
	public void SetError(String msg) {
		this.error_count++;
		this.ErrorInfo = msg;
	}

	/** 
	 * Returns the proper RFC 822 formatted date.
	 * @access private
	 * @return string
	 */
	public String RFCDate() {
		int tz;
		String tzs = null;
		String result = null;
		tz = intval(DateTime.date("Z"));
		tzs = ((tz < 0) ? "-" : "+");
		tz = Math.abs(tz);
		tz = intval(tz / floatval(3600) * floatval(100) + floatval(tz % 3600) / floatval(60));
		result = QStrings.sprintf("%s %s%04d", DateTime.date("D, j M Y H:i:s"), tzs, tz);
		return result;
	}

	/** 
	 * Returns the appropriate server variable. Should work with both PHP
	 * 4.1.0+ as well as older versions. Returns an empty string if nothing is
	 * found.
	 * @access private
	 * @return mixed
	 */
	public String ServerVar(String varName) {

		// Commented by Numiton. Meaningless in Java

		//		if (!isset(gVars.webEnv._SERVER))
		//		{
		//			gVars.webEnv._SERVER = HTTP_SERVER_VARS;
		//			if (!isset(gVars.webEnv.getRemoteAddr())) {
		//				gVars.webEnv._SERVER = HTTP_ENV_VARS; // must be Apache
		//			}
		//		}

		if (isset(gVars.webEnv._SERVER.getValue(varName))) {
			return strval(gVars.webEnv._SERVER.getValue(varName));
		}
		else
			return "";
	}

	/** 
	 * Returns the server hostname or 'localhost.localdomain' if unknown.
	 * @access private
	 * @return string
	 */
	public String ServerHostname() {
		String result = null;
		if (!equal(this.Hostname, "")) {
			result = this.Hostname;
		}
		else
			if (!equal(this.ServerVar("SERVER_NAME"), "")) {
				result = this.ServerVar("SERVER_NAME");
			}
			else
				result = "localhost.localdomain";
		return result;
	}

	/** 
	 * Returns a message in the appropriate language.
	 * @access private
	 * @return string
	 */
	public String Lang(String key) {
		if (Array.count(this.language) < 1) {
			this.SetLanguage("en"); // set the default language
		}
		if (isset(this.language.getValue(key))) {
			return strval(this.language.getValue(key));
		}
		else
			return "Language string failed to load: " + key;
	}

	/** 
	 * Returns true if an error occurred.
	 * @return bool
	 */
	public boolean IsError() {
		return this.error_count > 0;
	}

	/** 
	 * Changes every end of line from CR or LF to CRLF.
	 * @access private
	 * @return string
	 */
	public String FixEOL(String str) {
		str = Strings.str_replace("\r\n", "\n", str);
		str = Strings.str_replace("\r", "\n", str);
		str = Strings.str_replace("\n", this.LE, str);
		return str;
	}

	/** 
	 * Adds a custom header.
	 * @return void
	 */
	public void AddCustomHeader(String custom_header) {
		this.CustomHeader.putValue(Strings.explode(":", custom_header, 2));
	}

	public String replaceSprintfOrd(Array matches) {
		return "=" + QStrings.sprintf("%02X", Strings.ord(strval(matches.getValue(1))));
	}

	public String replaceSprintfOrdLE(Array matches) {
		return "=" + QStrings.sprintf("%02X", Strings.ord(strval(matches.getValue(1)))) + this.LE;
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
