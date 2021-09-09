package com.android.example.pdponline.adapters.course

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.adapters.module.ModuleAdapter
import com.android.example.pdponline.databinding.ItemCourseBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Module

class CourseAdapter(
    private val listCourse: List<Course>,
    private val listModule: List<Module>,
    val onAllItemClickListener: OnAllItemClickListener,
    val onModuleItemClickListener: OnModuleItemClickListener
) :
    RecyclerView.Adapter<CourseAdapter.Vh>() {

    inner class Vh(private val itemCourseBinding: ItemCourseBinding) :
        RecyclerView.ViewHolder(itemCourseBinding.root) {

        fun onBind(course: Course, position: Int) {
            itemCourseBinding.categoryTv.text = course.name

            itemCourseBinding.showAllBtn.setOnClickListener {
                onAllItemClickListener.onItemListener(course)
            }

            val moduleList = ArrayList<Module>()
            for (module in listModule) {
                if (module.courseId == course.id) {
                    moduleList.add(module)
                }
            }

            val moduleAdapter =
                ModuleAdapter(
                    moduleList,
                    onModuleItemClickListener
                )
            itemCourseBinding.gameRv.adapter = moduleAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = listCourse.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(listCourse[position], position)
    }

    interface OnAllItemClickListener {
        fun onItemListener(course: Course)
    }

    interface OnModuleItemClickListener {
        fun onItemModuleListener(module: Module)
    }
}