package com.oops.oops_android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.oops.oops_android.config.XAcessTokenIntercepter
import com.navercorp.nid.NaverIdLoginSDK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import com.oops.oops_android.BuildConfig

/* 공통적으로 사용하는 데이터를 관리하는 파일 */
class ApplicationClass: Application() {
    init {
        instance = this
    }
    companion object {
        lateinit var instance: ApplicationClass

        // 전용 applicationcontext 가져오기
        fun applicationContext(): Context {
            return instance.applicationContext
        }

        const val X_AUTH_TOKEN: String = "xAuthToken" // token 키 값
        const val TAG: String = "AUTH" // SharedPreferences 키 값
        private const val DEV_URL: String = "http://10.0.2.2:8080" // 테스트 주소
        private const val PROD_URL: String = BuildConfig.API_KEY // 실서버 주소
        const val BASE_URL: String = PROD_URL // 서버를 번갈아 가며 테스트하기 위한 변수

        lateinit var retrofit: Retrofit
        lateinit var mSharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        // 네이버 로그인 SDK 초기화
        NaverIdLoginSDK.initialize(this, getString(R.string.naver_login_client_id) , getString(R.string.naver_login_client_secret), getString(R.string.app_name))

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        // http 통신 타이머 설정
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(50000, TimeUnit.MILLISECONDS)
            .connectTimeout(50000, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .addNetworkInterceptor(XAcessTokenIntercepter()) // JWT 자동 헤더 전송
            .build()

        val gson = GsonBuilder()
            .setLenient()
            // LocalDate 기본 형식 지정
            .registerTypeAdapter(LocalDate::class.java, JsonDeserializer<LocalDate> { json, typeOfT, context ->
                if (json.asJsonPrimitive.isNumber || json.asJsonArray.isJsonObject) {
                    LocalDate.parse("yyyy-MM-dd")
                }
                else
                    null
            })
            // LocalTime 기본 형식 지정
            .registerTypeAdapter(LocalTime::class.java, JsonDeserializer<LocalTime> { json, typeOfT, context ->
                if (json.asJsonPrimitive.isNumber || json.asJsonArray.isJsonObject) {
                    LocalTime.parse("HH:mm")
                }
                else
                    null
            })
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
}