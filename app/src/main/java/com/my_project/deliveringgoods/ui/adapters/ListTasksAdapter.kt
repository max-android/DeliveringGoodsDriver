package com.my_project.deliveringgoods.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.Order
import com.my_project.deliveringgoods.utilities.simpleTimeFormat


class ListTasksAdapter(private val action: (item: Order, view: View) -> Unit) :
    ListAdapter<Order, ListTasksAdapter.TasksHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return TasksHolder(view)
    }

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        holder.bindTo(getItem(position), position)
    }

    inner class TasksHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

        private var valueNumberOrderTextView =
            containerView.findViewById(R.id.valueNumberOrderTextView) as AppCompatTextView
        private var dateOrderTextView = containerView.findViewById(R.id.dateOrderTextView) as AppCompatTextView
        private var nameAddressOrderTextView =
            containerView.findViewById(R.id.nameAddressOrderTextView) as AppCompatTextView

        private lateinit var order: Order

        init {
            containerView.setOnClickListener { action(order, containerView) }
        }

        fun bindTo(order: Order, position: Int) {
            this.order = order
            with(order) {
                valueNumberOrderTextView.text = id.toString()
                dateOrderTextView.text = startTime.simpleTimeFormat + "-" + endTime.simpleTimeFormat
                nameAddressOrderTextView.text = address
            }
        }
    }
}