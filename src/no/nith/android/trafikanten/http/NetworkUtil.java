/**
 * NetworkUtil
 * 
 * Method for checking if network is available
 * 
 * Code: Copyright Thomas Pettersen
 */
package no.nith.android.trafikanten.http;

import android.content.Context;
import android.net.*;

public class NetworkUtil
{
	private static ConnectivityManager conManager;

	/**
	 * Check if network is available
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager != null) {
			if (conManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED
					|| conManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
					|| conManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
					|| conManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING) {
				return true;
			}
		}
		return false;
	}

}
