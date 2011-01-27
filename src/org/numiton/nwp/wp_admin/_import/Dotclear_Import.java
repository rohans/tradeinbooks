/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Dotclear_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.CommonInterface5;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_admin.includes.BookmarkPage;
import org.numiton.nwp.wp_admin.includes.CommentPage;
import org.numiton.nwp.wp_admin.includes.PostPage;
import org.numiton.nwp.wp_admin.includes.TaxonomyPage;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.UserPage;

import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


/**
 * The Main Importer Class
 */
public class Dotclear_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Dotclear_Import.class.getName());
    public CommonInterface5 commonInterface5;
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public Dotclear_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import DotClear", "default") + "</h2>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Steps may take a few minutes depending on the size of your database. Please be patient.", "default") + "</p>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        echo(
                gVars.webEnv,
                "<div class=\"narrow\"><p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This importer allows you to extract posts from a DotClear database into your blog.  Mileage may vary.", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Your DotClear Configuration settings are as follows:", "default") + "</p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=dotclear&amp;step=1\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-dotclear", "_wpnonce", true, true);
        this.db_form();
        echo(gVars.webEnv,
            "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Categories", "default")) + "\" /></p>");
        echo(gVars.webEnv, "</form></div>");
    }

    public Array<Object> get_dc_cats() {
        wpdb dcdb = null;
        Object dbprefix = null;
        
		// General Housekeeping
        dcdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcpass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dchost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        dbprefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcdbprefix");

		// Get Categories
        return dcdb.get_results("SELECT * FROM " + dbprefix + "categorie", gConsts.getARRAY_A());
    }

    public Array<Object> get_dc_users() {
        wpdb dcdb = null;
        String dbprefix = null;
        
		// General Housekeeping
        dcdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcpass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dchost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        dbprefix = strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcdbprefix"));

		// Get Users
        return dcdb.get_results("SELECT * FROM " + dbprefix + "user", gConsts.getARRAY_A());
    }

    public Array<Object> get_dc_posts() {
        wpdb dcdb = null;
        Object dbprefix = null;
        
		// General Housekeeping
        dcdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcpass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dchost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        dbprefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcdbprefix");

		// Get Posts
        return dcdb.get_results(
                "SELECT " + dbprefix + "post.*, " + dbprefix + "categorie.cat_libelle_url AS post_cat_name\n\t\t\t\t\t\tFROM " + dbprefix + "post INNER JOIN " + dbprefix +
                "categorie\n\t\t\t\t\t\tON " + dbprefix + "post.cat_id = " + dbprefix + "categorie.cat_id",
                gConsts.getARRAY_A());
    }

    public Array<Object> get_dc_comments() {
        wpdb dcdb = null;
        Object dbprefix = null;
        
		// General Housekeeping
        dcdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcpass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dchost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        dbprefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcdbprefix");

		// Get Comments
        return dcdb.get_results("SELECT * FROM " + dbprefix + "comment", gConsts.getARRAY_A());
    }

    public Array<Object> get_dc_links() {
        wpdb dcdb = null;
        Object dbprefix = null;
        
		//General Housekeeping
        dcdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcpass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dchost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        dbprefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcdbprefix");

        return dcdb.get_results("SELECT * FROM " + dbprefix + "link ORDER BY position", gConsts.getARRAY_A());
    }

    public boolean cat2wp(Object categories)/* Do not change type */
     {
        int count = 0;
        Array<Object> dccat2wpcat = new Array<Object>();
        Array<Object> category = null;
        String name = null;
        String cat_libelle_url = null;
        String title = null;
        String cat_libelle = null;
        String desc = null;
        String cat_desc = null;
        String cinfo = null;
        Object ret_id = null;
        Object id = null;
        
		// General Housekeeping
        count = 0;
        dccat2wpcat = new Array<Object>();

		// Do the Magic
        if (is_array(categories)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Categories...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry52 : ((Array<?>) categories).entrySet()) {
                category = (Array<Object>) javaEntry52.getValue();
                
                count++;
                cat_libelle_url = strval(Array.extractVar(category, "cat_libelle_url", cat_libelle_url, Array.EXTR_OVERWRITE));
                cat_libelle = strval(Array.extractVar(category, "cat_libelle", cat_libelle, Array.EXTR_OVERWRITE));
                cat_desc = strval(Array.extractVar(category, "cat_desc", cat_desc, Array.EXTR_OVERWRITE));
                id = Array.extractVar(category, "id", id, Array.EXTR_OVERWRITE);
                
				// Make Nice Variables
                name = gVars.wpdb.escape(cat_libelle_url);
                title = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(cat_libelle));
                desc = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(cat_desc));

                if (booleanval(cinfo = strval(getIncluded(TaxonomyPage.class, gVars, gConsts).category_exists(name)))) {
                    ret_id = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_category(new Array<Object>(new ArrayEntry<Object>("cat_ID", cinfo),
                                new ArrayEntry<Object>("category_nicename", name), new ArrayEntry<Object>("cat_name", title), new ArrayEntry<Object>("category_description", desc)), false);
                } else {
                    ret_id = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_category(new Array<Object>(
                                new ArrayEntry<Object>("category_nicename", name),
                                new ArrayEntry<Object>("cat_name", title),
                                new ArrayEntry<Object>("category_description", desc)), false);
                }

                dccat2wpcat.putValue(id, ret_id);
            }

			// Store category translation for future use
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dccat2wpcat", dccat2wpcat, "", "yes");
            echo(gVars.webEnv,
                "<p>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts)
                                     .__ngettext("Done! <strong>%1$s</strong> category imported.", "Done! <strong>%1$s</strong> categories imported.", count, "default"), count) + "<br /><br /></p>");

            return true;
        }

        echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("No Categories to Import!", "default"));

        return false;
    }

    public boolean users2wp(Object users)/* Do not change type */
     {
        int count = 0;
        Array<Object> dcid2wpid = new Array<Object>();
        WP_User user = null;
        String name = null;
        String user_pseudo = null;
        StdClass uinfo = null;
        int ret_id = 0;
        Object user_id = null;
        String Realname = null;
        String user_email = null;
        int wp_perms = 0;
        int user_level = 0;
        String user_prenom = null;
        String user_nom = null;
        
		// General Housekeeping
        count = 0;
        dcid2wpid = new Array<Object>();

		// Midnight Mojo
        if (is_array(users)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Users...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry53 : ((Array<?>) users).entrySet()) {
                Array userArray = (Array) javaEntry53.getValue();
                count++;
                name = strval(Array.extractVar(userArray, "name", name, Array.EXTR_OVERWRITE));
                user_pseudo = strval(Array.extractVar(userArray, "user_pseudo", user_pseudo, Array.EXTR_OVERWRITE));
                user_id = Array.extractVar(userArray, "user_id", user_id, Array.EXTR_OVERWRITE);
                user_email = strval(Array.extractVar(userArray, "user_email", user_email, Array.EXTR_OVERWRITE));
                user_level = intval(Array.extractVar(userArray, "user_level", user_level, Array.EXTR_OVERWRITE));
                user_prenom = strval(Array.extractVar(userArray, "user_prenom", user_prenom, Array.EXTR_OVERWRITE));
                user_nom = strval(Array.extractVar(userArray, "user_nom", user_nom, Array.EXTR_OVERWRITE));
                
				// Make Nice Variables
                name = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(name));
                Realname = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(user_pseudo));

                if (booleanval(uinfo = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(name))) {
                    ret_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(
                            new Array<Object>(new ArrayEntry<Object>("ID", StdClass.getValue(uinfo, "ID")), new ArrayEntry<Object>("user_login", user_id),
                                new ArrayEntry<Object>("user_nicename", Realname), new ArrayEntry<Object>("user_email", user_email), new ArrayEntry<Object>("user_url", "http://"),
                                new ArrayEntry<Object>("display_name", Realname)));
                } else {
                    ret_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(
                            new Array<Object>(
                                new ArrayEntry<Object>("user_login", user_id),
                                new ArrayEntry<Object>("user_nicename", getIncluded(DotclearPage.class, gVars, gConsts).csc(user_pseudo)),
                                new ArrayEntry<Object>("user_email", user_email),
                                new ArrayEntry<Object>("user_url", "http://"),
                                new ArrayEntry<Object>("display_name", Realname)));
                }

                dcid2wpid.putValue(user_id, ret_id);
                
				// Set DotClear-to-WordPress permissions translation

				// Update Usermeta Data
                user = new WP_User(gVars, gConsts, ret_id);
                wp_perms = user_level + 1;

                if (equal(10, wp_perms)) {
                    user.set_role("administrator");
                } else if (equal(9, wp_perms)) {
                    user.set_role("editor");
                } else if (5 <= wp_perms) {
                    user.set_role("editor");
                } else if (4 <= wp_perms) {
                    user.set_role("author");
                } else if (3 <= wp_perms) {
                    user.set_role("contributor");
                } else if (2 <= wp_perms) {
                    user.set_role("contributor");
                } else {
                    user.set_role("subscriber");
                }

                getIncluded(UserPage.class, gVars, gConsts).update_usermeta(ret_id, "wp_user_level", wp_perms);
                getIncluded(UserPage.class, gVars, gConsts).update_usermeta(ret_id, "rich_editing", "false");
                getIncluded(UserPage.class, gVars, gConsts).update_usermeta(ret_id, "first_name", getIncluded(DotclearPage.class, gVars, gConsts).csc(user_prenom));
                getIncluded(UserPage.class, gVars, gConsts).update_usermeta(ret_id, "last_name", getIncluded(DotclearPage.class, gVars, gConsts).csc(user_nom));
            }// End foreach($users as $user)

			// Store id translation array for future use
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dcid2wpid", dcid2wpid, "", "yes");
            echo(gVars.webEnv, "<p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Done! <strong>%1$s</strong> users imported.", "default"), count) + "<br /><br /></p>");

            return true;
        }// End if(is_array($users)

        echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("No Users to Import!", "default"));

        return false;
    }// End function user2wp()

    /**
     * End function user2wp() End function user2wp()
     */
    public Object posts2wp(Object posts)/* Do not change type */
     {
        int count = 0;
        Array<Object> dcposts2wpposts = new Array<Object>();
        Array<Object> cats = new Array<Object>();
        Array<Object> post = null;
        Array<Object> stattrans = new Array<Object>();
        Array<Object> comment_status_map = new Array<Object>();
        Object uinfo;

        /* Do not change type */
        String user_id = null;
        int authorid = 0;
        String Title = null;
        String post_titre = null;
        String post_content = null;
        String post_excerpt = null;
        Object post_chapo = null;
        Object post_status = null;
        String post_pub = null;
        int pinfo = 0;
        Object ret_id;
        Object post_dt = null;
        Object post_upddt = null;
        Object post_titre_url = null;
        Object post_open_comment = null;
        Object post_open_tb = null;
        Object post_nb_comment = null;
        Object post_nb_trackback = null;
        Object post_modified_gmt = null;
        Object post_id = null;
        Object category1 = null;
        Object post_cat_name = null;
        Object cat1 = null;
        
		// General Housekeeping
        count = 0;
        dcposts2wpposts = new Array<Object>();
        cats = new Array<Object>();

		// Do the Magic
        if (is_array(posts)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Posts...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry54 : ((Array<?>) posts).entrySet()) {
                post = (Array<Object>) javaEntry54.getValue();
                count++;
                user_id = strval(Array.extractVar(post, "user_id", user_id, Array.EXTR_OVERWRITE));
                post_titre = strval(Array.extractVar(post, "post_titre", post_titre, Array.EXTR_OVERWRITE));
                post_content = strval(Array.extractVar(post, "post_content", post_content, Array.EXTR_OVERWRITE));
                post_chapo = Array.extractVar(post, "post_chapo", post_chapo, Array.EXTR_OVERWRITE);
                post_pub = strval(Array.extractVar(post, "post_pub", post_pub, Array.EXTR_OVERWRITE));
                post_dt = Array.extractVar(post, "post_dt", post_dt, Array.EXTR_OVERWRITE);
                post_upddt = Array.extractVar(post, "post_upddt", post_upddt, Array.EXTR_OVERWRITE);
                post_titre_url = Array.extractVar(post, "post_titre_url", post_titre_url, Array.EXTR_OVERWRITE);
                post_open_comment = Array.extractVar(post, "post_open_comment", post_open_comment, Array.EXTR_OVERWRITE);
                post_open_tb = Array.extractVar(post, "post_open_tb", post_open_tb, Array.EXTR_OVERWRITE);
                post_nb_comment = Array.extractVar(post, "post_nb_comment", post_nb_comment, Array.EXTR_OVERWRITE);
                post_nb_trackback = Array.extractVar(post, "post_nb_trackback", post_nb_trackback, Array.EXTR_OVERWRITE);
                post_modified_gmt = Array.extractVar(post, "post_modified_gmt", post_modified_gmt, Array.EXTR_OVERWRITE);
                post_id = Array.extractVar(post, "post_id", post_id, Array.EXTR_OVERWRITE);
                post_cat_name = Array.extractVar(post, "post_cat_name", post_cat_name, Array.EXTR_OVERWRITE);
                
				// Set DotClear-to-WordPress status translation
                stattrans = new Array<Object>(new ArrayEntry<Object>(0, "draft"), new ArrayEntry<Object>(1, "publish"));
                comment_status_map = new Array<Object>(new ArrayEntry<Object>(0, "closed"), new ArrayEntry<Object>(1, "open"));
                
				//Can we do this more efficiently?
                uinfo = (booleanval(getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(user_id))
                    ? getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(user_id)
                    : 1);
                authorid = intval((is_object(uinfo)
                        ? ((StdClass) uinfo).fields.getValue("ID")
                        : uinfo));
                Title = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(post_titre));
                post_content = getIncluded(DotclearPage.class, gVars, gConsts).textconv(post_content);
                post_excerpt = "";

                if (!equal(post_chapo, "")) {
                    post_excerpt = getIncluded(DotclearPage.class, gVars, gConsts).textconv(strval(post_chapo));
                    post_content = post_excerpt + "\n<!--more-->\n" + post_content;
                }

                post_excerpt = gVars.wpdb.escape(post_excerpt);
                post_content = gVars.wpdb.escape(post_content);
                post_status = stattrans.getValue(post_pub);

				// Import Post data into WordPress
                
                if (booleanval(pinfo = getIncluded(PostPage.class, gVars, gConsts).post_exists(Title, post_content, ""))) {
                    ret_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(
                            new Array<Object>(
                                new ArrayEntry<Object>("ID", pinfo),
                                new ArrayEntry<Object>("post_author", authorid),
                                new ArrayEntry<Object>("post_date", post_dt),
                                new ArrayEntry<Object>("post_date_gmt", post_dt),
                                new ArrayEntry<Object>("post_modified", post_upddt),
                                new ArrayEntry<Object>("post_modified_gmt", post_upddt),
                                new ArrayEntry<Object>("post_title", Title),
                                new ArrayEntry<Object>("post_content", post_content),
                                new ArrayEntry<Object>("post_excerpt", post_excerpt),
                                new ArrayEntry<Object>("post_status", post_status),
                                new ArrayEntry<Object>("post_name", post_titre_url),
                                new ArrayEntry<Object>("comment_status", comment_status_map.getValue(post_open_comment)),
                                new ArrayEntry<Object>("ping_status", comment_status_map.getValue(post_open_tb)),
                                new ArrayEntry<Object>("comment_count", intval(post_nb_comment) + intval(post_nb_trackback))));

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ret_id)) {
                        return ret_id;
                    }
                } else {
                    ret_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(
                            new Array<Object>(
                                new ArrayEntry<Object>("post_author", authorid),
                                new ArrayEntry<Object>("post_date", post_dt),
                                new ArrayEntry<Object>("post_date_gmt", post_dt),
                                new ArrayEntry<Object>("post_modified", post_modified_gmt),
                                new ArrayEntry<Object>("post_modified_gmt", post_modified_gmt),
                                new ArrayEntry<Object>("post_title", Title),
                                new ArrayEntry<Object>("post_content", post_content),
                                new ArrayEntry<Object>("post_excerpt", post_excerpt),
                                new ArrayEntry<Object>("post_status", post_status),
                                new ArrayEntry<Object>("post_name", post_titre_url),
                                new ArrayEntry<Object>("comment_status", comment_status_map.getValue(post_open_comment)),
                                new ArrayEntry<Object>("ping_status", comment_status_map.getValue(post_open_tb)),
                                new ArrayEntry<Object>("comment_count", intval(post_nb_comment) + intval(post_nb_trackback))));

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ret_id)) {
                        return ret_id;
                    }
                }

                dcposts2wpposts.putValue(post_id, ret_id);
                
				// Make Post-to-Category associations
                cats = new Array<Object>();

                StdClass category1Obj = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category_by_slug(post_cat_name);
                category1 = StdClass.getValue(category1Obj, "term_id");

                if (booleanval(cat1 = category1)) {
                    cats.putValue(1, cat1);
                }

                if (!empty(cats)) {
                    (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_set_post_categories(intval(ret_id), cats);
                }
            }
        }

		// Store ID translation for later use
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dcposts2wpposts", dcposts2wpposts, "", "yes");
        
        echo(gVars.webEnv, "<p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Done! <strong>%1$s</strong> posts imported.", "default"), count) + "<br /><br /></p>");

        return true;
    }

    public boolean comments2wp(Object comments)/* Do not change type */
     {
        int count = 0;
        Array<Object> dccm2wpcm = new Array<Object>();
        Array<Object> postarr = new Array<Object>();
        Array<Object> comment = null;
        int comment_ID = 0;
        String comment_id = null;
        int comment_post_ID = 0;
        Object post_id = null;
        Object comment_approved = null;
        Object comment_pub = null;
        String name = null;
        String comment_auteur = null;
        String email = null;
        String comment_email = null;
        String web = null;
        String comment_site = null;
        String message = null;
        String comment_content = null;
        Object cinfo = null;
        Object comment_dt = null;
        int ret_id = 0;
        Object comment_ip = null;
        
		// General Housekeeping
        count = 0;
        dccm2wpcm = new Array<Object>();
        postarr = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcposts2wpposts");

		// Magic Mojo
        if (is_array(comments)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Comments...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry55 : ((Array<?>) comments).entrySet())/*
             * nWordPressify Data
             */
             {
                comment = (Array<Object>) javaEntry55.getValue();
                count++;
                comment_id = strval(Array.extractVar(comment, "comment_id", comment_id, Array.EXTR_OVERWRITE));
                post_id = Array.extractVar(comment, "post_id", post_id, Array.EXTR_OVERWRITE);
                comment_pub = Array.extractVar(comment, "comment_pub", comment_pub, Array.EXTR_OVERWRITE);
                comment_auteur = strval(Array.extractVar(comment, "comment_auteur", comment_auteur, Array.EXTR_OVERWRITE));
                comment_email = strval(Array.extractVar(comment, "comment_email", comment_email, Array.EXTR_OVERWRITE));
                comment_site = strval(Array.extractVar(comment, "comment_site", comment_site, Array.EXTR_OVERWRITE));
                comment_content = strval(Array.extractVar(comment, "comment_content", comment_content, Array.EXTR_OVERWRITE));
                comment_dt = Array.extractVar(comment, "comment_dt", comment_dt, Array.EXTR_OVERWRITE);
                comment_ip = Array.extractVar(comment, "comment_ip", comment_ip, Array.EXTR_OVERWRITE);
                
				// WordPressify Data
                comment_ID = intval(Strings.ltrim(comment_id, "0"));
                comment_post_ID = intval(postarr.getValue(post_id));
                comment_approved = comment_pub;
                name = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(comment_auteur));
                email = gVars.wpdb.escape(comment_email);
                web = "http://" + gVars.wpdb.escape(comment_site);
                message = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).textconv(comment_content));

                if (booleanval(cinfo = getIncluded(CommentPage.class, gVars, gConsts).comment_exists(name, strval(comment_dt)))) {
					// Update comments
                    ret_id = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_update_comment(
                                new Array<Object>(
                                    new ArrayEntry<Object>("comment_ID", cinfo),
                                    new ArrayEntry<Object>("comment_post_ID", comment_post_ID),
                                    new ArrayEntry<Object>("comment_author", name),
                                    new ArrayEntry<Object>("comment_author_email", email),
                                    new ArrayEntry<Object>("comment_author_url", web),
                                    new ArrayEntry<Object>("comment_author_IP", comment_ip),
                                    new ArrayEntry<Object>("comment_date", comment_dt),
                                    new ArrayEntry<Object>("comment_date_gmt", comment_dt),
                                    new ArrayEntry<Object>("comment_content", message),
                                    new ArrayEntry<Object>("comment_approved", comment_approved)));
                } else {
					// Insert comments
                    ret_id = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_insert_comment(
                                new Array<Object>(
                                    new ArrayEntry<Object>("comment_post_ID", comment_post_ID),
                                    new ArrayEntry<Object>("comment_author", name),
                                    new ArrayEntry<Object>("comment_author_email", email),
                                    new ArrayEntry<Object>("comment_author_url", web),
                                    new ArrayEntry<Object>("comment_author_IP", comment_ip),
                                    new ArrayEntry<Object>("comment_date", comment_dt),
                                    new ArrayEntry<Object>("comment_date_gmt", comment_dt),
                                    new ArrayEntry<Object>("comment_content", message),
                                    new ArrayEntry<Object>("comment_approved", comment_approved)));
                }

                dccm2wpcm.putValue(comment_ID, ret_id);
            }

			// Store Comment ID translation for future use
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dccm2wpcm", dccm2wpcm, "", "yes");
            
			// Associate newly formed categories with posts
            commonInterface5.get_comment_count(ret_id);
            echo(gVars.webEnv, "<p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Done! <strong>%1$s</strong> comments imported.", "default"), count) + "<br /><br /></p>");

            return true;
        }

        echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("No Comments to Import!", "default"));

        return false;
    }

    public boolean links2wp(Object links)/* Do not change type */
     {
        int count = 0;
        Array<Object> link = null;
        String title = null;
        Array<Object> cinfo = new Array<Object>();
        Object category = null;
        String linkname = null;
        String label = null;
        String description = null;
        Object linfo = null;
        int ret_id = 0;
        Object href = null;
        Object url = null;
        Array<Object> dclinks2wplinks = new Array<Object>();
        Object link_id = null;
        
		// General Housekeeping
        count = 0;

		// Deal with the links
        if (is_array(links)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Links...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry56 : ((Array<?>) links).entrySet()) {
                link = (Array<Object>) javaEntry56.getValue();
                count++;
                title = strval(Array.extractVar(link, "title", title, Array.EXTR_OVERWRITE));
                label = strval(Array.extractVar(link, "label", label, Array.EXTR_OVERWRITE));
                href = Array.extractVar(link, "href", href, Array.EXTR_OVERWRITE);
                url = Array.extractVar(link, "url", url, Array.EXTR_OVERWRITE);
                link_id = Array.extractVar(link, "link_id", link_id, Array.EXTR_OVERWRITE);

                if (!equal(title, "")) {
                    if (booleanval(
                                cinfo = (Array<Object>) (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).is_term(getIncluded(
                                            DotclearPage.class,
                                            gVars,
                                            gConsts).csc(title), "link_category"))) {
                        category = cinfo.getValue("term_id");
                    } else {
                        category = (((org.numiton.nwp.wp_includes.TaxonomyPage) getIncluded(org.numiton.nwp.wp_includes.TaxonomyPage.class, gVars, gConsts))).wp_insert_term(
                                    gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(title)),
                                    "link_category",
                                    new Array<Object>());
                        category = ((Array) category).getValue("term_id");
                    }
                } else {
                    linkname = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(label));
                    description = gVars.wpdb.escape(getIncluded(DotclearPage.class, gVars, gConsts).csc(title));

                    if (booleanval(linfo = getIncluded(DotclearPage.class, gVars, gConsts).link_exists(linkname))) {
                        ret_id = getIncluded(BookmarkPage.class, gVars, gConsts).wp_insert_link(
                                new Array<Object>(
                                    new ArrayEntry<Object>("link_id", linfo),
                                    new ArrayEntry<Object>("link_url", href),
                                    new ArrayEntry<Object>("link_name", linkname),
                                    new ArrayEntry<Object>("link_category", category),
                                    new ArrayEntry<Object>("link_description", description)));
                    } else {
                        ret_id = getIncluded(BookmarkPage.class, gVars, gConsts).wp_insert_link(
                                new Array<Object>(
                                    new ArrayEntry<Object>("link_url", url),
                                    new ArrayEntry<Object>("link_name", linkname),
                                    new ArrayEntry<Object>("link_category", category),
                                    new ArrayEntry<Object>("link_description", description)));
                    }

                    dclinks2wplinks.putValue(link_id, ret_id);
                }
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dclinks2wplinks", dclinks2wplinks, "", "yes");
            echo(gVars.webEnv, "<p>");
            QStrings.printf(gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts)
                    .__ngettext("Done! <strong>%s</strong> link or link category imported.", "Done! <strong>%s</strong> links or link categories imported.", count, "default"), count);
            echo(gVars.webEnv, "<br /><br /></p>");

            return true;
        }

        echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("No Links to Import!", "default"));

        return false;
    }

    public void import_categories() {
        Array<Object> cats = new Array<Object>();
        
		// Category Import
        cats = this.get_dc_cats();
        this.cat2wp(cats);
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dc_cats", cats, "", "yes");
        
        
        
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=dotclear&amp;step=2\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-dotclear", "_wpnonce", true, true);
        QStrings.printf(gVars.webEnv, "<input type=\"submit\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Users", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public void import_users() {
        Array<Object> users = new Array<Object>();
        
		// User Import
        users = this.get_dc_users();
        this.users2wp(users);
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=dotclear&amp;step=3\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-dotclear", "_wpnonce", true, true);
        QStrings.printf(gVars.webEnv, "<input type=\"submit\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Posts", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public Object import_posts() {
        Array<Object> posts = new Array<Object>();
        Object result;
        
		// Post Import
        posts = this.get_dc_posts();
        result = this.posts2wp(posts);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            return result;
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=dotclear&amp;step=4\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-dotclear", "_wpnonce", true, true);
        QStrings.printf(gVars.webEnv, "<input type=\"submit\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Comments", "default")));
        echo(gVars.webEnv, "</form>");

        return 0;
    }

    public void import_comments() {
        Array<Object> comments = new Array<Object>();
		// Comment Import
        comments = this.get_dc_comments();
        this.comments2wp(comments);
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=dotclear&amp;step=5\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-dotclear", "_wpnonce", true, true);
        QStrings.printf(gVars.webEnv, "<input type=\"submit\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Links", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public void import_links() {
        Array<Object> links = new Array<Object>();
        
		//Link Import
        links = this.get_dc_links();
        this.links2wp(links);
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dc_links", links, "", "yes");
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=dotclear&amp;step=6\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-dotclear", "_wpnonce", true, true);
        QStrings.printf(gVars.webEnv, "<input type=\"submit\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Finish", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public void cleanup_dcimport() {
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcdbprefix");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dc_cats");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcid2wpid");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dccat2wpcat");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcposts2wpposts");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dccm2wpcm");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dclinks2wplinks");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcuser");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcpass");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcname");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dchost");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dccharset");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "dotclear");
        this.tips();
    }

    public void tips() {
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "Welcome to nWordPress.  We hope (and expect!) that you will find this platform incredibly rewarding!  As a new nWordPress user coming from DotClear, there are some things that we would like to point out.  Hopefully, they will help your transition go as smoothly as possible.",
                        "default") + "</p>");
        echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Users", "default") + "</h3>");
        echo(
                gVars.webEnv,
                "<p>" +
                QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__(
                                "You have already setup nWordPress and have been assigned an administrative login and password.  Forget it.  You didn\'t have that login in DotClear, why should you have it here?  Instead we have taken care to import all of your users into our system.  Unfortunately there is one downside.  Because both nWordPress and DotClear uses a strong encryption hash with passwords, it is impossible to decrypt it and we are forced to assign temporary passwords to all your users.  <strong>Every user has the same username, but their passwords are reset to password123.</strong>  So <a href=\"%1$s\">Login</a> and change it.",
                                "default"),
                        "/wp-login.php") + "</p>");
        echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Preserving Authors", "default") + "</h3>");
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "Secondly, we have attempted to preserve post authors.  If you are the only author or contributor to your blog, then you are safe.  In most cases, we are successful in this preservation endeavor.  However, if we cannot ascertain the name of the writer due to discrepancies between database tables, we assign it to you, the administrative user.",
                        "default") + "</p>");
        echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Textile", "default") + "</h3>");
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "Also, since you\'re coming from DotClear, you probably have been using Textile to format your comments and posts.  If this is the case, we recommend downloading and installing <a href=\"http://www.huddledmasses.org/category/development/wordpress/textile/\">Textile for WordPress</a>.  Trust me... You\'ll want it.",
                        "default") + "</p>");
        echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("WordPress Resources", "default") + "</h3>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Finally, there are numerous WordPress resources around the internet.  Some of them are:", "default") + "</p>");
        echo(gVars.webEnv, "<ul>");
        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://www.wordpress.org\">The official WordPress site</a>", "default") + "</li>");
        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://wordpress.org/support/\">The WordPress support forums</a>", "default") + "</li>");
        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://codex.wordpress.org\">The Codex (In other words, the WordPress Bible)</a>", "default") + "</li>");
        echo(gVars.webEnv, "</ul>");
        echo(
            gVars.webEnv,
            "<p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("That\'s it! What are you waiting for? Go <a href=\"%1$s\">login</a>!", "default"), "../wp-login.php") + "</p>");
    }

    public void db_form() {
        echo(gVars.webEnv, "<table class=\"form-table\">");
        QStrings.printf(
            gVars.webEnv,
            "<tr><th><label for=\"dbuser\">%s</label></th><td><input type=\"text\" name=\"dbuser\" id=\"dbuser\" /></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("DotClear Database User:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th><label for=\"dbpass\">%s</label></th><td><input type=\"password\" name=\"dbpass\" id=\"dbpass\" /></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("DotClear Database Password:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th><label for=\"dbname\">%s</label></th><td><input type=\"text\" name=\"dbname\" id=\"dbname\" /></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("DotClear Database Name:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th><label for=\"dbhost\">%s</label></th><td><input type=\"text\" name=\"dbhost\" nameid=\"dbhost\" value=\"localhost\" /></td></tr>",
            (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("DotClear Database Host:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th><label for=\"dbprefix\">%s</label></th><td><input type=\"text\" name=\"dbprefix\" id=\"dbprefix\" value=\"dc_\"/></td></tr>",
            (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("DotClear Table prefix:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th><label for=\"dccharset\">%s</label></th><td><input type=\"text\" name=\"dccharset\" id=\"dccharset\" value=\"ISO-8859-15\"/></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("Originating character set:", "default"));
        echo(gVars.webEnv, "</table>");
    }

    public void dispatch() {
        int step = 0;
        Object result;

        if (empty(gVars.webEnv._GET.getValue("step"))) {
            step = 0;
        } else {
            step = intval(gVars.webEnv._GET.getValue("step"));
        }

        this.header();

        if (step > 0) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-dotclear", "_wpnonce");

            if (booleanval(gVars.webEnv._POST.getValue("dbuser"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcuser"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcuser");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("dcuser", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbuser")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbpass"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcpass"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcpass");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("dcpass", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbpass")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbname"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcname"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcname");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("dcname", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbname")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbhost"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dchost"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dchost");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("dchost", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbhost")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dccharset"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dccharset"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dccharset");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dccharset",
                    getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dccharset")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbprefix"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("dcdbprefix"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("dcdbprefix");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts).add_option("dcdbprefix",
                    getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbprefix")), true), "", "yes");
            }
        }

        switch (step) {
        default: {
        }

        case 0: {
            this.greet();

            break;
        }

        case 1: {
            this.import_categories();

            break;
        }

        case 2: {
            this.import_users();

            break;
        }

        case 3: {
            result = this.import_posts();

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
                echo(gVars.webEnv, ((WP_Error) result).get_error_message());
            }

            break;
        }

        case 4: {
            this.import_comments();

            break;
        }

        case 5: {
            this.import_links();

            break;
        }

        case 6: {
            this.cleanup_dcimport();

            break;
        }
        }

        this.footer();
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
