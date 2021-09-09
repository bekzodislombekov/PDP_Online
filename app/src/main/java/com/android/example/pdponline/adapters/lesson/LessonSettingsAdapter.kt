package com.android.example.pdponline.adapters.lesson

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.databinding.ItemLessonSettingsBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Lesson
import java.io.File

class LessonSettingsAdapter(
    private val course: Course,
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<Lesson, LessonSettingsAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(private val itemLessonSettingsBinding: ItemLessonSettingsBinding) :
        RecyclerView.ViewHolder(itemLessonSettingsBinding.root) {

        fun onBind(lesson: Lesson) {
            itemLessonSettingsBinding.name.text = "${lesson.position}-dars"
            itemLessonSettingsBinding.conspect.text = lesson.theme
            itemLessonSettingsBinding.image.setImageURI(Uri.fromFile(File(course.imagePath)))

            itemLessonSettingsBinding.delete.setOnClickListener {
                onItemClickListener.onDeleteListener(lesson)
            }
            itemLessonSettingsBinding.edit.setOnClickListener {
                onItemClickListener.onEditListener(lesson)
            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemLessonSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onDeleteListener(lesson: Lesson)

        fun onEditListener(lesson: Lesson)
    }
}