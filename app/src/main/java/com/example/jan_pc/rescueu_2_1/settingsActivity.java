package com.example.jan_pc.rescueu_2_1;

import android.graphics.Color;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class settingsActivity extends AppCompatActivity {

    String name;
    String vorname;
    String phone;
    String handycap;
    String medikamente;
    String notfallPin;
    String alarmPin;
    String notKontakt;

    SharedPreferences sharedPreferences;
    EditText phone_text;
    EditText name_text;
    EditText vorname_text;
    EditText handycap_text;
    EditText medikamente_text;
    EditText notKontakt_text;
    EditText notfallPin_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        name_text = (EditText)findViewById(R.id.setting_name);
        vorname_text = (EditText)findViewById(R.id.setting_vorname);
        phone_text = (EditText)findViewById(R.id.setting_phone);
        handycap_text = (EditText)findViewById(R.id.setting_handycap);
        medikamente_text = (EditText)findViewById(R.id.setting_medikamente);
        notfallPin_text = (EditText)findViewById(R.id.setting_notPin);
        notKontakt_text = (EditText)findViewById(R.id.setting_notKontakt);


        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("setting_name")){
            name = sharedPreferences.getString("setting_name",null);
            name_text.setText(name);
        }

        if(sharedPreferences.contains("setting_vorname")){
            vorname = sharedPreferences.getString("setting_vorname",null);
            vorname_text.setText(vorname);
        }

        if(sharedPreferences.contains("setting_phone")){
            phone = sharedPreferences.getString("setting_phone",null);
            phone_text.setText(phone);
        }

        if(sharedPreferences.contains("setting_handycap")){
            handycap = sharedPreferences.getString("setting_handycap",null);
            handycap_text.setText(handycap);
        }

        if(sharedPreferences.contains("setting_medikamente")){
            medikamente = sharedPreferences.getString("setting_medikamente",null);
            medikamente_text.setText(medikamente);
        }

        if(sharedPreferences.contains("setting_notfallPin")){
            notfallPin = sharedPreferences.getString("setting_notfallPin",null);
            notfallPin_text.setText(notfallPin);
        }

        if(sharedPreferences.contains("setting_notKontakt")){
            notKontakt = sharedPreferences.getString("setting_notKontakt",null);
            notKontakt_text.setText(notKontakt);
        }
    }


    //Settings Speichern
    public void saveBtn (View view){

        name = name_text.getText().toString();
        vorname = vorname_text.getText().toString();
        phone = phone_text.getText().toString();

        handycap = handycap_text.getText().toString();
        medikamente = medikamente_text.getText().toString();
        notfallPin = notfallPin_text.getText().toString();

        notKontakt = notKontakt_text.getText().toString();
        if(!phone.equals("") && !name.equals("") && !vorname.equals("")&&
                !handycap.equals("")&& !medikamente.equals("")&& !notfallPin.equals("")&&                         !notKontakt.equals("")){
            Log.i("phone", phone);
            sharedPreferences.edit().putString("setting_phone", phone).apply();
            sharedPreferences.edit().putString("setting_name", name).apply();
            sharedPreferences.edit().putString("setting_vorname", vorname).apply();
            sharedPreferences.edit().putString("setting_handycap", handycap).apply();
            sharedPreferences.edit().putString("setting_medikamente", medikamente).apply();
            sharedPreferences.edit().putString("setting_notfallPin", notfallPin).apply();
            sharedPreferences.edit().putString("setting_notKontakt", notKontakt).apply();
            Intent intent = new Intent(this, hauptmenueActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Bitte füllen Sie alle Felder aus. Sollten Sie kein Handycap besitzen oder Medikamente nehmen geben sie 'Keine' an.", Toast.LENGTH_LONG).show();

            if(notKontakt.equals("")){
                notKontakt_text.setHintTextColor(Color.RED);
            }
            if(phone.equals("")){
                phone_text.setHintTextColor(Color.RED);
            }
            if(name.equals("")){
                name_text.setHintTextColor(Color.RED);
            }
            if(vorname.equals("")){
                vorname_text.setHintTextColor(Color.RED);
            }
            if(handycap.equals("")){
                handycap_text.setHintTextColor(Color.RED);
            }
            if(medikamente.equals("")){
                medikamente_text.setHintTextColor(Color.RED);
            }
            if(notfallPin.equals("")){
                notfallPin_text.setHintTextColor(Color.RED);
            }
        }
    }


}
