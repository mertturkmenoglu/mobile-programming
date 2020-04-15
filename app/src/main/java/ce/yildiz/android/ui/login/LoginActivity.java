package ce.yildiz.android.ui.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.data.model.User;
import ce.yildiz.android.data.model.UserContract;
import ce.yildiz.android.databinding.ActivityLoginBinding;
import ce.yildiz.android.ui.email.EmailComposeActivity;
import ce.yildiz.android.ui.user.userlist.UserListActivity;
import ce.yildiz.android.util.AppConstants;
import ce.yildiz.android.util.DBHelper;

public class LoginActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private ActivityLoginBinding binding;
    private static int invalidLoginAttemptCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        invalidLoginAttemptCount = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        binding.loginForgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
            }
        });

        binding.loginSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = binding.loginLoginEt.getText().toString().trim();
                String password = binding.loginPasswordEt.getText().toString().trim();

                if (loginText.isEmpty() || password.length() < AppConstants.MIN_PASSWORD_LENGTH) {
                    invalidLoginAttemptCount++;

                    if (invalidLoginAttemptCount >= 3) {
                        Toast
                            .makeText(LoginActivity.this, "Too many wrong login attempts. Closing app.", Toast.LENGTH_SHORT)
                            .show();

                        finish();
                    } else {
                        Toast
                            .makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT)
                            .show();
                    }
                } else {
                    User user = login(loginText, password);

                    if (user != null) {
                        Toast
                            .makeText(LoginActivity.this, "You made it!!! Login!!!", Toast.LENGTH_SHORT)
                            .show();
                        Intent emailComposeIntent = new Intent(LoginActivity.this, EmailComposeActivity.class);
                        emailComposeIntent.putExtra("email", user.getEmail());
                        startActivity(emailComposeIntent);
                    } else {
                        Toast
                            .makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT)
                            .show();
                    }
                }
            }
        });

        binding.loginListUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userListIntent = new Intent(LoginActivity.this, UserListActivity.class);
                startActivity(userListIntent);
            }
        });
    }

    private User login(String loginText, String password) {
        Cursor cursor = mDatabase.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            try {
                String dbUserName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USER_NAME));
                String dbEmail = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL));
                String dbPassword = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD));
                String dbImageURL = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_IMAGE_URL));

                if ((dbUserName.equals(loginText) || dbEmail.equals(loginText)) && dbPassword.equals(password)) {
                    cursor.close();
                    return new User(dbUserName, dbEmail, dbPassword, dbImageURL);
                }
            } catch (Exception ignored) {

            }
        }

        cursor.close();
        return null;
    }
}
