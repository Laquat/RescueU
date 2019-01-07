package com.example.jan_pc.rescueu_2_1;

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

public class settingsActivity extends AppCompatActivity {

    String phone_number;
    String name;
    SharedPreferences sharedPreferences;
    EditText phone_text;
    EditText name_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        phone_text = (EditText)findViewById(R.id.settingsPhone_number);
        name_text = (EditText)findViewById(R.id.settingsName);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("setting_phone")){
            phone_number = sharedPreferences.getString("setting_phone",null);
            phone_text.setText(phone_number);
        }

        if(sharedPreferences.contains("setting_name")){
            name = sharedPreferences.getString("setting_name",null);
            name_text.setText(name);
        }


    }


    //Settings Speichern
    public void saveBtn (View view){
        phone_number = phone_text.getText().toString();
        name = name_text.getText().toString();
        if(!phone_number.equals("") && !name.equals("")) {

            Log.i("phone", phone_number);
            sharedPreferences.edit().putString("setting_phone", phone_number).apply();
            sharedPreferences.edit().putString("setting_name", name).apply();
            Intent intent = new Intent(this, hauptmenueActivity.class);
            startActivity(intent);
        }
        else{
            if(phone_number.equals("")) {
                Toast.makeText(this, "Bitte eine Telefonnummer eingeben!", Toast.LENGTH_SHORT).show();
            }
            if(name.equals("")) {
                Toast.makeText(this, "Bitte einen Namen eingeben!", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
