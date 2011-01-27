/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: POP3.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.Options;
import com.numiton.RegExPosix;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


/**
 * mail_fetch/setup.php
 *
 * @package SquirrelMail
 *
 * @copyright (c) 1999-2006 The SquirrelMail Project Team
 *
 * @copyright (c) 1999 CDI (cdi@thewebmasters.net) All Rights Reserved
 * Modified by Philippe Mingo 2001 mingo@rotedic.com
 * An RFC 1939 compliant wrapper class for the POP3 protocol.
 *
 * Licensed under the GNU GPL. For full terms see the file COPYING.
 *
 * pop3 class
 *
 * $Id: POP3.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
 */
public class POP3 implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(POP3.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String ERROR = ""; //  Error string.
    public int TIMEOUT = 60; //  Default timeout before giving up on a
                             //  network operation.
    public int COUNT = -1; //  Mailbox msg count
    public int BUFFER = 512; //  Socket buffer for socket fgets() calls.
                             //  Per RFC 1939 the returned line a POP3
                             //  server can send is 512 bytes.
    public int FP = 0; //  The connection to the server's
                       //  file descriptor
    public String MAILSERVER = ""; // Set this to hard code the server name
    public boolean DEBUG = false; // set to true to echo pop3
                                  // commands and responses to error_log
                                  // this WILL log passwords!
    public String BANNER = ""; //  Holds the banner returned by the
                               //  pop server - used for apop()
    public boolean ALLOWAPOP = false; //  Allow or disallow apop()
                                      //  This must be set to true
                                      //  manually
    Object pop_list_junk = null;

    /**
     * Generated in place of local variable 'num' from method 'pop_list' because
     * it is used inside an inner class.
     */
    Object pop_list_num = null;

    /**
     * Generated in place of local variable 'size' from method 'pop_list'
     * because it is used inside an inner class.
     */
    Object pop_list_size = null;

    /**
     * Generated in place of local variable 'thisMsg' from method 'pop_list'
     * because it is used inside an inner class.
     */
    Object pop_list_thisMsg;

    /**
     * Generated in place of local variable 'msgSize' from method 'pop_list'
     * because it is used inside an inner class.
     */
    Object pop_list_msgSize = null;

    /**
     * Generated in place of local variable 'ok' from method 'uidl' because it
     * is used inside an inner class.
     */
    Object uidl_ok = null;

    /**
     * Generated in place of local variable 'num' from method 'uidl' because it
     * is used inside an inner class.
     */
    String uidl_num = null;

    /**
     * Generated in place of local variable 'myUidl' from method 'uidl' because
     * it is used inside an inner class.
     */
    Object uidl_myUidl = null;

    /**
     * Generated in place of local variable 'msg' from method 'uidl' because it
     * is used inside an inner class.
     */
    Object uidl_msg = null;

    /**
     * Generated in place of local variable 'msgUidl' from method 'uidl' because
     * it is used inside an inner class.
     */
    String uidl_msgUidl = null;

    public POP3(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        this(javaGlobalVariables, javaGlobalConstants, "", 0);
    }

    /**
     * Allow or disallow apop() Allow or disallow apop() This must be set to
     * true This must be set to true manually manually
     */
    public POP3(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String server, int timeout) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        if (!empty(server)){
            // Do not allow programs to alter MAILSERVER
            // if it is already specified. They can get around
            // this if they -really- want to, so don't count on it.
            if (empty(this.MAILSERVER)) {
                this.MAILSERVER = server;
            }
        }

        if (!empty(timeout)) {
            this.TIMEOUT = timeout;

            if (!booleanval(Options.ini_get(gVars.webEnv, "safe_mode"))) {
                Options.set_time_limit(gVars.webEnv, timeout);
            }
        }
    }

    public boolean update_timer() {
        if (!booleanval(Options.ini_get(gVars.webEnv, "safe_mode"))) {
            Options.set_time_limit(gVars.webEnv, this.TIMEOUT);
        }

        return true;
    }

    public boolean connect(String server, int port) {
        int fp = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        String reply = null;

        //  Opens a socket to the specified server. Unless overridden,
        //  port defaults to 110. Returns true on success, false on fail

        // If MAILSERVER is set, override $server with it's value

        if (!isset(port) || !booleanval(port)) {
            port = 110;
        }

        if (!empty(this.MAILSERVER)) {
            server = this.MAILSERVER;
        }

        if (empty(server)) {
            this.ERROR = "POP3 connect: " + getIncluded(CompatPage.class, gVars, gConsts)._("No server specified");
            this.FP = 0;

            return false;
        }

        fp = FileSystemOrSocket.fsockopen(gVars.webEnv, server, port, errno, errstr);

        if (!booleanval(fp)) {
            this.ERROR = "POP3 connect: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + strval(errno.value) + "] [" + errstr.value + "]";
            this.FP = 0;

            return false;
        }

        /* Commented by Numiton. Socket is blocking. */
        this.update_timer();
        reply = FileSystemOrSocket.fgets(gVars.webEnv, fp, this.BUFFER);
        reply = this.strip_clf(reply);

        if (this.DEBUG) {
            ErrorHandling.error_log(gVars.webEnv, "POP3 SEND [connect: " + server + "] GOT [" + reply + "]", 0);
        }

        if (!booleanval(this.is_ok(reply))) {
            this.ERROR = "POP3 connect: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";
            this.FP = 0;

            return false;
        }

        this.FP = fp;
        this.BANNER = this.parse_banner(reply);

        return true;
    }

    public boolean user(Object user) {
        String reply = null;

        // Sends the USER command, returns true or false
        
        if (empty(user)) {
            this.ERROR = "POP3 user: " + getIncluded(CompatPage.class, gVars, gConsts)._("no login ID submitted");

            return false;
        } else if (!isset(this.FP)) {
            this.ERROR = "POP3 user: " + getIncluded(CompatPage.class, gVars, gConsts)._("connection not established");

            return false;
        } else {
            reply = this.send_cmd("USER " + user);

            if (!booleanval(this.is_ok(reply))) {
                this.ERROR = "POP3 user: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

                return false;
            } else {
                return true;
            }
        }
    }

    public int pass(String pass) {
        String reply = null;
        int count;

        // Sends the PASS command, returns # of msgs in mailbox,
        // returns false (undef) on Auth failure

        if (empty(pass)) {
            this.ERROR = "POP3 pass: " + getIncluded(CompatPage.class, gVars, gConsts)._("No password submitted");

            return BOOLEAN_FALSE;
        } else if (!isset(this.FP)) {
            this.ERROR = "POP3 pass: " + getIncluded(CompatPage.class, gVars, gConsts)._("connection not established");

            return BOOLEAN_FALSE;
        } else {
            reply = this.send_cmd("PASS " + pass);

            if (!booleanval(this.is_ok(reply))) {
                this.ERROR = "POP3 pass: " + getIncluded(CompatPage.class, gVars, gConsts)._("Authentication failed") + " [" + reply + "]";
                this.quit();

                return BOOLEAN_FALSE;
            } else {
                //  Auth successful.
                count = intval(this.last("count"));
                this.COUNT = count;

                return count;
            }
        }
    }

    public int apop(String login, String pass) {
        int retVal;
        String banner = null;
        String AuthString = null;
        String APOPString = null;
        String cmd = null;
        String reply = null;
        int count;

        //  Attempts an APOP login. If this fails, it'll
        //  try a standard login. YOUR SERVER MUST SUPPORT
        //  THE USE OF THE APOP COMMAND!
        //  (apop is optional per rfc1939)

        if (!isset(this.FP)) {
            this.ERROR = "POP3 apop: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return 0;
        } else if (!this.ALLOWAPOP) {
            retVal = this.login(login, pass);

            return retVal;
        } else if (empty(login)) {
            this.ERROR = "POP3 apop: " + getIncluded(CompatPage.class, gVars, gConsts)._("No login ID submitted");

            return 0;
        } else if (empty(pass)) {
            this.ERROR = "POP3 apop: " + getIncluded(CompatPage.class, gVars, gConsts)._("No password submitted");

            return 0;
        } else {
            banner = this.BANNER;

            if (!booleanval(banner) || empty(banner)) {
                this.ERROR = "POP3 apop: " + getIncluded(CompatPage.class, gVars, gConsts)._("No server banner") + " - " + getIncluded(CompatPage.class, gVars, gConsts)._("abort");
                retVal = this.login(login, pass);

                return retVal;
            } else {
                AuthString = banner;
                AuthString = AuthString + pass;
                APOPString = Strings.md5(AuthString);
                cmd = "APOP " + login + " " + APOPString;
                reply = this.send_cmd(cmd);

                if (!booleanval(this.is_ok(reply))) {
                    this.ERROR = "POP3 apop: " + getIncluded(CompatPage.class, gVars, gConsts)._("apop authentication failed") + " - " + getIncluded(CompatPage.class, gVars, gConsts)._("abort");
                    retVal = this.login(login, pass);

                    return retVal;
                } else {
                    //  Auth successful.
                    count = intval(this.last("count"));
                    this.COUNT = count;

                    return count;
                }
            }
        }
    }

    public int login(String login, String pass) {
        int fp;
        int count;

        // Sends both user and pass. Returns # of msgs in mailbox or
        // false on failure (or -1, if the error occurs while getting
        // the number of messages.)

        if (!isset(this.FP)) {
            this.ERROR = "POP3 login: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return intval(false);
        } else {
            fp = this.FP;

            if (!this.user(login)) {
                //  Preserve the error generated by user()
                return intval(false);
            } else {
                count = this.pass(pass);

                if (!booleanval(count) || equal(count, -1)) {
                    //  Preserve the error generated by last() and pass()
                    return intval(false);
                } else {
                    return count;
                }
            }
        }
    }

    public Array<Object> top(int msgNum, String numLines) {
        int fp;
        int buffer = 0;
        String cmd = null;
        String reply = null;
        int count = 0;
        Array<Object> MsgArray = new Array<Object>();
        String line = null;

        //  Gets the header and first $numLines of the msg body
        //  returns data in an array with each returned line being
        //  an array element. If $numLines is empty, returns
        //  only the header information, and none of the body.

        if (!isset(this.FP)) {
            this.ERROR = "POP3 top: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return new Array<Object>();
        }

        this.update_timer();
        fp = this.FP;
        buffer = this.BUFFER;
        cmd = "TOP " + strval(msgNum) + " " + numLines;
        FileSystemOrSocket.fwrite(gVars.webEnv, fp, "TOP " + strval(msgNum) + " " + numLines + "\r\n");
        reply = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);
        reply = this.strip_clf(reply);

        if (this.DEBUG) {
            ErrorHandling.error_log(gVars.webEnv, "POP3 SEND [" + cmd + "] GOT [" + reply + "]", 0);
        }

        if (!booleanval(this.is_ok(reply))) {
            this.ERROR = "POP3 top: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

            return new Array<Object>();
        }

        count = 0;
        MsgArray = new Array<Object>();
        line = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);

        while (!booleanval(RegExPosix.ereg("^\\.\r\n", line))) {
            MsgArray.putValue(count, line);
            count++;
            line = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);

            if (empty(line)) {
                break;
            }
        }

        return MsgArray;
    }

    public Object pop_list(int msgNum) {
        int fp;
        int Total;
        String cmd = null;
        String reply = null;
        Array<Object> MsgArray = new Array<Object>();
        int msgC = 0;
        String line = null;

        //  If called with an argument, returns that msgs' size in octets
        //  No argument returns an associative array of undeleted
        //  msg numbers and their sizes in octets

        if (!isset(this.FP)) {
            this.ERROR = "POP3 pop_list: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return new Array<Object>();
        }

        fp = this.FP;
        Total = this.COUNT;

        if (!booleanval(Total) || equal(Total, -1)) {
            return new Array<Object>();
        }

        if (equal(Total, 0)) {
            return new Array<Object>(new ArrayEntry<Object>("0"), new ArrayEntry<Object>("0"));
            // return -1;   // mailbox empty
        }

        this.update_timer();

        if (!empty(msgNum)) {
            cmd = "LIST " + strval(msgNum);
            FileSystemOrSocket.fwrite(gVars.webEnv, fp, cmd + "\r\n");
            reply = FileSystemOrSocket.fgets(gVars.webEnv, fp, this.BUFFER);
            reply = this.strip_clf(reply);

            if (this.DEBUG) {
                ErrorHandling.error_log(gVars.webEnv, "POP3 SEND [" + cmd + "] GOT [" + reply + "]", 0);
            }

            if (!booleanval(this.is_ok(reply))) {
                this.ERROR = "POP3 pop_list: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

                return new Array<Object>();
            }

            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        pop_list_junk = srcArray.getValue(0);
                        pop_list_num = srcArray.getValue(1);
                        pop_list_size = srcArray.getValue(2);

                        return srcArray;
                    }
                }.doAssign(QRegExPerl.preg_split("/\\s+/", reply));

            return pop_list_size;
        }

        cmd = "LIST";
        reply = this.send_cmd(cmd);

        if (!booleanval(this.is_ok(reply))) {
            reply = this.strip_clf(reply);
            this.ERROR = "POP3 pop_list: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

            return new Array<Object>();
        }

        MsgArray = new Array<Object>();
        MsgArray.putValue(0, Total);

        for (msgC = 1; msgC <= Total; msgC++) {
            if (msgC > Total) {
                break;
            }

            line = FileSystemOrSocket.fgets(gVars.webEnv, fp, this.BUFFER);
            line = this.strip_clf(line);

            if (booleanval(RegExPosix.ereg("^\\.", line))) {
                this.ERROR = "POP3 pop_list: " + getIncluded(CompatPage.class, gVars, gConsts)._("Premature end of list");

                return new Array<Object>();
            }

            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        pop_list_thisMsg = srcArray.getValue(0);
                        pop_list_msgSize = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(QRegExPerl.preg_split("/\\s+/", line));
            pop_list_thisMsg = intval(pop_list_thisMsg);

            if (!equal(pop_list_thisMsg, msgC)) {
                MsgArray.putValue(msgC, "deleted");
            } else {
                MsgArray.putValue(msgC, pop_list_msgSize);
            }
        }

        return MsgArray;
    }

    public Array<Object> get(int msgNum) {
        int fp = 0;
        int buffer = 0;
        String cmd = null;
        String reply = null;
        int count = 0;
        Array<Object> MsgArray = new Array<Object>();
        String line = null;

        //  Retrieve the specified msg number. Returns an array
        //  where each line of the msg is an array element.

        if (!isset(this.FP)) {
            this.ERROR = "POP3 get: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return new Array<Object>();
        }

        this.update_timer();
        fp = this.FP;
        buffer = this.BUFFER;
        cmd = "RETR " + strval(msgNum);
        reply = this.send_cmd(cmd);

        if (!booleanval(this.is_ok(reply))) {
            this.ERROR = "POP3 get: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

            return new Array<Object>();
        }

        count = 0;
        MsgArray = new Array<Object>();
        line = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);

        while (!booleanval(RegExPosix.ereg("^\\.\r\n", line))) {
            if (equal(Strings.getCharAt(line, 0), ".")) {
                line = Strings.substr(line, 1);
            }

            MsgArray.putValue(count, line);
            count++;
            line = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);

            if (empty(line)) {
                break;
            }
        }

        return MsgArray;
    }

    public Object last(String type) {
        int last = 0;
        String reply = null;
        Array<Object> Vars = new Array<Object>();
        Object count;
        Object size;
        
        //  Returns the highest msg number in the mailbox.
        //  returns -1 on error, 0+ on success, if type != count
        //  results in a popstat() call (2 element array returned)

        last = -1;

        if (!isset(this.FP)) {
            this.ERROR = "POP3 last: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return last;
        }

        reply = this.send_cmd("STAT");

        if (!booleanval(this.is_ok(reply))) {
            this.ERROR = "POP3 last: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

            return last;
        }

        Vars = QRegExPerl.preg_split("/\\s+/", reply);
        count = Vars.getValue(1);
        size = Vars.getValue(2);
        count = intval(count);
        size = intval(size);

        if (!equal(type, "count")) {
            return new Array<Object>(new ArrayEntry<Object>(count), new ArrayEntry<Object>(size));
        }

        return count;
    }

    public boolean reset() {
        String reply = null;

        //  Resets the status of the remote server. This includes
        //  resetting the status of ALL msgs to not be deleted.
        //  This method automatically closes the connection to the server.

        if (!isset(this.FP)) {
            this.ERROR = "POP3 reset: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return false;
        }

        reply = this.send_cmd("RSET");

        if (!booleanval(this.is_ok(reply))) {
            //  The POP3 RSET command -never- gives a -ERR
            //  response - if it ever does, something truely
            //  wild is going on.

            this.ERROR = "POP3 reset: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";
            ErrorHandling.error_log(gVars.webEnv, "POP3 reset: ERROR [" + reply + "]", 0);
        }

        this.quit();

        return true;
    }

    public String send_cmd(String cmd) {
        int fp;
        int buffer = 0;
        String reply = null;

        //  Sends a user defined command string to the
        //  POP server and returns the results. Useful for
        //  non-compliant or custom POP servers.
        //  Do NOT includ the \r\n as part of your command
        //  string - it will be appended automatically.

        //  The return value is a standard fgets() call, which
        //  will read up to $this->BUFFER bytes of data, until it
        //  encounters a new line, or EOF, whichever happens first.

        //  This method works best if $cmd responds with only
        //  one line of data.
        
        if (!isset(this.FP)) {
            this.ERROR = "POP3 send_cmd: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return strval(false);
        }

        if (empty(cmd)) {
            this.ERROR = "POP3 send_cmd: " + getIncluded(CompatPage.class, gVars, gConsts)._("Empty command string");

            return "";
        }

        fp = this.FP;
        buffer = this.BUFFER;
        this.update_timer();
        FileSystemOrSocket.fwrite(gVars.webEnv, fp, cmd + "\r\n");
        reply = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);
        reply = this.strip_clf(reply);

        if (this.DEBUG) {
            ErrorHandling.error_log(gVars.webEnv, "POP3 SEND [" + cmd + "] GOT [" + reply + "]", 0);
        }

        return reply;
    }

    public boolean quit() {
        int fp;
        String cmd = null;
        String reply = null;

        //  Closes the connection to the POP3 server, deleting
        //  any msgs marked as deleted.

        if (!isset(this.FP)) {
            this.ERROR = "POP3 quit: " + getIncluded(CompatPage.class, gVars, gConsts)._("connection does not exist");

            return false;
        }

        fp = this.FP;
        cmd = "QUIT";
        FileSystemOrSocket.fwrite(gVars.webEnv, fp, cmd + "\r\n");
        reply = FileSystemOrSocket.fgets(gVars.webEnv, fp, this.BUFFER);
        reply = this.strip_clf(reply);

        if (this.DEBUG) {
            ErrorHandling.error_log(gVars.webEnv, "POP3 SEND [" + cmd + "] GOT [" + reply + "]", 0);
        }

        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        this.FP = 0;

        return true;
    }

    public Array<Object> popstat() {
        Array<Object> PopArray = new Array<Object>();
        
        //  Returns an array of 2 elements. The number of undeleted
        //  msgs in the mailbox, and the size of the mbox in octets.

        PopArray = (Array<Object>) this.last("array");

        if (equal(PopArray, -1)) {
            return new Array<Object>();
        }

        if (!booleanval(PopArray) || empty(PopArray)) {
            return new Array<Object>();
        }

        return PopArray;
    }

    public Object uidl(int msgNum) {
        int fp;
        int buffer = 0;
        String cmd = null;
        String reply = null;
        Array<Object> UIDLArray = new Array<Object>();
        int Total;
        String line = null;
        int count = 0;

        //  Returns the UIDL of the msg specified. If called with
        //  no arguments, returns an associative array where each
        //  undeleted msg num is a key, and the msg's uidl is the element
        //  Array element 0 will contain the total number of msgs

        if (!isset(this.FP)) {
            this.ERROR = "POP3 uidl: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return new Array<Object>();
        }

        fp = this.FP;
        buffer = this.BUFFER;

        if (!empty(msgNum)) {
            cmd = "UIDL " + strval(msgNum);
            reply = this.send_cmd(cmd);

            if (!booleanval(this.is_ok(reply))) {
                this.ERROR = "POP3 uidl: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

                return new Array<Object>();
            }

            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        uidl_ok = srcArray.getValue(0);
                        uidl_num = strval(srcArray.getValue(1));
                        uidl_myUidl = srcArray.getValue(2);

                        return srcArray;
                    }
                }.doAssign(QRegExPerl.preg_split("/\\s+/", reply));

            return uidl_myUidl;
        } else {
            this.update_timer();
            UIDLArray = new Array<Object>();
            Total = this.COUNT;
            UIDLArray.putValue(0, Total);

            if (Total < 1) {
                return UIDLArray;
            }

            cmd = "UIDL";
            FileSystemOrSocket.fwrite(gVars.webEnv, fp, "UIDL\r\n");
            reply = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);
            reply = this.strip_clf(reply);

            if (this.DEBUG) {
                ErrorHandling.error_log(gVars.webEnv, "POP3 SEND [" + cmd + "] GOT [" + reply + "]", 0);
            }

            if (!booleanval(this.is_ok(reply))) {
                this.ERROR = "POP3 uidl: " + getIncluded(CompatPage.class, gVars, gConsts)._("Error ") + "[" + reply + "]";

                return new Array<Object>();
            }

            line = "";
            count = 1;
            line = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);

            while (!booleanval(RegExPosix.ereg("^\\.\r\n", line))) {
                if (booleanval(RegExPosix.ereg("^\\.\r\n", line))) {
                    break;
                }

                new ListAssigner<Object>() {
                        public Array<Object> doAssign(Array<Object> srcArray) {
                            if (strictEqual(srcArray, null)) {
                                return null;
                            }

                            uidl_msg = srcArray.getValue(0);
                            uidl_msgUidl = strval(srcArray.getValue(1));

                            return srcArray;
                        }
                    }.doAssign(QRegExPerl.preg_split("/\\s+/", line));
                uidl_msgUidl = this.strip_clf(uidl_msgUidl);

                if (equal(count, uidl_msg)) {
                    UIDLArray.putValue(uidl_msg, uidl_msgUidl);
                } else {
                    UIDLArray.putValue(count, "deleted");
                }

                count++;
                line = FileSystemOrSocket.fgets(gVars.webEnv, fp, buffer);
            }
        }

        return UIDLArray;
    }

    public boolean delete(int msgNum) {
        String reply = null;

        //  Flags a specified msg as deleted. The msg will not
        //  be deleted until a quit() method is called.

        if (!isset(this.FP)) {
            this.ERROR = "POP3 delete: " + getIncluded(CompatPage.class, gVars, gConsts)._("No connection to server");

            return false;
        }

        if (empty(msgNum)) {
            this.ERROR = "POP3 delete: " + getIncluded(CompatPage.class, gVars, gConsts)._("No msg number submitted");

            return false;
        }

        reply = this.send_cmd("DELE " + msgNum);

        if (!booleanval(this.is_ok(reply))) {
            this.ERROR = "POP3 delete: " + getIncluded(CompatPage.class, gVars, gConsts)._("Command failed ") + "[" + reply + "]";

            return false;
        }

        return true;
    }

    //  *********************************************************

    //  The following methods are internal to the class.

    public int is_ok(String cmd) {
        //  Return true or false on +OK or -ERR

        if (empty(cmd)) {
            return intval(false);
        } else {
            return RegExPosix.ereg("^\\+OK", cmd);
        }
    }

    public String strip_clf(String text) {
        String stripped;

        // Strips \r\n from server responses

        if (empty(text)) {
            return text;
        } else {
            stripped = Strings.str_replace("\r", "", text);
            stripped = Strings.str_replace("\n", "", stripped);

            return stripped;
        }
    }

    public String parse_banner(String server_text) {
        boolean outside = false;
        String banner = null;
        int length = 0;
        String digit = null;
        int count = 0;
        outside = true;
        banner = "";
        length = Strings.strlen(server_text);

        for (count = 0; count < length; count++) {
            digit = Strings.substr(server_text, count, 1);

            if (!empty(digit)) {
                if (!outside && !equal(digit, "<") && !equal(digit, ">")) {
                    banner = banner + digit;
                }

                if (equal(digit, "<")) {
                    outside = false;
                }

                if (equal(digit, ">")) {
                    outside = true;
                }
            }
        }

        banner = this.strip_clf(banner);    // Just in case

        return "<" + banner + ">";
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
}   // End class
