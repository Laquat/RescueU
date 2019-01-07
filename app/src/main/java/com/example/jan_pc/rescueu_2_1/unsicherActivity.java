package com.example.jan_pc.rescueu_2_1;


import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

public class unsicherActivity extends AppCompatActivity {

    private Boolean aktiviert = false;
    private CameraManager cameraManager;
    private String cameraId = null;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

         mp = MediaPlayer.create(this, R.raw.sirenen);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsicher);

        SwipeButton swipeButton = (SwipeButton)findViewById(R.id.swipe_btn);
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {




            //Swipe Button wird Betätigt
            @Override
            public void onStateChange(boolean active) {


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




            }
        });
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
        }
    }
}
