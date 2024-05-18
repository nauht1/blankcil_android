package hcmute.com.blankcil.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.utils.SharedPrefManager;


public class AccountFragment extends Fragment implements View.OnClickListener {
    ImageView imgBanner, imgAvatar;
    TextView txtNumberOfPodcast, txtUsername, txtEmail;
    Button btnEdit;
    RecyclerView recyclerView;
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
    }

    @Override
    public void onClick(View v) {

    }
}