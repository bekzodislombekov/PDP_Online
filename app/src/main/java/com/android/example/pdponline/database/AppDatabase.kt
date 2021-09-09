package com.android.example.pdponline.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.example.pdponline.dao.CourseDao
import com.android.example.pdponline.dao.LessonDao
import com.android.example.pdponline.dao.ModuleDao
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Lesson
import com.android.example.pdponline.models.Module

@Database(entities = [Course::class, Module::class, Lesson::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao
    abstract fun moduleDao(): ModuleDao
    abstract fun lessonDao(): LessonDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "pdp")
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}