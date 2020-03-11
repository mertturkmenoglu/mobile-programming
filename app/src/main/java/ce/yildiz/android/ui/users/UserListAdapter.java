package ce.yildiz.android.ui.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ce.yildiz.android.R;
import ce.yildiz.android.data.model.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private final Context mContext;
    private List<User> mDataset;

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTV;
        public TextView emailTV;
        public TextView passwordTV;
        public TextView imageURLTV;

        public UserViewHolder(View view) {
            super(view);
            usernameTV = view.findViewById(R.id.user_list_item_username);
            emailTV = view.findViewById(R.id.user_list_item_email);
            passwordTV = view.findViewById(R.id.user_list_item_password);
            imageURLTV = view.findViewById(R.id.user_list_item_image);
        }
    }

    public UserListAdapter(Context context, List<User> users) {
        this.mContext = context;
        this.mDataset = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = mDataset.get(position);
        holder.usernameTV.setText(user.getUsername());
        holder.emailTV.setText(user.getEmail());
        holder.passwordTV.setText(user.getPassword());
        holder.imageURLTV.setText(user.getImageURL());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
