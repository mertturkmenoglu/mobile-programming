package ce.yildiz.android.ui.login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ce.yildiz.android.R;
import ce.yildiz.android.data.model.User;
import ce.yildiz.android.util.DBHelper;
import ce.yildiz.android.data.model.UserContract;

public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final EditText loginET = findViewById(R.id.forgot_password_login_et);
        Button resetBtn = findViewById(R.id.forgot_password_reset_password_btn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginText = loginET.getText().toString();

                if (loginText.isEmpty()) {
                    Toast
                        .makeText(ForgotPasswordActivity.this, "Invalid e-mail/username", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    User user = findUser(loginText);

                    if (user != null) {
                        Intent passwordResetIntent = new Intent(ForgotPasswordActivity.this, PasswordResetActivity.class);
                        passwordResetIntent.putExtra("username", user.getUsername());
                        startActivity(passwordResetIntent);
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private User findUser(String loginText) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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
            String dbUserName = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USER_NAME));
            String dbEmail = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL));
            String dbPassword = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD));
            String dbImageURL = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_IMAGE_URL));

            if (dbUserName.equals(loginText) || dbEmail.equals(loginText)) {
                cursor.close();
                return new User(dbUserName, dbEmail, dbPassword, dbImageURL);
            }
        }

        cursor.close();
        return null;
    }
}
