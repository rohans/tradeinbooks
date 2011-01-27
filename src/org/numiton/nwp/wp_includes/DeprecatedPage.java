/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: DeprecatedPage.java,v 1.4 2008/10/14 13:15:49 numiton Exp $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.numiton.nwp.NumitonController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.DateTime;
import com.numiton.SourceCodeInfo;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class DeprecatedPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(DeprecatedPage.class.getName());
    public int currentcat;
    public int previouscat;
    
    public Object tableposts;
    public Object tableusers;
    public Object tablecategories;
    public Object tablepost2cat;
    public Object tablecomments;
    public Object tablelinks;
    public Object tablelinkcategories;
    public Object tableoptions;
    public Object tablepostmeta;

    @Override
    @RequestMapping("/wp-includes/deprecated.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/deprecated";
    }

    /*
     * Deprecated functions come here to die.
     */

    /**
     * get_postdata() - Entire Post data
     *
     * @since 0.71
     * @deprecated Use get_post()
     * @see get_post()
     *
     * @param int $postid
     * @return array
     */
    public Array<Object> get_postdata(Object postid) {
        StdClass post;
        Array<Object> postdata = new Array<Object>();
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_post()");
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(postid, gConsts.getOBJECT(), "raw");
        postdata = new Array<Object>(
                new ArrayEntry<Object>("ID", StdClass.getValue(post, "ID")),
                new ArrayEntry<Object>("Author_ID", StdClass.getValue(post, "post_author")),
                new ArrayEntry<Object>("Date", StdClass.getValue(post, "post_date")),
                new ArrayEntry<Object>("Content", StdClass.getValue(post, "post_content")),
                new ArrayEntry<Object>("Excerpt", StdClass.getValue(post, "post_excerpt")),
                new ArrayEntry<Object>("Title", StdClass.getValue(post, "post_title")),
                new ArrayEntry<Object>("Category", StdClass.getValue(post, "post_category")),
                new ArrayEntry<Object>("post_status", StdClass.getValue(post, "post_status")),
                new ArrayEntry<Object>("comment_status", StdClass.getValue(post, "comment_status")),
                new ArrayEntry<Object>("ping_status", StdClass.getValue(post, "ping_status")),
                new ArrayEntry<Object>("post_password", StdClass.getValue(post, "post_password")),
                new ArrayEntry<Object>("to_ping", StdClass.getValue(post, "to_ping")),
                new ArrayEntry<Object>("pinged", StdClass.getValue(post, "pinged")),
                new ArrayEntry<Object>("post_type", StdClass.getValue(post, "post_type")),
                new ArrayEntry<Object>("post_name", StdClass.getValue(post, "post_name")));

        return postdata;
    }

    /**
     * start_wp() - Sets up the WordPress Loop
     * @since 1.0.1
     * @deprecated Since 1.5 -{@link http://codex.wordpress.org/The_Loop Use new WordPress Loop}
     */
    public void start_wp() {
        getIncluded(FunctionsPage.class, gVars, gConsts)
            ._deprecated_function(SourceCodeInfo.getCurrentFunction(), "1.5", getIncluded(L10nPage.class, gVars, gConsts).__("new nWordPress Loop", "default"));
        
    	// Since the old style loop is being used, advance the query iterator here.
        gVars.wp_query.next_post();
        
        getIncluded(QueryPage.class, gVars, gConsts).setup_postdata(gVars.post);
    }

    /**
     * the_category_ID() - Return or Print Category ID
     * @since 0.71
     * @deprecated use get_the_category()
     * @see get_the_category()
     * @param bool $echo
     * @return null|int
     */
    public Object the_category_ID(Object echo) {
        Array<StdClass> categories;
        Object cat = null;
        
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_the_category()");
        
    	// Grab the first cat in the list.
        categories = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(false));
        cat = categories.getValue(0).fields.getValue("term_id");

        if (booleanval(echo)) {
            echo(gVars.webEnv, cat);
        }

        return cat;
    }

    /**
     * the_category_head() - Print category with optional text before and
     * after
     * @since 0.71
     * @deprecated use get_the_category_by_ID()
     * @see get_the_category_by_ID()
     * @param string $before
     * @param string $after
     */
    public void the_category_head(String before, String after) {
        Array<Object> categories = new Array<Object>();
        
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_the_category_by_ID()");
        
    	// Grab the first cat in the list.
        categories = getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category(intval(false));
        currentcat = intval(((StdClass) categories.getValue(0)).fields.getValue("category_id"));

        if (!equal(currentcat, previouscat)) {
            echo(gVars.webEnv, before);
            echo(gVars.webEnv, getIncluded(Category_templatePage.class, gVars, gConsts).get_the_category_by_ID(currentcat));
            echo(gVars.webEnv, after);
            previouscat = currentcat;
        }
    }

    /**
     * previous_post() - Prints link to the previous post
     * @since 1.5
     * @deprecated Use previous_post_link()
     * @see previous_post_link()
     * @param string $format
     * @param string $previous
     * @param string $title
     * @param string $in_same_cat
     * @param int $limitprev
     * @param string $excluded_categories
     */
    public void previous_post(String format, String previous, String title, String in_same_cat, int limitprev, String excluded_categories) {
        StdClass post;
        String string = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "previous_post_link()");

        if (empty(in_same_cat) || equal("no", in_same_cat)) {
            in_same_cat = strval(false);
        } else {
            in_same_cat = strval(true);
        }

        post = getIncluded(Link_templatePage.class, gVars, gConsts).get_previous_post(booleanval(in_same_cat), excluded_categories);

        if (!booleanval(post)) {
            return;
        }

        string = "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(post, "ID"), false) + "\">" + previous;

        if (equal("yes", title)) {
            string = string + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(post, "post_title"), post);
        }

        string = string + "</a>";
        format = Strings.str_replace("%", string, format);
        echo(gVars.webEnv, format);
    }

    /**
     * next_post() - Prints link to the next post
     * @since 0.71
     * @deprecated Use next_post_link()
     * @see next_post_link()
     * @param string $format
     * @param string $previous
     * @param string $title
     * @param string $in_same_cat
     * @param int $limitprev
     * @param string $excluded_categories
     */
    public void next_post(String format, String next, String title, boolean in_same_cat, Object limitnext, String excluded_categories) {
        StdClass post;
        String string = null;
        Object nextpost = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "next_post_link()");

        if (empty(in_same_cat) || equal("no", in_same_cat)) {
            in_same_cat = false;
        } else {
            in_same_cat = true;
        }

        post = getIncluded(Link_templatePage.class, gVars, gConsts).get_next_post(in_same_cat, excluded_categories);

        if (!booleanval(post)) {
            return;
        }

        string = "<a href=\"" + getIncluded(Link_templatePage.class, gVars, gConsts).get_permalink(StdClass.getValue(post, "ID"), false) + "\">" + next;

        if (equal("yes", title)) {
            string = string + getIncluded(PluginPage.class, gVars, gConsts).apply_filters("the_title", StdClass.getValue(post, "post_title"), nextpost);
        }

        string = string + "</a>";
        format = Strings.str_replace("%", string, format);
        echo(gVars.webEnv, format);
    }

    /**
     * user_can_create_post() - Whether user can create a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $blog_id Not Used
     * @param int $category_id Not Used
     * @return bool
     */
    public boolean user_can_create_post(int user_id, int blog_id, int category_id) {
        StdClass author_data;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");
        author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);

        return intval(StdClass.getValue(author_data, "user_level")) > 1;
    }

    /**
     * user_can_create_draft() - Whether user can create a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $blog_id Not Used
     * @param int $category_id Not Used
     * @return bool
     */
    public boolean user_can_create_draft(int user_id, int blog_id, int category_id) {
        StdClass author_data;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");
        author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);

        return intval(StdClass.getValue(author_data, "user_level")) >= 1;
    }

    /**
     * user_can_edit_post() - Whether user can edit a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $post_id
     * @param int $blog_id Not Used
     * @return bool
     */
    public boolean user_can_edit_post(int user_id, int post_id, int blog_id) {
        StdClass author_data;
        StdClass post = null;
        StdClass post_author_data;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0", "current_user_can()");
        author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
        post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(post_id, gConsts.getOBJECT(), "raw");
        post_author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(intval(StdClass.getValue(post, "post_author")));

        if ((equal(user_id, StdClass.getValue(post_author_data, "ID")) && !(equal(StdClass.getValue(post, "post_status"), "publish") && (intval(StdClass.getValue(author_data, "user_level")) < 2))) ||
                (intval(StdClass.getValue(author_data, "user_level")) > intval(StdClass.getValue(post_author_data, "user_level"))) || (intval(StdClass.getValue(author_data, "user_level")) >= 10)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * user_can_delete_post() - Whether user can delete a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $post_id
     * @param int $blog_id Not Used
     * @return bool
     */
    public boolean user_can_delete_post(int user_id, int post_id, int blog_id) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");

    	// right now if one can edit, one can delete
        return user_can_edit_post(user_id, post_id, blog_id);
    }

    /**
     * user_can_set_post_date() - Whether user can set new posts' dates
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $blog_id Not Used
     * @param int $category_id Not Used
     * @return bool
     */
    public boolean user_can_set_post_date(int user_id, int blog_id, int category_id) {
        StdClass author_data;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");
        author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);

        return (intval(StdClass.getValue(author_data, "user_level")) > 4) && user_can_create_post(user_id, blog_id, category_id);
    }

    /* returns true if $user_id can edit $post_id's date */
    /**
     * user_can_edit_post_date() - Whether user can delete a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $post_id
     * @param int $blog_id Not Used
     * @return bool
     */
    public boolean user_can_edit_post_date(int user_id, int post_id, int blog_id) {
        StdClass author_data;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");
        author_data = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);

        return (intval(StdClass.getValue(author_data, "user_level")) > 4) && user_can_edit_post(user_id, post_id, blog_id);
    }

    /* returns true if $user_id can edit $post_id's comments */
    /**
     * user_can_edit_post_comments() - Whether user can delete a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $post_id
     * @param int $blog_id Not Used
     * @return bool
     */
    public boolean user_can_edit_post_comments(int user_id, int post_id, int blog_id) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");

    	// right now if one can edit a post, one can edit comments made on it
        return user_can_edit_post(user_id, post_id, blog_id);
    }

    /**
     * returns true if $user_id can delete $post_id's comments *
     * user_can_delete_post_comments() - Whether user can delete a post
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $post_id
     * @param int $blog_id Not Used
     * @return bool
     */
    public boolean user_can_delete_post_comments(int user_id, int post_id, int blog_id) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");

    	// right now if one can edit comments, one can delete comments
        return user_can_edit_post_comments(user_id, post_id, blog_id);
    }

    /**
     * user_can_edit_user() - Can user can edit other user
     * @since 1.5
     * @deprecated Use current_user_can()
     * @see current_user_can()
     * @param int $user_id
     * @param int $other_user
     * @return bool
     */
    public boolean user_can_edit_user(int user_id, int other_user) {
        StdClass user;
        StdClass other;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "current_user_can()");
        user = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(user_id);
        other = getIncluded(PluggablePage.class, gVars, gConsts).get_userdata(other_user);

        if ((intval(StdClass.getValue(user, "user_level")) > intval(StdClass.getValue(other, "user_level"))) || (intval(StdClass.getValue(user, "user_level")) > 8) ||
                equal(StdClass.getValue(user, "ID"), StdClass.getValue(other, "ID"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get_linksbyname() - Gets the links associated with category $cat_name.
     *
     * @since 0.71
     * @deprecated Use get_links()
     * @see get_links()
     *
     * @param string 	$cat_name 	Optional. The category name to use. If no match is found uses all.
     * @param string 	$before 	Optional. The html to output before the link.
     * @param string 	$after 		Optional. The html to output after the link.
     * @param string 	$between 	Optional. The html to output between the link/image and it's description. Not used if no image or $show_images is true.
     * @param bool 		$show_images Optional. Whether to show images (if defined).
     * @param string 	$orderby	Optional. The order to output the links. E.g. 'id', 'name', 'url', 'description' or 'rating'. Or maybe owner.
     *		If you start the name with an underscore the order will be reversed. You can also specify 'rand' as the order which will return links in a
     *		random order.
     * @param bool 		$show_description Optional. Whether to show the description if show_images=false/not defined.
     * @param bool 		$show_rating Optional. Show rating stars/chars.
     * @param int 		$limit		Optional. Limit to X entries. If not specified, all entries are shown.
     * @param int 		$show_updated Optional. Whether to show last updated timestamp
     */
    public void get_linksbyname(Object cat_name, Object before, Object after, Object between, Object show_images, Object orderby, Object show_description, boolean show_rating, Object limit,
        Object show_updated) {
        int cat_id = 0;
        StdClass cat = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_links()");
        cat_id = -1;
        cat = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("name", cat_name, "link_category", gConsts.getOBJECT(), "raw");

        if (booleanval(cat)) {
            cat_id = intval(StdClass.getValue(cat, "term_id"));
        }

        get_links(strval(cat_id), strval(before), strval(after), strval(between), show_images, strval(orderby), show_description, show_rating, intval(limit), intval(show_updated), true);
    }

    /**
     * wp_get_linksbyname() - Gets the links associated with the named
     * category.
     * @since 1.0.1
     * @deprecated Use wp_get_links()
     * @see wp_get_links()
     * @param string $category The category to use.
     * @param string $args
     * @return bool|null
     */
    public boolean wp_get_linksbyname(String category, String args) {
        StdClass cat = null;
        Object cat_id = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_get_links()");
        cat = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("name", category, "link_category", gConsts.getOBJECT(), "raw");

        if (!booleanval(cat)) {
            return false;
        }

        cat_id = StdClass.getValue(cat, "term_id");
        args = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("category", cat_id, args);
        wp_get_links(args);

        return false;
    }

    /**
     * get_linkobjectsbyname() - Gets an array of link objects associated with category $cat_name.
     *
     * <code>
     *	$links = get_linkobjectsbyname('fred');
     *	foreach ($links as $link) {
     * 		echo '<li>'.$link->link_name.'</li>';
     *	}
     * </code>
     *
     * @since 1.0.1
     * @deprecated Use get_linkobjects()
     * @see get_linkobjects()
     *
     * @param string $cat_name The category name to use. If no match is found uses all.
     * @param string $orderby The order to output the links. E.g. 'id', 'name', 'url', 'description', or 'rating'.
     *		Or maybe owner. If you start the name with an underscore the order will be reversed. You can also
     *		specify 'rand' as the order which will return links in a random order.
     * @param int $limit Limit to X entries. If not specified, all entries are shown.
     * @return unknown
     */
    public Array<Object> get_linkobjectsbyname(String cat_name, String orderby, int limit) {
        int cat_id = 0;
        StdClass cat = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_linkobjects()");
        cat_id = -1;
        cat = (StdClass) getIncluded(TaxonomyPage.class, gVars, gConsts).get_term_by("name", cat_name, "link_category", gConsts.getOBJECT(), "raw");

        if (booleanval(cat)) {
            cat_id = intval(StdClass.getValue(cat, "term_id"));
        }

        return get_linkobjects(cat_id, orderby, limit);
    }

    /**
     * get_linkobjects() - Gets an array of link objects associated with category n.
     *
     * Usage:
     * <code>
     *	$links = get_linkobjects(1);
     *	if ($links) {
     *		foreach ($links as $link) {
     *			echo '<li>'.$link->link_name.'<br />'.$link->link_description.'</li>';
     *		}
     *	}
     * </code>
     *
     * Fields are:
     * <ol>
     *	<li>link_id</li>
     *	<li>link_url</li>
     *	<li>link_name</li>
     *	<li>link_image</li>
     *	<li>link_target</li>
     *	<li>link_category</li>
     *	<li>link_description</li>
     *	<li>link_visible</li>
     *	<li>link_owner</li>
     *	<li>link_rating</li>
     *	<li>link_updated</li>
     *	<li>link_rel</li>
     *	<li>link_notes</li>
     * </ol>
     *
     * @since 1.0.1
     * @deprecated Use get_bookmarks()
     * @see get_bookmarks()
     *
     * @param int $category The category to use. If no category supplied uses all
     * @param string $orderby the order to output the links. E.g. 'id', 'name', 'url',
     *		'description', or 'rating'. Or maybe owner. If you start the name with an
     *		underscore the order will be reversed. You can also specify 'rand' as the
     *		order which will return links in a random order.
     * @param int $limit Limit to X entries. If not specified, all entries are shown.
     * @return unknown
     */
    public Array<Object> get_linkobjects(int category, String orderby, int limit) {
        Array<Object> links = null;
        Array<Object> links_array = new Array<Object>();
        Object link = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_bookmarks()");
        links = getIncluded(BookmarkPage.class, gVars, gConsts).get_bookmarks("category=" + strval(category) + "&orderby=" + orderby + "&limit=" + strval(limit));
        links_array = new Array<Object>();

        for (Map.Entry javaEntry456 : links.entrySet()) {
            link = javaEntry456.getValue();
            links_array.putValue(link);
        }

        return links_array;
    }

    /**
     * get_linksbyname_withrating() - Gets the links associated with category 'cat_name' and display rating stars/chars.
     *
     * @since 0.71
     * @deprecated Use get_bookmarks()
     * @see get_bookmarks()
     *
     * @param string $cat_name The category name to use. If no match is found uses all
     * @param string $before The html to output before the link
     * @param string $after The html to output after the link
     * @param string $between The html to output between the link/image and it's description. Not used if no image or show_images is true
     * @param bool $show_images Whether to show images (if defined).
     * @param string $orderby the order to output the links. E.g. 'id', 'name', 'url',
     *		'description', or 'rating'. Or maybe owner. If you start the name with an
     *		underscore the order will be reversed. You can also specify 'rand' as the
     *		order which will return links in a random order.
     * @param bool $show_description Whether to show the description if show_images=false/not defined
     * @param int $limit Limit to X entries. If not specified, all entries are shown.
     * @param int $show_updated Whether to show last updated timestamp
     */
    public void get_linksbyname_withrating(Object cat_name, Object before, Object after, Object between, Object show_images, Object orderby, Object show_description, Object limit, Object show_updated) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_bookmarks()");
        get_linksbyname(cat_name, before, after, between, show_images, orderby, show_description, true, limit, show_updated);
    }

    /**
     * get_links_withrating() - Gets the links associated with category n and
     * display rating stars/chars.
     * @since 0.71
     * @deprecated Use get_bookmarks()
     * @see get_bookmarks()
     * @param int $category The category to use. If no category supplied uses
     * all
     * @param string $before The html to output before the link
     * @param string $after The html to output after the link
     * @param string $between The html to output between the link/image and it's
     * description. Not used if no image or show_images == true
     * @param bool $show_images Whether to show images (if defined).
     * @param string $orderby The order to output the links. E.g. 'id', 'name',
     * 'url', 'description', or 'rating'. Or maybe owner. If you
     * start the name with an underscore the order will be reversed.
     * You can also specify 'rand' as the order which will return
     * links in a random order.
     * @param bool $show_description Whether to show the description if
     * show_images=false/not defined.
     * @param string $limit Limit to X entries. If not specified, all entries
     * are shown.
     * @param int $show_updated Whether to show last updated timestamp
     */
    public void get_links_withrating(Object category, Object before, Object after, Object between, Object show_images, Object orderby, Object show_description, Object limit, Object show_updated) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_bookmarks()");
        get_links(strval(category), strval(before), strval(after), strval(between), show_images, strval(orderby), show_description, true, intval(limit), intval(show_updated), true);
    }

    /**
     * get_autotoggle() - Gets the auto_toggle setting
     * @since 0.71
     * @deprecated No alternative function available
     * @param int $id The category to get. If no category supplied uses 0
     * @return int Only returns 0.
     */
    public int get_autotoggle(Object id) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", null);

        return 0;
    }

    /**
     * @since 0.71
     * @deprecated Use wp_list_categories()
     * @see wp_list_categories()
     * @param int $optionall
     * @param string $all
     * @param string $sort_column
     * @param string $sort_order
     * @param string $file
     * @param bool $list
     * @param int $optiondates
     * @param int $optioncount
     * @param int $hide_empty
     * @param int $use_desc_for_title
     * @param bool $children
     * @param int $child_of
     * @param int $categories
     * @param int $recurse
     * @param string $feed
     * @param string $feed_image
     * @param string $exclude
     * @param bool $hierarchical
     * @return unknown
     */
    public Object list_cats(Object optionall, Object all, Object sort_column, Object sort_order, Object file, Object list, Object optiondates, Object optioncount, Object hide_empty,
        Object use_desc_for_title, Object children, Object child_of, Object categories, Object recurse, Object feed, Object feed_image, Object exclude, Object hierarchical) {
        Array<Object> query = new Array<Object>();
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_list_categories()");
        query = Array.compact(
                new ArrayEntry("optionall", optionall),
                new ArrayEntry("all", all),
                new ArrayEntry("sort_column", sort_column),
                new ArrayEntry("sort_order", sort_order),
                new ArrayEntry("file", file),
                new ArrayEntry("list", list),
                new ArrayEntry("optiondates", optiondates),
                new ArrayEntry("optioncount", optioncount),
                new ArrayEntry("hide_empty", hide_empty),
                new ArrayEntry("use_desc_for_title", use_desc_for_title),
                new ArrayEntry("children", children),
                new ArrayEntry("child_of", child_of),
                new ArrayEntry("categories", categories),
                new ArrayEntry("recurse", recurse),
                new ArrayEntry("feed", feed),
                new ArrayEntry("feed_image", feed_image),
                new ArrayEntry("exclude", exclude),
                new ArrayEntry("hierarchical", hierarchical));

        return wp_list_cats(query);
    }

    /**
     * @since 1.2
     * @deprecated Use wp_list_categories()
     * @see wp_list_categories()
     * @param string|array $args
     * @return unknown
     */
    public Object wp_list_cats(Array<Object> args) {
        Array<Object> r = new Array<Object>();
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_list_categories()");
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, "");

    	// Map to new names.
        if (isset(r.getValue("optionall")) && isset(r.getValue("all"))) {
            r.putValue("show_option_all", r.getValue("all"));
        }

        if (isset(r.getValue("sort_column"))) {
            r.putValue("orderby", r.getValue("sort_column"));
        }

        if (isset(r.getValue("sort_order"))) {
            r.putValue("order", r.getValue("sort_order"));
        }

        if (isset(r.getValue("optiondates"))) {
            r.putValue("show_last_update", r.getValue("optiondates"));
        }

        if (isset(r.getValue("optioncount"))) {
            r.putValue("show_count", r.getValue("optioncount"));
        }

        if (isset(r.getValue("list"))) {
            r.putValue("style", booleanval(r.getValue("list"))
                ? "list"
                : "break");
        }

        r.putValue("title_li", "");

        return getIncluded(Category_templatePage.class, gVars, gConsts).wp_list_categories(strval(r));
    }

    /**
     * @since 0.71
     * @deprecated Use wp_dropdown_categories()
     * @see wp_dropdown_categories()
     * @param int $optionall
     * @param string $all
     * @param string $orderby
     * @param string $order
     * @param int $show_last_update
     * @param int $show_count
     * @param int $hide_empty
     * @param bool $optionnone
     * @param int $selected
     * @param int $exclude
     * @return unknown
     */
    public Object dropdown_cats(Object optionall, Object all, Object orderby, Object order, Object show_last_update, Object show_count, Object hide_empty, Object optionnone, Object selected,
        Object exclude) {
        String show_option_all = null;
        String show_option_none = null;
        Array<Object> vars = new Array<Object>();
        String query = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_dropdown_categories()");
        show_option_all = "";

        if (booleanval(optionall)) {
            show_option_all = strval(all);
        }

        show_option_none = "";

        if (booleanval(optionnone)) {
            show_option_none = getIncluded(L10nPage.class, gVars, gConsts).__("None", "default");
        }

        vars = Array.compact(
                new ArrayEntry("show_option_all", show_option_all),
                new ArrayEntry("show_option_none", show_option_none),
                new ArrayEntry("orderby", orderby),
                new ArrayEntry("order", order),
                new ArrayEntry("show_last_update", show_last_update),
                new ArrayEntry("show_count", show_count),
                new ArrayEntry("hide_empty", hide_empty),
                new ArrayEntry("selected", selected),
                new ArrayEntry("exclude", exclude));
        query = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg(vars, "");

        return getIncluded(Category_templatePage.class, gVars, gConsts).wp_dropdown_categories(query);
    }

    /**
     * @since 2.1
     * @deprecated Use wp_print_scripts() or WP_Scripts.
     * @see wp_print_scripts()
     * @see WP_Scripts
     */
    public void tinymce_include() {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_print_scripts()/WP_Scripts");
    }

    //Modified by Numiton
    //		wp_print_script("wp_tiny_mce");
    /**
     * @since 1.2
     * @deprecated Use wp_list_authors()
     * @see wp_list_authors()
     * @param bool $optioncount
     * @param bool $exclude_admin
     * @param bool $show_fullname
     * @param bool $hide_empty
     * @param string $feed
     * @param string $feed_image
     * @return unknown
     */
    public Object list_authors(Object optioncount, Object exclude_admin, Object show_fullname, Object hide_empty, Object feed, Object feed_image) {
        Array<Object> args = new Array<Object>();
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_list_authors()");
        args = Array.compact(new ArrayEntry("optioncount", optioncount), new ArrayEntry("exclude_admin", exclude_admin), new ArrayEntry("show_fullname", show_fullname),
                new ArrayEntry("hide_empty", hide_empty), new ArrayEntry("feed", feed), new ArrayEntry("feed_image", feed_image));

        return getIncluded(Author_templatePage.class, gVars, gConsts).wp_list_authors(args);
    }

    /**
     * @since 1.0.1
     * @deprecated Use wp_get_post_categories()
     * @see wp_get_post_categories()
     * @param int $blogid Not Used
     * @param int $post_ID
     * @return unknown
     */
    public Object wp_get_post_cats(int blogid, int post_ID) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_get_post_categories()");

        return getIncluded(PostPage.class, gVars, gConsts).wp_get_post_categories(post_ID, new Array<Object>());
    }

    /**
     * wp_set_post_cats() - Sets the categories that the post id belongs to.
     * @since 1.0.1
     * @deprecated Use wp_set_post_categories()
     * @see wp_set_post_categories()
     * @param int $blogid Not used
     * @param int $post_ID
     * @param array $post_categories
     * @return unknown
     */
    public Object wp_set_post_cats(int blogid, int post_ID, Object post_categories) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_set_post_categories()");

        return getIncluded(PostPage.class, gVars, gConsts).wp_set_post_categories(post_ID, post_categories);
    }

    /**
     * @since 0.71
     * @deprecated Use wp_get_archives()
     * @see wp_get_archives()
     * @param string $type
     * @param string $limit
     * @param string $format
     * @param string $before
     * @param string $after
     * @param bool $show_post_count
     * @return unknown
     */
    public void get_archives(Object type, Object limit, Object format, Object before, Object after, Object show_post_count) {
        Array<Object> args = new Array<Object>();
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_get_archives()");
        args = Array.compact(
                new ArrayEntry("type", type),
                new ArrayEntry("limit", limit),
                new ArrayEntry("format", format),
                new ArrayEntry("before", before),
                new ArrayEntry("after", after),
                new ArrayEntry("show_post_count", show_post_count));
        getIncluded(General_templatePage.class, gVars, gConsts).wp_get_archives(strval(args));
    }

    /**
     * get_author_link() - Returns or Prints link to the author's posts
     * @since 1.2
     * @deprecated Use get_author_posts_url()
     * @see get_author_posts_url()
     * @param bool $echo Optional.
     * @param int $author_id Required.
     * @param string $author_nicename Optional.
     * @return string|null
     */
    public String get_author_link(boolean echo, Object author_id, Object author_nicename) {
        String link = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_author_posts_url()");
        link = getIncluded(Author_templatePage.class, gVars, gConsts).get_author_posts_url(intval(author_id), strval(author_nicename));

        if (echo) {
            echo(gVars.webEnv, link);
        }

        return link;
    }

    /**
     * link_pages() - Print list of pages based on arguments
     * @since 0.71
     * @deprecated Use wp_link_pages()
     * @see wp_link_pages()
     * @param string $before
     * @param string $after
     * @param string $next_or_number
     * @param string $nextpagelink
     * @param string $previouspagelink
     * @param string $pagelink
     * @param string $more_file
     * @return string
     */
    public Object link_pages(Object before, Object after, Object next_or_number, Object nextpagelink, Object previouspagelink, Object pagelink, Object more_file) {
        Array<Object> args = new Array<Object>();
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "wp_link_pages()");
        args = Array.compact(
                new ArrayEntry("before", before),
                new ArrayEntry("after", after),
                new ArrayEntry("next_or_number", next_or_number),
                new ArrayEntry("nextpagelink", nextpagelink),
                new ArrayEntry("previouspagelink", previouspagelink),
                new ArrayEntry("pagelink", pagelink),
                new ArrayEntry("more_file", more_file));

        return getIncluded(Post_templatePage.class, gVars, gConsts).wp_link_pages(args);
    }

    /**
     * get_settings() - Get value based on option
     * @since 0.71
     * @deprecated Use get_option()
     * @see get_option()
     * @param string $option
     * @return string
     */
    public Object get_settings(Object option) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_option()");

        return getIncluded(FunctionsPage.class, gVars, gConsts).get_option(strval(option));
    }

    /**
     * permalink_link() - Print the permalink of the current post in the loop
     * @since 0.71
     * @deprecated Use the_permalink()
     * @see the_permalink()
     */
    public void permalink_link() {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "the_permalink()");
        getIncluded(Link_templatePage.class, gVars, gConsts).the_permalink();
    }

    /**
     * permalink_single_rss() - Print the permalink to the RSS feed
     * @since 0.71
     * @deprecated Use the_permalink_rss()
     * @see the_permalink_rss()
     * @param string $file
     */
    public void permalink_single_rss(Object deprecated) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "the_permalink_rss()");
        getIncluded(FeedPage.class, gVars, gConsts).the_permalink_rss();
    }

    /**
     * wp_get_links() - Gets the links associated with category.
     * @see get_links() for argument information that can be used in $args
     * @since 1.0.1
     * @deprecated Use get_bookmarks()
     * @see get_bookmarks()
     * @param string $args a query string
     * @return null|string
     */
    public Object wp_get_links(String args) {
        String cat_id = null;
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Object category = null;
        Object before = null;
        Object after = null;
        Object between = null;
        Object show_images = null;
        Object orderby = null;
        Object show_description = null;
        Object show_rating = null;
        Object limit = null;
        Object show_updated = null;
        Object echo = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_bookmarks()");

        if (strictEqual(Strings.strpos(args, "="), BOOLEAN_FALSE)) {
            cat_id = args;
            args = getIncluded(FunctionsPage.class, gVars, gConsts).add_query_arg("category", cat_id, args);
        }

        defaults = new Array<Object>(
                new ArrayEntry<Object>("category", -1),
                new ArrayEntry<Object>("before", ""),
                new ArrayEntry<Object>("after", "<br />"),
                new ArrayEntry<Object>("between", " "),
                new ArrayEntry<Object>("show_images", true),
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("show_description", true),
                new ArrayEntry<Object>("show_rating", false),
                new ArrayEntry<Object>("limit", -1),
                new ArrayEntry<Object>("show_updated", true),
                new ArrayEntry<Object>("echo", true));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        category = Array.extractVar(r, "category", category, Array.EXTR_SKIP);
        before = Array.extractVar(r, "before", before, Array.EXTR_SKIP);
        after = Array.extractVar(r, "after", after, Array.EXTR_SKIP);
        between = Array.extractVar(r, "between", between, Array.EXTR_SKIP);
        show_images = Array.extractVar(r, "show_images", show_images, Array.EXTR_SKIP);
        orderby = Array.extractVar(r, "orderby", orderby, Array.EXTR_SKIP);
        show_description = Array.extractVar(r, "show_description", show_description, Array.EXTR_SKIP);
        show_rating = Array.extractVar(r, "show_rating", show_rating, Array.EXTR_SKIP);
        limit = Array.extractVar(r, "limit", limit, Array.EXTR_SKIP);
        show_updated = Array.extractVar(r, "show_updated", show_updated, Array.EXTR_SKIP);
        echo = Array.extractVar(r, "echo", echo, Array.EXTR_SKIP);

        return get_links(strval(category), strval(before), strval(after), strval(between), show_images, strval(orderby), show_description, show_rating, intval(limit), intval(show_updated), echo);
    }

    /**
     * get_links() - Gets the links associated with category by id.
     * @since 0.71
     * @deprecated Use get_bookmarks()
     * @see get_bookmarks()
     * @param int $category The category to use. If no category supplied uses
     * all
     * @param string $before the html to output before the link
     * @param string $after the html to output after the link
     * @param string $between the html to output between the link/image and its
     * description. Not used if no image or show_images == true
     * @param bool $show_images whether to show images (if defined).
     * @param string $orderby the order to output the links. E.g. 'id', 'name',
     * 'url', 'description', or 'rating'. Or maybe owner. If you
     * start the name with an underscore the order will be reversed.
     * You can also specify 'rand' as the order which will return
     * links in a random order.
     * @param bool $show_description whether to show the description if
     * show_images=false/not defined.
     * @param bool $show_rating show rating stars/chars
     * @param int $limit Limit to X entries. If not specified, all entries are
     * shown.
     * @param int $show_updated whether to show last updated timestamp
     * @param bool $echo whether to echo the results, or return them instead
     * @return null|string
     */
    public String get_links(String category, String before, String after, String between, Object show_images, String orderby, Object show_description, Object show_rating, int limit, int show_updated,
        Object echo) {
        String order = null;
        Object results = null;
        String output = null;
        StdClass row = null;
        String the_link = null;
        String rel = null;
        String desc = null;
        String name = null;
        String title = null;
        String alt = null;
        String target = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_bookmarks()");
        order = "ASC";

        if (equal(Strings.substr(orderby, 0, 1), "_")) {
            order = "DESC";
            orderby = Strings.substr(orderby, 1);
        }

        if (equal(category, -1)) { //get_bookmarks uses '' to signify all categories
            category = "";
        }

        results = getIncluded(BookmarkPage.class, gVars, gConsts).get_bookmarks(
                    "category=" + category + "&orderby=" + orderby + "&order=" + order + "&show_updated=" + strval(show_updated) + "&limit=" + strval(limit));

        if (!booleanval(results)) {
            return null;
        }

        output = "";

        for (Map.Entry javaEntry457 : new Array<Object>(results).entrySet()) {
            row = (StdClass) javaEntry457.getValue();

            if (!isset(StdClass.getValue(row, "recently_updated"))) {
                row.fields.putValue("recently_updated", false);
            }

            output = output + before;

            if (booleanval(show_updated) && booleanval(StdClass.getValue(row, "recently_updated"))) {
                output = output + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_recently_updated_prepend");
            }

            the_link = "#";

            if (!empty(StdClass.getValue(row, "link_url"))) {
                the_link = getIncluded(FormattingPage.class, gVars, gConsts).clean_url(strval(StdClass.getValue(row, "link_url")), null, "display");
            }

            rel = strval(StdClass.getValue(row, "link_rel"));

            if (!equal("", rel)) {
                rel = " rel=\"" + rel + "\"";
            }

            desc = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                    strval(getIncluded(BookmarkPage.class, gVars, gConsts)
                               .sanitize_bookmark_field("link_description", StdClass.getValue(row, "link_description"), StdClass.getValue(row, "link_id"), "display")));
            name = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(
                    strval(getIncluded(BookmarkPage.class, gVars, gConsts).sanitize_bookmark_field("link_name", StdClass.getValue(row, "link_name"), StdClass.getValue(row, "link_id"), "display")));
            title = desc;

            if (booleanval(show_updated)) {
                if (!equal(Strings.substr(strval(StdClass.getValue(row, "link_updated_f")), 0, 2), "00")) {
                    title = title + " (" + getIncluded(L10nPage.class, gVars, gConsts).__("Last updated", "default") + " " +
                        DateTime.date(
                            strval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_updated_date_format")),
                            intval(intval(StdClass.getValue(row, "link_updated_f")) + (floatval(getIncluded(FunctionsPage.class, gVars, gConsts).get_option("gmt_offset")) * 3600))) + ")";
                }
            }

            if (!equal("", title)) {
                title = " title=\"" + title + "\"";
            }

            alt = " alt=\"" + name + "\"";
            target = strval(StdClass.getValue(row, "link_target"));

            if (!equal("", target)) {
                target = " target=\"" + target + "\"";
            }

            output = output + "<a href=\"" + the_link + "\"" + rel + title + target + ">";

            if (!equal(StdClass.getValue(row, "link_image"), null) && booleanval(show_images)) {
                if (!strictEqual(Strings.strpos(strval(StdClass.getValue(row, "link_image")), "http"), BOOLEAN_FALSE)) {
                    output = output + "<img src=\"" + StdClass.getValue(row, "link_image") + "\" " + alt + " " + title + " />";
                } else { // If it's a relative path
                    output = output + "<img src=\"" + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("siteurl") + StdClass.getValue(row, "link_image") + "\" " + alt + " " + title +
                        " />";
                }
            } else {
                output = output + name;
            }

            output = output + "</a>";

            if (booleanval(show_updated) && booleanval(StdClass.getValue(row, "recently_updated"))) {
                output = output + getIncluded(FunctionsPage.class, gVars, gConsts).get_option("links_recently_updated_append");
            }

            if (booleanval(show_description) && !equal("", desc)) {
                output = output + between + desc;
            }

            if (booleanval(show_rating)) {
                output = output + between + get_linkrating(row);
            }

            output = output + after + "\n";
        } // end while

        if (!booleanval(echo)) {
            return output;
        }

        echo(gVars.webEnv, output);

        return "";
    }

    /**
     * get_links_list() - Output entire list of links by category
     * Output a list of all links, listed by category, using the settings in
     * $wpdb->linkcategories and output it as a nested HTML unordered list.
     * @author Dougal
     * @since 1.0.1
     * @deprecated Use get_categories()
     * @see get_categories()
     * @param string $order Sort link categories by 'name' or 'id'
     * @param string $$deprecated Not Used
     */
    public void get_links_list(String order, Object deprecated) {
        String direction = null;
        Array<Object> cats = new Array<Object>();
        StdClass cat = null;
        
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_categories()");
        
        order = Strings.strtolower(order);
        
    	// Handle link category sorting
        direction = "ASC";

        if (equal("_", Strings.substr(order, 0, 1))) {
            direction = "DESC";
            order = Strings.substr(order, 1);
        }

        if (!isset(direction)) {
            direction = "";
        }

        cats = getIncluded(CategoryPage.class, gVars, gConsts).get_categories("type=link&orderby=" + order + "&order=" + direction + "&hierarchical=0");

    	// Display each category
        if (booleanval(cats)) {
            for (Map.Entry javaEntry458 : new Array<Object>(cats).entrySet()) {
                cat = (StdClass) javaEntry458.getValue();
                
    			// Handle each category.

    			// Display the category name
                echo(gVars.webEnv,
                    "  <li id=\"linkcat-" + StdClass.getValue(cat, "term_id") + "\" class=\"linkcat\"><h2>" +
                    getIncluded(PluginPage.class, gVars, gConsts).apply_filters("link_category", StdClass.getValue(cat, "name")) + "</h2>\n\t<ul>\n");
                
    			// Call get_links() with all the appropriate params
                get_links(strval(StdClass.getValue(cat, "term_id")), "<li>", "</li>", "\n", true, "name", false, false, -1, 1, true);
                
    			// Close the last category
                echo(gVars.webEnv, "\n\t</ul>\n</li>\n");
            }
        }
    }

    /**
     * links_popup_script() - Show the link to the links popup and the number
     * of links
     * @author Fullo
     * @link http://sprite.csr.unibo.it/fullo
     * @since 0.71
     * @deprecated {@internal Use function instead is unknown}}
     * @param string $text the text of the link
     * @param int $width the width of the popup window
     * @param int $height the height of the popup window
     * @param string $file the page to open in the popup window
     * @param bool $count the number of links in the db
     */
    public void links_popup_script(Object text, Object width, Object height, Object file, Object count) {
    }

    // Commented by Numiton
    //		Object counts = null;
    //		Object wpdb = null;
    //		Object javascript = null;
    //		getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", null);
    //		if (booleanval(count)) {
    //			counts = wpdb.get_var("SELECT COUNT(*) FROM " + wpdb.links);
    //		}
    //		javascript = "<a href=\"#\" onclick=\"javascript:window.open(\'" + strval(file) + "?popup=1\', \'_blank\', \'width=" + strval(width) + ",height=" + strval(height)
    //		        + ",scrollbars=yes,status=no\'); return 0\">";
    //		javascript = strval(javascript) + strval(text);
    //		if (booleanval(count)) {
    //			javascript = strval(javascript) + " (" + strval(counts) + ")";
    //		}
    //		javascript = strval(javascript) + "</a>\n\n";
    //		echo(gVars.webEnv, javascript);
    /**
     * @since 1.0.1
     * @deprecated Use sanitize_bookmark_field()
     * @see sanitize_bookmark_field()
     * @param object $link
     * @return unknown
     */
    public Object get_linkrating(StdClass link) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "sanitize_bookmark_field()");

        return getIncluded(BookmarkPage.class, gVars, gConsts).sanitize_bookmark_field("link_rating", StdClass.getValue(link, "link_rating"), StdClass.getValue(link, "link_id"), "display");
    }

    /**
     * get_linkcatname() - Gets the name of category by id.
     * @since 0.71
     * @deprecated Use get_category()
     * @see get_category()
     * @param int $id The category to get. If no category supplied uses 0
     * @return string
     */
    public String get_linkcatname(int id) {
        Object cats;

        /* Do not change type */
        int cat_id = 0;
        StdClass cat = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_category()");
        id = id;

        if (empty(id)) {
            return "";
        }

        cats = (((org.numiton.nwp.wp_admin.includes.BookmarkPage) getIncluded(org.numiton.nwp.wp_admin.includes.BookmarkPage.class, gVars, gConsts))).wp_get_link_cats(id);

        if (empty(cats) || !is_array(cats)) {
            return "";
        }

        cat_id = intval(((Array) cats).getValue(0)); // Take the first cat.
        
        cat = (StdClass) getIncluded(CategoryPage.class, gVars, gConsts).get_category(cat_id, gConsts.getOBJECT(), "raw");

        return strval(StdClass.getValue(cat, "name"));
    }

    /**
     * comment_rss_link() - Print RSS comment feed link
     * @since 1.0.1
     * @deprecated Use post_comments_feed_link()
     * @see post_comments_feed_link()
     * @param string $link_text
     * @param string $deprecated Not used
     */
    public void comments_rss_link(String link_text, Object deprecated) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "post_comments_feed_link()");
        getIncluded(Link_templatePage.class, gVars, gConsts).post_comments_feed_link(link_text, intval(""), "");
    }

    /**
     * get_category_rss_link() - Print/Return link to category RSS2 feed
     * @since 1.2
     * @deprecated Use get_category_feed_link()
     * @see get_category_feed_link()
     * @param bool $echo
     * @param int $cat_ID
     * @param string $deprecated Not used
     * @return string|null
     */
    public String get_category_rss_link(boolean echo, int cat_ID, Object deprecated) {
        String link = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_category_feed_link()");
        link = getIncluded(Link_templatePage.class, gVars, gConsts).get_category_feed_link(cat_ID, "rss2");

        if (echo) {
            echo(gVars.webEnv, link);
        }

        return link;
    }

    /**
     * get_author_rss_link() - Print/Return link to author RSS feed
     * @since 1.2
     * @deprecated Use get_author_feed_link()
     * @see get_author_feed_link()
     * @param bool $echo
     * @param int $author_id
     * @param string $deprecated Not used
     * @return string|null
     */
    public String get_author_rss_link(boolean echo, int author_id, Object deprecated) {
        String link = null;
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "0.0", "get_author_feed_link()");
        link = getIncluded(Link_templatePage.class, gVars, gConsts).get_author_feed_link(author_id, "");

        if (echo) {
            echo(gVars.webEnv, link);
        }

        return link;
    }

    /**
     * comments_rss() - Return link to the post RSS feed
     * @since 1.5
     * @deprecated Use get_post_comments_feed_link()
     * @see get_post_comments_feed_link()
     * @param string $deprecated Not used
     * @return string
     */
    public Object comments_rss(Object deprecated) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "2.2", "get_post_comments_feed_link()");

        return getIncluded(Link_templatePage.class, gVars, gConsts).get_post_comments_feed_link(intval(""), "");
    }

    /**
     * create_user() - An alias of wp_create_user().
     * @param string $username The user's username.
     * @param string $password The user's password.
     * @param string $email The user's email (optional).
     * @return int The new user's ID.
     * @deprecated Use wp_create_user()
     * @see wp_create_user()
     */
    public int create_user(String username, String password, String email) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "2.0", "wp_create_user()");

        return getIncluded(RegistrationPage.class, gVars, gConsts).wp_create_user(username, password, email);
    }

    /**
     * documentation_link() - Unused Admin function
     * @since 2.0
     * @param string $deprecated Unknown
     * @deprecated 2.5
     */
    public void documentation_link(Object deprecated) {
        getIncluded(FunctionsPage.class, gVars, gConsts)._deprecated_function(SourceCodeInfo.getCurrentFunction(), "2.5", "");

        return;
    }

    /**
     * gzip_compression() - Unused function
     * @deprecated 2.5
     */
    public boolean gzip_compression() {
        return false;
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;
        
        /**
         * Deprecated functions from past WordPress versions
         * @package WordPress
         * @subpackage Deprecated
         */

        /*
         * Deprecated global variables.
         */

        /**
         * The name of the Posts table
         * @global string $tableposts
         * @deprecated Use $wpdb->posts
         */
        tableposts = gVars.wpdb.posts;
        
        /**
         * The name of the Users table
         * @global string $tableusers
         * @deprecated Use $wpdb->users
         */
        tableusers = gVars.wpdb.users;
        
        /**
         * The name of the Categories table
         * @global string $tablecategories
         * @deprecated Use $wpdb->categories
         */
        tablecategories = gVars.wpdb.categories;
        
        /**
         * The name of the post to category table
         * @global string $tablepost2cat
         * @deprecated Use $wpdb->post2cat;
         */
        tablepost2cat = gVars.wpdb.post2cat;
        
        /**
         * The name of the comments table
         * @global string $tablecomments
         * @deprecated Use $wpdb->comments;
         */
        tablecomments = gVars.wpdb.comments;
        
        /**
         * The name of the links table
         * @global string $tablelinks
         * @deprecated Use $wpdb->links;
         */
        tablelinks = gVars.wpdb.links;
        
        /**
         * @global string $tablelinkcategories
         * @deprecated Not used anymore;
         */
        tablelinkcategories = "linkcategories_is_gone";
        
        /**
         * The name of the options table
         * @global string $tableoptions
         * @deprecated Use $wpdb->options;
         */
        tableoptions = gVars.wpdb.options;
        
        /**
         * The name of the postmeta table
         * @global string $tablepostmeta
         * @deprecated Use $wpdb->postmeta;
         */
        tablepostmeta = gVars.wpdb.postmeta;

        return DEFAULT_VAL;
    }
}
