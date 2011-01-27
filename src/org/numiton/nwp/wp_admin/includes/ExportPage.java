/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ExportPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.numiton.nwp.wp_includes.TaxonomyPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Network;
import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;
import com.numiton.xml.XMLParser;


@Controller(value = "wp_admin/includes/ExportPage")
@Scope("request")
public class ExportPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ExportPage.class.getName());
    public StdClass current_site = new StdClass();

    @Override
    @RequestMapping("/wp-admin/includes/export.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/export";
    }

    public Array<Object> wxr_missing_parents(Object categories /* Do not change type */) {
        Array<Object> parents = new Array<Object>();
        StdClass category = null;
        Object zero = null;

        if (!is_array(categories) || empty(categories)) {
            return new Array<Object>();
        }

        for (Map.Entry javaEntry137 : ((Array<?>) categories).entrySet()) {
            category = (StdClass) javaEntry137.getValue();
            parents.putValue(StdClass.getValue(category, "term_id"), StdClass.getValue(category, "parent"));
        }

        parents = Array.array_unique(Array.array_diff(parents, Array.array_keys(parents)));

        if (booleanval(zero = Array.array_search("0", parents))) {
            parents.arrayUnset(zero);
        }

        return parents;
    }

    public String wxr_cdata(String str) {
        if (equal(getIncluded(FormattingPage.class, gVars, gConsts).seems_utf8(str), false)) {
            str = XMLParser.utf8_encode(str);
        }

        str = "<![CDATA[" + str + (equal(Strings.substr(str, -1), "]")
            ? " "
            : "") + "]]>";
        
        // $str = ent2ncr(wp_specialchars($str));

        return str;
    }

    public String wxr_site_url() {
        // Modified by Numiton
    	
    	// mu: the base url
        if (isset(StdClass.getValue(current_site, "domain"))) {
            return "http://" + StdClass.getValue(current_site, "domain") + StdClass.getValue(current_site, "path");
        } 
        // wp: the blog url
        else {
            return getIncluded(FeedPage.class, gVars, gConsts).get_bloginfo_rss("url");
        }
    }

    public void wxr_cat_name(StdClass c) {
        if (empty(StdClass.getValue(c, "name"))) {
            return;
        }

        echo(gVars.webEnv, "<wp:cat_name>" + wxr_cdata(strval(StdClass.getValue(c, "name"))) + "</wp:cat_name>");
    }

    public void wxr_category_description(StdClass c) {
        if (empty(StdClass.getValue(c, "description"))) {
            return;
        }

        echo(gVars.webEnv, "<wp:category_description>" + wxr_cdata(strval(StdClass.getValue(c, "description"))) + "</wp:category_description>");
    }

    public void wxr_tag_name(StdClass t) {
        if (empty(StdClass.getValue(t, "name"))) {
            return;
        }

        echo(gVars.webEnv, "<wp:tag_name>" + wxr_cdata(strval(StdClass.getValue(t, "name"))) + "</wp:tag_name>");
    }

    public void wxr_tag_description(StdClass t) {
        if (empty(StdClass.getValue(t, "description"))) {
            return;
        }

        echo(gVars.webEnv, "<wp:tag_description>" + wxr_cdata(strval(StdClass.getValue(t, "description"))) + "</wp:tag_description>");
    }

    public void wxr_post_taxonomy() {
        Array<Object> categories = new Array<Object>();
        Object tags = null;
        Object the_list = null;
        String filter = null;
        Object cat_name = null;
        StdClass category = null;
        Object tag_name = null;
        StdClass tag = null;
        categories = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(false));
        tags = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_tags(0);
        the_list = "";
        filter = "rss";

        if (!empty(categories)) {
            for (Map.Entry javaEntry138 : new Array<Object>(categories).entrySet()) {
                category = (StdClass) javaEntry138.getValue();
                
                cat_name = getIncluded(TaxonomyPage.class, gVars, gConsts)
                               .sanitize_term_field("name", StdClass.getValue(category, "name"), intval(StdClass.getValue(category, "term_id")), "category", filter);
                
                // for backwards compatibility
                the_list = strval(the_list) + "\n\t\t<category><![CDATA[" + strval(cat_name) + "]]></category>\n";
                
                // forwards compatibility: use a unique identifier for each cat to avoid clashes
        		// http://trac.wordpress.org/ticket/5447
                the_list = strval(the_list) + "\n\t\t<category domain=\"category\" nicename=\"" + StdClass.getValue(category, "slug") + "\"><![CDATA[" + strval(cat_name) + "]]></category>\n";
            }
        }

        if (!empty(tags)) {
            for (Map.Entry javaEntry139 : new Array<Object>(tags).entrySet()) {
                tag = (StdClass) javaEntry139.getValue();
                
                tag_name = getIncluded(TaxonomyPage.class, gVars, gConsts).sanitize_term_field("name", StdClass.getValue(tag, "name"), intval(StdClass.getValue(tag, "term_id")), "post_tag", filter);
                the_list = strval(the_list) + "\n\t\t<category domain=\"tag\"><![CDATA[" + strval(tag_name) + "]]></category>\n";
                // forwards compatibility as above
                the_list = strval(the_list) + "\n\t\t<category domain=\"tag\" nicename=\"" + StdClass.getValue(tag, "slug") + "\"><![CDATA[" + strval(tag_name) + "]]></category>\n";
            }
        }

        echo(gVars.webEnv, the_list);
    }

    public void export_wp(Object author) {
        String filename = null;
        String where = null;
        int author_id = 0;
        Array<Object> categories = new Array<Object>();
        Array<Object> tags = new Array<Object>();
        Object found_parents; /* Do not change type */
        Array parents = new Array();
        int pass = 0;
        int passes = 0;
        StdClass cat = null;
        Array<Object> cats = new Array<Object>();
        StdClass c = null;
        StdClass t = null;
        Array next_posts = new Array();
        Array<StdClass> posts = null;
        Array<Object> postmeta = null;
        StdClass meta = null;
        Array<Object> comments = null;
        
        getIncluded(PluginPage.class, gVars, gConsts).do_action("export_wp", "");
        
        filename = "wordpress." + DateTime.date("Y-m-d") + ".xml";
        
        Network.header(gVars.webEnv, "Content-Description: File Transfer");
        Network.header(gVars.webEnv, "Content-Disposition: attachment; filename=" + filename);
        Network.header(gVars.webEnv, "Content-Type: text/xml; charset=" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"), true);
        
        where = "";

        if (booleanval(author) && !equal(author, "all")) {
            author_id = intval(author);
            where = " WHERE post_author = \'" + strval(author_id) + "\' ";
        }

        // grab a snapshot of post IDs, just in case it changes during the export
        gVars.post_ids = gVars.wpdb.get_col("SELECT ID FROM " + gVars.wpdb.posts + " " + where + " ORDER BY post_date_gmt ASC");
        
        categories = new Array<Object>(getIncluded(CategoryPage.class, gVars, gConsts).get_categories("get=all"));
        tags = new Array<Object>(getIncluded(CategoryPage.class, gVars, gConsts).get_tags("get=all"));

        while (booleanval(parents = wxr_missing_parents(categories))) {
            found_parents = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("include=" + Strings.join(", ", parents));

            if (is_array(found_parents) && booleanval(Array.count(found_parents))) {
                categories = Array.array_merge(categories, (Array) found_parents);
            } else {
                break;
            }
        }

        // Put them in order to be inserted with no child going before its parent
        pass = 0;
        passes = 1000 + Array.count(categories);

        while (booleanval(cat = (StdClass) Array.array_shift(categories)) && (++pass < passes)) {
            if (equal(StdClass.getValue(cat, "parent"), 0) || isset(cats.getValue(StdClass.getValue(cat, "parent")))) {
                cats.putValue(StdClass.getValue(cat, "term_id"), cat);
            } else {
                categories.putValue(cat);
            }
        }

        categories = null;
        echo(gVars.webEnv, "<?xml version=\"1.0\" encoding=\"" + getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("charset", "raw") + "\"?" + ">\n");
        echo(
                gVars.webEnv,
                "<!-- This is a WordPress eXtended RSS file generated by nWordPress as an export of your blog. -->\n<!-- It contains information about your blog\'s posts, comments, and categories. -->\n<!-- You may use this file to transfer that content from one site to another. -->\n<!-- This file is not intended to serve as a complete backup of your blog. -->\n\n<!-- To import this information into a nWordPress blog follow these steps. -->\n<!-- 1. Log into that blog as an administrator. -->\n<!-- 2. Go to Manage: Import in the blog\'s admin panels. -->\n<!-- 3. Choose \"nWordPress\" from the list. -->\n<!-- 4. Upload this file using the form provided on that page. -->\n<!-- 5. You will first be asked to map the authors in this export file to users -->\n<!--    on the blog.  For each author, you may choose to map to an -->\n<!--    existing user on the blog or to create a new user -->\n<!-- 6. nWordPress will then import each of the posts, comments, and categories -->\n<!--    contained in this file into your blog -->\n\n");
        getIncluded(General_templatePage.class, gVars, gConsts).the_generator("export");
        echo(
                gVars.webEnv,
                "<rss version=\"2.0\"\n\txmlns:content=\"http://purl.org/rss/1.0/modules/content/\"\n\txmlns:wfw=\"http://wellformedweb.org/CommentAPI/\"\n\txmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n\txmlns:wp=\"http://wordpress.org/export/");
        echo(gVars.webEnv, gConsts.getWXR_VERSION());
        echo(gVars.webEnv, "/\"\n>\n\n<channel>\n\t<title>");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("name");
        echo(gVars.webEnv, "</title>\n\t<link>");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");
        echo(gVars.webEnv, "</link>\n\t<description>");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("description");
        echo(gVars.webEnv, "</description>\n\t<pubDate>");

        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).mysql2date("D, d M Y H:i:s +0000", getIncluded(PostPage.class, gVars, gConsts).get_lastpostmodified("GMT"), false));
        echo(gVars.webEnv, "</pubDate>\n\t<generator>http://wordpress.org/?v=");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("version");
        echo(gVars.webEnv, "</generator>\n\t<language>");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("rss_language"));
        echo(gVars.webEnv, "</language>\n\t<wp:wxr_version>");
        echo(gVars.webEnv, gConsts.getWXR_VERSION());
        echo(gVars.webEnv, "</wp:wxr_version>\n\t<wp:base_site_url>");
        echo(gVars.webEnv, wxr_site_url());
        echo(gVars.webEnv, "</wp:base_site_url>\n\t<wp:base_blog_url>");
        getIncluded(FeedPage.class, gVars, gConsts).bloginfo_rss("url");
        echo(gVars.webEnv, "</wp:base_blog_url>\n");

        if (booleanval(cats)) {
            for (Map.Entry javaEntry140 : cats.entrySet()) {
                c = (StdClass) javaEntry140.getValue();
                echo(gVars.webEnv, "\t<wp:category><wp:category_nicename>");
                echo(gVars.webEnv, StdClass.getValue(c, "slug"));
                echo(gVars.webEnv, "</wp:category_nicename><wp:category_parent>");
                echo(gVars.webEnv, booleanval(StdClass.getValue(c, "parent"))
                    ? ((StdClass) cats.getValue(StdClass.getValue(c, "parent"))).fields.getValue("name")
                    : "");
                echo(gVars.webEnv, "</wp:category_parent>");
                wxr_cat_name(c);
                wxr_category_description(c);
                echo(gVars.webEnv, "</wp:category>\n");
            }
        } else {
        }

        if (booleanval(tags)) {
            for (Map.Entry javaEntry141 : tags.entrySet()) {
                t = (StdClass) javaEntry141.getValue();
                echo(gVars.webEnv, "\t<wp:tag><wp:tag_slug>");
                echo(gVars.webEnv, StdClass.getValue(t, "slug"));
                echo(gVars.webEnv, "</wp:tag_slug>");
                wxr_tag_name(t);
                wxr_tag_description(t);
                echo(gVars.webEnv, "</wp:tag>\n");
            }
        } else {
        }

        echo(gVars.webEnv, "\t");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("rss2_head", "");
        echo(gVars.webEnv, "\t");

        if (booleanval(gVars.post_ids)) {
            gVars.wp_query.in_the_loop = true;  // Fake being in the loop.
    		// fetch 20 posts at a time rather than loading the entire table into memory
            while (booleanval(next_posts = Array.array_splice(gVars.post_ids, 0, 20))) {
                where = "WHERE ID IN (" + Strings.join(",", next_posts) + ")";
                posts = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.posts + " " + where + " ORDER BY post_date_gmt ASC");

                for (Map.Entry javaEntry142 : posts.entrySet()) {
                    gVars.post = (StdClass) javaEntry142.getValue();
                    getIncluded(QueryPage.class, gVars, gConsts).setup_postdata(gVars.post);
                    echo(gVars.webEnv, "<item>\n<title>");
                    echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title_rss", StdClass.getValue(gVars.post, "post_title")));
                    echo(gVars.webEnv, "</title>\n<link>");
                    getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
                    echo(gVars.webEnv, "</link>\n<pubDate>");

                    echo(
                        gVars.webEnv,
                        getIncluded(FunctionsPage.class, gVars, gConsts)
                            .mysql2date("D, d M Y H:i:s +0000", getIncluded(General_templatePage.class, gVars, gConsts).get_post_time("Y-m-d H:i:s", true), false));
                    echo(gVars.webEnv, "</pubDate>\n<dc:creator>");
                    echo(gVars.webEnv, wxr_cdata(getIncluded(Author_templatePage.class, gVars, gConsts).get_the_author("")));
                    echo(gVars.webEnv, "</dc:creator>\n");
                    wxr_post_taxonomy();
                    echo(gVars.webEnv, "\n<guid isPermaLink=\"false\">");
                    getIncluded(Post_templatePage.class, gVars, gConsts).the_guid(0);
                    echo(gVars.webEnv, "</guid>\n<description></description>\n<content:encoded>");
                    echo(gVars.webEnv, wxr_cdata(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_content_export", StdClass.getValue(gVars.post, "post_content")))));
                    echo(gVars.webEnv, "</content:encoded>\n<wp:post_id>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "ID"));
                    echo(gVars.webEnv, "</wp:post_id>\n<wp:post_date>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_date"));
                    echo(gVars.webEnv, "</wp:post_date>\n<wp:post_date_gmt>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_date_gmt"));
                    echo(gVars.webEnv, "</wp:post_date_gmt>\n<wp:comment_status>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "comment_status"));
                    echo(gVars.webEnv, "</wp:comment_status>\n<wp:ping_status>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "ping_status"));
                    echo(gVars.webEnv, "</wp:ping_status>\n<wp:post_name>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_name"));
                    echo(gVars.webEnv, "</wp:post_name>\n<wp:status>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_status"));
                    echo(gVars.webEnv, "</wp:status>\n<wp:post_parent>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_parent"));
                    echo(gVars.webEnv, "</wp:post_parent>\n<wp:menu_order>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "menu_order"));
                    echo(gVars.webEnv, "</wp:menu_order>\n<wp:post_type>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_type"));
                    echo(gVars.webEnv, "</wp:post_type>\n<wp:post_password>");
                    echo(gVars.webEnv, StdClass.getValue(gVars.post, "post_password"));
                    echo(gVars.webEnv, "</wp:post_password>\n");

                    if (equal(StdClass.getValue(gVars.post, "post_type"), "attachment")) {
                        echo(gVars.webEnv, "<wp:attachment_url>");
                        echo(gVars.webEnv, getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(intval(StdClass.getValue(gVars.post, "ID"))));
                        echo(gVars.webEnv, "</wp:attachment_url>\n");
                    }

                    postmeta = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.postmeta + " WHERE post_id = " + StdClass.getValue(gVars.post, "ID"));

                    if (booleanval(postmeta)) {
                        for (Map.Entry javaEntry143 : postmeta.entrySet()) {
                            meta = (StdClass) javaEntry143.getValue();
                            echo(gVars.webEnv, "<wp:postmeta>\n<wp:meta_key>");
                            echo(gVars.webEnv, StdClass.getValue(meta, "meta_key"));
                            echo(gVars.webEnv, "</wp:meta_key>\n<wp:meta_value>");
                            echo(gVars.webEnv, StdClass.getValue(meta, "meta_value"));
                            echo(gVars.webEnv, "</wp:meta_value>\n</wp:postmeta>\n");
                        }
                    }

                    comments = gVars.wpdb.get_results("SELECT * FROM " + gVars.wpdb.comments + " WHERE comment_post_ID = " + StdClass.getValue(gVars.post, "ID"));

                    if (booleanval(comments)) {
                        for (Map.Entry javaEntry144 : comments.entrySet()) {
                            c = (StdClass) javaEntry144.getValue();
                            echo(gVars.webEnv, "<wp:comment>\n<wp:comment_id>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_ID"));
                            echo(gVars.webEnv, "</wp:comment_id>\n<wp:comment_author>");
                            echo(gVars.webEnv, wxr_cdata(strval(StdClass.getValue(c, "comment_author"))));
                            echo(gVars.webEnv, "</wp:comment_author>\n<wp:comment_author_email>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_author_email"));
                            echo(gVars.webEnv, "</wp:comment_author_email>\n<wp:comment_author_url>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_author_url"));
                            echo(gVars.webEnv, "</wp:comment_author_url>\n<wp:comment_author_IP>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_author_IP"));
                            echo(gVars.webEnv, "</wp:comment_author_IP>\n<wp:comment_date>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_date"));
                            echo(gVars.webEnv, "</wp:comment_date>\n<wp:comment_date_gmt>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_date_gmt"));
                            echo(gVars.webEnv, "</wp:comment_date_gmt>\n<wp:comment_content>");
                            echo(gVars.webEnv, wxr_cdata(strval(StdClass.getValue(c, "comment_content"))));
                            echo(gVars.webEnv, "</wp:comment_content>\n<wp:comment_approved>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_approved"));
                            echo(gVars.webEnv, "</wp:comment_approved>\n<wp:comment_type>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_type"));
                            echo(gVars.webEnv, "</wp:comment_type>\n<wp:comment_parent>");
                            echo(gVars.webEnv, StdClass.getValue(c, "comment_parent"));
                            echo(gVars.webEnv, "</wp:comment_parent>\n<wp:comment_user_id>");
                            echo(gVars.webEnv, StdClass.getValue(c, "user_id"));
                            echo(gVars.webEnv, "</wp:comment_user_id>\n</wp:comment>\n");
                        }
                    }

                    echo(gVars.webEnv, "\t</item>\n");
                }
            }
        }

        echo(gVars.webEnv, "</channel>\n</rss>\n");
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_export_block1");
        gVars.webEnv = webEnv;
        
        // version number for the export format.  bump this when something changes that might affect compatibility.
        gConsts.setWXR_VERSION("1.0");

        return DEFAULT_VAL;
    }
}
