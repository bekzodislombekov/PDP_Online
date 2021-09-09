package com.android.example.pdponline.fragments.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.R
import com.android.example.pdponline.adapters.lesson.LessonListAdapter
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentLessonListBinding
import com.android.example.pdponline.models.Lesson
import com.android.example.pdponline.models.Module

class LessonListFragment : Fragment() {
    private lateinit var binding: FragmentLessonListBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var lessonListAdapter: LessonListAdapter
    private var module: Module? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        module = arguments?.getSerializable("module") as Module?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLessonListBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = module?.name
        lessonListAdapter =
            LessonListAdapter(
                appDatabase.lessonDao().getAllLessonsByModule(module?.id!!),
                object : LessonListAdapter.OnItemClickListener {
                    override fun onItemListener(lesson: Lesson) {
                        val bundle = bundleOf("lesson" to lesson)
                        findNavController().navigate(R.id.lessonFragment, bundle)
                    }
                })

        binding.rv.adapter = lessonListAdapter
        return binding.root
    }

}