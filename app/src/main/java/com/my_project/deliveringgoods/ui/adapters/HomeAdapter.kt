package com.my_project.deliveringgoods.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.PagerAdapter
import com.facebook.drawee.view.SimpleDraweeView
import com.my_project.deliveringgoods.R
import com.my_project.deliveringgoods.data.entities.AdvInfo

class HomeAdapter : PagerAdapter() {

    var info: List<AdvInfo> = mutableListOf()

    fun addData(data: List<AdvInfo>) {
        this.info = data
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 === p1 as View
    }

    override fun getCount() = info.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val layout = inflater.inflate(R.layout.layout_home_pager_item, container, false) as ViewGroup
        val imageView = layout.findViewById<SimpleDraweeView>(R.id.infoImageView)
        val textView = layout.findViewById<AppCompatTextView>(R.id.infoTextView)
        textView.text = info[position].info
        imageView.setImageURI(info[position].image)
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return ""
    }
}