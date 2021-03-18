package com.githubissuetracker.di.module

import com.githubissuetracker.views.fragments.IssueFeedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun bindIssueFeedFragment(): IssueFeedFragment
}