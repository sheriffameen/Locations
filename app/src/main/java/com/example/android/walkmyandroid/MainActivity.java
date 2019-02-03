/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.walkmyandroid;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


//DEVICE LOCATION
//1.Add ACCESS_FINE_LOCATION permission, because you want the most accurate location information possible
public class MainActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "Sheriff Activity" ;
    private static final String TRACKING_LOCATION_KEY = "Tracking Key";
    private Button mLocationButton;
    //7.In your MainActivity class, create a member variable of the Location type called mLastLocation
//    private Location mLastLocation;
    //9.Create a member variable of the FusedLocationProviderClient type called mFusedLocationClient
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView mLocationTextView;
    //SETUP THE UI AND THE METHOD STUBS
    //28.declare the member variables mAndroidImageView (of type ImageView) and mRotateAnim (of type AnimatorSet)
    private ImageView mAndroidImageView;
    private AnimatorSet mRotateAnim;
    //31.Create a boolean member variable called mTrackingLocation. Boolean primitives default to
    // false, so you do not need to initialize mTrackingLocation
    private boolean mTrackingLocation;
    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationButton = findViewById(R.id.button_location);

        //8.Find the location TextView by ID (textview_location) in onCreate(). Assign the TextView
        // to a member variable called mLocationTextView
        mLocationTextView = findViewById(R.id.textview_location);

        //9.Initialize mFusedLocationClient in onCreate() with the following code
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //29.In the onCreate() method, find the Android ImageView by ID and assign it to mAndroidImageView.
        // Then find the animation included in the starter code by ID and assign it to mRotateAnim.
        // Finally set the Android ImageView as the target for the animation
        mAndroidImageView = (ImageView) findViewById(R.id.imageview_android);

        mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator
                (this, R.animator.rotate);

        mRotateAnim.setTarget(mAndroidImageView);



        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //3.Create a method stub called startTrackingLocation() that takes no arguments and doesn't
                // return anything. Invoke the startTrackingLocation() method from the button's onClick() method.
                //startTrackingLocation();
                //32.Change the onClick() method for the button's onClickListener
                if (!mTrackingLocation) {
                    startTrackingLocation();
                } else {
                    stopTrackingLocation();
                }
            }
        });
        //35.At the bottom of onCreate(), create a new LocationCallback object and assign it to a
        // member variable called mLocationCallback
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //check mTrackingLocation. If mTrackingLocation is true, execute FetchAddressTask(),
                // and use the LocationResult.getLastLocation() method to obtain the most recent Location object
                if (mTrackingLocation) {
                    new FetchAddressTask(MainActivity.this, MainActivity.this)
                            .execute(locationResult.getLastLocation());
                }

            }
        };

        if (savedInstanceState != null) {
            mTrackingLocation = savedInstanceState.getBoolean(
                    TRACKING_LOCATION_KEY);
        }

    }
    //CREATE THE LOCATION REQUEST OBJECT
    //34.Create a method called getLocationRequest() that takes no arguments and returns a LocationRequest
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }





    //30.Changed a few member variables and refactored the getLocation Method to startTracking Location.
    //Created a stopTrackingLocation method stub.
    private void stopTrackingLocation(){
        //if the you are tracking the location. If you are, stop the animation by calling
        // mRotateAnim.end(), set mTrackingLocation to to false, change the button text back to
        // "Start Tracking Location" and reset the location TextView to show the original hint
        if (mTrackingLocation) {
            mTrackingLocation = false;
            mLocationButton.setText(R.string.start_tracking_location);
            mLocationTextView.setText(R.string.textview_hint);
            mRotateAnim.end();
            //37.Call removeLocationUpdates() on mFusedLocationClient. Pass in the LocationCallback object
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        }

    }
    //2.Create an OnClickListener for the Get Location button in onCreate() in MainActivity.
    private void startTrackingLocation() {
        //3.In the startTrackingLocation() method, check for the ACCESS_FINE_LOCATION permission.
        //If the permission has not been granted, request it.
        //If the permission has been granted, display a message in the logs. (The code below shows
        // a TAG variable, which you declare later, in Task 3.1.)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            //4. define an integer constant REQUEST_LOCATION_PERMISSION. This constant is used to
            // identify the permission request when the results come back in the onRequestPemissionsResult()
            // method. It can be any integer greater than 0
        } else {
            //10.Replace the log statement in the startTrackingLocation() method with the following code
            // snippet. The code obtains the device's most recent location and assigns it to mLastLocation
            //(NEXT TASK IN FETCH ADDRESSTASK CLASS

//                    mFusedLocationClient.getLastLocation().addOnSuccessListener(
//                    new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            //26.Replace the lines that assigns the passed-in location to mLastLocation
//                            //and sets the TextView with the following line of code. This code
//                            // creates a new FetchAddressTask and executes it, passing in the Location
//                            // object. You can also remove the now unused mLastLocation member variable
//                            if (location != null) {
////                                mLastLocation = location;
////                                mLocationTextView.setText(
////                                        getString(R.string.location_text,
////                                                mLastLocation.getLatitude(),
////                                                mLastLocation.getLongitude(),
////                                                mLastLocation.getTime()));
//                                // Start the reverse geocode AsyncTask
//                                new FetchAddressTask(MainActivity.this,
//                                        MainActivity.this).execute(location);
//                            } else {
//                                mLocationTextView.setText(R.string.no_location);
//                            }
//                        }
//
//                    });
                    //REQUEST LOCATION UPDATES
                    //36.To request periodic location updates, replace the call to getLastLocation()
            // in startTrackingLocation() (along with the OnSuccessListener) with the following method
            // call. Pass in the LocationRequest and LocationCallback
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,
                            null /* Looper */);




    }
        //27.Show loading text while the FetchAddressTask runs
        mLocationTextView.setText(getString(R.string.address_text,
                getString(R.string.loading),
                System.currentTimeMillis()));
        //33.start the animation by calling mRotateAnim.start(). Set mTrackingLocation to to true
        // and change the button text to "Stop Tracking Location"
        mRotateAnim.start();
        mTrackingLocation = true;
        mLocationButton.setText(getString(R.string.stop_location));

    }
    //5.Override the onRequestPermissionsResult() method. If the permission was granted, call
    // startTrackingLocation(). Otherwise, show a Toast saying that the permission was denied
    //6. In strings.xml, add a string resource called location_text. Use location_text to display
    // the latitude, longitude, and timestamp of the last known location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //24.update the activity to implement the FetchAddressTask.OnTaskCompleted interface you created
    // and override the required onTaskCompleted() method
    @Override
    public void onTaskCompleted(String result) {
        //25.Update the TextView with the resulting address and the current time
        mLocationTextView.setText(getString(R.string.address_text,result,System.currentTimeMillis()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mTrackingLocation){
            startTrackingLocation();
        }
    }
    //KEYYSSSS: Right now, the app continues to request location updates until the user clicks the
    // button, or until the Activity is destroyed. To conserve power, stop location updates when
    // your Activity is not in focus (in the paused state) and resume location updates when the
    // Activity regains focus:
    @Override
    protected void onPause() {
        super.onPause();
        if (!mTrackingLocation){
            stopTrackingLocation();
            mTrackingLocation = true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(TRACKING_LOCATION_KEY, mTrackingLocation);
        super.onSaveInstanceState(outState);
    }
}
