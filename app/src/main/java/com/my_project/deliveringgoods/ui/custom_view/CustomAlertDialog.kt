package com.my_project.deliveringgoods.ui.custom_view

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.my_project.deliveringgoods.R


open class CustomAlertDialog(context: Context) {

    private var dialog: MaterialDialog = MaterialDialog(context)
    private var message: AppCompatTextView
    private var title: AppCompatTextView
    private var image: AppCompatImageView
    private var negativeButton: AppCompatTextView
    private var positiveButton: AppCompatTextView

    init {
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        message = dialog.findViewById(R.id.textViewMessage)
        title = dialog.findViewById(R.id.textViewTitle)
        image = dialog.findViewById(R.id.imageViewDialog)
        negativeButton = dialog.findViewById(R.id.textViewNegative)
        positiveButton = dialog.findViewById(R.id.textViewPositive)
    }

    protected fun setDialogTitle(text: String = "") {
        title.visibility = View.VISIBLE
        title.text = text
    }

    protected fun setDialogMessage(text: String) {
        message.text = text
    }

    protected fun setDialogImage(res: Int) {
        image.visibility = View.VISIBLE
        image.setImageDrawable(ContextCompat.getDrawable(dialog.context, res))
    }

    protected fun setDialogNegativeButton(text: String, listener: DialogInterface.OnClickListener) {
        negativeButton.visibility = View.VISIBLE
        negativeButton.text = text
        negativeButton.setOnClickListener { listener.onClick(dialog, R.id.textViewNegative) }
    }

    protected fun setDialogPositiveButton(text: String, listener: DialogInterface.OnClickListener) {
        positiveButton.text = text
        positiveButton.setOnClickListener { listener.onClick(dialog, R.id.textViewPositive) }
    }

    fun dismiss() {
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }

    protected fun getDialog(): CustomAlertDialog {
        return this
    }

    class Builder(private val context: Context) : CustomAlertDialog(context) {

        private var textTitle: String? = null
        private var textMessage: String? = null
        private var textPositive: String? = null
        private var textNegative: String? = null
        private lateinit var listenerNegative: DialogInterface.OnClickListener
        private lateinit var listenerPositive: DialogInterface.OnClickListener
        private var imageRes: Int? = null

        fun setTitle(title: String): Builder {
            textTitle = title
            return this
        }

        fun setTitle(res: Int): Builder {
            textTitle = context.getString(res)
            return this
        }

        fun setMessage(text: String): Builder {
            textMessage = text
            return this
        }

        fun setMessage(res: Int): Builder {
            textMessage = context.getString(res)
            return this
        }

        fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            textNegative = text
            listenerNegative = listener
            return this
        }

        fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            textPositive = text
            listenerPositive = listener
            return this
        }

        fun setImage(res: Int): Builder {
            imageRes = res
            return this
        }

        fun build() {
            constructDialog()
            show()
        }

        fun create(): CustomAlertDialog {
            constructDialog()
            return getDialog()
        }

        private fun constructDialog() {
            if (textTitle != null) {
                setDialogTitle(textTitle ?: "")
            }
            if (textMessage != null) {
                setDialogMessage(textMessage ?: "")
            }
            if (textPositive != null) {
                setDialogPositiveButton(textPositive ?: "", listenerPositive)
            }
            if (textNegative != null) {
                setDialogNegativeButton(textNegative ?: "", listenerNegative)
            }
            if (imageRes != null) {
                setDialogImage(imageRes ?: -1)
            }
        }

    }
}