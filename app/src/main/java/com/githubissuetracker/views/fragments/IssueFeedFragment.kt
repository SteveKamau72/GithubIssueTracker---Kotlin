package com.githubissuetracker.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.githubissuetracker.R
import com.githubissuetracker.databinding.FragmentIssueFeedBinding
import com.githubissuetracker.structure.ResultState
import com.githubissuetracker.utils.ViewModelFactory
import com.githubissuetracker.views.viewmodels.IssueFeedViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class IssueFeedFragment : DaggerFragment() {
    private lateinit var binding: FragmentIssueFeedBinding
    private lateinit var viewModel: IssueFeedViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_issue_feed,
            container,
            false
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[IssueFeedViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.resultStateLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is ResultState.Loading -> {
                    viewModel.setIssuesToAdapter()
                }
                is ResultState.Success -> {
                    viewModel.setIssuesToAdapter()
                }
            }
        })
        return binding.root
    }
}