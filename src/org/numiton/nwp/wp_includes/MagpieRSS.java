/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MagpieRSS.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


public class MagpieRSS implements ContextCarrierInterface, Serializable, Cloneable {
    private static final long serialVersionUID = 3145198352238593998L;
    protected static final Logger LOG = Logger.getLogger(MagpieRSS.class.getName());
    public transient GlobalConsts gConsts;
    public transient GlobalVars gVars;
    public int parser;
    public Array<Object> current_item = new Array<Object>();	// item currently being parsed
    public Array<Object> items = new Array<Object>();	// collection of parsed items
    public Array<Object> channel = new Array<Object>();	// hash of channel fields
    public Array<Object> textinput = new Array<Object>();
    public Array<Object> image = new Array<Object>();
    public Object feed_type;
    public String feed_version;

	// parser variables
    public Array<String> stack = new Array<String>(); // parser stack
    public boolean inchannel = false;
    public boolean initem = false;
    public String incontent; // if in Atom <content mode="xml"> field
    public boolean intextinput = false;
    public boolean inimage = false;
    public String current_field = "";
    public String current_namespace = "";

	//var $ERROR = "";
    
    public Array<Object> _CONTENT_CONSTRUCTS = new Array<Object>(new ArrayEntry<Object>("content"), new ArrayEntry<Object>("summary"), new ArrayEntry<Object>("info"), new ArrayEntry<Object>("title"),
            new ArrayEntry<Object>("tagline"), new ArrayEntry<Object>("copyright"));

    /**
     * Generated in place of local variable 'el' from method
     * 'feed_start_element' because it is used inside an inner class.
     */
    String feed_start_element_el = null;

    /**
     * Generated in place of local variable 'ns' from method
     * 'feed_start_element' because it is used inside an inner class.
     */
    String feed_start_element_ns = null;
    
    public int from_cache;
    public String etag;
    public String last_modified;
    public Object ERROR;

    public MagpieRSS(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String source) {
        setContext(javaGlobalVariables, javaGlobalConstants);

        int parser = 0;
        int status = 0;
        int errorcode = 0;
        String xml_error = null;
        int error_line = 0;
        int error_col = 0;
        String errormsg = null;

        // if PHP xml isn't compiled in, die
        //
        if (!true)/*Modified by Numiton*/
         {
            ErrorHandling.trigger_error(gVars.webEnv, "Failed to load PHP\'s XML Extension. http://www.php.net/manual/en/ref.xml.php");
        }

        parser = XMLParser.xml_parser_create(gVars.webEnv);

        if (!is_resource(parser)) {
            ErrorHandling.trigger_error(gVars.webEnv, "Failed to create an instance of PHP\'s XML parser. http://www.php.net/manual/en/ref.xml.php");
        }

        this.parser = parser;
        
        // pass in parser, and a reference to this object
		// setup handlers
		//
        XMLParser.xml_set_object(gVars.webEnv, this.parser, this);
        XMLParser.xml_set_element_handler(gVars.webEnv, this.parser, new Callback("feed_start_element", this), new Callback("feed_end_element", this));
        
        XMLParser.xml_set_character_data_handler(gVars.webEnv, this.parser, new Callback("feed_cdata", this));
        
        status = XMLParser.xml_parse(gVars.webEnv, this.parser, source);

        if (!booleanval(status)) {
            errorcode = XMLParser.xml_get_error_code(gVars.webEnv, this.parser);

            if (!equal(errorcode, XMLParser.XML_ERROR_NONE)) {
                xml_error = XMLParser.xml_error_string(errorcode);
                error_line = XMLParser.xml_get_current_line_number(gVars.webEnv, this.parser);
                error_col = XMLParser.xml_get_current_column_number(gVars.webEnv, this.parser);
                errormsg = xml_error + " at line " + strval(error_line) + ", column " + strval(error_col);
                this.error(errormsg);
            }
        }

        XMLParser.xml_parser_free(gVars.webEnv, this.parser);
        this.normalize();
    }

    public void feed_start_element(int p, String element, Array<Object> attrs) {
        String attrs_str = null;
        String link_el = null;
        
        feed_start_element_el = element = Strings.strtolower(element);
        attrs = Array.array_change_key_case(attrs, Array.CASE_LOWER);
        
		// check for a namespace, and split if found
        feed_start_element_ns = "";

        if (BOOLEAN_FALSE != Strings.strpos(element, ":")) {
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        feed_start_element_ns = srcArray.getValue(0);
                        feed_start_element_el = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(QRegExPosix.split(":", element, 2));
        }

        if (booleanval(feed_start_element_ns) && !equal(feed_start_element_ns, "rdf")) {
            this.current_namespace = feed_start_element_ns;
        }

        // if feed type isn't set, then this is first element of feed
		// identify feed from root element
		//
        if (!isset(this.feed_type)) {
            if (equal(feed_start_element_el, "rdf")) {
                this.feed_type = gConsts.getRSS();
                this.feed_version = "1.0";
            } else if (equal(feed_start_element_el, "rss")) {
                this.feed_type = gConsts.getRSS();
                this.feed_version = strval(attrs.getValue("version"));
            } else if (equal(feed_start_element_el, "feed")) {
                this.feed_type = gConsts.getATOM();
                this.feed_version = strval(attrs.getValue("version"));
                this.inchannel = true;
            }

            return;
        }

        if (equal(feed_start_element_el, "channel")) {
            this.inchannel = true;
        } else if (equal(feed_start_element_el, "item") || equal(feed_start_element_el, "entry")) {
            this.initem = true;

            if (isset(attrs.getValue("rdf:about"))) {
                this.current_item.putValue("about", attrs.getValue("rdf:about"));
            }
        } 
		// if we're in the default namespace of an RSS feed,
		//  record textinput or image fields
        else if (equal(this.feed_type, gConsts.getRSS()) && equal(this.current_namespace, "") && equal(feed_start_element_el, "textinput")) {
            this.intextinput = true;
        } else if (equal(this.feed_type, gConsts.getRSS()) && equal(this.current_namespace, "") && equal(feed_start_element_el, "image")) {
            this.inimage = true;
        } 
        // handle atom content constructs
        else if (equal(this.feed_type, gConsts.getATOM()) && Array.in_array(feed_start_element_el, this._CONTENT_CONSTRUCTS)) {
        	// avoid clashing w/ RSS mod_content
            if (equal(feed_start_element_el, "content")) {
                feed_start_element_el = "atom_content";
            }

            this.incontent = feed_start_element_el;
        }
		// if inside an Atom content construct (e.g. content or summary) field treat tags as text
        else if (equal(this.feed_type, gConsts.getATOM()) && booleanval(this.incontent)) {
			// if tags are inlined, then flatten
            attrs_str = Strings.join(" ", Array.array_map(new Callback("map_attrs", this), Array.array_keys(attrs), Array.array_values(attrs)));
            
            this.append_content("<" + element + " " + attrs_str + ">");
            
            Array.array_unshift(this.stack, feed_start_element_el);
        } 
		// Atom support many links per containging element.
		// Magpie treats link elements of type rel='alternate'
		// as being equivalent to RSS's simple link element.
		//
        else if (equal(this.feed_type, gConsts.getATOM()) && equal(feed_start_element_el, "link")) {
            if (isset(attrs.getValue("rel")) && equal(attrs.getValue("rel"), "alternate")) {
                link_el = "link";
            } else {
                link_el = "link_" + strval(attrs.getValue("rel"));
            }

            this.append(link_el, strval(attrs.getValue("href")));
        } 
		// set stack[0] to current element
        else {
            Array.array_unshift(this.stack, feed_start_element_el);
        }
    }

    public void feed_cdata(int p, String text) {
        String current_el = null;

        if (equal(this.feed_type, gConsts.getATOM()) && booleanval(this.incontent)) {
            this.append_content(strval(text));
        } else {
            current_el = Strings.join("_", Array.array_reverse(this.stack));
            this.append(current_el, strval(text));
        }
    }

    public void feed_end_element(int p, String el) {
        el = Strings.strtolower(el);

        if (equal(el, "item") || equal(el, "entry")) {
            this.items.putValue(this.current_item);
            this.current_item = new Array<Object>();
            this.initem = false;
        } else if (equal(this.feed_type, gConsts.getRSS()) && equal(this.current_namespace, "") && equal(el, "textinput")) {
            this.intextinput = false;
        } else if (equal(this.feed_type, gConsts.getRSS()) && equal(this.current_namespace, "") && equal(el, "image")) {
            this.inimage = false;
        } else if (equal(this.feed_type, gConsts.getATOM()) && Array.in_array(el, this._CONTENT_CONSTRUCTS)) {
            this.incontent = strval(false);
        } else if (equal(el, "channel") || equal(el, "feed")) {
            this.inchannel = false;
        } else if (equal(this.feed_type, gConsts.getATOM()) && booleanval(this.incontent)) {
			// balance tags properly
			// note:  i don't think this is actually neccessary
            if (equal(this.stack.getValue(0), el)) {
                this.append_content("</" + el + ">");
            } else {
                this.append_content("<" + el + " />");
            }

            Array.array_shift(this.stack);
        } else {
            Array.array_shift(this.stack);
        }

        this.current_namespace = "";
    }

    public void concat(Ref str1, String str2) {
        if (!isset(str1.value)) {
            str1.value = "";
        }

        str1.value = strval(str1.value) + str2;
    }

    public void append_content(String text) {
        if (this.initem) {
            this.concat(this.current_item.getRef(this.incontent), text);
        } else if (this.inchannel) {
            this.concat(this.channel.getRef(this.incontent), text);
        }
    }

	// smart append - field and namespace aware
    public void append(String el, String text) {
        if (!booleanval(el)) {
            return;
        }

        if (booleanval(this.current_namespace)) {
            if (this.initem) {
                this.concat(this.current_item.getArrayValue(this.current_namespace).getRef(el), text);
            } else if (this.inchannel) {
                this.concat(this.channel.getArrayValue(this.current_namespace).getRef(el), text);
            } else if (this.intextinput) {
                this.concat(this.textinput.getArrayValue(this.current_namespace).getRef(el), text);
            } else if (this.inimage) {
                this.concat(this.image.getArrayValue(this.current_namespace).getRef(el), text);
            }
        } else {
            if (this.initem) {
                this.concat(this.current_item.getRef(el), text);
            } else if (this.intextinput) {
                this.concat(this.textinput.getRef(el), text);
            } else if (this.inimage) {
                this.concat(this.image.getRef(el), text);
            } else if (this.inchannel) {
                this.concat(this.channel.getRef(el), text);
            }
        }
    }

    public void normalize() {
        Array<Object> item = new Array<Object>();
        int i = 0;

		// if atom populate rss fields
        if (booleanval(this.is_atom())) {
            this.channel.putValue("descripton", this.channel.getValue("tagline"));

            for (i = 0; i < Array.count(this.items); i++) {
                item = this.items.getArrayValue(i);

                if (isset(item.getValue("summary"))) {
                    item.putValue("description", item.getValue("summary"));
                }

                if (isset(item.getValue("atom_content"))) {
                    item.getArrayValue("content").putValue("encoded", item.getValue("atom_content"));
                }

                this.items.putValue(i, item);
            }
        } else if (booleanval(this.is_rss())) {
            this.channel.putValue("tagline", this.channel.getValue("description"));

            for (i = 0; i < Array.count(this.items); i++) {
                item = this.items.getArrayValue(i);

                if (isset(item.getValue("description"))) {
                    item.putValue("summary", item.getValue("description"));
                }

                if (isset(item.getArrayValue("content").getValue("encoded"))) {
                    item.putValue("atom_content", item.getArrayValue("content").getValue("encoded"));
                }

                this.items.putValue(i, item);
            }
        }
    }

    public String is_rss() {
        if (equal(this.feed_type, gConsts.getRSS())) {
            return this.feed_version;
        } else {
            return strval(false);
        }
    }

    public String is_atom() {
        if (equal(this.feed_type, gConsts.getATOM())) {
            return this.feed_version;
        } else {
            return strval(false);
        }
    }

    public String map_attrs(Object k, Object v) {
        return strval(k) + "=\"" + strval(v) + "\"";
    }

    public void error(String errormsg) {
        error(errormsg, ErrorHandling.E_USER_WARNING);
    }

    public void error(String errormsg, int lvl) {
		// append PHP's error message if track_errors enabled
        if (isset(ErrorHandling.getLastErrorMessage(gVars.webEnv))) {
            errormsg = errormsg + " (" + ErrorHandling.getLastErrorMessage(gVars.webEnv) + ")";
        }

        if (booleanval(gConsts.getMAGPIE_DEBUG())) {
            ErrorHandling.trigger_error(gVars.webEnv, errormsg, lvl);
        } else {
            ErrorHandling.error_log(gVars.webEnv, errormsg, 0);
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
