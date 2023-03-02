package com.fabrika.pampaza.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fabrika.pampaza.MainActivity
import com.fabrika.pampaza.R
import com.fabrika.pampaza.databinding.ItemPostBinding
import com.fabrika.pampaza.home.model.PostEntity

class MyPostAdapter(var activity: MainActivity) : RecyclerView.Adapter<MyPostAdapter.MyPostAdapterViewHolder>() {

    var onLikeButtonClick: ((entity: PostEntity) -> Unit)? = null
    var onRePostButtonClick: ((entity: PostEntity) -> Unit)? = null
    var onShareButtonClick: ((entity: PostEntity) -> Unit)? = null
    var onCommentButtonClick: ((entity: PostEntity) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPostAdapterViewHolder {
//        val layoutId = when (viewType) {
//            TYPE_NEWS -> R.layout.item_post
//            else -> throw IllegalArgumentException("Invalid type")
//        }

        val layoutId = R.layout.item_post

        return MyPostAdapterViewHolder(
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

    override fun onBindViewHolder(holder: MyPostAdapterViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyPostAdapterViewHolder(private val context: Context, private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private fun bindNewsBodyItem(item: PostEntity) {
            if(binding is ItemPostBinding){
                Glide.with(context).load(item.authorAvatarUrl).into(binding.iAvatar)
                binding.tUsername.text = item.authorName
                binding.tBody.text = item.body
                binding.tCommentCount.text = item.commentCount.toString()
                binding.tLikeCount.text = item.likeCount.toString()
                binding.tRetweetCount.text = item.rePostCount.toString()
                binding.cardRepost.visibility = View.GONE
                item.originalPostId?.let {
                    binding.cardRepost.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(item.originalPostAuthorAvatarUrl.toString())
                        .circleCrop()
                        .into(binding.iRepostAvatar)
                    binding.tRepostUsername.text = item.originalPostAuthorName.toString()
                    binding.tRepostId.text = item.originalPostAuthorId.toString()
                    binding.tRepostBody.text = item.originalPostBody.toString()
                }

                item.id?.let { checkLikeStatus(it, binding.iLike) }

                binding.iComment.setOnClickListener{
                    onCommentButtonClick?.invoke(item)
                }
                binding.iRetweet.setOnClickListener{
                    onRePostButtonClick?.invoke(item)
                }
                binding.iLike.setOnClickListener{
                    item.id?.let { checkLikeStatus(it, binding.iLike) }
                    onLikeButtonClick?.invoke(item)
                }
                binding.iShare.setOnClickListener{
                    onShareButtonClick?.invoke(item)
                }
            }
        }

        fun bind(model: PostEntity) {
            bindNewsBodyItem(model)
        }

        private fun checkLikeStatus(id: String, view: ImageView){
            if(activity.viewmodel.userEntity.value?.likedPosts?.contains(id) == true){
                view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heard_filled))
            } else {
                view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_heart))
            }
        }

    }
}