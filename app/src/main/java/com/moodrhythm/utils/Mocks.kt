package com.moodrhythm.utils

import android.content.SharedPreferences

class MockSharedPreferences : SharedPreferences {
    private val data = mutableMapOf<String, Any?>()

    override fun getInt(key: String, defValue: Int): Int {
        return data[key] as? Int ?: defValue
    }

    override fun getLong(p0: String?, p1: Long): Long {
        TODO("Not yet implemented")
    }

    override fun getFloat(p0: String?, p1: Float): Float {
        TODO("Not yet implemented")
    }

    override fun getBoolean(p0: String?, p1: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun getString(key: String, defValue: String?): String? {
        return data[key] as? String ?: defValue
    }

    override fun getStringSet(p0: String?, p1: MutableSet<String>?): MutableSet<String>? {
        TODO("Not yet implemented")
    }

    override fun edit(): SharedPreferences.Editor {
        return MockEditor(data)
    }

    override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {
        TODO("Not yet implemented")
    }

    override fun contains(key: String?): Boolean = data.containsKey(key)
    override fun getAll(): MutableMap<String, *> = data

}

class MockEditor(private val data: MutableMap<String, Any?>) : SharedPreferences.Editor {
    override fun putInt(key: String, value: Int): SharedPreferences.Editor {
        data[key] = value
        return this
    }

    override fun putLong(p0: String?, p1: Long): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putFloat(p0: String?, p1: Float): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun putBoolean(p0: String?, p1: Boolean): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun remove(p0: String?): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun clear(): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun commit(): Boolean {
        TODO("Not yet implemented")
    }

    override fun putString(key: String, value: String?): SharedPreferences.Editor {
        data[key] = value
        return this
    }

    override fun putStringSet(p0: String?, p1: MutableSet<String>?): SharedPreferences.Editor {
        TODO("Not yet implemented")
    }

    override fun apply() {
    }
}
