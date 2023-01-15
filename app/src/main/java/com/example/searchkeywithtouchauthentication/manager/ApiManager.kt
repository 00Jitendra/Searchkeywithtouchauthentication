package com.example.searchkeywithtouchauthentication.manager


import android.content.Context
import com.example.searchkeywithtouchauthentication.api.SearchKeyService
import com.example.searchkeywithtouchauthentication.BuildConfig
import com.example.searchkeywithtouchauthentication.utils.Connectivity
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiManager {

    fun initRetrofit(context: Context): SearchKeyService {

        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(makeOkHttpClient(context))
            .build().create(SearchKeyService::class.java)
    }


    private fun makeOkHttpClient(context: Context): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)


        return OkHttpClient.Builder()
            //     .addInterceptor(makeLoggingInterceptor())
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS).retryOnConnectionFailure(true)
            .cache(myCache)
            .apply {
                addInterceptor(Interceptor { chain ->
                    var original = chain.request()
                    original = if (Connectivity.isInternetOn())
                    /*
                    *  If there is Internet, get the cache that was stored 5 seconds ago.
                    *  If the cache is older than 5 seconds, then discard it,
                    *  and indicate an error in fetching the response.
                    *  The 'max-age' attribute is responsible for this behavior.
                    */
                        original.newBuilder().header(
                            "Cache-Control",
                            "public, max-age=" + 5
                        ).method(original.method, original.body).build()
                    else
                    /*
                    *  If there is no Internet, get the cache that was stored 7 days ago.
                    *  If the cache is older than 7 days, then discard it,
                    *  and indicate an error in fetching the response.
                    *  The 'max-stale' attribute is responsible for this behavior.
                    *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                    */
                        original.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        )
                            .method(original.method, original.body).build()
                    // End of if-else statement


                    /* val request = original.newBuilder()
                         .method(original.method(), original.body())
                         .build()*/
                    chain.proceed(original)
                })
                if (BuildConfig.DEBUG)
                    addInterceptor(makeLoggingInterceptor())
                addInterceptor(ConnectivityAwareClient(context))
            }.build()
    }


    private fun makeLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


}