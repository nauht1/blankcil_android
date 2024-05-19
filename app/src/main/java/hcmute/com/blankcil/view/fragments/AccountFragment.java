package hcmute.com.blankcil.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.LogoutResponse;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import hcmute.com.blankcil.view.activities.EmailConfirmationActivity;
import hcmute.com.blankcil.view.activities.LoginActivity;
import hcmute.com.blankcil.view.adapter.PodcastMiniAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment implements PodcastMiniAdapter.OnItemClickListener {
    ImageView imgBanner, imgAvatar;
    TextView txtNumberOfPodcast, txtUsername, txtEmail;
    Button btnEdit, btnLogout;
    RecyclerView recyclerView;
    private PodcastMiniAdapter podcastMiniAdapter;
    private APIService apiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        imgBanner = view.findViewById(R.id.imgBanner);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtNumberOfPodcast = view.findViewById(R.id.txtNumberOfPodcasts);
        recyclerView = view.findViewById(R.id.recyclerviewPodcast);
        txtEmail = view.findViewById(R.id.txtEmailProfile);
        btnEdit = view.findViewById(R.id.btnEditProfile);
        btnEdit.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        podcastMiniAdapter = new PodcastMiniAdapter();
        recyclerView.setAdapter(podcastMiniAdapter);
        podcastMiniAdapter.setOnItemClickListener(this::onItemClick);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setVisibility(View.VISIBLE);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        UserModel userModel = SharedPrefManager.getInstance(getContext()).getUserModel();
        if(userModel!=null){
            txtUsername.setText(userModel.getFullname());
            txtEmail.setText(userModel.getEmail());
            List<PodcastModel> podcastModelList = userModel.getPodcasts();
            int numberofPodcasts = podcastModelList.size();
            txtNumberOfPodcast.setText(String.valueOf(numberofPodcasts));
            String avatarURL = userModel.getAvatar_url();
            String coverURL = userModel.getCover_url();

            if(avatarURL!=null && !avatarURL.isEmpty()){
                Glide.with(this)
                        .load(avatarURL)
                        .into(imgAvatar);
            } else {
                //nếu avatar chưa có ảnh thì để ảnh mặc định theo link bên dưới
                Glide.with(this)
                        .load("https://cdn3.vectorstock.com/i/1000x1000/80/92/avatar-user-basic-blue-dotted-line-icon-vector-25358092.jpg")
                        .into(imgAvatar);
            }

            if(coverURL!=null && !coverURL.isEmpty()){
                Glide.with(this)
                        .load(coverURL)
                        .into(imgBanner);
            } else {
                //nếu banner chưa có ảnh thì để ảnh mặc định theo link bên dưới
                Glide.with(this)
                        .load("https://as1.ftcdn.net/v2/jpg/04/62/76/74/1000_F_462767413_4gRTpH3q2U9DUXdK3UubkOeFXsnUlENd.jpg")
                        .into(imgBanner);
            }

            List<PodcastModel> podcastList = userModel.getPodcasts();
            Log.d("AccountFragment", "PODCAST: " + podcastList);
            podcastMiniAdapter.setPodcasts(podcastList);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEditButton();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new EditProfileFragment()); // 'container' là ID của ViewGroup chứa Fragment trong Activity
                transaction.addToBackStack(null); // Lưu lịch sử fragment
                transaction.commit(); // Thực hiện transaction
            }
        });
        return  view;
    }

    private void hideEditButton() {
        btnEdit.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
    }

    private void logoutUser() {
        String token = SharedPrefManager.getInstance(getContext()).getAccessToken();
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        apiService.logout("Bearer " + token).enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(getContext()).clear();
                    Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Logout failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("AccountFragment", "MSG" + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(PodcastModel podcast) {
        btnEdit.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        VideoFragment videoFragment = VideoFragment.newInstance(podcast);
        transaction.replace(R.id.container, videoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}