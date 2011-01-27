/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: AtomParser.java,v 1.5 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin._import;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


public class AtomParser implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(AtomParser.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> ATOM_CONTENT_ELEMENTS = new Array<Object>(new ArrayEntry<Object>("content"), new ArrayEntry<Object>("summary"), new ArrayEntry<Object>("title"),
            new ArrayEntry<Object>("subtitle"), new ArrayEntry<Object>("rights"));
    public Array<Object> ATOM_SIMPLE_ELEMENTS = new Array<Object>(new ArrayEntry<Object>("id"), new ArrayEntry<Object>("updated"), new ArrayEntry<Object>("published"),
            new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("author"));
    public int depth = 0;
    public int indent = 2;
    public Array in_content = new Array();
    public Array<Object> ns_contexts = new Array<Object>();
    public Array<Object> ns_decls = new Array<Object>();
    public boolean is_xhtml = false;
    public boolean skipped_div = false;
    public AtomEntry entry;
    public Object feed;

    public AtomParser(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.entry = new AtomEntry(gVars, gConsts);
    }

    public String createFunction_map_attrs_func(Object k, Object v) {
        return k + "=\"" + strval(v) + "\"";
    }

    public String createFunction_map_xmlns_func(Object p, Array<String> n) {
        String xd = "xmlns";

        if (Strings.strlen(n.getValue(0)) > 0) {
            xd += (":" + n.getValue(0));
        }

        return xd + "=\"" + n.getValue(1) + "\"";
    }

    public boolean parse(String xml) {
        int parser = 0;
        String contents = null;
        Array.array_unshift(this.ns_contexts, new Array<Object>());
        parser = XMLParser.xml_parser_create_ns(gVars.webEnv);
        XMLParser.xml_set_object(gVars.webEnv, parser, this);
        XMLParser.xml_set_element_handler(gVars.webEnv, parser, new Callback("start_element", this), new Callback("end_element", this));
        XMLParser.xml_parser_set_option(gVars.webEnv, parser, XMLParser.XML_OPTION_CASE_FOLDING, 0);
        XMLParser.xml_parser_set_option(gVars.webEnv, parser, XMLParser.XML_OPTION_SKIP_WHITE, 0);
        XMLParser.xml_set_character_data_handler(gVars.webEnv, parser, new Callback("cdata", this));
        XMLParser.xml_set_default_handler(gVars.webEnv, parser, new Callback("_default", this));
        XMLParser.xml_set_start_namespace_decl_handler(gVars.webEnv, parser, new Callback("start_ns", this));
        XMLParser.xml_set_end_namespace_decl_handler(gVars.webEnv, parser, new Callback("end_ns", this));
        contents = "";
        XMLParser.xml_parse(gVars.webEnv, parser, xml);
        XMLParser.xml_parser_free(gVars.webEnv, parser);

        return true;
    }

    public void start_element(Object parser, String name, Array<Object> attrs) {
        Object tag = null;
        Array<Object> attrs_prefix = new Array<Object>();
        String key = null;
        Object value = null;
        String attrs_str = null;
        String xmlns_str = null;
        tag = Array.array_pop(QRegExPosix.split(":", name));
        Array.array_unshift(this.ns_contexts, this.ns_decls);
        this.depth++;

        if (!empty(this.in_content)) {
            attrs_prefix = new Array<Object>();

			// resolve prefixes for attributes
            for (Map.Entry javaEntry44 : attrs.entrySet()) {
                key = strval(javaEntry44.getKey());
                value = javaEntry44.getValue();
                attrs_prefix.putValue(this.ns_to_prefix(key), this.xml_escape(strval(value)));
            }

            attrs_str = Strings.join(" ", Array.array_map(new Callback("createFunction_map_attrs_func", this), Array.array_keys(attrs_prefix), Array.array_values(attrs_prefix)));

            if (Strings.strlen(attrs_str) > 0) {
                attrs_str = " " + attrs_str;
            }

            xmlns_str = Strings.join(
                    " ",
                    Array.array_map(new Callback("createFunction_map_xmlns_func", this), Array.array_keys(this.ns_contexts.getArrayValue(0)), Array.array_values(this.ns_contexts.getArrayValue(0))));

            if (Strings.strlen(xmlns_str) > 0) {
                xmlns_str = " " + xmlns_str;
            }

			// handle self-closing tags (case: a new child found right-away, no text node)
            if (equal(Array.count(this.in_content), 2)) {
                Array.array_push(this.in_content, ">");
            }

            Array.array_push(this.in_content, "<" + this.ns_to_prefix(name) + xmlns_str + attrs_str);
        } else if (Array.in_array(tag, this.ATOM_CONTENT_ELEMENTS) || Array.in_array(tag, this.ATOM_SIMPLE_ELEMENTS)) {
            this.in_content = new Array<Object>();
            this.is_xhtml = equal(attrs.getValue("type"), "xhtml");
            Array.array_push(this.in_content, new Array<Object>(new ArrayEntry<Object>(tag), new ArrayEntry<Object>(this.depth)));
        } else if (equal(tag, "link")) {
            Array.array_push(this.entry.links, attrs);
        } else if (equal(tag, "category")) {
            Array.array_push(this.entry.categories, attrs.getValue("term"));
        }

        this.ns_decls = new Array<Object>();
    }

    public void end_element(Object parser, String name) {
        String tag = null;
        String endtag = null;
        tag = Array.array_pop(QRegExPosix.split(":", name));

        if (!empty(this.in_content)) {
            if (equal(this.in_content.getArrayValue(0).getValue(0), tag) && equal(this.in_content.getArrayValue(0).getValue(1), this.depth)) {
                Array.array_shift(this.in_content);

                if (this.is_xhtml) {
                    this.in_content = Array.array_slice(this.in_content, 2, Array.count(this.in_content) - 3);
                }

                /* Modified by Numiton */
                String value = Strings.join("", this.in_content);

                if (equal(tag, "draft")) {
                    this.entry.draft = value;
                } else if (equal(tag, "old_permalink")) {
                    this.entry.old_permalink = value;
                } else if (equal(tag, "published")) {
                    this.entry.published = value;
                } else if (equal(tag, "content")) {
                    this.entry.content = value;
                } else if (equal(tag, "title")) {
                    this.entry.title = value;
                } else if (equal(tag, "author")) {
                    this.entry.author = value;
                } else if (equal(tag, "old_post_permalink")) {
                    this.entry.old_post_permalink = value;
                } else if (equal(tag, "updated")) {
                    this.entry.updated = value;
                } else {
                    LOG.warn("Unknown field name: " + tag);
                }

                this.in_content = new Array<Object>();
            } else {
                endtag = this.ns_to_prefix(name);

                if (!strictEqual(Strings.strpos(strval(this.in_content.getValue(Array.count(this.in_content) - 1)), "<" + endtag), BOOLEAN_FALSE)) {
                    Array.array_push(this.in_content, "/>");
                } else {
                    Array.array_push(this.in_content, "</" + endtag + ">");
                }
            }
        }

        Array.array_shift(this.ns_contexts);
        
		// print str_repeat(" ", $this->depth * $this->indent) . "end_element('$name')" ."\n";
        
        this.depth--;
    }

    public void start_ns(Object parser, String prefix, String uri) {
    	//print str_repeat(" ", $this->depth * $this->indent) . "starting: " . $prefix . ":" . $uri . "\n";
        Array.array_push(this.ns_decls, new Array<Object>(new ArrayEntry<Object>(prefix), new ArrayEntry<Object>(uri)));
    }

    public void end_ns(Object parser, String prefix) {
    	//print str_repeat(" ", $this->depth * $this->indent) . "ending: #" . $prefix . "#\n";
    }

    public void cdata(Object parser, String data) {
    	//print str_repeat(" ", $this->depth * $this->indent) . "data: #" . $data . "#\n";
        if (!empty(this.in_content)) {
			// handle self-closing tags (case: text node found, need to close element started)
            if (!strictEqual(Strings.strpos(strval(this.in_content.getValue(Array.count(this.in_content) - 1)), "<"), BOOLEAN_FALSE)) {
                Array.array_push(this.in_content, ">");
            }

            Array.array_push(this.in_content, this.xml_escape(data));
        }
    }

    public void _default(Object parser, Object data) {
    	// when does this gets called?
    }

    public String ns_to_prefix(String qname) {
        Array<String> components = new Array<String>();
        String name = null;
        String ns = null;
        Array<Object> mapping = new Array<Object>();
        Array<Object> context = null;
        components = QRegExPosix.split(":", qname);
        name = Array.array_pop(components);

        if (!empty(components)) {
            ns = Strings.join(":", components);

            for (Map.Entry javaEntry45 : this.ns_contexts.entrySet()) {
                context = (Array<Object>) javaEntry45.getValue();

                for (Map.Entry javaEntry46 : context.entrySet()) {
                    mapping = (Array<Object>) javaEntry46.getValue();

                    if (equal(mapping.getValue(1), ns) && (Strings.strlen(strval(mapping.getValue(0))) > 0)) {
                        return strval(mapping.getValue(0)) + ":" + name;
                    }
                }
            }
        }

        return name;
    }

    public String xml_escape(String string) {
        return Strings.str_replace(
            new Array<String>(new ArrayEntry<String>("&"), new ArrayEntry<String>("\""), new ArrayEntry<String>("\'"), new ArrayEntry<String>("<"), new ArrayEntry<String>(">")),
            new Array<String>(new ArrayEntry<String>("&amp;"), new ArrayEntry<String>("&quot;"), new ArrayEntry<String>("&apos;"), new ArrayEntry<String>("&lt;"), new ArrayEntry<String>("&gt;")),
            string);
    }

    public Array<String> xml_escape(Array string) {
        return Strings.str_replace(
            new Array<String>(new ArrayEntry<String>("&"), new ArrayEntry<String>("\""), new ArrayEntry<String>("\'"), new ArrayEntry<String>("<"), new ArrayEntry<String>(">")),
            new Array<String>(new ArrayEntry<String>("&amp;"), new ArrayEntry<String>("&quot;"), new ArrayEntry<String>("&apos;"), new ArrayEntry<String>("&lt;"), new ArrayEntry<String>("&gt;")),
            string);
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
