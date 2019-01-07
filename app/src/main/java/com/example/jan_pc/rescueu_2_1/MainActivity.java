    package com.example.jan_pc.rescueu_2_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private SignInButton SignIn;
    private TextView Name, Email;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private String googleauth;
    private GoogleSignInClient mGoogleSignInClient;

    SharedPreferences sharedPreferences;
    public static final String text ="text";

    private GoogleSignInAccount googledata = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        findViewById(R.id.sign_in_button).setOnClickListener(this);



        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);



        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jan_pc.rescueu_2_1", Context.MODE_PRIVATE);

    }

    //OnClock switch Case
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    //Startet die Google Login Activity
    private void signIn(){

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_CODE);

    }

    //Behandelt das Resultat von GoogleLogin


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            googleauth = account.getId();

            updateUI(true);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            Log.w("Login Fehler", "signInResult:failed code=" + e.getStatusCode());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
           updateUI(true);
        }
        super.onStart();
    }
}
