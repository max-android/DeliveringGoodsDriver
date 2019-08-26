package com.my_project.deliveringgoods.data.local_storage

import CONST
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class UserDataHolder(context: Context) {

    private var preferences: SharedPreferences = context.getSharedPreferences(CONST.USER_DATA_HOLDER, Context.MODE_PRIVATE)

    var token: String?
        get() = preferences.getString(CONST.TOKEN_KEY, null)
        set(token) = preferences.edit {
            putString(CONST.TOKEN_KEY, token)
        }

   var nameUser: String
            get() =  preferences.getString(CONST.USER_NAME_KEY, CONST.DEFAULT_USER_NAME_KEY)!!
         set(name) = preferences.edit {
        putString(CONST.USER_NAME_KEY, name)
    }

    var phoneUser: String
       get() = preferences.getString(CONST.USER_PHONE_KEY, CONST.DEFAULT_USER_PHONE_KEY)!!
       set(phone) = preferences.edit {
        putString(CONST.USER_PHONE_KEY, phone)
         }

    fun deleteDataUser() {
        preferences.edit {
            remove(CONST.TOKEN_KEY)
            remove(CONST.USER_NAME_KEY)
            remove(CONST.USER_PHONE_KEY)
        }
    }
}