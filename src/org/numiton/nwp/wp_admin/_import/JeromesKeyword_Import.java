/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: JeromesKeyword_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class JeromesKeyword_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(JeromesKeyword_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public JeromesKeyword_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Jerome&#8217;s Keywords", "default") + "</h2>");
        echo(
            gVars.webEnv,
            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Steps may take a few minutes depending on the size of your database. Please be patient.", "default") + "<br /><br /></p>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This imports tags from Jerome&#8217;s Keywords into nWordPress tags.", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("This is suitable for Jerome&#8217;s Keywords version 1.x and 2.0a.", "default") + "</p>");
        echo(gVars.webEnv, "<p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("All existing Jerome&#8217;s Keywords will be removed after import.", "default") + "</strong></p>");
        echo(gVars.webEnv, "<p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Don&#8217;t be stupid - backup your database before proceeding!", "default") + "</strong></p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=jkw&amp;step=1\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-jkw", "_wpnonce", true, true);
        echo(
            gVars.webEnv,
            "<p class=\"submit\"><input type=\"submit\" name=\"submit\" class=\"button\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Version 1.x", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=jkw&amp;step=3\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-jkw", "_wpnonce", true, true);
        echo(
            gVars.webEnv,
            "<p class=\"submit\"><input type=\"submit\" name=\"submit\" class=\"button\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Version 2.0a", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public void dispatch() {
        int step = 0;

        if (empty(gVars.webEnv._GET.getValue("step"))) {
            step = 0;
        } else {
            step = getIncluded(FunctionsPage.class, gVars, gConsts).absint(gVars.webEnv._GET.getValue("step"));
        }

		// load the header
        this.header();

        {
            int javaSwitchSelector9 = 0;

            if (equal(step, 0)) {
                javaSwitchSelector9 = 1;
            }

            if (equal(step, 1)) {
                javaSwitchSelector9 = 2;
            }

            if (equal(step, 2)) {
                javaSwitchSelector9 = 3;
            }

            if (equal(step, 3)) {
                javaSwitchSelector9 = 4;
            }

            if (equal(step, 4)) {
                javaSwitchSelector9 = 5;
            }

            if (equal(step, 5)) {
                javaSwitchSelector9 = 6;
            }

            if (equal(step, 6)) {
                javaSwitchSelector9 = 7;
            }

            switch (javaSwitchSelector9) {
            case 1: {
                this.greet();

                break;
            }

            case 2: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-jkw", "_wpnonce");
                this.check_V1_post_keyword(true);

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-jkw", "_wpnonce");
                this.check_V1_post_keyword(false);

                break;
            }

            case 4: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-jkw", "_wpnonce");
                this.check_V2_post_keyword(true);

                break;
            }

            case 5: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-jkw", "_wpnonce");
                this.check_V2_post_keyword(false);

                break;
            }

            case 6: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-jkw", "_wpnonce");
                this.cleanup_V2_import();

                break;
            }

            case 7: {
                this.done();

                break;
            }
            }
        }

		// load the footer
        this.footer();
    }

    public void check_V1_post_keyword(boolean precheck) {
        Object metakeys; /* Do not change type */
        int count = 0;
        StdClass post_meta = null;
        Array<String> post_keys = new Array<String>();
        String keyword = null;
        
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Reading Jerome&#8217;s Keywords Tags&#8230;", "default") + "</h3></p>");
        
		// import Jerome's Keywords tags
        metakeys = gVars.wpdb.get_results("SELECT post_id, meta_id, meta_key, meta_value FROM " + gVars.wpdb.postmeta + " WHERE " + gVars.wpdb.postmeta + ".meta_key = \'keywords\'");

        if (!is_array(metakeys)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No Tags Found!", "default") + "</p>");

            return;
        } else {
            count = Array.count(metakeys);
            echo(gVars.webEnv,
                "<p>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                        "Done! <strong>%s</strong> post with tags were read.",
                        "Done! <strong>%s</strong> posts with tags were read.",
                        count,
                        "default"), count) + "<br /></p>");
            echo(gVars.webEnv, "<ul>");

            for (Map.Entry javaEntry58 : ((Array<?>) metakeys).entrySet()) {
                post_meta = (StdClass) javaEntry58.getValue();

                if (!equal(StdClass.getValue(post_meta, "meta_value"), "")) {
                    post_keys = Strings.explode(",", strval(StdClass.getValue(post_meta, "meta_value")));

                    for (Map.Entry javaEntry59 : post_keys.entrySet()) {
                        keyword = strval(javaEntry59.getValue());
                        keyword = Strings.addslashes(gVars.webEnv, Strings.trim(keyword));

                        if (!equal("", keyword)) {
                            echo(gVars.webEnv, "<li>" + StdClass.getValue(post_meta, "post_id") + "&nbsp;-&nbsp;" + keyword + "</li>");

                            if (!precheck) {
                                getIncluded(PostPage.class, gVars, gConsts).wp_add_post_tags(intval(StdClass.getValue(post_meta, "post_id")), keyword);
                            }
                        }
                    }
                }

                if (!precheck) {
                    getIncluded(PostPage.class, gVars, gConsts).delete_post_meta(intval(StdClass.getValue(post_meta, "post_id")), "keywords", "");
                }
            }

            echo(gVars.webEnv, "</ul>");
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=jkw&amp;step=" + strval(precheck
                ? 2
                : 6) + "\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-jkw", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Next", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public void check_V2_post_keyword(boolean precheck) {
        String tablename = null;
        Object metakeys /* Do not change type */;
        int count = 0;
        String keyword = null;
        StdClass post_meta = null;

        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Reading Jerome&#8217;s Keywords Tags&#8230;", "default") + "</h3></p>");
        
		// import Jerome's Keywords tags
        tablename = gVars.wpdb.prefix + Strings.substr(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("jkeywords_keywords_table")), 1, -1);
        metakeys = gVars.wpdb.get_results("SELECT post_id, tag_name FROM " + tablename);

        if (!is_array(metakeys)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No Tags Found!", "default") + "</p>");

            return;
        } else {
            count = Array.count(metakeys);
            echo(
                gVars.webEnv,
                "<p>" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext("Done! <strong>%s</strong> tag were read.", "Done! <strong>%s</strong> tags were read.", count, "default"),
                    count) + "<br /></p>");
            echo(gVars.webEnv, "<ul>");

            for (Map.Entry javaEntry60 : ((Array<?>) metakeys).entrySet()) {
                post_meta = (StdClass) javaEntry60.getValue();
                keyword = Strings.addslashes(gVars.webEnv, Strings.trim(strval(StdClass.getValue(post_meta, "tag_name"))));

                if (!equal(keyword, "")) {
                    echo(gVars.webEnv, "<li>" + StdClass.getValue(post_meta, "post_id") + "&nbsp;-&nbsp;" + keyword + "</li>");

                    if (!precheck) {
                        getIncluded(PostPage.class, gVars, gConsts).wp_add_post_tags(intval(StdClass.getValue(post_meta, "post_id")), keyword);
                    }
                }
            }

            echo(gVars.webEnv, "</ul>");
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=jkw&amp;step=" + strval(precheck
                ? 4
                : 5) + "\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-jkw", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Next", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");
    }

    public void cleanup_V2_import() {
        Object o = null;
        
		/* options from V2.0a (jeromes-keywords.php) */
        Array<String> options = new Array<String>(new ArrayEntry<String>("version"), new ArrayEntry<String>("keywords_table"), new ArrayEntry<String>("query_varname"),
                new ArrayEntry<String>("template"), new ArrayEntry<String>("meta_always_include"), new ArrayEntry<String>("meta_includecats"), new ArrayEntry<String>("meta_autoheader"),
                new ArrayEntry<String>("search_strict"), new ArrayEntry<String>("use_feed_cats"), new ArrayEntry<String>("post_linkformat"), new ArrayEntry<String>("post_tagseparator"),
                new ArrayEntry<String>("post_includecats"), new ArrayEntry<String>("post_notagstext"), new ArrayEntry<String>("cloud_linkformat"), new ArrayEntry<String>("cloud_tagseparator"),
                new ArrayEntry<String>("cloud_includecats"), new ArrayEntry<String>("cloud_sortorder"), new ArrayEntry<String>("cloud_displaymax"), new ArrayEntry<String>("cloud_displaymin"),
                new ArrayEntry<String>("cloud_scalemax"), new ArrayEntry<String>("cloud_scalemin"));
        
        gVars.wpdb.query("DROP TABLE IF EXISTS " + gVars.wpdb.prefix + Strings.substr(strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("jkeywords_keywords_table")), 1, -1));

        for (Map.Entry javaEntry61 : options.entrySet()) {
            o = javaEntry61.getValue();
            getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("jkeywords_" + strval(o));
        }

        this.done();
    }

    public void done() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Complete!", "default") + "</h3></p>");
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
