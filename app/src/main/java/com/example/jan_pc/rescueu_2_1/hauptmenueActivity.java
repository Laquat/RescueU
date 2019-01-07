package com.example.jan_pc.rescueu_2_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class hauptmenueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int item_id = item.getItemId();

        switch (item_id){
            case R.id.action_settings:
                Intent intent = new Intent(this,settingsActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void settingsBtn(View view){


        Intent intent = new Intent(this,settingsActivity.class);
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
