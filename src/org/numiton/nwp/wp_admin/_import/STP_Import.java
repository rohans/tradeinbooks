/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: STP_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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


public class STP_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(STP_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public STP_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
		// Nothing.
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Simple Tagging", "default") + "</h2>");
        echo(
            gVars.webEnv,
            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Steps may take a few minutes depending on the size of your database. Please be patient.", "default") + "<br /><br /></p>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This imports tags from Simple Tagging 1.6.2 into nWordPress tags.", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("This has not been tested on any other versions of Simple Tagging. Mileage may vary.", "default") + "</p>");
        echo(
                gVars.webEnv,
                "<p>" +
                getIncluded(L10nPage.class, gVars, gConsts).__(
                        "To accommodate larger databases for those tag-crazy authors out there, we have made this into an easy 4-step program to help you kick that nasty Simple Tagging habit. Just keep clicking along and we will let you know when you are in the clear!",
                        "default") + "</p>");
        echo(gVars.webEnv, "<p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Don&#8217;t be stupid - backup your database before proceeding!", "default") + "</strong></p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=stp&amp;step=1\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-stp", "_wpnonce", true, true);
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

		// load the header
        this.header();

        switch (step) {
        case 0: {
            this.greet();

            break;
        }

        case 1: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-stp", "_wpnonce");
            this.import_posts();

            break;
        }

        case 2: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-stp", "_wpnonce");
            this.import_t2p();

            break;
        }

        case 3: {
            getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-stp", "_wpnonce");
            this.cleanup_import();

            break;
        }
        }

		// load the footer
        this.footer();
    }

    public boolean import_posts() {
        Object posts; /* Do not change type */
        int count = 0;
        
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Reading STP Post Tags&#8230;", "default") + "</h3></p>");
        
		// read in all the STP tag -> post settings
        posts = this.get_stp_posts();

		// if we didn't get any tags back, that's all there is folks!
        if (!is_array(posts)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No posts were found to have tags!", "default") + "</p>");

            return false;
        } else {
			// if there's an existing entry, delete it
            if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("stpimp_posts"))) {
                getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("stpimp_posts");
            }

            getIncluded(FunctionsPage.class, gVars, gConsts).add_option("stpimp_posts", posts, "", "yes");
            count = Array.count(posts);
            echo(gVars.webEnv,
                "<p>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                        "Done! <strong>%s</strong> tag to post relationships were read.",
                        "Done! <strong>%s</strong> tags to post relationships were read.",
                        count,
                        "default"), count) + "<br /></p>");
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=stp&amp;step=2\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-stp", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Step 2", "default") + "\" /></p>");
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
                getIncluded(L10nPage.class, gVars, gConsts).__ngettext("Done! <strong>%s</strong> tag was added!", "Done! <strong>%s</strong> tags were added!", tags_added, "default"),
                tags_added) + "<br /></p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=stp&amp;step=3\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-stp", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Step 3", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public Array<Object> get_stp_posts() {
        String posts_query = null;
        Array<Object> posts = new Array<Object>();
        
		// read in all the posts from the STP post->tag table: should be wp_post2tag
        posts_query = "SELECT post_id, tag_name FROM " + gVars.wpdb.prefix + "stp_tags";
        posts = gVars.wpdb.get_results(posts_query);

        return posts;
    }

    public int tag2post() {
        Array<Object> posts = null;
        int tags_added = 0;
        int the_post = 0;
        StdClass this_post = null;
        String the_tag = null;
        
		// get the tags and posts we imported in the last 2 steps
        posts = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("stpimp_posts");
        
		// null out our results
        tags_added = 0;

		// loop through each post and add its tags to the db
        for (Map.Entry javaEntry73 : posts.entrySet()) {
            this_post = (StdClass) javaEntry73.getValue();
            the_post = intval(StdClass.getValue(this_post, "post_id"));
            the_tag = gVars.wpdb.escape(strval(StdClass.getValue(this_post, "tag_name")));
			// try to add the tag
            getIncluded(PostPage.class, gVars, gConsts).wp_add_post_tags(the_post, the_tag);
            tags_added++;
        }

		// that's it, all posts should be linked to their tags properly, pending any errors we just spit out!
        return tags_added;
    }

    public void cleanup_import() {
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("stpimp_posts");
        this.done();
    }

    public void done() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Complete!", "default") + "</h3></p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("OK, so we lied about this being a 4-step program! You&#8217;re done!", "default") + "</p>");
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
