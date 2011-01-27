/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: TaxonomyPage.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.numiton.ClassHandling;
import com.numiton.FunctionHandling;
import com.numiton.VarHandling;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.Callback;
import com.numiton.generic.PhpWebEnvironment;
import com.numiton.generic.StdClass;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;
import com.numiton.ntile.til.libraries.php.quercus.QStrings;
import com.numiton.string.Strings;


@Controller
@Scope("request")
public class TaxonomyPage extends NumitonController {
    protected static final Logger LOG = Logger.getLogger(TaxonomyPage.class.getName());
    public boolean wp_defer_term_counting__defer = false;
    public Array<Object> wp_update_term_count__deferred = new Array<Object>();

    @Override
    @RequestMapping("/wp-includes/taxonomy.php")
    public ModelAndView execute(HttpServletRequest javaRequest, HttpServletResponse javaResponse)
        throws IOException {
        return super.execute(javaRequest, javaResponse);
    }

    @Override
    public String getViewName() {
        return "wp_includes/taxonomy";
    }

    /**
     * get_object_taxonomies() - Return all of the taxonomy names that are of
     * $object_type
     * It appears that this function can be used to find all of the names inside
     * of $wp_taxonomies global variable.
     * <code><?php $taxonomies = get_object_taxonomies('post'); ?></code>
     * Should result in <code>Array('category', 'post_tag')</code>
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wp_taxonomies
     * @param array|string|object $object Name of the type of taxonomy object,
     * or an object (row from posts)
     * @return array The names of all taxonomy of $object_type.
     */
    public Array<Object> get_object_taxonomies(Object object)/* Do not change type */
     {
        Array<Object> taxonomies = new Array<Object>();
        StdClass taxonomy = null;

        if (is_object(object)) {
            if (equal(((StdClass) object).fields.getValue("post_type"), "attachment")) {
                return getIncluded(MediaPage.class, gVars, gConsts).get_attachment_taxonomies(object);
            }

            object = ((StdClass) object).fields.getValue("post_type");
        }

        object = new Array<Object>(object);
        taxonomies = new Array<Object>();

        for (Map.Entry javaEntry599 : gVars.wp_taxonomies.entrySet()) {
            taxonomy = (StdClass) javaEntry599.getValue();

            if (booleanval(Array.array_intersect((Array) object, new Array<Object>(StdClass.getValue(taxonomy, "object_type"))))) {
                taxonomies.putValue(StdClass.getValue(taxonomy, "name"));
            }
        }

        return taxonomies;
    }

    /**
     * get_taxonomy() - Returns the taxonomy object of $taxonomy.
     * The get_taxonomy function will first check that the parameter string
     * given is a taxonomy object and if it is, it will return it.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wp_taxonomies
     * @uses is_taxonomy() Checks whether taxonomy exists
     * @param string $taxonomy Name of taxonomy object to return
     * @return object|bool The Taxonomy Object or false if $taxonomy doesn't
     * exist
     */
    public Object get_taxonomy(String taxonomy) {
        if (!is_taxonomy(taxonomy)) {
            return false;
        }

        return gVars.wp_taxonomies.getValue(taxonomy);
    }

    /**
     * is_taxonomy() - Checks that the taxonomy name exists
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wp_taxonomies
     * @param string $taxonomy Name of taxonomy object
     * @return bool Whether the taxonomy exists or not.
     */
    public boolean is_taxonomy(String taxonomy) {
        return isset(gVars.wp_taxonomies.getValue(taxonomy));
    }

    /**
     * is_taxonomy_hierarchical() - Whether the taxonomy object is
     * hierarchical
     * Checks to make sure that the taxonomy is an object first. Then Gets the
     * object, and finally returns the hierarchical value in the object.
     * A false return value might also mean that the taxonomy does not exist.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses is_taxonomy() Checks whether taxonomy exists
     * @uses get_taxonomy() Used to get the taxonomy object
     * @param string $taxonomy Name of taxonomy object
     * @return bool Whether the taxonomy is hierarchical
     */
    public boolean is_taxonomy_hierarchical(String taxonomy) {
        if (!is_taxonomy(taxonomy)) {
            return false;
        }

        StdClass taxonomyObj = (StdClass) get_taxonomy(taxonomy);

        return booleanval(StdClass.getValue(taxonomyObj, "hierarchical"));
    }

    /**
     * register_taxonomy() - Create or modify a taxonomy object. Do not use
     * before init.
     * A simple function for creating or modifying a taxonomy object based on
     * the parameters given. The function will accept an array (third optional
     * parameter), along with strings for the taxonomy name and another string
     * for the object type.
     * Nothing is returned, so expect error maybe or use is_taxonomy() to check
     * whether taxonomy exists.
     * Optional $args contents: hierarachical - has some defined purpose at
     * other parts of the API and is a boolean value. update_count_callback -
     * works much like a hook, in that it will be called when the count is
     * updated. rewrite - false to prevent rewrite, or array('slug'=>$slug) to
     * customize permastruct; default will use $taxonomy as slug query_var -
     * false to prevent queries, or string to customize query var
     * (?$query_var=$term); default will use $taxonomy as query var
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wp_taxonomies Inserts new taxonomy object into the list
     * @uses $wp_rewrite Adds rewrite tags and permastructs
     * @uses $wp Adds query vars
     * @param string $taxonomy Name of taxonomy object
     * @param array|string $object_type Name of the object type for the taxonomy
     * object.
     * @param array|string $args See above description for the two keys values.
     */
    public void register_taxonomy(String taxonomy, Object object_type, Object argsObj) {
        Array<Object> defaults = new Array<Object>();
        Object term = null;
        defaults = new Array<Object>(
                new ArrayEntry<Object>("hierarchical", false),
                new ArrayEntry<Object>("update_count_callback", ""),
                new ArrayEntry<Object>("rewrite", true),
                new ArrayEntry<Object>("query_var", true));

        Array args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(argsObj, defaults);

        if (!equal(false, args.getValue("query_var")) && !empty(gVars.wp)) {
            if (empty(args.getValue("query_var"))) {
                args.putValue("query_var", taxonomy);
            }

            args.putValue("query_var", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title_with_dashes(strval(args.getValue("query_var"))));
            gVars.wp.add_query_var(strval(args.getValue("query_var")));
        }

        if (!equal(false, args.getValue("rewrite")) && !empty(gVars.wp_rewrite)) {
            if (!is_array(args.getValue("rewrite"))) {
                args.putValue("rewrite", new Array<Object>());
            }

            if (!isset(args.getArrayValue("rewrite").getValue("slug"))) {
                args.getArrayValue("rewrite").putValue("slug", getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title_with_dashes(taxonomy));
            }

            gVars.wp_rewrite.add_rewrite_tag("%" + taxonomy + "%", "([^/]+)", booleanval(args.getValue("query_var"))
                ? (args.getValue("query_var") + "=")
                : ("taxonomy=" + taxonomy + "&term=" + term));
            gVars.wp_rewrite.add_permastruct(taxonomy, args.getArrayValue("rewrite").getValue("slug") + "/%" + taxonomy + "%");
        }

        args.putValue("name", taxonomy);
        args.putValue("object_type", object_type);
        gVars.wp_taxonomies.putValue(taxonomy, Array.toStdClass(args));
    }

 //
 // Term API
 //

 /**
  * get_objects_in_term() - Return object_ids of valid taxonomy and term
  *
  * The strings of $taxonomies must exist before this function will continue. On failure of finding
  * a valid taxonomy, it will return an WP_Error class, kind of like Exceptions in PHP 5, except you
  * can't catch them. Even so, you can still test for the WP_Error class and get the error message.
  *
  * The $terms aren't checked the same as $taxonomies, but still need to exist for $object_ids to
  * be returned.
  *
  * It is possible to change the order that object_ids is returned by either using PHP sort family
  * functions or using the database by using $args with either ASC or DESC array. The value should
  * be in the key named 'order'.
  *
  * @package WordPress
  * @subpackage Taxonomy
  * @since 2.3
  *
  * @uses $wpdb
  * @uses wp_parse_args() Creates an array from string $args.
  *
  * @param string|array $terms String of term or array of string values of terms that will be used
  * @param string|array $taxonomies String of taxonomy name or Array of string values of taxonomy names
  * @param array|string $args Change the order of the object_ids, either ASC or DESC
  * @return WP_Error|array If the taxonomy does not exist, then WP_Error will be returned. On success
  *	the array can be empty meaning that there are no $object_ids found or it will return the $object_ids found.
  */
    public Object get_objects_in_term(Object termsObj, /* Do not change type */
        Object taxonomiesObj, /* Do not change type */
        Object args) {
        String taxonomy = null;
        Array<Object> defaults = new Array<Object>();
        String order = null;
        Array<Object> object_ids = new Array<Object>();

        /* Modified by Numiton */
        Array terms;

        if (!is_array(termsObj)) {
            terms = new Array<Object>(new ArrayEntry<Object>(termsObj));
        } else {
            terms = (Array) termsObj;
        }

        /* Modified by Numiton */
        Array taxonomies;

        if (!is_array(taxonomiesObj)) {
            taxonomies = new Array<Object>(new ArrayEntry<Object>(taxonomiesObj));
        } else {
            taxonomies = (Array) taxonomiesObj;
        }

        for (Map.Entry javaEntry600 : ((Array<?>) taxonomies).entrySet()) {
            taxonomy = strval(javaEntry600.getValue());

            if (!is_taxonomy(taxonomy)) {
                return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid Taxonomy", "default"));
            }
        }

        defaults = new Array<Object>(new ArrayEntry<Object>("order", "ASC"));
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        order = strval(Array.extractVar((Array) args, "order", order, Array.EXTR_SKIP));
        order = (equal("desc", Strings.strtolower(order))
            ? "DESC"
            : "ASC");
        terms = Array.array_map(new Callback("intval", VarHandling.class), terms);

        String taxonomiesStr = "\'" + Strings.implode("\', \'", (Array) taxonomies) + "\'";
        String termsStr = "\'" + Strings.implode("\', \'", (Array) terms) + "\'";
        object_ids = gVars.wpdb.get_col(
                    "SELECT tr.object_id FROM " + gVars.wpdb.term_relationships + " AS tr INNER JOIN " + gVars.wpdb.term_taxonomy +
                    " AS tt ON tr.term_taxonomy_id = tt.term_taxonomy_id WHERE tt.taxonomy IN (" + taxonomiesStr + ") AND tt.term_id IN (" + termsStr + ") ORDER BY tr.object_id " + order);

        if (!booleanval(object_ids)) {
            return new Array<Object>();
        }

        return object_ids;
    }

    /**
     * get_term() - Get all Term data from database by Term ID.
     * The usage of the get_term function is to apply filters to a term object.
     * It is possible to get a term object from the database before applying the
     * filters.
     * $term ID must be part of $taxonomy, to get from the database. Failure,
     * might be able to be captured by the hooks. Failure would be the same
     * value as $wpdb returns for the get_row method.
     * There are two hooks, one is specifically for each term, named 'get_term',
     * and the second is for the taxonomy name, 'term_$taxonomy'. Both hooks
     * gets the term object, and the taxonomy name as parameters. Both hooks are
     * expected to return a Term object.
     * 'get_term' hook - Takes two parameters the term Object and the taxonomy
     * name. Must return term object. Used in get_term() as a catch-all filter
     * for every $term.
     * 'get_$taxonomy' hook - Takes two parameters the term Object and the
     * taxonomy name. Must return term object. $taxonomy will be the taxonomy
     * name, so for example, if 'category', it would be 'get_category' as the
     * filter name. Useful for custom taxonomies or plugging into default
     * taxonomies.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @uses sanitize_term() Cleanses the term based on $filter context before
     * returning.
     * @see sanitize_term_field() The $context param lists the available values
     * for get_term_by() $filter param.
     * @param int|object $term If integer, will get from database. If object
     * will apply filters and return $term.
     * @param string $taxonomy Taxonomy name that $term is part of.
     * @param string $output Constant OBJECT, ARRAY_A, or ARRAY_N
     * @param string $filter Optional, default is raw or no WordPress defined
     * filter will applied.
     * @return mixed|null|WP_Error Term Row from database. Will return null if
     * $term is empty. If taxonomy does not exist then WP_Error will be
     * returned.
     */
    public Object get_term(Object term, /* Do not change type */
        String taxonomy, String output, String filter) {
        Object _term;

        if (empty(term)) {
            return null;
        }

        if (!is_taxonomy(taxonomy)) {
            return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid Taxonomy", "default"));
        }

        if (is_object(term)) {
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(((StdClass) term).fields.getValue("term_id"), term, taxonomy, 0);
            _term = term;
        } else {
            term = intval(term);

            if (!booleanval(_term = getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(term, taxonomy))) {
                _term = gVars.wpdb.get_row(
                            gVars.wpdb.prepare(
                                    "SELECT t.*, tt.* FROM " + gVars.wpdb.terms + " AS t INNER JOIN " + gVars.wpdb.term_taxonomy +
                                    " AS tt ON t.term_id = tt.term_id WHERE tt.taxonomy = %s AND t.term_id = %s LIMIT 1",
                                    taxonomy,
                                    term));
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(term, _term, taxonomy, 0);
            }
        }

        _term = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_term", _term, taxonomy);
        _term = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_" + taxonomy, _term, taxonomy);
        _term = sanitize_term(_term, taxonomy, filter);

        if (equal(output, gConsts.getOBJECT())) {
            return _term;
        } else if (equal(output, gConsts.getARRAY_A())) {
            return ClassHandling.get_object_vars(_term);
        } else if (equal(output, gConsts.getARRAY_N())) {
            return Array.array_values(ClassHandling.get_object_vars(_term));
        } else {
            return _term;
        }
    }

    /**
     * get_term_by() - Get all Term data from database by Term field and data.
     * Warning: $value is not escaped for 'name' $field. You must do it
     * yourself, if required.
     * The default $field is 'id', therefore it is possible to also use null for
     * field, but not recommended that you do so.
     * If $value does not exist, the return value will be false. If $taxonomy
     * exists and $field and $value combinations exist, the Term will be
     * returned.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @uses sanitize_term() Cleanses the term based on $filter context before
     * returning.
     * @see sanitize_term_field() The $context param lists the available values
     * for get_term_by() $filter param.
     * @param string $field Either 'slug', 'name', or 'id'
     * @param string|int $value Search for this term value
     * @param string $taxonomy Taxonomy Name
     * @param string $output Constant OBJECT, ARRAY_A, or ARRAY_N
     * @param string $filter Optional, default is raw or no WordPress defined
     * filter will applied.
     * @return mixed Term Row from database. Will return false if $taxonomy does
     * not exist or $term was not found.
     */
    public Object get_term_by(String field, Object value, String taxonomy, Object output, String filter) {
        StdClass term = null;

        if (!is_taxonomy(taxonomy)) {
            return false;
        }

        if (equal("slug", field)) {
            field = "t.slug";
            value = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(value), "");

            if (empty(value)) {
                return false;
            }
        } else if (equal("name", field)) {
    		// Assume already escaped
            field = "t.name";
        } else {
            field = "t.term_id";
            value = intval(value);
        }

        term = (StdClass) gVars.wpdb.get_row(
                    gVars.wpdb.prepare(
                            "SELECT t.*, tt.* FROM " + gVars.wpdb.terms + " AS t INNER JOIN " + gVars.wpdb.term_taxonomy + " AS tt ON t.term_id = tt.term_id WHERE tt.taxonomy = %s AND " + field +
                            " = %s LIMIT 1",
                            taxonomy,
                            value));

        if (!booleanval(term)) {
            return false;
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(term, "term_id"), term, taxonomy, 0);
        term = (StdClass) sanitize_term(term, taxonomy, filter);

        if (equal(output, gConsts.getOBJECT())) {
            return term;
        } else if (equal(output, gConsts.getARRAY_A())) {
            return ClassHandling.get_object_vars(term);
        } else if (equal(output, gConsts.getARRAY_N())) {
            return Array.array_values(ClassHandling.get_object_vars(term));
        } else {
            return term;
        }
    }

    /**
     * get_term_children() - Merge all term children into a single array.
     * This recursive function will merge all of the children of $term into the
     * same array. Only useful for taxonomies which are hierarchical.
     * Will return an empty array if $term does not exist in $taxonomy.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @uses _get_term_hierarchy()
     * @uses get_term_children() Used to get the children of both $taxonomy and
     * the parent $term
     * @param string $term Name of Term to get children
     * @param string $taxonomy Taxonomy Name
     * @return array|WP_Error List of Term Objects. WP_Error returned if
     * $taxonomy does not exist
     */
    public Object get_term_children(Object term, String taxonomy) {
        Array<Object> terms = new Array<Object>();
        Array<Object> children = new Array<Object>();
        Object child = null;

        if (!is_taxonomy(taxonomy)) {
            return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid Taxonomy", "default"));
        }

        terms = _get_term_hierarchy(taxonomy);

        if (!isset(terms.getValue(term))) {
            return new Array<Object>();
        }

        children = terms.getArrayValue(term);

        for (Map.Entry javaEntry601 : (Set<Map.Entry>) terms.getArrayValue(term).entrySet()) {
            child = javaEntry601.getValue();

            if (isset(terms.getValue(child))) {
                children = Array.array_merge(children, (Array) get_term_children(child, taxonomy));
            }
        }

        return children;
    }

    /**
     * get_term_field() - Get sanitized Term field
     * Does checks for $term, based on the $taxonomy. The function is for
     * contextual reasons and for simplicity of usage. See sanitize_term_field()
     * for more information.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses sanitize_term_field() Passes the return value in
     * sanitize_term_field on success.
     * @param string $field Term field to fetch
     * @param int $term Term ID
     * @param string $taxonomy Taxonomy Name
     * @param string $context Optional, default is display. Look at
     * sanitize_term_field() for available options.
     * @return mixed Will return an empty string if $term is not an object or if
     * $field is not set in $term.
     */
    public Object get_term_field(String field, int termInt, String taxonomy, String context) {
        // term=intval(term);
        Object term =  /* Do not change type */get_term(termInt, taxonomy, gConsts.getOBJECT(), "raw");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(term)) {
            return term;
        }

        if (!is_object(term)) {
            return "";
        }

        if (!isset(((StdClass) term).fields.getValue(field))) {
            return "";
        }

        return sanitize_term_field(field, ((StdClass) term).fields.getValue(field), intval(((StdClass) term).fields.getValue("term_id")), taxonomy, context);
    }

    /**
     * get_term_to_edit() - Sanitizes Term for editing
     * Return value is sanitize_term() and usage is for sanitizing the term for
     * editing. Function is for contextual and simplicity.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses sanitize_term() Passes the return value on success
     * @param int|object $id Term ID or Object
     * @param string $taxonomy Taxonomy Name
     * @return mixed|null|WP_Error Will return empty string if $term is not an
     * object.
     */
    public Object get_term_to_edit(int id, String taxonomy) {
        Object term = null;

        /* Do not change type */
        term = get_term(id, taxonomy, gConsts.getOBJECT(), "raw");

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(term)) {
            return term;
        }

        if (!is_object(term)) {
            return "";
        }

        return sanitize_term(term, taxonomy, "edit");
    }

    /**
     * get_terms() - Retrieve the terms in taxonomy or list of taxonomies.
     *
     * You can fully inject any customizations to the query before it is sent, as well as control
     * the output with a filter.
     *
     * The 'get_terms' filter will be called when the cache has the term and will pass the found
     * term along with the array of $taxonomies and array of $args. This filter is also called
     * before the array of terms is passed and will pass the array of terms, along with the $taxonomies
     * and $args.
     *
     * The 'list_terms_exclusions' filter passes the compiled exclusions along with the $args.
     *
     * The list that $args can contain, which will overwrite the defaults.
     * orderby - Default is 'name'. Can be name, count, or nothing (will use term_id).
     * order - Default is ASC. Can use DESC.
     * hide_empty - Default is true. Will not return empty $terms.
     * fields - Default is all.
     * slug - Any terms that has this value. Default is empty string.
     * hierarchical - Whether to return hierarchical taxonomy. Default is true.
     * name__like - Default is empty string.
     *
     * The argument 'pad_counts' will count all of the children along with the $terms.
     *
     * The 'get' argument allows for overwriting 'hide_empty' and 'child_of', which can be done by
     * setting the value to 'all', instead of its default empty string value.
     *
     * The 'child_of' argument will be used if you use multiple taxonomy or the first $taxonomy
     * isn't hierarchical or 'parent' isn't used. The default is 0, which will be translated to
     * a false value. If 'child_of' is set, then 'child_of' value will be tested against
     * $taxonomy to see if 'child_of' is contained within. Will return an empty array if test
     * fails.
     *
     * If 'parent' is set, then it will be used to test against the first taxonomy. Much like
     * 'child_of'. Will return an empty array if the test fails.
     *
     * @package WordPress
     * @subpackage Taxonomy
     * @since 2.3
     *
     * @uses $wpdb
     * @uses wp_parse_args() Merges the defaults with those defined by $args and allows for strings.
     *
     *
     * @param string|array Taxonomy name or list of Taxonomy names
     * @param string|array $args The values of what to search for when returning terms
     * @return array|WP_Error List of Term Objects and their children. Will return WP_Error, if any of $taxonomies do not exist.
     */
    public Object get_terms(Object taxonomiesObj, /* Do not change type */
        Object args) {
        Array<Object> empty_array = new Array<Object>();
        Boolean single_taxonomy = null;
        String taxonomy = null;
        String in_taxonomies = null;
        Array<Object> defaults = new Array<Object>();
        Integer child_of = null;
        Object get = null;
        Array<Object> hierarchy = new Array<Object>();
        Integer parent = null;
        String key = null;
        Array<Object> cache = new Array<Object>();
        String orderby = null;
        String where = null;
        String inclusions = null;
        String include = null;
        String exclude = null;
        Array<Object> interms = new Array<Object>();
        Object interm = null;
        String exclusions = null;
        Array<Object> exterms = new Array<Object>();
        Object exterm = null;
        String slug = null;
        Object name__like = null;
        Object hide_empty = null;
        Object hierarchical = null;
        String number = null;
        Object offset = null;
        String search = null;
        String select_this = null;
        Object fields = null;
        String query = null;
        Object order = null;
        Array<Object> terms = new Array<Object>();
        Object children = null;
        Object pad_counts = null;
        StdClass term = null;
        StdClass child = null;
        Object k = null;
        empty_array = new Array<Object>();
        single_taxonomy = false;

        Array<?> taxonomies;

        if (!is_array(taxonomiesObj)) {
            single_taxonomy = true;
            taxonomies = new Array<Object>(new ArrayEntry<Object>(taxonomiesObj));
        } else {
            taxonomies = (Array) taxonomiesObj;
        }

        for (Map.Entry javaEntry602 : taxonomies.entrySet()) {
            taxonomy = strval(javaEntry602.getValue());

            if (!is_taxonomy(taxonomy)) {
                return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid Taxonomy", "default"));
            }
        }

        in_taxonomies = "\'" + Strings.implode("\', \'", taxonomies) + "\'";
        defaults = new Array<Object>(
                new ArrayEntry<Object>("orderby", "name"),
                new ArrayEntry<Object>("order", "ASC"),
                new ArrayEntry<Object>("hide_empty", true),
                new ArrayEntry<Object>("exclude", ""),
                new ArrayEntry<Object>("include", ""),
                new ArrayEntry<Object>("number", ""),
                new ArrayEntry<Object>("fields", "all"),
                new ArrayEntry<Object>("slug", ""),
                new ArrayEntry<Object>("parent", ""),
                new ArrayEntry<Object>("hierarchical", true),
                new ArrayEntry<Object>("child_of", 0),
                new ArrayEntry<Object>("get", ""),
                new ArrayEntry<Object>("name__like", ""),
                new ArrayEntry<Object>("pad_counts", false),
                new ArrayEntry<Object>("offset", ""),
                new ArrayEntry<Object>("search", ""));

        Array<Object> argsArray = (Array<Object>) getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        argsArray.putValue("number", getIncluded(FunctionsPage.class, gVars, gConsts).absint(argsArray.getValue("number")));
        argsArray.putValue("offset", getIncluded(FunctionsPage.class, gVars, gConsts).absint(argsArray.getValue("offset")));

        if (!single_taxonomy || !is_taxonomy_hierarchical(strval(taxonomies.getValue(0))) || !equal("", argsArray.getValue("parent"))) {
            argsArray.putValue("child_of", 0);
            argsArray.putValue("hierarchical", false);
            argsArray.putValue("pad_counts", false);
        }

        if (equal("all", argsArray.getValue("get"))) {
            argsArray.putValue("child_of", 0);
            argsArray.putValue("hide_empty", 0);
            argsArray.putValue("hierarchical", false);
            argsArray.putValue("pad_counts", false);
        }

        parent = intval(Array.extractVar(argsArray, "parent", parent, Array.EXTR_SKIP));
        orderby = strval(Array.extractVar(argsArray, "orderby", orderby, Array.EXTR_SKIP));
        include = strval(Array.extractVar(argsArray, "include", include, Array.EXTR_SKIP));
        exclude = strval(Array.extractVar(argsArray, "exclude", exclude, Array.EXTR_SKIP));
        child_of = intval(Array.extractVar(argsArray, "child_of", child_of, Array.EXTR_SKIP));
        slug = strval(Array.extractVar(argsArray, "slug", slug, Array.EXTR_SKIP));
        name__like = Array.extractVar(argsArray, "name__like", name__like, Array.EXTR_SKIP);
        hide_empty = Array.extractVar(argsArray, "hide_empty", hide_empty, Array.EXTR_SKIP);
        hierarchical = Array.extractVar(argsArray, "hierarchical", hierarchical, Array.EXTR_SKIP);
        get = Array.extractVar(argsArray, "get", get, Array.EXTR_SKIP);
        number = strval(Array.extractVar(argsArray, "number", number, Array.EXTR_SKIP));
        offset = Array.extractVar(argsArray, "offset", offset, Array.EXTR_SKIP);
        search = strval(Array.extractVar(argsArray, "search", search, Array.EXTR_SKIP));
        fields = Array.extractVar(argsArray, "fields", fields, Array.EXTR_SKIP);
        order = Array.extractVar(argsArray, "order", order, Array.EXTR_SKIP);
        terms = (Array<Object>) Array.extractVar(argsArray, "terms", terms, Array.EXTR_SKIP);
        pad_counts = Array.extractVar(argsArray, "pad_counts", pad_counts, Array.EXTR_SKIP);

        if (booleanval(child_of)) {
            hierarchy = _get_term_hierarchy(strval(taxonomies.getValue(0)));

            if (!isset(hierarchy.getValue(child_of))) {
                return empty_array;
            }
        }

        if (booleanval(parent)) {
            hierarchy = _get_term_hierarchy(strval(taxonomies.getValue(0)));

            if (!isset(hierarchy.getValue(parent))) {
                return empty_array;
            }
        }

        /* Modified by Numiton */
    	// $args can be whatever, only use the args defined in defaults to compute the key
        key = Strings.md5(
                serialize(
                    Array.compact(
                        new ArrayEntry<Object>("orderby", orderby),
                        new ArrayEntry<Object>("order", order),
                        new ArrayEntry<Object>("hide_empty", hide_empty),
                        new ArrayEntry<Object>("exclude", exclude),
                        new ArrayEntry<Object>("include", include),
                        new ArrayEntry<Object>("number", number),
                        new ArrayEntry<Object>("fields", fields),
                        new ArrayEntry<Object>("slug", slug),
                        new ArrayEntry<Object>("parent", parent),
                        new ArrayEntry<Object>("hierarchical", hierarchical),
                        new ArrayEntry<Object>("child_of", child_of),
                        new ArrayEntry<Object>("get", get),
                        new ArrayEntry<Object>("name__like", name__like),
                        new ArrayEntry<Object>("pad_counts", pad_counts),
                        new ArrayEntry<Object>("offset", offset),
                        new ArrayEntry<Object>("search", search))) + serialize(taxonomies));

        if (booleanval(cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get("get_terms", "terms"))) {
            if (isset(cache.getValue(key))) {
                return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_terms", cache.getValue(key), taxonomies, argsArray);
            }
        } else {
            cache = new Array<Object>();
        }

        if (equal("count", orderby)) {
            orderby = "tt.count";
        } else if (equal("name", orderby)) {
            orderby = "t.name";
        } else if (equal("slug", orderby)) {
            orderby = "t.slug";
        } else if (equal("term_group", orderby)) {
            orderby = "t.term_group";
        } else {
            orderby = "t.term_id";
        }

        where = "";
        inclusions = "";

        if (!empty(include)) {
            exclude = "";
            interms = QRegExPerl.preg_split("/[\\s,]+/", include);

            if (booleanval(Array.count(interms))) {
                for (Map.Entry javaEntry603 : interms.entrySet()) {
                    interm = javaEntry603.getValue();

                    if (empty(inclusions)) {
                        inclusions = " AND ( t.term_id = " + strval(interm) + " ";
                    } else {
                        inclusions = inclusions + " OR t.term_id = " + strval(interm) + " ";
                    }
                }
            }
        }

        if (!empty(inclusions)) {
            inclusions = inclusions + ")";
        }

        where = where + inclusions;
        exclusions = "";

        if (!empty(exclude)) {
            exterms = QRegExPerl.preg_split("/[\\s,]+/", exclude);

            if (booleanval(Array.count(exterms))) {
                for (Map.Entry javaEntry604 : exterms.entrySet()) {
                    exterm = javaEntry604.getValue();

                    if (empty(exclusions)) {
                        exclusions = " AND ( t.term_id <> " + strval(exterm) + " ";
                    } else {
                        exclusions = exclusions + " AND t.term_id <> " + strval(exterm) + " ";
                    }
                }
            }
        }

        if (!empty(exclusions)) {
            exclusions = exclusions + ")";
        }

        exclusions = strval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("list_terms_exclusions", exclusions, argsArray));
        where = where + exclusions;

        if (!empty(slug)) {
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(slug, "");
            where = where + " AND t.slug = \'" + slug + "\'";
        }

        if (!empty(name__like)) {
            where = where + " AND t.name LIKE \'" + strval(name__like) + "%\'";
        }

        if (!equal("", parent)) {
            parent = parent;
            where = where + " AND tt.parent = \'" + strval(parent) + "\'";
        }

        if (booleanval(hide_empty) && !booleanval(hierarchical)) {
            where = where + " AND tt.count > 0";
        }

        if (!empty(number)) {
            if (booleanval(offset)) {
                number = "LIMIT " + strval(offset) + "," + number;
            } else {
                number = "LIMIT " + number;
            }
        } else {
            number = "";
        }

        if (!empty(search)) {
            search = getIncluded(FormattingPage.class, gVars, gConsts).like_escape(search);
            where = where + " AND (t.name LIKE \'%" + search + "%\')";
        }

        select_this = "";

        if (equal("all", fields)) {
            select_this = "t.*, tt.*";
        } else if (equal("ids", fields)) {
            select_this = "t.term_id";
        } else if (equal("names", fields)) {
            select_this = "t.name";
        }

        query = "SELECT " + select_this + " FROM " + gVars.wpdb.terms + " AS t INNER JOIN " + gVars.wpdb.term_taxonomy + " AS tt ON t.term_id = tt.term_id WHERE tt.taxonomy IN (" + in_taxonomies +
            ") " + where + " ORDER BY " + orderby + " " + strval(order) + " " + number;

        if (equal("all", fields)) {
            terms = gVars.wpdb.get_results(query);
            update_term_cache(terms, "");
        } else if (equal("ids", fields) || equal("names", fields)) {
            terms = gVars.wpdb.get_col(query);
        }

        if (empty(terms)) {
            cache.putValue(key, new Array<Object>());
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("get_terms", cache, "terms", 0);

            return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_terms", new Array<Object>(), taxonomies, argsArray);
        }

        if (booleanval(child_of) || booleanval(hierarchical)) {
            children = _get_term_hierarchy(strval(taxonomies.getValue(0)));

            if (!empty(children)) {
                terms = (Array<Object>) _get_term_children(child_of, terms, strval(taxonomies.getValue(0)));
            }
        }

    	// Update term counts to include children.
        if (booleanval(pad_counts)) {
            _pad_term_counts(terms, strval(taxonomies.getValue(0)));
        }

    	// Make sure we show empty categories that have children.
        if (booleanval(hierarchical) && booleanval(hide_empty)) {
outer: 
            for (Map.Entry javaEntry605 : terms.entrySet()) {
                k = javaEntry605.getKey();
                term = (StdClass) javaEntry605.getValue();

                if (!booleanval(StdClass.getValue(term, "count")))/*
                 * It really is empty It really is empty
                 */
                 {
                    children = _get_term_children(intval(StdClass.getValue(term, "term_id")), terms, strval(taxonomies.getValue(0)));

                    for (Map.Entry javaEntry606 : ((Array<?>) children).entrySet()) {
                        child = (StdClass) javaEntry606.getValue();

                        if (booleanval(StdClass.getValue(child, "count"))) {
                            continue outer;
                        }
                    }

    				// It really is empty
                    terms.arrayUnset(k);
                }
            }
        }

        Array.reset(terms);
        cache.putValue(key, terms);
        getIncluded(CachePage.class, gVars, gConsts).wp_cache_set("get_terms", cache, "terms", 0);
        terms = (Array<Object>) getIncluded(PluginPage.class, gVars, gConsts).apply_filters("get_terms", terms, taxonomies, argsArray);

        return terms;
    }

    /**
     * is_term() - Check if Term exists
     * Returns the index of a defined term, or 0 (false) if the term doesn't
     * exist.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param int|string $term The term to check
     * @param string $taxonomy The taxonomy name to use
     * @return mixed Get the term id or Term Object, if exists.
     */
    public Object is_term(Object term, String taxonomy) {
        String where = null;

        if (is_int(term)) {
            if (equal(0, term)) {
                return 0;
            }

            where = gVars.wpdb.prepare("t.term_id = %d", term);
        } else {
            if (strictEqual("", term = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(strval(term), ""))) {
                return 0;
            }

            where = gVars.wpdb.prepare("t.slug = %s", term);
        }

        if (!empty(taxonomy)) {
            return gVars.wpdb.get_row(
                    "SELECT tt.term_id, tt.term_taxonomy_id FROM " + gVars.wpdb.terms + " AS t INNER JOIN " + gVars.wpdb.term_taxonomy + " as tt ON tt.term_id = t.term_id WHERE " + where +
                    " AND tt.taxonomy = \'" + taxonomy + "\'",
                    gConsts.getARRAY_A());
        }

        return gVars.wpdb.get_var("SELECT term_id FROM " + gVars.wpdb.terms + " as t WHERE " + where);
    }

    /**
     * sanitize_term() - Sanitize Term all fields
     * Relys on sanitize_term_field() to sanitize the term. The difference is
     * that this function will sanitize <strong>all</strong> fields. The
     * context is based on sanitize_term_field().
     * The $term is expected to be either an array or an object.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses sanitize_term_field Used to sanitize all fields in a term
     * @param array|object $term The term to check
     * @param string $taxonomy The taxonomy name to use
     * @param string $context Default is 'display'.
     * @return array|object Term with all fields sanitized
     */
    public Object sanitize_term(Object term, /* Do not change type */
        String taxonomy, String context) {
        Array<Object> fields = new Array<Object>();
        boolean do_object = false;
        Object field = null;

        if (equal("raw", context)) {
            return term;
        }

        fields = new Array<Object>(
                new ArrayEntry<Object>("term_id"),
                new ArrayEntry<Object>("name"),
                new ArrayEntry<Object>("description"),
                new ArrayEntry<Object>("slug"),
                new ArrayEntry<Object>("count"),
                new ArrayEntry<Object>("parent"),
                new ArrayEntry<Object>("term_group"));
        do_object = false;

        if (is_object(term)) {
            do_object = true;
        }

        for (Map.Entry javaEntry607 : fields.entrySet()) {
            field = javaEntry607.getValue();

            if (do_object) {
                ((StdClass) term).fields.putValue(
                    field,
                    sanitize_term_field(strval(field), ((StdClass) term).fields.getValue(field), intval(((StdClass) term).fields.getValue("term_id")), taxonomy, context));
            } else {
                ((Array) term).putValue(field, sanitize_term_field(strval(field), ((Array) term).getValue(field), intval(((Array) term).getValue("term_id")), taxonomy, context));
            }
        }

        return term;
    }

    /**
     * sanitize_term_field() - Cleanse the field value in the term based on
     * the context
     * Passing a term field value through the function should be assumed to have
     * cleansed the value for whatever context the term field is going to be
     * used.
     * If no context or an unsupported context is given, then default filters
     * will be applied.
     * There are enough filters for each context to support a custom filtering
     * without creating your own filter function. Simply create a function that
     * hooks into the filter you need.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param string $field Term field to sanitize
     * @param string $value Search for this term value
     * @param int $term_id Term ID
     * @param string $taxonomy Taxonomy Name
     * @param string $context Either edit, db, display, attribute, or js.
     * @return mixed sanitized field
     */
    public Object sanitize_term_field(String field, Object value, int term_id, String taxonomy, String context) {
        if (equal("parent", field) || equal("term_id", field) || equal("count", field) || equal("term_group", field)) {
            value = intval(value);

            if (intval(value) < 0) {
                value = 0;
            }
        }

        if (equal("raw", context)) {
            return value;
        }

        if (equal("edit", context)) {
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_term_" + field, value, term_id, taxonomy);
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("edit_" + taxonomy + "_" + field, value, term_id);

            if (equal("description", field)) {
                value = getIncluded(FormattingPage.class, gVars, gConsts).format_to_edit(strval(value), false);
            } else {
                value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(value));
            }
        } else if (equal("db", context)) {
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_term_" + field, value, taxonomy);
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_" + taxonomy + "_" + field, value);
    		// Back compat filters
            if (equal("slug", field)) {
                value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("pre_category_nicename", value);
            }
        } else if (equal("rss", context)) {
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_" + field + "_rss", value, taxonomy);
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(taxonomy + "_" + field + "_rss", value);
        } else {
    		// Use display filters by default.
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_" + field, value, term_id, taxonomy, context);
            value = getIncluded(PluginPage.class, gVars, gConsts).apply_filters(taxonomy + "_" + field, value, term_id, context);
        }

        if (equal("attribute", context)) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(value));
        } else if (equal("js", context)) {
            value = getIncluded(FormattingPage.class, gVars, gConsts).js_escape(strval(value));
        }

        return value;
    }

    /**
     * wp_count_terms() - Count how many terms are in Taxonomy
     * Default $args is 'ignore_empty' which can be
     * <code>'ignore_empty=true'</code> or
     * <code>array('ignore_empty' => true);</code>.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @uses wp_parse_args() Turns strings into arrays and merges defaults into
     * an array.
     * @param string $taxonomy Taxonomy name
     * @param array|string $args Overwrite defaults
     * @return int How many terms are in $taxonomy
     */
    public int wp_count_terms(String taxonomy, Object args) {
        Array<Object> defaults = new Array<Object>();
        String where = null;
        Object ignore_empty = null;
        defaults = new Array<Object>(new ArrayEntry<Object>("ignore_empty", false));
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        ignore_empty = Array.extractVar((Array) args, "ignore_empty", ignore_empty, Array.EXTR_SKIP);
        where = "";

        if (booleanval(ignore_empty)) {
            where = "AND count > 0";
        }

        taxonomy = gVars.wpdb.escape(taxonomy);

        return intval(gVars.wpdb.get_var("SELECT COUNT(*) FROM " + gVars.wpdb.term_taxonomy + " WHERE taxonomy = \'" + taxonomy + "\' " + where));
    }

    /**
     * wp_delete_object_term_relationships() - Will unlink the term from the
     * taxonomy
     * Will remove the term's relationship to the taxonomy, not the term or
     * taxonomy itself. The term and taxonomy will still exist. Will require the
     * term's object ID to perform the operation.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param int $object_id The term Object Id that refers to the term
     * @param string|array $taxonomy List of Taxonomy Names or single Taxonomy
     * name.
     */
    public void wp_delete_object_term_relationships(int object_id, Object taxonomies)/*
     * Do
     * not
     * change
     */
     {
        Array terms = null;
        Object taxonomy = null;
        String in_terms = null;
        object_id = object_id;

        if (!is_array(taxonomies)) {
            taxonomies = new Array<Object>(new ArrayEntry<Object>(taxonomies));
        }

        for (Map.Entry javaEntry608 : ((Array<?>) taxonomies).entrySet()) {
            taxonomy = javaEntry608.getValue();
            terms = (Array<Object>) wp_get_object_terms(object_id, taxonomy, "fields=tt_ids");
            in_terms = "\'" + Strings.implode("\', \'", terms) + "\'";
            gVars.wpdb.query("DELETE FROM " + gVars.wpdb.term_relationships + " WHERE object_id = \'" + object_id + "\' AND term_taxonomy_id IN (" + in_terms + ")");
            wp_update_term_count(terms, strval(taxonomy), false);
        }
    }

    /**
     * wp_delete_term() - Removes a term from the database.
     * If the term is a parent of other terms, then the children will be updated
     * to that term's parent.
     * The $args 'default' will only override the terms found, if there is only
     * one term found. Any other and the found terms are used.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @uses do_action() Calls both 'delete_term' and 'delete_$taxonomy' action
     * hooks, passing term object, term id. 'delete_term' gets an
     * additional parameter with the $taxonomy parameter.
     * @param int $term Term ID
     * @param string $taxonomy Taxonomy Name
     * @param array|string $args Optional. Change 'default' term id and override
     * found term ids.
     * @return bool|WP_Error Returns false if not term; true if completes delete
     * action.
     */
    public Object wp_delete_term(int term, String taxonomy, Object args) {
        Object ids;
        int tt_id;
        Array<Object> defaults = new Array<Object>();
        Integer _default = null;
        Object term_obj = null;
        int parent;
        Array<Object> objects = new Array<Object>();
        Array<Object> terms = new Array<Object>();
        Object object = null;
        term = term;

        if (!booleanval(ids = is_term(term, taxonomy))) {
            return false;
        }

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(ids)) {
            return ids;
        }

        tt_id = intval(((Array) ids).getValue("term_taxonomy_id"));
        defaults = new Array<Object>();

        Array argsArray = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        _default = intval(Array.extractVar(argsArray, "default", _default, Array.EXTR_SKIP));

        if (isset(_default)) {
            _default = _default;

            if (!booleanval(is_term(_default, taxonomy))) {
                _default = null;
            }
        }

    	// Update children to point to new parent
        if (is_taxonomy_hierarchical(taxonomy)) {
            term_obj = get_term(term, taxonomy, gConsts.getOBJECT(), "raw");

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(term_obj)) {
                return term_obj;
            }

            parent = intval(((StdClass) term_obj).fields.getValue("parent"));
            gVars.wpdb.update(gVars.wpdb.term_taxonomy, Array.compact(new ArrayEntry("parent", parent)),
                Array.arrayAppend(new Array<Object>(new ArrayEntry<Object>("parent", ((StdClass) term_obj).fields.getValue("term_id"))), Array.compact(new ArrayEntry("taxonomy", taxonomy))));
        }

        objects = gVars.wpdb.get_col(gVars.wpdb.prepare("SELECT object_id FROM " + gVars.wpdb.term_relationships + " WHERE term_taxonomy_id = %d", tt_id));

        for (Map.Entry javaEntry609 : new Array<Object>(objects).entrySet()) {
            object = javaEntry609.getValue();
            terms = (Array<Object>) wp_get_object_terms(strval(object), taxonomy, "fields=ids");

            if (equal(1, Array.count(terms)) && isset(_default)) {
                terms = new Array<Object>(new ArrayEntry<Object>(_default));
            } else {
                terms = Array.array_diff(terms, new Array<Object>(new ArrayEntry<Object>(term)));
            }

            terms = Array.array_map(new Callback("intval", VarHandling.class), terms);
            wp_set_object_terms(intval(object), terms, taxonomy, false);
        }

        gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.term_taxonomy + " WHERE term_taxonomy_id = %d", tt_id));

    	// Delete the term if no taxonomies use it.
        if (!booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT COUNT(*) FROM " + gVars.wpdb.term_taxonomy + " WHERE term_id = %d", term)))) {
            gVars.wpdb.query(gVars.wpdb.prepare("DELETE FROM " + gVars.wpdb.terms + " WHERE term_id = %d", term));
        }

        clean_term_cache(term, taxonomy);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_term", term, tt_id, taxonomy);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("delete_" + taxonomy, term, tt_id);

        return true;
    }

    /**
     * wp_get_object_terms() - Retrieves the terms associated with the given
     * object(s), in the supplied taxonomies.
     * The following information has to do the $args parameter and for what can
     * be contained in the string or array of that parameter, if it exists.
     * The first argument is called, 'orderby' and has the default value of
     * 'name'. The other value that is supported is 'count'.
     * The second argument is called, 'order' and has the default value of
     * 'ASC'. The only other value that will be acceptable is 'DESC'.
     * The final argument supported is called, 'fields' and has the default
     * value of 'all'. There are multiple other options that can be used
     * instead. Supported values are as follows: 'all', 'ids', 'names', and
     * finally 'all_with_object_id'.
     * The fields argument also decides what will be returned. If 'all' or
     * 'all_with_object_id' is choosen or the default kept intact, then all
     * matching terms objects will be returned. If either 'ids' or 'names' is
     * used, then an array of all matching term ids or term names will be
     * returned respectively.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param int|array $object_id The id of the object(s) to retrieve.
     * @param string|array $taxonomies The taxonomies to retrieve terms from.
     * @param array|string $args Change what is returned
     * @return array|WP_Error The requested term data or empty array if no terms
     * found. WP_Error if $taxonomy does not exist.
     */
    public Object wp_get_object_terms(Object object_idsObj, /* Do not change type */
        Object taxonomiesObj, /* Do not change type */
        Object args) {
        String taxonomy = null;
        Array<Object> defaults = new Array<Object>();
        Array<Object> terms = new Array<Object>();
        StdClass t = null;
        Object index = null;
        String orderby = null;
        String select_this = null;
        Object fields = null;
        String query = null;
        Object order = null;
        Array taxonomies;

        if (!is_array(taxonomiesObj)) {
            taxonomies = new Array<Object>(new ArrayEntry<Object>(taxonomiesObj));
        } else {
            taxonomies = (Array) taxonomiesObj;
        }

        for (Map.Entry javaEntry610 : ((Array<?>) taxonomies).entrySet()) {
            taxonomy = strval(javaEntry610.getValue());

            if (!is_taxonomy(taxonomy)) {
                return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid Taxonomy", "default"));
            }
        }

        /* Modified by Numiton */
        Array object_ids;

        if (!is_array(object_idsObj)) {
            object_ids = new Array<Object>(new ArrayEntry<Object>(object_idsObj));
        } else {
            object_ids = (Array) object_idsObj;
        }

        object_ids = Array.array_map(new Callback("intval", VarHandling.class), object_ids);
        defaults = new Array<Object>(new ArrayEntry<Object>("orderby", "name"), new ArrayEntry<Object>("order", "ASC"), new ArrayEntry<Object>("fields", "all"));
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        terms = new Array<Object>();

        if (Array.count(taxonomies) > 1) {
            for (Map.Entry javaEntry611 : ((Array<?>) taxonomies).entrySet()) {
                index = javaEntry611.getKey();
                taxonomy = strval(javaEntry611.getValue());
                t = (StdClass) get_taxonomy(taxonomy);

                if (is_array(StdClass.getValue(t, "args")) && !equal(args, Array.array_merge((Array) args, t.fields.getArrayValue("args")))) {
                    ((Array) taxonomies).arrayUnset(index);
                    terms = Array.array_merge(terms, (Array) wp_get_object_terms(object_ids, taxonomy, Array.array_merge((Array) args, t.fields.getArrayValue("args"))));
                }
            }
        } else {
            t = (StdClass) get_taxonomy(strval(taxonomies.getValue(0)));

            if (is_array(StdClass.getValue(t, "args"))) {
                args = Array.array_merge((Array) args, t.fields.getArrayValue("args"));
            }
        }

        {
            Array argsArray = (Array) args;
            orderby = strval(Array.extractVar(argsArray, "orderby", orderby, Array.EXTR_SKIP));
            fields = Array.extractVar(argsArray, "fields", fields, Array.EXTR_SKIP);
            order = Array.extractVar(argsArray, "order", order, Array.EXTR_SKIP);
        }

        if (equal("count", orderby)) {
            orderby = "tt.count";
        } else if (equal("name", orderby)) {
            orderby = "t.name";
        } else if (equal("slug", orderby)) {
            orderby = "t.slug";
        } else if (equal("term_group", orderby)) {
            orderby = "t.term_group";
        } else if (equal("term_order", orderby)) {
            orderby = "tr.term_order";
        } else {
            orderby = "t.term_id";
        }

        String taxonomiesStr = "\'" + Strings.implode("\', \'", taxonomies) + "\'";
        String object_idsStr = Strings.implode(", ", (Array) object_ids);
        select_this = "";

        if (equal("all", fields)) {
            select_this = "t.*, tt.*";
        } else if (equal("ids", fields)) {
            select_this = "t.term_id";
        } else if (equal("names", fields)) {
            select_this = "t.name";
        } else if (equal("all_with_object_id", fields)) {
            select_this = "t.*, tt.*, tr.object_id";
        }

        query = "SELECT " + select_this + " FROM " + gVars.wpdb.terms + " AS t INNER JOIN " + gVars.wpdb.term_taxonomy + " AS tt ON tt.term_id = t.term_id INNER JOIN " +
            gVars.wpdb.term_relationships + " AS tr ON tr.term_taxonomy_id = tt.term_taxonomy_id WHERE tt.taxonomy IN (" + taxonomiesStr + ") AND tr.object_id IN (" + object_idsStr + ") ORDER BY " +
            orderby + " " + strval(order);

        if (equal("all", fields) || equal("all_with_object_id", fields)) {
            terms = Array.array_merge(terms, gVars.wpdb.get_results(query));
            update_term_cache(terms, "");
        } else if (equal("ids", fields) || equal("names", fields)) {
            terms = Array.array_merge(terms, gVars.wpdb.get_col(query));
        } else if (equal("tt_ids", fields)) {
            terms = gVars.wpdb.get_col(
                        "SELECT tr.term_taxonomy_id FROM " + gVars.wpdb.term_relationships + " AS tr INNER JOIN " + gVars.wpdb.term_taxonomy +
                        " AS tt ON tr.term_taxonomy_id = tt.term_taxonomy_id WHERE tr.object_id IN (" + object_idsStr + ") AND tt.taxonomy IN (" + taxonomiesStr + ") ORDER BY tr.term_taxonomy_id " +
                        order);
        }

        if (!booleanval(terms)) {
            return new Array<Object>();
        }

        return terms;
    }

    /**
     * wp_insert_term() - Adds a new term to the database. Optionally marks it as an alias of an existing term.
     *
     * Error handling is assigned for the nonexistance of the $taxonomy and $term parameters before inserting.
     * If both the term id and taxonomy exist previously, then an array will be returned that contains the term
     * id and the contents of what is returned. The keys of the array are 'term_id' and 'term_taxonomy_id' containing
     * numeric values.
     *
     * It is assumed that the term does not yet exist or the above will apply. The term will be first added to the term
     * table and then related to the taxonomy if everything is well. If everything is correct, then several actions
     * will be run prior to a filter and then several actions will be run after the filter is run.
     *
     * The arguments decide how the term is handled based on the $args parameter. The following
     * is a list of the available overrides and the defaults.
     *
     * 'alias_of'. There is no default, but if added, expected is the slug that the term will be an alias of.
     * Expected to be a string.
     *
     * 'description'. There is no default. If exists, will be added to the database along with the term. Expected
     * to be a string.
     *
     * 'parent'. Expected to be numeric and default is 0 (zero). Will assign value of 'parent' to the term.
     *
     * 'slug'. Expected to be a string. There is no default.
     *
     * If 'slug' argument exists then the slug will be checked to see if it is not a valid term. If that check
     * succeeds (it is not a valid term), then it is added and the term id is given. If it fails, then a check
     * is made to whether the taxonomy is hierarchical and the parent argument is not empty. If the second check
     * succeeds, the term will be inserted and the term id will be given.
     *
     * @package WordPress
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     *
     * @uses do_action() Calls 'create_term' hook with the term id and taxonomy id as parameters.
     * @uses do_action() Calls 'create_$taxonomy' hook with term id and taxonomy id as parameters.
     * @uses apply_filters() Calls 'term_id_filter' hook with term id and taxonomy id as parameters.
     * @uses do_action() Calls 'created_term' hook with the term id and taxonomy id as parameters.
     * @uses do_action() Calls 'created_$taxonomy' hook with term id and taxonomy id as parameters.
     *
     * @param int|string $term The term to add or update.
     * @param string $taxonomy The taxonomy to which to add the term
     * @param array|string $args Change the values of the inserted term
     * @return array|WP_Error The Term ID and Term Taxonomy ID
     */
    public Object wp_insert_term(Object term, String taxonomy, Object args) {
        Array<Object> defaults = new Array<Object>();
        String name = null;
        String description = null;
        String slug = null;
        Integer term_group = null;
        Object alias_of = null;
        StdClass alias;
        int term_id = 0;
        Object parent = null;
        Integer tt_id = null;

        if (!is_taxonomy(taxonomy)) {
            return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid taxonomy", "default"));
        }

        if (is_int(term) && equal(0, term)) {
            return new WP_Error(gVars, gConsts, "invalid_term_id", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid term ID", "default"));
        }

        defaults = new Array<Object>(new ArrayEntry<Object>("alias_of", ""), new ArrayEntry<Object>("description", ""), new ArrayEntry<Object>("parent", 0), new ArrayEntry<Object>("slug", ""));

        Array argsArray = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        argsArray.putValue("name", term);
        argsArray.putValue("taxonomy", taxonomy);
        argsArray = (Array<Object>) sanitize_term(argsArray, taxonomy, "db");
        name = strval(Array.extractVar(argsArray, "name", name, Array.EXTR_SKIP));
        description = strval(Array.extractVar(argsArray, "description", description, Array.EXTR_SKIP));
        slug = strval(Array.extractVar(argsArray, "slug", slug, Array.EXTR_SKIP));
        alias_of = Array.extractVar(argsArray, "alias_of", alias_of, Array.EXTR_SKIP);
        parent = Array.extractVar(argsArray, "parent", parent, Array.EXTR_SKIP);
        
    	// expected_slashed ($name)
        name = Strings.stripslashes(gVars.webEnv, name);
        description = Strings.stripslashes(gVars.webEnv, description);

        if (empty(slug)) {
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(name, "");
        }

        term_group = 0;

        if (booleanval(alias_of)) {
            alias = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT term_id, term_group FROM " + gVars.wpdb.terms + " WHERE slug = %s", alias_of));

            if (booleanval(StdClass.getValue(alias, "term_group"))) {
    			// The alias we want is already in a group, so let's use that one.
                term_group = intval(StdClass.getValue(alias, "term_group"));
            } else {
    			// The alias isn't in a group, so let's create a new one and firstly add the alias term to it.
                term_group = intval(gVars.wpdb.get_var("SELECT MAX(term_group) FROM " + gVars.wpdb.terms)) + 1;
                gVars.wpdb.query(gVars.wpdb.prepare("UPDATE " + gVars.wpdb.terms + " SET term_group = %d WHERE term_id = %d", term_group, StdClass.getValue(alias, "term_id")));
            }
        }

        if (!booleanval(term_id = intval(is_term(slug, "")))) {
            if (strictEqual(BOOLEAN_FALSE, gVars.wpdb.insert(gVars.wpdb.terms, Array.compact(new ArrayEntry("name", name), new ArrayEntry("slug", slug), new ArrayEntry("term_group", term_group))))) {
                return new WP_Error(gVars, gConsts, "db_insert_error", getIncluded(L10nPage.class, gVars, gConsts).__("Could not insert term into the database", "default"), gVars.wpdb.last_error);
            }

            term_id = gVars.wpdb.insert_id;
        } else if (is_taxonomy_hierarchical(taxonomy) && !empty(parent)) {
    		// If the taxonomy supports hierarchy and the term has a parent, make the slug unique
    		// by incorporating parent slugs.
            slug = wp_unique_term_slug(slug, Array.toStdClass(argsArray));

            if (strictEqual(BOOLEAN_FALSE, gVars.wpdb.insert(gVars.wpdb.terms, Array.compact(new ArrayEntry("name", name), new ArrayEntry("slug", slug), new ArrayEntry("term_group", term_group))))) {
                return new WP_Error(gVars, gConsts, "db_insert_error", getIncluded(L10nPage.class, gVars, gConsts).__("Could not insert term into the database", "default"), gVars.wpdb.last_error);
            }

            term_id = gVars.wpdb.insert_id;
        }

        if (empty(slug)) {
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(slug, strval(term_id));
            gVars.wpdb.update(gVars.wpdb.terms, Array.compact(new ArrayEntry("slug", slug)), Array.compact(new ArrayEntry("term_id", term_id)));
        }

        tt_id = intval(
                    gVars.wpdb.get_var(
                            gVars.wpdb.prepare(
                                    "SELECT tt.term_taxonomy_id FROM " + gVars.wpdb.term_taxonomy + " AS tt INNER JOIN " + gVars.wpdb.terms +
                                    " AS t ON tt.term_id = t.term_id WHERE tt.taxonomy = %s AND t.term_id = %d",
                                    taxonomy,
                                    term_id)));

        if (!empty(tt_id)) {
            return new Array<Object>(new ArrayEntry<Object>("term_id", term_id), new ArrayEntry<Object>("term_taxonomy_id", tt_id));
        }

        gVars.wpdb.insert(
            gVars.wpdb.term_taxonomy,
            Array.arrayAppend(
                Array.compact(new ArrayEntry("term_id", term_id), new ArrayEntry("taxonomy", taxonomy), new ArrayEntry("description", description), new ArrayEntry("parent", parent)),
                new Array<Object>(new ArrayEntry<Object>("count", 0))));
        tt_id = gVars.wpdb.insert_id;
        getIncluded(PluginPage.class, gVars, gConsts).do_action("create_term", term_id, tt_id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("create_" + taxonomy, term_id, tt_id);
        term_id = intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_id_filter", term_id, tt_id));
        clean_term_cache(term_id, taxonomy);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("created_term", term_id, tt_id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("created_" + taxonomy, term_id, tt_id);

        return new Array<Object>(new ArrayEntry<Object>("term_id", term_id), new ArrayEntry<Object>("term_taxonomy_id", tt_id));
    }

    /**
     * wp_set_object_terms() - Create Term and Taxonomy Relationships
     * Relates an object (post, link etc) to a term and taxonomy type. Creates
     * the term and taxonomy relationship if it doesn't already exist. Creates a
     * term if it doesn't exist (using the slug).
     * A relationship means that the term is grouped in or belongs to the
     * taxonomy. A term has no meaning until it is given context by defining
     * which taxonomy it exists under.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param int $object_id The object to relate to.
     * @param array|int|string $term The slug or id of the term.
     * @param array|string $taxonomy The context in which to relate the term to
     * the object.
     * @param bool $append If false will delete difference of terms.
     * @return array|WP_Error Affected Term IDs
     */
    public Object wp_set_object_terms(int object_id, Object terms, /* Do not change */
        String taxonomy, boolean append) {
        Array<Object> old_terms = new Array<Object>();
        Array<Object> tt_ids = new Array<Object>();
        Array<Object> term_ids = new Array<Object>();
        String term = null;
        Object id;
        Array delete_terms = new Array();
        String in_delete_terms = null;
        StdClass t = null;
        Array<String> values = new Array<String>();
        int term_order = 0;
        Array<Object> final_term_ids = null;
        Object term_id = null;
        object_id = object_id;

        if (!is_taxonomy(taxonomy)) {
            return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid Taxonomy", "default"));
        }

        if (!is_array(terms)) {
            terms = new Array<Object>(new ArrayEntry<Object>(terms));
        }

        if (!append) {
            old_terms = (Array<Object>) wp_get_object_terms(object_id, taxonomy, "fields=tt_ids");
        }

        tt_ids = new Array<Object>();
        term_ids = new Array<Object>();

        for (Map.Entry javaEntry612 : ((Array<?>) terms).entrySet()) {
            term = strval(javaEntry612.getValue());

            if (!booleanval(Strings.strlen(Strings.trim(term)))) {
                continue;
            }

            if (!booleanval(id = is_term(term, taxonomy))) {
                id = wp_insert_term(term, taxonomy, new Array<Object>());
            }

            if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(id)) {
                return id;
            }

            term_ids.putValue(((Array) id).getValue("term_id"));
            id = intval(((Array) id).getValue("term_taxonomy_id"));
            tt_ids.putValue(id);

            if (booleanval(gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT term_taxonomy_id FROM " + gVars.wpdb.term_relationships + " WHERE object_id = %d AND term_taxonomy_id = %d", object_id, id)))) {
                continue;
            }

            gVars.wpdb.insert(gVars.wpdb.term_relationships, new Array<Object>(new ArrayEntry<Object>("object_id", object_id), new ArrayEntry<Object>("term_taxonomy_id", id)));
        }

        wp_update_term_count(tt_ids, taxonomy, false);

        if (!append) {
            delete_terms = Array.array_diff(old_terms, tt_ids);

            if (booleanval(delete_terms)) {
                in_delete_terms = "\'" + Strings.implode("\', \'", delete_terms) + "\'";
                gVars.wpdb.query("DELETE FROM " + gVars.wpdb.term_relationships + " WHERE object_id = \'" + object_id + "\' AND term_taxonomy_id IN (" + in_delete_terms + ")");
                wp_update_term_count(delete_terms, taxonomy, false);
            }
        }

        t = (StdClass) get_taxonomy(taxonomy);

        if (!append && isset(StdClass.getValue(t, "sort")) && booleanval(StdClass.getValue(t, "sort"))) {
            values = new Array<String>();
            term_order = 0;
            final_term_ids = (Array<Object>) wp_get_object_terms(object_id, taxonomy, "fields=tt_ids");

            for (Map.Entry javaEntry613 : term_ids.entrySet()) {
                term_id = javaEntry613.getValue();

                if (Array.in_array(term_id, final_term_ids)) {
                    values.putValue(gVars.wpdb.prepare("(%d, %d, %d)", object_id, term_id, ++term_order));
                }
            }

            if (booleanval(values)) {
                gVars.wpdb.query(
                        "INSERT INTO " + gVars.wpdb.term_relationships + " (object_id, term_taxonomy_id, term_order) VALUES " + Strings.join(",", values) +
                        " ON DUPLICATE KEY UPDATE term_order = VALUES(term_order)");
            }
        }

        return tt_ids;
    }

    /**
     * wp_unique_term_slug() - Will make slug unique, if it isn't already
     * The $slug has to be unique global to every taxonomy, meaning that one
     * taxonomy term can't have a matching slug with another taxonomy term. Each
     * slug has to be globally unique for every taxonomy.
     * The way this works is that if the taxonomy that the term belongs to is
     * heirarchical and has a parent, it will append that parent to the $slug.
     * If that still doesn't return an unique slug, then it try to append a
     * number until it finds a number that is truely unique.
     * The only purpose for $term is for appending a parent, if one exists.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param string $slug The string that will be tried for a unique slug
     * @param object $term The term object that the $slug will belong too
     * @return string Will return a true unique slug.
     */
    public String wp_unique_term_slug(String slug, StdClass term) {
        Object the_parent = null;
        StdClass parent_term = null;
        Array<Object> args = new Array<Object>();
        String query = null;
        int num = 0;
        String alt_slug = null;
        Object slug_check = null;

    	// If the taxonomy supports hierarchy and the term has a parent, make the slug unique
    	// by incorporating parent slugs.
        if (is_taxonomy_hierarchical(strval(StdClass.getValue(term, "taxonomy"))) && !empty(StdClass.getValue(term, "parent"))) {
            the_parent = StdClass.getValue(term, "parent");

            while (!empty(the_parent)) {
                parent_term = (StdClass) get_term(the_parent, strval(StdClass.getValue(term, "taxonomy")), gConsts.getOBJECT(), "raw");

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(parent_term) || empty(parent_term)) {
                    break;
                }

                slug = slug + "-" + StdClass.getValue(parent_term, "slug");

                if (empty(StdClass.getValue(parent_term, "parent"))) {
                    break;
                }

                the_parent = StdClass.getValue(parent_term, "parent");
            }
        }

    	// If we didn't get a unique slug, try appending a number to make it unique.
        if (!empty(args.getValue("term_id"))) {
            query = gVars.wpdb.prepare("SELECT slug FROM " + gVars.wpdb.terms + " WHERE slug = %s AND term_id != %d", slug, args.getValue("term_id"));
        } else {
            query = gVars.wpdb.prepare("SELECT slug FROM " + gVars.wpdb.terms + " WHERE slug = %s", slug);
        }

        if (booleanval(gVars.wpdb.get_var(query))) {
            num = 2;

            do {
                alt_slug = slug + "-" + strval(num);
                num++;
                slug_check = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT slug FROM " + gVars.wpdb.terms + " WHERE slug = %s", alt_slug));
            } while (booleanval(slug_check));

            slug = alt_slug;
        }

        return slug;
    }

    /**
     * wp_update_term() - Update term based on arguments provided
     * The $args will indiscriminately override all values with the same field
     * name. Care must be taken to not override important information need to
     * update or update will fail (or perhaps create a new term, neither would
     * be acceptable).
     * Defaults will set 'alias_of', 'description', 'parent', and 'slug' if not
     * defined in $args already.
     * 'alias_of' will create a term group, if it doesn't already exist, and
     * update it for the $term.
     * If the 'slug' argument in $args is missing, then the 'name' in $args will
     * be used. It should also be noted that if you set 'slug' and it isn't
     * unique then a WP_Error will be passed back. If you don't pass any slug,
     * then a unique one will be created for you.
     * For what can be overrode in $args, check the term scheme can contain and
     * stay away from the term keys.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @uses do_action() Will call both 'edit_term' and 'edit_$taxonomy' twice.
     * @uses apply_filters() Will call the 'term_id_filter' filter and pass the
     * term id and taxonomy id.
     * @param int $term The ID of the term
     * @param string $taxonomy The context in which to relate the term to the
     * object.
     * @param array|string $args Overwrite term field values
     * @return array|WP_Error Returns Term ID and Taxonomy Term ID
     */
    public Object wp_update_term(int termId, String taxonomy, Array<Object> args) {
        int term_id;
        Array<Object> defaults = new Array<Object>();
        String name = null;
        String description = null;
        Boolean empty_slug = null;
        String slug = null;
        Object alias_of = null;
        StdClass alias;
        Integer term_group = null;
        Object id = null;
        Object parent = null;
        String tt_id = null;

        if (!is_taxonomy(taxonomy)) {
            return new WP_Error(gVars, gConsts, "invalid_taxonomy", getIncluded(L10nPage.class, gVars, gConsts).__("Invalid taxonomy", "default"));
        }

        term_id = termId;

    	// First, get all of the original args
        Array term = (Array) get_term(term_id, taxonomy, gConsts.getARRAY_A(), "raw");
        
    	// Escape data pulled from DB.
        term = getIncluded(FunctionsPage.class, gVars, gConsts).add_magic_quotes(term);
        
    	// Merge old and new args with new args overwriting old ones.
        args = Array.array_merge(term, args);
        
        defaults = new Array<Object>(new ArrayEntry<Object>("alias_of", ""), new ArrayEntry<Object>("description", ""), new ArrayEntry<Object>("parent", 0), new ArrayEntry<Object>("slug", ""));
        args = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        args = (Array<Object>) sanitize_term(args, taxonomy, "db");
        name = strval(Array.extractVar(args, "name", name, Array.EXTR_SKIP));
        description = strval(Array.extractVar(args, "description", description, Array.EXTR_SKIP));
        slug = strval(Array.extractVar(args, "slug", slug, Array.EXTR_SKIP));
        alias_of = Array.extractVar(args, "alias_of", alias_of, Array.EXTR_SKIP);
        term_group = intval(Array.extractVar(args, "term_group", term_group, Array.EXTR_SKIP));
        parent = Array.extractVar(args, "parent", parent, Array.EXTR_SKIP);
        
    	// expected_slashed ($name)
        name = Strings.stripslashes(gVars.webEnv, name);
        description = Strings.stripslashes(gVars.webEnv, description);
        empty_slug = false;

        if (empty(slug)) {
            empty_slug = true;
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(name, "");
        }

        if (booleanval(alias_of)) {
            alias = (StdClass) gVars.wpdb.get_row(gVars.wpdb.prepare("SELECT term_id, term_group FROM " + gVars.wpdb.terms + " WHERE slug = %s", alias_of));

            if (booleanval(StdClass.getValue(alias, "term_group"))) {
    			// The alias we want is already in a group, so let's use that one.
                term_group = intval(StdClass.getValue(alias, "term_group"));
            } else {
    			// The alias isn't in a group, so let's create a new one and firstly add the alias term to it.
                term_group = intval(gVars.wpdb.get_var("SELECT MAX(term_group) FROM " + gVars.wpdb.terms)) + 1;
                gVars.wpdb.update(gVars.wpdb.terms, Array.compact(new ArrayEntry("term_group", term_group)), new Array<Object>(new ArrayEntry<Object>("term_id", StdClass.getValue(alias, "term_id"))));
            }
        }

    	// Check for duplicate slug
        id = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT term_id FROM " + gVars.wpdb.terms + " WHERE slug = %s", slug));

        if (booleanval(id) && !equal(id, term_id)) {
    		// If an empty slug was passed or the parent changed, reset the slug to something unique.
    		// Otherwise, bail.
            if (empty_slug)/* Commented by Numiton: BUG || !equal(parent, term.parent)*/
             {
                slug = wp_unique_term_slug(slug, Array.toStdClass(args));
            } else {
                return new WP_Error(gVars, gConsts, "duplicate_term_slug",
                    QStrings.sprintf(getIncluded(L10nPage.class, gVars, gConsts).__("The slug \"%s\" is already in use by another term", "default"), slug));
            }
        }

        gVars.wpdb.update(gVars.wpdb.terms, Array.compact(new ArrayEntry("name", name), new ArrayEntry("slug", slug), new ArrayEntry("term_group", term_group)),
            Array.compact(new ArrayEntry("term_id", term_id)));

        if (empty(slug)) {
            slug = getIncluded(FormattingPage.class, gVars, gConsts).sanitize_title(name, strval(term_id));
            gVars.wpdb.update(gVars.wpdb.terms, Array.compact(new ArrayEntry("slug", slug)), Array.compact(new ArrayEntry("term_id", term_id)));
        }

        tt_id = strval(
                    gVars.wpdb.get_var(
                            gVars.wpdb.prepare(
                                    "SELECT tt.term_taxonomy_id FROM " + gVars.wpdb.term_taxonomy + " AS tt INNER JOIN " + gVars.wpdb.terms +
                                    " AS t ON tt.term_id = t.term_id WHERE tt.taxonomy = %s AND t.term_id = %d",
                                    taxonomy,
                                    term_id)));
        gVars.wpdb.update(
            gVars.wpdb.term_taxonomy,
            Array.compact(new ArrayEntry("term_id", term_id), new ArrayEntry("taxonomy", taxonomy), new ArrayEntry("description", description), new ArrayEntry("parent", parent)),
            new Array<Object>(new ArrayEntry<Object>("term_taxonomy_id", tt_id)));
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_term", term_id, tt_id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edit_" + taxonomy, term_id, tt_id);
        term_id = intval(getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_id_filter", term_id, tt_id));
        clean_term_cache(term_id, taxonomy);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edited_term", term_id, tt_id);
        getIncluded(PluginPage.class, gVars, gConsts).do_action("edited_" + taxonomy, term_id, tt_id);

        return new Array<Object>(new ArrayEntry<Object>("term_id", term_id), new ArrayEntry<Object>("term_taxonomy_id", tt_id));
    }

 // enable or disable term count deferring
 // if no value is supplied, the current value of the defer setting is returned
    public boolean wp_defer_term_counting(Object defer) {
        if (is_bool(defer)) {
            wp_defer_term_counting__defer = booleanval(defer);
    		// flush any deferred counts
            if (!booleanval(defer)) {
                wp_update_term_count(null, strval(null), true);
            }
        }

        return wp_defer_term_counting__defer;
    }

    /**
     * wp_update_term_count() - Updates the amount of terms in taxonomy
     * If there is a taxonomy callback applyed, then it will be called for
     * updating the count.
     * The default action is to count what the amount of terms have the
     * relationship of term ID. Once that is done, then update the database.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param int|array $terms The ID of the terms
     * @param string $taxonomy The context of the term.
     * @return bool If no terms will return false, and if successful will return
     * true.
     */
    public boolean wp_update_term_count(Object terms, /* Do not change type */
        String taxonomy, boolean do_deferred) {
        Object tax = null;

        if (do_deferred) {
            for (Map.Entry javaEntry614 : Array.array_keys(wp_update_term_count__deferred).entrySet()) {
                tax = javaEntry614.getValue();
                wp_update_term_count_now(wp_update_term_count__deferred.getArrayValue(tax), strval(tax));
                wp_update_term_count__deferred.arrayUnset(tax);
            }
        }

        if (empty(terms)) {
            return false;
        }

        if (!is_array(terms)) {
            terms = new Array<Object>(new ArrayEntry<Object>(terms));
        }

        if (wp_defer_term_counting(null)) {
            if (!isset(wp_update_term_count__deferred.getValue(taxonomy))) {
                wp_update_term_count__deferred.putValue(taxonomy, new Array<Object>());
            }

            wp_update_term_count__deferred.putValue(taxonomy, Array.array_unique(Array.array_merge(wp_update_term_count__deferred.getArrayValue(taxonomy), (Array) terms)));

            return true;
        }

        return wp_update_term_count_now((Array) terms, taxonomy);
    }

    public boolean wp_update_term_count_now(Array<Object> terms, String taxonomyStr) {
        Object count = null;
        Object term = null;
        terms = Array.array_map(new Callback("intval", VarHandling.class), terms);

        StdClass taxonomy = (StdClass) get_taxonomy(taxonomyStr);

        if (!empty(StdClass.getValue(taxonomy, "update_count_callback"))) {
            FunctionHandling.call_user_func(new Callback(strval(StdClass.getValue(taxonomy, "update_count_callback")), this), terms);
        } else {
    		// Default count updater
            for (Map.Entry javaEntry615 : terms.entrySet()) {
                term = javaEntry615.getValue();
                count = gVars.wpdb.get_var(gVars.wpdb.prepare("SELECT COUNT(*) FROM " + gVars.wpdb.term_relationships + " WHERE term_taxonomy_id = %d", term));
                gVars.wpdb.update(gVars.wpdb.term_taxonomy, Array.compact(new ArrayEntry("count", count)), new Array<Object>(new ArrayEntry<Object>("term_taxonomy_id", term)));
            }
        }

        clean_term_cache(terms, "");

        return true;
    }

    //
 // Cache
 //

 /**
  * clean_object_term_cache() - Removes the taxonomy relationship to terms from the cache.
  *
  * Will remove the entire taxonomy relationship containing term $object_id. The term IDs
  * have to exist within the taxonomy $object_type for the deletion to take place.
  *
  * @package WordPress
  * @subpackage Taxonomy
  * @since 2.3
  *
  * @see get_object_taxonomies() for more on $object_type
  * @uses do_action() Will call action hook named, 'clean_object_term_cache' after completion.
  *	Passes, function params in same order.
  *
  * @param int|array $object_ids Single or list of term object ID(s)
  * @param array|string $object_type The taxonomy object type
  */
    public void clean_object_term_cache(Object object_ids, /* Do not change type */
        String object_type) {
        Object id = null;
        Object taxonomy = null;

        if (!is_array(object_ids)) {
            object_ids = new Array<Object>(new ArrayEntry<Object>(object_ids));
        }

        for (Map.Entry javaEntry616 : ((Array<?>) object_ids).entrySet()) {
            id = javaEntry616.getValue();

            for (Map.Entry javaEntry617 : get_object_taxonomies(object_type).entrySet()) {
                taxonomy = javaEntry617.getValue();
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(id, strval(taxonomy) + "_relationships");
            }
        }

        getIncluded(PluginPage.class, gVars, gConsts).do_action("clean_object_term_cache", object_ids, object_type);
    }

    /**
     * clean_term_cache() - Will remove all of the term ids from the cache
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses $wpdb
     * @param int|array $ids Single or list of Term IDs
     * @param string $taxonomy Can be empty and will assume tt_ids, else will
     * use for context.
     */
    public void clean_term_cache(Object ids, /* Do not change type */
        String taxonomy) {
        Array<Object> taxonomies = new Array<Object>();
        String tt_ids = null;
        Array<Object> terms = new Array<Object>();
        StdClass term = null;
        Object id = null;

        if (!is_array(ids)) {
            ids = new Array<Object>(new ArrayEntry<Object>(ids));
        }

        taxonomies = new Array<Object>();
    	// If no taxonomy, assume tt_ids.
        if (empty(taxonomy)) {
            tt_ids = Strings.implode(", ", (Array) ids);
            terms = gVars.wpdb.get_results("SELECT term_id, taxonomy FROM " + gVars.wpdb.term_taxonomy + " WHERE term_taxonomy_id IN (" + tt_ids + ")");

            for (Map.Entry javaEntry618 : new Array<Object>(terms).entrySet()) {
                term = (StdClass) javaEntry618.getValue();
                taxonomies.putValue(StdClass.getValue(term, "taxonomy"));
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(StdClass.getValue(term, "term_id"), strval(StdClass.getValue(term, "taxonomy")));
            }

            taxonomies = Array.array_unique(taxonomies);
        } else {
            for (Map.Entry javaEntry619 : ((Array<?>) ids).entrySet()) {
                id = javaEntry619.getValue();
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete(id, taxonomy);
            }

            taxonomies = new Array<Object>(new ArrayEntry<Object>(taxonomy));
        }

        for (Map.Entry javaEntry620 : taxonomies.entrySet()) {
            taxonomy = strval(javaEntry620.getValue());
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("all_ids", taxonomy);
            getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("get", taxonomy);
            getIncluded(FunctionsPage.class, gVars, gConsts).delete_option(taxonomy + "_children");
        }

        getIncluded(CachePage.class, gVars, gConsts).wp_cache_delete("get_terms", "terms");
        getIncluded(PluginPage.class, gVars, gConsts).do_action("clean_term_cache", ids, taxonomy);
    }

    /**
     * get_object_term_cache() - Retrieves the taxonomy relationship to the
     * term object id.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses wp_cache_get() Retrieves taxonomy relationship from cache
     * @param int|array $id Term object ID
     * @param string $taxonomy Taxonomy Name
     * @return bool|array Empty array if $terms found, but not $taxonomy. False
     * if nothing is in cache for $taxonomy and $id.
     */
    public Array<Object> get_object_term_cache(Object id, String taxonomy) {
        Array<Object> cache;
        cache = (Array<Object>) getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(id, taxonomy + "_relationships");

        return (Array) cache;
    }

    /**
     * update_object_term_cache() - Updates the cache for Term ID(s)
     * Will only update the cache for terms not already cached.
     * The $object_ids expects that the ids be separated by commas, if it is a
     * string.
     * It should be noted that update_object_term_cache() is very time
     * extensive. It is advised that the function is not called very often or at
     * least not for a lot of terms that exist in a lot of taxonomies. The
     * amount of time increases for each term and it also increases for each
     * taxonomy the term belongs to.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @uses wp_get_object_terms() Used to get terms from the database to update
     * @param string|array $object_ids Single or list of term object ID(s)
     * @param array|string $object_type The taxonomy object type
     * @return null|bool Null value is given with empty $object_ids. False if
     */
    public boolean update_object_term_cache(Object object_ids, /* Do not change type */
        String object_type) {
        Array<Object> taxonomies = new Array<Object>();
        Array<Object> ids = new Array<Object>();
        Object id = null;
        Object taxonomy = null;
        Object terms = null;
        Array<Object> object_terms = new Array<Object>();
        StdClass term = null;
        Array<Object> value = null;

        if (empty(object_ids)) {
            return false;
        }

        if (!is_array(object_ids)) {
            object_ids = Strings.explode(",", strval(object_ids));
        }

        object_ids = Array.array_map(new Callback("intval", VarHandling.class), (Array<Object>) object_ids);
        taxonomies = get_object_taxonomies(object_type);
        ids = new Array<Object>();

        for (Map.Entry javaEntry621 : new Array<Object>(object_ids).entrySet()) {
            id = javaEntry621.getValue();

            for (Map.Entry javaEntry622 : taxonomies.entrySet()) {
                taxonomy = javaEntry622.getValue();

                if (strictEqual(null, getIncluded(CachePage.class, gVars, gConsts).wp_cache_get(id, strval(taxonomy) + "_relationships"))) {
                    ids.putValue(id);

                    break;
                }
            }
        }

        if (empty(ids)) {
            return false;
        }

        terms = wp_get_object_terms(ids, taxonomies, "fields=all_with_object_id");
        object_terms = new Array<Object>();

        for (Map.Entry javaEntry623 : new Array<Object>(terms).entrySet()) {
            term = (StdClass) javaEntry623.getValue();
            object_terms.getArrayValue(StdClass.getValue(term, "object_id")).getArrayValue(StdClass.getValue(term, "taxonomy")).putValue(StdClass.getValue(term, "term_id"), term);
        }

        for (Map.Entry javaEntry624 : ids.entrySet()) {
            id = javaEntry624.getValue();

            for (Map.Entry javaEntry625 : taxonomies.entrySet()) {
                taxonomy = javaEntry625.getValue();

                if (!isset(object_terms.getArrayValue(id).getValue(taxonomy))) {
                    if (!isset(object_terms.getValue(id))) {
                        object_terms.putValue(id, new Array<Object>());
                    }

                    object_terms.getArrayValue(id).putValue(taxonomy, new Array<Object>());
                }
            }
        }

        for (Map.Entry javaEntry626 : object_terms.entrySet()) {
            id = javaEntry626.getKey();
            value = (Array<Object>) javaEntry626.getValue();

            for (Map.Entry javaEntry627 : value.entrySet()) {
                taxonomy = javaEntry627.getKey();
                terms = javaEntry627.getValue();
                getIncluded(CachePage.class, gVars, gConsts).wp_cache_set(id, terms, strval(taxonomy) + "_relationships", 0);
            }
        }

        return false;
    }

    /**
     * update_term_cache() - Updates Terms to Taxonomy in cache.
     *
     * @subpackage Taxonomy
     * @since 2.3
     * @param array $terms List of Term objects to change
     * @param string $taxonomy Optional. Update Term to this taxonomy in cache
     */
    public void update_term_cache(Array<Object> terms, String taxonomy) {
        String term_taxonomy = null;
        StdClass term = null;

        for (Map.Entry javaEntry628 : terms.entrySet()) {
            term = (StdClass) javaEntry628.getValue();
            term_taxonomy = taxonomy;

            if (empty(term_taxonomy)) {
                term_taxonomy = strval(StdClass.getValue(term, "taxonomy"));
            }

            getIncluded(CachePage.class, gVars, gConsts).wp_cache_add(StdClass.getValue(term, "term_id"), term, term_taxonomy, 0);
        }
    }

 //
 // Private
 //

 /**
  * _get_term_hierarchy() - Retrieves children of taxonomy
  *
  * @package WordPress
  * @subpackage Taxonomy
  * @access private
  * @since 2.3
  *
  * @uses update_option() Stores all of the children in "$taxonomy_children" option.
  *	That is the name of the taxonomy, immediately followed by '_children'.
  *
  * @param string $taxonomy Taxonomy Name
  * @return array Empty if $taxonomy isn't hierarachical or returns children.
  */
    public Array<Object> _get_term_hierarchy(String taxonomy) {
        Object children;

        /* Do not change type */
        Array<Object> terms = null;
        StdClass term = null;

        if (!is_taxonomy_hierarchical(taxonomy)) {
            return new Array<Object>();
        }

        children = getIncluded(FunctionsPage.class, gVars, gConsts).get_option(taxonomy + "_children");

        if (is_array(children)) {
            return (Array) children;
        }

        children = new Array<Object>();
        terms = (Array<Object>) get_terms(taxonomy, "get=all");

        for (Map.Entry javaEntry629 : terms.entrySet()) {
            term = (StdClass) javaEntry629.getValue();

            if (intval(StdClass.getValue(term, "parent")) > 0) {
                ((Array) children).getArrayValue(StdClass.getValue(term, "parent")).putValue(StdClass.getValue(term, "term_id"));
            }
        }

        getIncluded(FunctionsPage.class, gVars, gConsts).update_option(taxonomy + "_children", children);

        return (Array) children;
    }

    /**
     * _get_term_children() - Get array of child terms
     * If $terms is an array of objects, then objects will returned from the
     * function. If $terms is an array of IDs, then an array of ids of children
     * will be returned.
     *
     * @subpackage Taxonomy
     * @access private
     * @since 2.3
     * @param int $term_id Look for this Term ID in $terms
     * @param array $terms List of Term IDs
     * @param string $taxonomy Term Context
     * @return array Empty if $terms is empty else returns full list of child
     * terms.
     */
    public Object _get_term_children(int term_id, Array<Object> terms, String taxonomy) {
        Array<Object> empty_array = new Array<Object>();
        Array<Object> term_list = new Array<Object>();
        Array<Object> has_children = new Array<Object>();
        boolean use_id = false;
        Object term;

        /* Do not change type */
        Array<Object> children = null;
        empty_array = new Array<Object>();

        if (empty(terms)) {
            return empty_array;
        }

        term_list = new Array<Object>();
        has_children = _get_term_hierarchy(taxonomy);

        if (!equal(0, term_id) && !isset(has_children.getValue(term_id))) {
            return empty_array;
        }

        for (Map.Entry javaEntry630 : terms.entrySet()) {
            term = javaEntry630.getValue();
            use_id = false;

            if (!is_object(term)) {
                term = get_term(term, taxonomy, gConsts.getOBJECT(), "raw");

                if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(term)) {
                    return term;
                }

                use_id = true;
            }

            if (equal(((StdClass) term).fields.getValue("term_id"), term_id)) {
                continue;
            }

            if (equal(((StdClass) term).fields.getValue("parent"), term_id)) {
                if (use_id) {
                    term_list.putValue(((StdClass) term).fields.getValue("term_id"));
                } else {
                    term_list.putValue(term);
                }

                if (!isset(has_children.getValue(((StdClass) term).fields.getValue("term_id")))) {
                    continue;
                }

                if (booleanval(children = (Array<Object>) _get_term_children(intval(((StdClass) term).fields.getValue("term_id")), terms, taxonomy))) {
                    term_list = Array.array_merge(term_list, children);
                }
            }
        }

        return term_list;
    }

    /**
     * _pad_term_counts() - Add count of children to parent count
     * Recalculates term counts by including items from child terms. Assumes all
     * relevant children are already in the $terms argument
     *
     * @subpackage Taxonomy
     * @access private
     * @since 2.3
     * @uses $wpdb
     * @param array $terms List of Term IDs
     * @param string $taxonomy Term Context
     * @return null Will break from function if conditions are not met.
     */
    public void _pad_term_counts(Array<Object> terms, String taxonomy) {
        Array<Object> term_hier = new Array<Object>();
        Array<Object> term_items = new Array<Object>();
        Array<StdClass> terms_by_id = new Array<StdClass>();
        StdClass term = null;
        Object key = null;
        Array<Object> term_ids = new Array<Object>();
        Array<Object> results = new Array<Object>();
        Object id = null;
        StdClass row = null;
        Object child = null;
        Object term_id = null;
        Object parent = null;
        Object item_id = null;
        Object touches = null;
        Object items = null;

    	// This function only works for post categories.
        if (!equal("category", taxonomy)) {
            return;
        }

        term_hier = _get_term_hierarchy(taxonomy);

        if (empty(term_hier)) {
            return;
        }

        term_items = new Array<Object>();

        for (Map.Entry javaEntry631 : terms.entrySet()) {
            key = javaEntry631.getKey();
            term = (StdClass) javaEntry631.getValue();
            terms_by_id.putValue(StdClass.getValue(term, "term_id"), terms.getRef(key));
            term_ids.putValue(StdClass.getValue(term, "term_taxonomy_id"), StdClass.getValue(term, "term_id"));
        }

    	// Get the object and term ids and stick them in a lookup table
        results = gVars.wpdb.get_results(
                    "SELECT object_id, term_taxonomy_id FROM " + gVars.wpdb.term_relationships + " INNER JOIN " + gVars.wpdb.posts + " ON object_id = ID WHERE term_taxonomy_id IN (" +
                    Strings.join(",", Array.array_keys(term_ids)) + ") AND post_type = \'post\' AND post_status = \'publish\'");

        for (Map.Entry javaEntry632 : results.entrySet()) {
            row = (StdClass) javaEntry632.getValue();
            id = term_ids.getValue(StdClass.getValue(row, "term_taxonomy_id"));
            term_items.getArrayValue(id).incValue(StdClass.getValue(row, "object_id"));
        }

    	// Touch every ancestor's lookup row for each post in each term
        for (Map.Entry javaEntry633 : term_ids.entrySet()) {
            term_id = javaEntry633.getValue();
            child = term_id;

            while (booleanval(parent = terms_by_id.getValue(child).fields.getValue("parent"))) {
                if (!empty(term_items.getValue(term_id))) {
                    for (Map.Entry javaEntry634 : (Set<Map.Entry>) term_items.getArrayValue(term_id).entrySet()) {
                        item_id = javaEntry634.getKey();
                        touches = javaEntry634.getValue();
                        term_items.getArrayValue(parent).incValue(item_id);
                    }
                }

                child = parent;
            }
        }

    	// Transfer the touched cells
        for (Map.Entry javaEntry635 : new Array<Object>(term_items).entrySet()) {
            id = javaEntry635.getKey();
            items = javaEntry635.getValue();

            if (isset(terms_by_id.getValue(id))) {
                terms_by_id.getValue(id).fields.putValue("count", Array.count(items));
            }
        }
    }

 //
 // Default callbacks
 //

 /**
  * _update_post_term_count() - Will update term count based on posts
  *
  * Private function for the default callback for post_tag and category taxonomies.
  *
  * @package WordPress
  * @subpackage Taxonomy
  * @access private
  * @since 2.3
  * @uses $wpdb
  *
  * @param array $terms List of Term IDs
  */
    public void _update_post_term_count(Array<Object> terms) {
        Object count = null;
        Object term = null;

        for (Map.Entry javaEntry636 : terms.entrySet()) {
            term = javaEntry636.getValue();
            count = gVars.wpdb.get_var(
                        gVars.wpdb.prepare(
                                "SELECT COUNT(*) FROM " + gVars.wpdb.term_relationships + ", " + gVars.wpdb.posts + " WHERE " + gVars.wpdb.posts + ".ID = " + gVars.wpdb.term_relationships +
                                ".object_id AND post_status = \'publish\' AND post_type = \'post\' AND term_taxonomy_id = %d",
                                term));
            gVars.wpdb.update(gVars.wpdb.term_taxonomy, Array.compact(new ArrayEntry("count", count)), new Array<Object>(new ArrayEntry<Object>("term_taxonomy_id", term)));
        }
    }

    /**
     * get_term_link() - Generates a permalink for a taxonomy term archive
     * @param object|int|string $term
     * @param string $taxonomy
     * @return string HTML link to taxonomy term archive
     */
    public Object get_term_link(Object term, /* Do not change type */
        String taxonomy) {
        String termlink = null;
        String slug = null;
        String file = null;
        StdClass t = null;

    	// use legacy functions for core taxonomies until they are fully plugged in
        if (equal(taxonomy, "category")) {
            return strval(getIncluded(Category_templatePage.class, gVars, gConsts).get_category_link(term));
        }

        if (equal(taxonomy, "post_tag")) {
            return strval(getIncluded(Category_templatePage.class, gVars, gConsts).get_tag_link(term));
        }

        termlink = gVars.wp_rewrite.get_extra_permastruct(taxonomy);

        if (!is_object(term)) {
            if (is_int(term)) {
                term = get_term(term, taxonomy, gConsts.getOBJECT(), "raw");
            } else {
                term = get_term_by("slug", term, taxonomy, gConsts.getOBJECT(), "raw");
            }
        }

        if (getIncluded(ClassesPage.class, gVars, gConsts).is_wp_error(term)) {
            return term;
        }

        slug = strval(((StdClass) term).fields.getValue("slug"));

        if (empty(termlink)) {
            file = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + "/";
            t = (StdClass) get_taxonomy(taxonomy);

            if (booleanval(StdClass.getValue(t, "query_var"))) {
                termlink = file + "?" + StdClass.getValue(t, "query_var") + "=" + slug;
            } else {
                termlink = file + "?taxonomy=" + taxonomy + "&term=" + slug;
            }
        } else {
            termlink = Strings.str_replace("%" + taxonomy + "%", slug, termlink);
            termlink = getIncluded(FunctionsPage.class, gVars, gConsts).get_option("home") + getIncluded(Link_templatePage.class, gVars, gConsts).user_trailingslashit(termlink, "category");
        }

        return getIncluded(PluginPage.class, gVars, gConsts).apply_filters("term_link", termlink, term, taxonomy);
    }

    public void the_taxonomies() {
        the_taxonomies(new Array<Object>());
    }

    public void the_taxonomies(Object args) {
        Array<Object> defaults = new Array<Object>();
        Array<Object> r = null;
        Object before = null;
        String sep = null;
        Object post = null;
        Object after = null;
        defaults = new Array<Object>(new ArrayEntry<Object>("post", 0), new ArrayEntry<Object>("before", ""), new ArrayEntry<Object>("sep", " "), new ArrayEntry<Object>("after", ""));
        r = getIncluded(FunctionsPage.class, gVars, gConsts).wp_parse_args(args, defaults);
        before = Array.extractVar(r, "before", before, Array.EXTR_SKIP);
        sep = strval(Array.extractVar(r, "sep", sep, Array.EXTR_SKIP));
        post = Array.extractVar(r, "post", post, Array.EXTR_SKIP);
        after = Array.extractVar(r, "after", after, Array.EXTR_SKIP);
        echo(gVars.webEnv, strval(before) + Strings.join(sep, get_the_taxonomies(post)) + strval(after));
    }

    public Array<String> get_the_taxonomies(Object postObj)/* Do not change type */
     {
        Array<String> taxonomies;
        Object template = null;
        Array<Object> t = new Array<Object>();
        String taxonomy = null;
        Array<Object> terms = null;
        Array<Object> links = new Array<Object>();
        StdClass term = null;
        StdClass post = new StdClass();

        if (is_int(postObj)) {
            post = (StdClass) getIncluded(PostPage.class, gVars, gConsts).get_post(postObj, gConsts.getOBJECT(), "raw");
        } else if (!is_object(postObj)) {
            post = gVars.post;
        }

        taxonomies = new Array<String>();

        if (!booleanval(post)) {
            return taxonomies;
        }

        template = getIncluded(PluginPage.class, gVars, gConsts).apply_filters("taxonomy_template", "%s: %l.");

        for (Map.Entry javaEntry637 : get_object_taxonomies(post).entrySet()) {
            taxonomy = strval(javaEntry637.getValue());
            t = new Array<Object>(get_taxonomy(taxonomy));

            if (empty(t.getValue("label"))) {
                t.putValue("label", taxonomy);
            }

            if (empty(t.getValue("args"))) {
                t.putValue("args", new Array<Object>());
            }

            if (empty(t.getValue("template"))) {
                t.putValue("template", template);
            }

            terms = get_object_term_cache(StdClass.getValue(post, "ID"), taxonomy);

            if (empty(terms)) {
                terms = (Array<Object>) wp_get_object_terms(StdClass.getValue(post, "ID"), taxonomy, strval(t.getValue("args")));
            }

            links = new Array<Object>();

            for (Map.Entry javaEntry638 : terms.entrySet()) {
                term = (StdClass) javaEntry638.getValue();
                links.putValue(
                    "<a href=\'" + getIncluded(FormattingPage.class, gVars, gConsts).attribute_escape(strval(get_term_link(term, taxonomy))) + "\'>" + StdClass.getValue(term, "name") + "</a>");
            }

            if (booleanval(links)) {
                taxonomies.putValue(taxonomy, getIncluded(FormattingPage.class, gVars, gConsts).wp_sprintf(strval(t.getValue("template")), t.getValue("label"), links, terms));
            }
        }

        return taxonomies;
    }

    public Array<Object> get_post_taxonomies(Object post) {
        post = getIncluded(PostPage.class, gVars, gConsts).get_post(post, gConsts.getOBJECT(), "raw");

        return get_object_taxonomies(post);
    }

    public Object generateContent(PhpWebEnvironment webEnv)
        throws IOException, ServletException {
        gVars.webEnv = webEnv;
        
        /**
         * @package WordPress
         * @subpackage Taxonomy
         * @since 2.3
         */

        //
        // Taxonomy Registration
        //

        /**
         * Default Taxonomy Objects
         * @since 2.3
         * @global array $wp_taxonomies
         */
        gVars.wp_taxonomies = new Array<Object>();
        gVars.wp_taxonomies.putValue("category",
            Array.toStdClass(
                new Array<Object>(
                    new ArrayEntry<Object>("name", "category"),
                    new ArrayEntry<Object>("object_type", "post"),
                    new ArrayEntry<Object>("hierarchical", true),
                    new ArrayEntry<Object>("update_count_callback", "_update_post_term_count"))));
        gVars.wp_taxonomies.putValue("post_tag",
            Array.toStdClass(
                new Array<Object>(
                    new ArrayEntry<Object>("name", "post_tag"),
                    new ArrayEntry<Object>("object_type", "post"),
                    new ArrayEntry<Object>("hierarchical", false),
                    new ArrayEntry<Object>("update_count_callback", "_update_post_term_count"))));
        gVars.wp_taxonomies.putValue(
            "link_category",
            Array.toStdClass(new Array<Object>(new ArrayEntry<Object>("name", "link_category"), new ArrayEntry<Object>("object_type", "link"), new ArrayEntry<Object>("hierarchical", false))));

        return DEFAULT_VAL;
    }
}
