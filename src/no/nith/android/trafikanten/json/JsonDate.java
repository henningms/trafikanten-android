/**
 * JsonDate
 * 
 * Helper class for converting a date string to a date object
 * 
 * Written by Henning M. Stephansen © 2011
 */
package no.nith.android.trafikanten.json;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class JsonDate
{
	/**
	 * Method taken from http://www.weston-fl.com/blog/?p=2968
	 * Method cleaned up by Henning M. Stephansen
	 * 
	 * Converts a JSON Date String to a Java Date
	 * @param dateFromJson String from JSON in format "\/Date(xxxxxx+xxxx)/\"
	 * @return Date object 
	 */
	public static Date Deserialize(String dateFromJson)
	{
		// Extract the date part
		String dateString = dateFromJson.replace("/Date(", "").replace(")/", ""); 
		
		// Split date into time in milliseconds and timezone
		String[] dateParts = dateString.split("[+-]");
		
		// Use Calendar to set time and timezone
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTimeInMillis(Long.parseLong(dateParts[0]));
		calendar.setTimeZone(TimeZone.getTimeZone(dateParts[1]));
	
		// Return the correct date
		return calendar.getTime();
	}
}
