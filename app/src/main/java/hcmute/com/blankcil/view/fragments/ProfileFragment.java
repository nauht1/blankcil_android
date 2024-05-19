package hcmute.com.blankcil.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.view.activities.MainActivity;
import hcmute.com.blankcil.view.adapter.PodcastAdapter;
import hcmute.com.blankcil.view.adapter.PodcastMiniAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private static final String ARG_USER_ID = "user_id";
    private int userId;
    private ImageView avatarImageView;
    private TextView userNameTextView, userEmailTextView, textTitle;
    private RecyclerView podcastRecyclerView;
    private APIService apiService;
    private List<PodcastModel> podcasts = new ArrayList<>();
    private PodcastMiniAdapter podcastAdapter;
    private FloatingActionButton fabCreatePodcast;

    public static ProfileFragment newInstance(int userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
        }
        fabCreatePodcast = ((MainActivity) getActivity()).findViewById(R.id.fabCreatePodcast);
        fabCreatePodcast.setVisibility(View.GONE);
        avatarImageView = view.findViewById(R.id.userAvatar);
        userNameTextView = view.findViewById(R.id.userName);
        userEmailTextView = view.findViewById(R.id.userEmail);
        podcastRecyclerView = view.findViewById(R.id.recyclerViewPodcasts);
        podcastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        podcastAdapter = new PodcastMiniAdapter();
        podcastRecyclerView.setAdapter(podcastAdapter);

        apiService = RetrofitClient.getInstance().getApi();

        loadUserProfile(userId);
    }

    private void loadUserProfile(int userId) {
        apiService.getProfile(userId).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel user = response.body().getBody();
                    Glide.with(getContext()).load(user.getAvatar_url()).into(avatarImageView);
                    userNameTextView.setText(user.getFullname());
                    userEmailTextView.setText(user.getEmail());

                    podcasts = user.getPodcasts();
                    podcastAdapter.setPodcasts(podcasts);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                Log.e(TAG, "Failed to load user profile", t);
            }
        });
    }
}