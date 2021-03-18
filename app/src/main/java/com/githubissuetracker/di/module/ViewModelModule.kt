package com.githubissuetracker.di.module

import androidx.lifecycle.ViewModel
import com.githubissuetracker.di.util.ViewModelKey
import com.githubissuetracker.views.viewmodels.IssueFeedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(IssueFeedViewModel::class)
    abstract fun bindIssueFeedViewModel(viewModel: IssueFeedViewModel): ViewModel
}