package hcmute.com.blankcil.constants;

import hcmute.com.blankcil.model.PageResponseModel;
import hcmute.com.blankcil.model.PodcastModel;
import hcmute.com.blankcil.model.ResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("podcast/view/page?pageNumber=0&trending=false")
    Call<ResponseModel<PageResponseModel<PodcastModel>>> getPodcasts();
}
