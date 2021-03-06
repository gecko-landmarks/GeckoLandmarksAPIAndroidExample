package com.geckolandmarks.apiexample;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
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

	private int mTextSize;

	public LandmarkOverlay(Context context) {
		super( boundCenterBottom(
				context.getResources().getDrawable(MARKER_ID)) );
		mContext = context;
		// Text size is 14DP, converted to pixels.
		// See http://developer.android.com/guide/practices/screens_support.html#dips-pels
		mTextSize = (int)(14.0f * context.getResources().getDisplayMetrics().density);
	}

	/**
	 * Note: One MUST call doPopulate()() after calling this to make the landmarks visible.
	 */
	public void addLandmark(double lat, double lon, String title, String description) {
		OverlayItem overlay = new OverlayItem(
				new com.google.android.maps.GeoPoint( (int)(lat*1E6), (int)(lon*1E6) ), 
				title, description );
		mOverlays.add(overlay);
	}

	public void doPopulate() {
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

	/**
	 * Overridden to show text below the icons.
	 * 
	 * See http://stackoverflow.com/questions/5070492 for more details.
	 */
	@Override
	public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow)
	{
		super.draw(canvas, mapView, shadow);

		if (shadow == false)
		{
			//cycle through all overlays
			for (int index = 0; index < mOverlays.size(); index++)
			{
				OverlayItem item = mOverlays.get(index);

				// Converts lat/lng-Point to coordinates on the screen
				GeoPoint point = item.getPoint();
				Point ptScreenCoord = new Point() ;
				mapView.getProjection().toPixels(point, ptScreenCoord);

				//Paint
				Paint paint = new Paint();
				paint.setTextAlign(Paint.Align.CENTER);
				paint.setTextSize(mTextSize);
				paint.setAntiAlias(true);

				//Show text below the icon, black surrounded with 1 white pixel
				paint.setColor(Color.WHITE);
				final int[] X_DIFF = { -1, 1, 0, 0 };
				final int[] Y_DIFF = { 0, 0, -1, 1 };
				for(int s=0; s<X_DIFF.length; s++) {
					canvas.drawText(item.getTitle(), 
							ptScreenCoord.x + X_DIFF[s], 
							ptScreenCoord.y+mTextSize + Y_DIFF[s], paint);
				}
				paint.setColor(Color.BLACK);
				canvas.drawText(item.getTitle(), ptScreenCoord.x, ptScreenCoord.y+mTextSize, paint);
			}
		}
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		String message = "Landmark selected:\n" + item.getSnippet();
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
		return true;
	}    
}
