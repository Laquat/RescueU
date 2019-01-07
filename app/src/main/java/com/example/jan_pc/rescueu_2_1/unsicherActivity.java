package com.example.jan_pc.rescueu_2_1;


import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

public class unsicherActivity extends AppCompatActivity {

    private Boolean aktiviert = false;
    private CameraManager cameraManager;
    private String cameraId = null;
    private MediaPlayer mp;


    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    String phoneNo;
    String nach_name;
    String vor_name;
    String message;
    String gps;

    private SharedPreferences sharedPreferences;

    private Criteria criteria;
    private String bestProvider;

    private String provider;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String notpin;

    boolean islocked;



    private EditText pin;
    private Button lockButton;
    private SwipeButton swipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        islocked =false;

        //Speicher einrichten
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

        //Speicher auf Setting Überprüfen
        if(sharedPreferences.contains("setting_phone")){
            phoneNo = sharedPreferences.getString("setting_notKontakt",null);
        }
        if(sharedPreferences.contains("setting_name")){
            nach_name = sharedPreferences.getString("setting_name",null);
        }
        if(sharedPreferences.contains("setting_vorname")){
            vor_name = sharedPreferences.getString("setting_vorname",null);
        }
        if(sharedPreferences.contains("setting_notfallPin")){
            notpin = sharedPreferences.getString("setting_notfallPin",null);
        }







         mp = MediaPlayer.create(this, R.raw.sirenen);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsicher);


        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);




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
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //Permission für GPS Überprüfen
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        else{
                //GPS Aktivieren
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, locationListener);

                //Letzte GPS Position bestimmen
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastKnownLocation != null){
                    gps = "Latitude:" + lastKnownLocation.getLatitude() + "\n Longitude:" + lastKnownLocation.getLongitude();
                }

            }


        pin = (EditText) findViewById(R.id.pintext);
        SwipeButton swipeButton = (SwipeButton)findViewById(R.id.swipe_btn);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {

            //Panik Button wird betätigt
            @Override
            public void onStateChange(boolean active) {

                message = "Dies ist eine Automatische Notfall Nachricht von " + vor_name + " " + nach_name + ". Es wurde der Panikbutton aktiviert. Die Aktuelle Position ist " + gps;

                sendmassage();

                //Musik abspielen in schleife
                mp.start();
                mp.setLooping(true);
                mp.setVolume(1, 1);
                aktiviert = true;


                //Versuchen die Kamera zu Aktivieren
                try {
                    cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


                //speerCode
                lock();




            }
        });


    }

    public  void sendmassage() {


        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else {

        }




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
    }




    //GPS Aufruf
    public void getgps(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        //wenn der Swipe Button betätigt wurde Sound und Kamera deaktivieren
        if(aktiviert) {
            try {
                mp.setLooping(false);
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            aktiviert=false;
        }
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


    public void lockBtn(View view){


        String pintext = pin.getText().toString();

        if(notpin.equals(pintext)) {
            islocked = false;
            swipeButton.setVisibility(View.VISIBLE);
            pin.setVisibility(View.INVISIBLE);
            lockButton.setVisibility(View.INVISIBLE);


            if(aktiviert) {
                try {
                    mp.setLooping(false);
                    cameraManager.setTorchMode(cameraId, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                aktiviert=false;
            }

        }
    }


    //Lock Aktivieren
    public void lock(){

        islocked = true;


        swipeButton = (SwipeButton)findViewById(R.id.swipe_btn);
        swipeButton.setVisibility(View.INVISIBLE);

        lockButton = findViewById(R.id.lockBtn);

        lockButton.setVisibility(View.VISIBLE);
        pin.setVisibility(View.VISIBLE);


    }

}
