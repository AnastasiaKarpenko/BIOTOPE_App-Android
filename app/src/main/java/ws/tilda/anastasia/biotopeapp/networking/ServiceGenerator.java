package ws.tilda.anastasia.biotopeapp.networking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ws.tilda.anastasia.biotopeapp.parsing.ToStringConverterFactory;

public class ServiceGenerator {

    public static String apiBaseUrl = "http://biotope.serval.uni.lu:8383/";

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(apiBaseUrl)
                    .addConverterFactory(new ToStringConverterFactory());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    public static void changeApiBaseUrl(String newApiBaseUrl) {
        apiBaseUrl = newApiBaseUrl;

        builder = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(new ToStringConverterFactory());
    }

    public static <S> S createService(
            Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }

        OkHttpClient okHttpClient = httpClient.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        builder.client(okHttpClient);

        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public interface RetrofitService {
        @POST(".")
        Call<String> getResponse(@Body String query);
    }
}