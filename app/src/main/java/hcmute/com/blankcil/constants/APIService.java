package hcmute.com.blankcil.constants;

import hcmute.com.blankcil.model.AuthenticateRequest;
import hcmute.com.blankcil.model.AuthenticateResponse;
import hcmute.com.blankcil.model.PodcastResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("podcast/view/page")
    Call<PodcastResponse> getPodcasts(
        @Query("pageNumber") int pageNumber
    );
    @POST("auth/authenticate")
    Call<AuthenticateResponse> authenticate(
        @Body AuthenticateRequest request
    );
}
