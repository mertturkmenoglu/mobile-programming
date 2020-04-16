package ce.yildiz.android.ui.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.databinding.ActivityNavigationBinding;
import ce.yildiz.android.ui.email.EmailComposeActivity;
import ce.yildiz.android.ui.notes.notelist.NoteListActivity;
import ce.yildiz.android.ui.sensor.SensorActivity;
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

        try {
            intentEmail = comingIntent.getStringExtra("email");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (intentEmail == null) {
            Toast.makeText(NavigationActivity.this, "Invalid intent", Toast.LENGTH_SHORT).show();
            finish();
        }

        final String email = intentEmail;

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
                Toast.makeText(NavigationActivity.this, "Settings", Toast.LENGTH_SHORT).show();
            }
        });

        binding.navigationNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavigationActivity.this, "Notes", Toast.LENGTH_SHORT).show();
                Intent noteListIntent = new Intent(NavigationActivity.this, NoteListActivity.class);
                startActivity(noteListIntent);
            }
        });

        binding.navigationSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavigationActivity.this, "Sensors", Toast.LENGTH_SHORT).show();
                Intent sensorListIntent = new Intent(NavigationActivity.this, SensorActivity.class);
                startActivity(sensorListIntent);
            }
        });

    }
}
