package com.my_project.deliveringgoods.ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class EmptyRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {
    private var emptyView: View? = null
    private val emptyObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            setUpVisibility()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            setUpVisibility()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            setUpVisibility()
        }
    }

    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        if (getAdapter() != null) {
            getAdapter()!!.unregisterAdapterDataObserver(emptyObserver)
        }
        adapter?.registerAdapterDataObserver(emptyObserver)
        super.setAdapter(adapter)
        setUpVisibility()
    }

    private fun setUpVisibility() {
        if (adapter != null && emptyView != null) {
            val showEmptyView = adapter!!.itemCount == 0
            emptyView!!.visibility = if (showEmptyView) View.VISIBLE else View.GONE
            visibility = if (showEmptyView) View.GONE else View.VISIBLE
        }
    }
}
