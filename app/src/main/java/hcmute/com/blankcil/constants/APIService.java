package hcmute.com.blankcil.constants;

import hcmute.com.blankcil.model.AuthenticateRequest;
import hcmute.com.blankcil.model.AuthenticateResponse;
import hcmute.com.blankcil.model.PodcastResponse;
import hcmute.com.blankcil.model.ProfileResponse;
import hcmute.com.blankcil.model.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("podcast/auth/view/page")
    Call<PodcastResponse> getPodcasts(
            @Header("Authorization") String token,
            @Query("pageNumber") int pageNumber,
            @Query("trending") boolean trending
    );

    @POST("auth/authenticate")
    Call<AuthenticateResponse> authenticate(
            @Body AuthenticateRequest request
    );

    @POST("auth/refresh-token")
    Call<AuthenticateResponse> refreshToken(
            @Header("Authorization") String token
    );

    @GET("users/profile")
    Call<ProfileResponse> getProfile(
            @Header("Authorization") String token
    );
}
