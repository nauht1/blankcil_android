package hcmute.com.blankcil.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.model.PodcastModel;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {
    private Context context;
    private List<PodcastModel> podcastList;
    public PodcastAdapter(Context context, List<PodcastModel> podcastList) {
        this.context = context;
        this.podcastList = podcastList;
    }

    @NonNull
    @Override
    public PodcastAdapter.PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new PodcastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PodcastAdapter.PodcastViewHolder holder, int position) {
        PodcastModel podcast = podcastList.get(position);
        holder.textTitle.setText(podcast.getTitle());
        holder.textContent.setText(podcast.getContent());
        holder.likeCount.setText(String.valueOf(podcast.getNumberOfLikes()));
        holder.commentCount.setText(String.valueOf(podcast.getNumberOfComments()));
        holder.videoView.setVideoURI(Uri.parse(podcast.getAudio_url()));
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = holder.videoView.getWidth() / (float) holder.videoView.getHeight();
                float scale = videoRatio / screenRatio;
                if (scale >= 1f) {
                    holder.videoView.setScaleX(scale);
                }
                else {
                    holder.videoView.setScaleY(1f/scale);
                }
            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                holder.videoView.setMediaController(new MediaController(context));
                holder.videoView.requestFocus();
                mp.start();
            }
        });

        // Kiểm tra giá trị hasLiked và cập nhật hình ảnh của nút like
        if (podcast.isHasLiked()) {
            holder.imLike.setImageResource(R.drawable.ic_liked);
        } else {
            holder.imLike.setImageResource(R.drawable.ic_like);
        }
    }

    @Override
    public int getItemCount() {
        if (podcastList != null) {
            return podcastList.size();
        }
        return 0;
    }

    public static class PodcastViewHolder extends RecyclerView.ViewHolder {

        private VideoView videoView;
        private TextView textTitle, textContent;
        private TextView likeCount, commentCount;
        private ImageButton imLike, imComment, imShare;

        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            textTitle = itemView.findViewById(R.id.videoTitle);
            textContent = itemView.findViewById(R.id.videoContent);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            imLike = itemView.findViewById(R.id.btnLike);
            imComment = itemView.findViewById(R.id.btnComment);
            imShare = itemView.findViewById(R.id.btnShareLink);
        }
    }

    public void addPodcasts(List<PodcastModel> newPodcasts) {
        int startPosition = podcastList.size();
        podcastList.addAll(newPodcasts);
        notifyItemRangeInserted(startPosition, newPodcasts.size());
    }
}
