package ws.tilda.anastasia.biotopeapp.networking;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ws.tilda.anastasia.biotopeapp.parsing.ToStringConverterFactory;

public class ApiClient {


    private static final String APIPATH = "http:biotope.cs.hut.fi/omi/node/";
    private static final String APIPATH_ps = "http:veivi.parkkis.com:8080";

    private static OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

    private static RetrofitService retrofitService = null;

    public static RetrofitService getApi() {

        getLogger();

        if (retrofitService == null) {
            Retrofit retrofit = getRetrofit();
            retrofitService = retrofit.create(RetrofitService.class);
        }
        return retrofitService;
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(APIPATH)
                .addConverterFactory(new ToStringConverterFactory())
                .client(okhttpClientBuilder.build())
                .build();
    }

    private static void getLogger() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);
    }

    public interface RetrofitService {
        @POST(".")
        Call<String> getResponse(@Body String query);
    }
}
