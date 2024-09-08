package com.gustavohnsv.drink_me.util

import android.content.Context
import com.gustavohnsv.drink_me.model.DailyRecord
import com.google.gson.Gson
import java.time.LocalDate

class DataStorageUtils {
    
    companion object {
        fun saveData(context: Context, data: DailyRecord) {
            val prefsEditor = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit()
            val gson = Gson()
            prefsEditor.putString("daily_record[" + LocalDate.now().toString() + "]", gson.toJson(data))
            prefsEditor.apply()
        }
        
        fun getData(context: Context): List<DailyRecord> {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val allRecords = prefs.all
            if (allRecords.isEmpty()) {
                return listOf()
            }
            val listRecords = mutableListOf<DailyRecord>()
            for ((_, value) in allRecords) {
                listRecords.add(gson.fromJson(value as String, DailyRecord::class.java))
            }
            return listRecords
        }
        
        fun getDailyData(context: Context): DailyRecord? {
            val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            return prefs.all.mapNotNull {
                it.value as String
            }.mapNotNull {
                gson.fromJson(it, DailyRecord::class.java)
            }.firstOrNull {
                it.date == LocalDate.now().toString()
            }
        }
    }

}