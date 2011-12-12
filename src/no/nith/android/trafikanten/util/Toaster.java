/**
 * Toaster
 * 
 * Helper class for showing toasts
 * 
 * Written by Henning M. Stephansen © 2011
 */
package no.nith.android.trafikanten.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster
{
	public static void show(Context context, int id)
	{
		String message = context.getResources().getString(id);
		
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
	}
	
	public static void show(Context context, int id, Object...objects)
	{
		String message = String.format(context.getResources().getString(id), objects);
		
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
	}
	
	public static void show(Context context, String message)
	{
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		toast.show();
	}
}
