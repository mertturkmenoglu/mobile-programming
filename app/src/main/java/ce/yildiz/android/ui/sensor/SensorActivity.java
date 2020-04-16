package ce.yildiz.android.ui.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ce.yildiz.android.databinding.ActivitySensorBinding;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = SensorActivity.class.getSimpleName();
    private ActivitySensorBinding binding;
    private SensorManager sensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mLightSensor;
    private float prevX;
    private float prevY;
    private float prevZ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySensorBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            binding.sensorListRecyclerView.setLayoutManager(layoutManager);

            SensorListAdapter adapter = new SensorListAdapter(sensorList);
            binding.sensorListRecyclerView.setAdapter(adapter);

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
            Log.e(TAG, "Accelerometer changed: " + dx + ", " + dy + ", " + dz + "\n\n");
            prevX = x;
            prevY = y;
            prevZ = z;
        } else if (sensorType == mLightSensor.getType()) {
            float lightValue = event.values[0];
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }
}
