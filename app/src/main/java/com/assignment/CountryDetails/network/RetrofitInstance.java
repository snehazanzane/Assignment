package com.assignment.CountryDetails.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit = null;

    /**
     * Create the Instance of Retrofit REST API entrypoint
     * @return
     */
    public static RestApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            return chain.proceed(chain.request()
                                    .newBuilder()
                                    .build());
                        }
                    })
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS);
            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit
                    .Builder()
                    .client(okHttpClient)
                    .baseUrl(NetworkAPIUrls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(RestApiService.class);
    }
}
