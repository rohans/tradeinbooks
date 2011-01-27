/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Install_helperPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp.wp_admin;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.Wp_configPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;


@Controller
@Scope("request")
public class Install_helperPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(Install_helperPage.class.getName());

    @Override
    @RequestMapping("/wp-admin/install-helper.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/install_helper";
    }

    /**
     ** maybe_create_table()
     ** Create db table if it doesn't exist.
     ** Returns:  true if already exists or on successful completion
     **           false on error
     */
    public boolean maybe_create_table(String table_name, String create_ddl) {
        Object table = null;

        for (Map.Entry javaEntry262 : gVars.wpdb.get_col("SHOW TABLES", 0).entrySet()) {
            table = javaEntry262.getValue();

            if (equal(table, table_name)) {
                return true;
            }
        }

        //didn't find it try to create it.
        gVars.wpdb.query(create_ddl);

        // we cannot directly tell that whether this succeeded!
        for (Map.Entry javaEntry263 : gVars.wpdb.get_col("SHOW TABLES", 0).entrySet()) {
            table = javaEntry263.getValue();

            if (equal(table, table_name)) {
                return true;
            }
        }

        return false;
    }

    /**
     ** maybe_add_column()
     ** Add column to db table if it doesn't exist.
     ** Returns:  true if already exists or on successful completion
     **           false on error
     */
    public boolean maybe_add_column(String table_name, String column_name, String create_ddl) {
        Object column = null;

        for (Map.Entry javaEntry264 : gVars.wpdb.get_col("DESC " + table_name, 0).entrySet()) {
            column = javaEntry264.getValue();

            if (booleanval(gVars.debug)) {
                echo(gVars.webEnv, "checking " + strval(column) + " == " + column_name + "<br />");
            }

            if (equal(column, column_name)) {
                return true;
            }
        }

        //didn't find it try to create it.
        gVars.wpdb.query(create_ddl);

        // we cannot directly tell that whether this succeeded!
        for (Map.Entry javaEntry265 : gVars.wpdb.get_col("DESC " + table_name, 0).entrySet()) {
            column = javaEntry265.getValue();

            if (equal(column, column_name)) {
                return true;
            }
        }

        return false;
    }

    /**
     ** maybe_drop_column()
     ** Drop column from db table if it exists.
     ** Returns:  true if it doesn't already exist or on successful drop
     **           false on error
     */
    public boolean maybe_drop_column(String table_name, String column_name, String drop_ddl) {
        Object column = null;

        for (Map.Entry javaEntry266 : gVars.wpdb.get_col("DESC " + table_name, 0).entrySet()) {
            column = javaEntry266.getValue();

            if (equal(column, column_name)) {
            	//found it try to drop it.
                gVars.wpdb.query(drop_ddl);

                // we cannot directly tell that whether this succeeded!
                for (Map.Entry javaEntry267 : gVars.wpdb.get_col("DESC " + table_name, 0).entrySet()) {
                    column = javaEntry267.getValue();

                    if (equal(column, column_name)) {
                        return false;
                    }
                }
            }
        }

        // else didn't find it
        return true;
    }

    /**
     ** check_column()
     ** Check column matches passed in criteria.
     ** Pass in null to skip checking that criteria
     ** Returns:  true if it matches
     **           false otherwise
     ** (case sensitive) Column names returned from DESC table are:
     **      Field
     **      Type
     **      Null
     **      Key
     **      Default
     **      Extra
     */
    public boolean check_column(Object table_name, Object col_name, Object col_type, Object is_null, Object key, Object _default, Object extra) {
        int diffs = 0;
        Array<Object> results = new Array<Object>();
        StdClass row = null;
        diffs = 0;
        results = gVars.wpdb.get_results("DESC " + table_name);

        for (Map.Entry javaEntry268 : results.entrySet())/*
         * end if found our column end if found our column
         */
         {
            row = (StdClass) javaEntry268.getValue();

            if (gVars.debug > 1) {
                print_r(gVars.webEnv, row);
            }

            if (equal(StdClass.getValue(row, "Field"), col_name)) {
            	// got our column, check the params
                if (booleanval(gVars.debug)) {
                    echo(gVars.webEnv, "checking " + StdClass.getValue(row, "Type") + " against " + strval(col_type) + "\n");
                }

                if (!equal(col_type, null) && !equal(StdClass.getValue(row, "Type"), col_type)) {
                    ++diffs;
                }

                if (!equal(is_null, null) && !equal(StdClass.getValue(row, "Null"), is_null)) {
                    ++diffs;
                }

                if (!equal(key, null) && !equal(StdClass.getValue(row, "Key"), key)) {
                    ++diffs;
                }

                if (!equal(_default, null) && !equal(StdClass.getValue(row, "Default"), _default)) {
                    ++diffs;
                }

                if (!equal(extra, null) && !equal(StdClass.getValue(row, "Extra"), extra)) {
                    ++diffs;
                }

                if (diffs > 0) {
                    if (booleanval(gVars.debug)) {
                        echo(gVars.webEnv, "diffs = " + strval(diffs) + " returning 0\n");
                    }

                    return false;
                }

                return true;
            } // end if found our column
        }

        return false;
    }
    
    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_install_helper_block1");
        gVars.webEnv = webEnv;
        requireOnce(gVars, gConsts, Wp_configPage.class);
        gVars.debug = 0;

        return DEFAULT_VAL;
        
        /*
        echo "<p>testing</p>";
        echo "<pre>";

        //check_column('wp_links', 'link_description', 'mediumtext');
        //if (check_column($wpdb->comments, 'comment_author', 'tinytext'))
//            echo "ok\n";
        $error_count = 0;
        $tablename = $wpdb->links;
        // check the column
        if (!check_column($wpdb->links, 'link_description', 'varchar(255)'))
        {
        	$ddl = "ALTER TABLE $wpdb->links MODIFY COLUMN link_description varchar(255) NOT NULL DEFAULT '' ";
        	$q = $wpdb->query($ddl);
        }
        if (check_column($wpdb->links, 'link_description', 'varchar(255)')) {
        	$res .= $tablename . ' - ok <br />';
        } else {
        	$res .= 'There was a problem with ' . $tablename . '<br />';
        	++$error_count;
        }
        echo "</pre>";
        */
    }
}
