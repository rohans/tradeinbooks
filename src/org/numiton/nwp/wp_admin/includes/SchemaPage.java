/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: SchemaPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class SchemaPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(SchemaPage.class.getName());
    public Object charset_collate;

    @Override
    @RequestMapping("/wp-admin/includes/schema.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/schema";
    }

    public void populate_options() {
        String schema = null;
        String guessurl;
        Array<Object> unusedoptions = new Array<Object>();
        Object option = null;
        Array<Object> fatoptions = new Array<Object>();
        Object fatoption = null;
        schema = ((isset(gVars.webEnv.getHttps()) && equal(Strings.strtolower(gVars.webEnv.getHttps()), "on"))
            ? "https://"
            : "http://");
        guessurl = QRegExPerl.preg_replace("|/wp-admin/.*|i", "", schema + gVars.webEnv.getHttpHost() + gVars.webEnv.getRequestURI());
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("siteurl", guessurl, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("blogname", getIncluded(L10nPage.class, gVars, gConsts).__("My Blog", "default"), "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("blogdescription", getIncluded(L10nPage.class, gVars, gConsts).__("Just another nWordPress weblog", "default"), "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("users_can_register", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("admin_email", "you@example.com", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("start_of_week", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("use_balanceTags", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("use_smilies", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("require_name_email", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("comments_notify", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("posts_per_rss", 10, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("rss_excerpt_length", 50, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("rss_use_excerpt", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("mailserver_url", "mail.example.com", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("mailserver_login", "login@example.com", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("mailserver_pass", "password", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("mailserver_port", 110, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_category", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_comment_status", "open", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_ping_status", "open", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_pingback_flag", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_post_edit_rows", 10, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("posts_per_page", 10, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("what_to_show", "posts", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("date_format", getIncluded(L10nPage.class, gVars, gConsts).__("F j, Y", "default"), "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("time_format", getIncluded(L10nPage.class, gVars, gConsts).__("g:i a", "default"), "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("links_updated_date_format", getIncluded(L10nPage.class, gVars, gConsts).__("F j, Y g:i a", "default"), "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("links_recently_updated_prepend", "<em>", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("links_recently_updated_append", "</em>", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("links_recently_updated_time", 120, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("comment_moderation", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("moderation_notify", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("permalink_structure", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("gzipcompression", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("hack_file", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("blog_charset", "UTF-8", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("moderation_keys", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("active_plugins", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("home", guessurl, "", "yes");

    	// in case it is set, but blank, update "home"
        if (!booleanval(getIncluded(UpgradePage.class, gVars, gConsts).__get_option("home"))) {
            getIncluded(FunctionsPage.class, gVars, gConsts).update_option("home", guessurl);
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("category_base", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("ping_sites", "http://rpc.pingomatic.com/", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("advanced_edit", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("comment_max_links", 2, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("gmt_offset", floatval(DateTime.date("Z")) / floatval(3600), "", "yes");
        // 1.5
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_email_category", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("recently_edited", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("use_linksupdate", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("template", "default", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("stylesheet", "default", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("comment_whitelist", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("page_uris", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("blacklist_keys", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("comment_registration", 0, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("rss_language", "en", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("html_type", "text/html", "", "yes");
        // 1.5.1
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("use_trackback", 0, "", "yes");
        // 2.0
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_role", "subscriber", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("db_version", gVars.wp_db_version, "", "yes");

        // 2.0.1
        if (booleanval(Options.ini_get(gVars.webEnv, "safe_mode"))) {
        	// Safe mode screws up mkdir(), so we must use a flat structure.
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("uploads_use_yearmonth_folders", 0, "", "yes");
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("upload_path", "wp-content", "", "yes");
        } else {
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("uploads_use_yearmonth_folders", 1, "", "yes");
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("upload_path", "wp-content/uploads", "", "yes");
        }

        // 2.0.3
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("secret", getIncluded(PluggablePage.class, gVars, gConsts).wp_generate_password(64), "", "yes");
        
        // 2.1
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("blog_public", "1", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("default_link_category", 2, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("show_on_front", "posts", "", "yes");
        
        // 2.2
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("tag_base", "", "", "yes");
        
        // 2.5
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("show_avatars", "1", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("avatar_rating", "G", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("upload_url_path", "", "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("thumbnail_size_w", 150, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("thumbnail_size_h", 150, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("thumbnail_crop", 1, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("medium_size_w", 300, "", "yes");
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("medium_size_h", 300, "", "yes");
        
        // Delete unused options
        unusedoptions = new Array<Object>(
                new ArrayEntry<Object>("blodotgsping_url"),
                new ArrayEntry<Object>("bodyterminator"),
                new ArrayEntry<Object>("emailtestonly"),
                new ArrayEntry<Object>("phoneemail_separator"),
                new ArrayEntry<Object>("smilies_directory"),
                new ArrayEntry<Object>("subjectprefix"),
                new ArrayEntry<Object>("use_bbcode"),
                new ArrayEntry<Object>("use_blodotgsping"),
                new ArrayEntry<Object>("use_phoneemail"),
                new ArrayEntry<Object>("use_quicktags"),
                new ArrayEntry<Object>("use_weblogsping"),
                new ArrayEntry<Object>("weblogs_cache_file"),
                new ArrayEntry<Object>("use_preview"),
                new ArrayEntry<Object>("use_htmltrans"),
                new ArrayEntry<Object>("smilies_directory"),
                new ArrayEntry<Object>("fileupload_allowedusers"),
                new ArrayEntry<Object>("use_phoneemail"),
                new ArrayEntry<Object>("default_post_status"),
                new ArrayEntry<Object>("default_post_category"),
                new ArrayEntry<Object>("archive_mode"),
                new ArrayEntry<Object>("time_difference"),
                new ArrayEntry<Object>("links_minadminlevel"),
                new ArrayEntry<Object>("links_use_adminlevels"),
                new ArrayEntry<Object>("links_rating_type"),
                new ArrayEntry<Object>("links_rating_char"),
                new ArrayEntry<Object>("links_rating_ignore_zero"),
                new ArrayEntry<Object>("links_rating_single_image"),
                new ArrayEntry<Object>("links_rating_image0"),
                new ArrayEntry<Object>("links_rating_image1"),
                new ArrayEntry<Object>("links_rating_image2"),
                new ArrayEntry<Object>("links_rating_image3"),
                new ArrayEntry<Object>("links_rating_image4"),
                new ArrayEntry<Object>("links_rating_image5"),
                new ArrayEntry<Object>("links_rating_image6"),
                new ArrayEntry<Object>("links_rating_image7"),
                new ArrayEntry<Object>("links_rating_image8"),
                new ArrayEntry<Object>("links_rating_image9"),
                new ArrayEntry<Object>("weblogs_cacheminutes"),
                new ArrayEntry<Object>("comment_allowed_tags"),
                new ArrayEntry<Object>("search_engine_friendly_urls"),
                new ArrayEntry<Object>("default_geourl_lat"),
                new ArrayEntry<Object>("default_geourl_lon"),
                new ArrayEntry<Object>("use_default_geourl"),
                new ArrayEntry<Object>("weblogs_xml_url"),
                new ArrayEntry<Object>("new_users_can_blog"),
                new ArrayEntry<Object>("_wpnonce"),
                new ArrayEntry<Object>("_wp_http_referer"),
                new ArrayEntry<Object>("Update"),
                new ArrayEntry<Object>("action"),
                new ArrayEntry<Object>("rich_editing"),
                new ArrayEntry<Object>("autosave_interval"));

        for (Map.Entry javaEntry191 : unusedoptions.entrySet()) {
            option = javaEntry191.getValue();
            getIncluded(FunctionsPage.class, gVars, gConsts).delete_option(strval(option));
        }

        // Set up a few options not to load by default
        fatoptions = new Array<Object>(new ArrayEntry<Object>("moderation_keys"), new ArrayEntry<Object>("recently_edited"), new ArrayEntry<Object>("blacklist_keys"));

        for (Map.Entry javaEntry192 : fatoptions.entrySet()) {
            fatoption = javaEntry192.getValue();
            gVars.wpdb.query("UPDATE " + gVars.wpdb.options + " SET `autoload` = \'no\' WHERE option_name = \'" + fatoption + "\'");
        }
    }

    public void populate_roles() {
        populate_roles_160();
        populate_roles_210();
        populate_roles_230();
        populate_roles_250();
    }

    public void populate_roles_160() {
        WP_Role role = null;
        
        // Add roles

    	// Dummy gettext calls to get strings in the catalog.
        getIncluded(L10nPage.class, gVars, gConsts)._c("Administrator|User role", "default");
        getIncluded(L10nPage.class, gVars, gConsts)._c("Editor|User role", "default");
        getIncluded(L10nPage.class, gVars, gConsts)._c("Author|User role", "default");
        getIncluded(L10nPage.class, gVars, gConsts)._c("Contributor|User role", "default");
        getIncluded(L10nPage.class, gVars, gConsts)._c("Subscriber|User role", "default");
        
        getIncluded(CapabilitiesPage.class, gVars, gConsts).add_role("administrator", "Administrator|User role", new Array<Object>());
        getIncluded(CapabilitiesPage.class, gVars, gConsts).add_role("editor", "Editor|User role", new Array<Object>());
        getIncluded(CapabilitiesPage.class, gVars, gConsts).add_role("author", "Author|User role", new Array<Object>());
        getIncluded(CapabilitiesPage.class, gVars, gConsts).add_role("contributor", "Contributor|User role", new Array<Object>());
        getIncluded(CapabilitiesPage.class, gVars, gConsts).add_role("subscriber", "Subscriber|User role", new Array<Object>());
        
        // Add caps for Administrator role
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("administrator");
        role.add_cap("switch_themes");
        role.add_cap("edit_themes");
        role.add_cap("activate_plugins");
        role.add_cap("edit_plugins");
        role.add_cap("edit_users");
        role.add_cap("edit_files");
        role.add_cap("manage_options");
        role.add_cap("moderate_comments");
        role.add_cap("manage_categories");
        role.add_cap("manage_links");
        role.add_cap("upload_files");
        role.add_cap("import");
        role.add_cap("unfiltered_html");
        role.add_cap("edit_posts");
        role.add_cap("edit_others_posts");
        role.add_cap("edit_published_posts");
        role.add_cap("publish_posts");
        role.add_cap("edit_pages");
        role.add_cap("read");
        role.add_cap("level_10");
        role.add_cap("level_9");
        role.add_cap("level_8");
        role.add_cap("level_7");
        role.add_cap("level_6");
        role.add_cap("level_5");
        role.add_cap("level_4");
        role.add_cap("level_3");
        role.add_cap("level_2");
        role.add_cap("level_1");
        role.add_cap("level_0");
        
        // Add caps for Editor role
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("editor");
        role.add_cap("moderate_comments");
        role.add_cap("manage_categories");
        role.add_cap("manage_links");
        role.add_cap("upload_files");
        role.add_cap("unfiltered_html");
        role.add_cap("edit_posts");
        role.add_cap("edit_others_posts");
        role.add_cap("edit_published_posts");
        role.add_cap("publish_posts");
        role.add_cap("edit_pages");
        role.add_cap("read");
        role.add_cap("level_7");
        role.add_cap("level_6");
        role.add_cap("level_5");
        role.add_cap("level_4");
        role.add_cap("level_3");
        role.add_cap("level_2");
        role.add_cap("level_1");
        role.add_cap("level_0");
        
        // Add caps for Author role
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("author");
        role.add_cap("upload_files");
        role.add_cap("edit_posts");
        role.add_cap("edit_published_posts");
        role.add_cap("publish_posts");
        role.add_cap("read");
        role.add_cap("level_2");
        role.add_cap("level_1");
        role.add_cap("level_0");
        
        // Add caps for Contributor role
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("contributor");
        role.add_cap("edit_posts");
        role.add_cap("read");
        role.add_cap("level_1");
        role.add_cap("level_0");
        
        // Add caps for Subscriber role
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("subscriber");
        role.add_cap("read");
        role.add_cap("level_0");
    }

    public void populate_roles_210() {
        Array<Object> roles = new Array<Object>();
        WP_Role role = null;
        roles = new Array<Object>(new ArrayEntry<Object>("administrator"), new ArrayEntry<Object>("editor"));

        for (Map.Entry javaEntry193 : roles.entrySet()) {
            String roleStr = strval(javaEntry193.getValue());
            role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role(roleStr);

            if (empty(role)) {
                continue;
            }

            role.add_cap("edit_others_pages");
            role.add_cap("edit_published_pages");
            role.add_cap("publish_pages");
            role.add_cap("delete_pages");
            role.add_cap("delete_others_pages");
            role.add_cap("delete_published_pages");
            role.add_cap("delete_posts");
            role.add_cap("delete_others_posts");
            role.add_cap("delete_published_posts");
            role.add_cap("delete_private_posts");
            role.add_cap("edit_private_posts");
            role.add_cap("read_private_posts");
            role.add_cap("delete_private_pages");
            role.add_cap("edit_private_pages");
            role.add_cap("read_private_pages");
        }

        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("administrator");

        if (!empty(role)) {
            role.add_cap("delete_users");
            role.add_cap("create_users");
        }

        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("author");

        if (!empty(role)) {
            role.add_cap("delete_posts");
            role.add_cap("delete_published_posts");
        }

        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("contributor");

        if (!empty(role)) {
            role.add_cap("delete_posts");
        }
    }

    public void populate_roles_230() {
        WP_Role role = null;
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("administrator");

        if (!empty(role)) {
            role.add_cap("unfiltered_upload");
        }
    }

    public void populate_roles_250() {
        WP_Role role = null;
        role = getIncluded(CapabilitiesPage.class, gVars, gConsts).get_role("administrator");

        if (!empty(role)) {
            role.add_cap("edit_dashboard");
        }
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_schema_block1");
        gVars.webEnv = webEnv;
        
        // Here we keep the DB structure and option values
        charset_collate = "";

        if (gVars.wpdb.supports_collation()) {
            if (!empty(gVars.wpdb.charset)) {
                charset_collate = "DEFAULT CHARACTER SET " + gVars.wpdb.charset;
            }

            if (!empty(gVars.wpdb.collate)) {
                charset_collate = charset_collate + " COLLATE " + gVars.wpdb.collate;
            }
        }

        gVars.wp_queries = "CREATE TABLE " + gVars.wpdb.terms +
            " (\n term_id bigint(20) NOT NULL auto_increment,\n name varchar(55) NOT NULL default \'\',\n slug varchar(200) NOT NULL default \'\',\n term_group bigint(10) NOT NULL default 0,\n PRIMARY KEY  (term_id),\n UNIQUE KEY slug (slug)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.term_taxonomy +
            " (\n term_taxonomy_id bigint(20) NOT NULL auto_increment,\n term_id bigint(20) NOT NULL default 0,\n taxonomy varchar(32) NOT NULL default \'\',\n description longtext NOT NULL,\n parent bigint(20) NOT NULL default 0,\n count bigint(20) NOT NULL default 0,\n PRIMARY KEY  (term_taxonomy_id),\n UNIQUE KEY term_id_taxonomy (term_id,taxonomy)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.term_relationships +
            " (\n object_id bigint(20) NOT NULL default 0,\n term_taxonomy_id bigint(20) NOT NULL default 0,\n term_order int(11) NOT NULL default 0,\n PRIMARY KEY  (object_id,term_taxonomy_id),\n KEY term_taxonomy_id (term_taxonomy_id)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.comments +
            " (\n  comment_ID bigint(20) unsigned NOT NULL auto_increment,\n  comment_post_ID int(11) NOT NULL default \'0\',\n  comment_author tinytext NOT NULL,\n  comment_author_email varchar(100) NOT NULL default \'\',\n  comment_author_url varchar(200) NOT NULL default \'\',\n  comment_author_IP varchar(100) NOT NULL default \'\',\n  comment_date datetime NOT NULL default \'0000-00-00 00:00:00\',\n  comment_date_gmt datetime NOT NULL default \'0000-00-00 00:00:00\',\n  comment_content text NOT NULL,\n  comment_karma int(11) NOT NULL default \'0\',\n  comment_approved varchar(20) NOT NULL default \'1\',\n  comment_agent varchar(255) NOT NULL default \'\',\n  comment_type varchar(20) NOT NULL default \'\',\n  comment_parent bigint(20) NOT NULL default \'0\',\n  user_id bigint(20) NOT NULL default \'0\',\n  PRIMARY KEY  (comment_ID),\n  KEY comment_approved (comment_approved),\n  KEY comment_post_ID (comment_post_ID),\n  KEY comment_approved_date_gmt (comment_approved,comment_date_gmt),\n  KEY comment_date_gmt (comment_date_gmt)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.links +
            " (\n  link_id bigint(20) NOT NULL auto_increment,\n  link_url varchar(255) NOT NULL default \'\',\n  link_name varchar(255) NOT NULL default \'\',\n  link_image varchar(255) NOT NULL default \'\',\n  link_target varchar(25) NOT NULL default \'\',\n  link_category bigint(20) NOT NULL default \'0\',\n  link_description varchar(255) NOT NULL default \'\',\n  link_visible varchar(20) NOT NULL default \'Y\',\n  link_owner int(11) NOT NULL default \'1\',\n  link_rating int(11) NOT NULL default \'0\',\n  link_updated datetime NOT NULL default \'0000-00-00 00:00:00\',\n  link_rel varchar(255) NOT NULL default \'\',\n  link_notes mediumtext NOT NULL,\n  link_rss varchar(255) NOT NULL default \'\',\n  PRIMARY KEY  (link_id),\n  KEY link_category (link_category),\n  KEY link_visible (link_visible)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.options +
            " (\n  option_id bigint(20) NOT NULL auto_increment,\n  blog_id int(11) NOT NULL default \'0\',\n  option_name varchar(64) NOT NULL default \'\',\n  option_value longtext NOT NULL,\n  autoload varchar(20) NOT NULL default \'yes\',\n  PRIMARY KEY  (option_id,blog_id,option_name),\n  KEY option_name (option_name)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.postmeta +
            " (\n  meta_id bigint(20) NOT NULL auto_increment,\n  post_id bigint(20) NOT NULL default \'0\',\n  meta_key varchar(255) default NULL,\n  meta_value longtext,\n  PRIMARY KEY  (meta_id),\n  KEY post_id (post_id),\n  KEY meta_key (meta_key)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.posts +
            " (\n  ID bigint(20) unsigned NOT NULL auto_increment,\n  post_author bigint(20) NOT NULL default \'0\',\n  post_date datetime NOT NULL default \'0000-00-00 00:00:00\',\n  post_date_gmt datetime NOT NULL default \'0000-00-00 00:00:00\',\n  post_content longtext NOT NULL,\n  post_title text NOT NULL,\n  post_category int(4) NOT NULL default \'0\',\n  post_excerpt text NOT NULL,\n  post_status varchar(20) NOT NULL default \'publish\',\n  comment_status varchar(20) NOT NULL default \'open\',\n  ping_status varchar(20) NOT NULL default \'open\',\n  post_password varchar(20) NOT NULL default \'\',\n  post_name varchar(200) NOT NULL default \'\',\n  to_ping text NOT NULL,\n  pinged text NOT NULL,\n  post_modified datetime NOT NULL default \'0000-00-00 00:00:00\',\n  post_modified_gmt datetime NOT NULL default \'0000-00-00 00:00:00\',\n  post_content_filtered text NOT NULL,\n  post_parent bigint(20) NOT NULL default \'0\',\n  guid varchar(255) NOT NULL default \'\',\n  menu_order int(11) NOT NULL default \'0\',\n  post_type varchar(20) NOT NULL default \'post\',\n  post_mime_type varchar(100) NOT NULL default \'\',\n  comment_count bigint(20) NOT NULL default \'0\',\n  PRIMARY KEY  (ID),\n  KEY post_name (post_name),\n  KEY type_status_date (post_type,post_status,post_date,ID)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.users +
            " (\n  ID bigint(20) unsigned NOT NULL auto_increment,\n  user_login varchar(60) NOT NULL default \'\',\n  user_pass varchar(64) NOT NULL default \'\',\n  user_nicename varchar(50) NOT NULL default \'\',\n  user_email varchar(100) NOT NULL default \'\',\n  user_url varchar(100) NOT NULL default \'\',\n  user_registered datetime NOT NULL default \'0000-00-00 00:00:00\',\n  user_activation_key varchar(60) NOT NULL default \'\',\n  user_status int(11) NOT NULL default \'0\',\n  display_name varchar(250) NOT NULL default \'\',\n  PRIMARY KEY  (ID),\n  KEY user_login_key (user_login),\n  KEY user_nicename (user_nicename)\n) " +
            charset_collate + ";\nCREATE TABLE " + gVars.wpdb.usermeta +
            " (\n  umeta_id bigint(20) NOT NULL auto_increment,\n  user_id bigint(20) NOT NULL default \'0\',\n  meta_key varchar(255) default NULL,\n  meta_value longtext,\n  PRIMARY KEY  (umeta_id),\n  KEY user_id (user_id),\n  KEY meta_key (meta_key)\n) " +
            charset_collate + ";";

        return DEFAULT_VAL;
    }
}
