package ce.yildiz.android.ui.sensor;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ce.yildiz.android.databinding.ActivitySensorBinding;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private ActivitySensorBinding binding;
    private SensorManager sensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mLightSensor;
    private float prevX;
    private float prevY;
    private float prevZ;
    public static final long DISCONNECT_TIMEOUT = 5000;
    private static final float EPSILON = 1.0F;

    private Handler disconnectHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return true;
        }
    });

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(SensorActivity.this, "5 second inactivity", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
            StringBuilder sb = new StringBuilder();

            for (Sensor s : sensorList) {
                sb.append(s.getName()).append("\n");
            }

            binding.sensorName.setText(sb.toString());

            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                mAccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            }

            if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
                mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            }

            if (mAccelerometerSensor == null || mLightSensor == null) {
                Toast.makeText(this, "You need accelerometer and light sensor. Sorry :/", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();

        if (sensorType == mAccelerometerSensor.getType()) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float dx = Math.abs(prevX - x);
            float dy = Math.abs(prevY - y);
            float dz = Math.abs(prevZ - z);

            if (dx > EPSILON || dy > EPSILON || dz > EPSILON) {
                resetDisconnectTimer();
            }

            prevX = x;
            prevY = y;
            prevZ = z;
        } else if (sensorType == mLightSensor.getType()) {
            float lightValue = event.values[0];
            int background;
            int foreground;

            if (lightValue < 255) {
                background = Math.round(lightValue);
                foreground = Math.round(255 - lightValue);
            } else {
                background = 255;
                foreground = 0;
            }

            binding.sensorName.setTextColor(Color.rgb(foreground, foreground, foreground));
            binding.sensorScrollView.setBackgroundColor(Color.rgb(background, background, background));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        resetDisconnectTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }
}
