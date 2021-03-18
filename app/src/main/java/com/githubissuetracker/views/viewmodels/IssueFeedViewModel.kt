package com.githubissuetracker.views.viewmodels

import ListIssuesQuery
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.githubissuetracker.R
import com.githubissuetracker.structure.IssueStructure
import com.githubissuetracker.structure.ResultState
import com.githubissuetracker.utils.Constants.Companion.DATE_MONTH_YEAR_FORMATTED
import com.githubissuetracker.utils.Constants.Companion.GENERIC_DATE_TIME_FORMAT_UTC
import com.githubissuetracker.utils.Utils
import com.githubissuetracker.views.adapter.IssuesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class IssueFeedViewModel @Inject constructor(private val apolloClient: ApolloClient) : ViewModel() {
    var resultStateLiveData: MutableLiveData<ResultState> = MutableLiveData()
    var issuesList: MutableList<IssueStructure> = ArrayList()
    var issuesAdapter: IssuesAdapter

    init {
        fetchIssues()
        issuesAdapter = IssuesAdapter(R.layout.issue_item, this)
    }

    private fun fetchIssues() {
        var errorMessage = ""
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                apolloClient.query(ListIssuesQuery()).toDeferred().await()
            } catch (e: ApolloException) {
                e.printStackTrace()
                errorMessage = e.message.toString()
                setResult(ResultState.Success(false, errorMessage))
                return@launch
            }
            val issues = response.data?.repository
            if (issues != null && !response.hasErrors()) {
                //create a new structure because the generated ListIssuesQuery has data-binding
                // issues and also causes cyclic dependencies error
                issues.issues.edges?.map {
                    val issueStructure = it?.node?.let { node ->
                        node.author?.let { it1 ->
                            IssueStructure(
                                node.title, node.state.name,
                                Utils().formatDateString(
                                    GENERIC_DATE_TIME_FORMAT_UTC,
                                    DATE_MONTH_YEAR_FORMATTED,
                                    node.createdAt.toString()
                                ),
                                it1.login,
                                node.url.toString(),
                                node.comments.totalCount
                            )
                        }
                    }
                    issueStructure?.let { it1 -> issuesList.add(it1) }
                }

            }
            setResult(ResultState.Success(true, errorMessage))
        }
    }

    private fun setResult(result: ResultState) {
        resultStateLiveData.postValue(result)
    }

    fun setIssuesToAdapter() {
        issuesAdapter.setIssuesList(issuesList)
    }
}