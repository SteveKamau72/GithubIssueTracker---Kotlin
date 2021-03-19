package com.githubissuetracker.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.githubissuetracker.BR
import com.githubissuetracker.structure.HeadersStructure
import com.githubissuetracker.views.viewmodels.IssueFeedViewModel

class HeadersAdapter(
    @param:LayoutRes private val layoutId: Int,
    private val issueFeedViewModel: IssueFeedViewModel
) :
    RecyclerView.Adapter<HeadersAdapter.ViewHolder>() {
    var headersList: List<HeadersStructure> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, issueFeedViewModel, headersList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    override fun getItemCount(): Int {
        return headersList.size
    }

    @JvmName("setIssuesList1")
    fun setHeadersList(headersList: List<HeadersStructure>) {
        this.headersList = headersList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, issueFeedViewModel: IssueFeedViewModel, header: HeadersStructure) {
            binding.setVariable(BR.structure, header)
            binding.setVariable(BR.position, position)
            binding.setVariable(BR.viewModel, issueFeedViewModel)
        }
    }
}