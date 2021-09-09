package com.android.example.pdponline.fragments.module

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.example.pdponline.R
import com.android.example.pdponline.adapters.module.ModuleSettingsAdapter
import com.android.example.pdponline.database.AppDatabase
import com.android.example.pdponline.databinding.FragmentModuleSettingsBinding
import com.android.example.pdponline.models.Course
import com.android.example.pdponline.models.Lesson
import com.android.example.pdponline.models.Module
import io.reactivex.Observable

private const val ARG_PARAM1 = "course"

class ModuleSettingsFragment : Fragment() {
    private var course: Course? = null
    private lateinit var binding: FragmentModuleSettingsBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var moduleSettingsAdapter: ModuleSettingsAdapter
    private lateinit var lessonList: ArrayList<Lesson>

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
        binding = FragmentModuleSettingsBinding.inflate(inflater, container, false)
        appDatabase = AppDatabase.getInstance(requireContext())

        binding.actionBarTv.text = course?.name
        binding.addBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val pos = binding.position.text.toString()
            val module = Module(name = name, position = pos.toInt(), courseId = course?.id!!)
            Observable.fromCallable {
                appDatabase.moduleDao().addModule(module)
                moduleSettingsAdapter.submitList(
                    appDatabase.moduleDao().getAllModulesByCourse(course?.id!!)
                )
            }.subscribe()
            binding.name.setText("")
            binding.position.setText("")
            binding.name.isFocusable = true
        }
        moduleSettingsAdapter =
            ModuleSettingsAdapter(
                course!!,
                object :
                    ModuleSettingsAdapter.OnItemClickListener {
                    override fun onItemListener(module: Module) {
                        val bundle = bundleOf("course" to course, "module" to module)
                        findNavController().navigate(R.id.lessonSettingsFragment, bundle)
                    }

                    override fun onEditListener(module: Module) {
                        val bundle = bundleOf("module" to module)
                        findNavController().navigate(R.id.editModuleFragment, bundle)
                    }

                    override fun onDeleteListener(module: Module) {
                        lessonList =
                            ArrayList(appDatabase.lessonDao().getAllLessonsByModule(module.id))
                        val alertDialog = AlertDialog.Builder(requireContext())
                        if (lessonList.size > 0) {
                            alertDialog.setMessage("Bu modul ichida darslar kiritilgan. Darslar bilan birgalikda o’chib ketishiga rozimisiz?")
                        } else {
                            alertDialog.setMessage("Modulni o'chirmoqchimisiz?")
                        }
                        alertDialog.setPositiveButton("Ha") { dialog, which ->
                            appDatabase.moduleDao().deleteModule(module)
                            moduleSettingsAdapter.submitList(
                                appDatabase.moduleDao().getAllModulesByCourse(course?.id!!)
                            )
                        }
                        alertDialog.setNegativeButton("No") { dialog, which ->
                            dialog.dismiss()
                        }
                        alertDialog.show()
                    }
                })
        moduleSettingsAdapter.submitList(
            appDatabase.moduleDao().getAllModulesByCourse(course?.id!!)
        )
        binding.rv.adapter = moduleSettingsAdapter

        return binding.root
    }

}