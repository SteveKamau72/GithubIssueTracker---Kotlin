package com.githubissuetracker.di.component

import android.app.Application
import com.githubissuetracker.di.module.ActivityBindingModule
import com.githubissuetracker.di.module.ApplicationModule
import com.githubissuetracker.di.module.FragmentBindingModule
import com.githubissuetracker.di.module.ViewModelFactoryModule
import com.githubissuetracker.utils.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
        ActivityBindingModule::class, FragmentBindingModule::class,
        ViewModelFactoryModule::class]
)
interface ApplicationComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}