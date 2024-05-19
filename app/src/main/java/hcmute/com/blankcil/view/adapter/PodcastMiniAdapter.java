package hcmute.com.blankcil.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.model.PodcastModel;

public class PodcastMiniAdapter extends RecyclerView.Adapter<PodcastMiniAdapter.ViewHolder> {
    private List<PodcastModel> podcasts = new ArrayList<>();

    public void setPodcasts(List<PodcastModel> podcasts) {
        this.podcasts = podcasts;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(PodcastModel podcast);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_podcast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PodcastModel podcast = podcasts.get(position);
        Glide.with(holder.thumbnail.getContext()).load(podcast.getThumbnail_url()).into(holder.thumbnail);
        holder.title.setText(String.valueOf(podcast.getTitle()));
        holder.content.setText(String.valueOf(podcast.getContent()));
//        Glide.with(holder.userAvatar.getContext()).load(podcast.getUser_podcast().getAvatar_url()).into(holder.userAvatar);
//        holder.userFullname.setText(String.valueOf(podcast.getUser_podcast().getFullname()));
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(podcast);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (podcasts != null) {
            return podcasts.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title, content, userFullname;
        ImageView userAvatar;
        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.podcast_search_title);
            content = itemView.findViewById(R.id.podcast_search_content);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userFullname =itemView.findViewById(R.id.user_fullname);
        }
    }
}
