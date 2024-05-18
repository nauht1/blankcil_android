package hcmute.com.blankcil.view.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<CommentModel> commentList;

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

        Glide.with(context).load(comment.getUserComment().getAvatar_url()).into(holder.userAvatar);
        holder.userFullName.setText(comment.getUserComment().getFullname());
        holder.commentContent.setText(comment.getContent());
        holder.likeCount.setText(String.valueOf(comment.getTotalLikes()));
        holder.replyCount.setText(String.valueOf(comment.getTotalReplies()));

        holder.btnLikeComment.setOnClickListener(v -> {
            // Handle like comment action
        });

        holder.btnReplyComment.setOnClickListener(v -> {
            // Handle reply comment action
        });

        // Handle replies
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            holder.repliesContainer.setVisibility(View.VISIBLE);
            holder.repliesContainer.removeAllViews();
            for (CommentModel reply : comment.getReplies()) {
                View replyView = LayoutInflater.from(context).inflate(R.layout.item_comment, holder.repliesContainer, false);
                CommentViewHolder replyHolder = new CommentViewHolder(replyView);
                bindComment(replyHolder, reply);
                holder.repliesContainer.addView(replyView);
            }
        } else {
            holder.repliesContainer.setVisibility(View.GONE);
        }
    }

    private void bindComment(CommentViewHolder holder, CommentModel comment) {
        Glide.with(context).load(comment.getUserComment().getAvatar_url()).into(holder.userAvatar);
        holder.userFullName.setText(comment.getUserComment().getFullname());
        holder.commentContent.setText(comment.getContent());
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
}
