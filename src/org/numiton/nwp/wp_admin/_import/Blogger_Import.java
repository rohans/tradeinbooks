/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Blogger_Import.java,v 1.3 2008/10/10 16:48:04 numiton Exp $
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

import static com.numiton.PhpCommonConstants.INVALID_RESOURCE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.numiton.nwp.CallbackUtils;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.*;

import com.numiton.*;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;

public class Blogger_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Blogger_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public String token;
    public String title;
    public Array<Object> blogs = new Array<Object>();
    public Array<Object> _import = new Array<Object>();
    public int importer_started;
    public Object importing_blog;
    public Array<Object> importer_users;
    /**
     * Generated in place of local variable 'headers' from method 'show_blogs'
     * because it is used inside an inner class.
     */
    Array<String> show_blogs_headers = new Array<String>();

    /**
     * Generated in place of local variable 'xml' from method 'show_blogs'
     * because it is used inside an inner class.
     */
    String show_blogs_xml = null;

    /**
     * Generated in place of local variable 'response_headers' from method
     * 'parse_response' because it is used inside an inner class.
     */
    String parse_response_response_headers = null;

    /**
     * Generated in place of local variable 'response_body' from method
     * 'parse_response' because it is used inside an inner class.
     */
    String parse_response_response_body = null;

    /**
     * Generated in place of local variable 'header' from method
     * 'parse_response' because it is used inside an inner class.
     */
    Object parse_response_header = null;

    /**
     * Generated in place of local variable 'value' from method 'parse_response'
     * because it is used inside an inner class.
     */
    Object parse_response_value = null;

    public Blogger_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        importer_started = DateTime.time();

        if (isset(gVars.webEnv._GET.getValue("import")) && equal(gVars.webEnv._GET.getValue("import"), "blogger")) {
            getIncluded(Script_loaderPage.class, gVars, gConsts).wp_enqueue_script("jquery", false, new Array<Object>(), false);
            getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head", Callback.createCallbackArray(this, "admin_head"), 10, 1);
        }
    }

    /**
     * Shows the welcome screen and the magic auth link.
     */
    public void greet() {
        Object next_url = null;
        String auth_url = null;
        Object title = null;
        Object welcome = null;
        Object prereqs = null;
        Object stepone = null;
        Object auth = null;
        next_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/index.php?import=blogger&noheader=true";
        auth_url = "https://www.google.com/accounts/AuthSubRequest";
        title = getIncluded(L10nPage.class, gVars, gConsts).__("Import Blogger", "default");
        welcome = getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This importer allows you to import posts and comments from your Blogger account into your nWordPress blog.", "default");
        prereqs = getIncluded(L10nPage.class, gVars, gConsts).__(
                    "To use this importer, you must have a Google account and an upgraded (New, was Beta) blog hosted on blogspot.com or a custom domain (not FTP).",
                    "default");
        stepone = getIncluded(L10nPage.class, gVars, gConsts).__(
                    "The first thing you need to do is tell Blogger to let nWordPress access your account. You will be sent back here after providing authorization.",
                    "default");
        auth = getIncluded(L10nPage.class, gVars, gConsts).__("Authorize", "default");
        echo(
                gVars.webEnv,
                "\n\t\t<div class=\'wrap\'><h2>" + strval(title) + "</h2><p>" + strval(welcome) + "</p><p>" + strval(prereqs) + "</p><p>" + strval(stepone) + "</p>\n\t\t\t<form action=\'" + auth_url +
                "\' method=\'get\'>\n\t\t\t\t<p class=\'submit\' style=\'text-align:left;\'>\n\t\t\t\t\t<input type=\'submit\' class=\'button\' value=\'" + strval(auth) +
                "\' />\n\t\t\t\t\t<input type=\'hidden\' name=\'scope\' value=\'http://www.blogger.com/feeds/\' />\n\t\t\t\t\t<input type=\'hidden\' name=\'session\' value=\'1\' />\n\t\t\t\t\t<input type=\'hidden\' name=\'secure\' value=\'0\' />\n\t\t\t\t\t<input type=\'hidden\' name=\'next\' value=\'" +
                strval(next_url) + "\' />\n\t\t\t\t</p>\n\t\t\t</form>\n\t\t</div>\n");
    }

    public void uh_oh(String title, Object message, String info) {
        echo(gVars.webEnv, "<div class=\'wrap\'><h2>" + title + "</h2><p>" + strval(message) + "</p><pre>" + info + "</pre></div>");
    }

    public boolean auth() {
        String token;
        Array<String> headers = new Array<String>();
        String request = null;
        int sock = 0;
        String response = null;
        Array<Object> matches = new Array<Object>();
        
		// We have a single-use token that must be upgraded to a session token.
        token = QRegExPerl.preg_replace("/[^-_0-9a-zA-Z]/", "", strval(gVars.webEnv._GET.getValue("token")));
        headers = new Array<String>(new ArrayEntry<String>("GET /accounts/AuthSubSessionToken HTTP/1.0"), new ArrayEntry<String>("Authorization: AuthSub token=\"" + token + "\""));
        request = Strings.join("\r\n", headers) + "\r\n\r\n";
        sock = this._get_auth_sock();

        if (!booleanval(sock)) {
            return false;
        }

        response = this._txrx(sock, request);
        QRegExPerl.preg_match("/token=([-_0-9a-z]+)/i", response, matches);

        if (empty(matches.getValue(1))) {
            this.uh_oh(
                getIncluded(L10nPage.class, gVars, gConsts).__("Authorization failed", "default"),
                getIncluded(L10nPage.class, gVars, gConsts).__("Something went wrong. If the problem persists, send this info to support:", "default"),
                Strings.htmlspecialchars(response));

            return false;
        }

        this.token = strval(matches.getValue(1));
        getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(getIncluded(FunctionsPage.class, gVars, gConsts).remove_query_arg(
                new Array<Object>(new ArrayEntry<Object>("token"), new ArrayEntry<Object>("noheader"))), 302);

        return false;
    }

    public Array<Object> get_token_info() {
        Array<String> headers = new Array<String>();
        String request = null;
        int sock = 0;
        String response = null;
        headers = new Array<String>(new ArrayEntry<String>("GET /accounts/AuthSubTokenInfo  HTTP/1.0"), new ArrayEntry<String>("Authorization: AuthSub token=\"" + this.token + "\""));
        request = Strings.join("\r\n", headers) + "\r\n\r\n";
        sock = this._get_auth_sock();

        if (!booleanval(sock)) {
            return null;
        }

        response = this._txrx(sock, request);

        return this.parse_response(response);
    }

    public boolean token_is_valid() {
        Array<Object> info = new Array<Object>();
        info = this.get_token_info();

        if (equal(info.getValue("code"), 200)) {
            return true;
        }

        return false;
    }

    public Object show_blogs() {
        return show_blogs(0);
    }

    public Object show_blogs(int iter) {
        String request = null;
        int sock = 0;
        String response = null;
        int p = 0;
        Array<Object> vals = new Array<Object>();
        Array<Object> index = new Array<Object>();
        Array<Object> blog = new Array<Object>();
        Array<Object> tag = new Array<Object>();
        Array<String> parts = new Array<String>();
        int i = 0;
        Object start = null;
        Object _continue = null;
        Object stop = null;
        Object authors = null;
        Object loadauth = null;
        Object authhead = null;
        Object nothing = null;
        Object title = null;
        Object name = null;
        String url = null;
        Object action = null;
        Object posts = null;
        Object comments = null;
        Object noscript = null;
        int interval = 0;
        Object value = null;
        Object blogtitle = null;
        int pdone = 0;
        int cdone = 0;
        Object init = null;
        String pstat = null;
        String cstat = null;
        Object rows = null;
        Object stopping = null;

        if (empty(this.blogs)) {
            show_blogs_headers = new Array<String>(
                    new ArrayEntry<String>("GET /feeds/default/blogs HTTP/1.0"),
                    new ArrayEntry<String>("Host: www.blogger.com"),
                    new ArrayEntry<String>("Authorization: AuthSub token=\"" + this.token + "\""));
            request = Strings.join("\r\n", show_blogs_headers) + "\r\n\r\n";
            sock = this._get_blogger_sock();

            if (!booleanval(sock)) {
                return null;
            }

            response = this._txrx(sock, request);
            
			// Quick and dirty XML mining.
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        show_blogs_headers = srcArray.getArrayValue(0);
                        show_blogs_xml = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(Strings.explode("\r\n\r\n", response));
            p = XMLParser.xml_parser_create(gVars.webEnv);
            XMLParser.xml_parse_into_struct(gVars.webEnv, p, show_blogs_xml, vals, index);
            XMLParser.xml_parser_free(gVars.webEnv, p);
            this.title = strval(vals.getArrayValue(index.getArrayValue("TITLE").getValue(0)).getValue("value"));

			// Give it a few retries... this step often flakes out the first time.
            if (empty(index.getValue("ENTRY"))) {
                if (iter < 3) {
                    return this.show_blogs(iter + 1);
                } else {
                    this.uh_oh(
                        getIncluded(L10nPage.class, gVars, gConsts).__("Trouble signing in", "default"),
                        getIncluded(L10nPage.class, gVars, gConsts).__("We were not able to gain access to your account. Try starting over.", "default"),
                        "");

                    return false;
                }
            }

            for (Map.Entry javaEntry29 : (Set<Map.Entry>) index.getArrayValue("ENTRY").entrySet()) {
                i = intval(javaEntry29.getValue());
                blog = new Array<Object>();

                while (booleanval(tag = vals.getArrayValue(i)) && !(equal(tag.getValue("tag"), "ENTRY") && equal(tag.getValue("type"), "close"))) {
                    if (equal(tag.getValue("tag"), "TITLE")) {
                        blog.putValue("title", tag.getValue("value"));
                    } else if (equal(tag.getValue("tag"), "SUMMARY")) {
                        equal(blog.getValue("summary"), tag.getValue("value"));
                    } else if (equal(tag.getValue("tag"), "LINK")) {
                        if (equal(tag.getArrayValue("attributes").getValue("REL"), "alternate") && equal(tag.getArrayValue("attributes").getValue("TYPE"), "text/html")) {
                            parts = URL.parse_url(strval(tag.getArrayValue("attributes").getValue("HREF")));
                            blog.putValue("host", parts.getValue("host"));
                        } else if (equal(tag.getArrayValue("attributes").getValue("REL"), "edit")) {
                            blog.putValue("gateway", tag.getArrayValue("attributes").getValue("HREF"));
                        }
                    }

                    ++i;
                }

                if (!empty(blog)) {
                    blog.putValue("total_posts", this.get_total_results("posts", blog.getValue("host")));
                    blog.putValue("total_comments", this.get_total_results("comments", blog.getValue("host")));
                    blog.putValue("mode", "init");
                    this.blogs.putValue(blog);
                }
            }

            if (empty(this.blogs)) {
                this.uh_oh(
                    getIncluded(L10nPage.class, gVars, gConsts).__("No blogs found", "default"),
                    getIncluded(L10nPage.class, gVars, gConsts).__("We were able to log in but there were no blogs. Try a different account next time.", "default"),
                    "");

                return false;
            }
        }

      //echo '<pre>'.print_r($this,1).'</pre>';
        start = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import", "default"));
        _continue = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Continue", "default"));
        stop = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Importing...", "default"));
        authors = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Set Authors", "default"));
        loadauth = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Preparing author mapping form...", "default"));
        authhead = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Final Step: Author Mapping", "default"));
        nothing = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Nothing was imported. Had you already imported this blog?", "default"));
        title = getIncluded(L10nPage.class, gVars, gConsts).__("Blogger Blogs", "default");
        name = getIncluded(L10nPage.class, gVars, gConsts).__("Blog Name", "default");
        url = getIncluded(L10nPage.class, gVars, gConsts).__("Blog URL", "default");
        action = getIncluded(L10nPage.class, gVars, gConsts).__("The Magic Button", "default");
        posts = getIncluded(L10nPage.class, gVars, gConsts).__("Posts", "default");
        comments = getIncluded(L10nPage.class, gVars, gConsts).__("Comments", "default");
        noscript = getIncluded(L10nPage.class, gVars, gConsts).__(
                    "This feature requires Javascript but it seems to be disabled. Please enable Javascript and then reload this page. Don\'t worry, you can turn it back off when you\'re done.",
                    "default");
        interval = gConsts.getSTATUS_INTERVAL() * 1000;

        for (Map.Entry javaEntry30 : this.blogs.entrySet()) {
            i = intval(javaEntry30.getKey());
            blog = (Array<Object>) javaEntry30.getValue();

            if (equal(blog.getValue("mode"), "init")) {
                value = start;
            } else if (equal(blog.getValue("mode"), "posts") || equal(blog.getValue("mode"), "comments")) {
                value = _continue;
            } else {
                value = authors;
            }

            blogtitle = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(strval(blog.getValue("title")));
            pdone = (isset(blog.getValue("posts_done"))
                ? intval(blog.getValue("posts_done"))
                : 0);
            cdone = (isset(blog.getValue("comments_done"))
                ? intval(blog.getValue("comments_done"))
                : 0);
            init = strval(init) + "blogs[" + strval(i) + "]=new blog(" + strval(i) + ",\'" + strval(blogtitle) + "\',\'" + strval(blog.getValue("mode")) + "\'," + this.get_js_status(i) + ");";
            pstat = "<div class=\'ind\' id=\'pind" + strval(i) + "\'>&nbsp;</div><div id=\'pstat" + strval(i) + "\' class=\'stat\'>" + strval(pdone) + "/" + strval(blog.getValue("total_posts")) +
                "</div>";
            cstat = "<div class=\'ind\' id=\'cind" + strval(i) + "\'>&nbsp;</div><div id=\'cstat" + strval(i) + "\' class=\'stat\'>" + strval(cdone) + "/" + strval(blog.getValue("total_comments")) +
                "</div>";
            rows = strval(rows) + "<tr id=\'blog" + strval(i) + "\'><td class=\'blogtitle\'>" + strval(blogtitle) + "</td><td class=\'bloghost\'>" + strval(blog.getValue("host")) +
                "</td><td class=\'bar\'>" + pstat + "</td><td class=\'bar\'>" + cstat + "</td><td class=\'submit\'><input type=\'submit\' class=\'button\' id=\'submit" + strval(i) + "\' value=\'" +
                strval(value) + "\' /><input type=\'hidden\' name=\'blog\' value=\'" + strval(i) + "\' /></td></tr>\n";
        }

        echo(
                gVars.webEnv,
                "<div class=\'wrap\'><h2>" + strval(title) + "</h2><noscript>" + strval(noscript) + "</noscript><table cellpadding=\'5px\'><thead><td>" + strval(name) + "</td><td>" + url +
                "</td><td>" + strval(posts) + "</td><td>" + strval(comments) + "</td><td>" + strval(action) + "</td></thead>\n" + strval(rows) + "</table></form></div>");
        echo(
                gVars.webEnv,
                "\n\t\t<script type=\'text/javascript\'>\n\t\t\tvar strings = {cont:\'" + strval(_continue) + "\',stop:\'" + strval(stop) + "\',stopping:\'" + strval(stopping) + "\',authors:\'" +
                strval(authors) + "\',nothing:\'" + strval(nothing) +
                "\'};\n\t\t\tvar blogs = {};\n\t\t\tfunction blog(i, title, mode, status){\n\t\t\t\tthis.blog   = i;\n\t\t\t\tthis.mode   = mode;\n\t\t\t\tthis.title  = title;\n\t\t\t\tthis.status = status;\n\t\t\t\tthis.button = document.getElementById(\'submit\'+this.blog);\n\t\t\t};\n\t\t\tblog.prototype = {\n\t\t\t\tstart: function() {\n\t\t\t\t\tthis.cont = 1;\n\t\t\t\t\tthis.kick();\n\t\t\t\t\tthis.check();\n\t\t\t\t},\n\t\t\t\tkick: function() {\n\t\t\t\t\t++this.kicks;\n\t\t\t\t\tvar i = this.blog;\n\t\t\t\t\tjQuery.post(\'admin.php?import=blogger&noheader=1\',{blog:this.blog},function(text,result){blogs[i].kickd(text,result)});\n\t\t\t\t},\n\t\t\t\tcheck: function() {\n\t\t\t\t\t++this.checks;\n\t\t\t\t\tvar i = this.blog;\n\t\t\t\t\tjQuery.post(\'admin.php?import=blogger&noheader=1&status=1\',{blog:this.blog},function(text,result){blogs[i].checkd(text,result)});\n\t\t\t\t},\n\t\t\t\tkickd: function(text, result) {\n\t\t\t\t\tif ( result == \'error\' ) {\n\t\t\t\t\t\t// TODO: exception handling\n\t\t\t\t\t\tif ( this.cont )\n\t\t\t\t\t\t\tsetTimeout(\'blogs[\'+this.blog+\'].kick()\', 1000);\n\t\t\t\t\t} else {\n\t\t\t\t\t\tif ( text == \'done\' ) {\n\t\t\t\t\t\t\tthis.stop();\n\t\t\t\t\t\t\tthis.done();\n\t\t\t\t\t\t} else if ( text == \'nothing\' ) {\n\t\t\t\t\t\t\tthis.stop();\n\t\t\t\t\t\t\tthis.nothing();\n\t\t\t\t\t\t} else if ( text == \'continue\' ) {\n\t\t\t\t\t\t\tthis.kick();\n\t\t\t\t\t\t} else if ( this.mode = \'stopped\' )\n\t\t\t\t\t\t\tjQuery(this.button).attr(\'value\', strings.cont);\n\t\t\t\t\t}\n\t\t\t\t\t--this.kicks;\n\t\t\t\t},\n\t\t\t\tcheckd: function(text, result) {\n\t\t\t\t\tif ( result == \'error\' ) {\n\t\t\t\t\t\t// TODO: exception handling\n\t\t\t\t\t} else {\n\t\t\t\t\t\teval(\'this.status=\'+text);\n\t\t\t\t\t\tjQuery(\'#pstat\'+this.blog).empty().append(this.status.p1+\'/\'+this.status.p2);\n\t\t\t\t\t\tjQuery(\'#cstat\'+this.blog).empty().append(this.status.c1+\'/\'+this.status.c2);\n\t\t\t\t\t\tthis.update();\n\t\t\t\t\t\tif ( this.cont || this.kicks > 0 )\n\t\t\t\t\t\t\tsetTimeout(\'blogs[\'+this.blog+\'].check()\', " +
                strval(interval) +
                ");\n\t\t\t\t\t}\n\t\t\t\t\t--this.checks;\n\t\t\t\t},\n\t\t\t\tupdate: function() {\n\t\t\t\t\tjQuery(\'#pind\'+this.blog).width(((this.status.p1>0&&this.status.p2>0)?(this.status.p1/this.status.p2*jQuery(\'#pind\'+this.blog).parent().width()):1)+\'px\');\n\t\t\t\t\tjQuery(\'#cind\'+this.blog).width(((this.status.c1>0&&this.status.c2>0)?(this.status.c1/this.status.c2*jQuery(\'#cind\'+this.blog).parent().width()):1)+\'px\');\n\t\t\t\t},\n\t\t\t\tstop: function() {\n\t\t\t\t\tthis.cont = 0;\n\t\t\t\t},\n\t\t\t\tdone: function() {\n\t\t\t\t\tthis.mode = \'authors\';\n\t\t\t\t\tjQuery(this.button).attr(\'value\', strings.authors);\n\t\t\t\t},\n\t\t\t\tnothing: function() {\n\t\t\t\t\tthis.mode = \'nothing\';\n\t\t\t\t\tjQuery(this.button).remove();\n\t\t\t\t\talert(strings.nothing);\n\t\t\t\t},\n\t\t\t\tgetauthors: function() {\n\t\t\t\t\tif ( jQuery(\'div.wrap\').length > 1 )\n\t\t\t\t\t\tjQuery(\'div.wrap\').gt(0).remove();\n\t\t\t\t\tjQuery(\'div.wrap\').empty().append(\'<h2>" +
                strval(authhead) + "</h2><h3>\' + this.title + \'</h3>\');\n\t\t\t\t\tjQuery(\'div.wrap\').append(\'<p id=\"auth\">" + strval(loadauth) +
                "</p>\');\n\t\t\t\t\tjQuery(\'p#auth\').load(\'index.php?import=blogger&noheader=1&authors=1\',{blog:this.blog});\n\t\t\t\t},\n\t\t\t\tinit: function() {\n\t\t\t\t\tthis.update();\n\t\t\t\t\tvar i = this.blog;\n\t\t\t\t\tjQuery(this.button).bind(\'click\', function(){return blogs[i].click();});\n\t\t\t\t\tthis.kicks = 0;\n\t\t\t\t\tthis.checks = 0;\n\t\t\t\t},\n\t\t\t\tclick: function() {\n\t\t\t\t\tif ( this.mode == \'init\' || this.mode == \'stopped\' || this.mode == \'posts\' || this.mode == \'comments\' ) {\n\t\t\t\t\t\tthis.mode = \'started\';\n\t\t\t\t\t\tthis.start();\n\t\t\t\t\t\tjQuery(this.button).attr(\'value\', strings.stop);\n\t\t\t\t\t} else if ( this.mode == \'started\' ) {\n\t\t\t\t\t\treturn 0; // let it run...\n\t\t\t\t\t\tthis.mode = \'stopped\';\n\t\t\t\t\t\tthis.stop();\n\t\t\t\t\t\tif ( this.checks > 0 || this.kicks > 0 ) {\n\t\t\t\t\t\t\tthis.mode = \'stopping\';\n\t\t\t\t\t\t\tjQuery(this.button).attr(\'value\', strings.stopping);\n\t\t\t\t\t\t} else {\n\t\t\t\t\t\t\tjQuery(this.button).attr(\'value\', strings.cont);\n\t\t\t\t\t\t}\n\t\t\t\t\t} else if ( this.mode == \'authors\' ) {\n\t\t\t\t\t\tdocument.location = \'index.php?import=blogger&authors=1&blog=\'+this.blog;\n\t\t\t\t\t\t//this.mode = \'authors2\';\n\t\t\t\t\t\t//this.getauthors();\n\t\t\t\t\t}\n\t\t\t\t\treturn 0;\n\t\t\t\t}\n\t\t\t};\n\t\t\t" +
                strval(init) + "\n\t\t\tjQuery.each(blogs, function(i, me){me.init();});\n\t\t</script>\n");

        return false;
    }

    /**
     * Handy function for stopping the script after a number of seconds.
     */
    public boolean have_time() {
        if ((DateTime.time() - importer_started) > gConsts.getMAX_EXECUTION_TIME()) {
            System.exit("continue");
        }

        return true;
    }

    public int get_total_results(String type, Object host) {
        Array<String> headers;
        String request = null;
        int sock = 0;
        String response = null;
        int parser = 0;
        Array<Object> struct = new Array<Object>();
        Array<Object> index = new Array<Object>();
        Object total_results = null;
        headers = new Array<String>(
                new ArrayEntry<String>("GET /feeds/" + type + "/default?max-results=1&start-index=2 HTTP/1.0"),
                new ArrayEntry<String>("Host: " + strval(host)),
                new ArrayEntry<String>("Authorization: AuthSub token=\"" + this.token + "\""));
        request = Strings.join("\r\n", headers) + "\r\n\r\n";
        sock = this._get_blogger_sock(strval(host));

        if (!booleanval(sock)) {
            return 0;
        }

        response = this._txrx(sock, request);

        Array responseArray = this.parse_response(response);
        parser = XMLParser.xml_parser_create(gVars.webEnv);
        XMLParser.xml_parse_into_struct(gVars.webEnv, parser, strval(responseArray.getValue("body")), struct, index);
        XMLParser.xml_parser_free(gVars.webEnv, parser);
        total_results = struct.getArrayValue(index.getArrayValue("OPENSEARCH:TOTALRESULTS").getValue(0)).getValue("value");

        return intval(total_results);
    }

    public Object import_blog(int blogID) {
        Array<Object> blog = new Array<Object>();
        int total_results = 0;
        int start_index = 0;
        String query = null;
        Array<Object> index = new Array<Object>();
        Array<Object> struct = new Array<Object>();
        Array<Object> entries = new Array<Object>();
        Array<String> headers;
        String request = null;
        int sock = 0;
        String response = null;
        Array matches = new Array();
        String entry = null;
        AtomParser AtomParser = null;
        Object result = 0;
        int links = 0;
        String match = null;
        Array<Object> q = new Array<Object>();
        importing_blog = blogID;

        if (isset(gVars.webEnv._GET.getValue("authors"))) {
            return print(gVars.webEnv, this.get_author_form());
        }

        Network.header(gVars.webEnv, "Content-Type: text/plain");

        if (isset(gVars.webEnv._GET.getValue("status"))) {
            System.exit(this.get_js_status());
        }

        if (isset(gVars.webEnv._GET.getValue("saveauthors"))) {
            System.exit(this.save_authors());
        }

        blog = this.blogs.getArrayValue(blogID);
        total_results = this.get_total_results("posts", blog.getValue("host"));
        this.blogs.getArrayValue(importing_blog).putValue("total_posts", total_results);
        start_index = total_results - gConsts.getMAX_RESULTS() + 1;

        if (isset(this.blogs.getArrayValue(importing_blog).getValue("posts_start_index"))) {
            start_index = intval(this.blogs.getArrayValue(importing_blog).getValue("posts_start_index"));
        } else if (total_results > gConsts.getMAX_RESULTS()) {
            start_index = total_results - gConsts.getMAX_RESULTS() + 1;
        } else {
            start_index = 1;
        }

		// This will be positive until we have finished importing posts
        if (start_index > 0) {
			// Grab all the posts
            this.blogs.getArrayValue(importing_blog).putValue("mode", "posts");
            query = "start-index=" + strval(start_index) + "&max-results=" + gConsts.getMAX_RESULTS();

            do {
                index = struct = entries = new Array<Object>();
                headers = new Array<String>(
                        new ArrayEntry<String>("GET /feeds/posts/default?" + query + " HTTP/1.0"),
                        new ArrayEntry<String>("Host: " + strval(blog.getValue("host"))),
                        new ArrayEntry<String>("Authorization: AuthSub token=\"" + this.token + "\""));
                request = Strings.join("\r\n", headers) + "\r\n\r\n";
                sock = this._get_blogger_sock(strval(blog.getValue("host")));

                if (!booleanval(sock)) {
                    return 0; // TODO: Error handling
                }

                response = this._txrx(sock, request);

                Array responseArray = this.parse_response(response);
                
				// Extract the entries and send for insertion
                QRegExPerl.preg_match_all("/<entry[^>]*>.*?<\\/entry>/s", strval(responseArray.getValue("body")), matches);

                if (booleanval(Array.count(matches.getValue(0)))) {
                    entries = Array.array_reverse(matches.getArrayValue(0));

                    for (Map.Entry javaEntry31 : entries.entrySet()) {
                        entry = strval(javaEntry31.getValue());
                        entry = "<feed>" + entry + "</feed>";
                        AtomParser = new AtomParser(gVars, gConsts);
                        AtomParser.parse(entry);
                        result = this.import_post(AtomParser.entry);

                        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                            return result;
                        }

                        AtomParser = null;
                    }
                } else {
                    break;
                }

				// Get the 'previous' query string which we'll use on the next iteration
                query = "";
                links = QRegExPerl.preg_match_all("/<link([^>]*)>/", strval(responseArray.getValue("body")), matches);

                if (booleanval(Array.count(matches.getValue(1)))) {
                    for (Map.Entry javaEntry32 : (Set<Map.Entry>) matches.getArrayValue(1).entrySet()) {
                        match = strval(javaEntry32.getValue());

                        if (QRegExPerl.preg_match("/rel=.previous./", match)) {
                            query = Strings.html_entity_decode(QRegExPerl.preg_replace("/^.*href=[\'\"].*\\?(.+)[\'\"].*$/", "$1", match));
                        }
                    }
                }

                if (booleanval(query)) {
                    Strings.parse_str(query, q);
                    this.blogs.getArrayValue(importing_blog).putValue("posts_start_index", intval(q.getValue("start-index")));
                } else {
                    this.blogs.getArrayValue(importing_blog).putValue("posts_start_index", 0);
                }

                this.save_vars();
            } while (!empty(query) && this.have_time());
        }

        total_results = this.get_total_results("comments", blog.getValue("host"));
        this.blogs.getArrayValue(importing_blog).putValue("total_comments", total_results);

        if (isset(this.blogs.getArrayValue(importing_blog).getValue("comments_start_index"))) {
            start_index = intval(this.blogs.getArrayValue(importing_blog).getValue("comments_start_index"));
        } else if (total_results > gConsts.getMAX_RESULTS()) {
            start_index = total_results - gConsts.getMAX_RESULTS() + 1;
        } else {
            start_index = 1;
        }

        if (start_index > 0) {
			// Grab all the comments
            this.blogs.getArrayValue(importing_blog).putValue("mode", "comments");
            query = "start-index=" + strval(start_index) + "&max-results=" + gConsts.getMAX_RESULTS();

            do {
                index = struct = entries = new Array<Object>();
                headers = new Array<String>(
                        new ArrayEntry<String>("GET /feeds/comments/default?" + query + " HTTP/1.0"),
                        new ArrayEntry<String>("Host: " + strval(blog.getValue("host"))),
                        new ArrayEntry<String>("Authorization: AuthSub token=\"" + this.token + "\""));
                request = Strings.join("\r\n", headers) + "\r\n\r\n";
                sock = this._get_blogger_sock(strval(blog.getValue("host")));

                if (!booleanval(sock)) {
                    return 0; // TODO: Error handling
                }

                response = this._txrx(sock, request);

                Array responseArray = this.parse_response(response);
                
				// Extract the comments and send for insertion
                QRegExPerl.preg_match_all("/<entry[^>]*>.*?<\\/entry>/s", strval(responseArray.getValue("body")), matches);

                if (booleanval(Array.count(matches.getValue(0)))) {
                    entries = Array.array_reverse(matches.getArrayValue(0));

                    for (Map.Entry javaEntry33 : entries.entrySet()) {
                        entry = strval(javaEntry33.getValue());
                        entry = "<feed>" + entry + "</feed>";
                        AtomParser = new AtomParser(gVars, gConsts);
                        AtomParser.parse(entry);
                        this.import_comment(AtomParser.entry);
                        AtomParser = null;
                    }
                }

				// Get the 'previous' query string which we'll use on the next iteration
                query = "";
                links = QRegExPerl.preg_match_all("/<link([^>]*)>/", strval(responseArray.getValue("body")), matches);

                if (booleanval(Array.count(matches.getValue(1)))) {
                    for (Map.Entry javaEntry34 : (Set<Map.Entry>) matches.getArrayValue(1).entrySet()) {
                        match = strval(javaEntry34.getValue());

                        if (QRegExPerl.preg_match("/rel=.previous./", match)) {
                            query = Strings.html_entity_decode(QRegExPerl.preg_replace("/^.*href=[\'\"].*\\?(.+)[\'\"].*$/", "$1", match));
                        }
                    }
                }

                Strings.parse_str(query, q);
                this.blogs.getArrayValue(importing_blog).putValue("comments_start_index", intval(q.getValue("start-index")));
                this.save_vars();
            } while (!empty(query) && this.have_time());
        }

        this.blogs.getArrayValue(importing_blog).putValue("mode", "authors");
        this.save_vars();

        if (!booleanval(this.blogs.getArrayValue(importing_blog).getValue("posts_done")) && !booleanval(this.blogs.getArrayValue(importing_blog).getValue("comments_done"))) {
            System.exit("nothing");
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "blogger");
        System.exit("done");

        return 0;
    }

    public String convert_date(String date) {
        Array<Object> date_bits = new Array<Object>();
        int offset = 0;
        int timestamp = 0;
        QRegExPerl.preg_match("#([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})(?:\\.[0-9]+)?(Z|[\\+|\\-][0-9]{2,4}){0,1}#", date, date_bits);
        offset = getIncluded(FormattingPage.class, gVars, gConsts).iso8601_timezone_to_offset(strval(date_bits.getValue(7)));
        timestamp = DateTime.gmmktime(
                intval(date_bits.getValue(4)),
                intval(date_bits.getValue(5)),
                intval(date_bits.getValue(6)),
                intval(date_bits.getValue(2)),
                intval(date_bits.getValue(3)),
                intval(date_bits.getValue(1)));
        
        timestamp = timestamp - offset; // Convert from Blogger local time to GMT
        timestamp = intval(timestamp + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600)); // Convert from GMT to WP local time

        return DateTime.gmdate("Y-m-d H:i:s", timestamp);
    }

    public String no_apos(String string) {
        return Strings.str_replace("&apos;", "\'", string);
    }

    public String min_whitespace(String string) {
        return QRegExPerl.preg_replace("|\\s+|", " ", string);
    }

    public Object import_post(AtomEntry entry) {
        String rel = null;
        Array<Object> link = new Array<Object>();
        Array<String> parts = new Array<String>();
        String post_date = null;
        String post_content = null;
        String post_title = null;
        String post_status = null;
        int post_id;
        Array<Object> post = new Array<Object>();
        String author;

		// The old permalink is all Blogger gives us to link comments to their posts.
        if (isset(entry.draft)) {
            rel = "self";
        } else {
            rel = "alternate";
        }

        for (Map.Entry javaEntry35 : entry.links.entrySet()) {
            link = (Array<Object>) javaEntry35.getValue();

            if (equal(link.getValue("rel"), rel)) {
                parts = URL.parse_url(strval(link.getValue("href")));
                entry.old_permalink = parts.getValue("path");

                break;
            }
        }

        post_date = this.convert_date(entry.published);
        post_content = Strings.trim(Strings.addslashes(gVars.webEnv, this.no_apos(Strings.html_entity_decode(entry.content))));
        post_title = Strings.trim(Strings.addslashes(gVars.webEnv, this.no_apos(this.min_whitespace(entry.title))));
        post_status = (isset(entry.draft)
            ? "draft"
            : "publish");

        // Modified by Numiton
		// Clean up content
        post_content = RegExPerl.preg_replace_callback("|<(/?[A-Z]+)|", new Callback("htmlTagToLowercase", CallbackUtils.class), post_content);
        post_content = Strings.str_replace("<br>", "<br />", post_content);
        post_content = Strings.str_replace("<hr>", "<hr />", post_content);

		// Checks for duplicates
        if (isset(this.blogs.getArrayValue(importing_blog).getArrayValue("posts").getValue(entry.old_permalink))) {
            this.blogs.getArrayValue(importing_blog).incValue("posts_skipped");
        } else if (booleanval(post_id = getIncluded(PostPage.class, gVars, gConsts).post_exists(post_title, post_content, post_date))) {
            this.blogs.getArrayValue(importing_blog).getArrayValue("posts").putValue(entry.old_permalink, post_id);
            this.blogs.getArrayValue(importing_blog).incValue("posts_skipped");
        } else {
            post = Array.compact(
                    new ArrayEntry("post_date", post_date),
                    new ArrayEntry("post_content", post_content),
                    new ArrayEntry("post_title", post_title),
                    new ArrayEntry("post_status", post_status));
            post_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(post);

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(post_id)) {
                return post_id;
            }

            getIncluded(TaxonomyPage.class, gVars, gConsts).wp_create_categories(Array.array_map(new Callback("addslashes", this), entry.categories), post_id);
            author = this.no_apos(Strings.strip_tags(entry.author));
            (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).add_post_meta(
                post_id,
                "blogger_blog",
                this.blogs.getArrayValue(importing_blog).getValue("host"),
                true);
            (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).add_post_meta(post_id, "blogger_author", author, true);
            (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).add_post_meta(post_id, "blogger_permalink", entry.old_permalink, true);
            this.blogs.getArrayValue(importing_blog).getArrayValue("posts").putValue(entry.old_permalink, post_id);
            this.blogs.getArrayValue(importing_blog).incValue("posts_done");
        }

        this.save_vars();

        return 0;
    }

    public void import_comment(AtomEntry entry) {
        Array<Object> link = new Array<Object>();
        Array<String> parts = new Array<String>();
        int comment_post_ID = 0;
        Array<Object> matches = new Array<Object>();
        String comment_author = null;
        String comment_author_url = null;
        String comment_date = null;
        String comment_content = null;
        Array<Object> comment = new Array<Object>();
        int comment_id = 0;

		// Drop the #fragment and we have the comment's old post permalink.
        for (Map.Entry javaEntry36 : entry.links.entrySet()) {
            link = (Array<Object>) javaEntry36.getValue();

            if (equal(link.getValue("rel"), "alternate")) {
                parts = URL.parse_url(strval(link.getValue("href")));
                entry.old_permalink = parts.getValue("fragment");
                entry.old_post_permalink = parts.getValue("path");

                break;
            }
        }

        comment_post_ID = intval(this.blogs.getArrayValue(importing_blog).getArrayValue("posts").getValue(entry.old_post_permalink));
        QRegExPerl.preg_match("#<name>(.+?)</name>.*(?:\\<uri>(.+?)</uri>)?#", entry.author, matches);
        comment_author = Strings.addslashes(gVars.webEnv, this.no_apos(Strings.strip_tags(strval(matches.getValue(1)))));
        comment_author_url = Strings.addslashes(gVars.webEnv, this.no_apos(Strings.strip_tags(strval(matches.getValue(2)))));
        comment_date = this.convert_date(entry.updated);
        comment_content = Strings.addslashes(gVars.webEnv, this.no_apos(Strings.html_entity_decode(entry.content)));

		// Clean up content
        // Modified by Numiton
        comment_content = RegExPerl.preg_replace_callback("|<(/?[A-Z]+)|", new Callback("htmlTagToLowercase", CallbackUtils.class), comment_content);
        comment_content = Strings.str_replace("<br>", "<br />", comment_content);
        comment_content = Strings.str_replace("<hr>", "<hr />", comment_content);

		// Checks for duplicates
        if (isset(this.blogs.getArrayValue(importing_blog).getArrayValue("comments").getValue(entry.old_permalink)) ||
                booleanval(getIncluded(CommentPage.class, gVars, gConsts).comment_exists(comment_author, comment_date))) {
            this.blogs.getArrayValue(importing_blog).incValue("comments_skipped");
        } else {
            comment = Array.compact(
                    new ArrayEntry("comment_post_ID", comment_post_ID),
                    new ArrayEntry("comment_author", comment_author),
                    new ArrayEntry("comment_author_url", comment_author_url),
                    new ArrayEntry("comment_date", comment_date),
                    new ArrayEntry("comment_content", comment_content));
            comment_id = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(comment);
            this.blogs.getArrayValue(importing_blog).getArrayValue("comments").putValue(entry.old_permalink, comment_id);
            this.blogs.getArrayValue(importing_blog).incValue("comments_done");
        }

        this.save_vars();
    }

    public String get_js_status() {
        return get_js_status(false);
    }

    public String get_js_status(Object blogObj) {
        int p1 = 0;
        int p2 = 0;
        int c1 = 0;
        int c2 = 0;
        Array<Object> blog;

        if (strictEqual(blogObj, false)) {
            blog = this.blogs.getArrayValue(importing_blog);
        } else {
            blog = this.blogs.getArrayValue(blogObj);
        }

        p1 = (isset(blog.getValue("posts_done"))
            ? intval(blog.getValue("posts_done"))
            : 0);
        p2 = (isset(blog.getValue("total_posts"))
            ? intval(blog.getValue("total_posts"))
            : 0);
        c1 = (isset(blog.getValue("comments_done"))
            ? intval(blog.getValue("comments_done"))
            : 0);
        c2 = (isset(blog.getValue("total_comments"))
            ? intval(blog.getValue("total_comments"))
            : 0);

        return "{p1:" + strval(p1) + ",p2:" + strval(p2) + ",c1:" + strval(c1) + ",c2:" + strval(c2) + "}";
    }

    public String get_author_form() {
        return get_author_form(false);
    }

    public String get_author_form(Object blogId) {
        Array<Object> blog = new Array<Object>();
        Array post_ids = new Array();
        Array<Object> authors = new Array<Object>();
        Object directions = null;
        Object heading = null;
        String blogtitle = null;
        Object mapthis = null;
        Object tothis = null;
        Object submit = null;
        String rows = null;
        Object i = null;
        Array<Object> author = new Array<Object>();

        if (strictEqual(blogId, false)) {
            blog = this.blogs.getArrayValue(importing_blog);
        } else {
            blog = this.blogs.getArrayValue(blog);
        }

        if (!isset(blog.getValue("authors"))) {
            post_ids = Array.array_values(blog.getArrayValue("posts"));
            authors = new Array<Object>(
                        gVars.wpdb.get_col("SELECT DISTINCT meta_value FROM " + gVars.wpdb.postmeta + " WHERE meta_key = \'blogger_author\' AND post_id IN (" + Strings.join(",", post_ids) + ")"));
            blog.putValue("authors", Array.array_map(new Callback(null, null), authors, Array.array_fill(0, Array.count(authors), gVars.current_user.getID())));
            this.save_vars();
        }

        directions = getIncluded(L10nPage.class, gVars, gConsts).__(
                    "All posts were imported with the current user as author. Use this form to move each Blogger user\'s posts to a different nWordPress user. You may <a href=\"users.php\">add users</a> and then return to this page and complete the user mapping. This form may be used as many times as you like until you activate the \"Restart\" function below.",
                    "default");
        heading = getIncluded(L10nPage.class, gVars, gConsts).__("Author mapping", "default");
        blogtitle = strval(blog.getValue("title")) + " (" + strval(blog.getValue("host")) + ")";
        mapthis = getIncluded(L10nPage.class, gVars, gConsts).__("Blogger username", "default");
        tothis = getIncluded(L10nPage.class, gVars, gConsts).__("nWordPress login", "default");
        submit = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save Changes", "default"));

        for (Map.Entry javaEntry37 : (Set<Map.Entry>) blog.getArrayValue("authors").entrySet()) {
            i = javaEntry37.getKey();
            author = (Array<Object>) javaEntry37.getValue();
            rows = rows + "<tr><td><label for=\'authors[" + strval(i) + "]\'>" + strval(author.getValue(0)) + "</label></td><td><select name=\'authors[" + strval(i) + "]\' id=\'authors[" + strval(i) +
                "]\'>" + this.get_user_options(author.getValue(1)) + "</select></td></tr>";
        }

        return "<div class=\'wrap\'><h2>" + heading + "</h2><h3>" + blogtitle + "</h3><p>" + directions +
        "</p><form action=\'index.php?import=blogger&noheader=1&saveauthors=1\' method=\'post\'><input type=\'hidden\' name=\'blog\' value=\'" + importing_blog +
        "\' /><table cellpadding=\'5\'><thead><td>" + mapthis + "</td><td>" + tothis + "</td></thead>" + rows +
        "<tr><td></td><td class=\'submit\'><input type=\'submit\' class=\'button authorsubmit\' value=\'" + submit + "\' /></td></tr></table></form></div>";
    }

    public String get_user_options(Object current) {
        String sel = null;
        StdClass user = null;
        String options = null;

        if (!isset(importer_users)) {
            importer_users = new Array<Object>(getIncluded(UserPage.class, gVars, gConsts).get_users_of_blog(intval("")));
        }

        for (Map.Entry javaEntry38 : importer_users.entrySet()) {
            user = (StdClass) javaEntry38.getValue();
            sel = (equal(StdClass.getValue(user, "user_id"), current)
                ? " selected=\'selected\'"
                : "");
            options = options + "<option value=\'" + StdClass.getValue(user, "user_id") + "\'" + sel + ">" + StdClass.getValue(user, "display_name") + "</option>";
        }

        return options;
    }

    public int save_authors() {
        Array<Object> authors = new Array<Object>();
        Object host = null;
        String post_ids = null;
        Array<Object> results = new Array<Object>();
        Array<Object> authors_posts = new Array<Object>();
        StdClass row = null;
        int user_id = 0;
        Object author = null;
        authors = new Array<Object>(gVars.webEnv._POST.getValue("authors"));
        host = this.blogs.getArrayValue(importing_blog).getValue("host");

		// Get an array of posts => authors
        Array post_idsArray = new Array<Object>(gVars.wpdb.get_col("SELECT post_id FROM " + gVars.wpdb.postmeta + " WHERE meta_key = \'blogger_blog\' AND meta_value = \'" + host + "\'"));
        post_ids = Strings.join(",", post_idsArray);
        results = new Array<Object>(gVars.wpdb.get_results("SELECT post_id, meta_value FROM " + gVars.wpdb.postmeta + " WHERE meta_key = \'blogger_author\' AND post_id IN (" + post_ids + ")"));

        for (Map.Entry javaEntry39 : results.entrySet()) {
            row = (StdClass) javaEntry39.getValue();
            authors_posts.putValue(StdClass.getValue(row, "post_id"), StdClass.getValue(row, "meta_value"));
        }

        for (Map.Entry javaEntry40 : authors.entrySet()) {
            author = javaEntry40.getKey();
            user_id = intval(javaEntry40.getValue());

            // user_id = intval(user_id);
            
			// Skip authors that haven't been changed
            if (equal(user_id, this.blogs.getArrayValue(importing_blog).getArrayValue("authors").getArrayValue(author).getValue(1))) {
                continue;
            }

			// Get a list of the selected author's posts
            post_idsArray = new Array<Object>(Array.array_keys(authors_posts, this.blogs.getArrayValue(importing_blog).getArrayValue("authors").getArrayValue(author).getValue(0)));
            post_ids = Strings.join(",", post_idsArray);
            
            gVars.wpdb.query("UPDATE " + gVars.wpdb.posts + " SET post_author = " + user_id + " WHERE id IN (" + post_ids + ")");
            this.blogs.getArrayValue(importing_blog).getArrayValue("authors").getArrayValue(author).putValue(1, user_id);
        }

        this.save_vars();
        getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("edit.php", 302);

        return 0;
    }

    public int _get_auth_sock() {
        int sock = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();

		// Connect to https://www.google.com
        if (!booleanval(sock = FileSystemOrSocket.fsockopen(gVars.webEnv, "ssl://www.google.com", 443, errno, errstr))) {
            this.uh_oh(
                getIncluded(L10nPage.class, gVars, gConsts).__("Could not connect to https://www.google.com", "default"),
                getIncluded(L10nPage.class, gVars, gConsts).__("There was a problem opening a secure connection to Google. This is what went wrong:", "default"),
                errstr + " (" + errno + ")");

            return INVALID_RESOURCE;
        }

        return sock;
    }

    public int _get_blogger_sock() {
        return _get_blogger_sock("www2.blogger.com");
    }

    public int _get_blogger_sock(String host) {
        int sock = 0;
        Ref<Integer> errno = new Ref<Integer>();
        Ref<String> errstr = new Ref<String>();

        if (!booleanval(sock = FileSystemOrSocket.fsockopen(gVars.webEnv, host, 80, errno, errstr))) {
            this.uh_oh(
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Could not connect to %s", "default"), host),
                getIncluded(L10nPage.class, gVars, gConsts).__("There was a problem opening a connection to Blogger. This is what went wrong:", "default"),
                errstr + " (" + errno + ")");

            return INVALID_RESOURCE;
        }

        return sock;
    }

    public String _txrx(int sock, String request) {
        String response = null;
        FileSystemOrSocket.fwrite(gVars.webEnv, sock, request);

        while (!FileSystemOrSocket.feof(gVars.webEnv, sock))
            response = response + FileSystemOrSocket.fread(gVars.webEnv, sock, 8192);

        FileSystemOrSocket.fclose(gVars.webEnv, sock);

        return response;
    }

    public boolean revoke(Object token) {
        Array<String> headers;
        String request = null;
        int sock = 0;
        headers = new Array<String>(new ArrayEntry<String>("GET /accounts/AuthSubRevokeToken HTTP/1.0"), new ArrayEntry<String>("Authorization: AuthSub token=\"" + strval(token) + "\""));
        request = Strings.join("\r\n", headers) + "\r\n\r\n";
        sock = this._get_auth_sock();

        if (!booleanval(sock)) {
            return false;
        }

        this._txrx(sock, request);

        return false;
    }

    public void restart() {
        Array<Object> options = new Array<Object>();
        options = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogger_importer");

        if (isset(options.getValue("token"))) {
            this.revoke(options.getValue("token"));
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("blogger_importer");
        gVars.wpdb.query("DELETE FROM " + gVars.wpdb.postmeta + " WHERE meta_key = \'blogger_author\'");
        getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect("?import=blogger", 302);
    }

	// Returns associative array of code, header, cookies, body. Based on code from php.net.
    public Array<Object> parse_response(String this_response) {
        Array<String> response_header_lines = new Array<String>();
        String http_response_line = null;
        Array<Object> matches = new Array<Object>();
        Object response_code = null;
        Array<Object> response_header_array = new Array<Object>();
        String header_line = null;
        Array<Object> cookie_array = new Array<Object>();
        Array<String> cookies = new Array<String>();
        Object this_cookie = null;
        
		// Split response into header and body sections
        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    parse_response_response_headers = srcArray.getValue(0);
                    parse_response_response_body = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(Strings.explode("\r\n\r\n", this_response, 2));
        response_header_lines = Strings.explode("\r\n", parse_response_response_headers);
        
		// First line of headers is the HTTP response code
        http_response_line = Array.array_shift(response_header_lines);

        if (QRegExPerl.preg_match("@^HTTP/[0-9]\\.[0-9] ([0-9]{3})@", http_response_line, matches)) {
            response_code = matches.getValue(1);
        }

		// put the rest of the headers in an array
        response_header_array = new Array<Object>();

        for (Map.Entry javaEntry41 : response_header_lines.entrySet()) {
            header_line = strval(javaEntry41.getValue());
            new ListAssigner<String>() {
                    public Array<String> doAssign(Array<String> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        parse_response_header = srcArray.getValue(0);
                        parse_response_value = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(Strings.explode(": ", header_line, 2));
            response_header_array.putValue(parse_response_header, strval(response_header_array.getValue(parse_response_header)) + strval(parse_response_value) + "\n");
        }

        cookie_array = new Array<Object>();
        cookies = Strings.explode("\n", strval(response_header_array.getValue("Set-Cookie")));

        for (Map.Entry javaEntry42 : cookies.entrySet()) {
            this_cookie = javaEntry42.getValue();
            Array.array_push(cookie_array, "Cookie: " + strval(this_cookie));
        }

        return new Array<Object>(
            new ArrayEntry<Object>("code", response_code),
            new ArrayEntry<Object>("header", response_header_array),
            new ArrayEntry<Object>("cookies", cookie_array),
            new ArrayEntry<Object>("body", parse_response_response_body));
    }

    /**
     * Step 9: Congratulate the user
     */
    public void congrats() {
        int blog = 0;
        int n = 0;
        blog = intval(gVars.webEnv._GET.getValue("blog"));
        echo(
                gVars.webEnv,
                "<h1>" + getIncluded(L10nPage.class, gVars, gConsts).__("Congratulations!", "default") + "</h1><p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Now that you have imported your Blogger blog into nWordPress, what are you going to do? Here are some suggestions:", "default") +
                "</p><ul><li>" + getIncluded(L10nPage.class, gVars, gConsts).__("That was hard work! Take a break.", "default") + "</li>");

        if (Array.count(this._import.getValue("blogs")) > 1) {
            echo(
                    gVars.webEnv,
                    "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("In case you haven\'t done it already, you can import the posts from your other blogs:", "default") +
                    strval(this.show_blogs()) + "</li>");
        }

        if (booleanval(n = Array.count(this._import.getArrayValue("blogs").getArrayValue(blog).getValue("newusers")))) {
            echo(
                    gVars.webEnv,
                    "<li>" +
                    QStrings.sprintf(
                            getIncluded(L10nPage.class, gVars, gConsts).__(
                                    "Go to <a href=\"%s\" target=\"%s\">Authors &amp; Users</a>, where you can modify the new user(s) or delete them. If you want to make all of the imported posts yours, you will be given that option when you delete the new authors.",
                                    "default"),
                            "users.php",
                            "_parent") + "</li>");
        }

        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("For security, click the link below to reset this importer.", "default") + "</li>");
        echo(gVars.webEnv, "</ul>");
    }

    /**
     * Figures out what to do, then does it.
     */
    public void start() {
        Object options = null;

        /* Do not change type */
        Object key = null;
        Object value = null;
        int blog = 0;
        Array<Object> keys = new Array<Object>();
        Object result;
        boolean saved = false;
        Object restart = null;
        Object message = null;
        Object submit = null;

        if (isset(gVars.webEnv._POST.getValue("restart"))) {
            this.restart();
        }

        options = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blogger_importer");

        if (is_array(options)) {
            for (Map.Entry javaEntry43 : ((Array<?>) options).entrySet()) {
                key = javaEntry43.getKey();
                value = javaEntry43.getValue();

                // Modified by Numiton
                if (equal(key, "token")) {
                    token = strval(value);
                } else if (equal(key, "title")) {
                    title = strval(value);
                } else if (equal(key, "blogs")) {
                    blogs = (Array<Object>) value;
                } else if (equal(key, "import")) {
                    _import = (Array<Object>) value;
                } else if (equal(key, "importing_blog")) {
                    importing_blog = value;
                } else if (equal(key, "importer_users")) {
                    importer_users = (Array<Object>) value;
                }
            }
        }

        if (isset(gVars.webEnv._REQUEST.getValue("blog"))) {
            blog = (is_array(gVars.webEnv._REQUEST.getValue("blog"))
                ? intval(Array.array_shift(keys = Array.array_keys(gVars.webEnv._REQUEST.getArrayValue("blog"))))
                : intval(gVars.webEnv._REQUEST.getValue("blog")));
            blog = blog;
            result = this.import_blog(blog);

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                echo(gVars.webEnv, ((WP_Error) result).get_error_message());
            }
        } else if (isset(gVars.webEnv._GET.getValue("token"))) {
            this.auth();
        } else if (booleanval(this.token) && this.token_is_valid()) {
            this.show_blogs();
        } else {
            this.greet();
        }

        saved = this.save_vars();

        if (saved && !isset(gVars.webEnv._GET.getValue("noheader"))) {
            restart = getIncluded(L10nPage.class, gVars, gConsts).__("Restart", "default");
            message = getIncluded(L10nPage.class, gVars, gConsts).__(
                        "We have saved some information about your Blogger account in your nWordPress database. Clearing this information will allow you to start over. Restarting will not affect any posts you have already imported. If you attempt to re-import a blog, duplicate posts and comments will be skipped.",
                        "default");
            submit = getIncluded(L10nPage.class, gVars, gConsts).__("Clear account information", "default");
            echo(
                    gVars.webEnv,
                    "<div class=\'wrap\'><h2>" + strval(restart) + "</h2><p>" + strval(message) +
                    "</p><form method=\'post\' action=\'?import=blogger&noheader=1\'><p class=\'submit\' style=\'text-align:left;\'><input type=\'submit\' class=\'button\' value=\'" + strval(submit) +
                    "\' name=\'restart\' /></p></form></div>");
        }
    }

    public boolean save_vars() {
        Array<Object> vars = new Array<Object>();
        vars = ClassHandling.get_object_vars(this);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("blogger_importer", vars);

        return !empty(vars);
    }

    public void admin_head() {
        echo(
                gVars.webEnv,
                "<style type=\"text/css\">\ntd { text-align: center; line-height: 2em;}\nthead td { font-weight: bold; }\n.bar {\n\twidth: 200px;\n\ttext-align: left;\n\tline-height: 2em;\n\tpadding: 0px;\n}\n.ind {\n\tposition: absolute;\n\tbackground-color: #83B4D8;\n\twidth: 1px;\n\tz-index: 9;\n}\n.stat {\n\tz-index: 10;\n\tposition: relative;\n\ttext-align: center;\n}\n</style>\n");
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
