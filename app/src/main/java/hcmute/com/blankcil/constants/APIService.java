package hcmute.com.blankcil.constants;

import hcmute.com.blankcil.model.AuthenticateRequest;
import hcmute.com.blankcil.model.AuthenticateResponse;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import hcmute.com.blankcil.model.SearchResponse;
import okhttp3.MultipartBody;
import hcmute.com.blankcil.model.ConfirmRequest;
import hcmute.com.blankcil.model.PodcastResponse;
import hcmute.com.blankcil.model.ProfileResponse;
import hcmute.com.blankcil.model.RegisterRequest;
import hcmute.com.blankcil.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {
    @GET("podcast/auth/view/page")
    Call<PodcastResponse> getPodcasts(
            @Header("Authorization") String token,
            @Query("pageNumber") int pageNumber,
            @Query("trending") boolean trending
    );

    @Multipart
    @POST("podcast/upload")
    Call<ResponseModel<PodcastModel>> createPodcast(
            @Header("Authorization") String token,
            @Part MultipartBody.Part title,
            @Part MultipartBody.Part content,
            @Part MultipartBody.Part imageFile,
            @Part MultipartBody.Part audioFile
    );

    @POST("auth/authenticate")
    Call<AuthenticateResponse> authenticate(
            @Body AuthenticateRequest request
    );

    @POST("auth/register")
    Call<RegisterResponse> register(
            @Body RegisterRequest registerRequest
    );

    @POST("auth/confirm-email")
    Call<AuthenticateResponse> confirmEmail(
            @Body ConfirmRequest confirmRequest
    );

    @POST("auth/refresh-token")
    Call<AuthenticateResponse> refreshToken(
            @Header("Authorization") String token
    );

    @GET("users/profile")
    Call<ProfileResponse> getProfile(
            @Header("Authorization") String token
    );

    @GET("users/search")
    Call<SearchResponse> search(
            @Query("keyword") String keyword
    );
}
