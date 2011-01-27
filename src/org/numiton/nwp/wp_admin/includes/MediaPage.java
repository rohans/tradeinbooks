/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MediaPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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

import static com.numiton.PhpCommonConstants.BOOLEAN_FALSE;
import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.numiton.nwp.wp_includes.TaxonomyPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.*;
import com.numiton.Math;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller(value = "wp_admin/includes/MediaPage")
@Scope("request")
public class MediaPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(MediaPage.class.getName());

    /**
     * Generated in place of local variable 'avail_post_mime_types' from method
     * 'media_upload_library_form' because it is used inside an inner class.
     */
    Object media_upload_library_form_avail_post_mime_types = null;

    // Added by Numiton
    private int limit_filter_start;

    @Override
    @RequestMapping("/wp-admin/includes/media.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/media";
    }

    public Array<Object> media_upload_tabs() {
        Array<Object> _default_tabs = new Array<Object>();
        _default_tabs = new Array<Object>(
                new ArrayEntry<Object>("type", getIncluded(L10nPage.class, gVars, gConsts).__("Choose File", "default")), // handler action suffix => tab text
                new ArrayEntry<Object>("gallery", getIncluded(L10nPage.class, gVars, gConsts).__("Gallery", "default")),
                new ArrayEntry<Object>("library", getIncluded(L10nPage.class, gVars, gConsts).__("Media Library", "default")));

        return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("media_upload_tabs", _default_tabs);
    }

    public Array<Object> update_gallery_tab(Array<Object> tabs) {
        int attachments = 0;

        if (!isset(gVars.webEnv._REQUEST.getValue("post_id"))) {
            tabs.arrayUnset("gallery");

            return tabs;
        }

        if (booleanval(gVars.webEnv._REQUEST.getValue("post_id"))) {
            attachments = intval(
                    gVars.wpdb.get_var(
                        gVars.wpdb.prepare("SELECT count(*) FROM " + gVars.wpdb.posts + " WHERE post_type = \'attachment\' AND post_parent = %d", gVars.webEnv._REQUEST.getValue("post_id"))));
        }

        tabs.putValue("gallery", QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("Gallery (%s)", "default"), "<span id=\'attachments-count\'>" + strval(attachments) + "</span>"));

        return tabs;
    }

    public void the_media_upload_tabs() {
        Array<Object> tabs = null;
        Object current = null;
        Array<Object> keys = new Array<Object>();
        String _class = null;
        Object callback = null;
        String href = null;
        String link = null;
        Object text = null;
        tabs = media_upload_tabs();

        if (!empty(tabs)) {
            echo(gVars.webEnv, "<ul id=\'sidemenu\'>\n");

            if (isset(gVars.webEnv._GET.getValue("tab")) && Array.array_key_exists(gVars.webEnv._GET.getValue("tab"), tabs)) {
                current = gVars.webEnv._GET.getValue("tab");
            } else {
                keys = Array.array_keys(tabs);
                current = Array.array_shift(keys);
            }

            for (Map.Entry javaEntry148 : tabs.entrySet()) {
                callback = javaEntry148.getKey();
                text = javaEntry148.getValue();
                _class = "";

                if (equal(current, callback)) {
                    _class = " class=\'current\'";
                }

                href = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                        new Array<Object>(new ArrayEntry<Object>("tab", callback), new ArrayEntry<Object>("s", false), new ArrayEntry<Object>("paged", false),
                            new ArrayEntry<Object>("post_mime_type", false), new ArrayEntry<Object>("m", false)));
                link = "<a href=\'" + href + "\'" + _class + ">" + strval(text) + "</a>";
                echo(gVars.webEnv, "\t<li id=\'tab-" + strval(callback) + "\'>" + link + "</li>\n");
            }

            echo(gVars.webEnv, "</ul>\n");
        }
    }

    public String get_image_send_to_editor(int id, String alt, String title, String align, String url, String rel, String size) {
        String html = null;
        html = (((org.numiton.nwp.wp_includes.MediaPage) getIncluded(org.numiton.nwp.wp_includes.MediaPage.class, gVars, gConsts))).get_image_tag(id, alt, title, align, size);
        rel = (booleanval(rel)
            ? (" rel=\"attachment wp-att-" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(id)) + "\"")
            : "");

        if (booleanval(url)) {
            html = "<a href=\'" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(url) + "\'" + rel + ">" + html + "</a>";
        }

        html = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("image_send_to_editor", html, id, alt, title, align, url, size));

        return html;
    }

    public void media_send_to_editor(String html) {
        echo(gVars.webEnv, "<script type=\"text/javascript\">\n<!--\ntop.send_to_editor(\'");
        echo(gVars.webEnv, Strings.addslashes(gVars.webEnv, html));
        echo(gVars.webEnv, "\');\ntop.tb_remove();\n-->\n</script>\n\t");
        System.exit();
    }

    /**
     * this handles the file upload POST itself, creating the attachment post
     */
    public Object media_handle_upload(String file_id, Object post_id, Array<Object> post_data) {
        Array<Object> overrides = new Array<Object>();
        Array<Object> file = new Array<Object>();
        Object url = null;
        Object type = null;
        String title = null;
        String content = null;
        Array<Object> image_meta = new Array<Object>();
        Array<Object> attachment = new Array<Object>();
        Object id;
        int post_parent = 0;
        overrides = new Array<Object>(new ArrayEntry<Object>("test_form", false));
        file = getIncluded(FilePage.class, gVars, gConsts).wp_handle_upload(gVars.webEnv._FILES.getArrayValue(file_id), overrides);

        if (isset(file.getValue("error"))) {
            return new WP_Error(gVars, gConsts, "upload_error", strval(file.getValue("error")));
        }

        url = file.getValue("url");
        type = file.getValue("type");

        String fileStr = strval(file.getValue("file"));
        title = QRegExPerl.preg_replace("/\\.[^.]+$/", "", FileSystemOrSocket.basename(fileStr));
        content = "";

        // use image exif/iptc data for title and caption defaults if possible
        if (booleanval(image_meta = getIncluded(ImagePage.class, gVars, gConsts).wp_read_image_metadata(fileStr))) {
            if (booleanval(Strings.trim(strval(image_meta.getValue("title"))))) {
                title = strval(image_meta.getValue("title"));
            }

            if (booleanval(Strings.trim(strval(image_meta.getValue("caption"))))) {
                content = strval(image_meta.getValue("caption"));
            }
        }

        // Construct the attachment array
        attachment = Array.array_merge(new Array<Object>(
                    new ArrayEntry<Object>("post_mime_type", type),
                    new ArrayEntry<Object>("guid", url),
                    new ArrayEntry<Object>("post_parent", post_id),
                    new ArrayEntry<Object>("post_title", title),
                    new ArrayEntry<Object>("post_content", content)), post_data);
        
        // Save the data
        id = getIncluded(PostPage.class, gVars, gConsts).wp_insert_attachment(attachment, fileStr, post_parent);

        if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
            getIncluded(PostPage.class, gVars, gConsts).wp_update_attachment_metadata(id, getIncluded(ImagePage.class, gVars, gConsts).wp_generate_attachment_metadata(intval(id), fileStr));
        }

        return id;
    }

 // wrap iframe content (produced by $content_func) in a doctype, html head/body etc
 // any additional function args will be passed to content_func
    public void wp_iframe(String content_func, /* ... */ Object... vargs) {
        Array<Object> args = new Array<Object>();
        echo(gVars.webEnv,
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" ");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_xml_ns", "");
        echo(gVars.webEnv, " ");
        getIncluded(General_templatePage.class, gVars, gConsts).language_attributes("html");
        echo(gVars.webEnv, ">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("html_type");
        echo(gVars.webEnv, "; charset=");
        echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("blog_charset"));
        echo(gVars.webEnv, "\" />\n<title>");
        getIncluded(General_templatePage.class, gVars, gConsts).bloginfo("name");
        echo(gVars.webEnv, " &rsaquo; ");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Uploads", "default");
        echo(gVars.webEnv, " &#8212; ");
        getIncluded(L10nPage.class, gVars, gConsts)._e("nWordPress", "default");
        echo(gVars.webEnv, "</title>\n");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/global");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("wp-admin");
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/colors");
        echo(
                gVars.webEnv,
                "<script type=\"text/javascript\">\n//<![CDATA[\nfunction addLoadEvent(func) {if ( typeof wpOnload!=\'function\'){wpOnload=func;}else{ var oldonload=wpOnload;wpOnload=function(){oldonload();func();}}}\n//]]>\n</script>\n");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_print_scripts", "");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_head", "");

        if (is_string(content_func)) {
            getIncluded(PluginPage.class, gVars, gConsts).do_action("admin_head_" + content_func, "");
        }

        echo(gVars.webEnv, "</head>\n<body");

        if (isset(gVars.body_id)) {
            echo(gVars.webEnv, " id=\"" + gVars.body_id + "\"");
        }

        echo(gVars.webEnv, ">\n");

        // Modified by Numiton
        args = FunctionHandling.func_get_args(FunctionHandling.buildTotalArgs(content_func, vargs));

        args = Array.array_slice(args, 1);
        FunctionHandling.call_user_func_array(new Callback(content_func, this), args);
        echo(gVars.webEnv, "</body>\n</html>\n");
    }

    public void media_buttons() {
        int uploading_iframe_ID = 0;
        String context = null;
        String media_upload_iframe_src = null;
        Object media_title = null;
        Object image_upload_iframe_src = null;
        Object image_title = null;
        Object video_upload_iframe_src = null;
        Object video_title = null;
        Object audio_upload_iframe_src = null;
        Object audio_title = null;
        String out = null;
        uploading_iframe_ID = equal(0, gVars.post_ID)
            ? gVars.temp_ID
            : intval(gVars.post_ID);
        context = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("media_buttons_context", getIncluded(L10nPage.class, gVars, gConsts).__("Add media: %s", "default")));
        media_upload_iframe_src = "media-upload.php?post_id=" + strval(uploading_iframe_ID);
        media_title = getIncluded(L10nPage.class, gVars, gConsts).__("Add Media", "default");
        image_upload_iframe_src = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("image_upload_iframe_src", media_upload_iframe_src + "&amp;type=image");
        image_title = getIncluded(L10nPage.class, gVars, gConsts).__("Add an Image", "default");
        video_upload_iframe_src = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("video_upload_iframe_src", media_upload_iframe_src + "&amp;type=video");
        video_title = getIncluded(L10nPage.class, gVars, gConsts).__("Add Video", "default");
        audio_upload_iframe_src = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("audio_upload_iframe_src", media_upload_iframe_src + "&amp;type=audio");
        audio_title = getIncluded(L10nPage.class, gVars, gConsts).__("Add Audio", "default");
        out = "\n\t<a href=\"" + strval(image_upload_iframe_src) + "&amp;TB_iframe=1&amp;height=500&amp;width=640\" class=\"thickbox\" title=\'" + strval(image_title) +
            "\'><img src=\'images/media-button-image.gif\' alt=\'" + strval(image_title) + "\' /></a>\n\t<a href=\"" + strval(video_upload_iframe_src) +
            "&amp;TB_iframe=1&amp;height=500&amp;width=640\" class=\"thickbox\" title=\'" + strval(video_title) + "\'><img src=\'images/media-button-video.gif\' alt=\'" + strval(video_title) +
            "\' /></a>\n\t<a href=\"" + strval(audio_upload_iframe_src) + "&amp;TB_iframe=1&amp;height=500&amp;width=640\" class=\"thickbox\" title=\'" + strval(audio_title) +
            "\'><img src=\'images/media-button-music.gif\' alt=\'" + strval(audio_title) + "\' /></a>\n\t<a href=\"" + media_upload_iframe_src +
            "&amp;TB_iframe=1&amp;height=500&amp;width=640\" class=\"thickbox\" title=\'" + strval(media_title) + "\'><img src=\'images/media-button-other.gif\' alt=\'" + strval(media_title) +
            "\' /></a>\n\n";
        QStrings.printf(gVars.webEnv, context, out);
    }

    public void media_buttons_head() {
        Object siteurl = null;
        siteurl = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl");
        echo(
                gVars.webEnv,
                "<style type=\'text/css\' media=\'all\'>\n\t@import \'" + strval(siteurl) +
                "/wp-includes/js/thickbox/thickbox.css?1\';\n\tdiv#TB_title {\n\t\tbackground-color: #222222;\n\t\tcolor: #cfcfcf;\n\t}\n\tdiv#TB_title a, div#TB_title a:visited {\n\t\tcolor: #cfcfcf;\n\t}\n</style>\n");
    }

    public void media_admin_css() {
        getIncluded(General_templatePage.class, gVars, gConsts).wp_admin_css("css/media");
    }

    public Object media_upload_form_handler() {
        Array<Object> post = new Array<Object>();
        Array<Object> _post = null;
        int attachment_id;
        Array<Object> attachment = new Array<Object>();
        Array<Object> errors = new Array<Object>();
        Object t = null;
        Array<Object> keys = new Array<Object>();
        int send_id = 0;
        String html = null;
        String rel = null;
        getIncluded(PluggablePage.class, gVars, gConsts).check_admin_referer("media-form", "_wpnonce");

        if (!empty(gVars.webEnv._POST.getValue("attachments"))) {
            for (Map.Entry javaEntry149 : (Set<Map.Entry>) gVars.webEnv._POST.getArrayValue("attachments").entrySet()) {
                attachment_id = intval(javaEntry149.getKey());
                attachment = (Array<Object>) javaEntry149.getValue();
                post = Array.arrayCopy(_post = (Array<Object>) getIncluded(PostPage.class, gVars, gConsts).get_post(attachment_id, gConsts.getARRAY_A(), "raw"));

                if (isset(attachment.getValue("post_content"))) {
                    post.putValue("post_content", attachment.getValue("post_content"));
                }

                if (isset(attachment.getValue("post_title"))) {
                    post.putValue("post_title", attachment.getValue("post_title"));
                }

                if (isset(attachment.getValue("post_excerpt"))) {
                    post.putValue("post_excerpt", attachment.getValue("post_excerpt"));
                }

                post = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attachment_fields_to_save", post, attachment);

                if (isset(post.getValue("errors"))) {
                    errors.putValue(attachment_id, post.getValue("errors"));
                    post.arrayUnset("errors");
                }

                if (!equal(post, _post)) {
                    getIncluded(PostPage.class, gVars, gConsts).wp_update_post(post);
                }

                for (Map.Entry javaEntry150 : (((org.numiton.nwp.wp_includes.MediaPage) getIncluded(org.numiton.nwp.wp_includes.MediaPage.class, gVars, gConsts))).get_attachment_taxonomies(post)
                                                                .entrySet()) {
                    t = javaEntry150.getValue();

                    if (isset(attachment.getValue(t))) {
                        getIncluded(TaxonomyPage.class, gVars, gConsts).wp_set_object_terms(attachment_id,
                            Array.array_map(new Callback("trim", Strings.class), QRegExPerl.preg_split("/,+/", strval(attachment.getValue(t)))), strval(t), false);
                    }
                }
            }
        }

        if (isset(gVars.webEnv._POST.getValue("insert-gallery"))) {
            media_send_to_editor("[gallery]");
        }

        if (isset(gVars.webEnv._POST.getValue("send"))) {
            keys = Array.array_keys(gVars.webEnv._POST.getArrayValue("send"));
            send_id = intval(Array.array_shift(keys));
            attachment = gVars.webEnv._POST.getArrayValue("attachments").getArrayValue(send_id);
            html = strval(attachment.getValue("post_title"));

            if (!empty(attachment.getValue("url"))) {
                if ((BOOLEAN_FALSE != Strings.strpos(strval(attachment.getValue("url")), "attachment_id")) ||
                        !strictEqual(
                            BOOLEAN_FALSE,
                            Strings.strpos(strval(attachment.getValue("url")), getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.webEnv._POST.getValue("post_id"), false)))) {
                    rel = " rel=\'attachment wp-att-" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(send_id)) + "\'";
                }

                html = "<a href=\'" + strval(attachment.getValue("url")) + "\'" + rel + ">" + html + "</a>";
            }

            html = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("media_send_to_editor", html, send_id, attachment));
            media_send_to_editor(html);
        }

        return errors;
    }

    public String media_upload_image() {
        Object id = null;
        Array<Object> errors = new Array<Object>();
        String src = null;
        Object alt = null;
        Object align = null;
        Object _class = null;
        String html = null;
        Object _return;

        /* Do not change type */
        if (isset(gVars.webEnv._POST.getValue("html-upload")) && !empty(gVars.webEnv._FILES)) {
        	// Upload File button was clicked
            id = media_handle_upload("async-upload", gVars.webEnv._REQUEST.getValue("post_id"), new Array<Object>());
            gVars.webEnv._FILES = null;

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
                errors.putValue("upload_error", id);
                id = false;
            }
        }

        if (!empty(gVars.webEnv._POST.getValue("insertonlybutton"))) {
            src = strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("src"));

            if (!empty(src) && (BOOLEAN_FALSE == Strings.strpos(src, "://"))) {
                src = "http://" + src;
            }

            alt = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("alt")));

            if (isset(gVars.webEnv._POST.getArrayValue("insertonly").getValue("align"))) {
                align = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("align")));
                _class = " class=\'align" + strval(align) + "\'";
            }

            if (!empty(src)) {
                html = "<img src=\'" + src + "\' alt=\'" + strval(alt) + "\'" + strval(_class) + " />";
            }

            media_send_to_editor(html);

            return "";
        }

        if (!empty(gVars.webEnv._POST)) {
            _return = media_upload_form_handler();

            if (is_string(_return)) {
                return strval(_return);
            }

            if (is_array(_return)) {
                errors = (Array<Object>) _return;
            }
        }

        if (isset(gVars.webEnv._POST.getValue("save"))) {
            errors.putValue("upload_notice", getIncluded(L10nPage.class, gVars, gConsts).__("Saved.", "default"));
        }

        wp_iframe("media_upload_type_form", "image", errors, id);

        return "";
    }

    public String media_upload_audio() {
        Object id = null;
        Array<Object> errors = new Array<Object>();
        String href = null;
        String title = null;
        String html = null;
        Object _return = null;

        /* Do not change type */
        if (isset(gVars.webEnv._POST.getValue("html-upload")) && !empty(gVars.webEnv._FILES)) {
        	// Upload File button was clicked
            id = media_handle_upload("async-upload", gVars.webEnv._REQUEST.getValue("post_id"), new Array<Object>());
            gVars.webEnv._FILES = null;

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
                errors.putValue("upload_error", id);
                id = false;
            }
        }

        if (!empty(gVars.webEnv._POST.getValue("insertonlybutton"))) {
            href = strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("href"));

            if (!empty(href) && (BOOLEAN_FALSE == Strings.strpos(href, "://"))) {
                href = "http://" + href;
            }

            title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("title")));

            if (empty(title)) {
                title = FileSystemOrSocket.basename(href);
            }

            if (!empty(title) && !empty(href)) {
                html = "<a href=\'" + href + "\' >" + title + "</a>";
            }

            media_send_to_editor(html);
        }

        if (!empty(gVars.webEnv._POST)) {
            _return = media_upload_form_handler();

            if (is_string(_return)) {
                return strval(_return);
            }

            if (is_array(_return)) {
                errors = (Array<Object>) _return;
            }
        }

        if (isset(gVars.webEnv._POST.getValue("save"))) {
            errors.putValue("upload_notice", getIncluded(L10nPage.class, gVars, gConsts).__("Saved.", "default"));
        }

        wp_iframe("media_upload_type_form", "audio", errors, id);

        return "";
    }

    public String media_upload_video() {
        Object id = null;
        Array<Object> errors = new Array<Object>();
        String href = null;
        String title = null;
        String html = null;
        Object _return = null;

        /* Do not change type */
        if (isset(gVars.webEnv._POST.getValue("html-upload")) && !empty(gVars.webEnv._FILES)) {
        	// Upload File button was clicked
            id = media_handle_upload("async-upload", gVars.webEnv._REQUEST.getValue("post_id"), new Array<Object>());
            gVars.webEnv._FILES = null;

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
                errors.putValue("upload_error", id);
                id = false;
            }
        }

        if (!empty(gVars.webEnv._POST.getValue("insertonlybutton"))) {
            href = strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("href"));

            if (!empty(href) && (BOOLEAN_FALSE == Strings.strpos(href, "://"))) {
                href = "http://" + href;
            }

            title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("title")));

            if (empty(title)) {
                title = FileSystemOrSocket.basename(href);
            }

            if (!empty(title) && !empty(href)) {
                html = "<a href=\'" + href + "\' >" + title + "</a>";
            }

            media_send_to_editor(html);
        }

        if (!empty(gVars.webEnv._POST)) {
            _return = media_upload_form_handler();

            if (is_string(_return)) {
                return strval(_return);
            }

            if (is_array(_return)) {
                errors = (Array<Object>) _return;
            }
        }

        if (isset(gVars.webEnv._POST.getValue("save"))) {
            errors.putValue("upload_notice", getIncluded(L10nPage.class, gVars, gConsts).__("Saved.", "default"));
        }

        wp_iframe("media_upload_type_form", "video", errors, id);

        return "";
    }

    public String media_upload_file() {
        Object id = null;
        Array<Object> errors = new Array<Object>();
        String href = null;
        String title = null;
        String html = null;
        Object _return = null;

        /* Do not change type */
        if (isset(gVars.webEnv._POST.getValue("html-upload")) && !empty(gVars.webEnv._FILES)) {
        	// Upload File button was clicked
            id = media_handle_upload("async-upload", gVars.webEnv._REQUEST.getValue("post_id"), new Array<Object>());
            gVars.webEnv._FILES = null;

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
                errors.putValue("upload_error", id);
                id = false;
            }
        }

        if (!empty(gVars.webEnv._POST.getValue("insertonlybutton"))) {
            href = strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("href"));

            if (!empty(href) && (BOOLEAN_FALSE == Strings.strpos(href, "://"))) {
                href = "http://" + href;
            }

            title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._POST.getArrayValue("insertonly").getValue("title")));

            if (empty(title)) {
                title = FileSystemOrSocket.basename(href);
            }

            if (!empty(title) && !empty(href)) {
                html = "<a href=\'" + href + "\' >" + title + "</a>";
            }

            media_send_to_editor(html);
        }

        if (!empty(gVars.webEnv._POST)) {
            _return = media_upload_form_handler();

            if (is_string(_return)) {
                return strval(_return);
            }

            if (is_array(_return)) {
                errors = (Array<Object>) _return;
            }
        }

        if (isset(gVars.webEnv._POST.getValue("save"))) {
            errors.putValue("upload_notice", getIncluded(L10nPage.class, gVars, gConsts).__("Saved.", "default"));
        }

        wp_iframe("media_upload_type_form", "file", errors, id);

        return "";
    }

    public String media_upload_gallery() {
        Object _return;

        /* Do not change type */
        Array<Object> errors = new Array<Object>();

        if (!empty(gVars.webEnv._POST)) {
            _return = media_upload_form_handler();

            if (is_string(_return)) {
                return strval(_return);
            }

            if (is_array(_return)) {
                errors = (Array<Object>) _return;
            }
        }

        wp_iframe("media_upload_gallery_form", errors);

        return "";
    }

    public String media_upload_library() {
        Object _return;

        /* Do not change type */
        Array<Object> errors = new Array<Object>();

        if (!empty(gVars.webEnv._POST)) {
            _return = media_upload_form_handler();

            if (is_string(_return)) {
                return strval(_return);
            }

            if (is_array(_return)) {
                errors = (Array<Object>) _return;
            }
        }

        wp_iframe("media_upload_library_form", errors);

        return "";
    }

    public Array<Object> image_attachment_fields_to_edit(Array<Object> form_fields, StdClass post) {
        Object thumb = null;

        if (equal(Strings.substr(strval(StdClass.getValue(post, "post_mime_type")), 0, 5), "image")) {
            form_fields.getArrayValue("post_title").putValue("required", true);
            form_fields.getArrayValue("post_excerpt").putValue("label", getIncluded(L10nPage.class, gVars, gConsts).__("Caption", "default"));
            form_fields.getArrayValue("post_excerpt").getArrayValue("helps").putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Alternate text, e.g. \"The Mona Lisa\"", "default"));
            form_fields.getArrayValue("post_content").putValue("label", getIncluded(L10nPage.class, gVars, gConsts).__("Description", "default"));
            thumb = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_thumb_url(intval(StdClass.getValue(post, "ID")));
            form_fields.putValue(
                    "align",
                    new Array<Object>(
                            new ArrayEntry<Object>("label", getIncluded(L10nPage.class, gVars, gConsts).__("Alignment", "default")),
                            new ArrayEntry<Object>("input", "html"),
                            new ArrayEntry<Object>(
                                    "html",
                                    "\n\t\t\t\t<input type=\'radio\' name=\'attachments[" + StdClass.getValue(post, "ID") + "][align]\' id=\'image-align-none-" + StdClass.getValue(post, "ID") +
                                    "\' value=\'none\' checked=\'checked\' />\n\t\t\t\t<label for=\'image-align-none-" + StdClass.getValue(post, "ID") + "\' class=\'align image-align-none-label\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("None", "default") + "</label>\n\t\t\t\t<input type=\'radio\' name=\'attachments[" + StdClass.getValue(post, "ID") +
                                    "][align]\' id=\'image-align-left-" + StdClass.getValue(post, "ID") + "\' value=\'left\' />\n\t\t\t\t<label for=\'image-align-left-" +
                                    StdClass.getValue(post, "ID") + "\' class=\'align image-align-left-label\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("Left", "default") +
                                    "</label>\n\t\t\t\t<input type=\'radio\' name=\'attachments[" + StdClass.getValue(post, "ID") + "][align]\' id=\'image-align-center-" +
                                    StdClass.getValue(post, "ID") + "\' value=\'center\' />\n\t\t\t\t<label for=\'image-align-center-" + StdClass.getValue(post, "ID") +
                                    "\' class=\'align image-align-center-label\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("Center", "default") +
                                    "</label>\n\t\t\t\t<input type=\'radio\' name=\'attachments[" + StdClass.getValue(post, "ID") + "][align]\' id=\'image-align-right-" +
                                    StdClass.getValue(post, "ID") + "\' value=\'right\' />\n\t\t\t\t<label for=\'image-align-right-" + StdClass.getValue(post, "ID") +
                                    "\' class=\'align image-align-right-label\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("Right", "default") + "</label>\n")));
            form_fields.putValue(
                    "image-size",
                    new Array<Object>(
                            new ArrayEntry<Object>("label", getIncluded(L10nPage.class, gVars, gConsts).__("Size", "default")),
                            new ArrayEntry<Object>("input", "html"),
                            new ArrayEntry<Object>(
                                    "html",
                                    "\n\t\t\t\t" +
                                    (booleanval(thumb)
                                    ? ("<input type=\'radio\' name=\'attachments[" + StdClass.getValue(post, "ID") + "][image-size]\' id=\'image-size-thumb-" + StdClass.getValue(post, "ID") +
                                    "\' value=\'thumbnail\' />\n\t\t\t\t<label for=\'image-size-thumb-" + StdClass.getValue(post, "ID") + "\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("Thumbnail", "default") + "</label>\n\t\t\t\t")
                                    : "") + "<input type=\'radio\' name=\'attachments[" + StdClass.getValue(post, "ID") + "][image-size]\' id=\'image-size-medium-" + StdClass.getValue(post, "ID") +
                                    "\' value=\'medium\' checked=\'checked\' />\n\t\t\t\t<label for=\'image-size-medium-" + StdClass.getValue(post, "ID") + "\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("Medium", "default") + "</label>\n\t\t\t\t<input type=\'radio\' name=\'attachments[" +
                                    StdClass.getValue(post, "ID") + "][image-size]\' id=\'image-size-full-" + StdClass.getValue(post, "ID") +
                                    "\' value=\'full\' />\n\t\t\t\t<label for=\'image-size-full-" + StdClass.getValue(post, "ID") + "\'>" +
                                    getIncluded(L10nPage.class, gVars, gConsts).__("Full size", "default") + "</label>")));
        }

        return form_fields;
    }

    public Array<Object> media_single_attachment_fields_to_edit(Array<Object> form_fields, Object post) {
        form_fields.arrayUnset("url");
        form_fields.arrayUnset("align");
        form_fields.arrayUnset("image-size");

        return form_fields;
    }

    public Array<Object> image_attachment_fields_to_save(Array<Object> post, Object attachment) {
        if (equal(Strings.substr(strval(post.getValue("post_mime_type")), 0, 5), "image")) {
            if (equal(Strings.strlen(Strings.trim(strval(post.getValue("post_title")))), 0)) {
                post.putValue("post_title", QRegExPerl.preg_replace("/\\.\\w+$/", "", FileSystemOrSocket.basename(strval(post.getValue("guid")))));
                post.getArrayValue("errors").getArrayValue("post_title").getArrayValue("errors").putValue(
                    getIncluded(L10nPage.class, gVars, gConsts).__("Empty Title filled from filename.", "default"));
            }
        }

        return post;
    }

    public String image_media_send_to_editor(String html, int attachment_id, Array<Object> attachment) {
        StdClass post;
        String url = null;
        String align = null;
        String size = null;
        boolean rel = false;
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(attachment_id, gConsts.getOBJECT(), "raw");

        if (equal(Strings.substr(strval(StdClass.getValue(post, "post_mime_type")), 0, 5), "image")) {
            url = strval(attachment.getValue("url"));

            if (isset(attachment.getValue("align"))) {
                align = strval(attachment.getValue("align"));
            } else {
                align = "none";
            }

            if (!empty(attachment.getValue("image-size"))) {
                size = strval(attachment.getValue("image-size"));
            } else {
                size = "medium";
            }

            rel = equal(url, getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(attachment_id));

            return get_image_send_to_editor(attachment_id, strval(attachment.getValue("post_excerpt")), strval(attachment.getValue("post_title")), align, url, strval(rel), size);
        }

        return html;
    }

    public Array<Object> get_attachment_fields_to_edit(Object postObj, /* Do not change type */
        Object errors) {
        StdClass edit_post;
        String file;
        String link = null;
        Array<Object> form_fields = new Array<Object>();
        Array<Object> t = new Array<Object>();
        String taxonomy = null;
        Array<Object> terms = null;
        Array<String> values = new Array<String>();
        StdClass term = null;
        StdClass post = new StdClass();

        if (is_int(postObj)) {
            post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(postObj, gConsts.getOBJECT(), "raw");
        } else if (is_array(postObj)) {
            post = Array.toStdClass((Array) postObj);
        } else {
            post = (StdClass) postObj;
        }

        edit_post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).sanitize_post(post, "edit");
        file = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(intval(StdClass.getValue(post, "ID")));
        link = getIncluded(Link_templatePage.class, gVars, gConsts).get_attachment_link(intval(StdClass.getValue(post, "ID")));
        form_fields = new Array<Object>(
                    new ArrayEntry<Object>("post_title",
                        new Array<Object>(new ArrayEntry<Object>("label", getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default")),
                            new ArrayEntry<Object>("value", StdClass.getValue(edit_post, "post_title")))),
                    new ArrayEntry<Object>("post_excerpt",
                        new Array<Object>(new ArrayEntry<Object>("label", getIncluded(L10nPage.class, gVars, gConsts).__("Caption", "default")),
                            new ArrayEntry<Object>("value", StdClass.getValue(edit_post, "post_excerpt")))),
                    new ArrayEntry<Object>("post_content",
                        new Array<Object>(new ArrayEntry<Object>("label", getIncluded(L10nPage.class, gVars, gConsts).__("Description", "default")),
                            new ArrayEntry<Object>("value", StdClass.getValue(edit_post, "post_content")),
                            new ArrayEntry<Object>("input", "textarea"))),
                    new ArrayEntry<Object>(
                            "url",
                            new Array<Object>(
                                    new ArrayEntry<Object>("label", getIncluded(L10nPage.class, gVars, gConsts).__("Link URL", "default")),
                                    new ArrayEntry<Object>("input", "html"),
                                    new ArrayEntry<Object>(
                                            "html",
                                            "\n\t\t\t\t<input type=\'text\' name=\'attachments[" + StdClass.getValue(post, "ID") + "][url]\' value=\'" +
                                            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(file) + "\' /><br />\n\t\t\t\t<button type=\'button\' class=\'button url-" +
                                            StdClass.getValue(post, "ID") + "\' value=\'\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("None", "default") +
                                            "</button>\n\t\t\t\t<button type=\'button\' class=\'button url-" + StdClass.getValue(post, "ID") + "\' value=\'" +
                                            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(file) + "\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("File URL", "default") +
                                            "</button>\n\t\t\t\t<button type=\'button\' class=\'button url-" + StdClass.getValue(post, "ID") + "\' value=\'" +
                                            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(link) + "\'>" + getIncluded(L10nPage.class, gVars, gConsts).__("Post URL", "default") +
                                            "</button>\n\t\t\t\t<script type=\'text/javascript\'>\n\t\t\t\tjQuery(\'button.url-" + StdClass.getValue(post, "ID") +
                                            "\').bind(\'click\', function(){jQuery(this).siblings(\'input\').val(this.value);});\n\t\t\t\t</script>\n"),
                                    new ArrayEntry<Object>("helps", getIncluded(L10nPage.class, gVars, gConsts).__("Enter a link URL or click above for presets.", "default")))));

        for (Map.Entry javaEntry151 : (((org.numiton.nwp.wp_includes.MediaPage) getIncluded(org.numiton.nwp.wp_includes.MediaPage.class, gVars, gConsts))).get_attachment_taxonomies(post).entrySet()) {
            taxonomy = strval(javaEntry151.getValue());
            t = new Array<Object>(getIncluded(TaxonomyPage.class, gVars, gConsts).get_taxonomy(taxonomy));

            if (empty(t.getValue("label"))) {
                t.putValue("label", taxonomy);
            }

            if (empty(t.getValue("args"))) {
                t.putValue("args", new Array<Object>());
            }

            terms = getIncluded(TaxonomyPage.class, gVars, gConsts).get_object_term_cache(StdClass.getValue(post, "ID"), taxonomy);

            if (empty(terms)) {
                terms = (Array<Object>) getIncluded(TaxonomyPage.class, gVars, gConsts).wp_get_object_terms(StdClass.getValue(post, "ID"), taxonomy, t.getValue("args"));
            }

            values = new Array<String>();

            for (Map.Entry javaEntry152 : terms.entrySet()) {
                term = (StdClass) javaEntry152.getValue();
                values.putValue(StdClass.getValue(term, "name"));
            }

            t.putValue("value", Strings.join(", ", values));
            form_fields.putValue(taxonomy, t);
        }

        // Merge default fields with their errors, so any key passed with the error (e.g. 'error', 'helps', 'value') will replace the default
    	// The recursive merge is easily traversed with array casting: foreach( (array) $things as $thing )
        form_fields = Array.array_merge_recursive(form_fields, new Array<Object>(errors));
        form_fields = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("attachment_fields_to_edit", form_fields, post);

        return form_fields;
    }

    public Object get_media_items(int post_id, Array<Object> errors) {
        StdClass post = null;
        Array<Object> attachments = new Array<Object>();
        StdClass attachment = null;
        Object item = null;
        int id = 0;
        Object output = null;

        if (booleanval(post_id)) {
            post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(post_id, gConsts.getOBJECT(), "raw");

            if (booleanval(post) && equal(StdClass.getValue(post, "post_type"), "attachment")) {
                attachments = new Array<Object>(new ArrayEntry<Object>(StdClass.getValue(post, "ID"), post));
            } else {
                attachments = getIncluded(PostPage.class, gVars, gConsts)
                                  .get_children("post_parent=" + strval(post_id) + "&post_type=attachment&orderby=menu_order ASC, ID&order=DESC", gConsts.getOBJECT());
            }
        } else {
            if (is_array(gVars.wp_the_query.posts)) {
                for (Map.Entry javaEntry153 : gVars.wp_the_query.posts.entrySet()) {
                    attachment = (StdClass) javaEntry153.getValue();
                    attachments.putValue(StdClass.getValue(attachment, "ID"), attachment);
                }
            }
        }

        if (empty(attachments)) {
            return "";
        }

        for (Map.Entry javaEntry154 : attachments.entrySet()) {
            id = intval(javaEntry154.getKey());
            attachment = (StdClass) javaEntry154.getValue();

            if (booleanval(item = get_media_item(id, new Array<Object>(new ArrayEntry<Object>("errors", isset(errors.getValue(id))
                                        ? errors.getValue(id)
                                            : null))))) {
                output = strval(output) + "\n<div id=\'media-item-" + strval(id) + "\' class=\'media-item child-of-" + StdClass.getValue(attachment, "post_parent") +
                    " preloaded\'><div class=\'progress\'><div class=\'bar\'></div></div><div id=\'media-upload-error-" + strval(id) + "\'></div><div class=\'filename\'></div>" + strval(item) +
                    "\n</div>";
            }
        }

        return output;
    }

    public Object get_media_item(int attachment_id, Object args) {
        Array<Object> default_args = new Array<Object>();
        String thumb_url = null;
        Object title_label = null;
        Object description_label = null;
        Object tags_label = null;
        Object toggle_on = null;
        Object toggle_off = null;
        StdClass post = null;
        String filename = null;
        Object title = null;
        Object description = null;
        Array<Object> _tags = null;
        String tags;
        StdClass tag = null;
        Array<Object> keys = new Array<Object>();
        String type = null;
        Array<Object> form_fields = new Array<Object>();
        Object errors = null;
        Object toggle = null;
        String _class = null;
        Object toggle_links = null;
        Object display_title = null;
        Object item = null;
        Array<Object> defaults = new Array<Object>();
        Object delete_href = null;
        Object send = null;
        Object delete = null;
        Array<Object> hidden_fields = new Array<Object>();
        String id;
        Array<Object> field = new Array<Object>();
        Object name = null;
        String required = null;
        Array<Object> extra_rows = new Array<Object>();
        Object error = null;
        Object html = null;
        Array<Object> rows = null;
        String value = null;
        default_args = new Array<Object>(new ArrayEntry<Object>("errors", null), new ArrayEntry<Object>("send", true), new ArrayEntry<Object>("delete", true), new ArrayEntry<Object>("toggle", true));

        Array argsArray = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, default_args);
        thumb_url = strval(Array.extractVar(argsArray, "thumb_url", thumb_url, Array.EXTR_SKIP));
        errors = Array.extractVar(argsArray, "errors", errors, Array.EXTR_SKIP);
        toggle = Array.extractVar(argsArray, "toggle", toggle, Array.EXTR_SKIP);
        send = Array.extractVar(argsArray, "send", send, Array.EXTR_SKIP);
        delete = Array.extractVar(argsArray, "delete", delete, Array.EXTR_SKIP);

        Array thumb_urlArray;

        if (booleanval(attachment_id = attachment_id) && booleanval(thumb_urlArray = getIncluded(Post_templatePage.class, gVars, gConsts).get_attachment_icon_src(attachment_id, false))) {
            thumb_url = strval(thumb_urlArray.getValue(0));
        } else {
            return false;
        }

        title_label = getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default");
        description_label = getIncluded(L10nPage.class, gVars, gConsts).__("Description", "default");
        tags_label = getIncluded(L10nPage.class, gVars, gConsts).__("Tags", "default");
        toggle_on = getIncluded(L10nPage.class, gVars, gConsts).__("Show", "default");
        toggle_off = getIncluded(L10nPage.class, gVars, gConsts).__("Hide", "default");
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(attachment_id, gConsts.getOBJECT(), "raw");
        filename = FileSystemOrSocket.basename(strval(StdClass.getValue(post, "guid")));
        title = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(post, "post_title")));
        description = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(post, "post_content")));

        if (booleanval(_tags = (Array<Object>) getIncluded(Category_templatePage.class, gVars, gConsts).get_the_tags(attachment_id))) {
            Array tagsArray = new Array();

            for (Map.Entry javaEntry155 : _tags.entrySet()) {
                tag = (StdClass) javaEntry155.getValue();
                tagsArray.putValue(StdClass.getValue(tag, "name"));
            }

            tags = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(Strings.join(", ", tagsArray));
        }

        if (!empty(gVars.post_mime_types)) {
            keys = Array.array_keys(getIncluded(PostPage.class, gVars, gConsts).wp_match_mime_types(Array.array_keys(gVars.post_mime_types), StdClass.getValue(post, "post_mime_type")));
            type = strval(Array.array_shift(keys));
            type = "<input type=\'hidden\' id=\'type-of-" + strval(attachment_id) + "\' value=\'" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(type) + "\' />";
        } else {
            type = ""; // Added by Numiton
        }

        form_fields = get_attachment_fields_to_edit(post, errors);

        if (booleanval(toggle)) {
            _class = (empty(errors)
                ? "startclosed"
                : "startopen");
            toggle_links = "\n\t<a class=\'toggle describe-toggle-on\' href=\'#\'>" + strval(toggle_on) + "</a>\n\t<a class=\'toggle describe-toggle-off\' href=\'#\'>" + strval(toggle_off) + "</a>";
        } else {
            _class = "form-table";
            toggle_links = "";
        }

        display_title = ((!empty(title))
            ? strval(title)
            : filename); // $title shouldn't ever be empty, but just in case
        
        item = "\n\t" + type + "\n\t" + strval(toggle_links) + "\n\t<div class=\'filename new\'>" + strval(display_title) + "</div>\n\t<table class=\'slidetoggle describe " + _class +
            "\'>\n\t\t<thead class=\'media-item-info\'>\n\t\t<tr>\n\t\t\t<td class=\'A1B1\' rowspan=\'4\'><img class=\'thumbnail\' src=\'" + thumb_url + "\' alt=\'\' /></td>\n\t\t\t<td>" + filename +
            "</td>\n\t\t</tr>\n\t\t<tr><td>" + StdClass.getValue(post, "post_mime_type") + "</td></tr>\n\t\t<tr><td>" +
            getIncluded(FunctionsPage.class, gVars, gConsts)
                .mysql2date(strval(StdClass.getValue(post, "post_date")), strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("time_format")), true) + "</td></tr>\n\t\t<tr><td>" +
            getIncluded(PluginPage.class, gVars, gConsts).apply_filters("media_meta", "", post) + "</td></tr>\n\t\t</thead>\n\t\t<tbody>\n";
        defaults = new Array<Object>(new ArrayEntry<Object>("input", "text"), new ArrayEntry<Object>("required", false), new ArrayEntry<Object>("value", ""),
                new ArrayEntry<Object>("extra_rows", new Array<Object>()));
        delete_href = getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_url("post.php?action=delete-post&amp;post=" + strval(attachment_id), "delete-post_" + strval(attachment_id));

        if (booleanval(send)) {
            send = "<input type=\'submit\' class=\'button\' name=\'send[" + strval(attachment_id) + "]\' value=\'" +
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert into Post", "default")) + "\' />";
        }

        if (booleanval(delete)) {
            delete = "<a href=\'" + strval(delete_href) + "\' id=\'del[" + strval(attachment_id) + "]\' disabled=\'disabled\' class=\'delete\'>" +
                getIncluded(L10nPage.class, gVars, gConsts).__("Delete", "default") + "</button>";
        }

        if ((booleanval(send) || booleanval(delete)) && !isset(form_fields.getValue("buttons"))) {
            form_fields.putValue(
                "buttons",
                new Array<Object>(new ArrayEntry<Object>("tr", "\t\t<tr class=\'submit\'><td></td><td class=\'savesend\'>" + strval(send) + " " + strval(delete) + "</td></tr>\n")));
        }

        hidden_fields = new Array<Object>();

        for (Map.Entry javaEntry156 : form_fields.entrySet()) {
            id = strval(javaEntry156.getKey());

            if (equal(Strings.getCharAt(id, 0), "_")) {
                continue;
            }

            field = (Array<Object>) javaEntry156.getValue();

            if (!empty(field.getValue("tr"))) {
                item = strval(item) + strval(field.getValue("tr"));

                continue;
            }

            field = Array.array_merge(defaults, field);
            name = "attachments[" + strval(attachment_id) + "][" + id + "]";

            if (equal(field.getValue("input"), "hidden")) {
                hidden_fields.putValue(name, field.getValue("value"));

                continue;
            }

            required = (booleanval(field.getValue("required"))
                ? "<abbr title=\"required\" class=\"required\">*</abbr>"
                : "");
            _class = id;
            _class = _class + (booleanval(field.getValue("required"))
                ? " form-required"
                : "");
            item = strval(item) + "\t\t<tr class=\'" + _class + "\'>\n\t\t\t<th valign=\'top\' scope=\'row\' class=\'label\'><label for=\'" + strval(name) + "\'><span class=\'alignleft\'>" +
                strval(field.getValue("label")) + "</span><span class=\'alignright\'>" + required + "</span><br class=\'clear\' /></label></th>\n\t\t\t<td class=\'field\'>";

            if (!empty(field.getValue(field.getValue("input")))) {
                item = strval(item) + strval(field.getValue(field.getValue("input")));
            } else if (equal(field.getValue("input"), "textarea")) {
                item = strval(item) + "<textarea type=\'text\' id=\'" + strval(name) + "\' name=\'" + strval(name) + "\'>" +
                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(field.getValue("value"))) + "</textarea>";
            } else {
                item = strval(item) + "<input type=\'text\' id=\'" + strval(name) + "\' name=\'" + strval(name) + "\' value=\'" +
                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(field.getValue("value"))) + "\' />";
            }

            if (!empty(field.getValue("helps"))) {
                item = strval(item) + "<p class=\'help\'>" + Strings.join("</p>\n<p class=\'help\'>", Array.array_unique(new Array(field.getValue("helps")))) + "</p>";
            }

            item = strval(item) + "</td>\n\t\t</tr>\n";
            extra_rows = new Array<Object>();

            if (!empty(field.getValue("errors"))) {
                for (Map.Entry javaEntry157 : Array.array_unique(new Array<Object>(field.getValue("errors"))).entrySet()) {
                    error = javaEntry157.getValue();
                    extra_rows.getArrayValue("error").putValue(error);
                }
            }

            if (!empty(field.getValue("extra_rows"))) {
                for (Map.Entry javaEntry158 : (Set<Map.Entry>) field.getArrayValue("extra_rows").entrySet()) {
                    _class = strval(javaEntry158.getKey());
                    rows = (Array<Object>) javaEntry158.getValue();

                    for (Map.Entry javaEntry159 : rows.entrySet()) {
                        html = javaEntry159.getValue();
                        extra_rows.getArrayValue(_class).putValue(html);
                    }
                }
            }

            for (Map.Entry javaEntry160 : extra_rows.entrySet()) {
                _class = strval(javaEntry160.getKey());
                rows = (Array<Object>) javaEntry160.getValue();

                for (Map.Entry javaEntry161 : rows.entrySet()) {
                    html = javaEntry161.getValue();
                    item = strval(item) + "\t\t<tr><td></td><td class=\'" + _class + "\'>" + strval(html) + "</td></tr>\n";
                }
            }
        }

        if (!empty(form_fields.getValue("_final"))) {
            item = strval(item) + "\t\t<tr class=\'final\'><td colspan=\'2\'>" + strval(form_fields.getValue("_final")) + "</td></tr>\n";
        }

        item = strval(item) + "\t</tbody>\n";
        item = strval(item) + "\t</table>\n";

        for (Map.Entry javaEntry162 : hidden_fields.entrySet()) {
            name = javaEntry162.getKey();
            value = strval(javaEntry162.getValue());
            item = strval(item) + "\t<input type=\'hidden\' name=\'" + strval(name) + "\' id=\'" + strval(name) + "\' value=\'" +
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(value) + "\' />\n";
        }

        return item;
    }

    public void media_upload_header() {
        echo(gVars.webEnv, "\t<script type=\"text/javascript\">post_id = ");
        echo(gVars.webEnv, intval(gVars.webEnv._REQUEST.getValue("post_id")));
        echo(gVars.webEnv, ";</script>\n\t<div id=\"media-upload-header\">\n\t");
        the_media_upload_tabs();
        echo(gVars.webEnv, "\t</div>\n\t");
    }

    public void media_upload_form(Array<Object> errors) {
        String flash_action_url = null;
        boolean flash = false;
        int post_id = 0;
        
        flash_action_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/async-upload.php";
        
        // If Mac and mod_security, no Flash. :(
        flash = true;

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(Strings.strtolower(gVars.webEnv.getHttpUserAgent()), "mac")) &&
                getIncluded(FunctionsPage.class, gVars, gConsts).apache_mod_loaded("mod_security", false)) {
            flash = false;
        }

        flash = booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("flash_uploader", flash));
        post_id = intval(gVars.webEnv._REQUEST.getValue("post_id"));
        echo(gVars.webEnv, "<input type=\'hidden\' name=\'post_id\' value=\'");
        echo(gVars.webEnv, post_id);
        echo(gVars.webEnv, "\' />\n<div id=\"media-upload-notice\">\n");

        if (isset(errors.getValue("upload_notice"))) {
            echo(gVars.webEnv, "\t");
            echo(gVars.webEnv, errors.getValue("upload_notice"));
        }

        echo(gVars.webEnv, "</div>\n<div id=\"media-upload-error\">\n");

        if (isset(errors.getValue("upload_error")) && getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(errors.getValue("upload_error"))) {
            echo(gVars.webEnv, "\t");
            echo(gVars.webEnv, ((WP_Error) errors.getValue("upload_error")).get_error_message());
        }

        echo(gVars.webEnv, "</div>\n");

        if (flash) {
            echo(gVars.webEnv, "<script type=\"text/javascript\">\n<!--\njQuery(function($){\n\tswfu = new SWFUpload({\n\t\t\tupload_url : \"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(flash_action_url));
            echo(gVars.webEnv, "\",\n\t\t\tflash_url : \"");
            echo(gVars.webEnv, getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-includes/js/swfupload/swfupload_f9.swf");
            echo(gVars.webEnv, "\",\n\t\t\tfile_post_name: \"async-upload\",\n\t\t\tfile_types: \"");
            echo(gVars.webEnv, getIncluded(PluginPage.class, gVars, gConsts).apply_filters("upload_file_glob", "*.*"));
            echo(gVars.webEnv, "\",\n\t\t\tpost_params : {\n\t\t\t\t\"post_id\" : \"");
            echo(gVars.webEnv, post_id);
            echo(gVars.webEnv, "\",\n\t\t\t\t\"auth_cookie\" : \"");
            echo(gVars.webEnv, gVars.webEnv._COOKIE.getValue(gConsts.getAUTH_COOKIE()));
            echo(gVars.webEnv, "\",\n\t\t\t\t\"type\" : \"");
            echo(gVars.webEnv, gVars.type);
            echo(gVars.webEnv, "\",\n\t\t\t\t\"tab\" : \"");
            echo(gVars.webEnv, gVars.tab);
            echo(gVars.webEnv, "\",\n\t\t\t\t\"short\" : \"1\"\n\t\t\t},\n\t\t\tfile_size_limit : \"");
            echo(gVars.webEnv, getIncluded(TemplatePage.class, gVars, gConsts).wp_max_upload_size());
            echo(
                    gVars.webEnv,
                    "b\",\n\t\t\tswfupload_element_id : \"flash-upload-ui\", // id of the element displayed when swfupload is available\n\t\t\tdegraded_element_id : \"html-upload-ui\",   // when swfupload is unavailable\n\t\t\tfile_dialog_start_handler : fileDialogStart,\n\t\t\tfile_queued_handler : fileQueued,\n\t\t\tupload_start_handler : uploadStart,\n\t\t\tupload_progress_handler : uploadProgress,\n\t\t\tupload_error_handler : uploadError,\n\t\t\tupload_success_handler : uploadSuccess,\n\t\t\tupload_complete_handler : uploadComplete,\n\t\t\tfile_queue_error_handler : fileQueueError,\n\t\t\tfile_dialog_complete_handler : fileDialogComplete,\n\n\t\t\tdebug: false\n\t\t});\n\t$(\"#flash-browse-button\").bind( \"click\", function(){swfu.selectFiles();});\n});\n//-->\n</script>\n\n\n<div id=\"flash-upload-ui\">\n\t<p><input id=\"flash-browse-button\" type=\"button\" value=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Choose files to upload", "default")));
            echo(gVars.webEnv, "\" class=\"button\" /></p>\n\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("After a file has been uploaded, you can add titles and descriptions.", "default");
            echo(gVars.webEnv, "</p>\n</div>\n\n");
        } else {
        } // $flash

        echo(
            gVars.webEnv,
            "\n<div id=\"html-upload-ui\">\n\t<p>\n\t<input type=\"file\" name=\"async-upload\" id=\"async-upload\" /> <input type=\"submit\" class=\"button\" name=\"html-upload\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Upload", "default")));
        echo(gVars.webEnv, "\" /> <a href=\"#\" onClick=\"return top.tb_remove();\">");
        getIncluded(L10nPage.class, gVars, gConsts)._e("Cancel", "default");
        echo(gVars.webEnv, "</a>\n\t</p>\n\t<input type=\"hidden\" name=\"post_id\" id=\"post_id\" value=\"");
        echo(gVars.webEnv, post_id);
        echo(gVars.webEnv, "\" />\n\t<br class=\"clear\" />\n\t");

        if (getIncluded(FunctionsPage.class, gVars, gConsts).is_lighttpd_before_150()) {
            echo(gVars.webEnv, "\t<p>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("If you want to use all capabilities of the uploader, like uploading multiple files at once, please upgrade to lighttpd 1.5.", "default");
            echo(gVars.webEnv, "</p>\n\t");
        } else {
        }

        echo(gVars.webEnv, "</div>\n");
    }

    public void media_upload_type_form() {
        media_upload_type_form("file", new Array(), null);
    }

    public void media_upload_type_form(String type, Array<Object> errors, Object id) {
        int post_id = 0;
        String form_action_url = null;
        String callback = null;
        media_upload_header();
        post_id = intval(gVars.webEnv._REQUEST.getValue("post_id"));
        form_action_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/media-upload.php?type=" + type + "&tab=type&post_id=" + strval(post_id);
        callback = "type_form_" + type;
        echo(gVars.webEnv, "\n<form enctype=\"multipart/form-data\" method=\"post\" action=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(form_action_url));
        echo(gVars.webEnv, "\" class=\"media-upload-form type-form validate\" id=\"");
        echo(gVars.webEnv, type);
        echo(gVars.webEnv, "-form\">\n<input type=\"hidden\" name=\"post_id\" id=\"post_id\" value=\"");
        echo(gVars.webEnv, post_id);
        echo(gVars.webEnv, "\" />\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("media-form", "_wpnonce", true, true);
        echo(gVars.webEnv, "<h3>");
        getIncluded(L10nPage.class, gVars, gConsts)._e("From Computer", "default");
        echo(gVars.webEnv, "</h3>\n");
        media_upload_form(errors);
        echo(
                gVars.webEnv,
                "\n<script type=\"text/javascript\">\n<!--\njQuery(function($){\n\tvar preloaded = $(\".media-item.preloaded\");\n\tif ( preloaded.length > 0 ) {\n\t\tpreloaded.each(function(){prepareMediaItem({id:this.id.replace(/[^0-9]/g, \'\')},\'\');});\n\t}\n\tupdateMediaForm();\n});\n-->\n</script>\n");

        if (booleanval(id) && !getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
            echo(gVars.webEnv, "<div id=\"media-items\">\n");
            echo(gVars.webEnv, get_media_items(intval(id), errors));
            echo(gVars.webEnv, "</div>\n<input type=\"submit\" class=\"button savebutton\" name=\"save\" value=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save all changes", "default")));
            echo(gVars.webEnv, "\" />\n\n");
        } else if (VarHandling.is_callable(new Callback(callback, this))) {
            echo(gVars.webEnv, "\n<div class=\"media-blank\">\n<p style=\"text-align:center\">");
            getIncluded(L10nPage.class, gVars, gConsts)._e("&mdash; OR &mdash;", "default");
            echo(gVars.webEnv, "</p>\n<h3>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("From URL", "default");
            echo(gVars.webEnv, "</h3>\n</div>\n\n<div id=\"media-items\">\n<div class=\"media-item media-blank\">\n");
            echo(gVars.webEnv, FunctionHandling.call_user_func(new Callback(callback, this)));
            echo(gVars.webEnv, "</div>\n</div>\n<input type=\"submit\" class=\"button savebutton\" name=\"save\" value=\"");
            echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save all changes", "default")));
            echo(gVars.webEnv, "\" />\n");
        } else {
        }
    }

    public void media_upload_gallery_form() {
        media_upload_gallery_form(null);
    }

    public void media_upload_gallery_form(Array<Object> errors) {
        int post_id = 0;
        String form_action_url = null;
        media_upload_header();
        post_id = intval(gVars.webEnv._REQUEST.getValue("post_id"));
        form_action_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/media-upload.php?type=" + gVars.type + "&tab=gallery&post_id=" + strval(post_id);
        echo(
                gVars.webEnv,
                "\n<script type=\"text/javascript\">\n<!--\njQuery(function($){\n\tvar preloaded = $(\".media-item.preloaded\");\n\tif ( preloaded.length > 0 ) {\n\t\tpreloaded.each(function(){prepareMediaItem({id:this.id.replace(/[^0-9]/g, \'\')},\'\');});\n\t\tupdateMediaForm();\n\t}\n});\n-->\n</script>\n\n<form enctype=\"multipart/form-data\" method=\"post\" action=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(form_action_url));
        echo(gVars.webEnv, "\" class=\"media-upload-form validate\" id=\"gallery-form\">\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("media-form");
        //media_upload_form( $errors );
        echo(gVars.webEnv, "\n<div id=\"media-items\">\n");
        echo(gVars.webEnv, get_media_items(post_id, errors));
        echo(gVars.webEnv, "</div>\n<input type=\"submit\" class=\"button savebutton\" name=\"save\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save all changes", "default")));
        echo(gVars.webEnv, "\" />\n<input type=\"submit\" class=\"button insert-gallery\" name=\"insert-gallery\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert gallery into post", "default")));
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"post_id\" id=\"post_id\" value=\"");
        echo(gVars.webEnv, post_id);
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"type\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.type));
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"tab\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.tab));
        echo(gVars.webEnv, "\" />\n</form>\n");
    }

    public Object media_upload_library_form() {
        return media_upload_library_form(new Array());
    }

    public Object media_upload_library_form(Array<Object> errors) {
        int post_id = 0;
        String form_action_url = null;
        int start = 0;
        Array<String> type_links = new Array<String>();
        Array<Object> _num_posts = new Array<Object>();
        Array<Object> matches = new Array<Object>();
        Array<Object> num_posts = new Array<Object>();
        Object _type = null;
        Object real = null;
        Array<Object> reals = null;
        String _class = null;
        Object mime_type = null;
        Array<Object> label = new Array<Object>();
        String page_links = null;
        String arc_query = null;
        Array<Object> arc_result = new Array<Object>();
        int month_count = 0;
        StdClass arc_row = null;
        String _default = null;
        media_upload_header();
        post_id = intval(gVars.webEnv._REQUEST.getValue("post_id"));
        form_action_url = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + "/wp-admin/media-upload.php?type=" + gVars.type + "&tab=library&post_id=" + strval(post_id);
        gVars.webEnv._GET.putValue("paged", intval(gVars.webEnv._GET.getValue("paged")));

        if (intval(gVars.webEnv._GET.getValue("paged")) < 1) {
            gVars.webEnv._GET.putValue("paged", 1);
        }

        start = (intval(gVars.webEnv._GET.getValue("paged")) - 1) * 10;

        if (start < 1) {
            start = 0;
        }

        // Added by Numiton
        limit_filter_start = start;

        getIncluded(PluginPage.class, gVars, gConsts).add_filter("post_limits", Callback.createCallbackArray(this, "createFunction_limit_filter"), 10, 1);
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    gVars.post_mime_types = srcArray.getArrayValue(0);
                    media_upload_library_form_avail_post_mime_types = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign((((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).wp_edit_attachments_query(null));
        echo(gVars.webEnv, "\n<form id=\"filter\" action=\"\" method=\"get\">\n<input type=\"hidden\" name=\"type\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.type));
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"tab\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(gVars.tab));
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"post_id\" value=\"");
        echo(gVars.webEnv, post_id);
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"post_mime_type\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(gVars.webEnv._GET.getValue("post_mime_type"))));
        echo(gVars.webEnv, "\" />\n\n<div id=\"search-filter\">\n\t<input type=\"text\" id=\"post-search-input\" name=\"s\" value=\"");
        getIncluded(General_templatePage.class, gVars, gConsts).the_search_query();
        echo(gVars.webEnv, "\" />\n\t<input type=\"submit\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Search Media", "default")));
        echo(gVars.webEnv, "\" class=\"button\" />\n</div>\n\n<p>\n<ul class=\"subsubsub\">\n");
        type_links = new Array<String>();
        _num_posts = new Array<Object>(getIncluded(PostPage.class, gVars, gConsts).wp_count_attachments(""));
        matches = getIncluded(PostPage.class, gVars, gConsts).wp_match_mime_types(Array.array_keys(gVars.post_mime_types), Array.array_keys(_num_posts));

        for (Map.Entry javaEntry163 : matches.entrySet()) {
            _type = javaEntry163.getKey();
            reals = (Array<Object>) javaEntry163.getValue();

            for (Map.Entry javaEntry164 : reals.entrySet()) {
                real = javaEntry164.getValue();
                num_posts.putValue(_type, intval(num_posts.getValue(_type)) + intval(_num_posts.getValue(real)));
            }
        }

        // If available type specified by media button clicked, filter by that type
        if (empty(gVars.webEnv._GET.getValue("post_mime_type")) && !empty(num_posts.getValue(gVars.type))) {
            gVars.webEnv._GET.putValue("post_mime_type", gVars.type);
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        gVars.post_mime_types = srcArray.getArrayValue(0);
                        media_upload_library_form_avail_post_mime_types = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign((((org.numiton.nwp.wp_admin.includes.PostPage) getIncluded(org.numiton.nwp.wp_admin.includes.PostPage.class, gVars, gConsts))).wp_edit_attachments_query(null));
        }

        if (empty(gVars.webEnv._GET.getValue("post_mime_type")) || equal(gVars.webEnv._GET.getValue("post_mime_type"), "all")) {
            _class = " class=\"current\"";
        }

        type_links.putValue(
            "<li><a href=\'" +
            getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(
                new Array<Object>(new ArrayEntry<Object>("post_mime_type", "all"), new ArrayEntry<Object>("paged", false), new ArrayEntry<Object>("m", false))) + "\'" + _class + ">" +
            getIncluded(L10nPage.class, gVars, gConsts).__("All Types", "default") + "</a>");

        for (Map.Entry javaEntry165 : gVars.post_mime_types.entrySet()) {
            mime_type = javaEntry165.getKey();
            label = (Array<Object>) javaEntry165.getValue();
            _class = "";

            if (!booleanval(getIncluded(PostPage.class, gVars, gConsts).wp_match_mime_types(mime_type, media_upload_library_form_avail_post_mime_types))) {
                continue;
            }

            if (booleanval(getIncluded(PostPage.class, gVars, gConsts).wp_match_mime_types(mime_type, gVars.webEnv._GET.getValue("post_mime_type")))) {
                _class = " class=\"current\"";
            }

            type_links.putValue(
                "<li><a href=\'" +
                getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(new Array<Object>(new ArrayEntry<Object>("post_mime_type", mime_type), new ArrayEntry<Object>("paged", false))) + "\'" +
                _class + ">" +
                QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__ngettext(
                        strval(label.getArrayValue(2).getValue(0)),
                        strval(label.getArrayValue(2).getValue(1)),
                        intval(num_posts.getValue(mime_type)),
                        "default"),
                    "<span id=\'" + strval(mime_type) + "-counter\'>" + getIncluded(FunctionsPage.class, gVars, gConsts).number_format_i18n(intval(num_posts.getValue(mime_type)), null) + "</span>") +
                "</a>");
        }

        echo(gVars.webEnv, Strings.implode(" | </li>", type_links) + "</li>");
        type_links = null;
        echo(gVars.webEnv, "</ul>\n</p>\n\n<div class=\"tablenav\">\n\n");
        page_links = strval(
                getIncluded(General_templatePage.class, gVars, gConsts).paginate_links(
                    new Array<Object>(
                        new ArrayEntry<Object>("base", getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("paged", "%#%")),
                        new ArrayEntry<Object>("format", ""),
                        new ArrayEntry<Object>("total", Math.ceil(gVars.wp_query.found_posts / floatval(10))),
                        new ArrayEntry<Object>("current", gVars.webEnv._GET.getValue("paged")))));

        if (booleanval(page_links)) {
            echo(gVars.webEnv, "<div class=\'tablenav-pages\'>" + page_links + "</div>");
        }

        echo(gVars.webEnv, "\n<div class=\"alignleft\">\n");
        arc_query = "SELECT DISTINCT YEAR(post_date) AS yyear, MONTH(post_date) AS mmonth FROM " + gVars.wpdb.posts + " WHERE post_type = \'attachment\' ORDER BY post_date DESC";
        arc_result = gVars.wpdb.get_results(arc_query);
        month_count = Array.count(arc_result);

        if (booleanval(month_count) && !(equal(1, month_count) && equal(0, ((StdClass) arc_result.getValue(0)).fields.getValue("mmonth")))) {
            echo(gVars.webEnv, "<select name=\'m\'>\n<option");
            getIncluded(TemplatePage.class, gVars, gConsts).selected(strval(gVars.webEnv._GET.getValue("m")), strval(0));
            echo(gVars.webEnv, " value=\'0\'>");
            getIncluded(L10nPage.class, gVars, gConsts)._e("Show all dates", "default");
            echo(gVars.webEnv, "</option>\n");

            for (Map.Entry javaEntry166 : arc_result.entrySet()) {
                arc_row = (StdClass) javaEntry166.getValue();

                if (equal(StdClass.getValue(arc_row, "yyear"), 0)) {
                    continue;
                }

                arc_row.fields.putValue("mmonth", getIncluded(FormattingPage.class, gVars, gConsts).zeroise(strval(StdClass.getValue(arc_row, "mmonth")), 2));

                if (equal(intval(StdClass.getValue(arc_row, "yyear")) + intval(StdClass.getValue(arc_row, "mmonth")), gVars.webEnv._GET.getValue("m"))) {
                    _default = " selected=\"selected\"";
                } else {
                    _default = "";
                }

                echo(
                    gVars.webEnv,
                    "<option" + _default + " value=\'" +
                    getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(StdClass.getValue(arc_row, "yyear")) + strval(StdClass.getValue(arc_row, "mmonth"))) + "\'>");
                echo(
                    gVars.webEnv,
                    getIncluded(FormattingPage.class, gVars, gConsts)
                        .wp_specialchars(gVars.wp_locale.get_month(strval(StdClass.getValue(arc_row, "mmonth"))) + " " + StdClass.getValue(arc_row, "yyear"), strval(0)));
                echo(gVars.webEnv, "</option>\n");
            }

            echo(gVars.webEnv, "</select>\n");
        }

        echo(gVars.webEnv, "\n<input type=\"submit\" id=\"post-query-submit\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Filter &#187;", "default")));
        echo(gVars.webEnv, "\" class=\"button-secondary\" />\n\n</div>\n\n<br class=\"clear\" />\n</div>\n</form>\n\n<form enctype=\"multipart/form-data\" method=\"post\" action=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(form_action_url));
        echo(gVars.webEnv, "\" class=\"media-upload-form validate\" id=\"library-form\">\n\n");
        getIncluded(FunctionsPage.class, gVars, gConsts).wp_nonce_field("media-form", "_wpnonce", true, true);
        //media_upload_form( $errors ); 
        echo(
                gVars.webEnv,
                "\n<script type=\"text/javascript\">\n<!--\njQuery(function($){\n\tvar preloaded = $(\".media-item.preloaded\");\n\tif ( preloaded.length > 0 ) {\n\t\tpreloaded.each(function(){prepareMediaItem({id:this.id.replace(/[^0-9]/g, \'\')},\'\');});\n\t\tupdateMediaForm();\n\t}\n});\n-->\n</script>\n\n<div id=\"media-items\">\n");
        echo(gVars.webEnv, get_media_items(intval(null), errors));
        echo(gVars.webEnv, "</div>\n<input type=\"submit\" class=\"button savebutton\" name=\"save\" value=\"");
        echo(gVars.webEnv, getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Save all changes", "default")));
        echo(gVars.webEnv, "\" />\n<input type=\"hidden\" name=\"post_id\" id=\"post_id\" value=\"");
        echo(gVars.webEnv, post_id);
        echo(gVars.webEnv, "\" />\n</form>\n");

        return "";
    }

    public String type_form_image() {
        return "\n\t<table class=\"describe\"><tbody>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[src]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Image URL", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[src]\" name=\"insertonly[src]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[alt]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Description", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[alt]\" name=\"insertonly[alt]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr><td></td><td class=\"help\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Alternate text, e.g. \"The Mona Lisa\"", "default") +
        "</td></tr>\n\t\t<tr class=\"align\">\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\"><label for=\"insertonly[align]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Alignment", "default") +
        "</label></th>\n\t\t\t<td class=\"field\">\n\t\t\t\t<input name=\"insertonly[align]\" id=\"image-align-none-0\" value=\"none\" type=\"radio\" checked=\"checked\" />\n\t\t\t\t<label for=\"image-align-none-0\" class=\"align image-align-none-label\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("None", "default") +
        "</label>\n\t\t\t\t<input name=\"insertonly[align]\" id=\"image-align-left-0\" value=\"left\" type=\"radio\" />\n\t\t\t\t<label for=\"image-align-left-0\" class=\"align image-align-left-label\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Left", "default") +
        "</label>\n\t\t\t\t<input name=\"insertonly[align]\" id=\"image-align-center-0\" value=\"center\" type=\"radio\" />\n\t\t\t\t<label for=\"image-align-center-0\" class=\"align image-align-center-label\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Center", "default") +
        "</label>\n\t\t\t\t<input name=\"insertonly[align]\" id=\"image-align-right-0\" value=\"right\" type=\"radio\" />\n\t\t\t\t<label for=\"image-align-right-0\" class=\"align image-align-right-label\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Right", "default") +
        "</label>\n\t\t\t</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td></td>\n\t\t\t<td>\n\t\t\t\t<input type=\"submit\" class=\"button\" name=\"insertonlybutton\" value=\"" +
        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert into Post", "default")) +
        "\" />\n\t\t\t</td>\n\t\t</tr>\n\t</tbody></table>\n";
    }

    public String type_form_audio() {
        return "\n\t<table class=\"describe\"><tbody>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[href]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Audio File URL", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[href]\" name=\"insertonly[href]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[title]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[title]\" name=\"insertonly[title]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr><td></td><td class=\"help\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Link text, e.g. \"Still Alive by Jonathan Coulton\"", "default") +
        "</td></tr>\n\t\t<tr>\n\t\t\t<td></td>\n\t\t\t<td>\n\t\t\t\t<input type=\"submit\" class=\"button\" name=\"insertonlybutton\" value=\"" +
        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert into Post", "default")) +
        "\" />\n\t\t\t</td>\n\t\t</tr>\n\t</tbody></table>\n";
    }

    public String type_form_video() {
        return "\n\t<table class=\"describe\"><tbody>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[href]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Video URL", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[href]\" name=\"insertonly[href]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[title]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[title]\" name=\"insertonly[title]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr><td></td><td class=\"help\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Link text, e.g. \"Lucy on YouTube\"", "default") +
        "</td></tr>\n\t\t<tr>\n\t\t\t<td></td>\n\t\t\t<td>\n\t\t\t\t<input type=\"submit\" class=\"button\" name=\"insertonlybutton\" value=\"" +
        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert into Post", "default")) +
        "\" />\n\t\t\t</td>\n\t\t</tr>\n\t</tbody></table>\n";
    }

    public String type_form_file() {
        return "\n\t<table class=\"describe\"><tbody>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[href]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("URL", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[href]\" name=\"insertonly[href]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<th valign=\"top\" scope=\"row\" class=\"label\">\n\t\t\t\t<span class=\"alignleft\"><label for=\"insertonly[title]\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Title", "default") +
        "</label></span>\n\t\t\t\t<span class=\"alignright\"><abbr title=\"required\" class=\"required\">*</abbr></span>\n\t\t\t</th>\n\t\t\t<td class=\"field\"><input id=\"insertonly[title]\" name=\"insertonly[title]\" value=\"\" type=\"text\"></td>\n\t\t</tr>\n\t\t<tr><td></td><td class=\"help\">" +
        getIncluded(L10nPage.class, gVars, gConsts).__("Link text, e.g. \"Ransom Demands (PDF)\"", "default") +
        "</td></tr>\n\t\t<tr>\n\t\t\t<td></td>\n\t\t\t<td>\n\t\t\t\t<input type=\"submit\" class=\"button\" name=\"insertonlybutton\" value=\"" +
        getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(getIncluded(L10nPage.class, gVars, gConsts).__("Insert into Post", "default")) +
        "\" />\n\t\t\t</td>\n\t\t</tr>\n\t</tbody></table>\n";
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_admin_includes_media_block1");
        gVars.webEnv = webEnv;
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("media_upload_tabs", Callback.createCallbackArray(this, "update_gallery_tab"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("media_buttons", Callback.createCallbackArray(this, "media_buttons"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_print_scripts", Callback.createCallbackArray(this, "media_buttons_head"), 10, 1);

        // Commented by Numiton: function not found

        //getIncluded(PluginPage.class, gVars, gConsts).add_action("media_upload_media", "media_upload_handler", 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("attachment_fields_to_edit", Callback.createCallbackArray(this, "image_attachment_fields_to_edit"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("attachment_fields_to_save", Callback.createCallbackArray(this, "image_attachment_fields_to_save"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("media_send_to_editor", Callback.createCallbackArray(this, "image_media_send_to_editor"), 10, 3);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("async_upload_image", Callback.createCallbackArray(this, "get_media_item"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("async_upload_audio", Callback.createCallbackArray(this, "get_media_item"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("async_upload_video", Callback.createCallbackArray(this, "get_media_item"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("async_upload_file", Callback.createCallbackArray(this, "get_media_item"), 10, 2);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("media_upload_image", Callback.createCallbackArray(this, "media_upload_image"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("media_upload_audio", Callback.createCallbackArray(this, "media_upload_audio"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("media_upload_video", Callback.createCallbackArray(this, "media_upload_video"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("media_upload_file", Callback.createCallbackArray(this, "media_upload_file"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head_media_upload_type_form", Callback.createCallbackArray(this, "media_admin_css"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("media_upload_gallery", Callback.createCallbackArray(this, "media_upload_gallery"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head_media_upload_gallery_form", Callback.createCallbackArray(this, "media_admin_css"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_filter("media_upload_library", Callback.createCallbackArray(this, "media_upload_library"), 10, 1);
        getIncluded(PluginPage.class, gVars, gConsts).add_action("admin_head_media_upload_library_form", Callback.createCallbackArray(this, "media_admin_css"), 10, 1);

        return DEFAULT_VAL;
    }

    // Created by Numiton
    public String createFunction_limit_filter(Object a) {
        return "LIMIT " + strval(limit_filter_start) + ", 10";
    }
}
