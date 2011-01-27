/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ftp_base.java,v 1.5 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.PhpCommonConstants.*;
import static com.numiton.VarHandling.*;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.ftp.FTP;
import com.numiton.generic.*;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.*;
import com.numiton.output.OutputControl;
import com.numiton.string.Strings;


public abstract class ftp_base implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(ftp_base.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    /**
     * Public variables
     */
    public boolean LocalEcho;
    public boolean Verbose;
    public String OS_local;
    public String OS_remote;

    /**
     * Private variables
     */
    public int _lastaction;
    public Object _errors;
    public Object _type;
    public int _umask;
    public int _timeout;
    public boolean _passive;
    public String _host;
    public String _fullhost;
    public int _port;
    public Ref<String> _datahost = new Ref<String>();
    public Ref<Integer> _dataport = new Ref<Integer>();
    public int _ftp_control_sock;
    public int _ftp_data_sock;
    public int _ftp_temp_sock;
    public int _ftp_buff_size;
    public String _login;
    public String _password;
    public boolean _connected;
    public boolean _ready;
    public int _code;
    public String _message;
    public boolean _can_restore;
    public boolean _port_available;
    public int _curtype;
    public Array<Object> _features = new Array<Object>();
    public Ref<Array<Array<Object>>> _error_array = new Ref<Array<Array<Object>>>();
    public Array<Integer> AuthorizedTransferMode = new Array<Integer>();
    public Array<String> OS_FullName = new Array<String>();
    public Array<String> _eol_code = new Array<String>();
    public Array<String> AutoAsciiExt = new Array<String>();
    public Array<Object> features = new Array<Object>();

    /**
     * Constructor
     */

    // Commented by Numiton
    //	public ftp_base(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object port_mode) {
    //		setContext(javaGlobalVariables, javaGlobalConstants);
    //		this(port_mode);
    //	}
    public ftp_base(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, boolean port_mode, boolean verb, boolean le) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.LocalEcho = le;
        this.Verbose = verb;
        this._lastaction = intval(null);
        this._error_array.value = new Array<Array<Object>>();
        this._eol_code = new Array<String>(
                new ArrayEntry<String>(gConsts.getFTP_OS_Unix(), "\n"),
                new ArrayEntry<String>(gConsts.getFTP_OS_Mac(), "\r"),
                new ArrayEntry<String>(gConsts.getFTP_OS_Windows(), "\r\n"));
        this.AuthorizedTransferMode = new Array<Integer>(new ArrayEntry<Integer>(gConsts.getFTP_AUTOASCII()), new ArrayEntry<Integer>(FTP.FTP_ASCII), new ArrayEntry<Integer>(FTP.FTP_BINARY));
        this.OS_FullName = new Array<String>(
                new ArrayEntry<String>(gConsts.getFTP_OS_Unix(), "UNIX"),
                new ArrayEntry<String>(gConsts.getFTP_OS_Windows(), "WINDOWS"),
                new ArrayEntry<String>(gConsts.getFTP_OS_Mac(), "MACOS"));
        this.AutoAsciiExt = new Array<String>(
                new ArrayEntry<String>("ASP"),
                new ArrayEntry<String>("BAT"),
                new ArrayEntry<String>("C"),
                new ArrayEntry<String>("CPP"),
                new ArrayEntry<String>("CSS"),
                new ArrayEntry<String>("CSV"),
                new ArrayEntry<String>("JS"),
                new ArrayEntry<String>("H"),
                new ArrayEntry<String>("HTM"),
                new ArrayEntry<String>("HTML"),
                new ArrayEntry<String>("SHTML"),
                new ArrayEntry<String>("INI"),
                new ArrayEntry<String>("LOG"),
                new ArrayEntry<String>("PHP3"),
                new ArrayEntry<String>("PHTML"),
                new ArrayEntry<String>("PL"),
                new ArrayEntry<String>("PERL"),
                new ArrayEntry<String>("SH"),
                new ArrayEntry<String>("SQL"),
                new ArrayEntry<String>("TXT"));
        this._port_available = equal(port_mode, true);
        this.SendMSG("Staring FTP client class" + (this._port_available
            ? ""
            : " without PORT mode support"));
        this._connected = false;
        this._ready = false;
        this._can_restore = false;
        this._code = 0;
        this._message = "";
        this._ftp_buff_size = 4096;
        this._curtype = intval(null);
        this.SetUmask(22);
        this.SetType(gConsts.getFTP_AUTOASCII());
        this.SetTimeout(30);
        this.Passive(!this._port_available);
        this._login = "anonymous";
        this._password = "anon@ftp.com";
        this._features = new Array<Object>();
        this.OS_local = gConsts.getFTP_OS_Unix();
        this.OS_remote = gConsts.getFTP_OS_Unix();
        this.features = new Array<Object>();

        if (strictEqual(Strings.strtoupper(Strings.substr("PHP_OS", 0, 3)), "WIN")) {
            this.OS_local = gConsts.getFTP_OS_Windows();
        } else if (strictEqual(Strings.strtoupper(Strings.substr("PHP_OS", 0, 3)), "MAC")) {
            this.OS_local = gConsts.getFTP_OS_Mac();
        }
    }

 // <!-- --------------------------------------------------------------------------------------- -->
 // <!--       Public functions                                                                  -->
 // <!-- --------------------------------------------------------------------------------------- -->
    public Array<Object> parselisting(String line) {
        boolean is_windows = false;
        Array<Object> lucifer = new Array<Object>();
        Array<Object> b = new Array<Object>();
        int lcount = 0;
        Array<Object> l2 = new Array<Object>();
        is_windows = equal(this.OS_remote, gConsts.getFTP_OS_Windows());

        if (is_windows && QRegExPerl.preg_match("/([0-9]{2})-([0-9]{2})-([0-9]{2}) +([0-9]{2}):([0-9]{2})(AM|PM) +([0-9]+|<DIR>) +(.+)/", line, lucifer)) {
            b = new Array<Object>();

            if (intval(lucifer.getValue(3)) < 70) {
                lucifer.putValue(3, intval(lucifer.getValue(3)) + 2000);
            } else {
                lucifer.putValue(3, intval(lucifer.getValue(3)) + 1900);
            } // 4digit year fix

            b.putValue("isdir", equal(lucifer.getValue(7), "<DIR>"));

            if (booleanval(b.getValue("isdir"))) {
                b.putValue("type", "d");
            } else {
                b.putValue("type", "f");
            }

            b.putValue("size", lucifer.getValue(7));
            b.putValue("month", lucifer.getValue(1));
            b.putValue("day", lucifer.getValue(2));
            b.putValue("year", lucifer.getValue(3));
            b.putValue("hour", lucifer.getValue(4));
            b.putValue("minute", lucifer.getValue(5));
            b.putValue("time",
                DateTime.mktime(intval(lucifer.getValue(4)) + (equal(Strings.strcasecmp(strval(lucifer.getValue(6)), "PM"), 0)
                    ? 12
                    : 0), intval(lucifer.getValue(5)), 0, intval(lucifer.getValue(1)), intval(lucifer.getValue(2)), intval(lucifer.getValue(3))));
            b.putValue("am/pm", lucifer.getValue(6));
            b.putValue("name", lucifer.getValue(8));
        } else if (!is_windows && booleanval(lucifer = QRegExPerl.preg_split("/[ ]/", line, 9, RegExPerl.PREG_SPLIT_NO_EMPTY))) {
        	//echo $line."\n";
            lcount = Array.count(lucifer);

            if (lcount < 8) {
                return new Array<Object>();
            }

            b = new Array<Object>();
            b.putValue("isdir", strictEqual(lucifer.getArrayValue(0).getValue(0), "d"));
            b.putValue("islink", strictEqual(lucifer.getArrayValue(0).getValue(0), "l"));

            if (booleanval(b.getValue("isdir"))) {
                b.putValue("type", "d");
            } else if (booleanval(b.getValue("islink"))) {
                b.putValue("type", "l");
            } else {
                b.putValue("type", "f");
            }

            b.putValue("perms", lucifer.getValue(0));
            b.putValue("number", lucifer.getValue(1));
            b.putValue("owner", lucifer.getValue(2));
            b.putValue("group", lucifer.getValue(3));
            b.putValue("size", lucifer.getValue(4));

            if (equal(lcount, 8)) {
                Strings.sscanf(strval(lucifer.getValue(5)), "%d-%d-%d", b.getRef("year"), b.getRef("month"), b.getRef("day"));
                Strings.sscanf(strval(lucifer.getValue(6)), "%d:%d", b.getRef("hour"), b.getRef("minute"));
                b.putValue("time", DateTime.mktime(intval(b.getValue("hour")), intval(b.getValue("minute")), 0, intval(b.getValue("month")), intval(b.getValue("day")), intval(b.getValue("year"))));
                b.putValue("name", lucifer.getValue(7));
            } else {
                b.putValue("month", lucifer.getValue(5));
                b.putValue("day", lucifer.getValue(6));

                if (QRegExPerl.preg_match("/([0-9]{2}):([0-9]{2})/", strval(lucifer.getValue(7)), l2)) {
                    b.putValue("year", DateTime.date("Y"));
                    b.putValue("hour", l2.getValue(1));
                    b.putValue("minute", l2.getValue(2));
                } else {
                    b.putValue("year", lucifer.getValue(7));
                    b.putValue("hour", 0);
                    b.putValue("minute", 0);
                }

                b.putValue("time", QDateTime.strtotime(QStrings.sprintf("%d %s %d %02d:%02d", b.getValue("day"), b.getValue("month"), b.getValue("year"), b.getValue("hour"), b.getValue("minute"))));
                b.putValue("name", lucifer.getValue(8));
            }
        }

        return b;
    }

    public boolean SendMSG(String message) {
        return SendMSG(message, true);
    }

    public boolean SendMSG(String message, boolean crlf) {
        if (this.Verbose) {
            echo(gVars.webEnv, message + (crlf
                ? gConsts.getCRLF()
                : ""));
            OutputControl.flush(gVars.webEnv);
        }

        return true;
    }

    public boolean SetType(int mode) {
        if (!Array.in_array(mode, this.AuthorizedTransferMode)) {
            this.SendMSG("Wrong type");

            return false;
        }

        this._type = mode;
        this.SendMSG("Transfer type: " + (equal(this._type, FTP.FTP_BINARY)
            ? "binary"
            : (equal(this._type, FTP.FTP_ASCII)
            ? "ASCII"
            : "auto ASCII")));

        return true;
    }

    public abstract boolean _exec(String cmd, String fnction);

    public boolean _exec(String cmd) {
        return _exec(cmd, "_exec");
    }

    public boolean _settype(int mode) {
        if (this._ready) {
            if (equal(mode, FTP.FTP_BINARY)) {
                if (!equal(this._curtype, FTP.FTP_BINARY)) {
                    if (!this._exec("TYPE I", "SetType")) {
                        return false;
                    }

                    this._curtype = FTP.FTP_BINARY;
                }
            } else if (!equal(this._curtype, FTP.FTP_ASCII)) {
                if (!this._exec("TYPE A", "SetType")) {
                    return false;
                }

                this._curtype = FTP.FTP_ASCII;
            }
        } else {
            return false;
        }

        return true;
    }

    public boolean Passive(boolean pasv) {
        if (is_null(pasv)) {
            this._passive = !this._passive;
        } else {
            this._passive = pasv;
        }

        if (!this._port_available && !this._passive) {
            this.SendMSG("Only passive connections available!");
            this._passive = true;

            return false;
        }

        this.SendMSG("Passive mode " + (this._passive
            ? "on"
            : "off"));

        return true;
    }

    public boolean SetServer(String host) {
        return SetServer(host, 21, true);
    }

    public boolean SetServer(String host, int port) {
        return SetServer(host, port, true);
    }

    public boolean SetServer(String host, int port, boolean reconnect) {
        String ip = null;
        String dns = null;

        if (!is_long(port)) {
            // Commented by Numiton - invalid field reference case

            //this.verbose = true;
            this.SendMSG("Incorrect port syntax");

            return false;
        } else {
            ip = Network.gethostbyname(host);
            dns = Network.gethostbyaddr(host);

            if (!booleanval(ip)) {
                ip = host;
            }

            if (!booleanval(dns)) {
                dns = host;
            }

            if (strictEqual(Network.ip2long(ip), BOOLEAN_FALSE)) {
                this.SendMSG("Wrong host name/address \"" + host + "\"");

                return false;
            }

            this._host = ip;
            this._fullhost = dns;
            this._port = port;
            this._dataport.value = port - 1;
        }

        this.SendMSG("Host \"" + this._fullhost + "(" + this._host + "):" + this._port + "\"");

        if (reconnect) {
            if (this._connected) {
                this.SendMSG("Reconnecting");

                if (!this.quit(gConsts.getFTP_FORCE())) {
                    return false;
                }

                if (!this.connect()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean SetUmask(int umask) {
        this._umask = umask;
        JFileSystemOrSocket.umask(this._umask);
        this.SendMSG("UMASK 0" + Math.decoct(this._umask));

        return true;
    }

    public abstract boolean _settimeout(int sock);

    public boolean SetTimeout(int timeout) {
        this._timeout = timeout;
        this.SendMSG("Timeout " + this._timeout);

        if (this._connected) {
            if (!this._settimeout(this._ftp_control_sock)) {
                return false;
            }
        }

        return true;
    }

    public boolean connect() {
        return connect("");
    }

    public abstract int _connect(String host, int port);

    public abstract boolean _readmsg(String fnction);

    public boolean _readmsg() {
        return _readmsg("_readmsg");
    }

    public boolean connect(String server) {
        Array<String> syst = new Array<String>();

        if (!empty(server)) {
            if (!this.SetServer(server)) {
                return false;
            }
        }

        if (this._ready) {
            return true;
        }

        this.SendMSG("Local OS : " + this.OS_FullName.getValue(this.OS_local));

        if (!booleanval(this._ftp_control_sock = this._connect(this._host, this._port))) {
            this.SendMSG("Error : Cannot connect to remote host \"" + this._fullhost + " :" + this._port + "\"");

            return false;
        }

        this.SendMSG("Connected to remote host \"" + this._fullhost + ":" + this._port + "\". Waiting for greeting.");

        do {
            if (!this._readmsg()) {
                return false;
            }

            if (!this._checkCode()) {
                return false;
            }

            this._lastaction = DateTime.time();
        } while (this._code < 200);

        this._ready = true;
        syst = this.systype();

        if (!booleanval(syst)) {
            this.SendMSG("Can\'t detect remote OS");
        } else {
            if (QRegExPerl.preg_match("/win|dos|novell/i", syst.getValue(0))) {
                this.OS_remote = gConsts.getFTP_OS_Windows();
            } else if (QRegExPerl.preg_match("/os/i", syst.getValue(0))) {
                this.OS_remote = gConsts.getFTP_OS_Mac();
            } else if (QRegExPerl.preg_match("/(li|u)nix/i", syst.getValue(0))) {
                this.OS_remote = gConsts.getFTP_OS_Unix();
            } else {
                this.OS_remote = gConsts.getFTP_OS_Mac();
            }

            this.SendMSG("Remote OS: " + this.OS_FullName.getValue(this.OS_remote));
        }

        if (!this.features()) {
            this.SendMSG("Can\'t get features list. All supported - disabled");
        } else {
            this.SendMSG("Supported features: " + Strings.implode(", ", Array.array_keys(this._features)));
        }

        return true;
    }

    public abstract void _quit();

    public boolean quit() {
        return quit(false);
    }

    public boolean quit(boolean force) {
        if (this._ready) {
            if (!this._exec("QUIT") && !force) {
                return false;
            }

            if (!this._checkCode() && !force) {
                return false;
            }

            this._ready = false;
            this.SendMSG("Session finished");
        }

        this._quit();

        return true;
    }

    public boolean login(Object user, Object pass) {
        if (!is_null(user)) {
            this._login = strval(user);
        } else {
            this._login = "anonymous";
        }

        if (!is_null(pass)) {
            this._password = strval(pass);
        } else {
            this._password = "anon@anon.com";
        }

        if (!this._exec("USER " + this._login, "login")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        if (!equal(this._code, 230)) {
            if (!this._exec((equal(this._code, 331)
                        ? "PASS "
                            : "ACCT ") + this._password, "login")) {
                return false;
            }

            if (!this._checkCode()) {
                return false;
            }
        }

        this.SendMSG("Authentication succeeded");

        if (empty(this._features)) {
            if (!this.features()) {
                this.SendMSG("Can\'t get features list. All supported - disabled");
            } else {
                this.SendMSG("Supported features: " + Strings.implode(", ", Array.array_keys(this._features)));
            }
        }

        return true;
    }

    public String pwd() {
        if (!this._exec("PWD", "pwd")) {
            return strval(false);
        }

        if (!this._checkCode()) {
            return strval(false);
        }

        return QRegExPosix.ereg_replace("^[0-9]{3} \"(.+)\".+", "\\1", this._message);
    }

    public boolean cdup() {
        if (!this._exec("CDUP", "cdup")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean chdir(String pathname) {
        if (!this._exec("CWD " + pathname, "chdir")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean rmdir(String pathname) {
        if (!this._exec("RMD " + pathname, "rmdir")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean mkdir(String pathname) {
        if (!this._exec("MKD " + pathname, "mkdir")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean rename(String from, String to) {
        if (!this._exec("RNFR " + from, "rename")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        if (equal(this._code, 350)) {
            if (!this._exec("RNTO " + to, "rename")) {
                return false;
            }

            if (!this._checkCode()) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public int filesize(String pathname) {
        if (!isset(this._features.getValue("SIZE"))) {
            this.PushError("filesize", "not supported by server");

            return 0;
        }

        if (!this._exec("SIZE " + pathname, "filesize")) {
            return 0;
        }

        if (!this._checkCode()) {
            return 0;
        }

        return intval(QRegExPosix.ereg_replace("^[0-9]{3} ([0-9]+)" + gConsts.getCRLF(), "\\1", this._message));
    }

    public boolean abort() {
        if (!this._exec("ABOR", "abort")) {
            return false;
        }

        if (!this._checkCode()) {
            if (!equal(this._code, 426)) {
                return false;
            }

            if (!this._readmsg("abort")) {
                return false;
            }

            if (!this._checkCode()) {
                return false;
            }
        }

        return true;
    }

    public int mdtm(String pathname) {
        String mdtm = null;
        Array<Integer> date = new Array<Integer>();
        int timestamp = 0;

        if (!isset(this._features.getValue("MDTM"))) {
            this.PushError("mdtm", "not supported by server");

            return intval(false);
        }

        if (!this._exec("MDTM " + pathname, "mdtm")) {
            return intval(false);
        }

        if (!this._checkCode()) {
            return intval(false);
        }

        mdtm = QRegExPosix.ereg_replace("^[0-9]{3} ([0-9]+)" + gConsts.getCRLF(), "\\1", this._message);
        date = Strings.sscanf(mdtm, "%4d%2d%2d%2d%2d%2d");
        timestamp = DateTime.mktime(date.getValue(3), date.getValue(4), date.getValue(5), date.getValue(1), date.getValue(2), date.getValue(0));

        return timestamp;
    }

    public Array<String> systype() {
        Array<String> DATA = new Array<String>();

        if (!this._exec("SYST", "systype")) {
            return new Array<String>();
        }

        if (!this._checkCode()) {
            return new Array<String>();
        }

        DATA = Strings.explode(" ", this._message);

        return new Array<String>(new ArrayEntry<String>(DATA.getValue(1)), new ArrayEntry<String>(DATA.getValue(3)));
    }

    public boolean delete(String pathname) {
        if (!this._exec("DELE " + pathname, "delete")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean site(String command, String fnction) {
        if (!this._exec("SITE " + command, fnction)) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean chmod(String pathname, int mode) {
        if (!this.site(QStrings.sprintf("CHMOD %o %s", mode, pathname), "chmod")) {
            return false;
        }

        return true;
    }

    public boolean restore(int from) {
        if (!isset(this._features.getValue("REST"))) {
            this.PushError("restore", "not supported by server");

            return false;
        }

        if (!equal(this._curtype, FTP.FTP_BINARY)) {
            this.PushError("restore", "can\'t restore in ASCII mode");

            return false;
        }

        if (!this._exec("REST " + from, "resore")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return true;
    }

    public boolean features() {
        Array<Object> f = new Array<Object>();
        String v;
        Object k = null;

        if (!this._exec("FEAT", "features")) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        f = QRegExPerl.preg_split("/[" + gConsts.getCRLF() + "]+/", QRegExPerl.preg_replace("/[0-9]{3}[ -].*[" + gConsts.getCRLF() + "]+/", "", this._message), -1, RegExPerl.PREG_SPLIT_NO_EMPTY);
        this._features = new Array<Object>();

        for (Map.Entry javaEntry102 : f.entrySet()) {
            k = javaEntry102.getKey();
            v = strval(javaEntry102.getValue());

            Array<String> vArray = new Array<String>(Strings.explode(" ", Strings.trim(v)));
            this._features.putValue(Array.array_shift(vArray), v);
        }

        return true;
    }

    public Array<String> rawlist(String pathname, String arg) {
        return this._list((booleanval(arg)
            ? (" " + arg)
            : "") + (booleanval(pathname)
            ? (" " + pathname)
            : ""), "LIST", "rawlist");
    }

    public Array<String> nlist(String pathname) {
        Object arg = null;

        return this._list((booleanval(arg)
            ? (" " + arg)
            : "") + (booleanval(pathname)
            ? (" " + pathname)
            : ""), "NLST", "nlist");
    }

    public boolean is_exists(String pathname) {
        return this.file_exists(pathname);
    }

    public boolean file_exists(String pathname) {
        boolean exists = false;
        exists = true;

        if (!this._exec("RNFR " + pathname, "rename")) {
            exists = false;
        } else {
            if (!this._checkCode()) {
                exists = false;
            }

            this.abort();
        }

        if (exists) {
            this.SendMSG("Remote file " + pathname + " exists");
        } else {
            this.SendMSG("Remote file " + pathname + " does not exist");
        }

        return exists;
    }

    public abstract boolean _data_prepare(int mode);

    public boolean _data_prepare() {
        return _data_prepare(gConsts.getFTP_ASCII());
    }

    public abstract boolean _data_close();

    public abstract String _data_read(int mode, int fp);

    public String _data_read() {
        return _data_read(gConsts.getFTP_ASCII(), 0);
    }

    public String fget(int fp, String remotefile) {
        return fget(fp, remotefile, 0);
    }

    public String fget(int fp, String remotefile, int rest) {
        Array<Object> pi = new Array<Object>();
        int mode = 0;
        String out = null;

        if (this._can_restore && !equal(rest, 0)) {
            FileSystemOrSocket.fseek(gVars.webEnv, fp, rest);
        }

        pi = (Array<Object>) FileSystemOrSocket.pathinfo(remotefile);

        if (equal(this._type, FTP.FTP_ASCII) || (equal(this._type, gConsts.getFTP_AUTOASCII()) && Array.in_array(Strings.strtoupper(strval(pi.getValue("extension"))), this.AutoAsciiExt))) {
            mode = FTP.FTP_ASCII;
        } else {
            mode = FTP.FTP_BINARY;
        }

        if (!this._data_prepare(mode)) {
            return "";
        }

        if (this._can_restore && !equal(rest, 0)) {
            this.restore(rest);
        }

        if (!this._exec("RETR " + remotefile, "get")) {
            this._data_close();

            return "";
        }

        if (!this._checkCode()) {
            this._data_close();

            return "";
        }

        out = this._data_read(mode, fp);
        this._data_close();

        if (!this._readmsg()) {
            return "";
        }

        if (!this._checkCode()) {
            return "";
        }

        return out;
    }

    public String get(String remotefile) {
        return get(remotefile, null, 0);
    }

    public String get(String remotefile, String localfile) {
        return get(remotefile, localfile, 0);
    }

    public String get(String remotefile, String localfile, int rest) {
        int fp = 0;
        Array<Object> pi = new Array<Object>();
        int mode = 0;
        String out = null;

        if (is_null(localfile)) {
            localfile = remotefile;
        }

        if (FileSystemOrSocket.file_exists(gVars.webEnv, localfile)) {
            this.SendMSG("Warning : local file will be overwritten");
        }

        fp = FileSystemOrSocket.fopen(gVars.webEnv, localfile, "w");

        if (!booleanval(fp)) {
            this.PushError("get", "can\'t open local file", "Cannot create \"" + localfile + "\"");

            return "";
        }

        if (this._can_restore && !equal(rest, 0)) {
            FileSystemOrSocket.fseek(gVars.webEnv, fp, rest);
        }

        pi = (Array<Object>) FileSystemOrSocket.pathinfo(remotefile);

        if (equal(this._type, FTP.FTP_ASCII) || (equal(this._type, gConsts.getFTP_AUTOASCII()) && Array.in_array(Strings.strtoupper(strval(pi.getValue("extension"))), this.AutoAsciiExt))) {
            mode = FTP.FTP_ASCII;
        } else {
            mode = FTP.FTP_BINARY;
        }

        if (!this._data_prepare(mode)) {
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return "";
        }

        if (this._can_restore && !equal(rest, 0)) {
            this.restore(rest);
        }

        if (!this._exec("RETR " + remotefile, "get")) {
            this._data_close();
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return "";
        }

        if (!this._checkCode()) {
            this._data_close();
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return "";
        }

        out = this._data_read(mode, fp);
        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        this._data_close();

        if (!this._readmsg()) {
            return "";
        }

        if (!this._checkCode()) {
            return "";
        }

        return out;
    }

    public abstract boolean _data_write(int mode, int fp);

    public boolean fput(String remotefile, int fp) {
        int rest = 0;
        Array<Object> pi = new Array<Object>();
        int mode = 0;
        boolean ret;

        if (this._can_restore && !equal(rest, 0)) {
            FileSystemOrSocket.fseek(gVars.webEnv, fp, rest);
        }

        pi = FileSystemOrSocket.pathinfo(remotefile);

        if (equal(this._type, FTP.FTP_ASCII) || (equal(this._type, gConsts.getFTP_AUTOASCII()) && Array.in_array(Strings.strtoupper(strval(pi.getValue("extension"))), this.AutoAsciiExt))) {
            mode = FTP.FTP_ASCII;
        } else {
            mode = FTP.FTP_BINARY;
        }

        if (!this._data_prepare(mode)) {
            return false;
        }

        if (this._can_restore && !equal(rest, 0)) {
            this.restore(rest);
        }

        if (!this._exec("STOR " + remotefile, "put")) {
            this._data_close();

            return false;
        }

        if (!this._checkCode()) {
            this._data_close();

            return false;
        }

        ret = this._data_write(mode, fp);
        this._data_close();

        if (!this._readmsg()) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return ret;
    }

    public boolean put(String localfile) {
        return put(localfile, null, 0);
    }

    public boolean put(String localfile, String remotefile) {
        return put(localfile, remotefile, 0);
    }

    public boolean put(String localfile, String remotefile, int rest) {
        int fp = 0;
        Array<Object> pi = new Array<Object>();
        int mode = 0;
        boolean ret;

        if (is_null(remotefile)) {
            remotefile = localfile;
        }

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, localfile)) {
            this.PushError("put", "can\'t open local file", "No such file or directory \"" + localfile + "\"");

            return false;
        }

        fp = FileSystemOrSocket.fopen(gVars.webEnv, localfile, "r");

        if (!booleanval(fp)) {
            this.PushError("put", "can\'t open local file", "Cannot read file \"" + localfile + "\"");

            return false;
        }

        if (this._can_restore && !equal(rest, 0)) {
            FileSystemOrSocket.fseek(gVars.webEnv, fp, rest);
        }

        pi = FileSystemOrSocket.pathinfo(localfile);

        if (equal(this._type, FTP.FTP_ASCII) || (equal(this._type, gConsts.getFTP_AUTOASCII()) && Array.in_array(Strings.strtoupper(strval(pi.getValue("extension"))), this.AutoAsciiExt))) {
            mode = FTP.FTP_ASCII;
        } else {
            mode = FTP.FTP_BINARY;
        }

        if (!this._data_prepare(mode)) {
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return false;
        }

        if (this._can_restore && !equal(rest, 0)) {
            this.restore(rest);
        }

        if (!this._exec("STOR " + remotefile, "put")) {
            this._data_close();
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return false;
        }

        if (!this._checkCode()) {
            this._data_close();
            FileSystemOrSocket.fclose(gVars.webEnv, fp);

            return false;
        }

        ret = this._data_write(mode, fp);
        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        this._data_close();

        if (!this._readmsg()) {
            return false;
        }

        if (!this._checkCode()) {
            return false;
        }

        return ret;
    }

    public boolean mput() {
        return mput(".", null, false);
    }

    public boolean mput(String local) {
        return mput(local, null, false);
    }

    public boolean mput(String local, String remote) {
        return mput(local, remote, false);
    }

    public boolean mput(String local, String remote, boolean continious) {
        int handle = 0;
        Array<Object> list = new Array<Object>();
        String file = null;
        boolean ret = false;
        Object el = null;
        boolean t = false;
        local = FileSystemOrSocket.realpath(gVars.webEnv, local);

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, local)) {
            this.PushError("mput", "can\'t open local folder", "Cannot stat folder \"" + local + "\"");

            return false;
        }

        if (!FileSystemOrSocket.is_dir(gVars.webEnv, local)) {
            return this.put(local, remote);
        }

        if (empty(remote)) {
            remote = ".";
        } else if (!this.file_exists(remote) && !this.mkdir(remote)) {
            return false;
        }

        if (booleanval(handle = Directories.opendir(gVars.webEnv, local))) {
            list = new Array<Object>();

            while (!strictEqual(STRING_FALSE, file = Directories.readdir(gVars.webEnv, handle))) {
                if (!equal(file, ".") && !equal(file, "..")) {
                    list.putValue(file);
                }
            }

            Directories.closedir(gVars.webEnv, handle);
        } else {
            this.PushError("mput", "can\'t open local folder", "Cannot read folder \"" + local + "\"");

            return false;
        }

        if (empty(list)) {
            return true;
        }

        ret = true;

        for (Map.Entry javaEntry103 : list.entrySet()) {
            el = javaEntry103.getValue();

            if (FileSystemOrSocket.is_dir(gVars.webEnv, local + "/" + strval(el))) {
                t = this.mput(local + "/" + el, remote + "/" + el);
            } else {
                t = this.put(local + "/" + el, remote + "/" + el);
            }

            if (!t) {
                ret = false;

                if (!continious) {
                    break;
                }
            }
        }

        return ret;
    }

    public boolean mget(String remote, String local, Object continious) {
        Object k = null;
        String v = null;
        boolean ret = false;
        Array<Object> el = new Array<Object>();
        int t = 0;
        Array<?> list = this.rawlist(remote, "-lA");

        if (strictEqual(list, null)) {
            this.PushError("mget", "can\'t read remote folder list", "Can\'t read remote folder \"" + remote + "\" contents");

            return false;
        }

        if (empty(list)) {
            return true;
        }

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, local)) {
            if (!JFileSystemOrSocket.mkdir(gVars.webEnv, local)) {
                this.PushError("mget", "can\'t create local folder", "Cannot create folder \"" + local + "\"");

                return false;
            }
        }

        for (Map.Entry javaEntry104 : list.entrySet()) {
            k = javaEntry104.getKey();
            v = strval(javaEntry104.getValue());
            list.putValue(k, this.parselisting(v));

            if (equal(list.getArrayValue(k).getValue("name"), ".") || equal(list.getArrayValue(k).getValue("name"), "..")) {
                list.arrayUnset(k);
            }
        }

        ret = true;

        for (Map.Entry javaEntry105 : list.entrySet()) {
            el = (Array<Object>) javaEntry105.getValue();

            if (equal(el.getValue("type"), "d")) {
                if (!this.mget(remote + "/" + el.getValue("name"), local + "/" + el.getValue("name"), continious)) {
                    this.PushError("mget", "can\'t copy folder", "Can\'t copy remote folder \"" + remote + "/" + el.getValue("name") + "\" to local \"" + local + "/" + el.getValue("name") + "\"");
                    ret = false;

                    if (!booleanval(continious)) {
                        break;
                    }
                }
            } else {
                if (!booleanval(this.get(remote + "/" + el.getValue("name"), local + "/" + el.getValue("name")))) {
                    this.PushError("mget", "can\'t copy file", "Can\'t copy remote file \"" + remote + "/" + el.getValue("name") + "\" to local \"" + local + "/" + el.getValue("name") + "\"");
                    ret = false;

                    if (!booleanval(continious)) {
                        break;
                    }
                }
            }

            JFileSystemOrSocket.chmod(gVars.webEnv, local + "/" + strval(el.getValue("name")), intval(el.getValue("perms")));
            t = QDateTime.strtotime(strval(el.getValue("date")));

            if (!strictEqual(t, BOOLEAN_FALSE) && !strictEqual(t, false)) {
                JFileSystemOrSocket.touch(gVars.webEnv, local + "/" + strval(el.getValue("name")), t);
            }
        }

        return ret;
    }

    public boolean mdel(String remote) {
        return mdel(remote, false);
    }

    public boolean mdel(String remote, boolean continious) {
        Array<?> list = null;
        Object k = null;
        String v = null;
        boolean ret = false;
        Array<Object> el = new Array<Object>();
        list = this.rawlist(remote, "-la");

        if (strictEqual(list, null)) {
            this.PushError("mdel", "can\'t read remote folder list", "Can\'t read remote folder \"" + remote + "\" contents");

            return false;
        }

        for (Map.Entry javaEntry106 : list.entrySet()) {
            k = javaEntry106.getKey();
            v = strval(javaEntry106.getValue());
            list.putValue(k, this.parselisting(v));

            if (equal(list.getArrayValue(k).getValue("name"), ".") || equal(list.getArrayValue(k).getValue("name"), "..")) {
                list.arrayUnset(k);
            }
        }

        ret = true;

        for (Map.Entry javaEntry107 : list.entrySet()) {
            el = (Array<Object>) javaEntry107.getValue();

            if (empty(el)) {
                continue;
            }

            if (equal(el.getValue("type"), "d")) {
                if (!this.mdel(remote + "/" + el.getValue("name"), continious)) {
                    ret = false;

                    if (!continious) {
                        break;
                    }
                }
            } else {
                if (!this.delete(remote + "/" + el.getValue("name"))) {
                    this.PushError("mdel", "can\'t delete file", "Can\'t delete remote file \"" + remote + "/" + el.getValue("name") + "\"");
                    ret = false;

                    if (!continious) {
                        break;
                    }
                }
            }
        }

        if (!this.rmdir(remote)) {
            this.PushError("mdel", "can\'t delete folder", "Can\'t delete remote folder \"" + remote + "/" + el.getValue("name") + "\"");
            ret = false;
        }

        return ret;
    }

    public boolean mmkdir(String dir, int mode) {
        boolean r = false;

        if (empty(dir)) {
            return false;
        }

        if (this.is_exists(dir) || equal(dir, "/")) {
            return true;
        }

        if (!this.mmkdir(FileSystemOrSocket.dirname(dir), mode)) {
            return false;
        }

        r = this.mkdir(dir);

        /*, mode*/
        this.chmod(dir, mode);

        return r;
    }

    public Array<Object> glob(String pattern, Object handle)/* Do not change type */
     {
        String path = null;
        Array<Object> output = new Array<Object>();
        String slash = null;
        int lastpos = 0;
        String dir = null;
        path = null;

        if (equal("PHP_OS", "WIN32")) {
            slash = "\\";
        } else {
            slash = "/";
        }

        lastpos = Strings.strrpos(pattern, slash);

        if (!strictEqual(lastpos, BOOLEAN_FALSE)) {
            path = Strings.substr(pattern, 0, -lastpos - 1);
            pattern = Strings.substr(pattern, lastpos);
        } else {
            path = Directories.getcwd(gVars.webEnv);
        }

        if (is_array(handle) && !empty(handle)) {
            while (booleanval(dir = strval(Array.each((Array<Object>) handle)))) {
                if (booleanval(this.glob_pattern_match(pattern, dir))) {
                    output.putValue(dir);
                }
            }
        } else {
            ((Ref<Integer>) handle).value = Directories.opendir(gVars.webEnv, path);

            if (strictEqual(handle, INVALID_RESOURCE)) {
                return new Array<Object>();
            }

            while (booleanval(dir = Directories.readdir(gVars.webEnv, ((Ref<Integer>) handle).value))) {
                if (booleanval(this.glob_pattern_match(pattern, dir))) {
                    output.putValue(dir);
                }
            }

            Directories.closedir(gVars.webEnv, ((Ref<Integer>) handle).value);
        }

        if (is_array(output)) {
            return output;
        }

        return new Array<Object>();
    }

    public int glob_pattern_match(String pattern, String string) {
        Array<Object> out = new Array<Object>();
        Array<String> chunks = new Array<String>();
        Array<String> escape = new Array<String>();
        String probe = null;
        Object tester = null;
        chunks = Strings.explode(";", pattern);

        for (Map.Entry javaEntry108 : chunks.entrySet()) {
            pattern = strval(javaEntry108.getValue());
            escape = new Array<String>(
                    new ArrayEntry<String>("$"),
                    new ArrayEntry<String>("^"),
                    new ArrayEntry<String>("."),
                    new ArrayEntry<String>("{"),
                    new ArrayEntry<String>("}"),
                    new ArrayEntry<String>("("),
                    new ArrayEntry<String>(")"),
                    new ArrayEntry<String>("["),
                    new ArrayEntry<String>("]"),
                    new ArrayEntry<String>("|"));

            while (!strictEqual(Strings.strpos(pattern, "**"), BOOLEAN_FALSE))
                pattern = Strings.str_replace("**", "*", pattern);

            for (Map.Entry javaEntry109 : escape.entrySet()) {
                probe = strval(javaEntry109.getValue());
                pattern = Strings.str_replace(probe, "\\" + probe, pattern);
            }

            pattern = Strings.str_replace("?*", "*", Strings.str_replace("*?", "*", Strings.str_replace("*", ".*", Strings.str_replace("?", ".{1,1}", pattern))));
            out.putValue(pattern);
        }

        if (equal(Array.count(out), 1)) {
            return this.glob_regexp("^" + out.getValue(0) + "$", string);
        } else {
            for (Map.Entry javaEntry110 : out.entrySet()) {
                tester = javaEntry110.getValue();
            }
        }

        // Commented by Numiton

        //				if (booleanval(this.my_regexp("^" + tester + "$", string))) {

        //					return intval(true);

        //				}
        return 0;
    }

    public int glob_regexp(String pattern, String probe) {
        boolean sensitive = false;
        sensitive = !equal("PHP_OS", "WIN32");

        return sensitive
        ? RegExPosix.ereg(pattern, probe)
        : RegExPosix.eregi(pattern, probe);
    }

    public Array<Object> dirlist(String remote) {
        Array<?> list = null;
        Array<Object> dirlist = new Array<Object>();
        Array<Object> entry = null;
        String v = null;
        Object k = null;
        list = this.rawlist(remote, "-la");

        if (strictEqual(list, null)) {
            this.PushError("dirlist", "can\'t read remote folder list", "Can\'t read remote folder \"" + remote + "\" contents");

            return new Array<Object>();
        }

        dirlist = new Array<Object>();

        for (Map.Entry javaEntry111 : list.entrySet()) {
            k = javaEntry111.getKey();
            v = strval(javaEntry111.getValue());
            entry = this.parselisting(v);

            if (empty(entry)) {
                continue;
            }

            if (equal(entry.getValue("name"), ".") || equal(entry.getValue("name"), "..")) {
                continue;
            }

            dirlist.putValue(entry.getValue("name"), entry);
        }

        return dirlist;
    }

 // <!-- --------------------------------------------------------------------------------------- -->
 // <!--       Private functions                                                                 -->
 // <!-- --------------------------------------------------------------------------------------- -->
    public boolean _checkCode() {
        return (this._code < 400) && (this._code > 0);
    }

    public Array<String> _list(String arg, String cmd, String fnction) {
        Array<String> out = null;

        if (!this._data_prepare()) {
            return null;
        }

        if (!this._exec(cmd + arg, fnction)) {
            this._data_close();

            return null;
        }

        if (!this._checkCode()) {
            this._data_close();

            return null;
        }

        out = new Array();

        if (this._code < 200) {
            String outStr = this._data_read();
            this._data_close();

            if (!this._readmsg()) {
                return null;
            }

            if (!this._checkCode()) {
                return null;
            }

            if (strictEqual(outStr, null)) {
                return null;
            }

            out = QRegExPerl.preg_split("/[" + gConsts.getCRLF() + "]+/", outStr, -1, RegExPerl.PREG_SPLIT_NO_EMPTY);
//			$this->SendMSG(implode($this->_eol_code[$this->OS_local], $out));
        }

        return out;
    }

    public int PushError(String fctname, String msg) {
        return PushError(fctname, msg, "");
    }

 // <!-- --------------------------------------------------------------------------------------- -->
 // <!-- Partie : gestion des erreurs                                                            -->
 // <!-- --------------------------------------------------------------------------------------- -->
 // Gnre une erreur pour traitement externe  la classe
    public int PushError(String fctname, String msg, String desc) {
        Array<Object> error = new Array<Object>();
        String tmp = null;
        error = new Array<Object>();
        error.putValue("time", DateTime.time());
        error.putValue("fctname", fctname);
        error.putValue("msg", msg);
        error.putValue("desc", desc);

        if (booleanval(desc)) {
            tmp = " (" + desc + ")";
        } else {
            tmp = "";
        }

        this.SendMSG(fctname + ": " + msg + tmp);

        return Array.array_push(this._error_array.value, error);
    }

    /**
     * Rcupre une erreur externe Rcupre une erreur externe
     */
    public Object PopError() {
        if (booleanval(Array.count(this._error_array))) {
            return Array.array_pop(this._error_array);
        } else {
            return false;
        }
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
