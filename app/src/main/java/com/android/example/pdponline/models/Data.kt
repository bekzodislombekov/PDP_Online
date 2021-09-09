package com.android.example.pdponline.models

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var imagePath: String
) : Serializable

@Entity(
    tableName = "modules",
    foreignKeys = [ForeignKey(
        entity = Course::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("course_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Module(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var position: Int,
    @ColumnInfo(name = "course_id")
    var courseId: Int
) : Serializable

@Entity(
    tableName = "lessons",
    foreignKeys = [ForeignKey(
        entity = Module::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("module_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var theme: String,
    var conspect: String,
    var position: Int,
    @ColumnInfo(name = "module_id")
    val moduleId: Int
) : Serializable
