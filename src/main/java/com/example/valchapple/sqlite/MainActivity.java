package com.example.valchapple.sqlite;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = "MAIN";
    private static final int LOCATION_PERMISSION_RESULT = 17;
    private static final double DEFAULT_LAT = 44.5;
    private static final double DEFAULT_LON = -123.2;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mCurrentLocation;
    private EditText textView;
    private View mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Connect Submit Button
        final Button buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Submit Clicked");
                submitText();
            }
        });

        // Connect ListView and Adapter
        //    Item[] items = Item.getItems(GridViewActivity.this);
        //    GridView gridView = (GridView)findViewById(R.id.gridview);
        //    ItemsAdapter itemsAdapter = new ItemsAdapter(this, items);
        //        gridView.setAdapter(itemsAdapter);
    }

    private void submitText() {
        // Get Text
        // Connect Edit Text
        textView = findViewById(R.id.editText);

        String txt = textView.getText().toString();

        // Check txt is not empty
        if( txt.length() > 0) {
            Log.d(TAG, "submitText Not Empty.");

            // Check location permissions
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {
                // Ask for permission
                Log.d(TAG, "submitText Location Denied.");
                requestLocationPermission();
            }
            else if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Save text and location, if permitted
                    // If not permitted, save as latitude and longitude to 44.5 and ­123.2 respectively
                    // Save Text and Location
                    Log.d(TAG, "submitText Logging with location.");
                    getLastLocation();
            }
        }
    }

    private void requestLocationPermission() {
//        startLocationPermissionRequest();
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
                getLastLocation();
            } else {
                // Permission request was denied.
                mCurrentLocation = new Location("default");
                mCurrentLocation.setLatitude(DEFAULT_LAT);
                mCurrentLocation.setLongitude(DEFAULT_LON);
            }
        }
    }

    // Save Text with location or not
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Log.d(TAG, "updateLocation with current location.");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d(TAG, "Location returned.");
                            mCurrentLocation = location;
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
        Log.d(TAG, this.textView.getText().toString());
    }
}
