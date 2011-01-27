/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: UTW_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_includes.*;

import com.numiton.array.Array;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;


public class UTW_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(UTW_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public UTW_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Ultimate Tag Warrior", "default") + "</h2>");
        echo(
            gVars.webEnv,
            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Steps may take a few minutes depending on the size of your database. Please be patient.", "default") + "<br /><br /></p>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This imports tags from Ultimate Tag Warrior 3 into nWordPress tags.", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("This has not been tested on any other versions of Ultimate Tag Warrior. Mileage may vary.", "default") + "</p>");
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "To accommodate larger databases for those tag-crazy authors out there, we have made this into an easy 5-step program to help you kick that nasty UTW habit. Just keep clicking along and we will let you know when you are in the clear!",
                        "default") + "</p>");
        echo(gVars.webEnv, "<p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Don&#8217;t be stupid - backup your database before proceeding!", "default") + "</strong></p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=utw&amp;step=1\" method=\"post\">");
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Step 1", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public void dispatch() {
        int step = 0;

        if (empty(gVars.webEnv._GET.getValue("step"))) {
            step = 0;
        } else {
            step = intval(gVars.webEnv._GET.getValue("step"));
        }

        if (step > 1) {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-utw", "_wpnonce");
        }

		// load the header
        this.header();

        switch (step) {
        case 0: {
            this.greet();

            break;
        }

        case 1: {
            this.import_tags();

            break;
        }

        case 2: {
            this.import_posts();

            break;
        }

        case 3: {
            this.import_t2p();

            break;
        }

        case 4: {
            this.cleanup_import();

            break;
        }
        }

		// load the footer
        this.footer();
    }

    public boolean import_tags() {
        Object tags; /* Do not change type */
        int count = 0;
        Object tag_name = null;
        Object tag_id = null;
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Reading UTW Tags&#8230;", "default") + "</h3></p>");
        tags = this.get_utw_tags();

		// if we didn't get any tags back, that's all there is folks!
        if (!is_array(tags)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No Tags Found!", "default") + "</p>");

            return false;
        } else {
        	
			// if there's an existing entry, delete it
            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("utwimp_tags"))) {
                getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("utwimp_tags");
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("utwimp_tags", tags, "", "yes");
            count = Array.count(tags);
            echo(
                gVars.webEnv,
                "<p>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("Done! <strong>%s</strong> tag were read.", "Done! <strong>%s</strong> tags were read.", count, "default"),
                    count) + "<br /></p>");
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("The following tags were found:", "default") + "</p>");
            echo(gVars.webEnv, "<ul>");

            for (Map.Entry javaEntry79 : ((Array<?>) tags).entrySet()) {
                tag_id = javaEntry79.getKey();
                tag_name = javaEntry79.getValue();
                echo(gVars.webEnv, "<li>" + strval(tag_name) + "</li>");
            }

            echo(gVars.webEnv, "</ul>");
            echo(gVars.webEnv, "<br />");
            echo(
                    gVars.webEnv,
                    "<p>" +
                    getIncluded(L10nPage.class, gVars, gConsts).__(
                            "If you don&#8217;t want to import any of these tags, you should delete them from the UTW tag management page and then re-run this import.",
                            "default") + "</p>");
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=utw&amp;step=2\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-utw", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Step 2", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");

        return false;
    }

    public boolean import_posts() {
        Object posts; /* Do not change type */
        int count = 0;
        
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Reading UTW Post Tags&#8230;", "default") + "</h3></p>");
        
		// read in all the UTW tag -> post settings
        posts = this.get_utw_posts();

		// if we didn't get any tags back, that's all there is folks!
        if (!is_array(posts)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No posts were found to have tags!", "default") + "</p>");

            return false;
        } else {

			// if there's an existing entry, delete it
            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("utwimp_posts"))) {
                getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("utwimp_posts");
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("utwimp_posts", posts, "", "yes");
            count = Array.count(posts);
            echo(gVars.webEnv,
                "<p>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                        "Done! <strong>%s</strong> tag to post relationships were read.",
                        "Done! <strong>%s</strong> tags to post relationships were read.",
                        count,
                        "default"), count) + "<br /></p>");
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=utw&amp;step=3\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-utw", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Step 3", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");

        return false;
    }

    public void import_t2p() {
        int tags_added = 0;
        
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Adding Tags to Posts&#8230;", "default") + "</h3></p>");
        
		// run that funky magic!
        tags_added = this.tag2post();
        
        echo(
            gVars.webEnv,
            "<p>" +
            QStrings.sprintf(
                getIncluded(L10nPage.class, gVars, gConsts).__ngettext("Done! <strong>%s</strong> tag were added!", "Done! <strong>%s</strong> tags were added!", tags_added, "default"),
                tags_added) + "<br /></p>");
        
        echo(gVars.webEnv, "<form action=\"admin.php?import=utw&amp;step=4\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-utw", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Step 4", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public Array<Object> get_utw_tags() {
        String tags_query = null;
        Array<Object> tags = new Array<Object>();
        Array<Object> new_tags = new Array<Object>();
        StdClass tag = null;
        
		// read in all the tags from the UTW tags table: should be wp_tags
        tags_query = "SELECT tag_id, tag FROM " + gVars.wpdb.prefix + "tags";
        
        tags = gVars.wpdb.get_results(tags_query);

		// rearrange these tags into something we can actually use
        for (Map.Entry javaEntry80 : tags.entrySet()) {
            tag = (StdClass) javaEntry80.getValue();
            new_tags.putValue(StdClass.getValue(tag, "tag_id"), StdClass.getValue(tag, "tag"));
        }

        return new_tags;
    }

    public Array<Object> get_utw_posts() {
        String posts_query = null;
        Array<Object> posts = new Array<Object>();
        posts_query = "SELECT tag_id, post_id FROM " + gVars.wpdb.prefix + "post2tag";
        posts = gVars.wpdb.get_results(posts_query);

        return posts;
    }

    public int tag2post() {
        Array<Object> tags = new Array<Object>();
        Array<Object> posts = null;
        int tags_added = 0;
        int the_post = 0;
        StdClass this_post = null;
        int the_tag = 0;
        
		// get the tags and posts we imported in the last 2 steps
        tags = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("utwimp_tags");
        posts = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("utwimp_posts");
        
		// null out our results
        tags_added = 0;

		// loop through each post and add its tags to the db
        for (Map.Entry javaEntry81 : posts.entrySet()) {
            this_post = (StdClass) javaEntry81.getValue();
            
            the_post = intval(StdClass.getValue(this_post, "post_id"));
            the_tag = intval(StdClass.getValue(this_post, "tag_id"));
            
			// what's the tag name for that id?
            the_tag = intval(tags.getValue(the_tag));
            
			// screw it, just try to add the tag
            getIncluded(PostPage.class, gVars, gConsts).wp_add_post_tags(the_post, strval(the_tag));
            
            tags_added++;
        }

		// that's it, all posts should be linked to their tags properly, pending any errors we just spit out!
        return tags_added;
    }

    public void cleanup_import() {
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("utwimp_tags");
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("utwimp_posts");
        this.done();
    }

    public void done() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Complete!", "default") + "</h3></p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("OK, so we lied about this being a 5-step program! You&#8217;re done!", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Now wasn&#8217;t that easy?", "default") + "</p>");
        echo(gVars.webEnv, "</div>");
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
