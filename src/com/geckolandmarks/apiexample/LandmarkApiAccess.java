package com.geckolandmarks.apiexample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LandmarkApiAccess {
	
	private static final int HTTP_RETRY_COUNT = 5,
			HTTP_RETRY_SLEEP_MS = 1000;

	// Note: Replace with your own Landmark API key for proper testing
	private static final String LANDMARK_API_KEY = "EXAMPLE_KEY_3edaba1953abf86";
	public static final int COUNT = 30;
	
	private static final String LANDMARK_API_BASE_URL = "http://api.geckolandmarks.com/json?lat=%f&lon=%f&api_key=" + LANDMARK_API_KEY
			+ "&count=" + COUNT;
	
	public static JSONArray loadNearestLandmarks( final double lat, final double lon ) throws IOException, JSONException {
		byte[] rawData = getDataFromHttp( String.format(LANDMARK_API_BASE_URL, lat, lon) );
		JSONObject container = new JSONObject( new String( rawData ) );
		return container.getJSONArray("landmarks");
	}
	
	/**
	 * Loads data from the given url with max HTTP_RETRY_COUNT retries
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static byte[] getDataFromHttp( String url ) throws IOException {
    	for(int retries=0; retries<HTTP_RETRY_COUNT; retries++) {
    		try {
		    	// Inspiration from http://stackoverflow.com/questions/4457492/
		    	HttpClient httpClient = new DefaultHttpClient();
		        HttpGet httpget = new HttpGet(url);
		        HttpResponse response = httpClient.execute(httpget);
		        HttpEntity entity = response.getEntity();
		
		        if (entity != null) {
		        	// Use the HTTP Client to read the data
			        ByteArrayOutputStream baos = new ByteArrayOutputStream();
			        entity.writeTo(baos);
				    return baos.toByteArray();
		        }
    		} catch (Exception e) {
    			// Do nothing and retry
    		}
        	try {
				Thread.sleep(HTTP_RETRY_SLEEP_MS);
			} catch (InterruptedException e) {
				// Ignore
			}
    	}
    	throw new IOException("getDataFromHttp() failed permanently");
	}

}
