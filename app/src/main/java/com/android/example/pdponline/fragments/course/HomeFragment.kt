package com.android.example.pdponline.fragments.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.R
import com.android.example.pdponline.adapters.course.CourseAdapter
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentHomeBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Module

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: CourseAdapter
    private lateinit var appDatabase: AppDatabase
    private lateinit var courseList: ArrayList<Course>
    private lateinit var moduleList: ArrayList<Module>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())
        courseList = ArrayList(appDatabase.courseDao().getAllCourses())
        moduleList = ArrayList(appDatabase.moduleDao().getAllModules())

        adapter =
            CourseAdapter(
                courseList,
                moduleList,
                object :
                    CourseAdapter.OnAllItemClickListener {
                    override fun onItemListener(course: Course) {
                        val bundle = bundleOf("course" to course)
                        findNavController().navigate(R.id.moduleFragment, bundle)
                    }

                },
                object :
                    CourseAdapter.OnModuleItemClickListener {
                    override fun onItemModuleListener(module: Module) {
                        val bundle = bundleOf("module" to module)
                        findNavController().navigate(R.id.lessonListFragment, bundle)
                    }
                })
        binding.rv.adapter = adapter

        binding.settingsCourseBtn.setOnClickListener {
            findNavController().navigate(R.id.courseSettingsFragment)
        }
        return binding.root
    }
}