package evans.exercise.user.ui.main

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import evans.exercise.user.R
import evans.exercise.user.data.User
import evans.exercise.user.databinding.ItemUserInListBinding

/**
 * Created by evans.lai on 2022/3/5.
 */

private const val USER_ITEM = R.layout.item_user_in_list

class UserListAdapter(private val userItemClickListener: UserItemClickListener) :
    ListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USER_ITEM -> UserInListViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserInListViewHolder -> {
                val user = getItem(position)
                holder.itemView.setOnClickListener {
                    userItemClickListener.onUserClicked(user)
                }
                holder.bind(user)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is User -> USER_ITEM
            else -> throw IllegalArgumentException("Unknown ViewHolder class")
        }
    }

    fun attachPhoto(position: Int, bitmap: Bitmap?) {
        getItem(position).photo = bitmap
        notifyItemChanged(position)
    }

}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.login == newItem.login
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}

class UserInListViewHolder(val binding: ItemUserInListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): UserInListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemUserInListBinding.inflate(layoutInflater, parent, false)
            return UserInListViewHolder(binding)
        }
    }

    fun bind(user: User) {
        Log.d("UserListAdapter", "bind: $user")
        binding.login.text = user.login
        binding.avatar.setImageBitmap(user.photo)
    }
}

interface UserItemClickListener {
    fun onUserClicked(user: User)
}