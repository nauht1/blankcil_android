package hcmute.com.blankcil.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.constants.Interface;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.view.fragments.EditProfileFragment;

public class UserMiniAdapter extends RecyclerView.Adapter<UserMiniAdapter.ViewHolder> {
    private List<UserModel> users = new ArrayList<>();
    private Interface.OnAvatarClickListener avatarClickListener;
    public void setUsers(List<UserModel> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public UserMiniAdapter(Interface.OnAvatarClickListener listener) {
        this.avatarClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = users.get(position);
        holder.fullname.setText(user.getFullname());
        Glide.with(holder.avatar.getContext()).load(user.getAvatar_url()).into(holder.avatar);

        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avatarClickListener != null) {
                    Log.d("UserMiniAdapter", "onAvatarClick");
                    avatarClickListener.onAvatarClick(user.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView fullname;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            fullname = itemView.findViewById(R.id.fullname);
        }
    }
}
