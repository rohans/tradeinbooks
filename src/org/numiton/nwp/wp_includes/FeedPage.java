/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: FeedPage.java,v 1.5 2008/10/14 13:15:48 numiton Exp $
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
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPosix;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


@Controller
@Scope("request")
public class FeedPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(FeedPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/feed.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/feed";
    }

    public String get_bloginfo_rss(String show) {
        String info = null;
        info = Strings.strip_tags(getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo(show, "raw"));

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_bloginfo_rss", getIncluded(FormattingPage.class, gVars, gConsts).convert_chars(info, ""), show));
    }

    public void bloginfo_rss(String show) {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("bloginfo_rss", get_bloginfo_rss(show), show));
    }

    public String get_default_feed() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("default_feed", "rss2"));
    }

    public String get_wp_title_rss() {
        return get_wp_title_rss("&#187;");
    }

    public String get_wp_title_rss(String sep) {
        Object title = null;
        title = getIncluded(General_templatePage.class, gVars, gConsts).wp_title(sep, false, "");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(title)) {
            return ((WP_Error) title).get_error_message();
        }

        title = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_wp_title_rss", title);

        return strval(title);
    }

    public void wp_title_rss(String sep) {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_title_rss", get_wp_title_rss(sep)));
    }

    public Object get_the_title_rss() {
        Object title = null;
        title = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_title(0);
        title = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title_rss", title);

        return title;
    }

    public void the_title_rss() {
        echo(gVars.webEnv, get_the_title_rss());
    }

    public void the_content_rss(String more_link_text, int stripteaser, String more_file, int cut, int encode_html) {
        String content = null;
        Array<String> blah = new Array<String>();
        int k = 0;
        int use_dotdotdot = 0;
        String excerpt = null;
        int i = 0;
        content = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_content(more_link_text, stripteaser, more_file);
        content = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_content_rss", content));

        if (booleanval(cut) && !booleanval(encode_html)) {
            encode_html = 2;
        }

        if (equal(1, encode_html)) {
            content = getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(content, strval(0));
            cut = 0;
        } else if (equal(0, encode_html)) {
            content = getIncluded(FunctionsPage.class, gVars, gConsts).make_url_footnote(content);
        } else if (equal(2, encode_html)) {
            content = Strings.strip_tags(content);
        }

        if (booleanval(cut)) {
            blah = Strings.explode(" ", content);

            if (Array.count(blah) > cut) {
                k = cut;
                use_dotdotdot = 1;
            } else {
                k = Array.count(blah);
                use_dotdotdot = 0;
            }

            for (i = 0; i < k; i++)
                excerpt = excerpt + blah.getValue(i) + " ";

            excerpt = excerpt + (booleanval(use_dotdotdot)
                ? "..."
                : "");
            content = excerpt;
        }

        content = Strings.str_replace("]]>", "]]&gt;", content);
        echo(gVars.webEnv, content);
    }

    public void the_excerpt_rss() {
        String output = null;
        output = getIncluded(Post_templatePage.class, gVars, gConsts).get_the_excerpt("");
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_excerpt_rss", output));
    }

    public void the_permalink_rss() {
        echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_permalink_rss", getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(0, false)));
    }

    public void comment_guid() {
        echo(gVars.webEnv, get_comment_guid());
    }

    public String get_comment_guid() {
        if (!is_object(gVars.comment)) {
            return strval(false);
        }

        return getIncluded(Post_templatePage.class, gVars, gConsts).get_the_guid(intval(StdClass.getValue(gVars.comment, "comment_post_ID"))) + "#comment-" +
        intval(StdClass.getValue(gVars.comment, "comment_ID"));
    }

    public void comment_link() {
        echo(gVars.webEnv, getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_link());
    }

    public Object get_comment_author_rss() {
        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_author_rss", getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_author());
    }

    public void comment_author_rss() {
        echo(gVars.webEnv, get_comment_author_rss());
    }

    public void comment_text_rss() {
        Object comment_text = null;
        comment_text = getIncluded(Comment_templatePage.class, gVars, gConsts).get_comment_text();
        comment_text = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_text_rss", comment_text);
        echo(gVars.webEnv, comment_text);
    }

    public Object get_the_category_rss(String type) {
        Array<Object> categories = new Array<Object>();
        Object tags = null;
        String the_list = null;
        Array<Object> cat_names = new Array<Object>();
        String filter = null;
        StdClass category = null;
        StdClass tag = null;
        String cat_name = null;
        categories = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(false));
        tags = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_tags(0);
        the_list = "";
        cat_names = new Array<Object>();
        filter = "rss";

        if (equal("atom", type)) {
            filter = "raw";
        }

        if (!empty(categories)) {
            for (Map.Entry javaEntry459 : new Array<Object>(categories).entrySet()) {
                category = (StdClass) javaEntry459.getValue();
                cat_names.putValue(
                    getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("name", StdClass.getValue(category, "name"), intval(StdClass.getValue(category, "term_id")), "category", filter));
            }
        }

        if (!empty(tags)) {
            for (Map.Entry javaEntry460 : new Array<Object>(tags).entrySet()) {
                tag = (StdClass) javaEntry460.getValue();
                cat_names.putValue(
                    getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("name", StdClass.getValue(tag, "name"), intval(StdClass.getValue(tag, "term_id")), "post_tag", filter));
            }
        }

        cat_names = Array.array_unique(cat_names);

        for (Map.Entry javaEntry461 : cat_names.entrySet()) {
            cat_name = strval(javaEntry461.getValue());

            if (equal("rdf", type)) {
                the_list = the_list + "\n\t\t<dc:subject><![CDATA[" + cat_name + "]]></dc:subject>\n";
            } else if (equal("atom", type)) {
                the_list = the_list +
                    QStrings.sprintf(
                        "<category scheme=\"%1$s\" term=\"%2$s\" />",
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                            strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_bloginfo_rss", getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("url", "raw")))),
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(cat_name));
            } else {
                the_list = the_list + "\n\t\t<category><![CDATA[" + cat_name + "]]></category>\n";
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_category_rss", the_list, type);
    }

    public void the_category_rss(String type) {
        echo(gVars.webEnv, get_the_category_rss(type));
    }

    public void html_type_rss() {
        String type = null;
        type = getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("html_type", "raw");

        if (!strictEqual(Strings.strpos(type, "xhtml"), BOOLEAN_FALSE)) {
            type = "xhtml";
        } else {
            type = "html";
        }

        echo(gVars.webEnv, type);
    }

    public void rss_enclosure() {
        Object key = null;
        Array<String> enclosure = new Array<String>();
        String enc = null;
        Object val = null;

        if (!empty(StdClass.getValue(gVars.post, "post_password")) &&
                (!isset(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH())) ||
                !equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(gVars.post, "post_password")))) {
            return;
        }

        for (Map.Entry javaEntry462 : getIncluded(PostPage.class, gVars, gConsts).get_post_custom(0).entrySet()) {
            key = javaEntry462.getKey();
            val = javaEntry462.getValue();

            if (equal(key, "enclosure")) {
                for (Map.Entry javaEntry463 : new Array<Object>(val).entrySet()) {
                    enc = strval(javaEntry463.getValue());
                    enclosure = QRegExPosix.split("\n", enc);
                    echo(
                            gVars.webEnv,
                            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                                    "rss_enclosure",
                                    "<enclosure url=\"" + Strings.trim(Strings.htmlspecialchars(enclosure.getValue(0))) + "\" length=\"" + Strings.trim(enclosure.getValue(1)) + "\" type=\"" +
                                    Strings.trim(enclosure.getValue(2)) + "\" />" + "\n"));
                }
            }
        }
    }

    public void atom_enclosure() {
        Object key = null;
        Array<String> enclosure = new Array<String>();
        String enc = null;
        Object val = null;

        if (!empty(StdClass.getValue(gVars.post, "post_password")) && !equal(gVars.webEnv._COOKIE.getValue("wp-postpass_" + gConsts.getCOOKIEHASH()), StdClass.getValue(gVars.post, "post_password"))) {
            return;
        }

        for (Map.Entry javaEntry464 : getIncluded(PostPage.class, gVars, gConsts).get_post_custom(0).entrySet()) {
            key = javaEntry464.getKey();
            val = javaEntry464.getValue();

            if (equal(key, "enclosure")) {
                for (Map.Entry javaEntry465 : new Array<Object>(val).entrySet()) {
                    enc = strval(javaEntry465.getValue());
                    enclosure = QRegExPosix.split("\n", enc);
                    echo(
                            gVars.webEnv,
                            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                                    "atom_enclosure",
                                    "<link href=\"" + Strings.trim(Strings.htmlspecialchars(enclosure.getValue(0))) + "\" rel=\"enclosure\" length=\"" + Strings.trim(enclosure.getValue(1)) +
                                    "\" type=\"" + Strings.trim(enclosure.getValue(2)) + "\" />" + "\n"));
                }
            }
        }
    }

    /**
     * prep_atom_text_construct() - Determine the type of a given string of data
     *
     * Tell whether the type is text, html, or xhtml, per RFC 4287 section 3.1.
     *
     * In the case of WordPress, text is defined as containing no markup,
     * xhtml is defined as "well formed", and html as tag soup (i.e., the rest).
     *
     * Container div tags are added to xhtml values, per section 3.1.1.3.
     *
     * @link http://www.atomenabled.org/developers/syndication/atom-format-spec.php#rfc.section.3.1
     *
     * @package WordPress
     * @subpackage Feed
     * @since 2.5
     *
     * @param string $data input string
     * @return array $result array(type, value)
     */
    public Array<Object> prep_atom_text_construct(String data) {
        int parser = 0;
        int code = 0;

        if (strictEqual(Strings.strpos(data, "<"), BOOLEAN_FALSE) && strictEqual(Strings.strpos(data, "&"), BOOLEAN_FALSE)) {
            return new Array<Object>(new ArrayEntry<Object>("text"), new ArrayEntry<Object>(data));
        }

        parser = XMLParser.xml_parser_create(gVars.webEnv);
        XMLParser.xml_parse(gVars.webEnv, parser, "<div>" + data + "</div>", true);
        code = XMLParser.xml_get_error_code(gVars.webEnv, parser);
        XMLParser.xml_parser_free(gVars.webEnv, parser);

        if (!booleanval(code)) {
            if (strictEqual(Strings.strpos(data, "<"), BOOLEAN_FALSE)) {
                return new Array<Object>(new ArrayEntry<Object>("text"), new ArrayEntry<Object>(data));
            } else {
                data = "<div xmlns=\'http://www.w3.org/1999/xhtml\'>" + data + "</div>";

                return new Array<Object>(new ArrayEntry<Object>("xhtml"), new ArrayEntry<Object>(data));
            }
        }

        if (equal(Strings.strpos(data, "]]>"), false)) {
            return new Array<Object>(new ArrayEntry<Object>("html"), new ArrayEntry<Object>("<![CDATA[" + data + "]]>"));
        } else {
            return new Array<Object>(new ArrayEntry<Object>("html"), new ArrayEntry<Object>(Strings.htmlspecialchars(data)));
        }
    }

    /**
     * self_link() - Generate a correct link for the atom:self elemet
     * Echo the link for the currently displayed feed in a XSS safe way.
     *
     * @subpackage Feed
     * @since 2.5
     */
    public void self_link() {
        echo(gVars.webEnv,
            "http" + (equal(gVars.webEnv._SERVER.getValue("https"), "on")
            ? "s"
            : "") + "://" + gVars.webEnv.getHttpHost() +
            getIncluded(FormattingPage.class, gVars, gConsts).wp_specialchars(Strings.stripslashes(gVars.webEnv, gVars.webEnv.getRequestURI()), strval(1)));
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
