/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_User_Search.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;
import org.numiton.nwp.wp_includes.General_templatePage;
import org.numiton.nwp.wp_includes.L10nPage;
import org.numiton.nwp.wp_includes.WP_Error;

import com.numiton.Math;
import com.numiton.URL;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.string.Strings;


//WP_User_Search class
//by Mark Jaquith
public class WP_User_Search implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_User_Search.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<Object> results = new Array<Object>();
    public String search_term;
    public int page;
    public Object role;
    public int raw_page;
    public int users_per_page = 50;
    public int first_user;
    public Object last_user;
    public String query_limit;
    public String query_sort;
    public String query_from_where;
    public int total_users_for_query = 0;
    public boolean too_many_total_users = false;
    public Object search_errors;
    public String paging_text;

    public WP_User_Search(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, Object search_term, Object page, Object role) { // constructor
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.search_term = strval(search_term);
        this.raw_page = (equal("", page)
            ? intval(false)
            : intval(page));
        this.page = (equal("", page)
            ? 1
            : intval(page));
        this.role = role;
        this.prepare_query();
        this.query();
        this.prepare_vars_for_template_usage();
        this.do_paging();
    }

    public void prepare_query() {
        String search_sql = null;
        Array<String> searches = new Array<String>();
        Object col = null;
        this.first_user = (this.page - 1) * this.users_per_page;
        this.query_limit = " LIMIT " + strval(this.first_user) + "," + strval(this.users_per_page);
        this.query_sort = " ORDER BY user_login";
        search_sql = "";

        if (booleanval(this.search_term)) {
            searches = new Array<String>();
            search_sql = "AND (";

            for (Map.Entry javaEntry259 : new Array<Object>(
                    new ArrayEntry<Object>("user_login"),
                    new ArrayEntry<Object>("user_nicename"),
                    new ArrayEntry<Object>("user_email"),
                    new ArrayEntry<Object>("user_url"),
                    new ArrayEntry<Object>("display_name")).entrySet()) {
                col = javaEntry259.getValue();
                searches.putValue(strval(col) + " LIKE \'%" + this.search_term + "%\'");
            }

            search_sql = search_sql + Strings.implode(" OR ", searches);
            search_sql = search_sql + ")";
        }

        this.query_from_where = "FROM " + gVars.wpdb.users;

        if (booleanval(this.role)) {
            this.query_from_where = this.query_from_where + " INNER JOIN " + gVars.wpdb.usermeta + " ON " + gVars.wpdb.users + ".ID = " + gVars.wpdb.usermeta + ".user_id WHERE " +
                gVars.wpdb.usermeta + ".meta_key = \'" + gVars.wpdb.prefix + "capabilities\' AND " + gVars.wpdb.usermeta + ".meta_value LIKE \'%" + strval(this.role) + "%\'";

            // TODO A dirty hack to search in the serialized form of the wpdb.usermeta. Won't work with Java serialization ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        } else {
            this.query_from_where = this.query_from_where + " WHERE 1=1";
        }

        this.query_from_where = this.query_from_where + " " + search_sql;
    }

    public void query() {
        this.results = gVars.wpdb.get_col("SELECT ID " + this.query_from_where + this.query_sort + this.query_limit);

        if (booleanval(this.results)) {
            this.total_users_for_query = intval(gVars.wpdb.get_var("SELECT COUNT(ID) " + this.query_from_where)); // no limit
        } else {
            this.search_errors = new WP_Error(gVars, gConsts, "no_matching_users_found", getIncluded(L10nPage.class, gVars, gConsts).__("No matching users were found!", "default"));
        }
    }

    public void prepare_vars_for_template_usage() {
        this.search_term = Strings.stripslashes(gVars.webEnv, this.search_term); // done with DB, from now on we want slashes gone
    }

    public void do_paging() {
        if (this.total_users_for_query > this.users_per_page) { // have to page the results
            this.paging_text = strval(
                    getIncluded(General_templatePage.class, gVars, gConsts).paginate_links(
                        new Array<Object>(
                            new ArrayEntry<Object>("total", Math.ceil(floatval(this.total_users_for_query) / floatval(this.users_per_page))),
                            new ArrayEntry<Object>("current", this.page),
                            new ArrayEntry<Object>("base", "users.php?%_%"),
                            new ArrayEntry<Object>("format", "userspage=%#%"),
                            new ArrayEntry<Object>("add_args", new Array<Object>(new ArrayEntry<Object>("usersearch", URL.urlencode(this.search_term)))))));
        }
    }

    public Array<Object> get_results() {
        return new Array<Object>(this.results);
    }

    public void page_links() {
        echo(gVars.webEnv, this.paging_text);
    }

    public boolean results_are_paged() {
        if (booleanval(this.paging_text)) {
            return true;
        }

        return false;
    }

    public boolean is_search() {
        if (booleanval(this.search_term)) {
            return true;
        }

        return false;
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
