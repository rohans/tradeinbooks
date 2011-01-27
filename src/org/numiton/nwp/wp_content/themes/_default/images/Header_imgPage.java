/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Header_imgPage.java,v 1.4 2008/10/14 13:15:50 numiton Exp $
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
package org.numiton.nwp.wp_content.themes._default.images;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.DEFAULT_VAL;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ListAssigner;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.Ref;
import com.numiton.image.Image;
import com.numiton.string.Strings;

@Controller
@Scope("request")
public class Header_imgPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Header_imgPage.class.getName());

	@Override
	@RequestMapping("/wp-content/themes/default/images/header-img.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_content/themes/_default/images/header_img";
	}

	@Override
    public boolean isBinaryOutput() {
	    return true;
    }

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_content_themes__default_images_header_img_block1");
		gVars.webEnv = webEnv;
		img = "kubrickheader.jpg";
		if (!true)
		/*Modified by Numiton*/
		{
			Network.header(gVars.webEnv, "Location: kubrickheader.jpg");
			System.exit();
		}
		gVars._default = strval(false);
		vars = new Array<Object>(new ArrayEntry<Object>("upper", new Array<Object>(new ArrayEntry<Object>("r1"), new ArrayEntry<Object>("g1"), new ArrayEntry<Object>("b1"))), new ArrayEntry<Object>(
		        "lower", new Array<Object>(new ArrayEntry<Object>("r2"), new ArrayEntry<Object>("g2"), new ArrayEntry<Object>("b2"))));

		/* Modified by Numiton */
		Array<Ref<Integer>> subvarsArray = new Array<Ref<Integer>>(new ArrayEntry("r1", r1), new ArrayEntry("g1", g1), new ArrayEntry("b1", b1), new ArrayEntry("r2", r2), new ArrayEntry("g2", g2),
		        new ArrayEntry("b2", b2));
		for (Map.Entry javaEntry363 : vars.entrySet()) {
			gVars.var = strval(javaEntry363.getKey());
			subvars = (Array<Object>) javaEntry363.getValue();
			if (isset(gVars.webEnv._GET.getValue(gVars.var))) {
				for (Map.Entry javaEntry364 : subvars.entrySet()) {
					gVars.index = javaEntry364.getKey();
					subvar = javaEntry364.getValue();

					/* Modified by Numiton */
					Ref subvarObj = subvarsArray.getRef(subvar);
					length = intval(floatval(Strings.strlen(strval(gVars.webEnv._GET.getValue(gVars.var)))) / 3);
					gVars.v = Strings.substr(strval(gVars.webEnv._GET.getValue(gVars.var)), intval(gVars.index) * length, length);
					if (equal(length, 1)) {
						gVars.v = "" + gVars.v + gVars.v;
					}
					subvarObj.value = com.numiton.Math.hexdec(gVars.v);
					if (intval(subvarObj.value) < 0 || intval(subvarObj.value) > 255) {
						gVars._default = strval(true);
					}
				}
			}
			else {
				gVars._default = strval(true);
			}
		}
		if (booleanval(gVars._default)) {
			new ListAssigner<Object>() {
				public Array<Object> doAssign(Array<Object> srcArray) {
					if (strictEqual(srcArray, null)) {
						return null;
					}
					r1.value = intval(srcArray.getValue(0));
					g1.value = intval(srcArray.getValue(1));
					b1.value = intval(srcArray.getValue(2));
					r2.value = intval(srcArray.getValue(3));
					g2.value = intval(srcArray.getValue(4));
					b2.value = intval(srcArray.getValue(5));
					return srcArray;
				}
			}.doAssign(new Array<Object>(new ArrayEntry<Object>(105), new ArrayEntry<Object>(174), new ArrayEntry<Object>(231), new ArrayEntry<Object>(65), new ArrayEntry<Object>(128),
			        new ArrayEntry<Object>(182)));
		}
		im = Image.imagecreatefromjpeg(gVars.webEnv, img);
		white = Image.imagecolorat(gVars.webEnv, im, 15, 15);
		h = 182;
		corners = new Array<Object>(new ArrayEntry<Object>(0, new Array<Object>(new ArrayEntry<Object>(25), new ArrayEntry<Object>(734))), new ArrayEntry<Object>(1, new Array<Object>(
		        new ArrayEntry<Object>(23), new ArrayEntry<Object>(736))), new ArrayEntry<Object>(2, new Array<Object>(new ArrayEntry<Object>(22), new ArrayEntry<Object>(737))),
		        new ArrayEntry<Object>(3, new Array<Object>(new ArrayEntry<Object>(21), new ArrayEntry<Object>(738))), new ArrayEntry<Object>(4, new Array<Object>(new ArrayEntry<Object>(21),
		                new ArrayEntry<Object>(738))), new ArrayEntry<Object>(177, new Array<Object>(new ArrayEntry<Object>(21), new ArrayEntry<Object>(738))), new ArrayEntry<Object>(178,
		                new Array<Object>(new ArrayEntry<Object>(21), new ArrayEntry<Object>(738))), new ArrayEntry<Object>(179, new Array<Object>(new ArrayEntry<Object>(22), new ArrayEntry<Object>(
		                737))), new ArrayEntry<Object>(180, new Array<Object>(new ArrayEntry<Object>(23), new ArrayEntry<Object>(736))), new ArrayEntry<Object>(181, new Array<Object>(
		                new ArrayEntry<Object>(25), new ArrayEntry<Object>(734))));
		for (gVars.i = 0; gVars.i < h; gVars.i++) {
			x1 = 19;
			x2 = 740;
			Image.imageline(gVars.webEnv, im, x1, 18 + gVars.i, x2, 18 + gVars.i, white);
		}
		for (int i = 0; i < h; i++) {
			x1 = 20;
			x2 = 739;
			
			int tmpr1 = r1.value;
			int tmpr2 = r2.value;
			int tmpg1 = g1.value;
			int tmpg2 = g2.value;
			int tmpb1 = b1.value;
			int tmpb2 = b2.value;
			
			// Sanitized by Numiton
			r = ( tmpr2 - tmpr1 != 0 ) ? tmpr1 + intval(( tmpr2 - tmpr1 ) * ( floatval(i) / h )) : tmpr1;
			g = ( tmpg2 - tmpg1 != 0 ) ? tmpg1 + intval(( tmpg2 - tmpg1 ) * ( floatval(i) / h )) : tmpg1;
			b = ( tmpb2 - tmpb1 != 0 ) ? tmpb1 + intval(( tmpb2 - tmpb1 ) * ( floatval(i) / h )) : tmpb1;
			
			int color = Image.imagecolorallocate(im, r, g, b);
			if (Array.array_key_exists(i, corners)) {
				Image.imageline(gVars.webEnv, im, x1, 18 + i, x2, 18 + i, white);
				new ListAssigner<Object>() {
					public Array<Object> doAssign(Array<Object> srcArray) {
						if (strictEqual(srcArray, null)) {
							return null;
						}
						x1 = intval(srcArray.getValue(0));
						x2 = intval(srcArray.getValue(1));
						return srcArray;
					}
				}.doAssign(corners.getArrayValue(i));
			}
			Image.imageline(gVars.webEnv, im, x1, 18 + i, x2, 18 + i, color);
		}
		Network.header(gVars.webEnv, "Content-Type: image/jpeg");
		Image.imagejpeg(gVars.webEnv, im, "", 92);
		Image.imagedestroy(gVars.webEnv, im);
		return DEFAULT_VAL;
	}
	public String	     img;
	public Array<Object>	vars;
	public int	         length;
	public Object	     subvar;
	public Array<Object>	subvars;
	public Ref<Integer>	 r1	        = new Ref<Integer>();
	public Ref<Integer>	 g1	        = new Ref<Integer>();
	public Ref<Integer>	 b1	        = new Ref<Integer>();
	public Ref<Integer>	 r2	        = new Ref<Integer>();
	public Ref<Integer>	 g2	        = new Ref<Integer>();
	public Ref<Integer>	 b2	        = new Ref<Integer>();
	public int	         im;
	public int	         white;
	public int	         h;
	public Array<Object>	corners	= new Array<Object>();
	public int	         x1;
	public int	         x2;
	public int			 r;
	public int	         g;
	public int	         b;
}
