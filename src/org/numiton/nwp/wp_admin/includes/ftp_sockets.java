/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ftp_sockets.java,v 1.3 2008/10/03 18:45:31 numiton Exp $
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

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.DateTime;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.ftp.FTP;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.Ref;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
import com.numiton.sockets.Sockets;
import com.numiton.string.Strings;


/**
 * PemFTP - A Ftp implementation in pure PHP
 * @package PemFTP
 * @since 2.5
 * @version 1.0
 * @copyright Alexey Dotsenko
 * @author Alexey Dotsenko
 * @link http://www.phpclasses.org/browse/package/1743.html Site
 * @license LGPL License http://www.opensource.org/licenses/lgpl-license.html
 */
public class ftp_sockets extends ftp_base implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(ftp_sockets.class.getName());
    
    public int stream;

    // Commented by Numiton
    //	public ftp(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object verb, Object le) {
    //		setContext(javaGlobalVariables, javaGlobalConstants);
    //		this.__construct(verb, le);
    //	}
    public ftp_sockets(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        this(javaGlobalVariables, javaGlobalConstants, false, false);
    }

    public ftp_sockets(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, boolean verb, boolean le) {
        // Commented by Numiton

        //		setContext(javaGlobalVariables, javaGlobalConstants);
        super(javaGlobalVariables, javaGlobalConstants, true, verb, le);
    }

 // <!-- --------------------------------------------------------------------------------------- -->
 // <!--       Private functions                                                                 -->
 // <!-- --------------------------------------------------------------------------------------- -->
    public boolean _settimeout(int sock) {
        if (!Sockets.socket_set_option(gVars.webEnv, sock, Sockets.SOL_SOCKET, Sockets.SO_RCVTIMEO, new Array<Object>(new ArrayEntry<Object>("sec", this._timeout), new ArrayEntry<Object>("usec", 0)))) {
            this.PushError("_connect", "socket set receive timeout", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, sock)));
            Sockets.socket_close(gVars.webEnv, sock);

            return false;
        }

        if (!Sockets.socket_set_option(gVars.webEnv, sock, Sockets.SOL_SOCKET, Sockets.SO_SNDTIMEO, new Array<Object>(new ArrayEntry<Object>("sec", this._timeout), new ArrayEntry<Object>("usec", 0)))) {
            this.PushError("_connect", "socket set send timeout", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, sock)));
            Sockets.socket_close(gVars.webEnv, sock);

            return false;
        }

        return true;
    }

    public int _connect(String host, int port) {
        int sock = 0;
        boolean res = false;
        this.SendMSG("Creating socket");

        if (!booleanval(sock = Sockets.socket_create(gVars.webEnv, Sockets.AF_INET, Sockets.SOCK_STREAM, Sockets.SOL_TCP))) {
            this.PushError("_connect", "socket create failed", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, sock)));

            return intval(false);
        }

        if (!this._settimeout(sock)) {
            return intval(false);
        }

        this.SendMSG("Connecting to \"" + host + ":" + port + "\"");

        if (!(res = Sockets.socket_connect(gVars.webEnv, sock, host, port))) {
            this.PushError("_connect", "socket connect failed", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, sock)));
            Sockets.socket_close(gVars.webEnv, sock);

            return intval(false);
        }

        this._connected = true;

        return sock;
    }

    public boolean _readmsg(String fnction) {
        boolean result = false;
        boolean go = false;
        String tmp = null;
        Array<Object> regs = new Array<Object>();

        if (!this._connected) {
            this.PushError(fnction, "Connect first");

            return false;
        }

        result = true;
        this._message = "";
        this._code = 0;
        go = true;

        do {
            tmp = Sockets.socket_read(gVars.webEnv, this._ftp_control_sock, 4096, Sockets.PHP_BINARY_READ);

            if (strictEqual(tmp, STRING_FALSE)) {
                go = result = false;
                this.PushError(fnction, "Read failed", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_control_sock)));
            } else {
                this._message = this._message + tmp;
                go = !QRegExPerl.preg_match("/^([0-9]{3})(-.+\\1)? [^" + gConsts.getCRLF() + "]+" + gConsts.getCRLF() + "$/Us", this._message, regs);
            }
        } while (go);

        if (this.LocalEcho) {
            echo(gVars.webEnv, "GET < " + Strings.rtrim(this._message, gConsts.getCRLF()) + gConsts.getCRLF());
        }

        this._code = intval(regs.getValue(1));

        return result;
    }

    public boolean _exec(String cmd, String fnction) {
        int status = 0;

        if (!this._ready) {
            this.PushError(fnction, "Connect first");

            return false;
        }

        if (this.LocalEcho) {
            echo(gVars.webEnv, "PUT > " + cmd + gConsts.getCRLF());
        }

        status = Sockets.socket_write(gVars.webEnv, this._ftp_control_sock, cmd + gConsts.getCRLF());

        if (strictEqual(status, INT_FALSE)) {
            this.PushError(fnction, "socket write failed", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this.stream)));

            return false;
        }

        this._lastaction = DateTime.time();

        if (!this._readmsg(fnction)) {
            return false;
        }

        return true;
    }

    public boolean _data_prepare(int mode) {
        Array<String> ip_port = new Array<String>();
        Ref<String> addr = new Ref<String>();
        Ref<Integer> port = new Ref<Integer>();

        if (!this._settype(mode)) {
            return false;
        }

        this.SendMSG("Creating data socket");
        this._ftp_data_sock = Sockets.socket_create(gVars.webEnv, Sockets.AF_INET, Sockets.SOCK_STREAM, Sockets.SOL_TCP);

        if (this._ftp_data_sock < 0) {
            this.PushError("_data_prepare", "socket create failed", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_data_sock)));

            return false;
        }

        if (!this._settimeout(this._ftp_data_sock)) {
            this._data_close();

            return false;
        }

        if (this._passive) {
            if (!this._exec("PASV", "pasv")) {
                this._data_close();

                return false;
            }

            if (!this._checkCode()) {
                this._data_close();

                return false;
            }

            ip_port = Strings.explode(",", QRegExPosix.ereg_replace("^.+ \\(?([0-9]{1,3},[0-9]{1,3},[0-9]{1,3},[0-9]{1,3},[0-9]+,[0-9]+)\\)?.*" + gConsts.getCRLF() + "$", "\\1", this._message));
            this._datahost.value = ip_port.getValue(0) + "." + ip_port.getValue(1) + "." + ip_port.getValue(2) + "." + ip_port.getValue(3);
            this._dataport.value = (intval(ip_port.getValue(4)) << 8) + intval(ip_port.getValue(5));
            this.SendMSG("Connecting to " + this._datahost + ":" + this._dataport);

            if (!Sockets.socket_connect(gVars.webEnv, this._ftp_data_sock, this._datahost.value, this._dataport.value)) {
                this.PushError("_data_prepare", "socket_connect", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_data_sock)));
                this._data_close();

                return false;
            } else {
                this._ftp_temp_sock = this._ftp_data_sock;
            }
        } else {
            if (!Sockets.socket_getsockname(gVars.webEnv, this._ftp_control_sock, addr, port)) {
                this.PushError("_data_prepare", "can\'t get control socket information", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_control_sock)));
                this._data_close();

                return false;
            }

            if (!Sockets.socket_bind(gVars.webEnv, this._ftp_data_sock, addr.value)) {
                this.PushError("_data_prepare", "can\'t bind data socket", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_data_sock)));
                this._data_close();

                return false;
            }

            if (!Sockets.socket_listen(gVars.webEnv, this._ftp_data_sock)) {
                this.PushError("_data_prepare", "can\'t listen data socket", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_data_sock)));
                this._data_close();

                return false;
            }

            if (!Sockets.socket_getsockname(gVars.webEnv, this._ftp_data_sock, this._datahost, this._dataport)) {
                this.PushError("_data_prepare", "can\'t get data socket information", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_data_sock)));
                this._data_close();

                return false;
            }

            if (!this._exec("PORT " + Strings.str_replace(".", ",", this._datahost + "." + strval(this._dataport.value >> 8) + "." + strval(this._dataport.value & 255)), "_port")) {
                this._data_close();

                return false;
            }

            if (!this._checkCode()) {
                this._data_close();

                return false;
            }
        }

        return true;
    }

    public String _data_read(int mode, int fp) {
        String out = null;
        String block = null;
        String NewLine = this._eol_code.getValue(this.OS_local);

        if (is_resource(fp)) {
            out = strval(0);
        } else {
            out = "";
        }

        if (!this._passive) {
            this.SendMSG("Connecting to " + this._datahost + ":" + this._dataport);
            this._ftp_temp_sock = Sockets.socket_accept(gVars.webEnv, this._ftp_data_sock);

            if (strictEqual(this._ftp_temp_sock, INT_FALSE)) {
                this.PushError("_data_read", "socket_accept", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_temp_sock)));
                this._data_close();

                return null;
            }
        }

        while (!strictEqual(block = Sockets.socket_read(gVars.webEnv, this._ftp_temp_sock, this._ftp_buff_size, Sockets.PHP_BINARY_READ), STRING_FALSE)) {
            if (strictEqual(block, STRING_FALSE)) {
                break;
            }

            if (!equal(mode, FTP.FTP_BINARY)) {
                block = QRegExPerl.preg_replace("/\r\n|\r|\n/", this._eol_code.getValue(this.OS_local), block);
            }

            if (is_resource(fp)) {
                out = out + FileSystemOrSocket.fwrite(gVars.webEnv, fp, block, Strings.strlen(block));
            } else {
                out = out + block;
            }
        }

        return out;
    }

    public boolean _data_write(int mode, int fp) {
        String out = null;
        String block = null;
        String NewLine = this._eol_code.getValue(this.OS_local);

        if (is_resource(fp)) {
            out = strval(0);
        } else {
            out = "";
        }

        if (!this._passive) {
            this.SendMSG("Connecting to " + this._datahost + ":" + this._dataport);
            this._ftp_temp_sock = Sockets.socket_accept(gVars.webEnv, this._ftp_data_sock);

            if (strictEqual(this._ftp_temp_sock, INT_FALSE)) {
                this.PushError("_data_write", "socket_accept", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_temp_sock)));
                this._data_close();

                return false;
            }
        }

        if (is_resource(fp)) {
            while (!FileSystemOrSocket.feof(gVars.webEnv, fp)) {
                block = FileSystemOrSocket.fread(gVars.webEnv, fp, this._ftp_buff_size);

                if (!this._data_write_block(mode, block)) {
                    return false;
                }
            }
        } else if (!this._data_write_block(mode, strval(fp))) {
            return false;
        }

        return true;
    }

    public boolean _data_write_block(int mode, String block) {
        int t = 0;

        if (!equal(mode, FTP.FTP_BINARY)) {
            block = QRegExPerl.preg_replace("/\r\n|\r|\n/", this._eol_code.getValue(this.OS_remote), block);
        }

        do {
            if (strictEqual(t = Sockets.socket_write(gVars.webEnv, this._ftp_temp_sock, block), INT_FALSE)) {
                this.PushError("_data_write", "socket_write", Sockets.socket_strerror(gVars.webEnv, Sockets.socket_last_error(gVars.webEnv, this._ftp_temp_sock)));
                this._data_close();

                return false;
            }

            block = Strings.substr(block, t);
        } while (!empty(block));

        return true;
    }

    public boolean _data_close() {
        Sockets.socket_close(gVars.webEnv, this._ftp_temp_sock);
        Sockets.socket_close(gVars.webEnv, this._ftp_data_sock);
        this.SendMSG("Disconnected data from remote host");

        return true;
    }

    public void _quit() {
        if (this._connected) {
            Sockets.socket_close(gVars.webEnv, this._ftp_control_sock);
            this._connected = false;
            this.SendMSG("Socket closed");
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
