/**********************************************************************************
 *   nWordPress is an automated migration of WordPress 2.5.1 performed by Numiton.
 *   
 *   copyright            : (C) 2008 Numiton - www.numiton.com
 *   email                : numiton@users.sourceforge.net
 *
 *   $Id: WP_Locale.java,v 1.4 2008/10/14 13:15:48 numiton Exp $
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
import static com.numiton.generic.PhpWeb.getIncluded;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.numiton.nwp.GlobalConsts;
import org.numiton.nwp.GlobalVars;

import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.generic.ContextCarrierInterface;
import com.numiton.generic.GlobalConstantsInterface;
import com.numiton.generic.GlobalVariablesContainer;
import com.numiton.ntile.til.libraries.php.quercus.QRegExPerl;


/**
 * Date and Time Locale object
 *
 * @package WordPress
 * @subpackage i18n
 */

/**
 * {@internal Missing Short Description}}
 *
 * {@internal Missing Long Description}}
 *
 * @since 2.1.0
 */
public class WP_Locale implements ContextCarrierInterface, Serializable, Cloneable {
    protected static final Logger LOG = Logger.getLogger(WP_Locale.class.getName());
    public GlobalConsts gConsts;
    public GlobalVars gVars;

    /**
     * * Stores the translated strings for the full weekday names.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<String> weekday = new Array<String>();

    /**
     * * Stores the translated strings for the one character weekday names.
     *
     * There is a hack to make sure that Tuesday and Thursday, as well as Sunday
     * and Saturday don't conflict. See init() method for more.
     *
     * @see WP_Locale::init() for how to handle the hack.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<String> weekday_initial = new Array<String>();

    /**
     * * Stores the translated strings for the abbreviated weekday names.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<String> weekday_abbrev = new Array<String>();

    /**
     * * Stores the translated strings for the full month names.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<String> month = new Array<String>();

    /**
     * * Stores the translated strings for the abbreviated month names.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<String> month_abbrev = new Array<String>();

    /**
     * * Stores the translated strings for 'am' and 'pm'.
     *
     * Also the capalized versions.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<String> meridiem = new Array<String>();

    /**
     * * The text direction of the locale language.
     *
     * Default is left to right 'ltr'.
     *
     * @since 2.1.0
     * @var string
     * @access private
     *
     */
    public String text_direction = "ltr";

    /**
     * * Imports the global version to the class property.
     *
     * @since 2.1.0
     * @var array
     * @access private
     *
     */
    public Array<Object> locale_vars = new Array<Object>(new ArrayEntry<Object>("text_direction"));
    public Array<Object> number_format = new Array<Object>();

    /**
     * * PHP4 style constructor which calls helper methods to set up object
     * variables
     *
     * @uses WP_Locale::init()
     * @uses WP_Locale::register_globals()
     * @since 2.1.0
     *
     * @return WP_Locale
     *
     */
    public WP_Locale(GlobalVars javaGlobalVariables, GlobalConsts javaGlobalConstants) {
        setContext(javaGlobalVariables, javaGlobalConstants);
        this.init();
        this.register_globals();
    }

    /**
     * * Sets up the translated strings and object properties.
     *
     * The method creates the translatable strings for various calendar
     * elements. Which allows for specifying locale specific calendar names and
     * text direction.
     *
     * @since 2.1.0
     * @access private
     *
     */
    public void init() {
        Object weekday_ = null;
        String weekday_initial_ = null;
        Object month_ = null;
        String month_abbrev_ = null;
        Object trans = null;
        Object var = null;
        
		// The Weekdays
        this.weekday.putValue(0, getIncluded(L10nPage.class, gVars, gConsts).__("Sunday", "default"));
        this.weekday.putValue(1, getIncluded(L10nPage.class, gVars, gConsts).__("Monday", "default"));
        this.weekday.putValue(2, getIncluded(L10nPage.class, gVars, gConsts).__("Tuesday", "default"));
        this.weekday.putValue(3, getIncluded(L10nPage.class, gVars, gConsts).__("Wednesday", "default"));
        this.weekday.putValue(4, getIncluded(L10nPage.class, gVars, gConsts).__("Thursday", "default"));
        this.weekday.putValue(5, getIncluded(L10nPage.class, gVars, gConsts).__("Friday", "default"));
        this.weekday.putValue(6, getIncluded(L10nPage.class, gVars, gConsts).__("Saturday", "default"));
        
		// The first letter of each day.  The _%day%_initial suffix is a hack to make
		// sure the day initials are unique.
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Sunday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("S_Sunday_initial", "default"));
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Monday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("M_Monday_initial", "default"));
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Tuesday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("T_Tuesday_initial", "default"));
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Wednesday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("W_Wednesday_initial", "default"));
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Thursday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("T_Thursday_initial", "default"));
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Friday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("F_Friday_initial", "default"));
        this.weekday_initial.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Saturday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("S_Saturday_initial", "default"));

        for (Map.Entry javaEntry505 : this.weekday_initial.entrySet()) {
            weekday_ = javaEntry505.getKey();
            weekday_initial_ = strval(javaEntry505.getValue());
            this.weekday_initial.putValue(weekday_, QRegExPerl.preg_replace("/_.+_initial$/", "", weekday_initial_));
        }

		// Abbreviations for each day.
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Sunday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Sun", "default"));
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Monday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Mon", "default"));
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Tuesday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Tue", "default"));
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Wednesday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Wed", "default"));
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Thursday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Thu", "default"));
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Friday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Fri", "default"));
        this.weekday_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("Saturday", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Sat", "default"));
        
		// The Months
        this.month.putValue("01", getIncluded(L10nPage.class, gVars, gConsts).__("January", "default"));
        this.month.putValue("02", getIncluded(L10nPage.class, gVars, gConsts).__("February", "default"));
        this.month.putValue("03", getIncluded(L10nPage.class, gVars, gConsts).__("March", "default"));
        this.month.putValue("04", getIncluded(L10nPage.class, gVars, gConsts).__("April", "default"));
        this.month.putValue("05", getIncluded(L10nPage.class, gVars, gConsts).__("May", "default"));
        this.month.putValue("06", getIncluded(L10nPage.class, gVars, gConsts).__("June", "default"));
        this.month.putValue("07", getIncluded(L10nPage.class, gVars, gConsts).__("July", "default"));
        this.month.putValue("08", getIncluded(L10nPage.class, gVars, gConsts).__("August", "default"));
        this.month.putValue("09", getIncluded(L10nPage.class, gVars, gConsts).__("September", "default"));
        this.month.putValue("10", getIncluded(L10nPage.class, gVars, gConsts).__("October", "default"));
        this.month.putValue("11", getIncluded(L10nPage.class, gVars, gConsts).__("November", "default"));
        this.month.putValue("12", getIncluded(L10nPage.class, gVars, gConsts).__("December", "default"));
        
		// Abbreviations for each month. Uses the same hack as above to get around the
		// 'May' duplication.
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("January", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Jan_January_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("February", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Feb_February_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("March", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Mar_March_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("April", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Apr_April_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("May", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("May_May_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("June", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Jun_June_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("July", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Jul_July_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("August", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Aug_August_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("September", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Sep_September_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("October", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Oct_October_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("November", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Nov_November_abbreviation", "default"));
        this.month_abbrev.putValue(getIncluded(L10nPage.class, gVars, gConsts).__("December", "default"), getIncluded(L10nPage.class, gVars, gConsts).__("Dec_December_abbreviation", "default"));

        for (Map.Entry javaEntry506 : this.month_abbrev.entrySet()) {
            month_ = javaEntry506.getKey();
            month_abbrev_ = strval(javaEntry506.getValue());
            this.month_abbrev.putValue(month_, QRegExPerl.preg_replace("/_.+_abbreviation$/", "", month_abbrev_));
        }

		// The Meridiems
        this.meridiem.putValue("am", getIncluded(L10nPage.class, gVars, gConsts).__("am", "default"));
        this.meridiem.putValue("pm", getIncluded(L10nPage.class, gVars, gConsts).__("pm", "default"));
        this.meridiem.putValue("AM", getIncluded(L10nPage.class, gVars, gConsts).__("AM", "default"));
        this.meridiem.putValue("PM", getIncluded(L10nPage.class, gVars, gConsts).__("PM", "default"));
        
		// Numbers formatting
		// See http://php.net/number_format
        trans = getIncluded(L10nPage.class, gVars, gConsts)._c("number_format_decimals|$decimals argument for http://php.net/number_format, default is 0", "default");
        this.number_format.putValue("decimals", equal("number_format_decimals", trans)
            ? 0
            : intval(trans));
        trans = getIncluded(L10nPage.class, gVars, gConsts)._c("number_format_decimal_point|$dec_point argument for http://php.net/number_format, default is .", "default");
        this.number_format.putValue("decimal_point", equal("number_format_decimal_point", trans)
            ? "."
            : strval(trans));
        trans = getIncluded(L10nPage.class, gVars, gConsts)._c("number_format_thousands_sep|$thousands_sep argument for http://php.net/number_format, default is ,", "default");
        this.number_format.putValue("thousands_sep", equal("number_format_thousands_sep", trans)
            ? ","
            : strval(trans));

        // Commented by Numiton
        // 		// Import global locale vars set during inclusion of $locale.php.
        //		for (Map.Entry javaEntry507 : this.locale_vars.entrySet()) {
        //			var = javaEntry507.getValue();
        //
        //			/*
        //			 * Unsupported GLOBALS referencing with non-static expression:
        //			 * com.numiton.ntile.til.model.expressions.impl.VariableRefImpl@275
        //			 * (generated: false, leadingText: null, translationHint: null,
        //			 * text: var, id: 404137) (declarationId: 0)
        //			 */
        //			if (isset(GLOBALS.getValue(var))) {
        //
        //				/*
        //				 * Unsupported GLOBALS referencing with non-static
        //				 * expression:
        //				 * com.numiton.ntile.til.model.expressions.impl.VariableRefImpl@275
        //				 * (generated: false, leadingText: null, translationHint: null,
        //				 * text: var, id: 404150) (declarationId: 0)
        //				 */
        //				/* unresolved dynamic construct: 404148 */;
        //			}
        //		}
    }

    /**
     * * Retrieve the full translated weekday word.
     *
     * Week starts on translated Sunday and can be fetched by using 0 (zero). So
     * the week starts with 0 (zero) and ends on Saturday with is fetched by
     * using 6 (six).
     *
     * @since 2.1.0
     * @access public
     *
     * @param int $weekday_number 0 for Sunday through 6 Saturday
     * @return string Full translated weekday
     *
     */
    public String get_weekday(int weekday_number) {
        return this.weekday.getValue(weekday_number);
    }

    /**
     * * Retrieve the translated weekday initial.
     *
     * The weekday initial is retrieved by the translated full weekday word.
     * When translating the weekday initial pay attention to make sure that the
     * starting letter does not conflict.
     *
     * @since 2.1.0
     * @access public
     *
     * @param string $weekday_name
     * @return string
     *
     */
    public String get_weekday_initial(String weekday_name) {
        return this.weekday_initial.getValue(weekday_name);
    }

    /**
     * * Retrieve the translated weekday abbreviation.
     *
     * The weekday abbreviation is retrieved by the translated full weekday
     * word.
     *
     * @since 2.1.0
     * @access public
     *
     * @param string $weekday_name Full translated weekday word
     * @return string Translated weekday abbreviation
     *
     */
    public String get_weekday_abbrev(String weekday_name) {
        return this.weekday_abbrev.getValue(weekday_name);
    }

    public String get_month(String month_number) {
        return get_month(intval(month_number));
    }

    /**
     * * Retrieve the full translated month by month number.
     *
     * The $month_number parameter has to be a string because it must have the
     * '0' in front of any number that is less than 10. Starts from '01' and
     * ends at '12'.
     *
     * You can use an integer instead and it will add the '0' before the numbers
     * less than 10 for you.
     *
     * @since 2.1.0
     * @access public
     *
     * @param string|int $month_number '01' through '12'
     * @return string Translated full month name
     *
     */
    public String get_month(int month_number) {
        return this.month.getValue(getIncluded(FormattingPage.class, gVars, gConsts).zeroise(month_number, 2));
    }

    /**
     * * Retrieve translated version of month abbreviation string.
     *
     * The $month_name parameter is expected to be the translated or
     * translatable version of the month.
     *
     * @since 2.1.0
     * @access public
     *
     * @param string $month_name Translated month to get abbreviated version
     * @return string Translated abbreviated month
     *
     */
    public String get_month_abbrev(String month_name) {
        return this.month_abbrev.getValue(month_name);
    }

    /**
     * * Retrieve translated version of meridiem string.
     *
     * The $meridiem parameter is expected to not be translated.
     *
     * @since 2.1.0
     * @access public
     *
     * @param string $meridiem Either 'am', 'pm', 'AM', or 'PM'. Not translated
     *            version.
     * @return string Translated version
     *
     */
    public String get_meridiem(String meridiem) {
        return this.meridiem.getValue(meridiem);
    }

    /**
     * * Global variables are deprecated. For backwards compatibility only.
     *
     * @deprecated For backwards compatibility only.
     * @access private
     *
     * @since 2.1.0
     *
     */
    public void register_globals() {
        // Commented by Numiton
        //		weekday = this.weekday;
        //		weekday_initial = this.weekday_initial;
        //		weekday_abbrev = this.weekday_abbrev;
        //		month = this.month;
        //		month_abbrev = this.month_abbrev;
    }

    // Commented by Numiton
    //	public Object	     weekday;
    //	public Object	     weekday_initial;
    //	public Object	     weekday_abbrev;
    //	public Object	     month;
    //	public Object	     month_abbrev;
    public void setContext(GlobalVariablesContainer javaGlobalVariables, GlobalConstantsInterface javaGlobalConstants) {
        gConsts = (GlobalConsts) javaGlobalConstants;
        gVars = (GlobalVars) javaGlobalVariables;
        gVars.gConsts = gConsts;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public GlobalVariablesContainer getGlobalVars() {
        return gVars;
    }
}
