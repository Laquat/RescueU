package com.example.jan_pc.rescueu_2_1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class notfallActicity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    String phoneNo;
    String name;
    String message;
    String gps;

    private String provider;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notfall);


        //Speicher einrichten
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

        //Speicher auf Setting Überprüfen
        if(sharedPreferences.contains("setting_phone")){
            phoneNo = sharedPreferences.getString("setting_phone",null);
        }
        if(sharedPreferences.contains("setting_name")){
            name = sharedPreferences.getString("setting_name",null);
        }

        //GPS Aktivieren
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //GPS Methoden
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gps = "Latitude:" + location.getLatitude() + "\n Longitude:" + location.getLongitude();
                Log.i("gps", location.toString());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        //Permission für GPS Überprüfen
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            //GPS Aktivieren
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

            //Letzte GPS Position bestimmen
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            gps = "Latitude:" + lastKnownLocation.getLatitude() + "\n Longitude:" + lastKnownLocation.getLongitude();

        }


    }

    //Button Call auswerten + Ausgabe der GPS Koordinaten
    public void callAssistens(View v){

        switch (v.getId()){
            case R.id.police:
                Toast.makeText(getApplicationContext(), "Polizei , "+gps,
                        Toast.LENGTH_SHORT).show();
                Log.d("Button","police");
                break;
            case R.id.arzt:
                Toast.makeText(getApplicationContext(), "Arzt , "+gps,
                        Toast.LENGTH_SHORT).show();
                Log.d("Button","arzt");
                break;
            case R.id.feuerwehr:
                Toast.makeText(getApplicationContext(), "Feuerwehr , "+gps,
                        Toast.LENGTH_SHORT).show();
                Log.d("Button","feuerwehr");
                break;
        }


    }

    //GPS Aufruf
    public void getgps(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


    //Send SMS
    public  void sendmassage(View view) {



        message = "This is a automatic Msg from RescueU your friend " + name + " needs your assistents. The Coordinates are " + gps;



        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        /*
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }*/

    }


    //Permission Request antwort überprüfen
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if(requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Nachricht gesendet an " + phoneNo,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                return;
            }
        }

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);
                }
            }



    }

}
