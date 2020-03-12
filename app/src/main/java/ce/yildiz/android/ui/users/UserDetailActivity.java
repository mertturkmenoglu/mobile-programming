package ce.yildiz.android.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import ce.yildiz.android.R;

public class UserDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent incomingIntent = getIntent();

        if (incomingIntent == null)  {
            return;
        }

        String imageURL = incomingIntent.getStringExtra("image_url");
        String username = incomingIntent.getStringExtra("username");
        String email = incomingIntent.getStringExtra("email");
        String password = incomingIntent.getStringExtra("password");

        if (imageURL == null || username == null || email == null || password == null) {
            Toast.makeText(UserDetailActivity.this, "Invalid values", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView imageIV = findViewById(R.id.user_detail_image);
        TextView usernameTV = findViewById(R.id.user_detail_username);
        TextView emailTV = findViewById(R.id.user_detail_email);
        TextView passwordTV = findViewById(R.id.user_detail_password);
        Button updateUserBtn = findViewById(R.id.user_detail_update_user_btn);
        Button deleteUserBtn = findViewById(R.id.user_detail_delete_user_btn);

        Glide.with(this)
                .load(imageURL)
                .error(R.drawable.ic_adb_black_24dp)
                .placeholder(R.drawable.ic_person_holo_purple_24dp)
                .override(125, 125)
                .into(imageIV);

        usernameTV.setText(username);
        emailTV.setText(email);
        passwordTV.setText(password);

        // TODO: Update method
        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserDetailActivity.this, "Update User", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: Update method
        deleteUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserDetailActivity.this, "Delete Users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
