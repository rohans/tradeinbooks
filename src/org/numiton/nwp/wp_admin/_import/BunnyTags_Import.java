/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: BunnyTags_Import.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import com.numiton.string.Strings;


public class BunnyTags_Import implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(BunnyTags_Import.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    public BunnyTags_Import(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
    }

    public void header() {
        echo(gVars.webEnv, "<div class=\"wrap\">");
        echo(gVars.webEnv, "<h2>" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Bunny&#8217;s Technorati Tags", "default") + "</h2>");
        echo(
            gVars.webEnv,
            "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Steps may take a few minutes depending on the size of your database. Please be patient.", "default") + "<br /><br /></p>");
    }

    public void footer() {
        echo(gVars.webEnv, "</div>");
    }

    public void greet() {
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("Howdy! This imports tags from Bunny&#8217;s Technorati Tags into nWordPress tags.", "default") + "</p>");
        echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("This is suitable for Bunny&#8217;s Technorati Tags version 0.6.", "default") + "</p>");
        echo(gVars.webEnv, "<p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("All existing Bunny&#8217;s Technorati Tags will be removed after import.", "default") + "</strong></p>");
        echo(gVars.webEnv, "<p><strong>" + getIncluded(L10nPage.class, gVars, gConsts).__("Don&#8217;t be stupid - backup your database before proceeding!", "default") + "</strong></p>");
        echo(gVars.webEnv, "<form action=\"admin.php?import=btt&amp;step=1\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-btt", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Import Tags", "default") + "\" /></p>");
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
            int javaSwitchSelector8 = 0;

            if (equal(step, 0)) {
                javaSwitchSelector8 = 1;
            }

            if (equal(step, 1)) {
                javaSwitchSelector8 = 2;
            }

            if (equal(step, 2)) {
                javaSwitchSelector8 = 3;
            }

            if (equal(step, 3)) {
                javaSwitchSelector8 = 4;
            }

            switch (javaSwitchSelector8) {
            case 1: {
                this.greet();

                break;
            }

            case 2: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-btt", "_wpnonce");
                this.check_post_keyword(true);

                break;
            }

            case 3: {
                getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("import-btt", "_wpnonce");
                this.check_post_keyword(false);

                break;
            }

            case 4: {
                this.done();

                break;
            }
            }
        }

		// load the footer
        this.footer();
    }

    public boolean check_post_keyword(boolean precheck) {
        Object metakeys; /* Do not change type */
        int count = 0;
        StdClass post_meta = null;
        Array<String> post_keys = new Array<String>();
        String keyword = null;
        
        echo(gVars.webEnv, "<div class=\"narrow\">");
        echo(gVars.webEnv, "<p><h3>" + getIncluded(L10nPage.class, gVars, gConsts).__("Reading Bunny&#8217;s Technorati Tags&#8230;", "default") + "</h3></p>");

        // import Bunny's Keywords tags
        metakeys = gVars.wpdb.get_results("SELECT post_id, meta_id, meta_key, meta_value FROM " + gVars.wpdb.postmeta + " WHERE " + gVars.wpdb.postmeta + ".meta_key = \'tags\'");

        if (!is_array(metakeys)) {
            echo(gVars.webEnv, "<p>" + getIncluded(L10nPage.class, gVars, gConsts).__("No Tags Found!", "default") + "</p>");

            return false;
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

            for (Map.Entry javaEntry50 : ((Array<?>) metakeys).entrySet()) {
                post_meta = (StdClass) javaEntry50.getValue();

                if (!equal(StdClass.getValue(post_meta, "meta_value"), "")) {
                    post_keys = Strings.explode(" ", strval(StdClass.getValue(post_meta, "meta_value")));

                    for (Map.Entry javaEntry51 : post_keys.entrySet()) {
                        keyword = strval(javaEntry51.getValue());
                        keyword = Strings.addslashes(gVars.webEnv, Strings.trim(Strings.str_replace("+", " ", keyword)));

                        if (!equal("", keyword)) {
                            echo(gVars.webEnv, "<li>" + StdClass.getValue(post_meta, "post_id") + "&nbsp;-&nbsp;" + keyword + "</li>");

                            if (!precheck) {
                                getIncluded(PostPage.class, gVars, gConsts).wp_add_post_tags(intval(StdClass.getValue(post_meta, "post_id")), keyword);
                            }
                        }
                    }
                }

                if (!precheck) {
                    getIncluded(PostPage.class, gVars, gConsts).delete_post_meta(intval(StdClass.getValue(post_meta, "post_id")), "tags", "");
                }
            }

            echo(gVars.webEnv, "</ul>");
        }

        echo(gVars.webEnv, "<form action=\"admin.php?import=btt&amp;step=" + strval(precheck
                ? 2
                : 3) + "\" method=\"post\">");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("import-btt", "_wpnonce", true, true);
        echo(gVars.webEnv, "<p class=\"submit\"><input type=\"submit\" name=\"submit\" value=\"" + getIncluded(L10nPage.class, gVars, gConsts).__("Next", "default") + "\" /></p>");
        echo(gVars.webEnv, "</form>");
        echo(gVars.webEnv, "</div>");

        return false;
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
