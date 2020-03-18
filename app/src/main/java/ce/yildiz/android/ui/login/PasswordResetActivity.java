package ce.yildiz.android.ui.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.data.model.UserContract;
import ce.yildiz.android.databinding.ActivityPasswordResetBinding;
import ce.yildiz.android.util.DBHelper;

public class PasswordResetActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private ActivityPasswordResetBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        Toast.makeText(this, "Reset password for " + username, Toast.LENGTH_SHORT).show();

        binding.resetPasswordResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = binding.resetPasswordNewPasswordEt.getText().toString();
                String confirmPassword = binding.resetPasswordConfirmPasswordEt.getText().toString();

                if (newPassword.length() < 6) {
                    Toast.makeText(PasswordResetActivity.this, "Minimum password length is 6", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(PasswordResetActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(username, newPassword);

                    Toast.makeText(PasswordResetActivity.this, "Reset", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
    }

    private void resetPassword(String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put(UserContract.UserEntry.COLUMN_USER_NAME, username);
        cv.put(UserContract.UserEntry.COLUMN_PASSWORD, password);

        String selection = UserContract.UserEntry.COLUMN_USER_NAME + " LIKE ?";
        String[] selectionArgs = { username };
        mDatabase.update(UserContract.UserEntry.TABLE_NAME, cv, selection, selectionArgs);
    }
}
