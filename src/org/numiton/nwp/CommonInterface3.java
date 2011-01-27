/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CommonInterface3.java,v 1.1 2008/09/19 09:44:35 numiton Exp $
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

import com.numiton.array.Array;

public interface CommonInterface3 {
	public void akismet_init();

	public Object akismet_nonce_field(String action);

	public String number_format_i18n(double number, int decimals);

	public void akismet_config_page();

	public void akismet_conf();

	public String akismet_verify_key(String key);

	public void akismet_warning();

	public Object akismet_http_post(String request, String host, String path, int port);

	public Array<Object> akismet_auto_check_comment(Array<Object> comment);

	public void akismet_delete_old();

	public void akismet_submit_nonspam_comment(int comment_id);

	public void akismet_submit_spam_comment(int comment_id);

	public int akismet_spam_count(String type);

	public Array<Object> akismet_spam_comments(String type, int page, int per_page);

	public Array<Object> akismet_spam_totals();

	public void akismet_manage_page();

	public void akismet_caught();

	public void akismet_stats();

	public void akismet_rightnow();

	public String akismet_recheck_button(String page);

	public void akismet_check_for_spam_button(Object comment_status);

	public void akismet_recheck_queue();

	public Object akismet_check_db_comment(int id);

	public int akismet_kill_proxy_check(Object option);

	public void widget_akismet(Array<Object> args);

	public void widget_akismet_style();

	public void widget_akismet_control();

	public void widget_akismet_register();

	public void akismet_counter();
}
