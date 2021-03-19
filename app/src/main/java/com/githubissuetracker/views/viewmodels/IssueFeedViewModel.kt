package com.githubissuetracker.views.viewmodels

import ListIssuesQuery
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.githubissuetracker.R
import com.githubissuetracker.structure.HeadersStructure
import com.githubissuetracker.structure.IssueStructure
import com.githubissuetracker.structure.ResultState
import com.githubissuetracker.utils.Constants.Companion.DATE_MONTH_YEAR_FORMATTED
import com.githubissuetracker.utils.Constants.Companion.GENERIC_DATE_TIME_FORMAT_UTC
import com.githubissuetracker.utils.Utils
import com.githubissuetracker.views.adapter.HeadersAdapter
import com.githubissuetracker.views.adapter.IssuesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class IssueFeedViewModel @Inject constructor(private val apolloClient: ApolloClient) : ViewModel() {
    var resultStateLiveData: MutableLiveData<ResultState> = MutableLiveData()
    var headerItemClickLiveData: MutableLiveData<Int> = MutableLiveData()
    var issuesList: MutableList<IssueStructure> = ArrayList()
    var headersList: MutableList<HeadersStructure> = ArrayList()
    var issuesAdapter: IssuesAdapter
    var headersAdapter: HeadersAdapter

    init {
        fetchIssues()
        issuesAdapter = IssuesAdapter(R.layout.issue_item, this)
        headersAdapter = HeadersAdapter(R.layout.header_item, this)
    }

    fun fetchIssues() {
        var errorMessage = ""
        setResult(ResultState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                apolloClient.query(ListIssuesQuery()).toDeferred().await()
            } catch (e: ApolloException) {
                e.printStackTrace()
                errorMessage = e.message.toString()
                setResult(ResultState.Success(false, errorMessage))
                return@launch
            }
            val repository = response.data?.repository
            if (repository != null && !response.hasErrors()) {
                issuesList.clear()
                //create a new structure because the generated ListIssuesQuery has data-binding
                // issues and also causes cyclic dependencies error
                repository.issues.edges?.map {
                    //add an issue object to list
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
                //add header labels to list
                headersList.clear()
                addFilterAndDateHeaders()
                repository.languages?.edges?.map {
                    val headersStructure = it?.node?.let { node -> HeadersStructure(node.name) }
                    headersStructure?.let { it1 -> headersList.add(it1) }
                }
            }
            setResult(ResultState.Success(true, errorMessage))
        }
    }

    private fun addFilterAndDateHeaders() {
        val dateHeader = HeadersStructure("Date")
        headersList.add(dateHeader)
        val filterHeader = HeadersStructure("Filter")
        headersList.add(filterHeader)
    }

    private fun setResult(result: ResultState) {
        resultStateLiveData.postValue(result)
    }

    fun setIssuesToAdapter() {
        issuesAdapter.setIssuesList(issuesList)
        headersAdapter.setHeadersList(headersList)
    }

    fun headerItemClick(position: Int) {
        headerItemClickLiveData.value = position
    }
}