package com.my_project.deliveringgoods.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.my_project.deliveringgoods.data.entities.Notification

class NotificationsDiffCallback(
    private val oldList: List<Notification>,
    private val newList: List<Notification>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
}