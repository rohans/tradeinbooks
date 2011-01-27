/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: Wp_comments_postPage.java,v 1.3 2008/10/14 13:15:49 numiton Exp $
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
package org.numiton.nwp;

import static com.numiton.VarHandling.*;
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_includes.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.Network;
import com.numiton.System;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;

@Controller
@Scope("request")
public class Wp_comments_postPage extends NumitonController {
	protected static final Logger	LOG	= Logger.getLogger(Wp_comments_postPage.class.getName());

	@Override
	@RequestMapping("/wp-comments-post.php")
	public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse) throws IOException {
		return super.execute(javaRequest, javaResponse);
	}

	@Override
	public String getViewName() {
		return "wp_comments_post";
	}

	public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {

		/* Start of block */
		super.startBlock("__wp_comments_post_block1");
		gVars.webEnv = webEnv;
		if (!equal("POST", gVars.webEnv.getRequestMethod())) {
			Network.header(gVars.webEnv, "Allow: POST");
			Network.header(gVars.webEnv, "HTTP/1.1 405 Method Not Allowed");
			Network.header(gVars.webEnv, "Content-Type: text/plain");
			System.exit();
		}
		
		require( /* Condensed dynamic construct: 237595 */gVars, gConsts, Wp_configPage.class);
		getIncluded(FunctionsPage.class, gVars, gConsts).nocache_headers();
		gVars.comment_post_ID = intval(gVars.webEnv._POST.getValue("comment_post_ID"));
		StdClass status = (StdClass) gVars.wpdb.get_row("SELECT post_status, comment_status FROM " + gVars.wpdb.posts + " WHERE ID = \'" + gVars.comment_post_ID + "\'");
		
		if (empty(StdClass.getValue(status, "comment_status"))) {
			getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_id_not_found", gVars.comment_post_ID);
			System.exit();
		}
		else
			if (!getIncluded(Comment_templatePage.class, gVars, gConsts).comments_open(gVars.comment_post_ID)) {
				getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_closed", gVars.comment_post_ID);
				getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__(
				        "Sorry, comments are closed for this item.", "default"), "");
			}
			else
				if (Array.in_array(StdClass.getValue(status, "post_status"), new Array<Object>(new ArrayEntry<Object>("draft"), new ArrayEntry<Object>("pending")))) {
					getIncluded(PluginPage.class, gVars, gConsts).do_action("comment_on_draft", gVars.comment_post_ID);
					System.exit();
				}
		
		gVars.comment_author = Strings.trim(Strings.strip_tags(strval(gVars.webEnv._POST.getValue("author"))));
		gVars.comment_author_email = Strings.trim(strval(gVars.webEnv._POST.getValue("email")));
		gVars.comment_author_url = Strings.trim(strval(gVars.webEnv._POST.getValue("url")));
		gVars.comment_content = Strings.trim(strval(gVars.webEnv._POST.getValue("comment")));
		
		// If the user is logged in
		gVars.user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();
		
		if (booleanval(((WP_User) gVars.user).getID())) {
			gVars.comment_author = gVars.wpdb.escape(((WP_User) gVars.user).getDisplay_name());
			gVars.comment_author_email = gVars.wpdb.escape(((WP_User) gVars.user).getUser_email());
			gVars.comment_author_url = gVars.wpdb.escape(((WP_User) gVars.user).getUser_url());
			if (getIncluded(CapabilitiesPage.class, gVars, gConsts).current_user_can("unfiltered_html"))
			/*
			 * set up the filters set up the filters
			 */
			{
				if (!equal(getIncluded(PluggablePage.class, gVars, gConsts).wp_create_nonce("unfiltered-html-comment_" + strval(gVars.comment_post_ID)), gVars.webEnv._POST
				        .getValue("_wp_unfiltered_html_comment"))) {
					getIncluded(KsesPage.class, gVars, gConsts).kses_remove_filters(); // start with a clean slate
					getIncluded(KsesPage.class, gVars, gConsts).kses_init_filters(); // set up the filters
				}
			}
		}
		else {
			if (booleanval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("comment_registration"))) {
				getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__(
				        "Sorry, you must be logged in to post a comment.", "default"), "");
			}
		}
		gVars.comment_type = "";
		if (booleanval(((FunctionsPage) getIncluded(FunctionsPage.class, gVars, gConsts)).get_option("require_name_email")) && !booleanval(((WP_User) gVars.user).getID())) {
			if (6 > Strings.strlen(gVars.comment_author_email) || equal("", gVars.comment_author)) {
				getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__(
				        "Error: please fill the required fields (name, email).", "default"), "");
			}
			else
				if (!getIncluded(FormattingPage.class, gVars, gConsts).is_email(gVars.comment_author_email)) {
					getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__(
					        "Error: please enter a valid email address.", "default"), "");
				}
		}
		
		if (equal("", gVars.comment_content)) {
			getIncluded(FunctionsPage.class, gVars, gConsts).wp_die(getIncluded(L10nPage.class, gVars, gConsts).__("Error: please type a comment.",
			        "default"), "");
		}
		
		gVars.commentdata = Array.compact(new ArrayEntry("comment_post_ID", gVars.comment_post_ID), new ArrayEntry("comment_author", gVars.comment_author), new ArrayEntry("comment_author_email",
		        gVars.comment_author_email), new ArrayEntry("comment_author_url", gVars.comment_author_url), new ArrayEntry("comment_content", gVars.comment_content), new ArrayEntry("comment_type",
		        gVars.comment_type), new ArrayEntry("user_ID", gVars.user_ID));
		
		gVars.comment_id = getIncluded(CommentPage.class, gVars, gConsts).wp_new_comment(gVars.commentdata);
		
		gVars.comment = (StdClass) getIncluded(CommentPage.class, gVars, gConsts).get_comment(gVars.comment_id, gConsts.getOBJECT());
		
		if (!booleanval(((WP_User) gVars.user).getID())) {
			Network.setcookie(gVars.webEnv, "comment_author_" + gConsts.getCOOKIEHASH(), strval(StdClass.getValue(gVars.comment, "comment_author")), DateTime.time() + 30000000, gConsts.getCOOKIEPATH(),
			        gConsts.getCOOKIE_DOMAIN());
			Network.setcookie(gVars.webEnv, "comment_author_email_" + gConsts.getCOOKIEHASH(), strval(StdClass.getValue(gVars.comment, "comment_author_email")), DateTime.time() + 30000000, gConsts
			        .getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
			Network.setcookie(gVars.webEnv, "comment_author_url_" + gConsts.getCOOKIEHASH(), getIncluded(FormattingPage.class, gVars, gConsts).clean_url(
			        strval(StdClass.getValue(gVars.comment, "comment_author_url")), null, "display"), DateTime.time() + 30000000, gConsts.getCOOKIEPATH(), gConsts.getCOOKIE_DOMAIN());
		}
		
		gVars.location = (empty(gVars.webEnv._POST.getValue("redirect_to")) ? getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(gVars.comment_post_ID,
		        false) : gVars.webEnv._POST.getValue("redirect_to"))
		        + "#comment-" + strval(gVars.comment_id);
		gVars.location = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("comment_post_redirect", gVars.location, gVars.comment));
		
		getIncluded(PluggablePage.class, gVars, gConsts).wp_redirect(gVars.location, 302);
		return DEFAULT_VAL;
	}
}
