package ce.yildiz.android.ui.login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.data.model.UserContract;
import ce.yildiz.android.databinding.ActivitySignUpBinding;
import ce.yildiz.android.util.AppConstants;
import ce.yildiz.android.util.DBHelper;

public class SignUpActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.signUpUsernameEt.getText().toString();
                String email = binding.signUpEmailEt.getText().toString();
                String password = binding.signUpPasswordEt.getText().toString();
                String imageURL = "https://github.com/" + binding.signUpGithubUsernameEt.getText().toString() + ".png";

                if (username.isEmpty() || email.isEmpty()
                        || password.length() < AppConstants.MIN_PASSWORD_LENGTH || imageURL.isEmpty()) {
                    Toast
                        .makeText(SignUpActivity.this, "Enter credentials", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    if (!userExist(username, email)) {
                        saveUser(username, email, password, imageURL);
                        Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    } else {
                        Toast.makeText(SignUpActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void saveUser(String username, String email, String password, String imageURL) {
        ContentValues cv = new ContentValues();
        cv.put(UserContract.UserEntry.COLUMN_USER_NAME, username);
        cv.put(UserContract.UserEntry.COLUMN_EMAIL, email);
        cv.put(UserContract.UserEntry.COLUMN_PASSWORD, password);
        cv.put(UserContract.UserEntry.COLUMN_IMAGE_URL, imageURL);
        mDatabase.insert(UserContract.UserEntry.TABLE_NAME, null, cv);
    }

    private boolean userExist(String username, String email) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String dbEmail = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL));
            String dbUserName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USER_NAME));

            if (dbEmail.equals(email) || dbUserName.equals(username)) {
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }
}
