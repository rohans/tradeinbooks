/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Link_parse_opmlPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.Wp_configPage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.xml.XMLParser;


@Controller
@Scope("request")
public class Link_parse_opmlPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Link_parse_opmlPage.class.getName());
    public Array<Object> map = new Array<Object>();
    public Array<Object> targets = new Array<Object>();
    public Array<Object> opml_map;
    public int xml_parser;

    @Override
    @RequestMapping("/wp-admin/link-parse-opml.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/link_parse_opml";
    }

    /**
     ** startElement()
     ** Callback function. Called at the start of a new xml tag.
     **/
    public void startElement(Object parser, Object tagName, Array<Object> attrs) {
        Object key = null;
        Object link_name = null;
        Object link_url = null;
        Object link_target = null;
        Object link_rss = null;
        Object link_description = null;

        if (equal(tagName, "OUTLINE")) {
            for (Map.Entry javaEntry276 : Array.array_keys(map).entrySet()) {
                key = javaEntry276.getValue();

                if (isset(attrs.getValue(key))) {
                    /* Modified by Numiton */ ;

                    if (equal(key, "URL")) {
                        link_url = attrs.getValue(key);
                    } else if (equal(key, "HTMLURL")) {
                        link_url = attrs.getValue(key);
                    } else if (equal(key, "TEXT")) {
                        link_name = attrs.getValue(key);
                    } else if (equal(key, "TITLE")) {
                        link_name = attrs.getValue(key);
                    } else if (equal(key, "TARGET")) {
                        link_target = attrs.getValue(key);
                    } else if (equal(key, "DESCRIPTION")) {
                        link_description = attrs.getValue(key);
                    } else if (equal(key, "XMLURL")) {
                        link_rss = attrs.getValue(key);
                    }
                }
            }

            //echo("got data: link_url = [$link_url], link_name = [$link_name], link_target = [$link_target], link_description = [$link_description]<br />\n");

    		// save the data away.
            gVars.names.putValue(link_name);
            gVars.urls.putValue(link_url);
            targets.putValue(link_target);
            gVars.feeds.putValue(link_rss);
            gVars.descriptions.putValue(link_description);
        } // end if outline
    }

    /**
     ** endElement()
     ** Callback function. Called at the end of an xml tag.
     **/
    public void endElement(Object parser, Object tagName) {
    	// nothing to do.
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_link_parse_opml_block1");
        gVars.webEnv = webEnv;
        
        requireOnce(gVars, gConsts, Wp_configPage.class);
        
	    // columns we wish to find are:  link_url, link_name, link_target, link_description
	    // we need to map XML attribute names to our columns
        opml_map = new Array<Object>(new ArrayEntry<Object>("URL", "link_url"), new ArrayEntry<Object>("HTMLURL", "link_url"), new ArrayEntry<Object>("TEXT", "link_name"),
                new ArrayEntry<Object>("TITLE", "link_name"), new ArrayEntry<Object>("TARGET", "link_target"), new ArrayEntry<Object>("DESCRIPTION", "link_description"),
                new ArrayEntry<Object>("XMLURL", "link_rss"));
        
        map = Array.arrayCopy(opml_map);
        
        	// Create an XML parser
        xml_parser = XMLParser.xml_parser_create(gVars.webEnv);
        
        // Set the functions to handle opening and closing tags
        XMLParser.xml_set_element_handler(gVars.webEnv, xml_parser, new Callback("startElement", this), new Callback("endElement", this));

        if (!booleanval(XMLParser.xml_parse(gVars.webEnv, xml_parser, gVars.opml, true))) {
            echo(
                gVars.webEnv,
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("XML error: %1$s at line %2$s", "default"),
                    XMLParser.xml_error_string(XMLParser.xml_get_error_code(gVars.webEnv, xml_parser)), XMLParser.xml_get_current_line_number(gVars.webEnv, xml_parser)));
        }

        // Free up memory used by the XML parser
        XMLParser.xml_parser_free(gVars.webEnv, xml_parser);

        return DEFAULT_VAL;
    }
}
