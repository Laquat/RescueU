package com.example.jan_pc.rescueu_2_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class hauptmenueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);



    }


    public void settingsBtn(View view){


        Intent intent = new Intent(this,settingsActivity.class);
        startActivity(intent);

    }

    public void mapBtn (View view){
        Intent intent = new Intent (this, MapsActivity.class);
        startActivity(intent);

        }

    public void notfallBtn (View view){
        Intent intent = new Intent (this, notfallActicity.class);
        startActivity(intent);

    }

    public void unsicherBtn (View view){
        Intent intent = new Intent(this, unsicherActivity.class);
        startActivity(intent);
    }

}
