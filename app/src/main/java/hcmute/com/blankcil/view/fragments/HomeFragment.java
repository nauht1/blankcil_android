package hcmute.com.blankcil.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.PodcastResponse;
import hcmute.com.blankcil.utils.SharedPrefManager;
import hcmute.com.blankcil.view.adapter.PodcastAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private PodcastAdapter podcastAdapter;
    private List<PodcastModel> podcastList;
    private APIService apiService;
    int currentPage = 0;
    int totalPage = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2 = view.findViewById(R.id.viewPager);

        // Khởi tạo danh sách podcast
        podcastList = new ArrayList<>();
        // Khởi tạo adapter và gắn adapter vào ViewPager2
        podcastAdapter = new PodcastAdapter(getContext(), podcastList);
        viewPager2.setAdapter(podcastAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        String accessToken = SharedPrefManager.getInstance(getContext()).getAccessToken();
        fetchPodcasts(currentPage, accessToken);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Kiểm tra xem người dùng cuộn đến gần cuối danh sách chưa
                if (position == podcastAdapter.getItemCount() - 1) {
                    if (currentPage < totalPage) {
                        currentPage++;
                        fetchPodcasts(currentPage, accessToken); // Gọi API để lấy danh sách podcast mới
                    }
                }
            }
        });

        return view;
    }

    private void fetchPodcasts(int page, String accessToken) {
        apiService.getPodcasts("Bearer " + accessToken, page, true).enqueue(new Callback<PodcastResponse>() {
            @Override
            public void onResponse(Call<PodcastResponse> call, Response<PodcastResponse> response) {
                if (response.body() != null && response.body().getBody() != null) {
                    List<PodcastModel> newPodcasts = response.body().getBody().getContent();
                    totalPage = response.body().getBody().getTotalPage();
                    podcastList.addAll(newPodcasts);
                    podcastAdapter.notifyDataSetChanged();
                } else {
                    Log.d("fetchPodcasts", "Response body or body content is null");
                }
            }

            @Override
            public void onFailure(Call<PodcastResponse> call, Throwable t) {
                Log.d("fetchPodcasts", "Request failed: " + t.getMessage());
            }
        });
    }
}