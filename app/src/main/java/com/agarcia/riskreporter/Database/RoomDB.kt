package com.agarcia.riskreporter.Database

import android.content.Context
import android.provider.DocumentsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Report::class), version = 1, exportSchema = false)
public abstract class RoomDB:RoomDatabase() {

    abstract fun reportDao(): ReportDAO

    companion object {
        @Volatile
        private var INSTANCE:RoomDB?=null

        fun getInstance(
            context: Context
        ):RoomDB {
            val tempinstance = INSTANCE
            if (tempinstance != null) {
                return tempinstance
            }
            synchronized(this){
                val instance = Room
                    .databaseBuilder(context, RoomDB::class.java, "ReportDB")
                    .build()
                INSTANCE = instance
                return instance
            }
        }

    }
}