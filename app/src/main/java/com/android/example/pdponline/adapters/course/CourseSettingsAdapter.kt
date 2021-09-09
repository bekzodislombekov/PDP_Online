package com.android.example.pdponline.adapters.course

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.databinding.ItemCourseSettingsBinding
import com.android.example.pdponline.models.Course
import java.io.File

class CourseSettingsAdapter(
    val onItemClickListener: OnItemClickListener
) :
    ListAdapter<Course, CourseSettingsAdapter.Vh>(
        MyDiffUtil()
    ) {

    inner class Vh(
        private val itemCourseSettingsBinding: ItemCourseSettingsBinding
    ) :
        RecyclerView.ViewHolder(itemCourseSettingsBinding.root) {

        fun bind(course: Course) {
            itemCourseSettingsBinding.image.setImageURI(Uri.fromFile(File(course.imagePath)))
            itemCourseSettingsBinding.name.text = course.name

            itemCourseSettingsBinding.card.setOnClickListener {
                onItemClickListener.onItemListener(course)
            }
            itemCourseSettingsBinding.delete.setOnClickListener {
                onItemClickListener.onDeleteListener(course)
            }
            itemCourseSettingsBinding.edit.setOnClickListener {
                onItemClickListener.onEditListener(course)
            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemCourseSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickListener {
        fun onItemListener(course: Course)

        fun onEditListener(course: Course)

        fun onDeleteListener(course: Course)
    }
}