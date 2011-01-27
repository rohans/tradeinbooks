/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: ImagePage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.numiton.nwp.wp_includes.*;
import org.numiton.nwp.wp_includes.MediaPage;
import org.numiton.nwp.wp_includes.PluginPage;
import org.numiton.nwp.wp_includes.PostPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Math;
import com.numiton.Options;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.file.FileSystemOrSocket;
import com.numiton.generic.ListAssigner;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.image.Image;
import com.numiton.ntile.til.libraries.php.quercus.*;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class ImagePage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(ImagePage.class.getName());

    /**
     * Generated in place of local variable 'uwidth' from method
     * 'wp_generate_attachment_metadata' because it is used inside an inner
     * class.
     */
    Object wp_generate_attachment_metadata_uwidth = null;

    /**
     * Generated in place of local variable 'uheight' from method
     * 'wp_generate_attachment_metadata' because it is used inside an inner
     * class.
     */
    Object wp_generate_attachment_metadata_uheight = null;

    /**
     * Generated in place of local variable 'n' from method 'wp_exif_frac2dec'
     * because it is used inside an inner class.
     */
    Object wp_exif_frac2dec_n = null;

    /**
     * Generated in place of local variable 'd' from method 'wp_exif_frac2dec'
     * because it is used inside an inner class.
     */
    Object wp_exif_frac2dec_d = null;

    /**
     * Generated in place of local variable 'date' from method 'wp_exif_date2ts'
     * because it is used inside an inner class.
     */
    String wp_exif_date2ts_date = null;

    /**
     * Generated in place of local variable 'time' from method 'wp_exif_date2ts'
     * because it is used inside an inner class.
     */
    Object wp_exif_date2ts_time = null;

    /**
     * Generated in place of local variable 'y' from method 'wp_exif_date2ts'
     * because it is used inside an inner class.
     */
    Object wp_exif_date2ts_y = null;

    /**
     * Generated in place of local variable 'm' from method 'wp_exif_date2ts'
     * because it is used inside an inner class.
     */
    Object wp_exif_date2ts_m = null;

    /**
     * Generated in place of local variable 'd' from method 'wp_exif_date2ts'
     * because it is used inside an inner class.
     */
    Object wp_exif_date2ts_d = null;

    /**
     * Generated in place of local variable 'sourceImageType' from method
     * 'wp_read_image_metadata' because it is used inside an inner class.
     */
    Object wp_read_image_metadata_sourceImageType = null;

    @Override
    @RequestMapping("/wp-admin/includes/image.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_admin/includes/image";
    }

    /**
     * File contains all the administration image manipulation functions.
     *
     * @package WordPress
     */

    /**
     * wp_create_thumbnail() - Create a thumbnail from an Image given a maximum side size.
     *
     * @package WordPress
     * @param	mixed	$file	Filename of the original image, Or attachment id
     * @param	int		$max_side	Maximum length of a single side for the thumbnail
     * @return	string			Thumbnail path on success, Error string on failure
     *
     * This function can handle most image file formats which PHP supports.
     * If PHP does not have the functionality to save in a file of the same format, the thumbnail will be created as a jpeg.
     */
    public String wp_create_thumbnail(String file, int max_side, Object deprecated) {
        String thumbpath = null;
        thumbpath = strval(getIncluded(MediaPage.class, gVars, gConsts).image_resize(file, max_side, max_side, false, strval(null), null, 90));

        return strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_create_thumbnail", thumbpath));
    }

    /**
     * wp_crop_image() - Crop an Image to a given size.
     *
     * @package WordPress
     * @internal Missing Long Description
     * @param	int	$src_file	The source file
     * @param	int	$src_x		The start x position to crop from
     * @param	int	$src_y		The start y position to crop from
     * @param	int	$src_w		The width to crop
     * @param	int	$src_h		The height to crop
     * @param	int	$dst_w		The destination width
     * @param	int	$dst_h		The destination height
     * @param	int	$src_abs	If the source crop points are absolute
     * @param	int	$dst_file	The destination file to write to
     * @return	string			New filepath on success, String error message on failure
     *
     */
    public String wp_crop_image(Object src_fileObj, int src_x, int src_y, int src_w, int src_h, int dst_w, int dst_h, boolean src_abs, String dst_file) {
        Object src = null;
        int dst = 0;
        String src_file;

        // Modified by Numiton
        if (is_numeric(src_fileObj)) { // Handle int as attachment ID
            src_file = strval(getIncluded(PostPage.class, gVars, gConsts).get_attached_file(intval(src_fileObj), false));
        } else {
            src_file = strval(src_fileObj);
        }

        src = wp_load_image(src_file);

        if (!is_resource(src)) {
            return strval(src);
        }

        dst = Image.imagecreatetruecolor(gVars.webEnv, dst_w, dst_h);

        if (src_abs) {
            src_w = src_w - src_x;
            src_h = src_h - src_y;
        }

        if (true)/*Modified by Numiton*/
         {
            Image.imageantialias(gVars.webEnv, dst, true);
        }

        Image.imagecopyresampled(gVars.webEnv, dst, intval(src), 0, 0, src_x, src_y, dst_w, dst_h, src_w, src_h);
        Image.imagedestroy(gVars.webEnv, intval(src)); // Free up memory

        if (!booleanval(dst_file)) {
            dst_file = Strings.str_replace(FileSystemOrSocket.basename(src_file), "cropped-" + FileSystemOrSocket.basename(src_file), src_file);
        }

        dst_file = QRegExPerl.preg_replace("/\\.[^\\.]+$/", ".jpg", dst_file);

        if (Image.imagejpeg(gVars.webEnv, dst, dst_file)) {
            return dst_file;
        } else {
            return strval(false);
        }
    }

    /**
     * wp_generate_attachment_metadata() - Generate post Image attachment Metadata
     *
     * @package WordPress
     * @internal Missing Long Description
     * @param	int		$attachment_id	Attachment Id to process
     * @param	string	$file	Filepath of the Attached image
     * @return	mixed			Metadata for attachment
     *
     */
    public Object wp_generate_attachment_metadata(int attachment_id, String file) {
        StdClass attachment = null;
        Array<Object> metadata = new Array<Object>();
        Array<Object> imagesize = new Array<Object>();
        Array<Object> sizes = new Array<Object>();
        Array<Object> resized = new Array<Object>();
        Object size = null;
        Array<Object> image_meta;
        attachment = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(attachment_id, gConsts.getOBJECT(), "raw");
        metadata = new Array<Object>();

        if (QRegExPerl.preg_match("!^image/!", getIncluded(PostPage.class, gVars, gConsts).get_post_mime_type(attachment)) && file_is_displayable_image(file)) {
            imagesize = QImage.getimagesize(gVars.webEnv, file);
            metadata.putValue("width", imagesize.getValue(0));
            metadata.putValue("height", imagesize.getValue(1));
            new ListAssigner<Object>() {
                    public Array<Object> doAssign(Array<Object> srcArray) {
                        if (strictEqual(srcArray, null)) {
                            return null;
                        }

                        wp_generate_attachment_metadata_uwidth = srcArray.getValue(0);
                        wp_generate_attachment_metadata_uheight = srcArray.getValue(1);

                        return srcArray;
                    }
                }.doAssign(wp_shrink_dimensions(intval(metadata.getValue("width")), intval(metadata.getValue("height")), 128, 96));
            metadata.putValue("hwstring_small", "height=\'" + strval(wp_generate_attachment_metadata_uheight) + "\' width=\'" + strval(wp_generate_attachment_metadata_uwidth) + "\'");
            metadata.putValue("file", file);
            
            // make thumbnails and other intermediate sizes
            sizes = new Array<Object>(new ArrayEntry<Object>("thumbnail"), new ArrayEntry<Object>("medium"));
            sizes = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("intermediate_image_sizes", sizes);

            for (Map.Entry javaEntry147 : sizes.entrySet()) {
                size = javaEntry147.getValue();
                resized = getIncluded(MediaPage.class, gVars, gConsts).image_make_intermediate_size(
                        file,
                        intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option(strval(size) + "_size_w")),
                        intval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option(strval(size) + "_size_h")),
                        booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option(strval(size) + "_crop")));

                if (booleanval(resized)) {
                    metadata.getArrayValue("sizes").putValue(size, resized);
                }
            }

            // fetch additional metadata from exif/iptc
            image_meta = wp_read_image_metadata(file);

            if (booleanval(image_meta)) {
                metadata.putValue("image_meta", image_meta);
            }
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_generate_attachment_metadata", metadata);
    }

    /**
     * wp_load_image() - Load an image which PHP Supports.
     *
     * @package WordPress
     * @internal Missing Long Description
     * @param	string	$file	Filename of the image to load
     * @return	resource		The resulting image resource on success, Error string on failure.
     *
     */
    public Object wp_load_image(Object fileObj) {
        int image = 0;

        // Modified by Numiton
        String file;

        if (is_numeric(fileObj)) {
            file = strval(getIncluded(PostPage.class, gVars, gConsts).get_attached_file(intval(fileObj), false));
        } else {
            file = strval(fileObj);
        }

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, file)) {
            return QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("File \'%s\' doesn\'t exist?", "default"), file);
        }

        if (!true)/*Modified by Numiton*/
         {
            return getIncluded(L10nPage.class, gVars, gConsts).__("The GD image library is not installed.", "default");
        }

        // Set artificially high because GD uses uncompressed images in memory
        Options.ini_set(gVars.webEnv, "memory_limit", "256M");
        image = Image.imagecreatefromstring(gVars.webEnv, FileSystemOrSocket.file_get_contents(gVars.webEnv, file));

        if (!is_resource(image)) {
            return QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("File \'%s\' is not an image.", "default"), file);
        }

        return image;
    }

    /**
     * get_udims() - Calculated the new dimentions for downsampled images
     *
     * @package WordPress
     * @internal Missing Description
     * @see wp_shrink_dimensions()
     * @param	int		$width	Current width of the image
     * @param	int 	$height	Current height of the image
     * @return	mixed			Array(height,width) of shrunk dimensions.
     *
     */
    public Array<Object> get_udims(int width, int height) {
        return wp_shrink_dimensions(width, height, 128, 96);
    }

    /**
     * wp_shrink_dimensions() - Calculates the new dimentions for a downsampled image.
     *
     * @package WordPress
     * @internal Missing Long Description
     * @param	int		$width	Current width of the image
     * @param	int 	$height	Current height of the image
     * @param	int		$wmax	Maximum wanted width
     * @param	int		$hmax	Maximum wanted height
     * @return	mixed			Array(height,width) of shrunk dimensions.
     *
     */
    public Array<Object> wp_shrink_dimensions(int width, int height, int wmax, int hmax) {
        return getIncluded(MediaPage.class, gVars, gConsts).wp_constrain_dimensions(width, height, wmax, hmax);
    }

 // convert a fraction string to a decimal
    public float wp_exif_frac2dec(String str) {
        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    wp_exif_frac2dec_n = srcArray.getValue(0);
                    wp_exif_frac2dec_d = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(Strings.explode("/", str));

        if (!empty(wp_exif_frac2dec_d)) {
            return floatval(wp_exif_frac2dec_n) / floatval(wp_exif_frac2dec_d);
        }

        return floatval(str);
    }

 // convert the exif date format to a unix timestamp
    public int wp_exif_date2ts(String str) {
    	// seriously, who formats a date like 'YYYY:MM:DD hh:mm:ss'?
        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    wp_exif_date2ts_date = srcArray.getValue(0);
                    wp_exif_date2ts_time = srcArray.getValue(1);

                    return srcArray;
                }
            }.doAssign(Strings.explode(" ", Strings.trim(str)));
        new ListAssigner<String>() {
                public Array<String> doAssign(Array<String> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    wp_exif_date2ts_y = srcArray.getValue(0);
                    wp_exif_date2ts_m = srcArray.getValue(1);
                    wp_exif_date2ts_d = srcArray.getValue(2);

                    return srcArray;
                }
            }.doAssign(Strings.explode(":", wp_exif_date2ts_date));

        return QDateTime.strtotime(strval(wp_exif_date2ts_y) + "-" + strval(wp_exif_date2ts_m) + "-" + strval(wp_exif_date2ts_d) + " " + strval(wp_exif_date2ts_time));
    }

 // get extended image metadata, exif or iptc as available
    public Array<Object> wp_read_image_metadata(String file) {
        Array<Object> meta = new Array<Object>();
        Array<Object> info = new Array<Object>();
        Array<Object> iptc = new Array<Object>();
        Array<Object> exif = new Array<Object>();

        if (!FileSystemOrSocket.file_exists(gVars.webEnv, file)) {
            return new Array<Object>();
        }

        new ListAssigner<Object>() {
                public Array<Object> doAssign(Array<Object> srcArray) {
                    if (strictEqual(srcArray, null)) {
                        return null;
                    }

                    wp_read_image_metadata_sourceImageType = srcArray.getValue(2);

                    return srcArray;
                }
            }.doAssign(QImage.getimagesize(gVars.webEnv, file));
            
    	// exif contains a bunch of data we'll probably never need formatted in ways that are difficult to use.
    	// We'll normalize it and just extract the fields that are likely to be useful.  Fractions and numbers
    	// are converted to floats, dates to unix timestamps, and everything else to strings.
        meta = new Array<Object>(
                new ArrayEntry<Object>("aperture", 0),
                new ArrayEntry<Object>("credit", ""),
                new ArrayEntry<Object>("camera", ""),
                new ArrayEntry<Object>("caption", ""),
                new ArrayEntry<Object>("created_timestamp", 0),
                new ArrayEntry<Object>("copyright", ""),
                new ArrayEntry<Object>("focal_length", 0),
                new ArrayEntry<Object>("iso", 0),
                new ArrayEntry<Object>("shutter_speed", 0),
                new ArrayEntry<Object>("title", ""));

        // read iptc first, since it might contain data not available in exif such as caption, description etc
        if (true)/*Modified by Numiton*/
         {
            QImage.getimagesize(gVars.webEnv, file, info);

            if (!empty(info.getValue("APP13"))) {
                iptc = new Array<Object>();

                if (!empty(iptc.getArrayValue("2#110").getValue(0))) { // credit
                    meta.putValue("credit", Strings.trim(strval(iptc.getArrayValue("2#110").getValue(0))));
                } else if (!empty(iptc.getArrayValue("2#080").getValue(0))) { //byline
                    meta.putValue("credit", Strings.trim(strval(iptc.getArrayValue("2#080").getValue(0))));
                }

                if (!empty(iptc.getArrayValue("2#055").getValue(0)) && !empty(iptc.getArrayValue("2#060").getValue(0))) { // created datee and time
                    meta.putValue("created_timestamp", QDateTime.strtotime(strval(iptc.getArrayValue("2#055").getValue(0)) + " " + strval(iptc.getArrayValue("2#060").getValue(0))));
                }

                if (!empty(iptc.getArrayValue("2#120").getValue(0))) { // caption
                    meta.putValue("caption", Strings.trim(strval(iptc.getArrayValue("2#120").getValue(0))));
                }

                if (!empty(iptc.getArrayValue("2#116").getValue(0))) { // copyright
                    meta.putValue("copyright", Strings.trim(strval(iptc.getArrayValue("2#116").getValue(0))));
                }

                if (!empty(iptc.getArrayValue("2#005").getValue(0))) { // title
                    meta.putValue("title", Strings.trim(strval(iptc.getArrayValue("2#005").getValue(0))));
                }
            }
        }

        // fetch additional info from exif if available
        if (false && /*Modified by Numiton*/
                Array.in_array(wp_read_image_metadata_sourceImageType,
                    (Array) getIncluded(PluginPage.class, gVars, gConsts).apply_filters(
                        "wp_read_image_metadata_types",
                        new Array<Object>(new ArrayEntry<Object>(Image.IMAGETYPE_JPEG), new ArrayEntry<Object>(Image.IMAGETYPE_TIFF_II), new ArrayEntry<Object>(Image.IMAGETYPE_TIFF_MM))))) {
            exif = new Array<Object>();

            if (!empty(exif.getValue("FNumber"))) {
                meta.putValue("aperture", Math.round(wp_exif_frac2dec(strval(exif.getValue("FNumber"))), 2));
            }

            if (!empty(exif.getValue("Model"))) {
                meta.putValue("camera", Strings.trim(strval(exif.getValue("Model"))));
            }

            if (!empty(exif.getValue("DateTimeDigitized"))) {
                meta.putValue("created_timestamp", wp_exif_date2ts(strval(exif.getValue("DateTimeDigitized"))));
            }

            if (!empty(exif.getValue("FocalLength"))) {
                meta.putValue("focal_length", wp_exif_frac2dec(strval(exif.getValue("FocalLength"))));
            }

            if (!empty(exif.getValue("ISOSpeedRatings"))) {
                meta.putValue("iso", exif.getValue("ISOSpeedRatings"));
            }

            if (!empty(exif.getValue("ExposureTime"))) {
                meta.putValue("shutter_speed", wp_exif_frac2dec(strval(exif.getValue("ExposureTime"))));
            }
        }
        
        // FIXME: try other exif libraries if available

        return (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("wp_read_image_metadata", meta, file, wp_read_image_metadata_sourceImageType);
    }

 // is the file a real image file?
    public boolean file_is_valid_image(String path) {
        Array<Object> size = new Array<Object>();
        size = QImage.getimagesize(gVars.webEnv, path);

        return !empty(size);
    }

 // is the file an image suitable for displaying within a web page?
    public boolean file_is_displayable_image(String path) {
        Array<Object> info = new Array<Object>();
        boolean result = false;
        info = QImage.getimagesize(gVars.webEnv, path);

        if (empty(info)) {
            result = false;
        } else if (!Array.in_array(
                    info.getValue(2),
                    new Array<Object>(new ArrayEntry<Object>(Image.IMAGETYPE_GIF), new ArrayEntry<Object>(Image.IMAGETYPE_JPEG), new ArrayEntry<Object>(Image.IMAGETYPE_PNG)))) {
            result = false;
        } else if ((intval(info.getValue("channels")) > 0) && !equal(info.getValue("channels"), 3)) {
        	// only gif, jpeg and png images can reliably be displayed
            result = false;
        } else {
        	// some web browsers can't display cmyk or grayscale jpegs
            result = true;
        }

        return booleanval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("file_is_displayable_image", result, path));
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
