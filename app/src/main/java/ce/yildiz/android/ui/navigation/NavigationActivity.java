package ce.yildiz.android.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.databinding.ActivityNavigationBinding;
import ce.yildiz.android.ui.alarm.AlarmActivity;
import ce.yildiz.android.ui.email.EmailComposeActivity;
import ce.yildiz.android.ui.notes.notelist.NoteListActivity;
import ce.yildiz.android.ui.sensor.SensorActivity;
import ce.yildiz.android.ui.settings.SettingsActivity;
import ce.yildiz.android.ui.user.userlist.UserListActivity;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNavigationBinding binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        Intent comingIntent = getIntent();
        String intentEmail = null;
        String intentUsername = null;

        try {
            intentEmail = comingIntent.getStringExtra("email");
            intentUsername = comingIntent.getStringExtra("username");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intentEmail == null || intentUsername == null) {
            Toast.makeText(NavigationActivity.this, "Invalid intent", Toast.LENGTH_SHORT).show();
            finish();
        }

        final String email = intentEmail;
        final String username = intentUsername;

        binding.navigationSendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailComposeIntent = new Intent(NavigationActivity.this, EmailComposeActivity.class);
                emailComposeIntent.putExtra("email", email);
                startActivity(emailComposeIntent);
            }
        });

        binding.navigationListUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userListIntent = new Intent(NavigationActivity.this, UserListActivity.class);
                startActivity(userListIntent);
            }
        });

        binding.navigationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(NavigationActivity.this, SettingsActivity.class);
                settingsIntent.putExtra("username", username);
                startActivity(settingsIntent);
            }
        });

        binding.navigationNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent noteListIntent = new Intent(NavigationActivity.this, NoteListActivity.class);
                startActivity(noteListIntent);
            }
        });

        binding.navigationSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sensorListIntent = new Intent(NavigationActivity.this, SensorActivity.class);
                startActivity(sensorListIntent);
            }
        });

        binding.navigationAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarmIntent = new Intent(NavigationActivity.this, AlarmActivity.class);
                startActivity(alarmIntent);
            }
        });
    }
}
