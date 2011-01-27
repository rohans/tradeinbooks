/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Scripts.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


public class WP_Scripts implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Scripts.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;
    public Array<_WP_Script> scripts = new Array<_WP_Script>();
    public Array<Object> queue = new Array<Object>();
    public Array<Object> to_print = new Array<Object>();
    public Array<Object> printed = new Array<Object>();
    public Array<Object> args = new Array<Object>();

    public WP_Scripts(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.default_scripts();
    }

    public void default_scripts() {
        Object visual_editor = null;
        Object mce_version = null;
        this.add("common", "/wp-admin/js/common.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20080318");
        this.add("sack", "/wp-includes/js/tw-sack.js", false, "1.6.1");
        this.add("quicktags", "/wp-includes/js/quicktags.js", false, "3958");
        this.localize("quicktags", "quicktagsL10n",
            new Array<Object>(
                new ArrayEntry<Object>("quickLinks", getIncluded(L10nPage.class, gVars, gConsts).__("(Quick Links)", "default")),
                new ArrayEntry<Object>("wordLookup", getIncluded(L10nPage.class, gVars, gConsts).__("Enter a word to look up:", "default")),
                new ArrayEntry<Object>(
                    "dictionaryLookup",
                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Dictionary lookup", "default"))),
                new ArrayEntry<Object>("lookup", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("lookup", "default"))),
                new ArrayEntry<Object>(
                    "closeAllOpenTags",
                    (((FormattingPage) PhpWeb.getIncluded(FormattingPage.class, gVars, gConsts))).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Close all open tags", "default"))),
                new ArrayEntry<Object>("closeTags", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("close tags", "default"))),
                new ArrayEntry<Object>("enterURL", getIncluded(L10nPage.class, gVars, gConsts).__("Enter the URL", "default")),
                new ArrayEntry<Object>("enterImageURL", getIncluded(L10nPage.class, gVars, gConsts).__("Enter the URL of the image", "default")),
                new ArrayEntry<Object>("enterImageDescription", getIncluded(L10nPage.class, gVars, gConsts).__("Enter a description of the image", "default"))));
        
        this.add("colorpicker", "/wp-includes/js/colorpicker.js", new Array<Object>(new ArrayEntry<Object>("prototype")), "3517");
        
		// Let a plugin replace the visual editor
        visual_editor = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("visual_editor", new Array<Object>(new ArrayEntry<Object>("tiny_mce")));
        this.add("editor", false, visual_editor, "20080321");
        
        this.add("editor_functions", "/wp-admin/js/editor.js", false, "20080325");
        
		// Modify this version when tinyMCE plugins are changed.
        mce_version = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("tiny_mce_version", "20080414");
        this.add("tiny_mce", "/wp-includes/js/tinymce/tiny_mce_config.php", new Array<Object>(new ArrayEntry<Object>("editor_functions")), mce_version);
        
        this.add("prototype", "/wp-includes/js/prototype.js", false, "1.6");
        
        this.add("wp-ajax-response", "/wp-includes/js/wp-ajax-response.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20080316");
        this.localize(
            "wp-ajax-response",
            "wpAjax",
            new Array<Object>(
                new ArrayEntry<Object>("noPerm", getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to do that.", "default")),
                new ArrayEntry<Object>("broken", getIncluded(L10nPage.class, gVars, gConsts).__("An unidentified error has occurred.", "default"))));
        
        this.add("autosave", "/wp-includes/js/autosave.js", new Array<Object>(new ArrayEntry<Object>("schedule"), new ArrayEntry<Object>("wp-ajax-response")), "20080424");
        
        this.add("wp-ajax", "/wp-includes/js/wp-ajax.js", new Array<Object>(new ArrayEntry<Object>("prototype")), "20070306");
        this.localize("wp-ajax", "WPAjaxL10n",
            new Array<Object>(
                new ArrayEntry<Object>("defaultUrl", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/admin-ajax.php"),
                new ArrayEntry<Object>("permText", getIncluded(L10nPage.class, gVars, gConsts).__("You do not have permission to do that.", "default")),
                new ArrayEntry<Object>("strangeText", getIncluded(L10nPage.class, gVars, gConsts).__("Something strange happened.  Try refreshing the page.", "default")),
                new ArrayEntry<Object>("whoaText", getIncluded(L10nPage.class, gVars, gConsts).__("Slow down, I\'m still sending your data!", "default"))));
        
        this.add("wp-lists", "/wp-includes/js/wp-lists.js", new Array<Object>(new ArrayEntry<Object>("wp-ajax-response")), "20080411");
        this.localize("wp-lists", "wpListL10n", new Array<Object>(new ArrayEntry<Object>("url", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/admin-ajax.php")));
        this.add("scriptaculous-root", "/wp-includes/js/scriptaculous/scriptaculous.js", new Array<Object>(new ArrayEntry<Object>("prototype")), "1.8.0");
        this.add("scriptaculous-builder", "/wp-includes/js/scriptaculous/builder.js", new Array<Object>(new ArrayEntry<Object>("scriptaculous-root")), "1.8.0");
        this.add("scriptaculous-dragdrop", "/wp-includes/js/scriptaculous/dragdrop.js",
            new Array<Object>(new ArrayEntry<Object>("scriptaculous-builder"), new ArrayEntry<Object>("scriptaculous-effects")), "1.8.0");
        this.add("scriptaculous-effects", "/wp-includes/js/scriptaculous/effects.js", new Array<Object>(new ArrayEntry<Object>("scriptaculous-root")), "1.8.0");
        this.add("scriptaculous-slider", "/wp-includes/js/scriptaculous/slider.js", new Array<Object>(new ArrayEntry<Object>("scriptaculous-effects")), "1.8.0");
        this.add("scriptaculous-sound", "/wp-includes/js/scriptaculous/sound.js", new Array<Object>(new ArrayEntry<Object>("scriptaculous-root")), "1.8.0");
        this.add("scriptaculous-controls", "/wp-includes/js/scriptaculous/controls.js", new Array<Object>(new ArrayEntry<Object>("scriptaculous-root")), "1.8.0");
        this.add("scriptaculous", "",
            new Array<Object>(new ArrayEntry<Object>("scriptaculous-dragdrop"), new ArrayEntry<Object>("scriptaculous-slider"), new ArrayEntry<Object>("scriptaculous-controls")), "1.8.0");
        this.add("cropper", "/wp-includes/js/crop/cropper.js", new Array<Object>(new ArrayEntry<Object>("scriptaculous-dragdrop")), "20070118");
        this.add("jquery", "/wp-includes/js/jquery/jquery.js", false, "1.2.3");
        this.add("jquery-form", "/wp-includes/js/jquery/jquery.form.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "2.02");
        this.add("jquery-color", "/wp-includes/js/jquery/jquery.color.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "2.0-4561");
        this.add("interface", "/wp-includes/js/jquery/interface.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "1.2");
        this.add("dimensions", "/wp-includes/js/jquery/jquery.dimensions.min.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "1.1.2");
        this.add("suggest", "/wp-includes/js/jquery/suggest.js", new Array<Object>(new ArrayEntry<Object>("dimensions")), "1.1");
        this.add("schedule", "/wp-includes/js/jquery/jquery.schedule.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20");
        this.add("thickbox", "/wp-includes/js/thickbox/thickbox.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "3.1");
        this.add("swfupload", "/wp-includes/js/swfupload/swfupload.js", false, "2.0.2");
        this.add("swfupload-degrade", "/wp-includes/js/swfupload/plugins/swfupload.graceful_degradation.js", new Array<Object>(new ArrayEntry<Object>("swfupload")), "2.0.2");
        this.localize("swfupload-degrade", "uploadDegradeOptions",
            new Array<Object>(new ArrayEntry<Object>("is_lighttpd_before_150", getIncluded(FunctionsPage.class, gVars, gConsts).is_lighttpd_before_150())));
        this.add("swfupload-queue", "/wp-includes/js/swfupload/plugins/swfupload.queue.js", new Array<Object>(new ArrayEntry<Object>("swfupload")), "2.0.2");
        this.add("swfupload-handlers", "/wp-includes/js/swfupload/handlers.js", new Array<Object>(new ArrayEntry<Object>("swfupload")), "2.0.2-20080407");
		// these error messages came from the sample swfupload js, they might need changing.
        this.localize(
                "swfupload-handlers",
                "swfuploadL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("queue_limit_exceeded", getIncluded(L10nPage.class, gVars, gConsts).__("You have attempted to queue too many files.", "default")),
                    new ArrayEntry<Object>("file_exceeds_size_limit",
                        QStrings.sprintf(
                            getIncluded(L10nPage.class, gVars, gConsts).__("This file is too big. Your php.ini upload_max_filesize is %s.", "default"),
                            Options.ini_get(gVars.webEnv, "upload_max_filesize"))),
                    new ArrayEntry<Object>("zero_byte_file", getIncluded(L10nPage.class, gVars, gConsts).__("This file is empty. Please try another.", "default")),
                    new ArrayEntry<Object>("invalid_filetype", getIncluded(L10nPage.class, gVars, gConsts).__("This file type is not allowed. Please try another.", "default")),
                    new ArrayEntry<Object>("default_error", getIncluded(L10nPage.class, gVars, gConsts).__("An error occurred in the upload. Please try again later.", "default")),
                    new ArrayEntry<Object>("missing_upload_url", getIncluded(L10nPage.class, gVars, gConsts).__("There was a configuration error. Please contact the server administrator.", "default")),
                    new ArrayEntry<Object>("upload_limit_exceeded", getIncluded(L10nPage.class, gVars, gConsts).__("You may only upload 1 file.", "default")),
                    new ArrayEntry<Object>("http_error", getIncluded(L10nPage.class, gVars, gConsts).__("HTTP error.", "default")),
                    new ArrayEntry<Object>("upload_failed", getIncluded(L10nPage.class, gVars, gConsts).__("Upload failed.", "default")),
                    new ArrayEntry<Object>("io_error", getIncluded(L10nPage.class, gVars, gConsts).__("IO error.", "default")),
                    new ArrayEntry<Object>("security_error", getIncluded(L10nPage.class, gVars, gConsts).__("Security error.", "default")),
                    new ArrayEntry<Object>("file_cancelled", getIncluded(L10nPage.class, gVars, gConsts).__("File cancelled.", "default")),
                    new ArrayEntry<Object>("upload_stopped", getIncluded(L10nPage.class, gVars, gConsts).__("Upload stopped.", "default")),
                    new ArrayEntry<Object>("dismiss", getIncluded(L10nPage.class, gVars, gConsts).__("Dismiss", "default")),
                    new ArrayEntry<Object>("crunching", getIncluded(L10nPage.class, gVars, gConsts).__("Crunching&hellip;", "default")),
                    new ArrayEntry<Object>("deleted", getIncluded(L10nPage.class, gVars, gConsts).__("Deleted", "default"))));
        
        this.add("jquery-ui-tabs", "/wp-includes/js/jquery/ui.tabs.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "3");

        if (getIncluded(QueryPage.class, gVars, gConsts).is_admin()) {
            this.add("ajaxcat", "/wp-admin/js/cat.js", new Array<Object>(new ArrayEntry<Object>("wp-lists")), "20071101");
            this.localize("ajaxcat", "catL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("add", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Add", "default"))),
                    new ArrayEntry<Object>("how", getIncluded(L10nPage.class, gVars, gConsts).__("Separate multiple categories with commas.", "default"))));
            this.add("admin-categories", "/wp-admin/js/categories.js", new Array<Object>(new ArrayEntry<Object>("wp-lists")), "20071031");
            this.add("admin-tags", "/wp-admin/js/tags.js", new Array<Object>(new ArrayEntry<Object>("wp-lists")), "20071031");
            this.add("admin-custom-fields", "/wp-admin/js/custom-fields.js", new Array<Object>(new ArrayEntry<Object>("wp-lists")), "20070823");
            this.add("password-strength-meter", "/wp-admin/js/password-strength-meter.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20070405");
            this.localize("password-strength-meter", "pwsL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("short", getIncluded(L10nPage.class, gVars, gConsts).__("Too short", "default")),
                    new ArrayEntry<Object>("bad", getIncluded(L10nPage.class, gVars, gConsts).__("Bad", "default")),
                    new ArrayEntry<Object>("good", getIncluded(L10nPage.class, gVars, gConsts).__("Good", "default")),
                    new ArrayEntry<Object>("strong", getIncluded(L10nPage.class, gVars, gConsts).__("Strong", "default"))));
            this.add("admin-comments", "/wp-admin/js/edit-comments.js", new Array<Object>(new ArrayEntry<Object>("wp-lists")), "20080311");
            this.localize("admin-comments", "adminCommentsL10n", new Array<Object>(new ArrayEntry<Object>("pending", getIncluded(L10nPage.class, gVars, gConsts).__("%i% pending", "default")))); // must look like: "# blah blah"
            this.add("admin-users", "/wp-admin/js/users.js", new Array<Object>(new ArrayEntry<Object>("wp-lists")), "20070823");
            this.add("admin-forms", "/wp-admin/js/forms.js", false, "20080317");
            this.add("xfn", "/wp-admin/js/xfn.js", false, "3517");
            this.add("upload", "/wp-admin/js/upload.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20070518");
            this.add("postbox", "/wp-admin/js/postbox.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20080128");
            this.localize("postbox", "postboxL10n",
                new Array<Object>(new ArrayEntry<Object>("requestFile", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/admin-ajax.php")));
            this.add("slug", "/wp-admin/js/slug.js", new Array<Object>(new ArrayEntry<Object>("jquery")), "20080208");
            this.localize("slug", "slugL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("requestFile", getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/admin-ajax.php"),
                    new ArrayEntry<Object>("save", getIncluded(L10nPage.class, gVars, gConsts).__("Save", "default")),
                    new ArrayEntry<Object>("cancel", getIncluded(L10nPage.class, gVars, gConsts).__("Cancel", "default"))));
            this.add("post", "/wp-admin/js/post.js",
                new Array<Object>(
                    new ArrayEntry<Object>("suggest"),
                    new ArrayEntry<Object>("jquery-ui-tabs"),
                    new ArrayEntry<Object>("wp-lists"),
                    new ArrayEntry<Object>("postbox"),
                    new ArrayEntry<Object>("slug")), "20080422");
            this.localize("post", "postL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("tagsUsed", getIncluded(L10nPage.class, gVars, gConsts).__("Tags used on this post:", "default")),
                    new ArrayEntry<Object>("add", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Add", "default"))),
                    new ArrayEntry<Object>("addTag", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Add new tag", "default"))),
                    new ArrayEntry<Object>("separate", (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Separate tags with commas", "default")),
                    new ArrayEntry<Object>("cancel", getIncluded(L10nPage.class, gVars, gConsts).__("Cancel", "default")),
                    new ArrayEntry<Object>("edit", getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default"))));
            this.add("page", "/wp-admin/js/page.js", new Array<Object>(new ArrayEntry<Object>("jquery"), new ArrayEntry<Object>("slug"), new ArrayEntry<Object>("postbox")), "20080318");
            this.localize("page", "postL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("cancel", getIncluded(L10nPage.class, gVars, gConsts).__("Cancel", "default")),
                    new ArrayEntry<Object>("edit", getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default"))));
            this.add("link", "/wp-admin/js/link.js", new Array<Object>(new ArrayEntry<Object>("jquery-ui-tabs"), new ArrayEntry<Object>("wp-lists"), new ArrayEntry<Object>("postbox")), "20080131");
            this.add("comment", "/wp-admin/js/comment.js", new Array<Object>(new ArrayEntry<Object>("postbox")), "20080219");
            this.localize("comment", "commentL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("cancel", getIncluded(L10nPage.class, gVars, gConsts).__("Cancel", "default")),
                    new ArrayEntry<Object>("edit", getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default"))));
            this.add("media-upload", "/wp-admin/js/media-upload.js", false, "20080109");
            this.localize("upload", "uploadL10n",
                new Array<Object>(
                    new ArrayEntry<Object>(
                        "browseTitle",
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Browse your files", "default"))),
                    new ArrayEntry<Object>("back", (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("&laquo; Back", "default")),
                    new ArrayEntry<Object>(
                        "directTitle",
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Direct link to file", "default"))),
                    new ArrayEntry<Object>("edit", getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default")),
                    new ArrayEntry<Object>("thumb", getIncluded(L10nPage.class, gVars, gConsts).__("Thumbnail", "default")),
                    new ArrayEntry<Object>("full", getIncluded(L10nPage.class, gVars, gConsts).__("Full size", "default")),
                    new ArrayEntry<Object>("icon", getIncluded(L10nPage.class, gVars, gConsts).__("Icon", "default")),
                    new ArrayEntry<Object>("title", (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Title", "default")),
                    new ArrayEntry<Object>("show", getIncluded(L10nPage.class, gVars, gConsts).__("Show:", "default")),
                    new ArrayEntry<Object>("link", getIncluded(L10nPage.class, gVars, gConsts).__("Link to:", "default")),
                    new ArrayEntry<Object>("file", getIncluded(L10nPage.class, gVars, gConsts).__("File", "default")),
                    new ArrayEntry<Object>("page", (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Page", "default")),
                    new ArrayEntry<Object>("none", getIncluded(L10nPage.class, gVars, gConsts).__("None", "default")),
                    new ArrayEntry<Object>(
                        "editorText",
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Send to editor &raquo;", "default"))),
                    new ArrayEntry<Object>("insert", getIncluded(L10nPage.class, gVars, gConsts).__("Insert", "default")),
                    new ArrayEntry<Object>("urlText", getIncluded(L10nPage.class, gVars, gConsts).__("URL", "default")),
                    new ArrayEntry<Object>("desc", getIncluded(L10nPage.class, gVars, gConsts).__("Description", "default")),
                    new ArrayEntry<Object>(
                        "deleteText",
                        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape((((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Delete File", "default"))),
                    new ArrayEntry<Object>("saveText", getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save &raquo;", "default"))),
                    new ArrayEntry<Object>(
                        "confirmText",
                        getIncluded(L10nPage.class, gVars, gConsts).__("Are you sure you want to delete the file \'%title%\'?\nClick ok to delete or cancel to go back.", "default"))));
            this.add("admin-widgets", "/wp-admin/js/widgets.js", new Array<Object>(new ArrayEntry<Object>("interface")), "20080407c");
            this.localize("admin-widgets", "widgetsL10n",
                new Array<Object>(
                    new ArrayEntry<Object>("add", getIncluded(L10nPage.class, gVars, gConsts).__("Add", "default")),
                    new ArrayEntry<Object>("edit", getIncluded(L10nPage.class, gVars, gConsts).__("Edit", "default")),
                    new ArrayEntry<Object>("cancel", (((L10nPage) PhpWeb.getIncluded(L10nPage.class, gVars, gConsts))).__("Cancel", "default"))));
            this.add("editor", "/wp-admin/js/editor.js", new Array<Object>(new ArrayEntry<Object>("tiny_mce")), "20080221");
        }
    }

    /**
     * Prints script tags
     * Prints the scripts passed to it or the print queue. Also prints all
     * necessary dependencies.
     * @param mixed handles (optional) Scripts to be printed. (void) prints
     * queue, (string) prints that script, (array of strings) prints
     * those scripts.
     * @return array Scripts that have been printed
     */
    public Array<Object> print_scripts(Object handles) {
        Array<Object> to_print = null;
        Object handle = null;
        String ver = null;
        String src = null;
        
		// Print the queue if nothing is passed.  If a string is passed, print that script.  If an array is passed, print those scripts.
        handles = (equal(false, handles)
            ? this.queue
            : new Array<Object>(handles));
        this.all_deps(handles);
        
        to_print = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("print_scripts_array", Array.array_keys(this.to_print));

        for (Map.Entry javaEntry591 : to_print.entrySet()) {
            handle = javaEntry591.getValue();

            if (!Array.in_array(handle, this.printed) && isset(this.scripts.getValue(handle))) {
                if (booleanval(this.scripts.getValue(handle).src)) { // Else it defines a group.
                    ver = (booleanval(this.scripts.getValue(handle).ver)
                        ? this.scripts.getValue(handle).ver
                        : strval(gVars.wp_db_version));

                    if (isset(this.args.getValue(handle))) {
                        ver = ver + "&amp;" + strval(this.args.getValue(handle));
                    }

                    src = (strictEqual(0, Strings.strpos(this.scripts.getValue(handle).src, "http://"))
                        ? this.scripts.getValue(handle).src
                        : (getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + this.scripts.getValue(handle).src));
                    src = this.scripts.getValue(handle).src;

                    if (!QRegExPerl.preg_match("|^https?://|", src)) {
                        src = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + src;
                    }

                    src = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("ver", ver, src);
                    src = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("script_loader_src", src)), null, "display");
                    this.print_scripts_l10n(handle);
                    echo(gVars.webEnv, "<script type=\'text/javascript\' src=\'" + src + "\'></script>\n");
                }

                this.printed.putValue(handle);
            }
        }

        this.to_print = new Array<Object>();

        return this.printed;
    }

    public void print_scripts_l10n(Object handle) {
        Object object_name = null;
        String eol = null;
        Object var = null;
        String val = null;

        if (empty(this.scripts.getValue(handle).l10n_object) || empty(this.scripts.getValue(handle).l10n) || !is_array(this.scripts.getValue(handle).l10n)) {
            return;
        }

        object_name = this.scripts.getValue(handle).l10n_object;
        echo(gVars.webEnv, "<script type=\'text/javascript\'>\n");
        echo(gVars.webEnv, "/* <![CDATA[ */\n");
        echo(gVars.webEnv, "\t" + strval(object_name) + " = {\n");
        eol = "";

        for (Map.Entry javaEntry592 : this.scripts.getValue(handle).l10n.entrySet()) {
            var = javaEntry592.getKey();
            val = strval(javaEntry592.getValue());
            echo(gVars.webEnv, eol + "\t\t" + strval(var) + ": \"" + getIncluded(FormattingPage.class, gVars, gConsts).js_escape(val) + "\"");
            eol = ",\n";
        }

        echo(gVars.webEnv, "\n\t}\n");
        echo(gVars.webEnv, "/* ]]> */\n");
        echo(gVars.webEnv, "</script>\n");
    }

    public boolean all_deps(Object handlesObj) {
        return all_deps(handlesObj, false);
    }

    /**
     * Determines dependencies of scripts
     * Recursively builds array of scripts to print taking dependencies into
     * account. Does NOT catch infinite loops.
     * @param mixed handles Accepts (string) script name or (array of strings)
     * script names
     * @param bool recursion Used internally when function calls itself
     */
    public boolean all_deps(Object handlesObj, boolean recursion) {
        String handle;
        boolean keep_going = false;
        Array<?> handles;

        if (!booleanval(handles = new Array<Object>(handlesObj))) {
            return false;
        }

        for (Map.Entry javaEntry593 : handles.entrySet()) {
            handle = strval(javaEntry593.getValue());

            Array<String> handleArray = Strings.explode("?", handle);

            if (isset(handleArray.getValue(1))) {
                this.args.putValue(handleArray.getValue(0), handleArray.getValue(1));
            }

            handle = handleArray.getValue(0);

            if (isset(this.to_print.getValue(handle))) { // Already grobbed it and its deps
                continue;
            }

            keep_going = true;

            if (!isset(this.scripts.getValue(handle))) {
                keep_going = false; // Script doesn't exist
            } else if (booleanval(this.scripts.getValue(handle).deps) && booleanval(Array.array_diff((Array) this.scripts.getValue(handle).deps, Array.array_keys(this.scripts)))) {
                keep_going = false; // Script requires deps which don't exist (not a necessary check.  efficiency?)
            } else if (booleanval(this.scripts.getValue(handle).deps) && !this.all_deps(this.scripts.getValue(handle).deps, true)) {
                keep_going = false; // Script requires deps which don't exist
            }

            if (!keep_going) { // Either script or its deps don't exist.
                if (recursion) {
                    return false; // Abort this branch.
                } else {
                    continue; // We're at the top level.  Move on to the next one.
                }
            }

            this.to_print.putValue(handle, true);
        }

        return true;
    }

    /**
     * Adds script
     * Adds the script only if no script of that name already exists
     * @param string handle Script name
     * @param string src Script url
     * @param array deps (optional) Array of script names on which this script
     * depends
     * @param string ver (optional) Script version (used for cache busting)
     * @return array Hierarchical array of dependencies
     */
    public boolean add(Object handle, Object src, Object deps, Object ver) {
        if (isset(this.scripts.getValue(handle))) {
            return false;
        }

        this.scripts.putValue(handle, new _WP_Script(gVars, gConsts, handle, src, deps, ver));

        return true;
    }

    /**
     * Localizes a script
     * Localizes only if script has already been added
     * @param string handle Script name
     * @param string object_name Name of JS object to hold l10n info
     * @param array l10n Array of JS var name => localized string
     * @return bool Successful localization
     */
    public boolean localize(String handle, String object_name, Array<Object> l10n) {
        if (!isset(this.scripts.getValue(handle))) {
            return false;
        }

        return this.scripts.getValue(handle).localize(object_name, l10n);
    }

    public void remove(Object handles) {
        Object handle = null;

        for (Map.Entry javaEntry594 : new Array<Object>(handles).entrySet()) {
            handle = javaEntry594.getValue();
            this.scripts.arrayUnset(handle);
        }
    }

    public void enqueue(Array<Object> handles) {
        String handle;

        for (Map.Entry javaEntry595 : handles.entrySet()) {
            handle = strval(javaEntry595.getValue());

            Array<String> handleArray = Strings.explode("?", handle);

            if (!Array.in_array(handleArray.getValue(0), this.queue) && isset(this.scripts.getValue(handleArray.getValue(0)))) {
                this.queue.putValue(handleArray.getValue(0));

                if (isset(handleArray.getValue(1))) {
                    this.args.putValue(handleArray.getValue(0), handleArray.getValue(1));
                }
            }
        }
    }

    public void dequeue(Array<Object> handles) {
        Object handle = null;

        for (Map.Entry javaEntry596 : handles.entrySet()) {
            handle = javaEntry596.getValue();
            this.queue.arrayUnset(handle);
        }
    }

    public Object query(Object handle) {
        return query(handle, "scripts");
    }

    public Object query(Object handle, String list) { // scripts, queue, or printed

        // Modified by Numiton
        if (equal(list, "scripts")) {
            if (isset(this.scripts.getValue(handle))) {
                return this.scripts.getValue(handle);
            }
        } else {
            if (equal(list, "queue")) {
                if (Array.in_array(handle, this.queue)) {
                    return true;
                }
            } else if (equal(list, "printed")) {
                if (Array.in_array(handle, this.printed)) {
                    return true;
                }
            }
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
