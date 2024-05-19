package hcmute.com.blankcil.view.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.constants.Interface;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import hcmute.com.blankcil.view.fragments.CommentsBottomSheet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder> {
    private Context context;
    private List<PodcastModel> podcastList;
    private APIService apiService;
    private Interface.OnCommentCountChangeListener commentCountChangeListener;
    private Interface.OnAvatarClickListener avatarClickListener;
    public PodcastAdapter(Context context, List<PodcastModel> podcastList) {
        this.context = context;
        this.podcastList = podcastList;
    }

    public PodcastAdapter(Context context, List<PodcastModel> podcastList, Interface.OnAvatarClickListener listener) {
        this.context = context;
        this.podcastList = podcastList;
        this.avatarClickListener = listener;
    }

    public void setOnAvatarClickListener(Interface.OnAvatarClickListener listener) {
        this.avatarClickListener = listener;
    }

    public void setOnCommentCountChangeListener(Interface.OnCommentCountChangeListener listener) {
        this.commentCountChangeListener = listener;
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
        int totalComments = podcast.getNumberOfComments();
        holder.commentCount.setText(String.valueOf(totalComments));
        Glide.with(holder.imUserAvatar.getContext()).load(podcast.getUser_podcast().getAvatar_url()).into(holder.imUserAvatar);
        holder.textUserFullname.setText(String.valueOf(podcast.getUser_podcast().getFullname()));
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

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleVideo(holder.videoView);
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

        holder.imUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (avatarClickListener != null) {
                    avatarClickListener.onAvatarClick(podcast.getUser_podcast().getId());
                }
            }
        });

        // Cập nhật hình ảnh của nút like dựa trên giá trị hasLiked
        updateLikeButtonImage(holder.imLike, podcast.isHasLiked());

        // Thêm sự kiện click cho nút like
        holder.imLike.setOnClickListener(v -> {
            sendLikeRequest(podcast, holder.likeCount, holder.imLike);
        });

        holder.imComment.setOnClickListener(v -> {
            CommentsBottomSheet commentsBottomSheet = CommentsBottomSheet.newInstance(podcast.getId());
            Log.d("PodcastAdapter", "PodcastId: " + podcast.getId());
            commentsBottomSheet.setOnCommentCountChangeListener((podcastId, newCommentCount) -> {
                if (podcast.getId() == podcastId) {
                    podcast.setNumberOfComments(totalComments + newCommentCount);
                    holder.commentCount.setText(String.valueOf(totalComments + newCommentCount));
                }
            });
            commentsBottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), commentsBottomSheet.getTag());
        });
    }

    public void handleVideo(VideoView videoView) {
        if (videoView.isPlaying()) {
            videoView.pause();
        } else {
            videoView.start();
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
        private TextView textTitle, textContent, likeCount, commentCount, textUserFullname;
        private ImageButton imLike, imComment, imShare;
        private ImageView imUserAvatar;

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
            textUserFullname = itemView.findViewById(R.id.user_fullname);
            imUserAvatar = itemView.findViewById(R.id.user_avatar);
        }
    }

    private void updateLikeButtonImage(ImageButton imLike, boolean hasLiked) {
        if (hasLiked) {
            imLike.setImageResource(R.drawable.ic_liked);
        } else {
            imLike.setImageResource(R.drawable.ic_like);
        }
    }

    private void updateLikeCount(TextView likeCount, PodcastModel podcast) {
        likeCount.setText(String.valueOf(podcast.getNumberOfLikes()));
    }

    private void sendLikeRequest(PodcastModel podcast, TextView likeCount, ImageButton imLike) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        String accessToken = SharedPrefManager.getInstance(context.getApplicationContext()).getAccessToken();

        apiService.likePodcast("Bearer " + accessToken, podcast.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    if ("Liked".equals(message)) {
                        podcast.setHasLiked(true);
                        podcast.setNumberOfLikes(podcast.getNumberOfLikes() + 1);
                        updateLikeButtonImage(imLike, true);
                    } else if ("Unliked".equals(message)) {
                        podcast.setHasLiked(false);
                        podcast.setNumberOfLikes(podcast.getNumberOfLikes() - 1);
                        updateLikeButtonImage(imLike, false);
                    }
                    updateLikeCount(likeCount, podcast);
                } else {
                    Log.d("PodcastAdapter", "Response not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d("PodcastAdapter", "Request failed: " + t.getMessage());
            }
        });
    }
}
