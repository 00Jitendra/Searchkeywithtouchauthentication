package com.example.searchkeywithtouchauthentication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.searchkeywithtouchauthentication.data.model.SearchKeyModel
import com.example.searchkeywithtouchauthentication.databinding.ItemSearchBinding
import com.example.searchkeywithtouchauthentication.utils.imageHelper.ImageHelper


class SearchActivityAdapter(
    private val viewModel: HomeViewModel,
    val mContext: HomeActivity
) :
    ListAdapter<SearchKeyModel.Hit, SearchActivityAdapter.ViewHolder>(
        TaskDiffCallbackReport()
    ) {

    fun swapData(data: List<SearchKeyModel.Hit>) {
        submitList(data.toMutableList())
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(viewModel, getItem(position), mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor(val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            viewModel: HomeViewModel,
            item: SearchKeyModel.Hit,
            context: HomeActivity
        ) {

            binding.viewModel = viewModel
            binding.allItem = item
            binding.executePendingBindings()
            ImageHelper.loadImage(binding.images,item.previewURL,context)

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemSearchBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(
                    binding
                )
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallbackReport : DiffUtil.ItemCallback<SearchKeyModel.Hit>() {
    override fun areItemsTheSame(
        oldItem: SearchKeyModel.Hit,
        newItem: SearchKeyModel.Hit
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SearchKeyModel.Hit,
        newItem: SearchKeyModel.Hit
    ): Boolean {
        return oldItem == newItem
    }
}