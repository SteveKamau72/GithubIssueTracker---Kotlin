package com.githubissuetracker.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("setAdapter")
fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.setHasFixedSize(true)
    recyclerView.addItemDecoration(RvListDividerItemDecoration(recyclerView.context))
    recyclerView.isNestedScrollingEnabled = false
    recyclerView.adapter = adapter
}
@BindingAdapter("setHorizontalAdapter")
fun setHorizontalAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.isNestedScrollingEnabled = false
    recyclerView.setHasFixedSize(true)
    recyclerView.adapter = adapter
}