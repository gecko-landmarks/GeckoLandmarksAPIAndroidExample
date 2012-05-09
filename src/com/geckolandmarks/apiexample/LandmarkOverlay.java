package com.geckolandmarks.apiexample;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Inspired by
 * http://developer.android.com/resources/tutorials/views/hello-mapview.html
 */
public class LandmarkOverlay extends ItemizedOverlay<OverlayItem> {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext;
	
	// Use an icon from default Android graphics
	static final int MARKER_ID = android.R.drawable.btn_star_big_on;

	public LandmarkOverlay(Context context) {
		super( boundCenterBottom(
				context.getResources().getDrawable(MARKER_ID)) );
		mContext = context;
	}
	
	public void clearOverlays() {
		mOverlays.clear();
	}
	
	/**
	 * Add a single overlay item
	 */
	public void addOverlay(double lat, double lon, int iconIndex) {
		// Note: Title and description are not given, not used in current UI
		OverlayItem overlay = new OverlayItem(
				new com.google.android.maps.GeoPoint( (int)(lat*1E6), (int)(lon*1E6) ), 
				"", "");
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
}
