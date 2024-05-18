package hcmute.com.blankcil.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hcmute.com.blankcil.constants.APIService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //Dùng để chạy local host song song với máy ảo
//    private static String BASE_URL="http://10.0.2.2:9090/api/v1/";

    //Dùng để chạy local host song song với máy THẬT, cả 2 thiết bị phải cùng kết nối chung 1 internet
    //và thay IPv4 của (windows/mac) vào bên dưới nhé.
//    private static String BASE_URL="http://192.168.1.3:9090/api/v1/";
    private static String BASE_URL="http://192.168.1.132:9090/api/v1/";


    private static RetrofitClient retrofitClient;

    private static Retrofit retrofit;

    private OkHttpClient.Builder builder = new OkHttpClient.Builder();

    private HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").create();

    private RetrofitClient() {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public APIService getApi() {
        return retrofit.create(APIService.class);
    }
}
