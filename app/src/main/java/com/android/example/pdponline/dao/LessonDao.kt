package com.android.example.pdponline.dao

import androidx.room.*
import com.android.example.pdponline.models.Lesson

@Dao
interface LessonDao {

    @Insert
    fun addLesson(lesson: Lesson)

    @Delete
    fun deleteLesson(lesson: Lesson)

    @Update
    fun updateLesson(lesson: Lesson)

    @Query("SELECT * FROM lessons WHERE module_id =:moduleId ORDER BY position")
    fun getAllLessonsByModule(moduleId: Int): List<Lesson>

    @Query("SELECT * FROM lessons ORDER BY position")
    fun getAllLessons(): List<Lesson>

}