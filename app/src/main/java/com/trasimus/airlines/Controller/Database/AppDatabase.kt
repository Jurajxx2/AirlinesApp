package com.trasimus.airlines.Controller.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.trasimus.airlines.Objects.Airline

@Database(entities = [(Airline::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun airlineModel(): AirlineDao

    companion object {

        private var sInstance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                sInstance = Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "database")
                    .build()
            }
            return sInstance!!
        }

        fun destroyInstance() {
            sInstance = null
        }


    }
}