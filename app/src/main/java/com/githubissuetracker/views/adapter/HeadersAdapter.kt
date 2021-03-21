package com.githubissuetracker.views.adapter

import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.githubissuetracker.BR
import com.githubissuetracker.structure.HeadersStructure
import com.githubissuetracker.views.viewmodels.IssueFeedViewModel
import com.skydoves.powermenu.CircularEffect
import com.skydoves.powermenu.MenuAnimation
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem


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
        return ViewHolder(viewBinding, parent.context)
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

    class ViewHolder(private val binding: ViewDataBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var issueFeedViewModel: IssueFeedViewModel
        fun bind(position: Int, issueFeedViewModel: IssueFeedViewModel, header: HeadersStructure) {
            binding.setVariable(BR.structure, header)
            binding.setVariable(BR.position, position)
            binding.setVariable(BR.viewModel, issueFeedViewModel)
            this.issueFeedViewModel = issueFeedViewModel
            val headerCell =
                binding.root.findViewById<ConstraintLayout>(com.githubissuetracker.R.id.root_view)
            headerCell.setOnClickListener {
                when (position) {
                    0 -> {//date item selected
                        issueFeedViewModel.dateFilterItemClick()
                    }
                    1 -> {//filter item selected
                        showMenu(it)
                    }//ignore the rest
                }

            }
        }

        private fun showMenu(view: View) {
            val powerMenu = PowerMenu.Builder(context)
                .setHeaderView(com.githubissuetracker.R.layout.filter_header)
                .addItem(PowerMenuItem("All", false)) // add an item.
                .addItem(PowerMenuItem("Open", false)) // add an item.
                .addItem(PowerMenuItem("Closed", false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                .setCircularEffect(CircularEffect.BODY)
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.githubissuetracker.R.color.text_color
                    )
                )
                .setTextGravity(Gravity.START)
                .setTextTypeface(Typeface.createFromAsset(context.assets, "fonts/Lato-Regular.ttf"))
                .setSelectedTextColor(
                    ContextCompat.getColor(
                        context,
                        com.githubissuetracker.R.color.text_color
                    )
                )
                .setMenuColor(Color.WHITE)
                .setAutoDismiss(true)
                .setTextSize(18)
                .setSelectedMenuColor(ContextCompat.getColor(context, R.color.white))
                .setOnMenuItemClickListener { position, item ->
                    //For position 0 (All), we will send an empty state. Supported states are "open" and "closed"
                    issueFeedViewModel.filterByState(if (item.title.toString() == "All") "" else item.title.toString())
                }
                .build()
                .showAsAnchorRightBottom(view)
        }
    }
}