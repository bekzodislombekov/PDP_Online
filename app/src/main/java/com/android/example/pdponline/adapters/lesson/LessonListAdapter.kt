package com.android.example.pdponline.adapters.lesson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.example.pdponline.databinding.ItemLessonListBinding
import com.android.example.pdponline.models.Lesson

class LessonListAdapter(
    private val list: List<Lesson>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<LessonListAdapter.Vh>() {

    inner class Vh(private val itemLessonListBinding: ItemLessonListBinding) :
        RecyclerView.ViewHolder(itemLessonListBinding.root) {

        fun onBind(lesson: Lesson) {
            itemLessonListBinding.pos.text = lesson.position.toString()
            itemLessonListBinding.image.setOnClickListener {
                onItemClickListener.onItemListener(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):Vh = Vh(
        ItemLessonListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    interface OnItemClickListener {
        fun onItemListener(lesson: Lesson)
    }
}