/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: SMTP.java,v 1.3 2008/10/03 18:45:29 numiton Exp $
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

import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.string.Strings;


////////////////////////////////////////////////////
//SMTP - PHP SMTP class
//
//Version 1.02
//
//Define an SMTP class that can be used to connect
//and communicate with any SMTP server. It implements
//all the SMTP functions defined in RFC821 except TURN.
//
//Author: Chris Ryan
//
//License: LGPL, see LICENSE
////////////////////////////////////////////////////

/**
* SMTP is rfc 821 compliant and implements all the rfc 821 SMTP
* commands except TURN which will always return a not implemented
* error. SMTP also provides some utility methods for sending mail
* to an SMTP server.
* @package PHPMailer
* @author Chris Ryan
*/
public class SMTP implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(SMTP.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    /**
     * SMTP server port
     * @var int
     */
    public int SMTP_PORT = 25;

    /**
     * SMTP reply line ending
     * @var string
     */
    public String CRLF = "\r\n";

    /**
     * Sets whether debugging is turned on
     * @var bool
     */
    public int do_debug; // the level of debug to perform

    /**
     *#@+
     * @access private
     */
    public Integer smtp_conn; //the socket to the server
    public Array<Object> error = new Array<Object>(); //error if any on the last call
    public String helo_rply; //the reply the server sent to us for HELO

    /**
     * Generated in place of local variable 'line' from method 'Data' because it
     * is used inside an inner class.
     */
    String Data_line = null;

    /**
     * Generated in place of local variable 'line_out' from method 'Data'
     * because it is used inside an inner class.
     */
    String Data_line_out = null;

    /**
     * Generated in place of local variable 'l' from method 'Expand' because it
     * is used inside an inner class.
     */
    String Expand_l = null;

    /**
     * #@-* Initialize the class so that the data is in a known state.
     * @access public
     * @return void
     */
    public SMTP(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.smtp_conn = 0;
        this.error = null;
        this.helo_rply = null;
        this.do_debug = 0;
    }

    /*************************************************************
     *                    CONNECTION FUNCTIONS                  *
     ***********************************************************/

    /**
     * Connect to the server specified on the port specified.
     * If the port is not specified use the default SMTP_PORT.
     * If tval is specified then a connection will try and be
     * established with the server for that number of seconds.
     * If tval is not specified the default is 30 seconds to
     * try on the connection.
     *
     * SMTP CODE SUCCESS: 220
     * SMTP CODE FAILURE: 421
     * @access public
     * @return bool
     */
    public boolean Connect(String host, int port, double tval) {
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        String announce = null;
        
        // set the error val to null so there is no confusion
        this.error = null;

        // make sure we are __not__ connected
        if (this.Connected()) {
        	// ok we are connected! what should we do?
			// for now we will just give an error saying we
			// are already connected
			this.error = new Array<Object>(new ArrayEntry<Object>("error", "Already connected to a server"));

            return false;
        }

        if (empty(port)) {
            port = this.SMTP_PORT;
        }

        // connect to the smtp server
        this.smtp_conn = FileSystemOrSocket.fsockopen(gVars.webEnv, host, // the host of the server 
        															port, // the port to use
        															errno, // error number if any
        															errstr, // error message if any
        															tval); // give up after ? secs

        // verify we connected properly
        if (empty(this.smtp_conn)) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Failed to connect to server"), new ArrayEntry<Object>("errno", errno), new ArrayEntry<Object>("errstr", errstr));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + strval(errstr) + " (" + strval(errno) + ")" + this.CRLF);
            }

            return false;
        }

        // sometimes the SMTP server takes a little longer to respond
        // so we will give it a longer timeout for the first read
        // Windows still does not have support for this timeout function
        if (!equal(Strings.substr("PHP_OS", 0, 3), "WIN")) {
            FileSystemOrSocket.socket_set_timeout(gVars.webEnv, this.smtp_conn, intval(tval), 0);
        }

        // get any announcement stuff
        announce = this.get_lines();

        // set the timeout  of any socket functions at 1/10 of a second
        //if(function_exists("socket_set_timeout"))
        //   socket_set_timeout($this->smtp_conn, 0, 100000);
        
        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + announce);
        }

        return true;
    }

    /**
     * Performs SMTP authentication. Must be run after running the Hello()
     * method. Returns true if successfully authenticated.
     * @access public
     * @return bool
     */
    public boolean Authenticate(String username, String password) {
        String rply = null;
        String code = null;
        
        // Start authentication
        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "AUTH LOGIN" + this.CRLF);
        
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (!equal(code, 334)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "AUTH not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        // Send encoded username
        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, URL.base64_encode(username) + this.CRLF);
        
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (!equal(code, 334)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "Username not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        // Send encoded password
        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, URL.base64_encode(password) + this.CRLF);
        
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (!equal(code, 235)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "Password not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Returns true if connected to a server otherwise false
     * @access private
     * @return bool
     */
    public boolean Connected() {
        Array<Object> sock_status = new Array<Object>();

        if (!empty(this.smtp_conn)) {
            sock_status = FileSystemOrSocket.socket_get_status(gVars.webEnv, this.smtp_conn);

            if (booleanval(sock_status.getValue("eof"))) {
                // hmm this is an odd situation... the socket is
                // valid but we aren't connected anymore
                if (this.do_debug >= 1) {
                    echo(gVars.webEnv, "SMTP -> NOTICE:" + this.CRLF + "EOF caught while checking if connected");
                }

                this.Close();

                return false;
            }

            return true; // everything looks good
        }

        return false;
    }

    /**
     * Closes the socket and cleans up the state of the class. It is not
     * considered good to use this function without first trying to use QUIT.
     * @access public
     * @return void
     */
    public void Close() {
        this.error = null; // so there is no confusion
        this.helo_rply = null;

        if (!empty(this.smtp_conn)) {
        	// close the connection and cleanup
            FileSystemOrSocket.fclose(gVars.webEnv, this.smtp_conn);
            this.smtp_conn = 0;
        }
    }

    /***************************************************************
     *                        SMTP COMMANDS                       *
     *************************************************************/

    /**
     * Issues a data command and sends the msg_data to the server
     * finializing the mail transaction. $msg_data is the message
     * that is to be send with the headers. Each header needs to be
     * on a single line followed by a <CRLF> with the message headers
     * and the message body being seperated by and additional <CRLF>.
     *
     * Implements rfc 821: DATA <CRLF>
     *
     * SMTP CODE INTERMEDIATE: 354
     *     [data]
     *     <CRLF>.<CRLF>
     *     SMTP CODE SUCCESS: 250
     *     SMTP CODE FAILURE: 552,554,451,452
     * SMTP CODE FAILURE: 451,554
     * SMTP CODE ERROR  : 500,501,503,421
     * @access public
     * @return bool
     */
    public boolean Data(String msg_data) {
        String rply = null;
        String code = null;
        Array<String> lines = new Array<String>();
        String field = null;
        boolean in_headers = false;
        int max_line_length = 0;
        Array<Object> lines_out = new Array<Object>();
        int pos = 0;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Data() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "DATA" + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 354)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "DATA command not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        // the server is ready to accept data!
        // according to rfc 821 we should not send more than 1000
        // including the CRLF
        // characters on a single line so we will break the data up
        // into lines by \r and/or \n then if needed we will break
        // each of those into smaller lines to fit within the limit.
        // in addition we will be looking for lines that start with
        // a period '.' and append and additional period '.' to that
        // line. NOTE: this does not count towards are limit.

        // normalize the line breaks so we know the explode works
        msg_data = Strings.str_replace("\r\n", "\n", msg_data);
        msg_data = Strings.str_replace("\r", "\n", msg_data);
        lines = Strings.explode("\n", msg_data);
        
        // we need to find a good way to determine is headers are
        // in the msg_data or if it is a straight msg body
        // currently I'm assuming rfc 822 definitions of msg headers
        // and if the first field of the first line (':' sperated)
        // does not contain a space then it _should_ be a header
        // and we can process all lines before a blank "" line as
        // headers.
        field = Strings.substr(lines.getValue(0), 0, Strings.strpos(lines.getValue(0), ":"));
        in_headers = false;

        if (!empty(field) && !booleanval(Strings.strstr(field, " "))) {
            in_headers = true;
        }

        max_line_length = 998; // used below; set here for ease in change

        while (booleanval(
                    new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    Data_line = strval(srcArray.getValue(1));

                    return srcArray;
                }
            }.doAssign(Array.each(lines)))) {
            lines_out = new Array<Object>();

            if (equal(Data_line, "") && in_headers) {
                in_headers = false;
            }

            // ok we need to break this line up into several
            // smaller lines
            while (Strings.strlen(Data_line) > max_line_length) {
                pos = Strings.strrpos(Strings.substr(Data_line, 0, max_line_length), " ");

                // Patch to fix DOS attack
                if (!booleanval(pos)) {
                    pos = max_line_length - 1;
                }

                lines_out.putValue(Strings.substr(Data_line, 0, pos));
                Data_line = Strings.substr(Data_line, pos + 1);

                // if we are processing headers we need to
                // add a LWSP-char to the front of the new line
                // rfc 822 on long msg headers
                if (in_headers) {
                    Data_line = "\t" + Data_line;
                }
            }

            lines_out.putValue(Data_line);

            // now send the lines to the server
            while (booleanval(
                        new ListAssigner<Object>() {
                        public Array<Object> doAssign(Array<Object> srcArray) {
                            if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        Data_line_out = strval(srcArray.getValue(1));

                        return srcArray;
                    }
                }.doAssign(Array.each(lines_out)))) {
                if (Strings.strlen(Data_line_out) > 0) {
                    if (equal(Strings.substr(Data_line_out, 0, 1), ".")) {
                        Data_line_out = "." + Data_line_out;
                    }
                }

                FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, Data_line_out + this.CRLF);
            }
        }

        // ok all the message data has been sent so lets get this
        // over with aleady
        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, this.CRLF + "." + this.CRLF);
        
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "DATA not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Expand takes the name and asks the server to list all the
     * people who are members of the _list_. Expand will return
     * back and array of the result or false if an error occurs.
     * Each value in the array returned has the format of:
     *     [ <full-name> <sp> ] <path>
     * The definition of <path> is defined in rfc 821
     *
     * Implements rfc 821: EXPN <SP> <string> <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE FAILURE: 550
     * SMTP CODE ERROR  : 500,501,502,504,421
     * @access public
     * @return string array
     */
    public Array<Object> Expand(Object name) {
        String rply = null;
        String code = null;
        Array<String> entries = new Array<String>();
        Array<Object> list = new Array<Object>();
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Expand() without being connected"));

            return new Array<Object>();
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "EXPN " + strval(name) + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "EXPN not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return new Array<Object>();
        }

        // parse the reply and place in our array to return to user
        entries = Strings.explode(this.CRLF, rply);

        while (booleanval(
                    new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    Expand_l = strval(srcArray.getValue(1));

                    return srcArray;
                }
            }.doAssign(Array.each(entries)))) {
            list.putValue(Strings.substr(Expand_l, 4));
        }

        return list;
    }

    /**
     * Sends the HELO command to the smtp server.
     * This makes sure that we and the server are in
     * the same known state.
     *
     * Implements from rfc 821: HELO <SP> <domain> <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE ERROR  : 500, 501, 504, 421
     * @access public
     * @return bool
     */
    public boolean Hello(String host) {
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Hello() without being connected"));

            return false;
        }

        // if a hostname for the HELO wasn't specified determine
        // a suitable one to send
        if (empty(host)) {
            // we need to determine some sort of appopiate default
            // to send to the server
            host = "localhost";
        }

        // Send extended hello first (RFC 2821)
        if (!this.SendHello("EHLO", host)) {
            if (!this.SendHello("HELO", host)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sends a HELO/EHLO command.
     * @access private
     * @return bool
     */
    public boolean SendHello(String hello, String host) {
        String rply = null;
        String code = null;
        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, hello + " " + host + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER: " + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", hello + " not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        this.helo_rply = rply;

        return true;
    }

    /**
     * Gets help information on the keyword specified. If the keyword
     * is not specified then returns generic help, ussually contianing
     * A list of keywords that help is available on. This function
     * returns the results back to the user. It is up to the user to
     * handle the returned data. If an error occurs then false is
     * returned with $this->error set appropiately.
     *
     * Implements rfc 821: HELP [ <SP> <string> ] <CRLF>
     *
     * SMTP CODE SUCCESS: 211,214
     * SMTP CODE ERROR  : 500,501,502,504,421
     * @access public
     * @return string
     */
    public String Help(Object keyword) {
        String extra = null;
        String rply = null;
        String code = null;
        
        this.error = null; // to avoid confusion

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Help() without being connected"));

            return strval(false);
        }

        extra = "";

        if (!empty(keyword)) {
            extra = " " + strval(keyword);
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "HELP" + extra + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 211) && !equal(code, 214)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "HELP not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return strval(false);
        }

        return rply;
    }

    /**
     * Starts a mail transaction from the email address specified in
     * $from. Returns true if successful or false otherwise. If True
     * the mail transaction is started and then one or more Recipient
     * commands may be called followed by a Data command.
     *
     * Implements rfc 821: MAIL <SP> FROM:<reverse-path> <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE SUCCESS: 552,451,452
     * SMTP CODE SUCCESS: 500,501,421
     * @access public
     * @return bool
     */
    public boolean Mail(Object from) {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Mail() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "MAIL FROM:<" + strval(from) + ">" + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "MAIL not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Sends the command NOOP to the SMTP server.
     *
     * Implements from rfc 821: NOOP <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE ERROR  : 500, 421
     * @access public
     * @return bool
     */
    public boolean Noop() {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Noop() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "NOOP" + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "NOOP not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    public boolean Quit() {
        return Quit(true);
    }

    /**
     * Sends the quit command to the server and then closes the socket
     * if there is no error or the $close_on_error argument is true.
     *
     * Implements from rfc 821: QUIT <CRLF>
     *
     * SMTP CODE SUCCESS: 221
     * SMTP CODE ERROR  : 500
     * @access public
     * @return bool
     */
    public boolean Quit(boolean close_on_error) {
        String byemsg = null;
        boolean rval = false;
        Array<Object> e = new Array<Object>();
        String code = null;
        
        this.error = null; // so there is no confusion

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Quit() without being connected"));

            return false;
        }

        // send the quit command to the server
        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "quit" + this.CRLF);
        
        // get any good-bye messages
        byemsg = this.get_lines();

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + byemsg);
        }

        rval = true;
        e = null;
        code = Strings.substr(byemsg, 0, 3);

        if (!equal(code, 221)) {
        	// use e as a tmp var cause Close will overwrite $this->error
            e = new Array<Object>(
                    new ArrayEntry<Object>("error", "SMTP server rejected quit command"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_rply", Strings.substr(byemsg, 4)));
            rval = false;

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(e.getValue("error")) + ": " + byemsg + this.CRLF);
            }
        }

        if (empty(e) || close_on_error) {
            this.Close();
        }

        return rval;
    }

    /**
     * Sends the command RCPT to the SMTP server with the TO: argument of $to.
     * Returns true if the recipient was accepted false if it was rejected.
     *
     * Implements from rfc 821: RCPT <SP> TO:<forward-path> <CRLF>
     *
     * SMTP CODE SUCCESS: 250,251
     * SMTP CODE FAILURE: 550,551,552,553,450,451,452
     * SMTP CODE ERROR  : 500,501,503,421
     * @access public
     * @return bool
     */
    public boolean Recipient(Object to) {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Recipient() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "RCPT TO:<" + strval(to) + ">" + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250) && !equal(code, 251)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "RCPT not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Sends the RSET command to abort and transaction that is
     * currently in progress. Returns true if successful false
     * otherwise.
     *
     * Implements rfc 821: RSET <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE ERROR  : 500,501,504,421
     * @access public
     * @return bool
     */
    public boolean Reset() {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Reset() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "RSET" + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "RSET failed"), new ArrayEntry<Object>("smtp_code", code), new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Starts a mail transaction from the email address specified in
     * $from. Returns true if successful or false otherwise. If True
     * the mail transaction is started and then one or more Recipient
     * commands may be called followed by a Data command. This command
     * will send the message to the users terminal if they are logged
     * in.
     *
     * Implements rfc 821: SEND <SP> FROM:<reverse-path> <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE SUCCESS: 552,451,452
     * SMTP CODE SUCCESS: 500,501,502,421
     * @access public
     * @return bool
     */
    public boolean Send(Object from) {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Send() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "SEND FROM:" + strval(from) + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "SEND not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Starts a mail transaction from the email address specified in
     * $from. Returns true if successful or false otherwise. If True
     * the mail transaction is started and then one or more Recipient
     * commands may be called followed by a Data command. This command
     * will send the message to the users terminal if they are logged
     * in and send them an email.
     *
     * Implements rfc 821: SAML <SP> FROM:<reverse-path> <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE SUCCESS: 552,451,452
     * SMTP CODE SUCCESS: 500,501,502,421
     * @access public
     * @return bool
     */
    public boolean SendAndMail(Object from) {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called SendAndMail() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "SAML FROM:" + strval(from) + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "SAML not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * Starts a mail transaction from the email address specified in
     * $from. Returns true if successful or false otherwise. If True
     * the mail transaction is started and then one or more Recipient
     * commands may be called followed by a Data command. This command
     * will send the message to the users terminal if they are logged
     * in or mail it to them if they are not.
     *
     * Implements rfc 821: SOML <SP> FROM:<reverse-path> <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE SUCCESS: 552,451,452
     * SMTP CODE SUCCESS: 500,501,502,421
     * @access public
     * @return bool
     */
    public boolean SendOrMail(Object from) {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called SendOrMail() without being connected"));

            return false;
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "SOML FROM:" + strval(from) + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "SOML not accepted from server"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return false;
        }

        return true;
    }

    /**
     * This is an optional command for SMTP that this class does not
     * support. This method is here to make the RFC821 Definition
     * complete for this class and __may__ be implimented in the future
     *
     * Implements from rfc 821: TURN <CRLF>
     *
     * SMTP CODE SUCCESS: 250
     * SMTP CODE FAILURE: 502
     * SMTP CODE ERROR  : 500, 503
     * @access public
     * @return bool
     */
    public boolean Turn() {
        this.error = new Array<Object>(new ArrayEntry<Object>("error", "This method, TURN, of the SMTP " + "is not implemented"));

        if (this.do_debug >= 1) {
            echo(gVars.webEnv, "SMTP -> NOTICE: " + strval(this.error.getValue("error")) + this.CRLF);
        }

        return false;
    }

    /**
     * Verifies that the name is recognized by the server.
     * Returns false if the name could not be verified otherwise
     * the response from the server is returned.
     *
     * Implements rfc 821: VRFY <SP> <string> <CRLF>
     *
     * SMTP CODE SUCCESS: 250,251
     * SMTP CODE FAILURE: 550,551,553
     * SMTP CODE ERROR  : 500,501,502,421
     * @access public
     * @return int
     */
    public String Verify(Object name) {
        String rply = null;
        String code = null;
        
        this.error = null; // so no confusion is caused

        if (!this.Connected()) {
            this.error = new Array<Object>(new ArrayEntry<Object>("error", "Called Verify() without being connected"));

            return strval(false);
        }

        FileSystemOrSocket.fputs(gVars.webEnv, this.smtp_conn, "VRFY " + strval(name) + this.CRLF);
        rply = this.get_lines();
        code = Strings.substr(rply, 0, 3);

        if (this.do_debug >= 2) {
            echo(gVars.webEnv, "SMTP -> FROM SERVER:" + this.CRLF + rply);
        }

        if (!equal(code, 250) && !equal(code, 251)) {
            this.error = new Array<Object>(
                    new ArrayEntry<Object>("error", "VRFY failed on name \'" + strval(name) + "\'"),
                    new ArrayEntry<Object>("smtp_code", code),
                    new ArrayEntry<Object>("smtp_msg", Strings.substr(rply, 4)));

            if (this.do_debug >= 1) {
                echo(gVars.webEnv, "SMTP -> ERROR: " + strval(this.error.getValue("error")) + ": " + rply + this.CRLF);
            }

            return strval(false);
        }

        return rply;
    }

    /*******************************************************************
     *                       INTERNAL FUNCTIONS                       *
     ******************************************************************/

    /**
     * Read in as many lines as possible
     * either before eof or socket timeout occurs on the operation.
     * With SMTP we can tell if we have more lines to read if the
     * 4th character is '-' symbol. If it is a space then we don't
     * need to read anything else.
     * @access private
     * @return string
     */
    public String get_lines() {
        String data = null;
        String str = null;
        data = "";

        while (booleanval(str = FileSystemOrSocket.fgets(gVars.webEnv, this.smtp_conn, 515))) {
            if (this.do_debug >= 4) {
                echo(gVars.webEnv, "SMTP -> get_lines(): $data was \"" + data + "\"" + this.CRLF);
                echo(gVars.webEnv, "SMTP -> get_lines(): $str is \"" + str + "\"" + this.CRLF);
            }

            data = data + str;

            if (this.do_debug >= 4) {
                echo(gVars.webEnv, "SMTP -> get_lines(): $data is \"" + data + "\"" + this.CRLF);
            }

            // if the 4th character is a space then we are done reading
            // so just break the loop
            if (equal(Strings.substr(str, 3, 1), " ")) {
                break;
            }
        }

        return data;
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
