package com.geckolandmarks.apiexample;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class LandmarkApiActivity extends MapActivity {
    private LandmarkOverlay mapOverlay;
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
		mapOverlay = new LandmarkOverlay(this);
		mapOverlay.addOverlay(0.0, 0.0, 0);
		mapView.getOverlays().add(mapOverlay);
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
    
    private void loadLandmarks() {
        mapView.getController().animateTo(myLocationOverlay.getMyLocation());
		setStatus("Loading landmark data...");
	}

}