package ce.yildiz.android.ui.users;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ce.yildiz.android.R;
import ce.yildiz.android.data.model.User;
import ce.yildiz.android.data.model.UserContract;
import ce.yildiz.android.util.DBHelper;
import ce.yildiz.android.util.RecyclerViewClickListener;

public class UserListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        List<User> users = getUsersFromDatabase();

        RecyclerView recyclerView = findViewById(R.id.user_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                TextView password = view.findViewById(R.id.user_list_item_password);
                CardView cardView = view.findViewById(R.id.user_list_item_card_view);

                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cardView.setCardBackgroundColor(getColor(android.R.color.holo_red_light));
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cardView.setCardBackgroundColor(getColor(android.R.color.background_light));
                }
            }
        };

        RecyclerView.Adapter adapter = new UserListAdapter(this, users, listener);
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
