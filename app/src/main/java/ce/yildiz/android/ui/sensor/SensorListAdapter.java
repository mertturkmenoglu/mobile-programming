package ce.yildiz.android.ui.sensor;

import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ce.yildiz.android.R;

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensorViewHolder> {
    private List<Sensor> mSensors;

    static class SensorViewHolder extends RecyclerView.ViewHolder {
        TextView sensorNameTV;

        SensorViewHolder(View view) {
            super(view);
            sensorNameTV = view.findViewById(R.id.sensor_list_item_sensor_name);
        }
    }

    SensorListAdapter(List<Sensor> sensors) {
        this.mSensors = sensors;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.sensor_list_item, parent, false);

        return new SensorViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = mSensors.get(position);
        holder.sensorNameTV.setText(sensor.getName());
    }

    @Override
    public int getItemCount() {
        return mSensors.size();
    }
}
