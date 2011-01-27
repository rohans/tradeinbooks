/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: PasswordHash.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.ntile.til.libraries.php.quercus.QMisc;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;

/**
 * Portable PHP password hashing framework.
 * @package phpass
 * @since 2.5
 * @version 0.1
 * @link http://www.openwall.com/phpass/
 */

//
// Portable PHP password hashing framework.
//
// Version 0.1 / genuine.
//
// Written by Solar Designer <solar at openwall.com> in 2004-2006 and placed in
// the public domain.
//
// There's absolutely no warranty.
//
// The homepage URL for this framework is:
//
//	http://www.openwall.com/phpass/
//
// Please be sure to update the Version line if you edit this file in any way.
// It is suggested that you leave the main version number intact, but indicate
// your project name (after the slash) and add your own revision information.
//
// Please do not change the "private" password hashing method implemented in
// here, thereby making your hashes incompatible.  However, if you must, please
// change the hash type identifier (the "$P$") to something different.
//
// Obviously, since this code is in the public domain, the above are not
// requirements (there can be none), but merely suggestions.
//
public class PasswordHash implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	= Logger.getLogger(PasswordHash.class.getName());
	public GlobalConsts	          gConsts;
	public GlobalVars	          gVars;
	public String	              itoa64;
	public int	                  iteration_count_log2;
	public boolean	              portable_hashes;
	public String	              random_state;

	public PasswordHash(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, int iteration_count_log2, boolean portable_hashes) {
		setContext(javaGlobalVariables, javaGlobalConstants);
		this.itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		if (iteration_count_log2 < 4 || iteration_count_log2 > 31) {
			iteration_count_log2 = 8;
		}
		this.iteration_count_log2 = iteration_count_log2;
		this.portable_hashes = portable_hashes;
		this.random_state = strval(DateTime.microtime()) + (false ? /*Modified by Numiton*/strval(Unsupported.getmypid()) : "") + Misc.uniqid(strval(Math.rand()), true);
	}

	public String get_random_bytes(int count) {
		String output = null;
		int fh = 0;
		int i = 0;
		output = "";
		if (booleanval(fh = FileSystemOrSocket.fopen(gVars.webEnv, "/dev/urandom", "rb"))) {
			output = FileSystemOrSocket.fread(gVars.webEnv, fh, count);
			FileSystemOrSocket.fclose(gVars.webEnv, fh);
		}
		if (Strings.strlen(output) < count) {
			output = "";
			for (i = 0; i < count; i = i + 16) {
				this.random_state = Strings.md5(strval(DateTime.microtime()) + this.random_state);
				output = output + QMisc.pack("H*", Strings.md5(this.random_state));
			}
			output = Strings.substr(output, 0, count);
		}
		return output;
	}

	public String encode64(String input, int count) {
		String output = null;
		int i = 0;
		int value = 0;
		output = "";
		i = 0;
		do {
			value = Strings.ord(Strings.getCharAt(input, i++));
			output = output + Strings.getCharAt(this.itoa64, value & 63);
			if (i < count) {
				value = value | Strings.ord(Strings.getCharAt(input, i)) << 8;
			}
			output = output + Strings.getCharAt(this.itoa64, value >> 6 & 63);
			if (i++ >= count) {
				break;
			}
			if (i < count) {
				value = value | Strings.ord(Strings.getCharAt(input, i)) << 16;
			}
			output = output + Strings.getCharAt(this.itoa64, value >> 12 & 63);
			if (i++ >= count) {
				break;
			}
			output = output + Strings.getCharAt(this.itoa64, value >> 18 & 63);
		}
		while (i < count);
		return output;
	}

	public String gensalt_private(String input) {
		String output = null;
		output = "$P$";
		output = output + Strings.getCharAt(this.itoa64, intval(Math.min(this.iteration_count_log2 + ((intval("PHP_VERSION") >= intval("5")) ? 5 : 3), 30)));
		output = output + this.encode64(input, 6);
		return output;
	}

	public String crypt_private(String password, String setting) {
		String output = null;
		int count_log2 = 0;
		int count = 0;
		String salt = null;
		String hash = null;
		output = "*0";
		if (equal(Strings.substr(setting, 0, 2), output)) {
			output = "*1";
		}
		if (!equal(Strings.substr(setting, 0, 3), "$P$")) {
			return output;
		}
		count_log2 = Strings.strpos(this.itoa64, Strings.getCharAt(setting, 3));
		if (count_log2 < 7 || count_log2 > 30) {
			return output;
		}
		count = 1 << count_log2;
		salt = Strings.substr(setting, 4, 8);
		if (!equal(Strings.strlen(salt), 8)) {
			return output;
		}
		
		// We're kind of forced to use MD5 here since it's the only
		// cryptographic primitive available in all versions of PHP
		// currently in use.  To implement our own low-level crypto
		// in PHP would result in much worse performance and
		// consequently in lower iteration counts and hashes that are
		// quicker to crack (by non-PHP code).
		if (intval("PHP_VERSION") >= intval("5")) {
			hash = Strings.md5(salt + password, true);
			do {
				hash = Strings.md5(hash + password, true);
			}
			while (booleanval(--count));
		}
		else {
			hash = QMisc.pack("H*", Strings.md5(salt + password));
			do {
				hash = QMisc.pack("H*", Strings.md5(hash + password));
			}
			while (booleanval(--count));
		}
		output = Strings.substr(setting, 0, 12);
		output = output + this.encode64(hash, 16);
		return output;
	}

	public String gensalt_extended(Object input) {
		Object count_log2 = null;
		int count = 0;
		String output = null;
		
		count_log2 = Math.min(this.iteration_count_log2 + 8, 24);
		// This should be odd to not reveal weak DES keys, and the
		// maximum valid value is (2**24 - 1) which is odd anyway.
		count = (1 << intval(count_log2)) - 1;
		
		output = "_";
		output = output + Strings.getCharAt(this.itoa64, count & 63);
		output = output + Strings.getCharAt(this.itoa64, count >> 6 & 63);
		output = output + Strings.getCharAt(this.itoa64, count >> 12 & 63);
		output = output + Strings.getCharAt(this.itoa64, count >> 18 & 63);
		
		output = output + this.encode64(strval(input), 3);
		
		return output;
	}

	public String gensalt_blowfish(String input) {
		String itoa64 = null;
		String output = null;
		int i = 0;
		int c1 = 0;
		int c2 = 0;
		
		// This one needs to use a different order of characters and a
		// different encoding scheme from the one in encode64() above.
		// We care because the last character in our encoded string will
		// only represent 2 bits.  While two known implementations of
		// bcrypt will happily accept and correct a salt string which
		// has the 4 unused bits set to non-zero, we do not want to take
		// chances and we also do not want to waste an additional byte
		// of entropy.
		itoa64 = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		output = "$2a$";
		output = output + Strings.chr(intval(floatval(Strings.ord("0")) + floatval(this.iteration_count_log2) / floatval(10)));
		output = output + Strings.chr(Strings.ord("0") + this.iteration_count_log2 % 10);
		output = output + "$";
		i = 0;
		do {
			c1 = Strings.ord(Strings.getCharAt(input, i++));
			output = output + Strings.getCharAt(itoa64, c1 >> 2);
			c1 = (c1 & 3) << 4;
			if (i >= 16) {
				output = output + Strings.getCharAt(itoa64, c1);
				break;
			}
			c2 = Strings.ord(Strings.getCharAt(input, i++));
			c1 = c1 | c2 >> 4;
			output = output + Strings.getCharAt(itoa64, c1);
			c1 = (c2 & 15) << 2;
			c2 = Strings.ord(Strings.getCharAt(input, i++));
			c1 = c1 | c2 >> 6;
			output = output + Strings.getCharAt(itoa64, c1);
			output = output + Strings.getCharAt(itoa64, c2 & 63);
		}
		while (booleanval(1));
		return output;
	}

	public String HashPassword(String password) {
		String random = null;
		String hash = null;
		random = "";
		if (equal(Strings.CRYPT_BLOWFISH, 1) && !this.portable_hashes) {
			random = this.get_random_bytes(16);
			hash = QStrings.crypt(password, this.gensalt_blowfish(random));
			if (equal(Strings.strlen(hash), 60)) {
				return hash;
			}
		}
		if (equal(Strings.CRYPT_EXT_DES, 1) && !this.portable_hashes) {
			if (Strings.strlen(random) < 3) {
				random = this.get_random_bytes(3);
			}
			hash = QStrings.crypt(password, this.gensalt_extended(random));
			if (equal(Strings.strlen(hash), 20)) {
				return hash;
			}
		}
		if (Strings.strlen(random) < 6) {
			random = this.get_random_bytes(6);
		}
		hash = this.crypt_private(password, this.gensalt_private(random));
		if (equal(Strings.strlen(hash), 34)) {
			return hash;
		}
		
		// Returning '*' on error is safe here, but would _not_ be safe
		// in a crypt(3)-like function used _both_ for generating new
		// hashes and for validating passwords against existing hashes.
		return "*";
	}

	public boolean CheckPassword(String password, String stored_hash) {
		String hash = null;
		hash = this.crypt_private(password, stored_hash);
		if (equal(Strings.getCharAt(hash, 0), "*")) {
			hash = QStrings.crypt(password, stored_hash);
		}
		return equal(hash, stored_hash);
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
