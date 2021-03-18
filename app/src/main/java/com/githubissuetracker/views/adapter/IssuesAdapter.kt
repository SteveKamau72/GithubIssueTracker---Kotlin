package com.githubissuetracker.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.githubissuetracker.BR
import com.githubissuetracker.structure.IssueStructure
import com.githubissuetracker.views.adapter.IssuesAdapter.ViewHolder
import com.githubissuetracker.views.viewmodels.IssueFeedViewModel

class IssuesAdapter(
    @param:LayoutRes private val layoutId: Int,
    private val issueFeedViewModel: IssueFeedViewModel
) :
    RecyclerView.Adapter<ViewHolder>() {
    var issuesList: List<IssueStructure> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, issuesList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    override fun getItemCount(): Int {
        return issuesList.size
    }

    @JvmName("setIssuesList1")
    fun setIssuesList(issuesList: List<IssueStructure>) {
        this.issuesList = issuesList
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, issue: IssueStructure) {
            binding.setVariable(BR.structure, issue)
            binding.setVariable(BR.position, position)
        }
    }
}