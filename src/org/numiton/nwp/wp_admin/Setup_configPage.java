/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Setup_configPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.*;
import org.numiton.nwp.wp_includes.ClassesPage;
import org.numiton.nwp.wp_includes.CompatPage;
import org.numiton.nwp.wp_includes.FunctionsPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.SourceCodeInfo;
import com.numiton.array.Array;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class Setup_configPage extends NumitonController implements CommonInterface1, CommonInterface2 {
    protected static final Logger LOG = Logger.getLogger(Setup_configPage.class.getName());
    public CommonInterface2 commonInterface2;
    public Array<String> configFile;
    public String dbname;
    public String uname;
    public String passwrd;
    public String dbhost;
    public Object line_num;

    @Override
    @RequestMapping("/wp-admin/setup-config.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/setup_config";
    }

    public void display_header() {
        Object admin_dir = null;
        Network.header(gVars.webEnv, "Content-Type: text/html; charset=utf-8");
        echo(
                gVars.webEnv,
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n<title>nWordPress &rsaquo; Setup Configuration File</title>\n<link rel=\"stylesheet\" href=\"");
        echo(gVars.webEnv, admin_dir);
        echo(gVars.webEnv, "css/install.css\" type=\"text/css\" />\n\n</head>\n<body>\n<h1 id=\"logo\"><img alt=\"nWordPress\" src=\"images/wordpress-logo.png\" /></h1>\n");
    }//end function display_header();

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_setup_config_block1");
        gVars.webEnv = webEnv;
        
        gConsts.setWP_INSTALLING(true);
        //These two defines are required to allow us to use require_wp_db() to load the database class while being wp-content/wp-db.php aware
        gConsts.setABSPATH(FileSystemOrSocket.dirname(FileSystemOrSocket.dirname(SourceCodeInfo.getCurrentFile(gVars.webEnv))) + "/");
        gConsts.setWPINC("wp-includes");
        
        requireOnce(gVars, gConsts, CompatPage.class);
        requireOnce(gVars, gConsts, FunctionsPage.class);
        requireOnce(gVars, gConsts, ClassesPage.class);

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, "../wp-config-sample.php")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die("Sorry, I need a wp-config-sample.php file to work from. Please re-upload this file from your nWordPress installation.", "");
        }

        configFile = FileSystemOrSocket.file(gVars.webEnv, "../wp-config-sample.php");

        if (!FileSystemOrSocket.is_writable(gVars.webEnv, "../")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                    "Sorry, I can\'t write to the directory. You\'ll have to either change the permissions on your nWordPress directory or create your wp-config.php manually.",
                    "");
        }

        // Check if wp-config.php has been created
        if (FileSystemOrSocket.file_exists(gVars.webEnv, "../wp-config.php")) {
            getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(
                    "<p>The file \'wp-config.php\' already exists. If you need to reset any of the configuration items in this file, please delete it first. You may try <a href=\'install.php\'>installing now</a>.</p>",
                    "");
        }

        if (isset(gVars.webEnv._GET.getValue("step"))) {
            gVars.step = intval(gVars.webEnv._GET.getValue("step"));
        } else {
            gVars.step = 0;
        }

        switch (gVars.step) {
        case 0: {
            this.display_header();
            echo(
                    gVars.webEnv,
                    "\n<p>Welcome to nWordPress. Before getting started, we need some information on the database. You will need to know the following items before proceeding.</p>\n<ol>\n\t<li>Database name</li>\n\t<li>Database username</li>\n\t<li>Database password</li>\n\t<li>Database host</li>\n\t<li>Table prefix (if you want to run more than one nWordPress in a single database) </li>\n</ol>\n<p><strong>If for any reason this automatic file creation doesn\'t work, don\'t worry. All this does is fill in the database information to a configuration file. You may also simply open <code>wp-config-sample.php</code> in a text editor, fill in your information, and save it as <code>wp-config.php</code>. </strong></p>\n<p>In all likelihood, these items were supplied to you by your ISP. If you do not have this information, then you will need to contact them before you can continue. If you&#8217;re all ready&hellip;</p>\n\n<p><a href=\"setup-config.php?step=1\" class=\"button\">Let&#8217;s go!</a></p>\n");

            break;
        }

        case 1: {
            this.display_header();
            echo(
                    gVars.webEnv,
                    "<form method=\"post\" action=\"setup-config.php?step=2\">\n\t<p>Below you should enter your database connection details. If you\'re not sure about these, contact your host. </p>\n\t<table class=\"form-table\">\n\t\t<tr>\n\t\t\t<th scope=\"row\">Database Name</th>\n\t\t\t<td><input name=\"dbname\" type=\"text\" size=\"25\" value=\"nwordpress\" /></td>\n\t\t\t<td>The name of the database you want to run WP in. </td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th scope=\"row\">User Name</th>\n\t\t\t<td><input name=\"uname\" type=\"text\" size=\"25\" value=\"username\" /></td>\n\t\t\t<td>Your MySQL username</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th scope=\"row\">Password</th>\n\t\t\t<td><input name=\"pwd\" type=\"text\" size=\"25\" value=\"password\" /></td>\n\t\t\t<td>...and MySQL password.</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th scope=\"row\">Database Host</th>\n\t\t\t<td><input name=\"dbhost\" type=\"text\" size=\"25\" value=\"localhost\" /></td>\n\t\t\t<td>99% chance you won\'t need to change this value.</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th scope=\"row\">Table Prefix</th>\n\t\t\t<td><input name=\"prefix\" type=\"text\" id=\"prefix\" value=\"nwp_\" size=\"25\" /></td>\n\t\t\t<td>If you want to run multiple nWordPress installations in a single database, change this.</td>\n\t\t</tr>\n\t</table>\n\t<h2 class=\"step\">\n\t<input name=\"submit\" type=\"submit\" value=\"Submit\" class=\"button\" />\n\t</h2>\n</form>\n");

            break;
        }

        case 2:/*
         * Test the db connection. Test the db connection.
         */

        /*
         * We'll fail here if the values are no good. We'll fail here if
         * the values are no good.
         */
         {
            dbname = Strings.trim(strval(gVars.webEnv._POST.getValue("dbname")));
            uname = Strings.trim(strval(gVars.webEnv._POST.getValue("uname")));
            passwrd = Strings.trim(strval(gVars.webEnv._POST.getValue("pwd")));
            dbhost = Strings.trim(strval(gVars.webEnv._POST.getValue("dbhost")));
            gVars.prefix = Strings.trim(strval(gVars.webEnv._POST.getValue("prefix")));

            if (empty(gVars.prefix)) {
                gVars.prefix = "nwp_";
            }

            // Test the db connection.
            gConsts.setDB_NAME(dbname);
            gConsts.setDB_USER(uname);
            gConsts.setDB_PASSWORD(passwrd);
            gConsts.setDB_HOST(dbhost);
            
            // We'll fail here if the values are no good.
            getIncluded(FunctionsPage.class, gVars, gConsts).require_wp_db();

            if (!empty(gVars.wpdb.error)) {
                getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(gVars.wpdb.error.get_error_message(), "");
            }

            gVars.handle = FileSystemOrSocket.fopen(gVars.webEnv, "../wp-config.php", "w");

            for (Map.Entry javaEntry300 : configFile.entrySet()) {
                line_num = javaEntry300.getKey();
                gVars.line = strval(javaEntry300.getValue());

                {
                    int javaSwitchSelector28 = 0;

                    if (equal(Strings.substr(gVars.line, 0, 16), "define(\'DB_NAME\'")) {
                        javaSwitchSelector28 = 1;
                    }

                    if (equal(Strings.substr(gVars.line, 0, 16), "define(\'DB_USER\'")) {
                        javaSwitchSelector28 = 2;
                    }

                    if (equal(Strings.substr(gVars.line, 0, 16), "define(\'DB_PASSW")) {
                        javaSwitchSelector28 = 3;
                    }

                    if (equal(Strings.substr(gVars.line, 0, 16), "define(\'DB_HOST\'")) {
                        javaSwitchSelector28 = 4;
                    }

                    if (equal(Strings.substr(gVars.line, 0, 16), "$table_prefix  =")) {
                        javaSwitchSelector28 = 5;
                    }

                    switch (javaSwitchSelector28) {
                    case 1: {
                        FileSystemOrSocket.fwrite(gVars.webEnv, gVars.handle, Strings.str_replace("putyourdbnamehere", dbname, gVars.line));

                        break;
                    }

                    case 2: {
                        FileSystemOrSocket.fwrite(gVars.webEnv, gVars.handle, Strings.str_replace("\'usernamehere\'", "\'" + uname + "\'", gVars.line));

                        break;
                    }

                    case 3: {
                        FileSystemOrSocket.fwrite(gVars.webEnv, gVars.handle, Strings.str_replace("\'yourpasswordhere\'", "\'" + passwrd + "\'", gVars.line));

                        break;
                    }

                    case 4: {
                        FileSystemOrSocket.fwrite(gVars.webEnv, gVars.handle, Strings.str_replace("localhost", dbhost, gVars.line));

                        break;
                    }

                    case 5: {
                        FileSystemOrSocket.fwrite(gVars.webEnv, gVars.handle, Strings.str_replace("nwp_", strval(gVars.prefix), gVars.line));

                        break;
                    }

                    default:FileSystemOrSocket.fwrite(gVars.webEnv, gVars.handle, gVars.line);
                    }
                }
            }

            FileSystemOrSocket.fclose(gVars.webEnv, gVars.handle);
            JFileSystemOrSocket.chmod(gVars.webEnv, "../wp-config.php", 666);
            // Added by Numiton. Store the DB info in a properties file
            {
                Properties prop = new Properties();
                prop.setProperty(Wp_configPage.DB_NAME_KEY, dbname);
                prop.setProperty(Wp_configPage.DB_USER_KEY, uname);
                prop.setProperty(Wp_configPage.DB_PASSWORD_KEY, passwrd);
                prop.setProperty(Wp_configPage.DB_HOST_KEY, dbhost);
                prop.setProperty(Wp_configPage.DB_TABLE_PREFIX_KEY, strval(gVars.prefix));

                int iniFp = FileSystemOrSocket.fopen(gVars.webEnv, "../" + Wp_configPage.WP_CONFIG_PROPERTIES_FILE, "w");
                ByteArrayOutputStream iniOutStream = new ByteArrayOutputStream();
                prop.store(iniOutStream, "nWordPress Init Properties");

                String iniOutStreamStr = iniOutStream.toString();
                FileSystemOrSocket.fputs(gVars.webEnv, iniFp, iniOutStreamStr, Strings.strlen(iniOutStreamStr));
                FileSystemOrSocket.fclose(gVars.webEnv, iniFp);
            }

            this.display_header();
            echo(
                    gVars.webEnv,
                    "<p>All right sparky! You\'ve made it through this part of the installation. nWordPress can now communicate with your database. If you are ready, time now to&hellip;</p>\n\n<p><a href=\"install.php\" class=\"button\">Run the install</a></p>\n");

            break;
        }
        }

        return DEFAULT_VAL;
    }
}
