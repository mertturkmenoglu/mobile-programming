package ce.yildiz.android.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import ce.yildiz.android.R;
import ce.yildiz.android.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        Intent comingIntent = getIntent();
        String intentUsername = null;

        try {
            intentUsername = comingIntent.getStringExtra("username");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intentUsername == null) {
            Toast.makeText(this, "Invalid intent", Toast.LENGTH_SHORT).show();
            finish();
        }

        final String username = intentUsername;
        binding.settingsUsernameTextView.setText(username);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender_array,
                android.R.layout.simple_spinner_item
        );

        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.settingsGenderSpinner.setAdapter(genderAdapter);

        binding.settingsAgePicker.setMinValue(18);
        binding.settingsAgePicker.setMaxValue(120);
        binding.settingsAgePicker.setWrapSelectorWheel(true);

        final SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(username, Context.MODE_PRIVATE);

        String gender = sharedPreferences.getString("gender", "Male");
        int height = sharedPreferences.getInt("height", 0);
        int weight = sharedPreferences.getInt("weight", 0);
        int age = sharedPreferences.getInt("age", 20);
        String mode = sharedPreferences.getString("mode", "dark");

        List<String> genderChoices = Arrays.asList(
                getResources().getStringArray(R.array.gender_array)
        );

        binding.settingsGenderSpinner.setSelection(genderChoices.indexOf(gender));
        binding.settingsHeightEt.setText(String.valueOf(height));
        binding.settingsWeightEt.setText(String.valueOf(weight));
        binding.settingsAgePicker.setValue(age);

        if (mode.equals("dark")) {
            binding.settingDarkRadioBtn.toggle();
        } else {
            binding.settingsLightRadioBtn.toggle();
        }

        binding.settingsSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.putString("gender", (String) binding.settingsGenderSpinner.getSelectedItem());
                editor.putInt("height", Integer.parseInt(binding.settingsHeightEt.getText().toString()));
                editor.putInt("weight", Integer.parseInt(binding.settingsWeightEt.getText().toString()));
                editor.putInt("age", binding.settingsAgePicker.getValue());

                if (binding.settingDarkRadioBtn.isChecked()) {
                    editor.putString("mode", "dark");
                } else {
                    editor.putString("mode", "light");
                }

                editor.apply();
            }
        });
    }
}
