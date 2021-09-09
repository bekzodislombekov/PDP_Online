package com.android.example.pdponline.dao

import androidx.room.*
import com.android.example.pdponline.models.Module

@Dao
interface ModuleDao {

    @Insert
    fun addModule(module: Module)

    @Delete
    fun deleteModule(module: Module)

    @Update
    fun updateModule(module: Module)

    @Query("SELECT * FROM modules ORDER BY position")
    fun getAllModules(): List<Module>

    @Query("SELECT * FROM modules WHERE course_id =:courseId ORDER BY position")
    fun getAllModulesByCourse(courseId: Int): List<Module>
}