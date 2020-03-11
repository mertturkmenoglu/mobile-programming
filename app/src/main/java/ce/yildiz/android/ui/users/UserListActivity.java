package ce.yildiz.android.ui.users;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ce.yildiz.android.R;
import ce.yildiz.android.data.model.User;
import ce.yildiz.android.data.model.UserContract;
import ce.yildiz.android.util.DBHelper;

public class UserListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        List<User> users = getUsersFromDatabase();

        RecyclerView recyclerView = findViewById(R.id.user_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerView.Adapter adapter = new UserListAdapter(this, users);
        recyclerView.setAdapter(adapter);
    }

    private List<User> getUsersFromDatabase() {
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

        ArrayList<User> users = new ArrayList<>();

        while (cursor.moveToNext()) {
            String dbUsername = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_USER_NAME));
            String dbEmail = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL));
            String dbPassword = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PASSWORD));
            String dbImageURL = cursor.getString(cursor.getColumnIndex(UserContract.UserEntry.COLUMN_IMAGE_URL));

            User user = new User(dbUsername, dbEmail, dbPassword, dbImageURL);
            users.add(user);
        }

        cursor.close();
        return users;
    }
}
