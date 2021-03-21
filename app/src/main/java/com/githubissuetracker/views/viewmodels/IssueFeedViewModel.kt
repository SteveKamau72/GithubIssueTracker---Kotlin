package com.githubissuetracker.views.viewmodels

import GetIssuesQuery
import RepoLanguagesQuery
import android.util.Log
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
import com.githubissuetracker.utils.Constants.Companion.QUERY_ISSUE
import com.githubissuetracker.utils.Constants.Companion.REPO_LANGUAGES
import com.githubissuetracker.utils.Constants.Companion.REPO_NAME
import com.githubissuetracker.utils.Constants.Companion.REPO_OWNER
import com.githubissuetracker.utils.Utils
import com.githubissuetracker.views.adapter.HeadersAdapter
import com.githubissuetracker.views.adapter.IssuesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class IssueFeedViewModel @Inject constructor(private val apolloClient: ApolloClient) : ViewModel() {
    var resultStateLiveData: MutableLiveData<ResultState> = MutableLiveData()
    var dateItemClickLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var issuesList: MutableList<IssueStructure> = ArrayList()
    var headersList: MutableList<HeadersStructure> = ArrayList()
    var issuesAdapter: IssuesAdapter
    var headersAdapter: HeadersAdapter
    var startDate: String = ""
    var endDate: String = ""
    var searchText: String = ""

    init {
        issuesAdapter = IssuesAdapter(R.layout.issue_item, this)
        headersAdapter = HeadersAdapter(R.layout.header_item, this)
        fetchRepoLanguages()
        queryIssues("", "", "", "")
    }

    private fun fetchRepoLanguages() {
        var errorMessage = ""
        setResult(ResultState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                apolloClient.query(RepoLanguagesQuery(REPO_OWNER, REPO_NAME)).toDeferred().await()
            } catch (e: ApolloException) {
                e.printStackTrace()
                errorMessage = e.message.toString()
                setResult(ResultState.Success(false, QUERY_ISSUE, errorMessage))
                return@launch
            }
            val repository = response.data?.repository
            if (repository != null && !response.hasErrors()) {
                //create a new structure because the generated RepoLanguagesQuery has data-binding
                // issues and also causes cyclic dependencies error
                //add header labels to list
                headersList.clear()
                addFilterAndDateHeaders()
                repository.languages?.edges?.map {
                    val headersStructure = it?.node?.let { node -> HeadersStructure(node.name) }
                    headersStructure?.let { it1 -> headersList.add(it1) }
                }
            }
            setResult(ResultState.Success(true, REPO_LANGUAGES, errorMessage))
        }
    }

    /**
     * @param startDate YYYY-MM-DD
     * @param endDate YYYY-MM-DD
     * @param state issue state: open or closed
     * @param searchText search title field
     **/
    fun queryIssues(startDate: String, endDate: String, state: String, searchText: String) {
        var errorMessage = ""
        this.startDate = startDate
        this.endDate = endDate
        this.searchText = searchText;
        setResult(ResultState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                apolloClient.query(GetIssuesQuery(getQuery(state)))
                    .toDeferred().await()
            } catch (e: ApolloException) {
                e.printStackTrace()
                errorMessage = e.message.toString()
                setResult(ResultState.Success(false, QUERY_ISSUE, errorMessage))
                return@launch
            }
            val search = response.data?.search
            if (search != null && !response.hasErrors()) {
                issuesList.clear()
                //create a new structure because the generated SearchByDateQuery has data-binding
                // issues and also causes cyclic dependencies error
                search.nodes?.map {
                    //add an issue object to list
                    val issueStructure = it?.asIssue?.let { issue ->
                        issue.author?.let { it1 ->
                            IssueStructure(
                                issue.title, issue.state.name,
                                Utils().formatDateString(
                                    GENERIC_DATE_TIME_FORMAT_UTC,
                                    DATE_MONTH_YEAR_FORMATTED,
                                    issue.createdAt.toString()
                                ),
                                it1.login,
                                issue.url.toString(),
                                issue.comments.totalCount
                            )
                        }
                    }
                    issueStructure?.let { it1 -> issuesList.add(it1) }
                }
            }
            setResult(ResultState.Success(true, QUERY_ISSUE, errorMessage))
        }
    }

    /**
     * @param state
     * Default query:
     * "query" : "repo:tensorflow/tensorflow type:issue"
     * Filters added
     * "query" : "repo:tensorflow/tensorflow created:2020-11-01..2020-12-01 type:issue state:open"
     */
    private fun getQuery(state: String): String {
        val query: StringBuilder = StringBuilder()
        query.append("repo:")
        query.append(REPO_OWNER)
        query.append("/")
        query.append(REPO_NAME)
        if (startDate.isNotEmpty()) {//only add if it is a date query
            query.append(" created:$startDate..$endDate")
        }
        if (state.isNotEmpty()) {//only add if it is a state filter
            query.append(" state:$state")
        }
        if (searchText.isNotEmpty()) {
            query.append(" in:title $searchText")
        }
        query.append(" type:issue")
        Log.e("getQueryForDate: ", query.toString())
        return query.toString()
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
    }

    fun setLanguagesToAdapter() {
        headersAdapter.setHeadersList(headersList)
    }

    fun dateFilterItemClick() {
        dateItemClickLiveData.value = true
    }

    fun filterByState(state: String) {
        queryIssues(startDate, endDate, state.decapitalize(), searchText)
    }
}