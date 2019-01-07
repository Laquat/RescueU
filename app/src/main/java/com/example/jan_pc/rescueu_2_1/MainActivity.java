package com.example.jan_pc.rescueu_2_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private SignInButton SignIn;
    private TextView Name, Email;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private String googleauth;

    SharedPreferences sharedPreferences;
    public static final String text ="text";

    private GoogleSignInAccount googledata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignIn = (SignInButton)findViewById(R.id.gbn_login);
        SignIn.setOnClickListener(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

    }

    //OnClock switch Case
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gbn_login:
                signIn();
                break;
        }
    }

    //Startet die Google Login Activity
    private void signIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

        startActivityForResult(intent, REQ_CODE);

    }

    //Behandelt das Resultat von GoogleLogin
    private void handleResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            googleauth = account.getId();
            googledata = account;


            updateUI(true);



        }
        else{
            updateUI(false);
        }
    }


    //Was soll Passieren wenn erfolgreich oder nicht
    private void updateUI(boolean isLogin){

        if(isLogin){
            Toast.makeText(this, "Success " + googleauth, Toast.LENGTH_SHORT).show();


            if(sharedPreferences.contains("setting_phone")){
                Intent intent = new Intent(this, hauptmenueActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(this, settingsActivity.class);
                startActivity(intent);
            }


        }
        else {
            Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
        }


    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
