package com.my_project.deliveringgoods.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Notification


class NotificationsAdapter(private val action: (Notification) -> Unit) :
    RecyclerView.Adapter<NotificationsAdapter.NotifyHolder>() {

    private val items: MutableList<Notification> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotifyHolder(view)
    }

    override fun onBindViewHolder(holder: NotifyHolder, position: Int) {
        val user = items[position]
        holder.bindTo(user)
    }

    override fun getItemCount() = items.size

    fun swapItems(notify: List<Notification>) {
        val diffResult = DiffUtil.calculateDiff(NotificationsDiffCallback(this.items, notify))
        items.addAll(notify)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, notify: Notification) {
        items[position] = notify
        notifyItemChanged(position)
    }

    fun insertItem(position: Int, notify: Notification) {
        items.add(position, notify)
        notifyItemInserted(position)
    }

    fun deleteItem(notify: Notification) {
        val position = items.indexOf(notify)
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class NotifyHolder(private val containerView: View) : RecyclerView.ViewHolder(containerView) {
        private var notifyTextView = containerView.findViewById(R.id.notifyTextView) as AppCompatTextView
        private var notifyImageView = containerView.findViewById(R.id.notifyImageView) as SimpleDraweeView

        init {
            containerView.setOnClickListener { action(items[layoutPosition]) }
        }

        fun bindTo(notify: Notification) = with(notify) {
            notifyTextView.text = message
            notifyImageView.setImageURI(url)
        }
    }
}