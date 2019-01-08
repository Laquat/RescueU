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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class KrankenwagenAuswahlActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    //zu Testzwecken eigene SMS
    private String phoneNo = "015778863007";
    private String gps;
    private String message;
    private String nach_name;
    private String vor_name;
    private String handycap;
    private String medikamente;
    private String ownphone;

    private Switch s1;
    private Switch s2;
    private Switch s3;

    private boolean r1,r2,r3;

    EditText customtext;

    private SharedPreferences sharedPreferences;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Criteria criteria;
    private String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_krankenwagen_auswahl);

        s1 = (Switch)findViewById(R.id.switch1);
        s2 = (Switch)findViewById(R.id.switch2);
        s3 = (Switch)findViewById(R.id.switch3);

        r1=false;r2=false;r3=false;

        customtext = findViewById(R.id.customText);

        //Speicher einrichten
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

        save();

        getgps();


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (s1.isChecked())
                    r1 = true;
                else
                    r1 = false;
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1, str2;
                if (s2.isChecked())
                    r2 = true;
                else
                    r2 = false;
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1, str2;
                if (s3.isChecked())
                    r3 = true;
                else
                    r3 = false;
            }
        });

    }
    //Senden Wurde geklickt
    public void sendBtn(View view){
        buildmessage();

        sendsms();

        Intent intent = new Intent(this, hauptmenueActivity.class);
        startActivity(intent);
    }

    //Message erstellen
    public void buildmessage(){
        message = "Dies ist ein Automatischer Notruf von " + vor_name + " " + nach_name + ". Ich benötige einen Krankenwagen, meine Position ist : " + gps + ".";

        if(r1){
            message += " An meiner Position befindet sich eine Verletzte Person. ";
        }
        if(r2){
            message += " An meiner Position befindet sich eine Erkranke Person. ";
        }
        if(r2){
            message += " An meiner Position wird ein Notarzt benötigt. ";
        }

        message += " Ich habe folgende Behinderungen: " + handycap + ".";
        message += " Ich nehme folgende Medikamente: " + medikamente + ".";
        message += " Meine Telefonnummer ist: " + ownphone + ".";
        message += "Weitere Informationen: " + customtext.getText().toString();

    }

    //Message Versenden
    public void sendsms(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
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

    //GPS daten abfragen
    public void getgps(){


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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
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


    }

    //Benutzerinformationen laden
    public void save(){
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
        if(sharedPreferences.contains("setting_handycap")){
            handycap = sharedPreferences.getString("setting_handycap",null);
        }
        if(sharedPreferences.contains("setting_medikamente")){
            medikamente = sharedPreferences.getString("setting_medikamente",null);
        }
        if(sharedPreferences.contains("setting_phone")){
            ownphone = sharedPreferences.getString("setting_phone",null);
        }
    }

    //Permission abfragen
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
