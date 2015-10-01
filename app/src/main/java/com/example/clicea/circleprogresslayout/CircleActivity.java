package com.example.clicea.circleprogresslayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class CircleActivity extends AppCompatActivity {



    CircleProgressLayout CustomViewGroup;
    int progreso=0;
    private Handler progressBarbHandler = new Handler();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);


        CustomViewGroup = (CircleProgressLayout) findViewById(R.id.circle_progress_bar);
        CustomViewGroup.setProgress(progreso);


        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    progreso += 1;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            CustomViewGroup.setProgress(progreso);

                        }
                    });

                    if (progreso == 100) {
                        progreso = 0;
                    }
                }
            }
        }).start();

    }


}
