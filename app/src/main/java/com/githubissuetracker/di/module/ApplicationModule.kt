package com.githubissuetracker.di.module

import android.app.Application
import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.githubissuetracker.utils.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideApolloClient() : ApolloClient{
        return ApolloClient.builder()
            .serverUrl("https://api.github.com/graphql")
            .okHttpClient(OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor())
                .build())
            .build()
    }
}