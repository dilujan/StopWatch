package com.dilujan.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //CT-2016-012

    //Declare member variable
    FloatingActionButton btn_start;
    FloatingActionButton btn_pause;
    FloatingActionButton btn_stop;
    ImageView ic_anchor;
    TextView time_view;

    //Animation object variable
    ObjectAnimator anim;

    //Number of seconds displayed on the stopwatch.
    private int seconds = 0;

    //Is the stopwatch running?
    private boolean running;
    private boolean wasRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initials the layout Views
        btn_start = findViewById(R.id.play);
        btn_stop = findViewById(R.id.stop);
        btn_pause = findViewById(R.id.pause);
        ic_anchor = findViewById(R.id.anchor);

        //Initials and load the animation
        anim = ObjectAnimator.ofFloat(ic_anchor, "rotation", 0, 360);
        anim.setDuration(4000);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(ObjectAnimator.RESTART);


        if(savedInstanceState != null){

            //Get the previous state of the stopwatch,
            // if the activity has been destroyed and recreated.
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");

        }

        //increment the number of seconds and display
        //the number of seconds in the text view.
        runTimer();

    }

    //Save the state of the stopwatch. if it's about to be destroyed.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds",seconds);
        savedInstanceState.putBoolean("running",running);
        savedInstanceState.putBoolean("wasRunning",wasRunning);
    }

    //If the activity is paused, stop the stopwatch.
    @Override
    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    //If the activity is resumed, start the stopwatch again if it was running previously.
    @Override
    protected void onResume() {
        super.onResume();
        if(wasRunning) {
            running = true;
        }
    }

    //Start the stopwatch running, when the startWatch button is clicked.
    //Resumed the stopwatch, if the  stopwatch has been paused
    public void startWatch(View view){
        running = true;

        //Start and resume the animation
        if(seconds == 0) {
            anim.start();
        } else {
            anim.resume();
        }
    }

    //Stop the stopwatch running, when the stopWatch button is clicked.
    public  void stopWatch(View view){
        running = false;
        seconds = 0;

        //Stop the animation
        anim.end();

    }

    //Pause the stopwatch running, when the pauseWatch button is clicked.
    public void pauseWatch(View view){
        running = false;

        //Pause the animation
        anim.pause();

    }

    //Sets the NUmber of seconds on the timer.
    //The runTimer() method uses a Handler to increment the seconds and update the text view.
    private void runTimer() {

        //Get the text view(TimeView).
        time_view = findViewById(R.id.timeView);

        final Handler handler = new Handler();

        //The post() method processes code immediately.
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                //Format the time
                String time = String.format(Locale.getDefault(),"%d:%02d:%02d",
                        hours,minutes,secs);

                //Set the text view the time
                time_view.setText(time);

                //Increment the seconds
                if(running) {
                    seconds++;
                }

                //Post the code again with a delay of 1 second.
                handler.postDelayed(this,1000);
            }
        });
    }
}