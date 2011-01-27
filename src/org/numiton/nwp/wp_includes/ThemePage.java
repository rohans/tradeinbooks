/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ThemePage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import static com.numiton.PhpCommonConstants.STRING_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.Custom_Image_Header;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.Directories;
import com.numiton.file.Directory;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class ThemePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ThemePage.class.getName());
    public Object wp_did_header;
    public Object custom_image_header;
    public Array<Array<Object>> wp_themes;

    @Override
    @RequestMapping("/wp-includes/theme.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/theme";
    }

    /**
     * Theme/template/stylesheet functions.
     */
    public String get_stylesheet() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("stylesheet", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("stylesheet")));
    }

    public String get_stylesheet_directory() {
        Object stylesheet = null;
        Object stylesheet_dir = null;
        stylesheet = get_stylesheet();
        stylesheet_dir = get_theme_root() + "/" + strval(stylesheet);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("stylesheet_directory", stylesheet_dir, stylesheet));
    }

    public String get_stylesheet_directory_uri() {
        Object stylesheet = null;
        Object stylesheet_dir_uri = null;
        stylesheet = get_stylesheet();
        stylesheet_dir_uri = get_theme_root_uri() + "/" + strval(stylesheet);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("stylesheet_directory_uri", stylesheet_dir_uri, stylesheet));
    }

    public String get_stylesheet_uri() {
        Object stylesheet_dir_uri = null;
        Object stylesheet_uri = null;
        stylesheet_dir_uri = get_stylesheet_directory_uri();
        stylesheet_uri = strval(stylesheet_dir_uri) + "/style.css";

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("stylesheet_uri", stylesheet_uri, stylesheet_dir_uri));
    }

    public String get_locale_stylesheet_uri() {
        Object stylesheet_dir_uri = null;
        Object dir = null;
        String locale = null;
        Object stylesheet_uri = null;
        stylesheet_dir_uri = get_stylesheet_directory_uri();
        dir = get_stylesheet_directory();
        locale = getIncluded(L10nPage.class, gVars, gConsts).get_locale();

        if (FileSystemOrSocket.file_exists(gVars.webEnv, strval(dir) + "/" + locale + ".css")) {
            stylesheet_uri = strval(stylesheet_dir_uri) + "/" + locale + ".css";
        } else if (!empty(gVars.wp_locale.text_direction) && FileSystemOrSocket.file_exists(gVars.webEnv, strval(dir) + "/" + gVars.wp_locale.text_direction + ".css")) {
            stylesheet_uri = strval(stylesheet_dir_uri) + "/" + gVars.wp_locale.text_direction + ".css";
        } else {
            stylesheet_uri = "";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("locale_stylesheet_uri", stylesheet_uri, stylesheet_dir_uri));
    }

    public String get_template() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("template", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("template")));
    }

    public String get_template_directory() {
        Object template = null;
        Object template_dir = null;
        template = get_template();
        template_dir = get_theme_root() + "/" + strval(template);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("template_directory", template_dir, template));
    }

    public String get_template_directory_uri() {
        Object template = null;
        Object template_dir_uri = null;
        template = get_template();
        template_dir_uri = get_theme_root_uri() + "/" + strval(template);

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("template_directory_uri", template_dir_uri, template));
    }

    public Array<Object> get_theme_data(String theme_file) {
        Array<Object> themes_allowed_tags = new Array<Object>();
        String theme_data = null;
        Array<Object> theme_name = new Array<Object>();
        String theme_uri;
        Array<Object> theme_uriArray = new Array<Object>();
        String description;
        Array<Object> descriptionArray = new Array<Object>();
        String author_uri = null;
        String author_uti = null;
        Array<Object> author_uriArray = new Array<Object>();
        String template = null;
        Array<Object> templateArray = new Array<Object>();
        String version = null;
        Array<Object> versionArray = new Array<Object>();
        String status = null;
        Array<Object> statusArray = new Array<Object>();
        Array tags = new Array();
        String name;
        String theme;
        Array author_name = new Array();
        String author = null;
        themes_allowed_tags = new Array<Object>(
                new ArrayEntry<Object>("a", new Array<Object>(new ArrayEntry<Object>("href", new Array<Object>()), new ArrayEntry<Object>("title", new Array<Object>()))),
                new ArrayEntry<Object>("abbr", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                new ArrayEntry<Object>("acronym", new Array<Object>(new ArrayEntry<Object>("title", new Array<Object>()))),
                new ArrayEntry<Object>("code", new Array<Object>()),
                new ArrayEntry<Object>("em", new Array<Object>()),
                new ArrayEntry<Object>("strong", new Array<Object>()));
        theme_data = Strings.implode("", FileSystemOrSocket.file(gVars.webEnv, theme_file));
        theme_data = Strings.str_replace("\\r", "\\n", theme_data);
        QRegExPerl.preg_match("|Theme Name:(.*)$|mi", theme_data, theme_name);
        QRegExPerl.preg_match("|Theme URI:(.*)$|mi", theme_data, theme_uriArray);
        QRegExPerl.preg_match("|Description:(.*)$|mi", theme_data, descriptionArray);

        if (QRegExPerl.preg_match("|Author URI:(.*)$|mi", theme_data, author_uriArray)) {
            author_uri = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.trim(strval(author_uriArray.getValue(1))), null, "display");
        } else {
            author_uti = "";
        }

        if (QRegExPerl.preg_match("|Template:(.*)$|mi", theme_data, templateArray)) {
            template = getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(templateArray.getValue(1))), themes_allowed_tags,
                    new Array<Object>(new ArrayEntry<Object>("http"),
                        new ArrayEntry<Object>("https"),
                        new ArrayEntry<Object>("ftp"),
                        new ArrayEntry<Object>("ftps"),
                        new ArrayEntry<Object>("mailto"),
                        new ArrayEntry<Object>("news"),
                        new ArrayEntry<Object>("irc"),
                        new ArrayEntry<Object>("gopher"),
                        new ArrayEntry<Object>("nntp"),
                        new ArrayEntry<Object>("feed"),
                        new ArrayEntry<Object>("telnet")));
        } else {
            template = "";
        }

        if (QRegExPerl.preg_match("|Version:(.*)|i", theme_data, versionArray)) {
            version = getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(versionArray.getValue(1))), themes_allowed_tags,
                    new Array<Object>(new ArrayEntry<Object>("http"),
                        new ArrayEntry<Object>("https"),
                        new ArrayEntry<Object>("ftp"),
                        new ArrayEntry<Object>("ftps"),
                        new ArrayEntry<Object>("mailto"),
                        new ArrayEntry<Object>("news"),
                        new ArrayEntry<Object>("irc"),
                        new ArrayEntry<Object>("gopher"),
                        new ArrayEntry<Object>("nntp"),
                        new ArrayEntry<Object>("feed"),
                        new ArrayEntry<Object>("telnet")));
        } else {
            version = "";
        }

        if (QRegExPerl.preg_match("|Status:(.*)|i", theme_data, statusArray)) {
            status = getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(statusArray.getValue(1))), themes_allowed_tags,
                    new Array<Object>(new ArrayEntry<Object>("http"),
                        new ArrayEntry<Object>("https"),
                        new ArrayEntry<Object>("ftp"),
                        new ArrayEntry<Object>("ftps"),
                        new ArrayEntry<Object>("mailto"),
                        new ArrayEntry<Object>("news"),
                        new ArrayEntry<Object>("irc"),
                        new ArrayEntry<Object>("gopher"),
                        new ArrayEntry<Object>("nntp"),
                        new ArrayEntry<Object>("feed"),
                        new ArrayEntry<Object>("telnet")));
        } else {
            status = "publish";
        }

        if (QRegExPerl.preg_match("|Tags:(.*)|i", theme_data, tags)) {
            tags = Array.array_map(
                    new Callback("trim", Strings.class),
                    Strings.explode(",",
                        getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(tags.getValue(1))), new Array<Object>(),
                            new Array<Object>(new ArrayEntry<Object>("http"),
                                new ArrayEntry<Object>("https"),
                                new ArrayEntry<Object>("ftp"),
                                new ArrayEntry<Object>("ftps"),
                                new ArrayEntry<Object>("mailto"),
                                new ArrayEntry<Object>("news"),
                                new ArrayEntry<Object>("irc"),
                                new ArrayEntry<Object>("gopher"),
                                new ArrayEntry<Object>("nntp"),
                                new ArrayEntry<Object>("feed"),
                                new ArrayEntry<Object>("telnet")))));
        } else {
            tags = new Array<Object>();
        }

        name = theme = getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(theme_name.getValue(1))), themes_allowed_tags,
                    new Array<Object>(new ArrayEntry<Object>("http"),
                        new ArrayEntry<Object>("https"),
                        new ArrayEntry<Object>("ftp"),
                        new ArrayEntry<Object>("ftps"),
                        new ArrayEntry<Object>("mailto"),
                        new ArrayEntry<Object>("news"),
                        new ArrayEntry<Object>("irc"),
                        new ArrayEntry<Object>("gopher"),
                        new ArrayEntry<Object>("nntp"),
                        new ArrayEntry<Object>("feed"),
                        new ArrayEntry<Object>("telnet")));
        theme_uri = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(Strings.trim(strval(theme_uriArray.getValue(1))), null, "display");
        description = getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(
                getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(descriptionArray.getValue(1))), themes_allowed_tags,
                    new Array<Object>(new ArrayEntry<Object>("http"),
                        new ArrayEntry<Object>("https"),
                        new ArrayEntry<Object>("ftp"),
                        new ArrayEntry<Object>("ftps"),
                        new ArrayEntry<Object>("mailto"),
                        new ArrayEntry<Object>("news"),
                        new ArrayEntry<Object>("irc"),
                        new ArrayEntry<Object>("gopher"),
                        new ArrayEntry<Object>("nntp"),
                        new ArrayEntry<Object>("feed"),
                        new ArrayEntry<Object>("telnet"))));

        if (QRegExPerl.preg_match("|Author:(.*)$|mi", theme_data, author_name)) {
            if (empty(author_uri)) {
                author = getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(author_name.getValue(1))), themes_allowed_tags,
                        new Array<Object>(new ArrayEntry<Object>("http"),
                            new ArrayEntry<Object>("https"),
                            new ArrayEntry<Object>("ftp"),
                            new ArrayEntry<Object>("ftps"),
                            new ArrayEntry<Object>("mailto"),
                            new ArrayEntry<Object>("news"),
                            new ArrayEntry<Object>("irc"),
                            new ArrayEntry<Object>("gopher"),
                            new ArrayEntry<Object>("nntp"),
                            new ArrayEntry<Object>("feed"),
                            new ArrayEntry<Object>("telnet")));
            } else {
                author = QStrings.sprintf(
                        "<a href=\"%1$s\" title=\"%2$s\">%3$s</a>",
                        author_uri,
                        getIncluded(L10nPage.class, gVars, gConsts).__("Visit author homepage", "default"),
                        getIncluded(KsesPage.class, gVars, gConsts).wp_kses(Strings.trim(strval(author_name.getValue(1))), themes_allowed_tags,
                            new Array<Object>(new ArrayEntry<Object>("http"),
                                new ArrayEntry<Object>("https"),
                                new ArrayEntry<Object>("ftp"),
                                new ArrayEntry<Object>("ftps"),
                                new ArrayEntry<Object>("mailto"),
                                new ArrayEntry<Object>("news"),
                                new ArrayEntry<Object>("irc"),
                                new ArrayEntry<Object>("gopher"),
                                new ArrayEntry<Object>("nntp"),
                                new ArrayEntry<Object>("feed"),
                                new ArrayEntry<Object>("telnet"))));
            }
        } else {
            author = getIncluded(L10nPage.class, gVars, gConsts).__("Anonymous", "default");
        }

        return new Array<Object>(
            new ArrayEntry<Object>("Name", name),
            new ArrayEntry<Object>("Title", theme),
            new ArrayEntry<Object>("URI", theme_uri),
            new ArrayEntry<Object>("Description", description),
            new ArrayEntry<Object>("Author", author),
            new ArrayEntry<Object>("Version", version),
            new ArrayEntry<Object>("Template", template),
            new ArrayEntry<Object>("Status", status),
            new ArrayEntry<Object>("Tags", tags));
    }

    public Array<Array<Object>> get_themes() {
        Array<Array<Object>> themes = new Array<Array<Object>>();
        Object theme_loc = null;
        String theme_root = null;
        int themes_dir = 0;
        String theme_dir = null;
        int stylish_dir = 0;
        boolean found_stylesheet = false;
        String theme_file = null;
        Array<Object> theme_files = new Array<Object>();
        String subdir = null;
        String subdir_name = null;
        int theme_subdir = 0;
        Array<Object> theme_data = new Array<Object>();
        String name = null;
        String title = null;
        String description = null;
        Object version = null;
        Object author = null;
        String template = null;
        String stylesheet = null;
        String screenshot = null;
        Object ext = null;
        String parent_dir = null;
        Array<String> stylesheet_files;
        Directory stylesheet_dir = null;
        String file = null;
        Array<String> template_files;
        Directory template_dir = null;
        Object suffix = null;
        String new_name = null;
        Array<Object> theme_names = new Array<Object>();
        Object theme_name = null;
        Object parent_theme_name = null;

        if (isset(wp_themes)) {
            return wp_themes;
        }

        themes = new Array<Array<Object>>();
        gVars.wp_broken_themes = new Array<Object>();
        theme_loc = theme_root = get_theme_root();

        if (!equal("/", gConsts.getABSPATH())) { // don't want to replace all forward slashes, see Trac #4541
            theme_loc = Strings.str_replace(gConsts.getABSPATH(), "", theme_root);
        }

    	// Files in wp-content/themes directory and one subdir down
        themes_dir = Directories.opendir(gVars.webEnv, theme_root);

        if (!booleanval(themes_dir)) {
            return new Array<Array<Object>>();
        }

        while (!strictEqual(theme_dir = Directories.readdir(gVars.webEnv, themes_dir), STRING_FALSE)) {
            if (FileSystemOrSocket.is_dir(gVars.webEnv, theme_root + "/" + theme_dir) && FileSystemOrSocket.is_readable(gVars.webEnv, theme_root + "/" + theme_dir)) {
                if (equal(Strings.getCharAt(theme_dir, 0), ".") || equal(theme_dir, "..") || equal(theme_dir, "CVS")) {
                    continue;
                }

                stylish_dir = Directories.opendir(gVars.webEnv, theme_root + "/" + theme_dir);
                found_stylesheet = false;

                while (!strictEqual(theme_file = Directories.readdir(gVars.webEnv, stylish_dir), STRING_FALSE)) {
                    if (equal(theme_file, "style.css")) {
                        theme_files.putValue(theme_dir + "/" + theme_file);
                        found_stylesheet = true;

                        break;
                    }
                }

                Directories.closedir(gVars.webEnv, stylish_dir);

                if (!found_stylesheet) { // look for themes in that dir
                    subdir = theme_root + "/" + theme_dir;
                    subdir_name = theme_dir;
                    theme_subdir = Directories.opendir(gVars.webEnv, subdir);

                    while (!strictEqual(theme_dir = Directories.readdir(gVars.webEnv, theme_subdir), STRING_FALSE)) {
                        if (FileSystemOrSocket.is_dir(gVars.webEnv, subdir + "/" + theme_dir) && FileSystemOrSocket.is_readable(gVars.webEnv, subdir + "/" + theme_dir)) {
                            if (equal(Strings.getCharAt(theme_dir, 0), ".") || equal(theme_dir, "..") || equal(theme_dir, "CVS")) {
                                continue;
                            }

                            stylish_dir = Directories.opendir(gVars.webEnv, subdir + "/" + theme_dir);
                            found_stylesheet = false;

                            while (!strictEqual(theme_file = Directories.readdir(gVars.webEnv, stylish_dir), STRING_FALSE)) {
                                if (equal(theme_file, "style.css")) {
                                    theme_files.putValue(subdir_name + "/" + theme_dir + "/" + theme_file);
                                    found_stylesheet = true;

                                    break;
                                }
                            }

                            Directories.closedir(gVars.webEnv, stylish_dir);
                        }
                    }

                    Directories.closedir(gVars.webEnv, theme_subdir);
                    gVars.wp_broken_themes.putValue(theme_dir,
                        new Array<Object>(new ArrayEntry<Object>("Name", theme_dir),
                            new ArrayEntry<Object>("Title", theme_dir),
                            new ArrayEntry<Object>("Description", getIncluded(L10nPage.class, gVars, gConsts).__("Stylesheet is missing.", "default"))));
                }
            }
        }

        if (FileSystemOrSocket.is_dir(gVars.webEnv, theme_dir)) {
            Directories.closedir(gVars.webEnv, themes_dir);
        }

        // Modified by Numiton
        if (!booleanval(themes_dir) || !booleanval(theme_files)) {
            return themes;
        }

        Array.sort(theme_files);

        for (Map.Entry javaEntry639 : new Array<Object>(theme_files).entrySet()) {
            theme_file = strval(javaEntry639.getValue());

            if (!FileSystemOrSocket.is_readable(gVars.webEnv, theme_root + "/" + theme_file)) {
                gVars.wp_broken_themes.putValue(theme_file,
                    new Array<Object>(new ArrayEntry<Object>("Name", theme_file),
                        new ArrayEntry<Object>("Title", theme_file),
                        new ArrayEntry<Object>("Description", getIncluded(L10nPage.class, gVars, gConsts).__("File not readable.", "default"))));

                continue;
            }

            theme_data = get_theme_data(theme_root + "/" + theme_file);
            name = strval(theme_data.getValue("Name"));
            title = strval(theme_data.getValue("Title"));
            description = getIncluded(FormattingPage.class, gVars, gConsts).wptexturize(strval(theme_data.getValue("Description")));
            version = theme_data.getValue("Version");
            author = theme_data.getValue("Author");
            template = strval(theme_data.getValue("Template"));
            stylesheet = FileSystemOrSocket.dirname(theme_file);
            screenshot = strval(false);

            for (Map.Entry javaEntry640 : new Array<Object>(new ArrayEntry<Object>("png"), new ArrayEntry<Object>("gif"), new ArrayEntry<Object>("jpg"), new ArrayEntry<Object>(
                        "jpeg")).entrySet()) {
                ext = javaEntry640.getValue();

                if (FileSystemOrSocket.file_exists(gVars.webEnv, theme_root + "/" + stylesheet + "/screenshot." + strval(ext))) {
                    screenshot = "screenshot." + strval(ext);

                    break;
                }
            }

            if (empty(name)) {
                name = FileSystemOrSocket.dirname(theme_file);
                title = name;
            }

            if (empty(template)) {
                if (FileSystemOrSocket.file_exists(gVars.webEnv, FileSystemOrSocket.dirname(theme_root + "/" + theme_file + "/index.php"))) {
                    template = FileSystemOrSocket.dirname(theme_file);
                } else {
                    continue;
                }
            }

            template = Strings.trim(template);

            if (!FileSystemOrSocket.file_exists(gVars.webEnv, theme_root + "/" + template + "/index.php")) {
                parent_dir = FileSystemOrSocket.dirname(FileSystemOrSocket.dirname(theme_file));

                if (FileSystemOrSocket.file_exists(gVars.webEnv, theme_root + "/" + parent_dir + "/" + template + "/index.php")) {
                    template = parent_dir + "/" + template;
                } else {
                    gVars.wp_broken_themes.putValue(name,
                        new Array<Object>(new ArrayEntry<Object>("Name", name),
                            new ArrayEntry<Object>("Title", title),
                            new ArrayEntry<Object>("Description", getIncluded(L10nPage.class, gVars, gConsts).__("Template is missing.", "default"))));

                    continue;
                }
            }

            stylesheet_files = new Array<String>();
            stylesheet_dir = Directories.dir(gVars.webEnv, theme_root + "/" + stylesheet);

            if (booleanval(stylesheet_dir)) {
                while (!strictEqual(file = stylesheet_dir.read(), STRING_FALSE)) {
                    if (!QRegExPerl.preg_match("|^\\.+$|", file) && QRegExPerl.preg_match("|\\.css$|", file)) {
                        stylesheet_files.putValue(strval(theme_loc) + "/" + stylesheet + "/" + file);
                    }
                }
            }

            template_files = new Array<String>();
            template_dir = Directories.dir(gVars.webEnv, theme_root + "/" + template);

            if (booleanval(template_dir)) {
                while (!strictEqual(file = template_dir.read(), STRING_FALSE)) {
                    if (!QRegExPerl.preg_match("|^\\.+$|", file) && QRegExPerl.preg_match("|\\.php$|", file)) {
                        template_files.putValue(strval(theme_loc) + "/" + template + "/" + file);
                    }
                }
            }

            String template_dirStr = FileSystemOrSocket.dirname(template_files.getValue(0));
            String stylesheet_dirStr = FileSystemOrSocket.dirname(stylesheet_files.getValue(0));

            if (empty(template_dirStr)) {
                template_dirStr = "/";
            }

            if (empty(stylesheet_dirStr)) {
                stylesheet_dirStr = "/";
            }

    		// Check for theme name collision.  This occurs if a theme is copied to
    		// a new theme directory and the theme header is not updated.  Whichever
    		// theme is first keeps the name.  Subsequent themes get a suffix applied.
    		// The Default and Classic themes always trump their pretenders.
            if (isset(themes.getValue(name))) {
                if ((equal("nWordPress Default", name) || equal("nWordPress Classic", name)) && (equal("default", stylesheet) || equal("classic", stylesheet))) {
    				// If another theme has claimed to be one of our default themes, move
    				// them aside.
                    suffix = themes.getArrayValue(name).getValue("Stylesheet");
                    new_name = name + "/" + strval(suffix);
                    themes.putValue(new_name, themes.getValue(name));
                    themes.getArrayValue(new_name).putValue("Name", new_name);
                } else {
                    name = name + "/" + stylesheet;
                }
            }

            themes.putValue(name,
                new Array<Object>(new ArrayEntry<Object>("Name", name), new ArrayEntry<Object>("Title", title), new ArrayEntry<Object>("Description", description),
                    new ArrayEntry<Object>("Author", author), new ArrayEntry<Object>("Version", version), new ArrayEntry<Object>("Template", template),
                    new ArrayEntry<Object>("Stylesheet", stylesheet), new ArrayEntry<Object>("Template Files", template_files), new ArrayEntry<Object>("Stylesheet Files", stylesheet_files),
                    new ArrayEntry<Object>("Template Dir", template_dirStr), new ArrayEntry<Object>("Stylesheet Dir", stylesheet_dirStr),
                    new ArrayEntry<Object>("Status", theme_data.getValue("Status")), new ArrayEntry<Object>("Screenshot", screenshot), new ArrayEntry<Object>("Tags", theme_data.getValue("Tags"))));
        }

    	// Resolve theme dependencies.
        theme_names = Array.array_keys(themes);

        for (Map.Entry javaEntry641 : new Array<Object>(theme_names).entrySet()) {
            theme_name = javaEntry641.getValue();
            themes.getArrayValue(theme_name).putValue("Parent Theme", "");

            if (!equal(themes.getArrayValue(theme_name).getValue("Stylesheet"), themes.getArrayValue(theme_name).getValue("Template"))) {
                for (Map.Entry javaEntry642 : new Array<Object>(theme_names).entrySet()) {
                    parent_theme_name = javaEntry642.getValue();

                    if (equal(themes.getArrayValue(parent_theme_name).getValue("Stylesheet"), themes.getArrayValue(parent_theme_name).getValue("Template")) &&
                            equal(themes.getArrayValue(parent_theme_name).getValue("Template"), themes.getArrayValue(theme_name).getValue("Template"))) {
                        themes.getArrayValue(theme_name).putValue("Parent Theme", themes.getArrayValue(parent_theme_name).getValue("Name"));

                        break;
                    }
                }
            }
        }

        wp_themes = themes;

        return themes;
    }

    public Array<Object> get_theme(String theme) {
        Array<Array<Object>> themes = get_themes();

        if (Array.array_key_exists(theme, themes)) {
            return themes.getArrayValue(theme);
        }

        return null;
    }

    public String get_current_theme() {
        Object theme = null;
        Array<Array<Object>> themes = new Array<Array<Object>>();
        Array<Object> theme_names = new Array<Object>();
        Object current_template = null;
        Object current_stylesheet = null;
        String current_theme = null;
        Object theme_name = null;

        if (booleanval(theme = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("current_theme"))) {
            return strval(theme);
        }

        themes = get_themes();
        theme_names = Array.array_keys(themes);
        current_template = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("template");
        current_stylesheet = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("stylesheet");
        current_theme = "nWordPress Default";

        if (booleanval(themes)) {
            for (Map.Entry javaEntry643 : new Array<Object>(theme_names).entrySet()) {
                theme_name = javaEntry643.getValue();

                if (equal(themes.getArrayValue(theme_name).getValue("Stylesheet"), current_stylesheet) && equal(themes.getArrayValue(theme_name).getValue("Template"), current_template)) {
                    current_theme = strval(themes.getArrayValue(theme_name).getValue("Name"));

                    break;
                }
            }
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("current_theme", current_theme);

        return current_theme;
    }

    public String get_theme_root() {
        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("theme_root", gConsts.getABSPATH() + "wp-content/themes"));
    }

    public String get_theme_root_uri() {
        return strval(
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "theme_root_uri",
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-content/themes",
                getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl")));
    }

    public String get_query_template(String type) {
        String template = null;
        template = "";
        type = QRegExPerl.preg_replace("|[^a-z0-9-]+|", "", type);

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/" + type + ".php")) {
            template = gConsts.getTEMPLATEPATH() + "/" + type + ".php";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters(type + "_template", template));
    }

    public String get_404_template() {
        return get_query_template("404");
    }

    public String get_archive_template() {
        return get_query_template("archive");
    }

    public String get_author_template() {
        return get_query_template("author");
    }

    public String get_category_template() {
        String template = null;
        template = "";

        if (FileSystemOrSocket.file_exists(
                    gVars.webEnv,
                    gConsts.getTEMPLATEPATH() + "/category-" + getIncluded(FunctionsPage.class, gVars, gConsts).absint(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("cat")) + ".php")) {
            template = gConsts.getTEMPLATEPATH() + "/category-" + getIncluded(FunctionsPage.class, gVars, gConsts).absint(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("cat")) + ".php";
        } else if (false)/*Modified by Numiton. TODO Add support for multiple themes*/
         {
            template = gConsts.getTEMPLATEPATH() + "/category.php";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("category_template", template));
    }

    public String get_tag_template() {
        String template = null;
        template = "";

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/tag-" + getIncluded(QueryPage.class, gVars, gConsts).get_query_var("tag") + ".php")) {
            template = gConsts.getTEMPLATEPATH() + "/tag-" + getIncluded(QueryPage.class, gVars, gConsts).get_query_var("tag") + ".php";
        } else if (false)/*Modified by Numiton. TODO Add support for multiple themes*/
         {
            template = gConsts.getTEMPLATEPATH() + "/tag.php";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tag_template", template));
    }

    public String get_taxonomy_template() {
        String template = null;
        String taxonomy = null;
        String term = null;
        template = "";
        taxonomy = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("taxonomy"));
        term = strval(getIncluded(QueryPage.class, gVars, gConsts).get_query_var("term"));

        if (booleanval(taxonomy) && booleanval(term) && FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/taxonomy-" + taxonomy + "-" + term + ".php")) {
            template = gConsts.getTEMPLATEPATH() + "/taxonomy-" + taxonomy + "-" + term + ".php";
        } else if (booleanval(taxonomy) && FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/taxonomy-" + taxonomy + ".php")) {
            template = gConsts.getTEMPLATEPATH() + "/taxonomy-" + taxonomy + ".php";
        } else if (false)/*Modified by Numiton. TODO Add support for multiple themes*/
         {
            template = gConsts.getTEMPLATEPATH() + "/taxonomy.php";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("taxonomy_template", template));
    }

    public String get_date_template() {
        return get_query_template("date");
    }

    public String get_home_template() {
        String template = null;
        template = "";

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/home.php")) {
            template = gConsts.getTEMPLATEPATH() + "/home.php";
        } else if (true)/*Modified by Numiton. TODO Add support for multiple themes*/
         {
            template = gConsts.getTEMPLATEPATH() + "/index.php";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("home_template", template));
    }

    public String get_page_template() {
        int id = 0;
        String template = null;
        id = intval(StdClass.getValue(gVars.wp_query.post, "ID"));
        template = strval(getIncluded(PostPage.class, gVars, gConsts).get_post_meta(id, "_wp_page_template", true));

        if (equal("default", template)) {
            template = "";
        }

        if (!empty(template) && FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/" + template)) {
            template = gConsts.getTEMPLATEPATH() + "/" + template;
        } else if (true)/*Modified by Numiton. TODO Add support for multiple themes*/
         {
            template = gConsts.getTEMPLATEPATH() + "/page.php";
        } else {
            template = "";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("page_template", template));
    }

    public String get_paged_template() {
        return get_query_template("paged");
    }

    public String get_search_template() {
        return get_query_template("search");
    }

    public String get_single_template() {
        return get_query_template("single");
    }

    public String get_attachment_template() {
        Array<String> type = new Array<String>();
        String template = null;
        type = Strings.explode("/", strval(gVars.posts.getValue(0).fields.getValue("post_mime_type")));

        if (booleanval(template = get_query_template(type.getValue(0)))) {
            return template;
        } else if (booleanval(template = get_query_template(type.getValue(1)))) {
            return template;
        } else if (booleanval(template = get_query_template(type.getValue(0) + "_" + type.getValue(1)))) {
            return template;
        } else {
            return get_query_template("attachment");
        }
    }

    public String get_comments_popup_template() {
        String template = null;

        if (FileSystemOrSocket.file_exists(gVars.webEnv, gConsts.getTEMPLATEPATH() + "/comments-popup.php")) {
            template = gConsts.getTEMPLATEPATH() + "/comments-popup.php";
        } else {
            template = get_theme_root() + "/default/comments-popup.php";
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comments_popup_template", template));
    }

    public void load_template(String _template_file, Class templateClass) {
        if (templateClass == null) {
            // Do nothing
            return;
        }

        if (is_array(gVars.wp_query.query_vars)) {
            gVars.posts = (Array<StdClass>) Array.extractVar(gVars.wp_query.query_vars, "posts", gVars.posts, Array.EXTR_SKIP);
            gVars.post = (StdClass) Array.extractVar(gVars.wp_query.query_vars, "post", gVars.post, Array.EXTR_SKIP);
            wp_did_header = (Boolean) Array.extractVar(gVars.wp_query.query_vars, "wp_did_header", wp_did_header, Array.EXTR_SKIP);
            gVars.wp_did_template_redirect = Array.extractVar(gVars.wp_query.query_vars, "wp_did_template_redirect", gVars.wp_did_template_redirect, Array.EXTR_SKIP);
            gVars.wp_rewrite = (WP_Rewrite) Array.extractVar(gVars.wp_query.query_vars, "wp_rewrite", gVars.wp_rewrite, Array.EXTR_SKIP);
            gVars.wp_version = strval(Array.extractVar(gVars.wp_query.query_vars, "wp_version", gVars.wp_version, Array.EXTR_SKIP));
            gVars.wpdb = (wpdb) Array.extractVar(gVars.wp_query.query_vars, "wpdb", gVars.wpdb, Array.EXTR_SKIP);
            gVars.wp = (WP) Array.extractVar(gVars.wp_query.query_vars, "wp", gVars.wp, Array.EXTR_SKIP);
            gVars.id = Array.extractVar(gVars.wp_query.query_vars, "id", gVars.id, Array.EXTR_SKIP);
            gVars.comment = (StdClass) Array.extractVar(gVars.wp_query.query_vars, "comment", gVars.comment, Array.EXTR_SKIP);
            gVars.user_ID = intval(Array.extractVar(gVars.wp_query.query_vars, "user_ID", gVars.user_ID, Array.EXTR_SKIP));
        }

        requireOnce(gVars, gConsts, templateClass);
    }

    public void locale_stylesheet() {
        Object stylesheet = null;
        stylesheet = get_locale_stylesheet_uri();

        if (empty(stylesheet)) {
            return;
        }

        echo(gVars.webEnv, "<link rel=\"stylesheet\" href=\"" + strval(stylesheet) + "\" type=\"text/css\" media=\"screen\" />");
    }

    public void switch_theme(String template, String stylesheet) {
        String theme = null;
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("template", template);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("stylesheet", stylesheet);
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("current_theme");
        theme = get_current_theme();
        getIncluded(PluginPage.class, gVars, gConsts).do_action("switch_theme", theme);
    }

    public boolean validate_current_theme() {
    	// Don't validate during an install/upgrade.
        if (gConsts.isWP_INSTALLINGDefined()) {
            return true;
        }

        if (!equal(get_template(), "default") && !FileSystemOrSocket.file_exists(gVars.webEnv, get_template_directory() + "/index.php")) {
            switch_theme("default", "default");

            return false;
        }

        if (!equal(get_stylesheet(), "default") && !FileSystemOrSocket.file_exists(gVars.webEnv, get_template_directory() + "/style.css")) {
            switch_theme("default", "default");

            return false;
        }

        return true;
    }

    public String get_theme_mod(String name, String _default) {
        String theme = null;
        Array<Object> mods = new Array<Object>();
        theme = get_current_theme();
        mods = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mods_" + theme);

        if (isset(mods.getValue(name))) {
            return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("theme_mod_" + name, mods.getValue(name)));
        }

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("theme_mod_" + name, QStrings.sprintf(_default, get_template_directory_uri(), get_stylesheet_directory_uri())));
    }

    public void set_theme_mod(String name, Object value) {
        String theme = null;
        Array<Object> mods = new Array<Object>();
        theme = get_current_theme();
        mods = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mods_" + theme);
        mods.putValue(name, value);
        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("mods_" + theme, mods);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("mods_" + theme, "options");
    }

    public void remove_theme_mod(String name) {
        String theme = null;
        Array<Object> mods = new Array<Object>();
        theme = get_current_theme();
        mods = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).get_option("mods_" + theme);

        if (!isset(mods.getValue(name))) {
            return;
        }

        mods.arrayUnset(name);

        if (empty(mods)) {
            remove_theme_mods();
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option("mods_" + theme, mods);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("mods_" + theme, "options");
    }

    public void remove_theme_mods() {
        String theme = null;
        theme = get_current_theme();
        getIncluded(FunctionsPage.class, gVars, gConsts).delete_option("mods_" + theme);
    }

    public String get_header_textcolor() {
        return get_theme_mod("header_textcolor", "HEADER_TEXTCOLOR");
    }

    public void header_textcolor() {
        echo(gVars.webEnv, get_header_textcolor());
    }

    public String get_header_image() {
        return get_theme_mod("header_image", "HEADER_IMAGE");
    }

    public String header_image() {
        echo(gVars.webEnv, get_header_image());

        return "";
    }

    public void add_custom_image_header(Array<Object> header_callback, Array<Object> admin_header_callback) {
        if (!empty(header_callback)) {
            getIncluded(PluginPage.class, gVars, gConsts).add_action("wp_head", header_callback, 10, 1);
        }

        if (!getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
            return;
        }

        custom_image_header = new Custom_Image_Header(gVars, gConsts, admin_header_callback);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_menu", new Array<Object>(new ArrayEntry<Object>(custom_image_header), new ArrayEntry<Object>("init")), 10, 1);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
