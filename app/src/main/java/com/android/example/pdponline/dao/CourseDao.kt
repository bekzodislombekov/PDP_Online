package com.android.example.pdponline.dao

import androidx.room.*
import com.android.example.pdponline.models.Course

@Dao
interface CourseDao {

    @Insert
    fun addCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Update
    fun updateCourse(course: Course)

    @Query("SELECT * FROM courses")
    fun getAllCourses(): List<Course>

}