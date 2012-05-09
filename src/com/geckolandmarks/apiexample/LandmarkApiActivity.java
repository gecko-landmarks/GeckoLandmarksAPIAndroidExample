package com.geckolandmarks.apiexample;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class LandmarkApiActivity extends MapActivity {
	private MyLocationOverlay myLocationOverlay;
	private MapView mapView;
	private TextView statusText;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        statusText = (TextView) findViewById(R.id.status_text);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
		myLocationOverlay = new com.google.android.maps.MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
            	loadLandmarks();
            }
        });				
    }
    
	@Override
    protected boolean isRouteDisplayed() {
        return false;
    }

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
	}
    
    private void setStatus(final CharSequence text) {
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				statusText.setText(text);
			}
		});
    }
    
    /**
     * Generates a description text of a JSON landmark object.
     * 
     * Does not include fields that are duplicates, for example
     * "Turku, Turku, Varsinais-Suomi, FI" => "Turku, Varsinais-Suomi, FI"
     */
    private String landmarkDescription( JSONObject jsonLandmark ) throws JSONException {
	    final String[] FIELDS = {"name1", "name2", "name3", "ccode" };
	    StringBuffer buffer = new StringBuffer();
	    String previousValue = null;
	    for(String field:FIELDS) {
	    	String value = jsonLandmark.getString(field); 
    		if(value.length()>0) {
    			if(previousValue == null || !previousValue.equals(value)) {
	    			if(buffer.length()>0)
	    				buffer.append("\n");
	    			buffer.append(value);
    			}
   				previousValue = value;
    		}
	    }
		return buffer.toString();    	
    }
    
    private void loadLandmarks() {
    	// Default zoom before landmarks are loaded
    	final int DEFAULT_ZOOM = 15;
    	// Zoom to this many landmarks when loaded
    	final int ZOOM_LANDMARK_COUNT = 8;
		LandmarkOverlay landmarkOverlay = new LandmarkOverlay(this);
    	MapController mapController = mapView.getController();
    	mapController.animateTo(myLocationOverlay.getMyLocation());
    	mapController.setZoom(DEFAULT_ZOOM);
		setStatus("Loading landmark data...");
		GeoPoint location = myLocationOverlay.getMyLocation();
		try {
			JSONArray landmarks = LandmarkApiAccess.loadNearestLandmarks(
					location.getLatitudeE6()/1000000.0, 
					location.getLongitudeE6()/1000000.0 );
			if(landmarks.length() != LandmarkApiAccess.COUNT )
				throw new IOException("Only " + landmarks.length() + 
						" landmarks loaded, should be " + LandmarkApiAccess.COUNT );
			
			// Latitude and longitude span of the nearest landmarks
			// for zooming
			int latSpanE6 = 0, lonSpanE6 = 0;
			for(int i=0; i<landmarks.length(); i++) {
				JSONObject jl = landmarks.getJSONObject(i);
				double jlLat = jl.getDouble("lat");
				double jlLon = jl.getDouble("lon");
				landmarkOverlay.addLandmark(jlLat,
						jlLon,
						jl.getString("name1"),
						landmarkDescription(jl) );
				if(i<ZOOM_LANDMARK_COUNT) {
					int jlLatE6 = (int)(jlLat * 1000000);
					int jlLonE6 = (int)(jlLon * 1000000);
					latSpanE6 = Math.max( latSpanE6, Math.abs( location.getLatitudeE6() - jlLatE6) ); 
					lonSpanE6 = Math.max( lonSpanE6, Math.abs( location.getLongitudeE6() - jlLonE6) );
				}
			}
			landmarkOverlay.doPopulate();
			mapView.getOverlays().add(landmarkOverlay);
			mapController.zoomToSpan(latSpanE6, lonSpanE6);
			setStatus("Please select a landmark");
		} catch (IOException e) {
			setStatus("Server error: " + e.getLocalizedMessage() );
			e.printStackTrace();
		} catch (JSONException e) {
			setStatus("JSON error: " + e.getLocalizedMessage() );
			e.printStackTrace();
		}
	}

}