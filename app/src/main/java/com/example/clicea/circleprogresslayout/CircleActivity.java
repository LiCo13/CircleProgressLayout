package com.example.clicea.circleprogresslayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class CircleActivity extends AppCompatActivity {


    CircleProgressLayout mNewCustomViewGroup;
    CircleProgressLayout mPitzLinear;
    CircleProgressLayout mPitzSwe;
    CircleProgressLayout mDefault;

    Button btn;




    Boolean indeterminate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        btn = findViewById(R.id.button);
        mNewCustomViewGroup = findViewById(R.id.cpl_pitz_radial);
        mPitzLinear = findViewById(R.id.cpl_pitz);
        mPitzSwe = findViewById(R.id.cpl_pitz_sweep);
        mDefault = findViewById(R.id.cpl_default);


        mNewCustomViewGroup.setIndeterminate(indeterminate);
        mPitzSwe.setIndeterminate(indeterminate);
        mPitzLinear.setIndeterminate(indeterminate);
        mDefault.setIndeterminate(indeterminate);

        SeekBar seekBar = findViewById(R.id.seek);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

//                mNewCustomViewGroup.setProgress(progress);
//                mPitzLinear.setProgress(progress);
//                mPitzSwe.setProgress(progress);
//                mDefault.setProgress(progress);

                mNewCustomViewGroup.setProgressWithAnimation(progress);
                mPitzLinear.setProgressWithAnimation(progress);
                mPitzSwe.setProgressWithAnimation(progress);
                mDefault.setProgressWithAnimation(progress);


//                if (fromUser) {
//                    mNewCustomViewGroup.setProgressWithAnimation(progress);
//                    mPitzLinear.setProgressWithAnimation(progress);
//                } else {
//                    mNewCustomViewGroup.setProgress(progress);
//                    mPitzLinear.setProgress(progress);
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



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                indeterminate = !indeterminate;
                mNewCustomViewGroup.setIndeterminate(indeterminate);
                mPitzSwe.setIndeterminate(indeterminate);
                mPitzLinear.setIndeterminate(indeterminate);
                mDefault.setIndeterminate(indeterminate);


            }
        });
    }


}
