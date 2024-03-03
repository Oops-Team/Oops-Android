package com.oops.oops_android.config

import com.oops.oops_android.ApplicationClass.Companion.X_AUTH_TOKEN
import com.oops.oops_android.utils.getToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/** API 통신 시 매번 호출
 *  spf에 저장된 값(=access token)을 header에 넣어주기 위한 클래스
 */
class XAcessTokenIntercepter: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val jwtToken: String? = getToken()

        if (jwtToken != null) {
            builder.addHeader(X_AUTH_TOKEN, jwtToken)
        }

        return chain.proceed(builder.build())
    }
}