package com.my_project.deliveringgoods.data.provider

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class ResourceProvider(private val context: Context) {

    fun getString(@StringRes id: Int): String {
        return context.getString(id)
    }

    fun getStringForm(@StringRes id: Int,any:Any): String {
        return context.getString(id,any)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    fun getFloat(@DimenRes id: Int):Float{
        return context.resources.getDimension(id)
    }

    fun getStringArray(id: Int): Array<String> {
        return context.resources.getStringArray(id)
    }

    fun getDimens(id: Int):Int {
        return  context.resources.getDimension(id).toInt()
    }
}