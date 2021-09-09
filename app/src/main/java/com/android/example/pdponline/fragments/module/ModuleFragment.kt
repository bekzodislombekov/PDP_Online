package com.android.example.pdponline.fragments.module

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.R
import com.android.example.pdponline.adapters.module.ModuleByCourseAdapter
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentModuleBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Module

private const val ARG_PARAM1 = "course"

class ModuleFragment : Fragment() {
    private lateinit var binding: FragmentModuleBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var moduleByCourseAdapter: ModuleByCourseAdapter
    private var course: Course? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getSerializable(ARG_PARAM1) as Course?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModuleBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = course?.name
        moduleByCourseAdapter =
            ModuleByCourseAdapter(
                course!!,
                appDatabase.lessonDao().getAllLessons(),
                appDatabase.moduleDao().getAllModulesByCourse(course?.id!!),
                object : ModuleByCourseAdapter.OnItemClickListener {
                    override fun onItemListener(module: Module) {
                        val bundle = bundleOf("module" to module)
                        findNavController().navigate(R.id.lessonListFragment, bundle)
                    }
                })
        binding.rv.adapter = moduleByCourseAdapter
        return binding.root
    }
}