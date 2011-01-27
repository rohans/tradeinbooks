/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: CapabilitiesPage.java,v 1.3 2008/10/14 13:15:48 numiton Exp $
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
import static com.numiton.generic.PhpWeb.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.FunctionHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;


@Controller
@Scope("request")
public class CapabilitiesPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(CapabilitiesPage.class.getName());

    @Override
    @RequestMapping("/wp-includes/capabilities.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/capabilities";
    }

 // Map meta capabilities to primitive capabilities.
    public Array<Object> map_meta_cap(Object cap, int user_id, Object... vargs) {
        Array<Object> args = new Array<Object>();
        Array<Object> caps = new Array<Object>();
        StdClass author_data;
        StdClass post = null;
        StdClass post_author_data;
        StdClass page = null;
        StdClass page_author_data;

        // Modified by Numiton
        args = FunctionHandling.func_get_args(vargs);
        caps = new Array<Object>();
        
        {
            int javaSwitchSelector39 = 0;

            if (equal(cap, "delete_user")) {
                javaSwitchSelector39 = 1;
            }

            if (equal(cap, "edit_user")) {
                javaSwitchSelector39 = 2;
            }

            if (equal(cap, "delete_post")) {
                javaSwitchSelector39 = 3;
            }

            if (equal(cap, "delete_page")) {
                javaSwitchSelector39 = 4;
            }

            if (equal(cap, "edit_post")) {
                javaSwitchSelector39 = 5;
            }

            if (equal(cap, "edit_page")) {
                javaSwitchSelector39 = 6;
            }

            if (equal(cap, "read_post")) {
                javaSwitchSelector39 = 7;
            }

            if (equal(cap, "read_page")) {
                javaSwitchSelector39 = 8;
            }

            switch (javaSwitchSelector39) {
            case 1: {
                caps.putValue("delete_users");

                break;
            }

            case 2: {
                if (!isset(args.getValue(0)) || !equal(user_id, args.getValue(0))) {
                    caps.putValue("edit_users");
                }

                break;
            }

            case 3: {
                author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
                
        		//echo "post ID: {$args[0]}<br />";
                post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(args.getValue(0), gConsts.getOBJECT(), "raw");

                if (equal("page", StdClass.getValue(post, "post_type"))) {
                    args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>("delete_page"), new ArrayEntry<Object>(user_id)), args);

                    return (Array<Object>) FunctionHandling.call_user_func_array(new Callback("map_meta_cap", this), args);
                }

                post_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(post, "post_author")));
                //echo "current user id : $user_id, post author id: " . $post_author_data->ID . "<br />";
        		// If the user is the author...
                if (equal(user_id, StdClass.getValue(post_author_data, "ID"))) {
        			// If the post is published...
                    if (equal(StdClass.getValue(post, "post_status"), "publish")) {
                        caps.putValue("delete_published_posts");
                    } else {
        				// If the post is draft...
                        caps.putValue("delete_posts");
                    }
                } else {
        			// The user is trying to edit someone else's post.
                    caps.putValue("delete_others_posts");
        			// The post is published, extra cap required.
                    if (equal(StdClass.getValue(post, "post_status"), "publish")) {
                        caps.putValue("delete_published_posts");
                    } else if (equal(StdClass.getValue(post, "post_status"), "private")) {
                        caps.putValue("delete_private_posts");
                    }
                }

                break;
            }

            case 4: {
                author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
        		//echo "post ID: {$args[0]}<br />";
                page = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page(intval(args.getValue(0)), gConsts.getOBJECT(), "raw");
                page_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(page, "post_author")));
                //echo "current user id : $user_id, page author id: " . $page_author_data->ID . "<br />";
        		// If the user is the author...
                if (equal(user_id, StdClass.getValue(page_author_data, "ID"))) {
                	// If the page is published...
                    if (equal(StdClass.getValue(page, "post_status"), "publish")) {
                        caps.putValue("delete_published_pages");
                    } else {
                    	// If the page is draft...
                        caps.putValue("delete_pages");
                    }
                } else {
                	// The user is trying to edit someone else's page.
                    caps.putValue("delete_others_pages");
                    
                    // The page is published, extra cap required.
                    if (equal(StdClass.getValue(page, "post_status"), "publish")) {
                        caps.putValue("delete_published_pages");
                    } else if (equal(StdClass.getValue(page, "post_status"), "private")) {
                        caps.putValue("delete_private_pages");
                    }
                }

                break;
                // edit_post breaks down to edit_posts, edit_published_posts, or
        		// edit_others_posts
            }

            case 5: {
                author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
                //echo "post ID: {$args[0]}<br />";
                post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(args.getValue(0), gConsts.getOBJECT(), "raw");

                if (equal("page", StdClass.getValue(post, "post_type"))) {
                    args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>("edit_page"), new ArrayEntry<Object>(user_id)), args);

                    return (Array<Object>) FunctionHandling.call_user_func_array(new Callback("map_meta_cap", this), args);
                }

                post_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(post, "post_author")));

                //echo "current user id : $user_id, post author id: " . $post_author_data->ID . "<br />";
        		// If the user is the author...
                if (equal(user_id, StdClass.getValue(post_author_data, "ID"))) {
                	// If the post is published...
                    if (equal(StdClass.getValue(post, "post_status"), "publish")) {
                        caps.putValue("edit_published_posts");
                    } else {
                    	// If the post is draft...
                        caps.putValue("edit_posts");
                    }
                } else {
                	// The user is trying to edit someone else's post.
                    caps.putValue("edit_others_posts");
                    // The post is published, extra cap required.
                    if (equal(StdClass.getValue(post, "post_status"), "publish")) {
                        caps.putValue("edit_published_posts");
                    } else if (equal(StdClass.getValue(post, "post_status"), "private")) {
                        caps.putValue("edit_private_posts");
                    }
                }

                break;
            }

            case 6: {
                author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
                //echo "post ID: {$args[0]}<br />";
                page = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page(intval(args.getValue(0)), gConsts.getOBJECT(), "raw");
                page_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(page, "post_author")));
                //echo "current user id : $user_id, page author id: " . $page_author_data->ID . "<br />";
        		// If the user is the author...
                if (equal(user_id, StdClass.getValue(page_author_data, "ID"))) {
                	// If the page is published...
                    if (equal(StdClass.getValue(page, "post_status"), "publish")) {
                        caps.putValue("edit_published_pages");
                    } else {
                    	// If the page is draft...
                        caps.putValue("edit_pages");
                    }
                } else {
                	// The user is trying to edit someone else's page.
                    caps.putValue("edit_others_pages");
                    // The page is published, extra cap required.
                    if (equal(StdClass.getValue(page, "post_status"), "publish")) {
                        caps.putValue("edit_published_pages");
                    } else if (equal(StdClass.getValue(page, "post_status"), "private")) {
                        caps.putValue("edit_private_pages");
                    }
                }

                break;
            }

            case 7: {
                post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(args.getValue(0), gConsts.getOBJECT(), "raw");

                if (equal("page", StdClass.getValue(post, "post_type"))) {
                    args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>("read_page"), new ArrayEntry<Object>(user_id)), args);

                    return (Array<Object>) FunctionHandling.call_user_func_array(new Callback("map_meta_cap", this), args);
                }

                if (!equal("private", StdClass.getValue(post, "post_status"))) {
                    caps.putValue("read");

                    break;
                }

                author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
                post_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(post, "post_author")));

                if (equal(user_id, StdClass.getValue(post_author_data, "ID"))) {
                    caps.putValue("read");
                } else {
                    caps.putValue("read_private_posts");
                }

                break;
            }

            case 8: {
                page = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_page(intval(args.getValue(0)), gConsts.getOBJECT(), "raw");

                if (!equal("private", StdClass.getValue(page, "post_status"))) {
                    caps.putValue("read");

                    break;
                }

                author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
                page_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(page, "post_author")));

                if (equal(user_id, StdClass.getValue(page_author_data, "ID"))) {
                    caps.putValue("read");
                } else {
                    caps.putValue("read_private_pages");
                }

                break;
            }

            default:
            	// If no meta caps match, return the original cap.
                caps.putValue(cap);
            }
        }

        return caps;
    }

    /**
     * Capability checking wrapper around the global $current_user object.
     *
     */
    public boolean current_user_can(String capability, Object... vargs) {
        WP_User current_user;
        Array<Object> args;
        current_user = getIncluded(PluggablePage.class, gVars, gConsts).wp_get_current_user();

        if (empty(current_user)) {
            return false;
        }

        // Modified by Numiton
        args = FunctionHandling.func_get_args(vargs);
        args = Array.array_merge(new Array<Object>(new ArrayEntry<Object>(capability)), args);

        return booleanval(FunctionHandling.call_user_func_array(new Callback("has_cap", current_user), args));
    }

    /**
     * Convenience wrappers around $wp_roles.
     */
    public WP_Role get_role(String role) {
        if (!isset(gVars.wp_roles)) {
            gVars.wp_roles = new WP_Roles(gVars, gConsts);
        }

        return gVars.wp_roles.get_role(role);
    }

    public Object add_role(String role, String display_name, Array<Object> capabilities) {
        if (!isset(gVars.wp_roles)) {
            gVars.wp_roles = new WP_Roles(gVars, gConsts);
        }

        return gVars.wp_roles.add_role(role, display_name, capabilities);
    }

    public void remove_role(Object role) {
        if (!isset(gVars.wp_roles)) {
            gVars.wp_roles = new WP_Roles(gVars, gConsts);
        }

        gVars.wp_roles.remove_role(role);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;

        return DEFAULT_VAL;
    }
}
