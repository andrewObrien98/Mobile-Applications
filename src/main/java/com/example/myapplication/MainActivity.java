package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.myapplication.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    private int stepCount = 0;
    private long lastStepTimestamp = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView countText = findViewById(R.id.text_view_step_counter);

        //this part will contain all that is necessary for updating the steps count if the person is moving
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float gForceX = x/SensorManager.GRAVITY_EARTH;
                float gForceY = y/SensorManager.GRAVITY_EARTH;
                float gForceZ = z/SensorManager.GRAVITY_EARTH;

                float normalizedGForce = (float)Math.sqrt(gForceX * gForceX + gForceY * gForceY + gForceZ * gForceZ);

                if(normalizedGForce > 1.5f){
                    long now = System.currentTimeMillis();
                    if(lastStepTimestamp + 500 > now){
                        return;
                    } else {
                        stepCount++;//increment the amount of steps by one
                        countText.setText(stepCount + "");
                    }
                    lastStepTimestamp = now;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //now this is where if the screen is touched then we restart from 0
        findViewById(R.id.constraint_layout_container).setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                stepCount = 0;//increment the amount of steps by one
                countText.setText(stepCount + "");
                return true;
            }
            return false;
        });
    }
}