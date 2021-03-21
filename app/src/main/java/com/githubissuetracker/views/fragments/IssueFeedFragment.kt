package com.githubissuetracker.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.githubissuetracker.R
import com.githubissuetracker.databinding.FragmentIssueFeedBinding
import com.githubissuetracker.structure.ResultState
import com.githubissuetracker.utils.Constants.Companion.QUERY_ISSUE
import com.githubissuetracker.utils.Constants.Companion.REPO_LANGUAGES
import com.githubissuetracker.utils.ViewModelFactory
import com.githubissuetracker.views.viewmodels.IssueFeedViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class IssueFeedFragment : DaggerFragment() {
    private lateinit var binding: FragmentIssueFeedBinding
    private lateinit var viewModel: IssueFeedViewModel
    private lateinit var datePicker: PrimeDatePicker

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
        setViews()
        createObservers()

        return binding.root
    }

    private fun setViews() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.queryIssues("", "", "", "")
        }

        //handle ImeOptions' done button when user searches
        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchText: String = binding.etSearch.text.toString()
                if (searchText.isNotEmpty()) {
                    binding.etSearch.error = null
                    viewModel.queryIssues("", "", "", searchText)
                } else {
                    binding.etSearch.error = getString(R.string.empty_string)
                }
                true
            } else {
                false
            }
        }
    }

    private fun setUpDatePicker() {
        // To show a date picker with Civil dates, also today as the starting date
        val today = CivilCalendar()
        val callback = SingleDayPickCallback { day ->
            //format date and month to add leading zeros
            viewModel.queryIssues(
                "${day.year}-${String.format("%02d", day.month)}-${
                    String.format(
                        "%02d",
                        day.date
                    )
                }",
                "${day.year}-${String.format("%02d", day.month)}-${
                    String.format(
                        "%02d",
                        day.date
                    )
                }", "", ""
            )
        }
        datePicker = PrimeDatePicker.bottomSheetWith(today)
            .pickSingleDay(callback)
            .initiallyPickedSingleDay(today)
            .maxPossibleDate(today)
            .build()
    }

    private fun createObservers() {
        viewModel.resultStateLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is ResultState.Success -> {
                    if (result.request == REPO_LANGUAGES) {
                        setUpDatePicker()
                        viewModel.setLanguagesToAdapter()
                    } else if (result.request == QUERY_ISSUE) {
                        when (result.success) {
                            true -> {
                                binding.swipeRefresh.isRefreshing = false
                                viewModel.setIssuesToAdapter()
                            }
                            false -> {
                                Log.e("error: ", result.message)
                            }
                        }
                    }
                }
            }
        })
        viewModel.dateItemClickLiveData.observe(viewLifecycleOwner, {
            when (it) {
                true -> {//date item selected
                    datePicker.show(childFragmentManager, "Date Picker")
                }
            }
        })
    }
}