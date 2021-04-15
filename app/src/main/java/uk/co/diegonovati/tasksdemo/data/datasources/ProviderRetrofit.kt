package uk.co.diegonovati.tasksdemo.data.datasources

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.diegonovati.tasksdemo.BuildConfig
import java.util.concurrent.TimeUnit

class ProviderRetrofit {

    fun getRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl(BuildConfig.backendUrl)
        .client(getHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getHttpClient(): OkHttpClient {
        var level = HttpLoggingInterceptor.Level.NONE
        if (BuildConfig.BUILD_TYPE != "release") {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = level

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}