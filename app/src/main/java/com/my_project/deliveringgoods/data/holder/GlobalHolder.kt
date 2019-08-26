package com.my_project.deliveringgoods.data.holder

object GlobalHolder {

    private val hashMap = HashMap<String, Any?>()

    fun saveData(key: String, value: Any?) {
        hashMap[key] = value
    }

    fun getData(key: String): Any? = hashMap[key]

    fun isEmpty() = hashMap.isEmpty()

    fun sizeHolder() = hashMap.size

    fun clearHolder() {
        hashMap.clear()
    }
}