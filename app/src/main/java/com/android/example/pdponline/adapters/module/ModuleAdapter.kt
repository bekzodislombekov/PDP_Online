package com.android.example.pdponline.adapters.module

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.adapters.course.CourseAdapter
import com.android.example.pdponline.databinding.ItemModuleBinding
import com.android.example.pdponline.models.Module

class ModuleAdapter(
    private val list: List<Module>,
    val onModuleItemClickListener: CourseAdapter.OnModuleItemClickListener
) :
    RecyclerView.Adapter<ModuleAdapter.Vh>() {

    inner class Vh(private val itemModuleBinding: ItemModuleBinding) :
        RecyclerView.ViewHolder(itemModuleBinding.root) {
        fun onBind(module: Module, position: Int) {
            if (position == 0) {
                itemModuleBinding.container.setPadding(32, 0, 0, 0)
            } else if (position == list.size - 1) {
                itemModuleBinding.container.setPadding(0, 0, 32, 0)
            }
            itemModuleBinding.moduleTv.text = module.name
            itemModuleBinding.moduleCard.setOnClickListener {
                onModuleItemClickListener.onItemModuleListener(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemModuleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

}