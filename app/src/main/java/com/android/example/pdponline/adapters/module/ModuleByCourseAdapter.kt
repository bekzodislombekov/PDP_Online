package com.android.example.pdponline.adapters.module

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.databinding.ItemModuleByCourseBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Lesson
import com.android.example.pdponline.models.Module
import java.io.File

class ModuleByCourseAdapter(
    private val course: Course,
    private val lessonList: List<Lesson>,
    private val moduleList: List<Module>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ModuleByCourseAdapter.Vh>() {

    inner class Vh(private val itemModuleByCourseBinding: ItemModuleByCourseBinding) :
        RecyclerView.ViewHolder(itemModuleByCourseBinding.root) {

        fun onBind(module: Module, position: Int) {
            itemModuleByCourseBinding.image.setImageURI(Uri.fromFile(File(course.imagePath)))
            itemModuleByCourseBinding.course.text = course.name
            itemModuleByCourseBinding.name.text = module.name
            val list = ArrayList<Lesson>()
            for (lesson in lessonList) {
                if (lesson.moduleId == module.id) {
                    list.add(lesson)
                }
            }
            itemModuleByCourseBinding.posTv.text = list.size.toString()
            itemModuleByCourseBinding.card.setOnClickListener {
                onItemClickListener.onItemListener(module)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemModuleByCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = moduleList.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(moduleList[position], position)
    }

    interface OnItemClickListener {
        fun onItemListener(module: Module)
    }
}