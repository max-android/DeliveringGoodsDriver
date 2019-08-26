package com.my_project.deliveringgoods.data.local_storage

import CONST
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class FirebaseHolder(context: Context) {

    private var preferences: SharedPreferences = context.getSharedPreferences(CONST.FIREBASE_DATA_HOLDER, Context.MODE_PRIVATE)

    var token: String
        get() = preferences.getString(CONST.F_TOKEN_KEY, CONST.DEFAULT_TOKEN_KEY)!!
        set(token) = preferences.edit {
            putString(CONST.F_TOKEN_KEY, token)
        }

   var enabledPush: Boolean
    get() = preferences.getBoolean(CONST.PUSH_ENABLED_KEY, false)
    set(enabled) = preferences.edit {
        putBoolean(CONST.PUSH_ENABLED_KEY, enabled)
    }

    fun deleteFirebaseToken() {
        preferences.edit {
            remove(CONST.F_TOKEN_KEY)
            remove(CONST.PUSH_ENABLED_KEY)
        }
    }
}