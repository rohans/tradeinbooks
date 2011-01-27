/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: wpdb.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
package org.numiton.nwp.wp_includes;

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.*;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.db.MySQL;
import com.numiton.error.ErrorHandling;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class wpdb implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(wpdb.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public boolean show_errors = false;
    public boolean suppress_errors = false;
    public String last_error = "";
    public int num_queries = 0;
    public String last_query;
    public Array<StdClass> col_info = new Array<StdClass>();
    public Array<Object> queries = new Array<Object>();
    public String prefix = "";
    public boolean ready = false;

	// Our tables
    public String posts;
    public String users;
    public String categories;
    public String post2cat;
    public String comments;
    public String links;
    public String options;
    public String postmeta;
    public String usermeta;
    public String terms;
    public String term_taxonomy;
    public String term_relationships;
    public Array<Object> tables = new Array<Object>(new ArrayEntry<Object>("users"), new ArrayEntry<Object>("usermeta"), new ArrayEntry<Object>("posts"), new ArrayEntry<Object>("categories"),
            new ArrayEntry<Object>("post2cat"), new ArrayEntry<Object>("comments"), new ArrayEntry<Object>("links"), new ArrayEntry<Object>("link2cat"), new ArrayEntry<Object>("options"),
            new ArrayEntry<Object>("postmeta"), new ArrayEntry<Object>("terms"), new ArrayEntry<Object>("term_taxonomy"), new ArrayEntry<Object>("term_relationships"));
    public Object charset;
    public Object collate;
    public int dbh;
    public Array<Object> last_result = new Array<Object>();
    public Object func_call;
    public int result;
    public int rows_affected;
    public int insert_id;
    public int num_rows;
    public int time_start;
    public WP_Error error;
    public Object link2cat;
    public Array<Object> EZSQL_ERROR = new Array<Object>();

    /**
     * Connects to the database server and selects a database
     * @param string $dbuser
     * @param string $dbpassword
     * @param string $dbname
     * @param string $dbhost
     */

    // Commented by Numiton
    // public wpdb(GlobalVars javaGlobalVariables, GlobalConsts
    // javaGlobalConstants, Object dbuser, Object dbpassword, Object dbname,
    // Object dbhost) {
    // setContext(javaGlobalVariables, javaGlobalConstants);
    // return this.__construct(dbuser, dbpassword, dbname, dbhost);
    // }
    public wpdb(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants, String dbuser, String dbpassword, String dbname, String dbhost) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        FunctionHandling.register_shutdown_function(gVars.webEnv, new Callback("__destruct", this));

        if (gConsts.isWP_DEBUGDefined() && equal(gConsts.getWP_DEBUG(), true)) {
            this.show_errors();
        }

        if (gConsts.isDB_CHARSETDefined()) {
            this.charset = gConsts.getDB_CHARSET();
        }

        if (gConsts.isDB_COLLATEDefined()) {
            this.collate = gConsts.getDB_COLLATE();
        }

        this.dbh = MySQL.mysql_connect(gVars.webEnv, dbhost, dbuser, dbpassword, true);

        if (!booleanval(this.dbh)) {
            this.bail(
                    "\n<h1>Error establishing a database connection</h1>\n<p>This either means that the username and password information in your <code>wp-config.php</code> file is incorrect or we can\'t contact the database server at <code>" +
                    dbhost +
                    "</code>. This could mean your host\'s database server is down.</p>\n<ul>\n\t<li>Are you sure you have the correct username and password?</li>\n\t<li>Are you sure that you have typed the correct hostname?</li>\n\t<li>Are you sure that the database server is running?</li>\n</ul>\n<p>If you\'re unsure what these terms mean you should probably contact your host. If you still need help you can always visit the <a href=\'http://wordpress.org/support/\'>WordPress Support Forums</a>.</p>\n");

            return;
        }

        this.ready = true;

        // Modified by Numiton
        this.select(dbname);

        if (!empty(this.charset) && booleanval(Options.version_compare(MySQL.mysql_get_server_info(gVars.webEnv, this.dbh), "4.1.0", ">="))) {
            this.query("SET NAMES \'" + this.charset + "\'");
        }
    }

    public boolean __destruct() {
        return true;
    }

    public Object set_prefix(String prefix) {
        String old_prefix = null;
        Object table = null;

        if (QRegExPerl.preg_match("|[^a-z0-9_]|i", prefix)) {
            return new WP_Error(gVars, gConsts, "invalid_db_prefix", "Invalid database prefix"); // No gettext here
        }

        old_prefix = this.prefix;
        this.prefix = prefix;

        for (Map.Entry javaEntry673 : this.tables.entrySet()) {
            table = javaEntry673.getValue();

            // Modified by Numiton
            String value = this.prefix + table;

            if (equal(table, "users")) {
                this.users = value;
            } else if (equal(table, "usermeta")) {
                this.usermeta = value;
            } else if (equal(table, "posts")) {
                this.posts = value;
            } else if (equal(table, "categories")) {
                this.categories = value;
            } else if (equal(table, "post2cat")) {
                this.post2cat = value;
            } else if (equal(table, "comments")) {
                this.comments = value;
            } else if (equal(table, "links")) {
                this.links = value;
            } else if (equal(table, "link2cat")) {
                this.link2cat = value;
            } else if (equal(table, "options")) {
                this.options = value;
            } else if (equal(table, "postmeta")) {
                this.postmeta = value;
            } else if (equal(table, "terms")) {
                this.terms = value;
            } else if (equal(table, "term_taxonomy")) {
                this.term_taxonomy = value;
            } else if (equal(table, "term_relationships")) {
                this.term_relationships = value;
            } else {
                LOG.warn("Invalid table name: " + table);
            }
        }

        if (gConsts.isCUSTOM_USER_TABLEDefined()) {
            this.users = gConsts.getCUSTOM_USER_TABLE();
        }

        if (gConsts.isCUSTOM_USER_META_TABLEDefined()) {
            this.usermeta = gConsts.getCUSTOM_USER_META_TABLE();
        }

        return old_prefix;
    }

    /**
     * Selects a database using the current class's $this->dbh
     * @param string $db name
     */
    public void select(String db) {
        if (!MySQL.mysql_select_db(gVars.webEnv, db, this.dbh)) {
            this.ready = false;
            this.bail(
                    "\n<h1>Can&#8217;t select database</h1>\n<p>We were able to connect to the database server (which means your username and password is okay) but not able to select the <code>" +
                    db + "</code> database.</p>\n<ul>\n<li>Are you sure it exists?</li>\n<li>Does the user <code>" + gConsts.getDB_USER() + "</code> have permission to use the <code>" + db +
                    "</code> database?</li>\n<li>On some systems the name of your database is prefixed with your username, so it would be like username_wordpress. Could that be the problem?</li>\n</ul>\n<p>If you don\'t know how to setup a database you should <strong>contact your host</strong>. If all else fails you may find help at the <a href=\'http://wordpress.org/support/\'>WordPress Support Forums</a>.</p>");

            return;
        }
    }

    /**
     * Escapes content for insertion into the database, for security
     * @param string $string
     * @return string query safe string
     */
    public String escape(String string) {
        return Strings.addslashes(gVars.webEnv, string);
        // Disable rest for now, causing problems
		/*
		if( !$this->dbh || version_compare( phpversion(), '4.3.0' ) == '-1' )
			return mysql_escape_string( $string );
		else
			return mysql_real_escape_string( $string, $this->dbh );
		*/
    }

    /**
     * Escapes content by reference for insertion into the database, for
     * security
     * @param string $s
     */
    public void escape_by_ref(Ref<Object> s) {
        s.value = this.escape(strval(s.value));
    }

    /**
     * Prepares a SQL query for safe use, using sprintf() syntax
     */
    public String prepare(Object... args) {
        String query;

        if (strictEqual(null, args)) {
            return null;
        }

        // Modified by Numiton
        Array<Object> argsArray = new Array<Object>(new Array<Object>());
        argsArray.putAllValues(args);
        query = strval(Array.array_shift(argsArray));
        query = Strings.str_replace("\'%s\'", "%s", query); // in case someone mistakenly already singlequoted it
        query = Strings.str_replace("\"%s\"", "%s", query); // doublequote unquoting
        query = Strings.str_replace("%s", "\'%s\'", query); // quote the strings
        Array.array_walk(argsArray, new Callback("escape_by_ref", this));

        return QStrings.vsprintf(query, argsArray);
    }

    public boolean print_error() {
        return print_error("");
    }

	// ==================================================================
	//	Print SQL/DB error.

    public boolean print_error(String str) {
        String error_str = null;
        String caller = null;
        boolean log_error = false;
        String log_file = null;
        String query = null;

        if (!booleanval(str)) {
            str = MySQL.mysql_error(gVars.webEnv, this.dbh);
        }

        EZSQL_ERROR.putValue(new Array<Object>(new ArrayEntry<Object>("query", this.last_query), new ArrayEntry<Object>("error_str", str)));

        if (this.suppress_errors) {
            return false;
        }

        error_str = "nWordPress database error " + str + " for query " + this.last_query;

        if (booleanval(caller = this.get_caller())) {
            error_str = error_str + " made by " + caller;
        }

        log_error = true;

        if (!true)/*Modified by Numiton*/
         {
            log_error = false;
        }

        log_file = Options.ini_get(gVars.webEnv, "error_log");

        if (!empty(log_file) && !equal("syslog", log_file) && !FileSystemOrSocket.is_writable(gVars.webEnv, log_file)) {
            log_error = false;
        }

        if (log_error) {
            ErrorHandling.error_log(gVars.webEnv, error_str, 0);
        }

		// Is error output turned on or not..
        if (!this.show_errors) {
            return false;
        }

        str = Strings.htmlspecialchars(str, Strings.ENT_QUOTES);
        query = Strings.htmlspecialchars(this.last_query, Strings.ENT_QUOTES);
        
		// If there is an error then take note of it
        print(gVars.webEnv, "<div id=\'error\'>\n\t\t<p class=\'wpdberror\'><strong>nWordPress database error:</strong> [" + str + "]<br />\n\t\t<code>" + query + "</code></p>\n\t\t</div>");

        return false;
    }

    public boolean show_errors() {
        return show_errors(true);
    }

	// ==================================================================
	//	Turn error handling on or off..

    public boolean show_errors(boolean show) {
        boolean errors = false;
        errors = this.show_errors;
        this.show_errors = show;

        return errors;
    }

    public boolean hide_errors() {
        boolean show = false;
        show = this.show_errors;
        this.show_errors = false;

        return show;
    }

    public boolean suppress_errors() {
        return suppress_errors(true);
    }

    public boolean suppress_errors(boolean suppress) {
        boolean errors = false;
        errors = this.suppress_errors;
        this.suppress_errors = suppress;

        return errors;
    }

	// ==================================================================
	//	Kill cached query results

    public void flush() {
        this.last_result = new Array<Object>();
        this.col_info = new Array<StdClass>();
        this.last_query = null;
    }

	// ==================================================================
	//	Basic Query	- see docs for more detail

    public int query(String query) {
        int return_val = 0;
        int i = 0;
        int num_rows = 0;
        StdClass row = null;

        if (!this.ready) {
            return BOOLEAN_FALSE;
        }

		// filter the query, if filters are available
		// NOTE: some queries are made before the plugins have been loaded, and thus cannot be filtered with this method
        if (true)/*Modified by Numiton*/
         {
            query = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("query", query));
        }

		// initialise return
        return_val = 0;
        this.flush();
        
		// Log how the function was called
        this.func_call = "$db->query(\"" + query + "\")";
        
		// Keep track of the last query for debug..
        this.last_query = query;

		// Perform the query via std mysql_query function..
        if (gConsts.getSAVEQUERIES()) {
            this.timer_start();
        }

        this.result = MySQL.mysql_query(gVars.webEnv, query, this.dbh);
        ++this.num_queries;

        if (gConsts.getSAVEQUERIES()) {
            this.queries.putValue(new Array<Object>(new ArrayEntry<Object>(query), new ArrayEntry<Object>(this.timer_stop()), new ArrayEntry<Object>(this.get_caller())));
        }

		// If there is an error then take note of it..
        if (booleanval(this.last_error = MySQL.mysql_error(gVars.webEnv, this.dbh))) {
            this.print_error();

            return BOOLEAN_FALSE;
        }

        if (QRegExPerl.preg_match("/^\\s*(insert|delete|update|replace) /i", query)) {
            this.rows_affected = MySQL.mysql_affected_rows(gVars.webEnv, this.dbh);
			// Take note of the insert_id
            if (QRegExPerl.preg_match("/^\\s*(insert|replace) /i", query)) {
                this.insert_id = MySQL.mysql_insert_id(gVars.webEnv, this.dbh);
            }
			// Return number of rows affected
            return_val = this.rows_affected;
        } else {
            i = 0;

            while (i < MySQL.mysql_num_fields(gVars.webEnv, this.result)) {
                this.col_info.putValue(i, MySQL.mysql_fetch_field(gVars.webEnv, this.result));
                i++;
            }

            num_rows = 0;

            while (booleanval(row = MySQL.mysql_fetch_object(gVars.webEnv, this.result))) {
                this.last_result.putValue(num_rows, row);
                num_rows++;
            }

            MySQL.mysql_free_result(gVars.webEnv, this.result);
            
			// Log number of rows the query returned
            this.num_rows = num_rows;
            
			// Return number of rows selected
            return_val = this.num_rows;
        }

        return return_val;
    }

    /**
     * Insert an array of data into a table
     * @param string $table WARNING: not sanitized!
     * @param array $data should not already be SQL-escaped
     * @return mixed results of $this->query()
     */
    public int insert(String table, Array data) {
        Array fields;
        data = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(data);
        fields = Array.array_keys(data);

        return this.query("INSERT INTO " + table + " (`" + Strings.implode("`,`", fields) + "`) VALUES (\'" + Strings.implode("\',\'", data) + "\')");
    }

    /**
     * Update a row in the table with an array of data
     * @param string $table WARNING: not sanitized!
     * @param array $data should not already be SQL-escaped
     * @param array $where a named array of WHERE column => value relationships.
     * Multiple member pairs will be joined with ANDs. WARNING: the
     * column names are not currently sanitized!
     * @return mixed results of $this->query()
     */
    public int update(Object table, Array<Object> data, Array<Object> where)/* Array or null */
     {
        Array<String> bits = new Array<String>();
        Array<String> wheres = new Array<String>();
        Object k = null;
        Object c = null;
        String v = null;
        data = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(data);

        //		bits = wheres = new Array<Object>();
        for (Map.Entry javaEntry674 : Array.array_keys(data).entrySet()) {
            k = javaEntry674.getValue();
            bits.putValue("`" + strval(k) + "` = \'" + strval(data.getValue(k)) + "\'");
        }

        if (is_array(where)) {
            for (Map.Entry javaEntry675 : where.entrySet()) {
                c = javaEntry675.getKey();
                v = strval(javaEntry675.getValue());
                wheres.putValue(strval(c) + " = \'" + this.escape(v) + "\'");
            }
        } else {
            return intval(false);
        }

        return this.query("UPDATE " + table + " SET " + Strings.implode(", ", bits) + " WHERE " + Strings.implode(" AND ", wheres) + " LIMIT 1");
    }

    public Object get_var(String query) {
        return get_var(query, 0, 0);
    }

    /**
     * Get one variable from the database
     * @param string $query (can be null as well, for caching, see codex)
     * @param int $x = 0 row num to return
     * @param int $y = 0 col num to return
     * @return mixed results
     */
    public Object get_var(String query, int x, int y) {
        Array<Object> values = new Array<Object>();
        this.func_call = "$db->get_var(\"" + query + "\"," + strval(x) + "," + strval(y) + ")";

        if (booleanval(query)) {
            this.query(query);
        }

		// Extract var out of cached results based x,y vals
        if (!empty(this.last_result.getValue(y))) {
            values = Array.array_values(ClassHandling.get_object_vars(this.last_result.getValue(y)));
        }

		// If there is a value return it else return null
        return (isset(values.getValue(x)) && !strictEqual(values.getValue(x), ""))
		        ? values.getValue(x)
		        : null;
    }

    public Object get_row() {
        return get_row("", gConsts.getOBJECT(), 0);
    }

    public Object get_row(String query) {
        return get_row(query, gConsts.getOBJECT(), 0);
    }

    public Object get_row(String query, String output) {
        return get_row(query, output, 0);
    }

    /**
     * Get one row from the database
     * @param string $query
     * @param string $output ARRAY_A | ARRAY_N | OBJECT
     * @param int $y row num to return
     * @return mixed results
     */
    public Object get_row(String query, String output, int y) {
        this.func_call = "$db->get_row(\"" + query + "\"," + output + "," + strval(y) + ")";

        if (booleanval(query)) {
            this.query(query);
        } else {
            return null;
        }

        if (!isset(this.last_result.getValue(y))) {
            return null;
        }

        if (equal(output, gConsts.getOBJECT())) {
            return booleanval(this.last_result.getValue(y))
            ? this.last_result.getValue(y)
            : null;
        } else if (equal(output, gConsts.getARRAY_A())) {
            return booleanval(this.last_result.getValue(y))
            ? ClassHandling.get_object_vars(this.last_result.getValue(y))
            : (Array<Object>) null;
        } else if (equal(output, gConsts.getARRAY_N())) {
            return booleanval(this.last_result.getValue(y))
            ? Array.array_values(ClassHandling.get_object_vars(this.last_result.getValue(y)))
            : (Array<Object>) null;
        } else {
            this.print_error(" $db->get_row(string query, output type, int offset) -- Output type must be one of: OBJECT, ARRAY_A, ARRAY_N");
        }

        return null;
    }

    public Array get_col(String query) {
        return get_col(query, 0);
    }

    /**
     * Gets one column from the database
     * @param string $query (can be null as well, for caching, see codex)
     * @param int $x col num to return
     * @return array results
     */
    public Array<Object> get_col(String query, int x) {
        Array<Object> new_array = new Array<Object>();
        int i = 0;

        if (booleanval(query)) {
            this.query(query);
        }

        new_array = new Array<Object>();
		// Extract the column values
        for (i = 0; i < Array.count(this.last_result); i++) {
            new_array.putValue(i, this.get_var(null, x, i));
        }

        return new_array;
    }

    public Array<Object> get_results() {
        return get_results("", gConsts.getOBJECT());
    }

    public Array get_results(String query) {
        return get_results(query, gConsts.getOBJECT());
    }

    /**
     * Return an entire result set from the database
     * @param string $query (can also be null to pull from the cache)
     * @param string $output ARRAY_A | ARRAY_N | OBJECT_K | OBJECT
     * @return mixed results
     */
    public Array<Object> get_results(String query, String output) {
        Object key = null;
        Object row = null;
        Array<Object> new_array = new Array<Object>();
        int i = 0;
        this.func_call = "$db->get_results(\"" + query + "\", " + output + ")";

        if (booleanval(query)) {
            this.query(query);
        } else {
            return null;
        }

        if (equal(output, gConsts.getOBJECT())) {
			// Return an integer-keyed array of row objects
            return this.last_result;
        } else if (equal(output, gConsts.getOBJECT_K())) {
			// Return an array of row objects with keys from column 1
			// (Duplicates are discarded)
            for (Map.Entry javaEntry676 : this.last_result.entrySet()) {
                row = javaEntry676.getValue();
                key = Array.array_shift(ClassHandling.get_object_vars(row));

                if (!isset(new_array.getValue(key))) {
                    new_array.putValue(key, row);
                }
            }

            return new_array;
        } else if (equal(output, gConsts.getARRAY_A()) || equal(output, gConsts.getARRAY_N())) {
			// Return an integer-keyed array of...
            if (booleanval(this.last_result)) {
                i = 0;

                for (Map.Entry javaEntry677 : this.last_result.entrySet()) {
                    row = javaEntry677.getValue();

                    if (equal(output, gConsts.getARRAY_N())) {
						// ...integer-keyed row arrays
                        new_array.putValue(i, Array.array_values(ClassHandling.get_object_vars(row)));
                    } else {
						// ...column name-keyed row arrays
                        new_array.putValue(i, ClassHandling.get_object_vars(row));
                    }

                    ++i;
                }

                return new_array;
            }
        }

        return new Array<Object>();
    }

    /**
     * Grabs column metadata from the last query
     * @param string $info_type one of name, table, def, max_length, not_null,
     * primary_key, multiple_key, unique_key, numeric, blob, type,
     * unsigned, zerofill
     * @param int $col_offset 0: col name. 1: which table the col's in. 2: col's
     * max length. 3: if the col is numeric. 4: col's type
     * @return mixed results
     */
    public Object get_col_info(String info_type, int col_offset) {
        int i = 0;
        Array<Object> new_array = new Array<Object>();
        StdClass col = null;

        if (booleanval(this.col_info)) {
            if (equal(col_offset, -1)) {
                i = 0;

                for (Map.Entry javaEntry678 : this.col_info.entrySet()) {
                    col = (StdClass) javaEntry678.getValue();
                    new_array.putValue(i, StdClass.getValue(col, info_type));
                    i++;
                }

                return new_array;
            } else {
                return this.col_info.getValue(col_offset).fields.getValue(info_type);
            }
        }

        return null;
    }

    /**
     * Starts the timer, for debugging purposes
     */
    public boolean timer_start() {
        Array<String> mtime = new Array<String>();
        String mtimeStr = strval(DateTime.microtime());
        mtime = Strings.explode(" ", mtimeStr);
        this.time_start = intval(mtime.getValue(1)) + intval(mtime.getValue(0));

        return true;
    }

    /**
     * Stops the debugging timer
     * @return int total time spent on the query, in milliseconds
     */
    public int timer_stop() {
        Array<String> mtime = new Array<String>();
        int time_end = 0;
        int time_total = 0;
        String mtimeStr = strval(DateTime.microtime());
        mtime = Strings.explode(" ", mtimeStr);
        time_end = intval(mtime.getValue(1)) + intval(mtime.getValue(0));
        time_total = time_end - this.time_start;

        return time_total;
    }

    /**
     * Wraps fatal errors in a nice header and footer and dies.
     * @param string $message
     */
    public boolean bail(String message) { // Just wraps errors in a nice header and footer
        if (!this.show_errors) {
            /* Modified by Numiton: WP_Error ALWAYS exists */

            //			if (/* Usage of unsupported runtime function 'class_exists' */Unsupported.class_exists("WP_Error")) {
            this.error = new WP_Error(gVars, gConsts, "500", message);

            //			}

            //			else

            //				this.error = message;
            return false;
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(message, "");

        return false;
    }

    /**
     * Checks wether of not the database version is high enough to support the
     * features WordPress uses
     * @global $wp_version
     */
    public Object check_database_version() {
		// Make sure the server has MySQL 4.0
        String mysql_version = QRegExPerl.preg_replace("|[^0-9\\.]|", "", MySQL.mysql_get_server_info(gVars.webEnv, this.dbh));

        if (booleanval(Options.version_compare(mysql_version, "4.0.0", "<"))) {
            return new WP_Error(gVars, gConsts, "database_version",
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("<strong>ERROR</strong>: nWordPress %s requires MySQL 4.0.0 or higher", "default"), gVars.wp_version));
        }

        return "";
    }

    /**
     * This function is called when WordPress is generating the table schema
     * to determine wether or not the current database supports or needs the
     * collation statements.
     */
    public boolean supports_collation() {
        return booleanval(Options.version_compare(MySQL.mysql_get_server_info(gVars.webEnv, this.dbh), "4.1.0", ">="));
    }

    /**
     * Get the name of the function that called wpdb.
     * @return string the name of the calling function
     */
    public String get_caller() {
        Array<Object> bt = new Array<Object>();
        String caller = null;
        Array<Object> trace = new Array<Object>();

		// requires PHP 4.3+
        if (!true)/*Modified by Numiton*/
         {
            return "";
        }

        bt = ErrorHandling.debug_backtrace();
        caller = "";

        for (Map.Entry javaEntry679 : bt.entrySet()) {
            trace = (Array<Object>) javaEntry679.getValue();

            if (equal(trace.getValue("class"), SourceCodeInfo.getCurrentClass())) {
                continue;
            } else if (equal(Strings.strtolower(strval(trace.getValue("function"))), "call_user_func_array")) {
                continue;
            } else if (equal(Strings.strtolower(strval(trace.getValue("function"))), "apply_filters")) {
                continue;
            } else if (equal(Strings.strtolower(strval(trace.getValue("function"))), "do_action")) {
                continue;
            }

            caller = strval(trace.getValue("function"));

            break;
        }

        return caller;
    }

    protected void finalize() throws Throwable {
        __destruct();
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
