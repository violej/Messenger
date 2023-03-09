package com.example.messenger;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();

    private OnUserClickListener onUserClickListener;

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull UserViewHolder holder, int position) {

        User user = users.get(position);
        String userInfo = String.format("%s,%s,%S",user.getName(),user.getLastName(),user.getAge());
        holder.textViewUserInfo.setText(userInfo);
        int resBgId;

        if (user.isOnline()){
            resBgId = R.drawable.circle_green;
        }
        else {

            resBgId = R.drawable.circle_red;

        }
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(),resBgId);
        holder.onlineStatus.setBackground(background);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onUserClickListener != null){
                    onUserClickListener.onUserClick(user);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserClickListener{

        void onUserClick(User user);

    }

    static class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewUserInfo;
        private View onlineStatus;


        public UserViewHolder(@NotNull View itemView) {
            super(itemView);
            textViewUserInfo = itemView.findViewById(R.id.textViewUserInfo);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
        }
    }
}
