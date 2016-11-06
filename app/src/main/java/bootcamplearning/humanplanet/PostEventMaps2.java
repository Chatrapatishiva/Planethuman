package bootcamplearning.humanplanet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostEventMaps2 extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;
    String lati,longi,usereventlocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event_maps);

        Button button=(Button)findViewById(R.id.nextb);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapp);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

        }else{
            showGPSDisabledAlertToUser();
        }
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Geocoder.isPresent()){
                    try {

                        String location ="India";
                        Geocoder gc = new Geocoder(PostEventMaps2.this);
                        List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

                        List<Double> ll = new ArrayList<Double>(addresses.size()); // A list to save the coordinates if they are available
                        for(android.location.Address a : addresses){
                            if(a.hasLatitude() && a.hasLongitude()){
                                ll.add(a.getLatitude());
                                ll.add(a.getLongitude());
                                lati=String.valueOf(ll.get(0));
                                longi=String.valueOf(ll.get(1));



                            }
                        }
                    } catch (IOException e) {
                        // handle the exception
                    }
                }

            }
        });*/

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            LatLng sydney = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            Geocoder geocoder;
            List<android.location.Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
             String latitude=getIntent().getExtras().getString("Lati");
            String longitude=getIntent().getExtras().getString("Longi");

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
               String city = addresses.get(0).getLocality();
               String state = addresses.get(0).getAdminArea();
               String country = addresses.get(0).getCountryName();
               String postalCode = addresses.get(0).getPostalCode();
               String knownName = addresses.get(0).getFeatureName();
                String local=addresses.get(0).getLocality();
                Intent i=new Intent(getApplicationContext(),PostTitleSum.class);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("country", country);
                i.putExtra("postalcode", postalCode);
                i.putExtra("knownplace", knownName);
                i.putExtra("Locality",local);
                i.putExtra("stuff",getIntent().getExtras().getString("stuff"));
                startActivity(i);
                finish();



            } catch (Exception e) {
                e.printStackTrace();
            }
















            // Toast.makeText(MapsActivity.this, String.valueOf(mLastLocation.getLatitude()), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MapsActivity.this, String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();

            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }


    }


    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to Use App")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(PostEventMaps2.this, "Connection Suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(PostEventMaps2.this, "Couldn't Get Location", Toast.LENGTH_LONG).show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}


