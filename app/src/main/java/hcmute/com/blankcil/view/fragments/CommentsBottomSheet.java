package hcmute.com.blankcil.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.constants.Interface;
import hcmute.com.blankcil.model.CommentModel;
import hcmute.com.blankcil.model.CommentResponse;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import hcmute.com.blankcil.view.adapter.CommentAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsBottomSheet extends BottomSheetDialogFragment {
    private final String TAG = "CommentBottomSheet";
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private APIService apiService;
    private int podcastId;
    private List<CommentModel> commentList;
    private EditText commentInput;
    private ImageButton btnSendComment;
    private ImageView avatarImage;
    private Interface.OnCommentCountChangeListener commentCountChangeListener;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String accessToken;

    public static CommentsBottomSheet newInstance(int podcastId) {
        CommentsBottomSheet fragment = new CommentsBottomSheet();
        Bundle args = new Bundle();
        args.putInt("podcastId", podcastId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnCommentCountChangeListener(Interface.OnCommentCountChangeListener listener) {
        this.commentCountChangeListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        accessToken = SharedPrefManager.getInstance(getContext()).getAccessToken();
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        commentInput = view.findViewById(R.id.commentInput);
        btnSendComment = view.findViewById(R.id.btnSendComment);
        avatarImage = view.findViewById(R.id.userAvatar);

        String avatarUrl = SharedPrefManager.getInstance(getContext()).getUserModel().getAvatar_url();
        Glide.with(getContext()).load(avatarUrl).into(avatarImage);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), commentList);
        recyclerView.setAdapter(commentAdapter);

        apiService = RetrofitClient.getInstance().getApi();

        if (getArguments() != null) {
            podcastId = getArguments().getInt("podcastId");
            loadComments(currentPage);
        }

        btnSendComment.setOnClickListener(v -> {
            String content = commentInput.getText().toString().trim();
            if (!content.isEmpty()) {
                sendComment(content);
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == commentList.size() - 1) {
                    if (!isLoading && !isLastPage) {
                        currentPage++;
                        loadComments(currentPage);
                    }
                }
            }
        });
    }

    private void loadComments(int page) {
        isLoading = true;
        apiService.getCommentsForPodcast("Bearer " + accessToken, podcastId, page).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CommentModel> newComments = response.body().getBody();
                    if (newComments != null && !newComments.isEmpty()) {
                        commentList.addAll(newComments);
                        commentAdapter.setComment(commentList);
                        commentAdapter.notifyItemRangeInserted(commentList.size() - newComments.size(), newComments.size());
                    } else {
                        isLastPage = true;
                    }
                } else {
                    isLastPage = true;
                }
                isLoading = false;
//                if (response.isSuccessful() && response.body() != null) {
//                    commentList = response.body().getBody();
//                    commentAdapter.setComment(commentList);
//                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.d(TAG, "FAILED" + t.getMessage());
                isLoading = false;
            }
        });
    }
    private void sendComment(String content) {
        apiService.commentOnPodcast("Bearer " + accessToken, content, podcastId).enqueue(new Callback<ResponseModel<CommentModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<CommentModel>> call, Response<ResponseModel<CommentModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CommentModel newComment = response.body().getBody();
                    commentList.add(0, newComment);
                    commentAdapter.notifyItemInserted(0);
                    commentInput.setText("");
                    recyclerView.smoothScrollToPosition(0);

                    if (commentCountChangeListener != null) {
                        commentCountChangeListener.onCommentCountChanged(podcastId, 1);
                    }

                } else {
                    Toast.makeText(getContext(), "Failed to post comment", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<CommentModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
