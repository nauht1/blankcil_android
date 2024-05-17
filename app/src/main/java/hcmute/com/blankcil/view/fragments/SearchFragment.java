package hcmute.com.blankcil.view.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.SearchResponse;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.view.adapter.PodcastAdapter;
import hcmute.com.blankcil.view.adapter.PodcastMiniAdapter;
import hcmute.com.blankcil.view.adapter.UserMiniAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView usersRecyclerView;
    private RecyclerView podcastsRecyclerView;
    private UserMiniAdapter usersAdapter;
    private PodcastMiniAdapter podcastsAdapter;
    private APIService apiService;
    TextView txtUserNotFound, txtPodcastNotFound;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();

        searchView = view.findViewById(R.id.search_view);
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        podcastsRecyclerView = view.findViewById(R.id.podcasts_recycler_view);

        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersAdapter = new UserMiniAdapter();
        usersRecyclerView.setAdapter(usersAdapter);

        podcastsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        podcastsAdapter = new PodcastMiniAdapter();
        podcastsRecyclerView.setAdapter(podcastsAdapter);

        txtUserNotFound = view.findViewById(R.id.noUserFound);
        txtPodcastNotFound = view.findViewById(R.id.noPodcastFound);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void performSearch(String query) {
        apiService.search(query).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SearchResponse searchResponse = response.body();
                    List<UserModel> users = searchResponse.getBody().getUsers();
                    List<PodcastModel> podcasts = searchResponse.getBody().getPodcasts();
                    if (searchResponse.getStatus()) {
                            txtUserNotFound.setVisibility(View.GONE);
                            txtPodcastNotFound.setVisibility(View.GONE);
                            usersAdapter.setUsers(users);
                            podcastsAdapter.setPodcasts(podcasts);

                            if (users.isEmpty()) {
                                txtUserNotFound.setVisibility(View.VISIBLE);
                            }
                            if (podcasts.isEmpty()) {
                                txtPodcastNotFound.setVisibility(View.VISIBLE);
                            }
                    }
                    else {
                        Toast.makeText(getContext(), "Không tìm thấy!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Không tìm thấy!", Toast.LENGTH_SHORT).show();
                    clearResults();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Loi
    private void clearResults() {
        usersAdapter.setUsers(Collections.emptyList());
        podcastsAdapter.setPodcasts(Collections.emptyList());
        txtUserNotFound.setVisibility(View.VISIBLE);
        txtPodcastNotFound.setVisibility(View.VISIBLE);
    }
}