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
    public static RestApiService getApiService() {
        if (retrofit == null) {

            /*val cacheInterceptor: Interceptor = object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val cacheBuilder = CacheControl.Builder()
                    cacheBuilder.maxAge(0, TimeUnit.SECONDS)
                    cacheBuilder.maxStale(365, TimeUnit.DAYS)
                    val cacheControl = cacheBuilder.build()
                    var request: Request = chain.request()
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build()

                    val originalResponse: Response = chain.proceed(request)
                    return originalResponse.newBuilder()
                            .header(
                                    "Cache-Control", "no-cache"
                            )
                            .build()
                }
            }*/

            /*var interceptor: Interceptor = object : Interceptor {
                @Throws(java.io.IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {

                    return chain.proceed(
                            chain.request()
                                    .newBuilder()
                                    .build()
                    )
                }
            }

            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(cacheInterceptor)
                    //.cache(RepositoryId.cache)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build()*/


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
