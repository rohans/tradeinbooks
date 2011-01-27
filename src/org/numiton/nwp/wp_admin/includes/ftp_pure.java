/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ftp_pure.java,v 1.3 2008/10/03 18:45:31 numiton Exp $
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
import com.numiton.file.FileSystemOrSocket;
import com.numiton.ftp.FTP;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.ExpressionHelper;
import com.numiton.generic.Ref;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
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
public class ftp_pure extends ftp_base implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(ftp_pure.class.getName());

    // Commented by Numiton
    //	public ftp_pure(GlobalVars javaGlobalVariables,
    //			GlobalConsts javaGlobalConstants, Object verb, Object le) {
    //		setContext(javaGlobalVariables, javaGlobalConstants);
    //		this.__construct(verb, le);
    //	}
    public ftp_pure(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, boolean verb, boolean le) {
        super(javaGlobalVariables, javaGlobalConstants, false, verb, le);
    }

    // <!-- --------------------------------------------------------------------------------------- -->
    // <!--       Private functions                                                                 -->
    // <!-- --------------------------------------------------------------------------------------- -->
    public boolean _settimeout(int sock) {
        if (!FileSystemOrSocket.socket_set_timeout(gVars.webEnv, sock, this._timeout)) {
            this.PushError("_settimeout", "socket set send timeout", "");
            this._quit(false);

            return false;
        }

        return true;
    }

    public int _connect(String host, int port) {
        int sock = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();
        this.SendMSG("Creating socket");
        sock = FileSystemOrSocket.fsockopen(gVars.webEnv, host, port, errno, errstr, this._timeout);

        if (!booleanval(sock)) {
            this.PushError("_connect", "socket connect failed", errstr + " (" + errno + ")");

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
            tmp = FileSystemOrSocket.fgets(gVars.webEnv, this._ftp_control_sock, 512);

            if (strictEqual(tmp, STRING_FALSE)) {
                go = result = false;
                this.PushError(fnction, "Read failed");
            } else {
                this._message = this._message + tmp;

                if (QRegExPerl.preg_match("/^([0-9]{3})(-(.*[" + gConsts.getCRLF() + "]{1,2})+\\1)? [^" + gConsts.getCRLF() + "]+[" + gConsts.getCRLF() + "]{1,2}$/", this._message, regs)) {
                    go = false;
                }
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

        status = FileSystemOrSocket.fputs(gVars.webEnv, this._ftp_control_sock, cmd + gConsts.getCRLF());

        if (strictEqual(status, INT_FALSE)) {
            this.PushError(fnction, "socket write failed");

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
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();

        if (!this._settype(mode)) {
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
            this._ftp_data_sock = FileSystemOrSocket.fsockopen(gVars.webEnv, this._datahost.value, this._dataport.value, errno, errstr, this._timeout);

            if (!booleanval(this._ftp_data_sock)) {
                this.PushError("_data_prepare", "fsockopen fails", errstr + " (" + errno + ")");
                this._data_close();

                return false;
            } else {
                ExpressionHelper.execExpr(this._ftp_data_sock);
            }
        } else {
            this.SendMSG("Only passive connections available!");

            return false;
        }

        return true;
    }

    public String _data_read(int mode, int fp) {
        String out = null;
        String block = null;

        if (is_resource(fp)) {
            out = strval(0);
        } else {
            out = "";
        }

        if (!this._passive) {
            this.SendMSG("Only passive connections available!");

            return null;
        }

        while (!FileSystemOrSocket.feof(gVars.webEnv, this._ftp_data_sock)) {
            block = FileSystemOrSocket.fread(gVars.webEnv, this._ftp_data_sock, this._ftp_buff_size);

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

        if (is_resource(fp)) {
            out = strval(0);
        } else {
            out = "";
        }

        if (!this._passive) {
            this.SendMSG("Only passive connections available!");

            return false;
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
            if (strictEqual(t = FileSystemOrSocket.fwrite(gVars.webEnv, this._ftp_data_sock, block), INT_FALSE)) {
                this.PushError("_data_write", "Can\'t write to socket");

                return false;
            }

            block = Strings.substr(block, t);
        } while (!empty(block));

        return true;
    }

    public boolean _data_close() {
        FileSystemOrSocket.fclose(gVars.webEnv, this._ftp_data_sock);
        this.SendMSG("Disconnected data from remote host");

        return true;
    }

    public void _quit() {
        _quit(false);
    }

    public void _quit(boolean force) {
        if (this._connected || force) {
            FileSystemOrSocket.fclose(gVars.webEnv, this._ftp_control_sock);
            this._connected = false;
            this.SendMSG("Socket closed");
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
