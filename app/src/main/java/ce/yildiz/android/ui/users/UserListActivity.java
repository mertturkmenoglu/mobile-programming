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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

        final List<User> users = getUsersFromDatabase();

        final RecyclerView recyclerView = findViewById(R.id.user_list_recycler_view);
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

        UserListAdapter adapter = new UserListAdapter(this, users, listener);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.user_list_swipe_refresh);
        swipeRefresh.setColorSchemeColors(getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                users.clear();
                users.addAll(getUsersFromDatabase());

                RecyclerView.Adapter a = recyclerView.getAdapter();
                if (a != null) {
                    a.notifyDataSetChanged();
                }

                swipeRefresh.setRefreshing(false);
            }
        });
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
