/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Textpattern_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.CommonInterface4;
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
	The Main Importer Class
**/
public class Textpattern_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(Textpattern_Import.class.getName());
    public CommonInterface4 commonInterface4;
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public Textpattern_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Textpattern", "default") + "</h2>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Steps may take a few minutes depending on the size of your database. Please be patient.", "default") + "</p>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(
            gVars.webEnv,
            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This imports categories, users, posts, comments, and links from any Textpattern 4.0.2+ into this blog.", "default") +
            "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("This has not been tested on previous versions of Textpattern.  Mileage may vary.", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Your Textpattern Configuration settings are as follows:", "default") + "</p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=textpattern&amp;step=1\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-textpattern", "_wpnonce", true, true);
        this.db_form();
        echo(
            gVars.webEnv,
            "<p class=\"submit\"><input type=\"submit\" class=\"button\" name=\"submit\" value=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import", "default")) + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public Array<Object> get_txp_cats() {
        wpdb txpdb = null;
        Object prefix = null;
        
		// General Housekeeping
        txpdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txppass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txphost")));
        
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        prefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tpre");

		// Get Categories
        return txpdb.get_results("SELECT\n\t\t\tid,\n\t\t\tname,\n\t\t\ttitle\n\t\t\tFROM " + prefix + "txp_category\n\t\t\tWHERE type = \"article\"", gConsts.getARRAY_A());
    }

    public Array<Object> get_txp_users() {
        wpdb txpdb = null;
        Object prefix = null;
        
		// General Housekeeping
        txpdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txppass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txphost")));
        
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        prefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tpre");

		// Get Users
        
        return txpdb.get_results("SELECT\n\t\t\tuser_id,\n\t\t\tname,\n\t\t\tRealName,\n\t\t\temail,\n\t\t\tprivs\n\t\t\tFROM " + prefix + "txp_users", gConsts.getARRAY_A());
    }

    public Array<Object> get_txp_posts() {
        wpdb txpdb = null;
        Object prefix = null;
        
		// General Housekeeping
        txpdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txppass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txphost")));
        
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        prefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tpre");

		// Get Posts
        return txpdb.get_results(
                "SELECT\n\t\t\tID,\n\t\t\tPosted,\n\t\t\tAuthorID,\n\t\t\tLastMod,\n\t\t\tTitle,\n\t\t\tBody,\n\t\t\tExcerpt,\n\t\t\tCategory1,\n\t\t\tCategory2,\n\t\t\tStatus,\n\t\t\tKeywords,\n\t\t\turl_title,\n\t\t\tcomments_count\n\t\t\tFROM " +
                prefix + "textpattern\n\t\t\t",
                gConsts.getARRAY_A());
    }

    public Array<Object> get_txp_comments() {
        wpdb txpdb = null;
        Object prefix = null;
        
		// General Housekeeping
        txpdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txppass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txphost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        prefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tpre");

		// Get Comments
        return txpdb.get_results("SELECT * FROM " + prefix + "txp_discuss", gConsts.getARRAY_A());
    }

    public Array<Object> get_txp_links() {
        wpdb txpdb = null;
        Object prefix = null;
        
		//General Housekeeping
        txpdb = new wpdb(
                gVars,
                gConsts,
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpuser")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txppass")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpname")),
                strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txphost")));
        Options.set_magic_quotes_runtime(gVars.webEnv, 0);
        prefix = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tpre");

        return txpdb.get_results("SELECT\n\t\t\tid,\n\t\t\tdate,\n\t\t\tcategory,\n\t\t\turl,\n\t\t\tlinkname,\n\t\t\tdescription\n\t\t\tFROM " + prefix + "txp_link", gConsts.getARRAY_A());
    }

    public boolean cat2wp(Object categories)/* Do not change type */
     {
        int count = 0;
        Array<Object> txpcat2wpcat = new Array<Object>();
        Array<Object> category = null;
        String name = null;
        String title = null;
        Object cinfo;
        Object ret_id = null;
        Object id = null;
        
		//General Housekeeping
        count = 0;
        txpcat2wpcat = new Array<Object>();

		// Do the Magic
        if (is_array(categories)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Categories...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry74 : ((Array<?>) categories).entrySet()) {
                category = (Array<Object>) javaEntry74.getValue();
                count++;
                name = strval(Array.extractVar(category, "name", name, Array.EXTR_OVERWRITE));
                title = strval(Array.extractVar(category, "title", title, Array.EXTR_OVERWRITE));
                id = Array.extractVar(category, "id", id, Array.EXTR_OVERWRITE);
                
                
                
				// Make Nice Variables
                name = gVars.wpdb.escape(name);
                title = gVars.wpdb.escape(title);

                if (booleanval(cinfo = getIncluded(TaxonomyPage.class, gVars, gConsts).category_exists(name))) {
                    ret_id = getIncluded(TaxonomyPage.class, gVars, gConsts).wp_insert_category(new Array<Object>(
                                new ArrayEntry<Object>("cat_ID", cinfo),
                                new ArrayEntry<Object>("category_nicename", name),
                                new ArrayEntry<Object>("cat_name", title)), false);
                } else {
                    ret_id = getIncluded(TaxonomyPage.class, gVars, gConsts)
                                 .wp_insert_category(new Array<Object>(new ArrayEntry<Object>("category_nicename", name), new ArrayEntry<Object>("cat_name", title)), false);
                }

                txpcat2wpcat.putValue(id, ret_id);
            }

			// Store category translation for future use
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txpcat2wpcat", txpcat2wpcat, "", "yes");
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
        Array<Object> txpid2wpid = new Array<Object>();
        WP_User user = null;
        String name = null;
        String RealName = null;
        StdClass uinfo = null;
        int ret_id = 0;
        Object email = null;
        Object user_id = null;
        Array<Object> transperms = new Array<Object>();
        Object privs = null;
        
		// General Housekeeping
        count = 0;
        txpid2wpid = new Array<Object>();

		// Midnight Mojo
        if (is_array(users)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Users...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry75 : ((Array<?>) users).entrySet()) {
                Array userArray = (Array) javaEntry75.getValue();
                count++;
                name = strval(Array.extractVar(userArray, "name", name, Array.EXTR_OVERWRITE));
                RealName = strval(Array.extractVar(userArray, "RealName", RealName, Array.EXTR_OVERWRITE));
                email = Array.extractVar(userArray, "email", email, Array.EXTR_OVERWRITE);
                user_id = Array.extractVar(userArray, "user_id", user_id, Array.EXTR_OVERWRITE);
                privs = Array.extractVar(userArray, "privs", privs, Array.EXTR_OVERWRITE);
                
				// Make Nice Variables
                name = gVars.wpdb.escape(name);
                RealName = gVars.wpdb.escape(RealName);

                if (booleanval(uinfo = getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(name))) {
                    ret_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(
                            new Array<Object>(
                                new ArrayEntry<Object>("ID", StdClass.getValue(uinfo, "ID")),
                                new ArrayEntry<Object>("user_login", name),
                                new ArrayEntry<Object>("user_nicename", RealName),
                                new ArrayEntry<Object>("user_email", email),
                                new ArrayEntry<Object>("user_url", "http://"),
                                new ArrayEntry<Object>("display_name", name)));
                } else {
                    ret_id = getIncluded(RegistrationPage.class, gVars, gConsts).wp_insert_user(
                            new Array<Object>(
                                new ArrayEntry<Object>("user_login", name),
                                new ArrayEntry<Object>("user_nicename", RealName),
                                new ArrayEntry<Object>("user_email", email),
                                new ArrayEntry<Object>("user_url", "http://"),
                                new ArrayEntry<Object>("display_name", name)));
                }

                txpid2wpid.putValue(user_id, ret_id);
                
				// Set Textpattern-to-WordPress permissions translation
                transperms = new Array<Object>(new ArrayEntry<Object>(1, "10"), new ArrayEntry<Object>(2, "9"), new ArrayEntry<Object>(3, "5"), new ArrayEntry<Object>(4, "4"),
                        new ArrayEntry<Object>(5, "3"), new ArrayEntry<Object>(6, "2"), new ArrayEntry<Object>(7, "0"));
                
				// Update Usermeta Data
                user = new WP_User(gVars, gConsts, ret_id);

                if (equal("10", transperms.getValue(privs))) {
                    user.set_role("administrator");
                }

                if (equal("9", transperms.getValue(privs))) {
                    user.set_role("editor");
                }

                if (equal("5", transperms.getValue(privs))) {
                    user.set_role("editor");
                }

                if (equal("4", transperms.getValue(privs))) {
                    user.set_role("author");
                }

                if (equal("3", transperms.getValue(privs))) {
                    user.set_role("contributor");
                }

                if (equal("2", transperms.getValue(privs))) {
                    user.set_role("contributor");
                }

                if (equal("0", transperms.getValue(privs))) {
                    user.set_role("subscriber");
                }

                getIncluded(UserPage.class, gVars, gConsts).update_usermeta(ret_id, "wp_user_level", transperms.getValue(privs));
                getIncluded(UserPage.class, gVars, gConsts).update_usermeta(ret_id, "rich_editing", "false");
            }// End foreach($users as $user)

			// Store id translation array for future use
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txpid2wpid", txpid2wpid, "", "yes");
            echo(gVars.webEnv, "<p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Done! <strong>%1$s</strong> users imported.", "default"), count) + "<br /><br /></p>");

            return true;
        }// End if(is_array($users)

        echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("No Users to Import!", "default"));

        return false;
    }// End function user2wp()

    public Object posts2wp(Object posts)/* Do not change type */
     {
        int count = 0;
        Array<Object> txpposts2wpposts = new Array<Object>();
        Array<Object> cats = new Array<Object>();
        Array<Object> post = null;
        Array<Object> stattrans = new Array<Object>();
        Object uinfo;

        /* Do not change type */
        String AuthorID = null;
        int authorid = 0;
        String Title = null;
        String Body = null;
        String Excerpt = null;
        Object post_status = null;
        Object Status = null;
        int pinfo = 0;
        Object ret_id = 0;
        Object Posted = null;
        Object post_date_gmt = null;
        Object LastMod = null;
        Object post_modified_gmt = null;
        Object url_title = null;
        Object comments_count = null;
        Object ID = null;
        Object category1 = null;
        Object Category1 = null;
        Object category2 = null;
        Object Category2 = null;
        Object cat1 = null;
        Object cat2 = null;
        
		// General Housekeeping
        count = 0;
        txpposts2wpposts = new Array<Object>();
        cats = new Array<Object>();

		// Do the Magic
        if (is_array(posts)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Posts...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry76 : ((Array<?>) posts).entrySet()) {
                post = (Array<Object>) javaEntry76.getValue();
                
                count++;
                AuthorID = strval(Array.extractVar(post, "AuthorID", AuthorID, Array.EXTR_OVERWRITE));
                Title = strval(Array.extractVar(post, "Title", Title, Array.EXTR_OVERWRITE));
                Body = strval(Array.extractVar(post, "Body", Body, Array.EXTR_OVERWRITE));
                Excerpt = strval(Array.extractVar(post, "Excerpt", Excerpt, Array.EXTR_OVERWRITE));
                Status = Array.extractVar(post, "Status", Status, Array.EXTR_OVERWRITE);
                Posted = Array.extractVar(post, "Posted", Posted, Array.EXTR_OVERWRITE);
                post_date_gmt = Array.extractVar(post, "post_date_gmt", post_date_gmt, Array.EXTR_OVERWRITE);
                LastMod = Array.extractVar(post, "LastMod", LastMod, Array.EXTR_OVERWRITE);
                post_modified_gmt = Array.extractVar(post, "post_modified_gmt", post_modified_gmt, Array.EXTR_OVERWRITE);
                url_title = Array.extractVar(post, "url_title", url_title, Array.EXTR_OVERWRITE);
                comments_count = Array.extractVar(post, "comments_count", comments_count, Array.EXTR_OVERWRITE);
                ID = Array.extractVar(post, "ID", ID, Array.EXTR_OVERWRITE);
                Category1 = Array.extractVar(post, "Category1", Category1, Array.EXTR_OVERWRITE);
                Category2 = Array.extractVar(post, "Category2", Category2, Array.EXTR_OVERWRITE);
                
				// Set Textpattern-to-WordPress status translation
                stattrans = new Array<Object>(
                        new ArrayEntry<Object>(1, "draft"),
                        new ArrayEntry<Object>(2, "private"),
                        new ArrayEntry<Object>(3, "draft"),
                        new ArrayEntry<Object>(4, "publish"),
                        new ArrayEntry<Object>(5, "publish"));
                
				//Can we do this more efficiently?
                uinfo = (booleanval(getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(AuthorID))
                    ? getIncluded(PluggablePage.class, gVars, gConsts).get_userdatabylogin(AuthorID)
                    : 1);
                authorid = intval(is_object(uinfo)
                        ? ((StdClass) uinfo).fields.getValue("ID")
                        : uinfo);
                
                Title = gVars.wpdb.escape(Title);
                Body = gVars.wpdb.escape(Body);
                Excerpt = gVars.wpdb.escape(Excerpt);
                post_status = stattrans.getValue(Status);

				// Import Post data into WordPress
                
                if (booleanval(pinfo = getIncluded(PostPage.class, gVars, gConsts).post_exists(Title, Body, ""))) {
                    ret_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(
                            new Array<Object>(
                                new ArrayEntry<Object>("ID", pinfo),
                                new ArrayEntry<Object>("post_date", Posted),
                                new ArrayEntry<Object>("post_date_gmt", post_date_gmt),
                                new ArrayEntry<Object>("post_author", authorid),
                                new ArrayEntry<Object>("post_modified", LastMod),
                                new ArrayEntry<Object>("post_modified_gmt", post_modified_gmt),
                                new ArrayEntry<Object>("post_title", Title),
                                new ArrayEntry<Object>("post_content", Body),
                                new ArrayEntry<Object>("post_excerpt", Excerpt),
                                new ArrayEntry<Object>("post_status", post_status),
                                new ArrayEntry<Object>("post_name", url_title),
                                new ArrayEntry<Object>("comment_count", comments_count)));

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ret_id)) {
                        return ret_id;
                    }
                } else {
                    ret_id = (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_insert_post(
                            new Array<Object>(
                                new ArrayEntry<Object>("post_date", Posted),
                                new ArrayEntry<Object>("post_date_gmt", post_date_gmt),
                                new ArrayEntry<Object>("post_author", authorid),
                                new ArrayEntry<Object>("post_modified", LastMod),
                                new ArrayEntry<Object>("post_modified_gmt", post_modified_gmt),
                                new ArrayEntry<Object>("post_title", Title),
                                new ArrayEntry<Object>("post_content", Body),
                                new ArrayEntry<Object>("post_excerpt", Excerpt),
                                new ArrayEntry<Object>("post_status", post_status),
                                new ArrayEntry<Object>("post_name", url_title),
                                new ArrayEntry<Object>("comment_count", comments_count)));

                    if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ret_id)) {
                        return ret_id;
                    }
                }

                txpposts2wpposts.putValue(ID, ret_id);
                
				// Make Post-to-Category associations
                cats = new Array<Object>();

                StdClass category1Obj = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category_by_slug(Category1);
                category1 = StdClass.getValue(category1Obj, "term_id");

                StdClass category2Obj = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category_by_slug(Category2);

                // TODO Bug?
                category2 = StdClass.getValue(category1Obj, "term_id");

                if (booleanval(cat1 = category1)) {
                    cats.putValue(1, cat1);
                }

                if (booleanval(cat2 = category2)) {
                    cats.putValue(2, cat2);
                }

                if (!empty(cats)) {
                    (((org.numiton.nwp.wp_includes.PostPage) getIncluded(org.numiton.nwp.wp_includes.PostPage.class, gVars, gConsts))).wp_set_post_categories(intval(ret_id), cats);
                }
            }
        }

		// Store ID translation for later use
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txpposts2wpposts", txpposts2wpposts, "", "yes");
        echo(gVars.webEnv, "<p>" + QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Done! <strong>%1$s</strong> posts imported.", "default"), count) + "<br /><br /></p>");

        return true;
    }

    public boolean comments2wp(Object comments)/* Do not change type */
     {
        int count = 0;
        Array<Object> txpcm2wpcm = new Array<Object>();
        Array<Object> postarr = new Array<Object>();
        Array<Object> comment = null;
        String comment_ID = null;
        String discussid = null;
        Object comment_post_ID = null;
        Object parentid = null;
        int comment_approved = 0;
        Object visible = null;
        String name = null;
        String email = null;
        String web = null;
        String message = null;
        Object cinfo = null;
        Object posted = null;
        int ret_id = 0;
        Object ip = null;
        
		// General Housekeeping
        count = 0;
        txpcm2wpcm = new Array<Object>();
        
        postarr = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpposts2wpposts");

		// Magic Mojo
        if (is_array(comments)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Comments...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry77 : ((Array<?>) comments).entrySet())/*
             * nWordPressify Data nWordPressify Data
             */
             {
                comment = (Array<Object>) javaEntry77.getValue();
                
                count++;
                discussid = strval(Array.extractVar(comment, "discussid", discussid, Array.EXTR_OVERWRITE));
                parentid = Array.extractVar(comment, "parentid", parentid, Array.EXTR_OVERWRITE);
                visible = Array.extractVar(comment, "visible", visible, Array.EXTR_OVERWRITE);
                name = strval(Array.extractVar(comment, "name", name, Array.EXTR_OVERWRITE));
                email = strval(Array.extractVar(comment, "email", email, Array.EXTR_OVERWRITE));
                web = strval(Array.extractVar(comment, "web", web, Array.EXTR_OVERWRITE));
                message = strval(Array.extractVar(comment, "message", message, Array.EXTR_OVERWRITE));
                posted = Array.extractVar(comment, "posted", posted, Array.EXTR_OVERWRITE);
                ip = Array.extractVar(comment, "ip", ip, Array.EXTR_OVERWRITE);
                
				// WordPressify Data
                comment_ID = Strings.ltrim(discussid, "0");
                comment_post_ID = postarr.getValue(parentid);
                comment_approved = (equal(1, visible)
                    ? 1
                    : 0);
                name = gVars.wpdb.escape(name);
                email = gVars.wpdb.escape(email);
                web = gVars.wpdb.escape(web);
                message = gVars.wpdb.escape(message);

                if (booleanval(cinfo = getIncluded(CommentPage.class, gVars, gConsts).comment_exists(name, strval(posted)))) {
					// Update comments
                    ret_id = (((org.numiton.nwp.wp_includes.CommentPage) getIncluded(org.numiton.nwp.wp_includes.CommentPage.class, gVars, gConsts))).wp_update_comment(
                                new Array<Object>(
                                    new ArrayEntry<Object>("comment_ID", cinfo),
                                    new ArrayEntry<Object>("comment_post_ID", comment_post_ID),
                                    new ArrayEntry<Object>("comment_author", name),
                                    new ArrayEntry<Object>("comment_author_email", email),
                                    new ArrayEntry<Object>("comment_author_url", web),
                                    new ArrayEntry<Object>("comment_date", posted),
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
                                    new ArrayEntry<Object>("comment_author_IP", ip),
                                    new ArrayEntry<Object>("comment_date", posted),
                                    new ArrayEntry<Object>("comment_content", message),
                                    new ArrayEntry<Object>("comment_approved", comment_approved)));
                }

                txpcm2wpcm.putValue(comment_ID, ret_id);
            }

			// Store Comment ID translation for future use
            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txpcm2wpcm", txpcm2wpcm, "", "yes");
            
			// Associate newly formed categories with posts
            getIncluded(TextpatternPage.class, gVars, gConsts).get_comment_count(ret_id);
            
            
            
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
        String category = null;
        String linkname = null;
        String description = null;
        Object linfo = null;
        int ret_id = 0;
        Object url = null;
        Object date = null;
        Array<Object> txplinks2wplinks = new Array<Object>();
        Object link_id = null;
        
		// General Housekeeping
        count = 0;

		// Deal with the links
        if (is_array(links)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Importing Links...", "default") + "<br /><br /></p>");

            for (Map.Entry javaEntry78 : ((Array<?>) links).entrySet()) {
                link = (Array<Object>) javaEntry78.getValue();
                
                count++;
                category = strval(Array.extractVar(link, "category", category, Array.EXTR_OVERWRITE));
                linkname = strval(Array.extractVar(link, "linkname", linkname, Array.EXTR_OVERWRITE));
                description = strval(Array.extractVar(link, "description", description, Array.EXTR_OVERWRITE));
                url = Array.extractVar(link, "url", url, Array.EXTR_OVERWRITE);
                date = Array.extractVar(link, "date", date, Array.EXTR_OVERWRITE);
                link_id = Array.extractVar(link, "link_id", link_id, Array.EXTR_OVERWRITE);
                
				// Make nice vars
                category = gVars.wpdb.escape(category);
                linkname = gVars.wpdb.escape(linkname);
                description = gVars.wpdb.escape(description);

                if (booleanval(linfo = commonInterface4.link_exists(linkname))) {
                    ret_id = getIncluded(BookmarkPage.class, gVars, gConsts).wp_insert_link(
                            new Array<Object>(
                                new ArrayEntry<Object>("link_id", linfo),
                                new ArrayEntry<Object>("link_url", url),
                                new ArrayEntry<Object>("link_name", linkname),
                                new ArrayEntry<Object>("link_category", category),
                                new ArrayEntry<Object>("link_description", description),
                                new ArrayEntry<Object>("link_updated", date)));
                } else {
                    ret_id = getIncluded(BookmarkPage.class, gVars, gConsts).wp_insert_link(
                            new Array<Object>(
                                new ArrayEntry<Object>("link_url", url),
                                new ArrayEntry<Object>("link_name", linkname),
                                new ArrayEntry<Object>("link_category", category),
                                new ArrayEntry<Object>("link_description", description),
                                new ArrayEntry<Object>("link_updated", date)));
                }

                txplinks2wplinks.putValue(link_id, ret_id);
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txplinks2wplinks", txplinks2wplinks, "", "yes");
            echo(gVars.webEnv, "<p>");
            QStrings.printf(
                gVars.webEnv,
                getIncluded(L10nPage.class, gVars, gConsts).__ngettext("Done! <strong>%s</strong> link imported", "Done! <strong>%s</strong> links imported", count, "default"),
                count);
            echo(gVars.webEnv, "<br /><br /></p>");

            return true;
        }

        echo(gVars.webEnv, getIncluded(L10nPage.class, gVars, gConsts).__("No Links to Import!", "default"));

        return false;
    }

    public void import_categories() {
        Array<Object> cats = new Array<Object>();
        
		// Category Import
        cats = this.get_txp_cats();
        this.cat2wp(cats);
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txp_cats", cats, "", "yes");
        
        
        
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=textpattern&amp;step=2\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-textpattern", "_wpnonce", true, true);
        QStrings.printf(
            gVars.webEnv,
            "<input type=\"submit\" class=\"button\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Users", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public void import_users() {
        Array<Object> users = new Array<Object>();
        
		// User Import
        users = this.get_txp_users();
        this.users2wp(users);
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=textpattern&amp;step=3\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-textpattern", "_wpnonce", true, true);
        QStrings.printf(
            gVars.webEnv,
            "<input type=\"submit\" class=\"button\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Posts", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public Object import_posts() {
        Array<Object> posts = new Array<Object>();
        Object result;
        
		// Post Import
        posts = this.get_txp_posts();
        result = this.posts2wp(posts);

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(result)) {
            return result;
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=textpattern&amp;step=4\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-textpattern", "_wpnonce", true, true);
        QStrings.printf(
            gVars.webEnv,
            "<input type=\"submit\" class=\"button\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Comments", "default")));
        echo(gVars.webEnv, "</form>");

        return 0;
    }

    public void import_comments() {
        Array<Object> comments = new Array<Object>();
        
		// Comment Import
        comments = this.get_txp_comments();
        this.comments2wp(comments);
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=textpattern&amp;step=5\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-textpattern", "_wpnonce", true, true);
        QStrings.printf(
            gVars.webEnv,
            "<input type=\"submit\" class=\"button\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Import Links", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public void import_links() {
        Array<Object> links = new Array<Object>();
        
		//Link Import
        links = this.get_txp_links();
        this.links2wp(links);
        getIncluded(FunctionsPage.class, gVars, gConsts).add_option("txp_links", links, "", "yes");
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=textpattern&amp;step=6\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-textpattern", "_wpnonce", true, true);
        QStrings.printf(
            gVars.webEnv,
            "<input type=\"submit\" class=\"button\" name=\"submit\" value=\"%s\" />",
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Finish", "default")));
        echo(gVars.webEnv, "</form>");
    }

    public void cleanup_txpimport() {
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("tpre");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txp_cats");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpid2wpid");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpcat2wpcat");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpposts2wpposts");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpcm2wpcm");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txplinks2wplinks");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpuser");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txppass");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpname");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txphost");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("import_done", "textpattern");
        this.tips();
    }

    public void tips() {
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "Welcome to nWordPress.  We hope (and expect!) that you will find this platform incredibly rewarding!  As a new nWordPress user coming from Textpattern, there are some things that we would like to point out.  Hopefully, they will help your transition go as smoothly as possible.",
                        "default") + "</p>");
        echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Users", "default") + "</h3>");
        echo(
                gVars.webEnv,
                "<p>" +
                QStrings.sprintf(
                        getIncluded(L10nPage.class, gVars, gConsts).__(
                                "You have already setup nWordPress and have been assigned an administrative login and password.  Forget it.  You didn&#8217;t have that login in Textpattern, why should you have it here?  Instead we have taken care to import all of your users into our system.  Unfortunately there is one downside.  Because both nWordPress and Textpattern uses a strong encryption hash with passwords, it is impossible to decrypt it and we are forced to assign temporary passwords to all your users.  <strong>Every user has the same username, but their passwords are reset to password123.</strong>  So <a href=\"%1$s\">Login</a> and change it.",
                                "default"),
                        getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("wpurl", "raw") + "/wp-login.php") + "</p>");
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
                        "Also, since you&#8217;re coming from Textpattern, you probably have been using Textile to format your comments and posts.  If this is the case, we recommend downloading and installing <a href=\"http://www.huddledmasses.org/category/development/wordpress/textile/\">Textile for WordPress</a>.  Trust me... You&#8217;ll want it.",
                        "default") + "</p>");
        echo(gVars.webEnv, "<h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("WordPress Resources", "default") + "</h3>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Finally, there are numerous WordPress resources around the internet.  Some of them are:", "default") + "</p>");
        echo(gVars.webEnv, "<ul>");
        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://www.wordpress.org\">The official WordPress site</a>", "default") + "</li>");
        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://wordpress.org/support/\">The WordPress support forums</a>", "default") + "</li>");
        echo(gVars.webEnv, "<li>" + getIncluded(L10nPage.class, gVars, gConsts).__("<a href=\"http://codex.wordpress.org\">The Codex (In other words, the WordPress Bible)</a>", "default") + "</li>");
        echo(gVars.webEnv, "</ul>");
        echo(gVars.webEnv,
            "<p>" +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__("That&#8217;s it! What are you waiting for? Go <a href=\"%1$s\">login</a>!", "default"),
                getIncluded(General_templatePage.class, gVars, gConsts).get_bloginfo("wpurl", "raw") + "/wp-login.php") + "</p>");
    }

    public void db_form() {
        echo(gVars.webEnv, "<table class=\"form-table\">");
        QStrings.printf(
            gVars.webEnv,
            "<tr><th scope=\"row\"><label for=\"dbuser\">%s</label></th><td><input type=\"text\" name=\"dbuser\" id=\"dbuser\" /></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("Textpattern Database User:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th scope=\"row\"><label for=\"dbpass\">%s</label></th><td><input type=\"password\" name=\"dbpass\" id=\"dbpass\" /></td></tr>",
            (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Textpattern Database Password:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th scope=\"row\"><label for=\"dbname\">%s</label></th><td><input type=\"text\" id=\"dbname\" name=\"dbname\" /></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("Textpattern Database Name:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th scope=\"row\"><label for=\"dbhost\">%s</label></th><td><input type=\"text\" id=\"dbhost\" name=\"dbhost\" value=\"localhost\" /></td></tr>",
            getIncluded(L10nPage.class, gVars, gConsts).__("Textpattern Database Host:", "default"));
        QStrings.printf(
            gVars.webEnv,
            "<tr><th scope=\"row\"><label for=\"dbprefix\">%s</label></th><td><input type=\"text\" name=\"dbprefix\" id=\"dbprefix\"  /></td></tr>",
            (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Textpattern Table prefix (if any):", "default"));
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
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-textpattern", "_wpnonce");

            if (booleanval(gVars.webEnv._POST.getValue("dbuser"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpuser"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpuser");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("txpuser", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbuser")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbpass"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txppass"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txppass");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("txppass", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbpass")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbname"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txpname"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txpname");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("txpname", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbname")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbhost"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("txphost"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("txphost");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("txphost", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbhost")), true), "", "yes");
            }

            if (booleanval(gVars.webEnv._POST.getValue("dbprefix"))) {
                if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("tpre"))) {
                    getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("tpre");
                }

                getIncluded(FunctionsPage.class, gVars, gConsts)
                    .add_option("tpre", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_user(strval(gVars.webEnv._POST.getValue("dbprefix")), false), "", "yes");
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
            this.cleanup_txpimport();

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
