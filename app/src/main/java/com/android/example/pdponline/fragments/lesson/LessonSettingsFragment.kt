package com.android.example.pdponline.fragments.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.R
import com.android.example.pdponline.adapters.lesson.LessonSettingsAdapter
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentLessonSettingsBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Lesson
import com.android.example.pdponline.models.Module
import io.reactivex.Observable

private const val ARG_PARAM1 = "course"
private const val ARG_PARAM2 = "module"

class LessonSettingsFragment : Fragment() {
    private var course: Course? = null
    private var module: Module? = null
    private lateinit var binding: FragmentLessonSettingsBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var lessonSettingsAdapter: LessonSettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable(ARG_PARAM1) as Course?
            module = it.getSerializable(ARG_PARAM2) as Module?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLessonSettingsBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = module?.name
        binding.addBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val conspect = binding.conspect.text.toString()
            val pos = binding.position.text.toString()
            val lesson =
                Lesson(
                    theme = name,
                    conspect = conspect,
                    position = pos.toInt(),
                    moduleId = module?.id!!
                )
            Observable.fromCallable {
                appDatabase.lessonDao().addLesson(lesson)
                lessonSettingsAdapter.submitList(
                    appDatabase.lessonDao().getAllLessonsByModule(module?.id!!)
                )
            }.subscribe()
            binding.name.setText("")
            binding.conspect.setText("")
            binding.position.setText("")
            binding.name.isFocusable = true
        }
        lessonSettingsAdapter =
            LessonSettingsAdapter(course!!, object : LessonSettingsAdapter.OnItemClickListener {
                override fun onDeleteListener(lesson: Lesson) {
                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setMessage("Dars o’chishiga rozimisiz?")
                    alertDialog.setPositiveButton("Ha") { dialog, which ->
                        appDatabase.lessonDao().deleteLesson(lesson)
                        lessonSettingsAdapter.submitList(
                            appDatabase.lessonDao().getAllLessonsByModule(module?.id!!)
                        )
                    }
                    alertDialog.setNegativeButton("Yo'q") { dialog, which ->
                        dialog.dismiss()
                    }
                    alertDialog.show()
                }

                override fun onEditListener(lesson: Lesson) {
                    val bundle = bundleOf("lesson" to lesson)
                    findNavController().navigate(R.id.editLessonFragment, bundle)
                }
            })
        lessonSettingsAdapter.submitList(
            appDatabase.lessonDao().getAllLessonsByModule(module?.id!!)
        )
        binding.rv.adapter = lessonSettingsAdapter
        return binding.root
    }
}