package hcmute.com.blankcil.constants;

import hcmute.com.blankcil.model.PodcastResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("podcast/view/page")
    Call<PodcastResponse> getPodcasts(
            @Query("pageNumber") int pageNumber
    );
}
