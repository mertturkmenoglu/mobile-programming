package ce.yildiz.android.ui.users;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    private static final int USER_DETAIL_REQUEST_CODE = 2;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final List<User> users = getUsersFromDatabase();

        mRecyclerView = findViewById(R.id.user_list_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent userDetailIntent = new Intent(UserListActivity.this, UserDetailActivity.class);
                User user = users.get(position);
                userDetailIntent.putExtra("username", user.getUsername());
                userDetailIntent.putExtra("email", user.getEmail());
                userDetailIntent.putExtra("password", user.getPassword());
                userDetailIntent.putExtra("image_url", user.getImageURL());
                startActivityForResult(userDetailIntent, USER_DETAIL_REQUEST_CODE);
            }
        };

        UserListAdapter adapter = new UserListAdapter(this, users, listener);
        mRecyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.user_list_swipe_refresh);
        swipeRefresh.setColorSchemeColors(getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                users.clear();
                users.addAll(getUsersFromDatabase());

                RecyclerView.Adapter a = mRecyclerView.getAdapter();
                if (a != null) {
                    a.notifyDataSetChanged();
                }

                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == USER_DETAIL_REQUEST_CODE) {
                RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
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
