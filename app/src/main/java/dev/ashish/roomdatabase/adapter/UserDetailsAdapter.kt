package dev.ashish.roomdatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ashish.roomdatabase.R
import dev.ashish.roomdatabase.databinding.UserDetailsBinding
import dev.ashish.roomdatabase.entity.UserDetails

class UserDetailsAdapter(private val mListener: UserDetailsAdapterListener) :
    ListAdapter<UserDetails, UserDetailsAdapter.ItemViewHolder>(UserDetailsDiffCallback()) {


    inner class ItemViewHolder(private val userDetailsBinding: UserDetailsBinding) :
        RecyclerView.ViewHolder(userDetailsBinding.root) {
        fun bind(item: UserDetails, listener: UserDetailsAdapterListener) {
            userDetailsBinding.userDetailsModel = item
            userDetailsBinding.listener = listener
            userDetailsBinding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: UserDetailsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.lsv_item,
            parent, false
        )
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), mListener)
    }

    class UserDetailsDiffCallback : DiffUtil.ItemCallback<UserDetails>() {

        override fun areItemsTheSame(oldItem: UserDetails, newItem: UserDetails): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: UserDetails, newItem: UserDetails): Boolean {
            return oldItem.firstName == newItem.firstName &&
                    oldItem.lastName == newItem.lastName &&
                    oldItem.email == newItem.email &&
                    oldItem.mobileNumber == newItem.mobileNumber
        }


    }

}

interface UserDetailsAdapterListener {
    fun onItemDelete(userDetails: UserDetails)
    fun onItemEdit(userDetails: UserDetails)
}