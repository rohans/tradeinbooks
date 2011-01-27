/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: GlobalVars.java,v 1.2 2008/10/10 16:48:04 numiton Exp $
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

import org.apache.log4j.Logger;
import org.numiton.nwp.wp_admin.includes.PclZip;
import org.numiton.nwp.wp_admin.includes.WP_Filesystem;
import org.numiton.nwp.wp_includes.*;

import com.numiton.array.Array;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.generic.StdClass;

public class GlobalVars extends GlobalVariablesContainer {
	protected static final Logger	LOG	= Logger.getLogger(GlobalVars.class.getName());
	public wp_xmlrpc_server	      sharedwp_xmlrpc_server;
	public PclZip	              sharedPclZip;
	public GlobalConsts	          gConsts;

	public GlobalVars() {
	}

	public PclZip getSharedPclZip() {
		if (sharedPclZip == null) {
			sharedPclZip = new PclZip(this, gConsts, "");
		}
		return sharedPclZip;
	}

	public wp_xmlrpc_server getSharedwp_xmlrpc_server() {
		if (sharedwp_xmlrpc_server == null) {
			sharedwp_xmlrpc_server = new wp_xmlrpc_server(this, gConsts);
		}
		return sharedwp_xmlrpc_server;
	}
	
	public String	                  s;
	public wpdb	                      wpdb;
	public Object	                  id; // StdClass mixed with WP_Error, int
	public StdClass	                  comment;
	public Object	                  r; // StdClass mixed with WP_Error
	public String	                  cat_name;
	public Object	                  action;
	public Array<String>	          names	                = new Array<String>();
	public int	                  parent;
	public Array<Object>	          post_category	        = new Array<Object>();
	public String	          cat_id;
	public StdClass	                  category;
	public Object	          data;
	public Object	                  cat; // StdClass mixed with WP_Error
	public Object	                  link_cat;
	public Object	          tag; // StdClass mixed with WP_Error
	public Object	                  search;
	public Object	                  start;
	public String	          status;
	public String	                  mode;
	public Array<StdClass>	          comments	            = new Array<StdClass>();
	public Object	                  total;
	public String	                  key;
	public Object	                  value; /* Do not change type */
	public Object	                  user_id; // StdClass mixed with WP_Error
	public WP_User	                  user_object;
	public String	                  message;
	public Object	                  post_ID; // StdClass mixed with WP_Error, int TODO Split it
	public StdClass	                  post;
	public int	                  last;
	public StdClass	          last_user;
	public String	                  last_user_name;
	public Integer	                  ID;
	public Object	                  page; // int or String
	public WP_User	                  current_user;
	public int	                  post_id;
	public String	                  title;
	public boolean	                  editing;
	public String	                  parent_file;
	public String	                  page_hook;
	public String	                  plugin_page;
	public Object	                  user_identity;
	public int	                      wp_db_version;
	public Object	                  posts_per_page;
	public Object	                  what_to_show;
	public String	                  pagenow;
	public Array<Object>	          wp_importers = new Array<Object>();
	public String	                  submenu_file;
	public String	                  type;
	public String	                  post_title;
	public String	          content;
	public Integer	                  cat_ID;
	public String	                  sendback;
	public Array<Object>	          messages	            = new Array<Object>();
	public int	                  comment_id;
	public String	                  nonce_action;
	public String	                  location;
	public Array<Object>	          posts_columns	        = new Array<Object>();
	public Object	                  post_column_key;
	public String	                  _class;
	public Object	                  column_display_name;
	public String	                  bgcolor;
	public String	                  post_owner;
	public Object	                  column_name;
	public Object	                  t_time;
	public Object	                  h_time;
	public String	                  m_time;
	public Object	                  time;
	public Array<Object>	          left	                = new Array<Object>();
	public String	                  pending_phrase;
	public Object	                  heading;
	public Object	                  submit_text;
	public String	                  form;
	public String	                  redirect_to;
	public int	                      deleted;
	public Array	          status_links	        = new Array();
	public Array<Object>	          label	                = new Array<Object>();
	public Object	                  offset;
	public String	                  page_links;
	public String	                  form_action;
	public int	                      temp_ID;
	public String	                  form_extra;
	public Integer	                      user_ID;
	public String	                  stamp;
	public String	                  date;
	public int	                  last_id;
	public String	                  sample_permalink_html;
	public Array<Object>	          metadata	            = new Array<Object>();
	public Array<Object>	          authors	            = new Array<Object>();
	public String	                  referer;
	public int	                  pagenum;
	public Array<Object>	          args	                = new Array<Object>();
	public Array<StdClass>	                  categories;
	public Object	                  output;
	public StdClass	                  link;
	public Integer	                  link_id;
	public String	                  order_by;
	public StdClass	                  post_del;
	public int	                  post_id_del;
	public Array<Object>	          post_stati	        = new Array<Object>();
	public Object	                  post_status_label;
	public String	                  h2_search;
	public Object	                  h2_author;
	public StdClass	          author_user;
	public Array<Object>	          avail_post_stati	    = new Array<Object>();
	public StdClass	          num_posts;
	public Array<StdClass>	          posts; // Initialized by code
	public StdClass	          authordata;
	public Array	          post_ids	            = new Array();
	public WP_Query	                  wp_query;
	public Array<?>	                  tags;
	public Integer	                  tag_ID;
	public int	          count;
	public Object	                  post_listing_pageable;
	public Object	                  h2_noun;
	public String	                  h2_cat;
	public String	                  h2_tag;
	public String	                  h2_month;
	public String	                  arc_query;
	public Array<StdClass>	          arc_result	        = new Array<StdClass>();
	public int	                      month_count;
	public StdClass	                  arc_row;
	public String	                  _default;
	public WP_Locale	              wp_locale;
	public String	                  style;
	public Object	                  prefix; // StdClass mixed with WP_Error
	public Array<Object>	     wp_registered_widgets	       = new Array<Object>();
	public Array<Object>	     wp_registered_widget_controls	= new Array<Object>();
	public Array<Object>	     wp_file_descriptions	       = new Array<Object>();
	public WP_Filesystem	             wp_filesystem;
	public String	             body_id;
	public WP_Query	         wp_the_query;
	public String	             tab;
	public Array<Object>	     post_mime_types	           = new Array<Object>();
	public WP_Rewrite	         wp_rewrite;
	public Array<Object>	     menu	                       = new Array<Object>();
	public Array<Object>	     admin_page_hooks	           = new Array<Object>();
	public Array<Object>	     _wp_real_parent_file	       = new Array<Object>();
	public Array<Object>	     _wp_submenu_nopriv	           = new Array<Object>();
	public Array<Object>	     submenu	                   = new Array<Object>();
	public Array<Object>	     _wp_menu_nopriv	           = new Array<Object>();
	public Object	             wp_queries;
	public WP_Roles	     wp_roles;
	public Array<Object>	     wp_broken_themes	           = new Array<Object>();
	public String	             wp_version;
	public Array<Object>	     plugin_data	               = new Array<Object>();
	public Integer	                 debug;
	public Object	             sidebar;
	public Array<Object>	     sidebars_widgets	           = new Array<Object>();
	public String	             edit_widget;
	public String	             sentence;
	public StdClass	             ct;
	public int	                 step;
	public Object	             result; // StdClass mixed with WP_Error
	public String	             this_file;
	public String	     file;
	public String	     url;
	public String	             opml;
	public int	                 i;
	public Array<Object>	     urls	                       = new Array<Object>();
	public Array<Object>	     descriptions	               = new Array<Object>();
	public Array<Object>	     feeds	                       = new Array<Object>();
	public Integer	                 attachment_id;
	public Object	             errors; // StdClass mixed with WP_Error
	public Array<Object>	     item	                       = new Array<Object>();
	public Object	             index;
	public Object	             selected;
	public boolean	             is_apache;
	public String	             goback;
	public Integer	                 p;
	public Integer	                 page_id;
	public Array<Object>	     newmeta	                   = new Array<Object>();
	public Array<?>	     plugins	                   = new Array();
	public String	             real_file;
	public String	             newcontent;
	public int	                 f;
	public Object	             error; // StdClass mixed with WP_Error
	public Object	             plugin_file;
	public String	             plugin;
	public String	             author;
	public int	                 handle;
	public String	             line;
	public Array<?>	     themes	                       = new Array();
	public String	             theme;
	public String	             description;
	public String	             theme_name;
	public Object	             template;
	public String	             query_string;
	public Array<Object>	     matches	                   = new Array<Object>();
	public String	             redirect;
	public WP_User	             profileuser;
	public String	                 color;
	public Array<Object>	     _wp_admin_css_colors	       = new Array<Object>();
	public String	             name;
	public String	             role;
	public Object	             user; // StdClass mixed with WP_Error
	public Object	             val;
	public String	             var;
	public Array<Object>	     wp_registered_sidebars; /* Initialized in code */
	public Array<Object>	     keys	                       = new Array<Object>();
	public boolean	             http_post;
	public String	             post_type;
	public WP	                 wp;
	public Integer	             blog_id;
	public int	                 always_authenticate;
	public Array<Object>	     entry	                       = new Array<Object>();
	public String	             path;
	public int	                 comment_post_ID;
	public String	             comment_author;
	public String	             comment_author_email;
	public String	             comment_author_url;
	public Object	             comment_content;
	public String	             comment_type;
	public Array<Object>	     commentdata	               = new Array<Object>();
	public String	             table_prefix;
	public Object	             commenter;
	public Object	             req;
	public Object	             withcomments;
	public String	             v;
	public boolean	             is_IIS;
	public Object	             HTTP_SERVER_VARS;
	public Object	             HTTP_ENV_VARS;
	public Integer	                 more;
	public Array<String>	     cache_lastcommentmodified	   = new Array<String>();
	public StdClass	             comment_post;
	public boolean	             is_winIE;
	public boolean	             is_macIE;
	public Array<String>	     wp_smiliessearch = new Array<String>();
	public Array<String>	     wp_smiliesreplace = new Array<String>();
	public String	             post_default_title;
	public String	             day;
	public String	             previousday;
	public Array<Object>	     wp_header_to_desc	           = new Array<Object>();
	public String	             m;
	public Object	             monthnum;
	public Object	             year;
	public Array<Object>	     allowedtags	               = new Array<Object>();
	public String	             language;
	public String	             strings;
	public Array<Object>	     config	                       = new Array<Object>();
	public String	             locale;
	public Integer	             paged;
	public String	             wp_default_secret_key;
	public Array<Object>	     wp_filter	                   = new Array<Object>();
	public Array<String>	     pages	                       = new Array<String>();
	public int	                 multipage;
	public Object	             preview;
	public int	                 numpages;
	public Array<Object>	     cache_lastpostdate	           = new Array<Object>();
	public Array<Object>	     wp_taxonomies	               = new Array<Object>();
	public StdClass	     userdata;
	public String	             user_login;
	public String	             user_email;
	public String	             PHP_SELF;
	public String	             post_status;
	public Object term_taxonomy_id; // Added because of extract
	public Object wp_did_template_redirect;
}
