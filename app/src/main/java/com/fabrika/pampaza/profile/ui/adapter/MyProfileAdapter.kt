package com.fabrika.pampaza.profile.ui.adapter

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
import com.fabrika.pampaza.common.utils.extensions.toDateString
import com.fabrika.pampaza.databinding.ItemPostBinding
import com.fabrika.pampaza.databinding.ItemProfileInfoBinding
import com.fabrika.pampaza.home.model.PostEntity
import com.fabrika.pampaza.postDetail.ui.PostDetailActivity
import com.fabrika.pampaza.profile.model.ProfileObj

class MyProfileAdapter() : RecyclerView.Adapter<MyProfileAdapter.MyPostAdapterViewHolder>() {

    var onPostItemClicked: ((entity: ProfileObj.ProfilePostEntity, index: Int) -> Unit)? = null
    var onOriginalPostItemClicked: ((entity: ProfileObj.ProfilePostEntity) -> Unit)? = null
    var onLastItemShown: ((entity: ProfileObj.ProfilePostEntity) -> Unit)? = null
    var onEditProfileCLicked: (() -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPostAdapterViewHolder {

        val layoutId = when (viewType){
            TYPE_PROFILE_INFO -> R.layout.item_profile_info
            else -> R.layout.item_post
        }

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
        private const val TYPE_PROFILE_INFO = 1
        private const val TYPE_POSTS = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is ProfileObj.ProfileUserEntity -> TYPE_PROFILE_INFO
            is ProfileObj.ProfilePostEntity -> TYPE_POSTS
            else -> 0
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ProfileObj>() {
        override fun areItemsTheSame(oldItem: ProfileObj, newItem: ProfileObj): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProfileObj, newItem: ProfileObj): Boolean {
            return if(oldItem is ProfileObj.ProfileUserEntity && newItem is ProfileObj.ProfileUserEntity){
                oldItem.id == newItem.id
            } else if (oldItem is ProfileObj.ProfilePostEntity && newItem is ProfileObj.ProfilePostEntity) {
                oldItem.id == newItem.id
            } else {
                return false
            }
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

        private fun bindProfileInfoItem(item: ProfileObj) {
            if(binding is ItemProfileInfoBinding){

                Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/pampaza-4100f.appspot.com/o/profile%2FkemalNurlybackground?alt=media&token=4cd5f2b3-4bfa-454d-a534-c66b7e79263b").into(binding.iBackground)
                Glide.with(context).load("https://pbs.twimg.com/profile_images/1183809420070936579/NpgRmxOq_400x400.jpg").into(binding.iAvatar)

                binding.bEditProfile.setOnClickListener {
                    onEditProfileCLicked?.invoke()
                }
            }
        }

        private fun bindProfilePostItem(item: ProfileObj.ProfilePostEntity) {
            if (binding is ItemPostBinding) {
                Glide
                    .with(context)
                    .load(item.authorAvatarUrl)
                    .error(R.drawable.logo)
                    .into(binding.iAvatar)
                binding.iPostImage.visibility =
                    if (item.imageUrl != null) View.VISIBLE else View.GONE
                item.imageUrl.let {
                    Glide
                        .with(context)
                        .load(it)
                        .into(binding.iPostImage)
                }
                binding.tUsername.text = item.authorName
                binding.tDate.text = item.date.toDateString()
                binding.tBody.text = item.body
                binding.tCommentCount.text = "${item.commentCount ?: 0}"
                binding.tLikeCount.text = item.likeCount.toString()
                binding.tRetweetCount.text = item.rePostCount.toString()
                binding.cardRepost.visibility = View.GONE
                item.originalPostId?.let {
                    binding.cardRepost.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(item.originalPostAuthorAvatarUrl.toString())
                        .error(R.drawable.logo)
                        .circleCrop()
                        .into(binding.iRepostAvatar)
                    binding.tRepostUsername.text = item.originalPostAuthorName.toString()
                    binding.tRepostDate.text = item.originalPostDate.toDateString()
                    binding.tRepostBody.text = item.originalPostBody.toString()
                }

                if (adapterPosition == itemCount - 1) {
                    onLastItemShown?.invoke(item)
                }

                binding.root.setOnClickListener {
                    onPostItemClicked?.invoke(item, adapterPosition)
                }
                binding.cardRepost.setOnClickListener {
                    onOriginalPostItemClicked?.invoke(item)
                }
            }
        }

        fun bind(model: ProfileObj) {
            when(model) {
                is ProfileObj.ProfileUserEntity -> bindProfileInfoItem(model)
                is ProfileObj.ProfilePostEntity -> bindProfilePostItem(model)
            }
        }
    }
}