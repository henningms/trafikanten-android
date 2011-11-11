/**
 * JsonParser2
 * 
 * Parses a JSONArray or JSONObject and matches it to a class
 * 
 * Uses JsonField annotation
 * 
 * Written By Henning M. Stephansen © 2011
 */

package no.nith.android.trafikanten.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import no.nith.android.trafikanten.json.annotation.JsonField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonParser2
{
	/**
	 * Parse a JSON array into an ArrayList of the specified Class
	 * 
	 * @param array JSONArray object to parse
	 * @param type Class to parse JSONObjects into
	 * @return ArrayList of specified type
	 */
	public static <T> ArrayList<T> parse(JSONArray array, Class<T> type)
	{
		if (array == null) return null;
		
		ArrayList<T> list = new ArrayList<T>();
			
		for (int i = 0; i < array.length(); i++)
		{
			list.add((T)parse(array.optJSONObject(i), type));
		}
		
		return list;
	}
	
	/**
	 * Parses a JSONObject and returns a new instance of specified type
	 * 
	 * @param obj JSONOBject to parse
	 * @param type Class to parse JSONObject into
	 * @return new instance of class
	 */
	public static <T> T parse(JSONObject obj, Class<T> type)
	{
		if (obj == null) return null;
		
		try
		{
			T returnObj = type.newInstance();
		
			Field[] fields = returnObj.getClass().getDeclaredFields();
		
			for (Field field : fields)
			{
				Annotation[] annotations = field.getAnnotations();
			
				// If the field has no annotations, it's not up for parsing
				if (annotations.length == 0) 
				{
					// Continue to next field.
					continue;
				}
			
				// Enumerate through annotations and see if we can find a JsonField annotation
				for (Annotation annotation : annotations)
				{
					if (annotation instanceof JsonField)
					{
						// Gets name of the JsonField we want to use
						String jsonfield = ((JsonField) annotation).value();
					
						// Set field accessible so we can edit the value
						field.setAccessible(true);
					
						Object value = obj.get(jsonfield);
						
						// Long list of checks to see what kind of Object
						// the JSON data is and casts accordingly
						if (value instanceof Integer)
						{
							field.set(returnObj, (Integer)value);
						}
						else if (value instanceof String)
						{
							// Check to see if value is a JSON Date
							if (isJsonDate((String)value))
								field.set(returnObj, JsonDate.Deserialize((String)value));
							else
								field.set(returnObj, (String)value);
						}
						else if (value instanceof Boolean)
						{
							field.set(returnObj, (Boolean)value);
						}
						else if (value instanceof Long)
						{
							field.set(returnObj, (Long)value);
						}
						else if (value instanceof Double)
						{
							field.set(returnObj, (Double)value);
						}
						else if (value instanceof Character)
						{
							field.set(returnObj, (Character)value);
						}
						else if (value instanceof JSONArray)
						{
							// Tries to carve out what class this field is (ArrayList<Class>)
							// and parse it again!
							Type arrType = field.getGenericType();
							
							if (arrType instanceof ParameterizedType)
							{
								ParameterizedType pt = (ParameterizedType)arrType;
								
								if (pt != null)
								{
									if (pt.getActualTypeArguments().length > 0)
									{
										Type model = pt.getActualTypeArguments()[0];
										Class<?> g = getClass(model);
										
										field.set(returnObj, parse((JSONArray)value, g));
									}
								}
							}
						}
						else
						{
							field.set(returnObj, null);
						}
								
						break;
					}
				}
			
			
			}
		
			return returnObj;
		}
		catch(InstantiationException e)
		{
			Log.e("JSONParser", e.getMessage() + "\n" + e.getStackTrace());
		}
		catch (IllegalAccessException e)
		{
			Log.e("JSONParser", e.getMessage() + "\n" + e.getStackTrace());
		}
		catch (JSONException e)
		{
			Log.e("JSONParser", e.getMessage() + "\n" + e.getStackTrace());
		}
		
		return null;
	}
	
	public static boolean isJsonDate(String value)
	{
		if (value.startsWith("/Date(") && value.endsWith(")/")) return true;
		if (value.startsWith("\\/Date(") && value.endsWith(")\\/")) return true;
		
		return false;
	}
	
	/**
	   * Get the underlying class for a type, or null if the type is a variable type.
	   * 
	   * COPYRIGHT 2007 Ian Robertson
	   * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
	   * 
	   * @param type the type
	   * @return the underlying class
	   */
	public static Class<?> getClass(Type type) {
	    if (type instanceof Class) {
	      return (Class) type;
	    }
	    else if (type instanceof ParameterizedType) {
	      return getClass(((ParameterizedType) type).getRawType());
	    }
	    else if (type instanceof GenericArrayType) {
	      Type componentType = ((GenericArrayType) type).getGenericComponentType();
	      Class<?> componentClass = getClass(componentType);
	      if (componentClass != null ) {
	        return Array.newInstance(componentClass, 0).getClass();
	      }
	      else {
	        return null;
	      }
	    }
	    else {
	      return null;
	    }
	  }
}
