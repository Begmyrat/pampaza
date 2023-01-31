package com.fabrika.pampaza.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.home.model.PostEntity

class MyPostAdapter(var activity: MainActivity) : RecyclerView.Adapter<MyPostAdapter.FinanceListAdapterViewHolder>() {

    var onComplaintButtonsClick: ((entity: String) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FinanceListAdapterViewHolder {
        val layoutId = when (viewType) {
            TYPE_NEWS -> R.layout.item_post
            else -> throw IllegalArgumentException("Invalid type")
        }

        return FinanceListAdapterViewHolder(
            parent.context,
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )
    }

    companion object {
        private const val TYPE_NEWS = 1
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_NEWS
    }

    private val differCallback = object : DiffUtil.ItemCallback<PostEntity>() {
        override fun areItemsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PostEntity, newItem: PostEntity): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onBindViewHolder(holder: FinanceListAdapterViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class FinanceListAdapterViewHolder(private val context: Context, private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun bindNewsBodyItem(item: PostEntity) {

        }

        fun bind(model: PostEntity) {
            bindNewsBodyItem(model)
        }

    }
}