/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: MediaPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_admin.includes.ImagePage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.*;
import com.numiton.image.Image;
import com.numiton.java.JFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QFileSystemOrSocket;
import com.numiton.ntile.til.libraries.php.quercus.QImage;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class MediaPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(MediaPage.class.getName());

    /**
     * Generated in place of local variable 'max_width' from method
     * 'image_constrain_size_for_editor' because it is used inside an inner
     * class.
     */
    int image_constrain_size_for_editor_max_width = 0;

    /**
     * Generated in place of local variable 'max_height' from method
     * 'image_constrain_size_for_editor' because it is used inside an inner
     * class.
     */
    int image_constrain_size_for_editor_max_height = 0;

    /**
     * Generated in place of local variable 'width' from method 'image_downsize'
     * because it is used inside an inner class.
     */
    int image_downsize_width = 0;

    /**
     * Generated in place of local variable 'height' from method
     * 'image_downsize' because it is used inside an inner class.
     */
    int image_downsize_height = 0;

    /**
     * Generated in place of local variable 'img_src' from method
     * 'get_image_tag' because it is used inside an inner class.
     */
    String get_image_tag_img_src = null;

    /**
     * Generated in place of local variable 'width' from method 'get_image_tag'
     * because it is used inside an inner class.
     */
    Object get_image_tag_width = null;

    /**
     * Generated in place of local variable 'height' from method 'get_image_tag'
     * because it is used inside an inner class.
     */
    Object get_image_tag_height = null;

    /**
     * Generated in place of local variable 'new_w' from method
     * 'image_resize_dimensions' because it is used inside an inner class.
     */
    float image_resize_dimensions_new_w = 0;

    /**
     * Generated in place of local variable 'new_h' from method
     * 'image_resize_dimensions' because it is used inside an inner class.
     */
    float image_resize_dimensions_new_h = 0;

    /**
     * Generated in place of local variable 'orig_w' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_orig_w;

    /**
     * Generated in place of local variable 'orig_h' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_orig_h;

    /**
     * Generated in place of local variable 'orig_type' from method
     * 'image_resize' because it is used inside an inner class.
     */
    Object image_resize_orig_type = null;

    /**
     * Generated in place of local variable 'dst_x' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_dst_x;

    /**
     * Generated in place of local variable 'dst_y' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_dst_y;

    /**
     * Generated in place of local variable 'src_x' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_src_x;

    /**
     * Generated in place of local variable 'src_y' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_src_y;

    /**
     * Generated in place of local variable 'dst_w' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_dst_w;

    /**
     * Generated in place of local variable 'dst_h' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_dst_h;

    /**
     * Generated in place of local variable 'src_w' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_src_w;

    /**
     * Generated in place of local variable 'src_h' from method 'image_resize'
     * because it is used inside an inner class.
     */
    int image_resize_src_h;

    /**
     * Generated in place of local variable 'width' from method
     * 'image_get_intermediate_size' because it is used inside an inner class.
     */
    String image_get_intermediate_size_width = null;

    /**
     * Generated in place of local variable 'height' from method
     * 'image_get_intermediate_size' because it is used inside an inner class.
     */
    Object image_get_intermediate_size_height = null;

    /**
     * Generated in place of local variable 'width' from method
     * 'wp_get_attachment_image_src' because it is used inside an inner class.
     */
    Object wp_get_attachment_image_src_width = null;

    /**
     * Generated in place of local variable 'height' from method
     * 'wp_get_attachment_image_src' because it is used inside an inner class.
     */
    Object wp_get_attachment_image_src_height = null;

    /**
     * Generated in place of local variable 'src' from method
     * 'wp_get_attachment_image' because it is used inside an inner class.
     */
    String wp_get_attachment_image_src = null;

    /**
     * Generated in place of local variable 'width' from method
     * 'wp_get_attachment_image' because it is used inside an inner class.
     */
    int wp_get_attachment_image_width;

    /**
     * Generated in place of local variable 'height' from method
     * 'wp_get_attachment_image' because it is used inside an inner class.
     */
    int wp_get_attachment_image_height;
    public Object content_width;

    @Override
    @RequestMapping("/wp-includes/media.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/media";
    }

 // functions for media display

 // scale down the default size of an image so it's a better fit for the editor and theme
    public Array<Object> image_constrain_size_for_editor(int width, int height, Object size)/* Do not change type */
     {
        if (is_array(size)) {
            image_constrain_size_for_editor_max_width = intval(((Array) size).getValue(0));
            image_constrain_size_for_editor_max_height = intval(((Array) size).getValue(1));
        } else if (equal(size, "thumb") || equal(size, "thumbnail")) {
            image_constrain_size_for_editor_max_width = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("thumbnail_size_w"));
            image_constrain_size_for_editor_max_height = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("thumbnail_size_h"));
    		// last chance thumbnail size defaults
            if (!booleanval(image_constrain_size_for_editor_max_width) && !booleanval(image_constrain_size_for_editor_max_height)) {
                image_constrain_size_for_editor_max_width = 128;
                image_constrain_size_for_editor_max_height = 96;
            }
        } else if (equal(size, "medium")) {
            image_constrain_size_for_editor_max_width = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("medium_size_w"));
            image_constrain_size_for_editor_max_height = intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("medium_size_h"));
    		// if no width is set, default to the theme content width if available
        } else { // $size == 'full'
    		// we're inserting a full size image into the editor.  if it's a really big image we'll scale it down to fit reasonably
    		// within the editor itself, and within the theme's content width if it's known.  the user can resize it in the editor
    		// if they wish.
            if (!empty(content_width)) {
                image_constrain_size_for_editor_max_width = intval(content_width);
            } else {
                image_constrain_size_for_editor_max_width = 500;
            }
        }

        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    image_constrain_size_for_editor_max_width = intval(srcArray.getValue(0));
                    image_constrain_size_for_editor_max_height = intval(srcArray.getValue(1));

                    return srcArray;
                }
            }.doAssign(
            (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                "editor_max_image_size",
                new Array<Object>(new ArrayEntry<Object>(image_constrain_size_for_editor_max_width), new ArrayEntry<Object>(image_constrain_size_for_editor_max_height)),
                size));

        return wp_constrain_dimensions(width, height, image_constrain_size_for_editor_max_width, image_constrain_size_for_editor_max_height);
    }

 // return a width/height string for use in an <img /> tag.  Empty values will be omitted.
    public String image_hwstring(Object width, Object height) {
        String out = null;
        out = "";

        if (booleanval(width)) {
            out = out + "width=\"" + strval(width) + "\" ";
        }

        if (booleanval(height)) {
            out = out + "height=\"" + strval(height) + "\" ";
        }

        return out;
    }

 // Scale an image to fit a particular size (such as 'thumb' or 'medium'), and return an image URL, height and width.
 // The URL might be the original image, or it might be a resized version.  This function won't create a new resized copy, it will just return an already resized one if it exists.
 // returns an array($url, $width, $height)
    public Array<Object> image_downsize(int id, Object size) {
        String img_url;
        Array<Object> meta = new Array<Object>();
        Array<Object> out = null;
        Array<Object> intermediate = new Array<Object>();
        String thumb_file = null;
        Array<Object> info = new Array<Object>();

        if (!getIncluded(PostPage.class, gVars, gConsts).wp_attachment_is_image(id)) {
            return new Array<Object>();
        }

        img_url = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(id);
        meta = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_metadata(id, false);
        image_downsize_width = image_downsize_height = 0;

    	// plugins can use this to provide resize services
        if (booleanval(out = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("image_downsize", new Array(), id, size))) {
            return out;
        }

    	// try for a new style intermediate size
        if (booleanval(intermediate = image_get_intermediate_size(id, size))) {
            img_url = Strings.str_replace(FileSystemOrSocket.basename(img_url), strval(intermediate.getValue("file")), img_url);
            image_downsize_width = intval(intermediate.getValue("width"));
            image_downsize_height = intval(intermediate.getValue("height"));
        } else if (equal(size, "thumbnail")) {
    		// fall back to the old thumbnail
            if (booleanval(thumb_file = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_thumb_file(0)) && booleanval(info = QImage.getimagesize(gVars.webEnv, thumb_file))) {
                img_url = Strings.str_replace(FileSystemOrSocket.basename(img_url), FileSystemOrSocket.basename(thumb_file), img_url);
                image_downsize_width = intval(info.getValue(0));
                image_downsize_height = intval(info.getValue(1));
            }
        }

        if (!booleanval(image_downsize_width) && !booleanval(image_downsize_height) && isset(meta.getValue("width")) && isset(meta.getValue("height"))) {
    		// any other type: use the real image and constrain it
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        image_downsize_width = intval(srcArray.getValue(0));
                        image_downsize_height = intval(srcArray.getValue(1));

                        return srcArray;
                    }
                }.doAssign(image_constrain_size_for_editor(intval(meta.getValue("width")), intval(meta.getValue("height")), size));
        }

        if (booleanval(img_url)) {
            return new Array<Object>(new ArrayEntry<Object>(img_url), new ArrayEntry<Object>(image_downsize_width), new ArrayEntry<Object>(image_downsize_height));
        }

        return new Array<Object>();
    }

 // return an <img src /> tag for the given image attachment, scaling it down if requested
    public String get_image_tag(int id, String alt, String title, String align, String size) {
        String hwstring = null;
        String html = null;
        String url = null;
        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    get_image_tag_img_src = strval(srcArray.getValue(0));
                    get_image_tag_width = srcArray.getValue(1);
                    get_image_tag_height = srcArray.getValue(2);

                    return srcArray;
                }
            }.doAssign(image_downsize(id, size));
        hwstring = image_hwstring(get_image_tag_width, get_image_tag_height);
        html = "<img src=\"" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(get_image_tag_img_src) + "\" alt=\"" +
            getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(alt) + "\" title=\"" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(title) + "\" " + hwstring +
            "class=\"align" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(align) + " size-" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(size) +
            " wp-image-" + strval(id) + "\" />";
        url = "";
        html = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("image_send_to_editor", html, id, alt, title, align, url, size));

        return html;
    }

 // same as wp_shrink_dimensions, except the max parameters are optional.
 // if either width or height are empty, no constraint is applied on that dimension.
    public Array<Object> wp_constrain_dimensions(int current_width, int current_height, int max_width, int max_height) {
        float width_ratio = 0;
        float height_ratio = 0;
        float ratio = 0;

        if (!booleanval(max_width) && !booleanval(max_height)) {
            return new Array<Object>(new ArrayEntry<Object>(current_width), new ArrayEntry<Object>(current_height));
        }

        width_ratio = height_ratio = 1.0f;

        if ((max_width > 0) && (current_width > max_width)) {
            width_ratio = floatval(max_width) / floatval(current_width);
        }

        if ((max_height > 0) && (current_height > max_height)) {
            height_ratio = floatval(max_height) / floatval(current_height);
        }

    	// the smaller ratio is the one we need to fit it to the constraining box
        ratio = floatval(Math.min(width_ratio, height_ratio));

        return new Array<Object>(new ArrayEntry<Object>(current_width * ratio), new ArrayEntry<Object>(current_height * ratio));
    }

 // calculate dimensions and coordinates for a resized image that fits within a specified width and height
 // if $crop is true, the largest matching central portion of the image will be cropped out and resized to the required size
    public Array<Object> image_resize_dimensions(int orig_w, int orig_h, int dest_w, int dest_h, boolean crop) {
        float aspect_ratio = 0;
        float size_ratio = 0;
        float crop_w = 0;
        float crop_h = 0;
        float s_x = 0;
        float s_y = 0;

        if ((orig_w <= 0) || (orig_h <= 0)) {
            return new Array<Object>();
        }
    	// at least one of dest_w or dest_h must be specific
        if ((dest_w <= 0) && (dest_h <= 0)) {
            return new Array<Object>();
        }

        if (crop) {
    		// crop the largest possible portion of the original image that we can size to $dest_w x $dest_h
            aspect_ratio = floatval(orig_w) / floatval(orig_h);
            image_resize_dimensions_new_w = floatval(Math.min(dest_w, orig_w));
            image_resize_dimensions_new_h = floatval(Math.min(dest_h, orig_h));

            if (!booleanval(image_resize_dimensions_new_w)) {
                image_resize_dimensions_new_w = image_resize_dimensions_new_h * aspect_ratio;
            }

            if (!booleanval(image_resize_dimensions_new_h)) {
                image_resize_dimensions_new_h = image_resize_dimensions_new_w / aspect_ratio;
            }

            size_ratio = floatval(Math.max(image_resize_dimensions_new_w / orig_w, image_resize_dimensions_new_h / orig_h));
            crop_w = Math.ceil(image_resize_dimensions_new_w / size_ratio);
            crop_h = Math.ceil(image_resize_dimensions_new_h / size_ratio);
            s_x = Math.floor((orig_w - crop_w) / floatval(2));
            s_y = Math.floor((orig_h - crop_h) / floatval(2));
        } else {
    		// don't crop, just resize using $dest_w x $dest_h as a maximum bounding box
            crop_w = floatval(orig_w);
            crop_h = floatval(orig_h);
            s_x = floatval(0);
            s_y = floatval(0);
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        image_resize_dimensions_new_w = intval(srcArray.getValue(0));
                        image_resize_dimensions_new_h = intval(srcArray.getValue(1));

                        return srcArray;
                    }
                }.doAssign(wp_constrain_dimensions(orig_w, orig_h, dest_w, dest_h));
        }

    	// if the resulting image would be the same size or larger we don't want to resize it
        if ((image_resize_dimensions_new_w >= orig_w) && (image_resize_dimensions_new_h >= orig_h)) {
            return new Array<Object>();
        }

    	// the return array matches the parameters to imagecopyresampled()
    	// int dst_x, int dst_y, int src_x, int src_y, int dst_w, int dst_h, int src_w, int src_h
        return new Array<Object>(
            new ArrayEntry<Object>(0),
            new ArrayEntry<Object>(0),
            new ArrayEntry<Object>(s_x),
            new ArrayEntry<Object>(s_y),
            new ArrayEntry<Object>(image_resize_dimensions_new_w),
            new ArrayEntry<Object>(image_resize_dimensions_new_h),
            new ArrayEntry<Object>(crop_w),
            new ArrayEntry<Object>(crop_h));
    }

 // Scale down an image to fit a particular size and save a new copy of the image
    public Object image_resize(String file, int max_w, int max_h, boolean crop, String suffix, String dest_path, int jpeg_quality) {
        Object image = null;
        Array<Object> dims = new Array<Object>();
        int newimage = 0;
        Array<Object> info = new Array<Object>();
        String dir = null;
        Object ext = null;
        String name = null;
        String _dest_path = null;
        String destfilename = null;
        Array<Object> stat = new Array<Object>();
        int perms = 0;
        image = getIncluded(ImagePage.class, gVars, gConsts).wp_load_image(file);

        if (!is_resource(image)) {
            return new WP_Error(gVars, gConsts, "error_loading_image", strval(image));
        }

        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    image_resize_orig_w = intval(srcArray.getValue(0));
                    image_resize_orig_h = intval(srcArray.getValue(1));
                    image_resize_orig_type = srcArray.getValue(2);

                    return srcArray;
                }
            }.doAssign(QImage.getimagesize(gVars.webEnv, file));
        dims = image_resize_dimensions(image_resize_orig_w, image_resize_orig_h, max_w, max_h, crop);

        if (!booleanval(dims)) {
            return dims;
        }

        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    image_resize_dst_x = intval(srcArray.getValue(0));
                    image_resize_dst_y = intval(srcArray.getValue(1));
                    image_resize_src_x = intval(srcArray.getValue(2));
                    image_resize_src_y = intval(srcArray.getValue(3));
                    image_resize_dst_w = intval(srcArray.getValue(4));
                    image_resize_dst_h = intval(srcArray.getValue(5));
                    image_resize_src_w = intval(srcArray.getValue(6));
                    image_resize_src_h = intval(srcArray.getValue(7));

                    return srcArray;
                }
            }.doAssign(dims);
        newimage = Image.imagecreatetruecolor(gVars.webEnv, image_resize_dst_w, image_resize_dst_h);

    	// preserve PNG transparency
        if (equal(Image.IMAGETYPE_PNG, image_resize_orig_type) && true && true)/*Modified by Numiton*/

        /*Modified by Numiton*/
         {
            Image.imagealphablending(gVars.webEnv, newimage, false);
            Image.imagesavealpha(gVars.webEnv, newimage, true);
        }

        Image.imagecopyresampled(
            gVars.webEnv,
            newimage,
            intval(image),
            image_resize_dst_x,
            image_resize_dst_y,
            image_resize_src_x,
            image_resize_src_y,
            image_resize_dst_w,
            image_resize_dst_h,
            image_resize_src_w,
            image_resize_src_h);
        
    	// we don't need the original in memory anymore
        Image.imagedestroy(gVars.webEnv, intval(image));

    	// $suffix will be appended to the destination filename, just before the extension
        if (!booleanval(suffix)) {
            suffix = strval(image_resize_dst_w) + "x" + strval(image_resize_dst_h);
        }

        info = FileSystemOrSocket.pathinfo(file);
        dir = strval(info.getValue("dirname"));
        ext = info.getValue("extension");
        name = FileSystemOrSocket.basename(file, "." + strval(ext));

        if (!is_null(dest_path) && booleanval(_dest_path = FileSystemOrSocket.realpath(gVars.webEnv, dest_path))) {
            dir = _dest_path;
        }

        destfilename = dir + "/" + name + "-" + suffix + "." + strval(ext);

        if (equal(image_resize_orig_type, Image.IMAGETYPE_GIF)) {
            if (!Image.imagegif(gVars.webEnv, newimage, destfilename)) {
                return new WP_Error(gVars, gConsts, "resize_path_invalid", getIncluded(L10nPage.class, gVars, gConsts).__("Resize path invalid", "default"));
            }
        } else if (equal(image_resize_orig_type, Image.IMAGETYPE_PNG)) {
            if (!Image.imagepng(gVars.webEnv, newimage, destfilename)) {
                return new WP_Error(gVars, gConsts, "resize_path_invalid", getIncluded(L10nPage.class, gVars, gConsts).__("Resize path invalid", "default"));
            }
        } else {
    		// all other formats are converted to jpg
            destfilename = dir + "/" + name + "-" + suffix + ".jpg";

            if (!Image.imagejpeg(gVars.webEnv, newimage, destfilename, intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("jpeg_quality", jpeg_quality)))) {
                return new WP_Error(gVars, gConsts, "resize_path_invalid", getIncluded(L10nPage.class, gVars, gConsts).__("Resize path invalid", "default"));
            }
        }

        Image.imagedestroy(gVars.webEnv, newimage);
        
    	// Set correct file permissions
        stat = QFileSystemOrSocket.stat(gVars.webEnv, FileSystemOrSocket.dirname(destfilename));
        perms = intval(stat.getValue("mode")) & 0000666; //same permissions as parent folder, strip off the executable bits
        JFileSystemOrSocket.chmod(gVars.webEnv, destfilename, perms);

        return destfilename;
    }

 // resize an image to make a thumbnail or intermediate size, and return metadata describing the new copy
 // returns false if no image was created
    public Array<Object> image_make_intermediate_size(String file, int width, int height, boolean crop) {
        Object resized_file = null;
        Array<Object> info = new Array<Object>();

        if (booleanval(width) || booleanval(height)) {
            resized_file = image_resize(file, width, height, crop, strval(null), null, 90);

            if (!getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(resized_file) && booleanval(resized_file) && booleanval(info = QImage.getimagesize(gVars.webEnv, strval(resized_file)))) {
                resized_file = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("image_make_intermediate_size", resized_file);

                return new Array<Object>(
                    new ArrayEntry<Object>("file", FileSystemOrSocket.basename(strval(resized_file))),
                    new ArrayEntry<Object>("width", info.getValue(0)),
                    new ArrayEntry<Object>("height", info.getValue(1)));
            }
        }

        return new Array<Object>();
    }

    public Array<Object> image_get_intermediate_size(int post_id, Object size)/* Do not change type */
     {
        Array<Object> imagedata = new Array<Object>();
        Array<Object> data = new Array<Object>();
        Object file = null;
        Array<Object> areas = new Array<Object>();
        Object _size = null;
        String file_url = strval(false);

        if (!booleanval(imagedata = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_metadata(post_id, false))) {
            return new Array<Object>();
        }

    	// get the best one for a specified set of dimensions
        if (is_array(size) && !empty(imagedata.getValue("sizes"))) {
            for (Map.Entry javaEntry508 : (Set<Map.Entry>) imagedata.getArrayValue("sizes").entrySet()) {
                _size = javaEntry508.getKey();
                data = (Array<Object>) javaEntry508.getValue();

    			// already cropped to width or height; so use this size
                if ((equal(data.getValue("width"), ((Array) size).getValue(0)) && (intval(data.getValue("height")) <= intval(((Array) size).getValue(1)))) ||
                        (equal(data.getValue("height"), ((Array) size).getValue(1)) && (intval(data.getValue("width")) <= intval(((Array) size).getValue(0))))) {
                    file = data.getValue("file");
                    new ListAssigner<Object>() {
                            public Array<Object> doAssign(Array<Object> srcArray) {
                                if (strictEqual(srcArray, null)) {
                                    return null;
                                }

                                image_get_intermediate_size_width = strval(srcArray.getValue(0));
                                image_get_intermediate_size_height = srcArray.getValue(1);

                                return srcArray;
                            }
                        }.doAssign(image_constrain_size_for_editor(intval(data.getValue("width")), intval(data.getValue("height")), size));

                    return Array.compact(new ArrayEntry("file", file), new ArrayEntry("width", image_get_intermediate_size_width), new ArrayEntry("height", image_get_intermediate_size_height));
                }

    			// add to lookup table: area => size
                areas.putValue(intval(data.getValue("width")) * intval(data.getValue("height")), _size);
            }

            if (!booleanval(size) || !empty(areas)) {
    			// find for the smallest image not smaller than the desired size
                Array.ksort(areas);

                for (Map.Entry javaEntry509 : areas.entrySet()) {
                    _size = javaEntry509.getValue();
                    data = imagedata.getArrayValue("sizes").getArrayValue(_size);

                    if ((intval(data.getValue("width")) >= intval(((Array) size).getValue(0))) || (intval(data.getValue("height")) >= intval(((Array) size).getValue(1)))) {
                        file = data.getValue("file");
                        new ListAssigner<Object>() {
                                public Array<Object> doAssign(Array<Object> srcArray) {
                                    if (strictEqual(srcArray, null)) {
                                        return null;
                                    }

                                    image_get_intermediate_size_width = strval(srcArray.getValue(0));
                                    image_get_intermediate_size_height = srcArray.getValue(1);

                                    return srcArray;
                                }
                            }.doAssign(image_constrain_size_for_editor(intval(data.getValue("width")), intval(data.getValue("height")), size));

                        return Array.compact(new ArrayEntry("file", file), new ArrayEntry("width", image_get_intermediate_size_width), new ArrayEntry("height", image_get_intermediate_size_height));
                    }
                }
            }
        }

        if (is_array(size) || empty(size) || empty(imagedata.getArrayValue("sizes").getValue(size))) {
            return new Array<Object>();
        }

        data = imagedata.getArrayValue("sizes").getArrayValue(size);
    	// include the full filesystem path of the intermediate file
        if (empty(data.getValue("path")) && !empty(data.getValue("file"))) {
            file_url = getIncluded(PostPage.class, gVars, gConsts).wp_get_attachment_url(post_id);
            data.putValue("path", getIncluded(FunctionsPage.class, gVars, gConsts).path_join(FileSystemOrSocket.dirname(strval(imagedata.getValue("file"))), strval(data.getValue("file"))));
            data.putValue("url", getIncluded(FunctionsPage.class, gVars, gConsts).path_join(FileSystemOrSocket.dirname(file_url), strval(data.getValue("file"))));
        }

        return data;
    }

 // get an image to represent an attachment - a mime icon for files, thumbnail or intermediate size for images
 // returns an array (url, width, height), or false if no image is available
    public Array<Object> wp_get_attachment_image_src(int attachment_id, Object size, boolean icon) {
        Array<Object> image = new Array<Object>();
        String src = null;
        Object icon_dir = null;
        String src_file = null;

    	// get a thumbnail or intermediate image if there is one
        if (booleanval(image = image_downsize(attachment_id, size))) {
            return image;
        }

        if (icon && booleanval((src = getIncluded(PostPage.class, gVars, gConsts).wp_mime_type_icon(attachment_id)))) {
            icon_dir = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("icon_dir", gConsts.getABSPATH() + gConsts.getWPINC() + "/images/crystal");
            src_file = strval(icon_dir) + "/" + FileSystemOrSocket.basename(src);
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        wp_get_attachment_image_src_width = srcArray.getValue(0);
                        wp_get_attachment_image_src_height = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(QImage.getimagesize(gVars.webEnv, src_file));
        }

        if (booleanval(src) && booleanval(wp_get_attachment_image_src_width) && booleanval(wp_get_attachment_image_src_height)) {
            return new Array<Object>(new ArrayEntry<Object>(src), new ArrayEntry<Object>(wp_get_attachment_image_src_width), new ArrayEntry<Object>(wp_get_attachment_image_src_height));
        }

        return new Array<Object>();
    }

    public String wp_get_attachment_image(int attachment_id) {
        return wp_get_attachment_image(attachment_id, "thumbnail", false);
    }

    public String wp_get_attachment_image(int attachment_id, Object size) {
        return wp_get_attachment_image(attachment_id, size, false);
    }

 // as per wp_get_attachment_image_src, but returns an <img> tag
    public String wp_get_attachment_image(int attachment_id, Object size, boolean /* Do not change type */ icon) {
        String html = null;
        Array<Object> image = new Array<Object>();
        String hwstring = null;
        html = "";
        image = wp_get_attachment_image_src(attachment_id, size, icon);

        if (booleanval(image)) {
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        wp_get_attachment_image_src = strval(srcArray.getValue(0));
                        wp_get_attachment_image_width = intval(srcArray.getValue(1));
                        wp_get_attachment_image_height = intval(srcArray.getValue(2));

                        return srcArray;
                    }
                }.doAssign(image);
            hwstring = image_hwstring(wp_get_attachment_image_width, wp_get_attachment_image_height);

            if (is_array(size)) {
                size = Strings.join("x", (Array) size);
            }

            html = "<img src=\"" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(wp_get_attachment_image_src) + "\" " + hwstring + "class=\"attachment-" +
                getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(size)) + "\" alt=\"\" />";
        }

        return html;
    }

    public Object gallery_shortcode(Array<Object> attr) {
        Object output = null;
        int id = 0;
        Array<Object> attachments = new Array<Object>();
        Object orderby = null;
        Object size = null;
        StdClass attachment = null;
        String listtag = null;
        String itemtag = null;
        String captiontag = null;
        int columns = 0;
        float itemwidth = 0;
        Object link = null;
        Object icontag = null;
        int i = 0;
        
    	// Allow plugins/themes to override the default gallery template.
        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("post_gallery", "", attr);

        if (!equal(output, "")) {
            return output;
        }

    	// We're trusting author input, so let's at least make sure it looks like a valid orderby statement
        if (isset(attr.getValue("orderby"))) {
            attr.putValue("orderby", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_sql_orderby(strval(attr.getValue("orderby"))));

            if (!booleanval(attr.getValue("orderby"))) {
                attr.arrayUnset("orderby");
            }
        }

        {
            Array<Object> shortcode_attsArray = getIncluded(ShortcodesPage.class, gVars, gConsts).shortcode_atts(new Array<Object>(
                        new ArrayEntry<Object>("orderby", "menu_order ASC, ID ASC"),
                        new ArrayEntry<Object>("id", StdClass.getValue(gVars.post, "ID")),
                        new ArrayEntry<Object>("itemtag", "dl"),
                        new ArrayEntry<Object>("icontag", "dt"),
                        new ArrayEntry<Object>("captiontag", "dd"),
                        new ArrayEntry<Object>("columns", 3),
                        new ArrayEntry<Object>("size", "thumbnail")), attr);
            id = intval(Array.extractVar(shortcode_attsArray, "id", id, Array.EXTR_OVERWRITE));
            orderby = Array.extractVar(shortcode_attsArray, "orderby", orderby, Array.EXTR_OVERWRITE);
            size = Array.extractVar(shortcode_attsArray, "size", size, Array.EXTR_OVERWRITE);
            listtag = strval(Array.extractVar(shortcode_attsArray, "listtag", listtag, Array.EXTR_OVERWRITE));
            itemtag = strval(Array.extractVar(shortcode_attsArray, "itemtag", itemtag, Array.EXTR_OVERWRITE));
            captiontag = strval(Array.extractVar(shortcode_attsArray, "captiontag", captiontag, Array.EXTR_OVERWRITE));
            columns = intval(Array.extractVar(shortcode_attsArray, "columns", columns, Array.EXTR_OVERWRITE));
            icontag = Array.extractVar(shortcode_attsArray, "icontag", icontag, Array.EXTR_OVERWRITE);
        }

        id = id;
        attachments = getIncluded(PostPage.class, gVars, gConsts)
                          .get_children("post_parent=" + strval(id) + "&post_type=attachment&post_mime_type=image&orderby=" + strval(orderby), gConsts.getOBJECT());

        if (empty(attachments)) {
            return "";
        }

        if (getIncluded(QueryPage.class, gVars, gConsts).is_feed()) {
            output = "\n";

            for (Map.Entry javaEntry510 : attachments.entrySet()) {
                id = intval(javaEntry510.getKey());
                attachment = (StdClass) javaEntry510.getValue();
                output = strval(output) + getIncluded(Post_templatePage.class, gVars, gConsts).wp_get_attachment_link(id, size, true, false) + "\n";
            }

            return output;
        }

        listtag = getIncluded(FormattingPage.class, gVars, gConsts).tag_escape(listtag);
        itemtag = getIncluded(FormattingPage.class, gVars, gConsts).tag_escape(itemtag);
        captiontag = getIncluded(FormattingPage.class, gVars, gConsts).tag_escape(captiontag);
        columns = columns;
        itemwidth = ((columns > 0)
            ? Math.floor(floatval(100) / floatval(columns))
            : floatval(100));
        output = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                    "gallery_style",
                    "\n\t\t<style type=\'text/css\'>\n\t\t\t.gallery {\n\t\t\t\tmargin: auto;\n\t\t\t}\n\t\t\t.gallery-item {\n\t\t\t\tfloat: left;\n\t\t\t\tmargin-top: 10px;\n\t\t\t\ttext-align: center;\n\t\t\t\twidth: " +
                    strval(itemwidth) +
                    "%;\t\t\t}\n\t\t\t.gallery img {\n\t\t\t\tborder: 2px solid #cfcfcf;\n\t\t\t}\n\t\t\t.gallery-caption {\n\t\t\t\tmargin-left: 0;\n\t\t\t}\n\t\t</style>\n\t\t<!-- see gallery_shortcode() in wp-includes/media.php -->\n\t\t<div class=\'gallery\'>");

        for (Map.Entry javaEntry511 : attachments.entrySet()) {
            id = intval(javaEntry511.getKey());
            attachment = (StdClass) javaEntry511.getValue();
            link = getIncluded(Post_templatePage.class, gVars, gConsts).wp_get_attachment_link(id, size, true, false);
            output = strval(output) + "<" + itemtag + " class=\'gallery-item\'>";
            output = strval(output) + "\n\t\t\t<" + strval(icontag) + " class=\'gallery-icon\'>\n\t\t\t\t" + strval(link) + "\n\t\t\t</" + strval(icontag) + ">";

            if (booleanval(captiontag) && booleanval(Strings.trim(strval(StdClass.getValue(attachment, "post_excerpt"))))) {
                output = strval(output) + "\n\t\t\t\t<" + captiontag + " class=\'gallery-caption\'>\n\t\t\t\t" + StdClass.getValue(attachment, "post_excerpt") + "\n\t\t\t\t</" + captiontag + ">";
            }

            output = strval(output) + "</" + itemtag + ">";

            if ((columns > 0) && equal(++i % columns, 0)) {
                output = strval(output) + "<br style=\"clear: both\" />";
            }
        }

        output = strval(output) + "\n\t\t\t<br style=\'clear: both;\' />\n\t\t</div>\n";

        return output;
    }

    public void previous_image_link() {
        adjacent_image_link(true);
    }

    public void next_image_link() {
        adjacent_image_link(false);
    }

    public void adjacent_image_link(boolean prev) {
        Array<Object> attachments = new Array<Object>();
        StdClass attachment = null;
        int k = 0;
        gVars.post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(gVars.post, gConsts.getOBJECT(), "raw");
        attachments = Array.array_values(
                    getIncluded(PostPage.class, gVars, gConsts).get_children(
                            "post_parent=" + StdClass.getValue(gVars.post, "post_parent") + "&post_type=attachment&post_mime_type=image&orderby=menu_order ASC, ID ASC",
                            gConsts.getOBJECT()));

        for (Map.Entry javaEntry512 : attachments.entrySet()) {
            k = intval(javaEntry512.getKey());
            attachment = (StdClass) javaEntry512.getValue();

            if (equal(StdClass.getValue(attachment, "ID"), StdClass.getValue(gVars.post, "ID"))) {
                break;
            }
        }

        k = (prev
            ? (k - 1)
            : (k + 1));

        if (isset(attachments.getValue(k))) {
            echo(gVars.webEnv, getIncluded(Post_templatePage.class, gVars, gConsts).wp_get_attachment_link(intval(((StdClass) attachments.getValue(k)).fields.getValue("ID")), "thumbnail", true, false));
        }
    }

    public Array<Object> get_attachment_taxonomies(Object attachmentObj)/* Do not change type */
     {
        String filename = null;
        Array<Object> objects = new Array<Object>();
        Object token = null;
        Array<Object> taxonomies = new Array<Object>();
        Array<Object> taxes = new Array<Object>();
        Object object = null;
        StdClass attachment = null;

        if (is_int(attachmentObj)) {
            attachment = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(attachmentObj, gConsts.getOBJECT(), "raw");
        } else if (is_array(attachmentObj)) {
            attachment = Array.toStdClass((Array) attachmentObj);
        }

        if (!is_object(attachment)) {
            return new Array<Object>();
        }

        filename = FileSystemOrSocket.basename(strval(StdClass.getValue(attachment, "guid")));
        objects = new Array<Object>(new ArrayEntry<Object>("attachment"));

        if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(filename, "."))) {
            objects.putValue("attachment:" + Strings.substr(filename, Strings.strrpos(filename, ".") + 1));
        }

        if (!empty(StdClass.getValue(attachment, "post_mime_type"))) {
            objects.putValue("attachment:" + StdClass.getValue(attachment, "post_mime_type"));

            if (!strictEqual(BOOLEAN_FALSE, Strings.strpos(strval(StdClass.getValue(attachment, "post_mime_type")), "/"))) {
                for (Map.Entry javaEntry513 : Strings.explode("/", strval(StdClass.getValue(attachment, "post_mime_type"))).entrySet()) {
                    token = javaEntry513.getValue();

                    if (!empty(token)) {
                        objects.putValue("attachment:" + strval(token));
                    }
                }
            }
        }

        taxonomies = new Array<Object>();

        for (Map.Entry javaEntry514 : objects.entrySet()) {
            object = javaEntry514.getValue();

            if (booleanval(taxes = getIncluded(TaxonomyPage.class, gVars, gConsts).get_object_taxonomies(object))) {
                taxonomies = Array.array_merge(taxonomies, taxes);
            }
        }

        return Array.array_unique(taxonomies);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        /* Start of block */
        super.startBlock("__wp_includes_media_block1");
        gVars.webEnv = webEnv;
        getIncluded(ShortcodesPage.class, gVars, gConsts).add_shortcode("gallery", Callback.createCallbackArray(this, "gallery_shortcode"));

        return DEFAULT_VAL;
    }
}
