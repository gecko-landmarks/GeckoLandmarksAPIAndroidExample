Gecko Landmarks API Android Example
===============

An example Android application that uses the [Landmark API](http://geckolandmarks.com/landmark_api.html) to fetch local landmark data and show that on a Google Map.

This builds on the Google Maps example available at http://developer.android.com/resources/tutorials/views/hello-mapview.html

![Gecko Landmark API Android Example screenshot](https://github.com/gecko-landmarks/GeckoLandmarksAPIAndroidExample/raw/master/Example_Victoria_Island_Lagos_NG.png)


Testing the application
===

You can install the application from the provided [APK-file](https://github.com/gecko-landmarks/GeckoLandmarksAPIAndroidExample/raw/master/GeckoLandmarksAPIAndroidExample.apk).


Building the application
===

Before importing the project to Eclipse, you should:

1. Have Google APIs installed in Eclipse. The example uses Google API v7 as default target. See http://developer.android.com/sdk/adding-components.html
2. Register your development certificate with Google to get a Google Maps API key, see https://developers.google.com/maps/documentation/android/mapkey
3. Insert your Google Maps API key into res/layout/main.xml: `android:apiKey="INSERT_YOUR_API_KEY"`

For proper testing, you should also insert your own Landmark API key to src/com/geckolandmarks/apiexample/LandmarkApiAccess.Java, `private static final String LANDMARK_API_KEY = "EXAMPLE_KEY_3edaba1953abf86";`. Contact api@geckolandmarks.com to get your own key. The default example key works but is rate-limited.

After these steps, either download [the full sources in a ZIP-file](https://github.com/gecko-landmarks/GeckoLandmarksAPIAndroidExample/zipball/master) or clone this repo. Then in Eclipse do *File > Import > Existing Projects into Workspace*.

*Note: Proper error and string handling has been omitted from the example to make the code as simple as possible.*
