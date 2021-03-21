package com.githubissuetracker.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
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
            viewModel.fetchIssues()
        }
    }

    private fun setUpDatePicker() {
        // To show a date picker with Civil dates, also today as the starting date
        val today = CivilCalendar()
        val callback = SingleDayPickCallback { day ->
            //format date and month to add leading zeros
            viewModel.searchIssueByDate(
                "${day.year}-${String.format("%02d", day.month)}-${
                    String.format(
                        "%02d",
                        day.date
                    )
                }",
                "${day.year}-${String.format("%02d", day.month)}-${String.format("%02d", day.date)}"
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
                    binding.swipeRefresh.isRefreshing = false
                    viewModel.setIssuesToAdapter()
                    setUpDatePicker()
                }
            }
        })
        viewModel.headerItemClickLiveData.observe(viewLifecycleOwner, {
            when (it) {
                0 -> {//date item selected
                    datePicker.show(childFragmentManager, "Date Picker")
                }
                1 -> {//filter item selected
                }
            }
        })
    }
}