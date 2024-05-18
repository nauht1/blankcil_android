package hcmute.com.blankcil.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.CommentModel;
import hcmute.com.blankcil.model.CommentResponse;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<CommentModel> commentList;
    private APIService apiService;

    public CommentAdapter(Context context, List<CommentModel> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    public void setComment(List<CommentModel> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentModel comment = commentList.get(position);

        Glide.with(context).load(comment.getUser_comment().getAvatar_url()).into(holder.userAvatar);
        holder.userFullName.setText(comment.getUser_comment().getFullname());
        holder.commentContent.setText(comment.getContent());
        holder.likeCount.setText(String.valueOf(comment.getTotalLikes()));
        holder.replyCount.setText(String.valueOf(comment.getTotalReplies()));

        updateLikeButtonImage(holder.btnLikeComment, comment.isHasLiked());

        holder.btnLikeComment.setOnClickListener(v -> {
            sendLikeRequest(comment, holder.likeCount, holder.btnLikeComment);
        });

        holder.btnReplyComment.setOnClickListener(v -> {
            // Handle reply comment action
        });

        // Handle replies
//        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
//            holder.repliesContainer.setVisibility(View.VISIBLE);
//            holder.repliesContainer.removeAllViews();
//            for (CommentModel reply : comment.getReplies()) {
//                View replyView = LayoutInflater.from(context).inflate(R.layout.item_comment, holder.repliesContainer, false);
//                CommentViewHolder replyHolder = new CommentViewHolder(replyView);
//                bindComment(replyHolder, reply);
//                holder.repliesContainer.addView(replyView);
//            }
//        } else {
//            holder.repliesContainer.setVisibility(View.GONE);
//        }
    }

    private void bindComment(CommentViewHolder holder, CommentModel comment) {
        Glide.with(context).load(comment.getUser_comment().getAvatar_url()).into(holder.userAvatar);
        holder.userFullName.setText(comment.getUser_comment().getFullname());
        holder.commentContent.setText(comment.getContent());
    }

    private void updateLikeButtonImage(ImageButton imLike, boolean hasLiked) {
        if (hasLiked) {
            imLike.setImageResource(R.drawable.ic_liked);
        } else {
            imLike.setImageResource(R.drawable.ic_like);
        }
    }

    private void updateLikeCount(TextView likeCount, CommentModel comment) {
        likeCount.setText(String.valueOf(comment.getTotalLikes()));
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView userAvatar;
        private TextView userFullName, commentContent, likeCount, replyCount;
        private ImageButton btnLikeComment, btnReplyComment;
        private LinearLayout repliesContainer;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userFullName = itemView.findViewById(R.id.userFullName);
            commentContent = itemView.findViewById(R.id.commentContent);
            btnLikeComment = itemView.findViewById(R.id.btnLikeComment);
            btnReplyComment = itemView.findViewById(R.id.btnReplyComment);
            repliesContainer = itemView.findViewById(R.id.repliesContainer);
            likeCount = itemView.findViewById(R.id.likeCount);
            replyCount = itemView.findViewById(R.id.replyCount);
        }
    }

    private void sendLikeRequest(CommentModel comment, TextView likeCount, ImageButton btnLikeComment) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        String accessToken = SharedPrefManager.getInstance(context.getApplicationContext()).getAccessToken();

        apiService.likeComment("Bearer " + accessToken, comment.getId()).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    if ("Liked".equals(message)) {
                        comment.setHasLiked(true);
                        comment.setTotalLikes(comment.getTotalLikes() + 1);
                        updateLikeButtonImage(btnLikeComment, true);
                    } else if ("Unliked".equals(message)) {
                        comment.setHasLiked(false);
                        comment.setTotalLikes(comment.getTotalLikes() - 1);
                        updateLikeButtonImage(btnLikeComment, false);
                    }
                    updateLikeCount(likeCount, comment);
                    notifyItemChanged(commentList.indexOf(comment));
                } else {
                    Log.d("CommentAdapter", "Response not successful or body is null");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d("CommentAdapter", "Request failed: " + t.getMessage());
            }
        });
    }
}
