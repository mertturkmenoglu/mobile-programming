package ce.yildiz.android.ui.users;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ce.yildiz.android.R;
import ce.yildiz.android.data.model.User;
import ce.yildiz.android.util.RecyclerViewClickListener;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private static final String TAG = UserListAdapter.class.getSimpleName();
    private final Context mContext;
    private List<User> mUsers;
    private RecyclerViewClickListener mListener;

    static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView usernameTV;
        TextView emailTV;
        TextView passwordTV;
        ImageView imageIV;
        RecyclerViewClickListener mListener;

        UserViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            mListener = listener;
            view.setOnClickListener(this);

            usernameTV = view.findViewById(R.id.user_list_item_username);
            emailTV = view.findViewById(R.id.user_list_item_email);
            passwordTV = view.findViewById(R.id.user_list_item_password);
            imageIV = view.findViewById(R.id.user_list_item_image);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

    UserListAdapter(Context context, List<User> users, RecyclerViewClickListener listener) {
        this.mContext = context;
        this.mUsers = users;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new UserViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.usernameTV.setText(user.getUsername());
        holder.emailTV.setText(user.getEmail());
        holder.passwordTV.setText(user.getPassword());

        Glide.with(mContext).load(user.getImageURL()).override(125, 125).into(holder.imageIV);
        Log.e(TAG, "onBindViewHolder: " + user.getImageURL());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
