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
package org.numiton.nwp.wp_includes;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


/**
 * AtomLib Atom Parser API
 * @package AtomLib
 */
public class AtomParser implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(AtomParser.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String NS = "http://www.w3.org/2005/Atom";
    public Array<Object> ATOM_CONTENT_ELEMENTS = new Array<Object>(new ArrayEntry<Object>("content"), new ArrayEntry<Object>("summary"), new ArrayEntry<Object>("title"),
            new ArrayEntry<Object>("subtitle"), new ArrayEntry<Object>("rights"));
    public Array<Object> ATOM_SIMPLE_ELEMENTS = new Array<Object>(new ArrayEntry<Object>("id"), new ArrayEntry<Object>("updated"), new ArrayEntry<Object>("published"), new ArrayEntry<Object>("draft"));
    public boolean debug = false;
    public int depth = 0;
    public int indent = 2;
    public Array<Object> in_content = new Array<Object>();
    public Array<Object> ns_contexts = new Array<Object>();
    public Array<Object> ns_decls = new Array<Object>();
    public Array<Object> content_ns_decls = new Array<Object>();
    public Array<Object> content_ns_contexts = new Array<Object>();
    public boolean is_xhtml = false;
    public boolean is_html = false;
    public boolean is_text = true;
    public boolean skipped_div = false;
    public String FILE = "php://input";
    public AtomFeed feed;
    public AtomBase current;
    public Object error;
    public String content;

    public AtomParser(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.feed = new AtomFeed(gVars, gConsts);
        this.current = null;
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

    public void _p(String msg) {
        if (this.debug) {
            print(gVars.webEnv, Strings.str_repeat(" ", this.depth * this.indent) + msg + "\n");
        }
    }

    public void error_handler(Object log_level, Object log_text, Object error_file, Object error_line) {
        this.error = log_text;
    }

    public boolean parse() {
        int parser = 0;
        boolean ret = false;
        int fp = 0;
        String data = null;
        int xml_parser = 0;
        ErrorHandling.set_error_handler(gVars.webEnv, new Callback("error_handler", this));
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
        this.content = "";
        ret = true;
        fp = FileSystemOrSocket.fopen(gVars.webEnv, this.FILE, "r");

        while (booleanval(data = FileSystemOrSocket.fread(gVars.webEnv, fp, 4096))) {
            if (this.debug) {
                this.content = this.content + data;
            }

            if (!booleanval(XMLParser.xml_parse(gVars.webEnv, parser, data, FileSystemOrSocket.feof(gVars.webEnv, fp)))) {
                ErrorHandling.trigger_error(gVars.webEnv,
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("XML error: %s at line %d", "default") + "\n",
                        XMLParser.xml_error_string(XMLParser.xml_get_error_code(gVars.webEnv, xml_parser)), XMLParser.xml_get_current_line_number(gVars.webEnv, xml_parser)));
                ret = false;

                break;
            }
        }

        FileSystemOrSocket.fclose(gVars.webEnv, fp);
        XMLParser.xml_parser_free(gVars.webEnv, parser);
        ErrorHandling.restore_error_handler(gVars.webEnv);

        return ret;
    }

    public void start_element(Object parser, String name, Array<Object> attrs) {
        Object tag = null;
        Array<Object> attrs_prefix = new Array<Object>();
        Array<Object> with_prefix = new Array<Object>();
        String key = null;
        Object value = null;
        String attrs_str = null;
        String xmlns_str = null;
        String type = null;
        
        tag = Array.array_pop(QRegExPosix.split(":", name));

        {
            int javaSwitchSelector37 = 0;

            if (equal(name, this.NS + ":feed")) {
                javaSwitchSelector37 = 1;
            }

            if (equal(name, this.NS + ":entry")) {
                javaSwitchSelector37 = 2;
            }

            switch (javaSwitchSelector37) {
            case 1: {
                this.current = this.feed;

                break;
            }

            case 2: {
                this.current = new AtomEntry(gVars, gConsts);

                break;
            }
            }
        }

        this._p("start_element(\'" + name + "\')");
//        $this->_p(print_r($this->ns_contexts,true));
//        $this->_p('current(' . $this->current . ')');
        
        Array.array_unshift(this.ns_contexts, this.ns_decls);
        this.depth++;

        if (!empty(this.in_content)) {
            this.content_ns_decls = new Array<Object>();

            if (this.is_html || this.is_text) {
                ErrorHandling.trigger_error(gVars.webEnv, "Invalid content in element found. Content must not be of type text or html if it contains markup.");
            }

            attrs_prefix = new Array<Object>();

            // resolve prefixes for attributes
            for (Map.Entry javaEntry368 : attrs.entrySet()) {
                key = strval(javaEntry368.getKey());
                value = javaEntry368.getValue();
                with_prefix = this.ns_to_prefix(key, true);
                attrs_prefix.putValue(with_prefix.getValue(1), this.xml_escape(strval(value)));
            }

            attrs_str = Strings.join(" ", Array.array_map(new Callback("createFunction_map_attrs_func", this), Array.array_keys(attrs_prefix), Array.array_values(attrs_prefix)));

            if (Strings.strlen(attrs_str) > 0) {
                attrs_str = " " + attrs_str;
            }

            with_prefix = this.ns_to_prefix(name);

            if (!this.is_declared_content_ns(with_prefix.getValue(0))) {
                Array.array_push(this.content_ns_decls, with_prefix.getValue(0));
            }

            xmlns_str = "";

            if (Array.count(this.content_ns_decls) > 0) {
                Array.array_unshift(this.content_ns_contexts, this.content_ns_decls);
                xmlns_str = xmlns_str +
                    Strings.join(" ",
                        Array.array_map(
                            new Callback("createFunction_map_xmlns_func", this),
                            Array.array_keys(this.content_ns_contexts.getArrayValue(0)),
                            Array.array_values(this.content_ns_contexts.getArrayValue(0))));

                if (Strings.strlen(xmlns_str) > 0) {
                    xmlns_str = " " + xmlns_str;
                }
            }

            Array.array_push(
                this.in_content,
                new Array<Object>(new ArrayEntry<Object>(tag), new ArrayEntry<Object>(this.depth), new ArrayEntry<Object>("<" + strval(with_prefix.getValue(1)) + xmlns_str + attrs_str + ">")));
        } else if (Array.in_array(tag, this.ATOM_CONTENT_ELEMENTS) || Array.in_array(tag, this.ATOM_SIMPLE_ELEMENTS)) {
            this.in_content = new Array<Object>();
            this.is_xhtml = equal(attrs.getValue("type"), "xhtml");
            this.is_html = equal(attrs.getValue("type"), "html") || equal(attrs.getValue("type"), "text/html");
            this.is_text = !Array.in_array("type", Array.array_keys(attrs)) || equal(attrs.getValue("type"), "text");
            type = (this.is_xhtml
                ? "XHTML"
                : (this.is_html
                ? "HTML"
                : (this.is_text
                ? "TEXT"
                : strval(attrs.getValue("type")))));

            if (Array.in_array("src", Array.array_keys(attrs))) {
                // Modified by Numiton
                if (equal(tag, "content")) {
                    this.current.content = attrs;
                } else if (equal(tag, "summary")) {
                    this.current.summary = attrs;
                } else if (equal(tag, "title")) {
                    this.current.title = attrs;
                } else if (equal(tag, "subtitle")) {
                    this.current.subtitle = attrs;
                } else if (equal(tag, "rights")) {
                    this.current.rights = attrs;
                } else {
                    /* Other possible values, but of incompatible type:
                     - id
                     - updated
                     - published
                     - draft
                     */
                    LOG.warn("Invalid tag value: " + tag + ". Trying to assign: " + var_export_internal(attrs));
                }
            } else {
                Array.array_push(this.in_content, new Array<Object>(new ArrayEntry<Object>(tag), new ArrayEntry<Object>(this.depth), new ArrayEntry<Object>(type)));
            }
        } else if (equal(tag, "link")) {
            Array.array_push(this.current.links, attrs);
        } else if (equal(tag, "category")) {
            Array.array_push(this.current.categories, attrs);
        }

        this.ns_decls = new Array<Object>();
    }

    public void end_element(Object parser, String name) {
        Object tag = null;
        int ccount = 0;
        Object origtype = null;
        Array<String> newcontent;
        Array<Object> c = new Array<Object>();
        Array<Object> endtag = new Array<Object>();
        tag = Array.array_pop(QRegExPosix.split(":", name));
        ccount = Array.count(this.in_content);

        if (!empty(this.in_content)) {
        	// if we are *in* content, then let's proceed to serialize it
        	// then let's finalize the content
            if (equal(this.in_content.getArrayValue(0).getValue(0), tag) && equal(this.in_content.getArrayValue(0).getValue(1), this.depth)) {
                origtype = this.in_content.getArrayValue(0).getValue(2);
                Array.array_shift(this.in_content);
                newcontent = new Array<String>();

                for (Map.Entry javaEntry369 : this.in_content.entrySet()) {
                    c = (Array<Object>) javaEntry369.getValue();

                    if (equal(Array.count(c), 3)) {
                        Array.array_push(newcontent, c.getValue(2));
                    } else {
                        if (this.is_xhtml || this.is_text) {
                            Array.array_push(newcontent, this.xml_escape(c));
                        } else {
                            Array.array_push(newcontent, c);
                        }
                    }
                }

                if (Array.in_array(tag, this.ATOM_CONTENT_ELEMENTS)) {
                    // Modified by Numiton
                    Array value = new Array(new ArrayEntry(origtype, Strings.join("", newcontent)));

                    if (equal(tag, "content")) {
                        this.current.content = value;
                    } else if (equal(tag, "summary")) {
                        this.current.summary = value;
                    } else if (equal(tag, "title")) {
                        this.current.title = value;
                    } else if (equal(tag, "subtitle")) {
                        this.current.subtitle = value;
                    } else if (equal(tag, "rights")) {
                        this.current.rights = value;
                    }
                } else {
                    // Modified by Numiton
                    String value = Strings.join("", newcontent);

                    if (equal(tag, "id")) {
                        this.current.id = value;
                    } else if (equal(tag, "updated")) {
                        this.current.updated = value;
                    } else if (equal(tag, "published")) {
                        this.current.published = value;
                    } else if (equal(tag, "draft")) {
                        this.current.draft = value;
                    }
                }

                this.in_content = new Array<Object>();
            } else if (equal(this.in_content.getArrayValue(ccount - 1).getValue(0), tag) && equal(this.in_content.getArrayValue(ccount - 1).getValue(1), this.depth)) {
                this.in_content.getArrayValue(ccount - 1).putValue(2, Strings.substr(strval(this.in_content.getArrayValue(ccount - 1).getValue(2)), 0, -1) + "/>");
            } else {
            	// else, just finalize the current element's content
                endtag = this.ns_to_prefix(name);
                Array.array_push(this.in_content, new Array<Object>(new ArrayEntry<Object>(tag), new ArrayEntry<Object>(this.depth), new ArrayEntry<Object>("</" + strval(endtag.getValue(1)) + ">")));
            }
        }

        Array.array_shift(this.ns_contexts);
        this.depth--;

        if (equal(name, this.NS + ":entry")) {
            Array.array_push(this.feed.entries, this.current);
            this.current = null;
        }

        this._p("end_element(\'" + name + "\')");
    }

    public void start_ns(Object parser, Object prefix, Object uri) {
        this._p("starting: " + prefix + ":" + uri);
        Array.array_push(this.ns_decls, new Array<Object>(new ArrayEntry<Object>(prefix), new ArrayEntry<Object>(uri)));
    }

    public void end_ns(Object parser, Object prefix) {
        this._p("ending: #" + prefix + "#");
    }

    public void cdata(Object parser, String data) {
        this._p("data: #" + Strings.str_replace(new Array<Object>(new ArrayEntry<Object>("\n")), new Array<Object>(new ArrayEntry<Object>("\\n")), Strings.trim(data)) + "#");

        if (!empty(this.in_content)) {
            Array.array_push(this.in_content, data);
        }
    }

    public void _default(Object parser, Object data) {
    	// when does this gets called?
    }

    public Array<Object> ns_to_prefix(String name) {
        return ns_to_prefix(name, false);
    }

    public Array<Object> ns_to_prefix(String qname, boolean attr) {
        Array<String> components = new Array<String>();
        Object name = null;
        String ns = null;
        Array<Object> mapping = new Array<Object>();
        Array<Object> context = null;
        
        // split 'http://www.w3.org/1999/xhtml:div' into ('http','//www.w3.org/1999/xhtml','div')
        components = QRegExPosix.split(":", qname);
        
        // grab the last one (e.g 'div')
        name = Array.array_pop(components);

        if (!empty(components)) {
        	// re-join back the namespace component
            ns = Strings.join(":", components);

            for (Map.Entry javaEntry370 : this.ns_contexts.entrySet()) {
                context = (Array<Object>) javaEntry370.getValue();

                for (Map.Entry javaEntry371 : context.entrySet()) {
                    mapping = (Array<Object>) javaEntry371.getValue();

                    if (equal(mapping.getValue(1), ns) && (Strings.strlen(strval(mapping.getValue(0))) > 0)) {
                        return new Array<Object>(new ArrayEntry<Object>(mapping), new ArrayEntry<Object>(strval(mapping.getValue(0)) + ":" + strval(name)));
                    }
                }
            }
        }

        if (attr) {
            return new Array<Object>(new ArrayEntry<Object>(null), new ArrayEntry<Object>(name));
        } else {
            for (Map.Entry javaEntry372 : this.ns_contexts.entrySet()) {
                context = (Array<Object>) javaEntry372.getValue();

                for (Map.Entry javaEntry373 : context.entrySet()) {
                    mapping = (Array<Object>) javaEntry373.getValue();

                    if (equal(Strings.strlen(strval(mapping.getValue(0))), 0)) {
                        return new Array<Object>(new ArrayEntry<Object>(mapping), new ArrayEntry<Object>(name));
                    }
                }
            }
        }

        return new Array<Object>();
    }

    public boolean is_declared_content_ns(Object new_mapping) {
        Object mapping = null;
        Array<Object> context = null;

        for (Map.Entry javaEntry374 : this.content_ns_contexts.entrySet()) {
            context = (Array<Object>) javaEntry374.getValue();

            for (Map.Entry javaEntry375 : context.entrySet()) {
                mapping = javaEntry375.getValue();

                if (equal(new_mapping, mapping)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Array<String> xml_escape(Array string) {
        return Strings.str_replace(
            new Array<String>(new ArrayEntry<String>("&"), new ArrayEntry<String>("\""), new ArrayEntry<String>("\'"), new ArrayEntry<String>("<"), new ArrayEntry<String>(">")),
            new Array<String>(new ArrayEntry<String>("&amp;"), new ArrayEntry<String>("&quot;"), new ArrayEntry<String>("&apos;"), new ArrayEntry<String>("&lt;"), new ArrayEntry<String>("&gt;")),
            string);
    }

    public String xml_escape(String string) {
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
