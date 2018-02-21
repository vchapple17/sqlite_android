package com.example.valchapple.sqlite;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {
    //Constants
    private static final String TAG = "MAIN";
    private static final int LOCATION_PERMISSION_RESULT = 17;
    private static final double DEFAULT_LAT = 44.5;
    private static final double DEFAULT_LON = -123.2;

    // Location
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;

    // Location Services API
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    // Outlets
    private EditText textView;
    private View mLayout;

    // Database
    private SQLiteDatabase mDB;
    private SQLiteDBHelper mDBHelper;
    private SimpleCursorAdapter mCursorAdaptor;
    private Cursor mCursor;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check location permissions
        if (checkPermissions() == false) {
            // Ask for permission
            Log.d(TAG, "onCreate Location Denied.");
            requestLocationPermission();
        } else {
            // Get location
            Log.d(TAG, "onCreate startLocationUpdates.");
            startLocationUpdates();
        }



        //Database Helper and writable DB
        mDBHelper = new SQLiteDBHelper(this);
        mDB = mDBHelper.getWritableDatabase();

        populateTable();


        // Connect Submit Button
        final Button buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit Clicked");
                submitText();
            }
        });

    }

    // Populate Table
    public void populateTable() {
        Log.d(TAG, "populateTable START.");
        // Check that database and adaptors setup properly
        if (mDB != null) {
            try {
                // Handle if Cursor is not closed
                if (mCursorAdaptor != null && mCursorAdaptor.getCursor() != null) {
                    if (!mCursorAdaptor.getCursor().isClosed()) {
                        mCursorAdaptor.getCursor().close();
                    }
                }
                // Setup Cursor
                mCursor = mDB.query(SQLiteDB.LocationTable.TABLE_NAME,
                        new String[]{SQLiteDB.LocationTable._ID,
                                SQLiteDB.LocationTable.COLUMN_NAME_TEXT_STRING,
                                SQLiteDB.LocationTable.COLUMN_NAME_LATITUDE,
                                SQLiteDB.LocationTable.COLUMN_NAME_LONGITUDE},
                        null,
                        null,
                        null,
                        null,
                        null
                );

                //Send data to ListView wih adapter called listview
                ListView listView = findViewById(R.id.listview);
                mCursorAdaptor = new SimpleCursorAdapter(this,
                        R.layout.layout_list_view_row,
                        mCursor,
                        new String[]{SQLiteDB.LocationTable.COLUMN_NAME_TEXT_STRING,
                                SQLiteDB.LocationTable.COLUMN_NAME_LATITUDE,
                                SQLiteDB.LocationTable.COLUMN_NAME_LONGITUDE},
                        new int[]{R.id.list_textview, R.id.list_textview_lat, R.id.list_textview_lon},
                        0
                );
                listView.setAdapter(mCursorAdaptor);
                Log.d(TAG, "populateTable Adaptor set.");
            } catch (Exception e) {
                Log.d(TAG, "populateTable Error loading data from database.");
            }
        }
    }

    // Handle Click and Location Permissions
    private void submitText() {
        // Get Text
        // Connect Edit Text
        textView = findViewById(R.id.editText);

        String txt = textView.getText().toString();

        // Check txt is not empty
        if (txt.length() > 0) {
            Log.d(TAG, "submitText Not Empty.");

            // Check location permissions
            if (checkPermissions() == false) {
                // Ask for permission
                Log.d(TAG, "submitText Location Denied.");
                mCurrentLocation = new Location("default");
                mCurrentLocation.setLatitude(DEFAULT_LAT);
                mCurrentLocation.setLongitude(DEFAULT_LON);
                saveTextLabel();

            } else {
                // Get location
                Log.d(TAG, "submitText Logging with location.");
                saveLastLocation();
            }
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Additional rationale for the use of the permission with button to request
            // Reference; Google's Examples
            Log.d(TAG, "requestLocationPermission Pre-Prompt Location Permission.");
            Snackbar.make(mLayout, R.string.locationPermissionRequired,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.okButton, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    Log.d(TAG, "requestLocationPermission OK to request Location Permission.");
                    startLocationPermissionRequest();
                }
            }).show();

        } else {
            Log.d(TAG, "requestLocationPermission Requesting Location Permission.");
            startLocationPermissionRequest();
        }
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_RESULT);    // IDENTIFIER FOR REQUEST CALLBACK
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == LOCATION_PERMISSION_RESULT) {
            // Analyze for location permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Get Location
                Log.d(TAG, "onRequestPermissionsResult Permission granted");
                startLocationUpdates();
            }
//            else {
//                // Permission request was denied.
//                mCurrentLocation = new Location("default");
//                mCurrentLocation.setLatitude(DEFAULT_LAT);
//                mCurrentLocation.setLongitude(DEFAULT_LON);
//            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void saveLastLocation() {
        Log.d(TAG, "updateLocation with current location.");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d(TAG, "Location returned.");
                            onLocationChanged(location);
                        } else {
                            Log.d(TAG, "Null location returned.");
                        }
                        saveTextLabel();
                    }
                });
    }

    private void saveTextLabel() {
        Log.d(TAG, String.valueOf(mCurrentLocation.getLatitude()));
        Log.d(TAG, String.valueOf(mCurrentLocation.getLongitude()));

        if (mDB != null && textView != null) {
            ContentValues vals = new ContentValues();
            vals.put(SQLiteDB.LocationTable.COLUMN_NAME_TEXT_STRING,
                    textView.getText().toString());
            vals.put(SQLiteDB.LocationTable.COLUMN_NAME_LATITUDE,
                    String.valueOf(mCurrentLocation.getLatitude()));
            vals.put(SQLiteDB.LocationTable.COLUMN_NAME_LONGITUDE,
                    String.valueOf(mCurrentLocation.getLongitude()));
            mDB.insert(SQLiteDB.LocationTable.TABLE_NAME, null, vals);
            populateTable();
        } else {
            Log.d(TAG, "saveTextLabel Unable to access database for writing.");
        }
    }

    // Trigger new location updates at interval
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {
        // Reference: https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        }, Looper.myLooper());
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        mCurrentLocation = location;
        Log.d(TAG, String.valueOf(mCurrentLocation.getLatitude()));
        Log.d(TAG, String.valueOf(mCurrentLocation.getLongitude()));
    }
}
