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
    }

    @Override
    public int getItemCount() {
        return podcasts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title, content;

        ViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.podcast_search_title);
            content = itemView.findViewById(R.id.podcast_search_content);
        }
    }
}
