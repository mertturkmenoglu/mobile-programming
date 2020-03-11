package ce.yildiz.android.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ce.yildiz.android.R;
import ce.yildiz.android.data.model.User;
import ce.yildiz.android.ui.email.EmailComposeActivity;
import ce.yildiz.android.util.AppConstants;
import ce.yildiz.android.util.DBHelper;
import ce.yildiz.android.data.model.UserContract;

public class LoginActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        final EditText loginET = findViewById(R.id.login_login_et);
        final EditText passwordET = findViewById(R.id.login_password_et);
        TextView forgotPasswordTV = findViewById(R.id.login_forgot_password_text);
        TextView signUpTV = findViewById(R.id.login_sign_up_text);
        Button loginBtn = findViewById(R.id.login_button);

        forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
            }
        });

        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = loginET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if (loginText.isEmpty() || password.length() < AppConstants.MIN_PASSWORD_LENGTH) {
                    Toast
                        .makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    User user = login(loginText, password);

                    if (user != null) {
                        Toast
                            .makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT)
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
