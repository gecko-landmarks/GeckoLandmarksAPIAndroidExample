Gecko Landmarks API Android Example
===============

An example Android application that uses the [Landmark API](http://geckolandmarks.com/landmark_api.html) to fetch local landmark data and show that on a Google Map.

This builds on the Google Maps example available at http://developer.android.com/resources/tutorials/views/hello-mapview.html

To build and run the example, you must:
1. Have Google APIs installed in Eclipse. See http://developer.android.com/sdk/adding-components.html for more details. The example uses Google API v10 as target
2. Register your development certificate with Google, see https://developers.google.com/maps/documentation/android/mapkey
3. Insert your Google Maps API key into res/layout/main.xml:
	...
	android:apiKey="INSERT_YOUR_API_KEY"
	...
