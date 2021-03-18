package com.githubissuetracker.utils

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer f609cb419f2e94b569ead7cba60b475411f04518")
            .build()

        return chain.proceed(request)
    }
}