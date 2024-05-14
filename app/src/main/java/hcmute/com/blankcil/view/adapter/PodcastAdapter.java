package hcmute.com.blankcil.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
//        holder.textVideoDescription.setText(video1Model.getDesc());
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
        // Set thông tin video cho VideoView
//        holder.setVideo(podcast.getAudio_url());
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                holder.videoView.setMediaController(new MediaController(context));
                holder.videoView.requestFocus();
                mp.start();
            }
        });
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

        public PodcastViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
        }

//        public void setVideo(String videoUrl) {
//            // Tạo một Uri từ URL video
//            Uri videoUri = Uri.parse(videoUrl);
//
//            // Sử dụng Runnable để đặt đường dẫn của video cho VideoView trong luồng nền
//            videoView.post(new Runnable() {
//                @Override
//                public void run() {
//                    // Đặt đường dẫn của video cho VideoView
//                    videoView.setVideoURI(videoUri);
//                    // Bắt đầu phát video
//                    videoView.start();
//                }
//            });
//        }
    }
}
