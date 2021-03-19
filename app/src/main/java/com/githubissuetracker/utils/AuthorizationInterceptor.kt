package com.githubissuetracker.utils

import com.githubissuetracker.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + BuildConfig.GITHUB_KEY)
            .build()

        return chain.proceed(request)
    }
}