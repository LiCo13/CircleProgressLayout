package com.example.clicea.circleprogresslayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class CircleActivity extends AppCompatActivity {


    CircleProgressLayout mNewCustomViewGroup;
    com.example.clicea.circleprogresslayout.likePitz.CircleProgressLayout mPitz;

    Button btn;

    int progreso = 0;
    private Handler progressBarbHandler = new Handler();

    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

btn=(Button) findViewById(R.id.button);
        mNewCustomViewGroup = (CircleProgressLayout) findViewById(R.id.circle_progress_bar);
        mPitz = (com.example.clicea.circleprogresslayout.likePitz.CircleProgressLayout) findViewById(R.id.cpl_pitz);

//mNewCustomViewGroup.setIndeterminate(true);
//mOriginal.setIndeterminate(true);
//mPitz.setIndeterminate(true);






        SeekBar seekBar = (SeekBar) findViewById(R.id.seek);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mNewCustomViewGroup.setProgressWithAnimation(progress);
                mPitz.setProgressWithAnimation(progress);


//                if (fromUser) {
//                    mNewCustomViewGroup.setProgressWithAnimation(progress);
//                    mPitz.setProgressWithAnimation(progress);
//                } else {
//                    mNewCustomViewGroup.setProgress(progress);
//                    mPitz.setProgress(progress);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        mNewCustomViewGroup.setProgress(progreso);


         t=new Thread(new Runnable() {
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
                            mNewCustomViewGroup.setProgress(progreso);
                            mPitz.setProgress(progreso);

                        }
                    });

                    if (progreso == 100) {
                        progreso = 0;
                    }
                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        t.stop();
    }
}
