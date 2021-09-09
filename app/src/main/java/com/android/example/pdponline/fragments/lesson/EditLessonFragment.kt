package com.android.example.pdponline.fragments.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentEditLessonBinding
import com.android.example.pdponline.models.Lesson

private const val ARG_PARAM1 = "lesson"

class EditLessonFragment : Fragment() {
    private var lesson: Lesson? = null
    private lateinit var binding: FragmentEditLessonBinding
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson = it.getSerializable(ARG_PARAM1) as Lesson?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditLessonBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = "${lesson?.position}-dars"
        binding.name.setText(lesson?.theme)
        binding.conspect.setText(lesson?.conspect)
        binding.position.setText(lesson?.position.toString())

        binding.editBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val conspect = binding.conspect.text.toString()
            val pos = binding.position.text.toString()
            lesson?.theme = name
            lesson?.conspect = conspect
            lesson?.position = pos.toInt()
            appDatabase.lessonDao().updateLesson(lesson!!)
            findNavController().popBackStack()
        }
        return binding.root
    }

}