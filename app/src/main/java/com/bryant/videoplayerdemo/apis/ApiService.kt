package com.bryant.videoplayerdemo.apis

import com.bryant.videoplayerdemo.BuildConfig
import com.bryant.videoplayerdemo.data.VideoData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

private const val URL = "https://srv0api-dev-v2-dot-framy-stage.uc.r.appspot.com/"
private const val PREFIX = "/test1.0/backstage/exm1"
private const val OKHTTP_TIMEOUT = 10L
private const val AUTHORIZATION = "authorization"

object Service {
    private val okhttpClient = OkHttpClient()
        .newBuilder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder().addHeader(AUTHORIZATION, BuildConfig.APIKEY)
                    .build()
            return@addInterceptor chain.proceed(request)
        }
        .connectTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(OKHTTP_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
    }

    val playseeApi: PlayseeApi by lazy {
        retrofit().create(PlayseeApi::class.java)
    }
}

interface PlayseeApi {
    @GET(PREFIX)
    suspend fun getVideoDataList(): Response<VideoData>
}