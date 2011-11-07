package no.nith.android.trafikanten.http;

import java.io.*;
import java.net.*;

import com.loopj.android.http.*;

public class WebClient
{
	public static String get(String uri) throws MalformedURLException, IOException
	{
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		InputStream inStream = new BufferedInputStream(connection.getInputStream());
		
		return readStream(inStream);
	}
	
	public static void getAsync(String url, AsyncHttpResponseHandler callback)
	{
		AsyncHttpClient httpClient = new AsyncHttpClient();
		
		httpClient.get(url, callback);
	}
	
	public static String readStream(InputStream stream) throws IOException
	{
		String line = null;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		StringBuilder sb = new StringBuilder();
		
		while ((line = reader.readLine()) != null)
		{
			sb.append(line);
		}
		
		return sb.toString();
	}
}
