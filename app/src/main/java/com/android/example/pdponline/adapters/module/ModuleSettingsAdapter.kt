package com.android.example.pdponline.adapters.module

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.databinding.ItemModuleSettingsBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Module
import java.io.File

class ModuleSettingsAdapter(
    private val course: Course,
    private val onItemClickListener: OnItemClickListener
) :
    ListAdapter<Module, ModuleSettingsAdapter.Vh>(
        MyDiffUtil()
    ) {

    inner class Vh(private val itemModuleSettingsBinding: ItemModuleSettingsBinding) :
        RecyclerView.ViewHolder(itemModuleSettingsBinding.root) {

        fun onBind(module: Module) {
            itemModuleSettingsBinding.name.text = module.name
            itemModuleSettingsBinding.posTv.text = module.position.toString()
            itemModuleSettingsBinding.image.setImageURI(Uri.fromFile(File(course.imagePath)))

            itemModuleSettingsBinding.delete.setOnClickListener {
                onItemClickListener.onDeleteListener(module)
            }
            itemModuleSettingsBinding.edit.setOnClickListener {
                onItemClickListener.onEditListener(module)
            }
            itemModuleSettingsBinding.card.setOnClickListener {
                onItemClickListener.onItemListener(module)
            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ItemModuleSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onItemListener(module: Module)

        fun onEditListener(module: Module)

        fun onDeleteListener(module: Module)
    }
}