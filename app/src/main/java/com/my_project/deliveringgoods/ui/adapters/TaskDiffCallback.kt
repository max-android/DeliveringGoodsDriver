package com.my_project.deliveringgoods.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.my_project.deliveringgoods.data.entities.Order

class TaskDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}