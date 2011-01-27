/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: gettext_reader.java,v 1.2 2008/10/03 18:45:30 numiton Exp $
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

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.FunctionHandling;
import com.numiton.Math;
import com.numiton.RegExPosix;
import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QMisc;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QVarHandling;
import com.numiton.string.Strings;

/**
 * PHP-Gettext External Library: gettext_reader class
 *
 * @package External
 * @subpackage PHP-gettext
 *
 * @internal
	 Copyright (c) 2003 Danilo Segan <danilo@kvota.net>.
	 Copyright (c) 2005 Nico Kaiser <nico@siriux.net>

	 This file is part of PHP-gettext.

	 PHP-gettext is free software; you can redistribute it and/or modify
	 it under the terms of the GNU General Public License as published by
	 the Free Software Foundation; either version 2 of the License, or
	 (at your option) any later version.

	 PHP-gettext is distributed in the hope that it will be useful,
	 but WITHOUT ANY WARRANTY; without even the implied warranty of
	 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 GNU General Public License for more details.

	 You should have received a copy of the GNU General Public License
	 along with PHP-gettext; if not, write to the Free Software
	 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/

/**
 * Provides a simple gettext replacement that works independently from
 * the system's gettext abilities.
 * It can read MO files and use them for translating strings.
 * The files are passed to gettext_reader as a Stream (see streams.php)
 *
 * This version has the ability to cache all strings and translations to
 * speed up the string lookup.
 * While the cache is enabled by default, it can be switched off with the
 * second parameter in the constructor (e.g. whenusing very large MO files
 * that you don't want to keep in memory)
 */
public class gettext_reader implements ContextCarrierInterface, Serializable, Cloneable {
	protected static final Logger	LOG	                 = Logger.getLogger(gettext_reader.class.getName());
	public GlobalConsts	          gConsts;
	public GlobalVars	          gVars;
	//public:
	public int	                  error	                 = 0; // public variable that holds error code (0 if no error)
	
	 //private:
	public int	                  BYTEORDER	             = 0;        // 0: low endian, 1: big endian
	public StreamReader	          STREAM	             = null;
	public boolean	              short_circuit	         = false;
	public boolean	              enable_cache	         = false;
	public int	                  originals	             = 0;      // offset of original table
	public int	                  translations	         = 0;    // offset of translation table
	public String	              pluralheader;   // cache header field for plural forms
	public String	              select_string_function	= null; // cache function, which chooses plural forms
	public int	                  total	                 = 0;          // total string count
	public Array<Integer>	      table_originals; /*Initialized in code*/ // table for original strings (offsets)
	public Array<Integer>	      table_translations; /* Initialized in code */  // table for translated strings (offsets)
	public Array<String>	      cache_translations; /* Initialized in code */  // original -> translation mapping
	
	/* Methods */


	/**
	 * Reads a 32bit Integer from the Stream
	 *
	 * @access private
	 * @return Integer from the Stream
	 */
	public int readint() {
		Ref<Array<Integer>> low_end = new Ref<Array<Integer>>();
		Ref<Array<Integer>> big_end = new Ref<Array<Integer>>();
		if (equal(this.BYTEORDER, 0)) {
			// low endian
			low_end.value = QMisc.unpack("V", this.STREAM.read(4));
			return Array.array_shift(low_end);
		}
		else {
			// big endian
			big_end.value = QMisc.unpack("N", this.STREAM.read(4));
			return Array.array_shift(big_end);
		}
	}

	/** 
	 * Reads an array of Integers from the Stream
	 * @param int count How many elements should be read
	 * @return Array of Integers
	 */
	public Array<Integer> readintarray(int count) {
		if (equal(this.BYTEORDER, 0)) {
			// low endian
			return QMisc.unpack("V" + strval(count), this.STREAM.read(4 * count));
		}
		else {
			// big endian
			return QMisc.unpack("N" + strval(count), this.STREAM.read(4 * count));
		}
	}

	public gettext_reader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, StreamReader Reader) {
		this(javaGlobalVariables, javaGlobalConstants, Reader, true);
	}

	/** 
	 * Constructor
	 * @param object Reader the StreamReader object
	 * @param boolean enable_cache Enable or disable caching of strings (default
	 * on)
	 */
	public gettext_reader(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, StreamReader Reader, boolean enable_cache) {
		setContext(javaGlobalVariables, javaGlobalConstants);
		int MAGIC1 = 0;
		int MAGIC2 = 0;
		int MAGIC3 = 0;
		Object magic = null;
		Object revision = null;
		
		// If there isn't a StreamReader, turn on short circuit mode.
		if (!booleanval(Reader))
		/*Commented by Numiton || isset(Reader.error)*/
		{
			this.short_circuit = true;
			return;
		}
		
		// Caching can be turned off
		this.enable_cache = enable_cache;
		
		// $MAGIC1 = (int)0x950412de; //bug in PHP 5.0.2, see https://savannah.nongnu.org/bugs/?func=detailitem&item_id=10565
		MAGIC1 = -1794895138;
		// $MAGIC2 = (int)0xde120495; //bug
		MAGIC2 = -569244523;
		// 64-bit fix
		MAGIC3 = intval(2500072158L);
		
		this.STREAM = Reader;
		magic = this.readint();
		if (equal(magic, MAGIC1) || equal(magic, MAGIC3)) { // to make sure it works for 64-bit platforms
			this.BYTEORDER = 0;
		}
		else
			if (equal(magic, MAGIC2 & 4294967295L)) {
				this.BYTEORDER = 1;
			}
			else {
				this.error = 1; // not MO file
				return;
			}
		
		// FIXME: Do we care about revision? We should.
		revision = this.readint();
		this.total = this.readint();
		this.originals = this.readint();
		this.translations = this.readint();
	}

	/** 
	 * Loads the translation tables from the MO file into the cache If caching
	 * is enabled, also loads all strings into a cache to speed up translation
	 * lookups
	 * @access private
	 */
	public void load_tables() {
		int i = 0;
		String original = null;
		String translation = null;
		
		if (is_array(this.cache_translations) && is_array(this.table_originals) && is_array(this.table_translations)) {
			return;
		}
		
		/* get original and translations tables */
		this.STREAM.seekto(this.originals);
		this.table_originals = this.readintarray(this.total * 2);
		this.STREAM.seekto(this.translations);
		this.table_translations = this.readintarray(this.total * 2);
		if (this.enable_cache) {
			this.cache_translations = new Array<String>();
			
			/* read all strings in the cache */
			for (i = 0; i < this.total; i++) {
				this.STREAM.seekto(this.table_originals.getValue(i * 2 + 2));
				original = this.STREAM.read(this.table_originals.getValue(i * 2 + 1));
				this.STREAM.seekto(this.table_translations.getValue(i * 2 + 2));
				translation = this.STREAM.read(this.table_translations.getValue(i * 2 + 1));
				this.cache_translations.putValue(original, translation);
			}
		}
	}

	/** 
	 * Returns a string from the "originals" table
	 * @access private
	 * @param int num Offset number of original string
	 * @return string Requested string if found, otherwise ''
	 */
	public String get_original_string(int num) {
		int length = 0;
		int offset = 0;
		String data = null;
		length = this.table_originals.getValue(num * 2 + 1);
		offset = this.table_originals.getValue(num * 2 + 2);
		if (!booleanval(length)) {
			return "";
		}
		this.STREAM.seekto(offset);
		data = this.STREAM.read(length);
		return data;
	}

	/** 
	 * Returns a string from the "translations" table
	 * @access private
	 * @param int num Offset number of original string
	 * @return string Requested string if found, otherwise ''
	 */
	public String get_translation_string(int num) {
		int length = 0;
		int offset = 0;
		String data = null;
		length = this.table_translations.getValue(num * 2 + 1);
		offset = this.table_translations.getValue(num * 2 + 2);
		if (!booleanval(length)) {
			return "";
		}
		this.STREAM.seekto(offset);
		data = this.STREAM.read(length);
		return data;
	}

	public int find_string(String string) {
		return find_string(string, -1, -1);
	}

	/** 
	 * Binary search for string
	 * @access private
	 * @param string string
	 * @param int start (internally used in recursive function)
	 * @param int end (internally used in recursive function)
	 * @return int string number (offset in originals table)
	 */
	public int find_string(String string, int start, int end) {
		String txt;
		int half;
		int cmp = 0;
		if (equal(start, -1) || equal(end, -1)) {
			// find_string is called with only one parameter, set start end end
			start = 0;
			end = this.total;
		}
		if (Math.abs(start - end) <= 1) {
			// We're done, now we either found the string, or it doesn't exist
			txt = this.get_original_string(start);
			if (equal(string, txt)) {
				return start;
			}
			else
				return -1;
		}
		else
			if (start > end) {
				// start > end -> turn around and start over
				return this.find_string(string, end, start);
			}
			else {
				// Divide table in two parts
				half = intval(floatval(start + end) / floatval(2));
				cmp = Strings.strcmp(string, this.get_original_string(half));

				if (equal(cmp, 0)) {
					// string is exactly in the middle => return it
					return half;
				}
				else
					if (cmp < 0) {
						// The string is in the upper half
						return this.find_string(string, start, half);
					}
					else {
						// The string is in the lower half
						return this.find_string(string, half, end);
					}
			}
	}

	/** 
	 * Translates a string
	 * @access public
	 * @param string string to be translated
	 * @return string translated string (or original, if not found)
	 */
	public String translate(String string) {
		int num = 0;
		if (this.short_circuit) {
			return string;
		}
		this.load_tables();
		if (this.enable_cache) {
			// Caching enabled, get translated string from cache
			if (Array.array_key_exists(string, this.cache_translations)) {
				return this.cache_translations.getValue(string);
			}
			else
				return string;
		}
		else {
			// Caching not enabled, try to find string
			num = this.find_string(string);
			if (equal(num, -1)) {
				return string;
			}
			else
				return this.get_translation_string(num);
		}
	}

	/** 
	 * Get possible plural forms from MO header
	 * @access private
	 * @return string plural form header
	 */
	public String get_plural_forms() {
		String header = null;
		Array<Object> regs = new Array<Object>();
		String expr = null;
		String res = null;
		int p = 0;
		Object ch = null;
		int i = 0;
		
		// lets assume message number 0 is header
		// this is true, right?
		this.load_tables();
		
		// cache header field for plural forms
		if (!is_string(this.pluralheader)) {
			if (this.enable_cache) {
				header = this.cache_translations.getValue("");
			}
			else {
				header = this.get_translation_string(0);
			}
			header = header + "\n"; //make sure our regex matches
			if (booleanval(RegExPosix.eregi("plural-forms: ([^\n]*)\n", header, regs))) {
				expr = strval(regs.getValue(1));
			}
			else
				expr = "nplurals=2; plural=n == 1 ? 0 : 1;";
			
			// add parentheses
 			// important since PHP's ternary evaluates from left to right
			expr = expr + ";";
			res = "";
			p = 0;
			for (i = 0; i < Strings.strlen(expr); i++) {
				ch = Strings.getCharAt(expr, i);
				{
					int javaSwitchSelector69 = 0;
					if (equal(ch, "?"))
						javaSwitchSelector69 = 1;
					if (equal(ch, ":"))
						javaSwitchSelector69 = 2;
					if (equal(ch, ";"))
						javaSwitchSelector69 = 3;
					switch (javaSwitchSelector69) {
						case 1: {
							res = res + " ? (";
							p++;
							break;
						}
						case 2: {
							res = res + ") : (";
							break;
						}
						case 3: {
							res = res + Strings.str_repeat(")", p) + ";";
							p = 0;
							break;
						}
						default: {
							res = res + strval(ch);
						}
					}
				}
			}
			this.pluralheader = res;
		}
		return this.pluralheader;
	}

	/** 
	 * Detects which plural form to take
	 * @access private
	 * @param n count
	 * @return int array index of the right plural form
	 */
	public int select_string(int n) {
		String string = null;
		Array<Object> matches = new Array<Object>();
		if (is_null(this.select_string_function)) {
			string = this.get_plural_forms();
			if (QRegExPerl.preg_match("/nplurals\\s*=\\s*(\\d+)\\s*\\;\\s*plural\\s*=\\s*(.*?)\\;+/", string, matches)) {
				nplurals = intval(matches.getValue(1));
				expression = strval(matches.getValue(2));
				expression = Strings.str_replace("n", "$n", expression);
			}
			else {
				nplurals = 2;
				expression = " $n == 1 ? 0 : 1 ";
			}
			this.select_string_function = "function created";
		}
		return intval(FunctionHandling.call_user_func(new Callback("createFunction_select_string_function", this), n));
	}
	protected int	 nplurals;
	protected String	expression;

	// TODO Eliminate error-prone dynamic code
	public Object createFunction_select_string_function(String n) {
		String evalStr = "$expression=\"" + expression + "\";\n" + "$n = " + n + ";\n" + "$plural = ($expression);\n" + "echo(($plural <= $nplurals)? $plural : $plural - 1;)";
		LOG.debug("Evaluated string: " + evalStr);
		try {
			return QVarHandling.eval(evalStr);
		}
		catch (IOException ex) {
			LOG.warn(ex, ex);
			return null;
		}
	}

	/** 
	 * Plural version of gettext
	 * @access public
	 * @param string single
	 * @param string plural
	 * @param string number
	 * @return translated plural form
	 */
	public Object ngettext(String single, String plural, int number) {
		int select = 0;
		String key = null;
		String result = null;
		Array<String> list = new Array<String>();
		int num = 0;
		if (this.short_circuit) {
			if (!equal(number, 1)) {
				return plural;
			}
			else
				return single;
		}
		
		// find out the appropriate form
		select = this.select_string(number);
		
		// this should contains all strings separated by NULLs
		key = single + Strings.chr(0) + plural;
		
		if (this.enable_cache) {
			if (!Array.array_key_exists(key, this.cache_translations)) {
				return !equal(number, 1) ? plural : single;
			}
			else {
				result = this.cache_translations.getValue(key);
				list = Strings.explode(Strings.chr(0), result);
				return list.getValue(select);
			}
		}
		else {
			num = this.find_string(key);
			if (equal(num, -1)) {
				return !equal(number, 1) ? plural : single;
			}
			else {
				result = this.get_translation_string(num);
				list = Strings.explode(Strings.chr(0), result);
				return list.getValue(select);
			}
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
